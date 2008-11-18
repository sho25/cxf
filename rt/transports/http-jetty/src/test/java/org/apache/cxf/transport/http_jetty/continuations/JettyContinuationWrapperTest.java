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
operator|.
name|continuations
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|continuations
operator|.
name|ContinuationInfo
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
name|message
operator|.
name|MessageImpl
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
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|util
operator|.
name|ajax
operator|.
name|Continuation
import|;
end_import

begin_class
specifier|public
class|class
name|JettyContinuationWrapperTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testContinuationInterface
parameter_list|()
block|{
name|Message
name|m
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|ContinuationInfo
name|ci
init|=
operator|new
name|ContinuationInfo
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|Object
name|userObject
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|Continuation
name|c
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Continuation
operator|.
name|class
argument_list|)
decl_stmt|;
name|c
operator|.
name|isNew
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|c
operator|.
name|isPending
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|c
operator|.
name|isResumed
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|c
operator|.
name|getObject
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|ci
argument_list|)
operator|.
name|times
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|c
operator|.
name|setObject
argument_list|(
name|ci
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|c
operator|.
name|reset
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|c
operator|.
name|resume
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|c
operator|.
name|suspend
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|JettyContinuationWrapper
name|cw
init|=
operator|new
name|JettyContinuationWrapper
argument_list|(
name|c
argument_list|,
name|m
argument_list|)
decl_stmt|;
name|cw
operator|.
name|isNew
argument_list|()
expr_stmt|;
name|cw
operator|.
name|isPending
argument_list|()
expr_stmt|;
name|cw
operator|.
name|isResumed
argument_list|()
expr_stmt|;
name|assertSame
argument_list|(
name|ci
argument_list|,
name|cw
operator|.
name|getObject
argument_list|()
argument_list|)
expr_stmt|;
name|cw
operator|.
name|setObject
argument_list|(
name|userObject
argument_list|)
expr_stmt|;
name|cw
operator|.
name|reset
argument_list|()
expr_stmt|;
name|cw
operator|.
name|resume
argument_list|()
expr_stmt|;
name|cw
operator|.
name|suspend
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|userObject
argument_list|,
name|ci
operator|.
name|getUserObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

