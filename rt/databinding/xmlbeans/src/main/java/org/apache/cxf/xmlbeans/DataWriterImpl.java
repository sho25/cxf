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
name|xmlbeans
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Comment
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
name|DocumentFragment
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
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|Text
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
name|databinding
operator|.
name|DataWriter
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
name|message
operator|.
name|Attachment
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
name|message
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
name|message
operator|.
name|MessageUtils
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
name|MessagePartInfo
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
name|XmlSchemaElement
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmlbeans
operator|.
name|SchemaType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmlbeans
operator|.
name|XmlAnySimpleType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmlbeans
operator|.
name|XmlObject
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmlbeans
operator|.
name|XmlOptions
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmlbeans
operator|.
name|XmlTokenSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmlbeans
operator|.
name|impl
operator|.
name|values
operator|.
name|XmlObjectBase
import|;
end_import

begin_class
specifier|public
class|class
name|DataWriterImpl
implements|implements
name|DataWriter
argument_list|<
name|XMLStreamWriter
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getLogger
argument_list|(
name|XmlBeansDataBinding
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Schema
name|schema
decl_stmt|;
specifier|private
name|Message
name|message
decl_stmt|;
specifier|public
name|DataWriterImpl
parameter_list|()
block|{     }
specifier|public
name|void
name|write
parameter_list|(
name|Object
name|obj
parameter_list|,
name|XMLStreamWriter
name|output
parameter_list|)
block|{
name|write
argument_list|(
name|obj
argument_list|,
literal|null
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|write
parameter_list|(
name|Object
name|obj
parameter_list|,
name|MessagePartInfo
name|part
parameter_list|,
name|XMLStreamWriter
name|output
parameter_list|)
block|{
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|typeClass
init|=
name|part
operator|.
name|getTypeClass
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|XmlObject
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|typeClass
argument_list|)
condition|)
block|{
name|typeClass
operator|=
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|part
operator|.
name|getProperty
argument_list|(
name|XmlAnySimpleType
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|cls
index|[]
init|=
name|typeClass
operator|.
name|getDeclaredClasses
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
range|:
name|cls
control|)
block|{
if|if
condition|(
literal|"Factory"
operator|.
name|equals
argument_list|(
name|c
operator|.
name|getSimpleName
argument_list|()
argument_list|)
condition|)
block|{
try|try
block|{
name|SchemaType
name|st
init|=
operator|(
name|SchemaType
operator|)
name|part
operator|.
name|getProperty
argument_list|(
name|SchemaType
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|XmlOptions
name|options
init|=
operator|new
name|XmlOptions
argument_list|()
decl_stmt|;
if|if
condition|(
name|schema
operator|!=
literal|null
condition|)
block|{
name|options
operator|.
name|setValidateOnSet
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|st
operator|.
name|isDocumentType
argument_list|()
condition|)
block|{
name|options
operator|.
name|setLoadReplaceDocumentElement
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
name|Method
name|meth
init|=
name|c
operator|.
name|getMethod
argument_list|(
literal|"newValue"
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
name|obj
operator|=
name|meth
operator|.
name|invoke
argument_list|(
literal|null
argument_list|,
name|obj
argument_list|)
expr_stmt|;
break|break;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
literal|"UNMARSHAL_ERROR"
argument_list|,
name|LOG
argument_list|,
name|e
argument_list|,
name|part
operator|.
name|getTypeClass
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
block|}
if|if
condition|(
name|obj
operator|!=
literal|null
operator|||
operator|!
operator|(
name|part
operator|.
name|getXmlSchema
argument_list|()
operator|instanceof
name|XmlSchemaElement
operator|)
condition|)
block|{
name|XmlOptions
name|options
init|=
operator|new
name|XmlOptions
argument_list|()
decl_stmt|;
if|if
condition|(
name|schema
operator|!=
literal|null
condition|)
block|{
name|options
operator|.
name|setValidateOnSet
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|message
operator|!=
literal|null
operator|&&
name|MessageUtils
operator|.
name|getContextualBoolean
argument_list|(
name|message
argument_list|,
name|XmlBeansDataBinding
operator|.
name|XMLBEANS_NAMESPACE_HACK
argument_list|,
literal|false
argument_list|)
condition|)
block|{
name|Object
name|dom
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|XmlObjectBase
condition|)
block|{
name|XmlObjectBase
name|source
init|=
operator|(
name|XmlObjectBase
operator|)
name|obj
decl_stmt|;
name|dom
operator|=
name|source
operator|.
name|newDomNode
argument_list|(
name|options
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|XmlTokenSource
name|source
init|=
operator|(
name|XmlTokenSource
operator|)
name|obj
decl_stmt|;
name|dom
operator|=
name|source
operator|.
name|newDomNode
argument_list|(
name|options
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|dom
operator|instanceof
name|Document
condition|)
block|{
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
name|e
init|=
operator|(
operator|(
name|Document
operator|)
name|dom
operator|)
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
name|e
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|dom
operator|instanceof
name|DocumentFragment
condition|)
block|{
name|DocumentFragment
name|frag
init|=
operator|(
name|DocumentFragment
operator|)
name|dom
decl_stmt|;
name|Node
name|node
init|=
name|frag
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
while|while
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|node
operator|instanceof
name|Element
condition|)
block|{
name|StaxUtils
operator|.
name|copy
argument_list|(
operator|(
name|Element
operator|)
name|node
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|node
operator|instanceof
name|Comment
condition|)
block|{
name|output
operator|.
name|writeComment
argument_list|(
operator|(
operator|(
name|Comment
operator|)
name|node
operator|)
operator|.
name|getData
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|node
operator|instanceof
name|Text
condition|)
block|{
name|output
operator|.
name|writeCharacters
argument_list|(
operator|(
operator|(
name|Text
operator|)
name|node
operator|)
operator|.
name|getData
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|node
operator|=
name|node
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|Fault
argument_list|(
literal|"Invalid document type returned: "
operator|+
name|dom
operator|.
name|toString
argument_list|()
argument_list|,
name|LOG
argument_list|)
throw|;
block|}
block|}
name|XMLStreamReader
name|reader
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|XmlObjectBase
condition|)
block|{
name|XmlObjectBase
name|source
init|=
operator|(
name|XmlObjectBase
operator|)
name|obj
decl_stmt|;
name|reader
operator|=
name|source
operator|.
name|newCursorForce
argument_list|()
operator|.
name|newXMLStreamReader
argument_list|(
name|options
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|XmlTokenSource
name|source
init|=
operator|(
name|XmlTokenSource
operator|)
name|obj
decl_stmt|;
name|reader
operator|=
name|source
operator|.
name|newCursor
argument_list|()
operator|.
name|newXMLStreamReader
argument_list|(
name|options
argument_list|)
expr_stmt|;
block|}
name|SchemaType
name|st
init|=
operator|(
name|SchemaType
operator|)
name|part
operator|.
name|getProperty
argument_list|(
name|SchemaType
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|st
operator|!=
literal|null
operator|&&
operator|!
name|st
operator|.
name|isDocumentType
argument_list|()
condition|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|part
operator|.
name|getConcreteName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
name|output
operator|.
name|writeStartElement
argument_list|(
name|part
operator|.
name|getConcreteName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|pfx
init|=
name|output
operator|.
name|getPrefix
argument_list|(
name|part
operator|.
name|getConcreteName
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
name|output
operator|.
name|writeStartElement
argument_list|(
literal|"tns"
argument_list|,
name|part
operator|.
name|getConcreteName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|,
name|part
operator|.
name|getConcreteName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|output
operator|.
name|writeNamespace
argument_list|(
literal|"tns"
argument_list|,
name|part
operator|.
name|getConcreteName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|output
operator|.
name|writeStartElement
argument_list|(
name|pfx
argument_list|,
name|part
operator|.
name|getConcreteName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|,
name|part
operator|.
name|getConcreteName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|StaxUtils
operator|.
name|copy
argument_list|(
name|reader
argument_list|,
name|output
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|output
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|StaxUtils
operator|.
name|copy
argument_list|(
name|reader
argument_list|,
name|output
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|obj
operator|==
literal|null
operator|&&
name|needToRender
argument_list|(
name|obj
argument_list|,
name|part
argument_list|)
condition|)
block|{
name|output
operator|.
name|writeStartElement
argument_list|(
name|part
operator|.
name|getConcreteName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|part
operator|.
name|getConcreteName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|output
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
literal|"MARSHAL_ERROR"
argument_list|,
name|LOG
argument_list|,
name|e
argument_list|,
name|obj
argument_list|)
throw|;
block|}
block|}
specifier|private
name|boolean
name|needToRender
parameter_list|(
name|Object
name|obj
parameter_list|,
name|MessagePartInfo
name|part
parameter_list|)
block|{
if|if
condition|(
name|part
operator|!=
literal|null
operator|&&
name|part
operator|.
name|getXmlSchema
argument_list|()
operator|instanceof
name|XmlSchemaElement
condition|)
block|{
name|XmlSchemaElement
name|element
init|=
operator|(
name|XmlSchemaElement
operator|)
name|part
operator|.
name|getXmlSchema
argument_list|()
decl_stmt|;
return|return
name|element
operator|.
name|isNillable
argument_list|()
operator|&&
name|element
operator|.
name|getMinOccurs
argument_list|()
operator|>
literal|0
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|setAttachments
parameter_list|(
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|attachments
parameter_list|)
block|{     }
specifier|public
name|void
name|setProperty
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|Message
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|message
operator|=
operator|(
name|Message
operator|)
name|value
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setSchema
parameter_list|(
name|Schema
name|schema
parameter_list|)
block|{
name|this
operator|.
name|schema
operator|=
name|schema
expr_stmt|;
block|}
block|}
end_class

end_unit

