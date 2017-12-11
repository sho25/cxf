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
name|core
operator|.
name|MediaType
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
name|json
operator|.
name|basic
operator|.
name|JsonMapObject
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
name|openapi
operator|.
name|SwaggerToOpenApiConversionFilter
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
name|SwaggerToOpenApiConversionTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|static
specifier|final
name|String
name|PORT
init|=
name|allocatePort
argument_list|(
name|SwaggerToOpenApiConversionTest
operator|.
name|class
argument_list|)
decl_stmt|;
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
specifier|static
class|class
name|Server
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
name|SwaggerToOpenApiConversionFilter
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Swagger2Feature
name|feature
init|=
name|createSwagger2Feature
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
name|PORT
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
name|Swagger2Feature
name|createSwagger2Feature
parameter_list|()
block|{
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
name|setContact
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
name|io
operator|.
name|swagger
operator|.
name|models
operator|.
name|auth
operator|.
name|BasicAuthDefinition
argument_list|()
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
name|Server
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
name|Test
specifier|public
name|void
name|testOpenApiJSON
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
literal|"/openapi.json"
argument_list|)
decl_stmt|;
name|String
name|openApiResponse
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
name|JsonMapObject
name|openApiJson
init|=
operator|new
name|JsonMapObjectReaderWriter
argument_list|()
operator|.
name|fromJsonToJsonObject
argument_list|(
name|openApiResponse
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|openApiJson
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// version
name|assertEquals
argument_list|(
literal|"3.0.0"
argument_list|,
name|openApiJson
operator|.
name|getProperty
argument_list|(
literal|"openapi"
argument_list|)
argument_list|)
expr_stmt|;
comment|// servers
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|servers
init|=
name|openApiJson
operator|.
name|getListMapProperty
argument_list|(
literal|"servers"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|servers
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|servers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"https://localhost:"
operator|+
name|PORT
argument_list|,
name|servers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|get
argument_list|(
literal|"url"
argument_list|)
argument_list|)
expr_stmt|;
comment|// info
name|JsonMapObject
name|info
init|=
name|openApiJson
operator|.
name|getJsonMapProperty
argument_list|(
literal|"info"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|info
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DESCRIPTION
argument_list|,
name|info
operator|.
name|getProperty
argument_list|(
literal|"description"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TITLE
argument_list|,
name|info
operator|.
name|getProperty
argument_list|(
literal|"title"
argument_list|)
argument_list|)
expr_stmt|;
name|JsonMapObject
name|contact
init|=
name|info
operator|.
name|getJsonMapProperty
argument_list|(
literal|"contact"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|contact
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|CONTACT
argument_list|,
name|contact
operator|.
name|getProperty
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|JsonMapObject
name|license
init|=
name|info
operator|.
name|getJsonMapProperty
argument_list|(
literal|"license"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|license
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LICENSE
argument_list|,
name|license
operator|.
name|getProperty
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LICENSE_URL
argument_list|,
name|license
operator|.
name|getProperty
argument_list|(
literal|"url"
argument_list|)
argument_list|)
expr_stmt|;
comment|// tags
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|tags
init|=
name|openApiJson
operator|.
name|getListMapProperty
argument_list|(
literal|"tags"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|tags
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|tags
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bookstore"
argument_list|,
name|tags
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
comment|// paths
name|JsonMapObject
name|paths
init|=
name|openApiJson
operator|.
name|getJsonMapProperty
argument_list|(
literal|"paths"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|paths
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|//1: bookstore
name|JsonMapObject
name|bookstore
init|=
name|paths
operator|.
name|getJsonMapProperty
argument_list|(
literal|"/bookstore"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|bookstore
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// get
name|verifyBookStoreGet
argument_list|(
name|bookstore
argument_list|)
expr_stmt|;
comment|//2: bookstore/{id}
name|JsonMapObject
name|bookstoreId
init|=
name|paths
operator|.
name|getJsonMapProperty
argument_list|(
literal|"/bookstore/{id}"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|bookstoreId
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// get
name|verifyBookStoreIdGet
argument_list|(
name|bookstoreId
argument_list|)
expr_stmt|;
comment|// delete
name|verifyBookStoreIdDelete
argument_list|(
name|bookstoreId
argument_list|)
expr_stmt|;
comment|// components
name|JsonMapObject
name|comps
init|=
name|openApiJson
operator|.
name|getJsonMapProperty
argument_list|(
literal|"components"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|comps
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|JsonMapObject
name|requestBodies
init|=
name|comps
operator|.
name|getJsonMapProperty
argument_list|(
literal|"requestBodies"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|requestBodies
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|JsonMapObject
name|schemas
init|=
name|comps
operator|.
name|getJsonMapProperty
argument_list|(
literal|"schemas"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|schemas
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|JsonMapObject
name|secSchemes
init|=
name|comps
operator|.
name|getJsonMapProperty
argument_list|(
literal|"securitySchemes"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|secSchemes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|verifyBookStoreGet
parameter_list|(
name|JsonMapObject
name|bookstore
parameter_list|)
block|{
name|JsonMapObject
name|bookstoreGet
init|=
name|bookstore
operator|.
name|getJsonMapProperty
argument_list|(
literal|"get"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|bookstoreGet
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|bookstoreGetTags
init|=
name|bookstoreGet
operator|.
name|getListStringProperty
argument_list|(
literal|"tags"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|bookstoreGetTags
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bookstore"
argument_list|,
name|bookstoreGetTags
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get books"
argument_list|,
name|bookstoreGet
operator|.
name|getProperty
argument_list|(
literal|"summary"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get books"
argument_list|,
name|bookstoreGet
operator|.
name|getProperty
argument_list|(
literal|"description"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getBooks"
argument_list|,
name|bookstoreGet
operator|.
name|getProperty
argument_list|(
literal|"operationId"
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|bookstoreGetParams
init|=
name|bookstoreGet
operator|.
name|getListMapProperty
argument_list|(
literal|"parameters"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|bookstoreGetParams
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|JsonMapObject
name|firstParam
init|=
operator|new
name|JsonMapObject
argument_list|(
name|bookstoreGetParams
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|firstParam
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"page"
argument_list|,
name|firstParam
operator|.
name|getProperty
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"query"
argument_list|,
name|firstParam
operator|.
name|getProperty
argument_list|(
literal|"in"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Page to fetch"
argument_list|,
name|firstParam
operator|.
name|getProperty
argument_list|(
literal|"description"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|firstParam
operator|.
name|getBooleanProperty
argument_list|(
literal|"required"
argument_list|)
argument_list|)
expr_stmt|;
name|JsonMapObject
name|schema
init|=
name|firstParam
operator|.
name|getJsonMapProperty
argument_list|(
literal|"schema"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|schema
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"integer"
argument_list|,
name|schema
operator|.
name|getProperty
argument_list|(
literal|"type"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"int32"
argument_list|,
name|schema
operator|.
name|getProperty
argument_list|(
literal|"format"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|1
argument_list|)
argument_list|,
name|schema
operator|.
name|getIntegerProperty
argument_list|(
literal|"default"
argument_list|)
argument_list|)
expr_stmt|;
name|JsonMapObject
name|responses
init|=
name|bookstoreGet
operator|.
name|getJsonMapProperty
argument_list|(
literal|"responses"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|responses
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|JsonMapObject
name|okResp
init|=
name|responses
operator|.
name|getJsonMapProperty
argument_list|(
literal|"200"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|okResp
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"successful operation"
argument_list|,
name|okResp
operator|.
name|getProperty
argument_list|(
literal|"description"
argument_list|)
argument_list|)
expr_stmt|;
name|JsonMapObject
name|content
init|=
name|okResp
operator|.
name|getJsonMapProperty
argument_list|(
literal|"content"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|content
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|JsonMapObject
name|jsonResp
init|=
name|content
operator|.
name|getJsonMapProperty
argument_list|(
literal|"application/json"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|jsonResp
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|JsonMapObject
name|jsonRespSchema
init|=
name|jsonResp
operator|.
name|getJsonMapProperty
argument_list|(
literal|"schema"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|jsonRespSchema
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"array"
argument_list|,
name|jsonRespSchema
operator|.
name|getProperty
argument_list|(
literal|"type"
argument_list|)
argument_list|)
expr_stmt|;
name|JsonMapObject
name|jsonRespSchemaItems
init|=
name|jsonRespSchema
operator|.
name|getJsonMapProperty
argument_list|(
literal|"items"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|jsonRespSchemaItems
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"#components/schemas/Book"
argument_list|,
name|jsonRespSchemaItems
operator|.
name|getProperty
argument_list|(
literal|"$ref"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|verifyBookStoreIdGet
parameter_list|(
name|JsonMapObject
name|bookstore
parameter_list|)
block|{
name|JsonMapObject
name|bookstoreGet
init|=
name|bookstore
operator|.
name|getJsonMapProperty
argument_list|(
literal|"get"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|bookstoreGet
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|bookstoreGetTags
init|=
name|bookstoreGet
operator|.
name|getListStringProperty
argument_list|(
literal|"tags"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|bookstoreGetTags
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bookstore"
argument_list|,
name|bookstoreGetTags
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get book by Id"
argument_list|,
name|bookstoreGet
operator|.
name|getProperty
argument_list|(
literal|"summary"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get book by Id"
argument_list|,
name|bookstoreGet
operator|.
name|getProperty
argument_list|(
literal|"description"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getBook"
argument_list|,
name|bookstoreGet
operator|.
name|getProperty
argument_list|(
literal|"operationId"
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|bookstoreGetParams
init|=
name|bookstoreGet
operator|.
name|getListMapProperty
argument_list|(
literal|"parameters"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|bookstoreGetParams
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|JsonMapObject
name|firstParam
init|=
operator|new
name|JsonMapObject
argument_list|(
name|bookstoreGetParams
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|firstParam
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"id"
argument_list|,
name|firstParam
operator|.
name|getProperty
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"path"
argument_list|,
name|firstParam
operator|.
name|getProperty
argument_list|(
literal|"in"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"id"
argument_list|,
name|firstParam
operator|.
name|getProperty
argument_list|(
literal|"description"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|firstParam
operator|.
name|getBooleanProperty
argument_list|(
literal|"required"
argument_list|)
argument_list|)
expr_stmt|;
name|JsonMapObject
name|schema
init|=
name|firstParam
operator|.
name|getJsonMapProperty
argument_list|(
literal|"schema"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|schema
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"integer"
argument_list|,
name|schema
operator|.
name|getProperty
argument_list|(
literal|"type"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"int64"
argument_list|,
name|schema
operator|.
name|getProperty
argument_list|(
literal|"format"
argument_list|)
argument_list|)
expr_stmt|;
name|JsonMapObject
name|responses
init|=
name|bookstoreGet
operator|.
name|getJsonMapProperty
argument_list|(
literal|"responses"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|responses
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|JsonMapObject
name|okResp
init|=
name|responses
operator|.
name|getJsonMapProperty
argument_list|(
literal|"200"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|okResp
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"successful operation"
argument_list|,
name|okResp
operator|.
name|getProperty
argument_list|(
literal|"description"
argument_list|)
argument_list|)
expr_stmt|;
name|JsonMapObject
name|content
init|=
name|okResp
operator|.
name|getJsonMapProperty
argument_list|(
literal|"content"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|content
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|JsonMapObject
name|jsonResp
init|=
name|content
operator|.
name|getJsonMapProperty
argument_list|(
literal|"application/json"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|jsonResp
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|JsonMapObject
name|jsonRespSchema
init|=
name|jsonResp
operator|.
name|getJsonMapProperty
argument_list|(
literal|"schema"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|jsonRespSchema
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"#components/schemas/Book"
argument_list|,
name|jsonRespSchema
operator|.
name|getProperty
argument_list|(
literal|"$ref"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|verifyBookStoreIdDelete
parameter_list|(
name|JsonMapObject
name|bookstore
parameter_list|)
block|{
name|JsonMapObject
name|bookstoreDel
init|=
name|bookstore
operator|.
name|getJsonMapProperty
argument_list|(
literal|"delete"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|bookstoreDel
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|bookstoreDelTags
init|=
name|bookstoreDel
operator|.
name|getListStringProperty
argument_list|(
literal|"tags"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|bookstoreDelTags
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bookstore"
argument_list|,
name|bookstoreDelTags
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Delete book"
argument_list|,
name|bookstoreDel
operator|.
name|getProperty
argument_list|(
literal|"summary"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Delete book"
argument_list|,
name|bookstoreDel
operator|.
name|getProperty
argument_list|(
literal|"description"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"delete"
argument_list|,
name|bookstoreDel
operator|.
name|getProperty
argument_list|(
literal|"operationId"
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|bookstoreDelParams
init|=
name|bookstoreDel
operator|.
name|getListMapProperty
argument_list|(
literal|"parameters"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|bookstoreDelParams
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|JsonMapObject
name|firstParam
init|=
operator|new
name|JsonMapObject
argument_list|(
name|bookstoreDelParams
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|firstParam
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"id"
argument_list|,
name|firstParam
operator|.
name|getProperty
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"path"
argument_list|,
name|firstParam
operator|.
name|getProperty
argument_list|(
literal|"in"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"id"
argument_list|,
name|firstParam
operator|.
name|getProperty
argument_list|(
literal|"description"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|firstParam
operator|.
name|getBooleanProperty
argument_list|(
literal|"required"
argument_list|)
argument_list|)
expr_stmt|;
name|JsonMapObject
name|schema
init|=
name|firstParam
operator|.
name|getJsonMapProperty
argument_list|(
literal|"schema"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|schema
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"string"
argument_list|,
name|schema
operator|.
name|getProperty
argument_list|(
literal|"type"
argument_list|)
argument_list|)
expr_stmt|;
name|JsonMapObject
name|responses
init|=
name|bookstoreDel
operator|.
name|getJsonMapProperty
argument_list|(
literal|"responses"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|responses
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|JsonMapObject
name|okResp
init|=
name|responses
operator|.
name|getJsonMapProperty
argument_list|(
literal|"default"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|okResp
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"successful operation"
argument_list|,
name|okResp
operator|.
name|getProperty
argument_list|(
literal|"description"
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
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
name|url
argument_list|)
operator|.
name|accept
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
decl_stmt|;
name|WebClient
operator|.
name|getConfig
argument_list|(
name|wc
argument_list|)
operator|.
name|getHttpConduit
argument_list|()
operator|.
name|getClient
argument_list|()
operator|.
name|setReceiveTimeout
argument_list|(
literal|10000000L
argument_list|)
expr_stmt|;
return|return
name|wc
return|;
block|}
block|}
end_class

end_unit
