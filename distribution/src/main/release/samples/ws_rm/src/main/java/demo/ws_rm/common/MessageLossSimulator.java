begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|ws_rm
operator|.
name|common
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
name|ListIterator
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
name|Interceptor
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
name|InterceptorChain
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
name|MessageSenderInterceptor
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
name|AbstractWrappedOutputStream
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|AddressingProperties
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
name|ws
operator|.
name|rm
operator|.
name|RMContextUtils
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|MessageLossSimulator
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
name|Logger
operator|.
name|getLogger
argument_list|(
name|MessageLossSimulator
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
specifier|private
name|int
name|appMessageCount
decl_stmt|;
specifier|public
name|MessageLossSimulator
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PREPARE_SEND
argument_list|)
expr_stmt|;
name|addBefore
argument_list|(
name|MessageSenderInterceptor
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
name|AddressingProperties
name|maps
init|=
name|RMContextUtils
operator|.
name|retrieveMAPs
argument_list|(
name|message
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|//Fixed the build error of ws_rm, now there is no ensureExposedVersion anymore
comment|//RMContextUtils.ensureExposedVersion(maps);
name|String
name|action
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|maps
operator|!=
literal|null
operator|&&
literal|null
operator|!=
name|maps
operator|.
name|getAction
argument_list|()
condition|)
block|{
name|action
operator|=
name|maps
operator|.
name|getAction
argument_list|()
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|RMContextUtils
operator|.
name|isRMProtocolMessage
argument_list|(
name|action
argument_list|)
condition|)
block|{
return|return;
block|}
name|appMessageCount
operator|++
expr_stmt|;
comment|// do not discard odd-numbered messages
if|if
condition|(
literal|0
operator|!=
operator|(
name|appMessageCount
operator|%
literal|2
operator|)
condition|)
block|{
return|return;
block|}
comment|// discard even-numbered message
name|InterceptorChain
name|chain
init|=
name|message
operator|.
name|getInterceptorChain
argument_list|()
decl_stmt|;
name|ListIterator
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|it
init|=
name|chain
operator|.
name|getIterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|PhaseInterceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
name|pi
init|=
operator|(
name|PhaseInterceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|MessageSenderInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|pi
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
name|chain
operator|.
name|remove
argument_list|(
name|pi
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Removed MessageSenderInterceptor from interceptor chain."
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
name|message
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
operator|new
name|WrappedOutputStream
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
argument_list|(
name|Phase
operator|.
name|PREPARE_SEND_ENDING
argument_list|)
block|{
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
try|try
block|{
name|message
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
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
block|}
argument_list|)
expr_stmt|;
block|}
specifier|private
class|class
name|WrappedOutputStream
extends|extends
name|AbstractWrappedOutputStream
block|{
specifier|private
name|Message
name|outMessage
decl_stmt|;
name|WrappedOutputStream
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|this
operator|.
name|outMessage
operator|=
name|m
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|onFirstWrite
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|Long
name|nr
init|=
name|RMContextUtils
operator|.
name|retrieveRMProperties
argument_list|(
name|outMessage
argument_list|,
literal|true
argument_list|)
operator|.
name|getSequence
argument_list|()
operator|.
name|getMessageNumber
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Losing message "
operator|+
name|nr
argument_list|)
expr_stmt|;
block|}
name|wrappedStream
operator|=
operator|new
name|DummyOutputStream
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
class|class
name|DummyOutputStream
extends|extends
name|OutputStream
block|{
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|int
name|b
parameter_list|)
throws|throws
name|IOException
block|{
comment|// TODO Auto-generated method stub
block|}
block|}
block|}
end_class

end_unit

