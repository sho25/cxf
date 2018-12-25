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
name|description
operator|.
name|openapi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|MediaType
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
name|cxf
operator|.
name|ext
operator|.
name|logging
operator|.
name|LoggingFeature
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
name|feature
operator|.
name|Feature
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
name|JAXRSServerFactoryBean
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
name|jaxrs
operator|.
name|model
operator|.
name|UserApplication
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
name|openapi
operator|.
name|OpenApiCustomizer
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
name|openapi
operator|.
name|OpenApiFeature
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
name|openapi
operator|.
name|parse
operator|.
name|OpenApiParseUtils
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
name|description
operator|.
name|group2
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
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|containsString
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|equalTo
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|MatcherAssert
operator|.
name|assertThat
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
name|assertNotNull
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
name|OpenApiContextBasedConfigTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|PORT
init|=
name|allocatePort
argument_list|(
name|OpenApiContextBasedConfigTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
class|class
name|OpenApiContextBased
extends|extends
name|AbstractBusTestServerBase
block|{
annotation|@
name|Override
specifier|protected
name|void
name|run
parameter_list|()
block|{
name|createServerFactory
argument_list|(
literal|"/api"
argument_list|,
literal|"This is first API (api)"
argument_list|,
name|BookStoreOpenApi
operator|.
name|class
argument_list|)
expr_stmt|;
name|createServerFactory
argument_list|(
literal|"/api2"
argument_list|,
literal|"This is second API (api2)"
argument_list|,
name|BookStore
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createServerFactory
parameter_list|(
specifier|final
name|String
name|context
parameter_list|,
specifier|final
name|String
name|description
parameter_list|,
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|resource
parameter_list|)
block|{
specifier|final
name|JAXRSServerFactoryBean
name|sf
init|=
operator|new
name|JAXRSServerFactoryBean
argument_list|()
decl_stmt|;
name|sf
operator|.
name|setResourceClasses
argument_list|(
name|resource
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setProvider
argument_list|(
operator|new
name|JacksonJsonProvider
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|OpenApiFeature
name|feature
init|=
name|createOpenApiFeature
argument_list|(
name|description
argument_list|,
name|resource
argument_list|)
decl_stmt|;
name|sf
operator|.
name|setFeatures
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|feature
argument_list|)
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
name|context
argument_list|)
expr_stmt|;
name|sf
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|OpenApiFeature
name|createOpenApiFeature
parameter_list|(
specifier|final
name|String
name|description
parameter_list|,
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|resource
parameter_list|)
block|{
specifier|final
name|OpenApiCustomizer
name|customizer
init|=
operator|new
name|OpenApiCustomizer
argument_list|()
decl_stmt|;
name|customizer
operator|.
name|setDynamicBasePath
argument_list|(
literal|true
argument_list|)
expr_stmt|;
specifier|final
name|OpenApiFeature
name|feature
init|=
operator|new
name|OpenApiFeature
argument_list|()
decl_stmt|;
name|feature
operator|.
name|setDescription
argument_list|(
name|description
argument_list|)
expr_stmt|;
name|feature
operator|.
name|setCustomizer
argument_list|(
name|customizer
argument_list|)
expr_stmt|;
name|feature
operator|.
name|setScan
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|feature
operator|.
name|setUseContextBasedConfig
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|feature
operator|.
name|setResourceClasses
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|resource
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|feature
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
operator|new
name|OpenApiContextBased
argument_list|()
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
name|OpenApiContextBased
operator|.
name|class
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|createStaticBus
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFirstApi
parameter_list|()
block|{
specifier|final
name|String
name|swaggerJson
init|=
name|createWebClient
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/api/openapi.json"
argument_list|)
operator|.
name|get
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|swaggerJson
argument_list|,
name|containsString
argument_list|(
literal|"This is first API (api)"
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|UserApplication
name|ap
init|=
name|OpenApiParseUtils
operator|.
name|getUserApplicationFromJson
argument_list|(
name|swaggerJson
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|ap
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ap
operator|.
name|getResourcesAsMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ap
operator|.
name|getResourcesAsMap
argument_list|()
operator|.
name|get
argument_list|(
literal|""
argument_list|)
operator|.
name|getOperations
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSecondApi
parameter_list|()
block|{
specifier|final
name|String
name|swaggerJson
init|=
name|createWebClient
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/api2/openapi.json"
argument_list|)
operator|.
name|get
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|swaggerJson
argument_list|,
name|containsString
argument_list|(
literal|"This is second API (api2)"
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|UserApplication
name|ap
init|=
name|OpenApiParseUtils
operator|.
name|getUserApplicationFromJson
argument_list|(
name|swaggerJson
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|ap
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ap
operator|.
name|getResourcesAsMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ap
operator|.
name|getResourcesAsMap
argument_list|()
operator|.
name|get
argument_list|(
literal|""
argument_list|)
operator|.
name|getOperations
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|WebClient
name|createWebClient
parameter_list|(
specifier|final
name|String
name|url
parameter_list|)
block|{
return|return
name|WebClient
operator|.
name|create
argument_list|(
name|url
argument_list|,
name|Arrays
operator|.
expr|<
name|Object
operator|>
name|asList
argument_list|(
operator|new
name|JacksonJsonProvider
argument_list|()
argument_list|)
argument_list|,
name|Arrays
operator|.
expr|<
name|Feature
operator|>
name|asList
argument_list|(
operator|new
name|LoggingFeature
argument_list|()
argument_list|)
argument_list|,
literal|null
argument_list|)
operator|.
name|accept
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
operator|.
name|accept
argument_list|(
literal|"application/yaml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

