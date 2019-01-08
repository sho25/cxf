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
name|tools
operator|.
name|common
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|assertFalse
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
name|assertNull
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
name|ProcessorEnvironmentTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testGet
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"k1"
argument_list|,
literal|"v1"
argument_list|)
expr_stmt|;
name|ToolContext
name|env
init|=
operator|new
name|ToolContext
argument_list|()
decl_stmt|;
name|env
operator|.
name|setParameters
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|String
name|value
init|=
operator|(
name|String
operator|)
name|env
operator|.
name|get
argument_list|(
literal|"k1"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"v1"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPut
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"k1"
argument_list|,
literal|"v1"
argument_list|)
expr_stmt|;
name|ToolContext
name|env
init|=
operator|new
name|ToolContext
argument_list|()
decl_stmt|;
name|env
operator|.
name|setParameters
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
literal|"k2"
argument_list|,
literal|"v2"
argument_list|)
expr_stmt|;
name|String
name|value
init|=
operator|(
name|String
operator|)
name|env
operator|.
name|get
argument_list|(
literal|"k2"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"v2"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRemove
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"k1"
argument_list|,
literal|"v1"
argument_list|)
expr_stmt|;
name|ToolContext
name|env
init|=
operator|new
name|ToolContext
argument_list|()
decl_stmt|;
name|env
operator|.
name|setParameters
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
literal|"k2"
argument_list|,
literal|"v2"
argument_list|)
expr_stmt|;
name|String
name|value
init|=
operator|(
name|String
operator|)
name|env
operator|.
name|get
argument_list|(
literal|"k2"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"v2"
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|env
operator|.
name|remove
argument_list|(
literal|"k1"
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|env
operator|.
name|get
argument_list|(
literal|"k1"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testContainsKey
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"k1"
argument_list|,
literal|"v1"
argument_list|)
expr_stmt|;
name|ToolContext
name|env
init|=
operator|new
name|ToolContext
argument_list|()
decl_stmt|;
name|env
operator|.
name|setParameters
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|env
operator|.
name|containsKey
argument_list|(
literal|"k1"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetDefaultValue
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"k1"
argument_list|,
literal|"v1"
argument_list|)
expr_stmt|;
name|ToolContext
name|env
init|=
operator|new
name|ToolContext
argument_list|()
decl_stmt|;
name|env
operator|.
name|setParameters
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|String
name|k1
init|=
operator|(
name|String
operator|)
name|env
operator|.
name|get
argument_list|(
literal|"k1"
argument_list|,
literal|"v2"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"v1"
argument_list|,
name|k1
argument_list|)
expr_stmt|;
name|String
name|k2
init|=
operator|(
name|String
operator|)
name|env
operator|.
name|get
argument_list|(
literal|"k2"
argument_list|,
literal|"v2"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"v2"
argument_list|,
name|k2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOptionSet
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"k1"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|ToolContext
name|env
init|=
operator|new
name|ToolContext
argument_list|()
decl_stmt|;
name|env
operator|.
name|setParameters
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|env
operator|.
name|optionSet
argument_list|(
literal|"k1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|env
operator|.
name|optionSet
argument_list|(
literal|"k2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBooleanValue
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"k1"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|ToolContext
name|env
init|=
operator|new
name|ToolContext
argument_list|()
decl_stmt|;
name|env
operator|.
name|setParameters
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|Boolean
name|k1
init|=
name|Boolean
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|env
operator|.
name|get
argument_list|(
literal|"k1"
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|k1
argument_list|)
expr_stmt|;
name|Boolean
name|k2
init|=
name|Boolean
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|env
operator|.
name|get
argument_list|(
literal|"k2"
argument_list|,
literal|"true"
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|k2
argument_list|)
expr_stmt|;
name|Boolean
name|k3
init|=
name|Boolean
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|env
operator|.
name|get
argument_list|(
literal|"k3"
argument_list|,
literal|"yes"
argument_list|)
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|k3
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

