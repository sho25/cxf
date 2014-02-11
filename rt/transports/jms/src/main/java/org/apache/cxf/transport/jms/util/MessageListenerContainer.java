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
name|transport
operator|.
name|jms
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
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
name|Executors
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Destination
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|JMSException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|MessageConsumer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|MessageListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Session
import|;
end_import

begin_class
specifier|public
class|class
name|MessageListenerContainer
implements|implements
name|JMSListenerContainer
block|{
specifier|private
name|Connection
name|connection
decl_stmt|;
specifier|private
name|Destination
name|replyTo
decl_stmt|;
specifier|private
name|MessageListener
name|listenerHandler
decl_stmt|;
specifier|private
name|boolean
name|transacted
decl_stmt|;
specifier|private
name|int
name|acknowledgeMode
init|=
name|Session
operator|.
name|AUTO_ACKNOWLEDGE
decl_stmt|;
specifier|private
name|String
name|messageSelector
decl_stmt|;
specifier|private
name|boolean
name|running
decl_stmt|;
specifier|private
name|MessageConsumer
name|consumer
decl_stmt|;
specifier|private
name|Session
name|session
decl_stmt|;
specifier|private
name|ExecutorService
name|executor
decl_stmt|;
specifier|public
name|MessageListenerContainer
parameter_list|(
name|Connection
name|connection
parameter_list|,
name|Destination
name|replyTo
parameter_list|,
name|MessageListener
name|listenerHandler
parameter_list|)
block|{
name|this
operator|.
name|connection
operator|=
name|connection
expr_stmt|;
name|this
operator|.
name|replyTo
operator|=
name|replyTo
expr_stmt|;
name|this
operator|.
name|listenerHandler
operator|=
name|listenerHandler
expr_stmt|;
name|executor
operator|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
literal|20
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setTransacted
parameter_list|(
name|boolean
name|transacted
parameter_list|)
block|{
name|this
operator|.
name|transacted
operator|=
name|transacted
expr_stmt|;
block|}
specifier|public
name|void
name|setAcknowledgeMode
parameter_list|(
name|int
name|acknowledgeMode
parameter_list|)
block|{
name|this
operator|.
name|acknowledgeMode
operator|=
name|acknowledgeMode
expr_stmt|;
block|}
specifier|public
name|void
name|setMessageSelector
parameter_list|(
name|String
name|messageSelector
parameter_list|)
block|{
name|this
operator|.
name|messageSelector
operator|=
name|messageSelector
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isRunning
parameter_list|()
block|{
return|return
name|running
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|running
operator|=
literal|false
expr_stmt|;
name|ResourceCloser
operator|.
name|close
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|ResourceCloser
operator|.
name|close
argument_list|(
name|session
argument_list|)
expr_stmt|;
name|consumer
operator|=
literal|null
expr_stmt|;
name|session
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|start
parameter_list|()
block|{
try|try
block|{
name|session
operator|=
name|connection
operator|.
name|createSession
argument_list|(
name|transacted
argument_list|,
name|acknowledgeMode
argument_list|)
expr_stmt|;
name|consumer
operator|=
name|session
operator|.
name|createConsumer
argument_list|(
name|replyTo
argument_list|,
name|messageSelector
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|setMessageListener
argument_list|(
name|listenerHandler
argument_list|)
expr_stmt|;
name|running
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
throw|throw
name|JMSUtil
operator|.
name|convertJmsException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
name|stop
argument_list|()
expr_stmt|;
name|ResourceCloser
operator|.
name|close
argument_list|(
name|connection
argument_list|)
expr_stmt|;
block|}
class|class
name|DispachingListener
implements|implements
name|MessageListener
block|{
annotation|@
name|Override
specifier|public
name|void
name|onMessage
parameter_list|(
specifier|final
name|Message
name|message
parameter_list|)
block|{
name|executor
operator|.
name|execute
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|listenerHandler
operator|.
name|onMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

