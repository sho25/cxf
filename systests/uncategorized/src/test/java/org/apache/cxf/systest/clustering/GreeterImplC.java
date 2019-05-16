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
name|clustering
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
name|cxf
operator|.
name|greeter_control
operator|.
name|AbstractGreeterImpl
import|;
end_import

begin_class
annotation|@
name|WebService
argument_list|(
name|serviceName
operator|=
literal|"GreeterService"
argument_list|,
name|portName
operator|=
literal|"ReplicatedPortC"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.cxf.greeter_control.Greeter"
argument_list|,
name|targetNamespace
operator|=
literal|"http://cxf.apache.org/greeter_control"
argument_list|,
name|wsdlLocation
operator|=
literal|"wsdl/greeter_control.wsdl"
argument_list|)
specifier|public
class|class
name|GreeterImplC
extends|extends
name|AbstractGreeterImpl
block|{
specifier|private
name|String
name|address
decl_stmt|;
name|GreeterImplC
parameter_list|()
block|{
name|address
operator|=
name|FailoverTest
operator|.
name|REPLICA_C
expr_stmt|;
block|}
specifier|public
name|String
name|greetMe
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
name|super
operator|.
name|greetMe
argument_list|(
name|s
argument_list|)
operator|+
literal|" from: "
operator|+
name|address
return|;
block|}
block|}
end_class

end_unit

