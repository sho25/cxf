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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|endpoint
operator|.
name|Server
import|;
end_import

begin_comment
comment|/**  * An inbound endpoint is a CXF service endpoint facade exposed by  * the JCA connector.  Its role is to accept service requests from   * ordinary CXF clients and forward them to an invoker (running in   * the context of the activating message driven bean).  The invoker  * either contains the service implementation or dispatches the call  * to a Stateless Session Bean.  This class holds objects that are   * needed to accomplish the task and provides a shutdown method to   * clean up the endpoint.   *   */
end_comment

begin_class
specifier|public
class|class
name|InboundEndpoint
block|{
specifier|private
name|Server
name|server
decl_stmt|;
specifier|private
name|MDBInvoker
name|invoker
decl_stmt|;
comment|/**      * @param server the server object that has already been started      * @param invoker the invoker that invoker an EJB      */
name|InboundEndpoint
parameter_list|(
name|Server
name|server
parameter_list|,
name|MDBInvoker
name|invoker
parameter_list|)
block|{
name|this
operator|.
name|server
operator|=
name|server
expr_stmt|;
name|this
operator|.
name|invoker
operator|=
name|invoker
expr_stmt|;
block|}
comment|/**      * @return the invoker      */
specifier|public
name|MDBInvoker
name|getInvoker
parameter_list|()
block|{
return|return
name|invoker
return|;
block|}
comment|/**      * @return the server      */
specifier|public
name|Server
name|getServer
parameter_list|()
block|{
return|return
name|server
return|;
block|}
comment|/**      * @param invoker the invoker to set      */
specifier|public
name|void
name|setInvoker
parameter_list|(
name|MDBInvoker
name|invoker
parameter_list|)
block|{
name|this
operator|.
name|invoker
operator|=
name|invoker
expr_stmt|;
block|}
comment|/**      * @param server the server to set      */
specifier|public
name|void
name|setServer
parameter_list|(
name|Server
name|server
parameter_list|)
block|{
name|this
operator|.
name|server
operator|=
name|server
expr_stmt|;
block|}
comment|/**      * Shuts down the endpoint      *       * @throws Exception      */
specifier|public
name|void
name|shutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|invoker
operator|=
literal|null
expr_stmt|;
if|if
condition|(
name|server
operator|!=
literal|null
condition|)
block|{
name|server
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|server
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

