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
name|ws
operator|.
name|saml
package|;
end_package

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigInteger
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
name|javax
operator|.
name|crypto
operator|.
name|Cipher
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|SecretKey
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|spec
operator|.
name|SecretKeySpec
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
name|ws
operator|.
name|saml
operator|.
name|client
operator|.
name|SamlCallbackHandler
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
name|ws
operator|.
name|saml
operator|.
name|server
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
name|ws
operator|.
name|security
operator|.
name|saml
operator|.
name|ext
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
name|BeforeClass
import|;
end_import

begin_import
import|import
name|wssec
operator|.
name|saml
operator|.
name|DoubleItPortType
import|;
end_import

begin_import
import|import
name|wssec
operator|.
name|saml
operator|.
name|DoubleItService
import|;
end_import

begin_comment
comment|/**  * A set of tests for SAML Tokens.  */
end_comment

begin_class
specifier|public
class|class
name|SamlTokenTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
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
specifier|static
specifier|final
name|String
name|PORT2
init|=
name|allocatePort
argument_list|(
name|Server
operator|.
name|class
argument_list|,
literal|2
argument_list|)
decl_stmt|;
specifier|private
name|boolean
name|unrestrictedPoliciesInstalled
init|=
name|checkUnrestrictedPoliciesInstalled
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
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testSaml1OverTransport
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
name|SamlTokenTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client/client.xml"
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
name|DoubleItService
name|service
init|=
operator|new
name|DoubleItService
argument_list|()
decl_stmt|;
name|DoubleItPortType
name|saml1Port
init|=
name|service
operator|.
name|getDoubleItSaml1TransportPort
argument_list|()
decl_stmt|;
name|updateAddressPort
argument_list|(
name|saml1Port
argument_list|,
name|PORT2
argument_list|)
expr_stmt|;
try|try
block|{
name|saml1Port
operator|.
name|doubleIt
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|25
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected failure on an invocation with no SAML Assertion"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|soap
operator|.
name|SOAPFaultException
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"No SAML CallbackHandler available"
argument_list|)
argument_list|)
expr_stmt|;
block|}
operator|(
operator|(
name|BindingProvider
operator|)
name|saml1Port
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.saml-callback-handler"
argument_list|,
operator|new
name|SamlCallbackHandler
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|saml1Port
operator|.
name|doubleIt
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|25
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected failure on an invocation with a SAML2 Assertion"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|soap
operator|.
name|SOAPFaultException
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Wrong SAML Version"
argument_list|)
argument_list|)
expr_stmt|;
block|}
operator|(
operator|(
name|BindingProvider
operator|)
name|saml1Port
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.saml-callback-handler"
argument_list|,
operator|new
name|SamlCallbackHandler
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|BigInteger
name|result
init|=
name|saml1Port
operator|.
name|doubleIt
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|25
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|equals
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|50
argument_list|)
argument_list|)
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
name|testSaml2OverSymmetric
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|unrestrictedPoliciesInstalled
condition|)
block|{
return|return;
block|}
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
name|SamlTokenTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client/client.xml"
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
name|DoubleItService
name|service
init|=
operator|new
name|DoubleItService
argument_list|()
decl_stmt|;
name|DoubleItPortType
name|saml2Port
init|=
name|service
operator|.
name|getDoubleItSaml2SymmetricPort
argument_list|()
decl_stmt|;
name|updateAddressPort
argument_list|(
name|saml2Port
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
try|try
block|{
name|saml2Port
operator|.
name|doubleIt
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|25
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected failure on an invocation with no SAML Assertion"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|soap
operator|.
name|SOAPFaultException
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"No SAML CallbackHandler available"
argument_list|)
argument_list|)
expr_stmt|;
block|}
operator|(
operator|(
name|BindingProvider
operator|)
name|saml2Port
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.saml-callback-handler"
argument_list|,
operator|new
name|SamlCallbackHandler
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|saml2Port
operator|.
name|doubleIt
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|25
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected failure on an invocation with a SAML1 Assertion"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|soap
operator|.
name|SOAPFaultException
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Wrong SAML Version"
argument_list|)
argument_list|)
expr_stmt|;
block|}
operator|(
operator|(
name|BindingProvider
operator|)
name|saml2Port
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.saml-callback-handler"
argument_list|,
operator|new
name|SamlCallbackHandler
argument_list|()
argument_list|)
expr_stmt|;
name|BigInteger
name|result
init|=
name|saml2Port
operator|.
name|doubleIt
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|25
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|equals
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|50
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|SamlCallbackHandler
name|callbackHandler
init|=
operator|new
name|SamlCallbackHandler
argument_list|()
decl_stmt|;
name|callbackHandler
operator|.
name|setConfirmationMethod
argument_list|(
name|SAML2Constants
operator|.
name|CONF_BEARER
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|saml2Port
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.saml-callback-handler"
argument_list|,
name|callbackHandler
argument_list|)
expr_stmt|;
name|saml2Port
operator|.
name|doubleIt
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|25
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected failure on an invocation with a invalid SAML2 Assertion"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|soap
operator|.
name|SOAPFaultException
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"SAML token security failure"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Some negative tests. Send a sender-vouches assertion as a SupportingToken...this will      * fail as the provider will demand that there is a signature covering both the assertion      * and the message body.      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testSaml2OverSymmetricSupporting
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|unrestrictedPoliciesInstalled
condition|)
block|{
return|return;
block|}
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
name|SamlTokenTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client/client.xml"
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
name|DoubleItService
name|service
init|=
operator|new
name|DoubleItService
argument_list|()
decl_stmt|;
name|DoubleItPortType
name|saml2Port
init|=
name|service
operator|.
name|getDoubleItSaml2SymmetricSupportingPort
argument_list|()
decl_stmt|;
name|updateAddressPort
argument_list|(
name|saml2Port
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|saml2Port
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.saml-callback-handler"
argument_list|,
operator|new
name|SamlCallbackHandler
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|saml2Port
operator|.
name|doubleIt
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|25
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected failure on an invocation with an unsigned SAML SV Assertion"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|soap
operator|.
name|SOAPFaultException
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Assertion fails sender-vouches requirements"
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
name|Test
specifier|public
name|void
name|testSaml2OverAsymmetric
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|unrestrictedPoliciesInstalled
condition|)
block|{
return|return;
block|}
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
name|SamlTokenTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client/client.xml"
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
name|DoubleItService
name|service
init|=
operator|new
name|DoubleItService
argument_list|()
decl_stmt|;
name|DoubleItPortType
name|saml2Port
init|=
name|service
operator|.
name|getDoubleItSaml2AsymmetricPort
argument_list|()
decl_stmt|;
name|updateAddressPort
argument_list|(
name|saml2Port
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
try|try
block|{
name|saml2Port
operator|.
name|doubleIt
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|25
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected failure on an invocation with no SAML Assertion"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|soap
operator|.
name|SOAPFaultException
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"No SAML CallbackHandler available"
argument_list|)
argument_list|)
expr_stmt|;
block|}
operator|(
operator|(
name|BindingProvider
operator|)
name|saml2Port
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.saml-callback-handler"
argument_list|,
operator|new
name|SamlCallbackHandler
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|saml2Port
operator|.
name|doubleIt
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|25
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected failure on an invocation with a SAML1 Assertion"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|soap
operator|.
name|SOAPFaultException
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Wrong SAML Version"
argument_list|)
argument_list|)
expr_stmt|;
block|}
operator|(
operator|(
name|BindingProvider
operator|)
name|saml2Port
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.saml-callback-handler"
argument_list|,
operator|new
name|SamlCallbackHandler
argument_list|()
argument_list|)
expr_stmt|;
name|BigInteger
name|result
init|=
name|saml2Port
operator|.
name|doubleIt
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|25
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|equals
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|50
argument_list|)
argument_list|)
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
name|testSaml1SelfSignedOverTransport
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
name|SamlTokenTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client/client.xml"
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
name|DoubleItService
name|service
init|=
operator|new
name|DoubleItService
argument_list|()
decl_stmt|;
name|DoubleItPortType
name|saml1Port
init|=
name|service
operator|.
name|getDoubleItSaml1SelfSignedTransportPort
argument_list|()
decl_stmt|;
name|updateAddressPort
argument_list|(
name|saml1Port
argument_list|,
name|PORT2
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|saml1Port
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.saml-callback-handler"
argument_list|,
operator|new
name|SamlCallbackHandler
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|BigInteger
name|result
init|=
name|saml1Port
operator|.
name|doubleIt
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|25
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|equals
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|50
argument_list|)
argument_list|)
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
name|testAsymmetricSamlInitiator
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|unrestrictedPoliciesInstalled
condition|)
block|{
return|return;
block|}
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
name|SamlTokenTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client/client.xml"
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
name|DoubleItService
name|service
init|=
operator|new
name|DoubleItService
argument_list|()
decl_stmt|;
name|DoubleItPortType
name|saml2Port
init|=
name|service
operator|.
name|getDoubleItAsymmetricSamlInitiatorPort
argument_list|()
decl_stmt|;
name|updateAddressPort
argument_list|(
name|saml2Port
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|SamlCallbackHandler
name|callbackHandler
init|=
operator|new
name|SamlCallbackHandler
argument_list|()
decl_stmt|;
name|callbackHandler
operator|.
name|setConfirmationMethod
argument_list|(
name|SAML2Constants
operator|.
name|CONF_HOLDER_KEY
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|saml2Port
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.saml-callback-handler"
argument_list|,
name|callbackHandler
argument_list|)
expr_stmt|;
name|BigInteger
name|result
init|=
name|saml2Port
operator|.
name|doubleIt
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|25
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|equals
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|50
argument_list|)
argument_list|)
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
name|testSaml2OverSymmetricSignedElements
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|unrestrictedPoliciesInstalled
condition|)
block|{
return|return;
block|}
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
name|SamlTokenTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client/client.xml"
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
name|DoubleItService
name|service
init|=
operator|new
name|DoubleItService
argument_list|()
decl_stmt|;
name|DoubleItPortType
name|saml2Port
init|=
name|service
operator|.
name|getDoubleItSaml2SymmetricSignedElementsPort
argument_list|()
decl_stmt|;
name|updateAddressPort
argument_list|(
name|saml2Port
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|saml2Port
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.saml-callback-handler"
argument_list|,
operator|new
name|SamlCallbackHandler
argument_list|()
argument_list|)
expr_stmt|;
name|BigInteger
name|result
init|=
name|saml2Port
operator|.
name|doubleIt
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|25
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|equals
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|50
argument_list|)
argument_list|)
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
name|testSaml2EndorsingOverTransport
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
name|SamlTokenTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client/client.xml"
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
name|DoubleItService
name|service
init|=
operator|new
name|DoubleItService
argument_list|()
decl_stmt|;
name|DoubleItPortType
name|saml2Port
init|=
name|service
operator|.
name|getDoubleItSaml2EndorsingTransportPort
argument_list|()
decl_stmt|;
name|updateAddressPort
argument_list|(
name|saml2Port
argument_list|,
name|PORT2
argument_list|)
expr_stmt|;
name|SamlCallbackHandler
name|callbackHandler
init|=
operator|new
name|SamlCallbackHandler
argument_list|()
decl_stmt|;
name|callbackHandler
operator|.
name|setConfirmationMethod
argument_list|(
name|SAML2Constants
operator|.
name|CONF_HOLDER_KEY
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|saml2Port
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.saml-callback-handler"
argument_list|,
name|callbackHandler
argument_list|)
expr_stmt|;
name|BigInteger
name|result
init|=
name|saml2Port
operator|.
name|doubleIt
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|25
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|equals
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|50
argument_list|)
argument_list|)
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
name|testSaml2OverAsymmetricSignedEncrypted
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|unrestrictedPoliciesInstalled
condition|)
block|{
return|return;
block|}
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
name|SamlTokenTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client/client.xml"
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
name|DoubleItService
name|service
init|=
operator|new
name|DoubleItService
argument_list|()
decl_stmt|;
name|DoubleItPortType
name|saml2Port
init|=
name|service
operator|.
name|getDoubleItSaml2AsymmetricSignedEncryptedPort
argument_list|()
decl_stmt|;
name|updateAddressPort
argument_list|(
name|saml2Port
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|saml2Port
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.saml-callback-handler"
argument_list|,
operator|new
name|SamlCallbackHandler
argument_list|()
argument_list|)
expr_stmt|;
name|BigInteger
name|result
init|=
name|saml2Port
operator|.
name|doubleIt
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|25
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|equals
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|50
argument_list|)
argument_list|)
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
name|testSaml2OverAsymmetricEncrypted
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|unrestrictedPoliciesInstalled
condition|)
block|{
return|return;
block|}
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
name|SamlTokenTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client/client.xml"
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
name|DoubleItService
name|service
init|=
operator|new
name|DoubleItService
argument_list|()
decl_stmt|;
name|DoubleItPortType
name|saml2Port
init|=
name|service
operator|.
name|getDoubleItSaml2AsymmetricEncryptedPort
argument_list|()
decl_stmt|;
name|updateAddressPort
argument_list|(
name|saml2Port
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|SamlCallbackHandler
name|callbackHandler
init|=
operator|new
name|SamlCallbackHandler
argument_list|()
decl_stmt|;
name|callbackHandler
operator|.
name|setConfirmationMethod
argument_list|(
name|SAML2Constants
operator|.
name|CONF_BEARER
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|saml2Port
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.saml-callback-handler"
argument_list|,
name|callbackHandler
argument_list|)
expr_stmt|;
name|BigInteger
name|result
init|=
name|saml2Port
operator|.
name|doubleIt
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|25
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|equals
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|50
argument_list|)
argument_list|)
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
name|testSaml2EndorsingEncryptedOverTransport
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
name|SamlTokenTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client/client.xml"
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
name|DoubleItService
name|service
init|=
operator|new
name|DoubleItService
argument_list|()
decl_stmt|;
name|DoubleItPortType
name|saml2Port
init|=
name|service
operator|.
name|getDoubleItSaml2EndorsingEncryptedTransportPort
argument_list|()
decl_stmt|;
name|updateAddressPort
argument_list|(
name|saml2Port
argument_list|,
name|PORT2
argument_list|)
expr_stmt|;
name|SamlCallbackHandler
name|callbackHandler
init|=
operator|new
name|SamlCallbackHandler
argument_list|()
decl_stmt|;
name|callbackHandler
operator|.
name|setConfirmationMethod
argument_list|(
name|SAML2Constants
operator|.
name|CONF_HOLDER_KEY
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|saml2Port
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.saml-callback-handler"
argument_list|,
name|callbackHandler
argument_list|)
expr_stmt|;
name|BigInteger
name|result
init|=
name|saml2Port
operator|.
name|doubleIt
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|25
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|equals
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|50
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|checkUnrestrictedPoliciesInstalled
parameter_list|()
block|{
try|try
block|{
name|byte
index|[]
name|data
init|=
block|{
literal|0x00
block|,
literal|0x01
block|,
literal|0x02
block|,
literal|0x03
block|,
literal|0x04
block|,
literal|0x05
block|,
literal|0x06
block|,
literal|0x07
block|}
decl_stmt|;
name|SecretKey
name|key192
init|=
operator|new
name|SecretKeySpec
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|0x00
block|,
literal|0x01
block|,
literal|0x02
block|,
literal|0x03
block|,
literal|0x04
block|,
literal|0x05
block|,
literal|0x06
block|,
literal|0x07
block|,
literal|0x08
block|,
literal|0x09
block|,
literal|0x0a
block|,
literal|0x0b
block|,
literal|0x0c
block|,
literal|0x0d
block|,
literal|0x0e
block|,
literal|0x0f
block|,
literal|0x10
block|,
literal|0x11
block|,
literal|0x12
block|,
literal|0x13
block|,
literal|0x14
block|,
literal|0x15
block|,
literal|0x16
block|,
literal|0x17
block|}
argument_list|,
literal|"AES"
argument_list|)
decl_stmt|;
name|Cipher
name|c
init|=
name|Cipher
operator|.
name|getInstance
argument_list|(
literal|"AES"
argument_list|)
decl_stmt|;
name|c
operator|.
name|init
argument_list|(
name|Cipher
operator|.
name|ENCRYPT_MODE
argument_list|,
name|key192
argument_list|)
expr_stmt|;
name|c
operator|.
name|doFinal
argument_list|(
name|data
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

