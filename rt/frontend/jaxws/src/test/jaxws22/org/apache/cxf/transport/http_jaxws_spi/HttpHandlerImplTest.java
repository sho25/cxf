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
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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
name|HttpHandlerImplTest
extends|extends
name|Assert
block|{
specifier|private
name|IMocksControl
name|control
decl_stmt|;
specifier|private
name|HttpHandler
name|handler
decl_stmt|;
specifier|private
name|JAXWSHttpSpiDestination
name|destination
decl_stmt|;
specifier|private
name|HttpExchange
name|exchange
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
name|destination
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|JAXWSHttpSpiDestination
operator|.
name|class
argument_list|)
expr_stmt|;
name|handler
operator|=
operator|new
name|HttpHandlerImpl
argument_list|(
name|destination
argument_list|)
expr_stmt|;
name|exchange
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|HttpExchange
operator|.
name|class
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
name|exchange
operator|=
literal|null
expr_stmt|;
name|handler
operator|=
literal|null
expr_stmt|;
name|destination
operator|=
literal|null
expr_stmt|;
name|control
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHttpHandlerImpl
parameter_list|()
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|close
argument_list|()
expr_stmt|;
name|destination
operator|.
name|doService
argument_list|(
name|EasyMock
operator|.
name|isA
argument_list|(
name|HttpServletRequest
operator|.
name|class
argument_list|)
argument_list|,
name|EasyMock
operator|.
name|isA
argument_list|(
name|HttpServletResponse
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
name|handler
operator|.
name|handle
argument_list|(
name|exchange
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

