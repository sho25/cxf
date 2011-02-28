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
name|ws
operator|.
name|wssec11
operator|.
name|server
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
name|ws
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
name|testutil
operator|.
name|common
operator|.
name|AbstractBusTestServerBase
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
extends|extends
name|AbstractBusTestServerBase
block|{
specifier|static
specifier|final
name|String
name|PORT
init|=
name|allocatePort
argument_list|(
name|Server
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|Server
parameter_list|()
throws|throws
name|Exception
block|{
name|this
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Server
parameter_list|(
name|String
name|baseUrl
parameter_list|)
throws|throws
name|Exception
block|{
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"/APingService"
argument_list|,
operator|new
name|APingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"/A-NoTimestampPingService"
argument_list|,
operator|new
name|ANoTimestampPingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"/ADPingService"
argument_list|,
operator|new
name|ADPingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"/A-ESPingService"
argument_list|,
operator|new
name|AESPingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"/AD-ESPingService"
argument_list|,
operator|new
name|ADESPingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"/UXPingService"
argument_list|,
operator|new
name|UXPingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"/UX-NoTimestampPingService"
argument_list|,
operator|new
name|UXNoTimestampPingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"/UXDPingService"
argument_list|,
operator|new
name|UXDPingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"/UX-SEESPingService"
argument_list|,
operator|new
name|UXSEESPingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"/UXD-SEESPingService"
argument_list|,
operator|new
name|UXDSEESPingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"/XPingService"
argument_list|,
operator|new
name|XPingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"/X-NoTimestampPingService"
argument_list|,
operator|new
name|XNoTimestampPingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"/X-AES128PingService"
argument_list|,
operator|new
name|XAES128PingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"/X-AES256PingService"
argument_list|,
operator|new
name|XAES256PingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"/X-TripleDESPingService"
argument_list|,
operator|new
name|XTripleDESPingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"/XDPingService"
argument_list|,
operator|new
name|XDPingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"/XD-ESPingService"
argument_list|,
operator|new
name|XDESPingService
argument_list|()
argument_list|)
expr_stmt|;
name|doPublish
argument_list|(
name|baseUrl
operator|+
literal|"/XD-SEESPingService"
argument_list|,
operator|new
name|XDSEESPingService
argument_list|()
argument_list|)
expr_stmt|;
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
argument_list|,
literal|"org/apache/cxf/systest/ws/wssec11/server/bob.properties"
argument_list|)
expr_stmt|;
name|ep
operator|.
name|publish
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|run
parameter_list|()
block|{
name|Bus
name|busLocal
init|=
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|(
literal|"org/apache/cxf/systest/ws/wssec11/server/server.xml"
argument_list|)
decl_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|busLocal
argument_list|)
expr_stmt|;
name|setBus
argument_list|(
name|busLocal
argument_list|)
expr_stmt|;
try|try
block|{
operator|new
name|Server
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
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
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|(
literal|"org/apache/cxf/systest/ws/wssec11/server/server.xml"
argument_list|)
expr_stmt|;
operator|new
name|Server
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
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
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://WSSec/wssec11"
argument_list|,
name|serviceName
operator|=
literal|"PingService11"
argument_list|,
name|portName
operator|=
literal|"A_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"wssec.wssec11.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/test-classes/wsdl_systest_wsspec/wssec11/WsSecurity11.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|APingService
extends|extends
name|PingService
block|{     }
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://WSSec/wssec11"
argument_list|,
name|serviceName
operator|=
literal|"PingService11"
argument_list|,
name|portName
operator|=
literal|"A-NoTimestamp_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"wssec.wssec11.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/test-classes/wsdl_systest_wsspec/wssec11/WsSecurity11.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|ANoTimestampPingService
extends|extends
name|PingService
block|{     }
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://WSSec/wssec11"
argument_list|,
name|serviceName
operator|=
literal|"PingService11"
argument_list|,
name|portName
operator|=
literal|"AD_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"wssec.wssec11.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/test-classes/wsdl_systest_wsspec/wssec11/WsSecurity11.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|ADPingService
extends|extends
name|PingService
block|{     }
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://WSSec/wssec11"
argument_list|,
name|serviceName
operator|=
literal|"PingService11"
argument_list|,
name|portName
operator|=
literal|"A-ES_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"wssec.wssec11.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/test-classes/wsdl_systest_wsspec/wssec11/WsSecurity11.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|AESPingService
extends|extends
name|PingService
block|{     }
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://WSSec/wssec11"
argument_list|,
name|serviceName
operator|=
literal|"PingService11"
argument_list|,
name|portName
operator|=
literal|"AD-ES_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"wssec.wssec11.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/test-classes/wsdl_systest_wsspec/wssec11/WsSecurity11.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|ADESPingService
extends|extends
name|PingService
block|{     }
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://WSSec/wssec11"
argument_list|,
name|serviceName
operator|=
literal|"PingService11"
argument_list|,
name|portName
operator|=
literal|"UX_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"wssec.wssec11.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/test-classes/wsdl_systest_wsspec/wssec11/WsSecurity11.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|UXPingService
extends|extends
name|PingService
block|{     }
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://WSSec/wssec11"
argument_list|,
name|serviceName
operator|=
literal|"PingService11"
argument_list|,
name|portName
operator|=
literal|"UX-NoTimestamp_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"wssec.wssec11.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/test-classes/wsdl_systest_wsspec/wssec11/WsSecurity11.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|UXNoTimestampPingService
extends|extends
name|PingService
block|{     }
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://WSSec/wssec11"
argument_list|,
name|serviceName
operator|=
literal|"PingService11"
argument_list|,
name|portName
operator|=
literal|"UXD_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"wssec.wssec11.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/test-classes/wsdl_systest_wsspec/wssec11/WsSecurity11.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|UXDPingService
extends|extends
name|PingService
block|{     }
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://WSSec/wssec11"
argument_list|,
name|serviceName
operator|=
literal|"PingService11"
argument_list|,
name|portName
operator|=
literal|"UX-SEES_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"wssec.wssec11.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/test-classes/wsdl_systest_wsspec/wssec11/WsSecurity11.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|UXSEESPingService
extends|extends
name|PingService
block|{     }
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://WSSec/wssec11"
argument_list|,
name|serviceName
operator|=
literal|"PingService11"
argument_list|,
name|portName
operator|=
literal|"UXD-SEES_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"wssec.wssec11.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/test-classes/wsdl_systest_wsspec/wssec11/WsSecurity11.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|UXDSEESPingService
extends|extends
name|PingService
block|{     }
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://WSSec/wssec11"
argument_list|,
name|serviceName
operator|=
literal|"PingService11"
argument_list|,
name|portName
operator|=
literal|"X_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"wssec.wssec11.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/test-classes/wsdl_systest_wsspec/wssec11/WsSecurity11.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|XPingService
extends|extends
name|PingService
block|{     }
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://WSSec/wssec11"
argument_list|,
name|serviceName
operator|=
literal|"PingService11"
argument_list|,
name|portName
operator|=
literal|"X-NoTimestamp_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"wssec.wssec11.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/test-classes/wsdl_systest_wsspec/wssec11/WsSecurity11.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|XNoTimestampPingService
extends|extends
name|PingService
block|{     }
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://WSSec/wssec11"
argument_list|,
name|serviceName
operator|=
literal|"PingService11"
argument_list|,
name|portName
operator|=
literal|"XD_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"wssec.wssec11.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/test-classes/wsdl_systest_wsspec/wssec11/WsSecurity11.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|XDPingService
extends|extends
name|PingService
block|{     }
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://WSSec/wssec11"
argument_list|,
name|serviceName
operator|=
literal|"PingService11"
argument_list|,
name|portName
operator|=
literal|"XD-ES_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"wssec.wssec11.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/test-classes/wsdl_systest_wsspec/wssec11/WsSecurity11.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|XDESPingService
extends|extends
name|PingService
block|{     }
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://WSSec/wssec11"
argument_list|,
name|serviceName
operator|=
literal|"PingService11"
argument_list|,
name|portName
operator|=
literal|"XD-SEES_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"wssec.wssec11.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/test-classes/wsdl_systest_wsspec/wssec11/WsSecurity11.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|XDSEESPingService
extends|extends
name|PingService
block|{     }
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://WSSec/wssec11"
argument_list|,
name|serviceName
operator|=
literal|"PingService11"
argument_list|,
name|portName
operator|=
literal|"X-AES128_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"wssec.wssec11.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/test-classes/wsdl_systest_wsspec/wssec11/WsSecurity11.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|XAES128PingService
extends|extends
name|PingService
block|{     }
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://WSSec/wssec11"
argument_list|,
name|serviceName
operator|=
literal|"PingService11"
argument_list|,
name|portName
operator|=
literal|"X-AES256_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"wssec.wssec11.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/test-classes/wsdl_systest_wsspec/wssec11/WsSecurity11.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|XAES256PingService
extends|extends
name|PingService
block|{     }
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://WSSec/wssec11"
argument_list|,
name|serviceName
operator|=
literal|"PingService11"
argument_list|,
name|portName
operator|=
literal|"X-TripleDES_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"wssec.wssec11.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/test-classes/wsdl_systest_wsspec/wssec11/WsSecurity11.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|XTripleDESPingService
extends|extends
name|PingService
block|{     }
block|}
end_class

end_unit

