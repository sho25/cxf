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
name|common
operator|.
name|util
package|;
end_package

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
name|StringUtilsTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testDiff
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|str1
init|=
literal|"http://local/SoapContext/SoapPort/greetMe/me/CXF"
decl_stmt|;
name|String
name|str2
init|=
literal|"http://local/SoapContext/SoapPort"
decl_stmt|;
name|String
name|str3
init|=
literal|"http://local/SoapContext/SoapPort/"
decl_stmt|;
name|assertEquals
argument_list|(
literal|"/greetMe/me/CXF"
argument_list|,
name|StringUtils
operator|.
name|diff
argument_list|(
name|str1
argument_list|,
name|str2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"greetMe/me/CXF"
argument_list|,
name|StringUtils
operator|.
name|diff
argument_list|(
name|str1
argument_list|,
name|str3
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://local/SoapContext/SoapPort/"
argument_list|,
name|StringUtils
operator|.
name|diff
argument_list|(
name|str3
argument_list|,
name|str1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetFirstNotEmpty
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"greetMe"
argument_list|,
name|StringUtils
operator|.
name|getFirstNotEmpty
argument_list|(
literal|"/greetMe/me/CXF"
argument_list|,
literal|"/"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"greetMe"
argument_list|,
name|StringUtils
operator|.
name|getFirstNotEmpty
argument_list|(
literal|"greetMe/me/CXF"
argument_list|,
literal|"/"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetParts
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|str
init|=
literal|"/greetMe/me/CXF"
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|parts
init|=
name|StringUtils
operator|.
name|getParts
argument_list|(
name|str
argument_list|,
literal|"/"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|parts
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"greetMe"
argument_list|,
name|parts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"me"
argument_list|,
name|parts
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CXF"
argument_list|,
name|parts
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetPartsWithSingleSpace
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|str
init|=
literal|"a b"
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|parts
init|=
name|StringUtils
operator|.
name|getParts
argument_list|(
name|str
argument_list|,
literal|" "
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|parts
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|parts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"b"
argument_list|,
name|parts
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetPartsWithManySpaces
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|str
init|=
literal|"a  b"
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|parts
init|=
name|StringUtils
operator|.
name|getParts
argument_list|(
name|str
argument_list|,
literal|" "
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|parts
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|parts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"b"
argument_list|,
name|parts
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSplitWithDot
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|str
init|=
literal|"a.b.c"
decl_stmt|;
name|String
index|[]
name|parts
init|=
name|StringUtils
operator|.
name|split
argument_list|(
name|str
argument_list|,
literal|"\\."
argument_list|,
operator|-
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|parts
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|parts
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"b"
argument_list|,
name|parts
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"c"
argument_list|,
name|parts
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetFound
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|regex
init|=
literal|"velocity-\\d+\\.\\d+\\.jar"
decl_stmt|;
name|assertTrue
argument_list|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|StringUtils
operator|.
name|getFound
argument_list|(
literal|"velocity-dep-1.4.jar"
argument_list|,
name|regex
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|StringUtils
operator|.
name|getFound
argument_list|(
literal|"velocity-1.4.jar"
argument_list|,
name|regex
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|StringUtils
operator|.
name|getFound
argument_list|(
literal|null
argument_list|,
name|regex
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddPortIfMissing
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"http://localhost:80"
argument_list|,
name|StringUtils
operator|.
name|addDefaultPortIfMissing
argument_list|(
literal|"http://localhost"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://localhost:80/"
argument_list|,
name|StringUtils
operator|.
name|addDefaultPortIfMissing
argument_list|(
literal|"http://localhost/"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://localhost:80/abc"
argument_list|,
name|StringUtils
operator|.
name|addDefaultPortIfMissing
argument_list|(
literal|"http://localhost/abc"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://localhost:80"
argument_list|,
name|StringUtils
operator|.
name|addDefaultPortIfMissing
argument_list|(
literal|"http://localhost:80"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://localhost:9090"
argument_list|,
name|StringUtils
operator|.
name|addDefaultPortIfMissing
argument_list|(
literal|"http://localhost"
argument_list|,
literal|"9090"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

