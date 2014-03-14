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
name|io
operator|.
name|Closeable
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
name|feature
operator|.
name|Feature
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

begin_comment
comment|/**  * Represents an endpoint that receives messages.   *  */
end_comment

begin_interface
specifier|public
interface|interface
name|Endpoint
extends|extends
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
extends|,
name|InterceptorProvider
block|{
name|EndpointInfo
name|getEndpointInfo
parameter_list|()
function_decl|;
name|Binding
name|getBinding
parameter_list|()
function_decl|;
name|Service
name|getService
parameter_list|()
function_decl|;
name|void
name|setExecutor
parameter_list|(
name|Executor
name|executor
parameter_list|)
function_decl|;
name|Executor
name|getExecutor
parameter_list|()
function_decl|;
name|MessageObserver
name|getInFaultObserver
parameter_list|()
function_decl|;
name|MessageObserver
name|getOutFaultObserver
parameter_list|()
function_decl|;
name|void
name|setInFaultObserver
parameter_list|(
name|MessageObserver
name|observer
parameter_list|)
function_decl|;
name|void
name|setOutFaultObserver
parameter_list|(
name|MessageObserver
name|observer
parameter_list|)
function_decl|;
name|List
argument_list|<
name|Feature
argument_list|>
name|getActiveFeatures
parameter_list|()
function_decl|;
comment|/**      * Add a hook that will be called when this end point being terminated.      * This will be called prior to the Server/ClientLifecycleListener.*Destroyed()      * method is called.  This provides an opportunity to cleanup any resources      * that are specific to this Endpoint.       * @param c      */
name|void
name|addCleanupHook
parameter_list|(
name|Closeable
name|c
parameter_list|)
function_decl|;
name|List
argument_list|<
name|Closeable
argument_list|>
name|getCleanupHooks
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

