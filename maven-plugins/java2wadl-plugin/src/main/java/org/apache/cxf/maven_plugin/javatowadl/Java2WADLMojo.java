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
name|javatowadl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedWriter
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
name|FileWriter
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
name|lang
operator|.
name|annotation
operator|.
name|Annotation
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
name|Collection
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Path
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
name|util
operator|.
name|ClasspathScanner
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
name|jaxrs
operator|.
name|model
operator|.
name|ClassResourceInfo
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
name|jaxrs
operator|.
name|model
operator|.
name|wadl
operator|.
name|DocumentationProvider
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
name|jaxrs
operator|.
name|model
operator|.
name|wadl
operator|.
name|WadlGenerator
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
name|jaxrs
operator|.
name|utils
operator|.
name|InjectionUtils
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
name|jaxrs
operator|.
name|utils
operator|.
name|ResourceUtils
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
comment|/**  * @goal java2wadl  * @description CXF Java To WADL Tool  * @requiresDependencyResolution test  * @threadSafe */
end_comment

begin_class
specifier|public
class|class
name|Java2WADLMojo
extends|extends
name|AbstractMojo
block|{
specifier|public
specifier|static
specifier|final
name|String
name|WADL_NS
init|=
literal|"http://wadl.dev.java.net/2009/02"
decl_stmt|;
specifier|private
name|List
argument_list|<
name|ClassResourceInfo
argument_list|>
name|classResourceInfos
init|=
operator|new
name|ArrayList
argument_list|<
name|ClassResourceInfo
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * @parameter      */
specifier|private
name|String
name|outputFile
decl_stmt|;
comment|/**      * @parameter      */
specifier|private
name|String
name|address
decl_stmt|;
comment|/**      * @parameter      */
specifier|private
name|String
name|docProvider
decl_stmt|;
comment|/**      * Attach the generated wadl file to the list of files to be deployed      * on install. This means the wadl file will be copied to the repository      * with groupId, artifactId and version of the project and type "wadl".      *      * With this option you can use the maven repository as a Service Repository.      *      * @parameter default-value="true"      */
specifier|private
name|Boolean
name|attachWadl
decl_stmt|;
comment|/**      * @parameter      */
specifier|private
name|String
name|classifier
decl_stmt|;
comment|/**      * @parameter      * @required      */
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|classResourceNames
decl_stmt|;
comment|/**      * @parameter      */
specifier|private
name|String
name|basePackages
decl_stmt|;
comment|/**      * @parameter expression="${project}"      * @required      */
specifier|private
name|MavenProject
name|project
decl_stmt|;
comment|/**      * Maven ProjectHelper.      *      * @component      * @readonly      */
specifier|private
name|MavenProjectHelper
name|projectHelper
decl_stmt|;
comment|/**      * @parameter default-value="false"      */
specifier|private
name|boolean
name|useJson
decl_stmt|;
comment|/**      * @parameter default-value="false"      */
specifier|private
name|boolean
name|singleResourceMultipleMethods
decl_stmt|;
comment|/**      * @parameter default-value="false"      */
specifier|private
name|boolean
name|useSingleSlashResource
decl_stmt|;
comment|/**      * @parameter default-value="false"      */
specifier|private
name|boolean
name|ignoreForwardSlash
decl_stmt|;
comment|/**      * @parameter default-value="false"      */
specifier|private
name|boolean
name|addResourceAndMethodIds
decl_stmt|;
comment|/**      * @parameter default-value="false"      */
specifier|private
name|boolean
name|linkAnyMediaTypeToXmlSchema
decl_stmt|;
comment|/**      * @parameter default-value="false"      */
specifier|private
name|boolean
name|checkAbsolutePathSlash
decl_stmt|;
comment|/**      * @parameter      */
specifier|private
name|String
name|applicationTitle
decl_stmt|;
comment|/**      * @parameter      */
specifier|private
name|String
name|namespacePrefix
decl_stmt|;
comment|/**      * @parameter       */
specifier|private
name|String
name|outputFileName
decl_stmt|;
comment|/**      * @parameter default-value="wadl"      */
specifier|private
name|String
name|outputFileExtension
decl_stmt|;
specifier|private
name|ClassLoader
name|resourceClassLoader
decl_stmt|;
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|MojoExecutionException
block|{
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|resourceClasses
init|=
name|loadResourceClasses
argument_list|()
decl_stmt|;
name|initClassResourceInfoList
argument_list|(
name|resourceClasses
argument_list|)
expr_stmt|;
name|WadlGenerator
name|wadlGenerator
init|=
operator|new
name|WadlGenerator
argument_list|(
name|getBus
argument_list|()
argument_list|)
decl_stmt|;
name|DocumentationProvider
name|documentationProvider
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|docProvider
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|documentationProvider
operator|=
operator|(
name|DocumentationProvider
operator|)
name|getClassLoader
argument_list|()
operator|.
name|loadClass
argument_list|(
name|docProvider
argument_list|)
operator|.
name|getConstructor
argument_list|(
operator|new
name|Class
index|[]
block|{
name|String
operator|.
name|class
block|}
argument_list|)
operator|.
name|newInstance
argument_list|(
operator|new
name|Object
index|[]
block|{
name|project
operator|.
name|getBuild
argument_list|()
operator|.
name|getDirectory
argument_list|()
block|}
argument_list|)
expr_stmt|;
name|wadlGenerator
operator|.
name|setDocumentationProvider
argument_list|(
name|documentationProvider
argument_list|)
expr_stmt|;
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
name|setExtraProperties
argument_list|(
name|wadlGenerator
argument_list|)
expr_stmt|;
name|StringBuilder
name|sbMain
init|=
name|wadlGenerator
operator|.
name|generateWADL
argument_list|(
name|getBaseURI
argument_list|()
argument_list|,
name|classResourceInfos
argument_list|,
name|useJson
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"the wadl is =====> \n"
operator|+
name|sbMain
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|generateWadl
argument_list|(
name|resourceClasses
argument_list|,
name|sbMain
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|setExtraProperties
parameter_list|(
name|WadlGenerator
name|wg
parameter_list|)
block|{
name|wg
operator|.
name|setSingleResourceMultipleMethods
argument_list|(
name|singleResourceMultipleMethods
argument_list|)
expr_stmt|;
name|wg
operator|.
name|setUseSingleSlashResource
argument_list|(
name|useSingleSlashResource
argument_list|)
expr_stmt|;
name|wg
operator|.
name|setIgnoreForwardSlash
argument_list|(
name|ignoreForwardSlash
argument_list|)
expr_stmt|;
name|wg
operator|.
name|setAddResourceAndMethodIds
argument_list|(
name|addResourceAndMethodIds
argument_list|)
expr_stmt|;
name|wg
operator|.
name|setLinkAnyMediaTypeToXmlSchema
argument_list|(
name|linkAnyMediaTypeToXmlSchema
argument_list|)
expr_stmt|;
name|wg
operator|.
name|setCheckAbsolutePathSlash
argument_list|(
name|checkAbsolutePathSlash
argument_list|)
expr_stmt|;
if|if
condition|(
name|applicationTitle
operator|!=
literal|null
condition|)
block|{
name|wg
operator|.
name|setApplicationTitle
argument_list|(
name|applicationTitle
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|namespacePrefix
operator|!=
literal|null
condition|)
block|{
name|wg
operator|.
name|setNamespacePrefix
argument_list|(
name|namespacePrefix
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|generateWadl
parameter_list|(
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|resourceClasses
parameter_list|,
name|String
name|wadl
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
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
comment|// Put the wadl in target/generated/wadl
name|String
name|name
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|outputFileName
operator|!=
literal|null
condition|)
block|{
name|name
operator|=
name|outputFileName
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|resourceClasses
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|name
operator|=
name|resourceClasses
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getSimpleName
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|name
operator|=
literal|"application"
expr_stmt|;
block|}
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
literal|"/generated/wadl/"
operator|+
name|name
operator|+
literal|"."
operator|+
name|outputFileExtension
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
name|BufferedWriter
name|writer
init|=
literal|null
decl_stmt|;
try|try
block|{
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
comment|/*File wadlFile = new File(outputFile);             if (!wadlFile.exists()) {                 wadlFile.createNewFile();             }*/
name|writer
operator|=
operator|new
name|BufferedWriter
argument_list|(
operator|new
name|FileWriter
argument_list|(
name|outputFile
argument_list|)
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|wadl
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
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
finally|finally
block|{
try|try
block|{
if|if
condition|(
name|writer
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
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
comment|// Attach the generated wadl file to the artifacts that get deployed
comment|// with the enclosing project
if|if
condition|(
name|attachWadl
operator|&&
name|outputFile
operator|!=
literal|null
condition|)
block|{
name|File
name|wadlFile
init|=
operator|new
name|File
argument_list|(
name|outputFile
argument_list|)
decl_stmt|;
if|if
condition|(
name|wadlFile
operator|.
name|exists
argument_list|()
condition|)
block|{
if|if
condition|(
name|classifier
operator|!=
literal|null
condition|)
block|{
name|projectHelper
operator|.
name|attachArtifact
argument_list|(
name|project
argument_list|,
literal|"wadl"
argument_list|,
name|classifier
argument_list|,
name|wadlFile
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|projectHelper
operator|.
name|attachArtifact
argument_list|(
name|project
argument_list|,
literal|"wadl"
argument_list|,
name|wadlFile
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|String
name|getBaseURI
parameter_list|()
block|{
if|if
condition|(
name|address
operator|!=
literal|null
condition|)
block|{
return|return
name|address
return|;
block|}
else|else
block|{
comment|// the consumer may use the original target URI to figure out absolute URI
return|return
literal|"/"
return|;
block|}
block|}
specifier|private
name|ClassLoader
name|getClassLoader
parameter_list|()
throws|throws
name|MojoExecutionException
block|{
if|if
condition|(
name|resourceClassLoader
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|List
argument_list|<
name|?
argument_list|>
name|runtimeClasspathElements
init|=
name|project
operator|.
name|getRuntimeClasspathElements
argument_list|()
decl_stmt|;
name|URL
index|[]
name|runtimeUrls
init|=
operator|new
name|URL
index|[
name|runtimeClasspathElements
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|runtimeClasspathElements
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|String
name|element
init|=
operator|(
name|String
operator|)
name|runtimeClasspathElements
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|runtimeUrls
index|[
name|i
index|]
operator|=
operator|new
name|File
argument_list|(
name|element
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
expr_stmt|;
block|}
name|resourceClassLoader
operator|=
operator|new
name|URLClassLoader
argument_list|(
name|runtimeUrls
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
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
return|return
name|resourceClassLoader
return|;
block|}
specifier|private
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|loadResourceClasses
parameter_list|()
throws|throws
name|MojoExecutionException
block|{
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|resourceClasses
init|=
operator|new
name|ArrayList
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|(
name|classResourceNames
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|className
range|:
name|classResourceNames
control|)
block|{
try|try
block|{
name|resourceClasses
operator|.
name|add
argument_list|(
name|getClassLoader
argument_list|()
operator|.
name|loadClass
argument_list|(
name|className
argument_list|)
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|resourceClasses
operator|.
name|isEmpty
argument_list|()
operator|&&
name|basePackages
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|List
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|>
name|anns
init|=
operator|new
name|ArrayList
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|anns
operator|.
name|add
argument_list|(
name|Path
operator|.
name|class
argument_list|)
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|,
name|Collection
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
name|discoveredClasses
init|=
name|ClasspathScanner
operator|.
name|findClasses
argument_list|(
name|ClasspathScanner
operator|.
name|parsePackages
argument_list|(
name|basePackages
argument_list|)
argument_list|,
name|anns
argument_list|,
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|discoveredClasses
operator|.
name|containsKey
argument_list|(
name|Path
operator|.
name|class
argument_list|)
condition|)
block|{
name|resourceClasses
operator|.
name|addAll
argument_list|(
name|discoveredClasses
operator|.
name|get
argument_list|(
name|Path
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
block|}
return|return
name|resourceClasses
return|;
block|}
specifier|private
name|void
name|initClassResourceInfoList
parameter_list|(
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|resourceClasses
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|beanClass
range|:
name|resourceClasses
control|)
block|{
name|ClassResourceInfo
name|cri
init|=
name|getCreatedFromModel
argument_list|(
name|beanClass
argument_list|)
decl_stmt|;
if|if
condition|(
name|cri
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|InjectionUtils
operator|.
name|isConcreteClass
argument_list|(
name|cri
operator|.
name|getServiceClass
argument_list|()
argument_list|)
condition|)
block|{
name|cri
operator|=
operator|new
name|ClassResourceInfo
argument_list|(
name|cri
argument_list|)
expr_stmt|;
name|classResourceInfos
operator|.
name|add
argument_list|(
name|cri
argument_list|)
expr_stmt|;
block|}
name|cri
operator|.
name|setResourceClass
argument_list|(
name|beanClass
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|cri
operator|=
name|ResourceUtils
operator|.
name|createClassResourceInfo
argument_list|(
name|beanClass
argument_list|,
name|beanClass
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|cri
operator|!=
literal|null
condition|)
block|{
name|classResourceInfos
operator|.
name|add
argument_list|(
name|cri
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|Bus
name|getBus
parameter_list|()
block|{
return|return
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
return|;
block|}
specifier|private
name|ClassResourceInfo
name|getCreatedFromModel
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|realClass
parameter_list|)
block|{
for|for
control|(
name|ClassResourceInfo
name|cri
range|:
name|classResourceInfos
control|)
block|{
if|if
condition|(
name|cri
operator|.
name|isCreatedFromModel
argument_list|()
operator|&&
name|cri
operator|.
name|isRoot
argument_list|()
operator|&&
name|cri
operator|.
name|getServiceClass
argument_list|()
operator|.
name|isAssignableFrom
argument_list|(
name|realClass
argument_list|)
condition|)
block|{
return|return
name|cri
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

