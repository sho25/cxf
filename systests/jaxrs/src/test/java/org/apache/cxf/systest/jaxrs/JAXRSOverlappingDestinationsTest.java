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
name|jaxrs
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Callable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Executors
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|FutureTask
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|GET
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Produces
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|QueryParam
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|UriInfo
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
name|jaxrs
operator|.
name|client
operator|.
name|WebClient
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
name|AbstractBusClientServerTestBase
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
name|Ignore
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|JAXRSOverlappingDestinationsTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|int
name|PORT
init|=
name|SpringServer
operator|.
name|PORT
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
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|SpringServer
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAbsolutePathOne
parameter_list|()
throws|throws
name|Exception
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/one/bookstore/request"
argument_list|)
decl_stmt|;
name|String
name|path
init|=
name|wc
operator|.
name|accept
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|get
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Absolute RequestURI is wrong"
argument_list|,
name|wc
operator|.
name|getBaseURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAbsolutePathTwo
parameter_list|()
throws|throws
name|Exception
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/two/bookstore/request"
argument_list|)
decl_stmt|;
name|String
name|path
init|=
name|wc
operator|.
name|accept
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|get
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Absolute RequestURI is wrong"
argument_list|,
name|wc
operator|.
name|getBaseURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAbsolutePathOneAndTwo
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|requestURI
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/one/bookstore/request?delay"
decl_stmt|;
name|Callable
argument_list|<
name|String
argument_list|>
name|callable
init|=
operator|new
name|Callable
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
specifier|public
name|String
name|call
parameter_list|()
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|requestURI
argument_list|)
decl_stmt|;
return|return
name|wc
operator|.
name|accept
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|get
argument_list|(
name|String
operator|.
name|class
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|FutureTask
argument_list|<
name|String
argument_list|>
name|task
init|=
operator|new
name|FutureTask
argument_list|<>
argument_list|(
name|callable
argument_list|)
decl_stmt|;
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|executor
operator|.
name|execute
argument_list|(
name|task
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|Runnable
name|runnable
init|=
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|testAbsolutePathTwo
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Concurrent testAbsolutePathTwo failed"
argument_list|)
throw|;
block|}
block|}
block|}
decl_stmt|;
operator|new
name|Thread
argument_list|(
name|runnable
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|String
name|path
init|=
name|task
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Absolute RequestURI is wrong"
argument_list|,
name|requestURI
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAbsolutePathOneAndTwoWithLock
parameter_list|()
throws|throws
name|Exception
block|{
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/one/bookstore/lock"
argument_list|)
operator|.
name|accept
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
specifier|final
name|String
name|requestURI
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/one/bookstore/uris"
decl_stmt|;
name|Callable
argument_list|<
name|String
argument_list|>
name|callable
init|=
operator|new
name|Callable
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
specifier|public
name|String
name|call
parameter_list|()
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|requestURI
argument_list|)
decl_stmt|;
return|return
name|wc
operator|.
name|accept
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|get
argument_list|(
name|String
operator|.
name|class
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|FutureTask
argument_list|<
name|String
argument_list|>
name|task
init|=
operator|new
name|FutureTask
argument_list|<>
argument_list|(
name|callable
argument_list|)
decl_stmt|;
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|executor
operator|.
name|execute
argument_list|(
name|task
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
name|WebClient
name|wc2
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/two/bookstore/unlock"
argument_list|)
decl_stmt|;
name|wc2
operator|.
name|accept
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
name|String
name|path
init|=
name|task
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Absolute RequestURI is wrong"
argument_list|,
name|requestURI
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Ignore
specifier|public
specifier|static
class|class
name|SpringServer
extends|extends
name|AbstractSpringServer
block|{
specifier|public
specifier|static
specifier|final
name|int
name|PORT
init|=
name|allocatePortAsInt
argument_list|(
name|SpringServer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|SpringServer
parameter_list|()
block|{
name|super
argument_list|(
literal|"/jaxrs_many_destinations"
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Path
argument_list|(
literal|"/bookstore"
argument_list|)
specifier|public
specifier|static
class|class
name|Resource
block|{
specifier|private
specifier|volatile
name|boolean
name|locked
decl_stmt|;
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
literal|"text/plain"
argument_list|)
annotation|@
name|Path
argument_list|(
literal|"request"
argument_list|)
specifier|public
name|String
name|getRequestPath
parameter_list|(
annotation|@
name|Context
name|UriInfo
name|ui
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"delay"
argument_list|)
name|String
name|delay
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|delay
operator|!=
literal|null
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
block|}
return|return
name|ui
operator|.
name|getRequestUri
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/uris"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/plain"
argument_list|)
specifier|public
name|String
name|getUris
parameter_list|(
annotation|@
name|Context
name|UriInfo
name|uriInfo
parameter_list|)
block|{
name|String
name|baseUriOnEntry
init|=
name|uriInfo
operator|.
name|getRequestUri
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
try|try
block|{
while|while
condition|(
name|locked
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|x
parameter_list|)
block|{
comment|// ignore
block|}
name|String
name|baseUriOnExit
init|=
name|uriInfo
operator|.
name|getRequestUri
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|baseUriOnEntry
operator|.
name|equals
argument_list|(
name|baseUriOnExit
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|()
throw|;
block|}
return|return
name|baseUriOnExit
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/lock"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/plain"
argument_list|)
specifier|public
name|String
name|lock
parameter_list|()
block|{
name|locked
operator|=
literal|true
expr_stmt|;
return|return
literal|"locked"
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/unlock"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/plain"
argument_list|)
specifier|public
name|String
name|unlock
parameter_list|()
block|{
name|locked
operator|=
literal|false
expr_stmt|;
return|return
literal|"unlocked"
return|;
block|}
block|}
block|}
end_class

end_unit

