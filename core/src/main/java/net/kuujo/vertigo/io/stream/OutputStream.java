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
package net.kuujo.vertigo.io.stream;

import net.kuujo.vertigo.io.Closeable;
import net.kuujo.vertigo.io.Openable;
import net.kuujo.vertigo.io.Output;

/**
 * Output stream.<p>
 *
 * The output stream represents a group of connections between the current
 * component instance and multiple instances of another component. Each stream
 * uses an internal {@link net.kuujo.vertigo.io.selector.Selector} to select
 * connections to which to send each message. Each message sent on a stream
 * can be sent to a single connection or it can be copied to multiple connections
 * based on the selector implementation.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
public interface OutputStream extends Output<OutputStream>, Openable<OutputStream>, Closeable<OutputStream> {

  /**
   * Returns the output stream address.
   *
   * @return The output stream address.
   */
  String address();

}