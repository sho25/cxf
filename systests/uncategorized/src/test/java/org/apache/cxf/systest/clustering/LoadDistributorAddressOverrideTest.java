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
name|clustering
package|;
end_package

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
name|WebServiceException
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
name|clustering
operator|.
name|LoadDistributorTargetSelector
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
name|clustering
operator|.
name|SequentialStrategy
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
name|ConduitSelector
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|LoadDistributorAddressOverrideTest
extends|extends
name|FailoverAddressOverrideTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|FAILOVER_CONFIG
init|=
literal|"org/apache/cxf/systest/clustering/load_distributor_address_override.xml"
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|String
name|getConfig
parameter_list|()
block|{
return|return
name|FAILOVER_CONFIG
return|;
block|}
specifier|private
name|String
name|responseFrom
parameter_list|(
name|String
name|response
parameter_list|)
block|{
if|if
condition|(
name|response
operator|.
name|endsWith
argument_list|(
name|REPLICA_A
argument_list|)
condition|)
block|{
return|return
name|REPLICA_A
return|;
block|}
elseif|else
if|if
condition|(
name|response
operator|.
name|endsWith
argument_list|(
name|REPLICA_B
argument_list|)
condition|)
block|{
return|return
name|REPLICA_B
return|;
block|}
elseif|else
if|if
condition|(
name|response
operator|.
name|endsWith
argument_list|(
name|REPLICA_C
argument_list|)
condition|)
block|{
return|return
name|REPLICA_C
return|;
block|}
else|else
block|{
return|return
name|response
return|;
block|}
block|}
specifier|private
name|void
name|incrementResponseCount
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|responseCounts
parameter_list|,
name|String
name|response
parameter_list|)
block|{
name|String
name|responder
init|=
name|responseFrom
argument_list|(
name|response
argument_list|)
decl_stmt|;
name|Integer
name|currentCount
init|=
name|responseCounts
operator|.
name|get
argument_list|(
name|responder
argument_list|)
decl_stmt|;
name|responseCounts
operator|.
name|put
argument_list|(
name|responder
argument_list|,
literal|1
operator|+
operator|(
name|currentCount
operator|==
literal|null
condition|?
literal|0
else|:
name|currentCount
operator|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDistributedSequentialStrategy
parameter_list|()
throws|throws
name|Exception
block|{
name|startTarget
argument_list|(
name|REPLICA_A
argument_list|)
expr_stmt|;
name|startTarget
argument_list|(
name|REPLICA_B
argument_list|)
expr_stmt|;
name|startTarget
argument_list|(
name|REPLICA_C
argument_list|)
expr_stmt|;
name|setupGreeterA
argument_list|()
expr_stmt|;
name|verifyStrategy
argument_list|(
name|greeter
argument_list|,
name|SequentialStrategy
operator|.
name|class
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|responseCounts
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|12
condition|;
operator|++
name|i
control|)
block|{
name|String
name|response
init|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"fred"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"expected non-null response"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|incrementResponseCount
argument_list|(
name|responseCounts
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|4
argument_list|,
operator|(
name|long
operator|)
name|responseCounts
operator|.
name|get
argument_list|(
name|REPLICA_A
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
operator|(
name|long
operator|)
name|responseCounts
operator|.
name|get
argument_list|(
name|REPLICA_B
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
operator|(
name|long
operator|)
name|responseCounts
operator|.
name|get
argument_list|(
name|REPLICA_C
argument_list|)
argument_list|)
expr_stmt|;
name|verifyCurrentEndpoint
argument_list|(
name|REPLICA_C
argument_list|)
expr_stmt|;
name|stopTarget
argument_list|(
name|REPLICA_A
argument_list|)
expr_stmt|;
name|stopTarget
argument_list|(
name|REPLICA_B
argument_list|)
expr_stmt|;
name|stopTarget
argument_list|(
name|REPLICA_C
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDistributedSequentialStrategyWithFailover
parameter_list|()
throws|throws
name|Exception
block|{
name|startTarget
argument_list|(
name|REPLICA_A
argument_list|)
expr_stmt|;
name|startTarget
argument_list|(
name|REPLICA_C
argument_list|)
expr_stmt|;
name|setupGreeterA
argument_list|()
expr_stmt|;
name|verifyStrategy
argument_list|(
name|greeter
argument_list|,
name|SequentialStrategy
operator|.
name|class
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|responseCounts
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|12
condition|;
operator|++
name|i
control|)
block|{
name|String
name|response
init|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"fred"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"expected non-null response"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|incrementResponseCount
argument_list|(
name|responseCounts
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
comment|// Note that when failover occurs the address list is requeried
comment|// so SequentialStrategy it starts again from the beginning
comment|// (the failed address is removed from the returned list, if it's there).
name|assertEquals
argument_list|(
literal|8
argument_list|,
operator|(
name|long
operator|)
name|responseCounts
operator|.
name|get
argument_list|(
name|REPLICA_A
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|responseCounts
operator|.
name|get
argument_list|(
name|REPLICA_B
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
operator|(
name|long
operator|)
name|responseCounts
operator|.
name|get
argument_list|(
name|REPLICA_C
argument_list|)
argument_list|)
expr_stmt|;
name|verifyCurrentEndpoint
argument_list|(
name|REPLICA_C
argument_list|)
expr_stmt|;
name|stopTarget
argument_list|(
name|REPLICA_A
argument_list|)
expr_stmt|;
name|stopTarget
argument_list|(
name|REPLICA_C
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDistributedSequentialStrategyWithoutFailover
parameter_list|()
throws|throws
name|Exception
block|{
name|startTarget
argument_list|(
name|REPLICA_A
argument_list|)
expr_stmt|;
name|startTarget
argument_list|(
name|REPLICA_C
argument_list|)
expr_stmt|;
name|setupGreeterA
argument_list|()
expr_stmt|;
name|verifyStrategy
argument_list|(
name|greeter
argument_list|,
name|SequentialStrategy
operator|.
name|class
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|ConduitSelector
name|conduitSelector
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|greeter
argument_list|)
operator|.
name|getConduitSelector
argument_list|()
decl_stmt|;
if|if
condition|(
name|conduitSelector
operator|instanceof
name|LoadDistributorTargetSelector
condition|)
block|{
operator|(
operator|(
name|LoadDistributorTargetSelector
operator|)
name|conduitSelector
operator|)
operator|.
name|setFailover
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fail
argument_list|(
literal|"unexpected conduit selector: "
operator|+
name|conduitSelector
argument_list|)
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|responseCounts
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|12
condition|;
operator|++
name|i
control|)
block|{
try|try
block|{
name|String
name|response
init|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"fred"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"expected non-null response"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|incrementResponseCount
argument_list|(
name|responseCounts
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebServiceException
name|ex
parameter_list|)
block|{
name|incrementResponseCount
argument_list|(
name|responseCounts
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
block|}
name|assertEquals
argument_list|(
literal|4
argument_list|,
operator|(
name|long
operator|)
name|responseCounts
operator|.
name|get
argument_list|(
name|REPLICA_A
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|responseCounts
operator|.
name|get
argument_list|(
name|REPLICA_B
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
operator|(
name|long
operator|)
name|responseCounts
operator|.
name|get
argument_list|(
name|REPLICA_C
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
operator|(
name|long
operator|)
name|responseCounts
operator|.
name|get
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|verifyCurrentEndpoint
argument_list|(
name|REPLICA_C
argument_list|)
expr_stmt|;
name|stopTarget
argument_list|(
name|REPLICA_A
argument_list|)
expr_stmt|;
name|stopTarget
argument_list|(
name|REPLICA_C
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

