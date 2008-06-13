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
name|maven_plugin
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
name|net
operator|.
name|URI
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
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|AbstractMojo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|MojoExecutionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|project
operator|.
name|MavenProject
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|ExitException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|util
operator|.
name|optional
operator|.
name|NoExitSecurityManager
import|;
end_import

begin_comment
comment|/**  * @goal xsdtojava  * @description CXF XSD To Java Tool  */
end_comment

begin_class
specifier|public
class|class
name|XSDToJavaMojo
extends|extends
name|AbstractMojo
block|{
comment|/**      * @parameter      */
name|String
name|testSourceRoot
decl_stmt|;
comment|/**      * @parameter  expression="${project.build.directory}/generated/src/main/java"      * @required      */
name|String
name|sourceRoot
decl_stmt|;
comment|/**      * @parameter expression="${project}"      * @required      */
name|MavenProject
name|project
decl_stmt|;
comment|/**      * @parameter      */
name|XsdOption
name|xsdOptions
index|[]
decl_stmt|;
comment|/**      * Directory in which the "DONE" markers are saved that       * @parameter expression="${cxf.markerDirectory}"       *            default-value="${project.build.directory}/cxf-xsd-plugin-markers"      */
name|File
name|markerDirectory
decl_stmt|;
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|MojoExecutionException
block|{
name|String
name|outputDir
init|=
name|testSourceRoot
operator|==
literal|null
condition|?
name|sourceRoot
else|:
name|testSourceRoot
decl_stmt|;
name|File
name|outputDirFile
init|=
operator|new
name|File
argument_list|(
name|outputDir
argument_list|)
decl_stmt|;
name|outputDirFile
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|markerDirectory
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|long
name|timestamp
init|=
name|CodegenUtils
operator|.
name|getCodegenTimestamp
argument_list|()
decl_stmt|;
name|boolean
name|result
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|xsdOptions
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Must specify xsdOptions"
argument_list|)
throw|;
block|}
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|xsdOptions
operator|.
name|length
condition|;
name|x
operator|++
control|)
block|{
name|String
index|[]
name|args
init|=
name|getArguments
argument_list|(
name|xsdOptions
index|[
name|x
index|]
argument_list|,
name|outputDir
argument_list|)
decl_stmt|;
name|String
name|xsdLocation
init|=
name|xsdOptions
index|[
name|x
index|]
operator|.
name|getXsd
argument_list|()
decl_stmt|;
name|File
name|xsdFile
init|=
operator|new
name|File
argument_list|(
name|xsdLocation
argument_list|)
decl_stmt|;
name|URI
name|basedir
init|=
name|project
operator|.
name|getBasedir
argument_list|()
operator|.
name|toURI
argument_list|()
decl_stmt|;
name|URI
name|xsdURI
decl_stmt|;
if|if
condition|(
name|xsdFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|xsdURI
operator|=
name|xsdFile
operator|.
name|toURI
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|xsdURI
operator|=
name|basedir
operator|.
name|resolve
argument_list|(
name|xsdLocation
argument_list|)
expr_stmt|;
block|}
name|String
name|doneFileName
init|=
name|xsdURI
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|doneFileName
operator|.
name|startsWith
argument_list|(
name|basedir
operator|.
name|toString
argument_list|()
argument_list|)
condition|)
block|{
name|doneFileName
operator|=
name|doneFileName
operator|.
name|substring
argument_list|(
name|basedir
operator|.
name|toString
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|doneFileName
operator|=
name|doneFileName
operator|.
name|replace
argument_list|(
literal|'?'
argument_list|,
literal|'_'
argument_list|)
operator|.
name|replace
argument_list|(
literal|'&'
argument_list|,
literal|'_'
argument_list|)
operator|.
name|replace
argument_list|(
literal|'/'
argument_list|,
literal|'_'
argument_list|)
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'_'
argument_list|)
expr_stmt|;
comment|// If URL to WSDL, replace ? and& since they're invalid chars for file names
name|File
name|doneFile
init|=
operator|new
name|File
argument_list|(
name|markerDirectory
argument_list|,
literal|"."
operator|+
name|doneFileName
operator|+
literal|".DONE"
argument_list|)
decl_stmt|;
name|long
name|srctimestamp
init|=
literal|0
decl_stmt|;
if|if
condition|(
literal|"file"
operator|.
name|equals
argument_list|(
name|xsdURI
operator|.
name|getScheme
argument_list|()
argument_list|)
condition|)
block|{
name|srctimestamp
operator|=
operator|new
name|File
argument_list|(
name|xsdURI
argument_list|)
operator|.
name|lastModified
argument_list|()
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|srctimestamp
operator|=
name|xsdURI
operator|.
name|toURL
argument_list|()
operator|.
name|openConnection
argument_list|()
operator|.
name|getDate
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//ignore
block|}
block|}
name|boolean
name|doWork
init|=
name|timestamp
operator|>
name|doneFile
operator|.
name|lastModified
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|doneFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|doWork
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|srctimestamp
operator|>
name|doneFile
operator|.
name|lastModified
argument_list|()
condition|)
block|{
name|doWork
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|File
name|files
index|[]
init|=
name|xsdOptions
index|[
name|x
index|]
operator|.
name|getDependencies
argument_list|()
decl_stmt|;
if|if
condition|(
name|files
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|z
init|=
literal|0
init|;
name|z
operator|<
name|files
operator|.
name|length
condition|;
operator|++
name|z
control|)
block|{
if|if
condition|(
name|files
index|[
name|z
index|]
operator|.
name|lastModified
argument_list|()
operator|>
name|doneFile
operator|.
name|lastModified
argument_list|()
condition|)
block|{
name|doWork
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
name|doWork
condition|)
block|{
name|SecurityManager
name|oldSm
init|=
name|System
operator|.
name|getSecurityManager
argument_list|()
decl_stmt|;
try|try
block|{
try|try
block|{
name|System
operator|.
name|setSecurityManager
argument_list|(
operator|new
name|NoExitSecurityManager
argument_list|()
argument_list|)
expr_stmt|;
name|com
operator|.
name|sun
operator|.
name|tools
operator|.
name|xjc
operator|.
name|Driver
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExitException
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getStatus
argument_list|()
operator|==
literal|0
condition|)
block|{
name|doneFile
operator|.
name|delete
argument_list|()
expr_stmt|;
name|doneFile
operator|.
name|createNewFile
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
name|e
throw|;
block|}
block|}
finally|finally
block|{
name|System
operator|.
name|setSecurityManager
argument_list|(
name|oldSm
argument_list|)
expr_stmt|;
name|File
name|dirs
index|[]
init|=
name|xsdOptions
index|[
name|x
index|]
operator|.
name|getDeleteDirs
argument_list|()
decl_stmt|;
if|if
condition|(
name|dirs
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|idx
init|=
literal|0
init|;
name|idx
operator|<
name|dirs
operator|.
name|length
condition|;
operator|++
name|idx
control|)
block|{
name|result
operator|=
name|result
operator|&&
name|deleteDir
argument_list|(
name|dirs
index|[
name|idx
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
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
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
operator|!
name|result
condition|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Could not delete redundant dirs"
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|project
operator|!=
literal|null
operator|&&
name|sourceRoot
operator|!=
literal|null
condition|)
block|{
name|project
operator|.
name|addCompileSourceRoot
argument_list|(
name|sourceRoot
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|project
operator|!=
literal|null
operator|&&
name|testSourceRoot
operator|!=
literal|null
condition|)
block|{
name|project
operator|.
name|addTestCompileSourceRoot
argument_list|(
name|testSourceRoot
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|String
index|[]
name|getArguments
parameter_list|(
name|XsdOption
name|option
parameter_list|,
name|String
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
if|if
condition|(
name|option
operator|.
name|getPackagename
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
literal|"-p"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|option
operator|.
name|getPackagename
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|option
operator|.
name|getBindingFile
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
literal|"-b"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|option
operator|.
name|getBindingFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|option
operator|.
name|getCatalog
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
literal|"-catalog"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|option
operator|.
name|getCatalog
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|option
operator|.
name|isExtension
argument_list|()
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
literal|"-extension"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|option
operator|.
name|getExtensionArgs
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Iterator
name|it
init|=
name|option
operator|.
name|getExtensionArgs
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|it
operator|.
name|next
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|getLog
argument_list|()
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
literal|"-verbose"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|list
operator|.
name|add
argument_list|(
literal|"-quiet"
argument_list|)
expr_stmt|;
block|}
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
name|list
operator|.
name|add
argument_list|(
name|option
operator|.
name|getXsd
argument_list|()
argument_list|)
expr_stmt|;
return|return
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
return|;
block|}
specifier|private
name|boolean
name|deleteDir
parameter_list|(
name|File
name|f
parameter_list|)
block|{
if|if
condition|(
name|f
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|File
name|files
index|[]
init|=
name|f
operator|.
name|listFiles
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|idx
init|=
literal|0
init|;
name|idx
operator|<
name|files
operator|.
name|length
condition|;
operator|++
name|idx
control|)
block|{
name|deleteDir
argument_list|(
name|files
index|[
name|idx
index|]
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|f
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return
name|f
operator|.
name|delete
argument_list|()
return|;
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

