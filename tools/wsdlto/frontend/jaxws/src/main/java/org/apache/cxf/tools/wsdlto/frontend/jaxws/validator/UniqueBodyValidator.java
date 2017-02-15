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
name|Collection
import|;
end_import

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
name|HashSet
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
name|Set
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
name|BindingFaultInfo
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
name|BindingInfo
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
name|BindingOperationInfo
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
name|EndpointInfo
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
name|validator
operator|.
name|ServiceValidator
import|;
end_import

begin_class
specifier|public
class|class
name|UniqueBodyValidator
extends|extends
name|ServiceValidator
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|UniqueBodyValidator
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|UniqueBodyValidator
parameter_list|()
block|{     }
specifier|public
name|UniqueBodyValidator
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
name|checkUniqueBody
argument_list|()
return|;
block|}
specifier|private
name|boolean
name|checkUniqueBody
parameter_list|()
block|{
name|Collection
argument_list|<
name|EndpointInfo
argument_list|>
name|endpoints
init|=
name|service
operator|.
name|getEndpoints
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpoints
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|EndpointInfo
name|endpoint
range|:
name|endpoints
control|)
block|{
if|if
condition|(
operator|!
name|isValidEndpoint
argument_list|(
name|endpoint
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
name|isValidEndpoint
parameter_list|(
name|EndpointInfo
name|endpoint
parameter_list|)
block|{
name|BindingInfo
name|binding
init|=
name|endpoint
operator|.
name|getBinding
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|QName
argument_list|,
name|QName
argument_list|>
name|uniqueNames
init|=
operator|new
name|HashMap
argument_list|<
name|QName
argument_list|,
name|QName
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|QName
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|actions
init|=
operator|new
name|HashMap
argument_list|<
name|QName
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|BindingOperationInfo
argument_list|>
name|bos
init|=
name|binding
operator|.
name|getOperations
argument_list|()
decl_stmt|;
for|for
control|(
name|BindingOperationInfo
name|bo
range|:
name|bos
control|)
block|{
name|OperationInfo
name|op
init|=
name|binding
operator|.
name|getInterface
argument_list|()
operator|.
name|getOperation
argument_list|(
name|bo
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|op
operator|.
name|getInput
argument_list|()
operator|!=
literal|null
operator|&&
name|op
operator|.
name|getInput
argument_list|()
operator|.
name|getMessagePartsNumber
argument_list|()
operator|==
literal|1
condition|)
block|{
name|MessagePartInfo
name|part
init|=
name|op
operator|.
name|getInput
argument_list|()
operator|.
name|getFirstMessagePart
argument_list|()
decl_stmt|;
if|if
condition|(
name|part
operator|.
name|getElementQName
argument_list|()
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|QName
name|mName
init|=
name|part
operator|.
name|getElementQName
argument_list|()
decl_stmt|;
name|String
name|action
init|=
name|getWSAAction
argument_list|(
name|op
operator|.
name|getInput
argument_list|()
argument_list|)
decl_stmt|;
name|QName
name|opName
init|=
name|uniqueNames
operator|.
name|get
argument_list|(
name|mName
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|opActions
init|=
name|actions
operator|.
name|get
argument_list|(
name|mName
argument_list|)
decl_stmt|;
if|if
condition|(
name|opName
operator|!=
literal|null
operator|&&
name|opActions
operator|!=
literal|null
operator|&&
operator|!
name|opActions
operator|.
name|contains
argument_list|(
name|action
argument_list|)
condition|)
block|{
name|opName
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|opName
operator|!=
literal|null
condition|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"NON_UNIQUE_BODY"
argument_list|,
name|LOG
argument_list|,
name|endpoint
operator|.
name|getName
argument_list|()
argument_list|,
name|op
operator|.
name|getName
argument_list|()
argument_list|,
name|opName
argument_list|,
name|mName
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
return|return
literal|false
return|;
block|}
else|else
block|{
name|uniqueNames
operator|.
name|put
argument_list|(
name|mName
argument_list|,
name|op
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|action
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|opActions
operator|==
literal|null
condition|)
block|{
name|opActions
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
name|actions
operator|.
name|put
argument_list|(
name|mName
argument_list|,
name|opActions
argument_list|)
expr_stmt|;
block|}
name|opActions
operator|.
name|add
argument_list|(
name|action
argument_list|)
expr_stmt|;
block|}
block|}
block|}
for|for
control|(
name|BindingFaultInfo
name|fault
range|:
name|bo
operator|.
name|getFaults
argument_list|()
control|)
block|{
if|if
condition|(
name|fault
operator|.
name|getFaultInfo
argument_list|()
operator|.
name|getMessagePartsNumber
argument_list|()
operator|>
literal|1
condition|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"FAULT_WITH_MULTIPLE_PARTS"
argument_list|,
name|LOG
argument_list|,
name|fault
operator|.
name|getFaultInfo
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
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
name|String
name|getWSAAction
parameter_list|(
name|MessageInfo
name|input
parameter_list|)
block|{
if|if
condition|(
name|input
operator|.
name|getExtensionAttributes
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|QName
argument_list|,
name|Object
argument_list|>
name|ent
range|:
name|input
operator|.
name|getExtensionAttributes
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
literal|"Action"
operator|.
name|equals
argument_list|(
name|ent
operator|.
name|getKey
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|ent
operator|.
name|getValue
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

