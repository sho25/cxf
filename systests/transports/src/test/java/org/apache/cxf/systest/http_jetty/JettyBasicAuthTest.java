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
name|CXFBusFactory
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
name|BeforeClass
import|;
end_import

begin_comment
comment|/**  * Tests thread pool config.  */
end_comment

begin_class
specifier|public
class|class
name|JettyBasicAuthTest
extends|extends
name|AbstractClientServerTestBase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|ADDRESS
init|=
name|JettyBasicAuthServer
operator|.
name|ADDRESS
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
name|JettyBasicAuthServer
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
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
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testBasicAuth
parameter_list|()
throws|throws
name|Exception
block|{
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
name|CXFBusFactory
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
literal|"Basic"
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

