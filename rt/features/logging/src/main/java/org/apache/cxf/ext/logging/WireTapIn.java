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
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|SequenceInputStream
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
name|io
operator|.
name|DelegatingInputStream
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

begin_class
specifier|public
class|class
name|WireTapIn
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|private
name|long
name|threshold
init|=
operator|-
literal|1
decl_stmt|;
specifier|private
name|int
name|limit
init|=
name|AbstractLoggingInterceptor
operator|.
name|DEFAULT_LIMIT
decl_stmt|;
comment|/**      * Instantiates a new WireTapIn      * @param limit      *      * @param logMessageContent the log message content      */
specifier|public
name|WireTapIn
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
annotation|@
name|Override
specifier|public
name|void
name|handleMessage
parameter_list|(
specifier|final
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
try|try
block|{
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
name|handleInputStream
argument_list|(
name|message
argument_list|,
name|is
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Reader
name|reader
init|=
name|message
operator|.
name|getContent
argument_list|(
name|Reader
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|reader
operator|!=
literal|null
condition|)
block|{
name|handleReader
argument_list|(
name|message
argument_list|,
name|reader
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
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
specifier|private
name|void
name|handleReader
parameter_list|(
name|Message
name|message
parameter_list|,
name|Reader
name|reader
parameter_list|)
throws|throws
name|IOException
block|{
name|CachedWriter
name|writer
init|=
operator|new
name|CachedWriter
argument_list|()
decl_stmt|;
name|IOUtils
operator|.
name|copyAndCloseInput
argument_list|(
name|reader
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|Reader
operator|.
name|class
argument_list|,
name|writer
operator|.
name|getReader
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|CachedWriter
operator|.
name|class
argument_list|,
name|writer
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|handleInputStream
parameter_list|(
name|Message
name|message
parameter_list|,
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
name|CachedOutputStream
name|bos
init|=
operator|new
name|CachedOutputStream
argument_list|()
decl_stmt|;
if|if
condition|(
name|threshold
operator|>
literal|0
condition|)
block|{
name|bos
operator|.
name|setThreshold
argument_list|(
name|threshold
argument_list|)
expr_stmt|;
block|}
comment|// use the appropriate input stream and restore it later
name|InputStream
name|bis
init|=
name|is
operator|instanceof
name|DelegatingInputStream
condition|?
operator|(
operator|(
name|DelegatingInputStream
operator|)
name|is
operator|)
operator|.
name|getInputStream
argument_list|()
else|:
name|is
decl_stmt|;
comment|// only copy up to the limit since that's all we need to log
comment|// we can stream the rest
name|IOUtils
operator|.
name|copyAtLeast
argument_list|(
name|bis
argument_list|,
name|bos
argument_list|,
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
argument_list|)
expr_stmt|;
name|bos
operator|.
name|flush
argument_list|()
expr_stmt|;
name|bis
operator|=
operator|new
name|SequenceInputStream
argument_list|(
name|bos
operator|.
name|getInputStream
argument_list|()
argument_list|,
name|bis
argument_list|)
expr_stmt|;
comment|// restore the delegating input stream or the input stream
if|if
condition|(
name|is
operator|instanceof
name|DelegatingInputStream
condition|)
block|{
operator|(
operator|(
name|DelegatingInputStream
operator|)
name|is
operator|)
operator|.
name|setInputStream
argument_list|(
name|bis
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|message
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|bis
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|setContent
argument_list|(
name|CachedOutputStream
operator|.
name|class
argument_list|,
name|bos
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setLimit
parameter_list|(
name|int
name|limit
parameter_list|)
block|{
name|this
operator|.
name|limit
operator|=
name|limit
expr_stmt|;
block|}
specifier|public
name|void
name|setThreshold
parameter_list|(
name|long
name|threshold
parameter_list|)
block|{
name|this
operator|.
name|threshold
operator|=
name|threshold
expr_stmt|;
block|}
block|}
end_class

end_unit

