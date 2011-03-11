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
expr_stmt|;
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
block|}
return|return
name|client
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
literal|"/RST/Issue"
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
literal|"/RST/Cancel"
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
literal|"CancelSecurityToken"
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
name|mpi
operator|.
name|setElementQName
argument_list|(
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
literal|"CancelSecurityTokenResponse"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|oi
return|;
block|}
block|}
end_class

end_unit

