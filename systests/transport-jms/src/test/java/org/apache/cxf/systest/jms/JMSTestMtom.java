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
name|net
operator|.
name|URL
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
name|Binding
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
name|Endpoint
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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|soap
operator|.
name|SOAPBinding
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
name|jms_mtom
operator|.
name|JMSMTOMPortType
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
name|jms_mtom
operator|.
name|JMSMTOMService
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
name|jms_mtom
operator|.
name|JMSOutMTOMService
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
annotation|@
name|Ignore
specifier|public
class|class
name|JMSTestMtom
block|{
specifier|private
specifier|static
name|EmbeddedJMSBrokerLauncher
name|broker
decl_stmt|;
specifier|private
specifier|static
name|Bus
name|bus
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
name|broker
operator|=
operator|new
name|EmbeddedJMSBrokerLauncher
argument_list|()
expr_stmt|;
name|broker
operator|.
name|startInProcess
argument_list|()
expr_stmt|;
name|bus
operator|=
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
expr_stmt|;
name|broker
operator|.
name|updateWsdl
argument_list|(
name|bus
argument_list|,
literal|"testutils/jms_test_mtom.wsdl"
argument_list|)
expr_stmt|;
name|Object
name|mtom
init|=
operator|new
name|JMSMTOMImpl
argument_list|()
decl_stmt|;
name|EndpointImpl
name|ep
init|=
operator|(
name|EndpointImpl
operator|)
name|Endpoint
operator|.
name|publish
argument_list|(
literal|"jms:jndi:dynamicQueues/test.cxf.jmstransport.queue&amp;receiveTimeout=10000"
argument_list|,
name|mtom
argument_list|)
decl_stmt|;
name|Binding
name|binding
init|=
name|ep
operator|.
name|getBinding
argument_list|()
decl_stmt|;
operator|(
operator|(
name|SOAPBinding
operator|)
name|binding
operator|)
operator|.
name|setMTOMEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|stopServers
parameter_list|()
throws|throws
name|Exception
block|{
name|broker
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMTOM
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
literal|"http://cxf.apache.org/jms_mtom"
argument_list|,
literal|"JMSMTOMService"
argument_list|)
decl_stmt|;
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/jms_mtom"
argument_list|,
literal|"MTOMPort"
argument_list|)
decl_stmt|;
name|URL
name|wsdl
init|=
name|getWSDLURL
argument_list|(
literal|"/wsdl/jms_test_mtom.wsdl"
argument_list|)
decl_stmt|;
name|JMSMTOMService
name|service
init|=
operator|new
name|JMSMTOMService
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|JMSMTOMPortType
name|mtom
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|JMSMTOMPortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|Binding
name|binding
init|=
operator|(
operator|(
name|BindingProvider
operator|)
name|mtom
operator|)
operator|.
name|getBinding
argument_list|()
decl_stmt|;
operator|(
operator|(
name|SOAPBinding
operator|)
name|binding
operator|)
operator|.
name|setMTOMEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Holder
argument_list|<
name|String
argument_list|>
name|name
init|=
operator|new
name|Holder
argument_list|<>
argument_list|(
literal|"Sam"
argument_list|)
decl_stmt|;
name|URL
name|fileURL
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/org/apache/cxf/systest/jms/JMSClientServerTest.class"
argument_list|)
decl_stmt|;
name|Holder
argument_list|<
name|DataHandler
argument_list|>
name|handler1
init|=
operator|new
name|Holder
argument_list|<>
argument_list|()
decl_stmt|;
name|handler1
operator|.
name|value
operator|=
operator|new
name|DataHandler
argument_list|(
name|fileURL
argument_list|)
expr_stmt|;
name|int
name|size
init|=
name|handler1
operator|.
name|value
operator|.
name|getInputStream
argument_list|()
operator|.
name|available
argument_list|()
decl_stmt|;
name|mtom
operator|.
name|testDataHandler
argument_list|(
name|name
argument_list|,
name|handler1
argument_list|)
expr_stmt|;
name|byte
index|[]
name|bytes
init|=
name|IOUtils
operator|.
name|readBytesFromStream
argument_list|(
name|handler1
operator|.
name|value
operator|.
name|getInputStream
argument_list|()
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"The response file is not same with the sent file."
argument_list|,
name|size
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
expr_stmt|;
operator|(
operator|(
name|Closeable
operator|)
name|mtom
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOutMTOM
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
literal|"http://cxf.apache.org/jms_mtom"
argument_list|,
literal|"JMSMTOMService"
argument_list|)
decl_stmt|;
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/jms_mtom"
argument_list|,
literal|"MTOMPort"
argument_list|)
decl_stmt|;
name|URL
name|wsdl
init|=
name|getWSDLURL
argument_list|(
literal|"/wsdl/jms_test_mtom.wsdl"
argument_list|)
decl_stmt|;
name|JMSOutMTOMService
name|service
init|=
operator|new
name|JMSOutMTOMService
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|JMSMTOMPortType
name|mtom
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|JMSMTOMPortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|URL
name|fileURL
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/org/apache/cxf/systest/jms/JMSClientServerTest.class"
argument_list|)
decl_stmt|;
name|DataHandler
name|handler1
init|=
operator|new
name|DataHandler
argument_list|(
name|fileURL
argument_list|)
decl_stmt|;
name|int
name|size
init|=
name|handler1
operator|.
name|getInputStream
argument_list|()
operator|.
name|available
argument_list|()
decl_stmt|;
name|DataHandler
name|ret
init|=
name|mtom
operator|.
name|testOutMtom
argument_list|()
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
name|IOUtils
operator|.
name|readBytesFromStream
argument_list|(
name|ret
operator|.
name|getInputStream
argument_list|()
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"The response file is not same with the original file."
argument_list|,
name|size
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
expr_stmt|;
operator|(
operator|(
name|Closeable
operator|)
name|mtom
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|public
specifier|static
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
name|JMSTestMtom
operator|.
name|class
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
name|String
name|wsdlString
init|=
name|u
operator|.
name|toString
argument_list|()
operator|.
name|intern
argument_list|()
decl_stmt|;
name|broker
operator|.
name|updateWsdl
argument_list|(
name|bus
argument_list|,
name|wsdlString
argument_list|)
expr_stmt|;
return|return
name|u
return|;
block|}
block|}
end_class

end_unit

