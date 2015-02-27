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
name|security
operator|.
name|trust
package|;
end_package

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
name|BindingFactory
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
name|model
operator|.
name|SoapOperationInfo
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
name|Configurer
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
name|databinding
operator|.
name|source
operator|.
name|SourceDataBinding
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
name|message
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
name|InterfaceInfo
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
name|MessageInfo
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
name|MessagePartInfo
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
name|OperationInfo
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
name|VersionTransformer
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
name|security
operator|.
name|SecurityConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Policy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|policy
operator|.
name|model
operator|.
name|IssuedToken
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|STSUtils
block|{
comment|/**      * WS-T 1.0 Namespace.      */
specifier|public
specifier|static
specifier|final
name|String
name|WST_NS_05_02
init|=
literal|"http://schemas.xmlsoap.org/ws/2005/02/trust"
decl_stmt|;
comment|/**      * WS-T 1.3 Namespace.      */
specifier|public
specifier|static
specifier|final
name|String
name|WST_NS_05_12
init|=
literal|"http://docs.oasis-open.org/ws-sx/ws-trust/200512"
decl_stmt|;
comment|/**      * WS-T 1.4 Namespace.      */
specifier|public
specifier|static
specifier|final
name|String
name|WST_NS_08_02
init|=
literal|"http://docs.oasis-open.org/ws-sx/ws-trust/200802"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SCT_NS_05_02
init|=
literal|"http://schemas.xmlsoap.org/ws/2005/02/sc"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SCT_NS_05_12
init|=
literal|"http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TOKEN_TYPE_SCT_05_02
init|=
name|SCT_NS_05_02
operator|+
literal|"/sct"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TOKEN_TYPE_SCT_05_12
init|=
name|SCT_NS_05_12
operator|+
literal|"/sct"
decl_stmt|;
specifier|private
name|STSUtils
parameter_list|()
block|{
comment|//utility class
block|}
specifier|public
specifier|static
name|String
name|getTokenTypeSCT
parameter_list|(
name|String
name|trustNs
parameter_list|)
block|{
if|if
condition|(
name|WST_NS_05_02
operator|.
name|equals
argument_list|(
name|trustNs
argument_list|)
condition|)
block|{
return|return
name|TOKEN_TYPE_SCT_05_02
return|;
block|}
return|return
name|TOKEN_TYPE_SCT_05_12
return|;
block|}
specifier|public
specifier|static
name|STSClient
name|getClient
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|type
parameter_list|)
block|{
return|return
name|getClientWithIssuer
argument_list|(
name|message
argument_list|,
name|type
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|STSClient
name|getClient
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|type
parameter_list|,
name|IssuedToken
name|itok
parameter_list|)
block|{
if|if
condition|(
name|itok
operator|!=
literal|null
condition|)
block|{
return|return
name|getClientWithIssuer
argument_list|(
name|message
argument_list|,
name|type
argument_list|,
name|itok
operator|.
name|getIssuer
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|getClientWithIssuer
argument_list|(
name|message
argument_list|,
name|type
argument_list|,
literal|null
argument_list|)
return|;
block|}
block|}
specifier|public
specifier|static
name|STSClient
name|getClientWithIssuer
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|type
parameter_list|,
name|Element
name|issuer
parameter_list|)
block|{
comment|// Retrieve or create the STSClient
name|STSClient
name|client
init|=
operator|(
name|STSClient
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|STS_CLIENT
argument_list|)
decl_stmt|;
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
name|client
operator|=
name|createSTSClient
argument_list|(
name|message
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|Bus
name|bus
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Check for the "default" case first
name|bus
operator|.
name|getExtension
argument_list|(
name|Configurer
operator|.
name|class
argument_list|)
operator|.
name|configureBean
argument_list|(
literal|"default.sts-client"
argument_list|,
name|client
argument_list|)
expr_stmt|;
comment|// Check for Endpoint specific case next
if|if
condition|(
name|client
operator|.
name|getBeanName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|bus
operator|.
name|getExtension
argument_list|(
name|Configurer
operator|.
name|class
argument_list|)
operator|.
name|configureBean
argument_list|(
name|client
operator|.
name|getBeanName
argument_list|()
argument_list|,
name|client
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Find out if we have an EPR to get the STS Address (possibly via WS-MEX)
if|if
condition|(
name|issuer
operator|!=
literal|null
condition|)
block|{
name|EndpointReferenceType
name|epr
init|=
literal|null
decl_stmt|;
try|try
block|{
name|epr
operator|=
name|VersionTransformer
operator|.
name|parseEndpointReference
argument_list|(
name|issuer
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
name|IllegalArgumentException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|String
name|mexLocation
init|=
name|findMEXLocation
argument_list|(
name|epr
argument_list|)
decl_stmt|;
comment|// Configure via WS-MEX
if|if
condition|(
name|mexLocation
operator|!=
literal|null
operator|&&
name|MessageUtils
operator|.
name|getContextualBoolean
argument_list|(
name|message
argument_list|,
name|SecurityConstants
operator|.
name|PREFER_WSMEX_OVER_STS_CLIENT_CONFIG
argument_list|,
literal|false
argument_list|)
condition|)
block|{
comment|// WS-MEX call. So now either get the WS-MEX specific STSClient or else create one
name|STSClient
name|wsMexClient
init|=
operator|(
name|STSClient
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|STS_CLIENT
operator|+
literal|".wsmex"
argument_list|)
decl_stmt|;
if|if
condition|(
name|wsMexClient
operator|==
literal|null
condition|)
block|{
name|wsMexClient
operator|=
name|createSTSClient
argument_list|(
name|message
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
name|wsMexClient
operator|.
name|configureViaEPR
argument_list|(
name|epr
argument_list|,
literal|false
argument_list|)
expr_stmt|;
return|return
name|wsMexClient
return|;
block|}
elseif|else
if|if
condition|(
name|configureViaEPR
argument_list|(
name|client
argument_list|,
name|epr
argument_list|)
condition|)
block|{
comment|// Only use WS-MEX here if the pre-configured STSClient has no location/wsdllocation
name|boolean
name|useEPRWSAAddrAsMEXLocation
init|=
operator|!
name|Boolean
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|DISABLE_STS_CLIENT_WSMEX_CALL_USING_EPR_ADDRESS
argument_list|)
argument_list|)
decl_stmt|;
name|client
operator|.
name|configureViaEPR
argument_list|(
name|epr
argument_list|,
name|useEPRWSAAddrAsMEXLocation
argument_list|)
expr_stmt|;
return|return
name|client
return|;
block|}
block|}
return|return
name|client
return|;
block|}
specifier|public
specifier|static
name|boolean
name|configureViaEPR
parameter_list|(
name|STSClient
name|client
parameter_list|,
name|EndpointReferenceType
name|epr
parameter_list|)
block|{
if|if
condition|(
name|epr
operator|!=
literal|null
operator|&&
name|client
operator|.
name|getLocation
argument_list|()
operator|==
literal|null
operator|&&
name|client
operator|.
name|getWsdlLocation
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|private
specifier|static
name|STSClient
name|createSTSClient
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|type
parameter_list|)
block|{
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
name|type
operator|=
literal|""
expr_stmt|;
block|}
else|else
block|{
name|type
operator|=
literal|"."
operator|+
name|type
operator|+
literal|"-client"
expr_stmt|;
block|}
name|STSClient
name|client
init|=
operator|new
name|STSClient
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|Endpoint
name|ep
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|client
operator|.
name|setEndpointName
argument_list|(
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
name|type
argument_list|)
expr_stmt|;
name|client
operator|.
name|setBeanName
argument_list|(
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
name|type
argument_list|)
expr_stmt|;
if|if
condition|(
name|MessageUtils
operator|.
name|getContextualBoolean
argument_list|(
name|message
argument_list|,
name|SecurityConstants
operator|.
name|STS_CLIENT_SOAP12_BINDING
argument_list|,
literal|false
argument_list|)
condition|)
block|{
name|client
operator|.
name|setSoap12
argument_list|()
expr_stmt|;
block|}
return|return
name|client
return|;
block|}
specifier|public
specifier|static
name|String
name|findMEXLocation
parameter_list|(
name|EndpointReferenceType
name|ref
parameter_list|)
block|{
if|if
condition|(
name|ref
operator|.
name|getMetadata
argument_list|()
operator|!=
literal|null
operator|&&
name|ref
operator|.
name|getMetadata
argument_list|()
operator|.
name|getAny
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Object
name|any
range|:
name|ref
operator|.
name|getMetadata
argument_list|()
operator|.
name|getAny
argument_list|()
control|)
block|{
if|if
condition|(
name|any
operator|instanceof
name|Element
condition|)
block|{
name|String
name|addr
init|=
name|findMEXLocation
argument_list|(
operator|(
name|Element
operator|)
name|any
argument_list|)
decl_stmt|;
if|if
condition|(
name|addr
operator|!=
literal|null
condition|)
block|{
return|return
name|addr
return|;
block|}
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|String
name|findMEXLocation
parameter_list|(
name|Element
name|ref
parameter_list|)
block|{
name|Element
name|el
init|=
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|ref
argument_list|)
decl_stmt|;
while|while
condition|(
name|el
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|el
operator|.
name|getLocalName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"Address"
argument_list|)
operator|&&
name|VersionTransformer
operator|.
name|isSupported
argument_list|(
name|el
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|&&
literal|"MetadataReference"
operator|.
name|equals
argument_list|(
name|ref
operator|.
name|getLocalName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|DOMUtils
operator|.
name|getContent
argument_list|(
name|el
argument_list|)
return|;
block|}
else|else
block|{
name|String
name|ad
init|=
name|findMEXLocation
argument_list|(
name|el
argument_list|)
decl_stmt|;
if|if
condition|(
name|ad
operator|!=
literal|null
condition|)
block|{
return|return
name|ad
return|;
block|}
block|}
name|el
operator|=
name|DOMUtils
operator|.
name|getNextElement
argument_list|(
name|el
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|Endpoint
name|createSTSEndpoint
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|transportId
parameter_list|,
name|String
name|location
parameter_list|,
name|String
name|soapVersion
parameter_list|,
name|Policy
name|policy
parameter_list|,
name|QName
name|epName
parameter_list|)
throws|throws
name|BusException
throws|,
name|EndpointException
block|{
return|return
name|createSTSEndpoint
argument_list|(
name|bus
argument_list|,
name|namespace
argument_list|,
name|transportId
argument_list|,
name|location
argument_list|,
name|soapVersion
argument_list|,
name|policy
argument_list|,
name|epName
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Endpoint
name|createSCEndpoint
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|transportId
parameter_list|,
name|String
name|location
parameter_list|,
name|String
name|soapVersion
parameter_list|,
name|Policy
name|policy
parameter_list|)
throws|throws
name|BusException
throws|,
name|EndpointException
block|{
return|return
name|createSTSEndpoint
argument_list|(
name|bus
argument_list|,
name|namespace
argument_list|,
name|transportId
argument_list|,
name|location
argument_list|,
name|soapVersion
argument_list|,
name|policy
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
return|;
block|}
comment|//CHECKSTYLE:OFF
specifier|private
specifier|static
name|Endpoint
name|createSTSEndpoint
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|transportId
parameter_list|,
name|String
name|location
parameter_list|,
name|String
name|soapVersion
parameter_list|,
name|Policy
name|policy
parameter_list|,
name|QName
name|epName
parameter_list|,
name|boolean
name|sc
parameter_list|)
throws|throws
name|BusException
throws|,
name|EndpointException
block|{
comment|//CHECKSTYLE:ON
name|Service
name|service
init|=
literal|null
decl_stmt|;
name|String
name|ns
init|=
name|namespace
operator|+
literal|"/wsdl"
decl_stmt|;
name|ServiceInfo
name|si
init|=
operator|new
name|ServiceInfo
argument_list|()
decl_stmt|;
name|QName
name|iName
init|=
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
name|sc
condition|?
literal|"SecureConversationTokenService"
else|:
literal|"SecurityTokenService"
argument_list|)
decl_stmt|;
name|si
operator|.
name|setName
argument_list|(
name|iName
argument_list|)
expr_stmt|;
name|InterfaceInfo
name|ii
init|=
operator|new
name|InterfaceInfo
argument_list|(
name|si
argument_list|,
name|iName
argument_list|)
decl_stmt|;
name|OperationInfo
name|ioi
init|=
name|addIssueOperation
argument_list|(
name|ii
argument_list|,
name|namespace
argument_list|,
name|ns
argument_list|)
decl_stmt|;
name|OperationInfo
name|coi
init|=
name|addCancelOperation
argument_list|(
name|ii
argument_list|,
name|namespace
argument_list|,
name|ns
argument_list|)
decl_stmt|;
name|OperationInfo
name|roi
init|=
name|addRenewOperation
argument_list|(
name|ii
argument_list|,
name|namespace
argument_list|,
name|ns
argument_list|)
decl_stmt|;
name|si
operator|.
name|setInterface
argument_list|(
name|ii
argument_list|)
expr_stmt|;
name|service
operator|=
operator|new
name|ServiceImpl
argument_list|(
name|si
argument_list|)
expr_stmt|;
name|BindingFactoryManager
name|bfm
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
name|BindingFactory
name|bindingFactory
init|=
name|bfm
operator|.
name|getBindingFactory
argument_list|(
name|soapVersion
argument_list|)
decl_stmt|;
name|BindingInfo
name|bi
init|=
name|bindingFactory
operator|.
name|createBindingInfo
argument_list|(
name|service
argument_list|,
name|soapVersion
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|si
operator|.
name|addBinding
argument_list|(
name|bi
argument_list|)
expr_stmt|;
if|if
condition|(
name|transportId
operator|==
literal|null
condition|)
block|{
name|ConduitInitiatorManager
name|cim
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
name|ConduitInitiator
name|ci
init|=
name|cim
operator|.
name|getConduitInitiatorForUri
argument_list|(
name|location
argument_list|)
decl_stmt|;
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
name|EndpointInfo
name|ei
init|=
operator|new
name|EndpointInfo
argument_list|(
name|si
argument_list|,
name|transportId
argument_list|)
decl_stmt|;
name|ei
operator|.
name|setBinding
argument_list|(
name|bi
argument_list|)
expr_stmt|;
name|ei
operator|.
name|setName
argument_list|(
name|epName
operator|==
literal|null
condition|?
name|iName
else|:
name|epName
argument_list|)
expr_stmt|;
name|ei
operator|.
name|setAddress
argument_list|(
name|location
argument_list|)
expr_stmt|;
name|si
operator|.
name|addEndpoint
argument_list|(
name|ei
argument_list|)
expr_stmt|;
if|if
condition|(
name|policy
operator|!=
literal|null
condition|)
block|{
name|ei
operator|.
name|addExtensor
argument_list|(
name|policy
argument_list|)
expr_stmt|;
block|}
name|BindingOperationInfo
name|boi
init|=
name|bi
operator|.
name|getOperation
argument_list|(
name|ioi
argument_list|)
decl_stmt|;
name|SoapOperationInfo
name|soi
init|=
name|boi
operator|.
name|getExtensor
argument_list|(
name|SoapOperationInfo
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|soi
operator|==
literal|null
condition|)
block|{
name|soi
operator|=
operator|new
name|SoapOperationInfo
argument_list|()
expr_stmt|;
name|boi
operator|.
name|addExtensor
argument_list|(
name|soi
argument_list|)
expr_stmt|;
block|}
name|soi
operator|.
name|setAction
argument_list|(
name|namespace
operator|+
operator|(
name|sc
condition|?
literal|"/RST/SCT"
else|:
literal|"/RST/Issue"
operator|)
argument_list|)
expr_stmt|;
name|boi
operator|=
name|bi
operator|.
name|getOperation
argument_list|(
name|coi
argument_list|)
expr_stmt|;
name|soi
operator|=
name|boi
operator|.
name|getExtensor
argument_list|(
name|SoapOperationInfo
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|soi
operator|==
literal|null
condition|)
block|{
name|soi
operator|=
operator|new
name|SoapOperationInfo
argument_list|()
expr_stmt|;
name|boi
operator|.
name|addExtensor
argument_list|(
name|soi
argument_list|)
expr_stmt|;
block|}
name|soi
operator|.
name|setAction
argument_list|(
name|namespace
operator|+
operator|(
name|sc
condition|?
literal|"/RST/SCT/Cancel"
else|:
literal|"/RST/Cancel"
operator|)
argument_list|)
expr_stmt|;
name|boi
operator|=
name|bi
operator|.
name|getOperation
argument_list|(
name|roi
argument_list|)
expr_stmt|;
name|soi
operator|=
name|boi
operator|.
name|getExtensor
argument_list|(
name|SoapOperationInfo
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|soi
operator|==
literal|null
condition|)
block|{
name|soi
operator|=
operator|new
name|SoapOperationInfo
argument_list|()
expr_stmt|;
name|boi
operator|.
name|addExtensor
argument_list|(
name|soi
argument_list|)
expr_stmt|;
block|}
name|soi
operator|.
name|setAction
argument_list|(
name|namespace
operator|+
operator|(
name|sc
condition|?
literal|"/RST/SCT/Renew"
else|:
literal|"/RST/Renew"
operator|)
argument_list|)
expr_stmt|;
name|service
operator|.
name|setDataBinding
argument_list|(
operator|new
name|SourceDataBinding
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|new
name|EndpointImpl
argument_list|(
name|bus
argument_list|,
name|service
argument_list|,
name|ei
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|OperationInfo
name|addIssueOperation
parameter_list|(
name|InterfaceInfo
name|ii
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|servNamespace
parameter_list|)
block|{
name|OperationInfo
name|oi
init|=
name|ii
operator|.
name|addOperation
argument_list|(
operator|new
name|QName
argument_list|(
name|servNamespace
argument_list|,
literal|"RequestSecurityToken"
argument_list|)
argument_list|)
decl_stmt|;
name|MessageInfo
name|mii
init|=
name|oi
operator|.
name|createMessage
argument_list|(
operator|new
name|QName
argument_list|(
name|servNamespace
argument_list|,
literal|"RequestSecurityTokenMsg"
argument_list|)
argument_list|,
name|MessageInfo
operator|.
name|Type
operator|.
name|INPUT
argument_list|)
decl_stmt|;
name|oi
operator|.
name|setInput
argument_list|(
literal|"RequestSecurityTokenMsg"
argument_list|,
name|mii
argument_list|)
expr_stmt|;
name|MessagePartInfo
name|mpi
init|=
name|mii
operator|.
name|addMessagePart
argument_list|(
literal|"request"
argument_list|)
decl_stmt|;
name|mpi
operator|.
name|setElementQName
argument_list|(
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
literal|"RequestSecurityToken"
argument_list|)
argument_list|)
expr_stmt|;
name|MessageInfo
name|mio
init|=
name|oi
operator|.
name|createMessage
argument_list|(
operator|new
name|QName
argument_list|(
name|servNamespace
argument_list|,
literal|"RequestSecurityTokenResponseMsg"
argument_list|)
argument_list|,
name|MessageInfo
operator|.
name|Type
operator|.
name|OUTPUT
argument_list|)
decl_stmt|;
name|oi
operator|.
name|setOutput
argument_list|(
literal|"RequestSecurityTokenResponseMsg"
argument_list|,
name|mio
argument_list|)
expr_stmt|;
name|mpi
operator|=
name|mio
operator|.
name|addMessagePart
argument_list|(
literal|"response"
argument_list|)
expr_stmt|;
if|if
condition|(
name|WST_NS_05_02
operator|.
name|equals
argument_list|(
name|namespace
argument_list|)
condition|)
block|{
name|mpi
operator|.
name|setElementQName
argument_list|(
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
literal|"RequestSecurityTokenResponse"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|mpi
operator|.
name|setElementQName
argument_list|(
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
literal|"RequestSecurityTokenResponseCollection"
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|oi
return|;
block|}
specifier|private
specifier|static
name|OperationInfo
name|addCancelOperation
parameter_list|(
name|InterfaceInfo
name|ii
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|servNamespace
parameter_list|)
block|{
name|OperationInfo
name|oi
init|=
name|ii
operator|.
name|addOperation
argument_list|(
operator|new
name|QName
argument_list|(
name|servNamespace
argument_list|,
literal|"CancelSecurityToken"
argument_list|)
argument_list|)
decl_stmt|;
name|MessageInfo
name|mii
init|=
name|oi
operator|.
name|createMessage
argument_list|(
operator|new
name|QName
argument_list|(
name|servNamespace
argument_list|,
literal|"CancelSecurityTokenMsg"
argument_list|)
argument_list|,
name|MessageInfo
operator|.
name|Type
operator|.
name|INPUT
argument_list|)
decl_stmt|;
name|oi
operator|.
name|setInput
argument_list|(
literal|"CancelSecurityTokenMsg"
argument_list|,
name|mii
argument_list|)
expr_stmt|;
name|MessagePartInfo
name|mpi
init|=
name|mii
operator|.
name|addMessagePart
argument_list|(
literal|"request"
argument_list|)
decl_stmt|;
name|mpi
operator|.
name|setElementQName
argument_list|(
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
literal|"RequestSecurityToken"
argument_list|)
argument_list|)
expr_stmt|;
name|MessageInfo
name|mio
init|=
name|oi
operator|.
name|createMessage
argument_list|(
operator|new
name|QName
argument_list|(
name|servNamespace
argument_list|,
literal|"CancelSecurityTokenResponseMsg"
argument_list|)
argument_list|,
name|MessageInfo
operator|.
name|Type
operator|.
name|OUTPUT
argument_list|)
decl_stmt|;
name|oi
operator|.
name|setOutput
argument_list|(
literal|"CancelSecurityTokenResponseMsg"
argument_list|,
name|mio
argument_list|)
expr_stmt|;
name|mpi
operator|=
name|mio
operator|.
name|addMessagePart
argument_list|(
literal|"response"
argument_list|)
expr_stmt|;
if|if
condition|(
name|WST_NS_05_02
operator|.
name|equals
argument_list|(
name|namespace
argument_list|)
condition|)
block|{
name|mpi
operator|.
name|setElementQName
argument_list|(
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
literal|"RequestSecurityTokenResponse"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|mpi
operator|.
name|setElementQName
argument_list|(
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
literal|"RequestSecurityTokenResponseCollection"
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|oi
return|;
block|}
specifier|private
specifier|static
name|OperationInfo
name|addRenewOperation
parameter_list|(
name|InterfaceInfo
name|ii
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|servNamespace
parameter_list|)
block|{
name|OperationInfo
name|oi
init|=
name|ii
operator|.
name|addOperation
argument_list|(
operator|new
name|QName
argument_list|(
name|servNamespace
argument_list|,
literal|"RenewSecurityToken"
argument_list|)
argument_list|)
decl_stmt|;
name|MessageInfo
name|mii
init|=
name|oi
operator|.
name|createMessage
argument_list|(
operator|new
name|QName
argument_list|(
name|servNamespace
argument_list|,
literal|"RenewSecurityTokenMsg"
argument_list|)
argument_list|,
name|MessageInfo
operator|.
name|Type
operator|.
name|INPUT
argument_list|)
decl_stmt|;
name|oi
operator|.
name|setInput
argument_list|(
literal|"RenewSecurityTokenMsg"
argument_list|,
name|mii
argument_list|)
expr_stmt|;
name|MessagePartInfo
name|mpi
init|=
name|mii
operator|.
name|addMessagePart
argument_list|(
literal|"request"
argument_list|)
decl_stmt|;
name|mpi
operator|.
name|setElementQName
argument_list|(
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
literal|"RequestSecurityToken"
argument_list|)
argument_list|)
expr_stmt|;
name|MessageInfo
name|mio
init|=
name|oi
operator|.
name|createMessage
argument_list|(
operator|new
name|QName
argument_list|(
name|servNamespace
argument_list|,
literal|"RenewSecurityTokenResponseMsg"
argument_list|)
argument_list|,
name|MessageInfo
operator|.
name|Type
operator|.
name|OUTPUT
argument_list|)
decl_stmt|;
name|oi
operator|.
name|setOutput
argument_list|(
literal|"RenewSecurityTokenResponseMsg"
argument_list|,
name|mio
argument_list|)
expr_stmt|;
name|mpi
operator|=
name|mio
operator|.
name|addMessagePart
argument_list|(
literal|"response"
argument_list|)
expr_stmt|;
if|if
condition|(
name|WST_NS_05_02
operator|.
name|equals
argument_list|(
name|namespace
argument_list|)
condition|)
block|{
name|mpi
operator|.
name|setElementQName
argument_list|(
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
literal|"RequestSecurityTokenResponse"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|mpi
operator|.
name|setElementQName
argument_list|(
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
literal|"RequestSecurityTokenResponseCollection"
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|oi
return|;
block|}
block|}
end_class

end_unit

