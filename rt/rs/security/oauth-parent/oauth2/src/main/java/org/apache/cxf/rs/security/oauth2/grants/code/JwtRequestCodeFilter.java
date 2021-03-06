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
name|grants
operator|.
name|code
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|X509Certificate
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
name|helpers
operator|.
name|CastUtils
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
name|jaxrs
operator|.
name|json
operator|.
name|basic
operator|.
name|JsonMapObjectReaderWriter
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
name|jwa
operator|.
name|SignatureAlgorithm
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
name|jwe
operator|.
name|JweDecryptionProvider
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
name|jws
operator|.
name|JwsSignatureVerifier
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
name|jws
operator|.
name|JwsUtils
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
name|JwtClaims
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
name|AuthorizationRequestFilter
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
name|OAuthJoseJwtConsumer
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
name|CryptoUtils
import|;
end_import

begin_class
specifier|public
class|class
name|JwtRequestCodeFilter
extends|extends
name|OAuthJoseJwtConsumer
implements|implements
name|AuthorizationRequestFilter
block|{
specifier|private
specifier|static
specifier|final
name|String
name|REQUEST_PARAM
init|=
literal|"request"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|REQUEST_URI_PARAM
init|=
literal|"request_uri"
decl_stmt|;
specifier|private
name|boolean
name|verifyWithClientCertificates
decl_stmt|;
specifier|private
name|String
name|issuer
decl_stmt|;
specifier|private
name|JsonMapObjectReaderWriter
name|jsonHandler
init|=
operator|new
name|JsonMapObjectReaderWriter
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|process
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|,
name|UserSubject
name|endUser
parameter_list|,
name|Client
name|client
parameter_list|)
block|{
name|String
name|requestToken
init|=
name|params
operator|.
name|getFirst
argument_list|(
name|REQUEST_PARAM
argument_list|)
decl_stmt|;
if|if
condition|(
name|requestToken
operator|==
literal|null
condition|)
block|{
name|String
name|requestUri
init|=
name|params
operator|.
name|getFirst
argument_list|(
name|REQUEST_URI_PARAM
argument_list|)
decl_stmt|;
if|if
condition|(
name|isRequestUriValid
argument_list|(
name|client
argument_list|,
name|requestUri
argument_list|)
condition|)
block|{
name|requestToken
operator|=
name|WebClient
operator|.
name|create
argument_list|(
name|requestUri
argument_list|)
operator|.
name|get
argument_list|(
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|requestToken
operator|!=
literal|null
condition|)
block|{
name|JweDecryptionProvider
name|theDecryptor
init|=
name|super
operator|.
name|getInitializedDecryptionProvider
argument_list|(
name|client
operator|.
name|getClientSecret
argument_list|()
argument_list|)
decl_stmt|;
name|JwsSignatureVerifier
name|theSigVerifier
init|=
name|getInitializedSigVerifier
argument_list|(
name|client
argument_list|)
decl_stmt|;
name|JwtToken
name|jwt
init|=
name|getJwtToken
argument_list|(
name|requestToken
argument_list|,
name|theDecryptor
argument_list|,
name|theSigVerifier
argument_list|)
decl_stmt|;
name|JwtClaims
name|claims
init|=
name|jwt
operator|.
name|getClaims
argument_list|()
decl_stmt|;
comment|// Check issuer
name|String
name|iss
init|=
name|issuer
operator|!=
literal|null
condition|?
name|issuer
else|:
name|client
operator|.
name|getClientId
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|iss
operator|.
name|equals
argument_list|(
name|claims
operator|.
name|getIssuer
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|()
throw|;
block|}
comment|// Check client_id - if present it must match the client_id specified in the request
if|if
condition|(
name|claims
operator|.
name|getClaim
argument_list|(
name|OAuthConstants
operator|.
name|CLIENT_ID
argument_list|)
operator|!=
literal|null
operator|&&
operator|!
name|claims
operator|.
name|getStringProperty
argument_list|(
name|OAuthConstants
operator|.
name|CLIENT_ID
argument_list|)
operator|.
name|equals
argument_list|(
name|client
operator|.
name|getClientId
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|()
throw|;
block|}
comment|// Check response_type - if present it must match the response_type specified in the request
name|String
name|tokenResponseType
init|=
operator|(
name|String
operator|)
name|claims
operator|.
name|getClaim
argument_list|(
name|OAuthConstants
operator|.
name|RESPONSE_TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|tokenResponseType
operator|!=
literal|null
operator|&&
operator|!
name|tokenResponseType
operator|.
name|equals
argument_list|(
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|RESPONSE_TYPE
argument_list|)
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|()
throw|;
block|}
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|newParams
init|=
operator|new
name|MetadataMap
argument_list|<>
argument_list|(
name|params
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|claimsMap
init|=
name|claims
operator|.
name|asMap
argument_list|()
decl_stmt|;
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
name|claimsMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Object
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|Map
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|value
argument_list|)
decl_stmt|;
name|value
operator|=
name|jsonHandler
operator|.
name|toJson
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|List
condition|)
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|list
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|value
argument_list|)
decl_stmt|;
name|value
operator|=
name|jsonHandler
operator|.
name|toJson
argument_list|(
name|list
argument_list|)
expr_stmt|;
block|}
name|newParams
operator|.
name|putSingle
argument_list|(
name|key
argument_list|,
name|value
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|newParams
return|;
block|}
return|return
name|params
return|;
block|}
specifier|private
name|boolean
name|isRequestUriValid
parameter_list|(
name|Client
name|client
parameter_list|,
name|String
name|requestUri
parameter_list|)
block|{
comment|//TODO: consider restricting to specific hosts
return|return
name|requestUri
operator|!=
literal|null
operator|&&
name|requestUri
operator|.
name|startsWith
argument_list|(
literal|"https://"
argument_list|)
return|;
block|}
specifier|protected
name|JwsSignatureVerifier
name|getInitializedSigVerifier
parameter_list|(
name|Client
name|c
parameter_list|)
block|{
if|if
condition|(
name|verifyWithClientCertificates
condition|)
block|{
name|X509Certificate
name|cert
init|=
operator|(
name|X509Certificate
operator|)
name|CryptoUtils
operator|.
name|decodeCertificate
argument_list|(
name|c
operator|.
name|getApplicationCertificates
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|JwsUtils
operator|.
name|getPublicKeySignatureVerifier
argument_list|(
name|cert
argument_list|,
name|SignatureAlgorithm
operator|.
name|RS256
argument_list|)
return|;
block|}
return|return
name|super
operator|.
name|getInitializedSignatureVerifier
argument_list|(
name|c
operator|.
name|getClientSecret
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|void
name|setIssuer
parameter_list|(
name|String
name|issuer
parameter_list|)
block|{
name|this
operator|.
name|issuer
operator|=
name|issuer
expr_stmt|;
block|}
specifier|public
name|void
name|setVerifyWithClientCertificates
parameter_list|(
name|boolean
name|verifyWithClientCertificates
parameter_list|)
block|{
name|this
operator|.
name|verifyWithClientCertificates
operator|=
name|verifyWithClientCertificates
expr_stmt|;
block|}
block|}
end_class

end_unit

