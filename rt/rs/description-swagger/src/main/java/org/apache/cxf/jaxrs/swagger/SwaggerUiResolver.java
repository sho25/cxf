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
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLClassLoader
import|;
end_import

begin_class
specifier|public
class|class
name|SwaggerUiResolver
block|{
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
argument_list|()
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
argument_list|()
expr_stmt|;
block|}
name|HELPER
operator|=
name|theHelper
expr_stmt|;
block|}
specifier|protected
name|SwaggerUiResolver
parameter_list|()
block|{     }
specifier|protected
name|String
name|findSwaggerUiRootInternal
parameter_list|(
name|String
name|swaggerUiVersion
parameter_list|)
block|{
try|try
block|{
name|ClassLoader
name|cl
init|=
name|AbstractSwaggerFeature
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
if|if
condition|(
name|cl
operator|instanceof
name|URLClassLoader
condition|)
block|{
specifier|final
name|String
name|resourcesRootStart
init|=
literal|"META-INF/resources/webjars/swagger-ui/"
decl_stmt|;
for|for
control|(
name|URL
name|url
range|:
operator|(
operator|(
name|URLClassLoader
operator|)
name|cl
operator|)
operator|.
name|getURLs
argument_list|()
control|)
block|{
name|String
name|urlStr
init|=
name|url
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|urlStr
operator|.
name|contains
argument_list|(
literal|"/swagger-ui"
argument_list|)
operator|&&
name|urlStr
operator|.
name|toString
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".jar"
argument_list|)
condition|)
block|{
name|urlStr
operator|=
name|urlStr
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|urlStr
operator|.
name|length
argument_list|()
operator|-
literal|4
argument_list|)
expr_stmt|;
name|String
name|version
init|=
name|urlStr
operator|.
name|substring
argument_list|(
name|urlStr
operator|.
name|lastIndexOf
argument_list|(
literal|"/swagger-ui"
argument_list|)
operator|+
literal|12
argument_list|)
decl_stmt|;
if|if
condition|(
name|swaggerUiVersion
operator|!=
literal|null
operator|&&
operator|!
name|swaggerUiVersion
operator|.
name|equals
argument_list|(
name|version
argument_list|)
condition|)
block|{
continue|continue;
block|}
return|return
literal|"jar:"
operator|+
name|url
operator|.
name|toString
argument_list|()
operator|+
literal|"!/"
operator|+
name|resourcesRootStart
operator|+
name|version
operator|+
literal|"/"
return|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|String
name|findSwaggerUiRoot
parameter_list|(
name|String
name|swaggerUiVersion
parameter_list|)
block|{
return|return
name|HELPER
operator|.
name|findSwaggerUiRootInternal
argument_list|(
name|swaggerUiVersion
argument_list|)
return|;
block|}
block|}
end_class

end_unit
