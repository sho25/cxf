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
name|jca
operator|.
name|inbound
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|resource
operator|.
name|spi
operator|.
name|endpoint
operator|.
name|MessageEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Exchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|invoker
operator|.
name|AbstractInvoker
import|;
end_import

begin_comment
comment|/**  * An invoker that supports direct Message Driven Bean invocation.    * It get invoked in the context of the Message Driven Bean that   * activates the CXF service endpoint facade.  Applications that put   * service implementation inside the Message Driven Bean that activates   * the inbound endpoint facade should choose this invoker.  It is   * more straightforward and faster than {@link DispatchMBDInvoker} but   * it requires to modify resource adapter's deployment descriptor (ra.xml)  * as the<messaging-type> needs to be set to the Service Endpoint Interface  * (SEI) class.  */
end_comment

begin_class
specifier|public
class|class
name|MDBInvoker
extends|extends
name|AbstractInvoker
block|{
specifier|private
name|MessageEndpoint
name|messageEndpoint
decl_stmt|;
comment|/**      * @param messageEndpoint      */
specifier|public
name|MDBInvoker
parameter_list|(
name|MessageEndpoint
name|messageEndpoint
parameter_list|)
block|{
name|this
operator|.
name|messageEndpoint
operator|=
name|messageEndpoint
expr_stmt|;
block|}
comment|/**      * @return the messageEndpoint      */
specifier|public
name|MessageEndpoint
name|getMessageEndpoint
parameter_list|()
block|{
return|return
name|messageEndpoint
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|getServiceObject
parameter_list|(
name|Exchange
name|context
parameter_list|)
block|{
return|return
name|messageEndpoint
return|;
block|}
block|}
end_class

end_unit

