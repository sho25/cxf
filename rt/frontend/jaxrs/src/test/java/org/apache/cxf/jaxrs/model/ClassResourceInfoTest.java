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
name|model
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
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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
name|Consumes
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
name|HEAD
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
name|Produces
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
name|Context
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
name|HttpHeaders
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
name|utils
operator|.
name|ResourceUtils
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
name|ClassResourceInfoTest
extends|extends
name|Assert
block|{
annotation|@
name|Path
argument_list|(
literal|"/bar"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"test/bar"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"test/foo"
argument_list|)
specifier|static
class|class
name|TestClass
block|{
annotation|@
name|Context
name|UriInfo
name|u
decl_stmt|;
annotation|@
name|Context
name|HttpHeaders
name|h
decl_stmt|;
annotation|@
name|Resource
name|HttpServletRequest
name|req
decl_stmt|;
annotation|@
name|Resource
name|HttpServletResponse
name|res
decl_stmt|;
annotation|@
name|Resource
name|ServletContext
name|c
decl_stmt|;
name|int
name|i
decl_stmt|;
annotation|@
name|GET
specifier|public
name|void
name|getIt
parameter_list|()
block|{                       }
block|}
specifier|static
class|class
name|TestClass1
extends|extends
name|TestClass
block|{
annotation|@
name|GET
specifier|public
name|void
name|getIt
parameter_list|()
block|{                       }
block|}
specifier|static
class|class
name|TestClass2
extends|extends
name|TestClass1
block|{
annotation|@
name|GET
specifier|public
name|void
name|getIt
parameter_list|()
block|{                       }
block|}
specifier|static
class|class
name|TestClass3
block|{
annotation|@
name|Resource
name|HttpServletRequest
name|req
decl_stmt|;
annotation|@
name|Resource
name|HttpServletResponse
name|res
decl_stmt|;
annotation|@
name|Resource
name|ServletContext
name|c
decl_stmt|;
annotation|@
name|GET
specifier|public
name|void
name|getIt
parameter_list|()
block|{                       }
annotation|@
name|HEAD
specifier|public
name|void
name|head
parameter_list|()
block|{                       }
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetHttpContexts
parameter_list|()
block|{
name|ClassResourceInfo
name|c
init|=
operator|new
name|ClassResourceInfo
argument_list|(
name|TestClass
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Field
argument_list|>
name|fields
init|=
name|c
operator|.
name|getContextFields
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Only root classes should check these fields"
argument_list|,
literal|0
argument_list|,
name|fields
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|c
operator|=
operator|new
name|ClassResourceInfo
argument_list|(
name|TestClass
operator|.
name|class
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|fields
operator|=
name|c
operator|.
name|getContextFields
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"2 http context fields available"
argument_list|,
literal|2
argument_list|,
name|fields
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Wrong fields selected"
argument_list|,
operator|(
name|fields
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getType
argument_list|()
operator|==
name|UriInfo
operator|.
name|class
operator|||
name|fields
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getType
argument_list|()
operator|==
name|UriInfo
operator|.
name|class
operator|)
operator|&&
operator|(
name|fields
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getType
argument_list|()
operator|==
name|HttpHeaders
operator|.
name|class
operator|||
name|fields
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getType
argument_list|()
operator|==
name|HttpHeaders
operator|.
name|class
operator|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetResources
parameter_list|()
block|{
name|ClassResourceInfo
name|c
init|=
operator|new
name|ClassResourceInfo
argument_list|(
name|TestClass3
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Field
argument_list|>
name|fields
init|=
name|c
operator|.
name|getResourceFields
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Only root classes should check these fields"
argument_list|,
literal|0
argument_list|,
name|fields
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|c
operator|=
operator|new
name|ClassResourceInfo
argument_list|(
name|TestClass3
operator|.
name|class
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|fields
operator|=
name|c
operator|.
name|getResourceFields
argument_list|()
expr_stmt|;
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|clses
init|=
operator|new
name|HashSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Field
name|f
range|:
name|fields
control|)
block|{
name|clses
operator|.
name|add
argument_list|(
name|f
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|"3 resources fields available"
argument_list|,
literal|3
argument_list|,
name|fields
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Wrong fields selected"
argument_list|,
name|clses
operator|.
name|contains
argument_list|(
name|HttpServletRequest
operator|.
name|class
argument_list|)
operator|&&
name|clses
operator|.
name|contains
argument_list|(
name|HttpServletResponse
operator|.
name|class
argument_list|)
operator|&&
name|clses
operator|.
name|contains
argument_list|(
name|ServletContext
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
name|testGetPath
parameter_list|()
block|{
name|ClassResourceInfo
name|c
init|=
operator|new
name|ClassResourceInfo
argument_list|(
name|TestClass
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"/bar"
argument_list|,
name|c
operator|.
name|getPath
argument_list|()
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|c
operator|=
operator|new
name|ClassResourceInfo
argument_list|(
name|TestClass1
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/bar"
argument_list|,
name|c
operator|.
name|getPath
argument_list|()
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|c
operator|=
operator|new
name|ClassResourceInfo
argument_list|(
name|TestClass2
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/bar"
argument_list|,
name|c
operator|.
name|getPath
argument_list|()
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetProduce
parameter_list|()
block|{
name|ClassResourceInfo
name|c
init|=
operator|new
name|ClassResourceInfo
argument_list|(
name|TestClass
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"test/bar"
argument_list|,
name|c
operator|.
name|getProduceMime
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|c
operator|=
operator|new
name|ClassResourceInfo
argument_list|(
name|TestClass1
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test/bar"
argument_list|,
name|c
operator|.
name|getProduceMime
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|c
operator|=
operator|new
name|ClassResourceInfo
argument_list|(
name|TestClass2
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test/bar"
argument_list|,
name|c
operator|.
name|getProduceMime
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
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
name|testGetConsume
parameter_list|()
block|{
name|ClassResourceInfo
name|c
init|=
operator|new
name|ClassResourceInfo
argument_list|(
name|TestClass
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"test/foo"
argument_list|,
name|c
operator|.
name|getConsumeMime
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|c
operator|=
operator|new
name|ClassResourceInfo
argument_list|(
name|TestClass1
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test/foo"
argument_list|,
name|c
operator|.
name|getConsumeMime
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|c
operator|=
operator|new
name|ClassResourceInfo
argument_list|(
name|TestClass2
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test/foo"
argument_list|,
name|c
operator|.
name|getConsumeMime
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
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
name|testGetSameSubresource
parameter_list|()
block|{
name|ClassResourceInfo
name|c
init|=
operator|new
name|ClassResourceInfo
argument_list|(
name|TestClass
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"No subresources expected"
argument_list|,
literal|0
argument_list|,
name|c
operator|.
name|getSubResources
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|c
operator|.
name|findResource
argument_list|(
name|TestClass
operator|.
name|class
argument_list|,
name|TestClass
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|ClassResourceInfo
name|c1
init|=
name|c
operator|.
name|getSubResource
argument_list|(
name|TestClass
operator|.
name|class
argument_list|,
name|TestClass
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|c1
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|c1
argument_list|,
name|c
operator|.
name|findResource
argument_list|(
name|TestClass
operator|.
name|class
argument_list|,
name|TestClass
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|c1
argument_list|,
name|c
operator|.
name|getSubResource
argument_list|(
name|TestClass
operator|.
name|class
argument_list|,
name|TestClass
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Single subresources expected"
argument_list|,
literal|1
argument_list|,
name|c
operator|.
name|getSubResources
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetSubresourceSubclass
parameter_list|()
block|{
name|ClassResourceInfo
name|c
init|=
operator|new
name|ClassResourceInfo
argument_list|(
name|TestClass
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"No subresources expected"
argument_list|,
literal|0
argument_list|,
name|c
operator|.
name|getSubResources
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|c
operator|.
name|findResource
argument_list|(
name|TestClass
operator|.
name|class
argument_list|,
name|TestClass1
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|ClassResourceInfo
name|c1
init|=
name|c
operator|.
name|getSubResource
argument_list|(
name|TestClass
operator|.
name|class
argument_list|,
name|TestClass1
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|c1
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|c1
argument_list|,
name|c
operator|.
name|findResource
argument_list|(
name|TestClass
operator|.
name|class
argument_list|,
name|TestClass1
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|c
operator|.
name|findResource
argument_list|(
name|TestClass
operator|.
name|class
argument_list|,
name|TestClass2
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|ClassResourceInfo
name|c2
init|=
name|c
operator|.
name|getSubResource
argument_list|(
name|TestClass
operator|.
name|class
argument_list|,
name|TestClass2
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|c2
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|c2
argument_list|,
name|c
operator|.
name|findResource
argument_list|(
name|TestClass
operator|.
name|class
argument_list|,
name|TestClass2
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|c2
argument_list|,
name|c
operator|.
name|getSubResource
argument_list|(
name|TestClass
operator|.
name|class
argument_list|,
name|TestClass2
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|c1
argument_list|,
name|c2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAllowedMethods
parameter_list|()
block|{
name|ClassResourceInfo
name|c
init|=
name|ResourceUtils
operator|.
name|createClassResourceInfo
argument_list|(
name|TestClass3
operator|.
name|class
argument_list|,
name|TestClass3
operator|.
name|class
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|methods
init|=
name|c
operator|.
name|getAllowedMethods
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|methods
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|methods
operator|.
name|contains
argument_list|(
literal|"HEAD"
argument_list|)
operator|&&
name|methods
operator|.
name|contains
argument_list|(
literal|"GET"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

