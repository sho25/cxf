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
name|java
operator|.
name|util
operator|.
name|List
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
name|common
operator|.
name|OAuthPermission
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
comment|/**  * OAuth provider responsible for persisting the information about   * OAuth consumers, request and access tokens.  */
end_comment

begin_interface
specifier|public
interface|interface
name|AuthorizationCodeDataProvider
extends|extends
name|OAuthDataProvider
block|{
comment|/**      * Converts the requested scope to the list of permissions        * @param requestedScope      * @return list of permissions      */
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|convertScopeToPermissions
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|requestedScope
parameter_list|)
function_decl|;
comment|/**      * Creates a temporarily code grant which will capture the      * information about the {@link Client} attempting to access or      * modify the resource owner's resource       * @param reg AuthorizationCodeRegistration      * @return new code grant      * @see AuthorizationCodeRegistration      * @throws OAuthServiceException      */
name|ServerAuthorizationCodeGrant
name|createCodeGrant
parameter_list|(
name|AuthorizationCodeRegistration
name|reg
parameter_list|)
throws|throws
name|OAuthServiceException
function_decl|;
comment|/**      * Returns the previously registered {@link ServerAuthorizationCodeGrant}      * @param code the code grant      * @return AuthorizationCodeGrant      * @throws OAuthServiceException      */
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

