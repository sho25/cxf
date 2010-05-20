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
name|addressing
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

begin_comment
comment|/**  * A container for WS-Addressing constants.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|JAXWSAConstants
block|{
specifier|public
specifier|static
specifier|final
name|String
name|WSAW_PREFIX
init|=
literal|"wsaw"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NS_WSAW
init|=
literal|"http://www.w3.org/2006/05/addressing/wsdl"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSAM_PREFIX
init|=
literal|"wsam"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NS_WSAM
init|=
literal|"http://www.w3.org/2007/05/addressing/metadata"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|WSAW_ACTION_QNAME
init|=
operator|new
name|QName
argument_list|(
name|NS_WSAW
argument_list|,
literal|"Action"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|WSAM_ACTION_QNAME
init|=
operator|new
name|QName
argument_list|(
name|NS_WSAM
argument_list|,
literal|"Action"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|WSAW_USINGADDRESSING_QNAME
init|=
operator|new
name|QName
argument_list|(
name|NS_WSAW
argument_list|,
literal|"UsingAddressing"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NS_WSA
init|=
literal|"http://www.w3.org/2005/08/addressing"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSA_PREFIX
init|=
literal|"wsa"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSA_XSD
init|=
literal|"http://www.w3.org/2006/03/addressing/ws-addr.xsd"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSA_ERF_NAME
init|=
literal|"EndpointReference"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSA_REFERENCEPARAMETERS_NAME
init|=
literal|"ReferenceParameters"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSA_METADATA_NAME
init|=
literal|"Metadata"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSA_ADDRESS_NAME
init|=
literal|"Address"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSAM_SERVICENAME_NAME
init|=
literal|"ServiceName"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSAM_INTERFACE_NAME
init|=
literal|"InterfaceName"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSAM_ENDPOINT_NAME
init|=
literal|"EndpointName"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSDLI_PFX
init|=
literal|"wsdli"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSDLI_WSDLLOCATION
init|=
literal|"wsdlLocation"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NS_WSDLI
init|=
literal|"http://www.w3.org/ns/wsdl-instance"
decl_stmt|;
comment|/**      * Well-known Property names for AddressingProperties in BindingProvider      * Context.      */
specifier|public
specifier|static
specifier|final
name|String
name|CLIENT_ADDRESSING_PROPERTIES
init|=
literal|"javax.xml.ws.addressing.context"
decl_stmt|;
comment|/**      * Well-known Property names for AddressingProperties in Handler      * Context.      */
specifier|public
specifier|static
specifier|final
name|String
name|CLIENT_ADDRESSING_PROPERTIES_INBOUND
init|=
literal|"javax.xml.ws.addressing.context.inbound"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CLIENT_ADDRESSING_PROPERTIES_OUTBOUND
init|=
literal|"javax.xml.ws.addressing.context.outbound"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SERVER_ADDRESSING_PROPERTIES_INBOUND
init|=
literal|"javax.xml.ws.addressing.context.inbound"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SERVER_ADDRESSING_PROPERTIES_OUTBOUND
init|=
literal|"javax.xml.ws.addressing.context.outbound"
decl_stmt|;
comment|/**      * Used by AddressingBuilder factory method.      */
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_ADDRESSING_BUILDER
init|=
literal|"org.apache.cxf.ws.addressing.AddressingBuilderImpl"
decl_stmt|;
comment|/**      * Prevents instantiation.       */
specifier|private
name|JAXWSAConstants
parameter_list|()
block|{     }
block|}
end_class

end_unit

