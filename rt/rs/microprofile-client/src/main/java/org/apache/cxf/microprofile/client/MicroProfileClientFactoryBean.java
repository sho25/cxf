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
name|Comparator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
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
name|Set
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
name|client
operator|.
name|ClientResponseFilter
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
name|util
operator|.
name|ClassHelper
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
name|ClientProxyImpl
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
name|ClientState
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
name|jaxrs
operator|.
name|model
operator|.
name|ProviderInfo
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
name|provider
operator|.
name|jsrjsonp
operator|.
name|JsrJsonpProvider
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
name|proxy
operator|.
name|MicroProfileClientProxyImpl
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

begin_class
specifier|public
class|class
name|MicroProfileClientFactoryBean
extends|extends
name|JAXRSClientFactoryBean
block|{
specifier|private
specifier|final
name|Comparator
argument_list|<
name|ProviderInfo
argument_list|<
name|?
argument_list|>
argument_list|>
name|comparator
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|registeredProviders
decl_stmt|;
specifier|private
name|Configuration
name|configuration
decl_stmt|;
specifier|private
name|ClassLoader
name|proxyLoader
decl_stmt|;
specifier|private
name|boolean
name|inheritHeaders
decl_stmt|;
specifier|public
name|MicroProfileClientFactoryBean
parameter_list|(
name|MicroProfileClientConfigurableImpl
argument_list|<
name|RestClientBuilder
argument_list|>
name|configuration
parameter_list|,
name|String
name|baseUri
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|aClass
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
name|this
operator|.
name|comparator
operator|=
name|MicroProfileClientProviderFactory
operator|.
name|createComparator
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|super
operator|.
name|setAddress
argument_list|(
name|baseUri
argument_list|)
expr_stmt|;
name|super
operator|.
name|setServiceClass
argument_list|(
name|aClass
argument_list|)
expr_stmt|;
name|super
operator|.
name|setProviderComparator
argument_list|(
name|comparator
argument_list|)
expr_stmt|;
name|registeredProviders
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|registeredProviders
operator|.
name|addAll
argument_list|(
name|processProviders
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|configuration
operator|.
name|isDefaultExceptionMapperDisabled
argument_list|()
condition|)
block|{
name|registeredProviders
operator|.
name|add
argument_list|(
operator|new
name|ProviderInfo
argument_list|<>
argument_list|(
operator|new
name|DefaultResponseExceptionMapper
argument_list|()
argument_list|,
name|getBus
argument_list|()
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|registeredProviders
operator|.
name|add
argument_list|(
operator|new
name|ProviderInfo
argument_list|<>
argument_list|(
operator|new
name|JsrJsonpProvider
argument_list|()
argument_list|,
name|getBus
argument_list|()
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|super
operator|.
name|setProviders
argument_list|(
name|registeredProviders
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setClassLoader
parameter_list|(
name|ClassLoader
name|loader
parameter_list|)
block|{
name|super
operator|.
name|setClassLoader
argument_list|(
name|loader
argument_list|)
expr_stmt|;
name|this
operator|.
name|proxyLoader
operator|=
name|loader
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setInheritHeaders
parameter_list|(
name|boolean
name|inheritHeaders
parameter_list|)
block|{
name|super
operator|.
name|setInheritHeaders
argument_list|(
name|inheritHeaders
argument_list|)
expr_stmt|;
name|this
operator|.
name|inheritHeaders
operator|=
name|inheritHeaders
expr_stmt|;
block|}
annotation|@
name|Override
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
name|super
operator|.
name|initClient
argument_list|(
name|client
argument_list|,
name|ep
argument_list|,
name|addHeaders
argument_list|)
expr_stmt|;
name|MicroProfileClientProviderFactory
name|factory
init|=
name|MicroProfileClientProviderFactory
operator|.
name|createInstance
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|comparator
argument_list|)
decl_stmt|;
name|factory
operator|.
name|setUserProviders
argument_list|(
name|registeredProviders
argument_list|)
expr_stmt|;
name|ep
operator|.
name|put
argument_list|(
name|MicroProfileClientProviderFactory
operator|.
name|CLIENT_FACTORY_NAME
argument_list|,
name|factory
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|ClientProxyImpl
name|createClientProxy
parameter_list|(
name|ClassResourceInfo
name|cri
parameter_list|,
name|boolean
name|isRoot
parameter_list|,
name|ClientState
name|actualState
parameter_list|,
name|Object
index|[]
name|varValues
parameter_list|)
block|{
if|if
condition|(
name|actualState
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|MicroProfileClientProxyImpl
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
return|;
block|}
else|else
block|{
return|return
operator|new
name|MicroProfileClientProxyImpl
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
return|;
block|}
block|}
name|Configuration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
specifier|private
name|Set
argument_list|<
name|Object
argument_list|>
name|processProviders
parameter_list|()
block|{
name|Set
argument_list|<
name|Object
argument_list|>
name|providers
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|provider
range|:
name|configuration
operator|.
name|getInstances
argument_list|()
control|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|providerCls
init|=
name|ClassHelper
operator|.
name|getRealClass
argument_list|(
name|bus
argument_list|,
name|provider
argument_list|)
decl_stmt|;
if|if
condition|(
name|provider
operator|instanceof
name|ClientRequestFilter
operator|||
name|provider
operator|instanceof
name|ClientResponseFilter
condition|)
block|{
name|FilterProviderInfo
argument_list|<
name|Object
argument_list|>
name|filter
init|=
operator|new
name|FilterProviderInfo
argument_list|<>
argument_list|(
name|providerCls
argument_list|,
name|providerCls
argument_list|,
name|provider
argument_list|,
name|bus
argument_list|,
name|configuration
operator|.
name|getContracts
argument_list|(
name|providerCls
argument_list|)
argument_list|)
decl_stmt|;
name|providers
operator|.
name|add
argument_list|(
name|filter
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|providers
operator|.
name|add
argument_list|(
name|provider
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|providers
return|;
block|}
block|}
end_class

end_unit

