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
name|batch
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
name|STSUtils
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

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|common
operator|.
name|xml
operator|.
name|SAMLConstants
import|;
end_import

begin_comment
comment|/**  * In this test case, a CXF client requests a number of SAML Tokens from an STS using batch processing.  * It uses a simple STSClient implementation to request both a SAML 1.1 and 2.0 token at the same time.  * Batch validation is also tested.  */
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
name|SAMLBatchUnitTest
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
specifier|final
name|TestParam
name|test
decl_stmt|;
specifier|public
name|SAMLBatchUnitTest
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
literal|""
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
literal|""
argument_list|,
literal|false
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
name|testBatchSAMLTokens
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
name|SAMLBatchUnitTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"cxf-client-unit.xml"
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
name|String
name|wsdlLocation
init|=
literal|"https://localhost:"
operator|+
name|test
operator|.
name|getStsPort
argument_list|()
operator|+
literal|"/SecurityTokenService/Transport?wsdl"
decl_stmt|;
name|List
argument_list|<
name|BatchRequest
argument_list|>
name|requestList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|BatchRequest
name|request
init|=
operator|new
name|BatchRequest
argument_list|()
decl_stmt|;
name|request
operator|.
name|setAppliesTo
argument_list|(
literal|"https://localhost:8081/doubleit/services/doubleittransportsaml1"
argument_list|)
expr_stmt|;
name|request
operator|.
name|setTokenType
argument_list|(
literal|"http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV1.1"
argument_list|)
expr_stmt|;
name|request
operator|.
name|setKeyType
argument_list|(
literal|"http://docs.oasis-open.org/ws-sx/ws-trust/200512/Bearer"
argument_list|)
expr_stmt|;
name|requestList
operator|.
name|add
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|request
operator|=
operator|new
name|BatchRequest
argument_list|()
expr_stmt|;
name|request
operator|.
name|setAppliesTo
argument_list|(
literal|"https://localhost:8081/doubleit/services/doubleittransportsaml2"
argument_list|)
expr_stmt|;
name|request
operator|.
name|setTokenType
argument_list|(
literal|"http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0"
argument_list|)
expr_stmt|;
name|request
operator|.
name|setKeyType
argument_list|(
literal|"http://docs.oasis-open.org/ws-sx/ws-trust/200512/Bearer"
argument_list|)
expr_stmt|;
name|requestList
operator|.
name|add
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|String
name|action
init|=
literal|"http://docs.oasis-open.org/ws-sx/ws-trust/200512/BatchIssue"
decl_stmt|;
name|String
name|requestType
init|=
literal|"http://docs.oasis-open.org/ws-sx/ws-trust/200512/BatchIssue"
decl_stmt|;
name|String
name|port
init|=
literal|"{http://docs.oasis-open.org/ws-sx/ws-trust/200512/}Transport_Port"
decl_stmt|;
comment|// Request the token
name|List
argument_list|<
name|SecurityToken
argument_list|>
name|tokens
init|=
name|requestSecurityTokens
argument_list|(
name|bus
argument_list|,
name|wsdlLocation
argument_list|,
name|requestList
argument_list|,
name|action
argument_list|,
name|requestType
argument_list|,
name|port
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|tokens
operator|!=
literal|null
operator|&&
name|tokens
operator|.
name|size
argument_list|()
operator|==
literal|2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokens
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getToken
argument_list|()
operator|.
name|getLocalName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"Assertion"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokens
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getToken
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
operator|.
name|equals
argument_list|(
name|SAMLConstants
operator|.
name|SAML1_NS
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokens
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getToken
argument_list|()
operator|.
name|getLocalName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"Assertion"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokens
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getToken
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
operator|.
name|equals
argument_list|(
name|SAMLConstants
operator|.
name|SAML20_NS
argument_list|)
argument_list|)
expr_stmt|;
comment|// Now validate the tokens
name|requestList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|setValidateTarget
argument_list|(
name|tokens
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getToken
argument_list|()
argument_list|)
expr_stmt|;
name|requestList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|setTokenType
argument_list|(
name|STSUtils
operator|.
name|WST_NS_05_12
operator|+
literal|"/RSTR/Status"
argument_list|)
expr_stmt|;
name|requestList
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|setValidateTarget
argument_list|(
name|tokens
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getToken
argument_list|()
argument_list|)
expr_stmt|;
name|requestList
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|setTokenType
argument_list|(
name|STSUtils
operator|.
name|WST_NS_05_12
operator|+
literal|"/RSTR/Status"
argument_list|)
expr_stmt|;
name|action
operator|=
literal|"http://docs.oasis-open.org/ws-sx/ws-trust/200512/BatchValidate"
expr_stmt|;
name|requestType
operator|=
literal|"http://docs.oasis-open.org/ws-sx/ws-trust/200512/BatchValidate"
expr_stmt|;
name|port
operator|=
literal|"{http://docs.oasis-open.org/ws-sx/ws-trust/200512/}Transport_Port2"
expr_stmt|;
name|validateSecurityTokens
argument_list|(
name|bus
argument_list|,
name|wsdlLocation
argument_list|,
name|requestList
argument_list|,
name|action
argument_list|,
name|requestType
argument_list|,
name|port
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
name|List
argument_list|<
name|SecurityToken
argument_list|>
name|requestSecurityTokens
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|String
name|wsdlLocation
parameter_list|,
name|List
argument_list|<
name|BatchRequest
argument_list|>
name|requestList
parameter_list|,
name|String
name|action
parameter_list|,
name|String
name|requestType
parameter_list|,
name|String
name|port
parameter_list|)
throws|throws
name|Exception
block|{
name|SimpleBatchSTSClient
name|stsClient
init|=
operator|new
name|SimpleBatchSTSClient
argument_list|(
name|bus
argument_list|)
decl_stmt|;
name|stsClient
operator|.
name|setWsdlLocation
argument_list|(
name|wsdlLocation
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
name|port
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
name|STS_TOKEN_PROPERTIES
argument_list|,
literal|"serviceKeystore.properties"
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setEnableLifetime
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setProperties
argument_list|(
name|properties
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
name|requestBatchSecurityTokens
argument_list|(
name|requestList
argument_list|,
name|action
argument_list|,
name|requestType
argument_list|)
return|;
block|}
specifier|private
name|List
argument_list|<
name|SecurityToken
argument_list|>
name|validateSecurityTokens
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|String
name|wsdlLocation
parameter_list|,
name|List
argument_list|<
name|BatchRequest
argument_list|>
name|requestList
parameter_list|,
name|String
name|action
parameter_list|,
name|String
name|requestType
parameter_list|,
name|String
name|port
parameter_list|)
throws|throws
name|Exception
block|{
name|SimpleBatchSTSClient
name|stsClient
init|=
operator|new
name|SimpleBatchSTSClient
argument_list|(
name|bus
argument_list|)
decl_stmt|;
name|stsClient
operator|.
name|setWsdlLocation
argument_list|(
name|wsdlLocation
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
name|port
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
name|STS_TOKEN_PROPERTIES
argument_list|,
literal|"serviceKeystore.properties"
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setProperties
argument_list|(
name|properties
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
name|validateBatchSecurityTokens
argument_list|(
name|requestList
argument_list|,
name|action
argument_list|,
name|requestType
argument_list|)
return|;
block|}
block|}
end_class

end_unit

