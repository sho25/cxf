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
name|ws
operator|.
name|policy
operator|.
name|spring
package|;
end_package

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|configuration
operator|.
name|spring
operator|.
name|AbstractBeanDefinitionParser
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
name|BusWiringType
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
name|ws
operator|.
name|policy
operator|.
name|AlternativeSelector
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
name|ws
operator|.
name|policy
operator|.
name|PolicyEngine
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
name|ws
operator|.
name|policy
operator|.
name|PolicyEngineImpl
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
name|BeanDefinitionStoreException
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
name|AbstractBeanDefinition
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
name|BeanDefinitionBuilder
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
name|ParserContext
import|;
end_import

begin_class
specifier|public
class|class
name|PolicyEngineBeanDefinitionParser
extends|extends
name|AbstractBeanDefinitionParser
block|{
specifier|protected
name|void
name|doParse
parameter_list|(
name|Element
name|element
parameter_list|,
name|ParserContext
name|ctx
parameter_list|,
name|BeanDefinitionBuilder
name|bean
parameter_list|)
block|{
name|super
operator|.
name|addBusWiringAttribute
argument_list|(
name|bean
argument_list|,
name|BusWiringType
operator|.
name|CONSTRUCTOR
argument_list|)
expr_stmt|;
name|super
operator|.
name|doParse
argument_list|(
name|element
argument_list|,
name|ctx
argument_list|,
name|bean
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|mapElement
parameter_list|(
name|ParserContext
name|ctx
parameter_list|,
name|BeanDefinitionBuilder
name|bean
parameter_list|,
name|Element
name|e
parameter_list|,
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
literal|"alternativeSelector"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|setFirstChildAsProperty
argument_list|(
name|e
argument_list|,
name|ctx
argument_list|,
name|bean
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|getBeanClass
parameter_list|(
name|Element
name|el
parameter_list|)
block|{
return|return
name|PolicyEngineConfig
operator|.
name|class
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|resolveId
parameter_list|(
name|Element
name|e
parameter_list|,
name|AbstractBeanDefinition
name|abd
parameter_list|,
name|ParserContext
name|ctx
parameter_list|)
throws|throws
name|BeanDefinitionStoreException
block|{
return|return
name|PolicyEngineConfig
operator|.
name|class
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|public
specifier|static
class|class
name|PolicyEngineConfig
block|{
specifier|private
name|PolicyEngineImpl
name|engine
decl_stmt|;
specifier|public
name|PolicyEngineConfig
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|engine
operator|=
operator|(
name|PolicyEngineImpl
operator|)
name|bus
operator|.
name|getExtension
argument_list|(
name|PolicyEngine
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|getEnabled
parameter_list|()
block|{
return|return
name|engine
operator|.
name|isEnabled
argument_list|()
return|;
block|}
specifier|public
name|void
name|setEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
block|{
name|engine
operator|.
name|setEnabled
argument_list|(
name|enabled
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|getIgnoreUnknownAssertions
parameter_list|()
block|{
return|return
name|engine
operator|.
name|isIgnoreUnknownAssertions
argument_list|()
return|;
block|}
specifier|public
name|void
name|setIgnoreUnknownAssertions
parameter_list|(
name|boolean
name|ignoreUnknownAssertions
parameter_list|)
block|{
name|engine
operator|.
name|setIgnoreUnknownAssertions
argument_list|(
name|ignoreUnknownAssertions
argument_list|)
expr_stmt|;
block|}
specifier|public
name|AlternativeSelector
name|getAlternativeSelector
parameter_list|()
block|{
return|return
name|engine
operator|.
name|getAlternativeSelector
argument_list|()
return|;
block|}
specifier|public
name|void
name|setAlternativeSelector
parameter_list|(
name|AlternativeSelector
name|alternativeSelector
parameter_list|)
block|{
name|engine
operator|.
name|setAlternativeSelector
argument_list|(
name|alternativeSelector
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

