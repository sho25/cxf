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
name|common
operator|.
name|util
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
name|HashSet
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
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|WeakHashMap
import|;
end_import

begin_comment
comment|/**  * Implements a useful caching map. It weakly references the keys,  * but strongly references the data. It works much like the WeakHashMap,  * in that when the keys are garbage collected, the data is removed from  * the map.  *  * The main difference is that keys used for lookups don't have to be "=="  * the same to maintain the data in the cache.  Basically, lookups in this  * map use a ".equals" compare, but the keys are then stored with a "=="  * compare so if the original key is garbage collected, the other keys that  * may reference the data keep the data in the cache.  *  *<b>  * Note that this implementation is not synchronized. Not even a little.  * 'Read-only' operations can trigger internal modifications. If you share this  * class between threads, you must protect every operation.  *</b>  */
end_comment

begin_class
specifier|public
class|class
name|CacheMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
implements|implements
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|mainDataMap
init|=
operator|new
name|WeakHashMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|extraKeyMap
init|=
operator|new
name|WeakIdentityHashMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|CacheMap
parameter_list|()
block|{      }
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|mainDataMap
operator|.
name|clear
argument_list|()
expr_stmt|;
name|extraKeyMap
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|updateMainDataMap
parameter_list|()
block|{
comment|//if the singleton in the mainDataMap has been garbage collected,
comment|//we'll copy another version of it from the extraKeyMap
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
range|:
name|extraKeyMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|mainDataMap
operator|.
name|containsKey
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|mainDataMap
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|boolean
name|containsKey
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
if|if
condition|(
operator|!
name|mainDataMap
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|updateMainDataMap
argument_list|()
expr_stmt|;
return|return
name|mainDataMap
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
return|;
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|containsValue
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
return|return
name|mainDataMap
operator|.
name|containsValue
argument_list|(
name|value
argument_list|)
operator|||
name|extraKeyMap
operator|.
name|containsValue
argument_list|(
name|value
argument_list|)
return|;
block|}
specifier|public
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entrySet
parameter_list|()
block|{
name|updateMainDataMap
argument_list|()
expr_stmt|;
return|return
name|mainDataMap
operator|.
name|entrySet
argument_list|()
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|V
name|get
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|V
name|val
init|=
name|mainDataMap
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|==
literal|null
condition|)
block|{
name|updateMainDataMap
argument_list|()
expr_stmt|;
name|val
operator|=
name|mainDataMap
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|val
operator|!=
literal|null
condition|)
block|{
name|extraKeyMap
operator|.
name|put
argument_list|(
operator|(
name|K
operator|)
name|key
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
return|return
name|val
return|;
block|}
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|mainDataMap
operator|.
name|isEmpty
argument_list|()
operator|&&
name|extraKeyMap
operator|.
name|isEmpty
argument_list|()
return|;
block|}
specifier|public
name|Set
argument_list|<
name|K
argument_list|>
name|keySet
parameter_list|()
block|{
name|updateMainDataMap
argument_list|()
expr_stmt|;
return|return
name|mainDataMap
operator|.
name|keySet
argument_list|()
return|;
block|}
specifier|public
name|V
name|put
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|)
block|{
name|V
name|v
init|=
name|mainDataMap
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|V
name|v2
init|=
name|extraKeyMap
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
decl_stmt|;
return|return
name|v
operator|==
literal|null
condition|?
name|v2
else|:
name|v
return|;
block|}
specifier|public
name|void
name|putAll
parameter_list|(
name|Map
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|t
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|ent
range|:
name|t
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|put
argument_list|(
name|ent
operator|.
name|getKey
argument_list|()
argument_list|,
name|ent
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|V
name|remove
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|V
name|v
init|=
name|mainDataMap
operator|.
name|remove
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|K
argument_list|>
name|keys
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|extraKeyMap
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
name|V
name|v2
init|=
name|extraKeyMap
operator|.
name|remove
argument_list|(
name|key
argument_list|)
decl_stmt|;
for|for
control|(
name|K
name|nk
range|:
name|keys
control|)
block|{
if|if
condition|(
operator|(
name|key
operator|!=
literal|null
operator|&&
name|key
operator|.
name|equals
argument_list|(
name|nk
argument_list|)
operator|)
operator|||
operator|(
name|key
operator|==
literal|null
operator|&&
name|nk
operator|==
literal|null
operator|)
condition|)
block|{
name|V
name|v3
init|=
name|extraKeyMap
operator|.
name|remove
argument_list|(
name|nk
argument_list|)
decl_stmt|;
if|if
condition|(
name|v2
operator|==
literal|null
condition|)
block|{
name|v2
operator|=
name|v3
expr_stmt|;
block|}
block|}
block|}
return|return
name|v
operator|==
literal|null
condition|?
name|v2
else|:
name|v
return|;
block|}
specifier|public
name|int
name|size
parameter_list|()
block|{
name|updateMainDataMap
argument_list|()
expr_stmt|;
return|return
name|mainDataMap
operator|.
name|size
argument_list|()
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|V
argument_list|>
name|values
parameter_list|()
block|{
name|updateMainDataMap
argument_list|()
expr_stmt|;
return|return
name|mainDataMap
operator|.
name|values
argument_list|()
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|updateMainDataMap
argument_list|()
expr_stmt|;
return|return
name|mainDataMap
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

