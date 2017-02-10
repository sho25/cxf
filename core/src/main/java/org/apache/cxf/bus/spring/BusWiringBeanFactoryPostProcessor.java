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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|helpers
operator|.
name|CastUtils
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
name|config
operator|.
name|BeanFactoryPostProcessor
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
name|ConfigurableListableBeanFactory
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
name|ConstructorArgumentValues
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
name|ConstructorArgumentValues
operator|.
name|ValueHolder
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
name|RuntimeBeanReference
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
name|DefaultListableBeanFactory
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
name|RootBeanDefinition
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
name|ConfigurableApplicationContext
import|;
end_import

begin_comment
comment|/**  * BeanFactoryPostProcessor that looks for any bean definitions that have the  * {@link AbstractBeanDefinitionParser#WIRE_BUS_ATTRIBUTE} attribute set. If the attribute has the value  * {@link BusWiringType#PROPERTY} then it attaches their "bus" property to the bean called "cxf". If the  * attribute has the value {@link BusWiringType#CONSTRUCTOR} then it shifts any existing indexed constructor  * arguments one place to the right and adds a reference to "cxf" as the first constructor argument. This  * processor is intended to operate on beans defined via Spring namespace support which require a reference to  * the CXF bus.  */
end_comment

begin_class
specifier|public
class|class
name|BusWiringBeanFactoryPostProcessor
implements|implements
name|BeanFactoryPostProcessor
block|{
name|Bus
name|bus
decl_stmt|;
name|String
name|busName
decl_stmt|;
specifier|public
name|BusWiringBeanFactoryPostProcessor
parameter_list|()
block|{     }
specifier|public
name|BusWiringBeanFactoryPostProcessor
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|bus
operator|=
name|b
expr_stmt|;
block|}
specifier|public
name|BusWiringBeanFactoryPostProcessor
parameter_list|(
name|String
name|n
parameter_list|)
block|{
name|busName
operator|=
name|n
expr_stmt|;
block|}
specifier|private
specifier|static
name|Bus
name|getBusForName
parameter_list|(
name|String
name|name
parameter_list|,
name|ApplicationContext
name|context
parameter_list|,
name|boolean
name|create
parameter_list|)
block|{
if|if
condition|(
operator|!
name|context
operator|.
name|containsBean
argument_list|(
name|name
argument_list|)
operator|&&
operator|(
name|create
operator|||
name|Bus
operator|.
name|DEFAULT_BUS_ID
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|)
condition|)
block|{
name|SpringBus
name|b
init|=
operator|new
name|SpringBus
argument_list|()
decl_stmt|;
name|ConfigurableApplicationContext
name|cctx
init|=
operator|(
name|ConfigurableApplicationContext
operator|)
name|context
decl_stmt|;
name|cctx
operator|.
name|getBeanFactory
argument_list|()
operator|.
name|registerSingleton
argument_list|(
name|name
argument_list|,
name|b
argument_list|)
expr_stmt|;
name|b
operator|.
name|setApplicationContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|Bus
operator|)
name|context
operator|.
name|getBean
argument_list|(
name|name
argument_list|,
name|Bus
operator|.
name|class
argument_list|)
return|;
block|}
specifier|private
name|Object
name|getBusForName
parameter_list|(
name|String
name|name
parameter_list|,
name|ConfigurableListableBeanFactory
name|factory
parameter_list|,
name|boolean
name|create
parameter_list|,
name|String
name|cn
parameter_list|)
block|{
if|if
condition|(
operator|!
name|factory
operator|.
name|containsBeanDefinition
argument_list|(
name|name
argument_list|)
operator|&&
operator|(
name|create
operator|||
name|Bus
operator|.
name|DEFAULT_BUS_ID
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|)
condition|)
block|{
name|DefaultListableBeanFactory
name|df
init|=
operator|(
name|DefaultListableBeanFactory
operator|)
name|factory
decl_stmt|;
name|RootBeanDefinition
name|rbd
init|=
operator|new
name|RootBeanDefinition
argument_list|(
name|SpringBus
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|cn
operator|!=
literal|null
condition|)
block|{
name|rbd
operator|.
name|setAttribute
argument_list|(
literal|"busConfig"
argument_list|,
operator|new
name|RuntimeBeanReference
argument_list|(
name|cn
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|df
operator|.
name|registerBeanDefinition
argument_list|(
name|name
argument_list|,
name|rbd
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|cn
operator|!=
literal|null
condition|)
block|{
name|BeanDefinition
name|bd
init|=
name|factory
operator|.
name|getBeanDefinition
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|bd
operator|.
name|getPropertyValues
argument_list|()
operator|.
name|addPropertyValue
argument_list|(
literal|"busConfig"
argument_list|,
operator|new
name|RuntimeBeanReference
argument_list|(
name|cn
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|RuntimeBeanReference
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|void
name|postProcessBeanFactory
parameter_list|(
name|ConfigurableListableBeanFactory
name|factory
parameter_list|)
throws|throws
name|BeansException
block|{
name|Object
name|inject
init|=
name|bus
decl_stmt|;
if|if
condition|(
name|inject
operator|==
literal|null
condition|)
block|{
name|inject
operator|=
name|getBusForName
argument_list|(
name|Bus
operator|.
name|DEFAULT_BUS_ID
argument_list|,
name|factory
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
operator|!
name|factory
operator|.
name|containsBeanDefinition
argument_list|(
name|Bus
operator|.
name|DEFAULT_BUS_ID
argument_list|)
operator|&&
operator|!
name|factory
operator|.
name|containsSingleton
argument_list|(
name|Bus
operator|.
name|DEFAULT_BUS_ID
argument_list|)
condition|)
block|{
name|factory
operator|.
name|registerSingleton
argument_list|(
name|Bus
operator|.
name|DEFAULT_BUS_ID
argument_list|,
name|bus
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|String
name|beanName
range|:
name|factory
operator|.
name|getBeanDefinitionNames
argument_list|()
control|)
block|{
name|BeanDefinition
name|beanDefinition
init|=
name|factory
operator|.
name|getBeanDefinition
argument_list|(
name|beanName
argument_list|)
decl_stmt|;
name|BusWiringType
name|type
init|=
operator|(
name|BusWiringType
operator|)
name|beanDefinition
operator|.
name|getAttribute
argument_list|(
name|AbstractBeanDefinitionParser
operator|.
name|WIRE_BUS_ATTRIBUTE
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|String
name|busname
init|=
operator|(
name|String
operator|)
name|beanDefinition
operator|.
name|getAttribute
argument_list|(
name|AbstractBeanDefinitionParser
operator|.
name|WIRE_BUS_NAME
argument_list|)
decl_stmt|;
name|String
name|create
init|=
operator|(
name|String
operator|)
name|beanDefinition
operator|.
name|getAttribute
argument_list|(
name|AbstractBeanDefinitionParser
operator|.
name|WIRE_BUS_CREATE
argument_list|)
decl_stmt|;
name|Object
name|inj
init|=
name|inject
decl_stmt|;
if|if
condition|(
name|busname
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
continue|continue;
block|}
name|inj
operator|=
name|getBusForName
argument_list|(
name|busname
argument_list|,
name|factory
argument_list|,
name|create
operator|!=
literal|null
argument_list|,
name|create
argument_list|)
expr_stmt|;
block|}
name|beanDefinition
operator|.
name|removeAttribute
argument_list|(
name|AbstractBeanDefinitionParser
operator|.
name|WIRE_BUS_NAME
argument_list|)
expr_stmt|;
name|beanDefinition
operator|.
name|removeAttribute
argument_list|(
name|AbstractBeanDefinitionParser
operator|.
name|WIRE_BUS_ATTRIBUTE
argument_list|)
expr_stmt|;
name|beanDefinition
operator|.
name|removeAttribute
argument_list|(
name|AbstractBeanDefinitionParser
operator|.
name|WIRE_BUS_CREATE
argument_list|)
expr_stmt|;
if|if
condition|(
name|create
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|BusWiringType
operator|.
name|PROPERTY
operator|==
name|type
condition|)
block|{
name|beanDefinition
operator|.
name|getPropertyValues
argument_list|()
operator|.
name|addPropertyValue
argument_list|(
literal|"bus"
argument_list|,
name|inj
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|BusWiringType
operator|.
name|CONSTRUCTOR
operator|==
name|type
condition|)
block|{
name|ConstructorArgumentValues
name|constructorArgs
init|=
name|beanDefinition
operator|.
name|getConstructorArgumentValues
argument_list|()
decl_stmt|;
name|insertConstructorArg
argument_list|(
name|constructorArgs
argument_list|,
name|inj
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Insert the given value as the first constructor argument in the given set. To do this, we clear the      * argument set, then re-insert all its generic arguments, then re-insert all its indexed arguments with      * their indices incremented by 1, and finally set the first indexed argument (at index 0) to the given      * value.      *       * @param constructorArgs the argument definition to modify.      * @param valueToInsert the value to insert as the first argument.      */
specifier|private
name|void
name|insertConstructorArg
parameter_list|(
name|ConstructorArgumentValues
name|constructorArgs
parameter_list|,
name|Object
name|valueToInsert
parameter_list|)
block|{
name|List
argument_list|<
name|ValueHolder
argument_list|>
name|genericArgs
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|CastUtils
operator|.
expr|<
name|ValueHolder
operator|>
name|cast
argument_list|(
name|constructorArgs
operator|.
name|getGenericArgumentValues
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|Integer
argument_list|,
name|ValueHolder
argument_list|>
name|indexedArgs
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|ValueHolder
argument_list|>
argument_list|(
name|CastUtils
operator|.
expr|<
name|Integer
argument_list|,
name|ValueHolder
operator|>
name|cast
argument_list|(
name|constructorArgs
operator|.
name|getIndexedArgumentValues
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|constructorArgs
operator|.
name|clear
argument_list|()
expr_stmt|;
for|for
control|(
name|ValueHolder
name|genericValue
range|:
name|genericArgs
control|)
block|{
name|constructorArgs
operator|.
name|addGenericArgumentValue
argument_list|(
name|genericValue
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Integer
argument_list|,
name|ValueHolder
argument_list|>
name|entry
range|:
name|indexedArgs
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|constructorArgs
operator|.
name|addIndexedArgumentValue
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|1
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|constructorArgs
operator|.
name|addIndexedArgumentValue
argument_list|(
literal|0
argument_list|,
name|valueToInsert
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|Bus
name|addDefaultBus
parameter_list|(
name|ApplicationContext
name|ctx
parameter_list|)
block|{
if|if
condition|(
operator|!
name|ctx
operator|.
name|containsBean
argument_list|(
name|Bus
operator|.
name|DEFAULT_BUS_ID
argument_list|)
condition|)
block|{
name|Bus
name|b
init|=
name|getBusForName
argument_list|(
name|Bus
operator|.
name|DEFAULT_BUS_ID
argument_list|,
name|ctx
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|ctx
operator|instanceof
name|ConfigurableApplicationContext
condition|)
block|{
name|ConfigurableApplicationContext
name|cctx
init|=
operator|(
name|ConfigurableApplicationContext
operator|)
name|ctx
decl_stmt|;
operator|new
name|BusWiringBeanFactoryPostProcessor
argument_list|(
name|b
argument_list|)
operator|.
name|postProcessBeanFactory
argument_list|(
name|cctx
operator|.
name|getBeanFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|Bus
operator|.
name|class
operator|.
name|cast
argument_list|(
name|ctx
operator|.
name|getBean
argument_list|(
name|Bus
operator|.
name|DEFAULT_BUS_ID
argument_list|,
name|Bus
operator|.
name|class
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Bus
name|addBus
parameter_list|(
name|ApplicationContext
name|ctx
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
name|getBusForName
argument_list|(
name|name
argument_list|,
name|ctx
argument_list|,
literal|true
argument_list|)
return|;
block|}
block|}
end_class

end_unit

