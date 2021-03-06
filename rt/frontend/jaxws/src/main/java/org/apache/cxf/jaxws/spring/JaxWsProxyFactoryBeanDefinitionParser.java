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
name|jaxws
operator|.
name|spring
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
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
name|spring
operator|.
name|BusWiringBeanFactoryPostProcessor
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
name|endpoint
operator|.
name|Client
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
name|frontend
operator|.
name|ClientFactoryBean
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
name|frontend
operator|.
name|ClientProxy
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
name|frontend
operator|.
name|spring
operator|.
name|ClientProxyFactoryBeanDefinitionParser
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
name|jaxws
operator|.
name|JaxWsProxyFactoryBean
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
name|DisposableBean
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
name|FactoryBean
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
name|JaxWsProxyFactoryBeanDefinitionParser
extends|extends
name|ClientProxyFactoryBeanDefinitionParser
block|{
specifier|public
name|JaxWsProxyFactoryBeanDefinitionParser
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|setBeanClass
argument_list|(
name|JAXWSSpringClientProxyFactoryBean
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|getRawFactoryClass
parameter_list|()
block|{
return|return
name|JaxWsProxyFactoryBean
operator|.
name|class
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|getFactoryClass
parameter_list|()
block|{
return|return
name|JAXWSSpringClientProxyFactoryBean
operator|.
name|class
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|getSuffix
parameter_list|()
block|{
return|return
literal|".jaxws-client"
return|;
block|}
annotation|@
name|NoJSR250Annotations
specifier|public
specifier|static
class|class
name|JAXWSSpringClientProxyFactoryBean
extends|extends
name|JaxWsProxyFactoryBean
implements|implements
name|ApplicationContextAware
implements|,
name|FactoryBean
argument_list|<
name|Object
argument_list|>
implements|,
name|DisposableBean
block|{
specifier|private
name|Object
name|obj
decl_stmt|;
specifier|public
name|JAXWSSpringClientProxyFactoryBean
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|public
name|JAXWSSpringClientProxyFactoryBean
parameter_list|(
name|ClientFactoryBean
name|fact
parameter_list|)
block|{
name|super
argument_list|(
name|fact
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setApplicationContext
parameter_list|(
name|ApplicationContext
name|ctx
parameter_list|)
throws|throws
name|BeansException
block|{
if|if
condition|(
name|getBus
argument_list|()
operator|==
literal|null
condition|)
block|{
name|setBus
argument_list|(
name|BusWiringBeanFactoryPostProcessor
operator|.
name|addDefaultBus
argument_list|(
name|ctx
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Object
name|create
parameter_list|()
block|{
name|configured
operator|=
literal|true
expr_stmt|;
return|return
name|super
operator|.
name|create
argument_list|()
return|;
block|}
specifier|public
specifier|synchronized
name|Object
name|getObject
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
name|obj
operator|=
name|create
argument_list|()
expr_stmt|;
block|}
return|return
name|obj
return|;
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getObjectType
parameter_list|()
block|{
return|return
name|this
operator|.
name|getServiceClass
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|void
name|destroy
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|obj
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|Closeable
condition|)
block|{
operator|(
operator|(
name|Closeable
operator|)
name|obj
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|Client
name|c
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|obj
argument_list|)
decl_stmt|;
name|c
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
name|obj
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

