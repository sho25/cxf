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
name|soapfault
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|HandlerChain
import|;
end_import

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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|soapfault
operator|.
name|SoapFaultPortType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xmlsoap
operator|.
name|schemas
operator|.
name|soap
operator|.
name|envelope
operator|.
name|Fault
import|;
end_import

begin_class
annotation|@
name|WebService
argument_list|(
name|endpointInterface
operator|=
literal|"org.apache.cxf.soapfault.SoapFaultPortType"
argument_list|,
name|serviceName
operator|=
literal|"SoapFaultService"
argument_list|)
annotation|@
name|HandlerChain
argument_list|(
name|file
operator|=
literal|"./handlers.xml"
argument_list|,
name|name
operator|=
literal|"TestHandlerChain"
argument_list|)
specifier|public
class|class
name|SOAPFaultImpl
implements|implements
name|SoapFaultPortType
block|{
specifier|public
name|void
name|soapFault
parameter_list|(
name|Fault
name|fault
parameter_list|)
block|{
comment|//System.out.println("Received soap fault message");
comment|//System.out.println("FaultString: " + fault.getFaultstring());
comment|//System.out.println("FaulCode: " + fault.getFaultcode());
block|}
block|}
end_class

end_unit

