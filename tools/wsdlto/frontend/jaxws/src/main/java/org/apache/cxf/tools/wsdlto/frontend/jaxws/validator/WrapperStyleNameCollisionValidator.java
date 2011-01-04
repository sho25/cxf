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
name|wsdlto
operator|.
name|frontend
operator|.
name|jaxws
operator|.
name|validator
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
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
name|tools
operator|.
name|common
operator|.
name|ToolConstants
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
name|ToolContext
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
name|validator
operator|.
name|ServiceValidator
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
name|wsdlto
operator|.
name|frontend
operator|.
name|jaxws
operator|.
name|customization
operator|.
name|JAXWSBinding
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
name|wsdlto
operator|.
name|frontend
operator|.
name|jaxws
operator|.
name|customization
operator|.
name|JAXWSParameter
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
name|wsdlto
operator|.
name|frontend
operator|.
name|jaxws
operator|.
name|processor
operator|.
name|internal
operator|.
name|ProcessorUtil
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
name|wsdlto
operator|.
name|frontend
operator|.
name|jaxws
operator|.
name|processor
operator|.
name|internal
operator|.
name|WrapperElement
import|;
end_import

begin_class
specifier|public
class|class
name|WrapperStyleNameCollisionValidator
extends|extends
name|ServiceValidator
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
name|WrapperStyleNameCollisionValidator
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|WrapperStyleNameCollisionValidator
parameter_list|()
block|{     }
specifier|public
name|WrapperStyleNameCollisionValidator
parameter_list|(
name|ServiceInfo
name|s
parameter_list|)
block|{
name|this
operator|.
name|service
operator|=
name|s
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isValid
parameter_list|()
block|{
return|return
name|checkNameCollision
argument_list|()
return|;
block|}
specifier|private
name|boolean
name|checkNameCollision
parameter_list|()
block|{
name|InterfaceInfo
name|interfaceInfo
init|=
name|service
operator|.
name|getInterface
argument_list|()
decl_stmt|;
if|if
condition|(
name|interfaceInfo
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|OperationInfo
name|operation
range|:
name|interfaceInfo
operator|.
name|getOperations
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|isValidOperation
argument_list|(
name|operation
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|boolean
name|isValidOperation
parameter_list|(
name|OperationInfo
name|operation
parameter_list|)
block|{
name|ToolContext
name|context
init|=
name|service
operator|.
name|getProperty
argument_list|(
name|ToolContext
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|ToolContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|boolean
name|c
init|=
name|context
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_AUTORESOLVE
argument_list|)
decl_stmt|;
name|boolean
name|valid
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|operation
operator|.
name|getUnwrappedOperation
argument_list|()
operator|==
literal|null
condition|)
block|{
name|valid
operator|=
literal|true
expr_stmt|;
block|}
name|JAXWSBinding
name|binding
init|=
operator|(
name|JAXWSBinding
operator|)
name|operation
operator|.
name|getExtensor
argument_list|(
name|JAXWSBinding
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|binding
operator|!=
literal|null
operator|&&
operator|!
name|binding
operator|.
name|isEnableWrapperStyle
argument_list|()
condition|)
block|{
name|valid
operator|=
literal|true
expr_stmt|;
block|}
name|binding
operator|=
name|operation
operator|.
name|getInterface
argument_list|()
operator|.
name|getExtensor
argument_list|(
name|JAXWSBinding
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|binding
operator|!=
literal|null
operator|&&
operator|!
name|binding
operator|.
name|isEnableWrapperStyle
argument_list|()
condition|)
block|{
name|valid
operator|=
literal|true
expr_stmt|;
block|}
name|binding
operator|=
name|operation
operator|.
name|getInterface
argument_list|()
operator|.
name|getService
argument_list|()
operator|.
name|getDescription
argument_list|()
operator|.
name|getExtensor
argument_list|(
name|JAXWSBinding
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|binding
operator|!=
literal|null
operator|&&
operator|!
name|binding
operator|.
name|isEnableWrapperStyle
argument_list|()
condition|)
block|{
name|valid
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|valid
condition|)
block|{
return|return
literal|true
return|;
block|}
name|MessagePartInfo
name|input
init|=
literal|null
decl_stmt|;
name|MessagePartInfo
name|output
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|operation
operator|.
name|getInput
argument_list|()
operator|!=
literal|null
operator|&&
name|operation
operator|.
name|getInput
argument_list|()
operator|.
name|getMessageParts
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|input
operator|=
name|operation
operator|.
name|getInput
argument_list|()
operator|.
name|getMessageParts
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|operation
operator|.
name|getOutput
argument_list|()
operator|!=
literal|null
operator|&&
name|operation
operator|.
name|getOutput
argument_list|()
operator|.
name|getMessageParts
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|output
operator|=
name|operation
operator|.
name|getOutput
argument_list|()
operator|.
name|getMessageParts
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|c
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|QName
argument_list|>
name|names
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|QName
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|input
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|WrapperElement
name|element
range|:
name|ProcessorUtil
operator|.
name|getWrappedElement
argument_list|(
name|context
argument_list|,
name|input
operator|.
name|getElementQName
argument_list|()
argument_list|)
control|)
block|{
name|String
name|mappedName
init|=
name|mapElementName
argument_list|(
name|operation
argument_list|,
name|operation
operator|.
name|getUnwrappedOperation
argument_list|()
operator|.
name|getInput
argument_list|()
argument_list|,
name|element
argument_list|)
decl_stmt|;
if|if
condition|(
name|names
operator|.
name|containsKey
argument_list|(
name|mappedName
argument_list|)
operator|&&
operator|(
name|names
operator|.
name|get
argument_list|(
name|mappedName
argument_list|)
operator|==
name|element
operator|.
name|getSchemaTypeName
argument_list|()
operator|||
name|names
operator|.
name|get
argument_list|(
name|mappedName
argument_list|)
operator|.
name|equals
argument_list|(
name|element
operator|.
name|getSchemaTypeName
argument_list|()
argument_list|)
operator|)
condition|)
block|{
name|handleErrors
argument_list|(
name|names
operator|.
name|get
argument_list|(
name|mappedName
argument_list|)
argument_list|,
name|element
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
else|else
block|{
name|names
operator|.
name|put
argument_list|(
name|mappedName
argument_list|,
name|element
operator|.
name|getSchemaTypeName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|output
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|WrapperElement
argument_list|>
name|els
init|=
name|ProcessorUtil
operator|.
name|getWrappedElement
argument_list|(
name|context
argument_list|,
name|output
operator|.
name|getElementQName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|els
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
for|for
control|(
name|WrapperElement
name|element
range|:
name|els
control|)
block|{
name|String
name|mappedName
init|=
name|mapElementName
argument_list|(
name|operation
argument_list|,
name|operation
operator|.
name|getUnwrappedOperation
argument_list|()
operator|.
name|getOutput
argument_list|()
argument_list|,
name|element
argument_list|)
decl_stmt|;
if|if
condition|(
name|names
operator|.
name|containsKey
argument_list|(
name|mappedName
argument_list|)
operator|&&
operator|!
operator|(
name|names
operator|.
name|get
argument_list|(
name|mappedName
argument_list|)
operator|==
name|element
operator|.
name|getSchemaTypeName
argument_list|()
operator|||
name|names
operator|.
name|get
argument_list|(
name|mappedName
argument_list|)
operator|.
name|equals
argument_list|(
name|element
operator|.
name|getSchemaTypeName
argument_list|()
argument_list|)
operator|)
condition|)
block|{
name|handleErrors
argument_list|(
name|names
operator|.
name|get
argument_list|(
name|mappedName
argument_list|)
argument_list|,
name|element
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
else|else
block|{
name|names
operator|.
name|put
argument_list|(
name|mappedName
argument_list|,
name|element
operator|.
name|getSchemaTypeName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|String
name|mapElementName
parameter_list|(
name|OperationInfo
name|op
parameter_list|,
name|MessageInfo
name|mi
parameter_list|,
name|WrapperElement
name|element
parameter_list|)
block|{
name|MessagePartInfo
name|mpi
init|=
name|mi
operator|.
name|getMessagePart
argument_list|(
name|element
operator|.
name|getElementName
argument_list|()
argument_list|)
decl_stmt|;
name|JAXWSBinding
name|bind
init|=
name|op
operator|.
name|getExtensor
argument_list|(
name|JAXWSBinding
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|bind
operator|!=
literal|null
operator|&&
name|bind
operator|.
name|getJaxwsParas
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|JAXWSParameter
name|par
range|:
name|bind
operator|.
name|getJaxwsParas
argument_list|()
control|)
block|{
if|if
condition|(
name|mi
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|.
name|equals
argument_list|(
name|par
operator|.
name|getMessageName
argument_list|()
argument_list|)
operator|&&
name|mpi
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|.
name|equals
argument_list|(
name|par
operator|.
name|getElementName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|par
operator|.
name|getName
argument_list|()
return|;
block|}
block|}
block|}
return|return
name|mpi
operator|.
name|getElementQName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
return|;
block|}
specifier|private
name|void
name|handleErrors
parameter_list|(
name|QName
name|e1
parameter_list|,
name|WrapperElement
name|e2
parameter_list|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"WRAPPER_STYLE_NAME_COLLISION"
argument_list|,
name|LOG
argument_list|,
name|e2
operator|.
name|getElementName
argument_list|()
argument_list|,
name|e1
argument_list|,
name|e2
operator|.
name|getSchemaTypeName
argument_list|()
argument_list|)
decl_stmt|;
name|addErrorMessage
argument_list|(
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

