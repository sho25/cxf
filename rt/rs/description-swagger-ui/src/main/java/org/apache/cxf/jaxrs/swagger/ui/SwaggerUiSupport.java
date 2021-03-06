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
operator|.
name|ui
package|;
end_package

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
name|common
operator|.
name|util
operator|.
name|PropertyUtils
import|;
end_import

begin_comment
comment|/**  * Generic trait to support Swagger UI integration for Swagger 1.5.x and  * OpenAPI v3.x (Swagger 2.x) integrations.  */
end_comment

begin_interface
specifier|public
interface|interface
name|SwaggerUiSupport
block|{
name|String
name|SUPPORT_UI_PROPERTY
init|=
literal|"support.swagger.ui"
decl_stmt|;
comment|/**      * Holds the resources and/or providers which are required for      * Swagger UI integration to be plugged in.       */
class|class
name|Registration
block|{
specifier|private
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|resources
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|providers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|List
argument_list|<
name|Object
argument_list|>
name|getResources
parameter_list|()
block|{
return|return
name|resources
return|;
block|}
specifier|public
name|List
argument_list|<
name|Object
argument_list|>
name|getProviders
parameter_list|()
block|{
return|return
name|providers
return|;
block|}
block|}
comment|/**      * Detects the presence of Swagger UI in classpath with respect to properties and      * configuration provided.       * @param bus bus instance       * @param swaggerProps Swagger properties (usually externalized)       * @param runAsFilter "true" if Swagger integration is run as a filter, "false" otherwise.       * @return the Swagger UI registration      */
specifier|default
name|Registration
name|getSwaggerUi
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|Properties
name|swaggerProps
parameter_list|,
name|boolean
name|runAsFilter
parameter_list|)
block|{
specifier|final
name|Registration
name|registration
init|=
operator|new
name|Registration
argument_list|()
decl_stmt|;
if|if
condition|(
name|checkSupportSwaggerUiProp
argument_list|(
name|swaggerProps
argument_list|)
condition|)
block|{
name|String
name|swaggerUiRoot
init|=
name|findSwaggerUiRoot
argument_list|()
decl_stmt|;
if|if
condition|(
name|swaggerUiRoot
operator|!=
literal|null
condition|)
block|{
specifier|final
name|SwaggerUiResourceLocator
name|locator
init|=
operator|new
name|SwaggerUiResourceLocator
argument_list|(
name|swaggerUiRoot
argument_list|)
decl_stmt|;
name|SwaggerUiService
name|swaggerUiService
init|=
operator|new
name|SwaggerUiService
argument_list|(
name|locator
argument_list|,
name|getSwaggerUiMediaTypes
argument_list|()
argument_list|)
decl_stmt|;
name|swaggerUiService
operator|.
name|setConfig
argument_list|(
name|getSwaggerUiConfig
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|runAsFilter
condition|)
block|{
name|registration
operator|.
name|resources
operator|.
name|add
argument_list|(
name|swaggerUiService
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|registration
operator|.
name|providers
operator|.
name|add
argument_list|(
operator|new
name|SwaggerUiServiceFilter
argument_list|(
name|swaggerUiService
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|registration
operator|.
name|providers
operator|.
name|add
argument_list|(
operator|new
name|SwaggerUiResourceFilter
argument_list|(
name|locator
argument_list|)
argument_list|)
expr_stmt|;
name|bus
operator|.
name|setProperty
argument_list|(
literal|"swagger.service.ui.available"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|registration
return|;
block|}
comment|/**      * Checks the Swagger properties to determine if Swagger UI support is available or not.      * @param props Swagger properties (usually externalized)       * @return      */
specifier|default
name|boolean
name|checkSupportSwaggerUiProp
parameter_list|(
name|Properties
name|props
parameter_list|)
block|{
name|Boolean
name|theSupportSwaggerUI
init|=
name|isSupportSwaggerUi
argument_list|()
decl_stmt|;
if|if
condition|(
name|theSupportSwaggerUI
operator|==
literal|null
operator|&&
name|props
operator|!=
literal|null
operator|&&
name|props
operator|.
name|containsKey
argument_list|(
name|SUPPORT_UI_PROPERTY
argument_list|)
condition|)
block|{
name|theSupportSwaggerUI
operator|=
name|PropertyUtils
operator|.
name|isTrue
argument_list|(
name|props
operator|.
name|get
argument_list|(
name|SUPPORT_UI_PROPERTY
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|theSupportSwaggerUI
operator|==
literal|null
condition|)
block|{
name|theSupportSwaggerUI
operator|=
literal|true
expr_stmt|;
block|}
return|return
name|theSupportSwaggerUI
return|;
block|}
comment|/**      * Checks if Swagger UI support is available or not.      * @return "true" if Swagger UI support is available, "false" otherwise      */
name|Boolean
name|isSupportSwaggerUi
parameter_list|()
function_decl|;
comment|/**      * Detects the Swagger UI in root with respect to properties and configuration       * provided.       * @return Swagger UI in root or "null" if not available      */
name|String
name|findSwaggerUiRoot
parameter_list|()
function_decl|;
comment|/**      * Returns media types supported by Swagger UI      * @return media types supported by Swagger UI      */
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getSwaggerUiMediaTypes
parameter_list|()
function_decl|;
comment|/**      * Returns Swagger UI configuration parameters.      * @return Swagger UI configuration parameters or "null" if not available      */
name|SwaggerUiConfig
name|getSwaggerUiConfig
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

