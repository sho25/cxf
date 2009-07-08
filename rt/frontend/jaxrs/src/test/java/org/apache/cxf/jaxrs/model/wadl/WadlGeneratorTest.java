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
name|jaxrs
operator|.
name|model
operator|.
name|wadl
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
name|FileOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|HttpHeaders
import|;
end_import

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
name|Response
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
name|common
operator|.
name|xmlschema
operator|.
name|XmlSchemaConstants
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
name|jaxrs
operator|.
name|JAXRSServiceImpl
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
name|ClassResourceInfo
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
name|utils
operator|.
name|ResourceUtils
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
name|message
operator|.
name|Exchange
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
name|message
operator|.
name|ExchangeImpl
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
name|message
operator|.
name|Message
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
name|message
operator|.
name|MessageImpl
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
name|servlet
operator|.
name|ServletDestination
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|classextension
operator|.
name|EasyMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|classextension
operator|.
name|IMocksControl
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
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|WadlGeneratorTest
extends|extends
name|Assert
block|{
specifier|private
name|IMocksControl
name|control
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|control
operator|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
expr_stmt|;
name|control
operator|.
name|makeThreadSafe
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoWadl
parameter_list|()
block|{
name|WadlGenerator
name|wg
init|=
operator|new
name|WadlGenerator
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|wg
operator|.
name|handleRequest
argument_list|(
operator|new
name|MessageImpl
argument_list|()
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSingleRootResource
parameter_list|()
throws|throws
name|Exception
block|{
name|WadlGenerator
name|wg
init|=
operator|new
name|WadlGenerator
argument_list|()
decl_stmt|;
name|ClassResourceInfo
name|cri
init|=
name|ResourceUtils
operator|.
name|createClassResourceInfo
argument_list|(
name|BookStore
operator|.
name|class
argument_list|,
name|BookStore
operator|.
name|class
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Message
name|m
init|=
name|mockMessage
argument_list|(
literal|"http://localhost:8080/baz"
argument_list|,
literal|"/bar"
argument_list|,
name|WadlGenerator
operator|.
name|WADL_QUERY
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Response
name|r
init|=
name|wg
operator|.
name|handleRequest
argument_list|(
name|m
argument_list|,
name|cri
argument_list|)
decl_stmt|;
name|checkResponse
argument_list|(
name|r
argument_list|)
expr_stmt|;
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|readXml
argument_list|(
operator|new
name|StringReader
argument_list|(
name|r
operator|.
name|getEntity
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Element
argument_list|>
name|els
init|=
name|getWadlResourcesInfo
argument_list|(
name|doc
argument_list|,
literal|"http://localhost:8080/baz"
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|checkBookStoreInfo
argument_list|(
name|els
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
name|checkResponse
parameter_list|(
name|Response
name|r
parameter_list|)
throws|throws
name|Exception
block|{
name|assertNotNull
argument_list|(
name|r
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|WadlGenerator
operator|.
name|WADL_TYPE
operator|.
name|toString
argument_list|()
argument_list|,
name|r
operator|.
name|getMetadata
argument_list|()
operator|.
name|getFirst
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|File
name|f
init|=
operator|new
name|File
argument_list|(
literal|"test.xml"
argument_list|)
decl_stmt|;
name|f
operator|.
name|delete
argument_list|()
expr_stmt|;
name|f
operator|.
name|createNewFile
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|f
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
name|f
argument_list|)
decl_stmt|;
name|fos
operator|.
name|write
argument_list|(
name|r
operator|.
name|getEntity
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|fos
operator|.
name|flush
argument_list|()
expr_stmt|;
name|fos
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMultipleRootResources
parameter_list|()
throws|throws
name|Exception
block|{
name|WadlGenerator
name|wg
init|=
operator|new
name|WadlGenerator
argument_list|()
decl_stmt|;
name|ClassResourceInfo
name|cri1
init|=
name|ResourceUtils
operator|.
name|createClassResourceInfo
argument_list|(
name|BookStore
operator|.
name|class
argument_list|,
name|BookStore
operator|.
name|class
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|ClassResourceInfo
name|cri2
init|=
name|ResourceUtils
operator|.
name|createClassResourceInfo
argument_list|(
name|Orders
operator|.
name|class
argument_list|,
name|Orders
operator|.
name|class
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ClassResourceInfo
argument_list|>
name|cris
init|=
operator|new
name|ArrayList
argument_list|<
name|ClassResourceInfo
argument_list|>
argument_list|()
decl_stmt|;
name|cris
operator|.
name|add
argument_list|(
name|cri1
argument_list|)
expr_stmt|;
name|cris
operator|.
name|add
argument_list|(
name|cri2
argument_list|)
expr_stmt|;
name|Message
name|m
init|=
name|mockMessage
argument_list|(
literal|"http://localhost:8080/baz"
argument_list|,
literal|"/bar"
argument_list|,
name|WadlGenerator
operator|.
name|WADL_QUERY
argument_list|,
name|cris
argument_list|)
decl_stmt|;
name|Response
name|r
init|=
name|wg
operator|.
name|handleRequest
argument_list|(
name|m
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|checkResponse
argument_list|(
name|r
argument_list|)
expr_stmt|;
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|readXml
argument_list|(
operator|new
name|StringReader
argument_list|(
name|r
operator|.
name|getEntity
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|checkGrammars
argument_list|(
name|doc
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Element
argument_list|>
name|els
init|=
name|getWadlResourcesInfo
argument_list|(
name|doc
argument_list|,
literal|"http://localhost:8080/baz"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|checkBookStoreInfo
argument_list|(
name|els
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|Element
name|orderResource
init|=
name|els
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"/orders"
argument_list|,
name|orderResource
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
name|checkGrammars
parameter_list|(
name|Element
name|appElement
parameter_list|)
block|{
name|List
argument_list|<
name|Element
argument_list|>
name|grammarEls
init|=
name|DOMUtils
operator|.
name|getChildrenWithName
argument_list|(
name|appElement
argument_list|,
name|WadlGenerator
operator|.
name|WADL_NS
argument_list|,
literal|"grammars"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|grammarEls
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Element
argument_list|>
name|schemasEls
init|=
name|DOMUtils
operator|.
name|getChildrenWithName
argument_list|(
name|grammarEls
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|XmlSchemaConstants
operator|.
name|XSD_NAMESPACE_URI
argument_list|,
literal|"schema"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|schemasEls
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://superbooks"
argument_list|,
name|schemasEls
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getAttribute
argument_list|(
literal|"targetNamespace"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|DOMUtils
operator|.
name|getChildrenWithName
argument_list|(
name|schemasEls
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|XmlSchemaConstants
operator|.
name|XSD_NAMESPACE_URI
argument_list|,
literal|"element"
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|DOMUtils
operator|.
name|getChildrenWithName
argument_list|(
name|schemasEls
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|XmlSchemaConstants
operator|.
name|XSD_NAMESPACE_URI
argument_list|,
literal|"complexType"
argument_list|)
operator|.
name|size
argument_list|()
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
literal|"/bookstore/{id}"
argument_list|,
name|resource
operator|.
name|getAttribute
argument_list|(
literal|"path"
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
name|resource
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
literal|4
argument_list|,
name|resourceEls
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/"
argument_list|,
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
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/books/{bookid}"
argument_list|,
name|resourceEls
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getAttribute
argument_list|(
literal|"path"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/chapter"
argument_list|,
name|resourceEls
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getAttribute
argument_list|(
literal|"path"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/booksubresource"
argument_list|,
name|resourceEls
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|getAttribute
argument_list|(
literal|"path"
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Element
argument_list|>
name|methodEls
init|=
name|DOMUtils
operator|.
name|getChildrenWithName
argument_list|(
name|resourceEls
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|WadlGenerator
operator|.
name|WADL_NS
argument_list|,
literal|"method"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|methodEls
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"GET"
argument_list|,
name|methodEls
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getAttribute
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Element
argument_list|>
name|paramsEls
init|=
name|DOMUtils
operator|.
name|getChildrenWithName
argument_list|(
name|resourceEls
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
name|WadlGenerator
operator|.
name|WADL_NS
argument_list|,
literal|"param"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|paramsEls
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|checkParameter
argument_list|(
name|paramsEls
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
literal|"id"
argument_list|,
literal|"template"
argument_list|)
expr_stmt|;
name|checkParameter
argument_list|(
name|paramsEls
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
literal|"bookid"
argument_list|,
literal|"template"
argument_list|)
expr_stmt|;
name|checkParameter
argument_list|(
name|paramsEls
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|,
literal|"mid"
argument_list|,
literal|"matrix"
argument_list|)
expr_stmt|;
name|methodEls
operator|=
name|DOMUtils
operator|.
name|getChildrenWithName
argument_list|(
name|resourceEls
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
name|WadlGenerator
operator|.
name|WADL_NS
argument_list|,
literal|"method"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|methodEls
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"POST"
argument_list|,
name|methodEls
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getAttribute
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Element
argument_list|>
name|requestEls
init|=
name|DOMUtils
operator|.
name|getChildrenWithName
argument_list|(
name|methodEls
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|WadlGenerator
operator|.
name|WADL_NS
argument_list|,
literal|"request"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|requestEls
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Element
argument_list|>
name|repEls
init|=
name|DOMUtils
operator|.
name|getChildrenWithName
argument_list|(
name|requestEls
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|WadlGenerator
operator|.
name|WADL_NS
argument_list|,
literal|"representation"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|repEls
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"application/xml"
argument_list|,
name|repEls
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getAttribute
argument_list|(
literal|"mediaType"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"prefix1:thebook"
argument_list|,
name|repEls
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getAttribute
argument_list|(
literal|"element"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"application/json"
argument_list|,
name|repEls
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getAttribute
argument_list|(
literal|"mediaType"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|repEls
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getAttribute
argument_list|(
literal|"element"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|checkParameter
parameter_list|(
name|Element
name|paramEl
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|type
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|name
argument_list|,
name|paramEl
operator|.
name|getAttribute
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|type
argument_list|,
name|paramEl
operator|.
name|getAttribute
argument_list|(
literal|"style"
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
name|Document
name|doc
parameter_list|,
name|String
name|baseURI
parameter_list|,
name|int
name|size
parameter_list|)
throws|throws
name|Exception
block|{
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
specifier|private
name|Message
name|mockMessage
parameter_list|(
name|String
name|baseAddress
parameter_list|,
name|String
name|pathInfo
parameter_list|,
name|String
name|query
parameter_list|,
name|List
argument_list|<
name|ClassResourceInfo
argument_list|>
name|cris
parameter_list|)
block|{
name|Message
name|m
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|Exchange
name|e
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|e
operator|.
name|put
argument_list|(
name|Service
operator|.
name|class
argument_list|,
operator|new
name|JAXRSServiceImpl
argument_list|(
name|cris
argument_list|)
argument_list|)
expr_stmt|;
name|m
operator|.
name|setExchange
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|control
operator|.
name|reset
argument_list|()
expr_stmt|;
name|ServletDestination
name|d
init|=
name|control
operator|.
name|createMock
argument_list|(
name|ServletDestination
operator|.
name|class
argument_list|)
decl_stmt|;
name|EndpointInfo
name|epr
init|=
operator|new
name|EndpointInfo
argument_list|()
decl_stmt|;
name|epr
operator|.
name|setAddress
argument_list|(
name|baseAddress
argument_list|)
expr_stmt|;
name|d
operator|.
name|getEndpointInfo
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|epr
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|e
operator|.
name|setDestination
argument_list|(
name|d
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|REQUEST_URI
argument_list|,
name|pathInfo
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|QUERY_STRING
argument_list|,
name|query
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|,
literal|"GET"
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
return|return
name|m
return|;
block|}
block|}
end_class

end_unit

