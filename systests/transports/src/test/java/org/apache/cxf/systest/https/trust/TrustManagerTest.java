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
name|https
operator|.
name|trust
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
name|security
operator|.
name|cert
operator|.
name|CertificateException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|X509Certificate
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|TrustManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|X509TrustManager
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
name|configuration
operator|.
name|jsse
operator|.
name|TLSClientParameters
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
name|HTTPConduit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world
operator|.
name|Greeter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world
operator|.
name|services
operator|.
name|SOAPService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
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
comment|/**  * A set of tests for specifying a TrustManager  */
end_comment

begin_class
specifier|public
class|class
name|TrustManagerTest
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
name|TrustServer
operator|.
name|class
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
name|TrustServer
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|cleanup
parameter_list|()
throws|throws
name|Exception
block|{
name|stopAllServers
argument_list|()
expr_stmt|;
block|}
comment|// The X509TrustManager is effectively empty here so trust verification should work
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testNoOpX509TrustManager
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
name|TrustManagerTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client-trust.xml"
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
name|url
init|=
name|SOAPService
operator|.
name|WSDL_LOCATION
decl_stmt|;
name|SOAPService
name|service
init|=
operator|new
name|SOAPService
argument_list|(
name|url
argument_list|,
name|SOAPService
operator|.
name|SERVICE
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Service is null"
argument_list|,
name|service
argument_list|)
expr_stmt|;
specifier|final
name|Greeter
name|port
init|=
name|service
operator|.
name|getHttpsPort
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Port is null"
argument_list|,
name|port
argument_list|)
expr_stmt|;
name|updateAddressPort
argument_list|(
name|port
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|TLSClientParameters
name|tlsParams
init|=
operator|new
name|TLSClientParameters
argument_list|()
decl_stmt|;
name|X509TrustManager
name|trustManager
init|=
operator|new
name|NoOpX509TrustManager
argument_list|()
decl_stmt|;
name|TrustManager
index|[]
name|trustManagers
init|=
operator|new
name|TrustManager
index|[
literal|1
index|]
decl_stmt|;
name|trustManagers
index|[
literal|0
index|]
operator|=
name|trustManager
expr_stmt|;
name|tlsParams
operator|.
name|setTrustManagers
argument_list|(
name|trustManagers
argument_list|)
expr_stmt|;
name|tlsParams
operator|.
name|setDisableCNCheck
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Client
name|client
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|port
argument_list|)
decl_stmt|;
name|HTTPConduit
name|http
init|=
operator|(
name|HTTPConduit
operator|)
name|client
operator|.
name|getConduit
argument_list|()
decl_stmt|;
name|http
operator|.
name|setTlsClientParameters
argument_list|(
name|tlsParams
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|port
operator|.
name|greetMe
argument_list|(
literal|"Kitty"
argument_list|)
argument_list|,
literal|"Hello Kitty"
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
comment|// Here the Trust Manager checks the server cert
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testValidServerCertX509TrustManager
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
name|TrustManagerTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client-trust.xml"
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
name|url
init|=
name|SOAPService
operator|.
name|WSDL_LOCATION
decl_stmt|;
name|SOAPService
name|service
init|=
operator|new
name|SOAPService
argument_list|(
name|url
argument_list|,
name|SOAPService
operator|.
name|SERVICE
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Service is null"
argument_list|,
name|service
argument_list|)
expr_stmt|;
specifier|final
name|Greeter
name|port
init|=
name|service
operator|.
name|getHttpsPort
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Port is null"
argument_list|,
name|port
argument_list|)
expr_stmt|;
name|updateAddressPort
argument_list|(
name|port
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|String
name|validPrincipalName
init|=
literal|"CN=Bethal,OU=Bethal,O=ApacheTest,L=Syracuse,C=US"
decl_stmt|;
name|TLSClientParameters
name|tlsParams
init|=
operator|new
name|TLSClientParameters
argument_list|()
decl_stmt|;
name|X509TrustManager
name|trustManager
init|=
operator|new
name|ServerCertX509TrustManager
argument_list|(
name|validPrincipalName
argument_list|)
decl_stmt|;
name|TrustManager
index|[]
name|trustManagers
init|=
operator|new
name|TrustManager
index|[
literal|1
index|]
decl_stmt|;
name|trustManagers
index|[
literal|0
index|]
operator|=
name|trustManager
expr_stmt|;
name|tlsParams
operator|.
name|setTrustManagers
argument_list|(
name|trustManagers
argument_list|)
expr_stmt|;
name|tlsParams
operator|.
name|setDisableCNCheck
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Client
name|client
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|port
argument_list|)
decl_stmt|;
name|HTTPConduit
name|http
init|=
operator|(
name|HTTPConduit
operator|)
name|client
operator|.
name|getConduit
argument_list|()
decl_stmt|;
name|http
operator|.
name|setTlsClientParameters
argument_list|(
name|tlsParams
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|port
operator|.
name|greetMe
argument_list|(
literal|"Kitty"
argument_list|)
argument_list|,
literal|"Hello Kitty"
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
name|testInvalidServerCertX509TrustManager
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
name|TrustManagerTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client-trust.xml"
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
name|url
init|=
name|SOAPService
operator|.
name|WSDL_LOCATION
decl_stmt|;
name|SOAPService
name|service
init|=
operator|new
name|SOAPService
argument_list|(
name|url
argument_list|,
name|SOAPService
operator|.
name|SERVICE
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Service is null"
argument_list|,
name|service
argument_list|)
expr_stmt|;
specifier|final
name|Greeter
name|port
init|=
name|service
operator|.
name|getHttpsPort
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Port is null"
argument_list|,
name|port
argument_list|)
expr_stmt|;
name|updateAddressPort
argument_list|(
name|port
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|String
name|invalidPrincipalName
init|=
literal|"CN=Bethal2,OU=Bethal,O=ApacheTest,L=Syracuse,C=US"
decl_stmt|;
name|TLSClientParameters
name|tlsParams
init|=
operator|new
name|TLSClientParameters
argument_list|()
decl_stmt|;
name|X509TrustManager
name|trustManager
init|=
operator|new
name|ServerCertX509TrustManager
argument_list|(
name|invalidPrincipalName
argument_list|)
decl_stmt|;
name|TrustManager
index|[]
name|trustManagers
init|=
operator|new
name|TrustManager
index|[
literal|1
index|]
decl_stmt|;
name|trustManagers
index|[
literal|0
index|]
operator|=
name|trustManager
expr_stmt|;
name|tlsParams
operator|.
name|setTrustManagers
argument_list|(
name|trustManagers
argument_list|)
expr_stmt|;
name|tlsParams
operator|.
name|setDisableCNCheck
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Client
name|client
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|port
argument_list|)
decl_stmt|;
name|HTTPConduit
name|http
init|=
operator|(
name|HTTPConduit
operator|)
name|client
operator|.
name|getConduit
argument_list|()
decl_stmt|;
name|http
operator|.
name|setTlsClientParameters
argument_list|(
name|tlsParams
argument_list|)
expr_stmt|;
try|try
block|{
name|port
operator|.
name|greetMe
argument_list|(
literal|"Kitty"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on an invalid principal name"
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
specifier|public
specifier|static
class|class
name|NoOpX509TrustManager
implements|implements
name|X509TrustManager
block|{
specifier|public
name|NoOpX509TrustManager
parameter_list|()
block|{          }
annotation|@
name|Override
specifier|public
name|void
name|checkClientTrusted
parameter_list|(
name|X509Certificate
index|[]
name|chain
parameter_list|,
name|String
name|authType
parameter_list|)
throws|throws
name|CertificateException
block|{         }
annotation|@
name|Override
specifier|public
name|void
name|checkServerTrusted
parameter_list|(
name|X509Certificate
index|[]
name|chain
parameter_list|,
name|String
name|authType
parameter_list|)
throws|throws
name|CertificateException
block|{         }
annotation|@
name|Override
specifier|public
name|X509Certificate
index|[]
name|getAcceptedIssuers
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
specifier|static
class|class
name|ServerCertX509TrustManager
implements|implements
name|X509TrustManager
block|{
specifier|private
name|String
name|requiredServerPrincipalName
decl_stmt|;
specifier|public
name|ServerCertX509TrustManager
parameter_list|(
name|String
name|principalName
parameter_list|)
block|{
name|requiredServerPrincipalName
operator|=
name|principalName
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|checkClientTrusted
parameter_list|(
name|X509Certificate
index|[]
name|chain
parameter_list|,
name|String
name|authType
parameter_list|)
throws|throws
name|CertificateException
block|{         }
annotation|@
name|Override
specifier|public
name|void
name|checkServerTrusted
parameter_list|(
name|X509Certificate
index|[]
name|chain
parameter_list|,
name|String
name|authType
parameter_list|)
throws|throws
name|CertificateException
block|{
if|if
condition|(
name|chain
operator|==
literal|null
operator|||
name|chain
operator|.
name|length
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|CertificateException
argument_list|(
literal|"X509 Certificate chain is empty"
argument_list|)
throw|;
block|}
name|X509Certificate
name|serverCert
init|=
name|chain
index|[
literal|0
index|]
decl_stmt|;
if|if
condition|(
name|requiredServerPrincipalName
operator|!=
literal|null
operator|&&
operator|!
name|requiredServerPrincipalName
operator|.
name|equals
argument_list|(
name|serverCert
operator|.
name|getSubjectX500Principal
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|CertificateException
argument_list|(
literal|"X509 server certificate does not match requirement"
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|X509Certificate
index|[]
name|getAcceptedIssuers
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

