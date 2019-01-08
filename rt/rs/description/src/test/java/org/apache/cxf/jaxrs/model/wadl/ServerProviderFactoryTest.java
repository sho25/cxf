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
operator|.
name|wadl
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|container
operator|.
name|ContainerRequestContext
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
name|container
operator|.
name|ContainerRequestFilter
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
name|container
operator|.
name|PreMatching
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
name|AbstractResourceInfo
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
name|provider
operator|.
name|ServerProviderFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|assertSame
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
name|ServerProviderFactoryTest
block|{
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|ServerProviderFactory
operator|.
name|getInstance
argument_list|()
operator|.
name|clearProviders
argument_list|()
expr_stmt|;
name|AbstractResourceInfo
operator|.
name|clearAllMaps
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCustomWadlHandler
parameter_list|()
block|{
name|ServerProviderFactory
name|pf
init|=
name|ServerProviderFactory
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|pf
operator|.
name|getPreMatchContainerRequestFilters
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|pf
operator|.
name|getPreMatchContainerRequestFilters
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getProvider
argument_list|()
operator|instanceof
name|WadlGenerator
argument_list|)
expr_stmt|;
name|WadlGenerator
name|wg
init|=
operator|new
name|WadlGenerator
argument_list|()
decl_stmt|;
name|pf
operator|.
name|setUserProviders
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|wg
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|pf
operator|.
name|getPreMatchContainerRequestFilters
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|pf
operator|.
name|getPreMatchContainerRequestFilters
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getProvider
argument_list|()
operator|instanceof
name|WadlGenerator
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|wg
argument_list|,
name|pf
operator|.
name|getPreMatchContainerRequestFilters
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getProvider
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCustomTestHandler
parameter_list|()
block|{
name|ServerProviderFactory
name|pf
init|=
name|ServerProviderFactory
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|pf
operator|.
name|getPreMatchContainerRequestFilters
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|pf
operator|.
name|getPreMatchContainerRequestFilters
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getProvider
argument_list|()
operator|instanceof
name|WadlGenerator
argument_list|)
expr_stmt|;
name|TestHandler
name|th
init|=
operator|new
name|TestHandler
argument_list|()
decl_stmt|;
name|pf
operator|.
name|setUserProviders
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|th
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|pf
operator|.
name|getPreMatchContainerRequestFilters
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|pf
operator|.
name|getPreMatchContainerRequestFilters
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getProvider
argument_list|()
operator|instanceof
name|WadlGenerator
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|th
argument_list|,
name|pf
operator|.
name|getPreMatchContainerRequestFilters
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getProvider
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCustomTestAndWadlHandler
parameter_list|()
block|{
name|ServerProviderFactory
name|pf
init|=
name|ServerProviderFactory
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|pf
operator|.
name|getPreMatchContainerRequestFilters
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|pf
operator|.
name|getPreMatchContainerRequestFilters
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getProvider
argument_list|()
operator|instanceof
name|WadlGenerator
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|providers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|WadlGenerator
name|wg
init|=
operator|new
name|WadlGenerator
argument_list|()
decl_stmt|;
name|providers
operator|.
name|add
argument_list|(
name|wg
argument_list|)
expr_stmt|;
name|TestHandler
name|th
init|=
operator|new
name|TestHandler
argument_list|()
decl_stmt|;
name|providers
operator|.
name|add
argument_list|(
name|th
argument_list|)
expr_stmt|;
name|pf
operator|.
name|setUserProviders
argument_list|(
name|providers
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|pf
operator|.
name|getPreMatchContainerRequestFilters
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|wg
argument_list|,
name|pf
operator|.
name|getPreMatchContainerRequestFilters
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getProvider
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|th
argument_list|,
name|pf
operator|.
name|getPreMatchContainerRequestFilters
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getProvider
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|PreMatching
specifier|private
specifier|static
class|class
name|TestHandler
implements|implements
name|ContainerRequestFilter
block|{
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ContainerRequestContext
name|context
parameter_list|)
block|{
comment|// complete
block|}
block|}
block|}
end_class

end_unit

