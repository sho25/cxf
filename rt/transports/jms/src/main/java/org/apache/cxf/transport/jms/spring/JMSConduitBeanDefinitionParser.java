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
name|transport
operator|.
name|jms
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
name|transport
operator|.
name|jms
operator|.
name|AddressType
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
name|transport
operator|.
name|jms
operator|.
name|ClientBehaviorPolicyType
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
name|transport
operator|.
name|jms
operator|.
name|ClientConfig
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
name|transport
operator|.
name|jms
operator|.
name|JMSConduit
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
name|transport
operator|.
name|jms
operator|.
name|SessionPoolType
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
name|JMSConduitBeanDefinitionParser
extends|extends
name|AbstractBeanDefinitionParser
block|{
specifier|private
specifier|static
specifier|final
name|String
name|JMS_NS
init|=
literal|"http://cxf.apache.org/transports/jms"
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
name|bean
operator|.
name|setAbstract
argument_list|(
literal|true
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
name|JMS_NS
argument_list|,
literal|"clientConfig"
argument_list|)
argument_list|,
literal|"clientConfig"
argument_list|,
name|ClientConfig
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
name|JMS_NS
argument_list|,
literal|"runtimePolicy"
argument_list|)
argument_list|,
literal|"runtimePolicy"
argument_list|,
name|ClientBehaviorPolicyType
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
name|JMS_NS
argument_list|,
literal|"address"
argument_list|)
argument_list|,
literal|"address"
argument_list|,
name|AddressType
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
name|JMS_NS
argument_list|,
literal|"sessionPool"
argument_list|)
argument_list|,
literal|"sessionPool"
argument_list|,
name|SessionPoolType
operator|.
name|class
argument_list|)
expr_stmt|;
name|NodeList
name|el
init|=
name|element
operator|.
name|getElementsByTagNameNS
argument_list|(
name|JMS_NS
argument_list|,
literal|"jmsConfig-ref"
argument_list|)
decl_stmt|;
if|if
condition|(
name|el
operator|.
name|getLength
argument_list|()
operator|==
literal|1
condition|)
block|{
name|Node
name|el1
init|=
name|el
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|bean
operator|.
name|addPropertyReference
argument_list|(
literal|"jmsConfig"
argument_list|,
name|el1
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
name|Class
name|getBeanClass
parameter_list|(
name|Element
name|arg0
parameter_list|)
block|{
return|return
name|JMSConduit
operator|.
name|class
return|;
block|}
block|}
end_class

end_unit

