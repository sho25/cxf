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
name|feature
operator|.
name|transform
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
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamWriter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Templates
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
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
name|interceptor
operator|.
name|AbstractOutDatabindingInterceptor
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
name|interceptor
operator|.
name|Fault
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
name|interceptor
operator|.
name|StaxOutInterceptor
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
name|io
operator|.
name|CachedOutputStreamCallback
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
name|CachedWriter
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
name|phase
operator|.
name|Phase
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
name|staxutils
operator|.
name|DelegatingXMLStreamWriter
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
name|staxutils
operator|.
name|StaxUtils
import|;
end_import

begin_comment
comment|/** Class provides XSLT transformation of outgoing message.  * Actually it breaks streaming (can be fixed in further versions when XSLT engine supports XML stream)  */
end_comment

begin_class
specifier|public
class|class
name|XSLTOutInterceptor
extends|extends
name|AbstractXSLTInterceptor
block|{
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
name|XSLTOutInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|XSLTOutInterceptor
parameter_list|(
name|String
name|xsltPath
parameter_list|)
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_STREAM
argument_list|,
name|StaxOutInterceptor
operator|.
name|class
argument_list|,
literal|null
argument_list|,
name|xsltPath
argument_list|)
expr_stmt|;
block|}
specifier|public
name|XSLTOutInterceptor
parameter_list|(
name|String
name|phase
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|before
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|after
parameter_list|,
name|String
name|xsltPath
parameter_list|)
block|{
name|super
argument_list|(
name|phase
argument_list|,
name|before
argument_list|,
name|after
argument_list|,
name|xsltPath
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|checkContextProperty
argument_list|(
name|message
argument_list|)
condition|)
block|{
return|return;
block|}
comment|// 1. Try to get and transform XMLStreamWriter message content
name|XMLStreamWriter
name|xWriter
init|=
name|message
operator|.
name|getContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|xWriter
operator|!=
literal|null
condition|)
block|{
name|transformXWriter
argument_list|(
name|message
argument_list|,
name|xWriter
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// 2. Try to get and transform OutputStream message content
name|OutputStream
name|out
init|=
name|message
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|out
operator|!=
literal|null
condition|)
block|{
name|transformOS
argument_list|(
name|message
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// 3. Try to get and transform Writer message content (actually used for JMS TextMessage)
name|Writer
name|writer
init|=
name|message
operator|.
name|getContent
argument_list|(
name|Writer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|writer
operator|!=
literal|null
condition|)
block|{
name|transformWriter
argument_list|(
name|message
argument_list|,
name|writer
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|protected
name|void
name|transformXWriter
parameter_list|(
name|Message
name|message
parameter_list|,
name|XMLStreamWriter
name|xWriter
parameter_list|)
block|{
name|CachedWriter
name|writer
init|=
operator|new
name|CachedWriter
argument_list|()
decl_stmt|;
name|XMLStreamWriter
name|delegate
init|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|writer
argument_list|)
decl_stmt|;
name|XSLTStreamWriter
name|wrapper
init|=
operator|new
name|XSLTStreamWriter
argument_list|(
name|getXSLTTemplate
argument_list|()
argument_list|,
name|writer
argument_list|,
name|delegate
argument_list|,
name|xWriter
argument_list|)
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|,
name|wrapper
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|AbstractOutDatabindingInterceptor
operator|.
name|DISABLE_OUTPUTSTREAM_OPTIMIZATION
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|transformOS
parameter_list|(
name|Message
name|message
parameter_list|,
name|OutputStream
name|out
parameter_list|)
block|{
name|CachedOutputStream
name|wrapper
init|=
operator|new
name|CachedOutputStream
argument_list|()
decl_stmt|;
name|CachedOutputStreamCallback
name|callback
init|=
operator|new
name|XSLTCachedOutputStreamCallback
argument_list|(
name|getXSLTTemplate
argument_list|()
argument_list|,
name|out
argument_list|)
decl_stmt|;
name|wrapper
operator|.
name|registerCallback
argument_list|(
name|callback
argument_list|)
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
name|wrapper
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|transformWriter
parameter_list|(
name|Message
name|message
parameter_list|,
name|Writer
name|writer
parameter_list|)
block|{
name|XSLTCachedWriter
name|wrapper
init|=
operator|new
name|XSLTCachedWriter
argument_list|(
name|getXSLTTemplate
argument_list|()
argument_list|,
name|writer
argument_list|)
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|Writer
operator|.
name|class
argument_list|,
name|wrapper
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
class|class
name|XSLTStreamWriter
extends|extends
name|DelegatingXMLStreamWriter
block|{
specifier|private
specifier|final
name|Templates
name|xsltTemplate
decl_stmt|;
specifier|private
specifier|final
name|CachedWriter
name|cachedWriter
decl_stmt|;
specifier|private
specifier|final
name|XMLStreamWriter
name|origXWriter
decl_stmt|;
specifier|public
name|XSLTStreamWriter
parameter_list|(
name|Templates
name|xsltTemplate
parameter_list|,
name|CachedWriter
name|cachedWriter
parameter_list|,
name|XMLStreamWriter
name|delegateXWriter
parameter_list|,
name|XMLStreamWriter
name|origXWriter
parameter_list|)
block|{
name|super
argument_list|(
name|delegateXWriter
argument_list|)
expr_stmt|;
name|this
operator|.
name|xsltTemplate
operator|=
name|xsltTemplate
expr_stmt|;
name|this
operator|.
name|cachedWriter
operator|=
name|cachedWriter
expr_stmt|;
name|this
operator|.
name|origXWriter
operator|=
name|origXWriter
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
block|{
name|Reader
name|transformedReader
init|=
literal|null
decl_stmt|;
try|try
block|{
name|super
operator|.
name|flush
argument_list|()
expr_stmt|;
name|transformedReader
operator|=
name|XSLTUtils
operator|.
name|transform
argument_list|(
name|xsltTemplate
argument_list|,
name|cachedWriter
operator|.
name|getReader
argument_list|()
argument_list|)
expr_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
operator|new
name|StreamSource
argument_list|(
name|transformedReader
argument_list|)
argument_list|,
name|origXWriter
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
literal|"STAX_COPY"
argument_list|,
name|LOG
argument_list|,
name|e
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
literal|"GET_CACHED_INPUT_STREAM"
argument_list|,
name|LOG
argument_list|,
name|e
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
finally|finally
block|{
try|try
block|{
if|if
condition|(
name|transformedReader
operator|!=
literal|null
condition|)
block|{
name|transformedReader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|cachedWriter
operator|.
name|close
argument_list|()
expr_stmt|;
name|StaxUtils
operator|.
name|close
argument_list|(
name|origXWriter
argument_list|)
expr_stmt|;
name|super
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Cannot close stream after transformation: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|public
specifier|static
class|class
name|XSLTCachedOutputStreamCallback
implements|implements
name|CachedOutputStreamCallback
block|{
specifier|private
specifier|final
name|Templates
name|xsltTemplate
decl_stmt|;
specifier|private
specifier|final
name|OutputStream
name|origStream
decl_stmt|;
specifier|public
name|XSLTCachedOutputStreamCallback
parameter_list|(
name|Templates
name|xsltTemplate
parameter_list|,
name|OutputStream
name|origStream
parameter_list|)
block|{
name|this
operator|.
name|xsltTemplate
operator|=
name|xsltTemplate
expr_stmt|;
name|this
operator|.
name|origStream
operator|=
name|origStream
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onFlush
parameter_list|(
name|CachedOutputStream
name|wrapper
parameter_list|)
block|{                     }
annotation|@
name|Override
specifier|public
name|void
name|onClose
parameter_list|(
name|CachedOutputStream
name|wrapper
parameter_list|)
block|{
name|InputStream
name|transformedStream
init|=
literal|null
decl_stmt|;
try|try
block|{
name|transformedStream
operator|=
name|XSLTUtils
operator|.
name|transform
argument_list|(
name|xsltTemplate
argument_list|,
name|wrapper
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
name|IOUtils
operator|.
name|copyAndCloseInput
argument_list|(
name|transformedStream
argument_list|,
name|origStream
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
literal|"STREAM_COPY"
argument_list|,
name|LOG
argument_list|,
name|e
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
finally|finally
block|{
try|try
block|{
name|origStream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Cannot close stream after transformation: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|public
specifier|static
class|class
name|XSLTCachedWriter
extends|extends
name|CachedWriter
block|{
specifier|private
specifier|final
name|Templates
name|xsltTemplate
decl_stmt|;
specifier|private
specifier|final
name|Writer
name|origWriter
decl_stmt|;
specifier|public
name|XSLTCachedWriter
parameter_list|(
name|Templates
name|xsltTemplate
parameter_list|,
name|Writer
name|origWriter
parameter_list|)
block|{
name|this
operator|.
name|xsltTemplate
operator|=
name|xsltTemplate
expr_stmt|;
name|this
operator|.
name|origWriter
operator|=
name|origWriter
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doClose
parameter_list|()
block|{
name|Reader
name|transformedReader
init|=
literal|null
decl_stmt|;
try|try
block|{
name|transformedReader
operator|=
name|XSLTUtils
operator|.
name|transform
argument_list|(
name|xsltTemplate
argument_list|,
name|getReader
argument_list|()
argument_list|)
expr_stmt|;
name|IOUtils
operator|.
name|copyAndCloseInput
argument_list|(
name|transformedReader
argument_list|,
name|origWriter
argument_list|,
name|IOUtils
operator|.
name|DEFAULT_BUFFER_SIZE
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
literal|"READER_COPY"
argument_list|,
name|LOG
argument_list|,
name|e
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
finally|finally
block|{
try|try
block|{
name|origWriter
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Cannot close stream after transformation: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

