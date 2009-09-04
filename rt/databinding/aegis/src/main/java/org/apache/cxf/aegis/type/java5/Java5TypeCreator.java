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
operator|.
name|java5
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
name|WildcardType
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
name|AbstractTypeCreator
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
name|AegisType
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
name|TypeClassInfo
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
name|BeanType
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

begin_class
specifier|public
class|class
name|Java5TypeCreator
extends|extends
name|AbstractTypeCreator
block|{
specifier|private
name|AnnotationReader
name|annotationReader
decl_stmt|;
specifier|public
name|Java5TypeCreator
parameter_list|()
block|{
name|this
argument_list|(
operator|new
name|AnnotationReader
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Java5TypeCreator
parameter_list|(
name|AnnotationReader
name|annotationReader
parameter_list|)
block|{
name|this
operator|.
name|annotationReader
operator|=
name|annotationReader
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
specifier|static
name|Class
argument_list|<
name|?
extends|extends
name|AegisType
argument_list|>
name|castToAegisTypeClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
parameter_list|)
block|{
if|if
condition|(
name|c
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|AegisType
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|c
argument_list|)
condition|)
block|{
return|return
operator|(
name|Class
argument_list|<
name|?
extends|extends
name|AegisType
argument_list|>
operator|)
name|c
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Invalid Aegis type annotation to non-type class"
operator|+
name|c
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|TypeClassInfo
name|createClassInfo
parameter_list|(
name|Method
name|m
parameter_list|,
name|int
name|index
parameter_list|)
block|{
if|if
condition|(
name|index
operator|>=
literal|0
condition|)
block|{
name|TypeClassInfo
name|info
decl_stmt|;
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
name|genericType
init|=
name|m
operator|.
name|getGenericParameterTypes
argument_list|()
index|[
name|index
index|]
decl_stmt|;
if|if
condition|(
name|genericType
operator|instanceof
name|Class
condition|)
block|{
name|info
operator|=
name|nextCreator
operator|.
name|createClassInfo
argument_list|(
name|m
argument_list|,
name|index
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|info
operator|=
operator|new
name|TypeClassInfo
argument_list|()
expr_stmt|;
name|info
operator|.
name|setDescription
argument_list|(
literal|"method "
operator|+
name|m
operator|.
name|getName
argument_list|()
operator|+
literal|" parameter "
operator|+
name|index
argument_list|)
expr_stmt|;
name|info
operator|.
name|setGenericType
argument_list|(
name|genericType
argument_list|)
expr_stmt|;
block|}
name|info
operator|.
name|setTypeClass
argument_list|(
name|m
operator|.
name|getParameterTypes
argument_list|()
index|[
name|index
index|]
argument_list|)
expr_stmt|;
name|Class
name|paramTypeClass
init|=
name|annotationReader
operator|.
name|getParamType
argument_list|(
name|m
argument_list|,
name|index
argument_list|)
decl_stmt|;
name|info
operator|.
name|setAegisTypeClass
argument_list|(
name|castToAegisTypeClass
argument_list|(
name|paramTypeClass
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|paramName
init|=
name|annotationReader
operator|.
name|getParamName
argument_list|(
name|m
argument_list|,
name|index
argument_list|)
decl_stmt|;
if|if
condition|(
name|paramName
operator|!=
literal|null
condition|)
block|{
name|info
operator|.
name|setTypeName
argument_list|(
name|createQName
argument_list|(
name|m
operator|.
name|getParameterTypes
argument_list|()
index|[
name|index
index|]
argument_list|,
name|paramName
argument_list|,
name|annotationReader
operator|.
name|getParamNamespace
argument_list|(
name|m
argument_list|,
name|index
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|info
return|;
block|}
else|else
block|{
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
name|genericReturnType
init|=
name|m
operator|.
name|getGenericReturnType
argument_list|()
decl_stmt|;
name|TypeClassInfo
name|info
decl_stmt|;
if|if
condition|(
name|genericReturnType
operator|instanceof
name|Class
condition|)
block|{
name|info
operator|=
name|nextCreator
operator|.
name|createClassInfo
argument_list|(
name|m
argument_list|,
name|index
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|info
operator|=
operator|new
name|TypeClassInfo
argument_list|()
expr_stmt|;
name|info
operator|.
name|setDescription
argument_list|(
literal|"method "
operator|+
name|m
operator|.
name|getName
argument_list|()
operator|+
literal|" parameter "
operator|+
name|index
argument_list|)
expr_stmt|;
name|info
operator|.
name|setGenericType
argument_list|(
name|genericReturnType
argument_list|)
expr_stmt|;
block|}
name|info
operator|.
name|setTypeClass
argument_list|(
name|m
operator|.
name|getReturnType
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|m
operator|.
name|getParameterAnnotations
argument_list|()
operator|!=
literal|null
operator|&&
name|m
operator|.
name|getAnnotations
argument_list|()
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|info
operator|.
name|setAnnotations
argument_list|(
name|m
operator|.
name|getAnnotations
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|info
operator|.
name|setAegisTypeClass
argument_list|(
name|castToAegisTypeClass
argument_list|(
name|annotationReader
operator|.
name|getReturnType
argument_list|(
name|m
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|returnName
init|=
name|annotationReader
operator|.
name|getReturnName
argument_list|(
name|m
argument_list|)
decl_stmt|;
if|if
condition|(
name|returnName
operator|!=
literal|null
condition|)
block|{
name|info
operator|.
name|setTypeName
argument_list|(
name|createQName
argument_list|(
name|m
operator|.
name|getReturnType
argument_list|()
argument_list|,
name|returnName
argument_list|,
name|annotationReader
operator|.
name|getReturnNamespace
argument_list|(
name|m
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|info
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|TypeClassInfo
name|createClassInfo
parameter_list|(
name|PropertyDescriptor
name|pd
parameter_list|)
block|{
name|TypeClassInfo
name|info
init|=
name|createBasicClassInfo
argument_list|(
name|pd
operator|.
name|getPropertyType
argument_list|()
argument_list|)
decl_stmt|;
name|info
operator|.
name|setGenericType
argument_list|(
name|pd
operator|.
name|getReadMethod
argument_list|()
operator|.
name|getGenericReturnType
argument_list|()
argument_list|)
expr_stmt|;
name|info
operator|.
name|setAnnotations
argument_list|(
name|pd
operator|.
name|getReadMethod
argument_list|()
operator|.
name|getAnnotations
argument_list|()
argument_list|)
expr_stmt|;
name|info
operator|.
name|setAegisTypeClass
argument_list|(
name|castToAegisTypeClass
argument_list|(
name|annotationReader
operator|.
name|getType
argument_list|(
name|pd
operator|.
name|getReadMethod
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|info
return|;
block|}
annotation|@
name|Override
specifier|public
name|AegisType
name|createCollectionType
parameter_list|(
name|TypeClassInfo
name|info
parameter_list|)
block|{
name|Object
name|genericType
init|=
name|info
operator|.
name|getGenericType
argument_list|()
decl_stmt|;
name|Class
name|paramClass
init|=
name|getComponentType
argument_list|(
name|genericType
argument_list|,
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|paramClass
operator|!=
literal|null
condition|)
block|{
return|return
name|createCollectionTypeFromGeneric
argument_list|(
name|info
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|nextCreator
operator|.
name|createCollectionType
argument_list|(
name|info
argument_list|)
return|;
block|}
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
name|getOrCreateParameterizedType
argument_list|(
name|info
operator|.
name|getGenericType
argument_list|()
argument_list|,
literal|0
argument_list|)
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
name|getOrCreateParameterizedType
argument_list|(
name|info
operator|.
name|getGenericType
argument_list|()
argument_list|,
literal|0
argument_list|)
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
name|getOrCreateParameterizedType
argument_list|(
name|info
operator|.
name|getGenericType
argument_list|()
argument_list|,
literal|1
argument_list|)
return|;
block|}
specifier|protected
name|AegisType
name|getOrCreateParameterizedType
parameter_list|(
name|Object
name|generic
parameter_list|,
name|int
name|index
parameter_list|)
block|{
name|Class
name|clazz
init|=
name|getComponentType
argument_list|(
name|generic
argument_list|,
name|index
argument_list|)
decl_stmt|;
if|if
condition|(
name|clazz
operator|==
literal|null
condition|)
block|{
return|return
name|createObjectType
argument_list|()
return|;
block|}
if|if
condition|(
operator|!
name|Collection
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
operator|&&
operator|!
name|Map
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
return|return
name|getTopCreator
argument_list|()
operator|.
name|createType
argument_list|(
name|clazz
argument_list|)
return|;
block|}
name|Object
name|component
init|=
name|getGenericComponent
argument_list|(
name|generic
argument_list|,
name|index
argument_list|)
decl_stmt|;
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
name|info
operator|.
name|setGenericType
argument_list|(
name|component
argument_list|)
expr_stmt|;
name|AegisType
name|type
init|=
name|createTypeForClass
argument_list|(
name|info
argument_list|)
decl_stmt|;
return|return
name|type
return|;
block|}
specifier|private
name|Object
name|getGenericComponent
parameter_list|(
name|Object
name|genericType
parameter_list|,
name|int
name|index
parameter_list|)
block|{
if|if
condition|(
name|genericType
operator|instanceof
name|ParameterizedType
condition|)
block|{
name|ParameterizedType
name|type
init|=
operator|(
name|ParameterizedType
operator|)
name|genericType
decl_stmt|;
if|if
condition|(
name|type
operator|.
name|getActualTypeArguments
argument_list|()
index|[
name|index
index|]
operator|instanceof
name|WildcardType
condition|)
block|{
name|WildcardType
name|wildcardType
init|=
operator|(
name|WildcardType
operator|)
name|type
operator|.
name|getActualTypeArguments
argument_list|()
index|[
name|index
index|]
decl_stmt|;
return|return
name|wildcardType
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|.
name|getActualTypeArguments
argument_list|()
index|[
name|index
index|]
operator|instanceof
name|ParameterizedType
condition|)
block|{
name|ParameterizedType
name|ptype
init|=
operator|(
name|ParameterizedType
operator|)
name|type
operator|.
name|getActualTypeArguments
argument_list|()
index|[
name|index
index|]
decl_stmt|;
return|return
name|ptype
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|Class
name|getComponentType
parameter_list|(
name|Object
name|genericType
parameter_list|,
name|int
name|index
parameter_list|)
block|{
name|Class
name|paramClass
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|genericType
operator|instanceof
name|ParameterizedType
condition|)
block|{
name|ParameterizedType
name|type
init|=
operator|(
name|ParameterizedType
operator|)
name|genericType
decl_stmt|;
if|if
condition|(
name|type
operator|.
name|getActualTypeArguments
argument_list|()
index|[
name|index
index|]
operator|instanceof
name|Class
condition|)
block|{
name|paramClass
operator|=
operator|(
name|Class
operator|)
name|type
operator|.
name|getActualTypeArguments
argument_list|()
index|[
name|index
index|]
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|type
operator|.
name|getActualTypeArguments
argument_list|()
index|[
name|index
index|]
operator|instanceof
name|WildcardType
condition|)
block|{
name|WildcardType
name|wildcardType
init|=
operator|(
name|WildcardType
operator|)
name|type
operator|.
name|getActualTypeArguments
argument_list|()
index|[
name|index
index|]
decl_stmt|;
comment|// we really aren't prepared to deal with multiple upper bounds,
comment|// so we just look at the first one.
if|if
condition|(
name|wildcardType
operator|.
name|getUpperBounds
argument_list|()
index|[
literal|0
index|]
operator|instanceof
name|Class
condition|)
block|{
name|paramClass
operator|=
operator|(
name|Class
operator|)
name|wildcardType
operator|.
name|getUpperBounds
argument_list|()
index|[
literal|0
index|]
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|type
operator|.
name|getActualTypeArguments
argument_list|()
index|[
name|index
index|]
operator|instanceof
name|ParameterizedType
condition|)
block|{
name|ParameterizedType
name|ptype
init|=
operator|(
name|ParameterizedType
operator|)
name|type
operator|.
name|getActualTypeArguments
argument_list|()
index|[
name|index
index|]
decl_stmt|;
name|paramClass
operator|=
operator|(
name|Class
operator|)
name|ptype
operator|.
name|getRawType
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|paramClass
return|;
block|}
annotation|@
name|Override
specifier|public
name|AegisType
name|createDefaultType
parameter_list|(
name|TypeClassInfo
name|info
parameter_list|)
block|{
name|QName
name|typeName
init|=
name|info
operator|.
name|getTypeName
argument_list|()
decl_stmt|;
if|if
condition|(
name|typeName
operator|==
literal|null
condition|)
block|{
name|typeName
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
name|AnnotatedTypeInfo
name|typeInfo
init|=
operator|new
name|AnnotatedTypeInfo
argument_list|(
name|getTypeMapping
argument_list|()
argument_list|,
name|info
operator|.
name|getTypeClass
argument_list|()
argument_list|,
name|typeName
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|getConfiguration
argument_list|()
argument_list|)
decl_stmt|;
name|typeInfo
operator|.
name|setExtensibleElements
argument_list|(
name|annotationReader
operator|.
name|isExtensibleElements
argument_list|(
name|info
operator|.
name|getTypeClass
argument_list|()
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|isDefaultExtensibleElements
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|typeInfo
operator|.
name|setExtensibleAttributes
argument_list|(
name|annotationReader
operator|.
name|isExtensibleAttributes
argument_list|(
name|info
operator|.
name|getTypeClass
argument_list|()
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|isDefaultExtensibleAttributes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|typeInfo
operator|.
name|setDefaultMinOccurs
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getDefaultMinOccurs
argument_list|()
argument_list|)
expr_stmt|;
name|typeInfo
operator|.
name|setDefaultNillable
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|isDefaultNillable
argument_list|()
argument_list|)
expr_stmt|;
name|BeanType
name|type
init|=
operator|new
name|BeanType
argument_list|(
name|typeInfo
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
name|setSchemaType
argument_list|(
name|typeName
argument_list|)
expr_stmt|;
return|return
name|type
return|;
block|}
annotation|@
name|Override
specifier|public
name|AegisType
name|createEnumType
parameter_list|(
name|TypeClassInfo
name|info
parameter_list|)
block|{
name|EnumType
name|type
init|=
operator|new
name|EnumType
argument_list|()
decl_stmt|;
name|type
operator|.
name|setSchemaType
argument_list|(
name|createQName
argument_list|(
name|info
operator|.
name|getTypeClass
argument_list|()
argument_list|)
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
annotation|@
name|Override
specifier|public
name|QName
name|createQName
parameter_list|(
name|Class
name|typeClass
parameter_list|)
block|{
name|String
name|name
init|=
name|annotationReader
operator|.
name|getName
argument_list|(
name|typeClass
argument_list|)
decl_stmt|;
name|String
name|ns
init|=
name|annotationReader
operator|.
name|getNamespace
argument_list|(
name|typeClass
argument_list|)
decl_stmt|;
return|return
name|createQName
argument_list|(
name|typeClass
argument_list|,
name|name
argument_list|,
name|ns
argument_list|)
return|;
block|}
specifier|private
name|QName
name|createQName
parameter_list|(
name|Class
name|typeClass
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|ns
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
operator|||
name|name
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|name
operator|=
name|ServiceUtils
operator|.
name|makeServiceNameFromClassName
argument_list|(
name|typeClass
argument_list|)
expr_stmt|;
block|}
comment|// check jaxb package annotation
if|if
condition|(
name|ns
operator|==
literal|null
operator|||
name|ns
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|ns
operator|=
name|annotationReader
operator|.
name|getNamespace
argument_list|(
name|typeClass
operator|.
name|getPackage
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ns
operator|==
literal|null
operator|||
name|ns
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|ns
operator|=
name|NamespaceHelper
operator|.
name|makeNamespaceFromClassName
argument_list|(
name|typeClass
operator|.
name|getName
argument_list|()
argument_list|,
literal|"http"
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|isEnum
parameter_list|(
name|Class
name|javaType
parameter_list|)
block|{
return|return
name|javaType
operator|.
name|isEnum
argument_list|()
return|;
block|}
block|}
end_class

end_unit

