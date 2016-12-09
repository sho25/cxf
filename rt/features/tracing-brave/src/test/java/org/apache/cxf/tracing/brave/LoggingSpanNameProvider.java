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
name|http
operator|.
name|HttpRequest
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
name|http
operator|.
name|SpanNameProvider
import|;
end_import

begin_class
specifier|public
class|class
name|LoggingSpanNameProvider
implements|implements
name|SpanNameProvider
block|{
annotation|@
name|Override
specifier|public
name|String
name|spanName
parameter_list|(
name|HttpRequest
name|request
parameter_list|)
block|{
return|return
operator|(
name|request
operator|instanceof
name|SpanNameProvider
operator|)
condition|?
operator|(
operator|(
name|SpanNameProvider
operator|)
name|request
operator|)
operator|.
name|spanName
argument_list|(
name|request
argument_list|)
else|:
literal|""
return|;
block|}
block|}
end_class

end_unit
