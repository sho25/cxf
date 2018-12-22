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
name|servlet
operator|.
name|jetty
package|;
end_package

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
name|cxf
operator|.
name|jaxrs
operator|.
name|model
operator|.
name|AbstractResourceInfo
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
name|AbstractSciTest
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
name|util
operator|.
name|resource
operator|.
name|Resource
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
name|webapp
operator|.
name|WebAppContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
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
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|JettyEmptyPathApplicationTest
extends|extends
name|AbstractSciTest
block|{
annotation|@
name|Ignore
specifier|public
specifier|static
class|class
name|EmbeddedJettyServer
extends|extends
name|AbstractJettyServer
block|{
specifier|public
specifier|static
specifier|final
name|int
name|PORT
init|=
name|allocatePortAsInt
argument_list|(
name|EmbeddedJettyServer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|EmbeddedJettyServer
parameter_list|()
block|{
name|super
argument_list|(
literal|"/"
argument_list|,
operator|new
name|Resource
index|[]
block|{
comment|// Limit the classpath scanning to org.apache.demo.resources package
name|Resource
operator|.
name|newClassPathResource
argument_list|(
literal|"/org/apache/demo/resources"
argument_list|)
block|,
comment|// Include JAX-RS application from org.apache.applications.empty package
name|Resource
operator|.
name|newClassPathResource
argument_list|(
literal|"/org/apache/demo/applications/emptypath"
argument_list|)
block|,
comment|// Include Jackson @Providers into classpath scanning
name|Resource
operator|.
name|newResource
argument_list|(
name|JacksonJsonProvider
operator|.
name|class
operator|.
name|getProtectionDomain
argument_list|()
operator|.
name|getCodeSource
argument_list|()
operator|.
name|getLocation
argument_list|()
argument_list|)
block|}
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|configureContext
parameter_list|(
specifier|final
name|WebAppContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|context
operator|.
name|setDescriptor
argument_list|(
name|Resource
operator|.
name|newClassPathResource
argument_list|(
literal|"/WEB-INF/web-subclass.xml"
argument_list|)
operator|.
name|getFile
argument_list|()
operator|.
name|toURI
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|startServers
parameter_list|()
throws|throws
name|Exception
block|{
name|AbstractResourceInfo
operator|.
name|clearAllMaps
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|EmbeddedJettyServer
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|createStaticBus
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|int
name|getPort
parameter_list|()
block|{
return|return
name|EmbeddedJettyServer
operator|.
name|PORT
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|getContextPath
parameter_list|()
block|{
return|return
literal|"/subapi"
return|;
block|}
block|}
end_class

end_unit

