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
name|jms
operator|.
name|testsuite
operator|.
name|services
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jms_greeter
operator|.
name|JMSGreeterPortType
import|;
end_import

begin_class
annotation|@
name|javax
operator|.
name|jws
operator|.
name|WebService
argument_list|(
name|portName
operator|=
literal|"GreeterPort2"
argument_list|,
name|serviceName
operator|=
literal|"JMSGreeterService2"
argument_list|,
name|targetNamespace
operator|=
literal|"http://cxf.apache.org/jms_greeter"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.cxf.jms_greeter.JMSGreeterPortType"
argument_list|,
name|wsdlLocation
operator|=
literal|"testutils/jms_spec_test.wsdl"
argument_list|)
specifier|public
class|class
name|GreeterSpecWithPortError
implements|implements
name|JMSGreeterPortType
block|{
specifier|public
name|String
name|greetMe
parameter_list|(
name|String
name|me
parameter_list|)
block|{
comment|//System.out.println("Executing operation greetMe");
comment|//System.out.println("Message received: " + me + "\n");
return|return
literal|"Hello "
operator|+
name|me
return|;
block|}
specifier|public
name|String
name|sayHi
parameter_list|()
block|{
comment|//System.out.println("Executing operation sayHi" + "\n");
return|return
literal|"Bonjour"
return|;
block|}
specifier|public
name|void
name|greetMeOneWay
parameter_list|(
name|String
name|me
parameter_list|)
block|{
comment|//System.out.println("Executing operation greetMeOneWay\n");
comment|//System.out.println("Hello there " + me);
block|}
block|}
end_class

end_unit

