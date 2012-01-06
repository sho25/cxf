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
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|resource
operator|.
name|spi
operator|.
name|ResourceAdapterInternalException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|Subject
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
name|CXFInvocationHandlerData
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
name|InvocationHandlerFactoryTest
extends|extends
name|HandlerTestBase
block|{
specifier|private
name|CXFInvocationHandler
name|handler
decl_stmt|;
specifier|private
name|Subject
name|testSubject
decl_stmt|;
specifier|public
name|InvocationHandlerFactoryTest
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testSubject
operator|=
operator|new
name|Subject
argument_list|()
expr_stmt|;
try|try
block|{
name|InvocationHandlerFactory
name|factory
init|=
operator|new
name|InvocationHandlerFactory
argument_list|(
name|mockBus
argument_list|,
name|mci
argument_list|)
decl_stmt|;
name|handler
operator|=
name|factory
operator|.
name|createHandlers
argument_list|(
name|target
argument_list|,
name|testSubject
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResourceAdapterInternalException
name|e
parameter_list|)
block|{
name|fail
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateHandlerChain
parameter_list|()
throws|throws
name|ResourceAdapterInternalException
block|{
name|CXFInvocationHandler
name|first
init|=
name|handler
decl_stmt|;
name|CXFInvocationHandler
name|last
init|=
literal|null
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"handler must not be null"
argument_list|,
name|handler
argument_list|)
expr_stmt|;
name|int
name|count
init|=
literal|0
decl_stmt|;
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|allHandlerTypes
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
while|while
condition|(
name|handler
operator|!=
literal|null
condition|)
block|{
name|assertSame
argument_list|(
literal|"managed connection must be set"
argument_list|,
name|mci
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
name|assertSame
argument_list|(
literal|"bus must be set"
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
name|assertSame
argument_list|(
literal|"subject must be set"
argument_list|,
name|testSubject
argument_list|,
name|handler
operator|.
name|getData
argument_list|()
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"target must be set"
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
name|allHandlerTypes
operator|.
name|add
argument_list|(
name|handler
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|last
operator|=
name|handler
expr_stmt|;
name|handler
operator|=
name|handler
operator|.
name|getNext
argument_list|()
expr_stmt|;
name|count
operator|++
expr_stmt|;
block|}
name|assertNotNull
argument_list|(
name|last
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"must create correct number of handlers"
argument_list|,
name|count
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"first handler must a ProxyInvocationHandler"
argument_list|,
name|first
operator|instanceof
name|ProxyInvocationHandler
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"last handler must be an InvokingInvocationHandler"
argument_list|,
name|last
operator|instanceof
name|InvokingInvocationHandler
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|types
init|=
block|{
name|ProxyInvocationHandler
operator|.
name|class
block|,
name|ObjectMethodInvocationHandler
operator|.
name|class
block|,
name|InvokingInvocationHandler
operator|.
name|class
block|,
name|SecurityTestHandler
operator|.
name|class
block|}
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|types
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|assertTrue
argument_list|(
literal|"handler chain must contain type: "
operator|+
name|types
index|[
name|i
index|]
argument_list|,
name|allHandlerTypes
operator|.
name|contains
argument_list|(
name|types
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOrderedHandlerChain
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|ProxyInvocationHandler
operator|.
name|class
argument_list|,
name|handler
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ObjectMethodInvocationHandler
operator|.
name|class
argument_list|,
name|handler
operator|.
name|getNext
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SecurityTestHandler
operator|.
name|class
argument_list|,
name|handler
operator|.
name|getNext
argument_list|()
operator|.
name|getNext
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
class|class
name|SecurityTestHandler
extends|extends
name|CXFInvocationHandlerBase
block|{
specifier|public
name|SecurityTestHandler
parameter_list|(
name|CXFInvocationHandlerData
name|data
parameter_list|)
block|{
name|super
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Object
name|invoke
parameter_list|(
name|Object
name|proxy
parameter_list|,
name|Method
name|method
parameter_list|,
name|Object
index|[]
name|args
parameter_list|)
throws|throws
name|Throwable
block|{
return|return
name|invokeNext
argument_list|(
name|proxy
argument_list|,
name|method
argument_list|,
name|args
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

