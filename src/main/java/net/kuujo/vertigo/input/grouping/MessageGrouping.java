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
package net.kuujo.vertigo.input.grouping;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import net.kuujo.vertigo.output.selector.MessageSelector;
import net.kuujo.vertigo.util.serializer.JsonSerializable;

/**
 * An input grouping.<p>
 *
 * Input groupings define how messages are to be distributed among multiple
 * instance of a component. When an input prepares to register with the interesting
 * component's output, the grouping will be converted into a {@link MessageSelector}.
 * This selector is used to select specific component instances to which to send
 * each message.
 *
 * @author Jordan Halterman
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="type")
@JsonSubTypes({
  @JsonSubTypes.Type(value=RandomGrouping.class, name="random"),
  @JsonSubTypes.Type(value=RoundGrouping.class, name="round"),
  @JsonSubTypes.Type(value=FieldsGrouping.class, name="fields"),
  @JsonSubTypes.Type(value=AllGrouping.class, name="all")
})
public interface MessageGrouping extends JsonSerializable {

  /**
   * Creates an output selector from the grouping.
   *
   * @return
   *   An output selector.
   */
  MessageSelector createSelector();

}