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
name|javascript
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|namespace
operator|.
name|QName
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
name|common
operator|.
name|xmlschema
operator|.
name|XmlSchemaUtils
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
name|XmlSchemaChoiceMember
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
name|XmlSchemaObject
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
name|constants
operator|.
name|Constants
import|;
end_import

begin_comment
comment|/**  * All the information needed to create the JavaScript for an Xml Schema element  * or xs:any.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|ParticleInfo
implements|implements
name|ItemInfo
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
name|ParticleInfo
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|XmlSchemaParticle
name|particle
decl_stmt|;
specifier|private
name|String
name|javascriptName
decl_stmt|;
specifier|private
name|String
name|xmlName
decl_stmt|;
specifier|private
name|XmlSchemaType
name|containingType
decl_stmt|;
comment|// in the RPC case, we can have a type and no element.
specifier|private
name|XmlSchemaType
name|type
decl_stmt|;
specifier|private
name|boolean
name|empty
decl_stmt|;
specifier|private
name|boolean
name|isGroup
decl_stmt|;
specifier|private
name|List
argument_list|<
name|ParticleInfo
argument_list|>
name|children
decl_stmt|;
comment|// These are exactly the same values as we find in the XmlSchemaElement.
comment|// there is no rationalization. But the accessors take care of business.
specifier|private
name|long
name|minOccurs
decl_stmt|;
specifier|private
name|long
name|maxOccurs
decl_stmt|;
specifier|private
name|boolean
name|nillable
decl_stmt|;
specifier|private
name|boolean
name|any
decl_stmt|;
specifier|private
name|boolean
name|anyType
decl_stmt|;
specifier|private
name|String
name|defaultValue
decl_stmt|;
specifier|private
name|boolean
name|global
decl_stmt|;
specifier|private
name|ParticleInfo
parameter_list|()
block|{     }
comment|/**      * Create an elementInfo that stores information about a global, named,      * element.      *      * @param element the element      * @param currentSchema the schema it came from.      * @param schemaCollection the collection of all schemas.      * @param prefixAccumulator the accumulator that assigns prefixes.      * @return      */
specifier|public
specifier|static
name|ParticleInfo
name|forGlobalElement
parameter_list|(
name|XmlSchemaElement
name|element
parameter_list|,
name|XmlSchema
name|currentSchema
parameter_list|,
name|SchemaCollection
name|schemaCollection
parameter_list|,
name|NamespacePrefixAccumulator
name|prefixAccumulator
parameter_list|)
block|{
name|ParticleInfo
name|elementInfo
init|=
operator|new
name|ParticleInfo
argument_list|()
decl_stmt|;
name|elementInfo
operator|.
name|particle
operator|=
name|element
expr_stmt|;
name|elementInfo
operator|.
name|minOccurs
operator|=
name|element
operator|.
name|getMinOccurs
argument_list|()
expr_stmt|;
name|elementInfo
operator|.
name|maxOccurs
operator|=
name|element
operator|.
name|getMaxOccurs
argument_list|()
expr_stmt|;
name|elementInfo
operator|.
name|nillable
operator|=
name|element
operator|.
name|isNillable
argument_list|()
expr_stmt|;
name|elementInfo
operator|.
name|global
operator|=
literal|true
expr_stmt|;
name|factoryCommon
argument_list|(
name|element
argument_list|,
name|currentSchema
argument_list|,
name|schemaCollection
argument_list|,
name|prefixAccumulator
argument_list|,
name|elementInfo
argument_list|)
expr_stmt|;
return|return
name|elementInfo
return|;
block|}
comment|/**      * Create element information for a part element. For a part, the JavaScript      * and Element names are calculated in advance, and the element itself might      * be null! In that case, the minOccurs and maxOccurs are conventional. Note      * that in some cases the code in ServiceJavascriptBuilder uses a local      * element (or xa:any) from inside the part element, instead of the part      * element itself.      *      * @param element the element, or null      * @param schemaCollection the schema collection, for resolving types.      * @param javascriptName javascript variable name      * @param xmlElementName xml element string      * @return      */
specifier|public
specifier|static
name|ParticleInfo
name|forPartElement
parameter_list|(
name|XmlSchemaElement
name|element
parameter_list|,
name|SchemaCollection
name|schemaCollection
parameter_list|,
name|String
name|javascriptName
parameter_list|,
name|String
name|xmlElementName
parameter_list|)
block|{
name|ParticleInfo
name|elementInfo
init|=
operator|new
name|ParticleInfo
argument_list|()
decl_stmt|;
name|elementInfo
operator|.
name|particle
operator|=
name|element
expr_stmt|;
if|if
condition|(
name|element
operator|==
literal|null
condition|)
block|{
name|elementInfo
operator|.
name|minOccurs
operator|=
literal|1
expr_stmt|;
name|elementInfo
operator|.
name|maxOccurs
operator|=
literal|1
expr_stmt|;
block|}
else|else
block|{
name|elementInfo
operator|.
name|minOccurs
operator|=
name|element
operator|.
name|getMinOccurs
argument_list|()
expr_stmt|;
name|elementInfo
operator|.
name|maxOccurs
operator|=
name|element
operator|.
name|getMaxOccurs
argument_list|()
expr_stmt|;
name|elementInfo
operator|.
name|nillable
operator|=
name|element
operator|.
name|isNillable
argument_list|()
expr_stmt|;
name|factorySetupType
argument_list|(
name|element
argument_list|,
name|schemaCollection
argument_list|,
name|elementInfo
argument_list|)
expr_stmt|;
block|}
name|elementInfo
operator|.
name|javascriptName
operator|=
name|javascriptName
expr_stmt|;
name|elementInfo
operator|.
name|xmlName
operator|=
name|xmlElementName
expr_stmt|;
name|elementInfo
operator|.
name|global
operator|=
literal|true
expr_stmt|;
return|return
name|elementInfo
return|;
block|}
comment|/**      * Fill in an ElementInfo for an element or xs:any from a sequence.      *      * @param sequenceElement      * @param currentSchema      * @param schemaCollection      * @param prefixAccumulator      * @return      */
specifier|public
specifier|static
name|ParticleInfo
name|forLocalItem
parameter_list|(
name|XmlSchemaObject
name|sequenceObject
parameter_list|,
name|XmlSchema
name|currentSchema
parameter_list|,
name|SchemaCollection
name|schemaCollection
parameter_list|,
name|NamespacePrefixAccumulator
name|prefixAccumulator
parameter_list|,
name|QName
name|contextName
parameter_list|)
block|{
name|XmlSchemaParticle
name|sequenceParticle
init|=
name|JavascriptUtils
operator|.
name|getObjectParticle
argument_list|(
name|sequenceObject
argument_list|,
name|contextName
argument_list|)
decl_stmt|;
name|ParticleInfo
name|elementInfo
init|=
operator|new
name|ParticleInfo
argument_list|()
decl_stmt|;
name|XmlSchemaParticle
name|realParticle
init|=
name|sequenceParticle
decl_stmt|;
if|if
condition|(
name|sequenceParticle
operator|instanceof
name|XmlSchemaElement
condition|)
block|{
name|XmlSchemaElement
name|sequenceElement
init|=
operator|(
name|XmlSchemaElement
operator|)
name|sequenceParticle
decl_stmt|;
if|if
condition|(
name|sequenceElement
operator|.
name|getRef
argument_list|()
operator|.
name|getTargetQName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|XmlSchemaElement
name|refElement
init|=
name|sequenceElement
operator|.
name|getRef
argument_list|()
operator|.
name|getTarget
argument_list|()
decl_stmt|;
if|if
condition|(
name|refElement
operator|==
literal|null
condition|)
block|{
name|Message
name|message
init|=
operator|new
name|Message
argument_list|(
literal|"ELEMENT_DANGLING_REFERENCE"
argument_list|,
name|LOG
argument_list|,
name|sequenceElement
operator|.
name|getQName
argument_list|()
argument_list|,
name|sequenceElement
operator|.
name|getRef
argument_list|()
operator|.
name|getTargetQName
argument_list|()
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|UnsupportedConstruct
argument_list|(
name|message
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
name|realParticle
operator|=
name|refElement
expr_stmt|;
name|elementInfo
operator|.
name|global
operator|=
literal|true
expr_stmt|;
block|}
name|elementInfo
operator|.
name|nillable
operator|=
operator|(
operator|(
name|XmlSchemaElement
operator|)
name|realParticle
operator|)
operator|.
name|isNillable
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|sequenceParticle
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
name|sequenceParticle
decl_stmt|;
if|if
condition|(
name|sequenceParticle
operator|.
name|getMaxOccurs
argument_list|()
operator|>
literal|1
condition|)
block|{
name|Message
name|message
init|=
operator|new
name|Message
argument_list|(
literal|"GROUP_ELEMENT_MULTI_OCCURS"
argument_list|,
name|LOG
argument_list|,
name|sequenceParticle
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|UnsupportedConstruct
argument_list|(
name|message
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
name|elementInfo
operator|.
name|children
operator|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|XmlSchemaChoiceMember
argument_list|>
name|items
init|=
name|choice
operator|.
name|getItems
argument_list|()
decl_stmt|;
for|for
control|(
name|XmlSchemaChoiceMember
name|item
range|:
name|items
control|)
block|{
name|XmlSchemaObject
name|schemaObject
init|=
operator|(
name|XmlSchemaObject
operator|)
name|item
decl_stmt|;
name|ParticleInfo
name|childParticle
init|=
name|ParticleInfo
operator|.
name|forLocalItem
argument_list|(
name|schemaObject
argument_list|,
name|currentSchema
argument_list|,
name|schemaCollection
argument_list|,
name|prefixAccumulator
argument_list|,
name|contextName
argument_list|)
decl_stmt|;
if|if
condition|(
name|childParticle
operator|.
name|isAny
argument_list|()
condition|)
block|{
name|Message
name|message
init|=
operator|new
name|Message
argument_list|(
literal|"GROUP_ELEMENT_ANY"
argument_list|,
name|LOG
argument_list|,
name|sequenceParticle
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|UnsupportedConstruct
argument_list|(
name|message
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
name|childParticle
operator|.
name|minOccurs
operator|=
literal|0
expr_stmt|;
name|elementInfo
operator|.
name|children
operator|.
name|add
argument_list|(
name|childParticle
argument_list|)
expr_stmt|;
block|}
block|}
name|elementInfo
operator|.
name|minOccurs
operator|=
name|sequenceParticle
operator|.
name|getMinOccurs
argument_list|()
expr_stmt|;
name|elementInfo
operator|.
name|maxOccurs
operator|=
name|sequenceParticle
operator|.
name|getMaxOccurs
argument_list|()
expr_stmt|;
name|factoryCommon
argument_list|(
name|realParticle
argument_list|,
name|currentSchema
argument_list|,
name|schemaCollection
argument_list|,
name|prefixAccumulator
argument_list|,
name|elementInfo
argument_list|)
expr_stmt|;
name|elementInfo
operator|.
name|particle
operator|=
name|realParticle
expr_stmt|;
return|return
name|elementInfo
return|;
block|}
specifier|private
specifier|static
name|void
name|factoryCommon
parameter_list|(
name|XmlSchemaParticle
name|particle
parameter_list|,
name|XmlSchema
name|currentSchema
parameter_list|,
name|SchemaCollection
name|schemaCollection
parameter_list|,
name|NamespacePrefixAccumulator
name|prefixAccumulator
parameter_list|,
name|ParticleInfo
name|elementInfo
parameter_list|)
block|{
if|if
condition|(
name|particle
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
name|particle
decl_stmt|;
name|QName
name|elementQName
init|=
name|XmlSchemaUtils
operator|.
name|getElementQualifiedName
argument_list|(
name|element
argument_list|,
name|currentSchema
argument_list|)
decl_stmt|;
name|String
name|elementNamespaceURI
init|=
name|elementQName
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
name|boolean
name|elementNoNamespace
init|=
literal|""
operator|.
name|equals
argument_list|(
name|elementNamespaceURI
argument_list|)
decl_stmt|;
name|XmlSchema
name|elementSchema
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|elementNoNamespace
condition|)
block|{
name|elementSchema
operator|=
name|schemaCollection
operator|.
name|getSchemaByTargetNamespace
argument_list|(
name|elementNamespaceURI
argument_list|)
expr_stmt|;
if|if
condition|(
name|elementSchema
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Missing schema "
operator|+
name|elementNamespaceURI
argument_list|)
throw|;
block|}
block|}
name|boolean
name|qualified
init|=
operator|!
name|elementNoNamespace
operator|&&
name|XmlSchemaUtils
operator|.
name|isElementQualified
argument_list|(
name|element
argument_list|,
literal|true
argument_list|,
name|currentSchema
argument_list|,
name|elementSchema
argument_list|)
decl_stmt|;
name|elementInfo
operator|.
name|xmlName
operator|=
name|prefixAccumulator
operator|.
name|xmlElementString
argument_list|(
name|elementQName
argument_list|,
name|qualified
argument_list|)
expr_stmt|;
comment|// we are assuming here that we are not dealing, in close proximity,
comment|// with elements with identical local names and different
comment|// namespaces.
name|elementInfo
operator|.
name|javascriptName
operator|=
name|elementQName
operator|.
name|getLocalPart
argument_list|()
expr_stmt|;
name|String
name|schemaDefaultValue
init|=
name|element
operator|.
name|getDefaultValue
argument_list|()
decl_stmt|;
comment|/*              * Schema default values are carried as strings.              * In javascript, for actual strings, we need quotes, but not for              * numbers. The following is a trick.              */
name|schemaDefaultValue
operator|=
name|protectDefaultValue
argument_list|(
name|schemaDefaultValue
argument_list|)
expr_stmt|;
name|elementInfo
operator|.
name|defaultValue
operator|=
name|schemaDefaultValue
expr_stmt|;
name|factorySetupType
argument_list|(
name|element
argument_list|,
name|schemaCollection
argument_list|,
name|elementInfo
argument_list|)
expr_stmt|;
name|elementInfo
operator|.
name|isGroup
operator|=
literal|false
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
name|elementInfo
operator|.
name|isGroup
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
comment|// any
name|elementInfo
operator|.
name|any
operator|=
literal|true
expr_stmt|;
name|elementInfo
operator|.
name|xmlName
operator|=
literal|null
expr_stmt|;
comment|// unknown until runtime.
comment|// TODO: multiple 'any'
name|elementInfo
operator|.
name|javascriptName
operator|=
literal|"any"
expr_stmt|;
name|elementInfo
operator|.
name|type
operator|=
literal|null
expr_stmt|;
comment|// runtime for any.
name|elementInfo
operator|.
name|isGroup
operator|=
literal|false
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|String
name|protectDefaultValue
parameter_list|(
name|String
name|schemaDefaultValue
parameter_list|)
block|{
if|if
condition|(
name|schemaDefaultValue
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|boolean
name|leaveAlone
init|=
literal|false
decl_stmt|;
try|try
block|{
name|Long
operator|.
name|parseLong
argument_list|(
name|schemaDefaultValue
argument_list|)
expr_stmt|;
name|leaveAlone
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|nfe
parameter_list|)
block|{
try|try
block|{
name|Double
operator|.
name|parseDouble
argument_list|(
name|schemaDefaultValue
argument_list|)
expr_stmt|;
name|leaveAlone
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|nfe2
parameter_list|)
block|{
comment|//
block|}
block|}
if|if
condition|(
operator|!
name|leaveAlone
condition|)
block|{
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"'"
argument_list|)
expr_stmt|;
for|for
control|(
name|char
name|c
range|:
name|schemaDefaultValue
operator|.
name|toCharArray
argument_list|()
control|)
block|{
if|if
condition|(
name|c
operator|==
literal|'\''
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|"\\'"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|c
operator|==
literal|'\\'
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|"\\\\"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
name|builder
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|schemaDefaultValue
operator|=
name|builder
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
return|return
name|schemaDefaultValue
return|;
block|}
specifier|private
specifier|static
name|void
name|factorySetupType
parameter_list|(
name|XmlSchemaElement
name|element
parameter_list|,
name|SchemaCollection
name|schemaCollection
parameter_list|,
name|ParticleInfo
name|elementInfo
parameter_list|)
block|{
name|elementInfo
operator|.
name|type
operator|=
name|element
operator|.
name|getSchemaType
argument_list|()
expr_stmt|;
if|if
condition|(
name|elementInfo
operator|.
name|type
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|element
operator|.
name|getSchemaTypeName
argument_list|()
operator|==
literal|null
comment|// no type at all -> anyType
operator|||
name|element
operator|.
name|getSchemaTypeName
argument_list|()
operator|.
name|equals
argument_list|(
name|Constants
operator|.
name|XSD_ANYTYPE
argument_list|)
condition|)
block|{
name|elementInfo
operator|.
name|anyType
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|elementInfo
operator|.
name|type
operator|=
name|schemaCollection
operator|.
name|getTypeByQName
argument_list|(
name|element
operator|.
name|getSchemaTypeName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|elementInfo
operator|.
name|type
operator|==
literal|null
operator|&&
operator|!
name|element
operator|.
name|getSchemaTypeName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
operator|.
name|equals
argument_list|(
name|Constants
operator|.
name|URI_2001_SCHEMA_XSD
argument_list|)
condition|)
block|{
name|JavascriptUtils
operator|.
name|unsupportedConstruct
argument_list|(
literal|"MISSING_TYPE"
argument_list|,
name|element
operator|.
name|getSchemaTypeName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|element
operator|.
name|getQName
argument_list|()
argument_list|,
name|element
argument_list|)
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|elementInfo
operator|.
name|type
operator|.
name|getQName
argument_list|()
operator|!=
literal|null
operator|&&
name|Constants
operator|.
name|XSD_ANYTYPE
operator|.
name|equals
argument_list|(
name|elementInfo
operator|.
name|type
operator|.
name|getQName
argument_list|()
argument_list|)
condition|)
block|{
name|elementInfo
operator|.
name|anyType
operator|=
literal|true
expr_stmt|;
block|}
block|}
comment|/**      * As a general rule, the JavaScript code is organized by types. The      * exception is global elements that have anonymous types. In those cases,      * the JavaScript code has its functions named according to the element.      * This method returns the QName for the type or element, accordingly. If a      * schema has a local element with an anonymous, complex, type, this will      * throw. This will need to be fixed.      *      * @return the qname.      */
specifier|public
name|QName
name|getControllingName
parameter_list|()
block|{
if|if
condition|(
name|type
operator|!=
literal|null
operator|&&
name|type
operator|.
name|getQName
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|type
operator|.
name|getQName
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|particle
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
name|particle
decl_stmt|;
if|if
condition|(
name|element
operator|.
name|getQName
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|element
operator|.
name|getQName
argument_list|()
return|;
block|}
block|}
name|Message
name|message
init|=
operator|new
name|Message
argument_list|(
literal|"IMPOSSIBLE_GLOBAL_ITEM"
argument_list|,
name|LOG
argument_list|,
name|JavascriptUtils
operator|.
name|cleanedUpSchemaSource
argument_list|(
name|particle
argument_list|)
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|severe
argument_list|(
name|message
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|UnsupportedConstruct
argument_list|(
name|message
argument_list|)
throw|;
block|}
specifier|public
name|XmlSchemaParticle
name|getParticle
parameter_list|()
block|{
return|return
name|particle
return|;
block|}
specifier|public
name|String
name|getJavascriptName
parameter_list|()
block|{
return|return
name|javascriptName
return|;
block|}
specifier|public
name|void
name|setJavascriptName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|javascriptName
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|String
name|getXmlName
parameter_list|()
block|{
return|return
name|xmlName
return|;
block|}
specifier|public
name|void
name|setXmlName
parameter_list|(
name|String
name|elementXmlName
parameter_list|)
block|{
name|this
operator|.
name|xmlName
operator|=
name|elementXmlName
expr_stmt|;
block|}
specifier|public
name|XmlSchemaType
name|getContainingType
parameter_list|()
block|{
return|return
name|containingType
return|;
block|}
specifier|public
name|void
name|setContainingType
parameter_list|(
name|XmlSchemaType
name|containingType
parameter_list|)
block|{
name|this
operator|.
name|containingType
operator|=
name|containingType
expr_stmt|;
block|}
specifier|public
name|XmlSchemaType
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
specifier|public
name|void
name|setType
parameter_list|(
name|XmlSchemaType
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
specifier|public
name|boolean
name|isGroup
parameter_list|()
block|{
return|return
name|isGroup
return|;
block|}
specifier|public
name|List
argument_list|<
name|ParticleInfo
argument_list|>
name|getChildren
parameter_list|()
block|{
return|return
name|children
return|;
block|}
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|empty
return|;
block|}
specifier|public
name|void
name|setEmpty
parameter_list|(
name|boolean
name|empty
parameter_list|)
block|{
name|this
operator|.
name|empty
operator|=
name|empty
expr_stmt|;
block|}
specifier|public
name|long
name|getMinOccurs
parameter_list|()
block|{
return|return
name|minOccurs
return|;
block|}
specifier|public
name|long
name|getMaxOccurs
parameter_list|()
block|{
return|return
name|maxOccurs
return|;
block|}
specifier|public
name|boolean
name|isArray
parameter_list|()
block|{
return|return
name|maxOccurs
operator|>
literal|1
return|;
block|}
specifier|public
name|boolean
name|isOptional
parameter_list|()
block|{
return|return
name|minOccurs
operator|==
literal|0
operator|&&
name|maxOccurs
operator|==
literal|1
return|;
block|}
comment|/**      * @return Returns the nillable flag for the element. False for 'xs:any'      */
specifier|public
name|boolean
name|isNillable
parameter_list|()
block|{
return|return
name|nillable
return|;
block|}
specifier|public
name|boolean
name|isAny
parameter_list|()
block|{
return|return
name|any
return|;
block|}
specifier|public
name|boolean
name|isAnyType
parameter_list|()
block|{
return|return
name|anyType
return|;
block|}
comment|/**      * *      *      * @return Returns the defaultValue.      */
specifier|public
name|String
name|getDefaultValue
parameter_list|()
block|{
if|if
condition|(
name|isOptional
argument_list|()
condition|)
block|{
return|return
literal|"null"
return|;
block|}
else|else
block|{
return|return
name|defaultValue
return|;
block|}
block|}
comment|/**      * @param defaultValue The defaultValue to set.      */
specifier|public
name|void
name|setDefaultValue
parameter_list|(
name|String
name|defaultValue
parameter_list|)
block|{
name|this
operator|.
name|defaultValue
operator|=
name|defaultValue
expr_stmt|;
block|}
comment|/**      * True if this describes a global, named, element.      *      * @return      */
specifier|public
name|boolean
name|isGlobal
parameter_list|()
block|{
return|return
name|global
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"ItemInfo: "
operator|+
name|javascriptName
return|;
block|}
block|}
end_class

end_unit

