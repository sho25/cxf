begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|jaxrs
operator|.
name|sse
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|catalina
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|catalina
operator|.
name|WebResourceRoot
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|catalina
operator|.
name|Wrapper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|catalina
operator|.
name|core
operator|.
name|StandardContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|catalina
operator|.
name|servlets
operator|.
name|DefaultServlet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|catalina
operator|.
name|startup
operator|.
name|Tomcat
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|catalina
operator|.
name|webresources
operator|.
name|DirResourceSet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|catalina
operator|.
name|webresources
operator|.
name|StandardRoot
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
name|cxf
operator|.
name|transport
operator|.
name|sse
operator|.
name|SseHttpTransportFactory
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
name|ContextLoaderListener
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|StatsServer
block|{
specifier|private
name|StatsServer
parameter_list|()
block|{     }
specifier|public
specifier|static
name|void
name|main
parameter_list|(
specifier|final
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Register and map the dispatcher servlet
specifier|final
name|File
name|base
init|=
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.io.tmpdir"
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|Tomcat
name|server
init|=
operator|new
name|Tomcat
argument_list|()
decl_stmt|;
name|server
operator|.
name|setPort
argument_list|(
literal|8686
argument_list|)
expr_stmt|;
name|server
operator|.
name|setBaseDir
argument_list|(
name|base
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|StandardContext
name|context
init|=
operator|(
name|StandardContext
operator|)
name|server
operator|.
name|addWebapp
argument_list|(
literal|"/"
argument_list|,
name|base
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
decl_stmt|;
name|context
operator|.
name|setConfigFile
argument_list|(
name|StatsServer
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"/META-INF/context.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|addApplicationListener
argument_list|(
name|ContextLoaderListener
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|setAddWebinfClassesResources
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|context
operator|.
name|setResources
argument_list|(
name|resourcesFrom
argument_list|(
name|context
argument_list|,
literal|"target/classes"
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|Wrapper
name|cxfServlet
init|=
name|Tomcat
operator|.
name|addServlet
argument_list|(
name|context
argument_list|,
literal|"cxfServlet"
argument_list|,
operator|new
name|CXFServlet
argument_list|()
argument_list|)
decl_stmt|;
name|cxfServlet
operator|.
name|addInitParameter
argument_list|(
name|CXFServlet
operator|.
name|TRANSPORT_ID
argument_list|,
name|SseHttpTransportFactory
operator|.
name|TRANSPORT_ID
argument_list|)
expr_stmt|;
name|cxfServlet
operator|.
name|setAsyncSupported
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|context
operator|.
name|addServletMapping
argument_list|(
literal|"/rest/*"
argument_list|,
literal|"cxfServlet"
argument_list|)
expr_stmt|;
specifier|final
name|Context
name|staticContext
init|=
name|server
operator|.
name|addWebapp
argument_list|(
literal|"/static"
argument_list|,
name|base
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
decl_stmt|;
name|Tomcat
operator|.
name|addServlet
argument_list|(
name|staticContext
argument_list|,
literal|"cxfStaticServlet"
argument_list|,
operator|new
name|DefaultServlet
argument_list|()
argument_list|)
expr_stmt|;
name|staticContext
operator|.
name|addServletMapping
argument_list|(
literal|"/static/*"
argument_list|,
literal|"cxfStaticServlet"
argument_list|)
expr_stmt|;
name|staticContext
operator|.
name|setResources
argument_list|(
name|resourcesFrom
argument_list|(
name|staticContext
argument_list|,
literal|"target/classes/web-ui"
argument_list|)
argument_list|)
expr_stmt|;
name|staticContext
operator|.
name|setParentClassLoader
argument_list|(
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
argument_list|)
expr_stmt|;
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
name|server
operator|.
name|getServer
argument_list|()
operator|.
name|await
argument_list|()
expr_stmt|;
block|}
specifier|private
specifier|static
name|WebResourceRoot
name|resourcesFrom
parameter_list|(
specifier|final
name|Context
name|context
parameter_list|,
specifier|final
name|String
name|path
parameter_list|)
block|{
specifier|final
name|File
name|additionResources
init|=
operator|new
name|File
argument_list|(
name|path
argument_list|)
decl_stmt|;
specifier|final
name|WebResourceRoot
name|resources
init|=
operator|new
name|StandardRoot
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|resources
operator|.
name|addPreResources
argument_list|(
operator|new
name|DirResourceSet
argument_list|(
name|resources
argument_list|,
literal|"/"
argument_list|,
name|additionResources
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
literal|"/"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|resources
return|;
block|}
block|}
end_class

end_unit

