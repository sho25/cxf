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
name|Cookie
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
name|CookieHeaderProviderTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testFromSimpleString
parameter_list|()
block|{
name|Cookie
name|c
init|=
name|Cookie
operator|.
name|valueOf
argument_list|(
literal|"foo=bar"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"bar"
operator|.
name|equals
argument_list|(
name|c
operator|.
name|getValue
argument_list|()
argument_list|)
operator|&&
literal|"foo"
operator|.
name|equals
argument_list|(
name|c
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
literal|0
operator|==
name|c
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoValue
parameter_list|()
block|{
name|Cookie
name|c
init|=
name|Cookie
operator|.
name|valueOf
argument_list|(
literal|"foo="
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|""
operator|.
name|equals
argument_list|(
name|c
operator|.
name|getValue
argument_list|()
argument_list|)
operator|&&
literal|"foo"
operator|.
name|equals
argument_list|(
name|c
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFromComplexString
parameter_list|()
block|{
name|Cookie
name|c
init|=
name|Cookie
operator|.
name|valueOf
argument_list|(
literal|"$Version=2;foo=bar;$Path=path;$Domain=domain"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"bar"
operator|.
name|equals
argument_list|(
name|c
operator|.
name|getValue
argument_list|()
argument_list|)
operator|&&
literal|"foo"
operator|.
name|equals
argument_list|(
name|c
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
literal|2
operator|==
name|c
operator|.
name|getVersion
argument_list|()
operator|&&
literal|"path"
operator|.
name|equals
argument_list|(
name|c
operator|.
name|getPath
argument_list|()
argument_list|)
operator|&&
literal|"domain"
operator|.
name|equals
argument_list|(
name|c
operator|.
name|getDomain
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
name|Cookie
name|c
init|=
operator|new
name|Cookie
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|,
literal|"path"
argument_list|,
literal|"domain"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"$Version=2;foo=bar;$Path=path;$Domain=domain"
argument_list|,
name|c
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
name|testToStringWithQuotes
parameter_list|()
block|{
name|Cookie
name|c
init|=
operator|new
name|Cookie
argument_list|(
literal|"foo"
argument_list|,
literal|"bar z"
argument_list|,
literal|"path"
argument_list|,
literal|"domain"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"$Version=2;foo=\"bar z\";$Path=path;$Domain=domain"
argument_list|,
name|c
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
name|testCookieWithQuotes
parameter_list|()
block|{
name|Cookie
name|c
init|=
name|Cookie
operator|.
name|valueOf
argument_list|(
literal|"$Version=\"1\"; foo=\"bar\"; $Path=\"/path\""
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"bar"
operator|.
name|equals
argument_list|(
name|c
operator|.
name|getValue
argument_list|()
argument_list|)
operator|&&
literal|"foo"
operator|.
name|equals
argument_list|(
name|c
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
literal|1
operator|==
name|c
operator|.
name|getVersion
argument_list|()
operator|&&
literal|"/path"
operator|.
name|equals
argument_list|(
name|c
operator|.
name|getPath
argument_list|()
argument_list|)
operator|&&
literal|null
operator|==
name|c
operator|.
name|getDomain
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
name|Cookie
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

