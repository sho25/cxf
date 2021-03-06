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
name|mime
operator|.
name|MIMEContent
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
name|mime
operator|.
name|MIMEMultipartRelated
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
name|mime
operator|.
name|MIMEPart
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
name|binding
operator|.
name|soap
operator|.
name|SOAPBindingUtil
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
name|MIMEBindingValidator
extends|extends
name|AbstractDefinitionValidator
block|{
specifier|public
name|MIMEBindingValidator
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
name|bindings
control|)
block|{
name|Collection
argument_list|<
name|BindingOperation
argument_list|>
name|bindingOps
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
name|bindingOps
control|)
block|{
if|if
condition|(
name|bindingOperation
operator|.
name|getBindingInput
argument_list|()
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|Collection
argument_list|<
name|ExtensibilityElement
argument_list|>
name|exts
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|bindingOperation
operator|.
name|getBindingInput
argument_list|()
operator|.
name|getExtensibilityElements
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|ExtensibilityElement
name|extElement
range|:
name|exts
control|)
block|{
if|if
condition|(
name|extElement
operator|instanceof
name|MIMEMultipartRelated
operator|&&
operator|!
name|doValidate
argument_list|(
operator|(
name|MIMEMultipartRelated
operator|)
name|extElement
argument_list|,
name|bindingOperation
operator|.
name|getName
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
name|doValidate
parameter_list|(
name|MIMEMultipartRelated
name|mimeExt
parameter_list|,
name|String
name|operationName
parameter_list|)
block|{
name|boolean
name|gotRootPart
init|=
literal|false
decl_stmt|;
name|List
argument_list|<
name|MIMEPart
argument_list|>
name|parts
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|mimeExt
operator|.
name|getMIMEParts
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|MIMEPart
name|mPart
range|:
name|parts
control|)
block|{
name|List
argument_list|<
name|MIMEContent
argument_list|>
name|mimeContents
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ExtensibilityElement
argument_list|>
name|extns
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|mPart
operator|.
name|getExtensibilityElements
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|ExtensibilityElement
name|extElement
range|:
name|extns
control|)
block|{
if|if
condition|(
name|SOAPBindingUtil
operator|.
name|isSOAPBody
argument_list|(
name|extElement
argument_list|)
condition|)
block|{
if|if
condition|(
name|gotRootPart
condition|)
block|{
name|addErrorMessage
argument_list|(
literal|"Operation("
operator|+
name|operationName
operator|+
literal|"): There's more than one soap body mime part"
operator|+
literal|" in its binding input"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|gotRootPart
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|extElement
operator|instanceof
name|MIMEContent
condition|)
block|{
name|mimeContents
operator|.
name|add
argument_list|(
operator|(
name|MIMEContent
operator|)
name|extElement
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|doValidateMimeContentPartNames
argument_list|(
name|mimeContents
argument_list|,
name|operationName
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
if|if
condition|(
operator|!
name|gotRootPart
condition|)
block|{
name|addErrorMessage
argument_list|(
literal|"Operation("
operator|+
name|operationName
operator|+
literal|"): There's no soap body in mime part"
operator|+
literal|" in its binding input"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|boolean
name|doValidateMimeContentPartNames
parameter_list|(
name|List
argument_list|<
name|MIMEContent
argument_list|>
name|mimeContents
parameter_list|,
name|String
name|operationName
parameter_list|)
block|{
comment|// validate mime:content(s) in the mime:part as per R2909
name|String
name|partName
init|=
literal|null
decl_stmt|;
for|for
control|(
name|MIMEContent
name|mimeContent
range|:
name|mimeContents
control|)
block|{
name|String
name|mimeContnetPart
init|=
name|mimeContent
operator|.
name|getPart
argument_list|()
decl_stmt|;
if|if
condition|(
name|mimeContnetPart
operator|==
literal|null
condition|)
block|{
name|addErrorMessage
argument_list|(
literal|"Operation("
operator|+
name|operationName
operator|+
literal|"): Must provide part attribute value for meme:content elements"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
if|if
condition|(
name|partName
operator|==
literal|null
condition|)
block|{
name|partName
operator|=
name|mimeContnetPart
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
operator|!
name|partName
operator|.
name|equals
argument_list|(
name|mimeContnetPart
argument_list|)
condition|)
block|{
name|addErrorMessage
argument_list|(
literal|"Operation("
operator|+
name|operationName
operator|+
literal|"): Part attribute value for meme:content "
operator|+
literal|"elements are different"
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

