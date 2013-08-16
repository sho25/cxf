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
name|io
operator|.
name|Closeable
import|;
end_import

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
name|Ehcache
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
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|config
operator|.
name|CacheConfiguration
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
name|Bus
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
name|buslifecycle
operator|.
name|BusLifeCycleListener
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
name|buslifecycle
operator|.
name|BusLifeCycleManager
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
name|wss4j
operator|.
name|common
operator|.
name|cache
operator|.
name|EHCacheManagerHolder
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
implements|,
name|Closeable
implements|,
name|BusLifeCycleListener
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
specifier|private
name|Ehcache
name|cache
decl_stmt|;
specifier|private
name|Bus
name|bus
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
name|Bus
name|b
parameter_list|,
name|URL
name|configFileURL
parameter_list|)
block|{
name|bus
operator|=
name|b
expr_stmt|;
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
name|b
operator|.
name|getExtension
argument_list|(
name|BusLifeCycleManager
operator|.
name|class
argument_list|)
operator|.
name|registerLifeCycleListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
name|String
name|confName
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
name|confName
operator|=
name|bus
operator|.
name|getId
argument_list|()
expr_stmt|;
block|}
name|cacheManager
operator|=
name|EHCacheManagerHolder
operator|.
name|getCacheManager
argument_list|(
name|confName
argument_list|,
name|configFileURL
argument_list|)
expr_stmt|;
comment|// Cannot overflow to disk as SecurityToken Elements can't be serialized
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
name|CacheConfiguration
name|cc
init|=
name|EHCacheManagerHolder
operator|.
name|getCacheConfiguration
argument_list|(
name|key
argument_list|,
name|cacheManager
argument_list|)
operator|.
name|overflowToDisk
argument_list|(
literal|false
argument_list|)
decl_stmt|;
comment|//tokens not writable
name|Ehcache
name|newCache
init|=
operator|new
name|Cache
argument_list|(
name|cc
argument_list|)
decl_stmt|;
name|cache
operator|=
name|cacheManager
operator|.
name|addCacheIfAbsent
argument_list|(
name|newCache
argument_list|)
expr_stmt|;
comment|// Set the TimeToLive value from the CacheConfiguration
name|ttl
operator|=
name|cc
operator|.
name|getTimeToLiveSeconds
argument_list|()
expr_stmt|;
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
name|getTTL
argument_list|(
name|token
argument_list|)
decl_stmt|;
if|if
condition|(
name|parsedTTL
operator|>
literal|0
condition|)
block|{
name|Element
name|element
init|=
operator|new
name|Element
argument_list|(
name|token
operator|.
name|getId
argument_list|()
argument_list|,
name|token
argument_list|)
decl_stmt|;
name|element
operator|.
name|setTimeToLive
argument_list|(
name|parsedTTL
argument_list|)
expr_stmt|;
name|element
operator|.
name|setTimeToIdle
argument_list|(
name|parsedTTL
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|add
parameter_list|(
name|String
name|identifier
parameter_list|,
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
name|identifier
argument_list|)
condition|)
block|{
name|int
name|parsedTTL
init|=
name|getTTL
argument_list|(
name|token
argument_list|)
decl_stmt|;
if|if
condition|(
name|parsedTTL
operator|>
literal|0
condition|)
block|{
name|Element
name|element
init|=
operator|new
name|Element
argument_list|(
name|identifier
argument_list|,
name|token
argument_list|)
decl_stmt|;
name|element
operator|.
name|setTimeToLive
argument_list|(
name|parsedTTL
argument_list|)
expr_stmt|;
name|element
operator|.
name|setTimeToIdle
argument_list|(
name|parsedTTL
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|remove
parameter_list|(
name|String
name|identifier
parameter_list|)
block|{
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|identifier
argument_list|)
operator|&&
name|cache
operator|.
name|isKeyInCache
argument_list|(
name|identifier
argument_list|)
condition|)
block|{
name|cache
operator|.
name|remove
argument_list|(
name|identifier
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
name|identifier
parameter_list|)
block|{
name|Element
name|element
init|=
name|cache
operator|.
name|get
argument_list|(
name|identifier
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
specifier|private
name|int
name|getTTL
parameter_list|(
name|SecurityToken
name|token
parameter_list|)
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
if|if
condition|(
name|expiryTime
operator|<
literal|0
condition|)
block|{
return|return
literal|0
return|;
block|}
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
operator|>
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
return|return
name|parsedTTL
return|;
block|}
specifier|public
name|void
name|close
parameter_list|()
block|{
if|if
condition|(
name|cacheManager
operator|!=
literal|null
condition|)
block|{
name|EHCacheManagerHolder
operator|.
name|releaseCacheManger
argument_list|(
name|cacheManager
argument_list|)
expr_stmt|;
name|cacheManager
operator|=
literal|null
expr_stmt|;
name|cache
operator|=
literal|null
expr_stmt|;
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
name|bus
operator|.
name|getExtension
argument_list|(
name|BusLifeCycleManager
operator|.
name|class
argument_list|)
operator|.
name|unregisterLifeCycleListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|initComplete
parameter_list|()
block|{     }
specifier|public
name|void
name|preShutdown
parameter_list|()
block|{
name|close
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|postShutdown
parameter_list|()
block|{
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

