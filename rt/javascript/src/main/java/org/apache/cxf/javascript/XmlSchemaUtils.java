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
name|wsdl
operator|.
name|WSDLConstants
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
name|XmlSchemaAnnotated
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
name|XmlSchemaAny
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
name|XmlSchemaAnyAttribute
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
name|XmlSchemaType
import|;
end_import

begin_comment
comment|/**  * There are a number of pitfalls in Commons Xml Schema. This class contains  * some utilities that avoid some of the problems and centralizes some  * repetitive tasks.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|XmlSchemaUtils
block|{
specifier|public
specifier|static
specifier|final
name|XmlSchemaForm
name|QUALIFIED
init|=
operator|new
name|XmlSchemaForm
argument_list|(
name|XmlSchemaForm
operator|.
name|QUALIFIED
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|XmlSchemaForm
name|UNQUALIFIED
init|=
operator|new
name|XmlSchemaForm
argument_list|(
name|XmlSchemaForm
operator|.
name|UNQUALIFIED
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|XSI_NS_ATTR
init|=
name|WSDLConstants
operator|.
name|NP_XMLNS
operator|+
literal|":"
operator|+
name|WSDLConstants
operator|.
name|NP_SCHEMA_XSI
operator|+
literal|"='"
operator|+
name|WSDLConstants
operator|.
name|NS_SCHEMA_XSI
operator|+
literal|"'"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|XSI_NIL_WITH_PREFIX
init|=
name|XSI_NS_ATTR
operator|+
literal|" xsi:nil='true'"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|XSI_NIL
init|=
literal|"xsi:nil='true'"
decl_stmt|;
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
name|XmlSchemaUtils
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|XmlSchemaSequence
name|EMPTY_SEQUENCE
init|=
operator|new
name|XmlSchemaSequence
argument_list|()
decl_stmt|;
specifier|private
name|XmlSchemaUtils
parameter_list|()
block|{     }
specifier|static
name|String
name|cleanedUpSchemaSource
parameter_list|(
name|XmlSchemaObject
name|subject
parameter_list|)
block|{
if|if
condition|(
name|subject
operator|.
name|getSourceURI
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|""
return|;
block|}
else|else
block|{
return|return
name|subject
operator|.
name|getSourceURI
argument_list|()
operator|+
literal|":"
operator|+
name|subject
operator|.
name|getLineNumber
argument_list|()
return|;
block|}
block|}
specifier|public
specifier|static
name|void
name|unsupportedConstruct
parameter_list|(
name|String
name|messageKey
parameter_list|,
name|XmlSchemaType
name|subject
parameter_list|)
block|{
name|Message
name|message
init|=
operator|new
name|Message
argument_list|(
name|messageKey
argument_list|,
name|LOG
argument_list|,
name|subject
operator|.
name|getQName
argument_list|()
argument_list|,
name|cleanedUpSchemaSource
argument_list|(
name|subject
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
specifier|static
name|void
name|unsupportedConstruct
parameter_list|(
name|String
name|messageKey
parameter_list|,
name|String
name|what
parameter_list|,
name|QName
name|subjectName
parameter_list|,
name|XmlSchemaObject
name|subject
parameter_list|)
block|{
name|Message
name|message
init|=
operator|new
name|Message
argument_list|(
name|messageKey
argument_list|,
name|LOG
argument_list|,
name|what
argument_list|,
name|subjectName
operator|==
literal|null
condition|?
literal|"anonymous"
else|:
name|subjectName
argument_list|,
name|cleanedUpSchemaSource
argument_list|(
name|subject
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
specifier|static
name|XmlSchemaSequence
name|getSequence
parameter_list|(
name|XmlSchemaComplexType
name|type
parameter_list|)
block|{
name|XmlSchemaParticle
name|particle
init|=
name|type
operator|.
name|getParticle
argument_list|()
decl_stmt|;
name|XmlSchemaSequence
name|sequence
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|particle
operator|==
literal|null
condition|)
block|{
comment|// the code that uses this wants to iterate. An empty one is more useful than
comment|// a null pointer, and certainly an exception.
return|return
name|EMPTY_SEQUENCE
return|;
block|}
try|try
block|{
name|sequence
operator|=
operator|(
name|XmlSchemaSequence
operator|)
name|particle
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|cce
parameter_list|)
block|{
name|unsupportedConstruct
argument_list|(
literal|"NON_SEQUENCE_PARTICLE"
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
return|return
name|sequence
return|;
block|}
comment|/**      * This copes with an observed phenomenon in the schema built by the      * ReflectionServiceFactoryBean. It is creating element such that: (a) the      * type is not set. (b) the refName is set. (c) the namespaceURI in the      * refName is set empty. This apparently indicates 'same Schema' to everyone      * else, so thus function implements that convention here. It is unclear if      * that is a correct structure, and it if changes, we can simplify or      * eliminate this function.      *       * @param name      * @param referencingURI      * @return      */
specifier|public
specifier|static
name|XmlSchemaElement
name|findElementByRefName
parameter_list|(
name|SchemaCollection
name|xmlSchemaCollection
parameter_list|,
name|QName
name|name
parameter_list|,
name|String
name|referencingURI
parameter_list|)
block|{
name|String
name|uri
init|=
name|name
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
if|if
condition|(
literal|""
operator|.
name|equals
argument_list|(
name|uri
argument_list|)
condition|)
block|{
name|uri
operator|=
name|referencingURI
expr_stmt|;
block|}
name|QName
name|copyName
init|=
operator|new
name|QName
argument_list|(
name|uri
argument_list|,
name|name
operator|.
name|getLocalPart
argument_list|()
argument_list|)
decl_stmt|;
name|XmlSchemaElement
name|target
init|=
name|xmlSchemaCollection
operator|.
name|getElementByQName
argument_list|(
name|copyName
argument_list|)
decl_stmt|;
assert|assert
name|target
operator|!=
literal|null
assert|;
return|return
name|target
return|;
block|}
comment|/**      * Follow a chain of references from element to element until we can obtain      * a type.      *       * @param element      * @return      */
specifier|public
specifier|static
name|XmlSchemaType
name|getElementType
parameter_list|(
name|SchemaCollection
name|xmlSchemaCollection
parameter_list|,
name|String
name|referencingURI
parameter_list|,
name|XmlSchemaElement
name|element
parameter_list|,
name|XmlSchemaType
name|containingType
parameter_list|)
block|{
if|if
condition|(
name|element
operator|.
name|getSchemaTypeName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|XmlSchemaType
name|type
init|=
name|xmlSchemaCollection
operator|.
name|getTypeByQName
argument_list|(
name|element
operator|.
name|getSchemaTypeName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
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
literal|"ELEMENT_TYPE_MISSING"
argument_list|,
name|LOG
argument_list|,
name|element
operator|.
name|getQName
argument_list|()
argument_list|,
name|element
operator|.
name|getSchemaTypeName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|UnsupportedConstruct
argument_list|(
name|message
argument_list|)
throw|;
block|}
return|return
name|type
return|;
block|}
assert|assert
name|element
operator|!=
literal|null
assert|;
comment|// The referencing URI only helps if there is a schema that points to
comment|// it.
comment|// It might be the URI for the wsdl TNS, which might have no schema.
if|if
condition|(
name|xmlSchemaCollection
operator|.
name|getSchemaByTargetNamespace
argument_list|(
name|referencingURI
argument_list|)
operator|==
literal|null
condition|)
block|{
name|referencingURI
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|referencingURI
operator|==
literal|null
operator|&&
name|containingType
operator|!=
literal|null
condition|)
block|{
name|referencingURI
operator|=
name|containingType
operator|.
name|getQName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
expr_stmt|;
block|}
name|XmlSchemaElement
name|originalElement
init|=
name|element
decl_stmt|;
while|while
condition|(
name|element
operator|.
name|getSchemaType
argument_list|()
operator|==
literal|null
operator|&&
name|element
operator|.
name|getRefName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|XmlSchemaElement
name|nextElement
init|=
name|findElementByRefName
argument_list|(
name|xmlSchemaCollection
argument_list|,
name|element
operator|.
name|getRefName
argument_list|()
argument_list|,
name|referencingURI
argument_list|)
decl_stmt|;
assert|assert
name|nextElement
operator|!=
literal|null
assert|;
name|element
operator|=
name|nextElement
expr_stmt|;
block|}
if|if
condition|(
name|element
operator|.
name|getSchemaType
argument_list|()
operator|==
literal|null
condition|)
block|{
name|XmlSchemaUtils
operator|.
name|unsupportedConstruct
argument_list|(
literal|"ELEMENT_HAS_NO_TYPE"
argument_list|,
name|originalElement
operator|.
name|getName
argument_list|()
argument_list|,
name|containingType
operator|.
name|getQName
argument_list|()
argument_list|,
name|containingType
argument_list|)
expr_stmt|;
block|}
return|return
name|element
operator|.
name|getSchemaType
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isComplexType
parameter_list|(
name|XmlSchemaType
name|type
parameter_list|)
block|{
return|return
name|type
operator|instanceof
name|XmlSchemaComplexType
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isElementNameQualified
parameter_list|(
name|XmlSchemaElement
name|element
parameter_list|,
name|XmlSchema
name|schema
parameter_list|)
block|{
if|if
condition|(
name|element
operator|.
name|getRefName
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"isElementNameQualified on element with ref="
argument_list|)
throw|;
block|}
if|if
condition|(
name|element
operator|.
name|getForm
argument_list|()
operator|.
name|equals
argument_list|(
name|QUALIFIED
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|element
operator|.
name|getForm
argument_list|()
operator|.
name|equals
argument_list|(
name|UNQUALIFIED
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|schema
operator|.
name|getElementFormDefault
argument_list|()
operator|.
name|equals
argument_list|(
name|QUALIFIED
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isAttributeNameQualified
parameter_list|(
name|XmlSchemaAttribute
name|attribute
parameter_list|,
name|XmlSchema
name|schema
parameter_list|)
block|{
if|if
condition|(
name|attribute
operator|.
name|getRefName
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"isElementNameQualified on element with ref="
argument_list|)
throw|;
block|}
if|if
condition|(
name|attribute
operator|.
name|getForm
argument_list|()
operator|.
name|equals
argument_list|(
name|QUALIFIED
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|attribute
operator|.
name|getForm
argument_list|()
operator|.
name|equals
argument_list|(
name|UNQUALIFIED
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|schema
operator|.
name|getAttributeFormDefault
argument_list|()
operator|.
name|equals
argument_list|(
name|QUALIFIED
argument_list|)
return|;
block|}
comment|/**      * due to a bug, feature, or just plain oddity of JAXB, it isn't good enough      * to just check the form of an element and of its schema. If schema 'a'      * (default unqualified) has a complex type with an element with a ref= to      * schema (b) (default unqualified), JAXB seems to expect to see a      * qualifier, anyway.<br/> So, if the element is local to a complex type,      * all we care about is the default element form of the schema and the local      * form of the element.<br/> If, on the other hand, the element is global,      * we might need to compare namespaces.<br/>      *       * @param element the element.      * @param global if this element is a global element (complex type ref= to      *                it, or in a part)      * @param localSchema the schema of the complex type containing the      *                reference, only used for the 'odd case'.      * @param elementSchema the schema for the element.      * @return if the element needs to be qualified.      */
specifier|public
specifier|static
name|boolean
name|isElementQualified
parameter_list|(
name|XmlSchemaElement
name|element
parameter_list|,
name|boolean
name|global
parameter_list|,
name|XmlSchema
name|localSchema
parameter_list|,
name|XmlSchema
name|elementSchema
parameter_list|)
block|{
if|if
condition|(
name|element
operator|.
name|getQName
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"getSchemaQualifier on anonymous element."
argument_list|)
throw|;
block|}
if|if
condition|(
name|element
operator|.
name|getRefName
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"getSchemaQualified on the 'from' side of ref=."
argument_list|)
throw|;
block|}
if|if
condition|(
name|global
condition|)
block|{
return|return
name|isElementNameQualified
argument_list|(
name|element
argument_list|,
name|elementSchema
argument_list|)
operator|||
operator|(
name|localSchema
operator|!=
literal|null
operator|&&
operator|!
operator|(
name|element
operator|.
name|getQName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
operator|.
name|equals
argument_list|(
name|localSchema
operator|.
name|getTargetNamespace
argument_list|()
argument_list|)
operator|)
operator|)
return|;
block|}
else|else
block|{
return|return
name|isElementNameQualified
argument_list|(
name|element
argument_list|,
name|elementSchema
argument_list|)
return|;
block|}
block|}
comment|/**      * due to a bug, feature, or just plain oddity of JAXB, it isn't good enough      * to just check the form of an element and of its schema. If schema 'a'      * (default unqualified) has a complex type with an element with a ref= to      * schema (b) (default unqualified), JAXB seems to expect to see a      * qualifier, anyway.<br/> So, if the element is local to a complex type,      * all we care about is the default element form of the schema and the local      * form of the element.<br/> If, on the other hand, the element is global,      * we might need to compare namespaces.<br/>      *       * @param attribute the attribute      * @param global if this element is a global element (complex type ref= to      *                it, or in a part)      * @param localSchema the schema of the complex type containing the      *                reference, only used for the 'odd case'.      * @param elementSchema the schema for the element.      * @return if the element needs to be qualified.      */
specifier|public
specifier|static
name|boolean
name|isAttributeQualified
parameter_list|(
name|XmlSchemaAttribute
name|attribute
parameter_list|,
name|boolean
name|global
parameter_list|,
name|XmlSchema
name|localSchema
parameter_list|,
name|XmlSchema
name|attributeSchema
parameter_list|)
block|{
if|if
condition|(
name|attribute
operator|.
name|getQName
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"getSchemaQualifier on anonymous element."
argument_list|)
throw|;
block|}
if|if
condition|(
name|attribute
operator|.
name|getRefName
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"getSchemaQualified on the 'from' side of ref=."
argument_list|)
throw|;
block|}
if|if
condition|(
name|global
condition|)
block|{
return|return
name|isAttributeNameQualified
argument_list|(
name|attribute
argument_list|,
name|attributeSchema
argument_list|)
operator|||
operator|(
name|localSchema
operator|!=
literal|null
operator|&&
operator|!
operator|(
name|attribute
operator|.
name|getQName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
operator|.
name|equals
argument_list|(
name|localSchema
operator|.
name|getTargetNamespace
argument_list|()
argument_list|)
operator|)
operator|)
return|;
block|}
else|else
block|{
return|return
name|isAttributeNameQualified
argument_list|(
name|attribute
argument_list|,
name|attributeSchema
argument_list|)
return|;
block|}
block|}
comment|/**      * If the object is an element or an any, return the particle. If it's not a particle, or it's a group,      * throw. We're not ready for groups yet.      * @param object      * @return      */
specifier|public
specifier|static
name|XmlSchemaParticle
name|getObjectParticle
parameter_list|(
name|XmlSchemaObject
name|object
parameter_list|,
name|QName
name|contextName
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|object
operator|instanceof
name|XmlSchemaParticle
operator|)
condition|)
block|{
name|XmlSchemaUtils
operator|.
name|unsupportedConstruct
argument_list|(
literal|"NON_PARTICLE_CHILD"
argument_list|,
name|object
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|contextName
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
operator|(
name|object
operator|instanceof
name|XmlSchemaElement
operator|)
operator|&&
operator|!
operator|(
name|object
operator|instanceof
name|XmlSchemaAny
operator|)
condition|)
block|{
name|XmlSchemaUtils
operator|.
name|unsupportedConstruct
argument_list|(
literal|"GROUP_CHILD"
argument_list|,
name|object
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|contextName
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|XmlSchemaParticle
operator|)
name|object
return|;
block|}
comment|/**      * If the object is an attribute or an anyAttribute,       * return the 'Annotated'. If it's not one of those, or it's a group,      * throw. We're not ready for groups yet.      * @param object      * @return      */
specifier|public
specifier|static
name|XmlSchemaAnnotated
name|getObjectAnnotated
parameter_list|(
name|XmlSchemaObject
name|object
parameter_list|,
name|QName
name|contextName
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|object
operator|instanceof
name|XmlSchemaAnnotated
operator|)
condition|)
block|{
name|XmlSchemaUtils
operator|.
name|unsupportedConstruct
argument_list|(
literal|"NON_ANNOTATED_ATTRIBUTE"
argument_list|,
name|object
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|contextName
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
operator|(
name|object
operator|instanceof
name|XmlSchemaAttribute
operator|)
operator|&&
operator|!
operator|(
name|object
operator|instanceof
name|XmlSchemaAnyAttribute
operator|)
condition|)
block|{
name|XmlSchemaUtils
operator|.
name|unsupportedConstruct
argument_list|(
literal|"EXOTIC_ATTRIBUTE"
argument_list|,
name|object
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|contextName
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|XmlSchemaAnnotated
operator|)
name|object
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isParticleArray
parameter_list|(
name|XmlSchemaParticle
name|particle
parameter_list|)
block|{
return|return
name|particle
operator|.
name|getMaxOccurs
argument_list|()
operator|>
literal|1
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isParticleOptional
parameter_list|(
name|XmlSchemaParticle
name|particle
parameter_list|)
block|{
return|return
name|particle
operator|.
name|getMinOccurs
argument_list|()
operator|==
literal|0
operator|&&
name|particle
operator|.
name|getMaxOccurs
argument_list|()
operator|==
literal|1
return|;
block|}
specifier|public
specifier|static
name|XmlSchemaElement
name|getReferredElement
parameter_list|(
name|XmlSchemaElement
name|element
parameter_list|,
name|SchemaCollection
name|xmlSchemaCollection
parameter_list|)
block|{
if|if
condition|(
name|element
operator|.
name|getRefName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|XmlSchemaElement
name|refElement
init|=
name|xmlSchemaCollection
operator|.
name|getElementByQName
argument_list|(
name|element
operator|.
name|getRefName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|refElement
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Dangling reference"
argument_list|)
throw|;
block|}
return|return
name|refElement
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

