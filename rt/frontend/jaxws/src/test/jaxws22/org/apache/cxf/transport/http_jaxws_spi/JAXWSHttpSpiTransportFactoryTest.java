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
name|HttpHandler
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
name|Destination
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
name|Assert
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

begin_class
specifier|public
class|class
name|JAXWSHttpSpiTransportFactoryTest
extends|extends
name|Assert
block|{
specifier|private
name|IMocksControl
name|control
decl_stmt|;
specifier|private
name|HttpContext
name|context
decl_stmt|;
specifier|private
name|JAXWSHttpSpiTransportFactory
name|factory
decl_stmt|;
specifier|private
name|Bus
name|bus
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
name|factory
operator|=
operator|new
name|JAXWSHttpSpiTransportFactory
argument_list|(
name|context
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
name|factory
operator|=
literal|null
expr_stmt|;
name|context
operator|=
literal|null
expr_stmt|;
name|bus
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetDestination1
parameter_list|()
throws|throws
name|Exception
block|{
name|getDestination
argument_list|(
literal|"/bar"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetDestination2
parameter_list|()
throws|throws
name|Exception
block|{
name|getDestination
argument_list|(
literal|"http://localhost:8080/foo/bar"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|getDestination
parameter_list|(
name|String
name|endpointAddress
parameter_list|)
throws|throws
name|Exception
block|{
name|context
operator|.
name|setHandler
argument_list|(
name|EasyMock
operator|.
name|isA
argument_list|(
name|HttpHandler
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|EndpointInfo
name|endpoint
init|=
operator|new
name|EndpointInfo
argument_list|()
decl_stmt|;
name|endpoint
operator|.
name|setAddress
argument_list|(
name|endpointAddress
argument_list|)
expr_stmt|;
name|Destination
name|destination
init|=
name|factory
operator|.
name|getDestination
argument_list|(
name|endpoint
argument_list|,
name|bus
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|destination
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
name|endpointAddress
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
name|assertEquals
argument_list|(
name|endpointAddress
argument_list|,
name|endpoint
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

