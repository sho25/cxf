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
name|ws
operator|.
name|discovery
package|;
end_package

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
name|List
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
name|JAXBElement
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
name|annotation
operator|.
name|XmlSeeAlso
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
name|util
operator|.
name|JAXBSource
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
name|Provider
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
name|WebServiceProvider
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
name|soap
operator|.
name|Addressing
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
name|JAXBContextCache
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
name|discovery
operator|.
name|wsdl
operator|.
name|HelloType
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
name|discovery
operator|.
name|wsdl
operator|.
name|ObjectFactory
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
name|discovery
operator|.
name|wsdl
operator|.
name|ProbeMatchType
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
name|discovery
operator|.
name|wsdl
operator|.
name|ProbeMatchesType
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
name|discovery
operator|.
name|wsdl
operator|.
name|ProbeType
import|;
end_import

begin_class
specifier|public
class|class
name|WSDiscoveryService
block|{
name|Bus
name|bus
decl_stmt|;
name|Endpoint
name|udpEndpoint
decl_stmt|;
name|WSDiscoveryClient
name|client
decl_stmt|;
name|List
argument_list|<
name|HelloType
argument_list|>
name|registered
init|=
operator|new
name|ArrayList
argument_list|<
name|HelloType
argument_list|>
argument_list|()
decl_stmt|;
name|ObjectFactory
name|factory
init|=
operator|new
name|ObjectFactory
argument_list|()
decl_stmt|;
specifier|public
name|WSDiscoveryService
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|bus
operator|=
name|b
expr_stmt|;
name|client
operator|=
operator|new
name|WSDiscoveryClient
argument_list|()
expr_stmt|;
block|}
specifier|public
name|HelloType
name|register
parameter_list|(
name|EndpointReference
name|ref
parameter_list|)
block|{
name|HelloType
name|ht
init|=
name|client
operator|.
name|register
argument_list|(
name|ref
argument_list|)
decl_stmt|;
name|registered
operator|.
name|add
argument_list|(
name|ht
argument_list|)
expr_stmt|;
return|return
name|ht
return|;
block|}
specifier|public
name|void
name|unregister
parameter_list|(
name|HelloType
name|ht
parameter_list|)
block|{
name|registered
operator|.
name|remove
argument_list|(
name|ht
argument_list|)
expr_stmt|;
name|client
operator|.
name|unregister
argument_list|(
name|ht
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|startup
parameter_list|()
block|{
name|udpEndpoint
operator|=
name|Endpoint
operator|.
name|create
argument_list|(
operator|new
name|WSDiscoveryProvider
argument_list|()
argument_list|)
expr_stmt|;
name|udpEndpoint
operator|.
name|publish
argument_list|(
literal|"soap.udp://239.255.255.250:3702"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|WebServiceProvider
argument_list|(
name|wsdlLocation
operator|=
literal|"classpath:/org/apache/cxf/ws/discovery/wsdl/wsdd-discovery-1.1-wsdl-os.wsdl"
argument_list|,
name|targetNamespace
operator|=
literal|"http://docs.oasis-open.org/ws-dd/ns/discovery/2009/01"
argument_list|,
name|serviceName
operator|=
literal|"Discovery"
argument_list|,
name|portName
operator|=
literal|"DiscoveryUDP"
argument_list|)
annotation|@
name|XmlSeeAlso
argument_list|(
name|ObjectFactory
operator|.
name|class
argument_list|)
annotation|@
name|Addressing
argument_list|(
name|required
operator|=
literal|true
argument_list|)
class|class
name|WSDiscoveryProvider
implements|implements
name|Provider
argument_list|<
name|Source
argument_list|>
block|{
name|JAXBContext
name|context
decl_stmt|;
specifier|public
name|WSDiscoveryProvider
parameter_list|()
block|{
try|try
block|{
name|context
operator|=
name|JAXBContextCache
operator|.
name|getCachedContextAndSchemas
argument_list|(
name|ObjectFactory
operator|.
name|class
argument_list|)
operator|.
name|getContext
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JAXBException
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
name|Source
name|invoke
parameter_list|(
name|Source
name|request
parameter_list|)
block|{
try|try
block|{
name|Object
name|obj
init|=
name|context
operator|.
name|createUnmarshaller
argument_list|()
operator|.
name|unmarshal
argument_list|(
name|request
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|JAXBElement
condition|)
block|{
name|obj
operator|=
operator|(
operator|(
name|JAXBElement
operator|)
name|obj
operator|)
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|obj
operator|instanceof
name|ProbeType
condition|)
block|{
name|ProbeMatchesType
name|pmt
init|=
operator|new
name|ProbeMatchesType
argument_list|()
decl_stmt|;
for|for
control|(
name|HelloType
name|ht
range|:
name|registered
control|)
block|{
name|ProbeMatchType
name|m
init|=
operator|new
name|ProbeMatchType
argument_list|()
decl_stmt|;
name|m
operator|.
name|setEndpointReference
argument_list|(
name|ht
operator|.
name|getEndpointReference
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|.
name|setScopes
argument_list|(
name|ht
operator|.
name|getScopes
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|.
name|setMetadataVersion
argument_list|(
name|ht
operator|.
name|getMetadataVersion
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|.
name|getTypes
argument_list|()
operator|.
name|addAll
argument_list|(
name|ht
operator|.
name|getTypes
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|.
name|getXAddrs
argument_list|()
operator|.
name|addAll
argument_list|(
name|ht
operator|.
name|getXAddrs
argument_list|()
argument_list|)
expr_stmt|;
name|pmt
operator|.
name|getProbeMatch
argument_list|()
operator|.
name|add
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|JAXBSource
argument_list|(
name|context
argument_list|,
name|factory
operator|.
name|createProbeMatches
argument_list|(
name|pmt
argument_list|)
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
comment|// TODO Auto-generated catch block
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

