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
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|binding
operator|.
name|BindingFactoryManager
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
name|binding
operator|.
name|soap
operator|.
name|SoapBindingConfiguration
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
name|binding
operator|.
name|soap
operator|.
name|jms
operator|.
name|interceptor
operator|.
name|SoapJMSConstants
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
name|binding
operator|.
name|soap
operator|.
name|model
operator|.
name|SoapBindingInfo
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
name|i18n
operator|.
name|Message
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
name|endpoint
operator|.
name|AbstractEndpointFactory
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
name|endpoint
operator|.
name|EndpointImpl
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
name|AbstractServiceConfiguration
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
name|service
operator|.
name|model
operator|.
name|BindingInfo
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
name|model
operator|.
name|BindingOperationInfo
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
name|model
operator|.
name|EndpointInfo
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
name|model
operator|.
name|ServiceModelUtil
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
name|DestinationFactory
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
name|transport
operator|.
name|local
operator|.
name|LocalTransportFactory
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
specifier|abstract
class|class
name|AbstractWSDLBasedEndpointFactory
extends|extends
name|AbstractEndpointFactory
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
name|AbstractWSDLBasedEndpointFactory
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Class
name|serviceClass
decl_stmt|;
specifier|private
name|ReflectionServiceFactoryBean
name|serviceFactory
decl_stmt|;
specifier|protected
name|AbstractWSDLBasedEndpointFactory
parameter_list|(
name|ReflectionServiceFactoryBean
name|sbean
parameter_list|)
block|{
name|serviceFactory
operator|=
name|sbean
expr_stmt|;
name|serviceClass
operator|=
name|sbean
operator|.
name|getServiceClass
argument_list|()
expr_stmt|;
name|serviceName
operator|=
name|sbean
operator|.
name|getServiceQName
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|endpointName
operator|=
name|sbean
operator|.
name|getEndpointName
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sbean
operator|.
name|getServiceConfigurations
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|SoapBindingServiceConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|AbstractWSDLBasedEndpointFactory
parameter_list|()
block|{     }
specifier|private
class|class
name|SoapBindingServiceConfiguration
extends|extends
name|AbstractServiceConfiguration
block|{
specifier|public
name|String
name|getStyle
parameter_list|()
block|{
if|if
condition|(
name|getBindingConfig
argument_list|()
operator|instanceof
name|SoapBindingConfiguration
operator|&&
operator|(
operator|(
name|SoapBindingConfiguration
operator|)
name|getBindingConfig
argument_list|()
operator|)
operator|.
name|isSetStyle
argument_list|()
condition|)
block|{
return|return
operator|(
operator|(
name|SoapBindingConfiguration
operator|)
name|getBindingConfig
argument_list|()
operator|)
operator|.
name|getStyle
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|Boolean
name|isWrapped
parameter_list|()
block|{
if|if
condition|(
name|getBindingConfig
argument_list|()
operator|instanceof
name|SoapBindingConfiguration
operator|&&
operator|(
operator|(
name|SoapBindingConfiguration
operator|)
name|getBindingConfig
argument_list|()
operator|)
operator|.
name|isSetStyle
argument_list|()
operator|&&
literal|"rpc"
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|SoapBindingConfiguration
operator|)
name|getBindingConfig
argument_list|()
operator|)
operator|.
name|getStyle
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|Boolean
operator|.
name|FALSE
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|()
throws|throws
name|BusException
throws|,
name|EndpointException
block|{
name|serviceFactory
operator|.
name|setFeatures
argument_list|(
name|getFeatures
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|serviceName
operator|!=
literal|null
condition|)
block|{
name|serviceFactory
operator|.
name|setServiceName
argument_list|(
name|serviceName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endpointName
operator|!=
literal|null
condition|)
block|{
name|serviceFactory
operator|.
name|setEndpointName
argument_list|(
name|endpointName
argument_list|)
expr_stmt|;
block|}
name|Service
name|service
init|=
name|serviceFactory
operator|.
name|getService
argument_list|()
decl_stmt|;
if|if
condition|(
name|service
operator|==
literal|null
condition|)
block|{
name|initializeServiceFactory
argument_list|()
expr_stmt|;
name|service
operator|=
name|serviceFactory
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|endpointName
operator|==
literal|null
condition|)
block|{
name|endpointName
operator|=
name|serviceFactory
operator|.
name|getEndpointName
argument_list|()
expr_stmt|;
block|}
name|EndpointInfo
name|ei
init|=
name|service
operator|.
name|getEndpointInfo
argument_list|(
name|endpointName
argument_list|)
decl_stmt|;
if|if
condition|(
name|ei
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|transportId
operator|!=
literal|null
operator|&&
operator|!
name|ei
operator|.
name|getTransportId
argument_list|()
operator|.
name|equals
argument_list|(
name|transportId
argument_list|)
condition|)
block|{
name|ei
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|BindingFactoryManager
name|bfm
init|=
name|getBus
argument_list|()
operator|.
name|getExtension
argument_list|(
name|BindingFactoryManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|bindingFactory
operator|=
name|bfm
operator|.
name|getBindingFactory
argument_list|(
name|ei
operator|.
name|getBinding
argument_list|()
operator|.
name|getBindingId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|ei
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|getAddress
argument_list|()
operator|==
literal|null
condition|)
block|{
name|ei
operator|=
name|ServiceModelUtil
operator|.
name|findBestEndpointInfo
argument_list|(
name|serviceFactory
operator|.
name|getInterfaceName
argument_list|()
argument_list|,
name|service
operator|.
name|getServiceInfos
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ei
operator|==
literal|null
operator|&&
operator|!
name|serviceFactory
operator|.
name|isPopulateFromClass
argument_list|()
condition|)
block|{
name|ei
operator|=
name|ServiceModelUtil
operator|.
name|findBestEndpointInfo
argument_list|(
name|serviceFactory
operator|.
name|getInterfaceName
argument_list|()
argument_list|,
name|service
operator|.
name|getServiceInfos
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|ei
operator|!=
literal|null
operator|&&
name|transportId
operator|!=
literal|null
operator|&&
operator|!
name|ei
operator|.
name|getTransportId
argument_list|()
operator|.
name|equals
argument_list|(
name|transportId
argument_list|)
condition|)
block|{
name|ei
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|ei
operator|!=
literal|null
condition|)
block|{
name|BindingFactoryManager
name|bfm
init|=
name|getBus
argument_list|()
operator|.
name|getExtension
argument_list|(
name|BindingFactoryManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|bindingFactory
operator|=
name|bfm
operator|.
name|getBindingFactory
argument_list|(
name|ei
operator|.
name|getBinding
argument_list|()
operator|.
name|getBindingId
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ei
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Could not find endpoint/port for "
operator|+
name|endpointName
operator|+
literal|" in wsdl. Creating default."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Could not find endpoint/port for "
operator|+
name|endpointName
operator|+
literal|" in wsdl. Using "
operator|+
name|ei
operator|.
name|getName
argument_list|()
operator|+
literal|"."
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|ei
operator|==
literal|null
condition|)
block|{
name|ei
operator|=
name|createEndpointInfo
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|getAddress
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ei
operator|.
name|setAddress
argument_list|(
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|ei
operator|.
name|getAddress
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"camel"
argument_list|)
operator|||
name|ei
operator|.
name|getAddress
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"local"
argument_list|)
condition|)
block|{
name|modifyTransportIdPerAddress
argument_list|(
name|ei
argument_list|)
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|getAddress
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ei
operator|.
name|setAddress
argument_list|(
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endpointReference
operator|!=
literal|null
condition|)
block|{
name|ei
operator|.
name|setAddress
argument_list|(
name|endpointReference
argument_list|)
expr_stmt|;
block|}
name|Endpoint
name|ep
init|=
name|service
operator|.
name|getEndpoints
argument_list|()
operator|.
name|get
argument_list|(
name|ei
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ep
operator|==
literal|null
condition|)
block|{
name|ep
operator|=
name|serviceFactory
operator|.
name|createEndpoint
argument_list|(
name|ei
argument_list|)
expr_stmt|;
operator|(
operator|(
name|EndpointImpl
operator|)
name|ep
operator|)
operator|.
name|initializeActiveFeatures
argument_list|(
name|getFeatures
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|serviceFactory
operator|.
name|setEndpointName
argument_list|(
name|ei
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|ep
operator|.
name|getActiveFeatures
argument_list|()
operator|==
literal|null
condition|)
block|{
operator|(
operator|(
name|EndpointImpl
operator|)
name|ep
operator|)
operator|.
name|initializeActiveFeatures
argument_list|(
name|getFeatures
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|properties
operator|!=
literal|null
condition|)
block|{
name|ep
operator|.
name|putAll
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
name|service
operator|.
name|getEndpoints
argument_list|()
operator|.
name|put
argument_list|(
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|ep
argument_list|)
expr_stmt|;
if|if
condition|(
name|getInInterceptors
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ep
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
name|ep
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|WSDLGetInterceptor
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getOutInterceptors
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ep
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
block|}
if|if
condition|(
name|getInFaultInterceptors
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ep
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
block|}
if|if
condition|(
name|getOutFaultInterceptors
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ep
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|addAll
argument_list|(
name|getOutFaultInterceptors
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|serviceFactory
operator|.
name|sendEvent
argument_list|(
name|FactoryBeanListener
operator|.
name|Event
operator|.
name|ENDPOINT_SELECTED
argument_list|,
name|ei
argument_list|,
name|ep
argument_list|,
name|serviceFactory
operator|.
name|getServiceClass
argument_list|()
argument_list|,
name|getServiceClass
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|ep
return|;
block|}
specifier|private
name|void
name|modifyTransportIdPerAddress
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|)
block|{
comment|//get chance to set transportId according to the the publish address prefix
comment|//this is useful for local& camel transport
if|if
condition|(
name|transportId
operator|==
literal|null
operator|&&
name|getAddress
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|DestinationFactory
name|df
init|=
name|getDestinationFactory
argument_list|()
decl_stmt|;
if|if
condition|(
name|df
operator|==
literal|null
condition|)
block|{
name|DestinationFactoryManager
name|dfm
init|=
name|getBus
argument_list|()
operator|.
name|getExtension
argument_list|(
name|DestinationFactoryManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|df
operator|=
name|dfm
operator|.
name|getDestinationFactoryForUri
argument_list|(
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|df
operator|!=
literal|null
condition|)
block|{
name|transportId
operator|=
name|df
operator|.
name|getTransportIds
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// check conduits (the address could be supported on
comment|// client only)
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
name|transportId
operator|=
name|ci
operator|.
name|getTransportIds
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|transportId
operator|!=
literal|null
condition|)
block|{
name|ei
operator|.
name|setTransportId
argument_list|(
name|transportId
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|initializeServiceFactory
parameter_list|()
block|{
name|Class
name|cls
init|=
name|getServiceClass
argument_list|()
decl_stmt|;
name|serviceFactory
operator|.
name|setServiceClass
argument_list|(
name|cls
argument_list|)
expr_stmt|;
name|serviceFactory
operator|.
name|setBus
argument_list|(
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|dataBinding
operator|!=
literal|null
condition|)
block|{
name|serviceFactory
operator|.
name|setDataBinding
argument_list|(
name|dataBinding
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
specifier|abstract
name|String
name|detectTransportIdFromAddress
parameter_list|(
name|String
name|ad
parameter_list|)
function_decl|;
specifier|protected
specifier|abstract
name|WSDLEndpointFactory
name|getWSDLEndpointFactory
parameter_list|()
function_decl|;
specifier|protected
name|EndpointInfo
name|createEndpointInfo
parameter_list|()
throws|throws
name|BusException
block|{
comment|// Get the Service from the ServiceFactory if specified
name|Service
name|service
init|=
name|serviceFactory
operator|.
name|getService
argument_list|()
decl_stmt|;
comment|// setup the transport ID for the soap over jms if there is only address information
if|if
condition|(
name|transportId
operator|==
literal|null
operator|&&
name|getAddress
argument_list|()
operator|!=
literal|null
operator|&&
name|getAddress
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"jms:"
argument_list|)
operator|&&
operator|!
literal|"jms://"
operator|.
name|equals
argument_list|(
name|getAddress
argument_list|()
argument_list|)
condition|)
block|{
comment|// Set the transportId to be soap over jms transport
name|transportId
operator|=
name|SoapJMSConstants
operator|.
name|SOAP_JMS_SPECIFICIATION_TRANSPORTID
expr_stmt|;
block|}
comment|// SOAP nonsense
name|BindingInfo
name|bindingInfo
init|=
name|createBindingInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|bindingInfo
operator|instanceof
name|SoapBindingInfo
operator|&&
operator|(
operator|(
operator|(
name|SoapBindingInfo
operator|)
name|bindingInfo
operator|)
operator|.
name|getTransportURI
argument_list|()
operator|==
literal|null
operator|||
name|LocalTransportFactory
operator|.
name|TRANSPORT_ID
operator|.
name|equals
argument_list|(
name|transportId
argument_list|)
operator|)
condition|)
block|{
operator|(
operator|(
name|SoapBindingInfo
operator|)
name|bindingInfo
operator|)
operator|.
name|setTransportURI
argument_list|(
name|transportId
argument_list|)
expr_stmt|;
name|transportId
operator|=
literal|"http://schemas.xmlsoap.org/wsdl/soap/"
expr_stmt|;
block|}
if|if
condition|(
name|transportId
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|bindingInfo
operator|instanceof
name|SoapBindingInfo
condition|)
block|{
name|transportId
operator|=
operator|(
operator|(
name|SoapBindingInfo
operator|)
name|bindingInfo
operator|)
operator|.
name|getTransportURI
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|transportId
operator|==
literal|null
operator|&&
name|getAddress
argument_list|()
operator|!=
literal|null
operator|&&
name|getAddress
argument_list|()
operator|.
name|contains
argument_list|(
literal|"://"
argument_list|)
condition|)
block|{
name|transportId
operator|=
name|detectTransportIdFromAddress
argument_list|(
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|transportId
operator|==
literal|null
condition|)
block|{
name|transportId
operator|=
literal|"http://schemas.xmlsoap.org/wsdl/http/"
expr_stmt|;
block|}
block|}
name|service
operator|.
name|getServiceInfos
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|addBinding
argument_list|(
name|bindingInfo
argument_list|)
expr_stmt|;
name|setTransportId
argument_list|(
name|transportId
argument_list|)
expr_stmt|;
name|WSDLEndpointFactory
name|wsdlEndpointFactory
init|=
name|getWSDLEndpointFactory
argument_list|()
decl_stmt|;
name|EndpointInfo
name|ei
decl_stmt|;
if|if
condition|(
name|wsdlEndpointFactory
operator|!=
literal|null
condition|)
block|{
name|ei
operator|=
name|wsdlEndpointFactory
operator|.
name|createEndpointInfo
argument_list|(
name|service
operator|.
name|getServiceInfos
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|bindingInfo
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|ei
operator|.
name|setTransportId
argument_list|(
name|transportId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ei
operator|=
operator|new
name|EndpointInfo
argument_list|(
name|service
operator|.
name|getServiceInfos
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|transportId
argument_list|)
expr_stmt|;
block|}
name|int
name|count
init|=
literal|1
decl_stmt|;
while|while
condition|(
name|service
operator|.
name|getEndpointInfo
argument_list|(
name|endpointName
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|endpointName
operator|=
operator|new
name|QName
argument_list|(
name|endpointName
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|endpointName
operator|.
name|getLocalPart
argument_list|()
operator|+
name|count
argument_list|)
expr_stmt|;
name|count
operator|++
expr_stmt|;
block|}
name|ei
operator|.
name|setName
argument_list|(
name|endpointName
argument_list|)
expr_stmt|;
name|ei
operator|.
name|setAddress
argument_list|(
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|ei
operator|.
name|setBinding
argument_list|(
name|bindingInfo
argument_list|)
expr_stmt|;
if|if
condition|(
name|publishedEndpointUrl
operator|!=
literal|null
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|publishedEndpointUrl
argument_list|)
condition|)
block|{
name|ei
operator|.
name|setProperty
argument_list|(
literal|"publishedEndpointUrl"
argument_list|,
name|publishedEndpointUrl
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|wsdlEndpointFactory
operator|!=
literal|null
condition|)
block|{
name|wsdlEndpointFactory
operator|.
name|createPortExtensors
argument_list|(
name|ei
argument_list|,
name|service
argument_list|)
expr_stmt|;
block|}
name|service
operator|.
name|getServiceInfos
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|addEndpoint
argument_list|(
name|ei
argument_list|)
expr_stmt|;
name|serviceFactory
operator|.
name|sendEvent
argument_list|(
name|FactoryBeanListener
operator|.
name|Event
operator|.
name|ENDPOINTINFO_CREATED
argument_list|,
name|ei
argument_list|)
expr_stmt|;
return|return
name|ei
return|;
block|}
specifier|protected
name|SoapBindingConfiguration
name|createSoapBindingConfig
parameter_list|()
block|{
return|return
operator|new
name|SoapBindingConfiguration
argument_list|()
return|;
block|}
specifier|protected
name|BindingInfo
name|createBindingInfo
parameter_list|()
block|{
name|BindingFactoryManager
name|mgr
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|BindingFactoryManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|binding
init|=
name|bindingId
decl_stmt|;
if|if
condition|(
name|binding
operator|==
literal|null
operator|&&
name|bindingConfig
operator|!=
literal|null
condition|)
block|{
name|binding
operator|=
name|bindingConfig
operator|.
name|getBindingId
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|binding
operator|==
literal|null
condition|)
block|{
comment|// default to soap binding
name|binding
operator|=
literal|"http://schemas.xmlsoap.org/soap/"
expr_stmt|;
block|}
try|try
block|{
if|if
condition|(
name|binding
operator|.
name|contains
argument_list|(
literal|"/soap"
argument_list|)
condition|)
block|{
if|if
condition|(
name|bindingConfig
operator|==
literal|null
condition|)
block|{
name|bindingConfig
operator|=
name|createSoapBindingConfig
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|bindingConfig
operator|instanceof
name|SoapBindingConfiguration
operator|&&
operator|!
operator|(
operator|(
name|SoapBindingConfiguration
operator|)
name|bindingConfig
operator|)
operator|.
name|isSetStyle
argument_list|()
condition|)
block|{
operator|(
operator|(
name|SoapBindingConfiguration
operator|)
name|bindingConfig
operator|)
operator|.
name|setStyle
argument_list|(
name|serviceFactory
operator|.
name|getStyle
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|bindingFactory
operator|=
name|mgr
operator|.
name|getBindingFactory
argument_list|(
name|binding
argument_list|)
expr_stmt|;
name|BindingInfo
name|inf
init|=
name|bindingFactory
operator|.
name|createBindingInfo
argument_list|(
name|serviceFactory
operator|.
name|getService
argument_list|()
argument_list|,
name|binding
argument_list|,
name|bindingConfig
argument_list|)
decl_stmt|;
for|for
control|(
name|BindingOperationInfo
name|boi
range|:
name|inf
operator|.
name|getOperations
argument_list|()
control|)
block|{
name|serviceFactory
operator|.
name|updateBindingOperation
argument_list|(
name|boi
argument_list|)
expr_stmt|;
name|serviceFactory
operator|.
name|sendEvent
argument_list|(
name|FactoryBeanListener
operator|.
name|Event
operator|.
name|BINDING_OPERATION_CREATED
argument_list|,
name|inf
argument_list|,
name|boi
argument_list|)
expr_stmt|;
block|}
name|serviceFactory
operator|.
name|sendEvent
argument_list|(
name|FactoryBeanListener
operator|.
name|Event
operator|.
name|BINDING_CREATED
argument_list|,
name|inf
argument_list|)
expr_stmt|;
return|return
name|inf
return|;
block|}
catch|catch
parameter_list|(
name|BusException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ServiceConstructionException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"COULD.NOT.RESOLVE.BINDING"
argument_list|,
name|LOG
argument_list|,
name|bindingId
argument_list|)
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
name|Class
name|getServiceClass
parameter_list|()
block|{
return|return
name|serviceClass
return|;
block|}
comment|/**      * Specifies the class implementing the service.      *      * @param serviceClass the service's implementaiton class      */
specifier|public
name|void
name|setServiceClass
parameter_list|(
name|Class
name|serviceClass
parameter_list|)
block|{
name|this
operator|.
name|serviceClass
operator|=
name|serviceClass
expr_stmt|;
block|}
specifier|public
name|ReflectionServiceFactoryBean
name|getServiceFactory
parameter_list|()
block|{
return|return
name|serviceFactory
return|;
block|}
specifier|public
name|void
name|setServiceFactory
parameter_list|(
name|ReflectionServiceFactoryBean
name|serviceFactory
parameter_list|)
block|{
name|this
operator|.
name|serviceFactory
operator|=
name|serviceFactory
expr_stmt|;
block|}
specifier|public
name|String
name|getWsdlURL
parameter_list|()
block|{
return|return
name|getServiceFactory
argument_list|()
operator|.
name|getWsdlURL
argument_list|()
return|;
block|}
specifier|public
name|void
name|setWsdlURL
parameter_list|(
name|String
name|wsdlURL
parameter_list|)
block|{
name|getServiceFactory
argument_list|()
operator|.
name|setWsdlURL
argument_list|(
name|wsdlURL
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

