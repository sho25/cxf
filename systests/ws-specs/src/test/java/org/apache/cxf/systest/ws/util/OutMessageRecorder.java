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
name|systest
operator|.
name|ws
operator|.
name|util
package|;
end_package

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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CopyOnWriteArrayList
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
name|WriteOnCloseOutputStream
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
name|ws
operator|.
name|rm
operator|.
name|RMUtils
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
name|RetransmissionInterceptor
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|OutMessageRecorder
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
name|getLogger
argument_list|(
name|OutMessageRecorder
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|List
argument_list|<
name|byte
index|[]
argument_list|>
name|outbound
decl_stmt|;
specifier|public
name|OutMessageRecorder
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_STREAM
argument_list|)
expr_stmt|;
name|outbound
operator|=
operator|new
name|CopyOnWriteArrayList
argument_list|<
name|byte
index|[]
argument_list|>
argument_list|()
expr_stmt|;
name|addAfter
argument_list|(
name|RetransmissionInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
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
literal|null
operator|==
name|os
condition|)
block|{
return|return;
block|}
name|WriteOnCloseOutputStream
name|stream
init|=
name|RMUtils
operator|.
name|createCachedStream
argument_list|(
name|message
argument_list|,
name|os
argument_list|)
decl_stmt|;
name|stream
operator|.
name|registerCallback
argument_list|(
operator|new
name|RecorderCallback
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|byte
index|[]
argument_list|>
name|getOutboundMessages
parameter_list|()
block|{
return|return
name|outbound
return|;
block|}
class|class
name|RecorderCallback
implements|implements
name|CachedOutputStreamCallback
block|{
specifier|public
name|void
name|onFlush
parameter_list|(
name|CachedOutputStream
name|cos
parameter_list|)
block|{            }
specifier|public
name|void
name|onClose
parameter_list|(
name|CachedOutputStream
name|cos
parameter_list|)
block|{
comment|// bytes were already copied after flush
try|try
block|{
name|byte
name|bytes
index|[]
init|=
name|cos
operator|.
name|getBytes
argument_list|()
decl_stmt|;
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
name|LOG
operator|.
name|fine
argument_list|(
literal|"outbound: "
operator|+
name|bytes
argument_list|)
expr_stmt|;
block|}
name|outbound
operator|.
name|add
argument_list|(
name|bytes
argument_list|)
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
name|fine
argument_list|(
literal|"Can't record message from output stream class: "
operator|+
name|cos
operator|.
name|getOut
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

