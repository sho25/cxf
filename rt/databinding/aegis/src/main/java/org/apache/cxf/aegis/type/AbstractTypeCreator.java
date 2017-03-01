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
name|lang
operator|.
name|reflect
operator|.
name|TypeVariable
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
name|constants
operator|.
name|Constants
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractTypeCreator
implements|implements
name|TypeCreator
block|{
specifier|public
specifier|static
specifier|final
name|String
name|HTTP_CXF_APACHE_ORG_ARRAYS
init|=
literal|"http://cxf.apache.org/arrays"
decl_stmt|;
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
name|Type
name|type
parameter_list|)
block|{
name|TypeClassInfo
name|info
init|=
operator|new
name|TypeClassInfo
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|typeClass
init|=
name|TypeUtil
operator|.
name|getTypeClass
argument_list|(
name|type
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|typeClass
operator|!=
literal|null
condition|)
block|{
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
literal|"'"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|info
operator|.
name|setDescription
argument_list|(
literal|"type '"
operator|+
name|type
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
name|info
operator|.
name|setType
argument_list|(
name|type
argument_list|)
expr_stmt|;
return|return
name|info
return|;
block|}
specifier|public
name|AegisType
name|createTypeForClass
parameter_list|(
name|TypeClassInfo
name|info
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|javaClass
init|=
name|TypeUtil
operator|.
name|getTypeRelatedClass
argument_list|(
name|info
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
name|AegisType
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
operator|instanceof
name|TypeVariable
condition|)
block|{
comment|//it's the generic type
name|result
operator|=
name|getOrCreateGenericType
argument_list|(
name|info
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|info
operator|.
name|getAegisTypeClass
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
name|javaClass
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
name|javaClass
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
name|javaClass
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
name|javaClass
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
name|javaClass
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
elseif|else
if|if
condition|(
name|javaClass
operator|.
name|equals
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
condition|)
block|{
name|result
operator|=
name|getTypeMapping
argument_list|()
operator|.
name|getType
argument_list|(
name|javaClass
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|AegisType
name|type
init|=
name|getTypeMapping
argument_list|()
operator|.
name|getType
argument_list|(
name|info
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
operator|||
operator|(
name|info
operator|.
name|getTypeName
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|type
operator|.
name|getSchemaType
argument_list|()
operator|.
name|equals
argument_list|(
name|info
operator|.
name|getTypeName
argument_list|()
argument_list|)
operator|)
condition|)
block|{
if|if
condition|(
name|info
operator|.
name|getTypeName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|type
operator|=
name|getTypeMapping
argument_list|()
operator|.
name|getType
argument_list|(
name|info
operator|.
name|getTypeName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
name|type
operator|=
name|getTypeMapping
argument_list|()
operator|.
name|getType
argument_list|(
name|javaClass
argument_list|)
expr_stmt|;
block|}
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
argument_list|<
name|?
argument_list|>
name|javaType
parameter_list|)
block|{
return|return
literal|"javax.xml.ws.Holder"
operator|.
name|equals
argument_list|(
name|javaType
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|AegisType
name|createHolderType
parameter_list|(
name|TypeClassInfo
name|info
parameter_list|)
block|{
name|Type
name|heldType
init|=
name|TypeUtil
operator|.
name|getSingleTypeParameter
argument_list|(
name|info
operator|.
name|getType
argument_list|()
argument_list|,
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|heldType
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Invalid holder type "
operator|+
name|info
operator|.
name|getType
argument_list|()
argument_list|)
throw|;
block|}
name|info
operator|.
name|setType
argument_list|(
name|heldType
argument_list|)
expr_stmt|;
return|return
name|createType
argument_list|(
name|heldType
argument_list|)
return|;
block|}
specifier|protected
name|boolean
name|isArray
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
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
name|AegisType
name|createUserType
parameter_list|(
name|TypeClassInfo
name|info
parameter_list|)
block|{
try|try
block|{
name|AegisType
name|type
init|=
name|info
operator|.
name|getAegisTypeClass
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
comment|// We do not want to use the java.lang.whatever schema type.
comment|// If the @ annotation or XML file didn't specify a schema type,
comment|// but the natural type has a schema type mapping, we use that rather
comment|// than create nonsense.
name|Class
argument_list|<
name|?
argument_list|>
name|typeClass
init|=
name|TypeUtil
operator|.
name|getTypeRelatedClass
argument_list|(
name|info
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|typeClass
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"java"
argument_list|)
condition|)
block|{
name|name
operator|=
name|tm
operator|.
name|getTypeQName
argument_list|(
name|typeClass
argument_list|)
expr_stmt|;
block|}
comment|// if it's still null, we'll take our lumps, but probably end up with
comment|// an invalid schema.
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
name|typeClass
argument_list|)
expr_stmt|;
block|}
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
name|getType
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
name|getAegisTypeClass
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
name|getAegisTypeClass
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
name|AegisType
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
name|getType
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
argument_list|<
name|?
argument_list|>
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
argument_list|<
name|?
argument_list|>
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
name|AegisType
name|createCollectionTypeFromGeneric
parameter_list|(
name|TypeClassInfo
name|info
parameter_list|)
block|{
name|AegisType
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
name|getType
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
name|AegisType
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
name|AegisType
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
name|AegisType
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
name|Constants
operator|.
name|XSD_ANYTYPE
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
name|AegisType
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
name|AegisType
name|createMapType
parameter_list|(
name|TypeClassInfo
name|info
parameter_list|,
name|AegisType
name|keyType
parameter_list|,
name|AegisType
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
name|getType
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|type
return|;
block|}
specifier|protected
name|AegisType
name|createMapType
parameter_list|(
name|TypeClassInfo
name|info
parameter_list|)
block|{
name|AegisType
name|keyType
init|=
name|getOrCreateMapKeyType
argument_list|(
name|info
argument_list|)
decl_stmt|;
name|AegisType
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
name|AegisType
name|keyType
parameter_list|,
name|AegisType
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
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
name|TypeUtil
operator|.
name|getTypeRelatedClass
argument_list|(
name|info
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
name|name
operator|+=
name|cls
operator|.
name|getSimpleName
argument_list|()
expr_stmt|;
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
argument_list|<
name|?
argument_list|>
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
argument_list|<
name|?
argument_list|>
name|javaType
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|AegisType
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
name|AegisType
name|createCollectionType
parameter_list|(
name|TypeClassInfo
name|info
parameter_list|)
function_decl|;
specifier|public
specifier|abstract
name|AegisType
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
name|AegisType
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
name|HTTP_CXF_APACHE_ORG_ARRAYS
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
name|getMinOccurs
argument_list|()
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
name|getMaxOccurs
argument_list|()
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
comment|/**      * Create a AegisType for a Method parameter.      *      * @param m the method to create a type for      * @param index The parameter index. If the index is less than zero, the      *            return type is used.      */
specifier|public
name|AegisType
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
comment|/**      * Create type information for a PropertyDescriptor.      *      * @param pd the propertydescriptor      */
specifier|public
name|AegisType
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
comment|/**      * Create type information for a<code>Field</code>.      *      * @param f the field to create a type from      */
specifier|public
name|AegisType
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
comment|/**      * Create an Aegis type from a reflected type description.      * This will only work for the restricted set of collection      * types supported by Aegis.      * @param t the reflected type.      * @return the type      */
specifier|public
name|AegisType
name|createType
parameter_list|(
name|Type
name|t
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
name|setType
argument_list|(
name|t
argument_list|)
expr_stmt|;
name|info
operator|.
name|setDescription
argument_list|(
literal|"reflected type "
operator|+
name|t
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
name|AegisType
name|createType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
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
block|}
end_class

end_unit

