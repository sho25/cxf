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
name|common
operator|.
name|injection
operator|.
name|ResourceInjector
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
name|DestructionAwareBeanPostProcessor
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
name|core
operator|.
name|Ordered
import|;
end_import

begin_class
specifier|public
class|class
name|Jsr250BeanPostProcessor
implements|implements
name|DestructionAwareBeanPostProcessor
implements|,
name|Ordered
implements|,
name|ApplicationContextAware
block|{
specifier|private
name|ResourceManager
name|resourceManager
decl_stmt|;
specifier|private
name|ApplicationContext
name|context
decl_stmt|;
specifier|private
name|boolean
name|isProcessing
init|=
literal|true
decl_stmt|;
comment|//private int count;
comment|//private int count2;
name|Jsr250BeanPostProcessor
parameter_list|()
block|{     }
specifier|public
name|void
name|setApplicationContext
parameter_list|(
name|ApplicationContext
name|arg0
parameter_list|)
throws|throws
name|BeansException
block|{
name|context
operator|=
name|arg0
expr_stmt|;
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
name|Class
operator|.
name|forName
argument_list|(
literal|"org.springframework.context.annotation.CommonAnnotationBeanPostProcessor"
argument_list|)
decl_stmt|;
name|isProcessing
operator|=
name|context
operator|.
name|getBeanNamesForType
argument_list|(
name|cls
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
operator|.
name|length
operator|==
literal|0
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
name|isProcessing
operator|=
literal|true
expr_stmt|;
block|}
block|}
specifier|public
name|int
name|getOrder
parameter_list|()
block|{
return|return
literal|1010
return|;
block|}
specifier|private
name|boolean
name|injectable
parameter_list|(
name|Object
name|bean
parameter_list|,
name|String
name|beanId
parameter_list|)
block|{
return|return
operator|!
literal|"cxf"
operator|.
name|equals
argument_list|(
name|beanId
argument_list|)
operator|&&
name|ResourceInjector
operator|.
name|processable
argument_list|(
name|bean
operator|.
name|getClass
argument_list|()
argument_list|,
name|bean
argument_list|)
return|;
block|}
specifier|private
name|ResourceManager
name|getResourceManager
parameter_list|(
name|Object
name|bean
parameter_list|)
block|{
if|if
condition|(
name|resourceManager
operator|==
literal|null
condition|)
block|{
name|boolean
name|temp
init|=
name|isProcessing
decl_stmt|;
name|isProcessing
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|bean
operator|instanceof
name|ResourceManager
condition|)
block|{
name|resourceManager
operator|=
operator|(
name|ResourceManager
operator|)
name|bean
expr_stmt|;
name|resourceManager
operator|.
name|addResourceResolver
argument_list|(
operator|new
name|BusApplicationContextResourceResolver
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ResourceManager
name|m
init|=
literal|null
decl_stmt|;
try|try
block|{
name|m
operator|=
operator|(
name|ResourceManager
operator|)
name|context
operator|.
name|getBean
argument_list|(
name|ResourceManager
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchBeanDefinitionException
name|t
parameter_list|)
block|{
comment|//ignore - no resource manager
block|}
if|if
condition|(
name|resourceManager
operator|==
literal|null
operator|&&
name|m
operator|!=
literal|null
condition|)
block|{
name|resourceManager
operator|=
name|m
expr_stmt|;
name|resourceManager
operator|.
name|addResourceResolver
argument_list|(
operator|new
name|BusApplicationContextResourceResolver
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|isProcessing
operator|=
name|temp
expr_stmt|;
block|}
return|return
name|resourceManager
return|;
block|}
specifier|public
name|Object
name|postProcessAfterInitialization
parameter_list|(
name|Object
name|bean
parameter_list|,
name|String
name|beanId
parameter_list|)
throws|throws
name|BeansException
block|{
if|if
condition|(
operator|!
name|isProcessing
condition|)
block|{
return|return
name|bean
return|;
block|}
if|if
condition|(
name|bean
operator|!=
literal|null
operator|&&
name|injectable
argument_list|(
name|bean
argument_list|,
name|beanId
argument_list|)
condition|)
block|{
operator|new
name|ResourceInjector
argument_list|(
name|getResourceManager
argument_list|(
name|bean
argument_list|)
argument_list|)
operator|.
name|construct
argument_list|(
name|bean
argument_list|)
expr_stmt|;
block|}
return|return
name|bean
return|;
block|}
specifier|public
name|Object
name|postProcessBeforeInitialization
parameter_list|(
name|Object
name|bean
parameter_list|,
name|String
name|beanId
parameter_list|)
throws|throws
name|BeansException
block|{
if|if
condition|(
operator|!
name|isProcessing
condition|)
block|{
return|return
name|bean
return|;
block|}
comment|/*         if (bean.getClass().getName().contains("Corb")) {             Thread.dumpStack();         }         */
if|if
condition|(
name|bean
operator|!=
literal|null
operator|&&
name|injectable
argument_list|(
name|bean
argument_list|,
name|beanId
argument_list|)
condition|)
block|{
operator|new
name|ResourceInjector
argument_list|(
name|getResourceManager
argument_list|(
name|bean
argument_list|)
argument_list|)
operator|.
name|inject
argument_list|(
name|bean
argument_list|)
expr_stmt|;
comment|/*             System.out.println("p :" + (++count) + ": " + bean.getClass().getName() + " " + beanId);         } else if (bean != null) {             System.out.println("np: " + (++count2)                                 + ": " + bean.getClass().getName() + " " + beanId);                                */
block|}
return|return
name|bean
return|;
block|}
specifier|public
name|void
name|postProcessBeforeDestruction
parameter_list|(
name|Object
name|bean
parameter_list|,
name|String
name|beanId
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isProcessing
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|bean
operator|!=
literal|null
operator|&&
name|injectable
argument_list|(
name|bean
argument_list|,
name|beanId
argument_list|)
condition|)
block|{
operator|new
name|ResourceInjector
argument_list|(
name|getResourceManager
argument_list|(
name|bean
argument_list|)
argument_list|)
operator|.
name|destroy
argument_list|(
name|bean
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

