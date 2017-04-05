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
name|ApplicationPath
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
name|GET
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
name|Path
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
name|PathParam
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
name|Application
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
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
name|BusFactory
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
name|JAXRSServerFactoryBean
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
argument_list|<
name|?
argument_list|>
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
argument_list|<>
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
literal|null
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
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
name|testUserResourceFromFile
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|UserResource
argument_list|>
name|list
init|=
name|ResourceUtils
operator|.
name|getUserResources
argument_list|(
literal|"classpath:/resources.xml"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|UserResource
name|resource
init|=
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"java.util.Map"
argument_list|,
name|resource
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"map"
argument_list|,
name|resource
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"application/xml"
argument_list|,
name|resource
operator|.
name|getProduces
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"application/json"
argument_list|,
name|resource
operator|.
name|getConsumes
argument_list|()
argument_list|)
expr_stmt|;
name|UserOperation
name|oper
init|=
name|resource
operator|.
name|getOperations
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"putAll"
argument_list|,
name|oper
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/putAll"
argument_list|,
name|oper
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"PUT"
argument_list|,
name|oper
operator|.
name|getVerb
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"application/json"
argument_list|,
name|oper
operator|.
name|getProduces
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"application/xml"
argument_list|,
name|oper
operator|.
name|getConsumes
argument_list|()
argument_list|)
expr_stmt|;
name|Parameter
name|p
init|=
name|oper
operator|.
name|getParameters
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"map"
argument_list|,
name|p
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"emptyMap"
argument_list|,
name|p
operator|.
name|getDefaultValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|p
operator|.
name|isEncoded
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"REQUEST_BODY"
argument_list|,
name|p
operator|.
name|getType
argument_list|()
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
operator|.
name|getAllTypes
argument_list|()
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
annotation|@
name|Test
specifier|public
name|void
name|testGetAllJaxbClasses2
parameter_list|()
block|{
name|ClassResourceInfo
name|cri1
init|=
name|ResourceUtils
operator|.
name|createClassResourceInfo
argument_list|(
name|IProductResource
operator|.
name|class
argument_list|,
name|IProductResource
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
operator|.
name|getAllTypes
argument_list|()
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
annotation|@
name|Test
specifier|public
name|void
name|testGetAllJaxbClassesComplexGenericType
parameter_list|()
block|{
name|ClassResourceInfo
name|cri1
init|=
name|ResourceUtils
operator|.
name|createClassResourceInfo
argument_list|(
name|OrderResource
operator|.
name|class
argument_list|,
name|OrderResource
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
operator|.
name|getAllTypes
argument_list|()
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
name|OrderItemsDTO
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
name|OrderItemDTO
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testClassResourceInfoWithOverride
parameter_list|()
throws|throws
name|Exception
block|{
name|ClassResourceInfo
name|cri
init|=
name|ResourceUtils
operator|.
name|createClassResourceInfo
argument_list|(
name|ExampleImpl
operator|.
name|class
argument_list|,
name|ExampleImpl
operator|.
name|class
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
name|Method
name|m
init|=
name|ExampleImpl
operator|.
name|class
operator|.
name|getMethod
argument_list|(
literal|"get"
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
name|m
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|ori
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"GET"
argument_list|,
name|ori
operator|.
name|getHttpMethod
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|shouldCreateApplicationWhichInheritsApplicationPath
parameter_list|()
throws|throws
name|Exception
block|{
name|JAXRSServerFactoryBean
name|application
init|=
name|ResourceUtils
operator|.
name|createApplication
argument_list|(
operator|new
name|SuperApplication
argument_list|()
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"/base"
argument_list|,
name|application
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|shouldCreateApplicationWhichOverridesApplicationPath
parameter_list|()
throws|throws
name|Exception
block|{
name|JAXRSServerFactoryBean
name|application
init|=
name|ResourceUtils
operator|.
name|createApplication
argument_list|(
operator|new
name|CustomApplication
argument_list|()
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"/custom"
argument_list|,
name|application
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
interface|interface
name|IProductResource
block|{
annotation|@
name|Path
argument_list|(
literal|"/parts"
argument_list|)
name|IPartsResource
name|getParts
parameter_list|()
function_decl|;
block|}
specifier|public
interface|interface
name|IPartsResource
block|{
annotation|@
name|Path
argument_list|(
literal|"/{i}/"
argument_list|)
name|IPartsResource2
name|elementAt
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"i"
argument_list|)
name|String
name|i
parameter_list|)
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"/parts"
argument_list|)
name|IPartsResource
name|getParts
parameter_list|()
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"/products"
argument_list|)
name|IProductResource
name|getProducts
parameter_list|()
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"chapter"
argument_list|)
name|Chapter
name|get
parameter_list|()
function_decl|;
block|}
specifier|public
interface|interface
name|IPartsResource2
block|{
annotation|@
name|Path
argument_list|(
literal|"/{i}/"
argument_list|)
name|IPartsResource
name|elementAt
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"i"
argument_list|)
name|String
name|i
parameter_list|)
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"/products"
argument_list|)
name|IProductResource
name|getProducts
parameter_list|()
function_decl|;
annotation|@
name|GET
name|Book
name|get
parameter_list|()
function_decl|;
block|}
annotation|@
name|Path
argument_list|(
literal|"example"
argument_list|)
specifier|public
interface|interface
name|Example
block|{
annotation|@
name|GET
name|Book
name|get
parameter_list|()
function_decl|;
block|}
specifier|public
specifier|static
class|class
name|ExampleImpl
implements|implements
name|Example
block|{
annotation|@
name|Override
specifier|public
name|Book
name|get
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|XmlRootElement
specifier|public
specifier|static
class|class
name|OrderItem
block|{      }
annotation|@
name|XmlRootElement
specifier|public
specifier|static
class|class
name|OrderItemDTO
parameter_list|<
name|T
parameter_list|>
block|{      }
annotation|@
name|XmlRootElement
specifier|public
specifier|static
class|class
name|OrderItemsDTO
parameter_list|<
name|E
parameter_list|>
block|{      }
specifier|public
specifier|static
class|class
name|OrderResource
block|{
annotation|@
name|GET
specifier|public
name|OrderItemsDTO
argument_list|<
name|?
extends|extends
name|OrderItemDTO
argument_list|<
name|?
extends|extends
name|OrderItem
argument_list|>
argument_list|>
name|getOrders
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|ApplicationPath
argument_list|(
literal|"/base"
argument_list|)
specifier|private
specifier|static
class|class
name|BaseApplication
extends|extends
name|Application
block|{      }
specifier|private
specifier|static
class|class
name|SuperApplication
extends|extends
name|BaseApplication
block|{      }
annotation|@
name|ApplicationPath
argument_list|(
literal|"/custom"
argument_list|)
specifier|private
specifier|static
class|class
name|CustomApplication
extends|extends
name|BaseApplication
block|{      }
block|}
end_class

end_unit

