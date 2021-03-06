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
name|microprofile
operator|.
name|rest
operator|.
name|client
operator|.
name|tracing
operator|.
name|brave
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
name|zipkin2
operator|.
name|Span
import|;
end_import

begin_import
import|import
name|zipkin2
operator|.
name|reporter
operator|.
name|Reporter
import|;
end_import

begin_class
specifier|public
class|class
name|TestSpanReporter
implements|implements
name|Reporter
argument_list|<
name|Span
argument_list|>
block|{
specifier|private
specifier|static
name|List
argument_list|<
name|Span
argument_list|>
name|spans
init|=
name|Collections
operator|.
name|synchronizedList
argument_list|(
operator|new
name|ArrayList
argument_list|<>
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|report
parameter_list|(
name|Span
name|span
parameter_list|)
block|{
name|spans
operator|.
name|add
argument_list|(
name|span
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|Span
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

