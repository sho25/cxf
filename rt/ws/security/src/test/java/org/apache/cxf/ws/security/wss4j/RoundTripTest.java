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
name|ws
operator|.
name|security
operator|.
name|wss4j
package|;
end_package

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
name|saaj
operator|.
name|SAAJInInterceptor
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
name|saaj
operator|.
name|SAAJOutInterceptor
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
name|Server
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
name|ext
operator|.
name|logging
operator|.
name|LoggingInInterceptor
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
name|ext
operator|.
name|logging
operator|.
name|LoggingOutInterceptor
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
name|jaxws
operator|.
name|JaxWsProxyFactoryBean
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
name|JaxWsServerFactoryBean
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
name|service
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
name|transport
operator|.
name|local
operator|.
name|LocalTransportFactory
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
name|handler
operator|.
name|WSHandlerConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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

begin_class
specifier|public
class|class
name|RoundTripTest
extends|extends
name|AbstractSecurityTest
block|{
specifier|private
name|WSS4JInInterceptor
name|wsIn
decl_stmt|;
specifier|private
name|WSS4JOutInterceptor
name|wsOut
decl_stmt|;
specifier|private
name|Echo
name|echo
decl_stmt|;
specifier|private
name|Client
name|client
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUpService
parameter_list|()
throws|throws
name|Exception
block|{
name|JaxWsServerFactoryBean
name|factory
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setServiceBean
argument_list|(
operator|new
name|EchoImpl
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
literal|"local://Echo"
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setTransportId
argument_list|(
name|LocalTransportFactory
operator|.
name|TRANSPORT_ID
argument_list|)
expr_stmt|;
name|Server
name|server
init|=
name|factory
operator|.
name|create
argument_list|()
decl_stmt|;
name|Service
name|service
init|=
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getService
argument_list|()
decl_stmt|;
name|service
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|SAAJInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|service
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|service
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|SAAJOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|service
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|wsIn
operator|=
operator|new
name|WSS4JInInterceptor
argument_list|()
expr_stmt|;
name|wsIn
operator|.
name|setProperty
argument_list|(
name|WSHandlerConstants
operator|.
name|SIG_VER_PROP_FILE
argument_list|,
literal|"insecurity.properties"
argument_list|)
expr_stmt|;
name|wsIn
operator|.
name|setProperty
argument_list|(
name|WSHandlerConstants
operator|.
name|DEC_PROP_FILE
argument_list|,
literal|"insecurity.properties"
argument_list|)
expr_stmt|;
name|wsIn
operator|.
name|setProperty
argument_list|(
name|WSHandlerConstants
operator|.
name|PW_CALLBACK_CLASS
argument_list|,
name|TestPwdCallback
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|service
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|wsIn
argument_list|)
expr_stmt|;
name|wsOut
operator|=
operator|new
name|WSS4JOutInterceptor
argument_list|()
expr_stmt|;
name|wsOut
operator|.
name|setProperty
argument_list|(
name|WSHandlerConstants
operator|.
name|SIG_PROP_FILE
argument_list|,
literal|"outsecurity.properties"
argument_list|)
expr_stmt|;
name|wsOut
operator|.
name|setProperty
argument_list|(
name|WSHandlerConstants
operator|.
name|ENC_PROP_FILE
argument_list|,
literal|"outsecurity.properties"
argument_list|)
expr_stmt|;
name|wsOut
operator|.
name|setProperty
argument_list|(
name|WSHandlerConstants
operator|.
name|USER
argument_list|,
literal|"myalias"
argument_list|)
expr_stmt|;
name|wsOut
operator|.
name|setProperty
argument_list|(
literal|"password"
argument_list|,
literal|"myAliasPassword"
argument_list|)
expr_stmt|;
name|wsOut
operator|.
name|setProperty
argument_list|(
name|WSHandlerConstants
operator|.
name|PW_CALLBACK_CLASS
argument_list|,
name|TestPwdCallback
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|service
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|wsOut
argument_list|)
expr_stmt|;
comment|// Create the client
name|JaxWsProxyFactoryBean
name|proxyFac
init|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
decl_stmt|;
name|proxyFac
operator|.
name|setServiceClass
argument_list|(
name|Echo
operator|.
name|class
argument_list|)
expr_stmt|;
name|proxyFac
operator|.
name|setAddress
argument_list|(
literal|"local://Echo"
argument_list|)
expr_stmt|;
name|proxyFac
operator|.
name|getClientFactoryBean
argument_list|()
operator|.
name|setTransportId
argument_list|(
name|LocalTransportFactory
operator|.
name|TRANSPORT_ID
argument_list|)
expr_stmt|;
name|echo
operator|=
operator|(
name|Echo
operator|)
name|proxyFac
operator|.
name|create
argument_list|()
expr_stmt|;
name|client
operator|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|echo
argument_list|)
expr_stmt|;
name|client
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|wsIn
argument_list|)
expr_stmt|;
name|client
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|SAAJInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|wsOut
argument_list|)
expr_stmt|;
name|client
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|SAAJOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSignature
parameter_list|()
throws|throws
name|Exception
block|{
name|wsIn
operator|.
name|setProperty
argument_list|(
name|WSHandlerConstants
operator|.
name|ACTION
argument_list|,
name|WSHandlerConstants
operator|.
name|SIGNATURE
argument_list|)
expr_stmt|;
name|wsOut
operator|.
name|setProperty
argument_list|(
name|WSHandlerConstants
operator|.
name|ACTION
argument_list|,
name|WSHandlerConstants
operator|.
name|SIGNATURE
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|echo
operator|.
name|echo
argument_list|(
literal|"test"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEncryptionPlusSig
parameter_list|()
throws|throws
name|Exception
block|{
name|wsIn
operator|.
name|setProperty
argument_list|(
name|WSHandlerConstants
operator|.
name|ACTION
argument_list|,
name|WSHandlerConstants
operator|.
name|ENCRYPT
operator|+
literal|" "
operator|+
name|WSHandlerConstants
operator|.
name|SIGNATURE
argument_list|)
expr_stmt|;
name|wsOut
operator|.
name|setProperty
argument_list|(
name|WSHandlerConstants
operator|.
name|ACTION
argument_list|,
name|WSHandlerConstants
operator|.
name|ENCRYPT
operator|+
literal|" "
operator|+
name|WSHandlerConstants
operator|.
name|SIGNATURE
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|echo
operator|.
name|echo
argument_list|(
literal|"test"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUsernameToken
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|actions
init|=
name|WSHandlerConstants
operator|.
name|ENCRYPT
operator|+
literal|" "
operator|+
name|WSHandlerConstants
operator|.
name|SIGNATURE
operator|+
literal|" "
operator|+
name|WSHandlerConstants
operator|.
name|TIMESTAMP
operator|+
literal|" "
operator|+
name|WSHandlerConstants
operator|.
name|USERNAME_TOKEN
decl_stmt|;
name|wsIn
operator|.
name|setProperty
argument_list|(
name|WSHandlerConstants
operator|.
name|ACTION
argument_list|,
name|actions
argument_list|)
expr_stmt|;
name|wsOut
operator|.
name|setProperty
argument_list|(
name|WSHandlerConstants
operator|.
name|ACTION
argument_list|,
name|actions
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|echo
operator|.
name|echo
argument_list|(
literal|"test"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

