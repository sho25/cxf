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
name|sts
operator|.
name|username_actas
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
name|systest
operator|.
name|sts
operator|.
name|common
operator|.
name|TokenTestUtils
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
name|systest
operator|.
name|sts
operator|.
name|deployment
operator|.
name|STSServer
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
name|ws
operator|.
name|security
operator|.
name|SecurityConstants
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
name|tokenstore
operator|.
name|MemoryTokenStore
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
name|tokenstore
operator|.
name|TokenStore
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
name|trust
operator|.
name|STSClient
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
name|trust
operator|.
name|delegation
operator|.
name|WSSUsernameCallbackHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|example
operator|.
name|contract
operator|.
name|doubleit
operator|.
name|DoubleItPortType
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
comment|/**  * In this test case, a CXF client requests a Security Token from an STS, passing a username that  * it has obtained from an unknown client as an "ActAs" element. This username is obtained  * by parsing the "ws-security.username" property. The client then invokes on the service   * provider using the returned token from the STS.   */
end_comment

begin_class
specifier|public
class|class
name|UsernameActAsTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|static
specifier|final
name|String
name|STSPORT
init|=
name|allocatePort
argument_list|(
name|STSServer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|String
name|STSPORT2
init|=
name|allocatePort
argument_list|(
name|STSServer
operator|.
name|class
argument_list|,
literal|2
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NAMESPACE
init|=
literal|"http://www.example.org/contract/DoubleIt"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|SERVICE_QNAME
init|=
operator|new
name|QName
argument_list|(
name|NAMESPACE
argument_list|,
literal|"DoubleItService"
argument_list|)
decl_stmt|;
specifier|private
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
specifier|private
specifier|static
name|boolean
name|standalone
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
name|String
name|deployment
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"sts.deployment"
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"standalone"
operator|.
name|equals
argument_list|(
name|deployment
argument_list|)
condition|)
block|{
name|standalone
operator|=
literal|true
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Server failed to launch"
argument_list|,
comment|// run the server in the same process
comment|// set this to false to fork
name|launchServer
argument_list|(
name|STSServer
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
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
name|testUsernameActAs
parameter_list|()
throws|throws
name|Exception
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|URL
name|busFile
init|=
name|UsernameActAsTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"cxf-client.xml"
argument_list|)
decl_stmt|;
name|Bus
name|bus
init|=
name|bf
operator|.
name|createBus
argument_list|(
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|SpringBusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|SpringBusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|URL
name|wsdl
init|=
name|UsernameActAsTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleIt.wsdl"
argument_list|)
decl_stmt|;
name|Service
name|service
init|=
name|Service
operator|.
name|create
argument_list|(
name|wsdl
argument_list|,
name|SERVICE_QNAME
argument_list|)
decl_stmt|;
name|QName
name|portQName
init|=
operator|new
name|QName
argument_list|(
name|NAMESPACE
argument_list|,
literal|"DoubleItAsymmetricSAML2BearerPort"
argument_list|)
decl_stmt|;
name|DoubleItPortType
name|bearerPort
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portQName
argument_list|,
name|DoubleItPortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|updateAddressPort
argument_list|(
name|bearerPort
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
if|if
condition|(
name|standalone
condition|)
block|{
name|TokenTestUtils
operator|.
name|updateSTSPort
argument_list|(
operator|(
name|BindingProvider
operator|)
name|bearerPort
argument_list|,
name|STSPORT2
argument_list|)
expr_stmt|;
block|}
comment|// Transport port
operator|(
operator|(
name|BindingProvider
operator|)
name|bearerPort
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.username"
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|doubleIt
argument_list|(
name|bearerPort
argument_list|,
literal|25
argument_list|)
expr_stmt|;
name|DoubleItPortType
name|bearerPort2
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portQName
argument_list|,
name|DoubleItPortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|updateAddressPort
argument_list|(
name|bearerPort2
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
if|if
condition|(
name|standalone
condition|)
block|{
name|TokenTestUtils
operator|.
name|updateSTSPort
argument_list|(
operator|(
name|BindingProvider
operator|)
name|bearerPort2
argument_list|,
name|STSPORT2
argument_list|)
expr_stmt|;
block|}
operator|(
operator|(
name|BindingProvider
operator|)
name|bearerPort2
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.username"
argument_list|,
literal|"eve"
argument_list|)
expr_stmt|;
comment|// This time we expect a failure as the server validator doesn't accept "eve".
try|try
block|{
name|doubleIt
argument_list|(
name|bearerPort2
argument_list|,
literal|30
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on an unknown user"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// expected
block|}
block|}
comment|/**      * Test caching the issued token      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testUsernameActAsCaching
parameter_list|()
throws|throws
name|Exception
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|URL
name|busFile
init|=
name|UsernameActAsTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"cxf-client.xml"
argument_list|)
decl_stmt|;
name|Bus
name|bus
init|=
name|bf
operator|.
name|createBus
argument_list|(
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|SpringBusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|SpringBusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|URL
name|wsdl
init|=
name|UsernameActAsTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleIt.wsdl"
argument_list|)
decl_stmt|;
name|Service
name|service
init|=
name|Service
operator|.
name|create
argument_list|(
name|wsdl
argument_list|,
name|SERVICE_QNAME
argument_list|)
decl_stmt|;
name|QName
name|portQName
init|=
operator|new
name|QName
argument_list|(
name|NAMESPACE
argument_list|,
literal|"DoubleItAsymmetricSAML2BearerPort"
argument_list|)
decl_stmt|;
comment|//
comment|// Proxy no. 1
comment|//
name|DoubleItPortType
name|bearerPort
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portQName
argument_list|,
name|DoubleItPortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|updateAddressPort
argument_list|(
name|bearerPort
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
if|if
condition|(
name|standalone
condition|)
block|{
name|TokenTestUtils
operator|.
name|updateSTSPort
argument_list|(
operator|(
name|BindingProvider
operator|)
name|bearerPort
argument_list|,
name|STSPORT2
argument_list|)
expr_stmt|;
block|}
name|TokenStore
name|tokenStore
init|=
operator|new
name|MemoryTokenStore
argument_list|()
decl_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|bearerPort
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|TokenStore
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|tokenStore
argument_list|)
expr_stmt|;
comment|// Make a successful invocation
operator|(
operator|(
name|BindingProvider
operator|)
name|bearerPort
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.username"
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|doubleIt
argument_list|(
name|bearerPort
argument_list|,
literal|25
argument_list|)
expr_stmt|;
comment|// Change the STSClient so that it can no longer find the STS
name|STSClient
name|stsClient
init|=
operator|new
name|STSClient
argument_list|(
name|bus
argument_list|)
decl_stmt|;
name|stsClient
operator|.
name|setOnBehalfOf
argument_list|(
operator|new
name|WSSUsernameCallbackHandler
argument_list|()
argument_list|)
expr_stmt|;
name|BindingProvider
name|p
init|=
operator|(
name|BindingProvider
operator|)
name|bearerPort
decl_stmt|;
name|p
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|STS_CLIENT
argument_list|,
name|stsClient
argument_list|)
expr_stmt|;
comment|// This invocation should be successful as the token is cached
name|doubleIt
argument_list|(
name|bearerPort
argument_list|,
literal|25
argument_list|)
expr_stmt|;
comment|//
comment|// Proxy no. 2
comment|//
name|DoubleItPortType
name|bearerPort2
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portQName
argument_list|,
name|DoubleItPortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|updateAddressPort
argument_list|(
name|bearerPort2
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
if|if
condition|(
name|standalone
condition|)
block|{
name|TokenTestUtils
operator|.
name|updateSTSPort
argument_list|(
operator|(
name|BindingProvider
operator|)
name|bearerPort2
argument_list|,
name|STSPORT2
argument_list|)
expr_stmt|;
block|}
comment|// Change the STSClient so that it can no longer find the STS
name|stsClient
operator|=
operator|new
name|STSClient
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setOnBehalfOf
argument_list|(
operator|new
name|WSSUsernameCallbackHandler
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|=
operator|(
name|BindingProvider
operator|)
name|bearerPort2
expr_stmt|;
name|p
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|STS_CLIENT
argument_list|,
name|stsClient
argument_list|)
expr_stmt|;
comment|// This should fail as the cache is not being used
try|try
block|{
name|doubleIt
argument_list|(
name|bearerPort2
argument_list|,
literal|40
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected as the token is not stored in the cache"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// expected
block|}
comment|// Set the cache correctly
name|p
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|TokenStore
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|tokenStore
argument_list|)
expr_stmt|;
comment|// Make another invocation - this should succeed as the token is cached
name|p
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.username"
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|doubleIt
argument_list|(
name|bearerPort2
argument_list|,
literal|40
argument_list|)
expr_stmt|;
comment|// Reset the cache - this invocation should fail
name|p
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|TokenStore
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|MemoryTokenStore
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|doubleIt
argument_list|(
name|bearerPort2
argument_list|,
literal|40
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected as the cache is reset"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// expected
block|}
block|}
comment|/**      * Test caching the issued token when the STSClient is deployed in an intermediary      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testDifferentUsersCaching
parameter_list|()
throws|throws
name|Exception
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|URL
name|busFile
init|=
name|UsernameActAsTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"cxf-client.xml"
argument_list|)
decl_stmt|;
name|Bus
name|bus
init|=
name|bf
operator|.
name|createBus
argument_list|(
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|SpringBusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|SpringBusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|URL
name|wsdl
init|=
name|UsernameActAsTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleIt.wsdl"
argument_list|)
decl_stmt|;
name|Service
name|service
init|=
name|Service
operator|.
name|create
argument_list|(
name|wsdl
argument_list|,
name|SERVICE_QNAME
argument_list|)
decl_stmt|;
name|QName
name|portQName
init|=
operator|new
name|QName
argument_list|(
name|NAMESPACE
argument_list|,
literal|"DoubleItAsymmetricSAML2BearerPort"
argument_list|)
decl_stmt|;
name|DoubleItPortType
name|bearerPort
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portQName
argument_list|,
name|DoubleItPortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|updateAddressPort
argument_list|(
name|bearerPort
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
if|if
condition|(
name|standalone
condition|)
block|{
name|TokenTestUtils
operator|.
name|updateSTSPort
argument_list|(
operator|(
name|BindingProvider
operator|)
name|bearerPort
argument_list|,
name|STSPORT2
argument_list|)
expr_stmt|;
block|}
comment|// Disable storing tokens per-proxy
operator|(
operator|(
name|BindingProvider
operator|)
name|bearerPort
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|CACHE_ISSUED_TOKEN_IN_ENDPOINT
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
comment|// Make a successful invocation
operator|(
operator|(
name|BindingProvider
operator|)
name|bearerPort
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.username"
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|doubleIt
argument_list|(
name|bearerPort
argument_list|,
literal|25
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|bearerPort
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.username"
argument_list|,
literal|"bob"
argument_list|)
expr_stmt|;
name|doubleIt
argument_list|(
name|bearerPort
argument_list|,
literal|30
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|bearerPort
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.username"
argument_list|,
literal|"eve"
argument_list|)
expr_stmt|;
try|try
block|{
name|doubleIt
argument_list|(
name|bearerPort
argument_list|,
literal|30
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on a bad user"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//
block|}
comment|// Change the STSClient so that it can no longer find the STS
name|STSClient
name|stsClient
init|=
operator|new
name|STSClient
argument_list|(
name|bus
argument_list|)
decl_stmt|;
name|stsClient
operator|.
name|setOnBehalfOf
argument_list|(
operator|new
name|WSSUsernameCallbackHandler
argument_list|()
argument_list|)
expr_stmt|;
name|BindingProvider
name|p
init|=
operator|(
name|BindingProvider
operator|)
name|bearerPort
decl_stmt|;
name|p
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|STS_CLIENT
argument_list|,
name|stsClient
argument_list|)
expr_stmt|;
comment|// Make a successful invocation
operator|(
operator|(
name|BindingProvider
operator|)
name|bearerPort
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.username"
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|doubleIt
argument_list|(
name|bearerPort
argument_list|,
literal|25
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|bearerPort
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.username"
argument_list|,
literal|"bob"
argument_list|)
expr_stmt|;
name|doubleIt
argument_list|(
name|bearerPort
argument_list|,
literal|30
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|bearerPort
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.username"
argument_list|,
literal|"eve2"
argument_list|)
expr_stmt|;
try|try
block|{
name|doubleIt
argument_list|(
name|bearerPort
argument_list|,
literal|30
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on a bad user"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//
block|}
comment|// Reset the cache - this invocation should fail
name|p
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|TokenStore
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|MemoryTokenStore
argument_list|()
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|bearerPort
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.username"
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
try|try
block|{
name|doubleIt
argument_list|(
name|bearerPort
argument_list|,
literal|30
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//
block|}
block|}
comment|/**      * Test caching the issued token when the STSClient is deployed in an intermediary      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testAppliesToCaching
parameter_list|()
throws|throws
name|Exception
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|URL
name|busFile
init|=
name|UsernameActAsTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"cxf-client.xml"
argument_list|)
decl_stmt|;
name|Bus
name|bus
init|=
name|bf
operator|.
name|createBus
argument_list|(
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|SpringBusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|SpringBusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|URL
name|wsdl
init|=
name|UsernameActAsTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleIt.wsdl"
argument_list|)
decl_stmt|;
name|Service
name|service
init|=
name|Service
operator|.
name|create
argument_list|(
name|wsdl
argument_list|,
name|SERVICE_QNAME
argument_list|)
decl_stmt|;
name|QName
name|portQName
init|=
operator|new
name|QName
argument_list|(
name|NAMESPACE
argument_list|,
literal|"DoubleItAsymmetricSAML2BearerPort"
argument_list|)
decl_stmt|;
name|DoubleItPortType
name|bearerPort
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portQName
argument_list|,
name|DoubleItPortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|updateAddressPort
argument_list|(
name|bearerPort
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
if|if
condition|(
name|standalone
condition|)
block|{
name|TokenTestUtils
operator|.
name|updateSTSPort
argument_list|(
operator|(
name|BindingProvider
operator|)
name|bearerPort
argument_list|,
name|STSPORT2
argument_list|)
expr_stmt|;
block|}
comment|// Disable storing tokens per-proxy
operator|(
operator|(
name|BindingProvider
operator|)
name|bearerPort
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|CACHE_ISSUED_TOKEN_IN_ENDPOINT
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
comment|// Make a successful invocation
operator|(
operator|(
name|BindingProvider
operator|)
name|bearerPort
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.username"
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|BindingProvider
name|p
init|=
operator|(
name|BindingProvider
operator|)
name|bearerPort
decl_stmt|;
name|p
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|STS_APPLIES_TO
argument_list|,
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/doubleit/services/doubleitasymmetricnew"
argument_list|)
expr_stmt|;
name|doubleIt
argument_list|(
name|bearerPort
argument_list|,
literal|25
argument_list|)
expr_stmt|;
comment|// Make a successful invocation
operator|(
operator|(
name|BindingProvider
operator|)
name|bearerPort
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.username"
argument_list|,
literal|"bob"
argument_list|)
expr_stmt|;
name|p
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|STS_APPLIES_TO
argument_list|,
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/doubleit/services/doubleitasymmetricnew2"
argument_list|)
expr_stmt|;
name|doubleIt
argument_list|(
name|bearerPort
argument_list|,
literal|25
argument_list|)
expr_stmt|;
comment|// Change the STSClient so that it can no longer find the STS
name|STSClient
name|stsClient
init|=
operator|new
name|STSClient
argument_list|(
name|bus
argument_list|)
decl_stmt|;
name|stsClient
operator|.
name|setOnBehalfOf
argument_list|(
operator|new
name|WSSUsernameCallbackHandler
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|STS_CLIENT
argument_list|,
name|stsClient
argument_list|)
expr_stmt|;
comment|// Make a successful invocation - should work as token is cached
operator|(
operator|(
name|BindingProvider
operator|)
name|bearerPort
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.username"
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|p
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|STS_APPLIES_TO
argument_list|,
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/doubleit/services/doubleitasymmetricnew"
argument_list|)
expr_stmt|;
name|doubleIt
argument_list|(
name|bearerPort
argument_list|,
literal|25
argument_list|)
expr_stmt|;
comment|// Make a successful invocation - should work as token is cached
operator|(
operator|(
name|BindingProvider
operator|)
name|bearerPort
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.username"
argument_list|,
literal|"bob"
argument_list|)
expr_stmt|;
name|p
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|STS_APPLIES_TO
argument_list|,
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/doubleit/services/doubleitasymmetricnew2"
argument_list|)
expr_stmt|;
name|doubleIt
argument_list|(
name|bearerPort
argument_list|,
literal|25
argument_list|)
expr_stmt|;
comment|// Change appliesTo - should fail
operator|(
operator|(
name|BindingProvider
operator|)
name|bearerPort
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.username"
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|p
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|STS_APPLIES_TO
argument_list|,
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/doubleit/services/doubleitasymmetricnew2"
argument_list|)
expr_stmt|;
try|try
block|{
name|doubleIt
argument_list|(
name|bearerPort
argument_list|,
literal|30
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//
block|}
block|}
comment|/**      * Test caching the issued token when the STSClient is deployed in an intermediary      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testNoAppliesToCaching
parameter_list|()
throws|throws
name|Exception
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|URL
name|busFile
init|=
name|UsernameActAsTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"cxf-client.xml"
argument_list|)
decl_stmt|;
name|Bus
name|bus
init|=
name|bf
operator|.
name|createBus
argument_list|(
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|SpringBusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|SpringBusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|URL
name|wsdl
init|=
name|UsernameActAsTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleIt.wsdl"
argument_list|)
decl_stmt|;
name|Service
name|service
init|=
name|Service
operator|.
name|create
argument_list|(
name|wsdl
argument_list|,
name|SERVICE_QNAME
argument_list|)
decl_stmt|;
name|QName
name|portQName
init|=
operator|new
name|QName
argument_list|(
name|NAMESPACE
argument_list|,
literal|"DoubleItAsymmetricSAML2BearerPort"
argument_list|)
decl_stmt|;
name|DoubleItPortType
name|bearerPort
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portQName
argument_list|,
name|DoubleItPortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|updateAddressPort
argument_list|(
name|bearerPort
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
if|if
condition|(
name|standalone
condition|)
block|{
name|TokenTestUtils
operator|.
name|updateSTSPort
argument_list|(
operator|(
name|BindingProvider
operator|)
name|bearerPort
argument_list|,
name|STSPORT2
argument_list|)
expr_stmt|;
block|}
comment|// Disable storing tokens per-proxy
operator|(
operator|(
name|BindingProvider
operator|)
name|bearerPort
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|CACHE_ISSUED_TOKEN_IN_ENDPOINT
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
comment|// Make a successful invocation
operator|(
operator|(
name|BindingProvider
operator|)
name|bearerPort
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.username"
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
comment|// Disable appliesTo
name|BindingProvider
name|p
init|=
operator|(
name|BindingProvider
operator|)
name|bearerPort
decl_stmt|;
name|STSClient
name|stsClient
init|=
operator|(
name|STSClient
operator|)
name|p
operator|.
name|getRequestContext
argument_list|()
operator|.
name|get
argument_list|(
name|SecurityConstants
operator|.
name|STS_CLIENT
argument_list|)
decl_stmt|;
name|stsClient
operator|.
name|setEnableAppliesTo
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|doubleIt
argument_list|(
name|bearerPort
argument_list|,
literal|25
argument_list|)
expr_stmt|;
comment|// Change the STSClient so that it can no longer find the STS
name|stsClient
operator|=
operator|new
name|STSClient
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setOnBehalfOf
argument_list|(
operator|new
name|WSSUsernameCallbackHandler
argument_list|()
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setEnableAppliesTo
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|p
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|STS_CLIENT
argument_list|,
name|stsClient
argument_list|)
expr_stmt|;
comment|// This should work
name|doubleIt
argument_list|(
name|bearerPort
argument_list|,
literal|25
argument_list|)
expr_stmt|;
comment|// Bob should fail
operator|(
operator|(
name|BindingProvider
operator|)
name|bearerPort
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.username"
argument_list|,
literal|"bob"
argument_list|)
expr_stmt|;
try|try
block|{
name|doubleIt
argument_list|(
name|bearerPort
argument_list|,
literal|30
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//
block|}
block|}
specifier|private
specifier|static
name|void
name|doubleIt
parameter_list|(
name|DoubleItPortType
name|port
parameter_list|,
name|int
name|numToDouble
parameter_list|)
block|{
name|int
name|resp
init|=
name|port
operator|.
name|doubleIt
argument_list|(
name|numToDouble
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
operator|*
name|numToDouble
argument_list|,
name|resp
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

