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
package net.kuujo.vertigo.output.stream;

import io.vertx.codegen.annotations.VertxGen;
import net.kuujo.vertigo.Context;
import net.kuujo.vertigo.output.connection.OutputConnectionContext;
import net.kuujo.vertigo.output.partitioner.Partitioner;
import net.kuujo.vertigo.output.port.OutputPortContext;
import net.kuujo.vertigo.output.stream.impl.OutputStreamContextImpl;

import java.util.Collection;
import java.util.List;

/**
 * The output stream context represents a set of output connections
 * from one component partition to all partitions of another component.
 * The context contains information about how to dispatch messages
 * between the group of target component partitions.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
@VertxGen
public interface OutputStreamContext extends Context<OutputStreamContext> {

  /**
   * Returns a new output stream context builder.
   *
   * @return A new output stream context builder.
   */
  static Builder builder() {
    return new OutputStreamContextImpl.Builder();
  }

  /**
   * Returns a new output stream context builder.
   *
   * @param stream An existing output stream context object to wrap.
   * @return An output stream builder wrapper.
   */
  static Builder builder(OutputStreamContext stream) {
    return new OutputStreamContextImpl.Builder((OutputStreamContextImpl) stream);
  }

  /**
   * Returns the parent output port context.
   *
   * @return The parent port context.
   */
  OutputPortContext port();

  /**
   * Returns the stream connection partitioner.
   *
   * @return The stream connection partitioner.
   */
  Partitioner partitioner();

  /**
   * Returns a list of output connections.
   *
   * @return A list of output connections.
   */
  List<OutputConnectionContext> connections();

  /**
   * Output stream context builder.
   */
  public static interface Builder extends Context.Builder<Builder, OutputStreamContext> {

    /**
     * Adds a connection to the stream.
     *
     * @param connection The output connection context to add.
     * @return The output stream context builder.
     */
    Builder addConnection(OutputConnectionContext connection);

    /**
     * Removes a connection from the stream.
     *
     * @param connection The output connection context to remove.
     * @return The output stream context builder.
     */
    Builder removeConnection(OutputConnectionContext connection);

    /**
     * Sets all connections on the stream.
     *
     * @param connections A collection of output connection context to add.
     * @return The output stream context builder.
     */
    Builder setConnections(OutputConnectionContext... connections);

    /**
     * Sets all connections on the stream.
     *
     * @param connections A collection of output connection context to add.
     * @return The output stream context builder.
     */
    Builder setConnections(Collection<OutputConnectionContext> connections);

    /**
     * Sets the parent output port.
     *
     * @param port The output port context.
     * @return The output stream context builder.
     */
    Builder setPort(OutputPortContext port);
  }

}