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
name|cdi
operator|.
name|tomcat
package|;
end_package

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
name|systest
operator|.
name|jaxrs
operator|.
name|cdi
operator|.
name|AbstractBookStoreCdiTest
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
name|systests
operator|.
name|cdi
operator|.
name|base
operator|.
name|tomcat
operator|.
name|AbstractTomcatServer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|weld
operator|.
name|environment
operator|.
name|Container
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|weld
operator|.
name|environment
operator|.
name|tomcat
operator|.
name|TomcatContainer
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

begin_class
specifier|public
class|class
name|TomcatWarTest
extends|extends
name|AbstractBookStoreCdiTest
block|{
specifier|public
specifier|static
class|class
name|EmbeddedTomcatServer
extends|extends
name|AbstractTomcatServer
block|{
specifier|public
specifier|static
specifier|final
name|int
name|PORT
init|=
name|allocatePortAsInt
argument_list|(
name|EmbeddedTomcatServer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|EmbeddedTomcatServer
parameter_list|()
block|{
name|super
argument_list|(
literal|"/jaxrs_cdi"
argument_list|,
literal|"/"
argument_list|,
name|PORT
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
name|System
operator|.
name|setProperty
argument_list|(
name|Container
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|TomcatContainer
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|EmbeddedTomcatServer
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
name|EmbeddedTomcatServer
operator|.
name|PORT
return|;
block|}
block|}
end_class

end_unit

