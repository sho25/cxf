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
name|impl
package|;
end_package

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
name|EntityTag
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|EntityTagHeaderProviderTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testFromString
parameter_list|()
block|{
name|EntityTag
name|tag
init|=
name|EntityTag
operator|.
name|valueOf
argument_list|(
literal|"\"\""
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
operator|!
name|tag
operator|.
name|isWeak
argument_list|()
operator|&&
literal|""
operator|.
name|equals
argument_list|(
name|tag
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|tag
operator|=
name|EntityTag
operator|.
name|valueOf
argument_list|(
literal|"W/"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tag
operator|.
name|isWeak
argument_list|()
operator|&&
literal|""
operator|.
name|equals
argument_list|(
name|tag
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|tag
operator|=
name|EntityTag
operator|.
name|valueOf
argument_list|(
literal|"W/\"12345\""
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tag
operator|.
name|isWeak
argument_list|()
operator|&&
literal|"12345"
operator|.
name|equals
argument_list|(
name|tag
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|tag
operator|=
name|EntityTag
operator|.
name|valueOf
argument_list|(
literal|"\"12345\""
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|tag
operator|.
name|isWeak
argument_list|()
operator|&&
literal|"12345"
operator|.
name|equals
argument_list|(
name|tag
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testToString
parameter_list|()
block|{
name|EntityTag
name|tag
init|=
operator|new
name|EntityTag
argument_list|(
literal|""
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"\"\""
argument_list|,
name|tag
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|tag
operator|=
operator|new
name|EntityTag
argument_list|(
literal|""
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"W/\"\""
argument_list|,
name|tag
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|tag
operator|=
operator|new
name|EntityTag
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"\"bar\""
argument_list|,
name|tag
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testNullValue
parameter_list|()
throws|throws
name|Exception
block|{
name|EntityTag
operator|.
name|valueOf
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

