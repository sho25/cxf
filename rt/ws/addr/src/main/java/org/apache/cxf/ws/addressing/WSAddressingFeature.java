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
name|addressing
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
name|addressing
operator|.
name|soap
operator|.
name|MAPCodec
import|;
end_import

begin_class
annotation|@
name|NoJSR250Annotations
specifier|public
class|class
name|WSAddressingFeature
extends|extends
name|AbstractFeature
block|{
specifier|private
name|MAPAggregator
name|mapAggregator
init|=
operator|new
name|MAPAggregator
argument_list|()
decl_stmt|;
specifier|private
name|MAPCodec
name|mapCodec
init|=
operator|new
name|MAPCodec
argument_list|()
decl_stmt|;
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
name|provider
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|mapAggregator
argument_list|)
expr_stmt|;
name|provider
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|mapCodec
argument_list|)
expr_stmt|;
name|provider
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|mapAggregator
argument_list|)
expr_stmt|;
name|provider
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|mapCodec
argument_list|)
expr_stmt|;
name|provider
operator|.
name|getInFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|mapAggregator
argument_list|)
expr_stmt|;
name|provider
operator|.
name|getInFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|mapCodec
argument_list|)
expr_stmt|;
name|provider
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|mapAggregator
argument_list|)
expr_stmt|;
name|provider
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|mapCodec
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setAllowDuplicates
parameter_list|(
name|boolean
name|allow
parameter_list|)
block|{
name|mapAggregator
operator|.
name|setAllowDuplicates
argument_list|(
name|allow
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isAllowDuplicates
parameter_list|()
block|{
return|return
name|mapAggregator
operator|.
name|allowDuplicates
argument_list|()
return|;
block|}
specifier|public
name|void
name|setUsingAddressingAdvisory
parameter_list|(
name|boolean
name|advisory
parameter_list|)
block|{
name|mapAggregator
operator|.
name|setUsingAddressingAdvisory
argument_list|(
name|advisory
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isUsingAddressingAdvisory
parameter_list|()
block|{
return|return
name|mapAggregator
operator|.
name|isUsingAddressingAdvisory
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isAddressingRequired
parameter_list|()
block|{
return|return
name|mapAggregator
operator|.
name|isAddressingRequired
argument_list|()
return|;
block|}
specifier|public
name|void
name|setAddressingRequired
parameter_list|(
name|boolean
name|required
parameter_list|)
block|{
name|mapAggregator
operator|.
name|setAddressingRequired
argument_list|(
name|required
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

