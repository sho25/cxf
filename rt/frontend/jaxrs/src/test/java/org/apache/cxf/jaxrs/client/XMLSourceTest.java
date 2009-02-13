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
name|client
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
name|XmlRootElement
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
block|}
end_class

end_unit

