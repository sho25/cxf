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
name|model
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
name|Collections
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
name|concurrent
operator|.
name|ConcurrentHashMap
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

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|BindingOperationInfo
extends|extends
name|AbstractPropertiesHolder
block|{
specifier|protected
name|OperationInfo
name|opInfo
decl_stmt|;
name|BindingInfo
name|bindingInfo
decl_stmt|;
name|BindingMessageInfo
name|inputMessage
decl_stmt|;
name|BindingMessageInfo
name|outputMessage
decl_stmt|;
name|Map
argument_list|<
name|QName
argument_list|,
name|BindingFaultInfo
argument_list|>
name|faults
decl_stmt|;
name|BindingOperationInfo
name|opHolder
decl_stmt|;
specifier|public
name|BindingOperationInfo
parameter_list|()
block|{     }
specifier|public
name|BindingOperationInfo
parameter_list|(
name|BindingInfo
name|bi
parameter_list|,
name|OperationInfo
name|opinfo
parameter_list|)
block|{
name|bindingInfo
operator|=
name|bi
expr_stmt|;
name|opInfo
operator|=
name|opinfo
expr_stmt|;
if|if
condition|(
name|opInfo
operator|.
name|getInput
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|inputMessage
operator|=
operator|new
name|BindingMessageInfo
argument_list|(
name|opInfo
operator|.
name|getInput
argument_list|()
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|inputMessage
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|opInfo
operator|.
name|getOutput
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|outputMessage
operator|=
operator|new
name|BindingMessageInfo
argument_list|(
name|opInfo
operator|.
name|getOutput
argument_list|()
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|outputMessage
operator|=
literal|null
expr_stmt|;
block|}
name|Collection
argument_list|<
name|FaultInfo
argument_list|>
name|of
init|=
name|opinfo
operator|.
name|getFaults
argument_list|()
decl_stmt|;
if|if
condition|(
name|of
operator|!=
literal|null
operator|&&
operator|!
name|of
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|faults
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|QName
argument_list|,
name|BindingFaultInfo
argument_list|>
argument_list|(
name|of
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|FaultInfo
name|fault
range|:
name|of
control|)
block|{
name|faults
operator|.
name|put
argument_list|(
name|fault
operator|.
name|getFaultName
argument_list|()
argument_list|,
operator|new
name|BindingFaultInfo
argument_list|(
name|fault
argument_list|,
name|this
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|opinfo
operator|.
name|isUnwrappedCapable
argument_list|()
condition|)
block|{
name|opHolder
operator|=
operator|new
name|BindingOperationInfo
argument_list|(
name|bi
argument_list|,
name|opinfo
operator|.
name|getUnwrappedOperation
argument_list|()
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
block|}
name|BindingOperationInfo
parameter_list|(
name|BindingInfo
name|bi
parameter_list|,
name|OperationInfo
name|opinfo
parameter_list|,
name|BindingOperationInfo
name|wrapped
parameter_list|)
block|{
name|this
argument_list|(
name|bi
argument_list|,
name|opinfo
argument_list|)
expr_stmt|;
name|opHolder
operator|=
name|wrapped
expr_stmt|;
block|}
specifier|public
name|void
name|updateUnwrappedOperation
parameter_list|()
block|{
if|if
condition|(
name|opInfo
operator|.
name|isUnwrappedCapable
argument_list|()
operator|&&
name|opHolder
operator|==
literal|null
condition|)
block|{
name|opHolder
operator|=
operator|new
name|BindingOperationInfo
argument_list|(
name|bindingInfo
argument_list|,
name|opInfo
operator|.
name|getUnwrappedOperation
argument_list|()
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|BindingInfo
name|getBinding
parameter_list|()
block|{
return|return
name|bindingInfo
return|;
block|}
specifier|public
name|QName
name|getName
parameter_list|()
block|{
return|return
name|opInfo
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|public
name|OperationInfo
name|getOperationInfo
parameter_list|()
block|{
return|return
name|opInfo
return|;
block|}
specifier|public
name|BindingMessageInfo
name|getInput
parameter_list|()
block|{
return|return
name|inputMessage
return|;
block|}
specifier|public
name|BindingMessageInfo
name|getOutput
parameter_list|()
block|{
return|return
name|outputMessage
return|;
block|}
specifier|public
name|BindingFaultInfo
name|getFault
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
if|if
condition|(
name|faults
operator|!=
literal|null
condition|)
block|{
return|return
name|faults
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|BindingFaultInfo
argument_list|>
name|getFaults
parameter_list|()
block|{
if|if
condition|(
name|faults
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
return|return
name|Collections
operator|.
name|unmodifiableCollection
argument_list|(
name|faults
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isUnwrappedCapable
parameter_list|()
block|{
return|return
name|opInfo
operator|.
name|isUnwrappedCapable
argument_list|()
return|;
block|}
specifier|public
name|BindingOperationInfo
name|getUnwrappedOperation
parameter_list|()
block|{
return|return
name|opHolder
return|;
block|}
specifier|public
name|void
name|setUnwrappedOperation
parameter_list|(
name|BindingOperationInfo
name|op
parameter_list|)
block|{
name|opHolder
operator|=
name|op
expr_stmt|;
block|}
specifier|public
name|boolean
name|isUnwrapped
parameter_list|()
block|{
return|return
name|opInfo
operator|.
name|isUnwrapped
argument_list|()
return|;
block|}
specifier|public
name|BindingOperationInfo
name|getWrappedOperation
parameter_list|()
block|{
return|return
name|opHolder
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
literal|"[BindingOperationInfo: "
argument_list|)
operator|.
name|append
argument_list|(
name|getName
argument_list|()
operator|==
literal|null
condition|?
literal|""
else|:
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|opInfo
operator|.
name|hashCode
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|==
name|this
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|BindingOperationInfo
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|equals
argument_list|(
name|opInfo
argument_list|,
operator|(
operator|(
name|BindingOperationInfo
operator|)
name|o
operator|)
operator|.
name|opInfo
argument_list|)
return|;
block|}
block|}
end_class

end_unit

