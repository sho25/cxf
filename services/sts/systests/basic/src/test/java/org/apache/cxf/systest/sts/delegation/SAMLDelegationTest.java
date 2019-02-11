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
name|delegation
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
name|java
operator|.
name|util
operator|.
name|Properties
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
name|jaxws
operator|.
name|context
operator|.
name|WrappedMessageContext
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
name|MessageImpl
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
name|sts
operator|.
name|STSConstants
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
name|sts
operator|.
name|StaticSTSProperties
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
name|sts
operator|.
name|request
operator|.
name|KeyRequirements
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
name|sts
operator|.
name|request
operator|.
name|TokenRequirements
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
name|sts
operator|.
name|service
operator|.
name|EncryptionProperties
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
name|sts
operator|.
name|token
operator|.
name|provider
operator|.
name|SAMLTokenProvider
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
name|sts
operator|.
name|token
operator|.
name|provider
operator|.
name|TokenProviderParameters
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
name|sts
operator|.
name|token
operator|.
name|provider
operator|.
name|TokenProviderResponse
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
name|CommonCallbackHandler
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
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|WSS4JConstants
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
name|crypto
operator|.
name|Crypto
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
name|crypto
operator|.
name|CryptoFactory
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
name|WSSecurityException
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
name|principal
operator|.
name|CustomTokenPrincipal
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
comment|/**  * Some tests for sending a SAML Token OnBehalfOf/ActAs to the STS. The STS is set up with  * two endpoints, one requiring a UsernameToken over TLS, the other just requiring TLS  * without client authentication (insecure, but used as part of the test process) with a  * SAML DelegationHandler.  */
end_comment

begin_class
specifier|public
class|class
name|SAMLDelegationTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|private
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
name|SAML2_TOKEN_TYPE
init|=
literal|"http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0"
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
name|DEFAULT_ADDRESS
init|=
literal|"https://localhost:8081/doubleit/services/doubleittransportsaml1"
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
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testSAMLOnBehalfOf
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
name|SAMLDelegationTest
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
comment|// Get a token from the UT endpoint first
name|SecurityToken
name|token
init|=
name|requestSecurityToken
argument_list|(
name|SAML2_TOKEN_TYPE
argument_list|,
name|BEARER_KEYTYPE
argument_list|,
name|bus
argument_list|,
name|DEFAULT_ADDRESS
argument_list|,
literal|"Transport_UT_Port"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|SAML2_TOKEN_TYPE
argument_list|,
name|token
operator|.
name|getTokenType
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|token
operator|.
name|getToken
argument_list|()
argument_list|)
expr_stmt|;
comment|// Use the first token as OnBehalfOf to get another token
comment|// First try with the UT endpoint. This should fail as there is no Delegation Handler.
try|try
block|{
name|requestSecurityToken
argument_list|(
name|SAML2_TOKEN_TYPE
argument_list|,
name|BEARER_KEYTYPE
argument_list|,
name|token
operator|.
name|getToken
argument_list|()
argument_list|,
name|bus
argument_list|,
name|DEFAULT_ADDRESS
argument_list|,
literal|true
argument_list|,
literal|"Transport_UT_Port"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on no delegation handler"
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
comment|// Now send to the Transport endpoint.
name|SecurityToken
name|token2
init|=
name|requestSecurityToken
argument_list|(
name|SAML2_TOKEN_TYPE
argument_list|,
name|BEARER_KEYTYPE
argument_list|,
name|token
operator|.
name|getToken
argument_list|()
argument_list|,
name|bus
argument_list|,
name|DEFAULT_ADDRESS
argument_list|,
literal|true
argument_list|,
literal|"Transport_Port"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|SAML2_TOKEN_TYPE
argument_list|,
name|token2
operator|.
name|getTokenType
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|token2
operator|.
name|getToken
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
name|testSAMLActAs
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
name|SAMLDelegationTest
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
comment|// Get a token from the UT endpoint first
name|SecurityToken
name|token
init|=
name|requestSecurityToken
argument_list|(
name|SAML2_TOKEN_TYPE
argument_list|,
name|BEARER_KEYTYPE
argument_list|,
name|bus
argument_list|,
name|DEFAULT_ADDRESS
argument_list|,
literal|"Transport_UT_Port"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|SAML2_TOKEN_TYPE
argument_list|,
name|token
operator|.
name|getTokenType
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|token
operator|.
name|getToken
argument_list|()
argument_list|)
expr_stmt|;
comment|// Use the first token as ActAs to get another token
comment|// First try with the UT endpoint. This should fail as there is no Delegation Handler.
try|try
block|{
name|requestSecurityToken
argument_list|(
name|SAML2_TOKEN_TYPE
argument_list|,
name|BEARER_KEYTYPE
argument_list|,
name|token
operator|.
name|getToken
argument_list|()
argument_list|,
name|bus
argument_list|,
name|DEFAULT_ADDRESS
argument_list|,
literal|false
argument_list|,
literal|"Transport_UT_Port"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on no delegation handler"
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
comment|// Now send to the Transport endpoint.
name|SecurityToken
name|token2
init|=
name|requestSecurityToken
argument_list|(
name|SAML2_TOKEN_TYPE
argument_list|,
name|BEARER_KEYTYPE
argument_list|,
name|token
operator|.
name|getToken
argument_list|()
argument_list|,
name|bus
argument_list|,
name|DEFAULT_ADDRESS
argument_list|,
literal|false
argument_list|,
literal|"Transport_Port"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|SAML2_TOKEN_TYPE
argument_list|,
name|token2
operator|.
name|getTokenType
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|token2
operator|.
name|getToken
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
name|testTransportForgedDelegationToken
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
name|SAMLDelegationTest
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
name|Crypto
name|crypto
init|=
name|CryptoFactory
operator|.
name|getInstance
argument_list|(
name|getEncryptionProperties
argument_list|()
argument_list|)
decl_stmt|;
name|CallbackHandler
name|callbackHandler
init|=
operator|new
name|CommonCallbackHandler
argument_list|()
decl_stmt|;
comment|// Create SAML token
name|Element
name|samlToken
init|=
name|createSAMLAssertion
argument_list|(
name|WSS4JConstants
operator|.
name|WSS_SAML2_TOKEN_TYPE
argument_list|,
name|STSConstants
operator|.
name|BEARER_KEY_KEYTYPE
argument_list|,
name|crypto
argument_list|,
literal|"eve"
argument_list|,
name|callbackHandler
argument_list|,
literal|"alice"
argument_list|,
literal|"a-issuer"
argument_list|)
decl_stmt|;
try|try
block|{
name|requestSecurityToken
argument_list|(
name|SAML2_TOKEN_TYPE
argument_list|,
name|BEARER_KEYTYPE
argument_list|,
name|samlToken
argument_list|,
name|bus
argument_list|,
name|DEFAULT_ADDRESS
argument_list|,
literal|true
argument_list|,
literal|"Transport_Port"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on a forged delegation token"
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
try|try
block|{
name|requestSecurityToken
argument_list|(
name|SAML2_TOKEN_TYPE
argument_list|,
name|BEARER_KEYTYPE
argument_list|,
name|samlToken
argument_list|,
name|bus
argument_list|,
name|DEFAULT_ADDRESS
argument_list|,
literal|false
argument_list|,
literal|"Transport_Port"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on a forged delegation token"
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
name|testTransportUnsignedDelegationToken
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
name|SAMLDelegationTest
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
comment|// Create SAML token
name|Element
name|samlToken
init|=
name|createUnsignedSAMLAssertion
argument_list|(
name|WSS4JConstants
operator|.
name|WSS_SAML2_TOKEN_TYPE
argument_list|,
name|STSConstants
operator|.
name|BEARER_KEY_KEYTYPE
argument_list|,
literal|"alice"
argument_list|,
literal|"a-issuer"
argument_list|)
decl_stmt|;
try|try
block|{
name|requestSecurityToken
argument_list|(
name|SAML2_TOKEN_TYPE
argument_list|,
name|BEARER_KEYTYPE
argument_list|,
name|samlToken
argument_list|,
name|bus
argument_list|,
name|DEFAULT_ADDRESS
argument_list|,
literal|true
argument_list|,
literal|"Transport_Port"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on a unsigned delegation token"
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
try|try
block|{
name|requestSecurityToken
argument_list|(
name|SAML2_TOKEN_TYPE
argument_list|,
name|BEARER_KEYTYPE
argument_list|,
name|samlToken
argument_list|,
name|bus
argument_list|,
name|DEFAULT_ADDRESS
argument_list|,
literal|false
argument_list|,
literal|"Transport_Port"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on a unsigned delegation token"
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
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
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
parameter_list|,
name|String
name|wsdlPort
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
literal|true
argument_list|,
name|wsdlPort
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
name|boolean
name|onBehalfOf
parameter_list|,
name|String
name|wsdlPort
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
name|String
name|port
init|=
name|STSPORT
decl_stmt|;
name|stsClient
operator|.
name|setWsdlLocation
argument_list|(
literal|"https://localhost:"
operator|+
name|port
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
if|if
condition|(
name|wsdlPort
operator|!=
literal|null
condition|)
block|{
name|stsClient
operator|.
name|setEndpointName
argument_list|(
literal|"{http://docs.oasis-open.org/ws-sx/ws-trust/200512/}"
operator|+
name|wsdlPort
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|stsClient
operator|.
name|setEndpointName
argument_list|(
literal|"{http://docs.oasis-open.org/ws-sx/ws-trust/200512/}Transport_Port"
argument_list|)
expr_stmt|;
block|}
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
name|IS_BSP_COMPLIANT
argument_list|,
literal|"false"
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
if|if
condition|(
name|onBehalfOf
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
else|else
block|{
name|stsClient
operator|.
name|setActAs
argument_list|(
name|supportingToken
argument_list|)
expr_stmt|;
block|}
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
return|return
name|stsClient
operator|.
name|requestSecurityToken
argument_list|(
name|endpointAddress
argument_list|)
return|;
block|}
comment|/*      * Mock up an SAML assertion element      */
specifier|private
name|Element
name|createSAMLAssertion
parameter_list|(
name|String
name|tokenType
parameter_list|,
name|String
name|keyType
parameter_list|,
name|Crypto
name|crypto
parameter_list|,
name|String
name|signatureUsername
parameter_list|,
name|CallbackHandler
name|callbackHandler
parameter_list|,
name|String
name|user
parameter_list|,
name|String
name|issuer
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|SAMLTokenProvider
name|samlTokenProvider
init|=
operator|new
name|SAMLTokenProvider
argument_list|()
decl_stmt|;
name|TokenProviderParameters
name|providerParameters
init|=
name|createProviderParameters
argument_list|(
name|tokenType
argument_list|,
name|keyType
argument_list|,
name|crypto
argument_list|,
name|signatureUsername
argument_list|,
name|callbackHandler
argument_list|,
name|user
argument_list|,
name|issuer
argument_list|)
decl_stmt|;
name|TokenProviderResponse
name|providerResponse
init|=
name|samlTokenProvider
operator|.
name|createToken
argument_list|(
name|providerParameters
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|providerResponse
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|providerResponse
operator|.
name|getToken
argument_list|()
operator|!=
literal|null
operator|&&
name|providerResponse
operator|.
name|getTokenId
argument_list|()
operator|!=
literal|null
argument_list|)
expr_stmt|;
return|return
operator|(
name|Element
operator|)
name|providerResponse
operator|.
name|getToken
argument_list|()
return|;
block|}
specifier|private
name|Element
name|createUnsignedSAMLAssertion
parameter_list|(
name|String
name|tokenType
parameter_list|,
name|String
name|keyType
parameter_list|,
name|String
name|user
parameter_list|,
name|String
name|issuer
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|SAMLTokenProvider
name|samlTokenProvider
init|=
operator|new
name|SAMLTokenProvider
argument_list|()
decl_stmt|;
name|samlTokenProvider
operator|.
name|setSignToken
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|TokenProviderParameters
name|providerParameters
init|=
name|createProviderParameters
argument_list|(
name|tokenType
argument_list|,
name|keyType
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|user
argument_list|,
name|issuer
argument_list|)
decl_stmt|;
name|TokenProviderResponse
name|providerResponse
init|=
name|samlTokenProvider
operator|.
name|createToken
argument_list|(
name|providerParameters
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|providerResponse
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|providerResponse
operator|.
name|getToken
argument_list|()
operator|!=
literal|null
operator|&&
name|providerResponse
operator|.
name|getTokenId
argument_list|()
operator|!=
literal|null
argument_list|)
expr_stmt|;
return|return
operator|(
name|Element
operator|)
name|providerResponse
operator|.
name|getToken
argument_list|()
return|;
block|}
specifier|private
name|TokenProviderParameters
name|createProviderParameters
parameter_list|(
name|String
name|tokenType
parameter_list|,
name|String
name|keyType
parameter_list|,
name|Crypto
name|crypto
parameter_list|,
name|String
name|signatureUsername
parameter_list|,
name|CallbackHandler
name|callbackHandler
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|issuer
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|TokenProviderParameters
name|parameters
init|=
operator|new
name|TokenProviderParameters
argument_list|()
decl_stmt|;
name|TokenRequirements
name|tokenRequirements
init|=
operator|new
name|TokenRequirements
argument_list|()
decl_stmt|;
name|tokenRequirements
operator|.
name|setTokenType
argument_list|(
name|tokenType
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setTokenRequirements
argument_list|(
name|tokenRequirements
argument_list|)
expr_stmt|;
name|KeyRequirements
name|keyRequirements
init|=
operator|new
name|KeyRequirements
argument_list|()
decl_stmt|;
name|keyRequirements
operator|.
name|setKeyType
argument_list|(
name|keyType
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setKeyRequirements
argument_list|(
name|keyRequirements
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setPrincipal
argument_list|(
operator|new
name|CustomTokenPrincipal
argument_list|(
name|username
argument_list|)
argument_list|)
expr_stmt|;
comment|// Mock up message context
name|MessageImpl
name|msg
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|WrappedMessageContext
name|msgCtx
init|=
operator|new
name|WrappedMessageContext
argument_list|(
name|msg
argument_list|)
decl_stmt|;
name|parameters
operator|.
name|setMessageContext
argument_list|(
name|msgCtx
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setAppliesToAddress
argument_list|(
literal|"http://dummy-service.com/dummy"
argument_list|)
expr_stmt|;
comment|// Add STSProperties object
name|StaticSTSProperties
name|stsProperties
init|=
operator|new
name|StaticSTSProperties
argument_list|()
decl_stmt|;
name|stsProperties
operator|.
name|setSignatureCrypto
argument_list|(
name|crypto
argument_list|)
expr_stmt|;
name|stsProperties
operator|.
name|setSignatureUsername
argument_list|(
name|signatureUsername
argument_list|)
expr_stmt|;
name|stsProperties
operator|.
name|setCallbackHandler
argument_list|(
name|callbackHandler
argument_list|)
expr_stmt|;
name|stsProperties
operator|.
name|setIssuer
argument_list|(
name|issuer
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setStsProperties
argument_list|(
name|stsProperties
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setEncryptionProperties
argument_list|(
operator|new
name|EncryptionProperties
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|parameters
return|;
block|}
specifier|private
name|Properties
name|getEncryptionProperties
parameter_list|()
block|{
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"org.apache.ws.security.crypto.provider"
argument_list|,
literal|"org.apache.ws.security.components.crypto.Merlin"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"org.apache.ws.security.crypto.merlin.keystore.password"
argument_list|,
literal|"evespass"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"org.apache.ws.security.crypto.merlin.keystore.file"
argument_list|,
literal|"eve.jks"
argument_list|)
expr_stmt|;
return|return
name|properties
return|;
block|}
block|}
end_class

end_unit

