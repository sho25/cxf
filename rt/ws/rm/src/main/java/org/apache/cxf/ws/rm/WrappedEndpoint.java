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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|Executor
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
name|binding
operator|.
name|Binding
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
name|Endpoint
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
name|Service
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
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|MessageObserver
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
name|MAPAggregator
import|;
end_import

begin_class
specifier|public
class|class
name|WrappedEndpoint
implements|implements
name|Endpoint
block|{
specifier|private
name|Endpoint
name|wrappedEndpoint
decl_stmt|;
specifier|private
name|EndpointInfo
name|endpointInfo
decl_stmt|;
specifier|private
name|Service
name|service
decl_stmt|;
specifier|private
name|Boolean
name|usingAddressing
decl_stmt|;
name|WrappedEndpoint
parameter_list|(
name|Endpoint
name|wrapped
parameter_list|,
name|EndpointInfo
name|info
parameter_list|,
name|Service
name|s
parameter_list|)
block|{
name|wrappedEndpoint
operator|=
name|wrapped
expr_stmt|;
name|endpointInfo
operator|=
name|info
expr_stmt|;
name|service
operator|=
name|s
expr_stmt|;
block|}
specifier|public
name|Endpoint
name|getWrappedEndpoint
parameter_list|()
block|{
return|return
name|wrappedEndpoint
return|;
block|}
specifier|public
name|EndpointInfo
name|getEndpointInfo
parameter_list|()
block|{
return|return
name|endpointInfo
return|;
block|}
specifier|public
name|Service
name|getService
parameter_list|()
block|{
return|return
name|service
return|;
block|}
specifier|public
name|Binding
name|getBinding
parameter_list|()
block|{
return|return
name|wrappedEndpoint
operator|.
name|getBinding
argument_list|()
return|;
block|}
specifier|public
name|Executor
name|getExecutor
parameter_list|()
block|{
return|return
name|wrappedEndpoint
operator|.
name|getExecutor
argument_list|()
return|;
block|}
specifier|public
name|MessageObserver
name|getInFaultObserver
parameter_list|()
block|{
return|return
name|wrappedEndpoint
operator|.
name|getInFaultObserver
argument_list|()
return|;
block|}
specifier|public
name|MessageObserver
name|getOutFaultObserver
parameter_list|()
block|{
return|return
name|wrappedEndpoint
operator|.
name|getOutFaultObserver
argument_list|()
return|;
block|}
specifier|public
name|void
name|setExecutor
parameter_list|(
name|Executor
name|arg0
parameter_list|)
block|{
name|wrappedEndpoint
operator|.
name|setExecutor
argument_list|(
name|arg0
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setInFaultObserver
parameter_list|(
name|MessageObserver
name|arg0
parameter_list|)
block|{
name|wrappedEndpoint
operator|.
name|setInFaultObserver
argument_list|(
name|arg0
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setOutFaultObserver
parameter_list|(
name|MessageObserver
name|arg0
parameter_list|)
block|{
name|wrappedEndpoint
operator|.
name|setOutFaultObserver
argument_list|(
name|arg0
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|getInFaultInterceptors
parameter_list|()
block|{
return|return
name|wrappedEndpoint
operator|.
name|getInFaultInterceptors
argument_list|()
return|;
block|}
specifier|public
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|getInInterceptors
parameter_list|()
block|{
return|return
name|wrappedEndpoint
operator|.
name|getInInterceptors
argument_list|()
return|;
block|}
specifier|public
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|getOutFaultInterceptors
parameter_list|()
block|{
return|return
name|wrappedEndpoint
operator|.
name|getOutFaultInterceptors
argument_list|()
return|;
block|}
specifier|public
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|getOutInterceptors
parameter_list|()
block|{
return|return
name|wrappedEndpoint
operator|.
name|getOutInterceptors
argument_list|()
return|;
block|}
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|wrappedEndpoint
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|public
name|boolean
name|containsKey
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
return|return
name|wrappedEndpoint
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|containsValue
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
return|return
name|wrappedEndpoint
operator|.
name|containsValue
argument_list|(
name|value
argument_list|)
return|;
block|}
specifier|public
name|Set
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|entrySet
parameter_list|()
block|{
return|return
name|wrappedEndpoint
operator|.
name|entrySet
argument_list|()
return|;
block|}
specifier|public
name|Object
name|get
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
if|if
condition|(
name|MAPAggregator
operator|.
name|USING_ADDRESSING
operator|==
name|key
condition|)
block|{
return|return
name|usingAddressing
return|;
block|}
return|return
name|wrappedEndpoint
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|wrappedEndpoint
operator|.
name|isEmpty
argument_list|()
return|;
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|keySet
parameter_list|()
block|{
return|return
name|wrappedEndpoint
operator|.
name|keySet
argument_list|()
return|;
block|}
specifier|public
name|Object
name|put
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|MAPAggregator
operator|.
name|USING_ADDRESSING
operator|==
name|key
condition|)
block|{
name|usingAddressing
operator|=
operator|(
name|Boolean
operator|)
name|value
expr_stmt|;
return|return
literal|null
return|;
block|}
return|return
name|wrappedEndpoint
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
return|;
block|}
specifier|public
name|void
name|putAll
parameter_list|(
name|Map
argument_list|<
name|?
extends|extends
name|String
argument_list|,
name|?
extends|extends
name|Object
argument_list|>
name|t
parameter_list|)
block|{
name|wrappedEndpoint
operator|.
name|putAll
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Object
name|remove
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
return|return
name|wrappedEndpoint
operator|.
name|remove
argument_list|(
name|key
argument_list|)
return|;
block|}
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|wrappedEndpoint
operator|.
name|size
argument_list|()
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|Object
argument_list|>
name|values
parameter_list|()
block|{
return|return
name|wrappedEndpoint
operator|.
name|values
argument_list|()
return|;
block|}
comment|/**      * @return the list of fearures<b>already</b> activated for this endpoint.      */
specifier|public
name|List
argument_list|<
name|AbstractFeature
argument_list|>
name|getActiveFeatures
parameter_list|()
block|{
return|return
name|wrappedEndpoint
operator|.
name|getActiveFeatures
argument_list|()
return|;
block|}
block|}
end_class

end_unit

