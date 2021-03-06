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
operator|.
name|metrics
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|NotFoundException
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
name|ProcessingException
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
name|client
operator|.
name|Client
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
name|client
operator|.
name|ClientBuilder
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
name|MediaType
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|jaxrs
operator|.
name|json
operator|.
name|JacksonJsonProvider
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
name|endpoint
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
name|jaxrs
operator|.
name|JAXRSServerFactoryBean
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
name|JAXRSClientFactoryBean
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
name|jaxrs
operator|.
name|lifecycle
operator|.
name|SingletonResourceProvider
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
name|model
operator|.
name|AbstractResourceInfo
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
name|message
operator|.
name|Exchange
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
name|metrics
operator|.
name|MetricsContext
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
name|metrics
operator|.
name|MetricsFeature
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
name|metrics
operator|.
name|MetricsProvider
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
name|service
operator|.
name|model
operator|.
name|BindingOperationInfo
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
name|Ignore
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Rule
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
import|import
name|org
operator|.
name|junit
operator|.
name|rules
operator|.
name|ExpectedException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mockito
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|junit
operator|.
name|MockitoJUnitRunner
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

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|any
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|anyLong
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|times
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|MockitoJUnitRunner
operator|.
name|class
argument_list|)
specifier|public
class|class
name|JAXRSServerMetricsTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|allocatePort
argument_list|(
name|JAXRSServerMetricsTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|MetricsProvider
name|provider
decl_stmt|;
specifier|private
specifier|static
name|MetricsContext
name|operationContext
decl_stmt|;
specifier|private
specifier|static
name|MetricsContext
name|resourceContext
decl_stmt|;
specifier|private
specifier|static
name|MetricsContext
name|endpointContext
decl_stmt|;
annotation|@
name|Rule
specifier|public
name|ExpectedException
name|expectedException
init|=
name|ExpectedException
operator|.
name|none
argument_list|()
decl_stmt|;
specifier|public
specifier|static
class|class
name|BookLibrary
implements|implements
name|Library
block|{
annotation|@
name|Override
specifier|public
name|Book
name|getBook
parameter_list|(
name|int
name|id
parameter_list|)
block|{
if|if
condition|(
name|id
operator|==
literal|10
condition|)
block|{
throw|throw
operator|new
name|NotFoundException
argument_list|()
throw|;
block|}
else|else
block|{
return|return
operator|new
name|Book
argument_list|(
name|id
argument_list|)
return|;
block|}
block|}
block|}
annotation|@
name|Ignore
specifier|public
specifier|static
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
specifier|final
name|JAXRSServerFactoryBean
name|sf
init|=
operator|new
name|JAXRSServerFactoryBean
argument_list|()
decl_stmt|;
name|sf
operator|.
name|setResourceClasses
argument_list|(
name|BookLibrary
operator|.
name|class
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setResourceProvider
argument_list|(
name|BookLibrary
operator|.
name|class
argument_list|,
operator|new
name|SingletonResourceProvider
argument_list|(
operator|new
name|BookLibrary
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setFeatures
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|MetricsFeature
argument_list|(
name|provider
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/"
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setProvider
argument_list|(
operator|new
name|JacksonJsonProvider
argument_list|()
argument_list|)
expr_stmt|;
name|sf
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
name|endpointContext
operator|=
name|Mockito
operator|.
name|mock
argument_list|(
name|MetricsContext
operator|.
name|class
argument_list|)
expr_stmt|;
name|operationContext
operator|=
name|Mockito
operator|.
name|mock
argument_list|(
name|MetricsContext
operator|.
name|class
argument_list|)
expr_stmt|;
name|resourceContext
operator|=
name|Mockito
operator|.
name|mock
argument_list|(
name|MetricsContext
operator|.
name|class
argument_list|)
expr_stmt|;
name|provider
operator|=
operator|new
name|MetricsProvider
argument_list|()
block|{
specifier|public
name|MetricsContext
name|createEndpointContext
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|boolean
name|asClient
parameter_list|,
name|String
name|cid
parameter_list|)
block|{
return|return
name|endpointContext
return|;
block|}
specifier|public
name|MetricsContext
name|createOperationContext
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|BindingOperationInfo
name|boi
parameter_list|,
name|boolean
name|asClient
parameter_list|,
name|String
name|cid
parameter_list|)
block|{
return|return
name|operationContext
return|;
block|}
specifier|public
name|MetricsContext
name|createResourceContext
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|String
name|resourceName
parameter_list|,
name|boolean
name|asClient
parameter_list|,
name|String
name|cid
parameter_list|)
block|{
return|return
name|resourceContext
return|;
block|}
block|}
expr_stmt|;
name|AbstractResourceInfo
operator|.
name|clearAllMaps
argument_list|()
expr_stmt|;
comment|//keep out of process due to stack traces testing failures
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
name|createStaticBus
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|Mockito
operator|.
name|reset
argument_list|(
name|resourceContext
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|reset
argument_list|(
name|operationContext
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|reset
argument_list|(
name|endpointContext
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|usingClientProxyStopIsCalledWhenServerReturnsNotFound
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|JAXRSClientFactoryBean
name|factory
init|=
operator|new
name|JAXRSClientFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setResourceClass
argument_list|(
name|Library
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/"
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setProvider
argument_list|(
name|JacksonJsonProvider
operator|.
name|class
argument_list|)
expr_stmt|;
try|try
block|{
specifier|final
name|Library
name|client
init|=
name|factory
operator|.
name|create
argument_list|(
name|Library
operator|.
name|class
argument_list|)
decl_stmt|;
name|expectedException
operator|.
name|expect
argument_list|(
name|NotFoundException
operator|.
name|class
argument_list|)
expr_stmt|;
name|client
operator|.
name|getBook
argument_list|(
literal|10
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Mockito
operator|.
name|verify
argument_list|(
name|resourceContext
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|start
argument_list|(
name|any
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|resourceContext
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|stop
argument_list|(
name|anyLong
argument_list|()
argument_list|,
name|anyLong
argument_list|()
argument_list|,
name|anyLong
argument_list|()
argument_list|,
name|any
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|endpointContext
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|start
argument_list|(
name|any
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|endpointContext
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|stop
argument_list|(
name|anyLong
argument_list|()
argument_list|,
name|anyLong
argument_list|()
argument_list|,
name|anyLong
argument_list|()
argument_list|,
name|any
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verifyNoInteractions
argument_list|(
name|operationContext
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|usingClientStopIsCalledWhenServerReturnsNotFound
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Client
name|client
init|=
name|ClientBuilder
operator|.
name|newClient
argument_list|()
operator|.
name|register
argument_list|(
name|JacksonJsonProvider
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|expectedException
operator|.
name|expect
argument_list|(
name|ProcessingException
operator|.
name|class
argument_list|)
expr_stmt|;
name|client
operator|.
name|target
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/books/10"
argument_list|)
operator|.
name|request
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|readEntity
argument_list|(
name|Book
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Mockito
operator|.
name|verify
argument_list|(
name|resourceContext
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|start
argument_list|(
name|any
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|resourceContext
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|stop
argument_list|(
name|anyLong
argument_list|()
argument_list|,
name|anyLong
argument_list|()
argument_list|,
name|anyLong
argument_list|()
argument_list|,
name|any
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|endpointContext
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|start
argument_list|(
name|any
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|endpointContext
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|stop
argument_list|(
name|anyLong
argument_list|()
argument_list|,
name|anyLong
argument_list|()
argument_list|,
name|anyLong
argument_list|()
argument_list|,
name|any
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verifyNoInteractions
argument_list|(
name|operationContext
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|usingClientStopIsCalledWhenServerReturnSuccessfulResponse
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Client
name|client
init|=
name|ClientBuilder
operator|.
name|newClient
argument_list|()
operator|.
name|register
argument_list|(
name|JacksonJsonProvider
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|client
operator|.
name|target
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/books/11"
argument_list|)
operator|.
name|request
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|readEntity
argument_list|(
name|Book
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Mockito
operator|.
name|verify
argument_list|(
name|resourceContext
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|start
argument_list|(
name|any
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|resourceContext
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|stop
argument_list|(
name|anyLong
argument_list|()
argument_list|,
name|anyLong
argument_list|()
argument_list|,
name|anyLong
argument_list|()
argument_list|,
name|any
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|endpointContext
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|start
argument_list|(
name|any
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|endpointContext
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|stop
argument_list|(
name|anyLong
argument_list|()
argument_list|,
name|anyLong
argument_list|()
argument_list|,
name|anyLong
argument_list|()
argument_list|,
name|any
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verifyNoInteractions
argument_list|(
name|operationContext
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|usingWebClientStopIsCalledWhenServerReturnsNotFound
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|WebClient
name|client
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/books/10"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|JacksonJsonProvider
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|expectedException
operator|.
name|expect
argument_list|(
name|ProcessingException
operator|.
name|class
argument_list|)
expr_stmt|;
name|client
operator|.
name|get
argument_list|()
operator|.
name|readEntity
argument_list|(
name|Book
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Mockito
operator|.
name|verify
argument_list|(
name|resourceContext
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|start
argument_list|(
name|any
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|resourceContext
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|stop
argument_list|(
name|anyLong
argument_list|()
argument_list|,
name|anyLong
argument_list|()
argument_list|,
name|anyLong
argument_list|()
argument_list|,
name|any
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|endpointContext
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|start
argument_list|(
name|any
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|endpointContext
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|stop
argument_list|(
name|anyLong
argument_list|()
argument_list|,
name|anyLong
argument_list|()
argument_list|,
name|anyLong
argument_list|()
argument_list|,
name|any
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verifyNoInteractions
argument_list|(
name|operationContext
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|usingWebClientStopIsCalledWhenUrlIsNotServed
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|WebClient
name|client
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/books"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|JacksonJsonProvider
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|expectedException
operator|.
name|expect
argument_list|(
name|ProcessingException
operator|.
name|class
argument_list|)
expr_stmt|;
name|client
operator|.
name|get
argument_list|()
operator|.
name|readEntity
argument_list|(
name|Book
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Mockito
operator|.
name|verify
argument_list|(
name|endpointContext
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|start
argument_list|(
name|any
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|endpointContext
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|stop
argument_list|(
name|anyLong
argument_list|()
argument_list|,
name|anyLong
argument_list|()
argument_list|,
name|anyLong
argument_list|()
argument_list|,
name|any
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verifyNoInteractions
argument_list|(
name|resourceContext
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verifyNoInteractions
argument_list|(
name|operationContext
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

