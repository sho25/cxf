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
name|Server
extends|extends
name|AbstractBusTestServerBase
block|{
specifier|protected
name|void
name|run
parameter_list|()
block|{
name|Test0001Impl
name|t0001
init|=
operator|new
name|Test0001Impl
argument_list|()
decl_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|JMSTestUtil
operator|.
name|getTestCase
argument_list|(
literal|"test0001"
argument_list|)
operator|.
name|getAddress
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|,
name|t0001
argument_list|)
expr_stmt|;
name|Test0003Impl
name|t0003
init|=
operator|new
name|Test0003Impl
argument_list|()
decl_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|JMSTestUtil
operator|.
name|getTestCase
argument_list|(
literal|"test0003"
argument_list|)
operator|.
name|getAddress
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|,
name|t0003
argument_list|)
expr_stmt|;
name|Test0005Impl
name|t0005
init|=
operator|new
name|Test0005Impl
argument_list|()
decl_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|JMSTestUtil
operator|.
name|getTestCase
argument_list|(
literal|"test0005"
argument_list|)
operator|.
name|getAddress
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|,
name|t0005
argument_list|)
expr_stmt|;
name|Test0006Impl
name|t0006
init|=
operator|new
name|Test0006Impl
argument_list|()
decl_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|JMSTestUtil
operator|.
name|getTestCase
argument_list|(
literal|"test0006"
argument_list|)
operator|.
name|getAddress
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|,
name|t0006
argument_list|)
expr_stmt|;
name|Test0008Impl
name|t0008
init|=
operator|new
name|Test0008Impl
argument_list|()
decl_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|JMSTestUtil
operator|.
name|getTestCase
argument_list|(
literal|"test0008"
argument_list|)
operator|.
name|getAddress
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|,
name|t0008
argument_list|)
expr_stmt|;
name|Test0009Impl
name|t0009
init|=
operator|new
name|Test0009Impl
argument_list|()
decl_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|JMSTestUtil
operator|.
name|getTestCase
argument_list|(
literal|"test0009"
argument_list|)
operator|.
name|getAddress
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|,
name|t0009
argument_list|)
expr_stmt|;
name|Test0010Impl
name|t0010
init|=
operator|new
name|Test0010Impl
argument_list|()
decl_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|JMSTestUtil
operator|.
name|getTestCase
argument_list|(
literal|"test0010"
argument_list|)
operator|.
name|getAddress
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|,
name|t0010
argument_list|)
expr_stmt|;
name|Test0011Impl
name|t0011
init|=
operator|new
name|Test0011Impl
argument_list|()
decl_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|JMSTestUtil
operator|.
name|getTestCase
argument_list|(
literal|"test0011"
argument_list|)
operator|.
name|getAddress
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|,
name|t0011
argument_list|)
expr_stmt|;
name|Test0012Impl
name|t0012
init|=
operator|new
name|Test0012Impl
argument_list|()
decl_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|JMSTestUtil
operator|.
name|getTestCase
argument_list|(
literal|"test0012"
argument_list|)
operator|.
name|getAddress
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|,
name|t0012
argument_list|)
expr_stmt|;
name|Test1001Impl
name|t1001
init|=
operator|new
name|Test1001Impl
argument_list|()
decl_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|JMSTestUtil
operator|.
name|getTestCase
argument_list|(
literal|"test1001"
argument_list|)
operator|.
name|getAddress
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|,
name|t1001
argument_list|)
expr_stmt|;
name|Test1002Impl
name|t1002
init|=
operator|new
name|Test1002Impl
argument_list|()
decl_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|JMSTestUtil
operator|.
name|getTestCase
argument_list|(
literal|"test1002"
argument_list|)
operator|.
name|getAddress
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|,
name|t1002
argument_list|)
expr_stmt|;
name|Test1003Impl
name|t1003
init|=
operator|new
name|Test1003Impl
argument_list|()
decl_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|JMSTestUtil
operator|.
name|getTestCase
argument_list|(
literal|"test1003"
argument_list|)
operator|.
name|getAddress
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|,
name|t1003
argument_list|)
expr_stmt|;
name|Test1004Impl
name|t1004
init|=
operator|new
name|Test1004Impl
argument_list|()
decl_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|JMSTestUtil
operator|.
name|getTestCase
argument_list|(
literal|"test1004"
argument_list|)
operator|.
name|getAddress
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|,
name|t1004
argument_list|)
expr_stmt|;
name|Test1006Impl
name|t1006
init|=
operator|new
name|Test1006Impl
argument_list|()
decl_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|JMSTestUtil
operator|.
name|getTestCase
argument_list|(
literal|"test1006"
argument_list|)
operator|.
name|getAddress
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|,
name|t1006
argument_list|)
expr_stmt|;
name|Test1007Impl
name|t1007
init|=
operator|new
name|Test1007Impl
argument_list|()
decl_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|JMSTestUtil
operator|.
name|getTestCase
argument_list|(
literal|"test1007"
argument_list|)
operator|.
name|getAddress
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|,
name|t1007
argument_list|)
expr_stmt|;
name|Test1008Impl
name|t1008
init|=
operator|new
name|Test1008Impl
argument_list|()
decl_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|JMSTestUtil
operator|.
name|getTestCase
argument_list|(
literal|"test1008"
argument_list|)
operator|.
name|getAddress
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|,
name|t1008
argument_list|)
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
name|Server
name|s
init|=
operator|new
name|Server
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
block|}
end_class

end_unit

