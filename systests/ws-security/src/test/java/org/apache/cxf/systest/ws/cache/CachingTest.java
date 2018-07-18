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
name|cache
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|systest
operator|.
name|ws
operator|.
name|common
operator|.
name|SecurityTestUtil
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
name|ws
operator|.
name|common
operator|.
name|TestParam
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
name|TokenStore
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
operator|.
name|Parameters
import|;
end_import

begin_comment
comment|/**  * A set of tests for token caching on the client side  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|value
operator|=
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
operator|.
name|class
argument_list|)
specifier|public
class|class
name|CachingTest
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
specifier|final
name|TestParam
name|test
decl_stmt|;
specifier|public
name|CachingTest
parameter_list|(
name|TestParam
name|type
parameter_list|)
block|{
name|this
operator|.
name|test
operator|=
name|type
expr_stmt|;
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
name|Parameters
argument_list|(
name|name
operator|=
literal|"{0}"
argument_list|)
specifier|public
specifier|static
name|Collection
argument_list|<
name|TestParam
argument_list|>
name|data
parameter_list|()
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|TestParam
index|[]
block|{
operator|new
name|TestParam
argument_list|(
name|PORT
argument_list|,
literal|false
argument_list|)
block|,
operator|new
name|TestParam
argument_list|(
name|PORT
argument_list|,
literal|true
argument_list|)
block|,         }
argument_list|)
return|;
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|AfterClass
specifier|public
specifier|static
name|void
name|cleanup
parameter_list|()
throws|throws
name|Exception
block|{
name|SecurityTestUtil
operator|.
name|cleanup
argument_list|()
expr_stmt|;
name|stopAllServers
argument_list|()
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
name|testSymmetric
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
name|CachingTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
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
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|URL
name|wsdl
init|=
name|CachingTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleItCache.wsdl"
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
literal|"DoubleItCacheSymmetricPort"
argument_list|)
decl_stmt|;
comment|// First invocation
name|DoubleItPortType
name|port
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
name|port
argument_list|,
name|test
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|test
operator|.
name|isStreaming
argument_list|()
condition|)
block|{
name|SecurityTestUtil
operator|.
name|enableStreaming
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|50
argument_list|,
name|port
operator|.
name|doubleIt
argument_list|(
literal|25
argument_list|)
argument_list|)
expr_stmt|;
name|Client
name|client
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|port
argument_list|)
decl_stmt|;
name|TokenStore
name|tokenStore
init|=
operator|(
name|TokenStore
operator|)
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getProperty
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN_STORE_CACHE_INSTANCE
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tokenStore
argument_list|)
expr_stmt|;
comment|// We expect two tokens as the identifier + SHA-1 are cached
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|tokenStore
operator|.
name|getTokenIdentifiers
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Second invocation
name|DoubleItPortType
name|port2
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
name|port2
argument_list|,
name|test
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|test
operator|.
name|isStreaming
argument_list|()
condition|)
block|{
name|SecurityTestUtil
operator|.
name|enableStreaming
argument_list|(
name|port2
argument_list|)
expr_stmt|;
block|}
name|port2
operator|.
name|doubleIt
argument_list|(
literal|35
argument_list|)
expr_stmt|;
name|client
operator|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|port2
argument_list|)
expr_stmt|;
name|tokenStore
operator|=
operator|(
name|TokenStore
operator|)
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getProperty
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN_STORE_CACHE_INSTANCE
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|tokenStore
argument_list|)
expr_stmt|;
comment|// There should now be 4 tokens as both proxies share the same TokenStore
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|tokenStore
operator|.
name|getTokenIdentifiers
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
operator|(
operator|(
name|java
operator|.
name|io
operator|.
name|Closeable
operator|)
name|port
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
comment|//port2 is still holding onto the cache, thus, this should still be 4
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|tokenStore
operator|.
name|getTokenIdentifiers
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
operator|(
operator|(
name|java
operator|.
name|io
operator|.
name|Closeable
operator|)
name|port2
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
comment|//port2 is now closed, this should be null
name|assertNull
argument_list|(
name|tokenStore
operator|.
name|getTokenIdentifiers
argument_list|()
argument_list|)
expr_stmt|;
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
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
name|testCachePerProxySymmetric
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
name|CachingTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
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
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|URL
name|wsdl
init|=
name|CachingTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleItCache.wsdl"
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
literal|"DoubleItCachePerProxySymmetricPort"
argument_list|)
decl_stmt|;
comment|// First invocation
name|DoubleItPortType
name|port
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
name|port
argument_list|,
name|test
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|port
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|CACHE_IDENTIFIER
argument_list|,
literal|"proxy1"
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|port
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|CACHE_CONFIG_FILE
argument_list|,
literal|"per-proxy-cache.xml"
argument_list|)
expr_stmt|;
if|if
condition|(
name|test
operator|.
name|isStreaming
argument_list|()
condition|)
block|{
name|SecurityTestUtil
operator|.
name|enableStreaming
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|50
argument_list|,
name|port
operator|.
name|doubleIt
argument_list|(
literal|25
argument_list|)
argument_list|)
expr_stmt|;
name|Client
name|client
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|port
argument_list|)
decl_stmt|;
name|TokenStore
name|tokenStore
init|=
operator|(
name|TokenStore
operator|)
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getProperty
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN_STORE_CACHE_INSTANCE
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tokenStore
argument_list|)
expr_stmt|;
comment|// We expect two tokens as the identifier + SHA-1 are cached
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|tokenStore
operator|.
name|getTokenIdentifiers
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Second invocation
name|DoubleItPortType
name|port2
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
name|port2
argument_list|,
name|test
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|port2
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|CACHE_IDENTIFIER
argument_list|,
literal|"proxy2"
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|port2
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|CACHE_CONFIG_FILE
argument_list|,
literal|"per-proxy-cache.xml"
argument_list|)
expr_stmt|;
if|if
condition|(
name|test
operator|.
name|isStreaming
argument_list|()
condition|)
block|{
name|SecurityTestUtil
operator|.
name|enableStreaming
argument_list|(
name|port2
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|70
argument_list|,
name|port2
operator|.
name|doubleIt
argument_list|(
literal|35
argument_list|)
argument_list|)
expr_stmt|;
name|client
operator|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|port2
argument_list|)
expr_stmt|;
name|tokenStore
operator|=
operator|(
name|TokenStore
operator|)
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getProperty
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN_STORE_CACHE_INSTANCE
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|tokenStore
argument_list|)
expr_stmt|;
comment|// We expect two tokens as the identifier + SHA-1 are cached
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|tokenStore
operator|.
name|getTokenIdentifiers
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
operator|(
operator|(
name|java
operator|.
name|io
operator|.
name|Closeable
operator|)
name|port
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
operator|(
operator|(
name|java
operator|.
name|io
operator|.
name|Closeable
operator|)
name|port2
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

