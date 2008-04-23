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
name|tools
operator|.
name|fortest
operator|.
name|addr
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
name|Action
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
name|BindingType
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
name|Holder
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
name|soap
operator|.
name|Addressing
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
name|soap
operator|.
name|SOAPBinding
import|;
end_import

begin_class
annotation|@
name|WebService
argument_list|(
name|name
operator|=
literal|"AddressingFeatureTest2"
argument_list|,
name|portName
operator|=
literal|"AddressingFeatureTest2Port"
argument_list|,
name|targetNamespace
operator|=
literal|"http://addressingfeatureservice.org/wsdl"
argument_list|,
name|serviceName
operator|=
literal|"AddressingFeatureTest2Service"
argument_list|)
annotation|@
name|BindingType
argument_list|(
name|value
operator|=
name|SOAPBinding
operator|.
name|SOAP11HTTP_BINDING
argument_list|)
annotation|@
name|Addressing
argument_list|(
name|enabled
operator|=
literal|true
argument_list|,
name|required
operator|=
literal|true
argument_list|)
specifier|public
class|class
name|WSAImpl2
block|{
annotation|@
name|Action
argument_list|(
name|input
operator|=
literal|"inputAction"
argument_list|,
name|output
operator|=
literal|"outputAction"
argument_list|)
specifier|public
name|int
name|addNumbers
parameter_list|(
name|Holder
argument_list|<
name|String
argument_list|>
name|testname
parameter_list|,
name|int
name|number1
parameter_list|,
name|int
name|number2
parameter_list|)
block|{
if|if
condition|(
name|number1
operator|<
literal|0
operator|||
name|number2
operator|<
literal|0
condition|)
block|{
operator|new
name|AddressingFeatureException
argument_list|(
literal|"One of the numbers received was negative:"
operator|+
name|number1
operator|+
literal|", "
operator|+
name|number2
argument_list|)
expr_stmt|;
block|}
return|return
name|number1
operator|+
name|number2
return|;
block|}
block|}
end_class

end_unit

