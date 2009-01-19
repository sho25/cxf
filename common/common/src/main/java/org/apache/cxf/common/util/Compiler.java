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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileWriter
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
name|PrintWriter
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
name|Arrays
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|helpers
operator|.
name|FileUtils
import|;
end_import

begin_class
specifier|public
class|class
name|Compiler
block|{
specifier|public
name|boolean
name|compileFiles
parameter_list|(
name|String
index|[]
name|files
parameter_list|,
name|File
name|outputDir
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|// Start of honoring java.home for used javac
name|String
name|fsep
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"file.separator"
argument_list|)
decl_stmt|;
name|String
name|javacstr
init|=
literal|"javac"
decl_stmt|;
name|String
name|platformjavacname
init|=
literal|"javac"
decl_stmt|;
if|if
condition|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
operator|.
name|toLowerCase
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"windows"
argument_list|)
operator|>
operator|-
literal|1
condition|)
block|{
name|platformjavacname
operator|=
literal|"javac.exe"
expr_stmt|;
block|}
if|if
condition|(
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.home"
argument_list|)
operator|+
name|fsep
operator|+
name|platformjavacname
argument_list|)
operator|.
name|exists
argument_list|()
condition|)
block|{
comment|// check if java.home is jdk home
name|javacstr
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.home"
argument_list|)
operator|+
name|fsep
operator|+
name|platformjavacname
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.home"
argument_list|)
operator|+
name|fsep
operator|+
literal|".."
operator|+
name|fsep
operator|+
literal|"bin"
operator|+
name|fsep
operator|+
name|platformjavacname
argument_list|)
operator|.
name|exists
argument_list|()
condition|)
block|{
comment|// check if java.home is jre home
name|javacstr
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.home"
argument_list|)
operator|+
name|fsep
operator|+
literal|".."
operator|+
name|fsep
operator|+
literal|"bin"
operator|+
name|fsep
operator|+
name|platformjavacname
expr_stmt|;
block|}
name|list
operator|.
name|add
argument_list|(
name|javacstr
argument_list|)
expr_stmt|;
comment|// End of honoring java.home for used javac
comment|// This code doesn't honor java.home
comment|// list.add("javac");
if|if
condition|(
name|outputDir
operator|!=
literal|null
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
literal|"-d"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|outputDir
operator|.
name|getAbsolutePath
argument_list|()
operator|.
name|replace
argument_list|(
name|File
operator|.
name|pathSeparatorChar
argument_list|,
literal|'/'
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|javaClasspath
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.class.path"
argument_list|)
decl_stmt|;
name|boolean
name|classpathSetted
init|=
name|javaClasspath
operator|!=
literal|null
condition|?
literal|true
else|:
literal|false
decl_stmt|;
if|if
condition|(
operator|!
name|classpathSetted
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
literal|"-extdirs"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"."
argument_list|)
operator|.
name|getFile
argument_list|()
operator|+
literal|"../lib/"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|list
operator|.
name|add
argument_list|(
literal|"-classpath"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|javaClasspath
argument_list|)
expr_stmt|;
block|}
name|int
name|idx
init|=
name|list
operator|.
name|size
argument_list|()
decl_stmt|;
name|list
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|files
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|internalCompile
argument_list|(
name|list
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|list
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|,
name|idx
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|internalCompile
parameter_list|(
name|String
index|[]
name|args
parameter_list|,
name|int
name|sourceFileIndex
parameter_list|)
block|{
name|Process
name|p
init|=
literal|null
decl_stmt|;
name|String
name|cmdArray
index|[]
init|=
literal|null
decl_stmt|;
name|File
name|tmpFile
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|isLongCommandLines
argument_list|(
name|args
argument_list|)
operator|&&
name|sourceFileIndex
operator|>=
literal|0
condition|)
block|{
name|PrintWriter
name|out
init|=
literal|null
decl_stmt|;
name|tmpFile
operator|=
name|FileUtils
operator|.
name|createTempFile
argument_list|(
literal|"cxf-compiler"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|out
operator|=
operator|new
name|PrintWriter
argument_list|(
operator|new
name|FileWriter
argument_list|(
name|tmpFile
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
name|sourceFileIndex
init|;
name|i
operator|<
name|args
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|indexOf
argument_list|(
literal|" "
argument_list|)
operator|>
operator|-
literal|1
condition|)
block|{
name|args
index|[
name|i
index|]
operator|=
name|args
index|[
name|i
index|]
operator|.
name|replace
argument_list|(
name|File
operator|.
name|separatorChar
argument_list|,
literal|'/'
argument_list|)
expr_stmt|;
comment|//
comment|// javac gives an error if you use forward slashes
comment|// with package-info.java. Refer to:
comment|// http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6198196
comment|//
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|indexOf
argument_list|(
literal|"package-info.java"
argument_list|)
operator|>
operator|-
literal|1
operator|&&
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
operator|.
name|toLowerCase
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"windows"
argument_list|)
operator|>
operator|-
literal|1
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|"\""
operator|+
name|args
index|[
name|i
index|]
operator|.
name|replaceAll
argument_list|(
literal|"/"
argument_list|,
literal|"\\\\\\\\"
argument_list|)
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|out
operator|.
name|println
argument_list|(
literal|"\""
operator|+
name|args
index|[
name|i
index|]
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|out
operator|.
name|println
argument_list|(
name|args
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
name|cmdArray
operator|=
operator|new
name|String
index|[
name|sourceFileIndex
operator|+
literal|1
index|]
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|args
argument_list|,
literal|0
argument_list|,
name|cmdArray
argument_list|,
literal|0
argument_list|,
name|sourceFileIndex
argument_list|)
expr_stmt|;
name|cmdArray
index|[
name|sourceFileIndex
index|]
operator|=
literal|"@"
operator|+
name|tmpFile
expr_stmt|;
block|}
else|else
block|{
name|cmdArray
operator|=
operator|new
name|String
index|[
name|args
operator|.
name|length
index|]
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|args
argument_list|,
literal|0
argument_list|,
name|cmdArray
argument_list|,
literal|0
argument_list|,
name|args
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
operator|.
name|toLowerCase
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"windows"
argument_list|)
operator|>
operator|-
literal|1
condition|)
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
name|cmdArray
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|cmdArray
index|[
name|i
index|]
operator|.
name|indexOf
argument_list|(
literal|"package-info"
argument_list|)
operator|==
operator|-
literal|1
condition|)
block|{
name|cmdArray
index|[
name|i
index|]
operator|=
name|cmdArray
index|[
name|i
index|]
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'/'
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|p
operator|=
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|exec
argument_list|(
name|cmdArray
argument_list|)
expr_stmt|;
if|if
condition|(
name|p
operator|.
name|getErrorStream
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|StreamPrinter
name|errorStreamPrinter
init|=
operator|new
name|StreamPrinter
argument_list|(
name|p
operator|.
name|getErrorStream
argument_list|()
argument_list|,
literal|""
argument_list|,
name|System
operator|.
name|out
argument_list|)
decl_stmt|;
name|errorStreamPrinter
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|p
operator|.
name|getInputStream
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|StreamPrinter
name|infoStreamPrinter
init|=
operator|new
name|StreamPrinter
argument_list|(
name|p
operator|.
name|getInputStream
argument_list|()
argument_list|,
literal|"[INFO]"
argument_list|,
name|System
operator|.
name|out
argument_list|)
decl_stmt|;
name|infoStreamPrinter
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|p
operator|!=
literal|null
condition|)
block|{
return|return
name|p
operator|.
name|waitFor
argument_list|()
operator|==
literal|0
condition|?
literal|true
else|:
literal|false
return|;
block|}
block|}
catch|catch
parameter_list|(
name|SecurityException
name|e
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"[ERROR] SecurityException during exec() of compiler \""
operator|+
name|args
index|[
literal|0
index|]
operator|+
literal|"\"."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|print
argument_list|(
literal|"[ERROR] IOException during exec() of compiler \""
operator|+
name|args
index|[
literal|0
index|]
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|". Check your path environment variable."
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|tmpFile
operator|!=
literal|null
operator|&&
name|tmpFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|FileUtils
operator|.
name|delete
argument_list|(
name|tmpFile
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|boolean
name|isLongCommandLines
parameter_list|(
name|String
name|args
index|[]
parameter_list|)
block|{
name|StringBuffer
name|strBuffer
init|=
operator|new
name|StringBuffer
argument_list|()
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
name|args
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|strBuffer
operator|.
name|append
argument_list|(
name|args
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|strBuffer
operator|.
name|toString
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|4096
condition|?
literal|true
else|:
literal|false
return|;
block|}
block|}
end_class

end_unit

