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
name|provider
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
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
name|Collections
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
name|GET
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
name|POST
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
name|Path
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
name|PathParam
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
name|MediaType
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
name|aegis
operator|.
name|databinding
operator|.
name|AegisDatabinding
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
name|databinding
operator|.
name|DataBinding
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
name|jaxb
operator|.
name|JAXBDataBinding
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
name|impl
operator|.
name|MetadataMap
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
name|resources
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
name|jaxrs
operator|.
name|resources
operator|.
name|sdo
operator|.
name|Structure
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
name|resources
operator|.
name|sdo
operator|.
name|impl
operator|.
name|StructureImpl
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
name|sdo
operator|.
name|SDODataBinding
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

begin_class
specifier|public
class|class
name|DataBindingJSONProviderTest
extends|extends
name|Assert
block|{
specifier|private
name|ClassResourceInfo
name|c
decl_stmt|;
specifier|private
name|ClassResourceInfo
name|c2
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|c
operator|=
name|ResourceUtils
operator|.
name|createClassResourceInfo
argument_list|(
name|TheBooks
operator|.
name|class
argument_list|,
name|TheBooks
operator|.
name|class
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|c2
operator|=
name|ResourceUtils
operator|.
name|createClassResourceInfo
argument_list|(
name|TheSDOBooks
operator|.
name|class
argument_list|,
name|TheSDOBooks
operator|.
name|class
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Test
specifier|public
name|void
name|testSDOWrite
parameter_list|()
throws|throws
name|Exception
block|{
name|Service
name|s
init|=
operator|new
name|JAXRSServiceImpl
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|c2
argument_list|)
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|DataBinding
name|binding
init|=
operator|new
name|SDODataBinding
argument_list|()
decl_stmt|;
name|binding
operator|.
name|initialize
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|DataBindingJSONProvider
name|p
init|=
operator|new
name|DataBindingJSONProvider
argument_list|()
decl_stmt|;
name|p
operator|.
name|setDataBinding
argument_list|(
name|binding
argument_list|)
expr_stmt|;
name|p
operator|.
name|setNamespaceMap
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"http://apache.org/structure/types"
argument_list|,
literal|"p0"
argument_list|)
argument_list|)
expr_stmt|;
name|Structure
name|struct
init|=
operator|new
name|StructureImpl
argument_list|()
decl_stmt|;
name|struct
operator|.
name|getTexts
argument_list|()
operator|.
name|add
argument_list|(
literal|"text1"
argument_list|)
expr_stmt|;
name|struct
operator|.
name|setText
argument_list|(
literal|"sdo"
argument_list|)
expr_stmt|;
name|struct
operator|.
name|setInt
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|struct
operator|.
name|setDbl
argument_list|(
literal|123.5
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|p
operator|.
name|writeTo
argument_list|(
name|struct
argument_list|,
name|Structure
operator|.
name|class
argument_list|,
name|Structure
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[
literal|0
index|]
argument_list|,
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|,
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|String
name|data
init|=
literal|"{\"p0.Structure\":{\"@xsi.type\":\"p0:Structure\",\"p0.text\":\"sdo\",\"p0.int\":3"
operator|+
literal|",\"p0.dbl\":123.5,\"p0.texts\":\"text1\"}}"
decl_stmt|;
name|assertEquals
argument_list|(
name|bos
operator|.
name|toString
argument_list|()
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Test
specifier|public
name|void
name|testSDORead
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|data
init|=
literal|"{\"p0.Structure\":{\"@xsi.type\":\"p0:Structure\",\"p0.text\":\"sdo\",\"p0.int\":3"
operator|+
literal|",\"p0.dbl\":123.5,\"p0.texts\":\"text1\"}}"
decl_stmt|;
name|Service
name|s
init|=
operator|new
name|JAXRSServiceImpl
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|c2
argument_list|)
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|DataBinding
name|binding
init|=
operator|new
name|SDODataBinding
argument_list|()
decl_stmt|;
name|binding
operator|.
name|initialize
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|DataBindingJSONProvider
name|p
init|=
operator|new
name|DataBindingJSONProvider
argument_list|()
decl_stmt|;
name|p
operator|.
name|setDataBinding
argument_list|(
name|binding
argument_list|)
expr_stmt|;
name|p
operator|.
name|setNamespaceMap
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"http://apache.org/structure/types"
argument_list|,
literal|"p0"
argument_list|)
argument_list|)
expr_stmt|;
name|ByteArrayInputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|Structure
name|struct
init|=
operator|(
name|Structure
operator|)
name|p
operator|.
name|readFrom
argument_list|(
operator|(
name|Class
operator|)
name|Structure
operator|.
name|class
argument_list|,
name|Structure
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[
literal|0
index|]
argument_list|,
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|,
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|,
name|is
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"sdo"
argument_list|,
name|struct
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|123.5
argument_list|,
name|struct
operator|.
name|getDbl
argument_list|()
argument_list|,
literal|0.01
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|struct
operator|.
name|getInt
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJAXBWrite
parameter_list|()
throws|throws
name|Exception
block|{
name|Service
name|s
init|=
operator|new
name|JAXRSServiceImpl
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|c
argument_list|)
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|DataBinding
name|binding
init|=
operator|new
name|JAXBDataBinding
argument_list|()
decl_stmt|;
name|binding
operator|.
name|initialize
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|DataBindingJSONProvider
argument_list|<
name|Book
argument_list|>
name|p
init|=
operator|new
name|DataBindingJSONProvider
argument_list|<
name|Book
argument_list|>
argument_list|()
decl_stmt|;
name|p
operator|.
name|setDataBinding
argument_list|(
name|binding
argument_list|)
expr_stmt|;
name|Book
name|b
init|=
operator|new
name|Book
argument_list|(
literal|"CXF"
argument_list|,
literal|127L
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|p
operator|.
name|writeTo
argument_list|(
name|b
argument_list|,
name|Book
operator|.
name|class
argument_list|,
name|Book
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[
literal|0
index|]
argument_list|,
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|,
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|String
name|data
init|=
literal|"{\"Book\":{\"id\":127,\"name\":\"CXF\",\"state\":\"\"}}"
decl_stmt|;
name|assertEquals
argument_list|(
name|bos
operator|.
name|toString
argument_list|()
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Test
specifier|public
name|void
name|testJAXBRead
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|data
init|=
literal|"{\"Book\":{\"id\":127,\"name\":\"CXF\",\"state\":\"\"}}"
decl_stmt|;
name|Service
name|s
init|=
operator|new
name|JAXRSServiceImpl
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|c
argument_list|)
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|DataBinding
name|binding
init|=
operator|new
name|JAXBDataBinding
argument_list|()
decl_stmt|;
name|binding
operator|.
name|initialize
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|DataBindingJSONProvider
name|p
init|=
operator|new
name|DataBindingJSONProvider
argument_list|()
decl_stmt|;
name|p
operator|.
name|setDataBinding
argument_list|(
name|binding
argument_list|)
expr_stmt|;
name|ByteArrayInputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|Book
name|book
init|=
operator|(
name|Book
operator|)
name|p
operator|.
name|readFrom
argument_list|(
operator|(
name|Class
operator|)
name|Book
operator|.
name|class
argument_list|,
name|Book
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[
literal|0
index|]
argument_list|,
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|,
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|,
name|is
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"CXF"
argument_list|,
name|book
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|127L
argument_list|,
name|book
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
name|testAegisWrite
parameter_list|()
throws|throws
name|Exception
block|{
name|Service
name|s
init|=
operator|new
name|JAXRSServiceImpl
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|c
argument_list|)
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|s
operator|.
name|put
argument_list|(
literal|"writeXsiType"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|AegisDatabinding
name|binding
init|=
operator|new
name|AegisDatabinding
argument_list|()
decl_stmt|;
name|binding
operator|.
name|initialize
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|DataBindingJSONProvider
argument_list|<
name|Book
argument_list|>
name|p
init|=
operator|new
name|DataBindingJSONProvider
argument_list|<
name|Book
argument_list|>
argument_list|()
decl_stmt|;
name|p
operator|.
name|setDataBinding
argument_list|(
name|binding
argument_list|)
expr_stmt|;
name|Book
name|b
init|=
operator|new
name|Book
argument_list|(
literal|"CXF"
argument_list|,
literal|127L
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|p
operator|.
name|writeTo
argument_list|(
name|b
argument_list|,
name|Book
operator|.
name|class
argument_list|,
name|Book
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[
literal|0
index|]
argument_list|,
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|,
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|doTestAegisRead
argument_list|(
name|bos
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Ignore
specifier|public
name|void
name|testAegisCollectionWrite
parameter_list|()
throws|throws
name|Exception
block|{
name|Service
name|s
init|=
operator|new
name|JAXRSServiceImpl
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|c
argument_list|)
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|s
operator|.
name|put
argument_list|(
literal|"writeXsiType"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|AegisDatabinding
name|binding
init|=
operator|new
name|AegisDatabinding
argument_list|()
decl_stmt|;
name|binding
operator|.
name|initialize
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|DataBindingJSONProvider
argument_list|<
name|List
argument_list|<
name|Book
argument_list|>
argument_list|>
name|p
init|=
operator|new
name|DataBindingJSONProvider
argument_list|<
name|List
argument_list|<
name|Book
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|p
operator|.
name|setDataBinding
argument_list|(
name|binding
argument_list|)
expr_stmt|;
name|Book
name|b
init|=
operator|new
name|Book
argument_list|(
literal|"CXF"
argument_list|,
literal|127L
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Book
argument_list|>
name|books
init|=
operator|new
name|ArrayList
argument_list|<
name|Book
argument_list|>
argument_list|()
decl_stmt|;
name|books
operator|.
name|add
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|p
operator|.
name|writeTo
argument_list|(
name|books
argument_list|,
name|List
operator|.
name|class
argument_list|,
name|Book
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[
literal|0
index|]
argument_list|,
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|,
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|bos
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAegisRead
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|data
init|=
literal|"{\"ns1.Book\":{\"@xsi.type\":\"ns1:Book\",\"ns1.id\":127,"
operator|+
literal|"\"ns1.name\":\"CXF\",\"ns1.state\":\"\"}}"
decl_stmt|;
name|doTestAegisRead
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|void
name|doTestAegisRead
parameter_list|(
name|String
name|data
parameter_list|)
throws|throws
name|Exception
block|{
name|Service
name|s
init|=
operator|new
name|JAXRSServiceImpl
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|c
argument_list|)
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|s
operator|.
name|put
argument_list|(
literal|"readXsiType"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|AegisDatabinding
name|binding
init|=
operator|new
name|AegisDatabinding
argument_list|()
decl_stmt|;
name|binding
operator|.
name|initialize
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|DataBindingJSONProvider
name|p
init|=
operator|new
name|DataBindingJSONProvider
argument_list|()
decl_stmt|;
name|p
operator|.
name|setDataBinding
argument_list|(
name|binding
argument_list|)
expr_stmt|;
name|ByteArrayInputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|Book
name|book
init|=
operator|(
name|Book
operator|)
name|p
operator|.
name|readFrom
argument_list|(
operator|(
name|Class
operator|)
name|Book
operator|.
name|class
argument_list|,
name|Book
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[
literal|0
index|]
argument_list|,
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|,
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|,
name|is
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"CXF"
argument_list|,
name|book
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|127L
argument_list|,
name|book
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Path
argument_list|(
literal|"/"
argument_list|)
annotation|@
name|Ignore
specifier|public
specifier|static
class|class
name|TheBooks
block|{
annotation|@
name|Path
argument_list|(
literal|"/books/{bookId}/{new}"
argument_list|)
specifier|public
name|Book
name|getNewBook
parameter_list|(
name|Book
name|b
parameter_list|)
block|{
return|return
operator|new
name|Book
argument_list|()
return|;
block|}
annotation|@
name|Path
argument_list|(
literal|"/books/{bookId}/{new}"
argument_list|)
specifier|public
name|Book
name|getNewBook2
parameter_list|()
block|{
return|return
operator|new
name|Book
argument_list|()
return|;
block|}
comment|//        @Path("/books/{bookId}/{new}")
comment|//        public List<Book> getNewBook3() {
comment|//            return null;
comment|//        }
annotation|@
name|POST
specifier|public
name|void
name|setNewBook
parameter_list|(
name|Book
name|b
parameter_list|)
block|{         }
annotation|@
name|Path
argument_list|(
literal|"/books/{bookId}/{new}"
argument_list|)
annotation|@
name|POST
specifier|public
name|void
name|setNewBook2
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"new"
argument_list|)
name|String
name|id
parameter_list|,
name|Book
name|b
parameter_list|)
block|{         }
block|}
annotation|@
name|Path
argument_list|(
literal|"/"
argument_list|)
annotation|@
name|Ignore
specifier|public
specifier|static
class|class
name|TheSDOBooks
block|{
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/books/{bookId}/{new}"
argument_list|)
specifier|public
name|Structure
name|getStructure
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

