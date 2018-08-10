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
name|lang
operator|.
name|annotation
operator|.
name|Annotation
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
name|osgi
operator|.
name|framework
operator|.
name|Bundle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|FrameworkUtil
import|;
end_import

begin_class
specifier|public
class|class
name|OsgiSwaggerUiResolver
extends|extends
name|SwaggerUiResolver
block|{
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_COORDINATES
init|=
literal|"org.webjars/swagger-ui"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_LOCATIONS
index|[]
init|=
block|{
literal|"mvn:"
operator|+
name|DEFAULT_COORDINATES
operator|+
literal|"/"
block|,
literal|"wrap:mvn:"
operator|+
name|DEFAULT_COORDINATES
operator|+
literal|"/"
block|}
decl_stmt|;
specifier|private
specifier|final
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|annotationBundle
decl_stmt|;
specifier|public
name|OsgiSwaggerUiResolver
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|annotationBundle
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|annotationBundle
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
name|Class
operator|.
name|forName
argument_list|(
literal|"org.osgi.framework.FrameworkUtil"
argument_list|)
expr_stmt|;
name|this
operator|.
name|annotationBundle
operator|=
name|annotationBundle
expr_stmt|;
block|}
annotation|@
name|Override
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
name|Bundle
name|bundle
init|=
name|FrameworkUtil
operator|.
name|getBundle
argument_list|(
name|annotationBundle
argument_list|)
decl_stmt|;
if|if
condition|(
name|bundle
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
index|[]
name|locations
init|=
name|swaggerUiMavenGroupAndArtifact
operator|==
literal|null
condition|?
name|DEFAULT_LOCATIONS
else|:
operator|new
name|String
index|[]
block|{
literal|"mvn:"
operator|+
name|swaggerUiMavenGroupAndArtifact
operator|+
literal|"/"
block|,
literal|"wrap:mvn:"
operator|+
name|swaggerUiMavenGroupAndArtifact
operator|+
literal|"/"
block|}
decl_stmt|;
for|for
control|(
name|Bundle
name|b
range|:
name|bundle
operator|.
name|getBundleContext
argument_list|()
operator|.
name|getBundles
argument_list|()
control|)
block|{
name|String
name|location
init|=
name|b
operator|.
name|getLocation
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|pattern
range|:
name|locations
control|)
block|{
if|if
condition|(
name|swaggerUiVersion
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|pattern
operator|+
name|swaggerUiVersion
argument_list|)
condition|)
block|{
return|return
name|getSwaggerUiRoot
argument_list|(
name|b
argument_list|,
name|swaggerUiVersion
argument_list|)
return|;
block|}
block|}
elseif|else
if|if
condition|(
name|location
operator|.
name|startsWith
argument_list|(
name|pattern
argument_list|)
condition|)
block|{
name|int
name|dollarIndex
init|=
name|location
operator|.
name|indexOf
argument_list|(
literal|"$"
argument_list|)
decl_stmt|;
name|swaggerUiVersion
operator|=
name|location
operator|.
name|substring
argument_list|(
name|pattern
operator|.
name|length
argument_list|()
argument_list|,
name|dollarIndex
operator|>
name|pattern
operator|.
name|length
argument_list|()
condition|?
name|dollarIndex
else|:
name|location
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|getSwaggerUiRoot
argument_list|(
name|b
argument_list|,
name|swaggerUiVersion
argument_list|)
return|;
block|}
block|}
if|if
condition|(
name|swaggerUiMavenGroupAndArtifact
operator|==
literal|null
condition|)
block|{
name|String
name|rootCandidate
init|=
name|getSwaggerUiRoot
argument_list|(
name|b
argument_list|,
name|swaggerUiVersion
argument_list|)
decl_stmt|;
if|if
condition|(
name|rootCandidate
operator|!=
literal|null
condition|)
block|{
return|return
name|rootCandidate
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
specifier|private
name|String
name|getSwaggerUiRoot
parameter_list|(
name|Bundle
name|b
parameter_list|,
name|String
name|swaggerUiVersion
parameter_list|)
block|{
if|if
condition|(
name|swaggerUiVersion
operator|==
literal|null
condition|)
block|{
name|swaggerUiVersion
operator|=
name|b
operator|.
name|getVersion
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
name|URL
name|entry
init|=
name|b
operator|.
name|getEntry
argument_list|(
name|SwaggerUiResolver
operator|.
name|UI_RESOURCES_ROOT_START
operator|+
name|swaggerUiVersion
argument_list|)
decl_stmt|;
if|if
condition|(
name|entry
operator|!=
literal|null
condition|)
block|{
return|return
name|entry
operator|.
name|toString
argument_list|()
operator|+
literal|"/"
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

