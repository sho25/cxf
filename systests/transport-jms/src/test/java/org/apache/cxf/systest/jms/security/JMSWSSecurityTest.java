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
name|jms
operator|.
name|security
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
name|soap
operator|.
name|SOAPFaultException
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
name|hello_world_jms
operator|.
name|HelloWorldPortType
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
name|hello_world_jms
operator|.
name|HelloWorldService
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
name|testutil
operator|.
name|common
operator|.
name|EmbeddedJMSBrokerLauncher
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
name|wss4j
operator|.
name|common
operator|.
name|ConfigurationConstants
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
name|bean
operator|.
name|AudienceRestrictionBean
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
name|bean
operator|.
name|ConditionsBean
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
name|builder
operator|.
name|SAML2Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
name|BeforeClass
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
comment|/**  * Some WS-Security over JMS tests  */
end_comment

begin_class
specifier|public
class|class
name|JMSWSSecurityTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|allocatePort
argument_list|(
name|JMSWSSecurityTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|EmbeddedJMSBrokerLauncher
name|broker
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|wsdlStrings
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
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
name|broker
operator|=
operator|new
name|EmbeddedJMSBrokerLauncher
argument_list|(
literal|"tcp://localhost:"
operator|+
name|PORT
argument_list|)
expr_stmt|;
name|launchServer
argument_list|(
name|broker
argument_list|)
expr_stmt|;
name|launchServer
argument_list|(
operator|new
name|Server
argument_list|(
name|broker
argument_list|)
argument_list|)
expr_stmt|;
name|createStaticBus
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|assertSame
argument_list|(
name|getStaticBus
argument_list|()
argument_list|,
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|wsdlStrings
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|public
name|URL
name|getWSDLURL
parameter_list|(
name|String
name|s
parameter_list|)
throws|throws
name|Exception
block|{
name|URL
name|u
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
name|s
argument_list|)
decl_stmt|;
if|if
condition|(
name|u
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"WSDL classpath resource not found "
operator|+
name|s
argument_list|)
throw|;
block|}
name|String
name|wsdlString
init|=
name|u
operator|.
name|toString
argument_list|()
operator|.
name|intern
argument_list|()
decl_stmt|;
name|wsdlStrings
operator|.
name|add
argument_list|(
name|wsdlString
argument_list|)
expr_stmt|;
name|broker
operator|.
name|updateWsdl
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|wsdlString
argument_list|)
expr_stmt|;
return|return
name|u
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUnsignedSAML2Token
parameter_list|()
throws|throws
name|Exception
block|{
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/hello_world_jms"
argument_list|,
literal|"HelloWorldService"
argument_list|)
decl_stmt|;
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/hello_world_jms"
argument_list|,
literal|"HelloWorldPort"
argument_list|)
decl_stmt|;
name|URL
name|wsdl
init|=
name|getWSDLURL
argument_list|(
literal|"/wsdl/jms_test.wsdl"
argument_list|)
decl_stmt|;
name|HelloWorldService
name|service
init|=
operator|new
name|HelloWorldService
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|String
name|response
init|=
operator|new
name|String
argument_list|(
literal|"Bonjour"
argument_list|)
decl_stmt|;
name|HelloWorldPortType
name|greeter
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|HelloWorldPortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|SamlCallbackHandler
name|callbackHandler
init|=
operator|new
name|SamlCallbackHandler
argument_list|()
decl_stmt|;
name|callbackHandler
operator|.
name|setSignAssertion
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|callbackHandler
operator|.
name|setConfirmationMethod
argument_list|(
name|SAML2Constants
operator|.
name|CONF_BEARER
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|outProperties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|outProperties
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|ACTION
argument_list|,
name|ConfigurationConstants
operator|.
name|SAML_TOKEN_UNSIGNED
argument_list|)
expr_stmt|;
name|outProperties
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|SAML_CALLBACK_REF
argument_list|,
name|callbackHandler
argument_list|)
expr_stmt|;
name|WSS4JOutInterceptor
name|outInterceptor
init|=
operator|new
name|WSS4JOutInterceptor
argument_list|(
name|outProperties
argument_list|)
decl_stmt|;
name|Client
name|client
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|greeter
argument_list|)
decl_stmt|;
name|client
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|outInterceptor
argument_list|)
expr_stmt|;
name|String
name|reply
init|=
name|greeter
operator|.
name|sayHi
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"no response received from service"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|response
argument_list|,
name|reply
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
name|greeter
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUnsignedSAML2AudienceRestrictionTokenURI
parameter_list|()
throws|throws
name|Exception
block|{
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/hello_world_jms"
argument_list|,
literal|"HelloWorldService"
argument_list|)
decl_stmt|;
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/hello_world_jms"
argument_list|,
literal|"HelloWorldPort"
argument_list|)
decl_stmt|;
name|URL
name|wsdl
init|=
name|getWSDLURL
argument_list|(
literal|"/wsdl/jms_test.wsdl"
argument_list|)
decl_stmt|;
name|HelloWorldService
name|service
init|=
operator|new
name|HelloWorldService
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|String
name|response
init|=
operator|new
name|String
argument_list|(
literal|"Bonjour"
argument_list|)
decl_stmt|;
name|HelloWorldPortType
name|greeter
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|HelloWorldPortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|SamlCallbackHandler
name|callbackHandler
init|=
operator|new
name|SamlCallbackHandler
argument_list|()
decl_stmt|;
name|callbackHandler
operator|.
name|setSignAssertion
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|callbackHandler
operator|.
name|setConfirmationMethod
argument_list|(
name|SAML2Constants
operator|.
name|CONF_BEARER
argument_list|)
expr_stmt|;
name|ConditionsBean
name|conditions
init|=
operator|new
name|ConditionsBean
argument_list|()
decl_stmt|;
name|conditions
operator|.
name|setTokenPeriodMinutes
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|audiences
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|audiences
operator|.
name|add
argument_list|(
literal|"jms:jndi:dynamicQueues/test.jmstransport.text"
argument_list|)
expr_stmt|;
name|AudienceRestrictionBean
name|audienceRestrictionBean
init|=
operator|new
name|AudienceRestrictionBean
argument_list|()
decl_stmt|;
name|audienceRestrictionBean
operator|.
name|setAudienceURIs
argument_list|(
name|audiences
argument_list|)
expr_stmt|;
name|conditions
operator|.
name|setAudienceRestrictions
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|audienceRestrictionBean
argument_list|)
argument_list|)
expr_stmt|;
name|callbackHandler
operator|.
name|setConditions
argument_list|(
name|conditions
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|outProperties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|outProperties
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|ACTION
argument_list|,
name|ConfigurationConstants
operator|.
name|SAML_TOKEN_UNSIGNED
argument_list|)
expr_stmt|;
name|outProperties
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|SAML_CALLBACK_REF
argument_list|,
name|callbackHandler
argument_list|)
expr_stmt|;
name|WSS4JOutInterceptor
name|outInterceptor
init|=
operator|new
name|WSS4JOutInterceptor
argument_list|(
name|outProperties
argument_list|)
decl_stmt|;
name|Client
name|client
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|greeter
argument_list|)
decl_stmt|;
name|client
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|outInterceptor
argument_list|)
expr_stmt|;
name|String
name|reply
init|=
name|greeter
operator|.
name|sayHi
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"no response received from service"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|response
argument_list|,
name|reply
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
name|greeter
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUnsignedSAML2AudienceRestrictionTokenBadURI
parameter_list|()
throws|throws
name|Exception
block|{
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/hello_world_jms"
argument_list|,
literal|"HelloWorldService"
argument_list|)
decl_stmt|;
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/hello_world_jms"
argument_list|,
literal|"HelloWorldPort"
argument_list|)
decl_stmt|;
name|URL
name|wsdl
init|=
name|getWSDLURL
argument_list|(
literal|"/wsdl/jms_test.wsdl"
argument_list|)
decl_stmt|;
name|HelloWorldService
name|service
init|=
operator|new
name|HelloWorldService
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|HelloWorldPortType
name|greeter
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|HelloWorldPortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|SamlCallbackHandler
name|callbackHandler
init|=
operator|new
name|SamlCallbackHandler
argument_list|()
decl_stmt|;
name|callbackHandler
operator|.
name|setSignAssertion
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|callbackHandler
operator|.
name|setConfirmationMethod
argument_list|(
name|SAML2Constants
operator|.
name|CONF_BEARER
argument_list|)
expr_stmt|;
name|ConditionsBean
name|conditions
init|=
operator|new
name|ConditionsBean
argument_list|()
decl_stmt|;
name|conditions
operator|.
name|setTokenPeriodMinutes
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|audiences
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|audiences
operator|.
name|add
argument_list|(
literal|"jms:jndi:dynamicQueues/test.jmstransport.text.bad"
argument_list|)
expr_stmt|;
name|AudienceRestrictionBean
name|audienceRestrictionBean
init|=
operator|new
name|AudienceRestrictionBean
argument_list|()
decl_stmt|;
name|audienceRestrictionBean
operator|.
name|setAudienceURIs
argument_list|(
name|audiences
argument_list|)
expr_stmt|;
name|conditions
operator|.
name|setAudienceRestrictions
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|audienceRestrictionBean
argument_list|)
argument_list|)
expr_stmt|;
name|callbackHandler
operator|.
name|setConditions
argument_list|(
name|conditions
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|outProperties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|outProperties
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|ACTION
argument_list|,
name|ConfigurationConstants
operator|.
name|SAML_TOKEN_UNSIGNED
argument_list|)
expr_stmt|;
name|outProperties
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|SAML_CALLBACK_REF
argument_list|,
name|callbackHandler
argument_list|)
expr_stmt|;
name|WSS4JOutInterceptor
name|outInterceptor
init|=
operator|new
name|WSS4JOutInterceptor
argument_list|(
name|outProperties
argument_list|)
decl_stmt|;
name|Client
name|client
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|greeter
argument_list|)
decl_stmt|;
name|client
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|outInterceptor
argument_list|)
expr_stmt|;
try|try
block|{
name|greeter
operator|.
name|sayHi
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on a bad audience restriction"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPFaultException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
operator|(
operator|(
name|java
operator|.
name|io
operator|.
name|Closeable
operator|)
name|greeter
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUnsignedSAML2AudienceRestrictionTokenServiceName
parameter_list|()
throws|throws
name|Exception
block|{
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/hello_world_jms"
argument_list|,
literal|"HelloWorldService"
argument_list|)
decl_stmt|;
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/hello_world_jms"
argument_list|,
literal|"HelloWorldPort"
argument_list|)
decl_stmt|;
name|URL
name|wsdl
init|=
name|getWSDLURL
argument_list|(
literal|"/wsdl/jms_test.wsdl"
argument_list|)
decl_stmt|;
name|HelloWorldService
name|service
init|=
operator|new
name|HelloWorldService
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|String
name|response
init|=
operator|new
name|String
argument_list|(
literal|"Bonjour"
argument_list|)
decl_stmt|;
name|HelloWorldPortType
name|greeter
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|HelloWorldPortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|SamlCallbackHandler
name|callbackHandler
init|=
operator|new
name|SamlCallbackHandler
argument_list|()
decl_stmt|;
name|callbackHandler
operator|.
name|setSignAssertion
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|callbackHandler
operator|.
name|setConfirmationMethod
argument_list|(
name|SAML2Constants
operator|.
name|CONF_BEARER
argument_list|)
expr_stmt|;
name|ConditionsBean
name|conditions
init|=
operator|new
name|ConditionsBean
argument_list|()
decl_stmt|;
name|conditions
operator|.
name|setTokenPeriodMinutes
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|audiences
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|audiences
operator|.
name|add
argument_list|(
literal|"{http://cxf.apache.org/hello_world_jms}HelloWorldService"
argument_list|)
expr_stmt|;
name|AudienceRestrictionBean
name|audienceRestrictionBean
init|=
operator|new
name|AudienceRestrictionBean
argument_list|()
decl_stmt|;
name|audienceRestrictionBean
operator|.
name|setAudienceURIs
argument_list|(
name|audiences
argument_list|)
expr_stmt|;
name|conditions
operator|.
name|setAudienceRestrictions
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|audienceRestrictionBean
argument_list|)
argument_list|)
expr_stmt|;
name|callbackHandler
operator|.
name|setConditions
argument_list|(
name|conditions
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|outProperties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|outProperties
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|ACTION
argument_list|,
name|ConfigurationConstants
operator|.
name|SAML_TOKEN_UNSIGNED
argument_list|)
expr_stmt|;
name|outProperties
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|SAML_CALLBACK_REF
argument_list|,
name|callbackHandler
argument_list|)
expr_stmt|;
name|WSS4JOutInterceptor
name|outInterceptor
init|=
operator|new
name|WSS4JOutInterceptor
argument_list|(
name|outProperties
argument_list|)
decl_stmt|;
name|Client
name|client
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|greeter
argument_list|)
decl_stmt|;
name|client
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|outInterceptor
argument_list|)
expr_stmt|;
name|String
name|reply
init|=
name|greeter
operator|.
name|sayHi
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"no response received from service"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|response
argument_list|,
name|reply
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
name|greeter
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUnsignedSAML2AudienceRestrictionTokenBadServiceName
parameter_list|()
throws|throws
name|Exception
block|{
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/hello_world_jms"
argument_list|,
literal|"HelloWorldService"
argument_list|)
decl_stmt|;
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/hello_world_jms"
argument_list|,
literal|"HelloWorldPort"
argument_list|)
decl_stmt|;
name|URL
name|wsdl
init|=
name|getWSDLURL
argument_list|(
literal|"/wsdl/jms_test.wsdl"
argument_list|)
decl_stmt|;
name|HelloWorldService
name|service
init|=
operator|new
name|HelloWorldService
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|HelloWorldPortType
name|greeter
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|HelloWorldPortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|SamlCallbackHandler
name|callbackHandler
init|=
operator|new
name|SamlCallbackHandler
argument_list|()
decl_stmt|;
name|callbackHandler
operator|.
name|setSignAssertion
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|callbackHandler
operator|.
name|setConfirmationMethod
argument_list|(
name|SAML2Constants
operator|.
name|CONF_BEARER
argument_list|)
expr_stmt|;
name|ConditionsBean
name|conditions
init|=
operator|new
name|ConditionsBean
argument_list|()
decl_stmt|;
name|conditions
operator|.
name|setTokenPeriodMinutes
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|audiences
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|audiences
operator|.
name|add
argument_list|(
literal|"{http://cxf.apache.org/hello_world_jms}BadHelloWorldService"
argument_list|)
expr_stmt|;
name|AudienceRestrictionBean
name|audienceRestrictionBean
init|=
operator|new
name|AudienceRestrictionBean
argument_list|()
decl_stmt|;
name|audienceRestrictionBean
operator|.
name|setAudienceURIs
argument_list|(
name|audiences
argument_list|)
expr_stmt|;
name|conditions
operator|.
name|setAudienceRestrictions
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|audienceRestrictionBean
argument_list|)
argument_list|)
expr_stmt|;
name|callbackHandler
operator|.
name|setConditions
argument_list|(
name|conditions
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|outProperties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|outProperties
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|ACTION
argument_list|,
name|ConfigurationConstants
operator|.
name|SAML_TOKEN_UNSIGNED
argument_list|)
expr_stmt|;
name|outProperties
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|SAML_CALLBACK_REF
argument_list|,
name|callbackHandler
argument_list|)
expr_stmt|;
name|WSS4JOutInterceptor
name|outInterceptor
init|=
operator|new
name|WSS4JOutInterceptor
argument_list|(
name|outProperties
argument_list|)
decl_stmt|;
name|Client
name|client
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|greeter
argument_list|)
decl_stmt|;
name|client
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|outInterceptor
argument_list|)
expr_stmt|;
try|try
block|{
name|greeter
operator|.
name|sayHi
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on a bad audience restriction"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPFaultException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
operator|(
operator|(
name|java
operator|.
name|io
operator|.
name|Closeable
operator|)
name|greeter
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

