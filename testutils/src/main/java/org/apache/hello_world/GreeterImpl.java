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
name|hello_world
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
name|org
operator|.
name|apache
operator|.
name|hello_world
operator|.
name|messages
operator|.
name|PingMeFault
import|;
end_import

begin_class
annotation|@
name|WebService
argument_list|(
name|serviceName
operator|=
literal|"SOAPService"
argument_list|,
name|portName
operator|=
literal|"SoapPort"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.hello_world.Greeter"
argument_list|,
name|targetNamespace
operator|=
literal|"http://apache.org/hello_world/services"
argument_list|,
name|wsdlLocation
operator|=
literal|"testutils/others/hello_world_services_catalog.wsdl"
argument_list|)
specifier|public
class|class
name|GreeterImpl
implements|implements
name|Greeter
block|{
specifier|public
name|GreeterImpl
parameter_list|()
block|{     }
specifier|public
name|String
name|greetMe
parameter_list|(
name|String
name|requestType
parameter_list|)
block|{
return|return
literal|"Hello "
operator|+
name|requestType
return|;
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|void
name|pingMe
parameter_list|()
throws|throws
name|PingMeFault
block|{     }
comment|/** {@inheritDoc}*/
specifier|public
name|String
name|sayHi
parameter_list|()
block|{
return|return
literal|"Hi!"
return|;
block|}
block|}
end_class

end_unit

