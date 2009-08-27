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
name|utils
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Constructor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
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
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

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
name|UriInfo
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
name|Customer
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
name|model
operator|.
name|ClassResourceInfo
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
name|model
operator|.
name|OperationResourceInfo
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
name|model
operator|.
name|Parameter
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
name|model
operator|.
name|ParameterType
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
name|model
operator|.
name|UserOperation
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
name|model
operator|.
name|UserResource
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
name|resources
operator|.
name|Book
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
name|resources
operator|.
name|BookInterface
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
name|resources
operator|.
name|Chapter
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
name|ResourceUtilsTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testFindResourceConstructor
parameter_list|()
block|{
name|Constructor
name|c
init|=
name|ResourceUtils
operator|.
name|findResourceConstructor
argument_list|(
name|Customer
operator|.
name|class
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|c
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|UriInfo
operator|.
name|class
argument_list|,
name|c
operator|.
name|getParameterTypes
argument_list|()
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|c
operator|.
name|getParameterTypes
argument_list|()
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testClassResourceInfoUserResource
parameter_list|()
throws|throws
name|Exception
block|{
name|UserResource
name|ur
init|=
operator|new
name|UserResource
argument_list|()
decl_stmt|;
name|ur
operator|.
name|setName
argument_list|(
name|HashMap
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|ur
operator|.
name|setPath
argument_list|(
literal|"/hashmap"
argument_list|)
expr_stmt|;
name|UserOperation
name|op
init|=
operator|new
name|UserOperation
argument_list|()
decl_stmt|;
name|op
operator|.
name|setPath
argument_list|(
literal|"/key/{id}"
argument_list|)
expr_stmt|;
name|op
operator|.
name|setName
argument_list|(
literal|"get"
argument_list|)
expr_stmt|;
name|op
operator|.
name|setVerb
argument_list|(
literal|"POST"
argument_list|)
expr_stmt|;
name|op
operator|.
name|setParameters
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|Parameter
argument_list|(
name|ParameterType
operator|.
name|PATH
argument_list|,
literal|"id"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|ur
operator|.
name|setOperations
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|op
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|UserResource
argument_list|>
name|resources
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|UserResource
argument_list|>
argument_list|()
decl_stmt|;
name|resources
operator|.
name|put
argument_list|(
name|ur
operator|.
name|getName
argument_list|()
argument_list|,
name|ur
argument_list|)
expr_stmt|;
name|ClassResourceInfo
name|cri
init|=
name|ResourceUtils
operator|.
name|createClassResourceInfo
argument_list|(
name|resources
argument_list|,
name|ur
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|cri
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/hashmap"
argument_list|,
name|cri
operator|.
name|getURITemplate
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|Method
name|method
init|=
name|HashMap
operator|.
name|class
operator|.
name|getMethod
argument_list|(
literal|"get"
argument_list|,
operator|new
name|Class
index|[]
block|{
name|Object
operator|.
name|class
block|}
argument_list|)
decl_stmt|;
name|OperationResourceInfo
name|ori
init|=
name|cri
operator|.
name|getMethodDispatcher
argument_list|()
operator|.
name|getOperationResourceInfo
argument_list|(
name|method
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|ori
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/key/{id}"
argument_list|,
name|ori
operator|.
name|getURITemplate
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Parameter
argument_list|>
name|params
init|=
name|ori
operator|.
name|getParameters
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|params
argument_list|)
expr_stmt|;
name|Parameter
name|p
init|=
name|params
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"id"
argument_list|,
name|p
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetAllJaxbClasses
parameter_list|()
block|{
name|ClassResourceInfo
name|cri1
init|=
name|ResourceUtils
operator|.
name|createClassResourceInfo
argument_list|(
name|BookInterface
operator|.
name|class
argument_list|,
name|BookInterface
operator|.
name|class
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Type
argument_list|>
name|types
init|=
name|ResourceUtils
operator|.
name|getAllRequestResponseTypes
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|cri1
argument_list|)
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|types
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|types
operator|.
name|containsKey
argument_list|(
name|Book
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|types
operator|.
name|containsKey
argument_list|(
name|Chapter
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

