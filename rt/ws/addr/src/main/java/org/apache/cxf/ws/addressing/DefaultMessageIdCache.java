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
name|addressing
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|util
operator|.
name|ConcurrentHashSet
import|;
end_import

begin_comment
comment|/**  * An implementation that uses a simple set to store received message IDs.  * Note that this implementation does not make any attempt to flush older  * message IDs or to persist the message IDs outside of this instance.   */
end_comment

begin_class
specifier|public
class|class
name|DefaultMessageIdCache
implements|implements
name|MessageIdCache
block|{
comment|/**      * The set of message IDs.      */
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|messageIdSet
init|=
operator|new
name|ConcurrentHashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|boolean
name|checkUniquenessAndCacheId
parameter_list|(
name|String
name|messageId
parameter_list|)
block|{
return|return
name|this
operator|.
name|messageIdSet
operator|.
name|add
argument_list|(
name|messageId
argument_list|)
return|;
block|}
specifier|protected
name|Set
argument_list|<
name|String
argument_list|>
name|getMessageIdSet
parameter_list|()
block|{
return|return
name|this
operator|.
name|messageIdSet
return|;
block|}
block|}
end_class

end_unit

