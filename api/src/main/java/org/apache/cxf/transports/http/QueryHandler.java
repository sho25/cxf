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
name|transports
operator|.
name|http
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|model
operator|.
name|EndpointInfo
import|;
end_import

begin_interface
specifier|public
interface|interface
name|QueryHandler
block|{
comment|/**      * @param fullQueryString the target full query string (with params) of the request      * @param ctx the context that was set for this invokation      * @param endpoint the current endpoint for this context (e.g. the endpoint this      * Destination was activated for). Null if no current endpoint.      * @return true iff the URI is a recognized WSDL query      */
name|boolean
name|isRecognizedQuery
parameter_list|(
name|String
name|fullQueryString
parameter_list|,
name|String
name|ctx
parameter_list|,
name|EndpointInfo
name|endpoint
parameter_list|)
function_decl|;
comment|/**      * @param fullQueryString the target full query string (with params) of the request      * @param ctx the context that was set for this invokation      * @return the content-type for the response      */
name|String
name|getResponseContentType
parameter_list|(
name|String
name|fullQueryString
parameter_list|,
name|String
name|ctx
parameter_list|)
function_decl|;
comment|/**      * Write query response to output stream      * @param fullQueryString the target full query string (with params) of the request      * @param ctx the context that was set for this invokation      * @param endpoint the current endpoint for this context (e.g. the endpoint this      * Destination was activated for). Null if no current endpoint.      */
name|void
name|writeResponse
parameter_list|(
name|String
name|fullQueryString
parameter_list|,
name|String
name|ctx
parameter_list|,
name|EndpointInfo
name|endpoint
parameter_list|,
name|OutputStream
name|os
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

