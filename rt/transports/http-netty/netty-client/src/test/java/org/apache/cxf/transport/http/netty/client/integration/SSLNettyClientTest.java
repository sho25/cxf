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
name|transport
operator|.
name|http
operator|.
name|netty
operator|.
name|client
operator|.
name|integration
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|GeneralSecurityException
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
name|KeyStoreException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchAlgorithmException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutionException
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
name|xml
operator|.
name|ws
operator|.
name|AsyncHandler
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
name|Endpoint
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
name|Response
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
name|netty
operator|.
name|client
operator|.
name|NettyHttpConduit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
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
name|hello_world_soap_http
operator|.
name|SOAPService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|types
operator|.
name|GreetMeLaterResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|types
operator|.
name|GreetMeResponse
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|SSLNettyClientTest
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
name|SSLNettyClientTest
operator|.
name|class
argument_list|)
decl_stmt|;
static|static
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"SSLNettyClientTest.port"
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
block|}
specifier|static
name|Endpoint
name|ep
decl_stmt|;
specifier|static
name|Greeter
name|g
decl_stmt|;
specifier|static
name|String
name|address
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
name|Bus
name|b
init|=
name|createStaticBus
argument_list|(
literal|"/org/apache/cxf/transport/http/netty/client/integration/ServerConfig.xml"
argument_list|)
decl_stmt|;
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|address
operator|=
literal|"https://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext/SoapPort"
expr_stmt|;
name|ep
operator|=
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
operator|new
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|GreeterImpl
argument_list|()
argument_list|)
expr_stmt|;
name|URL
name|wsdl
init|=
name|SSLNettyClientTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"/wsdl/hello_world.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"WSDL is null"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|SOAPService
name|service
init|=
operator|new
name|SOAPService
argument_list|(
name|wsdl
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Service is null"
argument_list|,
name|service
argument_list|)
expr_stmt|;
name|g
operator|=
name|service
operator|.
name|getSoapPort
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Port is null"
argument_list|,
name|g
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|g
operator|!=
literal|null
condition|)
block|{
operator|(
operator|(
name|java
operator|.
name|io
operator|.
name|Closeable
operator|)
name|g
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|ep
operator|!=
literal|null
condition|)
block|{
name|ep
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
name|ep
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInvocation
parameter_list|()
throws|throws
name|Exception
block|{
name|setupTLS
argument_list|(
name|g
argument_list|)
expr_stmt|;
name|setAddress
argument_list|(
name|g
argument_list|,
name|address
argument_list|)
expr_stmt|;
name|String
name|response
init|=
name|g
operator|.
name|greetMe
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong response"
argument_list|,
literal|"Hello test"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|GreetMeResponse
name|resp
init|=
operator|(
name|GreetMeResponse
operator|)
name|g
operator|.
name|greetMeAsync
argument_list|(
literal|"asyncTest"
argument_list|,
operator|new
name|AsyncHandler
argument_list|<
name|GreetMeResponse
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|handleResponse
parameter_list|(
name|Response
argument_list|<
name|GreetMeResponse
argument_list|>
name|res
parameter_list|)
block|{
try|try
block|{
name|res
operator|.
name|get
argument_list|()
operator|.
name|getResponseType
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello asyncTest"
argument_list|,
name|resp
operator|.
name|getResponseType
argument_list|()
argument_list|)
expr_stmt|;
name|MyLaterResponseHandler
name|handler
init|=
operator|new
name|MyLaterResponseHandler
argument_list|()
decl_stmt|;
name|g
operator|.
name|greetMeLaterAsync
argument_list|(
literal|1000
argument_list|,
name|handler
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
comment|// need to check the result here
name|assertEquals
argument_list|(
literal|"Hello, finally!"
argument_list|,
name|handler
operator|.
name|getResponse
argument_list|()
operator|.
name|getResponseType
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|setupTLS
parameter_list|(
name|Greeter
name|port
parameter_list|)
throws|throws
name|FileNotFoundException
throws|,
name|IOException
throws|,
name|GeneralSecurityException
block|{
name|String
name|keyStoreLoc
init|=
literal|"src/test/resources/org/apache/cxf/transport/http/netty/client/integration/clientKeystore.jks"
decl_stmt|;
name|NettyHttpConduit
name|httpConduit
init|=
operator|(
name|NettyHttpConduit
operator|)
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|port
argument_list|)
operator|.
name|getConduit
argument_list|()
decl_stmt|;
name|TLSClientParameters
name|tlsCP
init|=
operator|new
name|TLSClientParameters
argument_list|()
decl_stmt|;
name|String
name|keyPassword
init|=
literal|"ckpass"
decl_stmt|;
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
operator|new
name|FileInputStream
argument_list|(
name|keyStoreLoc
argument_list|)
argument_list|,
literal|"cspass"
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
name|KeyManager
index|[]
name|myKeyManagers
init|=
name|getKeyManagers
argument_list|(
name|keyStore
argument_list|,
name|keyPassword
argument_list|)
decl_stmt|;
name|tlsCP
operator|.
name|setKeyManagers
argument_list|(
name|myKeyManagers
argument_list|)
expr_stmt|;
name|KeyStore
name|trustStore
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
literal|"JKS"
argument_list|)
decl_stmt|;
name|trustStore
operator|.
name|load
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|keyStoreLoc
argument_list|)
argument_list|,
literal|"cspass"
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
name|TrustManager
index|[]
name|myTrustStoreKeyManagers
init|=
name|getTrustManagers
argument_list|(
name|trustStore
argument_list|)
decl_stmt|;
name|tlsCP
operator|.
name|setTrustManagers
argument_list|(
name|myTrustStoreKeyManagers
argument_list|)
expr_stmt|;
name|httpConduit
operator|.
name|setTlsClientParameters
argument_list|(
name|tlsCP
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|TrustManager
index|[]
name|getTrustManagers
parameter_list|(
name|KeyStore
name|trustStore
parameter_list|)
throws|throws
name|NoSuchAlgorithmException
throws|,
name|KeyStoreException
block|{
name|String
name|alg
init|=
name|KeyManagerFactory
operator|.
name|getDefaultAlgorithm
argument_list|()
decl_stmt|;
name|TrustManagerFactory
name|fac
init|=
name|TrustManagerFactory
operator|.
name|getInstance
argument_list|(
name|alg
argument_list|)
decl_stmt|;
name|fac
operator|.
name|init
argument_list|(
name|trustStore
argument_list|)
expr_stmt|;
return|return
name|fac
operator|.
name|getTrustManagers
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|KeyManager
index|[]
name|getKeyManagers
parameter_list|(
name|KeyStore
name|keyStore
parameter_list|,
name|String
name|keyPassword
parameter_list|)
throws|throws
name|GeneralSecurityException
throws|,
name|IOException
block|{
name|String
name|alg
init|=
name|KeyManagerFactory
operator|.
name|getDefaultAlgorithm
argument_list|()
decl_stmt|;
name|char
index|[]
name|keyPass
init|=
name|keyPassword
operator|!=
literal|null
condition|?
name|keyPassword
operator|.
name|toCharArray
argument_list|()
else|:
literal|null
decl_stmt|;
name|KeyManagerFactory
name|fac
init|=
name|KeyManagerFactory
operator|.
name|getInstance
argument_list|(
name|alg
argument_list|)
decl_stmt|;
name|fac
operator|.
name|init
argument_list|(
name|keyStore
argument_list|,
name|keyPass
argument_list|)
expr_stmt|;
return|return
name|fac
operator|.
name|getKeyManagers
argument_list|()
return|;
block|}
specifier|private
class|class
name|MyLaterResponseHandler
implements|implements
name|AsyncHandler
argument_list|<
name|GreetMeLaterResponse
argument_list|>
block|{
name|GreetMeLaterResponse
name|response
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|handleResponse
parameter_list|(
name|Response
argument_list|<
name|GreetMeLaterResponse
argument_list|>
name|res
parameter_list|)
block|{
try|try
block|{
name|response
operator|=
name|res
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
name|GreetMeLaterResponse
name|getResponse
parameter_list|()
block|{
return|return
name|response
return|;
block|}
block|}
block|}
end_class

end_unit

