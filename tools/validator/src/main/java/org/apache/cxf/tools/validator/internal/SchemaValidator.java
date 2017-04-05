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
name|FilenameFilter
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
name|MalformedURLException
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
name|URLConnection
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
name|HashMap
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
name|xml
operator|.
name|XMLConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilderFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|ParserConfigurationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|SAXParser
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|SAXParserFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|sax
operator|.
name|SAXSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|validation
operator|.
name|Schema
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|validation
operator|.
name|SchemaFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|validation
operator|.
name|Validator
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
name|w3c
operator|.
name|dom
operator|.
name|ls
operator|.
name|LSInput
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
name|ls
operator|.
name|LSResourceResolver
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
name|ErrorHandler
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
name|xml
operator|.
name|sax
operator|.
name|SAXException
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
name|SAXParseException
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
name|common
operator|.
name|xmlschema
operator|.
name|LSInputImpl
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
name|ToolException
import|;
end_import

begin_class
specifier|public
class|class
name|SchemaValidator
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
name|SchemaValidator
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|String
index|[]
name|defaultSchemas
decl_stmt|;
specifier|protected
name|String
name|schemaLocation
init|=
literal|"./"
decl_stmt|;
specifier|private
name|String
name|wsdlsrc
decl_stmt|;
specifier|private
name|String
index|[]
name|xsds
decl_stmt|;
specifier|private
name|List
argument_list|<
name|InputSource
argument_list|>
name|schemaFromJar
decl_stmt|;
specifier|private
name|DocumentBuilder
name|docBuilder
decl_stmt|;
specifier|private
name|SAXParser
name|saxParser
decl_stmt|;
specifier|public
name|SchemaValidator
parameter_list|(
name|String
name|schemaDir
parameter_list|)
throws|throws
name|ToolException
block|{
name|schemaLocation
operator|=
name|schemaDir
expr_stmt|;
name|defaultSchemas
operator|=
name|getDefaultSchemas
argument_list|()
expr_stmt|;
block|}
specifier|public
name|SchemaValidator
parameter_list|(
name|String
name|schemaDir
parameter_list|,
name|String
name|wsdl
parameter_list|,
name|String
index|[]
name|schemas
parameter_list|)
throws|throws
name|ToolException
block|{
name|schemaLocation
operator|=
name|schemaDir
expr_stmt|;
name|defaultSchemas
operator|=
name|getDefaultSchemas
argument_list|()
expr_stmt|;
name|wsdlsrc
operator|=
name|wsdl
expr_stmt|;
name|xsds
operator|=
name|schemas
expr_stmt|;
block|}
specifier|public
name|SchemaValidator
parameter_list|(
name|List
argument_list|<
name|InputSource
argument_list|>
name|defaultSchemas
parameter_list|,
name|String
name|wsdl
parameter_list|,
name|String
index|[]
name|schemas
parameter_list|)
block|{
name|schemaLocation
operator|=
literal|null
expr_stmt|;
name|schemaFromJar
operator|=
name|defaultSchemas
expr_stmt|;
name|wsdlsrc
operator|=
name|wsdl
expr_stmt|;
name|xsds
operator|=
name|schemas
expr_stmt|;
block|}
specifier|public
name|boolean
name|isValid
parameter_list|()
block|{
return|return
name|validate
argument_list|(
name|wsdlsrc
argument_list|,
name|xsds
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|validate
parameter_list|(
name|String
name|wsdlsource
parameter_list|,
name|String
index|[]
name|schemas
parameter_list|)
throws|throws
name|ToolException
block|{
name|DocumentBuilderFactory
name|docFactory
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
try|try
block|{
name|docFactory
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|docFactory
operator|.
name|setFeature
argument_list|(
name|XMLConstants
operator|.
name|FEATURE_SECURE_PROCESSING
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|docBuilder
operator|=
name|docFactory
operator|.
name|newDocumentBuilder
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParserConfigurationException
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
name|String
name|systemId
init|=
literal|null
decl_stmt|;
name|systemId
operator|=
name|URIParserUtil
operator|.
name|getAbsoluteURI
argument_list|(
name|wsdlsource
argument_list|)
expr_stmt|;
name|InputSource
name|is
init|=
operator|new
name|InputSource
argument_list|(
name|systemId
argument_list|)
decl_stmt|;
return|return
name|validate
argument_list|(
name|is
argument_list|,
name|schemas
argument_list|)
return|;
block|}
specifier|private
name|Schema
name|createSchema
parameter_list|(
name|List
argument_list|<
name|InputSource
argument_list|>
name|xsdsInJar
parameter_list|,
name|String
index|[]
name|schemas
parameter_list|)
throws|throws
name|SAXException
throws|,
name|IOException
block|{
name|SchemaFactory
name|sf
init|=
name|SchemaFactory
operator|.
name|newInstance
argument_list|(
name|XMLConstants
operator|.
name|W3C_XML_SCHEMA_NS_URI
argument_list|)
decl_stmt|;
name|sf
operator|.
name|setFeature
argument_list|(
name|XMLConstants
operator|.
name|FEATURE_SECURE_PROCESSING
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setProperty
argument_list|(
name|XMLConstants
operator|.
name|ACCESS_EXTERNAL_SCHEMA
argument_list|,
literal|"file"
argument_list|)
expr_stmt|;
name|SchemaResourceResolver
name|resourceResolver
init|=
operator|new
name|SchemaResourceResolver
argument_list|()
decl_stmt|;
name|sf
operator|.
name|setResourceResolver
argument_list|(
name|resourceResolver
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Source
argument_list|>
name|sources
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|InputSource
name|is
range|:
name|xsdsInJar
control|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"CREATE_SCHEMA_LOADED_FROM_JAR"
argument_list|,
name|LOG
argument_list|,
name|is
operator|.
name|getSystemId
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|Document
name|doc
init|=
name|docBuilder
operator|.
name|parse
argument_list|(
name|is
operator|.
name|getByteStream
argument_list|()
argument_list|)
decl_stmt|;
name|DOMSource
name|stream
init|=
operator|new
name|DOMSource
argument_list|(
name|doc
argument_list|,
name|is
operator|.
name|getSystemId
argument_list|()
argument_list|)
decl_stmt|;
name|stream
operator|.
name|setSystemId
argument_list|(
name|is
operator|.
name|getSystemId
argument_list|()
argument_list|)
expr_stmt|;
name|sources
operator|.
name|add
argument_list|(
name|stream
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|schemas
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
name|schemas
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Document
name|doc
init|=
name|docBuilder
operator|.
name|parse
argument_list|(
name|schemas
index|[
name|i
index|]
argument_list|)
decl_stmt|;
name|DOMSource
name|stream
init|=
operator|new
name|DOMSource
argument_list|(
name|doc
argument_list|,
name|schemas
index|[
name|i
index|]
argument_list|)
decl_stmt|;
name|sources
operator|.
name|add
argument_list|(
name|stream
argument_list|)
expr_stmt|;
block|}
block|}
name|Source
index|[]
name|args
init|=
operator|new
name|Source
index|[
name|sources
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
name|sources
operator|.
name|toArray
argument_list|(
name|args
argument_list|)
expr_stmt|;
return|return
name|sf
operator|.
name|newSchema
argument_list|(
name|args
argument_list|)
return|;
block|}
specifier|private
name|Schema
name|createSchema
parameter_list|(
name|String
index|[]
name|schemas
parameter_list|)
throws|throws
name|SAXException
throws|,
name|IOException
block|{
name|SchemaFactory
name|sf
init|=
name|SchemaFactory
operator|.
name|newInstance
argument_list|(
name|XMLConstants
operator|.
name|W3C_XML_SCHEMA_NS_URI
argument_list|)
decl_stmt|;
name|sf
operator|.
name|setFeature
argument_list|(
name|XMLConstants
operator|.
name|FEATURE_SECURE_PROCESSING
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|SchemaResourceResolver
name|resourceResolver
init|=
operator|new
name|SchemaResourceResolver
argument_list|()
decl_stmt|;
name|sf
operator|.
name|setResourceResolver
argument_list|(
name|resourceResolver
argument_list|)
expr_stmt|;
name|Source
index|[]
name|sources
init|=
operator|new
name|Source
index|[
name|schemas
operator|.
name|length
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
name|schemas
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
comment|// need to validate the schema file
name|Document
name|doc
init|=
name|docBuilder
operator|.
name|parse
argument_list|(
name|schemas
index|[
name|i
index|]
argument_list|)
decl_stmt|;
name|DOMSource
name|stream
init|=
operator|new
name|DOMSource
argument_list|(
name|doc
argument_list|,
name|schemas
index|[
name|i
index|]
argument_list|)
decl_stmt|;
name|sources
index|[
name|i
index|]
operator|=
name|stream
expr_stmt|;
block|}
return|return
name|sf
operator|.
name|newSchema
argument_list|(
name|sources
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|validate
parameter_list|(
name|InputSource
name|wsdlsource
parameter_list|,
name|String
index|[]
name|schemas
parameter_list|)
throws|throws
name|ToolException
block|{
name|boolean
name|isValid
init|=
literal|false
decl_stmt|;
name|Schema
name|schema
decl_stmt|;
try|try
block|{
name|SAXParserFactory
name|saxFactory
init|=
name|SAXParserFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|saxFactory
operator|.
name|setFeature
argument_list|(
literal|"http://xml.org/sax/features/namespaces"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|saxFactory
operator|.
name|setFeature
argument_list|(
name|XMLConstants
operator|.
name|FEATURE_SECURE_PROCESSING
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|saxParser
operator|=
name|saxFactory
operator|.
name|newSAXParser
argument_list|()
expr_stmt|;
if|if
condition|(
name|defaultSchemas
operator|!=
literal|null
condition|)
block|{
name|schemas
operator|=
name|addSchemas
argument_list|(
name|defaultSchemas
argument_list|,
name|schemas
argument_list|)
expr_stmt|;
name|schema
operator|=
name|createSchema
argument_list|(
name|schemas
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|schema
operator|=
name|createSchema
argument_list|(
name|schemaFromJar
argument_list|,
name|schemas
argument_list|)
expr_stmt|;
block|}
name|Validator
name|validator
init|=
name|schema
operator|.
name|newValidator
argument_list|()
decl_stmt|;
name|NewStackTraceErrorHandler
name|errHandler
init|=
operator|new
name|NewStackTraceErrorHandler
argument_list|()
decl_stmt|;
name|validator
operator|.
name|setErrorHandler
argument_list|(
name|errHandler
argument_list|)
expr_stmt|;
name|SAXSource
name|saxSource
init|=
operator|new
name|SAXSource
argument_list|(
name|saxParser
operator|.
name|getXMLReader
argument_list|()
argument_list|,
name|wsdlsource
argument_list|)
decl_stmt|;
name|validator
operator|.
name|validate
argument_list|(
name|saxSource
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|errHandler
operator|.
name|isValid
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ToolException
argument_list|(
name|errHandler
operator|.
name|getErrorMessages
argument_list|()
argument_list|)
throw|;
block|}
name|isValid
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
throw|throw
operator|new
name|ToolException
argument_list|(
literal|"Cannot get the wsdl "
operator|+
name|wsdlsource
operator|.
name|getSystemId
argument_list|()
argument_list|,
name|ioe
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|SAXException
name|saxEx
parameter_list|)
block|{
throw|throw
operator|new
name|ToolException
argument_list|(
name|saxEx
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|ParserConfigurationException
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
return|return
name|isValid
return|;
block|}
specifier|private
name|String
index|[]
name|addSchemas
parameter_list|(
name|String
index|[]
name|defaults
parameter_list|,
name|String
index|[]
name|schemas
parameter_list|)
block|{
if|if
condition|(
name|schemas
operator|==
literal|null
operator|||
name|schemas
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
name|defaultSchemas
return|;
block|}
name|String
index|[]
name|ss
init|=
operator|new
name|String
index|[
name|schemas
operator|.
name|length
operator|+
name|defaults
operator|.
name|length
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|defaults
argument_list|,
literal|0
argument_list|,
name|ss
argument_list|,
literal|0
argument_list|,
name|defaults
operator|.
name|length
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|schemas
argument_list|,
literal|0
argument_list|,
name|ss
argument_list|,
name|defaults
operator|.
name|length
argument_list|,
name|schemas
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|ss
return|;
block|}
specifier|private
name|String
index|[]
name|getDefaultSchemas
parameter_list|()
throws|throws
name|ToolException
block|{
name|String
name|loc
init|=
name|schemaLocation
decl_stmt|;
if|if
condition|(
name|loc
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|loc
operator|.
name|trim
argument_list|()
argument_list|)
condition|)
block|{
name|loc
operator|=
literal|"./"
expr_stmt|;
block|}
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|loc
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|.
name|exists
argument_list|()
operator|&&
name|f
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|FilenameFilter
name|filter
init|=
operator|new
name|FilenameFilter
argument_list|()
block|{
specifier|public
name|boolean
name|accept
parameter_list|(
name|File
name|dir
parameter_list|,
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|.
name|toLowerCase
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".xsd"
argument_list|)
operator|&&
operator|!
operator|new
name|File
argument_list|(
name|dir
operator|.
name|getPath
argument_list|()
operator|+
name|File
operator|.
name|separator
operator|+
name|name
argument_list|)
operator|.
name|isDirectory
argument_list|()
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
decl_stmt|;
name|File
index|[]
name|files
init|=
name|f
operator|.
name|listFiles
argument_list|(
name|filter
argument_list|)
decl_stmt|;
if|if
condition|(
name|files
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|xsdUrls
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|files
operator|.
name|length
argument_list|)
decl_stmt|;
for|for
control|(
name|File
name|file
range|:
name|files
control|)
block|{
try|try
block|{
name|String
name|s
init|=
name|file
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|xsdUrls
operator|.
name|add
argument_list|(
name|s
argument_list|)
expr_stmt|;
if|if
condition|(
name|s
operator|.
name|contains
argument_list|(
literal|"http-conf"
argument_list|)
condition|)
block|{
name|xsdUrls
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|MalformedURLException
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
return|return
name|xsdUrls
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|xsdUrls
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

begin_class
class|class
name|NewStackTraceErrorHandler
implements|implements
name|ErrorHandler
block|{
specifier|protected
name|boolean
name|valid
decl_stmt|;
specifier|private
name|StringBuilder
name|buffer
decl_stmt|;
specifier|private
name|int
name|numErrors
decl_stmt|;
specifier|private
name|List
argument_list|<
name|SAXParseException
argument_list|>
name|errors
decl_stmt|;
name|NewStackTraceErrorHandler
parameter_list|()
block|{
name|valid
operator|=
literal|true
expr_stmt|;
name|numErrors
operator|=
literal|0
expr_stmt|;
name|buffer
operator|=
operator|new
name|StringBuilder
argument_list|()
expr_stmt|;
name|errors
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|error
parameter_list|(
name|SAXParseException
name|ex
parameter_list|)
throws|throws
name|SAXParseException
block|{
name|addError
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|fatalError
parameter_list|(
name|SAXParseException
name|ex
parameter_list|)
block|{
name|addError
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|warning
parameter_list|(
name|SAXParseException
name|ex
parameter_list|)
block|{
comment|// Warning messages are ignored.
comment|// return;
block|}
name|boolean
name|isValid
parameter_list|()
block|{
return|return
name|valid
return|;
block|}
name|int
name|getTotalErrors
parameter_list|()
block|{
return|return
name|numErrors
return|;
block|}
name|String
name|getErrorMessages
parameter_list|()
block|{
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
name|SAXParseException
index|[]
name|getErrors
parameter_list|()
block|{
if|if
condition|(
name|errors
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|errors
operator|.
name|toArray
argument_list|(
operator|new
name|SAXParseException
index|[
name|errors
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
name|void
name|addError
parameter_list|(
name|String
name|msg
parameter_list|,
name|SAXParseException
name|ex
parameter_list|)
block|{
name|valid
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|numErrors
operator|==
literal|0
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"\n\n"
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|numErrors
operator|++
expr_stmt|;
name|errors
operator|.
name|add
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|getErrorMessage
parameter_list|(
name|SAXParseException
name|ex
parameter_list|)
block|{
return|return
literal|"line "
operator|+
name|ex
operator|.
name|getLineNumber
argument_list|()
operator|+
literal|" column "
operator|+
name|ex
operator|.
name|getColumnNumber
argument_list|()
operator|+
literal|" of "
operator|+
name|ex
operator|.
name|getSystemId
argument_list|()
operator|+
literal|": "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
return|;
block|}
specifier|private
name|void
name|addError
parameter_list|(
name|SAXParseException
name|ex
parameter_list|)
block|{
name|addError
argument_list|(
name|getErrorMessage
argument_list|(
name|ex
argument_list|)
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
end_class

begin_class
class|class
name|SchemaResourceResolver
implements|implements
name|LSResourceResolver
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|SchemaValidator
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|NSFILEMAP
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
static|static
block|{
name|NSFILEMAP
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|XML_NAMESPACE_URI
argument_list|,
literal|"xml.xsd"
argument_list|)
expr_stmt|;
name|NSFILEMAP
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|WSDL_NAMESPACE_URI
argument_list|,
literal|"wsdl.xsd"
argument_list|)
expr_stmt|;
name|NSFILEMAP
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|SCHEMA_URI
argument_list|,
literal|"XMLSchema.xsd"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|LSInput
name|loadLSInput
parameter_list|(
name|String
name|ns
parameter_list|)
block|{
name|String
name|path
init|=
name|ToolConstants
operator|.
name|CXF_SCHEMAS_DIR_INJAR
operator|+
name|NSFILEMAP
operator|.
name|get
argument_list|(
name|ns
argument_list|)
decl_stmt|;
name|URL
name|url
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|LSInput
name|lsin
init|=
operator|new
name|LSInputImpl
argument_list|()
decl_stmt|;
name|lsin
operator|.
name|setSystemId
argument_list|(
name|url
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|lsin
operator|.
name|setByteStream
argument_list|(
name|url
operator|.
name|openStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|lsin
return|;
block|}
specifier|public
name|LSInput
name|resolveResource
parameter_list|(
name|String
name|type
parameter_list|,
name|String
name|namespaceURI
parameter_list|,
name|String
name|publicId
parameter_list|,
name|String
name|systemId
parameter_list|,
name|String
name|baseURI
parameter_list|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"RESOLVE_SCHEMA"
argument_list|,
name|LOG
argument_list|,
name|namespaceURI
argument_list|,
name|systemId
argument_list|,
name|baseURI
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|NSFILEMAP
operator|.
name|containsKey
argument_list|(
name|namespaceURI
argument_list|)
condition|)
block|{
return|return
name|loadLSInput
argument_list|(
name|namespaceURI
argument_list|)
return|;
block|}
name|LSInput
name|lsin
init|=
literal|null
decl_stmt|;
name|String
name|resURL
init|=
literal|null
decl_stmt|;
name|String
name|localFile
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|systemId
operator|!=
literal|null
condition|)
block|{
name|String
name|schemaLocation
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|baseURI
operator|!=
literal|null
condition|)
block|{
name|schemaLocation
operator|=
name|baseURI
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|baseURI
operator|.
name|lastIndexOf
argument_list|(
literal|"/"
argument_list|)
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|systemId
operator|.
name|indexOf
argument_list|(
literal|"http://"
argument_list|)
operator|<
literal|0
condition|)
block|{
name|resURL
operator|=
name|schemaLocation
operator|+
name|systemId
expr_stmt|;
block|}
else|else
block|{
name|resURL
operator|=
name|systemId
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|namespaceURI
operator|!=
literal|null
condition|)
block|{
name|resURL
operator|=
name|namespaceURI
expr_stmt|;
block|}
if|if
condition|(
name|resURL
operator|!=
literal|null
operator|&&
name|resURL
operator|.
name|startsWith
argument_list|(
literal|"http://"
argument_list|)
condition|)
block|{
name|String
name|filename
init|=
name|NSFILEMAP
operator|.
name|get
argument_list|(
name|resURL
argument_list|)
decl_stmt|;
if|if
condition|(
name|filename
operator|!=
literal|null
condition|)
block|{
name|localFile
operator|=
name|ToolConstants
operator|.
name|CXF_SCHEMAS_DIR_INJAR
operator|+
name|filename
expr_stmt|;
block|}
else|else
block|{
name|URL
name|url
decl_stmt|;
name|URLConnection
name|urlCon
init|=
literal|null
decl_stmt|;
try|try
block|{
name|url
operator|=
operator|new
name|URL
argument_list|(
name|resURL
argument_list|)
expr_stmt|;
name|urlCon
operator|=
name|url
operator|.
name|openConnection
argument_list|()
expr_stmt|;
name|urlCon
operator|.
name|setUseCaches
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|lsin
operator|=
operator|new
name|LSInputImpl
argument_list|()
expr_stmt|;
name|lsin
operator|.
name|setSystemId
argument_list|(
name|resURL
argument_list|)
expr_stmt|;
name|lsin
operator|.
name|setByteStream
argument_list|(
name|urlCon
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|=
operator|new
name|Message
argument_list|(
literal|"RESOLVE_FROM_REMOTE"
argument_list|,
name|LOG
argument_list|,
name|url
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|lsin
return|;
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
return|return
literal|null
return|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|resURL
operator|!=
literal|null
operator|&&
operator|!
name|resURL
operator|.
name|startsWith
argument_list|(
literal|"http:"
argument_list|)
condition|)
block|{
name|localFile
operator|=
name|resURL
expr_stmt|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
name|URIResolver
name|resolver
decl_stmt|;
try|try
block|{
name|msg
operator|=
operator|new
name|Message
argument_list|(
literal|"RESOLVE_FROM_LOCAL"
argument_list|,
name|LOG
argument_list|,
name|localFile
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|resolver
operator|=
operator|new
name|URIResolver
argument_list|(
name|localFile
argument_list|)
expr_stmt|;
if|if
condition|(
name|resolver
operator|.
name|isResolved
argument_list|()
condition|)
block|{
name|lsin
operator|=
operator|new
name|LSInputImpl
argument_list|()
expr_stmt|;
name|lsin
operator|.
name|setSystemId
argument_list|(
name|localFile
argument_list|)
expr_stmt|;
name|lsin
operator|.
name|setByteStream
argument_list|(
name|resolver
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|lsin
return|;
block|}
block|}
end_class

end_unit

