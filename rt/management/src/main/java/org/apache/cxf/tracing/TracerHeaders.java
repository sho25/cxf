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
package|;
end_package

begin_class
specifier|public
specifier|final
class|class
name|TracerHeaders
block|{
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_SPAN_ID
init|=
literal|"org.apache.cxf.tracing.header.span_id"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_HEADER_SPAN_ID
init|=
literal|"X-Span-Id"
decl_stmt|;
specifier|private
name|TracerHeaders
parameter_list|()
block|{     }
block|}
end_class

end_unit

