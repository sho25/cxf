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
name|time
operator|.
name|Duration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Instant
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|ZoneOffset
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|ZonedDateTime
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
name|HazelcastInstance
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
name|TokenStore
import|;
end_import

begin_class
specifier|public
class|class
name|HazelCastTokenStore
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
specifier|private
name|IMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cacheMap
decl_stmt|;
specifier|private
name|long
name|ttl
init|=
name|DEFAULT_TTL
decl_stmt|;
specifier|private
name|HazelcastInstance
name|hazelcastInstance
decl_stmt|;
specifier|private
name|String
name|mapName
decl_stmt|;
specifier|public
name|HazelCastTokenStore
parameter_list|(
name|String
name|mapName
parameter_list|)
block|{
name|this
operator|.
name|mapName
operator|=
name|mapName
expr_stmt|;
block|}
comment|/**      * Get the Hazelcast instance      * If null, return Default instance      * @param hzInstance Hazelcast instance      */
specifier|public
name|HazelcastInstance
name|getHazelcastInstance
parameter_list|()
block|{
if|if
condition|(
name|hazelcastInstance
operator|==
literal|null
condition|)
block|{
name|hazelcastInstance
operator|=
name|Hazelcast
operator|.
name|newHazelcastInstance
argument_list|()
expr_stmt|;
block|}
return|return
name|hazelcastInstance
return|;
block|}
comment|/**      * Set the Hazelcast instance, otherwise default instance used      * If you configure Hazelcast instance in spring, you must inject the instance here.      * @param hzInstance Hazelcast instance      */
specifier|public
name|void
name|setHazelcastInstance
parameter_list|(
name|HazelcastInstance
name|hazelcastInstance
parameter_list|)
block|{
name|this
operator|.
name|hazelcastInstance
operator|=
name|hazelcastInstance
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
name|getCacheMap
argument_list|()
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
name|parsedTTL
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
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
name|getCacheMap
argument_list|()
operator|.
name|put
argument_list|(
name|identifier
argument_list|,
name|token
argument_list|,
name|parsedTTL
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
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
name|getCacheMap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|identifier
argument_list|)
condition|)
block|{
name|getCacheMap
argument_list|()
operator|.
name|remove
argument_list|(
name|identifier
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
name|getCacheMap
argument_list|()
operator|.
name|keySet
argument_list|()
argument_list|)
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
return|return
operator|(
name|SecurityToken
operator|)
name|getCacheMap
argument_list|()
operator|.
name|get
argument_list|(
name|identifier
argument_list|)
return|;
block|}
specifier|public
name|void
name|destroy
parameter_list|()
block|{
if|if
condition|(
name|hazelcastInstance
operator|!=
literal|null
condition|)
block|{
name|hazelcastInstance
operator|.
name|getLifecycleService
argument_list|()
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
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
name|Instant
name|expires
init|=
name|token
operator|.
name|getExpires
argument_list|()
decl_stmt|;
name|ZonedDateTime
name|now
init|=
name|ZonedDateTime
operator|.
name|now
argument_list|(
name|ZoneOffset
operator|.
name|UTC
argument_list|)
decl_stmt|;
if|if
condition|(
name|expires
operator|.
name|isBefore
argument_list|(
name|now
operator|.
name|toInstant
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|0
return|;
block|}
name|Duration
name|duration
init|=
name|Duration
operator|.
name|between
argument_list|(
name|now
operator|.
name|toInstant
argument_list|()
argument_list|,
name|expires
argument_list|)
decl_stmt|;
name|parsedTTL
operator|=
operator|(
name|int
operator|)
name|duration
operator|.
name|getSeconds
argument_list|()
expr_stmt|;
if|if
condition|(
name|duration
operator|.
name|getSeconds
argument_list|()
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
specifier|private
name|IMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|getCacheMap
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|cacheMap
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|cacheMap
operator|=
name|getHazelcastInstance
argument_list|()
operator|.
name|getMap
argument_list|(
name|mapName
argument_list|)
expr_stmt|;
block|}
return|return
name|this
operator|.
name|cacheMap
return|;
block|}
block|}
end_class

end_unit

