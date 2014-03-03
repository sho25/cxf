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
name|continuations
package|;
end_package

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
name|Endpoint
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
name|pool
operator|.
name|PooledConnectionFactory
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
name|hello_world_jms
operator|.
name|HelloWorldPortType
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
name|hello_world_jms
operator|.
name|HelloWorldService
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
name|EndpointImpl
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
name|ConnectionFactoryFeature
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

begin_class
specifier|public
class|class
name|JMSContinuationsClientServerTest
block|{
specifier|private
specifier|static
name|Bus
name|bus
decl_stmt|;
specifier|private
specifier|static
name|ConnectionFactoryFeature
name|cff
decl_stmt|;
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
name|bus
operator|=
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
expr_stmt|;
name|ActiveMQConnectionFactory
name|cf
init|=
operator|new
name|ActiveMQConnectionFactory
argument_list|(
literal|"vm://localhost?broker.persistent=false"
argument_list|)
decl_stmt|;
name|PooledConnectionFactory
name|cfp
init|=
operator|new
name|PooledConnectionFactory
argument_list|(
name|cf
argument_list|)
decl_stmt|;
name|cff
operator|=
operator|new
name|ConnectionFactoryFeature
argument_list|(
name|cfp
argument_list|)
expr_stmt|;
name|Object
name|implementor
init|=
operator|new
name|GreeterImplWithContinuationsJMS
argument_list|()
decl_stmt|;
name|String
name|address
init|=
literal|"jms:queue:test.jmstransport.text?replyToQueueName=test.jmstransport.text.reply"
decl_stmt|;
name|EndpointImpl
name|ep
init|=
operator|(
name|EndpointImpl
operator|)
name|Endpoint
operator|.
name|create
argument_list|(
name|implementor
argument_list|)
decl_stmt|;
name|ep
operator|.
name|getFeatures
argument_list|()
operator|.
name|add
argument_list|(
name|cff
argument_list|)
expr_stmt|;
name|ep
operator|.
name|publish
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|clearProperty
parameter_list|()
block|{
name|bus
operator|.
name|shutdown
argument_list|(
literal|false
argument_list|)
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
name|URL
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|wsdl
argument_list|)
expr_stmt|;
return|return
name|wsdl
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testContinuationWithTimeout
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
literal|"http://cxf.apache.org/hello_world_jms"
argument_list|,
literal|"HelloWorldService"
argument_list|)
decl_stmt|;
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/hello_world_jms"
argument_list|,
literal|"HelloWorldPort"
argument_list|)
decl_stmt|;
name|URL
name|wsdl
init|=
name|getWSDLURL
argument_list|(
literal|"/org/apache/cxf/systest/jms/continuations/jms_test.wsdl"
argument_list|)
decl_stmt|;
name|HelloWorldService
name|service
init|=
operator|new
name|HelloWorldService
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|HelloWorldPortType
name|greeter
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|HelloWorldPortType
operator|.
name|class
argument_list|,
name|cff
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Hi Fred Ruby"
argument_list|,
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"Fred"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

