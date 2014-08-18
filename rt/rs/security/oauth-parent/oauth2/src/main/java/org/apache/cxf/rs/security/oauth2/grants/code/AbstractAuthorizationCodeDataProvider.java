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
name|provider
operator|.
name|AbstractOAuthDataProvider
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
name|utils
operator|.
name|OAuthUtils
import|;
end_import

begin_comment
comment|/**  * Abstract AuthorizationCodeDataProvider implementation   */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractAuthorizationCodeDataProvider
extends|extends
name|AbstractOAuthDataProvider
implements|implements
name|AuthorizationCodeDataProvider
block|{
specifier|private
name|long
name|grantLifetime
init|=
literal|3600L
decl_stmt|;
specifier|public
name|ServerAuthorizationCodeGrant
name|createCodeGrant
parameter_list|(
name|AuthorizationCodeRegistration
name|reg
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
name|ServerAuthorizationCodeGrant
name|grant
init|=
operator|new
name|ServerAuthorizationCodeGrant
argument_list|(
name|reg
operator|.
name|getClient
argument_list|()
argument_list|,
name|getCode
argument_list|(
name|reg
argument_list|)
argument_list|,
name|getGrantLifetime
argument_list|()
argument_list|,
name|getIssuedAt
argument_list|()
argument_list|)
decl_stmt|;
name|grant
operator|.
name|setApprovedScopes
argument_list|(
name|getApprovedScopes
argument_list|(
name|reg
argument_list|)
argument_list|)
expr_stmt|;
name|grant
operator|.
name|setAudience
argument_list|(
name|reg
operator|.
name|getAudience
argument_list|()
argument_list|)
expr_stmt|;
name|grant
operator|.
name|setClientCodeVerifier
argument_list|(
name|reg
operator|.
name|getClientCodeVerifier
argument_list|()
argument_list|)
expr_stmt|;
name|grant
operator|.
name|setSubject
argument_list|(
name|reg
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
name|grant
operator|.
name|setRedirectUri
argument_list|(
name|reg
operator|.
name|getRedirectUri
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|grant
return|;
block|}
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|getApprovedScopes
parameter_list|(
name|AuthorizationCodeRegistration
name|reg
parameter_list|)
block|{
return|return
name|reg
operator|.
name|getApprovedScope
argument_list|()
return|;
block|}
specifier|protected
name|String
name|getCode
parameter_list|(
name|AuthorizationCodeRegistration
name|reg
parameter_list|)
block|{
return|return
name|OAuthUtils
operator|.
name|generateRandomTokenKey
argument_list|()
return|;
block|}
specifier|public
name|long
name|getGrantLifetime
parameter_list|()
block|{
return|return
name|grantLifetime
return|;
block|}
specifier|public
name|void
name|setGrantLifetime
parameter_list|(
name|long
name|lifetime
parameter_list|)
block|{
name|this
operator|.
name|grantLifetime
operator|=
name|lifetime
expr_stmt|;
block|}
specifier|protected
name|long
name|getIssuedAt
parameter_list|()
block|{
return|return
name|OAuthUtils
operator|.
name|getIssuedAt
argument_list|()
return|;
block|}
block|}
end_class

end_unit

