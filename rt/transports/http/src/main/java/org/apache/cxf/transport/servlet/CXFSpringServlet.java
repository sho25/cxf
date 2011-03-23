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
name|transport
operator|.
name|servlet
operator|.
name|servicelist
operator|.
name|ServiceListGeneratorServlet
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
name|WebApplicationContext
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

begin_class
specifier|public
class|class
name|CXFSpringServlet
extends|extends
name|HttpServlet
block|{
specifier|private
name|ServletTransportFactory
name|servletTransportFactory
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|ServletController
name|controller
decl_stmt|;
specifier|public
name|CXFSpringServlet
parameter_list|()
block|{     }
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
name|WebApplicationContext
name|wac
init|=
name|WebApplicationContextUtils
operator|.
name|getRequiredWebApplicationContext
argument_list|(
name|sc
operator|.
name|getServletContext
argument_list|()
argument_list|)
decl_stmt|;
name|this
operator|.
name|bus
operator|=
name|wac
operator|.
name|getBean
argument_list|(
literal|"cxf"
argument_list|,
name|Bus
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|servletTransportFactory
operator|=
name|wac
operator|.
name|getBean
argument_list|(
name|ServletTransportFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|controller
operator|=
name|createServletController
argument_list|(
name|sc
argument_list|)
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
name|servletTransportFactory
operator|.
name|getRegistry
argument_list|()
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
name|servletTransportFactory
operator|.
name|getRegistry
argument_list|()
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
name|void
name|handleRequest
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|ServletException
throws|,
name|IOException
block|{
try|try
block|{
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
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
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
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
block|}
end_class

end_unit

