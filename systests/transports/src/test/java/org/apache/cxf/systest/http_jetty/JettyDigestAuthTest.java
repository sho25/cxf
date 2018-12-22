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
name|http_jetty
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
name|WebServiceException
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
name|configuration
operator|.
name|security
operator|.
name|AuthorizationPolicy
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
name|CastUtils
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
name|Fault
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
name|endpoint
operator|.
name|dynamic
operator|.
name|JaxWsDynamicClientFactory
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
name|phase
operator|.
name|AbstractPhaseInterceptor
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
name|phase
operator|.
name|Phase
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
name|AbstractClientServerTestBase
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
name|transport
operator|.
name|http
operator|.
name|HTTPConduitConfigurer
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
name|asyncclient
operator|.
name|AsyncHTTPConduit
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
name|auth
operator|.
name|DigestAuthSupplier
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
name|transports
operator|.
name|http
operator|.
name|configuration
operator|.
name|HTTPClientPolicy
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
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|SOAPService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|auth
operator|.
name|Credentials
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|auth
operator|.
name|UsernamePasswordCredentials
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_comment
comment|/**  * Tests thread pool config.  */
end_comment

begin_class
specifier|public
class|class
name|JettyDigestAuthTest
extends|extends
name|AbstractClientServerTestBase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|PORT
init|=
name|allocatePort
argument_list|(
name|JettyDigestAuthTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ADDRESS
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext/SoapPort"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|SERVICE_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"SOAPServiceAddressing"
argument_list|)
decl_stmt|;
specifier|private
name|Greeter
name|greeter
decl_stmt|;
specifier|public
specifier|static
class|class
name|JettyDigestServer
extends|extends
name|AbstractBusTestServerBase
block|{
name|Endpoint
name|ep
decl_stmt|;
specifier|protected
name|void
name|run
parameter_list|()
block|{
name|String
name|configurationFile
init|=
literal|"jettyDigestServer.xml"
decl_stmt|;
name|URL
name|configure
init|=
name|JettyBasicAuthServer
operator|.
name|class
operator|.
name|getResource
argument_list|(
name|configurationFile
argument_list|)
decl_stmt|;
name|Bus
name|bus
init|=
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|(
name|configure
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|bus
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
name|bus
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
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|GreeterImpl
name|implementor
init|=
operator|new
name|GreeterImpl
argument_list|()
decl_stmt|;
name|ep
operator|=
name|Endpoint
operator|.
name|publish
argument_list|(
name|ADDRESS
argument_list|,
name|implementor
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|ep
operator|!=
literal|null
condition|)
block|{
name|ep
operator|.
name|stop
argument_list|()
expr_stmt|;
name|ep
operator|=
literal|null
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
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|JettyDigestServer
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|HTTPConduit
name|setupClient
parameter_list|(
name|boolean
name|async
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
literal|"/wsdl/hello_world.wsdl"
argument_list|)
decl_stmt|;
name|greeter
operator|=
operator|new
name|SOAPService
argument_list|(
name|wsdl
argument_list|,
name|SERVICE_NAME
argument_list|)
operator|.
name|getPort
argument_list|(
name|Greeter
operator|.
name|class
argument_list|)
expr_stmt|;
name|BindingProvider
name|bp
init|=
operator|(
name|BindingProvider
operator|)
name|greeter
decl_stmt|;
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|greeter
argument_list|)
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
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|greeter
argument_list|)
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
name|bp
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|,
name|ADDRESS
argument_list|)
expr_stmt|;
name|HTTPConduit
name|cond
init|=
operator|(
name|HTTPConduit
operator|)
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|greeter
argument_list|)
operator|.
name|getConduit
argument_list|()
decl_stmt|;
name|HTTPClientPolicy
name|client
init|=
operator|new
name|HTTPClientPolicy
argument_list|()
decl_stmt|;
name|cond
operator|.
name|setClient
argument_list|(
name|client
argument_list|)
expr_stmt|;
if|if
condition|(
name|async
condition|)
block|{
if|if
condition|(
name|cond
operator|instanceof
name|AsyncHTTPConduit
condition|)
block|{
name|UsernamePasswordCredentials
name|creds
init|=
operator|new
name|UsernamePasswordCredentials
argument_list|(
literal|"ffang"
argument_list|,
literal|"pswd"
argument_list|)
decl_stmt|;
name|bp
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|Credentials
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|creds
argument_list|)
expr_stmt|;
name|bp
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|AsyncHTTPConduit
operator|.
name|USE_ASYNC
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|client
operator|.
name|setAutoRedirect
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fail
argument_list|(
literal|"Not an async conduit"
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|bp
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|USERNAME_PROPERTY
argument_list|,
literal|"ffang"
argument_list|)
expr_stmt|;
name|bp
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|PASSWORD_PROPERTY
argument_list|,
literal|"pswd"
argument_list|)
expr_stmt|;
name|cond
operator|.
name|setAuthSupplier
argument_list|(
operator|new
name|DigestAuthSupplier
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|greeter
argument_list|)
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
argument_list|(
name|Phase
operator|.
name|PRE_STREAM_ENDING
argument_list|)
block|{
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|headers
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|headers
operator|.
name|containsKey
argument_list|(
literal|"Proxy-Authorization"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Should not have Proxy-Authorization"
argument_list|)
throw|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|client
operator|.
name|setAllowChunking
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return
name|cond
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDigestAuth
parameter_list|()
throws|throws
name|Exception
block|{
comment|//CXF will handle the auth stuff within it's conduit implementation
name|doTest
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDigestAuthAsyncClient
parameter_list|()
throws|throws
name|Exception
block|{
comment|//We'll let HTTP async handle it.  Useful for things like NTLM
comment|//which async client can handle but we cannot.
name|doTest
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doTest
parameter_list|(
name|boolean
name|async
parameter_list|)
throws|throws
name|Exception
block|{
name|setupClient
argument_list|(
name|async
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Alice"
argument_list|,
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"Alice"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Bob"
argument_list|,
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"Bob"
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|BindingProvider
name|bp
init|=
operator|(
name|BindingProvider
operator|)
name|greeter
decl_stmt|;
if|if
condition|(
name|async
condition|)
block|{
name|UsernamePasswordCredentials
name|creds
init|=
operator|new
name|UsernamePasswordCredentials
argument_list|(
literal|"blah"
argument_list|,
literal|"foo"
argument_list|)
decl_stmt|;
name|bp
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|Credentials
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|creds
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|bp
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|USERNAME_PROPERTY
argument_list|,
literal|"blah"
argument_list|)
expr_stmt|;
name|bp
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|PASSWORD_PROPERTY
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
block|}
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"Alice"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Password was wrong, should have failed"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebServiceException
name|wse
parameter_list|)
block|{
comment|//ignore - expected
block|}
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testGetWSDL
parameter_list|()
throws|throws
name|Exception
block|{
name|BusFactory
name|bf
init|=
name|BusFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
name|bf
operator|.
name|createBus
argument_list|()
decl_stmt|;
name|bus
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
name|bus
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
name|MyHTTPConduitConfigurer
name|myHttpConduitConfig
init|=
operator|new
name|MyHTTPConduitConfigurer
argument_list|()
decl_stmt|;
name|bus
operator|.
name|setExtension
argument_list|(
name|myHttpConduitConfig
argument_list|,
name|HTTPConduitConfigurer
operator|.
name|class
argument_list|)
expr_stmt|;
name|JaxWsDynamicClientFactory
name|factory
init|=
name|JaxWsDynamicClientFactory
operator|.
name|newInstance
argument_list|(
name|bus
argument_list|)
decl_stmt|;
name|factory
operator|.
name|createClient
argument_list|(
name|ADDRESS
operator|+
literal|"?wsdl"
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
class|class
name|MyHTTPConduitConfigurer
implements|implements
name|HTTPConduitConfigurer
block|{
specifier|public
name|void
name|configure
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|address
parameter_list|,
name|HTTPConduit
name|c
parameter_list|)
block|{
name|AuthorizationPolicy
name|authorizationPolicy
init|=
operator|new
name|AuthorizationPolicy
argument_list|()
decl_stmt|;
name|authorizationPolicy
operator|.
name|setUserName
argument_list|(
literal|"ffang"
argument_list|)
expr_stmt|;
name|authorizationPolicy
operator|.
name|setPassword
argument_list|(
literal|"pswd"
argument_list|)
expr_stmt|;
name|authorizationPolicy
operator|.
name|setAuthorizationType
argument_list|(
literal|"Digest"
argument_list|)
expr_stmt|;
name|c
operator|.
name|setAuthorization
argument_list|(
name|authorizationPolicy
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

