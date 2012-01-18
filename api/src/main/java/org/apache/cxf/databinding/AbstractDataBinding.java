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
name|databinding
package|;
end_package

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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Resource
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
name|namespace
operator|.
name|QName
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
name|xmlschema
operator|.
name|SchemaCollection
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

begin_comment
comment|/**  * Supply default implementations, as appropriate, for DataBinding.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractDataBinding
implements|implements
name|DataBinding
block|{
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|BUILTIN_SCHEMA_LOCS
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
block|{
name|BUILTIN_SCHEMA_LOCS
operator|.
name|put
argument_list|(
literal|"http://www.w3.org/2005/08/addressing"
argument_list|,
literal|"http://www.w3.org/2006/03/addressing/ws-addr.xsd"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|boolean
name|mtomEnabled
decl_stmt|;
specifier|protected
name|int
name|mtomThreshold
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|DOMSource
argument_list|>
name|schemas
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaceMap
decl_stmt|;
specifier|private
name|boolean
name|hackAroundEmptyNamespaceIssue
decl_stmt|;
specifier|protected
name|Bus
name|getBus
parameter_list|()
block|{
if|if
condition|(
name|bus
operator|==
literal|null
condition|)
block|{
return|return
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
return|;
block|}
return|return
name|bus
return|;
block|}
comment|/**      * This call is used to set the bus. It should only be called once.      *      * @param bus      */
annotation|@
name|Resource
argument_list|(
name|name
operator|=
literal|"cxf"
argument_list|)
specifier|public
name|void
name|setBus
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
assert|assert
name|this
operator|.
name|bus
operator|==
literal|null
operator|||
name|this
operator|.
name|bus
operator|==
name|bus
assert|;
name|this
operator|.
name|bus
operator|=
name|bus
expr_stmt|;
block|}
specifier|public
name|Collection
argument_list|<
name|DOMSource
argument_list|>
name|getSchemas
parameter_list|()
block|{
return|return
name|schemas
return|;
block|}
specifier|public
name|void
name|setSchemas
parameter_list|(
name|Collection
argument_list|<
name|DOMSource
argument_list|>
name|schemas
parameter_list|)
block|{
name|this
operator|.
name|schemas
operator|=
name|schemas
expr_stmt|;
block|}
specifier|public
name|XmlSchema
name|addSchemaDocument
parameter_list|(
name|ServiceInfo
name|serviceInfo
parameter_list|,
name|SchemaCollection
name|col
parameter_list|,
name|Document
name|d
parameter_list|,
name|String
name|systemId
parameter_list|)
block|{
return|return
name|addSchemaDocument
argument_list|(
name|serviceInfo
argument_list|,
name|col
argument_list|,
name|d
argument_list|,
name|systemId
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
name|XmlSchema
name|addSchemaDocument
parameter_list|(
name|ServiceInfo
name|serviceInfo
parameter_list|,
name|SchemaCollection
name|col
parameter_list|,
name|Document
name|d
parameter_list|,
name|String
name|systemId
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|ids
parameter_list|)
block|{
comment|/*          * Sanity check. The document has to remotely resemble a schema.          */
if|if
condition|(
operator|!
name|XMLConstants
operator|.
name|W3C_XML_SCHEMA_NS_URI
operator|.
name|equals
argument_list|(
name|d
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
name|QName
name|qn
init|=
name|DOMUtils
operator|.
name|getElementQName
argument_list|(
name|d
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Invalid schema document passed to "
operator|+
literal|"AbstractDataBinding.addSchemaDocument, "
operator|+
literal|"not in W3C schema namespace: "
operator|+
name|qn
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
literal|"schema"
operator|.
name|equals
argument_list|(
name|d
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|getLocalName
argument_list|()
argument_list|)
condition|)
block|{
name|QName
name|qn
init|=
name|DOMUtils
operator|.
name|getElementQName
argument_list|(
name|d
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Invalid schema document passed to "
operator|+
literal|"AbstractDataBinding.addSchemaDocument, "
operator|+
literal|"document element isn't 'schema': "
operator|+
name|qn
argument_list|)
throw|;
block|}
name|String
name|ns
init|=
name|d
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|getAttribute
argument_list|(
literal|"targetNamespace"
argument_list|)
decl_stmt|;
name|boolean
name|copied
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|ns
argument_list|)
condition|)
block|{
if|if
condition|(
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|d
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
operator|==
literal|null
condition|)
block|{
name|hackAroundEmptyNamespaceIssue
operator|=
literal|true
expr_stmt|;
return|return
literal|null
return|;
block|}
comment|// create a copy of the dom so we
comment|// can modify it.
name|d
operator|=
name|copy
argument_list|(
name|d
argument_list|)
expr_stmt|;
name|copied
operator|=
literal|true
expr_stmt|;
name|ns
operator|=
name|serviceInfo
operator|.
name|getInterface
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
expr_stmt|;
name|d
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|setAttribute
argument_list|(
literal|"targetNamespace"
argument_list|,
name|ns
argument_list|)
expr_stmt|;
block|}
name|SchemaInfo
name|schemaInfo
init|=
name|serviceInfo
operator|.
name|getSchema
argument_list|(
name|ns
argument_list|)
decl_stmt|;
if|if
condition|(
name|schemaInfo
operator|!=
literal|null
operator|&&
operator|(
name|systemId
operator|==
literal|null
operator|&&
name|schemaInfo
operator|.
name|getSystemId
argument_list|()
operator|==
literal|null
operator|||
name|systemId
operator|!=
literal|null
operator|&&
name|systemId
operator|.
name|equalsIgnoreCase
argument_list|(
name|schemaInfo
operator|.
name|getSystemId
argument_list|()
argument_list|)
operator|)
condition|)
block|{
return|return
name|schemaInfo
operator|.
name|getSchema
argument_list|()
return|;
block|}
if|if
condition|(
name|hackAroundEmptyNamespaceIssue
condition|)
block|{
name|d
operator|=
name|doEmptyNamespaceHack
argument_list|(
name|d
argument_list|,
name|copied
argument_list|)
expr_stmt|;
block|}
name|Node
name|n
init|=
name|d
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
name|boolean
name|patchRequired
init|=
literal|false
decl_stmt|;
while|while
condition|(
name|n
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|n
operator|instanceof
name|Element
condition|)
block|{
name|Element
name|e
init|=
operator|(
name|Element
operator|)
name|n
decl_stmt|;
if|if
condition|(
name|e
operator|.
name|getLocalName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"import"
argument_list|)
condition|)
block|{
name|patchRequired
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
name|n
operator|=
name|n
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|patchRequired
condition|)
block|{
if|if
condition|(
operator|!
name|copied
condition|)
block|{
name|d
operator|=
name|copy
argument_list|(
name|d
argument_list|)
expr_stmt|;
block|}
name|n
operator|=
name|d
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|getFirstChild
argument_list|()
expr_stmt|;
while|while
condition|(
name|n
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|n
operator|instanceof
name|Element
condition|)
block|{
name|Element
name|e
init|=
operator|(
name|Element
operator|)
name|n
decl_stmt|;
if|if
condition|(
name|e
operator|.
name|getLocalName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"import"
argument_list|)
condition|)
block|{
name|e
operator|=
operator|(
name|Element
operator|)
name|n
expr_stmt|;
name|String
name|loc
init|=
name|e
operator|.
name|getAttribute
argument_list|(
literal|"schemaLocation"
argument_list|)
decl_stmt|;
if|if
condition|(
name|ids
operator|==
literal|null
operator|||
name|ids
operator|.
name|contains
argument_list|(
name|loc
argument_list|)
condition|)
block|{
name|e
operator|.
name|removeAttribute
argument_list|(
literal|"schemaLocation"
argument_list|)
expr_stmt|;
block|}
name|updateSchemaLocation
argument_list|(
name|e
argument_list|)
expr_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|e
operator|.
name|getAttribute
argument_list|(
literal|"namespace"
argument_list|)
argument_list|)
condition|)
block|{
name|e
operator|.
name|setAttribute
argument_list|(
literal|"namespace"
argument_list|,
name|serviceInfo
operator|.
name|getInterface
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|n
operator|=
name|n
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
block|}
name|SchemaInfo
name|schema
init|=
operator|new
name|SchemaInfo
argument_list|(
name|ns
argument_list|)
decl_stmt|;
name|schema
operator|.
name|setSystemId
argument_list|(
name|systemId
argument_list|)
expr_stmt|;
name|XmlSchema
name|xmlSchema
decl_stmt|;
synchronized|synchronized
init|(
name|d
init|)
block|{
name|xmlSchema
operator|=
name|col
operator|.
name|read
argument_list|(
name|d
argument_list|,
name|systemId
argument_list|)
expr_stmt|;
name|schema
operator|.
name|setSchema
argument_list|(
name|xmlSchema
argument_list|)
expr_stmt|;
name|schema
operator|.
name|setElement
argument_list|(
name|d
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|serviceInfo
operator|.
name|addSchema
argument_list|(
name|schema
argument_list|)
expr_stmt|;
return|return
name|xmlSchema
return|;
block|}
specifier|private
name|Document
name|doEmptyNamespaceHack
parameter_list|(
name|Document
name|d
parameter_list|,
name|boolean
name|alreadyWritable
parameter_list|)
block|{
name|boolean
name|hasStuffToRemove
init|=
literal|false
decl_stmt|;
name|Element
name|el
init|=
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|d
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
decl_stmt|;
while|while
condition|(
name|el
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
literal|"import"
operator|.
name|equals
argument_list|(
name|el
operator|.
name|getLocalName
argument_list|()
argument_list|)
operator|&&
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|el
operator|.
name|getAttribute
argument_list|(
literal|"targetNamespace"
argument_list|)
argument_list|)
condition|)
block|{
name|hasStuffToRemove
operator|=
literal|true
expr_stmt|;
break|break;
block|}
name|el
operator|=
name|DOMUtils
operator|.
name|getNextElement
argument_list|(
name|el
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|hasStuffToRemove
condition|)
block|{
comment|// create a copy of the dom so we
comment|// can modify it.
if|if
condition|(
operator|!
name|alreadyWritable
condition|)
block|{
name|d
operator|=
name|copy
argument_list|(
name|d
argument_list|)
expr_stmt|;
block|}
name|el
operator|=
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|d
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
while|while
condition|(
name|el
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
literal|"import"
operator|.
name|equals
argument_list|(
name|el
operator|.
name|getLocalName
argument_list|()
argument_list|)
operator|&&
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|el
operator|.
name|getAttribute
argument_list|(
literal|"targetNamespace"
argument_list|)
argument_list|)
condition|)
block|{
name|d
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|removeChild
argument_list|(
name|el
argument_list|)
expr_stmt|;
name|el
operator|=
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|d
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|el
operator|=
name|DOMUtils
operator|.
name|getNextElement
argument_list|(
name|el
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|d
return|;
block|}
specifier|private
name|Document
name|copy
parameter_list|(
name|Document
name|doc
parameter_list|)
block|{
try|try
block|{
return|return
name|StaxUtils
operator|.
name|copy
argument_list|(
name|doc
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
catch|catch
parameter_list|(
name|ParserConfigurationException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
return|return
name|doc
return|;
block|}
specifier|protected
name|void
name|updateSchemaLocation
parameter_list|(
name|Element
name|e
parameter_list|)
block|{
name|String
name|ns
init|=
name|e
operator|.
name|getAttribute
argument_list|(
literal|"namespace"
argument_list|)
decl_stmt|;
name|String
name|newLoc
init|=
name|BUILTIN_SCHEMA_LOCS
operator|.
name|get
argument_list|(
name|ns
argument_list|)
decl_stmt|;
if|if
condition|(
name|newLoc
operator|!=
literal|null
condition|)
block|{
name|e
operator|.
name|setAttribute
argument_list|(
literal|"schemaLocation"
argument_list|,
name|newLoc
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @return the namespaceMap (URI to prefix). This will be null      * if no particular namespace map has been set.      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getNamespaceMap
parameter_list|()
block|{
return|return
name|namespaceMap
return|;
block|}
comment|/**      * Set a map of from URI to prefix. If possible, the data binding will use these      * prefixes on the wire.      *      * @param namespaceMap The namespaceMap to set.      */
specifier|public
name|void
name|setNamespaceMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaceMap
parameter_list|)
block|{
name|checkNamespaceMap
argument_list|(
name|namespaceMap
argument_list|)
expr_stmt|;
name|this
operator|.
name|namespaceMap
operator|=
name|namespaceMap
expr_stmt|;
block|}
comment|/**      * Provide explicit mappings to ReflectionServiceFactory. {@inheritDoc}      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getDeclaredNamespaceMappings
parameter_list|()
block|{
return|return
name|this
operator|.
name|namespaceMap
return|;
block|}
specifier|protected
specifier|static
name|void
name|checkNamespaceMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaceMap
parameter_list|)
block|{
comment|// make some checks. This is a map from namespace to prefix, but we want unique prefixes.
if|if
condition|(
name|namespaceMap
operator|!=
literal|null
condition|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|prefixesSoFar
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|mapping
range|:
name|namespaceMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|prefixesSoFar
operator|.
name|contains
argument_list|(
name|mapping
operator|.
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Duplicate prefix "
operator|+
name|mapping
operator|.
name|getValue
argument_list|()
argument_list|)
throw|;
block|}
name|prefixesSoFar
operator|.
name|add
argument_list|(
name|mapping
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|setMtomEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
block|{
name|mtomEnabled
operator|=
name|enabled
expr_stmt|;
block|}
specifier|public
name|boolean
name|isMtomEnabled
parameter_list|()
block|{
return|return
name|mtomEnabled
return|;
block|}
specifier|public
name|int
name|getMtomThreshold
parameter_list|()
block|{
return|return
name|mtomThreshold
return|;
block|}
specifier|public
name|void
name|setMtomThreshold
parameter_list|(
name|int
name|threshold
parameter_list|)
block|{
name|mtomThreshold
operator|=
name|threshold
expr_stmt|;
block|}
block|}
end_class

end_unit

