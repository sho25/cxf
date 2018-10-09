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
name|spring
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|openapi
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
name|jaxrs
operator|.
name|openapi
operator|.
name|OpenApiCustomizer
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
name|openapi
operator|.
name|OpenApiFeature
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
name|BeanFactory
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
name|BeanFactoryAware
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
name|BeanPostProcessor
import|;
end_import

begin_comment
comment|/**  * Post-processes all OpenApiFeature beans in order to inject the instance of  * OpenApiCustomizer (if it is not set yet). For Spring / Spring Boot applications  * it is what you would need to do manually most of the time.  */
end_comment

begin_class
specifier|public
class|class
name|OpenApiFeatureBeanPostProcessor
implements|implements
name|BeanPostProcessor
implements|,
name|BeanFactoryAware
block|{
specifier|private
name|BeanFactory
name|beanFactory
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Object
name|postProcessAfterInitialization
parameter_list|(
name|Object
name|bean
parameter_list|,
name|String
name|beanName
parameter_list|)
throws|throws
name|BeansException
block|{
if|if
condition|(
name|bean
operator|instanceof
name|OpenApiFeature
condition|)
block|{
specifier|final
name|OpenApiFeature
name|feature
init|=
operator|(
name|OpenApiFeature
operator|)
name|bean
decl_stmt|;
if|if
condition|(
name|feature
operator|.
name|getCustomizer
argument_list|()
operator|==
literal|null
condition|)
block|{
specifier|final
name|OpenApiCustomizer
name|customizer
init|=
name|beanFactory
operator|.
name|getBean
argument_list|(
name|OpenApiCustomizer
operator|.
name|class
argument_list|)
decl_stmt|;
name|feature
operator|.
name|setCustomizer
argument_list|(
name|customizer
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|bean
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setBeanFactory
parameter_list|(
name|BeanFactory
name|beanFactory
parameter_list|)
throws|throws
name|BeansException
block|{
name|this
operator|.
name|beanFactory
operator|=
name|beanFactory
expr_stmt|;
block|}
block|}
end_class

end_unit

