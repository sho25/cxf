begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|interop
operator|.
name|server
package|;
end_package

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
name|interop
operator|.
name|client
operator|.
name|KeystorePasswordCallback
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
name|spring
operator|.
name|SpringBusFactory
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

begin_class
specifier|public
class|class
name|Server
block|{
specifier|protected
name|Server
parameter_list|(
name|String
name|baseUrl
parameter_list|)
throws|throws
name|Exception
block|{
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|(
literal|"etc/server.xml"
argument_list|)
expr_stmt|;
comment|//"SecureConversation_UserNameOverTransport_IPingService",
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"SecureConversation_MutualCertificate10SignEncrypt_IPingService"
argument_list|,
operator|new
name|SCMCSEIPingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"AC_IPingService"
argument_list|,
operator|new
name|ACIPingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"ADC_IPingService"
argument_list|,
operator|new
name|ADCIPingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"ADC-ES_IPingService"
argument_list|,
operator|new
name|ADCESIPingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"_A_IPingService"
argument_list|,
operator|new
name|AIPingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"_AD_IPingService"
argument_list|,
operator|new
name|ADIPingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"_AD-ES_IPingService"
argument_list|,
operator|new
name|ADESIPingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"UXC_IPingService"
argument_list|,
operator|new
name|UXCIPingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"UXDC_IPingService"
argument_list|,
operator|new
name|UXDCIPingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"UXDC-SEES_IPingService"
argument_list|,
operator|new
name|UXDCSEESIPingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"_UX_IPingService"
argument_list|,
operator|new
name|UXIPingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"_UXD_IPingService"
argument_list|,
operator|new
name|UXDIPingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"_UXD-SEES_IPingService"
argument_list|,
operator|new
name|UXDSEESIPingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"XC_IPingService"
argument_list|,
operator|new
name|XCIPingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"XDC_IPingService"
argument_list|,
operator|new
name|XDCIPingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"XDC_IPingService1"
argument_list|,
operator|new
name|XDC1IPingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"XDC-ES_IPingService"
argument_list|,
operator|new
name|XDCESIPingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"XDC-SEES_IPingService"
argument_list|,
operator|new
name|XDCSEESIPingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"_X_IPingService"
argument_list|,
operator|new
name|XIPingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"_X10_IPingService"
argument_list|,
operator|new
name|X10IPingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"_XD_IPingService"
argument_list|,
operator|new
name|XDIPingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"_XD-SEES_IPingService"
argument_list|,
operator|new
name|XDSEESIPingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"_XD-ES_IPingService"
argument_list|,
operator|new
name|XDESIPingService
argument_list|()
argument_list|)
expr_stmt|;
comment|//Kerberos token - not sure where the token comes from or how these work
comment|//"KC_IPingService",
comment|//"KDC_IPingService",
comment|//"KC10_IPingService",
comment|//"KDC10_IPingService",
comment|//"_K10_IPingService",
comment|//"_KD10_IPingService",
comment|//"_K_IPingService",
comment|//"_KD_IPingService",
comment|//"_KD-SEES_IPingService",
block|}
specifier|private
name|void
name|doPublish
parameter_list|(
name|String
name|url
parameter_list|,
name|Object
name|obj
parameter_list|)
block|{
name|Endpoint
name|ep
init|=
name|Endpoint
operator|.
name|create
argument_list|(
name|obj
argument_list|)
decl_stmt|;
name|ep
operator|.
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|CALLBACK_HANDLER
operator|+
literal|".sct"
argument_list|,
operator|new
name|KeystorePasswordCallback
argument_list|()
argument_list|)
expr_stmt|;
name|ep
operator|.
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|ENCRYPT_PROPERTIES
operator|+
literal|".sct"
argument_list|,
literal|"etc/bob.properties"
argument_list|)
expr_stmt|;
if|if
condition|(
name|url
operator|.
name|contains
argument_list|(
literal|"X10_I"
argument_list|)
condition|)
block|{
name|ep
operator|.
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|SIGNATURE_PROPERTIES
operator|+
literal|".sct"
argument_list|,
literal|"etc/bob.properties"
argument_list|)
expr_stmt|;
name|ep
operator|.
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|ENCRYPT_PROPERTIES
operator|+
literal|".sct"
argument_list|,
literal|"etc/alice.properties"
argument_list|)
expr_stmt|;
block|}
name|ep
operator|.
name|publish
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
name|args
index|[]
parameter_list|)
throws|throws
name|Exception
block|{
operator|new
name|Server
argument_list|(
literal|"http://localhost:9001/"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Server ready..."
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|60
operator|*
literal|60
operator|*
literal|10000
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Server exiting"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|javax
operator|.
name|jws
operator|.
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://InteropBaseAddress/interop"
argument_list|,
name|serviceName
operator|=
literal|"PingService"
argument_list|,
name|portName
operator|=
literal|"SecureConversation_MutualCertificate10SignEncrypt_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"interopbaseaddress.interop.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/wsdl2/WSSecureConversation.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|SCMCSEIPingService
extends|extends
name|PingServiceImpl
block|{     }
annotation|@
name|javax
operator|.
name|jws
operator|.
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://InteropBaseAddress/interop"
argument_list|,
name|serviceName
operator|=
literal|"PingService"
argument_list|,
name|portName
operator|=
literal|"AC_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"interopbaseaddress.interop.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/wsdl2/WSSecureConversation.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|ACIPingService
extends|extends
name|PingServiceImpl
block|{     }
annotation|@
name|javax
operator|.
name|jws
operator|.
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://InteropBaseAddress/interop"
argument_list|,
name|serviceName
operator|=
literal|"PingService"
argument_list|,
name|portName
operator|=
literal|"ADC_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"interopbaseaddress.interop.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/wsdl2/WSSecureConversation.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|ADCIPingService
extends|extends
name|PingServiceImpl
block|{     }
annotation|@
name|javax
operator|.
name|jws
operator|.
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://InteropBaseAddress/interop"
argument_list|,
name|serviceName
operator|=
literal|"PingService"
argument_list|,
name|portName
operator|=
literal|"ADC-ES_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"interopbaseaddress.interop.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/wsdl2/WSSecureConversation.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|ADCESIPingService
extends|extends
name|PingServiceImpl
block|{     }
annotation|@
name|javax
operator|.
name|jws
operator|.
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://InteropBaseAddress/interop"
argument_list|,
name|serviceName
operator|=
literal|"PingService"
argument_list|,
name|portName
operator|=
literal|"_A_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"interopbaseaddress.interop.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/wsdl2/WSSecureConversation.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|AIPingService
extends|extends
name|PingServiceImpl
block|{     }
annotation|@
name|javax
operator|.
name|jws
operator|.
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://InteropBaseAddress/interop"
argument_list|,
name|serviceName
operator|=
literal|"PingService"
argument_list|,
name|portName
operator|=
literal|"_AD_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"interopbaseaddress.interop.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/wsdl2/WSSecureConversation.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|ADIPingService
extends|extends
name|PingServiceImpl
block|{     }
annotation|@
name|javax
operator|.
name|jws
operator|.
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://InteropBaseAddress/interop"
argument_list|,
name|serviceName
operator|=
literal|"PingService"
argument_list|,
name|portName
operator|=
literal|"_AD-ES_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"interopbaseaddress.interop.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/wsdl2/WSSecureConversation.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|ADESIPingService
extends|extends
name|PingServiceImpl
block|{     }
annotation|@
name|javax
operator|.
name|jws
operator|.
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://InteropBaseAddress/interop"
argument_list|,
name|serviceName
operator|=
literal|"PingService"
argument_list|,
name|portName
operator|=
literal|"UXC_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"interopbaseaddress.interop.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/wsdl2/WSSecureConversation.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|UXCIPingService
extends|extends
name|PingServiceImpl
block|{     }
annotation|@
name|javax
operator|.
name|jws
operator|.
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://InteropBaseAddress/interop"
argument_list|,
name|serviceName
operator|=
literal|"PingService"
argument_list|,
name|portName
operator|=
literal|"UXDC_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"interopbaseaddress.interop.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/wsdl2/WSSecureConversation.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|UXDCIPingService
extends|extends
name|PingServiceImpl
block|{     }
annotation|@
name|javax
operator|.
name|jws
operator|.
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://InteropBaseAddress/interop"
argument_list|,
name|serviceName
operator|=
literal|"PingService"
argument_list|,
name|portName
operator|=
literal|"UXDC-SEES_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"interopbaseaddress.interop.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/wsdl2/WSSecureConversation.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|UXDCSEESIPingService
extends|extends
name|PingServiceImpl
block|{     }
annotation|@
name|javax
operator|.
name|jws
operator|.
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://InteropBaseAddress/interop"
argument_list|,
name|serviceName
operator|=
literal|"PingService"
argument_list|,
name|portName
operator|=
literal|"_UX_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"interopbaseaddress.interop.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/wsdl2/WSSecureConversation.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|UXIPingService
extends|extends
name|PingServiceImpl
block|{     }
annotation|@
name|javax
operator|.
name|jws
operator|.
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://InteropBaseAddress/interop"
argument_list|,
name|serviceName
operator|=
literal|"PingService"
argument_list|,
name|portName
operator|=
literal|"_UXD_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"interopbaseaddress.interop.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/wsdl2/WSSecureConversation.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|UXDIPingService
extends|extends
name|PingServiceImpl
block|{     }
annotation|@
name|javax
operator|.
name|jws
operator|.
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://InteropBaseAddress/interop"
argument_list|,
name|serviceName
operator|=
literal|"PingService"
argument_list|,
name|portName
operator|=
literal|"_UXD-SEES_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"interopbaseaddress.interop.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/wsdl2/WSSecureConversation.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|UXDSEESIPingService
extends|extends
name|PingServiceImpl
block|{     }
annotation|@
name|javax
operator|.
name|jws
operator|.
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://InteropBaseAddress/interop"
argument_list|,
name|serviceName
operator|=
literal|"PingService"
argument_list|,
name|portName
operator|=
literal|"XC_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"interopbaseaddress.interop.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/wsdl2/WSSecureConversation.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|XCIPingService
extends|extends
name|PingServiceImpl
block|{     }
annotation|@
name|javax
operator|.
name|jws
operator|.
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://InteropBaseAddress/interop"
argument_list|,
name|serviceName
operator|=
literal|"PingService"
argument_list|,
name|portName
operator|=
literal|"XDC_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"interopbaseaddress.interop.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/wsdl2/WSSecureConversation.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|XDCIPingService
extends|extends
name|PingServiceImpl
block|{     }
annotation|@
name|javax
operator|.
name|jws
operator|.
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://InteropBaseAddress/interop"
argument_list|,
name|serviceName
operator|=
literal|"PingService"
argument_list|,
name|portName
operator|=
literal|"XDC_IPingService1"
argument_list|,
name|endpointInterface
operator|=
literal|"interopbaseaddress.interop.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/wsdl2/WSSecureConversation.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|XDC1IPingService
extends|extends
name|PingServiceImpl
block|{     }
annotation|@
name|javax
operator|.
name|jws
operator|.
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://InteropBaseAddress/interop"
argument_list|,
name|serviceName
operator|=
literal|"PingService"
argument_list|,
name|portName
operator|=
literal|"XDC-ES_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"interopbaseaddress.interop.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/wsdl2/WSSecureConversation.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|XDCESIPingService
extends|extends
name|PingServiceImpl
block|{     }
annotation|@
name|javax
operator|.
name|jws
operator|.
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://InteropBaseAddress/interop"
argument_list|,
name|serviceName
operator|=
literal|"PingService"
argument_list|,
name|portName
operator|=
literal|"XDC-SEES_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"interopbaseaddress.interop.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/wsdl2/WSSecureConversation.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|XDCSEESIPingService
extends|extends
name|PingServiceImpl
block|{     }
annotation|@
name|javax
operator|.
name|jws
operator|.
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://InteropBaseAddress/interop"
argument_list|,
name|serviceName
operator|=
literal|"PingService"
argument_list|,
name|portName
operator|=
literal|"_X_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"interopbaseaddress.interop.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/wsdl2/WSSecureConversation.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|XIPingService
extends|extends
name|PingServiceImpl
block|{     }
annotation|@
name|javax
operator|.
name|jws
operator|.
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://InteropBaseAddress/interop"
argument_list|,
name|serviceName
operator|=
literal|"PingService"
argument_list|,
name|portName
operator|=
literal|"_X10_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"interopbaseaddress.interop.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/wsdl2/WSSecureConversation.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|X10IPingService
extends|extends
name|PingServiceImpl
block|{     }
annotation|@
name|javax
operator|.
name|jws
operator|.
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://InteropBaseAddress/interop"
argument_list|,
name|serviceName
operator|=
literal|"PingService"
argument_list|,
name|portName
operator|=
literal|"_XD_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"interopbaseaddress.interop.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/wsdl2/WSSecureConversation.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|XDIPingService
extends|extends
name|PingServiceImpl
block|{     }
annotation|@
name|javax
operator|.
name|jws
operator|.
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://InteropBaseAddress/interop"
argument_list|,
name|serviceName
operator|=
literal|"PingService"
argument_list|,
name|portName
operator|=
literal|"_XD-SEES_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"interopbaseaddress.interop.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/wsdl2/WSSecureConversation.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|XDSEESIPingService
extends|extends
name|PingServiceImpl
block|{     }
annotation|@
name|javax
operator|.
name|jws
operator|.
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://InteropBaseAddress/interop"
argument_list|,
name|serviceName
operator|=
literal|"PingService"
argument_list|,
name|portName
operator|=
literal|"_XD-ES_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"interopbaseaddress.interop.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/wsdl2/WSSecureConversation.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|XDESIPingService
extends|extends
name|PingServiceImpl
block|{     }
block|}
end_class

end_unit

