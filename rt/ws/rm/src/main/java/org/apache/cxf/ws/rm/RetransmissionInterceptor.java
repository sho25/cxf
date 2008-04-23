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
name|io
operator|.
name|OutputStream
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
name|StaxOutInterceptor
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
name|io
operator|.
name|WriteOnCloseOutputStream
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

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|RetransmissionInterceptor
extends|extends
name|AbstractPhaseInterceptor
block|{
name|RMManager
name|manager
decl_stmt|;
specifier|public
name|RetransmissionInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_STREAM
argument_list|)
expr_stmt|;
name|addBefore
argument_list|(
name|StaxOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|RMManager
name|getManager
parameter_list|()
block|{
return|return
name|manager
return|;
block|}
specifier|public
name|void
name|setManager
parameter_list|(
name|RMManager
name|manager
parameter_list|)
block|{
name|this
operator|.
name|manager
operator|=
name|manager
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
name|handle
argument_list|(
name|message
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|handleFault
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|handle
argument_list|(
name|message
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|void
name|handle
parameter_list|(
name|Message
name|message
parameter_list|,
name|boolean
name|isFault
parameter_list|)
block|{
if|if
condition|(
literal|null
operator|==
name|getManager
argument_list|()
operator|.
name|getRetransmissionQueue
argument_list|()
condition|)
block|{
return|return;
block|}
name|OutputStream
name|os
init|=
name|message
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|os
condition|)
block|{
return|return;
block|}
name|WriteOnCloseOutputStream
name|stream
init|=
name|RMUtils
operator|.
name|createCachedStream
argument_list|(
name|message
argument_list|,
name|os
argument_list|)
decl_stmt|;
name|stream
operator|.
name|registerCallback
argument_list|(
operator|new
name|RetransmissionCallback
argument_list|(
name|message
argument_list|,
name|getManager
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

