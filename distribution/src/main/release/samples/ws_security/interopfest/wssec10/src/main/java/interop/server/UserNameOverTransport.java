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

begin_class
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
literal|"PingService10"
argument_list|,
name|portName
operator|=
literal|"UserNameOverTransportLocal_IPingService"
argument_list|,
name|endpointInterface
operator|=
literal|"interopbaseaddress.interop.IPingService"
argument_list|,
name|wsdlLocation
operator|=
literal|"target/wsdl2/WsSecurity10.wsdl"
argument_list|)
specifier|public
class|class
name|UserNameOverTransport
extends|extends
name|PingServiceBase
block|{
comment|// complete
block|}
end_class

end_unit

