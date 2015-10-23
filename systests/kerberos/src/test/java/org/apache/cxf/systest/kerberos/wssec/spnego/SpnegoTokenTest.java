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
name|kerberos
operator|.
name|wssec
operator|.
name|spnego
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|nio
operator|.
name|file
operator|.
name|FileSystems
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
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
name|kerberos
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
name|testutil
operator|.
name|common
operator|.
name|TestUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|server
operator|.
name|annotations
operator|.
name|CreateKdcServer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|server
operator|.
name|annotations
operator|.
name|CreateTransport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|server
operator|.
name|core
operator|.
name|annotations
operator|.
name|ApplyLdifFiles
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|server
operator|.
name|core
operator|.
name|annotations
operator|.
name|CreateDS
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|server
operator|.
name|core
operator|.
name|annotations
operator|.
name|CreateIndex
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|server
operator|.
name|core
operator|.
name|annotations
operator|.
name|CreatePartition
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|server
operator|.
name|core
operator|.
name|integ
operator|.
name|AbstractLdapTestUnit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|server
operator|.
name|core
operator|.
name|integ
operator|.
name|FrameworkRunner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|server
operator|.
name|core
operator|.
name|kerberos
operator|.
name|KeyDerivationInterceptor
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
name|WSSConfig
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
name|Assert
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
name|runner
operator|.
name|RunWith
import|;
end_import

begin_comment
comment|/**  * A set of tests for Spnego Tokens that use an Apache DS instance as the KDC.  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|FrameworkRunner
operator|.
name|class
argument_list|)
comment|//Define the DirectoryService
annotation|@
name|CreateDS
argument_list|(
name|name
operator|=
literal|"AbstractKerberosTest-class"
argument_list|,
name|enableAccessControl
operator|=
literal|false
argument_list|,
name|allowAnonAccess
operator|=
literal|false
argument_list|,
name|enableChangeLog
operator|=
literal|true
argument_list|,
name|partitions
operator|=
block|{
annotation|@
name|CreatePartition
argument_list|(
name|name
operator|=
literal|"example"
argument_list|,
name|suffix
operator|=
literal|"dc=example,dc=com"
argument_list|,
name|indexes
operator|=
block|{
annotation|@
name|CreateIndex
argument_list|(
name|attribute
operator|=
literal|"objectClass"
argument_list|)
block|,
annotation|@
name|CreateIndex
argument_list|(
name|attribute
operator|=
literal|"dc"
argument_list|)
block|,
annotation|@
name|CreateIndex
argument_list|(
name|attribute
operator|=
literal|"ou"
argument_list|)
block|}
argument_list|)
block|}
argument_list|,
name|additionalInterceptors
operator|=
block|{
name|KeyDerivationInterceptor
operator|.
name|class
block|}
argument_list|)
annotation|@
name|CreateKdcServer
argument_list|(
name|transports
operator|=
block|{
annotation|@
name|CreateTransport
argument_list|(
name|protocol
operator|=
literal|"KRB"
argument_list|,
name|address
operator|=
literal|"127.0.0.1"
argument_list|)
block|}
argument_list|,
name|primaryRealm
operator|=
literal|"service.ws.apache.org"
argument_list|,
name|kdcPrincipal
operator|=
literal|"krbtgt/service.ws.apache.org@service.ws.apache.org"
argument_list|)
comment|//Inject an file containing entries
annotation|@
name|ApplyLdifFiles
argument_list|(
literal|"kerberos.ldif"
argument_list|)
specifier|public
class|class
name|SpnegoTokenTest
extends|extends
name|AbstractLdapTestUnit
block|{
specifier|static
specifier|final
name|String
name|PORT
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
name|Server
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|String
name|STAX_PORT
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
name|StaxServer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|String
name|PORT2
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
name|Server
operator|.
name|class
argument_list|,
literal|2
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|String
name|STAX_PORT2
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
name|StaxServer
operator|.
name|class
argument_list|,
literal|2
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
specifier|static
name|boolean
name|unrestrictedPoliciesInstalled
init|=
name|SecurityTestUtil
operator|.
name|checkUnrestrictedPoliciesInstalled
argument_list|()
decl_stmt|;
specifier|private
specifier|static
name|boolean
name|runTests
decl_stmt|;
specifier|private
specifier|static
name|boolean
name|portUpdated
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|updatePort
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|portUpdated
condition|)
block|{
name|String
name|basedir
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"basedir"
argument_list|)
decl_stmt|;
if|if
condition|(
name|basedir
operator|==
literal|null
condition|)
block|{
name|basedir
operator|=
operator|new
name|File
argument_list|(
literal|"."
argument_list|)
operator|.
name|getCanonicalPath
argument_list|()
expr_stmt|;
block|}
comment|// Read in krb5.conf and substitute in the correct port
name|Path
name|path
init|=
name|FileSystems
operator|.
name|getDefault
argument_list|()
operator|.
name|getPath
argument_list|(
name|basedir
argument_list|,
literal|"/src/test/resources/krb5.conf"
argument_list|)
decl_stmt|;
name|String
name|content
init|=
operator|new
name|String
argument_list|(
name|Files
operator|.
name|readAllBytes
argument_list|(
name|path
argument_list|)
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|content
operator|=
name|content
operator|.
name|replaceAll
argument_list|(
literal|"port"
argument_list|,
literal|""
operator|+
name|super
operator|.
name|getKdcServer
argument_list|()
operator|.
name|getTransports
argument_list|()
index|[
literal|0
index|]
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|Path
name|path2
init|=
name|FileSystems
operator|.
name|getDefault
argument_list|()
operator|.
name|getPath
argument_list|(
name|basedir
argument_list|,
literal|"/target/test-classes/wssec.spnego.krb5.conf"
argument_list|)
decl_stmt|;
name|Files
operator|.
name|write
argument_list|(
name|path2
argument_list|,
name|content
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"java.security.krb5.conf"
argument_list|,
name|path2
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|portUpdated
operator|=
literal|true
expr_stmt|;
block|}
block|}
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
name|WSSConfig
operator|.
name|init
argument_list|()
expr_stmt|;
comment|//
comment|// This test fails with the IBM JDK
comment|//
if|if
condition|(
operator|!
literal|"IBM Corporation"
operator|.
name|equals
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.vendor"
argument_list|)
argument_list|)
condition|)
block|{
name|runTests
operator|=
literal|true
expr_stmt|;
name|String
name|basedir
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"basedir"
argument_list|)
decl_stmt|;
if|if
condition|(
name|basedir
operator|==
literal|null
condition|)
block|{
name|basedir
operator|=
operator|new
name|File
argument_list|(
literal|"."
argument_list|)
operator|.
name|getCanonicalPath
argument_list|()
expr_stmt|;
block|}
comment|// System.setProperty("sun.security.krb5.debug", "true");
name|System
operator|.
name|setProperty
argument_list|(
literal|"java.security.auth.login.config"
argument_list|,
name|basedir
operator|+
literal|"/src/test/resources/kerberos.jaas"
argument_list|)
expr_stmt|;
block|}
comment|// Launch servers
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"Server failed to launch"
argument_list|,
comment|// run the server in the same process
comment|// set this to false to fork
name|AbstractBusClientServerTestBase
operator|.
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
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"Server failed to launch"
argument_list|,
comment|// run the server in the same process
comment|// set this to false to fork
name|AbstractBusClientServerTestBase
operator|.
name|launchServer
argument_list|(
name|StaxServer
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
name|AbstractBusClientServerTestBase
operator|.
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
name|testSpnegoOverSymmetric
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|runTests
operator|||
operator|!
name|unrestrictedPoliciesInstalled
condition|)
block|{
return|return;
block|}
name|String
name|portName
init|=
literal|"DoubleItSpnegoSymmetricPort"
decl_stmt|;
name|runKerberosTest
argument_list|(
name|portName
argument_list|,
literal|false
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|runKerberosTest
argument_list|(
name|portName
argument_list|,
literal|false
argument_list|,
name|STAX_PORT
argument_list|)
expr_stmt|;
name|runKerberosTest
argument_list|(
name|portName
argument_list|,
literal|true
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|runKerberosTest
argument_list|(
name|portName
argument_list|,
literal|true
argument_list|,
name|STAX_PORT
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
name|testSpnegoOverSymmetricDerived
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|runTests
operator|||
operator|!
name|unrestrictedPoliciesInstalled
condition|)
block|{
return|return;
block|}
name|String
name|portName
init|=
literal|"DoubleItSpnegoSymmetricDerivedPort"
decl_stmt|;
name|runKerberosTest
argument_list|(
name|portName
argument_list|,
literal|false
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|runKerberosTest
argument_list|(
name|portName
argument_list|,
literal|false
argument_list|,
name|STAX_PORT
argument_list|)
expr_stmt|;
name|runKerberosTest
argument_list|(
name|portName
argument_list|,
literal|true
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|runKerberosTest
argument_list|(
name|portName
argument_list|,
literal|true
argument_list|,
name|STAX_PORT
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
name|testSpnegoOverSymmetricEncryptBeforeSigning
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|runTests
operator|||
operator|!
name|unrestrictedPoliciesInstalled
condition|)
block|{
return|return;
block|}
name|String
name|portName
init|=
literal|"DoubleItSpnegoSymmetricEncryptBeforeSigningPort"
decl_stmt|;
name|runKerberosTest
argument_list|(
name|portName
argument_list|,
literal|false
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|runKerberosTest
argument_list|(
name|portName
argument_list|,
literal|false
argument_list|,
name|STAX_PORT
argument_list|)
expr_stmt|;
name|runKerberosTest
argument_list|(
name|portName
argument_list|,
literal|true
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|runKerberosTest
argument_list|(
name|portName
argument_list|,
literal|true
argument_list|,
name|STAX_PORT
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
name|testSpnegoOverTransport
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|runTests
operator|||
operator|!
name|unrestrictedPoliciesInstalled
condition|)
block|{
return|return;
block|}
name|String
name|portName
init|=
literal|"DoubleItSpnegoTransportPort"
decl_stmt|;
name|runKerberosTest
argument_list|(
name|portName
argument_list|,
literal|false
argument_list|,
name|PORT2
argument_list|)
expr_stmt|;
name|runKerberosTest
argument_list|(
name|portName
argument_list|,
literal|false
argument_list|,
name|STAX_PORT2
argument_list|)
expr_stmt|;
comment|// // TODO Supporting streaming Snego outbound
comment|// runKerberosTest(portName, true, PORT2);
comment|// runKerberosTest(portName, true, STAX_PORT2);
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testSpnegoOverTransportEndorsing
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|runTests
operator|||
operator|!
name|unrestrictedPoliciesInstalled
condition|)
block|{
return|return;
block|}
name|String
name|portName
init|=
literal|"DoubleItSpnegoTransportEndorsingPort"
decl_stmt|;
name|runKerberosTest
argument_list|(
name|portName
argument_list|,
literal|false
argument_list|,
name|PORT2
argument_list|)
expr_stmt|;
name|runKerberosTest
argument_list|(
name|portName
argument_list|,
literal|false
argument_list|,
name|STAX_PORT2
argument_list|)
expr_stmt|;
comment|// TODO Supporting streaming Spnego outbound
comment|// runKerberosTest(portName, true, PORT2);
comment|// runKerberosTest(portName, true, STAX_PORT2);
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testSpnegoOverTransportEndorsingSP11
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|runTests
operator|||
operator|!
name|unrestrictedPoliciesInstalled
condition|)
block|{
return|return;
block|}
name|String
name|portName
init|=
literal|"DoubleItSpnegoTransportEndorsingSP11Port"
decl_stmt|;
name|runKerberosTest
argument_list|(
name|portName
argument_list|,
literal|false
argument_list|,
name|PORT2
argument_list|)
expr_stmt|;
name|runKerberosTest
argument_list|(
name|portName
argument_list|,
literal|false
argument_list|,
name|STAX_PORT2
argument_list|)
expr_stmt|;
comment|// TODO Supporting streaming Spnego outbound
comment|// runKerberosTest(portName, true, PORT2);
comment|// runKerberosTest(portName, true, STAX_PORT2);
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
annotation|@
name|org
operator|.
name|junit
operator|.
name|Ignore
specifier|public
name|void
name|testSpnegoOverSymmetricSecureConversation
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|runTests
operator|||
operator|!
name|unrestrictedPoliciesInstalled
condition|)
block|{
return|return;
block|}
name|String
name|portName
init|=
literal|"DoubleItSpnegoSymmetricSecureConversationPort"
decl_stmt|;
name|runKerberosTest
argument_list|(
name|portName
argument_list|,
literal|false
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
comment|//runKerberosTest(portName, false, STAX_PORT);
comment|//runKerberosTest(portName, true, PORT);
comment|//runKerberosTest(portName, true, STAX_PORT);
block|}
specifier|private
name|void
name|runKerberosTest
parameter_list|(
name|String
name|portName
parameter_list|,
name|boolean
name|streaming
parameter_list|,
name|String
name|portNumber
parameter_list|)
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
name|SpnegoTokenTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
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
name|SpnegoTokenTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleItSpnego.wsdl"
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
name|portName
argument_list|)
decl_stmt|;
name|DoubleItPortType
name|kerberosPort
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
name|TestUtil
operator|.
name|updateAddressPort
argument_list|(
name|kerberosPort
argument_list|,
name|portNumber
argument_list|)
expr_stmt|;
if|if
condition|(
name|streaming
condition|)
block|{
name|SecurityTestUtil
operator|.
name|enableStreaming
argument_list|(
name|kerberosPort
argument_list|)
expr_stmt|;
block|}
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|50
argument_list|,
name|kerberosPort
operator|.
name|doubleIt
argument_list|(
literal|25
argument_list|)
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
name|kerberosPort
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
block|}
end_class

end_unit

