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
name|bus
operator|.
name|spring
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|bus
operator|.
name|CXFBusImpl
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
name|common
operator|.
name|util
operator|.
name|StringUtils
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
name|AbstractBeanDefinitionParser
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
name|BusWiringType
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
name|AbstractFeature
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
name|AbstractBasicInterceptorProvider
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
name|springframework
operator|.
name|beans
operator|.
name|BeansException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|PropertyValue
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|config
operator|.
name|BeanDefinition
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|support
operator|.
name|AbstractBeanDefinition
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|support
operator|.
name|BeanDefinitionBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|xml
operator|.
name|ParserContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContextAware
import|;
end_import

begin_class
specifier|public
class|class
name|BusDefinitionParser
extends|extends
name|AbstractBeanDefinitionParser
block|{
specifier|private
specifier|static
name|AtomicInteger
name|counter
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
decl_stmt|;
specifier|public
name|BusDefinitionParser
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|setBeanClass
argument_list|(
name|BusConfig
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|doParse
parameter_list|(
name|Element
name|element
parameter_list|,
name|ParserContext
name|ctx
parameter_list|,
name|BeanDefinitionBuilder
name|bean
parameter_list|)
block|{
name|String
name|bus
init|=
name|element
operator|.
name|getAttribute
argument_list|(
literal|"bus"
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|bus
argument_list|)
condition|)
block|{
name|bus
operator|=
name|element
operator|.
name|getAttribute
argument_list|(
literal|"name"
argument_list|)
expr_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|bus
argument_list|)
condition|)
block|{
name|element
operator|.
name|setAttribute
argument_list|(
literal|"bus"
argument_list|,
name|bus
argument_list|)
expr_stmt|;
block|}
block|}
name|element
operator|.
name|removeAttribute
argument_list|(
literal|"name"
argument_list|)
expr_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|bus
argument_list|)
condition|)
block|{
name|bus
operator|=
literal|"cxf"
expr_stmt|;
block|}
name|String
name|id
init|=
name|element
operator|.
name|getAttribute
argument_list|(
literal|"id"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|bean
operator|.
name|addPropertyValue
argument_list|(
literal|"id"
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|doParse
argument_list|(
name|element
argument_list|,
name|ctx
argument_list|,
name|bean
argument_list|)
expr_stmt|;
if|if
condition|(
name|ctx
operator|.
name|getRegistry
argument_list|()
operator|.
name|containsBeanDefinition
argument_list|(
name|bus
argument_list|)
condition|)
block|{
name|BeanDefinition
name|def
init|=
name|ctx
operator|.
name|getRegistry
argument_list|()
operator|.
name|getBeanDefinition
argument_list|(
name|bus
argument_list|)
decl_stmt|;
name|copyProps
argument_list|(
name|bean
argument_list|,
name|def
argument_list|)
expr_stmt|;
name|bean
operator|.
name|addConstructorArgValue
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
literal|"cxf"
operator|.
name|equals
argument_list|(
name|bus
argument_list|)
condition|)
block|{
name|bean
operator|.
name|getRawBeanDefinition
argument_list|()
operator|.
name|setBeanClass
argument_list|(
name|SpringBus
operator|.
name|class
argument_list|)
expr_stmt|;
name|bean
operator|.
name|getRawBeanDefinition
argument_list|()
operator|.
name|getPropertyValues
argument_list|()
operator|.
name|removePropertyValue
argument_list|(
literal|"bus"
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setDestroyMethodName
argument_list|(
literal|"shutdown"
argument_list|)
expr_stmt|;
name|element
operator|.
name|setUserData
argument_list|(
literal|"ID"
argument_list|,
name|bus
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|addBusWiringAttribute
argument_list|(
name|bean
argument_list|,
name|BusWiringType
operator|.
name|PROPERTY
argument_list|,
name|bus
argument_list|,
name|ctx
argument_list|)
expr_stmt|;
name|bean
operator|.
name|getRawBeanDefinition
argument_list|()
operator|.
name|setAttribute
argument_list|(
name|WIRE_BUS_CREATE
argument_list|,
name|resolveId
argument_list|(
name|element
argument_list|,
literal|null
argument_list|,
name|ctx
argument_list|)
argument_list|)
expr_stmt|;
name|bean
operator|.
name|addConstructorArgValue
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|boolean
name|processBusAttribute
parameter_list|(
name|Element
name|element
parameter_list|,
name|ParserContext
name|ctx
parameter_list|,
name|BeanDefinitionBuilder
name|bean
parameter_list|,
name|String
name|val
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
specifier|private
name|void
name|copyProps
parameter_list|(
name|BeanDefinitionBuilder
name|src
parameter_list|,
name|BeanDefinition
name|def
parameter_list|)
block|{
for|for
control|(
name|PropertyValue
name|v
range|:
name|src
operator|.
name|getBeanDefinition
argument_list|()
operator|.
name|getPropertyValues
argument_list|()
operator|.
name|getPropertyValues
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
literal|"bus"
operator|.
name|equals
argument_list|(
name|v
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|def
operator|.
name|getPropertyValues
argument_list|()
operator|.
name|addPropertyValue
argument_list|(
name|v
operator|.
name|getName
argument_list|()
argument_list|,
name|v
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|src
operator|.
name|getBeanDefinition
argument_list|()
operator|.
name|getPropertyValues
argument_list|()
operator|.
name|removePropertyValue
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|mapElement
parameter_list|(
name|ParserContext
name|ctx
parameter_list|,
name|BeanDefinitionBuilder
name|bean
parameter_list|,
name|Element
name|e
parameter_list|,
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
literal|"inInterceptors"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"inFaultInterceptors"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"outInterceptors"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"outFaultInterceptors"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"features"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|?
argument_list|>
name|list
init|=
name|ctx
operator|.
name|getDelegate
argument_list|()
operator|.
name|parseListElement
argument_list|(
name|e
argument_list|,
name|bean
operator|.
name|getBeanDefinition
argument_list|()
argument_list|)
decl_stmt|;
name|bean
operator|.
name|addPropertyValue
argument_list|(
name|name
argument_list|,
name|list
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"properties"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|map
init|=
name|ctx
operator|.
name|getDelegate
argument_list|()
operator|.
name|parseMapElement
argument_list|(
name|e
argument_list|,
name|bean
operator|.
name|getBeanDefinition
argument_list|()
argument_list|)
decl_stmt|;
name|bean
operator|.
name|addPropertyValue
argument_list|(
literal|"properties"
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|String
name|resolveId
parameter_list|(
name|Element
name|element
parameter_list|,
name|AbstractBeanDefinition
name|definition
parameter_list|,
name|ParserContext
name|ctx
parameter_list|)
block|{
name|String
name|bus
init|=
operator|(
name|String
operator|)
name|element
operator|.
name|getUserData
argument_list|(
literal|"ID"
argument_list|)
decl_stmt|;
if|if
condition|(
name|bus
operator|==
literal|null
condition|)
block|{
name|bus
operator|=
name|element
operator|.
name|getAttribute
argument_list|(
literal|"bus"
argument_list|)
expr_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|bus
argument_list|)
condition|)
block|{
name|bus
operator|=
name|element
operator|.
name|getAttribute
argument_list|(
literal|"name"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|bus
argument_list|)
condition|)
block|{
name|bus
operator|=
name|Bus
operator|.
name|DEFAULT_BUS_ID
operator|+
literal|".config"
operator|+
name|counter
operator|.
name|getAndIncrement
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|bus
operator|=
name|bus
operator|+
literal|".config"
expr_stmt|;
block|}
name|element
operator|.
name|setUserData
argument_list|(
literal|"ID"
argument_list|,
name|bus
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
return|return
name|bus
return|;
block|}
annotation|@
name|NoJSR250Annotations
specifier|public
specifier|static
class|class
name|BusConfig
extends|extends
name|AbstractBasicInterceptorProvider
implements|implements
name|ApplicationContextAware
block|{
name|CXFBusImpl
name|bus
decl_stmt|;
name|String
name|busName
decl_stmt|;
name|String
name|id
decl_stmt|;
name|Collection
argument_list|<
name|AbstractFeature
argument_list|>
name|features
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
decl_stmt|;
specifier|public
name|BusConfig
parameter_list|(
name|String
name|busName
parameter_list|)
block|{
name|this
operator|.
name|busName
operator|=
name|busName
expr_stmt|;
block|}
specifier|public
name|void
name|setBus
parameter_list|(
name|Bus
name|bb
parameter_list|)
block|{
if|if
condition|(
name|bus
operator|==
name|bb
condition|)
block|{
return|return;
block|}
name|CXFBusImpl
name|b
init|=
operator|(
name|CXFBusImpl
operator|)
name|bb
decl_stmt|;
if|if
condition|(
name|properties
operator|!=
literal|null
condition|)
block|{
name|b
operator|.
name|setProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|properties
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|getInInterceptors
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|b
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|addAll
argument_list|(
name|getInInterceptors
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|getOutInterceptors
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|b
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|addAll
argument_list|(
name|getOutInterceptors
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|getInFaultInterceptors
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|b
operator|.
name|getInFaultInterceptors
argument_list|()
operator|.
name|addAll
argument_list|(
name|getInFaultInterceptors
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|getOutFaultInterceptors
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|b
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|addAll
argument_list|(
name|getOutFaultInterceptors
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|b
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|features
operator|!=
literal|null
condition|)
block|{
name|b
operator|.
name|setFeatures
argument_list|(
name|features
argument_list|)
expr_stmt|;
name|features
operator|=
literal|null
expr_stmt|;
block|}
name|bus
operator|=
name|b
expr_stmt|;
block|}
specifier|public
name|void
name|setApplicationContext
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|)
throws|throws
name|BeansException
block|{
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
return|return;
block|}
comment|/*             bus = (CXFBusImpl)BusWiringBeanFactoryPostProcessor                     .addBus(applicationContext, busName);                     */
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
name|getOutFaultInterceptors
parameter_list|()
block|{
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
return|return
name|bus
operator|.
name|getOutFaultInterceptors
argument_list|()
return|;
block|}
return|return
name|super
operator|.
name|getOutFaultInterceptors
argument_list|()
return|;
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
name|getInFaultInterceptors
parameter_list|()
block|{
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
return|return
name|bus
operator|.
name|getInFaultInterceptors
argument_list|()
return|;
block|}
return|return
name|super
operator|.
name|getInFaultInterceptors
argument_list|()
return|;
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
name|getInInterceptors
parameter_list|()
block|{
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
return|return
name|bus
operator|.
name|getInInterceptors
argument_list|()
return|;
block|}
return|return
name|super
operator|.
name|getInInterceptors
argument_list|()
return|;
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
name|getOutInterceptors
parameter_list|()
block|{
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
return|return
name|bus
operator|.
name|getOutInterceptors
argument_list|()
return|;
block|}
return|return
name|super
operator|.
name|getOutInterceptors
argument_list|()
return|;
block|}
specifier|public
name|void
name|setInInterceptors
parameter_list|(
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
parameter_list|)
block|{
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
name|bus
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|addAll
argument_list|(
name|interceptors
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|setInInterceptors
argument_list|(
name|interceptors
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setInFaultInterceptors
parameter_list|(
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
parameter_list|)
block|{
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
name|bus
operator|.
name|getInFaultInterceptors
argument_list|()
operator|.
name|addAll
argument_list|(
name|interceptors
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|setInFaultInterceptors
argument_list|(
name|interceptors
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setOutInterceptors
parameter_list|(
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
parameter_list|)
block|{
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
name|bus
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|addAll
argument_list|(
name|interceptors
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|setOutInterceptors
argument_list|(
name|interceptors
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setOutFaultInterceptors
parameter_list|(
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
parameter_list|)
block|{
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
name|bus
operator|.
name|setOutFaultInterceptors
argument_list|(
name|interceptors
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|setOutFaultInterceptors
argument_list|(
name|interceptors
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Collection
argument_list|<
name|AbstractFeature
argument_list|>
name|getFeatures
parameter_list|()
block|{
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
return|return
name|bus
operator|.
name|getFeatures
argument_list|()
return|;
block|}
return|return
name|features
return|;
block|}
specifier|public
name|void
name|setFeatures
parameter_list|(
name|Collection
argument_list|<
name|AbstractFeature
argument_list|>
name|features
parameter_list|)
block|{
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
name|bus
operator|.
name|setFeatures
argument_list|(
name|features
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|features
operator|=
name|features
expr_stmt|;
block|}
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getProperties
parameter_list|()
block|{
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
return|return
name|bus
operator|.
name|getProperties
argument_list|()
return|;
block|}
return|return
name|properties
return|;
block|}
specifier|public
name|void
name|setProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|s
parameter_list|)
block|{
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
name|bus
operator|.
name|setProperties
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|properties
operator|=
name|s
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|id
operator|=
name|s
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

