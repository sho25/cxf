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
name|ext
operator|.
name|xml
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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|LinkedHashMap
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
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
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
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|XMLSourceTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testNodeStringValue
parameter_list|()
block|{
name|InputStream
name|is
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/book1.xsd"
argument_list|)
decl_stmt|;
name|XMLSource
name|xp
init|=
operator|new
name|XMLSource
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|nsMap
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"xs"
argument_list|,
name|XmlSchemaConstants
operator|.
name|XSD_NAMESPACE_URI
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|xp
operator|.
name|getNode
argument_list|(
literal|"/xs:schema"
argument_list|,
name|nsMap
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|value
operator|.
name|contains
argument_list|(
literal|"<?xml"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|value
operator|.
name|startsWith
argument_list|(
literal|"<xs:schema"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAttributeValue
parameter_list|()
block|{
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"<foo><bar attr=\"baz\">barValue</bar></foo>"
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|XMLSource
name|xp
init|=
operator|new
name|XMLSource
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"baz"
argument_list|,
name|xp
operator|.
name|getValue
argument_list|(
literal|"/foo/bar/@attr"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAttributeValueAsNode
parameter_list|()
block|{
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"<foo><bar attr=\"baz\">barValue</bar></foo>"
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|XMLSource
name|xp
init|=
operator|new
name|XMLSource
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|Node
name|node
init|=
name|xp
operator|.
name|getNode
argument_list|(
literal|"/foo/bar/@attr"
argument_list|,
name|Node
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"baz"
argument_list|,
name|node
operator|.
name|getTextContent
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNodeTextValue
parameter_list|()
block|{
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"<foo><bar attr=\"baz\">barValue</bar></foo>"
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|XMLSource
name|xp
init|=
operator|new
name|XMLSource
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"barValue"
argument_list|,
name|xp
operator|.
name|getValue
argument_list|(
literal|"/foo/bar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAttributeValues
parameter_list|()
block|{
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"<foo><bar attr=\"baz\">bar1</bar><bar attr=\"baz2\">bar2</bar></foo>"
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|XMLSource
name|xp
init|=
operator|new
name|XMLSource
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|xp
operator|.
name|getValues
argument_list|(
literal|"/foo/bar/@attr"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|values
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|values
operator|.
name|contains
argument_list|(
literal|"baz"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|values
operator|.
name|contains
argument_list|(
literal|"baz2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNodeTextValues
parameter_list|()
block|{
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"<foo><bar attr=\"baz\">bar1</bar><bar attr=\"baz2\">bar2</bar></foo>"
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|XMLSource
name|xp
init|=
operator|new
name|XMLSource
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|xp
operator|.
name|getValues
argument_list|(
literal|"/foo/bar/text()"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|values
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|values
operator|.
name|contains
argument_list|(
literal|"bar1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|values
operator|.
name|contains
argument_list|(
literal|"bar2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIntegerValues
parameter_list|()
block|{
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"<foo><bar attr=\"1\"/><bar attr=\"2\"/></foo>"
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|XMLSource
name|xp
init|=
operator|new
name|XMLSource
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|Integer
index|[]
name|values
init|=
name|xp
operator|.
name|getNodes
argument_list|(
literal|"/foo/bar/@attr"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|values
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|values
index|[
literal|0
index|]
operator|==
literal|1
operator|&&
name|values
index|[
literal|1
index|]
operator|==
literal|2
operator|||
name|values
index|[
literal|0
index|]
operator|==
literal|2
operator|&&
name|values
index|[
literal|1
index|]
operator|==
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetNodeNoNamespace
parameter_list|()
block|{
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"<foo><bar/></foo>"
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|XMLSource
name|xp
init|=
operator|new
name|XMLSource
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|Bar
name|bar
init|=
name|xp
operator|.
name|getNode
argument_list|(
literal|"/foo/bar"
argument_list|,
name|Bar
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bar
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetNodeAsElement
parameter_list|()
block|{
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"<foo><bar/></foo>"
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|XMLSource
name|xp
init|=
operator|new
name|XMLSource
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|Element
name|element
init|=
name|xp
operator|.
name|getNode
argument_list|(
literal|"/foo/bar"
argument_list|,
name|Element
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetNodeAsSource
parameter_list|()
block|{
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"<foo><bar/></foo>"
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|XMLSource
name|xp
init|=
operator|new
name|XMLSource
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|Source
name|element
init|=
name|xp
operator|.
name|getNode
argument_list|(
literal|"/foo/bar"
argument_list|,
name|Source
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetNodeNull
parameter_list|()
block|{
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"<foo><bar/></foo>"
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|XMLSource
name|xp
init|=
operator|new
name|XMLSource
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|xp
operator|.
name|getNode
argument_list|(
literal|"/foo/bar1"
argument_list|,
name|Element
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
name|testGetNodeAsJaxbElement
parameter_list|()
block|{
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"<foo><bar name=\"foo\"/></foo>"
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|XMLSource
name|xp
init|=
operator|new
name|XMLSource
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|Bar3
name|bar
init|=
name|xp
operator|.
name|getNode
argument_list|(
literal|"/foo/bar"
argument_list|,
name|Bar3
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bar
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|bar
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetNodeNamespace
parameter_list|()
block|{
name|String
name|data
init|=
literal|"<x:foo xmlns:x=\"http://baz\"><x:bar/></x:foo>"
decl_stmt|;
name|InputStream
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
name|XMLSource
name|xp
init|=
operator|new
name|XMLSource
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"x"
argument_list|,
literal|"http://baz"
argument_list|)
expr_stmt|;
name|Bar2
name|bar
init|=
name|xp
operator|.
name|getNode
argument_list|(
literal|"/x:foo/x:bar"
argument_list|,
name|map
argument_list|,
name|Bar2
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bar
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetNodeBuffering
parameter_list|()
block|{
name|String
name|data
init|=
literal|"<x:foo xmlns:x=\"http://baz\"><x:bar/></x:foo>"
decl_stmt|;
name|InputStream
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
name|XMLSource
name|xp
init|=
operator|new
name|XMLSource
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|xp
operator|.
name|setBuffering
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"x"
argument_list|,
literal|"http://baz"
argument_list|)
expr_stmt|;
name|Bar2
name|bar
init|=
name|xp
operator|.
name|getNode
argument_list|(
literal|"/x:foo/x:bar"
argument_list|,
name|map
argument_list|,
name|Bar2
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bar
argument_list|)
expr_stmt|;
name|bar
operator|=
name|xp
operator|.
name|getNode
argument_list|(
literal|"/x:foo/x:bar"
argument_list|,
name|map
argument_list|,
name|Bar2
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|bar
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetNodeNamespace2
parameter_list|()
block|{
name|String
name|data
init|=
literal|"<z:foo xmlns:z=\"http://baz\"><z:bar/></z:foo>"
decl_stmt|;
name|InputStream
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
name|XMLSource
name|xp
init|=
operator|new
name|XMLSource
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"x"
argument_list|,
literal|"http://baz"
argument_list|)
expr_stmt|;
name|Bar2
name|bar
init|=
name|xp
operator|.
name|getNode
argument_list|(
literal|"/x:foo/x:bar"
argument_list|,
name|map
argument_list|,
name|Bar2
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bar
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetNodeNamespace3
parameter_list|()
block|{
name|String
name|data
init|=
literal|"<x:foo xmlns:x=\"http://foo\" xmlns:z=\"http://baz\"><z:bar/></x:foo>"
decl_stmt|;
name|InputStream
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
name|XMLSource
name|xp
init|=
operator|new
name|XMLSource
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"x"
argument_list|,
literal|"http://foo"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"y"
argument_list|,
literal|"http://baz"
argument_list|)
expr_stmt|;
name|Bar2
name|bar
init|=
name|xp
operator|.
name|getNode
argument_list|(
literal|"/x:foo/y:bar"
argument_list|,
name|map
argument_list|,
name|Bar2
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bar
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetNodeDefaultNamespace
parameter_list|()
block|{
name|String
name|data
init|=
literal|"<foo xmlns=\"http://baz\"><bar/></foo>"
decl_stmt|;
name|InputStream
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
name|XMLSource
name|xp
init|=
operator|new
name|XMLSource
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"x"
argument_list|,
literal|"http://baz"
argument_list|)
expr_stmt|;
name|Bar2
name|bar
init|=
name|xp
operator|.
name|getNode
argument_list|(
literal|"/x:foo/x:bar"
argument_list|,
name|map
argument_list|,
name|Bar2
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bar
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetNodesNoNamespace
parameter_list|()
block|{
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"<foo><bar/><bar/></foo>"
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|XMLSource
name|xp
init|=
operator|new
name|XMLSource
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|Bar
index|[]
name|bars
init|=
name|xp
operator|.
name|getNodes
argument_list|(
literal|"/foo/bar"
argument_list|,
name|Bar
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bars
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|bars
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|bars
index|[
literal|0
index|]
argument_list|,
name|bars
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetNodesNamespace
parameter_list|()
block|{
name|String
name|data
init|=
literal|"<x:foo xmlns:x=\"http://baz\"><x:bar/><x:bar/></x:foo>"
decl_stmt|;
name|InputStream
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
name|XMLSource
name|xp
init|=
operator|new
name|XMLSource
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"x"
argument_list|,
literal|"http://baz"
argument_list|)
expr_stmt|;
name|Bar2
index|[]
name|bars
init|=
name|xp
operator|.
name|getNodes
argument_list|(
literal|"/x:foo/x:bar"
argument_list|,
name|map
argument_list|,
name|Bar2
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bars
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|bars
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|bars
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|bars
index|[
literal|0
index|]
argument_list|,
name|bars
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetStringValue
parameter_list|()
block|{
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"<foo><bar/><bar id=\"2\"/></foo>"
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|XMLSource
name|xp
init|=
operator|new
name|XMLSource
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|xp
operator|.
name|getValue
argument_list|(
literal|"/foo/bar/@id"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"2"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetRelativeLink
parameter_list|()
block|{
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"<foo><bar/><bar href=\"/2\"/></foo>"
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|XMLSource
name|xp
init|=
operator|new
name|XMLSource
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|URI
name|value
init|=
name|xp
operator|.
name|getLink
argument_list|(
literal|"/foo/bar/@href"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"/2"
argument_list|,
name|value
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
name|testBaseURI
parameter_list|()
block|{
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"<foo xml:base=\"http://bar\"><bar/><bar href=\"/2\"/></foo>"
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|XMLSource
name|xp
init|=
operator|new
name|XMLSource
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|URI
name|value
init|=
name|xp
operator|.
name|getBaseURI
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://bar"
argument_list|,
name|value
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|XmlRootElement
specifier|private
specifier|static
class|class
name|Bar
block|{              }
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"bar"
argument_list|,
name|namespace
operator|=
literal|"http://baz"
argument_list|)
specifier|private
specifier|static
class|class
name|Bar2
block|{              }
specifier|private
specifier|static
class|class
name|Bar3
block|{
annotation|@
name|XmlAttribute
specifier|private
name|String
name|name
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
block|}
block|}
end_class

end_unit

