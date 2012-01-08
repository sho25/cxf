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
name|jca
operator|.
name|core
operator|.
name|classloader
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
name|io
operator|.
name|InputStream
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
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|ProtectionDomain
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|SecureClassLoader
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
name|Enumeration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|Properties
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
name|classloader
operator|.
name|FireWallClassLoader
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
class|class
name|PlugInClassLoader
extends|extends
name|SecureClassLoader
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
name|PlugInClassLoader
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|FILE_COLON
init|=
literal|"file:"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ZIP_COLON
init|=
literal|"zip:"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|URL_SCHEME_COLON
init|=
literal|"classloader:"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|JARS_PROPS_FILE
init|=
literal|"jars.properties"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|FILTERS_PROPS_FILE
init|=
literal|"filters.properties"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NEFILTERS_PROPS_FILE
init|=
literal|"negativefilters.properties"
decl_stmt|;
specifier|private
name|String
name|jarUrls
index|[]
init|=
operator|new
name|String
index|[
literal|0
index|]
decl_stmt|;
specifier|private
specifier|final
name|ProtectionDomain
name|protectionDomain
decl_stmt|;
specifier|private
name|ClassLoader
name|ploader
decl_stmt|;
specifier|public
name|PlugInClassLoader
parameter_list|(
name|ClassLoader
name|p
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
operator|new
name|FireWallClassLoader
argument_list|(
name|p
argument_list|,
name|getFilterList
argument_list|(
name|p
argument_list|,
name|FILTERS_PROPS_FILE
argument_list|)
argument_list|,
name|getFilterList
argument_list|(
name|p
argument_list|,
name|NEFILTERS_PROPS_FILE
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|ploader
operator|=
name|p
expr_stmt|;
name|protectionDomain
operator|=
name|getClass
argument_list|()
operator|.
name|getProtectionDomain
argument_list|()
expr_stmt|;
name|jarUrls
operator|=
name|loadUrls
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|processJarUrls
argument_list|(
name|jarUrls
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|processJarUrls
parameter_list|(
name|String
name|urls
index|[]
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|urls
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|urls
index|[
name|i
index|]
operator|.
name|startsWith
argument_list|(
name|ZIP_COLON
argument_list|)
condition|)
block|{
name|urls
index|[
name|i
index|]
operator|=
name|FILE_COLON
operator|+
name|urls
index|[
name|i
index|]
operator|.
name|substring
argument_list|(
name|ZIP_COLON
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
specifier|static
name|String
index|[]
name|getFilterList
parameter_list|(
name|ClassLoader
name|parent
parameter_list|,
name|String
name|propFile
parameter_list|)
throws|throws
name|IOException
block|{
name|Properties
name|filtersProps
init|=
name|getProperties
argument_list|(
name|parent
argument_list|,
name|propFile
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|Object
argument_list|>
name|i
init|=
name|filtersProps
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|i
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|config
argument_list|(
literal|"get Filter "
operator|+
name|propFile
operator|+
literal|"::"
operator|+
operator|(
name|String
operator|)
name|i
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|filtersProps
operator|.
name|keySet
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[]
block|{}
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|Properties
name|getProperties
parameter_list|(
name|ClassLoader
name|parent
parameter_list|,
name|String
name|propsFileName
parameter_list|)
throws|throws
name|IOException
block|{
name|InputStream
name|in
init|=
name|parent
operator|.
name|getResourceAsStream
argument_list|(
name|propsFileName
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|in
condition|)
block|{
name|in
operator|=
name|PlugInClassLoader
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
name|propsFileName
argument_list|)
expr_stmt|;
if|if
condition|(
literal|null
operator|==
name|in
condition|)
block|{
name|String
name|msg
init|=
literal|"Internal rar classloader failed to locate configuration resource: "
operator|+
name|propsFileName
decl_stmt|;
name|IOException
name|ioe
init|=
operator|new
name|IOException
argument_list|(
name|msg
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|warning
argument_list|(
name|ioe
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|ioe
throw|;
block|}
block|}
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|props
operator|.
name|load
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Contents: "
operator|+
name|propsFileName
operator|+
name|props
argument_list|)
expr_stmt|;
return|return
name|props
return|;
block|}
specifier|private
name|String
index|[]
name|loadUrls
parameter_list|(
name|ClassLoader
name|parent
parameter_list|)
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|String
argument_list|>
name|urlList
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|Properties
name|props
init|=
name|getProperties
argument_list|(
name|parent
argument_list|,
name|JARS_PROPS_FILE
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
name|props
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|Enumeration
argument_list|<
name|Object
argument_list|>
name|keys
init|=
name|props
operator|.
name|keys
argument_list|()
decl_stmt|;
while|while
condition|(
name|keys
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|String
name|key
init|=
operator|(
name|String
operator|)
name|keys
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|URL
name|url
init|=
name|parent
operator|.
name|getResource
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|config
argument_list|(
name|url
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|urlList
operator|.
name|add
argument_list|(
name|url
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|urlList
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[]
block|{}
argument_list|)
return|;
block|}
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|findClass
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|ClassNotFoundException
block|{
name|String
name|path
init|=
name|name
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
operator|.
name|concat
argument_list|(
literal|".class"
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"findClass "
operator|+
name|path
argument_list|)
expr_stmt|;
name|byte
name|bytes
index|[]
init|=
literal|null
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|jarUrls
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|fullpath
init|=
name|jarUrls
index|[
name|i
index|]
operator|+
literal|"!/"
operator|+
name|path
decl_stmt|;
try|try
block|{
name|bytes
operator|=
name|PlugInClassLoaderHelper
operator|.
name|getResourceAsBytes
argument_list|(
name|fullpath
argument_list|)
expr_stmt|;
if|if
condition|(
name|bytes
operator|!=
literal|null
condition|)
block|{
break|break;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
comment|// we should find everything we look for but if we don't our
comment|// parent can when we throw cnf below
name|LOG
operator|.
name|fine
argument_list|(
literal|"findClass: "
operator|+
name|name
operator|+
literal|": "
operator|+
name|ex
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|bytes
operator|!=
literal|null
condition|)
block|{
return|return
name|defineClass
argument_list|(
name|name
argument_list|,
name|bytes
argument_list|,
literal|0
argument_list|,
name|bytes
operator|.
name|length
argument_list|,
name|protectionDomain
argument_list|)
return|;
block|}
else|else
block|{
name|LOG
operator|.
name|config
argument_list|(
literal|"can't find name "
operator|+
name|name
operator|+
literal|" , try to using the ploader"
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|result
init|=
name|ploader
operator|.
name|loadClass
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|result
condition|)
block|{
throw|throw
operator|new
name|ClassNotFoundException
argument_list|(
name|name
argument_list|)
throw|;
block|}
else|else
block|{
return|return
name|result
return|;
block|}
block|}
block|}
specifier|protected
name|URL
name|findResource
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"findResource: "
operator|+
name|name
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|jarUrls
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|fullpath
init|=
name|jarUrls
index|[
name|i
index|]
operator|+
literal|"!/"
operator|+
name|name
decl_stmt|;
if|if
condition|(
name|PlugInClassLoaderHelper
operator|.
name|hasResource
argument_list|(
name|fullpath
argument_list|)
condition|)
block|{
return|return
name|genURL
argument_list|(
name|fullpath
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|URL
name|genURL
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|URL
name|url
init|=
literal|null
decl_stmt|;
name|String
name|urlString
init|=
name|URL_SCHEME_COLON
operator|+
name|path
decl_stmt|;
try|try
block|{
name|url
operator|=
operator|new
name|URL
argument_list|(
literal|null
argument_list|,
name|urlString
argument_list|,
operator|new
name|Handler
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|mue
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
name|mue
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|url
return|;
block|}
block|}
end_class

end_unit

