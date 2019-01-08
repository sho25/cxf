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
name|transport
operator|.
name|http_jaxws_spi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

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
name|ws
operator|.
name|spi
operator|.
name|http
operator|.
name|HttpContext
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
name|spi
operator|.
name|http
operator|.
name|HttpExchange
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
name|service
operator|.
name|model
operator|.
name|EndpointInfo
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
name|MessageObserver
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
name|http
operator|.
name|DestinationRegistryImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|IMocksControl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|expect
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|isA
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
name|assertNotNull
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
name|assertNull
import|;
end_import

begin_class
specifier|public
class|class
name|JAXWSHttpSpiDestinationTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|ADDRESS
init|=
literal|"http://localhost:80/foo/bar"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CONTEXT_PATH
init|=
literal|"/foo"
decl_stmt|;
specifier|private
name|IMocksControl
name|control
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|HttpContext
name|context
decl_stmt|;
specifier|private
name|MessageObserver
name|observer
decl_stmt|;
specifier|private
name|EndpointInfo
name|endpoint
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|control
operator|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
expr_stmt|;
name|bus
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getExtension
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|policy
operator|.
name|PolicyDataEngine
operator|.
name|class
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|observer
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|MessageObserver
operator|.
name|class
argument_list|)
expr_stmt|;
name|context
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|HttpContext
operator|.
name|class
argument_list|)
expr_stmt|;
name|endpoint
operator|=
operator|new
name|EndpointInfo
argument_list|()
expr_stmt|;
name|endpoint
operator|.
name|setAddress
argument_list|(
name|ADDRESS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|context
operator|=
literal|null
expr_stmt|;
name|bus
operator|=
literal|null
expr_stmt|;
name|observer
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCtor
parameter_list|()
throws|throws
name|Exception
block|{
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|JAXWSHttpSpiDestination
name|destination
init|=
operator|new
name|JAXWSHttpSpiDestination
argument_list|(
name|bus
argument_list|,
operator|new
name|DestinationRegistryImpl
argument_list|()
argument_list|,
name|endpoint
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|destination
operator|.
name|getMessageObserver
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|destination
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|destination
operator|.
name|getAddress
argument_list|()
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ADDRESS
argument_list|,
name|destination
operator|.
name|getAddress
argument_list|()
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpExchange
name|exchange
init|=
name|setUpExchange
argument_list|()
decl_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|JAXWSHttpSpiDestination
name|destination
init|=
operator|new
name|JAXWSHttpSpiDestination
argument_list|(
name|bus
argument_list|,
operator|new
name|DestinationRegistryImpl
argument_list|()
argument_list|,
name|endpoint
argument_list|)
decl_stmt|;
name|destination
operator|.
name|setMessageObserver
argument_list|(
name|observer
argument_list|)
expr_stmt|;
name|destination
operator|.
name|doService
argument_list|(
operator|new
name|HttpServletRequestAdapter
argument_list|(
name|exchange
argument_list|)
argument_list|,
operator|new
name|HttpServletResponseAdapter
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
specifier|private
name|HttpExchange
name|setUpExchange
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpExchange
name|exchange
init|=
name|control
operator|.
name|createMock
argument_list|(
name|HttpExchange
operator|.
name|class
argument_list|)
decl_stmt|;
name|expect
argument_list|(
name|exchange
operator|.
name|getHttpContext
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|context
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|exchange
operator|.
name|getQueryString
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|exchange
operator|.
name|getPathInfo
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|exchange
operator|.
name|getRequestURI
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|CONTEXT_PATH
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|exchange
operator|.
name|getContextPath
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|CONTEXT_PATH
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|reqHeaders
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|reqHeaders
operator|.
name|put
argument_list|(
literal|"Content-Type"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"text/xml"
argument_list|)
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|exchange
operator|.
name|getRequestHeaders
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|reqHeaders
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|OutputStream
name|responseBody
init|=
name|control
operator|.
name|createMock
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|responseBody
operator|.
name|flush
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|exchange
operator|.
name|getResponseBody
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|responseBody
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|observer
operator|.
name|onMessage
argument_list|(
name|isA
argument_list|(
name|Message
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
return|return
name|exchange
return|;
block|}
block|}
end_class

end_unit

