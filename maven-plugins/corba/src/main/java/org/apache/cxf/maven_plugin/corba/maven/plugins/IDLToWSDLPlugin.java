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
name|tools
operator|.
name|corba
operator|.
name|IDLToWSDL
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

begin_comment
comment|/**  * @goal idl2wsdl  * @description CXF IDL To WSDL Tool  */
end_comment

begin_class
specifier|public
class|class
name|IDLToWSDLPlugin
extends|extends
name|AbstractMojo
block|{
comment|/**      * @parameter  expression="${project.build.directory}/generated/src/main/java"      * @required      */
name|String
name|outputDir
decl_stmt|;
comment|/**      * @parameter      */
name|IdltowsdlOption
name|idltowsdlOptions
index|[]
decl_stmt|;
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|MojoExecutionException
block|{
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
if|if
condition|(
name|idltowsdlOptions
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Please specify the idltowsdl options"
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
name|idltowsdlOptions
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
name|idltowsdlOptions
index|[
name|x
index|]
operator|.
name|getIDL
argument_list|()
argument_list|)
decl_stmt|;
name|File
name|doneFile
init|=
operator|new
name|File
argument_list|(
name|outputDirFile
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
name|Object
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|"-o"
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
name|addAll
argument_list|(
name|idltowsdlOptions
index|[
name|x
index|]
operator|.
name|getExtraargs
argument_list|()
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|idltowsdlOptions
index|[
name|x
index|]
operator|.
name|getIDL
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|IDLToWSDL
operator|.
name|run
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
block|}
end_class

end_unit

