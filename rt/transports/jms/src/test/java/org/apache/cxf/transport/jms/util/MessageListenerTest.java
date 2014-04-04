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
name|Enumeration
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
name|MessageProducer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Queue
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|QueueBrowser
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

begin_import
import|import
name|javax
operator|.
name|transaction
operator|.
name|TransactionManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|transaction
operator|.
name|xa
operator|.
name|XAException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|ActiveMQConnectionFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|ActiveMQXAConnectionFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|RedeliveryPolicy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|pool
operator|.
name|XaPooledConnectionFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|aries
operator|.
name|transaction
operator|.
name|internal
operator|.
name|AriesTransactionManagerImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|MessageListenerTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|FAIL
init|=
literal|"fail"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|FAILFIRST
init|=
literal|"failfirst"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|OK
init|=
literal|"ok"
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testWithJTA
parameter_list|()
throws|throws
name|JMSException
throws|,
name|XAException
throws|,
name|InterruptedException
block|{
name|TransactionManager
name|transactionManager
init|=
operator|new
name|AriesTransactionManagerImpl
argument_list|()
decl_stmt|;
name|Connection
name|connection
init|=
name|createXAConnection
argument_list|(
literal|"brokerJTA"
argument_list|,
name|transactionManager
argument_list|)
decl_stmt|;
name|Queue
name|dest
init|=
name|createQueue
argument_list|(
name|connection
argument_list|,
literal|"test"
argument_list|)
decl_stmt|;
name|MessageListener
name|listenerHandler
init|=
operator|new
name|TestMessageListener
argument_list|()
decl_stmt|;
name|PollingMessageListenerContainer
name|container
init|=
operator|new
name|PollingMessageListenerContainer
argument_list|(
name|connection
argument_list|,
name|dest
argument_list|,
name|listenerHandler
argument_list|)
decl_stmt|;
name|container
operator|.
name|setTransacted
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|container
operator|.
name|setAcknowledgeMode
argument_list|(
name|Session
operator|.
name|SESSION_TRANSACTED
argument_list|)
expr_stmt|;
name|container
operator|.
name|setTransactionManager
argument_list|(
name|transactionManager
argument_list|)
expr_stmt|;
name|container
operator|.
name|start
argument_list|()
expr_stmt|;
name|testTransactionalBehaviour
argument_list|(
name|connection
argument_list|,
name|dest
argument_list|)
expr_stmt|;
name|container
operator|.
name|stop
argument_list|()
expr_stmt|;
name|connection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoTransaction
parameter_list|()
throws|throws
name|JMSException
throws|,
name|XAException
throws|,
name|InterruptedException
block|{
name|Connection
name|connection
init|=
name|createConnection
argument_list|(
literal|"brokerNoTransaction"
argument_list|)
decl_stmt|;
name|Queue
name|dest
init|=
name|createQueue
argument_list|(
name|connection
argument_list|,
literal|"test"
argument_list|)
decl_stmt|;
name|MessageListener
name|listenerHandler
init|=
operator|new
name|TestMessageListener
argument_list|()
decl_stmt|;
name|PollingMessageListenerContainer
name|container
init|=
operator|new
name|PollingMessageListenerContainer
argument_list|(
name|connection
argument_list|,
name|dest
argument_list|,
name|listenerHandler
argument_list|)
decl_stmt|;
name|container
operator|.
name|setTransacted
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|container
operator|.
name|setAcknowledgeMode
argument_list|(
name|Session
operator|.
name|AUTO_ACKNOWLEDGE
argument_list|)
expr_stmt|;
name|container
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertNumMessagesInQueue
argument_list|(
literal|"At the start the queue should be empty"
argument_list|,
name|connection
argument_list|,
name|dest
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
name|connection
argument_list|,
name|dest
argument_list|,
name|OK
argument_list|)
expr_stmt|;
name|assertNumMessagesInQueue
argument_list|(
literal|"This message should be committed"
argument_list|,
name|connection
argument_list|,
name|dest
argument_list|,
literal|0
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
name|connection
argument_list|,
name|dest
argument_list|,
name|FAIL
argument_list|)
expr_stmt|;
name|assertNumMessagesInQueue
argument_list|(
literal|"Even when an exception occurs the message should be committed"
argument_list|,
name|connection
argument_list|,
name|dest
argument_list|,
literal|0
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
name|container
operator|.
name|stop
argument_list|()
expr_stmt|;
name|connection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLocalTransaction
parameter_list|()
throws|throws
name|JMSException
throws|,
name|XAException
throws|,
name|InterruptedException
block|{
name|Connection
name|connection
init|=
name|createConnection
argument_list|(
literal|"brokerLocalTransaction"
argument_list|)
decl_stmt|;
name|Queue
name|dest
init|=
name|createQueue
argument_list|(
name|connection
argument_list|,
literal|"test"
argument_list|)
decl_stmt|;
name|MessageListener
name|listenerHandler
init|=
operator|new
name|TestMessageListener
argument_list|()
decl_stmt|;
name|MessageListenerContainer
name|container
init|=
operator|new
name|MessageListenerContainer
argument_list|(
name|connection
argument_list|,
name|dest
argument_list|,
name|listenerHandler
argument_list|)
decl_stmt|;
name|container
operator|.
name|setTransacted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|container
operator|.
name|setAcknowledgeMode
argument_list|(
name|Session
operator|.
name|SESSION_TRANSACTED
argument_list|)
expr_stmt|;
name|container
operator|.
name|start
argument_list|()
expr_stmt|;
name|testTransactionalBehaviour
argument_list|(
name|connection
argument_list|,
name|dest
argument_list|)
expr_stmt|;
name|container
operator|.
name|stop
argument_list|()
expr_stmt|;
name|connection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|testTransactionalBehaviour
parameter_list|(
name|Connection
name|connection
parameter_list|,
name|Queue
name|dest
parameter_list|)
throws|throws
name|JMSException
throws|,
name|InterruptedException
block|{
name|Queue
name|dlq
init|=
name|createQueue
argument_list|(
name|connection
argument_list|,
literal|"ActiveMQ.DLQ"
argument_list|)
decl_stmt|;
name|assertNumMessagesInQueue
argument_list|(
literal|"At the start the queue should be empty"
argument_list|,
name|connection
argument_list|,
name|dest
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertNumMessagesInQueue
argument_list|(
literal|"At the start the DLQ should be empty"
argument_list|,
name|connection
argument_list|,
name|dlq
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
name|connection
argument_list|,
name|dest
argument_list|,
name|OK
argument_list|)
expr_stmt|;
name|assertNumMessagesInQueue
argument_list|(
literal|"This message should be committed"
argument_list|,
name|connection
argument_list|,
name|dest
argument_list|,
literal|0
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
name|connection
argument_list|,
name|dest
argument_list|,
name|FAILFIRST
argument_list|)
expr_stmt|;
name|assertNumMessagesInQueue
argument_list|(
literal|"Should succeed on second try"
argument_list|,
name|connection
argument_list|,
name|dest
argument_list|,
literal|0
argument_list|,
literal|2000
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
name|connection
argument_list|,
name|dest
argument_list|,
name|FAIL
argument_list|)
expr_stmt|;
name|assertNumMessagesInQueue
argument_list|(
literal|"Should be rolled back"
argument_list|,
name|connection
argument_list|,
name|dlq
argument_list|,
literal|1
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Connection
name|createConnection
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|JMSException
block|{
name|ActiveMQConnectionFactory
name|cf
init|=
operator|new
name|ActiveMQConnectionFactory
argument_list|(
literal|"vm://"
operator|+
name|name
operator|+
literal|"?broker.persistent=false"
argument_list|)
decl_stmt|;
name|cf
operator|.
name|setRedeliveryPolicy
argument_list|(
name|redeliveryPolicy
argument_list|()
argument_list|)
expr_stmt|;
name|Connection
name|connection
init|=
name|cf
operator|.
name|createConnection
argument_list|()
decl_stmt|;
name|connection
operator|.
name|start
argument_list|()
expr_stmt|;
return|return
name|connection
return|;
block|}
specifier|private
name|Connection
name|createXAConnection
parameter_list|(
name|String
name|name
parameter_list|,
name|TransactionManager
name|tm
parameter_list|)
throws|throws
name|JMSException
block|{
name|ActiveMQXAConnectionFactory
name|cf
init|=
operator|new
name|ActiveMQXAConnectionFactory
argument_list|(
literal|"vm://"
operator|+
name|name
operator|+
literal|"?broker.persistent=false"
argument_list|)
decl_stmt|;
name|cf
operator|.
name|setRedeliveryPolicy
argument_list|(
name|redeliveryPolicy
argument_list|()
argument_list|)
expr_stmt|;
name|XaPooledConnectionFactory
name|cfp
init|=
operator|new
name|XaPooledConnectionFactory
argument_list|(
name|cf
argument_list|)
decl_stmt|;
name|cfp
operator|.
name|setTransactionManager
argument_list|(
name|tm
argument_list|)
expr_stmt|;
name|cfp
operator|.
name|setConnectionFactory
argument_list|(
name|cf
argument_list|)
expr_stmt|;
name|Connection
name|connection
init|=
name|cfp
operator|.
name|createConnection
argument_list|()
decl_stmt|;
name|connection
operator|.
name|start
argument_list|()
expr_stmt|;
return|return
name|connection
return|;
block|}
specifier|private
name|RedeliveryPolicy
name|redeliveryPolicy
parameter_list|()
block|{
name|RedeliveryPolicy
name|redeliveryPolicy
init|=
operator|new
name|RedeliveryPolicy
argument_list|()
decl_stmt|;
name|redeliveryPolicy
operator|.
name|setRedeliveryDelay
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|redeliveryPolicy
operator|.
name|setMaximumRedeliveries
argument_list|(
literal|1
argument_list|)
expr_stmt|;
return|return
name|redeliveryPolicy
return|;
block|}
specifier|protected
name|void
name|drainQueue
parameter_list|(
name|Connection
name|connection
parameter_list|,
name|Queue
name|dest
parameter_list|)
throws|throws
name|JMSException
throws|,
name|InterruptedException
block|{
name|Session
name|session
init|=
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
decl_stmt|;
name|MessageConsumer
name|consumer
init|=
name|session
operator|.
name|createConsumer
argument_list|(
name|dest
argument_list|)
decl_stmt|;
while|while
condition|(
name|consumer
operator|.
name|receiveNoWait
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Consuming old message"
argument_list|)
expr_stmt|;
block|}
name|consumer
operator|.
name|close
argument_list|()
expr_stmt|;
name|session
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertNumMessagesInQueue
argument_list|(
literal|""
argument_list|,
name|connection
argument_list|,
name|dest
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertNumMessagesInQueue
parameter_list|(
name|String
name|message
parameter_list|,
name|Connection
name|connection
parameter_list|,
name|Queue
name|queue
parameter_list|,
name|int
name|expectedNum
parameter_list|,
name|int
name|timeout
parameter_list|)
throws|throws
name|JMSException
throws|,
name|InterruptedException
block|{
name|long
name|startTime
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|int
name|actualNum
decl_stmt|;
do|do
block|{
name|actualNum
operator|=
name|getNumMessages
argument_list|(
name|connection
argument_list|,
name|queue
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Messages in queue "
operator|+
name|queue
operator|.
name|getQueueName
argument_list|()
operator|+
literal|": "
operator|+
name|actualNum
operator|+
literal|", expecting: "
operator|+
name|expectedNum
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
block|}
do|while
condition|(
operator|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|startTime
operator|<
name|timeout
operator|)
operator|&&
name|expectedNum
operator|!=
name|actualNum
condition|)
do|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|message
operator|+
literal|" -> number of messages"
argument_list|,
name|expectedNum
argument_list|,
name|actualNum
argument_list|)
expr_stmt|;
block|}
specifier|private
name|int
name|getNumMessages
parameter_list|(
name|Connection
name|connection
parameter_list|,
name|Queue
name|queue
parameter_list|)
throws|throws
name|JMSException
block|{
name|Session
name|session
init|=
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
decl_stmt|;
name|QueueBrowser
name|browser
init|=
name|session
operator|.
name|createBrowser
argument_list|(
name|queue
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Enumeration
argument_list|<
name|Message
argument_list|>
name|messages
init|=
name|browser
operator|.
name|getEnumeration
argument_list|()
decl_stmt|;
name|int
name|actualNum
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|messages
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|actualNum
operator|++
expr_stmt|;
name|messages
operator|.
name|nextElement
argument_list|()
expr_stmt|;
block|}
name|browser
operator|.
name|close
argument_list|()
expr_stmt|;
name|session
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|actualNum
return|;
block|}
specifier|private
name|void
name|sendMessage
parameter_list|(
name|Connection
name|connection
parameter_list|,
name|Destination
name|dest
parameter_list|,
name|String
name|content
parameter_list|)
throws|throws
name|JMSException
throws|,
name|InterruptedException
block|{
name|Session
name|session
init|=
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
decl_stmt|;
name|MessageProducer
name|prod
init|=
name|session
operator|.
name|createProducer
argument_list|(
name|dest
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
name|session
operator|.
name|createTextMessage
argument_list|(
name|content
argument_list|)
decl_stmt|;
name|prod
operator|.
name|send
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|prod
operator|.
name|close
argument_list|()
expr_stmt|;
name|session
operator|.
name|close
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
comment|// Give receiver some time to process
block|}
specifier|private
name|Queue
name|createQueue
parameter_list|(
name|Connection
name|connection
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|JMSException
block|{
name|Session
name|session
init|=
literal|null
decl_stmt|;
try|try
block|{
name|session
operator|=
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
expr_stmt|;
return|return
name|session
operator|.
name|createQueue
argument_list|(
name|name
argument_list|)
return|;
block|}
finally|finally
block|{
name|session
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
specifier|final
class|class
name|TestMessageListener
implements|implements
name|MessageListener
block|{
annotation|@
name|Override
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|TextMessage
name|textMessage
init|=
operator|(
name|TextMessage
operator|)
name|message
decl_stmt|;
try|try
block|{
name|String
name|text
init|=
name|textMessage
operator|.
name|getText
argument_list|()
decl_stmt|;
if|if
condition|(
name|OK
operator|.
name|equals
argument_list|(
name|text
argument_list|)
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Simulating Processing successful"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|FAIL
operator|.
name|equals
argument_list|(
name|text
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Simulating something went wrong. Expecting rollback"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|FAILFIRST
operator|.
name|equals
argument_list|(
name|text
argument_list|)
condition|)
block|{
if|if
condition|(
name|message
operator|.
name|getJMSRedelivered
argument_list|()
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Simulating processing worked on second try"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Simulating something went wrong. Expecting rollback"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid message type"
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
block|}
block|}
block|}
end_class

end_unit

