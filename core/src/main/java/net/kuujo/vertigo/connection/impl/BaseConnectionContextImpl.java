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

package net.kuujo.vertigo.connection.impl;

import net.kuujo.vertigo.connection.ConnectionContext;
import net.kuujo.vertigo.connection.TargetContext;
import net.kuujo.vertigo.impl.BaseContextImpl;
import net.kuujo.vertigo.connection.SourceContext;

/**
 * Connection context implementation.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
public abstract class BaseConnectionContextImpl<T extends ConnectionContext<T>> extends BaseContextImpl<T> implements ConnectionContext<T> {
  protected String address;
  protected SourceContext source;
  protected TargetContext target;

  @Override
  public String address() {
    return address;
  }

  @Override
  public SourceContext source() {
    return source;
  }

  @Override
  public TargetContext target() {
    return target;
  }

}