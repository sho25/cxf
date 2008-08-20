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
name|net
operator|.
name|URLClassLoader
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
name|HashMap
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
name|Map
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

begin_comment
comment|/**  * @goal wsdl2java  * @description CXF WSDL To Java Tool  * @requiresDependencyResolution test */
end_comment

begin_class
specifier|public
class|class
name|WSDL2JavaMojo
extends|extends
name|AbstractMojo
block|{
comment|/**      * @parameter expression="${cxf.testSourceRoot}"      */
name|File
name|testSourceRoot
decl_stmt|;
comment|/**      * @parameter expression="${cxf.sourceRoot}"       *             default-value="${project.build.directory}/generated/src/main/java"      * @required      */
name|File
name|sourceRoot
decl_stmt|;
comment|/**      * @parameter expression="${project.build.outputDirectory}"      * @required      */
name|String
name|classesDirectory
decl_stmt|;
comment|/**      * @parameter expression="${project}"      * @required      */
name|MavenProject
name|project
decl_stmt|;
comment|/**      * Default options to be used when a wsdl has not had it's options explicitly specified.      * @parameter      */
name|Option
name|defaultOptions
decl_stmt|;
comment|/**      * @parameter      */
name|WsdlOption
name|wsdlOptions
index|[]
decl_stmt|;
comment|/**      * @parameter expression="${cxf.wsdlRoot}" default-value="${basedir}/src/main/resources/wsdl"      */
name|File
name|wsdlRoot
decl_stmt|;
comment|/**      * @parameter expression="${cxf.testWsdlRoot}" default-value="${basedir}/src/test/resources/wsdl"      */
name|File
name|testWsdlRoot
decl_stmt|;
comment|/**      * Directory in which the "DONE" markers are saved that       * @parameter expression="${cxf.markerDirectory}"       *            default-value="${project.build.directory}/cxf-codegen-plugin-markers"      */
name|File
name|markerDirectory
decl_stmt|;
comment|/**      * Use the compile classpath rather than the test classpath for execution      * useful if the test dependencies clash with those of wsdl2java      * @parameter expression="${cxf.useCompileClasspath}" default-value="false"      */
name|boolean
name|useCompileClasspath
decl_stmt|;
comment|/**      * A list of wsdl files to include. Can contain ant-style wildcards and double wildcards.        * Defaults to *.wsdl      * @parameter      */
name|String
name|includes
index|[]
decl_stmt|;
comment|/**      * A list of wsdl files to exclude. Can contain ant-style wildcards and double wildcards.        * @parameter      */
name|String
name|excludes
index|[]
decl_stmt|;
specifier|private
name|List
argument_list|<
name|WsdlOption
argument_list|>
name|getWsdlOptionsFromDir
parameter_list|(
specifier|final
name|File
name|root
parameter_list|,
specifier|final
name|File
name|output
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
name|List
argument_list|<
name|WsdlOption
argument_list|>
name|options
init|=
operator|new
name|ArrayList
argument_list|<
name|WsdlOption
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|WsdlOption
name|o
range|:
operator|new
name|WsdlOptionLoader
argument_list|()
operator|.
name|load
argument_list|(
name|root
argument_list|,
name|includes
argument_list|,
name|excludes
argument_list|,
name|defaultOptions
argument_list|)
control|)
block|{
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
name|output
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|options
operator|.
name|contains
argument_list|(
name|o
argument_list|)
condition|)
block|{
name|options
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|options
return|;
block|}
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|MojoExecutionException
block|{
if|if
condition|(
name|includes
operator|==
literal|null
condition|)
block|{
name|includes
operator|=
operator|new
name|String
index|[]
block|{
literal|"*.wsdl"
block|}
expr_stmt|;
block|}
name|File
name|classesDir
init|=
operator|new
name|File
argument_list|(
name|classesDirectory
argument_list|)
decl_stmt|;
name|classesDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|markerDirectory
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|WsdlOption
argument_list|>
name|options
init|=
operator|new
name|ArrayList
argument_list|<
name|WsdlOption
argument_list|>
argument_list|()
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
condition|)
block|{
name|options
operator|.
name|addAll
argument_list|(
name|getWsdlOptionsFromDir
argument_list|(
name|wsdlRoot
argument_list|,
name|sourceRoot
argument_list|)
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
condition|)
block|{
name|options
operator|.
name|addAll
argument_list|(
name|getWsdlOptionsFromDir
argument_list|(
name|testWsdlRoot
argument_list|,
name|testSourceRoot
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|wsdlOptions
operator|!=
literal|null
condition|)
block|{
name|File
name|outputDirFile
init|=
name|testSourceRoot
operator|==
literal|null
condition|?
name|sourceRoot
else|:
name|testSourceRoot
decl_stmt|;
for|for
control|(
name|WsdlOption
name|o
range|:
name|wsdlOptions
control|)
block|{
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
operator|new
name|File
argument_list|(
name|project
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|o
operator|.
name|getWsdl
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
for|for
control|(
name|WsdlOption
name|o2
range|:
name|options
control|)
block|{
name|File
name|file2
init|=
operator|new
name|File
argument_list|(
name|o2
operator|.
name|getWsdl
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|file2
operator|.
name|exists
argument_list|()
operator|&&
name|file2
operator|.
name|equals
argument_list|(
name|file
argument_list|)
condition|)
block|{
name|o
operator|.
name|getExtraargs
argument_list|()
operator|.
name|addAll
argument_list|(
literal|0
argument_list|,
name|o2
operator|.
name|getExtraargs
argument_list|()
argument_list|)
expr_stmt|;
name|options
operator|.
name|remove
argument_list|(
name|o2
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
name|options
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
block|}
name|wsdlOptions
operator|=
name|options
operator|.
name|toArray
argument_list|(
operator|new
name|WsdlOption
index|[
name|options
operator|.
name|size
argument_list|()
index|]
argument_list|)
expr_stmt|;
if|if
condition|(
name|wsdlOptions
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Nothing to generate"
argument_list|)
expr_stmt|;
return|return;
block|}
name|List
argument_list|<
name|URL
argument_list|>
name|urlList
init|=
operator|new
name|ArrayList
argument_list|<
name|URL
argument_list|>
argument_list|()
decl_stmt|;
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
try|try
block|{
name|urlList
operator|.
name|add
argument_list|(
name|classesDir
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
comment|//ignore
block|}
name|buf
operator|.
name|append
argument_list|(
name|classesDir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|File
operator|.
name|pathSeparatorChar
argument_list|)
expr_stmt|;
name|List
name|artifacts
init|=
name|useCompileClasspath
condition|?
name|project
operator|.
name|getCompileArtifacts
argument_list|()
else|:
name|project
operator|.
name|getTestArtifacts
argument_list|()
decl_stmt|;
for|for
control|(
name|Artifact
name|a
range|:
name|CastUtils
operator|.
name|cast
argument_list|(
name|artifacts
argument_list|,
name|Artifact
operator|.
name|class
argument_list|)
control|)
block|{
try|try
block|{
if|if
condition|(
name|a
operator|.
name|getFile
argument_list|()
operator|!=
literal|null
operator|&&
name|a
operator|.
name|getFile
argument_list|()
operator|.
name|exists
argument_list|()
condition|)
block|{
name|urlList
operator|.
name|add
argument_list|(
name|a
operator|.
name|getFile
argument_list|()
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|a
operator|.
name|getFile
argument_list|()
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|File
operator|.
name|pathSeparatorChar
argument_list|)
expr_stmt|;
comment|//System.out.println("     " + a.getFile().getAbsolutePath());
block|}
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
comment|//ignore
block|}
block|}
name|ClassLoader
name|origContext
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
name|URLClassLoader
name|loader
init|=
operator|new
name|URLClassLoader
argument_list|(
name|urlList
operator|.
name|toArray
argument_list|(
operator|new
name|URL
index|[
name|urlList
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|,
name|origContext
argument_list|)
decl_stmt|;
name|String
name|newCp
init|=
name|buf
operator|.
name|toString
argument_list|()
decl_stmt|;
comment|//with some VM's, creating an XML parser (which we will do to parse wsdls)
comment|//will set some system properties that then interferes with mavens
comment|//dependency resolution.  (OSX is the major culprit here)
comment|//We'll save the props and then set them back later.
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|origProps
init|=
operator|new
name|HashMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|(
name|System
operator|.
name|getProperties
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|cp
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.class.path"
argument_list|)
decl_stmt|;
name|boolean
name|result
init|=
literal|true
decl_stmt|;
try|try
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|loader
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"java.class.path"
argument_list|,
name|newCp
argument_list|)
expr_stmt|;
for|for
control|(
name|WsdlOption
name|o
range|:
name|wsdlOptions
control|)
block|{
name|processWsdl
argument_list|(
name|o
argument_list|)
expr_stmt|;
name|File
name|dirs
index|[]
init|=
name|o
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
finally|finally
block|{
comment|//cleanup as much as we can.
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|getDefaultBus
argument_list|(
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|origContext
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"java.class.path"
argument_list|,
name|cp
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|newProps
init|=
operator|new
name|HashMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|(
name|System
operator|.
name|getProperties
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|newProps
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|origProps
operator|.
name|containsKey
argument_list|(
name|o
argument_list|)
condition|)
block|{
name|System
operator|.
name|clearProperty
argument_list|(
name|o
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|System
operator|.
name|getProperties
argument_list|()
operator|.
name|putAll
argument_list|(
name|origProps
argument_list|)
expr_stmt|;
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
operator|&&
name|sourceRoot
operator|.
name|exists
argument_list|()
condition|)
block|{
name|project
operator|.
name|addCompileSourceRoot
argument_list|(
name|sourceRoot
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
name|testSourceRoot
operator|!=
literal|null
operator|&&
name|testSourceRoot
operator|.
name|exists
argument_list|()
condition|)
block|{
name|project
operator|.
name|addTestCompileSourceRoot
argument_list|(
name|testSourceRoot
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|gc
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|processWsdl
parameter_list|(
name|WsdlOption
name|wsdlOption
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
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
name|String
name|wsdlLocation
init|=
name|wsdlOption
operator|.
name|getWsdl
argument_list|()
decl_stmt|;
name|File
name|wsdlFile
init|=
operator|new
name|File
argument_list|(
name|wsdlLocation
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
name|wsdlURI
decl_stmt|;
if|if
condition|(
name|wsdlFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|wsdlURI
operator|=
name|wsdlFile
operator|.
name|toURI
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|wsdlURI
operator|=
name|basedir
operator|.
name|resolve
argument_list|(
name|wsdlLocation
argument_list|)
expr_stmt|;
block|}
name|String
name|doneFileName
init|=
name|wsdlURI
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
comment|//ignore
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
name|isDefServiceName
argument_list|(
name|wsdlOption
argument_list|)
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
if|if
condition|(
name|doWork
condition|)
block|{
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
name|generateCommandLine
argument_list|(
name|wsdlOption
argument_list|,
name|outputDirFile
argument_list|,
name|basedir
argument_list|,
name|wsdlURI
argument_list|)
decl_stmt|;
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Calling wsdl2java with args: "
operator|+
name|list
argument_list|)
expr_stmt|;
try|try
block|{
operator|new
name|WSDLToJava
argument_list|(
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
block|}
block|}
block|}
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|generateCommandLine
parameter_list|(
name|WsdlOption
name|wsdlOption
parameter_list|,
name|File
name|outputDirFile
parameter_list|,
name|URI
name|basedir
parameter_list|,
name|URI
name|wsdlURI
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
name|wsdlOption
operator|.
name|getPackagenames
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Iterator
name|it
init|=
name|wsdlOption
operator|.
name|getPackagenames
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
literal|"-p"
argument_list|)
expr_stmt|;
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
name|wsdlOption
operator|.
name|getNamespaceExcludes
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Iterator
name|it
init|=
name|wsdlOption
operator|.
name|getNamespaceExcludes
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
literal|"-nexclude"
argument_list|)
expr_stmt|;
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
comment|// -d specify the dir for generated source code
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
name|outputDirFile
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|binding
range|:
name|wsdlOption
operator|.
name|getBindingFiles
argument_list|()
control|)
block|{
name|File
name|bindingFile
init|=
operator|new
name|File
argument_list|(
name|binding
argument_list|)
decl_stmt|;
name|URI
name|bindingURI
decl_stmt|;
if|if
condition|(
name|bindingFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|bindingURI
operator|=
name|bindingFile
operator|.
name|toURI
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|bindingURI
operator|=
name|basedir
operator|.
name|resolve
argument_list|(
name|binding
argument_list|)
expr_stmt|;
block|}
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
name|bindingURI
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|wsdlOption
operator|.
name|getFrontEnd
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
literal|"-fe"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|wsdlOption
operator|.
name|getFrontEnd
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|wsdlOption
operator|.
name|getDataBinding
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
literal|"-db"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|wsdlOption
operator|.
name|getDataBinding
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|wsdlOption
operator|.
name|getWsdlVersion
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
literal|"-wv"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|wsdlOption
operator|.
name|getWsdlVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|wsdlOption
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
name|wsdlOption
operator|.
name|getCatalog
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|wsdlOption
operator|.
name|isExtendedSoapHeaders
argument_list|()
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
literal|"-exsh"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|wsdlOption
operator|.
name|isValidateWsdl
argument_list|()
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
literal|"-validate"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|wsdlOption
operator|.
name|getDefaultExcludesNamespace
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
literal|"-dex"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|wsdlOption
operator|.
name|getDefaultExcludesNamespace
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|wsdlOption
operator|.
name|getDefaultNamespacePackageMapping
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
literal|"-dns"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|wsdlOption
operator|.
name|getDefaultNamespacePackageMapping
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|wsdlOption
operator|.
name|getServiceName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
literal|"-sn"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|wsdlOption
operator|.
name|getServiceName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|wsdlOption
operator|.
name|isAutoNameResolution
argument_list|()
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
literal|"-autoNameResolution"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|wsdlOption
operator|.
name|isNoAddressBinding
argument_list|()
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
literal|"-noAddressBinding"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|wsdlOption
operator|.
name|getExtraargs
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Iterator
name|it
init|=
name|wsdlOption
operator|.
name|getExtraargs
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
name|Object
name|value
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|value
operator|=
literal|""
expr_stmt|;
comment|// Maven makes empty tags into null
comment|// instead of empty strings.
block|}
name|list
operator|.
name|add
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|wsdlOption
operator|.
name|isSetWsdlLocation
argument_list|()
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
literal|"-wsdlLocation"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|wsdlOption
operator|.
name|getWsdlLocation
argument_list|()
operator|==
literal|null
condition|?
literal|""
else|:
name|wsdlOption
operator|.
name|getWsdlLocation
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|list
operator|.
name|add
argument_list|(
name|wsdlURI
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|list
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
specifier|private
name|boolean
name|isDefServiceName
parameter_list|(
name|WsdlOption
name|wsdlOption
parameter_list|)
block|{
name|List
name|args
init|=
name|wsdlOption
operator|.
name|extraargs
decl_stmt|;
if|if
condition|(
name|args
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
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
name|args
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
literal|"-sn"
operator|.
name|equalsIgnoreCase
argument_list|(
operator|(
name|String
operator|)
name|args
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

