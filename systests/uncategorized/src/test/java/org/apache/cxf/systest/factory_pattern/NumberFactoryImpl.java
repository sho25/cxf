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
name|factory_pattern
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebService
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
name|factory_pattern
operator|.
name|NumberFactory
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
name|LoggingInInterceptor
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
name|testutil
operator|.
name|common
operator|.
name|EmbeddedJMSBrokerLauncher
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
name|EndpointReferenceUtils
import|;
end_import

begin_class
annotation|@
name|WebService
argument_list|(
name|serviceName
operator|=
literal|"NumberFactoryService"
argument_list|,
name|portName
operator|=
literal|"NumberFactoryPort"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.cxf.factory_pattern.NumberFactory"
argument_list|,
name|targetNamespace
operator|=
literal|"http://cxf.apache.org/factory_pattern"
argument_list|)
specifier|public
class|class
name|NumberFactoryImpl
implements|implements
name|NumberFactory
block|{
specifier|public
specifier|static
specifier|final
name|String
name|FACTORY_NS
init|=
literal|"http://cxf.apache.org/factory_pattern"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NUMBER_SERVICE_NAME
init|=
literal|"NumberService"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NUMBER_PORT_NAME
init|=
literal|"NumberPort"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|NUMBER_SERVICE_QNAME
init|=
operator|new
name|QName
argument_list|(
name|FACTORY_NS
argument_list|,
name|NUMBER_SERVICE_NAME
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|NUMBER_PORT_TYPE_QNAME
init|=
operator|new
name|QName
argument_list|(
name|FACTORY_NS
argument_list|,
name|NUMBER_PORT_NAME
argument_list|)
decl_stmt|;
specifier|protected
name|EndpointReferenceType
name|templateEpr
decl_stmt|;
specifier|protected
name|NumberImpl
name|servant
decl_stmt|;
specifier|protected
name|Bus
name|bus
decl_stmt|;
specifier|protected
name|String
name|port
decl_stmt|;
specifier|public
name|NumberFactoryImpl
parameter_list|(
name|Bus
name|b
parameter_list|,
name|String
name|p
parameter_list|)
block|{
name|bus
operator|=
name|b
expr_stmt|;
name|port
operator|=
name|p
expr_stmt|;
block|}
specifier|public
name|W3CEndpointReference
name|create
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|manageNumberServantInitialisation
argument_list|()
expr_stmt|;
name|int
name|val
init|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|id
argument_list|)
decl_stmt|;
comment|// allow clients to drive test scenarios with val
name|String
name|portName
init|=
literal|"NumberPort"
decl_stmt|;
if|if
condition|(
name|val
operator|>=
literal|30
condition|)
block|{
comment|// use jms transport
name|portName
operator|=
literal|"NumberPortJMS"
expr_stmt|;
block|}
name|EndpointReferenceType
name|epr
init|=
name|EndpointReferenceUtils
operator|.
name|getEndpointReferenceWithId
argument_list|(
name|NUMBER_SERVICE_QNAME
argument_list|,
name|portName
argument_list|,
name|id
argument_list|,
name|bus
argument_list|)
decl_stmt|;
name|Source
name|source
init|=
name|EndpointReferenceUtils
operator|.
name|convertToXML
argument_list|(
name|epr
argument_list|)
decl_stmt|;
return|return
operator|new
name|W3CEndpointReference
argument_list|(
name|source
argument_list|)
return|;
block|}
specifier|protected
specifier|synchronized
name|EndpointReferenceType
name|manageNumberServantInitialisation
parameter_list|()
block|{
if|if
condition|(
literal|null
operator|==
name|templateEpr
condition|)
block|{
name|initDefaultServant
argument_list|()
expr_stmt|;
block|}
return|return
name|templateEpr
return|;
block|}
specifier|protected
name|String
name|getServantAddressRoot
parameter_list|()
block|{
return|return
literal|"http://localhost:"
operator|+
name|port
operator|+
literal|"/NumberService/NumberPort/"
return|;
block|}
specifier|protected
name|void
name|initDefaultServant
parameter_list|()
block|{
name|servant
operator|=
operator|new
name|NumberImpl
argument_list|()
expr_stmt|;
name|String
name|wsdlLocation
init|=
literal|"testutils/factory_pattern.wsdl"
decl_stmt|;
name|String
name|bindingId
init|=
literal|null
decl_stmt|;
name|EndpointImpl
name|ep
init|=
operator|new
name|EndpointImpl
argument_list|(
name|bus
argument_list|,
name|servant
argument_list|,
name|bindingId
argument_list|,
name|wsdlLocation
argument_list|)
decl_stmt|;
name|ep
operator|.
name|setEndpointName
argument_list|(
operator|new
name|QName
argument_list|(
name|NUMBER_SERVICE_QNAME
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
literal|"NumberPort"
argument_list|)
argument_list|)
expr_stmt|;
name|ep
operator|.
name|publish
argument_list|(
name|getServantAddressRoot
argument_list|()
argument_list|)
expr_stmt|;
name|templateEpr
operator|=
name|ep
operator|.
name|getServer
argument_list|()
operator|.
name|getDestination
argument_list|()
operator|.
name|getAddress
argument_list|()
expr_stmt|;
comment|// jms port
name|EmbeddedJMSBrokerLauncher
operator|.
name|updateWsdlExtensors
argument_list|(
name|bus
argument_list|,
name|wsdlLocation
argument_list|)
expr_stmt|;
name|ep
operator|=
operator|new
name|EndpointImpl
argument_list|(
name|bus
argument_list|,
name|servant
argument_list|,
name|bindingId
argument_list|,
name|wsdlLocation
argument_list|)
expr_stmt|;
name|ep
operator|.
name|setEndpointName
argument_list|(
operator|new
name|QName
argument_list|(
name|NUMBER_SERVICE_QNAME
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
literal|"NumberPortJMS"
argument_list|)
argument_list|)
expr_stmt|;
name|ep
operator|.
name|publish
argument_list|()
expr_stmt|;
name|ep
operator|.
name|getServer
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|ep
operator|.
name|getServer
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

