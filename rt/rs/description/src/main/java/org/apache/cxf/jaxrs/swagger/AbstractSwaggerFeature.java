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
name|AbstractResourceInfo
import|;
end_import

begin_class
specifier|abstract
class|class
name|AbstractSwaggerFeature
extends|extends
name|AbstractFeature
block|{
specifier|protected
name|boolean
name|scan
init|=
literal|true
decl_stmt|;
specifier|protected
name|boolean
name|runAsFilter
decl_stmt|;
specifier|private
name|String
name|resourcePackage
decl_stmt|;
specifier|private
name|String
name|version
init|=
literal|"1.0.0"
decl_stmt|;
comment|// depending on swagger version basePath is set differently
specifier|private
name|String
name|basePath
decl_stmt|;
specifier|private
name|String
name|title
init|=
literal|"Sample REST Application"
decl_stmt|;
specifier|private
name|String
name|description
init|=
literal|"The Application"
decl_stmt|;
specifier|private
name|String
name|contact
init|=
literal|"committer@apache.org"
decl_stmt|;
specifier|private
name|String
name|license
init|=
literal|"Apache 2.0 License"
decl_stmt|;
specifier|private
name|String
name|licenseUrl
init|=
literal|"http://www.apache.org/licenses/LICENSE-2.0.html"
decl_stmt|;
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
name|calculateDefaultResourcePackage
argument_list|(
name|server
argument_list|)
expr_stmt|;
name|calculateDefaultBasePath
argument_list|(
name|server
argument_list|)
expr_stmt|;
name|addSwaggerResource
argument_list|(
name|server
argument_list|)
expr_stmt|;
name|initializeProvider
argument_list|(
name|server
operator|.
name|getEndpoint
argument_list|()
argument_list|,
name|bus
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|abstract
name|void
name|addSwaggerResource
parameter_list|(
name|Server
name|server
parameter_list|)
function_decl|;
specifier|protected
specifier|abstract
name|void
name|setBasePathByAddress
parameter_list|(
name|String
name|address
parameter_list|)
function_decl|;
specifier|private
name|void
name|calculateDefaultResourcePackage
parameter_list|(
name|Server
name|server
parameter_list|)
block|{
name|JAXRSServiceFactoryBean
name|serviceFactoryBean
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
name|AbstractResourceInfo
name|resourceInfo
init|=
name|serviceFactoryBean
operator|.
name|getClassResourceInfo
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|resourceInfo
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|getResourcePackage
argument_list|()
operator|==
literal|null
operator|||
name|getResourcePackage
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
operator|)
condition|)
block|{
name|setResourcePackage
argument_list|(
name|resourceInfo
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
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|calculateDefaultBasePath
parameter_list|(
name|Server
name|server
parameter_list|)
block|{
if|if
condition|(
name|getBasePath
argument_list|()
operator|==
literal|null
operator|||
name|getBasePath
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|String
name|address
init|=
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getAddress
argument_list|()
decl_stmt|;
name|setBasePathByAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getResourcePackage
parameter_list|()
block|{
return|return
name|resourcePackage
return|;
block|}
specifier|public
name|void
name|setResourcePackage
parameter_list|(
name|String
name|resourcePackage
parameter_list|)
block|{
name|this
operator|.
name|resourcePackage
operator|=
name|resourcePackage
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
name|getBasePath
parameter_list|()
block|{
return|return
name|basePath
return|;
block|}
specifier|public
name|void
name|setBasePath
parameter_list|(
name|String
name|basePath
parameter_list|)
block|{
name|this
operator|.
name|basePath
operator|=
name|basePath
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
name|getContact
parameter_list|()
block|{
return|return
name|contact
return|;
block|}
specifier|public
name|void
name|setContact
parameter_list|(
name|String
name|contact
parameter_list|)
block|{
name|this
operator|.
name|contact
operator|=
name|contact
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
name|boolean
name|isRunAsFilter
parameter_list|()
block|{
return|return
name|runAsFilter
return|;
block|}
specifier|public
name|void
name|setRunAsFilter
parameter_list|(
name|boolean
name|runAsFilter
parameter_list|)
block|{
name|this
operator|.
name|runAsFilter
operator|=
name|runAsFilter
expr_stmt|;
block|}
block|}
end_class

end_unit

