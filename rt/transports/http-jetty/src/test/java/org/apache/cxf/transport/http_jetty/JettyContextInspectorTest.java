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
name|http_jetty
package|;
end_package

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
name|easymock
operator|.
name|classextension
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

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|handler
operator|.
name|ContextHandler
import|;
end_import

begin_class
specifier|public
class|class
name|JettyContextInspectorTest
extends|extends
name|Assert
block|{
specifier|private
specifier|static
specifier|final
name|String
name|CONTEXT_PATH
init|=
literal|"/foo/bar"
decl_stmt|;
specifier|private
name|ContextHandler
name|context
decl_stmt|;
specifier|private
name|IMocksControl
name|control
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
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
name|ContextHandler
operator|.
name|class
argument_list|)
expr_stmt|;
name|context
operator|.
name|getContextPath
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|CONTEXT_PATH
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
name|control
operator|=
literal|null
expr_stmt|;
name|context
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetAddress
parameter_list|()
throws|throws
name|Exception
block|{
name|JettyContextInspector
name|inspector
init|=
operator|new
name|JettyContextInspector
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected address"
argument_list|,
name|CONTEXT_PATH
argument_list|,
name|inspector
operator|.
name|getAddress
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

