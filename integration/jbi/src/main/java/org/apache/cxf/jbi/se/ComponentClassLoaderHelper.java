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
name|jbi
operator|.
name|se
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
name|URL
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|logging
operator|.
name|LogUtils
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|ComponentClassLoaderHelper
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|ComponentClassLoaderHelper
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|byte
index|[]
argument_list|>
name|nonClassesMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|byte
index|[]
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|ComponentClassLoaderHelper
parameter_list|()
block|{
comment|// singleton
block|}
specifier|public
specifier|static
name|boolean
name|hasResource
parameter_list|(
name|String
name|name
parameter_list|)
block|{
try|try
block|{
return|return
name|getResourceAsBytes
argument_list|(
name|name
argument_list|)
operator|!=
literal|null
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"unexpected exception: "
operator|+
name|ex
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
specifier|public
specifier|static
name|byte
index|[]
name|getResourceAsBytes
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
comment|// check nonClassCache for properties etc..
if|if
condition|(
operator|!
name|name
operator|.
name|endsWith
argument_list|(
literal|".class"
argument_list|)
operator|&&
name|nonClassesMap
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
operator|(
name|byte
index|[]
operator|)
operator|(
name|nonClassesMap
operator|.
name|get
argument_list|(
name|name
argument_list|)
operator|)
return|;
block|}
comment|// first check file path directorys, then check jars
if|if
condition|(
operator|!
name|isJarReference
argument_list|(
name|name
argument_list|)
condition|)
block|{
comment|// try direct load of url
try|try
block|{
return|return
name|JarLoader
operator|.
name|getBytesFromInputStream
argument_list|(
operator|new
name|URL
argument_list|(
name|name
argument_list|)
operator|.
name|openStream
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|java
operator|.
name|net
operator|.
name|MalformedURLException
name|mue
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|mue
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
else|else
block|{
comment|// something with !/
comment|// check for a nested directory reference
if|if
condition|(
name|isNestedDirectoryReference
argument_list|(
name|name
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Accessing contents of directories within jars is currently not supported"
argument_list|)
throw|;
block|}
else|else
block|{
name|String
name|enclosingJar
init|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|"!/"
argument_list|)
operator|+
literal|2
argument_list|)
decl_stmt|;
name|String
name|resourceName
init|=
name|name
operator|.
name|substring
argument_list|(
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|"!/"
argument_list|)
operator|+
literal|2
argument_list|)
decl_stmt|;
name|Map
name|jarMap
init|=
name|JarLoader
operator|.
name|getJarContents
argument_list|(
name|enclosingJar
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|jarMap
operator|&&
name|jarMap
operator|.
name|containsKey
argument_list|(
name|resourceName
argument_list|)
condition|)
block|{
name|byte
name|bytes
index|[]
init|=
operator|(
name|byte
index|[]
operator|)
name|jarMap
operator|.
name|get
argument_list|(
name|resourceName
argument_list|)
decl_stmt|;
comment|// this class will not be looked for again
comment|// once it is loaded so to save memory we
comment|// remove it form the map, if it is not a
comment|// class we add it to the nonClasses cache,
comment|// this is only true for in memory cache.
comment|// REVISIT - this needs to be more specific,
comment|// some classes Home|Remote interfaces are
comment|// loaded multiple times - see remote class
comment|// downloading for the moment disable this
comment|// jarMap.remove(resourceName);
comment|//
if|if
condition|(
operator|!
name|name
operator|.
name|endsWith
argument_list|(
literal|".class"
argument_list|)
condition|)
block|{
name|nonClassesMap
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|bytes
argument_list|)
expr_stmt|;
block|}
return|return
name|bytes
return|;
block|}
block|}
block|}
comment|// failed to locate the resource
return|return
literal|null
return|;
block|}
specifier|private
specifier|static
name|boolean
name|isJarReference
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|name
operator|.
name|indexOf
argument_list|(
literal|"!/"
argument_list|)
operator|!=
operator|-
literal|1
return|;
block|}
specifier|private
specifier|static
name|boolean
name|isNestedDirectoryReference
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|String
name|nestedDir
init|=
name|path
operator|.
name|substring
argument_list|(
name|path
operator|.
name|lastIndexOf
argument_list|(
literal|"!/"
argument_list|)
operator|+
literal|2
argument_list|)
decl_stmt|;
return|return
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|nestedDir
argument_list|)
operator|&&
name|nestedDir
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

