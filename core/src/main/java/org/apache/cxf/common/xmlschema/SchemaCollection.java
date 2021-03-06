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
name|common
operator|.
name|xmlschema
package|;
end_package

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
name|Set
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
name|XmlSchemaAll
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
name|XmlSchemaAttribute
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
name|XmlSchemaAttributeGroupRef
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
name|XmlSchemaAttributeOrGroupRef
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
name|XmlSchemaChoice
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
name|XmlSchemaCollection
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
name|XmlSchemaComplexContentExtension
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
name|XmlSchemaComplexContentRestriction
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
name|XmlSchemaComplexType
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
name|XmlSchemaContent
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
name|XmlSchemaContentModel
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
name|XmlSchemaParticle
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
name|XmlSchemaSequence
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
name|XmlSchemaSequenceMember
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
name|XmlSchemaSimpleContentExtension
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
name|XmlSchemaSimpleContentRestriction
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
name|XmlSchemaType
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
name|extensions
operator|.
name|ExtensionRegistry
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
name|resolver
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
name|NamespacePrefixList
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
name|XmlSchemaObjectBase
import|;
end_import

begin_comment
comment|/**  * Wrapper class for XmlSchemaCollection that deals with various quirks and bugs.  */
end_comment

begin_class
specifier|public
class|class
name|SchemaCollection
block|{
specifier|private
name|XmlSchemaCollection
name|schemaCollection
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|XmlSchema
argument_list|,
name|Set
argument_list|<
name|XmlSchemaType
argument_list|>
argument_list|>
name|xmlTypesCheckedForCrossImportsPerSchema
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|SchemaCollection
parameter_list|()
block|{
name|this
argument_list|(
operator|new
name|XmlSchemaCollection
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|SchemaCollection
parameter_list|(
name|XmlSchemaCollection
name|col
parameter_list|)
block|{
name|schemaCollection
operator|=
name|col
expr_stmt|;
if|if
condition|(
name|schemaCollection
operator|.
name|getNamespaceContext
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// an empty prefix map avoids extra checks for null.
name|schemaCollection
operator|.
name|setNamespaceContext
argument_list|(
operator|new
name|NamespaceMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|XmlSchemaCollection
name|getXmlSchemaCollection
parameter_list|()
block|{
return|return
name|schemaCollection
return|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|SchemaCollection
condition|)
block|{
return|return
name|schemaCollection
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|SchemaCollection
operator|)
name|obj
operator|)
operator|.
name|schemaCollection
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|XmlSchemaCollection
condition|)
block|{
return|return
name|schemaCollection
operator|.
name|equals
argument_list|(
name|obj
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|XmlSchemaElement
name|getElementByQName
parameter_list|(
name|QName
name|qname
parameter_list|)
block|{
return|return
name|schemaCollection
operator|.
name|getElementByQName
argument_list|(
name|qname
argument_list|)
return|;
block|}
specifier|public
name|XmlSchemaAttribute
name|getAttributeByQName
parameter_list|(
name|QName
name|qname
parameter_list|)
block|{
return|return
name|schemaCollection
operator|.
name|getAttributeByQName
argument_list|(
name|qname
argument_list|)
return|;
block|}
specifier|public
name|ExtensionRegistry
name|getExtReg
parameter_list|()
block|{
return|return
name|schemaCollection
operator|.
name|getExtReg
argument_list|()
return|;
block|}
specifier|public
name|NamespacePrefixList
name|getNamespaceContext
parameter_list|()
block|{
return|return
name|schemaCollection
operator|.
name|getNamespaceContext
argument_list|()
return|;
block|}
specifier|public
name|XmlSchemaType
name|getTypeByQName
parameter_list|(
name|QName
name|schemaTypeName
parameter_list|)
block|{
return|return
name|schemaCollection
operator|.
name|getTypeByQName
argument_list|(
name|schemaTypeName
argument_list|)
return|;
block|}
specifier|public
name|XmlSchema
index|[]
name|getXmlSchema
parameter_list|(
name|String
name|systemId
parameter_list|)
block|{
return|return
name|schemaCollection
operator|.
name|getXmlSchema
argument_list|(
name|systemId
argument_list|)
return|;
block|}
specifier|public
name|XmlSchema
index|[]
name|getXmlSchemas
parameter_list|()
block|{
return|return
name|schemaCollection
operator|.
name|getXmlSchemas
argument_list|()
return|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|schemaCollection
operator|.
name|hashCode
argument_list|()
return|;
block|}
specifier|public
name|void
name|init
parameter_list|()
block|{
name|schemaCollection
operator|.
name|init
argument_list|()
expr_stmt|;
block|}
specifier|public
name|XmlSchema
name|read
parameter_list|(
name|Element
name|elem
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
return|return
name|schemaCollection
operator|.
name|read
argument_list|(
name|elem
argument_list|,
name|uri
argument_list|)
return|;
block|}
specifier|public
name|XmlSchema
name|read
parameter_list|(
name|Document
name|d
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
return|return
name|schemaCollection
operator|.
name|read
argument_list|(
name|d
argument_list|,
name|uri
argument_list|)
return|;
block|}
specifier|public
name|XmlSchema
name|read
parameter_list|(
name|Element
name|elem
parameter_list|)
block|{
return|return
name|schemaCollection
operator|.
name|read
argument_list|(
name|elem
argument_list|)
return|;
block|}
specifier|public
name|void
name|setBaseUri
parameter_list|(
name|String
name|baseUri
parameter_list|)
block|{
name|schemaCollection
operator|.
name|setBaseUri
argument_list|(
name|baseUri
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setExtReg
parameter_list|(
name|ExtensionRegistry
name|extReg
parameter_list|)
block|{
name|schemaCollection
operator|.
name|setExtReg
argument_list|(
name|extReg
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setNamespaceContext
parameter_list|(
name|NamespacePrefixList
name|namespaceContext
parameter_list|)
block|{
name|schemaCollection
operator|.
name|setNamespaceContext
argument_list|(
name|namespaceContext
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setSchemaResolver
parameter_list|(
name|URIResolver
name|schemaResolver
parameter_list|)
block|{
name|schemaCollection
operator|.
name|setSchemaResolver
argument_list|(
name|schemaResolver
argument_list|)
expr_stmt|;
block|}
comment|/**      * This function is not part of the XmlSchema API. Who knows why?      *      * @param namespaceURI targetNamespace      * @return schema, or null.      */
specifier|public
name|XmlSchema
name|getSchemaByTargetNamespace
parameter_list|(
name|String
name|namespaceURI
parameter_list|)
block|{
for|for
control|(
name|XmlSchema
name|schema
range|:
name|schemaCollection
operator|.
name|getXmlSchemas
argument_list|()
control|)
block|{
if|if
condition|(
name|namespaceURI
operator|!=
literal|null
operator|&&
name|namespaceURI
operator|.
name|equals
argument_list|(
name|schema
operator|.
name|getTargetNamespace
argument_list|()
argument_list|)
operator|||
name|namespaceURI
operator|==
literal|null
operator|&&
name|schema
operator|.
name|getTargetNamespace
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
name|schema
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|XmlSchema
name|getSchemaForElement
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
for|for
control|(
name|XmlSchema
name|schema
range|:
name|schemaCollection
operator|.
name|getXmlSchemas
argument_list|()
control|)
block|{
if|if
condition|(
name|name
operator|.
name|getNamespaceURI
argument_list|()
operator|.
name|equals
argument_list|(
name|schema
operator|.
name|getTargetNamespace
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|schema
operator|.
name|getElementByName
argument_list|(
name|name
operator|.
name|getLocalPart
argument_list|()
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
name|schema
return|;
block|}
elseif|else
if|if
condition|(
name|schema
operator|.
name|getElementByName
argument_list|(
name|name
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
name|schema
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Once upon a time, XmlSchema had a bug in the constructor used in this function. So this wrapper was      * created to hold a workaround.      *      * @param namespaceURI TNS for new schema.      * @return new schema      */
specifier|public
name|XmlSchema
name|newXmlSchemaInCollection
parameter_list|(
name|String
name|namespaceURI
parameter_list|)
block|{
return|return
operator|new
name|XmlSchema
argument_list|(
name|namespaceURI
argument_list|,
name|schemaCollection
argument_list|)
return|;
block|}
comment|/**      * Validate that a qualified name points to some namespace in the schema.      *      * @param qname      */
specifier|public
name|void
name|validateQNameNamespace
parameter_list|(
name|QName
name|qname
parameter_list|)
block|{
comment|// astonishingly, xmlSchemaCollection has no accessor by target URL.
if|if
condition|(
literal|""
operator|.
name|equals
argument_list|(
name|qname
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
return|return;
comment|// references to the 'unqualified' namespace are OK even if there is no schema for it.
block|}
for|for
control|(
name|XmlSchema
name|schema
range|:
name|schemaCollection
operator|.
name|getXmlSchemas
argument_list|()
control|)
block|{
if|if
condition|(
name|schema
operator|.
name|getTargetNamespace
argument_list|()
operator|.
name|equals
argument_list|(
name|qname
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
return|return;
block|}
block|}
throw|throw
operator|new
name|InvalidXmlSchemaReferenceException
argument_list|(
name|qname
operator|+
literal|" refers to unknown namespace."
argument_list|)
throw|;
block|}
specifier|public
name|void
name|validateElementName
parameter_list|(
name|QName
name|referrer
parameter_list|,
name|QName
name|elementQName
parameter_list|)
block|{
name|XmlSchemaElement
name|element
init|=
name|schemaCollection
operator|.
name|getElementByQName
argument_list|(
name|elementQName
argument_list|)
decl_stmt|;
if|if
condition|(
name|element
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|InvalidXmlSchemaReferenceException
argument_list|(
name|referrer
operator|+
literal|" references non-existent element "
operator|+
name|elementQName
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|validateTypeName
parameter_list|(
name|QName
name|referrer
parameter_list|,
name|QName
name|typeQName
parameter_list|)
block|{
name|XmlSchemaType
name|type
init|=
name|schemaCollection
operator|.
name|getTypeByQName
argument_list|(
name|typeQName
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|InvalidXmlSchemaReferenceException
argument_list|(
name|referrer
operator|+
literal|" references non-existent type "
operator|+
name|typeQName
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|addCrossImports
parameter_list|()
block|{
comment|/*          * We need to inventory all the cross-imports to see if any are missing.          */
for|for
control|(
name|XmlSchema
name|schema
range|:
name|schemaCollection
operator|.
name|getXmlSchemas
argument_list|()
control|)
block|{
name|addOneSchemaCrossImports
argument_list|(
name|schema
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|addOneSchemaCrossImports
parameter_list|(
name|XmlSchema
name|schema
parameter_list|)
block|{
comment|/*          * We need to visit all the top-level items.          */
for|for
control|(
name|XmlSchemaElement
name|element
range|:
name|schema
operator|.
name|getElements
argument_list|()
operator|.
name|values
argument_list|()
control|)
block|{
name|addElementCrossImportsElement
argument_list|(
name|schema
argument_list|,
name|element
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|XmlSchemaAttribute
name|attribute
range|:
name|schema
operator|.
name|getAttributes
argument_list|()
operator|.
name|values
argument_list|()
control|)
block|{
name|XmlSchemaUtils
operator|.
name|addImportIfNeeded
argument_list|(
name|schema
argument_list|,
name|attribute
operator|.
name|getRef
argument_list|()
operator|.
name|getTargetQName
argument_list|()
argument_list|)
expr_stmt|;
name|XmlSchemaUtils
operator|.
name|addImportIfNeeded
argument_list|(
name|schema
argument_list|,
name|attribute
operator|.
name|getSchemaTypeName
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|XmlSchemaType
name|type
range|:
name|schema
operator|.
name|getSchemaTypes
argument_list|()
operator|.
name|values
argument_list|()
control|)
block|{
name|addCrossImportsType
argument_list|(
name|schema
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|addElementCrossImportsElement
parameter_list|(
name|XmlSchema
name|schema
parameter_list|,
name|XmlSchemaElement
name|item
parameter_list|)
block|{
name|XmlSchemaElement
name|element
init|=
name|item
decl_stmt|;
name|XmlSchemaUtils
operator|.
name|addImportIfNeeded
argument_list|(
name|schema
argument_list|,
name|element
operator|.
name|getRef
argument_list|()
operator|.
name|getTargetQName
argument_list|()
argument_list|)
expr_stmt|;
name|XmlSchemaUtils
operator|.
name|addImportIfNeeded
argument_list|(
name|schema
argument_list|,
name|element
operator|.
name|getSchemaTypeName
argument_list|()
argument_list|)
expr_stmt|;
comment|// if there's an anonymous type, it might have element refs in it.
name|XmlSchemaType
name|schemaType
init|=
name|element
operator|.
name|getSchemaType
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|crossImportsAdded
argument_list|(
name|schema
argument_list|,
name|schemaType
argument_list|)
condition|)
block|{
name|addCrossImportsType
argument_list|(
name|schema
argument_list|,
name|schemaType
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Determines whether the schema has already received (cross) imports for the schemaType      *      * @param schema      * @param schemaType      * @return false if cross imports for schemaType must still be added to schema      */
specifier|private
name|boolean
name|crossImportsAdded
parameter_list|(
name|XmlSchema
name|schema
parameter_list|,
name|XmlSchemaType
name|schemaType
parameter_list|)
block|{
name|boolean
name|result
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|schemaType
operator|!=
literal|null
condition|)
block|{
name|Set
argument_list|<
name|XmlSchemaType
argument_list|>
name|xmlTypesCheckedForCrossImports
decl_stmt|;
if|if
condition|(
operator|!
name|xmlTypesCheckedForCrossImportsPerSchema
operator|.
name|containsKey
argument_list|(
name|schema
argument_list|)
condition|)
block|{
name|xmlTypesCheckedForCrossImports
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
name|xmlTypesCheckedForCrossImportsPerSchema
operator|.
name|put
argument_list|(
name|schema
argument_list|,
name|xmlTypesCheckedForCrossImports
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|xmlTypesCheckedForCrossImports
operator|=
name|xmlTypesCheckedForCrossImportsPerSchema
operator|.
name|get
argument_list|(
name|schema
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|xmlTypesCheckedForCrossImports
operator|.
name|contains
argument_list|(
name|schemaType
argument_list|)
condition|)
block|{
comment|// cross imports for this schemaType have not yet been added
name|xmlTypesCheckedForCrossImports
operator|.
name|add
argument_list|(
name|schemaType
argument_list|)
expr_stmt|;
name|result
operator|=
literal|false
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
specifier|private
name|void
name|addCrossImportsType
parameter_list|(
name|XmlSchema
name|schema
parameter_list|,
name|XmlSchemaType
name|schemaType
parameter_list|)
block|{
comment|// the base type might cross schemas.
if|if
condition|(
name|schemaType
operator|instanceof
name|XmlSchemaComplexType
condition|)
block|{
name|XmlSchemaComplexType
name|complexType
init|=
operator|(
name|XmlSchemaComplexType
operator|)
name|schemaType
decl_stmt|;
name|XmlSchemaUtils
operator|.
name|addImportIfNeeded
argument_list|(
name|schema
argument_list|,
name|complexType
operator|.
name|getBaseSchemaTypeName
argument_list|()
argument_list|)
expr_stmt|;
name|addCrossImports
argument_list|(
name|schema
argument_list|,
name|complexType
operator|.
name|getContentModel
argument_list|()
argument_list|)
expr_stmt|;
name|addCrossImportsAttributeList
argument_list|(
name|schema
argument_list|,
name|complexType
operator|.
name|getAttributes
argument_list|()
argument_list|)
expr_stmt|;
comment|// could it be a choice or something else?
if|if
condition|(
name|complexType
operator|.
name|getParticle
argument_list|()
operator|instanceof
name|XmlSchemaChoice
condition|)
block|{
name|XmlSchemaChoice
name|choice
init|=
operator|(
name|XmlSchemaChoice
operator|)
name|complexType
operator|.
name|getParticle
argument_list|()
decl_stmt|;
name|addCrossImports
argument_list|(
name|schema
argument_list|,
name|choice
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|complexType
operator|.
name|getParticle
argument_list|()
operator|instanceof
name|XmlSchemaAll
condition|)
block|{
name|XmlSchemaAll
name|all
init|=
operator|(
name|XmlSchemaAll
operator|)
name|complexType
operator|.
name|getParticle
argument_list|()
decl_stmt|;
name|addCrossImports
argument_list|(
name|schema
argument_list|,
name|all
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|complexType
operator|.
name|getParticle
argument_list|()
operator|instanceof
name|XmlSchemaSequence
condition|)
block|{
name|XmlSchemaSequence
name|sequence
init|=
operator|(
name|XmlSchemaSequence
operator|)
name|complexType
operator|.
name|getParticle
argument_list|()
decl_stmt|;
name|addCrossImports
argument_list|(
name|schema
argument_list|,
name|sequence
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|addCrossImports
parameter_list|(
name|XmlSchema
name|schema
parameter_list|,
name|XmlSchemaAll
name|all
parameter_list|)
block|{
for|for
control|(
name|XmlSchemaObjectBase
name|seqMember
range|:
name|all
operator|.
name|getItems
argument_list|()
control|)
block|{
if|if
condition|(
name|seqMember
operator|instanceof
name|XmlSchemaElement
condition|)
block|{
name|addElementCrossImportsElement
argument_list|(
name|schema
argument_list|,
operator|(
name|XmlSchemaElement
operator|)
name|seqMember
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|addCrossImports
parameter_list|(
name|XmlSchema
name|schema
parameter_list|,
name|XmlSchemaChoice
name|choice
parameter_list|)
block|{
for|for
control|(
name|XmlSchemaObjectBase
name|seqMember
range|:
name|choice
operator|.
name|getItems
argument_list|()
control|)
block|{
if|if
condition|(
name|seqMember
operator|instanceof
name|XmlSchemaElement
condition|)
block|{
name|addElementCrossImportsElement
argument_list|(
name|schema
argument_list|,
operator|(
name|XmlSchemaElement
operator|)
name|seqMember
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|addCrossImports
parameter_list|(
name|XmlSchema
name|schema
parameter_list|,
name|XmlSchemaSequence
name|sequence
parameter_list|)
block|{
for|for
control|(
name|XmlSchemaSequenceMember
name|seqMember
range|:
name|sequence
operator|.
name|getItems
argument_list|()
control|)
block|{
if|if
condition|(
name|seqMember
operator|instanceof
name|XmlSchemaElement
condition|)
block|{
name|addElementCrossImportsElement
argument_list|(
name|schema
argument_list|,
operator|(
name|XmlSchemaElement
operator|)
name|seqMember
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|addCrossImportsAttributeList
parameter_list|(
name|XmlSchema
name|schema
parameter_list|,
name|List
argument_list|<
name|XmlSchemaAttributeOrGroupRef
argument_list|>
name|list
parameter_list|)
block|{
for|for
control|(
name|XmlSchemaAttributeOrGroupRef
name|attr
range|:
name|list
control|)
block|{
specifier|final
name|QName
name|ref
decl_stmt|;
if|if
condition|(
name|attr
operator|instanceof
name|XmlSchemaAttribute
condition|)
block|{
name|ref
operator|=
operator|(
operator|(
name|XmlSchemaAttribute
operator|)
name|attr
operator|)
operator|.
name|getRef
argument_list|()
operator|.
name|getTargetQName
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|XmlSchemaAttributeGroupRef
name|groupRef
init|=
operator|(
name|XmlSchemaAttributeGroupRef
operator|)
name|attr
decl_stmt|;
name|ref
operator|=
name|groupRef
operator|.
name|getRef
argument_list|()
operator|.
name|getTargetQName
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
name|XmlSchemaUtils
operator|.
name|addImportIfNeeded
argument_list|(
name|schema
argument_list|,
name|ref
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|addCrossImports
parameter_list|(
name|XmlSchema
name|schema
parameter_list|,
name|XmlSchemaContentModel
name|contentModel
parameter_list|)
block|{
if|if
condition|(
name|contentModel
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|XmlSchemaContent
name|content
init|=
name|contentModel
operator|.
name|getContent
argument_list|()
decl_stmt|;
if|if
condition|(
name|content
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|content
operator|instanceof
name|XmlSchemaComplexContentExtension
condition|)
block|{
name|XmlSchemaComplexContentExtension
name|extension
init|=
operator|(
name|XmlSchemaComplexContentExtension
operator|)
name|content
decl_stmt|;
name|XmlSchemaUtils
operator|.
name|addImportIfNeeded
argument_list|(
name|schema
argument_list|,
name|extension
operator|.
name|getBaseTypeName
argument_list|()
argument_list|)
expr_stmt|;
name|addCrossImportsAttributeList
argument_list|(
name|schema
argument_list|,
name|extension
operator|.
name|getAttributes
argument_list|()
argument_list|)
expr_stmt|;
name|XmlSchemaParticle
name|particle
init|=
name|extension
operator|.
name|getParticle
argument_list|()
decl_stmt|;
if|if
condition|(
name|particle
operator|instanceof
name|XmlSchemaSequence
condition|)
block|{
name|addCrossImports
argument_list|(
name|schema
argument_list|,
operator|(
name|XmlSchemaSequence
operator|)
name|particle
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|particle
operator|instanceof
name|XmlSchemaChoice
condition|)
block|{
name|addCrossImports
argument_list|(
name|schema
argument_list|,
operator|(
name|XmlSchemaChoice
operator|)
name|particle
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|particle
operator|instanceof
name|XmlSchemaAll
condition|)
block|{
name|addCrossImports
argument_list|(
name|schema
argument_list|,
operator|(
name|XmlSchemaAll
operator|)
name|particle
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|content
operator|instanceof
name|XmlSchemaComplexContentRestriction
condition|)
block|{
name|XmlSchemaComplexContentRestriction
name|restriction
init|=
operator|(
name|XmlSchemaComplexContentRestriction
operator|)
name|content
decl_stmt|;
name|XmlSchemaUtils
operator|.
name|addImportIfNeeded
argument_list|(
name|schema
argument_list|,
name|restriction
operator|.
name|getBaseTypeName
argument_list|()
argument_list|)
expr_stmt|;
name|addCrossImportsAttributeList
argument_list|(
name|schema
argument_list|,
name|restriction
operator|.
name|getAttributes
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|content
operator|instanceof
name|XmlSchemaSimpleContentExtension
condition|)
block|{
name|XmlSchemaSimpleContentExtension
name|extension
init|=
operator|(
name|XmlSchemaSimpleContentExtension
operator|)
name|content
decl_stmt|;
name|XmlSchemaUtils
operator|.
name|addImportIfNeeded
argument_list|(
name|schema
argument_list|,
name|extension
operator|.
name|getBaseTypeName
argument_list|()
argument_list|)
expr_stmt|;
name|addCrossImportsAttributeList
argument_list|(
name|schema
argument_list|,
name|extension
operator|.
name|getAttributes
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|content
operator|instanceof
name|XmlSchemaSimpleContentRestriction
condition|)
block|{
name|XmlSchemaSimpleContentRestriction
name|restriction
init|=
operator|(
name|XmlSchemaSimpleContentRestriction
operator|)
name|content
decl_stmt|;
name|XmlSchemaUtils
operator|.
name|addImportIfNeeded
argument_list|(
name|schema
argument_list|,
name|restriction
operator|.
name|getBaseTypeName
argument_list|()
argument_list|)
expr_stmt|;
name|addCrossImportsAttributeList
argument_list|(
name|schema
argument_list|,
name|restriction
operator|.
name|getAttributes
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

