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
name|aegis
operator|.
name|type
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
name|Type
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
name|apache
operator|.
name|cxf
operator|.
name|aegis
operator|.
name|Context
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
name|aegis
operator|.
name|DatabindingException
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
name|aegis
operator|.
name|xml
operator|.
name|MessageReader
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
name|aegis
operator|.
name|xml
operator|.
name|MessageWriter
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

begin_comment
comment|/**  * Aegis abstraction for a object. Types are responsible for reading and writing the contents  * of objects, but not, almost always, their own outermost XML element.  *  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AegisType
block|{
specifier|protected
name|Type
name|typeClass
decl_stmt|;
specifier|private
name|QName
name|schemaType
decl_stmt|;
specifier|private
name|TypeMapping
name|typeMapping
decl_stmt|;
specifier|private
name|boolean
name|abstrct
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|nillable
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|writeOuter
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|flatArray
decl_stmt|;
specifier|public
name|AegisType
parameter_list|()
block|{     }
comment|/**      * Read in the XML fragment and create an object.      *      * @param reader      * @param context      * @return      * @throws DatabindingException      */
specifier|public
specifier|abstract
name|Object
name|readObject
parameter_list|(
name|MessageReader
name|reader
parameter_list|,
name|Context
name|context
parameter_list|)
throws|throws
name|DatabindingException
function_decl|;
comment|/**      * Writes the object to the MessageWriter.      *      * @param object      * @param writer      * @param context      * @throws DatabindingException      */
specifier|public
specifier|abstract
name|void
name|writeObject
parameter_list|(
name|Object
name|object
parameter_list|,
name|MessageWriter
name|writer
parameter_list|,
name|Context
name|context
parameter_list|)
throws|throws
name|DatabindingException
function_decl|;
comment|/**      * If this type should correspond to a global, named, schema type, here is where the      * type object adds it to the schema.      * @param root root of the XSD document.      */
specifier|public
name|void
name|writeSchema
parameter_list|(
name|XmlSchema
name|root
parameter_list|)
block|{     }
comment|/**      * If the type object merely wants to contribute attributes to the      * xsd:element element, it can implement this.      * @param schemaElement      */
specifier|public
name|void
name|addToSchemaElement
parameter_list|(
name|XmlSchemaElement
name|schemaElement
parameter_list|)
block|{     }
comment|/**      * @return Returns the typeMapping.      */
specifier|public
name|TypeMapping
name|getTypeMapping
parameter_list|()
block|{
return|return
name|typeMapping
return|;
block|}
comment|/**      * @param typeMapping The typeMapping to set.      */
specifier|public
name|void
name|setTypeMapping
parameter_list|(
name|TypeMapping
name|typeMapping
parameter_list|)
block|{
name|this
operator|.
name|typeMapping
operator|=
name|typeMapping
expr_stmt|;
block|}
comment|/**      * @return Returns the java type as a Class.      * For a generic, return the raw type. For something      * truly exotic, return null.      */
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getTypeClass
parameter_list|()
block|{
return|return
name|TypeUtil
operator|.
name|getTypeRelatedClass
argument_list|(
name|typeClass
argument_list|)
return|;
block|}
comment|/**      * @return Return the Java type.      */
specifier|public
name|Type
name|getType
parameter_list|()
block|{
return|return
name|typeClass
return|;
block|}
comment|/**      * @param typeClass The typeClass to set.      */
specifier|public
name|void
name|setTypeClass
parameter_list|(
name|Type
name|typeClass
parameter_list|)
block|{
name|this
operator|.
name|typeClass
operator|=
name|typeClass
expr_stmt|;
if|if
condition|(
name|typeClass
operator|instanceof
name|Class
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|plainClass
init|=
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|typeClass
decl_stmt|;
if|if
condition|(
name|plainClass
operator|.
name|isPrimitive
argument_list|()
condition|)
block|{
name|setNillable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * @return True if a complex type schema must be written.      */
specifier|public
name|boolean
name|isComplex
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|isAbstract
parameter_list|()
block|{
return|return
name|abstrct
return|;
block|}
specifier|public
name|void
name|setAbstract
parameter_list|(
name|boolean
name|ab
parameter_list|)
block|{
name|this
operator|.
name|abstrct
operator|=
name|ab
expr_stmt|;
block|}
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
name|void
name|setNillable
parameter_list|(
name|boolean
name|nillable
parameter_list|)
block|{
name|this
operator|.
name|nillable
operator|=
name|nillable
expr_stmt|;
block|}
comment|/**      * Return a set of AegisType dependencies. Returns null if this type has no      * dependencies.      *      * @return Set of<code>AegisType</code> dependencies      */
specifier|public
name|Set
argument_list|<
name|AegisType
argument_list|>
name|getDependencies
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/**      * @see java.lang.Object#equals(java.lang.Object)      */
annotation|@
name|Override
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
operator|==
name|this
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|obj
operator|instanceof
name|AegisType
condition|)
block|{
name|AegisType
name|type
init|=
operator|(
name|AegisType
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|type
operator|.
name|getSchemaType
argument_list|()
operator|.
name|equals
argument_list|(
name|getSchemaType
argument_list|()
argument_list|)
operator|&&
name|type
operator|.
name|getTypeClass
argument_list|()
operator|.
name|equals
argument_list|(
name|getTypeClass
argument_list|()
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
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|hashcode
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|getTypeClass
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|hashcode
operator|^=
name|getTypeClass
argument_list|()
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|getSchemaType
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|hashcode
operator|^=
name|getSchemaType
argument_list|()
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
return|return
name|hashcode
return|;
block|}
comment|/**      * @return Get the schema type.      */
specifier|public
name|QName
name|getSchemaType
parameter_list|()
block|{
return|return
name|schemaType
return|;
block|}
comment|/**      * @param name The qName to set.      */
specifier|public
name|void
name|setSchemaType
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
name|schemaType
operator|=
name|name
expr_stmt|;
block|}
comment|/**      * Defaults to true. False for types that disappear entirely when null,      * even when nillable.      * @return whether to write xsi:nil for null values.      */
specifier|public
name|boolean
name|isWriteOuter
parameter_list|()
block|{
return|return
name|writeOuter
return|;
block|}
specifier|public
name|void
name|setWriteOuter
parameter_list|(
name|boolean
name|writeOuter
parameter_list|)
block|{
name|this
operator|.
name|writeOuter
operator|=
name|writeOuter
expr_stmt|;
block|}
specifier|public
name|boolean
name|usesXmime
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
comment|/**      * True if this type requires the import of the aegisTypes schema.      * @return      */
specifier|public
name|boolean
name|usesUtilityTypes
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|hasMinOccurs
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|hasMaxOccurs
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|long
name|getMinOccurs
parameter_list|()
block|{
return|return
literal|0
return|;
comment|// not valid in general
block|}
specifier|public
name|long
name|getMaxOccurs
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"[class="
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|c
init|=
name|getTypeClass
argument_list|()
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|c
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"<generic or null>"
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|",\nQName="
argument_list|)
expr_stmt|;
name|QName
name|q
init|=
name|getSchemaType
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
operator|(
name|q
operator|==
literal|null
operator|)
condition|?
literal|"<null>"
else|:
name|q
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isFlatArray
parameter_list|()
block|{
return|return
name|flatArray
return|;
block|}
specifier|public
name|void
name|setFlatArray
parameter_list|(
name|boolean
name|flatArray
parameter_list|)
block|{
name|this
operator|.
name|flatArray
operator|=
name|flatArray
expr_stmt|;
block|}
block|}
end_class

end_unit

