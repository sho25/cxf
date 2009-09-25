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
name|lang
operator|.
name|reflect
operator|.
name|InvocationHandler
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Proxy
import|;
end_import

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
name|binding
operator|.
name|soap
operator|.
name|saaj
operator|.
name|SAAJInInterceptor
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
name|saaj
operator|.
name|SAAJOutInterceptor
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
name|endpoint
operator|.
name|Client
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
name|endpoint
operator|.
name|ClientImpl
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
name|frontend
operator|.
name|ClientProxy
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
name|interceptor
operator|.
name|LoggingInInterceptor
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
name|interceptor
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
name|jaxws
operator|.
name|JaxWsClientProxy
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
name|binding
operator|.
name|soap
operator|.
name|SOAPBindingImpl
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
name|support
operator|.
name|JaxWsEndpointImpl
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
name|support
operator|.
name|JaxWsServiceFactoryBean
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
name|mime
operator|.
name|types
operator|.
name|XopStringType
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
name|factory
operator|.
name|ReflectionServiceFactoryBean
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
name|test
operator|.
name|TestUtilities
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
name|ClientMtomXopTest
extends|extends
name|AbstractBusClientServerTestBase
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
literal|"TestMtomPort"
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
literal|"TestMtomService"
argument_list|)
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
name|TestUtilities
operator|.
name|setKeepAliveSystemProperty
argument_list|(
literal|false
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
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|cleanup
parameter_list|()
block|{
name|TestUtilities
operator|.
name|recoverKeepAliveSystemProperty
argument_list|()
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
argument_list|,
literal|true
argument_list|)
decl_stmt|;
try|try
block|{
name|Holder
argument_list|<
name|DataHandler
argument_list|>
name|param
init|=
operator|new
name|Holder
argument_list|<
name|DataHandler
argument_list|>
argument_list|()
decl_stmt|;
name|Holder
argument_list|<
name|String
argument_list|>
name|name
decl_stmt|;
name|byte
name|bytes
index|[]
decl_stmt|;
name|InputStream
name|in
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
operator|(
operator|(
name|BindingProvider
operator|)
name|mtomPort
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"schema-validation-enabled"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
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
name|name
operator|=
operator|new
name|Holder
argument_list|<
name|String
argument_list|>
argument_list|(
literal|"call detail"
argument_list|)
expr_stmt|;
name|mtomPort
operator|.
name|testXop
argument_list|(
name|name
argument_list|,
name|param
argument_list|)
expr_stmt|;
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
name|assertNotNull
argument_list|(
name|param
operator|.
name|value
argument_list|)
expr_stmt|;
name|in
operator|=
name|param
operator|.
name|value
operator|.
name|getInputStream
argument_list|()
expr_stmt|;
name|bytes
operator|=
name|IOUtils
operator|.
name|readBytesFromStream
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|data
operator|.
name|length
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
expr_stmt|;
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
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
name|name
operator|=
operator|new
name|Holder
argument_list|<
name|String
argument_list|>
argument_list|(
literal|"call detail"
argument_list|)
expr_stmt|;
name|mtomPort
operator|.
name|testXop
argument_list|(
name|name
argument_list|,
name|param
argument_list|)
expr_stmt|;
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
name|assertNotNull
argument_list|(
name|param
operator|.
name|value
argument_list|)
expr_stmt|;
name|in
operator|=
name|param
operator|.
name|value
operator|.
name|getInputStream
argument_list|()
expr_stmt|;
name|bytes
operator|=
name|IOUtils
operator|.
name|readBytesFromStream
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|data
operator|.
name|length
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
expr_stmt|;
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|mtomPort
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"schema-validation-enabled"
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|SAAJOutInterceptor
name|saajOut
init|=
operator|new
name|SAAJOutInterceptor
argument_list|()
decl_stmt|;
name|SAAJInInterceptor
name|saajIn
init|=
operator|new
name|SAAJInInterceptor
argument_list|()
decl_stmt|;
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
name|name
operator|=
operator|new
name|Holder
argument_list|<
name|String
argument_list|>
argument_list|(
literal|"call detail"
argument_list|)
expr_stmt|;
name|mtomPort
operator|.
name|testXop
argument_list|(
name|name
argument_list|,
name|param
argument_list|)
expr_stmt|;
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
name|assertNotNull
argument_list|(
name|param
operator|.
name|value
argument_list|)
expr_stmt|;
name|in
operator|=
name|param
operator|.
name|value
operator|.
name|getInputStream
argument_list|()
expr_stmt|;
name|bytes
operator|=
name|IOUtils
operator|.
name|readBytesFromStream
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|data
operator|.
name|length
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
expr_stmt|;
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|mtomPort
argument_list|)
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|saajIn
argument_list|)
expr_stmt|;
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|mtomPort
argument_list|)
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|saajOut
argument_list|)
expr_stmt|;
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
name|name
operator|=
operator|new
name|Holder
argument_list|<
name|String
argument_list|>
argument_list|(
literal|"call detail"
argument_list|)
expr_stmt|;
name|mtomPort
operator|.
name|testXop
argument_list|(
name|name
argument_list|,
name|param
argument_list|)
expr_stmt|;
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
name|assertNotNull
argument_list|(
name|param
operator|.
name|value
argument_list|)
expr_stmt|;
name|in
operator|=
name|param
operator|.
name|value
operator|.
name|getInputStream
argument_list|()
expr_stmt|;
name|bytes
operator|=
name|IOUtils
operator|.
name|readBytesFromStream
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|data
operator|.
name|length
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
expr_stmt|;
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|mtomPort
argument_list|)
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|remove
argument_list|(
name|saajIn
argument_list|)
expr_stmt|;
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|mtomPort
argument_list|)
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|remove
argument_list|(
name|saajOut
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
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
if|if
condition|(
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Connection reset"
argument_list|)
operator|&&
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.specification.version"
argument_list|,
literal|"1.5"
argument_list|)
operator|.
name|contains
argument_list|(
literal|"1.6"
argument_list|)
condition|)
block|{
comment|//There seems to be a bug/interaction with Java 1.6 and Jetty where
comment|//Jetty will occasionally send back a RST prior to all the data being
comment|//sent back to the client when using localhost (which is what we do)
comment|//we'll ignore for now
return|return;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|System
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Ignore
comment|// see CXF-1395
annotation|@
name|Test
specifier|public
name|void
name|testMtoMString
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
argument_list|,
literal|false
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
name|long
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
name|byte
index|[]
name|data
init|=
operator|new
name|byte
index|[
operator|(
name|int
operator|)
name|fileSize
index|]
decl_stmt|;
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
argument_list|)
expr_stmt|;
name|String
name|stringValue
init|=
operator|new
name|String
argument_list|(
name|data
argument_list|,
literal|"utf-8"
argument_list|)
decl_stmt|;
name|XopStringType
name|xsv
init|=
operator|new
name|XopStringType
argument_list|()
decl_stmt|;
name|xsv
operator|.
name|setAttachinfo
argument_list|(
name|stringValue
argument_list|)
expr_stmt|;
name|xsv
operator|.
name|setName
argument_list|(
literal|"eman"
argument_list|)
expr_stmt|;
name|XopStringType
name|r
init|=
name|mtomPort
operator|.
name|testXopString
argument_list|(
name|xsv
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|r
argument_list|)
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
parameter_list|,
name|boolean
name|installInterceptors
parameter_list|)
throws|throws
name|Exception
block|{
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
decl_stmt|;
name|ReflectionServiceFactoryBean
name|serviceFactory
init|=
operator|new
name|JaxWsServiceFactoryBean
argument_list|()
decl_stmt|;
name|serviceFactory
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|serviceFactory
operator|.
name|setServiceName
argument_list|(
name|serviceName
argument_list|)
expr_stmt|;
name|serviceFactory
operator|.
name|setServiceClass
argument_list|(
name|serviceEndpointInterface
argument_list|)
expr_stmt|;
name|serviceFactory
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
argument_list|)
expr_stmt|;
name|Service
name|service
init|=
name|serviceFactory
operator|.
name|create
argument_list|()
decl_stmt|;
name|EndpointInfo
name|ei
init|=
name|service
operator|.
name|getEndpointInfo
argument_list|(
name|portName
argument_list|)
decl_stmt|;
name|JaxWsEndpointImpl
name|jaxwsEndpoint
init|=
operator|new
name|JaxWsEndpointImpl
argument_list|(
name|bus
argument_list|,
name|service
argument_list|,
name|ei
argument_list|)
decl_stmt|;
name|SOAPBinding
name|jaxWsSoapBinding
init|=
operator|new
name|SOAPBindingImpl
argument_list|(
name|ei
operator|.
name|getBinding
argument_list|()
argument_list|)
decl_stmt|;
name|jaxWsSoapBinding
operator|.
name|setMTOMEnabled
argument_list|(
name|enableMTOM
argument_list|)
expr_stmt|;
if|if
condition|(
name|installInterceptors
condition|)
block|{
comment|//jaxwsEndpoint.getBinding().getInInterceptors().add(new TestMultipartMessageInterceptor());
name|jaxwsEndpoint
operator|.
name|getBinding
argument_list|()
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
block|}
name|jaxwsEndpoint
operator|.
name|getBinding
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|jaxwsEndpoint
operator|.
name|getBinding
argument_list|()
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
name|Client
name|client
init|=
operator|new
name|ClientImpl
argument_list|(
name|bus
argument_list|,
name|jaxwsEndpoint
argument_list|)
decl_stmt|;
name|InvocationHandler
name|ih
init|=
operator|new
name|JaxWsClientProxy
argument_list|(
name|client
argument_list|,
name|jaxwsEndpoint
operator|.
name|getJaxwsBinding
argument_list|()
argument_list|)
decl_stmt|;
name|Object
name|obj
init|=
name|Proxy
operator|.
name|newProxyInstance
argument_list|(
name|serviceEndpointInterface
operator|.
name|getClassLoader
argument_list|()
argument_list|,
operator|new
name|Class
index|[]
block|{
name|serviceEndpointInterface
block|,
name|BindingProvider
operator|.
name|class
block|}
argument_list|,
name|ih
argument_list|)
decl_stmt|;
return|return
name|serviceEndpointInterface
operator|.
name|cast
argument_list|(
name|obj
argument_list|)
return|;
block|}
block|}
end_class

end_unit

