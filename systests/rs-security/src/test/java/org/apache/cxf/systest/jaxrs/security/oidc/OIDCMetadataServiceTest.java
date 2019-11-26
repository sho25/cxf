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
name|security
operator|.
name|oidc
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|json
operator|.
name|basic
operator|.
name|JsonMapObjectReaderWriter
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
name|security
operator|.
name|SecurityTestUtil
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
name|security
operator|.
name|oauth2
operator|.
name|common
operator|.
name|OAuth2TestUtils
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
name|AbstractBusClientServerTestBase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
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

begin_comment
comment|/**  * Some unit tests for the OIDC/OAuth OidcConfigurationService  */
end_comment

begin_class
specifier|public
class|class
name|OIDCMetadataServiceTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|private
specifier|static
specifier|final
name|SpringBusTestServer
name|JCACHE_SERVER
init|=
operator|new
name|SpringBusTestServer
argument_list|(
literal|"metadata-server-jcache"
argument_list|)
decl_stmt|;
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
name|assertTrue
argument_list|(
literal|"Server failed to launch"
argument_list|,
name|launchServer
argument_list|(
name|JCACHE_SERVER
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|cleanup
parameter_list|()
throws|throws
name|Exception
block|{
name|SecurityTestUtil
operator|.
name|cleanup
argument_list|()
expr_stmt|;
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testOIDCMetadataService
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|OIDCMetadataServiceTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
argument_list|)
decl_stmt|;
name|String
name|address
init|=
literal|"https://localhost:"
operator|+
name|JCACHE_SERVER
operator|.
name|getPort
argument_list|()
operator|+
literal|"/services/.well-known/openid-configuration"
decl_stmt|;
name|WebClient
name|client
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|address
argument_list|,
name|OAuth2TestUtils
operator|.
name|setupProviders
argument_list|()
argument_list|,
literal|"alice"
argument_list|,
literal|"security"
argument_list|,
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|Response
name|response
init|=
name|client
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|response
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|responseStr
init|=
name|response
operator|.
name|readEntity
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|JsonMapObjectReaderWriter
name|reader
init|=
operator|new
name|JsonMapObjectReaderWriter
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|json
init|=
name|reader
operator|.
name|fromJson
argument_list|(
name|responseStr
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|containsKey
argument_list|(
literal|"issuer"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|containsKey
argument_list|(
literal|"response_types_supported"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
