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
name|interceptor
operator|.
name|OutFaultChainInitiatorObserver
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

begin_class
specifier|public
class|class
name|ColocOutFaultObserver
extends|extends
name|OutFaultChainInitiatorObserver
block|{
name|SortedSet
argument_list|<
name|Phase
argument_list|>
name|list
decl_stmt|;
specifier|public
name|ColocOutFaultObserver
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|super
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|list
operator|=
operator|new
name|TreeSet
argument_list|<>
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
expr_stmt|;
name|ColocUtil
operator|.
name|setPhases
argument_list|(
name|list
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
block|}
specifier|protected
name|SortedSet
argument_list|<
name|Phase
argument_list|>
name|getPhases
parameter_list|()
block|{
return|return
name|list
return|;
block|}
block|}
end_class

end_unit

