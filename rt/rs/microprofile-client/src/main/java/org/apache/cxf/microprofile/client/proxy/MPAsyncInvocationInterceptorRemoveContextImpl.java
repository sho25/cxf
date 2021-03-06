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
name|microprofile
operator|.
name|client
operator|.
name|proxy
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
name|eclipse
operator|.
name|microprofile
operator|.
name|rest
operator|.
name|client
operator|.
name|ext
operator|.
name|AsyncInvocationInterceptor
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|MPAsyncInvocationInterceptorRemoveContextImpl
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
name|MPAsyncInvocationInterceptorRemoveContextImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|MPAsyncInvocationInterceptorRemoveContextImpl
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
comment|/** {@inheritDoc}*/
annotation|@
name|Override
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
name|MPAsyncInvocationInterceptorImpl
name|aiii
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|MPAsyncInvocationInterceptorImpl
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|aiii
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|AsyncInvocationInterceptor
argument_list|>
name|interceptors
init|=
name|aiii
operator|.
name|getInterceptors
argument_list|()
decl_stmt|;
for|for
control|(
name|AsyncInvocationInterceptor
name|interceptor
range|:
name|interceptors
control|)
block|{
try|try
block|{
name|interceptor
operator|.
name|removeContext
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"ASYNC_INTERCEPTOR_EXCEPTION_REMOVE_CONTEXT"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|interceptor
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
block|,
name|t
block|}
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

