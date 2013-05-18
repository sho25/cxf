begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|wseventing
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
name|backend
operator|.
name|manager
operator|.
name|SubscriptionManager
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
name|ws
operator|.
name|eventing
operator|.
name|backend
operator|.
name|manager
operator|.
name|SubscriptionManagerImpl
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|SingletonSubscriptionManagerContainer
block|{
specifier|private
specifier|static
name|SubscriptionManager
name|instance
decl_stmt|;
specifier|private
name|SingletonSubscriptionManagerContainer
parameter_list|()
block|{      }
specifier|public
specifier|static
specifier|synchronized
name|SubscriptionManager
name|getInstance
parameter_list|()
block|{
if|if
condition|(
name|instance
operator|==
literal|null
condition|)
block|{
name|instance
operator|=
operator|new
name|SubscriptionManagerImpl
argument_list|(
literal|"http://localhost:8080/ws_eventing/services/SubscriptionManager"
argument_list|)
expr_stmt|;
block|}
return|return
name|instance
return|;
block|}
specifier|public
specifier|static
specifier|synchronized
name|void
name|destroy
parameter_list|()
block|{
name|instance
operator|=
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

