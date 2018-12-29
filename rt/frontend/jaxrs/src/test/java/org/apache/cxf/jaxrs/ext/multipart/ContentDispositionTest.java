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
name|multipart
package|;
end_package

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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_class
specifier|public
class|class
name|ContentDispositionTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testContentDisposition
parameter_list|()
block|{
name|ContentDisposition
name|cd
init|=
operator|new
name|ContentDisposition
argument_list|(
literal|" attachment ; bar=foo ; baz = baz1"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"attachment"
argument_list|,
name|cd
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|cd
operator|.
name|getParameter
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"baz1"
argument_list|,
name|cd
operator|.
name|getParameter
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
name|testContentDispositionWithQuotes
parameter_list|()
block|{
name|ContentDisposition
name|cd
init|=
operator|new
name|ContentDisposition
argument_list|(
literal|" attachment ; bar=\"foo.txt\" ; baz = baz1"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"attachment"
argument_list|,
name|cd
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo.txt"
argument_list|,
name|cd
operator|.
name|getParameter
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"baz1"
argument_list|,
name|cd
operator|.
name|getParameter
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
name|testContentDispositionWithQuotesAndSemicolon
parameter_list|()
block|{
name|ContentDisposition
name|cd
init|=
operator|new
name|ContentDisposition
argument_list|(
literal|" attachment ; bar=\"foo;txt\" ; baz = baz1"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"attachment"
argument_list|,
name|cd
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo;txt"
argument_list|,
name|cd
operator|.
name|getParameter
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"baz1"
argument_list|,
name|cd
operator|.
name|getParameter
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
name|testContentDispositionWithCreationDate
parameter_list|()
block|{
name|ContentDisposition
name|cd
init|=
operator|new
name|ContentDisposition
argument_list|(
literal|" attachment ; creation-date=\"21:08:08 14:00:00\""
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"attachment"
argument_list|,
name|cd
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"21:08:08 14:00:00"
argument_list|,
name|cd
operator|.
name|getParameter
argument_list|(
literal|"creation-date"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

