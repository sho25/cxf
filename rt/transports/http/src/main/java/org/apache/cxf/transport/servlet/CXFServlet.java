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
name|Collection
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
name|util
operator|.
name|ReflectionUtil
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
name|helpers
operator|.
name|CastUtils
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
name|AbstractApplicationContext
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
name|Resource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|util
operator|.
name|ReflectionUtils
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
name|context
operator|.
name|support
operator|.
name|WebApplicationContextUtils
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
name|context
operator|.
name|support
operator|.
name|XmlWebApplicationContext
import|;
end_import

begin_class
specifier|public
class|class
name|CXFServlet
extends|extends
name|CXFNonSpringServlet
implements|implements
name|ApplicationListener
argument_list|<
name|ContextRefreshedEvent
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|5922443981969455305L
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|BUS_PARAMETER
init|=
literal|"bus"
decl_stmt|;
specifier|private
name|boolean
name|busCreated
decl_stmt|;
specifier|private
name|XmlWebApplicationContext
name|createdContext
decl_stmt|;
specifier|public
name|CXFServlet
parameter_list|()
block|{     }
annotation|@
name|Override
specifier|protected
name|void
name|loadBus
parameter_list|(
name|ServletConfig
name|servletConfig
parameter_list|)
block|{
name|ApplicationContext
name|wac
init|=
name|WebApplicationContextUtils
operator|.
name|getWebApplicationContext
argument_list|(
name|servletConfig
operator|.
name|getServletContext
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|wac
operator|instanceof
name|AbstractApplicationContext
condition|)
block|{
name|addListener
argument_list|(
operator|(
name|AbstractApplicationContext
operator|)
name|wac
argument_list|)
expr_stmt|;
block|}
name|String
name|configLocation
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
name|configLocation
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|InputStream
name|is
init|=
name|servletConfig
operator|.
name|getServletContext
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/WEB-INF/cxf-servlet.xml"
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
operator|&&
name|is
operator|.
name|available
argument_list|()
operator|>
literal|0
condition|)
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
name|configLocation
operator|=
literal|"/WEB-INF/cxf-servlet.xml"
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//ignore
block|}
block|}
if|if
condition|(
name|configLocation
operator|!=
literal|null
condition|)
block|{
name|wac
operator|=
name|createSpringContext
argument_list|(
name|wac
argument_list|,
name|servletConfig
argument_list|,
name|configLocation
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|wac
operator|!=
literal|null
condition|)
block|{
name|String
name|busParam
init|=
name|servletConfig
operator|.
name|getInitParameter
argument_list|(
name|BUS_PARAMETER
argument_list|)
decl_stmt|;
name|String
name|busName
init|=
name|busParam
operator|==
literal|null
condition|?
literal|"cxf"
else|:
name|busParam
operator|.
name|trim
argument_list|()
decl_stmt|;
name|setBus
argument_list|(
name|wac
operator|.
name|getBean
argument_list|(
name|busName
argument_list|,
name|Bus
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|busCreated
operator|=
literal|true
expr_stmt|;
name|setBus
argument_list|(
name|BusFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createBus
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|addListener
parameter_list|(
name|AbstractApplicationContext
name|wac
parameter_list|)
block|{
comment|/**          * The change in the way application listeners are maintained during the context refresh           * since Spring Framework 5.1.5 (https://github.com/spring-projects/spring-framework/issues/22325). The          * CXF adds listener **after** the context has been refreshed, not much control we have over it, but          * it does matter now: the listeners registered after the context refresh disappear when           * context is refreshed. The ugly hack here, to stay in the loop, is to add CXF servlet          * to "earlyApplicationListeners" set, only than it will be kept between refreshes.          */
try|try
block|{
specifier|final
name|Field
name|f
init|=
name|ReflectionUtils
operator|.
name|findField
argument_list|(
name|wac
operator|.
name|getClass
argument_list|()
argument_list|,
literal|"earlyApplicationListeners"
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|!=
literal|null
condition|)
block|{
name|Collection
argument_list|<
name|Object
argument_list|>
name|c
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|ReflectionUtil
operator|.
name|setAccessible
argument_list|(
name|f
argument_list|)
operator|.
name|get
argument_list|(
name|wac
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|c
operator|.
name|add
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|SecurityException
decl||
name|IllegalAccessException
name|e
parameter_list|)
block|{
comment|//ignore.
block|}
try|try
block|{
comment|//spring 2 vs spring 3 return type is different
name|Method
name|m
init|=
name|wac
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getApplicationListeners"
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|Object
argument_list|>
name|c
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|ReflectionUtil
operator|.
name|setAccessible
argument_list|(
name|m
argument_list|)
operator|.
name|invoke
argument_list|(
name|wac
argument_list|)
argument_list|)
decl_stmt|;
name|c
operator|.
name|add
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//ignore.
block|}
block|}
comment|/**      * Try to create a spring application context from the config location.      * Will first try to resolve the location using the servlet context.      * If that does not work then the location is given as is to spring      *      * @param ctx      * @param sc      * @param configLocation      * @return      */
specifier|private
name|ApplicationContext
name|createSpringContext
parameter_list|(
name|ApplicationContext
name|ctx
parameter_list|,
name|ServletConfig
name|servletConfig
parameter_list|,
name|String
name|location
parameter_list|)
block|{
name|XmlWebApplicationContext
name|ctx2
init|=
operator|new
name|XmlWebApplicationContext
argument_list|()
decl_stmt|;
name|createdContext
operator|=
name|ctx2
expr_stmt|;
name|ctx2
operator|.
name|setServletConfig
argument_list|(
name|servletConfig
argument_list|)
expr_stmt|;
name|Resource
name|r
init|=
name|ctx2
operator|.
name|getResource
argument_list|(
name|location
argument_list|)
decl_stmt|;
try|try
block|{
name|InputStream
name|in
init|=
name|r
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|//ignore
name|r
operator|=
name|ctx2
operator|.
name|getResource
argument_list|(
literal|"classpath:"
operator|+
name|location
argument_list|)
expr_stmt|;
try|try
block|{
name|r
operator|.
name|getInputStream
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e2
parameter_list|)
block|{
comment|//ignore
name|r
operator|=
literal|null
expr_stmt|;
block|}
block|}
try|try
block|{
if|if
condition|(
name|r
operator|!=
literal|null
condition|)
block|{
name|location
operator|=
name|r
operator|.
name|getURL
argument_list|()
operator|.
name|toExternalForm
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|//ignore
block|}
if|if
condition|(
name|ctx
operator|!=
literal|null
condition|)
block|{
name|ctx2
operator|.
name|setParent
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
name|String
index|[]
name|names
init|=
name|ctx
operator|.
name|getBeanNamesForType
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|names
operator|==
literal|null
operator|||
name|names
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|ctx2
operator|.
name|setConfigLocations
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"classpath:/META-INF/cxf/cxf.xml"
block|,
name|location
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ctx2
operator|.
name|setConfigLocations
argument_list|(
operator|new
name|String
index|[]
block|{
name|location
block|}
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|ctx2
operator|.
name|setConfigLocations
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"classpath:/META-INF/cxf/cxf.xml"
block|,
name|location
block|}
argument_list|)
expr_stmt|;
name|createdContext
operator|=
name|ctx2
expr_stmt|;
block|}
name|ctx2
operator|.
name|refresh
argument_list|()
expr_stmt|;
return|return
name|ctx2
return|;
block|}
specifier|public
name|void
name|destroyBus
parameter_list|()
block|{
if|if
condition|(
name|busCreated
condition|)
block|{
comment|//if we created the Bus, we need to destroy it.  Otherwise, spring will handleit.
name|getBus
argument_list|()
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|createdContext
operator|!=
literal|null
condition|)
block|{
name|createdContext
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|onApplicationEvent
parameter_list|(
name|ContextRefreshedEvent
name|event
parameter_list|)
block|{
name|destroy
argument_list|()
expr_stmt|;
name|setBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
try|try
block|{
name|init
argument_list|(
name|getServletConfig
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ServletException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unable to reinitialize the CXFServlet"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

