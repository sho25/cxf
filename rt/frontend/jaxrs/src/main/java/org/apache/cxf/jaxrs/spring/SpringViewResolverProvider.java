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
name|spring
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

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
name|Type
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
name|Locale
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
name|javax
operator|.
name|servlet
operator|.
name|RequestDispatcher
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
name|Produces
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
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
name|ext
operator|.
name|MessageBodyWriter
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
name|StringUtils
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
name|provider
operator|.
name|AbstractConfigurableProvider
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
name|ExceptionUtils
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|phase
operator|.
name|PhaseInterceptorChain
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
name|http
operator|.
name|AbstractHTTPDestination
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|web
operator|.
name|servlet
operator|.
name|LocaleResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|web
operator|.
name|servlet
operator|.
name|View
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|web
operator|.
name|servlet
operator|.
name|ViewResolver
import|;
end_import

begin_comment
comment|/**  * CXF view provider that delegates view rendering to Spring MVC Views.  *  * Sample usage in a spring application:  *<pre>  @Bean  public SpringViewResolverProvider springViewProvider(ViewResolver viewResolver) {      SpringViewResolverProvider viewProvider = new SpringViewResolverProvider(viewResolver,             new AcceptHeaderLocaleResolver());      viewProvider.setUseClassNames(true);      viewProvider.setBeanName("model");      viewProvider.setResourcePaths(Collections.singletonMap("/remove", "registeredClients"));      return viewProvider;  }  *</pre>  */
end_comment

begin_class
annotation|@
name|Produces
argument_list|(
literal|"text/html"
argument_list|)
annotation|@
name|Provider
specifier|public
class|class
name|SpringViewResolverProvider
extends|extends
name|AbstractConfigurableProvider
implements|implements
name|MessageBodyWriter
argument_list|<
name|Object
argument_list|>
block|{
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
name|SpringViewResolverProvider
operator|.
name|class
argument_list|)
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
name|SpringViewResolverProvider
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|MESSAGE_RESOURCE_PATH_PROPERTY
init|=
literal|"redirect.resource.path"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_RESOURCE_EXTENSION
init|=
literal|""
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_LOCATION_PREFIX
init|=
literal|""
decl_stmt|;
specifier|private
specifier|final
name|ViewResolver
name|viewResolver
decl_stmt|;
specifier|private
name|String
name|resourcePath
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|resourcePaths
init|=
name|Collections
operator|.
name|emptyMap
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|classResources
init|=
name|Collections
operator|.
name|emptyMap
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|?
extends|extends
name|Enum
argument_list|<
name|?
argument_list|>
argument_list|,
name|String
argument_list|>
name|enumResources
init|=
name|Collections
operator|.
name|emptyMap
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|useClassNames
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|beanNames
init|=
name|Collections
operator|.
name|emptyMap
argument_list|()
decl_stmt|;
specifier|private
name|String
name|beanName
decl_stmt|;
specifier|private
name|boolean
name|logRedirects
decl_stmt|;
specifier|private
name|boolean
name|strictPathCheck
decl_stmt|;
specifier|private
name|String
name|locationPrefix
decl_stmt|;
specifier|private
name|String
name|resourceExtension
decl_stmt|;
specifier|private
name|MessageContext
name|mc
decl_stmt|;
specifier|private
name|LocaleResolver
name|localeResolver
decl_stmt|;
specifier|private
name|String
name|errorView
init|=
literal|"/error"
decl_stmt|;
specifier|public
name|SpringViewResolverProvider
parameter_list|(
name|ViewResolver
name|viewResolver
parameter_list|,
name|LocaleResolver
name|localeResolver
parameter_list|)
block|{
if|if
condition|(
name|viewResolver
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Argument viewResolver is required"
argument_list|)
throw|;
block|}
if|if
condition|(
name|localeResolver
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Argument localeResolver is required"
argument_list|)
throw|;
block|}
name|this
operator|.
name|viewResolver
operator|=
name|viewResolver
expr_stmt|;
name|this
operator|.
name|localeResolver
operator|=
name|localeResolver
expr_stmt|;
block|}
annotation|@
name|Context
specifier|public
name|void
name|setMessageContext
parameter_list|(
name|MessageContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|mc
operator|=
name|context
expr_stmt|;
block|}
specifier|public
name|void
name|setStrictPathCheck
parameter_list|(
name|boolean
name|use
parameter_list|)
block|{
name|strictPathCheck
operator|=
name|use
expr_stmt|;
block|}
specifier|public
name|void
name|setUseClassNames
parameter_list|(
name|boolean
name|use
parameter_list|)
block|{
name|useClassNames
operator|=
name|use
expr_stmt|;
block|}
specifier|public
name|long
name|getSize
parameter_list|(
name|Object
name|t
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|Annotation
index|[]
name|annotations
parameter_list|,
name|MediaType
name|mt
parameter_list|)
block|{
return|return
operator|-
literal|1
return|;
block|}
specifier|private
name|String
name|getViewName
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|String
name|viewName
init|=
name|doGetClassResourceName
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|viewName
operator|==
literal|null
condition|)
block|{
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|in
range|:
name|type
operator|.
name|getInterfaces
argument_list|()
control|)
block|{
name|viewName
operator|=
name|doGetClassResourceName
argument_list|(
name|in
argument_list|)
expr_stmt|;
if|if
condition|(
name|viewName
operator|!=
literal|null
condition|)
block|{
break|break;
block|}
block|}
block|}
return|return
name|viewName
return|;
block|}
specifier|private
name|Locale
name|getLocale
parameter_list|()
block|{
return|return
name|localeResolver
operator|.
name|resolveLocale
argument_list|(
name|mc
operator|.
name|getHttpServletRequest
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|String
name|doGetClassResourceName
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|String
name|simpleName
init|=
name|StringUtils
operator|.
name|uncapitalize
argument_list|(
name|type
operator|.
name|getSimpleName
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|thePrefix
init|=
name|locationPrefix
operator|==
literal|null
condition|?
name|DEFAULT_LOCATION_PREFIX
else|:
name|locationPrefix
decl_stmt|;
name|String
name|theExtension
init|=
name|resourceExtension
operator|==
literal|null
condition|?
name|DEFAULT_RESOURCE_EXTENSION
else|:
name|resourceExtension
decl_stmt|;
name|String
name|viewName
init|=
name|thePrefix
operator|+
name|simpleName
operator|+
name|theExtension
decl_stmt|;
name|View
name|view
init|=
name|resolveView
argument_list|(
name|viewName
argument_list|)
decl_stmt|;
return|return
name|view
operator|!=
literal|null
condition|?
name|viewName
else|:
literal|null
return|;
block|}
specifier|public
name|boolean
name|isWriteable
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|Annotation
index|[]
name|annotations
parameter_list|,
name|MediaType
name|mt
parameter_list|)
block|{
if|if
condition|(
name|useClassNames
operator|&&
name|getViewName
argument_list|(
name|type
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|resourcePath
operator|!=
literal|null
operator|||
name|classResourceSupported
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
name|resourcePaths
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
name|path
init|=
name|getRequestPath
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|requestPath
range|:
name|resourcePaths
operator|.
name|keySet
argument_list|()
control|)
block|{
name|boolean
name|result
init|=
name|strictPathCheck
condition|?
name|path
operator|.
name|endsWith
argument_list|(
name|requestPath
argument_list|)
else|:
name|path
operator|.
name|contains
argument_list|(
name|requestPath
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
name|mc
operator|!=
literal|null
operator|&&
name|mc
operator|.
name|get
argument_list|(
name|MESSAGE_RESOURCE_PATH_PROPERTY
argument_list|)
operator|!=
literal|null
return|;
block|}
specifier|private
name|boolean
name|classResourceSupported
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|String
name|typeName
init|=
name|type
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|type
operator|.
name|isEnum
argument_list|()
condition|)
block|{
for|for
control|(
name|Object
name|o
range|:
name|enumResources
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|o
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|typeName
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
for|for
control|(
name|String
name|name
range|:
name|classResources
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
name|typeName
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
return|return
name|classResources
operator|.
name|containsKey
argument_list|(
name|typeName
argument_list|)
return|;
block|}
specifier|public
name|void
name|writeTo
parameter_list|(
name|Object
name|o
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|Annotation
index|[]
name|annotations
parameter_list|,
name|MediaType
name|type
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|,
name|OutputStream
name|os
parameter_list|)
throws|throws
name|IOException
block|{
name|View
name|view
init|=
name|getView
argument_list|(
name|clazz
argument_list|,
name|o
argument_list|)
decl_stmt|;
name|String
name|attributeName
init|=
name|getBeanName
argument_list|(
name|o
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|model
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
name|attributeName
argument_list|,
name|o
argument_list|)
decl_stmt|;
try|try
block|{
name|mc
operator|.
name|put
argument_list|(
name|AbstractHTTPDestination
operator|.
name|REQUEST_REDIRECTED
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|logRedirection
argument_list|(
name|view
argument_list|,
name|attributeName
argument_list|,
name|o
argument_list|)
expr_stmt|;
name|view
operator|.
name|render
argument_list|(
name|model
argument_list|,
name|mc
operator|.
name|getHttpServletRequest
argument_list|()
argument_list|,
name|mc
operator|.
name|getHttpServletResponse
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
name|handleViewRenderingException
argument_list|(
name|view
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * By default we'll try to forward to Spring error handler.      *      * If no such handler has been set, or if there is an error during error handling,      * we throw an error and let CXF handle the internal error.      *      * @param view view that produced the rendering error      * @param exception rendering error      */
specifier|private
name|void
name|handleViewRenderingException
parameter_list|(
name|View
name|view
parameter_list|,
name|Throwable
name|exception
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
name|String
operator|.
name|format
argument_list|(
literal|"Error forwarding to '%s': %s"
argument_list|,
name|view
argument_list|,
name|exception
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|,
name|exception
argument_list|)
expr_stmt|;
if|if
condition|(
name|errorView
operator|!=
literal|null
condition|)
block|{
name|mc
operator|.
name|getHttpServletRequest
argument_list|()
operator|.
name|setAttribute
argument_list|(
name|RequestDispatcher
operator|.
name|ERROR_EXCEPTION
argument_list|,
name|exception
argument_list|)
expr_stmt|;
name|mc
operator|.
name|getHttpServletRequest
argument_list|()
operator|.
name|setAttribute
argument_list|(
name|RequestDispatcher
operator|.
name|ERROR_STATUS_CODE
argument_list|,
literal|500
argument_list|)
expr_stmt|;
name|mc
operator|.
name|getHttpServletRequest
argument_list|()
operator|.
name|setAttribute
argument_list|(
name|RequestDispatcher
operator|.
name|ERROR_MESSAGE
argument_list|,
name|exception
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|mc
operator|.
name|getServletContext
argument_list|()
operator|.
name|getRequestDispatcher
argument_list|(
name|errorView
argument_list|)
operator|.
name|forward
argument_list|(
name|mc
operator|.
name|getHttpServletRequest
argument_list|()
argument_list|,
name|mc
operator|.
name|getHttpServletResponse
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
name|String
operator|.
name|format
argument_list|(
literal|"Error forwarding to error page '%s': %s"
argument_list|,
name|errorView
argument_list|,
name|e
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|handleInternalViewRenderingException
argument_list|(
name|exception
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|handleInternalViewRenderingException
argument_list|(
name|exception
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|handleInternalViewRenderingException
parameter_list|(
name|Throwable
name|exception
parameter_list|)
block|{
name|mc
operator|.
name|put
argument_list|(
name|AbstractHTTPDestination
operator|.
name|REQUEST_REDIRECTED
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
throw|throw
name|ExceptionUtils
operator|.
name|toInternalServerErrorException
argument_list|(
name|exception
argument_list|,
literal|null
argument_list|)
throw|;
block|}
specifier|private
name|void
name|logRedirection
parameter_list|(
name|View
name|view
parameter_list|,
name|String
name|attributeName
parameter_list|,
name|Object
name|o
parameter_list|)
block|{
name|Level
name|level
init|=
name|logRedirects
condition|?
name|Level
operator|.
name|INFO
else|:
name|Level
operator|.
name|FINE
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|level
argument_list|)
condition|)
block|{
name|String
name|message
init|=
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
literal|"RESPONSE_REDIRECTED_TO"
argument_list|,
name|BUNDLE
argument_list|,
name|o
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|attributeName
argument_list|,
name|view
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|level
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
block|}
name|View
name|getView
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|Object
name|o
parameter_list|)
block|{
name|String
name|currentResourcePath
init|=
name|getPathFromMessageContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|currentResourcePath
operator|!=
literal|null
condition|)
block|{
return|return
name|resolveView
argument_list|(
name|currentResourcePath
argument_list|)
return|;
block|}
if|if
condition|(
operator|!
name|resourcePaths
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
name|path
init|=
name|getRequestPath
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|resourcePaths
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|path
operator|.
name|endsWith
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|resolveView
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|enumResources
operator|.
name|isEmpty
argument_list|()
operator|||
operator|!
name|classResources
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
name|name
init|=
name|cls
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|cls
operator|.
name|isEnum
argument_list|()
condition|)
block|{
name|String
name|enumResource
init|=
name|enumResources
operator|.
name|get
argument_list|(
name|o
argument_list|)
decl_stmt|;
if|if
condition|(
name|enumResource
operator|!=
literal|null
condition|)
block|{
return|return
name|resolveView
argument_list|(
name|enumResource
argument_list|)
return|;
block|}
name|name
operator|+=
literal|"."
operator|+
name|o
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
name|String
name|clsResourcePath
init|=
name|classResources
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|clsResourcePath
operator|!=
literal|null
condition|)
block|{
return|return
name|resolveView
argument_list|(
name|clsResourcePath
argument_list|)
return|;
block|}
block|}
if|if
condition|(
name|useClassNames
condition|)
block|{
return|return
name|resolveView
argument_list|(
name|getViewName
argument_list|(
name|cls
argument_list|)
argument_list|)
return|;
block|}
return|return
name|resolveView
argument_list|(
name|resourcePath
argument_list|)
return|;
block|}
specifier|private
name|View
name|resolveView
parameter_list|(
name|String
name|viewName
parameter_list|)
block|{
try|try
block|{
return|return
name|viewResolver
operator|.
name|resolveViewName
argument_list|(
name|viewName
argument_list|,
name|getLocale
argument_list|()
argument_list|)
return|;
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
name|ExceptionUtils
operator|.
name|getStackTrace
argument_list|(
name|ex
argument_list|)
argument_list|)
expr_stmt|;
throw|throw
name|ExceptionUtils
operator|.
name|toInternalServerErrorException
argument_list|(
name|ex
argument_list|,
literal|null
argument_list|)
throw|;
block|}
block|}
specifier|private
name|String
name|getPathFromMessageContext
parameter_list|()
block|{
if|if
condition|(
name|mc
operator|!=
literal|null
condition|)
block|{
name|Object
name|resourcePathProp
init|=
name|mc
operator|.
name|get
argument_list|(
name|MESSAGE_RESOURCE_PATH_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|resourcePathProp
operator|!=
literal|null
condition|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|locationPrefix
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|locationPrefix
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|resourcePathProp
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|resourceExtension
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|resourceExtension
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|String
name|getRequestPath
parameter_list|()
block|{
name|Message
name|inMessage
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
decl_stmt|;
return|return
operator|(
name|String
operator|)
name|inMessage
operator|.
name|get
argument_list|(
name|Message
operator|.
name|REQUEST_URI
argument_list|)
return|;
block|}
specifier|public
name|void
name|setResourcePath
parameter_list|(
name|String
name|resourcePath
parameter_list|)
block|{
name|this
operator|.
name|resourcePath
operator|=
name|resourcePath
expr_stmt|;
block|}
specifier|public
name|void
name|setBeanNames
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|beanNames
parameter_list|)
block|{
name|this
operator|.
name|beanNames
operator|=
name|beanNames
expr_stmt|;
block|}
specifier|public
name|void
name|setBeanName
parameter_list|(
name|String
name|beanName
parameter_list|)
block|{
name|this
operator|.
name|beanName
operator|=
name|beanName
expr_stmt|;
block|}
specifier|public
name|void
name|setLogRedirects
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|logRedirects
operator|=
name|Boolean
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|String
name|getBeanName
parameter_list|(
name|Object
name|bean
parameter_list|)
block|{
if|if
condition|(
name|beanName
operator|!=
literal|null
condition|)
block|{
return|return
name|beanName
return|;
block|}
name|String
name|name
init|=
name|beanNames
operator|.
name|get
argument_list|(
name|bean
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
return|return
name|name
return|;
block|}
name|Class
argument_list|<
name|?
argument_list|>
name|resourceClass
init|=
name|bean
operator|.
name|getClass
argument_list|()
decl_stmt|;
if|if
condition|(
name|useClassNames
operator|&&
name|doGetClassResourceName
argument_list|(
name|resourceClass
argument_list|)
operator|==
literal|null
condition|)
block|{
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
range|:
name|bean
operator|.
name|getClass
argument_list|()
operator|.
name|getInterfaces
argument_list|()
control|)
block|{
if|if
condition|(
name|doGetClassResourceName
argument_list|(
name|cls
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|resourceClass
operator|=
name|cls
expr_stmt|;
break|break;
block|}
block|}
block|}
return|return
name|resourceClass
operator|.
name|getSimpleName
argument_list|()
operator|.
name|toLowerCase
argument_list|()
return|;
block|}
specifier|public
name|void
name|setResourcePaths
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|resourcePaths
parameter_list|)
block|{
name|this
operator|.
name|resourcePaths
operator|=
name|resourcePaths
expr_stmt|;
block|}
specifier|public
name|void
name|setClassResources
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|resources
parameter_list|)
block|{
name|this
operator|.
name|classResources
operator|=
name|resources
expr_stmt|;
block|}
specifier|public
name|void
name|setEnumResources
parameter_list|(
name|Map
argument_list|<
name|?
extends|extends
name|Enum
argument_list|<
name|?
argument_list|>
argument_list|,
name|String
argument_list|>
name|enumResources
parameter_list|)
block|{
name|this
operator|.
name|enumResources
operator|=
name|enumResources
expr_stmt|;
block|}
specifier|public
name|void
name|setLocationPrefix
parameter_list|(
name|String
name|locationPrefix
parameter_list|)
block|{
name|this
operator|.
name|locationPrefix
operator|=
name|locationPrefix
expr_stmt|;
block|}
specifier|public
name|void
name|setResourceExtension
parameter_list|(
name|String
name|resourceExtension
parameter_list|)
block|{
name|this
operator|.
name|resourceExtension
operator|=
name|resourceExtension
expr_stmt|;
block|}
specifier|public
name|void
name|setErrorView
parameter_list|(
name|String
name|errorView
parameter_list|)
block|{
name|this
operator|.
name|errorView
operator|=
name|errorView
expr_stmt|;
block|}
block|}
end_class

end_unit

