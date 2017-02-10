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
name|jaxrs
operator|.
name|failover
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|clustering
operator|.
name|FailoverFeature
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
name|FailoverTargetSelector
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
name|RandomStrategy
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
name|message
operator|.
name|Exchange
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
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Tests failover within a static cluster.  */
end_comment

begin_class
specifier|public
class|class
name|FailoverTest
extends|extends
name|AbstractFailoverTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testSequentialStrategyWithCustomTargetSelector
parameter_list|()
throws|throws
name|Exception
block|{
name|FailoverFeature
name|feature
init|=
name|getCustomFeature
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|,
name|Server
operator|.
name|ADDRESS2
argument_list|,
name|Server
operator|.
name|ADDRESS3
argument_list|)
decl_stmt|;
name|strategyTest
argument_list|(
literal|"resolver://info"
argument_list|,
name|feature
argument_list|,
name|Server
operator|.
name|ADDRESS3
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSequentialStrategyWithCustomTargetSelector2
parameter_list|()
throws|throws
name|Exception
block|{
name|FailoverFeature
name|feature
init|=
name|getCustomFeature
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|,
name|Server
operator|.
name|ADDRESS2
argument_list|,
name|Server
operator|.
name|ADDRESS3
argument_list|)
decl_stmt|;
name|strategyTest
argument_list|(
literal|"resolver://info"
argument_list|,
name|feature
argument_list|,
name|Server
operator|.
name|ADDRESS3
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|FailoverFeature
name|getFeature
parameter_list|(
name|boolean
name|random
parameter_list|,
name|String
modifier|...
name|address
parameter_list|)
block|{
return|return
name|getCustomFeature
argument_list|(
literal|false
argument_list|,
name|random
argument_list|,
name|address
argument_list|)
return|;
block|}
specifier|private
name|FailoverFeature
name|getCustomFeature
parameter_list|(
name|boolean
name|custom
parameter_list|,
name|boolean
name|random
parameter_list|,
name|String
modifier|...
name|address
parameter_list|)
block|{
name|FailoverFeature
name|feature
init|=
operator|new
name|FailoverFeature
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|alternateAddresses
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|s
range|:
name|address
control|)
block|{
name|alternateAddresses
operator|.
name|add
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|random
condition|)
block|{
name|SequentialStrategy
name|strategy
init|=
operator|new
name|SequentialStrategy
argument_list|()
decl_stmt|;
name|strategy
operator|.
name|setAlternateAddresses
argument_list|(
name|alternateAddresses
argument_list|)
expr_stmt|;
name|feature
operator|.
name|setStrategy
argument_list|(
name|strategy
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|RandomStrategy
name|strategy
init|=
operator|new
name|RandomStrategy
argument_list|()
decl_stmt|;
name|strategy
operator|.
name|setAlternateAddresses
argument_list|(
name|alternateAddresses
argument_list|)
expr_stmt|;
name|feature
operator|.
name|setStrategy
argument_list|(
name|strategy
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|custom
condition|)
block|{
name|FailoverTargetSelector
name|selector
init|=
operator|new
name|ReplaceInitialAddressSelector
argument_list|()
decl_stmt|;
name|feature
operator|.
name|setTargetSelector
argument_list|(
name|selector
argument_list|)
expr_stmt|;
block|}
return|return
name|feature
return|;
block|}
specifier|private
specifier|static
class|class
name|ReplaceInitialAddressSelector
extends|extends
name|FailoverTargetSelector
block|{
annotation|@
name|Override
specifier|public
specifier|synchronized
name|void
name|prepare
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|EndpointInfo
name|ei
init|=
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
name|ei
operator|.
name|setAddress
argument_list|(
name|Server
operator|.
name|ADDRESS3
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|ENDPOINT_ADDRESS
argument_list|,
name|Server
operator|.
name|ADDRESS3
argument_list|)
expr_stmt|;
name|super
operator|.
name|prepare
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|requiresFailover
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Exception
name|ex
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
end_class

end_unit

