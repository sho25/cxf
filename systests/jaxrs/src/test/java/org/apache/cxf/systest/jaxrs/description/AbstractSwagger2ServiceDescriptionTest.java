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
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|List
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
name|HttpMethod
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
name|feature
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
name|helpers
operator|.
name|IOUtils
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
name|lifecycle
operator|.
name|SingletonResourceProvider
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
name|Parameter
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
name|ParameterType
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
name|model
operator|.
name|UserOperation
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
name|UserResource
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
name|swagger
operator|.
name|Swagger2Feature
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
name|swagger
operator|.
name|SwaggerUtils
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

begin_import
import|import
name|org
operator|.
name|yaml
operator|.
name|snakeyaml
operator|.
name|Yaml
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractSwagger2ServiceDescriptionTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
annotation|@
name|Ignore
specifier|public
specifier|abstract
specifier|static
class|class
name|Server
extends|extends
name|AbstractBusTestServerBase
block|{
specifier|protected
specifier|final
name|String
name|port
decl_stmt|;
specifier|protected
specifier|final
name|boolean
name|runAsFilter
decl_stmt|;
name|Server
parameter_list|(
specifier|final
name|String
name|port
parameter_list|,
specifier|final
name|boolean
name|runAsFilter
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
name|this
operator|.
name|runAsFilter
operator|=
name|runAsFilter
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|run
parameter_list|()
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
name|BookStoreSwagger2
operator|.
name|class
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setResourceProvider
argument_list|(
name|BookStoreSwagger2
operator|.
name|class
argument_list|,
operator|new
name|SingletonResourceProvider
argument_list|(
operator|new
name|BookStoreSwagger2
argument_list|()
argument_list|)
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
name|Swagger2Feature
name|feature
init|=
operator|new
name|Swagger2Feature
argument_list|()
decl_stmt|;
name|feature
operator|.
name|setRunAsFilter
argument_list|(
name|runAsFilter
argument_list|)
expr_stmt|;
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
name|port
operator|+
literal|"/"
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setExtensionMappings
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"json"
argument_list|,
literal|"application/json;charset=UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
name|sf
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
specifier|protected
specifier|static
name|void
name|start
parameter_list|(
specifier|final
name|Server
name|s
parameter_list|)
block|{
try|try
block|{
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
specifier|protected
specifier|static
name|void
name|startServers
parameter_list|(
specifier|final
name|Class
argument_list|<
name|?
extends|extends
name|Server
argument_list|>
name|serverClass
parameter_list|)
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
name|serverClass
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|createStaticBus
argument_list|()
expr_stmt|;
block|}
specifier|protected
specifier|abstract
name|String
name|getPort
parameter_list|()
function_decl|;
specifier|protected
specifier|abstract
name|String
name|getExpectedFileYaml
parameter_list|()
function_decl|;
specifier|protected
name|void
name|doTestApiListingIsProperlyReturnedJSON
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|WebClient
name|client
init|=
name|createWebClient
argument_list|(
literal|"/swagger.json"
argument_list|)
decl_stmt|;
try|try
block|{
name|String
name|swaggerJson
init|=
name|client
operator|.
name|get
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|UserApplication
name|ap
init|=
name|SwaggerUtils
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
name|List
argument_list|<
name|UserResource
argument_list|>
name|urs
init|=
name|ap
operator|.
name|getResources
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|urs
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|urs
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|UserResource
name|r
init|=
name|urs
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"/bookstore"
argument_list|,
name|r
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|UserOperation
argument_list|>
name|map
init|=
name|r
operator|.
name|getOperationsAsMap
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|UserOperation
name|getBooksOp
init|=
name|map
operator|.
name|get
argument_list|(
literal|"getBooks"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|HttpMethod
operator|.
name|GET
argument_list|,
name|getBooksOp
operator|.
name|getVerb
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/"
argument_list|,
name|getBooksOp
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|,
name|getBooksOp
operator|.
name|getProduces
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Parameter
argument_list|>
name|getBooksOpParams
init|=
name|getBooksOp
operator|.
name|getParameters
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getBooksOpParams
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ParameterType
operator|.
name|QUERY
argument_list|,
name|getBooksOpParams
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|UserOperation
name|getBookOp
init|=
name|map
operator|.
name|get
argument_list|(
literal|"getBook"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|HttpMethod
operator|.
name|GET
argument_list|,
name|getBookOp
operator|.
name|getVerb
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/{id}"
argument_list|,
name|getBookOp
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|,
name|getBookOp
operator|.
name|getProduces
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Parameter
argument_list|>
name|getBookOpParams
init|=
name|getBookOp
operator|.
name|getParameters
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getBookOpParams
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ParameterType
operator|.
name|PATH
argument_list|,
name|getBookOpParams
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|UserOperation
name|deleteOp
init|=
name|map
operator|.
name|get
argument_list|(
literal|"delete"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|HttpMethod
operator|.
name|DELETE
argument_list|,
name|deleteOp
operator|.
name|getVerb
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/{id}"
argument_list|,
name|deleteOp
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Parameter
argument_list|>
name|delOpParams
init|=
name|deleteOp
operator|.
name|getParameters
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|delOpParams
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ParameterType
operator|.
name|PATH
argument_list|,
name|delOpParams
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|client
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
annotation|@
name|Ignore
specifier|public
name|void
name|testApiListingIsProperlyReturnedYAML
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|WebClient
name|client
init|=
name|createWebClient
argument_list|(
literal|"/swagger.yaml"
argument_list|)
decl_stmt|;
try|try
block|{
specifier|final
name|Response
name|r
init|=
name|client
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|OK
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
comment|//REVISIT find a better way of reliably comparing two yaml instances.
comment|// I noticed that yaml.load instantiates a Map and
comment|// for an integer valued key, an Integer or a String is arbitrarily instantiated,
comment|// which leads to the assertion error. So, we serilialize the yamls and compare the re-serialized texts.
name|Yaml
name|yaml
init|=
operator|new
name|Yaml
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|yaml
operator|.
name|load
argument_list|(
name|getExpectedValue
argument_list|(
name|getExpectedFileYaml
argument_list|()
argument_list|,
name|getPort
argument_list|()
argument_list|)
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|,
name|yaml
operator|.
name|load
argument_list|(
name|IOUtils
operator|.
name|readStringFromStream
argument_list|(
operator|(
name|InputStream
operator|)
name|r
operator|.
name|getEntity
argument_list|()
argument_list|)
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|client
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
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
literal|"http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
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
specifier|private
specifier|static
name|String
name|getExpectedValue
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
modifier|...
name|args
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|String
operator|.
name|format
argument_list|(
name|IOUtils
operator|.
name|readStringFromStream
argument_list|(
name|AbstractSwagger2ServiceDescriptionTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
name|name
argument_list|)
argument_list|)
argument_list|,
name|args
argument_list|)
return|;
block|}
block|}
end_class

end_unit

