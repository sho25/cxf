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
name|ArrayList
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
name|Collection
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
name|HashSet
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
name|binding
operator|.
name|soap
operator|.
name|tcp
operator|.
name|SoapTcpDestination
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
name|tcp
operator|.
name|TCPConduit
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
name|wsdl
operator|.
name|extensions
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
name|common
operator|.
name|injection
operator|.
name|NoJSR250Annotations
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
name|StringUtils
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
name|wsdl
operator|.
name|WSDLManager
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
annotation|@
name|NoJSR250Annotations
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
name|CANNOT_GET_CONDUIT_ERROR
init|=
literal|"Could not find conduit initiator for address: %s and transport: %s"
decl_stmt|;
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
specifier|public
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|DEFAULT_NAMESPACES
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"http://schemas.xmlsoap.org/soap/"
argument_list|,
literal|"http://schemas.xmlsoap.org/wsdl/soap/"
argument_list|,
literal|"http://schemas.xmlsoap.org/wsdl/soap12/"
argument_list|,
literal|"http://schemas.xmlsoap.org/soap/http"
argument_list|,
literal|"http://schemas.xmlsoap.org/wsdl/soap/http"
argument_list|,
literal|"http://www.w3.org/2010/soapjms/"
argument_list|,
literal|"http://www.w3.org/2003/05/soap/bindings/HTTP/"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|DEFAULT_PREFIXES
init|=
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"soap.udp"
argument_list|,
literal|"soap.tcp"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
specifier|public
name|SoapTransportFactory
parameter_list|()
block|{
name|super
argument_list|(
name|DEFAULT_NAMESPACES
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getUriPrefixes
parameter_list|()
block|{
return|return
name|DEFAULT_PREFIXES
return|;
block|}
specifier|public
name|String
name|mapTransportURI
parameter_list|(
name|String
name|s
parameter_list|,
name|String
name|address
parameter_list|)
block|{
if|if
condition|(
name|SoapJMSConstants
operator|.
name|SOAP_JMS_SPECIFICIATION_TRANSPORTID
operator|.
name|equals
argument_list|(
name|s
argument_list|)
operator|||
operator|(
name|address
operator|!=
literal|null
operator|&&
name|address
operator|.
name|startsWith
argument_list|(
literal|"jms"
argument_list|)
operator|)
condition|)
block|{
name|s
operator|=
literal|"http://cxf.apache.org/transports/jms"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|address
operator|!=
literal|null
operator|&&
name|address
operator|.
name|startsWith
argument_list|(
literal|"soap.udp"
argument_list|)
condition|)
block|{
name|s
operator|=
literal|"http://cxf.apache.org/transports/udp"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SOAP_11_HTTP_BINDING
operator|.
name|equals
argument_list|(
name|s
argument_list|)
operator|||
name|SOAP_12_HTTP_BINDING
operator|.
name|equals
argument_list|(
name|s
argument_list|)
operator|||
literal|"http://schemas.xmlsoap.org/wsdl/soap/"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
operator|||
literal|"http://schemas.xmlsoap.org/wsdl/http"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
operator|||
literal|"http://schemas.xmlsoap.org/wsdl/soap/http"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
operator|||
literal|"http://schemas.xmlsoap.org/wsdl/soap/http/"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
operator|||
literal|"http://schemas.xmlsoap.org/wsdl/http/"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|s
operator|=
literal|"http://cxf.apache.org/transports/http"
expr_stmt|;
block|}
return|return
name|s
return|;
block|}
specifier|private
name|boolean
name|isJMSSpecAddress
parameter_list|(
name|String
name|address
parameter_list|)
block|{
return|return
name|address
operator|!=
literal|null
operator|&&
name|address
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
name|address
argument_list|)
return|;
block|}
specifier|public
name|Destination
name|getDestination
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|,
name|Bus
name|bus
parameter_list|)
throws|throws
name|IOException
block|{
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
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|address
argument_list|)
operator|&&
name|address
operator|.
name|startsWith
argument_list|(
literal|"soap.tcp"
argument_list|)
condition|)
block|{
return|return
operator|new
name|SoapTcpDestination
argument_list|(
name|ei
operator|.
name|getTarget
argument_list|()
argument_list|,
name|ei
argument_list|)
return|;
block|}
name|BindingInfo
name|bi
init|=
name|ei
operator|.
name|getBinding
argument_list|()
decl_stmt|;
name|String
name|transId
init|=
name|ei
operator|.
name|getTransportId
argument_list|()
decl_stmt|;
if|if
condition|(
name|bi
operator|instanceof
name|SoapBindingInfo
condition|)
block|{
name|transId
operator|=
operator|(
operator|(
name|SoapBindingInfo
operator|)
name|bi
operator|)
operator|.
name|getTransportURI
argument_list|()
expr_stmt|;
if|if
condition|(
name|transId
operator|==
literal|null
condition|)
block|{
name|transId
operator|=
name|ei
operator|.
name|getTransportId
argument_list|()
expr_stmt|;
block|}
block|}
name|DestinationFactory
name|destinationFactory
decl_stmt|;
try|try
block|{
name|DestinationFactoryManager
name|mgr
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
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|address
argument_list|)
operator|||
name|address
operator|.
name|startsWith
argument_list|(
literal|"http"
argument_list|)
operator|||
name|address
operator|.
name|startsWith
argument_list|(
literal|"jms"
argument_list|)
operator|||
name|address
operator|.
name|startsWith
argument_list|(
literal|"soap.udp"
argument_list|)
operator|||
name|address
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|destinationFactory
operator|=
name|mgr
operator|.
name|getDestinationFactory
argument_list|(
name|mapTransportURI
argument_list|(
name|transId
argument_list|,
name|address
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|destinationFactory
operator|=
name|mgr
operator|.
name|getDestinationFactoryForUri
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
return|return
name|destinationFactory
operator|.
name|getDestination
argument_list|(
name|ei
argument_list|,
name|bus
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|BusException
name|e
parameter_list|)
block|{
name|IOException
name|ex
init|=
operator|new
name|IOException
argument_list|(
literal|"Could not find destination factory for transport "
operator|+
name|transId
argument_list|)
decl_stmt|;
name|ex
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
specifier|public
name|void
name|createPortExtensors
parameter_list|(
name|Bus
name|b
parameter_list|,
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
name|b
argument_list|,
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
name|Bus
name|bus
parameter_list|,
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
name|ExtensionRegistry
name|registry
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|WSDLManager
operator|.
name|class
argument_list|)
operator|.
name|getExtensionRegistry
argument_list|()
decl_stmt|;
name|SoapAddress
name|soapAddress
init|=
name|SOAPBindingUtil
operator|.
name|createSoapAddress
argument_list|(
name|registry
argument_list|,
name|isSoap12
argument_list|)
decl_stmt|;
name|soapAddress
operator|.
name|setLocationURI
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|ei
operator|.
name|addExtensor
argument_list|(
name|soapAddress
argument_list|)
expr_stmt|;
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
name|Bus
name|bus
parameter_list|,
name|ServiceInfo
name|serviceInfo
parameter_list|,
name|BindingInfo
name|b
parameter_list|,
name|List
argument_list|<
name|?
argument_list|>
name|ees
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
if|if
condition|(
name|ees
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Iterator
argument_list|<
name|?
argument_list|>
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
if|if
condition|(
name|isJMSSpecAddress
argument_list|(
name|sa
operator|.
name|getLocationURI
argument_list|()
argument_list|)
condition|)
block|{
name|info
operator|.
name|setTransportId
argument_list|(
name|SoapJMSConstants
operator|.
name|SOAP_JMS_SPECIFICIATION_TRANSPORTID
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|info
operator|.
name|addExtensor
argument_list|(
name|extensor
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|info
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
parameter_list|,
name|Bus
name|bus
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|address
init|=
name|target
operator|==
literal|null
condition|?
name|ei
operator|.
name|getAddress
argument_list|()
else|:
name|target
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|address
argument_list|)
operator|&&
name|address
operator|.
name|startsWith
argument_list|(
literal|"soap.tcp://"
argument_list|)
condition|)
block|{
comment|//TODO - examine policies and stuff to look for the sun tcp policies
return|return
operator|new
name|TCPConduit
argument_list|(
name|ei
argument_list|)
return|;
block|}
name|BindingInfo
name|bi
init|=
name|ei
operator|.
name|getBinding
argument_list|()
decl_stmt|;
name|String
name|transId
init|=
name|ei
operator|.
name|getTransportId
argument_list|()
decl_stmt|;
if|if
condition|(
name|bi
operator|instanceof
name|SoapBindingInfo
condition|)
block|{
name|transId
operator|=
operator|(
operator|(
name|SoapBindingInfo
operator|)
name|bi
operator|)
operator|.
name|getTransportURI
argument_list|()
expr_stmt|;
if|if
condition|(
name|transId
operator|==
literal|null
condition|)
block|{
name|transId
operator|=
name|ei
operator|.
name|getTransportId
argument_list|()
expr_stmt|;
block|}
block|}
name|ConduitInitiator
name|conduitInit
decl_stmt|;
try|try
block|{
name|ConduitInitiatorManager
name|mgr
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ConduitInitiatorManager
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|address
argument_list|)
operator|||
name|address
operator|.
name|startsWith
argument_list|(
literal|"http"
argument_list|)
operator|||
name|address
operator|.
name|startsWith
argument_list|(
literal|"jms"
argument_list|)
operator|||
name|address
operator|.
name|startsWith
argument_list|(
literal|"soap.udp"
argument_list|)
condition|)
block|{
name|conduitInit
operator|=
name|mgr
operator|.
name|getConduitInitiator
argument_list|(
name|mapTransportURI
argument_list|(
name|transId
argument_list|,
name|address
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|conduitInit
operator|=
name|mgr
operator|.
name|getConduitInitiatorForUri
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|conduitInit
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|CANNOT_GET_CONDUIT_ERROR
argument_list|,
name|address
argument_list|,
name|transId
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|conduitInit
operator|.
name|getConduit
argument_list|(
name|ei
argument_list|,
name|target
argument_list|,
name|bus
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
name|String
operator|.
name|format
argument_list|(
name|CANNOT_GET_CONDUIT_ERROR
argument_list|,
name|address
argument_list|,
name|transId
argument_list|)
argument_list|)
throw|;
block|}
block|}
specifier|public
name|Conduit
name|getConduit
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|,
name|Bus
name|b
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|getConduit
argument_list|(
name|ei
argument_list|,
name|ei
operator|.
name|getTarget
argument_list|()
argument_list|,
name|b
argument_list|)
return|;
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
name|super
operator|.
name|setTransportIds
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|ans
argument_list|)
argument_list|)
expr_stmt|;
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

