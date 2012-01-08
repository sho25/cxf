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
operator|.
name|wsdl2java
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|Bus
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
name|BusFactory
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
name|maven_plugin
operator|.
name|AbstractCodegenMoho
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
name|maven_plugin
operator|.
name|GenericWsdlOption
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
name|common
operator|.
name|ToolContext
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
name|wsdlto
operator|.
name|WSDLToJava
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
name|artifact
operator|.
name|Artifact
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

begin_comment
comment|/**  * @goal wsdl2java  * @phase generate-sources  * @description CXF WSDL To Java Tool  * @requiresDependencyResolution test  * @threadSafe  */
end_comment

begin_class
specifier|public
class|class
name|WSDL2JavaMojo
extends|extends
name|AbstractCodegenMoho
block|{
comment|/**      * @parameter expression="${cxf.testSourceRoot}"      */
name|File
name|testSourceRoot
decl_stmt|;
comment|/**      * Path where the generated sources should be placed      *       * @parameter expression="${cxf.sourceRoot}"      *            default-value="${project.build.directory}/generated-sources/cxf"      * @required      */
name|File
name|sourceRoot
decl_stmt|;
comment|/**      * Options that specify WSDLs to process and/or control the processing of wsdls.       * If you have enabled wsdl scanning, these elements attach options to particular wsdls.      * If you have not enabled wsdl scanning, these options call out the wsdls to process.       * @parameter      */
name|WsdlOption
name|wsdlOptions
index|[]
decl_stmt|;
comment|/**      * Default options to be used when a wsdl has not had it's options explicitly specified.      *       * @parameter      */
name|Option
name|defaultOptions
init|=
operator|new
name|Option
argument_list|()
decl_stmt|;
comment|/**      * Merge WsdlOptions that point to the same file by adding the extraargs to the first option and deleting      * the second from the options list      *       * @param options      */
specifier|protected
name|void
name|mergeOptions
parameter_list|(
name|List
argument_list|<
name|GenericWsdlOption
argument_list|>
name|effectiveWsdlOptions
parameter_list|)
block|{
name|File
name|outputDirFile
init|=
name|getGeneratedTestRoot
argument_list|()
operator|==
literal|null
condition|?
name|getGeneratedSourceRoot
argument_list|()
else|:
name|getGeneratedTestRoot
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|GenericWsdlOption
argument_list|>
name|newList
init|=
operator|new
name|ArrayList
argument_list|<
name|GenericWsdlOption
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|GenericWsdlOption
name|go
range|:
name|effectiveWsdlOptions
control|)
block|{
name|WsdlOption
name|o
init|=
operator|(
name|WsdlOption
operator|)
name|go
decl_stmt|;
if|if
condition|(
name|defaultOptions
operator|!=
literal|null
condition|)
block|{
name|o
operator|.
name|merge
argument_list|(
name|defaultOptions
argument_list|)
expr_stmt|;
block|}
comment|/*              * If not output dir at all, go for tests, and failing that, source.              */
if|if
condition|(
name|o
operator|.
name|getOutputDir
argument_list|()
operator|==
literal|null
condition|)
block|{
name|o
operator|.
name|setOutputDir
argument_list|(
name|outputDirFile
argument_list|)
expr_stmt|;
block|}
name|File
name|file
init|=
name|o
operator|.
name|getWsdlFile
argument_list|(
name|project
operator|.
name|getBasedir
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|!=
literal|null
operator|&&
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
name|file
operator|=
name|file
operator|.
name|getAbsoluteFile
argument_list|()
expr_stmt|;
name|boolean
name|duplicate
init|=
literal|false
decl_stmt|;
for|for
control|(
name|GenericWsdlOption
name|o2w
range|:
name|newList
control|)
block|{
name|WsdlOption
name|o2
init|=
operator|(
name|WsdlOption
operator|)
name|o2w
decl_stmt|;
name|File
name|file2
init|=
name|o2
operator|.
name|getWsdlFile
argument_list|(
name|project
operator|.
name|getBasedir
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|file2
operator|!=
literal|null
operator|&&
name|file2
operator|.
name|exists
argument_list|()
operator|&&
name|file2
operator|.
name|getAbsoluteFile
argument_list|()
operator|.
name|equals
argument_list|(
name|file
argument_list|)
condition|)
block|{
name|o2
operator|.
name|getExtraargs
argument_list|()
operator|.
name|addAll
argument_list|(
literal|0
argument_list|,
name|o
operator|.
name|getExtraargs
argument_list|()
argument_list|)
expr_stmt|;
name|duplicate
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|duplicate
condition|)
block|{
name|newList
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|effectiveWsdlOptions
operator|.
name|clear
argument_list|()
expr_stmt|;
name|effectiveWsdlOptions
operator|.
name|addAll
argument_list|(
name|newList
argument_list|)
expr_stmt|;
block|}
comment|/**      * Determine if code should be generated from the given wsdl      *       * @param wsdlOption      * @param doneFile      * @param wsdlURI      * @return      */
specifier|protected
name|boolean
name|shouldRun
parameter_list|(
name|GenericWsdlOption
name|genericWsdlOption
parameter_list|,
name|File
name|doneFile
parameter_list|,
name|URI
name|wsdlURI
parameter_list|)
block|{
name|WsdlOption
name|wsdlOption
init|=
operator|(
name|WsdlOption
operator|)
name|genericWsdlOption
decl_stmt|;
name|long
name|timestamp
init|=
literal|0
decl_stmt|;
if|if
condition|(
literal|"file"
operator|.
name|equals
argument_list|(
name|wsdlURI
operator|.
name|getScheme
argument_list|()
argument_list|)
condition|)
block|{
name|timestamp
operator|=
operator|new
name|File
argument_list|(
name|wsdlURI
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
name|timestamp
operator|=
name|wsdlURI
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
comment|// ignore
block|}
block|}
name|boolean
name|doWork
init|=
literal|false
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
name|timestamp
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
elseif|else
if|if
condition|(
name|wsdlOption
operator|.
name|isDefServiceName
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
name|wsdlOption
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
return|return
name|doWork
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Bus
name|generate
parameter_list|(
name|GenericWsdlOption
name|genericWsdlOption
parameter_list|,
name|Bus
name|bus
parameter_list|,
name|Set
argument_list|<
name|URI
argument_list|>
name|classPath
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
name|WsdlOption
name|wsdlOption
init|=
operator|(
name|WsdlOption
operator|)
name|genericWsdlOption
decl_stmt|;
name|File
name|outputDirFile
init|=
name|wsdlOption
operator|.
name|getOutputDir
argument_list|()
decl_stmt|;
name|outputDirFile
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
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
name|wsdlURI
init|=
name|wsdlOption
operator|.
name|getWsdlURI
argument_list|(
name|basedir
argument_list|)
decl_stmt|;
name|File
name|doneFile
init|=
name|getDoneFile
argument_list|(
name|basedir
argument_list|,
name|wsdlURI
argument_list|,
literal|"java"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|shouldRun
argument_list|(
name|wsdlOption
argument_list|,
name|doneFile
argument_list|,
name|wsdlURI
argument_list|)
condition|)
block|{
return|return
name|bus
return|;
block|}
name|doneFile
operator|.
name|delete
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
name|wsdlOption
operator|.
name|generateCommandLine
argument_list|(
name|outputDirFile
argument_list|,
name|basedir
argument_list|,
name|wsdlURI
argument_list|,
name|getLog
argument_list|()
operator|.
name|isDebugEnabled
argument_list|()
argument_list|)
decl_stmt|;
name|String
index|[]
name|args
init|=
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
decl_stmt|;
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Calling wsdl2java with args: "
operator|+
name|Arrays
operator|.
name|toString
argument_list|(
name|args
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
literal|"false"
operator|.
name|equals
argument_list|(
name|fork
argument_list|)
condition|)
block|{
name|Set
argument_list|<
name|URI
argument_list|>
name|artifactsPath
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|URI
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Artifact
name|a
range|:
name|pluginArtifacts
control|)
block|{
name|File
name|file
init|=
name|a
operator|.
name|getFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|file
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Unable to find "
operator|+
name|file
operator|+
literal|" for artifact "
operator|+
name|a
operator|.
name|getGroupId
argument_list|()
operator|+
literal|":"
operator|+
name|a
operator|.
name|getArtifactId
argument_list|()
operator|+
literal|":"
operator|+
name|a
operator|.
name|getVersion
argument_list|()
argument_list|)
throw|;
block|}
name|artifactsPath
operator|.
name|add
argument_list|(
name|file
operator|.
name|toURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|addPluginArtifact
argument_list|(
name|artifactsPath
argument_list|)
expr_stmt|;
name|artifactsPath
operator|.
name|addAll
argument_list|(
name|classPath
argument_list|)
expr_stmt|;
name|runForked
argument_list|(
name|artifactsPath
argument_list|,
name|WSDLToJava
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|bus
operator|==
literal|null
condition|)
block|{
name|bus
operator|=
name|BusFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createBus
argument_list|()
expr_stmt|;
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
try|try
block|{
operator|new
name|WSDLToJava
argument_list|(
name|args
argument_list|)
operator|.
name|run
argument_list|(
operator|new
name|ToolContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
name|e
argument_list|)
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
try|try
block|{
name|doneFile
operator|.
name|createNewFile
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Could not create marker file "
operator|+
name|doneFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Failed to create marker file "
operator|+
name|doneFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|project
operator|!=
literal|null
operator|&&
name|getGeneratedSourceRoot
argument_list|()
operator|!=
literal|null
operator|&&
name|getGeneratedSourceRoot
argument_list|()
operator|.
name|exists
argument_list|()
condition|)
block|{
name|project
operator|.
name|addCompileSourceRoot
argument_list|(
name|getGeneratedSourceRoot
argument_list|()
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|project
operator|!=
literal|null
operator|&&
name|getGeneratedTestRoot
argument_list|()
operator|!=
literal|null
operator|&&
name|getGeneratedTestRoot
argument_list|()
operator|.
name|exists
argument_list|()
condition|)
block|{
name|project
operator|.
name|addTestCompileSourceRoot
argument_list|(
name|getGeneratedTestRoot
argument_list|()
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|bus
return|;
block|}
comment|/**      * @return effective WsdlOptions      * @throws MojoExecutionException      */
specifier|protected
name|List
argument_list|<
name|GenericWsdlOption
argument_list|>
name|createWsdlOptionsFromScansAndExplicitWsdlOptions
parameter_list|()
throws|throws
name|MojoExecutionException
block|{
name|List
argument_list|<
name|GenericWsdlOption
argument_list|>
name|effectiveWsdlOptions
init|=
operator|new
name|ArrayList
argument_list|<
name|GenericWsdlOption
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|wsdlOptions
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|WsdlOption
name|wo
range|:
name|wsdlOptions
control|)
block|{
name|effectiveWsdlOptions
operator|.
name|add
argument_list|(
name|wo
argument_list|)
expr_stmt|;
block|}
block|}
name|List
argument_list|<
name|GenericWsdlOption
argument_list|>
name|temp
decl_stmt|;
if|if
condition|(
name|wsdlRoot
operator|!=
literal|null
operator|&&
name|wsdlRoot
operator|.
name|exists
argument_list|()
operator|&&
operator|!
name|disableDirectoryScan
condition|)
block|{
name|temp
operator|=
name|WsdlOptionLoader
operator|.
name|loadWsdlOptionsFromFiles
argument_list|(
name|wsdlRoot
argument_list|,
name|includes
argument_list|,
name|excludes
argument_list|,
name|getGeneratedSourceRoot
argument_list|()
argument_list|)
expr_stmt|;
name|effectiveWsdlOptions
operator|.
name|addAll
argument_list|(
name|temp
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|testWsdlRoot
operator|!=
literal|null
operator|&&
name|testWsdlRoot
operator|.
name|exists
argument_list|()
operator|&&
operator|!
name|disableDirectoryScan
condition|)
block|{
name|temp
operator|=
name|WsdlOptionLoader
operator|.
name|loadWsdlOptionsFromFiles
argument_list|(
name|testWsdlRoot
argument_list|,
name|includes
argument_list|,
name|excludes
argument_list|,
name|getGeneratedTestRoot
argument_list|()
argument_list|)
expr_stmt|;
name|effectiveWsdlOptions
operator|.
name|addAll
argument_list|(
name|temp
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|disableDependencyScan
condition|)
block|{
name|temp
operator|=
name|WsdlOptionLoader
operator|.
name|loadWsdlOptionsFromDependencies
argument_list|(
name|project
argument_list|,
name|getGeneratedSourceRoot
argument_list|()
argument_list|)
expr_stmt|;
name|effectiveWsdlOptions
operator|.
name|addAll
argument_list|(
name|temp
argument_list|)
expr_stmt|;
block|}
name|mergeOptions
argument_list|(
name|effectiveWsdlOptions
argument_list|)
expr_stmt|;
name|downloadRemoteWsdls
argument_list|(
name|effectiveWsdlOptions
argument_list|)
expr_stmt|;
return|return
name|effectiveWsdlOptions
return|;
block|}
annotation|@
name|Override
specifier|protected
name|File
name|getGeneratedSourceRoot
parameter_list|()
block|{
return|return
name|sourceRoot
return|;
block|}
annotation|@
name|Override
specifier|protected
name|File
name|getGeneratedTestRoot
parameter_list|()
block|{
return|return
name|testSourceRoot
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|getForkClass
parameter_list|()
block|{
return|return
name|ForkOnceWSDL2Java
operator|.
name|class
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|MojoExecutionException
block|{
name|defaultOptions
operator|.
name|addDefaultBindingFileIfExists
argument_list|(
name|project
operator|.
name|getBasedir
argument_list|()
argument_list|)
expr_stmt|;
name|super
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|getMarkerSuffix
parameter_list|()
block|{
return|return
literal|"java"
return|;
block|}
block|}
end_class

end_unit

