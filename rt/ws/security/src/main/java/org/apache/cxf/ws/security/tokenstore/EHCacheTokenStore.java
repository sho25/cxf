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
name|net
operator|.
name|URL
import|;
end_import

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
name|Iterator
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
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|Cache
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|CacheManager
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|Element
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
comment|/**  * An in-memory EHCache implementation of the TokenStore interface. The default TTL is 60 minutes  * and the max TTL is 12 hours.  */
end_comment

begin_class
specifier|public
class|class
name|EHCacheTokenStore
implements|implements
name|TokenStore
block|{
specifier|public
specifier|static
specifier|final
name|long
name|DEFAULT_TTL
init|=
literal|3600L
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
specifier|public
specifier|static
specifier|final
name|int
name|MAX_ELEMENTS
init|=
literal|1000000
decl_stmt|;
specifier|private
name|Cache
name|cache
decl_stmt|;
specifier|private
name|CacheManager
name|cacheManager
decl_stmt|;
specifier|private
name|long
name|ttl
init|=
name|DEFAULT_TTL
decl_stmt|;
specifier|public
name|EHCacheTokenStore
parameter_list|(
name|String
name|key
parameter_list|,
name|URL
name|configFileURL
parameter_list|)
block|{
if|if
condition|(
name|cacheManager
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|configFileURL
operator|==
literal|null
condition|)
block|{
name|cacheManager
operator|=
name|CacheManager
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|cacheManager
operator|=
name|CacheManager
operator|.
name|create
argument_list|(
name|configFileURL
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|cacheManager
operator|.
name|cacheExists
argument_list|(
name|key
argument_list|)
condition|)
block|{
comment|// Cannot overflow to disk as SecurityToken Elements can't be serialized
name|cache
operator|=
operator|new
name|Cache
argument_list|(
name|key
argument_list|,
name|MAX_ELEMENTS
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
name|DEFAULT_TTL
argument_list|,
name|DEFAULT_TTL
argument_list|)
expr_stmt|;
name|cacheManager
operator|.
name|addCache
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cache
operator|=
name|cacheManager
operator|.
name|getCache
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Set a new (default) TTL value in seconds      * @param newTtl a new (default) TTL value in seconds      */
specifier|public
name|void
name|setTTL
parameter_list|(
name|long
name|newTtl
parameter_list|)
block|{
name|ttl
operator|=
name|newTtl
expr_stmt|;
block|}
comment|/**      * Get the (default) TTL value in seconds      * @return the (default) TTL value in seconds      */
specifier|public
name|long
name|getTTL
parameter_list|()
block|{
return|return
name|ttl
return|;
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
name|int
name|parsedTTL
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|token
operator|.
name|getExpires
argument_list|()
operator|!=
literal|null
condition|)
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
operator|(
name|expires
operator|.
name|getTime
argument_list|()
operator|-
name|current
operator|.
name|getTime
argument_list|()
operator|)
operator|/
literal|1000L
decl_stmt|;
name|parsedTTL
operator|=
operator|(
name|int
operator|)
name|expiryTime
expr_stmt|;
if|if
condition|(
name|expiryTime
operator|!=
operator|(
name|long
operator|)
name|parsedTTL
operator|||
name|parsedTTL
argument_list|<
literal|0
operator|||
name|parsedTTL
argument_list|>
name|MAX_TTL
condition|)
block|{
comment|// Default to configured value
name|parsedTTL
operator|=
operator|(
name|int
operator|)
name|ttl
expr_stmt|;
if|if
condition|(
name|ttl
operator|!=
operator|(
name|long
operator|)
name|parsedTTL
condition|)
block|{
comment|// Fall back to 60 minutes if the default TTL is set incorrectly
name|parsedTTL
operator|=
literal|3600
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
comment|// Default to configured value
name|parsedTTL
operator|=
operator|(
name|int
operator|)
name|ttl
expr_stmt|;
if|if
condition|(
name|ttl
operator|!=
operator|(
name|long
operator|)
name|parsedTTL
condition|)
block|{
comment|// Fall back to 60 minutes if the default TTL is set incorrectly
name|parsedTTL
operator|=
literal|3600
expr_stmt|;
block|}
block|}
name|cache
operator|.
name|put
argument_list|(
operator|new
name|Element
argument_list|(
name|token
operator|.
name|getId
argument_list|()
argument_list|,
name|token
argument_list|,
literal|false
argument_list|,
name|parsedTTL
argument_list|,
name|parsedTTL
argument_list|)
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
name|cache
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getTokenIdentifiers
parameter_list|()
block|{
return|return
name|cache
operator|.
name|getKeysWithExpiryCheck
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Iterator
argument_list|<
name|String
argument_list|>
name|ids
init|=
name|cache
operator|.
name|getKeys
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|ids
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Element
name|element
init|=
name|cache
operator|.
name|get
argument_list|(
name|ids
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|cache
operator|.
name|isExpired
argument_list|(
name|element
argument_list|)
condition|)
block|{
name|expiredTokens
operator|.
name|add
argument_list|(
operator|(
name|SecurityToken
operator|)
name|element
operator|.
name|getObjectValue
argument_list|()
argument_list|)
expr_stmt|;
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
name|Element
name|element
init|=
name|cache
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|element
operator|!=
literal|null
operator|&&
operator|!
name|cache
operator|.
name|isExpired
argument_list|(
name|element
argument_list|)
condition|)
block|{
return|return
operator|(
name|SecurityToken
operator|)
name|element
operator|.
name|getObjectValue
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Iterator
argument_list|<
name|String
argument_list|>
name|ids
init|=
name|cache
operator|.
name|getKeysWithExpiryCheck
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|ids
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Element
name|element
init|=
name|cache
operator|.
name|get
argument_list|(
name|ids
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
name|SecurityToken
name|securityToken
init|=
operator|(
name|SecurityToken
operator|)
name|element
operator|.
name|getObjectValue
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
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

