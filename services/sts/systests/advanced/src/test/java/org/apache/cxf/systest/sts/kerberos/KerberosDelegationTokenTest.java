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
name|kerberos
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
name|BindingProvider
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
name|TokenTestUtils
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
name|transport
operator|.
name|http
operator|.
name|auth
operator|.
name|SpnegoAuthSupplier
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
name|ietf
operator|.
name|jgss
operator|.
name|GSSName
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
comment|/**  * This tests credential delegation. The client enables credential delegation + sends a Kerberos  * token to an Intermediary via WS-Security. The Intermediary validates the token, and then  * uses the delgated credential to obtain a ticket to in turn retrieve a SAML token from the  * STS. The SAML token is used to secure access to the backend service.  *  * The tests are @Ignored by default, as a KDC is needed. To replicate the test scenario, set up a KDC with  * user principal "alice" (keytab in "/etc/alice.keytab"), and host service "bob@service.ws.apache.org"  * (keytab in "/etc/bob.keytab").  */
end_comment

begin_class
annotation|@
name|org
operator|.
name|junit
operator|.
name|Ignore
specifier|public
class|class
name|KerberosDelegationTokenTest
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
name|INTERMEDIARY_PORT
init|=
name|allocatePort
argument_list|(
name|Intermediary
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
name|Intermediary
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
name|testKerberosToken
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
name|KerberosDelegationTokenTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"cxf-intermediary-client.xml"
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
name|URL
name|wsdl
init|=
name|KerberosDelegationTokenTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleItIntermediary.wsdl"
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
literal|"DoubleItTransportKerberosPort"
argument_list|)
decl_stmt|;
name|DoubleItPortType
name|transportSaml2Port
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
name|transportSaml2Port
argument_list|,
name|INTERMEDIARY_PORT
argument_list|)
expr_stmt|;
name|TokenTestUtils
operator|.
name|updateSTSPort
argument_list|(
operator|(
name|BindingProvider
operator|)
name|transportSaml2Port
argument_list|,
name|STSPORT
argument_list|)
expr_stmt|;
name|doubleIt
argument_list|(
name|transportSaml2Port
argument_list|,
literal|25
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
name|transportSaml2Port
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
name|testKerberosTokenJAXRS
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|configLocation
init|=
literal|"org/apache/cxf/systest/sts/kerberos/cxf-intermediary-jaxrs-client.xml"
decl_stmt|;
specifier|final
name|String
name|address
init|=
literal|"https://localhost:"
operator|+
name|INTERMEDIARY_PORT
operator|+
literal|"/doubleit/services/doubleit-rs"
decl_stmt|;
specifier|final
name|int
name|numToDouble
init|=
literal|35
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
name|configLocation
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|requestContext
init|=
name|WebClient
operator|.
name|getConfig
argument_list|(
name|client
argument_list|)
operator|.
name|getRequestContext
argument_list|()
decl_stmt|;
name|requestContext
operator|.
name|put
argument_list|(
literal|"auth.spnego.useKerberosOid"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|requestContext
operator|.
name|put
argument_list|(
literal|"auth.spnego.requireCredDelegation"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|SpnegoAuthSupplier
name|authSupplier
init|=
operator|new
name|SpnegoAuthSupplier
argument_list|()
decl_stmt|;
name|authSupplier
operator|.
name|setServicePrincipalName
argument_list|(
literal|"bob@service.ws.apache.org"
argument_list|)
expr_stmt|;
name|authSupplier
operator|.
name|setServiceNameType
argument_list|(
name|GSSName
operator|.
name|NT_HOSTBASED_SERVICE
argument_list|)
expr_stmt|;
name|WebClient
operator|.
name|getConfig
argument_list|(
name|client
argument_list|)
operator|.
name|getHttpConduit
argument_list|()
operator|.
name|setAuthSupplier
argument_list|(
name|authSupplier
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
block|}
specifier|private
specifier|static
name|void
name|doubleIt
parameter_list|(
name|DoubleItPortType
name|port
parameter_list|,
name|int
name|numToDouble
parameter_list|)
block|{
name|int
name|resp
init|=
name|port
operator|.
name|doubleIt
argument_list|(
name|numToDouble
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|numToDouble
operator|*
literal|2
argument_list|,
name|resp
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

