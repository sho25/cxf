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
name|EventObject
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
comment|/**  * Base class for all the CXF Events.  */
end_comment

begin_class
specifier|public
class|class
name|Event
extends|extends
name|EventObject
block|{
comment|/*public static final String BUS_EVENT = "org.apache.cxf.bus.event";     public static final String COMPONENT_CREATED_EVENT = "COMPONENT_CREATED_EVENT";     public static final String COMPONENT_REMOVED_EVENT = "COMPONENT_REMOVED_EVENT";*/
specifier|private
name|QName
name|eventId
decl_stmt|;
comment|/**      * Constructs a<code>Event</code> with the event source and a unique event id.      * This id is used to identify the event type.      * @param source The<code>Object</code> representing the event information.      * @param id the QName identifying the event type      */
specifier|public
name|Event
parameter_list|(
name|Object
name|source
parameter_list|,
name|QName
name|id
parameter_list|)
block|{
name|super
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|eventId
operator|=
name|id
expr_stmt|;
block|}
comment|/**      * Returns the unique event id for this particular bus event.      * @return String The event id.      */
specifier|public
name|QName
name|getID
parameter_list|()
block|{
return|return
name|eventId
return|;
block|}
block|}
end_class

end_unit

