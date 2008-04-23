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

begin_class
specifier|public
class|class
name|BindingInfo
extends|extends
name|AbstractDescriptionElement
implements|implements
name|NamedItem
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
name|BindingInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|QName
name|name
decl_stmt|;
name|ServiceInfo
name|service
decl_stmt|;
specifier|final
name|String
name|bindingId
decl_stmt|;
name|Map
argument_list|<
name|QName
argument_list|,
name|BindingOperationInfo
argument_list|>
name|operations
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|QName
argument_list|,
name|BindingOperationInfo
argument_list|>
argument_list|(
literal|4
argument_list|)
decl_stmt|;
specifier|public
name|BindingInfo
parameter_list|(
name|ServiceInfo
name|service
parameter_list|,
name|String
name|bindingId
parameter_list|)
block|{
name|this
operator|.
name|service
operator|=
name|service
expr_stmt|;
name|this
operator|.
name|bindingId
operator|=
name|bindingId
expr_stmt|;
block|}
specifier|public
name|InterfaceInfo
name|getInterface
parameter_list|()
block|{
return|return
name|service
operator|.
name|getInterface
argument_list|()
return|;
block|}
specifier|public
name|ServiceInfo
name|getService
parameter_list|()
block|{
return|return
name|service
return|;
block|}
specifier|public
name|String
name|getBindingId
parameter_list|()
block|{
return|return
name|bindingId
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|QName
name|n
parameter_list|)
block|{
name|name
operator|=
name|n
expr_stmt|;
block|}
specifier|public
name|QName
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|private
name|boolean
name|nameEquals
parameter_list|(
name|String
name|a
parameter_list|,
name|String
name|b
parameter_list|)
block|{
if|if
condition|(
name|a
operator|==
literal|null
condition|)
block|{
comment|// in case of input/output itself is empty
return|return
literal|true
return|;
block|}
else|else
block|{
return|return
literal|""
operator|.
name|equals
argument_list|(
name|a
argument_list|)
condition|?
literal|""
operator|.
name|equals
argument_list|(
name|b
argument_list|)
else|:
name|a
operator|.
name|equals
argument_list|(
name|b
argument_list|)
return|;
block|}
block|}
specifier|public
name|BindingOperationInfo
name|buildOperation
parameter_list|(
name|QName
name|opName
parameter_list|,
name|String
name|inName
parameter_list|,
name|String
name|outName
parameter_list|)
block|{
for|for
control|(
name|OperationInfo
name|op
range|:
name|getInterface
argument_list|()
operator|.
name|getOperations
argument_list|()
control|)
block|{
if|if
condition|(
name|opName
operator|.
name|equals
argument_list|(
name|op
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|nameEquals
argument_list|(
name|inName
argument_list|,
name|op
operator|.
name|getInputName
argument_list|()
argument_list|)
operator|&&
name|nameEquals
argument_list|(
name|outName
argument_list|,
name|op
operator|.
name|getOutputName
argument_list|()
argument_list|)
condition|)
block|{
return|return
operator|new
name|BindingOperationInfo
argument_list|(
name|this
argument_list|,
name|op
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Adds an operation to this service.      *      * @param operation the operation.      */
specifier|public
name|void
name|addOperation
parameter_list|(
name|BindingOperationInfo
name|operation
parameter_list|)
block|{
if|if
condition|(
name|operation
operator|.
name|getName
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"BINDING.OPERATION.NAME.NOT.NULL"
argument_list|,
name|LOG
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|operations
operator|.
name|containsKey
argument_list|(
name|operation
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"DUPLICATED.OPERATION.NAME"
argument_list|,
name|LOG
argument_list|,
operator|new
name|Object
index|[]
block|{
name|operation
operator|.
name|getName
argument_list|()
block|}
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
name|operations
operator|.
name|put
argument_list|(
name|operation
operator|.
name|getName
argument_list|()
argument_list|,
name|operation
argument_list|)
expr_stmt|;
block|}
comment|/**      * Removes an operation from this service.      *      * @param operation the operation.      */
specifier|public
name|void
name|removeOperation
parameter_list|(
name|BindingOperationInfo
name|operation
parameter_list|)
block|{
if|if
condition|(
name|operation
operator|.
name|getName
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"BINDING.OPERATION.NAME.NOT.NULL"
argument_list|,
name|LOG
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
name|operations
operator|.
name|remove
argument_list|(
name|operation
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the operation info with the given name, if found.      *      * @param oname the name.      * @return the operation; or<code>null</code> if not found.      */
specifier|public
name|BindingOperationInfo
name|getOperation
parameter_list|(
name|QName
name|oname
parameter_list|)
block|{
return|return
name|operations
operator|.
name|get
argument_list|(
name|oname
argument_list|)
return|;
block|}
comment|/**      * Returns all operations for this service.      *      * @return all operations.      */
specifier|public
name|Collection
argument_list|<
name|BindingOperationInfo
argument_list|>
name|getOperations
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableCollection
argument_list|(
name|operations
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|BindingOperationInfo
name|getOperation
parameter_list|(
name|OperationInfo
name|oi
parameter_list|)
block|{
for|for
control|(
name|BindingOperationInfo
name|b
range|:
name|operations
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|b
operator|.
name|getOperationInfo
argument_list|()
operator|==
name|oi
condition|)
block|{
return|return
name|b
return|;
block|}
elseif|else
if|if
condition|(
name|b
operator|.
name|isUnwrappedCapable
argument_list|()
operator|&&
name|b
operator|.
name|getUnwrappedOperation
argument_list|()
operator|.
name|getOperationInfo
argument_list|()
operator|==
name|oi
condition|)
block|{
return|return
name|b
operator|.
name|getUnwrappedOperation
argument_list|()
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
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"[BindingInfo "
operator|+
name|getBindingId
argument_list|()
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

