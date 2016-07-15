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
operator|.
name|circuitbreaker
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
name|clustering
operator|.
name|CircuitBreakerTargetSelector
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

begin_class
specifier|public
class|class
name|CircuitBreakerFailoverFeature
extends|extends
name|FailoverFeature
block|{
specifier|private
name|int
name|threshold
decl_stmt|;
specifier|private
name|long
name|timeout
decl_stmt|;
specifier|private
name|FailoverTargetSelector
name|targetSelector
decl_stmt|;
specifier|public
name|CircuitBreakerFailoverFeature
parameter_list|()
block|{
name|this
argument_list|(
name|CircuitBreakerTargetSelector
operator|.
name|DEFAULT_THESHOLD
argument_list|,
name|CircuitBreakerTargetSelector
operator|.
name|DEFAULT_TIMEOUT
argument_list|)
expr_stmt|;
block|}
specifier|public
name|CircuitBreakerFailoverFeature
parameter_list|(
name|int
name|threshold
parameter_list|,
name|long
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|threshold
operator|=
name|threshold
expr_stmt|;
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
specifier|public
name|CircuitBreakerFailoverFeature
parameter_list|(
name|int
name|threshold
parameter_list|,
name|long
name|timeout
parameter_list|,
name|String
name|clientBootstrapAddress
parameter_list|)
block|{
name|super
argument_list|(
name|clientBootstrapAddress
argument_list|)
expr_stmt|;
name|this
operator|.
name|threshold
operator|=
name|threshold
expr_stmt|;
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|FailoverTargetSelector
name|getTargetSelector
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|targetSelector
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|targetSelector
operator|=
operator|new
name|CircuitBreakerTargetSelector
argument_list|(
name|threshold
argument_list|,
name|timeout
argument_list|,
name|super
operator|.
name|getClientBootstrapAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|this
operator|.
name|targetSelector
return|;
block|}
specifier|public
name|int
name|getThreshold
parameter_list|()
block|{
return|return
name|threshold
return|;
block|}
specifier|public
name|long
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
specifier|public
name|void
name|setThreshold
parameter_list|(
name|int
name|threshold
parameter_list|)
block|{
name|this
operator|.
name|threshold
operator|=
name|threshold
expr_stmt|;
block|}
specifier|public
name|void
name|setTimeout
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
block|}
end_class

end_unit

