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
name|wssc
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
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyStore
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
name|net
operator|.
name|ssl
operator|.
name|TrustManagerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|Callback
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|CallbackHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|UnsupportedCallbackException
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
name|common
operator|.
name|classloader
operator|.
name|ClassLoaderUtils
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
name|jsse
operator|.
name|TLSClientParameters
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
name|rt
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
name|addressing
operator|.
name|policy
operator|.
name|MetadataConstants
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
name|policy
operator|.
name|builder
operator|.
name|primitive
operator|.
name|PrimitiveAssertion
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
name|SecurityToken
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
name|DefaultSymmetricBinding
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
name|neethi
operator|.
name|All
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|ExactlyOne
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Policy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|ext
operator|.
name|WSPasswordCallback
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|policy
operator|.
name|SP12Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|policy
operator|.
name|SPConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|policy
operator|.
name|model
operator|.
name|AlgorithmSuite
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|policy
operator|.
name|model
operator|.
name|Header
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|policy
operator|.
name|model
operator|.
name|ProtectionToken
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|policy
operator|.
name|model
operator|.
name|SignedParts
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|policy
operator|.
name|model
operator|.
name|X509Token
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
name|Test
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
name|assertNotNull
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

begin_comment
comment|/**  * Some unit tests for SecureConversation.  */
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
name|WSSCUnitTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|static
specifier|final
name|String
name|PORT
init|=
name|allocatePort
argument_list|(
name|UnitServer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|String
name|PORT2
init|=
name|allocatePort
argument_list|(
name|UnitServer
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
specifier|final
name|TestParam
name|test
decl_stmt|;
specifier|public
name|WSSCUnitTest
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
name|UnitServer
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
name|Test
specifier|public
name|void
name|testEndorsingSecureConveration
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
name|WSSCUnitTest
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
name|WSSCUnitTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleItWSSC.wsdl"
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
literal|"DoubleItTransportPort"
argument_list|)
decl_stmt|;
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEndorsingSecureConverationViaCode
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|wsdl
init|=
name|WSSCUnitTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleItWSSC.wsdl"
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
literal|"DoubleItTransportPort"
argument_list|)
decl_stmt|;
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
comment|// TLS configuration
name|TrustManagerFactory
name|tmf
init|=
name|TrustManagerFactory
operator|.
name|getInstance
argument_list|(
name|TrustManagerFactory
operator|.
name|getDefaultAlgorithm
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|KeyStore
name|ts
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
literal|"JKS"
argument_list|)
decl_stmt|;
try|try
init|(
name|InputStream
name|trustStore
init|=
name|ClassLoaderUtils
operator|.
name|getResourceAsStream
argument_list|(
literal|"keys/Truststore.jks"
argument_list|,
name|WSSCUnitTest
operator|.
name|class
argument_list|)
init|)
block|{
name|ts
operator|.
name|load
argument_list|(
name|trustStore
argument_list|,
literal|"password"
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|tmf
operator|.
name|init
argument_list|(
name|ts
argument_list|)
expr_stmt|;
name|TLSClientParameters
name|tlsParams
init|=
operator|new
name|TLSClientParameters
argument_list|()
decl_stmt|;
name|tlsParams
operator|.
name|setTrustManagers
argument_list|(
name|tmf
operator|.
name|getTrustManagers
argument_list|()
argument_list|)
expr_stmt|;
name|tlsParams
operator|.
name|setDisableCNCheck
argument_list|(
literal|true
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
name|HTTPConduit
name|http
init|=
operator|(
name|HTTPConduit
operator|)
name|client
operator|.
name|getConduit
argument_list|()
decl_stmt|;
name|http
operator|.
name|setTlsClientParameters
argument_list|(
name|tlsParams
argument_list|)
expr_stmt|;
comment|// STSClient configuration
name|Bus
name|clientBus
init|=
name|BusFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createBus
argument_list|()
decl_stmt|;
name|STSClient
name|stsClient
init|=
operator|new
name|STSClient
argument_list|(
name|clientBus
argument_list|)
decl_stmt|;
name|stsClient
operator|.
name|setTlsClientParameters
argument_list|(
name|tlsParams
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
literal|"security.sts.client"
argument_list|,
name|stsClient
argument_list|)
expr_stmt|;
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEndorsingSecureConverationSP12
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
name|WSSCUnitTest
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
name|WSSCUnitTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleItWSSC.wsdl"
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
literal|"DoubleItTransportSP12Port"
argument_list|)
decl_stmt|;
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIssueUnitTest
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|test
operator|.
name|isStreaming
argument_list|()
condition|)
block|{
return|return;
block|}
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
name|WSSCUnitTest
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
name|setSecureConv
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setLocation
argument_list|(
literal|"https://localhost:"
operator|+
name|PORT
operator|+
literal|"/"
operator|+
literal|"DoubleItTransport"
argument_list|)
expr_stmt|;
comment|// Add Addressing policy
name|Policy
name|p
init|=
operator|new
name|Policy
argument_list|()
decl_stmt|;
name|ExactlyOne
name|ea
init|=
operator|new
name|ExactlyOne
argument_list|()
decl_stmt|;
name|p
operator|.
name|addPolicyComponent
argument_list|(
name|ea
argument_list|)
expr_stmt|;
name|All
name|all
init|=
operator|new
name|All
argument_list|()
decl_stmt|;
name|all
operator|.
name|addPolicyComponent
argument_list|(
operator|new
name|PrimitiveAssertion
argument_list|(
name|MetadataConstants
operator|.
name|USING_ADDRESSING_2006_QNAME
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|ea
operator|.
name|addPolicyComponent
argument_list|(
name|all
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setPolicy
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|requestSecurityToken
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/"
operator|+
literal|"DoubleItTransport"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIssueAndCancelUnitTest
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|test
operator|.
name|isStreaming
argument_list|()
condition|)
block|{
return|return;
block|}
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
name|WSSCUnitTest
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
name|setSecureConv
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setLocation
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT2
operator|+
literal|"/"
operator|+
literal|"DoubleItSymmetric"
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setPolicy
argument_list|(
name|createSymmetricBindingPolicy
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|ENCRYPT_USERNAME
argument_list|,
literal|"bob"
argument_list|)
expr_stmt|;
name|TokenCallbackHandler
name|callbackHandler
init|=
operator|new
name|TokenCallbackHandler
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|CALLBACK_HANDLER
argument_list|,
name|callbackHandler
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|SIGNATURE_PROPERTIES
argument_list|,
literal|"alice.properties"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|ENCRYPT_PROPERTIES
argument_list|,
literal|"bob.properties"
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|SecurityToken
name|securityToken
init|=
name|stsClient
operator|.
name|requestSecurityToken
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT2
operator|+
literal|"/"
operator|+
literal|"DoubleItSymmetric"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|securityToken
argument_list|)
expr_stmt|;
name|callbackHandler
operator|.
name|setSecurityToken
argument_list|(
name|securityToken
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|stsClient
operator|.
name|cancelSecurityToken
argument_list|(
name|securityToken
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIssueAndRenewUnitTest
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|test
operator|.
name|isStreaming
argument_list|()
condition|)
block|{
return|return;
block|}
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
name|WSSCUnitTest
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
name|setSecureConv
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setLocation
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT2
operator|+
literal|"/"
operator|+
literal|"DoubleItSymmetric"
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setPolicy
argument_list|(
name|createSymmetricBindingPolicy
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|ENCRYPT_USERNAME
argument_list|,
literal|"bob"
argument_list|)
expr_stmt|;
name|TokenCallbackHandler
name|callbackHandler
init|=
operator|new
name|TokenCallbackHandler
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|CALLBACK_HANDLER
argument_list|,
name|callbackHandler
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|SIGNATURE_PROPERTIES
argument_list|,
literal|"alice.properties"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|ENCRYPT_PROPERTIES
argument_list|,
literal|"bob.properties"
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|SecurityToken
name|securityToken
init|=
name|stsClient
operator|.
name|requestSecurityToken
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT2
operator|+
literal|"/"
operator|+
literal|"DoubleItSymmetric"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|securityToken
argument_list|)
expr_stmt|;
name|callbackHandler
operator|.
name|setSecurityToken
argument_list|(
name|securityToken
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|stsClient
operator|.
name|renewSecurityToken
argument_list|(
name|securityToken
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// mock up a SymmetricBinding policy to talk to the STS
specifier|private
name|Policy
name|createSymmetricBindingPolicy
parameter_list|()
block|{
comment|// Add Addressing policy
name|Policy
name|p
init|=
operator|new
name|Policy
argument_list|()
decl_stmt|;
name|ExactlyOne
name|ea
init|=
operator|new
name|ExactlyOne
argument_list|()
decl_stmt|;
name|p
operator|.
name|addPolicyComponent
argument_list|(
name|ea
argument_list|)
expr_stmt|;
name|All
name|all
init|=
operator|new
name|All
argument_list|()
decl_stmt|;
name|all
operator|.
name|addPolicyComponent
argument_list|(
operator|new
name|PrimitiveAssertion
argument_list|(
name|MetadataConstants
operator|.
name|USING_ADDRESSING_2006_QNAME
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|ea
operator|.
name|addPolicyComponent
argument_list|(
name|all
argument_list|)
expr_stmt|;
comment|// X509 Token
specifier|final
name|X509Token
name|x509Token
init|=
operator|new
name|X509Token
argument_list|(
name|SPConstants
operator|.
name|SPVersion
operator|.
name|SP12
argument_list|,
name|SPConstants
operator|.
name|IncludeTokenType
operator|.
name|INCLUDE_TOKEN_NEVER
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
operator|new
name|Policy
argument_list|()
argument_list|)
decl_stmt|;
name|Policy
name|x509Policy
init|=
operator|new
name|Policy
argument_list|()
decl_stmt|;
name|ExactlyOne
name|x509PolicyEa
init|=
operator|new
name|ExactlyOne
argument_list|()
decl_stmt|;
name|x509Policy
operator|.
name|addPolicyComponent
argument_list|(
name|x509PolicyEa
argument_list|)
expr_stmt|;
name|All
name|x509PolicyAll
init|=
operator|new
name|All
argument_list|()
decl_stmt|;
name|x509PolicyAll
operator|.
name|addPolicyComponent
argument_list|(
name|x509Token
argument_list|)
expr_stmt|;
name|x509PolicyEa
operator|.
name|addPolicyComponent
argument_list|(
name|x509PolicyAll
argument_list|)
expr_stmt|;
comment|// AlgorithmSuite
name|Policy
name|algSuitePolicy
init|=
operator|new
name|Policy
argument_list|()
decl_stmt|;
name|ExactlyOne
name|algSuitePolicyEa
init|=
operator|new
name|ExactlyOne
argument_list|()
decl_stmt|;
name|algSuitePolicy
operator|.
name|addPolicyComponent
argument_list|(
name|algSuitePolicyEa
argument_list|)
expr_stmt|;
name|All
name|algSuitePolicyAll
init|=
operator|new
name|All
argument_list|()
decl_stmt|;
name|algSuitePolicyAll
operator|.
name|addAssertion
argument_list|(
operator|new
name|PrimitiveAssertion
argument_list|(
operator|new
name|QName
argument_list|(
name|SP12Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|ALGO_SUITE_BASIC128
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|algSuitePolicyEa
operator|.
name|addPolicyComponent
argument_list|(
name|algSuitePolicyAll
argument_list|)
expr_stmt|;
name|AlgorithmSuite
name|algorithmSuite
init|=
operator|new
name|AlgorithmSuite
argument_list|(
name|SPConstants
operator|.
name|SPVersion
operator|.
name|SP12
argument_list|,
name|algSuitePolicy
argument_list|)
decl_stmt|;
comment|// Symmetric Binding
name|Policy
name|bindingPolicy
init|=
operator|new
name|Policy
argument_list|()
decl_stmt|;
name|ExactlyOne
name|bindingPolicyEa
init|=
operator|new
name|ExactlyOne
argument_list|()
decl_stmt|;
name|bindingPolicy
operator|.
name|addPolicyComponent
argument_list|(
name|bindingPolicyEa
argument_list|)
expr_stmt|;
name|All
name|bindingPolicyAll
init|=
operator|new
name|All
argument_list|()
decl_stmt|;
name|bindingPolicyAll
operator|.
name|addPolicyComponent
argument_list|(
operator|new
name|ProtectionToken
argument_list|(
name|SPConstants
operator|.
name|SPVersion
operator|.
name|SP12
argument_list|,
name|x509Policy
argument_list|)
argument_list|)
expr_stmt|;
name|bindingPolicyAll
operator|.
name|addPolicyComponent
argument_list|(
name|algorithmSuite
argument_list|)
expr_stmt|;
name|bindingPolicyAll
operator|.
name|addAssertion
argument_list|(
operator|new
name|PrimitiveAssertion
argument_list|(
name|SP12Constants
operator|.
name|INCLUDE_TIMESTAMP
argument_list|)
argument_list|)
expr_stmt|;
name|bindingPolicyAll
operator|.
name|addAssertion
argument_list|(
operator|new
name|PrimitiveAssertion
argument_list|(
name|SP12Constants
operator|.
name|ONLY_SIGN_ENTIRE_HEADERS_AND_BODY
argument_list|)
argument_list|)
expr_stmt|;
name|bindingPolicyEa
operator|.
name|addPolicyComponent
argument_list|(
name|bindingPolicyAll
argument_list|)
expr_stmt|;
name|DefaultSymmetricBinding
name|binding
init|=
operator|new
name|DefaultSymmetricBinding
argument_list|(
name|SPConstants
operator|.
name|SPVersion
operator|.
name|SP12
argument_list|,
name|bindingPolicy
argument_list|)
decl_stmt|;
name|binding
operator|.
name|setOnlySignEntireHeadersAndBody
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|binding
operator|.
name|setProtectTokens
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|all
operator|.
name|addPolicyComponent
argument_list|(
name|binding
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Header
argument_list|>
name|headers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|SignedParts
name|signedParts
init|=
operator|new
name|SignedParts
argument_list|(
name|SPConstants
operator|.
name|SPVersion
operator|.
name|SP12
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|all
operator|.
name|addPolicyComponent
argument_list|(
name|signedParts
argument_list|)
expr_stmt|;
return|return
name|p
return|;
block|}
specifier|private
specifier|static
class|class
name|TokenCallbackHandler
implements|implements
name|CallbackHandler
block|{
specifier|private
name|SecurityToken
name|securityToken
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|handle
parameter_list|(
name|Callback
index|[]
name|callbacks
parameter_list|)
throws|throws
name|IOException
throws|,
name|UnsupportedCallbackException
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|callbacks
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|WSPasswordCallback
name|pc
init|=
operator|(
name|WSPasswordCallback
operator|)
name|callbacks
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|securityToken
operator|!=
literal|null
operator|&&
name|pc
operator|.
name|getIdentifier
argument_list|()
operator|.
name|equals
argument_list|(
name|securityToken
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
name|pc
operator|.
name|setKey
argument_list|(
name|securityToken
operator|.
name|getSecret
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
operator|new
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
name|KeystorePasswordCallback
argument_list|()
operator|.
name|handle
argument_list|(
name|callbacks
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|setSecurityToken
parameter_list|(
name|SecurityToken
name|securityToken
parameter_list|)
block|{
name|this
operator|.
name|securityToken
operator|=
name|securityToken
expr_stmt|;
block|}
block|}
empty_stmt|;
block|}
end_class

end_unit

