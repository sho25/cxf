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
name|tools
operator|.
name|java2wsdl
operator|.
name|processor
operator|.
name|internal
operator|.
name|jaxws
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
name|Type
import|;
end_import

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
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebParam
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
name|tools
operator|.
name|common
operator|.
name|model
operator|.
name|JavaField
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
name|tools
operator|.
name|java2wsdl
operator|.
name|generator
operator|.
name|wsdl11
operator|.
name|model
operator|.
name|WrapperBeanClass
import|;
end_import

begin_class
specifier|public
class|class
name|RequestWrapper
extends|extends
name|Wrapper
block|{
annotation|@
name|Override
specifier|public
name|void
name|setOperationInfo
parameter_list|(
specifier|final
name|OperationInfo
name|op
parameter_list|)
block|{
name|super
operator|.
name|setOperationInfo
argument_list|(
name|op
argument_list|)
expr_stmt|;
name|setName
argument_list|(
name|op
operator|.
name|getInput
argument_list|()
operator|.
name|getMessageParts
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getElementQName
argument_list|()
argument_list|)
expr_stmt|;
name|setClassName
argument_list|(
operator|(
name|String
operator|)
name|op
operator|.
name|getInput
argument_list|()
operator|.
name|getMessageParts
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getProperty
argument_list|(
literal|"REQUEST.WRAPPER.CLASSNAME"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isWrapperAbsent
parameter_list|(
specifier|final
name|Method
name|method
parameter_list|)
block|{
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|RequestWrapper
name|reqWrapper
init|=
name|method
operator|.
name|getAnnotation
argument_list|(
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|RequestWrapper
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|getClassName
argument_list|()
operator|==
literal|null
operator|&&
operator|(
name|reqWrapper
operator|==
literal|null
operator|||
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|reqWrapper
operator|.
name|className
argument_list|()
argument_list|)
operator|)
return|;
block|}
specifier|public
name|String
name|getWrapperTns
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|RequestWrapper
name|reqWrapper
init|=
name|method
operator|.
name|getAnnotation
argument_list|(
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|RequestWrapper
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|reqWrapper
operator|!=
literal|null
condition|)
block|{
return|return
name|reqWrapper
operator|.
name|targetNamespace
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|protected
name|List
argument_list|<
name|JavaField
argument_list|>
name|buildFields
parameter_list|()
block|{
return|return
name|buildFields
argument_list|(
name|getMethod
argument_list|()
argument_list|,
name|getOperationInfo
argument_list|()
operator|.
name|getUnwrappedOperation
argument_list|()
operator|.
name|getInput
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|List
argument_list|<
name|JavaField
argument_list|>
name|buildFields
parameter_list|(
specifier|final
name|Method
name|method
parameter_list|,
specifier|final
name|MessageInfo
name|message
parameter_list|)
block|{
name|List
argument_list|<
name|JavaField
argument_list|>
name|fields
init|=
operator|new
name|ArrayList
argument_list|<
name|JavaField
argument_list|>
argument_list|()
decl_stmt|;
name|String
name|name
decl_stmt|;
name|String
name|type
init|=
literal|"Object"
decl_stmt|;
specifier|final
name|Type
index|[]
name|paramClasses
init|=
name|method
operator|.
name|getGenericParameterTypes
argument_list|()
decl_stmt|;
specifier|final
name|Annotation
index|[]
index|[]
name|paramAnnotations
init|=
name|method
operator|.
name|getParameterAnnotations
argument_list|()
decl_stmt|;
for|for
control|(
name|MessagePartInfo
name|mpi
range|:
name|message
operator|.
name|getMessageParts
argument_list|()
control|)
block|{
name|int
name|idx
init|=
name|mpi
operator|.
name|getIndex
argument_list|()
decl_stmt|;
name|name
operator|=
name|mpi
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
expr_stmt|;
name|Type
name|t
init|=
name|paramClasses
index|[
name|idx
index|]
decl_stmt|;
name|Class
name|clz
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|t
operator|instanceof
name|Class
condition|)
block|{
name|clz
operator|=
operator|(
name|Class
operator|)
name|t
expr_stmt|;
if|if
condition|(
name|clz
operator|.
name|isArray
argument_list|()
condition|)
block|{
if|if
condition|(
name|isBuiltInTypes
argument_list|(
name|clz
operator|.
name|getComponentType
argument_list|()
argument_list|)
condition|)
block|{
name|type
operator|=
name|clz
operator|.
name|getComponentType
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|"[]"
expr_stmt|;
block|}
else|else
block|{
name|type
operator|=
name|clz
operator|.
name|getComponentType
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"[]"
expr_stmt|;
block|}
block|}
else|else
block|{
name|type
operator|=
name|clz
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|t
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
name|t
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
name|clz
operator|=
operator|(
name|Class
operator|)
name|pt
operator|.
name|getActualTypeArguments
argument_list|()
index|[
literal|0
index|]
expr_stmt|;
name|type
operator|=
name|clz
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
block|}
name|JavaField
name|field
init|=
operator|new
name|JavaField
argument_list|(
name|name
argument_list|,
name|type
argument_list|,
literal|""
argument_list|)
decl_stmt|;
if|if
condition|(
name|paramAnnotations
operator|!=
literal|null
operator|&&
name|paramAnnotations
operator|.
name|length
operator|==
name|paramClasses
operator|.
name|length
condition|)
block|{
name|WebParam
name|wParam
init|=
name|getWebParamAnnotation
argument_list|(
name|paramAnnotations
index|[
name|idx
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|wParam
operator|!=
literal|null
operator|&&
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|wParam
operator|.
name|targetNamespace
argument_list|()
argument_list|)
condition|)
block|{
name|field
operator|.
name|setTargetNamespace
argument_list|(
name|wParam
operator|.
name|targetNamespace
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|field
operator|.
name|setTargetNamespace
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
block|}
name|List
argument_list|<
name|Annotation
argument_list|>
name|jaxbAnns
init|=
name|WrapperUtil
operator|.
name|getJaxbAnnotations
argument_list|(
name|method
argument_list|,
name|idx
argument_list|)
decl_stmt|;
name|field
operator|.
name|setJaxbAnnotations
argument_list|(
name|jaxbAnns
operator|.
name|toArray
argument_list|(
operator|new
name|Annotation
index|[
name|jaxbAnns
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|fields
operator|.
name|add
argument_list|(
name|field
argument_list|)
expr_stmt|;
block|}
return|return
name|fields
return|;
block|}
specifier|private
name|WebParam
name|getWebParamAnnotation
parameter_list|(
specifier|final
name|Annotation
index|[]
name|annotations
parameter_list|)
block|{
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
operator|instanceof
name|WebParam
condition|)
block|{
return|return
operator|(
name|WebParam
operator|)
name|annotation
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|WrapperBeanClass
name|getWrapperBeanClass
parameter_list|(
specifier|final
name|Method
name|method
parameter_list|)
block|{
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|RequestWrapper
name|reqWrapper
init|=
name|method
operator|.
name|getAnnotation
argument_list|(
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|RequestWrapper
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|reqClassName
init|=
name|getClassName
argument_list|()
decl_stmt|;
name|String
name|reqNs
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|reqWrapper
operator|!=
literal|null
condition|)
block|{
name|reqClassName
operator|=
name|reqWrapper
operator|.
name|className
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|?
name|reqWrapper
operator|.
name|className
argument_list|()
else|:
name|reqClassName
expr_stmt|;
name|reqNs
operator|=
name|reqWrapper
operator|.
name|targetNamespace
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|?
name|reqWrapper
operator|.
name|targetNamespace
argument_list|()
else|:
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|reqClassName
operator|==
literal|null
condition|)
block|{
name|reqClassName
operator|=
name|getPackageName
argument_list|(
name|method
argument_list|)
operator|+
literal|".jaxws."
operator|+
name|StringUtils
operator|.
name|capitalize
argument_list|(
name|method
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|WrapperBeanClass
name|jClass
init|=
operator|new
name|WrapperBeanClass
argument_list|()
decl_stmt|;
name|jClass
operator|.
name|setFullClassName
argument_list|(
name|reqClassName
argument_list|)
expr_stmt|;
name|jClass
operator|.
name|setNamespace
argument_list|(
name|reqNs
argument_list|)
expr_stmt|;
return|return
name|jClass
return|;
block|}
block|}
end_class

end_unit

