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
name|Collections
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
name|factory
operator|.
name|ReflectionServiceFactoryBean
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

begin_class
specifier|public
class|class
name|WrapperClassInInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
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
name|WrapperClassInInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|WrapperClassInInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|POST_LOGICAL
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
name|boi
init|=
name|ex
operator|.
name|getBindingOperationInfo
argument_list|()
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
name|PARTIAL_RESPONSE_MESSAGE
argument_list|)
argument_list|)
operator|||
name|boi
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Method
name|method
init|=
name|ex
operator|.
name|get
argument_list|(
name|Method
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|method
operator|!=
literal|null
operator|&&
name|method
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"Async"
argument_list|)
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|retType
init|=
name|method
operator|.
name|getReturnType
argument_list|()
decl_stmt|;
if|if
condition|(
name|retType
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"java.util.concurrent.Future"
argument_list|)
operator|||
name|retType
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"javax.xml.ws.Response"
argument_list|)
condition|)
block|{
return|return;
block|}
block|}
if|if
condition|(
name|boi
operator|!=
literal|null
operator|&&
name|boi
operator|.
name|isUnwrappedCapable
argument_list|()
condition|)
block|{
name|BindingOperationInfo
name|boi2
init|=
name|boi
operator|.
name|getUnwrappedOperation
argument_list|()
decl_stmt|;
name|OperationInfo
name|op
init|=
name|boi2
operator|.
name|getOperationInfo
argument_list|()
decl_stmt|;
name|BindingMessageInfo
name|bmi
decl_stmt|;
name|MessageInfo
name|wrappedMessageInfo
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
name|MessageInfo
name|messageInfo
decl_stmt|;
if|if
condition|(
name|wrappedMessageInfo
operator|==
name|boi
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|getInput
argument_list|()
condition|)
block|{
name|messageInfo
operator|=
name|op
operator|.
name|getInput
argument_list|()
expr_stmt|;
name|bmi
operator|=
name|boi2
operator|.
name|getInput
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|messageInfo
operator|=
name|op
operator|.
name|getOutput
argument_list|()
expr_stmt|;
name|bmi
operator|=
name|boi2
operator|.
name|getOutput
argument_list|()
expr_stmt|;
block|}
comment|// Sometimes, an operation can be unwrapped according to WSDLServiceFactory,
comment|// but not according to JAX-WS. We should unify these at some point, but
comment|// for now check for the wrapper class.
name|MessageContentsList
name|lst
init|=
name|MessageContentsList
operator|.
name|getContentsList
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|lst
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|lst
operator|!=
literal|null
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
name|messageInfo
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
name|bmi
argument_list|)
expr_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|,
name|boi2
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
name|op
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isGET
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"WrapperClassInInterceptor skipped in HTTP GET method"
argument_list|)
expr_stmt|;
return|return;
block|}
name|MessagePartInfo
name|wrapperPart
init|=
name|wrappedMessageInfo
operator|.
name|getMessagePart
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|wrapperClass
init|=
name|wrapperPart
operator|.
name|getTypeClass
argument_list|()
decl_stmt|;
name|Object
name|wrappedObject
init|=
name|lst
operator|.
name|get
argument_list|(
name|wrapperPart
operator|.
name|getIndex
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|wrapperClass
operator|!=
literal|null
operator|&&
operator|!
name|wrapperClass
operator|.
name|isInstance
argument_list|(
name|wrappedObject
argument_list|)
condition|)
block|{
name|wrappedObject
operator|=
literal|null
expr_stmt|;
name|wrapperPart
operator|=
literal|null
expr_stmt|;
name|wrapperClass
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|wrapperClass
operator|==
literal|null
operator|||
name|wrappedObject
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|WrapperHelper
name|helper
init|=
name|wrapperPart
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
name|wrapperClass
argument_list|)
expr_stmt|;
name|wrapperPart
operator|.
name|setProperty
argument_list|(
literal|"WRAPPER_CLASS"
argument_list|,
name|helper
argument_list|)
expr_stmt|;
block|}
else|else
block|{
return|return;
block|}
block|}
name|MessageContentsList
name|newParams
decl_stmt|;
try|try
block|{
name|newParams
operator|=
operator|new
name|MessageContentsList
argument_list|(
name|helper
operator|.
name|getWrapperParts
argument_list|(
name|wrappedObject
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|removes
init|=
literal|null
decl_stmt|;
name|int
name|count
init|=
literal|0
decl_stmt|;
for|for
control|(
name|MessagePartInfo
name|part
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
name|part
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
name|wrappedMessageInfo
operator|.
name|getMessagePart
argument_list|(
name|part
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|lst
operator|.
name|hasValue
argument_list|(
name|mpi
argument_list|)
condition|)
block|{
name|count
operator|++
expr_stmt|;
name|newParams
operator|.
name|put
argument_list|(
name|part
argument_list|,
name|lst
operator|.
name|get
argument_list|(
name|mpi
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|mpi
operator|.
name|getTypeClass
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|//header, but not mapped to a param on the method
if|if
condition|(
name|removes
operator|==
literal|null
condition|)
block|{
name|removes
operator|=
operator|new
name|ArrayList
argument_list|<
name|Integer
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|removes
operator|.
name|add
argument_list|(
name|part
operator|.
name|getIndex
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
operator|++
name|count
expr_stmt|;
block|}
block|}
if|if
condition|(
name|count
operator|==
literal|0
condition|)
block|{
name|newParams
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|removes
operator|!=
literal|null
condition|)
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|removes
argument_list|,
name|Collections
operator|.
name|reverseOrder
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Integer
name|i
range|:
name|removes
control|)
block|{
if|if
condition|(
name|i
operator|<
name|newParams
operator|.
name|size
argument_list|()
condition|)
block|{
name|newParams
operator|.
name|remove
argument_list|(
name|i
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
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
name|message
operator|.
name|setContent
argument_list|(
name|List
operator|.
name|class
argument_list|,
name|newParams
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
name|wrapperClass
operator|==
name|p
operator|.
name|getTypeClass
argument_list|()
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
if|if
condition|(
name|p
operator|.
name|getTypeClass
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|int
name|idx
init|=
name|p
operator|.
name|getIndex
argument_list|()
decl_stmt|;
name|ensureSize
argument_list|(
name|elTypeNames
argument_list|,
name|idx
argument_list|)
expr_stmt|;
name|ensureSize
argument_list|(
name|partClasses
argument_list|,
name|idx
argument_list|)
expr_stmt|;
name|ensureSize
argument_list|(
name|partNames
argument_list|,
name|idx
argument_list|)
expr_stmt|;
name|elTypeNames
operator|.
name|set
argument_list|(
name|idx
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|partClasses
operator|.
name|set
argument_list|(
name|idx
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|partNames
operator|.
name|set
argument_list|(
name|idx
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
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
name|int
name|idx
init|=
name|p
operator|.
name|getIndex
argument_list|()
decl_stmt|;
name|ensureSize
argument_list|(
name|elTypeNames
argument_list|,
name|idx
argument_list|)
expr_stmt|;
name|ensureSize
argument_list|(
name|partClasses
argument_list|,
name|idx
argument_list|)
expr_stmt|;
name|ensureSize
argument_list|(
name|partNames
argument_list|,
name|idx
argument_list|)
expr_stmt|;
name|elTypeNames
operator|.
name|set
argument_list|(
name|idx
argument_list|,
name|elementType
argument_list|)
expr_stmt|;
name|partClasses
operator|.
name|set
argument_list|(
name|idx
argument_list|,
name|p
operator|.
name|getTypeClass
argument_list|()
argument_list|)
expr_stmt|;
name|partNames
operator|.
name|set
argument_list|(
name|idx
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
block|}
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
block|}
end_class

end_unit

