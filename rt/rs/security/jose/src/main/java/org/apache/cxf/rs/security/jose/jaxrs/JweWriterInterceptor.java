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
name|rs
operator|.
name|security
operator|.
name|jose
operator|.
name|JoseConstants
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
name|JweEncryptionInput
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
name|jose
operator|.
name|jwe
operator|.
name|JweUtils
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
if|if
condition|(
name|ctx
operator|.
name|getEntity
argument_list|()
operator|==
literal|null
condition|)
block|{
name|ctx
operator|.
name|proceed
argument_list|()
expr_stmt|;
return|return;
block|}
name|OutputStream
name|actualOs
init|=
name|ctx
operator|.
name|getOutputStream
argument_list|()
decl_stmt|;
name|JweHeaders
name|jweHeaders
init|=
operator|new
name|JweHeaders
argument_list|()
decl_stmt|;
name|JweEncryptionProvider
name|theEncryptionProvider
init|=
name|getInitializedEncryptionProvider
argument_list|(
name|jweHeaders
argument_list|)
decl_stmt|;
name|String
name|ctString
init|=
literal|null
decl_stmt|;
name|MediaType
name|contentMediaType
init|=
name|ctx
operator|.
name|getMediaType
argument_list|()
decl_stmt|;
if|if
condition|(
name|contentTypeRequired
operator|&&
name|contentMediaType
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
literal|"application"
operator|.
name|equals
argument_list|(
name|contentMediaType
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
name|ctString
operator|=
name|contentMediaType
operator|.
name|getSubtype
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|ctString
operator|=
name|JAXRSUtils
operator|.
name|mediaTypeToString
argument_list|(
name|contentMediaType
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|ctString
operator|!=
literal|null
condition|)
block|{
name|jweHeaders
operator|.
name|setContentType
argument_list|(
name|ctString
argument_list|)
expr_stmt|;
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
operator|new
name|JweEncryptionInput
argument_list|(
name|jweHeaders
argument_list|)
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
name|wrappedStream
init|=
literal|null
decl_stmt|;
name|JweOutputStream
name|jweOutputStream
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
name|wrappedStream
operator|=
name|jweOutputStream
expr_stmt|;
if|if
condition|(
name|encryption
operator|.
name|isCompressionSupported
argument_list|()
condition|)
block|{
name|wrappedStream
operator|=
operator|new
name|DeflaterOutputStream
argument_list|(
name|jweOutputStream
argument_list|)
expr_stmt|;
block|}
name|ctx
operator|.
name|setOutputStream
argument_list|(
name|wrappedStream
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|proceed
argument_list|()
expr_stmt|;
name|setJoseMediaType
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
name|jweOutputStream
operator|.
name|finalFlush
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
name|jweHeaders
argument_list|)
decl_stmt|;
name|setJoseMediaType
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|StringUtils
operator|.
name|toBytesUTF8
argument_list|(
name|jweContent
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
specifier|private
name|void
name|setJoseMediaType
parameter_list|(
name|WriterInterceptorContext
name|ctx
parameter_list|)
block|{
name|MediaType
name|joseMediaType
init|=
name|JAXRSUtils
operator|.
name|toMediaType
argument_list|(
name|JoseConstants
operator|.
name|MEDIA_TYPE_JOSE
argument_list|)
decl_stmt|;
name|ctx
operator|.
name|setMediaType
argument_list|(
name|joseMediaType
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|JweEncryptionProvider
name|getInitializedEncryptionProvider
parameter_list|(
name|JweHeaders
name|headers
parameter_list|)
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
return|return
name|JweUtils
operator|.
name|loadEncryptionProvider
argument_list|(
name|headers
argument_list|,
literal|true
argument_list|)
return|;
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

