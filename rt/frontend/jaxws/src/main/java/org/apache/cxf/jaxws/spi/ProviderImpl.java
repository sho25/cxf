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
name|jaxws
operator|.
name|spi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|wsdl
operator|.
name|Definition
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|Unmarshaller
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Endpoint
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|EndpointReference
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceFeature
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|spi
operator|.
name|ServiceDelegate
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|wsaddressing
operator|.
name|W3CEndpointReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|BusFactory
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
name|WSDLConstants
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
name|classloader
operator|.
name|ClassLoaderUtils
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
name|jaxws
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
name|jaxws
operator|.
name|EndpointUtils
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
name|jaxws
operator|.
name|ServiceImpl
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
name|message
operator|.
name|MessageUtils
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
name|phase
operator|.
name|PhaseInterceptorChain
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
name|staxutils
operator|.
name|StaxUtils
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
name|staxutils
operator|.
name|W3CDOMStreamWriter
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
name|JAXWSAConstants
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

begin_class
specifier|public
class|class
name|ProviderImpl
extends|extends
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|spi
operator|.
name|Provider
block|{
specifier|public
specifier|static
specifier|final
name|String
name|JAXWS_PROVIDER
init|=
name|ProviderImpl
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|ProviderImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|JAXBContext
name|jaxbContext
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|boolean
name|JAXWS_22
decl_stmt|;
static|static
block|{
name|boolean
name|b
init|=
literal|false
decl_stmt|;
try|try
block|{
comment|//JAX-WS 2.2 would have the HttpContext class in the classloader
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
literal|"javax.xml.ws.spi.http.HttpContext"
argument_list|,
name|ProviderImpl
operator|.
name|class
argument_list|)
decl_stmt|;
comment|//In addition to that, the Endpoint class we pick up on the classloader
comment|//should have a publish method that uses it.  Otherwise, we MAY be
comment|//be getting the HttpContext from the 2.2 jaxws-api jar, but the Endpoint
comment|//class from the 2.1 JRE
name|Method
name|m
init|=
name|Endpoint
operator|.
name|class
operator|.
name|getMethod
argument_list|(
literal|"publish"
argument_list|,
name|cls
argument_list|)
decl_stmt|;
name|b
operator|=
name|m
operator|!=
literal|null
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
name|b
operator|=
literal|false
expr_stmt|;
block|}
name|JAXWS_22
operator|=
name|b
expr_stmt|;
block|}
specifier|public
specifier|static
name|boolean
name|isJaxWs22
parameter_list|()
block|{
return|return
name|JAXWS_22
return|;
block|}
annotation|@
name|Override
specifier|public
name|ServiceDelegate
name|createServiceDelegate
parameter_list|(
name|URL
name|url
parameter_list|,
name|QName
name|qname
parameter_list|,
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
name|Class
name|cls
parameter_list|)
block|{
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|()
decl_stmt|;
return|return
operator|new
name|ServiceImpl
argument_list|(
name|bus
argument_list|,
name|url
argument_list|,
name|qname
argument_list|,
name|cls
argument_list|)
return|;
block|}
comment|//new in 2.2
specifier|public
name|ServiceDelegate
name|createServiceDelegate
parameter_list|(
name|URL
name|wsdlDocumentLocation
parameter_list|,
name|QName
name|serviceName
parameter_list|,
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
name|Class
name|serviceClass
parameter_list|,
name|WebServiceFeature
modifier|...
name|features
parameter_list|)
block|{
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|()
decl_stmt|;
for|for
control|(
name|WebServiceFeature
name|f
range|:
name|features
control|)
block|{
if|if
condition|(
operator|!
name|f
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"javax.xml.ws"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|WebServiceException
argument_list|(
literal|"Unknown feature error: "
operator|+
name|f
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
return|return
operator|new
name|ServiceImpl
argument_list|(
name|bus
argument_list|,
name|wsdlDocumentLocation
argument_list|,
name|serviceName
argument_list|,
name|serviceClass
argument_list|,
name|features
argument_list|)
return|;
block|}
specifier|protected
name|EndpointImpl
name|createEndpointImpl
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|String
name|bindingId
parameter_list|,
name|Object
name|implementor
parameter_list|,
name|WebServiceFeature
modifier|...
name|features
parameter_list|)
block|{
return|return
operator|new
name|EndpointImpl
argument_list|(
name|bus
argument_list|,
name|implementor
argument_list|,
name|bindingId
argument_list|,
name|features
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|bindingId
parameter_list|,
name|Object
name|implementor
parameter_list|)
block|{
name|Endpoint
name|ep
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|EndpointUtils
operator|.
name|isValidImplementor
argument_list|(
name|implementor
argument_list|)
condition|)
block|{
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|()
decl_stmt|;
name|ep
operator|=
name|createEndpointImpl
argument_list|(
name|bus
argument_list|,
name|bindingId
argument_list|,
name|implementor
argument_list|)
expr_stmt|;
return|return
name|ep
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|WebServiceException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"INVALID_IMPLEMENTOR_EXC"
argument_list|,
name|LOG
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|//new in 2.2
specifier|public
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|bindingId
parameter_list|,
name|Object
name|implementor
parameter_list|,
name|WebServiceFeature
modifier|...
name|features
parameter_list|)
block|{
name|EndpointImpl
name|ep
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|EndpointUtils
operator|.
name|isValidImplementor
argument_list|(
name|implementor
argument_list|)
condition|)
block|{
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|()
decl_stmt|;
name|ep
operator|=
name|createEndpointImpl
argument_list|(
name|bus
argument_list|,
name|bindingId
argument_list|,
name|implementor
argument_list|,
name|features
argument_list|)
expr_stmt|;
return|return
name|ep
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|WebServiceException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"INVALID_IMPLEMENTOR_EXC"
argument_list|,
name|LOG
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Endpoint
name|createAndPublishEndpoint
parameter_list|(
name|String
name|url
parameter_list|,
name|Object
name|implementor
parameter_list|)
block|{
name|Endpoint
name|ep
init|=
name|createEndpoint
argument_list|(
literal|null
argument_list|,
name|implementor
argument_list|)
decl_stmt|;
name|ep
operator|.
name|publish
argument_list|(
name|url
argument_list|)
expr_stmt|;
return|return
name|ep
return|;
block|}
comment|//new in 2.2
specifier|public
name|Endpoint
name|createAndPublishEndpoint
parameter_list|(
name|String
name|address
parameter_list|,
name|Object
name|implementor
parameter_list|,
name|WebServiceFeature
modifier|...
name|features
parameter_list|)
block|{
name|Endpoint
name|ep
init|=
name|createEndpoint
argument_list|(
literal|null
argument_list|,
name|implementor
argument_list|,
name|features
argument_list|)
decl_stmt|;
name|ep
operator|.
name|publish
argument_list|(
name|address
argument_list|)
expr_stmt|;
return|return
name|ep
return|;
block|}
specifier|public
name|W3CEndpointReference
name|createW3CEndpointReference
parameter_list|(
name|String
name|address
parameter_list|,
name|QName
name|serviceName
parameter_list|,
name|QName
name|portName
parameter_list|,
name|List
argument_list|<
name|Element
argument_list|>
name|metadata
parameter_list|,
name|String
name|wsdlDocumentLocation
parameter_list|,
name|List
argument_list|<
name|Element
argument_list|>
name|referenceParameters
parameter_list|)
block|{
return|return
name|createW3CEndpointReference
argument_list|(
name|address
argument_list|,
literal|null
argument_list|,
name|serviceName
argument_list|,
name|portName
argument_list|,
name|metadata
argument_list|,
name|wsdlDocumentLocation
argument_list|,
name|referenceParameters
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|//CHECKSTYLE:OFF - spec requires a bunch of params
specifier|public
name|W3CEndpointReference
name|createW3CEndpointReference
parameter_list|(
name|String
name|address
parameter_list|,
name|QName
name|interfaceName
parameter_list|,
name|QName
name|serviceName
parameter_list|,
name|QName
name|portName
parameter_list|,
name|List
argument_list|<
name|Element
argument_list|>
name|metadata
parameter_list|,
name|String
name|wsdlDocumentLocation
parameter_list|,
name|List
argument_list|<
name|Element
argument_list|>
name|referenceParameters
parameter_list|,
name|List
argument_list|<
name|Element
argument_list|>
name|elements
parameter_list|,
name|Map
argument_list|<
name|QName
argument_list|,
name|String
argument_list|>
name|attributes
parameter_list|)
block|{
comment|//CHECKSTYLE:ON
if|if
condition|(
name|serviceName
operator|!=
literal|null
operator|&&
name|portName
operator|!=
literal|null
operator|&&
name|wsdlDocumentLocation
operator|!=
literal|null
operator|&&
name|interfaceName
operator|==
literal|null
condition|)
block|{
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|()
decl_stmt|;
name|WSDLManager
name|wsdlManager
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|WSDLManager
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|Definition
name|def
init|=
name|wsdlManager
operator|.
name|getDefinition
argument_list|(
name|wsdlDocumentLocation
argument_list|)
decl_stmt|;
name|interfaceName
operator|=
name|def
operator|.
name|getService
argument_list|(
name|serviceName
argument_list|)
operator|.
name|getPort
argument_list|(
name|portName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
operator|.
name|getBinding
argument_list|()
operator|.
name|getPortType
argument_list|()
operator|.
name|getQName
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// do nothing
block|}
block|}
if|if
condition|(
name|serviceName
operator|==
literal|null
operator|&&
name|portName
operator|==
literal|null
operator|&&
name|address
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Address in an EPR cannot be null, "
operator|+
literal|" when serviceName or portName is null"
argument_list|)
throw|;
block|}
try|try
block|{
name|W3CDOMStreamWriter
name|writer
init|=
operator|new
name|W3CDOMStreamWriter
argument_list|()
decl_stmt|;
name|writer
operator|.
name|setPrefix
argument_list|(
name|JAXWSAConstants
operator|.
name|WSA_PREFIX
argument_list|,
name|JAXWSAConstants
operator|.
name|NS_WSA
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeStartElement
argument_list|(
name|JAXWSAConstants
operator|.
name|WSA_PREFIX
argument_list|,
name|JAXWSAConstants
operator|.
name|WSA_ERF_NAME
argument_list|,
name|JAXWSAConstants
operator|.
name|NS_WSA
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeNamespace
argument_list|(
name|JAXWSAConstants
operator|.
name|WSA_PREFIX
argument_list|,
name|JAXWSAConstants
operator|.
name|NS_WSA
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeStartElement
argument_list|(
name|JAXWSAConstants
operator|.
name|WSA_PREFIX
argument_list|,
name|JAXWSAConstants
operator|.
name|WSA_ADDRESS_NAME
argument_list|,
name|JAXWSAConstants
operator|.
name|NS_WSA
argument_list|)
expr_stmt|;
name|address
operator|=
name|address
operator|==
literal|null
condition|?
literal|""
else|:
name|address
expr_stmt|;
name|writer
operator|.
name|writeCharacters
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
if|if
condition|(
name|referenceParameters
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|writeStartElement
argument_list|(
name|JAXWSAConstants
operator|.
name|WSA_PREFIX
argument_list|,
name|JAXWSAConstants
operator|.
name|WSA_REFERENCEPARAMETERS_NAME
argument_list|,
name|JAXWSAConstants
operator|.
name|NS_WSA
argument_list|)
expr_stmt|;
for|for
control|(
name|Element
name|ele
range|:
name|referenceParameters
control|)
block|{
name|StaxUtils
operator|.
name|writeElement
argument_list|(
name|ele
argument_list|,
name|writer
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|wsdlDocumentLocation
operator|!=
literal|null
operator|||
name|interfaceName
operator|!=
literal|null
operator|||
name|serviceName
operator|!=
literal|null
operator|||
operator|(
name|metadata
operator|!=
literal|null
operator|&&
name|metadata
operator|.
name|size
argument_list|()
operator|>
literal|0
operator|)
condition|)
block|{
name|writer
operator|.
name|writeStartElement
argument_list|(
name|JAXWSAConstants
operator|.
name|WSA_PREFIX
argument_list|,
name|JAXWSAConstants
operator|.
name|WSA_METADATA_NAME
argument_list|,
name|JAXWSAConstants
operator|.
name|NS_WSA
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeNamespace
argument_list|(
name|JAXWSAConstants
operator|.
name|WSAW_PREFIX
argument_list|,
name|JAXWSAConstants
operator|.
name|NS_WSAW
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeNamespace
argument_list|(
name|JAXWSAConstants
operator|.
name|WSAM_PREFIX
argument_list|,
name|JAXWSAConstants
operator|.
name|NS_WSAM
argument_list|)
expr_stmt|;
if|if
condition|(
name|wsdlDocumentLocation
operator|!=
literal|null
condition|)
block|{
name|boolean
name|includeLocationOnly
init|=
literal|false
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
name|message
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|includeLocationOnly
operator|=
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|message
operator|.
name|getContextualProperty
argument_list|(
literal|"org.apache.cxf.wsa.metadata.wsdlLocationOnly"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|attrubuteValue
init|=
name|serviceName
operator|!=
literal|null
operator|&&
operator|!
name|includeLocationOnly
condition|?
name|serviceName
operator|.
name|getNamespaceURI
argument_list|()
operator|+
literal|" "
operator|+
name|wsdlDocumentLocation
else|:
name|wsdlDocumentLocation
decl_stmt|;
name|writer
operator|.
name|writeNamespace
argument_list|(
name|JAXWSAConstants
operator|.
name|WSDLI_PFX
argument_list|,
name|JAXWSAConstants
operator|.
name|NS_WSDLI
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeAttribute
argument_list|(
name|JAXWSAConstants
operator|.
name|WSDLI_PFX
argument_list|,
name|JAXWSAConstants
operator|.
name|NS_WSDLI
argument_list|,
name|JAXWSAConstants
operator|.
name|WSDLI_WSDLLOCATION
argument_list|,
name|attrubuteValue
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|interfaceName
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|writeStartElement
argument_list|(
name|JAXWSAConstants
operator|.
name|WSAM_PREFIX
argument_list|,
name|JAXWSAConstants
operator|.
name|WSAM_INTERFACE_NAME
argument_list|,
name|JAXWSAConstants
operator|.
name|NS_WSAM
argument_list|)
expr_stmt|;
name|String
name|portTypePrefix
init|=
name|interfaceName
operator|.
name|getPrefix
argument_list|()
decl_stmt|;
if|if
condition|(
name|portTypePrefix
operator|==
literal|null
operator|||
name|portTypePrefix
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
name|portTypePrefix
operator|=
literal|"ns1"
expr_stmt|;
block|}
name|writer
operator|.
name|writeNamespace
argument_list|(
name|portTypePrefix
argument_list|,
name|interfaceName
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeCharacters
argument_list|(
name|portTypePrefix
operator|+
literal|":"
operator|+
name|interfaceName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
name|String
name|serviceNamePrefix
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|serviceName
operator|!=
literal|null
condition|)
block|{
name|serviceNamePrefix
operator|=
operator|(
name|serviceName
operator|.
name|getPrefix
argument_list|()
operator|==
literal|null
operator|||
name|serviceName
operator|.
name|getPrefix
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
operator|)
condition|?
literal|"ns2"
else|:
name|serviceName
operator|.
name|getPrefix
argument_list|()
expr_stmt|;
name|writer
operator|.
name|writeStartElement
argument_list|(
name|JAXWSAConstants
operator|.
name|WSAM_PREFIX
argument_list|,
name|JAXWSAConstants
operator|.
name|WSAM_SERVICENAME_NAME
argument_list|,
name|JAXWSAConstants
operator|.
name|NS_WSAM
argument_list|)
expr_stmt|;
if|if
condition|(
name|portName
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|writeAttribute
argument_list|(
name|JAXWSAConstants
operator|.
name|WSAM_ENDPOINT_NAME
argument_list|,
name|portName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|writeNamespace
argument_list|(
name|serviceNamePrefix
argument_list|,
name|serviceName
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeCharacters
argument_list|(
name|serviceNamePrefix
operator|+
literal|":"
operator|+
name|serviceName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|wsdlDocumentLocation
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|writeStartElement
argument_list|(
name|WSDLConstants
operator|.
name|WSDL_PREFIX
argument_list|,
name|WSDLConstants
operator|.
name|QNAME_DEFINITIONS
operator|.
name|getLocalPart
argument_list|()
argument_list|,
name|WSDLConstants
operator|.
name|NS_WSDL11
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeNamespace
argument_list|(
name|WSDLConstants
operator|.
name|WSDL_PREFIX
argument_list|,
name|WSDLConstants
operator|.
name|NS_WSDL11
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeStartElement
argument_list|(
name|WSDLConstants
operator|.
name|WSDL_PREFIX
argument_list|,
name|WSDLConstants
operator|.
name|QNAME_IMPORT
operator|.
name|getLocalPart
argument_list|()
argument_list|,
name|WSDLConstants
operator|.
name|QNAME_IMPORT
operator|.
name|getNamespaceURI
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
name|writer
operator|.
name|writeAttribute
argument_list|(
name|WSDLConstants
operator|.
name|ATTR_NAMESPACE
argument_list|,
name|serviceName
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|writeAttribute
argument_list|(
name|WSDLConstants
operator|.
name|ATTR_LOCATION
argument_list|,
name|wsdlDocumentLocation
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|metadata
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Element
name|e
range|:
name|metadata
control|)
block|{
name|StaxUtils
operator|.
name|writeElement
argument_list|(
name|e
argument_list|,
name|writer
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|elements
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Element
name|e
range|:
name|elements
control|)
block|{
name|StaxUtils
operator|.
name|writeElement
argument_list|(
name|e
argument_list|,
name|writer
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
name|Unmarshaller
name|unmarshaller
init|=
name|getJAXBContext
argument_list|()
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
return|return
operator|(
name|W3CEndpointReference
operator|)
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
name|writer
operator|.
name|getDocument
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|WebServiceException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"ERROR_UNMARSHAL_ENDPOINTREFERENCE"
argument_list|,
name|LOG
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getPort
parameter_list|(
name|EndpointReference
name|endpointReference
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|serviceEndpointInterface
parameter_list|,
name|WebServiceFeature
modifier|...
name|features
parameter_list|)
block|{
name|ServiceDelegate
name|sd
init|=
name|createServiceDelegate
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
name|serviceEndpointInterface
argument_list|)
decl_stmt|;
return|return
name|sd
operator|.
name|getPort
argument_list|(
name|endpointReference
argument_list|,
name|serviceEndpointInterface
argument_list|,
name|features
argument_list|)
return|;
block|}
specifier|public
name|EndpointReference
name|readEndpointReference
parameter_list|(
name|Source
name|eprInfoset
parameter_list|)
block|{
try|try
block|{
name|Unmarshaller
name|unmarshaller
init|=
name|getJAXBContext
argument_list|()
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
return|return
operator|(
name|EndpointReference
operator|)
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|eprInfoset
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|WebServiceException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"ERROR_UNMARSHAL_ENDPOINTREFERENCE"
argument_list|,
name|LOG
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|JAXBContext
name|getJAXBContext
parameter_list|()
block|{
if|if
condition|(
name|jaxbContext
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|jaxbContext
operator|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|W3CEndpointReference
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|WebServiceException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"JAXBCONTEXT_CREATION_FAILED"
argument_list|,
name|LOG
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|jaxbContext
return|;
block|}
block|}
end_class

end_unit

