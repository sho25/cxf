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
name|List
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
name|AbstractOutDatabindingInterceptor
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
name|BareOutInterceptor
extends|extends
name|AbstractOutDatabindingInterceptor
block|{
specifier|public
name|BareOutInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|MARSHAL
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
block|{
name|Exchange
name|exchange
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|BindingOperationInfo
name|operation
init|=
name|exchange
operator|.
name|getBindingOperationInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|operation
operator|==
literal|null
condition|)
block|{
return|return;
block|}
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
if|if
condition|(
name|objs
operator|==
literal|null
operator|||
name|objs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|List
argument_list|<
name|MessagePartInfo
argument_list|>
name|parts
init|=
literal|null
decl_stmt|;
name|BindingMessageInfo
name|bmsg
init|=
literal|null
decl_stmt|;
name|boolean
name|client
init|=
name|isRequestor
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|client
condition|)
block|{
if|if
condition|(
name|operation
operator|.
name|getOutput
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|bmsg
operator|=
name|operation
operator|.
name|getOutput
argument_list|()
expr_stmt|;
name|parts
operator|=
name|bmsg
operator|.
name|getMessageParts
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// partial response to oneway
return|return;
block|}
block|}
else|else
block|{
name|bmsg
operator|=
name|operation
operator|.
name|getInput
argument_list|()
expr_stmt|;
name|parts
operator|=
name|bmsg
operator|.
name|getMessageParts
argument_list|()
expr_stmt|;
block|}
name|writeParts
argument_list|(
name|message
argument_list|,
name|exchange
argument_list|,
name|operation
argument_list|,
name|objs
argument_list|,
name|parts
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

