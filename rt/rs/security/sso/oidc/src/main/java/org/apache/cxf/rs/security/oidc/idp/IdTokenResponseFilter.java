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
name|oidc
operator|.
name|idp
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|jose
operator|.
name|jwa
operator|.
name|SignatureAlgorithm
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
name|jose
operator|.
name|jws
operator|.
name|JwsUtils
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
name|jose
operator|.
name|jwt
operator|.
name|JwtToken
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
name|ClientAccessToken
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
name|ServerAccessToken
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
name|AbstractOAuthServerJoseJwtProducer
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
name|AccessTokenResponseFilter
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
name|oidc
operator|.
name|common
operator|.
name|IdToken
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
name|oidc
operator|.
name|utils
operator|.
name|OidcUtils
import|;
end_import

begin_class
specifier|public
class|class
name|IdTokenResponseFilter
extends|extends
name|AbstractOAuthServerJoseJwtProducer
implements|implements
name|AccessTokenResponseFilter
block|{
specifier|private
name|UserInfoProvider
name|userInfoProvider
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|ClientAccessToken
name|ct
parameter_list|,
name|ServerAccessToken
name|st
parameter_list|)
block|{
comment|// Only add an IdToken if the client has the "openid" scope
if|if
condition|(
name|ct
operator|.
name|getApprovedScope
argument_list|()
operator|==
literal|null
operator|||
operator|!
name|ct
operator|.
name|getApprovedScope
argument_list|()
operator|.
name|contains
argument_list|(
name|OidcUtils
operator|.
name|OPENID_SCOPE
argument_list|)
condition|)
block|{
return|return;
block|}
name|String
name|idToken
init|=
name|getProcessedIdToken
argument_list|(
name|st
argument_list|)
decl_stmt|;
if|if
condition|(
name|idToken
operator|!=
literal|null
condition|)
block|{
name|ct
operator|.
name|getParameters
argument_list|()
operator|.
name|put
argument_list|(
name|OidcUtils
operator|.
name|ID_TOKEN
argument_list|,
name|idToken
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|getProcessedIdToken
parameter_list|(
name|ServerAccessToken
name|st
parameter_list|)
block|{
if|if
condition|(
name|userInfoProvider
operator|!=
literal|null
condition|)
block|{
name|IdToken
name|idToken
init|=
name|userInfoProvider
operator|.
name|getIdToken
argument_list|(
name|st
operator|.
name|getClient
argument_list|()
operator|.
name|getClientId
argument_list|()
argument_list|,
name|st
operator|.
name|getSubject
argument_list|()
argument_list|,
name|st
operator|.
name|getScopes
argument_list|()
argument_list|)
decl_stmt|;
name|setAtHashAndNonce
argument_list|(
name|idToken
argument_list|,
name|st
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|processJwt
argument_list|(
operator|new
name|JwtToken
argument_list|(
name|idToken
argument_list|)
argument_list|,
name|st
operator|.
name|getClient
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|st
operator|.
name|getSubject
argument_list|()
operator|.
name|getProperties
argument_list|()
operator|.
name|containsKey
argument_list|(
name|OidcUtils
operator|.
name|ID_TOKEN
argument_list|)
condition|)
block|{
return|return
name|st
operator|.
name|getSubject
argument_list|()
operator|.
name|getProperties
argument_list|()
operator|.
name|get
argument_list|(
name|OidcUtils
operator|.
name|ID_TOKEN
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|st
operator|.
name|getSubject
argument_list|()
operator|instanceof
name|OidcUserSubject
condition|)
block|{
name|OidcUserSubject
name|sub
init|=
operator|(
name|OidcUserSubject
operator|)
name|st
operator|.
name|getSubject
argument_list|()
decl_stmt|;
name|IdToken
name|idToken
init|=
operator|new
name|IdToken
argument_list|(
name|sub
operator|.
name|getIdToken
argument_list|()
argument_list|)
decl_stmt|;
name|setAtHashAndNonce
argument_list|(
name|idToken
argument_list|,
name|st
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|processJwt
argument_list|(
operator|new
name|JwtToken
argument_list|(
name|idToken
argument_list|)
argument_list|,
name|st
operator|.
name|getClient
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|private
name|void
name|setAtHashAndNonce
parameter_list|(
name|IdToken
name|idToken
parameter_list|,
name|ServerAccessToken
name|st
parameter_list|)
block|{
if|if
condition|(
name|idToken
operator|.
name|getAccessTokenHash
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Properties
name|props
init|=
name|JwsUtils
operator|.
name|loadSignatureOutProperties
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|SignatureAlgorithm
name|sigAlgo
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|super
operator|.
name|isSignWithClientSecret
argument_list|()
condition|)
block|{
name|sigAlgo
operator|=
name|OAuthUtils
operator|.
name|getClientSecretSignatureAlgorithm
argument_list|(
name|props
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sigAlgo
operator|=
name|JwsUtils
operator|.
name|getSignatureAlgorithm
argument_list|(
name|props
argument_list|,
name|SignatureAlgorithm
operator|.
name|RS256
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sigAlgo
operator|!=
name|SignatureAlgorithm
operator|.
name|NONE
condition|)
block|{
name|String
name|atHash
init|=
name|OidcUtils
operator|.
name|calculateAccessTokenHash
argument_list|(
name|st
operator|.
name|getTokenKey
argument_list|()
argument_list|,
name|sigAlgo
argument_list|)
decl_stmt|;
name|idToken
operator|.
name|setAccessTokenHash
argument_list|(
name|atHash
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|idToken
operator|.
name|getNonce
argument_list|()
operator|==
literal|null
operator|&&
name|st
operator|.
name|getNonce
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|idToken
operator|.
name|setNonce
argument_list|(
name|st
operator|.
name|getNonce
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setUserInfoProvider
parameter_list|(
name|UserInfoProvider
name|userInfoProvider
parameter_list|)
block|{
name|this
operator|.
name|userInfoProvider
operator|=
name|userInfoProvider
expr_stmt|;
block|}
block|}
end_class

end_unit

