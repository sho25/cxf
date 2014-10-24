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
name|configuration
operator|.
name|spring
operator|.
name|StringBeanDefinitionParser
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
name|Server
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
name|ServerFactoryBeanDefinitionParser
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
name|JaxWsServerFactoryBean
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
name|support
operator|.
name|JaxWsServiceFactoryBean
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
name|xml
operator|.
name|NamespaceHandlerSupport
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
name|NamespaceHandler
extends|extends
name|NamespaceHandlerSupport
block|{
specifier|public
name|void
name|init
parameter_list|()
block|{
name|registerBeanDefinitionParser
argument_list|(
literal|"client"
argument_list|,
operator|new
name|JaxWsProxyFactoryBeanDefinitionParser
argument_list|()
argument_list|)
expr_stmt|;
name|registerBeanDefinitionParser
argument_list|(
literal|"endpoint"
argument_list|,
operator|new
name|EndpointDefinitionParser
argument_list|()
argument_list|)
expr_stmt|;
name|registerBeanDefinitionParser
argument_list|(
literal|"schemaLocation"
argument_list|,
operator|new
name|StringBeanDefinitionParser
argument_list|()
argument_list|)
expr_stmt|;
name|ServerFactoryBeanDefinitionParser
name|parser
init|=
operator|new
name|ServerFactoryBeanDefinitionParser
argument_list|()
decl_stmt|;
name|parser
operator|.
name|setBeanClass
argument_list|(
name|SpringServerFactoryBean
operator|.
name|class
argument_list|)
expr_stmt|;
name|registerBeanDefinitionParser
argument_list|(
literal|"server"
argument_list|,
name|parser
argument_list|)
expr_stmt|;
block|}
annotation|@
name|NoJSR250Annotations
specifier|public
specifier|static
class|class
name|SpringServerFactoryBean
extends|extends
name|JaxWsServerFactoryBean
implements|implements
name|ApplicationContextAware
block|{
name|Server
name|server
decl_stmt|;
specifier|public
name|SpringServerFactoryBean
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|public
name|SpringServerFactoryBean
parameter_list|(
name|JaxWsServiceFactoryBean
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
name|Server
name|getServer
parameter_list|()
block|{
return|return
name|server
return|;
block|}
specifier|public
name|Server
name|create
parameter_list|()
block|{
if|if
condition|(
name|server
operator|==
literal|null
condition|)
block|{
name|server
operator|=
name|super
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
return|return
name|server
return|;
block|}
specifier|public
name|void
name|destroy
parameter_list|()
block|{
if|if
condition|(
name|server
operator|!=
literal|null
condition|)
block|{
name|server
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|server
operator|=
literal|null
expr_stmt|;
block|}
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
name|bus
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
block|}
block|}
end_class

end_unit

