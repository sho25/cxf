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
name|ws
operator|.
name|rm
package|;
end_package

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
name|MessageUtils
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

begin_comment
comment|/**  * Interceptor used for InOrder delivery of messages to the destination. This works with  * {@link DestinationSequence} to allow only one message at a time from a particular sequence through to the  * destination (since otherwise there is no way to enforce in-order delivery).  */
end_comment

begin_class
specifier|public
class|class
name|RMDeliveryInterceptor
extends|extends
name|AbstractRMInterceptor
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
name|RMDeliveryInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|RMDeliveryInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|POST_INVOKE
argument_list|)
expr_stmt|;
block|}
comment|// Interceptor interface
specifier|public
name|void
name|handle
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|SequenceFault
throws|,
name|RMException
block|{
name|LOG
operator|.
name|entering
argument_list|(
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|"handleMessage"
argument_list|)
expr_stmt|;
name|Destination
name|dest
init|=
name|getManager
argument_list|()
operator|.
name|getDestination
argument_list|(
name|message
argument_list|)
decl_stmt|;
specifier|final
name|boolean
name|robust
init|=
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|message
operator|.
name|getContextualProperty
argument_list|(
name|Message
operator|.
name|ROBUST_ONEWAY
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|robust
condition|)
block|{
name|dest
operator|.
name|acknowledge
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
name|dest
operator|.
name|processingComplete
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

