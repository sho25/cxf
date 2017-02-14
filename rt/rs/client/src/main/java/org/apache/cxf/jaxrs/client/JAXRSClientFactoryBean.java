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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|CookieParam
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
name|HeaderParam
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
name|ext
operator|.
name|ParamConverter
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
name|ParamConverterProvider
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
name|common
operator|.
name|util
operator|.
name|PropertyUtils
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
name|ProxyHelper
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
name|security
operator|.
name|AuthorizationPolicy
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
name|endpoint
operator|.
name|ClientLifeCycleManager
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
name|endpoint
operator|.
name|ConduitSelector
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
name|endpoint
operator|.
name|Endpoint
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
name|endpoint
operator|.
name|UpfrontConduitSelector
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
name|feature
operator|.
name|Feature
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
name|AbstractJAXRSFactoryBean
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
name|JAXRSServiceFactoryBean
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
name|JAXRSServiceImpl
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
name|impl
operator|.
name|MetadataMap
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
name|ClassResourceInfo
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
name|AnnotationUtils
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
name|service
operator|.
name|Service
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
name|service
operator|.
name|factory
operator|.
name|FactoryBeanListener
import|;
end_import

begin_class
specifier|public
class|class
name|JAXRSClientFactoryBean
extends|extends
name|AbstractJAXRSFactoryBean
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
name|JAXRSClientFactoryBean
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|String
name|username
decl_stmt|;
specifier|private
name|String
name|password
decl_stmt|;
specifier|private
name|boolean
name|inheritHeaders
decl_stmt|;
specifier|private
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
decl_stmt|;
specifier|private
name|ClientState
name|initialState
decl_stmt|;
specifier|private
name|boolean
name|threadSafe
decl_stmt|;
specifier|private
name|long
name|timeToKeepState
decl_stmt|;
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|serviceClass
decl_stmt|;
specifier|private
name|ClassLoader
name|proxyLoader
decl_stmt|;
specifier|public
name|JAXRSClientFactoryBean
parameter_list|()
block|{
name|this
argument_list|(
operator|new
name|JAXRSServiceFactoryBean
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JAXRSClientFactoryBean
parameter_list|(
name|JAXRSServiceFactoryBean
name|serviceFactory
parameter_list|)
block|{
name|super
argument_list|(
name|serviceFactory
argument_list|)
expr_stmt|;
name|serviceFactory
operator|.
name|setEnableStaticResolution
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the custom class loader to be used for creating proxies.      * By default the class loader of the given serviceClass will be used.      *      * @param loader      */
specifier|public
name|void
name|setClassLoader
parameter_list|(
name|ClassLoader
name|loader
parameter_list|)
block|{
name|proxyLoader
operator|=
name|loader
expr_stmt|;
block|}
comment|/**      * Indicates if a single proxy or WebClient instance can be reused      * by multiple threads.      *      * @param threadSafe if true then multiple threads can invoke on      *        the same proxy or WebClient instance.      */
specifier|public
name|void
name|setThreadSafe
parameter_list|(
name|boolean
name|threadSafe
parameter_list|)
block|{
name|this
operator|.
name|threadSafe
operator|=
name|threadSafe
expr_stmt|;
block|}
comment|/**      * Sets the time a thread-local client state will be kept.      * This property is ignored for thread-unsafe clients      * @param secondsToKeepState      */
specifier|public
name|void
name|setSecondsToKeepState
parameter_list|(
name|long
name|time
parameter_list|)
block|{
name|this
operator|.
name|timeToKeepState
operator|=
name|time
expr_stmt|;
block|}
comment|/**      * Gets the user name      * @return the name      */
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|username
return|;
block|}
comment|/**      * Sets the username.      * Setting the username and password is a simple way to      * create a Basic Authentication token.      *      * @param username the user name      */
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
block|}
comment|/**      * Gets the password      * @return the password      */
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
comment|/**      * Sets the password.      * Setting the username and password is a simple way to      * create a Basic Authentication token.      *      * @param password the password      */
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
comment|/**      * Indicates if the headers set by a current proxy will be inherited      * when a subresource proxy is created      * vice versa.      *      * @param ih if set to true then the current headers will be inherited      */
specifier|public
name|void
name|setInheritHeaders
parameter_list|(
name|boolean
name|ih
parameter_list|)
block|{
name|inheritHeaders
operator|=
name|ih
expr_stmt|;
block|}
comment|/**      * Sets the resource class      * @param cls the resource class      */
specifier|public
name|void
name|setResourceClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
name|setServiceClass
argument_list|(
name|cls
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the resource class, may be called from a Spring handler      * @param cls the resource class      */
specifier|public
name|void
name|setServiceClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
name|this
operator|.
name|serviceClass
operator|=
name|cls
expr_stmt|;
name|serviceFactory
operator|.
name|setResourceClass
argument_list|(
name|cls
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the service class      * @param cls the service class      */
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getServiceClass
parameter_list|()
block|{
return|return
name|serviceClass
return|;
block|}
comment|/**      * Sets the headers new proxy or WebClient instances will be      * initialized with.      *      * @param map the headers      */
specifier|public
name|void
name|setHeaders
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
parameter_list|)
block|{
name|headers
operator|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
index|[]
name|values
init|=
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|v
range|:
name|values
control|)
block|{
if|if
condition|(
name|v
operator|.
name|length
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|headers
operator|.
name|add
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|v
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Gets the initial headers      * @return the headers      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|getHeaders
parameter_list|()
block|{
return|return
name|headers
return|;
block|}
comment|/**      * Creates a WebClient instance      * @return WebClient instance      */
specifier|public
name|WebClient
name|createWebClient
parameter_list|()
block|{
name|String
name|serviceAddress
init|=
name|getAddress
argument_list|()
decl_stmt|;
name|int
name|queryIndex
init|=
name|serviceAddress
operator|!=
literal|null
condition|?
name|serviceAddress
operator|.
name|lastIndexOf
argument_list|(
literal|'?'
argument_list|)
else|:
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|queryIndex
operator|!=
operator|-
literal|1
condition|)
block|{
name|serviceAddress
operator|=
name|serviceAddress
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|queryIndex
argument_list|)
expr_stmt|;
block|}
name|Service
name|service
init|=
operator|new
name|JAXRSServiceImpl
argument_list|(
name|serviceAddress
argument_list|,
name|getServiceName
argument_list|()
argument_list|)
decl_stmt|;
name|getServiceFactory
argument_list|()
operator|.
name|setService
argument_list|(
name|service
argument_list|)
expr_stmt|;
try|try
block|{
name|Endpoint
name|ep
init|=
name|createEndpoint
argument_list|()
decl_stmt|;
name|this
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|sendEvent
argument_list|(
name|FactoryBeanListener
operator|.
name|Event
operator|.
name|PRE_CLIENT_CREATE
argument_list|,
name|ep
argument_list|)
expr_stmt|;
name|ClientState
name|actualState
init|=
name|getActualState
argument_list|()
decl_stmt|;
name|WebClient
name|client
init|=
name|actualState
operator|==
literal|null
condition|?
operator|new
name|WebClient
argument_list|(
name|getAddress
argument_list|()
argument_list|)
else|:
operator|new
name|WebClient
argument_list|(
name|actualState
argument_list|)
decl_stmt|;
name|initClient
argument_list|(
name|client
argument_list|,
name|ep
argument_list|,
name|actualState
operator|==
literal|null
argument_list|)
expr_stmt|;
name|notifyLifecycleManager
argument_list|(
name|client
argument_list|)
expr_stmt|;
name|this
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|sendEvent
argument_list|(
name|FactoryBeanListener
operator|.
name|Event
operator|.
name|CLIENT_CREATED
argument_list|,
name|client
argument_list|,
name|ep
argument_list|)
expr_stmt|;
return|return
name|client
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|severe
argument_list|(
name|ex
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" : "
operator|+
name|ex
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|notifyLifecycleManager
parameter_list|(
name|Object
name|client
parameter_list|)
block|{
name|ClientLifeCycleManager
name|mgr
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ClientLifeCycleManager
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|mgr
condition|)
block|{
name|mgr
operator|.
name|clientCreated
argument_list|(
operator|new
name|FrontendClientAdapter
argument_list|(
name|WebClient
operator|.
name|getConfig
argument_list|(
name|client
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|ClientState
name|getActualState
parameter_list|()
block|{
if|if
condition|(
name|threadSafe
condition|)
block|{
name|initialState
operator|=
operator|new
name|ThreadLocalClientState
argument_list|(
name|getAddress
argument_list|()
argument_list|,
name|timeToKeepState
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|initialState
operator|!=
literal|null
condition|)
block|{
return|return
name|headers
operator|!=
literal|null
condition|?
name|initialState
operator|.
name|newState
argument_list|(
name|URI
operator|.
name|create
argument_list|(
name|getAddress
argument_list|()
argument_list|)
argument_list|,
name|headers
argument_list|,
literal|null
argument_list|)
else|:
name|initialState
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Creates a proxy      * @param cls the proxy class      * @param varValues optional list of values which will be used to substitute      *        template variables specified in the class-level JAX-RS Path annotations      * @return the proxy      */
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|create
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|,
name|Object
modifier|...
name|varValues
parameter_list|)
block|{
return|return
name|cls
operator|.
name|cast
argument_list|(
name|createWithValues
argument_list|(
name|varValues
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Create a Client instance. Proxies and WebClients are Clients.      * @return the client      */
specifier|public
name|Client
name|create
parameter_list|()
block|{
if|if
condition|(
name|serviceClass
operator|==
name|WebClient
operator|.
name|class
condition|)
block|{
return|return
name|createWebClient
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|createWithValues
argument_list|()
return|;
block|}
block|}
comment|/**      * Create a Client instance. Proxies and WebClients are Clients.      * @param varValues optional list of values which will be used to substitute      *        template variables specified in the class-level JAX-RS Path annotations      *      * @return the client      */
specifier|public
name|Client
name|createWithValues
parameter_list|(
name|Object
modifier|...
name|varValues
parameter_list|)
block|{
name|serviceFactory
operator|.
name|setBus
argument_list|(
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|checkResources
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|ClassResourceInfo
name|cri
init|=
literal|null
decl_stmt|;
try|try
block|{
name|Endpoint
name|ep
init|=
name|createEndpoint
argument_list|()
decl_stmt|;
if|if
condition|(
name|getServiceClass
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ClassResourceInfo
name|info
range|:
name|serviceFactory
operator|.
name|getClassResourceInfo
argument_list|()
control|)
block|{
if|if
condition|(
name|info
operator|.
name|getServiceClass
argument_list|()
operator|.
name|isAssignableFrom
argument_list|(
name|getServiceClass
argument_list|()
argument_list|)
operator|||
name|getServiceClass
argument_list|()
operator|.
name|isAssignableFrom
argument_list|(
name|info
operator|.
name|getServiceClass
argument_list|()
argument_list|)
condition|)
block|{
name|cri
operator|=
name|info
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|cri
operator|==
literal|null
condition|)
block|{
comment|// can not happen in the reality
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Service class "
operator|+
name|getServiceClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" is not recognized"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|cri
operator|=
name|serviceFactory
operator|.
name|getClassResourceInfo
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
name|boolean
name|isRoot
init|=
name|cri
operator|.
name|getURITemplate
argument_list|()
operator|!=
literal|null
decl_stmt|;
name|ClientProxyImpl
name|proxyImpl
init|=
literal|null
decl_stmt|;
name|ClientState
name|actualState
init|=
name|getActualState
argument_list|()
decl_stmt|;
if|if
condition|(
name|actualState
operator|==
literal|null
condition|)
block|{
name|proxyImpl
operator|=
operator|new
name|ClientProxyImpl
argument_list|(
name|URI
operator|.
name|create
argument_list|(
name|getAddress
argument_list|()
argument_list|)
argument_list|,
name|proxyLoader
argument_list|,
name|cri
argument_list|,
name|isRoot
argument_list|,
name|inheritHeaders
argument_list|,
name|varValues
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|proxyImpl
operator|=
operator|new
name|ClientProxyImpl
argument_list|(
name|actualState
argument_list|,
name|proxyLoader
argument_list|,
name|cri
argument_list|,
name|isRoot
argument_list|,
name|inheritHeaders
argument_list|,
name|varValues
argument_list|)
expr_stmt|;
block|}
name|initClient
argument_list|(
name|proxyImpl
argument_list|,
name|ep
argument_list|,
name|actualState
operator|==
literal|null
argument_list|)
expr_stmt|;
name|ClassLoader
name|theLoader
init|=
name|proxyLoader
operator|==
literal|null
condition|?
name|cri
operator|.
name|getServiceClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
else|:
name|proxyLoader
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|ifaces
init|=
operator|new
name|Class
index|[]
block|{
name|Client
operator|.
name|class
block|,
name|InvocationHandlerAware
operator|.
name|class
block|,
name|cri
operator|.
name|getServiceClass
argument_list|()
block|}
decl_stmt|;
name|Client
name|actualClient
init|=
operator|(
name|Client
operator|)
name|ProxyHelper
operator|.
name|getProxy
argument_list|(
name|theLoader
argument_list|,
name|ifaces
argument_list|,
name|proxyImpl
argument_list|)
decl_stmt|;
name|proxyImpl
operator|.
name|setProxyClient
argument_list|(
name|actualClient
argument_list|)
expr_stmt|;
name|notifyLifecycleManager
argument_list|(
name|actualClient
argument_list|)
expr_stmt|;
name|this
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|sendEvent
argument_list|(
name|FactoryBeanListener
operator|.
name|Event
operator|.
name|CLIENT_CREATED
argument_list|,
name|actualClient
argument_list|,
name|ep
argument_list|)
expr_stmt|;
return|return
name|actualClient
return|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|ex
parameter_list|)
block|{
name|String
name|message
init|=
name|ex
operator|.
name|getLocalizedMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|cri
operator|!=
literal|null
condition|)
block|{
name|String
name|expected
init|=
name|cri
operator|.
name|getServiceClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|expected
operator|+
literal|" is not an interface"
operator|)
operator|.
name|equals
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|message
operator|+=
literal|"; make sure CGLIB is on the classpath"
expr_stmt|;
block|}
block|}
name|LOG
operator|.
name|severe
argument_list|(
name|ex
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" : "
operator|+
name|message
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|severe
argument_list|(
name|ex
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" : "
operator|+
name|ex
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|ConduitSelector
name|getConduitSelector
parameter_list|(
name|Endpoint
name|ep
parameter_list|)
block|{
name|ConduitSelector
name|cs
init|=
name|getConduitSelector
argument_list|()
decl_stmt|;
if|if
condition|(
name|cs
operator|==
literal|null
condition|)
block|{
name|cs
operator|=
operator|new
name|UpfrontConduitSelector
argument_list|()
expr_stmt|;
block|}
name|cs
operator|.
name|setEndpoint
argument_list|(
name|ep
argument_list|)
expr_stmt|;
return|return
name|cs
return|;
block|}
specifier|protected
name|void
name|initClient
parameter_list|(
name|AbstractClient
name|client
parameter_list|,
name|Endpoint
name|ep
parameter_list|,
name|boolean
name|addHeaders
parameter_list|)
block|{
if|if
condition|(
name|username
operator|!=
literal|null
condition|)
block|{
name|AuthorizationPolicy
name|authPolicy
init|=
operator|new
name|AuthorizationPolicy
argument_list|()
decl_stmt|;
name|authPolicy
operator|.
name|setUserName
argument_list|(
name|username
argument_list|)
expr_stmt|;
name|authPolicy
operator|.
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|addExtensor
argument_list|(
name|authPolicy
argument_list|)
expr_stmt|;
block|}
name|client
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setConduitSelector
argument_list|(
name|getConduitSelector
argument_list|(
name|ep
argument_list|)
argument_list|)
expr_stmt|;
name|client
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setBus
argument_list|(
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|addAll
argument_list|(
name|getOutInterceptors
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|addAll
argument_list|(
name|ep
operator|.
name|getOutInterceptors
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|addAll
argument_list|(
name|getInInterceptors
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|addAll
argument_list|(
name|ep
operator|.
name|getInInterceptors
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getInFaultInterceptors
argument_list|()
operator|.
name|addAll
argument_list|(
name|getInFaultInterceptors
argument_list|()
argument_list|)
expr_stmt|;
name|applyFeatures
argument_list|(
name|client
argument_list|)
expr_stmt|;
if|if
condition|(
name|headers
operator|!=
literal|null
operator|&&
name|addHeaders
condition|)
block|{
name|client
operator|.
name|headers
argument_list|(
name|headers
argument_list|)
expr_stmt|;
block|}
name|ClientProviderFactory
name|factory
init|=
name|ClientProviderFactory
operator|.
name|createInstance
argument_list|(
name|getBus
argument_list|()
argument_list|)
decl_stmt|;
name|setupFactory
argument_list|(
name|factory
argument_list|,
name|ep
argument_list|)
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|theProperties
init|=
name|super
operator|.
name|getProperties
argument_list|()
decl_stmt|;
specifier|final
name|boolean
name|encodeClientParameters
init|=
name|PropertyUtils
operator|.
name|isTrue
argument_list|(
name|theProperties
argument_list|,
literal|"url.encode.client.parameters"
argument_list|)
decl_stmt|;
if|if
condition|(
name|encodeClientParameters
condition|)
block|{
specifier|final
name|String
name|encodeClientParametersList
init|=
name|theProperties
operator|==
literal|null
condition|?
literal|null
else|:
operator|(
name|String
operator|)
name|getProperties
argument_list|()
operator|.
name|get
argument_list|(
literal|"url.encode.client.parameters.list"
argument_list|)
decl_stmt|;
name|factory
operator|.
name|registerUserProvider
argument_list|(
operator|new
name|ParamConverterProvider
argument_list|()
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|ParamConverter
argument_list|<
name|T
argument_list|>
name|getConverter
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|,
name|Type
name|t
parameter_list|,
name|Annotation
index|[]
name|anns
parameter_list|)
block|{
if|if
condition|(
name|cls
operator|==
name|String
operator|.
name|class
operator|&&
name|AnnotationUtils
operator|.
name|getAnnotation
argument_list|(
name|anns
argument_list|,
name|HeaderParam
operator|.
name|class
argument_list|)
operator|==
literal|null
operator|&&
name|AnnotationUtils
operator|.
name|getAnnotation
argument_list|(
name|anns
argument_list|,
name|CookieParam
operator|.
name|class
argument_list|)
operator|==
literal|null
condition|)
block|{
return|return
operator|(
name|ParamConverter
argument_list|<
name|T
argument_list|>
operator|)
operator|new
name|UrlEncodingParamConverter
argument_list|(
name|encodeClientParametersList
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|applyFeatures
parameter_list|(
name|AbstractClient
name|client
parameter_list|)
block|{
if|if
condition|(
name|getFeatures
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Feature
name|feature
range|:
name|getFeatures
argument_list|()
control|)
block|{
name|feature
operator|.
name|initialize
argument_list|(
name|client
operator|.
name|getConfiguration
argument_list|()
argument_list|,
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Sets the initial client state, can be a thread-safe state.      * @param initialState the state      */
specifier|public
name|void
name|setInitialState
parameter_list|(
name|ClientState
name|initialState
parameter_list|)
block|{
name|this
operator|.
name|initialState
operator|=
name|initialState
expr_stmt|;
block|}
block|}
end_class

end_unit

