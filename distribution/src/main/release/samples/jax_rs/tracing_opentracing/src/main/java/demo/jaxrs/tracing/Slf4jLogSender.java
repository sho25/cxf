begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|jaxrs
operator|.
name|tracing
package|;
end_package

begin_import
import|import
name|com
operator|.
name|uber
operator|.
name|jaeger
operator|.
name|Span
import|;
end_import

begin_import
import|import
name|com
operator|.
name|uber
operator|.
name|jaeger
operator|.
name|exceptions
operator|.
name|SenderException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|uber
operator|.
name|jaeger
operator|.
name|senders
operator|.
name|Sender
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
specifier|public
class|class
name|Slf4jLogSender
implements|implements
name|Sender
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|Slf4jLogSender
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Override
specifier|public
name|int
name|append
parameter_list|(
name|Span
name|span
parameter_list|)
throws|throws
name|SenderException
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"{}"
argument_list|,
name|span
argument_list|)
expr_stmt|;
return|return
literal|0
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|flush
parameter_list|()
throws|throws
name|SenderException
block|{
return|return
literal|0
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|close
parameter_list|()
throws|throws
name|SenderException
block|{
return|return
literal|0
return|;
block|}
block|}
end_class

end_unit

