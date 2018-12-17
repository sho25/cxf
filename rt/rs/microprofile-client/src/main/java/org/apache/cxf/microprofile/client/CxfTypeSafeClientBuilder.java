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
name|microprofile
operator|.
name|client
package|;
end_package

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
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|AccessController
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivilegedAction
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ServiceLoader
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
name|TimeUnit
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
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|StreamSupport
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
name|Configurable
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
name|jaxrs
operator|.
name|client
operator|.
name|spec
operator|.
name|ClientImpl
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
name|microprofile
operator|.
name|client
operator|.
name|config
operator|.
name|ConfigFacade
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|rest
operator|.
name|client
operator|.
name|RestClientBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|rest
operator|.
name|client
operator|.
name|annotation
operator|.
name|RegisterProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|rest
operator|.
name|client
operator|.
name|spi
operator|.
name|RestClientListener
import|;
end_import

begin_class
specifier|public
class|class
name|CxfTypeSafeClientBuilder
implements|implements
name|RestClientBuilder
implements|,
name|Configurable
argument_list|<
name|RestClientBuilder
argument_list|>
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
name|CxfTypeSafeClientBuilder
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|REST_CONN_TIMEOUT_FORMAT
init|=
literal|"%s/mp-rest/connectTimeout"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|REST_READ_TIMEOUT_FORMAT
init|=
literal|"%s/mp-rest/readTimeout"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|ClassLoader
argument_list|,
name|Collection
argument_list|<
name|RestClientListener
argument_list|>
argument_list|>
name|REST_CLIENT_LISTENERS
init|=
operator|new
name|WeakHashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|String
name|baseUri
decl_stmt|;
specifier|private
name|ExecutorService
name|executorService
decl_stmt|;
specifier|private
specifier|final
name|MicroProfileClientConfigurableImpl
argument_list|<
name|RestClientBuilder
argument_list|>
name|configImpl
init|=
operator|new
name|MicroProfileClientConfigurableImpl
argument_list|<>
argument_list|(
name|this
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|Collection
argument_list|<
name|RestClientListener
argument_list|>
name|listeners
parameter_list|()
block|{
name|ClassLoader
name|threadContextClassLoader
decl_stmt|;
if|if
condition|(
name|System
operator|.
name|getSecurityManager
argument_list|()
operator|==
literal|null
condition|)
block|{
name|threadContextClassLoader
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|threadContextClassLoader
operator|=
name|AccessController
operator|.
name|doPrivileged
argument_list|(
call|(
name|PrivilegedAction
argument_list|<
name|ClassLoader
argument_list|>
call|)
argument_list|()
operator|->
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
synchronized|synchronized
init|(
name|REST_CLIENT_LISTENERS
init|)
block|{
return|return
name|REST_CLIENT_LISTENERS
operator|.
name|computeIfAbsent
argument_list|(
name|threadContextClassLoader
argument_list|,
name|key
lambda|->
name|StreamSupport
operator|.
name|stream
argument_list|(
name|ServiceLoader
operator|.
name|load
argument_list|(
name|RestClientListener
operator|.
name|class
argument_list|)
operator|.
name|spliterator
argument_list|()
argument_list|,
literal|false
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|RestClientBuilder
name|baseUrl
parameter_list|(
name|URL
name|url
parameter_list|)
block|{
name|this
operator|.
name|baseUri
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|url
argument_list|)
operator|.
name|toExternalForm
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|RestClientBuilder
name|baseUri
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
name|this
operator|.
name|baseUri
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|uri
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|RestClientBuilder
name|executorService
parameter_list|(
name|ExecutorService
name|executor
parameter_list|)
block|{
if|if
condition|(
literal|null
operator|==
name|executor
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"executor must not be null"
argument_list|)
throw|;
block|}
name|this
operator|.
name|executorService
operator|=
name|executor
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|RestClientBuilder
name|connectTimeout
parameter_list|(
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
if|if
condition|(
literal|null
operator|==
name|unit
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"time unit must not be null"
argument_list|)
throw|;
block|}
if|if
condition|(
name|timeout
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"timeout must be non-negative"
argument_list|)
throw|;
block|}
return|return
name|property
argument_list|(
name|ClientImpl
operator|.
name|HTTP_CONNECTION_TIMEOUT_PROP
argument_list|,
name|unit
operator|.
name|toMillis
argument_list|(
name|timeout
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|RestClientBuilder
name|readTimeout
parameter_list|(
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
if|if
condition|(
literal|null
operator|==
name|unit
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"time unit must not be null"
argument_list|)
throw|;
block|}
if|if
condition|(
name|timeout
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"timeout must be non-negative"
argument_list|)
throw|;
block|}
return|return
name|property
argument_list|(
name|ClientImpl
operator|.
name|HTTP_RECEIVE_TIMEOUT_PROP
argument_list|,
name|unit
operator|.
name|toMillis
argument_list|(
name|timeout
argument_list|)
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
name|build
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|aClass
parameter_list|)
block|{
if|if
condition|(
name|baseUri
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"baseUrl not set"
argument_list|)
throw|;
block|}
name|Validator
operator|.
name|checkValid
argument_list|(
name|aClass
argument_list|)
expr_stmt|;
name|RegisterProvider
index|[]
name|providers
init|=
name|aClass
operator|.
name|getAnnotationsByType
argument_list|(
name|RegisterProvider
operator|.
name|class
argument_list|)
decl_stmt|;
name|Configuration
name|config
init|=
name|configImpl
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|providers
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|RegisterProvider
name|provider
range|:
name|providers
control|)
block|{
if|if
condition|(
operator|!
name|config
operator|.
name|isRegistered
argument_list|(
name|provider
operator|.
name|value
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|provider
operator|.
name|priority
argument_list|()
operator|==
operator|-
literal|1
condition|)
block|{
name|register
argument_list|(
name|provider
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|register
argument_list|(
name|provider
operator|.
name|value
argument_list|()
argument_list|,
name|provider
operator|.
name|priority
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|final
name|String
name|interfaceName
init|=
name|aClass
operator|.
name|getName
argument_list|()
decl_stmt|;
name|ConfigFacade
operator|.
name|getOptionalLong
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|REST_CONN_TIMEOUT_FORMAT
argument_list|,
name|interfaceName
argument_list|)
argument_list|)
operator|.
name|ifPresent
argument_list|(
name|timeoutValue
lambda|->
block|{
name|connectTimeout
argument_list|(
name|timeoutValue
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINEST
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|finest
argument_list|(
literal|"readTimeout set by MP Config: "
operator|+
name|timeoutValue
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|ConfigFacade
operator|.
name|getOptionalLong
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|REST_READ_TIMEOUT_FORMAT
argument_list|,
name|interfaceName
argument_list|)
argument_list|)
operator|.
name|ifPresent
argument_list|(
name|timeoutValue
lambda|->
block|{
name|readTimeout
argument_list|(
name|timeoutValue
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINEST
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|finest
argument_list|(
literal|"readTimeout set by MP Config: "
operator|+
name|timeoutValue
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|listeners
argument_list|()
operator|.
name|forEach
argument_list|(
name|l
lambda|->
name|l
operator|.
name|onNewClient
argument_list|(
name|aClass
argument_list|,
name|this
argument_list|)
argument_list|)
expr_stmt|;
name|MicroProfileClientFactoryBean
name|bean
init|=
operator|new
name|MicroProfileClientFactoryBean
argument_list|(
name|configImpl
argument_list|,
name|baseUri
argument_list|,
name|aClass
argument_list|,
name|executorService
argument_list|)
decl_stmt|;
return|return
name|bean
operator|.
name|create
argument_list|(
name|aClass
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Configuration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configImpl
operator|.
name|getConfiguration
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|RestClientBuilder
name|property
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|configImpl
operator|.
name|property
argument_list|(
name|key
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
name|RestClientBuilder
name|register
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|componentClass
parameter_list|)
block|{
name|configImpl
operator|.
name|register
argument_list|(
name|componentClass
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|RestClientBuilder
name|register
parameter_list|(
name|Object
name|component
parameter_list|)
block|{
name|configImpl
operator|.
name|register
argument_list|(
name|component
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|RestClientBuilder
name|register
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|componentClass
parameter_list|,
name|int
name|priority
parameter_list|)
block|{
name|configImpl
operator|.
name|register
argument_list|(
name|componentClass
argument_list|,
name|priority
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|RestClientBuilder
name|register
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|componentClass
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|contracts
parameter_list|)
block|{
name|configImpl
operator|.
name|register
argument_list|(
name|componentClass
argument_list|,
name|contracts
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|RestClientBuilder
name|register
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|componentClass
parameter_list|,
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|contracts
parameter_list|)
block|{
name|configImpl
operator|.
name|register
argument_list|(
name|componentClass
argument_list|,
name|contracts
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|RestClientBuilder
name|register
parameter_list|(
name|Object
name|component
parameter_list|,
name|int
name|priority
parameter_list|)
block|{
name|configImpl
operator|.
name|register
argument_list|(
name|component
argument_list|,
name|priority
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|RestClientBuilder
name|register
parameter_list|(
name|Object
name|component
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|contracts
parameter_list|)
block|{
name|configImpl
operator|.
name|register
argument_list|(
name|component
argument_list|,
name|contracts
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|RestClientBuilder
name|register
parameter_list|(
name|Object
name|component
parameter_list|,
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|contracts
parameter_list|)
block|{
name|configImpl
operator|.
name|register
argument_list|(
name|component
argument_list|,
name|contracts
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

