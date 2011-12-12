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
name|blueprint
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|Unmarshaller
import|;
end_import

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
name|aries
operator|.
name|blueprint
operator|.
name|ParserContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|aries
operator|.
name|blueprint
operator|.
name|mutable
operator|.
name|MutableBeanMetadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|aries
operator|.
name|blueprint
operator|.
name|mutable
operator|.
name|MutablePassThroughMetadata
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
name|logging
operator|.
name|LogUtils
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
name|util
operator|.
name|PackageUtils
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
name|util
operator|.
name|StringUtils
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
name|blueprint
operator|.
name|AbstractBPBeanDefinitionParser
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
name|rm
operator|.
name|RMManager
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
name|manager
operator|.
name|DeliveryAssuranceType
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
name|manager
operator|.
name|DestinationPolicyType
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
name|manager
operator|.
name|ObjectFactory
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
name|manager
operator|.
name|SourcePolicyType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|reflect
operator|.
name|Metadata
import|;
end_import

begin_comment
comment|/**  * This class provides some common functions used by the two BP bean definition parsers  * in this package.   *   */
end_comment

begin_class
specifier|public
class|class
name|RMBPBeanDefinitionParser
extends|extends
name|AbstractBPBeanDefinitionParser
block|{
specifier|protected
specifier|static
specifier|final
name|String
name|RM_NS
init|=
literal|"http://cxf.apache.org/ws/rm/manager"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|RMBPBeanDefinitionParser
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|JAXBContext
name|jaxbContext
decl_stmt|;
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|beanClass
decl_stmt|;
specifier|public
name|RMBPBeanDefinitionParser
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|beanClass
parameter_list|)
block|{
name|this
operator|.
name|beanClass
operator|=
name|beanClass
expr_stmt|;
block|}
specifier|protected
name|Metadata
name|parse
parameter_list|(
name|Element
name|element
parameter_list|,
name|ParserContext
name|context
parameter_list|)
block|{
name|MutableBeanMetadata
name|bean
init|=
name|context
operator|.
name|createMetadata
argument_list|(
name|MutableBeanMetadata
operator|.
name|class
argument_list|)
decl_stmt|;
name|bean
operator|.
name|setRuntimeClass
argument_list|(
name|beanClass
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
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|bus
argument_list|)
condition|)
block|{
name|bus
operator|=
literal|"cxf"
expr_stmt|;
block|}
name|mapElementToJaxbProperty
argument_list|(
name|context
argument_list|,
name|bean
argument_list|,
name|element
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
argument_list|,
name|DeliveryAssuranceType
operator|.
name|class
argument_list|)
expr_stmt|;
name|mapElementToJaxbProperty
argument_list|(
name|context
argument_list|,
name|bean
argument_list|,
name|element
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
argument_list|,
name|SourcePolicyType
operator|.
name|class
argument_list|)
expr_stmt|;
name|mapElementToJaxbProperty
argument_list|(
name|context
argument_list|,
name|bean
argument_list|,
name|element
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
argument_list|,
name|DestinationPolicyType
operator|.
name|class
argument_list|)
expr_stmt|;
name|mapElementToJaxbProperty
argument_list|(
name|context
argument_list|,
name|bean
argument_list|,
name|element
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
name|context
argument_list|,
name|bean
argument_list|,
name|element
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
name|parseAttributes
argument_list|(
name|element
argument_list|,
name|context
argument_list|,
name|bean
argument_list|)
expr_stmt|;
name|parseChildElements
argument_list|(
name|element
argument_list|,
name|context
argument_list|,
name|bean
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setId
argument_list|(
name|beanClass
operator|.
name|getName
argument_list|()
operator|+
name|context
operator|.
name|generateId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|beanClass
operator|.
name|equals
argument_list|(
name|RMManager
operator|.
name|class
argument_list|)
condition|)
block|{
name|bean
operator|.
name|addProperty
argument_list|(
literal|"bus"
argument_list|,
name|getBusRef
argument_list|(
name|context
argument_list|,
name|bus
argument_list|)
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setDestroyMethod
argument_list|(
literal|"shutdown"
argument_list|)
expr_stmt|;
block|}
return|return
name|bean
return|;
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
name|MutableBeanMetadata
name|bean
parameter_list|,
name|Element
name|el
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
name|el
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
name|addProperty
argument_list|(
literal|"addressingNamespace"
argument_list|,
name|createValue
argument_list|(
name|ctx
argument_list|,
name|el
operator|.
name|getTextContent
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|mapElementToJaxbProperty
parameter_list|(
name|ParserContext
name|ctx
parameter_list|,
name|MutableBeanMetadata
name|bean
parameter_list|,
name|Element
name|parent
parameter_list|,
name|QName
name|name
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|c
parameter_list|)
block|{
name|Element
name|data
init|=
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|parent
argument_list|)
decl_stmt|;
try|try
block|{
name|Unmarshaller
name|unmarshaller
init|=
name|getJAXBContext
argument_list|()
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
name|MutablePassThroughMetadata
name|value
init|=
name|ctx
operator|.
name|createMetadata
argument_list|(
name|MutablePassThroughMetadata
operator|.
name|class
argument_list|)
decl_stmt|;
name|value
operator|.
name|setObject
argument_list|(
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
name|data
argument_list|,
name|c
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|bean
operator|.
name|addProperty
argument_list|(
name|propertyName
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Unable to parse property "
operator|+
name|propertyName
operator|+
literal|" due to "
operator|+
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|synchronized
name|JAXBContext
name|getJAXBContext
parameter_list|()
throws|throws
name|JAXBException
block|{
if|if
condition|(
name|jaxbContext
operator|==
literal|null
condition|)
block|{
name|jaxbContext
operator|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|PackageUtils
operator|.
name|getPackageName
argument_list|(
name|ObjectFactory
operator|.
name|class
argument_list|)
argument_list|,
name|ObjectFactory
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|jaxbContext
return|;
block|}
block|}
end_class

end_unit

