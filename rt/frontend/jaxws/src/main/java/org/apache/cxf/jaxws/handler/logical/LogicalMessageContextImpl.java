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
name|handler
operator|.
name|logical
package|;
end_package

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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|LogicalMessage
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|handler
operator|.
name|LogicalMessageContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|handler
operator|.
name|MessageContext
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
name|jaxws
operator|.
name|context
operator|.
name|WrappedMessageContext
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

begin_class
specifier|public
class|class
name|LogicalMessageContextImpl
extends|extends
name|WrappedMessageContext
implements|implements
name|LogicalMessageContext
block|{
specifier|public
name|LogicalMessageContextImpl
parameter_list|(
name|Message
name|wrapped
parameter_list|)
block|{
name|super
argument_list|(
name|wrapped
argument_list|,
name|Scope
operator|.
name|HANDLER
argument_list|)
expr_stmt|;
block|}
specifier|public
name|LogicalMessage
name|getMessage
parameter_list|()
block|{
return|return
operator|new
name|LogicalMessageImpl
argument_list|(
name|this
argument_list|)
return|;
block|}
specifier|public
name|Object
name|get
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|Object
name|o
init|=
name|super
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|MessageContext
operator|.
name|HTTP_RESPONSE_HEADERS
operator|.
name|equals
argument_list|(
name|key
argument_list|)
operator|||
name|MessageContext
operator|.
name|HTTP_REQUEST_HEADERS
operator|.
name|equals
argument_list|(
name|key
argument_list|)
operator|)
operator|&&
name|o
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|o
operator|)
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
operator|!
name|isResponse
argument_list|()
operator|&&
name|isOutbound
argument_list|()
operator|&&
name|MessageContext
operator|.
name|HTTP_RESPONSE_HEADERS
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|isRequestor
argument_list|()
operator|&&
operator|!
name|isOutbound
argument_list|()
operator|&&
name|MessageContext
operator|.
name|HTTP_REQUEST_HEADERS
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
block|}
return|return
name|o
return|;
block|}
block|}
end_class

end_unit

