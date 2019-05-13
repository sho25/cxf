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
name|Collections
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

begin_class
specifier|public
specifier|final
class|class
name|WsdlOptionLoader
block|{
specifier|private
specifier|static
specifier|final
name|String
name|WSDL_OPTIONS
init|=
literal|"-options$"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|WSDL_BINDINGS
init|=
literal|"-binding-?\\d*.xml$"
decl_stmt|;
specifier|private
name|WsdlOptionLoader
parameter_list|()
block|{     }
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
argument_list|<>
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
name|generateWsdlOptionFromArtifact
argument_list|(
name|artifact
argument_list|,
name|outputDir
argument_list|)
decl_stmt|;
if|if
condition|(
name|option
operator|!=
literal|null
condition|)
block|{
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
specifier|private
specifier|static
name|WsdlOption
name|generateWsdlOptionFromArtifact
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|File
name|outputDir
parameter_list|)
block|{
name|WsdlOption
name|option
init|=
operator|new
name|WsdlOption
argument_list|()
decl_stmt|;
if|if
condition|(
name|WsdlUtilities
operator|.
name|fillWsdlOptionFromArtifact
argument_list|(
name|option
argument_list|,
name|artifact
argument_list|,
name|outputDir
argument_list|)
condition|)
block|{
return|return
name|option
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Scan files in a directory and generate one wsdlOption per file found. Extra args for code generation      * can be defined in a file that is named like the wsdl file and ends in -options. Binding files can be      * defined in files named like the wsdl file and end in -binding-*.xml      *      * @param wsdlBasedir      * @param includes file name patterns to include      * @param excludes file name patterns to exclude      * @param defaultOutputDir output directory that should be used if no special file is given      * @return list of one WsdlOption object for each wsdl found      * @throws MojoExecutionException      */
specifier|public
specifier|static
name|List
argument_list|<
name|GenericWsdlOption
argument_list|>
name|loadWsdlOptionsFromFiles
parameter_list|(
name|File
name|wsdlBasedir
parameter_list|,
name|String
index|[]
name|includes
parameter_list|,
name|String
index|[]
name|excludes
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
name|wsdlOptions
init|=
operator|new
name|ArrayList
argument_list|<>
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
name|generateWsdlOptionFromFile
argument_list|(
name|wsdl
argument_list|,
name|defaultOutputDir
argument_list|)
decl_stmt|;
if|if
condition|(
name|wsdlOption
operator|!=
literal|null
condition|)
block|{
name|wsdlOptions
operator|.
name|add
argument_list|(
name|wsdlOption
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|wsdlOptions
return|;
block|}
specifier|private
specifier|static
name|String
index|[]
name|readOptionsFromFile
parameter_list|(
name|File
name|dir
parameter_list|,
name|String
name|wsdlName
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
name|String
index|[]
name|noOptions
init|=
operator|new
name|String
index|[]
block|{}
decl_stmt|;
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
name|dir
argument_list|,
name|wsdlName
operator|+
name|WSDL_OPTIONS
argument_list|)
decl_stmt|;
if|if
condition|(
name|files
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|noOptions
return|;
block|}
name|File
name|optionsFile
init|=
name|files
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|optionsFile
operator|==
literal|null
operator|||
operator|!
name|optionsFile
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return
name|noOptions
return|;
block|}
try|try
block|{
name|List
argument_list|<
name|String
argument_list|>
name|lines
init|=
name|FileUtils
operator|.
name|readLines
argument_list|(
name|optionsFile
argument_list|)
decl_stmt|;
if|if
condition|(
name|lines
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|noOptions
return|;
block|}
return|return
name|lines
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|split
argument_list|(
literal|" "
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Error reading options from file "
operator|+
name|optionsFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|protected
specifier|static
name|WsdlOption
name|generateWsdlOptionFromFile
parameter_list|(
specifier|final
name|File
name|wsdl
parameter_list|,
name|File
name|defaultOutputDir
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
if|if
condition|(
name|wsdl
operator|==
literal|null
operator|||
operator|!
name|wsdl
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
specifier|final
name|String
name|wsdlFileName
init|=
name|wsdl
operator|.
name|getName
argument_list|()
decl_stmt|;
name|int
name|idx
init|=
name|wsdlFileName
operator|.
name|toLowerCase
argument_list|()
operator|.
name|lastIndexOf
argument_list|(
literal|".wsdl"
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|==
operator|-
literal|1
condition|)
block|{
name|idx
operator|=
name|wsdlFileName
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|idx
operator|==
operator|-
literal|1
condition|)
block|{
return|return
literal|null
return|;
block|}
specifier|final
name|WsdlOption
name|wsdlOption
init|=
operator|new
name|WsdlOption
argument_list|()
decl_stmt|;
specifier|final
name|String
name|wsdlName
init|=
name|wsdlFileName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
decl_stmt|;
specifier|final
name|String
index|[]
name|options
init|=
name|readOptionsFromFile
argument_list|(
name|wsdl
operator|.
name|getParentFile
argument_list|()
argument_list|,
name|wsdlName
argument_list|)
decl_stmt|;
if|if
condition|(
name|options
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|Collections
operator|.
name|addAll
argument_list|(
name|wsdlOption
operator|.
name|getExtraargs
argument_list|()
argument_list|,
name|options
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|File
argument_list|>
name|bindingFiles
init|=
name|FileUtils
operator|.
name|getFiles
argument_list|(
name|wsdl
operator|.
name|getParentFile
argument_list|()
argument_list|,
name|wsdlName
operator|+
name|WSDL_BINDINGS
argument_list|)
decl_stmt|;
if|if
condition|(
name|bindingFiles
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|File
name|binding
range|:
name|bindingFiles
control|)
block|{
name|wsdlOption
operator|.
name|addBindingFile
argument_list|(
name|binding
argument_list|)
expr_stmt|;
block|}
block|}
name|wsdlOption
operator|.
name|setWsdl
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
if|if
condition|(
name|wsdlOption
operator|.
name|getOutputDir
argument_list|()
operator|==
literal|null
condition|)
block|{
name|wsdlOption
operator|.
name|setOutputDir
argument_list|(
name|defaultOutputDir
argument_list|)
expr_stmt|;
block|}
return|return
name|wsdlOption
return|;
block|}
block|}
end_class

end_unit

