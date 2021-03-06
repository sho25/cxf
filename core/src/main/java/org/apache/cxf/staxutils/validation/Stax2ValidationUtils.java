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
name|staxutils
operator|.
name|validation
package|;
end_package

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
name|TreeMap
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
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamWriter
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
name|endpoint
operator|.
name|Endpoint
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
name|interceptor
operator|.
name|Fault
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
name|service
operator|.
name|model
operator|.
name|SchemaInfo
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
name|service
operator|.
name|model
operator|.
name|ServiceInfo
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
name|staxutils
operator|.
name|DepthXMLStreamReader
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
name|staxutils
operator|.
name|StaxUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchemaExternal
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|stax2
operator|.
name|XMLStreamReader2
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|stax2
operator|.
name|XMLStreamWriter2
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|stax2
operator|.
name|validation
operator|.
name|ValidationProblemHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|stax2
operator|.
name|validation
operator|.
name|XMLValidationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|stax2
operator|.
name|validation
operator|.
name|XMLValidationProblem
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|stax2
operator|.
name|validation
operator|.
name|XMLValidationSchema
import|;
end_import

begin_comment
comment|/**  * This class touches stax2 API, so it is kept separate to allow graceful fallback.  */
end_comment

begin_class
class|class
name|Stax2ValidationUtils
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
name|Stax2ValidationUtils
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|KEY
init|=
name|XMLValidationSchema
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|boolean
name|HAS_WOODSTOX
decl_stmt|;
static|static
block|{
name|boolean
name|hasw
init|=
literal|false
decl_stmt|;
try|try
block|{
operator|new
name|ResolvingGrammarReaderController
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// will throw if msv isn't available
operator|new
name|W3CMultiSchemaFactory
argument_list|()
expr_stmt|;
comment|// will throw if wrong woodstox.
name|hasw
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// ignore
block|}
name|HAS_WOODSTOX
operator|=
name|hasw
expr_stmt|;
block|}
name|Stax2ValidationUtils
parameter_list|()
block|{
if|if
condition|(
operator|!
name|HAS_WOODSTOX
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Could not load woodstox"
argument_list|)
throw|;
block|}
block|}
comment|/**      * {@inheritDoc}      *      * @throws XMLStreamException      */
specifier|public
name|boolean
name|setupValidation
parameter_list|(
name|XMLStreamReader
name|reader
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|ServiceInfo
name|serviceInfo
parameter_list|)
throws|throws
name|XMLStreamException
block|{
comment|// Gosh, this is bad, but I don't know a better solution, unless we're willing
comment|// to require the stax2 API no matter what.
name|XMLStreamReader
name|effectiveReader
init|=
name|reader
decl_stmt|;
if|if
condition|(
name|effectiveReader
operator|instanceof
name|DepthXMLStreamReader
condition|)
block|{
name|effectiveReader
operator|=
operator|(
operator|(
name|DepthXMLStreamReader
operator|)
name|reader
operator|)
operator|.
name|getReader
argument_list|()
expr_stmt|;
block|}
specifier|final
name|XMLStreamReader2
name|reader2
init|=
operator|(
name|XMLStreamReader2
operator|)
name|effectiveReader
decl_stmt|;
name|XMLValidationSchema
name|vs
init|=
name|getValidator
argument_list|(
name|endpoint
argument_list|,
name|serviceInfo
argument_list|)
decl_stmt|;
if|if
condition|(
name|vs
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|reader2
operator|.
name|setValidationProblemHandler
argument_list|(
operator|new
name|ValidationProblemHandler
argument_list|()
block|{
specifier|public
name|void
name|reportProblem
parameter_list|(
name|XMLValidationProblem
name|problem
parameter_list|)
throws|throws
name|XMLValidationException
block|{
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
name|Message
argument_list|(
literal|"READ_VALIDATION_ERROR"
argument_list|,
name|LOG
argument_list|,
name|problem
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|,
name|Fault
operator|.
name|FAULT_CODE_CLIENT
argument_list|)
throw|;
block|}
block|}
argument_list|)
expr_stmt|;
name|reader2
operator|.
name|validateAgainst
argument_list|(
name|vs
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|setupValidation
parameter_list|(
name|XMLStreamWriter
name|writer
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|ServiceInfo
name|serviceInfo
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|XMLStreamWriter2
name|writer2
init|=
operator|(
name|XMLStreamWriter2
operator|)
name|writer
decl_stmt|;
name|XMLValidationSchema
name|vs
init|=
name|getValidator
argument_list|(
name|endpoint
argument_list|,
name|serviceInfo
argument_list|)
decl_stmt|;
if|if
condition|(
name|vs
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|writer2
operator|.
name|setValidationProblemHandler
argument_list|(
operator|new
name|ValidationProblemHandler
argument_list|()
block|{
specifier|public
name|void
name|reportProblem
parameter_list|(
name|XMLValidationProblem
name|problem
parameter_list|)
throws|throws
name|XMLValidationException
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|problem
operator|.
name|getMessage
argument_list|()
argument_list|,
name|LOG
argument_list|)
throw|;
block|}
block|}
argument_list|)
expr_stmt|;
name|writer2
operator|.
name|validateAgainst
argument_list|(
name|vs
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|/**      * Create woodstox validator for a schema set.      *      * @throws XMLStreamException      */
specifier|private
name|XMLValidationSchema
name|getValidator
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|ServiceInfo
name|serviceInfo
parameter_list|)
throws|throws
name|XMLStreamException
block|{
synchronized|synchronized
init|(
name|endpoint
init|)
block|{
name|XMLValidationSchema
name|ret
init|=
operator|(
name|XMLValidationSchema
operator|)
name|endpoint
operator|.
name|get
argument_list|(
name|KEY
argument_list|)
decl_stmt|;
if|if
condition|(
name|ret
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|endpoint
operator|.
name|containsKey
argument_list|(
name|KEY
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|EmbeddedSchema
argument_list|>
name|sources
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|SchemaInfo
name|schemaInfo
range|:
name|serviceInfo
operator|.
name|getSchemas
argument_list|()
control|)
block|{
name|XmlSchema
name|sch
init|=
name|schemaInfo
operator|.
name|getSchema
argument_list|()
decl_stmt|;
name|String
name|uri
init|=
name|sch
operator|.
name|getTargetNamespace
argument_list|()
decl_stmt|;
if|if
condition|(
name|XMLConstants
operator|.
name|W3C_XML_SCHEMA_NS_URI
operator|.
name|equals
argument_list|(
name|uri
argument_list|)
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|sch
operator|.
name|getTargetNamespace
argument_list|()
operator|==
literal|null
operator|&&
operator|!
name|sch
operator|.
name|getExternals
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|XmlSchemaExternal
name|xmlSchemaExternal
range|:
name|sch
operator|.
name|getExternals
argument_list|()
control|)
block|{
name|addSchema
argument_list|(
name|sources
argument_list|,
name|xmlSchemaExternal
operator|.
name|getSchema
argument_list|()
argument_list|,
name|getElement
argument_list|(
name|xmlSchemaExternal
operator|.
name|getSchema
argument_list|()
operator|.
name|getSourceURI
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
continue|continue;
block|}
elseif|else
if|if
condition|(
name|sch
operator|.
name|getTargetNamespace
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"An Schema without imports must have a targetNamespace"
argument_list|)
throw|;
block|}
name|addSchema
argument_list|(
name|sources
argument_list|,
name|sch
argument_list|,
name|schemaInfo
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|W3CMultiSchemaFactory
name|factory
init|=
operator|new
name|W3CMultiSchemaFactory
argument_list|()
decl_stmt|;
comment|// I don't think that we need the baseURI.
try|try
block|{
name|ret
operator|=
name|factory
operator|.
name|loadSchemas
argument_list|(
literal|null
argument_list|,
name|sources
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|put
argument_list|(
name|KEY
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"Problem loading schemas. Falling back to slower method."
argument_list|,
name|ret
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|put
argument_list|(
name|KEY
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|ret
return|;
block|}
block|}
specifier|private
name|void
name|addSchema
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|EmbeddedSchema
argument_list|>
name|sources
parameter_list|,
name|XmlSchema
name|schema
parameter_list|,
name|Element
name|element
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|String
name|schemaSystemId
init|=
name|schema
operator|.
name|getSourceURI
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|schemaSystemId
condition|)
block|{
name|schemaSystemId
operator|=
name|schema
operator|.
name|getTargetNamespace
argument_list|()
expr_stmt|;
block|}
name|EmbeddedSchema
name|embeddedSchema
init|=
operator|new
name|EmbeddedSchema
argument_list|(
name|schemaSystemId
argument_list|,
name|element
argument_list|)
decl_stmt|;
name|sources
operator|.
name|put
argument_list|(
name|schema
operator|.
name|getTargetNamespace
argument_list|()
argument_list|,
name|embeddedSchema
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Element
name|getElement
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|InputSource
name|in
init|=
operator|new
name|InputSource
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|Document
name|doc
init|=
name|StaxUtils
operator|.
name|read
argument_list|(
name|in
argument_list|)
decl_stmt|;
return|return
name|doc
operator|.
name|getDocumentElement
argument_list|()
return|;
block|}
block|}
end_class

end_unit

