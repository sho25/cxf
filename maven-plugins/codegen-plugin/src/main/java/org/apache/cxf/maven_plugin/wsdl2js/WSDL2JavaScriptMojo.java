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
name|wsdl2js
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
name|net
operator|.
name|URISyntaxException
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
name|Collections
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
name|helpers
operator|.
name|CastUtils
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
name|maven_plugin
operator|.
name|WsdlUtilities
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
name|javascript
operator|.
name|WSDLToJavaScript
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

begin_comment
comment|/**  * @goal wsdl2js  * @phase generate-sources  * @description CXF WSDL To JavaScript Tool  * @requiresDependencyResolution test  * @threadSafe  */
end_comment

begin_class
specifier|public
class|class
name|WSDL2JavaScriptMojo
extends|extends
name|AbstractCodegenMoho
block|{
comment|/**      * WSDL files to process. Each wsdl file is specified as an option element, with possible options.      *       * @parameter      * @required      */
name|WsdlOption
index|[]
name|wsdls
decl_stmt|;
comment|/**      * @parameter expression="${cxf.testJavascriptRoot}"      */
name|File
name|testSourceRoot
decl_stmt|;
comment|/**      * Path where the generated sources should be placed      *       * @parameter expression="${cxf.sourceJavascriptRoot}"      *            default-value="${project.build.directory}/generated-sources/cxf-js"      * @required      */
name|File
name|sourceRoot
decl_stmt|;
comment|/**      * Default options to be applied to all of the wsdls.      *       * @parameter      */
name|Option
name|defaultOptions
decl_stmt|;
comment|/**      * @parameter      */
name|WsdlOption
name|wsdlOptions
index|[]
decl_stmt|;
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
decl_stmt|;
try|try
block|{
name|wsdlURI
operator|=
operator|new
name|URI
argument_list|(
name|wsdlOption
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Failed to get URI for wsdl "
operator|+
name|wsdlOption
operator|.
name|getUri
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|File
name|doneFile
init|=
name|getDoneFile
argument_list|(
name|basedir
argument_list|,
name|wsdlURI
argument_list|,
literal|"js"
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
operator|(
name|String
index|[]
operator|)
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
literal|"Calling wsdl2js with args: "
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
name|WSDLToJavaScript
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
name|WSDLToJavaScript
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
for|for
control|(
name|GenericWsdlOption
name|wo
range|:
name|wsdls
control|)
block|{
name|WsdlOption
name|option
init|=
operator|(
name|WsdlOption
operator|)
name|wo
decl_stmt|;
name|option
operator|.
name|merge
argument_list|(
name|defaultOptions
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
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
name|List
argument_list|<
name|GenericWsdlOption
argument_list|>
name|temp
decl_stmt|;
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
name|loadWsdlOptionsFromFiles
argument_list|(
name|wsdlRoot
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
name|loadWsdlOptionsFromFiles
argument_list|(
name|testWsdlRoot
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
name|loadWsdlOptionsFromDependencies
argument_list|(
name|project
argument_list|,
name|defaultOptions
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
specifier|private
name|List
argument_list|<
name|GenericWsdlOption
argument_list|>
name|loadWsdlOptionsFromFiles
parameter_list|(
name|File
name|wsdlBasedir
parameter_list|,
name|File
name|defaultOutputDir
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
if|if
condition|(
name|wsdlBasedir
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
if|if
condition|(
operator|!
name|wsdlBasedir
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
name|wsdlBasedir
operator|+
literal|" does not exist"
argument_list|)
throw|;
block|}
name|List
argument_list|<
name|File
argument_list|>
name|wsdlFiles
init|=
name|WsdlUtilities
operator|.
name|getWsdlFiles
argument_list|(
name|wsdlBasedir
argument_list|,
name|includes
argument_list|,
name|excludes
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|GenericWsdlOption
argument_list|>
name|options
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
name|File
name|wsdl
range|:
name|wsdlFiles
control|)
block|{
name|WsdlOption
name|wsdlOption
init|=
operator|new
name|WsdlOption
argument_list|()
decl_stmt|;
name|wsdlOption
operator|.
name|setOutputDir
argument_list|(
name|defaultOutputDir
argument_list|)
expr_stmt|;
name|wsdlOption
operator|.
name|setUri
argument_list|(
name|wsdl
operator|.
name|toURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|options
operator|.
name|add
argument_list|(
name|wsdlOption
argument_list|)
expr_stmt|;
block|}
return|return
name|options
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|GenericWsdlOption
argument_list|>
name|loadWsdlOptionsFromDependencies
parameter_list|(
name|MavenProject
name|project
parameter_list|,
name|Option
name|defaultOptions
parameter_list|,
name|File
name|outputDir
parameter_list|)
block|{
name|List
argument_list|<
name|GenericWsdlOption
argument_list|>
name|options
init|=
operator|new
name|ArrayList
argument_list|<
name|GenericWsdlOption
argument_list|>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|Artifact
argument_list|>
name|dependencies
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|project
operator|.
name|getDependencyArtifacts
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Artifact
name|artifact
range|:
name|dependencies
control|)
block|{
name|WsdlOption
name|option
init|=
operator|new
name|WsdlOption
argument_list|()
decl_stmt|;
name|WsdlUtilities
operator|.
name|fillWsdlOptionFromArtifact
argument_list|(
operator|new
name|WsdlOption
argument_list|()
argument_list|,
name|artifact
argument_list|,
name|outputDir
argument_list|)
expr_stmt|;
if|if
condition|(
name|option
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|defaultOptions
operator|!=
literal|null
condition|)
block|{
name|option
operator|.
name|merge
argument_list|(
name|defaultOptions
argument_list|)
expr_stmt|;
block|}
name|options
operator|.
name|add
argument_list|(
name|option
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|options
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
name|ForkOnceWSDL2Javascript
operator|.
name|class
return|;
block|}
block|}
end_class

end_unit

