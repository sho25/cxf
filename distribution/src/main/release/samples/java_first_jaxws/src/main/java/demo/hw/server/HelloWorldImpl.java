begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|// START SNIPPET: service
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
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebService
import|;
end_import

begin_class
annotation|@
name|WebService
argument_list|(
name|endpointInterface
operator|=
literal|"demo.hw.server.HelloWorld"
argument_list|,
name|serviceName
operator|=
literal|"HelloWorld"
argument_list|)
specifier|public
class|class
name|HelloWorldImpl
implements|implements
name|HelloWorld
block|{
name|Map
argument_list|<
name|Integer
argument_list|,
name|User
argument_list|>
name|users
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|String
name|sayHi
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"sayHi called"
argument_list|)
expr_stmt|;
return|return
literal|"Hello "
operator|+
name|text
return|;
block|}
specifier|public
name|String
name|sayHiToUser
parameter_list|(
name|User
name|user
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"sayHiToUser called"
argument_list|)
expr_stmt|;
name|users
operator|.
name|put
argument_list|(
name|users
operator|.
name|size
argument_list|()
operator|+
literal|1
argument_list|,
name|user
argument_list|)
expr_stmt|;
return|return
literal|"Hello "
operator|+
name|user
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|public
name|Map
argument_list|<
name|Integer
argument_list|,
name|User
argument_list|>
name|getUsers
parameter_list|()
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"getUsers called"
argument_list|)
expr_stmt|;
return|return
name|users
return|;
block|}
block|}
end_class

begin_comment
comment|// END SNIPPET: service
end_comment

end_unit

