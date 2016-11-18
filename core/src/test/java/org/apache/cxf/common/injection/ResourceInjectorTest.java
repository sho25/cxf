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
name|injection
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
name|InvocationTargetException
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
name|Modifier
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
name|ArrayList
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
name|annotation
operator|.
name|PostConstruct
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|PreDestroy
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
name|annotation
operator|.
name|Resources
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
name|net
operator|.
name|sf
operator|.
name|cglib
operator|.
name|proxy
operator|.
name|MethodInterceptor
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
name|MethodProxy
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
name|resource
operator|.
name|ResourceManager
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
name|resource
operator|.
name|ResourceResolver
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
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|ResourceInjectorTest
extends|extends
name|Assert
block|{
specifier|private
specifier|static
specifier|final
name|String
name|RESOURCE_ONE
init|=
literal|"resource one"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RESOURCE_TWO
init|=
literal|"resource two"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RESOURCE_THREE
init|=
literal|"resource three"
decl_stmt|;
specifier|private
name|ResourceInjector
name|injector
decl_stmt|;
specifier|public
name|void
name|setUpResourceManager
parameter_list|(
name|String
name|pfx
parameter_list|)
block|{
name|ResourceManager
name|resMgr
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|ResourceManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ResourceResolver
argument_list|>
name|resolvers
init|=
operator|new
name|ArrayList
argument_list|<
name|ResourceResolver
argument_list|>
argument_list|()
decl_stmt|;
name|resMgr
operator|.
name|getResourceResolvers
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|resolvers
argument_list|)
expr_stmt|;
name|resMgr
operator|.
name|resolveResource
argument_list|(
name|pfx
operator|+
literal|"resource1"
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|resolvers
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|RESOURCE_ONE
argument_list|)
expr_stmt|;
name|resMgr
operator|.
name|resolveResource
argument_list|(
literal|"resource2"
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|resolvers
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|RESOURCE_TWO
argument_list|)
expr_stmt|;
name|resMgr
operator|.
name|resolveResource
argument_list|(
literal|"resource3"
argument_list|,
name|CharSequence
operator|.
name|class
argument_list|,
name|resolvers
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|RESOURCE_THREE
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|resMgr
argument_list|)
expr_stmt|;
name|injector
operator|=
operator|new
name|ResourceInjector
argument_list|(
name|resMgr
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFieldInjection
parameter_list|()
block|{
name|setUpResourceManager
argument_list|(
name|FieldTarget
operator|.
name|class
operator|.
name|getCanonicalName
argument_list|()
operator|+
literal|"/"
argument_list|)
expr_stmt|;
name|doInjectTest
argument_list|(
operator|new
name|FieldTarget
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFieldInSuperClassInjection
parameter_list|()
block|{
name|setUpResourceManager
argument_list|(
literal|"org.apache.cxf.common.injection.FieldTarget/"
argument_list|)
expr_stmt|;
name|doInjectTest
argument_list|(
operator|new
name|SubFieldTarget
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSetterInSuperClassInjection
parameter_list|()
block|{
name|setUpResourceManager
argument_list|(
literal|"org.apache.cxf.common.injection.SetterTarget/"
argument_list|)
expr_stmt|;
name|doInjectTest
argument_list|(
operator|new
name|SubSetterTarget
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSetterInjection
parameter_list|()
block|{
name|setUpResourceManager
argument_list|(
name|SetterTarget
operator|.
name|class
operator|.
name|getCanonicalName
argument_list|()
operator|+
literal|"/"
argument_list|)
expr_stmt|;
name|doInjectTest
argument_list|(
operator|new
name|SetterTarget
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testProxyInjection
parameter_list|()
block|{
name|setUpResourceManager
argument_list|(
name|SetterTarget
operator|.
name|class
operator|.
name|getCanonicalName
argument_list|()
operator|+
literal|"/"
argument_list|)
expr_stmt|;
name|doInjectTest
argument_list|(
name|getProxyObject
argument_list|()
argument_list|,
name|SetterTarget
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEnhancedInjection
parameter_list|()
block|{
name|setUpResourceManager
argument_list|(
name|FieldTarget
operator|.
name|class
operator|.
name|getCanonicalName
argument_list|()
operator|+
literal|"/"
argument_list|)
expr_stmt|;
name|doInjectTest
argument_list|(
name|getEnhancedObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testClassLevelInjection
parameter_list|()
block|{
name|setUpResourceManager
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|doInjectTest
argument_list|(
operator|new
name|ClassTarget
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResourcesContainer
parameter_list|()
block|{
name|setUpResourceManager
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|doInjectTest
argument_list|(
operator|new
name|ResourcesContainerTarget
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPostConstruct
parameter_list|()
block|{
name|setUpResourceManager
argument_list|(
name|SetterTarget
operator|.
name|class
operator|.
name|getCanonicalName
argument_list|()
operator|+
literal|"/"
argument_list|)
expr_stmt|;
name|SetterTarget
name|target
init|=
operator|new
name|SetterTarget
argument_list|()
decl_stmt|;
name|doInjectTest
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|target
operator|.
name|injectionCompleteCalled
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPreDestroy
parameter_list|()
block|{
name|injector
operator|=
operator|new
name|ResourceInjector
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|SetterTarget
name|target
init|=
operator|new
name|SetterTarget
argument_list|()
decl_stmt|;
name|injector
operator|.
name|destroy
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|target
operator|.
name|preDestroyCalled
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doInjectTest
parameter_list|(
name|Target
name|target
parameter_list|)
block|{
name|doInjectTest
argument_list|(
name|target
argument_list|,
name|target
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doInjectTest
parameter_list|(
name|Target
name|target
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
name|injector
operator|.
name|inject
argument_list|(
name|target
argument_list|,
name|clazz
argument_list|)
expr_stmt|;
name|injector
operator|.
name|construct
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|target
operator|.
name|getResource1
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|RESOURCE_ONE
argument_list|,
name|target
operator|.
name|getResource1
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|target
operator|.
name|getResource2
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|RESOURCE_TWO
argument_list|,
name|target
operator|.
name|getResource2
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|target
operator|.
name|getResource3
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|RESOURCE_THREE
argument_list|,
name|target
operator|.
name|getResource3
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Target
name|getProxyObject
parameter_list|()
block|{
name|Target
name|t
init|=
operator|(
name|Target
operator|)
name|Proxy
operator|.
name|newProxyInstance
argument_list|(
name|ISetterTarget
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|,
operator|new
name|Class
index|[]
block|{
name|ISetterTarget
operator|.
name|class
block|}
argument_list|,
operator|new
name|ProxyClass
argument_list|(
operator|new
name|SetterTarget
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|t
return|;
block|}
specifier|private
name|FieldTarget
name|getEnhancedObject
parameter_list|()
block|{
name|Enhancer
name|e
init|=
operator|new
name|Enhancer
argument_list|()
decl_stmt|;
name|e
operator|.
name|setSuperclass
argument_list|(
name|FieldTarget
operator|.
name|class
argument_list|)
expr_stmt|;
name|e
operator|.
name|setCallback
argument_list|(
operator|new
name|CallInterceptor
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|(
name|FieldTarget
operator|)
name|e
operator|.
name|create
argument_list|()
return|;
block|}
block|}
end_class

begin_interface
interface|interface
name|Target
block|{
name|String
name|getResource1
parameter_list|()
function_decl|;
name|String
name|getResource2
parameter_list|()
function_decl|;
name|CharSequence
name|getResource3
parameter_list|()
function_decl|;
block|}
end_interface

begin_class
class|class
name|CallInterceptor
implements|implements
name|MethodInterceptor
block|{
specifier|public
name|Object
name|intercept
parameter_list|(
name|Object
name|obj
parameter_list|,
name|Method
name|method
parameter_list|,
name|Object
index|[]
name|args
parameter_list|,
name|MethodProxy
name|proxy
parameter_list|)
throws|throws
name|Throwable
block|{
name|Object
name|retValFromSuper
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|Modifier
operator|.
name|isAbstract
argument_list|(
name|method
operator|.
name|getModifiers
argument_list|()
argument_list|)
condition|)
block|{
name|retValFromSuper
operator|=
name|proxy
operator|.
name|invokeSuper
argument_list|(
name|obj
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
return|return
name|retValFromSuper
return|;
block|}
block|}
end_class

begin_class
class|class
name|FieldTarget
implements|implements
name|Target
block|{
annotation|@
name|Resource
specifier|private
name|String
name|resource1
decl_stmt|;
annotation|@
name|Resource
argument_list|(
name|name
operator|=
literal|"resource2"
argument_list|)
specifier|private
name|String
name|resource2foo
decl_stmt|;
annotation|@
name|Resource
argument_list|(
name|name
operator|=
literal|"resource3"
argument_list|)
specifier|private
name|CharSequence
name|resource3foo
decl_stmt|;
specifier|public
name|String
name|getResource1
parameter_list|()
block|{
return|return
name|resource1
return|;
block|}
specifier|public
name|String
name|getResource2
parameter_list|()
block|{
return|return
name|resource2foo
return|;
block|}
specifier|public
name|CharSequence
name|getResource3
parameter_list|()
block|{
return|return
name|resource3foo
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"["
operator|+
name|resource1
operator|+
literal|":"
operator|+
name|resource2foo
operator|+
literal|":"
operator|+
name|resource3foo
operator|+
literal|"]"
return|;
block|}
block|}
end_class

begin_class
class|class
name|SubFieldTarget
extends|extends
name|FieldTarget
block|{ }
end_class

begin_class
class|class
name|SubSetterTarget
extends|extends
name|SetterTarget
block|{      }
end_class

begin_interface
interface|interface
name|ISetterTarget
extends|extends
name|Target
block|{
name|void
name|setResource1
parameter_list|(
name|String
name|argResource1
parameter_list|)
function_decl|;
name|void
name|setResource2
parameter_list|(
name|String
name|argResource2
parameter_list|)
function_decl|;
name|void
name|setResource3
parameter_list|(
name|CharSequence
name|argResource3
parameter_list|)
function_decl|;
block|}
end_interface

begin_class
class|class
name|ProxyClass
implements|implements
name|InvocationHandler
block|{
name|Object
name|obj
decl_stmt|;
name|ProxyClass
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|obj
operator|=
name|o
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
name|m
parameter_list|,
name|Object
index|[]
name|args
parameter_list|)
throws|throws
name|Throwable
block|{
name|Object
name|result
init|=
literal|null
decl_stmt|;
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|types
init|=
operator|new
name|Class
index|[
literal|0
index|]
decl_stmt|;
if|if
condition|(
name|args
operator|!=
literal|null
condition|)
block|{
name|types
operator|=
operator|new
name|Class
index|[
name|args
operator|.
name|length
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|args
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|types
index|[
name|i
index|]
operator|=
name|args
index|[
name|i
index|]
operator|.
name|getClass
argument_list|()
expr_stmt|;
if|if
condition|(
literal|"setResource3"
operator|.
name|equals
argument_list|(
name|m
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|types
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
name|String
operator|.
name|class
argument_list|)
condition|)
block|{
name|types
index|[
name|i
index|]
operator|=
name|CharSequence
operator|.
name|class
expr_stmt|;
block|}
block|}
block|}
name|Method
name|target
init|=
name|obj
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
name|m
operator|.
name|getName
argument_list|()
argument_list|,
name|types
argument_list|)
decl_stmt|;
name|result
operator|=
name|target
operator|.
name|invoke
argument_list|(
name|obj
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|e
parameter_list|)
block|{
comment|// Do nothing here
block|}
catch|catch
parameter_list|(
name|Exception
name|eBj
parameter_list|)
block|{
name|eBj
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

begin_class
class|class
name|SetterTarget
implements|implements
name|Target
block|{
specifier|private
name|String
name|resource1
decl_stmt|;
specifier|private
name|String
name|resource2
decl_stmt|;
specifier|private
name|CharSequence
name|resource3
decl_stmt|;
specifier|private
name|boolean
name|injectionCompletePublic
decl_stmt|;
specifier|private
name|boolean
name|injectionCompletePrivate
decl_stmt|;
specifier|private
name|boolean
name|preDestroy
decl_stmt|;
specifier|private
name|boolean
name|preDestroyPrivate
decl_stmt|;
specifier|public
specifier|final
name|String
name|getResource1
parameter_list|()
block|{
return|return
name|this
operator|.
name|resource1
return|;
block|}
annotation|@
name|Resource
specifier|public
specifier|final
name|void
name|setResource1
parameter_list|(
specifier|final
name|String
name|argResource1
parameter_list|)
block|{
name|this
operator|.
name|resource1
operator|=
name|argResource1
expr_stmt|;
block|}
specifier|public
specifier|final
name|String
name|getResource2
parameter_list|()
block|{
return|return
name|this
operator|.
name|resource2
return|;
block|}
annotation|@
name|Resource
argument_list|(
name|name
operator|=
literal|"resource2"
argument_list|)
specifier|public
name|void
name|setResource2
parameter_list|(
specifier|final
name|String
name|argResource2
parameter_list|)
block|{
name|this
operator|.
name|resource2
operator|=
name|argResource2
expr_stmt|;
block|}
specifier|public
specifier|final
name|CharSequence
name|getResource3
parameter_list|()
block|{
return|return
name|this
operator|.
name|resource3
return|;
block|}
annotation|@
name|Resource
argument_list|(
name|name
operator|=
literal|"resource3"
argument_list|)
specifier|public
name|void
name|setResource3
parameter_list|(
specifier|final
name|CharSequence
name|argResource3
parameter_list|)
block|{
name|this
operator|.
name|resource3
operator|=
name|argResource3
expr_stmt|;
block|}
annotation|@
name|PostConstruct
specifier|public
name|void
name|injectionIsAllFinishedNowThankYouVeryMuch
parameter_list|()
block|{
name|injectionCompletePublic
operator|=
literal|true
expr_stmt|;
comment|// stick this here to keep PMD happy...
name|injectionIsAllFinishedNowThankYouVeryMuchPrivate
argument_list|()
expr_stmt|;
block|}
annotation|@
name|PostConstruct
specifier|private
name|void
name|injectionIsAllFinishedNowThankYouVeryMuchPrivate
parameter_list|()
block|{
name|injectionCompletePrivate
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|PreDestroy
specifier|public
name|void
name|preDestroyMethod
parameter_list|()
block|{
name|preDestroy
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|PreDestroy
specifier|private
name|void
name|preDestroyMethodPrivate
parameter_list|()
block|{
name|preDestroyPrivate
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|boolean
name|injectionCompleteCalled
parameter_list|()
block|{
return|return
name|injectionCompletePrivate
operator|&&
name|injectionCompletePublic
return|;
block|}
specifier|public
name|boolean
name|preDestroyCalled
parameter_list|()
block|{
return|return
name|preDestroy
operator|&&
name|preDestroyPrivate
return|;
block|}
comment|// dummy method to access the private methods to avoid compile warnings
specifier|public
name|void
name|dummyMethod
parameter_list|()
block|{
name|preDestroyMethodPrivate
argument_list|()
expr_stmt|;
name|setResource2
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
block|}
end_class

begin_comment
comment|//CHECKSTYLE:OFF
end_comment

begin_class
annotation|@
name|Resource
argument_list|(
name|name
operator|=
literal|"resource1"
argument_list|)
class|class
name|ClassTarget
implements|implements
name|Target
block|{
annotation|@
name|Resource
argument_list|(
name|name
operator|=
literal|"resource3"
argument_list|)
specifier|public
name|CharSequence
name|resource3foo
decl_stmt|;
annotation|@
name|Resource
argument_list|(
name|name
operator|=
literal|"resource2"
argument_list|)
specifier|public
name|String
name|resource2foo
decl_stmt|;
specifier|private
name|String
name|res1
decl_stmt|;
specifier|public
specifier|final
name|void
name|setResource1
parameter_list|(
name|String
name|res
parameter_list|)
block|{
name|res1
operator|=
name|res
expr_stmt|;
block|}
specifier|public
specifier|final
name|String
name|getResource1
parameter_list|()
block|{
return|return
name|res1
return|;
block|}
specifier|public
specifier|final
name|String
name|getResource2
parameter_list|()
block|{
return|return
name|resource2foo
return|;
block|}
specifier|public
specifier|final
name|CharSequence
name|getResource3
parameter_list|()
block|{
return|return
name|resource3foo
return|;
block|}
block|}
end_class

begin_class
annotation|@
name|Resources
argument_list|(
block|{
annotation|@
name|Resource
argument_list|(
name|name
operator|=
literal|"resource1"
argument_list|)
block|,
annotation|@
name|Resource
argument_list|(
name|name
operator|=
literal|"resource2"
argument_list|)
block|,
annotation|@
name|Resource
argument_list|(
name|name
operator|=
literal|"resource3"
argument_list|)
block|}
argument_list|)
class|class
name|ResourcesContainerTarget
implements|implements
name|Target
block|{
specifier|private
name|String
name|res1
decl_stmt|;
specifier|private
name|String
name|resource2
decl_stmt|;
specifier|private
name|CharSequence
name|resource3
decl_stmt|;
specifier|public
specifier|final
name|void
name|setResource1
parameter_list|(
name|String
name|res
parameter_list|)
block|{
name|res1
operator|=
name|res
expr_stmt|;
block|}
specifier|public
specifier|final
name|String
name|getResource1
parameter_list|()
block|{
return|return
name|res1
return|;
block|}
specifier|public
specifier|final
name|String
name|getResource2
parameter_list|()
block|{
return|return
name|resource2
return|;
block|}
specifier|public
specifier|final
name|CharSequence
name|getResource3
parameter_list|()
block|{
return|return
name|resource3
return|;
block|}
block|}
end_class

end_unit

