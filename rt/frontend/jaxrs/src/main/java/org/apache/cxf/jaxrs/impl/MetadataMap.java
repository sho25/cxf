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
name|jaxrs
operator|.
name|impl
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
name|Arrays
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MultivaluedMap
import|;
end_import

begin_class
specifier|public
class|class
name|MetadataMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
implements|implements
name|MultivaluedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
specifier|private
name|boolean
name|caseInsensitive
decl_stmt|;
specifier|private
name|boolean
name|readOnly
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|K
argument_list|,
name|List
argument_list|<
name|V
argument_list|>
argument_list|>
name|m
decl_stmt|;
specifier|public
name|MetadataMap
parameter_list|()
block|{
name|this
operator|.
name|m
operator|=
operator|new
name|LinkedHashMap
argument_list|<
name|K
argument_list|,
name|List
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
block|}
specifier|public
name|MetadataMap
parameter_list|(
name|int
name|size
parameter_list|)
block|{
name|this
operator|.
name|m
operator|=
operator|new
name|LinkedHashMap
argument_list|<
name|K
argument_list|,
name|List
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|(
name|size
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MetadataMap
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|List
argument_list|<
name|V
argument_list|>
argument_list|>
name|store
parameter_list|)
block|{
name|this
argument_list|(
name|store
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MetadataMap
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|List
argument_list|<
name|V
argument_list|>
argument_list|>
name|store
parameter_list|,
name|boolean
name|copy
parameter_list|)
block|{
name|this
argument_list|(
name|store
argument_list|,
name|copy
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MetadataMap
parameter_list|(
name|boolean
name|readOnly
parameter_list|,
name|boolean
name|caseInsensitive
parameter_list|)
block|{
name|this
argument_list|(
literal|null
argument_list|,
name|readOnly
argument_list|,
name|caseInsensitive
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MetadataMap
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|List
argument_list|<
name|V
argument_list|>
argument_list|>
name|store
parameter_list|,
name|boolean
name|readOnly
parameter_list|,
name|boolean
name|caseInsensitive
parameter_list|)
block|{
name|this
argument_list|(
name|store
argument_list|,
literal|true
argument_list|,
name|readOnly
argument_list|,
name|caseInsensitive
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MetadataMap
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|List
argument_list|<
name|V
argument_list|>
argument_list|>
name|store
parameter_list|,
name|boolean
name|copyStore
parameter_list|,
name|boolean
name|readOnly
parameter_list|,
name|boolean
name|caseInsensitive
parameter_list|)
block|{
if|if
condition|(
name|copyStore
condition|)
block|{
name|this
operator|.
name|m
operator|=
operator|new
name|LinkedHashMap
argument_list|<
name|K
argument_list|,
name|List
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
if|if
condition|(
name|store
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|List
argument_list|<
name|V
argument_list|>
argument_list|>
name|entry
range|:
name|store
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|List
argument_list|<
name|V
argument_list|>
name|values
init|=
operator|new
name|ArrayList
argument_list|<
name|V
argument_list|>
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|m
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|values
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|this
operator|.
name|m
operator|=
name|store
expr_stmt|;
block|}
if|if
condition|(
name|readOnly
condition|)
block|{
name|this
operator|.
name|m
operator|=
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|caseInsensitive
operator|=
name|caseInsensitive
expr_stmt|;
name|this
operator|.
name|readOnly
operator|=
name|readOnly
expr_stmt|;
block|}
specifier|public
name|void
name|add
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|)
block|{
name|addValue
argument_list|(
name|key
argument_list|,
name|value
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|addValue
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|,
name|boolean
name|last
parameter_list|)
block|{
name|List
argument_list|<
name|V
argument_list|>
name|data
init|=
name|getList
argument_list|(
name|key
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|last
condition|)
block|{
name|data
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|data
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|ex
parameter_list|)
block|{
comment|// this may happen if an unmodifiable List was set via put or putAll
if|if
condition|(
operator|!
name|readOnly
condition|)
block|{
name|List
argument_list|<
name|V
argument_list|>
name|newList
init|=
operator|new
name|ArrayList
argument_list|<
name|V
argument_list|>
argument_list|(
name|data
argument_list|)
decl_stmt|;
name|put
argument_list|(
name|key
argument_list|,
name|newList
argument_list|)
expr_stmt|;
name|addValue
argument_list|(
name|key
argument_list|,
name|value
argument_list|,
name|last
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
name|ex
throw|;
block|}
block|}
block|}
specifier|private
name|List
argument_list|<
name|V
argument_list|>
name|getList
parameter_list|(
name|K
name|key
parameter_list|)
block|{
name|List
argument_list|<
name|V
argument_list|>
name|data
init|=
name|this
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|data
operator|==
literal|null
condition|)
block|{
name|data
operator|=
operator|new
name|ArrayList
argument_list|<
name|V
argument_list|>
argument_list|()
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
return|return
name|readOnly
condition|?
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|data
argument_list|)
else|:
name|data
return|;
block|}
specifier|public
name|V
name|getFirst
parameter_list|(
name|K
name|key
parameter_list|)
block|{
name|List
argument_list|<
name|V
argument_list|>
name|data
init|=
name|this
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
name|data
operator|==
literal|null
operator|||
name|data
operator|.
name|isEmpty
argument_list|()
condition|?
literal|null
else|:
name|data
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
specifier|public
name|void
name|putSingle
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|)
block|{
name|List
argument_list|<
name|V
argument_list|>
name|data
init|=
operator|new
name|ArrayList
argument_list|<
name|V
argument_list|>
argument_list|()
decl_stmt|;
name|data
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|this
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|m
operator|.
name|clear
argument_list|()
expr_stmt|;
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
name|caseInsensitive
condition|)
block|{
return|return
name|m
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
return|;
block|}
return|return
name|getMatchingKey
argument_list|(
name|key
argument_list|)
operator|!=
literal|null
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
name|m
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
name|Entry
argument_list|<
name|K
argument_list|,
name|List
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|>
name|entrySet
parameter_list|()
block|{
return|return
name|m
operator|.
name|entrySet
argument_list|()
return|;
block|}
specifier|public
name|List
argument_list|<
name|V
argument_list|>
name|get
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
if|if
condition|(
operator|!
name|caseInsensitive
operator|||
name|key
operator|==
literal|null
condition|)
block|{
return|return
name|m
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
name|K
name|realKey
init|=
name|getMatchingKey
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
name|realKey
operator|==
literal|null
condition|?
literal|null
else|:
name|m
operator|.
name|get
argument_list|(
name|realKey
argument_list|)
return|;
block|}
specifier|private
name|K
name|getMatchingKey
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
for|for
control|(
name|K
name|entry
range|:
name|m
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|!=
literal|null
operator|&&
name|entry
operator|.
name|toString
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|key
operator|.
name|toString
argument_list|()
argument_list|)
operator|||
name|entry
operator|==
literal|null
operator|&&
name|key
operator|==
literal|null
condition|)
block|{
return|return
name|entry
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|m
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
if|if
condition|(
operator|!
name|caseInsensitive
condition|)
block|{
return|return
name|m
operator|.
name|keySet
argument_list|()
return|;
block|}
else|else
block|{
name|Set
argument_list|<
name|K
argument_list|>
name|set
init|=
operator|new
name|TreeSet
argument_list|<
name|K
argument_list|>
argument_list|(
operator|new
name|KeyComparator
argument_list|<
name|K
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
name|set
operator|.
name|addAll
argument_list|(
name|m
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|set
return|;
block|}
block|}
specifier|public
name|List
argument_list|<
name|V
argument_list|>
name|put
parameter_list|(
name|K
name|key
parameter_list|,
name|List
argument_list|<
name|V
argument_list|>
name|value
parameter_list|)
block|{
name|K
name|realKey
init|=
operator|!
name|caseInsensitive
operator|||
name|key
operator|==
literal|null
condition|?
name|key
else|:
name|getMatchingKey
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
name|m
operator|.
name|put
argument_list|(
name|realKey
operator|==
literal|null
condition|?
name|key
else|:
name|realKey
argument_list|,
name|value
argument_list|)
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
name|List
argument_list|<
name|V
argument_list|>
argument_list|>
name|map
parameter_list|)
block|{
if|if
condition|(
operator|!
name|caseInsensitive
condition|)
block|{
name|m
operator|.
name|putAll
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
else|else
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
name|List
argument_list|<
name|V
argument_list|>
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|this
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
name|List
argument_list|<
name|V
argument_list|>
name|remove
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
if|if
condition|(
name|caseInsensitive
condition|)
block|{
name|K
name|realKey
init|=
name|getMatchingKey
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
name|m
operator|.
name|remove
argument_list|(
name|realKey
operator|==
literal|null
condition|?
name|key
else|:
name|realKey
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|m
operator|.
name|remove
argument_list|(
name|key
argument_list|)
return|;
block|}
block|}
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|m
operator|.
name|size
argument_list|()
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|List
argument_list|<
name|V
argument_list|>
argument_list|>
name|values
parameter_list|()
block|{
return|return
name|m
operator|.
name|values
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|m
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|m
operator|.
name|equals
argument_list|(
name|o
argument_list|)
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|m
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
specifier|static
class|class
name|KeyComparator
parameter_list|<
name|K
parameter_list|>
implements|implements
name|Comparator
argument_list|<
name|K
argument_list|>
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|K
name|k1
parameter_list|,
name|K
name|k2
parameter_list|)
block|{
name|String
name|s1
init|=
name|k1
operator|.
name|toString
argument_list|()
decl_stmt|;
name|String
name|s2
init|=
name|k2
operator|.
name|toString
argument_list|()
decl_stmt|;
return|return
name|s1
operator|.
name|compareToIgnoreCase
argument_list|(
name|s2
argument_list|)
return|;
block|}
block|}
annotation|@
name|SafeVarargs
specifier|public
specifier|final
name|void
name|addAll
parameter_list|(
name|K
name|key
parameter_list|,
name|V
modifier|...
name|newValues
parameter_list|)
block|{
name|this
operator|.
name|addAllValues
argument_list|(
name|key
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|newValues
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addAll
parameter_list|(
name|K
name|key
parameter_list|,
name|List
argument_list|<
name|V
argument_list|>
name|newValues
parameter_list|)
block|{
name|this
operator|.
name|addAllValues
argument_list|(
name|key
argument_list|,
name|newValues
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|addAllValues
parameter_list|(
name|K
name|key
parameter_list|,
name|List
argument_list|<
name|V
argument_list|>
name|newValues
parameter_list|)
block|{
if|if
condition|(
name|newValues
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"List is empty"
argument_list|)
throw|;
block|}
if|if
condition|(
name|newValues
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|getList
argument_list|(
name|key
argument_list|)
operator|.
name|addAll
argument_list|(
name|newValues
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addFirst
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|)
block|{
name|addValue
argument_list|(
name|key
argument_list|,
name|value
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|equalsIgnoreValueOrder
parameter_list|(
name|MultivaluedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
parameter_list|)
block|{
name|Set
argument_list|<
name|K
argument_list|>
name|mapKeys
init|=
name|map
operator|.
name|keySet
argument_list|()
decl_stmt|;
if|if
condition|(
name|mapKeys
operator|.
name|size
argument_list|()
operator|!=
name|m
operator|.
name|keySet
argument_list|()
operator|.
name|size
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|K
name|key
range|:
name|mapKeys
control|)
block|{
name|List
argument_list|<
name|V
argument_list|>
name|localValues
init|=
name|this
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|V
argument_list|>
name|mapValues
init|=
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|localValues
operator|==
literal|null
operator|||
name|localValues
operator|.
name|size
argument_list|()
operator|!=
name|mapValues
operator|.
name|size
argument_list|()
operator|||
operator|!
name|localValues
operator|.
name|containsAll
argument_list|(
name|mapValues
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

