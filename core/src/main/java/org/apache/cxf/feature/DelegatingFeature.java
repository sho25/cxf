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
comment|/**  * Enable to convert a {@link AbstractPortableFeature} to a {@link AbstractFeature}.  *  * @param<T> the "portable" feature.  */
end_comment

begin_class
specifier|public
class|class
name|DelegatingFeature
parameter_list|<
name|T
extends|extends
name|AbstractPortableFeature
parameter_list|>
extends|extends
name|AbstractFeature
block|{
specifier|protected
name|T
name|delegate
decl_stmt|;
specifier|protected
name|DelegatingFeature
parameter_list|(
specifier|final
name|T
name|d
parameter_list|)
block|{
name|delegate
operator|=
name|d
operator|==
literal|null
condition|?
name|getDelegate
argument_list|()
else|:
name|d
expr_stmt|;
block|}
specifier|protected
name|T
name|getDelegate
parameter_list|()
block|{
comment|// useful for inheritance
return|return
name|delegate
return|;
block|}
specifier|public
name|void
name|setDelegate
parameter_list|(
name|T
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|initialize
parameter_list|(
specifier|final
name|Server
name|server
parameter_list|,
specifier|final
name|Bus
name|bus
parameter_list|)
block|{
name|delegate
operator|.
name|initialize
argument_list|(
name|server
argument_list|,
name|bus
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|initialize
parameter_list|(
specifier|final
name|Client
name|client
parameter_list|,
specifier|final
name|Bus
name|bus
parameter_list|)
block|{
name|delegate
operator|.
name|initialize
argument_list|(
name|client
argument_list|,
name|bus
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|initialize
parameter_list|(
specifier|final
name|InterceptorProvider
name|interceptorProvider
parameter_list|,
specifier|final
name|Bus
name|bus
parameter_list|)
block|{
name|delegate
operator|.
name|initialize
argument_list|(
name|interceptorProvider
argument_list|,
name|bus
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|initialize
parameter_list|(
specifier|final
name|Bus
name|bus
parameter_list|)
block|{
name|delegate
operator|.
name|initialize
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|initializeProvider
parameter_list|(
specifier|final
name|InterceptorProvider
name|interceptorProvider
parameter_list|,
specifier|final
name|Bus
name|bus
parameter_list|)
block|{
name|delegate
operator|.
name|doInitializeProvider
argument_list|(
name|interceptorProvider
argument_list|,
name|bus
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

