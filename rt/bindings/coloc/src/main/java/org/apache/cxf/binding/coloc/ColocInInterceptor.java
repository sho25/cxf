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
name|binding
operator|.
name|coloc
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
import|;
end_import

begin_comment
comment|//import java.util.ResourceBundle;
end_comment

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

begin_comment
comment|//import org.apache.cxf.common.i18n.BundleUtils;
end_comment

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
name|interceptor
operator|.
name|InterceptorChain
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
name|ServiceInvokerInterceptor
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
name|phase
operator|.
name|PhaseManager
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
name|service
operator|.
name|model
operator|.
name|MessageInfo
import|;
end_import

begin_class
specifier|public
class|class
name|ColocInInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
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
name|ColocInInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|ColocInInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|INVOKE
argument_list|)
expr_stmt|;
name|addAfter
argument_list|(
name|ServiceInvokerInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|Exchange
name|ex
init|=
name|msg
operator|.
name|getExchange
argument_list|()
decl_stmt|;
if|if
condition|(
name|ex
operator|.
name|isOneWay
argument_list|()
condition|)
block|{
return|return;
block|}
name|Bus
name|bus
init|=
name|ex
operator|.
name|getBus
argument_list|()
decl_stmt|;
name|SortedSet
argument_list|<
name|Phase
argument_list|>
name|phases
init|=
operator|new
name|TreeSet
argument_list|<
name|Phase
argument_list|>
argument_list|(
name|bus
operator|.
name|getExtension
argument_list|(
name|PhaseManager
operator|.
name|class
argument_list|)
operator|.
name|getOutPhases
argument_list|()
argument_list|)
decl_stmt|;
comment|//TODO Set Coloc FaultObserver chain
name|ColocUtil
operator|.
name|setPhases
argument_list|(
name|phases
argument_list|,
name|Phase
operator|.
name|SETUP
argument_list|,
name|Phase
operator|.
name|USER_LOGICAL
argument_list|)
expr_stmt|;
name|InterceptorChain
name|chain
init|=
name|ColocUtil
operator|.
name|getOutInterceptorChain
argument_list|(
name|ex
argument_list|,
name|phases
argument_list|)
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINER
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|finer
argument_list|(
literal|"Processing Message at collocated endpoint.  Response message: "
operator|+
name|msg
argument_list|)
expr_stmt|;
block|}
comment|//Initiate OutBound Processing
name|BindingOperationInfo
name|boi
init|=
name|ex
operator|.
name|getBindingOperationInfo
argument_list|()
decl_stmt|;
name|Message
name|outBound
init|=
name|ex
operator|.
name|getOutMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|boi
operator|!=
literal|null
condition|)
block|{
name|outBound
operator|.
name|put
argument_list|(
name|MessageInfo
operator|.
name|class
argument_list|,
name|boi
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|getOutput
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|outBound
operator|.
name|put
argument_list|(
name|Message
operator|.
name|INBOUND_MESSAGE
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|outBound
operator|.
name|setInterceptorChain
argument_list|(
name|chain
argument_list|)
expr_stmt|;
name|chain
operator|.
name|doIntercept
argument_list|(
name|outBound
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

