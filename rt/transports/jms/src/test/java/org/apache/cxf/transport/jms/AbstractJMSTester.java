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
name|OutputStream
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
name|Writer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|atomic
operator|.
name|AtomicReference
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
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
name|DeliveryMode
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|broker
operator|.
name|BrokerService
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
name|store
operator|.
name|memory
operator|.
name|MemoryPersistenceAdapter
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
name|service
operator|.
name|Service
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
name|testutil
operator|.
name|common
operator|.
name|TestUtil
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
name|wsdl11
operator|.
name|WSDLServiceFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractJMSTester
block|{
specifier|protected
specifier|static
specifier|final
name|String
name|WSDL
init|=
literal|"/jms_test.wsdl"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|SERVICE_NS
init|=
literal|"http://cxf.apache.org/hello_world_jms"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|int
name|MAX_RECEIVE_TIME
init|=
literal|10
decl_stmt|;
specifier|protected
specifier|static
name|Bus
name|bus
decl_stmt|;
specifier|protected
specifier|static
name|ActiveMQConnectionFactory
name|cf1
decl_stmt|;
specifier|protected
specifier|static
name|ConnectionFactory
name|cf
decl_stmt|;
specifier|protected
specifier|static
name|BrokerService
name|broker
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|MESSAGE_CONTENT
init|=
literal|"HelloWorld"
decl_stmt|;
specifier|protected
enum|enum
name|ExchangePattern
block|{
name|oneway
block|,
name|requestReply
block|}
empty_stmt|;
specifier|private
specifier|final
name|AtomicReference
argument_list|<
name|Message
argument_list|>
name|inMessage
init|=
operator|new
name|AtomicReference
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|AtomicReference
argument_list|<
name|Message
argument_list|>
name|destMessage
init|=
operator|new
name|AtomicReference
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|startSerices
parameter_list|()
throws|throws
name|Exception
block|{
name|broker
operator|=
operator|new
name|BrokerService
argument_list|()
expr_stmt|;
name|broker
operator|.
name|setPersistent
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|broker
operator|.
name|setPopulateJMSXUserID
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|broker
operator|.
name|setUseAuthenticatedPrincipalForJMSXUserID
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|broker
operator|.
name|setUseJmx
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|broker
operator|.
name|setPersistenceAdapter
argument_list|(
operator|new
name|MemoryPersistenceAdapter
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|brokerUri
init|=
literal|"tcp://localhost:"
operator|+
name|TestUtil
operator|.
name|getNewPortNumber
argument_list|(
name|AbstractJMSTester
operator|.
name|class
argument_list|)
decl_stmt|;
name|broker
operator|.
name|addConnector
argument_list|(
name|brokerUri
argument_list|)
expr_stmt|;
name|broker
operator|.
name|start
argument_list|()
expr_stmt|;
name|bus
operator|=
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
expr_stmt|;
name|cf1
operator|=
operator|new
name|ActiveMQConnectionFactory
argument_list|(
name|brokerUri
argument_list|)
expr_stmt|;
name|cf
operator|=
name|cf1
expr_stmt|;
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|stopServices
parameter_list|()
throws|throws
name|Exception
block|{
name|bus
operator|.
name|shutdown
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|broker
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
specifier|protected
specifier|static
name|EndpointInfo
name|setupServiceInfo
parameter_list|(
name|String
name|serviceName
parameter_list|,
name|String
name|portName
parameter_list|)
block|{
return|return
name|setupServiceInfo
argument_list|(
name|SERVICE_NS
argument_list|,
name|WSDL
argument_list|,
name|serviceName
argument_list|,
name|portName
argument_list|)
return|;
block|}
specifier|protected
specifier|static
name|EndpointInfo
name|setupServiceInfo
parameter_list|(
name|String
name|ns
parameter_list|,
name|String
name|wsdl
parameter_list|,
name|String
name|serviceName
parameter_list|,
name|String
name|portName
parameter_list|)
block|{
name|URL
name|wsdlUrl
init|=
name|AbstractJMSTester
operator|.
name|class
operator|.
name|getResource
argument_list|(
name|wsdl
argument_list|)
decl_stmt|;
if|if
condition|(
name|wsdlUrl
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Wsdl file not found on class path "
operator|+
name|wsdl
argument_list|)
throw|;
block|}
name|WSDLServiceFactory
name|factory
init|=
operator|new
name|WSDLServiceFactory
argument_list|(
name|bus
argument_list|,
name|wsdlUrl
operator|.
name|toExternalForm
argument_list|()
argument_list|,
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
name|serviceName
argument_list|)
argument_list|)
decl_stmt|;
name|Service
name|service
init|=
name|factory
operator|.
name|create
argument_list|()
decl_stmt|;
return|return
name|service
operator|.
name|getEndpointInfo
argument_list|(
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
name|portName
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|MessageObserver
name|createMessageObserver
parameter_list|()
block|{
return|return
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
comment|//                Exchange exchange = new ExchangeImpl();
comment|//                exchange.setInMessage(m);
comment|//                m.setExchange(exchange);
name|destMessage
operator|.
name|set
argument_list|(
name|m
argument_list|)
expr_stmt|;
synchronized|synchronized
init|(
name|destMessage
init|)
block|{
name|destMessage
operator|.
name|notifyAll
argument_list|()
expr_stmt|;
block|}
block|}
block|}
return|;
block|}
specifier|protected
specifier|static
name|void
name|sendMessageAsync
parameter_list|(
name|Conduit
name|conduit
parameter_list|,
name|Message
name|message
parameter_list|)
throws|throws
name|IOException
block|{
name|sendoutMessage
argument_list|(
name|conduit
argument_list|,
name|message
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|static
name|void
name|sendMessageSync
parameter_list|(
name|Conduit
name|conduit
parameter_list|,
name|Message
name|message
parameter_list|)
throws|throws
name|IOException
block|{
name|sendoutMessage
argument_list|(
name|conduit
argument_list|,
name|message
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|static
name|void
name|sendMessage
parameter_list|(
name|Conduit
name|conduit
parameter_list|,
name|Message
name|message
parameter_list|,
name|boolean
name|synchronous
parameter_list|)
throws|throws
name|IOException
block|{
name|sendoutMessage
argument_list|(
name|conduit
argument_list|,
name|message
argument_list|,
literal|false
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|static
name|void
name|sendOneWayMessage
parameter_list|(
name|Conduit
name|conduit
parameter_list|,
name|Message
name|message
parameter_list|)
throws|throws
name|IOException
block|{
name|sendoutMessage
argument_list|(
name|conduit
argument_list|,
name|message
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|sendoutMessage
parameter_list|(
name|Conduit
name|conduit
parameter_list|,
name|Message
name|message
parameter_list|,
name|boolean
name|isOneWay
parameter_list|,
name|boolean
name|synchronous
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|Exchange
name|exchange
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setOneWay
argument_list|(
name|isOneWay
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setSynchronous
argument_list|(
name|synchronous
argument_list|)
expr_stmt|;
name|message
operator|.
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setOutMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|conduit
operator|.
name|prepare
argument_list|(
name|message
argument_list|)
expr_stmt|;
try|try
init|(
name|OutputStream
name|os
init|=
name|message
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
init|)
block|{
if|if
condition|(
name|os
operator|!=
literal|null
condition|)
block|{
name|os
operator|.
name|write
argument_list|(
name|MESSAGE_CONTENT
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
comment|// TODO encoding
return|return;
block|}
block|}
try|try
init|(
name|Writer
name|writer
init|=
name|message
operator|.
name|getContent
argument_list|(
name|Writer
operator|.
name|class
argument_list|)
init|)
block|{
if|if
condition|(
name|writer
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|write
argument_list|(
name|MESSAGE_CONTENT
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
name|fail
argument_list|(
literal|"The OutputStream and Writer should not both be null"
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|static
name|JMSConduit
name|setupJMSConduit
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|)
throws|throws
name|IOException
block|{
name|JMSConfiguration
name|jmsConfig
init|=
name|JMSConfigFactory
operator|.
name|createFromEndpointInfo
argument_list|(
name|bus
argument_list|,
name|ei
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|jmsConfig
operator|.
name|setConnectionFactory
argument_list|(
name|cf
argument_list|)
expr_stmt|;
return|return
operator|new
name|JMSConduit
argument_list|(
literal|null
argument_list|,
name|jmsConfig
argument_list|,
name|bus
argument_list|)
return|;
block|}
specifier|protected
name|JMSConduit
name|setupJMSConduitWithObserver
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|)
throws|throws
name|IOException
block|{
name|JMSConduit
name|jmsConduit
init|=
name|setupJMSConduit
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
name|inMessage
operator|.
name|set
argument_list|(
name|m
argument_list|)
expr_stmt|;
synchronized|synchronized
init|(
name|inMessage
init|)
block|{
name|inMessage
operator|.
name|notifyAll
argument_list|()
expr_stmt|;
block|}
block|}
block|}
decl_stmt|;
name|jmsConduit
operator|.
name|setMessageObserver
argument_list|(
name|observer
argument_list|)
expr_stmt|;
return|return
name|jmsConduit
return|;
block|}
specifier|protected
name|JMSDestination
name|setupJMSDestination
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|,
name|Function
argument_list|<
name|ConnectionFactory
argument_list|,
name|ConnectionFactory
argument_list|>
name|wrapper
parameter_list|)
throws|throws
name|IOException
block|{
name|JMSConfiguration
name|jmsConfig
init|=
name|JMSConfigFactory
operator|.
name|createFromEndpointInfo
argument_list|(
name|bus
argument_list|,
name|ei
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|jmsConfig
operator|.
name|setConnectionFactory
argument_list|(
name|wrapper
operator|.
name|apply
argument_list|(
name|cf
argument_list|)
argument_list|)
expr_stmt|;
return|return
operator|new
name|JMSDestination
argument_list|(
name|bus
argument_list|,
name|ei
argument_list|,
name|jmsConfig
argument_list|)
return|;
block|}
specifier|protected
name|JMSDestination
name|setupJMSDestination
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|)
throws|throws
name|IOException
block|{
name|JMSConfiguration
name|jmsConfig
init|=
name|JMSConfigFactory
operator|.
name|createFromEndpointInfo
argument_list|(
name|bus
argument_list|,
name|ei
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|jmsConfig
operator|.
name|setConnectionFactory
argument_list|(
name|cf
argument_list|)
expr_stmt|;
return|return
operator|new
name|JMSDestination
argument_list|(
name|bus
argument_list|,
name|ei
argument_list|,
name|jmsConfig
argument_list|)
return|;
block|}
specifier|protected
specifier|static
name|Message
name|createMessage
parameter_list|()
block|{
return|return
name|createMessage
argument_list|(
literal|null
argument_list|)
return|;
block|}
specifier|protected
specifier|static
name|Message
name|createMessage
parameter_list|(
name|String
name|correlationId
parameter_list|)
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
literal|1000L
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
specifier|static
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
literal|"<not found>"
decl_stmt|;
name|InputStream
name|bis
init|=
name|message
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|bis
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|byte
index|[]
name|bytes
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
name|bis
operator|.
name|read
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
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
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Read the Destination recieved Message error: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|Reader
name|reader
init|=
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
index|[]
name|buffer
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
name|fail
argument_list|(
literal|"Read the Destination recieved Message error: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|assertEquals
argument_list|(
literal|"The response content should be equal"
argument_list|,
name|MESSAGE_CONTENT
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|static
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
specifier|protected
specifier|static
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
specifier|protected
name|Message
name|waitForReceiveInMessage
parameter_list|()
throws|throws
name|InterruptedException
block|{
if|if
condition|(
literal|null
operator|==
name|inMessage
operator|.
name|get
argument_list|()
condition|)
block|{
synchronized|synchronized
init|(
name|inMessage
init|)
block|{
name|inMessage
operator|.
name|wait
argument_list|(
name|MAX_RECEIVE_TIME
operator|*
literal|1000L
argument_list|)
expr_stmt|;
block|}
name|assertNotNull
argument_list|(
literal|"Can't receive the Conduit Message in "
operator|+
name|MAX_RECEIVE_TIME
operator|+
literal|" seconds"
argument_list|,
name|inMessage
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|inMessage
operator|.
name|getAndSet
argument_list|(
literal|null
argument_list|)
return|;
block|}
specifier|protected
name|Message
name|waitForReceiveDestMessage
parameter_list|()
throws|throws
name|InterruptedException
block|{
if|if
condition|(
literal|null
operator|==
name|destMessage
operator|.
name|get
argument_list|()
condition|)
block|{
synchronized|synchronized
init|(
name|destMessage
init|)
block|{
name|destMessage
operator|.
name|wait
argument_list|(
name|MAX_RECEIVE_TIME
operator|*
literal|1000L
argument_list|)
expr_stmt|;
block|}
name|assertNotNull
argument_list|(
literal|"Can't receive the Destination message in "
operator|+
name|MAX_RECEIVE_TIME
operator|+
literal|" seconds"
argument_list|,
name|destMessage
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|destMessage
operator|.
name|getAndSet
argument_list|(
literal|null
argument_list|)
return|;
block|}
block|}
end_class

end_unit

