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
name|PrintWriter
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
name|Collection
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
name|Slf4jNoMdcEventSender
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
name|PhaseInterceptor
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
name|LoggingInInterceptor
extends|extends
name|AbstractLoggingInterceptor
block|{
class|class
name|LoggingInFaultInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
name|LoggingInFaultInterceptor
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
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{         }
annotation|@
name|Override
specifier|public
name|void
name|handleFault
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|LoggingInInterceptor
operator|.
name|this
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|LoggingInInterceptor
parameter_list|()
block|{
name|this
argument_list|(
operator|new
name|Slf4jNoMdcEventSender
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|LoggingInInterceptor
parameter_list|(
name|LogEventSender
name|sender
parameter_list|)
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_INVOKE
argument_list|,
name|sender
argument_list|)
expr_stmt|;
block|}
specifier|public
name|LoggingInInterceptor
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
name|Collection
argument_list|<
name|PhaseInterceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|getAdditionalInterceptors
parameter_list|()
block|{
name|Collection
argument_list|<
name|PhaseInterceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|ret
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|ret
operator|.
name|add
argument_list|(
operator|new
name|WireTapIn
argument_list|(
name|limit
argument_list|,
name|threshold
argument_list|)
argument_list|)
expr_stmt|;
name|ret
operator|.
name|add
argument_list|(
operator|new
name|LoggingInFaultInterceptor
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|ret
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
name|addContent
argument_list|(
name|message
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
name|AbstractLoggingInterceptor
operator|.
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
block|}
specifier|private
name|void
name|addContent
parameter_list|(
name|Message
name|message
parameter_list|,
specifier|final
name|LogEvent
name|event
parameter_list|)
block|{
try|try
block|{
name|CachedOutputStream
name|cos
init|=
name|message
operator|.
name|getContent
argument_list|(
name|CachedOutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|cos
operator|!=
literal|null
condition|)
block|{
name|handleOutputStream
argument_list|(
name|event
argument_list|,
name|message
argument_list|,
name|cos
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|CachedWriter
name|writer
init|=
name|message
operator|.
name|getContent
argument_list|(
name|CachedWriter
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
name|handleWriter
argument_list|(
name|event
argument_list|,
name|writer
argument_list|)
expr_stmt|;
block|}
block|}
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
specifier|private
name|void
name|handleOutputStream
parameter_list|(
specifier|final
name|LogEvent
name|event
parameter_list|,
name|Message
name|message
parameter_list|,
name|CachedOutputStream
name|cos
parameter_list|)
throws|throws
name|IOException
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
name|encoding
operator|=
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
name|StringBuilder
name|payload
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|cos
operator|.
name|writeCacheTo
argument_list|(
name|payload
argument_list|,
name|encoding
argument_list|,
name|limit
argument_list|)
expr_stmt|;
name|cos
operator|.
name|close
argument_list|()
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
name|event
operator|.
name|setFullContentFile
argument_list|(
name|cos
operator|.
name|getTempFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|handleWriter
parameter_list|(
specifier|final
name|LogEvent
name|event
parameter_list|,
name|CachedWriter
name|writer
parameter_list|)
throws|throws
name|IOException
block|{
name|boolean
name|isTruncated
init|=
name|writer
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
name|StringBuilder
name|payload
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|writer
operator|.
name|writeCacheTo
argument_list|(
name|payload
argument_list|,
name|limit
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
name|event
operator|.
name|setTruncated
argument_list|(
name|isTruncated
argument_list|)
expr_stmt|;
name|event
operator|.
name|setFullContentFile
argument_list|(
name|writer
operator|.
name|getTempFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

