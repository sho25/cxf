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
name|jaxrs
operator|.
name|security
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
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
name|endpoint
operator|.
name|ClientLifeCycleListener
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
name|ClientLifeCycleManager
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
name|Endpoint
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
name|JAXRSClientFactory
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
name|JAXRSClientFactoryBean
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
name|jaxrs
operator|.
name|Book
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
name|jaxrs
operator|.
name|BookStore
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
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
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
name|Ignore
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

begin_class
specifier|public
class|class
name|JAXRSHttpsBookTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|BookHttpsServer
operator|.
name|PORT
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CLIENT_CONFIG_FILE1
init|=
literal|"org/apache/cxf/systest/jaxrs/security/jaxrs-https-client1.xml"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CLIENT_CONFIG_FILE2
init|=
literal|"org/apache/cxf/systest/jaxrs/security/jaxrs-https-client2.xml"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CLIENT_CONFIG_FILE3
init|=
literal|"org/apache/cxf/systest/jaxrs/security/jaxrs-https-client3.xml"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CLIENT_CONFIG_FILE4
init|=
literal|"org/apache/cxf/systest/jaxrs/security/jaxrs-https-client4.xml"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CLIENT_CONFIG_FILE5
init|=
literal|"org/apache/cxf/systest/jaxrs/security/jaxrs-https-client5.xml"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CLIENT_CONFIG_FILE_OLD
init|=
literal|"org/apache/cxf/systest/jaxrs/security/jaxrs-https-client_old.xml"
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
name|createStaticBus
argument_list|(
literal|"org/apache/cxf/systest/jaxrs/security/jaxrs-https-server.xml"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|BookHttpsServer
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBook123Proxy
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestGetBook123Proxy
argument_list|(
name|CLIENT_CONFIG_FILE1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBook123ProxyWithURLConduitId
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestGetBook123Proxy
argument_list|(
name|CLIENT_CONFIG_FILE2
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doTestGetBook123Proxy
parameter_list|(
name|String
name|configFile
parameter_list|)
throws|throws
name|Exception
block|{
name|BookStore
name|bs
init|=
name|JAXRSClientFactory
operator|.
name|create
argument_list|(
literal|"https://localhost:"
operator|+
name|PORT
argument_list|,
name|BookStore
operator|.
name|class
argument_list|,
name|configFile
argument_list|)
decl_stmt|;
comment|// just to verify the interface call goes through CGLIB proxy too
name|assertEquals
argument_list|(
literal|"https://localhost:"
operator|+
name|PORT
argument_list|,
name|WebClient
operator|.
name|client
argument_list|(
name|bs
argument_list|)
operator|.
name|getBaseURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|Book
name|b
init|=
name|bs
operator|.
name|getSecureBook
argument_list|(
literal|"123"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|b
operator|.
name|getId
argument_list|()
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|b
operator|=
name|bs
operator|.
name|getSecureBook
argument_list|(
literal|"123"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|b
operator|.
name|getId
argument_list|()
argument_list|,
literal|123
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBook123ProxyFromSpring
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestGetBook123ProxyFromSpring
argument_list|(
name|CLIENT_CONFIG_FILE3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBook123ProxyFromSpringWildcard
parameter_list|()
throws|throws
name|Exception
block|{
name|ClassPathXmlApplicationContext
name|ctx
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
operator|new
name|String
index|[]
block|{
name|CLIENT_CONFIG_FILE4
block|}
argument_list|)
decl_stmt|;
name|Object
name|bean
init|=
name|ctx
operator|.
name|getBean
argument_list|(
literal|"bookService.proxyFactory"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|JAXRSClientFactoryBean
name|cfb
init|=
operator|(
name|JAXRSClientFactoryBean
operator|)
name|bean
decl_stmt|;
name|BookStore
name|bs
init|=
name|cfb
operator|.
name|create
argument_list|(
name|BookStore
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"https://localhost:"
operator|+
name|PORT
argument_list|,
name|WebClient
operator|.
name|client
argument_list|(
name|bs
argument_list|)
operator|.
name|getBaseURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|fromClient
argument_list|(
name|WebClient
operator|.
name|client
argument_list|(
name|bs
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"https://localhost:"
operator|+
name|PORT
argument_list|,
name|WebClient
operator|.
name|client
argument_list|(
name|bs
argument_list|)
operator|.
name|getBaseURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|wc
operator|.
name|accept
argument_list|(
literal|"application/xml"
argument_list|)
expr_stmt|;
name|wc
operator|.
name|path
argument_list|(
literal|"bookstore/securebooks/123"
argument_list|)
expr_stmt|;
name|TheBook
name|b
init|=
name|wc
operator|.
name|get
argument_list|(
name|TheBook
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|b
operator|.
name|getId
argument_list|()
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|b
operator|=
name|wc
operator|.
name|get
argument_list|(
name|TheBook
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|b
operator|.
name|getId
argument_list|()
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCustomVerbProxyFromSpringWildcard
parameter_list|()
throws|throws
name|Exception
block|{
name|ClassPathXmlApplicationContext
name|ctx
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
operator|new
name|String
index|[]
block|{
name|CLIENT_CONFIG_FILE3
block|}
argument_list|)
decl_stmt|;
name|Object
name|bean
init|=
name|ctx
operator|.
name|getBean
argument_list|(
literal|"bookService.proxyFactory"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|JAXRSClientFactoryBean
name|cfb
init|=
operator|(
name|JAXRSClientFactoryBean
operator|)
name|bean
decl_stmt|;
name|BookStore
name|bs
init|=
name|cfb
operator|.
name|create
argument_list|(
name|BookStore
operator|.
name|class
argument_list|)
decl_stmt|;
name|WebClient
operator|.
name|getConfig
argument_list|(
name|bs
argument_list|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"use.httpurlconnection.method.reflection"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// CXF RS Client code will set this property to true if the http verb is unknown
comment|// and this property is not already set. The async conduit is loaded in the tests module
comment|// but we do want to test HTTPUrlConnection reflection hence we set this property to false
name|WebClient
operator|.
name|getConfig
argument_list|(
name|bs
argument_list|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"use.async.http.conduit"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|Book
name|book
init|=
name|bs
operator|.
name|retrieveBook
argument_list|(
operator|new
name|Book
argument_list|(
literal|"Retrieve"
argument_list|,
literal|123L
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Retrieve"
argument_list|,
name|book
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBook123WebClientFromSpringWildcard
parameter_list|()
throws|throws
name|Exception
block|{
name|ClassPathXmlApplicationContext
name|ctx
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
operator|new
name|String
index|[]
block|{
name|CLIENT_CONFIG_FILE5
block|}
argument_list|)
decl_stmt|;
name|Object
name|bean
init|=
name|ctx
operator|.
name|getBean
argument_list|(
literal|"bookService.proxyFactory"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|JAXRSClientFactoryBean
name|cfb
init|=
operator|(
name|JAXRSClientFactoryBean
operator|)
name|bean
decl_stmt|;
name|WebClient
name|wc
init|=
operator|(
name|WebClient
operator|)
name|cfb
operator|.
name|create
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"https://localhost:"
operator|+
name|PORT
argument_list|,
name|wc
operator|.
name|getBaseURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|wc
operator|.
name|accept
argument_list|(
literal|"application/xml"
argument_list|)
expr_stmt|;
name|wc
operator|.
name|path
argument_list|(
literal|"bookstore/securebooks/123"
argument_list|)
expr_stmt|;
name|TheBook
name|b
init|=
name|wc
operator|.
name|get
argument_list|(
name|TheBook
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|b
operator|.
name|getId
argument_list|()
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|b
operator|=
name|wc
operator|.
name|get
argument_list|(
name|TheBook
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|b
operator|.
name|getId
argument_list|()
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Ignore
argument_list|(
literal|"Works in the studio only if local jaxrs.xsd is updated to have jaxrs:client"
argument_list|)
specifier|public
name|void
name|testGetBook123WebClientFromSpringWildcardOldJaxrsClient
parameter_list|()
throws|throws
name|Exception
block|{
name|ClassPathXmlApplicationContext
name|ctx
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
operator|new
name|String
index|[]
block|{
name|CLIENT_CONFIG_FILE_OLD
block|}
argument_list|)
decl_stmt|;
name|Object
name|bean
init|=
name|ctx
operator|.
name|getBean
argument_list|(
literal|"bookService.proxyFactory"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|JAXRSClientFactoryBean
name|cfb
init|=
operator|(
name|JAXRSClientFactoryBean
operator|)
name|bean
decl_stmt|;
name|WebClient
name|wc
init|=
operator|(
name|WebClient
operator|)
name|cfb
operator|.
name|create
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"https://localhost:"
operator|+
name|PORT
argument_list|,
name|wc
operator|.
name|getBaseURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|wc
operator|.
name|accept
argument_list|(
literal|"application/xml"
argument_list|)
expr_stmt|;
name|wc
operator|.
name|path
argument_list|(
literal|"bookstore/securebooks/123"
argument_list|)
expr_stmt|;
name|TheBook
name|b
init|=
name|wc
operator|.
name|get
argument_list|(
name|TheBook
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|b
operator|.
name|getId
argument_list|()
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|b
operator|=
name|wc
operator|.
name|get
argument_list|(
name|TheBook
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|b
operator|.
name|getId
argument_list|()
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|doTestGetBook123ProxyFromSpring
parameter_list|(
name|String
name|cfgFile
parameter_list|)
throws|throws
name|Exception
block|{
name|ClassPathXmlApplicationContext
name|ctx
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
operator|new
name|String
index|[]
block|{
name|cfgFile
block|}
argument_list|)
decl_stmt|;
name|Object
name|bean
init|=
name|ctx
operator|.
name|getBean
argument_list|(
literal|"bookService.proxyFactory"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|JAXRSClientFactoryBean
name|cfb
init|=
operator|(
name|JAXRSClientFactoryBean
operator|)
name|bean
decl_stmt|;
name|Bus
name|bus
init|=
name|cfb
operator|.
name|getBus
argument_list|()
decl_stmt|;
name|ClientLifeCycleManager
name|manager
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ClientLifeCycleManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|TestClientLifeCycleListener
name|listener
init|=
operator|new
name|TestClientLifeCycleListener
argument_list|()
decl_stmt|;
name|manager
operator|.
name|registerListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|BookStore
name|bs
init|=
name|cfb
operator|.
name|create
argument_list|(
name|BookStore
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|listener
operator|.
name|getEp
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"{http://service.rs}BookService"
argument_list|,
name|listener
operator|.
name|getEp
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"https://localhost:"
operator|+
name|PORT
argument_list|,
name|WebClient
operator|.
name|client
argument_list|(
name|bs
argument_list|)
operator|.
name|getBaseURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|Book
name|b
init|=
name|bs
operator|.
name|getSecureBook
argument_list|(
literal|"123"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|b
operator|.
name|getId
argument_list|()
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|b
operator|=
name|bs
operator|.
name|getSecureBook
argument_list|(
literal|"123"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|b
operator|.
name|getId
argument_list|()
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBook123ProxyToWebClient
parameter_list|()
throws|throws
name|Exception
block|{
name|BookStore
name|bs
init|=
name|JAXRSClientFactory
operator|.
name|create
argument_list|(
literal|"https://localhost:"
operator|+
name|PORT
argument_list|,
name|BookStore
operator|.
name|class
argument_list|,
name|CLIENT_CONFIG_FILE1
argument_list|)
decl_stmt|;
name|Book
name|b
init|=
name|bs
operator|.
name|getSecureBook
argument_list|(
literal|"123"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|b
operator|.
name|getId
argument_list|()
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|fromClient
argument_list|(
name|WebClient
operator|.
name|client
argument_list|(
name|bs
argument_list|)
argument_list|)
decl_stmt|;
name|wc
operator|.
name|path
argument_list|(
literal|"/bookstore/securebooks/123"
argument_list|)
operator|.
name|accept
argument_list|(
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|)
expr_stmt|;
name|Book
name|b2
init|=
name|wc
operator|.
name|get
argument_list|(
name|Book
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|b2
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBook123WebClientToProxy
parameter_list|()
throws|throws
name|Exception
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"https://localhost:"
operator|+
name|PORT
argument_list|,
name|CLIENT_CONFIG_FILE1
argument_list|)
decl_stmt|;
name|wc
operator|.
name|path
argument_list|(
literal|"/bookstore/securebooks/123"
argument_list|)
operator|.
name|accept
argument_list|(
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|)
expr_stmt|;
name|Book
name|b
init|=
name|wc
operator|.
name|get
argument_list|(
name|Book
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|b
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|wc
operator|.
name|back
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|BookStore
name|bs
init|=
name|JAXRSClientFactory
operator|.
name|fromClient
argument_list|(
name|wc
argument_list|,
name|BookStore
operator|.
name|class
argument_list|)
decl_stmt|;
name|Book
name|b2
init|=
name|bs
operator|.
name|getSecureBook
argument_list|(
literal|"123"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|b2
operator|.
name|getId
argument_list|()
argument_list|,
literal|123
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBook123WebClient
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestGetBook123WebClient
argument_list|(
name|CLIENT_CONFIG_FILE1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBook123WebClientWithURLConduitId
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestGetBook123WebClient
argument_list|(
name|CLIENT_CONFIG_FILE2
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doTestGetBook123WebClient
parameter_list|(
name|String
name|configFile
parameter_list|)
throws|throws
name|Exception
block|{
name|WebClient
name|client
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"https://localhost:"
operator|+
name|PORT
argument_list|,
name|configFile
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"https://localhost:"
operator|+
name|PORT
argument_list|,
name|client
operator|.
name|getBaseURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|path
argument_list|(
literal|"/bookstore/securebooks/123"
argument_list|)
operator|.
name|accept
argument_list|(
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|)
expr_stmt|;
name|Book
name|b
init|=
name|client
operator|.
name|get
argument_list|(
name|Book
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|b
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"TheBook"
argument_list|)
specifier|public
specifier|static
class|class
name|TheBook
block|{
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|long
name|id
decl_stmt|;
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|long
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
specifier|public
name|void
name|setId
parameter_list|(
name|long
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
class|class
name|TestClientLifeCycleListener
implements|implements
name|ClientLifeCycleListener
block|{
specifier|private
name|Endpoint
name|ep
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|clientCreated
parameter_list|(
name|Client
name|client
parameter_list|)
block|{
name|this
operator|.
name|ep
operator|=
name|client
operator|.
name|getEndpoint
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|clientDestroyed
parameter_list|(
name|Client
name|client
parameter_list|)
block|{
name|ep
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|Endpoint
name|getEp
parameter_list|()
block|{
return|return
name|ep
return|;
block|}
block|}
block|}
end_class

end_unit

