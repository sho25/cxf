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
name|io
operator|.
name|Closeable
import|;
end_import

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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutionException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
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
name|AsyncHandler
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
name|Dispatch
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
name|Response
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
name|soap
operator|.
name|AddressingFeature
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
name|SOAPBinding
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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|wsaddressing
operator|.
name|W3CEndpointReferenceBuilder
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
name|headers
operator|.
name|Header
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
name|jaxb
operator|.
name|JAXBDataBinding
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
name|AddressingProperties
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
name|AttributedURIType
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
name|ContextUtils
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
name|ws
operator|.
name|addressing
operator|.
name|impl
operator|.
name|AddressingPropertiesImpl
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
name|AppSequenceType
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
name|ByeType
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
name|ScopesType
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

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|WSDiscoveryClient
implements|implements
name|Closeable
block|{
specifier|static
specifier|final
name|QName
name|SERVICE_QNAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://docs.oasis-open.org/ws-dd/ns/discovery/2009/01"
argument_list|,
literal|"DiscoveryProxy"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|PORT_QNAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://docs.oasis-open.org/ws-dd/ns/discovery/2009/01"
argument_list|,
literal|"DiscoveryProxy"
argument_list|)
decl_stmt|;
name|String
name|address
init|=
literal|"soap.udp://239.255.255.250:3702"
decl_stmt|;
name|boolean
name|adHoc
init|=
literal|true
decl_stmt|;
name|AtomicInteger
name|msgId
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|long
name|instanceId
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|JAXBContext
name|jaxbContext
decl_stmt|;
name|Service
name|service
decl_stmt|;
name|Dispatch
argument_list|<
name|Object
argument_list|>
name|dispatch
decl_stmt|;
name|ObjectFactory
name|factory
init|=
operator|new
name|ObjectFactory
argument_list|()
decl_stmt|;
name|Bus
name|bus
decl_stmt|;
specifier|public
name|WSDiscoveryClient
parameter_list|()
block|{     }
specifier|public
name|WSDiscoveryClient
parameter_list|(
name|String
name|address
parameter_list|)
block|{
name|this
operator|.
name|address
operator|=
name|address
expr_stmt|;
name|adHoc
operator|=
literal|false
expr_stmt|;
block|}
specifier|private
specifier|synchronized
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
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|jaxbContext
return|;
block|}
specifier|private
specifier|synchronized
name|Service
name|getService
parameter_list|()
block|{
if|if
condition|(
name|service
operator|==
literal|null
condition|)
block|{
name|service
operator|=
name|Service
operator|.
name|create
argument_list|(
name|SERVICE_QNAME
argument_list|)
expr_stmt|;
name|service
operator|.
name|addPort
argument_list|(
name|PORT_QNAME
argument_list|,
name|SOAPBinding
operator|.
name|SOAP12HTTP_BINDING
argument_list|,
name|address
argument_list|)
expr_stmt|;
block|}
return|return
name|service
return|;
block|}
specifier|private
specifier|synchronized
name|void
name|resetDispatch
parameter_list|(
name|String
name|newad
parameter_list|)
block|{
name|address
operator|=
name|newad
expr_stmt|;
name|service
operator|=
literal|null
expr_stmt|;
name|dispatch
operator|=
literal|null
expr_stmt|;
name|adHoc
operator|=
literal|false
expr_stmt|;
block|}
specifier|private
specifier|synchronized
name|Dispatch
argument_list|<
name|Object
argument_list|>
name|getDispatchInternal
parameter_list|(
name|boolean
name|addSeq
parameter_list|)
block|{
if|if
condition|(
name|dispatch
operator|==
literal|null
condition|)
block|{
name|AddressingFeature
name|f
init|=
operator|new
name|AddressingFeature
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|dispatch
operator|=
name|getService
argument_list|()
operator|.
name|createDispatch
argument_list|(
name|PORT_QNAME
argument_list|,
name|getJAXBContext
argument_list|()
argument_list|,
name|Service
operator|.
name|Mode
operator|.
name|PAYLOAD
argument_list|,
name|f
argument_list|)
expr_stmt|;
name|dispatch
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"thread.local.request.context"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
name|addAddressing
argument_list|(
name|dispatch
argument_list|,
literal|false
argument_list|)
expr_stmt|;
return|return
name|dispatch
return|;
block|}
specifier|private
name|void
name|addAddressing
parameter_list|(
name|BindingProvider
name|p
parameter_list|,
name|boolean
name|addSeq
parameter_list|)
block|{
if|if
condition|(
name|adHoc
condition|)
block|{
name|EndpointReferenceType
name|to
init|=
operator|new
name|EndpointReferenceType
argument_list|()
decl_stmt|;
name|AddressingProperties
name|addrProperties
init|=
operator|new
name|AddressingPropertiesImpl
argument_list|()
decl_stmt|;
name|AttributedURIType
name|epr
init|=
operator|new
name|AttributedURIType
argument_list|()
decl_stmt|;
name|epr
operator|.
name|setValue
argument_list|(
literal|"urn:docs-oasis-open-org:ws-dd:ns:discovery:2009:01"
argument_list|)
expr_stmt|;
name|to
operator|.
name|setAddress
argument_list|(
name|epr
argument_list|)
expr_stmt|;
name|addrProperties
operator|.
name|setTo
argument_list|(
name|to
argument_list|)
expr_stmt|;
name|p
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|JAXWSAConstants
operator|.
name|CLIENT_ADDRESSING_PROPERTIES
argument_list|,
name|addrProperties
argument_list|)
expr_stmt|;
if|if
condition|(
name|addSeq
condition|)
block|{
name|AppSequenceType
name|s
init|=
operator|new
name|AppSequenceType
argument_list|()
decl_stmt|;
name|s
operator|.
name|setInstanceId
argument_list|(
name|instanceId
argument_list|)
expr_stmt|;
name|s
operator|.
name|setMessageNumber
argument_list|(
name|msgId
operator|.
name|getAndIncrement
argument_list|()
argument_list|)
expr_stmt|;
name|JAXBElement
argument_list|<
name|AppSequenceType
argument_list|>
name|seq
init|=
operator|new
name|ObjectFactory
argument_list|()
operator|.
name|createAppSequence
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|Header
name|h
init|=
operator|new
name|Header
argument_list|(
name|seq
operator|.
name|getName
argument_list|()
argument_list|,
name|seq
argument_list|,
operator|new
name|JAXBDataBinding
argument_list|(
name|getJAXBContext
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Header
argument_list|>
name|headers
init|=
operator|new
name|ArrayList
argument_list|<
name|Header
argument_list|>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|add
argument_list|(
name|h
argument_list|)
expr_stmt|;
name|p
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|Header
operator|.
name|HEADER_LIST
argument_list|,
name|headers
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
specifier|synchronized
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|dispatch
operator|!=
literal|null
condition|)
block|{
operator|(
operator|(
name|Closeable
operator|)
name|dispatch
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
name|dispatch
operator|=
literal|null
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|finalize
parameter_list|()
throws|throws
name|Throwable
block|{
name|super
operator|.
name|finalize
argument_list|()
expr_stmt|;
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * Sends the "Hello" to broadcast the availability of a service      * @param hello      * @return the hello      */
specifier|public
name|HelloType
name|register
parameter_list|(
name|HelloType
name|hello
parameter_list|)
block|{
if|if
condition|(
name|hello
operator|.
name|getEndpointReference
argument_list|()
operator|==
literal|null
condition|)
block|{
name|hello
operator|.
name|setEndpointReference
argument_list|(
name|generateW3CEndpointReference
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|getDispatchInternal
argument_list|(
literal|true
argument_list|)
operator|.
name|invokeOneWay
argument_list|(
name|factory
operator|.
name|createHello
argument_list|(
name|hello
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|hello
return|;
block|}
comment|/**      * Sends the "Hello" to broadcast the availability of a service      * @param ert The endpoint reference of the Service itself      * @return the Hello that was used to broadcast the availability.      */
specifier|public
name|HelloType
name|register
parameter_list|(
name|EndpointReference
name|ert
parameter_list|)
block|{
name|HelloType
name|hello
init|=
operator|new
name|HelloType
argument_list|()
decl_stmt|;
name|hello
operator|.
name|setScopes
argument_list|(
operator|new
name|ScopesType
argument_list|()
argument_list|)
expr_stmt|;
name|hello
operator|.
name|setMetadataVersion
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|EndpointReferenceType
name|ref
init|=
name|ProviderImpl
operator|.
name|convertToInternal
argument_list|(
name|ert
argument_list|)
decl_stmt|;
name|proccessEndpointReference
argument_list|(
name|ref
argument_list|,
name|hello
operator|.
name|getScopes
argument_list|()
argument_list|,
name|hello
operator|.
name|getTypes
argument_list|()
argument_list|,
name|hello
operator|.
name|getXAddrs
argument_list|()
argument_list|)
expr_stmt|;
name|hello
operator|.
name|setEndpointReference
argument_list|(
name|generateW3CEndpointReference
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|register
argument_list|(
name|hello
argument_list|)
return|;
block|}
specifier|public
name|void
name|unregister
parameter_list|(
name|ByeType
name|bye
parameter_list|)
block|{
name|getDispatchInternal
argument_list|(
literal|true
argument_list|)
operator|.
name|invokeOneWay
argument_list|(
name|factory
operator|.
name|createBye
argument_list|(
name|bye
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|unregister
parameter_list|(
name|HelloType
name|hello
parameter_list|)
block|{
name|ByeType
name|bt
init|=
operator|new
name|ByeType
argument_list|()
decl_stmt|;
name|bt
operator|.
name|setScopes
argument_list|(
name|hello
operator|.
name|getScopes
argument_list|()
argument_list|)
expr_stmt|;
name|bt
operator|.
name|setEndpointReference
argument_list|(
name|hello
operator|.
name|getEndpointReference
argument_list|()
argument_list|)
expr_stmt|;
name|unregister
argument_list|(
name|bt
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|EndpointReference
argument_list|>
name|probe
parameter_list|(
name|QName
name|type
parameter_list|)
block|{
name|ProbeType
name|p
init|=
operator|new
name|ProbeType
argument_list|()
decl_stmt|;
name|p
operator|.
name|getTypes
argument_list|()
operator|.
name|add
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|ProbeMatchesType
name|pmt
init|=
name|probe
argument_list|(
name|p
argument_list|,
literal|1000
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|EndpointReference
argument_list|>
name|er
init|=
operator|new
name|ArrayList
argument_list|<
name|EndpointReference
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|ProbeMatchType
name|pm
range|:
name|pmt
operator|.
name|getProbeMatch
argument_list|()
control|)
block|{
for|for
control|(
name|String
name|add
range|:
name|pm
operator|.
name|getXAddrs
argument_list|()
control|)
block|{
name|W3CEndpointReferenceBuilder
name|builder
init|=
operator|new
name|W3CEndpointReferenceBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|address
argument_list|(
name|add
argument_list|)
expr_stmt|;
comment|//builder.serviceName(type);
comment|//builder.endpointName(type);
name|er
operator|.
name|add
argument_list|(
name|builder
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|er
return|;
block|}
specifier|public
name|ProbeMatchesType
name|probe
parameter_list|(
name|ProbeType
name|params
parameter_list|)
block|{
return|return
name|probe
argument_list|(
name|params
argument_list|,
literal|1000
argument_list|)
return|;
block|}
specifier|public
name|ProbeMatchesType
name|probe
parameter_list|(
name|ProbeType
name|params
parameter_list|,
name|int
name|timeout
parameter_list|)
block|{
name|Dispatch
argument_list|<
name|Object
argument_list|>
name|disp
init|=
name|this
operator|.
name|getDispatchInternal
argument_list|(
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|adHoc
condition|)
block|{
name|disp
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"udp.multi.response.timeout"
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
specifier|final
name|ProbeMatchesType
name|response
init|=
operator|new
name|ProbeMatchesType
argument_list|()
decl_stmt|;
name|AsyncHandler
argument_list|<
name|Object
argument_list|>
name|handler
init|=
operator|new
name|AsyncHandler
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|handleResponse
parameter_list|(
name|Response
argument_list|<
name|Object
argument_list|>
name|res
parameter_list|)
block|{
try|try
block|{
name|Object
name|o
init|=
name|res
operator|.
name|get
argument_list|()
decl_stmt|;
while|while
condition|(
name|o
operator|instanceof
name|JAXBElement
condition|)
block|{
name|o
operator|=
operator|(
operator|(
name|JAXBElement
operator|)
name|o
operator|)
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|o
operator|instanceof
name|ProbeMatchesType
condition|)
block|{
name|response
operator|.
name|getProbeMatch
argument_list|()
operator|.
name|addAll
argument_list|(
operator|(
operator|(
name|ProbeMatchesType
operator|)
name|o
operator|)
operator|.
name|getProbeMatch
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|HelloType
condition|)
block|{
name|HelloType
name|h
init|=
operator|(
name|HelloType
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|h
operator|.
name|getTypes
argument_list|()
operator|.
name|contains
argument_list|(
name|SERVICE_QNAME
argument_list|)
operator|||
name|h
operator|.
name|getTypes
argument_list|()
operator|.
name|contains
argument_list|(
operator|new
name|QName
argument_list|(
literal|""
argument_list|,
name|SERVICE_QNAME
operator|.
name|getLocalPart
argument_list|()
argument_list|)
argument_list|)
condition|)
block|{
comment|// A DiscoveryProxy wants us to flip to managed mode
name|resetDispatch
argument_list|(
name|h
operator|.
name|getXAddrs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
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
catch|catch
parameter_list|(
name|ExecutionException
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
block|}
block|}
decl_stmt|;
name|disp
operator|.
name|invokeAsync
argument_list|(
operator|new
name|ObjectFactory
argument_list|()
operator|.
name|createProbe
argument_list|(
name|params
argument_list|)
argument_list|,
name|handler
argument_list|)
expr_stmt|;
return|return
name|response
return|;
block|}
name|Object
name|o
init|=
name|disp
operator|.
name|invoke
argument_list|(
operator|new
name|ObjectFactory
argument_list|()
operator|.
name|createProbe
argument_list|(
name|params
argument_list|)
argument_list|)
decl_stmt|;
while|while
condition|(
name|o
operator|instanceof
name|JAXBElement
condition|)
block|{
name|o
operator|=
operator|(
operator|(
name|JAXBElement
operator|)
name|o
operator|)
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
return|return
operator|(
name|ProbeMatchesType
operator|)
name|o
return|;
block|}
specifier|private
name|W3CEndpointReference
name|generateW3CEndpointReference
parameter_list|()
block|{
name|W3CEndpointReferenceBuilder
name|builder
init|=
operator|new
name|W3CEndpointReferenceBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|address
argument_list|(
name|ContextUtils
operator|.
name|generateUUID
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
specifier|private
name|void
name|proccessEndpointReference
parameter_list|(
name|EndpointReferenceType
name|ref
parameter_list|,
name|ScopesType
name|scopes
parameter_list|,
name|List
argument_list|<
name|QName
argument_list|>
name|types
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|xAddrs
parameter_list|)
block|{
name|QName
name|nm
init|=
name|EndpointReferenceUtils
operator|.
name|getPortQName
argument_list|(
name|ref
argument_list|,
name|bus
argument_list|)
decl_stmt|;
name|scopes
operator|.
name|getValue
argument_list|()
operator|.
name|add
argument_list|(
name|nm
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|types
operator|.
name|add
argument_list|(
name|nm
argument_list|)
expr_stmt|;
name|String
name|wsdlLocation
init|=
name|EndpointReferenceUtils
operator|.
name|getWSDLLocation
argument_list|(
name|ref
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|wsdlLocation
argument_list|)
condition|)
block|{
name|xAddrs
operator|.
name|add
argument_list|(
name|wsdlLocation
argument_list|)
expr_stmt|;
block|}
name|String
name|add
init|=
name|EndpointReferenceUtils
operator|.
name|getAddress
argument_list|(
name|ref
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|add
argument_list|)
operator|&&
operator|!
name|xAddrs
operator|.
name|contains
argument_list|(
name|add
argument_list|)
condition|)
block|{
name|xAddrs
operator|.
name|add
argument_list|(
name|add
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

