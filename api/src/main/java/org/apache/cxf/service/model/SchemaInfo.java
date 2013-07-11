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
name|service
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|ref
operator|.
name|SoftReference
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
name|Attr
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
name|XmlSchemaElement
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
name|XmlSchemaForm
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
name|XmlSchemaSerializer
operator|.
name|XmlSchemaSerializerException
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
name|constants
operator|.
name|Constants
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
name|utils
operator|.
name|NamespaceMap
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|SchemaInfo
extends|extends
name|AbstractPropertiesHolder
block|{
specifier|private
name|String
name|namespaceUri
decl_stmt|;
specifier|private
name|boolean
name|isElementQualified
decl_stmt|;
specifier|private
name|boolean
name|isAttributeQualified
decl_stmt|;
specifier|private
name|XmlSchema
name|schema
decl_stmt|;
specifier|private
name|String
name|systemId
decl_stmt|;
comment|// Avoid re-serializing all the time. Particularly as a cached WSDL will
comment|// hold a reference to the element.
specifier|private
name|SoftReference
argument_list|<
name|Element
argument_list|>
name|cachedElement
decl_stmt|;
specifier|public
name|SchemaInfo
parameter_list|(
name|String
name|namespaceUri
parameter_list|)
block|{
name|this
argument_list|(
name|namespaceUri
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|SchemaInfo
parameter_list|(
name|String
name|namespaceUri
parameter_list|,
name|boolean
name|qElement
parameter_list|,
name|boolean
name|qAttribute
parameter_list|)
block|{
name|this
operator|.
name|namespaceUri
operator|=
name|namespaceUri
expr_stmt|;
name|this
operator|.
name|isElementQualified
operator|=
name|qElement
expr_stmt|;
name|this
operator|.
name|isAttributeQualified
operator|=
name|qAttribute
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|" [namespaceURI: "
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|namespaceUri
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"] [systemId: "
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|systemId
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|String
name|getNamespaceURI
parameter_list|()
block|{
return|return
name|namespaceUri
return|;
block|}
specifier|public
name|void
name|setNamespaceURI
parameter_list|(
name|String
name|nsUri
parameter_list|)
block|{
name|this
operator|.
name|namespaceUri
operator|=
name|nsUri
expr_stmt|;
block|}
specifier|public
specifier|synchronized
name|void
name|setElement
parameter_list|(
name|Element
name|el
parameter_list|)
block|{
name|cachedElement
operator|=
operator|new
name|SoftReference
argument_list|<
name|Element
argument_list|>
argument_list|(
name|el
argument_list|)
expr_stmt|;
block|}
comment|/**      * Build and return a DOM tree for this schema.      * @return a DOM Element representation of the schema      */
specifier|public
specifier|synchronized
name|Element
name|getElement
parameter_list|()
block|{
comment|// if someone recently used this DOM tree, take advantage.
name|Element
name|element
init|=
name|cachedElement
operator|==
literal|null
condition|?
literal|null
else|:
name|cachedElement
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|element
operator|!=
literal|null
condition|)
block|{
return|return
name|element
return|;
block|}
if|if
condition|(
name|getSchema
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"No XmlSchema in SchemaInfo"
argument_list|)
throw|;
block|}
name|XmlSchema
name|sch
init|=
name|getSchema
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|sch
init|)
block|{
name|XmlSchema
name|schAgain
init|=
name|getSchema
argument_list|()
decl_stmt|;
comment|// XML Schema blows up when the context is null as opposed to empty.
comment|// Some unit tests really want to see 'tns:'.
if|if
condition|(
name|schAgain
operator|.
name|getNamespaceContext
argument_list|()
operator|==
literal|null
condition|)
block|{
name|NamespaceMap
name|nsMap
init|=
operator|new
name|NamespaceMap
argument_list|()
decl_stmt|;
name|nsMap
operator|.
name|add
argument_list|(
literal|"xsd"
argument_list|,
name|Constants
operator|.
name|URI_2001_SCHEMA_XSD
argument_list|)
expr_stmt|;
name|nsMap
operator|.
name|add
argument_list|(
literal|"tns"
argument_list|,
name|schAgain
operator|.
name|getTargetNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|schAgain
operator|.
name|setNamespaceContext
argument_list|(
name|nsMap
argument_list|)
expr_stmt|;
block|}
name|Document
name|serializedSchema
decl_stmt|;
try|try
block|{
name|serializedSchema
operator|=
name|schAgain
operator|.
name|getSchemaDocument
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XmlSchemaSerializerException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error serializing Xml Schema"
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|element
operator|=
name|serializedSchema
operator|.
name|getDocumentElement
argument_list|()
expr_stmt|;
name|cachedElement
operator|=
operator|new
name|SoftReference
argument_list|<
name|Element
argument_list|>
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
comment|// XXX A problem can occur with the ibm jdk when the XmlSchema
comment|// object is serialized. The xmlns declaration gets incorrectly
comment|// set to the same value as the targetNamespace attribute.
comment|// The aegis databinding tests demonstrate this particularly.
if|if
condition|(
name|element
operator|.
name|getPrefix
argument_list|()
operator|==
literal|null
operator|&&
operator|!
name|Constants
operator|.
name|URI_2001_SCHEMA_XSD
operator|.
name|equals
argument_list|(
name|element
operator|.
name|getAttributeNS
argument_list|(
name|Constants
operator|.
name|XMLNS_ATTRIBUTE_NS_URI
argument_list|,
name|Constants
operator|.
name|XMLNS_ATTRIBUTE
argument_list|)
argument_list|)
condition|)
block|{
name|Attr
name|attr
init|=
name|element
operator|.
name|getOwnerDocument
argument_list|()
operator|.
name|createAttributeNS
argument_list|(
name|Constants
operator|.
name|XMLNS_ATTRIBUTE_NS_URI
argument_list|,
name|Constants
operator|.
name|XMLNS_ATTRIBUTE
argument_list|)
decl_stmt|;
name|attr
operator|.
name|setValue
argument_list|(
name|Constants
operator|.
name|URI_2001_SCHEMA_XSD
argument_list|)
expr_stmt|;
name|element
operator|.
name|setAttributeNodeNS
argument_list|(
name|attr
argument_list|)
expr_stmt|;
block|}
return|return
name|element
return|;
block|}
specifier|public
name|boolean
name|isElementFormQualified
parameter_list|()
block|{
return|return
name|isElementQualified
return|;
block|}
specifier|public
name|boolean
name|isAttributeFormQualified
parameter_list|()
block|{
return|return
name|isAttributeQualified
return|;
block|}
specifier|public
name|XmlSchema
name|getSchema
parameter_list|()
block|{
return|return
name|schema
return|;
block|}
specifier|public
name|void
name|setSchema
parameter_list|(
name|XmlSchema
name|schema
parameter_list|)
block|{
name|this
operator|.
name|schema
operator|=
name|schema
expr_stmt|;
name|isElementQualified
operator|=
name|schema
operator|.
name|getElementFormDefault
argument_list|()
operator|.
name|equals
argument_list|(
name|XmlSchemaForm
operator|.
name|QUALIFIED
argument_list|)
expr_stmt|;
name|isAttributeQualified
operator|=
name|schema
operator|.
name|getAttributeFormDefault
argument_list|()
operator|.
name|equals
argument_list|(
name|XmlSchemaForm
operator|.
name|QUALIFIED
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getSystemId
parameter_list|()
block|{
return|return
name|systemId
return|;
block|}
specifier|public
name|void
name|setSystemId
parameter_list|(
name|String
name|systemId
parameter_list|)
block|{
name|this
operator|.
name|systemId
operator|=
name|systemId
expr_stmt|;
block|}
specifier|public
name|XmlSchemaElement
name|getElementByQName
parameter_list|(
name|QName
name|qname
parameter_list|)
block|{
name|String
name|uri
init|=
name|qname
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
if|if
condition|(
name|schema
operator|!=
literal|null
operator|&&
name|schema
operator|.
name|getTargetNamespace
argument_list|()
operator|!=
literal|null
operator|&&
name|schema
operator|.
name|getTargetNamespace
argument_list|()
operator|.
name|equals
argument_list|(
name|uri
argument_list|)
condition|)
block|{
return|return
name|schema
operator|.
name|getElementByName
argument_list|(
name|qname
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
name|String
name|getNamespaceUri
parameter_list|()
block|{
return|return
name|namespaceUri
return|;
block|}
name|boolean
name|isElementQualified
parameter_list|()
block|{
return|return
name|isElementQualified
return|;
block|}
name|boolean
name|isAttributeQualified
parameter_list|()
block|{
return|return
name|isAttributeQualified
return|;
block|}
block|}
end_class

end_unit

