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
name|corba
operator|.
name|maven
operator|.
name|plugins
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
name|URL
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
name|common
operator|.
name|classloader
operator|.
name|ClassLoaderUtils
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
name|corba
operator|.
name|WSDLToIDL
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
comment|/**  * @goal wsdl2idl  * @requiresDependencyResolution test  * @description CXF WSDL To IDL Tool  * @threadSafe  */
end_comment

begin_class
specifier|public
class|class
name|WSDLToIDLPlugin
extends|extends
name|AbstractMojo
block|{
comment|/**      * @parameter  expression="${project.build.directory}/generated/src/main/java"      * @required      */
name|File
name|outputDir
decl_stmt|;
comment|/**      * @parameter      */
name|WsdltoidlOption
name|wsdltoidlOptions
index|[]
decl_stmt|;
comment|/**      * @parameter expression="${project}"      * @required      */
name|MavenProject
name|project
decl_stmt|;
comment|/**      * Use the compile classpath rather than the test classpath for execution      * useful if the test dependencies clash with those of wsdl2java      * @parameter expression="${cxf.useCompileClasspath}" default-value="false"      */
name|boolean
name|useCompileClasspath
decl_stmt|;
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|MojoExecutionException
block|{
if|if
condition|(
name|outputDir
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"The outputDir must be specified"
argument_list|)
throw|;
block|}
if|if
condition|(
name|project
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"The project must be specified"
argument_list|)
throw|;
block|}
if|if
condition|(
name|wsdltoidlOptions
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Please specify the wsdltoidl options"
argument_list|)
throw|;
block|}
name|outputDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|URL
argument_list|>
name|urlList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
try|try
block|{
name|urlList
operator|.
name|add
argument_list|(
name|outputDir
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
name|outputDir
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
name|List
argument_list|<
name|?
argument_list|>
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
name|ClassLoader
name|loader
init|=
name|ClassLoaderUtils
operator|.
name|getURLClassLoader
argument_list|(
name|urlList
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
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|wsdltoidlOptions
operator|.
name|length
condition|;
name|x
operator|++
control|)
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|wsdltoidlOptions
index|[
name|x
index|]
operator|.
name|getWSDL
argument_list|()
argument_list|)
decl_stmt|;
name|File
name|doneFile
init|=
operator|new
name|File
argument_list|(
name|outputDir
argument_list|,
literal|"."
operator|+
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|".DONE"
argument_list|)
decl_stmt|;
name|boolean
name|doWork
init|=
name|file
operator|.
name|lastModified
argument_list|()
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
name|file
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
if|if
condition|(
name|doWork
condition|)
block|{
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
argument_list|)
expr_stmt|;
if|if
condition|(
name|wsdltoidlOptions
index|[
name|x
index|]
operator|.
name|isCorbaEnabled
argument_list|()
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
literal|"-corba"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|wsdltoidlOptions
index|[
name|x
index|]
operator|.
name|isIdlEnabled
argument_list|()
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
literal|"-idl"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|wsdltoidlOptions
index|[
name|x
index|]
operator|.
name|getExtraargs
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|list
operator|.
name|addAll
argument_list|(
name|wsdltoidlOptions
index|[
name|x
index|]
operator|.
name|getExtraargs
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|list
operator|.
name|add
argument_list|(
name|wsdltoidlOptions
index|[
name|x
index|]
operator|.
name|getWSDL
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|WSDLToIDL
operator|.
name|run
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
argument_list|)
expr_stmt|;
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
catch|catch
parameter_list|(
name|Throwable
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
if|if
condition|(
name|cp
operator|!=
literal|null
condition|)
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"java.class.path"
argument_list|,
name|cp
argument_list|)
expr_stmt|;
block|}
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
block|}
block|}
end_class

end_unit

