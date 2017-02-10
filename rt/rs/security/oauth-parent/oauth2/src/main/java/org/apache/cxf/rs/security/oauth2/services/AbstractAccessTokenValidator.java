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
name|services
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|Context
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
name|ext
operator|.
name|MessageContext
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
name|ext
operator|.
name|MessageContextImpl
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
name|ExceptionUtils
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
name|phase
operator|.
name|PhaseInterceptorChain
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
name|AccessTokenValidation
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
name|AccessTokenValidator
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
name|AuthorizationUtils
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

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractAccessTokenValidator
block|{
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_AUTH_SCHEME
init|=
name|OAuthConstants
operator|.
name|BEARER_AUTHORIZATION_SCHEME
decl_stmt|;
specifier|protected
name|Set
argument_list|<
name|String
argument_list|>
name|supportedSchemes
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
specifier|protected
name|String
name|realm
decl_stmt|;
specifier|private
name|MessageContext
name|mc
decl_stmt|;
specifier|private
name|List
argument_list|<
name|AccessTokenValidator
argument_list|>
name|tokenHandlers
init|=
name|Collections
operator|.
name|emptyList
argument_list|()
decl_stmt|;
specifier|private
name|OAuthDataProvider
name|dataProvider
decl_stmt|;
specifier|private
name|int
name|maxValidationDataCacheSize
decl_stmt|;
specifier|private
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|AccessTokenValidation
argument_list|>
name|accessTokenValidations
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|AccessTokenValidation
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|void
name|setTokenValidator
parameter_list|(
name|AccessTokenValidator
name|validator
parameter_list|)
block|{
name|setTokenValidators
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|validator
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setTokenValidators
parameter_list|(
name|List
argument_list|<
name|AccessTokenValidator
argument_list|>
name|validators
parameter_list|)
block|{
name|tokenHandlers
operator|=
name|validators
expr_stmt|;
for|for
control|(
name|AccessTokenValidator
name|handler
range|:
name|validators
control|)
block|{
name|supportedSchemes
operator|.
name|addAll
argument_list|(
name|handler
operator|.
name|getSupportedAuthorizationSchemes
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setDataProvider
parameter_list|(
name|OAuthDataProvider
name|provider
parameter_list|)
block|{
name|dataProvider
operator|=
name|provider
expr_stmt|;
block|}
annotation|@
name|Context
specifier|public
name|void
name|setMessageContext
parameter_list|(
name|MessageContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|mc
operator|=
name|context
expr_stmt|;
block|}
specifier|public
name|MessageContext
name|getMessageContext
parameter_list|()
block|{
return|return
name|mc
operator|!=
literal|null
condition|?
name|mc
else|:
operator|new
name|MessageContextImpl
argument_list|(
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|AccessTokenValidator
name|findTokenValidator
parameter_list|(
name|String
name|authScheme
parameter_list|)
block|{
for|for
control|(
name|AccessTokenValidator
name|handler
range|:
name|tokenHandlers
control|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|handlerSchemes
init|=
name|handler
operator|.
name|getSupportedAuthorizationSchemes
argument_list|()
decl_stmt|;
if|if
condition|(
name|handlerSchemes
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|&&
name|OAuthConstants
operator|.
name|ALL_AUTH_SCHEMES
operator|.
name|equals
argument_list|(
name|handlerSchemes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|||
name|handlerSchemes
operator|.
name|contains
argument_list|(
name|authScheme
argument_list|)
condition|)
block|{
return|return
name|handler
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Get the access token      */
specifier|protected
name|AccessTokenValidation
name|getAccessTokenValidation
parameter_list|(
name|String
name|authScheme
parameter_list|,
name|String
name|authSchemeData
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extraProps
parameter_list|)
block|{
name|AccessTokenValidation
name|accessTokenV
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|dataProvider
operator|==
literal|null
operator|&&
name|tokenHandlers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
name|ExceptionUtils
operator|.
name|toInternalServerErrorException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
throw|;
block|}
if|if
condition|(
name|maxValidationDataCacheSize
operator|>
literal|0
condition|)
block|{
name|accessTokenV
operator|=
name|accessTokenValidations
operator|.
name|get
argument_list|(
name|authSchemeData
argument_list|)
expr_stmt|;
block|}
name|ServerAccessToken
name|localAccessToken
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|accessTokenV
operator|==
literal|null
condition|)
block|{
comment|// Get the registered handler capable of processing the token
name|AccessTokenValidator
name|handler
init|=
name|findTokenValidator
argument_list|(
name|authScheme
argument_list|)
decl_stmt|;
if|if
condition|(
name|handler
operator|!=
literal|null
condition|)
block|{
try|try
block|{
comment|// Convert the HTTP Authorization scheme data into a token
name|accessTokenV
operator|=
name|handler
operator|.
name|validateAccessToken
argument_list|(
name|getMessageContext
argument_list|()
argument_list|,
name|authScheme
argument_list|,
name|authSchemeData
argument_list|,
name|extraProps
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OAuthServiceException
name|ex
parameter_list|)
block|{
name|AuthorizationUtils
operator|.
name|throwAuthorizationFailure
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|authScheme
argument_list|)
argument_list|,
name|realm
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|ex
parameter_list|)
block|{
name|AuthorizationUtils
operator|.
name|throwAuthorizationFailure
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|authScheme
argument_list|)
argument_list|,
name|realm
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Default processing if no registered providers available
if|if
condition|(
name|accessTokenV
operator|==
literal|null
operator|&&
name|dataProvider
operator|!=
literal|null
operator|&&
name|authScheme
operator|.
name|equals
argument_list|(
name|DEFAULT_AUTH_SCHEME
argument_list|)
condition|)
block|{
try|try
block|{
name|localAccessToken
operator|=
name|dataProvider
operator|.
name|getAccessToken
argument_list|(
name|authSchemeData
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OAuthServiceException
name|ex
parameter_list|)
block|{
comment|// to be handled next
block|}
if|if
condition|(
name|localAccessToken
operator|==
literal|null
condition|)
block|{
name|AuthorizationUtils
operator|.
name|throwAuthorizationFailure
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|authScheme
argument_list|)
argument_list|,
name|realm
argument_list|)
expr_stmt|;
block|}
name|accessTokenV
operator|=
operator|new
name|AccessTokenValidation
argument_list|(
name|localAccessToken
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|accessTokenV
operator|==
literal|null
condition|)
block|{
name|AuthorizationUtils
operator|.
name|throwAuthorizationFailure
argument_list|(
name|supportedSchemes
argument_list|,
name|realm
argument_list|)
expr_stmt|;
block|}
comment|// Check if token is still valid
if|if
condition|(
name|OAuthUtils
operator|.
name|isExpired
argument_list|(
name|accessTokenV
operator|.
name|getTokenIssuedAt
argument_list|()
argument_list|,
name|accessTokenV
operator|.
name|getTokenLifetime
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|localAccessToken
operator|!=
literal|null
condition|)
block|{
name|removeAccessToken
argument_list|(
name|localAccessToken
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|maxValidationDataCacheSize
operator|>
literal|0
condition|)
block|{
name|accessTokenValidations
operator|.
name|remove
argument_list|(
name|authSchemeData
argument_list|)
expr_stmt|;
block|}
name|AuthorizationUtils
operator|.
name|throwAuthorizationFailure
argument_list|(
name|supportedSchemes
argument_list|,
name|realm
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|maxValidationDataCacheSize
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|accessTokenValidations
operator|.
name|size
argument_list|()
operator|>=
name|maxValidationDataCacheSize
condition|)
block|{
comment|// or delete the ones expiring sooner than others, etc
name|accessTokenValidations
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
name|accessTokenValidations
operator|.
name|put
argument_list|(
name|authSchemeData
argument_list|,
name|accessTokenV
argument_list|)
expr_stmt|;
block|}
return|return
name|accessTokenV
return|;
block|}
specifier|protected
name|void
name|removeAccessToken
parameter_list|(
name|ServerAccessToken
name|at
parameter_list|)
block|{
name|dataProvider
operator|.
name|revokeToken
argument_list|(
name|at
operator|.
name|getClient
argument_list|()
argument_list|,
name|at
operator|.
name|getTokenKey
argument_list|()
argument_list|,
name|OAuthConstants
operator|.
name|ACCESS_TOKEN
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setRealm
parameter_list|(
name|String
name|realm
parameter_list|)
block|{
name|this
operator|.
name|realm
operator|=
name|realm
expr_stmt|;
block|}
specifier|public
name|void
name|setMaxValidationDataCacheSize
parameter_list|(
name|int
name|maxValidationDataCacheSize
parameter_list|)
block|{
name|this
operator|.
name|maxValidationDataCacheSize
operator|=
name|maxValidationDataCacheSize
expr_stmt|;
block|}
block|}
end_class

end_unit

