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
name|servlet
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
name|Modifier
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
name|Set
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
name|ServletConfig
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
name|ServletException
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
name|ext
operator|.
name|Provider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|ClassUtils
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
name|JAXRSServerFactoryBean
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
name|lifecycle
operator|.
name|PerRequestResourceProvider
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
name|lifecycle
operator|.
name|ResourceProvider
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
name|lifecycle
operator|.
name|SingletonResourceProvider
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
name|ResourceUtils
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
name|transport
operator|.
name|servlet
operator|.
name|CXFNonSpringServlet
import|;
end_import

begin_class
specifier|public
class|class
name|CXFNonSpringJaxrsServlet
extends|extends
name|CXFNonSpringServlet
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
name|CXFNonSpringJaxrsServlet
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SERVICE_ADDRESS_PARAM
init|=
literal|"jaxrs.address"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SERVICE_CLASSES_PARAM
init|=
literal|"jaxrs.serviceClasses"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PROVIDERS_PARAM
init|=
literal|"jaxrs.providers"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SERVICE_SCOPE_PARAM
init|=
literal|"jaxrs.scope"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SERVICE_SCOPE_SINGLETON
init|=
literal|"singleton"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SERVICE_SCOPE_REQUEST
init|=
literal|"prototype"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|JAXRS_APPLICATION_PARAM
init|=
literal|"javax.ws.rs.Application"
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|loadBus
parameter_list|(
name|ServletConfig
name|servletConfig
parameter_list|)
throws|throws
name|ServletException
block|{
name|super
operator|.
name|loadBus
argument_list|(
name|servletConfig
argument_list|)
expr_stmt|;
name|String
name|applicationClass
init|=
name|servletConfig
operator|.
name|getInitParameter
argument_list|(
name|JAXRS_APPLICATION_PARAM
argument_list|)
decl_stmt|;
if|if
condition|(
name|applicationClass
operator|!=
literal|null
condition|)
block|{
name|createServerFromApplication
argument_list|(
name|applicationClass
argument_list|)
expr_stmt|;
return|return;
block|}
name|String
name|address
init|=
name|servletConfig
operator|.
name|getInitParameter
argument_list|(
name|SERVICE_ADDRESS_PARAM
argument_list|)
decl_stmt|;
if|if
condition|(
name|address
operator|==
literal|null
condition|)
block|{
name|address
operator|=
literal|"/"
expr_stmt|;
block|}
name|List
argument_list|<
name|Class
argument_list|>
name|resourceClasses
init|=
name|getServiceClasses
argument_list|(
name|servletConfig
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|Class
argument_list|,
name|ResourceProvider
argument_list|>
name|resourceProviders
init|=
name|getResourceProviders
argument_list|(
name|servletConfig
argument_list|,
name|resourceClasses
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|providers
init|=
name|getProviders
argument_list|(
name|servletConfig
argument_list|)
decl_stmt|;
name|JAXRSServerFactoryBean
name|bean
init|=
operator|new
name|JAXRSServerFactoryBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setResourceClasses
argument_list|(
name|resourceClasses
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setProviders
argument_list|(
name|providers
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Class
argument_list|,
name|ResourceProvider
argument_list|>
name|entry
range|:
name|resourceProviders
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|bean
operator|.
name|setResourceProvider
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|bean
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|List
argument_list|<
name|Class
argument_list|>
name|getServiceClasses
parameter_list|(
name|ServletConfig
name|servletConfig
parameter_list|)
throws|throws
name|ServletException
block|{
name|String
name|serviceBeans
init|=
name|servletConfig
operator|.
name|getInitParameter
argument_list|(
name|SERVICE_CLASSES_PARAM
argument_list|)
decl_stmt|;
if|if
condition|(
name|serviceBeans
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"At least one resource class should be specified"
argument_list|)
throw|;
block|}
name|String
index|[]
name|classNames
init|=
name|serviceBeans
operator|.
name|split
argument_list|(
literal|" "
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Class
argument_list|>
name|resourceClasses
init|=
operator|new
name|ArrayList
argument_list|<
name|Class
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|cName
range|:
name|classNames
control|)
block|{
if|if
condition|(
name|cName
operator|.
name|length
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
name|loadClass
argument_list|(
name|cName
argument_list|)
decl_stmt|;
name|resourceClasses
operator|.
name|add
argument_list|(
name|cls
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|resourceClasses
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"At least one resource class should be specified"
argument_list|)
throw|;
block|}
return|return
name|resourceClasses
return|;
block|}
specifier|protected
name|List
argument_list|<
name|?
argument_list|>
name|getProviders
parameter_list|(
name|ServletConfig
name|servletConfig
parameter_list|)
throws|throws
name|ServletException
block|{
name|String
name|providersList
init|=
name|servletConfig
operator|.
name|getInitParameter
argument_list|(
name|PROVIDERS_PARAM
argument_list|)
decl_stmt|;
if|if
condition|(
name|providersList
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|EMPTY_LIST
return|;
block|}
name|String
index|[]
name|classNames
init|=
name|providersList
operator|.
name|split
argument_list|(
literal|" "
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|providers
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|cName
range|:
name|classNames
control|)
block|{
if|if
condition|(
name|cName
operator|.
name|length
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
name|loadClass
argument_list|(
name|cName
argument_list|)
decl_stmt|;
name|providers
operator|.
name|add
argument_list|(
name|createSingletonInstance
argument_list|(
name|cls
argument_list|,
name|servletConfig
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|providers
return|;
block|}
specifier|protected
name|Map
argument_list|<
name|Class
argument_list|,
name|ResourceProvider
argument_list|>
name|getResourceProviders
parameter_list|(
name|ServletConfig
name|servletConfig
parameter_list|,
name|List
argument_list|<
name|Class
argument_list|>
name|resourceClasses
parameter_list|)
throws|throws
name|ServletException
block|{
name|String
name|scope
init|=
name|servletConfig
operator|.
name|getInitParameter
argument_list|(
name|SERVICE_SCOPE_PARAM
argument_list|)
decl_stmt|;
if|if
condition|(
name|scope
operator|!=
literal|null
operator|&&
operator|!
name|SERVICE_SCOPE_SINGLETON
operator|.
name|equals
argument_list|(
name|scope
argument_list|)
operator|&&
operator|!
name|SERVICE_SCOPE_REQUEST
operator|.
name|equals
argument_list|(
name|scope
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"Only singleton and prototype scopes are supported"
argument_list|)
throw|;
block|}
name|boolean
name|isPrototype
init|=
name|SERVICE_SCOPE_REQUEST
operator|.
name|equals
argument_list|(
name|scope
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|Class
argument_list|,
name|ResourceProvider
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|Class
argument_list|,
name|ResourceProvider
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
name|c
range|:
name|resourceClasses
control|)
block|{
name|map
operator|.
name|put
argument_list|(
name|c
argument_list|,
name|isPrototype
condition|?
operator|new
name|PerRequestResourceProvider
argument_list|(
name|c
argument_list|)
else|:
operator|new
name|SingletonResourceProvider
argument_list|(
name|createSingletonInstance
argument_list|(
name|c
argument_list|,
name|servletConfig
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|map
return|;
block|}
specifier|private
name|Object
name|createSingletonInstance
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|ServletConfig
name|sc
parameter_list|)
throws|throws
name|ServletException
block|{
name|Constructor
name|c
init|=
name|ResourceUtils
operator|.
name|findResourceConstructor
argument_list|(
name|cls
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"No valid constructor found for "
operator|+
name|cls
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|boolean
name|isDefault
init|=
name|c
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
operator|==
literal|0
decl_stmt|;
if|if
condition|(
operator|!
name|isDefault
operator|&&
operator|(
name|c
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
operator|!=
literal|1
operator|||
name|c
operator|.
name|getParameterTypes
argument_list|()
index|[
literal|0
index|]
operator|!=
name|ServletConfig
operator|.
name|class
operator|&&
name|c
operator|.
name|getParameterTypes
argument_list|()
index|[
literal|0
index|]
operator|!=
name|ServletContext
operator|.
name|class
operator|)
condition|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"Resource classes with singleton scope can only have "
operator|+
literal|"ServletConfig or ServletContext instances injected through their constructors"
argument_list|)
throw|;
block|}
name|Object
index|[]
name|values
init|=
name|isDefault
condition|?
operator|new
name|Object
index|[]
block|{}
else|:
operator|new
name|Object
index|[]
block|{
name|c
operator|.
name|getParameterTypes
argument_list|()
index|[
literal|0
index|]
operator|==
name|ServletConfig
operator|.
name|class
condition|?
name|sc
else|:
name|sc
operator|.
name|getServletContext
argument_list|()
block|}
decl_stmt|;
try|try
block|{
return|return
name|c
operator|.
name|newInstance
argument_list|(
name|values
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|InstantiationException
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"Resource class "
operator|+
name|cls
operator|.
name|getName
argument_list|()
operator|+
literal|" can not be instantiated"
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"Resource class "
operator|+
name|cls
operator|.
name|getName
argument_list|()
operator|+
literal|" can not be instantiated due to IllegalAccessException"
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"Resource class "
operator|+
name|cls
operator|.
name|getName
argument_list|()
operator|+
literal|" can not be instantiated due to InvocationTargetException"
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|void
name|createServerFromApplication
parameter_list|(
name|String
name|cName
parameter_list|)
throws|throws
name|ServletException
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|appClass
init|=
name|loadClass
argument_list|(
name|cName
argument_list|,
literal|"Application"
argument_list|)
decl_stmt|;
name|Application
name|app
init|=
literal|null
decl_stmt|;
try|try
block|{
name|app
operator|=
operator|(
name|Application
operator|)
name|appClass
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InstantiationException
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"Application class "
operator|+
name|cName
operator|+
literal|" can not be instantiated"
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"Application class "
operator|+
name|cName
operator|+
literal|" can not be instantiated due to IllegalAccessException"
argument_list|)
throw|;
block|}
name|verifySingletons
argument_list|(
name|app
operator|.
name|getSingletons
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Class
argument_list|>
name|resourceClasses
init|=
operator|new
name|ArrayList
argument_list|<
name|Class
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|providers
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|Class
argument_list|,
name|ResourceProvider
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|Class
argument_list|,
name|ResourceProvider
argument_list|>
argument_list|()
decl_stmt|;
comment|// at the moment we don't support per-request providers, only resource classes
comment|// Note, app.getClasse() returns a list of per-resource classes
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
range|:
name|app
operator|.
name|getClasses
argument_list|()
control|)
block|{
if|if
condition|(
name|isValidPerRequestResourceClass
argument_list|(
name|c
argument_list|,
name|app
operator|.
name|getSingletons
argument_list|()
argument_list|)
condition|)
block|{
name|resourceClasses
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|c
argument_list|,
operator|new
name|PerRequestResourceProvider
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// we can get either a provider or resource class here
for|for
control|(
name|Object
name|o
range|:
name|app
operator|.
name|getSingletons
argument_list|()
control|)
block|{
name|boolean
name|isProvider
init|=
name|o
operator|.
name|getClass
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|Provider
operator|.
name|class
argument_list|)
operator|!=
literal|null
decl_stmt|;
if|if
condition|(
name|isProvider
condition|)
block|{
name|providers
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|resourceClasses
operator|.
name|add
argument_list|(
name|o
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|o
operator|.
name|getClass
argument_list|()
argument_list|,
operator|new
name|SingletonResourceProvider
argument_list|(
name|o
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|JAXRSServerFactoryBean
name|bean
init|=
operator|new
name|JAXRSServerFactoryBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setAddress
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setResourceClasses
argument_list|(
name|resourceClasses
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setProviders
argument_list|(
name|providers
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Class
argument_list|,
name|ResourceProvider
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|bean
operator|.
name|setResourceProvider
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|bean
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|loadClass
parameter_list|(
name|String
name|cName
parameter_list|)
throws|throws
name|ServletException
block|{
return|return
name|loadClass
argument_list|(
name|cName
argument_list|,
literal|"Resource"
argument_list|)
return|;
block|}
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|loadClass
parameter_list|(
name|String
name|cName
parameter_list|,
name|String
name|classType
parameter_list|)
throws|throws
name|ServletException
block|{
try|try
block|{
return|return
name|ClassUtils
operator|.
name|getClass
argument_list|(
name|CXFNonSpringJaxrsServlet
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|,
name|cName
operator|.
name|trim
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"No "
operator|+
name|classType
operator|+
literal|" class "
operator|+
name|cName
operator|.
name|trim
argument_list|()
operator|+
literal|" can be found"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|private
name|boolean
name|isValidResourceClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
parameter_list|)
block|{
if|if
condition|(
name|c
operator|.
name|isInterface
argument_list|()
operator|||
name|Modifier
operator|.
name|isAbstract
argument_list|(
name|c
operator|.
name|getModifiers
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Ignoring invalid resource class "
operator|+
name|c
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|boolean
name|isValidPerRequestResourceClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
parameter_list|,
name|Set
argument_list|<
name|Object
argument_list|>
name|singletons
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isValidResourceClass
argument_list|(
name|c
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|Object
name|s
range|:
name|singletons
control|)
block|{
if|if
condition|(
name|c
operator|==
name|s
operator|.
name|getClass
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Ignoring per-request resource class "
operator|+
name|c
operator|.
name|getName
argument_list|()
operator|+
literal|" as it is also registered as singleton"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|void
name|verifySingletons
parameter_list|(
name|Set
argument_list|<
name|Object
argument_list|>
name|singletons
parameter_list|)
throws|throws
name|ServletException
block|{
if|if
condition|(
name|singletons
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|Set
argument_list|<
name|String
argument_list|>
name|map
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|s
range|:
name|singletons
control|)
block|{
if|if
condition|(
name|map
operator|.
name|contains
argument_list|(
name|s
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"More than one instance of the same singleton class "
operator|+
name|s
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" is available"
argument_list|)
throw|;
block|}
else|else
block|{
name|map
operator|.
name|add
argument_list|(
name|s
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

