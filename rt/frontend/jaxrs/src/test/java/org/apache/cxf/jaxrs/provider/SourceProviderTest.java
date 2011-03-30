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
name|util
operator|.
name|Collections
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
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|MessageBodyReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilderFactory
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
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|sax
operator|.
name|SAXSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamResult
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
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
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|ext
operator|.
name|MessageContext
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
name|StaxSource
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
name|staxutils
operator|.
name|transform
operator|.
name|InTransformReader
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
name|SourceProviderTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testIsWriteable
parameter_list|()
block|{
name|SourceProvider
name|p
init|=
operator|new
name|SourceProvider
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|p
operator|.
name|isWriteable
argument_list|(
name|StreamSource
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
operator|&&
name|p
operator|.
name|isWriteable
argument_list|(
name|DOMSource
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
operator|&&
name|p
operator|.
name|isWriteable
argument_list|(
name|Source
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
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
name|testIsReadable
parameter_list|()
block|{
name|SourceProvider
name|p
init|=
operator|new
name|SourceProvider
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|p
operator|.
name|isReadable
argument_list|(
name|StreamSource
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
operator|&&
name|p
operator|.
name|isReadable
argument_list|(
name|DOMSource
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
operator|&&
name|p
operator|.
name|isReadable
argument_list|(
name|Source
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
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
name|testReadFrom
parameter_list|()
throws|throws
name|Exception
block|{
name|SourceProvider
name|p
init|=
operator|new
name|TestSourceProvider
argument_list|()
decl_stmt|;
name|assertSame
argument_list|(
name|StreamSource
operator|.
name|class
argument_list|,
name|verifyRead
argument_list|(
name|p
argument_list|,
name|StreamSource
operator|.
name|class
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|StreamSource
operator|.
name|class
argument_list|,
name|verifyRead
argument_list|(
name|p
argument_list|,
name|Source
operator|.
name|class
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|StaxSource
operator|.
name|class
argument_list|,
name|verifyRead
argument_list|(
name|p
argument_list|,
name|SAXSource
operator|.
name|class
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|StaxSource
operator|.
name|class
argument_list|,
name|verifyRead
argument_list|(
name|p
argument_list|,
name|StaxSource
operator|.
name|class
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|DOMSource
operator|.
name|class
argument_list|,
name|verifyRead
argument_list|(
name|p
argument_list|,
name|DOMSource
operator|.
name|class
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Document
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|verifyRead
argument_list|(
name|p
argument_list|,
name|Document
operator|.
name|class
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|)
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
name|testReadFromStreamReader
parameter_list|()
throws|throws
name|Exception
block|{
name|TestSourceProvider
name|p
init|=
operator|new
name|TestSourceProvider
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"<test xmlns=\"http://bar\"/>"
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|reader
operator|=
operator|new
name|InTransformReader
argument_list|(
name|reader
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"{http://bar}test"
argument_list|,
literal|"test2"
argument_list|)
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|p
operator|.
name|getMessage
argument_list|()
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|reader
argument_list|)
expr_stmt|;
name|Source
name|source
init|=
operator|(
name|Source
operator|)
name|p
operator|.
name|readFrom
argument_list|(
operator|(
name|Class
operator|)
name|Source
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|is
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|TransformerFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newTransformer
argument_list|()
operator|.
name|transform
argument_list|(
name|source
argument_list|,
operator|new
name|StreamResult
argument_list|(
name|bos
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|bos
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
literal|"test2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadFromWithPreferredFormat
parameter_list|()
throws|throws
name|Exception
block|{
name|TestSourceProvider
name|p
init|=
operator|new
name|TestSourceProvider
argument_list|()
decl_stmt|;
name|p
operator|.
name|getMessage
argument_list|()
operator|.
name|put
argument_list|(
literal|"source-preferred-format"
argument_list|,
literal|"sax"
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|StaxSource
operator|.
name|class
argument_list|,
name|verifyRead
argument_list|(
name|p
argument_list|,
name|Source
operator|.
name|class
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWriteTo
parameter_list|()
throws|throws
name|Exception
block|{
name|SourceProvider
name|p
init|=
operator|new
name|TestSourceProvider
argument_list|()
decl_stmt|;
name|StreamSource
name|s
init|=
operator|new
name|StreamSource
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"<test/>"
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|os
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|p
operator|.
name|writeTo
argument_list|(
name|s
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
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
name|Object
argument_list|>
argument_list|()
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|os
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
literal|"<test/>"
argument_list|)
argument_list|)
expr_stmt|;
name|os
operator|=
operator|new
name|ByteArrayOutputStream
argument_list|()
expr_stmt|;
name|p
operator|.
name|writeTo
argument_list|(
name|createDomSource
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
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
name|Object
argument_list|>
argument_list|()
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|os
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
literal|"<test/>"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|T
name|verifyRead
parameter_list|(
name|MessageBodyReader
name|p
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|(
name|T
operator|)
name|p
operator|.
name|readFrom
argument_list|(
name|type
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"<test/>"
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|DOMSource
name|createDomSource
parameter_list|()
throws|throws
name|Exception
block|{
name|DocumentBuilderFactory
name|factory
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|DocumentBuilder
name|builder
decl_stmt|;
name|builder
operator|=
name|factory
operator|.
name|newDocumentBuilder
argument_list|()
expr_stmt|;
return|return
operator|new
name|DOMSource
argument_list|(
name|builder
operator|.
name|parse
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"<test/>"
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
specifier|private
specifier|static
class|class
name|TestSourceProvider
extends|extends
name|SourceProvider
block|{
specifier|private
name|Message
name|m
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
specifier|public
name|TestSourceProvider
parameter_list|()
block|{         }
specifier|public
name|Message
name|getMessage
parameter_list|()
block|{
return|return
name|m
return|;
block|}
specifier|protected
name|MessageContext
name|getContext
parameter_list|()
block|{
return|return
operator|new
name|MessageContextImpl
argument_list|(
name|m
argument_list|)
return|;
block|}
empty_stmt|;
block|}
block|}
end_class

end_unit

