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
name|wsdl11
package|;
end_package

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
name|IdentityHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|Import
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
name|extensions
operator|.
name|UnknownExtensibilityElement
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
name|extensions
operator|.
name|schema
operator|.
name|SchemaImport
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
name|catalog
operator|.
name|CatalogXmlSchemaURIResolver
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
import|import static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|helpers
operator|.
name|CastUtils
operator|.
name|cast
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|SchemaUtil
block|{
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Element
argument_list|>
name|schemaList
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|catalogResolved
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Bus
name|bus
decl_stmt|;
specifier|public
name|SchemaUtil
parameter_list|(
specifier|final
name|Bus
name|b
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Element
argument_list|>
name|s
parameter_list|)
block|{
name|this
operator|.
name|bus
operator|=
name|b
expr_stmt|;
name|this
operator|.
name|schemaList
operator|=
name|s
expr_stmt|;
block|}
specifier|public
name|void
name|getSchemas
parameter_list|(
specifier|final
name|Definition
name|def
parameter_list|,
specifier|final
name|ServiceInfo
name|serviceInfo
parameter_list|)
block|{
name|SchemaCollection
name|schemaCol
init|=
name|serviceInfo
operator|.
name|getXmlSchemaCollection
argument_list|()
decl_stmt|;
name|getSchemas
argument_list|(
name|def
argument_list|,
name|schemaCol
argument_list|,
name|serviceInfo
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|getSchemas
parameter_list|(
specifier|final
name|Definition
name|def
parameter_list|,
name|SchemaCollection
name|schemaCol
parameter_list|,
name|ServiceInfo
name|serviceInfo
parameter_list|)
block|{
name|getSchemas
argument_list|(
name|def
argument_list|,
name|schemaCol
argument_list|,
name|serviceInfo
operator|.
name|getSchemas
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|getSchemas
parameter_list|(
specifier|final
name|Definition
name|def
parameter_list|,
specifier|final
name|SchemaCollection
name|schemaCol
parameter_list|,
name|List
argument_list|<
name|SchemaInfo
argument_list|>
name|schemas
parameter_list|)
block|{
name|List
argument_list|<
name|Definition
argument_list|>
name|defList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|parseImports
argument_list|(
name|def
argument_list|,
name|defList
argument_list|)
expr_stmt|;
name|extractSchema
argument_list|(
name|def
argument_list|,
name|schemaCol
argument_list|,
name|schemas
argument_list|)
expr_stmt|;
comment|// added
name|getSchemaList
argument_list|(
name|def
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|Definition
argument_list|,
name|Definition
argument_list|>
name|done
init|=
operator|new
name|IdentityHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|done
operator|.
name|put
argument_list|(
name|def
argument_list|,
name|def
argument_list|)
expr_stmt|;
for|for
control|(
name|Definition
name|def2
range|:
name|defList
control|)
block|{
if|if
condition|(
operator|!
name|done
operator|.
name|containsKey
argument_list|(
name|def2
argument_list|)
condition|)
block|{
name|extractSchema
argument_list|(
name|def2
argument_list|,
name|schemaCol
argument_list|,
name|schemas
argument_list|)
expr_stmt|;
comment|// added
name|getSchemaList
argument_list|(
name|def2
argument_list|)
expr_stmt|;
name|done
operator|.
name|put
argument_list|(
name|def2
argument_list|,
name|def2
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|extractSchema
parameter_list|(
name|Definition
name|def
parameter_list|,
name|SchemaCollection
name|schemaCol
parameter_list|,
name|List
argument_list|<
name|SchemaInfo
argument_list|>
name|schemaInfos
parameter_list|)
block|{
name|Types
name|typesElement
init|=
name|def
operator|.
name|getTypes
argument_list|()
decl_stmt|;
if|if
condition|(
name|typesElement
operator|!=
literal|null
condition|)
block|{
name|int
name|schemaCount
init|=
literal|1
decl_stmt|;
for|for
control|(
name|Object
name|obj
range|:
name|typesElement
operator|.
name|getExtensibilityElements
argument_list|()
control|)
block|{
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
name|schemaElem
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|Schema
condition|)
block|{
name|Schema
name|schema
init|=
operator|(
name|Schema
operator|)
name|obj
decl_stmt|;
name|schemaElem
operator|=
name|schema
operator|.
name|getElement
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|UnknownExtensibilityElement
condition|)
block|{
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
name|elem
init|=
operator|(
operator|(
name|UnknownExtensibilityElement
operator|)
name|obj
operator|)
operator|.
name|getElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|elem
operator|.
name|getLocalName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"schema"
argument_list|)
condition|)
block|{
name|schemaElem
operator|=
name|elem
expr_stmt|;
block|}
block|}
if|if
condition|(
name|schemaElem
operator|!=
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|schemaElem
operator|.
name|getOwnerDocument
argument_list|()
init|)
block|{
for|for
control|(
name|Object
name|prefix
range|:
name|def
operator|.
name|getNamespaces
argument_list|()
operator|.
name|keySet
argument_list|()
control|)
block|{
name|String
name|ns
init|=
operator|(
name|String
operator|)
name|def
operator|.
name|getNamespaces
argument_list|()
operator|.
name|get
argument_list|(
name|prefix
argument_list|)
decl_stmt|;
if|if
condition|(
literal|""
operator|.
name|equals
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|schemaElem
operator|.
name|hasAttribute
argument_list|(
literal|"xmlns"
argument_list|)
condition|)
block|{
name|Attr
name|attr
init|=
name|schemaElem
operator|.
name|getOwnerDocument
argument_list|()
operator|.
name|createAttributeNS
argument_list|(
name|javax
operator|.
name|xml
operator|.
name|XMLConstants
operator|.
name|XMLNS_ATTRIBUTE_NS_URI
argument_list|,
literal|"xmlns"
argument_list|)
decl_stmt|;
name|attr
operator|.
name|setValue
argument_list|(
name|ns
argument_list|)
expr_stmt|;
name|schemaElem
operator|.
name|setAttributeNodeNS
argument_list|(
name|attr
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
operator|!
name|schemaElem
operator|.
name|hasAttribute
argument_list|(
literal|"xmlns:"
operator|+
name|prefix
argument_list|)
condition|)
block|{
name|String
name|namespace
init|=
name|javax
operator|.
name|xml
operator|.
name|XMLConstants
operator|.
name|XMLNS_ATTRIBUTE_NS_URI
decl_stmt|;
name|Attr
name|attr
init|=
name|schemaElem
operator|.
name|getOwnerDocument
argument_list|()
operator|.
name|createAttributeNS
argument_list|(
name|namespace
argument_list|,
literal|"xmlns:"
operator|+
name|prefix
argument_list|)
decl_stmt|;
name|attr
operator|.
name|setValue
argument_list|(
name|ns
argument_list|)
expr_stmt|;
name|schemaElem
operator|.
name|setAttributeNodeNS
argument_list|(
name|attr
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|systemId
init|=
name|def
operator|.
name|getDocumentBaseURI
argument_list|()
operator|+
literal|"#types"
operator|+
name|schemaCount
decl_stmt|;
if|if
condition|(
name|def
operator|.
name|getDocumentBaseURI
argument_list|()
operator|!=
literal|null
operator|&&
name|def
operator|.
name|getDocumentBaseURI
argument_list|()
operator|.
name|toUpperCase
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".XSD"
argument_list|)
operator|&&
name|def
operator|.
name|getTargetNamespace
argument_list|()
operator|==
literal|null
operator|&&
name|obj
operator|instanceof
name|Schema
operator|&&
operator|(
operator|(
name|Schema
operator|)
name|obj
operator|)
operator|.
name|getDocumentBaseURI
argument_list|()
operator|.
name|equals
argument_list|(
name|def
operator|.
name|getDocumentBaseURI
argument_list|()
argument_list|)
condition|)
block|{
name|systemId
operator|=
name|def
operator|.
name|getDocumentBaseURI
argument_list|()
expr_stmt|;
block|}
name|schemaCol
operator|.
name|setBaseUri
argument_list|(
name|def
operator|.
name|getDocumentBaseURI
argument_list|()
argument_list|)
expr_stmt|;
name|CatalogXmlSchemaURIResolver
name|schemaResolver
init|=
operator|new
name|CatalogXmlSchemaURIResolver
argument_list|(
name|bus
argument_list|)
decl_stmt|;
name|schemaCol
operator|.
name|setSchemaResolver
argument_list|(
name|schemaResolver
argument_list|)
expr_stmt|;
name|XmlSchema
name|xmlSchema
init|=
name|schemaCol
operator|.
name|read
argument_list|(
name|schemaElem
argument_list|,
name|systemId
argument_list|)
decl_stmt|;
name|catalogResolved
operator|.
name|putAll
argument_list|(
name|schemaResolver
operator|.
name|getResolvedMap
argument_list|()
argument_list|)
expr_stmt|;
name|SchemaInfo
name|schemaInfo
init|=
operator|new
name|SchemaInfo
argument_list|(
name|xmlSchema
operator|.
name|getTargetNamespace
argument_list|()
argument_list|)
decl_stmt|;
name|schemaInfo
operator|.
name|setSchema
argument_list|(
name|xmlSchema
argument_list|)
expr_stmt|;
name|schemaInfo
operator|.
name|setSystemId
argument_list|(
name|systemId
argument_list|)
expr_stmt|;
name|schemaInfo
operator|.
name|setElement
argument_list|(
name|schemaElem
argument_list|)
expr_stmt|;
name|schemaInfos
operator|.
name|add
argument_list|(
name|schemaInfo
argument_list|)
expr_stmt|;
name|schemaCount
operator|++
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
specifier|private
name|void
name|parseImports
parameter_list|(
name|Definition
name|def
parameter_list|,
name|List
argument_list|<
name|Definition
argument_list|>
name|defList
parameter_list|)
block|{
name|List
argument_list|<
name|Import
argument_list|>
name|importList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|List
argument_list|<
name|Import
argument_list|>
argument_list|>
name|ilist
init|=
name|cast
argument_list|(
name|def
operator|.
name|getImports
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|List
argument_list|<
name|Import
argument_list|>
name|list
range|:
name|ilist
control|)
block|{
name|importList
operator|.
name|addAll
argument_list|(
name|list
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Import
name|impt
range|:
name|importList
control|)
block|{
if|if
condition|(
operator|!
name|defList
operator|.
name|contains
argument_list|(
name|impt
operator|.
name|getDefinition
argument_list|()
argument_list|)
condition|)
block|{
name|defList
operator|.
name|add
argument_list|(
name|impt
operator|.
name|getDefinition
argument_list|()
argument_list|)
expr_stmt|;
name|parseImports
argument_list|(
name|impt
operator|.
name|getDefinition
argument_list|()
argument_list|,
name|defList
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// Workaround for getting the elements
specifier|private
name|void
name|getSchemaList
parameter_list|(
name|Definition
name|def
parameter_list|)
block|{
name|Types
name|typesElement
init|=
name|def
operator|.
name|getTypes
argument_list|()
decl_stmt|;
if|if
condition|(
name|typesElement
operator|!=
literal|null
condition|)
block|{
name|Iterator
argument_list|<
name|?
argument_list|>
name|ite
init|=
name|typesElement
operator|.
name|getExtensibilityElements
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|ite
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|obj
init|=
name|ite
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|Schema
condition|)
block|{
name|Schema
name|schema
init|=
operator|(
name|Schema
operator|)
name|obj
decl_stmt|;
name|addSchema
argument_list|(
name|schema
operator|.
name|getDocumentBaseURI
argument_list|()
argument_list|,
name|schema
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|addSchema
parameter_list|(
name|String
name|docBaseURI
parameter_list|,
name|Schema
name|schema
parameter_list|)
block|{
comment|//String docBaseURI = schema.getDocumentBaseURI();
name|Element
name|schemaEle
init|=
name|schema
operator|.
name|getElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|schemaList
operator|.
name|get
argument_list|(
name|docBaseURI
argument_list|)
operator|==
literal|null
condition|)
block|{
name|schemaList
operator|.
name|put
argument_list|(
name|docBaseURI
argument_list|,
name|schemaEle
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|schemaList
operator|.
name|get
argument_list|(
name|docBaseURI
argument_list|)
operator|!=
literal|null
operator|&&
name|schemaList
operator|.
name|containsValue
argument_list|(
name|schemaEle
argument_list|)
condition|)
block|{
comment|// do nothing
block|}
else|else
block|{
name|String
name|tns
init|=
name|schema
operator|.
name|getDocumentBaseURI
argument_list|()
operator|+
literal|"#"
operator|+
name|schema
operator|.
name|getElement
argument_list|()
operator|.
name|getAttribute
argument_list|(
literal|"targetNamespace"
argument_list|)
decl_stmt|;
if|if
condition|(
name|schemaList
operator|.
name|get
argument_list|(
name|tns
argument_list|)
operator|==
literal|null
condition|)
block|{
name|schemaList
operator|.
name|put
argument_list|(
name|tns
argument_list|,
name|schema
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|?
argument_list|>
argument_list|>
name|imports
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|schema
operator|.
name|getImports
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|imports
operator|!=
literal|null
operator|&&
operator|!
name|imports
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
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
name|?
argument_list|>
argument_list|>
name|entry
range|:
name|imports
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|importNamespace
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|SchemaImport
argument_list|>
name|schemaImports
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|SchemaImport
name|schemaImport
range|:
name|schemaImports
control|)
block|{
name|Schema
name|tempImport
init|=
name|schemaImport
operator|.
name|getReferencedSchema
argument_list|()
decl_stmt|;
name|String
name|key
init|=
name|schemaImport
operator|.
name|getSchemaLocationURI
argument_list|()
decl_stmt|;
if|if
condition|(
name|importNamespace
operator|==
literal|null
operator|&&
name|tempImport
operator|!=
literal|null
condition|)
block|{
name|importNamespace
operator|=
name|tempImport
operator|.
name|getDocumentBaseURI
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|tempImport
operator|!=
literal|null
operator|&&
operator|!
name|catalogResolved
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|key
operator|=
name|tempImport
operator|.
name|getDocumentBaseURI
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|tempImport
operator|!=
literal|null
operator|&&
operator|!
name|isSchemaParsed
argument_list|(
name|key
argument_list|,
name|importNamespace
argument_list|)
operator|&&
operator|!
name|schemaList
operator|.
name|containsValue
argument_list|(
name|tempImport
operator|.
name|getElement
argument_list|()
argument_list|)
condition|)
block|{
name|addSchema
argument_list|(
name|key
argument_list|,
name|tempImport
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
specifier|private
name|boolean
name|isSchemaParsed
parameter_list|(
name|String
name|baseUri
parameter_list|,
name|String
name|ns
parameter_list|)
block|{
if|if
condition|(
name|schemaList
operator|.
name|get
argument_list|(
name|baseUri
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|Element
name|ele
init|=
name|schemaList
operator|.
name|get
argument_list|(
name|baseUri
argument_list|)
decl_stmt|;
name|String
name|tns
init|=
name|ele
operator|.
name|getAttribute
argument_list|(
literal|"targetNamespace"
argument_list|)
decl_stmt|;
if|if
condition|(
name|ns
operator|.
name|equals
argument_list|(
name|tns
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
block|}
end_class

end_unit

