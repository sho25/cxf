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
name|systest
operator|.
name|jaeger
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
name|io
operator|.
name|jaegertracing
operator|.
name|internal
operator|.
name|JaegerSpan
import|;
end_import

begin_import
import|import
name|io
operator|.
name|jaegertracing
operator|.
name|internal
operator|.
name|exceptions
operator|.
name|SenderException
import|;
end_import

begin_import
import|import
name|io
operator|.
name|jaegertracing
operator|.
name|spi
operator|.
name|Sender
import|;
end_import

begin_class
specifier|public
class|class
name|TestSender
implements|implements
name|Sender
block|{
specifier|private
specifier|static
name|List
argument_list|<
name|JaegerSpan
argument_list|>
name|spans
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|int
name|append
parameter_list|(
name|JaegerSpan
name|span
parameter_list|)
throws|throws
name|SenderException
block|{
name|spans
operator|.
name|add
argument_list|(
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
specifier|public
specifier|static
name|List
argument_list|<
name|JaegerSpan
argument_list|>
name|getAllSpans
parameter_list|()
block|{
return|return
name|spans
return|;
block|}
specifier|public
specifier|static
name|void
name|clear
parameter_list|()
block|{
name|spans
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

