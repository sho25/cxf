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
name|cache
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
name|io
operator|.
name|IOException
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
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|cache
operator|.
name|ReplayCache
import|;
end_import

begin_comment
comment|/**  * An in-memory EHCache implementation of the ReplayCache interface. The default TTL is 60 minutes and the  * max TTL is 12 hours.  */
end_comment

begin_class
specifier|public
class|class
name|EHCacheReplayCache
implements|implements
name|ReplayCache
implements|,
name|Closeable
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
name|EHCacheReplayCache
parameter_list|(
name|String
name|key
parameter_list|,
name|URL
name|configFileURL
parameter_list|)
block|{
name|cacheManager
operator|=
name|EHCacheManagerHolder
operator|.
name|getCacheManager
argument_list|(
name|configFileURL
argument_list|)
expr_stmt|;
name|Ehcache
name|newCache
init|=
operator|new
name|Cache
argument_list|(
name|key
argument_list|,
literal|50000
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
name|DEFAULT_TTL
argument_list|,
name|DEFAULT_TTL
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
comment|/**      * Add the given identifier to the cache. It will be cached for a default amount of time.      * @param identifier The identifier to be added      */
specifier|public
name|void
name|add
parameter_list|(
name|String
name|identifier
parameter_list|)
block|{
name|add
argument_list|(
name|identifier
argument_list|,
name|ttl
argument_list|)
expr_stmt|;
block|}
comment|/**      * Add the given identifier to the cache to be cached for the given time      * @param identifier The identifier to be added      * @param timeToLive The length of time to cache the Identifier in seconds      */
specifier|public
name|void
name|add
parameter_list|(
name|String
name|identifier
parameter_list|,
name|long
name|timeToLive
parameter_list|)
block|{
if|if
condition|(
name|identifier
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|identifier
argument_list|)
condition|)
block|{
return|return;
block|}
name|int
name|parsedTTL
init|=
operator|(
name|int
operator|)
name|timeToLive
decl_stmt|;
if|if
condition|(
name|timeToLive
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
name|cache
operator|.
name|put
argument_list|(
operator|new
name|Element
argument_list|(
name|identifier
argument_list|,
name|identifier
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
comment|/**      * Return true if the given identifier is contained in the cache      * @param identifier The identifier to check      */
specifier|public
name|boolean
name|contains
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
condition|)
block|{
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
name|cache
operator|.
name|remove
argument_list|(
name|identifier
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
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
block|}
block|}
block|}
end_class

end_unit

