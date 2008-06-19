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
name|java
operator|.
name|io
operator|.
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|StringTokenizer
import|;
end_import

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
name|JAXBElement
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
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamWriter
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
name|CacheMap
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
name|staxutils
operator|.
name|StaxUtils
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
name|config
operator|.
name|BeanDefinition
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
name|BeanDefinitionHolder
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
name|BeanDefinitionParserDelegate
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|util
operator|.
name|StringUtils
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractBeanDefinitionParser
extends|extends
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
name|AbstractSingleBeanDefinitionParser
block|{
specifier|public
specifier|static
specifier|final
name|String
name|WIRE_BUS_ATTRIBUTE
init|=
name|AbstractBeanDefinitionParser
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".wireBus"
decl_stmt|;
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|JAXBContext
argument_list|>
name|packageContextCache
init|=
operator|new
name|CacheMap
argument_list|<
name|String
argument_list|,
name|JAXBContext
argument_list|>
argument_list|()
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
name|AbstractBeanDefinitionParser
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Class
name|beanClass
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
name|boolean
name|setBus
init|=
name|parseAttributes
argument_list|(
name|element
argument_list|,
name|ctx
argument_list|,
name|bean
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|setBus
operator|&&
name|hasBusProperty
argument_list|()
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
specifier|protected
name|boolean
name|parseAttributes
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
name|NamedNodeMap
name|atts
init|=
name|element
operator|.
name|getAttributes
argument_list|()
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
name|String
name|prefix
init|=
name|node
operator|.
name|getPrefix
argument_list|()
decl_stmt|;
comment|// Don't process namespaces
if|if
condition|(
name|isNamespace
argument_list|(
name|name
argument_list|,
name|prefix
argument_list|)
condition|)
block|{
continue|continue;
block|}
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
literal|"abstract"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
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
literal|"depends-on"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|bean
operator|.
name|addDependsOn
argument_list|(
name|val
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
if|if
condition|(
name|val
operator|!=
literal|null
operator|&&
name|val
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
operator|&&
name|ctx
operator|.
name|getRegistry
argument_list|()
operator|.
name|containsBeanDefinition
argument_list|(
name|val
argument_list|)
condition|)
block|{
name|bean
operator|.
name|addPropertyReference
argument_list|(
name|name
argument_list|,
name|val
argument_list|)
expr_stmt|;
name|setBus
operator|=
literal|true
expr_stmt|;
block|}
block|}
else|else
block|{
name|mapAttribute
argument_list|(
name|bean
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
block|}
return|return
name|setBus
return|;
block|}
specifier|private
name|boolean
name|isNamespace
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|prefix
parameter_list|)
block|{
return|return
literal|"xmlns"
operator|.
name|equals
argument_list|(
name|prefix
argument_list|)
operator|||
name|prefix
operator|==
literal|null
operator|&&
literal|"xmlns"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|protected
name|void
name|parseChildElements
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
name|bean
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
block|}
specifier|public
name|Class
name|getBeanClass
parameter_list|()
block|{
return|return
name|beanClass
return|;
block|}
specifier|public
name|void
name|setBeanClass
parameter_list|(
name|Class
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
annotation|@
name|Override
specifier|protected
name|Class
name|getBeanClass
parameter_list|(
name|Element
name|e
parameter_list|)
block|{
return|return
name|beanClass
return|;
block|}
specifier|protected
name|void
name|mapAttribute
parameter_list|(
name|BeanDefinitionBuilder
name|bean
parameter_list|,
name|Element
name|e
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|val
parameter_list|)
block|{
name|mapAttribute
argument_list|(
name|bean
argument_list|,
name|name
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|mapAttribute
parameter_list|(
name|BeanDefinitionBuilder
name|bean
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|val
parameter_list|)
block|{
name|mapToProperty
argument_list|(
name|bean
argument_list|,
name|name
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
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
block|{     }
annotation|@
name|Override
specifier|protected
name|String
name|resolveId
parameter_list|(
name|Element
name|elem
parameter_list|,
name|AbstractBeanDefinition
name|definition
parameter_list|,
name|ParserContext
name|ctx
parameter_list|)
throws|throws
name|BeanDefinitionStoreException
block|{
comment|// REVISIT: use getAttributeNS instead
name|String
name|id
init|=
name|getIdOrName
argument_list|(
name|elem
argument_list|)
decl_stmt|;
name|String
name|createdFromAPI
init|=
name|elem
operator|.
name|getAttribute
argument_list|(
name|BeanConstants
operator|.
name|CREATED_FROM_API_ATTR
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|id
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|id
argument_list|)
condition|)
block|{
return|return
name|super
operator|.
name|resolveId
argument_list|(
name|elem
argument_list|,
name|definition
argument_list|,
name|ctx
argument_list|)
return|;
block|}
if|if
condition|(
name|createdFromAPI
operator|!=
literal|null
operator|&&
literal|"true"
operator|.
name|equals
argument_list|(
name|createdFromAPI
operator|.
name|toLowerCase
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|id
operator|+
name|getSuffix
argument_list|()
return|;
block|}
return|return
name|id
return|;
block|}
specifier|protected
name|boolean
name|hasBusProperty
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|protected
name|String
name|getSuffix
parameter_list|()
block|{
return|return
literal|""
return|;
block|}
specifier|protected
name|void
name|setFirstChildAsProperty
parameter_list|(
name|Element
name|element
parameter_list|,
name|ParserContext
name|ctx
parameter_list|,
name|BeanDefinitionBuilder
name|bean
parameter_list|,
name|String
name|propertyName
parameter_list|)
block|{
name|Element
name|first
init|=
name|getFirstChild
argument_list|(
name|element
argument_list|)
decl_stmt|;
if|if
condition|(
name|first
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|propertyName
operator|+
literal|" property must have child elements!"
argument_list|)
throw|;
block|}
comment|// Seems odd that we have to do the registration, I wonder if there is a better way
name|String
name|id
decl_stmt|;
name|BeanDefinition
name|child
decl_stmt|;
if|if
condition|(
name|first
operator|.
name|getNamespaceURI
argument_list|()
operator|.
name|equals
argument_list|(
name|BeanDefinitionParserDelegate
operator|.
name|BEANS_NAMESPACE_URI
argument_list|)
condition|)
block|{
name|String
name|name
init|=
name|first
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"ref"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|id
operator|=
name|first
operator|.
name|getAttribute
argument_list|(
literal|"bean"
argument_list|)
expr_stmt|;
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"<ref> elements must have a \"bean\" attribute!"
argument_list|)
throw|;
block|}
name|bean
operator|.
name|addPropertyReference
argument_list|(
name|propertyName
argument_list|,
name|id
argument_list|)
expr_stmt|;
return|return;
block|}
elseif|else
if|if
condition|(
literal|"bean"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|BeanDefinitionHolder
name|bdh
init|=
name|ctx
operator|.
name|getDelegate
argument_list|()
operator|.
name|parseBeanDefinitionElement
argument_list|(
name|first
argument_list|)
decl_stmt|;
name|child
operator|=
name|bdh
operator|.
name|getBeanDefinition
argument_list|()
expr_stmt|;
name|bean
operator|.
name|addPropertyValue
argument_list|(
name|propertyName
argument_list|,
name|child
argument_list|)
expr_stmt|;
return|return;
block|}
else|else
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Elements with the name "
operator|+
name|name
operator|+
literal|" are not currently "
operator|+
literal|"supported as sub elements of "
operator|+
name|element
operator|.
name|getLocalName
argument_list|()
argument_list|)
throw|;
block|}
block|}
name|child
operator|=
name|ctx
operator|.
name|getDelegate
argument_list|()
operator|.
name|parseCustomElement
argument_list|(
name|first
argument_list|,
name|bean
operator|.
name|getBeanDefinition
argument_list|()
argument_list|)
expr_stmt|;
name|bean
operator|.
name|addPropertyValue
argument_list|(
name|propertyName
argument_list|,
name|child
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Element
name|getFirstChild
parameter_list|(
name|Element
name|element
parameter_list|)
block|{
name|Element
name|first
init|=
literal|null
decl_stmt|;
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
name|first
operator|=
operator|(
name|Element
operator|)
name|n
expr_stmt|;
block|}
block|}
return|return
name|first
return|;
block|}
specifier|protected
name|void
name|addBusWiringAttribute
parameter_list|(
name|BeanDefinitionBuilder
name|bean
parameter_list|,
name|BusWiringType
name|type
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Adding "
operator|+
name|WIRE_BUS_ATTRIBUTE
operator|+
literal|" attribute "
operator|+
name|type
operator|+
literal|" to bean "
operator|+
name|bean
argument_list|)
expr_stmt|;
name|bean
operator|.
name|getRawBeanDefinition
argument_list|()
operator|.
name|setAttribute
argument_list|(
name|WIRE_BUS_ATTRIBUTE
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|mapElementToJaxbProperty
parameter_list|(
name|Element
name|parent
parameter_list|,
name|BeanDefinitionBuilder
name|bean
parameter_list|,
name|QName
name|name
parameter_list|,
name|String
name|propertyName
parameter_list|)
block|{
name|mapElementToJaxbProperty
argument_list|(
name|parent
argument_list|,
name|bean
argument_list|,
name|name
argument_list|,
name|propertyName
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|mapElementToJaxbProperty
parameter_list|(
name|Element
name|parent
parameter_list|,
name|BeanDefinitionBuilder
name|bean
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
literal|null
decl_stmt|;
name|NodeList
name|nl
init|=
name|parent
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
name|nl
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
name|nl
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
operator|&&
name|name
operator|.
name|getLocalPart
argument_list|()
operator|.
name|equals
argument_list|(
name|n
operator|.
name|getLocalName
argument_list|()
argument_list|)
operator|&&
name|name
operator|.
name|getNamespaceURI
argument_list|()
operator|.
name|equals
argument_list|(
name|n
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
name|data
operator|=
operator|(
name|Element
operator|)
name|n
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|data
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|JAXBContext
name|context
init|=
literal|null
decl_stmt|;
try|try
block|{
name|String
name|pkg
init|=
name|getJaxbPackage
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|c
operator|&&
name|c
operator|.
name|getPackage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|pkg
operator|=
name|c
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
name|context
operator|=
name|packageContextCache
operator|.
name|get
argument_list|(
name|pkg
argument_list|)
expr_stmt|;
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
name|context
operator|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|pkg
argument_list|,
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
name|packageContextCache
operator|.
name|put
argument_list|(
name|pkg
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|StringWriter
name|writer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|XMLStreamWriter
name|xmlWriter
init|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|writer
argument_list|)
decl_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
name|data
argument_list|,
name|xmlWriter
argument_list|)
expr_stmt|;
name|xmlWriter
operator|.
name|flush
argument_list|()
expr_stmt|;
name|BeanDefinitionBuilder
name|jaxbbean
init|=
name|BeanDefinitionBuilder
operator|.
name|rootBeanDefinition
argument_list|(
name|JAXBBeanFactory
operator|.
name|class
argument_list|)
decl_stmt|;
name|jaxbbean
operator|.
name|getRawBeanDefinition
argument_list|()
operator|.
name|setFactoryMethodName
argument_list|(
literal|"createJAXBBean"
argument_list|)
expr_stmt|;
name|jaxbbean
operator|.
name|addConstructorArg
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|jaxbbean
operator|.
name|addConstructorArg
argument_list|(
name|writer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|jaxbbean
operator|.
name|addConstructorArg
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|bean
operator|.
name|addPropertyValue
argument_list|(
name|propertyName
argument_list|,
name|jaxbbean
operator|.
name|getBeanDefinition
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|Unmarshaller
name|u
init|=
name|context
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
name|Object
name|obj
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|obj
operator|=
name|u
operator|.
name|unmarshal
argument_list|(
name|data
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|obj
operator|=
name|u
operator|.
name|unmarshal
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|obj
operator|instanceof
name|JAXBElement
argument_list|<
name|?
argument_list|>
condition|)
block|{
name|JAXBElement
argument_list|<
name|?
argument_list|>
name|el
init|=
operator|(
name|JAXBElement
argument_list|<
name|?
argument_list|>
operator|)
name|obj
decl_stmt|;
name|obj
operator|=
name|el
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|obj
operator|!=
literal|null
condition|)
block|{
name|bean
operator|.
name|addPropertyValue
argument_list|(
name|propertyName
argument_list|,
name|obj
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Could not parse configuration."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|String
name|getJaxbPackage
parameter_list|()
block|{
return|return
literal|""
return|;
block|}
specifier|protected
name|void
name|mapToProperty
parameter_list|(
name|BeanDefinitionBuilder
name|bean
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|String
name|val
parameter_list|)
block|{
if|if
condition|(
name|ID_ATTRIBUTE
operator|.
name|equals
argument_list|(
name|propertyName
argument_list|)
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|hasText
argument_list|(
name|val
argument_list|)
condition|)
block|{
if|if
condition|(
name|val
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
condition|)
block|{
name|bean
operator|.
name|addPropertyReference
argument_list|(
name|propertyName
argument_list|,
name|val
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|bean
operator|.
name|addPropertyValue
argument_list|(
name|propertyName
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|boolean
name|isAttribute
parameter_list|(
name|String
name|pre
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
operator|!
literal|"xmlns"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|&&
operator|(
name|pre
operator|==
literal|null
operator|||
operator|!
name|pre
operator|.
name|equals
argument_list|(
literal|"xmlns"
argument_list|)
operator|)
operator|&&
operator|!
literal|"abstract"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|&&
operator|!
literal|"lazy-init"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|&&
operator|!
literal|"id"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|protected
name|QName
name|parseQName
parameter_list|(
name|Element
name|element
parameter_list|,
name|String
name|t
parameter_list|)
block|{
name|String
name|ns
init|=
literal|null
decl_stmt|;
name|String
name|pre
init|=
literal|null
decl_stmt|;
name|String
name|local
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|t
operator|.
name|startsWith
argument_list|(
literal|"{"
argument_list|)
condition|)
block|{
name|int
name|i
init|=
name|t
operator|.
name|indexOf
argument_list|(
literal|'}'
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|==
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Namespace bracket '{' must having a closing bracket '}'."
argument_list|)
throw|;
block|}
name|ns
operator|=
name|t
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|i
argument_list|)
expr_stmt|;
name|t
operator|=
name|t
operator|.
name|substring
argument_list|(
name|i
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|int
name|colIdx
init|=
name|t
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
if|if
condition|(
name|colIdx
operator|==
operator|-
literal|1
condition|)
block|{
name|local
operator|=
name|t
expr_stmt|;
name|pre
operator|=
literal|""
expr_stmt|;
name|ns
operator|=
name|DOMUtils
operator|.
name|getNamespace
argument_list|(
name|element
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|pre
operator|=
name|t
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|colIdx
argument_list|)
expr_stmt|;
name|local
operator|=
name|t
operator|.
name|substring
argument_list|(
name|colIdx
operator|+
literal|1
argument_list|)
expr_stmt|;
name|ns
operator|=
name|DOMUtils
operator|.
name|getNamespace
argument_list|(
name|element
argument_list|,
name|pre
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
name|local
argument_list|,
name|pre
argument_list|)
return|;
block|}
comment|/* This id-or-name resolution logic follows that in Spring's      * org.springframework.beans.factory.xml.BeanDefinitionParserDelegate object      * Intent is to have resolution of CXF custom beans follow that of Spring beans      */
specifier|protected
name|String
name|getIdOrName
parameter_list|(
name|Element
name|elem
parameter_list|)
block|{
name|String
name|id
init|=
name|elem
operator|.
name|getAttribute
argument_list|(
name|BeanDefinitionParserDelegate
operator|.
name|ID_ATTRIBUTE
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|id
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|String
name|names
init|=
name|elem
operator|.
name|getAttribute
argument_list|(
name|BeanConstants
operator|.
name|NAME_ATTR
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|names
condition|)
block|{
name|StringTokenizer
name|st
init|=
operator|new
name|StringTokenizer
argument_list|(
name|names
argument_list|,
name|BeanDefinitionParserDelegate
operator|.
name|BEAN_NAME_DELIMITERS
argument_list|)
decl_stmt|;
if|if
condition|(
name|st
operator|.
name|countTokens
argument_list|()
operator|>
literal|0
condition|)
block|{
name|id
operator|=
name|st
operator|.
name|nextToken
argument_list|()
expr_stmt|;
block|}
block|}
block|}
return|return
name|id
return|;
block|}
block|}
end_class

end_unit

