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
name|oauth
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
name|security
operator|.
name|PrivateKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ProcessingException
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
name|WebApplicationException
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
name|Form
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
name|UriBuilder
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
name|OAuthAccessor
import|;
end_import

begin_import
import|import
name|net
operator|.
name|oauth
operator|.
name|OAuthConsumer
import|;
end_import

begin_import
import|import
name|net
operator|.
name|oauth
operator|.
name|OAuthMessage
import|;
end_import

begin_import
import|import
name|net
operator|.
name|oauth
operator|.
name|signature
operator|.
name|RSA_SHA1
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

begin_comment
comment|/**  * The utility class for simplifying making OAuth request and access token  * requests as well as for creating Authorization OAuth headers  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|OAuthClientUtils
block|{
specifier|private
name|OAuthClientUtils
parameter_list|()
block|{      }
comment|/**      * Returns URI of the authorization service with the query parameter containing      * the request token key      * @param authorizationServiceURI the service URI      * @param requestToken the request token key      * @return      */
specifier|public
specifier|static
name|URI
name|getAuthorizationURI
parameter_list|(
name|String
name|authorizationServiceURI
parameter_list|,
name|String
name|requestToken
parameter_list|)
block|{
return|return
name|UriBuilder
operator|.
name|fromUri
argument_list|(
name|authorizationServiceURI
argument_list|)
operator|.
name|queryParam
argument_list|(
literal|"oauth_token"
argument_list|,
name|requestToken
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
comment|/**      * Returns a simple representation of the Request token      * @param requestTokenService initialized RequestToken service client      * @param consumer Consumer bean containing the consumer key and secret      * @param callback the callback URI where the request token verifier will      *        be returned      * @param extraParams additional parameters such as state, scope, etc      * @return the token      */
specifier|public
specifier|static
name|Token
name|getRequestToken
parameter_list|(
name|WebClient
name|requestTokenService
parameter_list|,
name|Consumer
name|consumer
parameter_list|,
name|URI
name|callback
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extraParams
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
return|return
name|getRequestToken
argument_list|(
name|requestTokenService
argument_list|,
name|consumer
argument_list|,
name|callback
argument_list|,
name|extraParams
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|prepareOAuthRsaProperties
parameter_list|(
name|PrivateKey
name|pk
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
name|OAuth
operator|.
name|OAUTH_SIGNATURE_METHOD
argument_list|,
name|OAuth
operator|.
name|RSA_SHA1
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|RSA_SHA1
operator|.
name|PRIVATE_KEY
argument_list|,
name|pk
argument_list|)
expr_stmt|;
return|return
name|props
return|;
block|}
specifier|public
specifier|static
name|Token
name|getRequestToken
parameter_list|(
name|WebClient
name|requestTokenService
parameter_list|,
name|Consumer
name|consumer
parameter_list|,
name|URI
name|callback
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extraParams
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|oauthConsumerProps
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parameters
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|extraParams
operator|!=
literal|null
condition|)
block|{
name|parameters
operator|.
name|putAll
argument_list|(
name|extraParams
argument_list|)
expr_stmt|;
block|}
name|parameters
operator|.
name|put
argument_list|(
name|OAuth
operator|.
name|OAUTH_CALLBACK
argument_list|,
name|callback
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|oauthConsumerProps
operator|==
literal|null
operator|||
operator|!
name|oauthConsumerProps
operator|.
name|containsKey
argument_list|(
name|OAuth
operator|.
name|OAUTH_SIGNATURE_METHOD
argument_list|)
condition|)
block|{
name|parameters
operator|.
name|put
argument_list|(
name|OAuth
operator|.
name|OAUTH_SIGNATURE_METHOD
argument_list|,
name|OAuth
operator|.
name|HMAC_SHA1
argument_list|)
expr_stmt|;
block|}
name|parameters
operator|.
name|put
argument_list|(
name|OAuth
operator|.
name|OAUTH_NONCE
argument_list|,
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
name|OAuth
operator|.
name|OAUTH_TIMESTAMP
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|/
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
name|OAuth
operator|.
name|OAUTH_CONSUMER_KEY
argument_list|,
name|consumer
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|OAuthAccessor
name|accessor
init|=
name|createAccessor
argument_list|(
name|consumer
argument_list|,
name|oauthConsumerProps
argument_list|)
decl_stmt|;
return|return
name|getToken
argument_list|(
name|requestTokenService
argument_list|,
name|accessor
argument_list|,
name|parameters
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|OAuthAccessor
name|createAccessor
parameter_list|(
name|Consumer
name|consumer
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
parameter_list|)
block|{
name|OAuthConsumer
name|oAuthConsumer
init|=
operator|new
name|OAuthConsumer
argument_list|(
literal|null
argument_list|,
name|consumer
operator|.
name|getKey
argument_list|()
argument_list|,
name|consumer
operator|.
name|getSecret
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|props
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|props
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|oAuthConsumer
operator|.
name|setProperty
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|OAuthAccessor
argument_list|(
name|oAuthConsumer
argument_list|)
return|;
block|}
comment|/**      * Returns a simple representation of the Access token      * @param accessTokenService initialized AccessToken service client      * @param consumer Consumer bean containing the consumer key and secret      * @param verifier the verifier/authorization key      * @return the token      */
specifier|public
specifier|static
name|Token
name|getAccessToken
parameter_list|(
name|WebClient
name|accessTokenService
parameter_list|,
name|Consumer
name|consumer
parameter_list|,
name|Token
name|requestToken
parameter_list|,
name|String
name|verifier
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
return|return
name|getAccessToken
argument_list|(
name|accessTokenService
argument_list|,
name|consumer
argument_list|,
name|requestToken
argument_list|,
name|verifier
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Token
name|getAccessToken
parameter_list|(
name|WebClient
name|accessTokenService
parameter_list|,
name|Consumer
name|consumer
parameter_list|,
name|Token
name|requestToken
parameter_list|,
name|String
name|verifier
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|oauthConsumerProps
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parameters
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
name|OAuth
operator|.
name|OAUTH_CONSUMER_KEY
argument_list|,
name|consumer
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
name|OAuth
operator|.
name|OAUTH_TOKEN
argument_list|,
name|requestToken
operator|.
name|getToken
argument_list|()
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
name|OAuth
operator|.
name|OAUTH_VERIFIER
argument_list|,
name|verifier
argument_list|)
expr_stmt|;
if|if
condition|(
name|oauthConsumerProps
operator|==
literal|null
operator|||
operator|!
name|oauthConsumerProps
operator|.
name|containsKey
argument_list|(
name|OAuth
operator|.
name|OAUTH_SIGNATURE_METHOD
argument_list|)
condition|)
block|{
name|parameters
operator|.
name|put
argument_list|(
name|OAuth
operator|.
name|OAUTH_SIGNATURE_METHOD
argument_list|,
name|OAuth
operator|.
name|HMAC_SHA1
argument_list|)
expr_stmt|;
block|}
name|OAuthAccessor
name|accessor
init|=
name|createAccessor
argument_list|(
name|consumer
argument_list|,
name|oauthConsumerProps
argument_list|)
decl_stmt|;
name|accessor
operator|.
name|requestToken
operator|=
name|requestToken
operator|.
name|getToken
argument_list|()
expr_stmt|;
name|accessor
operator|.
name|tokenSecret
operator|=
name|requestToken
operator|.
name|getSecret
argument_list|()
expr_stmt|;
return|return
name|getToken
argument_list|(
name|accessTokenService
argument_list|,
name|accessor
argument_list|,
name|parameters
argument_list|)
return|;
block|}
comment|/**      * Creates OAuth Authorization header      * @param consumer Consumer bean containing the consumer key and secret      * @param accessToken Access token representation      * @param method HTTP method      * @param requestURI request URI      * @return the header value      */
specifier|public
specifier|static
name|String
name|createAuthorizationHeader
parameter_list|(
name|Consumer
name|consumer
parameter_list|,
name|Token
name|accessToken
parameter_list|,
name|String
name|method
parameter_list|,
name|String
name|requestURI
parameter_list|)
block|{
return|return
name|createAuthorizationHeader
argument_list|(
name|consumer
argument_list|,
name|accessToken
argument_list|,
name|method
argument_list|,
name|requestURI
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|createAuthorizationHeader
parameter_list|(
name|Consumer
name|consumer
parameter_list|,
name|Token
name|accessToken
parameter_list|,
name|String
name|method
parameter_list|,
name|String
name|requestURI
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|oauthConsumerProps
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parameters
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
name|OAuth
operator|.
name|OAUTH_CONSUMER_KEY
argument_list|,
name|consumer
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|accessToken
operator|!=
literal|null
condition|)
block|{
name|parameters
operator|.
name|put
argument_list|(
name|OAuth
operator|.
name|OAUTH_TOKEN
argument_list|,
name|accessToken
operator|.
name|getToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|oauthConsumerProps
operator|==
literal|null
operator|||
operator|!
name|oauthConsumerProps
operator|.
name|containsKey
argument_list|(
name|OAuth
operator|.
name|OAUTH_SIGNATURE_METHOD
argument_list|)
condition|)
block|{
name|parameters
operator|.
name|put
argument_list|(
name|OAuth
operator|.
name|OAUTH_SIGNATURE_METHOD
argument_list|,
name|OAuth
operator|.
name|HMAC_SHA1
argument_list|)
expr_stmt|;
block|}
name|parameters
operator|.
name|put
argument_list|(
name|OAuth
operator|.
name|OAUTH_NONCE
argument_list|,
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
name|OAuth
operator|.
name|OAUTH_TIMESTAMP
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|/
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|OAuthAccessor
name|accessor
init|=
name|createAccessor
argument_list|(
name|consumer
argument_list|,
name|oauthConsumerProps
argument_list|)
decl_stmt|;
if|if
condition|(
name|accessToken
operator|!=
literal|null
condition|)
block|{
name|accessor
operator|.
name|accessToken
operator|=
name|accessToken
operator|.
name|getToken
argument_list|()
expr_stmt|;
name|accessor
operator|.
name|tokenSecret
operator|=
name|accessToken
operator|.
name|getSecret
argument_list|()
expr_stmt|;
block|}
return|return
name|doGetAuthorizationHeader
argument_list|(
name|accessor
argument_list|,
name|method
argument_list|,
name|requestURI
argument_list|,
name|parameters
argument_list|)
return|;
block|}
comment|/**      * Creates OAuth Authorization header containing consumer key and secret values only      * @param consumer Consumer bean containing the consumer key and secret      * @return the header value      */
specifier|public
specifier|static
name|String
name|createAuthorizationHeader
parameter_list|(
name|Consumer
name|consumer
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|64
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"OAuth "
argument_list|)
operator|.
name|append
argument_list|(
literal|"oauth_consumer_key="
argument_list|)
operator|.
name|append
argument_list|(
name|consumer
operator|.
name|getKey
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"oauth_consumer_secret="
argument_list|)
operator|.
name|append
argument_list|(
name|consumer
operator|.
name|getSecret
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|String
name|doGetAuthorizationHeader
parameter_list|(
name|OAuthAccessor
name|accessor
parameter_list|,
name|String
name|method
parameter_list|,
name|String
name|requestURI
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parameters
parameter_list|)
block|{
try|try
block|{
name|OAuthMessage
name|msg
init|=
name|accessor
operator|.
name|newRequestMessage
argument_list|(
name|method
argument_list|,
name|requestURI
argument_list|,
name|parameters
operator|.
name|entrySet
argument_list|()
argument_list|)
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|msg
operator|.
name|getAuthorizationHeader
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|parameters
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"oauth_"
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|OAuth
operator|.
name|percentEncode
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|"=\""
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|OAuth
operator|.
name|percentEncode
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|'"'
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ProcessingException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|private
specifier|static
name|Token
name|getToken
parameter_list|(
name|WebClient
name|tokenService
parameter_list|,
name|OAuthAccessor
name|accessor
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
name|String
name|header
init|=
name|doGetAuthorizationHeader
argument_list|(
name|accessor
argument_list|,
literal|"POST"
argument_list|,
name|tokenService
operator|.
name|getBaseURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
try|try
block|{
name|tokenService
operator|.
name|replaceHeader
argument_list|(
literal|"Authorization"
argument_list|,
name|header
argument_list|)
expr_stmt|;
name|Form
name|form
init|=
name|tokenService
operator|.
name|post
argument_list|(
literal|null
argument_list|,
name|Form
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
operator|new
name|Token
argument_list|(
name|form
operator|.
name|asMap
argument_list|()
operator|.
name|getFirst
argument_list|(
literal|"oauth_token"
argument_list|)
argument_list|,
name|form
operator|.
name|asMap
argument_list|()
operator|.
name|getFirst
argument_list|(
literal|"oauth_token_secret"
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|WebApplicationException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
comment|/**      * Simple token representation      */
specifier|public
specifier|static
class|class
name|Token
block|{
specifier|private
name|String
name|token
decl_stmt|;
specifier|private
name|String
name|secret
decl_stmt|;
specifier|public
name|Token
parameter_list|(
name|String
name|token
parameter_list|,
name|String
name|secret
parameter_list|)
block|{
name|this
operator|.
name|token
operator|=
name|token
expr_stmt|;
name|this
operator|.
name|secret
operator|=
name|secret
expr_stmt|;
block|}
specifier|public
name|String
name|getToken
parameter_list|()
block|{
return|return
name|token
return|;
block|}
specifier|public
name|String
name|getSecret
parameter_list|()
block|{
return|return
name|secret
return|;
block|}
block|}
comment|/**      * Simple consumer representation      */
specifier|public
specifier|static
class|class
name|Consumer
block|{
specifier|private
name|String
name|key
decl_stmt|;
specifier|private
name|String
name|secret
decl_stmt|;
specifier|public
name|Consumer
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|secret
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
name|this
operator|.
name|secret
operator|=
name|secret
expr_stmt|;
block|}
specifier|public
name|String
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
specifier|public
name|String
name|getSecret
parameter_list|()
block|{
return|return
name|secret
return|;
block|}
block|}
block|}
end_class

end_unit

