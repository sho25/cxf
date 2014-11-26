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
name|Closeable
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
name|net
operator|.
name|URISyntaxException
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|PreDestroy
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
name|cache
operator|.
name|CacheManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|Caching
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|configuration
operator|.
name|Factory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|configuration
operator|.
name|MutableConfiguration
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|expiry
operator|.
name|ExpiryPolicy
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|integration
operator|.
name|CacheLoader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|integration
operator|.
name|CacheWriter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|spi
operator|.
name|CachingProvider
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
name|Feature
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
name|FeatureContext
import|;
end_import

begin_class
specifier|public
class|class
name|CacheControlFeature
implements|implements
name|Feature
block|{
specifier|private
name|CachingProvider
name|provider
decl_stmt|;
specifier|private
name|CacheManager
name|manager
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
specifier|private
name|boolean
name|cacheResponseInputStream
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|configure
parameter_list|(
specifier|final
name|FeatureContext
name|context
parameter_list|)
block|{
comment|// TODO: read context properties to exclude some patterns?
specifier|final
name|Cache
argument_list|<
name|Key
argument_list|,
name|Entry
argument_list|>
name|entryCache
init|=
name|createCache
argument_list|(
name|context
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getProperties
argument_list|()
argument_list|)
decl_stmt|;
name|context
operator|.
name|register
argument_list|(
operator|new
name|CacheControlClientRequestFilter
argument_list|(
name|entryCache
argument_list|)
argument_list|)
expr_stmt|;
name|CacheControlClientReaderInterceptor
name|reader
init|=
operator|new
name|CacheControlClientReaderInterceptor
argument_list|(
name|entryCache
argument_list|)
decl_stmt|;
name|reader
operator|.
name|setCacheResponseInputStream
argument_list|(
name|cacheResponseInputStream
argument_list|)
expr_stmt|;
name|context
operator|.
name|register
argument_list|(
name|reader
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|PreDestroy
comment|// TODO: check it is called
specifier|public
name|void
name|close
parameter_list|()
block|{
for|for
control|(
specifier|final
name|Closeable
name|c
range|:
name|Arrays
operator|.
name|asList
argument_list|(
name|cache
argument_list|,
name|manager
argument_list|,
name|provider
argument_list|)
control|)
block|{
try|try
block|{
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|c
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
specifier|final
name|Exception
name|e
parameter_list|)
block|{
comment|// no-op
block|}
block|}
block|}
specifier|private
name|Cache
argument_list|<
name|Key
argument_list|,
name|Entry
argument_list|>
name|createCache
parameter_list|(
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
specifier|final
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|props
operator|.
name|putAll
argument_list|(
name|properties
argument_list|)
expr_stmt|;
specifier|final
name|String
name|prefix
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"."
decl_stmt|;
specifier|final
name|String
name|uri
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|prefix
operator|+
literal|"config-uri"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|name
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|prefix
operator|+
literal|"name"
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|ClassLoader
name|contextClassLoader
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
name|provider
operator|=
name|Caching
operator|.
name|getCachingProvider
argument_list|()
expr_stmt|;
try|try
block|{
name|manager
operator|=
name|provider
operator|.
name|getCacheManager
argument_list|(
name|uri
operator|==
literal|null
condition|?
name|provider
operator|.
name|getDefaultURI
argument_list|()
else|:
operator|new
name|URI
argument_list|(
name|uri
argument_list|)
argument_list|,
name|contextClassLoader
argument_list|,
name|props
argument_list|)
expr_stmt|;
specifier|final
name|MutableConfiguration
argument_list|<
name|Key
argument_list|,
name|Entry
argument_list|>
name|configuration
init|=
operator|new
name|MutableConfiguration
argument_list|<
name|Key
argument_list|,
name|Entry
argument_list|>
argument_list|()
operator|.
name|setReadThrough
argument_list|(
literal|"true"
operator|.
name|equalsIgnoreCase
argument_list|(
name|props
operator|.
name|getProperty
argument_list|(
name|prefix
operator|+
literal|"readThrough"
argument_list|,
literal|"false"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|setWriteThrough
argument_list|(
literal|"true"
operator|.
name|equalsIgnoreCase
argument_list|(
name|props
operator|.
name|getProperty
argument_list|(
name|prefix
operator|+
literal|"writeThrough"
argument_list|,
literal|"false"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|setManagementEnabled
argument_list|(
literal|"true"
operator|.
name|equalsIgnoreCase
argument_list|(
name|props
operator|.
name|getProperty
argument_list|(
name|prefix
operator|+
literal|"managementEnabled"
argument_list|,
literal|"false"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|setStatisticsEnabled
argument_list|(
literal|"true"
operator|.
name|equalsIgnoreCase
argument_list|(
name|props
operator|.
name|getProperty
argument_list|(
name|prefix
operator|+
literal|"statisticsEnabled"
argument_list|,
literal|"false"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|setStoreByValue
argument_list|(
literal|"true"
operator|.
name|equalsIgnoreCase
argument_list|(
name|props
operator|.
name|getProperty
argument_list|(
name|prefix
operator|+
literal|"storeByValue"
argument_list|,
literal|"false"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|String
name|loader
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|prefix
operator|+
literal|"loaderFactory"
argument_list|)
decl_stmt|;
if|if
condition|(
name|loader
operator|!=
literal|null
condition|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Factory
argument_list|<
name|?
extends|extends
name|CacheLoader
argument_list|<
name|Key
argument_list|,
name|Entry
argument_list|>
argument_list|>
name|f
init|=
name|newInstance
argument_list|(
name|contextClassLoader
argument_list|,
name|loader
argument_list|,
name|Factory
operator|.
name|class
argument_list|)
decl_stmt|;
name|configuration
operator|.
name|setCacheLoaderFactory
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
specifier|final
name|String
name|writer
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|prefix
operator|+
literal|"writerFactory"
argument_list|)
decl_stmt|;
if|if
condition|(
name|writer
operator|!=
literal|null
condition|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Factory
argument_list|<
name|?
extends|extends
name|CacheWriter
argument_list|<
name|Key
argument_list|,
name|Entry
argument_list|>
argument_list|>
name|f
init|=
name|newInstance
argument_list|(
name|contextClassLoader
argument_list|,
name|writer
argument_list|,
name|Factory
operator|.
name|class
argument_list|)
decl_stmt|;
name|configuration
operator|.
name|setCacheWriterFactory
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
specifier|final
name|String
name|expiry
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|prefix
operator|+
literal|"expiryFactory"
argument_list|)
decl_stmt|;
if|if
condition|(
name|expiry
operator|!=
literal|null
condition|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Factory
argument_list|<
name|?
extends|extends
name|ExpiryPolicy
argument_list|>
name|f
init|=
name|newInstance
argument_list|(
name|contextClassLoader
argument_list|,
name|expiry
argument_list|,
name|Factory
operator|.
name|class
argument_list|)
decl_stmt|;
name|configuration
operator|.
name|setExpiryPolicyFactory
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
name|cache
operator|=
name|manager
operator|.
name|createCache
argument_list|(
name|name
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
return|return
name|cache
return|;
block|}
catch|catch
parameter_list|(
specifier|final
name|URISyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|newInstance
parameter_list|(
specifier|final
name|ClassLoader
name|contextClassLoader
parameter_list|,
specifier|final
name|String
name|clazz
parameter_list|,
specifier|final
name|Class
argument_list|<
name|T
argument_list|>
name|cast
parameter_list|)
block|{
try|try
block|{
return|return
operator|(
name|T
operator|)
name|contextClassLoader
operator|.
name|loadClass
argument_list|(
name|clazz
argument_list|)
operator|.
name|newInstance
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
specifier|final
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|setCacheResponseInputStream
parameter_list|(
name|boolean
name|cacheStream
parameter_list|)
block|{
name|this
operator|.
name|cacheResponseInputStream
operator|=
name|cacheStream
expr_stmt|;
block|}
block|}
end_class

end_unit

