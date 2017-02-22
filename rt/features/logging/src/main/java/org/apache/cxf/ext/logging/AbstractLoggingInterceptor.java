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
name|util
operator|.
name|UUID
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
name|PropertyUtils
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
name|PrettyLoggingFilter
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
name|message
operator|.
name|Exchange
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

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractLoggingInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_LIMIT
init|=
literal|48
operator|*
literal|1024
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CONTENT_SUPPRESSED
init|=
literal|"--- Content suppressed ---"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|LIVE_LOGGING_PROP
init|=
literal|"org.apache.cxf.logging.enable"
decl_stmt|;
specifier|protected
name|int
name|limit
init|=
name|DEFAULT_LIMIT
decl_stmt|;
specifier|protected
name|long
name|threshold
init|=
operator|-
literal|1
decl_stmt|;
specifier|protected
name|boolean
name|logBinary
decl_stmt|;
specifier|protected
name|boolean
name|logMultipart
init|=
literal|true
decl_stmt|;
specifier|protected
name|LogEventSender
name|sender
decl_stmt|;
specifier|public
name|AbstractLoggingInterceptor
parameter_list|(
name|String
name|phase
parameter_list|,
name|LogEventSender
name|sender
parameter_list|)
block|{
name|super
argument_list|(
name|phase
argument_list|)
expr_stmt|;
name|this
operator|.
name|sender
operator|=
name|sender
expr_stmt|;
block|}
specifier|protected
specifier|static
name|boolean
name|isLoggingDisabledNow
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|Object
name|liveLoggingProp
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|LIVE_LOGGING_PROP
argument_list|)
decl_stmt|;
return|return
name|liveLoggingProp
operator|!=
literal|null
operator|&&
name|PropertyUtils
operator|.
name|isFalse
argument_list|(
name|liveLoggingProp
argument_list|)
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
name|this
operator|.
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
name|this
operator|.
name|limit
return|;
block|}
specifier|public
name|void
name|setInMemThreshold
parameter_list|(
name|long
name|t
parameter_list|)
block|{
name|this
operator|.
name|threshold
operator|=
name|t
expr_stmt|;
block|}
specifier|public
name|long
name|getInMemThreshold
parameter_list|()
block|{
return|return
name|threshold
return|;
block|}
specifier|public
name|void
name|setPrettyLogging
parameter_list|(
name|boolean
name|prettyLogging
parameter_list|)
block|{
if|if
condition|(
name|sender
operator|instanceof
name|PrettyLoggingFilter
condition|)
block|{
operator|(
operator|(
name|PrettyLoggingFilter
operator|)
name|this
operator|.
name|sender
operator|)
operator|.
name|setPrettyLogging
argument_list|(
name|prettyLogging
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|boolean
name|shouldLogContent
parameter_list|(
name|LogEvent
name|event
parameter_list|)
block|{
return|return
name|event
operator|.
name|isBinaryContent
argument_list|()
operator|&&
name|logBinary
operator|||
name|event
operator|.
name|isMultipartContent
argument_list|()
operator|&&
name|logMultipart
operator|||
operator|!
name|event
operator|.
name|isBinaryContent
argument_list|()
operator|&&
operator|!
name|event
operator|.
name|isMultipartContent
argument_list|()
return|;
block|}
specifier|public
name|void
name|setLogBinary
parameter_list|(
name|boolean
name|logBinary
parameter_list|)
block|{
name|this
operator|.
name|logBinary
operator|=
name|logBinary
expr_stmt|;
block|}
specifier|public
name|void
name|setLogMultipart
parameter_list|(
name|boolean
name|logMultipart
parameter_list|)
block|{
name|this
operator|.
name|logMultipart
operator|=
name|logMultipart
expr_stmt|;
block|}
specifier|public
name|void
name|createExchangeId
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|String
name|exchangeId
init|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|get
argument_list|(
name|LogEvent
operator|.
name|KEY_EXCHANGE_ID
argument_list|)
decl_stmt|;
if|if
condition|(
name|exchangeId
operator|==
literal|null
condition|)
block|{
name|exchangeId
operator|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
name|exchange
operator|.
name|put
argument_list|(
name|LogEvent
operator|.
name|KEY_EXCHANGE_ID
argument_list|,
name|exchangeId
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

