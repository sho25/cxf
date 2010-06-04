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
name|ws
operator|.
name|addressing
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
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
name|jws
operator|.
name|WebService
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

begin_comment
comment|/**  * Tests the addition of WS-Addressing Message Addressing Properties  * in the non-decoupled case.  */
end_comment

begin_class
specifier|public
class|class
name|DecoupledJMSTest
extends|extends
name|MAPTestBase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|ADDRESS
init|=
literal|"jms:jndi:dynamicQueues/testqueue0001?"
operator|+
literal|"jndiInitialContextFactory=org.apache.activemq.jndi.ActiveMQInitialContextFactory"
operator|+
literal|"&jndiConnectionFactoryName=ConnectionFactory&jndiURL=tcp://localhost:"
operator|+
name|EmbeddedJMSBrokerLauncher
operator|.
name|PORT
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CONFIG
init|=
literal|"org/apache/cxf/systest/ws/addressing/jms_decoupled.xml"
decl_stmt|;
specifier|public
name|String
name|getConfigFileName
parameter_list|()
block|{
return|return
name|CONFIG
return|;
block|}
specifier|protected
name|void
name|updateAddressPort
parameter_list|(
name|Object
name|o
parameter_list|,
name|int
name|port
parameter_list|)
throws|throws
name|MalformedURLException
block|{     }
annotation|@
name|Test
annotation|@
name|Override
specifier|public
name|void
name|testImplicitMAPs
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|testImplicitMAPs
argument_list|()
expr_stmt|;
block|}
specifier|public
name|String
name|getAddress
parameter_list|()
block|{
return|return
name|ADDRESS
return|;
block|}
specifier|public
name|URL
name|getWSDLURL
parameter_list|()
block|{
return|return
literal|null
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
literal|null
argument_list|,
operator|new
name|String
index|[]
block|{
name|ADDRESS
block|,
name|GreeterImpl
operator|.
name|class
operator|.
name|getName
argument_list|()
block|}
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|WebService
argument_list|(
name|serviceName
operator|=
literal|"SOAPServiceAddressing"
argument_list|,
name|portName
operator|=
literal|"SoapPort"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.hello_world_soap_http.Greeter"
argument_list|,
name|targetNamespace
operator|=
literal|"http://apache.org/hello_world_soap_http"
argument_list|)
specifier|public
specifier|static
class|class
name|GreeterImpl
extends|extends
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|systest
operator|.
name|ws
operator|.
name|addressing
operator|.
name|AbstractGreeterImpl
block|{              }
block|}
end_class

end_unit

