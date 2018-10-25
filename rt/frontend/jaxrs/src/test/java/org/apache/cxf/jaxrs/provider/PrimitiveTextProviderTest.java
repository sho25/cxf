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
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|core
operator|.
name|MultivaluedMap
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
name|NoContentException
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
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|MessageBodyWriter
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
name|PrimitiveTextProviderTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testNumberIsWriteable
parameter_list|()
block|{
name|MessageBodyWriter
argument_list|<
name|Object
argument_list|>
name|p
init|=
operator|new
name|PrimitiveTextProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|p
operator|.
name|isWriteable
argument_list|(
name|Byte
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|MediaType
operator|.
name|TEXT_PLAIN_TYPE
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNumberIsNotWriteable
parameter_list|()
block|{
name|MessageBodyWriter
argument_list|<
name|Object
argument_list|>
name|p
init|=
operator|new
name|PrimitiveTextProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|p
operator|.
name|isWriteable
argument_list|(
name|Byte
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|MediaType
operator|.
name|valueOf
argument_list|(
literal|"text/custom"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBooleanIsWriteable
parameter_list|()
block|{
name|MessageBodyWriter
argument_list|<
name|Object
argument_list|>
name|p
init|=
operator|new
name|PrimitiveTextProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|p
operator|.
name|isWriteable
argument_list|(
name|Boolean
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|MediaType
operator|.
name|TEXT_PLAIN_TYPE
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBooleanIsNotWriteable
parameter_list|()
block|{
name|MessageBodyWriter
argument_list|<
name|Object
argument_list|>
name|p
init|=
operator|new
name|PrimitiveTextProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|p
operator|.
name|isWriteable
argument_list|(
name|Boolean
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|MediaType
operator|.
name|valueOf
argument_list|(
literal|"text/custom"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCharacterIsWriteable
parameter_list|()
block|{
name|MessageBodyWriter
argument_list|<
name|Object
argument_list|>
name|p
init|=
operator|new
name|PrimitiveTextProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|p
operator|.
name|isWriteable
argument_list|(
name|Character
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|MediaType
operator|.
name|TEXT_PLAIN_TYPE
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCharacterIsNotWriteable
parameter_list|()
block|{
name|MessageBodyWriter
argument_list|<
name|Object
argument_list|>
name|p
init|=
operator|new
name|PrimitiveTextProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|p
operator|.
name|isWriteable
argument_list|(
name|Character
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|MediaType
operator|.
name|valueOf
argument_list|(
literal|"text/custom"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStringIsWriteable
parameter_list|()
block|{
name|MessageBodyWriter
argument_list|<
name|Object
argument_list|>
name|p
init|=
operator|new
name|PrimitiveTextProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|p
operator|.
name|isWriteable
argument_list|(
name|String
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|MediaType
operator|.
name|TEXT_PLAIN_TYPE
argument_list|)
operator|&&
name|p
operator|.
name|isWriteable
argument_list|(
name|String
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|MediaType
operator|.
name|valueOf
argument_list|(
literal|"text/custom"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNumberIsReadable
parameter_list|()
block|{
name|MessageBodyReader
argument_list|<
name|Object
argument_list|>
name|p
init|=
operator|new
name|PrimitiveTextProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|p
operator|.
name|isReadable
argument_list|(
name|Byte
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|MediaType
operator|.
name|TEXT_PLAIN_TYPE
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNumberIsNotReadable
parameter_list|()
block|{
name|MessageBodyReader
argument_list|<
name|Object
argument_list|>
name|p
init|=
operator|new
name|PrimitiveTextProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|p
operator|.
name|isReadable
argument_list|(
name|Byte
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|MediaType
operator|.
name|valueOf
argument_list|(
literal|"text/custom"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBooleanIsReadable
parameter_list|()
block|{
name|MessageBodyReader
argument_list|<
name|Object
argument_list|>
name|p
init|=
operator|new
name|PrimitiveTextProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|p
operator|.
name|isReadable
argument_list|(
name|Boolean
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|MediaType
operator|.
name|TEXT_PLAIN_TYPE
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBooleanIsNotReadable
parameter_list|()
block|{
name|MessageBodyReader
argument_list|<
name|Object
argument_list|>
name|p
init|=
operator|new
name|PrimitiveTextProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|p
operator|.
name|isReadable
argument_list|(
name|Boolean
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|MediaType
operator|.
name|valueOf
argument_list|(
literal|"text/custom"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCharacterIsReadable
parameter_list|()
block|{
name|MessageBodyReader
argument_list|<
name|Object
argument_list|>
name|p
init|=
operator|new
name|PrimitiveTextProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|p
operator|.
name|isReadable
argument_list|(
name|Character
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|MediaType
operator|.
name|TEXT_PLAIN_TYPE
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCharacterIsNotReadable
parameter_list|()
block|{
name|MessageBodyReader
argument_list|<
name|Object
argument_list|>
name|p
init|=
operator|new
name|PrimitiveTextProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|p
operator|.
name|isReadable
argument_list|(
name|Character
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|MediaType
operator|.
name|valueOf
argument_list|(
literal|"text/custom"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStringIsReadable
parameter_list|()
block|{
name|MessageBodyReader
argument_list|<
name|Object
argument_list|>
name|p
init|=
operator|new
name|PrimitiveTextProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|p
operator|.
name|isReadable
argument_list|(
name|String
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|MediaType
operator|.
name|TEXT_PLAIN_TYPE
argument_list|)
operator|&&
name|p
operator|.
name|isReadable
argument_list|(
name|String
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|MediaType
operator|.
name|valueOf
argument_list|(
literal|"text/custom"
argument_list|)
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
name|testReadByte
parameter_list|()
throws|throws
name|Exception
block|{
name|MessageBodyReader
argument_list|<
name|?
argument_list|>
name|p
init|=
operator|new
name|PrimitiveTextProvider
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
name|Byte
name|valueRead
init|=
operator|(
name|Byte
operator|)
name|p
operator|.
name|readFrom
argument_list|(
operator|(
name|Class
operator|)
name|Byte
operator|.
name|TYPE
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
literal|"1"
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|valueRead
operator|.
name|byteValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|NoContentException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testReadEmptyByte
parameter_list|()
throws|throws
name|Exception
block|{
name|MessageBodyReader
argument_list|<
name|?
argument_list|>
name|p
init|=
operator|new
name|PrimitiveTextProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|p
operator|.
name|readFrom
argument_list|(
operator|(
name|Class
operator|)
name|Byte
operator|.
name|TYPE
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
literal|""
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
annotation|@
name|Test
specifier|public
name|void
name|testReadBoolean
parameter_list|()
throws|throws
name|Exception
block|{
name|MessageBodyReader
name|p
init|=
operator|new
name|PrimitiveTextProvider
argument_list|()
decl_stmt|;
name|boolean
name|valueRead
init|=
operator|(
name|Boolean
operator|)
name|p
operator|.
name|readFrom
argument_list|(
name|Boolean
operator|.
name|TYPE
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
literal|"true"
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|valueRead
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
annotation|@
name|Test
specifier|public
name|void
name|testWriteBoolean
parameter_list|()
throws|throws
name|Exception
block|{
name|MessageBodyWriter
name|p
init|=
operator|new
name|PrimitiveTextProvider
argument_list|()
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
name|Boolean
operator|.
name|TRUE
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|MediaType
operator|.
name|TEXT_PLAIN_TYPE
argument_list|,
literal|null
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
operator|new
name|String
argument_list|(
literal|"true"
argument_list|)
operator|.
name|getBytes
argument_list|()
argument_list|,
name|os
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|os
operator|=
operator|new
name|ByteArrayOutputStream
argument_list|()
expr_stmt|;
specifier|final
name|boolean
name|value
init|=
literal|true
decl_stmt|;
name|p
operator|.
name|writeTo
argument_list|(
name|value
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|MediaType
operator|.
name|TEXT_PLAIN_TYPE
argument_list|,
literal|null
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
operator|new
name|String
argument_list|(
literal|"true"
argument_list|)
operator|.
name|getBytes
argument_list|()
argument_list|,
name|os
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWriteStringISO
parameter_list|()
throws|throws
name|Exception
block|{
name|MessageBodyWriter
argument_list|<
name|String
argument_list|>
name|p
init|=
operator|new
name|PrimitiveTextProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|os
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|MetadataMap
argument_list|<>
argument_list|()
decl_stmt|;
name|String
name|eWithAcute
init|=
literal|"\u00E9"
decl_stmt|;
name|String
name|helloStringUTF16
init|=
literal|"Hello, my name is F"
operator|+
name|eWithAcute
operator|+
literal|"lix Agn"
operator|+
name|eWithAcute
operator|+
literal|"s"
decl_stmt|;
name|p
operator|.
name|writeTo
argument_list|(
name|helloStringUTF16
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|String
operator|.
name|class
argument_list|,
literal|null
argument_list|,
name|MediaType
operator|.
name|valueOf
argument_list|(
literal|"text/plain;charset=ISO-8859-1"
argument_list|)
argument_list|,
name|headers
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|byte
index|[]
name|iso88591bytes
init|=
name|helloStringUTF16
operator|.
name|getBytes
argument_list|(
literal|"ISO-8859-1"
argument_list|)
decl_stmt|;
name|String
name|helloStringISO88591
init|=
operator|new
name|String
argument_list|(
name|iso88591bytes
argument_list|,
literal|"ISO-8859-1"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|helloStringISO88591
argument_list|,
name|os
operator|.
name|toString
argument_list|(
literal|"ISO-8859-1"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadChineeseChars
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|s
init|=
literal|"中文"
decl_stmt|;
name|MessageBodyReader
argument_list|<
name|String
argument_list|>
name|p
init|=
operator|new
name|PrimitiveTextProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|p
operator|.
name|readFrom
argument_list|(
name|String
operator|.
name|class
argument_list|,
literal|null
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|MediaType
operator|.
name|valueOf
argument_list|(
name|MediaType
operator|.
name|APPLICATION_XML
operator|+
literal|";charset=UTF-8"
argument_list|)
argument_list|,
literal|null
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|s
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|value
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
enum|enum
name|TestEnum
block|{
name|TEST
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEnum
parameter_list|()
throws|throws
name|Exception
block|{
name|testClass
argument_list|(
name|TestEnum
operator|.
name|TEST
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testURI
parameter_list|()
throws|throws
name|Exception
block|{
name|testClass
argument_list|(
operator|new
name|java
operator|.
name|net
operator|.
name|URI
argument_list|(
literal|"uri"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testURL
parameter_list|()
throws|throws
name|Exception
block|{
name|testClass
argument_list|(
operator|new
name|java
operator|.
name|net
operator|.
name|URL
argument_list|(
literal|"http://www.example.com"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|testClass
parameter_list|(
name|Object
name|value
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|PrimitiveTextProvider
argument_list|<
name|Object
argument_list|>
name|p
init|=
operator|new
name|PrimitiveTextProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|p
operator|.
name|isWriteable
argument_list|(
name|value
operator|.
name|getClass
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|MediaType
operator|.
name|TEXT_PLAIN_TYPE
argument_list|)
argument_list|)
expr_stmt|;
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
name|value
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|MediaType
operator|.
name|TEXT_PLAIN_TYPE
argument_list|,
literal|null
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
name|value
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|()
argument_list|,
name|os
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|p
operator|.
name|isReadable
argument_list|(
name|value
operator|.
name|getClass
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|MediaType
operator|.
name|TEXT_PLAIN_TYPE
argument_list|)
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Object
name|valueRead
init|=
name|p
operator|.
name|readFrom
argument_list|(
operator|(
name|Class
argument_list|<
name|Object
argument_list|>
operator|)
name|value
operator|.
name|getClass
argument_list|()
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
name|os
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|value
argument_list|,
name|valueRead
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

