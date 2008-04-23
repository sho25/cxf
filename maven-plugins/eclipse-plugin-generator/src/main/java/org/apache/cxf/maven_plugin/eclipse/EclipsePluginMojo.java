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
name|eclipse
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
name|common
operator|.
name|util
operator|.
name|ReflectionUtil
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
name|util
operator|.
name|StringUtils
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
name|VelocityGenerator
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
name|FileWriterUtil
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
name|plugin
operator|.
name|MojoFailureException
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
comment|/**  * @goal eclipseplugin  * @description CXF eclipse plugin generator  */
end_comment

begin_class
specifier|public
class|class
name|EclipsePluginMojo
extends|extends
name|AbstractMojo
block|{
specifier|private
specifier|static
specifier|final
name|String
name|LIB_PATH
init|=
literal|"lib"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ECLIPSE_VERSION
init|=
literal|"3.2"
decl_stmt|;
comment|/**      * @parameter expression="${project}"      * @required      */
name|MavenProject
name|project
decl_stmt|;
comment|/**      * The set of dependencies required by the project       * @parameter default-value="${project.artifacts}"      * @required      * @readonly      */
name|Set
name|dependencies
decl_stmt|;
comment|/**      * @parameter  expression="${project.build.directory}";      * @required      */
name|File
name|targetDir
decl_stmt|;
specifier|private
name|String
name|getTemplateFile
parameter_list|(
name|String
name|version
parameter_list|)
block|{
return|return
literal|"/org/apache/cxf/maven_plugin/eclipse/"
operator|+
name|version
operator|+
literal|"/MANIFEST.vm"
return|;
block|}
specifier|private
name|List
argument_list|<
name|File
argument_list|>
name|listJars
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|File
argument_list|>
name|jars
init|=
operator|new
name|ArrayList
argument_list|<
name|File
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|dependencies
operator|!=
literal|null
operator|&&
operator|!
name|dependencies
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|Iterator
name|it
init|=
name|dependencies
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Artifact
name|artifact
init|=
operator|(
name|Artifact
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|File
name|oldJar
init|=
name|artifact
operator|.
name|getFile
argument_list|()
decl_stmt|;
name|jars
operator|.
name|add
argument_list|(
name|oldJar
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|jars
return|;
block|}
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|MojoExecutionException
throws|,
name|MojoFailureException
block|{
try|try
block|{
name|generatePluginXML
argument_list|(
name|listJars
argument_list|()
argument_list|)
expr_stmt|;
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
specifier|private
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|StringUtils
operator|.
name|formatVersionNumber
argument_list|(
name|project
operator|.
name|getVersion
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|getExportedPackages
parameter_list|(
name|List
argument_list|<
name|File
argument_list|>
name|jars
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|packages
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|File
name|jarFile
range|:
name|jars
control|)
block|{
name|packages
operator|.
name|addAll
argument_list|(
name|ReflectionUtil
operator|.
name|getPackagesFromJar
argument_list|(
name|jarFile
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|packages
return|;
block|}
specifier|private
name|void
name|generatePluginXML
parameter_list|(
name|List
argument_list|<
name|File
argument_list|>
name|jars
parameter_list|)
throws|throws
name|Exception
block|{
name|VelocityGenerator
name|velocity
init|=
operator|new
name|VelocityGenerator
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|String
name|templateFile
init|=
name|getTemplateFile
argument_list|(
name|ECLIPSE_VERSION
argument_list|)
decl_stmt|;
name|velocity
operator|.
name|setAttributes
argument_list|(
literal|"ECLIPSE_VERSION"
argument_list|,
name|ECLIPSE_VERSION
argument_list|)
expr_stmt|;
name|velocity
operator|.
name|setAttributes
argument_list|(
literal|"PLUGIN_VERSION"
argument_list|,
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|velocity
operator|.
name|setAttributes
argument_list|(
literal|"GROUP_ID"
argument_list|,
name|project
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|velocity
operator|.
name|setAttributes
argument_list|(
literal|"libPath"
argument_list|,
name|LIB_PATH
argument_list|)
expr_stmt|;
name|velocity
operator|.
name|setAttributes
argument_list|(
literal|"jars"
argument_list|,
name|jars
argument_list|)
expr_stmt|;
name|velocity
operator|.
name|setAttributes
argument_list|(
literal|"exportedPackages"
argument_list|,
name|getExportedPackages
argument_list|(
name|jars
argument_list|)
argument_list|)
expr_stmt|;
name|File
name|outputFile
init|=
operator|new
name|File
argument_list|(
name|targetDir
argument_list|,
literal|"MANIFEST.MF"
argument_list|)
decl_stmt|;
name|velocity
operator|.
name|doWrite
argument_list|(
name|templateFile
argument_list|,
name|FileWriterUtil
operator|.
name|getWriter
argument_list|(
name|outputFile
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

