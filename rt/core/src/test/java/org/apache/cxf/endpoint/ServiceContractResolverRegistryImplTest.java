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
name|endpoint
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|classextension
operator|.
name|EasyMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|classextension
operator|.
name|IMocksControl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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

begin_class
specifier|public
class|class
name|ServiceContractResolverRegistryImplTest
extends|extends
name|Assert
block|{
specifier|private
name|ServiceContractResolverRegistryImpl
name|registry
decl_stmt|;
specifier|private
name|ServiceContractResolver
name|resolver1
decl_stmt|;
specifier|private
name|ServiceContractResolver
name|resolver2
decl_stmt|;
specifier|private
name|URI
name|uri1
decl_stmt|;
specifier|private
name|URI
name|uri2
decl_stmt|;
specifier|private
name|IMocksControl
name|control
decl_stmt|;
specifier|private
name|QName
name|serviceName
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|registry
operator|=
operator|new
name|ServiceContractResolverRegistryImpl
argument_list|()
expr_stmt|;
name|control
operator|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
expr_stmt|;
name|resolver1
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|ServiceContractResolver
operator|.
name|class
argument_list|)
expr_stmt|;
name|resolver2
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|ServiceContractResolver
operator|.
name|class
argument_list|)
expr_stmt|;
name|uri1
operator|=
operator|new
name|URI
argument_list|(
literal|"http://mock"
argument_list|)
expr_stmt|;
name|uri2
operator|=
operator|new
name|URI
argument_list|(
literal|"file:///foo/bar"
argument_list|)
expr_stmt|;
name|serviceName
operator|=
operator|new
name|QName
argument_list|(
literal|"namespace"
argument_list|,
literal|"local"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|resolver1
operator|=
literal|null
expr_stmt|;
name|resolver2
operator|=
literal|null
expr_stmt|;
name|serviceName
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRegister
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"unexpected resolver count"
argument_list|,
literal|0
argument_list|,
name|registry
operator|.
name|getResolvers
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|registry
operator|.
name|register
argument_list|(
name|resolver1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected resolver count"
argument_list|,
literal|1
argument_list|,
name|registry
operator|.
name|getResolvers
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"expected resolver to be registered"
argument_list|,
name|registry
operator|.
name|getResolvers
argument_list|()
operator|.
name|contains
argument_list|(
name|resolver1
argument_list|)
argument_list|)
expr_stmt|;
name|registry
operator|.
name|unregister
argument_list|(
name|resolver1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected resolver count"
argument_list|,
literal|0
argument_list|,
name|registry
operator|.
name|getResolvers
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"expected resolver to be registered"
argument_list|,
name|registry
operator|.
name|getResolvers
argument_list|()
operator|.
name|contains
argument_list|(
name|resolver1
argument_list|)
argument_list|)
expr_stmt|;
name|registry
operator|.
name|register
argument_list|(
name|resolver2
argument_list|)
expr_stmt|;
name|registry
operator|.
name|register
argument_list|(
name|resolver1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected resolver count"
argument_list|,
literal|2
argument_list|,
name|registry
operator|.
name|getResolvers
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"expected resolver to be registered"
argument_list|,
name|registry
operator|.
name|getResolvers
argument_list|()
operator|.
name|contains
argument_list|(
name|resolver1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"expected resolver to be registered"
argument_list|,
name|registry
operator|.
name|getResolvers
argument_list|()
operator|.
name|contains
argument_list|(
name|resolver2
argument_list|)
argument_list|)
expr_stmt|;
name|registry
operator|.
name|unregister
argument_list|(
name|resolver2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected resolver count"
argument_list|,
literal|1
argument_list|,
name|registry
operator|.
name|getResolvers
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"expected resolver to be registered"
argument_list|,
name|registry
operator|.
name|getResolvers
argument_list|()
operator|.
name|contains
argument_list|(
name|resolver1
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"expected resolver to be registered"
argument_list|,
name|registry
operator|.
name|getResolvers
argument_list|()
operator|.
name|contains
argument_list|(
name|resolver2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetContactLocation
parameter_list|()
block|{
name|registry
operator|.
name|register
argument_list|(
name|resolver1
argument_list|)
expr_stmt|;
name|registry
operator|.
name|register
argument_list|(
name|resolver2
argument_list|)
expr_stmt|;
name|resolver1
operator|.
name|getContractLocation
argument_list|(
name|serviceName
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|uri1
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|URI
name|resolved
init|=
name|registry
operator|.
name|getContractLocation
argument_list|(
name|serviceName
argument_list|)
decl_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
name|assertSame
argument_list|(
literal|"unexpected physical EPR"
argument_list|,
name|uri1
argument_list|,
name|resolved
argument_list|)
expr_stmt|;
name|control
operator|.
name|reset
argument_list|()
expr_stmt|;
name|resolver1
operator|.
name|getContractLocation
argument_list|(
name|serviceName
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|resolver2
operator|.
name|getContractLocation
argument_list|(
name|serviceName
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|uri2
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|resolved
operator|=
name|registry
operator|.
name|getContractLocation
argument_list|(
name|serviceName
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
name|assertSame
argument_list|(
literal|"unexpected physical EPR"
argument_list|,
name|uri2
argument_list|,
name|resolved
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
literal|"unexpected physical EPR"
argument_list|,
name|uri1
argument_list|,
name|resolved
argument_list|)
expr_stmt|;
name|control
operator|.
name|reset
argument_list|()
expr_stmt|;
name|resolver1
operator|.
name|getContractLocation
argument_list|(
name|serviceName
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|resolver2
operator|.
name|getContractLocation
argument_list|(
name|serviceName
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|resolved
operator|=
name|registry
operator|.
name|getContractLocation
argument_list|(
name|serviceName
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
literal|"unexpected physical EPR"
argument_list|,
name|resolved
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

