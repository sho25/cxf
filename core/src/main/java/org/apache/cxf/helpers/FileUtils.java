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
name|BufferedReader
import|;
end_import

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
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileReader
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
name|io
operator|.
name|InputStreamReader
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
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
name|int
name|RETRY_SLEEP_MILLIS
init|=
literal|10
decl_stmt|;
specifier|private
specifier|static
name|File
name|defaultTempDir
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
block|{              }
specifier|public
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
name|file
operator|.
name|exists
argument_list|()
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
name|defaultTempDir
operator|.
name|exists
argument_list|()
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
argument_list|()
expr_stmt|;
block|}
return|return
name|defaultTempDir
return|;
block|}
specifier|public
specifier|static
name|File
name|createTmpDir
parameter_list|()
block|{
name|int
name|x
init|=
call|(
name|int
call|)
argument_list|(
name|Math
operator|.
name|random
argument_list|()
operator|*
literal|1000000
argument_list|)
decl_stmt|;
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
name|checkExists
operator|.
name|exists
argument_list|()
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
name|f
init|=
operator|new
name|File
argument_list|(
name|checkExists
argument_list|,
literal|"cxf-tmp-"
operator|+
name|x
argument_list|)
decl_stmt|;
name|int
name|count
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|!
name|f
operator|.
name|mkdir
argument_list|()
condition|)
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
name|x
operator|=
call|(
name|int
call|)
argument_list|(
name|Math
operator|.
name|random
argument_list|()
operator|*
literal|1000000
argument_list|)
expr_stmt|;
name|f
operator|=
operator|new
name|File
argument_list|(
name|checkExists
argument_list|,
literal|"cxf-tmp-"
operator|+
name|x
argument_list|)
expr_stmt|;
name|count
operator|++
expr_stmt|;
block|}
name|File
name|newTmpDir
init|=
name|f
decl_stmt|;
specifier|final
name|File
name|f2
init|=
name|f
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
name|dir
operator|.
name|exists
argument_list|()
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
if|if
condition|(
name|isWindows
argument_list|()
condition|)
block|{
name|System
operator|.
name|gc
argument_list|()
expr_stmt|;
block|}
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
specifier|private
specifier|static
name|boolean
name|isWindows
parameter_list|()
block|{
name|String
name|osName
init|=
name|SystemPropertyAction
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
decl_stmt|;
return|return
name|osName
operator|.
name|indexOf
argument_list|(
literal|"windows"
argument_list|)
operator|>
operator|-
literal|1
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
name|result
init|=
literal|null
decl_stmt|;
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
name|suffix
operator|==
literal|null
condition|)
block|{
name|suffix
operator|=
literal|".tmp"
expr_stmt|;
block|}
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
name|result
operator|=
name|File
operator|.
name|createTempFile
argument_list|(
name|prefix
argument_list|,
name|suffix
argument_list|,
name|parent
argument_list|)
expr_stmt|;
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
name|String
name|getStringFromFile
parameter_list|(
name|File
name|location
parameter_list|)
block|{
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
name|String
name|result
init|=
literal|null
decl_stmt|;
try|try
block|{
name|is
operator|=
operator|new
name|FileInputStream
argument_list|(
name|location
argument_list|)
expr_stmt|;
name|result
operator|=
name|normalizeCRLF
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//do nothing
block|}
block|}
block|}
return|return
name|result
return|;
block|}
specifier|public
specifier|static
name|String
name|normalizeCRLF
parameter_list|(
name|InputStream
name|instream
parameter_list|)
block|{
name|BufferedReader
name|in
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|instream
argument_list|)
argument_list|)
decl_stmt|;
name|StringBuilder
name|result
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|String
name|line
init|=
literal|null
decl_stmt|;
try|try
block|{
name|line
operator|=
name|in
operator|.
name|readLine
argument_list|()
expr_stmt|;
while|while
condition|(
name|line
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|tok
init|=
name|line
operator|.
name|split
argument_list|(
literal|"\\s"
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|tok
operator|.
name|length
condition|;
name|x
operator|++
control|)
block|{
name|String
name|token
init|=
name|tok
index|[
name|x
index|]
decl_stmt|;
name|result
operator|.
name|append
argument_list|(
literal|"  "
operator|+
name|token
argument_list|)
expr_stmt|;
block|}
name|line
operator|=
name|in
operator|.
name|readLine
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
name|String
name|rtn
init|=
name|result
operator|.
name|toString
argument_list|()
decl_stmt|;
name|rtn
operator|=
name|ignoreTokens
argument_list|(
name|rtn
argument_list|,
literal|"<!--"
argument_list|,
literal|"-->"
argument_list|)
expr_stmt|;
name|rtn
operator|=
name|ignoreTokens
argument_list|(
name|rtn
argument_list|,
literal|"/*"
argument_list|,
literal|"*/"
argument_list|)
expr_stmt|;
return|return
name|rtn
return|;
block|}
specifier|private
specifier|static
name|String
name|ignoreTokens
parameter_list|(
specifier|final
name|String
name|contents
parameter_list|,
specifier|final
name|String
name|startToken
parameter_list|,
specifier|final
name|String
name|endToken
parameter_list|)
block|{
name|String
name|rtn
init|=
name|contents
decl_stmt|;
name|int
name|headerIndexStart
init|=
name|rtn
operator|.
name|indexOf
argument_list|(
name|startToken
argument_list|)
decl_stmt|;
name|int
name|headerIndexEnd
init|=
name|rtn
operator|.
name|indexOf
argument_list|(
name|endToken
argument_list|)
decl_stmt|;
if|if
condition|(
name|headerIndexStart
operator|!=
operator|-
literal|1
operator|&&
name|headerIndexEnd
operator|!=
operator|-
literal|1
operator|&&
name|headerIndexStart
operator|<
name|headerIndexEnd
condition|)
block|{
name|rtn
operator|=
name|rtn
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|headerIndexStart
operator|-
literal|1
argument_list|)
operator|+
name|rtn
operator|.
name|substring
argument_list|(
name|headerIndexEnd
operator|+
name|endToken
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|rtn
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
return|return
name|getFiles
argument_list|(
name|dir
argument_list|,
name|pattern
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|File
argument_list|>
name|getFilesRecurse
parameter_list|(
name|File
name|dir
parameter_list|,
specifier|final
name|String
name|pattern
parameter_list|)
block|{
return|return
name|getFilesRecurse
argument_list|(
name|dir
argument_list|,
name|pattern
argument_list|,
literal|null
argument_list|)
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
parameter_list|,
name|File
name|exclude
parameter_list|)
block|{
return|return
name|getFilesRecurse
argument_list|(
name|dir
argument_list|,
name|Pattern
operator|.
name|compile
argument_list|(
name|pattern
argument_list|)
argument_list|,
name|exclude
argument_list|,
literal|false
argument_list|,
operator|new
name|ArrayList
argument_list|<
name|File
argument_list|>
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
name|getFilesRecurse
parameter_list|(
name|File
name|dir
parameter_list|,
specifier|final
name|String
name|pattern
parameter_list|,
name|File
name|exclude
parameter_list|)
block|{
return|return
name|getFilesRecurse
argument_list|(
name|dir
argument_list|,
name|Pattern
operator|.
name|compile
argument_list|(
name|pattern
argument_list|)
argument_list|,
name|exclude
argument_list|,
literal|true
argument_list|,
operator|new
name|ArrayList
argument_list|<
name|File
argument_list|>
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
name|getFilesRecurse
parameter_list|(
name|File
name|dir
parameter_list|,
name|Pattern
name|pattern
parameter_list|,
name|File
name|exclude
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
for|for
control|(
name|File
name|file
range|:
name|dir
operator|.
name|listFiles
argument_list|()
control|)
block|{
if|if
condition|(
name|file
operator|.
name|equals
argument_list|(
name|exclude
argument_list|)
condition|)
block|{
continue|continue;
block|}
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
name|getFilesRecurse
argument_list|(
name|file
argument_list|,
name|pattern
argument_list|,
name|exclude
argument_list|,
name|rec
argument_list|,
name|fileList
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Matcher
name|m
init|=
name|pattern
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
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
return|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|results
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
try|try
init|(
name|BufferedReader
name|reader
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|FileReader
argument_list|(
name|file
argument_list|)
argument_list|)
init|)
block|{
name|String
name|line
init|=
name|reader
operator|.
name|readLine
argument_list|()
decl_stmt|;
while|while
condition|(
name|line
operator|!=
literal|null
condition|)
block|{
name|results
operator|.
name|add
argument_list|(
name|line
argument_list|)
expr_stmt|;
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|results
return|;
block|}
block|}
end_class

end_unit

