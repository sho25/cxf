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
name|ws
operator|.
name|eventing
operator|.
name|backend
operator|.
name|notification
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
name|ws
operator|.
name|eventing
operator|.
name|shared
operator|.
name|EventingConstants
import|;
end_import

begin_enum
specifier|public
enum|enum
name|SubscriptionEndStatus
block|{
name|DELIVERY_FAILURE
parameter_list|(
name|EventingConstants
operator|.
name|SUBSCRIPTION_END_DELIVERY_FAILURE
parameter_list|)
operator|,
constructor|SOURCE_SHUTTING_DOWN(EventingConstants.SUBSCRIPTION_END_SHUTTING_DOWN
block|)
enum|,
name|SOURCE_CANCELLING
argument_list|(
name|EventingConstants
operator|.
name|SUBSCRIPTION_END_SOURCE_CANCELLING
argument_list|)
enum|;
end_enum

begin_decl_stmt
specifier|private
name|String
name|namespace
decl_stmt|;
end_decl_stmt

begin_expr_stmt
name|SubscriptionEndStatus
argument_list|(
name|String
name|namespace
argument_list|)
block|{
name|this
operator|.
name|namespace
operator|=
name|namespace
block|;     }
expr|@
name|Override
specifier|public
name|String
name|toString
argument_list|()
block|{
return|return
name|namespace
return|;
block|}
end_expr_stmt

unit|}
end_unit

