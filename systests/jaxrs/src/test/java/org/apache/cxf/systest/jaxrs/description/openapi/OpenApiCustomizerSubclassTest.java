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
name|doc
operator|.
name|JavaDocProvider
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

begin_class
specifier|public
class|class
name|OpenApiCustomizerSubclassTest
extends|extends
name|AbstractOpenApiServiceDescriptionTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|PORT
init|=
name|allocatePort
argument_list|(
name|OpenApiCustomizerSubclassTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
class|class
name|OpenApiRegular
extends|extends
name|Server
block|{
specifier|public
name|OpenApiRegular
parameter_list|()
block|{
name|super
argument_list|(
name|PORT
argument_list|,
literal|false
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
name|start
argument_list|(
operator|new
name|OpenApiRegular
argument_list|()
argument_list|)
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
annotation|@
name|Override
specifier|protected
name|OpenApiFeature
name|createOpenApiFeature
parameter_list|()
block|{
specifier|final
name|OpenApiCustomizer
name|customizer
init|=
operator|new
name|OpenApiCustomizer
argument_list|()
block|{
specifier|public
name|void
name|customize
parameter_list|(
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
name|OpenAPI
name|oas
parameter_list|)
block|{
name|super
operator|.
name|customize
argument_list|(
name|oas
argument_list|)
expr_stmt|;
name|oas
operator|.
name|getInfo
argument_list|()
operator|.
name|setDescription
argument_list|(
literal|"Custom Description"
argument_list|)
expr_stmt|;
name|oas
operator|.
name|getInfo
argument_list|()
operator|.
name|getLicense
argument_list|()
operator|.
name|setName
argument_list|(
literal|"Custom License"
argument_list|)
expr_stmt|;
name|oas
operator|.
name|getComponents
argument_list|()
operator|.
name|getSecuritySchemes
argument_list|()
operator|.
name|put
argument_list|(
literal|"openid"
argument_list|,
operator|new
name|SecurityScheme
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|customizer
operator|.
name|setJavadocProvider
argument_list|(
operator|new
name|JavaDocProvider
argument_list|()
argument_list|)
expr_stmt|;
name|customizer
operator|.
name|setDynamicBasePath
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|customizer
operator|.
name|setReplaceTags
argument_list|(
literal|true
argument_list|)
expr_stmt|;
specifier|final
name|OpenApiFeature
name|feature
init|=
name|super
operator|.
name|createOpenApiFeature
argument_list|()
decl_stmt|;
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
name|setResourcePackages
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|getClass
argument_list|()
operator|.
name|getPackage
argument_list|()
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
block|}
annotation|@
name|Override
specifier|protected
name|String
name|getDescription
parameter_list|()
block|{
return|return
literal|"Custom Description"
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|getLicense
parameter_list|()
block|{
return|return
literal|"Custom License"
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|getSecurityDefinitionName
parameter_list|()
block|{
return|return
literal|"openid"
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|getTags
parameter_list|()
block|{
return|return
literal|"_bookstore"
return|;
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
name|startServers
argument_list|(
name|OpenApiRegular
operator|.
name|class
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
annotation|@
name|Override
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
operator|+
name|getApplicationPath
argument_list|()
return|;
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
annotation|@
name|Test
specifier|public
name|void
name|testApiListingIsProperlyReturnedJSON
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestApiListingIsProperlyReturnedJSON
argument_list|(
literal|false
argument_list|,
literal|"http://localhost:"
operator|+
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
