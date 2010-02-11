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
name|jaxrs
operator|.
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MultivaluedMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
operator|.
name|ResponseBuilder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|UriBuilder
import|;
end_import

begin_comment
comment|/**  * Represents the client state :  *  - baseURI  *  - current uri builder  *  - current requestHeaders,  *  - current response  */
end_comment

begin_interface
specifier|public
interface|interface
name|ClientState
block|{
comment|/**      * Sets the current builder      * @param currentBuilder the builder      */
name|void
name|setCurrentBuilder
parameter_list|(
name|UriBuilder
name|currentBuilder
parameter_list|)
function_decl|;
comment|/**      * Gets the current builder      * @return      */
name|UriBuilder
name|getCurrentBuilder
parameter_list|()
function_decl|;
comment|/**      * Sets the base URI      * @param baseURI baseURI      */
name|void
name|setBaseURI
parameter_list|(
name|URI
name|baseURI
parameter_list|)
function_decl|;
comment|/**      * Gets the base URI      * @return baseURI      */
name|URI
name|getBaseURI
parameter_list|()
function_decl|;
comment|/**      * Sets the responseBuilder      * @param responseBuilder responseBuilder      */
name|void
name|setResponseBuilder
parameter_list|(
name|ResponseBuilder
name|responseBuilder
parameter_list|)
function_decl|;
comment|/**      * Gets the responseBuilder      * @return responseBuilder      */
name|ResponseBuilder
name|getResponseBuilder
parameter_list|()
function_decl|;
comment|/**      * Sets the request headers      * @param requestHeaders request headers      */
name|void
name|setRequestHeaders
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|requestHeaders
parameter_list|)
function_decl|;
comment|/**      * Gets the request headers      * @return request headers, may be immutable      */
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getRequestHeaders
parameter_list|()
function_decl|;
comment|/**      * Resets the current state to the baseURI      *      */
name|void
name|reset
parameter_list|()
function_decl|;
comment|/**      * The factory method for creating a new state.       * Example, proxy and WebClient.fromClient will use this method when creating       * subresource proxies and new web clients respectively to ensure thet stay      * thread-local if needed      * @param baseURI baseURI      * @param headers request headers      * @return client state      */
name|ClientState
name|newState
parameter_list|(
name|URI
name|baseURI
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

