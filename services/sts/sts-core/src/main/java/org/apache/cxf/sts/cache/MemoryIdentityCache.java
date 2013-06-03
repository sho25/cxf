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
name|security
operator|.
name|Principal
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|logging
operator|.
name|LogUtils
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
name|sts
operator|.
name|IdentityMapper
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
name|principal
operator|.
name|CustomTokenPrincipal
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jmx
operator|.
name|export
operator|.
name|annotation
operator|.
name|ManagedOperation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jmx
operator|.
name|export
operator|.
name|annotation
operator|.
name|ManagedResource
import|;
end_import

begin_comment
comment|/**  * A simple in-memory HashMap based cache to cache identities in different realms where  * the relationship is of type FederateIdentity.  */
end_comment

begin_class
annotation|@
name|ManagedResource
argument_list|()
specifier|public
class|class
name|MemoryIdentityCache
implements|implements
name|IdentityCache
implements|,
name|IdentityMapper
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|MemoryIdentityCache
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|cache
init|=
name|Collections
operator|.
name|synchronizedMap
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
specifier|private
name|long
name|maxCacheItems
init|=
literal|10000L
decl_stmt|;
specifier|private
name|IdentityMapper
name|identityMapper
decl_stmt|;
specifier|private
name|MemoryIdentityCacheStatistics
name|statistics
decl_stmt|;
specifier|protected
name|MemoryIdentityCache
parameter_list|()
block|{              }
specifier|public
name|MemoryIdentityCache
parameter_list|(
name|IdentityMapper
name|identityMapper
parameter_list|)
block|{
name|this
operator|.
name|identityMapper
operator|=
name|identityMapper
expr_stmt|;
block|}
specifier|public
name|MemoryIdentityCacheStatistics
name|getStatistics
parameter_list|()
block|{
if|if
condition|(
name|statistics
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|statistics
operator|=
operator|new
name|MemoryIdentityCacheStatistics
argument_list|()
expr_stmt|;
block|}
return|return
name|statistics
return|;
block|}
specifier|public
name|void
name|setStatistics
parameter_list|(
name|MemoryIdentityCacheStatistics
name|stats
parameter_list|)
block|{
name|this
operator|.
name|statistics
operator|=
name|stats
expr_stmt|;
block|}
specifier|public
name|long
name|getMaxCacheItems
parameter_list|()
block|{
return|return
name|maxCacheItems
return|;
block|}
specifier|public
name|void
name|setMaxCacheItems
parameter_list|(
name|long
name|maxCacheItems
parameter_list|)
block|{
name|this
operator|.
name|maxCacheItems
operator|=
name|maxCacheItems
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|add
parameter_list|(
name|String
name|user
parameter_list|,
name|String
name|realm
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|identities
parameter_list|)
block|{
if|if
condition|(
name|cache
operator|.
name|size
argument_list|()
operator|>=
name|maxCacheItems
condition|)
block|{
name|cache
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
name|cache
operator|.
name|put
argument_list|(
name|user
operator|+
literal|"@"
operator|+
name|realm
argument_list|,
name|identities
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedOperation
argument_list|()
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|get
parameter_list|(
name|String
name|user
parameter_list|,
name|String
name|realm
parameter_list|)
block|{
return|return
name|cache
operator|.
name|get
argument_list|(
name|user
operator|+
literal|"@"
operator|+
name|realm
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|remove
parameter_list|(
name|String
name|user
parameter_list|,
name|String
name|realm
parameter_list|)
block|{
name|cache
operator|.
name|remove
argument_list|(
name|user
operator|+
literal|"@"
operator|+
name|realm
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedOperation
argument_list|()
annotation|@
name|Override
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|cache
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|ManagedOperation
argument_list|()
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|cache
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|ManagedOperation
argument_list|()
specifier|public
name|String
name|getContent
parameter_list|()
block|{
return|return
name|this
operator|.
name|cache
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Principal
name|mapPrincipal
parameter_list|(
name|String
name|sourceRealm
parameter_list|,
name|Principal
name|sourcePrincipal
parameter_list|,
name|String
name|targetRealm
parameter_list|)
block|{
name|Principal
name|targetPrincipal
init|=
literal|null
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|identities
init|=
name|this
operator|.
name|get
argument_list|(
name|sourcePrincipal
operator|.
name|getName
argument_list|()
argument_list|,
name|sourceRealm
argument_list|)
decl_stmt|;
if|if
condition|(
name|identities
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Identities found for '"
operator|+
name|sourcePrincipal
operator|.
name|getName
argument_list|()
operator|+
literal|"@"
operator|+
name|sourceRealm
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
comment|// Identities object found for key sourceUser@sourceRealm
name|String
name|targetUser
init|=
name|identities
operator|.
name|get
argument_list|(
name|targetRealm
argument_list|)
decl_stmt|;
if|if
condition|(
name|targetUser
operator|==
literal|null
condition|)
block|{
name|getStatistics
argument_list|()
operator|.
name|increaseCacheMiss
argument_list|()
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"No mapping found for realm "
operator|+
name|targetRealm
operator|+
literal|" of user '"
operator|+
name|sourcePrincipal
operator|.
name|getName
argument_list|()
operator|+
literal|"@"
operator|+
name|sourceRealm
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
comment|// User identity of target realm not cached yet
name|targetPrincipal
operator|=
name|this
operator|.
name|identityMapper
operator|.
name|mapPrincipal
argument_list|(
name|sourceRealm
argument_list|,
name|sourcePrincipal
argument_list|,
name|targetRealm
argument_list|)
expr_stmt|;
comment|// Add the identity for target realm to the cached entry
name|identities
operator|.
name|put
argument_list|(
name|targetRealm
argument_list|,
name|targetPrincipal
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// Verify whether target user has cached some identities already
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|cachedItem
init|=
name|this
operator|.
name|get
argument_list|(
name|targetPrincipal
operator|.
name|getName
argument_list|()
argument_list|,
name|targetRealm
argument_list|)
decl_stmt|;
if|if
condition|(
name|cachedItem
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Merging mappings for '"
operator|+
name|sourcePrincipal
operator|.
name|getName
argument_list|()
operator|+
literal|"@"
operator|+
name|sourceRealm
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
comment|//Identites already cached for targetUser@targetRealm key pair
comment|//Merge into identities object
name|this
operator|.
name|mergeMap
argument_list|(
name|identities
argument_list|,
name|cachedItem
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|add
argument_list|(
name|targetPrincipal
operator|.
name|getName
argument_list|()
argument_list|,
name|targetRealm
argument_list|,
name|identities
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getStatistics
argument_list|()
operator|.
name|increaseCacheHit
argument_list|()
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|INFO
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Mapping '"
operator|+
name|sourcePrincipal
operator|.
name|getName
argument_list|()
operator|+
literal|"@"
operator|+
name|sourceRealm
operator|+
literal|"' to '"
operator|+
name|targetUser
operator|+
literal|"@"
operator|+
name|targetRealm
operator|+
literal|"' cached"
argument_list|)
expr_stmt|;
block|}
name|targetPrincipal
operator|=
operator|new
name|CustomTokenPrincipal
argument_list|(
name|targetUser
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"No mapping found for realm "
operator|+
name|targetRealm
operator|+
literal|" of user '"
operator|+
name|sourcePrincipal
operator|.
name|getName
argument_list|()
operator|+
literal|"@"
operator|+
name|sourceRealm
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
name|getStatistics
argument_list|()
operator|.
name|increaseCacheMiss
argument_list|()
expr_stmt|;
comment|// Identities object NOT found for key sourceUser@sourceRealm
name|targetPrincipal
operator|=
name|this
operator|.
name|identityMapper
operator|.
name|mapPrincipal
argument_list|(
name|sourceRealm
argument_list|,
name|sourcePrincipal
argument_list|,
name|targetRealm
argument_list|)
expr_stmt|;
name|identities
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|identities
operator|.
name|put
argument_list|(
name|sourceRealm
argument_list|,
name|sourcePrincipal
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|identities
operator|.
name|put
argument_list|(
name|targetRealm
argument_list|,
name|targetPrincipal
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|add
argument_list|(
name|targetPrincipal
operator|.
name|getName
argument_list|()
argument_list|,
name|targetRealm
argument_list|,
name|identities
argument_list|)
expr_stmt|;
name|this
operator|.
name|add
argument_list|(
name|sourcePrincipal
operator|.
name|getName
argument_list|()
argument_list|,
name|sourceRealm
argument_list|,
name|identities
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Cache content: "
operator|+
name|this
operator|.
name|cache
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|targetPrincipal
return|;
block|}
specifier|private
name|void
name|mergeMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|to
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|from
parameter_list|)
block|{
for|for
control|(
name|String
name|key
range|:
name|from
operator|.
name|keySet
argument_list|()
control|)
block|{
name|to
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|from
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|String
name|key
range|:
name|to
operator|.
name|keySet
argument_list|()
control|)
block|{
name|from
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|to
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

