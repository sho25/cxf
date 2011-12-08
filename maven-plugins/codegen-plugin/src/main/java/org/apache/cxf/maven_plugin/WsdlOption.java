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
name|net
operator|.
name|URI
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
name|common
operator|.
name|ToolConstants
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
name|URIParserUtil
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
class|class
name|WsdlOption
extends|extends
name|Option
block|{
comment|/**      * The WSDL file to process.      */
name|String
name|wsdl
decl_stmt|;
comment|/**      * Alternatively to the wsdl string an artifact can be specified      */
name|WsdlArtifact
name|wsdlArtifact
decl_stmt|;
specifier|public
name|String
name|getWsdl
parameter_list|()
block|{
return|return
name|wsdl
return|;
block|}
specifier|public
name|void
name|setWsdl
parameter_list|(
name|String
name|w
parameter_list|)
block|{
name|wsdl
operator|=
name|w
expr_stmt|;
block|}
specifier|public
name|WsdlArtifact
name|getWsdlArtifact
parameter_list|()
block|{
return|return
name|wsdlArtifact
return|;
block|}
specifier|public
name|void
name|setWsdlArtifact
parameter_list|(
name|WsdlArtifact
name|wsdlArtifact
parameter_list|)
block|{
name|this
operator|.
name|wsdlArtifact
operator|=
name|wsdlArtifact
expr_stmt|;
block|}
comment|/**      * Try to find a file matching the wsdl path (either absolutely, relatively to the current dir or to      * the project base dir)      *       * @return wsdl file      */
specifier|public
name|File
name|getWsdlFile
parameter_list|(
name|File
name|baseDir
parameter_list|)
block|{
if|if
condition|(
name|wsdl
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
name|wsdl
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
name|wsdl
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
name|wsdl
argument_list|)
expr_stmt|;
block|}
return|return
name|file
return|;
block|}
specifier|public
name|URI
name|getWsdlURI
parameter_list|(
name|URI
name|baseURI
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
name|String
name|wsdlLocation
init|=
name|getWsdl
argument_list|()
decl_stmt|;
if|if
condition|(
name|wsdlLocation
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"No wsdl available for base URI "
operator|+
name|baseURI
argument_list|)
throw|;
block|}
name|File
name|wsdlFile
init|=
operator|new
name|File
argument_list|(
name|wsdlLocation
argument_list|)
decl_stmt|;
return|return
name|wsdlFile
operator|.
name|exists
argument_list|()
condition|?
name|wsdlFile
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
name|wsdlLocation
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isDefServiceName
parameter_list|()
block|{
if|if
condition|(
name|extraargs
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|extraargs
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
literal|"-sn"
operator|.
name|equalsIgnoreCase
argument_list|(
name|extraargs
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
if|if
condition|(
name|wsdl
operator|!=
literal|null
condition|)
block|{
return|return
name|wsdl
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
name|WsdlOption
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|WsdlOption
name|t
init|=
operator|(
name|WsdlOption
operator|)
name|obj
decl_stmt|;
return|return
name|t
operator|.
name|getWsdl
argument_list|()
operator|.
name|equals
argument_list|(
name|getWsdl
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
literal|"WSDL: "
argument_list|)
operator|.
name|append
argument_list|(
name|wsdl
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
literal|"Extraargs: "
argument_list|)
operator|.
name|append
argument_list|(
name|extraargs
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
literal|"XJCargs: "
argument_list|)
operator|.
name|append
argument_list|(
name|xjcargs
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
literal|"Packagenames: "
argument_list|)
operator|.
name|append
argument_list|(
name|packagenames
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
name|wsdlURI
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
name|addList
argument_list|(
name|list
argument_list|,
literal|"-p"
argument_list|,
literal|true
argument_list|,
name|getPackagenames
argument_list|()
argument_list|)
expr_stmt|;
name|addList
argument_list|(
name|list
argument_list|,
literal|"-nexclude"
argument_list|,
literal|true
argument_list|,
name|getNamespaceExcludes
argument_list|()
argument_list|)
expr_stmt|;
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
name|getFrontEnd
argument_list|()
argument_list|,
literal|"-fe"
argument_list|)
expr_stmt|;
name|addIfNotNull
argument_list|(
name|list
argument_list|,
name|getDataBinding
argument_list|()
argument_list|,
literal|"-db"
argument_list|)
expr_stmt|;
name|addIfNotNull
argument_list|(
name|list
argument_list|,
name|getWsdlVersion
argument_list|()
argument_list|,
literal|"-wv"
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|isExtendedSoapHeaders
argument_list|()
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
literal|"-exsh"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|"true"
argument_list|)
expr_stmt|;
block|}
name|addIfTrue
argument_list|(
name|list
argument_list|,
name|isNoTypes
argument_list|()
argument_list|,
literal|"-noTypes"
argument_list|)
expr_stmt|;
name|addIfTrue
argument_list|(
name|list
argument_list|,
name|isAllowElementRefs
argument_list|()
argument_list|,
literal|"-"
operator|+
name|ToolConstants
operator|.
name|CFG_ALLOW_ELEMENT_REFS
argument_list|)
expr_stmt|;
name|addIfTrue
argument_list|(
name|list
argument_list|,
name|isValidateWsdl
argument_list|()
argument_list|,
literal|"-"
operator|+
name|ToolConstants
operator|.
name|CFG_VALIDATE_WSDL
argument_list|)
expr_stmt|;
name|addIfTrue
argument_list|(
name|list
argument_list|,
name|isMarkGenerated
argument_list|()
operator|!=
literal|null
operator|&&
name|isMarkGenerated
argument_list|()
argument_list|,
literal|"-"
operator|+
name|ToolConstants
operator|.
name|CFG_MARK_GENERATED
argument_list|)
expr_stmt|;
name|addIfNotNull
argument_list|(
name|list
argument_list|,
name|getDefaultExcludesNamespace
argument_list|()
argument_list|,
literal|"-dex"
argument_list|)
expr_stmt|;
name|addIfNotNull
argument_list|(
name|list
argument_list|,
name|getDefaultNamespacePackageMapping
argument_list|()
argument_list|,
literal|"-dns"
argument_list|)
expr_stmt|;
name|addIfNotNull
argument_list|(
name|list
argument_list|,
name|getServiceName
argument_list|()
argument_list|,
literal|"-sn"
argument_list|)
expr_stmt|;
name|addIfNotNull
argument_list|(
name|list
argument_list|,
name|getFaultSerialVersionUID
argument_list|()
argument_list|,
literal|"-"
operator|+
name|ToolConstants
operator|.
name|CFG_FAULT_SERIAL_VERSION_UID
argument_list|)
expr_stmt|;
name|addIfNotNull
argument_list|(
name|list
argument_list|,
name|getExceptionSuper
argument_list|()
argument_list|,
literal|"-"
operator|+
name|ToolConstants
operator|.
name|CFG_EXCEPTION_SUPER
argument_list|)
expr_stmt|;
name|addIfTrue
argument_list|(
name|list
argument_list|,
name|isAutoNameResolution
argument_list|()
argument_list|,
literal|"-"
operator|+
name|ToolConstants
operator|.
name|CFG_AUTORESOLVE
argument_list|)
expr_stmt|;
name|addIfTrue
argument_list|(
name|list
argument_list|,
name|isNoAddressBinding
argument_list|()
argument_list|,
literal|"-"
operator|+
name|ToolConstants
operator|.
name|CFG_NO_ADDRESS_BINDING
argument_list|)
expr_stmt|;
name|addList
argument_list|(
name|list
argument_list|,
literal|"-xjc"
argument_list|,
literal|false
argument_list|,
name|getXJCargs
argument_list|()
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
if|if
condition|(
name|isSetWsdlLocation
argument_list|()
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
literal|"-wsdlLocation"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|getWsdlLocation
argument_list|()
operator|==
literal|null
condition|?
literal|""
else|:
name|getWsdlLocation
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|addIfTrue
argument_list|(
name|list
argument_list|,
name|isWsdlList
argument_list|()
argument_list|,
literal|"-wsdlList"
argument_list|)
expr_stmt|;
name|addIfTrue
argument_list|(
name|list
argument_list|,
name|debug
operator|&&
operator|!
name|list
operator|.
name|contains
argument_list|(
literal|"-verbose"
argument_list|)
argument_list|,
literal|"-verbose"
argument_list|)
expr_stmt|;
name|addEqualsArray
argument_list|(
name|list
argument_list|,
literal|"-asyncMethods"
argument_list|,
name|getAsyncMethods
argument_list|()
argument_list|)
expr_stmt|;
name|addEqualsArray
argument_list|(
name|list
argument_list|,
literal|"-bareMethods"
argument_list|,
name|getBareMethods
argument_list|()
argument_list|)
expr_stmt|;
name|addEqualsArray
argument_list|(
name|list
argument_list|,
literal|"-mimeMethods"
argument_list|,
name|getMimeMethods
argument_list|()
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|wsdlURI
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|list
return|;
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
name|addEqualsArray
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
name|String
index|[]
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
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|sourceList
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
expr_stmt|;
name|boolean
name|first
init|=
literal|true
decl_stmt|;
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
operator|!
name|first
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
name|b
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|first
operator|=
literal|false
expr_stmt|;
block|}
block|}
name|destList
operator|.
name|add
argument_list|(
name|b
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

