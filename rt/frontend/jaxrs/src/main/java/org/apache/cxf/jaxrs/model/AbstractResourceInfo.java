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
name|Field
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
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|Context
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
name|impl
operator|.
name|tl
operator|.
name|ThreadLocalProxy
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
name|InjectionUtils
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractResourceInfo
block|{
specifier|public
specifier|static
specifier|final
name|String
name|CONSTRUCTOR_PROXY_MAP
init|=
literal|"jaxrs-constructor-proxy-map"
decl_stmt|;
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
name|AbstractResourceInfo
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|FIELD_PROXY_MAP
init|=
literal|"jaxrs-field-proxy-map"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SETTER_PROXY_MAP
init|=
literal|"jaxrs-setter-proxy-map"
decl_stmt|;
specifier|protected
name|boolean
name|root
decl_stmt|;
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|resourceClass
decl_stmt|;
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|serviceClass
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|List
argument_list|<
name|Field
argument_list|>
argument_list|>
name|contextFields
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Method
argument_list|>
argument_list|>
name|contextMethods
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|boolean
name|constructorProxiesAvailable
decl_stmt|;
specifier|private
name|boolean
name|contextsAvailable
decl_stmt|;
specifier|protected
name|AbstractResourceInfo
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|this
operator|.
name|bus
operator|=
name|bus
expr_stmt|;
block|}
specifier|protected
name|AbstractResourceInfo
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|resourceClass
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|serviceClass
parameter_list|,
name|boolean
name|isRoot
parameter_list|,
name|boolean
name|checkContexts
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|this
argument_list|(
name|resourceClass
argument_list|,
name|serviceClass
argument_list|,
name|isRoot
argument_list|,
name|checkContexts
argument_list|,
literal|null
argument_list|,
name|bus
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|AbstractResourceInfo
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|resourceClass
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|serviceClass
parameter_list|,
name|boolean
name|isRoot
parameter_list|,
name|boolean
name|checkContexts
parameter_list|,
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|ThreadLocalProxy
argument_list|<
name|?
argument_list|>
argument_list|>
name|constructorProxies
parameter_list|,
name|Bus
name|bus
parameter_list|,
name|Object
name|provider
parameter_list|)
block|{
name|this
operator|.
name|bus
operator|=
name|bus
expr_stmt|;
name|this
operator|.
name|serviceClass
operator|=
name|serviceClass
expr_stmt|;
name|this
operator|.
name|resourceClass
operator|=
name|resourceClass
expr_stmt|;
name|root
operator|=
name|isRoot
expr_stmt|;
if|if
condition|(
name|checkContexts
operator|&&
name|resourceClass
operator|!=
literal|null
condition|)
block|{
name|findContexts
argument_list|(
name|serviceClass
argument_list|,
name|provider
argument_list|,
name|constructorProxies
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|findContexts
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|Object
name|provider
parameter_list|,
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|ThreadLocalProxy
argument_list|<
name|?
argument_list|>
argument_list|>
name|constructorProxies
parameter_list|)
block|{
name|findContextFields
argument_list|(
name|cls
argument_list|,
name|provider
argument_list|)
expr_stmt|;
name|findContextSetterMethods
argument_list|(
name|cls
argument_list|,
name|provider
argument_list|)
expr_stmt|;
if|if
condition|(
name|constructorProxies
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|ThreadLocalProxy
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
name|proxies
init|=
name|getConstructorProxyMap
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|proxies
operator|.
name|put
argument_list|(
name|serviceClass
argument_list|,
name|constructorProxies
argument_list|)
expr_stmt|;
name|constructorProxiesAvailable
operator|=
literal|true
expr_stmt|;
block|}
name|contextsAvailable
operator|=
name|contextFields
operator|!=
literal|null
operator|&&
operator|!
name|contextFields
operator|.
name|isEmpty
argument_list|()
operator|||
name|contextMethods
operator|!=
literal|null
operator|&&
operator|!
name|contextMethods
operator|.
name|isEmpty
argument_list|()
operator|||
name|constructorProxiesAvailable
expr_stmt|;
block|}
specifier|public
name|boolean
name|contextsAvailable
parameter_list|()
block|{
return|return
name|contextsAvailable
return|;
block|}
specifier|public
name|Bus
name|getBus
parameter_list|()
block|{
return|return
name|bus
return|;
block|}
specifier|public
name|void
name|setResourceClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|rClass
parameter_list|)
block|{
name|resourceClass
operator|=
name|rClass
expr_stmt|;
if|if
condition|(
name|serviceClass
operator|.
name|isInterface
argument_list|()
operator|&&
name|resourceClass
operator|!=
literal|null
operator|&&
operator|!
name|resourceClass
operator|.
name|isInterface
argument_list|()
condition|)
block|{
name|findContexts
argument_list|(
name|resourceClass
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getServiceClass
parameter_list|()
block|{
return|return
name|serviceClass
return|;
block|}
specifier|private
name|void
name|findContextFields
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|Object
name|provider
parameter_list|)
block|{
if|if
condition|(
name|cls
operator|==
name|Object
operator|.
name|class
operator|||
name|cls
operator|==
literal|null
condition|)
block|{
return|return;
block|}
for|for
control|(
name|Field
name|f
range|:
name|cls
operator|.
name|getDeclaredFields
argument_list|()
control|)
block|{
for|for
control|(
name|Annotation
name|a
range|:
name|f
operator|.
name|getAnnotations
argument_list|()
control|)
block|{
if|if
condition|(
name|a
operator|.
name|annotationType
argument_list|()
operator|==
name|Context
operator|.
name|class
operator|&&
operator|(
name|f
operator|.
name|getType
argument_list|()
operator|.
name|isInterface
argument_list|()
operator|||
name|f
operator|.
name|getType
argument_list|()
operator|==
name|Application
operator|.
name|class
operator|)
condition|)
block|{
name|contextFields
operator|=
name|addContextField
argument_list|(
name|contextFields
argument_list|,
name|f
argument_list|)
expr_stmt|;
name|checkContextClass
argument_list|(
name|f
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|InjectionUtils
operator|.
name|VALUE_CONTEXTS
operator|.
name|contains
argument_list|(
name|f
operator|.
name|getType
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|addToMap
argument_list|(
name|getFieldProxyMap
argument_list|(
literal|true
argument_list|)
argument_list|,
name|f
argument_list|,
name|getFieldThreadLocalProxy
argument_list|(
name|f
argument_list|,
name|provider
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|findContextFields
argument_list|(
name|cls
operator|.
name|getSuperclass
argument_list|()
argument_list|,
name|provider
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|ThreadLocalProxy
argument_list|<
name|?
argument_list|>
name|getFieldThreadLocalProxy
parameter_list|(
name|Field
name|f
parameter_list|,
name|Object
name|provider
parameter_list|)
block|{
if|if
condition|(
name|provider
operator|!=
literal|null
condition|)
block|{
name|Object
name|proxy
init|=
literal|null
decl_stmt|;
synchronized|synchronized
init|(
name|provider
init|)
block|{
try|try
block|{
name|proxy
operator|=
name|InjectionUtils
operator|.
name|extractFieldValue
argument_list|(
name|f
argument_list|,
name|provider
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// continue
block|}
if|if
condition|(
operator|!
operator|(
name|proxy
operator|instanceof
name|ThreadLocalProxy
operator|)
condition|)
block|{
name|proxy
operator|=
name|InjectionUtils
operator|.
name|createThreadLocalProxy
argument_list|(
name|f
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|InjectionUtils
operator|.
name|injectFieldValue
argument_list|(
name|f
argument_list|,
name|provider
argument_list|,
name|proxy
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|(
name|ThreadLocalProxy
argument_list|<
name|?
argument_list|>
operator|)
name|proxy
return|;
block|}
return|return
name|InjectionUtils
operator|.
name|createThreadLocalProxy
argument_list|(
name|f
operator|.
name|getType
argument_list|()
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|ThreadLocalProxy
argument_list|<
name|?
argument_list|>
name|getMethodThreadLocalProxy
parameter_list|(
name|Method
name|m
parameter_list|,
name|Object
name|provider
parameter_list|)
block|{
if|if
condition|(
name|provider
operator|!=
literal|null
condition|)
block|{
name|Object
name|proxy
init|=
literal|null
decl_stmt|;
synchronized|synchronized
init|(
name|provider
init|)
block|{
try|try
block|{
name|proxy
operator|=
name|InjectionUtils
operator|.
name|extractFromMethod
argument_list|(
name|provider
argument_list|,
name|InjectionUtils
operator|.
name|getGetterFromSetter
argument_list|(
name|m
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// continue
block|}
if|if
condition|(
operator|!
operator|(
name|proxy
operator|instanceof
name|ThreadLocalProxy
operator|)
condition|)
block|{
name|proxy
operator|=
name|InjectionUtils
operator|.
name|createThreadLocalProxy
argument_list|(
name|m
operator|.
name|getParameterTypes
argument_list|()
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|InjectionUtils
operator|.
name|injectThroughMethod
argument_list|(
name|provider
argument_list|,
name|m
argument_list|,
name|proxy
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|(
name|ThreadLocalProxy
argument_list|<
name|?
argument_list|>
operator|)
name|proxy
return|;
block|}
return|return
name|InjectionUtils
operator|.
name|createThreadLocalProxy
argument_list|(
name|m
operator|.
name|getParameterTypes
argument_list|()
index|[
literal|0
index|]
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Map
argument_list|<
name|T
argument_list|,
name|ThreadLocalProxy
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
name|getProxyMap
parameter_list|(
name|String
name|prop
parameter_list|,
name|boolean
name|create
parameter_list|)
block|{
name|Object
name|property
init|=
literal|null
decl_stmt|;
synchronized|synchronized
init|(
name|bus
init|)
block|{
name|property
operator|=
name|bus
operator|.
name|getProperty
argument_list|(
name|prop
argument_list|)
expr_stmt|;
if|if
condition|(
name|property
operator|==
literal|null
operator|&&
name|create
condition|)
block|{
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Map
argument_list|<
name|T
argument_list|,
name|ThreadLocalProxy
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
name|map
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|bus
operator|.
name|setProperty
argument_list|(
name|prop
argument_list|,
name|map
argument_list|)
expr_stmt|;
name|property
operator|=
name|map
expr_stmt|;
block|}
block|}
return|return
operator|(
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Map
argument_list|<
name|T
argument_list|,
name|ThreadLocalProxy
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
operator|)
name|property
return|;
block|}
specifier|public
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|ThreadLocalProxy
argument_list|<
name|?
argument_list|>
argument_list|>
name|getConstructorProxies
parameter_list|()
block|{
if|if
condition|(
name|constructorProxiesAvailable
condition|)
block|{
return|return
name|getConstructorProxyMap
argument_list|(
literal|false
argument_list|)
operator|.
name|get
argument_list|(
name|serviceClass
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|ThreadLocalProxy
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
name|getConstructorProxyMap
parameter_list|(
name|boolean
name|create
parameter_list|)
block|{
name|Object
name|property
init|=
name|bus
operator|.
name|getProperty
argument_list|(
name|CONSTRUCTOR_PROXY_MAP
argument_list|)
decl_stmt|;
if|if
condition|(
name|property
operator|==
literal|null
condition|)
block|{
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|ThreadLocalProxy
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
name|map
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|bus
operator|.
name|setProperty
argument_list|(
name|CONSTRUCTOR_PROXY_MAP
argument_list|,
name|map
argument_list|)
expr_stmt|;
name|property
operator|=
name|map
expr_stmt|;
block|}
return|return
operator|(
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|ThreadLocalProxy
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
operator|)
name|property
return|;
block|}
specifier|private
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Map
argument_list|<
name|Field
argument_list|,
name|ThreadLocalProxy
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
name|getFieldProxyMap
parameter_list|(
name|boolean
name|create
parameter_list|)
block|{
return|return
name|getProxyMap
argument_list|(
name|FIELD_PROXY_MAP
argument_list|,
name|create
argument_list|)
return|;
block|}
specifier|private
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Map
argument_list|<
name|Method
argument_list|,
name|ThreadLocalProxy
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
name|getSetterProxyMap
parameter_list|(
name|boolean
name|create
parameter_list|)
block|{
return|return
name|getProxyMap
argument_list|(
name|SETTER_PROXY_MAP
argument_list|,
name|create
argument_list|)
return|;
block|}
specifier|private
name|void
name|findContextSetterMethods
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|Object
name|provider
parameter_list|)
block|{
for|for
control|(
name|Method
name|m
range|:
name|cls
operator|.
name|getMethods
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|m
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"set"
argument_list|)
operator|||
name|m
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
operator|!=
literal|1
condition|)
block|{
continue|continue;
block|}
for|for
control|(
name|Annotation
name|a
range|:
name|m
operator|.
name|getAnnotations
argument_list|()
control|)
block|{
if|if
condition|(
name|a
operator|.
name|annotationType
argument_list|()
operator|==
name|Context
operator|.
name|class
condition|)
block|{
name|checkContextMethod
argument_list|(
name|m
argument_list|,
name|provider
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|interfaces
init|=
name|cls
operator|.
name|getInterfaces
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|i
range|:
name|interfaces
control|)
block|{
name|findContextSetterMethods
argument_list|(
name|i
argument_list|,
name|provider
argument_list|)
expr_stmt|;
block|}
name|Class
argument_list|<
name|?
argument_list|>
name|superCls
init|=
name|cls
operator|.
name|getSuperclass
argument_list|()
decl_stmt|;
if|if
condition|(
name|superCls
operator|!=
literal|null
operator|&&
name|superCls
operator|!=
name|Object
operator|.
name|class
condition|)
block|{
name|findContextSetterMethods
argument_list|(
name|superCls
argument_list|,
name|provider
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|checkContextMethod
parameter_list|(
name|Method
name|m
parameter_list|,
name|Object
name|provider
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|m
operator|.
name|getParameterTypes
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
if|if
condition|(
name|type
operator|.
name|isInterface
argument_list|()
operator|||
name|type
operator|==
name|Application
operator|.
name|class
condition|)
block|{
name|checkContextClass
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|addContextMethod
argument_list|(
name|type
argument_list|,
name|m
argument_list|,
name|provider
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|checkContextClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
if|if
condition|(
operator|!
name|InjectionUtils
operator|.
name|STANDARD_CONTEXT_CLASSES
operator|.
name|contains
argument_list|(
name|type
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Injecting a custom context "
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|", ContextProvider is required for this type"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Method
argument_list|>
name|getContextMethods
parameter_list|()
block|{
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Method
argument_list|>
name|methods
init|=
name|contextMethods
operator|==
literal|null
condition|?
literal|null
else|:
name|contextMethods
operator|.
name|get
argument_list|(
name|getServiceClass
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|methods
operator|==
literal|null
condition|?
name|Collections
operator|.
name|emptyMap
argument_list|()
else|:
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|methods
argument_list|)
return|;
block|}
specifier|private
name|void
name|addContextMethod
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|contextClass
parameter_list|,
name|Method
name|m
parameter_list|,
name|Object
name|provider
parameter_list|)
block|{
if|if
condition|(
name|contextMethods
operator|==
literal|null
condition|)
block|{
name|contextMethods
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|addToMap
argument_list|(
name|contextMethods
argument_list|,
name|contextClass
argument_list|,
name|m
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|InjectionUtils
operator|.
name|VALUE_CONTEXTS
operator|.
name|contains
argument_list|(
name|m
operator|.
name|getParameterTypes
argument_list|()
index|[
literal|0
index|]
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|addToMap
argument_list|(
name|getSetterProxyMap
argument_list|(
literal|true
argument_list|)
argument_list|,
name|m
argument_list|,
name|getMethodThreadLocalProxy
argument_list|(
name|m
argument_list|,
name|provider
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|isRoot
parameter_list|()
block|{
return|return
name|root
return|;
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getResourceClass
parameter_list|()
block|{
return|return
name|resourceClass
return|;
block|}
specifier|public
name|List
argument_list|<
name|Field
argument_list|>
name|getContextFields
parameter_list|()
block|{
return|return
name|getList
argument_list|(
name|contextFields
argument_list|)
return|;
block|}
specifier|public
name|ThreadLocalProxy
argument_list|<
name|?
argument_list|>
name|getContextFieldProxy
parameter_list|(
name|Field
name|f
parameter_list|)
block|{
return|return
name|getProxy
argument_list|(
name|getFieldProxyMap
argument_list|(
literal|true
argument_list|)
argument_list|,
name|f
argument_list|)
return|;
block|}
specifier|public
name|ThreadLocalProxy
argument_list|<
name|?
argument_list|>
name|getContextSetterProxy
parameter_list|(
name|Method
name|m
parameter_list|)
block|{
return|return
name|getProxy
argument_list|(
name|getSetterProxyMap
argument_list|(
literal|true
argument_list|)
argument_list|,
name|m
argument_list|)
return|;
block|}
specifier|public
specifier|abstract
name|boolean
name|isSingleton
parameter_list|()
function_decl|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|public
specifier|static
name|void
name|clearAllMaps
parameter_list|()
block|{
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|(
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
name|Object
name|property
init|=
name|bus
operator|.
name|getProperty
argument_list|(
name|FIELD_PROXY_MAP
argument_list|)
decl_stmt|;
if|if
condition|(
name|property
operator|!=
literal|null
condition|)
block|{
operator|(
operator|(
name|Map
operator|)
name|property
operator|)
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
name|property
operator|=
name|bus
operator|.
name|getProperty
argument_list|(
name|SETTER_PROXY_MAP
argument_list|)
expr_stmt|;
if|if
condition|(
name|property
operator|!=
literal|null
condition|)
block|{
operator|(
operator|(
name|Map
operator|)
name|property
operator|)
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
name|property
operator|=
name|bus
operator|.
name|getProperty
argument_list|(
name|CONSTRUCTOR_PROXY_MAP
argument_list|)
expr_stmt|;
if|if
condition|(
name|property
operator|!=
literal|null
condition|)
block|{
operator|(
operator|(
name|Map
operator|)
name|property
operator|)
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|clearThreadLocalProxies
parameter_list|()
block|{
name|clearProxies
argument_list|(
name|getFieldProxyMap
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|clearProxies
argument_list|(
name|getSetterProxyMap
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|clearProxies
argument_list|(
name|getConstructorProxyMap
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
parameter_list|<
name|T
parameter_list|>
name|void
name|clearProxies
parameter_list|(
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Map
argument_list|<
name|T
argument_list|,
name|ThreadLocalProxy
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
name|tlps
parameter_list|)
block|{
name|Map
argument_list|<
name|T
argument_list|,
name|ThreadLocalProxy
argument_list|<
name|?
argument_list|>
argument_list|>
name|proxies
init|=
name|tlps
operator|==
literal|null
condition|?
literal|null
else|:
name|tlps
operator|.
name|get
argument_list|(
name|getServiceClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|proxies
operator|==
literal|null
condition|)
block|{
return|return;
block|}
for|for
control|(
name|ThreadLocalProxy
argument_list|<
name|?
argument_list|>
name|tlp
range|:
name|proxies
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|tlp
operator|!=
literal|null
condition|)
block|{
name|tlp
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|List
argument_list|<
name|Field
argument_list|>
argument_list|>
name|addContextField
parameter_list|(
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|List
argument_list|<
name|Field
argument_list|>
argument_list|>
name|theFields
parameter_list|,
name|Field
name|f
parameter_list|)
block|{
if|if
condition|(
name|theFields
operator|==
literal|null
condition|)
block|{
name|theFields
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|List
argument_list|<
name|Field
argument_list|>
name|fields
init|=
name|theFields
operator|.
name|get
argument_list|(
name|serviceClass
argument_list|)
decl_stmt|;
if|if
condition|(
name|fields
operator|==
literal|null
condition|)
block|{
name|fields
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|theFields
operator|.
name|put
argument_list|(
name|serviceClass
argument_list|,
name|fields
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|fields
operator|.
name|contains
argument_list|(
name|f
argument_list|)
condition|)
block|{
name|fields
operator|.
name|add
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
return|return
name|theFields
return|;
block|}
specifier|private
parameter_list|<
name|T
parameter_list|,
name|V
parameter_list|>
name|void
name|addToMap
parameter_list|(
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Map
argument_list|<
name|T
argument_list|,
name|V
argument_list|>
argument_list|>
name|proxyMap
parameter_list|,
name|T
name|f
parameter_list|,
name|V
name|proxy
parameter_list|)
block|{
name|Map
argument_list|<
name|T
argument_list|,
name|V
argument_list|>
name|proxies
init|=
name|proxyMap
operator|.
name|get
argument_list|(
name|serviceClass
argument_list|)
decl_stmt|;
if|if
condition|(
name|proxies
operator|==
literal|null
condition|)
block|{
name|proxies
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|T
argument_list|,
name|V
argument_list|>
argument_list|()
expr_stmt|;
name|proxyMap
operator|.
name|put
argument_list|(
name|serviceClass
argument_list|,
name|proxies
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|proxies
operator|.
name|containsKey
argument_list|(
name|f
argument_list|)
condition|)
block|{
name|proxies
operator|.
name|put
argument_list|(
name|f
argument_list|,
name|proxy
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|List
argument_list|<
name|Field
argument_list|>
name|getList
parameter_list|(
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|List
argument_list|<
name|Field
argument_list|>
argument_list|>
name|fields
parameter_list|)
block|{
name|List
argument_list|<
name|Field
argument_list|>
name|ret
init|=
name|fields
operator|==
literal|null
condition|?
literal|null
else|:
name|fields
operator|.
name|get
argument_list|(
name|getServiceClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ret
operator|!=
literal|null
condition|)
block|{
name|ret
operator|=
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|ret
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ret
operator|=
name|Collections
operator|.
name|emptyList
argument_list|()
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
specifier|private
parameter_list|<
name|T
parameter_list|>
name|ThreadLocalProxy
argument_list|<
name|?
argument_list|>
name|getProxy
parameter_list|(
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Map
argument_list|<
name|T
argument_list|,
name|ThreadLocalProxy
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
name|proxies
parameter_list|,
name|T
name|key
parameter_list|)
block|{
name|Map
argument_list|<
name|?
argument_list|,
name|ThreadLocalProxy
argument_list|<
name|?
argument_list|>
argument_list|>
name|theMap
init|=
name|proxies
operator|==
literal|null
condition|?
literal|null
else|:
name|proxies
operator|.
name|get
argument_list|(
name|getServiceClass
argument_list|()
argument_list|)
decl_stmt|;
name|ThreadLocalProxy
argument_list|<
name|?
argument_list|>
name|ret
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|theMap
operator|!=
literal|null
condition|)
block|{
name|ret
operator|=
name|theMap
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
block|}
end_class

end_unit

