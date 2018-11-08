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
name|http_jetty
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|BindingProvider
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
name|AbstractClientServerTestBase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
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
name|hello_world_soap_http
operator|.
name|SOAPService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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

begin_comment
comment|/**  * Tests thread pool config.  */
end_comment

begin_class
specifier|public
class|class
name|ThreadPoolTest
extends|extends
name|AbstractClientServerTestBase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|ADDRESS
init|=
name|Server
operator|.
name|ADDRESS
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|SERVICE_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"SOAPServiceAddressing"
argument_list|)
decl_stmt|;
specifier|private
name|Greeter
name|greeter
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|startServers
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|threads
init|=
name|Math
operator|.
name|max
argument_list|(
literal|1
argument_list|,
operator|(
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|availableProcessors
argument_list|()
operator|+
literal|3
operator|)
operator|/
literal|4
argument_list|)
operator|*
literal|2
operator|+
literal|3
decl_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"ThreadPoolTest.threads"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|threads
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|Server
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/hello_world.wsdl"
argument_list|)
decl_stmt|;
name|greeter
operator|=
operator|new
name|SOAPService
argument_list|(
name|wsdl
argument_list|,
name|SERVICE_NAME
argument_list|)
operator|.
name|getPort
argument_list|(
name|Greeter
operator|.
name|class
argument_list|)
expr_stmt|;
name|BindingProvider
name|bp
init|=
operator|(
name|BindingProvider
operator|)
name|greeter
decl_stmt|;
name|bp
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|,
name|ADDRESS
argument_list|)
expr_stmt|;
block|}
class|class
name|TestRunnable
implements|implements
name|Runnable
block|{
name|int
name|i
decl_stmt|;
name|long
name|total
decl_stmt|;
name|TestRunnable
parameter_list|(
name|int
name|i
parameter_list|)
block|{
name|this
operator|.
name|i
operator|=
name|i
expr_stmt|;
block|}
specifier|public
name|void
name|run
parameter_list|()
block|{
name|long
name|start
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
try|try
block|{
name|greeter
operator|.
name|greetMeLater
argument_list|(
literal|1600
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//ignore
block|}
name|long
name|end
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|total
operator|=
name|end
operator|-
name|start
expr_stmt|;
block|}
specifier|public
name|long
name|getTotal
parameter_list|()
block|{
return|return
name|total
return|;
block|}
block|}
comment|// @Ignore'ing due to continual failure on Jenkins
annotation|@
name|Test
annotation|@
name|org
operator|.
name|junit
operator|.
name|Ignore
specifier|public
name|void
name|testFallbackThreadPoolConfig
parameter_list|()
throws|throws
name|Exception
block|{
comment|//make sure things are running
name|greeter
operator|.
name|greetMeLater
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|greeter
operator|.
name|greetMeLater
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|TestRunnable
index|[]
name|r
init|=
operator|new
name|TestRunnable
index|[
literal|5
index|]
decl_stmt|;
name|Thread
index|[]
name|invokers
init|=
operator|new
name|Thread
index|[
literal|5
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|invokers
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|r
index|[
name|i
index|]
operator|=
operator|new
name|TestRunnable
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|invokers
index|[
name|i
index|]
operator|=
operator|new
name|Thread
argument_list|(
name|r
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|invokers
index|[
name|i
index|]
operator|.
name|setDaemon
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|invokers
index|[
name|i
index|]
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
name|int
name|countLess
init|=
literal|0
decl_stmt|;
name|int
name|countMore
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|invokers
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|invokers
index|[
name|i
index|]
operator|.
name|join
argument_list|(
literal|6
operator|*
literal|1000
argument_list|)
expr_stmt|;
if|if
condition|(
name|r
index|[
name|i
index|]
operator|.
name|getTotal
argument_list|()
operator|>
literal|3000
condition|)
block|{
name|countMore
operator|++
expr_stmt|;
block|}
else|else
block|{
name|countLess
operator|++
expr_stmt|;
block|}
block|}
comment|//Jetty 8 and 9 use different numbers of threads for the connectors and internal management.
comment|//Make sure we have some that took longer and some that took shorter
name|assertTrue
argument_list|(
name|countLess
operator|>=
literal|2
operator|&&
name|countLess
operator|<=
literal|3
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|countMore
operator|>=
literal|2
operator|&&
name|countMore
operator|<=
literal|3
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

