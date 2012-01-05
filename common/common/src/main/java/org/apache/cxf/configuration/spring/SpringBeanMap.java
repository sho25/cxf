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
name|springframework
operator|.
name|beans
operator|.
name|Mergeable
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
name|BeanIsAbstractException
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
name|TypedStringValue
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
name|ManagedList
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
name|ManagedSet
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

begin_class
annotation|@
name|NoJSR250Annotations
specifier|public
class|class
name|SpringBeanMap
parameter_list|<
name|V
parameter_list|>
extends|extends
name|AbstractSpringBeanMap
argument_list|<
name|String
argument_list|,
name|V
argument_list|>
block|{
specifier|protected
name|void
name|processBeans
parameter_list|(
name|ApplicationContext
name|beanFactory
parameter_list|)
block|{
if|if
condition|(
name|beanFactory
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|String
index|[]
name|beanNames
init|=
name|beanFactory
operator|.
name|getBeanNamesForType
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|ConfigurableApplicationContext
name|ctxt
init|=
operator|(
name|ConfigurableApplicationContext
operator|)
name|beanFactory
decl_stmt|;
comment|// Take any bean name or alias that has a web service annotation
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|beanNames
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|BeanDefinition
name|def
init|=
name|ctxt
operator|.
name|getBeanFactory
argument_list|()
operator|.
name|getBeanDefinition
argument_list|(
name|beanNames
index|[
name|i
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|beanFactory
operator|.
name|isSingleton
argument_list|(
name|beanNames
index|[
name|i
index|]
argument_list|)
operator|||
name|def
operator|.
name|isAbstract
argument_list|()
condition|)
block|{
continue|continue;
block|}
try|try
block|{
name|Collection
argument_list|<
name|?
argument_list|>
name|ids
init|=
literal|null
decl_stmt|;
name|PropertyValue
name|pv
init|=
name|def
operator|.
name|getPropertyValues
argument_list|()
operator|.
name|getPropertyValue
argument_list|(
name|idsProperty
argument_list|)
decl_stmt|;
if|if
condition|(
name|pv
operator|!=
literal|null
condition|)
block|{
name|Object
name|value
init|=
name|pv
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|value
operator|instanceof
name|Collection
operator|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"The property "
operator|+
name|idsProperty
operator|+
literal|" must be a collection!"
argument_list|)
throw|;
block|}
if|if
condition|(
name|value
operator|instanceof
name|Mergeable
condition|)
block|{
if|if
condition|(
operator|!
operator|(
operator|(
name|Mergeable
operator|)
name|value
operator|)
operator|.
name|isMergeEnabled
argument_list|()
condition|)
block|{
name|ids
operator|=
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|value
expr_stmt|;
block|}
block|}
else|else
block|{
name|ids
operator|=
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|value
expr_stmt|;
block|}
block|}
if|if
condition|(
name|ids
operator|==
literal|null
condition|)
block|{
name|ids
operator|=
name|getIds
argument_list|(
name|ctxt
operator|.
name|getBean
argument_list|(
name|beanNames
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|ids
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
block|}
if|if
condition|(
name|ids
operator|instanceof
name|ManagedSet
operator|||
name|ids
operator|instanceof
name|ManagedList
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|newIds
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|?
argument_list|>
name|itr
init|=
name|ids
operator|.
name|iterator
argument_list|()
init|;
name|itr
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Object
name|o
init|=
name|itr
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|TypedStringValue
condition|)
block|{
name|newIds
operator|.
name|add
argument_list|(
operator|(
operator|(
name|TypedStringValue
operator|)
name|o
operator|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|newIds
operator|.
name|add
argument_list|(
operator|(
name|String
operator|)
name|o
argument_list|)
expr_stmt|;
block|}
block|}
name|ids
operator|=
name|newIds
expr_stmt|;
block|}
for|for
control|(
name|Object
name|id
range|:
name|ids
control|)
block|{
name|getBeanListForId
argument_list|(
name|id
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|add
argument_list|(
name|beanNames
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|BeanIsAbstractException
name|e
parameter_list|)
block|{
comment|// The bean is abstract, we won't be doing anything with it.
continue|continue;
block|}
block|}
name|processBeans
argument_list|(
name|ctxt
operator|.
name|getParent
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

