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
name|systest
operator|.
name|servlet
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Endpoint
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
name|jaxws
operator|.
name|EndpointImpl
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
name|testutil
operator|.
name|common
operator|.
name|AbstractBusTestServerBase
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
name|CXFServlet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|GreeterImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|Server
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|handler
operator|.
name|ContextHandlerCollection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|servlet
operator|.
name|ServletContextHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|servlet
operator|.
name|ServletHolder
import|;
end_import

begin_class
specifier|public
class|class
name|NoSpringServletServer
extends|extends
name|AbstractBusTestServerBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|allocatePort
argument_list|(
name|NoSpringServletServer
operator|.
name|class
argument_list|)
decl_stmt|;
name|Server
name|httpServer
decl_stmt|;
name|Bus
name|bus
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|run
parameter_list|()
block|{
comment|// setup the system properties
name|String
name|busFactory
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|BusFactory
operator|.
name|BUS_FACTORY_PROPERTY_NAME
argument_list|)
decl_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
name|BusFactory
operator|.
name|BUS_FACTORY_PROPERTY_NAME
argument_list|,
literal|"org.apache.cxf.bus.CXFBusFactory"
argument_list|)
expr_stmt|;
try|try
block|{
name|httpServer
operator|=
operator|new
name|Server
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|PORT
argument_list|)
argument_list|)
expr_stmt|;
name|ContextHandlerCollection
name|contexts
init|=
operator|new
name|ContextHandlerCollection
argument_list|()
decl_stmt|;
name|httpServer
operator|.
name|setHandler
argument_list|(
name|contexts
argument_list|)
expr_stmt|;
name|ServletContextHandler
name|root
init|=
operator|new
name|ServletContextHandler
argument_list|(
name|contexts
argument_list|,
literal|"/"
argument_list|,
name|ServletContextHandler
operator|.
name|SESSIONS
argument_list|)
decl_stmt|;
name|bus
operator|=
name|BusFactory
operator|.
name|getDefaultBus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|CXFServlet
name|cxf
init|=
operator|new
name|CXFServlet
argument_list|()
decl_stmt|;
name|cxf
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|ServletHolder
name|servlet
init|=
operator|new
name|ServletHolder
argument_list|(
name|cxf
argument_list|)
decl_stmt|;
name|servlet
operator|.
name|setName
argument_list|(
literal|"soap"
argument_list|)
expr_stmt|;
name|servlet
operator|.
name|setForcedPath
argument_list|(
literal|"soap"
argument_list|)
expr_stmt|;
name|root
operator|.
name|addServlet
argument_list|(
name|servlet
argument_list|,
literal|"/soap/*"
argument_list|)
expr_stmt|;
name|httpServer
operator|.
name|start
argument_list|()
expr_stmt|;
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|GreeterImpl
name|impl
init|=
operator|new
name|GreeterImpl
argument_list|()
decl_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
literal|"/Greeter"
argument_list|,
name|impl
argument_list|)
expr_stmt|;
name|HelloImpl
name|helloImpl
init|=
operator|new
name|HelloImpl
argument_list|()
decl_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
literal|"/Hello"
argument_list|,
name|helloImpl
argument_list|)
expr_stmt|;
operator|(
operator|(
name|EndpointImpl
operator|)
name|Endpoint
operator|.
name|create
argument_list|(
name|helloImpl
argument_list|)
operator|)
operator|.
name|publish
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
comment|// clean up the system properties
if|if
condition|(
name|busFactory
operator|!=
literal|null
condition|)
block|{
name|System
operator|.
name|setProperty
argument_list|(
name|BusFactory
operator|.
name|BUS_FACTORY_PROPERTY_NAME
argument_list|,
name|busFactory
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|clearProperty
argument_list|(
name|BusFactory
operator|.
name|BUS_FACTORY_PROPERTY_NAME
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|httpServer
operator|!=
literal|null
condition|)
block|{
name|httpServer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
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
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
try|try
block|{
name|NoSpringServletServer
name|s
init|=
operator|new
name|NoSpringServletServer
argument_list|()
decl_stmt|;
name|s
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

