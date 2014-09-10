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
name|jwt
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
name|oauth2
operator|.
name|jwe
operator|.
name|AesGcmWrapKeyDecryptionAlgorithm
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
name|jwe
operator|.
name|AesWrapKeyDecryptionAlgorithm
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
name|jwe
operator|.
name|JweCryptoProperties
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
name|oauth2
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
name|oauth2
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
name|oauth2
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
name|oauth2
operator|.
name|jwe
operator|.
name|WrappedKeyDecryptionAlgorithm
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
name|oauth2
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
name|oauth2
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
name|oauth2
operator|.
name|jwt
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
name|oauth2
operator|.
name|utils
operator|.
name|crypto
operator|.
name|CryptoUtils
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
name|JweDecryptionProvider
name|decryption
decl_stmt|;
specifier|private
name|JweCryptoProperties
name|cryptoProperties
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
name|WrappedKeyDecryptionAlgorithm
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
name|CryptoUtils
operator|.
name|RSSEC_KEY_STORE_TYPE
argument_list|)
argument_list|)
condition|)
block|{
comment|//TODO: Private JWK sets can be JWE encrypted
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
argument_list|)
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
name|keyDecryptionProvider
operator|=
operator|new
name|RSAOaepKeyDecryptionAlgorithm
argument_list|(
name|jwk
operator|.
name|toRSAPrivateKey
argument_list|()
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
name|SecretKey
name|key
init|=
name|jwk
operator|.
name|toSecretKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|Algorithm
operator|.
name|isAesKeyWrap
argument_list|(
name|jwk
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
condition|)
block|{
name|keyDecryptionProvider
operator|=
operator|new
name|AesWrapKeyDecryptionAlgorithm
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Algorithm
operator|.
name|isAesGcmKeyWrap
argument_list|(
name|jwk
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
condition|)
block|{
name|keyDecryptionProvider
operator|=
operator|new
name|AesGcmWrapKeyDecryptionAlgorithm
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
comment|// etc
block|}
else|else
block|{
comment|// TODO: support elliptic curve keys
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
name|CryptoUtils
operator|.
name|loadPrivateKey
argument_list|(
name|m
argument_list|,
name|props
argument_list|,
name|CryptoUtils
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
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|()
throw|;
block|}
return|return
operator|new
name|WrappedKeyJweDecryption
argument_list|(
name|keyDecryptionProvider
argument_list|,
name|cryptoProperties
argument_list|,
literal|null
argument_list|)
return|;
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
specifier|public
name|void
name|setCryptoProperties
parameter_list|(
name|JweCryptoProperties
name|cryptoProperties
parameter_list|)
block|{
name|this
operator|.
name|cryptoProperties
operator|=
name|cryptoProperties
expr_stmt|;
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

