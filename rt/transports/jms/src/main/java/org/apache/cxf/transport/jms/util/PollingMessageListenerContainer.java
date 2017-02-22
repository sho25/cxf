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

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Topic
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|transaction
operator|.
name|Status
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|transaction
operator|.
name|Transaction
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

begin_class
specifier|public
class|class
name|PollingMessageListenerContainer
extends|extends
name|AbstractMessageListenerContainer
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
name|PollingMessageListenerContainer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|PollingMessageListenerContainer
parameter_list|(
name|Connection
name|connection
parameter_list|,
name|Destination
name|destination
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
name|destination
operator|=
name|destination
expr_stmt|;
name|this
operator|.
name|listenerHandler
operator|=
name|listenerHandler
expr_stmt|;
block|}
specifier|private
class|class
name|Poller
extends|extends
name|AbstractPoller
implements|implements
name|Runnable
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|Session
name|session
init|=
literal|null
decl_stmt|;
name|init
argument_list|()
expr_stmt|;
while|while
condition|(
name|running
condition|)
block|{
try|try
init|(
name|ResourceCloser
name|closer
init|=
operator|new
name|ResourceCloser
argument_list|()
init|)
block|{
name|closer
operator|.
name|register
argument_list|(
name|createInitialContext
argument_list|()
argument_list|)
expr_stmt|;
comment|// Create session early to optimize performance
name|session
operator|=
name|closer
operator|.
name|register
argument_list|(
name|connection
operator|.
name|createSession
argument_list|(
name|transacted
argument_list|,
name|acknowledgeMode
argument_list|)
argument_list|)
expr_stmt|;
name|MessageConsumer
name|consumer
init|=
name|closer
operator|.
name|register
argument_list|(
name|createConsumer
argument_list|(
name|session
argument_list|)
argument_list|)
decl_stmt|;
while|while
condition|(
name|running
condition|)
block|{
name|Message
name|message
init|=
name|consumer
operator|.
name|receive
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|listenerHandler
operator|.
name|onMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|session
operator|.
name|getTransacted
argument_list|()
condition|)
block|{
name|session
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Exception while processing jms message in cxf. Rolling back"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|safeRollBack
argument_list|(
name|session
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
name|catchUnexpectedExceptionDuringPolling
argument_list|(
literal|null
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|safeRollBack
parameter_list|(
name|Session
name|session
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|session
operator|!=
literal|null
operator|&&
name|session
operator|.
name|getTransacted
argument_list|()
condition|)
block|{
name|session
operator|.
name|rollback
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e1
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Rollback of Local transaction failed"
argument_list|,
name|e1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
class|class
name|XAPoller
extends|extends
name|AbstractPoller
implements|implements
name|Runnable
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|init
argument_list|()
expr_stmt|;
while|while
condition|(
name|running
condition|)
block|{
try|try
init|(
name|ResourceCloser
name|closer
init|=
operator|new
name|ResourceCloser
argument_list|()
init|)
block|{
name|closer
operator|.
name|register
argument_list|(
name|createInitialContext
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Transaction
name|externalTransaction
init|=
name|transactionManager
operator|.
name|getTransaction
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|externalTransaction
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|externalTransaction
operator|.
name|getStatus
argument_list|()
operator|==
name|Status
operator|.
name|STATUS_ACTIVE
operator|)
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"External transactions are not supported in XAPoller"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"External transactions are not supported in XAPoller"
argument_list|)
throw|;
block|}
name|transactionManager
operator|.
name|begin
argument_list|()
expr_stmt|;
comment|/*                      * Create session inside transaction to give it the                      * chance to enlist itself as a resource                      */
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
name|transacted
argument_list|,
name|acknowledgeMode
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
name|createConsumer
argument_list|(
name|session
argument_list|)
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
name|consumer
operator|.
name|receive
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|listenerHandler
operator|.
name|onMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
name|transactionManager
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Exception while processing jms message in cxf. Rolling back"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|safeRollBack
argument_list|(
name|session
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|catchUnexpectedExceptionDuringPolling
argument_list|(
literal|null
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|safeRollBack
parameter_list|(
name|Session
name|session
parameter_list|)
block|{
try|try
block|{
name|transactionManager
operator|.
name|rollback
argument_list|()
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
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Rollback of XA transaction failed"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
specifier|abstract
class|class
name|AbstractPoller
block|{
specifier|private
specifier|static
specifier|final
name|String
name|RETRY_COUNTER_ON_EXCEPTION
init|=
literal|"jms.polling.retrycounteronexception"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SLEEPING_TIME_BEFORE_RETRY
init|=
literal|"jms.polling.sleepingtimebeforeretry"
decl_stmt|;
specifier|protected
name|int
name|retryCounter
init|=
operator|-
literal|1
decl_stmt|;
specifier|protected
name|int
name|counter
decl_stmt|;
specifier|protected
name|int
name|sleepingTime
init|=
literal|5000
decl_stmt|;
specifier|protected
name|void
name|init
parameter_list|()
block|{
if|if
condition|(
name|jndiEnvironment
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|jndiEnvironment
operator|.
name|containsKey
argument_list|(
name|RETRY_COUNTER_ON_EXCEPTION
argument_list|)
condition|)
block|{
name|retryCounter
operator|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|jndiEnvironment
operator|.
name|getProperty
argument_list|(
name|RETRY_COUNTER_ON_EXCEPTION
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|jndiEnvironment
operator|.
name|containsKey
argument_list|(
name|SLEEPING_TIME_BEFORE_RETRY
argument_list|)
condition|)
block|{
name|sleepingTime
operator|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|jndiEnvironment
operator|.
name|getProperty
argument_list|(
name|SLEEPING_TIME_BEFORE_RETRY
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|boolean
name|hasToCount
parameter_list|()
block|{
return|return
name|retryCounter
operator|>
operator|-
literal|1
return|;
block|}
specifier|protected
name|boolean
name|hasToStop
parameter_list|()
block|{
return|return
name|counter
operator|>
name|retryCounter
return|;
block|}
specifier|protected
name|void
name|catchUnexpectedExceptionDuringPolling
parameter_list|(
name|Session
name|session
parameter_list|,
name|Throwable
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Unexpected exception."
argument_list|,
name|e
argument_list|)
expr_stmt|;
if|if
condition|(
name|hasToCount
argument_list|()
condition|)
block|{
name|counter
operator|++
expr_stmt|;
if|if
condition|(
name|hasToStop
argument_list|()
condition|)
block|{
name|stop
argument_list|(
name|session
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|running
condition|)
block|{
try|try
block|{
name|String
name|log
init|=
literal|"Now sleeping for "
operator|+
name|sleepingTime
operator|/
literal|1000
operator|+
literal|" seconds"
decl_stmt|;
name|log
operator|+=
name|hasToCount
argument_list|()
condition|?
literal|". Then restarting session and consumer: attempt "
operator|+
name|counter
operator|+
literal|"/"
operator|+
name|retryCounter
else|:
literal|""
expr_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
name|log
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
name|sleepingTime
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e1
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
name|e1
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|void
name|stop
parameter_list|(
name|Session
name|session
parameter_list|,
name|Throwable
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Stopping the jms message polling thread in cxf"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|safeRollBack
argument_list|(
name|session
argument_list|)
expr_stmt|;
name|running
operator|=
literal|false
expr_stmt|;
block|}
specifier|protected
specifier|abstract
name|void
name|safeRollBack
parameter_list|(
name|Session
name|session
parameter_list|)
function_decl|;
block|}
specifier|private
name|MessageConsumer
name|createConsumer
parameter_list|(
name|Session
name|session
parameter_list|)
throws|throws
name|JMSException
block|{
if|if
condition|(
name|durableSubscriptionName
operator|!=
literal|null
operator|&&
name|destination
operator|instanceof
name|Topic
condition|)
block|{
return|return
name|session
operator|.
name|createDurableSubscriber
argument_list|(
operator|(
name|Topic
operator|)
name|destination
argument_list|,
name|durableSubscriptionName
argument_list|,
name|messageSelector
argument_list|,
name|pubSubNoLocal
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|session
operator|.
name|createConsumer
argument_list|(
name|destination
argument_list|,
name|messageSelector
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|start
parameter_list|()
block|{
if|if
condition|(
name|running
condition|)
block|{
return|return;
block|}
name|running
operator|=
literal|true
expr_stmt|;
for|for
control|(
name|int
name|c
init|=
literal|0
init|;
name|c
operator|<
name|getConcurrentConsumers
argument_list|()
condition|;
name|c
operator|++
control|)
block|{
name|Runnable
name|poller
init|=
operator|(
name|transactionManager
operator|!=
literal|null
operator|)
condition|?
operator|new
name|XAPoller
argument_list|()
else|:
operator|new
name|Poller
argument_list|()
decl_stmt|;
name|getExecutor
argument_list|()
operator|.
name|execute
argument_list|(
name|poller
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Shuttting down "
operator|+
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|running
condition|)
block|{
return|return;
block|}
name|running
operator|=
literal|false
expr_stmt|;
name|super
operator|.
name|stop
argument_list|()
expr_stmt|;
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
block|}
block|}
end_class

end_unit

