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
name|beans
operator|.
name|PropertyDescriptor
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
name|Field
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
name|Map
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
name|ws
operator|.
name|Holder
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
name|type
operator|.
name|basic
operator|.
name|ArrayType
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
name|type
operator|.
name|basic
operator|.
name|ObjectType
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
name|type
operator|.
name|collection
operator|.
name|CollectionType
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
name|type
operator|.
name|collection
operator|.
name|MapType
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
name|aegis
operator|.
name|util
operator|.
name|ServiceUtils
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
name|XMLSchemaQNames
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

begin_comment
comment|/**  * @author Hani Suleiman Date: Jun 14, 2005 Time: 11:59:57 PM  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractTypeCreator
implements|implements
name|TypeCreator
block|{
specifier|protected
name|TypeMapping
name|tm
decl_stmt|;
specifier|protected
name|AbstractTypeCreator
name|nextCreator
decl_stmt|;
specifier|private
name|TypeCreationOptions
name|typeConfiguration
decl_stmt|;
specifier|private
name|TypeCreator
name|parent
decl_stmt|;
specifier|public
name|TypeMapping
name|getTypeMapping
parameter_list|()
block|{
return|return
name|tm
return|;
block|}
specifier|public
name|TypeCreator
name|getTopCreator
parameter_list|()
block|{
name|TypeCreator
name|top
init|=
name|this
decl_stmt|;
name|TypeCreator
name|next
init|=
name|top
decl_stmt|;
while|while
condition|(
name|next
operator|!=
literal|null
condition|)
block|{
name|top
operator|=
name|next
expr_stmt|;
name|next
operator|=
name|top
operator|.
name|getParent
argument_list|()
expr_stmt|;
block|}
return|return
name|top
return|;
block|}
specifier|public
name|TypeCreator
name|getParent
parameter_list|()
block|{
return|return
name|parent
return|;
block|}
specifier|public
name|void
name|setParent
parameter_list|(
name|TypeCreator
name|parent
parameter_list|)
block|{
name|this
operator|.
name|parent
operator|=
name|parent
expr_stmt|;
block|}
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
name|tm
operator|=
name|typeMapping
expr_stmt|;
if|if
condition|(
name|nextCreator
operator|!=
literal|null
condition|)
block|{
name|nextCreator
operator|.
name|setTypeMapping
argument_list|(
name|tm
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setNextCreator
parameter_list|(
name|AbstractTypeCreator
name|creator
parameter_list|)
block|{
name|this
operator|.
name|nextCreator
operator|=
name|creator
expr_stmt|;
name|nextCreator
operator|.
name|parent
operator|=
name|this
expr_stmt|;
block|}
specifier|public
name|TypeClassInfo
name|createClassInfo
parameter_list|(
name|Field
name|f
parameter_list|)
block|{
name|TypeClassInfo
name|info
init|=
name|createBasicClassInfo
argument_list|(
name|f
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
name|info
operator|.
name|setDescription
argument_list|(
literal|"field "
operator|+
name|f
operator|.
name|getName
argument_list|()
operator|+
literal|" in  "
operator|+
name|f
operator|.
name|getDeclaringClass
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|info
return|;
block|}
specifier|public
name|TypeClassInfo
name|createBasicClassInfo
parameter_list|(
name|Class
name|typeClass
parameter_list|)
block|{
name|TypeClassInfo
name|info
init|=
operator|new
name|TypeClassInfo
argument_list|()
decl_stmt|;
name|info
operator|.
name|setDescription
argument_list|(
literal|"class '"
operator|+
name|typeClass
operator|.
name|getName
argument_list|()
operator|+
literal|'\''
argument_list|)
expr_stmt|;
name|info
operator|.
name|setTypeClass
argument_list|(
name|typeClass
argument_list|)
expr_stmt|;
return|return
name|info
return|;
block|}
specifier|public
name|Type
name|createTypeForClass
parameter_list|(
name|TypeClassInfo
name|info
parameter_list|)
block|{
name|Class
name|javaType
init|=
name|info
operator|.
name|getTypeClass
argument_list|()
decl_stmt|;
name|Type
name|result
init|=
literal|null
decl_stmt|;
name|boolean
name|newType
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|info
operator|.
name|getType
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
name|createUserType
argument_list|(
name|info
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isArray
argument_list|(
name|javaType
argument_list|)
condition|)
block|{
name|result
operator|=
name|createArrayType
argument_list|(
name|info
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isMap
argument_list|(
name|javaType
argument_list|)
condition|)
block|{
name|result
operator|=
name|createMapType
argument_list|(
name|info
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isHolder
argument_list|(
name|javaType
argument_list|)
condition|)
block|{
name|result
operator|=
name|createHolderType
argument_list|(
name|info
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isCollection
argument_list|(
name|javaType
argument_list|)
condition|)
block|{
name|result
operator|=
name|createCollectionType
argument_list|(
name|info
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isEnum
argument_list|(
name|javaType
argument_list|)
condition|)
block|{
name|result
operator|=
name|createEnumType
argument_list|(
name|info
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Type
name|type
init|=
name|getTypeMapping
argument_list|()
operator|.
name|getType
argument_list|(
name|javaType
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
name|createDefaultType
argument_list|(
name|info
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|newType
operator|=
literal|false
expr_stmt|;
block|}
name|result
operator|=
name|type
expr_stmt|;
block|}
if|if
condition|(
name|newType
operator|&&
operator|!
name|getConfiguration
argument_list|()
operator|.
name|isDefaultNillable
argument_list|()
condition|)
block|{
name|result
operator|.
name|setNillable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|protected
name|boolean
name|isHolder
parameter_list|(
name|Class
name|javaType
parameter_list|)
block|{
return|return
name|javaType
operator|.
name|equals
argument_list|(
name|Holder
operator|.
name|class
argument_list|)
return|;
block|}
specifier|protected
name|Type
name|createHolderType
parameter_list|(
name|TypeClassInfo
name|info
parameter_list|)
block|{
if|if
condition|(
name|info
operator|.
name|getGenericType
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"To use holder types "
operator|+
literal|"you must have an XML descriptor declaring the component type."
argument_list|)
throw|;
block|}
name|Class
name|heldCls
init|=
operator|(
name|Class
operator|)
name|info
operator|.
name|getGenericType
argument_list|()
decl_stmt|;
name|info
operator|.
name|setTypeClass
argument_list|(
name|heldCls
argument_list|)
expr_stmt|;
return|return
name|createType
argument_list|(
name|heldCls
argument_list|)
return|;
block|}
specifier|protected
name|boolean
name|isArray
parameter_list|(
name|Class
name|javaType
parameter_list|)
block|{
return|return
name|javaType
operator|.
name|isArray
argument_list|()
operator|&&
operator|!
name|javaType
operator|.
name|equals
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
return|;
block|}
specifier|protected
name|Type
name|createUserType
parameter_list|(
name|TypeClassInfo
name|info
parameter_list|)
block|{
try|try
block|{
name|Type
name|type
init|=
operator|(
name|Type
operator|)
name|info
operator|.
name|getType
argument_list|()
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|QName
name|name
init|=
name|info
operator|.
name|getTypeName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|name
operator|=
name|createQName
argument_list|(
name|info
operator|.
name|getTypeClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|type
operator|.
name|setSchemaType
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|type
operator|.
name|setTypeClass
argument_list|(
name|info
operator|.
name|getTypeClass
argument_list|()
argument_list|)
expr_stmt|;
name|type
operator|.
name|setTypeMapping
argument_list|(
name|getTypeMapping
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|type
return|;
block|}
catch|catch
parameter_list|(
name|InstantiationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Couldn't instantiate type classs "
operator|+
name|info
operator|.
name|getType
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Couldn't access type classs "
operator|+
name|info
operator|.
name|getType
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|Type
name|createArrayType
parameter_list|(
name|TypeClassInfo
name|info
parameter_list|)
block|{
name|ArrayType
name|type
init|=
operator|new
name|ArrayType
argument_list|()
decl_stmt|;
name|type
operator|.
name|setTypeMapping
argument_list|(
name|getTypeMapping
argument_list|()
argument_list|)
expr_stmt|;
name|type
operator|.
name|setTypeClass
argument_list|(
name|info
operator|.
name|getTypeClass
argument_list|()
argument_list|)
expr_stmt|;
name|type
operator|.
name|setSchemaType
argument_list|(
name|createCollectionQName
argument_list|(
name|info
argument_list|,
name|type
operator|.
name|getComponentType
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|info
operator|.
name|getMinOccurs
argument_list|()
operator|!=
operator|-
literal|1
condition|)
block|{
name|type
operator|.
name|setMinOccurs
argument_list|(
name|info
operator|.
name|getMinOccurs
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|type
operator|.
name|setMinOccurs
argument_list|(
name|typeConfiguration
operator|.
name|getDefaultMinOccurs
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|info
operator|.
name|getMaxOccurs
argument_list|()
operator|!=
operator|-
literal|1
condition|)
block|{
name|type
operator|.
name|setMaxOccurs
argument_list|(
name|info
operator|.
name|getMaxOccurs
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|type
operator|.
name|setFlat
argument_list|(
name|info
operator|.
name|isFlat
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|type
return|;
block|}
specifier|protected
name|QName
name|createQName
parameter_list|(
name|Class
name|javaType
parameter_list|)
block|{
name|String
name|clsName
init|=
name|javaType
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|ns
init|=
name|NamespaceHelper
operator|.
name|makeNamespaceFromClassName
argument_list|(
name|clsName
argument_list|,
literal|"http"
argument_list|)
decl_stmt|;
name|String
name|localName
init|=
name|ServiceUtils
operator|.
name|makeServiceNameFromClassName
argument_list|(
name|javaType
argument_list|)
decl_stmt|;
return|return
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
name|localName
argument_list|)
return|;
block|}
specifier|protected
name|boolean
name|isCollection
parameter_list|(
name|Class
name|javaType
parameter_list|)
block|{
return|return
name|Collection
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|javaType
argument_list|)
return|;
block|}
specifier|protected
name|Type
name|createCollectionTypeFromGeneric
parameter_list|(
name|TypeClassInfo
name|info
parameter_list|)
block|{
name|Type
name|component
init|=
name|getOrCreateGenericType
argument_list|(
name|info
argument_list|)
decl_stmt|;
name|CollectionType
name|type
init|=
operator|new
name|CollectionType
argument_list|(
name|component
argument_list|)
decl_stmt|;
name|type
operator|.
name|setTypeMapping
argument_list|(
name|getTypeMapping
argument_list|()
argument_list|)
expr_stmt|;
name|QName
name|name
init|=
name|info
operator|.
name|getTypeName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|name
operator|=
name|createCollectionQName
argument_list|(
name|info
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
name|type
operator|.
name|setSchemaType
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|type
operator|.
name|setTypeClass
argument_list|(
name|info
operator|.
name|getTypeClass
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|info
operator|.
name|getMinOccurs
argument_list|()
operator|!=
operator|-
literal|1
condition|)
block|{
name|type
operator|.
name|setMinOccurs
argument_list|(
name|info
operator|.
name|getMinOccurs
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|info
operator|.
name|getMaxOccurs
argument_list|()
operator|!=
operator|-
literal|1
condition|)
block|{
name|type
operator|.
name|setMaxOccurs
argument_list|(
name|info
operator|.
name|getMaxOccurs
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|type
operator|.
name|setFlat
argument_list|(
name|info
operator|.
name|isFlat
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|type
return|;
block|}
specifier|protected
name|Type
name|getOrCreateGenericType
parameter_list|(
name|TypeClassInfo
name|info
parameter_list|)
block|{
return|return
name|createObjectType
argument_list|()
return|;
block|}
specifier|protected
name|Type
name|getOrCreateMapKeyType
parameter_list|(
name|TypeClassInfo
name|info
parameter_list|)
block|{
return|return
name|nextCreator
operator|.
name|getOrCreateMapKeyType
argument_list|(
name|info
argument_list|)
return|;
block|}
specifier|protected
name|Type
name|createObjectType
parameter_list|()
block|{
name|ObjectType
name|type
init|=
operator|new
name|ObjectType
argument_list|()
decl_stmt|;
name|type
operator|.
name|setSchemaType
argument_list|(
name|XMLSchemaQNames
operator|.
name|XSD_ANY
argument_list|)
expr_stmt|;
name|type
operator|.
name|setTypeClass
argument_list|(
name|Object
operator|.
name|class
argument_list|)
expr_stmt|;
name|type
operator|.
name|setTypeMapping
argument_list|(
name|getTypeMapping
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|type
return|;
block|}
specifier|protected
name|Type
name|getOrCreateMapValueType
parameter_list|(
name|TypeClassInfo
name|info
parameter_list|)
block|{
return|return
name|nextCreator
operator|.
name|getOrCreateMapValueType
argument_list|(
name|info
argument_list|)
return|;
block|}
specifier|protected
name|Type
name|createMapType
parameter_list|(
name|TypeClassInfo
name|info
parameter_list|,
name|Type
name|keyType
parameter_list|,
name|Type
name|valueType
parameter_list|)
block|{
name|QName
name|schemaType
init|=
name|createMapQName
argument_list|(
name|info
argument_list|,
name|keyType
argument_list|,
name|valueType
argument_list|)
decl_stmt|;
name|MapType
name|type
init|=
operator|new
name|MapType
argument_list|(
name|schemaType
argument_list|,
name|keyType
argument_list|,
name|valueType
argument_list|)
decl_stmt|;
name|type
operator|.
name|setTypeMapping
argument_list|(
name|getTypeMapping
argument_list|()
argument_list|)
expr_stmt|;
name|type
operator|.
name|setTypeClass
argument_list|(
name|info
operator|.
name|getTypeClass
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|type
return|;
block|}
specifier|protected
name|Type
name|createMapType
parameter_list|(
name|TypeClassInfo
name|info
parameter_list|)
block|{
name|Type
name|keyType
init|=
name|getOrCreateMapKeyType
argument_list|(
name|info
argument_list|)
decl_stmt|;
name|Type
name|valueType
init|=
name|getOrCreateMapValueType
argument_list|(
name|info
argument_list|)
decl_stmt|;
return|return
name|createMapType
argument_list|(
name|info
argument_list|,
name|keyType
argument_list|,
name|valueType
argument_list|)
return|;
block|}
specifier|protected
name|QName
name|createMapQName
parameter_list|(
name|TypeClassInfo
name|info
parameter_list|,
name|Type
name|keyType
parameter_list|,
name|Type
name|valueType
parameter_list|)
block|{
name|String
name|name
init|=
name|keyType
operator|.
name|getSchemaType
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|'2'
operator|+
name|valueType
operator|.
name|getSchemaType
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|"Map"
decl_stmt|;
comment|// TODO: Get namespace from XML?
return|return
operator|new
name|QName
argument_list|(
name|tm
operator|.
name|getMappingIdentifierURI
argument_list|()
argument_list|,
name|name
argument_list|)
return|;
block|}
specifier|protected
name|boolean
name|isMap
parameter_list|(
name|Class
name|javaType
parameter_list|)
block|{
return|return
name|Map
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|javaType
argument_list|)
return|;
block|}
specifier|public
specifier|abstract
name|TypeClassInfo
name|createClassInfo
parameter_list|(
name|PropertyDescriptor
name|pd
parameter_list|)
function_decl|;
specifier|protected
name|boolean
name|isEnum
parameter_list|(
name|Class
name|javaType
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|Type
name|createEnumType
parameter_list|(
name|TypeClassInfo
name|info
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
specifier|abstract
name|Type
name|createCollectionType
parameter_list|(
name|TypeClassInfo
name|info
parameter_list|)
function_decl|;
specifier|public
specifier|abstract
name|Type
name|createDefaultType
parameter_list|(
name|TypeClassInfo
name|info
parameter_list|)
function_decl|;
specifier|protected
name|QName
name|createCollectionQName
parameter_list|(
name|TypeClassInfo
name|info
parameter_list|,
name|Type
name|type
parameter_list|)
block|{
name|String
name|ns
decl_stmt|;
if|if
condition|(
name|type
operator|.
name|isComplex
argument_list|()
condition|)
block|{
name|ns
operator|=
name|type
operator|.
name|getSchemaType
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|ns
operator|=
name|tm
operator|.
name|getMappingIdentifierURI
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|WSDLConstants
operator|.
name|NS_SCHEMA_XSD
operator|.
name|equals
argument_list|(
name|ns
argument_list|)
condition|)
block|{
name|ns
operator|=
literal|"http://cxf.apache.org/arrays"
expr_stmt|;
block|}
name|String
name|first
init|=
name|type
operator|.
name|getSchemaType
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|String
name|last
init|=
name|type
operator|.
name|getSchemaType
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|String
name|localName
init|=
literal|"ArrayOf"
operator|+
name|first
operator|.
name|toUpperCase
argument_list|()
operator|+
name|last
decl_stmt|;
if|if
condition|(
name|info
operator|.
name|nonDefaultAttributes
argument_list|()
condition|)
block|{
name|localName
operator|+=
literal|"-"
expr_stmt|;
if|if
condition|(
name|info
operator|.
name|getMinOccurs
argument_list|()
operator|>=
literal|0
condition|)
block|{
name|localName
operator|+=
name|info
operator|.
name|minOccurs
expr_stmt|;
block|}
name|localName
operator|+=
literal|"-"
expr_stmt|;
if|if
condition|(
name|info
operator|.
name|getMaxOccurs
argument_list|()
operator|>=
literal|0
condition|)
block|{
name|localName
operator|+=
name|info
operator|.
name|maxOccurs
expr_stmt|;
block|}
if|if
condition|(
name|info
operator|.
name|isFlat
argument_list|()
condition|)
block|{
name|localName
operator|+=
literal|"Flat"
expr_stmt|;
block|}
block|}
return|return
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
name|localName
argument_list|)
return|;
block|}
specifier|public
specifier|abstract
name|TypeClassInfo
name|createClassInfo
parameter_list|(
name|Method
name|m
parameter_list|,
name|int
name|index
parameter_list|)
function_decl|;
comment|/**      * Create a Type for a Method parameter.      *       * @param m the method to create a type for      * @param index The parameter index. If the index is less than zero, the      *            return type is used.      */
specifier|public
name|Type
name|createType
parameter_list|(
name|Method
name|m
parameter_list|,
name|int
name|index
parameter_list|)
block|{
name|TypeClassInfo
name|info
init|=
name|createClassInfo
argument_list|(
name|m
argument_list|,
name|index
argument_list|)
decl_stmt|;
name|info
operator|.
name|setDescription
argument_list|(
operator|(
name|index
operator|==
operator|-
literal|1
condition|?
literal|"return type"
else|:
literal|"parameter "
operator|+
name|index
operator|)
operator|+
literal|" of method "
operator|+
name|m
operator|.
name|getName
argument_list|()
operator|+
literal|" in "
operator|+
name|m
operator|.
name|getDeclaringClass
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|createTypeForClass
argument_list|(
name|info
argument_list|)
return|;
block|}
specifier|public
name|QName
name|getElementName
parameter_list|(
name|Method
name|m
parameter_list|,
name|int
name|index
parameter_list|)
block|{
name|TypeClassInfo
name|info
init|=
name|createClassInfo
argument_list|(
name|m
argument_list|,
name|index
argument_list|)
decl_stmt|;
return|return
name|info
operator|.
name|getMappedName
argument_list|()
return|;
block|}
comment|/**      * Create type information for a PropertyDescriptor.      *       * @param pd the propertydescriptor      */
specifier|public
name|Type
name|createType
parameter_list|(
name|PropertyDescriptor
name|pd
parameter_list|)
block|{
name|TypeClassInfo
name|info
init|=
name|createClassInfo
argument_list|(
name|pd
argument_list|)
decl_stmt|;
name|info
operator|.
name|setDescription
argument_list|(
literal|"property "
operator|+
name|pd
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|createTypeForClass
argument_list|(
name|info
argument_list|)
return|;
block|}
comment|/**      * Create type information for a<code>Field</code>.      *       * @param f the field to create a type from      */
specifier|public
name|Type
name|createType
parameter_list|(
name|Field
name|f
parameter_list|)
block|{
name|TypeClassInfo
name|info
init|=
name|createClassInfo
argument_list|(
name|f
argument_list|)
decl_stmt|;
name|info
operator|.
name|setDescription
argument_list|(
literal|"field "
operator|+
name|f
operator|.
name|getName
argument_list|()
operator|+
literal|" in "
operator|+
name|f
operator|.
name|getDeclaringClass
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|createTypeForClass
argument_list|(
name|info
argument_list|)
return|;
block|}
specifier|public
name|Type
name|createType
parameter_list|(
name|Class
name|clazz
parameter_list|)
block|{
name|TypeClassInfo
name|info
init|=
name|createBasicClassInfo
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
name|info
operator|.
name|setDescription
argument_list|(
name|clazz
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|createTypeForClass
argument_list|(
name|info
argument_list|)
return|;
block|}
specifier|public
name|TypeCreationOptions
name|getConfiguration
parameter_list|()
block|{
return|return
name|typeConfiguration
return|;
block|}
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|TypeCreationOptions
name|tpConfiguration
parameter_list|)
block|{
name|this
operator|.
name|typeConfiguration
operator|=
name|tpConfiguration
expr_stmt|;
block|}
comment|/**      * Object to carry information for a type, such as that from an XML mapping file.       */
specifier|public
specifier|static
class|class
name|TypeClassInfo
block|{
name|Class
name|typeClass
decl_stmt|;
name|Object
index|[]
name|annotations
decl_stmt|;
name|Object
name|genericType
decl_stmt|;
name|Object
name|keyType
decl_stmt|;
name|Object
name|valueType
decl_stmt|;
name|QName
name|mappedName
decl_stmt|;
name|QName
name|typeName
decl_stmt|;
name|Class
name|type
decl_stmt|;
name|String
name|description
decl_stmt|;
name|long
name|minOccurs
init|=
operator|-
literal|1
decl_stmt|;
name|long
name|maxOccurs
init|=
operator|-
literal|1
decl_stmt|;
name|boolean
name|flat
decl_stmt|;
specifier|public
name|boolean
name|nonDefaultAttributes
parameter_list|()
block|{
return|return
name|minOccurs
operator|!=
operator|-
literal|1
operator|||
name|maxOccurs
operator|!=
operator|-
literal|1
operator|||
name|flat
return|;
block|}
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
block|}
specifier|public
name|Object
index|[]
name|getAnnotations
parameter_list|()
block|{
return|return
name|annotations
return|;
block|}
specifier|public
name|void
name|setAnnotations
parameter_list|(
name|Object
index|[]
name|annotations
parameter_list|)
block|{
name|this
operator|.
name|annotations
operator|=
name|annotations
expr_stmt|;
block|}
specifier|public
name|Object
name|getGenericType
parameter_list|()
block|{
return|return
name|genericType
return|;
block|}
specifier|public
name|void
name|setGenericType
parameter_list|(
name|Object
name|genericType
parameter_list|)
block|{
name|this
operator|.
name|genericType
operator|=
name|genericType
expr_stmt|;
block|}
specifier|public
name|Object
name|getKeyType
parameter_list|()
block|{
return|return
name|keyType
return|;
block|}
specifier|public
name|void
name|setKeyType
parameter_list|(
name|Object
name|keyType
parameter_list|)
block|{
name|this
operator|.
name|keyType
operator|=
name|keyType
expr_stmt|;
block|}
specifier|public
name|Class
name|getTypeClass
parameter_list|()
block|{
return|return
name|typeClass
return|;
block|}
specifier|public
name|void
name|setTypeClass
parameter_list|(
name|Class
name|typeClass
parameter_list|)
block|{
name|this
operator|.
name|typeClass
operator|=
name|typeClass
expr_stmt|;
block|}
specifier|public
name|QName
name|getTypeName
parameter_list|()
block|{
return|return
name|typeName
return|;
block|}
specifier|public
name|void
name|setTypeName
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
name|this
operator|.
name|typeName
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|Class
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
name|Class
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
name|QName
name|getMappedName
parameter_list|()
block|{
return|return
name|mappedName
return|;
block|}
specifier|public
name|void
name|setMappedName
parameter_list|(
name|QName
name|mappedName
parameter_list|)
block|{
name|this
operator|.
name|mappedName
operator|=
name|mappedName
expr_stmt|;
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
name|void
name|setMaxOccurs
parameter_list|(
name|long
name|maxOccurs
parameter_list|)
block|{
name|this
operator|.
name|maxOccurs
operator|=
name|maxOccurs
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
name|void
name|setMinOccurs
parameter_list|(
name|long
name|minOccurs
parameter_list|)
block|{
name|this
operator|.
name|minOccurs
operator|=
name|minOccurs
expr_stmt|;
block|}
specifier|public
name|boolean
name|isFlat
parameter_list|()
block|{
return|return
name|flat
return|;
block|}
specifier|public
name|void
name|setFlat
parameter_list|(
name|boolean
name|flat
parameter_list|)
block|{
name|this
operator|.
name|flat
operator|=
name|flat
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"TypeClassInfo "
operator|+
name|getDescription
argument_list|()
return|;
block|}
specifier|public
name|Object
name|getValueType
parameter_list|()
block|{
return|return
name|valueType
return|;
block|}
specifier|public
name|void
name|setValueType
parameter_list|(
name|Object
name|valueType
parameter_list|)
block|{
name|this
operator|.
name|valueType
operator|=
name|valueType
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

