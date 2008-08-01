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
specifier|abstract
class|class
name|AbstractServiceConfiguration
block|{
specifier|protected
name|String
name|serviceNamespace
decl_stmt|;
specifier|private
name|ReflectionServiceFactoryBean
name|serviceFactory
decl_stmt|;
specifier|public
name|ReflectionServiceFactoryBean
name|getServiceFactory
parameter_list|()
block|{
return|return
name|serviceFactory
return|;
block|}
specifier|public
name|void
name|setServiceFactory
parameter_list|(
name|ReflectionServiceFactoryBean
name|serviceFactory
parameter_list|)
block|{
name|this
operator|.
name|serviceFactory
operator|=
name|serviceFactory
expr_stmt|;
block|}
specifier|public
name|String
name|getWsdlURL
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getServiceName
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getServiceNamespace
parameter_list|()
block|{
return|return
name|serviceNamespace
return|;
block|}
specifier|public
name|void
name|setServiceNamespace
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|serviceNamespace
operator|=
name|s
expr_stmt|;
block|}
specifier|public
name|Boolean
name|isOperation
parameter_list|(
specifier|final
name|Method
name|method
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getStyle
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Boolean
name|isWrapped
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Boolean
name|isWrapped
parameter_list|(
name|Method
name|m
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
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
return|return
literal|null
return|;
block|}
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
return|return
literal|null
return|;
block|}
specifier|public
name|QName
name|getInputMessageName
parameter_list|(
specifier|final
name|OperationInfo
name|op
parameter_list|,
name|Method
name|method
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|QName
name|getOutputMessageName
parameter_list|(
specifier|final
name|OperationInfo
name|op
parameter_list|,
name|Method
name|method
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Boolean
name|hasOutMessage
parameter_list|(
name|Method
name|m
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
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
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getAction
parameter_list|(
name|OperationInfo
name|op
parameter_list|,
name|Method
name|method
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
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
literal|null
return|;
block|}
comment|/**      * Creates a name for the operation from the method name. If an operation      * with that name already exists, a name is create by appending an integer      * to the end. I.e. if there is already two methods named      *<code>doSomething</code>, the first one will have an operation name of      * "doSomething" and the second "doSomething1".      *       * @param service      * @param method      */
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
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getMEP
parameter_list|(
specifier|final
name|Method
name|method
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Boolean
name|isAsync
parameter_list|(
specifier|final
name|Method
name|method
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|QName
name|getInParameterName
parameter_list|(
specifier|final
name|OperationInfo
name|op
parameter_list|,
specifier|final
name|Method
name|method
parameter_list|,
specifier|final
name|int
name|paramNumber
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|QName
name|getOutParameterName
parameter_list|(
specifier|final
name|OperationInfo
name|op
parameter_list|,
specifier|final
name|Method
name|method
parameter_list|,
specifier|final
name|int
name|paramNumber
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|QName
name|getInPartName
parameter_list|(
specifier|final
name|OperationInfo
name|op
parameter_list|,
specifier|final
name|Method
name|method
parameter_list|,
specifier|final
name|int
name|paramNumber
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|QName
name|getOutPartName
parameter_list|(
specifier|final
name|OperationInfo
name|op
parameter_list|,
specifier|final
name|Method
name|method
parameter_list|,
specifier|final
name|int
name|paramNumber
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|QName
name|getInterfaceName
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|QName
name|getEndpointName
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|QName
name|getRequestWrapperName
parameter_list|(
name|OperationInfo
name|op
parameter_list|,
name|Method
name|method
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|QName
name|getResponseWrapperName
parameter_list|(
name|OperationInfo
name|op
parameter_list|,
name|Method
name|method
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getResponseWrapperPartName
parameter_list|(
name|OperationInfo
name|op
parameter_list|,
name|Method
name|method
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Class
name|getResponseWrapper
parameter_list|(
name|Method
name|selected
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Class
name|getRequestWrapper
parameter_list|(
name|Method
name|selected
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getResponseWrapperClassName
parameter_list|(
name|Method
name|selected
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getRequestWrapperClassName
parameter_list|(
name|Method
name|selected
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Boolean
name|isRPC
parameter_list|(
name|Method
name|selected
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
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
return|return
literal|null
return|;
block|}
specifier|public
name|Type
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
return|return
literal|null
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
literal|null
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
comment|//return Long.MAX_VALUE for unbounded
return|return
literal|null
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
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

