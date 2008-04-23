begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|hw
operator|.
name|server
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_xml_http
operator|.
name|bare
operator|.
name|Greeter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_xml_http
operator|.
name|bare
operator|.
name|types
operator|.
name|MyComplexStructType
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
name|serviceName
operator|=
literal|"XMLService"
argument_list|,
name|portName
operator|=
literal|"XMLPort"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.hello_world_xml_http.bare.Greeter"
argument_list|,
name|targetNamespace
operator|=
literal|"http://apache.org/hello_world_xml_http/bare"
argument_list|)
annotation|@
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|BindingType
argument_list|(
name|value
operator|=
literal|"http://cxf.apache.org/bindings/xformat"
argument_list|)
specifier|public
class|class
name|GreeterImpl
implements|implements
name|Greeter
block|{
specifier|public
name|String
name|greetMe
parameter_list|(
name|String
name|me
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"received calling greetMe!"
argument_list|)
expr_stmt|;
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
comment|// TODO Auto-generated method stub
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"received calling sayHi!"
argument_list|)
expr_stmt|;
return|return
literal|"Bonjour"
return|;
block|}
specifier|public
name|MyComplexStructType
name|sendReceiveData
parameter_list|(
name|MyComplexStructType
name|in
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"received calling sendReceiveData!"
argument_list|)
expr_stmt|;
return|return
name|in
return|;
block|}
specifier|public
name|String
name|testMultiParamPart
parameter_list|(
name|MyComplexStructType
name|in2
parameter_list|,
name|String
name|in1
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
name|in2
operator|.
name|setElem1
argument_list|(
name|in1
argument_list|)
expr_stmt|;
return|return
literal|"Bonjour"
return|;
block|}
block|}
end_class

end_unit

