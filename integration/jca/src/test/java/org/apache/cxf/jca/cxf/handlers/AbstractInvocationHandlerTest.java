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
name|CXFManagedConnectionFactory
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
specifier|abstract
class|class
name|AbstractInvocationHandlerTest
extends|extends
name|HandlerTestBase
block|{
specifier|public
name|AbstractInvocationHandlerTest
parameter_list|()
block|{           }
comment|// seach for the setNext method
annotation|@
name|Test
specifier|public
name|void
name|testHandlerInvokesNext
parameter_list|()
throws|throws
name|Throwable
block|{
name|Object
index|[]
name|args
init|=
operator|new
name|Object
index|[
literal|0
index|]
decl_stmt|;
name|CXFInvocationHandler
name|handler
init|=
name|getHandler
argument_list|()
decl_stmt|;
name|handler
operator|.
name|setNext
argument_list|(
name|mockHandler
argument_list|)
expr_stmt|;
name|handler
operator|.
name|invoke
argument_list|(
name|target
argument_list|,
name|testMethod
argument_list|,
name|args
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"target object must not be called"
argument_list|,
operator|!
name|target
operator|.
name|methodInvoked
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTargetAttribute
parameter_list|()
block|{
name|CXFInvocationHandler
name|handler
init|=
name|getHandler
argument_list|()
decl_stmt|;
name|handler
operator|.
name|getData
argument_list|()
operator|.
name|setTarget
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"target must be retrievable after set"
argument_list|,
name|target
argument_list|,
name|handler
operator|.
name|getData
argument_list|()
operator|.
name|getTarget
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBusAttribute
parameter_list|()
block|{
name|CXFInvocationHandler
name|handler
init|=
name|getHandler
argument_list|()
decl_stmt|;
name|handler
operator|.
name|getData
argument_list|()
operator|.
name|setBus
argument_list|(
name|mockBus
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"bus must be retrievable after set"
argument_list|,
name|mockBus
argument_list|,
name|handler
operator|.
name|getData
argument_list|()
operator|.
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testManagedConnectionAttribute
parameter_list|()
block|{
name|CXFInvocationHandler
name|handler
init|=
name|getHandler
argument_list|()
decl_stmt|;
name|handler
operator|.
name|getData
argument_list|()
operator|.
name|setManagedConnection
argument_list|(
name|mockManagedConnection
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"bus must be retrievable after set"
argument_list|,
name|mockManagedConnection
argument_list|,
name|handler
operator|.
name|getData
argument_list|()
operator|.
name|getManagedConnection
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|CXFInvocationHandler
name|getNextHandler
parameter_list|()
block|{
return|return
operator|(
name|CXFInvocationHandler
operator|)
name|mockHandler
return|;
block|}
specifier|protected
specifier|abstract
name|CXFInvocationHandler
name|getHandler
parameter_list|()
function_decl|;
specifier|protected
name|CXFManagedConnectionFactory
name|getTestManagedConnectionFactory
parameter_list|()
block|{
return|return
operator|(
name|CXFManagedConnectionFactory
operator|)
name|mcf
return|;
block|}
block|}
end_class

end_unit

