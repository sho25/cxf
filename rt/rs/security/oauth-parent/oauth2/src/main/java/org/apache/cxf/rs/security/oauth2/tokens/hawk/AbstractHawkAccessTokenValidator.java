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
name|MessageDigest
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
name|HashMap
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
name|common
operator|.
name|util
operator|.
name|Base64Exception
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
name|Base64Utility
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
name|client
operator|.
name|HttpRequestProperties
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
name|rt
operator|.
name|security
operator|.
name|crypto
operator|.
name|HmacUtils
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractHawkAccessTokenValidator
implements|implements
name|AccessTokenValidator
block|{
specifier|protected
specifier|static
specifier|final
name|String
name|HTTP_VERB
init|=
literal|"http.verb"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|HTTP_URI
init|=
literal|"http.uri"
decl_stmt|;
specifier|private
name|NonceVerifier
name|nonceVerifier
decl_stmt|;
specifier|private
name|boolean
name|remoteSignatureValidation
decl_stmt|;
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getSupportedAuthorizationSchemes
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|OAuthConstants
operator|.
name|HAWK_AUTHORIZATION_SCHEME
argument_list|)
return|;
block|}
specifier|public
name|AccessTokenValidation
name|validateAccessToken
parameter_list|(
name|MessageContext
name|mc
parameter_list|,
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
throws|throws
name|OAuthServiceException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|schemeParams
init|=
name|getSchemeParameters
argument_list|(
name|authSchemeData
argument_list|)
decl_stmt|;
name|AccessTokenValidation
name|atv
init|=
name|getAccessTokenValidation
argument_list|(
name|mc
argument_list|,
name|authScheme
argument_list|,
name|authSchemeData
argument_list|,
name|extraProps
argument_list|,
name|schemeParams
argument_list|)
decl_stmt|;
if|if
condition|(
name|isRemoteSignatureValidation
argument_list|()
condition|)
block|{
return|return
name|atv
return|;
block|}
name|String
name|macKey
init|=
name|atv
operator|.
name|getExtraProps
argument_list|()
operator|.
name|get
argument_list|(
name|OAuthConstants
operator|.
name|HAWK_TOKEN_KEY
argument_list|)
decl_stmt|;
name|String
name|macAlgo
init|=
name|atv
operator|.
name|getExtraProps
argument_list|()
operator|.
name|get
argument_list|(
name|OAuthConstants
operator|.
name|HAWK_TOKEN_ALGORITHM
argument_list|)
decl_stmt|;
name|HttpRequestProperties
name|httpProps
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|extraProps
operator|!=
literal|null
operator|&&
name|extraProps
operator|.
name|containsKey
argument_list|(
name|HTTP_VERB
argument_list|)
operator|&&
name|extraProps
operator|.
name|containsKey
argument_list|(
name|HTTP_URI
argument_list|)
condition|)
block|{
name|httpProps
operator|=
operator|new
name|HttpRequestProperties
argument_list|(
name|URI
operator|.
name|create
argument_list|(
name|extraProps
operator|.
name|getFirst
argument_list|(
name|HTTP_URI
argument_list|)
argument_list|)
argument_list|,
name|extraProps
operator|.
name|getFirst
argument_list|(
name|HTTP_VERB
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|httpProps
operator|=
operator|new
name|HttpRequestProperties
argument_list|(
name|mc
operator|.
name|getUriInfo
argument_list|()
operator|.
name|getRequestUri
argument_list|()
argument_list|,
name|mc
operator|.
name|getHttpServletRequest
argument_list|()
operator|.
name|getMethod
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|HawkAuthorizationScheme
name|macAuthInfo
init|=
operator|new
name|HawkAuthorizationScheme
argument_list|(
name|httpProps
argument_list|,
name|schemeParams
argument_list|)
decl_stmt|;
name|String
name|normalizedString
init|=
name|macAuthInfo
operator|.
name|getNormalizedRequestString
argument_list|()
decl_stmt|;
try|try
block|{
name|HmacAlgorithm
name|hmacAlgo
init|=
name|HmacAlgorithm
operator|.
name|toHmacAlgorithm
argument_list|(
name|macAlgo
argument_list|)
decl_stmt|;
name|byte
index|[]
name|serverMacData
init|=
name|HmacUtils
operator|.
name|computeHmac
argument_list|(
name|macKey
argument_list|,
name|hmacAlgo
operator|.
name|getJavaName
argument_list|()
argument_list|,
name|normalizedString
argument_list|)
decl_stmt|;
name|String
name|clientMacString
init|=
name|schemeParams
operator|.
name|get
argument_list|(
name|OAuthConstants
operator|.
name|HAWK_TOKEN_SIGNATURE
argument_list|)
decl_stmt|;
name|byte
index|[]
name|clientMacData
init|=
name|Base64Utility
operator|.
name|decode
argument_list|(
name|clientMacString
argument_list|)
decl_stmt|;
name|boolean
name|validMac
init|=
name|MessageDigest
operator|.
name|isEqual
argument_list|(
name|serverMacData
argument_list|,
name|clientMacData
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|validMac
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
name|OAuthConstants
operator|.
name|HAWK_AUTHORIZATION_SCHEME
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Base64Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
name|OAuthConstants
operator|.
name|SERVER_ERROR
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|validateTimestampNonce
argument_list|(
name|macKey
argument_list|,
name|macAuthInfo
operator|.
name|getTimestamp
argument_list|()
argument_list|,
name|macAuthInfo
operator|.
name|getNonce
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|atv
return|;
block|}
specifier|protected
specifier|abstract
name|AccessTokenValidation
name|getAccessTokenValidation
parameter_list|(
name|MessageContext
name|mc
parameter_list|,
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
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|schemeParams
parameter_list|)
function_decl|;
specifier|protected
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getSchemeParameters
parameter_list|(
name|String
name|authData
parameter_list|)
block|{
name|String
index|[]
name|attributePairs
init|=
name|authData
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|attributeMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|pair
range|:
name|attributePairs
control|)
block|{
name|String
index|[]
name|pairValues
init|=
name|pair
operator|.
name|trim
argument_list|()
operator|.
name|split
argument_list|(
literal|"="
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|attributeMap
operator|.
name|put
argument_list|(
name|pairValues
index|[
literal|0
index|]
operator|.
name|trim
argument_list|()
argument_list|,
name|pairValues
index|[
literal|1
index|]
operator|.
name|trim
argument_list|()
operator|.
name|replaceAll
argument_list|(
literal|"\""
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|attributeMap
return|;
block|}
specifier|protected
name|void
name|validateTimestampNonce
parameter_list|(
name|String
name|tokenKey
parameter_list|,
name|String
name|ts
parameter_list|,
name|String
name|nonce
parameter_list|)
block|{
if|if
condition|(
name|nonceVerifier
operator|!=
literal|null
condition|)
block|{
name|nonceVerifier
operator|.
name|verifyNonce
argument_list|(
name|tokenKey
argument_list|,
name|nonce
argument_list|,
name|ts
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setNonceVerifier
parameter_list|(
name|NonceVerifier
name|nonceVerifier
parameter_list|)
block|{
name|this
operator|.
name|nonceVerifier
operator|=
name|nonceVerifier
expr_stmt|;
block|}
specifier|public
name|boolean
name|isRemoteSignatureValidation
parameter_list|()
block|{
return|return
name|remoteSignatureValidation
return|;
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

