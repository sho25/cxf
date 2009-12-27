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
name|aegis
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
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
name|Collection
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
name|wsdl
operator|.
name|Import
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
name|factory
operator|.
name|WSDLFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|xml
operator|.
name|WSDLWriter
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
name|NamespaceContext
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
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamWriter
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
name|Document
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
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|aegis
operator|.
name|databinding
operator|.
name|AegisDatabinding
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
name|aegis
operator|.
name|type
operator|.
name|AegisType
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
name|aegis
operator|.
name|xml
operator|.
name|stax
operator|.
name|ElementWriter
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
name|SoapBindingConstants
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
name|SoapBindingFactory
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
name|SoapTransportFactory
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
name|bus
operator|.
name|extension
operator|.
name|ExtensionManagerBus
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
name|util
operator|.
name|SOAPConstants
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
name|xmlschema
operator|.
name|XmlSchemaConstants
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
name|Server
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
name|ServerRegistry
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
name|frontend
operator|.
name|AbstractWSDLBasedEndpointFactory
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
name|frontend
operator|.
name|ServerFactoryBean
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
name|helpers
operator|.
name|DOMUtils
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
name|helpers
operator|.
name|MapNamespaceContext
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
name|interceptor
operator|.
name|LoggingOutInterceptor
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
name|JaxWsServerFactoryBean
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
name|support
operator|.
name|JaxWsServiceFactoryBean
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
name|test
operator|.
name|AbstractCXFTest
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
name|ServiceWSDLBuilder
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
name|WSDLDefinitionBuilder
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
name|WSDLManagerImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|utils
operator|.
name|NamespaceMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractAegisTest
extends|extends
name|AbstractCXFTest
block|{
specifier|protected
name|LocalTransportFactory
name|localTransport
decl_stmt|;
specifier|private
name|boolean
name|enableJDOM
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUpBus
argument_list|()
expr_stmt|;
name|SoapBindingFactory
name|bindingFactory
init|=
operator|new
name|SoapBindingFactory
argument_list|()
decl_stmt|;
name|bindingFactory
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getExtension
argument_list|(
name|BindingFactoryManager
operator|.
name|class
argument_list|)
operator|.
name|registerBindingFactory
argument_list|(
literal|"http://schemas.xmlsoap.org/wsdl/soap/"
argument_list|,
name|bindingFactory
argument_list|)
expr_stmt|;
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
name|SoapTransportFactory
name|soapDF
init|=
operator|new
name|SoapTransportFactory
argument_list|()
decl_stmt|;
name|soapDF
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|dfm
operator|.
name|registerDestinationFactory
argument_list|(
literal|"http://schemas.xmlsoap.org/wsdl/soap/"
argument_list|,
name|soapDF
argument_list|)
expr_stmt|;
name|dfm
operator|.
name|registerDestinationFactory
argument_list|(
name|SoapBindingConstants
operator|.
name|SOAP11_BINDING_ID
argument_list|,
name|soapDF
argument_list|)
expr_stmt|;
name|dfm
operator|.
name|registerDestinationFactory
argument_list|(
literal|"http://cxf.apache.org/transports/local"
argument_list|,
name|soapDF
argument_list|)
expr_stmt|;
name|localTransport
operator|=
operator|new
name|LocalTransportFactory
argument_list|()
expr_stmt|;
name|dfm
operator|.
name|registerDestinationFactory
argument_list|(
literal|"http://schemas.xmlsoap.org/soap/http"
argument_list|,
name|localTransport
argument_list|)
expr_stmt|;
name|dfm
operator|.
name|registerDestinationFactory
argument_list|(
literal|"http://schemas.xmlsoap.org/wsdl/soap/http"
argument_list|,
name|localTransport
argument_list|)
expr_stmt|;
name|dfm
operator|.
name|registerDestinationFactory
argument_list|(
literal|"http://cxf.apache.org/bindings/xformat"
argument_list|,
name|localTransport
argument_list|)
expr_stmt|;
name|dfm
operator|.
name|registerDestinationFactory
argument_list|(
literal|"http://cxf.apache.org/transports/local"
argument_list|,
name|localTransport
argument_list|)
expr_stmt|;
name|ConduitInitiatorManager
name|extension
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
name|extension
operator|.
name|registerConduitInitiator
argument_list|(
name|LocalTransportFactory
operator|.
name|TRANSPORT_ID
argument_list|,
name|localTransport
argument_list|)
expr_stmt|;
name|extension
operator|.
name|registerConduitInitiator
argument_list|(
literal|"http://schemas.xmlsoap.org/wsdl/soap/"
argument_list|,
name|localTransport
argument_list|)
expr_stmt|;
name|extension
operator|.
name|registerConduitInitiator
argument_list|(
literal|"http://schemas.xmlsoap.org/soap/http"
argument_list|,
name|localTransport
argument_list|)
expr_stmt|;
name|extension
operator|.
name|registerConduitInitiator
argument_list|(
name|SoapBindingConstants
operator|.
name|SOAP11_BINDING_ID
argument_list|,
name|localTransport
argument_list|)
expr_stmt|;
name|bus
operator|.
name|setExtension
argument_list|(
operator|new
name|WSDLManagerImpl
argument_list|()
argument_list|,
name|WSDLManager
operator|.
name|class
argument_list|)
expr_stmt|;
comment|//WoodstoxValidationImpl wstxVal = new WoodstoxValidationImpl();
name|addNamespace
argument_list|(
literal|"wsdl"
argument_list|,
name|SOAPConstants
operator|.
name|WSDL11_NS
argument_list|)
expr_stmt|;
name|addNamespace
argument_list|(
literal|"wsdlsoap"
argument_list|,
name|SOAPConstants
operator|.
name|WSDL11_SOAP_NS
argument_list|)
expr_stmt|;
name|addNamespace
argument_list|(
literal|"xsd"
argument_list|,
name|SOAPConstants
operator|.
name|XSD
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Bus
name|createBus
parameter_list|()
throws|throws
name|BusException
block|{
name|ExtensionManagerBus
name|bus
init|=
operator|new
name|ExtensionManagerBus
argument_list|()
decl_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
return|return
name|bus
return|;
block|}
specifier|protected
name|Node
name|invoke
parameter_list|(
name|String
name|service
parameter_list|,
name|String
name|message
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|invoke
argument_list|(
literal|"local://"
operator|+
name|service
argument_list|,
name|LocalTransportFactory
operator|.
name|TRANSPORT_ID
argument_list|,
name|message
argument_list|)
return|;
block|}
specifier|public
name|Server
name|createService
parameter_list|(
name|Class
name|serviceClass
parameter_list|,
name|QName
name|name
parameter_list|)
block|{
return|return
name|createService
argument_list|(
name|serviceClass
argument_list|,
literal|null
argument_list|,
name|name
argument_list|)
return|;
block|}
specifier|public
name|Server
name|createService
parameter_list|(
name|Class
name|serviceClass
parameter_list|,
name|Object
name|serviceBean
parameter_list|,
name|QName
name|name
parameter_list|)
block|{
return|return
name|createService
argument_list|(
name|serviceClass
argument_list|,
name|serviceBean
argument_list|,
name|serviceClass
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|name
argument_list|)
return|;
block|}
specifier|protected
name|Server
name|createService
parameter_list|(
name|Class
name|serviceClass
parameter_list|,
name|QName
name|name
parameter_list|,
name|AegisDatabinding
name|binding
parameter_list|)
block|{
return|return
name|createService
argument_list|(
name|serviceClass
argument_list|,
name|serviceClass
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|name
argument_list|,
name|binding
argument_list|)
return|;
block|}
specifier|protected
name|Server
name|createService
parameter_list|(
name|Class
name|serviceClass
parameter_list|,
name|String
name|address
parameter_list|,
name|QName
name|name
parameter_list|,
name|AegisDatabinding
name|binding
parameter_list|)
block|{
name|ServerFactoryBean
name|sf
init|=
name|createServiceFactory
argument_list|(
name|serviceClass
argument_list|,
literal|null
argument_list|,
name|address
argument_list|,
name|name
argument_list|,
name|binding
argument_list|)
decl_stmt|;
return|return
name|sf
operator|.
name|create
argument_list|()
return|;
block|}
specifier|protected
name|Server
name|createService
parameter_list|(
name|Class
name|serviceClass
parameter_list|,
name|String
name|address
parameter_list|)
block|{
name|ServerFactoryBean
name|sf
init|=
name|createServiceFactory
argument_list|(
name|serviceClass
argument_list|,
literal|null
argument_list|,
name|address
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
return|return
name|sf
operator|.
name|create
argument_list|()
return|;
block|}
specifier|protected
name|Server
name|createService
parameter_list|(
name|Class
name|serviceClass
parameter_list|)
block|{
name|ServerFactoryBean
name|sf
init|=
name|createServiceFactory
argument_list|(
name|serviceClass
argument_list|,
literal|null
argument_list|,
name|serviceClass
operator|.
name|getSimpleName
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
return|return
name|sf
operator|.
name|create
argument_list|()
return|;
block|}
specifier|protected
name|Server
name|createJaxwsService
parameter_list|(
name|Class
name|serviceClass
parameter_list|,
name|Object
name|serviceBean
parameter_list|,
name|String
name|address
parameter_list|,
name|QName
name|name
parameter_list|)
block|{
if|if
condition|(
name|address
operator|==
literal|null
condition|)
block|{
name|address
operator|=
name|serviceClass
operator|.
name|getSimpleName
argument_list|()
expr_stmt|;
block|}
name|JaxWsServiceFactoryBean
name|sf
init|=
operator|new
name|JaxWsServiceFactoryBean
argument_list|()
decl_stmt|;
name|sf
operator|.
name|setDataBinding
argument_list|(
operator|new
name|AegisDatabinding
argument_list|()
argument_list|)
expr_stmt|;
name|JaxWsServerFactoryBean
name|serverFactoryBean
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|serverFactoryBean
operator|.
name|setServiceClass
argument_list|(
name|serviceClass
argument_list|)
expr_stmt|;
if|if
condition|(
name|serviceBean
operator|!=
literal|null
condition|)
block|{
name|serverFactoryBean
operator|.
name|setServiceBean
argument_list|(
name|serviceBean
argument_list|)
expr_stmt|;
block|}
name|serverFactoryBean
operator|.
name|setAddress
argument_list|(
literal|"local://"
operator|+
name|address
argument_list|)
expr_stmt|;
name|serverFactoryBean
operator|.
name|setServiceFactory
argument_list|(
name|sf
argument_list|)
expr_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|serverFactoryBean
operator|.
name|setEndpointName
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|serverFactoryBean
operator|.
name|create
argument_list|()
return|;
block|}
specifier|public
name|Server
name|createService
parameter_list|(
name|Class
name|serviceClass
parameter_list|,
name|Object
name|serviceBean
parameter_list|,
name|String
name|address
parameter_list|,
name|QName
name|name
parameter_list|)
block|{
name|ServerFactoryBean
name|sf
init|=
name|createServiceFactory
argument_list|(
name|serviceClass
argument_list|,
name|serviceBean
argument_list|,
name|address
argument_list|,
name|name
argument_list|,
literal|null
argument_list|)
decl_stmt|;
return|return
name|sf
operator|.
name|create
argument_list|()
return|;
block|}
specifier|public
name|Server
name|createService
parameter_list|(
name|Class
name|serviceClass
parameter_list|,
name|Object
name|serviceBean
parameter_list|,
name|String
name|address
parameter_list|,
name|AegisDatabinding
name|binding
parameter_list|)
block|{
name|ServerFactoryBean
name|sf
init|=
name|createServiceFactory
argument_list|(
name|serviceClass
argument_list|,
name|serviceBean
argument_list|,
name|address
argument_list|,
literal|null
argument_list|,
name|binding
argument_list|)
decl_stmt|;
return|return
name|sf
operator|.
name|create
argument_list|()
return|;
block|}
specifier|protected
name|ServerFactoryBean
name|createServiceFactory
parameter_list|(
name|Class
name|serviceClass
parameter_list|,
name|Object
name|serviceBean
parameter_list|,
name|String
name|address
parameter_list|,
name|QName
name|name
parameter_list|,
name|AegisDatabinding
name|binding
parameter_list|)
block|{
name|ServerFactoryBean
name|sf
init|=
operator|new
name|ServerFactoryBean
argument_list|()
decl_stmt|;
name|sf
operator|.
name|setServiceClass
argument_list|(
name|serviceClass
argument_list|)
expr_stmt|;
if|if
condition|(
name|serviceBean
operator|!=
literal|null
condition|)
block|{
name|sf
operator|.
name|setServiceBean
argument_list|(
name|serviceBean
argument_list|)
expr_stmt|;
block|}
name|sf
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|setServiceName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setAddress
argument_list|(
literal|"local://"
operator|+
name|address
argument_list|)
expr_stmt|;
name|setupAegis
argument_list|(
name|sf
argument_list|,
name|binding
argument_list|)
expr_stmt|;
return|return
name|sf
return|;
block|}
specifier|protected
name|void
name|setupAegis
parameter_list|(
name|AbstractWSDLBasedEndpointFactory
name|sf
parameter_list|)
block|{
name|setupAegis
argument_list|(
name|sf
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
specifier|protected
name|void
name|setupAegis
parameter_list|(
name|AbstractWSDLBasedEndpointFactory
name|sf
parameter_list|,
name|AegisDatabinding
name|binding
parameter_list|)
block|{
if|if
condition|(
name|binding
operator|==
literal|null
condition|)
block|{
name|AegisContext
name|context
init|=
operator|new
name|AegisContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|enableJDOM
condition|)
block|{
name|context
operator|.
name|setEnableJDOMMappings
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|binding
operator|=
operator|new
name|AegisDatabinding
argument_list|()
expr_stmt|;
comment|// perhaps the data binding needs to do this for itself?
name|binding
operator|.
name|setBus
argument_list|(
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|enableJDOM
condition|)
block|{
comment|// this preserves pre-2.1 behavior.
name|binding
operator|.
name|setAegisContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
block|}
name|sf
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|getServiceConfigurations
argument_list|()
operator|.
name|add
argument_list|(
literal|0
argument_list|,
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|aegis
operator|.
name|databinding
operator|.
name|AegisServiceConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|sf
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|setDataBinding
argument_list|(
name|binding
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Collection
argument_list|<
name|Document
argument_list|>
name|getWSDLDocuments
parameter_list|(
name|String
name|string
parameter_list|)
throws|throws
name|WSDLException
block|{
name|WSDLWriter
name|writer
init|=
name|WSDLFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newWSDLWriter
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|Document
argument_list|>
name|docs
init|=
operator|new
name|ArrayList
argument_list|<
name|Document
argument_list|>
argument_list|()
decl_stmt|;
name|Definition
name|definition
init|=
name|getWSDLDefinition
argument_list|(
name|string
argument_list|)
decl_stmt|;
if|if
condition|(
name|definition
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|docs
operator|.
name|add
argument_list|(
name|writer
operator|.
name|getDocument
argument_list|(
name|definition
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|Import
name|wsdlImport
range|:
name|WSDLDefinitionBuilder
operator|.
name|getImports
argument_list|(
name|definition
argument_list|)
control|)
block|{
name|docs
operator|.
name|add
argument_list|(
name|writer
operator|.
name|getDocument
argument_list|(
name|wsdlImport
operator|.
name|getDefinition
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|docs
return|;
block|}
specifier|protected
name|Definition
name|getWSDLDefinition
parameter_list|(
name|String
name|string
parameter_list|)
throws|throws
name|WSDLException
block|{
name|ServerRegistry
name|svrMan
init|=
name|getBus
argument_list|()
operator|.
name|getExtension
argument_list|(
name|ServerRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
name|Server
name|s
range|:
name|svrMan
operator|.
name|getServers
argument_list|()
control|)
block|{
name|Service
name|svc
init|=
name|s
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getService
argument_list|()
decl_stmt|;
if|if
condition|(
name|svc
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|.
name|equals
argument_list|(
name|string
argument_list|)
condition|)
block|{
name|ServiceWSDLBuilder
name|builder
init|=
operator|new
name|ServiceWSDLBuilder
argument_list|(
name|bus
argument_list|,
name|svc
operator|.
name|getServiceInfos
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|Document
name|getWSDLDocument
parameter_list|(
name|String
name|string
parameter_list|)
throws|throws
name|WSDLException
block|{
name|Definition
name|definition
init|=
name|getWSDLDefinition
argument_list|(
name|string
argument_list|)
decl_stmt|;
if|if
condition|(
name|definition
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|WSDLWriter
name|writer
init|=
name|WSDLFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newWSDLWriter
argument_list|()
decl_stmt|;
return|return
name|writer
operator|.
name|getDocument
argument_list|(
name|definition
argument_list|)
return|;
block|}
specifier|protected
name|Context
name|getContext
parameter_list|()
block|{
name|AegisContext
name|globalContext
init|=
operator|new
name|AegisContext
argument_list|()
decl_stmt|;
name|globalContext
operator|.
name|initialize
argument_list|()
expr_stmt|;
return|return
operator|new
name|Context
argument_list|(
name|globalContext
argument_list|)
return|;
block|}
specifier|protected
name|XmlSchema
name|newXmlSchema
parameter_list|(
name|String
name|targetNamespace
parameter_list|)
block|{
name|XmlSchema
name|s
init|=
operator|new
name|XmlSchema
argument_list|()
decl_stmt|;
name|s
operator|.
name|setTargetNamespace
argument_list|(
name|targetNamespace
argument_list|)
expr_stmt|;
name|NamespaceMap
name|xmlsNamespaceMap
init|=
operator|new
name|NamespaceMap
argument_list|()
decl_stmt|;
name|s
operator|.
name|setNamespaceContext
argument_list|(
name|xmlsNamespaceMap
argument_list|)
expr_stmt|;
comment|// tns: is conventional, and besides we have unit tests that are hardcoded to it.
name|xmlsNamespaceMap
operator|.
name|add
argument_list|(
name|WSDLConstants
operator|.
name|CONVENTIONAL_TNS_PREFIX
argument_list|,
name|targetNamespace
argument_list|)
expr_stmt|;
comment|// ditto for xsd: instead of just namespace= for the schema schema.
name|xmlsNamespaceMap
operator|.
name|add
argument_list|(
literal|"xsd"
argument_list|,
name|XmlSchemaConstants
operator|.
name|XSD_NAMESPACE_URI
argument_list|)
expr_stmt|;
return|return
name|s
return|;
block|}
specifier|protected
name|Element
name|createElement
parameter_list|(
name|String
name|namespace
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
name|createElement
argument_list|(
name|namespace
argument_list|,
name|name
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|protected
name|Element
name|createElement
parameter_list|(
name|String
name|namespace
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|namespacePrefix
parameter_list|)
block|{
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|createDocument
argument_list|()
decl_stmt|;
name|Element
name|element
init|=
name|doc
operator|.
name|createElementNS
argument_list|(
name|namespace
argument_list|,
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|namespacePrefix
operator|!=
literal|null
condition|)
block|{
name|element
operator|.
name|setPrefix
argument_list|(
name|namespacePrefix
argument_list|)
expr_stmt|;
name|DOMUtils
operator|.
name|addNamespacePrefix
argument_list|(
name|element
argument_list|,
name|namespace
argument_list|,
name|namespacePrefix
argument_list|)
expr_stmt|;
block|}
name|doc
operator|.
name|appendChild
argument_list|(
name|element
argument_list|)
expr_stmt|;
return|return
name|element
return|;
block|}
specifier|protected
name|ElementWriter
name|getElementWriter
parameter_list|(
name|Element
name|element
parameter_list|)
block|{
return|return
name|getElementWriter
argument_list|(
name|element
argument_list|,
operator|new
name|MapNamespaceContext
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|ElementWriter
name|getElementWriter
parameter_list|(
name|Element
name|element
parameter_list|,
name|NamespaceContext
name|namespaceContext
parameter_list|)
block|{
name|XMLStreamWriter
name|writer
init|=
operator|new
name|W3CDOMStreamWriter
argument_list|(
name|element
argument_list|)
decl_stmt|;
try|try
block|{
name|writer
operator|.
name|setNamespaceContext
argument_list|(
name|namespaceContext
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
operator|new
name|ElementWriter
argument_list|(
name|writer
argument_list|)
return|;
block|}
specifier|protected
name|Element
name|writeObjectToElement
parameter_list|(
name|AegisType
name|type
parameter_list|,
name|Object
name|bean
parameter_list|)
block|{
return|return
name|writeObjectToElement
argument_list|(
name|type
argument_list|,
name|bean
argument_list|,
name|getContext
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|Element
name|writeObjectToElement
parameter_list|(
name|AegisType
name|type
parameter_list|,
name|Object
name|bean
parameter_list|,
name|Context
name|context
parameter_list|)
block|{
name|Element
name|element
init|=
name|createElement
argument_list|(
literal|"urn:Bean"
argument_list|,
literal|"root"
argument_list|,
literal|"b"
argument_list|)
decl_stmt|;
name|ElementWriter
name|writer
init|=
name|getElementWriter
argument_list|(
name|element
argument_list|,
operator|new
name|MapNamespaceContext
argument_list|()
argument_list|)
decl_stmt|;
name|type
operator|.
name|writeObject
argument_list|(
name|bean
argument_list|,
name|writer
argument_list|,
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|element
return|;
block|}
specifier|protected
name|boolean
name|isEnableJDOM
parameter_list|()
block|{
return|return
name|enableJDOM
return|;
block|}
specifier|protected
name|void
name|setEnableJDOM
parameter_list|(
name|boolean
name|enableJDOM
parameter_list|)
block|{
name|this
operator|.
name|enableJDOM
operator|=
name|enableJDOM
expr_stmt|;
block|}
specifier|protected
name|String
name|renderWsdl
parameter_list|(
name|Document
name|wsdlDoc
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|StringWriter
name|out
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|XMLStreamWriter
name|writer
init|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|out
argument_list|)
decl_stmt|;
name|StaxUtils
operator|.
name|writeNode
argument_list|(
name|wsdlDoc
argument_list|,
name|writer
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return
name|out
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

