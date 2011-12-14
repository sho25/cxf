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
name|jca
operator|.
name|cxf
operator|.
name|handlers
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
name|Method
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
name|Bus
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
name|jca
operator|.
name|cxf
operator|.
name|CXFInvocationHandler
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
name|jca
operator|.
name|cxf
operator|.
name|CXFManagedConnection
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
name|jca
operator|.
name|cxf
operator|.
name|ManagedConnectionFactoryImpl
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
name|jca
operator|.
name|cxf
operator|.
name|ManagedConnectionImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
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
name|HandlerTestBase
extends|extends
name|Assert
block|{
specifier|protected
name|Bus
name|mockBus
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|CXFManagedConnection
name|mockManagedConnection
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|CXFManagedConnection
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|CXFInvocationHandler
name|mockHandler
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|CXFInvocationHandler
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|ManagedConnectionFactoryImpl
name|mcf
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|ManagedConnectionFactoryImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|ManagedConnectionImpl
name|mci
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|ManagedConnectionImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|Method
name|testMethod
decl_stmt|;
specifier|protected
name|TestTarget
name|target
init|=
operator|new
name|TestTarget
argument_list|()
decl_stmt|;
specifier|public
name|HandlerTestBase
parameter_list|()
block|{     }
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|EasyMock
operator|.
name|reset
argument_list|(
name|mcf
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|reset
argument_list|(
name|mci
argument_list|)
expr_stmt|;
name|mcf
operator|.
name|getBus
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|mockBus
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|mcf
argument_list|)
expr_stmt|;
name|mci
operator|.
name|getManagedConnectionFactory
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|mcf
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|mci
argument_list|)
expr_stmt|;
try|try
block|{
name|testMethod
operator|=
name|TestTarget
operator|.
name|class
operator|.
name|getMethod
argument_list|(
literal|"testMethod"
argument_list|,
operator|new
name|Class
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|ex
parameter_list|)
block|{
name|fail
argument_list|(
name|ex
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNullTestTarget
parameter_list|()
block|{
comment|// do nothing here ,just for avoid the junit test warning
block|}
block|}
end_class

end_unit

