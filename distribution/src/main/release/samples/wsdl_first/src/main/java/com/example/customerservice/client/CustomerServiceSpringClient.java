begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|com
operator|.
name|example
operator|.
name|customerservice
operator|.
name|client
package|;
end_package

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|CustomerServiceSpringClient
block|{
specifier|private
name|CustomerServiceSpringClient
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
comment|// Initialize the spring context and fetch our test client
name|ClassPathXmlApplicationContext
name|context
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"classpath:client-applicationContext.xml"
block|}
argument_list|)
decl_stmt|;
name|CustomerServiceTester
name|client
init|=
operator|(
name|CustomerServiceTester
operator|)
name|context
operator|.
name|getBean
argument_list|(
literal|"tester"
argument_list|)
decl_stmt|;
name|client
operator|.
name|testCustomerService
argument_list|()
expr_stmt|;
name|context
operator|.
name|close
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

