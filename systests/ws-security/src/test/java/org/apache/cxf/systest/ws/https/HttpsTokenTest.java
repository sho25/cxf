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
name|https
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
name|https
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
comment|/**  * A set of tests for the HttpsToken policy. It tests both DOM + StAX clients against the   * DOM server.  */
end_comment

begin_class
specifier|public
class|class
name|HttpsTokenTest
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
name|testRequireClientCert
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
name|HttpsTokenTest
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
name|HttpsTokenTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleItHttps.wsdl"
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
literal|"DoubleItRequireClientCertPort"
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
comment|// DOM
name|port
operator|.
name|doubleIt
argument_list|(
literal|25
argument_list|)
expr_stmt|;
comment|// Streaming
name|SecurityTestUtil
operator|.
name|enableStreaming
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|port
operator|.
name|doubleIt
argument_list|(
literal|25
argument_list|)
expr_stmt|;
comment|// This should fail, as the client does not use a client cert
name|portQName
operator|=
operator|new
name|QName
argument_list|(
name|NAMESPACE
argument_list|,
literal|"DoubleItRequireClientCertPort2"
argument_list|)
expr_stmt|;
name|port
operator|=
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
expr_stmt|;
name|updateAddressPort
argument_list|(
name|port
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
try|try
block|{
comment|// DOM
name|port
operator|.
name|doubleIt
argument_list|(
literal|25
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on not using a client cert"
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
name|String
name|error
init|=
literal|"HttpsToken"
decl_stmt|;
name|assertTrue
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
name|error
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
block|{
comment|// Streaming
name|SecurityTestUtil
operator|.
name|enableStreaming
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|port
operator|.
name|doubleIt
argument_list|(
literal|25
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on not using a client cert"
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
name|port
operator|)
operator|.
name|close
argument_list|()
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
name|testBasicAuth
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
name|HttpsTokenTest
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
name|HttpsTokenTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleItHttps.wsdl"
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
literal|"DoubleItBasicAuthPort"
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
comment|// DOM
name|port
operator|.
name|doubleIt
argument_list|(
literal|25
argument_list|)
expr_stmt|;
comment|// Streaming
name|SecurityTestUtil
operator|.
name|enableStreaming
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|port
operator|.
name|doubleIt
argument_list|(
literal|25
argument_list|)
expr_stmt|;
comment|// This should fail, as the client does not send a UsernamePassword
name|portQName
operator|=
operator|new
name|QName
argument_list|(
name|NAMESPACE
argument_list|,
literal|"DoubleItBasicAuthPort2"
argument_list|)
expr_stmt|;
name|port
operator|=
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
expr_stmt|;
name|updateAddressPort
argument_list|(
name|port
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
try|try
block|{
comment|// DOM
name|port
operator|.
name|doubleIt
argument_list|(
literal|25
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on not sending a UsernamePassword"
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
name|String
name|error
init|=
literal|"HttpsToken"
decl_stmt|;
name|assertTrue
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
name|error
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
block|{
comment|// Streaming
name|SecurityTestUtil
operator|.
name|enableStreaming
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|port
operator|.
name|doubleIt
argument_list|(
literal|25
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on not sending a UsernamePassword"
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
comment|// expected
block|}
block|}
block|}
end_class

end_unit

