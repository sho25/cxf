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
name|feature
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
name|endpoint
operator|.
name|Client
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
name|Server
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

begin_comment
comment|/**  * A Feature is something that is able to customize a Server, Client, or Bus, typically  * adding capabilities. For instance, there may be a LoggingFeature which configures  * one of the above to log each of their messages.  *<p>  * By default the initialize methods all delegate to initializeProvider(InterceptorProvider).   * If you're simply adding interceptors to a Server, Client, or Bus, this allows you to add  * them easily.  */
end_comment

begin_class
specifier|public
class|class
name|WrappedFeature
extends|extends
name|AbstractFeature
implements|implements
name|Feature
block|{
specifier|final
name|Feature
name|wrapped
decl_stmt|;
specifier|public
name|WrappedFeature
parameter_list|(
name|Feature
name|f
parameter_list|)
block|{
name|wrapped
operator|=
name|f
expr_stmt|;
block|}
specifier|public
name|void
name|initialize
parameter_list|(
name|Server
name|server
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|wrapped
operator|.
name|initialize
argument_list|(
name|server
argument_list|,
name|bus
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|initialize
parameter_list|(
name|Client
name|client
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|wrapped
operator|.
name|initialize
argument_list|(
name|client
argument_list|,
name|bus
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|initialize
parameter_list|(
name|InterceptorProvider
name|interceptorProvider
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|wrapped
operator|.
name|initialize
argument_list|(
name|interceptorProvider
argument_list|,
name|bus
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|initialize
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|wrapped
operator|.
name|initialize
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

