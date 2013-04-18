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
operator|.
name|saml
package|;
end_package

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
name|interceptor
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
name|interceptor
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
name|cxf
operator|.
name|ws
operator|.
name|security
operator|.
name|wss4j
operator|.
name|AbstractSecurityTest
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
name|wss4j
operator|.
name|Echo
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
name|wss4j
operator|.
name|EchoImpl
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
name|wss4j
operator|.
name|WSS4JOutInterceptor
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
name|wss4j
operator|.
name|WSS4JStaxInInterceptor
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
name|WSSSecurityProperties
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
comment|/**  * In these test-cases, the client is using DOM and the service is using StaX.  */
end_comment

begin_class
specifier|public
class|class
name|DOMToStaxSamlTest
extends|extends
name|AbstractSecurityTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testSaml1
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
name|WSSSecurityProperties
name|inProperties
init|=
operator|new
name|WSSSecurityProperties
argument_list|()
decl_stmt|;
name|inProperties
operator|.
name|setValidateSamlSubjectConfirmation
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|WSS4JStaxInInterceptor
name|inhandler
init|=
operator|new
name|WSS4JStaxInInterceptor
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
name|inhandler
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
name|WSHandlerConstants
operator|.
name|ACTION
argument_list|,
name|WSHandlerConstants
operator|.
name|SAML_TOKEN_UNSIGNED
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|SAML_CALLBACK_REF
argument_list|,
operator|new
name|SAML1CallbackHandler
argument_list|()
argument_list|)
expr_stmt|;
name|WSS4JOutInterceptor
name|ohandler
init|=
operator|new
name|WSS4JOutInterceptor
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
name|testSaml1SignedSenderVouches
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
name|WSSSecurityProperties
name|inProperties
init|=
operator|new
name|WSSSecurityProperties
argument_list|()
decl_stmt|;
name|Properties
name|cryptoProperties
init|=
name|CryptoFactory
operator|.
name|getProperties
argument_list|(
literal|"insecurity.properties"
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
name|inProperties
operator|.
name|setSignatureVerificationCryptoProperties
argument_list|(
name|cryptoProperties
argument_list|)
expr_stmt|;
name|WSS4JStaxInInterceptor
name|inhandler
init|=
operator|new
name|WSS4JStaxInInterceptor
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
name|inhandler
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
name|WSHandlerConstants
operator|.
name|ACTION
argument_list|,
name|WSHandlerConstants
operator|.
name|SAML_TOKEN_SIGNED
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|SAML_CALLBACK_CLASS
argument_list|,
literal|"org.apache.cxf.ws.security.wss4j.saml.SAML1CallbackHandler"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|SIG_KEY_ID
argument_list|,
literal|"DirectReference"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|USER
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|PW_CALLBACK_REF
argument_list|,
operator|new
name|PasswordCallbackHandler
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|SIG_PROP_FILE
argument_list|,
literal|"alice.properties"
argument_list|)
expr_stmt|;
name|WSS4JOutInterceptor
name|ohandler
init|=
operator|new
name|WSS4JOutInterceptor
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
name|testSaml2
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
name|WSSSecurityProperties
name|inProperties
init|=
operator|new
name|WSSSecurityProperties
argument_list|()
decl_stmt|;
name|inProperties
operator|.
name|setValidateSamlSubjectConfirmation
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|WSS4JStaxInInterceptor
name|inhandler
init|=
operator|new
name|WSS4JStaxInInterceptor
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
name|inhandler
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
name|WSHandlerConstants
operator|.
name|ACTION
argument_list|,
name|WSHandlerConstants
operator|.
name|SAML_TOKEN_UNSIGNED
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|SAML_CALLBACK_REF
argument_list|,
operator|new
name|SAML2CallbackHandler
argument_list|()
argument_list|)
expr_stmt|;
name|WSS4JOutInterceptor
name|ohandler
init|=
operator|new
name|WSS4JOutInterceptor
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
name|testSaml2SignedSenderVouches
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
name|WSSSecurityProperties
name|inProperties
init|=
operator|new
name|WSSSecurityProperties
argument_list|()
decl_stmt|;
name|Properties
name|cryptoProperties
init|=
name|CryptoFactory
operator|.
name|getProperties
argument_list|(
literal|"insecurity.properties"
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
name|inProperties
operator|.
name|setSignatureVerificationCryptoProperties
argument_list|(
name|cryptoProperties
argument_list|)
expr_stmt|;
name|WSS4JStaxInInterceptor
name|inhandler
init|=
operator|new
name|WSS4JStaxInInterceptor
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
name|inhandler
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
name|WSHandlerConstants
operator|.
name|ACTION
argument_list|,
name|WSHandlerConstants
operator|.
name|SAML_TOKEN_SIGNED
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|SAML_CALLBACK_CLASS
argument_list|,
literal|"org.apache.cxf.ws.security.wss4j.saml.SAML2CallbackHandler"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|SIG_KEY_ID
argument_list|,
literal|"DirectReference"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|USER
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|PW_CALLBACK_REF
argument_list|,
operator|new
name|PasswordCallbackHandler
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|SIG_PROP_FILE
argument_list|,
literal|"alice.properties"
argument_list|)
expr_stmt|;
name|WSS4JOutInterceptor
name|ohandler
init|=
operator|new
name|WSS4JOutInterceptor
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

