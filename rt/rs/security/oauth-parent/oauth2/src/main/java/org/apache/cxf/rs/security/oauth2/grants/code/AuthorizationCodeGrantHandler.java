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
name|Collections
import|;
end_import

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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|utils
operator|.
name|JAXRSUtils
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
name|AccessTokenRegistration
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
name|Client
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
name|grants
operator|.
name|AbstractGrantHandler
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
name|OAuthConstants
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
comment|/**  * Authorization Code Grant Handler  */
end_comment

begin_class
specifier|public
class|class
name|AuthorizationCodeGrantHandler
extends|extends
name|AbstractGrantHandler
block|{
specifier|private
name|CodeVerifierTransformer
name|codeVerifierTransformer
decl_stmt|;
specifier|private
name|boolean
name|expectCodeVerifierForPublicClients
decl_stmt|;
specifier|public
name|AuthorizationCodeGrantHandler
parameter_list|()
block|{
name|super
argument_list|(
name|OAuthConstants
operator|.
name|AUTHORIZATION_CODE_GRANT
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ServerAccessToken
name|createAccessToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
comment|// Get the grant representation from the provider
name|String
name|codeValue
init|=
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|AUTHORIZATION_CODE_VALUE
argument_list|)
decl_stmt|;
name|ServerAuthorizationCodeGrant
name|grant
init|=
operator|(
operator|(
name|AuthorizationCodeDataProvider
operator|)
name|getDataProvider
argument_list|()
operator|)
operator|.
name|removeCodeGrant
argument_list|(
name|codeValue
argument_list|)
decl_stmt|;
if|if
condition|(
name|grant
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// check it has not expired, the client ids are the same
if|if
condition|(
name|OAuthUtils
operator|.
name|isExpired
argument_list|(
name|grant
operator|.
name|getIssuedAt
argument_list|()
argument_list|,
name|grant
operator|.
name|getExpiresIn
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
name|OAuthConstants
operator|.
name|INVALID_GRANT
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|grant
operator|.
name|getClient
argument_list|()
operator|.
name|getClientId
argument_list|()
operator|.
name|equals
argument_list|(
name|client
operator|.
name|getClientId
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
name|OAuthConstants
operator|.
name|INVALID_GRANT
argument_list|)
throw|;
block|}
comment|// redirect URIs must match too
name|String
name|expectedRedirectUri
init|=
name|grant
operator|.
name|getRedirectUri
argument_list|()
decl_stmt|;
name|String
name|providedRedirectUri
init|=
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|REDIRECT_URI
argument_list|)
decl_stmt|;
if|if
condition|(
name|providedRedirectUri
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|expectedRedirectUri
operator|==
literal|null
operator|||
operator|!
name|providedRedirectUri
operator|.
name|equals
argument_list|(
name|expectedRedirectUri
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
name|OAuthConstants
operator|.
name|INVALID_REQUEST
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
name|expectedRedirectUri
operator|==
literal|null
operator|&&
operator|!
name|isCanSupportPublicClients
argument_list|()
operator|||
name|expectedRedirectUri
operator|!=
literal|null
operator|&&
operator|(
name|client
operator|.
name|getRedirectUris
argument_list|()
operator|.
name|size
argument_list|()
operator|!=
literal|1
operator|||
operator|!
name|client
operator|.
name|getRedirectUris
argument_list|()
operator|.
name|contains
argument_list|(
name|expectedRedirectUri
argument_list|)
operator|)
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
name|OAuthConstants
operator|.
name|INVALID_REQUEST
argument_list|)
throw|;
block|}
name|String
name|clientCodeVerifier
init|=
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|AUTHORIZATION_CODE_VERIFIER
argument_list|)
decl_stmt|;
name|String
name|clientCodeChallenge
init|=
name|grant
operator|.
name|getClientCodeChallenge
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|compareCodeVerifierWithChallenge
argument_list|(
name|client
argument_list|,
name|clientCodeVerifier
argument_list|,
name|clientCodeChallenge
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
name|OAuthConstants
operator|.
name|INVALID_GRANT
argument_list|)
throw|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|audiences
init|=
name|getAudiences
argument_list|(
name|client
argument_list|,
name|params
argument_list|,
name|grant
operator|.
name|getAudience
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|doCreateAccessToken
argument_list|(
name|client
argument_list|,
name|grant
argument_list|,
name|getSingleGrantType
argument_list|()
argument_list|,
name|clientCodeVerifier
argument_list|,
name|audiences
argument_list|)
return|;
block|}
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|getAudiences
parameter_list|(
name|Client
name|client
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|,
name|String
name|grantAudience
parameter_list|)
block|{
name|String
name|clientAudience
init|=
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|CLIENT_AUDIENCE
argument_list|)
decl_stmt|;
if|if
condition|(
name|client
operator|.
name|getRegisteredAudiences
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|&&
name|clientAudience
operator|==
literal|null
operator|&&
name|grantAudience
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
comment|// if the audience was approved at the grant creation time and the audience is also
comment|// sent to the token endpoint then both values must match
if|if
condition|(
name|grantAudience
operator|!=
literal|null
operator|&&
name|clientAudience
operator|!=
literal|null
operator|&&
operator|!
name|grantAudience
operator|.
name|equals
argument_list|(
name|clientAudience
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
name|OAuthConstants
operator|.
name|INVALID_REQUEST
argument_list|)
throw|;
block|}
return|return
name|getAudiences
argument_list|(
name|client
argument_list|,
name|clientAudience
operator|==
literal|null
condition|?
name|grantAudience
else|:
name|clientAudience
argument_list|)
return|;
block|}
specifier|private
name|ServerAccessToken
name|doCreateAccessToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|ServerAuthorizationCodeGrant
name|grant
parameter_list|,
name|String
name|requestedGrant
parameter_list|,
name|String
name|codeVerifier
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|audiences
parameter_list|)
block|{
if|if
condition|(
name|grant
operator|.
name|isPreauthorizedTokenAvailable
argument_list|()
condition|)
block|{
name|ServerAccessToken
name|token
init|=
name|getPreAuthorizedToken
argument_list|(
name|client
argument_list|,
name|grant
operator|.
name|getSubject
argument_list|()
argument_list|,
name|requestedGrant
argument_list|,
name|grant
operator|.
name|getRequestedScopes
argument_list|()
argument_list|,
name|getAudiences
argument_list|(
name|client
argument_list|,
name|grant
operator|.
name|getAudience
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|token
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|grant
operator|.
name|getNonce
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|OAuthConstants
operator|.
name|NONCE
argument_list|,
name|grant
operator|.
name|getNonce
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|token
return|;
block|}
else|else
block|{
comment|// the grant was issued based on the authorization time check confirming the
comment|// token was available but it has expired by now or been removed then
comment|// creating a completely new token can be wrong - though this needs to be reviewed
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
name|OAuthConstants
operator|.
name|INVALID_GRANT
argument_list|)
throw|;
block|}
block|}
comment|// Delegate to the data provider to create the one
name|AccessTokenRegistration
name|reg
init|=
operator|new
name|AccessTokenRegistration
argument_list|()
decl_stmt|;
name|reg
operator|.
name|setClient
argument_list|(
name|client
argument_list|)
expr_stmt|;
name|reg
operator|.
name|setGrantType
argument_list|(
name|requestedGrant
argument_list|)
expr_stmt|;
name|reg
operator|.
name|setSubject
argument_list|(
name|grant
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
name|reg
operator|.
name|setRequestedScope
argument_list|(
name|grant
operator|.
name|getRequestedScopes
argument_list|()
argument_list|)
expr_stmt|;
name|reg
operator|.
name|setNonce
argument_list|(
name|grant
operator|.
name|getNonce
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|grant
operator|.
name|getApprovedScopes
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|reg
operator|.
name|setApprovedScope
argument_list|(
name|grant
operator|.
name|getApprovedScopes
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|reg
operator|.
name|setApprovedScope
argument_list|(
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|reg
operator|.
name|setAudiences
argument_list|(
name|audiences
argument_list|)
expr_stmt|;
name|reg
operator|.
name|setClientCodeVerifier
argument_list|(
name|codeVerifier
argument_list|)
expr_stmt|;
return|return
name|getDataProvider
argument_list|()
operator|.
name|createAccessToken
argument_list|(
name|reg
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|compareCodeVerifierWithChallenge
parameter_list|(
name|Client
name|c
parameter_list|,
name|String
name|clientCodeVerifier
parameter_list|,
name|String
name|clientCodeChallenge
parameter_list|)
block|{
if|if
condition|(
name|clientCodeChallenge
operator|==
literal|null
operator|&&
name|clientCodeVerifier
operator|==
literal|null
operator|&&
operator|(
name|c
operator|.
name|isConfidential
argument_list|()
operator|||
operator|!
name|expectCodeVerifierForPublicClients
operator|)
condition|)
block|{
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|clientCodeChallenge
operator|!=
literal|null
operator|&&
name|clientCodeVerifier
operator|==
literal|null
operator|||
name|clientCodeChallenge
operator|==
literal|null
operator|&&
name|clientCodeVerifier
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
else|else
block|{
name|String
name|transformedCodeVerifier
init|=
name|codeVerifierTransformer
operator|==
literal|null
condition|?
name|clientCodeVerifier
else|:
name|codeVerifierTransformer
operator|.
name|transformCodeVerifier
argument_list|(
name|clientCodeVerifier
argument_list|)
decl_stmt|;
return|return
name|clientCodeChallenge
operator|.
name|equals
argument_list|(
name|transformedCodeVerifier
argument_list|)
return|;
block|}
block|}
specifier|public
name|void
name|setCodeVerifierTransformer
parameter_list|(
name|CodeVerifierTransformer
name|codeVerifier
parameter_list|)
block|{
name|this
operator|.
name|codeVerifierTransformer
operator|=
name|codeVerifier
expr_stmt|;
block|}
specifier|public
name|void
name|setExpectCodeVerifierForPublicClients
parameter_list|(
name|boolean
name|expectCodeVerifierForPublicClients
parameter_list|)
block|{
name|this
operator|.
name|expectCodeVerifierForPublicClients
operator|=
name|expectCodeVerifierForPublicClients
expr_stmt|;
block|}
block|}
end_class

end_unit

