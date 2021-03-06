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
name|handlers
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
name|test
operator|.
name|AbstractCXFSpringTest
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
name|TestUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|handlers
operator|.
name|AddNumbers
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|SpringConfiguredHandlerTest
extends|extends
name|AbstractCXFSpringTest
block|{
specifier|static
name|String
name|port
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
literal|"springport"
argument_list|)
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|String
index|[]
name|getConfigLocations
parameter_list|()
block|{
return|return
operator|new
name|String
index|[]
block|{
literal|"classpath:/org/apache/cxf/systest/handlers/beans.xml"
block|}
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSpringConfiguresHandlers
parameter_list|()
throws|throws
name|Exception
block|{
name|AddNumbers
name|addNumbers
init|=
name|getApplicationContext
argument_list|()
operator|.
name|getBean
argument_list|(
literal|"cxfHandlerTestClientEndpoint"
argument_list|,
name|AddNumbers
operator|.
name|class
argument_list|)
decl_stmt|;
name|int
name|r
init|=
name|addNumbers
operator|.
name|addNumbers
argument_list|(
literal|10
argument_list|,
literal|15
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1015
argument_list|,
name|r
argument_list|)
expr_stmt|;
name|addNumbers
operator|=
name|getApplicationContext
argument_list|()
operator|.
name|getBean
argument_list|(
literal|"cxfHandlerTestClientEndpointNoHandler"
argument_list|,
name|AddNumbers
operator|.
name|class
argument_list|)
expr_stmt|;
name|r
operator|=
name|addNumbers
operator|.
name|addNumbers
argument_list|(
literal|10
argument_list|,
literal|15
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|115
argument_list|,
name|r
argument_list|)
expr_stmt|;
name|addNumbers
operator|=
name|getApplicationContext
argument_list|()
operator|.
name|getBean
argument_list|(
literal|"cxfHandlerTestClientServer"
argument_list|,
name|AddNumbers
operator|.
name|class
argument_list|)
expr_stmt|;
name|r
operator|=
name|addNumbers
operator|.
name|addNumbers
argument_list|(
literal|10
argument_list|,
literal|15
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1015
argument_list|,
name|r
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

