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
name|javax
operator|.
name|servlet
operator|.
name|FilterChain
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|FilterRegistration
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
name|javax
operator|.
name|servlet
operator|.
name|ServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletResponse
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
name|HttpServlet
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
name|HttpServletRequestWrapper
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
name|BusException
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
name|classloader
operator|.
name|ClassLoaderUtils
operator|.
name|ClassLoaderHolder
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
name|transport
operator|.
name|DestinationFactory
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
name|DestinationFactoryManager
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
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|http
operator|.
name|DestinationRegistry
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
name|HTTPTransportFactory
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
name|servicelist
operator|.
name|ServiceListGeneratorServlet
import|;
end_import

begin_class
specifier|public
class|class
name|CXFNonSpringServlet
extends|extends
name|AbstractHTTPServlet
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|2437897227486327166L
decl_stmt|;
specifier|private
name|DestinationRegistry
name|destinationRegistry
decl_stmt|;
specifier|private
name|boolean
name|globalRegistry
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|ServletController
name|controller
decl_stmt|;
specifier|private
name|ClassLoader
name|loader
decl_stmt|;
specifier|private
name|boolean
name|loadBus
init|=
literal|true
decl_stmt|;
specifier|public
name|CXFNonSpringServlet
parameter_list|()
block|{     }
specifier|public
name|CXFNonSpringServlet
parameter_list|(
name|DestinationRegistry
name|destinationRegistry
parameter_list|)
block|{
name|this
argument_list|(
name|destinationRegistry
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|CXFNonSpringServlet
parameter_list|(
name|DestinationRegistry
name|destinationRegistry
parameter_list|,
name|boolean
name|loadBus
parameter_list|)
block|{
name|this
operator|.
name|destinationRegistry
operator|=
name|destinationRegistry
expr_stmt|;
name|this
operator|.
name|globalRegistry
operator|=
name|destinationRegistry
operator|!=
literal|null
expr_stmt|;
name|this
operator|.
name|loadBus
operator|=
name|loadBus
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|init
parameter_list|(
name|ServletConfig
name|sc
parameter_list|)
throws|throws
name|ServletException
block|{
name|super
operator|.
name|init
argument_list|(
name|sc
argument_list|)
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|bus
operator|==
literal|null
operator|&&
name|loadBus
condition|)
block|{
name|loadBus
argument_list|(
name|sc
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|bus
operator|!=
literal|null
condition|)
block|{
name|loader
operator|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ClassLoader
operator|.
name|class
argument_list|)
expr_stmt|;
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
name|sc
operator|.
name|getServletContext
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|destinationRegistry
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|destinationRegistry
operator|=
name|getDestinationRegistryFromBus
argument_list|(
name|this
operator|.
name|bus
argument_list|)
expr_stmt|;
block|}
block|}
name|this
operator|.
name|controller
operator|=
name|createServletController
argument_list|(
name|sc
argument_list|)
expr_stmt|;
name|finalizeServletInit
argument_list|(
name|sc
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|DestinationRegistry
name|getDestinationRegistryFromBus
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|DestinationFactoryManager
name|dfm
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|DestinationFactoryManager
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|DestinationFactory
name|df
init|=
name|dfm
operator|.
name|getDestinationFactory
argument_list|(
literal|"http://cxf.apache.org/transports/http/configuration"
argument_list|)
decl_stmt|;
if|if
condition|(
name|df
operator|instanceof
name|HTTPTransportFactory
condition|)
block|{
name|HTTPTransportFactory
name|transportFactory
init|=
operator|(
name|HTTPTransportFactory
operator|)
name|df
decl_stmt|;
return|return
name|transportFactory
operator|.
name|getRegistry
argument_list|()
return|;
block|}
block|}
catch|catch
parameter_list|(
name|BusException
name|e
parameter_list|)
block|{
comment|// why are we throwing a busexception if the DF isn't found?
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|void
name|loadBus
parameter_list|(
name|ServletConfig
name|sc
parameter_list|)
block|{
name|this
operator|.
name|bus
operator|=
name|BusFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createBus
argument_list|()
expr_stmt|;
block|}
specifier|private
name|ServletController
name|createServletController
parameter_list|(
name|ServletConfig
name|servletConfig
parameter_list|)
block|{
name|HttpServlet
name|serviceListGeneratorServlet
init|=
operator|new
name|ServiceListGeneratorServlet
argument_list|(
name|destinationRegistry
argument_list|,
name|bus
argument_list|)
decl_stmt|;
name|ServletController
name|newController
init|=
operator|new
name|ServletController
argument_list|(
name|destinationRegistry
argument_list|,
name|servletConfig
argument_list|,
name|serviceListGeneratorServlet
argument_list|)
decl_stmt|;
return|return
name|newController
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
name|setBus
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
specifier|public
name|void
name|doFilter
parameter_list|(
name|ServletRequest
name|request
parameter_list|,
name|ServletResponse
name|response
parameter_list|,
name|FilterChain
name|chain
parameter_list|)
throws|throws
name|IOException
throws|,
name|ServletException
block|{
name|ClassLoaderHolder
name|origLoader
init|=
literal|null
decl_stmt|;
name|Bus
name|origBus
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|request
operator|instanceof
name|HttpServletRequest
operator|&&
name|response
operator|instanceof
name|HttpServletResponse
condition|)
block|{
try|try
block|{
if|if
condition|(
name|loader
operator|!=
literal|null
condition|)
block|{
name|origLoader
operator|=
name|ClassLoaderUtils
operator|.
name|setThreadContextClassloader
argument_list|(
name|loader
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
name|origBus
operator|=
name|BusFactory
operator|.
name|getAndSetThreadDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
name|HttpServletRequest
name|httpRequest
init|=
operator|(
name|HttpServletRequest
operator|)
name|request
decl_stmt|;
if|if
condition|(
name|controller
operator|.
name|filter
argument_list|(
operator|new
name|HttpServletRequestFilter
argument_list|(
name|httpRequest
argument_list|,
name|super
operator|.
name|getServletName
argument_list|()
argument_list|)
argument_list|,
operator|(
name|HttpServletResponse
operator|)
name|response
argument_list|)
condition|)
block|{
return|return;
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|origBus
operator|!=
name|bus
condition|)
block|{
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|origBus
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|origLoader
operator|!=
literal|null
condition|)
block|{
name|origLoader
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
block|}
block|}
name|chain
operator|.
name|doFilter
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|invoke
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|ServletException
block|{
name|ClassLoaderHolder
name|origLoader
init|=
literal|null
decl_stmt|;
name|Bus
name|origBus
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|loader
operator|!=
literal|null
condition|)
block|{
name|origLoader
operator|=
name|ClassLoaderUtils
operator|.
name|setThreadContextClassloader
argument_list|(
name|loader
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
name|origBus
operator|=
name|BusFactory
operator|.
name|getAndSetThreadDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
name|controller
operator|.
name|invoke
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|origBus
operator|!=
name|bus
condition|)
block|{
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|origLoader
operator|!=
literal|null
condition|)
block|{
name|origLoader
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|destroy
parameter_list|()
block|{
if|if
condition|(
operator|!
name|globalRegistry
condition|)
block|{
for|for
control|(
name|String
name|path
range|:
name|destinationRegistry
operator|.
name|getDestinationsPaths
argument_list|()
control|)
block|{
comment|// clean up the destination in case the destination itself can
comment|// no longer access the registry later
name|AbstractHTTPDestination
name|dest
init|=
name|destinationRegistry
operator|.
name|getDestinationForPath
argument_list|(
name|path
argument_list|)
decl_stmt|;
synchronized|synchronized
init|(
name|dest
init|)
block|{
name|destinationRegistry
operator|.
name|removeDestination
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|dest
operator|.
name|releaseRegistry
argument_list|()
expr_stmt|;
block|}
block|}
name|destinationRegistry
operator|=
literal|null
expr_stmt|;
block|}
name|destroyBus
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|destroyBus
parameter_list|()
block|{
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|bus
operator|=
literal|null
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
class|class
name|HttpServletRequestFilter
extends|extends
name|HttpServletRequestWrapper
block|{
specifier|private
name|String
name|filterName
decl_stmt|;
specifier|public
name|HttpServletRequestFilter
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|String
name|filterName
parameter_list|)
block|{
name|super
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|this
operator|.
name|filterName
operator|=
name|filterName
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getServletPath
parameter_list|()
block|{
name|FilterRegistration
name|fr
init|=
name|super
operator|.
name|getServletContext
argument_list|()
operator|.
name|getFilterRegistration
argument_list|(
name|filterName
argument_list|)
decl_stmt|;
if|if
condition|(
name|fr
operator|!=
literal|null
operator|&&
operator|!
name|fr
operator|.
name|getUrlPatternMappings
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
name|mapping
init|=
name|fr
operator|.
name|getUrlPatternMappings
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|mapping
operator|.
name|endsWith
argument_list|(
literal|"/*"
argument_list|)
condition|)
block|{
return|return
name|mapping
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|mapping
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|)
return|;
block|}
block|}
return|return
literal|""
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getPathInfo
parameter_list|()
block|{
name|String
name|pathInfo
init|=
name|super
operator|.
name|getPathInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|pathInfo
operator|==
literal|null
condition|)
block|{
name|pathInfo
operator|=
name|getRequestURI
argument_list|()
expr_stmt|;
block|}
name|String
name|prefix
init|=
name|super
operator|.
name|getContextPath
argument_list|()
operator|+
name|this
operator|.
name|getServletPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|pathInfo
operator|.
name|startsWith
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
name|pathInfo
operator|=
name|pathInfo
operator|.
name|substring
argument_list|(
name|prefix
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|pathInfo
return|;
block|}
block|}
block|}
end_class

end_unit

