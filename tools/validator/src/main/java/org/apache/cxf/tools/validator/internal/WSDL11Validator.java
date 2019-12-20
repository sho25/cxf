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
name|tools
operator|.
name|validator
operator|.
name|internal
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
name|FileFilter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
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
name|net
operator|.
name|JarURLConnection
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
name|Comparator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
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
name|jar
operator|.
name|JarEntry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|JarFile
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Definition
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|WSDLException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|InputSource
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
name|catalog
operator|.
name|OASISCatalogManager
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
name|catalog
operator|.
name|OASISCatalogManagerHelper
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
name|common
operator|.
name|i18n
operator|.
name|Message
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
name|logging
operator|.
name|LogUtils
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
name|resource
operator|.
name|URIResolver
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
name|ToolException
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
name|validator
operator|.
name|AbstractValidator
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
name|wsdl
operator|.
name|WSDLManager
import|;
end_import

begin_class
specifier|public
class|class
name|WSDL11Validator
extends|extends
name|AbstractDefinitionValidator
block|{
specifier|protected
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|WSDL11Validator
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|AbstractValidator
argument_list|>
name|validators
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|WSDL11Validator
parameter_list|(
specifier|final
name|Definition
name|definition
parameter_list|)
block|{
name|this
argument_list|(
name|definition
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|WSDL11Validator
parameter_list|(
specifier|final
name|Definition
name|definition
parameter_list|,
specifier|final
name|ToolContext
name|pe
parameter_list|)
block|{
name|this
argument_list|(
name|definition
argument_list|,
name|pe
argument_list|,
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|WSDL11Validator
parameter_list|(
specifier|final
name|Definition
name|definition
parameter_list|,
specifier|final
name|ToolContext
name|pe
parameter_list|,
specifier|final
name|Bus
name|b
parameter_list|)
block|{
name|super
argument_list|(
name|definition
argument_list|,
name|pe
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Document
name|getWSDLDoc
parameter_list|(
name|String
name|wsdl
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
operator|new
name|Message
argument_list|(
literal|"VALIDATE_WSDL"
argument_list|,
name|LOG
argument_list|,
name|wsdl
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|OASISCatalogManager
name|catalogResolver
init|=
name|OASISCatalogManager
operator|.
name|getCatalogManager
argument_list|(
name|this
operator|.
name|getBus
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|nw
init|=
operator|new
name|OASISCatalogManagerHelper
argument_list|()
operator|.
name|resolve
argument_list|(
name|catalogResolver
argument_list|,
name|wsdl
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|nw
operator|==
literal|null
condition|)
block|{
name|nw
operator|=
name|wsdl
expr_stmt|;
block|}
return|return
operator|new
name|Stax2DOM
argument_list|()
operator|.
name|getDocument
argument_list|(
name|URIParserUtil
operator|.
name|getAbsoluteURI
argument_list|(
name|nw
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|fe
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Cannot find the wsdl "
operator|+
name|wsdl
operator|+
literal|"to validate"
argument_list|)
expr_stmt|;
return|return
literal|null
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
name|ToolException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|boolean
name|isValid
parameter_list|()
throws|throws
name|ToolException
block|{
comment|//boolean isValid = true;
name|String
name|schemaDir
init|=
name|getSchemaDir
argument_list|()
decl_stmt|;
name|SchemaValidator
name|schemaValidator
init|=
literal|null
decl_stmt|;
name|String
index|[]
name|schemas
init|=
operator|(
name|String
index|[]
operator|)
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_SCHEMA_URL
argument_list|)
decl_stmt|;
comment|// Tool will use the following sequence to find the schema files
comment|// 1.ToolConstants.CFG_SCHEMA_DIR from ToolContext
comment|// 2.ToolConstants.CXF_SCHEMA_DIR from System property
comment|// 3.If 1 and 2 is null , then load these schema files from jar file
name|String
name|wsdl
init|=
operator|(
name|String
operator|)
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDLURL
argument_list|)
decl_stmt|;
name|Document
name|doc
init|=
name|getWSDLDoc
argument_list|(
name|wsdl
argument_list|)
decl_stmt|;
if|if
condition|(
name|doc
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|this
operator|.
name|def
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|this
operator|.
name|def
operator|=
name|getBus
argument_list|()
operator|.
name|getExtension
argument_list|(
name|WSDLManager
operator|.
name|class
argument_list|)
operator|.
name|getDefinition
argument_list|(
name|wsdl
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSDLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ToolException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
name|WSDLRefValidator
name|wsdlRefValidator
init|=
operator|new
name|WSDLRefValidator
argument_list|(
name|this
operator|.
name|def
argument_list|,
name|doc
argument_list|,
name|getBus
argument_list|()
argument_list|)
decl_stmt|;
name|wsdlRefValidator
operator|.
name|setSuppressWarnings
argument_list|(
name|env
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_SUPPRESS_WARNINGS
argument_list|)
argument_list|)
expr_stmt|;
name|validators
operator|.
name|add
argument_list|(
name|wsdlRefValidator
argument_list|)
expr_stmt|;
if|if
condition|(
name|env
operator|.
name|fullValidateWSDL
argument_list|()
condition|)
block|{
name|validators
operator|.
name|add
argument_list|(
operator|new
name|UniqueBodyPartsValidator
argument_list|(
name|this
operator|.
name|def
argument_list|)
argument_list|)
expr_stmt|;
name|validators
operator|.
name|add
argument_list|(
operator|new
name|WSIBPValidator
argument_list|(
name|this
operator|.
name|def
argument_list|)
argument_list|)
expr_stmt|;
name|validators
operator|.
name|add
argument_list|(
operator|new
name|MIMEBindingValidator
argument_list|(
name|this
operator|.
name|def
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|boolean
name|notValid
init|=
literal|false
decl_stmt|;
for|for
control|(
name|AbstractValidator
name|validator
range|:
name|validators
control|)
block|{
if|if
condition|(
operator|!
name|validator
operator|.
name|isValid
argument_list|()
condition|)
block|{
name|notValid
operator|=
literal|true
expr_stmt|;
name|addErrorMessage
argument_list|(
name|validator
operator|.
name|getErrorMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|notValid
condition|)
block|{
throw|throw
operator|new
name|ToolException
argument_list|(
name|this
operator|.
name|getErrorMessage
argument_list|()
argument_list|)
throw|;
block|}
comment|// By default just use WsdlRefValidator
if|if
condition|(
operator|!
name|env
operator|.
name|fullValidateWSDL
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|schemaDir
argument_list|)
condition|)
block|{
name|schemaValidator
operator|=
operator|new
name|SchemaValidator
argument_list|(
name|schemaDir
argument_list|,
name|wsdl
argument_list|,
name|schemas
argument_list|)
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|schemaValidator
operator|=
operator|new
name|SchemaValidator
argument_list|(
name|getDefaultSchemas
argument_list|()
argument_list|,
name|wsdl
argument_list|,
name|schemas
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
name|ToolException
argument_list|(
literal|"Schemas can not be loaded before validating wsdl"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
operator|!
name|schemaValidator
operator|.
name|isValid
argument_list|()
condition|)
block|{
name|this
operator|.
name|addErrorMessage
argument_list|(
name|schemaValidator
operator|.
name|getErrorMessage
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|this
operator|.
name|getErrorMessage
argument_list|()
argument_list|)
throw|;
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|String
name|getSchemaDir
parameter_list|()
block|{
name|String
name|dir
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_SCHEMA_DIR
argument_list|)
operator|==
literal|null
condition|)
block|{
name|dir
operator|=
name|System
operator|.
name|getProperty
argument_list|(
name|ToolConstants
operator|.
name|CXF_SCHEMA_DIR
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|dir
operator|=
operator|(
name|String
operator|)
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_SCHEMA_DIR
argument_list|)
expr_stmt|;
block|}
return|return
name|dir
return|;
block|}
specifier|protected
name|List
argument_list|<
name|InputSource
argument_list|>
name|getDefaultSchemas
parameter_list|()
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|InputSource
argument_list|>
name|xsdList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|URL
name|url
init|=
name|ClassLoaderUtils
operator|.
name|getResource
argument_list|(
literal|"/schemas/configuration/parameterized-types.xsd"
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
name|InputSource
name|is
init|=
operator|new
name|InputSource
argument_list|(
name|url
operator|.
name|openStream
argument_list|()
argument_list|)
decl_stmt|;
name|is
operator|.
name|setSystemId
argument_list|(
name|url
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|xsdList
operator|.
name|add
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
name|addDefaultSchemas
argument_list|(
name|ToolConstants
operator|.
name|CXF_SCHEMAS_DIR_INJAR
argument_list|,
name|xsdList
argument_list|)
expr_stmt|;
return|return
name|xsdList
return|;
block|}
specifier|private
name|void
name|addDefaultSchemas
parameter_list|(
name|String
name|location
parameter_list|,
name|List
argument_list|<
name|InputSource
argument_list|>
name|xsdList
parameter_list|)
throws|throws
name|IOException
block|{
name|ClassLoader
name|clzLoader
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
name|Enumeration
argument_list|<
name|URL
argument_list|>
name|urls
init|=
name|clzLoader
operator|.
name|getResources
argument_list|(
name|location
argument_list|)
decl_stmt|;
while|while
condition|(
name|urls
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|URL
name|url
init|=
name|urls
operator|.
name|nextElement
argument_list|()
decl_stmt|;
comment|//from jar files
if|if
condition|(
name|url
operator|.
name|toString
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"jar"
argument_list|)
condition|)
block|{
name|JarURLConnection
name|jarConnection
init|=
operator|(
name|JarURLConnection
operator|)
name|url
operator|.
name|openConnection
argument_list|()
decl_stmt|;
name|JarFile
name|jarFile
init|=
name|jarConnection
operator|.
name|getJarFile
argument_list|()
decl_stmt|;
name|Enumeration
argument_list|<
name|JarEntry
argument_list|>
name|entry
init|=
name|jarFile
operator|.
name|entries
argument_list|()
decl_stmt|;
while|while
condition|(
name|entry
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|JarEntry
name|ele
init|=
name|entry
operator|.
name|nextElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|ele
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".xsd"
argument_list|)
operator|&&
name|ele
operator|.
name|getName
argument_list|()
operator|.
name|indexOf
argument_list|(
name|ToolConstants
operator|.
name|CXF_SCHEMAS_DIR_INJAR
argument_list|)
operator|>
operator|-
literal|1
condition|)
block|{
name|URIResolver
name|resolver
init|=
operator|new
name|URIResolver
argument_list|(
name|ele
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|resolver
operator|.
name|isResolved
argument_list|()
condition|)
block|{
name|InputSource
name|is
init|=
operator|new
name|InputSource
argument_list|(
name|resolver
operator|.
name|getInputStream
argument_list|()
argument_list|)
decl_stmt|;
comment|// Use the resolved URI of the schema if available.
comment|// The ibm jdk won't resolve the schema if we set
comment|// the id to the relative path.
if|if
condition|(
name|resolver
operator|.
name|getURI
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|is
operator|.
name|setSystemId
argument_list|(
name|resolver
operator|.
name|getURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|is
operator|.
name|setSystemId
argument_list|(
name|ele
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|xsdList
operator|.
name|add
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|//from class path direcotry
block|}
elseif|else
if|if
condition|(
name|url
operator|.
name|toString
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"file"
argument_list|)
condition|)
block|{
name|URI
name|loc
init|=
literal|null
decl_stmt|;
try|try
block|{
name|loc
operator|=
name|url
operator|.
name|toURI
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
comment|//
block|}
if|if
condition|(
name|loc
operator|!=
literal|null
condition|)
block|{
name|java
operator|.
name|io
operator|.
name|File
name|file
init|=
operator|new
name|java
operator|.
name|io
operator|.
name|File
argument_list|(
name|loc
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
name|File
index|[]
name|files
init|=
name|file
operator|.
name|listFiles
argument_list|(
operator|new
name|FileFilter
argument_list|()
block|{
specifier|public
name|boolean
name|accept
parameter_list|(
name|File
name|pathname
parameter_list|)
block|{
if|if
condition|(
name|pathname
operator|.
name|getAbsolutePath
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".xsd"
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
argument_list|)
decl_stmt|;
if|if
condition|(
name|files
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|files
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|InputSource
name|is
init|=
operator|new
name|InputSource
argument_list|(
name|files
index|[
name|i
index|]
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
operator|.
name|openStream
argument_list|()
argument_list|)
decl_stmt|;
name|is
operator|.
name|setSystemId
argument_list|(
name|files
index|[
name|i
index|]
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|xsdList
operator|.
name|add
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
name|sort
argument_list|(
name|xsdList
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|sort
parameter_list|(
name|List
argument_list|<
name|InputSource
argument_list|>
name|list
parameter_list|)
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|list
argument_list|,
operator|new
name|Comparator
argument_list|<
name|InputSource
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|InputSource
name|i1
parameter_list|,
name|InputSource
name|i2
parameter_list|)
block|{
if|if
condition|(
name|i1
operator|==
literal|null
operator|&&
name|i2
operator|!=
literal|null
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
elseif|else
if|if
condition|(
name|i1
operator|!=
literal|null
operator|&&
name|i2
operator|==
literal|null
condition|)
block|{
return|return
literal|1
return|;
block|}
elseif|else
if|if
condition|(
name|i1
operator|==
literal|null
operator|&&
name|i2
operator|==
literal|null
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
return|return
name|i1
operator|.
name|getSystemId
argument_list|()
operator|.
name|compareTo
argument_list|(
name|i2
operator|.
name|getSystemId
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

