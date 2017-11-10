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
name|springmvc
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
name|jaxrs
operator|.
name|provider
operator|.
name|AbstractResponseViewProvider
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
specifier|public
class|class
name|SpringViewResolverProvider
extends|extends
name|AbstractResponseViewProvider
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
specifier|final
name|ViewResolver
name|viewResolver
decl_stmt|;
specifier|private
name|LocaleResolver
name|localeResolver
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
name|getMessageContext
argument_list|()
operator|.
name|getHttpServletRequest
argument_list|()
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
name|getMessageContext
argument_list|()
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
name|getMessageContext
argument_list|()
operator|.
name|getHttpServletRequest
argument_list|()
argument_list|,
name|getMessageContext
argument_list|()
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
operator|.
name|toString
argument_list|()
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
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
name|isLogRedirects
argument_list|()
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
name|path
init|=
name|getResourcePath
argument_list|(
name|cls
argument_list|,
name|o
argument_list|)
decl_stmt|;
if|if
condition|(
name|path
operator|!=
literal|null
condition|)
block|{
return|return
name|resolveView
argument_list|(
name|path
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
annotation|@
name|Override
specifier|protected
name|boolean
name|resourceAvailable
parameter_list|(
name|String
name|resourceName
parameter_list|)
block|{
return|return
name|resolveView
argument_list|(
name|resourceName
argument_list|)
operator|!=
literal|null
return|;
block|}
block|}
end_class

end_unit
