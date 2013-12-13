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
name|ArrayList
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
name|HashMap
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|classloader
operator|.
name|ClassLoaderUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|support
operator|.
name|PathMatchingResourcePatternResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|support
operator|.
name|ResourcePatternResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|type
operator|.
name|AnnotationMetadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|type
operator|.
name|classreading
operator|.
name|CachingMetadataReaderFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|type
operator|.
name|classreading
operator|.
name|MetadataReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|type
operator|.
name|classreading
operator|.
name|MetadataReaderFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|util
operator|.
name|ClassUtils
import|;
end_import

begin_class
class|class
name|SpringClasspathScanner
extends|extends
name|ClasspathScanner
block|{
name|SpringClasspathScanner
parameter_list|()
throws|throws
name|Exception
block|{
name|Class
operator|.
name|forName
argument_list|(
literal|"org.springframework.core.io.support.PathMatchingResourcePatternResolver"
argument_list|)
expr_stmt|;
name|Class
operator|.
name|forName
argument_list|(
literal|"org.springframework.core.type.classreading.CachingMetadataReaderFactory"
argument_list|)
expr_stmt|;
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
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
name|ResourcePatternResolver
name|resolver
init|=
operator|new
name|PathMatchingResourcePatternResolver
argument_list|()
decl_stmt|;
name|MetadataReaderFactory
name|factory
init|=
operator|new
name|CachingMetadataReaderFactory
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
specifier|final
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
name|classes
init|=
operator|new
name|HashMap
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
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|annotation
range|:
name|annotations
control|)
block|{
name|classes
operator|.
name|put
argument_list|(
name|annotation
argument_list|,
operator|new
name|ArrayList
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|basePackages
operator|==
literal|null
operator|||
name|basePackages
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|classes
return|;
block|}
for|for
control|(
specifier|final
name|String
name|basePackage
range|:
name|basePackages
control|)
block|{
specifier|final
name|boolean
name|scanAllPackages
init|=
name|basePackage
operator|.
name|equals
argument_list|(
name|ALL_PACKAGES
argument_list|)
decl_stmt|;
specifier|final
name|String
name|packageSearchPath
init|=
name|ResourcePatternResolver
operator|.
name|CLASSPATH_ALL_URL_PREFIX
operator|+
operator|(
name|scanAllPackages
condition|?
literal|""
else|:
name|ClassUtils
operator|.
name|convertClassNameToResourcePath
argument_list|(
name|basePackage
argument_list|)
operator|)
operator|+
name|ALL_CLASS_FILES
decl_stmt|;
specifier|final
name|Resource
index|[]
name|resources
init|=
name|resolver
operator|.
name|getResources
argument_list|(
name|packageSearchPath
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|Resource
name|resource
range|:
name|resources
control|)
block|{
specifier|final
name|MetadataReader
name|reader
init|=
name|factory
operator|.
name|getMetadataReader
argument_list|(
name|resource
argument_list|)
decl_stmt|;
specifier|final
name|AnnotationMetadata
name|metadata
init|=
name|reader
operator|.
name|getAnnotationMetadata
argument_list|()
decl_stmt|;
if|if
condition|(
name|scanAllPackages
operator|&&
name|shouldSkip
argument_list|(
name|metadata
operator|.
name|getClassName
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
for|for
control|(
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|annotation
range|:
name|annotations
control|)
block|{
if|if
condition|(
name|metadata
operator|.
name|isAnnotated
argument_list|(
name|annotation
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|classes
operator|.
name|get
argument_list|(
name|annotation
argument_list|)
operator|.
name|add
argument_list|(
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|metadata
operator|.
name|getClassName
argument_list|()
argument_list|,
name|getClass
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|classes
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
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|List
argument_list|<
name|URL
argument_list|>
name|resourceURLs
init|=
operator|new
name|ArrayList
argument_list|<
name|URL
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|basePackages
operator|==
literal|null
operator|||
name|basePackages
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|resourceURLs
return|;
block|}
name|ResourcePatternResolver
name|resolver
init|=
operator|new
name|PathMatchingResourcePatternResolver
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|String
name|basePackage
range|:
name|basePackages
control|)
block|{
specifier|final
name|boolean
name|scanAllPackages
init|=
name|basePackage
operator|.
name|equals
argument_list|(
name|ALL_PACKAGES
argument_list|)
decl_stmt|;
name|String
name|theBasePackage
init|=
name|basePackage
decl_stmt|;
if|if
condition|(
name|theBasePackage
operator|.
name|startsWith
argument_list|(
name|CLASSPATH_URL_SCHEME
argument_list|)
condition|)
block|{
name|theBasePackage
operator|=
name|theBasePackage
operator|.
name|substring
argument_list|(
name|CLASSPATH_URL_SCHEME
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|String
name|packageSearchPath
init|=
name|ResourcePatternResolver
operator|.
name|CLASSPATH_ALL_URL_PREFIX
operator|+
operator|(
name|scanAllPackages
condition|?
literal|""
else|:
name|ClassUtils
operator|.
name|convertClassNameToResourcePath
argument_list|(
name|theBasePackage
argument_list|)
operator|)
operator|+
name|ALL_FILES
operator|+
literal|"."
operator|+
name|extension
decl_stmt|;
specifier|final
name|Resource
index|[]
name|resources
init|=
name|resolver
operator|.
name|getResources
argument_list|(
name|packageSearchPath
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|Resource
name|resource
range|:
name|resources
control|)
block|{
name|resourceURLs
operator|.
name|add
argument_list|(
name|resource
operator|.
name|getURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|resourceURLs
return|;
block|}
specifier|private
name|boolean
name|shouldSkip
parameter_list|(
specifier|final
name|String
name|classname
parameter_list|)
block|{
for|for
control|(
name|String
name|packageToSkip
range|:
name|PACKAGES_TO_SKIP
control|)
block|{
if|if
condition|(
name|classname
operator|.
name|startsWith
argument_list|(
name|packageToSkip
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

