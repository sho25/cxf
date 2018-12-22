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
name|corba
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
name|hello_world_corba
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
name|cxf
operator|.
name|hello_world_corba
operator|.
name|PingMeFault
import|;
end_import

begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
literal|"GreeterTimeoutCORBAPort"
argument_list|,
name|serviceName
operator|=
literal|"GreeterTimeoutCORBAService"
argument_list|,
name|targetNamespace
operator|=
literal|"http://cxf.apache.org/hello_world_corba"
argument_list|,
name|wsdlLocation
operator|=
literal|"classpath:/wsdl_systest/hello_world_corba_timeout.wsdl"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.cxf.hello_world_corba.Greeter"
argument_list|)
specifier|public
class|class
name|BaseGreeterTimeoutImpl
implements|implements
name|Greeter
block|{
specifier|public
specifier|static
specifier|final
name|String
name|GREETME_OUT
init|=
literal|"test out"
decl_stmt|;
specifier|static
specifier|final
name|String
name|EX_STRING
init|=
literal|"CXF RUNTIME EXCEPTION"
decl_stmt|;
specifier|public
name|String
name|greetMe
parameter_list|(
name|String
name|me
parameter_list|)
block|{
name|String
name|timeout
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"jacorb.connection.client.pending_reply_timeout"
argument_list|,
literal|"0"
argument_list|)
decl_stmt|;
if|if
condition|(
name|timeout
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|Integer
name|ms
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|timeout
argument_list|)
decl_stmt|;
if|if
condition|(
name|ms
operator|>
literal|0
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|ms
operator|+
literal|1000
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ignore
parameter_list|)
block|{             }
block|}
return|return
literal|"Hello "
operator|+
name|me
return|;
block|}
specifier|public
name|void
name|greetMeOneWay
parameter_list|(
name|String
name|me
parameter_list|)
block|{     }
specifier|public
name|String
name|sayHi
parameter_list|()
block|{
return|return
name|GREETME_OUT
return|;
block|}
specifier|public
name|void
name|pingMe
parameter_list|(
name|String
name|faultType
parameter_list|)
throws|throws
name|PingMeFault
block|{     }
block|}
end_class

end_unit

