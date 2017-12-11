begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|odata
operator|.
name|server
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|olingo
operator|.
name|commons
operator|.
name|api
operator|.
name|edm
operator|.
name|EdmPrimitiveTypeKind
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|commons
operator|.
name|api
operator|.
name|edm
operator|.
name|FullQualifiedName
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|commons
operator|.
name|api
operator|.
name|edm
operator|.
name|provider
operator|.
name|CsdlAbstractEdmProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|commons
operator|.
name|api
operator|.
name|edm
operator|.
name|provider
operator|.
name|CsdlEntityContainer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|commons
operator|.
name|api
operator|.
name|edm
operator|.
name|provider
operator|.
name|CsdlEntityContainerInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|commons
operator|.
name|api
operator|.
name|edm
operator|.
name|provider
operator|.
name|CsdlEntitySet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|commons
operator|.
name|api
operator|.
name|edm
operator|.
name|provider
operator|.
name|CsdlEntityType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|commons
operator|.
name|api
operator|.
name|edm
operator|.
name|provider
operator|.
name|CsdlProperty
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|commons
operator|.
name|api
operator|.
name|edm
operator|.
name|provider
operator|.
name|CsdlPropertyRef
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|commons
operator|.
name|api
operator|.
name|edm
operator|.
name|provider
operator|.
name|CsdlSchema
import|;
end_import

begin_comment
comment|/**  * this class is supposed to declare the metadata of the OData service  * it is invoked by the Olingo framework e.g. when the metadata document of the service is invoked  * e.g. http://localhost:8080/ExampleService1/ExampleService1.svc/$metadata  */
end_comment

begin_class
specifier|public
class|class
name|DemoEdmProvider
extends|extends
name|CsdlAbstractEdmProvider
block|{
comment|// Service Namespace
specifier|public
specifier|static
specifier|final
name|String
name|NAMESPACE
init|=
literal|"OData.Demo"
decl_stmt|;
comment|// EDM Container
specifier|public
specifier|static
specifier|final
name|String
name|CONTAINER_NAME
init|=
literal|"Container"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|FullQualifiedName
name|CONTAINER
init|=
operator|new
name|FullQualifiedName
argument_list|(
name|NAMESPACE
argument_list|,
name|CONTAINER_NAME
argument_list|)
decl_stmt|;
comment|// Entity Types Names
specifier|public
specifier|static
specifier|final
name|String
name|ET_PRODUCT_NAME
init|=
literal|"Product"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|FullQualifiedName
name|ET_PRODUCT_FQN
init|=
operator|new
name|FullQualifiedName
argument_list|(
name|NAMESPACE
argument_list|,
name|ET_PRODUCT_NAME
argument_list|)
decl_stmt|;
comment|// Entity Set Names
specifier|public
specifier|static
specifier|final
name|String
name|ES_PRODUCTS_NAME
init|=
literal|"Products"
decl_stmt|;
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|CsdlSchema
argument_list|>
name|getSchemas
parameter_list|()
block|{
comment|// create Schema
name|CsdlSchema
name|schema
init|=
operator|new
name|CsdlSchema
argument_list|()
decl_stmt|;
name|schema
operator|.
name|setNamespace
argument_list|(
name|NAMESPACE
argument_list|)
expr_stmt|;
comment|// add EntityTypes
name|List
argument_list|<
name|CsdlEntityType
argument_list|>
name|entityTypes
init|=
operator|new
name|ArrayList
argument_list|<
name|CsdlEntityType
argument_list|>
argument_list|()
decl_stmt|;
name|entityTypes
operator|.
name|add
argument_list|(
name|getEntityType
argument_list|(
name|ET_PRODUCT_FQN
argument_list|)
argument_list|)
expr_stmt|;
name|schema
operator|.
name|setEntityTypes
argument_list|(
name|entityTypes
argument_list|)
expr_stmt|;
comment|// add EntityContainer
name|schema
operator|.
name|setEntityContainer
argument_list|(
name|getEntityContainer
argument_list|()
argument_list|)
expr_stmt|;
comment|// finally
name|List
argument_list|<
name|CsdlSchema
argument_list|>
name|schemas
init|=
operator|new
name|ArrayList
argument_list|<
name|CsdlSchema
argument_list|>
argument_list|()
decl_stmt|;
name|schemas
operator|.
name|add
argument_list|(
name|schema
argument_list|)
expr_stmt|;
return|return
name|schemas
return|;
block|}
annotation|@
name|Override
specifier|public
name|CsdlEntityType
name|getEntityType
parameter_list|(
name|FullQualifiedName
name|entityTypeName
parameter_list|)
block|{
comment|// this method is called for one of the EntityTypes that are configured in the Schema
if|if
condition|(
name|entityTypeName
operator|.
name|equals
argument_list|(
name|ET_PRODUCT_FQN
argument_list|)
condition|)
block|{
comment|//create EntityType properties
name|CsdlProperty
name|id
init|=
operator|new
name|CsdlProperty
argument_list|()
operator|.
name|setName
argument_list|(
literal|"ID"
argument_list|)
operator|.
name|setType
argument_list|(
name|EdmPrimitiveTypeKind
operator|.
name|Int32
operator|.
name|getFullQualifiedName
argument_list|()
argument_list|)
decl_stmt|;
name|CsdlProperty
name|name
init|=
operator|new
name|CsdlProperty
argument_list|()
operator|.
name|setName
argument_list|(
literal|"Name"
argument_list|)
operator|.
name|setType
argument_list|(
name|EdmPrimitiveTypeKind
operator|.
name|String
operator|.
name|getFullQualifiedName
argument_list|()
argument_list|)
decl_stmt|;
name|CsdlProperty
name|description
init|=
operator|new
name|CsdlProperty
argument_list|()
operator|.
name|setName
argument_list|(
literal|"Description"
argument_list|)
operator|.
name|setType
argument_list|(
name|EdmPrimitiveTypeKind
operator|.
name|String
operator|.
name|getFullQualifiedName
argument_list|()
argument_list|)
decl_stmt|;
comment|// create CsdlPropertyRef for Key element
name|CsdlPropertyRef
name|propertyRef
init|=
operator|new
name|CsdlPropertyRef
argument_list|()
decl_stmt|;
name|propertyRef
operator|.
name|setName
argument_list|(
literal|"ID"
argument_list|)
expr_stmt|;
comment|// configure EntityType
name|CsdlEntityType
name|entityType
init|=
operator|new
name|CsdlEntityType
argument_list|()
decl_stmt|;
name|entityType
operator|.
name|setName
argument_list|(
name|ET_PRODUCT_NAME
argument_list|)
expr_stmt|;
name|entityType
operator|.
name|setProperties
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|id
argument_list|,
name|name
argument_list|,
name|description
argument_list|)
argument_list|)
expr_stmt|;
name|entityType
operator|.
name|setKey
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|propertyRef
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|entityType
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|CsdlEntitySet
name|getEntitySet
parameter_list|(
name|FullQualifiedName
name|entityContainer
parameter_list|,
name|String
name|entitySetName
parameter_list|)
block|{
if|if
condition|(
name|entityContainer
operator|.
name|equals
argument_list|(
name|CONTAINER
argument_list|)
condition|)
block|{
if|if
condition|(
name|entitySetName
operator|.
name|equals
argument_list|(
name|ES_PRODUCTS_NAME
argument_list|)
condition|)
block|{
name|CsdlEntitySet
name|entitySet
init|=
operator|new
name|CsdlEntitySet
argument_list|()
decl_stmt|;
name|entitySet
operator|.
name|setName
argument_list|(
name|ES_PRODUCTS_NAME
argument_list|)
expr_stmt|;
name|entitySet
operator|.
name|setType
argument_list|(
name|ET_PRODUCT_FQN
argument_list|)
expr_stmt|;
return|return
name|entitySet
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|CsdlEntityContainer
name|getEntityContainer
parameter_list|()
block|{
comment|// create EntitySets
name|List
argument_list|<
name|CsdlEntitySet
argument_list|>
name|entitySets
init|=
operator|new
name|ArrayList
argument_list|<
name|CsdlEntitySet
argument_list|>
argument_list|()
decl_stmt|;
name|entitySets
operator|.
name|add
argument_list|(
name|getEntitySet
argument_list|(
name|CONTAINER
argument_list|,
name|ES_PRODUCTS_NAME
argument_list|)
argument_list|)
expr_stmt|;
comment|// create EntityContainer
name|CsdlEntityContainer
name|entityContainer
init|=
operator|new
name|CsdlEntityContainer
argument_list|()
decl_stmt|;
name|entityContainer
operator|.
name|setName
argument_list|(
name|CONTAINER_NAME
argument_list|)
expr_stmt|;
name|entityContainer
operator|.
name|setEntitySets
argument_list|(
name|entitySets
argument_list|)
expr_stmt|;
return|return
name|entityContainer
return|;
block|}
annotation|@
name|Override
specifier|public
name|CsdlEntityContainerInfo
name|getEntityContainerInfo
parameter_list|(
name|FullQualifiedName
name|entityContainerName
parameter_list|)
block|{
comment|// This method is invoked when displaying the service document at e.g. http://localhost:8080/DemoService/DemoService.svc
if|if
condition|(
name|entityContainerName
operator|==
literal|null
operator|||
name|entityContainerName
operator|.
name|equals
argument_list|(
name|CONTAINER
argument_list|)
condition|)
block|{
name|CsdlEntityContainerInfo
name|entityContainerInfo
init|=
operator|new
name|CsdlEntityContainerInfo
argument_list|()
decl_stmt|;
name|entityContainerInfo
operator|.
name|setContainerName
argument_list|(
name|CONTAINER
argument_list|)
expr_stmt|;
return|return
name|entityContainerInfo
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit
