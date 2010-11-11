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
name|List
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
name|Holder
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

begin_class
specifier|public
class|class
name|HolderInInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|public
specifier|static
specifier|final
name|String
name|CLIENT_HOLDERS
init|=
literal|"client.holders"
decl_stmt|;
specifier|public
name|HolderInInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_INVOKE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
name|Exchange
name|exchange
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|BindingOperationInfo
name|bop
init|=
name|exchange
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
name|OperationInfo
name|op
init|=
name|bop
operator|.
name|getOperationInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|op
operator|==
literal|null
operator|||
operator|!
name|op
operator|.
name|hasOutput
argument_list|()
operator|||
name|op
operator|.
name|getOutput
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
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
name|op
operator|.
name|getOutput
argument_list|()
operator|.
name|getMessageParts
argument_list|()
decl_stmt|;
name|boolean
name|client
init|=
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
decl_stmt|;
if|if
condition|(
name|client
condition|)
block|{
name|List
argument_list|<
name|Holder
argument_list|>
name|outHolders
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
operator|)
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getOutMessage
argument_list|()
operator|.
name|get
argument_list|(
name|CLIENT_HOLDERS
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|MessagePartInfo
name|part
range|:
name|parts
control|)
block|{
if|if
condition|(
name|part
operator|.
name|getIndex
argument_list|()
operator|!=
literal|0
operator|&&
name|part
operator|.
name|getTypeClass
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Holder
name|holder
init|=
operator|(
name|Holder
operator|)
name|outHolders
operator|.
name|get
argument_list|(
name|part
operator|.
name|getIndex
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|holder
operator|!=
literal|null
condition|)
block|{
name|holder
operator|.
name|value
operator|=
name|inObjects
operator|.
name|get
argument_list|(
name|part
argument_list|)
expr_stmt|;
name|inObjects
operator|.
name|put
argument_list|(
name|part
argument_list|,
name|holder
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
else|else
block|{
for|for
control|(
name|MessagePartInfo
name|part
range|:
name|parts
control|)
block|{
name|int
name|idx
init|=
name|part
operator|.
name|getIndex
argument_list|()
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|idx
operator|>=
literal|0
operator|&&
name|part
operator|.
name|getTypeClass
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|idx
operator|>=
name|inObjects
operator|.
name|size
argument_list|()
condition|)
block|{
name|inObjects
operator|.
name|set
argument_list|(
name|idx
argument_list|,
operator|new
name|Holder
argument_list|<
name|Object
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Object
name|o
init|=
name|inObjects
operator|.
name|get
argument_list|(
name|idx
argument_list|)
decl_stmt|;
name|inObjects
operator|.
name|set
argument_list|(
name|idx
argument_list|,
operator|new
name|Holder
argument_list|<
name|Object
argument_list|>
argument_list|(
name|o
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

