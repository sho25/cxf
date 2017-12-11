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
name|io
operator|.
name|swagger
operator|.
name|annotations
operator|.
name|Api
import|;
end_import

begin_comment
comment|/**  * SwaggerUI resolvers implementation for Swagger 1.5.x  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|SwaggerUi
block|{
specifier|private
specifier|static
specifier|final
name|SwaggerUiResolver
name|HELPER
decl_stmt|;
static|static
block|{
name|SwaggerUiResolver
name|theHelper
init|=
literal|null
decl_stmt|;
try|try
block|{
name|theHelper
operator|=
operator|new
name|OsgiSwaggerUiResolver
argument_list|(
name|Api
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
name|theHelper
operator|=
operator|new
name|SwaggerUiResolver
argument_list|(
name|AbstractSwaggerFeature
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|HELPER
operator|=
name|theHelper
expr_stmt|;
block|}
specifier|private
name|SwaggerUi
parameter_list|()
block|{     }
specifier|public
specifier|static
name|String
name|findSwaggerUiRoot
parameter_list|(
name|String
name|swaggerUiMavenGroupAndArtifact
parameter_list|,
name|String
name|swaggerUiVersion
parameter_list|)
block|{
name|String
name|root
init|=
name|HELPER
operator|.
name|findSwaggerUiRootInternal
argument_list|(
name|swaggerUiMavenGroupAndArtifact
argument_list|,
name|swaggerUiVersion
argument_list|)
decl_stmt|;
if|if
condition|(
name|root
operator|==
literal|null
operator|&&
name|HELPER
operator|.
name|getClass
argument_list|()
operator|!=
name|SwaggerUiResolver
operator|.
name|class
condition|)
block|{
name|root
operator|=
operator|new
name|SwaggerUiResolver
argument_list|(
name|AbstractSwaggerFeature
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
operator|.
name|findSwaggerUiRootInternal
argument_list|(
name|swaggerUiMavenGroupAndArtifact
argument_list|,
name|swaggerUiVersion
argument_list|)
expr_stmt|;
block|}
return|return
name|root
return|;
block|}
block|}
end_class

end_unit

