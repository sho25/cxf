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
name|LinkedList
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
name|javax
operator|.
name|tools
operator|.
name|Diagnostic
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|tools
operator|.
name|DiagnosticListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|tools
operator|.
name|JavaCompiler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|tools
operator|.
name|JavaCompiler
operator|.
name|CompilationTask
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|tools
operator|.
name|JavaFileManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|tools
operator|.
name|JavaFileObject
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|tools
operator|.
name|StandardJavaFileManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|tools
operator|.
name|ToolProvider
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
name|JavaUtils
import|;
end_import

begin_class
specifier|public
class|class
name|Compiler
block|{
specifier|private
name|long
name|maxMemory
init|=
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|maxMemory
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|verbose
decl_stmt|;
specifier|private
name|String
name|target
decl_stmt|;
specifier|private
name|String
name|outputDir
decl_stmt|;
specifier|private
name|String
name|classPath
decl_stmt|;
specifier|private
name|String
name|encoding
decl_stmt|;
specifier|private
name|boolean
name|forceFork
init|=
name|Boolean
operator|.
name|getBoolean
argument_list|(
name|Compiler
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"-fork"
argument_list|)
decl_stmt|;
specifier|private
name|File
name|classpathTmpFile
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|errors
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|warnings
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|Compiler
parameter_list|()
block|{     }
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getErrors
parameter_list|()
block|{
return|return
name|errors
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getWarnings
parameter_list|()
block|{
return|return
name|warnings
return|;
block|}
specifier|public
name|void
name|setMaxMemory
parameter_list|(
name|long
name|l
parameter_list|)
block|{
name|maxMemory
operator|=
name|l
expr_stmt|;
block|}
specifier|public
name|void
name|setVerbose
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
name|verbose
operator|=
name|b
expr_stmt|;
block|}
specifier|public
name|void
name|setTarget
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|target
operator|=
name|s
expr_stmt|;
block|}
specifier|public
name|void
name|setOutputDir
parameter_list|(
name|File
name|s
parameter_list|)
block|{
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
name|outputDir
operator|=
name|s
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
expr_stmt|;
block|}
else|else
block|{
name|outputDir
operator|=
literal|null
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setOutputDir
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|outputDir
operator|=
name|s
operator|.
name|replace
argument_list|(
name|File
operator|.
name|pathSeparatorChar
argument_list|,
literal|'/'
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setClassPath
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|classPath
operator|=
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|s
argument_list|)
condition|?
literal|null
else|:
name|s
expr_stmt|;
block|}
specifier|protected
name|void
name|addArgs
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|list
parameter_list|)
block|{
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|encoding
argument_list|)
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
literal|"-encoding"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|encoding
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|target
argument_list|)
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
literal|"-target"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|"-source"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|target
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|outputDir
argument_list|)
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
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|classPath
argument_list|)
condition|)
block|{
name|String
name|javaClasspath
init|=
name|SystemPropertyAction
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
name|File
name|f
init|=
operator|new
name|File
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
argument_list|)
decl_stmt|;
name|f
operator|=
operator|new
name|File
argument_list|(
name|f
argument_list|,
literal|"../lib"
argument_list|)
expr_stmt|;
if|if
condition|(
name|f
operator|.
name|exists
argument_list|()
operator|&&
name|f
operator|.
name|isDirectory
argument_list|()
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
name|f
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|classPath
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|compileFiles
parameter_list|(
name|File
index|[]
name|files
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|f
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|files
operator|.
name|length
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
name|f
operator|.
name|add
argument_list|(
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|compileFiles
argument_list|(
name|f
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|files
operator|.
name|length
index|]
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|compileFiles
parameter_list|(
name|List
argument_list|<
name|File
argument_list|>
name|files
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|f
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|files
operator|.
name|size
argument_list|()
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
name|f
operator|.
name|add
argument_list|(
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|compileFiles
argument_list|(
name|f
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|files
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|compileFiles
parameter_list|(
name|String
index|[]
name|files
parameter_list|)
block|{
name|String
name|endorsed
init|=
name|SystemPropertyAction
operator|.
name|getProperty
argument_list|(
literal|"java.endorsed.dirs"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|forceFork
condition|)
block|{
return|return
name|useJava6Compiler
argument_list|(
name|files
argument_list|)
return|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
comment|// Start of honoring java.home for used javac
name|String
name|fsep
init|=
name|File
operator|.
name|separator
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
name|SystemPropertyAction
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
name|SystemPropertyAction
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
name|SystemPropertyAction
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
name|SystemPropertyAction
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
name|SystemPropertyAction
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
elseif|else
if|if
condition|(
operator|new
name|File
argument_list|(
name|SystemPropertyAction
operator|.
name|getProperty
argument_list|(
literal|"java.home"
argument_list|)
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
comment|//java9
name|javacstr
operator|=
name|SystemPropertyAction
operator|.
name|getProperty
argument_list|(
literal|"java.home"
argument_list|)
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
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|endorsed
argument_list|)
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
literal|"-endorseddirs"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|endorsed
argument_list|)
expr_stmt|;
block|}
comment|//fix for CXF-2081, set maximum heap of this VM to javac.
name|list
operator|.
name|add
argument_list|(
literal|"-J-Xmx"
operator|+
name|maxMemory
argument_list|)
expr_stmt|;
if|if
condition|(
name|JavaUtils
operator|.
name|isJava9Compatible
argument_list|()
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
literal|"--add-modules"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|"java.activation,java.xml.ws.annotation,java.corba,java.transaction,java.xml.bind,java.xml.ws"
argument_list|)
expr_stmt|;
block|}
name|addArgs
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|int
name|classpathIdx
init|=
name|list
operator|.
name|indexOf
argument_list|(
literal|"-classpath"
argument_list|)
decl_stmt|;
name|String
name|classpath
init|=
name|list
operator|.
name|get
argument_list|(
name|classpathIdx
operator|+
literal|1
argument_list|)
decl_stmt|;
name|checkLongClasspath
argument_list|(
name|classpath
argument_list|,
name|list
argument_list|,
name|classpathIdx
argument_list|)
expr_stmt|;
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
specifier|protected
name|boolean
name|useJava6Compiler
parameter_list|(
name|String
index|[]
name|files
parameter_list|)
block|{
name|JavaCompiler
name|compiler
init|=
name|ToolProvider
operator|.
name|getSystemJavaCompiler
argument_list|()
decl_stmt|;
if|if
condition|(
name|compiler
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"No compiler detected, make sure you are running on top of a JDK instead of a JRE."
argument_list|)
throw|;
block|}
name|StandardJavaFileManager
name|fileManager
init|=
name|compiler
operator|.
name|getStandardFileManager
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Iterable
argument_list|<
name|?
extends|extends
name|JavaFileObject
argument_list|>
name|fileList
init|=
name|fileManager
operator|.
name|getJavaFileObjectsFromStrings
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|files
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|internalJava6Compile
argument_list|(
name|compiler
argument_list|,
name|wrapJavaFileManager
argument_list|(
name|fileManager
argument_list|)
argument_list|,
name|setupDiagnosticListener
argument_list|()
argument_list|,
name|fileList
argument_list|)
return|;
block|}
specifier|protected
name|JavaFileManager
name|wrapJavaFileManager
parameter_list|(
name|StandardJavaFileManager
name|standardJavaFileManger
parameter_list|)
block|{
return|return
name|standardJavaFileManger
return|;
block|}
specifier|protected
name|DiagnosticListener
argument_list|<
name|JavaFileObject
argument_list|>
name|setupDiagnosticListener
parameter_list|()
block|{
return|return
operator|new
name|DiagnosticListener
argument_list|<
name|JavaFileObject
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|report
parameter_list|(
name|Diagnostic
argument_list|<
name|?
extends|extends
name|JavaFileObject
argument_list|>
name|diagnostic
parameter_list|)
block|{
switch|switch
condition|(
name|diagnostic
operator|.
name|getKind
argument_list|()
condition|)
block|{
case|case
name|ERROR
case|:
name|errors
operator|.
name|add
argument_list|(
name|diagnostic
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|verbose
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|diagnostic
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|WARNING
case|:
case|case
name|MANDATORY_WARNING
case|:
name|warnings
operator|.
name|add
argument_list|(
name|diagnostic
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|verbose
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|diagnostic
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
break|break;
default|default:
break|break;
block|}
block|}
block|}
return|;
block|}
specifier|protected
name|boolean
name|internalJava6Compile
parameter_list|(
name|JavaCompiler
name|compiler
parameter_list|,
name|JavaFileManager
name|fileManager
parameter_list|,
name|DiagnosticListener
argument_list|<
name|JavaFileObject
argument_list|>
name|listener
parameter_list|,
name|Iterable
argument_list|<
name|?
extends|extends
name|JavaFileObject
argument_list|>
name|fileList
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|args
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|addArgs
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|CompilationTask
name|task
init|=
name|compiler
operator|.
name|getTask
argument_list|(
literal|null
argument_list|,
name|fileManager
argument_list|,
name|listener
argument_list|,
name|args
argument_list|,
literal|null
argument_list|,
name|fileList
argument_list|)
decl_stmt|;
name|Boolean
name|ret
init|=
name|task
operator|.
name|call
argument_list|()
decl_stmt|;
try|try
block|{
name|fileManager
operator|.
name|close
argument_list|()
expr_stmt|;
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
literal|"[ERROR] IOException during compiling."
argument_list|)
expr_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
return|return
name|ret
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
name|SystemPropertyAction
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
name|SystemPropertyAction
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
name|start
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
name|start
argument_list|()
expr_stmt|;
block|}
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
if|if
condition|(
name|classpathTmpFile
operator|!=
literal|null
operator|&&
name|classpathTmpFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|FileUtils
operator|.
name|delete
argument_list|(
name|classpathTmpFile
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
name|StringBuilder
name|strBuffer
init|=
operator|new
name|StringBuilder
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
specifier|private
name|boolean
name|isLongClasspath
parameter_list|(
name|String
name|classpath
parameter_list|)
block|{
return|return
name|classpath
operator|.
name|length
argument_list|()
operator|>
literal|2048
condition|?
literal|true
else|:
literal|false
return|;
block|}
specifier|private
name|void
name|checkLongClasspath
parameter_list|(
name|String
name|classpath
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|list
parameter_list|,
name|int
name|classpathIdx
parameter_list|)
block|{
if|if
condition|(
name|isLongClasspath
argument_list|(
name|classpath
argument_list|)
condition|)
block|{
name|PrintWriter
name|out
init|=
literal|null
decl_stmt|;
try|try
block|{
name|classpathTmpFile
operator|=
name|FileUtils
operator|.
name|createTempFile
argument_list|(
literal|"cxf-compiler-classpath"
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
name|classpathTmpFile
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|classpath
argument_list|)
expr_stmt|;
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
name|list
operator|.
name|set
argument_list|(
name|classpathIdx
operator|+
literal|1
argument_list|,
literal|"@"
operator|+
name|classpathTmpFile
argument_list|)
expr_stmt|;
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
literal|"[ERROR] can't write long classpath to @argfile"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|setEncoding
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|encoding
operator|=
name|string
expr_stmt|;
block|}
block|}
end_class

end_unit

