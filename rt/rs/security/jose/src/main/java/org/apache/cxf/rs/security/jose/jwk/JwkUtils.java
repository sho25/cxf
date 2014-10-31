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
name|jwk
package|;
end_package

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
name|interfaces
operator|.
name|ECPrivateKey
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
name|ECPublicKey
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
name|crypto
operator|.
name|SecretKey
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
name|Bus
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
name|StringUtils
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
name|crypto
operator|.
name|CryptoUtils
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
name|IOUtils
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
name|jaxrs
operator|.
name|PrivateKeyPasswordProvider
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
name|jwe
operator|.
name|AesCbcHmacJweDecryption
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
name|AesCbcHmacJweEncryption
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
name|KeyDecryptionAlgorithm
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
name|KeyEncryptionAlgorithm
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
name|PbesHmacAesWrapKeyDecryptionAlgorithm
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
name|PbesHmacAesWrapKeyEncryptionAlgorithm
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|JwkUtils
block|{
specifier|public
specifier|static
specifier|final
name|String
name|JWK_KEY_STORE_TYPE
init|=
literal|"jwk"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_KEY_STORE_JWKSET
init|=
literal|"rs.security.keystore.jwkset"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_KEY_STORE_JWKKEY
init|=
literal|"rs.security.keystore.jwkkey"
decl_stmt|;
specifier|private
name|JwkUtils
parameter_list|()
block|{              }
specifier|public
specifier|static
name|JsonWebKey
name|readJwkKey
parameter_list|(
name|URI
name|uri
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|readJwkKey
argument_list|(
name|uri
operator|.
name|toURL
argument_list|()
operator|.
name|openStream
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JsonWebKeys
name|readJwkSet
parameter_list|(
name|URI
name|uri
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|readJwkSet
argument_list|(
name|uri
operator|.
name|toURL
argument_list|()
operator|.
name|openStream
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JsonWebKey
name|readJwkKey
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|readJwkKey
argument_list|(
name|IOUtils
operator|.
name|readStringFromStream
argument_list|(
name|is
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JsonWebKeys
name|readJwkSet
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|readJwkSet
argument_list|(
name|IOUtils
operator|.
name|readStringFromStream
argument_list|(
name|is
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JsonWebKey
name|readJwkKey
parameter_list|(
name|String
name|jwkJson
parameter_list|)
block|{
return|return
operator|new
name|DefaultJwkReaderWriter
argument_list|()
operator|.
name|jsonToJwk
argument_list|(
name|jwkJson
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JsonWebKeys
name|readJwkSet
parameter_list|(
name|String
name|jwksJson
parameter_list|)
block|{
return|return
operator|new
name|DefaultJwkReaderWriter
argument_list|()
operator|.
name|jsonToJwkSet
argument_list|(
name|jwksJson
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|jwkKeyToJson
parameter_list|(
name|JsonWebKey
name|jwkKey
parameter_list|)
block|{
return|return
operator|new
name|DefaultJwkReaderWriter
argument_list|()
operator|.
name|jwkToJson
argument_list|(
name|jwkKey
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|jwkSetToJson
parameter_list|(
name|JsonWebKeys
name|jwkSet
parameter_list|)
block|{
return|return
operator|new
name|DefaultJwkReaderWriter
argument_list|()
operator|.
name|jwkSetToJson
argument_list|(
name|jwkSet
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|encryptJwkSet
parameter_list|(
name|JsonWebKeys
name|jwkSet
parameter_list|,
name|char
index|[]
name|password
parameter_list|)
block|{
return|return
name|encryptJwkSet
argument_list|(
name|jwkSet
argument_list|,
name|password
argument_list|,
operator|new
name|DefaultJwkReaderWriter
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|encryptJwkSet
parameter_list|(
name|JsonWebKeys
name|jwkSet
parameter_list|,
name|char
index|[]
name|password
parameter_list|,
name|JwkReaderWriter
name|writer
parameter_list|)
block|{
return|return
name|encryptJwkSet
argument_list|(
name|jwkSet
argument_list|,
name|createDefaultEncryption
argument_list|(
name|password
argument_list|)
argument_list|,
name|writer
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|encryptJwkSet
parameter_list|(
name|JsonWebKeys
name|jwkSet
parameter_list|,
name|JweEncryptionProvider
name|jwe
parameter_list|,
name|JwkReaderWriter
name|writer
parameter_list|)
block|{
return|return
name|jwe
operator|.
name|encrypt
argument_list|(
name|stringToBytes
argument_list|(
name|writer
operator|.
name|jwkSetToJson
argument_list|(
name|jwkSet
argument_list|)
argument_list|)
argument_list|,
literal|"jwk-set+json"
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JsonWebKeys
name|decryptJwkSet
parameter_list|(
name|String
name|jsonJwkSet
parameter_list|,
name|char
index|[]
name|password
parameter_list|)
block|{
return|return
name|decryptJwkSet
argument_list|(
name|jsonJwkSet
argument_list|,
name|password
argument_list|,
operator|new
name|DefaultJwkReaderWriter
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JsonWebKeys
name|decryptJwkSet
parameter_list|(
name|String
name|jsonJwkSet
parameter_list|,
name|char
index|[]
name|password
parameter_list|,
name|JwkReaderWriter
name|reader
parameter_list|)
block|{
return|return
name|decryptJwkSet
argument_list|(
name|jsonJwkSet
argument_list|,
name|createDefaultDecryption
argument_list|(
name|password
argument_list|)
argument_list|,
name|reader
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JsonWebKeys
name|decryptJwkSet
parameter_list|(
name|String
name|jsonJwkSet
parameter_list|,
name|JweDecryptionProvider
name|jwe
parameter_list|,
name|JwkReaderWriter
name|reader
parameter_list|)
block|{
return|return
name|reader
operator|.
name|jsonToJwkSet
argument_list|(
name|jwe
operator|.
name|decrypt
argument_list|(
name|jsonJwkSet
argument_list|)
operator|.
name|getContentText
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JsonWebKeys
name|decryptJwkSet
parameter_list|(
name|InputStream
name|is
parameter_list|,
name|char
index|[]
name|password
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|decryptJwkSet
argument_list|(
name|is
argument_list|,
name|password
argument_list|,
operator|new
name|DefaultJwkReaderWriter
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JsonWebKeys
name|decryptJwkSet
parameter_list|(
name|InputStream
name|is
parameter_list|,
name|char
index|[]
name|password
parameter_list|,
name|JwkReaderWriter
name|reader
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|decryptJwkSet
argument_list|(
name|is
argument_list|,
name|createDefaultDecryption
argument_list|(
name|password
argument_list|)
argument_list|,
name|reader
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JsonWebKeys
name|decryptJwkSet
parameter_list|(
name|InputStream
name|is
parameter_list|,
name|JweDecryptionProvider
name|jwe
parameter_list|,
name|JwkReaderWriter
name|reader
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|reader
operator|.
name|jsonToJwkSet
argument_list|(
name|jwe
operator|.
name|decrypt
argument_list|(
name|IOUtils
operator|.
name|readStringFromStream
argument_list|(
name|is
argument_list|)
argument_list|)
operator|.
name|getContentText
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|encryptJwkKey
parameter_list|(
name|JsonWebKey
name|jwk
parameter_list|,
name|char
index|[]
name|password
parameter_list|)
block|{
return|return
name|encryptJwkKey
argument_list|(
name|jwk
argument_list|,
name|password
argument_list|,
operator|new
name|DefaultJwkReaderWriter
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|encryptJwkKey
parameter_list|(
name|JsonWebKey
name|jwkKey
parameter_list|,
name|char
index|[]
name|password
parameter_list|,
name|JwkReaderWriter
name|writer
parameter_list|)
block|{
return|return
name|encryptJwkKey
argument_list|(
name|jwkKey
argument_list|,
name|createDefaultEncryption
argument_list|(
name|password
argument_list|)
argument_list|,
name|writer
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|encryptJwkKey
parameter_list|(
name|JsonWebKey
name|jwkKey
parameter_list|,
name|JweEncryptionProvider
name|jwe
parameter_list|,
name|JwkReaderWriter
name|writer
parameter_list|)
block|{
return|return
name|jwe
operator|.
name|encrypt
argument_list|(
name|stringToBytes
argument_list|(
name|writer
operator|.
name|jwkToJson
argument_list|(
name|jwkKey
argument_list|)
argument_list|)
argument_list|,
literal|"jwk+json"
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JsonWebKey
name|decryptJwkKey
parameter_list|(
name|String
name|jsonJwkKey
parameter_list|,
name|char
index|[]
name|password
parameter_list|)
block|{
return|return
name|decryptJwkKey
argument_list|(
name|jsonJwkKey
argument_list|,
name|password
argument_list|,
operator|new
name|DefaultJwkReaderWriter
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JsonWebKey
name|decryptJwkKey
parameter_list|(
name|String
name|jsonJwkKey
parameter_list|,
name|char
index|[]
name|password
parameter_list|,
name|JwkReaderWriter
name|reader
parameter_list|)
block|{
return|return
name|decryptJwkKey
argument_list|(
name|jsonJwkKey
argument_list|,
name|createDefaultDecryption
argument_list|(
name|password
argument_list|)
argument_list|,
name|reader
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JsonWebKey
name|decryptJwkKey
parameter_list|(
name|String
name|jsonJwkKey
parameter_list|,
name|JweDecryptionProvider
name|jwe
parameter_list|,
name|JwkReaderWriter
name|reader
parameter_list|)
block|{
return|return
name|reader
operator|.
name|jsonToJwk
argument_list|(
name|jwe
operator|.
name|decrypt
argument_list|(
name|jsonJwkKey
argument_list|)
operator|.
name|getContentText
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JsonWebKey
name|decryptJwkKey
parameter_list|(
name|InputStream
name|is
parameter_list|,
name|char
index|[]
name|password
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|decryptJwkKey
argument_list|(
name|is
argument_list|,
name|password
argument_list|,
operator|new
name|DefaultJwkReaderWriter
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JsonWebKey
name|decryptJwkKey
parameter_list|(
name|InputStream
name|is
parameter_list|,
name|char
index|[]
name|password
parameter_list|,
name|JwkReaderWriter
name|reader
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|decryptJwkKey
argument_list|(
name|is
argument_list|,
name|createDefaultDecryption
argument_list|(
name|password
argument_list|)
argument_list|,
name|reader
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JsonWebKey
name|decryptJwkKey
parameter_list|(
name|InputStream
name|is
parameter_list|,
name|JweDecryptionProvider
name|jwe
parameter_list|,
name|JwkReaderWriter
name|reader
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|reader
operator|.
name|jsonToJwk
argument_list|(
name|jwe
operator|.
name|decrypt
argument_list|(
name|IOUtils
operator|.
name|readStringFromStream
argument_list|(
name|is
argument_list|)
argument_list|)
operator|.
name|getContentText
argument_list|()
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|JweEncryptionProvider
name|createDefaultEncryption
parameter_list|(
name|char
index|[]
name|password
parameter_list|)
block|{
name|KeyEncryptionAlgorithm
name|keyEncryption
init|=
operator|new
name|PbesHmacAesWrapKeyEncryptionAlgorithm
argument_list|(
name|password
argument_list|,
name|Algorithm
operator|.
name|PBES2_HS256_A128KW
operator|.
name|getJwtName
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|AesCbcHmacJweEncryption
argument_list|(
name|Algorithm
operator|.
name|A128CBC_HS256
operator|.
name|getJwtName
argument_list|()
argument_list|,
name|keyEncryption
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|JweDecryptionProvider
name|createDefaultDecryption
parameter_list|(
name|char
index|[]
name|password
parameter_list|)
block|{
name|KeyDecryptionAlgorithm
name|keyDecryption
init|=
operator|new
name|PbesHmacAesWrapKeyDecryptionAlgorithm
argument_list|(
name|password
argument_list|)
decl_stmt|;
return|return
operator|new
name|AesCbcHmacJweDecryption
argument_list|(
name|keyDecryption
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JsonWebKeys
name|loadJwkSet
parameter_list|(
name|Message
name|m
parameter_list|,
name|Properties
name|props
parameter_list|,
name|PrivateKeyPasswordProvider
name|cb
parameter_list|)
block|{
return|return
name|loadJwkSet
argument_list|(
name|m
argument_list|,
name|props
argument_list|,
name|cb
argument_list|,
operator|new
name|DefaultJwkReaderWriter
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JsonWebKeys
name|loadJwkSet
parameter_list|(
name|Message
name|m
parameter_list|,
name|Properties
name|props
parameter_list|,
name|PrivateKeyPasswordProvider
name|cb
parameter_list|,
name|JwkReaderWriter
name|reader
parameter_list|)
block|{
name|JsonWebKeys
name|jwkSet
init|=
operator|(
name|JsonWebKeys
operator|)
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|props
operator|.
name|get
argument_list|(
name|KeyManagementUtils
operator|.
name|RSSEC_KEY_STORE_FILE
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|jwkSet
operator|==
literal|null
condition|)
block|{
name|jwkSet
operator|=
name|loadJwkSet
argument_list|(
name|props
argument_list|,
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getBus
argument_list|()
argument_list|,
name|cb
argument_list|,
name|reader
argument_list|)
expr_stmt|;
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
operator|(
name|String
operator|)
name|props
operator|.
name|get
argument_list|(
name|KeyManagementUtils
operator|.
name|RSSEC_KEY_STORE_FILE
argument_list|)
argument_list|,
name|jwkSet
argument_list|)
expr_stmt|;
block|}
return|return
name|jwkSet
return|;
block|}
specifier|public
specifier|static
name|JsonWebKeys
name|loadJwkSet
parameter_list|(
name|Properties
name|props
parameter_list|,
name|Bus
name|bus
parameter_list|,
name|PrivateKeyPasswordProvider
name|cb
parameter_list|)
block|{
return|return
name|loadJwkSet
argument_list|(
name|props
argument_list|,
name|bus
argument_list|,
name|cb
argument_list|,
operator|new
name|DefaultJwkReaderWriter
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JsonWebKeys
name|loadJwkSet
parameter_list|(
name|Properties
name|props
parameter_list|,
name|Bus
name|bus
parameter_list|,
name|PrivateKeyPasswordProvider
name|cb
parameter_list|,
name|JwkReaderWriter
name|reader
parameter_list|)
block|{
name|JweDecryptionProvider
name|decryption
init|=
name|cb
operator|!=
literal|null
condition|?
operator|new
name|AesCbcHmacJweDecryption
argument_list|(
operator|new
name|PbesHmacAesWrapKeyDecryptionAlgorithm
argument_list|(
name|cb
operator|.
name|getPassword
argument_list|(
name|props
argument_list|)
argument_list|)
argument_list|)
else|:
literal|null
decl_stmt|;
return|return
name|loadJwkSet
argument_list|(
name|props
argument_list|,
name|bus
argument_list|,
name|decryption
argument_list|,
name|reader
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JsonWebKeys
name|loadJwkSet
parameter_list|(
name|Properties
name|props
parameter_list|,
name|Bus
name|bus
parameter_list|,
name|JweDecryptionProvider
name|jwe
parameter_list|,
name|JwkReaderWriter
name|reader
parameter_list|)
block|{
name|String
name|keyContent
init|=
literal|null
decl_stmt|;
name|String
name|keyStoreLoc
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|KeyManagementUtils
operator|.
name|RSSEC_KEY_STORE_FILE
argument_list|)
decl_stmt|;
if|if
condition|(
name|keyStoreLoc
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|InputStream
name|is
init|=
name|ResourceUtils
operator|.
name|getResourceStream
argument_list|(
name|keyStoreLoc
argument_list|,
name|bus
argument_list|)
decl_stmt|;
name|keyContent
operator|=
name|IOUtils
operator|.
name|readStringFromStream
argument_list|(
name|is
argument_list|)
expr_stmt|;
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
else|else
block|{
name|keyContent
operator|=
name|props
operator|.
name|getProperty
argument_list|(
name|RSSEC_KEY_STORE_JWKSET
argument_list|)
expr_stmt|;
if|if
condition|(
name|keyContent
operator|==
literal|null
condition|)
block|{
name|keyContent
operator|=
name|props
operator|.
name|getProperty
argument_list|(
name|RSSEC_KEY_STORE_JWKKEY
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|jwe
operator|!=
literal|null
condition|)
block|{
name|keyContent
operator|=
name|jwe
operator|.
name|decrypt
argument_list|(
name|keyContent
argument_list|)
operator|.
name|getContentText
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|props
operator|.
name|getProperty
argument_list|(
name|RSSEC_KEY_STORE_JWKKEY
argument_list|)
operator|==
literal|null
condition|)
block|{
return|return
name|reader
operator|.
name|jsonToJwkSet
argument_list|(
name|keyContent
argument_list|)
return|;
block|}
else|else
block|{
name|JsonWebKey
name|key
init|=
name|reader
operator|.
name|jsonToJwk
argument_list|(
name|keyContent
argument_list|)
decl_stmt|;
name|JsonWebKeys
name|keys
init|=
operator|new
name|JsonWebKeys
argument_list|()
decl_stmt|;
name|keys
operator|.
name|setKeys
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|keys
return|;
block|}
block|}
specifier|public
specifier|static
name|JsonWebKey
name|loadJsonWebKey
parameter_list|(
name|Message
name|m
parameter_list|,
name|Properties
name|props
parameter_list|,
name|String
name|keyOper
parameter_list|)
block|{
return|return
name|loadJsonWebKey
argument_list|(
name|m
argument_list|,
name|props
argument_list|,
name|keyOper
argument_list|,
operator|new
name|DefaultJwkReaderWriter
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JsonWebKey
name|loadJsonWebKey
parameter_list|(
name|Message
name|m
parameter_list|,
name|Properties
name|props
parameter_list|,
name|String
name|keyOper
parameter_list|,
name|JwkReaderWriter
name|reader
parameter_list|)
block|{
name|PrivateKeyPasswordProvider
name|cb
init|=
name|loadPasswordProvider
argument_list|(
name|m
argument_list|,
name|props
argument_list|,
name|keyOper
argument_list|)
decl_stmt|;
name|JsonWebKeys
name|jwkSet
init|=
name|loadJwkSet
argument_list|(
name|m
argument_list|,
name|props
argument_list|,
name|cb
argument_list|,
name|reader
argument_list|)
decl_stmt|;
name|String
name|kid
init|=
name|getKeyId
argument_list|(
name|props
argument_list|,
name|KeyManagementUtils
operator|.
name|RSSEC_KEY_STORE_ALIAS
argument_list|,
name|keyOper
argument_list|)
decl_stmt|;
if|if
condition|(
name|kid
operator|!=
literal|null
condition|)
block|{
return|return
name|jwkSet
operator|.
name|getKey
argument_list|(
name|kid
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|keyOper
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|JsonWebKey
argument_list|>
name|keys
init|=
name|jwkSet
operator|.
name|getKeyUseMap
argument_list|()
operator|.
name|get
argument_list|(
name|keyOper
argument_list|)
decl_stmt|;
if|if
condition|(
name|keys
operator|!=
literal|null
operator|&&
name|keys
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|keys
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|JsonWebKey
argument_list|>
name|loadJsonWebKeys
parameter_list|(
name|Message
name|m
parameter_list|,
name|Properties
name|props
parameter_list|,
name|String
name|keyOper
parameter_list|)
block|{
return|return
name|loadJsonWebKeys
argument_list|(
name|m
argument_list|,
name|props
argument_list|,
name|keyOper
argument_list|,
operator|new
name|DefaultJwkReaderWriter
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|JsonWebKey
argument_list|>
name|loadJsonWebKeys
parameter_list|(
name|Message
name|m
parameter_list|,
name|Properties
name|props
parameter_list|,
name|String
name|keyOper
parameter_list|,
name|JwkReaderWriter
name|reader
parameter_list|)
block|{
name|PrivateKeyPasswordProvider
name|cb
init|=
name|loadPasswordProvider
argument_list|(
name|m
argument_list|,
name|props
argument_list|,
name|keyOper
argument_list|)
decl_stmt|;
name|JsonWebKeys
name|jwkSet
init|=
name|loadJwkSet
argument_list|(
name|m
argument_list|,
name|props
argument_list|,
name|cb
argument_list|,
name|reader
argument_list|)
decl_stmt|;
name|String
name|kid
init|=
name|getKeyId
argument_list|(
name|props
argument_list|,
name|KeyManagementUtils
operator|.
name|RSSEC_KEY_STORE_ALIAS
argument_list|,
name|keyOper
argument_list|)
decl_stmt|;
if|if
condition|(
name|kid
operator|!=
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|jwkSet
operator|.
name|getKey
argument_list|(
name|kid
argument_list|)
argument_list|)
return|;
block|}
name|String
name|kids
init|=
name|getKeyId
argument_list|(
name|props
argument_list|,
name|KeyManagementUtils
operator|.
name|RSSEC_KEY_STORE_ALIASES
argument_list|,
name|keyOper
argument_list|)
decl_stmt|;
if|if
condition|(
name|kids
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|values
init|=
name|kids
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|JsonWebKey
argument_list|>
name|keys
init|=
operator|new
name|ArrayList
argument_list|<
name|JsonWebKey
argument_list|>
argument_list|(
name|values
operator|.
name|length
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|value
range|:
name|values
control|)
block|{
name|keys
operator|.
name|add
argument_list|(
name|jwkSet
operator|.
name|getKey
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|keys
return|;
block|}
if|if
condition|(
name|keyOper
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|JsonWebKey
argument_list|>
name|keys
init|=
name|jwkSet
operator|.
name|getKeyUseMap
argument_list|()
operator|.
name|get
argument_list|(
name|keyOper
argument_list|)
decl_stmt|;
if|if
condition|(
name|keys
operator|!=
literal|null
operator|&&
name|keys
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|keys
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
specifier|static
name|String
name|getKeyId
parameter_list|(
name|Properties
name|props
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|String
name|keyOper
parameter_list|)
block|{
name|String
name|kid
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|propertyName
argument_list|)
decl_stmt|;
if|if
condition|(
name|kid
operator|==
literal|null
operator|&&
name|keyOper
operator|!=
literal|null
condition|)
block|{
name|String
name|keyIdProp
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|keyOper
operator|.
name|equals
argument_list|(
name|JsonWebKey
operator|.
name|KEY_OPER_ENCRYPT
argument_list|)
condition|)
block|{
name|keyIdProp
operator|=
name|propertyName
operator|+
literal|".jwe"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|keyOper
operator|.
name|equals
argument_list|(
name|JsonWebKey
operator|.
name|KEY_OPER_SIGN
argument_list|)
operator|||
name|keyOper
operator|.
name|equals
argument_list|(
name|JsonWebKey
operator|.
name|KEY_OPER_VERIFY
argument_list|)
condition|)
block|{
name|keyIdProp
operator|=
name|propertyName
operator|+
literal|".jws"
expr_stmt|;
block|}
if|if
condition|(
name|keyIdProp
operator|!=
literal|null
condition|)
block|{
name|kid
operator|=
name|props
operator|.
name|getProperty
argument_list|(
name|keyIdProp
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|kid
return|;
block|}
specifier|public
specifier|static
name|PrivateKeyPasswordProvider
name|loadPasswordProvider
parameter_list|(
name|Message
name|m
parameter_list|,
name|Properties
name|props
parameter_list|,
name|String
name|keyOper
parameter_list|)
block|{
name|PrivateKeyPasswordProvider
name|cb
init|=
operator|(
name|PrivateKeyPasswordProvider
operator|)
name|m
operator|.
name|getContextualProperty
argument_list|(
name|KeyManagementUtils
operator|.
name|RSSEC_KEY_PSWD_PROVIDER
argument_list|)
decl_stmt|;
if|if
condition|(
name|cb
operator|==
literal|null
operator|&&
name|keyOper
operator|!=
literal|null
condition|)
block|{
name|String
name|propName
init|=
name|keyOper
operator|.
name|equals
argument_list|(
name|JsonWebKey
operator|.
name|KEY_OPER_SIGN
argument_list|)
condition|?
name|KeyManagementUtils
operator|.
name|RSSEC_SIG_KEY_PSWD_PROVIDER
else|:
name|keyOper
operator|.
name|equals
argument_list|(
name|JsonWebKey
operator|.
name|KEY_OPER_ENCRYPT
argument_list|)
condition|?
name|KeyManagementUtils
operator|.
name|RSSEC_DECRYPT_KEY_PSWD_PROVIDER
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|propName
operator|!=
literal|null
condition|)
block|{
name|cb
operator|=
operator|(
name|PrivateKeyPasswordProvider
operator|)
name|m
operator|.
name|getContextualProperty
argument_list|(
name|propName
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|cb
return|;
block|}
specifier|public
specifier|static
name|RSAPublicKey
name|toRSAPublicKey
parameter_list|(
name|JsonWebKey
name|jwk
parameter_list|)
block|{
name|String
name|encodedModulus
init|=
operator|(
name|String
operator|)
name|jwk
operator|.
name|getProperty
argument_list|(
name|JsonWebKey
operator|.
name|RSA_MODULUS
argument_list|)
decl_stmt|;
name|String
name|encodedPublicExponent
init|=
operator|(
name|String
operator|)
name|jwk
operator|.
name|getProperty
argument_list|(
name|JsonWebKey
operator|.
name|RSA_PUBLIC_EXP
argument_list|)
decl_stmt|;
return|return
name|CryptoUtils
operator|.
name|getRSAPublicKey
argument_list|(
name|encodedModulus
argument_list|,
name|encodedPublicExponent
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|RSAPrivateKey
name|toRSAPrivateKey
parameter_list|(
name|JsonWebKey
name|jwk
parameter_list|)
block|{
name|String
name|encodedModulus
init|=
operator|(
name|String
operator|)
name|jwk
operator|.
name|getProperty
argument_list|(
name|JsonWebKey
operator|.
name|RSA_MODULUS
argument_list|)
decl_stmt|;
name|String
name|encodedPrivateExponent
init|=
operator|(
name|String
operator|)
name|jwk
operator|.
name|getProperty
argument_list|(
name|JsonWebKey
operator|.
name|RSA_PRIVATE_EXP
argument_list|)
decl_stmt|;
name|String
name|encodedPrimeP
init|=
operator|(
name|String
operator|)
name|jwk
operator|.
name|getProperty
argument_list|(
name|JsonWebKey
operator|.
name|RSA_FIRST_PRIME_FACTOR
argument_list|)
decl_stmt|;
if|if
condition|(
name|encodedPrimeP
operator|==
literal|null
condition|)
block|{
return|return
name|CryptoUtils
operator|.
name|getRSAPrivateKey
argument_list|(
name|encodedModulus
argument_list|,
name|encodedPrivateExponent
argument_list|)
return|;
block|}
else|else
block|{
name|String
name|encodedPublicExponent
init|=
operator|(
name|String
operator|)
name|jwk
operator|.
name|getProperty
argument_list|(
name|JsonWebKey
operator|.
name|RSA_PUBLIC_EXP
argument_list|)
decl_stmt|;
name|String
name|encodedPrimeQ
init|=
operator|(
name|String
operator|)
name|jwk
operator|.
name|getProperty
argument_list|(
name|JsonWebKey
operator|.
name|RSA_SECOND_PRIME_FACTOR
argument_list|)
decl_stmt|;
name|String
name|encodedPrimeExpP
init|=
operator|(
name|String
operator|)
name|jwk
operator|.
name|getProperty
argument_list|(
name|JsonWebKey
operator|.
name|RSA_FIRST_PRIME_CRT
argument_list|)
decl_stmt|;
name|String
name|encodedPrimeExpQ
init|=
operator|(
name|String
operator|)
name|jwk
operator|.
name|getProperty
argument_list|(
name|JsonWebKey
operator|.
name|RSA_SECOND_PRIME_CRT
argument_list|)
decl_stmt|;
name|String
name|encodedCrtCoefficient
init|=
operator|(
name|String
operator|)
name|jwk
operator|.
name|getProperty
argument_list|(
name|JsonWebKey
operator|.
name|RSA_FIRST_CRT_COEFFICIENT
argument_list|)
decl_stmt|;
return|return
name|CryptoUtils
operator|.
name|getRSAPrivateKey
argument_list|(
name|encodedModulus
argument_list|,
name|encodedPublicExponent
argument_list|,
name|encodedPrivateExponent
argument_list|,
name|encodedPrimeP
argument_list|,
name|encodedPrimeQ
argument_list|,
name|encodedPrimeExpP
argument_list|,
name|encodedPrimeExpQ
argument_list|,
name|encodedCrtCoefficient
argument_list|)
return|;
block|}
block|}
specifier|public
specifier|static
name|ECPublicKey
name|toECPublicKey
parameter_list|(
name|JsonWebKey
name|jwk
parameter_list|)
block|{
name|String
name|eCurve
init|=
operator|(
name|String
operator|)
name|jwk
operator|.
name|getProperty
argument_list|(
name|JsonWebKey
operator|.
name|EC_CURVE
argument_list|)
decl_stmt|;
name|String
name|encodedXCoord
init|=
operator|(
name|String
operator|)
name|jwk
operator|.
name|getProperty
argument_list|(
name|JsonWebKey
operator|.
name|EC_X_COORDINATE
argument_list|)
decl_stmt|;
name|String
name|encodedYCoord
init|=
operator|(
name|String
operator|)
name|jwk
operator|.
name|getProperty
argument_list|(
name|JsonWebKey
operator|.
name|EC_Y_COORDINATE
argument_list|)
decl_stmt|;
return|return
name|CryptoUtils
operator|.
name|getECPublicKey
argument_list|(
name|eCurve
argument_list|,
name|encodedXCoord
argument_list|,
name|encodedYCoord
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|ECPrivateKey
name|toECPrivateKey
parameter_list|(
name|JsonWebKey
name|jwk
parameter_list|)
block|{
name|String
name|eCurve
init|=
operator|(
name|String
operator|)
name|jwk
operator|.
name|getProperty
argument_list|(
name|JsonWebKey
operator|.
name|EC_CURVE
argument_list|)
decl_stmt|;
name|String
name|encodedPrivateKey
init|=
operator|(
name|String
operator|)
name|jwk
operator|.
name|getProperty
argument_list|(
name|JsonWebKey
operator|.
name|EC_PRIVATE_KEY
argument_list|)
decl_stmt|;
return|return
name|CryptoUtils
operator|.
name|getECPrivateKey
argument_list|(
name|eCurve
argument_list|,
name|encodedPrivateKey
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|SecretKey
name|toSecretKey
parameter_list|(
name|JsonWebKey
name|jwk
parameter_list|)
block|{
return|return
name|CryptoUtils
operator|.
name|createSecretKeySpec
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
argument_list|,
name|Algorithm
operator|.
name|toJavaName
argument_list|(
name|jwk
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|byte
index|[]
name|stringToBytes
parameter_list|(
name|String
name|str
parameter_list|)
block|{
return|return
name|StringUtils
operator|.
name|toBytesUTF8
argument_list|(
name|str
argument_list|)
return|;
block|}
block|}
end_class

end_unit

