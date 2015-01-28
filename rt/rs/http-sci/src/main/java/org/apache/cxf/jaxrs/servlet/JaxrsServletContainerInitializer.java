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
name|annotation
operator|.
name|Annotation
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
name|Collection
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
name|ServletContainerInitializer
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
name|servlet
operator|.
name|ServletRegistration
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletRegistration
operator|.
name|Dynamic
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|annotation
operator|.
name|HandlesTypes
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
name|ApplicationPath
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
name|Path
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
annotation|@
name|HandlesTypes
argument_list|(
block|{
name|Application
operator|.
name|class
block|,
name|Provider
operator|.
name|class
block|,
name|Path
operator|.
name|class
block|}
argument_list|)
specifier|public
class|class
name|JaxrsServletContainerInitializer
implements|implements
name|ServletContainerInitializer
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
name|JaxrsServletContainerInitializer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|IGNORE_PACKAGE
init|=
literal|"org.apache.cxf"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|JAXRS_APPLICATION_PARAM
init|=
literal|"javax.ws.rs.Application"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CXF_JAXRS_APPLICATION_PARAM
init|=
literal|"jaxrs.application"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CXF_JAXRS_CLASSES_PARAM
init|=
literal|"jaxrs.classes"
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|onStartup
parameter_list|(
specifier|final
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
parameter_list|,
specifier|final
name|ServletContext
name|ctx
parameter_list|)
throws|throws
name|ServletException
block|{
name|Application
name|app
init|=
literal|null
decl_stmt|;
name|String
name|servletName
init|=
literal|null
decl_stmt|;
name|String
name|servletMapping
init|=
literal|null
decl_stmt|;
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|appClass
init|=
name|findCandidate
argument_list|(
name|classes
argument_list|)
decl_stmt|;
if|if
condition|(
name|appClass
operator|!=
literal|null
condition|)
block|{
comment|// The best effort at detecting a CXFNonSpringJaxrsServlet handling this application.
comment|// Custom servlets using non-standard mechanisms to create Application will not be detected
if|if
condition|(
name|isApplicationServletAvailable
argument_list|(
name|ctx
argument_list|,
name|appClass
argument_list|)
condition|)
block|{
return|return;
block|}
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
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
name|t
argument_list|)
throw|;
block|}
comment|// Servlet name is the application class name
name|servletName
operator|=
name|appClass
operator|.
name|getName
argument_list|()
expr_stmt|;
name|ApplicationPath
name|appPath
init|=
name|appClass
operator|.
name|getAnnotation
argument_list|(
name|ApplicationPath
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// If ApplicationPath is available - use its value as a mapping otherwise get it from
comment|// a servlet registration with an application implementation class name
if|if
condition|(
name|appPath
operator|!=
literal|null
condition|)
block|{
name|servletMapping
operator|=
name|appPath
operator|.
name|value
argument_list|()
operator|+
literal|"/*"
expr_stmt|;
block|}
else|else
block|{
name|servletMapping
operator|=
name|getServletMapping
argument_list|(
name|ctx
argument_list|,
name|servletName
argument_list|)
expr_stmt|;
block|}
block|}
comment|// If application is null or empty then try to create a new application from available
comment|// resource and provider classes
if|if
condition|(
name|app
operator|==
literal|null
operator|||
name|app
operator|.
name|getClasses
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|&&
name|app
operator|.
name|getSingletons
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// The best effort at detecting a CXFNonSpringJaxrsServlet
comment|// Custom servlets using non-standard mechanisms to create Application will not be detected
if|if
condition|(
name|isCxfServletAvailable
argument_list|(
name|ctx
argument_list|)
condition|)
block|{
return|return;
block|}
specifier|final
name|Map
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|,
name|Collection
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
name|providersAndResources
init|=
name|groupByAnnotations
argument_list|(
name|classes
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|providersAndResources
operator|.
name|get
argument_list|(
name|Path
operator|.
name|class
argument_list|)
operator|.
name|isEmpty
argument_list|()
operator|||
operator|!
name|providersAndResources
operator|.
name|get
argument_list|(
name|Provider
operator|.
name|class
argument_list|)
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|app
operator|==
literal|null
condition|)
block|{
comment|// Servlet name is a JAX-RS Application class name
name|servletName
operator|=
name|JAXRS_APPLICATION_PARAM
expr_stmt|;
comment|// Servlet mapping is obtained from a servlet registration
comment|// with a JAX-RS Application class name
name|servletMapping
operator|=
name|getServletMapping
argument_list|(
name|ctx
argument_list|,
name|JAXRS_APPLICATION_PARAM
argument_list|)
expr_stmt|;
block|}
name|app
operator|=
operator|new
name|Application
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|getClasses
parameter_list|()
block|{
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|set
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
name|set
operator|.
name|addAll
argument_list|(
name|providersAndResources
operator|.
name|get
argument_list|(
name|Path
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|set
operator|.
name|addAll
argument_list|(
name|providersAndResources
operator|.
name|get
argument_list|(
name|Provider
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|set
return|;
block|}
block|}
expr_stmt|;
block|}
block|}
if|if
condition|(
name|app
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|CXFNonSpringJaxrsServlet
name|cxfServlet
init|=
operator|new
name|CXFNonSpringJaxrsServlet
argument_list|(
name|app
argument_list|)
decl_stmt|;
specifier|final
name|Dynamic
name|servlet
init|=
name|ctx
operator|.
name|addServlet
argument_list|(
name|servletName
argument_list|,
name|cxfServlet
argument_list|)
decl_stmt|;
name|servlet
operator|.
name|addMapping
argument_list|(
name|servletMapping
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|isCxfServletAvailable
parameter_list|(
name|ServletContext
name|ctx
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|?
extends|extends
name|ServletRegistration
argument_list|>
name|entry
range|:
name|ctx
operator|.
name|getServletRegistrations
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|getInitParameter
argument_list|(
name|CXF_JAXRS_CLASSES_PARAM
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|String
name|getServletMapping
parameter_list|(
specifier|final
name|ServletContext
name|ctx
parameter_list|,
specifier|final
name|String
name|name
parameter_list|)
throws|throws
name|ServletException
block|{
name|ServletRegistration
name|sr
init|=
name|ctx
operator|.
name|getServletRegistration
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|sr
operator|!=
literal|null
condition|)
block|{
return|return
name|sr
operator|.
name|getMappings
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
else|else
block|{
specifier|final
name|String
name|error
init|=
literal|"Servlet with a name "
operator|+
name|name
operator|+
literal|" is not available"
decl_stmt|;
throw|throw
operator|new
name|ServletException
argument_list|(
name|error
argument_list|)
throw|;
block|}
block|}
specifier|private
name|boolean
name|isApplicationServletAvailable
parameter_list|(
specifier|final
name|ServletContext
name|ctx
parameter_list|,
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|appClass
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|?
extends|extends
name|ServletRegistration
argument_list|>
name|entry
range|:
name|ctx
operator|.
name|getServletRegistrations
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|appParam
init|=
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|getInitParameter
argument_list|(
name|JAXRS_APPLICATION_PARAM
argument_list|)
decl_stmt|;
if|if
condition|(
name|appParam
operator|==
literal|null
condition|)
block|{
name|appParam
operator|=
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|getInitParameter
argument_list|(
name|CXF_JAXRS_APPLICATION_PARAM
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|appParam
operator|!=
literal|null
operator|&&
name|appParam
operator|.
name|equals
argument_list|(
name|appClass
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|Map
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|,
name|Collection
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
name|groupByAnnotations
parameter_list|(
specifier|final
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
parameter_list|)
block|{
specifier|final
name|Map
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|,
name|Collection
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
name|grouped
init|=
operator|new
name|HashMap
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|,
name|Collection
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|grouped
operator|.
name|put
argument_list|(
name|Provider
operator|.
name|class
argument_list|,
operator|new
name|ArrayList
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
name|grouped
operator|.
name|put
argument_list|(
name|Path
operator|.
name|class
argument_list|,
operator|new
name|ArrayList
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
range|:
name|classes
control|)
block|{
if|if
condition|(
operator|!
name|classShouldBeIgnored
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
for|for
control|(
specifier|final
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|annotation
range|:
name|grouped
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|clazz
operator|.
name|isAnnotationPresent
argument_list|(
name|annotation
argument_list|)
condition|)
block|{
name|grouped
operator|.
name|get
argument_list|(
name|annotation
argument_list|)
operator|.
name|add
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|grouped
return|;
block|}
specifier|private
specifier|static
name|boolean
name|classShouldBeIgnored
parameter_list|(
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
return|return
name|clazz
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
name|IGNORE_PACKAGE
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|Class
argument_list|<
name|?
argument_list|>
name|findCandidate
parameter_list|(
specifier|final
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
parameter_list|)
block|{
for|for
control|(
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
range|:
name|classes
control|)
block|{
if|if
condition|(
name|Application
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
operator|&&
operator|!
name|classShouldBeIgnored
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Found JAX-RS application to initialize: "
operator|+
name|clazz
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|clazz
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

