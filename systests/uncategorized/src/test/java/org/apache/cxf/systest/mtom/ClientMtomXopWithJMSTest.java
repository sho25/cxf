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
name|mtom
package|;
end_package

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
name|util
operator|.
name|Collections
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
name|mime
operator|.
name|TestMtom
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
name|ClientMtomXopWithJMSTest
block|{
specifier|public
specifier|static
specifier|final
name|QName
name|MTOM_PORT
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/mime"
argument_list|,
literal|"TestMtomJMSPort"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|MTOM_SERVICE
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/mime"
argument_list|,
literal|"TestMtomJMSService"
argument_list|)
decl_stmt|;
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
name|Object
name|implementor
init|=
operator|new
name|TestMtomJMSImpl
argument_list|()
decl_stmt|;
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
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|TestMultipartMessageInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|ep
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|TestAttachmentOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
comment|//ep.getInInterceptors().add(new LoggingInInterceptor());
comment|//ep.getOutInterceptors().add(new LoggingOutInterceptor());
name|SOAPBinding
name|jaxWsSoapBinding
init|=
operator|(
name|SOAPBinding
operator|)
name|ep
operator|.
name|getBinding
argument_list|()
decl_stmt|;
name|jaxWsSoapBinding
operator|.
name|setMTOMEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ep
operator|.
name|publish
argument_list|()
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
name|bus
operator|.
name|shutdown
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMtomXop
parameter_list|()
throws|throws
name|Exception
block|{
name|TestMtom
name|mtomPort
init|=
name|createPort
argument_list|(
name|MTOM_SERVICE
argument_list|,
name|MTOM_PORT
argument_list|,
name|TestMtom
operator|.
name|class
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|InputStream
name|pre
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/wsdl/mtom_xop.wsdl"
argument_list|)
decl_stmt|;
name|int
name|fileSize
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|pre
operator|.
name|read
argument_list|()
init|;
name|i
operator|!=
operator|-
literal|1
condition|;
name|i
operator|=
name|pre
operator|.
name|read
argument_list|()
control|)
block|{
name|fileSize
operator|++
expr_stmt|;
block|}
name|Holder
argument_list|<
name|DataHandler
argument_list|>
name|param
init|=
operator|new
name|Holder
argument_list|<>
argument_list|()
decl_stmt|;
name|int
name|count
init|=
literal|50
decl_stmt|;
name|byte
index|[]
name|data
init|=
operator|new
name|byte
index|[
name|fileSize
operator|*
name|count
index|]
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|count
condition|;
name|x
operator|++
control|)
block|{
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/wsdl/mtom_xop.wsdl"
argument_list|)
operator|.
name|read
argument_list|(
name|data
argument_list|,
name|fileSize
operator|*
name|x
argument_list|,
name|fileSize
argument_list|)
expr_stmt|;
block|}
name|param
operator|.
name|value
operator|=
operator|new
name|DataHandler
argument_list|(
operator|new
name|ByteArrayDataSource
argument_list|(
name|data
argument_list|,
literal|"application/octet-stream"
argument_list|)
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
literal|"call detail"
argument_list|)
decl_stmt|;
name|mtomPort
operator|.
name|testXop
argument_list|(
name|name
argument_list|,
name|param
argument_list|)
expr_stmt|;
comment|// TODO Why should it fail here?
comment|// Assert.fail("Expect the exception here !");
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"name unchanged"
argument_list|,
literal|"return detail + call detail"
argument_list|,
name|name
operator|.
name|value
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|param
operator|.
name|value
argument_list|)
expr_stmt|;
name|param
operator|.
name|value
operator|.
name|getInputStream
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|createPort
parameter_list|(
name|QName
name|serviceName
parameter_list|,
name|QName
name|portName
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|serviceEndpointInterface
parameter_list|,
name|boolean
name|enableMTOM
parameter_list|)
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
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setServiceName
argument_list|(
name|serviceName
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setServiceClass
argument_list|(
name|serviceEndpointInterface
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setWsdlURL
argument_list|(
name|ClientMtomXopTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"/wsdl/mtom_xop.wsdl"
argument_list|)
operator|.
name|toExternalForm
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setFeatures
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|cff
argument_list|)
argument_list|)
expr_stmt|;
name|factory
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|TestMultipartMessageInterceptor
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
name|TestAttachmentOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|T
name|proxy
init|=
operator|(
name|T
operator|)
name|factory
operator|.
name|create
argument_list|()
decl_stmt|;
name|BindingProvider
name|bp
init|=
operator|(
name|BindingProvider
operator|)
name|proxy
decl_stmt|;
name|SOAPBinding
name|binding
init|=
operator|(
name|SOAPBinding
operator|)
name|bp
operator|.
name|getBinding
argument_list|()
decl_stmt|;
name|binding
operator|.
name|setMTOMEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|proxy
return|;
block|}
block|}
end_class

end_unit

