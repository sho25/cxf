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
name|Consumes
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
name|Encoded
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
name|WebApplicationException
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
name|Form
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
name|utils
operator|.
name|HttpUtils
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
name|FormEncodingProviderTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testReadFrom
parameter_list|()
throws|throws
name|Exception
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
name|FormEncodingProvider
argument_list|<
name|MultivaluedMap
argument_list|>
name|ferp
init|=
operator|new
name|FormEncodingProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"singleValPostBody.txt"
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|mvMap
init|=
name|ferp
operator|.
name|readFrom
argument_list|(
name|MultivaluedMap
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
name|APPLICATION_FORM_URLENCODED_TYPE
argument_list|,
literal|null
argument_list|,
name|is
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong entry for foo"
argument_list|,
literal|"bar"
argument_list|,
name|mvMap
operator|.
name|getFirst
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong entry for boo"
argument_list|,
literal|"far"
argument_list|,
name|mvMap
operator|.
name|getFirst
argument_list|(
literal|"boo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadFromForm
parameter_list|()
throws|throws
name|Exception
block|{
name|FormEncodingProvider
argument_list|<
name|Form
argument_list|>
name|ferp
init|=
operator|new
name|FormEncodingProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"singleValPostBody.txt"
argument_list|)
decl_stmt|;
name|Form
name|form
init|=
name|ferp
operator|.
name|readFrom
argument_list|(
name|Form
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
name|APPLICATION_FORM_URLENCODED_TYPE
argument_list|,
literal|null
argument_list|,
name|is
argument_list|)
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|mvMap
init|=
name|form
operator|.
name|asMap
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong entry for foo"
argument_list|,
literal|"bar"
argument_list|,
name|mvMap
operator|.
name|getFirst
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong entry for boo"
argument_list|,
literal|"far"
argument_list|,
name|mvMap
operator|.
name|getFirst
argument_list|(
literal|"boo"
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
name|testDecoded
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|values
init|=
literal|"foo=1+2&bar=1+3"
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
name|FormEncodingProvider
argument_list|<
name|MultivaluedMap
argument_list|>
name|ferp
init|=
operator|new
name|FormEncodingProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|mvMap
init|=
name|ferp
operator|.
name|readFrom
argument_list|(
name|MultivaluedMap
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
name|APPLICATION_FORM_URLENCODED_TYPE
argument_list|,
literal|null
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|values
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong entry for foo"
argument_list|,
literal|"1 2"
argument_list|,
name|mvMap
operator|.
name|getFirst
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong entry for boo"
argument_list|,
literal|"1 3"
argument_list|,
name|mvMap
operator|.
name|getFirst
argument_list|(
literal|"bar"
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
name|testEncoded
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|values
init|=
literal|"foo=1+2&bar=1+3"
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
name|FormEncodingProvider
argument_list|<
name|MultivaluedMap
argument_list|>
name|ferp
init|=
operator|new
name|FormEncodingProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|mvMap
init|=
name|ferp
operator|.
name|readFrom
argument_list|(
name|MultivaluedMap
operator|.
name|class
argument_list|,
literal|null
argument_list|,
operator|new
name|Annotation
index|[]
block|{
name|CustomMap
operator|.
name|class
operator|.
name|getAnnotations
argument_list|()
index|[
literal|0
index|]
block|}
argument_list|,
name|MediaType
operator|.
name|APPLICATION_FORM_URLENCODED_TYPE
argument_list|,
literal|null
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|values
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong entry for foo"
argument_list|,
literal|"1+2"
argument_list|,
name|mvMap
operator|.
name|getFirst
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong entry for boo"
argument_list|,
literal|"1+3"
argument_list|,
name|mvMap
operator|.
name|getFirst
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCustomMapImpl
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|values
init|=
literal|"foo=1+2&bar=1+3&baz=4"
decl_stmt|;
name|FormEncodingProvider
argument_list|<
name|CustomMap
argument_list|>
name|ferp
init|=
operator|new
name|FormEncodingProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|mvMap
init|=
name|ferp
operator|.
name|readFrom
argument_list|(
name|CustomMap
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
name|APPLICATION_FORM_URLENCODED_TYPE
argument_list|,
literal|null
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|values
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|mvMap
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|mvMap
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|mvMap
operator|.
name|get
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|mvMap
operator|.
name|get
argument_list|(
literal|"baz"
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong entry for foo"
argument_list|,
literal|"1 2"
argument_list|,
name|mvMap
operator|.
name|getFirst
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong entry for boo"
argument_list|,
literal|"1 3"
argument_list|,
name|mvMap
operator|.
name|getFirst
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong entry for baz"
argument_list|,
literal|"4"
argument_list|,
name|mvMap
operator|.
name|getFirst
argument_list|(
literal|"baz"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMultiLines
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|values
init|=
literal|"foo=1+2&bar=line1%0D%0Aline+2&baz=4"
decl_stmt|;
name|FormEncodingProvider
argument_list|<
name|CustomMap
argument_list|>
name|ferp
init|=
operator|new
name|FormEncodingProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|mvMap
init|=
name|ferp
operator|.
name|readFrom
argument_list|(
name|CustomMap
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
name|APPLICATION_FORM_URLENCODED_TYPE
argument_list|,
literal|null
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|values
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|mvMap
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|mvMap
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|mvMap
operator|.
name|get
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|mvMap
operator|.
name|get
argument_list|(
literal|"baz"
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong entry for foo"
argument_list|,
literal|"1 2"
argument_list|,
name|mvMap
operator|.
name|getFirst
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong entry line for bar"
argument_list|,
name|HttpUtils
operator|.
name|urlDecode
argument_list|(
literal|"line1%0D%0Aline+2"
argument_list|)
argument_list|,
name|mvMap
operator|.
name|get
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong entry for baz"
argument_list|,
literal|"4"
argument_list|,
name|mvMap
operator|.
name|getFirst
argument_list|(
literal|"baz"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWriteMultipleValues
parameter_list|()
throws|throws
name|Exception
block|{
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|mvMap
init|=
operator|new
name|MetadataMap
argument_list|<>
argument_list|()
decl_stmt|;
name|mvMap
operator|.
name|add
argument_list|(
literal|"a"
argument_list|,
literal|"a1"
argument_list|)
expr_stmt|;
name|mvMap
operator|.
name|add
argument_list|(
literal|"a"
argument_list|,
literal|"a2"
argument_list|)
expr_stmt|;
name|FormEncodingProvider
argument_list|<
name|MultivaluedMap
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
argument_list|>
name|ferp
init|=
operator|new
name|FormEncodingProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|ferp
operator|.
name|writeTo
argument_list|(
name|mvMap
argument_list|,
name|MultivaluedMap
operator|.
name|class
argument_list|,
name|MultivaluedMap
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
name|APPLICATION_FORM_URLENCODED_TYPE
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
name|result
init|=
name|bos
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong value"
argument_list|,
literal|"a=a1&a=a2"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWriteMultipleValues2
parameter_list|()
throws|throws
name|Exception
block|{
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|mvMap
init|=
operator|new
name|MetadataMap
argument_list|<>
argument_list|()
decl_stmt|;
name|mvMap
operator|.
name|add
argument_list|(
literal|"a"
argument_list|,
literal|"a1"
argument_list|)
expr_stmt|;
name|mvMap
operator|.
name|add
argument_list|(
literal|"a"
argument_list|,
literal|"a2"
argument_list|)
expr_stmt|;
name|mvMap
operator|.
name|add
argument_list|(
literal|"b"
argument_list|,
literal|"b1"
argument_list|)
expr_stmt|;
name|FormEncodingProvider
argument_list|<
name|MultivaluedMap
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
argument_list|>
name|ferp
init|=
operator|new
name|FormEncodingProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|ferp
operator|.
name|writeTo
argument_list|(
name|mvMap
argument_list|,
name|MultivaluedMap
operator|.
name|class
argument_list|,
name|MultivaluedMap
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
name|APPLICATION_FORM_URLENCODED_TYPE
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
name|result
init|=
name|bos
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong value"
argument_list|,
literal|"a=a1&a=a2&b=b1"
argument_list|,
name|result
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
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|mvMap
init|=
operator|new
name|MetadataMap
argument_list|<>
argument_list|()
decl_stmt|;
name|mvMap
operator|.
name|add
argument_list|(
literal|"a"
argument_list|,
literal|"a1"
argument_list|)
expr_stmt|;
name|mvMap
operator|.
name|add
argument_list|(
literal|"b"
argument_list|,
literal|"b1"
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|FormEncodingProvider
argument_list|<
name|MultivaluedMap
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
argument_list|>
name|ferp
init|=
operator|new
name|FormEncodingProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|ferp
operator|.
name|writeTo
argument_list|(
name|mvMap
argument_list|,
name|MultivaluedMap
operator|.
name|class
argument_list|,
name|MultivaluedMap
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
name|APPLICATION_FORM_URLENCODED_TYPE
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
name|result
init|=
name|bos
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong value"
argument_list|,
literal|"a=a1&b=b1"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWriteForm
parameter_list|()
throws|throws
name|Exception
block|{
name|Form
name|form
init|=
operator|new
name|Form
argument_list|(
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|FormEncodingProvider
argument_list|<
name|Form
argument_list|>
name|ferp
init|=
operator|new
name|FormEncodingProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|ferp
operator|.
name|writeTo
argument_list|(
name|form
operator|.
name|param
argument_list|(
literal|"a"
argument_list|,
literal|"a1"
argument_list|)
operator|.
name|param
argument_list|(
literal|"b"
argument_list|,
literal|"b1"
argument_list|)
argument_list|,
name|Form
operator|.
name|class
argument_list|,
name|Form
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
name|APPLICATION_FORM_URLENCODED_TYPE
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
name|result
init|=
name|bos
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong value"
argument_list|,
literal|"a=a1&b=b1"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testValidation
parameter_list|()
throws|throws
name|Exception
block|{
name|FormEncodingProvider
argument_list|<
name|CustomMap
argument_list|>
name|ferp
init|=
operator|new
name|FormEncodingProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|ferp
operator|.
name|setValidator
argument_list|(
operator|new
name|CustomFormValidator
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|values
init|=
literal|"foo=1+2&bar=1+3"
decl_stmt|;
try|try
block|{
name|ferp
operator|.
name|readFrom
argument_list|(
name|CustomMap
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
name|APPLICATION_FORM_URLENCODED_TYPE
argument_list|,
literal|null
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|values
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebApplicationException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
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
name|testReadFromMultiples
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|is
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"multiValPostBody.txt"
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
name|FormEncodingProvider
argument_list|<
name|MultivaluedMap
argument_list|>
name|ferp
init|=
operator|new
name|FormEncodingProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|mvMap
init|=
name|ferp
operator|.
name|readFrom
argument_list|(
name|MultivaluedMap
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
name|APPLICATION_FORM_URLENCODED_TYPE
argument_list|,
literal|null
argument_list|,
name|is
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|vals
init|=
name|mvMap
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong size for foo params"
argument_list|,
literal|2
argument_list|,
name|vals
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong size for foo params"
argument_list|,
literal|1
argument_list|,
name|mvMap
operator|.
name|get
argument_list|(
literal|"boo"
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong entry for foo 0"
argument_list|,
literal|"bar"
argument_list|,
name|vals
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong entry for foo 1"
argument_list|,
literal|"bar2"
argument_list|,
name|vals
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong entry for boo"
argument_list|,
literal|"far"
argument_list|,
name|mvMap
operator|.
name|getFirst
argument_list|(
literal|"boo"
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
name|testReadFromISO
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|eWithAcute
init|=
literal|"\u00E9"
decl_stmt|;
name|String
name|helloStringUTF16
init|=
literal|"name=F"
operator|+
name|eWithAcute
operator|+
literal|"lix"
decl_stmt|;
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
name|FormEncodingProvider
argument_list|<
name|MultivaluedMap
argument_list|>
name|ferp
init|=
operator|new
name|FormEncodingProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|mvMap
init|=
name|ferp
operator|.
name|readFrom
argument_list|(
name|MultivaluedMap
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
name|APPLICATION_FORM_URLENCODED
operator|+
literal|";charset=ISO-8859-1"
argument_list|)
argument_list|,
literal|null
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|iso88591bytes
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|mvMap
operator|.
name|getFirst
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|helloStringISO88591
argument_list|,
literal|"name="
operator|+
name|value
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
name|testReadChineeseChars
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|s
init|=
literal|"name=中文"
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
name|FormEncodingProvider
argument_list|<
name|MultivaluedMap
argument_list|>
name|ferp
init|=
operator|new
name|FormEncodingProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|mvMap
init|=
name|ferp
operator|.
name|readFrom
argument_list|(
name|MultivaluedMap
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
name|APPLICATION_FORM_URLENCODED
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
name|String
name|value
init|=
name|mvMap
operator|.
name|getFirst
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|s
argument_list|,
literal|"name="
operator|+
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadableMap
parameter_list|()
block|{
name|FormEncodingProvider
argument_list|<
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|ferp
init|=
operator|new
name|FormEncodingProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|ferp
operator|.
name|isReadable
argument_list|(
name|MultivaluedMap
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
name|testReadableForm
parameter_list|()
block|{
name|FormEncodingProvider
argument_list|<
name|Form
argument_list|>
name|ferp
init|=
operator|new
name|FormEncodingProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|ferp
operator|.
name|isReadable
argument_list|(
name|Form
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
name|testAnnotations
parameter_list|()
block|{
name|FormEncodingProvider
argument_list|<
name|Form
argument_list|>
name|ferp
init|=
operator|new
name|FormEncodingProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"application/x-www-form-urlencoded"
argument_list|,
name|ferp
operator|.
name|getClass
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|Consumes
operator|.
name|class
argument_list|)
operator|.
name|value
argument_list|()
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Encoded
specifier|public
specifier|static
class|class
name|CustomMap
extends|extends
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
block|{      }
specifier|private
specifier|static
class|class
name|CustomFormValidator
implements|implements
name|FormValidator
block|{
specifier|public
name|void
name|validate
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|?
extends|extends
name|Object
argument_list|>
name|params
parameter_list|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|()
throw|;
block|}
block|}
block|}
end_class

end_unit

