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
name|client
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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|javax
operator|.
name|annotation
operator|.
name|Priority
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|Cache
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
name|HttpMethod
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
name|Priorities
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
name|client
operator|.
name|ClientRequestContext
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
name|client
operator|.
name|ClientRequestFilter
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
name|HttpHeaders
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
name|Response
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

begin_class
annotation|@
name|Priority
argument_list|(
name|Priorities
operator|.
name|USER
operator|-
literal|1
argument_list|)
specifier|public
class|class
name|CacheControlClientRequestFilter
implements|implements
name|ClientRequestFilter
block|{
specifier|static
specifier|final
name|String
name|NO_CACHE_PROPERTY
init|=
literal|"no_client_cache"
decl_stmt|;
specifier|static
specifier|final
name|String
name|CACHED_ENTITY_PROPERTY
init|=
literal|"client_cached_entity"
decl_stmt|;
specifier|static
specifier|final
name|String
name|CLIENT_ACCEPTS
init|=
literal|"client_accepts"
decl_stmt|;
specifier|static
specifier|final
name|String
name|CLIENT_CACHE_CONTROL
init|=
literal|"client_cache_control"
decl_stmt|;
specifier|private
name|Cache
argument_list|<
name|Key
argument_list|,
name|Entry
argument_list|>
name|cache
decl_stmt|;
specifier|public
name|CacheControlClientRequestFilter
parameter_list|(
specifier|final
name|Cache
argument_list|<
name|Key
argument_list|,
name|Entry
argument_list|>
name|cache
parameter_list|)
block|{
name|this
operator|.
name|cache
operator|=
name|cache
expr_stmt|;
block|}
specifier|public
name|CacheControlClientRequestFilter
parameter_list|()
block|{
comment|// no-op: use setCache then
block|}
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
specifier|final
name|ClientRequestContext
name|request
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|HttpMethod
operator|.
name|GET
operator|.
name|equals
argument_list|(
name|request
operator|.
name|getMethod
argument_list|()
argument_list|)
condition|)
block|{
comment|//TODO: Review the possibility of supporting POST responses, example,
comment|//      POST create request may get a created entity representation returned
name|request
operator|.
name|setProperty
argument_list|(
name|NO_CACHE_PROPERTY
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
return|return;
block|}
specifier|final
name|URI
name|uri
init|=
name|request
operator|.
name|getUri
argument_list|()
decl_stmt|;
specifier|final
name|String
name|accepts
init|=
name|request
operator|.
name|getHeaderString
argument_list|(
name|HttpHeaders
operator|.
name|ACCEPT
argument_list|)
decl_stmt|;
specifier|final
name|Key
name|key
init|=
operator|new
name|Key
argument_list|(
name|uri
argument_list|,
name|accepts
argument_list|)
decl_stmt|;
name|Entry
name|entry
init|=
name|cache
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|entry
operator|!=
literal|null
condition|)
block|{
comment|//TODO: do the extra validation against the conditional headers
comment|//      which may be contained in the current request
if|if
condition|(
name|entry
operator|.
name|isOutDated
argument_list|()
condition|)
block|{
name|String
name|ifNoneMatchHeader
init|=
name|entry
operator|.
name|getCacheHeaders
argument_list|()
operator|.
name|get
argument_list|(
name|HttpHeaders
operator|.
name|IF_NONE_MATCH
argument_list|)
decl_stmt|;
name|String
name|ifModifiedSinceHeader
init|=
name|entry
operator|.
name|getCacheHeaders
argument_list|()
operator|.
name|get
argument_list|(
name|HttpHeaders
operator|.
name|IF_MODIFIED_SINCE
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|ifNoneMatchHeader
argument_list|)
operator|&&
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|ifModifiedSinceHeader
argument_list|)
condition|)
block|{
name|cache
operator|.
name|remove
argument_list|(
name|key
argument_list|,
name|entry
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|request
operator|.
name|getHeaders
argument_list|()
operator|.
name|add
argument_list|(
name|HttpHeaders
operator|.
name|IF_NONE_MATCH
argument_list|,
name|ifNoneMatchHeader
argument_list|)
expr_stmt|;
name|request
operator|.
name|setProperty
argument_list|(
name|CACHED_ENTITY_PROPERTY
argument_list|,
name|entry
operator|.
name|getData
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|Object
name|cachedEntity
init|=
name|entry
operator|.
name|getData
argument_list|()
decl_stmt|;
name|Response
operator|.
name|ResponseBuilder
name|ok
init|=
name|Response
operator|.
name|ok
argument_list|(
name|cachedEntity
argument_list|)
decl_stmt|;
if|if
condition|(
name|entry
operator|.
name|getHeaders
argument_list|()
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
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|h
range|:
name|entry
operator|.
name|getHeaders
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
for|for
control|(
specifier|final
name|Object
name|instance
range|:
name|h
operator|.
name|getValue
argument_list|()
control|)
block|{
name|ok
operator|=
name|ok
operator|.
name|header
argument_list|(
name|h
operator|.
name|getKey
argument_list|()
argument_list|,
name|instance
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|request
operator|.
name|setProperty
argument_list|(
name|CACHED_ENTITY_PROPERTY
argument_list|,
name|cachedEntity
argument_list|)
expr_stmt|;
name|request
operator|.
name|abortWith
argument_list|(
name|ok
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Should the map of all request headers shared ?
name|request
operator|.
name|setProperty
argument_list|(
name|CLIENT_ACCEPTS
argument_list|,
name|accepts
argument_list|)
expr_stmt|;
name|request
operator|.
name|setProperty
argument_list|(
name|CLIENT_CACHE_CONTROL
argument_list|,
name|request
operator|.
name|getHeaderString
argument_list|(
name|HttpHeaders
operator|.
name|CACHE_CONTROL
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|CacheControlClientRequestFilter
name|setCache
parameter_list|(
specifier|final
name|Cache
argument_list|<
name|Key
argument_list|,
name|Entry
argument_list|>
name|c
parameter_list|)
block|{
name|this
operator|.
name|cache
operator|=
name|c
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

