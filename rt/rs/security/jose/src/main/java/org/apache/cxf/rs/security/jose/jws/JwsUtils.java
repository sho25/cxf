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
name|jose
operator|.
name|jws
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|interfaces
operator|.
name|RSAPrivateKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|interfaces
operator|.
name|RSAPublicKey
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
name|Properties
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
name|utils
operator|.
name|ResourceUtils
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
name|jose
operator|.
name|JoseHeaders
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
name|JoseUtils
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
name|jaxrs
operator|.
name|KeyManagementUtils
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
name|Algorithm
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
name|jwk
operator|.
name|JsonWebKey
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
name|jwk
operator|.
name|JwkUtils
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|JwsUtils
block|{
specifier|private
specifier|static
specifier|final
name|String
name|JSON_WEB_SIGNATURE_ALGO_PROP
init|=
literal|"rs.security.jws.content.signature.algorithm"
decl_stmt|;
specifier|private
name|JwsUtils
parameter_list|()
block|{              }
specifier|public
specifier|static
name|String
name|sign
parameter_list|(
name|RSAPrivateKey
name|key
parameter_list|,
name|String
name|algo
parameter_list|,
name|String
name|content
parameter_list|)
block|{
return|return
name|sign
argument_list|(
name|getRSAKeySignatureProvider
argument_list|(
name|key
argument_list|,
name|algo
argument_list|)
argument_list|,
name|content
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|sign
parameter_list|(
name|byte
index|[]
name|key
parameter_list|,
name|String
name|algo
parameter_list|,
name|String
name|content
parameter_list|)
block|{
return|return
name|sign
argument_list|(
name|getHmacSignatureProvider
argument_list|(
name|key
argument_list|,
name|algo
argument_list|)
argument_list|,
name|content
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|verifyAndGetContent
parameter_list|(
name|RSAPublicKey
name|key
parameter_list|,
name|String
name|algo
parameter_list|,
name|String
name|content
parameter_list|)
block|{
name|JwsCompactConsumer
name|jws
init|=
name|verify
argument_list|(
name|getRSAKeySignatureVerifier
argument_list|(
name|key
argument_list|,
name|algo
argument_list|)
argument_list|,
name|content
argument_list|)
decl_stmt|;
return|return
name|jws
operator|.
name|getDecodedJwsPayload
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|String
name|verifyAndGetContent
parameter_list|(
name|byte
index|[]
name|key
parameter_list|,
name|String
name|algo
parameter_list|,
name|String
name|content
parameter_list|)
block|{
name|JwsCompactConsumer
name|jws
init|=
name|verify
argument_list|(
name|getHmacSignatureVerifier
argument_list|(
name|key
argument_list|,
name|algo
argument_list|)
argument_list|,
name|content
argument_list|)
decl_stmt|;
return|return
name|jws
operator|.
name|getDecodedJwsPayload
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|JwsSignatureProvider
name|getSignatureProvider
parameter_list|(
name|JsonWebKey
name|jwk
parameter_list|)
block|{
return|return
name|getSignatureProvider
argument_list|(
name|jwk
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JwsSignatureProvider
name|getSignatureProvider
parameter_list|(
name|JsonWebKey
name|jwk
parameter_list|,
name|String
name|defaultAlgorithm
parameter_list|)
block|{
name|String
name|rsaSignatureAlgo
init|=
name|jwk
operator|.
name|getAlgorithm
argument_list|()
operator|==
literal|null
condition|?
name|defaultAlgorithm
else|:
name|jwk
operator|.
name|getAlgorithm
argument_list|()
decl_stmt|;
name|JwsSignatureProvider
name|theSigProvider
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|JsonWebKey
operator|.
name|KEY_TYPE_RSA
operator|.
name|equals
argument_list|(
name|jwk
operator|.
name|getKeyType
argument_list|()
argument_list|)
condition|)
block|{
name|theSigProvider
operator|=
name|getRSAKeySignatureProvider
argument_list|(
name|JwkUtils
operator|.
name|toRSAPrivateKey
argument_list|(
name|jwk
argument_list|)
argument_list|,
name|rsaSignatureAlgo
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|JsonWebKey
operator|.
name|KEY_TYPE_OCTET
operator|.
name|equals
argument_list|(
name|jwk
operator|.
name|getKeyType
argument_list|()
argument_list|)
condition|)
block|{
name|byte
index|[]
name|key
init|=
name|JoseUtils
operator|.
name|decode
argument_list|(
operator|(
name|String
operator|)
name|jwk
operator|.
name|getProperty
argument_list|(
name|JsonWebKey
operator|.
name|OCTET_KEY_VALUE
argument_list|)
argument_list|)
decl_stmt|;
name|theSigProvider
operator|=
name|getHmacSignatureProvider
argument_list|(
name|key
argument_list|,
name|rsaSignatureAlgo
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|JsonWebKey
operator|.
name|KEY_TYPE_ELLIPTIC
operator|.
name|equals
argument_list|(
name|jwk
operator|.
name|getKeyType
argument_list|()
argument_list|)
condition|)
block|{
name|theSigProvider
operator|=
operator|new
name|EcDsaJwsSignatureProvider
argument_list|(
name|JwkUtils
operator|.
name|toECPrivateKey
argument_list|(
name|jwk
argument_list|)
argument_list|,
name|rsaSignatureAlgo
argument_list|)
expr_stmt|;
block|}
return|return
name|theSigProvider
return|;
block|}
specifier|public
specifier|static
name|JwsSignatureProvider
name|getRSAKeySignatureProvider
parameter_list|(
name|RSAPrivateKey
name|key
parameter_list|,
name|String
name|algo
parameter_list|)
block|{
return|return
operator|new
name|PrivateKeyJwsSignatureProvider
argument_list|(
name|key
argument_list|,
name|algo
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JwsSignatureProvider
name|getHmacSignatureProvider
parameter_list|(
name|byte
index|[]
name|key
parameter_list|,
name|String
name|algo
parameter_list|)
block|{
if|if
condition|(
name|Algorithm
operator|.
name|isHmacSign
argument_list|(
name|algo
argument_list|)
condition|)
block|{
return|return
operator|new
name|HmacJwsSignatureProvider
argument_list|(
name|key
argument_list|,
name|algo
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|JwsSignatureVerifier
name|getSignatureVerifier
parameter_list|(
name|JsonWebKey
name|jwk
parameter_list|)
block|{
return|return
name|getSignatureVerifier
argument_list|(
name|jwk
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JwsSignatureVerifier
name|getSignatureVerifier
parameter_list|(
name|JsonWebKey
name|jwk
parameter_list|,
name|String
name|defaultAlgorithm
parameter_list|)
block|{
name|String
name|rsaSignatureAlgo
init|=
name|jwk
operator|.
name|getAlgorithm
argument_list|()
operator|==
literal|null
condition|?
name|defaultAlgorithm
else|:
name|jwk
operator|.
name|getAlgorithm
argument_list|()
decl_stmt|;
name|JwsSignatureVerifier
name|theVerifier
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|JsonWebKey
operator|.
name|KEY_TYPE_RSA
operator|.
name|equals
argument_list|(
name|jwk
operator|.
name|getKeyType
argument_list|()
argument_list|)
condition|)
block|{
name|theVerifier
operator|=
name|getRSAKeySignatureVerifier
argument_list|(
name|JwkUtils
operator|.
name|toRSAPublicKey
argument_list|(
name|jwk
argument_list|)
argument_list|,
name|rsaSignatureAlgo
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|JsonWebKey
operator|.
name|KEY_TYPE_OCTET
operator|.
name|equals
argument_list|(
name|jwk
operator|.
name|getKeyType
argument_list|()
argument_list|)
condition|)
block|{
name|byte
index|[]
name|key
init|=
name|JoseUtils
operator|.
name|decode
argument_list|(
operator|(
name|String
operator|)
name|jwk
operator|.
name|getProperty
argument_list|(
name|JsonWebKey
operator|.
name|OCTET_KEY_VALUE
argument_list|)
argument_list|)
decl_stmt|;
name|theVerifier
operator|=
name|getHmacSignatureVerifier
argument_list|(
name|key
argument_list|,
name|rsaSignatureAlgo
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|JsonWebKey
operator|.
name|KEY_TYPE_ELLIPTIC
operator|.
name|equals
argument_list|(
name|jwk
operator|.
name|getKeyType
argument_list|()
argument_list|)
condition|)
block|{
name|theVerifier
operator|=
operator|new
name|EcDsaJwsSignatureVerifier
argument_list|(
name|JwkUtils
operator|.
name|toECPublicKey
argument_list|(
name|jwk
argument_list|)
argument_list|,
name|rsaSignatureAlgo
argument_list|)
expr_stmt|;
block|}
return|return
name|theVerifier
return|;
block|}
specifier|public
specifier|static
name|JwsSignatureVerifier
name|getRSAKeySignatureVerifier
parameter_list|(
name|RSAPublicKey
name|key
parameter_list|,
name|String
name|algo
parameter_list|)
block|{
return|return
operator|new
name|PublicKeyJwsSignatureVerifier
argument_list|(
name|key
argument_list|,
name|algo
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JwsSignatureVerifier
name|getHmacSignatureVerifier
parameter_list|(
name|byte
index|[]
name|key
parameter_list|,
name|String
name|algo
parameter_list|)
block|{
if|if
condition|(
name|Algorithm
operator|.
name|isHmacSign
argument_list|(
name|algo
argument_list|)
condition|)
block|{
return|return
operator|new
name|HmacJwsSignatureVerifier
argument_list|(
name|key
argument_list|,
name|algo
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|JwsJsonSignatureEntry
argument_list|>
name|getJwsJsonSignatureMap
parameter_list|(
name|List
argument_list|<
name|JwsJsonSignatureEntry
argument_list|>
name|signatures
parameter_list|)
block|{
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|JwsJsonSignatureEntry
argument_list|>
name|map
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|JwsJsonSignatureEntry
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|JwsJsonSignatureEntry
name|entry
range|:
name|signatures
control|)
block|{
name|map
operator|.
name|add
argument_list|(
name|entry
operator|.
name|getUnionHeader
argument_list|()
operator|.
name|getAlgorithm
argument_list|()
argument_list|,
name|entry
argument_list|)
expr_stmt|;
block|}
return|return
name|map
return|;
block|}
specifier|public
specifier|static
name|JwsSignatureProvider
name|loadSignatureProvider
parameter_list|(
name|String
name|propLoc
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
return|return
name|loadSignatureProvider
argument_list|(
name|propLoc
argument_list|,
name|m
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|JwsSignatureProvider
argument_list|>
name|loadSignatureProviders
parameter_list|(
name|String
name|propLoc
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
name|Properties
name|props
init|=
name|loadProperties
argument_list|(
name|m
argument_list|,
name|propLoc
argument_list|)
decl_stmt|;
name|JwsSignatureProvider
name|theSigProvider
init|=
name|loadSignatureProvider
argument_list|(
name|propLoc
argument_list|,
name|m
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|theSigProvider
operator|!=
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|theSigProvider
argument_list|)
return|;
block|}
name|List
argument_list|<
name|JwsSignatureProvider
argument_list|>
name|theSigProviders
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|JwkUtils
operator|.
name|JWK_KEY_STORE_TYPE
operator|.
name|equals
argument_list|(
name|props
operator|.
name|get
argument_list|(
name|KeyManagementUtils
operator|.
name|RSSEC_KEY_STORE_TYPE
argument_list|)
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|JsonWebKey
argument_list|>
name|jwks
init|=
name|JwkUtils
operator|.
name|loadJsonWebKeys
argument_list|(
name|m
argument_list|,
name|props
argument_list|,
name|JsonWebKey
operator|.
name|KEY_OPER_SIGN
argument_list|)
decl_stmt|;
if|if
condition|(
name|jwks
operator|!=
literal|null
condition|)
block|{
name|theSigProviders
operator|=
operator|new
name|ArrayList
argument_list|<
name|JwsSignatureProvider
argument_list|>
argument_list|(
name|jwks
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|JsonWebKey
name|jwk
range|:
name|jwks
control|)
block|{
name|theSigProviders
operator|.
name|add
argument_list|(
name|JwsUtils
operator|.
name|getSignatureProvider
argument_list|(
name|jwk
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|theSigProviders
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|()
throw|;
block|}
return|return
name|theSigProviders
return|;
block|}
specifier|public
specifier|static
name|JwsSignatureVerifier
name|loadSignatureVerifier
parameter_list|(
name|String
name|propLoc
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
return|return
name|loadSignatureVerifier
argument_list|(
name|propLoc
argument_list|,
name|m
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|JwsSignatureVerifier
argument_list|>
name|loadSignatureVerifiers
parameter_list|(
name|String
name|propLoc
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
name|Properties
name|props
init|=
name|loadProperties
argument_list|(
name|m
argument_list|,
name|propLoc
argument_list|)
decl_stmt|;
name|JwsSignatureVerifier
name|theVerifier
init|=
name|loadSignatureVerifier
argument_list|(
name|propLoc
argument_list|,
name|m
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|theVerifier
operator|!=
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|theVerifier
argument_list|)
return|;
block|}
name|List
argument_list|<
name|JwsSignatureVerifier
argument_list|>
name|theVerifiers
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|JwkUtils
operator|.
name|JWK_KEY_STORE_TYPE
operator|.
name|equals
argument_list|(
name|props
operator|.
name|get
argument_list|(
name|KeyManagementUtils
operator|.
name|RSSEC_KEY_STORE_TYPE
argument_list|)
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|JsonWebKey
argument_list|>
name|jwks
init|=
name|JwkUtils
operator|.
name|loadJsonWebKeys
argument_list|(
name|m
argument_list|,
name|props
argument_list|,
name|JsonWebKey
operator|.
name|KEY_OPER_SIGN
argument_list|)
decl_stmt|;
if|if
condition|(
name|jwks
operator|!=
literal|null
condition|)
block|{
name|theVerifiers
operator|=
operator|new
name|ArrayList
argument_list|<
name|JwsSignatureVerifier
argument_list|>
argument_list|(
name|jwks
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|JsonWebKey
name|jwk
range|:
name|jwks
control|)
block|{
name|theVerifiers
operator|.
name|add
argument_list|(
name|JwsUtils
operator|.
name|getSignatureVerifier
argument_list|(
name|jwk
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|theVerifiers
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|()
throw|;
block|}
return|return
name|theVerifiers
return|;
block|}
specifier|public
specifier|static
name|boolean
name|validateCriticalHeaders
parameter_list|(
name|JoseHeaders
name|headers
parameter_list|)
block|{
comment|//TODO: validate JWS specific constraints
return|return
name|JoseUtils
operator|.
name|validateCriticalHeaders
argument_list|(
name|headers
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|JwsSignatureProvider
name|loadSignatureProvider
parameter_list|(
name|String
name|propLoc
parameter_list|,
name|Message
name|m
parameter_list|,
name|boolean
name|ignoreNullProvider
parameter_list|)
block|{
name|Properties
name|props
init|=
name|loadProperties
argument_list|(
name|m
argument_list|,
name|propLoc
argument_list|)
decl_stmt|;
name|JwsSignatureProvider
name|theSigProvider
init|=
literal|null
decl_stmt|;
name|String
name|rsaSignatureAlgo
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|JwkUtils
operator|.
name|JWK_KEY_STORE_TYPE
operator|.
name|equals
argument_list|(
name|props
operator|.
name|get
argument_list|(
name|KeyManagementUtils
operator|.
name|RSSEC_KEY_STORE_TYPE
argument_list|)
argument_list|)
condition|)
block|{
name|JsonWebKey
name|jwk
init|=
name|JwkUtils
operator|.
name|loadJsonWebKey
argument_list|(
name|m
argument_list|,
name|props
argument_list|,
name|JsonWebKey
operator|.
name|KEY_OPER_SIGN
argument_list|)
decl_stmt|;
if|if
condition|(
name|jwk
operator|!=
literal|null
condition|)
block|{
name|rsaSignatureAlgo
operator|=
name|getSignatureAlgo
argument_list|(
name|props
argument_list|,
name|jwk
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
name|theSigProvider
operator|=
name|JwsUtils
operator|.
name|getSignatureProvider
argument_list|(
name|jwk
argument_list|,
name|rsaSignatureAlgo
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|rsaSignatureAlgo
operator|=
name|getSignatureAlgo
argument_list|(
name|props
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|RSAPrivateKey
name|pk
init|=
operator|(
name|RSAPrivateKey
operator|)
name|KeyManagementUtils
operator|.
name|loadPrivateKey
argument_list|(
name|m
argument_list|,
name|props
argument_list|,
name|KeyManagementUtils
operator|.
name|RSSEC_SIG_KEY_PSWD_PROVIDER
argument_list|)
decl_stmt|;
name|theSigProvider
operator|=
name|getRSAKeySignatureProvider
argument_list|(
name|pk
argument_list|,
name|rsaSignatureAlgo
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|theSigProvider
operator|==
literal|null
operator|&&
operator|!
name|ignoreNullProvider
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|()
throw|;
block|}
return|return
name|theSigProvider
return|;
block|}
specifier|private
specifier|static
name|JwsSignatureVerifier
name|loadSignatureVerifier
parameter_list|(
name|String
name|propLoc
parameter_list|,
name|Message
name|m
parameter_list|,
name|boolean
name|ignoreNullVerifier
parameter_list|)
block|{
name|Properties
name|props
init|=
name|loadProperties
argument_list|(
name|m
argument_list|,
name|propLoc
argument_list|)
decl_stmt|;
name|JwsSignatureVerifier
name|theVerifier
init|=
literal|null
decl_stmt|;
name|String
name|rsaSignatureAlgo
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|JwkUtils
operator|.
name|JWK_KEY_STORE_TYPE
operator|.
name|equals
argument_list|(
name|props
operator|.
name|get
argument_list|(
name|KeyManagementUtils
operator|.
name|RSSEC_KEY_STORE_TYPE
argument_list|)
argument_list|)
condition|)
block|{
name|JsonWebKey
name|jwk
init|=
name|JwkUtils
operator|.
name|loadJsonWebKey
argument_list|(
name|m
argument_list|,
name|props
argument_list|,
name|JsonWebKey
operator|.
name|KEY_OPER_VERIFY
argument_list|)
decl_stmt|;
if|if
condition|(
name|jwk
operator|!=
literal|null
condition|)
block|{
name|rsaSignatureAlgo
operator|=
name|getSignatureAlgo
argument_list|(
name|props
argument_list|,
name|jwk
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
name|theVerifier
operator|=
name|JwsUtils
operator|.
name|getSignatureVerifier
argument_list|(
name|jwk
argument_list|,
name|rsaSignatureAlgo
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|rsaSignatureAlgo
operator|=
name|getSignatureAlgo
argument_list|(
name|props
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|theVerifier
operator|=
name|getRSAKeySignatureVerifier
argument_list|(
operator|(
name|RSAPublicKey
operator|)
name|KeyManagementUtils
operator|.
name|loadPublicKey
argument_list|(
name|m
argument_list|,
name|props
argument_list|)
argument_list|,
name|rsaSignatureAlgo
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|theVerifier
operator|==
literal|null
operator|&&
operator|!
name|ignoreNullVerifier
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|()
throw|;
block|}
return|return
name|theVerifier
return|;
block|}
specifier|private
specifier|static
name|Properties
name|loadProperties
parameter_list|(
name|Message
name|m
parameter_list|,
name|String
name|propLoc
parameter_list|)
block|{
try|try
block|{
return|return
name|ResourceUtils
operator|.
name|loadProperties
argument_list|(
name|propLoc
argument_list|,
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getBus
argument_list|()
argument_list|)
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
name|SecurityException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|private
specifier|static
name|String
name|getSignatureAlgo
parameter_list|(
name|Properties
name|props
parameter_list|,
name|String
name|algo
parameter_list|)
block|{
return|return
name|algo
operator|==
literal|null
condition|?
name|props
operator|.
name|getProperty
argument_list|(
name|JSON_WEB_SIGNATURE_ALGO_PROP
argument_list|)
else|:
name|algo
return|;
block|}
specifier|private
specifier|static
name|JwsCompactConsumer
name|verify
parameter_list|(
name|JwsSignatureVerifier
name|v
parameter_list|,
name|String
name|content
parameter_list|)
block|{
name|JwsCompactConsumer
name|jws
init|=
operator|new
name|JwsCompactConsumer
argument_list|(
name|content
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|jws
operator|.
name|verifySignatureWith
argument_list|(
name|v
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|()
throw|;
block|}
return|return
name|jws
return|;
block|}
specifier|private
specifier|static
name|String
name|sign
parameter_list|(
name|JwsSignatureProvider
name|jwsSig
parameter_list|,
name|String
name|content
parameter_list|)
block|{
name|JwsCompactProducer
name|jws
init|=
operator|new
name|JwsCompactProducer
argument_list|(
name|content
argument_list|)
decl_stmt|;
name|jws
operator|.
name|signWith
argument_list|(
name|jwsSig
argument_list|)
expr_stmt|;
return|return
name|jws
operator|.
name|getSignedEncodedJws
argument_list|()
return|;
block|}
block|}
end_class

end_unit

