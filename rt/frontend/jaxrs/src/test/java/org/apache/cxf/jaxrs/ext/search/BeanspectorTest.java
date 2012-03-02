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
name|search
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|Ignore
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
name|BeanspectorTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testSimpleBean
parameter_list|()
throws|throws
name|FiqlParseException
block|{
name|Beanspector
argument_list|<
name|SimpleBean
argument_list|>
name|bean
init|=
operator|new
name|Beanspector
argument_list|<
name|SimpleBean
argument_list|>
argument_list|(
operator|new
name|SimpleBean
argument_list|()
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|getters
init|=
name|bean
operator|.
name|getGettersNames
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|getters
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getters
operator|.
name|contains
argument_list|(
literal|"class"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getters
operator|.
name|contains
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getters
operator|.
name|contains
argument_list|(
literal|"promised"
argument_list|)
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|setters
init|=
name|bean
operator|.
name|getSettersNames
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|setters
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getters
operator|.
name|contains
argument_list|(
literal|"a"
argument_list|)
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
name|testMismatchedAccessorTypes
parameter_list|()
throws|throws
name|FiqlParseException
block|{
operator|new
name|Beanspector
argument_list|<
name|MismatchedTypes
argument_list|>
argument_list|(
name|MismatchedTypes
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Ignore
specifier|static
class|class
name|MismatchedTypes
block|{
specifier|public
name|Date
name|getFoo
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setFoo
parameter_list|(
name|String
name|val
parameter_list|)
block|{         }
block|}
annotation|@
name|Ignore
specifier|static
class|class
name|SimpleBean
block|{
specifier|public
name|boolean
name|isPromised
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|String
name|getA
parameter_list|()
block|{
return|return
literal|"a"
return|;
block|}
specifier|public
name|void
name|setA
parameter_list|(
name|String
name|val
parameter_list|)
block|{         }
block|}
block|}
end_class

end_unit

