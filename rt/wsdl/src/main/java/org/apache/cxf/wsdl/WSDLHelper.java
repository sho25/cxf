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
name|wsdl
package|;
end_package

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
name|Binding
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
name|Input
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
name|Output
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
name|PortType
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
name|WSDLHelper
block|{
specifier|public
name|BindingOperation
name|getBindingOperation
parameter_list|(
name|Definition
name|def
parameter_list|,
name|String
name|operationName
parameter_list|)
block|{
if|if
condition|(
name|operationName
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Iterator
argument_list|<
name|Binding
argument_list|>
name|ite
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|def
operator|.
name|getBindings
argument_list|()
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|)
decl_stmt|;
while|while
condition|(
name|ite
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Binding
name|binding
init|=
name|ite
operator|.
name|next
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|BindingOperation
argument_list|>
name|ite1
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|binding
operator|.
name|getBindingOperations
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|)
decl_stmt|;
while|while
condition|(
name|ite1
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|BindingOperation
name|bop
init|=
name|ite1
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|bop
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|operationName
argument_list|)
condition|)
block|{
return|return
name|bop
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|String
name|writeQName
parameter_list|(
name|Definition
name|def
parameter_list|,
name|QName
name|qname
parameter_list|)
block|{
return|return
name|def
operator|.
name|getPrefix
argument_list|(
name|qname
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|+
literal|":"
operator|+
name|qname
operator|.
name|getLocalPart
argument_list|()
return|;
block|}
specifier|public
name|BindingOperation
name|getBindingOperation
parameter_list|(
name|Binding
name|binding
parameter_list|,
name|String
name|operationName
parameter_list|)
block|{
if|if
condition|(
name|operationName
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
argument_list|<
name|BindingOperation
argument_list|>
name|bindingOperations
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
name|bindingOperation
range|:
name|bindingOperations
control|)
block|{
if|if
condition|(
name|operationName
operator|.
name|equals
argument_list|(
name|bindingOperation
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|bindingOperation
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|List
argument_list|<
name|PortType
argument_list|>
name|getPortTypes
parameter_list|(
name|Definition
name|def
parameter_list|)
block|{
name|List
argument_list|<
name|PortType
argument_list|>
name|portTypes
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|PortType
argument_list|>
name|ite
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|def
operator|.
name|getPortTypes
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|PortType
name|portType
range|:
name|ite
control|)
block|{
name|portTypes
operator|.
name|add
argument_list|(
name|portType
argument_list|)
expr_stmt|;
block|}
return|return
name|portTypes
return|;
block|}
specifier|public
name|List
argument_list|<
name|Part
argument_list|>
name|getInMessageParts
parameter_list|(
name|Operation
name|operation
parameter_list|)
block|{
name|Input
name|input
init|=
name|operation
operator|.
name|getInput
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Part
argument_list|>
name|partsList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|input
operator|!=
literal|null
operator|&&
name|input
operator|.
name|getMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Collection
argument_list|<
name|Part
argument_list|>
name|parts
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|input
operator|.
name|getMessage
argument_list|()
operator|.
name|getParts
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Part
name|p
range|:
name|parts
control|)
block|{
name|partsList
operator|.
name|add
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|partsList
return|;
block|}
specifier|public
name|List
argument_list|<
name|Part
argument_list|>
name|getOutMessageParts
parameter_list|(
name|Operation
name|operation
parameter_list|)
block|{
name|Output
name|output
init|=
name|operation
operator|.
name|getOutput
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Part
argument_list|>
name|partsList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|output
operator|!=
literal|null
operator|&&
name|output
operator|.
name|getMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Collection
argument_list|<
name|Part
argument_list|>
name|parts
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|output
operator|.
name|getMessage
argument_list|()
operator|.
name|getParts
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Part
name|p
range|:
name|parts
control|)
block|{
name|partsList
operator|.
name|add
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|partsList
return|;
block|}
specifier|public
name|Binding
name|getBinding
parameter_list|(
name|BindingOperation
name|bop
parameter_list|,
name|Definition
name|def
parameter_list|)
block|{
name|Collection
argument_list|<
name|Binding
argument_list|>
name|ite
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|def
operator|.
name|getBindings
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
name|ite
control|)
block|{
name|List
argument_list|<
name|BindingOperation
argument_list|>
name|bos
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
name|bindingOperation
range|:
name|bos
control|)
block|{
if|if
condition|(
name|bindingOperation
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|bop
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|binding
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

