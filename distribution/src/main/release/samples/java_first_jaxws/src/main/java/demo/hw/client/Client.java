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
name|client
package|;
end_package

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
name|Service
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
name|soap
operator|.
name|SOAPBinding
import|;
end_import

begin_import
import|import
name|demo
operator|.
name|hw
operator|.
name|server
operator|.
name|HelloWorld
import|;
end_import

begin_import
import|import
name|demo
operator|.
name|hw
operator|.
name|server
operator|.
name|User
import|;
end_import

begin_import
import|import
name|demo
operator|.
name|hw
operator|.
name|server
operator|.
name|UserImpl
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
literal|"http://server.hw.demo/"
argument_list|,
literal|"HelloWorld"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|PORT_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://server.hw.demo/"
argument_list|,
literal|"HelloWorldPort"
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
name|args
index|[]
parameter_list|)
throws|throws
name|Exception
block|{
name|Service
name|service
init|=
name|Service
operator|.
name|create
argument_list|(
name|SERVICE_NAME
argument_list|)
decl_stmt|;
comment|// Endpoint Address
name|String
name|endpointAddress
init|=
literal|"http://localhost:9000/helloWorld"
decl_stmt|;
comment|// If doing Tomcat deployment, endpoint will be similar to:
comment|// String endpointAddress = "http://localhost:8080/java_first_jaxws-2.6-0-SNAPSHOT/services/hello_world";
comment|// Add a port to the Service
name|service
operator|.
name|addPort
argument_list|(
name|PORT_NAME
argument_list|,
name|SOAPBinding
operator|.
name|SOAP11HTTP_BINDING
argument_list|,
name|endpointAddress
argument_list|)
expr_stmt|;
name|HelloWorld
name|hw
init|=
name|service
operator|.
name|getPort
argument_list|(
name|HelloWorld
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
name|hw
operator|.
name|sayHi
argument_list|(
literal|"World"
argument_list|)
argument_list|)
expr_stmt|;
name|User
name|user
init|=
operator|new
name|UserImpl
argument_list|(
literal|"World"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|hw
operator|.
name|sayHiToUser
argument_list|(
name|user
argument_list|)
argument_list|)
expr_stmt|;
comment|//say hi to some more users to fill up the map a bit
name|user
operator|=
operator|new
name|UserImpl
argument_list|(
literal|"Galaxy"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|hw
operator|.
name|sayHiToUser
argument_list|(
name|user
argument_list|)
argument_list|)
expr_stmt|;
name|user
operator|=
operator|new
name|UserImpl
argument_list|(
literal|"Universe"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|hw
operator|.
name|sayHiToUser
argument_list|(
name|user
argument_list|)
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
name|out
operator|.
name|println
argument_list|(
literal|"Users: "
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|Integer
argument_list|,
name|User
argument_list|>
name|users
init|=
name|hw
operator|.
name|getUsers
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Integer
argument_list|,
name|User
argument_list|>
name|e
range|:
name|users
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  "
operator|+
name|e
operator|.
name|getKey
argument_list|()
operator|+
literal|": "
operator|+
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

