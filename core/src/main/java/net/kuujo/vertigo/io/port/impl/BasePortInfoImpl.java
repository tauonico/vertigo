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

import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonObject;
import net.kuujo.vertigo.VertigoException;
import net.kuujo.vertigo.io.port.PortInfo;

/**
 * Port info implementation.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
abstract class BasePortInfoImpl<T extends PortInfo<T>> implements PortInfo<T> {
  private String name;
  private Class<?> type;
  private Class<? extends MessageCodec> codec;

  protected BasePortInfoImpl(String name, Class<?> type) {
    this.name = name;
    this.type = type;
  }

  protected BasePortInfoImpl(JsonObject port) {
    this.name = port.getString("name");
    String type = port.getString("type");
    if (type != null) {
      try {
        this.type = Class.forName(type);
      } catch (ClassNotFoundException e) {
        throw new VertigoException(e);
      }
    }
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  @SuppressWarnings("unchecked")
  public T setName(String name) {
    this.name = name;
    return (T) this;
  }

  @Override
  public Class<?> getType() {
    return type;
  }

  @Override
  @SuppressWarnings("unchecked")
  public T setType(Class<?> type) {
    this.type = type;
    return (T) this;
  }

  @Override
  public Class<? extends MessageCodec> getCodec() {
    return codec;
  }

  @Override
  @SuppressWarnings("unchecked")
  public T setCodec(Class<? extends MessageCodec> codec) {
    this.codec = codec;
    return (T) this;
  }
}
