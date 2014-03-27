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
name|JMSException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|NamingException
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
name|transport
operator|.
name|jms
operator|.
name|util
operator|.
name|JndiHelper
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
comment|/**      * Retrieve connection factory from JNDI      *       * @param jmsConfig      * @param jndiConfig      * @return      */
specifier|static
name|ConnectionFactory
name|getConnectionFactoryFromJndi
parameter_list|(
name|JMSConfiguration
name|jmsConfig
parameter_list|)
block|{
if|if
condition|(
name|jmsConfig
operator|.
name|getJndiEnvironment
argument_list|()
operator|==
literal|null
operator|||
name|jmsConfig
operator|.
name|getConnectionFactoryName
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
name|ConnectionFactory
name|cf
init|=
operator|new
name|JndiHelper
argument_list|(
name|jmsConfig
operator|.
name|getJndiEnvironment
argument_list|()
argument_list|)
operator|.
name|lookup
argument_list|(
name|jmsConfig
operator|.
name|getConnectionFactoryName
argument_list|()
argument_list|,
name|ConnectionFactory
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|cf
return|;
block|}
catch|catch
parameter_list|(
name|NamingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Create JmsSender from configuration information. Most settings are taken from jmsConfig. The QoS      * settings in messageProperties override the settings from jmsConfig      *       * @param jmsConfig configuration information      * @param messageProperties context headers override config settings      * @return      */
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
name|JMSConfiguration
name|jmsConfig
parameter_list|)
throws|throws
name|JMSException
block|{
name|Connection
name|connection
init|=
name|jmsConfig
operator|.
name|getConnectionFactory
argument_list|()
operator|.
name|createConnection
argument_list|(
name|jmsConfig
operator|.
name|getUserName
argument_list|()
argument_list|,
name|jmsConfig
operator|.
name|getPassword
argument_list|()
argument_list|)
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
return|return
name|connection
return|;
block|}
specifier|public
specifier|static
name|Executor
name|createExecutor
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
name|Executor
name|workQueue
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
name|name
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
name|workQueue
operator|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
literal|20
argument_list|)
expr_stmt|;
block|}
return|return
name|workQueue
return|;
block|}
block|}
end_class

end_unit

