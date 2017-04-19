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
name|ExceptionListener
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
name|transport
operator|.
name|jms
operator|.
name|util
operator|.
name|JMSSender
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
name|AutomaticWorkQueue
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

begin_comment
comment|/**  * Factory to create jms helper objects from configuration and context information  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|JMSFactory
block|{
specifier|public
specifier|static
specifier|final
name|String
name|JMS_DESTINATION_EXECUTOR
init|=
literal|"org.apache.cxf.extensions.jms.destination.executor"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JMS_CONDUIT_EXECUTOR
init|=
literal|"org.apache.cxf.extensions.jms.conduit.executor"
decl_stmt|;
specifier|static
specifier|final
name|String
name|MESSAGE_ENDPOINT_FACTORY
init|=
literal|"MessageEndpointFactory"
decl_stmt|;
specifier|static
specifier|final
name|String
name|MDB_TRANSACTED_METHOD
init|=
literal|"MDBTransactedMethod"
decl_stmt|;
comment|//private static final Logger LOG = LogUtils.getL7dLogger(JMSFactory.class);
specifier|private
name|JMSFactory
parameter_list|()
block|{     }
comment|/**      * Create JmsSender from configuration information. Most settings are taken from jmsConfig. The QoS      * settings in messageProperties override the settings from jmsConfig      *      * @param jmsConfig configuration information      * @param messageProperties context headers override config settings      * @return      */
specifier|public
specifier|static
name|JMSSender
name|createJmsSender
parameter_list|(
name|JMSConfiguration
name|jmsConfig
parameter_list|,
name|JMSMessageHeadersType
name|messageProperties
parameter_list|)
block|{
name|JMSSender
name|sender
init|=
operator|new
name|JMSSender
argument_list|()
decl_stmt|;
name|long
name|timeToLive
init|=
operator|(
name|messageProperties
operator|!=
literal|null
operator|&&
name|messageProperties
operator|.
name|isSetTimeToLive
argument_list|()
operator|)
condition|?
name|messageProperties
operator|.
name|getTimeToLive
argument_list|()
else|:
name|jmsConfig
operator|.
name|getTimeToLive
argument_list|()
decl_stmt|;
name|sender
operator|.
name|setTimeToLive
argument_list|(
name|timeToLive
argument_list|)
expr_stmt|;
name|int
name|priority
init|=
operator|(
name|messageProperties
operator|!=
literal|null
operator|&&
name|messageProperties
operator|.
name|isSetJMSPriority
argument_list|()
operator|)
condition|?
name|messageProperties
operator|.
name|getJMSPriority
argument_list|()
else|:
name|jmsConfig
operator|.
name|getPriority
argument_list|()
decl_stmt|;
name|sender
operator|.
name|setPriority
argument_list|(
name|priority
argument_list|)
expr_stmt|;
name|int
name|deliveryMode
init|=
operator|(
name|messageProperties
operator|!=
literal|null
operator|&&
name|messageProperties
operator|.
name|isSetJMSDeliveryMode
argument_list|()
operator|)
condition|?
name|messageProperties
operator|.
name|getJMSDeliveryMode
argument_list|()
else|:
name|jmsConfig
operator|.
name|getDeliveryMode
argument_list|()
decl_stmt|;
name|sender
operator|.
name|setDeliveryMode
argument_list|(
name|deliveryMode
argument_list|)
expr_stmt|;
name|sender
operator|.
name|setExplicitQosEnabled
argument_list|(
name|jmsConfig
operator|.
name|isExplicitQosEnabled
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|sender
return|;
block|}
specifier|static
name|String
name|getMessageSelector
parameter_list|(
name|JMSConfiguration
name|jmsConfig
parameter_list|,
name|String
name|conduitId
parameter_list|)
block|{
name|String
name|staticSelectorPrefix
init|=
name|jmsConfig
operator|.
name|getConduitSelectorPrefix
argument_list|()
decl_stmt|;
name|String
name|conduitIdSt
init|=
name|jmsConfig
operator|.
name|isUseConduitIdSelector
argument_list|()
operator|&&
name|conduitId
operator|!=
literal|null
condition|?
name|conduitId
else|:
literal|""
decl_stmt|;
name|String
name|correlationIdPrefix
init|=
name|staticSelectorPrefix
operator|+
name|conduitIdSt
decl_stmt|;
return|return
name|correlationIdPrefix
operator|.
name|isEmpty
argument_list|()
condition|?
literal|null
else|:
literal|"JMSCorrelationID LIKE '"
operator|+
name|correlationIdPrefix
operator|+
literal|"%'"
return|;
block|}
specifier|public
specifier|static
name|Connection
name|createConnection
parameter_list|(
specifier|final
name|JMSConfiguration
name|jmsConfig
parameter_list|)
throws|throws
name|JMSException
block|{
name|String
name|username
init|=
name|jmsConfig
operator|.
name|getUserName
argument_list|()
decl_stmt|;
name|ConnectionFactory
name|cf
init|=
name|jmsConfig
operator|.
name|getConnectionFactory
argument_list|()
decl_stmt|;
name|Connection
name|connection
init|=
name|username
operator|!=
literal|null
condition|?
name|cf
operator|.
name|createConnection
argument_list|(
name|username
argument_list|,
name|jmsConfig
operator|.
name|getPassword
argument_list|()
argument_list|)
else|:
name|cf
operator|.
name|createConnection
argument_list|()
decl_stmt|;
if|if
condition|(
name|jmsConfig
operator|.
name|getDurableSubscriptionClientId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|connection
operator|.
name|setClientID
argument_list|(
name|jmsConfig
operator|.
name|getDurableSubscriptionClientId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|connection
operator|.
name|setExceptionListener
argument_list|(
operator|new
name|ExceptionListener
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onException
parameter_list|(
name|JMSException
name|exception
parameter_list|)
block|{
name|jmsConfig
operator|.
name|resetCachedReplyDestination
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|connection
return|;
block|}
comment|/**      * Get workqueue from workqueue manager. Return an executor that will never reject messages and      * instead block when all threads are used.      *      * @param bus      * @param name      * @return      */
specifier|public
specifier|static
name|Executor
name|createWorkQueueExecutor
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|String
name|name
parameter_list|)
block|{
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
name|AutomaticWorkQueue
name|workQueue1
init|=
name|manager
operator|.
name|getNamedWorkQueue
argument_list|(
name|name
argument_list|)
decl_stmt|;
specifier|final
name|WorkQueue
name|workQueue
init|=
operator|(
name|workQueue1
operator|==
literal|null
operator|)
condition|?
name|manager
operator|.
name|getAutomaticWorkQueue
argument_list|()
else|:
name|workQueue1
decl_stmt|;
return|return
operator|new
name|Executor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|(
name|Runnable
name|command
parameter_list|)
block|{
name|workQueue
operator|.
name|execute
argument_list|(
name|command
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
else|else
block|{
return|return
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
literal|20
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

