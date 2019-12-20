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
name|DataInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|DataOutputStream
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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|net
operator|.
name|URL
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
name|maven_plugin
operator|.
name|AbstractCodegenMojo
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
name|common
operator|.
name|ToolErrorListener
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
name|OutputStreamCreator
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugins
operator|.
name|annotations
operator|.
name|LifecyclePhase
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
name|plugins
operator|.
name|annotations
operator|.
name|Mojo
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
name|plugins
operator|.
name|annotations
operator|.
name|Parameter
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
name|plugins
operator|.
name|annotations
operator|.
name|ResolutionScope
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|plexus
operator|.
name|build
operator|.
name|incremental
operator|.
name|BuildContext
import|;
end_import

begin_class
annotation|@
name|Mojo
argument_list|(
name|name
operator|=
literal|"wsdl2java"
argument_list|,
name|defaultPhase
operator|=
name|LifecyclePhase
operator|.
name|GENERATE_SOURCES
argument_list|,
name|threadSafe
operator|=
literal|true
argument_list|,
name|requiresDependencyResolution
operator|=
name|ResolutionScope
operator|.
name|TEST
argument_list|)
specifier|public
class|class
name|WSDL2JavaMojo
extends|extends
name|AbstractCodegenMojo
block|{
specifier|final
class|class
name|MavenToolErrorListener
extends|extends
name|ToolErrorListener
block|{
specifier|private
specifier|final
name|List
argument_list|<
name|File
argument_list|>
name|errorfiles
decl_stmt|;
name|MavenToolErrorListener
parameter_list|(
name|List
argument_list|<
name|File
argument_list|>
name|errorfiles
parameter_list|)
block|{
name|this
operator|.
name|errorfiles
operator|=
name|errorfiles
expr_stmt|;
block|}
specifier|public
name|void
name|addError
parameter_list|(
specifier|final
name|String
name|file
parameter_list|,
name|int
name|line
parameter_list|,
name|int
name|column
parameter_list|,
name|String
name|message
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
name|super
operator|.
name|addError
argument_list|(
name|file
argument_list|,
name|line
argument_list|,
name|column
argument_list|,
name|message
argument_list|,
name|t
argument_list|)
expr_stmt|;
name|File
name|f
init|=
name|mapFile
argument_list|(
name|file
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|!=
literal|null
operator|&&
operator|!
name|errorfiles
operator|.
name|contains
argument_list|(
name|f
argument_list|)
condition|)
block|{
name|buildContext
operator|.
name|removeMessages
argument_list|(
name|f
argument_list|)
expr_stmt|;
name|errorfiles
operator|.
name|add
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|f
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|file
operator|==
literal|null
condition|)
block|{
name|f
operator|=
operator|new
name|File
argument_list|(
literal|"null"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|f
operator|=
operator|new
name|File
argument_list|(
name|file
argument_list|)
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
specifier|public
name|String
name|getAbsolutePath
parameter_list|()
block|{
return|return
name|file
return|;
block|}
block|}
expr_stmt|;
block|}
block|}
name|buildContext
operator|.
name|addMessage
argument_list|(
name|f
argument_list|,
name|line
argument_list|,
name|column
argument_list|,
name|message
argument_list|,
name|BuildContext
operator|.
name|SEVERITY_ERROR
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addWarning
parameter_list|(
specifier|final
name|String
name|file
parameter_list|,
name|int
name|line
parameter_list|,
name|int
name|column
parameter_list|,
name|String
name|message
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
name|File
name|f
init|=
name|mapFile
argument_list|(
name|file
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|!=
literal|null
operator|&&
operator|!
name|errorfiles
operator|.
name|contains
argument_list|(
name|f
argument_list|)
condition|)
block|{
name|buildContext
operator|.
name|removeMessages
argument_list|(
name|f
argument_list|)
expr_stmt|;
name|errorfiles
operator|.
name|add
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|f
operator|==
literal|null
condition|)
block|{
name|f
operator|=
operator|new
name|File
argument_list|(
name|file
argument_list|)
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
specifier|public
name|String
name|getAbsolutePath
parameter_list|()
block|{
return|return
name|file
return|;
block|}
block|}
expr_stmt|;
block|}
comment|//don't send to super which just LOG.warns.   We'll let Maven do that to
comment|//not duplicate the error message.
name|buildContext
operator|.
name|addMessage
argument_list|(
name|f
argument_list|,
name|line
argument_list|,
name|column
argument_list|,
name|message
argument_list|,
name|BuildContext
operator|.
name|SEVERITY_WARNING
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
specifier|private
name|File
name|mapFile
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|File
name|file
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
operator|&&
name|s
operator|.
name|startsWith
argument_list|(
literal|"file:"
argument_list|)
condition|)
block|{
if|if
condition|(
name|s
operator|.
name|contains
argument_list|(
literal|"#"
argument_list|)
condition|)
block|{
name|s
operator|=
name|s
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|s
operator|.
name|indexOf
argument_list|(
literal|'#'
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|file
operator|=
operator|new
name|File
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
comment|//ignore
block|}
block|}
return|return
name|file
return|;
block|}
block|}
annotation|@
name|Parameter
argument_list|(
name|property
operator|=
literal|"cxf.testSourceRoot"
argument_list|)
name|File
name|testSourceRoot
decl_stmt|;
comment|/**      * Path where the generated sources should be placed      *      */
annotation|@
name|Parameter
argument_list|(
name|required
operator|=
literal|true
argument_list|,
name|defaultValue
operator|=
literal|"${project.build.directory}/generated-sources/cxf"
argument_list|,
name|property
operator|=
literal|"cxf.sourceRoot"
argument_list|)
name|File
name|sourceRoot
decl_stmt|;
comment|/**      * Options that specify WSDLs to process and/or control the processing of wsdls.      * If you have enabled wsdl scanning, these elements attach options to particular wsdls.      * If you have not enabled wsdl scanning, these options call out the wsdls to process.      */
annotation|@
name|Parameter
name|WsdlOption
index|[]
name|wsdlOptions
decl_stmt|;
comment|/**      * Default options to be used when a wsdl has not had it's options explicitly specified.      */
annotation|@
name|Parameter
name|Option
name|defaultOptions
init|=
operator|new
name|Option
argument_list|()
decl_stmt|;
comment|/**      * Encoding to use for generated sources      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project.build.sourceEncoding}"
argument_list|)
name|String
name|encoding
decl_stmt|;
comment|/**      * Merge WsdlOptions that point to the same file by adding the extraargs to the first option and deleting      * the second from the options list      *      * @param effectiveWsdlOptions      */
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
argument_list|<>
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
else|else
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
comment|/**      * Determine if code should be generated from the given wsdl      *      * @param genericWsdlOption      * @param doneFile      * @param wsdlURI      * @return      */
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
name|getTimestamp
argument_list|(
name|wsdlURI
argument_list|)
decl_stmt|;
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
name|URI
index|[]
name|dependencies
init|=
name|wsdlOption
operator|.
name|getDependencyURIs
argument_list|(
name|project
operator|.
name|getBasedir
argument_list|()
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|dependencies
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
name|dependencies
operator|.
name|length
condition|;
operator|++
name|z
control|)
block|{
name|long
name|dependencyTimestamp
init|=
name|getTimestamp
argument_list|(
name|dependencies
index|[
name|z
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|dependencyTimestamp
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
break|break;
block|}
block|}
block|}
block|}
if|if
condition|(
operator|!
name|doWork
condition|)
block|{
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
name|String
name|options
init|=
name|wsdlOption
operator|.
name|generateCommandLine
argument_list|(
literal|null
argument_list|,
name|basedir
argument_list|,
name|wsdlURI
argument_list|,
literal|false
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
try|try
init|(
name|DataInputStream
name|reader
init|=
operator|new
name|DataInputStream
argument_list|(
name|Files
operator|.
name|newInputStream
argument_list|(
name|doneFile
operator|.
name|toPath
argument_list|()
argument_list|)
argument_list|)
init|)
block|{
name|String
name|s
init|=
name|reader
operator|.
name|readUTF
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|options
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|doWork
operator|=
literal|true
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//ignore
block|}
block|}
return|return
name|doWork
return|;
block|}
specifier|protected
name|void
name|createMarkerFile
parameter_list|(
name|GenericWsdlOption
name|wsdlOption
parameter_list|,
name|File
name|doneFile
parameter_list|,
name|URI
name|wsdlURI
parameter_list|)
throws|throws
name|IOException
block|{
name|doneFile
operator|.
name|createNewFile
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
name|String
name|options
init|=
name|wsdlOption
operator|.
name|generateCommandLine
argument_list|(
literal|null
argument_list|,
name|basedir
argument_list|,
name|wsdlURI
argument_list|,
literal|false
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
try|try
init|(
name|DataOutputStream
name|writer
init|=
operator|new
name|DataOutputStream
argument_list|(
name|Files
operator|.
name|newOutputStream
argument_list|(
name|doneFile
operator|.
name|toPath
argument_list|()
argument_list|)
argument_list|)
init|)
block|{
name|writer
operator|.
name|writeUTF
argument_list|(
name|options
argument_list|)
expr_stmt|;
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Finds the timestamp for a given URI. Calls {@link #getBaseFileURI(URI)} prior to the timestamp      * check in order to handle "classpath" and "jar" URIs.      *      * @param uri the URI to timestamp      * @return a timestamp      */
specifier|protected
name|long
name|getTimestamp
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
name|long
name|timestamp
init|=
literal|0
decl_stmt|;
name|URI
name|baseURI
init|=
name|getBaseFileURI
argument_list|(
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"file"
operator|.
name|equals
argument_list|(
name|baseURI
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
name|baseURI
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
name|baseURI
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
return|return
name|timestamp
return|;
block|}
comment|/**      * Finds the base file URI that 'contains' the given URI. If the URI can not be resolved to a file URI      * then the original URI is returned. This method currently attempts to resolve only "classpath" and      * "jar" URIs.      *      * @param uri the URI to resolve      * @return uri a file URI if the original URI is contained in a file, otherwise the original URI      */
specifier|protected
name|URI
name|getBaseFileURI
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
if|if
condition|(
literal|"classpath"
operator|.
name|equals
argument_list|(
name|uri
operator|.
name|getScheme
argument_list|()
argument_list|)
condition|)
block|{
name|URL
name|resource
init|=
name|ClassLoaderUtils
operator|.
name|getResource
argument_list|(
name|uri
operator|.
name|toString
argument_list|()
operator|.
name|substring
argument_list|(
literal|10
argument_list|)
argument_list|,
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|resource
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
name|getBaseFileURI
argument_list|(
name|resource
operator|.
name|toURI
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
elseif|else
if|if
condition|(
literal|"jar"
operator|.
name|equals
argument_list|(
name|uri
operator|.
name|getScheme
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|jarUrl
init|=
name|uri
operator|.
name|toString
argument_list|()
decl_stmt|;
name|int
name|embeddedUrlEndIndex
init|=
name|jarUrl
operator|.
name|lastIndexOf
argument_list|(
literal|"!/"
argument_list|)
decl_stmt|;
if|if
condition|(
name|embeddedUrlEndIndex
operator|!=
operator|-
literal|1
condition|)
block|{
name|String
name|embeddedUrl
init|=
name|jarUrl
operator|.
name|substring
argument_list|(
literal|4
argument_list|,
name|embeddedUrlEndIndex
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|getBaseFileURI
argument_list|(
operator|new
name|URI
argument_list|(
name|embeddedUrl
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
return|return
name|uri
return|;
block|}
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|generateCommandLine
parameter_list|(
name|GenericWsdlOption
name|wsdlOption
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
name|List
argument_list|<
name|String
argument_list|>
name|ret
init|=
name|super
operator|.
name|generateCommandLine
argument_list|(
name|wsdlOption
argument_list|)
decl_stmt|;
if|if
condition|(
name|encoding
operator|!=
literal|null
condition|)
block|{
name|ret
operator|.
name|add
argument_list|(
literal|0
argument_list|,
literal|"-encoding"
argument_list|)
expr_stmt|;
name|ret
operator|.
name|add
argument_list|(
literal|1
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
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
try|try
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|getBaseFileURI
argument_list|(
name|wsdlURI
argument_list|)
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
name|buildContext
operator|.
name|removeMessages
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//ignore
block|}
if|if
condition|(
name|wsdlOption
operator|.
name|getDependencies
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|URI
name|dependency
range|:
name|wsdlOption
operator|.
name|getDependencyURIs
argument_list|(
name|project
operator|.
name|getBasedir
argument_list|()
operator|.
name|toURI
argument_list|()
argument_list|)
control|)
block|{
name|URI
name|baseDependency
init|=
name|getBaseFileURI
argument_list|(
name|dependency
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"file"
operator|.
name|equals
argument_list|(
name|baseDependency
operator|.
name|getScheme
argument_list|()
argument_list|)
condition|)
block|{
name|buildContext
operator|.
name|removeMessages
argument_list|(
operator|new
name|File
argument_list|(
name|baseDependency
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
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
if|if
condition|(
name|encoding
operator|!=
literal|null
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
literal|0
argument_list|,
literal|"-encoding"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|1
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
block|}
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
literal|0
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
argument_list|<>
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
literal|"Unable to find (null) file for artifact "
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
name|ToolContext
name|ctx
init|=
operator|new
name|ToolContext
argument_list|()
decl_stmt|;
specifier|final
name|List
argument_list|<
name|File
argument_list|>
name|files
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
name|List
argument_list|<
name|File
argument_list|>
name|errorfiles
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|ctx
operator|.
name|put
argument_list|(
name|OutputStreamCreator
operator|.
name|class
argument_list|,
operator|new
name|OutputStreamCreator
argument_list|()
block|{
specifier|public
name|OutputStream
name|createOutputStream
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
name|files
operator|.
name|add
argument_list|(
name|file
argument_list|)
expr_stmt|;
return|return
name|buildContext
operator|.
name|newFileOutputStream
argument_list|(
name|file
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|setErrorListener
argument_list|(
operator|new
name|MavenToolErrorListener
argument_list|(
name|errorfiles
argument_list|)
argument_list|)
expr_stmt|;
operator|new
name|WSDLToJava
argument_list|(
name|args
argument_list|)
operator|.
name|run
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|File
argument_list|>
name|oldFiles
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|buildContext
operator|.
name|getValue
argument_list|(
literal|"cxf.file.list."
operator|+
name|doneFile
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldFiles
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|File
name|f
range|:
name|oldFiles
control|)
block|{
if|if
condition|(
operator|!
name|files
operator|.
name|contains
argument_list|(
name|f
argument_list|)
condition|)
block|{
name|f
operator|.
name|delete
argument_list|()
expr_stmt|;
name|buildContext
operator|.
name|refresh
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|buildContext
operator|.
name|setValue
argument_list|(
literal|"cxf.file.list."
operator|+
name|doneFile
operator|.
name|getName
argument_list|()
argument_list|,
name|files
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|buildContext
operator|.
name|setValue
argument_list|(
literal|"cxf.file.list."
operator|+
name|doneFile
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
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
if|if
condition|(
name|e
operator|instanceof
name|RuntimeException
condition|)
block|{
throw|throw
operator|(
name|RuntimeException
operator|)
name|e
throw|;
block|}
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
name|createMarkerFile
argument_list|(
name|wsdlOption
argument_list|,
name|doneFile
argument_list|,
name|wsdlURI
argument_list|)
expr_stmt|;
name|buildContext
operator|.
name|refresh
argument_list|(
name|doneFile
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
name|buildContext
operator|.
name|refresh
argument_list|(
name|getGeneratedSourceRoot
argument_list|()
operator|.
name|getAbsoluteFile
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
name|buildContext
operator|.
name|refresh
argument_list|(
name|getGeneratedTestRoot
argument_list|()
operator|.
name|getAbsoluteFile
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
argument_list|<>
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

