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
name|ArrayList
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
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
name|message
operator|.
name|MessageImpl
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
name|SearchContextImplTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testFiqlSearchCondition
parameter_list|()
block|{
name|doTestFiqlSearchCondition
argument_list|(
name|SearchContextImpl
operator|.
name|SEARCH_QUERY
operator|+
literal|"="
operator|+
literal|"name==CXF%20Rocks;id=gt=123"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFiqlSearchConditionWithShortQuery
parameter_list|()
block|{
name|doTestFiqlSearchCondition
argument_list|(
name|SearchContextImpl
operator|.
name|SHORT_SEARCH_QUERY
operator|+
literal|"="
operator|+
literal|"name==CXF%20Rocks;id=gt=123"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFiqlSearchConditionWithNonFiqlQuery
parameter_list|()
block|{
name|doTestFiqlSearchCondition
argument_list|(
literal|"_s=name==CXF%20Rocks;id=gt=123&a=b"
argument_list|)
expr_stmt|;
name|doTestFiqlSearchCondition
argument_list|(
literal|"a=b&_s=name==CXF%20Rocks;id=gt=123"
argument_list|)
expr_stmt|;
name|doTestFiqlSearchCondition
argument_list|(
literal|"a=b&_s=name==CXF%20Rocks;id=gt=123&c=d"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doTestFiqlSearchCondition
parameter_list|(
name|String
name|queryString
parameter_list|)
block|{
name|Message
name|m
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|QUERY_STRING
argument_list|,
name|queryString
argument_list|)
expr_stmt|;
name|SearchContext
name|context
init|=
operator|new
name|SearchContextImpl
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|SearchCondition
argument_list|<
name|Book
argument_list|>
name|sc
init|=
name|context
operator|.
name|getCondition
argument_list|(
name|Book
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|sc
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Book
argument_list|>
name|books
init|=
operator|new
name|ArrayList
argument_list|<
name|Book
argument_list|>
argument_list|()
decl_stmt|;
name|books
operator|.
name|add
argument_list|(
operator|new
name|Book
argument_list|(
literal|"CXF is cool"
argument_list|,
literal|125L
argument_list|)
argument_list|)
expr_stmt|;
name|books
operator|.
name|add
argument_list|(
operator|new
name|Book
argument_list|(
literal|"CXF Rocks"
argument_list|,
literal|125L
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Book
argument_list|>
name|found
init|=
name|sc
operator|.
name|findAll
argument_list|(
name|books
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|found
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Book
argument_list|(
literal|"CXF Rocks"
argument_list|,
literal|125L
argument_list|)
argument_list|,
name|found
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Ignore
specifier|public
specifier|static
class|class
name|Book
block|{
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|long
name|id
decl_stmt|;
specifier|public
name|Book
parameter_list|()
block|{         }
specifier|public
name|Book
parameter_list|(
name|String
name|name
parameter_list|,
name|long
name|id
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|n
parameter_list|)
block|{
name|name
operator|=
name|n
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setId
parameter_list|(
name|Long
name|i
parameter_list|)
block|{
name|id
operator|=
name|i
expr_stmt|;
block|}
specifier|public
name|Long
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|name
operator|.
name|hashCode
argument_list|()
operator|*
literal|37
operator|+
operator|new
name|Long
argument_list|(
name|id
argument_list|)
operator|.
name|hashCode
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|Book
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Book
name|other
init|=
operator|(
name|Book
operator|)
name|o
decl_stmt|;
return|return
name|other
operator|.
name|name
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|&&
name|other
operator|.
name|id
operator|==
name|id
return|;
block|}
block|}
block|}
end_class

end_unit

