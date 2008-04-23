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
name|refparam
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebParam
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
name|soap
operator|.
name|Addressing
import|;
end_import

begin_class
annotation|@
name|WebService
argument_list|(
name|name
operator|=
literal|"AddNumbersPortType"
argument_list|,
name|portName
operator|=
literal|"AddNumbersPort"
argument_list|,
name|serviceName
operator|=
literal|"AddNumbersService"
argument_list|,
name|targetNamespace
operator|=
literal|"http://example.com"
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
name|AddNumbersImpl
block|{
annotation|@
name|Action
argument_list|(
name|input
operator|=
literal|"addInAction"
argument_list|,
name|output
operator|=
literal|"addOutAction"
argument_list|)
specifier|public
name|int
name|addNumbers
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"number1"
argument_list|,
name|targetNamespace
operator|=
literal|"http://example.com"
argument_list|)
name|int
name|number1
parameter_list|,
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"number2"
argument_list|,
name|targetNamespace
operator|=
literal|"http://example.com"
argument_list|)
name|int
name|number2
parameter_list|)
throws|throws
name|AddNumbersException
block|{
return|return
name|doStuff
argument_list|(
name|number1
argument_list|,
name|number2
argument_list|)
return|;
block|}
specifier|public
name|int
name|addNumbersAndPassString
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"number1"
argument_list|,
name|targetNamespace
operator|=
literal|"http://example.com"
argument_list|)
name|int
name|number1
parameter_list|,
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"number2"
argument_list|,
name|targetNamespace
operator|=
literal|"http://example.com"
argument_list|)
name|int
name|number2
parameter_list|,
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"testString"
argument_list|,
name|targetNamespace
operator|=
literal|"http://example.com"
argument_list|)
name|String
name|testString
parameter_list|)
throws|throws
name|AddNumbersException
block|{
return|return
name|doStuff
argument_list|(
name|number1
argument_list|,
name|number2
argument_list|)
return|;
block|}
name|int
name|doStuff
parameter_list|(
name|int
name|number1
parameter_list|,
name|int
name|number2
parameter_list|)
throws|throws
name|AddNumbersException
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
throw|throw
operator|new
name|AddNumbersException
argument_list|(
literal|"Negative numbers can't be added!"
argument_list|,
literal|"Numbers: "
operator|+
name|number1
operator|+
literal|", "
operator|+
name|number2
argument_list|)
throw|;
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

