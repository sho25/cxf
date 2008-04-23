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
name|aegis
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|mortbay
operator|.
name|jetty
operator|.
name|Connector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|Handler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|handler
operator|.
name|DefaultHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|handler
operator|.
name|HandlerCollection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|nio
operator|.
name|SelectChannelConnector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|webapp
operator|.
name|WebAppContext
import|;
end_import

begin_class
specifier|public
class|class
name|AegisServer
extends|extends
name|AbstractBusTestServerBase
block|{
specifier|private
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|Server
name|server
decl_stmt|;
specifier|protected
name|void
name|run
parameter_list|()
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Starting Server"
argument_list|)
expr_stmt|;
name|server
operator|=
operator|new
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|Server
argument_list|()
expr_stmt|;
name|SelectChannelConnector
name|connector
init|=
operator|new
name|SelectChannelConnector
argument_list|()
decl_stmt|;
name|connector
operator|.
name|setPort
argument_list|(
literal|9002
argument_list|)
expr_stmt|;
name|server
operator|.
name|setConnectors
argument_list|(
operator|new
name|Connector
index|[]
block|{
name|connector
block|}
argument_list|)
expr_stmt|;
name|WebAppContext
name|webappcontext
init|=
operator|new
name|WebAppContext
argument_list|()
decl_stmt|;
name|String
name|contextPath
init|=
literal|null
decl_stmt|;
try|try
block|{
name|contextPath
operator|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/webapp"
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|getPath
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e1
parameter_list|)
block|{
name|e1
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
name|webappcontext
operator|.
name|setContextPath
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
name|webappcontext
operator|.
name|setWar
argument_list|(
name|contextPath
argument_list|)
expr_stmt|;
name|HandlerCollection
name|handlers
init|=
operator|new
name|HandlerCollection
argument_list|()
decl_stmt|;
name|handlers
operator|.
name|setHandlers
argument_list|(
operator|new
name|Handler
index|[]
block|{
name|webappcontext
block|,
operator|new
name|DefaultHandler
argument_list|()
block|}
argument_list|)
expr_stmt|;
name|server
operator|.
name|setHandler
argument_list|(
name|handlers
argument_list|)
expr_stmt|;
try|try
block|{
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|stopInProcess
parameter_list|()
throws|throws
name|Exception
block|{
name|boolean
name|ret
init|=
name|super
operator|.
name|stopInProcess
argument_list|()
decl_stmt|;
if|if
condition|(
name|server
operator|!=
literal|null
condition|)
block|{
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
name|args
index|[]
parameter_list|)
block|{
try|try
block|{
name|AegisServer
name|s
init|=
operator|new
name|AegisServer
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
finally|finally
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"done!"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

