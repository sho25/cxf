begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/*  * Code in this file derives from source code in Woodstox which   * carries a ASL 2.0 license.  */
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
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
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
name|Locator
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ctc
operator|.
name|wstx
operator|.
name|msv
operator|.
name|BaseSchemaFactory
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ctc
operator|.
name|wstx
operator|.
name|msv
operator|.
name|W3CSchema
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|msv
operator|.
name|grammar
operator|.
name|ExpressionPool
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|msv
operator|.
name|grammar
operator|.
name|xmlschema
operator|.
name|XMLSchemaGrammar
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|msv
operator|.
name|grammar
operator|.
name|xmlschema
operator|.
name|XMLSchemaSchema
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|msv
operator|.
name|reader
operator|.
name|GrammarReaderController
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|msv
operator|.
name|reader
operator|.
name|State
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|msv
operator|.
name|reader
operator|.
name|xmlschema
operator|.
name|MultiSchemaReader
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|msv
operator|.
name|reader
operator|.
name|xmlschema
operator|.
name|SchemaState
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|msv
operator|.
name|reader
operator|.
name|xmlschema
operator|.
name|XMLSchemaReader
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
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|W3CMultiSchemaFactory
extends|extends
name|BaseSchemaFactory
block|{
specifier|private
name|MultiSchemaReader
name|multiSchemaReader
decl_stmt|;
specifier|private
name|SAXParserFactory
name|parserFactory
decl_stmt|;
specifier|private
name|RecursiveAllowedXMLSchemaReader
name|xmlSchemaReader
decl_stmt|;
specifier|public
name|W3CMultiSchemaFactory
parameter_list|()
block|{
name|super
argument_list|(
name|XMLValidationSchema
operator|.
name|SCHEMA_ID_W3C_SCHEMA
argument_list|)
expr_stmt|;
block|}
specifier|static
class|class
name|RecursiveAllowedXMLSchemaReader
extends|extends
name|XMLSchemaReader
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|sysIds
init|=
operator|new
name|TreeSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|RecursiveAllowedXMLSchemaReader
parameter_list|(
name|GrammarReaderController
name|controller
parameter_list|,
name|SAXParserFactory
name|parserFactory
parameter_list|)
block|{
name|super
argument_list|(
name|controller
argument_list|,
name|parserFactory
argument_list|,
operator|new
name|StateFactory
argument_list|()
block|{
specifier|public
name|State
name|schemaHead
parameter_list|(
name|String
name|expectedNamespace
parameter_list|)
block|{
return|return
operator|new
name|SchemaState
argument_list|(
name|expectedNamespace
argument_list|)
block|{
specifier|private
name|XMLSchemaSchema
name|old
decl_stmt|;
specifier|protected
name|void
name|endSelf
parameter_list|()
block|{
name|super
operator|.
name|endSelf
argument_list|()
expr_stmt|;
name|RecursiveAllowedXMLSchemaReader
name|r
init|=
operator|(
name|RecursiveAllowedXMLSchemaReader
operator|)
name|reader
decl_stmt|;
name|r
operator|.
name|currentSchema
operator|=
name|old
expr_stmt|;
block|}
specifier|protected
name|void
name|onTargetNamespaceResolved
parameter_list|(
name|String
name|targetNs
parameter_list|,
name|boolean
name|ignoreContents
parameter_list|)
block|{
name|RecursiveAllowedXMLSchemaReader
name|r
init|=
operator|(
name|RecursiveAllowedXMLSchemaReader
operator|)
name|reader
decl_stmt|;
comment|// sets new XMLSchemaGrammar object.
name|old
operator|=
name|r
operator|.
name|currentSchema
expr_stmt|;
name|r
operator|.
name|currentSchema
operator|=
name|r
operator|.
name|getOrCreateSchema
argument_list|(
name|targetNs
argument_list|)
expr_stmt|;
if|if
condition|(
name|ignoreContents
condition|)
block|{
return|return;
block|}
if|if
condition|(
operator|!
name|r
operator|.
name|isSchemaDefined
argument_list|(
name|r
operator|.
name|currentSchema
argument_list|)
condition|)
block|{
name|r
operator|.
name|markSchemaAsDefined
argument_list|(
name|r
operator|.
name|currentSchema
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|;
block|}
block|}
argument_list|,
operator|new
name|ExpressionPool
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setLocator
parameter_list|(
name|Locator
name|locator
parameter_list|)
block|{
if|if
condition|(
name|locator
operator|==
literal|null
operator|&&
name|getLocator
argument_list|()
operator|!=
literal|null
operator|&&
name|getLocator
argument_list|()
operator|.
name|getSystemId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sysIds
operator|.
name|add
argument_list|(
name|getLocator
argument_list|()
operator|.
name|getSystemId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|setLocator
argument_list|(
name|locator
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|switchSource
parameter_list|(
name|Source
name|source
parameter_list|,
name|State
name|newState
parameter_list|)
block|{
name|String
name|url
init|=
name|source
operator|.
name|getSystemId
argument_list|()
decl_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
operator|&&
name|sysIds
operator|.
name|contains
argument_list|(
name|url
argument_list|)
condition|)
block|{
return|return;
block|}
name|super
operator|.
name|switchSource
argument_list|(
name|source
argument_list|,
name|newState
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|XMLValidationSchema
name|loadSchemas
parameter_list|(
name|String
name|baseURI
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|EmbeddedSchema
argument_list|>
name|sources
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|parserFactory
operator|=
name|getSaxFactory
argument_list|()
expr_stmt|;
name|ResolvingGrammarReaderController
name|ctrl
init|=
operator|new
name|ResolvingGrammarReaderController
argument_list|(
name|baseURI
argument_list|,
name|sources
argument_list|)
decl_stmt|;
name|xmlSchemaReader
operator|=
operator|new
name|RecursiveAllowedXMLSchemaReader
argument_list|(
name|ctrl
argument_list|,
name|parserFactory
argument_list|)
expr_stmt|;
name|multiSchemaReader
operator|=
operator|new
name|MultiSchemaReader
argument_list|(
name|xmlSchemaReader
argument_list|)
expr_stmt|;
for|for
control|(
name|EmbeddedSchema
name|source
range|:
name|sources
operator|.
name|values
argument_list|()
control|)
block|{
name|DOMSource
name|domSource
init|=
operator|new
name|DOMSource
argument_list|(
name|source
operator|.
name|getSchemaElement
argument_list|()
argument_list|)
decl_stmt|;
name|domSource
operator|.
name|setSystemId
argument_list|(
name|source
operator|.
name|getSystemId
argument_list|()
argument_list|)
expr_stmt|;
name|multiSchemaReader
operator|.
name|parse
argument_list|(
name|domSource
argument_list|)
expr_stmt|;
block|}
name|XMLSchemaGrammar
name|grammar
init|=
name|multiSchemaReader
operator|.
name|getResult
argument_list|()
decl_stmt|;
if|if
condition|(
name|grammar
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|XMLStreamException
argument_list|(
literal|"Failed to load schemas"
argument_list|)
throw|;
block|}
return|return
operator|new
name|W3CSchema
argument_list|(
name|grammar
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|XMLValidationSchema
name|loadSchema
parameter_list|(
name|InputSource
name|src
parameter_list|,
name|Object
name|sysRef
parameter_list|)
throws|throws
name|XMLStreamException
block|{
throw|throw
operator|new
name|XMLStreamException
argument_list|(
literal|"W3CMultiSchemaFactory does not support the provider API."
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

