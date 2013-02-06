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
name|InputStream
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
name|Application
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
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamWriter
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
name|ext
operator|.
name|MessageContextImpl
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
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|resources
operator|.
name|SuperBook
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
name|staxutils
operator|.
name|StaxUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
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
name|testIsWriteable
parameter_list|()
throws|throws
name|Exception
block|{
name|XSLTJaxbProvider
argument_list|<
name|Book
argument_list|>
name|provider
init|=
operator|new
name|XSLTJaxbProvider
argument_list|<
name|Book
argument_list|>
argument_list|()
decl_stmt|;
name|provider
operator|.
name|setOutTemplate
argument_list|(
name|TEMPLATE_LOCATION
argument_list|)
expr_stmt|;
name|provider
operator|.
name|isWriteable
argument_list|(
name|Book
operator|.
name|class
argument_list|,
name|Book
operator|.
name|class
argument_list|,
literal|null
argument_list|,
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsWriteableWithSetClasses
parameter_list|()
throws|throws
name|Exception
block|{
name|XSLTJaxbProvider
argument_list|<
name|Book
argument_list|>
name|provider
init|=
operator|new
name|XSLTJaxbProvider
argument_list|<
name|Book
argument_list|>
argument_list|()
decl_stmt|;
name|provider
operator|.
name|setOutTemplate
argument_list|(
name|TEMPLATE_LOCATION
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|names
operator|.
name|add
argument_list|(
name|Book
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|provider
operator|.
name|setOutClassNames
argument_list|(
name|names
argument_list|)
expr_stmt|;
name|provider
operator|.
name|isWriteable
argument_list|(
name|Book
operator|.
name|class
argument_list|,
name|Book
operator|.
name|class
argument_list|,
literal|null
argument_list|,
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNotWriteableWithSetClasses
parameter_list|()
throws|throws
name|Exception
block|{
name|XSLTJaxbProvider
argument_list|<
name|SuperBook
argument_list|>
name|provider
init|=
operator|new
name|XSLTJaxbProvider
argument_list|<
name|SuperBook
argument_list|>
argument_list|()
decl_stmt|;
name|provider
operator|.
name|setOutTemplate
argument_list|(
name|TEMPLATE_LOCATION
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|names
operator|.
name|add
argument_list|(
name|Book
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|provider
operator|.
name|setOutClassNames
argument_list|(
name|names
argument_list|)
expr_stmt|;
name|provider
operator|.
name|isWriteable
argument_list|(
name|SuperBook
operator|.
name|class
argument_list|,
name|SuperBook
operator|.
name|class
argument_list|,
literal|null
argument_list|,
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsWriteableWithSetClassesAndJaxbOnly
parameter_list|()
throws|throws
name|Exception
block|{
name|XSLTJaxbProvider
argument_list|<
name|SuperBook
argument_list|>
name|provider
init|=
operator|new
name|XSLTJaxbProvider
argument_list|<
name|SuperBook
argument_list|>
argument_list|()
decl_stmt|;
name|provider
operator|.
name|setSupportJaxbOnly
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|provider
operator|.
name|setOutTemplate
argument_list|(
name|TEMPLATE_LOCATION
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|names
operator|.
name|add
argument_list|(
name|Book
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|provider
operator|.
name|setOutClassNames
argument_list|(
name|names
argument_list|)
expr_stmt|;
name|provider
operator|.
name|isWriteable
argument_list|(
name|SuperBook
operator|.
name|class
argument_list|,
name|SuperBook
operator|.
name|class
argument_list|,
literal|null
argument_list|,
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|)
expr_stmt|;
block|}
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
argument_list|<
name|Book
argument_list|>
name|provider
init|=
operator|new
name|XSLTJaxbProvider
argument_list|<
name|Book
argument_list|>
argument_list|()
decl_stmt|;
name|provider
operator|.
name|setOutTemplate
argument_list|(
name|TEMPLATE_LOCATION
argument_list|)
expr_stmt|;
name|provider
operator|.
name|setMessageContext
argument_list|(
operator|new
name|MessageContextImpl
argument_list|(
name|createMessage
argument_list|()
argument_list|)
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
name|Test
specifier|public
name|void
name|testWriteToStreamWriter
parameter_list|()
throws|throws
name|Exception
block|{
name|XSLTJaxbProvider
argument_list|<
name|Book
argument_list|>
name|provider
init|=
operator|new
name|XSLTJaxbProvider
argument_list|<
name|Book
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|XMLStreamWriter
name|getStreamWriter
parameter_list|(
name|Object
name|obj
parameter_list|,
name|OutputStream
name|os
parameter_list|,
name|MediaType
name|mt
parameter_list|)
block|{
return|return
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|os
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|provider
operator|.
name|setOutTemplate
argument_list|(
name|TEMPLATE_LOCATION
argument_list|)
expr_stmt|;
name|provider
operator|.
name|setMessageContext
argument_list|(
operator|new
name|MessageContextImpl
argument_list|(
name|createMessage
argument_list|()
argument_list|)
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
name|Test
specifier|public
name|void
name|testWriteWithoutTemplate
parameter_list|()
throws|throws
name|Exception
block|{
name|XSLTJaxbProvider
argument_list|<
name|Book
argument_list|>
name|provider
init|=
operator|new
name|XSLTJaxbProvider
argument_list|<
name|Book
argument_list|>
argument_list|()
decl_stmt|;
name|provider
operator|.
name|setSupportJaxbOnly
argument_list|(
literal|true
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
name|assertEquals
argument_list|(
name|b
argument_list|,
name|b2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsReadable
parameter_list|()
throws|throws
name|Exception
block|{
name|XSLTJaxbProvider
argument_list|<
name|Book
argument_list|>
name|provider
init|=
operator|new
name|XSLTJaxbProvider
argument_list|<
name|Book
argument_list|>
argument_list|()
decl_stmt|;
name|provider
operator|.
name|setInTemplate
argument_list|(
name|TEMPLATE_LOCATION
argument_list|)
expr_stmt|;
name|provider
operator|.
name|isReadable
argument_list|(
name|Book
operator|.
name|class
argument_list|,
name|Book
operator|.
name|class
argument_list|,
literal|null
argument_list|,
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsReadableWithSetClasses
parameter_list|()
throws|throws
name|Exception
block|{
name|XSLTJaxbProvider
argument_list|<
name|Book
argument_list|>
name|provider
init|=
operator|new
name|XSLTJaxbProvider
argument_list|<
name|Book
argument_list|>
argument_list|()
decl_stmt|;
name|provider
operator|.
name|setInTemplate
argument_list|(
name|TEMPLATE_LOCATION
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|names
operator|.
name|add
argument_list|(
name|Book
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|provider
operator|.
name|setInClassNames
argument_list|(
name|names
argument_list|)
expr_stmt|;
name|provider
operator|.
name|isReadable
argument_list|(
name|Book
operator|.
name|class
argument_list|,
name|Book
operator|.
name|class
argument_list|,
literal|null
argument_list|,
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNotReadableWithSetClasses
parameter_list|()
throws|throws
name|Exception
block|{
name|XSLTJaxbProvider
argument_list|<
name|SuperBook
argument_list|>
name|provider
init|=
operator|new
name|XSLTJaxbProvider
argument_list|<
name|SuperBook
argument_list|>
argument_list|()
decl_stmt|;
name|provider
operator|.
name|setInTemplate
argument_list|(
name|TEMPLATE_LOCATION
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|names
operator|.
name|add
argument_list|(
name|Book
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|provider
operator|.
name|setInClassNames
argument_list|(
name|names
argument_list|)
expr_stmt|;
name|provider
operator|.
name|isReadable
argument_list|(
name|SuperBook
operator|.
name|class
argument_list|,
name|SuperBook
operator|.
name|class
argument_list|,
literal|null
argument_list|,
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsReadableWithSetClassesAndJaxbOnly
parameter_list|()
throws|throws
name|Exception
block|{
name|XSLTJaxbProvider
argument_list|<
name|SuperBook
argument_list|>
name|provider
init|=
operator|new
name|XSLTJaxbProvider
argument_list|<
name|SuperBook
argument_list|>
argument_list|()
decl_stmt|;
name|provider
operator|.
name|setSupportJaxbOnly
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|provider
operator|.
name|setInTemplate
argument_list|(
name|TEMPLATE_LOCATION
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|names
operator|.
name|add
argument_list|(
name|Book
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|provider
operator|.
name|setInClassNames
argument_list|(
name|names
argument_list|)
expr_stmt|;
name|provider
operator|.
name|isReadable
argument_list|(
name|SuperBook
operator|.
name|class
argument_list|,
name|SuperBook
operator|.
name|class
argument_list|,
literal|null
argument_list|,
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|)
expr_stmt|;
block|}
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
argument_list|<
name|Book
argument_list|>
name|provider
init|=
operator|new
name|XSLTJaxbProvider
argument_list|<
name|Book
argument_list|>
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
name|provider
operator|.
name|readFrom
argument_list|(
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
annotation|@
name|Test
specifier|public
name|void
name|testReadFromStreamReader
parameter_list|()
throws|throws
name|Exception
block|{
name|XSLTJaxbProvider
argument_list|<
name|Book
argument_list|>
name|provider
init|=
operator|new
name|XSLTJaxbProvider
argument_list|<
name|Book
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|XMLStreamReader
name|getStreamReader
parameter_list|(
name|InputStream
name|is
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|MediaType
name|mt
parameter_list|)
block|{
return|return
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|is
argument_list|)
return|;
block|}
block|}
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
name|provider
operator|.
name|readFrom
argument_list|(
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
annotation|@
name|Test
specifier|public
name|void
name|testReadWithoutTemplate
parameter_list|()
throws|throws
name|Exception
block|{
name|XSLTJaxbProvider
argument_list|<
name|Book
argument_list|>
name|provider
init|=
operator|new
name|XSLTJaxbProvider
argument_list|<
name|Book
argument_list|>
argument_list|()
decl_stmt|;
name|provider
operator|.
name|setSupportJaxbOnly
argument_list|(
literal|true
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
name|provider
operator|.
name|readFrom
argument_list|(
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
specifier|private
name|Message
name|createMessage
parameter_list|()
block|{
name|ProviderFactory
name|factory
init|=
name|ServerProviderFactory
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|Message
name|m
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|ENDPOINT_ADDRESS
argument_list|,
literal|"http://localhost:8080/bar"
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
literal|"org.apache.cxf.http.case_insensitive_queries"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|Exchange
name|e
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|m
operator|.
name|setExchange
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|e
operator|.
name|setInMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|endpoint
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
literal|null
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|endpoint
operator|.
name|get
argument_list|(
name|Application
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|size
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|0
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|endpoint
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|endpoint
operator|.
name|get
argument_list|(
name|ServerProviderFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|factory
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|e
operator|.
name|put
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
return|return
name|m
return|;
block|}
block|}
end_class

end_unit

