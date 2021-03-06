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
name|jaxrs
operator|.
name|sse
operator|.
name|tomcat
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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|jaxrs
operator|.
name|json
operator|.
name|JacksonJsonProvider
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
name|cxf
operator|.
name|jaxrs
operator|.
name|servlet
operator|.
name|CXFNonSpringJaxrsServlet
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
name|systest
operator|.
name|jaxrs
operator|.
name|sse
operator|.
name|BookStore
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
name|systest
operator|.
name|jaxrs
operator|.
name|sse
operator|.
name|BookStoreResponseFilter
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractTomcatServer
extends|extends
name|AbstractBusTestServerBase
block|{
specifier|private
name|Tomcat
name|server
decl_stmt|;
specifier|private
specifier|final
name|String
name|resourcePath
decl_stmt|;
specifier|private
specifier|final
name|String
name|contextPath
decl_stmt|;
specifier|private
specifier|final
name|int
name|port
decl_stmt|;
specifier|protected
name|AbstractTomcatServer
parameter_list|(
specifier|final
name|String
name|contextPath
parameter_list|,
name|int
name|portNumber
parameter_list|)
block|{
name|this
argument_list|(
literal|null
argument_list|,
name|contextPath
argument_list|,
name|portNumber
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|AbstractTomcatServer
parameter_list|(
specifier|final
name|String
name|resourcePath
parameter_list|,
specifier|final
name|String
name|contextPath
parameter_list|,
name|int
name|portNumber
parameter_list|)
block|{
name|this
operator|.
name|resourcePath
operator|=
name|resourcePath
expr_stmt|;
name|this
operator|.
name|contextPath
operator|=
name|contextPath
expr_stmt|;
name|this
operator|.
name|port
operator|=
name|portNumber
expr_stmt|;
block|}
specifier|protected
name|void
name|run
parameter_list|()
block|{
name|server
operator|=
operator|new
name|Tomcat
argument_list|()
expr_stmt|;
name|server
operator|.
name|setPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
try|try
block|{
specifier|final
name|File
name|base
init|=
name|createTemporaryDirectory
argument_list|()
decl_stmt|;
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
if|if
condition|(
name|resourcePath
operator|==
literal|null
condition|)
block|{
specifier|final
name|Context
name|context
init|=
name|server
operator|.
name|addContext
argument_list|(
literal|"/"
argument_list|,
name|base
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
decl_stmt|;
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
name|CXFNonSpringJaxrsServlet
argument_list|()
argument_list|)
decl_stmt|;
name|cxfServlet
operator|.
name|addInitParameter
argument_list|(
literal|"jaxrs.serviceClasses"
argument_list|,
name|BookStore
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|cxfServlet
operator|.
name|addInitParameter
argument_list|(
literal|"jaxrs.providers"
argument_list|,
name|String
operator|.
name|join
argument_list|(
literal|","
argument_list|,
name|JacksonJsonProvider
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|BookStoreResponseFilter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
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
name|addServletMappingDecoded
argument_list|(
literal|"/rest/*"
argument_list|,
literal|"cxfServlet"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|server
operator|.
name|getHost
argument_list|()
operator|.
name|setAppBase
argument_list|(
name|base
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|server
operator|.
name|getHost
argument_list|()
operator|.
name|setAutoDeploy
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|server
operator|.
name|getHost
argument_list|()
operator|.
name|setDeployOnStartup
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|server
operator|.
name|addWebapp
argument_list|(
name|contextPath
argument_list|,
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
name|resourcePath
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|getPath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|Exception
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|fail
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|configureServer
parameter_list|(
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|Server
name|theserver
parameter_list|)
throws|throws
name|Exception
block|{      }
specifier|private
specifier|static
name|File
name|createTemporaryDirectory
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|File
name|base
init|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"tmp-"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|base
operator|.
name|delete
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cannot (re)create base folder: "
operator|+
name|base
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|base
operator|.
name|mkdir
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cannot create base folder: "
operator|+
name|base
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
name|base
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
return|return
name|base
return|;
block|}
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
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
name|server
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|server
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

