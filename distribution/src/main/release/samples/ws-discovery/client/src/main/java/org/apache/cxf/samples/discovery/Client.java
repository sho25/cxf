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
name|util
operator|.
name|List
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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|EndpointReference
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
name|hello_world
operator|.
name|discovery
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
name|hello_world
operator|.
name|discovery
operator|.
name|GreeterService
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
name|ws
operator|.
name|discovery
operator|.
name|WSDiscoveryClient
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|Client
block|{
comment|/**      *       */
specifier|public
name|Client
parameter_list|()
block|{
comment|// TODO Auto-generated constructor stub
block|}
comment|/**      * @param args      */
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
name|WSDiscoveryClient
name|client
init|=
operator|new
name|WSDiscoveryClient
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|EndpointReference
argument_list|>
name|references
init|=
name|client
operator|.
name|probe
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/hello_world/discovery"
argument_list|,
literal|"Greeter"
argument_list|,
literal|"tp"
argument_list|)
argument_list|)
decl_stmt|;
name|client
operator|.
name|close
argument_list|()
expr_stmt|;
name|GreeterService
name|service
init|=
operator|new
name|GreeterService
argument_list|()
decl_stmt|;
for|for
control|(
name|EndpointReference
name|ref
range|:
name|references
control|)
block|{
name|Greeter
name|g
init|=
name|service
operator|.
name|getPort
argument_list|(
name|ref
argument_list|,
name|Greeter
operator|.
name|class
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|g
operator|.
name|greetMe
argument_list|(
literal|"World"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

