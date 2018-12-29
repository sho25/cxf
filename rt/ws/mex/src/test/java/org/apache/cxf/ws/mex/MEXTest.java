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
name|ws
operator|.
name|mex
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebMethod
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
name|transport
operator|.
name|local
operator|.
name|LocalTransportFactory
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
name|mex
operator|.
name|model
operator|.
name|_2004_09
operator|.
name|Metadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|MEXTest
block|{
specifier|static
name|Server
name|server
decl_stmt|;
specifier|static
name|Server
name|mexServer
decl_stmt|;
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"org.apache.cxf.ws.mex.test.Echo"
argument_list|)
specifier|public
specifier|static
class|class
name|EchoImpl
block|{
annotation|@
name|WebMethod
specifier|public
name|String
name|echo
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|text
return|;
block|}
block|}
comment|/**      * @throws java.lang.Exception      */
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|setUpBeforeClass
parameter_list|()
throws|throws
name|Exception
block|{
name|JaxWsServerFactoryBean
name|factory
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setServiceBean
argument_list|(
operator|new
name|EchoImpl
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
literal|"local://Echo"
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setTransportId
argument_list|(
name|LocalTransportFactory
operator|.
name|TRANSPORT_ID
argument_list|)
expr_stmt|;
name|server
operator|=
name|factory
operator|.
name|create
argument_list|()
expr_stmt|;
name|factory
operator|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
expr_stmt|;
name|factory
operator|.
name|setServiceBean
argument_list|(
operator|new
name|MEXEndpoint
argument_list|(
name|server
argument_list|)
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
literal|"local://Echo-mex"
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setTransportId
argument_list|(
name|LocalTransportFactory
operator|.
name|TRANSPORT_ID
argument_list|)
expr_stmt|;
name|factory
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
name|mexServer
operator|=
name|factory
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
comment|/**      * @throws java.lang.Exception      */
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|tearDownAfterClass
parameter_list|()
throws|throws
name|Exception
block|{
name|server
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|mexServer
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGet
parameter_list|()
block|{
comment|// Create the client
name|JaxWsProxyFactoryBean
name|proxyFac
init|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
decl_stmt|;
name|proxyFac
operator|.
name|setAddress
argument_list|(
literal|"local://Echo-mex"
argument_list|)
expr_stmt|;
name|proxyFac
operator|.
name|getClientFactoryBean
argument_list|()
operator|.
name|setTransportId
argument_list|(
name|LocalTransportFactory
operator|.
name|TRANSPORT_ID
argument_list|)
expr_stmt|;
name|MetadataExchange
name|exc
init|=
name|proxyFac
operator|.
name|create
argument_list|(
name|MetadataExchange
operator|.
name|class
argument_list|)
decl_stmt|;
name|Metadata
name|metadata
init|=
name|exc
operator|.
name|get2004
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|metadata
argument_list|)
expr_stmt|;
name|proxyFac
operator|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
expr_stmt|;
name|proxyFac
operator|.
name|setAddress
argument_list|(
literal|"local://Echo"
argument_list|)
expr_stmt|;
name|proxyFac
operator|.
name|getClientFactoryBean
argument_list|()
operator|.
name|setTransportId
argument_list|(
name|LocalTransportFactory
operator|.
name|TRANSPORT_ID
argument_list|)
expr_stmt|;
name|exc
operator|=
name|proxyFac
operator|.
name|create
argument_list|(
name|MetadataExchange
operator|.
name|class
argument_list|)
expr_stmt|;
name|metadata
operator|=
name|exc
operator|.
name|get2004
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|metadata
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

