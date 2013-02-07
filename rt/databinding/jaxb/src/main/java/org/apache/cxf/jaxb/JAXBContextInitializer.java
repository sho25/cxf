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
name|jaxb
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
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
name|Modifier
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
name|HashSet
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
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlSeeAlso
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|adapters
operator|.
name|XmlJavaTypeAdapter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|adapters
operator|.
name|XmlJavaTypeAdapters
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
name|classloader
operator|.
name|JAXBClassLoaderUtils
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
name|jaxb
operator|.
name|JAXBUtils
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
name|ReflectionUtil
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
name|service
operator|.
name|ServiceModelVisitor
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
name|MessageInfo
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
name|MessagePartInfo
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
name|OperationInfo
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
name|service
operator|.
name|model
operator|.
name|UnwrappedOperationInfo
import|;
end_import

begin_comment
comment|/**  * Walks the service model and sets up the classes for the context.  */
end_comment

begin_class
class|class
name|JAXBContextInitializer
extends|extends
name|ServiceModelVisitor
block|{
specifier|private
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|Object
argument_list|>
name|typeReferences
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|globalAdapters
init|=
operator|new
name|HashSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|JAXBContextInitializer
parameter_list|(
name|ServiceInfo
name|serviceInfo
parameter_list|,
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
parameter_list|,
name|Collection
argument_list|<
name|Object
argument_list|>
name|typeReferences
parameter_list|)
block|{
name|super
argument_list|(
name|serviceInfo
argument_list|)
expr_stmt|;
name|this
operator|.
name|classes
operator|=
name|classes
expr_stmt|;
name|this
operator|.
name|typeReferences
operator|=
name|typeReferences
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|begin
parameter_list|(
name|MessagePartInfo
name|part
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|part
operator|.
name|getTypeClass
argument_list|()
decl_stmt|;
if|if
condition|(
name|clazz
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|Exception
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
comment|//exceptions are handled special, make sure we mark it
name|part
operator|.
name|setProperty
argument_list|(
name|JAXBDataBinding
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".CUSTOM_EXCEPTION"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
name|boolean
name|isFromWrapper
init|=
name|part
operator|.
name|getMessageInfo
argument_list|()
operator|.
name|getOperation
argument_list|()
operator|.
name|isUnwrapped
argument_list|()
decl_stmt|;
if|if
condition|(
name|isFromWrapper
operator|&&
operator|!
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|part
operator|.
name|getProperty
argument_list|(
literal|"messagepart.isheader"
argument_list|)
argument_list|)
condition|)
block|{
name|UnwrappedOperationInfo
name|uop
init|=
operator|(
name|UnwrappedOperationInfo
operator|)
name|part
operator|.
name|getMessageInfo
argument_list|()
operator|.
name|getOperation
argument_list|()
decl_stmt|;
name|OperationInfo
name|op
init|=
name|uop
operator|.
name|getWrappedOperation
argument_list|()
decl_stmt|;
name|MessageInfo
name|inf
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|uop
operator|.
name|getInput
argument_list|()
operator|==
name|part
operator|.
name|getMessageInfo
argument_list|()
condition|)
block|{
name|inf
operator|=
name|op
operator|.
name|getInput
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|uop
operator|.
name|getOutput
argument_list|()
operator|==
name|part
operator|.
name|getMessageInfo
argument_list|()
condition|)
block|{
name|inf
operator|=
name|op
operator|.
name|getOutput
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|inf
operator|!=
literal|null
operator|&&
name|inf
operator|.
name|getMessagePart
argument_list|(
literal|0
argument_list|)
operator|.
name|getTypeClass
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|//if the wrapper has a type class, we don't need to do anything
comment|//as everything would have been discovered when walking the
comment|//wrapper type (unless it's a header which wouldn't be in the wrapper)
return|return;
block|}
block|}
if|if
condition|(
name|isFromWrapper
operator|&&
name|clazz
operator|.
name|isArray
argument_list|()
operator|&&
operator|!
name|Byte
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|clazz
operator|.
name|getComponentType
argument_list|()
argument_list|)
condition|)
block|{
name|clazz
operator|=
name|clazz
operator|.
name|getComponentType
argument_list|()
expr_stmt|;
block|}
name|Annotation
index|[]
name|a
init|=
operator|(
name|Annotation
index|[]
operator|)
name|part
operator|.
name|getProperty
argument_list|(
literal|"parameter.annotations"
argument_list|)
decl_stmt|;
name|checkForAdapter
argument_list|(
name|clazz
argument_list|,
name|a
argument_list|)
expr_stmt|;
name|Type
name|genericType
init|=
operator|(
name|Type
operator|)
name|part
operator|.
name|getProperty
argument_list|(
literal|"generic.type"
argument_list|)
decl_stmt|;
if|if
condition|(
name|genericType
operator|!=
literal|null
condition|)
block|{
name|boolean
name|isList
init|=
name|Collection
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
if|if
condition|(
name|isFromWrapper
condition|)
block|{
if|if
condition|(
name|genericType
operator|instanceof
name|Class
operator|&&
operator|(
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|genericType
operator|)
operator|.
name|isArray
argument_list|()
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|cl2
init|=
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|genericType
decl_stmt|;
if|if
condition|(
name|cl2
operator|.
name|isArray
argument_list|()
operator|&&
operator|!
name|Byte
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|cl2
operator|.
name|getComponentType
argument_list|()
argument_list|)
condition|)
block|{
name|genericType
operator|=
name|cl2
operator|.
name|getComponentType
argument_list|()
expr_stmt|;
block|}
name|addType
argument_list|(
name|genericType
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|isList
condition|)
block|{
name|addType
argument_list|(
name|genericType
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|addType
argument_list|(
name|genericType
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isList
operator|&&
name|genericType
operator|instanceof
name|ParameterizedType
condition|)
block|{
name|ParameterizedType
name|pt
init|=
operator|(
name|ParameterizedType
operator|)
name|genericType
decl_stmt|;
if|if
condition|(
name|pt
operator|.
name|getActualTypeArguments
argument_list|()
operator|.
name|length
operator|>
literal|0
operator|&&
name|pt
operator|.
name|getActualTypeArguments
argument_list|()
index|[
literal|0
index|]
operator|instanceof
name|Class
condition|)
block|{
name|Class
argument_list|<
name|?
extends|extends
name|Object
argument_list|>
name|arrayCls
init|=
name|Array
operator|.
name|newInstance
argument_list|(
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|pt
operator|.
name|getActualTypeArguments
argument_list|()
index|[
literal|0
index|]
argument_list|,
literal|0
argument_list|)
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|clazz
operator|=
name|arrayCls
expr_stmt|;
name|part
operator|.
name|setTypeClass
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
if|if
condition|(
name|isFromWrapper
condition|)
block|{
name|addType
argument_list|(
name|clazz
operator|.
name|getComponentType
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|pt
operator|.
name|getActualTypeArguments
argument_list|()
operator|.
name|length
operator|>
literal|0
operator|&&
name|pt
operator|.
name|getActualTypeArguments
argument_list|()
index|[
literal|0
index|]
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
name|pt
operator|.
name|getActualTypeArguments
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
name|gat
operator|.
name|getGenericComponentType
argument_list|()
expr_stmt|;
name|Class
argument_list|<
name|?
extends|extends
name|Object
argument_list|>
name|arrayCls
init|=
name|Array
operator|.
name|newInstance
argument_list|(
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|gat
operator|.
name|getGenericComponentType
argument_list|()
argument_list|,
literal|0
argument_list|)
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|clazz
operator|=
name|Array
operator|.
name|newInstance
argument_list|(
name|arrayCls
argument_list|,
literal|0
argument_list|)
operator|.
name|getClass
argument_list|()
expr_stmt|;
name|part
operator|.
name|setTypeClass
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
if|if
condition|(
name|isFromWrapper
condition|)
block|{
name|addType
argument_list|(
name|clazz
operator|.
name|getComponentType
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|isFromWrapper
operator|&&
name|isList
condition|)
block|{
name|clazz
operator|=
literal|null
expr_stmt|;
block|}
block|}
if|if
condition|(
name|clazz
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|isFromWrapper
operator|&&
name|clazz
operator|.
name|getAnnotation
argument_list|(
name|XmlRootElement
operator|.
name|class
argument_list|)
operator|==
literal|null
operator|&&
name|clazz
operator|.
name|getAnnotation
argument_list|(
name|XmlType
operator|.
name|class
argument_list|)
operator|!=
literal|null
operator|&&
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|clazz
operator|.
name|getAnnotation
argument_list|(
name|XmlType
operator|.
name|class
argument_list|)
operator|.
name|name
argument_list|()
argument_list|)
condition|)
block|{
name|Object
name|ref
init|=
name|JAXBClassLoaderUtils
operator|.
name|createTypeReference
argument_list|(
name|part
operator|.
name|getName
argument_list|()
argument_list|,
name|clazz
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
name|typeReferences
operator|.
name|add
argument_list|(
name|ref
argument_list|)
expr_stmt|;
block|}
block|}
name|addClass
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|checkForAdapter
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|,
name|Annotation
index|[]
name|anns
parameter_list|)
block|{
if|if
condition|(
name|anns
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Annotation
name|a
range|:
name|anns
control|)
block|{
if|if
condition|(
name|XmlJavaTypeAdapter
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|a
operator|.
name|annotationType
argument_list|()
argument_list|)
condition|)
block|{
name|Type
name|t
init|=
name|Utils
operator|.
name|getTypeFromXmlAdapter
argument_list|(
operator|(
name|XmlJavaTypeAdapter
operator|)
name|a
argument_list|)
decl_stmt|;
if|if
condition|(
name|t
operator|!=
literal|null
condition|)
block|{
name|addType
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|XmlJavaTypeAdapter
name|xjta
init|=
name|clazz
operator|.
name|getAnnotation
argument_list|(
name|XmlJavaTypeAdapter
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|xjta
operator|!=
literal|null
condition|)
block|{
name|Type
name|t
init|=
name|Utils
operator|.
name|getTypeFromXmlAdapter
argument_list|(
name|xjta
argument_list|)
decl_stmt|;
if|if
condition|(
name|t
operator|!=
literal|null
condition|)
block|{
name|addType
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|clazz
operator|.
name|getPackage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|XmlJavaTypeAdapters
name|adapt
init|=
name|clazz
operator|.
name|getPackage
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|XmlJavaTypeAdapters
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|adapt
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|XmlJavaTypeAdapter
name|a
range|:
name|adapt
operator|.
name|value
argument_list|()
control|)
block|{
name|globalAdapters
operator|.
name|add
argument_list|(
name|a
operator|.
name|type
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|XmlJavaTypeAdapter
name|a
range|:
name|adapt
operator|.
name|value
argument_list|()
control|)
block|{
name|Type
name|t
init|=
name|Utils
operator|.
name|getTypeFromXmlAdapter
argument_list|(
name|a
argument_list|)
decl_stmt|;
if|if
condition|(
name|t
operator|!=
literal|null
condition|)
block|{
name|addType
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
specifier|private
name|void
name|addType
parameter_list|(
name|Type
name|cls
parameter_list|)
block|{
name|addType
argument_list|(
name|cls
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|addType
parameter_list|(
name|Type
name|cls
parameter_list|,
name|boolean
name|allowArray
parameter_list|)
block|{
if|if
condition|(
name|cls
operator|instanceof
name|Class
condition|)
block|{
if|if
condition|(
name|globalAdapters
operator|.
name|contains
argument_list|(
name|cls
argument_list|)
condition|)
block|{
return|return;
block|}
if|if
condition|(
operator|(
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|cls
operator|)
operator|.
name|isArray
argument_list|()
operator|&&
operator|!
name|allowArray
condition|)
block|{
name|addClass
argument_list|(
operator|(
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|cls
operator|)
operator|.
name|getComponentType
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|addClass
argument_list|(
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|cls
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|cls
operator|instanceof
name|ParameterizedType
condition|)
block|{
name|addType
argument_list|(
operator|(
operator|(
name|ParameterizedType
operator|)
name|cls
operator|)
operator|.
name|getRawType
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
operator|(
operator|(
name|ParameterizedType
operator|)
name|cls
operator|)
operator|.
name|getRawType
argument_list|()
operator|.
name|equals
argument_list|(
name|Enum
operator|.
name|class
argument_list|)
condition|)
block|{
for|for
control|(
name|Type
name|t2
range|:
operator|(
operator|(
name|ParameterizedType
operator|)
name|cls
operator|)
operator|.
name|getActualTypeArguments
argument_list|()
control|)
block|{
name|addType
argument_list|(
name|t2
argument_list|)
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|cls
operator|instanceof
name|GenericArrayType
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|ct
decl_stmt|;
name|GenericArrayType
name|gt
init|=
operator|(
name|GenericArrayType
operator|)
name|cls
decl_stmt|;
name|Type
name|componentType
init|=
name|gt
operator|.
name|getGenericComponentType
argument_list|()
decl_stmt|;
if|if
condition|(
name|componentType
operator|instanceof
name|Class
condition|)
block|{
name|ct
operator|=
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|componentType
expr_stmt|;
block|}
else|else
block|{
name|TypeVariable
argument_list|<
name|?
argument_list|>
name|tv
init|=
operator|(
name|TypeVariable
argument_list|<
name|?
argument_list|>
operator|)
name|componentType
decl_stmt|;
name|Type
index|[]
name|bounds
init|=
name|tv
operator|.
name|getBounds
argument_list|()
decl_stmt|;
if|if
condition|(
name|bounds
operator|!=
literal|null
operator|&&
name|bounds
operator|.
name|length
operator|==
literal|1
condition|)
block|{
if|if
condition|(
name|bounds
index|[
literal|0
index|]
operator|instanceof
name|Class
condition|)
block|{
name|ct
operator|=
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|bounds
index|[
literal|0
index|]
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unable to determine type for: "
operator|+
name|tv
argument_list|)
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unable to determine type for: "
operator|+
name|tv
argument_list|)
throw|;
block|}
block|}
name|ct
operator|=
name|Array
operator|.
name|newInstance
argument_list|(
name|ct
argument_list|,
literal|0
argument_list|)
operator|.
name|getClass
argument_list|()
expr_stmt|;
name|addClass
argument_list|(
name|ct
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|cls
operator|instanceof
name|WildcardType
condition|)
block|{
for|for
control|(
name|Type
name|t
range|:
operator|(
operator|(
name|WildcardType
operator|)
name|cls
operator|)
operator|.
name|getUpperBounds
argument_list|()
control|)
block|{
name|addType
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Type
name|t
range|:
operator|(
operator|(
name|WildcardType
operator|)
name|cls
operator|)
operator|.
name|getLowerBounds
argument_list|()
control|)
block|{
name|addType
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|cls
operator|instanceof
name|TypeVariable
condition|)
block|{
for|for
control|(
name|Type
name|t
range|:
operator|(
operator|(
name|TypeVariable
argument_list|<
name|?
argument_list|>
operator|)
name|cls
operator|)
operator|.
name|getBounds
argument_list|()
control|)
block|{
name|addType
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|void
name|addClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
if|if
condition|(
name|Throwable
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|cls
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|Throwable
operator|.
name|class
operator|.
name|equals
argument_list|(
name|cls
argument_list|)
operator|&&
operator|!
name|Exception
operator|.
name|class
operator|.
name|equals
argument_list|(
name|cls
argument_list|)
condition|)
block|{
name|walkReferences
argument_list|(
name|cls
argument_list|)
expr_stmt|;
block|}
name|addClass
argument_list|(
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cls
operator|=
name|JAXBUtils
operator|.
name|getValidClass
argument_list|(
name|cls
argument_list|)
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|cls
condition|)
block|{
if|if
condition|(
name|classes
operator|.
name|contains
argument_list|(
name|cls
argument_list|)
condition|)
block|{
return|return;
block|}
if|if
condition|(
operator|!
name|cls
operator|.
name|isInterface
argument_list|()
condition|)
block|{
name|classes
operator|.
name|add
argument_list|(
name|cls
argument_list|)
expr_stmt|;
block|}
name|XmlSeeAlso
name|xsa
init|=
name|cls
operator|.
name|getAnnotation
argument_list|(
name|XmlSeeAlso
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|xsa
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
range|:
name|xsa
operator|.
name|value
argument_list|()
control|)
block|{
name|addClass
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
name|XmlJavaTypeAdapter
name|xjta
init|=
name|cls
operator|.
name|getAnnotation
argument_list|(
name|XmlJavaTypeAdapter
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|xjta
operator|!=
literal|null
condition|)
block|{
comment|//has an adapter.   We need to inspect the adapter and then
comment|//return as the adapter will handle the superclass
comment|//and interfaces and such
name|Type
name|t
init|=
name|Utils
operator|.
name|getTypeFromXmlAdapter
argument_list|(
name|xjta
argument_list|)
decl_stmt|;
if|if
condition|(
name|t
operator|!=
literal|null
condition|)
block|{
name|addType
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
if|if
condition|(
name|cls
operator|.
name|getSuperclass
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|//JAXB should do this, but it doesn't always.
comment|//in particular, older versions of jaxb don't
name|addClass
argument_list|(
name|cls
operator|.
name|getSuperclass
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|cls
operator|.
name|isInterface
argument_list|()
condition|)
block|{
name|walkReferences
argument_list|(
name|cls
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|walkReferences
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
if|if
condition|(
name|cls
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|cls
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"java."
argument_list|)
operator|||
name|cls
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"javax."
argument_list|)
condition|)
block|{
return|return;
block|}
comment|//walk the public fields/methods to try and find all the classes. JAXB will only load the
comment|//EXACT classes in the fields/methods if they are in a different package. Thus,
comment|//subclasses won't be found and the xsi:type stuff won't work at all.
comment|//We'll grab the public field/method types and then add the ObjectFactory stuff
comment|//as well as look for jaxb.index files in those packages.
name|XmlAccessType
name|accessType
init|=
name|Utils
operator|.
name|getXmlAccessType
argument_list|(
name|cls
argument_list|)
decl_stmt|;
if|if
condition|(
name|accessType
operator|!=
name|XmlAccessType
operator|.
name|PROPERTY
condition|)
block|{
comment|// only look for fields if we are instructed to
comment|//fields are accessible even if not public, must look at the declared fields
comment|//then walk to parents declared fields, etc...
name|Field
name|fields
index|[]
init|=
name|ReflectionUtil
operator|.
name|getDeclaredFields
argument_list|(
name|cls
argument_list|)
decl_stmt|;
for|for
control|(
name|Field
name|f
range|:
name|fields
control|)
block|{
if|if
condition|(
name|isFieldAccepted
argument_list|(
name|f
argument_list|,
name|accessType
argument_list|)
condition|)
block|{
name|addType
argument_list|(
name|f
operator|.
name|getGenericType
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|walkReferences
argument_list|(
name|cls
operator|.
name|getSuperclass
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|accessType
operator|!=
name|XmlAccessType
operator|.
name|FIELD
condition|)
block|{
comment|// only look for methods if we are instructed to
name|Method
name|methods
index|[]
init|=
name|ReflectionUtil
operator|.
name|getDeclaredMethods
argument_list|(
name|cls
argument_list|)
decl_stmt|;
for|for
control|(
name|Method
name|m
range|:
name|methods
control|)
block|{
if|if
condition|(
name|isMethodAccepted
argument_list|(
name|m
argument_list|,
name|accessType
argument_list|)
condition|)
block|{
name|addType
argument_list|(
name|m
operator|.
name|getGenericReturnType
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Type
name|t
range|:
name|m
operator|.
name|getGenericParameterTypes
argument_list|()
control|)
block|{
name|addType
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
comment|/**      * Checks if the field is accepted as a JAXB property.      */
specifier|static
name|boolean
name|isFieldAccepted
parameter_list|(
name|Field
name|field
parameter_list|,
name|XmlAccessType
name|accessType
parameter_list|)
block|{
comment|// We only accept non static fields which are not marked @XmlTransient
if|if
condition|(
name|Modifier
operator|.
name|isStatic
argument_list|(
name|field
operator|.
name|getModifiers
argument_list|()
argument_list|)
operator|||
name|field
operator|.
name|isAnnotationPresent
argument_list|(
name|XmlTransient
operator|.
name|class
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|accessType
operator|==
name|XmlAccessType
operator|.
name|PUBLIC_MEMBER
operator|&&
operator|!
name|Modifier
operator|.
name|isPublic
argument_list|(
name|field
operator|.
name|getModifiers
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|accessType
operator|==
name|XmlAccessType
operator|.
name|NONE
operator|||
name|accessType
operator|==
name|XmlAccessType
operator|.
name|PROPERTY
condition|)
block|{
return|return
name|checkJaxbAnnotation
argument_list|(
name|field
operator|.
name|getAnnotations
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|true
return|;
block|}
block|}
comment|/**      * Checks if the method is accepted as a JAXB property getter.      */
specifier|static
name|boolean
name|isMethodAccepted
parameter_list|(
name|Method
name|method
parameter_list|,
name|XmlAccessType
name|accessType
parameter_list|)
block|{
comment|// We only accept non static property getters which are not marked @XmlTransient
if|if
condition|(
name|Modifier
operator|.
name|isStatic
argument_list|(
name|method
operator|.
name|getModifiers
argument_list|()
argument_list|)
operator|||
name|method
operator|.
name|isAnnotationPresent
argument_list|(
name|XmlTransient
operator|.
name|class
argument_list|)
operator|||
operator|!
name|Modifier
operator|.
name|isPublic
argument_list|(
name|method
operator|.
name|getModifiers
argument_list|()
argument_list|)
operator|||
literal|"getClass"
operator|.
name|equals
argument_list|(
name|method
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// must not have parameters and return type must not be void
if|if
condition|(
name|method
operator|.
name|getReturnType
argument_list|()
operator|==
name|Void
operator|.
name|class
operator|||
name|method
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
operator|!=
literal|0
operator|||
operator|(
name|method
operator|.
name|getDeclaringClass
argument_list|()
operator|.
name|equals
argument_list|(
name|Throwable
operator|.
name|class
argument_list|)
operator|&&
operator|!
operator|(
literal|"getMessage"
operator|.
name|equals
argument_list|(
name|method
operator|.
name|getName
argument_list|()
argument_list|)
operator|)
operator|)
operator|||
operator|!
operator|(
name|method
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"get"
argument_list|)
operator|||
name|method
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"is"
argument_list|)
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|int
name|beginIndex
init|=
literal|3
decl_stmt|;
if|if
condition|(
name|method
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"is"
argument_list|)
condition|)
block|{
name|beginIndex
operator|=
literal|2
expr_stmt|;
block|}
name|Method
name|setter
init|=
literal|null
decl_stmt|;
try|try
block|{
name|setter
operator|=
name|method
operator|.
name|getDeclaringClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"set"
operator|+
name|method
operator|.
name|getName
argument_list|()
operator|.
name|substring
argument_list|(
name|beginIndex
argument_list|)
argument_list|,
operator|new
name|Class
index|[]
block|{
name|method
operator|.
name|getReturnType
argument_list|()
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//getter, but no setter
block|}
if|if
condition|(
name|setter
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|setter
operator|.
name|isAnnotationPresent
argument_list|(
name|XmlTransient
operator|.
name|class
argument_list|)
operator|||
operator|!
name|Modifier
operator|.
name|isPublic
argument_list|(
name|setter
operator|.
name|getModifiers
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
elseif|else
if|if
condition|(
operator|!
name|Collection
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|method
operator|.
name|getReturnType
argument_list|()
argument_list|)
operator|&&
operator|!
name|Throwable
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|method
operator|.
name|getDeclaringClass
argument_list|()
argument_list|)
condition|)
block|{
comment|//no setter, it's not a collection (thus getter().add(...)), and
comment|//not an Exception,
return|return
literal|false
return|;
block|}
if|if
condition|(
name|accessType
operator|==
name|XmlAccessType
operator|.
name|NONE
operator|||
name|accessType
operator|==
name|XmlAccessType
operator|.
name|FIELD
condition|)
block|{
return|return
name|checkJaxbAnnotation
argument_list|(
name|method
operator|.
name|getAnnotations
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|true
return|;
block|}
comment|/**      * Checks if there are JAXB annotations among the annotations of the class member.      * @param annotations the array of annotations from the class member      * @return true if JAXB annotations are present, false otherwise      */
specifier|static
name|boolean
name|checkJaxbAnnotation
parameter_list|(
name|Annotation
index|[]
name|annotations
parameter_list|)
block|{
comment|// must check if there are any jaxb annotations
name|Package
name|jaxbAnnotationsPackage
init|=
name|XmlElement
operator|.
name|class
operator|.
name|getPackage
argument_list|()
decl_stmt|;
for|for
control|(
name|Annotation
name|annotation
range|:
name|annotations
control|)
block|{
if|if
condition|(
name|annotation
operator|.
name|annotationType
argument_list|()
operator|.
name|getPackage
argument_list|()
operator|==
name|jaxbAnnotationsPackage
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

