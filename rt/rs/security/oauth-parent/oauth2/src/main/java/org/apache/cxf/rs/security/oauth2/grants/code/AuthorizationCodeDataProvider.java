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
name|grants
operator|.
name|code
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
name|rs
operator|.
name|security
operator|.
name|oauth2
operator|.
name|provider
operator|.
name|OAuthDataProvider
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
name|rs
operator|.
name|security
operator|.
name|oauth2
operator|.
name|provider
operator|.
name|OAuthServiceException
import|;
end_import

begin_comment
comment|/**  * AuthorizationCodeDataProvider is the {@link OAuthDataProvider} which  * can additionally persist the authorization code grant information   */
end_comment

begin_interface
specifier|public
interface|interface
name|AuthorizationCodeDataProvider
extends|extends
name|OAuthDataProvider
block|{
comment|/**      * Creates a temporarily code grant which will capture the      * information about the {@link Client} requesting the access to      * the resource owner's resources       * @param reg information about the client code grant request      * @return new code grant      * @see AuthorizationCodeRegistration      * @see ServerAuthorizationCodeGrant      * @throws OAuthServiceException      */
name|ServerAuthorizationCodeGrant
name|createCodeGrant
parameter_list|(
name|AuthorizationCodeRegistration
name|reg
parameter_list|)
throws|throws
name|OAuthServiceException
function_decl|;
comment|/**      * Returns the previously registered {@link ServerAuthorizationCodeGrant}      * @param code the code grant      * @return the grant      * @throws OAuthServiceException if no grant with this code is available      * @see ServerAuthorizationCodeGrant      */
name|ServerAuthorizationCodeGrant
name|removeCodeGrant
parameter_list|(
name|String
name|code
parameter_list|)
throws|throws
name|OAuthServiceException
function_decl|;
block|}
end_interface

end_unit

