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
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

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
name|List
import|;
end_import

begin_class
specifier|public
class|class
name|OAuthParams
implements|implements
name|Serializable
block|{
specifier|private
name|String
name|temporaryCredentialsEndpoint
init|=
literal|"http://localhost:8081/auth/oauth/initiate"
decl_stmt|;
specifier|private
name|String
name|resourceOwnerAuthorizationEndpoint
init|=
literal|"http://localhost:8081/auth/oauth/authorize"
decl_stmt|;
specifier|private
name|String
name|tokenRequestEndpoint
init|=
literal|"http://localhost:8081/auth/oauth/token"
decl_stmt|;
specifier|private
name|String
name|getResourceURL
init|=
literal|"http://localhost:8081/auth/resources/person/get/john"
decl_stmt|;
specifier|private
name|String
name|postResourceURL
init|=
literal|"http://localhost:8081/auth/resources/person/modify/john"
decl_stmt|;
specifier|private
name|String
name|callbackURL
init|=
literal|"http://localhost:8080/app/callback"
decl_stmt|;
specifier|private
name|String
name|clientID
init|=
literal|"12345678"
decl_stmt|;
specifier|private
name|String
name|clientSecret
init|=
literal|"secret"
decl_stmt|;
specifier|private
name|String
name|signatureMethod
decl_stmt|;
specifier|private
name|String
name|oauthToken
decl_stmt|;
specifier|private
name|String
name|oauthTokenSecret
decl_stmt|;
specifier|private
name|String
name|oauthVerifier
decl_stmt|;
specifier|private
name|String
name|errorMessage
decl_stmt|;
specifier|private
name|String
name|resourceResponse
decl_stmt|;
specifier|private
name|String
name|header
decl_stmt|;
specifier|private
name|Integer
name|responseCode
decl_stmt|;
specifier|private
name|List
argument_list|<
name|SignatureMethod
argument_list|>
name|methods
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|OAuthParams
parameter_list|()
block|{
name|methods
operator|.
name|add
argument_list|(
operator|new
name|SignatureMethod
argument_list|(
literal|"HMAC-SHA1"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|OAuthParams
parameter_list|(
name|String
name|clientSecret
parameter_list|,
name|String
name|clientID
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|clientSecret
operator|=
name|clientSecret
expr_stmt|;
name|this
operator|.
name|clientID
operator|=
name|clientID
expr_stmt|;
block|}
specifier|public
name|String
name|getClientSecret
parameter_list|()
block|{
return|return
name|clientSecret
return|;
block|}
specifier|public
name|void
name|setClientSecret
parameter_list|(
name|String
name|clientSecret
parameter_list|)
block|{
name|this
operator|.
name|clientSecret
operator|=
name|clientSecret
expr_stmt|;
block|}
specifier|public
name|String
name|getClientID
parameter_list|()
block|{
return|return
name|clientID
return|;
block|}
specifier|public
name|void
name|setClientID
parameter_list|(
name|String
name|clientID
parameter_list|)
block|{
name|this
operator|.
name|clientID
operator|=
name|clientID
expr_stmt|;
block|}
specifier|public
name|String
name|getSignatureMethod
parameter_list|()
block|{
return|return
name|signatureMethod
return|;
block|}
specifier|public
name|void
name|setSignatureMethod
parameter_list|(
name|String
name|signatureMethod
parameter_list|)
block|{
name|this
operator|.
name|signatureMethod
operator|=
name|signatureMethod
expr_stmt|;
block|}
specifier|public
name|String
name|getTemporaryCredentialsEndpoint
parameter_list|()
block|{
return|return
name|temporaryCredentialsEndpoint
return|;
block|}
specifier|public
name|void
name|setTemporaryCredentialsEndpoint
parameter_list|(
name|String
name|temporaryCredentialsEndpoint
parameter_list|)
block|{
name|this
operator|.
name|temporaryCredentialsEndpoint
operator|=
name|temporaryCredentialsEndpoint
expr_stmt|;
block|}
specifier|public
name|String
name|getOauthToken
parameter_list|()
block|{
return|return
name|oauthToken
return|;
block|}
specifier|public
name|void
name|setOauthToken
parameter_list|(
name|String
name|oauthToken
parameter_list|)
block|{
name|this
operator|.
name|oauthToken
operator|=
name|oauthToken
expr_stmt|;
block|}
specifier|public
name|String
name|getOauthTokenSecret
parameter_list|()
block|{
return|return
name|oauthTokenSecret
return|;
block|}
specifier|public
name|void
name|setOauthTokenSecret
parameter_list|(
name|String
name|oauthTokenSecret
parameter_list|)
block|{
name|this
operator|.
name|oauthTokenSecret
operator|=
name|oauthTokenSecret
expr_stmt|;
block|}
specifier|public
name|String
name|getResourceOwnerAuthorizationEndpoint
parameter_list|()
block|{
return|return
name|resourceOwnerAuthorizationEndpoint
return|;
block|}
specifier|public
name|void
name|setResourceOwnerAuthorizationEndpoint
parameter_list|(
name|String
name|resourceOwnerAuthorizationEndpoint
parameter_list|)
block|{
name|this
operator|.
name|resourceOwnerAuthorizationEndpoint
operator|=
name|resourceOwnerAuthorizationEndpoint
expr_stmt|;
block|}
specifier|public
name|String
name|getTokenRequestEndpoint
parameter_list|()
block|{
return|return
name|tokenRequestEndpoint
return|;
block|}
specifier|public
name|void
name|setTokenRequestEndpoint
parameter_list|(
name|String
name|tokenRequestEndpoint
parameter_list|)
block|{
name|this
operator|.
name|tokenRequestEndpoint
operator|=
name|tokenRequestEndpoint
expr_stmt|;
block|}
specifier|public
name|String
name|getOauthVerifier
parameter_list|()
block|{
return|return
name|oauthVerifier
return|;
block|}
specifier|public
name|void
name|setOauthVerifier
parameter_list|(
name|String
name|oauthVerifier
parameter_list|)
block|{
name|this
operator|.
name|oauthVerifier
operator|=
name|oauthVerifier
expr_stmt|;
block|}
specifier|public
name|String
name|getErrorMessage
parameter_list|()
block|{
return|return
name|errorMessage
return|;
block|}
specifier|public
name|void
name|setErrorMessage
parameter_list|(
name|String
name|errorMessage
parameter_list|)
block|{
name|this
operator|.
name|errorMessage
operator|=
name|errorMessage
expr_stmt|;
block|}
specifier|public
name|String
name|getGetResourceURL
parameter_list|()
block|{
return|return
name|getResourceURL
return|;
block|}
specifier|public
name|void
name|setGetResourceURL
parameter_list|(
name|String
name|getResourceURL
parameter_list|)
block|{
name|this
operator|.
name|getResourceURL
operator|=
name|getResourceURL
expr_stmt|;
block|}
specifier|public
name|String
name|getCallbackURL
parameter_list|()
block|{
return|return
name|callbackURL
return|;
block|}
specifier|public
name|void
name|setCallbackURL
parameter_list|(
name|String
name|callbackURL
parameter_list|)
block|{
name|this
operator|.
name|callbackURL
operator|=
name|callbackURL
expr_stmt|;
block|}
specifier|public
name|String
name|getResourceResponse
parameter_list|()
block|{
return|return
name|resourceResponse
return|;
block|}
specifier|public
name|void
name|setResourceResponse
parameter_list|(
name|String
name|resourceResponse
parameter_list|)
block|{
name|this
operator|.
name|resourceResponse
operator|=
name|resourceResponse
expr_stmt|;
block|}
specifier|public
name|String
name|getHeader
parameter_list|()
block|{
return|return
name|header
return|;
block|}
specifier|public
name|void
name|setHeader
parameter_list|(
name|String
name|header
parameter_list|)
block|{
name|this
operator|.
name|header
operator|=
name|header
expr_stmt|;
block|}
specifier|public
name|List
name|getMethods
parameter_list|()
block|{
return|return
name|methods
return|;
block|}
specifier|public
name|void
name|setMethods
parameter_list|(
name|List
argument_list|<
name|SignatureMethod
argument_list|>
name|methods
parameter_list|)
block|{
name|this
operator|.
name|methods
operator|=
name|methods
expr_stmt|;
block|}
specifier|public
name|String
name|getPostResourceURL
parameter_list|()
block|{
return|return
name|postResourceURL
return|;
block|}
specifier|public
name|void
name|setPostResourceURL
parameter_list|(
name|String
name|postResourceURL
parameter_list|)
block|{
name|this
operator|.
name|postResourceURL
operator|=
name|postResourceURL
expr_stmt|;
block|}
specifier|public
name|Integer
name|getResponseCode
parameter_list|()
block|{
return|return
name|responseCode
return|;
block|}
specifier|public
name|void
name|setResponseCode
parameter_list|(
name|Integer
name|responseCode
parameter_list|)
block|{
name|this
operator|.
name|responseCode
operator|=
name|responseCode
expr_stmt|;
block|}
specifier|static
class|class
name|SignatureMethod
block|{
specifier|private
name|String
name|methodName
decl_stmt|;
name|SignatureMethod
parameter_list|(
name|String
name|methodName
parameter_list|)
block|{
name|this
operator|.
name|methodName
operator|=
name|methodName
expr_stmt|;
block|}
specifier|public
name|String
name|getMethodName
parameter_list|()
block|{
return|return
name|methodName
return|;
block|}
specifier|public
name|void
name|setMethodName
parameter_list|(
name|String
name|methodName
parameter_list|)
block|{
name|this
operator|.
name|methodName
operator|=
name|methodName
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

