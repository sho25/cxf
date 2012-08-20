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
name|samples
operator|.
name|discovery
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetSocketAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|ServerSocket
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
name|Endpoint
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|Main
block|{
specifier|public
name|Main
parameter_list|()
block|{
comment|// TODO Auto-generated constructor stub
block|}
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
comment|//find a randomish port to use
name|ServerSocket
name|sock
init|=
operator|new
name|ServerSocket
argument_list|()
decl_stmt|;
name|InetSocketAddress
name|s
init|=
operator|new
name|InetSocketAddress
argument_list|(
name|InetAddress
operator|.
name|getLocalHost
argument_list|()
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|sock
operator|.
name|bind
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|int
name|port
init|=
name|sock
operator|.
name|getLocalPort
argument_list|()
decl_stmt|;
name|sock
operator|.
name|close
argument_list|()
expr_stmt|;
name|String
name|address
init|=
literal|"http://localhost:"
operator|+
name|port
operator|+
literal|"/Greeter"
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Publishing on "
operator|+
name|address
argument_list|)
expr_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
operator|new
name|GreeterImpl
argument_list|(
name|port
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

