begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|sample
operator|.
name|ws
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamResult
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
import|;
end_import

begin_class
specifier|public
class|class
name|SampleWsApplicationClient
block|{
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|String
name|address
init|=
literal|"http://localhost:8080/Service/Hello"
decl_stmt|;
comment|// final String request =
comment|// "<q0:sayHello xmlns:q0=\"http://service.ws.sample\">Elan</q0:sayHello>";
name|String
name|request
init|=
literal|"<q0:sayHello xmlns:q0=\"http://service.ws.sample/\"><myname>Elan</myname></q0:sayHello>"
decl_stmt|;
name|StreamSource
name|source
init|=
operator|new
name|StreamSource
argument_list|(
operator|new
name|StringReader
argument_list|(
name|request
argument_list|)
argument_list|)
decl_stmt|;
name|StreamResult
name|result
init|=
operator|new
name|StreamResult
argument_list|(
name|System
operator|.
name|out
argument_list|)
decl_stmt|;
comment|//assertThat(this.output.toString(),
comment|//           containsString("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
comment|//                          + "<ns2:sayHelloResponse xmlns:ns2=\"http://service.ws.sample/\">"
comment|//                          + "<return>Hello, Welcome to CXF Spring boot Elan!!!</return>"
comment|//                          + "</ns2:sayHelloResponse>"));
block|}
block|}
end_class

end_unit

