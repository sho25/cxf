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
name|MalformedURLException
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
name|java
operator|.
name|net
operator|.
name|URL
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
comment|/**  * Swagger UI resource locator  */
end_comment

begin_class
specifier|public
class|class
name|SwaggerUiResourceLocator
block|{
specifier|private
specifier|final
name|String
name|swaggerUiRoot
decl_stmt|;
specifier|public
name|SwaggerUiResourceLocator
parameter_list|(
name|String
name|swaggerUiRoot
parameter_list|)
block|{
name|this
operator|.
name|swaggerUiRoot
operator|=
name|swaggerUiRoot
expr_stmt|;
block|}
comment|/**      * Locate Swagger UI resource corresponding to resource path      * @param resourcePath resource path      * @return Swagger UI resource URL      * @throws MalformedURLException      */
specifier|public
name|URL
name|locate
parameter_list|(
name|String
name|resourcePath
parameter_list|)
throws|throws
name|MalformedURLException
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|resourcePath
argument_list|)
operator|||
literal|"/"
operator|.
name|equals
argument_list|(
name|resourcePath
argument_list|)
condition|)
block|{
name|resourcePath
operator|=
literal|"index.html"
expr_stmt|;
block|}
if|if
condition|(
name|resourcePath
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|resourcePath
operator|=
name|resourcePath
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|URL
name|ret
decl_stmt|;
try|try
block|{
name|ret
operator|=
name|URI
operator|.
name|create
argument_list|(
name|swaggerUiRoot
operator|+
name|resourcePath
argument_list|)
operator|.
name|toURL
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|MalformedURLException
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|ret
return|;
block|}
comment|/**      * Checks the existence of the Swagger UI resource corresponding to resource path      * @param resourcePath resource path      * @return "true" if Swagger UI resource exists, "false" otherwise      */
specifier|public
name|boolean
name|exists
parameter_list|(
name|String
name|resourcePath
parameter_list|)
block|{
try|try
block|{
comment|// The connect() will try to locate the entry (jar file, classpath resource)
comment|// and fail with FileNotFoundException /IOException if there is none.
name|locate
argument_list|(
name|resourcePath
argument_list|)
operator|.
name|openConnection
argument_list|()
operator|.
name|connect
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
end_class

end_unit

