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
name|jaxrs
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
name|JAXRSUtils
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
name|message
operator|.
name|MessageUtils
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
name|AesGcmContentDecryptionAlgorithm
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
name|DirectKeyJweDecryption
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
name|JweDecryptionOutput
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
name|RSAOaepKeyDecryptionAlgorithm
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
name|WrappedKeyJweDecryption
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
class|class
name|AbstractJweDecryptingFilter
block|{
specifier|private
specifier|static
specifier|final
name|String
name|RSSEC_ENCRYPTION_IN_PROPS
init|=
literal|"rs.security.encryption.in.properties"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RSSEC_ENCRYPTION_PROPS
init|=
literal|"rs.security.encryption.properties"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|JSON_WEB_ENCRYPTION_KEY_ALGO_PROP
init|=
literal|"rs.security.jwe.key.encryption.algorithm"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|JSON_WEB_ENCRYPTION_CEK_ALGO_PROP
init|=
literal|"rs.security.jwe.content.encryption.algorithm"
decl_stmt|;
specifier|private
name|JweDecryptionProvider
name|decryption
decl_stmt|;
specifier|private
name|String
name|defaultMediaType
decl_stmt|;
specifier|protected
name|JweDecryptionOutput
name|decrypt
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
name|JweDecryptionProvider
name|theDecryptor
init|=
name|getInitializedDecryptionProvider
argument_list|()
decl_stmt|;
name|JweDecryptionOutput
name|out
init|=
name|theDecryptor
operator|.
name|decrypt
argument_list|(
operator|new
name|String
argument_list|(
name|IOUtils
operator|.
name|readBytesFromStream
argument_list|(
name|is
argument_list|)
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
decl_stmt|;
name|validateHeaders
argument_list|(
name|out
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|out
return|;
block|}
specifier|protected
name|void
name|validateHeaders
parameter_list|(
name|JweHeaders
name|headers
parameter_list|)
block|{
comment|// complete
block|}
specifier|public
name|void
name|setDecryptionProvider
parameter_list|(
name|JweDecryptionProvider
name|decryptor
parameter_list|)
block|{
name|this
operator|.
name|decryption
operator|=
name|decryptor
expr_stmt|;
block|}
specifier|protected
name|JweDecryptionProvider
name|getInitializedDecryptionProvider
parameter_list|()
block|{
if|if
condition|(
name|decryption
operator|!=
literal|null
condition|)
block|{
return|return
name|decryption
return|;
block|}
name|Message
name|m
init|=
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
name|String
name|propLoc
init|=
operator|(
name|String
operator|)
name|MessageUtils
operator|.
name|getContextualProperty
argument_list|(
name|m
argument_list|,
name|RSSEC_ENCRYPTION_IN_PROPS
argument_list|,
name|RSSEC_ENCRYPTION_PROPS
argument_list|)
decl_stmt|;
if|if
condition|(
name|propLoc
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
name|Bus
name|bus
init|=
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getBus
argument_list|()
decl_stmt|;
try|try
block|{
name|KeyDecryptionAlgorithm
name|keyDecryptionProvider
init|=
literal|null
decl_stmt|;
name|Properties
name|props
init|=
name|ResourceUtils
operator|.
name|loadProperties
argument_list|(
name|propLoc
argument_list|,
name|bus
argument_list|)
decl_stmt|;
name|String
name|contentEncryptionAlgo
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|JSON_WEB_ENCRYPTION_CEK_ALGO_PROP
argument_list|)
decl_stmt|;
name|SecretKey
name|ctDecryptionKey
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
name|KEY_OPER_ENCRYPT
argument_list|)
decl_stmt|;
name|String
name|keyEncryptionAlgo
init|=
name|getKeyEncryptionAlgo
argument_list|(
name|props
argument_list|,
name|jwk
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"direct"
operator|.
name|equals
argument_list|(
name|keyEncryptionAlgo
argument_list|)
condition|)
block|{
name|contentEncryptionAlgo
operator|=
name|getContentEncryptionAlgo
argument_list|(
name|props
argument_list|,
name|contentEncryptionAlgo
argument_list|)
expr_stmt|;
name|ctDecryptionKey
operator|=
name|JweUtils
operator|.
name|getContentDecryptionSecretKey
argument_list|(
name|jwk
argument_list|,
name|contentEncryptionAlgo
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|keyDecryptionProvider
operator|=
name|JweUtils
operator|.
name|getKeyDecryptionAlgorithm
argument_list|(
name|jwk
argument_list|,
name|keyEncryptionAlgo
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|keyDecryptionProvider
operator|=
operator|new
name|RSAOaepKeyDecryptionAlgorithm
argument_list|(
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
name|RSSEC_DECRYPT_KEY_PSWD_PROVIDER
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|keyDecryptionProvider
operator|==
literal|null
operator|&&
name|ctDecryptionKey
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
if|if
condition|(
name|keyDecryptionProvider
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|Algorithm
operator|.
name|isAesCbcHmac
argument_list|(
name|contentEncryptionAlgo
argument_list|)
condition|)
block|{
return|return
operator|new
name|AesCbcHmacJweDecryption
argument_list|(
name|keyDecryptionProvider
argument_list|,
name|contentEncryptionAlgo
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|WrappedKeyJweDecryption
argument_list|(
name|keyDecryptionProvider
argument_list|,
operator|new
name|AesGcmContentDecryptionAlgorithm
argument_list|(
name|contentEncryptionAlgo
argument_list|)
argument_list|)
return|;
block|}
block|}
else|else
block|{
return|return
operator|new
name|DirectKeyJweDecryption
argument_list|(
name|ctDecryptionKey
argument_list|,
operator|new
name|AesGcmContentDecryptionAlgorithm
argument_list|(
name|contentEncryptionAlgo
argument_list|)
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|SecurityException
name|ex
parameter_list|)
block|{
throw|throw
name|ex
throw|;
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
name|String
name|getKeyEncryptionAlgo
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
name|JSON_WEB_ENCRYPTION_KEY_ALGO_PROP
argument_list|)
else|:
name|algo
return|;
block|}
specifier|private
name|String
name|getContentEncryptionAlgo
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
name|JSON_WEB_ENCRYPTION_CEK_ALGO_PROP
argument_list|)
else|:
name|algo
return|;
block|}
specifier|public
name|String
name|getDefaultMediaType
parameter_list|()
block|{
return|return
name|defaultMediaType
return|;
block|}
specifier|public
name|void
name|setDefaultMediaType
parameter_list|(
name|String
name|defaultMediaType
parameter_list|)
block|{
name|this
operator|.
name|defaultMediaType
operator|=
name|defaultMediaType
expr_stmt|;
block|}
block|}
end_class

end_unit

