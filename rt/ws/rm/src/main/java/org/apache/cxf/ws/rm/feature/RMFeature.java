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
name|ws
operator|.
name|rm
operator|.
name|feature
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
name|common
operator|.
name|injection
operator|.
name|NoJSR250Annotations
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
name|InterceptorProvider
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
name|ws
operator|.
name|rm
operator|.
name|RMCaptureInInterceptor
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
name|ws
operator|.
name|rm
operator|.
name|RMDeliveryInterceptor
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
name|ws
operator|.
name|rm
operator|.
name|RMInInterceptor
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
name|ws
operator|.
name|rm
operator|.
name|RMManager
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
name|ws
operator|.
name|rm
operator|.
name|RMOutInterceptor
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
name|ws
operator|.
name|rm
operator|.
name|manager
operator|.
name|DeliveryAssuranceType
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
name|ws
operator|.
name|rm
operator|.
name|manager
operator|.
name|DestinationPolicyType
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
name|ws
operator|.
name|rm
operator|.
name|manager
operator|.
name|RM10AddressingNamespaceType
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
name|ws
operator|.
name|rm
operator|.
name|manager
operator|.
name|SourcePolicyType
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
name|ws
operator|.
name|rm
operator|.
name|persistence
operator|.
name|RMStore
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
name|ws
operator|.
name|rm
operator|.
name|soap
operator|.
name|RMSoapInterceptor
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
name|ws
operator|.
name|rmp
operator|.
name|v200502
operator|.
name|RMAssertion
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
annotation|@
name|NoJSR250Annotations
specifier|public
class|class
name|RMFeature
extends|extends
name|AbstractFeature
block|{
specifier|private
name|RMAssertion
name|rmAssertion
decl_stmt|;
specifier|private
name|DeliveryAssuranceType
name|deliveryAssurance
decl_stmt|;
specifier|private
name|SourcePolicyType
name|sourcePolicy
decl_stmt|;
specifier|private
name|DestinationPolicyType
name|destinationPolicy
decl_stmt|;
specifier|private
name|String
name|rmNamespace
decl_stmt|;
specifier|private
name|RM10AddressingNamespaceType
name|rm10AddressingNamespace
decl_stmt|;
specifier|private
name|RMStore
name|store
decl_stmt|;
specifier|private
name|RMInInterceptor
name|rmLogicalIn
init|=
operator|new
name|RMInInterceptor
argument_list|()
decl_stmt|;
specifier|private
name|RMOutInterceptor
name|rmLogicalOut
init|=
operator|new
name|RMOutInterceptor
argument_list|()
decl_stmt|;
specifier|private
name|RMDeliveryInterceptor
name|rmDelivery
init|=
operator|new
name|RMDeliveryInterceptor
argument_list|()
decl_stmt|;
specifier|private
name|RMSoapInterceptor
name|rmCodec
init|=
operator|new
name|RMSoapInterceptor
argument_list|()
decl_stmt|;
specifier|private
name|RMCaptureInInterceptor
name|rmCaptureIn
init|=
operator|new
name|RMCaptureInInterceptor
argument_list|()
decl_stmt|;
specifier|public
name|void
name|setDeliveryAssurance
parameter_list|(
name|DeliveryAssuranceType
name|da
parameter_list|)
block|{
name|deliveryAssurance
operator|=
name|da
expr_stmt|;
block|}
specifier|public
name|void
name|setDestinationPolicy
parameter_list|(
name|DestinationPolicyType
name|dp
parameter_list|)
block|{
name|destinationPolicy
operator|=
name|dp
expr_stmt|;
block|}
specifier|public
name|void
name|setRMAssertion
parameter_list|(
name|RMAssertion
name|rma
parameter_list|)
block|{
name|rmAssertion
operator|=
name|rma
expr_stmt|;
block|}
specifier|public
name|void
name|setSourcePolicy
parameter_list|(
name|SourcePolicyType
name|sp
parameter_list|)
block|{
name|sourcePolicy
operator|=
name|sp
expr_stmt|;
block|}
specifier|public
name|void
name|setStore
parameter_list|(
name|RMStore
name|store
parameter_list|)
block|{
name|this
operator|.
name|store
operator|=
name|store
expr_stmt|;
block|}
specifier|public
name|void
name|setRMNamespace
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|rmNamespace
operator|=
name|uri
expr_stmt|;
block|}
specifier|public
name|void
name|setRM10AddressingNamespace
parameter_list|(
name|RM10AddressingNamespaceType
name|addrns
parameter_list|)
block|{
name|rm10AddressingNamespace
operator|=
name|addrns
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|initializeProvider
parameter_list|(
name|InterceptorProvider
name|provider
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|RMManager
name|manager
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|RMManager
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|rmAssertion
condition|)
block|{
name|manager
operator|.
name|setRMAssertion
argument_list|(
name|rmAssertion
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|null
operator|!=
name|deliveryAssurance
condition|)
block|{
name|manager
operator|.
name|setDeliveryAssurance
argument_list|(
name|deliveryAssurance
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|null
operator|!=
name|sourcePolicy
condition|)
block|{
name|manager
operator|.
name|setSourcePolicy
argument_list|(
name|sourcePolicy
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|null
operator|!=
name|destinationPolicy
condition|)
block|{
name|manager
operator|.
name|setDestinationPolicy
argument_list|(
name|destinationPolicy
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|null
operator|!=
name|store
condition|)
block|{
name|manager
operator|.
name|setStore
argument_list|(
name|store
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|null
operator|!=
name|rmNamespace
condition|)
block|{
name|manager
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setRMNamespace
argument_list|(
name|rmNamespace
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|null
operator|!=
name|rm10AddressingNamespace
condition|)
block|{
name|manager
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setRM10AddressingNamespace
argument_list|(
name|rm10AddressingNamespace
argument_list|)
expr_stmt|;
block|}
name|rmLogicalIn
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|rmLogicalOut
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|rmDelivery
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|rmCaptureIn
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|provider
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|rmLogicalIn
argument_list|)
expr_stmt|;
name|provider
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|rmCodec
argument_list|)
expr_stmt|;
name|provider
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|rmDelivery
argument_list|)
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|store
condition|)
block|{
name|provider
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|rmCaptureIn
argument_list|)
expr_stmt|;
block|}
name|provider
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|rmLogicalOut
argument_list|)
expr_stmt|;
name|provider
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|rmCodec
argument_list|)
expr_stmt|;
name|provider
operator|.
name|getInFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|rmLogicalIn
argument_list|)
expr_stmt|;
name|provider
operator|.
name|getInFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|rmCodec
argument_list|)
expr_stmt|;
name|provider
operator|.
name|getInFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|rmDelivery
argument_list|)
expr_stmt|;
name|provider
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|rmLogicalOut
argument_list|)
expr_stmt|;
name|provider
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|rmCodec
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

