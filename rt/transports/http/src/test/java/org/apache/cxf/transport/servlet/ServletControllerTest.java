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
name|servlet
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletException
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
name|easymock
operator|.
name|classextension
operator|.
name|EasyMock
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
name|ServletControllerTest
extends|extends
name|Assert
block|{
specifier|private
name|HttpServletRequest
name|req
decl_stmt|;
specifier|private
name|HttpServletResponse
name|res
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|req
operator|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|HttpServletRequest
operator|.
name|class
argument_list|)
expr_stmt|;
name|res
operator|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|HttpServletResponse
operator|.
name|class
argument_list|)
expr_stmt|;
name|req
operator|.
name|getPathInfo
argument_list|()
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
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGenerateServiceListing
parameter_list|()
throws|throws
name|Exception
block|{
name|req
operator|.
name|getRequestURI
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|"/services"
argument_list|)
expr_stmt|;
name|req
operator|.
name|getParameter
argument_list|(
literal|"stylesheet"
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
expr_stmt|;
name|req
operator|.
name|getParameter
argument_list|(
literal|"formatted"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|"true"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|req
argument_list|)
expr_stmt|;
name|TestServletController
name|sc
init|=
operator|new
name|TestServletController
argument_list|()
decl_stmt|;
name|sc
operator|.
name|invoke
argument_list|(
name|req
argument_list|,
name|res
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sc
operator|.
name|generateListCalled
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|sc
operator|.
name|generateUnformattedCalled
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|sc
operator|.
name|invokeDestinationCalled
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGenerateUnformattedServiceListing
parameter_list|()
throws|throws
name|Exception
block|{
name|req
operator|.
name|getRequestURI
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|"/services"
argument_list|)
expr_stmt|;
name|req
operator|.
name|getParameter
argument_list|(
literal|"stylesheet"
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
expr_stmt|;
name|req
operator|.
name|getParameter
argument_list|(
literal|"formatted"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|"false"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|req
argument_list|)
expr_stmt|;
name|TestServletController
name|sc
init|=
operator|new
name|TestServletController
argument_list|()
decl_stmt|;
name|sc
operator|.
name|invoke
argument_list|(
name|req
argument_list|,
name|res
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|sc
operator|.
name|generateListCalled
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sc
operator|.
name|generateUnformattedCalled
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|sc
operator|.
name|invokeDestinationCalled
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHideServiceListing
parameter_list|()
throws|throws
name|Exception
block|{
name|EasyMock
operator|.
name|replay
argument_list|(
name|req
argument_list|)
expr_stmt|;
name|TestServletController
name|sc
init|=
operator|new
name|TestServletController
argument_list|()
decl_stmt|;
name|sc
operator|.
name|setHideServiceList
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|sc
operator|.
name|invoke
argument_list|(
name|req
argument_list|,
name|res
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|sc
operator|.
name|generateListCalled
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|sc
operator|.
name|generateUnformattedCalled
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sc
operator|.
name|invokeDestinationCalled
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDifferentServiceListPath
parameter_list|()
throws|throws
name|Exception
block|{
name|req
operator|.
name|getRequestURI
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|"/listing"
argument_list|)
expr_stmt|;
name|req
operator|.
name|getParameter
argument_list|(
literal|"stylesheet"
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
expr_stmt|;
name|req
operator|.
name|getParameter
argument_list|(
literal|"formatted"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|"true"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|req
argument_list|)
expr_stmt|;
name|TestServletController
name|sc
init|=
operator|new
name|TestServletController
argument_list|()
decl_stmt|;
name|sc
operator|.
name|setServiceListRelativePath
argument_list|(
literal|"/listing"
argument_list|)
expr_stmt|;
name|sc
operator|.
name|invoke
argument_list|(
name|req
argument_list|,
name|res
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sc
operator|.
name|generateListCalled
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|sc
operator|.
name|generateUnformattedCalled
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|sc
operator|.
name|invokeDestinationCalled
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
class|class
name|TestServletController
extends|extends
name|ServletController
block|{
specifier|private
name|boolean
name|generateListCalled
decl_stmt|;
specifier|private
name|boolean
name|generateUnformattedCalled
decl_stmt|;
specifier|private
name|boolean
name|invokeDestinationCalled
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|ServletDestination
name|getDestination
parameter_list|(
name|String
name|address
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|updateDests
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|)
block|{          }
annotation|@
name|Override
specifier|protected
name|ServletDestination
name|checkRestfulRequest
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|)
throws|throws
name|IOException
block|{
name|ServletDestination
name|sd
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|ServletDestination
operator|.
name|class
argument_list|)
decl_stmt|;
name|sd
operator|.
name|getMessageObserver
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|EasyMock
operator|.
name|createMock
argument_list|(
name|MessageObserver
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|sd
argument_list|)
expr_stmt|;
return|return
name|sd
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|invokeDestination
parameter_list|(
specifier|final
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|,
name|ServletDestination
name|d
parameter_list|)
throws|throws
name|ServletException
block|{
name|invokeDestinationCalled
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|generateServiceList
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|IOException
block|{
name|generateListCalled
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|generateUnformattedServiceList
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|IOException
block|{
name|generateUnformattedCalled
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|boolean
name|generateListCalled
parameter_list|()
block|{
return|return
name|generateListCalled
return|;
block|}
specifier|public
name|boolean
name|generateUnformattedCalled
parameter_list|()
block|{
return|return
name|generateUnformattedCalled
return|;
block|}
specifier|public
name|boolean
name|invokeDestinationCalled
parameter_list|()
block|{
return|return
name|invokeDestinationCalled
return|;
block|}
block|}
block|}
end_class

end_unit

