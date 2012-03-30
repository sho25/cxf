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
name|soap12
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
name|binding
operator|.
name|soap
operator|.
name|SoapFault
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
comment|/**  * This is a test for invoking on an STS using SOAP 1.2 via the TransportBinding. The CXF client gets a   * token from the STS over TLS, and then sends it to the CXF endpoint over TLS.  */
end_comment

begin_class
specifier|public
class|class
name|Soap12Test
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
name|SAML1_TOKEN_TYPE
init|=
literal|"http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV1.1"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PUBLIC_KEY_KEYTYPE
init|=
literal|"http://docs.oasis-open.org/ws-sx/ws-trust/200512/PublicKey"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|BEARER_KEYTYPE
init|=
literal|"http://docs.oasis-open.org/ws-sx/ws-trust/200512/Bearer"
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
block|{
name|SecurityTestUtil
operator|.
name|cleanup
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
name|testSAML2
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
name|Soap12Test
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
name|Soap12Test
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
literal|"DoubleItTransportSAML2Port"
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
name|doubleIt
argument_list|(
name|transportSaml2Port
argument_list|,
literal|30
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test the endpoint address sent to the STS as part of AppliesTo. If the STS does not      * recognise the endpoint address it does not issue a token.      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testFaultCode
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
name|Soap12Test
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
try|try
block|{
name|String
name|badAddress
init|=
literal|"https://localhost:"
operator|+
name|PORT
operator|+
literal|"/doubleit/services/doubleitbadtransportsaml1"
decl_stmt|;
name|requestSecurityToken
argument_list|(
name|SAML1_TOKEN_TYPE
argument_list|,
name|BEARER_KEYTYPE
argument_list|,
name|bus
argument_list|,
name|badAddress
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on a bad endpoint address"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SoapFault
name|ex
parameter_list|)
block|{
comment|// expected
block|}
block|}
specifier|private
name|SecurityToken
name|requestSecurityToken
parameter_list|(
name|String
name|tokenType
parameter_list|,
name|String
name|keyType
parameter_list|,
name|Bus
name|bus
parameter_list|,
name|String
name|endpointAddress
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|requestSecurityToken
argument_list|(
name|tokenType
argument_list|,
name|keyType
argument_list|,
literal|null
argument_list|,
name|bus
argument_list|,
name|endpointAddress
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|private
name|SecurityToken
name|requestSecurityToken
parameter_list|(
name|String
name|tokenType
parameter_list|,
name|String
name|keyType
parameter_list|,
name|Element
name|supportingToken
parameter_list|,
name|Bus
name|bus
parameter_list|,
name|String
name|endpointAddress
parameter_list|,
name|String
name|context
parameter_list|)
throws|throws
name|Exception
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
name|setWsdlLocation
argument_list|(
literal|"https://localhost:"
operator|+
name|STSPORT
operator|+
literal|"/SecurityTokenService/TransportSoap12?wsdl"
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
literal|"{http://docs.oasis-open.org/ws-sx/ws-trust/200512/}Transport_Soap12_Port"
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
name|SecurityConstants
operator|.
name|USERNAME
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|CALLBACK_HANDLER
argument_list|,
literal|"org.apache.cxf.systest.sts.common.CommonCallbackHandler"
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
literal|"clientKeystore.properties"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|ENCRYPT_USERNAME
argument_list|,
literal|"mystskey"
argument_list|)
expr_stmt|;
if|if
condition|(
name|PUBLIC_KEY_KEYTYPE
operator|.
name|equals
argument_list|(
name|keyType
argument_list|)
condition|)
block|{
name|properties
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|STS_TOKEN_USERNAME
argument_list|,
literal|"myclientkey"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|STS_TOKEN_PROPERTIES
argument_list|,
literal|"clientKeystore.properties"
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setUseCertificateForConfirmationKeyInfo
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|supportingToken
operator|!=
literal|null
condition|)
block|{
name|stsClient
operator|.
name|setOnBehalfOf
argument_list|(
name|supportingToken
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|stsClient
operator|.
name|setContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
name|stsClient
operator|.
name|setProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setTokenType
argument_list|(
name|tokenType
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setKeyType
argument_list|(
name|keyType
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setAddressingNamespace
argument_list|(
literal|"http://www.w3.org/2005/08/addressing"
argument_list|)
expr_stmt|;
return|return
name|stsClient
operator|.
name|requestSecurityToken
argument_list|(
name|endpointAddress
argument_list|)
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

