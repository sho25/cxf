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
name|security
operator|.
name|KeyStore
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
name|ScheduledExecutorService
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
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|HostnameVerifier
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|KeyManagerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|TrustManagerFactory
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
name|ProcessingException
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
name|RuntimeType
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
name|Client
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
name|ClientBuilder
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
name|jaxrs
operator|.
name|client
operator|.
name|AbstractClient
import|;
end_import

begin_class
specifier|public
class|class
name|ClientBuilderImpl
extends|extends
name|ClientBuilder
block|{
specifier|private
name|ClientConfigurableImpl
argument_list|<
name|ClientBuilder
argument_list|>
name|configImpl
decl_stmt|;
specifier|private
name|TLSConfiguration
name|secConfig
init|=
operator|new
name|TLSConfiguration
argument_list|()
decl_stmt|;
specifier|public
name|ClientBuilderImpl
parameter_list|()
block|{
name|configImpl
operator|=
operator|new
name|ClientConfigurableImpl
argument_list|<
name|ClientBuilder
argument_list|>
argument_list|(
name|this
argument_list|)
expr_stmt|;
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
name|ClientBuilder
name|property
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
return|return
name|configImpl
operator|.
name|property
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ClientBuilder
name|register
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|configImpl
operator|.
name|register
argument_list|(
name|cls
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ClientBuilder
name|register
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
return|return
name|configImpl
operator|.
name|register
argument_list|(
name|object
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ClientBuilder
name|register
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|int
name|index
parameter_list|)
block|{
return|return
name|configImpl
operator|.
name|register
argument_list|(
name|cls
argument_list|,
name|index
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ClientBuilder
name|register
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|contracts
parameter_list|)
block|{
return|return
name|configImpl
operator|.
name|register
argument_list|(
name|cls
argument_list|,
name|contracts
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ClientBuilder
name|register
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
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
name|map
parameter_list|)
block|{
return|return
name|configImpl
operator|.
name|register
argument_list|(
name|cls
argument_list|,
name|map
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ClientBuilder
name|register
parameter_list|(
name|Object
name|object
parameter_list|,
name|int
name|index
parameter_list|)
block|{
return|return
name|configImpl
operator|.
name|register
argument_list|(
name|object
argument_list|,
name|index
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ClientBuilder
name|register
parameter_list|(
name|Object
name|object
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|contracts
parameter_list|)
block|{
return|return
name|configImpl
operator|.
name|register
argument_list|(
name|object
argument_list|,
name|contracts
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ClientBuilder
name|register
parameter_list|(
name|Object
name|object
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
name|map
parameter_list|)
block|{
return|return
name|configImpl
operator|.
name|register
argument_list|(
name|object
argument_list|,
name|map
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Client
name|build
parameter_list|()
block|{
return|return
operator|new
name|ClientImpl
argument_list|(
name|configImpl
operator|.
name|getConfiguration
argument_list|()
argument_list|,
name|secConfig
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
block|{
name|super
operator|.
name|close
argument_list|()
expr_stmt|;
name|configImpl
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
specifier|public
name|ClientBuilder
name|hostnameVerifier
parameter_list|(
name|HostnameVerifier
name|verifier
parameter_list|)
block|{
name|secConfig
operator|.
name|getTlsClientParams
argument_list|()
operator|.
name|setHostnameVerifier
argument_list|(
name|verifier
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|ClientBuilder
name|sslContext
parameter_list|(
name|SSLContext
name|sslContext
parameter_list|)
block|{
name|secConfig
operator|.
name|getTlsClientParams
argument_list|()
operator|.
name|setKeyManagers
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|secConfig
operator|.
name|getTlsClientParams
argument_list|()
operator|.
name|setTrustManagers
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|secConfig
operator|.
name|setSslContext
argument_list|(
name|sslContext
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|ClientBuilder
name|keyStore
parameter_list|(
name|KeyStore
name|store
parameter_list|,
name|char
index|[]
name|password
parameter_list|)
block|{
name|secConfig
operator|.
name|setSslContext
argument_list|(
literal|null
argument_list|)
expr_stmt|;
try|try
block|{
name|KeyManagerFactory
name|tmf
init|=
name|KeyManagerFactory
operator|.
name|getInstance
argument_list|(
name|KeyManagerFactory
operator|.
name|getDefaultAlgorithm
argument_list|()
argument_list|)
decl_stmt|;
name|tmf
operator|.
name|init
argument_list|(
name|store
argument_list|,
name|password
argument_list|)
expr_stmt|;
name|secConfig
operator|.
name|getTlsClientParams
argument_list|()
operator|.
name|setKeyManagers
argument_list|(
name|tmf
operator|.
name|getKeyManagers
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ProcessingException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|ClientBuilder
name|trustStore
parameter_list|(
name|KeyStore
name|store
parameter_list|)
block|{
name|secConfig
operator|.
name|setSslContext
argument_list|(
literal|null
argument_list|)
expr_stmt|;
try|try
block|{
name|TrustManagerFactory
name|tmf
init|=
name|TrustManagerFactory
operator|.
name|getInstance
argument_list|(
name|TrustManagerFactory
operator|.
name|getDefaultAlgorithm
argument_list|()
argument_list|)
decl_stmt|;
name|tmf
operator|.
name|init
argument_list|(
name|store
argument_list|)
expr_stmt|;
name|secConfig
operator|.
name|getTlsClientParams
argument_list|()
operator|.
name|setTrustManagers
argument_list|(
name|tmf
operator|.
name|getTrustManagers
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ProcessingException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|ClientBuilder
name|withConfig
parameter_list|(
name|Configuration
name|cfg
parameter_list|)
block|{
if|if
condition|(
name|cfg
operator|.
name|getRuntimeType
argument_list|()
operator|!=
name|RuntimeType
operator|.
name|CLIENT
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|()
throw|;
block|}
name|configImpl
operator|=
operator|new
name|ClientConfigurableImpl
argument_list|<
name|ClientBuilder
argument_list|>
argument_list|(
name|this
argument_list|,
name|cfg
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|ClientBuilder
name|executorService
parameter_list|(
name|ExecutorService
name|executorService
parameter_list|)
block|{
return|return
name|configImpl
operator|.
name|property
argument_list|(
name|AbstractClient
operator|.
name|EXECUTOR_SERVICE_PROPERTY
argument_list|,
name|executorService
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ClientBuilder
name|scheduledExecutorService
parameter_list|(
name|ScheduledExecutorService
name|scheduledExecutorService
parameter_list|)
block|{
return|return
name|configImpl
operator|.
name|property
argument_list|(
literal|"scheduledExecutorService"
argument_list|,
name|scheduledExecutorService
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ClientBuilder
name|connectTimeout
parameter_list|(
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|timeUnit
parameter_list|)
block|{
name|validateTimeout
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
return|return
name|property
argument_list|(
name|ClientImpl
operator|.
name|HTTP_CONNECTION_TIMEOUT_PROP
argument_list|,
name|timeUnit
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
name|ClientBuilder
name|readTimeout
parameter_list|(
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|timeUnit
parameter_list|)
block|{
name|validateTimeout
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
return|return
name|property
argument_list|(
name|ClientImpl
operator|.
name|HTTP_RECEIVE_TIMEOUT_PROP
argument_list|,
name|timeUnit
operator|.
name|toMillis
argument_list|(
name|timeout
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|void
name|validateTimeout
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
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
literal|"Negative timeout is not allowed."
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

