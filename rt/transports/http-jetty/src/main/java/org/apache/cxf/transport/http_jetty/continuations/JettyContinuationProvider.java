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
name|transport
operator|.
name|http_jetty
operator|.
name|continuations
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
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
name|continuations
operator|.
name|Continuation
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
name|continuations
operator|.
name|ContinuationProvider
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

begin_class
specifier|public
class|class
name|JettyContinuationProvider
implements|implements
name|ContinuationProvider
block|{
specifier|private
name|HttpServletRequest
name|request
decl_stmt|;
specifier|private
name|Message
name|inMessage
decl_stmt|;
specifier|private
name|JettyContinuationWrapper
name|wrapper
decl_stmt|;
specifier|public
name|JettyContinuationProvider
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
name|request
operator|=
name|req
expr_stmt|;
name|this
operator|.
name|inMessage
operator|=
name|m
expr_stmt|;
block|}
specifier|public
name|Continuation
name|getContinuation
parameter_list|()
block|{
return|return
name|getContinuation
argument_list|(
literal|true
argument_list|)
return|;
block|}
specifier|public
name|JettyContinuationWrapper
name|getContinuation
parameter_list|(
name|boolean
name|create
parameter_list|)
block|{
if|if
condition|(
name|inMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|isOneWay
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|wrapper
operator|==
literal|null
operator|&&
name|create
condition|)
block|{
name|wrapper
operator|=
operator|new
name|JettyContinuationWrapper
argument_list|(
name|request
argument_list|,
name|inMessage
argument_list|)
expr_stmt|;
block|}
return|return
name|wrapper
return|;
block|}
block|}
end_class

end_unit

