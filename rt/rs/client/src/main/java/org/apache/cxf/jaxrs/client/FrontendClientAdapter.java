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
name|jaxrs
operator|.
name|client
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
name|ClientCallback
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
name|BindingOperationInfo
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
name|Conduit
import|;
end_import

begin_comment
comment|/**  * The adapter between JAX-RS and CXF Frontend Client, can be used in cases  * when org.apache.cxf.endpoint.Client is expected by the runtime, example when  * bus level features have to applied to a given JAX-RS client instance, etc  */
end_comment

begin_class
class|class
name|FrontendClientAdapter
implements|implements
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|endpoint
operator|.
name|Client
block|{
specifier|private
name|ClientConfiguration
name|config
decl_stmt|;
name|FrontendClientAdapter
parameter_list|(
name|ClientConfiguration
name|config
parameter_list|)
block|{
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
block|}
annotation|@
name|Override
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
name|config
operator|.
name|getInInterceptors
argument_list|()
return|;
block|}
annotation|@
name|Override
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
name|config
operator|.
name|getOutInterceptors
argument_list|()
return|;
block|}
annotation|@
name|Override
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
name|config
operator|.
name|getInFaultInterceptors
argument_list|()
return|;
block|}
annotation|@
name|Override
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
name|config
operator|.
name|getOutFaultInterceptors
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Conduit
name|getConduit
parameter_list|()
block|{
return|return
name|config
operator|.
name|getConduit
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|ConduitSelector
name|getConduitSelector
parameter_list|()
block|{
return|return
name|config
operator|.
name|getConduitSelector
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setConduitSelector
parameter_list|(
name|ConduitSelector
name|selector
parameter_list|)
block|{
name|config
operator|.
name|setConduitSelector
argument_list|(
name|selector
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Bus
name|getBus
parameter_list|()
block|{
return|return
name|config
operator|.
name|getBus
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|config
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|destroy
parameter_list|()
block|{
comment|//complete, the actual JAX-RS Client will be closed via a different path
block|}
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getRequestContext
parameter_list|()
block|{
return|return
name|config
operator|.
name|getRequestContext
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getResponseContext
parameter_list|()
block|{
return|return
name|config
operator|.
name|getResponseContext
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setThreadLocalRequestContext
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isThreadLocalRequestContext
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setExecutor
parameter_list|(
name|Executor
name|executor
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|Object
index|[]
name|invoke
parameter_list|(
name|String
name|operationName
parameter_list|,
name|Object
modifier|...
name|params
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|Object
index|[]
name|invoke
parameter_list|(
name|QName
name|operationName
parameter_list|,
name|Object
modifier|...
name|params
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|Object
index|[]
name|invokeWrapped
parameter_list|(
name|String
name|operationName
parameter_list|,
name|Object
modifier|...
name|params
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|Object
index|[]
name|invokeWrapped
parameter_list|(
name|QName
name|operationName
parameter_list|,
name|Object
modifier|...
name|params
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|Object
index|[]
name|invoke
parameter_list|(
name|BindingOperationInfo
name|oi
parameter_list|,
name|Object
modifier|...
name|params
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|Object
index|[]
name|invoke
parameter_list|(
name|BindingOperationInfo
name|oi
parameter_list|,
name|Object
index|[]
name|params
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|context
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|Object
index|[]
name|invoke
parameter_list|(
name|BindingOperationInfo
name|oi
parameter_list|,
name|Object
index|[]
name|params
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|context
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|invoke
parameter_list|(
name|ClientCallback
name|callback
parameter_list|,
name|String
name|operationName
parameter_list|,
name|Object
modifier|...
name|params
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|invoke
parameter_list|(
name|ClientCallback
name|callback
parameter_list|,
name|QName
name|operationName
parameter_list|,
name|Object
modifier|...
name|params
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|invokeWrapped
parameter_list|(
name|ClientCallback
name|callback
parameter_list|,
name|String
name|operationName
parameter_list|,
name|Object
modifier|...
name|params
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|invokeWrapped
parameter_list|(
name|ClientCallback
name|callback
parameter_list|,
name|QName
name|operationName
parameter_list|,
name|Object
modifier|...
name|params
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|invoke
parameter_list|(
name|ClientCallback
name|callback
parameter_list|,
name|BindingOperationInfo
name|oi
parameter_list|,
name|Object
modifier|...
name|params
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|invoke
parameter_list|(
name|ClientCallback
name|callback
parameter_list|,
name|BindingOperationInfo
name|oi
parameter_list|,
name|Object
index|[]
name|params
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|context
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|invoke
parameter_list|(
name|ClientCallback
name|callback
parameter_list|,
name|BindingOperationInfo
name|oi
parameter_list|,
name|Object
index|[]
name|params
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|invoke
parameter_list|(
name|ClientCallback
name|callback
parameter_list|,
name|BindingOperationInfo
name|oi
parameter_list|,
name|Object
index|[]
name|params
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|context
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|Exception
block|{
name|destroy
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

