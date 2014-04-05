/*
 * Copyright 2013 the original author or authors.
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
package net.kuujo.vertigo.output.selector;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import net.kuujo.vertigo.message.JsonMessage;
import net.kuujo.vertigo.util.serializer.JsonSerializable;

/**
 * An output selector.
 *
 * Output selectors are the counterparts to input groupings. When an input is
 * used to subscribe to the output of another component, the input's grouping
 * is converted to an output {@link MessageSelector}. Each time a message is emitted
 * to the resulting output channel, the selector is used to select which
 * {@link ConnectionConfig}s to which to send the message.
 *
 * @author Jordan Halterman
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="type")
@JsonSubTypes({
  @JsonSubTypes.Type(value=RandomSelector.class, name="random"),
  @JsonSubTypes.Type(value=RoundSelector.class, name="round"),
  @JsonSubTypes.Type(value=FieldsSelector.class, name="fields"),
  @JsonSubTypes.Type(value=AllSelector.class, name="all")
})
public interface MessageSelector extends JsonSerializable {

  /**
   * Selects a list of connections to which to emit messages.
   *
   * @param message
   *   The message being emitted.
   * @param targets
   *   A list of targets from which to select.
   * @return
   *   A list of selected connections.
   */
  List<String> select(JsonMessage message, List<String> targets);

}