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
name|ext
operator|.
name|logging
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FilterWriter
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
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|injection
operator|.
name|NoJSR250Annotations
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
name|ext
operator|.
name|logging
operator|.
name|event
operator|.
name|DefaultLogEventMapper
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
name|ext
operator|.
name|logging
operator|.
name|event
operator|.
name|LogEvent
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
name|ext
operator|.
name|logging
operator|.
name|event
operator|.
name|LogEventSender
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
name|ext
operator|.
name|logging
operator|.
name|event
operator|.
name|PrintWriterEventSender
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
name|ext
operator|.
name|logging
operator|.
name|slf4j
operator|.
name|Slf4jVerboseEventSender
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
name|CacheAndWriteOutputStream
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

begin_comment
comment|/**  *  */
end_comment

begin_class
annotation|@
name|NoJSR250Annotations
specifier|public
class|class
name|LoggingOutInterceptor
extends|extends
name|AbstractLoggingInterceptor
block|{
specifier|public
name|LoggingOutInterceptor
parameter_list|()
block|{
name|this
argument_list|(
operator|new
name|Slf4jVerboseEventSender
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|LoggingOutInterceptor
parameter_list|(
name|PrintWriter
name|writer
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|PrintWriterEventSender
argument_list|(
name|writer
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|LoggingOutInterceptor
parameter_list|(
name|LogEventSender
name|sender
parameter_list|)
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_STREAM
argument_list|,
name|sender
argument_list|)
expr_stmt|;
name|addBefore
argument_list|(
name|StaxOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
if|if
condition|(
name|isLoggingDisabledNow
argument_list|(
name|message
argument_list|)
condition|)
block|{
return|return;
block|}
name|createExchangeId
argument_list|(
name|message
argument_list|)
expr_stmt|;
specifier|final
name|OutputStream
name|os
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
name|os
operator|!=
literal|null
condition|)
block|{
name|LoggingCallback
name|callback
init|=
operator|new
name|LoggingCallback
argument_list|(
name|sender
argument_list|,
name|message
argument_list|,
name|os
argument_list|,
name|limit
argument_list|)
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
name|createCachingOut
argument_list|(
name|message
argument_list|,
name|os
argument_list|,
name|callback
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
specifier|final
name|Writer
name|iowriter
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
name|iowriter
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setContent
argument_list|(
name|Writer
operator|.
name|class
argument_list|,
operator|new
name|LogEventSendingWriter
argument_list|(
name|sender
argument_list|,
name|message
argument_list|,
name|iowriter
argument_list|,
name|limit
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|OutputStream
name|createCachingOut
parameter_list|(
name|Message
name|message
parameter_list|,
specifier|final
name|OutputStream
name|os
parameter_list|,
name|CachedOutputStreamCallback
name|callback
parameter_list|)
block|{
specifier|final
name|CacheAndWriteOutputStream
name|newOut
init|=
operator|new
name|CacheAndWriteOutputStream
argument_list|(
name|os
argument_list|)
decl_stmt|;
if|if
condition|(
name|threshold
operator|>
literal|0
condition|)
block|{
name|newOut
operator|.
name|setThreshold
argument_list|(
name|threshold
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|limit
operator|>
literal|0
condition|)
block|{
comment|// make the limit for the cache greater than the limit for the truncated payload in the log event,
comment|// this is necessary for finding out that the payload was truncated
comment|//(see boolean isTruncated = cos.size()> limit&& limit != -1;)  in method copyPayload
name|newOut
operator|.
name|setCacheLimit
argument_list|(
name|getCacheLimit
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|newOut
operator|.
name|registerCallback
argument_list|(
name|callback
argument_list|)
expr_stmt|;
return|return
name|newOut
return|;
block|}
specifier|private
name|int
name|getCacheLimit
parameter_list|()
block|{
if|if
condition|(
name|limit
operator|==
name|Integer
operator|.
name|MAX_VALUE
condition|)
block|{
return|return
name|limit
return|;
block|}
return|return
name|limit
operator|+
literal|1
return|;
block|}
specifier|private
class|class
name|LogEventSendingWriter
extends|extends
name|FilterWriter
block|{
name|StringWriter
name|out2
decl_stmt|;
name|int
name|count
decl_stmt|;
name|Message
name|message
decl_stmt|;
specifier|final
name|int
name|lim
decl_stmt|;
specifier|private
name|LogEventSender
name|sender
decl_stmt|;
name|LogEventSendingWriter
parameter_list|(
name|LogEventSender
name|sender
parameter_list|,
name|Message
name|message
parameter_list|,
name|Writer
name|writer
parameter_list|,
name|int
name|limit
parameter_list|)
block|{
name|super
argument_list|(
name|writer
argument_list|)
expr_stmt|;
name|this
operator|.
name|sender
operator|=
name|sender
expr_stmt|;
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
if|if
condition|(
operator|!
operator|(
name|writer
operator|instanceof
name|StringWriter
operator|)
condition|)
block|{
name|out2
operator|=
operator|new
name|StringWriter
argument_list|()
expr_stmt|;
block|}
name|lim
operator|=
name|limit
operator|==
operator|-
literal|1
condition|?
name|Integer
operator|.
name|MAX_VALUE
else|:
name|limit
expr_stmt|;
block|}
specifier|public
name|void
name|write
parameter_list|(
name|int
name|c
parameter_list|)
throws|throws
name|IOException
block|{
name|super
operator|.
name|write
argument_list|(
name|c
argument_list|)
expr_stmt|;
if|if
condition|(
name|out2
operator|!=
literal|null
operator|&&
name|count
operator|<
name|lim
condition|)
block|{
name|out2
operator|.
name|write
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
name|count
operator|++
expr_stmt|;
block|}
specifier|public
name|void
name|write
parameter_list|(
name|char
index|[]
name|cbuf
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{
name|super
operator|.
name|write
argument_list|(
name|cbuf
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
expr_stmt|;
if|if
condition|(
name|out2
operator|!=
literal|null
operator|&&
name|count
operator|<
name|lim
condition|)
block|{
name|out2
operator|.
name|write
argument_list|(
name|cbuf
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
name|count
operator|+=
name|len
expr_stmt|;
block|}
specifier|public
name|void
name|write
parameter_list|(
name|String
name|str
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{
name|super
operator|.
name|write
argument_list|(
name|str
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
expr_stmt|;
if|if
condition|(
name|out2
operator|!=
literal|null
operator|&&
name|count
operator|<
name|lim
condition|)
block|{
name|out2
operator|.
name|write
argument_list|(
name|str
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
name|count
operator|+=
name|len
expr_stmt|;
block|}
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|LogEvent
name|event
init|=
operator|new
name|DefaultLogEventMapper
argument_list|()
operator|.
name|map
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|StringWriter
name|w2
init|=
name|out2
decl_stmt|;
if|if
condition|(
name|w2
operator|==
literal|null
condition|)
block|{
name|w2
operator|=
operator|(
name|StringWriter
operator|)
name|out
expr_stmt|;
block|}
name|String
name|payload
init|=
name|shouldLogContent
argument_list|(
name|event
argument_list|)
condition|?
name|getPayload
argument_list|(
name|event
argument_list|,
name|w2
argument_list|)
else|:
name|CONTENT_SUPPRESSED
decl_stmt|;
name|event
operator|.
name|setPayload
argument_list|(
name|payload
argument_list|)
expr_stmt|;
name|sender
operator|.
name|send
argument_list|(
name|event
argument_list|)
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|Writer
operator|.
name|class
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|super
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|private
name|String
name|getPayload
parameter_list|(
specifier|final
name|LogEvent
name|event
parameter_list|,
name|StringWriter
name|w2
parameter_list|)
block|{
name|StringBuilder
name|payload
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|writePayload
argument_list|(
name|payload
argument_list|,
name|w2
argument_list|,
name|event
argument_list|)
expr_stmt|;
return|return
name|payload
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|void
name|writePayload
parameter_list|(
name|StringBuilder
name|builder
parameter_list|,
name|StringWriter
name|stringWriter
parameter_list|,
name|LogEvent
name|event
parameter_list|)
block|{
name|StringBuffer
name|buffer
init|=
name|stringWriter
operator|.
name|getBuffer
argument_list|()
decl_stmt|;
if|if
condition|(
name|buffer
operator|.
name|length
argument_list|()
operator|>
name|lim
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
name|buffer
operator|.
name|subSequence
argument_list|(
literal|0
argument_list|,
name|lim
argument_list|)
argument_list|)
expr_stmt|;
name|event
operator|.
name|setTruncated
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|.
name|append
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
name|event
operator|.
name|setTruncated
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
class|class
name|LoggingCallback
implements|implements
name|CachedOutputStreamCallback
block|{
specifier|private
specifier|final
name|Message
name|message
decl_stmt|;
specifier|private
specifier|final
name|OutputStream
name|origStream
decl_stmt|;
specifier|private
specifier|final
name|int
name|lim
decl_stmt|;
specifier|private
name|LogEventSender
name|sender
decl_stmt|;
specifier|public
name|LoggingCallback
parameter_list|(
specifier|final
name|LogEventSender
name|sender
parameter_list|,
specifier|final
name|Message
name|msg
parameter_list|,
specifier|final
name|OutputStream
name|os
parameter_list|,
name|int
name|limit
parameter_list|)
block|{
name|this
operator|.
name|sender
operator|=
name|sender
expr_stmt|;
name|this
operator|.
name|message
operator|=
name|msg
expr_stmt|;
name|this
operator|.
name|origStream
operator|=
name|os
expr_stmt|;
name|this
operator|.
name|lim
operator|=
name|limit
operator|==
operator|-
literal|1
condition|?
name|Integer
operator|.
name|MAX_VALUE
else|:
name|limit
expr_stmt|;
block|}
specifier|public
name|void
name|onFlush
parameter_list|(
name|CachedOutputStream
name|cos
parameter_list|)
block|{          }
specifier|public
name|void
name|onClose
parameter_list|(
name|CachedOutputStream
name|cos
parameter_list|)
block|{
specifier|final
name|LogEvent
name|event
init|=
operator|new
name|DefaultLogEventMapper
argument_list|()
operator|.
name|map
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|shouldLogContent
argument_list|(
name|event
argument_list|)
condition|)
block|{
name|copyPayload
argument_list|(
name|cos
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|event
operator|.
name|setPayload
argument_list|(
name|CONTENT_SUPPRESSED
argument_list|)
expr_stmt|;
block|}
name|sender
operator|.
name|send
argument_list|(
name|event
argument_list|)
expr_stmt|;
try|try
block|{
comment|// empty out the cache
name|cos
operator|.
name|lockOutputStream
argument_list|()
expr_stmt|;
name|cos
operator|.
name|resetOut
argument_list|(
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
name|message
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
name|origStream
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|copyPayload
parameter_list|(
name|CachedOutputStream
name|cos
parameter_list|,
specifier|final
name|LogEvent
name|event
parameter_list|)
block|{
try|try
block|{
name|String
name|encoding
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|ENCODING
argument_list|)
decl_stmt|;
name|StringBuilder
name|payload
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|writePayload
argument_list|(
name|payload
argument_list|,
name|cos
argument_list|,
name|encoding
argument_list|,
name|event
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|event
operator|.
name|setPayload
argument_list|(
name|payload
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|boolean
name|isTruncated
init|=
name|cos
operator|.
name|size
argument_list|()
operator|>
name|limit
operator|&&
name|limit
operator|!=
operator|-
literal|1
decl_stmt|;
name|event
operator|.
name|setTruncated
argument_list|(
name|isTruncated
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
block|}
specifier|protected
name|void
name|writePayload
parameter_list|(
name|StringBuilder
name|builder
parameter_list|,
name|CachedOutputStream
name|cos
parameter_list|,
name|String
name|encoding
parameter_list|,
name|String
name|contentType
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|encoding
argument_list|)
condition|)
block|{
name|cos
operator|.
name|writeCacheTo
argument_list|(
name|builder
argument_list|,
name|lim
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cos
operator|.
name|writeCacheTo
argument_list|(
name|builder
argument_list|,
name|encoding
argument_list|,
name|lim
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

