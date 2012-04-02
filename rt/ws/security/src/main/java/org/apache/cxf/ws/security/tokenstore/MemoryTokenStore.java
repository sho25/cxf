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
name|security
operator|.
name|tokenstore
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|Date
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Map
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
name|ConcurrentHashMap
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

begin_comment
comment|/**  * A simple HashMap-based TokenStore. The default TTL is 5 minutes and the max TTL is 1 hour.  */
end_comment

begin_class
specifier|public
class|class
name|MemoryTokenStore
implements|implements
name|TokenStore
block|{
specifier|public
specifier|static
specifier|final
name|long
name|DEFAULT_TTL
init|=
literal|60L
operator|*
literal|5L
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|long
name|MAX_TTL
init|=
name|DEFAULT_TTL
operator|*
literal|12L
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|CacheEntry
argument_list|>
name|tokens
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|CacheEntry
argument_list|>
argument_list|()
decl_stmt|;
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
name|CacheEntry
name|cacheEntry
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|token
operator|.
name|getExpires
argument_list|()
operator|==
literal|null
condition|)
block|{
name|Date
name|expires
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|long
name|currentTime
init|=
name|expires
operator|.
name|getTime
argument_list|()
decl_stmt|;
name|expires
operator|.
name|setTime
argument_list|(
name|currentTime
operator|+
operator|(
name|DEFAULT_TTL
operator|*
literal|1000L
operator|)
argument_list|)
expr_stmt|;
name|cacheEntry
operator|=
operator|new
name|CacheEntry
argument_list|(
name|token
argument_list|,
name|expires
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Date
name|expires
init|=
name|token
operator|.
name|getExpires
argument_list|()
decl_stmt|;
name|Date
name|current
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|long
name|expiryTime
init|=
name|expires
operator|.
name|getTime
argument_list|()
operator|-
name|current
operator|.
name|getTime
argument_list|()
decl_stmt|;
if|if
condition|(
name|expiryTime
argument_list|<
literal|0
operator|||
name|expiryTime
argument_list|>
argument_list|(
name|MAX_TTL
operator|*
literal|1000L
argument_list|)
condition|)
block|{
name|expires
operator|.
name|setTime
argument_list|(
name|current
operator|.
name|getTime
argument_list|()
operator|+
operator|(
name|DEFAULT_TTL
operator|*
literal|1000L
operator|)
argument_list|)
expr_stmt|;
block|}
name|cacheEntry
operator|=
operator|new
name|CacheEntry
argument_list|(
name|token
argument_list|,
name|expires
argument_list|)
expr_stmt|;
block|}
name|tokens
operator|.
name|put
argument_list|(
name|token
operator|.
name|getId
argument_list|()
argument_list|,
name|cacheEntry
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
name|tokens
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
name|processTokenExpiry
argument_list|()
expr_stmt|;
return|return
name|tokens
operator|.
name|keySet
argument_list|()
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
name|List
argument_list|<
name|SecurityToken
argument_list|>
name|expiredTokens
init|=
operator|new
name|ArrayList
argument_list|<
name|SecurityToken
argument_list|>
argument_list|()
decl_stmt|;
name|Date
name|current
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|tokens
init|)
block|{
for|for
control|(
name|String
name|id
range|:
name|tokens
operator|.
name|keySet
argument_list|()
control|)
block|{
name|CacheEntry
name|cacheEntry
init|=
name|tokens
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|cacheEntry
operator|.
name|getExpiry
argument_list|()
operator|.
name|before
argument_list|(
name|current
argument_list|)
condition|)
block|{
name|expiredTokens
operator|.
name|add
argument_list|(
name|cacheEntry
operator|.
name|getSecurityToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|expiredTokens
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
name|processTokenExpiry
argument_list|()
expr_stmt|;
name|CacheEntry
name|cacheEntry
init|=
name|tokens
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|cacheEntry
operator|!=
literal|null
condition|)
block|{
return|return
name|cacheEntry
operator|.
name|getSecurityToken
argument_list|()
return|;
block|}
return|return
literal|null
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
name|processTokenExpiry
argument_list|()
expr_stmt|;
synchronized|synchronized
init|(
name|tokens
init|)
block|{
for|for
control|(
name|String
name|id
range|:
name|tokens
operator|.
name|keySet
argument_list|()
control|)
block|{
name|CacheEntry
name|cacheEntry
init|=
name|tokens
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|SecurityToken
name|securityToken
init|=
name|cacheEntry
operator|.
name|getSecurityToken
argument_list|()
decl_stmt|;
if|if
condition|(
name|hashCode
operator|==
name|securityToken
operator|.
name|getAssociatedHash
argument_list|()
condition|)
block|{
return|return
name|securityToken
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|void
name|processTokenExpiry
parameter_list|()
block|{
name|Date
name|current
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|tokens
init|)
block|{
for|for
control|(
name|String
name|id
range|:
name|tokens
operator|.
name|keySet
argument_list|()
control|)
block|{
name|CacheEntry
name|cacheEntry
init|=
name|tokens
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|cacheEntry
operator|.
name|getExpiry
argument_list|()
operator|.
name|before
argument_list|(
name|current
argument_list|)
condition|)
block|{
name|tokens
operator|.
name|remove
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
specifier|static
class|class
name|CacheEntry
block|{
specifier|private
specifier|final
name|SecurityToken
name|securityToken
decl_stmt|;
specifier|private
specifier|final
name|Date
name|expires
decl_stmt|;
specifier|public
name|CacheEntry
parameter_list|(
name|SecurityToken
name|securityToken
parameter_list|,
name|Date
name|expires
parameter_list|)
block|{
name|this
operator|.
name|securityToken
operator|=
name|securityToken
expr_stmt|;
name|this
operator|.
name|expires
operator|=
name|expires
expr_stmt|;
block|}
comment|/**          * Get the SecurityToken          * @return the SecurityToken          */
specifier|public
name|SecurityToken
name|getSecurityToken
parameter_list|()
block|{
return|return
name|securityToken
return|;
block|}
comment|/**          * Get when this CacheEntry is to be removed from the cache          * @return when this CacheEntry is to be removed from the cache          */
specifier|public
name|Date
name|getExpiry
parameter_list|()
block|{
return|return
name|expires
return|;
block|}
block|}
block|}
end_class

end_unit

