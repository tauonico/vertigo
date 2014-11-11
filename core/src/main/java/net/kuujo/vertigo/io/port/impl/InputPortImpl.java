/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.kuujo.vertigo.io.port.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.impl.LoggerFactory;
import net.kuujo.vertigo.io.ControllableInput;
import net.kuujo.vertigo.io.VertigoMessage;
import net.kuujo.vertigo.io.connection.InputConnection;
import net.kuujo.vertigo.io.connection.InputConnectionContext;
import net.kuujo.vertigo.io.connection.impl.InputConnectionImpl;
import net.kuujo.vertigo.io.port.InputPort;
import net.kuujo.vertigo.io.port.InputPortContext;
import net.kuujo.vertigo.util.Closeable;
import net.kuujo.vertigo.util.CountingCompletionHandler;
import net.kuujo.vertigo.util.Openable;
import net.kuujo.vertigo.util.TaskRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Input port implementation.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
public class InputPortImpl<T> implements InputPort<T>, ControllableInput<InputPort<T>, T>, Openable<InputPort<T>>, Closeable<InputPort<T>> {
  private static final Logger log = LoggerFactory.getLogger(InputPortImpl.class);
  private final Vertx vertx;
  private InputPortContext info;
  private final List<InputConnectionImpl<T>> connections = new ArrayList<>();
  private final TaskRunner tasks = new TaskRunner();
  @SuppressWarnings("rawtypes")
  private Handler<VertigoMessage<T>> messageHandler;
  private boolean open;
  private boolean paused;

  public InputPortImpl(Vertx vertx, InputPortContext info) {
    this.vertx = vertx;
    this.info = info;
  }

  @Override
  public String name() {
    return info.name();
  }

  @Override
  public InputPort<T> pause() {
    paused = true;
    for (InputConnection connection : connections) {
      connection.pause();
    }
    return this;
  }

  @Override
  public InputPort<T> resume() {
    paused = false;
    for (InputConnection connection : connections) {
      connection.resume();
    }
    return this;
  }

  @Override
  public InputPort<T> messageHandler(final Handler<VertigoMessage<T>> handler) {
    if (open && handler == null) {
      throw new IllegalStateException("cannot unset handler on locked port");
    }
    this.messageHandler = handler;
    for (InputConnection<T> connection : connections) {
      connection.messageHandler(messageHandler);
    }
    return this;
  }

  @Override
  public InputPort<T> open() {
    return open(null);
  }

  @Override
  public InputPort<T> open(final Handler<AsyncResult<Void>> doneHandler) {
    // Prevent the object from being opened and closed simultaneously
    // by queueing open/close operations as tasks.
    tasks.runTask((task) -> {
      if (!open) {
        final CountingCompletionHandler<Void> startCounter = new CountingCompletionHandler<Void>(info.connections().size());
        startCounter.setHandler((result) -> {
          if (result.succeeded()) {
            open = true;
          }
          doneHandler.handle(result);
          task.complete();
        });

        // Add all connections from the connection context.
        // Only add connections to the connections list once the connection
        // has been opened. This ensures that we don't attempt to send messages
        // on a close connection.
        connections.clear();
        for (InputConnectionContext connectionInfo : info.connections()) {
          final InputConnectionImpl<T> connection = new InputConnectionImpl<>(vertx, connectionInfo);
          connection.open((result) -> {
            if (result.failed()) {
              log.error(String.format("%s - Failed to open input connection: %s", InputPortImpl.this, connection));
              startCounter.fail(result.cause());
            } else {
              log.info(String.format("%s - Opened input connection: %s", InputPortImpl.this, connection));
              connections.add(setupConnection(connection));
              startCounter.succeed();
            }
          });
        }
      } else {
        doneHandler.handle(Future.completedFuture());
        task.complete();
      }
    });
    return this;
  }

  /**
   * Sets up the given connection.
   */
  @SuppressWarnings("unchecked")
  private InputConnectionImpl<T> setupConnection(InputConnectionImpl<T> connection) {
    log.debug(String.format("%s - Setting up connection: %s", this, connection));
    connection.messageHandler(messageHandler);
    if (paused) {
      connection.pause();
    }
    return connection;
  }

  @Override
  public void close() {
    close(null);
  }

  @Override
  public void close(final Handler<AsyncResult<Void>> doneHandler) {
    // Prevent the object from being opened and closed simultaneously
    // by queueing open/close operations as tasks.
    tasks.runTask((task) -> {
      if (open) {
        final CountingCompletionHandler<Void> stopCounter = new CountingCompletionHandler<Void>(connections.size());
        stopCounter.setHandler((result) -> {
          if (result.succeeded()) {
            connections.clear();
          }
          open = false;
          doneHandler.handle(result);
          task.complete();
        });

        for (final InputConnectionImpl connection : connections) {
          connection.close((Handler<AsyncResult<Void>>)(result) -> {
            if (result.failed()) {
              log.warn(String.format("%s - Failed to close input connection: %s", InputPortImpl.this, connection));
              stopCounter.fail(result.cause());
            } else {
              log.info(String.format("%s - Closed input connection: %s", InputPortImpl.this, connection));
              stopCounter.succeed();
            }
          });
        }
      } else {
        doneHandler.handle(Future.completedFuture());
        task.complete();
      }
    });
  }

  @Override
  public String toString() {
    return info.toString();
  }

}