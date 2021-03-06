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
name|validator
operator|.
name|internal
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
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Binding
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|BindingInput
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|BindingOperation
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Definition
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Operation
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Part
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|extensions
operator|.
name|ExtensibilityElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|extensions
operator|.
name|soap
operator|.
name|SOAPHeader
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
name|helpers
operator|.
name|CastUtils
import|;
end_import

begin_class
specifier|public
class|class
name|UniqueBodyPartsValidator
extends|extends
name|AbstractDefinitionValidator
block|{
specifier|private
name|Map
argument_list|<
name|QName
argument_list|,
name|String
argument_list|>
name|uniqueBodyPartsMap
decl_stmt|;
specifier|public
name|UniqueBodyPartsValidator
parameter_list|(
name|Definition
name|def
parameter_list|)
block|{
name|super
argument_list|(
name|def
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isValid
parameter_list|()
block|{
name|Collection
argument_list|<
name|Binding
argument_list|>
name|bindings
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|def
operator|.
name|getAllBindings
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Binding
name|binding
range|:
name|bindings
control|)
block|{
name|uniqueBodyPartsMap
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|BindingOperation
argument_list|>
name|ops
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|binding
operator|.
name|getBindingOperations
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|BindingOperation
name|op
range|:
name|ops
control|)
block|{
name|Operation
name|operation
init|=
name|op
operator|.
name|getOperation
argument_list|()
decl_stmt|;
if|if
condition|(
name|operation
operator|!=
literal|null
operator|&&
name|operation
operator|.
name|getInput
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Message
name|inMessage
init|=
name|operation
operator|.
name|getInput
argument_list|()
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|BindingInput
name|bin
init|=
name|op
operator|.
name|getBindingInput
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|headers
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|bin
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|ExtensibilityElement
argument_list|>
name|lst
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|bin
operator|.
name|getExtensibilityElements
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|ExtensibilityElement
name|ext
range|:
name|lst
control|)
block|{
if|if
condition|(
operator|!
operator|(
name|ext
operator|instanceof
name|SOAPHeader
operator|)
condition|)
block|{
continue|continue;
block|}
name|SOAPHeader
name|header
init|=
operator|(
name|SOAPHeader
operator|)
name|ext
decl_stmt|;
if|if
condition|(
operator|!
name|header
operator|.
name|getMessage
argument_list|()
operator|.
name|equals
argument_list|(
name|inMessage
operator|.
name|getQName
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|headers
operator|.
name|add
argument_list|(
name|header
operator|.
name|getPart
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|//find the headers as they don't contribute to the body
if|if
condition|(
name|inMessage
operator|!=
literal|null
operator|&&
operator|!
name|isUniqueBodyPart
argument_list|(
name|operation
operator|.
name|getName
argument_list|()
argument_list|,
name|inMessage
argument_list|,
name|headers
argument_list|,
name|binding
operator|.
name|getQName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|boolean
name|isUniqueBodyPart
parameter_list|(
name|String
name|operationName
parameter_list|,
name|Message
name|msg
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|headers
parameter_list|,
name|QName
name|bindingName
parameter_list|)
block|{
name|List
argument_list|<
name|Part
argument_list|>
name|partList
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|msg
operator|.
name|getOrderedParts
argument_list|(
literal|null
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|Part
name|part
range|:
name|partList
control|)
block|{
if|if
condition|(
name|headers
operator|.
name|contains
argument_list|(
name|part
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|part
operator|.
name|getElementName
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
name|String
name|opName
init|=
name|getOperationNameWithSamePart
argument_list|(
name|operationName
argument_list|,
name|part
argument_list|)
decl_stmt|;
if|if
condition|(
name|opName
operator|!=
literal|null
condition|)
block|{
name|addErrorMessage
argument_list|(
literal|"Non unique body parts, operation "
operator|+
literal|"[ "
operator|+
name|opName
operator|+
literal|" ] "
operator|+
literal|"and operation [ "
operator|+
name|operationName
operator|+
literal|" ] in binding "
operator|+
name|bindingName
operator|.
name|toString
argument_list|()
operator|+
literal|" have the same body block: "
operator|+
name|part
operator|.
name|getElementName
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
comment|//just need to check the first element
return|return
literal|true
return|;
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|String
name|getOperationNameWithSamePart
parameter_list|(
name|String
name|operationName
parameter_list|,
name|Part
name|part
parameter_list|)
block|{
name|QName
name|partQN
init|=
name|part
operator|.
name|getElementName
argument_list|()
decl_stmt|;
name|String
name|opName
init|=
name|uniqueBodyPartsMap
operator|.
name|get
argument_list|(
name|partQN
argument_list|)
decl_stmt|;
if|if
condition|(
name|opName
operator|==
literal|null
condition|)
block|{
name|uniqueBodyPartsMap
operator|.
name|put
argument_list|(
name|partQN
argument_list|,
name|operationName
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
return|return
name|opName
return|;
block|}
block|}
end_class

end_unit

