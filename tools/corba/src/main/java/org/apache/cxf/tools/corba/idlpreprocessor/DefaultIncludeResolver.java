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
name|tools
operator|.
name|corba
operator|.
name|idlpreprocessor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

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
name|logging
operator|.
name|Level
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

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|DefaultIncludeResolver
implements|implements
name|IncludeResolver
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
name|DefaultIncludeResolver
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|File
index|[]
name|userIdlDirs
decl_stmt|;
specifier|public
name|DefaultIncludeResolver
parameter_list|(
name|File
modifier|...
name|idlDirs
parameter_list|)
block|{
for|for
control|(
specifier|final
name|File
name|dir
range|:
name|idlDirs
control|)
block|{
if|if
condition|(
operator|!
name|dir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"not a directory: "
operator|+
name|dir
operator|.
name|getAbsoluteFile
argument_list|()
argument_list|)
throw|;
block|}
block|}
name|userIdlDirs
operator|=
name|idlDirs
expr_stmt|;
block|}
comment|/**      * @see IncludeResolver#findSystemInclude(java.lang.String)      */
specifier|public
name|URL
name|findSystemInclude
parameter_list|(
name|String
name|spec
parameter_list|)
block|{
return|return
name|findUserInclude
argument_list|(
name|spec
argument_list|)
return|;
block|}
comment|/**      * @see IncludeResolver#findUserInclude(java.lang.String)      */
specifier|public
name|URL
name|findUserInclude
parameter_list|(
name|String
name|spec
parameter_list|)
block|{
for|for
control|(
specifier|final
name|File
name|searchDir
range|:
name|userIdlDirs
control|)
block|{
name|URI
name|searchDirURI
init|=
name|searchDir
operator|.
name|toURI
argument_list|()
decl_stmt|;
try|try
block|{
comment|// offload slash vs backslash to URL machinery
name|URL
name|searchDirURL
init|=
name|searchDirURI
operator|.
name|toURL
argument_list|()
decl_stmt|;
specifier|final
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|searchDirURL
argument_list|,
name|spec
argument_list|)
decl_stmt|;
comment|// Check if file in URL exists, otherwise try next searchDir
try|try
block|{
comment|// If we can open a stream, the file exists
name|InputStream
name|str
init|=
name|url
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|str
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|url
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Not able to resolve "
operator|+
name|spec
operator|+
literal|"from  "
operator|+
name|searchDirURL
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
specifier|final
name|PreprocessingException
name|preprocessingException
init|=
operator|new
name|PreprocessingException
argument_list|(
literal|"Unable to resolve user include '"
operator|+
name|spec
operator|+
literal|"' in '"
operator|+
name|Arrays
operator|.
name|toString
argument_list|(
name|userIdlDirs
argument_list|)
operator|+
literal|"'"
argument_list|,
literal|null
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|preprocessingException
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|preprocessingException
throw|;
block|}
block|}
throw|throw
operator|new
name|PreprocessingException
argument_list|(
literal|"unable to resolve "
operator|+
name|spec
argument_list|,
literal|null
argument_list|,
literal|0
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

