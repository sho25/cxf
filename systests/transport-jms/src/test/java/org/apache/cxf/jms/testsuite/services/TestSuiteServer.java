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
name|java
operator|.
name|util
operator|.
name|LinkedList
import|;
end_import

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
name|ws
operator|.
name|Endpoint
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
name|BusFactory
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
name|jms
operator|.
name|testsuite
operator|.
name|util
operator|.
name|JMSTestUtil
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
name|testutil
operator|.
name|common
operator|.
name|AbstractBusTestServerBase
import|;
end_import

begin_class
specifier|public
class|class
name|TestSuiteServer
extends|extends
name|AbstractBusTestServerBase
block|{
specifier|private
specifier|static
name|String
name|jndiUrl
decl_stmt|;
name|List
argument_list|<
name|Endpoint
argument_list|>
name|endpoints
init|=
operator|new
name|LinkedList
argument_list|<
name|Endpoint
argument_list|>
argument_list|()
decl_stmt|;
specifier|protected
name|void
name|run
parameter_list|()
block|{
name|setBus
argument_list|(
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
argument_list|)
expr_stmt|;
name|startEndpoint
argument_list|(
literal|"test0001"
argument_list|,
operator|new
name|Test0001Impl
argument_list|()
argument_list|)
expr_stmt|;
name|startEndpoint
argument_list|(
literal|"test0101"
argument_list|,
operator|new
name|Test0101Impl
argument_list|()
argument_list|)
expr_stmt|;
name|startEndpoint
argument_list|(
literal|"test0003"
argument_list|,
operator|new
name|Test0003Impl
argument_list|()
argument_list|)
expr_stmt|;
name|startEndpoint
argument_list|(
literal|"test0005"
argument_list|,
operator|new
name|Test0005Impl
argument_list|()
argument_list|)
expr_stmt|;
name|startEndpoint
argument_list|(
literal|"test0006"
argument_list|,
operator|new
name|Test0006Impl
argument_list|()
argument_list|)
expr_stmt|;
name|startEndpoint
argument_list|(
literal|"test0008"
argument_list|,
operator|new
name|Test0008Impl
argument_list|()
argument_list|)
expr_stmt|;
name|startEndpoint
argument_list|(
literal|"test0009"
argument_list|,
operator|new
name|Test0009Impl
argument_list|()
argument_list|)
expr_stmt|;
name|startEndpoint
argument_list|(
literal|"test0010"
argument_list|,
operator|new
name|Test0010Impl
argument_list|()
argument_list|)
expr_stmt|;
name|startEndpoint
argument_list|(
literal|"test0011"
argument_list|,
operator|new
name|Test0011Impl
argument_list|()
argument_list|)
expr_stmt|;
name|startEndpoint
argument_list|(
literal|"test0012"
argument_list|,
operator|new
name|Test0012Impl
argument_list|()
argument_list|)
expr_stmt|;
name|startEndpoint
argument_list|(
literal|"test0013"
argument_list|,
operator|new
name|Test0013Impl
argument_list|()
argument_list|)
expr_stmt|;
name|startEndpoint
argument_list|(
literal|"test0014"
argument_list|,
operator|new
name|Test0014Impl
argument_list|()
argument_list|)
expr_stmt|;
name|startEndpoint
argument_list|(
literal|"test1001"
argument_list|,
operator|new
name|Test1001Impl
argument_list|()
argument_list|)
expr_stmt|;
name|startEndpoint
argument_list|(
literal|"test1002"
argument_list|,
operator|new
name|Test1002Impl
argument_list|()
argument_list|)
expr_stmt|;
name|startEndpoint
argument_list|(
literal|"test1003"
argument_list|,
operator|new
name|Test1003Impl
argument_list|()
argument_list|)
expr_stmt|;
name|startEndpoint
argument_list|(
literal|"test1004"
argument_list|,
operator|new
name|Test1004Impl
argument_list|()
argument_list|)
expr_stmt|;
name|startEndpoint
argument_list|(
literal|"test1006"
argument_list|,
operator|new
name|Test1006Impl
argument_list|()
argument_list|)
expr_stmt|;
name|startEndpoint
argument_list|(
literal|"test1007"
argument_list|,
operator|new
name|Test1007Impl
argument_list|()
argument_list|)
expr_stmt|;
name|startEndpoint
argument_list|(
literal|"test1008"
argument_list|,
operator|new
name|Test1008Impl
argument_list|()
argument_list|)
expr_stmt|;
name|startEndpoint
argument_list|(
literal|"test1101"
argument_list|,
operator|new
name|Test1101Impl
argument_list|()
argument_list|)
expr_stmt|;
name|startEndpoint
argument_list|(
literal|"test1102"
argument_list|,
operator|new
name|Test1102Impl
argument_list|()
argument_list|)
expr_stmt|;
name|startEndpoint
argument_list|(
literal|"test1103"
argument_list|,
operator|new
name|Test1103Impl
argument_list|()
argument_list|)
expr_stmt|;
name|startEndpoint
argument_list|(
literal|"test1104"
argument_list|,
operator|new
name|Test1104Impl
argument_list|()
argument_list|)
expr_stmt|;
name|startEndpoint
argument_list|(
literal|"test1105"
argument_list|,
operator|new
name|Test1105Impl
argument_list|()
argument_list|)
expr_stmt|;
name|startEndpoint
argument_list|(
literal|"test1106"
argument_list|,
operator|new
name|Test1106Impl
argument_list|()
argument_list|)
expr_stmt|;
name|startEndpoint
argument_list|(
literal|"test1107"
argument_list|,
operator|new
name|Test1107Impl
argument_list|()
argument_list|)
expr_stmt|;
name|startEndpoint
argument_list|(
literal|"test1108"
argument_list|,
operator|new
name|Test1108Impl
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|startEndpoint
parameter_list|(
name|String
name|endpointName
parameter_list|,
name|Object
name|testImpl
parameter_list|)
block|{
name|String
name|partAddress
init|=
name|JMSTestUtil
operator|.
name|getTestCase
argument_list|(
name|endpointName
argument_list|)
operator|.
name|getAddress
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
name|String
name|address
init|=
name|JMSTestUtil
operator|.
name|getFullAddress
argument_list|(
name|partAddress
argument_list|,
name|jndiUrl
argument_list|)
decl_stmt|;
name|endpoints
operator|.
name|add
argument_list|(
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
name|testImpl
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
for|for
control|(
name|Endpoint
name|ep
range|:
name|endpoints
control|)
block|{
name|ep
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
name|endpoints
operator|.
name|clear
argument_list|()
expr_stmt|;
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
block|{
try|try
block|{
name|TestSuiteServer
name|s
init|=
operator|new
name|TestSuiteServer
argument_list|()
decl_stmt|;
name|s
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"done!"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|setJndiUrl
parameter_list|(
name|String
name|jndiUrl
parameter_list|)
block|{
name|TestSuiteServer
operator|.
name|jndiUrl
operator|=
name|jndiUrl
expr_stmt|;
block|}
block|}
end_class

end_unit

