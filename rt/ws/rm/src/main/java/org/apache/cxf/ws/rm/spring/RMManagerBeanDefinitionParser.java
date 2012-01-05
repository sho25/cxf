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
name|rm
operator|.
name|spring
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|rm
operator|.
name|RMManager
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
name|RMManagerBeanDefinitionParser
extends|extends
name|AbstractBeanDefinitionParser
block|{
specifier|private
specifier|static
specifier|final
name|String
name|RM_NS
init|=
literal|"http://cxf.apache.org/ws/rm/manager"
decl_stmt|;
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
name|mapElementToJaxbProperty
argument_list|(
name|element
argument_list|,
name|bean
argument_list|,
operator|new
name|QName
argument_list|(
name|RM_NS
argument_list|,
literal|"deliveryAssurance"
argument_list|)
argument_list|,
literal|"deliveryAssurance"
argument_list|)
expr_stmt|;
name|mapElementToJaxbProperty
argument_list|(
name|element
argument_list|,
name|bean
argument_list|,
operator|new
name|QName
argument_list|(
name|RM_NS
argument_list|,
literal|"sourcePolicy"
argument_list|)
argument_list|,
literal|"sourcePolicy"
argument_list|)
expr_stmt|;
name|mapElementToJaxbProperty
argument_list|(
name|element
argument_list|,
name|bean
argument_list|,
operator|new
name|QName
argument_list|(
name|RM_NS
argument_list|,
literal|"destinationPolicy"
argument_list|)
argument_list|,
literal|"destinationPolicy"
argument_list|)
expr_stmt|;
name|mapElementToJaxbProperty
argument_list|(
name|element
argument_list|,
name|bean
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://schemas.xmlsoap.org/ws/2005/02/rm/policy"
argument_list|,
literal|"RMAssertion"
argument_list|)
argument_list|,
literal|"RMAssertion"
argument_list|,
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|rmp
operator|.
name|v200502
operator|.
name|RMAssertion
operator|.
name|class
argument_list|)
expr_stmt|;
name|mapElementToJaxbProperty
argument_list|(
name|element
argument_list|,
name|bean
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://docs.oasis-open.org/ws-rx/wsrmp/200702"
argument_list|,
literal|"RMAssertion"
argument_list|)
argument_list|,
literal|"RMAssertion"
argument_list|,
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|rmp
operator|.
name|v200702
operator|.
name|RMAssertion
operator|.
name|class
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|getDelegate
argument_list|()
operator|.
name|parsePropertyElements
argument_list|(
name|element
argument_list|,
name|bean
operator|.
name|getBeanDefinition
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|bus
init|=
name|element
operator|.
name|getAttribute
argument_list|(
literal|"bus"
argument_list|)
decl_stmt|;
if|if
condition|(
name|bus
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|bus
argument_list|)
condition|)
block|{
name|addBusWiringAttribute
argument_list|(
name|bean
argument_list|,
name|BusWiringType
operator|.
name|PROPERTY
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|bean
operator|.
name|addPropertyReference
argument_list|(
literal|"bus"
argument_list|,
name|bus
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|parseChildElements
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
literal|"store"
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
elseif|else
if|if
condition|(
literal|"addressingNamespace"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|bean
operator|.
name|addPropertyValue
argument_list|(
literal|"addressingNamespace"
argument_list|,
name|e
operator|.
name|getTextContent
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|hasBusProperty
parameter_list|()
block|{
return|return
literal|true
return|;
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
name|element
parameter_list|)
block|{
return|return
name|RMManager
operator|.
name|class
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|getJaxbPackage
parameter_list|()
block|{
return|return
literal|"org.apache.cxf.ws.rm.manager"
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|shouldGenerateIdAsFallback
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

