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
name|IOException
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
name|RequestResponseTest
extends|extends
name|AbstractJMSTester
block|{
specifier|private
name|void
name|verifyReceivedMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|String
name|response
init|=
name|getContent
argument_list|(
name|message
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
name|testRequestQueueResponseTempQueue
parameter_list|()
throws|throws
name|Exception
block|{
name|EndpointInfo
name|ei
init|=
name|setupServiceInfo
argument_list|(
literal|"http://cxf.apache.org/jms_simple"
argument_list|,
literal|"/wsdl/jms_spec_testsuite.wsdl"
argument_list|,
literal|"JMSSimpleService002X"
argument_list|,
literal|"SimplePortQueueRequest"
argument_list|)
decl_stmt|;
name|sendAndReceiveMessages
argument_list|(
name|ei
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|sendAndReceiveMessages
argument_list|(
name|ei
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRequestQueueResponseStaticQueue
parameter_list|()
throws|throws
name|Exception
block|{
name|EndpointInfo
name|ei
init|=
name|setupServiceInfo
argument_list|(
literal|"http://cxf.apache.org/jms_simple"
argument_list|,
literal|"/wsdl/jms_spec_testsuite.wsdl"
argument_list|,
literal|"JMSSimpleService002X"
argument_list|,
literal|"SimplePortQueueRequestQueueResponse"
argument_list|)
decl_stmt|;
name|sendAndReceiveMessages
argument_list|(
name|ei
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|sendAndReceiveMessages
argument_list|(
name|ei
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRequestTopicResponseTempQueue
parameter_list|()
throws|throws
name|Exception
block|{
name|EndpointInfo
name|ei
init|=
name|setupServiceInfo
argument_list|(
literal|"http://cxf.apache.org/jms_simple"
argument_list|,
literal|"/wsdl/jms_spec_testsuite.wsdl"
argument_list|,
literal|"JMSSimpleService002X"
argument_list|,
literal|"SimplePortTopicRequest"
argument_list|)
decl_stmt|;
name|sendAndReceiveMessages
argument_list|(
name|ei
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Ignore
specifier|public
name|void
name|testRequestTopicResponseStaticQueue
parameter_list|()
throws|throws
name|Exception
block|{
name|EndpointInfo
name|ei
init|=
name|setupServiceInfo
argument_list|(
literal|"http://cxf.apache.org/jms_simple"
argument_list|,
literal|"/wsdl/jms_spec_testsuite.wsdl"
argument_list|,
literal|"JMSSimpleService002X"
argument_list|,
literal|"SimplePortTopicRequestQueueResponse"
argument_list|)
decl_stmt|;
name|sendAndReceiveMessages
argument_list|(
name|ei
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|sendAndReceiveMessages
argument_list|(
name|ei
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Message
name|createMessage
parameter_list|()
block|{
name|Message
name|outMessage
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|JMSMessageHeadersType
name|header
init|=
operator|new
name|JMSMessageHeadersType
argument_list|()
decl_stmt|;
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
return|return
name|outMessage
return|;
block|}
specifier|protected
name|void
name|sendAndReceiveMessages
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|,
name|boolean
name|synchronous
parameter_list|)
throws|throws
name|IOException
block|{
name|inMessage
operator|=
literal|null
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
name|createMessage
argument_list|()
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
try|try
block|{
name|sendMessage
argument_list|(
name|conduit
argument_list|,
name|outMessage
argument_list|,
name|synchronous
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
block|}
finally|finally
block|{
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
block|}
end_class

end_unit

