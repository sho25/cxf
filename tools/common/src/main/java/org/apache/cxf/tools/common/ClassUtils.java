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
name|common
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
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
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
name|HashSet
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
name|Set
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
name|i18n
operator|.
name|Message
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
name|Compiler
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
name|tools
operator|.
name|util
operator|.
name|ClassCollector
import|;
end_import

begin_class
specifier|public
class|class
name|ClassUtils
block|{
specifier|protected
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|ClassUtils
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|void
name|compile
parameter_list|(
name|ToolContext
name|context
parameter_list|)
throws|throws
name|ToolException
block|{
name|List
argument_list|<
name|String
argument_list|>
name|argList
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|//fix for CXF-2081, set maximum heap of current VM to javac.
name|argList
operator|.
name|add
argument_list|(
literal|"-J-Xmx"
operator|+
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|maxMemory
argument_list|()
argument_list|)
expr_stmt|;
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
comment|// hard code cxf.jar
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
comment|//&& (javaClasspath.indexOf("cxf.jar")>= 0);
if|if
condition|(
name|context
operator|.
name|isVerbose
argument_list|()
condition|)
block|{
name|argList
operator|.
name|add
argument_list|(
literal|"-verbose"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|"1.5"
operator|.
name|equals
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.specification.version"
argument_list|)
argument_list|)
condition|)
block|{
name|argList
operator|.
name|add
argument_list|(
literal|"-target"
argument_list|)
expr_stmt|;
name|argList
operator|.
name|add
argument_list|(
literal|"1.5"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_CLASSDIR
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|argList
operator|.
name|add
argument_list|(
literal|"-d"
argument_list|)
expr_stmt|;
name|String
name|classDir
init|=
operator|(
name|String
operator|)
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_CLASSDIR
argument_list|)
decl_stmt|;
name|argList
operator|.
name|add
argument_list|(
name|classDir
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
if|if
condition|(
operator|!
name|classpathSetted
condition|)
block|{
name|argList
operator|.
name|add
argument_list|(
literal|"-extdirs"
argument_list|)
expr_stmt|;
name|argList
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
name|argList
operator|.
name|add
argument_list|(
literal|"-classpath"
argument_list|)
expr_stmt|;
if|if
condition|(
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_OUTPUTDIR
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|argList
operator|.
name|add
argument_list|(
name|javaClasspath
operator|+
name|File
operator|.
name|pathSeparatorChar
operator|+
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_OUTPUTDIR
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|argList
operator|.
name|add
argument_list|(
name|javaClasspath
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|outPutDir
init|=
operator|(
name|String
operator|)
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_OUTPUTDIR
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|dirSet
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|ClassCollector
name|classCollector
init|=
name|context
operator|.
name|get
argument_list|(
name|ClassCollector
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|fileList
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|ite
init|=
name|classCollector
operator|.
name|getGeneratedFileInfo
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|ite
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|fileName
init|=
name|ite
operator|.
name|next
argument_list|()
decl_stmt|;
name|fileName
operator|=
name|fileName
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
name|File
operator|.
name|separatorChar
argument_list|)
expr_stmt|;
name|String
name|dirName
init|=
name|fileName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|fileName
operator|.
name|lastIndexOf
argument_list|(
name|File
operator|.
name|separator
argument_list|)
operator|+
literal|1
argument_list|)
decl_stmt|;
name|String
name|path
init|=
name|outPutDir
operator|+
name|File
operator|.
name|separator
operator|+
name|dirName
decl_stmt|;
if|if
condition|(
operator|!
name|dirSet
operator|.
name|contains
argument_list|(
name|path
argument_list|)
condition|)
block|{
name|dirSet
operator|.
name|add
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
for|for
control|(
name|String
name|str
range|:
name|file
operator|.
name|list
argument_list|()
control|)
block|{
if|if
condition|(
name|str
operator|.
name|endsWith
argument_list|(
literal|"java"
argument_list|)
condition|)
block|{
name|fileList
operator|.
name|add
argument_list|(
name|path
operator|+
name|str
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// copy generated xml file or others to class directory
name|File
name|otherFile
init|=
operator|new
name|File
argument_list|(
name|path
operator|+
name|File
operator|.
name|separator
operator|+
name|str
argument_list|)
decl_stmt|;
if|if
condition|(
name|otherFile
operator|.
name|isFile
argument_list|()
operator|&&
name|str
operator|.
name|toLowerCase
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"xml"
argument_list|)
operator|&&
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_CLASSDIR
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|String
name|targetDir
init|=
operator|(
name|String
operator|)
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_CLASSDIR
argument_list|)
decl_stmt|;
name|File
name|targetFile
init|=
operator|new
name|File
argument_list|(
name|targetDir
operator|+
name|File
operator|.
name|separator
operator|+
name|dirName
operator|+
name|File
operator|.
name|separator
operator|+
name|str
argument_list|)
decl_stmt|;
name|copyXmlFile
argument_list|(
name|otherFile
argument_list|,
name|targetFile
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// JAXB plugins will generate extra files under the runtime directory
comment|// Those files can not be allocated into the ClassCollector
name|File
name|jaxbRuntime
init|=
operator|new
name|File
argument_list|(
name|path
argument_list|,
literal|"runtime"
argument_list|)
decl_stmt|;
if|if
condition|(
name|jaxbRuntime
operator|.
name|isDirectory
argument_list|()
operator|&&
name|jaxbRuntime
operator|.
name|exists
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|File
argument_list|>
name|files
init|=
name|FileUtils
operator|.
name|getFiles
argument_list|(
name|jaxbRuntime
argument_list|,
literal|".+\\.java$"
argument_list|)
decl_stmt|;
for|for
control|(
name|File
name|f
range|:
name|files
control|)
block|{
name|fileList
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
block|}
block|}
block|}
comment|//Jaxb's bug . Jaxb ClassNameCollecotr may not be invoked when generated class is an enum.
comment|//So we need recheck whether we add all generated source files to  fileList
name|String
index|[]
name|arguments
init|=
operator|new
name|String
index|[
name|argList
operator|.
name|size
argument_list|()
operator|+
name|fileList
operator|.
name|size
argument_list|()
operator|+
literal|1
index|]
decl_stmt|;
name|arguments
index|[
literal|0
index|]
operator|=
literal|"javac"
expr_stmt|;
name|int
name|i
init|=
literal|1
decl_stmt|;
for|for
control|(
name|Object
name|obj
range|:
name|argList
operator|.
name|toArray
argument_list|()
control|)
block|{
name|String
name|arg
init|=
operator|(
name|String
operator|)
name|obj
decl_stmt|;
name|arguments
index|[
name|i
index|]
operator|=
name|arg
expr_stmt|;
name|i
operator|++
expr_stmt|;
block|}
name|int
name|srcFileIndex
init|=
name|i
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|fileList
operator|.
name|toArray
argument_list|()
control|)
block|{
name|String
name|file
init|=
operator|(
name|String
operator|)
name|o
decl_stmt|;
name|arguments
index|[
name|i
index|]
operator|=
name|file
expr_stmt|;
name|i
operator|++
expr_stmt|;
block|}
name|Compiler
name|compiler
init|=
operator|new
name|Compiler
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|compiler
operator|.
name|internalCompile
argument_list|(
name|arguments
argument_list|,
name|srcFileIndex
argument_list|)
condition|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"FAIL_TO_COMPILE_GENERATE_CODES"
argument_list|,
name|LOG
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|copyXmlFile
parameter_list|(
name|File
name|from
parameter_list|,
name|File
name|to
parameter_list|)
throws|throws
name|ToolException
block|{
try|try
block|{
name|String
name|dir
init|=
name|to
operator|.
name|getCanonicalPath
argument_list|()
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|to
operator|.
name|getCanonicalPath
argument_list|()
operator|.
name|lastIndexOf
argument_list|(
name|File
operator|.
name|separator
argument_list|)
argument_list|)
decl_stmt|;
name|File
name|dirFile
init|=
operator|new
name|File
argument_list|(
name|dir
argument_list|)
decl_stmt|;
name|dirFile
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|FileInputStream
name|input
init|=
operator|new
name|FileInputStream
argument_list|(
name|from
argument_list|)
decl_stmt|;
name|FileOutputStream
name|output
init|=
operator|new
name|FileOutputStream
argument_list|(
name|to
argument_list|)
decl_stmt|;
name|byte
index|[]
name|b
init|=
operator|new
name|byte
index|[
literal|1024
operator|*
literal|3
index|]
decl_stmt|;
name|int
name|len
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|len
operator|!=
operator|-
literal|1
condition|)
block|{
name|len
operator|=
name|input
operator|.
name|read
argument_list|(
name|b
argument_list|)
expr_stmt|;
if|if
condition|(
name|len
operator|!=
operator|-
literal|1
condition|)
block|{
name|output
operator|.
name|write
argument_list|(
name|b
argument_list|,
literal|0
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
block|}
name|output
operator|.
name|flush
argument_list|()
expr_stmt|;
name|output
operator|.
name|close
argument_list|()
expr_stmt|;
name|input
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
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"FAIL_TO_COPY_GENERATED_RESOURCE_FILE"
argument_list|,
name|LOG
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

