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
operator|.
name|brave
package|;
end_package

begin_import
import|import
name|brave
operator|.
name|Tracing
import|;
end_import

begin_import
import|import
name|brave
operator|.
name|http
operator|.
name|HttpTracing
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
name|NoJSR250Annotations
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
name|Client
argument_list|)
specifier|public
class|class
name|BraveClientFeature
extends|extends
name|DelegatingFeature
argument_list|<
name|BraveClientFeature
operator|.
name|Portable
argument_list|>
block|{
specifier|public
name|BraveClientFeature
parameter_list|(
specifier|final
name|Tracing
name|tracing
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|Portable
argument_list|(
name|tracing
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|BraveClientFeature
parameter_list|(
name|HttpTracing
name|brave
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|Portable
argument_list|(
name|brave
argument_list|)
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
name|BraveClientStartInterceptor
name|out
decl_stmt|;
specifier|private
name|BraveClientStopInterceptor
name|in
decl_stmt|;
specifier|public
name|Portable
parameter_list|(
specifier|final
name|Tracing
name|tracing
parameter_list|)
block|{
name|this
argument_list|(
name|HttpTracing
operator|.
name|newBuilder
argument_list|(
name|tracing
argument_list|)
operator|.
name|clientParser
argument_list|(
operator|new
name|HttpClientSpanParser
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Portable
parameter_list|(
name|HttpTracing
name|brave
parameter_list|)
block|{
name|out
operator|=
operator|new
name|BraveClientStartInterceptor
argument_list|(
name|brave
argument_list|)
expr_stmt|;
name|in
operator|=
operator|new
name|BraveClientStopInterceptor
argument_list|(
name|brave
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|doInitializeProvider
parameter_list|(
name|InterceptorProvider
name|provider
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|provider
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|provider
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
block|}
block|}
end_class

end_unit

