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
name|policy
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|javax
operator|.
name|annotation
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|configuration
operator|.
name|ConfiguredBeanLocator
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
name|configuration
operator|.
name|spring
operator|.
name|MapProvider
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
name|extension
operator|.
name|BusExtension
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
name|extension
operator|.
name|RegistryImpl
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
name|Interceptor
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
name|neethi
operator|.
name|Assertion
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
annotation|@
name|NoJSR250Annotations
argument_list|(
name|unlessNull
operator|=
literal|"bus"
argument_list|)
specifier|public
class|class
name|PolicyInterceptorProviderRegistryImpl
extends|extends
name|RegistryImpl
argument_list|<
name|QName
argument_list|,
name|PolicyInterceptorProvider
argument_list|>
implements|implements
name|PolicyInterceptorProviderRegistry
implements|,
name|BusExtension
block|{
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|boolean
name|dynamicLoaded
decl_stmt|;
specifier|public
name|PolicyInterceptorProviderRegistryImpl
parameter_list|()
block|{
name|super
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|PolicyInterceptorProviderRegistryImpl
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|super
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|setBus
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
specifier|public
name|PolicyInterceptorProviderRegistryImpl
parameter_list|(
name|Map
argument_list|<
name|QName
argument_list|,
name|PolicyInterceptorProvider
argument_list|>
name|interceptors
parameter_list|)
block|{
name|super
argument_list|(
name|interceptors
argument_list|)
expr_stmt|;
block|}
specifier|public
name|PolicyInterceptorProviderRegistryImpl
parameter_list|(
name|MapProvider
argument_list|<
name|QName
argument_list|,
name|PolicyInterceptorProvider
argument_list|>
name|interceptors
parameter_list|)
block|{
name|super
argument_list|(
name|interceptors
operator|.
name|createMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|PolicyInterceptorProviderRegistryImpl
parameter_list|(
name|Bus
name|b
parameter_list|,
name|MapProvider
argument_list|<
name|QName
argument_list|,
name|PolicyInterceptorProvider
argument_list|>
name|interceptors
parameter_list|)
block|{
name|super
argument_list|(
name|interceptors
operator|.
name|createMap
argument_list|()
argument_list|)
expr_stmt|;
name|setBus
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Resource
specifier|public
specifier|final
name|void
name|setBus
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|bus
operator|=
name|b
expr_stmt|;
if|if
condition|(
name|b
operator|!=
literal|null
condition|)
block|{
name|b
operator|.
name|setExtension
argument_list|(
name|this
argument_list|,
name|PolicyInterceptorProviderRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|register
parameter_list|(
name|PolicyInterceptorProvider
name|provider
parameter_list|)
block|{
for|for
control|(
name|QName
name|qn
range|:
name|provider
operator|.
name|getAssertionTypes
argument_list|()
control|)
block|{
name|super
operator|.
name|register
argument_list|(
name|qn
argument_list|,
name|provider
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getRegistrationType
parameter_list|()
block|{
return|return
name|PolicyInterceptorProviderRegistry
operator|.
name|class
return|;
block|}
specifier|protected
specifier|synchronized
name|void
name|loadDynamic
parameter_list|()
block|{
if|if
condition|(
operator|!
name|dynamicLoaded
operator|&&
name|bus
operator|!=
literal|null
condition|)
block|{
name|dynamicLoaded
operator|=
literal|true
expr_stmt|;
name|ConfiguredBeanLocator
name|c
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ConfiguredBeanLocator
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|c
operator|.
name|getBeansOfType
argument_list|(
name|PolicyInterceptorProviderLoader
operator|.
name|class
argument_list|)
expr_stmt|;
for|for
control|(
name|PolicyInterceptorProvider
name|b
range|:
name|c
operator|.
name|getBeansOfType
argument_list|(
name|PolicyInterceptorProvider
operator|.
name|class
argument_list|)
control|)
block|{
name|register
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|public
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|getInterceptors
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|Assertion
argument_list|>
name|alternative
parameter_list|,
name|boolean
name|out
parameter_list|,
name|boolean
name|fault
parameter_list|)
block|{
name|loadDynamic
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|interceptors
init|=
operator|new
name|ArrayList
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Assertion
name|a
range|:
name|alternative
control|)
block|{
if|if
condition|(
name|a
operator|.
name|isOptional
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|QName
name|qn
init|=
name|a
operator|.
name|getName
argument_list|()
decl_stmt|;
name|PolicyInterceptorProvider
name|pp
init|=
name|get
argument_list|(
name|qn
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|pp
condition|)
block|{
name|interceptors
operator|.
name|addAll
argument_list|(
name|out
condition|?
operator|(
name|fault
condition|?
name|pp
operator|.
name|getOutFaultInterceptors
argument_list|()
else|:
name|pp
operator|.
name|getOutInterceptors
argument_list|()
operator|)
else|:
operator|(
name|fault
condition|?
name|pp
operator|.
name|getInFaultInterceptors
argument_list|()
else|:
name|pp
operator|.
name|getInInterceptors
argument_list|()
operator|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|interceptors
return|;
block|}
block|}
end_class

end_unit

