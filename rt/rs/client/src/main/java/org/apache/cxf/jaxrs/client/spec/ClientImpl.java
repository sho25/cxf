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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|WeakHashMap
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
name|SSLContext
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
name|WebTarget
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
name|Link
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
name|UriBuilder
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
name|configuration
operator|.
name|jsse
operator|.
name|TLSClientParameters
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
name|ClientConfiguration
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
name|ClientProviderFactory
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
name|JAXRSClientFactoryBean
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
name|model
operator|.
name|FilterProviderInfo
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
name|transport
operator|.
name|https
operator|.
name|SSLUtils
import|;
end_import

begin_class
specifier|public
class|class
name|ClientImpl
implements|implements
name|Client
block|{
specifier|private
specifier|static
specifier|final
name|String
name|HTTP_CONNECTION_TIMEOUT_PROP
init|=
literal|"http.connection.timeout"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|HTTP_RECEIVE_TIMEOUT_PROP
init|=
literal|"http.receive.timeout"
decl_stmt|;
specifier|private
name|Configurable
argument_list|<
name|Client
argument_list|>
name|configImpl
decl_stmt|;
specifier|private
name|TLSConfiguration
name|secConfig
decl_stmt|;
specifier|private
name|boolean
name|closed
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|WebClient
argument_list|>
name|baseClients
init|=
name|Collections
operator|.
name|newSetFromMap
argument_list|(
operator|new
name|WeakHashMap
argument_list|<
name|WebClient
argument_list|,
name|Boolean
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
specifier|public
name|ClientImpl
parameter_list|(
name|Configuration
name|config
parameter_list|,
name|TLSConfiguration
name|secConfig
parameter_list|)
block|{
name|configImpl
operator|=
operator|new
name|ClientConfigurableImpl
argument_list|<
name|Client
argument_list|>
argument_list|(
name|this
argument_list|,
name|config
argument_list|)
expr_stmt|;
name|this
operator|.
name|secConfig
operator|=
name|secConfig
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
block|{
if|if
condition|(
operator|!
name|closed
condition|)
block|{
for|for
control|(
name|WebClient
name|wc
range|:
name|baseClients
control|)
block|{
name|wc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|baseClients
operator|=
literal|null
expr_stmt|;
name|closed
operator|=
literal|true
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Builder
name|invocation
parameter_list|(
name|Link
name|link
parameter_list|)
block|{
name|checkNull
argument_list|(
name|link
argument_list|)
expr_stmt|;
name|checkClosed
argument_list|()
expr_stmt|;
name|Builder
name|builder
init|=
name|target
argument_list|(
name|link
operator|.
name|getUriBuilder
argument_list|()
argument_list|)
operator|.
name|request
argument_list|()
decl_stmt|;
name|String
name|type
init|=
name|link
operator|.
name|getType
argument_list|()
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|accept
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
return|;
block|}
annotation|@
name|Override
specifier|public
name|WebTarget
name|target
parameter_list|(
name|UriBuilder
name|builder
parameter_list|)
block|{
name|checkNull
argument_list|(
name|builder
argument_list|)
expr_stmt|;
name|checkClosed
argument_list|()
expr_stmt|;
return|return
operator|new
name|WebTargetImpl
argument_list|(
name|builder
argument_list|,
name|getConfiguration
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|WebTarget
name|target
parameter_list|(
name|String
name|address
parameter_list|)
block|{
name|checkNull
argument_list|(
name|address
argument_list|)
expr_stmt|;
if|if
condition|(
name|address
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|address
operator|=
literal|"/"
expr_stmt|;
block|}
return|return
name|target
argument_list|(
name|UriBuilder
operator|.
name|fromUri
argument_list|(
name|address
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|WebTarget
name|target
parameter_list|(
name|Link
name|link
parameter_list|)
block|{
name|checkNull
argument_list|(
name|link
argument_list|)
expr_stmt|;
return|return
name|target
argument_list|(
name|link
operator|.
name|getUriBuilder
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|WebTarget
name|target
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
name|checkNull
argument_list|(
name|uri
argument_list|)
expr_stmt|;
return|return
name|target
argument_list|(
name|UriBuilder
operator|.
name|fromUri
argument_list|(
name|uri
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|void
name|checkNull
parameter_list|(
name|Object
modifier|...
name|target
parameter_list|)
block|{
for|for
control|(
name|Object
name|o
range|:
name|target
control|)
block|{
if|if
condition|(
name|o
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Value is null"
argument_list|)
throw|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|HostnameVerifier
name|getHostnameVerifier
parameter_list|()
block|{
name|checkClosed
argument_list|()
expr_stmt|;
return|return
name|secConfig
operator|.
name|getTlsClientParams
argument_list|()
operator|.
name|getHostnameVerifier
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|SSLContext
name|getSslContext
parameter_list|()
block|{
name|checkClosed
argument_list|()
expr_stmt|;
if|if
condition|(
name|secConfig
operator|.
name|getSslContext
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|secConfig
operator|.
name|getSslContext
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|secConfig
operator|.
name|getTlsClientParams
argument_list|()
operator|.
name|getTrustManagers
argument_list|()
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
name|SSLUtils
operator|.
name|getSSLContext
argument_list|(
name|secConfig
operator|.
name|getTlsClientParams
argument_list|()
argument_list|)
return|;
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
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|private
name|void
name|checkClosed
parameter_list|()
block|{
if|if
condition|(
name|closed
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|()
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Configuration
name|getConfiguration
parameter_list|()
block|{
name|checkClosed
argument_list|()
expr_stmt|;
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
name|Client
name|property
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|checkClosed
argument_list|()
expr_stmt|;
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
name|Client
name|register
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
name|checkClosed
argument_list|()
expr_stmt|;
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
name|Client
name|register
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|checkClosed
argument_list|()
expr_stmt|;
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
name|Client
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
name|checkClosed
argument_list|()
expr_stmt|;
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
name|Client
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
name|checkClosed
argument_list|()
expr_stmt|;
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
name|Client
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
name|checkClosed
argument_list|()
expr_stmt|;
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
name|Client
name|register
parameter_list|(
name|Object
name|object
parameter_list|,
name|int
name|index
parameter_list|)
block|{
name|checkClosed
argument_list|()
expr_stmt|;
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
name|Client
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
name|checkClosed
argument_list|()
expr_stmt|;
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
name|Client
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
name|checkClosed
argument_list|()
expr_stmt|;
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
specifier|public
class|class
name|WebTargetImpl
implements|implements
name|WebTarget
block|{
specifier|private
name|Configurable
argument_list|<
name|WebTarget
argument_list|>
name|configImpl
decl_stmt|;
specifier|private
name|UriBuilder
name|uriBuilder
decl_stmt|;
specifier|private
name|WebClient
name|targetClient
decl_stmt|;
specifier|public
name|WebTargetImpl
parameter_list|(
name|UriBuilder
name|uriBuilder
parameter_list|,
name|Configuration
name|config
parameter_list|)
block|{
name|this
argument_list|(
name|uriBuilder
argument_list|,
name|config
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|WebTargetImpl
parameter_list|(
name|UriBuilder
name|uriBuilder
parameter_list|,
name|Configuration
name|config
parameter_list|,
name|WebClient
name|targetClient
parameter_list|)
block|{
name|this
operator|.
name|configImpl
operator|=
operator|new
name|ClientConfigurableImpl
argument_list|<
name|WebTarget
argument_list|>
argument_list|(
name|this
argument_list|,
name|config
argument_list|)
expr_stmt|;
name|this
operator|.
name|uriBuilder
operator|=
name|uriBuilder
operator|.
name|clone
argument_list|()
expr_stmt|;
name|this
operator|.
name|targetClient
operator|=
name|targetClient
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
name|targetClient
return|;
block|}
annotation|@
name|Override
specifier|public
name|Builder
name|request
parameter_list|()
block|{
name|checkClosed
argument_list|()
expr_stmt|;
name|initTargetClientIfNeeded
argument_list|()
expr_stmt|;
name|ClientProviderFactory
name|pf
init|=
name|ClientProviderFactory
operator|.
name|getInstance
argument_list|(
name|WebClient
operator|.
name|getConfig
argument_list|(
name|targetClient
argument_list|)
operator|.
name|getEndpoint
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|providers
init|=
operator|new
name|LinkedList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Configuration
name|cfg
init|=
name|configImpl
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|p
range|:
name|cfg
operator|.
name|getInstances
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
operator|(
name|p
operator|instanceof
name|Feature
operator|)
condition|)
block|{
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
init|=
name|cfg
operator|.
name|getContracts
argument_list|(
name|p
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|contracts
operator|==
literal|null
operator|||
name|contracts
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|providers
operator|.
name|add
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|providers
operator|.
name|add
argument_list|(
operator|new
name|FilterProviderInfo
argument_list|<
name|Object
argument_list|>
argument_list|(
name|p
argument_list|,
name|pf
operator|.
name|getBus
argument_list|()
argument_list|,
name|contracts
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|pf
operator|.
name|setUserProviders
argument_list|(
name|providers
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|configProps
init|=
name|getConfiguration
argument_list|()
operator|.
name|getProperties
argument_list|()
decl_stmt|;
name|ClientConfiguration
name|clientCfg
init|=
name|WebClient
operator|.
name|getConfig
argument_list|(
name|targetClient
argument_list|)
decl_stmt|;
name|clientCfg
operator|.
name|getRequestContext
argument_list|()
operator|.
name|putAll
argument_list|(
name|configProps
argument_list|)
expr_stmt|;
name|clientCfg
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|Client
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|ClientImpl
operator|.
name|this
argument_list|)
expr_stmt|;
name|clientCfg
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|Configuration
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
comment|// TLS
name|TLSClientParameters
name|tlsParams
init|=
name|secConfig
operator|.
name|getTlsClientParams
argument_list|()
decl_stmt|;
if|if
condition|(
name|tlsParams
operator|.
name|getSSLSocketFactory
argument_list|()
operator|!=
literal|null
operator|||
name|tlsParams
operator|.
name|getTrustManagers
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|clientCfg
operator|.
name|getHttpConduit
argument_list|()
operator|.
name|setTlsClientParameters
argument_list|(
name|tlsParams
argument_list|)
expr_stmt|;
block|}
name|Long
name|connTimeOutValue
init|=
name|getLongValue
argument_list|(
name|configProps
operator|.
name|get
argument_list|(
name|HTTP_CONNECTION_TIMEOUT_PROP
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|connTimeOutValue
operator|!=
literal|null
condition|)
block|{
name|clientCfg
operator|.
name|getHttpConduit
argument_list|()
operator|.
name|getClient
argument_list|()
operator|.
name|setConnectionTimeout
argument_list|(
name|connTimeOutValue
argument_list|)
expr_stmt|;
block|}
name|Long
name|recTimeOutValue
init|=
name|getLongValue
argument_list|(
name|configProps
operator|.
name|get
argument_list|(
name|HTTP_RECEIVE_TIMEOUT_PROP
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|recTimeOutValue
operator|!=
literal|null
condition|)
block|{
name|clientCfg
operator|.
name|getHttpConduit
argument_list|()
operator|.
name|getClient
argument_list|()
operator|.
name|setReceiveTimeout
argument_list|(
name|recTimeOutValue
argument_list|)
expr_stmt|;
block|}
comment|// start building the invocation
return|return
operator|new
name|InvocationBuilderImpl
argument_list|(
name|WebClient
operator|.
name|fromClient
argument_list|(
name|targetClient
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|Long
name|getLongValue
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|o
operator|instanceof
name|Long
condition|?
operator|(
name|Long
operator|)
name|o
else|:
name|o
operator|instanceof
name|String
condition|?
name|Long
operator|.
name|valueOf
argument_list|(
name|o
operator|.
name|toString
argument_list|()
argument_list|)
else|:
literal|null
return|;
block|}
specifier|private
name|void
name|initTargetClientIfNeeded
parameter_list|()
block|{
name|URI
name|uri
init|=
name|uriBuilder
operator|.
name|build
argument_list|()
decl_stmt|;
if|if
condition|(
name|targetClient
operator|==
literal|null
condition|)
block|{
name|JAXRSClientFactoryBean
name|bean
init|=
operator|new
name|JAXRSClientFactoryBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setAddress
argument_list|(
name|uri
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|targetClient
operator|=
name|bean
operator|.
name|createWebClient
argument_list|()
expr_stmt|;
name|ClientImpl
operator|.
name|this
operator|.
name|baseClients
operator|.
name|add
argument_list|(
name|targetClient
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|targetClient
operator|.
name|getCurrentURI
argument_list|()
operator|.
name|equals
argument_list|(
name|uri
argument_list|)
condition|)
block|{
name|targetClient
operator|.
name|to
argument_list|(
name|uri
operator|.
name|toString
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Builder
name|request
parameter_list|(
name|String
modifier|...
name|accept
parameter_list|)
block|{
return|return
name|request
argument_list|()
operator|.
name|accept
argument_list|(
name|accept
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Builder
name|request
parameter_list|(
name|MediaType
modifier|...
name|accept
parameter_list|)
block|{
return|return
name|request
argument_list|()
operator|.
name|accept
argument_list|(
name|accept
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|URI
name|getUri
parameter_list|()
block|{
name|checkClosed
argument_list|()
expr_stmt|;
return|return
name|uriBuilder
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|UriBuilder
name|getUriBuilder
parameter_list|()
block|{
name|checkClosed
argument_list|()
expr_stmt|;
return|return
name|uriBuilder
operator|.
name|clone
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|WebTarget
name|path
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|checkNull
argument_list|(
name|path
argument_list|)
expr_stmt|;
return|return
name|newWebTarget
argument_list|(
name|getUriBuilder
argument_list|()
operator|.
name|path
argument_list|(
name|path
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|WebTarget
name|queryParam
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
modifier|...
name|values
parameter_list|)
block|{
name|checkNullValues
argument_list|(
name|name
argument_list|,
name|values
argument_list|)
expr_stmt|;
name|UriBuilder
name|thebuilder
init|=
name|getUriBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|values
operator|==
literal|null
operator|||
name|values
operator|.
name|length
operator|==
literal|1
operator|&&
name|values
index|[
literal|0
index|]
operator|==
literal|null
condition|)
block|{
name|thebuilder
operator|.
name|replaceQueryParam
argument_list|(
name|name
argument_list|,
operator|(
name|Object
index|[]
operator|)
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|thebuilder
operator|.
name|queryParam
argument_list|(
name|name
argument_list|,
name|values
argument_list|)
expr_stmt|;
block|}
return|return
name|newWebTarget
argument_list|(
name|thebuilder
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|WebTarget
name|matrixParam
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
modifier|...
name|values
parameter_list|)
block|{
name|checkNullValues
argument_list|(
name|name
argument_list|,
name|values
argument_list|)
expr_stmt|;
name|UriBuilder
name|thebuilder
init|=
name|getUriBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|values
operator|==
literal|null
operator|||
name|values
operator|.
name|length
operator|==
literal|1
operator|&&
name|values
index|[
literal|0
index|]
operator|==
literal|null
condition|)
block|{
name|thebuilder
operator|.
name|replaceMatrixParam
argument_list|(
name|name
argument_list|,
operator|(
name|Object
index|[]
operator|)
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|thebuilder
operator|.
name|matrixParam
argument_list|(
name|name
argument_list|,
name|values
argument_list|)
expr_stmt|;
block|}
return|return
name|newWebTarget
argument_list|(
name|thebuilder
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|WebTarget
name|resolveTemplate
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
return|return
name|resolveTemplate
argument_list|(
name|name
argument_list|,
name|value
argument_list|,
literal|true
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|WebTarget
name|resolveTemplate
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|,
name|boolean
name|encodeSlash
parameter_list|)
block|{
name|checkNull
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|newWebTarget
argument_list|(
name|getUriBuilder
argument_list|()
operator|.
name|resolveTemplate
argument_list|(
name|name
argument_list|,
name|value
argument_list|,
name|encodeSlash
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|WebTarget
name|resolveTemplateFromEncoded
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|checkNull
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|newWebTarget
argument_list|(
name|getUriBuilder
argument_list|()
operator|.
name|resolveTemplateFromEncoded
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|WebTarget
name|resolveTemplates
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|templatesMap
parameter_list|)
block|{
return|return
name|resolveTemplates
argument_list|(
name|templatesMap
argument_list|,
literal|true
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|WebTarget
name|resolveTemplates
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|templatesMap
parameter_list|,
name|boolean
name|encodeSlash
parameter_list|)
block|{
name|checkNullMap
argument_list|(
name|templatesMap
argument_list|)
expr_stmt|;
if|if
condition|(
name|templatesMap
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|this
return|;
block|}
return|return
name|newWebTarget
argument_list|(
name|getUriBuilder
argument_list|()
operator|.
name|resolveTemplates
argument_list|(
name|templatesMap
argument_list|,
name|encodeSlash
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|WebTarget
name|resolveTemplatesFromEncoded
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|templatesMap
parameter_list|)
block|{
name|checkNullMap
argument_list|(
name|templatesMap
argument_list|)
expr_stmt|;
if|if
condition|(
name|templatesMap
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|this
return|;
block|}
return|return
name|newWebTarget
argument_list|(
name|getUriBuilder
argument_list|()
operator|.
name|resolveTemplatesFromEncoded
argument_list|(
name|templatesMap
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|WebTarget
name|newWebTarget
parameter_list|(
name|UriBuilder
name|newBuilder
parameter_list|)
block|{
name|checkClosed
argument_list|()
expr_stmt|;
name|boolean
name|complete
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|targetClient
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|newBuilder
operator|.
name|build
argument_list|()
expr_stmt|;
name|complete
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|ex
parameter_list|)
block|{
comment|//the builder still has unresolved vars
block|}
block|}
if|if
condition|(
operator|!
name|complete
condition|)
block|{
return|return
operator|new
name|WebTargetImpl
argument_list|(
name|newBuilder
argument_list|,
name|getConfiguration
argument_list|()
argument_list|)
return|;
block|}
name|WebClient
name|newClient
init|=
name|WebClient
operator|.
name|fromClient
argument_list|(
name|targetClient
argument_list|)
decl_stmt|;
return|return
operator|new
name|WebTargetImpl
argument_list|(
name|newBuilder
argument_list|,
name|getConfiguration
argument_list|()
argument_list|,
name|newClient
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
name|checkClosed
argument_list|()
expr_stmt|;
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
name|WebTarget
name|property
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|checkClosed
argument_list|()
expr_stmt|;
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
name|WebTarget
name|register
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
name|checkClosed
argument_list|()
expr_stmt|;
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
name|WebTarget
name|register
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|checkClosed
argument_list|()
expr_stmt|;
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
name|WebTarget
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
name|checkClosed
argument_list|()
expr_stmt|;
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
name|WebTarget
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
name|checkClosed
argument_list|()
expr_stmt|;
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
name|WebTarget
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
name|checkClosed
argument_list|()
expr_stmt|;
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
name|WebTarget
name|register
parameter_list|(
name|Object
name|object
parameter_list|,
name|int
name|index
parameter_list|)
block|{
name|checkClosed
argument_list|()
expr_stmt|;
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
name|WebTarget
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
name|checkClosed
argument_list|()
expr_stmt|;
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
name|WebTarget
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
name|checkClosed
argument_list|()
expr_stmt|;
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
specifier|private
name|void
name|checkNullValues
parameter_list|(
name|Object
name|name
parameter_list|,
name|Object
modifier|...
name|values
parameter_list|)
block|{
name|checkNull
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|values
operator|!=
literal|null
operator|&&
name|values
operator|.
name|length
operator|>
literal|1
condition|)
block|{
name|checkNull
argument_list|(
name|values
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|checkNullMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|templatesMap
parameter_list|)
block|{
name|checkNull
argument_list|(
name|templatesMap
argument_list|)
expr_stmt|;
name|checkNull
argument_list|(
name|templatesMap
operator|.
name|keySet
argument_list|()
operator|.
name|toArray
argument_list|()
argument_list|)
expr_stmt|;
name|checkNull
argument_list|(
name|templatesMap
operator|.
name|values
argument_list|()
operator|.
name|toArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

