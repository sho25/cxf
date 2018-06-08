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
name|wadlto
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
name|IOException
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
name|common
operator|.
name|DocumentArtifact
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
name|OptionLoader
block|{
specifier|private
specifier|static
specifier|final
name|String
name|WADL_TYPE
init|=
literal|"wadl"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|WADL_OPTIONS
init|=
literal|"-options$"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|WADL_BINDINGS
init|=
literal|"-binding-?\\d*.xml$"
decl_stmt|;
specifier|private
name|OptionLoader
parameter_list|()
block|{     }
specifier|public
specifier|static
name|List
argument_list|<
name|WadlOption
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
name|WadlOption
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
name|project
operator|.
name|getDependencyArtifacts
argument_list|()
decl_stmt|;
for|for
control|(
name|Artifact
name|artifact
range|:
name|dependencies
control|)
block|{
name|WadlOption
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
specifier|private
specifier|static
name|WadlOption
name|generateWsdlOptionFromArtifact
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|File
name|outputDir
parameter_list|)
block|{
if|if
condition|(
operator|!
name|WADL_TYPE
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|WadlOption
name|option
init|=
operator|new
name|WadlOption
argument_list|()
decl_stmt|;
name|DocumentArtifact
name|wsdlArtifact
init|=
operator|new
name|DocumentArtifact
argument_list|()
decl_stmt|;
name|wsdlArtifact
operator|.
name|setArtifactId
argument_list|(
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|wsdlArtifact
operator|.
name|setGroupId
argument_list|(
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|wsdlArtifact
operator|.
name|setType
argument_list|(
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|wsdlArtifact
operator|.
name|setVersion
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|option
operator|.
name|setWadlArtifact
argument_list|(
name|wsdlArtifact
argument_list|)
expr_stmt|;
name|option
operator|.
name|setOutputDir
argument_list|(
name|outputDir
argument_list|)
expr_stmt|;
return|return
name|option
return|;
block|}
comment|/**      * Scan files in a directory and generate one wadlOption per file found. Extra args for code generation      * can be defined in a file that is named like the wadl file and ends in -options. Binding files can be      * defined in files named like the wadl file and end in -binding-*.xml      *      * @param wadlBasedir      * @param includes file name patterns to include      * @param excludes file name patterns to exclude      * @param defaultOptions options that should be used if no special file is given      * @return list of one WadlOption object for each wadl found      * @throws MojoExecutionException      */
specifier|public
specifier|static
name|List
argument_list|<
name|WadlOption
argument_list|>
name|loadWadlOptionsFromFile
parameter_list|(
name|File
name|wadlBasedir
parameter_list|,
name|String
name|includes
index|[]
parameter_list|,
name|String
name|excludes
index|[]
parameter_list|,
name|Option
name|defaultOptions
parameter_list|,
name|File
name|defaultOutputDir
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
if|if
condition|(
name|wadlBasedir
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|ArrayList
argument_list|<>
argument_list|()
return|;
block|}
if|if
condition|(
operator|!
name|wadlBasedir
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
name|wadlBasedir
operator|+
literal|" does not exist"
argument_list|)
throw|;
block|}
name|List
argument_list|<
name|File
argument_list|>
name|wadlFiles
init|=
name|getWadlFiles
argument_list|(
name|wadlBasedir
argument_list|,
name|includes
argument_list|,
name|excludes
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|WadlOption
argument_list|>
name|wadlOptions
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|File
name|wadl
range|:
name|wadlFiles
control|)
block|{
name|WadlOption
name|wadlOption
init|=
name|generateWadlOptionFromFile
argument_list|(
name|wadl
argument_list|,
name|defaultOptions
argument_list|,
name|defaultOutputDir
argument_list|)
decl_stmt|;
if|if
condition|(
name|wadlOption
operator|!=
literal|null
condition|)
block|{
name|wadlOptions
operator|.
name|add
argument_list|(
name|wadlOption
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|wadlOptions
return|;
block|}
specifier|private
specifier|static
name|String
name|joinWithComma
parameter_list|(
name|String
index|[]
name|arr
parameter_list|)
block|{
if|if
condition|(
name|arr
operator|==
literal|null
operator|||
name|arr
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
literal|""
return|;
block|}
return|return
name|String
operator|.
name|join
argument_list|(
literal|","
argument_list|,
name|arr
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|List
argument_list|<
name|File
argument_list|>
name|getWadlFiles
parameter_list|(
name|File
name|dir
parameter_list|,
name|String
name|includes
index|[]
parameter_list|,
name|String
name|excludes
index|[]
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
name|List
argument_list|<
name|String
argument_list|>
name|exList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|excludes
operator|!=
literal|null
condition|)
block|{
name|exList
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|excludes
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|exList
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|util
operator|.
name|FileUtils
operator|.
name|getDefaultExcludes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|inc
init|=
name|joinWithComma
argument_list|(
name|includes
argument_list|)
decl_stmt|;
name|String
name|ex
init|=
name|joinWithComma
argument_list|(
name|exList
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|List
argument_list|<
name|?
argument_list|>
name|newfiles
init|=
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|util
operator|.
name|FileUtils
operator|.
name|getFiles
argument_list|(
name|dir
argument_list|,
name|inc
argument_list|,
name|ex
argument_list|)
decl_stmt|;
return|return
name|CastUtils
operator|.
name|cast
argument_list|(
name|newfiles
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|exc
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
name|exc
operator|.
name|getMessage
argument_list|()
argument_list|,
name|exc
argument_list|)
throw|;
block|}
block|}
specifier|protected
specifier|static
name|WadlOption
name|generateWadlOptionFromFile
parameter_list|(
specifier|final
name|File
name|wadl
parameter_list|,
specifier|final
name|Option
name|defaultOptions
parameter_list|,
name|File
name|defaultOutputDir
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
if|if
condition|(
name|wadl
operator|==
literal|null
operator|||
operator|!
name|wadl
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
name|wadlFileName
init|=
name|wadl
operator|.
name|getName
argument_list|()
decl_stmt|;
name|int
name|idx
init|=
name|wadlFileName
operator|.
name|toLowerCase
argument_list|()
operator|.
name|lastIndexOf
argument_list|(
literal|".wadl"
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
name|wadlFileName
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
name|WadlOption
name|wadlOption
init|=
operator|new
name|WadlOption
argument_list|()
decl_stmt|;
if|if
condition|(
name|defaultOptions
operator|!=
literal|null
condition|)
block|{
name|wadlOption
operator|.
name|merge
argument_list|(
name|defaultOptions
argument_list|)
expr_stmt|;
block|}
specifier|final
name|String
name|wadlName
init|=
name|wadlFileName
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
name|wadl
operator|.
name|getParentFile
argument_list|()
argument_list|,
name|wadlName
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
name|wadlOption
operator|.
name|getExtraargs
argument_list|()
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|options
argument_list|)
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
name|wadl
operator|.
name|getParentFile
argument_list|()
argument_list|,
name|wadlName
operator|+
name|WADL_BINDINGS
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
name|wadlOption
operator|.
name|addBindingFile
argument_list|(
name|binding
argument_list|)
expr_stmt|;
block|}
block|}
name|wadlOption
operator|.
name|setWadl
argument_list|(
name|wadl
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
name|wadlOption
operator|.
name|getOutputDir
argument_list|()
operator|==
literal|null
condition|)
block|{
name|wadlOption
operator|.
name|setOutputDir
argument_list|(
name|defaultOutputDir
argument_list|)
expr_stmt|;
block|}
return|return
name|wadlOption
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
name|WADL_OPTIONS
argument_list|)
decl_stmt|;
if|if
condition|(
name|files
operator|.
name|size
argument_list|()
operator|<=
literal|0
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
name|size
argument_list|()
operator|<=
literal|0
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
block|}
end_class

end_unit

