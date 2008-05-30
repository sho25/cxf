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
name|configuration
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
name|Attr
import|;
end_import

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
name|w3c
operator|.
name|dom
operator|.
name|NamedNodeMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|NodeList
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

begin_comment
comment|/**  * This class makes it easy to create two simultaneous beans - a factory bean and the bean  * that the factory produces.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractFactoryBeanDefinitionParser
extends|extends
name|AbstractBeanDefinitionParser
block|{
annotation|@
name|Override
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
name|BeanDefinitionBuilder
name|factoryBean
init|=
name|BeanDefinitionBuilder
operator|.
name|rootBeanDefinition
argument_list|(
name|getFactoryClass
argument_list|()
argument_list|)
decl_stmt|;
name|NamedNodeMap
name|atts
init|=
name|element
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
name|boolean
name|createdFromAPI
init|=
literal|false
decl_stmt|;
name|boolean
name|setBus
init|=
literal|false
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|atts
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Attr
name|node
init|=
operator|(
name|Attr
operator|)
name|atts
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|String
name|val
init|=
name|node
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|String
name|pre
init|=
name|node
operator|.
name|getPrefix
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|node
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"createdFromAPI"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|factoryBean
operator|.
name|setAbstract
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setAbstract
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|createdFromAPI
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"abstract"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|factoryBean
operator|.
name|setAbstract
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setAbstract
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
literal|"id"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|&&
operator|!
literal|"name"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|&&
name|isAttribute
argument_list|(
name|pre
argument_list|,
name|name
argument_list|)
condition|)
block|{
if|if
condition|(
literal|"bus"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|setBus
operator|=
literal|true
expr_stmt|;
block|}
name|mapAttribute
argument_list|(
name|factoryBean
argument_list|,
name|element
argument_list|,
name|name
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|setBus
condition|)
block|{
name|addBusWiringAttribute
argument_list|(
name|factoryBean
argument_list|,
name|BusWiringType
operator|.
name|PROPERTY
argument_list|)
expr_stmt|;
block|}
name|NodeList
name|children
init|=
name|element
operator|.
name|getChildNodes
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|children
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|n
init|=
name|children
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|n
operator|.
name|getNodeType
argument_list|()
operator|==
name|Node
operator|.
name|ELEMENT_NODE
condition|)
block|{
name|String
name|name
init|=
name|n
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
name|mapElement
argument_list|(
name|ctx
argument_list|,
name|factoryBean
argument_list|,
operator|(
name|Element
operator|)
name|n
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|id
init|=
name|getIdOrName
argument_list|(
name|element
argument_list|)
decl_stmt|;
if|if
condition|(
name|createdFromAPI
condition|)
block|{
name|id
operator|=
name|id
operator|+
name|getSuffix
argument_list|()
expr_stmt|;
block|}
name|String
name|factoryId
init|=
name|id
operator|+
name|getFactoryIdSuffix
argument_list|()
decl_stmt|;
name|ctx
operator|.
name|getRegistry
argument_list|()
operator|.
name|registerBeanDefinition
argument_list|(
name|factoryId
argument_list|,
name|factoryBean
operator|.
name|getBeanDefinition
argument_list|()
argument_list|)
expr_stmt|;
name|bean
operator|.
name|getBeanDefinition
argument_list|()
operator|.
name|setAttribute
argument_list|(
literal|"id"
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setFactoryBean
argument_list|(
name|factoryId
argument_list|,
literal|"create"
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|abstract
name|Class
name|getFactoryClass
parameter_list|()
function_decl|;
comment|/**      * @return The Spring ID of the factory bean.      */
specifier|protected
specifier|abstract
name|String
name|getFactoryIdSuffix
parameter_list|()
function_decl|;
block|}
end_class

end_unit

