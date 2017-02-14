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
name|corba
operator|.
name|common
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|stream
operator|.
name|XMLStreamException
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
name|helpers
operator|.
name|CastUtils
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
name|DOMUtils
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

begin_comment
comment|/*   * This class is extending the wsdl4j RI class to print out the   * extensibility elements at the top of a generated wsdl file.   *   */
end_comment

begin_class
specifier|public
class|class
name|WSDLCorbaWriterImpl
implements|implements
name|WSDLWriter
block|{
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_INDENT_LEVEL
init|=
literal|2
decl_stmt|;
specifier|final
name|WSDLWriter
name|wrapped
decl_stmt|;
specifier|public
name|WSDLCorbaWriterImpl
parameter_list|(
name|WSDLWriter
name|orig
parameter_list|)
block|{
name|wrapped
operator|=
name|orig
expr_stmt|;
block|}
specifier|public
name|void
name|setFeature
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|value
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|wrapped
operator|.
name|setFeature
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|getFeature
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
return|return
name|wrapped
operator|.
name|getFeature
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|Document
name|getDocument
parameter_list|(
name|Definition
name|wsdlDef
parameter_list|)
throws|throws
name|WSDLException
block|{
try|try
block|{
name|fixTypes
argument_list|(
name|wsdlDef
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|WSDLException
argument_list|(
name|WSDLException
operator|.
name|PARSER_ERROR
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex
argument_list|)
throw|;
block|}
name|Document
name|doc
init|=
name|wrapped
operator|.
name|getDocument
argument_list|(
name|wsdlDef
argument_list|)
decl_stmt|;
name|Element
name|imp
init|=
literal|null
decl_stmt|;
name|Element
name|child
init|=
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|doc
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
decl_stmt|;
comment|//move extensability things to the top
while|while
condition|(
name|child
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|child
operator|.
name|getNamespaceURI
argument_list|()
operator|.
name|equals
argument_list|(
name|doc
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
comment|//wsdl node
if|if
condition|(
name|imp
operator|==
literal|null
condition|)
block|{
name|imp
operator|=
name|child
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|imp
operator|!=
literal|null
condition|)
block|{
name|doc
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|removeChild
argument_list|(
name|child
argument_list|)
expr_stmt|;
name|doc
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|insertBefore
argument_list|(
name|child
argument_list|,
name|imp
argument_list|)
expr_stmt|;
block|}
name|child
operator|=
name|DOMUtils
operator|.
name|getNextElement
argument_list|(
name|child
argument_list|)
expr_stmt|;
block|}
return|return
name|doc
return|;
block|}
specifier|private
name|void
name|fixTypes
parameter_list|(
name|Definition
name|wsdlDef
parameter_list|)
throws|throws
name|ParserConfigurationException
block|{
name|Types
name|t
init|=
name|wsdlDef
operator|.
name|getTypes
argument_list|()
decl_stmt|;
if|if
condition|(
name|t
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|List
argument_list|<
name|ExtensibilityElement
argument_list|>
name|l
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|t
operator|.
name|getExtensibilityElements
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|l
operator|==
literal|null
condition|)
block|{
return|return;
block|}
for|for
control|(
name|ExtensibilityElement
name|e
range|:
name|l
control|)
block|{
if|if
condition|(
name|e
operator|instanceof
name|Schema
condition|)
block|{
name|Schema
name|sc
init|=
operator|(
name|Schema
operator|)
name|e
decl_stmt|;
name|String
name|pfx
init|=
name|wsdlDef
operator|.
name|getPrefix
argument_list|(
name|sc
operator|.
name|getElementType
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|pfx
argument_list|)
condition|)
block|{
name|pfx
operator|=
literal|"xsd"
expr_stmt|;
name|String
name|ns
init|=
name|wsdlDef
operator|.
name|getNamespace
argument_list|(
name|pfx
argument_list|)
decl_stmt|;
name|int
name|count
init|=
literal|1
decl_stmt|;
while|while
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|ns
argument_list|)
condition|)
block|{
name|pfx
operator|=
literal|"xsd"
operator|+
name|count
operator|++
expr_stmt|;
name|ns
operator|=
name|wsdlDef
operator|.
name|getNamespace
argument_list|(
name|pfx
argument_list|)
expr_stmt|;
block|}
name|wsdlDef
operator|.
name|addNamespace
argument_list|(
name|pfx
argument_list|,
name|sc
operator|.
name|getElementType
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sc
operator|.
name|getElement
argument_list|()
operator|==
literal|null
condition|)
block|{
name|fixSchema
argument_list|(
name|sc
argument_list|,
name|pfx
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|fixSchema
parameter_list|(
name|Schema
name|sc
parameter_list|,
name|String
name|pfx
parameter_list|)
throws|throws
name|ParserConfigurationException
block|{
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|newDocument
argument_list|()
decl_stmt|;
name|Element
name|el
init|=
name|doc
operator|.
name|createElementNS
argument_list|(
name|sc
operator|.
name|getElementType
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|pfx
operator|+
literal|":"
operator|+
name|sc
operator|.
name|getElementType
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
decl_stmt|;
name|sc
operator|.
name|setElement
argument_list|(
name|el
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|mp
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|sc
operator|.
name|getImports
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|ent
range|:
name|mp
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Element
name|imp
init|=
name|doc
operator|.
name|createElementNS
argument_list|(
name|sc
operator|.
name|getElementType
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|pfx
operator|+
literal|":import"
argument_list|)
decl_stmt|;
name|el
operator|.
name|appendChild
argument_list|(
name|imp
argument_list|)
expr_stmt|;
name|imp
operator|.
name|setAttribute
argument_list|(
literal|"namespace"
argument_list|,
name|ent
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|writeWSDL
parameter_list|(
name|Definition
name|wsdlDef
parameter_list|,
name|Writer
name|sink
parameter_list|)
throws|throws
name|WSDLException
block|{
try|try
block|{
name|StaxUtils
operator|.
name|writeTo
argument_list|(
name|getDocument
argument_list|(
name|wsdlDef
argument_list|)
argument_list|,
name|sink
argument_list|,
literal|2
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|writeWSDL
parameter_list|(
name|Definition
name|wsdlDef
parameter_list|,
name|OutputStream
name|sink
parameter_list|)
throws|throws
name|WSDLException
block|{
try|try
block|{
name|StaxUtils
operator|.
name|writeTo
argument_list|(
name|getDocument
argument_list|(
name|wsdlDef
argument_list|)
argument_list|,
name|sink
argument_list|,
literal|2
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

