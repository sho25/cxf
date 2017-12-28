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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
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
name|Application
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
name|Bus
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
name|annotations
operator|.
name|Provider
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
name|annotations
operator|.
name|Provider
operator|.
name|Scope
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
name|annotations
operator|.
name|Provider
operator|.
name|Type
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
name|common
operator|.
name|util
operator|.
name|PropertyUtils
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
name|common
operator|.
name|util
operator|.
name|StringUtils
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
name|endpoint
operator|.
name|Server
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
name|AbstractFeature
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
name|JAXRSServiceFactoryBean
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
name|ApplicationInfo
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
name|ClassResourceInfo
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
name|provider
operator|.
name|ServerProviderFactory
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
name|SwaggerUiSupport
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
name|jaxrs2
operator|.
name|integration
operator|.
name|JaxrsOpenApiContextBuilder
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
name|jaxrs2
operator|.
name|integration
operator|.
name|resources
operator|.
name|OpenApiResource
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
name|integration
operator|.
name|GenericOpenApiContextBuilder
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
name|integration
operator|.
name|OpenApiConfigurationException
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
name|integration
operator|.
name|SwaggerConfiguration
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
name|integration
operator|.
name|api
operator|.
name|OpenAPIConfiguration
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
name|integration
operator|.
name|api
operator|.
name|OpenApiContext
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
name|OpenAPI
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
name|info
operator|.
name|Contact
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
name|info
operator|.
name|Info
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
name|info
operator|.
name|License
import|;
end_import

begin_class
annotation|@
name|Provider
argument_list|(
name|value
operator|=
name|Type
operator|.
name|Feature
argument_list|,
name|scope
operator|=
name|Scope
operator|.
name|Server
argument_list|)
specifier|public
class|class
name|OpenApiFeature
extends|extends
name|AbstractFeature
implements|implements
name|SwaggerUiSupport
implements|,
name|SwaggerProperties
block|{
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_PROPS_LOCATION
init|=
literal|"/swagger.properties"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_LICENSE_VALUE
init|=
literal|"Apache 2.0 License"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_LICENSE_URL
init|=
literal|"http://www.apache.org/licenses/LICENSE-2.0.html"
decl_stmt|;
specifier|private
name|String
name|version
decl_stmt|;
specifier|private
name|String
name|title
decl_stmt|;
specifier|private
name|String
name|description
decl_stmt|;
specifier|private
name|String
name|contactName
decl_stmt|;
specifier|private
name|String
name|contactEmail
decl_stmt|;
specifier|private
name|String
name|contactUrl
decl_stmt|;
specifier|private
name|String
name|license
decl_stmt|;
specifier|private
name|String
name|licenseUrl
decl_stmt|;
specifier|private
name|String
name|termsOfServiceUrl
decl_stmt|;
comment|// Read all operations also with no @Operation
specifier|private
name|boolean
name|readAllResources
init|=
literal|true
decl_stmt|;
comment|// Scan all JAX-RS resources automatically
specifier|private
name|boolean
name|scan
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|prettyPrint
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|runAsFilter
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|String
argument_list|>
name|ignoredRoutes
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|resourcePackages
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|resourceClasses
decl_stmt|;
specifier|private
name|String
name|filterClass
decl_stmt|;
specifier|private
name|Boolean
name|supportSwaggerUi
decl_stmt|;
specifier|private
name|String
name|swaggerUiVersion
decl_stmt|;
specifier|private
name|String
name|swaggerUiMavenGroupAndArtifact
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|swaggerUiMediaTypes
decl_stmt|;
comment|// Allows to pass the configuration location, usually openapi-configuration.json
comment|// or openapi-configuration.yml file.
specifier|private
name|String
name|configLocation
decl_stmt|;
comment|// Allows to pass the properties location, by default swagger.properties
specifier|private
name|String
name|propertiesLocation
init|=
name|DEFAULT_PROPS_LOCATION
decl_stmt|;
specifier|protected
specifier|static
class|class
name|DefaultApplication
extends|extends
name|Application
block|{
specifier|private
specifier|final
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|serviceClasses
decl_stmt|;
name|DefaultApplication
parameter_list|(
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|serviceClasses
parameter_list|)
block|{
name|this
operator|.
name|serviceClasses
operator|=
name|serviceClasses
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|getClasses
parameter_list|()
block|{
return|return
name|serviceClasses
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|initialize
parameter_list|(
name|Server
name|server
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
specifier|final
name|JAXRSServiceFactoryBean
name|sfb
init|=
operator|(
name|JAXRSServiceFactoryBean
operator|)
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|get
argument_list|(
name|JAXRSServiceFactoryBean
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|ServerProviderFactory
name|factory
init|=
operator|(
name|ServerProviderFactory
operator|)
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|get
argument_list|(
name|ServerProviderFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|packages
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|resourcePackages
operator|!=
literal|null
condition|)
block|{
name|packages
operator|.
name|addAll
argument_list|(
name|resourcePackages
argument_list|)
expr_stmt|;
block|}
name|Properties
name|swaggerProps
init|=
literal|null
decl_stmt|;
name|GenericOpenApiContextBuilder
argument_list|<
name|?
argument_list|>
name|openApiConfiguration
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|getConfigLocation
argument_list|()
argument_list|)
condition|)
block|{
name|swaggerProps
operator|=
name|getSwaggerProperties
argument_list|(
name|propertiesLocation
argument_list|,
name|bus
argument_list|)
expr_stmt|;
if|if
condition|(
name|isScan
argument_list|()
condition|)
block|{
name|packages
operator|.
name|addAll
argument_list|(
name|scanResourcePackages
argument_list|(
name|sfb
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|final
name|OpenAPI
name|oas
init|=
operator|new
name|OpenAPI
argument_list|()
operator|.
name|info
argument_list|(
name|getInfo
argument_list|(
name|swaggerProps
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|SwaggerConfiguration
name|config
init|=
operator|new
name|SwaggerConfiguration
argument_list|()
operator|.
name|openAPI
argument_list|(
name|oas
argument_list|)
operator|.
name|prettyPrint
argument_list|(
name|getOrFallback
argument_list|(
name|isPrettyPrint
argument_list|()
argument_list|,
name|swaggerProps
argument_list|,
name|PRETTY_PRINT_PROPERTY
argument_list|)
argument_list|)
operator|.
name|readAllResources
argument_list|(
name|isReadAllResources
argument_list|()
argument_list|)
operator|.
name|ignoredRoutes
argument_list|(
name|getIgnoredRoutes
argument_list|()
argument_list|)
operator|.
name|filterClass
argument_list|(
name|getOrFallback
argument_list|(
name|getFilterClass
argument_list|()
argument_list|,
name|swaggerProps
argument_list|,
name|FILTER_CLASS_PROPERTY
argument_list|)
argument_list|)
operator|.
name|resourceClasses
argument_list|(
name|getResourceClasses
argument_list|()
argument_list|)
operator|.
name|resourcePackages
argument_list|(
name|getOrFallback
argument_list|(
name|packages
argument_list|,
name|swaggerProps
argument_list|,
name|RESOURCE_PACKAGE_PROPERTY
argument_list|)
argument_list|)
decl_stmt|;
name|openApiConfiguration
operator|=
operator|new
name|JaxrsOpenApiContextBuilder
argument_list|<>
argument_list|()
operator|.
name|application
argument_list|(
name|getApplicationOrDefault
argument_list|(
name|server
argument_list|,
name|factory
argument_list|,
name|sfb
argument_list|,
name|bus
argument_list|)
argument_list|)
operator|.
name|openApiConfiguration
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|openApiConfiguration
operator|=
operator|new
name|JaxrsOpenApiContextBuilder
argument_list|<>
argument_list|()
operator|.
name|application
argument_list|(
name|getApplicationOrDefault
argument_list|(
name|server
argument_list|,
name|factory
argument_list|,
name|sfb
argument_list|,
name|bus
argument_list|)
argument_list|)
operator|.
name|configLocation
argument_list|(
name|getConfigLocation
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
specifier|final
name|OpenApiContext
name|context
init|=
name|openApiConfiguration
operator|.
name|buildContext
argument_list|(
literal|true
argument_list|)
decl_stmt|;
specifier|final
name|Properties
name|userProperties
init|=
name|getUserProperties
argument_list|(
name|context
operator|.
name|getOpenApiConfiguration
argument_list|()
operator|.
name|getUserDefinedOptions
argument_list|()
argument_list|)
decl_stmt|;
name|registerOpenApiResources
argument_list|(
name|sfb
argument_list|,
name|packages
argument_list|,
name|context
operator|.
name|getOpenApiConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|registerSwaggerUiResources
argument_list|(
name|sfb
argument_list|,
name|combine
argument_list|(
name|swaggerProps
argument_list|,
name|userProperties
argument_list|)
argument_list|,
name|factory
argument_list|,
name|bus
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OpenApiConfigurationException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unable to initialize OpenAPI context"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
name|boolean
name|isScan
parameter_list|()
block|{
return|return
name|scan
return|;
block|}
specifier|public
name|void
name|setScan
parameter_list|(
name|boolean
name|scan
parameter_list|)
block|{
name|this
operator|.
name|scan
operator|=
name|scan
expr_stmt|;
block|}
specifier|public
name|String
name|getFilterClass
parameter_list|()
block|{
return|return
name|filterClass
return|;
block|}
specifier|public
name|void
name|setFilterClass
parameter_list|(
name|String
name|filterClass
parameter_list|)
block|{
name|this
operator|.
name|filterClass
operator|=
name|filterClass
expr_stmt|;
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getResourcePackages
parameter_list|()
block|{
return|return
name|resourcePackages
return|;
block|}
specifier|public
name|void
name|setResourcePackages
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|resourcePackages
parameter_list|)
block|{
name|this
operator|.
name|resourcePackages
operator|=
operator|(
name|resourcePackages
operator|==
literal|null
operator|)
condition|?
literal|null
else|:
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|resourcePackages
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
specifier|public
name|void
name|setVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
specifier|public
name|String
name|getTitle
parameter_list|()
block|{
return|return
name|title
return|;
block|}
specifier|public
name|void
name|setTitle
parameter_list|(
name|String
name|title
parameter_list|)
block|{
name|this
operator|.
name|title
operator|=
name|title
expr_stmt|;
block|}
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
block|}
specifier|public
name|String
name|getContactName
parameter_list|()
block|{
return|return
name|contactName
return|;
block|}
specifier|public
name|void
name|setContactName
parameter_list|(
name|String
name|contactName
parameter_list|)
block|{
name|this
operator|.
name|contactName
operator|=
name|contactName
expr_stmt|;
block|}
specifier|public
name|String
name|getContactEmail
parameter_list|()
block|{
return|return
name|contactEmail
return|;
block|}
specifier|public
name|void
name|setContactEmail
parameter_list|(
name|String
name|contactEmail
parameter_list|)
block|{
name|this
operator|.
name|contactEmail
operator|=
name|contactEmail
expr_stmt|;
block|}
specifier|public
name|String
name|getContactUrl
parameter_list|()
block|{
return|return
name|contactUrl
return|;
block|}
specifier|public
name|void
name|setContactUrl
parameter_list|(
name|String
name|contactUrl
parameter_list|)
block|{
name|this
operator|.
name|contactUrl
operator|=
name|contactUrl
expr_stmt|;
block|}
specifier|public
name|String
name|getLicense
parameter_list|()
block|{
return|return
name|license
return|;
block|}
specifier|public
name|void
name|setLicense
parameter_list|(
name|String
name|license
parameter_list|)
block|{
name|this
operator|.
name|license
operator|=
name|license
expr_stmt|;
block|}
specifier|public
name|String
name|getLicenseUrl
parameter_list|()
block|{
return|return
name|licenseUrl
return|;
block|}
specifier|public
name|void
name|setLicenseUrl
parameter_list|(
name|String
name|licenseUrl
parameter_list|)
block|{
name|this
operator|.
name|licenseUrl
operator|=
name|licenseUrl
expr_stmt|;
block|}
specifier|public
name|String
name|getTermsOfServiceUrl
parameter_list|()
block|{
return|return
name|termsOfServiceUrl
return|;
block|}
specifier|public
name|void
name|setTermsOfServiceUrl
parameter_list|(
name|String
name|termsOfServiceUrl
parameter_list|)
block|{
name|this
operator|.
name|termsOfServiceUrl
operator|=
name|termsOfServiceUrl
expr_stmt|;
block|}
specifier|public
name|boolean
name|isReadAllResources
parameter_list|()
block|{
return|return
name|readAllResources
return|;
block|}
specifier|public
name|void
name|setReadAllResources
parameter_list|(
name|boolean
name|readAllResources
parameter_list|)
block|{
name|this
operator|.
name|readAllResources
operator|=
name|readAllResources
expr_stmt|;
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getResourceClasses
parameter_list|()
block|{
return|return
name|resourceClasses
return|;
block|}
specifier|public
name|void
name|setResourceClasses
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|resourceClasses
parameter_list|)
block|{
name|this
operator|.
name|resourceClasses
operator|=
operator|(
name|resourceClasses
operator|==
literal|null
operator|)
condition|?
literal|null
else|:
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|resourceClasses
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getIgnoredRoutes
parameter_list|()
block|{
return|return
name|ignoredRoutes
return|;
block|}
specifier|public
name|void
name|setIgnoredRoutes
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|ignoredRoutes
parameter_list|)
block|{
name|this
operator|.
name|ignoredRoutes
operator|=
operator|(
name|ignoredRoutes
operator|==
literal|null
operator|)
condition|?
literal|null
else|:
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|ignoredRoutes
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isPrettyPrint
parameter_list|()
block|{
return|return
name|prettyPrint
return|;
block|}
specifier|public
name|void
name|setPrettyPrint
parameter_list|(
name|boolean
name|prettyPrint
parameter_list|)
block|{
name|this
operator|.
name|prettyPrint
operator|=
name|prettyPrint
expr_stmt|;
block|}
specifier|public
name|boolean
name|isRunAsFilter
parameter_list|()
block|{
return|return
name|runAsFilter
return|;
block|}
annotation|@
name|Override
specifier|public
name|Boolean
name|isSupportSwaggerUi
parameter_list|()
block|{
return|return
name|supportSwaggerUi
return|;
block|}
specifier|public
name|void
name|setSupportSwaggerUi
parameter_list|(
name|Boolean
name|supportSwaggerUi
parameter_list|)
block|{
name|this
operator|.
name|supportSwaggerUi
operator|=
name|supportSwaggerUi
expr_stmt|;
block|}
specifier|public
name|String
name|getSwaggerUiVersion
parameter_list|()
block|{
return|return
name|swaggerUiVersion
return|;
block|}
specifier|public
name|void
name|setSwaggerUiVersion
parameter_list|(
name|String
name|swaggerUiVersion
parameter_list|)
block|{
name|this
operator|.
name|swaggerUiVersion
operator|=
name|swaggerUiVersion
expr_stmt|;
block|}
specifier|public
name|String
name|getSwaggerUiMavenGroupAndArtifact
parameter_list|()
block|{
return|return
name|swaggerUiMavenGroupAndArtifact
return|;
block|}
specifier|public
name|void
name|setSwaggerUiMavenGroupAndArtifact
parameter_list|(
name|String
name|swaggerUiMavenGroupAndArtifact
parameter_list|)
block|{
name|this
operator|.
name|swaggerUiMavenGroupAndArtifact
operator|=
name|swaggerUiMavenGroupAndArtifact
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getSwaggerUiMediaTypes
parameter_list|()
block|{
return|return
name|swaggerUiMediaTypes
return|;
block|}
specifier|public
name|void
name|setSwaggerUiMediaTypes
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|swaggerUiMediaTypes
parameter_list|)
block|{
name|this
operator|.
name|swaggerUiMediaTypes
operator|=
name|swaggerUiMediaTypes
expr_stmt|;
block|}
specifier|public
name|String
name|getConfigLocation
parameter_list|()
block|{
return|return
name|configLocation
return|;
block|}
specifier|public
name|void
name|setConfigLocation
parameter_list|(
name|String
name|configLocation
parameter_list|)
block|{
name|this
operator|.
name|configLocation
operator|=
name|configLocation
expr_stmt|;
block|}
specifier|public
name|String
name|getPropertiesLocation
parameter_list|()
block|{
return|return
name|propertiesLocation
return|;
block|}
specifier|public
name|void
name|setPropertiesLocation
parameter_list|(
name|String
name|propertiesLocation
parameter_list|)
block|{
name|this
operator|.
name|propertiesLocation
operator|=
name|propertiesLocation
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|findSwaggerUiRoot
parameter_list|()
block|{
return|return
name|SwaggerUi
operator|.
name|findSwaggerUiRoot
argument_list|(
name|swaggerUiMavenGroupAndArtifact
argument_list|,
name|swaggerUiVersion
argument_list|)
return|;
block|}
specifier|protected
name|Properties
name|getUserProperties
parameter_list|(
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|userDefinedOptions
parameter_list|)
block|{
specifier|final
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
if|if
condition|(
name|userDefinedOptions
operator|!=
literal|null
condition|)
block|{
name|userDefinedOptions
operator|.
name|entrySet
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|entry
lambda|->
name|entry
operator|.
name|getValue
argument_list|()
operator|!=
literal|null
argument_list|)
operator|.
name|forEach
argument_list|(
name|entry
lambda|->
name|properties
operator|.
name|setProperty
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|properties
return|;
block|}
specifier|protected
name|void
name|registerOpenApiResources
parameter_list|(
name|JAXRSServiceFactoryBean
name|sfb
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|packages
parameter_list|,
name|OpenAPIConfiguration
name|config
parameter_list|)
block|{
name|sfb
operator|.
name|setResourceClassesFromBeans
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|OpenApiResource
argument_list|()
operator|.
name|openApiConfiguration
argument_list|(
name|config
argument_list|)
operator|.
name|configLocation
argument_list|(
name|configLocation
argument_list|)
operator|.
name|resourcePackages
argument_list|(
name|packages
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|registerSwaggerUiResources
parameter_list|(
name|JAXRSServiceFactoryBean
name|sfb
parameter_list|,
name|Properties
name|properties
parameter_list|,
name|ServerProviderFactory
name|factory
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
specifier|final
name|Registration
name|swaggerUiRegistration
init|=
name|getSwaggerUi
argument_list|(
name|bus
argument_list|,
name|properties
argument_list|,
name|isRunAsFilter
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|isRunAsFilter
argument_list|()
condition|)
block|{
name|sfb
operator|.
name|setResourceClassesFromBeans
argument_list|(
name|swaggerUiRegistration
operator|.
name|getResources
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|factory
operator|.
name|setUserProviders
argument_list|(
name|swaggerUiRegistration
operator|.
name|getProviders
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Detects the application (if present) or creates the default application (in case the      * scan is disabled)      */
specifier|protected
name|Application
name|getApplicationOrDefault
parameter_list|(
name|Server
name|server
parameter_list|,
name|ServerProviderFactory
name|factory
parameter_list|,
name|JAXRSServiceFactoryBean
name|sfb
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|ApplicationInfo
name|appInfo
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|isScan
argument_list|()
condition|)
block|{
name|appInfo
operator|=
name|factory
operator|.
name|getApplicationProvider
argument_list|()
expr_stmt|;
if|if
condition|(
name|appInfo
operator|==
literal|null
condition|)
block|{
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|serviceClasses
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|ClassResourceInfo
name|cri
range|:
name|sfb
operator|.
name|getClassResourceInfo
argument_list|()
control|)
block|{
name|serviceClasses
operator|.
name|add
argument_list|(
name|cri
operator|.
name|getServiceClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|appInfo
operator|=
operator|new
name|ApplicationInfo
argument_list|(
operator|new
name|DefaultApplication
argument_list|(
name|serviceClasses
argument_list|)
argument_list|,
name|bus
argument_list|)
expr_stmt|;
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|put
argument_list|(
name|Application
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|appInfo
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|(
name|appInfo
operator|==
literal|null
operator|)
condition|?
literal|null
else|:
name|appInfo
operator|.
name|getProvider
argument_list|()
return|;
block|}
comment|/**      * The info will be used only if there is no @OpenAPIDefinition annotation is present.       */
specifier|private
name|Info
name|getInfo
parameter_list|(
specifier|final
name|Properties
name|properties
parameter_list|)
block|{
specifier|final
name|Info
name|info
init|=
operator|new
name|Info
argument_list|()
operator|.
name|title
argument_list|(
name|getOrFallback
argument_list|(
name|getTitle
argument_list|()
argument_list|,
name|properties
argument_list|,
name|TITLE_PROPERTY
argument_list|)
argument_list|)
operator|.
name|version
argument_list|(
name|getOrFallback
argument_list|(
name|getVersion
argument_list|()
argument_list|,
name|properties
argument_list|,
name|VERSION_PROPERTY
argument_list|)
argument_list|)
operator|.
name|description
argument_list|(
name|getOrFallback
argument_list|(
name|getDescription
argument_list|()
argument_list|,
name|properties
argument_list|,
name|DESCRIPTION_PROPERTY
argument_list|)
argument_list|)
operator|.
name|termsOfService
argument_list|(
name|getOrFallback
argument_list|(
name|getTermsOfServiceUrl
argument_list|()
argument_list|,
name|properties
argument_list|,
name|TERMS_URL_PROPERTY
argument_list|)
argument_list|)
operator|.
name|contact
argument_list|(
operator|new
name|Contact
argument_list|()
operator|.
name|name
argument_list|(
name|getOrFallback
argument_list|(
name|getContactName
argument_list|()
argument_list|,
name|properties
argument_list|,
name|CONTACT_PROPERTY
argument_list|)
argument_list|)
operator|.
name|email
argument_list|(
name|getContactEmail
argument_list|()
argument_list|)
operator|.
name|url
argument_list|(
name|getContactUrl
argument_list|()
argument_list|)
argument_list|)
operator|.
name|license
argument_list|(
operator|new
name|License
argument_list|()
operator|.
name|name
argument_list|(
name|getOrFallback
argument_list|(
name|getLicense
argument_list|()
argument_list|,
name|properties
argument_list|,
name|LICENSE_PROPERTY
argument_list|)
argument_list|)
operator|.
name|url
argument_list|(
name|getOrFallback
argument_list|(
name|getLicenseUrl
argument_list|()
argument_list|,
name|properties
argument_list|,
name|LICENSE_URL_PROPERTY
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|info
operator|.
name|getLicense
argument_list|()
operator|.
name|getName
argument_list|()
operator|==
literal|null
condition|)
block|{
name|info
operator|.
name|getLicense
argument_list|()
operator|.
name|setName
argument_list|(
name|DEFAULT_LICENSE_VALUE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|info
operator|.
name|getLicense
argument_list|()
operator|.
name|getUrl
argument_list|()
operator|==
literal|null
operator|&&
name|DEFAULT_LICENSE_VALUE
operator|.
name|equals
argument_list|(
name|info
operator|.
name|getLicense
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|info
operator|.
name|getLicense
argument_list|()
operator|.
name|setUrl
argument_list|(
name|DEFAULT_LICENSE_URL
argument_list|)
expr_stmt|;
block|}
return|return
name|info
return|;
block|}
specifier|private
name|String
name|getOrFallback
parameter_list|(
name|String
name|value
parameter_list|,
name|Properties
name|properties
parameter_list|,
name|String
name|property
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
operator|&&
name|properties
operator|!=
literal|null
condition|)
block|{
return|return
name|properties
operator|.
name|getProperty
argument_list|(
name|property
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|value
return|;
block|}
block|}
specifier|private
name|Boolean
name|getOrFallback
parameter_list|(
name|Boolean
name|value
parameter_list|,
name|Properties
name|properties
parameter_list|,
name|String
name|property
parameter_list|)
block|{
name|Boolean
name|fallback
init|=
name|value
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
operator|&&
name|properties
operator|!=
literal|null
condition|)
block|{
name|fallback
operator|=
name|PropertyUtils
operator|.
name|isTrue
argument_list|(
name|properties
operator|.
name|get
argument_list|(
name|PRETTY_PRINT_PROPERTY
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|fallback
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|fallback
return|;
block|}
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|getOrFallback
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|collection
parameter_list|,
name|Properties
name|properties
parameter_list|,
name|String
name|property
parameter_list|)
block|{
if|if
condition|(
name|collection
operator|.
name|isEmpty
argument_list|()
operator|&&
name|properties
operator|!=
literal|null
condition|)
block|{
specifier|final
name|String
name|value
init|=
name|properties
operator|.
name|getProperty
argument_list|(
name|property
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|collection
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|collection
return|;
block|}
specifier|private
name|Collection
argument_list|<
name|String
argument_list|>
name|scanResourcePackages
parameter_list|(
name|JAXRSServiceFactoryBean
name|sfb
parameter_list|)
block|{
return|return
name|sfb
operator|.
name|getClassResourceInfo
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|cri
lambda|->
name|cri
operator|.
name|getServiceClass
argument_list|()
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toSet
argument_list|()
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|Properties
name|combine
parameter_list|(
specifier|final
name|Properties
name|primary
parameter_list|,
specifier|final
name|Properties
name|secondary
parameter_list|)
block|{
if|if
condition|(
name|primary
operator|==
literal|null
condition|)
block|{
return|return
name|secondary
return|;
block|}
elseif|else
if|if
condition|(
name|secondary
operator|==
literal|null
condition|)
block|{
return|return
name|primary
return|;
block|}
else|else
block|{
specifier|final
name|Properties
name|combined
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|setOrReplace
argument_list|(
name|secondary
argument_list|,
name|combined
argument_list|)
expr_stmt|;
name|setOrReplace
argument_list|(
name|primary
argument_list|,
name|combined
argument_list|)
expr_stmt|;
return|return
name|combined
return|;
block|}
block|}
specifier|private
specifier|static
name|void
name|setOrReplace
parameter_list|(
specifier|final
name|Properties
name|source
parameter_list|,
specifier|final
name|Properties
name|destination
parameter_list|)
block|{
specifier|final
name|Enumeration
argument_list|<
name|?
argument_list|>
name|enumeration
init|=
name|source
operator|.
name|propertyNames
argument_list|()
decl_stmt|;
while|while
condition|(
name|enumeration
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
specifier|final
name|String
name|name
init|=
operator|(
name|String
operator|)
name|enumeration
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|destination
operator|.
name|setProperty
argument_list|(
name|name
argument_list|,
name|source
operator|.
name|getProperty
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

