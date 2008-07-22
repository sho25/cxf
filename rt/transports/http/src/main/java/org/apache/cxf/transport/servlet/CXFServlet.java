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
name|transport
operator|.
name|servlet
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
name|InputStream
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|bus
operator|.
name|spring
operator|.
name|BusApplicationContext
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
name|bus
operator|.
name|spring
operator|.
name|SpringBusFactory
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
name|classloader
operator|.
name|ClassLoaderUtils
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
name|URIResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|xml
operator|.
name|XmlBeanDefinitionReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ConfigurableApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|event
operator|.
name|ContextRefreshedEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|GenericApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|InputStreamResource
import|;
end_import

begin_comment
comment|/**  * A Servlet which supports loading of JAX-WS endpoints from an  * XML file and handling requests for endpoints created via other means  * such as Spring beans, or the Java API. All requests are passed on  * to the {@link ServletController}.  *  */
end_comment

begin_class
specifier|public
class|class
name|CXFServlet
extends|extends
name|AbstractCXFServlet
implements|implements
name|ApplicationListener
block|{
specifier|private
name|GenericApplicationContext
name|childCtx
decl_stmt|;
specifier|private
name|boolean
name|inRefresh
decl_stmt|;
specifier|public
specifier|static
name|Logger
name|getLogger
parameter_list|()
block|{
return|return
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|CXFServlet
operator|.
name|class
argument_list|)
return|;
block|}
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
name|String
name|springCls
init|=
literal|"org.springframework.context.ApplicationContext"
decl_stmt|;
try|try
block|{
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|springCls
argument_list|,
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|loadSpringBus
argument_list|(
name|servletConfig
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
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
literal|"FAILED_TO_LOAD_SPRING_BUS"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|e
block|}
argument_list|)
expr_stmt|;
operator|new
name|ServletException
argument_list|(
literal|"Can't load bus with Spring context class"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|loadSpringBus
parameter_list|(
name|ServletConfig
name|servletConfig
parameter_list|)
throws|throws
name|ServletException
block|{
comment|// try to pull an existing ApplicationContext out of the
comment|// ServletContext
name|ServletContext
name|svCtx
init|=
name|getServletContext
argument_list|()
decl_stmt|;
comment|// Spring 1.x
name|ApplicationContext
name|ctx
init|=
operator|(
name|ApplicationContext
operator|)
name|svCtx
operator|.
name|getAttribute
argument_list|(
literal|"interface org.springframework.web.context.WebApplicationContext.ROOT"
argument_list|)
decl_stmt|;
comment|// Spring 2.0
if|if
condition|(
name|ctx
operator|==
literal|null
condition|)
block|{
name|Object
name|ctxObject
init|=
name|svCtx
operator|.
name|getAttribute
argument_list|(
literal|"org.springframework.web.context.WebApplicationContext.ROOT"
argument_list|)
decl_stmt|;
if|if
condition|(
name|ctxObject
operator|instanceof
name|ApplicationContext
condition|)
block|{
name|ctx
operator|=
operator|(
name|ApplicationContext
operator|)
name|ctxObject
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ctxObject
operator|!=
literal|null
condition|)
block|{
comment|// it should be the runtime exception
name|Exception
name|ex
init|=
operator|(
name|Exception
operator|)
name|ctxObject
decl_stmt|;
throw|throw
operator|new
name|ServletException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
name|updateContext
argument_list|(
name|servletConfig
argument_list|,
name|ctx
argument_list|)
expr_stmt|;
if|if
condition|(
name|ctx
operator|instanceof
name|ConfigurableApplicationContext
condition|)
block|{
operator|(
operator|(
name|ConfigurableApplicationContext
operator|)
name|ctx
operator|)
operator|.
name|addApplicationListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|updateContext
parameter_list|(
name|ServletConfig
name|servletConfig
parameter_list|,
name|ApplicationContext
name|ctx
parameter_list|)
block|{
comment|// This constructor works whether there is a context or not
comment|// If the ctx is null, we just start up the default bus
if|if
condition|(
name|ctx
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"LOAD_BUS_WITHOUT_APPLICATION_CONTEXT"
argument_list|)
expr_stmt|;
name|bus
operator|=
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|()
expr_stmt|;
name|ctx
operator|=
name|bus
operator|.
name|getExtension
argument_list|(
name|BusApplicationContext
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"LOAD_BUS_WITH_APPLICATION_CONTEXT"
argument_list|)
expr_stmt|;
name|bus
operator|=
operator|new
name|SpringBusFactory
argument_list|(
name|ctx
argument_list|)
operator|.
name|createBus
argument_list|()
expr_stmt|;
block|}
name|ResourceManager
name|resourceManager
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ResourceManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|resourceManager
operator|.
name|addResourceResolver
argument_list|(
operator|new
name|ServletContextResourceResolver
argument_list|(
name|servletConfig
operator|.
name|getServletContext
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|replaceDestinationFactory
argument_list|()
expr_stmt|;
comment|// Set up the ServletController
name|controller
operator|=
name|createServletController
argument_list|(
name|servletConfig
argument_list|)
expr_stmt|;
comment|// build endpoints from the web.xml or a config file
name|loadAdditionalConfig
argument_list|(
name|ctx
argument_list|,
name|servletConfig
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|loadAdditionalConfig
parameter_list|(
name|ApplicationContext
name|ctx
parameter_list|,
name|ServletConfig
name|servletConfig
parameter_list|)
block|{
name|String
name|location
init|=
name|servletConfig
operator|.
name|getInitParameter
argument_list|(
literal|"config-location"
argument_list|)
decl_stmt|;
if|if
condition|(
name|location
operator|==
literal|null
condition|)
block|{
name|location
operator|=
literal|"/WEB-INF/cxf-servlet.xml"
expr_stmt|;
block|}
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
try|try
block|{
name|is
operator|=
name|servletConfig
operator|.
name|getServletContext
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|location
argument_list|)
expr_stmt|;
if|if
condition|(
name|is
operator|==
literal|null
operator|||
name|is
operator|.
name|available
argument_list|()
operator|==
operator|-
literal|1
condition|)
block|{
name|URIResolver
name|resolver
init|=
operator|new
name|URIResolver
argument_list|(
name|location
argument_list|)
decl_stmt|;
if|if
condition|(
name|resolver
operator|.
name|isResolved
argument_list|()
condition|)
block|{
name|is
operator|=
name|resolver
operator|.
name|getInputStream
argument_list|()
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|//throw new ServletException(e);
block|}
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"BUILD_ENDPOINTS_FROM_CONFIG_LOCATION"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|location
block|}
argument_list|)
expr_stmt|;
name|childCtx
operator|=
operator|new
name|GenericApplicationContext
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
name|XmlBeanDefinitionReader
name|reader
init|=
operator|new
name|XmlBeanDefinitionReader
argument_list|(
name|childCtx
argument_list|)
decl_stmt|;
name|reader
operator|.
name|setValidationMode
argument_list|(
name|XmlBeanDefinitionReader
operator|.
name|VALIDATION_XSD
argument_list|)
expr_stmt|;
name|reader
operator|.
name|loadBeanDefinitions
argument_list|(
operator|new
name|InputStreamResource
argument_list|(
name|is
argument_list|,
name|location
argument_list|)
argument_list|)
expr_stmt|;
name|childCtx
operator|.
name|refresh
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|destroy
parameter_list|()
block|{
if|if
condition|(
name|childCtx
operator|!=
literal|null
condition|)
block|{
name|childCtx
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|onApplicationEvent
parameter_list|(
name|ApplicationEvent
name|event
parameter_list|)
block|{
if|if
condition|(
operator|!
name|inRefresh
operator|&&
name|event
operator|instanceof
name|ContextRefreshedEvent
condition|)
block|{
comment|//need to re-do the bus/controller stuff
try|try
block|{
name|inRefresh
operator|=
literal|true
expr_stmt|;
name|updateContext
argument_list|(
name|this
operator|.
name|getServletConfig
argument_list|()
argument_list|,
operator|(
operator|(
name|ContextRefreshedEvent
operator|)
name|event
operator|)
operator|.
name|getApplicationContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|inRefresh
operator|=
literal|false
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

