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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|HttpURLConnection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|Socket
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
name|net
operator|.
name|URLConnection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|List
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
name|xpath
operator|.
name|XPathConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|helpers
operator|.
name|DOMUtils
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
name|helpers
operator|.
name|IOUtils
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
name|helpers
operator|.
name|XPathUtils
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
name|interceptor
operator|.
name|LoggingInInterceptor
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
name|jaxrs
operator|.
name|model
operator|.
name|AbstractResourceInfo
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
name|model
operator|.
name|wadl
operator|.
name|WadlGenerator
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
name|provider
operator|.
name|JAXBElementProvider
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
name|staxutils
operator|.
name|StaxUtils
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
name|JAXRSClientServerResourceCreatedSpringProviderTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|BookServerResourceCreatedSpringProviders
operator|.
name|PORT
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
name|AbstractResourceInfo
operator|.
name|clearAllMaps
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|BookServerResourceCreatedSpringProviders
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|createStaticBus
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBasePetStoreWithoutTrailingSlash
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/webapp/pets"
decl_stmt|;
name|WebClient
name|client
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|endpointAddress
argument_list|)
decl_stmt|;
name|HTTPConduit
name|conduit
init|=
name|WebClient
operator|.
name|getConfig
argument_list|(
name|client
argument_list|)
operator|.
name|getHttpConduit
argument_list|()
decl_stmt|;
name|conduit
operator|.
name|getClient
argument_list|()
operator|.
name|setReceiveTimeout
argument_list|(
literal|1000000
argument_list|)
expr_stmt|;
name|conduit
operator|.
name|getClient
argument_list|()
operator|.
name|setConnectionTimeout
argument_list|(
literal|1000000
argument_list|)
expr_stmt|;
name|String
name|value
init|=
name|client
operator|.
name|accept
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|get
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|PetStore
operator|.
name|CLOSED
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBasePetStore
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/webapp/pets/"
decl_stmt|;
name|WebClient
name|client
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|endpointAddress
argument_list|)
decl_stmt|;
name|HTTPConduit
name|conduit
init|=
name|WebClient
operator|.
name|getConfig
argument_list|(
name|client
argument_list|)
operator|.
name|getHttpConduit
argument_list|()
decl_stmt|;
name|conduit
operator|.
name|getClient
argument_list|()
operator|.
name|setReceiveTimeout
argument_list|(
literal|1000000
argument_list|)
expr_stmt|;
name|conduit
operator|.
name|getClient
argument_list|()
operator|.
name|setConnectionTimeout
argument_list|(
literal|1000000
argument_list|)
expr_stmt|;
name|String
name|value
init|=
name|client
operator|.
name|accept
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|get
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|PetStore
operator|.
name|CLOSED
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMultipleRootsWadl
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Element
argument_list|>
name|resourceEls
init|=
name|getWadlResourcesInfo
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/webapp/resources"
argument_list|,
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/webapp/resources"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|String
name|path1
init|=
name|resourceEls
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getAttribute
argument_list|(
literal|"path"
argument_list|)
decl_stmt|;
name|int
name|bookStoreInd
init|=
name|path1
operator|.
name|contains
argument_list|(
literal|"/bookstore"
argument_list|)
condition|?
literal|0
else|:
literal|1
decl_stmt|;
name|int
name|petStoreInd
init|=
name|bookStoreInd
operator|==
literal|0
condition|?
literal|1
else|:
literal|0
decl_stmt|;
name|checkBookStoreInfo
argument_list|(
name|resourceEls
operator|.
name|get
argument_list|(
name|bookStoreInd
argument_list|)
argument_list|)
expr_stmt|;
name|checkServletInfo
argument_list|(
name|resourceEls
operator|.
name|get
argument_list|(
name|petStoreInd
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBookStoreWadl
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Element
argument_list|>
name|resourceEls
init|=
name|getWadlResourcesInfo
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/webapp/resources"
argument_list|,
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/webapp/resources/bookstore"
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|checkBookStoreInfo
argument_list|(
name|resourceEls
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertValidType
parameter_list|(
name|XPathUtils
name|xpu
parameter_list|,
name|String
name|xpath
parameter_list|,
name|String
name|type
parameter_list|,
name|Element
name|el
parameter_list|)
block|{
name|String
name|s
init|=
operator|(
name|String
operator|)
name|xpu
operator|.
name|getValue
argument_list|(
name|xpath
argument_list|,
name|el
argument_list|,
name|XPathConstants
operator|.
name|STRING
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Expected "
operator|+
name|type
operator|+
literal|" but found "
operator|+
name|s
argument_list|,
name|s
operator|.
name|endsWith
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertValid
parameter_list|(
name|XPathUtils
name|xpu
parameter_list|,
name|String
name|xpath
parameter_list|,
name|Element
name|el
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|xpu
operator|.
name|getValue
argument_list|(
name|xpath
argument_list|,
name|el
argument_list|,
name|XPathConstants
operator|.
name|NODE
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPetStoreWadl
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Element
argument_list|>
name|resourceEls
init|=
name|getWadlResourcesInfo
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/webapp/pets"
argument_list|,
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/webapp/pets/"
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|checkPetStoreInfo
argument_list|(
name|resourceEls
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|Element
name|el
init|=
operator|(
name|Element
operator|)
name|resourceEls
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getParentNode
argument_list|()
operator|.
name|getParentNode
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|namespaces
operator|.
name|put
argument_list|(
literal|"xsd"
argument_list|,
literal|"http://www.w3.org/2001/XMLSchema"
argument_list|)
expr_stmt|;
name|namespaces
operator|.
name|put
argument_list|(
literal|"ns"
argument_list|,
literal|"http://pets"
argument_list|)
expr_stmt|;
name|namespaces
operator|.
name|put
argument_list|(
literal|"wadl"
argument_list|,
literal|"http://wadl.dev.java.net/2009/02"
argument_list|)
expr_stmt|;
name|XPathUtils
name|xpu
init|=
operator|new
name|XPathUtils
argument_list|(
name|namespaces
argument_list|)
decl_stmt|;
name|assertValidType
argument_list|(
name|xpu
argument_list|,
literal|"//xsd:element[@name='elstatus']/@type"
argument_list|,
literal|"petStoreStatusElement"
argument_list|,
name|el
argument_list|)
expr_stmt|;
name|assertValidType
argument_list|(
name|xpu
argument_list|,
literal|"//xsd:schema[@targetNamespace='http://pets']/xsd:element[@name='status']/@type"
argument_list|,
literal|"status"
argument_list|,
name|el
argument_list|)
expr_stmt|;
name|assertValidType
argument_list|(
name|xpu
argument_list|,
literal|"//xsd:element[@name='statusType']/@type"
argument_list|,
literal|"statusType"
argument_list|,
name|el
argument_list|)
expr_stmt|;
name|assertValidType
argument_list|(
name|xpu
argument_list|,
literal|"//xsd:element[@name='statusImpl1']/@type"
argument_list|,
literal|"petStoreStatusImpl1"
argument_list|,
name|el
argument_list|)
expr_stmt|;
name|assertValidType
argument_list|(
name|xpu
argument_list|,
literal|"//xsd:element[@name='statusImpl1']/@substitutionGroup"
argument_list|,
literal|"statusType"
argument_list|,
name|el
argument_list|)
expr_stmt|;
name|assertValidType
argument_list|(
name|xpu
argument_list|,
literal|"//xsd:element[@name='statusImpl2']/@type"
argument_list|,
literal|"petStoreStatusImpl2"
argument_list|,
name|el
argument_list|)
expr_stmt|;
name|assertValidType
argument_list|(
name|xpu
argument_list|,
literal|"//xsd:element[@name='statusImpl2']/@substitutionGroup"
argument_list|,
literal|"statusType"
argument_list|,
name|el
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
name|xpu
argument_list|,
literal|"//wadl:representation[@element='prefix1:status']"
argument_list|,
name|el
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
name|xpu
argument_list|,
literal|"//wadl:representation[@element='prefix1:elstatus']"
argument_list|,
name|el
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
name|xpu
argument_list|,
literal|"//wadl:representation[@element='prefix1:statuses']"
argument_list|,
name|el
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
name|xpu
argument_list|,
literal|"//wadl:representation[@element='prefix1:statusType']"
argument_list|,
name|el
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWadlPublishedEndpointUrl
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|requestURI
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/webapp/resources2"
decl_stmt|;
name|WebClient
name|client
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|requestURI
operator|+
literal|"?_wadl&_type=xml"
argument_list|)
decl_stmt|;
name|Document
name|doc
init|=
name|StaxUtils
operator|.
name|read
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|client
operator|.
name|get
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
decl_stmt|;
name|Element
name|root
init|=
name|doc
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|WadlGenerator
operator|.
name|WADL_NS
argument_list|,
name|root
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"application"
argument_list|,
name|root
operator|.
name|getLocalName
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Element
argument_list|>
name|resourcesEls
init|=
name|DOMUtils
operator|.
name|getChildrenWithName
argument_list|(
name|root
argument_list|,
name|WadlGenerator
operator|.
name|WADL_NS
argument_list|,
literal|"resources"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|resourcesEls
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Element
name|resourcesEl
init|=
name|resourcesEls
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://proxy"
argument_list|,
name|resourcesEl
operator|.
name|getAttribute
argument_list|(
literal|"base"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|checkBookStoreInfo
parameter_list|(
name|Element
name|resource
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"/bookstore"
argument_list|,
name|resource
operator|.
name|getAttribute
argument_list|(
literal|"path"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|checkServletInfo
parameter_list|(
name|Element
name|resource
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"/servlet"
argument_list|,
name|resource
operator|.
name|getAttribute
argument_list|(
literal|"path"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|checkPetStoreInfo
parameter_list|(
name|Element
name|resource
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"/"
argument_list|,
name|resource
operator|.
name|getAttribute
argument_list|(
literal|"path"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|List
argument_list|<
name|Element
argument_list|>
name|getWadlResourcesInfo
parameter_list|(
name|String
name|baseURI
parameter_list|,
name|String
name|requestURI
parameter_list|,
name|int
name|size
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
name|requestURI
operator|+
literal|"?_wadl&_type=xml"
argument_list|)
decl_stmt|;
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
name|getClient
argument_list|()
operator|.
name|setReceiveTimeout
argument_list|(
literal|10000000
argument_list|)
expr_stmt|;
name|Document
name|doc
init|=
name|StaxUtils
operator|.
name|read
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|client
operator|.
name|get
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
decl_stmt|;
name|Element
name|root
init|=
name|doc
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|WadlGenerator
operator|.
name|WADL_NS
argument_list|,
name|root
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"application"
argument_list|,
name|root
operator|.
name|getLocalName
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Element
argument_list|>
name|resourcesEls
init|=
name|DOMUtils
operator|.
name|getChildrenWithName
argument_list|(
name|root
argument_list|,
name|WadlGenerator
operator|.
name|WADL_NS
argument_list|,
literal|"resources"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|resourcesEls
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Element
name|resourcesEl
init|=
name|resourcesEls
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|baseURI
argument_list|,
name|resourcesEl
operator|.
name|getAttribute
argument_list|(
literal|"base"
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Element
argument_list|>
name|resourceEls
init|=
name|DOMUtils
operator|.
name|getChildrenWithName
argument_list|(
name|resourcesEl
argument_list|,
name|WadlGenerator
operator|.
name|WADL_NS
argument_list|,
literal|"resource"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|size
argument_list|,
name|resourceEls
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|resourceEls
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testServletConfigInitParam
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/webapp/resources/servlet/config/a"
decl_stmt|;
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|endpointAddress
argument_list|)
decl_stmt|;
name|wc
operator|.
name|accept
argument_list|(
literal|"text/plain"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"avalue"
argument_list|,
name|wc
operator|.
name|get
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBook123
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/webapp/resources/bookstore/books/123"
decl_stmt|;
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|endpointAddress
argument_list|)
decl_stmt|;
name|URLConnection
name|connect
init|=
name|url
operator|.
name|openConnection
argument_list|()
decl_stmt|;
name|connect
operator|.
name|addRequestProperty
argument_list|(
literal|"Accept"
argument_list|,
literal|"application/json"
argument_list|)
expr_stmt|;
name|connect
operator|.
name|addRequestProperty
argument_list|(
literal|"Content-Language"
argument_list|,
literal|"badgerFishLanguage"
argument_list|)
expr_stmt|;
name|InputStream
name|in
init|=
name|connect
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|in
argument_list|)
expr_stmt|;
comment|//Ensure BadgerFish output as this should have replaced the standard JSONProvider
name|InputStream
name|expected
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"resources/expected_get_book123badgerfish.txt"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"BadgerFish output not correct"
argument_list|,
name|stripXmlInstructionIfNeeded
argument_list|(
name|getStringFromInputStream
argument_list|(
name|expected
argument_list|)
operator|.
name|trim
argument_list|()
argument_list|)
argument_list|,
name|stripXmlInstructionIfNeeded
argument_list|(
name|getStringFromInputStream
argument_list|(
name|in
argument_list|)
operator|.
name|trim
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|stripXmlInstructionIfNeeded
parameter_list|(
name|String
name|str
parameter_list|)
block|{
if|if
condition|(
name|str
operator|!=
literal|null
operator|&&
name|str
operator|.
name|startsWith
argument_list|(
literal|"<?xml"
argument_list|)
condition|)
block|{
name|int
name|index
init|=
name|str
operator|.
name|indexOf
argument_list|(
literal|"?>"
argument_list|)
decl_stmt|;
name|str
operator|=
name|str
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|2
argument_list|)
expr_stmt|;
block|}
return|return
name|str
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookNotFound
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/webapp/resources/bookstore/books/12345"
decl_stmt|;
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|endpointAddress
argument_list|)
decl_stmt|;
name|HttpURLConnection
name|connect
init|=
operator|(
name|HttpURLConnection
operator|)
name|url
operator|.
name|openConnection
argument_list|()
decl_stmt|;
name|connect
operator|.
name|addRequestProperty
argument_list|(
literal|"Accept"
argument_list|,
literal|"text/plain,application/xml"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|500
argument_list|,
name|connect
operator|.
name|getResponseCode
argument_list|()
argument_list|)
expr_stmt|;
name|InputStream
name|in
init|=
name|connect
operator|.
name|getErrorStream
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|InputStream
name|expected
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"resources/expected_get_book_notfound_mapped.txt"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Exception is not mapped correctly"
argument_list|,
name|stripXmlInstructionIfNeeded
argument_list|(
name|getStringFromInputStream
argument_list|(
name|expected
argument_list|)
operator|.
name|trim
argument_list|()
argument_list|)
argument_list|,
name|stripXmlInstructionIfNeeded
argument_list|(
name|getStringFromInputStream
argument_list|(
name|in
argument_list|)
operator|.
name|trim
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookNotExistent
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/webapp/resources/bookstore/nonexistent"
decl_stmt|;
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|endpointAddress
argument_list|)
decl_stmt|;
name|HttpURLConnection
name|connect
init|=
operator|(
name|HttpURLConnection
operator|)
name|url
operator|.
name|openConnection
argument_list|()
decl_stmt|;
name|connect
operator|.
name|addRequestProperty
argument_list|(
literal|"Accept"
argument_list|,
literal|"application/xml"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|405
argument_list|,
name|connect
operator|.
name|getResponseCode
argument_list|()
argument_list|)
expr_stmt|;
name|InputStream
name|in
init|=
name|connect
operator|.
name|getErrorStream
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Exception is not mapped correctly"
argument_list|,
literal|"StringTextWriter - Nonexistent method"
argument_list|,
name|getStringFromInputStream
argument_list|(
name|in
argument_list|)
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPostPetStatus
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/webapp/pets/petstore/pets"
decl_stmt|;
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|endpointAddress
argument_list|)
decl_stmt|;
name|HttpURLConnection
name|httpUrlConnection
init|=
operator|(
name|HttpURLConnection
operator|)
name|url
operator|.
name|openConnection
argument_list|()
decl_stmt|;
name|httpUrlConnection
operator|.
name|setUseCaches
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|httpUrlConnection
operator|.
name|setDefaultUseCaches
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|httpUrlConnection
operator|.
name|setDoOutput
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|httpUrlConnection
operator|.
name|setDoInput
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|httpUrlConnection
operator|.
name|setRequestMethod
argument_list|(
literal|"POST"
argument_list|)
expr_stmt|;
name|httpUrlConnection
operator|.
name|setRequestProperty
argument_list|(
literal|"Accept"
argument_list|,
literal|"text/xml"
argument_list|)
expr_stmt|;
name|httpUrlConnection
operator|.
name|setRequestProperty
argument_list|(
literal|"Content-type"
argument_list|,
literal|"application/x-www-form-urlencoded"
argument_list|)
expr_stmt|;
name|httpUrlConnection
operator|.
name|setRequestProperty
argument_list|(
literal|"Connection"
argument_list|,
literal|"close"
argument_list|)
expr_stmt|;
name|OutputStream
name|outputstream
init|=
name|httpUrlConnection
operator|.
name|getOutputStream
argument_list|()
decl_stmt|;
name|File
name|inputFile
init|=
operator|new
name|File
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"resources/singleValPostBody.txt"
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
name|byte
index|[]
name|tmp
init|=
operator|new
name|byte
index|[
literal|4096
index|]
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
try|try
init|(
name|InputStream
name|is
init|=
operator|new
name|FileInputStream
argument_list|(
name|inputFile
argument_list|)
init|)
block|{
while|while
condition|(
operator|(
name|i
operator|=
name|is
operator|.
name|read
argument_list|(
name|tmp
argument_list|)
operator|)
operator|>=
literal|0
condition|)
block|{
name|outputstream
operator|.
name|write
argument_list|(
name|tmp
argument_list|,
literal|0
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
block|}
name|outputstream
operator|.
name|flush
argument_list|()
expr_stmt|;
name|int
name|responseCode
init|=
name|httpUrlConnection
operator|.
name|getResponseCode
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|responseCode
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong status returned"
argument_list|,
literal|"open"
argument_list|,
name|getStringFromInputStream
argument_list|(
name|httpUrlConnection
operator|.
name|getInputStream
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|httpUrlConnection
operator|.
name|disconnect
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPostPetStatus2
parameter_list|()
throws|throws
name|Exception
block|{
name|Socket
name|s
init|=
operator|new
name|Socket
argument_list|(
literal|"localhost"
argument_list|,
name|Integer
operator|.
name|parseInt
argument_list|(
name|PORT
argument_list|)
argument_list|)
decl_stmt|;
name|IOUtils
operator|.
name|copyAndCloseInput
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"resources/formRequest.txt"
argument_list|)
operator|.
name|openStream
argument_list|()
argument_list|,
name|s
operator|.
name|getOutputStream
argument_list|()
argument_list|)
expr_stmt|;
name|s
operator|.
name|getOutputStream
argument_list|()
operator|.
name|flush
argument_list|()
expr_stmt|;
try|try
block|{
name|assertTrue
argument_list|(
literal|"Wrong status returned"
argument_list|,
name|getStringFromInputStream
argument_list|(
name|s
operator|.
name|getInputStream
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
literal|"open"
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|s
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|getStringFromInputStream
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|IOUtils
operator|.
name|toString
argument_list|(
name|in
argument_list|)
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPostPetStatusType
parameter_list|()
throws|throws
name|Exception
block|{
name|JAXBElementProvider
argument_list|<
name|Object
argument_list|>
name|p
init|=
operator|new
name|JAXBElementProvider
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|p
operator|.
name|setUnmarshallAsJaxbElement
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/webapp/pets/petstore/jaxb/statusType/"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|p
argument_list|)
argument_list|)
decl_stmt|;
name|WebClient
operator|.
name|getConfig
argument_list|(
name|wc
argument_list|)
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|wc
operator|.
name|accept
argument_list|(
literal|"text/xml"
argument_list|)
expr_stmt|;
name|PetStore
operator|.
name|PetStoreStatusType
name|type
init|=
name|wc
operator|.
name|get
argument_list|(
name|PetStore
operator|.
name|PetStoreStatusType
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|PetStore
operator|.
name|CLOSED
argument_list|,
name|type
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

