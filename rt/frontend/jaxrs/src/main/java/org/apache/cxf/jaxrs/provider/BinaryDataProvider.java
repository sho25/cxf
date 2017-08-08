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
name|jaxrs
operator|.
name|provider
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedInputStream
import|;
end_import

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
name|File
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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
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
name|io
operator|.
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
import|;
end_import

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
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|DigestInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|WriteListener
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
name|HttpHeaders
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
name|core
operator|.
name|MultivaluedMap
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
name|StreamingOutput
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
name|MessageBodyReader
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
name|MessageBodyWriter
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
name|annotations
operator|.
name|UseNio
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
name|logging
operator|.
name|LogUtils
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
name|MessageDigestInputStream
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
name|continuations
operator|.
name|Continuation
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
name|continuations
operator|.
name|ContinuationProvider
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
name|FileUtils
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
name|impl
operator|.
name|HttpHeadersImpl
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
name|nio
operator|.
name|NioOutputStream
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
name|nio
operator|.
name|NioWriteEntity
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
name|nio
operator|.
name|NioWriteHandler
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
name|nio
operator|.
name|NioWriteListenerImpl
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
name|AnnotationUtils
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
name|ExceptionUtils
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
name|phase
operator|.
name|PhaseInterceptorChain
import|;
end_import

begin_class
specifier|public
class|class
name|BinaryDataProvider
parameter_list|<
name|T
parameter_list|>
extends|extends
name|AbstractConfigurableProvider
implements|implements
name|MessageBodyReader
argument_list|<
name|T
argument_list|>
implements|,
name|MessageBodyWriter
argument_list|<
name|T
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|String
name|HTTP_RANGE_PROPERTY
init|=
literal|"http.range.support"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|BinaryDataProvider
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|int
name|bufferSize
init|=
name|IOUtils
operator|.
name|DEFAULT_BUFFER_SIZE
decl_stmt|;
specifier|private
name|boolean
name|reportByteArraySize
decl_stmt|;
specifier|private
name|boolean
name|closeResponseInputStream
init|=
literal|true
decl_stmt|;
specifier|public
name|boolean
name|isReadable
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|Annotation
index|[]
name|annotations
parameter_list|,
name|MediaType
name|mt
parameter_list|)
block|{
return|return
name|byte
index|[]
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
operator|||
name|InputStream
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
operator|||
name|Reader
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
operator|||
name|File
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
operator|||
name|StreamingOutput
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
return|;
block|}
specifier|private
specifier|static
specifier|final
class|class
name|ReadingStreamingOutput
implements|implements
name|StreamingOutput
block|{
specifier|private
specifier|final
name|InputStream
name|inputStream
decl_stmt|;
specifier|private
name|ReadingStreamingOutput
parameter_list|(
name|InputStream
name|inputStream
parameter_list|)
block|{
name|this
operator|.
name|inputStream
operator|=
name|inputStream
expr_stmt|;
block|}
specifier|public
name|void
name|write
parameter_list|(
name|OutputStream
name|outputStream
parameter_list|)
throws|throws
name|IOException
block|{
name|IOUtils
operator|.
name|copy
argument_list|(
name|inputStream
argument_list|,
name|outputStream
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|T
name|readFrom
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|Annotation
index|[]
name|annotations
parameter_list|,
name|MediaType
name|type
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|,
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
if|if
condition|(
name|InputStream
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
if|if
condition|(
name|DigestInputStream
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
name|is
operator|=
operator|new
name|MessageDigestInputStream
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
return|return
name|clazz
operator|.
name|cast
argument_list|(
name|is
argument_list|)
return|;
block|}
if|if
condition|(
name|Reader
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
return|return
name|clazz
operator|.
name|cast
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|is
argument_list|,
name|getEncoding
argument_list|(
name|type
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
if|if
condition|(
name|byte
index|[]
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
return|return
name|clazz
operator|.
name|cast
argument_list|(
name|IOUtils
operator|.
name|readBytesFromStream
argument_list|(
name|is
argument_list|)
argument_list|)
return|;
block|}
if|if
condition|(
name|File
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Reading data into File objects with the help of pre-packaged"
operator|+
literal|" providers is not recommended - use InputStream or custom File reader"
argument_list|)
expr_stmt|;
comment|// create a temp file, delete on exit
name|File
name|f
init|=
name|FileUtils
operator|.
name|createTempFile
argument_list|(
literal|"File"
operator|+
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
literal|"jaxrs"
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|OutputStream
name|os
init|=
name|Files
operator|.
name|newOutputStream
argument_list|(
name|f
operator|.
name|toPath
argument_list|()
argument_list|)
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|is
argument_list|,
name|os
argument_list|,
name|bufferSize
argument_list|)
expr_stmt|;
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|clazz
operator|.
name|cast
argument_list|(
name|f
argument_list|)
return|;
block|}
if|if
condition|(
name|StreamingOutput
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
return|return
name|clazz
operator|.
name|cast
argument_list|(
operator|new
name|ReadingStreamingOutput
argument_list|(
name|is
argument_list|)
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|e
parameter_list|)
block|{
name|String
name|msg
init|=
literal|"Unsupported class: "
operator|+
name|clazz
operator|.
name|getName
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|warning
argument_list|(
name|msg
argument_list|)
expr_stmt|;
throw|throw
name|ExceptionUtils
operator|.
name|toInternalServerErrorException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
throw|;
block|}
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unrecognized class"
argument_list|)
throw|;
block|}
specifier|public
name|long
name|getSize
parameter_list|(
name|T
name|t
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|Annotation
index|[]
name|annotations
parameter_list|,
name|MediaType
name|mt
parameter_list|)
block|{
comment|// TODO: if it's a range request, then we should probably always return -1 and set
comment|// Content-Length and Content-Range in handleRangeRequest
if|if
condition|(
name|reportByteArraySize
operator|&&
name|byte
index|[]
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|t
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
return|return
operator|(
operator|(
name|byte
index|[]
operator|)
name|t
operator|)
operator|.
name|length
return|;
block|}
return|return
operator|-
literal|1
return|;
block|}
specifier|public
name|boolean
name|isWriteable
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|Annotation
index|[]
name|annotations
parameter_list|,
name|MediaType
name|mt
parameter_list|)
block|{
return|return
name|byte
index|[]
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
operator|||
name|InputStream
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
operator|||
name|File
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
operator|||
name|Reader
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
operator|||
name|StreamingOutput
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
return|;
block|}
specifier|public
name|void
name|writeTo
parameter_list|(
name|T
name|o
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|Annotation
index|[]
name|annotations
parameter_list|,
name|MediaType
name|type
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|,
name|OutputStream
name|os
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|InputStream
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|o
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|copyInputToOutput
argument_list|(
operator|(
name|InputStream
operator|)
name|o
argument_list|,
name|os
argument_list|,
name|annotations
argument_list|,
name|headers
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|File
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|o
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|copyInputToOutput
argument_list|(
operator|new
name|BufferedInputStream
argument_list|(
name|Files
operator|.
name|newInputStream
argument_list|(
operator|(
operator|(
name|File
operator|)
name|o
operator|)
operator|.
name|toPath
argument_list|()
argument_list|)
argument_list|)
argument_list|,
name|os
argument_list|,
name|annotations
argument_list|,
name|headers
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|byte
index|[]
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|o
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|copyInputToOutput
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|o
argument_list|)
argument_list|,
name|os
argument_list|,
name|annotations
argument_list|,
name|headers
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Reader
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|o
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
try|try
block|{
name|Writer
name|writer
init|=
operator|new
name|OutputStreamWriter
argument_list|(
name|os
argument_list|,
name|getEncoding
argument_list|(
name|type
argument_list|)
argument_list|)
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
operator|(
name|Reader
operator|)
name|o
argument_list|,
name|writer
argument_list|,
name|bufferSize
argument_list|)
expr_stmt|;
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
operator|(
operator|(
name|Reader
operator|)
name|o
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|StreamingOutput
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|o
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
operator|(
operator|(
name|StreamingOutput
operator|)
name|o
operator|)
operator|.
name|write
argument_list|(
name|os
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unrecognized class"
argument_list|)
throw|;
block|}
block|}
specifier|private
name|String
name|getEncoding
parameter_list|(
name|MediaType
name|mt
parameter_list|)
block|{
name|String
name|enc
init|=
name|mt
operator|.
name|getParameters
argument_list|()
operator|.
name|get
argument_list|(
literal|"charset"
argument_list|)
decl_stmt|;
return|return
name|enc
operator|==
literal|null
condition|?
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|name
argument_list|()
else|:
name|enc
return|;
block|}
specifier|protected
name|void
name|copyInputToOutput
parameter_list|(
name|InputStream
name|is
parameter_list|,
name|OutputStream
name|os
parameter_list|,
name|Annotation
index|[]
name|anns
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|outHeaders
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|isRangeSupported
argument_list|()
condition|)
block|{
name|Message
name|inMessage
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
decl_stmt|;
name|handleRangeRequest
argument_list|(
name|is
argument_list|,
name|os
argument_list|,
operator|new
name|HttpHeadersImpl
argument_list|(
name|inMessage
argument_list|)
argument_list|,
name|outHeaders
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|boolean
name|nioWrite
init|=
name|AnnotationUtils
operator|.
name|getAnnotation
argument_list|(
name|anns
argument_list|,
name|UseNio
operator|.
name|class
argument_list|)
operator|!=
literal|null
decl_stmt|;
if|if
condition|(
name|nioWrite
condition|)
block|{
name|ContinuationProvider
name|provider
init|=
name|getContinuationProvider
argument_list|()
decl_stmt|;
if|if
condition|(
name|provider
operator|!=
literal|null
condition|)
block|{
name|copyUsingNio
argument_list|(
name|is
argument_list|,
name|os
argument_list|,
name|provider
operator|.
name|getContinuation
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
if|if
condition|(
name|closeResponseInputStream
condition|)
block|{
name|IOUtils
operator|.
name|copyAndCloseInput
argument_list|(
name|is
argument_list|,
name|os
argument_list|,
name|bufferSize
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|IOUtils
operator|.
name|copy
argument_list|(
name|is
argument_list|,
name|os
argument_list|,
name|bufferSize
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|void
name|copyUsingNio
parameter_list|(
name|InputStream
name|is
parameter_list|,
name|OutputStream
name|os
parameter_list|,
name|Continuation
name|cont
parameter_list|)
block|{
name|NioWriteListenerImpl
name|listener
init|=
operator|new
name|NioWriteListenerImpl
argument_list|(
name|cont
argument_list|,
operator|new
name|NioWriteEntity
argument_list|(
name|getNioHandler
argument_list|(
name|is
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|,
operator|new
name|NioOutputStream
argument_list|(
name|os
argument_list|)
argument_list|)
decl_stmt|;
name|Message
name|m
init|=
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
name|m
operator|.
name|put
argument_list|(
name|WriteListener
operator|.
name|class
argument_list|,
name|listener
argument_list|)
expr_stmt|;
name|cont
operator|.
name|suspend
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ContinuationProvider
name|getContinuationProvider
parameter_list|()
block|{
return|return
operator|(
name|ContinuationProvider
operator|)
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
operator|.
name|get
argument_list|(
name|ContinuationProvider
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|void
name|handleRangeRequest
parameter_list|(
name|InputStream
name|is
parameter_list|,
name|OutputStream
name|os
parameter_list|,
name|HttpHeaders
name|inHeaders
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|outHeaders
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|range
init|=
name|inHeaders
operator|.
name|getRequestHeaders
argument_list|()
operator|.
name|getFirst
argument_list|(
literal|"Range"
argument_list|)
decl_stmt|;
if|if
condition|(
name|range
operator|==
literal|null
condition|)
block|{
name|IOUtils
operator|.
name|copyAndCloseInput
argument_list|(
name|is
argument_list|,
name|os
argument_list|,
name|bufferSize
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// implement
block|}
block|}
specifier|protected
name|boolean
name|isRangeSupported
parameter_list|()
block|{
name|Message
name|message
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
return|return
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|message
operator|.
name|get
argument_list|(
name|HTTP_RANGE_PROPERTY
argument_list|)
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|setReportByteArraySize
parameter_list|(
name|boolean
name|report
parameter_list|)
block|{
name|this
operator|.
name|reportByteArraySize
operator|=
name|report
expr_stmt|;
block|}
specifier|public
name|void
name|setCloseResponseInputStream
parameter_list|(
name|boolean
name|closeResponseInputStream
parameter_list|)
block|{
name|this
operator|.
name|closeResponseInputStream
operator|=
name|closeResponseInputStream
expr_stmt|;
block|}
specifier|public
name|void
name|setBufferSize
parameter_list|(
name|int
name|bufferSize
parameter_list|)
block|{
name|this
operator|.
name|bufferSize
operator|=
name|bufferSize
expr_stmt|;
block|}
specifier|protected
name|NioWriteHandler
name|getNioHandler
parameter_list|(
specifier|final
name|InputStream
name|in
parameter_list|)
block|{
return|return
operator|new
name|NioWriteHandler
argument_list|()
block|{
specifier|final
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
name|bufferSize
index|]
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|write
parameter_list|(
name|NioOutputStream
name|out
parameter_list|)
block|{
try|try
block|{
specifier|final
name|int
name|n
init|=
name|in
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
if|if
condition|(
name|n
operator|>=
literal|0
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|n
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
if|if
condition|(
name|closeResponseInputStream
condition|)
block|{
try|try
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
comment|/* do nothing */
block|}
block|}
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

