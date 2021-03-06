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
name|systest
operator|.
name|nested_callback
package|;
end_package

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
name|ws
operator|.
name|BindingProvider
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
name|Service
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
name|common
operator|.
name|jaxb
operator|.
name|JAXBUtils
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
name|spi
operator|.
name|ProviderImpl
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
name|ws
operator|.
name|addressing
operator|.
name|EndpointReferenceUtils
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
name|WSDLManagerImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|nested_callback
operator|.
name|CallbackPortType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|nested_callback
operator|.
name|NestedCallback
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|nested_callback
operator|.
name|ServerPortType
import|;
end_import

begin_class
annotation|@
name|javax
operator|.
name|jws
operator|.
name|WebService
argument_list|(
name|serviceName
operator|=
literal|"SOAPService"
argument_list|,
name|portName
operator|=
literal|"SOAPPort"
argument_list|,
name|targetNamespace
operator|=
literal|"http://apache.org/nested_callback"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.nested_callback.ServerPortType"
argument_list|,
name|wsdlLocation
operator|=
literal|"wsdl/nested_callback.wsdl"
argument_list|)
specifier|public
class|class
name|ServerImpl
implements|implements
name|ServerPortType
block|{
annotation|@
name|Resource
name|Bus
name|bus
decl_stmt|;
specifier|public
name|String
name|foo
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
name|s
return|;
block|}
specifier|public
name|String
name|registerCallback
parameter_list|(
name|NestedCallback
name|callbackObject
parameter_list|)
block|{
try|try
block|{
name|W3CEndpointReference
name|w3cEpr
init|=
name|callbackObject
operator|.
name|getCallback
argument_list|()
decl_stmt|;
name|EndpointReferenceType
name|callback
init|=
name|ProviderImpl
operator|.
name|convertToInternal
argument_list|(
name|w3cEpr
argument_list|)
decl_stmt|;
name|WSDLManager
name|manager
init|=
operator|new
name|WSDLManagerImpl
argument_list|()
decl_stmt|;
name|QName
name|interfaceName
init|=
name|EndpointReferenceUtils
operator|.
name|getInterfaceName
argument_list|(
name|callback
argument_list|,
name|bus
argument_list|)
decl_stmt|;
name|String
name|wsdlLocation
init|=
name|EndpointReferenceUtils
operator|.
name|getWSDLLocation
argument_list|(
name|callback
argument_list|)
decl_stmt|;
name|QName
name|serviceName
init|=
name|EndpointReferenceUtils
operator|.
name|getServiceName
argument_list|(
name|callback
argument_list|,
name|bus
argument_list|)
decl_stmt|;
name|String
name|address
init|=
name|EndpointReferenceUtils
operator|.
name|getAddress
argument_list|(
name|callback
argument_list|)
decl_stmt|;
name|String
name|portString
init|=
name|EndpointReferenceUtils
operator|.
name|getPortName
argument_list|(
name|callback
argument_list|)
decl_stmt|;
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
name|serviceName
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|portString
argument_list|)
decl_stmt|;
name|StringBuilder
name|seiName
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|seiName
operator|.
name|append
argument_list|(
name|JAXBUtils
operator|.
name|namespaceURIToPackage
argument_list|(
name|interfaceName
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|seiName
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
expr_stmt|;
name|seiName
operator|.
name|append
argument_list|(
name|JAXBUtils
operator|.
name|nameToIdentifier
argument_list|(
name|interfaceName
operator|.
name|getLocalPart
argument_list|()
argument_list|,
name|JAXBUtils
operator|.
name|IdentifierType
operator|.
name|INTERFACE
argument_list|)
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|sei
init|=
literal|null
decl_stmt|;
try|try
block|{
name|sei
operator|=
name|Class
operator|.
name|forName
argument_list|(
name|seiName
operator|.
name|toString
argument_list|()
argument_list|,
literal|true
argument_list|,
name|manager
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
name|URL
name|wsdlURL
init|=
operator|new
name|URL
argument_list|(
name|wsdlLocation
argument_list|)
decl_stmt|;
name|Service
name|service
init|=
name|Service
operator|.
name|create
argument_list|(
name|wsdlURL
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|CallbackPortType
name|port
init|=
operator|(
name|CallbackPortType
operator|)
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|sei
argument_list|)
decl_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|port
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|,
name|address
argument_list|)
expr_stmt|;
name|port
operator|.
name|serverSayHi
argument_list|(
literal|"Sean"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
return|return
literal|"registerCallback called"
return|;
block|}
block|}
end_class

end_unit

