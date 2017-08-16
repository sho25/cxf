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
name|misc
operator|.
name|processor
package|;
end_package

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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
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
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
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
name|Types
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
name|javax
operator|.
name|wsdl
operator|.
name|extensions
operator|.
name|ExtensibilityElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|extensions
operator|.
name|ExtensionRegistry
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|extensions
operator|.
name|schema
operator|.
name|Schema
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|factory
operator|.
name|WSDLFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|xml
operator|.
name|WSDLWriter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|Element
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
name|tools
operator|.
name|common
operator|.
name|Processor
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
name|common
operator|.
name|dom
operator|.
name|ExtendedDocumentBuilder
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
name|cxf
operator|.
name|tools
operator|.
name|util
operator|.
name|OutputStreamCreator
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
name|wsdlto
operator|.
name|frontend
operator|.
name|jaxws
operator|.
name|customization
operator|.
name|JAXWSBinding
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
name|wsdlto
operator|.
name|frontend
operator|.
name|jaxws
operator|.
name|customization
operator|.
name|JAXWSBindingDeserializer
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
name|WSDLConstants
import|;
end_import

begin_class
specifier|public
class|class
name|XSDToWSDLProcessor
implements|implements
name|Processor
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
name|XSDToWSDLProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|XSD_FILE_NAME_EXT
init|=
literal|".xsd"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|WSDL_FILE_NAME_EXT
init|=
literal|".wsdl"
decl_stmt|;
specifier|private
name|Definition
name|wsdlDefinition
decl_stmt|;
specifier|private
name|ExtensionRegistry
name|registry
decl_stmt|;
specifier|private
name|WSDLFactory
name|wsdlFactory
decl_stmt|;
specifier|private
name|String
name|xsdUrl
decl_stmt|;
specifier|private
specifier|final
name|ExtendedDocumentBuilder
name|xsdBuilder
init|=
operator|new
name|ExtendedDocumentBuilder
argument_list|()
decl_stmt|;
specifier|private
name|Document
name|xsdDoc
decl_stmt|;
specifier|private
name|ToolContext
name|env
decl_stmt|;
specifier|public
name|void
name|process
parameter_list|()
throws|throws
name|ToolException
block|{
name|envParamSetting
argument_list|()
expr_stmt|;
name|initXSD
argument_list|()
expr_stmt|;
name|initWSDL
argument_list|()
expr_stmt|;
name|addWSDLTypes
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|setEnvironment
parameter_list|(
name|ToolContext
name|newEnv
parameter_list|)
block|{
name|this
operator|.
name|env
operator|=
name|newEnv
expr_stmt|;
block|}
specifier|private
name|void
name|envParamSetting
parameter_list|()
block|{
name|xsdUrl
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
name|CFG_XSDURL
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|env
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_NAME
argument_list|)
condition|)
block|{
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_NAME
argument_list|,
name|xsdUrl
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|xsdUrl
operator|.
name|length
argument_list|()
operator|-
literal|4
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|initWSDL
parameter_list|()
throws|throws
name|ToolException
block|{
try|try
block|{
name|wsdlFactory
operator|=
name|WSDLFactory
operator|.
name|newInstance
argument_list|()
expr_stmt|;
name|wsdlDefinition
operator|=
name|wsdlFactory
operator|.
name|newDefinition
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSDLException
name|we
parameter_list|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"FAIL_TO_CREATE_WSDL_DEFINITION"
argument_list|,
name|LOG
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|,
name|we
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|initXSD
parameter_list|()
throws|throws
name|ToolException
block|{
name|InputStream
name|in
decl_stmt|;
try|try
block|{
name|in
operator|=
operator|new
name|URL
argument_list|(
name|xsdUrl
argument_list|)
operator|.
name|openStream
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|m
parameter_list|)
block|{
try|try
block|{
name|in
operator|=
name|Files
operator|.
name|newInputStream
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|xsdUrl
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"FAIL_TO_OPEN_XSD_FILE"
argument_list|,
name|LOG
argument_list|,
name|xsdUrl
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|,
name|ioe
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|in
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Cannot create a ToolSpec object from a null stream"
argument_list|)
throw|;
block|}
try|try
block|{
name|xsdBuilder
operator|.
name|setValidating
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|this
operator|.
name|xsdDoc
operator|=
name|xsdBuilder
operator|.
name|parse
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"FAIL_TO_PARSE_TOOLSPEC"
argument_list|,
name|LOG
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|addWSDLTypes
parameter_list|()
throws|throws
name|ToolException
block|{
name|Element
name|sourceElement
init|=
name|this
operator|.
name|xsdDoc
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
name|Element
name|targetElement
init|=
operator|(
name|Element
operator|)
name|sourceElement
operator|.
name|cloneNode
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|this
operator|.
name|wsdlDefinition
operator|.
name|setTargetNamespace
argument_list|(
operator|(
name|String
operator|)
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_NAMESPACE
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|wsdlDefinition
operator|.
name|setQName
argument_list|(
operator|new
name|QName
argument_list|(
name|WSDLConstants
operator|.
name|NS_WSDL11
argument_list|,
operator|(
name|String
operator|)
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_NAME
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|Types
name|types
init|=
name|this
operator|.
name|wsdlDefinition
operator|.
name|createTypes
argument_list|()
decl_stmt|;
name|ExtensibilityElement
name|extElement
decl_stmt|;
try|try
block|{
name|registry
operator|=
name|wsdlFactory
operator|.
name|newPopulatedExtensionRegistry
argument_list|()
expr_stmt|;
name|registerJAXWSBinding
argument_list|(
name|Definition
operator|.
name|class
argument_list|)
expr_stmt|;
name|registerJAXWSBinding
argument_list|(
name|Types
operator|.
name|class
argument_list|)
expr_stmt|;
name|registerJAXWSBinding
argument_list|(
name|Schema
operator|.
name|class
argument_list|)
expr_stmt|;
name|extElement
operator|=
name|registry
operator|.
name|createExtension
argument_list|(
name|Types
operator|.
name|class
argument_list|,
name|WSDLConstants
operator|.
name|QNAME_SCHEMA
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSDLException
name|wse
parameter_list|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"FAIL_TO_CREATE_SCHEMA_EXTENSION"
argument_list|,
name|LOG
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|,
name|wse
argument_list|)
throw|;
block|}
operator|(
operator|(
name|Schema
operator|)
name|extElement
operator|)
operator|.
name|setElement
argument_list|(
name|targetElement
argument_list|)
expr_stmt|;
name|types
operator|.
name|addExtensibilityElement
argument_list|(
name|extElement
argument_list|)
expr_stmt|;
name|this
operator|.
name|wsdlDefinition
operator|.
name|setTypes
argument_list|(
name|types
argument_list|)
expr_stmt|;
name|WSDLWriter
name|wsdlWriter
init|=
name|wsdlFactory
operator|.
name|newWSDLWriter
argument_list|()
decl_stmt|;
name|Writer
name|outputWriter
init|=
name|getOutputWriter
argument_list|()
decl_stmt|;
try|try
block|{
name|wsdlWriter
operator|.
name|writeWSDL
argument_list|(
name|wsdlDefinition
argument_list|,
name|outputWriter
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSDLException
name|wse
parameter_list|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"FAIL_TO_WRITE_WSDL"
argument_list|,
name|LOG
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|,
name|wse
argument_list|)
throw|;
block|}
try|try
block|{
name|outputWriter
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"FAIL_TO_CLOSE_WSDL_FILE"
argument_list|,
name|LOG
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|,
name|ioe
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|registerJAXWSBinding
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clz
parameter_list|)
block|{
name|registry
operator|.
name|registerDeserializer
argument_list|(
name|clz
argument_list|,
name|ToolConstants
operator|.
name|JAXWS_BINDINGS
argument_list|,
operator|new
name|JAXWSBindingDeserializer
argument_list|()
argument_list|)
expr_stmt|;
name|registry
operator|.
name|mapExtensionTypes
argument_list|(
name|clz
argument_list|,
name|ToolConstants
operator|.
name|JAXWS_BINDINGS
argument_list|,
name|JAXWSBinding
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Writer
name|getOutputWriter
parameter_list|()
throws|throws
name|ToolException
block|{
name|Writer
name|writer
init|=
literal|null
decl_stmt|;
name|String
name|newName
init|=
literal|null
decl_stmt|;
name|String
name|outputDir
decl_stmt|;
if|if
condition|(
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_OUTPUTFILE
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|newName
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
name|CFG_OUTPUTFILE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|oldName
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
name|CFG_XSDURL
argument_list|)
decl_stmt|;
name|int
name|position
init|=
name|oldName
operator|.
name|lastIndexOf
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
if|if
condition|(
name|position
operator|<
literal|0
condition|)
block|{
name|position
operator|=
name|oldName
operator|.
name|lastIndexOf
argument_list|(
literal|"\\"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|position
operator|>=
literal|0
condition|)
block|{
name|oldName
operator|=
name|oldName
operator|.
name|substring
argument_list|(
name|position
operator|+
literal|1
argument_list|,
name|oldName
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|oldName
operator|.
name|toLowerCase
argument_list|()
operator|.
name|indexOf
argument_list|(
name|XSD_FILE_NAME_EXT
argument_list|)
operator|>=
literal|0
condition|)
block|{
name|newName
operator|=
name|oldName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|oldName
operator|.
name|length
argument_list|()
operator|-
literal|4
argument_list|)
operator|+
name|WSDL_FILE_NAME_EXT
expr_stmt|;
block|}
else|else
block|{
name|newName
operator|=
name|oldName
operator|+
name|WSDL_FILE_NAME_EXT
expr_stmt|;
block|}
block|}
if|if
condition|(
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_OUTPUTDIR
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|outputDir
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
name|CFG_OUTPUTDIR
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
operator|(
literal|"/"
operator|.
name|equals
argument_list|(
name|outputDir
operator|.
name|substring
argument_list|(
name|outputDir
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|)
operator|||
literal|"\\"
operator|.
name|equals
argument_list|(
name|outputDir
operator|.
name|substring
argument_list|(
name|outputDir
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|)
operator|)
condition|)
block|{
name|outputDir
operator|=
name|outputDir
operator|+
literal|"/"
expr_stmt|;
block|}
block|}
else|else
block|{
name|outputDir
operator|=
literal|"./"
expr_stmt|;
block|}
name|FileWriterUtil
name|fw
init|=
operator|new
name|FileWriterUtil
argument_list|(
name|outputDir
argument_list|,
name|env
operator|.
name|get
argument_list|(
name|OutputStreamCreator
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|writer
operator|=
name|fw
operator|.
name|getWriter
argument_list|(
literal|""
argument_list|,
name|newName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"FAIL_TO_WRITE_FILE"
argument_list|,
name|LOG
argument_list|,
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_OUTPUTDIR
argument_list|)
operator|+
name|System
operator|.
name|getProperty
argument_list|(
literal|"file.seperator"
argument_list|)
operator|+
name|newName
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|,
name|ioe
argument_list|)
throw|;
block|}
return|return
name|writer
return|;
block|}
block|}
end_class

end_unit

