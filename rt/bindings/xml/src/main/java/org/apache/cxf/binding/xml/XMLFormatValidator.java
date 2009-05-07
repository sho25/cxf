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
name|binding
operator|.
name|xml
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
name|Iterator
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
name|bindings
operator|.
name|xformat
operator|.
name|XMLBindingMessageFormat
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
name|WSDLConstants
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
name|XMLFormatValidator
extends|extends
name|ServiceValidator
block|{
specifier|public
name|XMLFormatValidator
parameter_list|()
block|{     }
specifier|public
name|XMLFormatValidator
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
name|checkXMLBindingFormat
argument_list|()
return|;
block|}
specifier|private
name|boolean
name|checkXMLBindingFormat
parameter_list|()
block|{
name|Collection
argument_list|<
name|BindingInfo
argument_list|>
name|bindings
init|=
name|service
operator|.
name|getBindings
argument_list|()
decl_stmt|;
if|if
condition|(
name|bindings
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|BindingInfo
name|binding
range|:
name|bindings
control|)
block|{
if|if
condition|(
name|WSDLConstants
operator|.
name|NS_BINDING_XML
operator|.
name|equalsIgnoreCase
argument_list|(
name|binding
operator|.
name|getBindingId
argument_list|()
argument_list|)
operator|&&
operator|!
name|checkXMLFormat
argument_list|(
name|binding
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
name|checkXMLFormat
parameter_list|(
name|BindingInfo
name|binding
parameter_list|)
block|{
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
name|boolean
name|result
init|=
literal|true
decl_stmt|;
name|boolean
name|needRootNode
init|=
literal|false
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
name|needRootNode
operator|=
literal|false
expr_stmt|;
if|if
condition|(
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
operator|==
literal|0
operator|||
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
operator|>
literal|1
condition|)
block|{
name|needRootNode
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|needRootNode
condition|)
block|{
name|String
name|path
init|=
literal|"Binding("
operator|+
name|binding
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|"):BindingOperation("
operator|+
name|bo
operator|.
name|getName
argument_list|()
operator|+
literal|")"
decl_stmt|;
name|List
argument_list|<
name|ExtensibilityElement
argument_list|>
name|inExtensors
init|=
name|bo
operator|.
name|getInput
argument_list|()
operator|.
name|getExtensors
argument_list|(
name|ExtensibilityElement
operator|.
name|class
argument_list|)
decl_stmt|;
name|Iterator
name|itIn
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|inExtensors
operator|!=
literal|null
condition|)
block|{
name|itIn
operator|=
name|inExtensors
operator|.
name|iterator
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|findXMLFormatRootNode
argument_list|(
name|itIn
argument_list|,
name|bo
argument_list|,
name|path
operator|+
literal|"-input"
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// Input check correct, continue to check output binding
if|if
condition|(
name|op
operator|.
name|getOutput
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|needRootNode
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|op
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
literal|0
operator|||
name|op
operator|.
name|getOutput
argument_list|()
operator|.
name|getMessageParts
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|needRootNode
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|needRootNode
condition|)
block|{
name|List
argument_list|<
name|ExtensibilityElement
argument_list|>
name|outExtensors
init|=
name|bo
operator|.
name|getOutput
argument_list|()
operator|.
name|getExtensors
argument_list|(
name|ExtensibilityElement
operator|.
name|class
argument_list|)
decl_stmt|;
name|Iterator
name|itOut
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|outExtensors
operator|!=
literal|null
condition|)
block|{
name|itOut
operator|=
name|outExtensors
operator|.
name|iterator
argument_list|()
expr_stmt|;
block|}
name|result
operator|=
name|result
operator|&&
name|findXMLFormatRootNode
argument_list|(
name|itOut
argument_list|,
name|bo
argument_list|,
name|path
operator|+
literal|"-Output"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|result
condition|)
block|{
return|return
literal|false
return|;
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
name|boolean
name|findXMLFormatRootNode
parameter_list|(
name|Iterator
name|it
parameter_list|,
name|BindingOperationInfo
name|bo
parameter_list|,
name|String
name|errorPath
parameter_list|)
block|{
while|while
condition|(
name|it
operator|!=
literal|null
operator|&&
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|ext
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|ext
operator|instanceof
name|XMLBindingMessageFormat
condition|)
block|{
name|XMLBindingMessageFormat
name|xmlFormat
init|=
operator|(
name|XMLBindingMessageFormat
operator|)
name|ext
decl_stmt|;
if|if
condition|(
name|xmlFormat
operator|.
name|getRootNode
argument_list|()
operator|==
literal|null
condition|)
block|{
name|QName
name|rootNodeName
init|=
name|bo
operator|.
name|getName
argument_list|()
decl_stmt|;
name|addErrorMessage
argument_list|(
name|errorPath
operator|+
literal|": empty value of rootNode attribute, the value should be "
operator|+
name|rootNodeName
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
block|}
end_class

end_unit

