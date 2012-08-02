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
name|coverage_checker
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
name|ws
operator|.
name|coverage_checker
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
comment|/**  * A set of tests for the SignatureCoverageChecker.  */
end_comment

begin_class
specifier|public
class|class
name|SignatureCoverageCheckerTest
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
name|Server
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
name|testSignedBodyTimestamp
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
name|SignatureCoverageCheckerTest
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
name|URL
name|wsdl
init|=
name|SignatureCoverageCheckerTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleItCoverageChecker.wsdl"
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
literal|"DoubleItBodyTimestampPort"
argument_list|)
decl_stmt|;
name|DoubleItPortType
name|port
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
name|port
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|outProps
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
name|outProps
operator|.
name|put
argument_list|(
literal|"action"
argument_list|,
literal|"Timestamp Signature"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"signaturePropFile"
argument_list|,
literal|"org/apache/cxf/systest/ws/wssec10/client/alice.properties"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"user"
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"passwordCallbackClass"
argument_list|,
literal|"org.apache.cxf.systest.ws.wssec10.client.KeystorePasswordCallback"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"signatureParts"
argument_list|,
literal|"{}{http://schemas.xmlsoap.org/soap/envelope/}Body;"
operator|+
literal|"{}{http://docs.oasis-open.org/wss/2004/01/oasis-"
operator|+
literal|"200401-wss-wssecurity-utility-1.0.xsd}Timestamp;"
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|WSS4JOutInterceptor
argument_list|(
name|outProps
argument_list|)
argument_list|)
expr_stmt|;
name|port
operator|.
name|doubleIt
argument_list|(
literal|25
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
name|testSignedBodyOnly
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
name|SignatureCoverageCheckerTest
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
name|URL
name|wsdl
init|=
name|SignatureCoverageCheckerTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleItCoverageChecker.wsdl"
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
literal|"DoubleItBodyTimestampPort"
argument_list|)
decl_stmt|;
name|DoubleItPortType
name|port
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
name|port
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|outProps
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
name|outProps
operator|.
name|put
argument_list|(
literal|"action"
argument_list|,
literal|"Timestamp Signature"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"signaturePropFile"
argument_list|,
literal|"org/apache/cxf/systest/ws/wssec10/client/alice.properties"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"user"
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"passwordCallbackClass"
argument_list|,
literal|"org.apache.cxf.systest.ws.wssec10.client.KeystorePasswordCallback"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"signatureParts"
argument_list|,
literal|"{}{http://schemas.xmlsoap.org/soap/envelope/}Body;"
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|WSS4JOutInterceptor
argument_list|(
name|outProps
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|port
operator|.
name|doubleIt
argument_list|(
literal|25
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on not signing the Timestamp"
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
name|testSignedTimestampOnly
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
name|SignatureCoverageCheckerTest
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
name|URL
name|wsdl
init|=
name|SignatureCoverageCheckerTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleItCoverageChecker.wsdl"
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
literal|"DoubleItBodyTimestampPort"
argument_list|)
decl_stmt|;
name|DoubleItPortType
name|port
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
name|port
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|outProps
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
name|outProps
operator|.
name|put
argument_list|(
literal|"action"
argument_list|,
literal|"Timestamp Signature"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"signaturePropFile"
argument_list|,
literal|"org/apache/cxf/systest/ws/wssec10/client/alice.properties"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"user"
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"passwordCallbackClass"
argument_list|,
literal|"org.apache.cxf.systest.ws.wssec10.client.KeystorePasswordCallback"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"signatureParts"
argument_list|,
literal|"{}{http://docs.oasis-open.org/wss/2004/01/oasis-"
operator|+
literal|"200401-wss-wssecurity-utility-1.0.xsd}Timestamp;"
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|WSS4JOutInterceptor
argument_list|(
name|outProps
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|port
operator|.
name|doubleIt
argument_list|(
literal|25
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on not signing the Timestamp"
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
name|testSignedBodyTimestampSoap12
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
name|SignatureCoverageCheckerTest
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
name|URL
name|wsdl
init|=
name|SignatureCoverageCheckerTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleItCoverageChecker.wsdl"
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
literal|"DoubleItBodyTimestampSoap12Port"
argument_list|)
decl_stmt|;
name|DoubleItPortType
name|port
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
name|port
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|outProps
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
name|outProps
operator|.
name|put
argument_list|(
literal|"action"
argument_list|,
literal|"Timestamp Signature"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"signaturePropFile"
argument_list|,
literal|"org/apache/cxf/systest/ws/wssec10/client/alice.properties"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"user"
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"passwordCallbackClass"
argument_list|,
literal|"org.apache.cxf.systest.ws.wssec10.client.KeystorePasswordCallback"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"signatureParts"
argument_list|,
literal|"{}{http://www.w3.org/2003/05/soap-envelope}Body;"
operator|+
literal|"{}{http://docs.oasis-open.org/wss/2004/01/oasis-"
operator|+
literal|"200401-wss-wssecurity-utility-1.0.xsd}Timestamp;"
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|WSS4JOutInterceptor
argument_list|(
name|outProps
argument_list|)
argument_list|)
expr_stmt|;
name|port
operator|.
name|doubleIt
argument_list|(
literal|25
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
name|testSignedBodyOnlySoap12
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
name|SignatureCoverageCheckerTest
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
name|URL
name|wsdl
init|=
name|SignatureCoverageCheckerTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleItCoverageChecker.wsdl"
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
literal|"DoubleItBodyTimestampSoap12Port"
argument_list|)
decl_stmt|;
name|DoubleItPortType
name|port
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
name|port
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|outProps
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
name|outProps
operator|.
name|put
argument_list|(
literal|"action"
argument_list|,
literal|"Timestamp Signature"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"signaturePropFile"
argument_list|,
literal|"org/apache/cxf/systest/ws/wssec10/client/alice.properties"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"user"
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"passwordCallbackClass"
argument_list|,
literal|"org.apache.cxf.systest.ws.wssec10.client.KeystorePasswordCallback"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"signatureParts"
argument_list|,
literal|"{}{http://www.w3.org/2003/05/soap-envelope}Body;"
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|WSS4JOutInterceptor
argument_list|(
name|outProps
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|port
operator|.
name|doubleIt
argument_list|(
literal|25
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on not signing the Timestamp"
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
name|testSignedTimestampOnlySoap12
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
name|SignatureCoverageCheckerTest
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
name|URL
name|wsdl
init|=
name|SignatureCoverageCheckerTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleItCoverageChecker.wsdl"
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
literal|"DoubleItBodyTimestampSoap12Port"
argument_list|)
decl_stmt|;
name|DoubleItPortType
name|port
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
name|port
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|outProps
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
name|outProps
operator|.
name|put
argument_list|(
literal|"action"
argument_list|,
literal|"Timestamp Signature"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"signaturePropFile"
argument_list|,
literal|"org/apache/cxf/systest/ws/wssec10/client/alice.properties"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"user"
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"passwordCallbackClass"
argument_list|,
literal|"org.apache.cxf.systest.ws.wssec10.client.KeystorePasswordCallback"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"signatureParts"
argument_list|,
literal|"{}{http://docs.oasis-open.org/wss/2004/01/oasis-"
operator|+
literal|"200401-wss-wssecurity-utility-1.0.xsd}Timestamp;"
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|WSS4JOutInterceptor
argument_list|(
name|outProps
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|port
operator|.
name|doubleIt
argument_list|(
literal|25
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on not signing the Timestamp"
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

