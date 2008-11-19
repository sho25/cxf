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
name|utils
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
name|Constructor
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
name|ParameterizedType
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
name|lang
operator|.
name|reflect
operator|.
name|Type
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|AccessController
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivilegedAction
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
name|HashSet
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
name|ResourceBundle
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
name|SortedSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
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
name|servlet
operator|.
name|ServletContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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
name|HttpHeaders
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
name|MultivaluedMap
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
name|Request
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
name|SecurityContext
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
name|UriInfo
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
name|ext
operator|.
name|ContextResolver
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
name|ext
operator|.
name|Providers
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
name|i18n
operator|.
name|BundleUtils
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
name|common
operator|.
name|util
operator|.
name|PrimitiveUtils
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
name|ext
operator|.
name|MessageContext
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
name|ThreadLocalContextResolver
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
name|ThreadLocalHttpHeaders
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
name|ThreadLocalHttpServletRequest
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
name|ThreadLocalHttpServletResponse
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
name|ThreadLocalMessageBodyWorkers
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
name|ThreadLocalMessageContext
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
name|impl
operator|.
name|tl
operator|.
name|ThreadLocalRequest
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
name|ThreadLocalSecurityContext
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
name|ThreadLocalServletContext
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
name|ThreadLocalUriInfo
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
name|message
operator|.
name|Message
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|InjectionUtils
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
name|InjectionUtils
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ResourceBundle
name|BUNDLE
init|=
name|BundleUtils
operator|.
name|getBundle
argument_list|(
name|InjectionUtils
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|InjectionUtils
parameter_list|()
block|{              }
specifier|public
specifier|static
name|Method
name|checkProxy
parameter_list|(
name|Method
name|methodToInvoke
parameter_list|,
name|Object
name|resourceObject
parameter_list|)
block|{
if|if
condition|(
name|Proxy
operator|.
name|class
operator|.
name|isInstance
argument_list|(
name|resourceObject
argument_list|)
condition|)
block|{
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
range|:
name|resourceObject
operator|.
name|getClass
argument_list|()
operator|.
name|getInterfaces
argument_list|()
control|)
block|{
try|try
block|{
name|Method
name|m
init|=
name|c
operator|.
name|getMethod
argument_list|(
name|methodToInvoke
operator|.
name|getName
argument_list|()
argument_list|,
name|methodToInvoke
operator|.
name|getParameterTypes
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|!=
literal|null
condition|)
block|{
return|return
name|m
return|;
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|ex
parameter_list|)
block|{
comment|//ignore
block|}
block|}
block|}
return|return
name|methodToInvoke
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
specifier|static
name|void
name|injectFieldValue
parameter_list|(
specifier|final
name|Field
name|f
parameter_list|,
specifier|final
name|Object
name|o
parameter_list|,
specifier|final
name|Object
name|v
parameter_list|)
block|{
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|PrivilegedAction
argument_list|()
block|{
specifier|public
name|Object
name|run
parameter_list|()
block|{
name|f
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
name|f
operator|.
name|set
argument_list|(
name|o
argument_list|,
name|v
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
argument_list|(
literal|"FIELD_INJECTION_FAILURE"
argument_list|,
name|BUNDLE
argument_list|,
name|f
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|Class
argument_list|<
name|?
argument_list|>
name|getActualType
parameter_list|(
name|Type
name|genericType
parameter_list|)
block|{
if|if
condition|(
name|genericType
operator|==
literal|null
operator|||
operator|!
name|ParameterizedType
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|genericType
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|ParameterizedType
name|paramType
init|=
operator|(
name|ParameterizedType
operator|)
name|genericType
decl_stmt|;
return|return
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|paramType
operator|.
name|getActualTypeArguments
argument_list|()
index|[
literal|0
index|]
return|;
block|}
specifier|public
specifier|static
name|void
name|injectThroughMethod
parameter_list|(
name|Object
name|requestObject
parameter_list|,
name|Method
name|method
parameter_list|,
name|Object
name|parameterValue
parameter_list|)
block|{
try|try
block|{
name|Method
name|methodToInvoke
init|=
name|checkProxy
argument_list|(
name|method
argument_list|,
name|requestObject
argument_list|)
decl_stmt|;
name|methodToInvoke
operator|.
name|invoke
argument_list|(
name|requestObject
argument_list|,
operator|new
name|Object
index|[]
block|{
name|parameterValue
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
argument_list|(
literal|"METHOD_INJECTION_FAILURE"
argument_list|,
name|BUNDLE
argument_list|,
name|method
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|Object
name|handleParameter
parameter_list|(
name|String
name|value
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|pClass
parameter_list|)
block|{
if|if
condition|(
name|pClass
operator|.
name|isPrimitive
argument_list|()
condition|)
block|{
return|return
name|PrimitiveUtils
operator|.
name|read
argument_list|(
name|value
argument_list|,
name|pClass
argument_list|)
return|;
block|}
comment|// check constructors accepting a single String value
try|try
block|{
name|Constructor
argument_list|<
name|?
argument_list|>
name|c
init|=
name|pClass
operator|.
name|getConstructor
argument_list|(
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[]
block|{
name|String
operator|.
name|class
block|}
block|)
empty_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
return|return
name|c
operator|.
name|newInstance
argument_list|(
operator|new
name|Object
index|[]
block|{
name|value
block|}
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// try valueOf
block|}
comment|// check for valueOf(String) static methods
try|try
block|{
name|Method
name|m
init|=
name|pClass
operator|.
name|getMethod
argument_list|(
literal|"valueOf"
argument_list|,
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[]
block|{
name|String
operator|.
name|class
block|}
block|)
empty_stmt|;
if|if
condition|(
name|m
operator|!=
literal|null
operator|&&
name|Modifier
operator|.
name|isStatic
argument_list|(
name|m
operator|.
name|getModifiers
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|m
operator|.
name|invoke
argument_list|(
literal|null
argument_list|,
operator|new
name|Object
index|[]
block|{
name|value
block|}
argument_list|)
return|;
block|}
block|}
end_class

begin_catch
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// no luck
block|}
end_catch

begin_return
return|return
literal|null
return|;
end_return

begin_function
unit|}          public
specifier|static
name|Object
name|handleBean
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|paramType
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
parameter_list|)
block|{
name|Object
name|bean
init|=
literal|null
decl_stmt|;
try|try
block|{
name|bean
operator|=
name|paramType
operator|.
name|newInstance
argument_list|()
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|entry
range|:
name|values
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|boolean
name|injected
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Method
name|m
range|:
name|paramType
operator|.
name|getMethods
argument_list|()
control|)
block|{
if|if
condition|(
name|m
operator|.
name|getName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"set"
operator|+
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
operator|&&
name|m
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
operator|==
literal|1
condition|)
block|{
name|Object
name|paramValue
init|=
name|handleParameter
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|m
operator|.
name|getParameterTypes
argument_list|()
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|paramValue
operator|!=
literal|null
condition|)
block|{
name|injectThroughMethod
argument_list|(
name|bean
argument_list|,
name|m
argument_list|,
name|paramValue
argument_list|)
expr_stmt|;
name|injected
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
block|}
if|if
condition|(
name|injected
condition|)
block|{
continue|continue;
block|}
for|for
control|(
name|Field
name|f
range|:
name|paramType
operator|.
name|getFields
argument_list|()
control|)
block|{
if|if
condition|(
name|f
operator|.
name|getName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|Object
name|paramValue
init|=
name|handleParameter
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|f
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|paramValue
operator|!=
literal|null
condition|)
block|{
name|injectFieldValue
argument_list|(
name|f
argument_list|,
name|bean
argument_list|,
name|paramValue
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
argument_list|(
literal|"CLASS_INSTANCIATION_FAILURE"
argument_list|,
name|BUNDLE
argument_list|,
name|paramType
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|bean
return|;
block|}
end_function

begin_function
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
specifier|static
name|Object
name|injectIntoList
parameter_list|(
name|Type
name|genericType
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|values
parameter_list|,
name|boolean
name|decoded
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|realType
init|=
name|InjectionUtils
operator|.
name|getActualType
argument_list|(
name|genericType
argument_list|)
decl_stmt|;
name|List
name|theValues
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|r
range|:
name|values
control|)
block|{
if|if
condition|(
name|decoded
condition|)
block|{
name|r
operator|=
name|JAXRSUtils
operator|.
name|uriDecode
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
name|Object
name|o
init|=
name|InjectionUtils
operator|.
name|handleParameter
argument_list|(
name|r
argument_list|,
name|realType
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|!=
literal|null
condition|)
block|{
name|theValues
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|theValues
return|;
block|}
end_function

begin_function
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
specifier|static
name|Object
name|injectIntoSet
parameter_list|(
name|Type
name|genericType
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|values
parameter_list|,
name|boolean
name|sorted
parameter_list|,
name|boolean
name|decoded
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|realType
init|=
name|InjectionUtils
operator|.
name|getActualType
argument_list|(
name|genericType
argument_list|)
decl_stmt|;
name|Set
name|theValues
init|=
name|sorted
condition|?
operator|new
name|TreeSet
argument_list|()
else|:
operator|new
name|HashSet
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|r
range|:
name|values
control|)
block|{
if|if
condition|(
name|decoded
condition|)
block|{
name|r
operator|=
name|JAXRSUtils
operator|.
name|uriDecode
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
name|Object
name|o
init|=
name|InjectionUtils
operator|.
name|handleParameter
argument_list|(
name|r
argument_list|,
name|realType
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|!=
literal|null
condition|)
block|{
name|theValues
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|theValues
return|;
block|}
end_function

begin_function
specifier|public
specifier|static
name|Object
name|createParameterObject
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|paramValues
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|paramType
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|String
name|defaultValue
parameter_list|,
name|boolean
name|isLast
parameter_list|,
name|boolean
name|decoded
parameter_list|)
block|{
if|if
condition|(
name|paramValues
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|defaultValue
operator|!=
literal|null
condition|)
block|{
name|paramValues
operator|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|defaultValue
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|paramType
operator|.
name|isPrimitive
argument_list|()
condition|)
block|{
name|paramValues
operator|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|boolean
operator|.
name|class
operator|==
name|paramType
condition|?
literal|"false"
else|:
literal|"0"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
if|if
condition|(
name|List
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|paramType
argument_list|)
condition|)
block|{
return|return
name|InjectionUtils
operator|.
name|injectIntoList
argument_list|(
name|genericType
argument_list|,
name|paramValues
argument_list|,
name|decoded
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|Set
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|paramType
argument_list|)
condition|)
block|{
return|return
name|InjectionUtils
operator|.
name|injectIntoSet
argument_list|(
name|genericType
argument_list|,
name|paramValues
argument_list|,
literal|false
argument_list|,
name|decoded
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|SortedSet
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|paramType
argument_list|)
condition|)
block|{
return|return
name|InjectionUtils
operator|.
name|injectIntoSet
argument_list|(
name|genericType
argument_list|,
name|paramValues
argument_list|,
literal|true
argument_list|,
name|decoded
argument_list|)
return|;
block|}
else|else
block|{
name|String
name|result
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|paramValues
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|result
operator|=
name|isLast
condition|?
name|paramValues
operator|.
name|get
argument_list|(
name|paramValues
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
else|:
name|paramValues
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|decoded
condition|)
block|{
name|result
operator|=
name|JAXRSUtils
operator|.
name|uriDecode
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
return|return
name|InjectionUtils
operator|.
name|handleParameter
argument_list|(
name|result
argument_list|,
name|paramType
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_function

begin_function
specifier|public
specifier|static
name|ThreadLocalProxy
name|createThreadLocalProxy
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|ThreadLocalProxy
name|proxy
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|UriInfo
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|proxy
operator|=
operator|new
name|ThreadLocalUriInfo
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|HttpHeaders
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|proxy
operator|=
operator|new
name|ThreadLocalHttpHeaders
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SecurityContext
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|proxy
operator|=
operator|new
name|ThreadLocalSecurityContext
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ContextResolver
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|proxy
operator|=
operator|new
name|ThreadLocalContextResolver
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Request
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|proxy
operator|=
operator|new
name|ThreadLocalRequest
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Providers
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|proxy
operator|=
operator|new
name|ThreadLocalMessageBodyWorkers
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|HttpServletRequest
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|proxy
operator|=
operator|new
name|ThreadLocalHttpServletRequest
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ServletContext
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|proxy
operator|=
operator|new
name|ThreadLocalServletContext
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|HttpServletResponse
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|proxy
operator|=
operator|new
name|ThreadLocalHttpServletResponse
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|MessageContext
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|proxy
operator|=
operator|new
name|ThreadLocalMessageContext
argument_list|()
expr_stmt|;
block|}
return|return
name|proxy
return|;
block|}
end_function

begin_function
specifier|public
specifier|static
name|void
name|injectContextProxies
parameter_list|(
name|AbstractResourceInfo
name|cri
parameter_list|,
name|Object
name|instance
parameter_list|)
block|{
if|if
condition|(
operator|!
name|cri
operator|.
name|isSingleton
argument_list|()
condition|)
block|{
return|return;
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Method
argument_list|>
name|entry
range|:
name|cri
operator|.
name|getContextMethods
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|ThreadLocalProxy
name|proxy
init|=
name|cri
operator|.
name|getContextSetterProxy
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|InjectionUtils
operator|.
name|injectThroughMethod
argument_list|(
name|instance
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|proxy
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Field
name|f
range|:
name|cri
operator|.
name|getContextFields
argument_list|()
control|)
block|{
name|ThreadLocalProxy
name|proxy
init|=
name|cri
operator|.
name|getContextFieldProxy
argument_list|(
name|f
argument_list|)
decl_stmt|;
name|InjectionUtils
operator|.
name|injectFieldValue
argument_list|(
name|f
argument_list|,
name|instance
argument_list|,
name|proxy
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Field
name|f
range|:
name|cri
operator|.
name|getResourceFields
argument_list|()
control|)
block|{
name|ThreadLocalProxy
name|proxy
init|=
name|cri
operator|.
name|getResourceFieldProxy
argument_list|(
name|f
argument_list|)
decl_stmt|;
name|InjectionUtils
operator|.
name|injectFieldValue
argument_list|(
name|f
argument_list|,
name|instance
argument_list|,
name|proxy
argument_list|)
expr_stmt|;
block|}
block|}
end_function

begin_function
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
specifier|static
name|void
name|injectContextField
parameter_list|(
name|AbstractResourceInfo
name|cri
parameter_list|,
name|Field
name|f
parameter_list|,
name|Object
name|o
parameter_list|,
name|Object
name|value
parameter_list|,
name|boolean
name|resource
parameter_list|)
block|{
if|if
condition|(
operator|!
name|cri
operator|.
name|isSingleton
argument_list|()
condition|)
block|{
name|InjectionUtils
operator|.
name|injectFieldValue
argument_list|(
name|f
argument_list|,
name|o
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ThreadLocalProxy
name|proxy
init|=
name|resource
condition|?
name|cri
operator|.
name|getResourceFieldProxy
argument_list|(
name|f
argument_list|)
else|:
name|cri
operator|.
name|getContextFieldProxy
argument_list|(
name|f
argument_list|)
decl_stmt|;
if|if
condition|(
name|proxy
operator|!=
literal|null
condition|)
block|{
name|proxy
operator|.
name|set
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_function

begin_function
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
specifier|static
name|void
name|injectContextMethods
parameter_list|(
name|Object
name|requestObject
parameter_list|,
name|AbstractResourceInfo
name|cri
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Method
argument_list|>
name|entry
range|:
name|cri
operator|.
name|getContextMethods
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Object
name|o
init|=
name|JAXRSUtils
operator|.
name|createContextValue
argument_list|(
name|message
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|getGenericParameterTypes
argument_list|()
index|[
literal|0
index|]
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|cri
operator|.
name|isSingleton
argument_list|()
condition|)
block|{
name|InjectionUtils
operator|.
name|injectThroughMethod
argument_list|(
name|requestObject
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|o
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ThreadLocalProxy
name|proxy
init|=
name|cri
operator|.
name|getContextSetterProxy
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|proxy
operator|!=
literal|null
condition|)
block|{
name|proxy
operator|.
name|set
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_function

begin_comment
comment|// TODO : should we have context and resource fields be treated as context fields ?
end_comment

begin_function
specifier|public
specifier|static
name|void
name|injectContextFields
parameter_list|(
name|Object
name|o
parameter_list|,
name|AbstractResourceInfo
name|cri
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
for|for
control|(
name|Field
name|f
range|:
name|cri
operator|.
name|getContextFields
argument_list|()
control|)
block|{
name|Object
name|value
init|=
name|JAXRSUtils
operator|.
name|createContextValue
argument_list|(
name|m
argument_list|,
name|f
operator|.
name|getGenericType
argument_list|()
argument_list|,
name|f
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
name|InjectionUtils
operator|.
name|injectContextField
argument_list|(
name|cri
argument_list|,
name|f
argument_list|,
name|o
argument_list|,
name|value
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
end_function

begin_function
specifier|public
specifier|static
name|void
name|injectResourceFields
parameter_list|(
name|Object
name|o
parameter_list|,
name|AbstractResourceInfo
name|cri
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
for|for
control|(
name|Field
name|f
range|:
name|cri
operator|.
name|getResourceFields
argument_list|()
control|)
block|{
name|Object
name|value
init|=
name|JAXRSUtils
operator|.
name|createResourceValue
argument_list|(
name|m
argument_list|,
name|f
operator|.
name|getGenericType
argument_list|()
argument_list|,
name|f
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
name|InjectionUtils
operator|.
name|injectContextField
argument_list|(
name|cri
argument_list|,
name|f
argument_list|,
name|o
argument_list|,
name|value
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
end_function

unit|}
end_unit

