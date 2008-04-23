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
name|databinding
operator|.
name|stax
package|;
end_package

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
name|endpoint
operator|.
name|Server
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|interceptor
operator|.
name|BareInInterceptor
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
name|interceptor
operator|.
name|DocLiteralInInterceptor
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
name|interceptor
operator|.
name|Interceptor
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
name|interceptor
operator|.
name|WrappedInInterceptor
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
name|phase
operator|.
name|PhaseInterceptor
import|;
end_import

begin_class
specifier|public
class|class
name|StaxDataBindingFeature
extends|extends
name|AbstractFeature
block|{
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
name|removeDatabindingInterceptor
argument_list|(
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|initialize
parameter_list|(
name|Server
name|server
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|removeDatabindingInterceptor
argument_list|(
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|removeDatabindingInterceptor
parameter_list|(
name|List
argument_list|<
name|Interceptor
argument_list|>
name|inInterceptors
parameter_list|)
block|{
name|removeInterceptor
argument_list|(
name|inInterceptors
argument_list|,
name|DocLiteralInInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|removeInterceptor
argument_list|(
name|inInterceptors
argument_list|,
name|BareInInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|removeInterceptor
argument_list|(
name|inInterceptors
argument_list|,
name|WrappedInInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|inInterceptors
operator|.
name|add
argument_list|(
operator|new
name|StaxDataBindingInterceptor
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|removeInterceptor
parameter_list|(
name|List
argument_list|<
name|Interceptor
argument_list|>
name|inInterceptors
parameter_list|,
name|String
name|name
parameter_list|)
block|{
for|for
control|(
name|Interceptor
name|i
range|:
name|inInterceptors
control|)
block|{
if|if
condition|(
name|i
operator|instanceof
name|PhaseInterceptor
condition|)
block|{
name|PhaseInterceptor
name|p
init|=
operator|(
name|PhaseInterceptor
operator|)
name|i
decl_stmt|;
if|if
condition|(
name|p
operator|.
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|inInterceptors
operator|.
name|remove
argument_list|(
name|p
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

