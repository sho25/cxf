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
name|binding
operator|.
name|soap
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|Iterator
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
name|javax
operator|.
name|annotation
operator|.
name|PostConstruct
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Port
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|WSDLException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|extensions
operator|.
name|ExtensionRegistry
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|factory
operator|.
name|WSDLFactory
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
name|Bus
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
name|binding
operator|.
name|soap
operator|.
name|wsdl11
operator|.
name|SoapAddressPlugin
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
name|ServiceInfo
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
name|tools
operator|.
name|common
operator|.
name|extensions
operator|.
name|soap
operator|.
name|SoapAddress
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
name|tools
operator|.
name|util
operator|.
name|SOAPBindingUtil
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
name|AbstractTransportFactory
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
name|Conduit
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
name|Destination
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
name|ws
operator|.
name|addressing
operator|.
name|EndpointReferenceType
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
name|SoapTransportFactory
extends|extends
name|AbstractTransportFactory
implements|implements
name|DestinationFactory
implements|,
name|WSDLEndpointFactory
implements|,
name|ConduitInitiator
block|{
specifier|public
specifier|static
specifier|final
name|String
name|SOAP_11_HTTP_BINDING
init|=
literal|"http://schemas.xmlsoap.org/soap/http"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SOAP_12_HTTP_BINDING
init|=
literal|"http://www.w3.org/2003/05/soap/bindings/HTTP/"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TRANSPORT_ID
init|=
literal|"http://schemas.xmlsoap.org/soap/"
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|String
argument_list|>
name|activationNamespaces
decl_stmt|;
specifier|public
name|SoapTransportFactory
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|public
name|Destination
name|getDestination
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|)
throws|throws
name|IOException
block|{
name|SoapBindingInfo
name|binding
init|=
operator|(
name|SoapBindingInfo
operator|)
name|ei
operator|.
name|getBinding
argument_list|()
decl_stmt|;
name|DestinationFactory
name|destinationFactory
decl_stmt|;
try|try
block|{
name|destinationFactory
operator|=
name|bus
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
name|binding
operator|.
name|getTransportURI
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|destinationFactory
operator|.
name|getDestination
argument_list|(
name|ei
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|BusException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Could not find destination factory for transport "
operator|+
name|binding
operator|.
name|getTransportURI
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|createPortExtensors
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|,
name|Service
name|service
parameter_list|)
block|{
if|if
condition|(
name|ei
operator|.
name|getBinding
argument_list|()
operator|instanceof
name|SoapBindingInfo
condition|)
block|{
name|SoapBindingInfo
name|bi
init|=
operator|(
name|SoapBindingInfo
operator|)
name|ei
operator|.
name|getBinding
argument_list|()
decl_stmt|;
name|createSoapExtensors
argument_list|(
name|ei
argument_list|,
name|bi
argument_list|,
name|bi
operator|.
name|getSoapVersion
argument_list|()
operator|instanceof
name|Soap12
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|createSoapExtensors
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|,
name|SoapBindingInfo
name|bi
parameter_list|,
name|boolean
name|isSoap12
parameter_list|)
block|{
try|try
block|{
comment|// We need to populate the soap extensibilityelement proxy for soap11 and soap12
name|ExtensionRegistry
name|extensionRegistry
init|=
name|WSDLFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newPopulatedExtensionRegistry
argument_list|()
decl_stmt|;
name|SoapAddressPlugin
name|addresser
init|=
operator|new
name|SoapAddressPlugin
argument_list|()
decl_stmt|;
name|addresser
operator|.
name|setExtensionRegistry
argument_list|(
name|extensionRegistry
argument_list|)
expr_stmt|;
comment|//SoapAddress soapAddress = SOAPBindingUtil.createSoapAddress(extensionRegistry, isSoap12);
name|String
name|address
init|=
name|ei
operator|.
name|getAddress
argument_list|()
decl_stmt|;
if|if
condition|(
name|address
operator|==
literal|null
condition|)
block|{
name|address
operator|=
literal|"http://localhost:9090"
expr_stmt|;
block|}
comment|//soapAddress.setLocationURI(address);
name|ei
operator|.
name|addExtensor
argument_list|(
name|addresser
operator|.
name|createExtension
argument_list|(
name|isSoap12
argument_list|,
name|address
argument_list|)
argument_list|)
expr_stmt|;
comment|//createSoapBinding(isSoap12, extensionRegistry, bi);
block|}
catch|catch
parameter_list|(
name|WSDLException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|EndpointInfo
name|createEndpointInfo
parameter_list|(
name|ServiceInfo
name|serviceInfo
parameter_list|,
name|BindingInfo
name|b
parameter_list|,
name|Port
name|port
parameter_list|)
block|{
name|String
name|transportURI
init|=
literal|"http://schemas.xmlsoap.org/wsdl/soap/"
decl_stmt|;
if|if
condition|(
name|b
operator|instanceof
name|SoapBindingInfo
condition|)
block|{
name|SoapBindingInfo
name|sbi
init|=
operator|(
name|SoapBindingInfo
operator|)
name|b
decl_stmt|;
name|transportURI
operator|=
name|sbi
operator|.
name|getTransportURI
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|port
operator|!=
literal|null
condition|)
block|{
name|List
name|ees
init|=
name|port
operator|.
name|getExtensibilityElements
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|itr
init|=
name|ees
operator|.
name|iterator
argument_list|()
init|;
name|itr
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Object
name|extensor
init|=
name|itr
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|SOAPBindingUtil
operator|.
name|isSOAPAddress
argument_list|(
name|extensor
argument_list|)
condition|)
block|{
specifier|final
name|SoapAddress
name|sa
init|=
name|SOAPBindingUtil
operator|.
name|getSoapAddress
argument_list|(
name|extensor
argument_list|)
decl_stmt|;
name|EndpointInfo
name|info
init|=
operator|new
name|SoapEndpointInfo
argument_list|(
name|serviceInfo
argument_list|,
name|transportURI
argument_list|)
decl_stmt|;
name|info
operator|.
name|addExtensor
argument_list|(
name|sa
argument_list|)
expr_stmt|;
name|info
operator|.
name|setAddress
argument_list|(
name|sa
operator|.
name|getLocationURI
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|info
return|;
block|}
block|}
block|}
return|return
operator|new
name|SoapEndpointInfo
argument_list|(
name|serviceInfo
argument_list|,
name|transportURI
argument_list|)
return|;
block|}
specifier|public
name|Conduit
name|getConduit
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|,
name|EndpointReferenceType
name|target
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|getConduit
argument_list|(
name|ei
argument_list|)
return|;
block|}
specifier|public
name|Conduit
name|getConduit
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|)
throws|throws
name|IOException
block|{
name|SoapBindingInfo
name|binding
init|=
operator|(
name|SoapBindingInfo
operator|)
name|ei
operator|.
name|getBinding
argument_list|()
decl_stmt|;
name|ConduitInitiator
name|conduitInit
decl_stmt|;
try|try
block|{
name|conduitInit
operator|=
name|bus
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
name|binding
operator|.
name|getTransportURI
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|conduitInit
operator|.
name|getConduit
argument_list|(
name|ei
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|BusException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Could not find conduit initiator for transport "
operator|+
name|binding
operator|.
name|getTransportURI
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|Bus
name|getBus
parameter_list|()
block|{
return|return
name|bus
return|;
block|}
annotation|@
name|Resource
argument_list|(
name|name
operator|=
literal|"bus"
argument_list|)
specifier|public
name|void
name|setBus
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|this
operator|.
name|bus
operator|=
name|bus
expr_stmt|;
block|}
specifier|public
name|void
name|setActivationNamespaces
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|ans
parameter_list|)
block|{
name|activationNamespaces
operator|=
name|ans
expr_stmt|;
block|}
annotation|@
name|PostConstruct
name|void
name|registerWithBindingManager
parameter_list|()
block|{
if|if
condition|(
literal|null
operator|==
name|bus
condition|)
block|{
return|return;
block|}
name|DestinationFactoryManager
name|dfm
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|DestinationFactoryManager
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|dfm
operator|&&
name|activationNamespaces
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|ns
range|:
name|activationNamespaces
control|)
block|{
name|dfm
operator|.
name|registerDestinationFactory
argument_list|(
name|ns
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
specifier|static
class|class
name|SoapEndpointInfo
extends|extends
name|EndpointInfo
block|{
name|SoapAddress
name|saddress
decl_stmt|;
name|SoapEndpointInfo
parameter_list|(
name|ServiceInfo
name|serv
parameter_list|,
name|String
name|trans
parameter_list|)
block|{
name|super
argument_list|(
name|serv
argument_list|,
name|trans
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setAddress
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|super
operator|.
name|setAddress
argument_list|(
name|s
argument_list|)
expr_stmt|;
if|if
condition|(
name|saddress
operator|!=
literal|null
condition|)
block|{
name|saddress
operator|.
name|setLocationURI
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|addExtensor
parameter_list|(
name|Object
name|el
parameter_list|)
block|{
name|super
operator|.
name|addExtensor
argument_list|(
name|el
argument_list|)
expr_stmt|;
if|if
condition|(
name|el
operator|instanceof
name|SoapAddress
condition|)
block|{
name|saddress
operator|=
operator|(
name|SoapAddress
operator|)
name|el
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

