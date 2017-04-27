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
name|java
operator|.
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
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
name|Queue
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
name|service
operator|.
name|model
operator|.
name|EndpointInfo
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
name|Ignore
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
annotation|@
name|Test
specifier|public
name|void
name|testGetConfigurationFromWSDL
parameter_list|()
throws|throws
name|Exception
block|{
name|EndpointInfo
name|ei
init|=
name|setupServiceInfo
argument_list|(
literal|"HelloWorldQueueBinMsgService"
argument_list|,
literal|"HelloWorldQueueBinMsgPort"
argument_list|)
decl_stmt|;
name|JMSDestination
name|destination
init|=
name|setupJMSDestination
argument_list|(
name|ei
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Can't get the right AddressPolicy's Destination"
argument_list|,
literal|"test.jmstransport.binary"
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
name|testDurableSubscriber
parameter_list|()
throws|throws
name|Exception
block|{
name|destMessage
operator|=
literal|null
expr_stmt|;
name|EndpointInfo
name|ei
init|=
name|setupServiceInfo
argument_list|(
literal|"HelloWorldPubSubService"
argument_list|,
literal|"HelloWorldPubSubPort"
argument_list|)
decl_stmt|;
name|JMSConduit
name|conduit
init|=
name|setupJMSConduitWithObserver
argument_list|(
name|ei
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
name|ei
argument_list|)
decl_stmt|;
name|destination
operator|.
name|setMessageObserver
argument_list|(
name|createMessageObserver
argument_list|()
argument_list|)
expr_stmt|;
comment|// The JMSBroker (ActiveMQ 5.x) need to take some time to setup the DurableSubscriber
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|sendOneWayMessage
argument_list|(
name|conduit
argument_list|,
name|outMessage
argument_list|)
expr_stmt|;
name|waitForReceiveDestMessage
argument_list|()
expr_stmt|;
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
name|EndpointInfo
name|ei
init|=
name|setupServiceInfo
argument_list|(
literal|"HWStaticReplyQBinMsgService"
argument_list|,
literal|"HWStaticReplyQBinMsgPort"
argument_list|)
decl_stmt|;
name|JMSDestination
name|destination
init|=
name|setupJMSDestination
argument_list|(
name|ei
argument_list|)
decl_stmt|;
name|destination
operator|.
name|setMessageObserver
argument_list|(
name|createMessageObserver
argument_list|()
argument_list|)
expr_stmt|;
name|JMSConduit
name|conduit
init|=
name|setupJMSConduitWithObserver
argument_list|(
name|ei
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
name|sendOneWayMessage
argument_list|(
name|conduit
argument_list|,
name|outMessage
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
parameter_list|,
name|String
name|replyTo
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
name|header
operator|.
name|setJMSReplyTo
argument_list|(
name|replyTo
operator|!=
literal|null
condition|?
name|replyTo
else|:
literal|null
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
argument_list|,
literal|null
argument_list|)
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
name|setupMessageHeader
argument_list|(
name|outMessage
argument_list|,
name|correlationId
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|verifyReceivedMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|ByteArrayInputStream
name|bis
init|=
operator|(
name|ByteArrayInputStream
operator|)
name|message
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|response
init|=
literal|"<not found>"
decl_stmt|;
if|if
condition|(
name|bis
operator|!=
literal|null
condition|)
block|{
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
name|response
operator|=
name|IOUtils
operator|.
name|newStringFromBytes
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|StringReader
name|reader
init|=
operator|(
name|StringReader
operator|)
name|message
operator|.
name|getContent
argument_list|(
name|Reader
operator|.
name|class
argument_list|)
decl_stmt|;
name|char
name|buffer
index|[]
init|=
operator|new
name|char
index|[
literal|5000
index|]
decl_stmt|;
try|try
block|{
name|int
name|i
init|=
name|reader
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
name|response
operator|=
operator|new
name|String
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|assertFalse
argument_list|(
literal|"Read the Destination recieved Message error "
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
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
name|msgIn
parameter_list|,
name|Message
name|msgOut
parameter_list|)
block|{
name|JMSMessageHeadersType
name|outHeader
init|=
operator|(
name|JMSMessageHeadersType
operator|)
name|msgOut
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
name|msgIn
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
name|msgOut
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
name|msgIn
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
name|msgIn
parameter_list|,
name|Message
name|msgOut
parameter_list|)
block|{
name|JMSMessageHeadersType
name|outHeader
init|=
operator|(
name|JMSMessageHeadersType
operator|)
name|msgOut
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
name|msgIn
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
name|Message
name|msg
init|=
name|testRoundTripDestination
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|SecurityContext
name|securityContext
init|=
name|msg
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRoundTripDestinationDoNotCreateSecurityContext
parameter_list|()
throws|throws
name|Exception
block|{
name|Message
name|msg
init|=
name|testRoundTripDestination
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|SecurityContext
name|securityContext
init|=
name|msg
operator|.
name|get
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"SecurityContext should not be set in message received by JMSDestination"
argument_list|,
name|securityContext
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Message
name|testRoundTripDestination
parameter_list|(
name|boolean
name|createSecurityContext
parameter_list|)
throws|throws
name|Exception
block|{
name|EndpointInfo
name|ei
init|=
name|setupServiceInfo
argument_list|(
literal|"HelloWorldService"
argument_list|,
literal|"HelloWorldPort"
argument_list|)
decl_stmt|;
name|JMSConduit
name|conduit
init|=
name|setupJMSConduitWithObserver
argument_list|(
name|ei
argument_list|)
decl_stmt|;
name|conduit
operator|.
name|getJmsConfig
argument_list|()
operator|.
name|setCreateSecurityContext
argument_list|(
name|createSecurityContext
argument_list|)
expr_stmt|;
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
name|ei
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
name|sendOneWayMessage
argument_list|(
name|backConduit
argument_list|,
name|replyMessage
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
name|sendMessageSync
argument_list|(
name|conduit
argument_list|,
name|outMessage
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
name|sendMessageSync
argument_list|(
name|conduit
argument_list|,
name|outMessage
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
return|return
name|inMessage
return|;
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
name|EndpointInfo
name|ei
init|=
name|setupServiceInfo
argument_list|(
literal|"HelloWorldService"
argument_list|,
literal|"HelloWorldPort"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|customPropertyName
init|=
literal|"THIS_PROPERTY_WILL_NOT_BE_AUTO_COPIED"
decl_stmt|;
comment|// set up the conduit send to be true
name|JMSConduit
name|conduit
init|=
name|setupJMSConduitWithObserver
argument_list|(
name|ei
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
name|putProperty
argument_list|(
name|customPropertyName
argument_list|,
name|customPropertyName
argument_list|)
expr_stmt|;
specifier|final
name|JMSDestination
name|destination
init|=
name|setupJMSDestination
argument_list|(
name|ei
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
name|sendOneWayMessage
argument_list|(
name|backConduit
argument_list|,
name|replyMessage
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
name|sendMessageSync
argument_list|(
name|conduit
argument_list|,
name|outMessage
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
name|EndpointInfo
name|ei
init|=
name|setupServiceInfo
argument_list|(
literal|"HelloWorldService"
argument_list|,
literal|"HelloWorldPort"
argument_list|)
decl_stmt|;
specifier|final
name|JMSDestination
name|destination
init|=
name|setupJMSDestination
argument_list|(
name|ei
argument_list|)
decl_stmt|;
name|destination
operator|.
name|setMessageObserver
argument_list|(
name|createMessageObserver
argument_list|()
argument_list|)
expr_stmt|;
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
name|SecurityContext
name|ctx
init|=
name|testSecurityContext
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"SecurityContext should be set in message received by JMSDestination"
argument_list|,
name|ctx
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Principal in SecurityContext should be"
argument_list|,
literal|"testUser"
argument_list|,
name|ctx
operator|.
name|getUserPrincipal
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDoNotCreateSecurityContext
parameter_list|()
throws|throws
name|Exception
block|{
name|SecurityContext
name|ctx
init|=
name|testSecurityContext
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"SecurityContext should not be set in message received by JMSDestination"
argument_list|,
name|ctx
argument_list|)
expr_stmt|;
block|}
specifier|private
name|SecurityContext
name|testSecurityContext
parameter_list|(
name|boolean
name|createSecurityContext
parameter_list|)
throws|throws
name|Exception
block|{
name|EndpointInfo
name|ei
init|=
name|setupServiceInfo
argument_list|(
literal|"HelloWorldService"
argument_list|,
literal|"HelloWorldPort"
argument_list|)
decl_stmt|;
specifier|final
name|JMSDestination
name|destination
init|=
name|setupJMSDestination
argument_list|(
name|ei
argument_list|)
decl_stmt|;
name|destination
operator|.
name|getJmsConfig
argument_list|()
operator|.
name|setCreateSecurityContext
argument_list|(
name|createSecurityContext
argument_list|)
expr_stmt|;
name|destination
operator|.
name|setMessageObserver
argument_list|(
name|createMessageObserver
argument_list|()
argument_list|)
expr_stmt|;
comment|// set up the conduit send to be true
name|JMSConduit
name|conduit
init|=
name|setupJMSConduitWithObserver
argument_list|(
name|ei
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
name|sendOneWayMessage
argument_list|(
name|conduit
argument_list|,
name|outMessage
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
return|return
name|securityContext
return|;
block|}
annotation|@
name|Test
annotation|@
name|Ignore
specifier|public
name|void
name|testOneWayReplyToSetUnset
parameter_list|()
throws|throws
name|Exception
block|{
comment|/* 1. Test that replyTo destination set in WSDL is NOT used          * in spec compliant mode */
name|destMessage
operator|=
literal|null
expr_stmt|;
name|EndpointInfo
name|ei
init|=
name|setupServiceInfo
argument_list|(
literal|"HWStaticReplyQBinMsgService"
argument_list|,
literal|"HWStaticReplyQBinMsgPort"
argument_list|)
decl_stmt|;
name|JMSConduit
name|conduit
init|=
name|setupJMSConduitWithObserver
argument_list|(
name|ei
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
name|ei
argument_list|)
decl_stmt|;
name|destination
operator|.
name|setMessageObserver
argument_list|(
name|createMessageObserver
argument_list|()
argument_list|)
expr_stmt|;
name|sendOneWayMessage
argument_list|(
name|conduit
argument_list|,
name|outMessage
argument_list|)
expr_stmt|;
name|waitForReceiveDestMessage
argument_list|()
expr_stmt|;
comment|// just verify the Destination inMessage
name|assertTrue
argument_list|(
literal|"The destination should have got the message "
argument_list|,
name|destMessage
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|verifyReplyToNotSet
argument_list|(
name|destMessage
argument_list|)
expr_stmt|;
name|destMessage
operator|=
literal|null
expr_stmt|;
comment|/* 2. Test that replyTo destination set in WSDL IS used          * in spec non-compliant mode */
name|sendOneWayMessage
argument_list|(
name|conduit
argument_list|,
name|outMessage
argument_list|)
expr_stmt|;
name|waitForReceiveDestMessage
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"The destination should have got the message "
argument_list|,
name|destMessage
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|String
name|exName
init|=
name|getQueueName
argument_list|(
name|conduit
operator|.
name|getJmsConfig
argument_list|()
operator|.
name|getReplyDestination
argument_list|()
argument_list|)
decl_stmt|;
name|verifyReplyToSet
argument_list|(
name|destMessage
argument_list|,
name|Queue
operator|.
name|class
argument_list|,
name|exName
argument_list|)
expr_stmt|;
name|destMessage
operator|=
literal|null
expr_stmt|;
comment|/* 3. Test that replyTo destination provided via invocation context          * overrides the value set in WSDL and IS used in spec non-compliant mode */
name|String
name|contextReplyTo
init|=
name|conduit
operator|.
name|getJmsConfig
argument_list|()
operator|.
name|getReplyDestination
argument_list|()
operator|+
literal|".context"
decl_stmt|;
name|exName
operator|+=
literal|".context"
expr_stmt|;
name|setupMessageHeader
argument_list|(
name|outMessage
argument_list|,
literal|"cidValue"
argument_list|,
name|contextReplyTo
argument_list|)
expr_stmt|;
name|sendOneWayMessage
argument_list|(
name|conduit
argument_list|,
name|outMessage
argument_list|)
expr_stmt|;
name|waitForReceiveDestMessage
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"The destiantion should have got the message "
argument_list|,
name|destMessage
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|verifyReplyToSet
argument_list|(
name|destMessage
argument_list|,
name|Queue
operator|.
name|class
argument_list|,
name|exName
argument_list|)
expr_stmt|;
name|destMessage
operator|=
literal|null
expr_stmt|;
comment|/* 4. Test that replyTo destination provided via invocation context          * and the value set in WSDL are NOT used in spec non-compliant mode          * when JMSConstants.JMS_SET_REPLY_TO == false */
name|setupMessageHeader
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
name|outMessage
operator|.
name|put
argument_list|(
name|JMSConstants
operator|.
name|JMS_SET_REPLY_TO
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|sendOneWayMessage
argument_list|(
name|conduit
argument_list|,
name|outMessage
argument_list|)
expr_stmt|;
name|waitForReceiveDestMessage
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"The destiantion should have got the message "
argument_list|,
name|destMessage
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|verifyReplyToNotSet
argument_list|(
name|destMessage
argument_list|)
expr_stmt|;
name|destMessage
operator|=
literal|null
expr_stmt|;
comment|/* 5. Test that replyTo destination set in WSDL IS used in spec non-compliant          * mode when JMSConstants.JMS_SET_REPLY_TO == true */
name|setupMessageHeader
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
name|outMessage
operator|.
name|put
argument_list|(
name|JMSConstants
operator|.
name|JMS_SET_REPLY_TO
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|sendOneWayMessage
argument_list|(
name|conduit
argument_list|,
name|outMessage
argument_list|)
expr_stmt|;
name|waitForReceiveDestMessage
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"The destiantion should have got the message "
argument_list|,
name|destMessage
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|exName
operator|=
name|getQueueName
argument_list|(
name|conduit
operator|.
name|getJmsConfig
argument_list|()
operator|.
name|getReplyDestination
argument_list|()
argument_list|)
expr_stmt|;
name|verifyReplyToSet
argument_list|(
name|destMessage
argument_list|,
name|Queue
operator|.
name|class
argument_list|,
name|exName
argument_list|)
expr_stmt|;
name|destMessage
operator|=
literal|null
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
name|String
name|getQueueName
parameter_list|(
name|String
name|exName
parameter_list|)
block|{
if|if
condition|(
name|exName
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|(
name|exName
operator|.
name|indexOf
argument_list|(
literal|'/'
argument_list|)
operator|!=
operator|-
literal|1
operator|&&
name|exName
operator|.
name|indexOf
argument_list|(
literal|'/'
argument_list|)
operator|<
name|exName
operator|.
name|length
argument_list|()
operator|)
condition|?
name|exName
operator|.
name|substring
argument_list|(
name|exName
operator|.
name|indexOf
argument_list|(
literal|'/'
argument_list|)
operator|+
literal|1
argument_list|)
else|:
name|exName
return|;
block|}
specifier|protected
name|void
name|verifyReplyToNotSet
parameter_list|(
name|Message
name|cxfMsg
parameter_list|)
block|{
name|javax
operator|.
name|jms
operator|.
name|Message
name|jmsMsg
init|=
name|javax
operator|.
name|jms
operator|.
name|Message
operator|.
name|class
operator|.
name|cast
argument_list|(
name|cxfMsg
operator|.
name|get
argument_list|(
name|JMSConstants
operator|.
name|JMS_REQUEST_MESSAGE
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"JMS Messsage must be null"
argument_list|,
name|jmsMsg
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|getDestinationName
parameter_list|(
name|Destination
name|dest
parameter_list|)
throws|throws
name|JMSException
block|{
if|if
condition|(
name|dest
operator|instanceof
name|Queue
condition|)
block|{
return|return
operator|(
operator|(
name|Queue
operator|)
name|dest
operator|)
operator|.
name|getQueueName
argument_list|()
return|;
block|}
else|else
block|{
return|return
operator|(
operator|(
name|Topic
operator|)
name|dest
operator|)
operator|.
name|getTopicName
argument_list|()
return|;
block|}
block|}
specifier|protected
name|void
name|verifyReplyToSet
parameter_list|(
name|Message
name|cxfMsg
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|Destination
argument_list|>
name|type
parameter_list|,
name|String
name|expectedName
parameter_list|)
throws|throws
name|Exception
block|{
name|javax
operator|.
name|jms
operator|.
name|Message
name|jmsMsg
init|=
name|javax
operator|.
name|jms
operator|.
name|Message
operator|.
name|class
operator|.
name|cast
argument_list|(
name|cxfMsg
operator|.
name|get
argument_list|(
name|JMSConstants
operator|.
name|JMS_REQUEST_MESSAGE
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"JMS Messsage must not be null"
argument_list|,
name|jmsMsg
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"JMS Messsage's replyTo must not be null"
argument_list|,
name|jmsMsg
operator|.
name|getJMSReplyTo
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"JMS Messsage's replyTo type must be of type "
operator|+
name|type
operator|.
name|getName
argument_list|()
argument_list|,
name|type
operator|.
name|isAssignableFrom
argument_list|(
name|jmsMsg
operator|.
name|getJMSReplyTo
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|receivedName
init|=
name|getDestinationName
argument_list|(
name|jmsMsg
operator|.
name|getJMSReplyTo
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"JMS Messsage's replyTo must be named "
operator|+
name|expectedName
operator|+
literal|" but was "
operator|+
name|receivedName
argument_list|,
name|expectedName
operator|==
name|receivedName
operator|||
name|receivedName
operator|.
name|equals
argument_list|(
name|expectedName
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

