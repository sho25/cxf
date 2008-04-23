begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|test
operator|.
name|consumer
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jbi
operator|.
name|component
operator|.
name|ComponentContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jbi
operator|.
name|servicedesc
operator|.
name|ServiceEndpoint
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
name|jbi
operator|.
name|ServiceConsumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world
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
name|hello_world
operator|.
name|HelloWorldService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world
operator|.
name|PingMeFault
import|;
end_import

begin_class
specifier|public
class|class
name|HelloWorldConsumer
implements|implements
name|ServiceConsumer
block|{
specifier|private
specifier|volatile
name|boolean
name|running
decl_stmt|;
specifier|private
name|ComponentContext
name|ctx
decl_stmt|;
specifier|public
name|void
name|setComponentContext
parameter_list|(
name|ComponentContext
name|cc
parameter_list|)
block|{
name|ctx
operator|=
name|cc
expr_stmt|;
block|}
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|running
operator|=
literal|false
expr_stmt|;
block|}
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
name|running
operator|=
literal|true
expr_stmt|;
name|waitForEndpointActivation
argument_list|()
expr_stmt|;
do|do
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"getting service"
argument_list|)
expr_stmt|;
name|HelloWorldService
name|service
init|=
operator|new
name|HelloWorldService
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"got service"
argument_list|)
expr_stmt|;
name|Greeter
name|g
init|=
name|service
operator|.
name|getSoapPort
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"invoking method"
argument_list|)
expr_stmt|;
name|String
name|ret
init|=
name|g
operator|.
name|greetMe
argument_list|(
literal|"ffang"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"greetMe service says: "
operator|+
name|ret
argument_list|)
expr_stmt|;
name|ret
operator|=
name|g
operator|.
name|sayHi
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"sayHi service says: "
operator|+
name|ret
argument_list|)
expr_stmt|;
name|g
operator|.
name|greetMeOneWay
argument_list|(
literal|"ffang"
argument_list|)
expr_stmt|;
try|try
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Invoking pingMe, expecting exception..."
argument_list|)
expr_stmt|;
name|g
operator|.
name|pingMe
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PingMeFault
name|ex
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Expected exception: PingMeFault has occurred: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Thread
operator|.
name|sleep
argument_list|(
literal|10000
argument_list|)
expr_stmt|;
block|}
do|while
condition|(
name|running
condition|)
do|;
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
block|}
block|}
specifier|protected
specifier|final
name|void
name|waitForEndpointActivation
parameter_list|()
block|{
specifier|final
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world"
argument_list|,
literal|"HelloWorldService"
argument_list|)
decl_stmt|;
name|boolean
name|ready
init|=
literal|false
decl_stmt|;
do|do
block|{
name|ServiceEndpoint
index|[]
name|eps
init|=
name|ctx
operator|.
name|getEndpointsForService
argument_list|(
name|serviceName
argument_list|)
decl_stmt|;
if|if
condition|(
name|eps
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"waiting for endpoints to become active"
argument_list|)
expr_stmt|;
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//ignore it
block|}
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"endpoints ready, pump starting"
argument_list|)
expr_stmt|;
name|ready
operator|=
literal|true
expr_stmt|;
block|}
block|}
do|while
condition|(
operator|!
name|ready
operator|&&
name|running
condition|)
do|;
block|}
block|}
end_class

end_unit

