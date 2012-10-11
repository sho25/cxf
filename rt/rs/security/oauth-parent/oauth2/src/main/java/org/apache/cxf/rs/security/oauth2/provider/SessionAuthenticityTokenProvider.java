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
name|oauth2
operator|.
name|provider
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
name|jaxrs
operator|.
name|ext
operator|.
name|MessageContext
import|;
end_import

begin_comment
comment|/**  * SessionAuthenticityTokenProvider responsible for storing and retrieving tokens   * to validate the authenticity of request sessions  */
end_comment

begin_interface
specifier|public
interface|interface
name|SessionAuthenticityTokenProvider
block|{
comment|/**      * Creates a new session token and stores it      *       * @param mc the {@link MessageContext} of this request      * @return the created session token      */
name|String
name|createSessionToken
parameter_list|(
name|MessageContext
name|mc
parameter_list|)
function_decl|;
comment|/**      * Retrieves the stored session token      *       * @param mc the {@link MessageContext} of this request      * @return the stored token      */
name|String
name|getSessionToken
parameter_list|(
name|MessageContext
name|mc
parameter_list|)
function_decl|;
comment|/**      * Removes the stored session token      *       * @param mc the {@link MessageContext} of this request      */
name|String
name|removeSessionToken
parameter_list|(
name|MessageContext
name|mc
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

