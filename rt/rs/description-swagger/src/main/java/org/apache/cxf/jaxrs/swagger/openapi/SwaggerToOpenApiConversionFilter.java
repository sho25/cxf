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
name|openapi
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
name|net
operator|.
name|URI
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
name|ContainerResponseContext
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
name|ContainerResponseFilter
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
name|ext
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
name|utils
operator|.
name|JAXRSUtils
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|util
operator|.
name|Json
import|;
end_import

begin_class
annotation|@
name|Provider
annotation|@
name|PreMatching
specifier|public
specifier|final
class|class
name|SwaggerToOpenApiConversionFilter
implements|implements
name|ContainerRequestFilter
implements|,
name|ContainerResponseFilter
block|{
specifier|private
specifier|static
specifier|final
name|String
name|SWAGGER_PATH
init|=
literal|"swagger.json"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|OPEN_API_PATH
init|=
literal|"openapi.json"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|OPEN_API_PROPERTY
init|=
literal|"openapi"
decl_stmt|;
specifier|private
name|OpenApiConfiguration
name|openApiConfig
decl_stmt|;
specifier|private
name|String
name|openApiJsonPath
init|=
name|OPEN_API_PATH
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ContainerRequestContext
name|reqCtx
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|path
init|=
name|reqCtx
operator|.
name|getUriInfo
argument_list|()
operator|.
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|endsWith
argument_list|(
name|openApiJsonPath
argument_list|)
condition|)
block|{
name|reqCtx
operator|.
name|setRequestUri
argument_list|(
name|URI
operator|.
name|create
argument_list|(
name|SWAGGER_PATH
argument_list|)
argument_list|)
expr_stmt|;
name|reqCtx
operator|.
name|setProperty
argument_list|(
name|OPEN_API_PROPERTY
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ContainerRequestContext
name|reqCtx
parameter_list|,
name|ContainerResponseContext
name|respCtx
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|Boolean
operator|.
name|TRUE
operator|==
name|reqCtx
operator|.
name|getProperty
argument_list|(
name|OPEN_API_PROPERTY
argument_list|)
condition|)
block|{
name|String
name|swaggerJson
init|=
name|respCtx
operator|.
name|getEntity
argument_list|()
operator|instanceof
name|String
condition|?
operator|(
name|String
operator|)
name|respCtx
operator|.
name|getEntity
argument_list|()
else|:
name|Json
operator|.
name|pretty
argument_list|(
name|respCtx
operator|.
name|getEntity
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|openApiJson
init|=
name|SwaggerToOpenApiConversionUtils
operator|.
name|getOpenApiFromSwaggerJson
argument_list|(
name|createMessageContext
argument_list|()
argument_list|,
name|swaggerJson
argument_list|,
name|openApiConfig
argument_list|)
decl_stmt|;
name|respCtx
operator|.
name|setEntity
argument_list|(
name|openApiJson
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|MessageContext
name|createMessageContext
parameter_list|()
block|{
return|return
name|JAXRSUtils
operator|.
name|createContextValue
argument_list|(
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
argument_list|,
literal|null
argument_list|,
name|MessageContext
operator|.
name|class
argument_list|)
return|;
block|}
specifier|public
name|void
name|setOpenApiConfig
parameter_list|(
name|OpenApiConfiguration
name|openApiConfig
parameter_list|)
block|{
name|this
operator|.
name|openApiConfig
operator|=
name|openApiConfig
expr_stmt|;
block|}
specifier|public
name|void
name|setOpenApiJsonPath
parameter_list|(
name|String
name|openApiJsonPath
parameter_list|)
block|{
name|this
operator|.
name|openApiJsonPath
operator|=
name|openApiJsonPath
expr_stmt|;
block|}
block|}
end_class

end_unit

