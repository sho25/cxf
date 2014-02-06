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
name|jms
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicLong
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

begin_class
specifier|public
class|class
name|JMSUtilTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testCorrelationIDGeneration
parameter_list|()
block|{
specifier|final
name|String
name|conduitId
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|replaceAll
argument_list|(
literal|"-"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
comment|// test min edge case
name|AtomicLong
name|messageMinCount
init|=
operator|new
name|AtomicLong
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|createAndCheck
argument_list|(
name|conduitId
argument_list|,
literal|"0000000000000000"
argument_list|,
name|messageMinCount
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
comment|// test max edge case
name|AtomicLong
name|messageMaxCount
init|=
operator|new
name|AtomicLong
argument_list|(
literal|0xFFFFFFFFFFFFFFFFL
argument_list|)
decl_stmt|;
name|createAndCheck
argument_list|(
name|conduitId
argument_list|,
literal|"ffffffffffffffff"
argument_list|,
name|messageMaxCount
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
comment|// test overflow case
name|AtomicLong
name|overflowCount
init|=
operator|new
name|AtomicLong
argument_list|(
literal|0xFFFFFFFFFFFFFFFFL
argument_list|)
decl_stmt|;
name|createAndCheck
argument_list|(
name|conduitId
argument_list|,
literal|"0000000000000000"
argument_list|,
name|overflowCount
operator|.
name|incrementAndGet
argument_list|()
argument_list|)
expr_stmt|;
comment|// Test sequence
name|AtomicLong
name|sequence
init|=
operator|new
name|AtomicLong
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|createAndCheck
argument_list|(
name|conduitId
argument_list|,
literal|"0000000000000001"
argument_list|,
name|sequence
operator|.
name|incrementAndGet
argument_list|()
argument_list|)
expr_stmt|;
name|createAndCheck
argument_list|(
name|conduitId
argument_list|,
literal|"0000000000000002"
argument_list|,
name|sequence
operator|.
name|incrementAndGet
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createAndCheck
parameter_list|(
name|String
name|prefix
parameter_list|,
specifier|final
name|String
name|expectedIndex
parameter_list|,
name|long
name|sequenceNum
parameter_list|)
block|{
name|String
name|correlationID
init|=
name|JMSUtil
operator|.
name|createCorrelationId
argument_list|(
name|prefix
argument_list|,
name|sequenceNum
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"The correlationID value does not match expected value"
argument_list|,
name|prefix
operator|+
name|expectedIndex
argument_list|,
name|correlationID
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

