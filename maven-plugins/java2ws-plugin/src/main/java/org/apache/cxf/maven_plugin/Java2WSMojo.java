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
name|java
operator|.
name|util
operator|.
name|StringTokenizer
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
name|tools
operator|.
name|common
operator|.
name|CommandInterfaceUtils
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
name|java2ws
operator|.
name|JavaToWS
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
name|MavenProjectHelper
import|;
end_import

begin_comment
comment|/**  * @goal java2ws  * @description CXF Java To Webservice Tool  */
end_comment

begin_class
specifier|public
class|class
name|Java2WSMojo
extends|extends
name|AbstractMojo
block|{
comment|/**      * @parameter       * @required      */
specifier|private
name|String
name|className
decl_stmt|;
comment|/**      * @parameter  expression="${project.build.outputDirectory}"      * @required      */
specifier|private
name|String
name|classpath
decl_stmt|;
comment|/**      * @parameter      */
specifier|private
name|String
name|outputFile
decl_stmt|;
comment|/**      * @parameter      */
specifier|private
name|Boolean
name|soap12
decl_stmt|;
comment|/**      * @parameter      */
specifier|private
name|String
name|targetNamespace
decl_stmt|;
comment|/**      * @parameter      */
specifier|private
name|String
name|serviceName
decl_stmt|;
comment|/**      * @parameter      */
specifier|private
name|Boolean
name|verbose
decl_stmt|;
comment|/**      * @parameter      */
specifier|private
name|Boolean
name|quiet
decl_stmt|;
comment|/**      * @parameter  expression="${project.compileClasspathElements}"      * @required      */
specifier|private
name|List
name|classpathElements
decl_stmt|;
comment|/**      * @parameter expression="${project}"      * @required      */
specifier|private
name|MavenProject
name|project
decl_stmt|;
comment|/**      * Maven ProjectHelper.      *       * @component      * @readonly      */
specifier|private
name|MavenProjectHelper
name|projectHelper
decl_stmt|;
comment|/**      * @parameter      */
specifier|private
name|String
name|argline
decl_stmt|;
comment|/**      * @parameter      */
specifier|private
name|String
name|frontend
decl_stmt|;
comment|/**      * @parameter      */
specifier|private
name|String
name|databinding
decl_stmt|;
comment|/**      * @parameter default-value="false"      */
specifier|private
name|Boolean
name|genWsdl
decl_stmt|;
comment|/**      * @parameter default-value="false"      */
specifier|private
name|Boolean
name|genServer
decl_stmt|;
comment|/**      * @parameter default-value="false"      */
specifier|private
name|Boolean
name|genClient
decl_stmt|;
comment|/**      * @parameter default-value="false"      */
specifier|private
name|Boolean
name|genWrapperbean
decl_stmt|;
comment|/**      * Attach the generated wsdl file to the list of files to be deployed      * on install. This means the wsdl file will be copied to the repository      * with groupId, artifactId and version of the project and type "wsdl".      *       * With this option you can use the maven repository as a Service Repository.      *       * @parameter default-value="true"      */
specifier|private
name|Boolean
name|attachWsdl
decl_stmt|;
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|MojoExecutionException
block|{
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|classpathElement
range|:
name|classpathElements
control|)
block|{
name|buf
operator|.
name|append
argument_list|(
name|classpathElement
operator|.
name|toString
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
block|}
name|String
name|newCp
init|=
name|buf
operator|.
name|toString
argument_list|()
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
name|System
operator|.
name|setProperty
argument_list|(
literal|"java.class.path"
argument_list|,
name|newCp
argument_list|)
expr_stmt|;
name|processJavaClass
argument_list|()
expr_stmt|;
block|}
finally|finally
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
name|System
operator|.
name|gc
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|processJavaClass
parameter_list|()
throws|throws
name|MojoExecutionException
block|{
name|List
argument_list|<
name|String
argument_list|>
name|args
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|// outputfile arg
if|if
condition|(
name|outputFile
operator|==
literal|null
operator|&&
name|project
operator|!=
literal|null
condition|)
block|{
comment|// Put the wsdl in target/generated/wsdl
name|int
name|i
init|=
name|className
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
comment|// Prone to OoBE, but then it's wrong anyway
name|String
name|name
init|=
name|className
operator|.
name|substring
argument_list|(
name|i
operator|+
literal|1
argument_list|)
decl_stmt|;
name|outputFile
operator|=
operator|(
name|project
operator|.
name|getBuild
argument_list|()
operator|.
name|getDirectory
argument_list|()
operator|+
literal|"/generated/wsdl/"
operator|+
name|name
operator|+
literal|".wsdl"
operator|)
operator|.
name|replace
argument_list|(
literal|"/"
argument_list|,
name|File
operator|.
name|separator
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|outputFile
operator|!=
literal|null
condition|)
block|{
comment|// JavaToWSDL freaks out if the directory of the outputfile doesn't exist, so lets
comment|// create it since there's no easy way for the user to create it beforehand in maven
name|FileUtils
operator|.
name|mkDir
argument_list|(
operator|new
name|File
argument_list|(
name|outputFile
argument_list|)
operator|.
name|getParentFile
argument_list|()
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
literal|"-o"
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
name|outputFile
argument_list|)
expr_stmt|;
comment|/*               Contributor's comment:               Sometimes JavaToWSDL creates Java code for the wrappers.  I don't *think* this is               needed by the end user.             */
comment|// Commiter's comment:
comment|// Yes, it's required, it's defined in the JAXWS spec.
if|if
condition|(
name|project
operator|!=
literal|null
condition|)
block|{
name|project
operator|.
name|addCompileSourceRoot
argument_list|(
operator|new
name|File
argument_list|(
name|outputFile
argument_list|)
operator|.
name|getParentFile
argument_list|()
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|frontend
operator|!=
literal|null
condition|)
block|{
name|args
operator|.
name|add
argument_list|(
literal|"-frontend"
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
name|frontend
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|databinding
operator|!=
literal|null
condition|)
block|{
name|args
operator|.
name|add
argument_list|(
literal|"-databinding"
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
name|databinding
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|genWrapperbean
condition|)
block|{
name|args
operator|.
name|add
argument_list|(
literal|"-wrapperbean"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|genWsdl
condition|)
block|{
name|args
operator|.
name|add
argument_list|(
literal|"-wsdl"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|genServer
condition|)
block|{
name|args
operator|.
name|add
argument_list|(
literal|"-server"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|genClient
condition|)
block|{
name|args
operator|.
name|add
argument_list|(
literal|"-client"
argument_list|)
expr_stmt|;
block|}
comment|// classpath arg
name|args
operator|.
name|add
argument_list|(
literal|"-cp"
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
name|classpath
argument_list|)
expr_stmt|;
comment|// soap12 arg
if|if
condition|(
name|soap12
operator|!=
literal|null
operator|&&
name|soap12
operator|.
name|booleanValue
argument_list|()
condition|)
block|{
name|args
operator|.
name|add
argument_list|(
literal|"-soap12"
argument_list|)
expr_stmt|;
block|}
comment|// target namespace arg
if|if
condition|(
name|targetNamespace
operator|!=
literal|null
condition|)
block|{
name|args
operator|.
name|add
argument_list|(
literal|"-t"
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
name|targetNamespace
argument_list|)
expr_stmt|;
block|}
comment|// servicename arg
if|if
condition|(
name|serviceName
operator|!=
literal|null
condition|)
block|{
name|args
operator|.
name|add
argument_list|(
literal|"-servicename"
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
name|serviceName
argument_list|)
expr_stmt|;
block|}
comment|// verbose arg
if|if
condition|(
name|verbose
operator|!=
literal|null
operator|&&
name|verbose
operator|.
name|booleanValue
argument_list|()
condition|)
block|{
name|args
operator|.
name|add
argument_list|(
literal|"-verbose"
argument_list|)
expr_stmt|;
block|}
comment|// quiet arg
if|if
condition|(
name|quiet
operator|!=
literal|null
operator|&&
name|quiet
operator|.
name|booleanValue
argument_list|()
condition|)
block|{
name|args
operator|.
name|add
argument_list|(
literal|"-quiet"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|argline
operator|!=
literal|null
condition|)
block|{
name|StringTokenizer
name|stoken
init|=
operator|new
name|StringTokenizer
argument_list|(
name|argline
argument_list|,
literal|" "
argument_list|)
decl_stmt|;
while|while
condition|(
name|stoken
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|args
operator|.
name|add
argument_list|(
name|stoken
operator|.
name|nextToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// classname arg
name|args
operator|.
name|add
argument_list|(
name|className
argument_list|)
expr_stmt|;
try|try
block|{
name|CommandInterfaceUtils
operator|.
name|commandCommonMain
argument_list|()
expr_stmt|;
name|JavaToWS
name|j2w
init|=
operator|new
name|JavaToWS
argument_list|(
name|args
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|args
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
decl_stmt|;
name|j2w
operator|.
name|run
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
comment|// Attach the generated wsdl file to the artifacts that get deployed
comment|// with the enclosing project
if|if
condition|(
name|attachWsdl
operator|&&
name|outputFile
operator|!=
literal|null
condition|)
block|{
name|File
name|wsdlFile
init|=
operator|new
name|File
argument_list|(
name|outputFile
argument_list|)
decl_stmt|;
name|projectHelper
operator|.
name|attachArtifact
argument_list|(
name|project
argument_list|,
literal|"wsdl"
argument_list|,
name|wsdlFile
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

