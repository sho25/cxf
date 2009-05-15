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
name|AnnotationUtils
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
specifier|private
specifier|static
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
specifier|static
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
name|resourceFields
decl_stmt|;
specifier|private
specifier|static
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
specifier|static
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
argument_list|>
argument_list|>
name|fieldProxyMap
decl_stmt|;
specifier|private
specifier|static
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
argument_list|>
argument_list|>
name|resourceProxyMap
decl_stmt|;
specifier|private
specifier|static
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
argument_list|>
argument_list|>
name|setterProxyMap
decl_stmt|;
specifier|private
name|boolean
name|root
decl_stmt|;
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|resourceClass
decl_stmt|;
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|serviceClass
decl_stmt|;
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
parameter_list|)
block|{
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
name|root
condition|)
block|{
name|initContextFields
argument_list|()
expr_stmt|;
name|initContextSetterMethods
argument_list|()
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
name|initContextFields
parameter_list|()
block|{
if|if
condition|(
name|resourceClass
operator|==
literal|null
operator|||
operator|!
name|root
condition|)
block|{
return|return;
block|}
name|findContextFields
argument_list|(
name|serviceClass
argument_list|)
expr_stmt|;
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
condition|)
block|{
if|if
condition|(
name|contextFields
operator|==
literal|null
condition|)
block|{
name|contextFields
operator|=
operator|new
name|HashMap
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
argument_list|()
expr_stmt|;
block|}
name|addContextField
argument_list|(
name|contextFields
argument_list|,
name|f
argument_list|)
expr_stmt|;
if|if
condition|(
name|fieldProxyMap
operator|==
literal|null
condition|)
block|{
name|fieldProxyMap
operator|=
operator|new
name|HashMap
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
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|addToMap
argument_list|(
name|fieldProxyMap
argument_list|,
name|f
argument_list|,
name|InjectionUtils
operator|.
name|createThreadLocalProxy
argument_list|(
name|f
operator|.
name|getType
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|a
operator|.
name|annotationType
argument_list|()
operator|==
name|Resource
operator|.
name|class
operator|&&
name|AnnotationUtils
operator|.
name|isContextClass
argument_list|(
name|f
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|resourceFields
operator|==
literal|null
condition|)
block|{
name|resourceFields
operator|=
operator|new
name|HashMap
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
argument_list|()
expr_stmt|;
block|}
name|addContextField
argument_list|(
name|resourceFields
argument_list|,
name|f
argument_list|)
expr_stmt|;
if|if
condition|(
name|resourceProxyMap
operator|==
literal|null
condition|)
block|{
name|resourceProxyMap
operator|=
operator|new
name|HashMap
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
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|addToMap
argument_list|(
name|resourceProxyMap
argument_list|,
name|f
argument_list|,
name|InjectionUtils
operator|.
name|createThreadLocalProxy
argument_list|(
name|f
operator|.
name|getType
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|findContextFields
argument_list|(
name|cls
operator|.
name|getSuperclass
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initContextSetterMethods
parameter_list|()
block|{
for|for
control|(
name|Method
name|m
range|:
name|getServiceClass
argument_list|()
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
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|checkContextMethod
parameter_list|(
name|Method
name|m
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
name|AnnotationUtils
operator|.
name|isContextClass
argument_list|(
name|type
argument_list|)
operator|&&
name|m
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"set"
operator|+
name|type
operator|.
name|getSimpleName
argument_list|()
argument_list|)
condition|)
block|{
name|addContextMethod
argument_list|(
name|type
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
name|EMPTY_MAP
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
name|setterProxyMap
operator|==
literal|null
condition|)
block|{
name|setterProxyMap
operator|=
operator|new
name|HashMap
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
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|addToMap
argument_list|(
name|setterProxyMap
argument_list|,
name|m
argument_list|,
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
argument_list|)
expr_stmt|;
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
name|List
argument_list|<
name|Field
argument_list|>
name|getResourceFields
parameter_list|()
block|{
return|return
name|getList
argument_list|(
name|resourceFields
argument_list|)
return|;
block|}
specifier|public
name|ThreadLocalProxy
name|getContextFieldProxy
parameter_list|(
name|Field
name|f
parameter_list|)
block|{
return|return
name|getProxy
argument_list|(
name|fieldProxyMap
argument_list|,
name|f
argument_list|)
return|;
block|}
specifier|public
name|ThreadLocalProxy
name|getResourceFieldProxy
parameter_list|(
name|Field
name|f
parameter_list|)
block|{
return|return
name|getProxy
argument_list|(
name|resourceProxyMap
argument_list|,
name|f
argument_list|)
return|;
block|}
specifier|public
name|ThreadLocalProxy
name|getContextSetterProxy
parameter_list|(
name|Method
name|m
parameter_list|)
block|{
return|return
name|getProxy
argument_list|(
name|setterProxyMap
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
specifier|public
name|void
name|clearThreadLocalProxies
parameter_list|()
block|{
name|clearProxies
argument_list|(
name|fieldProxyMap
argument_list|)
expr_stmt|;
name|clearProxies
argument_list|(
name|resourceProxyMap
argument_list|)
expr_stmt|;
name|clearProxies
argument_list|(
name|setterProxyMap
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
name|void
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
argument_list|<
name|Field
argument_list|>
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
name|theFields
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
name|theFields
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
name|HashMap
argument_list|<
name|T
argument_list|,
name|V
argument_list|>
argument_list|()
expr_stmt|;
name|theFields
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
name|T
argument_list|,
name|ThreadLocalProxy
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
return|return
name|theMap
operator|!=
literal|null
condition|?
name|theMap
operator|.
name|get
argument_list|(
name|key
argument_list|)
else|:
literal|null
return|;
block|}
block|}
end_class

end_unit

