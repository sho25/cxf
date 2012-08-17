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
name|jaxws
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
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
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
name|WebServiceContext
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
name|BusException
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
name|service
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
name|service
operator|.
name|invoker
operator|.
name|BeanInvoker
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
name|ws
operator|.
name|addressing
operator|.
name|WSAddressingFeature
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|GreeterImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|HelloImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|HelloWrongAnnotation
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
name|EndpointImplTest
extends|extends
name|AbstractJaxWsTest
block|{
annotation|@
name|Override
specifier|protected
name|Bus
name|createBus
parameter_list|()
throws|throws
name|BusException
block|{
return|return
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|address
init|=
literal|"http://localhost:8080/test"
decl_stmt|;
name|GreeterImpl
name|greeter
init|=
operator|new
name|GreeterImpl
argument_list|()
decl_stmt|;
name|EndpointImpl
name|endpoint
init|=
operator|new
name|EndpointImpl
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|greeter
argument_list|,
operator|(
name|String
operator|)
literal|null
argument_list|)
decl_stmt|;
name|WebServiceContext
name|ctx
init|=
name|greeter
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
try|try
block|{
name|endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|ex
operator|.
name|getCause
argument_list|()
operator|instanceof
name|BusException
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"BINDING_INCOMPATIBLE_ADDRESS_EXC"
argument_list|,
operator|(
operator|(
name|BusException
operator|)
name|ex
operator|.
name|getCause
argument_list|()
operator|)
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ctx
operator|=
name|greeter
operator|.
name|getContext
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
comment|// Test that we can't change settings through the JAX-WS API after publishing
try|try
block|{
name|endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"republished an already published endpoint."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|e
parameter_list|)
block|{
comment|// expected
block|}
try|try
block|{
name|endpoint
operator|.
name|setMetadata
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|Source
argument_list|>
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"set metadata on an already published endpoint."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEndpointStop
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|address
init|=
literal|"http://localhost:8080/test"
decl_stmt|;
name|GreeterImpl
name|greeter
init|=
operator|new
name|GreeterImpl
argument_list|()
decl_stmt|;
name|EndpointImpl
name|endpoint
init|=
operator|new
name|EndpointImpl
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|greeter
argument_list|,
operator|(
name|String
operator|)
literal|null
argument_list|)
decl_stmt|;
name|WebServiceContext
name|ctx
init|=
name|greeter
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
try|try
block|{
name|endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|ex
operator|.
name|getCause
argument_list|()
operator|instanceof
name|BusException
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"BINDING_INCOMPATIBLE_ADDRESS_EXC"
argument_list|,
operator|(
operator|(
name|BusException
operator|)
name|ex
operator|.
name|getCause
argument_list|()
operator|)
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ctx
operator|=
name|greeter
operator|.
name|getContext
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
comment|// Test that calling stop on the Endpoint works
name|assertTrue
argument_list|(
name|endpoint
operator|.
name|isPublished
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|endpoint
operator|.
name|isPublished
argument_list|()
argument_list|)
expr_stmt|;
comment|// Test that the Endpoint cannot be restarted.
try|try
block|{
name|endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"stopped endpoint restarted."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|e
parameter_list|)
block|{
comment|// expected.
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEndpointServiceConstructor
parameter_list|()
throws|throws
name|Exception
block|{
name|GreeterImpl
name|greeter
init|=
operator|new
name|GreeterImpl
argument_list|()
decl_stmt|;
name|JaxWsServiceFactoryBean
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
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|serviceFactory
operator|.
name|setInvoker
argument_list|(
operator|new
name|BeanInvoker
argument_list|(
name|greeter
argument_list|)
argument_list|)
expr_stmt|;
name|serviceFactory
operator|.
name|setServiceClass
argument_list|(
name|GreeterImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|EndpointImpl
name|endpoint
init|=
operator|new
name|EndpointImpl
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|greeter
argument_list|,
operator|new
name|JaxWsServerFactoryBean
argument_list|(
name|serviceFactory
argument_list|)
argument_list|)
decl_stmt|;
name|WebServiceContext
name|ctx
init|=
name|greeter
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
try|try
block|{
name|String
name|address
init|=
literal|"http://localhost:8080/test"
decl_stmt|;
name|endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|ex
operator|.
name|getCause
argument_list|()
operator|instanceof
name|BusException
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"BINDING_INCOMPATIBLE_ADDRESS_EXC"
argument_list|,
operator|(
operator|(
name|BusException
operator|)
name|ex
operator|.
name|getCause
argument_list|()
operator|)
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ctx
operator|=
name|greeter
operator|.
name|getContext
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWSAnnoWithoutWSDLLocationInSEI
parameter_list|()
throws|throws
name|Exception
block|{
name|HelloImpl
name|hello
init|=
operator|new
name|HelloImpl
argument_list|()
decl_stmt|;
name|JaxWsServiceFactoryBean
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
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|serviceFactory
operator|.
name|setInvoker
argument_list|(
operator|new
name|BeanInvoker
argument_list|(
name|hello
argument_list|)
argument_list|)
expr_stmt|;
name|serviceFactory
operator|.
name|setServiceClass
argument_list|(
name|HelloImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|EndpointImpl
name|endpoint
init|=
operator|new
name|EndpointImpl
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|hello
argument_list|,
operator|new
name|JaxWsServerFactoryBean
argument_list|(
name|serviceFactory
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|String
name|address
init|=
literal|"http://localhost:8080/test"
decl_stmt|;
name|endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|ex
operator|.
name|getCause
argument_list|()
operator|instanceof
name|BusException
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"BINDING_INCOMPATIBLE_ADDRESS_EXC"
argument_list|,
operator|(
operator|(
name|BusException
operator|)
name|ex
operator|.
name|getCause
argument_list|()
operator|)
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSOAPBindingOnMethodWithRPC
parameter_list|()
block|{
name|HelloWrongAnnotation
name|hello
init|=
operator|new
name|HelloWrongAnnotation
argument_list|()
decl_stmt|;
name|JaxWsServiceFactoryBean
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
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|serviceFactory
operator|.
name|setInvoker
argument_list|(
operator|new
name|BeanInvoker
argument_list|(
name|hello
argument_list|)
argument_list|)
expr_stmt|;
name|serviceFactory
operator|.
name|setServiceClass
argument_list|(
name|HelloWrongAnnotation
operator|.
name|class
argument_list|)
expr_stmt|;
try|try
block|{
operator|new
name|EndpointImpl
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|hello
argument_list|,
operator|new
name|JaxWsServerFactoryBean
argument_list|(
name|serviceFactory
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|String
name|expeced
init|=
literal|"Method [sayHi] processing error: SOAPBinding can not on method with RPC style"
decl_stmt|;
name|assertEquals
argument_list|(
name|expeced
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPublishEndpointPermission
parameter_list|()
throws|throws
name|Exception
block|{
name|Hello
name|service
init|=
operator|new
name|Hello
argument_list|()
decl_stmt|;
name|EndpointImpl
name|ep
init|=
operator|new
name|EndpointImpl
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|service
argument_list|,
operator|(
name|String
operator|)
literal|null
argument_list|)
decl_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
name|EndpointImpl
operator|.
name|CHECK_PUBLISH_ENDPOINT_PERMISSON_PROPERTY
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
try|try
block|{
name|ep
operator|.
name|publish
argument_list|(
literal|"local://localhost:9090/hello"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Did not throw exception as expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SecurityException
name|e
parameter_list|)
block|{
comment|// that's expected
block|}
finally|finally
block|{
name|System
operator|.
name|setProperty
argument_list|(
name|EndpointImpl
operator|.
name|CHECK_PUBLISH_ENDPOINT_PERMISSON_PROPERTY
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
block|}
name|ep
operator|.
name|publish
argument_list|(
literal|"local://localhost:9090/hello"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddWSAFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|GreeterImpl
name|greeter
init|=
operator|new
name|GreeterImpl
argument_list|()
decl_stmt|;
name|JaxWsServiceFactoryBean
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
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|serviceFactory
operator|.
name|setInvoker
argument_list|(
operator|new
name|BeanInvoker
argument_list|(
name|greeter
argument_list|)
argument_list|)
expr_stmt|;
name|serviceFactory
operator|.
name|setServiceClass
argument_list|(
name|GreeterImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|EndpointImpl
name|endpoint
init|=
operator|new
name|EndpointImpl
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|greeter
argument_list|,
operator|new
name|JaxWsServerFactoryBean
argument_list|(
name|serviceFactory
argument_list|)
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|getFeatures
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|WSAddressingFeature
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|String
name|address
init|=
literal|"http://localhost:8080/test"
decl_stmt|;
name|endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|ex
operator|.
name|getCause
argument_list|()
operator|instanceof
name|BusException
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"BINDING_INCOMPATIBLE_ADDRESS_EXC"
argument_list|,
operator|(
operator|(
name|BusException
operator|)
name|ex
operator|.
name|getCause
argument_list|()
operator|)
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|serviceFactory
operator|.
name|getFeatures
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|serviceFactory
operator|.
name|getFeatures
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|WSAddressingFeature
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJaxWsaFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|HelloWsa
name|greeter
init|=
operator|new
name|HelloWsa
argument_list|()
decl_stmt|;
name|JaxWsServiceFactoryBean
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
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|serviceFactory
operator|.
name|setInvoker
argument_list|(
operator|new
name|BeanInvoker
argument_list|(
name|greeter
argument_list|)
argument_list|)
expr_stmt|;
name|serviceFactory
operator|.
name|setServiceClass
argument_list|(
name|HelloWsa
operator|.
name|class
argument_list|)
expr_stmt|;
name|EndpointImpl
name|endpoint
init|=
operator|new
name|EndpointImpl
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|greeter
argument_list|,
operator|new
name|JaxWsServerFactoryBean
argument_list|(
name|serviceFactory
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|String
name|address
init|=
literal|"http://localhost:8080/test"
decl_stmt|;
name|endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|ex
operator|.
name|getCause
argument_list|()
operator|instanceof
name|BusException
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"BINDING_INCOMPATIBLE_ADDRESS_EXC"
argument_list|,
operator|(
operator|(
name|BusException
operator|)
name|ex
operator|.
name|getCause
argument_list|()
operator|)
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|serviceFactory
operator|.
name|getFeatures
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|serviceFactory
operator|.
name|getFeatures
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|WSAddressingFeature
argument_list|)
expr_stmt|;
block|}
specifier|static
class|class
name|EchoObserver
implements|implements
name|MessageObserver
block|{
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
try|try
block|{
name|Conduit
name|backChannel
init|=
name|message
operator|.
name|getDestination
argument_list|()
operator|.
name|getBackChannel
argument_list|(
name|message
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|backChannel
operator|.
name|prepare
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|OutputStream
name|out
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
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|InputStream
name|in
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
name|assertNotNull
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|copy
argument_list|(
name|in
argument_list|,
name|out
argument_list|,
literal|2045
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
name|in
operator|.
name|close
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
block|}
block|}
block|}
specifier|private
specifier|static
name|void
name|copy
parameter_list|(
specifier|final
name|InputStream
name|input
parameter_list|,
specifier|final
name|OutputStream
name|output
parameter_list|,
specifier|final
name|int
name|bufferSize
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
specifier|final
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
name|bufferSize
index|]
decl_stmt|;
name|int
name|n
init|=
name|input
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
while|while
condition|(
operator|-
literal|1
operator|!=
name|n
condition|)
block|{
name|output
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|n
argument_list|)
expr_stmt|;
name|n
operator|=
name|input
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|input
operator|.
name|close
argument_list|()
expr_stmt|;
name|output
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

