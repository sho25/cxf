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
name|clustering
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
name|endpoint
operator|.
name|Client
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
name|feature
operator|.
name|AbstractFeature
import|;
end_import

begin_comment
comment|/**  * This feature may be applied to a Client so as to enable  * failover from the initial target endpoint to any other  * compatible endpoint for the target service.  */
end_comment

begin_class
specifier|public
class|class
name|FailoverFeature
extends|extends
name|AbstractFeature
block|{
specifier|private
name|FailoverStrategy
name|failoverStrategy
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|initialize
parameter_list|(
name|Client
name|client
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|FailoverTargetSelector
name|selector
init|=
operator|new
name|FailoverTargetSelector
argument_list|()
decl_stmt|;
name|selector
operator|.
name|setEndpoint
argument_list|(
name|client
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|selector
operator|.
name|setStrategy
argument_list|(
name|getStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|setConduitSelector
argument_list|(
name|selector
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setStrategy
parameter_list|(
name|FailoverStrategy
name|strategy
parameter_list|)
block|{
name|failoverStrategy
operator|=
name|strategy
expr_stmt|;
block|}
specifier|public
name|FailoverStrategy
name|getStrategy
parameter_list|()
block|{
return|return
name|failoverStrategy
return|;
block|}
block|}
end_class

end_unit

