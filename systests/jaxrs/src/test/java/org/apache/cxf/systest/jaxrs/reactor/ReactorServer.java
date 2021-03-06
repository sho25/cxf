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
name|reactor
package|;
end_package

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
name|Bus
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
name|BusFactory
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
name|reactor
operator|.
name|server
operator|.
name|ReactorCustomizer
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
name|ReactorServer
extends|extends
name|AbstractBusTestServerBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|allocatePort
argument_list|(
name|ReactorServer
operator|.
name|class
argument_list|)
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|endpoint
operator|.
name|Server
name|server1
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|endpoint
operator|.
name|Server
name|server2
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|run
parameter_list|()
block|{
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
decl_stmt|;
comment|// Make sure default JSONProvider is not loaded
name|bus
operator|.
name|setProperty
argument_list|(
literal|"skip.default.json.provider.registration"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|JAXRSServerFactoryBean
name|sf
init|=
operator|new
name|JAXRSServerFactoryBean
argument_list|()
decl_stmt|;
name|sf
operator|.
name|getProperties
argument_list|(
literal|true
argument_list|)
operator|.
name|put
argument_list|(
literal|"useStreamingSubscriber"
argument_list|,
literal|false
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
operator|new
name|ReactorCustomizer
argument_list|()
operator|.
name|customize
argument_list|(
name|sf
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setResourceClasses
argument_list|(
name|FluxService
operator|.
name|class
argument_list|,
name|MonoService
operator|.
name|class
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setResourceProvider
argument_list|(
name|FluxService
operator|.
name|class
argument_list|,
operator|new
name|SingletonResourceProvider
argument_list|(
operator|new
name|FluxService
argument_list|()
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setResourceProvider
argument_list|(
name|MonoService
operator|.
name|class
argument_list|,
operator|new
name|SingletonResourceProvider
argument_list|(
operator|new
name|MonoService
argument_list|()
argument_list|,
literal|true
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
literal|"/reactor"
argument_list|)
expr_stmt|;
name|server1
operator|=
name|sf
operator|.
name|create
argument_list|()
expr_stmt|;
name|JAXRSServerFactoryBean
name|sf2
init|=
operator|new
name|JAXRSServerFactoryBean
argument_list|()
decl_stmt|;
name|sf2
operator|.
name|setProvider
argument_list|(
operator|new
name|JacksonJsonProvider
argument_list|()
argument_list|)
expr_stmt|;
name|sf2
operator|.
name|setProvider
argument_list|(
operator|new
name|IllegalArgumentExceptionMapper
argument_list|()
argument_list|)
expr_stmt|;
operator|new
name|ReactorCustomizer
argument_list|()
operator|.
name|customize
argument_list|(
name|sf2
argument_list|)
expr_stmt|;
name|sf2
operator|.
name|setResourceClasses
argument_list|(
name|FluxService
operator|.
name|class
argument_list|)
expr_stmt|;
name|sf2
operator|.
name|setResourceProvider
argument_list|(
name|FluxService
operator|.
name|class
argument_list|,
operator|new
name|SingletonResourceProvider
argument_list|(
operator|new
name|FluxService
argument_list|()
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|sf2
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/reactor2"
argument_list|)
expr_stmt|;
name|server2
operator|=
name|sf2
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|server1
operator|.
name|stop
argument_list|()
expr_stmt|;
name|server1
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|server1
operator|=
literal|null
expr_stmt|;
name|server2
operator|.
name|stop
argument_list|()
expr_stmt|;
name|server2
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|server2
operator|=
literal|null
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
name|ReactorServer
name|server
init|=
operator|new
name|ReactorServer
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Go to http://localhost:"
operator|+
name|PORT
operator|+
literal|"/reactor/flux/textJsonImplicitListAsyncStream"
argument_list|)
expr_stmt|;
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

