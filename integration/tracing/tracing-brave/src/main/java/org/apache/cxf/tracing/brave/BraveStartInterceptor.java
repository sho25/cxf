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
name|tracing
operator|.
name|brave
package|;
end_package

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|kristofa
operator|.
name|brave
operator|.
name|Brave
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|kristofa
operator|.
name|brave
operator|.
name|ServerSpan
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
name|injection
operator|.
name|NoJSR250Annotations
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
name|Phase
import|;
end_import

begin_class
annotation|@
name|NoJSR250Annotations
specifier|public
class|class
name|BraveStartInterceptor
extends|extends
name|AbstractBraveInterceptor
block|{
specifier|public
name|BraveStartInterceptor
parameter_list|(
name|Brave
name|brave
parameter_list|)
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_INVOKE
argument_list|,
name|brave
argument_list|,
operator|new
name|ServerSpanNameProvider
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
specifier|final
name|ParsedMessage
name|parsed
init|=
operator|new
name|ParsedMessage
argument_list|(
name|message
argument_list|)
decl_stmt|;
specifier|final
name|TraceScopeHolder
argument_list|<
name|ServerSpan
argument_list|>
name|holder
init|=
name|super
operator|.
name|startTraceSpan
argument_list|(
name|parsed
operator|.
name|getHeaders
argument_list|()
argument_list|,
name|parsed
operator|.
name|getUri
argument_list|()
argument_list|,
name|parsed
operator|.
name|getHttpMethod
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|holder
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|TRACE_SPAN
argument_list|,
name|holder
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

