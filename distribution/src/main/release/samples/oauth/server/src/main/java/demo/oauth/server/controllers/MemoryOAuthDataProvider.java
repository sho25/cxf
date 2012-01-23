begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|oauth
operator|.
name|server
operator|.
name|controllers
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
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
name|net
operator|.
name|oauth
operator|.
name|OAuth
import|;
end_import

begin_import
import|import
name|net
operator|.
name|oauth
operator|.
name|OAuthProblemException
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
name|impl
operator|.
name|MetadataMap
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
name|oauth
operator|.
name|data
operator|.
name|AccessToken
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
name|oauth
operator|.
name|data
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
name|oauth
operator|.
name|data
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
name|oauth
operator|.
name|data
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
name|oauth
operator|.
name|data
operator|.
name|RequestToken
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
name|oauth
operator|.
name|data
operator|.
name|RequestTokenRegistration
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
name|oauth
operator|.
name|data
operator|.
name|Token
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
name|oauth
operator|.
name|provider
operator|.
name|MD5SequenceGenerator
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
name|oauth
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
name|oauth
operator|.
name|provider
operator|.
name|OAuthServiceException
import|;
end_import

begin_class
specifier|public
class|class
name|MemoryOAuthDataProvider
implements|implements
name|OAuthDataProvider
block|{
specifier|public
specifier|static
specifier|final
name|String
name|CALLBACK
init|=
literal|"http://www.example.com/callback"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|APPLICATION_NAME
init|=
literal|"Test Oauth 1.0 application"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CLIENT_ID
init|=
literal|"12345678"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CLIENT_SECRET
init|=
literal|"secret"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|OAuthPermission
argument_list|>
name|AVAILABLE_PERMISSIONS
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|OAuthPermission
argument_list|>
argument_list|()
decl_stmt|;
static|static
block|{
name|AVAILABLE_PERMISSIONS
operator|.
name|put
argument_list|(
literal|"read_info"
argument_list|,
operator|new
name|OAuthPermission
argument_list|(
literal|"read_info"
argument_list|,
literal|"Read your personal information"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"ROLE_USER"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|AVAILABLE_PERMISSIONS
operator|.
name|put
argument_list|(
literal|"modify_info"
argument_list|,
operator|new
name|OAuthPermission
argument_list|(
literal|"modify_info"
argument_list|,
literal|"Modify your personal information"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"ROLE_ADMIN"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|Client
argument_list|>
name|clientAuthInfo
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|Client
argument_list|>
argument_list|()
decl_stmt|;
specifier|protected
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|userRegisteredClients
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|protected
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|userAuthorizedClients
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|protected
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|Token
argument_list|>
name|oauthTokens
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|Token
argument_list|>
argument_list|()
decl_stmt|;
specifier|protected
name|MD5SequenceGenerator
name|tokenGenerator
init|=
operator|new
name|MD5SequenceGenerator
argument_list|()
decl_stmt|;
specifier|public
name|MemoryOAuthDataProvider
parameter_list|()
block|{
name|Client
name|client
init|=
operator|new
name|Client
argument_list|(
name|CLIENT_ID
argument_list|,
name|CLIENT_SECRET
argument_list|,
name|APPLICATION_NAME
argument_list|,
name|CALLBACK
argument_list|)
decl_stmt|;
name|clientAuthInfo
operator|.
name|put
argument_list|(
name|CLIENT_ID
argument_list|,
name|client
argument_list|)
expr_stmt|;
block|}
specifier|private
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|getPermissionsInfo
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|requestPermissions
parameter_list|)
block|{
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|permissions
init|=
operator|new
name|ArrayList
argument_list|<
name|OAuthPermission
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|requestScope
range|:
name|requestPermissions
control|)
block|{
name|OAuthPermission
name|oAuthPermission
init|=
name|AVAILABLE_PERMISSIONS
operator|.
name|get
argument_list|(
name|requestScope
argument_list|)
decl_stmt|;
name|permissions
operator|.
name|add
argument_list|(
name|oAuthPermission
argument_list|)
expr_stmt|;
block|}
return|return
name|permissions
return|;
block|}
specifier|public
name|Client
name|getClient
parameter_list|(
name|String
name|consumerKey
parameter_list|)
block|{
return|return
name|clientAuthInfo
operator|.
name|get
argument_list|(
name|consumerKey
argument_list|)
return|;
block|}
specifier|public
name|RequestToken
name|createRequestToken
parameter_list|(
name|RequestTokenRegistration
name|reg
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
name|String
name|token
init|=
name|generateToken
argument_list|()
decl_stmt|;
name|String
name|tokenSecret
init|=
name|generateToken
argument_list|()
decl_stmt|;
name|RequestToken
name|reqToken
init|=
operator|new
name|RequestToken
argument_list|(
name|reg
operator|.
name|getClient
argument_list|()
argument_list|,
name|token
argument_list|,
name|tokenSecret
argument_list|,
name|reg
operator|.
name|getLifetime
argument_list|()
argument_list|,
name|reg
operator|.
name|getIssuedAt
argument_list|()
argument_list|)
decl_stmt|;
name|reqToken
operator|.
name|setScopes
argument_list|(
name|getPermissionsInfo
argument_list|(
name|reg
operator|.
name|getScopes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|reqToken
operator|.
name|setCallback
argument_list|(
name|reg
operator|.
name|getCallback
argument_list|()
argument_list|)
expr_stmt|;
name|oauthTokens
operator|.
name|put
argument_list|(
name|token
argument_list|,
name|reqToken
argument_list|)
expr_stmt|;
return|return
name|reqToken
return|;
block|}
specifier|public
name|RequestToken
name|getRequestToken
parameter_list|(
name|String
name|tokenString
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
name|Token
name|token
init|=
name|oauthTokens
operator|.
name|get
argument_list|(
name|tokenString
argument_list|)
decl_stmt|;
if|if
condition|(
name|token
operator|==
literal|null
operator|||
operator|(
operator|!
name|RequestToken
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|token
operator|.
name|getClass
argument_list|()
argument_list|)
operator|)
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
operator|new
name|OAuthProblemException
argument_list|(
name|OAuth
operator|.
name|Problems
operator|.
name|TOKEN_REJECTED
argument_list|)
argument_list|)
throw|;
block|}
return|return
operator|(
name|RequestToken
operator|)
name|token
return|;
block|}
specifier|public
name|String
name|setRequestTokenVerifier
parameter_list|(
name|RequestToken
name|requestToken
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
name|requestToken
operator|.
name|setVerifier
argument_list|(
name|generateToken
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|requestToken
operator|.
name|getVerifier
argument_list|()
return|;
block|}
specifier|public
name|AccessToken
name|createAccessToken
parameter_list|(
name|AccessTokenRegistration
name|reg
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
name|RequestToken
name|requestToken
init|=
name|reg
operator|.
name|getRequestToken
argument_list|()
decl_stmt|;
name|Client
name|client
init|=
name|requestToken
operator|.
name|getClient
argument_list|()
decl_stmt|;
name|requestToken
operator|=
name|getRequestToken
argument_list|(
name|requestToken
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|accessTokenString
init|=
name|generateToken
argument_list|()
decl_stmt|;
name|String
name|tokenSecretString
init|=
name|generateToken
argument_list|()
decl_stmt|;
name|AccessToken
name|accessToken
init|=
operator|new
name|AccessToken
argument_list|(
name|client
argument_list|,
name|accessTokenString
argument_list|,
name|tokenSecretString
argument_list|,
literal|3600
argument_list|,
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|/
literal|1000
argument_list|)
decl_stmt|;
name|accessToken
operator|.
name|setScopes
argument_list|(
name|requestToken
operator|.
name|getScopes
argument_list|()
argument_list|)
expr_stmt|;
synchronized|synchronized
init|(
name|oauthTokens
init|)
block|{
name|oauthTokens
operator|.
name|remove
argument_list|(
name|requestToken
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
name|oauthTokens
operator|.
name|put
argument_list|(
name|accessTokenString
argument_list|,
name|accessToken
argument_list|)
expr_stmt|;
synchronized|synchronized
init|(
name|userAuthorizedClients
init|)
block|{
name|userAuthorizedClients
operator|.
name|add
argument_list|(
name|client
operator|.
name|getConsumerKey
argument_list|()
argument_list|,
name|client
operator|.
name|getConsumerKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|accessToken
return|;
block|}
specifier|public
name|AccessToken
name|getAccessToken
parameter_list|(
name|String
name|accessToken
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
return|return
operator|(
name|AccessToken
operator|)
name|oauthTokens
operator|.
name|get
argument_list|(
name|accessToken
argument_list|)
return|;
block|}
specifier|public
name|void
name|removeAllTokens
parameter_list|(
name|String
name|consumerKey
parameter_list|)
block|{
comment|//TODO: implement
block|}
specifier|public
name|void
name|removeToken
parameter_list|(
name|Token
name|t
parameter_list|)
block|{
for|for
control|(
name|Token
name|token
range|:
name|oauthTokens
operator|.
name|values
argument_list|()
control|)
block|{
name|Client
name|authNInfo
init|=
name|token
operator|.
name|getClient
argument_list|()
decl_stmt|;
if|if
condition|(
name|t
operator|.
name|getClient
argument_list|()
operator|.
name|getConsumerKey
argument_list|()
operator|.
name|equals
argument_list|(
name|authNInfo
operator|.
name|getConsumerKey
argument_list|()
argument_list|)
condition|)
block|{
name|oauthTokens
operator|.
name|remove
argument_list|(
name|token
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
specifier|protected
name|String
name|generateToken
parameter_list|()
throws|throws
name|OAuthServiceException
block|{
name|String
name|token
decl_stmt|;
try|try
block|{
name|token
operator|=
name|tokenGenerator
operator|.
name|generate
argument_list|(
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
literal|"Unable to create token "
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|token
return|;
block|}
specifier|public
name|void
name|setClientAuthInfo
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Client
argument_list|>
name|clientAuthInfo
parameter_list|)
block|{
name|this
operator|.
name|clientAuthInfo
operator|.
name|putAll
argument_list|(
name|clientAuthInfo
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

