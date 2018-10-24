begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|stream
operator|.
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|frontend
operator|.
name|ClientProxy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
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
name|hello_world_soap_http
operator|.
name|SOAPService
import|;
end_import

begin_import
import|import
name|demo
operator|.
name|stream
operator|.
name|interceptor
operator|.
name|StreamInterceptor
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|Client
block|{
specifier|private
specifier|static
specifier|final
name|QName
name|SERVICE_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"SOAPService"
argument_list|)
decl_stmt|;
specifier|private
name|Client
parameter_list|()
block|{     }
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|URL
name|wsdlURL
init|=
name|Client
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"/wsdl/hello_world.wsdl"
argument_list|)
decl_stmt|;
name|SOAPService
name|ss
init|=
operator|new
name|SOAPService
argument_list|(
name|wsdlURL
argument_list|,
name|SERVICE_NAME
argument_list|)
decl_stmt|;
name|Greeter
name|port
init|=
name|ss
operator|.
name|getSoapPort
argument_list|()
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|endpoint
operator|.
name|Client
name|client
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|port
argument_list|)
decl_stmt|;
name|client
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|StreamInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|getInFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|StreamInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Invoking sayHi..."
argument_list|)
expr_stmt|;
name|String
name|resp
init|=
name|port
operator|.
name|sayHi
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Server responded with: "
operator|+
name|resp
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

