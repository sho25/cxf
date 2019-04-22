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
name|blueprint
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
name|TreeMap
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
name|aries
operator|.
name|blueprint
operator|.
name|services
operator|.
name|ExtendedBlueprintContainer
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
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|container
operator|.
name|BlueprintContainer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|container
operator|.
name|NoSuchComponentException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|reflect
operator|.
name|BeanMetadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|reflect
operator|.
name|ComponentMetadata
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|ConfigurerImpl
implements|implements
name|Configurer
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
name|BlueprintContainer
name|container
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
name|TreeMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|static
class|class
name|MatcherHolder
implements|implements
name|Comparable
argument_list|<
name|MatcherHolder
argument_list|>
block|{
name|Matcher
name|matcher
decl_stmt|;
name|String
name|wildCardId
decl_stmt|;
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
annotation|@
name|Override
specifier|public
name|int
name|compareTo
parameter_list|(
name|MatcherHolder
name|mh
parameter_list|)
block|{
name|Integer
name|literalCharsLen1
init|=
name|this
operator|.
name|wildCardId
operator|.
name|replace
argument_list|(
literal|"*"
argument_list|,
literal|""
argument_list|)
operator|.
name|length
argument_list|()
decl_stmt|;
name|Integer
name|literalCharsLen2
init|=
name|mh
operator|.
name|wildCardId
operator|.
name|replace
argument_list|(
literal|"*"
argument_list|,
literal|""
argument_list|)
operator|.
name|length
argument_list|()
decl_stmt|;
comment|// The expression with more literal characters should end up on the top of the list
return|return
name|literalCharsLen1
operator|.
name|compareTo
argument_list|(
name|literalCharsLen2
argument_list|)
operator|*
operator|-
literal|1
return|;
block|}
block|}
specifier|public
name|ConfigurerImpl
parameter_list|(
name|BlueprintContainer
name|con
parameter_list|)
block|{
name|container
operator|=
name|con
expr_stmt|;
name|initializeWildcardMap
argument_list|()
expr_stmt|;
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
specifier|private
name|void
name|initializeWildcardMap
parameter_list|()
block|{
for|for
control|(
name|String
name|s
range|:
name|container
operator|.
name|getComponentIds
argument_list|()
control|)
block|{
if|if
condition|(
name|isWildcardBeanName
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|ComponentMetadata
name|cmd
init|=
name|container
operator|.
name|getComponentMetadata
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
name|BlueprintBeanLocator
operator|.
name|getClassForMetaData
argument_list|(
name|container
argument_list|,
name|cmd
argument_list|)
decl_stmt|;
if|if
condition|(
name|cls
operator|!=
literal|null
condition|)
block|{
name|String
name|orig
init|=
name|s
decl_stmt|;
if|if
condition|(
name|s
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
name|s
operator|=
literal|"."
operator|+
name|s
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
name|s
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
name|cls
operator|.
name|getName
argument_list|()
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
argument_list|<>
argument_list|()
expr_stmt|;
name|wildCardBeanDefinitions
operator|.
name|put
argument_list|(
name|cls
operator|.
name|getName
argument_list|()
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
if|if
condition|(
name|container
operator|instanceof
name|ExtendedBlueprintContainer
condition|)
block|{
name|ComponentMetadata
name|cm
init|=
literal|null
decl_stmt|;
try|try
block|{
name|cm
operator|=
name|container
operator|.
name|getComponentMetadata
argument_list|(
name|bn
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchComponentException
name|nsce
parameter_list|)
block|{
name|cm
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|cm
operator|instanceof
name|BeanMetadata
condition|)
block|{
operator|(
operator|(
name|ExtendedBlueprintContainer
operator|)
name|container
operator|)
operator|.
name|injectBeanInstance
argument_list|(
operator|(
name|BeanMetadata
operator|)
name|cm
argument_list|,
name|beanInstance
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
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|beanInstance
operator|.
name|getClass
argument_list|()
decl_stmt|;
while|while
condition|(
operator|!
name|Object
operator|.
name|class
operator|.
name|equals
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
name|String
name|className
init|=
name|clazz
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
name|clazz
operator|=
name|clazz
operator|.
name|getSuperclass
argument_list|()
expr_stmt|;
block|}
block|}
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
name|FINE
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
block|}
end_class

end_unit

