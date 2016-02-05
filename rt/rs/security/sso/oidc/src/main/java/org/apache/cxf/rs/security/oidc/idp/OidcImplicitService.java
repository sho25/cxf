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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
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
name|JoseJwtProducer
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
name|OAuthError
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
name|common
operator|.
name|OAuthRedirectionState
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
name|common
operator|.
name|UserSubject
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
name|services
operator|.
name|ImplicitGrantService
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
name|OidcImplicitService
extends|extends
name|ImplicitGrantService
block|{
specifier|private
specifier|static
specifier|final
name|String
name|OPEN_ID_CONNECT_SCOPE
init|=
literal|"openid"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ID_TOKEN_RESPONSE_TYPE
init|=
literal|"id_token"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ID_TOKEN_AND_AT_RESPONSE_TYPE
init|=
literal|"id_token token"
decl_stmt|;
specifier|private
name|boolean
name|skipAuthorizationWithOidcScope
decl_stmt|;
specifier|private
name|JoseJwtProducer
name|idTokenHandler
decl_stmt|;
specifier|public
name|OidcImplicitService
parameter_list|()
block|{
name|super
argument_list|(
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|ID_TOKEN_RESPONSE_TYPE
argument_list|,
name|ID_TOKEN_AND_AT_RESPONSE_TYPE
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|canAccessTokenBeReturned
parameter_list|(
name|String
name|responseType
parameter_list|)
block|{
return|return
name|ID_TOKEN_AND_AT_RESPONSE_TYPE
operator|.
name|equals
argument_list|(
name|responseType
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Response
name|startAuthorization
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|,
name|UserSubject
name|userSubject
parameter_list|,
name|Client
name|client
parameter_list|)
block|{
comment|// Validate the nonce, it must be present for the Implicit flow
if|if
condition|(
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|NONCE
argument_list|)
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
operator|new
name|OAuthError
argument_list|(
name|OAuthConstants
operator|.
name|INVALID_REQUEST
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|super
operator|.
name|startAuthorization
argument_list|(
name|params
argument_list|,
name|userSubject
argument_list|,
name|client
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|canAuthorizationBeSkipped
parameter_list|(
name|Client
name|client
parameter_list|,
name|UserSubject
name|userSubject
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|requestedScope
parameter_list|,
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|permissions
parameter_list|)
block|{
comment|// No need to challenge the authenticated user with the authorization form
comment|// if all the client application redirecting a user needs is to get this user authenticated
comment|// with OIDC IDP
return|return
name|requestedScope
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|&&
name|permissions
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|&&
name|skipAuthorizationWithOidcScope
operator|&&
name|OPEN_ID_CONNECT_SCOPE
operator|.
name|equals
argument_list|(
name|requestedScope
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|void
name|setSkipAuthorizationWithOidcScope
parameter_list|(
name|boolean
name|skipAuthorizationWithOidcScope
parameter_list|)
block|{
name|this
operator|.
name|skipAuthorizationWithOidcScope
operator|=
name|skipAuthorizationWithOidcScope
expr_stmt|;
block|}
specifier|protected
name|Response
name|createGrant
parameter_list|(
name|OAuthRedirectionState
name|state
parameter_list|,
name|Client
name|client
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|requestedScope
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|approvedScope
parameter_list|,
name|UserSubject
name|userSubject
parameter_list|,
name|ServerAccessToken
name|preAuthorizedToken
parameter_list|)
block|{
if|if
condition|(
name|canAccessTokenBeReturned
argument_list|(
name|state
operator|.
name|getResponseType
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|super
operator|.
name|createGrant
argument_list|(
name|state
argument_list|,
name|client
argument_list|,
name|requestedScope
argument_list|,
name|approvedScope
argument_list|,
name|userSubject
argument_list|,
name|preAuthorizedToken
argument_list|)
return|;
block|}
comment|// id_token response type processing
name|StringBuilder
name|sb
init|=
name|getUriWithFragment
argument_list|(
name|state
operator|.
name|getRedirectUri
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|idToken
init|=
name|getProcessedIdToken
argument_list|(
name|state
argument_list|,
name|userSubject
argument_list|)
decl_stmt|;
if|if
condition|(
name|idToken
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|OidcUtils
operator|.
name|ID_TOKEN
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|idToken
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|state
operator|.
name|getState
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"&"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|OAuthConstants
operator|.
name|STATE
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|state
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|finalizeResponse
argument_list|(
name|sb
argument_list|,
name|state
argument_list|)
return|;
block|}
specifier|private
name|String
name|getProcessedIdToken
parameter_list|(
name|OAuthRedirectionState
name|state
parameter_list|,
name|UserSubject
name|subject
parameter_list|)
block|{
if|if
condition|(
name|subject
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
name|subject
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
name|subject
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
name|subject
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
name|idToken
operator|.
name|setNonce
argument_list|(
name|state
operator|.
name|getNonce
argument_list|()
argument_list|)
expr_stmt|;
name|JoseJwtProducer
name|processor
init|=
name|idTokenHandler
operator|==
literal|null
condition|?
operator|new
name|JoseJwtProducer
argument_list|()
else|:
name|idTokenHandler
decl_stmt|;
return|return
name|processor
operator|.
name|processJwt
argument_list|(
operator|new
name|JwtToken
argument_list|(
name|idToken
argument_list|)
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
specifier|public
name|void
name|setIdTokenJoseHandler
parameter_list|(
name|JoseJwtProducer
name|idTokenJoseHandler
parameter_list|)
block|{
name|this
operator|.
name|idTokenHandler
operator|=
name|idTokenJoseHandler
expr_stmt|;
block|}
block|}
end_class

end_unit

