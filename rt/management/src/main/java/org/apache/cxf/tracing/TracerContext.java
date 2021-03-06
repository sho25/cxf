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
name|tracing
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Callable
import|;
end_import

begin_interface
specifier|public
interface|interface
name|TracerContext
block|{
comment|/**      * Picks up an currently detached span from another thread. This method is intended      * to be used in the context of JAX-RS asynchronous invocations, where request and      * response are effectively executed by different threads.      * @param traceable traceable implementation to be executed      * @return the result of the execution      * @throws Exception any exception being thrown by the traceable implementation      */
parameter_list|<
name|T
parameter_list|>
name|T
name|continueSpan
parameter_list|(
name|Traceable
argument_list|<
name|T
argument_list|>
name|traceable
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Starts a new span in the current thread.      * @param description span description      * @return span instance object      */
parameter_list|<
name|T
parameter_list|>
name|T
name|startSpan
parameter_list|(
name|String
name|description
parameter_list|)
function_decl|;
comment|/**      * Wraps the traceable into a new span, preserving the current span as a parent.      * @param description span description      * @param traceable  traceable implementation to be wrapped      * @return callable to be executed (in current thread or any other thread pool)      */
parameter_list|<
name|T
parameter_list|>
name|Callable
argument_list|<
name|T
argument_list|>
name|wrap
parameter_list|(
name|String
name|description
parameter_list|,
name|Traceable
argument_list|<
name|T
argument_list|>
name|traceable
parameter_list|)
function_decl|;
comment|/**      * Adds a key/value pair to the currently active span.      * @param key key to add      * @param value value to add      */
name|void
name|annotate
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
function_decl|;
comment|/**      * Adds a timeline to the currently active span.      * @param message timeline message      */
name|void
name|timeline
parameter_list|(
name|String
name|message
parameter_list|)
function_decl|;
comment|/**      * Returns an object of the specified type to allow access to the specific API       * of the tracing provider.      * @param clazz - the class of the object to be returned.      * @return an instance of the specified class       */
specifier|default
parameter_list|<
name|T
parameter_list|>
name|T
name|unwrap
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"This operation is not supported"
argument_list|)
throw|;
block|}
block|}
end_interface

end_unit

