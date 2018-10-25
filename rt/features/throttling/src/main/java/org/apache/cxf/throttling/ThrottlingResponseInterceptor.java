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
name|throttling
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
name|Collections
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
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
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|ThrottlingResponseInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|public
name|ThrottlingResponseInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|SETUP
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
name|ThrottleResponse
name|rsp
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|ThrottleResponse
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|rsp
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|rsp
operator|.
name|getResponseCode
argument_list|()
operator|>
literal|0
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|RESPONSE_CODE
argument_list|,
name|rsp
operator|.
name|getResponseCode
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|rsp
operator|.
name|getErrorMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|ERROR_MESSAGE
argument_list|,
name|rsp
operator|.
name|getErrorMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|headers
operator|==
literal|null
condition|)
block|{
name|headers
operator|=
operator|new
name|TreeMap
argument_list|<>
argument_list|(
name|String
operator|.
name|CASE_INSENSITIVE_ORDER
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|,
name|headers
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|e
range|:
name|rsp
operator|.
name|getResponseHeaders
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|r
init|=
name|headers
operator|.
name|get
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|==
literal|null
condition|)
block|{
name|r
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|r
argument_list|)
expr_stmt|;
block|}
name|r
operator|.
name|add
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|rsp
operator|.
name|getResponseCode
argument_list|()
operator|==
literal|503
operator|&&
name|rsp
operator|.
name|getDelay
argument_list|()
operator|>
literal|0
operator|&&
operator|!
name|rsp
operator|.
name|getResponseHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|"Retry-After"
argument_list|)
condition|)
block|{
name|String
name|retryAfter
init|=
name|Long
operator|.
name|toString
argument_list|(
name|rsp
operator|.
name|getDelay
argument_list|()
operator|/
literal|1000
argument_list|)
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"Retry-After"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|retryAfter
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|ThrottlingCounter
name|tCounter
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|ThrottlingCounter
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|tCounter
operator|!=
literal|null
condition|)
block|{
name|tCounter
operator|.
name|decrementAndGet
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

