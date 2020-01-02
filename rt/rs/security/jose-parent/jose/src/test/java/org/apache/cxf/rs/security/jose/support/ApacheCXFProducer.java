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
name|support
package|;
end_package

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
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
name|Optional
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
name|ContentAlgorithm
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
name|KeyAlgorithm
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
name|JweCompactConsumer
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
name|JweCompactProducer
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
name|JweEncryptionProvider
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
name|JweHeaders
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
name|JweJsonConsumer
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
name|JweJsonProducer
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
name|JweUtils
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
name|JsonWebKeys
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
name|KeyType
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
name|JwsCompactProducer
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
name|JwsHeaders
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
name|JwsJsonProducer
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
name|JwsSignatureProvider
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
name|junit
operator|.
name|Assert
import|;
end_import

begin_class
specifier|public
class|class
name|ApacheCXFProducer
block|{
specifier|public
name|void
name|produceJWS
parameter_list|(
name|String
name|keyType
parameter_list|,
name|String
name|signatureAlgorithm
parameter_list|,
name|Serialization
name|serialization
parameter_list|,
name|String
name|plainText
parameter_list|,
name|String
name|jwksJson
parameter_list|)
block|{
name|JsonWebKeys
name|keys
init|=
name|JwkUtils
operator|.
name|readJwkSet
argument_list|(
name|jwksJson
argument_list|)
decl_stmt|;
name|JsonWebKey
name|key
init|=
name|getRequestedKeyType
argument_list|(
name|keyType
argument_list|,
name|keys
argument_list|)
operator|.
name|orElseThrow
argument_list|(
name|IllegalArgumentException
operator|::
operator|new
argument_list|)
decl_stmt|;
comment|// Sign
name|JwsHeaders
name|jwsHeaders
init|=
operator|new
name|JwsHeaders
argument_list|()
decl_stmt|;
name|jwsHeaders
operator|.
name|setKeyId
argument_list|(
name|key
operator|.
name|getKeyId
argument_list|()
argument_list|)
expr_stmt|;
name|jwsHeaders
operator|.
name|setAlgorithm
argument_list|(
name|signatureAlgorithm
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|serialization
condition|)
block|{
case|case
name|COMPACT
case|:
name|produceCompactJWS
argument_list|(
name|plainText
argument_list|,
name|key
argument_list|,
name|jwsHeaders
argument_list|)
expr_stmt|;
break|break;
case|case
name|FLATTENED
case|:
name|produceJsonJWS
argument_list|(
name|plainText
argument_list|,
name|key
argument_list|,
name|jwsHeaders
argument_list|,
literal|true
argument_list|)
expr_stmt|;
break|break;
case|case
name|JSON
case|:
name|produceJsonJWS
argument_list|(
name|plainText
argument_list|,
name|key
argument_list|,
name|jwsHeaders
argument_list|,
literal|false
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Serialization not supported: "
operator|+
name|serialization
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|produceCompactJWS
parameter_list|(
name|String
name|plainText
parameter_list|,
name|JsonWebKey
name|key
parameter_list|,
name|JwsHeaders
name|jwsHeaders
parameter_list|)
block|{
name|JwsCompactProducer
name|jwsProducer
init|=
operator|new
name|JwsCompactProducer
argument_list|(
name|jwsHeaders
argument_list|,
name|plainText
argument_list|)
decl_stmt|;
name|jwsProducer
operator|.
name|signWith
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|produceJsonJWS
parameter_list|(
name|String
name|plainText
parameter_list|,
name|JsonWebKey
name|key
parameter_list|,
name|JwsHeaders
name|jwsHeaders
parameter_list|,
name|boolean
name|flattened
parameter_list|)
block|{
name|JwsJsonProducer
name|jwsProducer
init|=
operator|new
name|JwsJsonProducer
argument_list|(
name|plainText
argument_list|,
name|flattened
argument_list|)
decl_stmt|;
name|JwsSignatureProvider
name|jwsSignatureProvider
init|=
name|JwsUtils
operator|.
name|getSignatureProvider
argument_list|(
name|key
argument_list|,
name|jwsHeaders
operator|.
name|getSignatureAlgorithm
argument_list|()
argument_list|)
decl_stmt|;
name|jwsProducer
operator|.
name|signWith
argument_list|(
name|jwsSignatureProvider
argument_list|,
literal|null
argument_list|,
name|jwsHeaders
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|produceJWE
parameter_list|(
name|String
name|keyType
parameter_list|,
name|String
name|keyEncryptionAlgorithm
parameter_list|,
name|String
name|contentEncryptionAlgorithm
parameter_list|,
name|Serialization
name|serialization
parameter_list|,
name|String
name|plainText
parameter_list|,
name|String
name|jwksJson
parameter_list|)
block|{
name|JsonWebKeys
name|keys
init|=
name|JwkUtils
operator|.
name|readJwkSet
argument_list|(
name|jwksJson
argument_list|)
decl_stmt|;
name|JsonWebKey
name|key
init|=
name|getRequestedKeyType
argument_list|(
name|keyType
argument_list|,
name|keys
argument_list|)
operator|.
name|orElseThrow
argument_list|(
name|IllegalArgumentException
operator|::
operator|new
argument_list|)
decl_stmt|;
comment|// Encrypt
switch|switch
condition|(
name|serialization
condition|)
block|{
case|case
name|COMPACT
case|:
name|JweHeaders
name|headers
init|=
operator|new
name|JweHeaders
argument_list|()
decl_stmt|;
name|headers
operator|.
name|setKeyId
argument_list|(
name|key
operator|.
name|getKeyId
argument_list|()
argument_list|)
expr_stmt|;
name|headers
operator|.
name|setKeyEncryptionAlgorithm
argument_list|(
name|KeyAlgorithm
operator|.
name|getAlgorithm
argument_list|(
name|keyEncryptionAlgorithm
argument_list|)
argument_list|)
expr_stmt|;
name|headers
operator|.
name|setContentEncryptionAlgorithm
argument_list|(
name|ContentAlgorithm
operator|.
name|getAlgorithm
argument_list|(
name|contentEncryptionAlgorithm
argument_list|)
argument_list|)
expr_stmt|;
name|produceCompactJWE
argument_list|(
name|plainText
argument_list|,
name|key
argument_list|,
name|headers
argument_list|)
expr_stmt|;
break|break;
case|case
name|FLATTENED
case|:
name|produceJsonJWE
argument_list|(
name|keyEncryptionAlgorithm
argument_list|,
name|contentEncryptionAlgorithm
argument_list|,
name|plainText
argument_list|,
name|key
argument_list|,
literal|true
argument_list|)
expr_stmt|;
break|break;
case|case
name|JSON
case|:
name|produceJsonJWE
argument_list|(
name|keyEncryptionAlgorithm
argument_list|,
name|contentEncryptionAlgorithm
argument_list|,
name|plainText
argument_list|,
name|key
argument_list|,
literal|false
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Serialization not supported: "
operator|+
name|serialization
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|produceJsonJWE
parameter_list|(
name|String
name|keyEncryptionAlgorithm
parameter_list|,
name|String
name|contentEncryptionAlgorithm
parameter_list|,
name|String
name|plainText
parameter_list|,
name|JsonWebKey
name|key
parameter_list|,
name|boolean
name|flattened
parameter_list|)
block|{
name|JweHeaders
name|protectedHeaders
init|=
operator|new
name|JweHeaders
argument_list|()
decl_stmt|;
name|protectedHeaders
operator|.
name|setKeyEncryptionAlgorithm
argument_list|(
name|KeyAlgorithm
operator|.
name|getAlgorithm
argument_list|(
name|keyEncryptionAlgorithm
argument_list|)
argument_list|)
expr_stmt|;
name|protectedHeaders
operator|.
name|setContentEncryptionAlgorithm
argument_list|(
name|ContentAlgorithm
operator|.
name|getAlgorithm
argument_list|(
name|contentEncryptionAlgorithm
argument_list|)
argument_list|)
expr_stmt|;
name|JweHeaders
name|recipientHeaders
init|=
operator|new
name|JweHeaders
argument_list|(
name|key
operator|.
name|getKeyId
argument_list|()
argument_list|)
decl_stmt|;
name|produceJsonJWE
argument_list|(
name|plainText
argument_list|,
name|key
argument_list|,
name|protectedHeaders
argument_list|,
literal|null
argument_list|,
name|recipientHeaders
argument_list|,
name|flattened
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|produceCompactJWE
parameter_list|(
name|String
name|plainText
parameter_list|,
name|JsonWebKey
name|key
parameter_list|,
name|JweHeaders
name|headers
parameter_list|)
block|{
name|JweCompactProducer
name|jweProducer
init|=
operator|new
name|JweCompactProducer
argument_list|(
name|headers
argument_list|,
name|plainText
argument_list|)
decl_stmt|;
name|JweEncryptionProvider
name|jweEncryptionProvider
init|=
name|JweUtils
operator|.
name|createJweEncryptionProvider
argument_list|(
name|key
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|String
name|encryptedData
init|=
name|jweProducer
operator|.
name|encryptWith
argument_list|(
name|jweEncryptionProvider
argument_list|)
decl_stmt|;
name|JweCompactConsumer
name|validator
init|=
operator|new
name|JweCompactConsumer
argument_list|(
name|encryptedData
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|headers
operator|.
name|getKeyEncryptionAlgorithm
argument_list|()
argument_list|,
name|validator
operator|.
name|getJweHeaders
argument_list|()
operator|.
name|getKeyEncryptionAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|headers
operator|.
name|getContentEncryptionAlgorithm
argument_list|()
argument_list|,
name|validator
operator|.
name|getJweHeaders
argument_list|()
operator|.
name|getContentEncryptionAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|headers
operator|.
name|getKeyId
argument_list|()
argument_list|,
name|validator
operator|.
name|getJweHeaders
argument_list|()
operator|.
name|getKeyId
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|produceJsonJWE
parameter_list|(
name|String
name|plainText
parameter_list|,
name|JsonWebKey
name|key
parameter_list|,
name|JweHeaders
name|protectedHeaders
parameter_list|,
name|JweHeaders
name|unprotectedJweHeaders
parameter_list|,
name|JweHeaders
name|recipientHeaders
parameter_list|,
name|boolean
name|flattened
parameter_list|)
block|{
name|JweJsonProducer
name|jweProducer
init|=
operator|new
name|JweJsonProducer
argument_list|(
name|protectedHeaders
argument_list|,
name|unprotectedJweHeaders
argument_list|,
name|plainText
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|,
literal|null
argument_list|,
name|flattened
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|union
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|protectedHeaders
operator|!=
literal|null
condition|)
block|{
name|union
operator|.
name|putAll
argument_list|(
name|protectedHeaders
operator|.
name|asMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|unprotectedJweHeaders
operator|!=
literal|null
condition|)
block|{
name|union
operator|.
name|putAll
argument_list|(
name|unprotectedJweHeaders
operator|.
name|asMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|JweHeaders
name|unionHeaders
init|=
operator|new
name|JweHeaders
argument_list|(
name|union
argument_list|)
decl_stmt|;
name|JweEncryptionProvider
name|jweEncryptionProvider
init|=
name|JweUtils
operator|.
name|createJweEncryptionProvider
argument_list|(
name|key
argument_list|,
name|unionHeaders
argument_list|)
decl_stmt|;
name|String
name|encryptedData
init|=
name|jweProducer
operator|.
name|encryptWith
argument_list|(
name|jweEncryptionProvider
argument_list|,
name|recipientHeaders
argument_list|)
decl_stmt|;
name|JweJsonConsumer
name|validator
init|=
operator|new
name|JweJsonConsumer
argument_list|(
name|encryptedData
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|protectedHeaders
operator|.
name|getKeyEncryptionAlgorithm
argument_list|()
argument_list|,
name|validator
operator|.
name|getProtectedHeader
argument_list|()
operator|.
name|getKeyEncryptionAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|protectedHeaders
operator|.
name|getContentEncryptionAlgorithm
argument_list|()
argument_list|,
name|validator
operator|.
name|getProtectedHeader
argument_list|()
operator|.
name|getContentEncryptionAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|validator
operator|.
name|getRecipients
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|recipientHeaders
operator|.
name|getKeyId
argument_list|()
argument_list|,
name|validator
operator|.
name|getRecipients
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getUnprotectedHeader
argument_list|()
operator|.
name|getKeyId
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Optional
argument_list|<
name|JsonWebKey
argument_list|>
name|getRequestedKeyType
parameter_list|(
name|String
name|keyType
parameter_list|,
name|JsonWebKeys
name|keys
parameter_list|)
block|{
name|KeyType
name|kty
init|=
name|KeyType
operator|.
name|valueOf
argument_list|(
name|keyType
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|kty
condition|)
block|{
case|case
name|EC
case|:
return|return
name|keys
operator|.
name|getEllipticKeys
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|findFirst
argument_list|()
return|;
case|case
name|RSA
case|:
return|return
name|keys
operator|.
name|getRsaKeys
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|findFirst
argument_list|()
return|;
case|case
name|OCTET
case|:
return|return
name|keys
operator|.
name|getSecretKeys
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|findFirst
argument_list|()
return|;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"KeyType not supported: "
operator|+
name|kty
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit
