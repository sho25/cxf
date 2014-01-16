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
name|jaxrs
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
name|Collections
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
name|bus
operator|.
name|spring
operator|.
name|SpringBus
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
name|feature
operator|.
name|Feature
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
name|interceptor
operator|.
name|Interceptor
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
name|jaxrs
operator|.
name|JAXRSServerFactoryBean
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
name|message
operator|.
name|Message
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
name|annotation
operator|.
name|Import
import|;
end_import

begin_class
annotation|@
name|Import
argument_list|(
name|JaxRsConfig
operator|.
name|class
argument_list|)
specifier|public
specifier|abstract
class|class
name|AbstractSpringConfigurationFactory
implements|implements
name|ApplicationContextAware
block|{
specifier|protected
name|ApplicationContext
name|applicationContext
decl_stmt|;
specifier|protected
name|Server
name|createJaxRsServer
parameter_list|()
block|{
name|JAXRSServerFactoryBean
name|factory
init|=
operator|new
name|JAXRSServerFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setTransportId
argument_list|(
name|getTransportId
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setBus
argument_list|(
name|applicationContext
operator|.
name|getBean
argument_list|(
name|SpringBus
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|setRootResources
argument_list|(
name|factory
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setProviders
argument_list|(
name|getJaxrsProviders
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setInInterceptors
argument_list|(
name|getInInterceptors
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setOutInterceptors
argument_list|(
name|getOutInterceptors
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setOutFaultInterceptors
argument_list|(
name|getOutFaultInterceptors
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setFeatures
argument_list|(
name|getFeatures
argument_list|()
argument_list|)
expr_stmt|;
name|finalizeFactorySetup
argument_list|(
name|factory
argument_list|)
expr_stmt|;
return|return
name|factory
operator|.
name|create
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setApplicationContext
parameter_list|(
name|ApplicationContext
name|ac
parameter_list|)
throws|throws
name|BeansException
block|{
name|applicationContext
operator|=
name|ac
expr_stmt|;
block|}
specifier|protected
specifier|abstract
name|void
name|setRootResources
parameter_list|(
name|JAXRSServerFactoryBean
name|factory
parameter_list|)
function_decl|;
specifier|protected
name|List
argument_list|<
name|Object
argument_list|>
name|getJaxrsProviders
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
specifier|protected
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|getInInterceptors
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
specifier|protected
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|getOutInterceptors
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
specifier|protected
name|List
argument_list|<
name|Feature
argument_list|>
name|getFeatures
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
specifier|protected
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|getOutFaultInterceptors
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
specifier|protected
name|String
name|getAddress
parameter_list|()
block|{
return|return
literal|"/"
return|;
block|}
specifier|protected
name|String
name|getTransportId
parameter_list|()
block|{
return|return
literal|"http://cxf.apache.org/transports/http"
return|;
block|}
specifier|protected
name|void
name|finalizeFactorySetup
parameter_list|(
name|JAXRSServerFactoryBean
name|factory
parameter_list|)
block|{
comment|// complete
block|}
block|}
end_class

end_unit

