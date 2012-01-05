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
name|List
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
name|helpers
operator|.
name|DOMUtils
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
name|WSPolicyFeature
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
name|PolicyFeatureBeanDefinitionParser
extends|extends
name|AbstractBeanDefinitionParser
block|{
annotation|@
name|Override
specifier|protected
name|void
name|parseChildElements
parameter_list|(
name|Element
name|e
parameter_list|,
name|ParserContext
name|ctx
parameter_list|,
name|BeanDefinitionBuilder
name|bean
parameter_list|)
block|{
name|List
argument_list|<
name|Element
argument_list|>
name|ps
init|=
operator|new
name|ArrayList
argument_list|<
name|Element
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Element
argument_list|>
name|prs
init|=
operator|new
name|ArrayList
argument_list|<
name|Element
argument_list|>
argument_list|()
decl_stmt|;
name|Element
name|elem
init|=
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|e
argument_list|)
decl_stmt|;
while|while
condition|(
name|elem
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
literal|"Policy"
operator|.
name|equals
argument_list|(
name|elem
operator|.
name|getLocalName
argument_list|()
argument_list|)
condition|)
block|{
name|ps
operator|.
name|add
argument_list|(
name|elem
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"PolicyReference"
operator|.
name|equals
argument_list|(
name|elem
operator|.
name|getLocalName
argument_list|()
argument_list|)
condition|)
block|{
name|prs
operator|.
name|add
argument_list|(
name|elem
argument_list|)
expr_stmt|;
block|}
name|elem
operator|=
name|DOMUtils
operator|.
name|getNextElement
argument_list|(
name|elem
argument_list|)
expr_stmt|;
block|}
name|bean
operator|.
name|addPropertyValue
argument_list|(
literal|"policyElements"
argument_list|,
name|ps
argument_list|)
expr_stmt|;
name|bean
operator|.
name|addPropertyValue
argument_list|(
literal|"policyReferenceElements"
argument_list|,
name|prs
argument_list|)
expr_stmt|;
name|super
operator|.
name|parseChildElements
argument_list|(
name|e
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
name|WSPolicyFeature
operator|.
name|class
return|;
block|}
block|}
end_class

end_unit

