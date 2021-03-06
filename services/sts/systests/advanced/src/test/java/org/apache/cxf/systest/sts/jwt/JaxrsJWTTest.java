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
name|jwt
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
name|Collections
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
name|ws
operator|.
name|rs
operator|.
name|client
operator|.
name|ClientRequestContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|client
operator|.
name|ClientRequestFilter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|HttpHeaders
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
name|jaxrs
operator|.
name|client
operator|.
name|WebClient
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
name|Message
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
name|phase
operator|.
name|Phase
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
name|phase
operator|.
name|PhaseInterceptorChain
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
name|policy
operator|.
name|interceptors
operator|.
name|STSTokenOutInterceptor
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
name|cxf
operator|.
name|ws
operator|.
name|security
operator|.
name|trust
operator|.
name|STSTokenRetriever
operator|.
name|TokenRequestParams
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
name|assertTrue
import|;
end_import

begin_comment
comment|/**  * In this test case, a CXF JAX-RS client gets a JWT token from the STS + sends it to the  * service provider, which validates it.  */
end_comment

begin_class
specifier|public
class|class
name|JaxrsJWTTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|JWT_TOKEN_TYPE
init|=
literal|"urn:ietf:params:oauth:token-type:jwt"
decl_stmt|;
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
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testSuccessfulInvocation
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
name|JaxrsJWTTest
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
specifier|final
name|String
name|address
init|=
literal|"https://localhost:"
operator|+
name|PORT
operator|+
literal|"/doubleit/services/doubleit-rs"
decl_stmt|;
specifier|final
name|int
name|numToDouble
init|=
literal|25
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|providers
init|=
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|JwtOutFilter
argument_list|()
argument_list|)
decl_stmt|;
name|WebClient
name|client
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|address
argument_list|,
name|providers
argument_list|)
decl_stmt|;
name|client
operator|.
name|type
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|accept
argument_list|(
literal|"text/plain"
argument_list|)
expr_stmt|;
name|STSClient
name|stsClient
init|=
name|getSTSClient
argument_list|(
name|JWT_TOKEN_TYPE
argument_list|,
name|bus
argument_list|)
decl_stmt|;
name|STSTokenOutInterceptor
name|stsInterceptor
init|=
operator|new
name|STSTokenOutInterceptor
argument_list|(
name|Phase
operator|.
name|PRE_LOGICAL
argument_list|,
name|stsClient
argument_list|,
operator|new
name|TokenRequestParams
argument_list|()
argument_list|)
decl_stmt|;
name|stsInterceptor
operator|.
name|getBefore
argument_list|()
operator|.
name|add
argument_list|(
name|JwtOutFilter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|WebClient
operator|.
name|getConfig
argument_list|(
name|client
argument_list|)
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|stsInterceptor
argument_list|)
expr_stmt|;
name|int
name|resp
init|=
name|client
operator|.
name|post
argument_list|(
name|numToDouble
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|2
operator|*
name|numToDouble
argument_list|,
name|resp
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
name|STSClient
name|getSTSClient
parameter_list|(
name|String
name|tokenType
parameter_list|,
name|Bus
name|bus
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
name|setSendKeyType
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return
name|stsClient
return|;
block|}
specifier|private
specifier|static
class|class
name|JwtOutFilter
implements|implements
name|ClientRequestFilter
block|{
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ClientRequestContext
name|requestContext
parameter_list|)
throws|throws
name|IOException
block|{
name|SecurityToken
name|token
init|=
operator|(
name|SecurityToken
operator|)
name|requestContext
operator|.
name|getProperty
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN
argument_list|)
decl_stmt|;
if|if
condition|(
name|token
operator|==
literal|null
condition|)
block|{
name|Message
name|m
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
name|token
operator|=
operator|(
name|SecurityToken
operator|)
name|m
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|token
operator|!=
literal|null
operator|&&
name|token
operator|.
name|getToken
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|requestContext
operator|.
name|getHeaders
argument_list|()
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|AUTHORIZATION
argument_list|,
literal|"JWT"
operator|+
literal|" "
operator|+
name|token
operator|.
name|getToken
argument_list|()
operator|.
name|getTextContent
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

