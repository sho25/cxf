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
name|InputStream
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
name|core
operator|.
name|MultivaluedMap
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
name|FormEncodingReaderProviderTest
extends|extends
name|Assert
block|{
specifier|private
name|FormEncodingReaderProvider
name|ferp
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|ferp
operator|=
operator|new
name|FormEncodingReaderProvider
argument_list|()
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
name|Object
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
name|Object
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
name|Test
specifier|public
name|void
name|testReadable
parameter_list|()
block|{
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
name|testAnnotations
parameter_list|()
block|{
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
block|}
end_class

end_unit

