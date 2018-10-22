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
operator|.
name|fiql
package|;
end_package

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
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|ext
operator|.
name|search
operator|.
name|SearchCondition
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
name|ext
operator|.
name|search
operator|.
name|SearchParseException
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
name|FiqlCollectionsTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testWithCollectionAfterFirstLevelOnCollection
parameter_list|()
throws|throws
name|SearchParseException
block|{
name|FiqlParser
argument_list|<
name|Place
argument_list|>
name|placeParser
init|=
operator|new
name|FiqlParser
argument_list|<>
argument_list|(
name|Place
operator|.
name|class
argument_list|)
decl_stmt|;
name|SearchCondition
argument_list|<
name|Place
argument_list|>
name|placeCondition
init|=
name|placeParser
operator|.
name|parse
argument_list|(
literal|"specs.features.description==description"
argument_list|)
decl_stmt|;
name|Place
name|place
init|=
name|placeCondition
operator|.
name|getCondition
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|place
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWithCollectionAfterFirstLevelOnSingleObject
parameter_list|()
throws|throws
name|SearchParseException
block|{
name|FiqlParser
argument_list|<
name|Room
argument_list|>
name|roomParser
init|=
operator|new
name|FiqlParser
argument_list|<>
argument_list|(
name|Room
operator|.
name|class
argument_list|)
decl_stmt|;
name|SearchCondition
argument_list|<
name|Room
argument_list|>
name|roomCondition
init|=
name|roomParser
operator|.
name|parse
argument_list|(
literal|"furniture.spec.features.description==description"
argument_list|)
decl_stmt|;
name|Room
name|room
init|=
name|roomCondition
operator|.
name|getCondition
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|room
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
class|class
name|Room
block|{
name|Set
argument_list|<
name|Furniture
argument_list|>
name|furniture
decl_stmt|;
specifier|public
name|Set
argument_list|<
name|Furniture
argument_list|>
name|getFurniture
parameter_list|()
block|{
return|return
name|furniture
return|;
block|}
specifier|public
name|void
name|setFurniture
parameter_list|(
name|Set
argument_list|<
name|Furniture
argument_list|>
name|furniture
parameter_list|)
block|{
name|this
operator|.
name|furniture
operator|=
name|furniture
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
class|class
name|Place
block|{
name|Set
argument_list|<
name|Spec
argument_list|>
name|specs
decl_stmt|;
specifier|public
name|Set
argument_list|<
name|Spec
argument_list|>
name|getSpecs
parameter_list|()
block|{
return|return
name|specs
return|;
block|}
specifier|public
name|void
name|setSpecs
parameter_list|(
name|Set
argument_list|<
name|Spec
argument_list|>
name|specs
parameter_list|)
block|{
name|this
operator|.
name|specs
operator|=
name|specs
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
class|class
name|Furniture
block|{
name|Spec
name|spec
decl_stmt|;
specifier|public
name|Spec
name|getSpec
parameter_list|()
block|{
return|return
name|spec
return|;
block|}
specifier|public
name|void
name|setSpec
parameter_list|(
name|Spec
name|spec
parameter_list|)
block|{
name|this
operator|.
name|spec
operator|=
name|spec
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
class|class
name|Spec
block|{
name|Set
argument_list|<
name|Feature
argument_list|>
name|features
decl_stmt|;
specifier|public
name|Set
argument_list|<
name|Feature
argument_list|>
name|getFeatures
parameter_list|()
block|{
return|return
name|features
return|;
block|}
specifier|public
name|void
name|setFeatures
parameter_list|(
name|Set
argument_list|<
name|Feature
argument_list|>
name|features
parameter_list|)
block|{
name|this
operator|.
name|features
operator|=
name|features
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
class|class
name|Feature
block|{
name|String
name|description
decl_stmt|;
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

