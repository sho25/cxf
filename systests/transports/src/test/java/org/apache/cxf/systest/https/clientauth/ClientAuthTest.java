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
name|clientauth
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|security
operator|.
name|KeyStore
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
name|HostnameVerifier
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
name|HttpsURLConnection
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
name|KeyManager
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
name|KeyManagerFactory
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
name|SSLContext
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
name|SSLSession
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
name|TrustManagerFactory
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
name|common
operator|.
name|classloader
operator|.
name|ClassLoaderUtils
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
comment|/**  * A set of tests for TLS client authentication.  */
end_comment

begin_class
specifier|public
class|class
name|ClientAuthTest
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
name|ClientAuthServer
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
name|ClientAuthServer
operator|.
name|class
argument_list|,
literal|2
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
name|ClientAuthServer
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
comment|// Server directly trusts the client cert
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testDirectTrust
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
name|ClientAuthTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client-auth.xml"
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
comment|// Server does not (directly) trust the client cert
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testInvalidDirectTrust
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
name|ClientAuthTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client-auth-invalid.xml"
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
literal|"Failure expected on an untrusted cert"
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
comment|// Client does not specify a KeyStore, only a TrustStore
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testNoClientCert
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
name|ClientAuthTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client-no-auth.xml"
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
literal|"Failure expected on no trusted cert"
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
comment|// Server trusts the issuer of the client cert
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testChainTrust
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
name|ClientAuthTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client-auth-chain.xml"
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
name|PORT2
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
comment|// Server does not trust the issuer of the client cert
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testInvalidChainTrust
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
name|ClientAuthTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client-auth-invalid2.xml"
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
name|PORT2
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
literal|"Failure expected on no trusted cert"
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
comment|// Client does not trust the issuer of the server cert
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testClientInvalidCertChain
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
name|ClientAuthTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client-auth-invalid2.xml"
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
literal|"Failure expected on no trusted cert"
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
comment|// Client does not directly trust the server cert
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testClientInvalidDirectTrust
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
name|ClientAuthTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client-auth-invalid.xml"
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
name|PORT2
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
literal|"Failure expected on no trusted cert"
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
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testSSLConnectionUsingJavaAPIs
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|service
init|=
operator|new
name|URL
argument_list|(
literal|"https://localhost:"
operator|+
name|PORT
argument_list|)
decl_stmt|;
name|HttpsURLConnection
name|connection
init|=
operator|(
name|HttpsURLConnection
operator|)
name|service
operator|.
name|openConnection
argument_list|()
decl_stmt|;
name|connection
operator|.
name|setHostnameVerifier
argument_list|(
operator|new
name|DisableCNCheckVerifier
argument_list|()
argument_list|)
expr_stmt|;
name|SSLContext
name|sslContext
init|=
name|SSLContext
operator|.
name|getInstance
argument_list|(
literal|"TLS"
argument_list|)
decl_stmt|;
name|KeyStore
name|ts
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
literal|"JKS"
argument_list|)
decl_stmt|;
try|try
init|(
name|InputStream
name|trustStore
init|=
name|ClassLoaderUtils
operator|.
name|getResourceAsStream
argument_list|(
literal|"keys/Truststore.jks"
argument_list|,
name|ClientAuthTest
operator|.
name|class
argument_list|)
init|)
block|{
name|ts
operator|.
name|load
argument_list|(
name|trustStore
argument_list|,
literal|"password"
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|TrustManagerFactory
name|tmf
init|=
name|TrustManagerFactory
operator|.
name|getInstance
argument_list|(
literal|"PKIX"
argument_list|)
decl_stmt|;
name|tmf
operator|.
name|init
argument_list|(
name|ts
argument_list|)
expr_stmt|;
name|KeyStore
name|ks
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
literal|"JKS"
argument_list|)
decl_stmt|;
try|try
init|(
name|InputStream
name|keyStore
init|=
name|ClassLoaderUtils
operator|.
name|getResourceAsStream
argument_list|(
literal|"keys/Morpit.jks"
argument_list|,
name|ClientAuthTest
operator|.
name|class
argument_list|)
init|)
block|{
name|ks
operator|.
name|load
argument_list|(
name|keyStore
argument_list|,
literal|"password"
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|KeyManagerFactory
name|kmf
init|=
name|KeyManagerFactory
operator|.
name|getInstance
argument_list|(
literal|"PKIX"
argument_list|)
decl_stmt|;
name|kmf
operator|.
name|init
argument_list|(
name|ks
argument_list|,
literal|"password"
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
name|sslContext
operator|.
name|init
argument_list|(
name|kmf
operator|.
name|getKeyManagers
argument_list|()
argument_list|,
name|tmf
operator|.
name|getTrustManagers
argument_list|()
argument_list|,
operator|new
name|java
operator|.
name|security
operator|.
name|SecureRandom
argument_list|()
argument_list|)
expr_stmt|;
name|connection
operator|.
name|setSSLSocketFactory
argument_list|(
name|sslContext
operator|.
name|getSocketFactory
argument_list|()
argument_list|)
expr_stmt|;
name|connection
operator|.
name|connect
argument_list|()
expr_stmt|;
name|connection
operator|.
name|disconnect
argument_list|()
expr_stmt|;
block|}
comment|// https://issues.apache.org/jira/browse/CXF-7763
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testCheckKeyManagersWithCertAlias
parameter_list|()
throws|throws
name|Exception
block|{
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
comment|// Set up (shared) KeyManagers/TrustManagers
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
name|KeyManagerFactory
name|kmf
init|=
name|KeyManagerFactory
operator|.
name|getInstance
argument_list|(
name|KeyManagerFactory
operator|.
name|getDefaultAlgorithm
argument_list|()
argument_list|)
decl_stmt|;
try|try
init|(
name|InputStream
name|inputStream
init|=
name|ClassLoaderUtils
operator|.
name|getResourceAsStream
argument_list|(
literal|"keymanagers.jks"
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
init|)
block|{
name|KeyStore
name|keyStore
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
literal|"JKS"
argument_list|)
decl_stmt|;
name|keyStore
operator|.
name|load
argument_list|(
name|inputStream
argument_list|,
literal|"password"
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
name|kmf
operator|.
name|init
argument_list|(
name|keyStore
argument_list|,
literal|"password"
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|KeyManager
index|[]
name|keyManagers
init|=
name|kmf
operator|.
name|getKeyManagers
argument_list|()
decl_stmt|;
comment|// First call to PORT using Morpit
name|TLSClientParameters
name|tlsParams
init|=
operator|new
name|TLSClientParameters
argument_list|()
decl_stmt|;
name|tlsParams
operator|.
name|setKeyManagers
argument_list|(
name|keyManagers
argument_list|)
expr_stmt|;
name|tlsParams
operator|.
name|setCertAlias
argument_list|(
literal|"morpit"
argument_list|)
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
comment|// Second call to PORT2 using "alice"
name|tlsParams
operator|=
operator|new
name|TLSClientParameters
argument_list|()
expr_stmt|;
name|tlsParams
operator|.
name|setKeyManagers
argument_list|(
name|keyManagers
argument_list|)
expr_stmt|;
name|tlsParams
operator|.
name|setCertAlias
argument_list|(
literal|"alice"
argument_list|)
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
name|port
operator|=
name|service
operator|.
name|getHttpsPort
argument_list|()
expr_stmt|;
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
name|PORT2
argument_list|)
expr_stmt|;
name|client
operator|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|http
operator|=
operator|(
name|HTTPConduit
operator|)
name|client
operator|.
name|getConduit
argument_list|()
expr_stmt|;
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
block|}
specifier|private
specifier|static
specifier|final
class|class
name|DisableCNCheckVerifier
implements|implements
name|HostnameVerifier
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|verify
parameter_list|(
name|String
name|arg0
parameter_list|,
name|SSLSession
name|arg1
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
block|}
empty_stmt|;
specifier|private
specifier|static
class|class
name|NoOpX509TrustManager
implements|implements
name|X509TrustManager
block|{
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
block|}
end_class

end_unit

