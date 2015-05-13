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
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|configuration
operator|.
name|security
operator|.
name|AuthorizationPolicy
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
name|client
operator|.
name|WebClient
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
name|message
operator|.
name|Message
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
name|provider
operator|.
name|OAuthJSONProvider
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|http
operator|.
name|auth
operator|.
name|HttpAuthSupplier
import|;
end_import

begin_class
specifier|public
class|class
name|BearerAuthSupplier
extends|extends
name|AbstractAuthSupplier
implements|implements
name|HttpAuthSupplier
block|{
specifier|private
name|Consumer
name|consumer
decl_stmt|;
specifier|private
name|String
name|accessTokenServiceUri
decl_stmt|;
specifier|private
name|boolean
name|refreshEarly
decl_stmt|;
specifier|public
name|BearerAuthSupplier
parameter_list|()
block|{
name|super
argument_list|(
name|OAuthConstants
operator|.
name|BEARER_AUTHORIZATION_SCHEME
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|requiresRequestCaching
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|String
name|getAuthorization
parameter_list|(
name|AuthorizationPolicy
name|authPolicy
parameter_list|,
name|URI
name|currentURI
parameter_list|,
name|Message
name|message
parameter_list|,
name|String
name|fullHeader
parameter_list|)
block|{
if|if
condition|(
name|getClientAccessToken
argument_list|()
operator|.
name|getTokenKey
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|fullHeader
operator|==
literal|null
condition|)
block|{
comment|// regular authorization
if|if
condition|(
name|refreshEarly
condition|)
block|{
name|refreshAccessTokenIfExpired
argument_list|(
name|authPolicy
argument_list|)
expr_stmt|;
block|}
return|return
name|createAuthorizationHeader
argument_list|()
return|;
block|}
comment|// the last call resulted in 401, trying to refresh the token(s)
if|if
condition|(
name|refreshAccessToken
argument_list|(
name|authPolicy
argument_list|)
condition|)
block|{
return|return
name|createAuthorizationHeader
argument_list|()
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
name|refreshAccessTokenIfExpired
parameter_list|(
name|AuthorizationPolicy
name|authPolicy
parameter_list|)
block|{
name|ClientAccessToken
name|at
init|=
name|getClientAccessToken
argument_list|()
decl_stmt|;
if|if
condition|(
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
name|refreshAccessToken
argument_list|(
name|authPolicy
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|refreshAccessToken
parameter_list|(
name|AuthorizationPolicy
name|authPolicy
parameter_list|)
block|{
name|ClientAccessToken
name|at
init|=
name|getClientAccessToken
argument_list|()
decl_stmt|;
if|if
condition|(
name|at
operator|.
name|getRefreshToken
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// Client id and secret are needed to refresh the tokens
comment|// AuthorizationPolicy can hold them by default, Consumer can also be injected into this supplier
comment|// and checked if the policy is null.
comment|// Client TLS authentication is also fine as an alternative authentication mechanism,
comment|// how can we check here that a 2-way TLS has been set up ?
name|Consumer
name|theConsumer
init|=
name|consumer
decl_stmt|;
if|if
condition|(
name|theConsumer
operator|==
literal|null
operator|&&
name|authPolicy
operator|!=
literal|null
operator|&&
name|authPolicy
operator|.
name|getUserName
argument_list|()
operator|!=
literal|null
operator|&&
name|authPolicy
operator|.
name|getPassword
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|theConsumer
operator|=
operator|new
name|Consumer
argument_list|(
name|authPolicy
operator|.
name|getUserName
argument_list|()
argument_list|,
name|authPolicy
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
if|if
condition|(
name|theConsumer
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// Can WebCient be safely constructed at HttpConduit initialization time ?
comment|// If yes then createAccessTokenServiceClient() can be called inside
comment|// setAccessTokenServiceUri, though given that the token refreshment would
comment|// not be done on every request the current approach is quite reasonable
name|WebClient
name|accessTokenService
init|=
name|createAccessTokenServiceClient
argument_list|()
decl_stmt|;
name|setClientAccessToken
argument_list|(
name|OAuthClientUtils
operator|.
name|refreshAccessToken
argument_list|(
name|accessTokenService
argument_list|,
name|theConsumer
argument_list|,
name|at
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
name|WebClient
name|createAccessTokenServiceClient
parameter_list|()
block|{
return|return
name|WebClient
operator|.
name|create
argument_list|(
name|accessTokenServiceUri
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|OAuthJSONProvider
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|void
name|setRefreshToken
parameter_list|(
name|String
name|refreshToken
parameter_list|)
block|{
name|getClientAccessToken
argument_list|()
operator|.
name|setRefreshToken
argument_list|(
name|refreshToken
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setAccessTokenServiceUri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|this
operator|.
name|accessTokenServiceUri
operator|=
name|uri
expr_stmt|;
block|}
specifier|public
name|Consumer
name|getConsumer
parameter_list|()
block|{
return|return
name|consumer
return|;
block|}
specifier|public
name|void
name|setConsumer
parameter_list|(
name|Consumer
name|consumer
parameter_list|)
block|{
name|this
operator|.
name|consumer
operator|=
name|consumer
expr_stmt|;
block|}
specifier|public
name|void
name|setRefreshEarly
parameter_list|(
name|boolean
name|refreshEarly
parameter_list|)
block|{
name|this
operator|.
name|refreshEarly
operator|=
name|refreshEarly
expr_stmt|;
block|}
block|}
end_class

end_unit

