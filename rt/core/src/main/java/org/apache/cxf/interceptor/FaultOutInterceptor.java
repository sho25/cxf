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
name|interceptor
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
name|InvocationTargetException
import|;
end_import

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
name|logging
operator|.
name|Level
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
name|stream
operator|.
name|XMLStreamWriter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|DataWriter
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
name|FaultInfoException
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
name|FaultInfo
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
name|staxutils
operator|.
name|W3CDOMStreamWriter
import|;
end_import

begin_class
specifier|public
class|class
name|FaultOutInterceptor
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
name|FaultOutInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|FaultOutInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_PROTOCOL
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
name|Fault
name|f
init|=
operator|(
name|Fault
operator|)
name|message
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Throwable
name|cause
init|=
name|f
operator|.
name|getCause
argument_list|()
decl_stmt|;
if|if
condition|(
name|cause
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|BindingOperationInfo
name|bop
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|bop
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|FaultInfo
name|fi
init|=
name|getFaultForClass
argument_list|(
name|bop
argument_list|,
name|cause
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|cause
operator|instanceof
name|Exception
operator|&&
name|fi
operator|!=
literal|null
condition|)
block|{
name|Exception
name|ex
init|=
operator|(
name|Exception
operator|)
name|cause
decl_stmt|;
name|Object
name|bean
init|=
name|getFaultBean
argument_list|(
name|cause
argument_list|,
name|fi
argument_list|,
name|message
argument_list|)
decl_stmt|;
name|Service
name|service
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Service
operator|.
name|class
argument_list|)
decl_stmt|;
name|MessagePartInfo
name|part
init|=
name|fi
operator|.
name|getMessageParts
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|DataBinding
name|db
init|=
name|service
operator|.
name|getDataBinding
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|isDOMSupported
argument_list|(
name|db
argument_list|)
condition|)
block|{
name|DataWriter
argument_list|<
name|Node
argument_list|>
name|writer
init|=
name|db
operator|.
name|createWriter
argument_list|(
name|Node
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|.
name|hasDetails
argument_list|()
condition|)
block|{
name|writer
operator|.
name|write
argument_list|(
name|bean
argument_list|,
name|part
argument_list|,
name|f
operator|.
name|getDetail
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|writer
operator|.
name|write
argument_list|(
name|bean
argument_list|,
name|part
argument_list|,
name|f
operator|.
name|getOrCreateDetail
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|f
operator|.
name|getDetail
argument_list|()
operator|.
name|hasChildNodes
argument_list|()
condition|)
block|{
name|f
operator|.
name|setDetail
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
if|if
condition|(
name|f
operator|.
name|hasDetails
argument_list|()
condition|)
block|{
name|XMLStreamWriter
name|xsw
init|=
operator|new
name|W3CDOMStreamWriter
argument_list|(
name|f
operator|.
name|getDetail
argument_list|()
argument_list|)
decl_stmt|;
name|DataWriter
argument_list|<
name|XMLStreamWriter
argument_list|>
name|writer
init|=
name|db
operator|.
name|createWriter
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|)
decl_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|bean
argument_list|,
name|part
argument_list|,
name|xsw
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|XMLStreamWriter
name|xsw
init|=
operator|new
name|W3CDOMStreamWriter
argument_list|(
name|f
operator|.
name|getOrCreateDetail
argument_list|()
argument_list|)
decl_stmt|;
name|DataWriter
argument_list|<
name|XMLStreamWriter
argument_list|>
name|writer
init|=
name|db
operator|.
name|createWriter
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|)
decl_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|bean
argument_list|,
name|part
argument_list|,
name|xsw
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|f
operator|.
name|getDetail
argument_list|()
operator|.
name|hasChildNodes
argument_list|()
condition|)
block|{
name|f
operator|.
name|setDetail
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|f
operator|.
name|setMessage
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|fex
parameter_list|)
block|{
comment|//ignore - if any exceptions occur here, we'll ignore them
comment|//and let the default fault handling of the binding convert
comment|//the fault like it was an unchecked exception.
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"EXCEPTION_WHILE_WRITING_FAULT"
argument_list|,
name|fex
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// Cannot find the fault info, now we should check if we need to
comment|// set the cause message of the exception
name|String
name|config
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
operator|.
name|EXCEPTION_MESSAGE_CAUSE_ENABLED
argument_list|)
decl_stmt|;
if|if
condition|(
name|config
operator|!=
literal|null
operator|&&
name|Boolean
operator|.
name|valueOf
argument_list|(
name|config
argument_list|)
operator|.
name|booleanValue
argument_list|()
condition|)
block|{
name|StringBuffer
name|buffer
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|f
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|" Caused by: "
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|cause
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|f
operator|.
name|setMessage
argument_list|(
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|boolean
name|isDOMSupported
parameter_list|(
name|DataBinding
name|db
parameter_list|)
block|{
name|boolean
name|supportsDOM
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
range|:
name|db
operator|.
name|getSupportedWriterFormats
argument_list|()
control|)
block|{
if|if
condition|(
name|c
operator|.
name|equals
argument_list|(
name|Node
operator|.
name|class
argument_list|)
condition|)
block|{
name|supportsDOM
operator|=
literal|true
expr_stmt|;
block|}
block|}
return|return
name|supportsDOM
return|;
block|}
specifier|protected
name|Object
name|getFaultBean
parameter_list|(
name|Throwable
name|cause
parameter_list|,
name|FaultInfo
name|faultPart
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|cause
operator|instanceof
name|FaultInfoException
condition|)
block|{
try|try
block|{
name|Method
name|method
init|=
name|cause
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getFaultInfo"
argument_list|,
operator|new
name|Class
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
return|return
name|method
operator|.
name|invoke
argument_list|(
name|cause
argument_list|,
operator|new
name|Object
index|[
literal|0
index|]
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
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
argument_list|(
literal|"INVOKE_FAULT_INFO"
argument_list|,
name|LOG
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
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
argument_list|(
literal|"NO_FAULT_INFO_METHOD"
argument_list|,
name|LOG
argument_list|)
argument_list|,
name|e
argument_list|)
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
operator|new
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
argument_list|(
literal|"NO_ACCCESS_FAULT_INFO"
argument_list|,
name|LOG
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|cause
return|;
block|}
comment|/**      * Find the correct Fault part for a particular exception.      *       * @param op      * @param class1      * @return      */
specifier|public
name|FaultInfo
name|getFaultForClass
parameter_list|(
name|BindingOperationInfo
name|op
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|class1
parameter_list|)
block|{
for|for
control|(
name|BindingFaultInfo
name|bfi
range|:
name|op
operator|.
name|getFaults
argument_list|()
control|)
block|{
name|FaultInfo
name|faultInfo
init|=
name|bfi
operator|.
name|getFaultInfo
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|c
init|=
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|faultInfo
operator|.
name|getProperty
argument_list|(
name|Class
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|.
name|isAssignableFrom
argument_list|(
name|class1
argument_list|)
condition|)
block|{
return|return
name|faultInfo
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

