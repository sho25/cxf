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
name|ws
operator|.
name|addressing
operator|.
name|spring
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|BusException
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
name|bus
operator|.
name|spring
operator|.
name|SpringBusFactory
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
name|configuration
operator|.
name|Configurer
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
name|Client
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
name|endpoint
operator|.
name|Server
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
name|frontend
operator|.
name|ClientProxy
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
name|interceptor
operator|.
name|Interceptor
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
name|JaxWsProxyFactoryBean
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
name|Message
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
name|test
operator|.
name|AbstractCXFTest
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
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|MAPAggregator
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
name|ws
operator|.
name|addressing
operator|.
name|impl
operator|.
name|DefaultMessageIdCache
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
name|ws
operator|.
name|addressing
operator|.
name|soap
operator|.
name|MAPCodec
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
name|GreeterImpl
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

begin_class
specifier|public
class|class
name|WSAFeatureXmlTest
extends|extends
name|AbstractCXFTest
block|{
specifier|static
specifier|final
name|String
name|PORT
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
name|WSAFeatureXmlTest
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|Bus
name|createBus
parameter_list|()
throws|throws
name|BusException
block|{
return|return
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|(
literal|"/org/apache/cxf/systest/ws/addressing/spring/spring.xml"
argument_list|)
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testServerFactory
parameter_list|()
block|{
name|JaxWsServerFactoryBean
name|sf
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
assert|assert
name|bus
operator|!=
literal|null
assert|;
name|sf
operator|.
name|setServiceBean
argument_list|(
operator|new
name|GreeterImpl
argument_list|()
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
literal|"/test"
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setStart
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Configurer
name|c
init|=
name|getBus
argument_list|()
operator|.
name|getExtension
argument_list|(
name|Configurer
operator|.
name|class
argument_list|)
decl_stmt|;
name|c
operator|.
name|configureBean
argument_list|(
literal|"server"
argument_list|,
name|sf
argument_list|)
expr_stmt|;
name|Server
name|server
init|=
name|sf
operator|.
name|create
argument_list|()
decl_stmt|;
name|Endpoint
name|endpoint
init|=
name|server
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|checkAddressInterceptors
argument_list|(
name|endpoint
operator|.
name|getInInterceptors
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testClientProxyFactory
parameter_list|()
block|{
name|JaxWsProxyFactoryBean
name|cf
init|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
decl_stmt|;
name|cf
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/test"
argument_list|)
expr_stmt|;
name|cf
operator|.
name|setServiceClass
argument_list|(
name|Greeter
operator|.
name|class
argument_list|)
expr_stmt|;
name|cf
operator|.
name|setBus
argument_list|(
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|Configurer
name|c
init|=
name|getBus
argument_list|()
operator|.
name|getExtension
argument_list|(
name|Configurer
operator|.
name|class
argument_list|)
decl_stmt|;
name|c
operator|.
name|configureBean
argument_list|(
literal|"client.proxyFactory"
argument_list|,
name|cf
argument_list|)
expr_stmt|;
name|Greeter
name|greeter
init|=
operator|(
name|Greeter
operator|)
name|cf
operator|.
name|create
argument_list|()
decl_stmt|;
name|Client
name|client
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|greeter
argument_list|)
decl_stmt|;
name|checkAddressInterceptors
argument_list|(
name|client
operator|.
name|getInInterceptors
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|checkAddressInterceptors
parameter_list|(
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|interceptors
parameter_list|)
block|{
name|boolean
name|hasAg
init|=
literal|false
decl_stmt|;
name|boolean
name|hasCodec
init|=
literal|false
decl_stmt|;
name|Object
name|cache
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
name|i
range|:
name|interceptors
control|)
block|{
if|if
condition|(
name|i
operator|instanceof
name|MAPAggregator
condition|)
block|{
name|hasAg
operator|=
literal|true
expr_stmt|;
name|cache
operator|=
operator|(
operator|(
name|MAPAggregator
operator|)
name|i
operator|)
operator|.
name|getMessageIdCache
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|i
operator|instanceof
name|MAPCodec
condition|)
block|{
name|hasCodec
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|assertTrue
argument_list|(
name|cache
operator|instanceof
name|TestCache
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|hasAg
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|hasCodec
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
class|class
name|TestCache
extends|extends
name|DefaultMessageIdCache
block|{     }
block|}
end_class

end_unit

