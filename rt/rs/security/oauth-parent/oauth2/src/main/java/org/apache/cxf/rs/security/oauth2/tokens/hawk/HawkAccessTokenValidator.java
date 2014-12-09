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
name|tokens
operator|.
name|hawk
package|;
end_package

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
name|OAuthConstants
import|;
end_import

begin_class
specifier|public
class|class
name|HawkAccessTokenValidator
extends|extends
name|AbstractHawkAccessTokenValidator
block|{
specifier|private
name|OAuthDataProvider
name|dataProvider
decl_stmt|;
specifier|private
name|boolean
name|remoteSignatureValidation
decl_stmt|;
specifier|protected
name|AccessTokenValidation
name|getAccessTokenValidation
parameter_list|(
name|MessageContext
name|mc
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|schemeParams
parameter_list|,
name|String
name|authSchemeData
parameter_list|)
block|{
name|String
name|macKey
init|=
name|schemeParams
operator|.
name|get
argument_list|(
name|OAuthConstants
operator|.
name|HAWK_TOKEN_ID
argument_list|)
decl_stmt|;
name|ServerAccessToken
name|accessToken
init|=
name|dataProvider
operator|.
name|getAccessToken
argument_list|(
name|macKey
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|accessToken
operator|instanceof
name|HawkAccessToken
operator|)
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
name|OAuthConstants
operator|.
name|SERVER_ERROR
argument_list|)
throw|;
block|}
name|HawkAccessToken
name|macAccessToken
init|=
operator|(
name|HawkAccessToken
operator|)
name|accessToken
decl_stmt|;
name|AccessTokenValidation
name|atv
init|=
operator|new
name|AccessTokenValidation
argument_list|(
name|macAccessToken
argument_list|)
decl_stmt|;
comment|// OAuth2 Pop token introspection will likely support returning a JWE-encrypted key
if|if
condition|(
operator|!
name|remoteSignatureValidation
operator|||
name|mc
operator|.
name|getSecurityContext
argument_list|()
operator|.
name|isSecure
argument_list|()
condition|)
block|{
name|atv
operator|.
name|getExtraProps
argument_list|()
operator|.
name|put
argument_list|(
name|OAuthConstants
operator|.
name|HAWK_TOKEN_KEY
argument_list|,
name|macAccessToken
operator|.
name|getMacKey
argument_list|()
argument_list|)
expr_stmt|;
name|atv
operator|.
name|getExtraProps
argument_list|()
operator|.
name|put
argument_list|(
name|OAuthConstants
operator|.
name|HAWK_TOKEN_ALGORITHM
argument_list|,
name|macAccessToken
operator|.
name|getMacAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|atv
return|;
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
specifier|public
name|void
name|setRemoteSignatureValidation
parameter_list|(
name|boolean
name|remoteSignatureValidation
parameter_list|)
block|{
name|this
operator|.
name|remoteSignatureValidation
operator|=
name|remoteSignatureValidation
expr_stmt|;
block|}
block|}
end_class

end_unit

