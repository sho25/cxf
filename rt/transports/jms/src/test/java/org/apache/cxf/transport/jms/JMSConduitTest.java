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
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
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
name|BytesMessage
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
name|Session
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jms
operator|.
name|core
operator|.
name|JmsTemplate
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
name|core
operator|.
name|SessionCallback
import|;
end_import

begin_class
specifier|public
class|class
name|JMSConduitTest
extends|extends
name|AbstractJMSTester
block|{
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|JMSConduitTest
operator|.
name|class
argument_list|)
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
literal|"tcp://localhost:"
operator|+
name|JMS_PORT
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
comment|// setup the new bus to get the configuration file
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
name|JMSConduit
name|conduit
init|=
name|setupJMSConduit
argument_list|(
literal|false
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Can't get the right ClientReceiveTimeout"
argument_list|,
literal|500L
argument_list|,
name|conduit
operator|.
name|getJmsConfig
argument_list|()
operator|.
name|getReceiveTimeout
argument_list|()
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|bus
operator|.
name|shutdown
argument_list|(
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
name|conduit
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPrepareSend
parameter_list|()
throws|throws
name|Exception
block|{
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
name|JMSConduit
name|conduit
init|=
name|setupJMSConduit
argument_list|(
literal|false
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
try|try
block|{
name|conduit
operator|.
name|prepare
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
name|verifySentMessage
argument_list|(
literal|false
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|verifySentMessage
parameter_list|(
name|boolean
name|send
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
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
decl_stmt|;
name|assertTrue
argument_list|(
literal|"OutputStream should not be null"
argument_list|,
name|os
operator|!=
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/*      * @Test public void testSendOut() throws Exception {      * setupServiceInfo("http://cxf.apache.org/hello_world_jms", "/wsdl/jms_test.wsdl",      * "HelloWorldServiceLoop", "HelloWorldPortLoop"); JMSConduit conduit = setupJMSConduit(true, false);      * conduit.getJmsConfig().setReceiveTimeout(Long.valueOf(10000)); try { for (int c = 0; c< 10; c++) {      * LOG.info("Sending message " + c); inMessage = null; Message message = new MessageImpl();      * sendoutMessage(conduit, message, false); verifyReceivedMessage(message); } } finally { conduit.close();      * } }      */
comment|/**      * Sends several messages and verifies the results. The service sends the message to itself. So it should      * always receive the result      *       * @throws Exception      */
annotation|@
name|Test
specifier|public
name|void
name|testTimeoutOnReceive
parameter_list|()
throws|throws
name|Exception
block|{
name|setupServiceInfo
argument_list|(
literal|"http://cxf.apache.org/hello_world_jms"
argument_list|,
literal|"/wsdl/jms_test.wsdl"
argument_list|,
literal|"HelloWorldServiceLoop"
argument_list|,
literal|"HelloWorldPortLoop"
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
comment|// TODO IF the system is extremely fast. The message could still get through
name|conduit
operator|.
name|getJmsConfig
argument_list|()
operator|.
name|setReceiveTimeout
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
try|try
block|{
name|sendoutMessage
argument_list|(
name|conduit
argument_list|,
name|message
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|verifyReceivedMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Expected a timeout here"
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Received exception. This is expected"
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
block|}
block|}
specifier|private
name|void
name|verifyReceivedMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|InterruptedException
block|{
while|while
condition|(
name|inMessage
operator|==
literal|null
condition|)
block|{
comment|//the send has completed, but the response might not be back yet.
comment|//wait for it.
synchronized|synchronized
init|(
name|this
init|)
block|{
name|wait
argument_list|(
literal|10
argument_list|)
expr_stmt|;
block|}
block|}
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
name|Assert
operator|.
name|assertNotNull
argument_list|(
literal|"The received message input stream should not be null"
argument_list|,
name|bis
argument_list|)
expr_stmt|;
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
literal|"The response data should be equal"
argument_list|,
literal|"HelloWorld"
argument_list|,
name|response
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
name|assertTrue
argument_list|(
literal|"The inMessage JMS Header should not be null"
argument_list|,
name|inHeader
operator|!=
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJMSMessageMarshal
parameter_list|()
throws|throws
name|Exception
block|{
name|setupServiceInfo
argument_list|(
literal|"http://cxf.apache.org/hello_world_jms"
argument_list|,
literal|"/wsdl/jms_test.wsdl"
argument_list|,
literal|"HelloWorldServiceLoop"
argument_list|,
literal|"HelloWorldPortLoop"
argument_list|)
expr_stmt|;
name|String
name|testMsg
init|=
literal|"Test Message"
decl_stmt|;
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
name|msg
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|conduit
operator|.
name|prepare
argument_list|(
name|msg
argument_list|)
expr_stmt|;
specifier|final
name|byte
index|[]
name|testBytes
init|=
name|testMsg
operator|.
name|getBytes
argument_list|(
name|Charset
operator|.
name|defaultCharset
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
decl_stmt|;
comment|// TODO encoding
name|JMSConfiguration
name|jmsConfig
init|=
name|conduit
operator|.
name|getJmsConfig
argument_list|()
decl_stmt|;
name|JmsTemplate
name|jmsTemplate
init|=
operator|new
name|JmsTemplate
argument_list|()
decl_stmt|;
name|jmsTemplate
operator|.
name|setConnectionFactory
argument_list|(
name|jmsConfig
operator|.
name|getOrCreateWrappedConnectionFactory
argument_list|()
argument_list|)
expr_stmt|;
name|SessionCallback
argument_list|<
name|javax
operator|.
name|jms
operator|.
name|Message
argument_list|>
name|sc
init|=
operator|new
name|SessionCallback
argument_list|<
name|javax
operator|.
name|jms
operator|.
name|Message
argument_list|>
argument_list|()
block|{
specifier|public
name|javax
operator|.
name|jms
operator|.
name|Message
name|doInJms
parameter_list|(
name|Session
name|session
parameter_list|)
throws|throws
name|JMSException
block|{
return|return
name|JMSUtils
operator|.
name|createAndSetPayload
argument_list|(
name|testBytes
argument_list|,
name|session
argument_list|,
name|JMSConstants
operator|.
name|BYTE_MESSAGE_TYPE
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|javax
operator|.
name|jms
operator|.
name|Message
name|message
init|=
name|jmsTemplate
operator|.
name|execute
argument_list|(
name|sc
argument_list|)
decl_stmt|;
comment|// The ibm jdk finalizes conduit (during most runs of this test) and
comment|// causes it to fail unless we reference the conduit here after the
comment|// jmsTemplate.execute() call.
name|assertNotNull
argument_list|(
literal|"Conduit is null"
argument_list|,
name|conduit
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Message should have been of type BytesMessage "
argument_list|,
name|message
operator|instanceof
name|BytesMessage
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

