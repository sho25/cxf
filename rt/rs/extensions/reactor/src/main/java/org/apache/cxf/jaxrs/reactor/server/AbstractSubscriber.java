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
name|reactor
operator|.
name|server
package|;
end_package

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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|container
operator|.
name|AsyncResponse
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
name|ext
operator|.
name|StreamingResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|reactivestreams
operator|.
name|Subscriber
import|;
end_import

begin_import
import|import
name|org
operator|.
name|reactivestreams
operator|.
name|Subscription
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractSubscriber
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Subscriber
argument_list|<
name|T
argument_list|>
block|{
specifier|private
name|AsyncResponse
name|ar
decl_stmt|;
specifier|private
name|Subscription
name|subscription
decl_stmt|;
specifier|protected
name|AbstractSubscriber
parameter_list|(
name|AsyncResponse
name|ar
parameter_list|)
block|{
name|this
operator|.
name|ar
operator|=
name|ar
expr_stmt|;
block|}
specifier|public
name|void
name|resume
parameter_list|(
name|T
name|response
parameter_list|)
block|{
name|ar
operator|.
name|resume
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|resume
parameter_list|(
name|List
argument_list|<
name|T
argument_list|>
name|response
parameter_list|)
block|{
name|ar
operator|.
name|resume
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|resume
parameter_list|(
name|StreamingResponse
argument_list|<
name|T
argument_list|>
name|response
parameter_list|)
block|{
name|ar
operator|.
name|resume
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onError
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|ar
operator|.
name|resume
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onSubscribe
parameter_list|(
name|Subscription
name|inSubscription
parameter_list|)
block|{
name|this
operator|.
name|subscription
operator|=
name|inSubscription
expr_stmt|;
name|requestAll
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onNext
parameter_list|(
name|T
name|t
parameter_list|)
block|{
name|resume
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onComplete
parameter_list|()
block|{     }
specifier|protected
name|AsyncResponse
name|getAsyncResponse
parameter_list|()
block|{
return|return
name|ar
return|;
block|}
specifier|protected
name|Subscription
name|getSubscription
parameter_list|()
block|{
return|return
name|subscription
return|;
block|}
specifier|protected
name|void
name|requestNext
parameter_list|()
block|{
name|request
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|requestAll
parameter_list|()
block|{
name|request
argument_list|(
name|Long
operator|.
name|MAX_VALUE
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|final
name|void
name|request
parameter_list|(
name|long
name|elements
parameter_list|)
block|{
name|this
operator|.
name|subscription
operator|.
name|request
argument_list|(
name|elements
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

