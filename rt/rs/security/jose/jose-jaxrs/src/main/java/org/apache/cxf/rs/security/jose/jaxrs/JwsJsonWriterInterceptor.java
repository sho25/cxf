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
name|OutputStream
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
name|List
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
name|Base64UrlOutputStream
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
name|Base64UrlUtility
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
name|provider
operator|.
name|json
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
name|common
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
name|common
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
name|JwsJsonOutputStream
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
name|JwsSignature
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

begin_class
annotation|@
name|Priority
argument_list|(
name|Priorities
operator|.
name|JWS_WRITE_PRIORITY
argument_list|)
specifier|public
class|class
name|JwsJsonWriterInterceptor
extends|extends
name|AbstractJwsJsonWriterProvider
implements|implements
name|WriterInterceptor
block|{
specifier|private
name|JsonMapObjectReaderWriter
name|writer
init|=
operator|new
name|JsonMapObjectReaderWriter
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|contentTypeRequired
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|useJwsOutputStream
decl_stmt|;
specifier|private
name|boolean
name|encodePayload
init|=
literal|true
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
name|List
argument_list|<
name|JwsSignatureProvider
argument_list|>
name|sigProviders
init|=
name|getInitializedSigProviders
argument_list|()
decl_stmt|;
name|OutputStream
name|actualOs
init|=
name|ctx
operator|.
name|getOutputStream
argument_list|()
decl_stmt|;
if|if
condition|(
name|useJwsOutputStream
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|protectedHeaders
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|sigProviders
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|JwsSignature
argument_list|>
name|signatures
init|=
operator|new
name|ArrayList
argument_list|<
name|JwsSignature
argument_list|>
argument_list|(
name|sigProviders
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|JwsSignatureProvider
name|signer
range|:
name|sigProviders
control|)
block|{
name|JwsHeaders
name|protectedHeader
init|=
name|prepareProtectedHeader
argument_list|(
name|ctx
argument_list|,
name|signer
argument_list|)
decl_stmt|;
name|String
name|encoded
init|=
name|Base64UrlUtility
operator|.
name|encode
argument_list|(
name|writer
operator|.
name|toJson
argument_list|(
name|protectedHeader
argument_list|)
argument_list|)
decl_stmt|;
name|protectedHeaders
operator|.
name|add
argument_list|(
name|encoded
argument_list|)
expr_stmt|;
name|JwsSignature
name|signature
init|=
name|signer
operator|.
name|createJwsSignature
argument_list|(
name|protectedHeader
argument_list|)
decl_stmt|;
name|byte
index|[]
name|start
init|=
name|StringUtils
operator|.
name|toBytesUTF8
argument_list|(
name|encoded
operator|+
literal|"."
argument_list|)
decl_stmt|;
name|signature
operator|.
name|update
argument_list|(
name|start
argument_list|,
literal|0
argument_list|,
name|start
operator|.
name|length
argument_list|)
expr_stmt|;
name|signatures
operator|.
name|add
argument_list|(
name|signature
argument_list|)
expr_stmt|;
block|}
name|ctx
operator|.
name|setMediaType
argument_list|(
name|JAXRSUtils
operator|.
name|toMediaType
argument_list|(
name|JoseConstants
operator|.
name|MEDIA_TYPE_JOSE_JSON
argument_list|)
argument_list|)
expr_stmt|;
name|actualOs
operator|.
name|write
argument_list|(
name|StringUtils
operator|.
name|toBytesUTF8
argument_list|(
literal|"{\"payload\":\""
argument_list|)
argument_list|)
expr_stmt|;
name|JwsJsonOutputStream
name|jwsStream
init|=
operator|new
name|JwsJsonOutputStream
argument_list|(
name|actualOs
argument_list|,
name|protectedHeaders
argument_list|,
name|signatures
argument_list|)
decl_stmt|;
name|Base64UrlOutputStream
name|base64Stream
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|encodePayload
condition|)
block|{
name|base64Stream
operator|=
operator|new
name|Base64UrlOutputStream
argument_list|(
name|jwsStream
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|setOutputStream
argument_list|(
name|base64Stream
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ctx
operator|.
name|setOutputStream
argument_list|(
name|jwsStream
argument_list|)
expr_stmt|;
block|}
name|ctx
operator|.
name|proceed
argument_list|()
expr_stmt|;
if|if
condition|(
name|encodePayload
condition|)
block|{
name|base64Stream
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
name|jwsStream
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
name|JwsJsonProducer
name|p
init|=
operator|new
name|JwsJsonProducer
argument_list|(
operator|new
name|String
argument_list|(
name|cos
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|JwsSignatureProvider
name|signer
range|:
name|sigProviders
control|)
block|{
name|JwsHeaders
name|protectedHeader
init|=
name|prepareProtectedHeader
argument_list|(
name|ctx
argument_list|,
name|signer
argument_list|)
decl_stmt|;
name|p
operator|.
name|signWith
argument_list|(
name|signer
argument_list|,
name|protectedHeader
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
name|ctx
operator|.
name|setMediaType
argument_list|(
name|JAXRSUtils
operator|.
name|toMediaType
argument_list|(
name|JoseConstants
operator|.
name|MEDIA_TYPE_JOSE_JSON
argument_list|)
argument_list|)
expr_stmt|;
name|writeJws
argument_list|(
name|p
argument_list|,
name|actualOs
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|JwsHeaders
name|prepareProtectedHeader
parameter_list|(
name|WriterInterceptorContext
name|ctx
parameter_list|,
name|JwsSignatureProvider
name|signer
parameter_list|)
block|{
name|JwsHeaders
name|headers
init|=
operator|new
name|JwsHeaders
argument_list|()
decl_stmt|;
name|headers
operator|.
name|setSignatureAlgorithm
argument_list|(
name|signer
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
name|setContentTypeIfNeeded
argument_list|(
name|headers
argument_list|,
name|ctx
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|encodePayload
condition|)
block|{
name|headers
operator|.
name|setPayloadEncodingStatus
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
return|return
name|headers
return|;
block|}
specifier|public
name|void
name|setContentTypeRequired
parameter_list|(
name|boolean
name|contentTypeRequired
parameter_list|)
block|{
name|this
operator|.
name|contentTypeRequired
operator|=
name|contentTypeRequired
expr_stmt|;
block|}
specifier|public
name|void
name|setUseJwsJsonOutputStream
parameter_list|(
name|boolean
name|useJwsJsonOutputStream
parameter_list|)
block|{
name|this
operator|.
name|useJwsOutputStream
operator|=
name|useJwsJsonOutputStream
expr_stmt|;
block|}
specifier|private
name|void
name|setContentTypeIfNeeded
parameter_list|(
name|JoseHeaders
name|headers
parameter_list|,
name|WriterInterceptorContext
name|ctx
parameter_list|)
block|{
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
operator|&&
operator|!
name|JAXRSUtils
operator|.
name|mediaTypeToString
argument_list|(
name|mt
argument_list|)
operator|.
name|equals
argument_list|(
name|JoseConstants
operator|.
name|MEDIA_TYPE_JOSE_JSON
argument_list|)
condition|)
block|{
if|if
condition|(
literal|"application"
operator|.
name|equals
argument_list|(
name|mt
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
name|headers
operator|.
name|setContentType
argument_list|(
name|mt
operator|.
name|getSubtype
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|headers
operator|.
name|setContentType
argument_list|(
name|JAXRSUtils
operator|.
name|mediaTypeToString
argument_list|(
name|mt
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|public
name|void
name|setEncodePayload
parameter_list|(
name|boolean
name|encodePayload
parameter_list|)
block|{
name|this
operator|.
name|encodePayload
operator|=
name|encodePayload
expr_stmt|;
block|}
block|}
end_class

end_unit

