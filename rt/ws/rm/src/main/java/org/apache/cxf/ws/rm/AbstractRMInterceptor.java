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
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
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
name|Fault
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
name|phase
operator|.
name|AbstractPhaseInterceptor
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
name|Phase
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
name|policy
operator|.
name|AssertionInfo
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
name|policy
operator|.
name|AssertionInfoMap
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
name|policy
operator|.
name|RMPolicyUtilities
import|;
end_import

begin_comment
comment|/**  * Interceptor responsible for implementing exchange of RM protocol messages,  * aggregating RM metadata in the application message and processing of   * RM metadata contained in incoming application messages.  * The same interceptor can be used on multiple endpoints.  *  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractRMInterceptor
parameter_list|<
name|T
extends|extends
name|Message
parameter_list|>
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|T
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|AbstractRMInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|RMManager
name|manager
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|protected
name|AbstractRMInterceptor
parameter_list|(
name|String
name|phase
parameter_list|)
block|{
name|super
argument_list|(
name|phase
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|AbstractRMInterceptor
parameter_list|()
block|{
name|this
argument_list|(
name|Phase
operator|.
name|PRE_LOGICAL
argument_list|)
expr_stmt|;
block|}
specifier|public
name|RMManager
name|getManager
parameter_list|()
block|{
if|if
condition|(
literal|null
operator|==
name|manager
condition|)
block|{
return|return
name|bus
operator|.
name|getExtension
argument_list|(
name|RMManager
operator|.
name|class
argument_list|)
return|;
block|}
return|return
name|manager
return|;
block|}
specifier|public
name|void
name|setManager
parameter_list|(
name|RMManager
name|m
parameter_list|)
block|{
name|manager
operator|=
name|m
expr_stmt|;
block|}
specifier|public
name|Bus
name|getBus
parameter_list|()
block|{
return|return
name|bus
return|;
block|}
specifier|public
name|void
name|setBus
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|this
operator|.
name|bus
operator|=
name|bus
expr_stmt|;
block|}
comment|// Interceptor interface
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|msg
parameter_list|)
throws|throws
name|Fault
block|{
try|try
block|{
name|handle
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SequenceFault
name|sf
parameter_list|)
block|{
comment|// log the fault as it may not be reported back to the client
name|Endpoint
name|e
init|=
name|msg
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|Binding
name|b
init|=
literal|null
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|e
condition|)
block|{
name|b
operator|=
name|e
operator|.
name|getBinding
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
literal|null
operator|!=
name|b
condition|)
block|{
name|RMManager
name|m
init|=
name|getManager
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Manager: "
operator|+
name|m
argument_list|)
expr_stmt|;
name|BindingFaultFactory
name|bff
init|=
name|m
operator|.
name|getBindingFaultFactory
argument_list|(
name|b
argument_list|)
decl_stmt|;
name|Fault
name|f
init|=
name|bff
operator|.
name|createFault
argument_list|(
name|sf
argument_list|,
name|msg
argument_list|)
decl_stmt|;
comment|// log with warning instead sever, as this may happen for some delayed messages
name|LogUtils
operator|.
name|log
argument_list|(
name|LOG
argument_list|,
name|Level
operator|.
name|WARNING
argument_list|,
literal|"SEQ_FAULT_MSG"
argument_list|,
name|bff
operator|.
name|toString
argument_list|(
name|f
argument_list|)
argument_list|)
expr_stmt|;
throw|throw
name|f
throw|;
block|}
throw|throw
operator|new
name|Fault
argument_list|(
name|sf
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|RMException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
comment|/**      * Asserts all RMAssertion assertions for the current message, regardless their attributes      * (if there is more than one we have ensured that they are all supported by considering      * e.g. the minimum acknowledgment interval).      * @param message the current message      */
name|void
name|assertReliability
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|AssertionInfoMap
name|aim
init|=
name|message
operator|.
name|get
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
name|RMPolicyUtilities
operator|.
name|collectRMAssertions
argument_list|(
name|aim
argument_list|)
decl_stmt|;
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
specifier|abstract
name|void
name|handle
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|SequenceFault
throws|,
name|RMException
function_decl|;
block|}
end_class

end_unit

