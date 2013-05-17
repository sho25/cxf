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
name|ExtensionManagerBus
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
name|buslifecycle
operator|.
name|BusLifeCycleManager
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
name|configuration
operator|.
name|spring
operator|.
name|ConfigurerImpl
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
name|resource
operator|.
name|ResourceManager
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
name|ApplicationEvent
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
name|ApplicationListener
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
name|event
operator|.
name|ContextClosedEvent
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
name|event
operator|.
name|ContextRefreshedEvent
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
name|support
operator|.
name|AbstractApplicationContext
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|SpringBus
extends|extends
name|ExtensionManagerBus
implements|implements
name|ApplicationContextAware
block|{
name|AbstractApplicationContext
name|ctx
decl_stmt|;
name|boolean
name|closeContext
decl_stmt|;
specifier|public
name|SpringBus
parameter_list|()
block|{     }
specifier|public
name|void
name|setBusConfig
parameter_list|(
name|BusDefinitionParser
operator|.
name|BusConfig
name|bc
parameter_list|)
block|{
name|bc
operator|.
name|setBus
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|loadAdditionalFeatures
parameter_list|()
block|{
name|super
operator|.
name|loadAdditionalFeatures
argument_list|()
expr_stmt|;
block|}
comment|/** {@inheritDoc}*/
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
name|ctx
operator|=
operator|(
name|AbstractApplicationContext
operator|)
name|applicationContext
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
name|ApplicationListener
name|listener
init|=
operator|new
name|ApplicationListener
argument_list|()
block|{
specifier|public
name|void
name|onApplicationEvent
parameter_list|(
name|ApplicationEvent
name|event
parameter_list|)
block|{
name|SpringBus
operator|.
name|this
operator|.
name|onApplicationEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|ctx
operator|.
name|addApplicationListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|ApplicationContext
name|ac
init|=
name|applicationContext
operator|.
name|getParent
argument_list|()
decl_stmt|;
while|while
condition|(
name|ac
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|ac
operator|instanceof
name|AbstractApplicationContext
condition|)
block|{
operator|(
operator|(
name|AbstractApplicationContext
operator|)
name|ac
operator|)
operator|.
name|addApplicationListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
name|ac
operator|=
name|ac
operator|.
name|getParent
argument_list|()
expr_stmt|;
block|}
comment|// set the classLoader extension with the application context classLoader
name|setExtension
argument_list|(
name|applicationContext
operator|.
name|getClassLoader
argument_list|()
argument_list|,
name|ClassLoader
operator|.
name|class
argument_list|)
expr_stmt|;
name|setExtension
argument_list|(
operator|new
name|ConfigurerImpl
argument_list|(
name|applicationContext
argument_list|)
argument_list|,
name|Configurer
operator|.
name|class
argument_list|)
expr_stmt|;
name|ResourceManager
name|m
init|=
name|getExtension
argument_list|(
name|ResourceManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|m
operator|.
name|addResourceResolver
argument_list|(
operator|new
name|BusApplicationContextResourceResolver
argument_list|(
name|applicationContext
argument_list|)
argument_list|)
expr_stmt|;
name|setExtension
argument_list|(
name|applicationContext
argument_list|,
name|ApplicationContext
operator|.
name|class
argument_list|)
expr_stmt|;
name|ConfiguredBeanLocator
name|loc
init|=
name|getExtension
argument_list|(
name|ConfiguredBeanLocator
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|loc
operator|instanceof
name|SpringBeanLocator
operator|)
condition|)
block|{
name|setExtension
argument_list|(
operator|new
name|SpringBeanLocator
argument_list|(
name|applicationContext
argument_list|,
name|this
argument_list|)
argument_list|,
name|ConfiguredBeanLocator
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getState
argument_list|()
operator|!=
name|BusState
operator|.
name|RUNNING
condition|)
block|{
name|initialize
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|onApplicationEvent
parameter_list|(
name|ApplicationEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|ctx
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|boolean
name|doIt
init|=
literal|false
decl_stmt|;
name|ApplicationContext
name|ac
init|=
name|ctx
decl_stmt|;
while|while
condition|(
name|ac
operator|!=
literal|null
operator|&&
operator|!
name|doIt
condition|)
block|{
if|if
condition|(
name|event
operator|.
name|getSource
argument_list|()
operator|==
name|ac
condition|)
block|{
name|doIt
operator|=
literal|true
expr_stmt|;
break|break;
block|}
name|ac
operator|=
name|ac
operator|.
name|getParent
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|doIt
condition|)
block|{
if|if
condition|(
name|event
operator|instanceof
name|ContextRefreshedEvent
condition|)
block|{
if|if
condition|(
name|getState
argument_list|()
operator|!=
name|BusState
operator|.
name|RUNNING
condition|)
block|{
name|initialize
argument_list|()
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|event
operator|instanceof
name|ContextClosedEvent
condition|)
block|{
name|getExtension
argument_list|(
name|BusLifeCycleManager
operator|.
name|class
argument_list|)
operator|.
name|postShutdown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|destroyBeans
parameter_list|()
block|{
if|if
condition|(
name|closeContext
condition|)
block|{
name|ctx
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|destroyBeans
argument_list|()
expr_stmt|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clsbc
init|=
name|Class
operator|.
name|forName
argument_list|(
literal|"org.osgi.framework.BundleContext"
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|clsb
init|=
name|Class
operator|.
name|forName
argument_list|(
literal|"org.osgi.framework.Bundle"
argument_list|)
decl_stmt|;
name|Object
name|o
init|=
name|getExtension
argument_list|(
name|clsbc
argument_list|)
decl_stmt|;
name|Object
name|o2
init|=
name|clsbc
operator|.
name|getMethod
argument_list|(
literal|"getBundle"
argument_list|)
operator|.
name|invoke
argument_list|(
name|o
argument_list|)
decl_stmt|;
name|String
name|s
init|=
operator|(
name|String
operator|)
name|clsb
operator|.
name|getMethod
argument_list|(
literal|"getSymbolicName"
argument_list|)
operator|.
name|invoke
argument_list|(
name|o2
argument_list|)
decl_stmt|;
name|id
operator|=
name|s
operator|+
literal|"-"
operator|+
name|DEFAULT_BUS_ID
operator|+
name|Integer
operator|.
name|toString
argument_list|(
name|this
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|id
operator|=
name|super
operator|.
name|getId
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|id
return|;
block|}
specifier|public
name|void
name|setCloseContext
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
name|closeContext
operator|=
name|b
expr_stmt|;
block|}
block|}
end_class

end_unit

