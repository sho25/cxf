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
name|brave
package|;
end_package

begin_class
specifier|public
specifier|abstract
class|class
name|BraveTestSupport
block|{
specifier|public
specifier|static
specifier|final
name|String
name|TRACE_ID_NAME
init|=
literal|"X-B3-TraceId"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SPAN_ID_NAME
init|=
literal|"X-B3-SpanId"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PARENT_SPAN_ID_NAME
init|=
literal|"X-B3-ParentSpanId"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SAMPLED_NAME
init|=
literal|"X-B3-Sampled"
decl_stmt|;
specifier|public
specifier|static
class|class
name|SpanId
block|{
specifier|private
name|long
name|traceId
decl_stmt|;
specifier|private
name|long
name|spanId
decl_stmt|;
specifier|private
name|Long
name|parentId
decl_stmt|;
specifier|private
name|boolean
name|sampled
decl_stmt|;
specifier|public
name|SpanId
name|traceId
parameter_list|(
name|long
name|id
parameter_list|)
block|{
name|this
operator|.
name|traceId
operator|=
name|id
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|SpanId
name|parentId
parameter_list|(
name|Long
name|id
parameter_list|)
block|{
name|this
operator|.
name|parentId
operator|=
name|id
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|SpanId
name|spanId
parameter_list|(
name|long
name|id
parameter_list|)
block|{
name|this
operator|.
name|spanId
operator|=
name|id
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|SpanId
name|sampled
parameter_list|(
name|boolean
name|s
parameter_list|)
block|{
name|this
operator|.
name|sampled
operator|=
name|s
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|long
name|traceId
parameter_list|()
block|{
return|return
name|traceId
return|;
block|}
specifier|public
name|long
name|spanId
parameter_list|()
block|{
return|return
name|spanId
return|;
block|}
specifier|public
name|Long
name|parentId
parameter_list|()
block|{
return|return
name|parentId
return|;
block|}
specifier|public
name|boolean
name|sampled
parameter_list|()
block|{
return|return
name|sampled
return|;
block|}
block|}
block|}
end_class

end_unit
