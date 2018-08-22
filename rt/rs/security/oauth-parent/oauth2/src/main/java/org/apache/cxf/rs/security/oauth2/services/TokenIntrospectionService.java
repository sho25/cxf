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
name|logging
operator|.
name|Logger
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
name|Consumes
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
name|Encoded
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
name|POST
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
name|Path
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
name|Produces
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
name|MediaType
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
name|SecurityContext
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
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
name|common
operator|.
name|util
operator|.
name|StringUtils
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
name|rs
operator|.
name|security
operator|.
name|jose
operator|.
name|jwt
operator|.
name|JoseJwtConsumer
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
name|JwtException
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
name|TokenIntrospection
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
annotation|@
name|Path
argument_list|(
literal|"introspect"
argument_list|)
specifier|public
class|class
name|TokenIntrospectionService
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|TokenIntrospectionService
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|boolean
name|blockUnsecureRequests
decl_stmt|;
specifier|private
name|boolean
name|blockUnauthorizedRequests
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|reportExtraTokenProperties
init|=
literal|true
decl_stmt|;
specifier|private
name|MessageContext
name|mc
decl_stmt|;
specifier|private
name|OAuthDataProvider
name|dataProvider
decl_stmt|;
specifier|private
name|JoseJwtConsumer
name|jwtTokenConsumer
decl_stmt|;
specifier|private
name|boolean
name|persistJwtEncoding
init|=
literal|true
decl_stmt|;
annotation|@
name|POST
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_JSON
block|}
argument_list|)
annotation|@
name|Consumes
argument_list|(
name|MediaType
operator|.
name|APPLICATION_FORM_URLENCODED
argument_list|)
specifier|public
name|TokenIntrospection
name|getTokenIntrospection
parameter_list|(
annotation|@
name|Encoded
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
block|{
name|checkSecurityContext
argument_list|()
expr_stmt|;
name|String
name|tokenId
init|=
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|TOKEN_ID
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|persistJwtEncoding
condition|)
block|{
try|try
block|{
name|JoseJwtConsumer
name|theConsumer
init|=
name|jwtTokenConsumer
operator|==
literal|null
condition|?
operator|new
name|JoseJwtConsumer
argument_list|()
else|:
name|jwtTokenConsumer
decl_stmt|;
name|JwtToken
name|token
init|=
name|theConsumer
operator|.
name|getJwtToken
argument_list|(
name|tokenId
argument_list|)
decl_stmt|;
name|tokenId
operator|=
name|token
operator|.
name|getClaims
argument_list|()
operator|.
name|getTokenId
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JwtException
name|ex
parameter_list|)
block|{
return|return
operator|new
name|TokenIntrospection
argument_list|(
literal|false
argument_list|)
return|;
block|}
block|}
name|ServerAccessToken
name|at
init|=
name|dataProvider
operator|.
name|getAccessToken
argument_list|(
name|tokenId
argument_list|)
decl_stmt|;
if|if
condition|(
name|at
operator|==
literal|null
operator|||
name|OAuthUtils
operator|.
name|isExpired
argument_list|(
name|at
operator|.
name|getIssuedAt
argument_list|()
argument_list|,
name|at
operator|.
name|getExpiresIn
argument_list|()
argument_list|)
condition|)
block|{
return|return
operator|new
name|TokenIntrospection
argument_list|(
literal|false
argument_list|)
return|;
block|}
name|TokenIntrospection
name|response
init|=
operator|new
name|TokenIntrospection
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|response
operator|.
name|setClientId
argument_list|(
name|at
operator|.
name|getClient
argument_list|()
operator|.
name|getClientId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|at
operator|.
name|getScopes
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|response
operator|.
name|setScope
argument_list|(
name|OAuthUtils
operator|.
name|convertPermissionsToScope
argument_list|(
name|at
operator|.
name|getScopes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|UserSubject
name|userSubject
init|=
name|at
operator|.
name|getSubject
argument_list|()
decl_stmt|;
if|if
condition|(
name|userSubject
operator|!=
literal|null
condition|)
block|{
name|response
operator|.
name|setUsername
argument_list|(
name|at
operator|.
name|getSubject
argument_list|()
operator|.
name|getLogin
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|userSubject
operator|.
name|getId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|response
operator|.
name|setSub
argument_list|(
name|userSubject
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|at
operator|.
name|getAudiences
argument_list|()
argument_list|)
condition|)
block|{
name|response
operator|.
name|setAud
argument_list|(
name|at
operator|.
name|getAudiences
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|at
operator|.
name|getIssuer
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|response
operator|.
name|setIss
argument_list|(
name|at
operator|.
name|getIssuer
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|response
operator|.
name|setIat
argument_list|(
name|at
operator|.
name|getIssuedAt
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|at
operator|.
name|getExpiresIn
argument_list|()
operator|>
literal|0
condition|)
block|{
name|response
operator|.
name|setExp
argument_list|(
name|at
operator|.
name|getIssuedAt
argument_list|()
operator|+
name|at
operator|.
name|getExpiresIn
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|response
operator|.
name|setTokenType
argument_list|(
name|at
operator|.
name|getTokenType
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|reportExtraTokenProperties
condition|)
block|{
name|response
operator|.
name|getExtensions
argument_list|()
operator|.
name|putAll
argument_list|(
name|at
operator|.
name|getExtraProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|response
return|;
block|}
specifier|private
name|void
name|checkSecurityContext
parameter_list|()
block|{
name|SecurityContext
name|sc
init|=
name|mc
operator|.
name|getSecurityContext
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|sc
operator|.
name|isSecure
argument_list|()
operator|&&
name|blockUnsecureRequests
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Unsecure HTTP, Transport Layer Security is recommended"
argument_list|)
expr_stmt|;
name|ExceptionUtils
operator|.
name|toNotAuthorizedException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sc
operator|.
name|getUserPrincipal
argument_list|()
operator|==
literal|null
operator|&&
name|blockUnauthorizedRequests
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Authenticated Principal is not available"
argument_list|)
expr_stmt|;
name|ExceptionUtils
operator|.
name|toNotAuthorizedException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setBlockUnsecureRequests
parameter_list|(
name|boolean
name|blockUnsecureRequests
parameter_list|)
block|{
name|this
operator|.
name|blockUnsecureRequests
operator|=
name|blockUnsecureRequests
expr_stmt|;
block|}
specifier|public
name|void
name|setBlockUnauthorizedRequests
parameter_list|(
name|boolean
name|blockUnauthorizedRequests
parameter_list|)
block|{
name|this
operator|.
name|blockUnauthorizedRequests
operator|=
name|blockUnauthorizedRequests
expr_stmt|;
block|}
specifier|public
name|void
name|setDataProvider
parameter_list|(
name|OAuthDataProvider
name|dataProvider
parameter_list|)
block|{
name|this
operator|.
name|dataProvider
operator|=
name|dataProvider
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
name|void
name|setReportExtraTokenProperties
parameter_list|(
name|boolean
name|reportExtraTokenProperties
parameter_list|)
block|{
name|this
operator|.
name|reportExtraTokenProperties
operator|=
name|reportExtraTokenProperties
expr_stmt|;
block|}
specifier|public
name|JoseJwtConsumer
name|getJwtTokenConsumer
parameter_list|()
block|{
return|return
name|jwtTokenConsumer
return|;
block|}
specifier|public
name|void
name|setJwtTokenConsumer
parameter_list|(
name|JoseJwtConsumer
name|jwtTokenConsumer
parameter_list|)
block|{
name|this
operator|.
name|jwtTokenConsumer
operator|=
name|jwtTokenConsumer
expr_stmt|;
block|}
specifier|public
name|boolean
name|isPersistJwtEncoding
parameter_list|()
block|{
return|return
name|persistJwtEncoding
return|;
block|}
specifier|public
name|void
name|setPersistJwtEncoding
parameter_list|(
name|boolean
name|persistJwtEncoding
parameter_list|)
block|{
name|this
operator|.
name|persistJwtEncoding
operator|=
name|persistJwtEncoding
expr_stmt|;
block|}
block|}
end_class

end_unit

