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
name|CollectionUtils
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
specifier|final
class|class
name|ResponseWrapper
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
name|getOutput
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
name|getOutput
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
literal|"RESPONSE.WRAPPER.CLASSNAME"
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
name|ResponseWrapper
name|resWrapper
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
name|ResponseWrapper
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
name|resWrapper
operator|==
literal|null
operator|||
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|resWrapper
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
name|getOutput
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
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|returnType
init|=
name|method
operator|.
name|getReturnType
argument_list|()
decl_stmt|;
name|JavaField
name|field
init|=
operator|new
name|JavaField
argument_list|()
decl_stmt|;
if|if
condition|(
name|CollectionUtils
operator|.
name|isEmpty
argument_list|(
name|message
operator|.
name|getMessageParts
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|fields
return|;
block|}
name|MessagePartInfo
name|part
init|=
name|message
operator|.
name|getMessageParts
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|field
operator|.
name|setName
argument_list|(
name|part
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|boolean
name|hasReturnType
init|=
literal|false
decl_stmt|;
if|if
condition|(
operator|!
name|returnType
operator|.
name|isAssignableFrom
argument_list|(
name|void
operator|.
name|class
argument_list|)
condition|)
block|{
name|hasReturnType
operator|=
literal|true
expr_stmt|;
name|String
name|type
init|=
name|getTypeString
argument_list|(
name|method
operator|.
name|getGenericReturnType
argument_list|()
argument_list|)
decl_stmt|;
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
argument_list|)
decl_stmt|;
name|field
operator|.
name|setType
argument_list|(
name|type
argument_list|)
expr_stmt|;
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
name|field
operator|.
name|setTargetNamespace
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
name|fields
operator|.
name|add
argument_list|(
name|field
argument_list|)
expr_stmt|;
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
name|hasReturnType
condition|?
name|mpi
operator|.
name|getIndex
argument_list|()
operator|-
literal|1
else|:
name|mpi
operator|.
name|getIndex
argument_list|()
decl_stmt|;
if|if
condition|(
name|idx
operator|>=
literal|0
condition|)
block|{
name|String
name|name
init|=
name|mpi
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
decl_stmt|;
name|Type
name|t
init|=
name|paramClasses
index|[
name|idx
index|]
decl_stmt|;
name|String
name|type
init|=
name|getTypeString
argument_list|(
name|t
argument_list|)
decl_stmt|;
name|JavaField
name|jf
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
operator|-
literal|1
argument_list|)
decl_stmt|;
name|jf
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
operator|new
name|JavaField
argument_list|(
name|name
argument_list|,
name|type
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|fields
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
name|ResponseWrapper
name|resWrapper
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
name|ResponseWrapper
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|resClassName
init|=
name|getClassName
argument_list|()
decl_stmt|;
name|String
name|resNs
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|resWrapper
operator|!=
literal|null
condition|)
block|{
name|resClassName
operator|=
name|resWrapper
operator|.
name|className
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|?
name|resWrapper
operator|.
name|className
argument_list|()
else|:
name|resClassName
expr_stmt|;
name|resNs
operator|=
name|resWrapper
operator|.
name|targetNamespace
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|?
name|resWrapper
operator|.
name|targetNamespace
argument_list|()
else|:
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|resClassName
operator|==
literal|null
condition|)
block|{
name|resClassName
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
operator|+
literal|"Response"
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
name|resClassName
argument_list|)
expr_stmt|;
name|jClass
operator|.
name|setNamespace
argument_list|(
name|resNs
argument_list|)
expr_stmt|;
return|return
name|jClass
return|;
block|}
block|}
end_class

end_unit

