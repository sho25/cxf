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
name|services
package|;
end_package

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
name|logging
operator|.
name|Level
import|;
end_import

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
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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
name|OAuthMessage
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
name|oauth
operator|.
name|utils
operator|.
name|OAuthUtils
import|;
end_import

begin_class
specifier|public
class|class
name|AccessTokenHandler
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
name|AccessTokenHandler
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|REQUIRED_PARAMETERS
init|=
operator|new
name|String
index|[]
block|{
name|OAuth
operator|.
name|OAUTH_CONSUMER_KEY
block|,
name|OAuth
operator|.
name|OAUTH_TOKEN
block|,
name|OAuth
operator|.
name|OAUTH_SIGNATURE_METHOD
block|,
name|OAuth
operator|.
name|OAUTH_SIGNATURE
block|,
name|OAuth
operator|.
name|OAUTH_TIMESTAMP
block|,
name|OAuth
operator|.
name|OAUTH_NONCE
block|,
name|OAuth
operator|.
name|OAUTH_VERIFIER
block|}
decl_stmt|;
specifier|public
name|Response
name|handle
parameter_list|(
name|MessageContext
name|mc
parameter_list|,
name|OAuthDataProvider
name|dataProvider
parameter_list|)
block|{
try|try
block|{
name|OAuthMessage
name|oAuthMessage
init|=
name|OAuthUtils
operator|.
name|getOAuthMessage
argument_list|(
name|mc
argument_list|,
name|mc
operator|.
name|getHttpServletRequest
argument_list|()
argument_list|,
name|REQUIRED_PARAMETERS
argument_list|)
decl_stmt|;
name|RequestToken
name|requestToken
init|=
name|dataProvider
operator|.
name|getRequestToken
argument_list|(
name|oAuthMessage
operator|.
name|getToken
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|requestToken
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|OAuthProblemException
argument_list|(
name|OAuth
operator|.
name|Problems
operator|.
name|TOKEN_REJECTED
argument_list|)
throw|;
block|}
name|String
name|oauthVerifier
init|=
name|oAuthMessage
operator|.
name|getParameter
argument_list|(
name|OAuth
operator|.
name|OAUTH_VERIFIER
argument_list|)
decl_stmt|;
if|if
condition|(
name|oauthVerifier
operator|==
literal|null
operator|||
operator|!
name|oauthVerifier
operator|.
name|equals
argument_list|(
name|requestToken
operator|.
name|getVerifier
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|OAuthProblemException
argument_list|(
name|OAuthConstants
operator|.
name|VERIFIER_INVALID
argument_list|)
throw|;
block|}
name|OAuthUtils
operator|.
name|validateMessage
argument_list|(
name|oAuthMessage
argument_list|,
name|requestToken
operator|.
name|getClient
argument_list|()
argument_list|,
name|requestToken
argument_list|,
name|dataProvider
argument_list|)
expr_stmt|;
name|AccessToken
name|accessToken
init|=
name|dataProvider
operator|.
name|createAccessToken
argument_list|(
name|requestToken
argument_list|)
decl_stmt|;
comment|//create response
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|responseParams
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|responseParams
operator|.
name|put
argument_list|(
name|OAuth
operator|.
name|OAUTH_TOKEN
argument_list|,
name|accessToken
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
name|responseParams
operator|.
name|put
argument_list|(
name|OAuth
operator|.
name|OAUTH_TOKEN_SECRET
argument_list|,
name|accessToken
operator|.
name|getTokenSecret
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|responseString
init|=
name|OAuth
operator|.
name|formEncode
argument_list|(
name|responseParams
operator|.
name|entrySet
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|responseString
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|OAuthProblemException
name|e
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"An OAuth-related problem: {0}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|e
operator|.
name|fillInStackTrace
argument_list|()
block|}
argument_list|)
expr_stmt|;
block|}
return|return
name|OAuthUtils
operator|.
name|handleException
argument_list|(
name|e
argument_list|,
name|e
operator|.
name|getHttpStatusCode
argument_list|()
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|e
operator|.
name|getParameters
argument_list|()
operator|.
name|get
argument_list|(
literal|"realm"
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Server Exception: {0}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|e
operator|.
name|fillInStackTrace
argument_list|()
block|}
argument_list|)
expr_stmt|;
block|}
return|return
name|OAuthUtils
operator|.
name|handleException
argument_list|(
name|e
argument_list|,
name|HttpServletResponse
operator|.
name|SC_INTERNAL_SERVER_ERROR
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

