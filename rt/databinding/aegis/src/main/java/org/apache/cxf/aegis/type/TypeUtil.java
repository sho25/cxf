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
name|Array
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|GenericArrayType
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|ParameterizedType
import|;
end_import

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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|aegis
operator|.
name|AegisContext
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
name|util
operator|.
name|NamespaceHelper
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
name|SOAPConstants
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

begin_comment
comment|/**  * Static methods/constants for Aegis.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|TypeUtil
block|{
specifier|public
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|TypeUtil
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|TypeUtil
parameter_list|()
block|{
comment|//utility class
block|}
specifier|public
specifier|static
name|AegisType
name|getReadType
parameter_list|(
name|XMLStreamReader
name|xsr
parameter_list|,
name|AegisContext
name|context
parameter_list|,
name|AegisType
name|baseType
parameter_list|)
block|{
if|if
condition|(
operator|!
name|context
operator|.
name|isReadXsiTypes
argument_list|()
condition|)
block|{
if|if
condition|(
name|baseType
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"xsi:type reading disabled, and no type available for "
operator|+
name|xsr
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|baseType
return|;
block|}
name|String
name|overrideType
init|=
name|xsr
operator|.
name|getAttributeValue
argument_list|(
name|SOAPConstants
operator|.
name|XSI_NS
argument_list|,
literal|"type"
argument_list|)
decl_stmt|;
if|if
condition|(
name|overrideType
operator|!=
literal|null
condition|)
block|{
name|QName
name|overrideName
init|=
name|NamespaceHelper
operator|.
name|createQName
argument_list|(
name|xsr
operator|.
name|getNamespaceContext
argument_list|()
argument_list|,
name|overrideType
argument_list|)
decl_stmt|;
if|if
condition|(
name|baseType
operator|==
literal|null
operator|||
operator|!
name|overrideName
operator|.
name|equals
argument_list|(
name|baseType
operator|.
name|getSchemaType
argument_list|()
argument_list|)
condition|)
block|{
name|AegisType
name|improvedType
init|=
literal|null
decl_stmt|;
name|TypeMapping
name|tm
decl_stmt|;
if|if
condition|(
name|baseType
operator|!=
literal|null
condition|)
block|{
name|tm
operator|=
name|baseType
operator|.
name|getTypeMapping
argument_list|()
expr_stmt|;
name|improvedType
operator|=
name|tm
operator|.
name|getType
argument_list|(
name|overrideName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|improvedType
operator|==
literal|null
condition|)
block|{
name|improvedType
operator|=
name|context
operator|.
name|getRootType
argument_list|(
name|overrideName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|improvedType
operator|!=
literal|null
condition|)
block|{
return|return
name|improvedType
return|;
block|}
block|}
if|if
condition|(
name|baseType
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|finest
argument_list|(
literal|"xsi:type=\""
operator|+
name|overrideName
operator|+
literal|"\" was specified, but no corresponding AegisType was registered; defaulting to "
operator|+
name|baseType
operator|.
name|getSchemaType
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|baseType
return|;
block|}
else|else
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"xsi:type=\""
operator|+
name|overrideName
operator|+
literal|"\" was specified, but no corresponding AegisType was registered; no default."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|baseType
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"xsi:type absent, and no type available for "
operator|+
name|xsr
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|baseType
return|;
block|}
block|}
comment|/**      * getReadType cannot just look up the xsi:type in the mapping. This function must be      * called instead at the root where there is no initial mapping to start from, as from      * a part or an element of some containing item.      * @param xsr      * @param context      * @return      */
specifier|public
specifier|static
name|AegisType
name|getReadTypeStandalone
parameter_list|(
name|XMLStreamReader
name|xsr
parameter_list|,
name|AegisContext
name|context
parameter_list|,
name|AegisType
name|baseType
parameter_list|)
block|{
if|if
condition|(
name|baseType
operator|!=
literal|null
condition|)
block|{
return|return
name|getReadType
argument_list|(
name|xsr
argument_list|,
name|context
argument_list|,
name|baseType
argument_list|)
return|;
block|}
if|if
condition|(
operator|!
name|context
operator|.
name|isReadXsiTypes
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"xsi:type reading disabled, and no type available for "
operator|+
name|xsr
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|String
name|typeNameString
init|=
name|xsr
operator|.
name|getAttributeValue
argument_list|(
name|SOAPConstants
operator|.
name|XSI_NS
argument_list|,
literal|"type"
argument_list|)
decl_stmt|;
if|if
condition|(
name|typeNameString
operator|!=
literal|null
condition|)
block|{
name|QName
name|schemaTypeName
init|=
name|NamespaceHelper
operator|.
name|createQName
argument_list|(
name|xsr
operator|.
name|getNamespaceContext
argument_list|()
argument_list|,
name|typeNameString
argument_list|)
decl_stmt|;
name|TypeMapping
name|tm
decl_stmt|;
name|tm
operator|=
name|context
operator|.
name|getTypeMapping
argument_list|()
expr_stmt|;
name|AegisType
name|type
init|=
name|tm
operator|.
name|getType
argument_list|(
name|schemaTypeName
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
name|type
operator|=
name|context
operator|.
name|getRootType
argument_list|(
name|schemaTypeName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
return|return
name|type
return|;
block|}
name|LOG
operator|.
name|warning
argument_list|(
literal|"xsi:type=\""
operator|+
name|schemaTypeName
operator|+
literal|"\" was specified, but no corresponding AegisType was registered; no default."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|LOG
operator|.
name|warning
argument_list|(
literal|"xsi:type was not specified for top-level element "
operator|+
name|xsr
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|AegisType
name|getWriteType
parameter_list|(
name|AegisContext
name|globalContext
parameter_list|,
name|Object
name|value
parameter_list|,
name|AegisType
name|type
parameter_list|)
block|{
if|if
condition|(
name|value
operator|!=
literal|null
operator|&&
name|type
operator|!=
literal|null
operator|&&
name|type
operator|.
name|getTypeClass
argument_list|()
operator|!=
name|value
operator|.
name|getClass
argument_list|()
condition|)
block|{
name|AegisType
name|overrideType
init|=
name|globalContext
operator|.
name|getRootType
argument_list|(
name|value
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|overrideType
operator|!=
literal|null
condition|)
block|{
return|return
name|overrideType
return|;
block|}
block|}
return|return
name|type
return|;
block|}
specifier|public
specifier|static
name|AegisType
name|getWriteTypeStandalone
parameter_list|(
name|AegisContext
name|globalContext
parameter_list|,
name|Object
name|value
parameter_list|,
name|AegisType
name|type
parameter_list|)
block|{
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
return|return
name|getWriteType
argument_list|(
name|globalContext
argument_list|,
name|value
argument_list|,
name|type
argument_list|)
return|;
block|}
name|TypeMapping
name|tm
decl_stmt|;
name|tm
operator|=
name|globalContext
operator|.
name|getTypeMapping
argument_list|()
expr_stmt|;
comment|// don't use this for null!
name|type
operator|=
name|tm
operator|.
name|getType
argument_list|(
name|value
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|type
return|;
block|}
comment|/**      * Allow writing of collections when the type of the collection object is known via      * an {@link java.lang.reflect.Type} object.      * @param globalContext the context      * @param value the object to write.      * @param reflectType the type to use in writing the object.      * @return      */
specifier|public
specifier|static
name|AegisType
name|getWriteTypeStandalone
parameter_list|(
name|AegisContext
name|globalContext
parameter_list|,
name|Object
name|value
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
name|reflectType
parameter_list|)
block|{
if|if
condition|(
name|reflectType
operator|==
literal|null
condition|)
block|{
return|return
name|getWriteTypeStandalone
argument_list|(
name|globalContext
argument_list|,
name|value
argument_list|,
operator|(
name|AegisType
operator|)
literal|null
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|globalContext
operator|.
name|getTypeMapping
argument_list|()
operator|.
name|getTypeCreator
argument_list|()
operator|.
name|createType
argument_list|(
name|reflectType
argument_list|)
return|;
block|}
block|}
specifier|public
specifier|static
name|void
name|setAttributeAttributes
parameter_list|(
name|QName
name|name
parameter_list|,
name|AegisType
name|type
parameter_list|,
name|XmlSchema
name|root
parameter_list|)
block|{
name|String
name|ns
init|=
name|type
operator|.
name|getSchemaType
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
name|XmlSchemaUtils
operator|.
name|addImportIfNeeded
argument_list|(
name|root
argument_list|,
name|ns
argument_list|)
expr_stmt|;
block|}
comment|/**      * Utility function to cast a Type to a Class. This throws an unchecked exception if the Type is      * not a Class. The idea here is that these Type references should have been checked for       * reasonableness before the point of calls to this function.      * @param type Reflection type.      * @param throwForNonClass whether to throw (true) or return null (false) if the Type      * is not a class.      * @return the Class      */
specifier|public
specifier|static
name|Class
argument_list|<
name|?
argument_list|>
name|getTypeClass
parameter_list|(
name|Type
name|type
parameter_list|,
name|boolean
name|throwForNonClass
parameter_list|)
block|{
if|if
condition|(
name|type
operator|instanceof
name|Class
condition|)
block|{
return|return
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|type
return|;
block|}
elseif|else
if|if
condition|(
name|throwForNonClass
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Attempt to derive Class from reflection Type "
operator|+
name|type
argument_list|)
throw|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Insist that a Type is a parameterized type of one parameter.      * This is used to decompose Holders, for example.      * @param type the type      * @return the parameter, or null if the type is not what we want.      */
specifier|public
specifier|static
name|Type
name|getSingleTypeParameter
parameter_list|(
name|Type
name|type
parameter_list|)
block|{
return|return
name|getSingleTypeParameter
argument_list|(
name|type
argument_list|,
literal|0
argument_list|)
return|;
block|}
comment|/**      * Insist that a Type is a parameterized type of one parameter.      * This is used to decompose Holders, for example.      * @param type the type      * @param index which parameter      * @return the parameter, or null if the type is not what we want.      */
specifier|public
specifier|static
name|Type
name|getSingleTypeParameter
parameter_list|(
name|Type
name|type
parameter_list|,
name|int
name|index
parameter_list|)
block|{
if|if
condition|(
name|type
operator|instanceof
name|ParameterizedType
condition|)
block|{
name|ParameterizedType
name|pType
init|=
operator|(
name|ParameterizedType
operator|)
name|type
decl_stmt|;
name|Type
index|[]
name|params
init|=
name|pType
operator|.
name|getActualTypeArguments
argument_list|()
decl_stmt|;
if|if
condition|(
name|params
operator|.
name|length
operator|>
name|index
condition|)
block|{
return|return
name|params
index|[
name|index
index|]
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * If a Type is a class, return it as a class.      * If it is a ParameterizedType, return the raw type as a class.      * Otherwise return null.      * @param type      * @return      */
specifier|public
specifier|static
name|Class
argument_list|<
name|?
argument_list|>
name|getTypeRelatedClass
parameter_list|(
name|Type
name|type
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|directClass
init|=
name|getTypeClass
argument_list|(
name|type
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|directClass
operator|!=
literal|null
condition|)
block|{
return|return
name|directClass
return|;
block|}
if|if
condition|(
name|type
operator|instanceof
name|ParameterizedType
condition|)
block|{
name|ParameterizedType
name|pType
init|=
operator|(
name|ParameterizedType
operator|)
name|type
decl_stmt|;
return|return
name|getTypeRelatedClass
argument_list|(
name|pType
operator|.
name|getRawType
argument_list|()
argument_list|)
return|;
block|}
if|if
condition|(
name|type
operator|instanceof
name|GenericArrayType
condition|)
block|{
name|GenericArrayType
name|gat
init|=
operator|(
name|GenericArrayType
operator|)
name|type
decl_stmt|;
name|Type
name|compType
init|=
name|gat
operator|.
name|getGenericComponentType
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|arrayBaseType
init|=
name|getTypeRelatedClass
argument_list|(
name|compType
argument_list|)
decl_stmt|;
comment|// believe it or not, this seems to be the only way to get the
comment|// Class object for an array of primitive type.
name|Object
name|instance
init|=
name|Array
operator|.
name|newInstance
argument_list|(
name|arrayBaseType
argument_list|,
literal|0
argument_list|)
decl_stmt|;
return|return
name|instance
operator|.
name|getClass
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

