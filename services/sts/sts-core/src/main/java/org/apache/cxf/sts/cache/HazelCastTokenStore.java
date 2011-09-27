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
name|sts
operator|.
name|cache
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|Hazelcast
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|IMap
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
name|common
operator|.
name|util
operator|.
name|StringUtils
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
name|helpers
operator|.
name|CastUtils
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
name|security
operator|.
name|tokenstore
operator|.
name|SecurityToken
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
name|security
operator|.
name|tokenstore
operator|.
name|SecurityToken
operator|.
name|State
import|;
end_import

begin_class
specifier|public
class|class
name|HazelCastTokenStore
implements|implements
name|STSTokenStore
block|{
name|IMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cacheMap
decl_stmt|;
name|boolean
name|autoRemove
init|=
literal|true
decl_stmt|;
specifier|public
name|HazelCastTokenStore
parameter_list|(
name|String
name|mapName
parameter_list|)
block|{
name|cacheMap
operator|=
name|Hazelcast
operator|.
name|getDefaultInstance
argument_list|()
operator|.
name|getMap
argument_list|(
name|mapName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|add
parameter_list|(
name|SecurityToken
name|token
parameter_list|)
block|{
if|if
condition|(
name|token
operator|!=
literal|null
operator|&&
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|token
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
name|cacheMap
operator|.
name|put
argument_list|(
name|token
operator|.
name|getId
argument_list|()
argument_list|,
name|token
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|update
parameter_list|(
name|SecurityToken
name|token
parameter_list|)
block|{
if|if
condition|(
name|autoRemove
operator|&&
operator|(
name|token
operator|.
name|getState
argument_list|()
operator|==
name|State
operator|.
name|EXPIRED
operator|||
name|token
operator|.
name|getState
argument_list|()
operator|==
name|State
operator|.
name|CANCELLED
operator|)
condition|)
block|{
name|remove
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|add
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|remove
parameter_list|(
name|SecurityToken
name|token
parameter_list|)
block|{
if|if
condition|(
name|token
operator|!=
literal|null
operator|&&
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|token
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
name|cacheMap
operator|.
name|remove
argument_list|(
name|token
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getTokenIdentifiers
parameter_list|()
block|{
return|return
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|cacheMap
operator|.
name|keySet
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|SecurityToken
argument_list|>
name|getExpiredTokens
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|SecurityToken
argument_list|>
name|getValidTokens
parameter_list|()
block|{
return|return
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|cacheMap
operator|.
name|keySet
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|SecurityToken
argument_list|>
name|getRenewedTokens
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|SecurityToken
argument_list|>
name|getCancelledTokens
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
specifier|public
name|SecurityToken
name|getToken
parameter_list|(
name|String
name|id
parameter_list|)
block|{
return|return
operator|(
name|SecurityToken
operator|)
name|cacheMap
operator|.
name|get
argument_list|(
name|id
argument_list|)
return|;
block|}
specifier|public
name|SecurityToken
name|getTokenByAssociatedHash
parameter_list|(
name|int
name|hashCode
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|removeExpiredTokens
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
block|}
specifier|public
name|void
name|removeCancelledTokens
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
block|}
specifier|public
name|void
name|setAutoRemoveTokens
parameter_list|(
name|boolean
name|auto
parameter_list|)
block|{
name|this
operator|.
name|autoRemove
operator|=
name|auto
expr_stmt|;
block|}
specifier|public
name|void
name|add
parameter_list|(
name|SecurityToken
name|token
parameter_list|,
name|Integer
name|timeToLiveSeconds
parameter_list|)
block|{
if|if
condition|(
name|token
operator|!=
literal|null
operator|&&
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|token
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
name|cacheMap
operator|.
name|put
argument_list|(
name|token
operator|.
name|getId
argument_list|()
argument_list|,
name|token
argument_list|,
name|timeToLiveSeconds
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

