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

begin_class
specifier|public
specifier|final
class|class
name|WsdlUtilities
block|{
specifier|public
specifier|static
specifier|final
name|String
name|WSDL_TYPE
init|=
literal|"wsdl"
decl_stmt|;
specifier|private
name|WsdlUtilities
parameter_list|()
block|{     }
specifier|public
specifier|static
name|boolean
name|fillWsdlOptionFromArtifact
parameter_list|(
name|GenericWsdlOption
name|option
parameter_list|,
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
name|WSDL_TYPE
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
literal|false
return|;
block|}
name|WsdlArtifact
name|wsdlArtifact
init|=
operator|new
name|WsdlArtifact
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
name|setArtifact
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
literal|true
return|;
block|}
specifier|public
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
condition|)
block|{
return|return
literal|""
return|;
block|}
name|StringBuilder
name|str
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|arr
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|s
range|:
name|arr
control|)
block|{
if|if
condition|(
name|str
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|str
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
name|str
operator|.
name|append
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|str
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|File
argument_list|>
name|getWsdlFiles
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
argument_list|<
name|String
argument_list|>
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
name|exList
operator|.
name|size
argument_list|()
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
block|}
end_class

end_unit

