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
name|swa
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
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
name|activation
operator|.
name|DataHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|util
operator|.
name|ByteArrayDataSource
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
name|Holder
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
name|binding
operator|.
name|soap
operator|.
name|jms
operator|.
name|interceptor
operator|.
name|SoapJMSConstants
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
name|ext
operator|.
name|logging
operator|.
name|LoggingOutInterceptor
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
name|jaxws
operator|.
name|JaxWsServerFactoryBean
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
name|AbstractBusTestServerBase
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
name|ClientServerSwaTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ADDRESS
init|=
literal|"jms:jndi:dynamicQueues/test.cxf.jmstransport.swa.queue"
operator|+
literal|"?jndiInitialContextFactory"
operator|+
literal|"=org.apache.activemq.jndi.ActiveMQInitialContextFactory"
operator|+
literal|"&jndiConnectionFactoryName=ConnectionFactory&jndiURL="
decl_stmt|;
specifier|static
name|EmbeddedJMSBrokerLauncher
name|broker
decl_stmt|;
specifier|public
specifier|static
class|class
name|Server
extends|extends
name|AbstractBusTestServerBase
block|{
specifier|protected
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|JaxWsServerFactoryBean
name|factory
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setBus
argument_list|(
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setWsdlLocation
argument_list|(
literal|"classpath:/swa-mime_jms.wsdl"
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setTransportId
argument_list|(
name|SoapJMSConstants
operator|.
name|SOAP_JMS_SPECIFICIATION_TRANSPORTID
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setServiceName
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/swa"
argument_list|,
literal|"SwAService"
argument_list|)
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setEndpointName
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/swa"
argument_list|,
literal|"SwAServiceJMSPort"
argument_list|)
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
name|ADDRESS
operator|+
name|broker
operator|.
name|getEncodedBrokerURL
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setServiceBean
argument_list|(
operator|new
name|SwAServiceImpl
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|create
argument_list|()
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
block|}
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
name|System
operator|.
name|setProperty
argument_list|(
literal|"EmbeddedBrokerURL"
argument_list|,
name|broker
operator|.
name|getBrokerURL
argument_list|()
argument_list|)
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
argument_list|()
argument_list|)
expr_stmt|;
name|createStaticBus
argument_list|()
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
name|System
operator|.
name|clearProperty
argument_list|(
literal|"EmbeddedBrokerURL"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSwa
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
name|setWsdlLocation
argument_list|(
literal|"classpath:/swa-mime_jms.wsdl"
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setTransportId
argument_list|(
name|SoapJMSConstants
operator|.
name|SOAP_JMS_SPECIFICIATION_TRANSPORTID
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setServiceName
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/swa"
argument_list|,
literal|"SwAService"
argument_list|)
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setEndpointName
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/swa"
argument_list|,
literal|"SwAServiceJMSPort"
argument_list|)
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
name|ADDRESS
operator|+
name|broker
operator|.
name|getEncodedBrokerURL
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|SwAService
name|port
init|=
name|factory
operator|.
name|create
argument_list|(
name|SwAService
operator|.
name|class
argument_list|)
decl_stmt|;
name|Holder
argument_list|<
name|String
argument_list|>
name|textHolder
init|=
operator|new
name|Holder
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|Holder
argument_list|<
name|DataHandler
argument_list|>
name|data
init|=
operator|new
name|Holder
argument_list|<
name|DataHandler
argument_list|>
argument_list|()
decl_stmt|;
name|ByteArrayDataSource
name|source
init|=
operator|new
name|ByteArrayDataSource
argument_list|(
literal|"foobar"
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|"application/octet-stream"
argument_list|)
decl_stmt|;
name|DataHandler
name|handler
init|=
operator|new
name|DataHandler
argument_list|(
name|source
argument_list|)
decl_stmt|;
name|data
operator|.
name|value
operator|=
name|handler
expr_stmt|;
name|textHolder
operator|.
name|value
operator|=
literal|"Hi"
expr_stmt|;
name|port
operator|.
name|echoData
argument_list|(
name|textHolder
argument_list|,
name|data
argument_list|)
expr_stmt|;
name|InputStream
name|bis
init|=
literal|null
decl_stmt|;
name|bis
operator|=
name|data
operator|.
name|value
operator|.
name|getDataSource
argument_list|()
operator|.
name|getInputStream
argument_list|()
expr_stmt|;
name|byte
name|b
index|[]
init|=
operator|new
name|byte
index|[
literal|10
index|]
decl_stmt|;
name|bis
operator|.
name|read
argument_list|(
name|b
argument_list|,
literal|0
argument_list|,
literal|10
argument_list|)
expr_stmt|;
name|String
name|string
init|=
name|IOUtils
operator|.
name|newStringFromBytes
argument_list|(
name|b
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"testfoobar"
argument_list|,
name|string
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hi"
argument_list|,
name|textHolder
operator|.
name|value
argument_list|)
expr_stmt|;
if|if
condition|(
name|port
operator|instanceof
name|Closeable
condition|)
block|{
operator|(
operator|(
name|Closeable
operator|)
name|port
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

