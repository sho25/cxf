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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_comment
comment|/**  * Caches all events that do not have a listener associated with them.  * The events will be stored until the cache limit is reached.  * After reaching the cache size, events will be discarded using first in,  * first out semantics.  */
end_comment

begin_interface
specifier|public
interface|interface
name|EventCache
block|{
comment|/**      * Add the<code>Event</code> to the cache.      * If the maximum size of the cache is reached, the first<code>Event</code>      * added will be removed from the cache(FIFO)      * @param e The<code>Event</code> to be added to the cache.      */
name|void
name|addEvent
parameter_list|(
name|Event
name|e
parameter_list|)
function_decl|;
comment|/**      * Flushes the cache of all the<code>Event</code>s.      * @return List Containing the cached<code>Event</code>s.      */
name|List
argument_list|<
name|Event
argument_list|>
name|flushEvents
parameter_list|()
function_decl|;
comment|/**      * Flushes the<code>Event</code> from the cache matching the event type.      * @param eventType the<code>Event</code> type.      * @return List the list of<code>Event</code>s matching the event type.       */
name|List
argument_list|<
name|Event
argument_list|>
name|flushEvents
parameter_list|(
name|QName
name|eventType
parameter_list|)
function_decl|;
comment|/**      * Flushes the<code>Event</code>s from the cache matching the event type namespace.      * @param namespaceURI the<code>Event</code> type namespace.      * @return List the list of<code>Event</code>s matching the event type namespace.      */
name|List
argument_list|<
name|Event
argument_list|>
name|flushEvents
parameter_list|(
name|String
name|namespaceURI
parameter_list|)
function_decl|;
comment|/**      * Returns all the events. This method doesn't remove the      * events from the cache.      * @return List the list of all events stored in the cache.      */
name|List
argument_list|<
name|Event
argument_list|>
name|getEvents
parameter_list|()
function_decl|;
comment|/**      * Returns all the events matching the event type. This method doesn't      * remove the events from the cache.      * @param eventType the<code>Event</code> type.      * @return the list of<code>Event</code>s matching the event type.      */
name|List
argument_list|<
name|Event
argument_list|>
name|getEvents
parameter_list|(
name|QName
name|eventType
parameter_list|)
function_decl|;
comment|/**      * Returns all the events matching the event type namespace. This method doesn't      * remove the events from the cache.      * @param namespaceURI the<code>Event</code> type namespace.      * @return the list of<code>Event</code>s matching the event type namespace.      */
name|List
argument_list|<
name|Event
argument_list|>
name|getEvents
parameter_list|(
name|String
name|namespaceURI
parameter_list|)
function_decl|;
comment|/**      * Sets the cache size. This method can be used to dynamically change the      * cache size from the configured size.      * @param size Indicates the new size of the cache.      */
name|void
name|setCacheSize
parameter_list|(
name|int
name|size
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

