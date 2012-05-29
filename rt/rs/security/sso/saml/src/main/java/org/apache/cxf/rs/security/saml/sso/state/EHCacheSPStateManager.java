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
name|rs
operator|.
name|security
operator|.
name|saml
operator|.
name|sso
operator|.
name|state
package|;
end_package

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
name|util
operator|.
name|Loader
import|;
end_import

begin_comment
comment|/**  * An in-memory EHCache implementation of the SPStateManager interface.   * The default TTL is 5 minutes.  */
end_comment

begin_class
specifier|public
class|class
name|EHCacheSPStateManager
implements|implements
name|SPStateManager
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
specifier|private
specifier|static
specifier|final
name|String
name|REQUEST_CACHE_KEY
init|=
literal|"cxf-samlp-request-state-cache"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RESPONSE_CACHE_KEY
init|=
literal|"cxf-samlp-response-state-cache"
decl_stmt|;
specifier|private
name|Ehcache
name|requestCache
decl_stmt|;
specifier|private
name|Ehcache
name|responseCache
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
name|EHCacheSPStateManager
parameter_list|()
block|{
name|String
name|defaultConfigFile
init|=
literal|"cxf-samlp-ehcache.xml"
decl_stmt|;
name|URL
name|configFileURL
init|=
name|Loader
operator|.
name|getResource
argument_list|(
name|defaultConfigFile
argument_list|)
decl_stmt|;
name|createCaches
argument_list|(
name|configFileURL
argument_list|)
expr_stmt|;
block|}
specifier|public
name|EHCacheSPStateManager
parameter_list|(
name|URL
name|configFileURL
parameter_list|)
block|{
name|createCaches
argument_list|(
name|configFileURL
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createCaches
parameter_list|(
name|URL
name|configFileURL
parameter_list|)
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
name|Ehcache
name|newCache
init|=
operator|new
name|Cache
argument_list|(
name|REQUEST_CACHE_KEY
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
name|requestCache
operator|=
name|cacheManager
operator|.
name|addCacheIfAbsent
argument_list|(
name|newCache
argument_list|)
expr_stmt|;
name|newCache
operator|=
operator|new
name|Cache
argument_list|(
name|RESPONSE_CACHE_KEY
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
expr_stmt|;
name|responseCache
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
specifier|public
name|ResponseState
name|getResponseState
parameter_list|(
name|String
name|securityContextKey
parameter_list|)
block|{
name|Element
name|element
init|=
name|responseCache
operator|.
name|get
argument_list|(
name|securityContextKey
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
name|responseCache
operator|.
name|isExpired
argument_list|(
name|element
argument_list|)
condition|)
block|{
name|responseCache
operator|.
name|remove
argument_list|(
name|securityContextKey
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
return|return
operator|(
name|ResponseState
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
name|ResponseState
name|removeResponseState
parameter_list|(
name|String
name|securityContextKey
parameter_list|)
block|{
name|Element
name|element
init|=
name|responseCache
operator|.
name|get
argument_list|(
name|securityContextKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|element
operator|!=
literal|null
condition|)
block|{
name|responseCache
operator|.
name|remove
argument_list|(
name|securityContextKey
argument_list|)
expr_stmt|;
return|return
operator|(
name|ResponseState
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
name|void
name|setResponseState
parameter_list|(
name|String
name|securityContextKey
parameter_list|,
name|ResponseState
name|state
parameter_list|)
block|{
if|if
condition|(
name|securityContextKey
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|securityContextKey
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
name|ttl
decl_stmt|;
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
comment|// Fall back to 5 minutes if the default TTL is set incorrectly
name|parsedTTL
operator|=
literal|60
operator|*
literal|5
expr_stmt|;
block|}
name|responseCache
operator|.
name|put
argument_list|(
operator|new
name|Element
argument_list|(
name|securityContextKey
argument_list|,
name|state
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
specifier|public
name|void
name|setRequestState
parameter_list|(
name|String
name|relayState
parameter_list|,
name|RequestState
name|state
parameter_list|)
block|{
if|if
condition|(
name|relayState
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|relayState
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
name|ttl
decl_stmt|;
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
name|requestCache
operator|.
name|put
argument_list|(
operator|new
name|Element
argument_list|(
name|relayState
argument_list|,
name|state
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
specifier|public
name|RequestState
name|removeRequestState
parameter_list|(
name|String
name|relayState
parameter_list|)
block|{
name|Element
name|element
init|=
name|requestCache
operator|.
name|get
argument_list|(
name|relayState
argument_list|)
decl_stmt|;
if|if
condition|(
name|element
operator|!=
literal|null
condition|)
block|{
name|requestCache
operator|.
name|remove
argument_list|(
name|relayState
argument_list|)
expr_stmt|;
return|return
operator|(
name|RequestState
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
name|cacheManager
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|cacheManager
operator|=
literal|null
expr_stmt|;
name|requestCache
operator|=
literal|null
expr_stmt|;
name|responseCache
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

