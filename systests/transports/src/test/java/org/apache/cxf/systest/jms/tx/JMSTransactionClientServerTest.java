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
name|systest
operator|.
name|jms
operator|.
name|tx
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
name|HashMap
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
name|ConnectionFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Definition
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
name|JMSConfigFeature
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
name|PingMeFault
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
name|SOAPService2
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|JMSTransactionClientServerTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|protected
specifier|static
name|boolean
name|serversStarted
decl_stmt|;
specifier|static
specifier|final
name|String
name|JMS_PORT
init|=
name|EmbeddedJMSBrokerLauncher
operator|.
name|PORT
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|startServers
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|serversStarted
condition|)
block|{
return|return;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|props
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"activemq.store.dir"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|props
operator|.
name|put
argument_list|(
literal|"activemq.store.dir"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"activemq.store.dir"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|props
operator|.
name|put
argument_list|(
literal|"java.util.logging.config.file"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.util.logging.config.file"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|EmbeddedJMSBrokerLauncher
operator|.
name|class
argument_list|,
name|props
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|Server
operator|.
name|class
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|serversStarted
operator|=
literal|true
expr_stmt|;
block|}
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
return|return
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
name|s
argument_list|)
return|;
block|}
specifier|public
name|QName
name|getServiceName
parameter_list|(
name|QName
name|q
parameter_list|)
block|{
return|return
name|q
return|;
block|}
specifier|public
name|QName
name|getPortName
parameter_list|(
name|QName
name|q
parameter_list|)
block|{
return|return
name|q
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDocBasicConnection
parameter_list|()
throws|throws
name|Exception
block|{
name|QName
name|serviceName
init|=
name|getServiceName
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_doc_lit"
argument_list|,
literal|"SOAPService2"
argument_list|)
argument_list|)
decl_stmt|;
name|QName
name|portName
init|=
name|getPortName
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_doc_lit"
argument_list|,
literal|"SoapPort2"
argument_list|)
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
name|assertNotNull
argument_list|(
name|wsdl
argument_list|)
expr_stmt|;
name|Definition
name|def
init|=
name|EmbeddedJMSBrokerLauncher
operator|.
name|updateWsdlExtensors
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|wsdl
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|def
argument_list|)
expr_stmt|;
name|SOAPService2
name|service
init|=
operator|new
name|SOAPService2
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
name|doService
argument_list|(
name|greeter
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNonAopTransaction
parameter_list|()
throws|throws
name|Exception
block|{
name|JaxWsProxyFactoryBean
name|factory
init|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setServiceClass
argument_list|(
name|Greeter
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
literal|"jms://"
argument_list|)
expr_stmt|;
name|JMSConfiguration
name|jmsConfig
init|=
operator|new
name|JMSConfiguration
argument_list|()
decl_stmt|;
name|ConnectionFactory
name|connectionFactory
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|ActiveMQConnectionFactory
argument_list|(
literal|"tcp://localhost:"
operator|+
name|JMS_PORT
argument_list|)
decl_stmt|;
name|jmsConfig
operator|.
name|setConnectionFactory
argument_list|(
name|connectionFactory
argument_list|)
expr_stmt|;
name|jmsConfig
operator|.
name|setTargetDestination
argument_list|(
literal|"greeter.queue.noaop"
argument_list|)
expr_stmt|;
name|jmsConfig
operator|.
name|setPubSubDomain
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|jmsConfig
operator|.
name|setUseJms11
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|JMSConfigFeature
name|jmsConfigFeature
init|=
operator|new
name|JMSConfigFeature
argument_list|()
decl_stmt|;
name|jmsConfigFeature
operator|.
name|setJmsConfig
argument_list|(
name|jmsConfig
argument_list|)
expr_stmt|;
name|factory
operator|.
name|getFeatures
argument_list|()
operator|.
name|add
argument_list|(
name|jmsConfigFeature
argument_list|)
expr_stmt|;
name|Greeter
name|greeter
init|=
operator|(
name|Greeter
operator|)
name|factory
operator|.
name|create
argument_list|()
decl_stmt|;
name|doService
argument_list|(
name|greeter
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|doService
parameter_list|(
name|Greeter
name|greeter
parameter_list|,
name|boolean
name|doEx
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|response1
init|=
operator|new
name|String
argument_list|(
literal|"Hello "
argument_list|)
decl_stmt|;
try|try
block|{
name|String
name|greeting
init|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"Good guy"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No response received from service"
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
name|String
name|exResponse
init|=
name|response1
operator|+
literal|"Good guy"
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get unexcpeted result"
argument_list|,
name|exResponse
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
name|greeting
operator|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"Bad guy"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"No response received from service"
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
name|exResponse
operator|=
name|response1
operator|+
literal|"[Bad guy]"
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get unexcpeted result"
argument_list|,
name|exResponse
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
if|if
condition|(
name|doEx
condition|)
block|{
try|try
block|{
name|greeter
operator|.
name|pingMe
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown FaultException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PingMeFault
name|ex
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|ex
operator|.
name|getFaultInfo
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
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
block|}
end_class

end_unit

