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
name|jaxws
operator|.
name|interceptors
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
name|List
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
name|annotations
operator|.
name|SchemaValidation
operator|.
name|SchemaValidationType
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
name|databinding
operator|.
name|AbstractWrapperHelper
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
name|databinding
operator|.
name|DataBinding
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
name|databinding
operator|.
name|WrapperCapableDatabinding
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
name|databinding
operator|.
name|WrapperHelper
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
name|interceptor
operator|.
name|Fault
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
name|message
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
name|message
operator|.
name|MessageContentsList
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
name|phase
operator|.
name|AbstractPhaseInterceptor
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
name|phase
operator|.
name|Phase
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
name|Service
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
name|BindingMessageInfo
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
name|ServiceModelUtil
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
name|wsdl
operator|.
name|service
operator|.
name|factory
operator|.
name|ReflectionServiceFactoryBean
import|;
end_import

begin_class
specifier|public
class|class
name|WrapperClassOutInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|public
name|WrapperClassOutInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_LOGICAL
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|Exchange
name|ex
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|BindingOperationInfo
name|bop
init|=
name|ex
operator|.
name|getBindingOperationInfo
argument_list|()
decl_stmt|;
name|MessageInfo
name|messageInfo
init|=
name|message
operator|.
name|get
argument_list|(
name|MessageInfo
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|messageInfo
operator|==
literal|null
operator|||
name|bop
operator|==
literal|null
operator|||
operator|!
name|bop
operator|.
name|isUnwrapped
argument_list|()
condition|)
block|{
return|return;
block|}
name|BindingOperationInfo
name|newbop
init|=
name|bop
operator|.
name|getWrappedOperation
argument_list|()
decl_stmt|;
name|MessageInfo
name|wrappedMsgInfo
decl_stmt|;
if|if
condition|(
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|REQUESTOR_ROLE
argument_list|)
argument_list|)
condition|)
block|{
name|wrappedMsgInfo
operator|=
name|newbop
operator|.
name|getInput
argument_list|()
operator|.
name|getMessageInfo
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|wrappedMsgInfo
operator|=
name|newbop
operator|.
name|getOutput
argument_list|()
operator|.
name|getMessageInfo
argument_list|()
expr_stmt|;
block|}
name|Class
argument_list|<
name|?
argument_list|>
name|wrapped
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|wrappedMsgInfo
operator|.
name|getMessagePartsNumber
argument_list|()
operator|>
literal|0
condition|)
block|{
name|wrapped
operator|=
name|wrappedMsgInfo
operator|.
name|getFirstMessagePart
argument_list|()
operator|.
name|getTypeClass
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|wrapped
operator|!=
literal|null
condition|)
block|{
name|MessagePartInfo
name|firstMessagePart
init|=
name|wrappedMsgInfo
operator|.
name|getFirstMessagePart
argument_list|()
decl_stmt|;
name|MessageContentsList
name|objs
init|=
name|MessageContentsList
operator|.
name|getContentsList
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|WrapperHelper
name|helper
init|=
name|firstMessagePart
operator|.
name|getProperty
argument_list|(
literal|"WRAPPER_CLASS"
argument_list|,
name|WrapperHelper
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|helper
operator|==
literal|null
condition|)
block|{
name|helper
operator|=
name|getWrapperHelper
argument_list|(
name|message
argument_list|,
name|messageInfo
argument_list|,
name|wrappedMsgInfo
argument_list|,
name|wrapped
argument_list|,
name|firstMessagePart
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|helper
operator|==
literal|null
condition|)
block|{
return|return;
block|}
try|try
block|{
name|MessageContentsList
name|newObjs
init|=
operator|new
name|MessageContentsList
argument_list|()
decl_stmt|;
if|if
condition|(
name|ServiceUtils
operator|.
name|isSchemaValidationEnabled
argument_list|(
name|SchemaValidationType
operator|.
name|OUT
argument_list|,
name|message
argument_list|)
operator|&&
name|helper
operator|instanceof
name|AbstractWrapperHelper
condition|)
block|{
operator|(
operator|(
name|AbstractWrapperHelper
operator|)
name|helper
operator|)
operator|.
name|setValidate
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|Object
name|o2
init|=
name|helper
operator|.
name|createWrapperObject
argument_list|(
name|objs
argument_list|)
decl_stmt|;
name|newObjs
operator|.
name|put
argument_list|(
name|firstMessagePart
argument_list|,
name|o2
argument_list|)
expr_stmt|;
for|for
control|(
name|MessagePartInfo
name|p
range|:
name|messageInfo
operator|.
name|getMessageParts
argument_list|()
control|)
block|{
if|if
condition|(
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|p
operator|.
name|getProperty
argument_list|(
name|ReflectionServiceFactoryBean
operator|.
name|HEADER
argument_list|)
argument_list|)
condition|)
block|{
name|MessagePartInfo
name|mpi
init|=
name|wrappedMsgInfo
operator|.
name|getMessagePart
argument_list|(
name|p
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|objs
operator|.
name|hasValue
argument_list|(
name|p
argument_list|)
condition|)
block|{
name|newObjs
operator|.
name|put
argument_list|(
name|mpi
argument_list|,
name|objs
operator|.
name|get
argument_list|(
name|p
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|message
operator|.
name|setContent
argument_list|(
name|List
operator|.
name|class
argument_list|,
name|newObjs
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Fault
name|f
parameter_list|)
block|{
throw|throw
name|f
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
comment|// we've now wrapped the object, so use the wrapped binding op
name|ex
operator|.
name|put
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|,
name|newbop
argument_list|)
expr_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|OperationInfo
operator|.
name|class
argument_list|,
name|newbop
operator|.
name|getOperationInfo
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|messageInfo
operator|==
name|bop
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|getInput
argument_list|()
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|MessageInfo
operator|.
name|class
argument_list|,
name|newbop
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|getInput
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|BindingMessageInfo
operator|.
name|class
argument_list|,
name|newbop
operator|.
name|getInput
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|messageInfo
operator|==
name|bop
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|getOutput
argument_list|()
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|MessageInfo
operator|.
name|class
argument_list|,
name|newbop
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|getOutput
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|BindingMessageInfo
operator|.
name|class
argument_list|,
name|newbop
operator|.
name|getOutput
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
specifier|synchronized
name|WrapperHelper
name|getWrapperHelper
parameter_list|(
name|Message
name|message
parameter_list|,
name|MessageInfo
name|messageInfo
parameter_list|,
name|MessageInfo
name|wrappedMessageInfo
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|wrapClass
parameter_list|,
name|MessagePartInfo
name|messagePartInfo
parameter_list|)
block|{
name|WrapperHelper
name|helper
init|=
name|messagePartInfo
operator|.
name|getProperty
argument_list|(
literal|"WRAPPER_CLASS"
argument_list|,
name|WrapperHelper
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|helper
operator|==
literal|null
condition|)
block|{
name|Service
name|service
init|=
name|ServiceModelUtil
operator|.
name|getService
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
decl_stmt|;
name|DataBinding
name|dataBinding
init|=
name|service
operator|.
name|getDataBinding
argument_list|()
decl_stmt|;
if|if
condition|(
name|dataBinding
operator|instanceof
name|WrapperCapableDatabinding
condition|)
block|{
name|helper
operator|=
name|createWrapperHelper
argument_list|(
operator|(
name|WrapperCapableDatabinding
operator|)
name|dataBinding
argument_list|,
name|messageInfo
argument_list|,
name|wrappedMessageInfo
argument_list|,
name|wrapClass
argument_list|)
expr_stmt|;
name|messagePartInfo
operator|.
name|setProperty
argument_list|(
literal|"WRAPPER_CLASS"
argument_list|,
name|helper
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|helper
return|;
block|}
specifier|private
name|void
name|ensureSize
parameter_list|(
name|List
argument_list|<
name|?
argument_list|>
name|lst
parameter_list|,
name|int
name|idx
parameter_list|)
block|{
while|while
condition|(
name|idx
operator|>=
name|lst
operator|.
name|size
argument_list|()
condition|)
block|{
name|lst
operator|.
name|add
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|WrapperHelper
name|createWrapperHelper
parameter_list|(
name|WrapperCapableDatabinding
name|dataBinding
parameter_list|,
name|MessageInfo
name|messageInfo
parameter_list|,
name|MessageInfo
name|wrappedMessageInfo
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|wrapperClass
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|partNames
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|elTypeNames
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|partClasses
init|=
operator|new
name|ArrayList
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|QName
name|wrapperName
init|=
literal|null
decl_stmt|;
for|for
control|(
name|MessagePartInfo
name|p
range|:
name|wrappedMessageInfo
operator|.
name|getMessageParts
argument_list|()
control|)
block|{
if|if
condition|(
name|p
operator|.
name|getTypeClass
argument_list|()
operator|==
name|wrapperClass
condition|)
block|{
name|wrapperName
operator|=
name|p
operator|.
name|getElementQName
argument_list|()
expr_stmt|;
block|}
block|}
for|for
control|(
name|MessagePartInfo
name|p
range|:
name|messageInfo
operator|.
name|getMessageParts
argument_list|()
control|)
block|{
if|if
condition|(
name|p
operator|.
name|getTypeClass
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|//WSDL part wasn't mapped to a parameter
continue|continue;
block|}
name|ensureSize
argument_list|(
name|partNames
argument_list|,
name|p
operator|.
name|getIndex
argument_list|()
argument_list|)
expr_stmt|;
name|ensureSize
argument_list|(
name|elTypeNames
argument_list|,
name|p
operator|.
name|getIndex
argument_list|()
argument_list|)
expr_stmt|;
name|ensureSize
argument_list|(
name|partClasses
argument_list|,
name|p
operator|.
name|getIndex
argument_list|()
argument_list|)
expr_stmt|;
name|partNames
operator|.
name|set
argument_list|(
name|p
operator|.
name|getIndex
argument_list|()
argument_list|,
name|p
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|elementType
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|p
operator|.
name|getTypeQName
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// handling anonymous complex type
name|elementType
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|elementType
operator|=
name|p
operator|.
name|getTypeQName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
expr_stmt|;
block|}
name|elTypeNames
operator|.
name|set
argument_list|(
name|p
operator|.
name|getIndex
argument_list|()
argument_list|,
name|elementType
argument_list|)
expr_stmt|;
name|partClasses
operator|.
name|set
argument_list|(
name|p
operator|.
name|getIndex
argument_list|()
argument_list|,
name|p
operator|.
name|getTypeClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|dataBinding
operator|.
name|createWrapperHelper
argument_list|(
name|wrapperClass
argument_list|,
name|wrapperName
argument_list|,
name|partNames
argument_list|,
name|elTypeNames
argument_list|,
name|partClasses
argument_list|)
return|;
block|}
block|}
end_class

end_unit

