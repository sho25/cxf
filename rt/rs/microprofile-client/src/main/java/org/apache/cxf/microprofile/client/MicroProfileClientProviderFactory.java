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
name|microprofile
operator|.
name|client
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Configuration
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
name|BusFactory
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
name|util
operator|.
name|ClassHelper
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
name|jaxrs
operator|.
name|model
operator|.
name|ProviderInfo
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
name|jaxrs
operator|.
name|provider
operator|.
name|ProviderFactory
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
name|eclipse
operator|.
name|microprofile
operator|.
name|rest
operator|.
name|client
operator|.
name|ext
operator|.
name|ResponseExceptionMapper
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|MicroProfileClientProviderFactory
extends|extends
name|ProviderFactory
block|{
specifier|static
specifier|final
name|String
name|CLIENT_FACTORY_NAME
init|=
name|MicroProfileClientProviderFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|ProviderInfo
argument_list|<
name|ResponseExceptionMapper
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
name|responseExceptionMappers
init|=
operator|new
name|ArrayList
argument_list|<
name|ProviderInfo
argument_list|<
name|ResponseExceptionMapper
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|ContractComparator
name|comparator
decl_stmt|;
specifier|private
name|MicroProfileClientProviderFactory
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|ContractComparator
name|comparator
parameter_list|)
block|{
name|super
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|this
operator|.
name|comparator
operator|=
name|comparator
expr_stmt|;
block|}
specifier|public
specifier|static
name|MicroProfileClientProviderFactory
name|createInstance
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|ContractComparator
name|comparator
parameter_list|)
block|{
if|if
condition|(
name|bus
operator|==
literal|null
condition|)
block|{
name|bus
operator|=
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|()
expr_stmt|;
block|}
name|MicroProfileClientProviderFactory
name|factory
init|=
operator|new
name|MicroProfileClientProviderFactory
argument_list|(
name|bus
argument_list|,
name|comparator
argument_list|)
decl_stmt|;
name|ProviderFactory
operator|.
name|initFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setBusProviders
argument_list|()
expr_stmt|;
return|return
name|factory
return|;
block|}
specifier|public
specifier|static
name|MicroProfileClientProviderFactory
name|getInstance
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|Endpoint
name|e
init|=
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
return|return
name|getInstance
argument_list|(
name|e
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|MicroProfileClientProviderFactory
name|getInstance
parameter_list|(
name|Endpoint
name|e
parameter_list|)
block|{
return|return
operator|(
name|MicroProfileClientProviderFactory
operator|)
name|e
operator|.
name|get
argument_list|(
name|CLIENT_FACTORY_NAME
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|setProviders
parameter_list|(
name|boolean
name|custom
parameter_list|,
name|boolean
name|busGlobal
parameter_list|,
name|Object
modifier|...
name|providers
parameter_list|)
block|{
name|List
argument_list|<
name|ProviderInfo
argument_list|<
name|?
argument_list|>
argument_list|>
name|theProviders
init|=
name|prepareProviders
argument_list|(
name|custom
argument_list|,
name|busGlobal
argument_list|,
name|providers
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|super
operator|.
name|setCommonProviders
argument_list|(
name|theProviders
argument_list|)
expr_stmt|;
for|for
control|(
name|ProviderInfo
argument_list|<
name|?
argument_list|>
name|provider
range|:
name|theProviders
control|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|providerCls
init|=
name|ClassHelper
operator|.
name|getRealClass
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|provider
operator|.
name|getProvider
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ResponseExceptionMapper
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|providerCls
argument_list|)
condition|)
block|{
name|addProviderToList
argument_list|(
name|responseExceptionMappers
argument_list|,
name|provider
argument_list|)
expr_stmt|;
block|}
block|}
name|responseExceptionMappers
operator|.
name|sort
argument_list|(
name|comparator
argument_list|)
expr_stmt|;
name|injectContextProxies
argument_list|(
name|responseExceptionMappers
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|List
argument_list|<
name|ResponseExceptionMapper
argument_list|<
name|?
argument_list|>
argument_list|>
name|createResponseExceptionMapper
parameter_list|(
name|Message
name|m
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|paramType
parameter_list|)
block|{
name|List
argument_list|<
name|ResponseExceptionMapper
argument_list|<
name|?
argument_list|>
argument_list|>
name|candidates
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|ProviderInfo
argument_list|<
name|ResponseExceptionMapper
argument_list|<
name|?
argument_list|>
argument_list|>
name|em
range|:
name|responseExceptionMappers
control|)
block|{
if|if
condition|(
name|handleMapper
argument_list|(
name|em
argument_list|,
name|paramType
argument_list|,
name|m
argument_list|,
name|ResponseExceptionMapper
operator|.
name|class
argument_list|,
literal|true
argument_list|)
condition|)
block|{
name|candidates
operator|.
name|add
argument_list|(
name|em
operator|.
name|getProvider
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|candidates
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
name|candidates
operator|.
name|sort
argument_list|(
operator|new
name|ResponseExceptionMapperComparator
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|candidates
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|clearProviders
parameter_list|()
block|{
name|super
operator|.
name|clearProviders
argument_list|()
expr_stmt|;
name|responseExceptionMappers
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Configuration
name|getConfiguration
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
return|return
operator|(
name|Configuration
operator|)
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getOutMessage
argument_list|()
operator|.
name|getContextualProperty
argument_list|(
name|Configuration
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
specifier|private
class|class
name|ResponseExceptionMapperComparator
implements|implements
name|Comparator
argument_list|<
name|ResponseExceptionMapper
argument_list|<
name|?
argument_list|>
argument_list|>
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|ResponseExceptionMapper
argument_list|<
name|?
argument_list|>
name|oLeft
parameter_list|,
name|ResponseExceptionMapper
argument_list|<
name|?
argument_list|>
name|oRight
parameter_list|)
block|{
return|return
name|oLeft
operator|.
name|getPriority
argument_list|()
operator|-
name|oRight
operator|.
name|getPriority
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

