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
name|interceptor
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
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
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
comment|/**  *   */
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
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getLogger
argument_list|(
name|LoggingOutInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|LOG_SETUP
init|=
name|LoggingOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".log-setup"
decl_stmt|;
specifier|public
name|LoggingOutInterceptor
parameter_list|(
name|String
name|phase
parameter_list|)
block|{
name|super
argument_list|(
name|phase
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
name|LoggingOutInterceptor
parameter_list|()
block|{
name|this
argument_list|(
name|Phase
operator|.
name|PRE_STREAM
argument_list|)
expr_stmt|;
block|}
specifier|public
name|LoggingOutInterceptor
parameter_list|(
name|int
name|lim
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|limit
operator|=
name|lim
expr_stmt|;
block|}
specifier|public
name|LoggingOutInterceptor
parameter_list|(
name|PrintWriter
name|w
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|this
operator|.
name|writer
operator|=
name|w
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
name|os
operator|==
literal|null
operator|&&
name|iowriter
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Logger
name|logger
init|=
name|getMessageLogger
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|logger
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|INFO
argument_list|)
operator|||
name|writer
operator|!=
literal|null
condition|)
block|{
comment|// Write the output while caching it for the log message
name|boolean
name|hasLogged
init|=
name|message
operator|.
name|containsKey
argument_list|(
name|LOG_SETUP
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|hasLogged
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|LOG_SETUP
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
if|if
condition|(
name|os
operator|!=
literal|null
condition|)
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
name|newOut
operator|.
name|setCacheLimit
argument_list|(
name|limit
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
name|newOut
argument_list|)
expr_stmt|;
name|newOut
operator|.
name|registerCallback
argument_list|(
operator|new
name|LoggingCallback
argument_list|(
name|logger
argument_list|,
name|message
argument_list|,
name|os
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
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
name|LogWriter
argument_list|(
name|logger
argument_list|,
name|message
argument_list|,
name|iowriter
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|LoggingMessage
name|setupBuffer
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|String
name|id
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|LoggingMessage
operator|.
name|ID_KEY
argument_list|)
decl_stmt|;
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
name|id
operator|=
name|LoggingMessage
operator|.
name|nextId
argument_list|()
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|LoggingMessage
operator|.
name|ID_KEY
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
specifier|final
name|LoggingMessage
name|buffer
init|=
operator|new
name|LoggingMessage
argument_list|(
literal|"Outbound Message\n---------------------------"
argument_list|,
name|id
argument_list|)
decl_stmt|;
name|Integer
name|responseCode
init|=
operator|(
name|Integer
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|RESPONSE_CODE
argument_list|)
decl_stmt|;
if|if
condition|(
name|responseCode
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|getResponseCode
argument_list|()
operator|.
name|append
argument_list|(
name|responseCode
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|encoding
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|getEncoding
argument_list|()
operator|.
name|append
argument_list|(
name|encoding
argument_list|)
expr_stmt|;
block|}
name|String
name|httpMethod
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
name|HTTP_REQUEST_METHOD
argument_list|)
decl_stmt|;
if|if
condition|(
name|httpMethod
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|getHttpMethod
argument_list|()
operator|.
name|append
argument_list|(
name|httpMethod
argument_list|)
expr_stmt|;
block|}
name|String
name|address
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
name|ENDPOINT_ADDRESS
argument_list|)
decl_stmt|;
if|if
condition|(
name|address
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|getAddress
argument_list|()
operator|.
name|append
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|String
name|uri
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
name|REQUEST_URI
argument_list|)
decl_stmt|;
if|if
condition|(
name|uri
operator|!=
literal|null
operator|&&
operator|!
name|address
operator|.
name|startsWith
argument_list|(
name|uri
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|address
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
operator|&&
operator|!
name|uri
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|buffer
operator|.
name|getAddress
argument_list|()
operator|.
name|append
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|getAddress
argument_list|()
operator|.
name|append
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|ct
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
name|CONTENT_TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|ct
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|getContentType
argument_list|()
operator|.
name|append
argument_list|(
name|ct
argument_list|)
expr_stmt|;
block|}
name|Object
name|headers
init|=
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
decl_stmt|;
if|if
condition|(
name|headers
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|getHeader
argument_list|()
operator|.
name|append
argument_list|(
name|headers
argument_list|)
expr_stmt|;
block|}
return|return
name|buffer
return|;
block|}
specifier|private
class|class
name|LogWriter
extends|extends
name|FilterWriter
block|{
name|StringWriter
name|out2
decl_stmt|;
name|int
name|count
decl_stmt|;
name|Logger
name|logger
decl_stmt|;
comment|//NOPMD
name|Message
name|message
decl_stmt|;
specifier|final
name|int
name|lim
decl_stmt|;
name|LogWriter
parameter_list|(
name|Logger
name|logger
parameter_list|,
name|Message
name|message
parameter_list|,
name|Writer
name|writer
parameter_list|)
block|{
name|super
argument_list|(
name|writer
argument_list|)
expr_stmt|;
name|this
operator|.
name|logger
operator|=
name|logger
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
name|LoggingMessage
name|buffer
init|=
name|setupBuffer
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|count
operator|>=
name|lim
condition|)
block|{
name|buffer
operator|.
name|getMessage
argument_list|()
operator|.
name|append
argument_list|(
literal|"(message truncated to "
operator|+
name|lim
operator|+
literal|" bytes)\n"
argument_list|)
expr_stmt|;
block|}
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
name|ct
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
name|CONTENT_TYPE
argument_list|)
decl_stmt|;
try|try
block|{
name|writePayload
argument_list|(
name|buffer
operator|.
name|getPayload
argument_list|()
argument_list|,
name|w2
argument_list|,
name|ct
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//ignore
block|}
name|log
argument_list|(
name|logger
argument_list|,
name|formatLoggingMessage
argument_list|(
name|buffer
argument_list|)
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
block|}
specifier|protected
name|String
name|formatLoggingMessage
parameter_list|(
name|LoggingMessage
name|buffer
parameter_list|)
block|{
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
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
name|Logger
name|logger
decl_stmt|;
comment|//NOPMD
specifier|private
specifier|final
name|int
name|lim
decl_stmt|;
name|LoggingCallback
parameter_list|(
specifier|final
name|Logger
name|logger
parameter_list|,
specifier|final
name|Message
name|msg
parameter_list|,
specifier|final
name|OutputStream
name|os
parameter_list|)
block|{
name|this
operator|.
name|logger
operator|=
name|logger
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
block|{                        }
specifier|public
name|void
name|onClose
parameter_list|(
name|CachedOutputStream
name|cos
parameter_list|)
block|{
name|LoggingMessage
name|buffer
init|=
name|setupBuffer
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|String
name|ct
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
name|CONTENT_TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|isShowBinaryContent
argument_list|()
operator|&&
name|isBinaryContent
argument_list|(
name|ct
argument_list|)
condition|)
block|{
name|buffer
operator|.
name|getMessage
argument_list|()
operator|.
name|append
argument_list|(
name|BINARY_CONTENT_MESSAGE
argument_list|)
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
name|log
argument_list|(
name|logger
argument_list|,
name|formatLoggingMessage
argument_list|(
name|buffer
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
operator|!
name|isShowMultipartContent
argument_list|()
operator|&&
name|isMultipartContent
argument_list|(
name|ct
argument_list|)
condition|)
block|{
name|buffer
operator|.
name|getMessage
argument_list|()
operator|.
name|append
argument_list|(
name|MULTIPART_CONTENT_MESSAGE
argument_list|)
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
name|log
argument_list|(
name|logger
argument_list|,
name|formatLoggingMessage
argument_list|(
name|buffer
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|cos
operator|.
name|getTempFile
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|//buffer.append("Outbound Message:\n");
if|if
condition|(
name|cos
operator|.
name|size
argument_list|()
operator|>=
name|lim
condition|)
block|{
name|buffer
operator|.
name|getMessage
argument_list|()
operator|.
name|append
argument_list|(
literal|"(message truncated to "
operator|+
name|lim
operator|+
literal|" bytes)\n"
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|buffer
operator|.
name|getMessage
argument_list|()
operator|.
name|append
argument_list|(
literal|"Outbound Message (saved to tmp file):\n"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|getMessage
argument_list|()
operator|.
name|append
argument_list|(
literal|"Filename: "
operator|+
name|cos
operator|.
name|getTempFile
argument_list|()
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
if|if
condition|(
name|cos
operator|.
name|size
argument_list|()
operator|>=
name|lim
condition|)
block|{
name|buffer
operator|.
name|getMessage
argument_list|()
operator|.
name|append
argument_list|(
literal|"(message truncated to "
operator|+
name|lim
operator|+
literal|" bytes)\n"
argument_list|)
expr_stmt|;
block|}
block|}
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
name|writePayload
argument_list|(
name|buffer
operator|.
name|getPayload
argument_list|()
argument_list|,
name|cos
argument_list|,
name|encoding
argument_list|,
name|ct
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//ignore
block|}
name|log
argument_list|(
name|logger
argument_list|,
name|formatLoggingMessage
argument_list|(
name|buffer
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
comment|//empty out the cache
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
comment|//ignore
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
block|}
annotation|@
name|Override
specifier|protected
name|Logger
name|getLogger
parameter_list|()
block|{
return|return
name|LOG
return|;
block|}
block|}
end_class

end_unit

