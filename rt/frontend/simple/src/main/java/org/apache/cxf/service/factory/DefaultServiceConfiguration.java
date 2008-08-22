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
name|service
operator|.
name|factory
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
name|util
operator|.
name|Arrays
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
name|util
operator|.
name|ParamReader
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
name|helpers
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
name|message
operator|.
name|Exchange
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
name|InterfaceInfo
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

begin_class
specifier|public
class|class
name|DefaultServiceConfiguration
extends|extends
name|AbstractServiceConfiguration
block|{
specifier|public
name|DefaultServiceConfiguration
parameter_list|()
block|{              }
annotation|@
name|Override
specifier|public
name|QName
name|getOperationName
parameter_list|(
name|InterfaceInfo
name|service
parameter_list|,
name|Method
name|method
parameter_list|)
block|{
name|boolean
name|fromWsdl
init|=
name|this
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|isFromWsdl
argument_list|()
decl_stmt|;
name|String
name|ns
init|=
name|service
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
name|String
name|local
init|=
name|method
operator|.
name|getName
argument_list|()
decl_stmt|;
name|QName
name|name
init|=
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
name|local
argument_list|)
decl_stmt|;
if|if
condition|(
name|fromWsdl
operator|&&
name|service
operator|.
name|getOperation
argument_list|(
name|name
argument_list|)
operator|!=
literal|null
condition|)
block|{
comment|//just matching the ops in the class to the ops on the wsdl
comment|//probably should check the params and such
return|return
name|name
return|;
block|}
if|if
condition|(
name|service
operator|.
name|getOperation
argument_list|(
name|name
argument_list|)
operator|==
literal|null
condition|)
block|{
return|return
name|name
return|;
block|}
name|int
name|i
init|=
literal|1
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|name
operator|=
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
name|local
operator|+
name|i
argument_list|)
expr_stmt|;
if|if
condition|(
name|service
operator|.
name|getOperation
argument_list|(
name|name
argument_list|)
operator|==
literal|null
condition|)
block|{
return|return
name|name
return|;
block|}
else|else
block|{
name|i
operator|++
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|QName
name|getFaultName
parameter_list|(
name|InterfaceInfo
name|service
parameter_list|,
name|OperationInfo
name|o
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|exClass
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|beanClass
parameter_list|)
block|{
name|String
name|name
init|=
name|ServiceUtils
operator|.
name|makeServiceNameFromClassName
argument_list|(
name|beanClass
argument_list|)
decl_stmt|;
return|return
operator|new
name|QName
argument_list|(
name|service
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|QName
name|getInPartName
parameter_list|(
name|OperationInfo
name|op
parameter_list|,
name|Method
name|method
parameter_list|,
name|int
name|paramNumber
parameter_list|)
block|{
return|return
name|getInParameterName
argument_list|(
name|op
argument_list|,
name|method
argument_list|,
name|paramNumber
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|QName
name|getOutPartName
parameter_list|(
name|OperationInfo
name|op
parameter_list|,
name|Method
name|method
parameter_list|,
name|int
name|paramNumber
parameter_list|)
block|{
return|return
name|getOutParameterName
argument_list|(
name|op
argument_list|,
name|method
argument_list|,
name|paramNumber
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|QName
name|getInParameterName
parameter_list|(
name|OperationInfo
name|op
parameter_list|,
name|Method
name|method
parameter_list|,
name|int
name|paramNumber
parameter_list|)
block|{
return|return
operator|new
name|QName
argument_list|(
name|op
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|getDefaultLocalName
argument_list|(
name|op
argument_list|,
name|method
argument_list|,
name|paramNumber
argument_list|,
literal|"arg"
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|QName
name|getInputMessageName
parameter_list|(
name|OperationInfo
name|op
parameter_list|,
name|Method
name|method
parameter_list|)
block|{
return|return
operator|new
name|QName
argument_list|(
name|op
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|op
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|QName
name|getOutParameterName
parameter_list|(
name|OperationInfo
name|op
parameter_list|,
name|Method
name|method
parameter_list|,
name|int
name|paramNumber
parameter_list|)
block|{
return|return
operator|new
name|QName
argument_list|(
name|op
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|getDefaultLocalName
argument_list|(
name|op
argument_list|,
name|method
argument_list|,
name|paramNumber
argument_list|,
literal|"return"
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|String
name|getDefaultLocalName
parameter_list|(
name|OperationInfo
name|op
parameter_list|,
name|Method
name|method
parameter_list|,
name|int
name|paramNumber
parameter_list|,
name|String
name|prefix
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|impl
init|=
name|getServiceFactory
argument_list|()
operator|.
name|getServiceClass
argument_list|()
decl_stmt|;
comment|// try to grab the implementation class so we can read the debug symbols from it
if|if
condition|(
name|impl
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|method
operator|=
name|impl
operator|.
name|getMethod
argument_list|(
name|method
operator|.
name|getName
argument_list|()
argument_list|,
name|method
operator|.
name|getParameterTypes
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ServiceConstructionException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|DefaultServiceConfiguration
operator|.
name|createName
argument_list|(
name|method
argument_list|,
name|paramNumber
argument_list|,
name|op
operator|.
name|getInput
argument_list|()
operator|.
name|getMessageParts
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|false
argument_list|,
name|prefix
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|createName
parameter_list|(
specifier|final
name|Method
name|method
parameter_list|,
specifier|final
name|int
name|paramNumber
parameter_list|,
specifier|final
name|int
name|currentSize
parameter_list|,
name|boolean
name|addMethodName
parameter_list|,
specifier|final
name|String
name|flow
parameter_list|)
block|{
name|String
name|paramName
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|paramNumber
operator|!=
operator|-
literal|1
condition|)
block|{
name|String
index|[]
name|names
init|=
name|ParamReader
operator|.
name|getParameterNamesFromDebugInfo
argument_list|(
name|method
argument_list|)
decl_stmt|;
comment|// get the specific parameter name from the parameter Number
if|if
condition|(
name|names
operator|!=
literal|null
operator|&&
name|names
index|[
name|paramNumber
index|]
operator|!=
literal|null
condition|)
block|{
name|paramName
operator|=
name|names
index|[
name|paramNumber
index|]
expr_stmt|;
name|addMethodName
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|paramName
operator|=
name|flow
operator|+
name|currentSize
expr_stmt|;
block|}
block|}
else|else
block|{
name|paramName
operator|=
name|flow
expr_stmt|;
block|}
name|paramName
operator|=
name|addMethodName
condition|?
name|method
operator|.
name|getName
argument_list|()
operator|+
name|paramName
else|:
name|paramName
expr_stmt|;
return|return
name|paramName
return|;
block|}
annotation|@
name|Override
specifier|public
name|QName
name|getOutputMessageName
parameter_list|(
name|OperationInfo
name|op
parameter_list|,
name|Method
name|method
parameter_list|)
block|{
return|return
operator|new
name|QName
argument_list|(
name|op
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|op
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|"Response"
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|QName
name|getInterfaceName
parameter_list|()
block|{
return|return
operator|new
name|QName
argument_list|(
name|getServiceFactory
argument_list|()
operator|.
name|getServiceNamespace
argument_list|()
argument_list|,
name|getServiceName
argument_list|()
operator|+
literal|"PortType"
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|QName
name|getEndpointName
parameter_list|()
block|{
return|return
operator|new
name|QName
argument_list|(
name|getServiceFactory
argument_list|()
operator|.
name|getServiceNamespace
argument_list|()
argument_list|,
name|getServiceName
argument_list|()
operator|+
literal|"Port"
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getServiceName
parameter_list|()
block|{
return|return
name|getServiceFactory
argument_list|()
operator|.
name|getServiceClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getServiceNamespace
parameter_list|()
block|{
name|String
name|ret
init|=
name|super
operator|.
name|getServiceNamespace
argument_list|()
decl_stmt|;
if|if
condition|(
name|ret
operator|==
literal|null
operator|&&
name|getServiceFactory
argument_list|()
operator|!=
literal|null
operator|&&
name|getServiceFactory
argument_list|()
operator|.
name|getServiceClass
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ret
operator|=
name|ServiceUtils
operator|.
name|makeNamespaceFromClassName
argument_list|(
name|getServiceFactory
argument_list|()
operator|.
name|getServiceClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|"http"
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
annotation|@
name|Override
specifier|public
name|Boolean
name|hasOutMessage
parameter_list|(
name|Method
name|m
parameter_list|)
block|{
if|if
condition|(
name|m
operator|.
name|getReturnType
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|equals
argument_list|(
name|void
operator|.
name|class
argument_list|)
operator|&&
name|m
operator|.
name|getExceptionTypes
argument_list|()
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|Boolean
name|isAsync
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
return|return
name|Boolean
operator|.
name|FALSE
return|;
block|}
annotation|@
name|Override
specifier|public
name|Boolean
name|isHeader
parameter_list|(
name|Method
name|method
parameter_list|,
name|int
name|j
parameter_list|)
block|{
return|return
name|Boolean
operator|.
name|FALSE
return|;
block|}
annotation|@
name|Override
specifier|public
name|Boolean
name|isInParam
parameter_list|(
name|Method
name|method
parameter_list|,
name|int
name|j
parameter_list|)
block|{
if|if
condition|(
name|j
operator|>=
literal|0
condition|)
block|{
name|Class
name|c
init|=
name|method
operator|.
name|getParameterTypes
argument_list|()
index|[
name|j
index|]
decl_stmt|;
if|if
condition|(
name|Exchange
operator|.
name|class
operator|.
name|equals
argument_list|(
name|c
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|Boolean
name|isOperation
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
if|if
condition|(
name|getServiceFactory
argument_list|()
operator|.
name|getIgnoredClasses
argument_list|()
operator|.
name|contains
argument_list|(
name|method
operator|.
name|getDeclaringClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|Boolean
operator|.
name|FALSE
return|;
block|}
comment|// Don't do m.equals(method)
for|for
control|(
name|Method
name|m
range|:
name|getServiceFactory
argument_list|()
operator|.
name|getIgnoredMethods
argument_list|()
control|)
block|{
if|if
condition|(
name|m
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|method
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|Arrays
operator|.
name|equals
argument_list|(
name|method
operator|.
name|getParameterTypes
argument_list|()
argument_list|,
name|m
operator|.
name|getParameterTypes
argument_list|()
argument_list|)
operator|&&
name|m
operator|.
name|getReturnType
argument_list|()
operator|==
name|method
operator|.
name|getReturnType
argument_list|()
condition|)
block|{
return|return
name|Boolean
operator|.
name|FALSE
return|;
block|}
block|}
specifier|final
name|int
name|modifiers
init|=
name|method
operator|.
name|getModifiers
argument_list|()
decl_stmt|;
if|if
condition|(
name|Modifier
operator|.
name|isPublic
argument_list|(
name|modifiers
argument_list|)
operator|&&
operator|!
name|Modifier
operator|.
name|isStatic
argument_list|(
name|modifiers
argument_list|)
condition|)
block|{
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
return|return
name|Boolean
operator|.
name|FALSE
return|;
block|}
annotation|@
name|Override
specifier|public
name|Boolean
name|isOutParam
parameter_list|(
name|Method
name|method
parameter_list|,
name|int
name|j
parameter_list|)
block|{
if|if
condition|(
name|j
operator|<
literal|0
condition|)
block|{
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
name|method
operator|.
name|getParameterTypes
argument_list|()
index|[
name|j
index|]
decl_stmt|;
name|Type
name|tp
init|=
name|method
operator|.
name|getGenericParameterTypes
argument_list|()
index|[
name|j
index|]
decl_stmt|;
return|return
name|isHolder
argument_list|(
name|cls
argument_list|,
name|tp
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Boolean
name|isWrapped
parameter_list|(
name|Method
name|m
parameter_list|)
block|{
return|return
name|getServiceFactory
argument_list|()
operator|.
name|isWrapped
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Boolean
name|isHolder
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|Type
name|type
parameter_list|)
block|{
if|if
condition|(
name|cls
operator|.
name|getSimpleName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"Holder"
argument_list|)
operator|&&
name|cls
operator|.
name|getDeclaredFields
argument_list|()
operator|.
name|length
operator|==
literal|1
operator|&&
literal|"value"
operator|.
name|equals
argument_list|(
name|cls
operator|.
name|getDeclaredFields
argument_list|()
index|[
literal|0
index|]
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|Modifier
operator|.
name|isPublic
argument_list|(
name|cls
operator|.
name|getDeclaredFields
argument_list|()
index|[
literal|0
index|]
operator|.
name|getModifiers
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
return|return
name|Boolean
operator|.
name|FALSE
return|;
block|}
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getHolderType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|Type
name|type
parameter_list|)
block|{
if|if
condition|(
name|isHolder
argument_list|(
name|cls
argument_list|,
name|type
argument_list|)
condition|)
block|{
if|if
condition|(
name|type
operator|instanceof
name|ParameterizedType
condition|)
block|{
comment|//JAX-WS style using generics
name|ParameterizedType
name|paramType
init|=
operator|(
name|ParameterizedType
operator|)
name|type
decl_stmt|;
name|cls
operator|=
name|getHolderClass
argument_list|(
name|paramType
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//JAXRPC style of code generated holder
return|return
name|cls
operator|.
name|getDeclaredFields
argument_list|()
index|[
literal|0
index|]
operator|.
name|getType
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
specifier|static
name|Class
name|getHolderClass
parameter_list|(
name|ParameterizedType
name|paramType
parameter_list|)
block|{
name|Object
name|rawType
init|=
name|paramType
operator|.
name|getActualTypeArguments
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
name|Class
name|rawClass
decl_stmt|;
if|if
condition|(
name|rawType
operator|instanceof
name|GenericArrayType
condition|)
block|{
name|rawClass
operator|=
call|(
name|Class
call|)
argument_list|(
operator|(
name|GenericArrayType
operator|)
name|rawType
argument_list|)
operator|.
name|getGenericComponentType
argument_list|()
expr_stmt|;
name|rawClass
operator|=
name|Array
operator|.
name|newInstance
argument_list|(
name|rawClass
argument_list|,
literal|0
argument_list|)
operator|.
name|getClass
argument_list|()
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|rawType
operator|instanceof
name|ParameterizedType
condition|)
block|{
name|rawType
operator|=
call|(
name|Class
call|)
argument_list|(
operator|(
name|ParameterizedType
operator|)
name|rawType
argument_list|)
operator|.
name|getRawType
argument_list|()
expr_stmt|;
block|}
name|rawClass
operator|=
operator|(
name|Class
operator|)
name|rawType
expr_stmt|;
block|}
return|return
name|rawClass
return|;
block|}
specifier|public
name|Boolean
name|isWrapperPartNillable
parameter_list|(
name|MessagePartInfo
name|mpi
parameter_list|)
block|{
return|return
operator|(
name|Boolean
operator|)
name|mpi
operator|.
name|getProperty
argument_list|(
literal|"nillable"
argument_list|)
return|;
block|}
specifier|public
name|Long
name|getWrapperPartMaxOccurs
parameter_list|(
name|MessagePartInfo
name|mpi
parameter_list|)
block|{
name|String
name|max
init|=
operator|(
name|String
operator|)
name|mpi
operator|.
name|getProperty
argument_list|(
literal|"maxOccurs"
argument_list|)
decl_stmt|;
name|long
name|maxi
init|=
literal|1
decl_stmt|;
if|if
condition|(
name|max
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|mpi
operator|.
name|getTypeClass
argument_list|()
operator|!=
literal|null
operator|&&
name|mpi
operator|.
name|getTypeClass
argument_list|()
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
name|mpi
operator|.
name|getTypeClass
argument_list|()
operator|.
name|getComponentType
argument_list|()
argument_list|)
condition|)
block|{
name|maxi
operator|=
name|Long
operator|.
name|MAX_VALUE
expr_stmt|;
block|}
block|}
else|else
block|{
name|maxi
operator|=
literal|"unbounded"
operator|.
name|equals
argument_list|(
name|max
argument_list|)
condition|?
name|Long
operator|.
name|MAX_VALUE
else|:
name|Long
operator|.
name|parseLong
argument_list|(
name|max
argument_list|)
expr_stmt|;
block|}
return|return
name|maxi
return|;
block|}
specifier|public
name|Long
name|getWrapperPartMinOccurs
parameter_list|(
name|MessagePartInfo
name|mpi
parameter_list|)
block|{
name|String
name|min
init|=
operator|(
name|String
operator|)
name|mpi
operator|.
name|getProperty
argument_list|(
literal|"minOccurs"
argument_list|)
decl_stmt|;
name|long
name|mini
init|=
literal|1
decl_stmt|;
if|if
condition|(
name|min
operator|!=
literal|null
condition|)
block|{
name|mini
operator|=
name|Long
operator|.
name|parseLong
argument_list|(
name|min
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|min
operator|==
literal|null
operator|&&
name|mpi
operator|.
name|getTypeClass
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|mpi
operator|.
name|getTypeClass
argument_list|()
operator|.
name|isPrimitive
argument_list|()
condition|)
block|{
name|mini
operator|=
literal|0
expr_stmt|;
block|}
return|return
name|mini
return|;
block|}
block|}
end_class

end_unit

