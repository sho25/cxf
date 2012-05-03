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
name|rs
operator|.
name|security
operator|.
name|saml
operator|.
name|sso
operator|.
name|state
package|;
end_package

begin_comment
comment|/**  * SSO Service Provider State Manager.  *   * TODO: review the possibility of working with the Servlet HTTPSession  * instead; in that case it can be tricky to configure various containers   * (Tomcat, Jetty) to make sure the cookies are shared across multiple   * war contexts which will be needed if RequestAssertionConsumerService  * needs to be run in its own war file instead of having every application   * war on the SP side have a dedicated RequestAssertionConsumerService endpoint     */
end_comment

begin_interface
specifier|public
interface|interface
name|SPStateManager
block|{
name|void
name|setRequestState
parameter_list|(
name|String
name|relayState
parameter_list|,
name|RequestState
name|state
parameter_list|)
function_decl|;
name|RequestState
name|removeRequestState
parameter_list|(
name|String
name|relayState
parameter_list|)
function_decl|;
name|void
name|setResponseState
parameter_list|(
name|String
name|contextKey
parameter_list|,
name|ResponseState
name|state
parameter_list|)
function_decl|;
name|ResponseState
name|getResponseState
parameter_list|(
name|String
name|contextKey
parameter_list|)
function_decl|;
name|ResponseState
name|removeResponseState
parameter_list|(
name|String
name|contextKey
parameter_list|)
function_decl|;
name|void
name|close
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

