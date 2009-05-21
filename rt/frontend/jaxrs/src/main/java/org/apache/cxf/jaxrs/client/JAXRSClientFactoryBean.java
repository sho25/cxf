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
name|Map
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
name|WebApplicationException
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
name|service
operator|.
name|Service
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
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|username
return|;
block|}
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
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
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
specifier|public
name|void
name|setResourceClass
parameter_list|(
name|Class
name|cls
parameter_list|)
block|{
name|setServiceClass
argument_list|(
name|cls
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setServiceClass
parameter_list|(
name|Class
name|cls
parameter_list|)
block|{
name|serviceFactory
operator|.
name|setResourceClass
argument_list|(
name|cls
argument_list|)
expr_stmt|;
block|}
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
specifier|public
name|WebClient
name|createWebClient
parameter_list|()
block|{
name|Service
name|service
init|=
operator|new
name|JAXRSServiceImpl
argument_list|(
name|getAddress
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
name|WebClient
name|client
init|=
operator|new
name|WebClient
argument_list|(
name|getAddress
argument_list|()
argument_list|)
decl_stmt|;
name|initClient
argument_list|(
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
throw|throw
operator|new
name|WebApplicationException
argument_list|()
throw|;
block|}
block|}
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
specifier|public
name|Client
name|create
parameter_list|()
block|{
return|return
name|createWithValues
argument_list|()
return|;
block|}
specifier|public
name|Client
name|createWithValues
parameter_list|(
name|Object
modifier|...
name|varValues
parameter_list|)
block|{
name|checkResources
argument_list|()
expr_stmt|;
try|try
block|{
name|Endpoint
name|ep
init|=
name|createEndpoint
argument_list|()
decl_stmt|;
name|URI
name|baseURI
init|=
name|URI
operator|.
name|create
argument_list|(
name|getAddress
argument_list|()
argument_list|)
decl_stmt|;
name|ClassResourceInfo
name|cri
init|=
name|serviceFactory
operator|.
name|getClassResourceInfo
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
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
operator|new
name|ClientProxyImpl
argument_list|(
name|baseURI
argument_list|,
name|baseURI
argument_list|,
name|cri
argument_list|,
name|isRoot
argument_list|,
name|inheritHeaders
argument_list|,
name|varValues
argument_list|)
decl_stmt|;
name|initClient
argument_list|(
name|proxyImpl
argument_list|,
name|ep
argument_list|)
expr_stmt|;
return|return
operator|(
name|Client
operator|)
name|ProxyHelper
operator|.
name|getProxy
argument_list|(
name|cri
operator|.
name|getServiceClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|,
operator|new
name|Class
index|[]
block|{
name|cri
operator|.
name|getServiceClass
argument_list|()
block|,
name|Client
operator|.
name|class
block|,
name|InvocationHandlerAware
operator|.
name|class
block|}
argument_list|,
name|proxyImpl
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
name|WebApplicationException
argument_list|()
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
name|cs
operator|=
name|cs
operator|==
literal|null
condition|?
operator|new
name|UpfrontConduitSelector
argument_list|()
else|:
name|cs
expr_stmt|;
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
name|setBus
argument_list|(
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|setOutInterceptors
argument_list|(
name|getOutInterceptors
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|setInInterceptors
argument_list|(
name|getInInterceptors
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|headers
operator|!=
literal|null
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
name|setupFactory
argument_list|(
name|ep
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

