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
name|client
operator|.
name|controllers
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
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
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|demo
operator|.
name|oauth
operator|.
name|client
operator|.
name|model
operator|.
name|OAuthParams
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
name|OAuthServiceProvider
import|;
end_import

begin_import
import|import
name|net
operator|.
name|oauth
operator|.
name|ParameterStyle
import|;
end_import

begin_import
import|import
name|net
operator|.
name|oauth
operator|.
name|client
operator|.
name|OAuthClient
import|;
end_import

begin_import
import|import
name|net
operator|.
name|oauth
operator|.
name|client
operator|.
name|OAuthResponseMessage
import|;
end_import

begin_import
import|import
name|net
operator|.
name|oauth
operator|.
name|client
operator|.
name|URLConnectionClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Controller
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|web
operator|.
name|bind
operator|.
name|annotation
operator|.
name|ModelAttribute
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|web
operator|.
name|bind
operator|.
name|annotation
operator|.
name|RequestMapping
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|web
operator|.
name|servlet
operator|.
name|ModelAndView
import|;
end_import

begin_class
annotation|@
name|Controller
specifier|public
class|class
name|GetProtectedResourceController
block|{
annotation|@
name|RequestMapping
argument_list|(
literal|"/getProtectedResource"
argument_list|)
specifier|protected
name|ModelAndView
name|handleRequest
parameter_list|(
annotation|@
name|ModelAttribute
argument_list|(
literal|"oAuthParams"
argument_list|)
name|OAuthParams
name|oAuthParams
parameter_list|,
name|HttpServletRequest
name|request
parameter_list|)
throws|throws
name|Exception
block|{
name|OAuthServiceProvider
name|provider
init|=
operator|new
name|OAuthServiceProvider
argument_list|(
name|oAuthParams
operator|.
name|getTemporaryCredentialsEndpoint
argument_list|()
argument_list|,
name|oAuthParams
operator|.
name|getResourceOwnerAuthorizationEndpoint
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|OAuthConsumer
name|consumer
init|=
operator|new
name|OAuthConsumer
argument_list|(
literal|null
argument_list|,
name|oAuthParams
operator|.
name|getClientID
argument_list|()
argument_list|,
name|oAuthParams
operator|.
name|getClientSecret
argument_list|()
argument_list|,
name|provider
argument_list|)
decl_stmt|;
name|OAuthAccessor
name|accessor
init|=
operator|new
name|OAuthAccessor
argument_list|(
name|consumer
argument_list|)
decl_stmt|;
name|accessor
operator|.
name|requestToken
operator|=
name|oAuthParams
operator|.
name|getOauthToken
argument_list|()
expr_stmt|;
name|accessor
operator|.
name|tokenSecret
operator|=
name|oAuthParams
operator|.
name|getOauthTokenSecret
argument_list|()
expr_stmt|;
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
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
name|OAuth
operator|.
name|OAUTH_SIGNATURE_METHOD
argument_list|,
name|oAuthParams
operator|.
name|getSignatureMethod
argument_list|()
argument_list|)
expr_stmt|;
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
name|OAUTH_TOKEN
argument_list|,
name|oAuthParams
operator|.
name|getOauthToken
argument_list|()
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
name|oAuthParams
operator|.
name|getClientID
argument_list|()
argument_list|)
expr_stmt|;
name|OAuthMessage
name|msg
init|=
literal|null
decl_stmt|;
name|String
name|method
init|=
name|request
operator|.
name|getParameter
argument_list|(
literal|"op"
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"GET"
operator|.
name|equals
argument_list|(
name|method
argument_list|)
condition|)
block|{
name|msg
operator|=
name|accessor
operator|.
name|newRequestMessage
argument_list|(
name|OAuthMessage
operator|.
name|GET
argument_list|,
name|oAuthParams
operator|.
name|getGetResourceURL
argument_list|()
argument_list|,
name|parameters
operator|.
name|entrySet
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|msg
operator|=
name|accessor
operator|.
name|newRequestMessage
argument_list|(
name|OAuthMessage
operator|.
name|POST
argument_list|,
name|oAuthParams
operator|.
name|getPostResourceURL
argument_list|()
argument_list|,
name|parameters
operator|.
name|entrySet
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|OAuthClient
name|client
init|=
operator|new
name|OAuthClient
argument_list|(
operator|new
name|URLConnectionClient
argument_list|()
argument_list|)
decl_stmt|;
name|msg
operator|=
name|client
operator|.
name|access
argument_list|(
name|msg
argument_list|,
name|ParameterStyle
operator|.
name|QUERY_STRING
argument_list|)
expr_stmt|;
name|StringBuffer
name|bodyBuffer
init|=
name|readBody
argument_list|(
name|msg
argument_list|)
decl_stmt|;
name|oAuthParams
operator|.
name|setResourceResponse
argument_list|(
name|bodyBuffer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|authHeader
init|=
name|msg
operator|.
name|getHeader
argument_list|(
literal|"WWW-Authenticate"
argument_list|)
decl_stmt|;
name|String
name|oauthHeader
init|=
name|msg
operator|.
name|getHeader
argument_list|(
literal|"OAuth"
argument_list|)
decl_stmt|;
name|String
name|header
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|authHeader
operator|!=
literal|null
condition|)
block|{
name|header
operator|+=
literal|"WWW-Authenticate:"
operator|+
name|authHeader
expr_stmt|;
block|}
if|if
condition|(
name|oauthHeader
operator|!=
literal|null
condition|)
block|{
name|header
operator|+=
literal|"OAuth:"
operator|+
name|oauthHeader
expr_stmt|;
block|}
name|oAuthParams
operator|.
name|setHeader
argument_list|(
name|header
argument_list|)
expr_stmt|;
name|oAuthParams
operator|.
name|setResponseCode
argument_list|(
operator|(
operator|(
name|OAuthResponseMessage
operator|)
name|msg
operator|)
operator|.
name|getHttpResponse
argument_list|()
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|new
name|ModelAndView
argument_list|(
literal|"accessToken"
argument_list|)
return|;
block|}
specifier|private
name|StringBuffer
name|readBody
parameter_list|(
name|OAuthMessage
name|msg
parameter_list|)
throws|throws
name|IOException
block|{
name|StringBuffer
name|body
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|InputStream
name|responseBody
init|=
literal|null
decl_stmt|;
name|BufferedReader
name|br
init|=
literal|null
decl_stmt|;
try|try
block|{
name|responseBody
operator|=
name|msg
operator|.
name|getBodyAsStream
argument_list|()
expr_stmt|;
if|if
condition|(
name|responseBody
operator|!=
literal|null
condition|)
block|{
name|br
operator|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|responseBody
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|buf
decl_stmt|;
while|while
condition|(
operator|(
name|buf
operator|=
name|br
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|body
operator|.
name|append
argument_list|(
name|buf
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|br
operator|!=
literal|null
condition|)
block|{
name|br
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|responseBody
operator|!=
literal|null
condition|)
block|{
name|responseBody
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|body
return|;
block|}
block|}
end_class

end_unit

