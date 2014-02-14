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
name|jms
operator|.
name|testsuite
operator|.
name|testcases
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|UndeclaredThrowableException
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
name|Map
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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|BindingProvider
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|soap
operator|.
name|SOAPFaultException
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
name|jaxws
operator|.
name|JaxWsProxyFactoryBean
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
name|jms_greeter
operator|.
name|JMSGreeterPortType
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
name|jms_greeter
operator|.
name|JMSGreeterService
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
name|jms_greeter
operator|.
name|JMSGreeterService2
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
name|systest
operator|.
name|jms
operator|.
name|Hello
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
name|systest
operator|.
name|jms
operator|.
name|Server
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
name|AbstractBusClientServerTestBase
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
name|EmbeddedJMSBrokerLauncher
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
name|transport
operator|.
name|jms
operator|.
name|JMSConstants
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
name|JMSMessageHeadersType
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
name|JMSOldConfigHolder
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
name|JNDIConfiguration
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
name|spec
operator|.
name|JMSSpecConstants
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
name|uri
operator|.
name|JMSEndpoint
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
name|transport
operator|.
name|jms
operator|.
name|util
operator|.
name|TestReceiver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_doc_lit
operator|.
name|Greeter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_doc_lit
operator|.
name|SOAPService7
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
name|SoapJmsSpecTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|private
specifier|static
name|EmbeddedJMSBrokerLauncher
name|broker
decl_stmt|;
specifier|private
name|String
name|wsdlString
decl_stmt|;
specifier|public
name|URL
name|getWSDLURL
parameter_list|(
name|String
name|s
parameter_list|)
throws|throws
name|Exception
block|{
name|URL
name|u
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
name|s
argument_list|)
decl_stmt|;
if|if
condition|(
name|u
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"WSDL classpath resource not found "
operator|+
name|s
argument_list|)
throw|;
block|}
name|wsdlString
operator|=
name|u
operator|.
name|toString
argument_list|()
operator|.
name|intern
argument_list|()
expr_stmt|;
name|broker
operator|.
name|updateWsdl
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|wsdlString
argument_list|)
expr_stmt|;
return|return
name|u
return|;
block|}
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|startServers
parameter_list|()
throws|throws
name|Exception
block|{
name|broker
operator|=
operator|new
name|EmbeddedJMSBrokerLauncher
argument_list|()
expr_stmt|;
name|launchServer
argument_list|(
name|broker
argument_list|)
expr_stmt|;
name|launchServer
argument_list|(
operator|new
name|Server
argument_list|(
name|broker
argument_list|)
argument_list|)
expr_stmt|;
name|createStaticBus
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSpecJMS
parameter_list|()
throws|throws
name|Exception
block|{
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/jms_greeter"
argument_list|,
literal|"JMSGreeterService"
argument_list|)
decl_stmt|;
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/jms_greeter"
argument_list|,
literal|"GreeterPort"
argument_list|)
decl_stmt|;
name|URL
name|wsdl
init|=
name|getWSDLURL
argument_list|(
literal|"/wsdl/jms_spec_test.wsdl"
argument_list|)
decl_stmt|;
name|JMSGreeterService
name|service
init|=
operator|new
name|JMSGreeterService
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|String
name|response1
init|=
operator|new
name|String
argument_list|(
literal|"Hello Milestone-"
argument_list|)
decl_stmt|;
name|String
name|response2
init|=
operator|new
name|String
argument_list|(
literal|"Bonjour"
argument_list|)
decl_stmt|;
name|JMSGreeterPortType
name|greeter
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|JMSGreeterPortType
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|idx
init|=
literal|0
init|;
name|idx
operator|<
literal|5
condition|;
name|idx
operator|++
control|)
block|{
name|greeter
operator|.
name|greetMeOneWay
argument_list|(
literal|"test String"
argument_list|)
expr_stmt|;
name|String
name|greeting
init|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"Milestone-"
operator|+
name|idx
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"no response received from service"
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
name|String
name|exResponse
init|=
name|response1
operator|+
name|idx
decl_stmt|;
name|assertEquals
argument_list|(
name|exResponse
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
name|String
name|reply
init|=
name|greeter
operator|.
name|sayHi
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"no response received from service"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|response2
argument_list|,
name|reply
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWsdlExtensionSpecJMS
parameter_list|()
throws|throws
name|Exception
block|{
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/jms_greeter"
argument_list|,
literal|"JMSGreeterService"
argument_list|)
decl_stmt|;
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/jms_greeter"
argument_list|,
literal|"GreeterPort"
argument_list|)
decl_stmt|;
name|URL
name|wsdl
init|=
name|getWSDLURL
argument_list|(
literal|"/wsdl/jms_spec_test.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|wsdl
argument_list|)
expr_stmt|;
name|JMSGreeterService
name|service
init|=
operator|new
name|JMSGreeterService
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|String
name|response
init|=
operator|new
name|String
argument_list|(
literal|"Bonjour"
argument_list|)
decl_stmt|;
try|try
block|{
name|JMSGreeterPortType
name|greeter
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|JMSGreeterPortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|requestContext
init|=
operator|(
operator|(
name|BindingProvider
operator|)
name|greeter
operator|)
operator|.
name|getRequestContext
argument_list|()
decl_stmt|;
name|JMSMessageHeadersType
name|requestHeader
init|=
operator|new
name|JMSMessageHeadersType
argument_list|()
decl_stmt|;
name|requestContext
operator|.
name|put
argument_list|(
name|JMSConstants
operator|.
name|JMS_CLIENT_REQUEST_HEADERS
argument_list|,
name|requestHeader
argument_list|)
expr_stmt|;
name|String
name|reply
init|=
name|greeter
operator|.
name|sayHi
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"no response received from service"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|response
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|requestContext
operator|=
operator|(
operator|(
name|BindingProvider
operator|)
name|greeter
operator|)
operator|.
name|getRequestContext
argument_list|()
expr_stmt|;
name|requestHeader
operator|=
operator|(
name|JMSMessageHeadersType
operator|)
name|requestContext
operator|.
name|get
argument_list|(
name|JMSConstants
operator|.
name|JMS_CLIENT_REQUEST_HEADERS
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|requestHeader
operator|.
name|getSOAPJMSBindingVersion
argument_list|()
argument_list|,
literal|"1.0"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|requestHeader
operator|.
name|getSOAPJMSSOAPAction
argument_list|()
argument_list|,
literal|"\"test\""
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|requestHeader
operator|.
name|getTimeToLive
argument_list|()
argument_list|,
literal|3000
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|requestHeader
operator|.
name|getJMSDeliveryMode
argument_list|()
argument_list|,
name|DeliveryMode
operator|.
name|PERSISTENT
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|requestHeader
operator|.
name|getJMSPriority
argument_list|()
argument_list|,
literal|7
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|responseContext
init|=
operator|(
operator|(
name|BindingProvider
operator|)
name|greeter
operator|)
operator|.
name|getResponseContext
argument_list|()
decl_stmt|;
name|JMSMessageHeadersType
name|responseHeader
init|=
operator|(
name|JMSMessageHeadersType
operator|)
name|responseContext
operator|.
name|get
argument_list|(
name|JMSConstants
operator|.
name|JMS_CLIENT_RESPONSE_HEADERS
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|responseHeader
operator|.
name|getSOAPJMSBindingVersion
argument_list|()
argument_list|,
literal|"1.0"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|responseHeader
operator|.
name|getSOAPJMSSOAPAction
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|responseHeader
operator|.
name|getJMSDeliveryMode
argument_list|()
argument_list|,
name|DeliveryMode
operator|.
name|PERSISTENT
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|responseHeader
operator|.
name|getJMSPriority
argument_list|()
argument_list|,
literal|7
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UndeclaredThrowableException
name|ex
parameter_list|)
block|{
throw|throw
operator|(
name|Exception
operator|)
name|ex
operator|.
name|getCause
argument_list|()
throw|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWsdlExtensionSpecJMSPortError
parameter_list|()
throws|throws
name|Exception
block|{
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/jms_greeter"
argument_list|,
literal|"JMSGreeterService2"
argument_list|)
decl_stmt|;
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/jms_greeter"
argument_list|,
literal|"GreeterPort2"
argument_list|)
decl_stmt|;
name|URL
name|wsdl
init|=
name|getWSDLURL
argument_list|(
literal|"/wsdl/jms_spec_test.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|wsdl
argument_list|)
expr_stmt|;
name|JMSGreeterService2
name|service
init|=
operator|new
name|JMSGreeterService2
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|String
name|response
init|=
operator|new
name|String
argument_list|(
literal|"Bonjour"
argument_list|)
decl_stmt|;
name|JMSGreeterPortType
name|greeter
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|JMSGreeterPortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|reply
init|=
name|greeter
operator|.
name|sayHi
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"no response received from service"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|response
argument_list|,
name|reply
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSpecNoWsdlService
parameter_list|()
throws|throws
name|Exception
block|{
name|specNoWsdlService
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSpecNoWsdlServiceWithDifferentMessageType
parameter_list|()
throws|throws
name|Exception
block|{
name|specNoWsdlService
argument_list|(
literal|"text"
argument_list|)
expr_stmt|;
name|specNoWsdlService
argument_list|(
literal|"byte"
argument_list|)
expr_stmt|;
name|specNoWsdlService
argument_list|(
literal|"binary"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|specNoWsdlService
parameter_list|(
name|String
name|messageType
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|address
init|=
literal|"jms:jndi:dynamicQueues/test.cxf.jmstransport.queue3"
operator|+
literal|"?jndiInitialContextFactory"
operator|+
literal|"=org.apache.activemq.jndi.ActiveMQInitialContextFactory"
operator|+
literal|"&jndiConnectionFactoryName=ConnectionFactory&jndiURL="
operator|+
name|broker
operator|.
name|getEncodedBrokerURL
argument_list|()
decl_stmt|;
if|if
condition|(
name|messageType
operator|!=
literal|null
condition|)
block|{
name|address
operator|=
name|address
operator|+
literal|"&messageType="
operator|+
name|messageType
expr_stmt|;
block|}
name|JaxWsProxyFactoryBean
name|factory
init|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setTransportId
argument_list|(
name|JMSSpecConstants
operator|.
name|SOAP_JMS_SPECIFICATION_TRANSPORTID
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setServiceClass
argument_list|(
name|Hello
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|Hello
name|client
init|=
operator|(
name|Hello
operator|)
name|factory
operator|.
name|create
argument_list|()
decl_stmt|;
name|String
name|reply
init|=
name|client
operator|.
name|sayHi
argument_list|(
literal|" HI"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|reply
argument_list|,
literal|"get HI"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBindingVersionError
parameter_list|()
throws|throws
name|Exception
block|{
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/jms_greeter"
argument_list|,
literal|"JMSGreeterService"
argument_list|)
decl_stmt|;
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/jms_greeter"
argument_list|,
literal|"GreeterPort"
argument_list|)
decl_stmt|;
name|URL
name|wsdl
init|=
name|getWSDLURL
argument_list|(
literal|"/wsdl/jms_spec_test.wsdl"
argument_list|)
decl_stmt|;
name|JMSGreeterService
name|service
init|=
operator|new
name|JMSGreeterService
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|JMSGreeterPortType
name|greeter
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|JMSGreeterPortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|BindingProvider
name|bp
init|=
operator|(
name|BindingProvider
operator|)
name|greeter
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|requestContext
init|=
name|bp
operator|.
name|getRequestContext
argument_list|()
decl_stmt|;
name|JMSMessageHeadersType
name|requestHeader
init|=
operator|new
name|JMSMessageHeadersType
argument_list|()
decl_stmt|;
name|requestHeader
operator|.
name|setSOAPJMSBindingVersion
argument_list|(
literal|"0.3"
argument_list|)
expr_stmt|;
name|requestContext
operator|.
name|put
argument_list|(
name|JMSConstants
operator|.
name|JMS_CLIENT_REQUEST_HEADERS
argument_list|,
name|requestHeader
argument_list|)
expr_stmt|;
try|try
block|{
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"Milestone-"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown a fault"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPFaultException
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"0.3"
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|responseContext
init|=
name|bp
operator|.
name|getResponseContext
argument_list|()
decl_stmt|;
name|JMSMessageHeadersType
name|responseHdr
init|=
operator|(
name|JMSMessageHeadersType
operator|)
name|responseContext
operator|.
name|get
argument_list|(
name|JMSConstants
operator|.
name|JMS_CLIENT_RESPONSE_HEADERS
argument_list|)
decl_stmt|;
if|if
condition|(
name|responseHdr
operator|==
literal|null
condition|)
block|{
name|fail
argument_list|(
literal|"response Header should not be null"
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|responseHdr
operator|.
name|isSOAPJMSIsFault
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReplyToConfig
parameter_list|()
throws|throws
name|Exception
block|{
name|JMSEndpoint
name|endpoint
init|=
operator|new
name|JMSEndpoint
argument_list|()
decl_stmt|;
name|endpoint
operator|.
name|setJndiInitialContextFactory
argument_list|(
literal|"org.apache.activemq.jndi.ActiveMQInitialContextFactory"
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setJndiURL
argument_list|(
name|broker
operator|.
name|getBrokerURL
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setJndiConnectionFactoryName
argument_list|(
literal|"ConnectionFactory"
argument_list|)
expr_stmt|;
specifier|final
name|JMSConfiguration
name|jmsConfig
init|=
operator|new
name|JMSConfiguration
argument_list|()
decl_stmt|;
name|JndiHelper
name|jt
init|=
operator|new
name|JndiHelper
argument_list|(
name|JMSOldConfigHolder
operator|.
name|getInitialContextEnv
argument_list|(
name|endpoint
argument_list|)
argument_list|)
decl_stmt|;
name|JNDIConfiguration
name|jndiConfig
init|=
operator|new
name|JNDIConfiguration
argument_list|()
decl_stmt|;
name|jndiConfig
operator|.
name|setJndiConnectionFactoryName
argument_list|(
name|endpoint
operator|.
name|getJndiConnectionFactoryName
argument_list|()
argument_list|)
expr_stmt|;
name|jmsConfig
operator|.
name|setJndiTemplate
argument_list|(
name|jt
argument_list|)
expr_stmt|;
name|jmsConfig
operator|.
name|setJndiConfig
argument_list|(
name|jndiConfig
argument_list|)
expr_stmt|;
name|TestReceiver
name|receiver
init|=
operator|new
name|TestReceiver
argument_list|(
name|jmsConfig
operator|.
name|getConnectionFactory
argument_list|()
argument_list|,
literal|"dynamicQueues/SoapService7.replyto.queue"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|receiver
operator|.
name|setStaticReplyQueue
argument_list|(
literal|"dynamicQueues/SoapService7.reply.queue"
argument_list|)
expr_stmt|;
name|receiver
operator|.
name|runAsync
argument_list|()
expr_stmt|;
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_doc_lit"
argument_list|,
literal|"SOAPService7"
argument_list|)
decl_stmt|;
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_doc_lit"
argument_list|,
literal|"SoapPort7"
argument_list|)
decl_stmt|;
name|URL
name|wsdl
init|=
name|getWSDLURL
argument_list|(
literal|"/wsdl/hello_world_doc_lit.wsdl"
argument_list|)
decl_stmt|;
name|SOAPService7
name|service
init|=
operator|new
name|SOAPService7
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|Greeter
name|greeter
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|Greeter
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|name
init|=
literal|"FooBar"
decl_stmt|;
name|String
name|reply
init|=
name|greeter
operator|.
name|greetMe
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|reply
argument_list|,
literal|"Hello "
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

