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
name|discovery
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Form
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
operator|.
name|Status
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
name|client
operator|.
name|JAXRSClientFactory
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
name|client
operator|.
name|WebClient
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
name|systest
operator|.
name|jaxrs
operator|.
name|AbstractSpringServer
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
name|validation
operator|.
name|AbstractJAXRSValidationTest
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
name|validation
operator|.
name|BookWithValidation
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
name|TestUtil
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
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|JAXRSServerSpringDiscoveryTest
extends|extends
name|AbstractJAXRSValidationTest
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
literal|"jaxrs-http"
argument_list|)
decl_stmt|;
annotation|@
name|Ignore
specifier|public
specifier|static
class|class
name|Server
extends|extends
name|AbstractSpringServer
block|{
specifier|public
name|Server
parameter_list|()
block|{
name|super
argument_list|(
literal|"/jaxrs_spring_discovery"
argument_list|,
name|Integer
operator|.
name|parseInt
argument_list|(
name|PORT
argument_list|)
argument_list|)
expr_stmt|;
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
name|Server
name|s
init|=
operator|new
name|Server
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
comment|//keep out of process due to stack traces testing failures
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|Server
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResponseValidationFailsIfNameIsNull
parameter_list|()
block|{
specifier|final
name|Response
name|r
init|=
name|createWebClient
argument_list|(
literal|"/bookstore/books"
argument_list|)
operator|.
name|post
argument_list|(
operator|new
name|Form
argument_list|()
operator|.
name|param
argument_list|(
literal|"id"
argument_list|,
literal|"1"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|INTERNAL_SERVER_ERROR
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testParameterValidationFailsIfIdIsNull
parameter_list|()
block|{
specifier|final
name|Response
name|r
init|=
name|createWebClient
argument_list|(
literal|"/bookstore/books"
argument_list|)
operator|.
name|post
argument_list|(
operator|new
name|Form
argument_list|()
operator|.
name|param
argument_list|(
literal|"name"
argument_list|,
literal|"aa"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|BAD_REQUEST
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testThatClientDiscoversServiceProperly
parameter_list|()
throws|throws
name|Exception
block|{
name|BookStore
name|bs
init|=
name|JAXRSClientFactory
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
argument_list|,
name|BookStore
operator|.
name|class
argument_list|,
literal|"org/apache/cxf/systest/jaxrs/discovery/jaxrs-http-client.xml"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
argument_list|,
name|WebClient
operator|.
name|client
argument_list|(
name|bs
argument_list|)
operator|.
name|getBaseURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|BookWithValidation
name|book
init|=
name|bs
operator|.
name|getBook
argument_list|(
literal|"123"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|book
operator|.
name|getId
argument_list|()
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|getPort
parameter_list|()
block|{
return|return
name|PORT
return|;
block|}
block|}
end_class

end_unit

