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
name|endpoint
package|;
end_package

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

begin_interface
specifier|public
interface|interface
name|Client
extends|extends
name|InterceptorProvider
extends|,
name|MessageObserver
block|{
name|String
name|REQUEST_CONTEXT
init|=
literal|"RequestContext"
decl_stmt|;
name|String
name|RESPONSE_CONTEXT
init|=
literal|"ResponseContext"
decl_stmt|;
comment|/**      * Invokes an operation synchronously      * @param operationName The name of the operation to be invoked. The service namespace will be used      * when looking up the BindingOperationInfo.      * @param params  The params that matches the parts of the input message of the operation.  If the       * BindingOperationInfo supports unwrapping, it assumes the params are in the "unwrapped" form.  If       * params are in the wrapped form, use invokeWrapped      * @return The return values that matche the parts of the output message of the operation      */
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
function_decl|;
comment|/**      * Invokes an operation synchronously      * @param operationName The name of the operation to be invoked      * @param params  The params that matches the parts of the input message of the operation.  If the       * BindingOperationInfo supports unwrapping, it assumes the params are in the "unwrapped" form.  If       * params are in the wrapped form, use invokeWrapped      * @return The return values that matche the parts of the output message of the operation      */
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
function_decl|;
comment|/**      * Invokes an operation synchronously      * @param operationName The name of the operation to be invoked. The service namespace will be used      * when looking up the BindingOperationInfo.      * @param params  The params that matches the parts of the input message of the operation      * @return The return values that matche the parts of the output message of the operation      */
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
function_decl|;
comment|/**      * Invokes an operation synchronously      * @param operationName The name of the operation to be invoked      * @param params  The params that matches the parts of the input message of the operation      * @return The return values that matche the parts of the output message of the operation      */
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
function_decl|;
comment|/**      * Invokes an operation synchronously      * @param oi  The operation to be invoked      * @param params  The params that matches the parts of the input message of the operation      * @return The return values that matche the parts of the output message of the operation      */
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
function_decl|;
comment|/**      * Invokes an operation synchronously      * @param oi  The operation to be invoked      * @param params  The params that matches the parts of the input message of the operation      * @param context  Optional (can be null) contextual information for the invocation           * @return The return values that matche the parts of the output message of the operation      */
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
function_decl|;
comment|/**      * Invokes an operation synchronously      * @param oi  The operation to be invoked      * @param params  The params that matches the parts of the input message of the operation      * @param context  Optional (can be null) contextual information for the invocation      * @param exchange The Exchange to be used for the invocation           * @return The return values that matche the parts of the output message of the operation      */
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
function_decl|;
comment|/**      * Invokes an operation asynchronously      * @param callback The callback that is called when the response is ready      * @param operationName The name of the operation to be invoked. The service namespace will be used      * when looking up the BindingOperationInfo.      * @param params  The params that matches the parts of the input message of the operation.  If the       * BindingOperationInfo supports unwrapping, it assumes the params are in the "unwrapped" form.  If       * params are in the wrapped form, use invokeWrapped      * @return The return values that matche the parts of the output message of the operation      */
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
function_decl|;
comment|/**      * Invokes an operation asynchronously      * @param callback The callback that is called when the response is ready      * @param operationName The name of the operation to be invoked      * @param params  The params that matches the parts of the input message of the operation.  If the       * BindingOperationInfo supports unwrapping, it assumes the params are in the "unwrapped" form.  If       * params are in the wrapped form, use invokeWrapped      * @return The return values that matche the parts of the output message of the operation      */
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
function_decl|;
comment|/**      * Invokes an operation asynchronously      * @param callback The callback that is called when the response is ready      * @param operationName The name of the operation to be invoked. The service namespace will be used      * when looking up the BindingOperationInfo.      * @param params  The params that matches the parts of the input message of the operation      * @return The return values that matche the parts of the output message of the operation      */
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
function_decl|;
comment|/**      * Invokes an operation asynchronously      * @param callback The callback that is called when the response is ready      * @param operationName The name of the operation to be invoked      * @param params  The params that matches the parts of the input message of the operation      * @return The return values that matche the parts of the output message of the operation      */
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
function_decl|;
comment|/**      * Invokes an operation asynchronously      * @param callback The callback that is called when the response is ready      * @param oi  The operation to be invoked      * @param params  The params that matches the parts of the input message of the operation      * @return The return values that matche the parts of the output message of the operation      */
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
function_decl|;
comment|/**      * Gets the request context used for future invocations      * @return context The context      */
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getRequestContext
parameter_list|()
function_decl|;
comment|/**      * Gets the response context from the last invocation on this thread      * @return context The context      */
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getResponseContext
parameter_list|()
function_decl|;
comment|/**      * Sets whether the request context is thread local or global to this client.  By       * default, the request context is "global" in that any values set in the context      * are seen by all threads using this client.  If set to true, the context is changed       * to be a ThreadLocal and values set are not seen by other threads.      * @param b      */
name|void
name|setThreadLocalRequestContext
parameter_list|(
name|boolean
name|b
parameter_list|)
function_decl|;
comment|/**      * Checks if the Request context is thread local or global.      * @return      */
name|boolean
name|isThreadLocalRequestContext
parameter_list|()
function_decl|;
name|Endpoint
name|getEndpoint
parameter_list|()
function_decl|;
comment|/**      * Get the Conduit that messages for this client will be sent on.      * @return Conduit      */
name|Conduit
name|getConduit
parameter_list|()
function_decl|;
comment|/**      * Get the ConduitSelector responsible for retreiving the Conduit.      *       * @return the current ConduitSelector      */
name|ConduitSelector
name|getConduitSelector
parameter_list|()
function_decl|;
comment|/**      * Set the ConduitSelector responsible for retreiving the Conduit.      *       * @param selector the ConduitSelector to use      */
name|void
name|setConduitSelector
parameter_list|(
name|ConduitSelector
name|selector
parameter_list|)
function_decl|;
comment|/**      * Indicates that the client is no longer needed and that any resources it holds      * can now be freed.      *      */
name|void
name|destroy
parameter_list|()
function_decl|;
comment|/**      * Sets the executor which is used to process Asynchronous responses.  The default      * is to use the threads provided by the transport.  (example: the JMS listener threads)       * @param executor      */
name|void
name|setExecutor
parameter_list|(
name|Executor
name|executor
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

