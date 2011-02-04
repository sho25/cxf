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
name|management
operator|.
name|web
operator|.
name|browser
operator|.
name|client
operator|.
name|event
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Nonnull
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|event
operator|.
name|shared
operator|.
name|GwtEvent
import|;
end_import

begin_class
specifier|public
class|class
name|ChangedSubscriptionsEvent
extends|extends
name|GwtEvent
argument_list|<
name|ChangedSubscriptionsEventHandler
argument_list|>
block|{
specifier|public
specifier|static
specifier|final
name|Type
argument_list|<
name|ChangedSubscriptionsEventHandler
argument_list|>
name|TYPE
init|=
operator|new
name|Type
argument_list|<
name|ChangedSubscriptionsEventHandler
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|Override
annotation|@
name|Nonnull
specifier|public
name|Type
argument_list|<
name|ChangedSubscriptionsEventHandler
argument_list|>
name|getAssociatedType
parameter_list|()
block|{
return|return
name|TYPE
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|dispatch
parameter_list|(
annotation|@
name|Nonnull
specifier|final
name|ChangedSubscriptionsEventHandler
name|handler
parameter_list|)
block|{
name|handler
operator|.
name|onChangedSubscriptions
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

