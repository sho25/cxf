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
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

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
name|InputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|DeliveryMode
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
name|bus
operator|.
name|spring
operator|.
name|SpringBusFactory
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
name|helpers
operator|.
name|IOUtils
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
name|Exchange
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
name|ExchangeImpl
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
name|message
operator|.
name|MessageImpl
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
name|security
operator|.
name|SecurityContext
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
name|Conduit
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
name|MultiplexDestination
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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
name|JMSDestinationTest
extends|extends
name|AbstractJMSTester
block|{
specifier|private
specifier|static
specifier|final
name|int
name|MAX_RECEIVE_TIME
init|=
literal|5
decl_stmt|;
specifier|private
name|Message
name|destMessage
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|createAndStartBroker
parameter_list|()
throws|throws
name|Exception
block|{
name|startBroker
argument_list|(
operator|new
name|JMSBrokerSetup
argument_list|(
literal|"tcp://localhost:61500"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|waitForReceiveInMessage
parameter_list|()
block|{
name|int
name|waitTime
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|inMessage
operator|==
literal|null
operator|&&
name|waitTime
operator|<
name|MAX_RECEIVE_TIME
condition|)
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// do nothing here
block|}
name|waitTime
operator|++
expr_stmt|;
block|}
name|assertTrue
argument_list|(
literal|"Can't receive the Conduit Message in "
operator|+
name|MAX_RECEIVE_TIME
operator|+
literal|" seconds"
argument_list|,
name|inMessage
operator|!=
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|waitForReceiveDestMessage
parameter_list|()
block|{
name|int
name|waitTime
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|destMessage
operator|==
literal|null
operator|&&
name|waitTime
operator|<
name|MAX_RECEIVE_TIME
condition|)
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// do nothing here
block|}
name|waitTime
operator|++
expr_stmt|;
block|}
name|assertTrue
argument_list|(
literal|"Can't receive the Destination message in "
operator|+
name|MAX_RECEIVE_TIME
operator|+
literal|" seconds"
argument_list|,
name|destMessage
operator|!=
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JMSDestination
name|setupJMSDestination
parameter_list|(
name|boolean
name|send
parameter_list|)
throws|throws
name|IOException
block|{
name|JMSConfiguration
name|jmsConfig
init|=
operator|new
name|JMSOldConfigHolder
argument_list|()
operator|.
name|createJMSConfigurationFromEndpointInfo
argument_list|(
name|bus
argument_list|,
name|endpointInfo
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|JMSDestination
name|jmsDestination
init|=
operator|new
name|JMSDestination
argument_list|(
name|bus
argument_list|,
name|endpointInfo
argument_list|,
name|jmsConfig
argument_list|)
decl_stmt|;
if|if
condition|(
name|send
condition|)
block|{
comment|// setMessageObserver
name|observer
operator|=
operator|new
name|MessageObserver
argument_list|()
block|{
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setInMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|m
operator|.
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|destMessage
operator|=
name|m
expr_stmt|;
block|}
block|}
expr_stmt|;
name|jmsDestination
operator|.
name|setMessageObserver
argument_list|(
name|observer
argument_list|)
expr_stmt|;
block|}
return|return
name|jmsDestination
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetConfigurationFromSpring
parameter_list|()
throws|throws
name|Exception
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|bus
operator|=
name|bf
operator|.
name|createBus
argument_list|(
literal|"/jms_test_config.xml"
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|setupServiceInfo
argument_list|(
literal|"http://cxf.apache.org/jms_conf_test"
argument_list|,
literal|"/wsdl/others/jms_test_no_addr.wsdl"
argument_list|,
literal|"HelloWorldQueueBinMsgService"
argument_list|,
literal|"HelloWorldQueueBinMsgPort"
argument_list|)
expr_stmt|;
name|JMSDestination
name|destination
init|=
name|setupJMSDestination
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|JMSConfiguration
name|jmsConfig
init|=
name|destination
operator|.
name|getJmsConfig
argument_list|()
decl_stmt|;
comment|//JmsTemplate jmsTemplate = destination.getJmsTemplate();
comment|//AbstractMessageListenerContainer jmsListener = destination.getJmsListener();
name|assertEquals
argument_list|(
literal|"Can't get the right ServerConfig's MessageTimeToLive "
argument_list|,
literal|500L
argument_list|,
name|jmsConfig
operator|.
name|getTimeToLive
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Can't get the right Server's MessageSelector"
argument_list|,
literal|"cxf_message_selector"
argument_list|,
name|jmsConfig
operator|.
name|getMessageSelector
argument_list|()
argument_list|)
expr_stmt|;
comment|// assertEquals("Can't get the right SessionPoolConfig's LowWaterMark", 10,
comment|// jmsListener.getLowWaterMark());
comment|// assertEquals("Can't get the right AddressPolicy's ConnectionPassword", "testPassword",
comment|// .getConnectionPassword());
name|assertEquals
argument_list|(
literal|"Can't get the right DurableSubscriberName"
argument_list|,
literal|"cxf_subscriber"
argument_list|,
name|jmsConfig
operator|.
name|getDurableSubscriptionName
argument_list|()
argument_list|)
expr_stmt|;
comment|/*setupServiceInfo("http://cxf.apache.org/hello_world_jms", "/wsdl/jms_test.wsdl",                          "HelloWorldQueueBinMsgService", "HelloWorldQueueBinMsgPort");         destination = setupJMSDestination(false);         jmsConfig = destination.getJmsConfig();*/
name|assertEquals
argument_list|(
literal|"The receiveTimeout should be set"
argument_list|,
name|jmsConfig
operator|.
name|getReceiveTimeout
argument_list|()
operator|.
name|longValue
argument_list|()
argument_list|,
literal|1500L
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The concurrentConsumer should be set"
argument_list|,
name|jmsConfig
operator|.
name|getConcurrentConsumers
argument_list|()
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The maxConcurrentConsumer should be set"
argument_list|,
name|jmsConfig
operator|.
name|getMaxConcurrentConsumers
argument_list|()
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The maxSuspendedContinuations should be set"
argument_list|,
name|jmsConfig
operator|.
name|getMaxSuspendedContinuations
argument_list|()
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"The acceptMessagesWhileStopping should be set to true"
argument_list|,
name|jmsConfig
operator|.
name|isAcceptMessagesWhileStopping
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"The connectionFactory should not be null"
argument_list|,
name|jmsConfig
operator|.
name|getConnectionFactory
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should get the instance of ActiveMQConnectionFactory"
argument_list|,
name|jmsConfig
operator|.
name|getConnectionFactory
argument_list|()
operator|instanceof
name|ActiveMQConnectionFactory
argument_list|)
expr_stmt|;
name|ActiveMQConnectionFactory
name|cf
init|=
operator|(
name|ActiveMQConnectionFactory
operator|)
name|jmsConfig
operator|.
name|getConnectionFactory
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"The borker URL is wrong"
argument_list|,
name|cf
operator|.
name|getBrokerURL
argument_list|()
argument_list|,
literal|"tcp://localhost:61500"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong TargetDestination"
argument_list|,
name|jmsConfig
operator|.
name|getTargetDestination
argument_list|()
argument_list|,
literal|"queue:test"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get the wrong pubSubDomain value"
argument_list|,
name|jmsConfig
operator|.
name|isPubSubDomain
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetConfigurationFromWSDL
parameter_list|()
throws|throws
name|Exception
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|bus
operator|=
name|bf
operator|.
name|createBus
argument_list|()
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|setupServiceInfo
argument_list|(
literal|"http://cxf.apache.org/hello_world_jms"
argument_list|,
literal|"/wsdl/jms_test.wsdl"
argument_list|,
literal|"HelloWorldQueueBinMsgService"
argument_list|,
literal|"HelloWorldQueueBinMsgPort"
argument_list|)
expr_stmt|;
name|JMSDestination
name|destination
init|=
name|setupJMSDestination
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Can't get the right DurableSubscriberName"
argument_list|,
literal|"CXF_subscriber"
argument_list|,
name|destination
operator|.
name|getJmsConfig
argument_list|()
operator|.
name|getDurableSubscriptionName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Can't get the right AddressPolicy's Destination"
argument_list|,
literal|"dynamicQueues/test.jmstransport.binary"
argument_list|,
name|destination
operator|.
name|getJmsConfig
argument_list|()
operator|.
name|getTargetDestination
argument_list|()
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDurableSubscriber
parameter_list|()
throws|throws
name|Exception
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|bus
operator|=
name|bf
operator|.
name|createBus
argument_list|(
literal|"jms_test_config.xml"
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|destMessage
operator|=
literal|null
expr_stmt|;
name|inMessage
operator|=
literal|null
expr_stmt|;
name|setupServiceInfo
argument_list|(
literal|"http://cxf.apache.org/hello_world_jms"
argument_list|,
literal|"/wsdl/jms_test.wsdl"
argument_list|,
literal|"HelloWorldPubSubService"
argument_list|,
literal|"HelloWorldPubSubPort"
argument_list|)
expr_stmt|;
name|JMSConduit
name|conduit
init|=
name|setupJMSConduit
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|Message
name|outMessage
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|setupMessageHeader
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
name|JMSDestination
name|destination
init|=
name|setupJMSDestination
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|destination
operator|.
name|activate
argument_list|()
expr_stmt|;
name|sendoutMessage
argument_list|(
name|conduit
argument_list|,
name|outMessage
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// wait for the message to be get from the destination
name|waitForReceiveDestMessage
argument_list|()
expr_stmt|;
comment|// just verify the Destination inMessage
name|assertTrue
argument_list|(
literal|"The destiantion should have got the message "
argument_list|,
name|destMessage
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|verifyReceivedMessage
argument_list|(
name|destMessage
argument_list|)
expr_stmt|;
name|verifyHeaders
argument_list|(
name|destMessage
argument_list|,
name|outMessage
argument_list|)
expr_stmt|;
name|conduit
operator|.
name|close
argument_list|()
expr_stmt|;
name|destination
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOneWayDestination
parameter_list|()
throws|throws
name|Exception
block|{
name|destMessage
operator|=
literal|null
expr_stmt|;
name|inMessage
operator|=
literal|null
expr_stmt|;
name|setupServiceInfo
argument_list|(
literal|"http://cxf.apache.org/hello_world_jms"
argument_list|,
literal|"/wsdl/jms_test.wsdl"
argument_list|,
literal|"HWStaticReplyQBinMsgService"
argument_list|,
literal|"HWStaticReplyQBinMsgPort"
argument_list|)
expr_stmt|;
name|JMSConduit
name|conduit
init|=
name|setupJMSConduit
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|Message
name|outMessage
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|setupMessageHeader
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
name|JMSDestination
name|destination
init|=
name|setupJMSDestination
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|destination
operator|.
name|activate
argument_list|()
expr_stmt|;
name|sendoutMessage
argument_list|(
name|conduit
argument_list|,
name|outMessage
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// wait for the message to be get from the destination
name|waitForReceiveDestMessage
argument_list|()
expr_stmt|;
comment|// just verify the Destination inMessage
name|assertTrue
argument_list|(
literal|"The destiantion should have got the message "
argument_list|,
name|destMessage
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|verifyReceivedMessage
argument_list|(
name|destMessage
argument_list|)
expr_stmt|;
name|verifyHeaders
argument_list|(
name|destMessage
argument_list|,
name|outMessage
argument_list|)
expr_stmt|;
name|conduit
operator|.
name|close
argument_list|()
expr_stmt|;
name|destination
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|setupMessageHeader
parameter_list|(
name|Message
name|outMessage
parameter_list|,
name|String
name|correlationId
parameter_list|)
block|{
name|JMSMessageHeadersType
name|header
init|=
operator|new
name|JMSMessageHeadersType
argument_list|()
decl_stmt|;
name|header
operator|.
name|setJMSCorrelationID
argument_list|(
name|correlationId
argument_list|)
expr_stmt|;
name|header
operator|.
name|setJMSDeliveryMode
argument_list|(
name|DeliveryMode
operator|.
name|PERSISTENT
argument_list|)
expr_stmt|;
name|header
operator|.
name|setJMSPriority
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|header
operator|.
name|setTimeToLive
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|outMessage
operator|.
name|put
argument_list|(
name|JMSConstants
operator|.
name|JMS_CLIENT_REQUEST_HEADERS
argument_list|,
name|header
argument_list|)
expr_stmt|;
name|outMessage
operator|.
name|put
argument_list|(
name|Message
operator|.
name|ENCODING
argument_list|,
literal|"US-ASCII"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|setupMessageHeader
parameter_list|(
name|Message
name|outMessage
parameter_list|)
block|{
name|setupMessageHeader
argument_list|(
name|outMessage
argument_list|,
literal|"Destination test"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|verifyReceivedMessage
parameter_list|(
name|Message
name|inMessage
parameter_list|)
block|{
name|ByteArrayInputStream
name|bis
init|=
operator|(
name|ByteArrayInputStream
operator|)
name|inMessage
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|byte
name|bytes
index|[]
init|=
operator|new
name|byte
index|[
name|bis
operator|.
name|available
argument_list|()
index|]
decl_stmt|;
try|try
block|{
name|bis
operator|.
name|read
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|assertFalse
argument_list|(
literal|"Read the Destination recieved Message error "
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
name|String
name|response
init|=
name|IOUtils
operator|.
name|newStringFromBytes
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"The response content should be equal"
argument_list|,
name|AbstractJMSTester
operator|.
name|MESSAGE_CONTENT
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|verifyRequestResponseHeaders
parameter_list|(
name|Message
name|inMessage
parameter_list|,
name|Message
name|outMessage
parameter_list|)
block|{
name|JMSMessageHeadersType
name|outHeader
init|=
operator|(
name|JMSMessageHeadersType
operator|)
name|outMessage
operator|.
name|get
argument_list|(
name|JMSConstants
operator|.
name|JMS_CLIENT_REQUEST_HEADERS
argument_list|)
decl_stmt|;
name|String
name|inEncoding
init|=
operator|(
name|String
operator|)
name|inMessage
operator|.
name|get
argument_list|(
name|Message
operator|.
name|ENCODING
argument_list|)
decl_stmt|;
name|String
name|outEncoding
init|=
operator|(
name|String
operator|)
name|outMessage
operator|.
name|get
argument_list|(
name|Message
operator|.
name|ENCODING
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"The message encoding should be equal"
argument_list|,
name|inEncoding
argument_list|,
name|outEncoding
argument_list|)
expr_stmt|;
name|JMSMessageHeadersType
name|inHeader
init|=
operator|(
name|JMSMessageHeadersType
operator|)
name|inMessage
operator|.
name|get
argument_list|(
name|JMSConstants
operator|.
name|JMS_CLIENT_RESPONSE_HEADERS
argument_list|)
decl_stmt|;
name|verifyJmsHeaderEquality
argument_list|(
name|outHeader
argument_list|,
name|inHeader
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|verifyHeaders
parameter_list|(
name|Message
name|inMessage
parameter_list|,
name|Message
name|outMessage
parameter_list|)
block|{
name|JMSMessageHeadersType
name|outHeader
init|=
operator|(
name|JMSMessageHeadersType
operator|)
name|outMessage
operator|.
name|get
argument_list|(
name|JMSConstants
operator|.
name|JMS_CLIENT_REQUEST_HEADERS
argument_list|)
decl_stmt|;
name|JMSMessageHeadersType
name|inHeader
init|=
operator|(
name|JMSMessageHeadersType
operator|)
name|inMessage
operator|.
name|get
argument_list|(
name|JMSConstants
operator|.
name|JMS_SERVER_REQUEST_HEADERS
argument_list|)
decl_stmt|;
name|verifyJmsHeaderEquality
argument_list|(
name|outHeader
argument_list|,
name|inHeader
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|verifyJmsHeaderEquality
parameter_list|(
name|JMSMessageHeadersType
name|outHeader
parameter_list|,
name|JMSMessageHeadersType
name|inHeader
parameter_list|)
block|{
if|if
condition|(
name|outHeader
operator|.
name|getJMSCorrelationID
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// only check if the correlation id was explicitly set as
comment|// otherwise the in header will contain an automatically
comment|// generated correlation id
name|assertEquals
argument_list|(
literal|"The inMessage and outMessage JMS Header's CorrelationID should be equals"
argument_list|,
name|outHeader
operator|.
name|getJMSCorrelationID
argument_list|()
argument_list|,
name|inHeader
operator|.
name|getJMSCorrelationID
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|"The inMessage and outMessage JMS Header's JMSPriority should be equals"
argument_list|,
name|outHeader
operator|.
name|getJMSPriority
argument_list|()
argument_list|,
name|inHeader
operator|.
name|getJMSPriority
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The inMessage and outMessage JMS Header's JMSDeliveryMode should be equals"
argument_list|,
name|outHeader
operator|.
name|getJMSDeliveryMode
argument_list|()
argument_list|,
name|inHeader
operator|.
name|getJMSDeliveryMode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The inMessage and outMessage JMS Header's JMSType should be equals"
argument_list|,
name|outHeader
operator|.
name|getJMSType
argument_list|()
argument_list|,
name|inHeader
operator|.
name|getJMSType
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRoundTripDestination
parameter_list|()
throws|throws
name|Exception
block|{
name|inMessage
operator|=
literal|null
expr_stmt|;
name|setupServiceInfo
argument_list|(
literal|"http://cxf.apache.org/hello_world_jms"
argument_list|,
literal|"/wsdl/jms_test.wsdl"
argument_list|,
literal|"HelloWorldService"
argument_list|,
literal|"HelloWorldPort"
argument_list|)
expr_stmt|;
comment|// set up the conduit send to be true
name|JMSConduit
name|conduit
init|=
name|setupJMSConduit
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|)
decl_stmt|;
specifier|final
name|Message
name|outMessage
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|setupMessageHeader
argument_list|(
name|outMessage
argument_list|,
literal|null
argument_list|)
expr_stmt|;
specifier|final
name|JMSDestination
name|destination
init|=
name|setupJMSDestination
argument_list|(
literal|true
argument_list|)
decl_stmt|;
comment|// set up MessageObserver for handling the conduit message
name|MessageObserver
name|observer
init|=
operator|new
name|MessageObserver
argument_list|()
block|{
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setInMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|m
operator|.
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|verifyReceivedMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|verifyHeaders
argument_list|(
name|m
argument_list|,
name|outMessage
argument_list|)
expr_stmt|;
comment|// setup the message for
name|Conduit
name|backConduit
decl_stmt|;
try|try
block|{
name|backConduit
operator|=
name|destination
operator|.
name|getBackChannel
argument_list|(
name|m
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// wait for the message to be got from the conduit
name|Message
name|replyMessage
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|sendoutMessage
argument_list|(
name|backConduit
argument_list|,
name|replyMessage
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
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
block|}
decl_stmt|;
name|destination
operator|.
name|setMessageObserver
argument_list|(
name|observer
argument_list|)
expr_stmt|;
comment|// set is oneway false for get response from destination
name|sendoutMessage
argument_list|(
name|conduit
argument_list|,
name|outMessage
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// wait for the message to be got from the destination,
comment|// create the thread to handler the Destination incoming message
name|waitForReceiveInMessage
argument_list|()
expr_stmt|;
name|verifyReceivedMessage
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
comment|// wait for a while for the jms session recycling
name|inMessage
operator|=
literal|null
expr_stmt|;
comment|// Send a second message to check for an issue
comment|// Where the session was closed the second time
name|sendoutMessage
argument_list|(
name|conduit
argument_list|,
name|outMessage
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|waitForReceiveInMessage
argument_list|()
expr_stmt|;
name|verifyReceivedMessage
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|conduit
operator|.
name|close
argument_list|()
expr_stmt|;
name|destination
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testProperty
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|customPropertyName
init|=
literal|"THIS_PROPERTY_WILL_NOT_BE_AUTO_COPIED"
decl_stmt|;
name|inMessage
operator|=
literal|null
expr_stmt|;
name|setupServiceInfo
argument_list|(
literal|"http://cxf.apache.org/hello_world_jms"
argument_list|,
literal|"/wsdl/jms_test.wsdl"
argument_list|,
literal|"HelloWorldService"
argument_list|,
literal|"HelloWorldPort"
argument_list|)
expr_stmt|;
comment|// set up the conduit send to be true
name|JMSConduit
name|conduit
init|=
name|setupJMSConduit
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|)
decl_stmt|;
specifier|final
name|Message
name|outMessage
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|setupMessageHeader
argument_list|(
name|outMessage
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|JMSPropertyType
name|excludeProp
init|=
operator|new
name|JMSPropertyType
argument_list|()
decl_stmt|;
name|excludeProp
operator|.
name|setName
argument_list|(
name|customPropertyName
argument_list|)
expr_stmt|;
name|excludeProp
operator|.
name|setValue
argument_list|(
name|customPropertyName
argument_list|)
expr_stmt|;
name|JMSMessageHeadersType
name|headers
init|=
operator|(
name|JMSMessageHeadersType
operator|)
name|outMessage
operator|.
name|get
argument_list|(
name|JMSConstants
operator|.
name|JMS_CLIENT_REQUEST_HEADERS
argument_list|)
decl_stmt|;
name|headers
operator|.
name|getProperty
argument_list|()
operator|.
name|add
argument_list|(
name|excludeProp
argument_list|)
expr_stmt|;
specifier|final
name|JMSDestination
name|destination
init|=
name|setupJMSDestination
argument_list|(
literal|true
argument_list|)
decl_stmt|;
comment|// set up MessageObserver for handling the conduit message
name|MessageObserver
name|observer
init|=
operator|new
name|MessageObserver
argument_list|()
block|{
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setInMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|m
operator|.
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|verifyReceivedMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|verifyHeaders
argument_list|(
name|m
argument_list|,
name|outMessage
argument_list|)
expr_stmt|;
comment|// setup the message for
name|Conduit
name|backConduit
decl_stmt|;
try|try
block|{
name|backConduit
operator|=
name|destination
operator|.
name|getBackChannel
argument_list|(
name|m
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// wait for the message to be got from the conduit
name|Message
name|replyMessage
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
comment|// copy the message encoding
name|replyMessage
operator|.
name|put
argument_list|(
name|Message
operator|.
name|ENCODING
argument_list|,
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|ENCODING
argument_list|)
argument_list|)
expr_stmt|;
name|sendoutMessage
argument_list|(
name|backConduit
argument_list|,
name|replyMessage
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
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
block|}
decl_stmt|;
name|destination
operator|.
name|setMessageObserver
argument_list|(
name|observer
argument_list|)
expr_stmt|;
comment|// set is oneway false for get response from destination
name|sendoutMessage
argument_list|(
name|conduit
argument_list|,
name|outMessage
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// wait for the message to be got from the destination,
comment|// create the thread to handler the Destination incoming message
name|waitForReceiveInMessage
argument_list|()
expr_stmt|;
name|verifyReceivedMessage
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
name|verifyRequestResponseHeaders
argument_list|(
name|inMessage
argument_list|,
name|outMessage
argument_list|)
expr_stmt|;
name|JMSMessageHeadersType
name|inHeader
init|=
operator|(
name|JMSMessageHeadersType
operator|)
name|inMessage
operator|.
name|get
argument_list|(
name|JMSConstants
operator|.
name|JMS_CLIENT_RESPONSE_HEADERS
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The inHeader should not be null"
argument_list|,
name|inHeader
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"The property should not be null "
operator|+
name|inHeader
operator|.
name|getProperty
argument_list|()
argument_list|)
expr_stmt|;
comment|// TODO we need to check the SOAP JMS transport properties here
comment|// wait for a while for the jms session recycling
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|conduit
operator|.
name|close
argument_list|()
expr_stmt|;
name|destination
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsMultiplexCapable
parameter_list|()
throws|throws
name|Exception
block|{
name|inMessage
operator|=
literal|null
expr_stmt|;
name|setupServiceInfo
argument_list|(
literal|"http://cxf.apache.org/hello_world_jms"
argument_list|,
literal|"/wsdl/jms_test.wsdl"
argument_list|,
literal|"HelloWorldService"
argument_list|,
literal|"HelloWorldPort"
argument_list|)
expr_stmt|;
specifier|final
name|JMSDestination
name|destination
init|=
name|setupJMSDestination
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"is multiplex"
argument_list|,
name|destination
operator|instanceof
name|MultiplexDestination
argument_list|)
expr_stmt|;
name|destination
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSecurityContext
parameter_list|()
throws|throws
name|Exception
block|{
name|inMessage
operator|=
literal|null
expr_stmt|;
name|setupServiceInfo
argument_list|(
literal|"http://cxf.apache.org/hello_world_jms"
argument_list|,
literal|"/wsdl/jms_test.wsdl"
argument_list|,
literal|"HelloWorldService"
argument_list|,
literal|"HelloWorldPort"
argument_list|)
expr_stmt|;
specifier|final
name|JMSDestination
name|destination
init|=
name|setupJMSDestination
argument_list|(
literal|true
argument_list|)
decl_stmt|;
comment|// set up the conduit send to be true
name|JMSConduit
name|conduit
init|=
name|setupJMSConduit
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|)
decl_stmt|;
specifier|final
name|Message
name|outMessage
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|setupMessageHeader
argument_list|(
name|outMessage
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|sendoutMessage
argument_list|(
name|conduit
argument_list|,
name|outMessage
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|waitForReceiveDestMessage
argument_list|()
expr_stmt|;
name|SecurityContext
name|securityContext
init|=
name|destMessage
operator|.
name|get
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"SecurityContext should be set in message received by JMSDestination"
argument_list|,
name|securityContext
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Principal in SecurityContext should be"
argument_list|,
literal|"testUser"
argument_list|,
name|securityContext
operator|.
name|getUserPrincipal
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|conduit
operator|.
name|close
argument_list|()
expr_stmt|;
name|destination
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

