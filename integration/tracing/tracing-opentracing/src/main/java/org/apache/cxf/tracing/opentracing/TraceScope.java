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
name|opentracing
package|;
end_package

begin_import
import|import
name|io
operator|.
name|opentracing
operator|.
name|ActiveSpan
import|;
end_import

begin_import
import|import
name|io
operator|.
name|opentracing
operator|.
name|ActiveSpan
operator|.
name|Continuation
import|;
end_import

begin_class
specifier|public
class|class
name|TraceScope
block|{
specifier|private
specifier|final
name|ActiveSpan
name|span
decl_stmt|;
specifier|private
specifier|final
name|Continuation
name|continuation
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|managed
decl_stmt|;
name|TraceScope
parameter_list|(
specifier|final
name|ActiveSpan
name|span
parameter_list|,
specifier|final
name|Continuation
name|continuation
parameter_list|)
block|{
name|this
argument_list|(
name|span
argument_list|,
name|continuation
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|TraceScope
parameter_list|(
specifier|final
name|ActiveSpan
name|span
parameter_list|,
specifier|final
name|Continuation
name|continuation
parameter_list|,
specifier|final
name|boolean
name|managed
parameter_list|)
block|{
name|this
operator|.
name|span
operator|=
name|span
expr_stmt|;
name|this
operator|.
name|continuation
operator|=
name|continuation
expr_stmt|;
name|this
operator|.
name|managed
operator|=
name|managed
expr_stmt|;
block|}
specifier|public
name|ActiveSpan
name|getSpan
parameter_list|()
block|{
return|return
name|span
return|;
block|}
specifier|public
name|Continuation
name|getContinuation
parameter_list|()
block|{
return|return
name|continuation
return|;
block|}
specifier|public
name|boolean
name|isManaged
parameter_list|()
block|{
return|return
name|managed
return|;
block|}
block|}
end_class

end_unit

