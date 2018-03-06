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
name|io
operator|.
name|IOException
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
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|DataHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|imageio
operator|.
name|ImageIO
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
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
name|SoapMessage
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
name|interceptor
operator|.
name|AbstractSoapInterceptor
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
name|model
operator|.
name|SoapBodyInfo
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
name|IOUtils
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
name|interceptor
operator|.
name|StaxInEndingInterceptor
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
name|Attachment
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
name|MessagePartInfo
import|;
end_import

begin_class
specifier|public
class|class
name|SwAInInterceptor
extends|extends
name|AbstractSoapInterceptor
block|{
specifier|public
name|SwAInInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_INVOKE
argument_list|)
expr_stmt|;
name|getBefore
argument_list|()
operator|.
name|add
argument_list|(
name|HolderInInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|getAfter
argument_list|()
operator|.
name|add
argument_list|(
name|StaxInEndingInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|BindingOperationInfo
name|bop
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getBindingOperationInfo
argument_list|()
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
if|if
condition|(
name|bop
operator|.
name|isUnwrapped
argument_list|()
condition|)
block|{
name|bop
operator|=
name|bop
operator|.
name|getWrappedOperation
argument_list|()
expr_stmt|;
block|}
name|boolean
name|client
init|=
name|isRequestor
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|BindingMessageInfo
name|bmi
init|=
name|client
condition|?
name|bop
operator|.
name|getOutput
argument_list|()
else|:
name|bop
operator|.
name|getInput
argument_list|()
decl_stmt|;
if|if
condition|(
name|bmi
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|SoapBodyInfo
name|sbi
init|=
name|bmi
operator|.
name|getExtensor
argument_list|(
name|SoapBodyInfo
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|sbi
operator|==
literal|null
operator|||
name|sbi
operator|.
name|getAttachments
argument_list|()
operator|==
literal|null
operator|||
name|sbi
operator|.
name|getAttachments
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|Set
argument_list|<
name|Integer
argument_list|>
name|foundAtts
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|MessageContentsList
name|inObjects
init|=
name|MessageContentsList
operator|.
name|getContentsList
argument_list|(
name|message
argument_list|)
decl_stmt|;
for|for
control|(
name|MessagePartInfo
name|mpi
range|:
name|sbi
operator|.
name|getAttachments
argument_list|()
control|)
block|{
name|String
name|partName
init|=
name|mpi
operator|.
name|getConcreteName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
decl_stmt|;
name|String
name|start
init|=
name|partName
operator|+
literal|"="
decl_stmt|;
name|boolean
name|found
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|foundAtts
operator|.
name|contains
argument_list|(
name|mpi
operator|.
name|getIndex
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|foundAtts
operator|.
name|add
argument_list|(
name|mpi
operator|.
name|getIndex
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Attachment
name|a
range|:
name|message
operator|.
name|getAttachments
argument_list|()
control|)
block|{
if|if
condition|(
name|a
operator|.
name|getId
argument_list|()
operator|.
name|startsWith
argument_list|(
name|start
argument_list|)
condition|)
block|{
name|DataHandler
name|dh
init|=
name|a
operator|.
name|getDataHandler
argument_list|()
decl_stmt|;
name|String
name|ct
init|=
name|dh
operator|.
name|getContentType
argument_list|()
decl_stmt|;
name|Object
name|o
init|=
literal|null
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|typeClass
init|=
name|mpi
operator|.
name|getTypeClass
argument_list|()
decl_stmt|;
if|if
condition|(
name|DataHandler
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|typeClass
argument_list|)
condition|)
block|{
name|o
operator|=
name|dh
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|String
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|typeClass
argument_list|)
condition|)
block|{
try|try
block|{
comment|//o = IOUtils.readBytesFromStream(dh.getInputStream());
name|o
operator|=
name|dh
operator|.
name|getContent
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
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
block|}
elseif|else
if|if
condition|(
name|byte
index|[]
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|typeClass
argument_list|)
condition|)
block|{
try|try
block|{
name|o
operator|=
name|IOUtils
operator|.
name|readBytesFromStream
argument_list|(
name|dh
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
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
block|}
elseif|else
if|if
condition|(
name|ct
operator|.
name|startsWith
argument_list|(
literal|"image/"
argument_list|)
condition|)
block|{
try|try
block|{
name|o
operator|=
name|ImageIO
operator|.
name|read
argument_list|(
name|dh
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
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
block|}
elseif|else
if|if
condition|(
name|ct
operator|.
name|startsWith
argument_list|(
literal|"text/xml"
argument_list|)
operator|||
name|ct
operator|.
name|startsWith
argument_list|(
literal|"application/xml"
argument_list|)
condition|)
block|{
try|try
block|{
name|o
operator|=
operator|new
name|StreamSource
argument_list|(
name|dh
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
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
block|}
else|else
block|{
name|o
operator|=
name|dh
expr_stmt|;
block|}
name|inObjects
operator|.
name|put
argument_list|(
name|mpi
argument_list|,
name|o
argument_list|)
expr_stmt|;
name|found
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|found
condition|)
block|{
name|inObjects
operator|.
name|put
argument_list|(
name|mpi
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

