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
operator|.
name|policy
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
name|ws
operator|.
name|policy
operator|.
name|AssertionBuilderLoader
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
name|AssertionBuilderRegistry
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
name|PolicyInterceptorProviderLoader
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
name|PolicyInterceptorProviderRegistry
import|;
end_import

begin_comment
comment|/**  * Policy loader for WS-RMP 1.2. This provides the hooks for Neethi to handle the WS-RMP 1.2 policy  * assertions.  */
end_comment

begin_class
annotation|@
name|NoJSR250Annotations
specifier|public
specifier|final
class|class
name|WSRMP12PolicyLoader
implements|implements
name|PolicyInterceptorProviderLoader
implements|,
name|AssertionBuilderLoader
block|{
name|Bus
name|bus
decl_stmt|;
specifier|public
name|WSRMP12PolicyLoader
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|bus
operator|=
name|b
expr_stmt|;
name|registerBuilders
argument_list|()
expr_stmt|;
try|try
block|{
name|registerProviders
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// We'll ignore this as the policy framework will then not find the providers and error out at
comment|// that point. If nothing uses WS-RMP 1.2 no warnings/errors will display
block|}
block|}
specifier|public
name|void
name|registerBuilders
parameter_list|()
block|{
name|AssertionBuilderRegistry
name|reg
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|AssertionBuilderRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|reg
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|reg
operator|.
name|registerBuilder
argument_list|(
operator|new
name|RM12AssertionBuilder
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|registerProviders
parameter_list|()
block|{
comment|//interceptor provider for the policy
name|PolicyInterceptorProviderRegistry
name|reg
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|PolicyInterceptorProviderRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|reg
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|reg
operator|.
name|register
argument_list|(
operator|new
name|RMPolicyInterceptorProvider
argument_list|(
name|bus
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

