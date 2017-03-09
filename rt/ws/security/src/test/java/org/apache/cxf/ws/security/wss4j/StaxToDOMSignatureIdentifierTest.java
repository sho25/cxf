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
name|java
operator|.
name|util
operator|.
name|Properties
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
name|apache
operator|.
name|wss4j
operator|.
name|stax
operator|.
name|ext
operator|.
name|WSSConstants
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
name|stax
operator|.
name|ext
operator|.
name|WSSSecurityProperties
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
name|stax
operator|.
name|securityToken
operator|.
name|WSSecurityTokenConstants
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

begin_comment
comment|/**  * In these test-cases, the client is using StaX and the service is using DOM. The tests are  * for different Signature Key Identifier methods.  */
end_comment

begin_class
specifier|public
class|class
name|StaxToDOMSignatureIdentifierTest
extends|extends
name|AbstractSecurityTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testSignatureDirectReference
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Create + configure service
name|Service
name|service
init|=
name|createService
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|inProperties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|inProperties
operator|.
name|put
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
name|inProperties
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|PW_CALLBACK_REF
argument_list|,
operator|new
name|TestPwdCallback
argument_list|()
argument_list|)
expr_stmt|;
name|inProperties
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|SIG_VER_PROP_FILE
argument_list|,
literal|"insecurity.properties"
argument_list|)
expr_stmt|;
name|WSS4JInInterceptor
name|inInterceptor
init|=
operator|new
name|WSS4JInInterceptor
argument_list|(
name|inProperties
argument_list|)
decl_stmt|;
name|service
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|inInterceptor
argument_list|)
expr_stmt|;
comment|// Create + configure client
name|Echo
name|echo
init|=
name|createClientProxy
argument_list|()
decl_stmt|;
name|Client
name|client
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|echo
argument_list|)
decl_stmt|;
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
name|WSSSecurityProperties
name|properties
init|=
operator|new
name|WSSSecurityProperties
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|WSSConstants
operator|.
name|Action
argument_list|>
name|actions
init|=
operator|new
name|ArrayList
argument_list|<
name|WSSConstants
operator|.
name|Action
argument_list|>
argument_list|()
decl_stmt|;
name|actions
operator|.
name|add
argument_list|(
name|WSSConstants
operator|.
name|SIGNATURE
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setActions
argument_list|(
name|actions
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setSignatureKeyIdentifier
argument_list|(
name|WSSecurityTokenConstants
operator|.
name|KEYIDENTIFIER_SECURITY_TOKEN_DIRECT_REFERENCE
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setSignatureUser
argument_list|(
literal|"myalias"
argument_list|)
expr_stmt|;
name|Properties
name|cryptoProperties
init|=
name|CryptoFactory
operator|.
name|getProperties
argument_list|(
literal|"outsecurity.properties"
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
name|properties
operator|.
name|setSignatureCryptoProperties
argument_list|(
name|cryptoProperties
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setCallbackHandler
argument_list|(
operator|new
name|TestPwdCallback
argument_list|()
argument_list|)
expr_stmt|;
name|WSS4JStaxOutInterceptor
name|ohandler
init|=
operator|new
name|WSS4JStaxOutInterceptor
argument_list|(
name|properties
argument_list|)
decl_stmt|;
name|client
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|ohandler
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
name|testSignatureIssuerSerial
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Create + configure service
name|Service
name|service
init|=
name|createService
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|inProperties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|inProperties
operator|.
name|put
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
name|inProperties
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|PW_CALLBACK_REF
argument_list|,
operator|new
name|TestPwdCallback
argument_list|()
argument_list|)
expr_stmt|;
name|inProperties
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|SIG_VER_PROP_FILE
argument_list|,
literal|"insecurity.properties"
argument_list|)
expr_stmt|;
name|WSS4JInInterceptor
name|inInterceptor
init|=
operator|new
name|WSS4JInInterceptor
argument_list|(
name|inProperties
argument_list|)
decl_stmt|;
name|service
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|inInterceptor
argument_list|)
expr_stmt|;
comment|// Create + configure client
name|Echo
name|echo
init|=
name|createClientProxy
argument_list|()
decl_stmt|;
name|Client
name|client
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|echo
argument_list|)
decl_stmt|;
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
name|WSSSecurityProperties
name|properties
init|=
operator|new
name|WSSSecurityProperties
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|WSSConstants
operator|.
name|Action
argument_list|>
name|actions
init|=
operator|new
name|ArrayList
argument_list|<
name|WSSConstants
operator|.
name|Action
argument_list|>
argument_list|()
decl_stmt|;
name|actions
operator|.
name|add
argument_list|(
name|WSSConstants
operator|.
name|SIGNATURE
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setActions
argument_list|(
name|actions
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setSignatureKeyIdentifier
argument_list|(
name|WSSecurityTokenConstants
operator|.
name|KeyIdentifier_IssuerSerial
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setSignatureUser
argument_list|(
literal|"myalias"
argument_list|)
expr_stmt|;
name|Properties
name|cryptoProperties
init|=
name|CryptoFactory
operator|.
name|getProperties
argument_list|(
literal|"outsecurity.properties"
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
name|properties
operator|.
name|setSignatureCryptoProperties
argument_list|(
name|cryptoProperties
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setCallbackHandler
argument_list|(
operator|new
name|TestPwdCallback
argument_list|()
argument_list|)
expr_stmt|;
name|WSS4JStaxOutInterceptor
name|ohandler
init|=
operator|new
name|WSS4JStaxOutInterceptor
argument_list|(
name|properties
argument_list|)
decl_stmt|;
name|client
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|ohandler
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
name|testSignatureThumbprint
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Create + configure service
name|Service
name|service
init|=
name|createService
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|inProperties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|inProperties
operator|.
name|put
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
name|inProperties
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|PW_CALLBACK_REF
argument_list|,
operator|new
name|TestPwdCallback
argument_list|()
argument_list|)
expr_stmt|;
name|inProperties
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|SIG_VER_PROP_FILE
argument_list|,
literal|"insecurity.properties"
argument_list|)
expr_stmt|;
name|WSS4JInInterceptor
name|inInterceptor
init|=
operator|new
name|WSS4JInInterceptor
argument_list|(
name|inProperties
argument_list|)
decl_stmt|;
name|service
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|inInterceptor
argument_list|)
expr_stmt|;
comment|// Create + configure client
name|Echo
name|echo
init|=
name|createClientProxy
argument_list|()
decl_stmt|;
name|Client
name|client
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|echo
argument_list|)
decl_stmt|;
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
name|WSSSecurityProperties
name|properties
init|=
operator|new
name|WSSSecurityProperties
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|WSSConstants
operator|.
name|Action
argument_list|>
name|actions
init|=
operator|new
name|ArrayList
argument_list|<
name|WSSConstants
operator|.
name|Action
argument_list|>
argument_list|()
decl_stmt|;
name|actions
operator|.
name|add
argument_list|(
name|WSSConstants
operator|.
name|SIGNATURE
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setActions
argument_list|(
name|actions
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setSignatureKeyIdentifier
argument_list|(
name|WSSecurityTokenConstants
operator|.
name|KEYIDENTIFIER_THUMBPRINT_IDENTIFIER
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setSignatureUser
argument_list|(
literal|"myalias"
argument_list|)
expr_stmt|;
name|Properties
name|cryptoProperties
init|=
name|CryptoFactory
operator|.
name|getProperties
argument_list|(
literal|"outsecurity.properties"
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
name|properties
operator|.
name|setSignatureCryptoProperties
argument_list|(
name|cryptoProperties
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setCallbackHandler
argument_list|(
operator|new
name|TestPwdCallback
argument_list|()
argument_list|)
expr_stmt|;
name|WSS4JStaxOutInterceptor
name|ohandler
init|=
operator|new
name|WSS4JStaxOutInterceptor
argument_list|(
name|properties
argument_list|)
decl_stmt|;
name|client
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|ohandler
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
name|testSignatureX509
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Create + configure service
name|Service
name|service
init|=
name|createService
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|inProperties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|inProperties
operator|.
name|put
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
name|inProperties
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|PW_CALLBACK_REF
argument_list|,
operator|new
name|TestPwdCallback
argument_list|()
argument_list|)
expr_stmt|;
name|inProperties
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|SIG_VER_PROP_FILE
argument_list|,
literal|"insecurity.properties"
argument_list|)
expr_stmt|;
name|inProperties
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|IS_BSP_COMPLIANT
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|WSS4JInInterceptor
name|inInterceptor
init|=
operator|new
name|WSS4JInInterceptor
argument_list|(
name|inProperties
argument_list|)
decl_stmt|;
name|service
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|inInterceptor
argument_list|)
expr_stmt|;
comment|// Create + configure client
name|Echo
name|echo
init|=
name|createClientProxy
argument_list|()
decl_stmt|;
name|Client
name|client
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|echo
argument_list|)
decl_stmt|;
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
name|WSSSecurityProperties
name|properties
init|=
operator|new
name|WSSSecurityProperties
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|WSSConstants
operator|.
name|Action
argument_list|>
name|actions
init|=
operator|new
name|ArrayList
argument_list|<
name|WSSConstants
operator|.
name|Action
argument_list|>
argument_list|()
decl_stmt|;
name|actions
operator|.
name|add
argument_list|(
name|WSSConstants
operator|.
name|SIGNATURE
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setActions
argument_list|(
name|actions
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setSignatureKeyIdentifier
argument_list|(
name|WSSecurityTokenConstants
operator|.
name|KeyIdentifier_X509KeyIdentifier
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setSignatureUser
argument_list|(
literal|"myalias"
argument_list|)
expr_stmt|;
name|Properties
name|cryptoProperties
init|=
name|CryptoFactory
operator|.
name|getProperties
argument_list|(
literal|"outsecurity.properties"
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
name|properties
operator|.
name|setSignatureCryptoProperties
argument_list|(
name|cryptoProperties
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setCallbackHandler
argument_list|(
operator|new
name|TestPwdCallback
argument_list|()
argument_list|)
expr_stmt|;
name|WSS4JStaxOutInterceptor
name|ohandler
init|=
operator|new
name|WSS4JStaxOutInterceptor
argument_list|(
name|properties
argument_list|)
decl_stmt|;
name|client
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|ohandler
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
name|testSignatureKeyValue
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Create + configure service
name|Service
name|service
init|=
name|createService
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|inProperties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|inProperties
operator|.
name|put
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
name|inProperties
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|PW_CALLBACK_REF
argument_list|,
operator|new
name|TestPwdCallback
argument_list|()
argument_list|)
expr_stmt|;
name|inProperties
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|SIG_VER_PROP_FILE
argument_list|,
literal|"insecurity.properties"
argument_list|)
expr_stmt|;
name|inProperties
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|IS_BSP_COMPLIANT
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|WSS4JInInterceptor
name|inInterceptor
init|=
operator|new
name|WSS4JInInterceptor
argument_list|(
name|inProperties
argument_list|)
decl_stmt|;
name|service
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|inInterceptor
argument_list|)
expr_stmt|;
comment|// Create + configure client
name|Echo
name|echo
init|=
name|createClientProxy
argument_list|()
decl_stmt|;
name|Client
name|client
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|echo
argument_list|)
decl_stmt|;
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
name|WSSSecurityProperties
name|properties
init|=
operator|new
name|WSSSecurityProperties
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|WSSConstants
operator|.
name|Action
argument_list|>
name|actions
init|=
operator|new
name|ArrayList
argument_list|<
name|WSSConstants
operator|.
name|Action
argument_list|>
argument_list|()
decl_stmt|;
name|actions
operator|.
name|add
argument_list|(
name|WSSConstants
operator|.
name|SIGNATURE
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setActions
argument_list|(
name|actions
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setSignatureKeyIdentifier
argument_list|(
name|WSSecurityTokenConstants
operator|.
name|KeyIdentifier_KeyValue
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setSignatureUser
argument_list|(
literal|"myalias"
argument_list|)
expr_stmt|;
name|Properties
name|cryptoProperties
init|=
name|CryptoFactory
operator|.
name|getProperties
argument_list|(
literal|"outsecurity.properties"
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
name|properties
operator|.
name|setSignatureCryptoProperties
argument_list|(
name|cryptoProperties
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setCallbackHandler
argument_list|(
operator|new
name|TestPwdCallback
argument_list|()
argument_list|)
expr_stmt|;
name|WSS4JStaxOutInterceptor
name|ohandler
init|=
operator|new
name|WSS4JStaxOutInterceptor
argument_list|(
name|properties
argument_list|)
decl_stmt|;
name|client
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|ohandler
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
specifier|private
name|Service
name|createService
parameter_list|()
block|{
comment|// Create the Service
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
name|LoggingOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|service
return|;
block|}
specifier|private
name|Echo
name|createClientProxy
parameter_list|()
block|{
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
return|return
operator|(
name|Echo
operator|)
name|proxyFac
operator|.
name|create
argument_list|()
return|;
block|}
block|}
end_class

end_unit

