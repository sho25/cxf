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
name|microprofile
operator|.
name|client
operator|.
name|cdi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Callable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|context
operator|.
name|spi
operator|.
name|CreationalContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|AnnotatedMethod
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|AnnotatedType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|BeanManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|InterceptionType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|Interceptor
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
import|;
end_import

begin_class
class|class
name|CDIInterceptorWrapperImpl
implements|implements
name|CDIInterceptorWrapper
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|CDIInterceptorWrapperImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|CreationalContext
argument_list|<
name|?
argument_list|>
name|creationalContext
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|Method
argument_list|,
name|List
argument_list|<
name|InterceptorInvoker
argument_list|>
argument_list|>
name|interceptorInvokers
decl_stmt|;
name|CDIInterceptorWrapperImpl
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|restClient
parameter_list|,
name|Object
name|beanManagerObject
parameter_list|)
block|{
name|BeanManager
name|beanManager
init|=
operator|(
name|BeanManager
operator|)
name|beanManagerObject
decl_stmt|;
name|creationalContext
operator|=
name|beanManager
operator|!=
literal|null
condition|?
name|beanManager
operator|.
name|createCreationalContext
argument_list|(
literal|null
argument_list|)
else|:
literal|null
expr_stmt|;
name|interceptorInvokers
operator|=
name|initInterceptorInvokers
argument_list|(
name|beanManager
argument_list|,
name|creationalContext
argument_list|,
name|restClient
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|Map
argument_list|<
name|Method
argument_list|,
name|List
argument_list|<
name|InterceptorInvoker
argument_list|>
argument_list|>
name|initInterceptorInvokers
parameter_list|(
name|BeanManager
name|beanManager
parameter_list|,
name|CreationalContext
argument_list|<
name|?
argument_list|>
name|creationalContext
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|restClient
parameter_list|)
block|{
name|Map
argument_list|<
name|Method
argument_list|,
name|List
argument_list|<
name|InterceptorInvoker
argument_list|>
argument_list|>
name|invokers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// Interceptor as a key in a map is not entirely correct (custom interceptors) but should work in most cases
name|Map
argument_list|<
name|Interceptor
argument_list|<
name|?
argument_list|>
argument_list|,
name|Object
argument_list|>
name|interceptorInstances
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|AnnotatedType
argument_list|<
name|?
argument_list|>
name|restClientType
init|=
name|beanManager
operator|.
name|createAnnotatedType
argument_list|(
name|restClient
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Annotation
argument_list|>
name|classBindings
init|=
name|getBindings
argument_list|(
name|restClientType
operator|.
name|getAnnotations
argument_list|()
argument_list|,
name|beanManager
argument_list|)
decl_stmt|;
for|for
control|(
name|AnnotatedMethod
argument_list|<
name|?
argument_list|>
name|method
range|:
name|restClientType
operator|.
name|getMethods
argument_list|()
control|)
block|{
name|Method
name|javaMethod
init|=
name|method
operator|.
name|getJavaMember
argument_list|()
decl_stmt|;
if|if
condition|(
name|javaMethod
operator|.
name|isDefault
argument_list|()
operator|||
name|method
operator|.
name|isStatic
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|List
argument_list|<
name|Annotation
argument_list|>
name|methodBindings
init|=
name|getBindings
argument_list|(
name|method
operator|.
name|getAnnotations
argument_list|()
argument_list|,
name|beanManager
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|classBindings
operator|.
name|isEmpty
argument_list|()
operator|||
operator|!
name|methodBindings
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Annotation
index|[]
name|interceptorBindings
init|=
name|merge
argument_list|(
name|methodBindings
argument_list|,
name|classBindings
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
argument_list|>
argument_list|>
name|interceptors
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|beanManager
operator|.
name|resolveInterceptors
argument_list|(
name|InterceptionType
operator|.
name|AROUND_INVOKE
argument_list|,
name|interceptorBindings
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINEST
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|finest
argument_list|(
literal|"Resolved interceptors from beanManager, "
operator|+
name|beanManager
operator|+
literal|":"
operator|+
name|interceptors
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|interceptors
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|InterceptorInvoker
argument_list|>
name|chain
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Interceptor
argument_list|<
name|?
argument_list|>
name|interceptor
range|:
name|interceptors
control|)
block|{
name|chain
operator|.
name|add
argument_list|(
operator|new
name|InterceptorInvoker
argument_list|(
name|interceptor
argument_list|,
name|interceptorInstances
operator|.
name|computeIfAbsent
argument_list|(
name|interceptor
argument_list|,
name|i
lambda|->
name|beanManager
operator|.
name|getReference
argument_list|(
name|i
argument_list|,
name|i
operator|.
name|getBeanClass
argument_list|()
argument_list|,
name|creationalContext
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|invokers
operator|.
name|put
argument_list|(
name|javaMethod
argument_list|,
name|chain
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|invokers
operator|.
name|isEmpty
argument_list|()
condition|?
name|Collections
operator|.
name|emptyMap
argument_list|()
else|:
name|invokers
return|;
block|}
specifier|private
specifier|static
name|Annotation
index|[]
name|merge
parameter_list|(
name|List
argument_list|<
name|Annotation
argument_list|>
name|methodBindings
parameter_list|,
name|List
argument_list|<
name|Annotation
argument_list|>
name|classBindings
parameter_list|)
block|{
name|Set
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|>
name|types
init|=
name|methodBindings
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|a
lambda|->
name|a
operator|.
name|annotationType
argument_list|()
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toSet
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Annotation
argument_list|>
name|merged
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|methodBindings
argument_list|)
decl_stmt|;
for|for
control|(
name|Annotation
name|annotation
range|:
name|classBindings
control|)
block|{
if|if
condition|(
operator|!
name|types
operator|.
name|contains
argument_list|(
name|annotation
operator|.
name|annotationType
argument_list|()
argument_list|)
condition|)
block|{
name|merged
operator|.
name|add
argument_list|(
name|annotation
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|merged
operator|.
name|toArray
argument_list|(
operator|new
name|Annotation
index|[]
block|{}
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|List
argument_list|<
name|Annotation
argument_list|>
name|getBindings
parameter_list|(
name|Set
argument_list|<
name|Annotation
argument_list|>
name|annotations
parameter_list|,
name|BeanManager
name|beanManager
parameter_list|)
block|{
if|if
condition|(
name|annotations
operator|==
literal|null
operator|||
name|annotations
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
name|List
argument_list|<
name|Annotation
argument_list|>
name|bindings
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Annotation
name|annotation
range|:
name|annotations
control|)
block|{
if|if
condition|(
name|beanManager
operator|.
name|isInterceptorBinding
argument_list|(
name|annotation
operator|.
name|annotationType
argument_list|()
argument_list|)
condition|)
block|{
name|bindings
operator|.
name|add
argument_list|(
name|annotation
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|bindings
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|invoke
parameter_list|(
name|Object
name|restClient
parameter_list|,
name|Method
name|method
parameter_list|,
name|Object
index|[]
name|params
parameter_list|,
name|Callable
argument_list|<
name|Object
argument_list|>
name|callable
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|InterceptorInvoker
argument_list|>
name|invokers
init|=
name|interceptorInvokers
operator|.
name|get
argument_list|(
name|method
argument_list|)
decl_stmt|;
if|if
condition|(
name|invokers
operator|==
literal|null
operator|||
name|invokers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|callable
operator|.
name|call
argument_list|()
return|;
block|}
return|return
operator|new
name|MPRestClientInvocationContextImpl
argument_list|(
name|restClient
argument_list|,
name|method
argument_list|,
name|params
argument_list|,
name|invokers
argument_list|,
name|callable
argument_list|)
operator|.
name|proceed
argument_list|()
return|;
block|}
block|}
end_class

end_unit
