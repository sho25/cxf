begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|event
package|;
end_package

begin_interface
specifier|public
interface|interface
name|EventProcessor
block|{
comment|/**      * Sends an event to the processor.      * @param e the event      */
name|void
name|sendEvent
parameter_list|(
name|Event
name|e
parameter_list|)
function_decl|;
comment|/**      * Registers an event listener with this event processor.      * @param listener the event listener      */
name|void
name|addEventListener
parameter_list|(
name|EventListener
name|listener
parameter_list|)
function_decl|;
comment|/**      * Registers an event listener with this event processor. The listener will      * only be notified when the event passes through the specified filter.      * @param listener the event listener      * @param filter the event filter      */
name|void
name|addEventListener
parameter_list|(
name|EventListener
name|listener
parameter_list|,
name|EventFilter
name|filter
parameter_list|)
function_decl|;
comment|/**      * Unregisters an event listener.      * @param listener the event listener      */
name|void
name|removeEventListener
parameter_list|(
name|EventListener
name|listener
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

