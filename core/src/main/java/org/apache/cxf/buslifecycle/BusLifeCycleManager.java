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
name|buslifecycle
package|;
end_package

begin_comment
comment|/**  * The manager interface for registering<code>BusLifeCycleListener</code>s.  *  * A class that implements the BusLifeCycleListener interface can be  * registered or unregistered to receive notification of<code>Bus</code>  * lifecycle events.  */
end_comment

begin_interface
specifier|public
interface|interface
name|BusLifeCycleManager
extends|extends
name|BusLifeCycleListener
block|{
comment|/**      * Register a listener to receive<code>Bus</code> lifecycle notification.      *      * @param listener The<code>BusLifeCycleListener</code> that will      * receive the events.      */
name|void
name|registerLifeCycleListener
parameter_list|(
name|BusLifeCycleListener
name|listener
parameter_list|)
function_decl|;
comment|/**      * Unregister a listener so that it will no longer receive<code>Bus</code>      * lifecycle events.      *      * @param listener The<code>BusLifeCycleListener</code> to unregister.      */
name|void
name|unregisterLifeCycleListener
parameter_list|(
name|BusLifeCycleListener
name|listener
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

