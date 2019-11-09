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
name|validation
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
name|annotations
operator|.
name|Provider
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
name|annotations
operator|.
name|Provider
operator|.
name|Scope
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
name|annotations
operator|.
name|Provider
operator|.
name|Type
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
name|AbstractPortableFeature
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
name|DelegatingFeature
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

begin_class
annotation|@
name|Provider
argument_list|(
name|value
operator|=
name|Type
operator|.
name|Feature
argument_list|,
name|scope
operator|=
name|Scope
operator|.
name|Server
argument_list|)
specifier|public
class|class
name|BeanValidationFeature
extends|extends
name|DelegatingFeature
argument_list|<
name|BeanValidationFeature
operator|.
name|Portable
argument_list|>
block|{
specifier|public
name|BeanValidationFeature
parameter_list|()
block|{
name|super
argument_list|(
operator|new
name|Portable
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setProvider
parameter_list|(
name|BeanValidationProvider
name|provider
parameter_list|)
block|{
name|delegate
operator|.
name|setProvider
argument_list|(
name|provider
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
class|class
name|Portable
implements|implements
name|AbstractPortableFeature
block|{
specifier|private
name|BeanValidationProvider
name|validationProvider
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|doInitializeProvider
parameter_list|(
name|InterceptorProvider
name|interceptorProvider
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|BeanValidationInInterceptor
name|in
init|=
operator|new
name|BeanValidationInInterceptor
argument_list|()
decl_stmt|;
name|BeanValidationOutInterceptor
name|out
init|=
operator|new
name|BeanValidationOutInterceptor
argument_list|()
decl_stmt|;
if|if
condition|(
name|validationProvider
operator|!=
literal|null
condition|)
block|{
name|in
operator|.
name|setProvider
argument_list|(
name|validationProvider
argument_list|)
expr_stmt|;
name|out
operator|.
name|setProvider
argument_list|(
name|validationProvider
argument_list|)
expr_stmt|;
block|}
name|interceptorProvider
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|interceptorProvider
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setProvider
parameter_list|(
name|BeanValidationProvider
name|provider
parameter_list|)
block|{
name|this
operator|.
name|validationProvider
operator|=
name|provider
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

