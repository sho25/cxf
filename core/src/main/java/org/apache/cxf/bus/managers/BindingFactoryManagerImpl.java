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
name|managers
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
name|Field
import|;
end_import

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
name|Collection
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
name|ResourceBundle
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
name|ConcurrentHashMap
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
name|javax
operator|.
name|annotation
operator|.
name|Resource
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
name|BusException
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
name|binding
operator|.
name|AbstractBindingFactory
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
name|binding
operator|.
name|BindingFactory
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
name|binding
operator|.
name|BindingFactoryManager
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
name|i18n
operator|.
name|BundleUtils
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
name|i18n
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
name|helpers
operator|.
name|CastUtils
import|;
end_import

begin_class
annotation|@
name|NoJSR250Annotations
argument_list|(
name|unlessNull
operator|=
literal|"bus"
argument_list|)
specifier|public
specifier|final
class|class
name|BindingFactoryManagerImpl
implements|implements
name|BindingFactoryManager
block|{
specifier|private
specifier|static
specifier|final
name|ResourceBundle
name|BUNDLE
init|=
name|BundleUtils
operator|.
name|getBundle
argument_list|(
name|BindingFactoryManagerImpl
operator|.
name|class
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|BindingFactory
argument_list|>
name|bindingFactories
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|failed
init|=
operator|new
name|CopyOnWriteArraySet
argument_list|<>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|loaded
init|=
operator|new
name|CopyOnWriteArraySet
argument_list|<>
argument_list|()
decl_stmt|;
name|Bus
name|bus
decl_stmt|;
specifier|public
name|BindingFactoryManagerImpl
parameter_list|()
block|{
name|bindingFactories
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|BindingFactory
argument_list|>
argument_list|(
literal|8
argument_list|,
literal|0.75f
argument_list|,
literal|4
argument_list|)
expr_stmt|;
block|}
specifier|public
name|BindingFactoryManagerImpl
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|bindingFactories
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|BindingFactory
argument_list|>
argument_list|(
literal|8
argument_list|,
literal|0.75f
argument_list|,
literal|4
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
literal|null
operator|!=
name|bus
condition|)
block|{
name|bus
operator|.
name|setExtension
argument_list|(
name|this
argument_list|,
name|BindingFactoryManager
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|registerBindingFactory
parameter_list|(
name|String
name|name
parameter_list|,
name|BindingFactory
name|factory
parameter_list|)
block|{
name|bindingFactories
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|factory
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|unregisterBindingFactory
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|bindingFactories
operator|.
name|remove
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|BindingFactory
name|getBindingFactory
parameter_list|(
specifier|final
name|String
name|namespace
parameter_list|)
throws|throws
name|BusException
block|{
name|BindingFactory
name|factory
init|=
name|bindingFactories
operator|.
name|get
argument_list|(
name|namespace
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|factory
condition|)
block|{
if|if
condition|(
operator|!
name|failed
operator|.
name|contains
argument_list|(
name|namespace
argument_list|)
condition|)
block|{
name|factory
operator|=
name|loadActivationNamespace
argument_list|(
name|namespace
argument_list|)
expr_stmt|;
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
name|factory
operator|=
name|loadDefaultNamespace
argument_list|(
name|namespace
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
name|factory
operator|=
name|loadAll
argument_list|(
name|namespace
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
name|failed
operator|.
name|add
argument_list|(
name|namespace
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|BusException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"NO_BINDING_FACTORY_EXC"
argument_list|,
name|BUNDLE
argument_list|,
name|namespace
argument_list|)
argument_list|)
throw|;
block|}
block|}
return|return
name|factory
return|;
block|}
specifier|private
name|BindingFactory
name|loadAll
parameter_list|(
specifier|final
name|String
name|namespace
parameter_list|)
block|{
comment|//Try old method of having activationNamespaces configured in.
comment|//It activates all the factories in the list until one matches, thus
comment|//it activates stuff that really aren't needed.
name|ConfiguredBeanLocator
operator|.
name|BeanLoaderListener
argument_list|<
name|BindingFactory
argument_list|>
name|listener
init|=
operator|new
name|ConfiguredBeanLocator
operator|.
name|BeanLoaderListener
argument_list|<
name|BindingFactory
argument_list|>
argument_list|()
block|{
specifier|public
name|boolean
name|beanLoaded
parameter_list|(
name|String
name|name
parameter_list|,
name|BindingFactory
name|bean
parameter_list|)
block|{
name|loaded
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|bindingFactories
operator|.
name|containsKey
argument_list|(
name|namespace
argument_list|)
condition|)
block|{
if|if
condition|(
name|bean
operator|instanceof
name|AbstractBindingFactory
condition|)
block|{
for|for
control|(
name|String
name|ns
range|:
operator|(
operator|(
name|AbstractBindingFactory
operator|)
name|bean
operator|)
operator|.
name|getActivationNamespaces
argument_list|()
control|)
block|{
name|registerBindingFactory
argument_list|(
name|ns
argument_list|,
name|bean
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
try|try
block|{
name|Method
name|m
init|=
name|bean
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getActivationNamespace"
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|String
argument_list|>
name|c
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|m
operator|.
name|invoke
argument_list|(
name|bean
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|s
range|:
name|c
control|)
block|{
name|registerBindingFactory
argument_list|(
name|s
argument_list|,
name|bean
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//ignore
block|}
block|}
block|}
return|return
name|bindingFactories
operator|.
name|containsKey
argument_list|(
name|namespace
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|loadBean
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|BindingFactory
argument_list|>
name|type
parameter_list|)
block|{
return|return
operator|!
name|bindingFactories
operator|.
name|containsKey
argument_list|(
name|namespace
argument_list|)
operator|&&
operator|!
name|loaded
operator|.
name|contains
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|bus
operator|.
name|getExtension
argument_list|(
name|ConfiguredBeanLocator
operator|.
name|class
argument_list|)
operator|.
name|loadBeansOfType
argument_list|(
name|BindingFactory
operator|.
name|class
argument_list|,
name|listener
argument_list|)
expr_stmt|;
return|return
name|bindingFactories
operator|.
name|get
argument_list|(
name|namespace
argument_list|)
return|;
block|}
specifier|private
name|BindingFactory
name|loadDefaultNamespace
parameter_list|(
specifier|final
name|String
name|namespace
parameter_list|)
block|{
comment|//First attempt will be to examine the factory class
comment|//for a DEFAULT_NAMESPACES field and use it
name|ConfiguredBeanLocator
operator|.
name|BeanLoaderListener
argument_list|<
name|BindingFactory
argument_list|>
name|listener
init|=
operator|new
name|ConfiguredBeanLocator
operator|.
name|BeanLoaderListener
argument_list|<
name|BindingFactory
argument_list|>
argument_list|()
block|{
specifier|public
name|boolean
name|beanLoaded
parameter_list|(
name|String
name|name
parameter_list|,
name|BindingFactory
name|bean
parameter_list|)
block|{
name|loaded
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|bindingFactories
operator|.
name|containsKey
argument_list|(
name|namespace
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|loadBean
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|BindingFactory
argument_list|>
name|type
parameter_list|)
block|{
if|if
condition|(
operator|!
name|loaded
operator|.
name|contains
argument_list|(
name|name
argument_list|)
condition|)
block|{
try|try
block|{
name|Field
name|f
init|=
name|type
operator|.
name|getField
argument_list|(
literal|"DEFAULT_NAMESPACES"
argument_list|)
decl_stmt|;
name|Object
name|o
init|=
name|f
operator|.
name|get
argument_list|(
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|Collection
condition|)
block|{
name|Collection
argument_list|<
name|String
argument_list|>
name|c
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|o
argument_list|)
decl_stmt|;
return|return
name|c
operator|.
name|contains
argument_list|(
name|namespace
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//ignore
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
decl_stmt|;
name|bus
operator|.
name|getExtension
argument_list|(
name|ConfiguredBeanLocator
operator|.
name|class
argument_list|)
operator|.
name|loadBeansOfType
argument_list|(
name|BindingFactory
operator|.
name|class
argument_list|,
name|listener
argument_list|)
expr_stmt|;
return|return
name|bindingFactories
operator|.
name|get
argument_list|(
name|namespace
argument_list|)
return|;
block|}
specifier|private
name|BindingFactory
name|loadActivationNamespace
parameter_list|(
specifier|final
name|String
name|namespace
parameter_list|)
block|{
specifier|final
name|ConfiguredBeanLocator
name|locator
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
comment|//Second attempt will be to examine the factory class
comment|//for a DEFAULT_NAMESPACES field and if it doesn't exist, try
comment|//using the older activation ns things.  This will then load most
comment|//of the "older" things
name|ConfiguredBeanLocator
operator|.
name|BeanLoaderListener
argument_list|<
name|BindingFactory
argument_list|>
name|listener
init|=
operator|new
name|ConfiguredBeanLocator
operator|.
name|BeanLoaderListener
argument_list|<
name|BindingFactory
argument_list|>
argument_list|()
block|{
specifier|public
name|boolean
name|beanLoaded
parameter_list|(
name|String
name|name
parameter_list|,
name|BindingFactory
name|bean
parameter_list|)
block|{
name|loaded
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|bindingFactories
operator|.
name|containsKey
argument_list|(
name|namespace
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|loadBean
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|BindingFactory
argument_list|>
name|type
parameter_list|)
block|{
if|if
condition|(
name|loaded
operator|.
name|contains
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
try|try
block|{
name|type
operator|.
name|getField
argument_list|(
literal|"DEFAULT_NAMESPACES"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//ignore
block|}
return|return
name|locator
operator|.
name|hasConfiguredPropertyValue
argument_list|(
name|name
argument_list|,
literal|"activationNamespaces"
argument_list|,
name|namespace
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|locator
operator|.
name|loadBeansOfType
argument_list|(
name|BindingFactory
operator|.
name|class
argument_list|,
name|listener
argument_list|)
expr_stmt|;
return|return
name|bindingFactories
operator|.
name|get
argument_list|(
name|namespace
argument_list|)
return|;
block|}
block|}
end_class

end_unit

