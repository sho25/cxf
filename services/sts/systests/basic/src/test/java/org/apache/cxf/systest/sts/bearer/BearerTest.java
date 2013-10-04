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
name|bearer
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
name|endpoint
operator|.
name|Endpoint
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
name|wss4j
operator|.
name|common
operator|.
name|saml
operator|.
name|SAMLCallback
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
name|saml
operator|.
name|SAMLUtil
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
name|saml
operator|.
name|SamlAssertionWrapper
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

begin_comment
comment|/**  * Test the Bearer TokenType over TLS.  *   * It tests both DOM + StAX clients against the DOM server  */
end_comment

begin_class
specifier|public
class|class
name|BearerTest
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
operator|||
name|deployment
operator|==
literal|null
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
name|testSAML2Bearer
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
name|BearerTest
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
name|BearerTest
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
literal|"DoubleItTransportSAML2BearerPort"
argument_list|)
decl_stmt|;
name|DoubleItPortType
name|transportSaml2Port
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
name|transportSaml2Port
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
name|transportSaml2Port
argument_list|,
name|STSPORT
argument_list|)
expr_stmt|;
block|}
comment|// DOM
name|doubleIt
argument_list|(
name|transportSaml2Port
argument_list|,
literal|45
argument_list|)
expr_stmt|;
comment|// Streaming
name|transportSaml2Port
operator|=
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
expr_stmt|;
name|updateAddressPort
argument_list|(
name|transportSaml2Port
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
name|transportSaml2Port
argument_list|,
name|STSPORT
argument_list|)
expr_stmt|;
block|}
name|SecurityTestUtil
operator|.
name|enableStreaming
argument_list|(
name|transportSaml2Port
argument_list|)
expr_stmt|;
name|doubleIt
argument_list|(
name|transportSaml2Port
argument_list|,
literal|45
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
name|transportSaml2Port
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
name|testSAML2UnsignedBearer
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
name|BearerTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"cxf-unsigned-client.xml"
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
name|BearerTest
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
literal|"DoubleItTransportSAML2BearerPort"
argument_list|)
decl_stmt|;
name|DoubleItPortType
name|transportSaml2Port
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
name|transportSaml2Port
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
name|transportSaml2Port
argument_list|,
name|STSPORT
argument_list|)
expr_stmt|;
block|}
comment|//
comment|// Create a SAML2 Bearer Assertion and add it to the TokenStore so that the
comment|// IssuedTokenInterceptorProvider does not invoke on the STS
comment|//
name|Client
name|client
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|transportSaml2Port
argument_list|)
decl_stmt|;
name|Endpoint
name|ep
init|=
name|client
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|String
name|id
init|=
literal|"1234"
decl_stmt|;
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|setProperty
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
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|setProperty
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN_ID
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|TokenStore
name|store
init|=
operator|(
name|TokenStore
operator|)
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getProperty
argument_list|(
name|TokenStore
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|SAMLCallback
name|samlCallback
init|=
operator|new
name|SAMLCallback
argument_list|()
decl_stmt|;
name|SAMLUtil
operator|.
name|doSAMLCallback
argument_list|(
operator|new
name|Saml2CallbackHandler
argument_list|()
argument_list|,
name|samlCallback
argument_list|)
expr_stmt|;
name|SamlAssertionWrapper
name|assertion
init|=
operator|new
name|SamlAssertionWrapper
argument_list|(
name|samlCallback
argument_list|)
decl_stmt|;
name|DocumentBuilderFactory
name|dbf
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|dbf
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|DocumentBuilder
name|db
init|=
name|dbf
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
name|Element
name|assertionElement
init|=
name|assertion
operator|.
name|toDOM
argument_list|(
name|db
operator|.
name|newDocument
argument_list|()
argument_list|)
decl_stmt|;
name|SecurityToken
name|tok
init|=
operator|new
name|SecurityToken
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|tok
operator|.
name|setTokenType
argument_list|(
name|WSConstants
operator|.
name|WSS_SAML2_TOKEN_TYPE
argument_list|)
expr_stmt|;
name|tok
operator|.
name|setToken
argument_list|(
name|assertionElement
argument_list|)
expr_stmt|;
name|store
operator|.
name|add
argument_list|(
name|tok
argument_list|)
expr_stmt|;
name|doubleIt
argument_list|(
name|transportSaml2Port
argument_list|,
literal|50
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
name|transportSaml2Port
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
name|testSAML2UnsignedBearerStreaming
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
name|BearerTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"cxf-unsigned-client.xml"
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
name|BearerTest
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
literal|"DoubleItTransportSAML2BearerPort"
argument_list|)
decl_stmt|;
name|DoubleItPortType
name|transportSaml2Port
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
name|transportSaml2Port
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
name|transportSaml2Port
argument_list|,
name|STSPORT
argument_list|)
expr_stmt|;
block|}
name|SecurityTestUtil
operator|.
name|enableStreaming
argument_list|(
name|transportSaml2Port
argument_list|)
expr_stmt|;
comment|//
comment|// Create a SAML2 Bearer Assertion and add it to the TokenStore so that the
comment|// IssuedTokenInterceptorProvider does not invoke on the STS
comment|//
name|Client
name|client
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|transportSaml2Port
argument_list|)
decl_stmt|;
name|Endpoint
name|ep
init|=
name|client
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|String
name|id
init|=
literal|"1234"
decl_stmt|;
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|setProperty
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
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|setProperty
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN_ID
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|TokenStore
name|store
init|=
operator|(
name|TokenStore
operator|)
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getProperty
argument_list|(
name|TokenStore
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|SAMLCallback
name|samlCallback
init|=
operator|new
name|SAMLCallback
argument_list|()
decl_stmt|;
name|SAMLUtil
operator|.
name|doSAMLCallback
argument_list|(
operator|new
name|Saml2CallbackHandler
argument_list|()
argument_list|,
name|samlCallback
argument_list|)
expr_stmt|;
name|SamlAssertionWrapper
name|assertion
init|=
operator|new
name|SamlAssertionWrapper
argument_list|(
name|samlCallback
argument_list|)
decl_stmt|;
name|DocumentBuilderFactory
name|dbf
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|dbf
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|DocumentBuilder
name|db
init|=
name|dbf
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
name|Element
name|assertionElement
init|=
name|assertion
operator|.
name|toDOM
argument_list|(
name|db
operator|.
name|newDocument
argument_list|()
argument_list|)
decl_stmt|;
name|SecurityToken
name|tok
init|=
operator|new
name|SecurityToken
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|tok
operator|.
name|setTokenType
argument_list|(
name|WSConstants
operator|.
name|WSS_SAML2_TOKEN_TYPE
argument_list|)
expr_stmt|;
name|tok
operator|.
name|setToken
argument_list|(
name|assertionElement
argument_list|)
expr_stmt|;
name|store
operator|.
name|add
argument_list|(
name|tok
argument_list|)
expr_stmt|;
name|doubleIt
argument_list|(
name|transportSaml2Port
argument_list|,
literal|50
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
name|transportSaml2Port
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
name|testSAML2BearerNoBinding
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
name|BearerTest
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
name|BearerTest
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
literal|"DoubleItTransportSAML2BearerPort2"
argument_list|)
decl_stmt|;
name|DoubleItPortType
name|transportSaml2Port
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
name|transportSaml2Port
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
name|transportSaml2Port
argument_list|,
name|STSPORT
argument_list|)
expr_stmt|;
block|}
comment|// DOM
name|doubleIt
argument_list|(
name|transportSaml2Port
argument_list|,
literal|45
argument_list|)
expr_stmt|;
comment|// Streaming
name|transportSaml2Port
operator|=
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
expr_stmt|;
name|updateAddressPort
argument_list|(
name|transportSaml2Port
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
name|transportSaml2Port
argument_list|,
name|STSPORT
argument_list|)
expr_stmt|;
block|}
name|SecurityTestUtil
operator|.
name|enableStreaming
argument_list|(
name|transportSaml2Port
argument_list|)
expr_stmt|;
name|doubleIt
argument_list|(
name|transportSaml2Port
argument_list|,
literal|45
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
name|transportSaml2Port
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

