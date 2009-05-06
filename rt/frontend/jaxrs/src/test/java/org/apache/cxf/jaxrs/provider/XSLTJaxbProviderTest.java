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
name|io
operator|.
name|StringReader
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|Unmarshaller
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
name|resources
operator|.
name|Book
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
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|XSLTJaxbProviderTest
extends|extends
name|Assert
block|{
specifier|private
specifier|static
specifier|final
name|String
name|TEMPLATE_LOCATION
init|=
literal|"classpath:/org/apache/cxf/jaxrs/provider/template.xsl"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|BOOK_XML
init|=
literal|"<Book><id>123</id><name>TheBook</name></Book>"
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testWrite
parameter_list|()
throws|throws
name|Exception
block|{
name|XSLTJaxbProvider
name|provider
init|=
operator|new
name|XSLTJaxbProvider
argument_list|()
decl_stmt|;
name|provider
operator|.
name|setOutTemplate
argument_list|(
name|TEMPLATE_LOCATION
argument_list|)
expr_stmt|;
name|Book
name|b
init|=
operator|new
name|Book
argument_list|()
decl_stmt|;
name|b
operator|.
name|setId
argument_list|(
literal|123L
argument_list|)
expr_stmt|;
name|b
operator|.
name|setName
argument_list|(
literal|"TheBook"
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|provider
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
name|b
operator|.
name|getClass
argument_list|()
operator|.
name|getAnnotations
argument_list|()
argument_list|,
name|MediaType
operator|.
name|TEXT_XML_TYPE
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
name|Unmarshaller
name|um
init|=
name|provider
operator|.
name|getClassContext
argument_list|(
name|Book
operator|.
name|class
argument_list|)
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
name|Book
name|b2
init|=
operator|(
name|Book
operator|)
name|um
operator|.
name|unmarshal
argument_list|(
operator|new
name|StringReader
argument_list|(
name|bos
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|b
operator|.
name|setName
argument_list|(
literal|"TheBook2"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Transformation is bad"
argument_list|,
name|b
argument_list|,
name|b2
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
name|testRead
parameter_list|()
throws|throws
name|Exception
block|{
name|XSLTJaxbProvider
name|provider
init|=
operator|new
name|XSLTJaxbProvider
argument_list|()
decl_stmt|;
name|provider
operator|.
name|setInTemplate
argument_list|(
name|TEMPLATE_LOCATION
argument_list|)
expr_stmt|;
name|Book
name|b
init|=
operator|new
name|Book
argument_list|()
decl_stmt|;
name|b
operator|.
name|setId
argument_list|(
literal|123L
argument_list|)
expr_stmt|;
name|b
operator|.
name|setName
argument_list|(
literal|"TheBook"
argument_list|)
expr_stmt|;
name|Book
name|b2
init|=
operator|(
name|Book
operator|)
name|provider
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
name|b
operator|.
name|getClass
argument_list|()
operator|.
name|getAnnotations
argument_list|()
argument_list|,
name|MediaType
operator|.
name|TEXT_XML_TYPE
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
operator|new
name|ByteArrayInputStream
argument_list|(
name|BOOK_XML
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|b
operator|.
name|setName
argument_list|(
literal|"TheBook2"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Transformation is bad"
argument_list|,
name|b
argument_list|,
name|b2
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

