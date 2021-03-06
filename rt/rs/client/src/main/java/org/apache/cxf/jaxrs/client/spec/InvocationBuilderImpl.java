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
name|spec
package|;
end_package

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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
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
name|ExecutorService
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
name|Future
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
name|client
operator|.
name|AsyncInvoker
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
name|CompletionStageRxInvoker
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
name|Entity
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
name|Invocation
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
name|Invocation
operator|.
name|Builder
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
name|InvocationCallback
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
name|RxInvoker
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
name|SyncInvoker
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
name|CacheControl
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
name|Configuration
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
name|Cookie
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
name|GenericType
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
name|MediaType
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|RuntimeDelegate
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
name|ext
operator|.
name|RuntimeDelegate
operator|.
name|HeaderDelegate
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
name|jaxrs
operator|.
name|client
operator|.
name|AbstractClient
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
name|jaxrs
operator|.
name|client
operator|.
name|WebClient
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
name|jaxrs
operator|.
name|utils
operator|.
name|HttpUtils
import|;
end_import

begin_class
specifier|public
class|class
name|InvocationBuilderImpl
implements|implements
name|Invocation
operator|.
name|Builder
block|{
specifier|private
specifier|static
specifier|final
name|String
name|PROPERTY_KEY
init|=
literal|"jaxrs.filter.properties"
decl_stmt|;
specifier|private
name|WebClient
name|webClient
decl_stmt|;
specifier|private
name|SyncInvoker
name|sync
decl_stmt|;
specifier|private
name|Configuration
name|config
decl_stmt|;
specifier|public
name|InvocationBuilderImpl
parameter_list|(
name|WebClient
name|webClient
parameter_list|,
name|Configuration
name|config
parameter_list|)
block|{
name|this
operator|.
name|webClient
operator|=
name|webClient
expr_stmt|;
name|this
operator|.
name|sync
operator|=
name|webClient
operator|.
name|sync
argument_list|()
expr_stmt|;
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
block|}
specifier|public
name|WebClient
name|getWebClient
parameter_list|()
block|{
return|return
name|this
operator|.
name|webClient
return|;
block|}
annotation|@
name|Override
specifier|public
name|Response
name|delete
parameter_list|()
block|{
return|return
name|sync
operator|.
name|delete
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|delete
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|sync
operator|.
name|delete
argument_list|(
name|cls
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|delete
parameter_list|(
name|GenericType
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|sync
operator|.
name|delete
argument_list|(
name|type
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Response
name|get
parameter_list|()
block|{
return|return
name|sync
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|get
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|sync
operator|.
name|get
argument_list|(
name|cls
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|get
parameter_list|(
name|GenericType
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|sync
operator|.
name|get
argument_list|(
name|type
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Response
name|head
parameter_list|()
block|{
return|return
name|sync
operator|.
name|head
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Response
name|method
parameter_list|(
name|String
name|method
parameter_list|)
block|{
return|return
name|sync
operator|.
name|method
argument_list|(
name|method
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|method
parameter_list|(
name|String
name|method
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|sync
operator|.
name|method
argument_list|(
name|method
argument_list|,
name|cls
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|method
parameter_list|(
name|String
name|method
parameter_list|,
name|GenericType
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|sync
operator|.
name|method
argument_list|(
name|method
argument_list|,
name|type
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Response
name|method
parameter_list|(
name|String
name|method
parameter_list|,
name|Entity
argument_list|<
name|?
argument_list|>
name|entity
parameter_list|)
block|{
return|return
name|sync
operator|.
name|method
argument_list|(
name|method
argument_list|,
name|entity
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|method
parameter_list|(
name|String
name|method
parameter_list|,
name|Entity
argument_list|<
name|?
argument_list|>
name|entity
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|sync
operator|.
name|method
argument_list|(
name|method
argument_list|,
name|entity
argument_list|,
name|cls
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|method
parameter_list|(
name|String
name|method
parameter_list|,
name|Entity
argument_list|<
name|?
argument_list|>
name|entity
parameter_list|,
name|GenericType
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|sync
operator|.
name|method
argument_list|(
name|method
argument_list|,
name|entity
argument_list|,
name|type
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Response
name|options
parameter_list|()
block|{
return|return
name|sync
operator|.
name|options
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|options
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|sync
operator|.
name|options
argument_list|(
name|cls
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|options
parameter_list|(
name|GenericType
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|sync
operator|.
name|options
argument_list|(
name|type
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Response
name|post
parameter_list|(
name|Entity
argument_list|<
name|?
argument_list|>
name|entity
parameter_list|)
block|{
return|return
name|sync
operator|.
name|post
argument_list|(
name|entity
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|post
parameter_list|(
name|Entity
argument_list|<
name|?
argument_list|>
name|entity
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|sync
operator|.
name|post
argument_list|(
name|entity
argument_list|,
name|cls
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|post
parameter_list|(
name|Entity
argument_list|<
name|?
argument_list|>
name|entity
parameter_list|,
name|GenericType
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|sync
operator|.
name|post
argument_list|(
name|entity
argument_list|,
name|type
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Response
name|put
parameter_list|(
name|Entity
argument_list|<
name|?
argument_list|>
name|entity
parameter_list|)
block|{
return|return
name|sync
operator|.
name|put
argument_list|(
name|entity
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|put
parameter_list|(
name|Entity
argument_list|<
name|?
argument_list|>
name|entity
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|sync
operator|.
name|put
argument_list|(
name|entity
argument_list|,
name|cls
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|put
parameter_list|(
name|Entity
argument_list|<
name|?
argument_list|>
name|entity
parameter_list|,
name|GenericType
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|sync
operator|.
name|put
argument_list|(
name|entity
argument_list|,
name|type
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Response
name|trace
parameter_list|()
block|{
return|return
name|sync
operator|.
name|trace
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|trace
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|sync
operator|.
name|trace
argument_list|(
name|cls
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|trace
parameter_list|(
name|GenericType
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|sync
operator|.
name|trace
argument_list|(
name|type
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Builder
name|accept
parameter_list|(
name|String
modifier|...
name|types
parameter_list|)
block|{
name|webClient
operator|.
name|accept
argument_list|(
name|types
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|Builder
name|accept
parameter_list|(
name|MediaType
modifier|...
name|types
parameter_list|)
block|{
name|webClient
operator|.
name|accept
argument_list|(
name|types
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|Builder
name|acceptEncoding
parameter_list|(
name|String
modifier|...
name|enc
parameter_list|)
block|{
name|webClient
operator|.
name|acceptEncoding
argument_list|(
name|enc
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|Builder
name|acceptLanguage
parameter_list|(
name|Locale
modifier|...
name|lang
parameter_list|)
block|{
for|for
control|(
name|Locale
name|l
range|:
name|lang
control|)
block|{
name|webClient
operator|.
name|acceptLanguage
argument_list|(
name|HttpUtils
operator|.
name|toHttpLanguage
argument_list|(
name|l
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|Builder
name|acceptLanguage
parameter_list|(
name|String
modifier|...
name|lang
parameter_list|)
block|{
name|webClient
operator|.
name|acceptLanguage
argument_list|(
name|lang
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|Builder
name|cacheControl
parameter_list|(
name|CacheControl
name|control
parameter_list|)
block|{
name|webClient
operator|.
name|header
argument_list|(
name|HttpHeaders
operator|.
name|CACHE_CONTROL
argument_list|,
name|control
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|Builder
name|cookie
parameter_list|(
name|Cookie
name|cookie
parameter_list|)
block|{
name|webClient
operator|.
name|cookie
argument_list|(
name|cookie
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|Builder
name|cookie
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|webClient
operator|.
name|header
argument_list|(
name|HttpHeaders
operator|.
name|COOKIE
argument_list|,
name|name
operator|+
literal|"="
operator|+
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|Builder
name|header
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|RuntimeDelegate
name|rd
init|=
name|HttpUtils
operator|.
name|getOtherRuntimeDelegate
argument_list|()
decl_stmt|;
name|doSetHeader
argument_list|(
name|rd
argument_list|,
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|Builder
name|headers
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|)
block|{
name|webClient
operator|.
name|removeAllHeaders
argument_list|()
expr_stmt|;
if|if
condition|(
name|headers
operator|!=
literal|null
condition|)
block|{
name|RuntimeDelegate
name|rd
init|=
name|HttpUtils
operator|.
name|getOtherRuntimeDelegate
argument_list|()
decl_stmt|;
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
name|Object
argument_list|>
argument_list|>
name|entry
range|:
name|headers
operator|.
name|entrySet
argument_list|()
control|)
block|{
for|for
control|(
name|Object
name|value
range|:
name|entry
operator|.
name|getValue
argument_list|()
control|)
block|{
name|doSetHeader
argument_list|(
name|rd
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|this
return|;
block|}
specifier|private
name|void
name|doSetHeader
parameter_list|(
name|RuntimeDelegate
name|rd
parameter_list|,
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|HeaderDelegate
argument_list|<
name|Object
argument_list|>
name|hd
init|=
name|HttpUtils
operator|.
name|getHeaderDelegate
argument_list|(
name|rd
argument_list|,
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|hd
operator|!=
literal|null
condition|)
block|{
name|value
operator|=
name|hd
operator|.
name|toString
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
comment|// If value is null then all current headers of the same name should be removed
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|webClient
operator|.
name|replaceHeader
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|webClient
operator|.
name|header
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Builder
name|property
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|contextProps
init|=
name|WebClient
operator|.
name|getConfig
argument_list|(
name|webClient
argument_list|)
operator|.
name|getRequestContext
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|filterProps
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|contextProps
operator|.
name|get
argument_list|(
name|PROPERTY_KEY
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|filterProps
operator|==
literal|null
condition|)
block|{
name|filterProps
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|contextProps
operator|.
name|put
argument_list|(
name|PROPERTY_KEY
argument_list|,
name|filterProps
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|filterProps
operator|.
name|remove
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|filterProps
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|AsyncInvoker
name|async
parameter_list|()
block|{
return|return
name|webClient
operator|.
name|async
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Invocation
name|build
parameter_list|(
name|String
name|method
parameter_list|)
block|{
return|return
operator|new
name|InvocationImpl
argument_list|(
name|method
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Invocation
name|build
parameter_list|(
name|String
name|method
parameter_list|,
name|Entity
argument_list|<
name|?
argument_list|>
name|entity
parameter_list|)
block|{
return|return
operator|new
name|InvocationImpl
argument_list|(
name|method
argument_list|,
name|entity
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Invocation
name|buildDelete
parameter_list|()
block|{
return|return
name|build
argument_list|(
name|HttpMethod
operator|.
name|DELETE
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Invocation
name|buildGet
parameter_list|()
block|{
return|return
name|build
argument_list|(
name|HttpMethod
operator|.
name|GET
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Invocation
name|buildPost
parameter_list|(
name|Entity
argument_list|<
name|?
argument_list|>
name|entity
parameter_list|)
block|{
return|return
name|build
argument_list|(
name|HttpMethod
operator|.
name|POST
argument_list|,
name|entity
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Invocation
name|buildPut
parameter_list|(
name|Entity
argument_list|<
name|?
argument_list|>
name|entity
parameter_list|)
block|{
return|return
name|build
argument_list|(
name|HttpMethod
operator|.
name|PUT
argument_list|,
name|entity
argument_list|)
return|;
block|}
specifier|private
class|class
name|InvocationImpl
implements|implements
name|Invocation
block|{
specifier|private
name|Invocation
operator|.
name|Builder
name|invBuilder
decl_stmt|;
specifier|private
name|String
name|httpMethod
decl_stmt|;
specifier|private
name|Entity
argument_list|<
name|?
argument_list|>
name|entity
decl_stmt|;
name|InvocationImpl
parameter_list|(
name|String
name|httpMethod
parameter_list|)
block|{
name|this
argument_list|(
name|httpMethod
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
name|InvocationImpl
parameter_list|(
name|String
name|httpMethod
parameter_list|,
name|Entity
argument_list|<
name|?
argument_list|>
name|entity
parameter_list|)
block|{
name|this
operator|.
name|invBuilder
operator|=
name|InvocationBuilderImpl
operator|.
name|this
expr_stmt|;
name|this
operator|.
name|httpMethod
operator|=
name|httpMethod
expr_stmt|;
name|this
operator|.
name|entity
operator|=
name|entity
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Response
name|invoke
parameter_list|()
block|{
return|return
name|invBuilder
operator|.
name|method
argument_list|(
name|httpMethod
argument_list|,
name|entity
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|invoke
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|invBuilder
operator|.
name|method
argument_list|(
name|httpMethod
argument_list|,
name|entity
argument_list|,
name|cls
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|invoke
parameter_list|(
name|GenericType
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|invBuilder
operator|.
name|method
argument_list|(
name|httpMethod
argument_list|,
name|entity
argument_list|,
name|type
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Invocation
name|property
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|invBuilder
operator|.
name|property
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|Future
argument_list|<
name|Response
argument_list|>
name|submit
parameter_list|()
block|{
return|return
name|invBuilder
operator|.
name|async
argument_list|()
operator|.
name|method
argument_list|(
name|httpMethod
argument_list|,
name|entity
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Future
argument_list|<
name|T
argument_list|>
name|submit
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|invBuilder
operator|.
name|async
argument_list|()
operator|.
name|method
argument_list|(
name|httpMethod
argument_list|,
name|entity
argument_list|,
name|cls
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Future
argument_list|<
name|T
argument_list|>
name|submit
parameter_list|(
name|GenericType
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|invBuilder
operator|.
name|async
argument_list|()
operator|.
name|method
argument_list|(
name|httpMethod
argument_list|,
name|entity
argument_list|,
name|type
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Future
argument_list|<
name|T
argument_list|>
name|submit
parameter_list|(
name|InvocationCallback
argument_list|<
name|T
argument_list|>
name|callback
parameter_list|)
block|{
return|return
name|invBuilder
operator|.
name|async
argument_list|()
operator|.
name|method
argument_list|(
name|httpMethod
argument_list|,
name|entity
argument_list|,
name|callback
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|CompletionStageRxInvoker
name|rx
parameter_list|()
block|{
return|return
name|webClient
operator|.
name|rx
argument_list|(
name|getConfiguredExecutorService
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
extends|extends
name|RxInvoker
parameter_list|>
name|T
name|rx
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|rxCls
parameter_list|)
block|{
return|return
name|webClient
operator|.
name|rx
argument_list|(
name|rxCls
argument_list|,
name|getConfiguredExecutorService
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|ExecutorService
name|getConfiguredExecutorService
parameter_list|()
block|{
return|return
operator|(
name|ExecutorService
operator|)
name|config
operator|.
name|getProperty
argument_list|(
name|AbstractClient
operator|.
name|EXECUTOR_SERVICE_PROPERTY
argument_list|)
return|;
block|}
block|}
end_class

end_unit

