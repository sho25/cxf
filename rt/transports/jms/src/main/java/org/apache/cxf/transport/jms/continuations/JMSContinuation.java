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
name|continuations
package|;
end_package

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
name|Bus
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
name|BusFactory
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
name|classloader
operator|.
name|ClassLoaderUtils
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
name|classloader
operator|.
name|ClassLoaderUtils
operator|.
name|ClassLoaderHolder
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
name|continuations
operator|.
name|Continuation
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
name|PhaseInterceptorChain
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
name|transport
operator|.
name|MessageObserver
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
name|transport
operator|.
name|jms
operator|.
name|JMSConfiguration
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
name|workqueue
operator|.
name|WorkQueue
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
name|workqueue
operator|.
name|WorkQueueManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jms
operator|.
name|listener
operator|.
name|AbstractMessageListenerContainer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jms
operator|.
name|listener
operator|.
name|DefaultMessageListenerContainer
import|;
end_import

begin_class
specifier|public
class|class
name|JMSContinuation
implements|implements
name|Continuation
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
name|JMSContinuation
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|Message
name|inMessage
decl_stmt|;
specifier|private
name|MessageObserver
name|incomingObserver
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|JMSContinuation
argument_list|>
name|continuations
decl_stmt|;
specifier|private
name|AbstractMessageListenerContainer
name|jmsListener
decl_stmt|;
specifier|private
name|JMSConfiguration
name|jmsConfig
decl_stmt|;
specifier|private
specifier|volatile
name|Object
name|userObject
decl_stmt|;
specifier|private
specifier|volatile
name|boolean
name|isNew
init|=
literal|true
decl_stmt|;
specifier|private
specifier|volatile
name|boolean
name|isPending
decl_stmt|;
specifier|private
specifier|volatile
name|boolean
name|isResumed
decl_stmt|;
specifier|private
specifier|volatile
name|boolean
name|isCanceled
decl_stmt|;
specifier|private
name|WorkQueue
name|workQueue
decl_stmt|;
specifier|private
name|ClassLoader
name|loader
decl_stmt|;
specifier|public
name|JMSContinuation
parameter_list|(
name|Bus
name|b
parameter_list|,
name|Message
name|m
parameter_list|,
name|MessageObserver
name|observer
parameter_list|,
name|Collection
argument_list|<
name|JMSContinuation
argument_list|>
name|cList
parameter_list|,
name|AbstractMessageListenerContainer
name|jmsListener
parameter_list|,
name|JMSConfiguration
name|jmsConfig
parameter_list|)
block|{
name|bus
operator|=
name|b
expr_stmt|;
name|inMessage
operator|=
name|m
expr_stmt|;
name|incomingObserver
operator|=
name|observer
expr_stmt|;
name|continuations
operator|=
name|cList
expr_stmt|;
name|this
operator|.
name|jmsListener
operator|=
name|jmsListener
expr_stmt|;
name|this
operator|.
name|jmsConfig
operator|=
name|jmsConfig
expr_stmt|;
name|WorkQueueManager
name|manager
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|WorkQueueManager
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|manager
operator|!=
literal|null
condition|)
block|{
name|workQueue
operator|=
name|manager
operator|.
name|getNamedWorkQueue
argument_list|(
literal|"jms-continuation"
argument_list|)
expr_stmt|;
if|if
condition|(
name|workQueue
operator|==
literal|null
condition|)
block|{
name|workQueue
operator|=
name|manager
operator|.
name|getAutomaticWorkQueue
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"ERROR_GETTING_WORK_QUEUE"
argument_list|)
expr_stmt|;
block|}
name|loader
operator|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ClassLoader
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Object
name|getObject
parameter_list|()
block|{
return|return
name|userObject
return|;
block|}
specifier|public
name|boolean
name|isNew
parameter_list|()
block|{
return|return
name|isNew
return|;
block|}
specifier|public
name|boolean
name|isPending
parameter_list|()
block|{
return|return
name|isPending
return|;
block|}
specifier|public
name|boolean
name|isResumed
parameter_list|()
block|{
return|return
name|isResumed
return|;
block|}
specifier|public
specifier|synchronized
name|void
name|reset
parameter_list|()
block|{
name|cancelTimerTask
argument_list|()
expr_stmt|;
name|isNew
operator|=
literal|true
expr_stmt|;
name|isPending
operator|=
literal|false
expr_stmt|;
name|isResumed
operator|=
literal|false
expr_stmt|;
name|userObject
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
specifier|synchronized
name|void
name|resume
parameter_list|()
block|{
if|if
condition|(
name|isResumed
operator|||
operator|!
name|isPending
condition|)
block|{
return|return;
block|}
name|isResumed
operator|=
literal|true
expr_stmt|;
name|cancelTimerTask
argument_list|()
expr_stmt|;
name|doResume
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|doResume
parameter_list|()
block|{
name|updateContinuations
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ClassLoaderHolder
name|origLoader
init|=
literal|null
decl_stmt|;
name|Bus
name|origBus
init|=
name|BusFactory
operator|.
name|getAndSetThreadDefaultBus
argument_list|(
name|bus
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|loader
operator|!=
literal|null
condition|)
block|{
name|origLoader
operator|=
name|ClassLoaderUtils
operator|.
name|setThreadContextClassloader
argument_list|(
name|loader
argument_list|)
expr_stmt|;
block|}
name|incomingObserver
operator|.
name|onMessage
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|isPending
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|origBus
operator|!=
name|bus
condition|)
block|{
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|origBus
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|origLoader
operator|!=
literal|null
condition|)
block|{
name|origLoader
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|setObject
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|userObject
operator|=
name|o
expr_stmt|;
block|}
specifier|public
specifier|synchronized
name|boolean
name|suspend
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
if|if
condition|(
name|isPending
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// the current thread is different to the one which holds a lock on PhaseInterceptorChain
name|inMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|Message
operator|.
name|SUSPENDED_INVOCATION
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Need to get the right message which is handled in the interceptor chain
name|inMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|suspend
argument_list|()
expr_stmt|;
block|}
name|updateContinuations
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|isNew
operator|=
literal|false
expr_stmt|;
name|isResumed
operator|=
literal|false
expr_stmt|;
name|isPending
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|timeout
operator|>
literal|0
condition|)
block|{
name|createTimerTask
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
specifier|protected
name|void
name|createTimerTask
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
name|workQueue
operator|.
name|schedule
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
synchronized|synchronized
init|(
name|JMSContinuation
operator|.
name|this
init|)
block|{
if|if
condition|(
name|isPending
operator|&&
operator|!
name|isCanceled
condition|)
block|{
name|doResume
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|synchronized
name|void
name|cancelTimerTask
parameter_list|()
block|{
name|isCanceled
operator|=
literal|true
expr_stmt|;
block|}
specifier|protected
name|void
name|updateContinuations
parameter_list|(
name|boolean
name|remove
parameter_list|)
block|{
if|if
condition|(
name|jmsConfig
operator|.
name|getMaxSuspendedContinuations
argument_list|()
operator|<
literal|0
operator|||
operator|(
name|jmsListener
operator|instanceof
name|DefaultMessageListenerContainer
operator|&&
operator|(
operator|(
name|DefaultMessageListenerContainer
operator|)
name|jmsListener
operator|)
operator|.
name|getCacheLevel
argument_list|()
operator|>=
name|DefaultMessageListenerContainer
operator|.
name|CACHE_CONSUMER
operator|)
condition|)
block|{
name|modifyList
argument_list|(
name|remove
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// throttle the flow if there're too many continuation instances in memory
synchronized|synchronized
init|(
name|continuations
init|)
block|{
name|modifyList
argument_list|(
name|remove
argument_list|)
expr_stmt|;
if|if
condition|(
name|continuations
operator|.
name|size
argument_list|()
operator|>=
name|jmsConfig
operator|.
name|getMaxSuspendedContinuations
argument_list|()
condition|)
block|{
name|jmsListener
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|jmsListener
operator|.
name|isRunning
argument_list|()
condition|)
block|{
name|int
name|limit
init|=
name|jmsConfig
operator|.
name|getReconnectPercentOfMax
argument_list|()
decl_stmt|;
if|if
condition|(
name|limit
argument_list|<
literal|0
operator|||
name|limit
argument_list|>
literal|100
condition|)
block|{
name|limit
operator|=
literal|70
expr_stmt|;
block|}
name|limit
operator|=
operator|(
name|limit
operator|*
name|jmsConfig
operator|.
name|getMaxSuspendedContinuations
argument_list|()
operator|)
operator|/
literal|100
expr_stmt|;
if|if
condition|(
name|continuations
operator|.
name|size
argument_list|()
operator|<=
name|limit
condition|)
block|{
name|jmsListener
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|protected
name|void
name|modifyList
parameter_list|(
name|boolean
name|remove
parameter_list|)
block|{
if|if
condition|(
name|remove
condition|)
block|{
name|continuations
operator|.
name|remove
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|continuations
operator|.
name|add
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

