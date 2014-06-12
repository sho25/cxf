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
name|common
operator|.
name|util
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
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|Set
import|;
end_import

begin_class
specifier|public
class|class
name|ClasspathScanner
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ALL_FILES
init|=
literal|"**/*"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ALL_CLASS_FILES
init|=
name|ALL_FILES
operator|+
literal|".class"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ALL_PACKAGES
init|=
literal|"*"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CLASSPATH_URL_SCHEME
init|=
literal|"classpath:"
decl_stmt|;
specifier|static
specifier|final
name|ClasspathScanner
name|HELPER
decl_stmt|;
static|static
block|{
name|ClasspathScanner
name|theHelper
init|=
literal|null
decl_stmt|;
try|try
block|{
name|theHelper
operator|=
operator|new
name|SpringClasspathScanner
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
name|ClasspathScanner
argument_list|()
expr_stmt|;
block|}
name|HELPER
operator|=
name|theHelper
expr_stmt|;
block|}
comment|// Default packages list to ignore during classpath scanning
specifier|static
specifier|final
name|String
index|[]
name|PACKAGES_TO_SKIP
init|=
block|{
literal|"org.apache.cxf"
block|}
decl_stmt|;
specifier|protected
name|ClasspathScanner
parameter_list|()
block|{     }
comment|/**      * Scans list of base packages for all classes marked with specific annotations.       * @param basePackage base package       * @param annotations annotations to discover      * @return all discovered classes grouped by annotations they belong too       * @throws IOException class metadata is not readable       * @throws ClassNotFoundException class not found      */
specifier|public
specifier|static
name|Map
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|,
name|Collection
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
name|findClasses
parameter_list|(
name|String
name|basePackage
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
modifier|...
name|annotations
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
return|return
name|findClasses
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|basePackage
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|annotations
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Scans list of base packages for all classes marked with specific annotations.       * @param basePackages list of base packages       * @param annotations annotations to discover      * @return all discovered classes grouped by annotations they belong too       * @throws IOException class metadata is not readable       * @throws ClassNotFoundException class not found      */
specifier|public
specifier|static
name|Map
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|,
name|Collection
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
name|findClasses
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|basePackages
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
modifier|...
name|annotations
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
return|return
name|findClasses
argument_list|(
name|basePackages
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|annotations
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Scans list of base packages for all classes marked with specific annotations.       * @param basePackages list of base packages       * @param annotations annotations to discover      * @return all discovered classes grouped by annotations they belong too       * @throws IOException class metadata is not readable       * @throws ClassNotFoundException class not found      */
specifier|public
specifier|static
name|Map
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|,
name|Collection
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
name|findClasses
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|basePackages
parameter_list|,
name|List
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|>
name|annotations
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
return|return
name|findClasses
argument_list|(
name|basePackages
argument_list|,
name|annotations
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Map
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|,
name|Collection
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
name|findClasses
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|basePackages
parameter_list|,
name|List
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|>
name|annotations
parameter_list|,
name|ClassLoader
name|loader
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
return|return
name|HELPER
operator|.
name|findClassesInternal
argument_list|(
name|basePackages
argument_list|,
name|annotations
argument_list|,
name|loader
argument_list|)
return|;
block|}
specifier|protected
name|Map
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|,
name|Collection
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
name|findClassesInternal
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|basePackages
parameter_list|,
name|List
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|>
name|annotations
parameter_list|,
name|ClassLoader
name|loader
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
comment|/**      * Scans list of base packages for all resources with the given extension.       * @param basePackage base package       * @param extension the extension matching resources needs to have      * @return list of all discovered resource URLs       * @throws IOException resource is not accessible      */
specifier|public
specifier|static
name|List
argument_list|<
name|URL
argument_list|>
name|findResources
parameter_list|(
name|String
name|basePackage
parameter_list|,
name|String
name|extension
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|findResources
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|basePackage
argument_list|)
argument_list|,
name|extension
argument_list|)
return|;
block|}
comment|/**      * Scans list of base packages for all resources with the given extension.       * @param basePackages list of base packages       * @param extension the extension matching resources needs to have      * @return list of all discovered resource URLs       * @throws IOException resource is not accessible      */
specifier|public
specifier|static
name|List
argument_list|<
name|URL
argument_list|>
name|findResources
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|basePackages
parameter_list|,
name|String
name|extension
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|findResources
argument_list|(
name|basePackages
argument_list|,
name|extension
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|URL
argument_list|>
name|findResources
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|basePackages
parameter_list|,
name|String
name|extension
parameter_list|,
name|ClassLoader
name|loader
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|HELPER
operator|.
name|findResourcesInternal
argument_list|(
name|basePackages
argument_list|,
name|extension
argument_list|,
name|loader
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Set
argument_list|<
name|String
argument_list|>
name|parsePackages
parameter_list|(
specifier|final
name|String
name|packagesAsCsv
parameter_list|)
block|{
specifier|final
name|String
index|[]
name|values
init|=
name|StringUtils
operator|.
name|split
argument_list|(
name|packagesAsCsv
argument_list|,
literal|","
argument_list|)
decl_stmt|;
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|basePackages
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|(
name|values
operator|.
name|length
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|String
name|value
range|:
name|values
control|)
block|{
specifier|final
name|String
name|trimmed
init|=
name|value
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|trimmed
operator|.
name|equals
argument_list|(
name|ClasspathScanner
operator|.
name|ALL_PACKAGES
argument_list|)
condition|)
block|{
name|basePackages
operator|.
name|clear
argument_list|()
expr_stmt|;
name|basePackages
operator|.
name|add
argument_list|(
name|trimmed
argument_list|)
expr_stmt|;
break|break;
block|}
elseif|else
if|if
condition|(
name|trimmed
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|basePackages
operator|.
name|add
argument_list|(
name|trimmed
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|basePackages
return|;
block|}
specifier|protected
name|List
argument_list|<
name|URL
argument_list|>
name|findResourcesInternal
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|basePackages
parameter_list|,
name|String
name|extension
parameter_list|,
name|ClassLoader
name|loader
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
block|}
end_class

end_unit

