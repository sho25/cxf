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
name|jaxrs
operator|.
name|rx2
operator|.
name|server
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|impl
operator|.
name|AsyncResponseImpl
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
name|jaxrs
operator|.
name|reactivestreams
operator|.
name|server
operator|.
name|AbstractReactiveInvoker
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
name|io
operator|.
name|reactivex
operator|.
name|Flowable
import|;
end_import

begin_import
import|import
name|io
operator|.
name|reactivex
operator|.
name|Observable
import|;
end_import

begin_import
import|import
name|io
operator|.
name|reactivex
operator|.
name|Single
import|;
end_import

begin_class
specifier|public
class|class
name|ReactiveIOInvoker
extends|extends
name|AbstractReactiveInvoker
block|{
specifier|protected
name|AsyncResponseImpl
name|checkFutureResponse
parameter_list|(
name|Message
name|inMessage
parameter_list|,
name|Object
name|result
parameter_list|)
block|{
if|if
condition|(
name|result
operator|instanceof
name|Flowable
condition|)
block|{
return|return
name|handleFlowable
argument_list|(
name|inMessage
argument_list|,
operator|(
name|Flowable
argument_list|<
name|?
argument_list|>
operator|)
name|result
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|result
operator|instanceof
name|Single
condition|)
block|{
return|return
name|handleSingle
argument_list|(
name|inMessage
argument_list|,
operator|(
name|Single
argument_list|<
name|?
argument_list|>
operator|)
name|result
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|result
operator|instanceof
name|Observable
condition|)
block|{
return|return
name|handleObservable
argument_list|(
name|inMessage
argument_list|,
operator|(
name|Observable
argument_list|<
name|?
argument_list|>
operator|)
name|result
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|protected
name|AsyncResponseImpl
name|handleSingle
parameter_list|(
name|Message
name|inMessage
parameter_list|,
name|Single
argument_list|<
name|?
argument_list|>
name|single
parameter_list|)
block|{
specifier|final
name|AsyncResponseImpl
name|asyncResponse
init|=
operator|new
name|AsyncResponseImpl
argument_list|(
name|inMessage
argument_list|)
decl_stmt|;
name|single
operator|.
name|subscribe
argument_list|(
name|v
lambda|->
name|asyncResponse
operator|.
name|resume
argument_list|(
name|v
argument_list|)
argument_list|,
name|t
lambda|->
name|handleThrowable
argument_list|(
name|asyncResponse
argument_list|,
name|t
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|asyncResponse
return|;
block|}
specifier|protected
name|AsyncResponseImpl
name|handleFlowable
parameter_list|(
name|Message
name|inMessage
parameter_list|,
name|Flowable
argument_list|<
name|?
argument_list|>
name|f
parameter_list|)
block|{
specifier|final
name|AsyncResponseImpl
name|asyncResponse
init|=
operator|new
name|AsyncResponseImpl
argument_list|(
name|inMessage
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|isStreamingSubscriberUsed
argument_list|(
name|f
argument_list|,
name|asyncResponse
argument_list|,
name|inMessage
argument_list|)
condition|)
block|{
name|f
operator|.
name|subscribe
argument_list|(
name|v
lambda|->
name|asyncResponse
operator|.
name|resume
argument_list|(
name|v
argument_list|)
argument_list|,
name|t
lambda|->
name|handleThrowable
argument_list|(
name|asyncResponse
argument_list|,
name|t
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|asyncResponse
return|;
block|}
specifier|protected
name|AsyncResponseImpl
name|handleObservable
parameter_list|(
name|Message
name|inMessage
parameter_list|,
name|Observable
argument_list|<
name|?
argument_list|>
name|obs
parameter_list|)
block|{
specifier|final
name|AsyncResponseImpl
name|asyncResponse
init|=
operator|new
name|AsyncResponseImpl
argument_list|(
name|inMessage
argument_list|)
decl_stmt|;
name|obs
operator|.
name|subscribe
argument_list|(
name|v
lambda|->
name|asyncResponse
operator|.
name|resume
argument_list|(
name|v
argument_list|)
argument_list|,
name|t
lambda|->
name|handleThrowable
argument_list|(
name|asyncResponse
argument_list|,
name|t
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|asyncResponse
return|;
block|}
block|}
end_class

end_unit

