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
name|service
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
name|HashMap
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
name|concurrent
operator|.
name|Executor
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|configuration
operator|.
name|Configurable
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
name|databinding
operator|.
name|DataBinding
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
name|interceptor
operator|.
name|AbstractAttributedInterceptorProvider
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
name|invoker
operator|.
name|Invoker
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
name|service
operator|.
name|model
operator|.
name|ServiceInfo
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
name|workqueue
operator|.
name|SynchronousExecutor
import|;
end_import

begin_class
specifier|public
class|class
name|ServiceImpl
extends|extends
name|AbstractAttributedInterceptorProvider
implements|implements
name|Service
implements|,
name|Configurable
block|{
specifier|private
name|List
argument_list|<
name|ServiceInfo
argument_list|>
name|serviceInfos
decl_stmt|;
specifier|private
name|DataBinding
name|dataBinding
decl_stmt|;
specifier|private
name|Executor
name|executor
decl_stmt|;
specifier|private
name|Invoker
name|invoker
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|QName
argument_list|,
name|Endpoint
argument_list|>
name|endpoints
init|=
operator|new
name|HashMap
argument_list|<
name|QName
argument_list|,
name|Endpoint
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|ServiceImpl
parameter_list|()
block|{
name|this
argument_list|(
operator|(
name|ServiceInfo
operator|)
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ServiceImpl
parameter_list|(
name|ServiceInfo
name|si
parameter_list|)
block|{
name|serviceInfos
operator|=
operator|new
name|ArrayList
argument_list|<
name|ServiceInfo
argument_list|>
argument_list|()
expr_stmt|;
if|if
condition|(
name|si
operator|!=
literal|null
condition|)
block|{
name|serviceInfos
operator|.
name|add
argument_list|(
name|si
argument_list|)
expr_stmt|;
block|}
name|executor
operator|=
name|SynchronousExecutor
operator|.
name|getInstance
argument_list|()
expr_stmt|;
block|}
specifier|public
name|ServiceImpl
parameter_list|(
name|List
argument_list|<
name|ServiceInfo
argument_list|>
name|si
parameter_list|)
block|{
name|serviceInfos
operator|=
name|si
expr_stmt|;
name|executor
operator|=
name|SynchronousExecutor
operator|.
name|getInstance
argument_list|()
expr_stmt|;
block|}
specifier|public
name|String
name|getBeanName
parameter_list|()
block|{
return|return
name|getName
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|QName
name|getName
parameter_list|()
block|{
return|return
name|serviceInfos
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|public
name|List
argument_list|<
name|ServiceInfo
argument_list|>
name|getServiceInfos
parameter_list|()
block|{
return|return
name|serviceInfos
return|;
block|}
specifier|public
name|EndpointInfo
name|getEndpointInfo
parameter_list|(
name|QName
name|endpoint
parameter_list|)
block|{
for|for
control|(
name|ServiceInfo
name|inf
range|:
name|serviceInfos
control|)
block|{
name|EndpointInfo
name|ef
init|=
name|inf
operator|.
name|getEndpoint
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
if|if
condition|(
name|ef
operator|!=
literal|null
condition|)
block|{
return|return
name|ef
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|Executor
name|getExecutor
parameter_list|()
block|{
return|return
name|executor
return|;
block|}
specifier|public
name|void
name|setExecutor
parameter_list|(
name|Executor
name|executor
parameter_list|)
block|{
name|this
operator|.
name|executor
operator|=
name|executor
expr_stmt|;
block|}
specifier|public
name|Invoker
name|getInvoker
parameter_list|()
block|{
return|return
name|invoker
return|;
block|}
specifier|public
name|void
name|setInvoker
parameter_list|(
name|Invoker
name|invoker
parameter_list|)
block|{
name|this
operator|.
name|invoker
operator|=
name|invoker
expr_stmt|;
block|}
specifier|public
name|DataBinding
name|getDataBinding
parameter_list|()
block|{
return|return
name|dataBinding
return|;
block|}
specifier|public
name|void
name|setDataBinding
parameter_list|(
name|DataBinding
name|dataBinding
parameter_list|)
block|{
name|this
operator|.
name|dataBinding
operator|=
name|dataBinding
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|QName
argument_list|,
name|Endpoint
argument_list|>
name|getEndpoints
parameter_list|()
block|{
return|return
name|endpoints
return|;
block|}
specifier|public
name|void
name|setEndpoints
parameter_list|(
name|Map
argument_list|<
name|QName
argument_list|,
name|Endpoint
argument_list|>
name|endpoints
parameter_list|)
block|{
name|this
operator|.
name|endpoints
operator|=
name|endpoints
expr_stmt|;
block|}
specifier|public
name|void
name|setProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
name|this
operator|.
name|putAll
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

