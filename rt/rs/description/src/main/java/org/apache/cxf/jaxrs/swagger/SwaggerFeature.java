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
name|swagger
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
name|util
operator|.
name|ArrayList
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
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|container
operator|.
name|ContainerRequestContext
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
name|container
operator|.
name|ContainerRequestFilter
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
name|container
operator|.
name|PreMatching
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
name|Context
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
name|UriInfo
import|;
end_import

begin_import
import|import
name|com
operator|.
name|wordnik
operator|.
name|swagger
operator|.
name|jaxrs
operator|.
name|config
operator|.
name|BeanConfig
import|;
end_import

begin_import
import|import
name|com
operator|.
name|wordnik
operator|.
name|swagger
operator|.
name|jaxrs
operator|.
name|listing
operator|.
name|ApiDeclarationProvider
import|;
end_import

begin_import
import|import
name|com
operator|.
name|wordnik
operator|.
name|swagger
operator|.
name|jaxrs
operator|.
name|listing
operator|.
name|ApiListingResourceJSON
import|;
end_import

begin_import
import|import
name|com
operator|.
name|wordnik
operator|.
name|swagger
operator|.
name|jaxrs
operator|.
name|listing
operator|.
name|ResourceListingProvider
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
name|ext
operator|.
name|MessageContext
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

begin_class
specifier|public
class|class
name|SwaggerFeature
extends|extends
name|AbstractSwaggerFeature
block|{
annotation|@
name|Override
specifier|protected
name|void
name|addSwaggerResource
parameter_list|(
name|Server
name|server
parameter_list|)
block|{
name|ApiListingResourceJSON
name|apiListingResource
init|=
operator|new
name|ApiListingResourceJSON
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|runAsFilter
condition|)
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|serviceBeans
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|serviceBeans
operator|.
name|add
argument_list|(
name|apiListingResource
argument_list|)
expr_stmt|;
operator|(
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
operator|)
operator|.
name|setResourceClassesFromBeans
argument_list|(
name|serviceBeans
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|Object
argument_list|>
name|providers
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|runAsFilter
condition|)
block|{
name|providers
operator|.
name|add
argument_list|(
operator|new
name|SwaggerContainerRequestFilter
argument_list|(
name|apiListingResource
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|providers
operator|.
name|add
argument_list|(
operator|new
name|ResourceListingProvider
argument_list|()
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|ApiDeclarationProvider
argument_list|()
argument_list|)
expr_stmt|;
operator|(
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
operator|)
operator|.
name|setUserProviders
argument_list|(
name|providers
argument_list|)
expr_stmt|;
name|BeanConfig
name|beanConfig
init|=
operator|new
name|BeanConfig
argument_list|()
decl_stmt|;
name|beanConfig
operator|.
name|setResourcePackage
argument_list|(
name|getResourcePackage
argument_list|()
argument_list|)
expr_stmt|;
name|beanConfig
operator|.
name|setVersion
argument_list|(
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|beanConfig
operator|.
name|setBasePath
argument_list|(
name|getBasePath
argument_list|()
argument_list|)
expr_stmt|;
name|beanConfig
operator|.
name|setTitle
argument_list|(
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
name|beanConfig
operator|.
name|setDescription
argument_list|(
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|beanConfig
operator|.
name|setContact
argument_list|(
name|getContact
argument_list|()
argument_list|)
expr_stmt|;
name|beanConfig
operator|.
name|setLicense
argument_list|(
name|getLicense
argument_list|()
argument_list|)
expr_stmt|;
name|beanConfig
operator|.
name|setLicenseUrl
argument_list|(
name|getLicenseUrl
argument_list|()
argument_list|)
expr_stmt|;
name|beanConfig
operator|.
name|setTermsOfServiceUrl
argument_list|(
name|getTermOfServiceUrl
argument_list|()
argument_list|)
expr_stmt|;
name|beanConfig
operator|.
name|setScan
argument_list|(
name|isScan
argument_list|()
argument_list|)
expr_stmt|;
name|beanConfig
operator|.
name|setFilterClass
argument_list|(
name|getFilterClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|setBasePathByAddress
parameter_list|(
name|String
name|address
parameter_list|)
block|{
name|setBasePath
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
annotation|@
name|PreMatching
specifier|private
specifier|static
class|class
name|SwaggerContainerRequestFilter
implements|implements
name|ContainerRequestFilter
block|{
specifier|private
specifier|static
specifier|final
name|String
name|APIDOCS_LISTING_PATH
init|=
literal|"api-docs"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Pattern
name|APIDOCS_RESOURCE_PATH
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|APIDOCS_LISTING_PATH
operator|+
literal|"(/.+)"
argument_list|)
decl_stmt|;
specifier|private
name|ApiListingResourceJSON
name|apiListingResource
decl_stmt|;
annotation|@
name|Context
specifier|private
name|MessageContext
name|mc
decl_stmt|;
specifier|public
name|SwaggerContainerRequestFilter
parameter_list|(
name|ApiListingResourceJSON
name|apiListingResource
parameter_list|)
block|{
name|this
operator|.
name|apiListingResource
operator|=
name|apiListingResource
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ContainerRequestContext
name|requestContext
parameter_list|)
throws|throws
name|IOException
block|{
name|UriInfo
name|ui
init|=
name|mc
operator|.
name|getUriInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|ui
operator|.
name|getPath
argument_list|()
operator|.
name|endsWith
argument_list|(
name|APIDOCS_LISTING_PATH
argument_list|)
condition|)
block|{
name|Response
name|r
init|=
name|apiListingResource
operator|.
name|resourceListing
argument_list|(
literal|null
argument_list|,
name|mc
operator|.
name|getServletConfig
argument_list|()
argument_list|,
name|mc
operator|.
name|getHttpHeaders
argument_list|()
argument_list|,
name|ui
argument_list|)
decl_stmt|;
name|requestContext
operator|.
name|abortWith
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
else|else
block|{
specifier|final
name|Matcher
name|matcher
init|=
name|APIDOCS_RESOURCE_PATH
operator|.
name|matcher
argument_list|(
name|ui
operator|.
name|getPath
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|matcher
operator|.
name|find
argument_list|()
condition|)
block|{
name|Response
name|r
init|=
name|apiListingResource
operator|.
name|apiDeclaration
argument_list|(
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
argument_list|,
literal|null
argument_list|,
name|mc
operator|.
name|getServletConfig
argument_list|()
argument_list|,
name|mc
operator|.
name|getHttpHeaders
argument_list|()
argument_list|,
name|ui
argument_list|)
decl_stmt|;
name|requestContext
operator|.
name|abortWith
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

