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
name|hello_world_soap_http
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
name|hello_world_rpclit
operator|.
name|GreeterRPCLit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_rpclit
operator|.
name|types
operator|.
name|MyComplexStruct
import|;
end_import

begin_class
annotation|@
name|WebService
argument_list|(
name|name
operator|=
literal|"GreeterRPCLit"
argument_list|,
name|serviceName
operator|=
literal|"SOAPServiceRPCLit"
argument_list|,
name|portName
operator|=
literal|"SoapPortRPCLit"
argument_list|,
name|targetNamespace
operator|=
literal|"http://apache.org/hello_world_rpclit"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.hello_world_rpclit.GreeterRPCLit"
argument_list|,
name|wsdlLocation
operator|=
literal|"testutils/hello_world_rpc_lit.wsdl"
argument_list|)
specifier|public
class|class
name|RPCLitGreeterImpl
implements|implements
name|GreeterRPCLit
block|{
specifier|public
name|String
name|greetMe
parameter_list|(
name|String
name|me
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Executing operation greetMe"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Message received: "
operator|+
name|me
operator|+
literal|"\n"
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
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Executing operation sayHi"
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
return|return
literal|"Bonjour"
return|;
block|}
specifier|public
name|MyComplexStruct
name|sendReceiveData
parameter_list|(
name|MyComplexStruct
name|in
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Executing operation sendReceiveData"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Received struct with values :\nElement-1 : "
operator|+
name|in
operator|.
name|getElem1
argument_list|()
operator|+
literal|"\nElement-2 : "
operator|+
name|in
operator|.
name|getElem2
argument_list|()
operator|+
literal|"\nElement-3 : "
operator|+
name|in
operator|.
name|getElem3
argument_list|()
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"invalid"
operator|.
name|equals
argument_list|(
name|in
operator|.
name|getElem2
argument_list|()
argument_list|)
condition|)
block|{
name|in
operator|.
name|setElem2
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
return|return
name|in
return|;
block|}
specifier|public
name|String
name|greetUs
parameter_list|(
name|String
name|you
parameter_list|,
name|String
name|me
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Executing operation greetUs"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Message received: you are "
operator|+
name|you
operator|+
literal|" I'm "
operator|+
name|me
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
return|return
literal|"Hello "
operator|+
name|you
operator|+
literal|" and "
operator|+
name|me
return|;
block|}
block|}
end_class

end_unit

