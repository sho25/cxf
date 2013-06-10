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
name|kerberos
operator|.
name|server
operator|.
name|StaxServer
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
comment|/**  * A set of tests for Kerberos Tokens. The tests are @Ignore'd, as they require a running KDC. To run the  * tests, set up a KDC of realm "WS.APACHE.ORG", with principal "alice" and service principal   * "bob/service.ws.apache.org". Create keytabs for both principals in "/etc/alice.keytab" and  * "/etc/bob.keytab" (this can all be edited in src/test/resource/kerberos.jaas". Then disable the  * @Ignore annotations and run the tests with:  *    * mvn test -Pnochecks -Dtest=StaxKerberosTokenTest   *     -Djava.security.auth.login.config=src/test/resources/kerberos.jaas  *   * See here for more information:  * http://coheigea.blogspot.com/2011/10/using-kerberos-with-web-services-part.html  *   * It tests both DOM + StAX clients against the StAX server  */
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
name|StaxKerberosTokenTest
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
name|allocatePort
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
name|stopAllServers
argument_list|()
expr_stmt|;
block|}
comment|// TODO See WSS-453
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
name|testKerberosOverTransport
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
name|StaxKerberosTokenTest
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
name|StaxKerberosTokenTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleItKerberos.wsdl"
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
literal|"DoubleItKerberosTransportPort"
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
name|updateAddressPort
argument_list|(
name|kerberosPort
argument_list|,
name|PORT2
argument_list|)
expr_stmt|;
comment|// DOM
name|kerberosPort
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
name|kerberosPort
argument_list|)
expr_stmt|;
name|kerberosPort
operator|.
name|doubleIt
argument_list|(
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
comment|/*     @org.junit.Test     public void testKerberosOverSymmetric() throws Exception {                  if (!unrestrictedPoliciesInstalled) {             return;         }          SpringBusFactory bf = new SpringBusFactory();         URL busFile = StaxKerberosTokenTest.class.getResource("client/client.xml");          Bus bus = bf.createBus(busFile.toString());         SpringBusFactory.setDefaultBus(bus);         SpringBusFactory.setThreadDefaultBus(bus);          URL wsdl = StaxKerberosTokenTest.class.getResource("DoubleItKerberos.wsdl");         Service service = Service.create(wsdl, SERVICE_QNAME);         QName portQName = new QName(NAMESPACE, "DoubleItKerberosSymmetricPort");         DoubleItPortType kerberosPort =                  service.getPort(portQName, DoubleItPortType.class);          updateAddressPort(kerberosPort, PORT);                  int result = kerberosPort.doubleIt(25);         assertTrue(result == 50);                  ((java.io.Closeable)kerberosPort).close();         bus.shutdown(true);     }          @org.junit.Test     public void testKerberosOverSymmetricSupporting() throws Exception {                  if (!unrestrictedPoliciesInstalled) {             return;         }          SpringBusFactory bf = new SpringBusFactory();         URL busFile = StaxKerberosTokenTest.class.getResource("client/client.xml");          Bus bus = bf.createBus(busFile.toString());         SpringBusFactory.setDefaultBus(bus);         SpringBusFactory.setThreadDefaultBus(bus);          URL wsdl = StaxKerberosTokenTest.class.getResource("DoubleItKerberos.wsdl");         Service service = Service.create(wsdl, SERVICE_QNAME);         QName portQName = new QName(NAMESPACE, "DoubleItKerberosSymmetricSupportingPort");         DoubleItPortType kerberosPort =                  service.getPort(portQName, DoubleItPortType.class);          updateAddressPort(kerberosPort, PORT);                  int result = kerberosPort.doubleIt(25);         assertTrue(result == 50);                  ((java.io.Closeable)kerberosPort).close();         bus.shutdown(true);     }     */
comment|// TODO - See WSS-454
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
name|testKerberosOverAsymmetric
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
name|StaxKerberosTokenTest
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
name|StaxKerberosTokenTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleItKerberos.wsdl"
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
literal|"DoubleItKerberosAsymmetricPort"
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
name|updateAddressPort
argument_list|(
name|kerberosPort
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
comment|// DOM
name|kerberosPort
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
name|kerberosPort
argument_list|)
expr_stmt|;
name|kerberosPort
operator|.
name|doubleIt
argument_list|(
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
comment|// TODO See WSS-453
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
name|testKerberosOverTransportEndorsing
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
name|StaxKerberosTokenTest
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
name|StaxKerberosTokenTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleItKerberos.wsdl"
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
literal|"DoubleItKerberosTransportEndorsingPort"
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
name|updateAddressPort
argument_list|(
name|kerberosPort
argument_list|,
name|PORT2
argument_list|)
expr_stmt|;
comment|// DOM
name|kerberosPort
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
name|kerberosPort
argument_list|)
expr_stmt|;
name|kerberosPort
operator|.
name|doubleIt
argument_list|(
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
comment|// TODO See WSS-453
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
name|testKerberosOverAsymmetricEndorsing
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
name|StaxKerberosTokenTest
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
name|StaxKerberosTokenTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleItKerberos.wsdl"
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
literal|"DoubleItKerberosAsymmetricEndorsingPort"
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
name|updateAddressPort
argument_list|(
name|kerberosPort
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
comment|// DOM
name|kerberosPort
operator|.
name|doubleIt
argument_list|(
literal|25
argument_list|)
expr_stmt|;
comment|// TODO Streaming
comment|// SecurityTestUtil.enableStreaming(kerberosPort);
comment|// kerberosPort.doubleIt(25);
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
comment|/*     @org.junit.Test     public void testKerberosOverSymmetricProtection() throws Exception {          SpringBusFactory bf = new SpringBusFactory();         URL busFile = StaxKerberosTokenTest.class.getResource("client/client.xml");          Bus bus = bf.createBus(busFile.toString());         SpringBusFactory.setDefaultBus(bus);         SpringBusFactory.setThreadDefaultBus(bus);          URL wsdl = StaxKerberosTokenTest.class.getResource("DoubleItKerberos.wsdl");         Service service = Service.create(wsdl, SERVICE_QNAME);         QName portQName = new QName(NAMESPACE, "DoubleItKerberosSymmetricProtectionPort");         DoubleItPortType kerberosPort =                  service.getPort(portQName, DoubleItPortType.class);                  updateAddressPort(kerberosPort, PORT);         int result = kerberosPort.doubleIt(25);         assertTrue(result == 50);                  ((java.io.Closeable)kerberosPort).close();         bus.shutdown(true);     }          @org.junit.Test     public void testKerberosOverSymmetricDerivedProtection() throws Exception {          SpringBusFactory bf = new SpringBusFactory();         URL busFile = StaxKerberosTokenTest.class.getResource("client/client.xml");          Bus bus = bf.createBus(busFile.toString());         SpringBusFactory.setDefaultBus(bus);         SpringBusFactory.setThreadDefaultBus(bus);          URL wsdl = StaxKerberosTokenTest.class.getResource("DoubleItKerberos.wsdl");         Service service = Service.create(wsdl, SERVICE_QNAME);         QName portQName = new QName(NAMESPACE, "DoubleItKerberosSymmetricDerivedProtectionPort");         DoubleItPortType kerberosPort =                  service.getPort(portQName, DoubleItPortType.class);                  updateAddressPort(kerberosPort, PORT);         int result = kerberosPort.doubleIt(25);         assertTrue(result == 50);                  ((java.io.Closeable)kerberosPort).close();         bus.shutdown(true);     }     */
comment|// TODO - See WSS-453 WSS-454
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
name|testKerberosOverAsymmetricSignedEndorsing
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
name|StaxKerberosTokenTest
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
name|StaxKerberosTokenTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleItKerberos.wsdl"
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
literal|"DoubleItKerberosAsymmetricSignedEndorsingPort"
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
name|updateAddressPort
argument_list|(
name|kerberosPort
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
comment|// DOM
name|kerberosPort
operator|.
name|doubleIt
argument_list|(
literal|25
argument_list|)
expr_stmt|;
comment|// TODO Streaming
comment|// SecurityTestUtil.enableStreaming(kerberosPort);
comment|// kerberosPort.doubleIt(25);
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
comment|// TODO - See WSS-453 WSS-454
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
name|testKerberosOverAsymmetricSignedEncrypted
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
name|StaxKerberosTokenTest
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
name|StaxKerberosTokenTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleItKerberos.wsdl"
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
literal|"DoubleItKerberosAsymmetricSignedEncryptedPort"
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
name|updateAddressPort
argument_list|(
name|kerberosPort
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
comment|// DOM
name|kerberosPort
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
name|kerberosPort
argument_list|)
expr_stmt|;
name|kerberosPort
operator|.
name|doubleIt
argument_list|(
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
comment|/*     @org.junit.Test     public void testKerberosOverSymmetricEndorsingEncrypted() throws Exception {                  if (!unrestrictedPoliciesInstalled) {             return;         }          SpringBusFactory bf = new SpringBusFactory();         URL busFile = StaxKerberosTokenTest.class.getResource("client/client.xml");          Bus bus = bf.createBus(busFile.toString());         SpringBusFactory.setDefaultBus(bus);         SpringBusFactory.setThreadDefaultBus(bus);          URL wsdl = StaxKerberosTokenTest.class.getResource("DoubleItKerberos.wsdl");         Service service = Service.create(wsdl, SERVICE_QNAME);         QName portQName = new QName(NAMESPACE, "DoubleItKerberosSymmetricEndorsingEncryptedPort");         DoubleItPortType kerberosPort =                  service.getPort(portQName, DoubleItPortType.class);                  updateAddressPort(kerberosPort, PORT);                  int result = kerberosPort.doubleIt(25);         assertTrue(result == 50);                  ((java.io.Closeable)kerberosPort).close();         bus.shutdown(true);     }          @org.junit.Test     public void testKerberosOverSymmetricSignedEndorsingEncrypted() throws Exception {                  if (!unrestrictedPoliciesInstalled) {             return;         }          SpringBusFactory bf = new SpringBusFactory();         URL busFile = StaxKerberosTokenTest.class.getResource("client/client.xml");          Bus bus = bf.createBus(busFile.toString());         SpringBusFactory.setDefaultBus(bus);         SpringBusFactory.setThreadDefaultBus(bus);          URL wsdl = StaxKerberosTokenTest.class.getResource("DoubleItKerberos.wsdl");         Service service = Service.create(wsdl, SERVICE_QNAME);         QName portQName = new QName(NAMESPACE, "DoubleItKerberosSymmetricSignedEndorsingEncryptedPort");         DoubleItPortType kerberosPort =                  service.getPort(portQName, DoubleItPortType.class);                  updateAddressPort(kerberosPort, PORT);                  int result = kerberosPort.doubleIt(25);         assertTrue(result == 50);                  ((java.io.Closeable)kerberosPort).close();         bus.shutdown(true);     }     */
block|}
end_class

end_unit

