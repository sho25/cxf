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
name|Executor
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|transaction
operator|.
name|TransactionManager
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
specifier|abstract
class|class
name|AbstractMessageListenerContainer
implements|implements
name|JMSListenerContainer
block|{
specifier|protected
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|MessageListenerContainer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|Connection
name|connection
decl_stmt|;
specifier|protected
name|Destination
name|destination
decl_stmt|;
specifier|protected
name|MessageListener
name|listenerHandler
decl_stmt|;
specifier|protected
name|boolean
name|transacted
decl_stmt|;
specifier|protected
name|int
name|acknowledgeMode
init|=
name|Session
operator|.
name|AUTO_ACKNOWLEDGE
decl_stmt|;
specifier|protected
name|String
name|messageSelector
decl_stmt|;
specifier|protected
name|boolean
name|running
decl_stmt|;
specifier|protected
name|String
name|durableSubscriptionName
decl_stmt|;
specifier|protected
name|boolean
name|pubSubNoLocal
decl_stmt|;
specifier|protected
name|TransactionManager
name|transactionManager
decl_stmt|;
specifier|private
name|Executor
name|executor
decl_stmt|;
specifier|private
name|int
name|concurrentConsumers
init|=
literal|1
decl_stmt|;
specifier|private
name|boolean
name|internalExecutor
decl_stmt|;
specifier|public
name|AbstractMessageListenerContainer
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|public
name|Connection
name|getConnection
parameter_list|()
block|{
return|return
name|connection
return|;
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
if|if
condition|(
name|this
operator|.
name|transacted
condition|)
block|{
name|this
operator|.
name|acknowledgeMode
operator|=
name|Session
operator|.
name|SESSION_TRANSACTED
expr_stmt|;
block|}
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
specifier|protected
name|Executor
name|getExecutor
parameter_list|()
block|{
if|if
condition|(
name|executor
operator|==
literal|null
condition|)
block|{
name|executor
operator|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
name|concurrentConsumers
argument_list|)
expr_stmt|;
name|internalExecutor
operator|=
literal|true
expr_stmt|;
block|}
return|return
name|executor
return|;
block|}
specifier|public
name|void
name|setExecutor
parameter_list|(
name|Executor
name|executor
parameter_list|)
block|{
name|this
operator|.
name|executor
operator|=
name|executor
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|stop
parameter_list|()
block|{
comment|// In case of using external executor, don't shutdown it
if|if
condition|(
operator|(
name|executor
operator|==
literal|null
operator|)
operator|||
operator|!
name|internalExecutor
condition|)
block|{
return|return;
block|}
name|ExecutorService
name|executorService
init|=
operator|(
name|ExecutorService
operator|)
name|executor
decl_stmt|;
name|executorService
operator|.
name|shutdown
argument_list|()
expr_stmt|;
try|try
block|{
name|executorService
operator|.
name|awaitTermination
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
name|executorService
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
name|executor
operator|=
literal|null
expr_stmt|;
name|internalExecutor
operator|=
literal|false
expr_stmt|;
block|}
specifier|public
name|void
name|setDurableSubscriptionName
parameter_list|(
name|String
name|durableSubscriptionName
parameter_list|)
block|{
name|this
operator|.
name|durableSubscriptionName
operator|=
name|durableSubscriptionName
expr_stmt|;
block|}
specifier|public
name|void
name|setPubSubNoLocal
parameter_list|(
name|boolean
name|pubSubNoLocal
parameter_list|)
block|{
name|this
operator|.
name|pubSubNoLocal
operator|=
name|pubSubNoLocal
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
specifier|public
name|void
name|setTransactionManager
parameter_list|(
name|TransactionManager
name|transactionManager
parameter_list|)
block|{
name|this
operator|.
name|transactionManager
operator|=
name|transactionManager
expr_stmt|;
block|}
specifier|public
name|void
name|setConcurrentConsumers
parameter_list|(
name|int
name|concurrentConsumers
parameter_list|)
block|{
name|this
operator|.
name|concurrentConsumers
operator|=
name|concurrentConsumers
expr_stmt|;
block|}
specifier|public
name|int
name|getConcurrentConsumers
parameter_list|()
block|{
return|return
name|concurrentConsumers
return|;
block|}
block|}
end_class

end_unit

