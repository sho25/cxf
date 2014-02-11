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
name|ConnectionFactory
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
name|MessageConsumer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|MessageProducer
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

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|TextMessage
import|;
end_import

begin_class
specifier|public
class|class
name|TestReceiver
block|{
specifier|private
name|ConnectionFactory
name|connectionFactory
decl_stmt|;
specifier|private
name|String
name|receiveQueueName
decl_stmt|;
specifier|private
name|String
name|requestMessageId
decl_stmt|;
specifier|private
name|String
name|staticReplyQueue
decl_stmt|;
specifier|private
name|Throwable
name|ex
decl_stmt|;
specifier|private
name|boolean
name|forceMessageIdAsCorrelationId
decl_stmt|;
specifier|public
name|TestReceiver
parameter_list|(
name|ConnectionFactory
name|connectionFactory
parameter_list|,
name|String
name|receiveQueueName
parameter_list|,
name|boolean
name|forceMessageIdAsCorrelationId
parameter_list|)
block|{
name|this
operator|.
name|connectionFactory
operator|=
name|connectionFactory
expr_stmt|;
name|this
operator|.
name|receiveQueueName
operator|=
name|receiveQueueName
expr_stmt|;
name|this
operator|.
name|forceMessageIdAsCorrelationId
operator|=
name|forceMessageIdAsCorrelationId
expr_stmt|;
assert|assert
name|this
operator|.
name|connectionFactory
operator|!=
literal|null
assert|;
assert|assert
name|this
operator|.
name|receiveQueueName
operator|!=
literal|null
assert|;
block|}
specifier|public
name|String
name|getRequestMessageId
parameter_list|()
block|{
return|return
name|requestMessageId
return|;
block|}
specifier|public
name|void
name|setStaticReplyQueue
parameter_list|(
name|String
name|staticReplyQueue
parameter_list|)
block|{
name|this
operator|.
name|staticReplyQueue
operator|=
name|staticReplyQueue
expr_stmt|;
block|}
specifier|private
name|void
name|drainQueue
parameter_list|()
block|{
name|ResourceCloser
name|closer
init|=
operator|new
name|ResourceCloser
argument_list|()
decl_stmt|;
try|try
block|{
name|Connection
name|connection
init|=
name|closer
operator|.
name|register
argument_list|(
name|connectionFactory
operator|.
name|createConnection
argument_list|()
argument_list|)
decl_stmt|;
name|Session
name|session
init|=
name|closer
operator|.
name|register
argument_list|(
name|connection
operator|.
name|createSession
argument_list|(
literal|false
argument_list|,
name|Session
operator|.
name|AUTO_ACKNOWLEDGE
argument_list|)
argument_list|)
decl_stmt|;
name|MessageConsumer
name|consumer
init|=
name|closer
operator|.
name|register
argument_list|(
name|session
operator|.
name|createConsumer
argument_list|(
name|session
operator|.
name|createQueue
argument_list|(
name|receiveQueueName
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|javax
operator|.
name|jms
operator|.
name|Message
name|message
init|=
literal|null
decl_stmt|;
do|do
block|{
name|message
operator|=
name|consumer
operator|.
name|receive
argument_list|(
literal|100
argument_list|)
expr_stmt|;
block|}
do|while
condition|(
name|message
operator|!=
literal|null
condition|)
do|;
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
finally|finally
block|{
name|closer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|receiveAndRespondWithMessageIdAsCorrelationId
parameter_list|()
block|{
name|ResourceCloser
name|closer
init|=
operator|new
name|ResourceCloser
argument_list|()
decl_stmt|;
try|try
block|{
name|Connection
name|connection
init|=
name|closer
operator|.
name|register
argument_list|(
name|connectionFactory
operator|.
name|createConnection
argument_list|()
argument_list|)
decl_stmt|;
name|Session
name|session
init|=
name|closer
operator|.
name|register
argument_list|(
name|connection
operator|.
name|createSession
argument_list|(
literal|false
argument_list|,
name|Session
operator|.
name|AUTO_ACKNOWLEDGE
argument_list|)
argument_list|)
decl_stmt|;
name|MessageConsumer
name|consumer
init|=
name|closer
operator|.
name|register
argument_list|(
name|session
operator|.
name|createConsumer
argument_list|(
name|session
operator|.
name|createQueue
argument_list|(
name|receiveQueueName
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|javax
operator|.
name|jms
operator|.
name|Message
name|inMessage
init|=
name|consumer
operator|.
name|receive
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
if|if
condition|(
name|inMessage
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"No message received on destination "
operator|+
name|receiveQueueName
argument_list|)
throw|;
block|}
name|requestMessageId
operator|=
name|inMessage
operator|.
name|getJMSMessageID
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Received message "
operator|+
name|requestMessageId
argument_list|)
expr_stmt|;
specifier|final
name|TextMessage
name|replyMessage
init|=
name|session
operator|.
name|createTextMessage
argument_list|(
literal|"Result"
argument_list|)
decl_stmt|;
name|String
name|correlationId
init|=
operator|(
name|forceMessageIdAsCorrelationId
operator|||
name|inMessage
operator|.
name|getJMSCorrelationID
argument_list|()
operator|==
literal|null
operator|)
condition|?
name|inMessage
operator|.
name|getJMSMessageID
argument_list|()
else|:
name|inMessage
operator|.
name|getJMSCorrelationID
argument_list|()
decl_stmt|;
name|replyMessage
operator|.
name|setJMSCorrelationID
argument_list|(
name|correlationId
argument_list|)
expr_stmt|;
name|Destination
name|replyDest
init|=
name|staticReplyQueue
operator|!=
literal|null
condition|?
name|session
operator|.
name|createQueue
argument_list|(
name|staticReplyQueue
argument_list|)
else|:
name|inMessage
operator|.
name|getJMSReplyTo
argument_list|()
decl_stmt|;
if|if
condition|(
name|replyDest
operator|!=
literal|null
condition|)
block|{
specifier|final
name|MessageProducer
name|producer
init|=
name|closer
operator|.
name|register
argument_list|(
name|session
operator|.
name|createProducer
argument_list|(
name|replyDest
argument_list|)
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Sending reply to "
operator|+
name|replyDest
argument_list|)
expr_stmt|;
name|producer
operator|.
name|send
argument_list|(
name|replyMessage
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|ex
operator|=
name|e
expr_stmt|;
block|}
finally|finally
block|{
name|closer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|runAsync
parameter_list|()
block|{
name|drainQueue
argument_list|()
expr_stmt|;
name|Executors
operator|.
name|newSingleThreadExecutor
argument_list|()
operator|.
name|execute
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|receiveAndRespondWithMessageIdAsCorrelationId
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|close
parameter_list|()
block|{
if|if
condition|(
name|ex
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error while receiving message or sending reply"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

