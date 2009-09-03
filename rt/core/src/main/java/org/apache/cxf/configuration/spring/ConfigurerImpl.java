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
name|configuration
operator|.
name|spring
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

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
name|Iterator
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
name|Set
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
name|CopyOnWriteArraySet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|logging
operator|.
name|LogUtils
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
name|Configurable
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
name|Configurer
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
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|NoSuchBeanDefinitionException
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
name|AutowireCapableBeanFactory
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
name|BeanDefinitionRegistry
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
name|wiring
operator|.
name|BeanConfigurerSupport
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
name|wiring
operator|.
name|BeanWiringInfo
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
name|wiring
operator|.
name|BeanWiringInfoResolver
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

begin_class
annotation|@
name|NoJSR250Annotations
specifier|public
class|class
name|ConfigurerImpl
extends|extends
name|BeanConfigurerSupport
implements|implements
name|Configurer
implements|,
name|ApplicationContextAware
implements|,
name|BusExtension
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|ConfigurerImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|ApplicationContext
argument_list|>
name|appContexts
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|MatcherHolder
argument_list|>
argument_list|>
name|wildCardBeanDefinitions
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|MatcherHolder
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|static
class|class
name|MatcherHolder
block|{
name|Matcher
name|matcher
decl_stmt|;
name|String
name|wildCardId
decl_stmt|;
specifier|public
name|MatcherHolder
parameter_list|(
name|String
name|orig
parameter_list|,
name|Matcher
name|matcher
parameter_list|)
block|{
name|wildCardId
operator|=
name|orig
expr_stmt|;
name|this
operator|.
name|matcher
operator|=
name|matcher
expr_stmt|;
block|}
block|}
specifier|public
name|ConfigurerImpl
parameter_list|()
block|{
comment|// complete
block|}
specifier|public
name|ConfigurerImpl
parameter_list|(
name|ApplicationContext
name|ac
parameter_list|)
block|{
name|setApplicationContext
argument_list|(
name|ac
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initWildcardDefinitionMap
parameter_list|()
block|{
if|if
condition|(
literal|null
operator|!=
name|appContexts
condition|)
block|{
for|for
control|(
name|ApplicationContext
name|appContext
range|:
name|appContexts
control|)
block|{
for|for
control|(
name|String
name|n
range|:
name|appContext
operator|.
name|getBeanDefinitionNames
argument_list|()
control|)
block|{
if|if
condition|(
name|isWildcardBeanName
argument_list|(
name|n
argument_list|)
condition|)
block|{
name|AutowireCapableBeanFactory
name|bf
init|=
name|appContext
operator|.
name|getAutowireCapableBeanFactory
argument_list|()
decl_stmt|;
name|BeanDefinitionRegistry
name|bdr
init|=
operator|(
name|BeanDefinitionRegistry
operator|)
name|bf
decl_stmt|;
name|BeanDefinition
name|bd
init|=
name|bdr
operator|.
name|getBeanDefinition
argument_list|(
name|n
argument_list|)
decl_stmt|;
name|String
name|className
init|=
name|bd
operator|.
name|getBeanClassName
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|className
condition|)
block|{
name|String
name|orig
init|=
name|n
decl_stmt|;
if|if
condition|(
name|n
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
operator|==
literal|'*'
condition|)
block|{
comment|//old wildcard
name|n
operator|=
literal|"."
operator|+
name|n
operator|.
name|replaceAll
argument_list|(
literal|"\\."
argument_list|,
literal|"\\."
argument_list|)
expr_stmt|;
block|}
name|Matcher
name|matcher
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|n
argument_list|)
operator|.
name|matcher
argument_list|(
literal|""
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|MatcherHolder
argument_list|>
name|m
init|=
name|wildCardBeanDefinitions
operator|.
name|get
argument_list|(
name|className
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|==
literal|null
condition|)
block|{
name|m
operator|=
operator|new
name|ArrayList
argument_list|<
name|MatcherHolder
argument_list|>
argument_list|()
expr_stmt|;
name|wildCardBeanDefinitions
operator|.
name|put
argument_list|(
name|className
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
name|MatcherHolder
name|holder
init|=
operator|new
name|MatcherHolder
argument_list|(
name|orig
argument_list|,
name|matcher
argument_list|)
decl_stmt|;
name|m
operator|.
name|add
argument_list|(
name|holder
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LogUtils
operator|.
name|log
argument_list|(
name|LOG
argument_list|,
name|Level
operator|.
name|WARNING
argument_list|,
literal|"WILDCARD_BEAN_ID_WITH_NO_CLASS_MSG"
argument_list|,
name|n
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
specifier|public
name|void
name|configureBean
parameter_list|(
name|Object
name|beanInstance
parameter_list|)
block|{
name|configureBean
argument_list|(
literal|null
argument_list|,
name|beanInstance
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|configureBean
parameter_list|(
name|String
name|bn
parameter_list|,
name|Object
name|beanInstance
parameter_list|)
block|{
name|configureBean
argument_list|(
name|bn
argument_list|,
name|beanInstance
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|synchronized
name|void
name|configureBean
parameter_list|(
name|String
name|bn
parameter_list|,
name|Object
name|beanInstance
parameter_list|,
name|boolean
name|checkWildcards
parameter_list|)
block|{
if|if
condition|(
literal|null
operator|==
name|appContexts
condition|)
block|{
return|return;
block|}
if|if
condition|(
literal|null
operator|==
name|bn
condition|)
block|{
name|bn
operator|=
name|getBeanName
argument_list|(
name|beanInstance
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|null
operator|==
name|bn
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|checkWildcards
condition|)
block|{
name|configureWithWildCard
argument_list|(
name|bn
argument_list|,
name|beanInstance
argument_list|)
expr_stmt|;
block|}
specifier|final
name|String
name|beanName
init|=
name|bn
decl_stmt|;
name|setBeanWiringInfoResolver
argument_list|(
operator|new
name|BeanWiringInfoResolver
argument_list|()
block|{
specifier|public
name|BeanWiringInfo
name|resolveWiringInfo
parameter_list|(
name|Object
name|instance
parameter_list|)
block|{
if|if
condition|(
literal|null
operator|!=
name|beanName
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|beanName
argument_list|)
condition|)
block|{
return|return
operator|new
name|BeanWiringInfo
argument_list|(
name|beanName
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
for|for
control|(
name|ApplicationContext
name|appContext
range|:
name|appContexts
control|)
block|{
if|if
condition|(
name|appContext
operator|.
name|containsBean
argument_list|(
name|bn
argument_list|)
condition|)
block|{
name|this
operator|.
name|setBeanFactory
argument_list|(
name|appContext
operator|.
name|getAutowireCapableBeanFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
try|try
block|{
name|super
operator|.
name|configureBean
argument_list|(
name|beanInstance
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Successfully performed injection."
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchBeanDefinitionException
name|ex
parameter_list|)
block|{
comment|// users often wonder why the settings in their configuration files seem
comment|// to have no effect - the most common cause is that they have been using
comment|// incorrect bean ids
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"NO_MATCHING_BEAN_MSG"
argument_list|,
name|beanName
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|configureWithWildCard
parameter_list|(
name|String
name|bn
parameter_list|,
name|Object
name|beanInstance
parameter_list|)
block|{
if|if
condition|(
operator|!
name|wildCardBeanDefinitions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
name|className
init|=
name|beanInstance
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|MatcherHolder
argument_list|>
name|matchers
init|=
name|wildCardBeanDefinitions
operator|.
name|get
argument_list|(
name|className
argument_list|)
decl_stmt|;
if|if
condition|(
name|matchers
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|MatcherHolder
name|m
range|:
name|matchers
control|)
block|{
synchronized|synchronized
init|(
name|m
operator|.
name|matcher
init|)
block|{
name|m
operator|.
name|matcher
operator|.
name|reset
argument_list|(
name|bn
argument_list|)
expr_stmt|;
if|if
condition|(
name|m
operator|.
name|matcher
operator|.
name|matches
argument_list|()
condition|)
block|{
name|configureBean
argument_list|(
name|m
operator|.
name|wildCardId
argument_list|,
name|beanInstance
argument_list|,
literal|false
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
block|}
block|}
block|}
specifier|private
name|boolean
name|isWildcardBeanName
parameter_list|(
name|String
name|bn
parameter_list|)
block|{
return|return
name|bn
operator|.
name|indexOf
argument_list|(
literal|'*'
argument_list|)
operator|!=
operator|-
literal|1
operator|||
name|bn
operator|.
name|indexOf
argument_list|(
literal|'?'
argument_list|)
operator|!=
operator|-
literal|1
operator|||
operator|(
name|bn
operator|.
name|indexOf
argument_list|(
literal|'('
argument_list|)
operator|!=
operator|-
literal|1
operator|&&
name|bn
operator|.
name|indexOf
argument_list|(
literal|')'
argument_list|)
operator|!=
operator|-
literal|1
operator|)
return|;
block|}
specifier|protected
name|String
name|getBeanName
parameter_list|(
name|Object
name|beanInstance
parameter_list|)
block|{
if|if
condition|(
name|beanInstance
operator|instanceof
name|Configurable
condition|)
block|{
return|return
operator|(
operator|(
name|Configurable
operator|)
name|beanInstance
operator|)
operator|.
name|getBeanName
argument_list|()
return|;
block|}
name|String
name|beanName
init|=
literal|null
decl_stmt|;
name|Method
name|m
init|=
literal|null
decl_stmt|;
try|try
block|{
name|m
operator|=
name|beanInstance
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredMethod
argument_list|(
literal|"getBeanName"
argument_list|,
operator|(
name|Class
index|[]
operator|)
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|ex
parameter_list|)
block|{
try|try
block|{
name|m
operator|=
name|beanInstance
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getBeanName"
argument_list|,
operator|(
name|Class
index|[]
operator|)
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
comment|//ignore
block|}
block|}
if|if
condition|(
name|m
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|beanName
operator|=
call|(
name|String
call|)
argument_list|(
name|m
operator|.
name|invoke
argument_list|(
name|beanInstance
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|LogUtils
operator|.
name|log
argument_list|(
name|LOG
argument_list|,
name|Level
operator|.
name|WARNING
argument_list|,
literal|"ERROR_DETERMINING_BEAN_NAME_EXC"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
literal|null
operator|==
name|beanName
condition|)
block|{
name|LogUtils
operator|.
name|log
argument_list|(
name|LOG
argument_list|,
name|Level
operator|.
name|INFO
argument_list|,
literal|"COULD_NOT_DETERMINE_BEAN_NAME_MSG"
argument_list|,
name|beanInstance
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|beanName
return|;
block|}
specifier|public
specifier|final
name|void
name|setApplicationContext
parameter_list|(
name|ApplicationContext
name|ac
parameter_list|)
block|{
name|appContexts
operator|=
operator|new
name|CopyOnWriteArraySet
argument_list|<
name|ApplicationContext
argument_list|>
argument_list|()
expr_stmt|;
name|addApplicationContext
argument_list|(
name|ac
argument_list|)
expr_stmt|;
name|setBeanFactory
argument_list|(
name|ac
operator|.
name|getAutowireCapableBeanFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|final
name|void
name|addApplicationContext
parameter_list|(
name|ApplicationContext
name|ac
parameter_list|)
block|{
if|if
condition|(
operator|!
name|appContexts
operator|.
name|contains
argument_list|(
name|ac
argument_list|)
condition|)
block|{
name|appContexts
operator|.
name|add
argument_list|(
name|ac
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ApplicationContext
argument_list|>
name|inactiveApplicationContexts
init|=
operator|new
name|ArrayList
argument_list|<
name|ApplicationContext
argument_list|>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|ApplicationContext
argument_list|>
name|it
init|=
name|appContexts
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ApplicationContext
name|c
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|c
operator|instanceof
name|ConfigurableApplicationContext
operator|&&
operator|!
operator|(
operator|(
name|ConfigurableApplicationContext
operator|)
name|c
operator|)
operator|.
name|isActive
argument_list|()
condition|)
block|{
name|inactiveApplicationContexts
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Remove the inactive application context here can avoid the UnsupportedOperationException
for|for
control|(
name|ApplicationContext
name|context
range|:
name|inactiveApplicationContexts
control|)
block|{
name|appContexts
operator|.
name|remove
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
name|initWildcardDefinitionMap
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|destroy
parameter_list|()
block|{
name|super
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|appContexts
operator|.
name|clear
argument_list|()
expr_stmt|;
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
name|Configurer
operator|.
name|class
return|;
block|}
specifier|protected
name|Set
argument_list|<
name|ApplicationContext
argument_list|>
name|getAppContexts
parameter_list|()
block|{
return|return
name|appContexts
return|;
block|}
block|}
end_class

end_unit

