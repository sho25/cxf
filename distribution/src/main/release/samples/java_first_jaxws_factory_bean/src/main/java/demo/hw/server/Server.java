begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|hw
operator|.
name|server
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
name|ext
operator|.
name|logging
operator|.
name|LoggingFeature
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
name|jaxws
operator|.
name|JaxWsServerFactoryBean
import|;
end_import

begin_class
specifier|public
class|class
name|Server
block|{
specifier|protected
name|Server
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Starting Server"
argument_list|)
expr_stmt|;
name|HelloWorldImpl
name|implementor
init|=
operator|new
name|HelloWorldImpl
argument_list|()
decl_stmt|;
name|JaxWsServerFactoryBean
name|svrFactory
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|svrFactory
operator|.
name|setServiceClass
argument_list|(
name|HelloWorld
operator|.
name|class
argument_list|)
expr_stmt|;
name|svrFactory
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:9000/helloWorld"
argument_list|)
expr_stmt|;
name|svrFactory
operator|.
name|setServiceBean
argument_list|(
name|implementor
argument_list|)
expr_stmt|;
name|svrFactory
operator|.
name|getFeatures
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingFeature
argument_list|()
argument_list|)
expr_stmt|;
name|svrFactory
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
name|args
index|[]
parameter_list|)
throws|throws
name|Exception
block|{
operator|new
name|Server
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Server ready..."
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|5
operator|*
literal|60
operator|*
literal|1000
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Server exiting"
argument_list|)
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

