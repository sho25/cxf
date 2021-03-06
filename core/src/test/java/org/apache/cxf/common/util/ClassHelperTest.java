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
name|common
operator|.
name|util
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
name|InvocationHandler
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
name|Proxy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|cglib
operator|.
name|proxy
operator|.
name|Callback
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|cglib
operator|.
name|proxy
operator|.
name|Enhancer
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
name|BusFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|aop
operator|.
name|AfterReturningAdvice
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|aop
operator|.
name|framework
operator|.
name|ProxyFactory
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
name|After
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
name|assertSame
import|;
end_import

begin_class
specifier|public
class|class
name|ClassHelperTest
block|{
specifier|private
name|Object
name|proxiedObject
decl_stmt|;
specifier|private
name|Object
name|springAopObject
decl_stmt|;
specifier|private
name|Object
name|cglibProxyObject
decl_stmt|;
specifier|private
name|InvocationHandler
name|realObjectInternalProxy
decl_stmt|;
specifier|private
name|Object
name|realObjectInternalSpring
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|Bus
name|currentThreadBus
decl_stmt|;
specifier|private
name|Function
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|fn
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|realObjectInternalProxy
operator|=
operator|new
name|InvocationHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Object
name|invoke
parameter_list|(
name|Object
name|o
parameter_list|,
name|Method
name|method
parameter_list|,
name|Object
index|[]
name|objects
parameter_list|)
throws|throws
name|Throwable
block|{
return|return
literal|null
return|;
block|}
block|}
expr_stmt|;
name|proxiedObject
operator|=
name|Proxy
operator|.
name|newProxyInstance
argument_list|(
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
argument_list|,
operator|new
name|Class
index|[]
block|{
name|AnyInterface
operator|.
name|class
block|}
argument_list|,
name|realObjectInternalProxy
argument_list|)
expr_stmt|;
name|realObjectInternalSpring
operator|=
operator|new
name|Object
argument_list|()
expr_stmt|;
name|ProxyFactory
name|proxyFactory
init|=
operator|new
name|ProxyFactory
argument_list|(
name|realObjectInternalSpring
argument_list|)
decl_stmt|;
name|proxyFactory
operator|.
name|addAdvice
argument_list|(
operator|new
name|AfterReturningAdvice
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|afterReturning
parameter_list|(
name|Object
name|o
parameter_list|,
name|Method
name|method
parameter_list|,
name|Object
index|[]
name|objects
parameter_list|,
name|Object
name|o1
parameter_list|)
throws|throws
name|Throwable
block|{              }
block|}
argument_list|)
expr_stmt|;
name|springAopObject
operator|=
name|proxyFactory
operator|.
name|getProxy
argument_list|()
expr_stmt|;
specifier|final
name|Callback
name|callback
init|=
operator|new
name|net
operator|.
name|sf
operator|.
name|cglib
operator|.
name|proxy
operator|.
name|InvocationHandler
argument_list|()
block|{
annotation|@
name|Override
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
literal|null
return|;
block|}
block|}
decl_stmt|;
name|cglibProxyObject
operator|=
name|Enhancer
operator|.
name|create
argument_list|(
name|Object
operator|.
name|class
argument_list|,
operator|new
name|Class
index|[]
block|{
name|Function
operator|.
name|class
block|}
argument_list|,
name|callback
argument_list|)
expr_stmt|;
name|fn
operator|=
name|Integer
operator|::
name|parseInt
expr_stmt|;
name|currentThreadBus
operator|=
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|()
expr_stmt|;
name|bus
operator|=
name|EasyMock
operator|.
name|mock
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|bus
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
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|currentThreadBus
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|getRealClassPropertyWasSetInBus
parameter_list|()
block|{
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getProperty
argument_list|(
name|ClassHelper
operator|.
name|USE_DEFAULT_CLASS_HELPER
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getProperty
argument_list|(
name|ClassUnwrapper
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|realObjectInternalProxy
operator|.
name|getClass
argument_list|()
argument_list|,
name|ClassHelper
operator|.
name|getRealClass
argument_list|(
name|proxiedObject
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|getRealClassPropertyWasNotSetInBus
parameter_list|()
block|{
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getProperty
argument_list|(
name|ClassHelper
operator|.
name|USE_DEFAULT_CLASS_HELPER
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getProperty
argument_list|(
name|ClassUnwrapper
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|realObjectInternalSpring
operator|.
name|getClass
argument_list|()
argument_list|,
name|ClassHelper
operator|.
name|getRealClass
argument_list|(
name|springAopObject
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|getRealClassFromClassPropertyWasSetInBus
parameter_list|()
block|{
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getProperty
argument_list|(
name|ClassHelper
operator|.
name|USE_DEFAULT_CLASS_HELPER
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getProperty
argument_list|(
name|ClassUnwrapper
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|proxiedObject
operator|.
name|getClass
argument_list|()
argument_list|,
name|ClassHelper
operator|.
name|getRealClassFromClass
argument_list|(
name|proxiedObject
operator|.
name|getClass
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|getRealClassFromClassPropertyWasNotSetInBus
parameter_list|()
block|{
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getProperty
argument_list|(
name|ClassHelper
operator|.
name|USE_DEFAULT_CLASS_HELPER
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getProperty
argument_list|(
name|ClassUnwrapper
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|realObjectInternalSpring
operator|.
name|getClass
argument_list|()
argument_list|,
name|ClassHelper
operator|.
name|getRealClassFromClass
argument_list|(
name|springAopObject
operator|.
name|getClass
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|getRealObjectPropertyWasSetInBus
parameter_list|()
block|{
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getProperty
argument_list|(
name|ClassHelper
operator|.
name|USE_DEFAULT_CLASS_HELPER
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getProperty
argument_list|(
name|ClassUnwrapper
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|realObjectInternalProxy
argument_list|,
name|ClassHelper
operator|.
name|getRealObject
argument_list|(
name|proxiedObject
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|getRealObjectPropertyWasNotSetInBus
parameter_list|()
block|{
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getProperty
argument_list|(
name|ClassHelper
operator|.
name|USE_DEFAULT_CLASS_HELPER
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getProperty
argument_list|(
name|ClassUnwrapper
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|realObjectInternalSpring
argument_list|,
name|ClassHelper
operator|.
name|getRealObject
argument_list|(
name|springAopObject
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|getRealLambdaClassPropertyWasNotSetInBus
parameter_list|()
block|{
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getProperty
argument_list|(
name|ClassHelper
operator|.
name|USE_DEFAULT_CLASS_HELPER
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getProperty
argument_list|(
name|ClassUnwrapper
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|fn
operator|.
name|getClass
argument_list|()
argument_list|,
name|ClassHelper
operator|.
name|getRealClass
argument_list|(
name|fn
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|getRealLambdaClassPropertyWasSetInBus
parameter_list|()
block|{
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getProperty
argument_list|(
name|ClassHelper
operator|.
name|USE_DEFAULT_CLASS_HELPER
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getProperty
argument_list|(
name|ClassUnwrapper
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|fn
operator|.
name|getClass
argument_list|()
argument_list|,
name|ClassHelper
operator|.
name|getRealClass
argument_list|(
name|fn
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|getRealLambdaClassFromClassPropertyWasSetInBus
parameter_list|()
block|{
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getProperty
argument_list|(
name|ClassHelper
operator|.
name|USE_DEFAULT_CLASS_HELPER
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getProperty
argument_list|(
name|ClassUnwrapper
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|fn
operator|.
name|getClass
argument_list|()
argument_list|,
name|ClassHelper
operator|.
name|getRealClassFromClass
argument_list|(
name|fn
operator|.
name|getClass
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|getRealLambdaClassFromClassPropertyWasNotSetInBus
parameter_list|()
block|{
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getProperty
argument_list|(
name|ClassHelper
operator|.
name|USE_DEFAULT_CLASS_HELPER
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getProperty
argument_list|(
name|ClassUnwrapper
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|fn
operator|.
name|getClass
argument_list|()
argument_list|,
name|ClassHelper
operator|.
name|getRealClassFromClass
argument_list|(
name|fn
operator|.
name|getClass
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|getRealCglibClassFromClassPropertyWasNotSetInBus
parameter_list|()
block|{
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getProperty
argument_list|(
name|ClassHelper
operator|.
name|USE_DEFAULT_CLASS_HELPER
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getProperty
argument_list|(
name|ClassUnwrapper
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|Object
operator|.
name|class
argument_list|,
name|ClassHelper
operator|.
name|getRealClassFromClass
argument_list|(
name|cglibProxyObject
operator|.
name|getClass
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|getRealCglibClassFromClassPropertyWasSetInBus
parameter_list|()
block|{
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getProperty
argument_list|(
name|ClassHelper
operator|.
name|USE_DEFAULT_CLASS_HELPER
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getProperty
argument_list|(
name|ClassUnwrapper
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|cglibProxyObject
operator|.
name|getClass
argument_list|()
argument_list|,
name|ClassHelper
operator|.
name|getRealClassFromClass
argument_list|(
name|cglibProxyObject
operator|.
name|getClass
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
specifier|public
interface|interface
name|AnyInterface
block|{
name|void
name|anyMethod
parameter_list|()
function_decl|;
block|}
block|}
end_class

end_unit

