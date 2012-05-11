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
name|secure_conv
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
name|junit
operator|.
name|BeforeClass
import|;
end_import

begin_comment
comment|/**  * In this test case, a CXF client requests a SecurityContextToken from an STS and then cancels it. When  * cancelling the token, the WSDL of the STS has an EndorsingSupportingToken consisting of the   * SecureConversationToken. The client must use the secret associated with the SecurityContextToken it gets   * back from the STS to sign the Timestamp.  */
end_comment

begin_class
specifier|public
class|class
name|SecurityContextTokenCancelTest
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
name|testCancelSecurityContextToken
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
name|SecurityContextTokenCancelTest
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
name|String
name|wsdlLocation
init|=
literal|"https://localhost:"
operator|+
name|STSPORT
operator|+
literal|"/SecurityTokenService/TransportSCT?wsdl"
decl_stmt|;
name|SecurityToken
name|token
init|=
name|requestSecurityToken
argument_list|(
name|bus
argument_list|,
name|wsdlLocation
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|token
operator|.
name|getSecret
argument_list|()
operator|!=
literal|null
operator|&&
name|token
operator|.
name|getSecret
argument_list|()
operator|.
name|length
operator|>
literal|0
argument_list|)
expr_stmt|;
comment|// Cancel the SecurityContextToken - this should fail as the secret associated with the SCT
comment|// is not used to sign some part of the message
name|String
name|port
init|=
literal|"{http://docs.oasis-open.org/ws-sx/ws-trust/200512/}Transport_Port"
decl_stmt|;
name|boolean
name|cancelled
init|=
name|cancelSecurityToken
argument_list|(
name|bus
argument_list|,
name|wsdlLocation
argument_list|,
name|port
argument_list|,
literal|true
argument_list|,
name|token
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|cancelled
argument_list|)
expr_stmt|;
name|String
name|endorsingPort
init|=
literal|"{http://docs.oasis-open.org/ws-sx/ws-trust/200512/}Transport_Endorsing_Port"
decl_stmt|;
name|cancelled
operator|=
name|cancelSecurityToken
argument_list|(
name|bus
argument_list|,
name|wsdlLocation
argument_list|,
name|endorsingPort
argument_list|,
literal|true
argument_list|,
name|token
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cancelled
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
name|SecurityToken
name|requestSecurityToken
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|String
name|wsdlLocation
parameter_list|,
name|boolean
name|enableEntropy
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
literal|"ws-security.callback-handler"
argument_list|,
literal|"org.apache.cxf.systest.sts.common.CommonCallbackHandler"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"ws-security.sts.token.properties"
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
name|setSecureConv
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setRequiresEntropy
argument_list|(
name|enableEntropy
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setKeySize
argument_list|(
literal|128
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
literal|null
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|cancelSecurityToken
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|String
name|wsdlLocation
parameter_list|,
name|String
name|port
parameter_list|,
name|boolean
name|enableEntropy
parameter_list|,
name|SecurityToken
name|securityToken
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
name|SIGNATURE_USERNAME
argument_list|,
literal|"myservicekey"
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
name|properties
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|SIGNATURE_PROPERTIES
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
name|setSecureConv
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setRequiresEntropy
argument_list|(
name|enableEntropy
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
name|cancelSecurityToken
argument_list|(
name|securityToken
argument_list|)
return|;
block|}
block|}
end_class

end_unit

