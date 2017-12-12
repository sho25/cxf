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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
import|;
end_import

begin_class
specifier|public
class|class
name|SwaggerUiResolver
block|{
specifier|protected
specifier|static
specifier|final
name|String
name|UI_RESOURCES_ROOT_START
init|=
literal|"META-INF/resources/webjars/swagger-ui/"
decl_stmt|;
specifier|private
specifier|final
name|ClassLoader
name|cl
decl_stmt|;
specifier|public
name|SwaggerUiResolver
parameter_list|(
name|ClassLoader
name|cl
parameter_list|)
block|{
name|this
operator|.
name|cl
operator|=
name|cl
expr_stmt|;
block|}
specifier|public
name|String
name|findSwaggerUiRootInternal
parameter_list|(
name|String
name|swaggerUiMavenGroupAndArtifact
parameter_list|,
name|String
name|swaggerUiVersion
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|cl
operator|instanceof
name|URLClassLoader
condition|)
block|{
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
name|root
init|=
name|checkUiRoot
argument_list|(
name|url
operator|.
name|toString
argument_list|()
argument_list|,
name|swaggerUiVersion
argument_list|)
decl_stmt|;
if|if
condition|(
name|root
operator|!=
literal|null
condition|)
block|{
return|return
name|root
return|;
block|}
block|}
block|}
name|Enumeration
argument_list|<
name|URL
argument_list|>
name|urls
init|=
name|cl
operator|.
name|getResources
argument_list|(
name|UI_RESOURCES_ROOT_START
argument_list|)
decl_stmt|;
while|while
condition|(
name|urls
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|String
name|urlStr
init|=
name|urls
operator|.
name|nextElement
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|replace
argument_list|(
name|UI_RESOURCES_ROOT_START
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|String
name|root
init|=
name|checkUiRoot
argument_list|(
name|urlStr
argument_list|,
name|swaggerUiVersion
argument_list|)
decl_stmt|;
if|if
condition|(
name|root
operator|!=
literal|null
condition|)
block|{
return|return
name|root
return|;
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
specifier|protected
specifier|static
name|String
name|checkUiRoot
parameter_list|(
name|String
name|urlStr
parameter_list|,
name|String
name|swaggerUiVersion
parameter_list|)
block|{
name|int
name|swaggerUiIndex
init|=
name|urlStr
operator|.
name|lastIndexOf
argument_list|(
literal|"/swagger-ui-"
argument_list|)
decl_stmt|;
if|if
condition|(
name|swaggerUiIndex
operator|!=
operator|-
literal|1
condition|)
block|{
name|boolean
name|urlEndsWithJarSep
init|=
name|urlStr
operator|.
name|endsWith
argument_list|(
literal|".jar!/"
argument_list|)
decl_stmt|;
if|if
condition|(
name|urlEndsWithJarSep
operator|||
name|urlStr
operator|.
name|endsWith
argument_list|(
literal|".jar"
argument_list|)
condition|)
block|{
name|int
name|offset
init|=
name|urlEndsWithJarSep
condition|?
literal|6
else|:
literal|4
decl_stmt|;
name|String
name|version
init|=
name|urlStr
operator|.
name|substring
argument_list|(
name|swaggerUiIndex
operator|+
literal|12
argument_list|,
name|urlStr
operator|.
name|length
argument_list|()
operator|-
name|offset
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
return|return
literal|null
return|;
block|}
if|if
condition|(
operator|!
name|urlEndsWithJarSep
condition|)
block|{
name|urlStr
operator|=
literal|"jar:"
operator|+
name|urlStr
operator|+
literal|"!/"
expr_stmt|;
block|}
return|return
name|urlStr
operator|+
name|UI_RESOURCES_ROOT_START
operator|+
name|version
operator|+
literal|"/"
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit
