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
name|Arrays
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|LinkedHashSet
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
name|java
operator|.
name|util
operator|.
name|Set
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
name|extension
operator|.
name|ExtensionManagerImpl
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
name|osgi
operator|.
name|framework
operator|.
name|ServiceReference
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
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|SpringBeanLocator
implements|implements
name|ConfiguredBeanLocator
block|{
name|ApplicationContext
name|context
decl_stmt|;
name|ConfiguredBeanLocator
name|orig
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|passThroughs
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|Object
name|bundleContext
decl_stmt|;
name|boolean
name|osgi
init|=
literal|true
decl_stmt|;
specifier|public
name|SpringBeanLocator
parameter_list|(
name|ApplicationContext
name|ctx
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|context
operator|=
name|ctx
expr_stmt|;
name|orig
operator|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ConfiguredBeanLocator
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|orig
operator|instanceof
name|ExtensionManagerImpl
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|names
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
name|String
name|s
range|:
name|ctx
operator|.
name|getBeanDefinitionNames
argument_list|()
control|)
block|{
name|ConfigurableApplicationContext
name|ctxt
init|=
operator|(
name|ConfigurableApplicationContext
operator|)
name|context
decl_stmt|;
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
name|s
argument_list|)
decl_stmt|;
name|String
name|cn
init|=
name|def
operator|.
name|getBeanClassName
argument_list|()
decl_stmt|;
if|if
condition|(
name|OldSpringSupport
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|cn
argument_list|)
condition|)
block|{
name|passThroughs
operator|.
name|add
argument_list|(
name|s
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|s2
range|:
name|ctx
operator|.
name|getAliases
argument_list|(
name|s
argument_list|)
control|)
block|{
name|passThroughs
operator|.
name|add
argument_list|(
name|s2
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|names
operator|.
name|add
argument_list|(
name|s
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|s2
range|:
name|ctx
operator|.
name|getAliases
argument_list|(
name|s
argument_list|)
control|)
block|{
name|names
operator|.
name|add
argument_list|(
name|s2
argument_list|)
expr_stmt|;
block|}
block|}
block|}
operator|(
operator|(
name|ExtensionManagerImpl
operator|)
name|orig
operator|)
operator|.
name|removeBeansOfNames
argument_list|(
name|names
argument_list|)
expr_stmt|;
block|}
name|loadOSGIContext
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|loadOSGIContext
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
try|try
block|{
comment|//use a little reflection to allow this to work without the spring-dm jars
comment|//for the non-osgi cases
name|Method
name|m
init|=
name|context
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getBundleContext"
argument_list|)
decl_stmt|;
name|bundleContext
operator|=
name|m
operator|.
name|invoke
argument_list|(
name|context
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Class
argument_list|<
name|Object
argument_list|>
name|cls
init|=
operator|(
name|Class
argument_list|<
name|Object
argument_list|>
operator|)
name|m
operator|.
name|getReturnType
argument_list|()
decl_stmt|;
name|b
operator|.
name|setExtension
argument_list|(
name|bundleContext
argument_list|,
name|cls
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//ignore
name|osgi
operator|=
literal|false
expr_stmt|;
block|}
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getBeanOfType
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|T
name|t
init|=
literal|null
decl_stmt|;
try|try
block|{
name|t
operator|=
name|context
operator|.
name|getBean
argument_list|(
name|name
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchBeanDefinitionException
name|nsbde
parameter_list|)
block|{
comment|//ignore
block|}
if|if
condition|(
name|t
operator|==
literal|null
condition|)
block|{
name|t
operator|=
name|orig
operator|.
name|getBeanOfType
argument_list|(
name|name
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
return|return
name|t
return|;
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getBeanNamesOfType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|s
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|String
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|context
operator|.
name|getBeanNamesForType
argument_list|(
name|type
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|s
operator|.
name|removeAll
argument_list|(
name|passThroughs
argument_list|)
expr_stmt|;
name|s
operator|.
name|addAll
argument_list|(
name|orig
operator|.
name|getBeanNamesOfType
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
return|return
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|s
argument_list|)
return|;
block|}
comment|/** {@inheritDoc}*/
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Collection
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|getBeansOfType
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|s
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|String
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|context
operator|.
name|getBeanNamesForType
argument_list|(
name|type
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|s
operator|.
name|removeAll
argument_list|(
name|passThroughs
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|T
argument_list|>
name|lst
init|=
operator|new
name|LinkedList
argument_list|<
name|T
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|n
range|:
name|s
control|)
block|{
name|lst
operator|.
name|add
argument_list|(
name|context
operator|.
name|getBean
argument_list|(
name|n
argument_list|,
name|type
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|lst
operator|.
name|addAll
argument_list|(
name|orig
operator|.
name|getBeansOfType
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|lst
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|tryOSGI
argument_list|(
name|lst
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
return|return
name|lst
return|;
block|}
specifier|private
parameter_list|<
name|T
parameter_list|>
name|void
name|tryOSGI
parameter_list|(
name|Collection
argument_list|<
name|T
argument_list|>
name|lst
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
if|if
condition|(
operator|!
name|osgi
condition|)
block|{
return|return;
block|}
try|try
block|{
comment|//use a little reflection to allow this to work without the spring-dm jars
comment|//for the non-osgi cases
name|Object
name|o
init|=
name|bundleContext
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getServiceReference"
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|.
name|invoke
argument_list|(
name|bundleContext
argument_list|,
name|type
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|!=
literal|null
condition|)
block|{
name|o
operator|=
name|bundleContext
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getService"
argument_list|,
name|ServiceReference
operator|.
name|class
argument_list|)
operator|.
name|invoke
argument_list|(
name|bundleContext
argument_list|,
name|o
argument_list|)
expr_stmt|;
name|lst
operator|.
name|add
argument_list|(
name|type
operator|.
name|cast
argument_list|(
name|o
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
name|osgi
operator|=
literal|false
expr_stmt|;
comment|//not using OSGi
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|//ignore
block|}
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|boolean
name|loadBeansOfType
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|BeanLoaderListener
argument_list|<
name|T
argument_list|>
name|listener
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|context
operator|.
name|getBeanNamesForType
argument_list|(
name|type
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|list
operator|.
name|removeAll
argument_list|(
name|passThroughs
argument_list|)
expr_stmt|;
name|Collections
operator|.
name|reverse
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|boolean
name|loaded
init|=
literal|false
decl_stmt|;
for|for
control|(
name|String
name|s
range|:
name|list
control|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|beanType
init|=
name|context
operator|.
name|getType
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|t
init|=
name|beanType
operator|.
name|asSubclass
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|listener
operator|.
name|loadBean
argument_list|(
name|s
argument_list|,
name|t
argument_list|)
condition|)
block|{
name|Object
name|o
init|=
name|context
operator|.
name|getBean
argument_list|(
name|s
argument_list|)
decl_stmt|;
if|if
condition|(
name|listener
operator|.
name|beanLoaded
argument_list|(
name|s
argument_list|,
name|type
operator|.
name|cast
argument_list|(
name|o
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
name|loaded
operator|=
literal|true
expr_stmt|;
block|}
block|}
return|return
name|loaded
operator|||
name|orig
operator|.
name|loadBeansOfType
argument_list|(
name|type
argument_list|,
name|listener
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|hasConfiguredPropertyValue
parameter_list|(
name|String
name|beanName
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|String
name|searchValue
parameter_list|)
block|{
if|if
condition|(
name|context
operator|.
name|containsBean
argument_list|(
name|beanName
argument_list|)
operator|&&
operator|!
name|passThroughs
operator|.
name|contains
argument_list|(
name|beanName
argument_list|)
condition|)
block|{
name|ConfigurableApplicationContext
name|ctxt
init|=
operator|(
name|ConfigurableApplicationContext
operator|)
name|context
decl_stmt|;
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
name|beanName
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|ctxt
operator|.
name|getBeanFactory
argument_list|()
operator|.
name|isSingleton
argument_list|(
name|beanName
argument_list|)
operator|||
name|def
operator|.
name|isAbstract
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
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
name|propertyName
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
name|propertyName
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
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Iterator
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
if|if
condition|(
name|searchValue
operator|.
name|equals
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
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|searchValue
operator|.
name|equals
argument_list|(
operator|(
name|String
operator|)
name|o
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
block|}
block|}
return|return
name|orig
operator|.
name|hasConfiguredPropertyValue
argument_list|(
name|beanName
argument_list|,
name|propertyName
argument_list|,
name|searchValue
argument_list|)
return|;
block|}
block|}
end_class

end_unit

