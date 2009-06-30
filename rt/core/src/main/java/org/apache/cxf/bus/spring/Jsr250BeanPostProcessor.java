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
name|helpers
operator|.
name|CastUtils
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
name|apache
operator|.
name|cxf
operator|.
name|resource
operator|.
name|ResourceResolver
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
name|List
argument_list|<
name|ResourceResolver
argument_list|>
name|resolvers
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
condition|)
block|{
operator|new
name|ResourceInjector
argument_list|(
name|resourceManager
argument_list|,
name|resolvers
argument_list|)
operator|.
name|construct
argument_list|(
name|bean
argument_list|)
expr_stmt|;
block|}
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|mp
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|context
operator|.
name|getBeansOfType
argument_list|(
name|ResourceResolver
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|ResourceResolver
argument_list|>
name|resolvs
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|mp
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
name|resolvers
operator|=
operator|new
name|ArrayList
argument_list|<
name|ResourceResolver
argument_list|>
argument_list|(
name|resourceManager
operator|.
name|getResourceResolvers
argument_list|()
argument_list|)
expr_stmt|;
name|resolvers
operator|.
name|addAll
argument_list|(
name|resolvs
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
if|if
condition|(
name|bean
operator|!=
literal|null
condition|)
block|{
operator|new
name|ResourceInjector
argument_list|(
name|resourceManager
argument_list|,
name|resolvers
argument_list|)
operator|.
name|inject
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
condition|)
block|{
operator|new
name|ResourceInjector
argument_list|(
name|resourceManager
argument_list|,
name|resolvers
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

