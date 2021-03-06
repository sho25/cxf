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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
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

begin_comment
comment|/**  * Please refer to https://github.com/swagger-api/swagger-ui/blob/master/docs/usage/configuration.md  * to get the idea what each parameter does.  */
end_comment

begin_class
specifier|public
class|class
name|SwaggerUiConfig
block|{
comment|// URL to fetch external configuration document from.
specifier|private
name|String
name|configUrl
decl_stmt|;
comment|// The url pointing to API definition (normally
comment|// swagger.json/swagger.yaml/openapi.json/openapi.yaml).
specifier|private
name|String
name|url
decl_stmt|;
comment|// If set, enables filtering. The top bar will show an edit box that
comment|// could be used to filter the tagged operations that are shown.
specifier|private
name|String
name|filter
decl_stmt|;
comment|// Enables or disables deep linking for tags and operations.
specifier|private
name|Boolean
name|deepLinking
decl_stmt|;
comment|//  Controls the display of operationId in operations list.
specifier|private
name|Boolean
name|displayOperationId
decl_stmt|;
comment|// The default expansion depth for models (set to -1 completely hide the models).
specifier|private
name|Integer
name|defaultModelsExpandDepth
decl_stmt|;
comment|// The default expansion depth for the model on the model-example section.
specifier|private
name|Integer
name|defaultModelExpandDepth
decl_stmt|;
comment|// Controls how the model is shown when the API is first rendered.
specifier|private
name|String
name|defaultModelRendering
decl_stmt|;
comment|// Controls the display of the request duration (in milliseconds) for Try-It-Out requests.
specifier|private
name|Boolean
name|displayRequestDuration
decl_stmt|;
comment|// Controls the default expansion setting for the operations and tags.
specifier|private
name|String
name|docExpansion
decl_stmt|;
comment|//  If set, limits the number of tagged operations displayed to at most this many.
specifier|private
name|Integer
name|maxDisplayedTags
decl_stmt|;
comment|// Controls the display of vendor extension (x-) fields and values.
specifier|private
name|Boolean
name|showExtensions
decl_stmt|;
comment|// Controls the display of extensions
specifier|private
name|Boolean
name|showCommonExtensions
decl_stmt|;
comment|// Set a different validator URL, for example for locally deployed validators
specifier|private
name|String
name|validatorUrl
decl_stmt|;
specifier|public
name|String
name|getConfigUrl
parameter_list|()
block|{
return|return
name|configUrl
return|;
block|}
specifier|public
name|void
name|setConfigUrl
parameter_list|(
specifier|final
name|String
name|configUrl
parameter_list|)
block|{
name|this
operator|.
name|configUrl
operator|=
name|configUrl
expr_stmt|;
block|}
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
name|url
return|;
block|}
specifier|public
name|void
name|setUrl
parameter_list|(
specifier|final
name|String
name|url
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
block|}
specifier|public
name|String
name|getFilter
parameter_list|()
block|{
return|return
name|filter
return|;
block|}
specifier|public
name|void
name|setFilter
parameter_list|(
specifier|final
name|String
name|filter
parameter_list|)
block|{
name|this
operator|.
name|filter
operator|=
name|filter
expr_stmt|;
block|}
specifier|public
name|Boolean
name|getShowCommonExtensions
parameter_list|()
block|{
return|return
name|showCommonExtensions
return|;
block|}
specifier|public
name|void
name|setShowCommonExtensions
parameter_list|(
name|Boolean
name|showCommonExtensions
parameter_list|)
block|{
name|this
operator|.
name|showCommonExtensions
operator|=
name|showCommonExtensions
expr_stmt|;
block|}
specifier|public
name|Boolean
name|getShowExtensions
parameter_list|()
block|{
return|return
name|showExtensions
return|;
block|}
specifier|public
name|Integer
name|getMaxDisplayedTags
parameter_list|()
block|{
return|return
name|maxDisplayedTags
return|;
block|}
specifier|public
name|void
name|setMaxDisplayedTags
parameter_list|(
name|Integer
name|maxDisplayedTags
parameter_list|)
block|{
name|this
operator|.
name|maxDisplayedTags
operator|=
name|maxDisplayedTags
expr_stmt|;
block|}
specifier|public
name|SwaggerUiConfig
name|maxDisplayedTags
parameter_list|(
name|Integer
name|value
parameter_list|)
block|{
name|setMaxDisplayedTags
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|void
name|setShowExtensions
parameter_list|(
name|Boolean
name|showExtensions
parameter_list|)
block|{
name|this
operator|.
name|showExtensions
operator|=
name|showExtensions
expr_stmt|;
block|}
specifier|public
name|String
name|getDocExpansion
parameter_list|()
block|{
return|return
name|docExpansion
return|;
block|}
specifier|public
name|void
name|setDocExpansion
parameter_list|(
name|String
name|docExpansion
parameter_list|)
block|{
name|this
operator|.
name|docExpansion
operator|=
name|docExpansion
expr_stmt|;
block|}
specifier|public
name|Boolean
name|getDisplayRequestDuration
parameter_list|()
block|{
return|return
name|displayRequestDuration
return|;
block|}
specifier|public
name|void
name|setDisplayRequestDuration
parameter_list|(
name|Boolean
name|displayRequestDuration
parameter_list|)
block|{
name|this
operator|.
name|displayRequestDuration
operator|=
name|displayRequestDuration
expr_stmt|;
block|}
specifier|public
name|String
name|getDefaultModelRendering
parameter_list|()
block|{
return|return
name|defaultModelRendering
return|;
block|}
specifier|public
name|void
name|setDefaultModelRendering
parameter_list|(
name|String
name|defaultModelRendering
parameter_list|)
block|{
name|this
operator|.
name|defaultModelRendering
operator|=
name|defaultModelRendering
expr_stmt|;
block|}
specifier|public
name|Integer
name|getDefaultModelExpandDepth
parameter_list|()
block|{
return|return
name|defaultModelExpandDepth
return|;
block|}
specifier|public
name|void
name|setDefaultModelExpandDepth
parameter_list|(
name|Integer
name|defaultModelExpandDepth
parameter_list|)
block|{
name|this
operator|.
name|defaultModelExpandDepth
operator|=
name|defaultModelExpandDepth
expr_stmt|;
block|}
specifier|public
name|Integer
name|getDefaultModelsExpandDepth
parameter_list|()
block|{
return|return
name|defaultModelsExpandDepth
return|;
block|}
specifier|public
name|void
name|setDefaultModelsExpandDepth
parameter_list|(
name|Integer
name|defaultModelsExpandDepth
parameter_list|)
block|{
name|this
operator|.
name|defaultModelsExpandDepth
operator|=
name|defaultModelsExpandDepth
expr_stmt|;
block|}
specifier|public
name|Boolean
name|getDisplayOperationId
parameter_list|()
block|{
return|return
name|displayOperationId
return|;
block|}
specifier|public
name|void
name|setDisplayOperationId
parameter_list|(
name|Boolean
name|displayOperationId
parameter_list|)
block|{
name|this
operator|.
name|displayOperationId
operator|=
name|displayOperationId
expr_stmt|;
block|}
specifier|public
name|Boolean
name|getDeepLinking
parameter_list|()
block|{
return|return
name|deepLinking
return|;
block|}
specifier|public
name|void
name|setDeepLinking
parameter_list|(
name|Boolean
name|deepLinking
parameter_list|)
block|{
name|this
operator|.
name|deepLinking
operator|=
name|deepLinking
expr_stmt|;
block|}
specifier|public
name|String
name|getValidatorUrl
parameter_list|()
block|{
return|return
name|validatorUrl
return|;
block|}
specifier|public
name|void
name|setValidatorUrl
parameter_list|(
name|String
name|validatorUrl
parameter_list|)
block|{
name|this
operator|.
name|validatorUrl
operator|=
name|validatorUrl
expr_stmt|;
block|}
specifier|public
name|SwaggerUiConfig
name|validatorUrl
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|setValidatorUrl
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|SwaggerUiConfig
name|deepLinking
parameter_list|(
name|Boolean
name|value
parameter_list|)
block|{
name|setDeepLinking
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|SwaggerUiConfig
name|displayOperationId
parameter_list|(
name|Boolean
name|value
parameter_list|)
block|{
name|setDisplayOperationId
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|SwaggerUiConfig
name|defaultModelsExpandDepth
parameter_list|(
name|Integer
name|value
parameter_list|)
block|{
name|setDefaultModelsExpandDepth
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|SwaggerUiConfig
name|defaultModelExpandDepth
parameter_list|(
name|Integer
name|value
parameter_list|)
block|{
name|setDefaultModelExpandDepth
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|SwaggerUiConfig
name|defaultModelRendering
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|setDefaultModelRendering
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|SwaggerUiConfig
name|displayRequestDuration
parameter_list|(
name|Boolean
name|value
parameter_list|)
block|{
name|setDisplayRequestDuration
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|SwaggerUiConfig
name|docExpansion
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|setDocExpansion
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|SwaggerUiConfig
name|showExtensions
parameter_list|(
name|Boolean
name|value
parameter_list|)
block|{
name|setShowExtensions
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|SwaggerUiConfig
name|showCommonExtensions
parameter_list|(
name|Boolean
name|value
parameter_list|)
block|{
name|setShowCommonExtensions
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|SwaggerUiConfig
name|url
parameter_list|(
specifier|final
name|String
name|u
parameter_list|)
block|{
name|setUrl
argument_list|(
name|u
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|SwaggerUiConfig
name|configUrl
parameter_list|(
specifier|final
name|String
name|cu
parameter_list|)
block|{
name|setConfigUrl
argument_list|(
name|cu
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|SwaggerUiConfig
name|filter
parameter_list|(
specifier|final
name|String
name|f
parameter_list|)
block|{
name|setFilter
argument_list|(
name|f
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getConfigParameters
parameter_list|()
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|()
decl_stmt|;
name|put
argument_list|(
literal|"url"
argument_list|,
name|getUrl
argument_list|()
argument_list|,
name|params
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|"configUrl"
argument_list|,
name|getConfigUrl
argument_list|()
argument_list|,
name|params
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|"filter"
argument_list|,
name|getFilter
argument_list|()
argument_list|,
name|params
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|"deepLinking"
argument_list|,
name|getDeepLinking
argument_list|()
argument_list|,
name|params
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|"displayOperationId"
argument_list|,
name|getDisplayOperationId
argument_list|()
argument_list|,
name|params
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|"defaultModelsExpandDepth"
argument_list|,
name|getDefaultModelsExpandDepth
argument_list|()
argument_list|,
name|params
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|"defaultModelExpandDepth"
argument_list|,
name|getDefaultModelExpandDepth
argument_list|()
argument_list|,
name|params
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|"defaultModelRendering"
argument_list|,
name|getDefaultModelRendering
argument_list|()
argument_list|,
name|params
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|"displayRequestDuration"
argument_list|,
name|getDisplayRequestDuration
argument_list|()
argument_list|,
name|params
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|"docExpansion"
argument_list|,
name|getDocExpansion
argument_list|()
argument_list|,
name|params
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|"maxDisplayedTags"
argument_list|,
name|getMaxDisplayedTags
argument_list|()
argument_list|,
name|params
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|"showExtensions"
argument_list|,
name|getShowExtensions
argument_list|()
argument_list|,
name|params
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|"showCommonExtensions"
argument_list|,
name|getShowCommonExtensions
argument_list|()
argument_list|,
name|params
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|"validatorUrl"
argument_list|,
name|getValidatorUrl
argument_list|()
argument_list|,
name|params
argument_list|)
expr_stmt|;
return|return
name|params
return|;
block|}
specifier|protected
specifier|static
name|void
name|put
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|Integer
name|value
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
block|{
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|params
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
specifier|static
name|void
name|put
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|Boolean
name|value
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
block|{
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|params
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
specifier|static
name|void
name|put
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|String
name|value
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
block|{
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
name|params
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

