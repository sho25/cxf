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
name|hamcrest
operator|.
name|CoreMatchers
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|v3
operator|.
name|oas
operator|.
name|models
operator|.
name|security
operator|.
name|SecurityScheme
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|v3
operator|.
name|oas
operator|.
name|models
operator|.
name|security
operator|.
name|SecurityScheme
operator|.
name|Type
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

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractOpenApiServiceDescriptionTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|static
specifier|final
name|String
name|SECURITY_DEFINITION_NAME
init|=
literal|"basicAuth"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CONTACT
init|=
literal|"cxf@apache.org"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TITLE
init|=
literal|"CXF unittest"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DESCRIPTION
init|=
literal|"API Description"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|LICENSE
init|=
literal|"API License"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|LICENSE_URL
init|=
literal|"API License URL"
decl_stmt|;
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
name|BookStoreOpenApi
operator|.
name|class
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setResourceClasses
argument_list|(
name|BookStoreStylesheetsOpenApi
operator|.
name|class
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setResourceProvider
argument_list|(
name|BookStoreOpenApi
operator|.
name|class
argument_list|,
operator|new
name|SingletonResourceProvider
argument_list|(
operator|new
name|BookStoreOpenApi
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
name|OpenApiFeature
name|feature
init|=
name|createOpenApiFeature
argument_list|()
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
name|port
operator|+
literal|"/"
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
parameter_list|()
block|{
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
name|setRunAsFilter
argument_list|(
name|runAsFilter
argument_list|)
expr_stmt|;
name|feature
operator|.
name|setContactName
argument_list|(
name|CONTACT
argument_list|)
expr_stmt|;
name|feature
operator|.
name|setTitle
argument_list|(
name|TITLE
argument_list|)
expr_stmt|;
name|feature
operator|.
name|setDescription
argument_list|(
name|DESCRIPTION
argument_list|)
expr_stmt|;
name|feature
operator|.
name|setLicense
argument_list|(
name|LICENSE
argument_list|)
expr_stmt|;
name|feature
operator|.
name|setLicenseUrl
argument_list|(
name|LICENSE_URL
argument_list|)
expr_stmt|;
name|feature
operator|.
name|setSecurityDefinitions
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
name|SECURITY_DEFINITION_NAME
argument_list|,
operator|new
name|SecurityScheme
argument_list|()
operator|.
name|type
argument_list|(
name|Type
operator|.
name|HTTP
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|feature
return|;
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
name|void
name|doTestApiListingIsProperlyReturnedJSON
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestApiListingIsProperlyReturnedJSON
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|doTestApiListingIsProperlyReturnedJSON
parameter_list|(
name|boolean
name|useXForwarded
parameter_list|)
throws|throws
name|Exception
block|{
name|doTestApiListingIsProperlyReturnedJSON
argument_list|(
name|useXForwarded
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|doTestApiListingIsProperlyReturnedJSON
parameter_list|(
name|boolean
name|useXForwarded
parameter_list|,
name|String
name|basePath
parameter_list|)
throws|throws
name|Exception
block|{
name|doTestApiListingIsProperlyReturnedJSON
argument_list|(
name|createWebClient
argument_list|(
literal|"/openapi.json"
argument_list|)
argument_list|,
name|useXForwarded
argument_list|,
name|basePath
argument_list|)
expr_stmt|;
name|checkUiResource
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|doTestApiListingIsProperlyReturnedJSON
parameter_list|(
specifier|final
name|WebClient
name|client
parameter_list|,
name|boolean
name|useXForwarded
parameter_list|,
name|String
name|basePath
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|useXForwarded
condition|)
block|{
name|client
operator|.
name|header
argument_list|(
literal|"USE_XFORWARDED"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|basePath
operator|==
literal|null
condition|)
block|{
name|assertEquals
argument_list|(
name|useXForwarded
condition|?
literal|"/reverse"
else|:
literal|"/"
argument_list|,
name|ap
operator|.
name|getBasePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertEquals
argument_list|(
name|basePath
argument_list|,
name|ap
operator|.
name|getBasePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|getApplicationPath
argument_list|()
operator|+
literal|"/bookstore"
argument_list|,
name|getBooksOp
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
comment|// see https://github.com/swagger-api/swagger-core/issues/2646
if|if
condition|(
name|getBooksOp
operator|.
name|getProduces
argument_list|()
operator|!=
literal|null
condition|)
block|{
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
block|}
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
name|getApplicationPath
argument_list|()
operator|+
literal|"/bookstore/{id}"
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
name|getApplicationPath
argument_list|()
operator|+
literal|"/bookstore/{id}"
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
name|assertThat
argument_list|(
name|swaggerJson
argument_list|,
name|CoreMatchers
operator|.
name|containsString
argument_list|(
name|CONTACT
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|swaggerJson
argument_list|,
name|CoreMatchers
operator|.
name|containsString
argument_list|(
name|TITLE
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|swaggerJson
argument_list|,
name|CoreMatchers
operator|.
name|containsString
argument_list|(
name|DESCRIPTION
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|swaggerJson
argument_list|,
name|CoreMatchers
operator|.
name|containsString
argument_list|(
name|LICENSE
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|swaggerJson
argument_list|,
name|CoreMatchers
operator|.
name|containsString
argument_list|(
name|LICENSE_URL
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|swaggerJson
argument_list|,
name|CoreMatchers
operator|.
name|containsString
argument_list|(
name|SECURITY_DEFINITION_NAME
argument_list|)
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
specifier|public
name|void
name|testNonUiResource
parameter_list|()
block|{
comment|// Test that Swagger UI resources do not interfere with
comment|// application-specific ones.
name|WebClient
name|uiClient
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|getBaseUrl
argument_list|()
operator|+
literal|"/css/book.css"
argument_list|)
operator|.
name|accept
argument_list|(
literal|"text/css"
argument_list|)
decl_stmt|;
name|String
name|css
init|=
name|uiClient
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
name|css
argument_list|,
name|equalTo
argument_list|(
literal|"body { background-color: lightblue; }"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUiResource
parameter_list|()
block|{
comment|// Test that Swagger UI resources do not interfere with
comment|// application-specific ones and are accessible.
name|WebClient
name|uiClient
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|getBaseUrl
argument_list|()
operator|+
literal|"/swagger-ui.css"
argument_list|)
operator|.
name|accept
argument_list|(
literal|"text/css"
argument_list|)
decl_stmt|;
name|String
name|css
init|=
name|uiClient
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
name|css
argument_list|,
name|containsString
argument_list|(
literal|".swagger-ui{font"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|String
name|getApplicationPath
parameter_list|()
block|{
return|return
literal|""
return|;
block|}
specifier|protected
name|String
name|getBaseUrl
parameter_list|()
block|{
return|return
literal|"http://localhost:"
operator|+
name|getPort
argument_list|()
return|;
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
name|getBaseUrl
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
specifier|protected
name|void
name|checkUiResource
parameter_list|()
block|{
name|WebClient
name|uiClient
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|getBaseUrl
argument_list|()
operator|+
literal|"/api-docs"
argument_list|)
operator|.
name|accept
argument_list|(
name|MediaType
operator|.
name|WILDCARD
argument_list|)
decl_stmt|;
name|String
name|uiHtml
init|=
name|uiClient
operator|.
name|get
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|uiHtml
operator|.
name|contains
argument_list|(
literal|"<title>Swagger UI</title>"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

