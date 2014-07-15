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
name|frontend
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|BusException
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
name|Client
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
name|EndpointException
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
name|service
operator|.
name|factory
operator|.
name|FactoryBeanListener
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
name|ServiceConstructionException
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
name|ConduitInitiator
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
name|ConduitInitiatorManager
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
name|DestinationFactoryManager
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
name|wsdl
operator|.
name|service
operator|.
name|factory
operator|.
name|ReflectionServiceFactoryBean
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
name|wsdl11
operator|.
name|WSDLEndpointFactory
import|;
end_import

begin_class
specifier|public
class|class
name|ClientFactoryBean
extends|extends
name|AbstractWSDLBasedEndpointFactory
block|{
specifier|public
name|ClientFactoryBean
parameter_list|()
block|{
name|this
argument_list|(
operator|new
name|ReflectionServiceFactoryBean
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ClientFactoryBean
parameter_list|(
name|ReflectionServiceFactoryBean
name|factory
parameter_list|)
block|{
name|super
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|detectTransportIdFromAddress
parameter_list|(
name|String
name|ad
parameter_list|)
block|{
name|ConduitInitiatorManager
name|cim
init|=
name|getBus
argument_list|()
operator|.
name|getExtension
argument_list|(
name|ConduitInitiatorManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|ConduitInitiator
name|ci
init|=
name|cim
operator|.
name|getConduitInitiatorForUri
argument_list|(
name|getAddress
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ci
operator|!=
literal|null
condition|)
block|{
return|return
name|ci
operator|.
name|getTransportIds
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|protected
name|WSDLEndpointFactory
name|getWSDLEndpointFactory
parameter_list|()
block|{
if|if
condition|(
name|destinationFactory
operator|instanceof
name|WSDLEndpointFactory
condition|)
block|{
return|return
operator|(
name|WSDLEndpointFactory
operator|)
name|destinationFactory
return|;
block|}
try|try
block|{
name|Object
name|o
init|=
name|getBus
argument_list|()
operator|.
name|getExtension
argument_list|(
name|ConduitInitiatorManager
operator|.
name|class
argument_list|)
operator|.
name|getConduitInitiator
argument_list|(
name|transportId
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|WSDLEndpointFactory
condition|)
block|{
return|return
operator|(
name|WSDLEndpointFactory
operator|)
name|o
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//ignore
block|}
if|if
condition|(
name|destinationFactory
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|destinationFactory
operator|=
name|getBus
argument_list|()
operator|.
name|getExtension
argument_list|(
name|DestinationFactoryManager
operator|.
name|class
argument_list|)
operator|.
name|getDestinationFactory
argument_list|(
name|transportId
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//ignore
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|Client
name|create
parameter_list|()
block|{
name|getServiceFactory
argument_list|()
operator|.
name|reset
argument_list|()
expr_stmt|;
if|if
condition|(
name|getServiceFactory
argument_list|()
operator|.
name|getProperties
argument_list|()
operator|==
literal|null
condition|)
block|{
name|getServiceFactory
argument_list|()
operator|.
name|setProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|properties
operator|!=
literal|null
condition|)
block|{
name|getServiceFactory
argument_list|()
operator|.
name|getProperties
argument_list|()
operator|.
name|putAll
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
name|Client
name|client
init|=
literal|null
decl_stmt|;
name|Endpoint
name|ep
init|=
literal|null
decl_stmt|;
try|try
block|{
name|ep
operator|=
name|createEndpoint
argument_list|()
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
name|PRE_CLIENT_CREATE
argument_list|,
name|ep
argument_list|)
expr_stmt|;
name|applyProperties
argument_list|(
name|ep
argument_list|)
expr_stmt|;
name|client
operator|=
name|createClient
argument_list|(
name|ep
argument_list|)
expr_stmt|;
name|initializeAnnotationInterceptors
argument_list|(
name|ep
argument_list|,
name|getServiceClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|EndpointException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ServiceConstructionException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|BusException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ServiceConstructionException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|applyFeatures
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
specifier|protected
name|Client
name|createClient
parameter_list|(
name|Endpoint
name|ep
parameter_list|)
block|{
return|return
operator|new
name|ClientImpl
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|ep
argument_list|,
name|getConduitSelector
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|void
name|applyFeatures
parameter_list|(
name|Client
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
argument_list|,
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|void
name|applyProperties
parameter_list|(
name|Endpoint
name|ep
parameter_list|)
block|{
comment|//Apply the AuthorizationPolicy to the endpointInfo
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
name|this
operator|.
name|getProperties
argument_list|()
decl_stmt|;
if|if
condition|(
name|props
operator|!=
literal|null
operator|&&
name|props
operator|.
name|get
argument_list|(
name|AuthorizationPolicy
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|AuthorizationPolicy
name|ap
init|=
operator|(
name|AuthorizationPolicy
operator|)
name|props
operator|.
name|get
argument_list|(
name|AuthorizationPolicy
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|addExtensor
argument_list|(
name|ap
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

