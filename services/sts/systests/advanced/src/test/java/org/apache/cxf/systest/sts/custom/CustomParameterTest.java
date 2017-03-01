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
name|custom
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
name|HashMap
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
name|sts
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
name|trust
operator|.
name|STSClient
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
comment|/**  * This test sends a custom parameter indicating the "realm" of the user, which is interpreted by the  * STS's CustomUTValidator.  */
end_comment

begin_class
specifier|public
class|class
name|CustomParameterTest
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
comment|// Here the custom parameter in the RST is parsed by the CustomUTValidator
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testCustomParameterInRSTValidator
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
name|CustomParameterTest
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
name|CustomParameterTest
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
literal|"DoubleItTransportCustomParameterPort"
argument_list|)
decl_stmt|;
name|DoubleItPortType
name|transportClaimsPort
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
name|transportClaimsPort
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|TokenTestUtils
operator|.
name|updateSTSPort
argument_list|(
operator|(
name|BindingProvider
operator|)
name|transportClaimsPort
argument_list|,
name|STSPORT
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
name|setWsdlLocation
argument_list|(
literal|"https://localhost:"
operator|+
name|STSPORT
operator|+
literal|"/SecurityTokenService/UT?wsdl"
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setServiceName
argument_list|(
literal|"{http://docs.oasis-open.org/ws-sx/ws-trust/200512/}SecurityTokenService"
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setEndpointName
argument_list|(
literal|"{http://docs.oasis-open.org/ws-sx/ws-trust/200512/}UT_Port"
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
literal|"security.username"
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"security.callback-handler"
argument_list|,
literal|"org.apache.cxf.systest.sts.common.CommonCallbackHandler"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"security.sts.token.username"
argument_list|,
literal|"myclientkey"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"security.sts.token.properties"
argument_list|,
literal|"clientKeystore.properties"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"security.sts.token.usecert"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|transportClaimsPort
operator|)
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
comment|// Successful test
comment|// Add custom content to the RST
name|stsClient
operator|.
name|setCustomContent
argument_list|(
literal|"<realm xmlns=\"http://cxf.apache.org/custom\">custom-realm</realm>"
argument_list|)
expr_stmt|;
name|doubleIt
argument_list|(
name|transportClaimsPort
argument_list|,
literal|25
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
name|transportClaimsPort
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
comment|// Here the custom parameter in the RST is parsed by the CustomUTValidator
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testCustomParameterInRST2Validator
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
name|CustomParameterTest
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
name|CustomParameterTest
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
literal|"DoubleItTransportCustomParameterPort"
argument_list|)
decl_stmt|;
name|DoubleItPortType
name|transportClaimsPort
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
name|transportClaimsPort
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|TokenTestUtils
operator|.
name|updateSTSPort
argument_list|(
operator|(
name|BindingProvider
operator|)
name|transportClaimsPort
argument_list|,
name|STSPORT
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
name|setWsdlLocation
argument_list|(
literal|"https://localhost:"
operator|+
name|STSPORT
operator|+
literal|"/SecurityTokenService/UT?wsdl"
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setServiceName
argument_list|(
literal|"{http://docs.oasis-open.org/ws-sx/ws-trust/200512/}SecurityTokenService"
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setEndpointName
argument_list|(
literal|"{http://docs.oasis-open.org/ws-sx/ws-trust/200512/}UT_Port"
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
literal|"security.username"
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"security.callback-handler"
argument_list|,
literal|"org.apache.cxf.systest.sts.common.CommonCallbackHandler"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"security.sts.token.username"
argument_list|,
literal|"myclientkey"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"security.sts.token.properties"
argument_list|,
literal|"clientKeystore.properties"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"security.sts.token.usecert"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|transportClaimsPort
operator|)
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
comment|// Failing test
comment|// Add custom content to the RST
name|stsClient
operator|.
name|setCustomContent
argument_list|(
literal|"<realm xmlns=\"http://cxf.apache.org/custom\">custom-unknown-realm</realm>"
argument_list|)
expr_stmt|;
try|try
block|{
name|doubleIt
argument_list|(
name|transportClaimsPort
argument_list|,
literal|25
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on the wrong realm"
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
operator|(
operator|(
name|java
operator|.
name|io
operator|.
name|Closeable
operator|)
name|transportClaimsPort
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
comment|// Here the custom parameter in the RST is parsed by the CustomClaimsHandler
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testCustomParameterInRSTClaimsHandler
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
name|CustomParameterTest
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
name|CustomParameterTest
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
literal|"DoubleItTransportCustomParameterClaimsPort"
argument_list|)
decl_stmt|;
name|DoubleItPortType
name|transportClaimsPort
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
name|transportClaimsPort
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|TokenTestUtils
operator|.
name|updateSTSPort
argument_list|(
operator|(
name|BindingProvider
operator|)
name|transportClaimsPort
argument_list|,
name|STSPORT
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
name|setWsdlLocation
argument_list|(
literal|"https://localhost:"
operator|+
name|STSPORT
operator|+
literal|"/SecurityTokenService/Transport?wsdl"
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setServiceName
argument_list|(
literal|"{http://docs.oasis-open.org/ws-sx/ws-trust/200512/}SecurityTokenService"
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setEndpointName
argument_list|(
literal|"{http://docs.oasis-open.org/ws-sx/ws-trust/200512/}Transport_Port"
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
literal|"security.username"
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"security.callback-handler"
argument_list|,
literal|"org.apache.cxf.systest.sts.common.CommonCallbackHandler"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"security.sts.token.username"
argument_list|,
literal|"myclientkey"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"security.sts.token.properties"
argument_list|,
literal|"clientKeystore.properties"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"security.sts.token.usecert"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|transportClaimsPort
operator|)
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
comment|// Successful test
comment|// Add custom content to the RST
name|stsClient
operator|.
name|setCustomContent
argument_list|(
literal|"<realm xmlns=\"http://cxf.apache.org/custom\">custom-realm</realm>"
argument_list|)
expr_stmt|;
name|doubleIt
argument_list|(
name|transportClaimsPort
argument_list|,
literal|25
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
name|transportClaimsPort
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
comment|// Here the custom parameter in the RST is parsed by the CustomClaimsHandler
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testCustomParameterInRSTClaimsHandler2
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
name|CustomParameterTest
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
name|CustomParameterTest
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
literal|"DoubleItTransportCustomParameterClaimsPort"
argument_list|)
decl_stmt|;
name|DoubleItPortType
name|transportClaimsPort
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
name|transportClaimsPort
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|TokenTestUtils
operator|.
name|updateSTSPort
argument_list|(
operator|(
name|BindingProvider
operator|)
name|transportClaimsPort
argument_list|,
name|STSPORT
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
name|setWsdlLocation
argument_list|(
literal|"https://localhost:"
operator|+
name|STSPORT
operator|+
literal|"/SecurityTokenService/Transport?wsdl"
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setServiceName
argument_list|(
literal|"{http://docs.oasis-open.org/ws-sx/ws-trust/200512/}SecurityTokenService"
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setEndpointName
argument_list|(
literal|"{http://docs.oasis-open.org/ws-sx/ws-trust/200512/}Transport_Port"
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
literal|"security.username"
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"security.callback-handler"
argument_list|,
literal|"org.apache.cxf.systest.sts.common.CommonCallbackHandler"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"security.sts.token.username"
argument_list|,
literal|"myclientkey"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"security.sts.token.properties"
argument_list|,
literal|"clientKeystore.properties"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"security.sts.token.usecert"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|transportClaimsPort
operator|)
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
comment|// Failing test
comment|// Add custom content to the RST
name|stsClient
operator|.
name|setCustomContent
argument_list|(
literal|"<realm xmlns=\"http://cxf.apache.org/custom\">custom-unknown-realm</realm>"
argument_list|)
expr_stmt|;
try|try
block|{
name|doubleIt
argument_list|(
name|transportClaimsPort
argument_list|,
literal|25
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on the wrong realm"
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
operator|(
operator|(
name|java
operator|.
name|io
operator|.
name|Closeable
operator|)
name|transportClaimsPort
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
name|numToDouble
operator|*
literal|2
argument_list|,
name|resp
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

