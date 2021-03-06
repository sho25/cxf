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
name|spring
package|;
end_package

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
name|X509KeyManager
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
name|configuration
operator|.
name|jsse
operator|.
name|TLSParameterJaxBUtils
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
name|security
operator|.
name|AuthorizationPolicy
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
name|security
operator|.
name|FiltersType
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
name|security
operator|.
name|KeyManagersType
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
name|security
operator|.
name|KeyStoreType
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
name|security
operator|.
name|TrustManagersType
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
name|service
operator|.
name|model
operator|.
name|EndpointInfo
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
name|cxf
operator|.
name|transport
operator|.
name|http
operator|.
name|HTTPTransportFactory
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
name|transports
operator|.
name|http
operator|.
name|configuration
operator|.
name|HTTPClientPolicy
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|HttpConduitConfigurationTest
block|{
specifier|private
specifier|static
name|EndpointInfo
name|ei
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|setUpOnce
parameter_list|()
block|{
name|ei
operator|=
operator|new
name|EndpointInfo
argument_list|()
expr_stmt|;
name|ei
operator|.
name|setName
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world"
argument_list|,
literal|"HelloWorld"
argument_list|)
argument_list|)
expr_stmt|;
name|ei
operator|.
name|setAddress
argument_list|(
literal|"https://localhost:8443/nopath"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConduitBean
parameter_list|()
throws|throws
name|Exception
block|{
name|SpringBusFactory
name|factory
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|bus
operator|=
name|factory
operator|.
name|createBus
argument_list|(
literal|"org/apache/cxf/transport/http/spring/conduit-bean.xml"
argument_list|)
expr_stmt|;
name|HTTPTransportFactory
name|atf
init|=
operator|new
name|HTTPTransportFactory
argument_list|()
decl_stmt|;
name|HTTPConduit
name|conduit
init|=
operator|(
name|HTTPConduit
operator|)
name|atf
operator|.
name|getConduit
argument_list|(
name|ei
argument_list|,
name|bus
argument_list|)
decl_stmt|;
name|verifyConduit
argument_list|(
name|conduit
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConduitBeanWithTLSReferences
parameter_list|()
throws|throws
name|Exception
block|{
name|SpringBusFactory
name|factory
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|bus
operator|=
name|factory
operator|.
name|createBus
argument_list|(
literal|"org/apache/cxf/transport/http/spring/conduit-tlsrefs-bean.xml"
argument_list|)
expr_stmt|;
name|HTTPTransportFactory
name|atf
init|=
operator|new
name|HTTPTransportFactory
argument_list|()
decl_stmt|;
name|HTTPConduit
name|conduit
init|=
operator|(
name|HTTPConduit
operator|)
name|atf
operator|.
name|getConduit
argument_list|(
name|ei
argument_list|,
name|bus
argument_list|)
decl_stmt|;
name|verifyConduit
argument_list|(
name|conduit
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|verifyConduit
parameter_list|(
name|HTTPConduit
name|conduit
parameter_list|)
block|{
name|AuthorizationPolicy
name|authp
init|=
name|conduit
operator|.
name|getAuthorization
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|authp
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Betty"
argument_list|,
name|authp
operator|.
name|getUserName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"password"
argument_list|,
name|authp
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|TLSClientParameters
name|tlscps
init|=
name|conduit
operator|.
name|getTlsClientParameters
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|tlscps
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tlscps
operator|.
name|isDisableCNCheck
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3600000
argument_list|,
name|tlscps
operator|.
name|getSslCacheTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|KeyManager
index|[]
name|kms
init|=
name|tlscps
operator|.
name|getKeyManagers
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|kms
operator|!=
literal|null
operator|&&
name|kms
operator|.
name|length
operator|==
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|kms
index|[
literal|0
index|]
operator|instanceof
name|X509KeyManager
argument_list|)
expr_stmt|;
name|TrustManager
index|[]
name|tms
init|=
name|tlscps
operator|.
name|getTrustManagers
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|tms
operator|!=
literal|null
operator|&&
name|tms
operator|.
name|length
operator|==
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tms
index|[
literal|0
index|]
operator|instanceof
name|X509TrustManager
argument_list|)
expr_stmt|;
name|FiltersType
name|csfs
init|=
name|tlscps
operator|.
name|getCipherSuitesFilter
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|csfs
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|csfs
operator|.
name|getInclude
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|csfs
operator|.
name|getExclude
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|HTTPClientPolicy
name|clientPolicy
init|=
name|conduit
operator|.
name|getClient
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|10240
argument_list|,
name|clientPolicy
operator|.
name|getChunkLength
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
specifier|final
class|class
name|ManagersFactory
block|{
specifier|public
specifier|static
name|KeyManager
index|[]
name|getKeyManagers
parameter_list|()
block|{
name|KeyManagersType
name|kmt
init|=
operator|new
name|KeyManagersType
argument_list|()
decl_stmt|;
name|KeyStoreType
name|kst
init|=
operator|new
name|KeyStoreType
argument_list|()
decl_stmt|;
name|kst
operator|.
name|setResource
argument_list|(
literal|"org/apache/cxf/transport/https/resources/Bethal.jks"
argument_list|)
expr_stmt|;
name|kst
operator|.
name|setPassword
argument_list|(
literal|"password"
argument_list|)
expr_stmt|;
name|kst
operator|.
name|setType
argument_list|(
literal|"JKS"
argument_list|)
expr_stmt|;
name|kmt
operator|.
name|setKeyStore
argument_list|(
name|kst
argument_list|)
expr_stmt|;
name|kmt
operator|.
name|setKeyPassword
argument_list|(
literal|"password"
argument_list|)
expr_stmt|;
try|try
block|{
return|return
name|TLSParameterJaxBUtils
operator|.
name|getKeyManagers
argument_list|(
name|kmt
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"failed to retrieve key managers"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|static
name|TrustManager
index|[]
name|getTrustManagers
parameter_list|()
block|{
name|TrustManagersType
name|tmt
init|=
operator|new
name|TrustManagersType
argument_list|()
decl_stmt|;
name|KeyStoreType
name|kst
init|=
operator|new
name|KeyStoreType
argument_list|()
decl_stmt|;
name|kst
operator|.
name|setResource
argument_list|(
literal|"org/apache/cxf/transport/https/resources/Gordy.jks"
argument_list|)
expr_stmt|;
name|kst
operator|.
name|setPassword
argument_list|(
literal|"password"
argument_list|)
expr_stmt|;
name|kst
operator|.
name|setType
argument_list|(
literal|"JKS"
argument_list|)
expr_stmt|;
name|tmt
operator|.
name|setKeyStore
argument_list|(
name|kst
argument_list|)
expr_stmt|;
try|try
block|{
return|return
name|TLSParameterJaxBUtils
operator|.
name|getTrustManagers
argument_list|(
name|tmt
argument_list|,
literal|false
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"failed to retrieve trust managers"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

