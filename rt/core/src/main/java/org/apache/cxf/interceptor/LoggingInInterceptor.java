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
name|PrintWriter
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
name|AbstractPhaseInterceptor
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
comment|/**  * A simple logging handler which outputs the bytes of the message to the  * Logger.  */
end_comment

begin_class
specifier|public
class|class
name|LoggingInInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
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
name|LoggingInInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|int
name|limit
init|=
literal|100
operator|*
literal|1024
decl_stmt|;
specifier|private
name|PrintWriter
name|writer
decl_stmt|;
specifier|public
name|LoggingInInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|RECEIVE
argument_list|)
expr_stmt|;
block|}
specifier|public
name|LoggingInInterceptor
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
block|}
specifier|public
name|LoggingInInterceptor
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
name|LoggingInInterceptor
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
name|setPrintWriter
parameter_list|(
name|PrintWriter
name|w
parameter_list|)
block|{
name|writer
operator|=
name|w
expr_stmt|;
block|}
specifier|public
name|PrintWriter
name|getPrintWriter
parameter_list|()
block|{
return|return
name|writer
return|;
block|}
specifier|public
name|void
name|setLimit
parameter_list|(
name|int
name|lim
parameter_list|)
block|{
name|limit
operator|=
name|lim
expr_stmt|;
block|}
specifier|public
name|int
name|getLimit
parameter_list|()
block|{
return|return
name|limit
return|;
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
name|writer
operator|!=
literal|null
operator|||
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|INFO
argument_list|)
condition|)
block|{
name|logging
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|logging
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
specifier|final
name|LoggingMessage
name|buffer
init|=
operator|new
name|LoggingMessage
argument_list|(
literal|"Inbound Message\n----------------------------"
argument_list|)
decl_stmt|;
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
name|InputStream
name|is
init|=
name|message
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|CachedOutputStream
name|bos
init|=
operator|new
name|CachedOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|IOUtils
operator|.
name|copy
argument_list|(
name|is
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|bos
operator|.
name|flush
argument_list|()
expr_stmt|;
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|bos
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|bos
operator|.
name|getTempFile
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|//large thing on disk...
name|buffer
operator|.
name|getMessage
argument_list|()
operator|.
name|append
argument_list|(
literal|"\nMessage (saved to tmp file):\n"
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
name|bos
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
literal|"\nMessage:\n"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|bos
operator|.
name|size
argument_list|()
operator|>
name|limit
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
name|limit
operator|+
literal|" bytes)\n"
argument_list|)
expr_stmt|;
block|}
name|bos
operator|.
name|writeCacheTo
argument_list|(
name|buffer
operator|.
name|getPayload
argument_list|()
argument_list|,
name|limit
argument_list|)
expr_stmt|;
name|bos
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
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|writer
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|println
argument_list|(
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|INFO
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

