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
package net.kuujo.vertigo.input.connection;

import net.kuujo.vertigo.Context;
import net.kuujo.vertigo.connection.ConnectionContext;
import net.kuujo.vertigo.connection.SourceContext;
import net.kuujo.vertigo.connection.TargetContext;
import net.kuujo.vertigo.input.connection.impl.InputConnectionContextImpl;
import net.kuujo.vertigo.input.port.InputPortContext;

/**
 * Input connection context represents a single partition's input from
 * an partition of another component.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
public interface InputConnectionContext extends ConnectionContext<InputConnectionContext> {

  /**
   * Returns a new input connection context builder.
   *
   * @return A new input connection context builder.
   */
  static Builder builder() {
    return new InputConnectionContextImpl.Builder();
  }

  /**
   * Returns a new input connection context builder.
   *
   * @param connection An existing input connection context object to wrap.
   * @return An input connection context builder wrapper.
   */
  static Builder builder(InputConnectionContext connection) {
    return new InputConnectionContextImpl.Builder((InputConnectionContextImpl) connection);
  }

  /**
   * Returns the parent input port context.
   *
   * @return The parent port context.
   */
  InputPortContext port();

  /**
   * Input connection context builder.
   */
  public static interface Builder extends Context.Builder<Builder, InputConnectionContext> {

    /**
     * Sets the connection address.
     *
     * @param address The connection address.
     * @return The input connection context builder.
     */
    Builder setAddress(String address);

    /**
     * Sets the connection source context.
     *
     * @param source The connection source context.
     * @return The input connection context builder.
     */
    Builder setSource(SourceContext source);

    /**
     * Sets the connection target context.
     *
     * @param target The connection target context.
     * @return The input connection context builder.
     */
    Builder setTarget(TargetContext target);

    /**
     * Sets the parent input port context.
     *
     * @param port The parent input port context.
     * @return The input connection context builder.
     */
    Builder setPort(InputPortContext port);
  }

}