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
name|LinkedList
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
name|common
operator|.
name|util
operator|.
name|URIParserUtil
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

begin_class
specifier|public
class|class
name|WadlOption
extends|extends
name|Option
block|{
comment|/**      * The WADL file to process.      */
name|String
name|wadl
decl_stmt|;
name|String
name|wadlFileExtension
init|=
literal|"wadl"
decl_stmt|;
comment|/**      * Alternatively to the wadl string an artifact can be specified      */
name|DocumentArtifact
name|wadlArtifact
decl_stmt|;
specifier|public
name|WadlOption
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|public
name|String
name|getWadl
parameter_list|()
block|{
return|return
name|wadl
return|;
block|}
specifier|public
name|void
name|setWadl
parameter_list|(
name|String
name|w
parameter_list|)
block|{
name|wadl
operator|=
name|w
expr_stmt|;
block|}
specifier|public
name|DocumentArtifact
name|getWadlArtifact
parameter_list|()
block|{
return|return
name|wadlArtifact
return|;
block|}
specifier|public
name|void
name|setWadlArtifact
parameter_list|(
name|DocumentArtifact
name|wadlArtifact
parameter_list|)
block|{
name|this
operator|.
name|wadlArtifact
operator|=
name|wadlArtifact
expr_stmt|;
block|}
comment|/**      * Try to find a file matching the wadl path (either absolutely, relatively to the current dir or to      * the project base dir)      *       * @return wadl file      */
specifier|public
name|File
name|getDocumentFile
parameter_list|(
name|File
name|baseDir
parameter_list|)
block|{
if|if
condition|(
name|wadl
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|File
name|file
init|=
literal|null
decl_stmt|;
try|try
block|{
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
name|wadl
argument_list|)
decl_stmt|;
if|if
condition|(
name|uri
operator|.
name|isAbsolute
argument_list|()
condition|)
block|{
name|file
operator|=
operator|new
name|File
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
if|if
condition|(
name|file
operator|==
literal|null
operator|||
operator|!
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
name|file
operator|=
operator|new
name|File
argument_list|(
name|wadl
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
name|file
operator|=
operator|new
name|File
argument_list|(
name|baseDir
argument_list|,
name|wadl
argument_list|)
expr_stmt|;
block|}
return|return
name|file
return|;
block|}
specifier|public
name|List
argument_list|<
name|URI
argument_list|>
name|getWadlURIs
parameter_list|(
name|URI
name|baseURI
parameter_list|,
name|ClassLoader
name|resourceLoader
parameter_list|)
block|{
name|String
name|wadlLocation
init|=
name|getWadl
argument_list|()
decl_stmt|;
if|if
condition|(
name|wadlLocation
operator|.
name|contains
argument_list|(
literal|"."
argument_list|)
operator|&&
operator|!
name|wadlLocation
operator|.
name|contains
argument_list|(
literal|"*"
argument_list|)
condition|)
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|getWadlURI
argument_list|(
name|baseURI
argument_list|,
name|wadlLocation
argument_list|)
argument_list|)
return|;
block|}
name|List
argument_list|<
name|URI
argument_list|>
name|uris
init|=
operator|new
name|LinkedList
argument_list|<
name|URI
argument_list|>
argument_list|()
decl_stmt|;
try|try
block|{
for|for
control|(
name|URL
name|nextLocation
range|:
name|ClasspathScanner
operator|.
name|findResources
argument_list|(
name|wadlLocation
argument_list|,
name|wadlFileExtension
argument_list|,
name|resourceLoader
argument_list|)
control|)
block|{
name|uris
operator|.
name|add
argument_list|(
name|getWadlURI
argument_list|(
name|baseURI
argument_list|,
name|nextLocation
operator|.
name|toURI
argument_list|()
operator|.
name|getPath
argument_list|()
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
return|return
name|uris
return|;
block|}
specifier|private
name|URI
name|getWadlURI
parameter_list|(
name|URI
name|baseURI
parameter_list|,
name|String
name|wadlLocation
parameter_list|)
block|{
name|File
name|wadlFile
init|=
operator|new
name|File
argument_list|(
name|wadlLocation
argument_list|)
decl_stmt|;
return|return
name|wadlFile
operator|.
name|exists
argument_list|()
condition|?
name|wadlFile
operator|.
name|toURI
argument_list|()
else|:
name|baseURI
operator|.
name|resolve
argument_list|(
name|URIParserUtil
operator|.
name|escapeChars
argument_list|(
name|wadlLocation
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
if|if
condition|(
name|wadl
operator|!=
literal|null
condition|)
block|{
return|return
name|wadl
operator|.
name|hashCode
argument_list|()
return|;
block|}
return|return
operator|-
literal|1
return|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|obj
operator|instanceof
name|WadlOption
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|WadlOption
name|t
init|=
operator|(
name|WadlOption
operator|)
name|obj
decl_stmt|;
return|return
name|t
operator|.
name|getWadl
argument_list|()
operator|.
name|equals
argument_list|(
name|getWadl
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"WADL: "
argument_list|)
operator|.
name|append
argument_list|(
name|wadl
argument_list|)
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"OutputDir: "
argument_list|)
operator|.
name|append
argument_list|(
name|outputDir
argument_list|)
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|generateCommandLine
parameter_list|(
name|File
name|outputDirFile
parameter_list|,
name|URI
name|basedir
parameter_list|,
name|URI
name|wadlURI
parameter_list|,
name|boolean
name|debug
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
name|addIfNotNull
argument_list|(
name|list
argument_list|,
name|outputDirFile
argument_list|,
literal|"-d"
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|binding
range|:
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
init|=
name|bindingFile
operator|.
name|exists
argument_list|()
condition|?
name|bindingFile
operator|.
name|toURI
argument_list|()
else|:
name|basedir
operator|.
name|resolve
argument_list|(
name|binding
argument_list|)
decl_stmt|;
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
name|addIfNotNull
argument_list|(
name|list
argument_list|,
name|getCatalog
argument_list|()
argument_list|,
literal|"-catalog"
argument_list|)
expr_stmt|;
name|addIfNotNull
argument_list|(
name|list
argument_list|,
name|getResourcename
argument_list|()
argument_list|,
literal|"-resource"
argument_list|)
expr_stmt|;
name|addIfNotNull
argument_list|(
name|list
argument_list|,
name|getPackagename
argument_list|()
argument_list|,
literal|"-p"
argument_list|)
expr_stmt|;
name|addList
argument_list|(
name|list
argument_list|,
literal|"-sp"
argument_list|,
literal|true
argument_list|,
name|getSchemaPackagenames
argument_list|()
argument_list|)
expr_stmt|;
name|addIfTrue
argument_list|(
name|list
argument_list|,
name|isImpl
argument_list|()
argument_list|,
literal|"-impl"
argument_list|)
expr_stmt|;
name|addIfTrue
argument_list|(
name|list
argument_list|,
name|isInterface
argument_list|()
argument_list|,
literal|"-interface"
argument_list|)
expr_stmt|;
name|addList
argument_list|(
name|list
argument_list|,
literal|""
argument_list|,
literal|false
argument_list|,
name|getExtraargs
argument_list|()
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|wadlURI
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|list
return|;
block|}
comment|// TODO: the following 3 helpers can go to a superclass or common utility class
comment|//       to be used by WADL and WSDL Pptions
specifier|private
specifier|static
name|void
name|addIfNotNull
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|list
parameter_list|,
name|Object
name|value
parameter_list|,
name|String
name|key
parameter_list|)
block|{
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|key
argument_list|)
expr_stmt|;
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
specifier|private
specifier|static
name|void
name|addList
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|destList
parameter_list|,
name|String
name|key
parameter_list|,
name|boolean
name|keyAsOwnElement
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|sourceList
parameter_list|)
block|{
if|if
condition|(
name|sourceList
operator|==
literal|null
condition|)
block|{
return|return;
block|}
for|for
control|(
name|String
name|value
range|:
name|sourceList
control|)
block|{
if|if
condition|(
name|keyAsOwnElement
condition|)
block|{
name|destList
operator|.
name|add
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|destList
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Maven makes empty tags into null
comment|// instead of empty strings. so replace null by ""
name|destList
operator|.
name|add
argument_list|(
name|key
operator|+
operator|(
operator|(
name|value
operator|==
literal|null
operator|)
condition|?
literal|""
else|:
name|value
operator|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
specifier|static
name|void
name|addIfTrue
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|list
parameter_list|,
name|boolean
name|expression
parameter_list|,
name|String
name|key
parameter_list|)
block|{
if|if
condition|(
name|expression
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getWadlFileExtension
parameter_list|()
block|{
return|return
name|wadlFileExtension
return|;
block|}
specifier|public
name|void
name|setWadlFileExtension
parameter_list|(
name|String
name|wadlFileExtension
parameter_list|)
block|{
name|this
operator|.
name|wadlFileExtension
operator|=
name|wadlFileExtension
expr_stmt|;
block|}
block|}
end_class

end_unit

