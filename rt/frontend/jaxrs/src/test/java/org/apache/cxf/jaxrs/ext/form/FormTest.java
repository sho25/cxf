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
name|form
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
name|Form
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
name|FormUtils
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
name|FormTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testToString
parameter_list|()
block|{
name|Form
name|form
init|=
operator|new
name|Form
argument_list|()
operator|.
name|param
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
operator|.
name|param
argument_list|(
literal|"c"
argument_list|,
literal|"d"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"a=b&c=d"
argument_list|,
name|FormUtils
operator|.
name|formToString
argument_list|(
name|form
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

