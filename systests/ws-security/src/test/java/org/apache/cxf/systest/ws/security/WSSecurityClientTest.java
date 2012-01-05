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
name|security
package|;
end_package

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
name|List
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
name|transform
operator|.
name|OutputKeys
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
name|transform
operator|.
name|Transformer
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
name|TransformerFactory
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
name|stream
operator|.
name|StreamResult
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
name|stream
operator|.
name|StreamSource
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
name|Dispatch
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
name|Service
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
name|handler
operator|.
name|Handler
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
name|handler
operator|.
name|MessageContext
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
name|http
operator|.
name|HTTPBinding
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
name|AddressingFeature
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
name|jaxws
operator|.
name|DispatchImpl
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
name|transport
operator|.
name|http
operator|.
name|HTTPConduit
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
name|security
operator|.
name|wss4j
operator|.
name|WSS4JOutInterceptor
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
name|Greeter
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
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|WSSecurityClientTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|allocatePort
argument_list|(
name|Server
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DEC_PORT
init|=
name|allocatePort
argument_list|(
name|WSSecurityClientTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|java
operator|.
name|net
operator|.
name|URL
name|WSDL_LOC
decl_stmt|;
static|static
block|{
name|java
operator|.
name|net
operator|.
name|URL
name|tmp
init|=
literal|null
decl_stmt|;
try|try
block|{
name|tmp
operator|=
name|WSSecurityClientTest
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"org/apache/cxf/systest/ws/security/hello_world.wsdl"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
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
name|WSDL_LOC
operator|=
name|tmp
expr_stmt|;
block|}
specifier|private
specifier|static
specifier|final
name|QName
name|GREETER_SERVICE_QNAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"GreeterService"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|TIMESTAMP_SIGN_ENCRYPT_PORT_QNAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"TimestampSignEncryptPort"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|USERNAME_TOKEN_PORT_QNAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"UsernameTokenPort"
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
name|assertTrue
argument_list|(
literal|"Server failed to launch"
argument_list|,
comment|// run the server in the same process
comment|// set this to false to fork
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
name|Test
specifier|public
name|void
name|testUsernameToken
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Service
name|svc
init|=
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Service
operator|.
name|create
argument_list|(
name|WSDL_LOC
argument_list|,
name|GREETER_SERVICE_QNAME
argument_list|)
decl_stmt|;
specifier|final
name|Greeter
name|greeter
init|=
name|svc
operator|.
name|getPort
argument_list|(
name|USERNAME_TOKEN_PORT_QNAME
argument_list|,
name|Greeter
operator|.
name|class
argument_list|)
decl_stmt|;
name|updateAddressPort
argument_list|(
name|greeter
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|Client
name|client
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|greeter
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"action"
argument_list|,
literal|"UsernameToken"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"user"
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|WSS4JOutInterceptor
name|wss4jOut
init|=
operator|new
name|WSS4JOutInterceptor
argument_list|(
name|props
argument_list|)
decl_stmt|;
name|client
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|wss4jOut
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|greeter
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"password"
argument_list|,
literal|"password"
argument_list|)
expr_stmt|;
name|String
name|s
init|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"CXF"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello CXF"
argument_list|,
name|s
argument_list|)
expr_stmt|;
try|try
block|{
operator|(
operator|(
name|BindingProvider
operator|)
name|greeter
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"password"
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"CXF"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"should fail"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//expected
block|}
try|try
block|{
name|props
operator|.
name|put
argument_list|(
literal|"passwordType"
argument_list|,
literal|"PasswordText"
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|greeter
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"password"
argument_list|,
literal|"password"
argument_list|)
expr_stmt|;
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"CXF"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"should fail"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//expected
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTimestampSignEncrypt
parameter_list|()
throws|throws
name|Exception
block|{
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|(
literal|"org/apache/cxf/systest/ws/security/client.xml"
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Service
name|svc
init|=
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Service
operator|.
name|create
argument_list|(
name|WSDL_LOC
argument_list|,
name|GREETER_SERVICE_QNAME
argument_list|)
decl_stmt|;
specifier|final
name|Greeter
name|greeter
init|=
name|svc
operator|.
name|getPort
argument_list|(
name|TIMESTAMP_SIGN_ENCRYPT_PORT_QNAME
argument_list|,
name|Greeter
operator|.
name|class
argument_list|)
decl_stmt|;
name|updateAddressPort
argument_list|(
name|greeter
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
comment|// Add a No-Op JAX-WS SoapHandler to the dispatch chain to
comment|// verify that the SoapHandlerInterceptor can peacefully co-exist
comment|// with the explicitly configured SAAJOutInterceptor
comment|//
name|List
argument_list|<
name|Handler
argument_list|>
name|handlerChain
init|=
operator|new
name|ArrayList
argument_list|<
name|Handler
argument_list|>
argument_list|()
decl_stmt|;
name|Binding
name|binding
init|=
operator|(
operator|(
name|BindingProvider
operator|)
name|greeter
operator|)
operator|.
name|getBinding
argument_list|()
decl_stmt|;
name|TestOutHandler
name|handler
init|=
operator|new
name|TestOutHandler
argument_list|()
decl_stmt|;
name|handlerChain
operator|.
name|add
argument_list|(
name|handler
argument_list|)
expr_stmt|;
name|binding
operator|.
name|setHandlerChain
argument_list|(
name|handlerChain
argument_list|)
expr_stmt|;
name|greeter
operator|.
name|sayHi
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"expected Handler.handleMessage() to be called"
argument_list|,
name|handler
operator|.
name|handleMessageCalledOutbound
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"expected Handler.handleFault() not to be called"
argument_list|,
name|handler
operator|.
name|handleFaultCalledOutbound
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMalformedSecurityHeaders
parameter_list|()
throws|throws
name|java
operator|.
name|lang
operator|.
name|Exception
block|{
name|Dispatch
argument_list|<
name|Source
argument_list|>
name|dispatcher
init|=
literal|null
decl_stmt|;
name|java
operator|.
name|io
operator|.
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
name|String
name|result
init|=
literal|null
decl_stmt|;
comment|//
comment|// Check to ensure that a well-formed request will pass
comment|//
name|dispatcher
operator|=
name|createUsernameTokenDispatcher
argument_list|()
expr_stmt|;
name|is
operator|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"test-data/UsernameTokenRequest.xml"
argument_list|)
expr_stmt|;
name|result
operator|=
name|source2String
argument_list|(
name|dispatcher
operator|.
name|invoke
argument_list|(
operator|new
name|StreamSource
argument_list|(
name|is
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|indexOf
argument_list|(
literal|"Bonjour"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
comment|//make sure the principal was set
name|assertNotNull
argument_list|(
name|GreeterImpl
operator|.
name|getUser
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"alice"
argument_list|,
name|GreeterImpl
operator|.
name|getUser
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|//
comment|// Sending no security headers should result in a Fault
comment|//
name|dispatcher
operator|=
name|createUsernameTokenDispatcher
argument_list|()
expr_stmt|;
name|is
operator|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"test-data/NoHeadersRequest.xml"
argument_list|)
expr_stmt|;
name|result
operator|=
name|source2String
argument_list|(
name|dispatcher
operator|.
name|invoke
argument_list|(
operator|new
name|StreamSource
argument_list|(
name|is
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|indexOf
argument_list|(
literal|"Fault"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
comment|//
comment|// Sending and empty header should result in a Fault
comment|//
name|dispatcher
operator|=
name|createUsernameTokenDispatcher
argument_list|()
expr_stmt|;
name|is
operator|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"test-data/EmptyHeaderRequest.xml"
argument_list|)
expr_stmt|;
name|result
operator|=
name|source2String
argument_list|(
name|dispatcher
operator|.
name|invoke
argument_list|(
operator|new
name|StreamSource
argument_list|(
name|is
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|indexOf
argument_list|(
literal|"Fault"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
comment|//
comment|// Sending and empty security header should result in a Fault
comment|//
name|dispatcher
operator|=
name|createUsernameTokenDispatcher
argument_list|()
expr_stmt|;
name|is
operator|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"test-data/EmptySecurityHeaderRequest.xml"
argument_list|)
expr_stmt|;
name|result
operator|=
name|source2String
argument_list|(
name|dispatcher
operator|.
name|invoke
argument_list|(
operator|new
name|StreamSource
argument_list|(
name|is
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|indexOf
argument_list|(
literal|"Fault"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDecoupledFaultFromSecurity
parameter_list|()
throws|throws
name|Exception
block|{
name|Dispatch
argument_list|<
name|Source
argument_list|>
name|dispatcher
init|=
literal|null
decl_stmt|;
name|java
operator|.
name|io
operator|.
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
comment|//
comment|// Sending no security headers should result in a Fault
comment|//
name|dispatcher
operator|=
name|createUsernameTokenDispatcher
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|is
operator|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"test-data/NoHeadersRequest.xml"
argument_list|)
expr_stmt|;
try|try
block|{
name|dispatcher
operator|.
name|invoke
argument_list|(
operator|new
name|StreamSource
argument_list|(
name|is
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"exception should have been generated"
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
literal|"Security"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|//
comment|// Sending and empty header should result in a Fault
comment|//
name|dispatcher
operator|=
name|createUsernameTokenDispatcher
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|is
operator|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"test-data/EmptyHeaderRequest.xml"
argument_list|)
expr_stmt|;
try|try
block|{
name|dispatcher
operator|.
name|invoke
argument_list|(
operator|new
name|StreamSource
argument_list|(
name|is
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"exception should have been generated"
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
literal|"Security"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|//
comment|// Sending and empty security header should result in a Fault
comment|//
name|dispatcher
operator|=
name|createUsernameTokenDispatcher
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|is
operator|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"test-data/EmptySecurityHeaderRequest.xml"
argument_list|)
expr_stmt|;
try|try
block|{
name|dispatcher
operator|.
name|invoke
argument_list|(
operator|new
name|StreamSource
argument_list|(
name|is
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"exception should have been generated"
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
literal|"Security"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|Dispatch
argument_list|<
name|Source
argument_list|>
name|createUsernameTokenDispatcher
parameter_list|()
block|{
return|return
name|createUsernameTokenDispatcher
argument_list|(
literal|false
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|Dispatch
argument_list|<
name|Source
argument_list|>
name|createUsernameTokenDispatcher
parameter_list|(
name|boolean
name|decoupled
parameter_list|)
block|{
specifier|final
name|Service
name|service
init|=
name|Service
operator|.
name|create
argument_list|(
name|GREETER_SERVICE_QNAME
argument_list|)
decl_stmt|;
name|service
operator|.
name|addPort
argument_list|(
name|USERNAME_TOKEN_PORT_QNAME
argument_list|,
name|decoupled
condition|?
name|SOAPBinding
operator|.
name|SOAP11HTTP_BINDING
else|:
name|HTTPBinding
operator|.
name|HTTP_BINDING
argument_list|,
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/GreeterService/UsernameTokenPort"
argument_list|)
expr_stmt|;
specifier|final
name|Dispatch
argument_list|<
name|Source
argument_list|>
name|dispatcher
init|=
name|service
operator|.
name|createDispatch
argument_list|(
name|USERNAME_TOKEN_PORT_QNAME
argument_list|,
name|Source
operator|.
name|class
argument_list|,
name|Service
operator|.
name|Mode
operator|.
name|MESSAGE
argument_list|,
operator|new
name|AddressingFeature
argument_list|(
name|decoupled
argument_list|,
name|decoupled
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|java
operator|.
name|util
operator|.
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|requestContext
init|=
name|dispatcher
operator|.
name|getRequestContext
argument_list|()
decl_stmt|;
name|requestContext
operator|.
name|put
argument_list|(
name|MessageContext
operator|.
name|HTTP_REQUEST_METHOD
argument_list|,
literal|"POST"
argument_list|)
expr_stmt|;
if|if
condition|(
name|decoupled
condition|)
block|{
name|HTTPConduit
name|cond
init|=
call|(
name|HTTPConduit
call|)
argument_list|(
operator|(
name|DispatchImpl
argument_list|<
name|?
argument_list|>
operator|)
name|dispatcher
argument_list|)
operator|.
name|getClient
argument_list|()
operator|.
name|getConduit
argument_list|()
decl_stmt|;
name|cond
operator|.
name|getClient
argument_list|()
operator|.
name|setDecoupledEndpoint
argument_list|(
literal|"http://localhost:"
operator|+
name|DEC_PORT
operator|+
literal|"/decoupled"
argument_list|)
expr_stmt|;
block|}
return|return
name|dispatcher
return|;
block|}
specifier|private
specifier|static
name|String
name|source2String
parameter_list|(
name|Source
name|source
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
specifier|final
name|StreamResult
name|sr
init|=
operator|new
name|StreamResult
argument_list|(
name|bos
argument_list|)
decl_stmt|;
specifier|final
name|Transformer
name|trans
init|=
name|TransformerFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newTransformer
argument_list|()
decl_stmt|;
specifier|final
name|java
operator|.
name|util
operator|.
name|Properties
name|oprops
init|=
operator|new
name|java
operator|.
name|util
operator|.
name|Properties
argument_list|()
decl_stmt|;
name|oprops
operator|.
name|put
argument_list|(
name|OutputKeys
operator|.
name|OMIT_XML_DECLARATION
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
name|trans
operator|.
name|setOutputProperties
argument_list|(
name|oprops
argument_list|)
expr_stmt|;
name|trans
operator|.
name|transform
argument_list|(
name|source
argument_list|,
name|sr
argument_list|)
expr_stmt|;
return|return
name|bos
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

