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
name|claims
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
name|systest
operator|.
name|sts
operator|.
name|deployment
operator|.
name|StaxSTSServer
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
comment|/**  * Test sending claims that are defined in the policy of the WSDL to the STS for evaluation.  * The SAML token is tested on the service side for the correct claims (role) information via a  * custom validator.  */
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
name|ClaimsTest
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
name|STAX_STSPORT
init|=
name|allocatePort
argument_list|(
name|StaxSTSServer
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
specifier|private
specifier|static
specifier|final
name|String
name|STAX_PORT
init|=
name|allocatePort
argument_list|(
name|StaxServer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|TestParam
name|test
decl_stmt|;
specifier|public
name|ClaimsTest
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
name|assertTrue
argument_list|(
literal|"Server failed to launch"
argument_list|,
comment|// run the server in the same process
comment|// set this to false to fork
name|launchServer
argument_list|(
name|StaxServer
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
name|assertTrue
argument_list|(
literal|"Server failed to launch"
argument_list|,
comment|// run the server in the same process
comment|// set this to false to fork
name|launchServer
argument_list|(
name|StaxSTSServer
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
index|[]
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
index|[]
block|{
block|{
operator|new
name|TestParam
argument_list|(
name|PORT
argument_list|,
literal|false
argument_list|,
name|STSPORT
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
name|PORT
argument_list|,
literal|true
argument_list|,
name|STSPORT
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
name|STAX_PORT
argument_list|,
literal|false
argument_list|,
name|STSPORT
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
name|STAX_PORT
argument_list|,
literal|true
argument_list|,
name|STSPORT
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
name|PORT
argument_list|,
literal|false
argument_list|,
name|STAX_STSPORT
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
name|PORT
argument_list|,
literal|true
argument_list|,
name|STAX_STSPORT
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
name|STAX_PORT
argument_list|,
literal|false
argument_list|,
name|STAX_STSPORT
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
name|STAX_PORT
argument_list|,
literal|true
argument_list|,
name|STAX_STSPORT
argument_list|)
block|}
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
name|testSaml1Claims
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
name|ClaimsTest
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
name|ClaimsTest
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
literal|"DoubleItTransportSAML1ClaimsPort"
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
name|test
operator|.
name|getPort
argument_list|()
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
name|test
operator|.
name|getStsPort
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
name|transportClaimsPort
argument_list|)
expr_stmt|;
block|}
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
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testSaml1CustomClaims
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
name|ClaimsTest
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
name|ClaimsTest
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
literal|"DoubleItTransportSAML1CustomClaimsPort"
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
name|test
operator|.
name|getPort
argument_list|()
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
name|test
operator|.
name|getStsPort
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
name|transportClaimsPort
argument_list|)
expr_stmt|;
block|}
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
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testSaml1WrongClaims
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
name|ClaimsTest
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
name|ClaimsTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleItWrongClaims.wsdl"
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
literal|"DoubleItTransportSAML1ClaimsPort"
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
name|test
operator|.
name|getPort
argument_list|()
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
name|test
operator|.
name|getStsPort
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
name|transportClaimsPort
argument_list|)
expr_stmt|;
block|}
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
literal|"Expected Exception"
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
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testSaml1ClaimsWrongRole
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
name|ClaimsTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"cxf-bad-client.xml"
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
name|ClaimsTest
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
literal|"DoubleItTransportSAML1FailingClaimsPort"
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
name|test
operator|.
name|getPort
argument_list|()
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
name|test
operator|.
name|getStsPort
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
name|transportClaimsPort
argument_list|)
expr_stmt|;
block|}
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
literal|"Expected Exception"
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
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testSaml2Claims
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
name|ClaimsTest
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
name|ClaimsTest
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
literal|"DoubleItTransportSAML2ClaimsPort"
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
name|test
operator|.
name|getPort
argument_list|()
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
name|test
operator|.
name|getStsPort
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
name|transportClaimsPort
argument_list|)
expr_stmt|;
block|}
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
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testSaml2WrongClaims
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
name|ClaimsTest
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
name|ClaimsTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleItWrongClaims.wsdl"
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
literal|"DoubleItTransportSAML2ClaimsPort"
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
name|test
operator|.
name|getPort
argument_list|()
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
name|test
operator|.
name|getStsPort
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
name|transportClaimsPort
argument_list|)
expr_stmt|;
block|}
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
literal|"Expected Exception"
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
comment|// In this test, the WSDL the client is using has no Claims Element (however the service
comment|// is using a WSDL that requires Claims). A CallbackHandler is used to send the Claims
comment|// Element to the STS.
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testSaml2ClaimsCallbackHandler
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
name|ClaimsTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"cxf-client-cbhandler.xml"
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
name|ClaimsTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleItNoClaims.wsdl"
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
literal|"DoubleItTransportSAML2ClaimsPort"
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
name|test
operator|.
name|getPort
argument_list|()
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
name|test
operator|.
name|getStsPort
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
name|transportClaimsPort
argument_list|)
expr_stmt|;
block|}
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
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testSaml2ChildClaims
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
name|ClaimsTest
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
name|ClaimsTest
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
literal|"DoubleItTransportSAML2ChildClaimsPort"
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

