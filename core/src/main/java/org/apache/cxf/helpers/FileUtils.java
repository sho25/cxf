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
name|helpers
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
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|AccessController
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivilegedAction
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
name|Collections
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
name|Random
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|SystemPropertyAction
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|FileUtils
block|{
specifier|private
specifier|static
specifier|final
name|long
name|RETRY_SLEEP_MILLIS
init|=
literal|10L
decl_stmt|;
specifier|private
specifier|static
name|File
name|defaultTempDir
decl_stmt|;
specifier|private
specifier|static
name|Thread
name|shutdownHook
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|char
index|[]
name|ILLEGAL_CHARACTERS
init|=
block|{
literal|'/'
block|,
literal|'\n'
block|,
literal|'\r'
block|,
literal|'\t'
block|,
literal|'\0'
block|,
literal|'\f'
block|,
literal|'`'
block|,
literal|'?'
block|,
literal|'*'
block|,
literal|'\\'
block|,
literal|'<'
block|,
literal|'>'
block|,
literal|'|'
block|,
literal|'\"'
block|,
literal|':'
block|}
decl_stmt|;
specifier|private
name|FileUtils
parameter_list|()
block|{      }
specifier|public
specifier|static
name|boolean
name|isValidFileName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
name|name
operator|.
name|length
argument_list|()
init|;
name|i
operator|>
literal|0
condition|;
name|i
operator|--
control|)
block|{
name|char
name|c
init|=
name|name
operator|.
name|charAt
argument_list|(
name|i
operator|-
literal|1
argument_list|)
decl_stmt|;
for|for
control|(
name|char
name|c2
range|:
name|ILLEGAL_CHARACTERS
control|)
block|{
if|if
condition|(
name|c
operator|==
name|c2
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|getDefaultTempDir
argument_list|()
argument_list|,
name|name
argument_list|)
decl_stmt|;
name|boolean
name|isValid
init|=
literal|true
decl_stmt|;
try|try
block|{
if|if
condition|(
name|exists
argument_list|(
name|file
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|file
operator|.
name|createNewFile
argument_list|()
condition|)
block|{
name|file
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|isValid
operator|=
literal|false
expr_stmt|;
block|}
return|return
name|isValid
return|;
block|}
specifier|public
specifier|static
specifier|synchronized
name|File
name|getDefaultTempDir
parameter_list|()
block|{
if|if
condition|(
name|defaultTempDir
operator|!=
literal|null
operator|&&
name|exists
argument_list|(
name|defaultTempDir
argument_list|)
condition|)
block|{
return|return
name|defaultTempDir
return|;
block|}
name|String
name|s
init|=
name|SystemPropertyAction
operator|.
name|getPropertyOrNull
argument_list|(
name|FileUtils
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".TempDirectory"
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
comment|//assume someone outside of us will manage the directory
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|s
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
name|defaultTempDir
operator|=
name|f
expr_stmt|;
block|}
block|}
if|if
condition|(
name|defaultTempDir
operator|==
literal|null
condition|)
block|{
name|defaultTempDir
operator|=
name|createTmpDir
argument_list|(
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|shutdownHook
operator|!=
literal|null
condition|)
block|{
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|removeShutdownHook
argument_list|(
name|shutdownHook
argument_list|)
expr_stmt|;
block|}
name|shutdownHook
operator|=
operator|new
name|Thread
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|removeDir
argument_list|(
name|defaultTempDir
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
expr_stmt|;
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|addShutdownHook
argument_list|(
name|shutdownHook
argument_list|)
expr_stmt|;
block|}
return|return
name|defaultTempDir
return|;
block|}
specifier|public
specifier|static
specifier|synchronized
name|void
name|maybeDeleteDefaultTempDir
parameter_list|()
block|{
if|if
condition|(
name|defaultTempDir
operator|!=
literal|null
condition|)
block|{
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|gc
argument_list|()
expr_stmt|;
comment|// attempt a garbage collect to close any files
name|String
index|[]
name|files
init|=
name|defaultTempDir
operator|.
name|list
argument_list|()
decl_stmt|;
if|if
condition|(
name|files
operator|!=
literal|null
operator|&&
name|files
operator|.
name|length
operator|>
literal|0
condition|)
block|{
comment|//there are files in there, we need to attempt some more cleanup
comment|//HOWEVER, we don't want to just wipe out every file as something may be holding onto
comment|//the files for a reason. We'll re-run the gc and run the finalizers to see if
comment|//anything gets cleaned up.
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|gc
argument_list|()
expr_stmt|;
comment|// attempt a garbage collect to close any files
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|runFinalization
argument_list|()
expr_stmt|;
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|gc
argument_list|()
expr_stmt|;
name|files
operator|=
name|defaultTempDir
operator|.
name|list
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|files
operator|==
literal|null
operator|||
name|files
operator|.
name|length
operator|==
literal|0
condition|)
block|{
comment|//all the files are gone, we can remove the shutdownhook and reset
try|try
block|{
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|removeShutdownHook
argument_list|(
name|shutdownHook
argument_list|)
expr_stmt|;
name|shutdownHook
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|ex
parameter_list|)
block|{
comment|// The JVM is already shutting down so do nothing
block|}
name|shutdownHook
operator|=
literal|null
expr_stmt|;
name|defaultTempDir
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
specifier|public
specifier|static
name|File
name|createTmpDir
parameter_list|()
block|{
return|return
name|createTmpDir
argument_list|(
literal|true
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|File
name|createTmpDir
parameter_list|(
name|boolean
name|addHook
parameter_list|)
block|{
name|String
name|s
init|=
name|SystemPropertyAction
operator|.
name|getProperty
argument_list|(
literal|"java.io.tmpdir"
argument_list|)
decl_stmt|;
name|File
name|checkExists
init|=
operator|new
name|File
argument_list|(
name|s
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|exists
argument_list|(
name|checkExists
argument_list|)
operator|||
operator|!
name|checkExists
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"The directory "
operator|+
name|checkExists
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|" does not exist, please set java.io.tempdir"
operator|+
literal|" to an existing directory"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|checkExists
operator|.
name|canWrite
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"The directory "
operator|+
name|checkExists
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|" is not writable, please set java.io.tempdir"
operator|+
literal|" to a writable directory"
argument_list|)
throw|;
block|}
if|if
condition|(
name|checkExists
operator|.
name|getUsableSpace
argument_list|()
operator|<
literal|1024
operator|*
literal|1024
condition|)
block|{
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|FileUtils
operator|.
name|class
argument_list|)
operator|.
name|warning
argument_list|(
literal|"The directory "
operator|+
name|s
operator|+
literal|" has very "
operator|+
literal|"little usable temporary space.  Operations"
operator|+
literal|" requiring temporary files may fail."
argument_list|)
expr_stmt|;
block|}
name|File
name|newTmpDir
decl_stmt|;
try|try
block|{
name|Path
name|path
init|=
name|Files
operator|.
name|createTempDirectory
argument_list|(
name|checkExists
operator|.
name|toPath
argument_list|()
argument_list|,
literal|"cxf-tmp-"
argument_list|)
decl_stmt|;
name|File
name|f
init|=
name|path
operator|.
name|toFile
argument_list|()
decl_stmt|;
name|f
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
name|newTmpDir
operator|=
name|f
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|Random
name|r
init|=
operator|new
name|Random
argument_list|()
decl_stmt|;
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|checkExists
argument_list|,
literal|"cxf-tmp-"
operator|+
name|r
operator|.
name|nextInt
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|count
init|=
literal|0
init|;
operator|!
name|f
operator|.
name|mkdir
argument_list|()
condition|;
name|count
operator|++
control|)
block|{
if|if
condition|(
name|count
operator|>
literal|10000
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Could not create a temporary directory in "
operator|+
name|s
operator|+
literal|",  please set java.io.tempdir"
operator|+
literal|" to a writable directory"
argument_list|)
throw|;
block|}
name|f
operator|=
operator|new
name|File
argument_list|(
name|checkExists
argument_list|,
literal|"cxf-tmp-"
operator|+
name|r
operator|.
name|nextInt
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|newTmpDir
operator|=
name|f
expr_stmt|;
block|}
if|if
condition|(
name|addHook
condition|)
block|{
specifier|final
name|File
name|f2
init|=
name|newTmpDir
decl_stmt|;
name|Thread
name|hook
init|=
operator|new
name|Thread
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|removeDir
argument_list|(
name|f2
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|addShutdownHook
argument_list|(
name|hook
argument_list|)
expr_stmt|;
block|}
return|return
name|newTmpDir
return|;
block|}
specifier|public
specifier|static
name|void
name|mkDir
parameter_list|(
name|File
name|dir
parameter_list|)
block|{
if|if
condition|(
name|dir
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"dir attribute is required"
argument_list|)
throw|;
block|}
if|if
condition|(
name|dir
operator|.
name|isFile
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unable to create directory as a file "
operator|+
literal|"already exists with that name: "
operator|+
name|dir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|exists
argument_list|(
name|dir
argument_list|)
condition|)
block|{
name|boolean
name|result
init|=
name|doMkDirs
argument_list|(
name|dir
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|result
condition|)
block|{
name|String
name|msg
init|=
literal|"Directory "
operator|+
name|dir
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|" creation was not successful for an unknown reason"
decl_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**      * Attempt to fix possible race condition when creating directories on      * WinXP, also Windows2000. If the mkdirs does not work, wait a little and      * try again.      */
specifier|private
specifier|static
name|boolean
name|doMkDirs
parameter_list|(
name|File
name|f
parameter_list|)
block|{
if|if
condition|(
operator|!
name|f
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|RETRY_SLEEP_MILLIS
argument_list|)
expr_stmt|;
return|return
name|f
operator|.
name|mkdirs
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ex
parameter_list|)
block|{
return|return
name|f
operator|.
name|mkdirs
argument_list|()
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|public
specifier|static
name|void
name|removeDir
parameter_list|(
name|File
name|d
parameter_list|)
block|{
name|removeDir
argument_list|(
name|d
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|removeDir
parameter_list|(
name|File
name|d
parameter_list|,
name|boolean
name|inShutdown
parameter_list|)
block|{
name|String
index|[]
name|list
init|=
name|d
operator|.
name|list
argument_list|()
decl_stmt|;
if|if
condition|(
name|list
operator|==
literal|null
condition|)
block|{
name|list
operator|=
operator|new
name|String
index|[
literal|0
index|]
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|list
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|s
init|=
name|list
index|[
name|i
index|]
decl_stmt|;
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|d
argument_list|,
name|s
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|removeDir
argument_list|(
name|f
argument_list|,
name|inShutdown
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|delete
argument_list|(
name|f
argument_list|,
name|inShutdown
argument_list|)
expr_stmt|;
block|}
block|}
name|delete
argument_list|(
name|d
argument_list|,
name|inShutdown
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|delete
parameter_list|(
name|File
name|f
parameter_list|)
block|{
name|delete
argument_list|(
name|f
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|delete
parameter_list|(
name|File
name|f
parameter_list|,
name|boolean
name|inShutdown
parameter_list|)
block|{
if|if
condition|(
operator|!
name|f
operator|.
name|delete
argument_list|()
condition|)
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|RETRY_SLEEP_MILLIS
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ex
parameter_list|)
block|{
comment|// Ignore Exception
block|}
if|if
condition|(
operator|!
name|f
operator|.
name|delete
argument_list|()
operator|&&
operator|!
name|inShutdown
condition|)
block|{
name|f
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|public
specifier|static
name|File
name|createTempFile
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|suffix
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|createTempFile
argument_list|(
name|prefix
argument_list|,
name|suffix
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|File
name|createTempFile
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|suffix
parameter_list|,
name|File
name|parentDir
parameter_list|,
name|boolean
name|deleteOnExit
parameter_list|)
throws|throws
name|IOException
block|{
name|File
name|parent
init|=
operator|(
name|parentDir
operator|==
literal|null
operator|)
condition|?
name|getDefaultTempDir
argument_list|()
else|:
name|parentDir
decl_stmt|;
if|if
condition|(
name|prefix
operator|==
literal|null
condition|)
block|{
name|prefix
operator|=
literal|"cxf"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|prefix
operator|.
name|length
argument_list|()
operator|<
literal|3
condition|)
block|{
name|prefix
operator|=
name|prefix
operator|+
literal|"cxf"
expr_stmt|;
block|}
name|File
name|result
init|=
name|Files
operator|.
name|createTempFile
argument_list|(
name|parent
operator|.
name|toPath
argument_list|()
argument_list|,
name|prefix
argument_list|,
name|suffix
argument_list|)
operator|.
name|toFile
argument_list|()
decl_stmt|;
comment|//if parentDir is null, we're in our default dir
comment|//which will get completely wiped on exit from our exit
comment|//hook.  No need to set deleteOnExit() which leaks memory.
if|if
condition|(
name|deleteOnExit
operator|&&
name|parentDir
operator|!=
literal|null
condition|)
block|{
name|result
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|File
argument_list|>
name|getFilesUsingSuffix
parameter_list|(
name|File
name|dir
parameter_list|,
specifier|final
name|String
name|suffix
parameter_list|)
block|{
return|return
name|getFilesRecurseUsingSuffix
argument_list|(
name|dir
argument_list|,
name|suffix
argument_list|,
literal|false
argument_list|,
operator|new
name|ArrayList
argument_list|<>
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|File
argument_list|>
name|getFilesRecurseUsingSuffix
parameter_list|(
name|File
name|dir
parameter_list|,
specifier|final
name|String
name|suffix
parameter_list|)
block|{
return|return
name|getFilesRecurseUsingSuffix
argument_list|(
name|dir
argument_list|,
name|suffix
argument_list|,
literal|true
argument_list|,
operator|new
name|ArrayList
argument_list|<>
argument_list|()
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|List
argument_list|<
name|File
argument_list|>
name|getFilesRecurseUsingSuffix
parameter_list|(
name|File
name|dir
parameter_list|,
specifier|final
name|String
name|suffix
parameter_list|,
name|boolean
name|rec
parameter_list|,
name|List
argument_list|<
name|File
argument_list|>
name|fileList
parameter_list|)
block|{
name|File
index|[]
name|files
init|=
name|dir
operator|.
name|listFiles
argument_list|()
decl_stmt|;
if|if
condition|(
name|files
operator|!=
literal|null
condition|)
block|{
name|int
name|suffixLength
init|=
name|suffix
operator|.
name|length
argument_list|()
decl_stmt|;
for|for
control|(
name|File
name|file
range|:
name|files
control|)
block|{
if|if
condition|(
name|file
operator|.
name|isDirectory
argument_list|()
operator|&&
name|rec
condition|)
block|{
name|getFilesRecurseUsingSuffix
argument_list|(
name|file
argument_list|,
name|suffix
argument_list|,
name|rec
argument_list|,
name|fileList
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|file
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
name|suffix
argument_list|)
operator|&&
name|file
operator|.
name|getName
argument_list|()
operator|.
name|length
argument_list|()
operator|>
name|suffixLength
condition|)
block|{
name|fileList
operator|.
name|add
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|fileList
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|File
argument_list|>
name|getFiles
parameter_list|(
name|File
name|dir
parameter_list|,
specifier|final
name|String
name|pattern
parameter_list|)
block|{
name|List
argument_list|<
name|File
argument_list|>
name|fileList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|File
index|[]
name|files
init|=
name|dir
operator|.
name|listFiles
argument_list|()
decl_stmt|;
if|if
condition|(
name|files
operator|!=
literal|null
condition|)
block|{
name|Pattern
name|p
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|pattern
argument_list|)
decl_stmt|;
for|for
control|(
name|File
name|file
range|:
name|files
control|)
block|{
name|Matcher
name|m
init|=
name|p
operator|.
name|matcher
argument_list|(
name|file
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
name|fileList
operator|.
name|add
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|fileList
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|readLines
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|exists
argument_list|(
name|file
argument_list|)
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
return|return
name|Files
operator|.
name|readAllLines
argument_list|(
name|file
operator|.
name|toPath
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|boolean
name|exists
parameter_list|(
name|File
name|file
parameter_list|)
block|{
if|if
condition|(
name|System
operator|.
name|getSecurityManager
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|file
operator|.
name|exists
argument_list|()
return|;
block|}
return|return
name|AccessController
operator|.
name|doPrivileged
argument_list|(
call|(
name|PrivilegedAction
argument_list|<
name|Boolean
argument_list|>
call|)
argument_list|()
operator|->
block|{
return|return
name|file
operator|.
name|exists
argument_list|()
return|;
block|}
block|)
function|;
block|}
end_class

begin_comment
comment|/**      * Strips any leading paths      */
end_comment

begin_function
specifier|public
specifier|static
name|String
name|stripPath
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|int
name|posUnix
init|=
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
name|int
name|posWin
init|=
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|'\\'
argument_list|)
decl_stmt|;
name|int
name|pos
init|=
name|Math
operator|.
name|max
argument_list|(
name|posUnix
argument_list|,
name|posWin
argument_list|)
decl_stmt|;
if|if
condition|(
name|pos
operator|!=
operator|-
literal|1
condition|)
block|{
return|return
name|name
operator|.
name|substring
argument_list|(
name|pos
operator|+
literal|1
argument_list|)
return|;
block|}
return|return
name|name
return|;
block|}
end_function

unit|}
end_unit

