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
name|ByteArrayInputStream
import|;
end_import

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
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PublicKey
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
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|DeflaterOutputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Priority
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
name|WebApplicationException
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
name|MediaType
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
name|ext
operator|.
name|WriterInterceptor
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
name|ext
operator|.
name|WriterInterceptorContext
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
name|io
operator|.
name|CachedOutputStream
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
name|oauth2
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
name|oauth2
operator|.
name|jwe
operator|.
name|JweEncryptionState
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
name|JweOutputStream
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
name|RSAOaepKeyEncryption
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
name|WrappedKeyJweEncryption
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
name|JwtHeadersWriter
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
name|JwtTokenReaderWriter
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
annotation|@
name|Priority
argument_list|(
name|Priorities
operator|.
name|JWE_WRITE_PRIORITY
argument_list|)
specifier|public
class|class
name|JweWriterInterceptor
implements|implements
name|WriterInterceptor
block|{
specifier|private
specifier|static
specifier|final
name|String
name|JSON_ENCRYPTION_OUT_PROPS
init|=
literal|"rs.security.encryption.out.properties"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|JSON_ENCRYPTION_PROPS
init|=
literal|"rs.security.encryption.properties"
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
name|JSON_WEB_ENCRYPTION_ZIP_ALGO_PROP
init|=
literal|"rs.security.jwe.zip.algorithm"
decl_stmt|;
specifier|private
name|JweEncryptionProvider
name|encryptionProvider
decl_stmt|;
specifier|private
name|boolean
name|contentTypeRequired
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|useJweOutputStream
decl_stmt|;
specifier|private
name|JwtHeadersWriter
name|writer
init|=
operator|new
name|JwtTokenReaderWriter
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|aroundWriteTo
parameter_list|(
name|WriterInterceptorContext
name|ctx
parameter_list|)
throws|throws
name|IOException
throws|,
name|WebApplicationException
block|{
name|OutputStream
name|actualOs
init|=
name|ctx
operator|.
name|getOutputStream
argument_list|()
decl_stmt|;
name|JweEncryptionProvider
name|theEncryptionProvider
init|=
name|getInitializedEncryptionProvider
argument_list|()
decl_stmt|;
name|String
name|ctString
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|contentTypeRequired
condition|)
block|{
name|MediaType
name|mt
init|=
name|ctx
operator|.
name|getMediaType
argument_list|()
decl_stmt|;
if|if
condition|(
name|mt
operator|!=
literal|null
condition|)
block|{
name|ctString
operator|=
name|JAXRSUtils
operator|.
name|mediaTypeToString
argument_list|(
name|mt
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|useJweOutputStream
condition|)
block|{
name|JweEncryptionState
name|encryption
init|=
name|theEncryptionProvider
operator|.
name|createJweEncryptionState
argument_list|(
name|ctString
argument_list|)
decl_stmt|;
try|try
block|{
name|JweCompactProducer
operator|.
name|startJweContent
argument_list|(
name|actualOs
argument_list|,
name|encryption
operator|.
name|getHeaders
argument_list|()
argument_list|,
name|writer
argument_list|,
name|encryption
operator|.
name|getContentEncryptionKey
argument_list|()
argument_list|,
name|encryption
operator|.
name|getIv
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
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
name|OutputStream
name|jweStream
init|=
operator|new
name|JweOutputStream
argument_list|(
name|actualOs
argument_list|,
name|encryption
operator|.
name|getCipher
argument_list|()
argument_list|,
name|encryption
operator|.
name|getAuthTagProducer
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|encryption
operator|.
name|isCompressionSupported
argument_list|()
condition|)
block|{
name|jweStream
operator|=
operator|new
name|DeflaterOutputStream
argument_list|(
name|jweStream
argument_list|)
expr_stmt|;
block|}
name|ctx
operator|.
name|setOutputStream
argument_list|(
name|jweStream
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|proceed
argument_list|()
expr_stmt|;
name|jweStream
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|CachedOutputStream
name|cos
init|=
operator|new
name|CachedOutputStream
argument_list|()
decl_stmt|;
name|ctx
operator|.
name|setOutputStream
argument_list|(
name|cos
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|proceed
argument_list|()
expr_stmt|;
name|String
name|jweContent
init|=
name|theEncryptionProvider
operator|.
name|encrypt
argument_list|(
name|cos
operator|.
name|getBytes
argument_list|()
argument_list|,
name|ctString
argument_list|)
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|jweContent
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
argument_list|,
name|actualOs
argument_list|)
expr_stmt|;
name|actualOs
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
name|JweEncryptionProvider
name|getInitializedEncryptionProvider
parameter_list|()
block|{
if|if
condition|(
name|encryptionProvider
operator|!=
literal|null
condition|)
block|{
return|return
name|encryptionProvider
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
name|JSON_ENCRYPTION_OUT_PROPS
argument_list|,
name|JSON_ENCRYPTION_PROPS
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
name|PublicKey
name|pk
init|=
name|CryptoUtils
operator|.
name|loadPublicKey
argument_list|(
name|m
argument_list|,
name|props
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|pk
operator|instanceof
name|RSAPublicKey
operator|)
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|()
throw|;
block|}
name|JweHeaders
name|headers
init|=
operator|new
name|JweHeaders
argument_list|(
name|props
operator|.
name|getProperty
argument_list|(
name|JSON_WEB_ENCRYPTION_KEY_ALGO_PROP
argument_list|)
argument_list|,
name|props
operator|.
name|getProperty
argument_list|(
name|JSON_WEB_ENCRYPTION_CEK_ALGO_PROP
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|compression
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|JSON_WEB_ENCRYPTION_ZIP_ALGO_PROP
argument_list|)
decl_stmt|;
if|if
condition|(
name|compression
operator|!=
literal|null
condition|)
block|{
name|headers
operator|.
name|setZipAlgorithm
argument_list|(
name|compression
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|WrappedKeyJweEncryption
argument_list|(
name|headers
argument_list|,
operator|new
name|RSAOaepKeyEncryption
argument_list|(
operator|(
name|RSAPublicKey
operator|)
name|pk
argument_list|)
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
name|setUseJweOutputStream
parameter_list|(
name|boolean
name|useJweOutputStream
parameter_list|)
block|{
name|this
operator|.
name|useJweOutputStream
operator|=
name|useJweOutputStream
expr_stmt|;
block|}
specifier|public
name|void
name|setWriter
parameter_list|(
name|JwtHeadersWriter
name|writer
parameter_list|)
block|{
name|this
operator|.
name|writer
operator|=
name|writer
expr_stmt|;
block|}
specifier|public
name|void
name|setEncryptionProvider
parameter_list|(
name|JweEncryptionProvider
name|encryptionProvider
parameter_list|)
block|{
name|this
operator|.
name|encryptionProvider
operator|=
name|encryptionProvider
expr_stmt|;
block|}
block|}
end_class

end_unit

