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
name|HolderOutInterceptor
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
name|HolderOutInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|HolderOutInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_LOGICAL
argument_list|)
expr_stmt|;
name|addBefore
argument_list|(
name|WrapperClassOutInterceptor
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
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|MessageContentsList
name|outObjects
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
name|OperationInfo
name|op
init|=
name|exchange
operator|.
name|getBindingOperationInfo
argument_list|()
operator|==
literal|null
condition|?
literal|null
else|:
name|exchange
operator|.
name|getBindingOperationInfo
argument_list|()
operator|.
name|getOperationInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"op: "
operator|+
name|op
argument_list|)
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|op
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"op.hasOutput(): "
operator|+
name|op
operator|.
name|hasOutput
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|op
operator|.
name|hasOutput
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"op.getOutput().size(): "
operator|+
name|op
operator|.
name|getOutput
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
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
name|LOG
operator|.
name|fine
argument_list|(
literal|"Returning."
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
operator|!
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
name|MessageContentsList
name|inObjects
init|=
name|MessageContentsList
operator|.
name|getContentsList
argument_list|(
name|exchange
operator|.
name|getInMessage
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|inObjects
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|inObjects
operator|!=
name|outObjects
condition|)
block|{
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|inObjects
operator|.
name|size
argument_list|()
condition|;
name|x
operator|++
control|)
block|{
name|Object
name|o
init|=
name|inObjects
operator|.
name|get
argument_list|(
name|x
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|Holder
condition|)
block|{
name|outObjects
operator|.
name|set
argument_list|(
name|x
operator|+
literal|1
argument_list|,
name|o
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|severe
argument_list|(
literal|"CANNOT_SET_HOLDER_OBJECTS"
argument_list|)
expr_stmt|;
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
literal|"CANNOT_SET_HOLDER_OBJECTS"
argument_list|,
name|LOG
argument_list|)
argument_list|)
throw|;
block|}
block|}
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
operator|>
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
argument_list|<
name|?
argument_list|>
name|holder
init|=
operator|(
name|Holder
argument_list|<
name|?
argument_list|>
operator|)
name|outObjects
operator|.
name|get
argument_list|(
name|part
argument_list|)
decl_stmt|;
name|outObjects
operator|.
name|put
argument_list|(
name|part
argument_list|,
name|holder
operator|.
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|holders
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|outObjects
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|outObjects
operator|.
name|size
argument_list|()
condition|;
name|x
operator|++
control|)
block|{
name|Object
name|o
init|=
name|outObjects
operator|.
name|get
argument_list|(
name|x
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|Holder
condition|)
block|{
name|outObjects
operator|.
name|set
argument_list|(
name|x
argument_list|,
operator|(
operator|(
name|Holder
argument_list|<
name|?
argument_list|>
operator|)
name|o
operator|)
operator|.
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|holders
operator|.
name|set
argument_list|(
name|x
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
name|message
operator|.
name|put
argument_list|(
name|HolderInInterceptor
operator|.
name|CLIENT_HOLDERS
argument_list|,
name|holders
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

