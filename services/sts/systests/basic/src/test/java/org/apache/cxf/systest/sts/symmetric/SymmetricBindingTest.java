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
name|symmetric
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
name|parsers
operator|.
name|DocumentBuilder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilderFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|ParserConfigurationException
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
name|dom
operator|.
name|DOMSource
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
name|soap
operator|.
name|AddressingFeature
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|wss4j
operator|.
name|dom
operator|.
name|WSConstants
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
comment|/**  * Test the Symmetric binding. The CXF client gets a token from the STS by authenticating via a  * Username Token over the symmetric binding, and then sends it to the CXF endpoint using  * the symmetric binding.  */
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
name|SymmetricBindingTest
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
specifier|static
specifier|final
name|String
name|STAX_STSPORT2
init|=
name|allocatePort
argument_list|(
name|StaxSTSServer
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
name|SymmetricBindingTest
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
name|STSServer
name|stsServer
init|=
operator|new
name|STSServer
argument_list|()
decl_stmt|;
name|stsServer
operator|.
name|setContext
argument_list|(
literal|"cxf-ut.xml"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|launchServer
argument_list|(
name|stsServer
argument_list|)
argument_list|)
expr_stmt|;
name|stsServer
operator|=
operator|new
name|STSServer
argument_list|()
expr_stmt|;
name|stsServer
operator|.
name|setContext
argument_list|(
literal|"cxf-ut-encrypted.xml"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|launchServer
argument_list|(
name|stsServer
argument_list|)
argument_list|)
expr_stmt|;
name|StaxSTSServer
name|staxStsServer
init|=
operator|new
name|StaxSTSServer
argument_list|()
decl_stmt|;
name|staxStsServer
operator|.
name|setContext
argument_list|(
literal|"stax-cxf-ut.xml"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|launchServer
argument_list|(
name|staxStsServer
argument_list|)
argument_list|)
expr_stmt|;
name|staxStsServer
operator|=
operator|new
name|StaxSTSServer
argument_list|()
expr_stmt|;
name|staxStsServer
operator|.
name|setContext
argument_list|(
literal|"stax-cxf-ut-encrypted.xml"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|launchServer
argument_list|(
name|staxStsServer
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
name|STSPORT2
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
name|STSPORT2
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
name|STSPORT2
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
name|STSPORT2
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
name|STAX_STSPORT2
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
name|STAX_STSPORT2
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
name|STAX_STSPORT2
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
name|STAX_STSPORT2
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
name|testUsernameTokenSAML1
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
name|SymmetricBindingTest
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
name|SymmetricBindingTest
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
literal|"DoubleItSymmetricSAML1Port"
argument_list|)
decl_stmt|;
name|DoubleItPortType
name|symmetricSaml1Port
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
name|symmetricSaml1Port
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
name|symmetricSaml1Port
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
name|symmetricSaml1Port
argument_list|)
expr_stmt|;
block|}
name|doubleIt
argument_list|(
name|symmetricSaml1Port
argument_list|,
literal|25
argument_list|)
expr_stmt|;
name|TokenTestUtils
operator|.
name|verifyToken
argument_list|(
name|symmetricSaml1Port
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
name|symmetricSaml1Port
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
name|testUsernameTokenSAML2
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
name|SymmetricBindingTest
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
name|SymmetricBindingTest
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
literal|"DoubleItSymmetricSAML2Port"
argument_list|)
decl_stmt|;
name|DoubleItPortType
name|symmetricSaml2Port
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
name|symmetricSaml2Port
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
name|symmetricSaml2Port
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
name|symmetricSaml2Port
argument_list|)
expr_stmt|;
block|}
name|doubleIt
argument_list|(
name|symmetricSaml2Port
argument_list|,
literal|30
argument_list|)
expr_stmt|;
name|TokenTestUtils
operator|.
name|verifyToken
argument_list|(
name|symmetricSaml2Port
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
name|symmetricSaml2Port
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
name|testUsernameTokenSAML1Encrypted
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
name|SymmetricBindingTest
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
name|SymmetricBindingTest
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
literal|"DoubleItSymmetricSAML1EncryptedPort"
argument_list|)
decl_stmt|;
name|DoubleItPortType
name|symmetricSaml1Port
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
name|symmetricSaml1Port
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
name|symmetricSaml1Port
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
name|symmetricSaml1Port
argument_list|)
expr_stmt|;
block|}
name|doubleIt
argument_list|(
name|symmetricSaml1Port
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
name|symmetricSaml1Port
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
name|testUsernameTokenSAML2SecureConversation
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
name|SymmetricBindingTest
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
name|SymmetricBindingTest
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
literal|"DoubleItSymmetricSAML2SecureConversationPort"
argument_list|)
decl_stmt|;
name|DoubleItPortType
name|symmetricSaml2Port
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
name|symmetricSaml2Port
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
name|symmetricSaml2Port
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
name|symmetricSaml2Port
argument_list|)
expr_stmt|;
block|}
name|doubleIt
argument_list|(
name|symmetricSaml2Port
argument_list|,
literal|30
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
name|symmetricSaml2Port
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
name|testUsernameTokenSAML2Dispatch
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
name|SymmetricBindingTest
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
name|SymmetricBindingTest
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
literal|"DoubleItSymmetricSAML2Port"
argument_list|)
decl_stmt|;
name|Dispatch
argument_list|<
name|DOMSource
argument_list|>
name|dispatch
init|=
name|service
operator|.
name|createDispatch
argument_list|(
name|portQName
argument_list|,
name|DOMSource
operator|.
name|class
argument_list|,
name|Service
operator|.
name|Mode
operator|.
name|PAYLOAD
argument_list|,
operator|new
name|AddressingFeature
argument_list|()
argument_list|)
decl_stmt|;
name|updateAddressPort
argument_list|(
name|dispatch
argument_list|,
name|test
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
comment|// Setup STSClient
name|STSClient
name|stsClient
init|=
name|createDispatchSTSClient
argument_list|(
name|bus
argument_list|)
decl_stmt|;
name|String
name|wsdlLocation
init|=
literal|"http://localhost:"
operator|+
name|test
operator|.
name|getStsPort
argument_list|()
operator|+
literal|"/SecurityTokenService/UT?wsdl"
decl_stmt|;
name|stsClient
operator|.
name|setWsdlLocation
argument_list|(
name|wsdlLocation
argument_list|)
expr_stmt|;
comment|// Creating a DOMSource Object for the request
name|DOMSource
name|request
init|=
name|createDOMRequest
argument_list|()
decl_stmt|;
comment|// Make a successful request
name|Client
name|client
init|=
operator|(
operator|(
name|DispatchImpl
argument_list|<
name|DOMSource
argument_list|>
operator|)
name|dispatch
operator|)
operator|.
name|getClient
argument_list|()
decl_stmt|;
name|client
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
if|if
condition|(
name|test
operator|.
name|isStreaming
argument_list|()
condition|)
block|{
name|client
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|ENABLE_STREAMING_SECURITY
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|client
operator|.
name|getResponseContext
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|ENABLE_STREAMING_SECURITY
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
block|}
name|DOMSource
name|response
init|=
name|dispatch
operator|.
name|invoke
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|response
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
name|testUsernameTokenSAML1Dispatch
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
name|SymmetricBindingTest
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
name|SymmetricBindingTest
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
literal|"DoubleItSymmetricSAML1Port"
argument_list|)
decl_stmt|;
name|Dispatch
argument_list|<
name|DOMSource
argument_list|>
name|dispatch
init|=
name|service
operator|.
name|createDispatch
argument_list|(
name|portQName
argument_list|,
name|DOMSource
operator|.
name|class
argument_list|,
name|Service
operator|.
name|Mode
operator|.
name|PAYLOAD
argument_list|,
operator|new
name|AddressingFeature
argument_list|()
argument_list|)
decl_stmt|;
name|updateAddressPort
argument_list|(
name|dispatch
argument_list|,
name|test
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
comment|// Setup STSClient
name|STSClient
name|stsClient
init|=
name|createDispatchSTSClient
argument_list|(
name|bus
argument_list|)
decl_stmt|;
name|String
name|wsdlLocation
init|=
literal|"http://localhost:"
operator|+
name|test
operator|.
name|getStsPort
argument_list|()
operator|+
literal|"/SecurityTokenService/UT?wsdl"
decl_stmt|;
name|stsClient
operator|.
name|setWsdlLocation
argument_list|(
name|wsdlLocation
argument_list|)
expr_stmt|;
comment|// Creating a DOMSource Object for the request
name|DOMSource
name|request
init|=
name|createDOMRequest
argument_list|()
decl_stmt|;
comment|// Make a successful request
name|Client
name|client
init|=
operator|(
operator|(
name|DispatchImpl
argument_list|<
name|DOMSource
argument_list|>
operator|)
name|dispatch
operator|)
operator|.
name|getClient
argument_list|()
decl_stmt|;
name|client
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
comment|//client.getRequestContext().put("find.dispatch.operation", Boolean.TRUE);
if|if
condition|(
name|test
operator|.
name|isStreaming
argument_list|()
condition|)
block|{
name|client
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|ENABLE_STREAMING_SECURITY
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|client
operator|.
name|getResponseContext
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|ENABLE_STREAMING_SECURITY
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
block|}
name|DOMSource
name|response
init|=
name|dispatch
operator|.
name|invoke
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|response
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
specifier|private
name|DOMSource
name|createDOMRequest
parameter_list|()
throws|throws
name|ParserConfigurationException
block|{
comment|// Creating a DOMSource Object for the request
name|DocumentBuilderFactory
name|dbf
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|DocumentBuilder
name|db
init|=
name|dbf
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
name|Document
name|requestDoc
init|=
name|db
operator|.
name|newDocument
argument_list|()
decl_stmt|;
name|Element
name|root
init|=
name|requestDoc
operator|.
name|createElementNS
argument_list|(
literal|"http://www.example.org/schema/DoubleIt"
argument_list|,
literal|"ns2:DoubleIt"
argument_list|)
decl_stmt|;
name|root
operator|.
name|setAttributeNS
argument_list|(
name|WSConstants
operator|.
name|XMLNS_NS
argument_list|,
literal|"xmlns:ns2"
argument_list|,
literal|"http://www.example.org/schema/DoubleIt"
argument_list|)
expr_stmt|;
name|Element
name|number
init|=
name|requestDoc
operator|.
name|createElementNS
argument_list|(
literal|null
argument_list|,
literal|"numberToDouble"
argument_list|)
decl_stmt|;
name|number
operator|.
name|setTextContent
argument_list|(
literal|"25"
argument_list|)
expr_stmt|;
name|root
operator|.
name|appendChild
argument_list|(
name|number
argument_list|)
expr_stmt|;
name|requestDoc
operator|.
name|appendChild
argument_list|(
name|root
argument_list|)
expr_stmt|;
return|return
operator|new
name|DOMSource
argument_list|(
name|requestDoc
argument_list|)
return|;
block|}
specifier|private
name|STSClient
name|createDispatchSTSClient
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
literal|"security.encryption.username"
argument_list|,
literal|"mystskey"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"security.encryption.properties"
argument_list|,
literal|"clientKeystore.properties"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"ws-security.is-bsp-compliant"
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
return|return
name|stsClient
return|;
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

