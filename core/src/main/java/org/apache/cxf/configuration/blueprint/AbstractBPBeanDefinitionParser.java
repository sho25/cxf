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
name|blueprint
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

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
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|Set
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
name|XMLStreamException
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
name|XMLStreamReader
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
name|apache
operator|.
name|aries
operator|.
name|blueprint
operator|.
name|ComponentDefinitionRegistry
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
name|PassThroughMetadata
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
name|MutableCollectionMetadata
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
name|aries
operator|.
name|blueprint
operator|.
name|mutable
operator|.
name|MutableRefMetadata
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
name|MutableValueMetadata
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
name|blueprint
operator|.
name|BlueprintBus
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
name|jaxb
operator|.
name|JAXBContextCache
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
name|jaxb
operator|.
name|JAXBContextCache
operator|.
name|CachedContextAndSchemas
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
name|osgi
operator|.
name|framework
operator|.
name|Bundle
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
name|BeanMetadata
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
name|CollectionMetadata
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
name|ComponentMetadata
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
name|MapMetadata
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
name|RefMetadata
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
name|ValueMetadata
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractBPBeanDefinitionParser
block|{
specifier|private
specifier|static
specifier|final
name|String
name|XMLNS_BLUEPRINT
init|=
literal|"http://www.osgi.org/xmlns/blueprint/v1.0.0"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|COMPONENT_ID
init|=
literal|"component-id"
decl_stmt|;
specifier|private
name|JAXBContext
name|jaxbContext
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|jaxbClasses
decl_stmt|;
specifier|protected
name|boolean
name|hasBusProperty
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|Metadata
name|createValue
parameter_list|(
name|ParserContext
name|context
parameter_list|,
name|QName
name|qName
parameter_list|)
block|{
name|MutableBeanMetadata
name|v
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
name|v
operator|.
name|setRuntimeClass
argument_list|(
name|QName
operator|.
name|class
argument_list|)
expr_stmt|;
name|v
operator|.
name|addArgument
argument_list|(
name|createValue
argument_list|(
name|context
argument_list|,
name|qName
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
argument_list|,
literal|null
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|v
operator|.
name|addArgument
argument_list|(
name|createValue
argument_list|(
name|context
argument_list|,
name|qName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
argument_list|,
literal|null
argument_list|,
literal|1
argument_list|)
expr_stmt|;
return|return
name|v
return|;
block|}
specifier|protected
name|Metadata
name|parseListData
parameter_list|(
name|ParserContext
name|context
parameter_list|,
name|ComponentMetadata
name|enclosingComponent
parameter_list|,
name|Element
name|element
parameter_list|)
block|{
name|MutableCollectionMetadata
name|m
init|=
operator|(
name|MutableCollectionMetadata
operator|)
name|context
operator|.
name|parseElement
argument_list|(
name|CollectionMetadata
operator|.
name|class
argument_list|,
name|enclosingComponent
argument_list|,
name|element
argument_list|)
decl_stmt|;
name|m
operator|.
name|setCollectionClass
argument_list|(
name|List
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
name|m
return|;
block|}
specifier|protected
name|Metadata
name|parseMapData
parameter_list|(
name|ParserContext
name|context
parameter_list|,
name|ComponentMetadata
name|enclosingComponent
parameter_list|,
name|Element
name|element
parameter_list|)
block|{
return|return
name|context
operator|.
name|parseElement
argument_list|(
name|MapMetadata
operator|.
name|class
argument_list|,
name|enclosingComponent
argument_list|,
name|element
argument_list|)
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
name|MutableBeanMetadata
name|bean
parameter_list|,
name|String
name|propertyName
parameter_list|)
block|{
name|Element
name|first
init|=
name|DOMUtils
operator|.
name|getFirstElement
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
name|String
name|id
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
name|XMLNS_BLUEPRINT
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
name|COMPONENT_ID
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
literal|"<ref> elements must have a \"component-id\" attribute!"
argument_list|)
throw|;
block|}
name|bean
operator|.
name|addProperty
argument_list|(
name|propertyName
argument_list|,
name|createRef
argument_list|(
name|ctx
argument_list|,
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//Rely on BP to handle these ones.
name|bean
operator|.
name|addProperty
argument_list|(
name|propertyName
argument_list|,
name|ctx
operator|.
name|parseElement
argument_list|(
name|Metadata
operator|.
name|class
argument_list|,
name|bean
argument_list|,
name|first
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|bean
operator|.
name|addProperty
argument_list|(
name|propertyName
argument_list|,
name|ctx
operator|.
name|parseElement
argument_list|(
name|Metadata
operator|.
name|class
argument_list|,
name|bean
argument_list|,
name|first
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
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
name|t1
init|=
name|t
decl_stmt|;
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
name|t1
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
name|t1
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
name|t1
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|i
argument_list|)
expr_stmt|;
name|t1
operator|=
name|t1
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
name|t1
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
name|t1
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
name|t1
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
name|t1
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
name|MutableBeanMetadata
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
operator|||
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
name|setScope
argument_list|(
name|BeanMetadata
operator|.
name|SCOPE_PROTOTYPE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
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
literal|"name"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|processNameAttribute
argument_list|(
name|element
argument_list|,
name|ctx
argument_list|,
name|bean
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
elseif|else
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
name|processBusAttribute
argument_list|(
name|element
argument_list|,
name|ctx
argument_list|,
name|bean
argument_list|,
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
name|isAttribute
argument_list|(
name|pre
argument_list|,
name|name
argument_list|)
condition|)
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
argument_list|,
name|ctx
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|setBus
return|;
block|}
specifier|protected
name|void
name|processBusAttribute
parameter_list|(
name|Element
name|element
parameter_list|,
name|ParserContext
name|ctx
parameter_list|,
name|MutableBeanMetadata
name|bean
parameter_list|,
name|String
name|val
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|hasBusProperty
argument_list|()
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
name|ctx
argument_list|,
name|val
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|bean
operator|.
name|addArgument
argument_list|(
name|getBusRef
argument_list|(
name|ctx
argument_list|,
name|val
argument_list|)
argument_list|,
literal|null
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|processNameAttribute
parameter_list|(
name|Element
name|element
parameter_list|,
name|ParserContext
name|ctx
parameter_list|,
name|MutableBeanMetadata
name|bean
parameter_list|,
name|String
name|val
parameter_list|)
block|{
comment|//nothing
block|}
specifier|protected
name|void
name|mapAttribute
parameter_list|(
name|MutableBeanMetadata
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
parameter_list|,
name|ParserContext
name|context
parameter_list|)
block|{
name|mapToProperty
argument_list|(
name|bean
argument_list|,
name|name
argument_list|,
name|val
argument_list|,
name|context
argument_list|)
expr_stmt|;
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
block|{     }
specifier|protected
name|void
name|mapToProperty
parameter_list|(
name|MutableBeanMetadata
name|bean
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|String
name|val
parameter_list|,
name|ParserContext
name|context
parameter_list|)
block|{
if|if
condition|(
literal|"id"
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
operator|!
name|StringUtils
operator|.
name|isEmpty
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
name|addProperty
argument_list|(
name|propertyName
argument_list|,
name|createRef
argument_list|(
name|context
argument_list|,
name|val
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|bean
operator|.
name|addProperty
argument_list|(
name|propertyName
argument_list|,
name|createValue
argument_list|(
name|context
argument_list|,
name|val
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
specifier|static
name|ValueMetadata
name|createValue
parameter_list|(
name|ParserContext
name|context
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|MutableValueMetadata
name|v
init|=
name|context
operator|.
name|createMetadata
argument_list|(
name|MutableValueMetadata
operator|.
name|class
argument_list|)
decl_stmt|;
name|v
operator|.
name|setStringValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
name|v
return|;
block|}
specifier|public
specifier|static
name|RefMetadata
name|createRef
parameter_list|(
name|ParserContext
name|context
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|MutableRefMetadata
name|r
init|=
name|context
operator|.
name|createMetadata
argument_list|(
name|MutableRefMetadata
operator|.
name|class
argument_list|)
decl_stmt|;
name|r
operator|.
name|setComponentId
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
name|r
return|;
block|}
specifier|public
specifier|static
name|PassThroughMetadata
name|createPassThrough
parameter_list|(
name|ParserContext
name|context
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|MutablePassThroughMetadata
name|v
init|=
name|context
operator|.
name|createMetadata
argument_list|(
name|MutablePassThroughMetadata
operator|.
name|class
argument_list|)
decl_stmt|;
name|v
operator|.
name|setObject
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
name|v
return|;
block|}
specifier|public
specifier|static
name|MutableBeanMetadata
name|createObjectOfClass
parameter_list|(
name|ParserContext
name|context
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|MutableBeanMetadata
name|v
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
name|v
operator|.
name|setClassName
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
name|v
return|;
block|}
specifier|protected
name|MutableBeanMetadata
name|getBus
parameter_list|(
name|ParserContext
name|context
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|ComponentDefinitionRegistry
name|cdr
init|=
name|context
operator|.
name|getComponentDefinitionRegistry
argument_list|()
decl_stmt|;
name|ComponentMetadata
name|meta
init|=
name|cdr
operator|.
name|getComponentDefinition
argument_list|(
literal|"blueprintBundle"
argument_list|)
decl_stmt|;
name|Bundle
name|blueprintBundle
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|meta
operator|instanceof
name|PassThroughMetadata
condition|)
block|{
name|blueprintBundle
operator|=
call|(
name|Bundle
call|)
argument_list|(
operator|(
name|PassThroughMetadata
operator|)
name|meta
argument_list|)
operator|.
name|getObject
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|cdr
operator|.
name|containsComponentDefinition
argument_list|(
name|InterceptorTypeConverter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|MutablePassThroughMetadata
name|md
init|=
name|context
operator|.
name|createMetadata
argument_list|(
name|MutablePassThroughMetadata
operator|.
name|class
argument_list|)
decl_stmt|;
name|md
operator|.
name|setObject
argument_list|(
operator|new
name|InterceptorTypeConverter
argument_list|()
argument_list|)
expr_stmt|;
name|md
operator|.
name|setId
argument_list|(
name|InterceptorTypeConverter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|getComponentDefinitionRegistry
argument_list|()
operator|.
name|registerTypeConverter
argument_list|(
name|md
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|blueprintBundle
operator|!=
literal|null
operator|&&
operator|!
name|cdr
operator|.
name|containsComponentDefinition
argument_list|(
name|name
argument_list|)
condition|)
block|{
comment|//Create a bus
name|MutableBeanMetadata
name|bus
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
name|bus
operator|.
name|setId
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|bus
operator|.
name|setRuntimeClass
argument_list|(
name|BlueprintBus
operator|.
name|class
argument_list|)
expr_stmt|;
name|bus
operator|.
name|addProperty
argument_list|(
literal|"bundleContext"
argument_list|,
name|createRef
argument_list|(
name|context
argument_list|,
literal|"blueprintBundleContext"
argument_list|)
argument_list|)
expr_stmt|;
name|bus
operator|.
name|addProperty
argument_list|(
literal|"blueprintContainer"
argument_list|,
name|createRef
argument_list|(
name|context
argument_list|,
literal|"blueprintContainer"
argument_list|)
argument_list|)
expr_stmt|;
name|bus
operator|.
name|setDestroyMethod
argument_list|(
literal|"shutdown"
argument_list|)
expr_stmt|;
name|bus
operator|.
name|setInitMethod
argument_list|(
literal|"initialize"
argument_list|)
expr_stmt|;
name|context
operator|.
name|getComponentDefinitionRegistry
argument_list|()
operator|.
name|registerComponentDefinition
argument_list|(
name|bus
argument_list|)
expr_stmt|;
return|return
name|bus
return|;
block|}
return|return
operator|(
name|MutableBeanMetadata
operator|)
name|cdr
operator|.
name|getComponentDefinition
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|protected
name|RefMetadata
name|getBusRef
parameter_list|(
name|ParserContext
name|context
parameter_list|,
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
literal|"cxf"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|getBus
argument_list|(
name|context
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|createRef
argument_list|(
name|context
argument_list|,
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
name|MutableBeanMetadata
name|bean
parameter_list|)
block|{
name|Element
name|el
init|=
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|element
argument_list|)
decl_stmt|;
while|while
condition|(
name|el
operator|!=
literal|null
condition|)
block|{
name|String
name|name
init|=
name|el
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
name|el
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|el
operator|=
name|DOMUtils
operator|.
name|getNextElement
argument_list|(
name|el
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
name|getFirstChildWithName
argument_list|(
name|parent
argument_list|,
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|data
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|mapElementToJaxbProperty
argument_list|(
name|ctx
argument_list|,
name|bean
argument_list|,
name|data
argument_list|,
name|propertyName
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
class|class
name|JAXBBeanFactory
block|{
specifier|final
name|JAXBContext
name|ctx
decl_stmt|;
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|cls
decl_stmt|;
specifier|public
name|JAXBBeanFactory
parameter_list|(
name|JAXBContext
name|c
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|c2
parameter_list|)
block|{
name|ctx
operator|=
name|c
expr_stmt|;
name|cls
operator|=
name|c2
expr_stmt|;
block|}
specifier|public
name|Object
name|createJAXBBean
parameter_list|(
name|String
name|v
parameter_list|)
block|{
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
operator|new
name|StringReader
argument_list|(
name|v
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|Object
name|o
init|=
name|ctx
operator|.
name|createUnmarshaller
argument_list|()
operator|.
name|unmarshal
argument_list|(
name|reader
argument_list|,
name|cls
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
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
name|o
decl_stmt|;
name|o
operator|=
name|el
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
return|return
name|o
return|;
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
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
try|try
block|{
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
comment|//ignore
block|}
block|}
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
name|data
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
try|try
block|{
name|XMLStreamWriter
name|xmlWriter
init|=
literal|null
decl_stmt|;
try|try
block|{
name|StringWriter
name|writer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|xmlWriter
operator|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|writer
argument_list|)
expr_stmt|;
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
name|MutableBeanMetadata
name|factory
init|=
name|ctx
operator|.
name|createMetadata
argument_list|(
name|MutableBeanMetadata
operator|.
name|class
argument_list|)
decl_stmt|;
name|factory
operator|.
name|setClassName
argument_list|(
name|c
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setFactoryComponent
argument_list|(
name|createPassThrough
argument_list|(
name|ctx
argument_list|,
operator|new
name|JAXBBeanFactory
argument_list|(
name|getContext
argument_list|(
name|c
argument_list|)
argument_list|,
name|c
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setFactoryMethod
argument_list|(
literal|"createJAXBBean"
argument_list|)
expr_stmt|;
name|factory
operator|.
name|addArgument
argument_list|(
name|createValue
argument_list|(
name|ctx
argument_list|,
name|writer
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|,
name|String
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|bean
operator|.
name|addProperty
argument_list|(
name|propertyName
argument_list|,
name|factory
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
name|getContext
argument_list|(
name|c
argument_list|)
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
name|obj
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
block|}
finally|finally
block|{
name|StaxUtils
operator|.
name|close
argument_list|(
name|xmlWriter
argument_list|)
expr_stmt|;
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
specifier|synchronized
name|JAXBContext
name|getContext
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
if|if
condition|(
name|jaxbContext
operator|==
literal|null
operator|||
name|jaxbClasses
operator|==
literal|null
operator|||
operator|!
name|jaxbClasses
operator|.
name|contains
argument_list|(
name|cls
argument_list|)
condition|)
block|{
try|try
block|{
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|tmp
init|=
operator|new
name|HashSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|jaxbClasses
operator|!=
literal|null
condition|)
block|{
name|tmp
operator|.
name|addAll
argument_list|(
name|jaxbClasses
argument_list|)
expr_stmt|;
block|}
name|JAXBContextCache
operator|.
name|addPackage
argument_list|(
name|tmp
argument_list|,
name|PackageUtils
operator|.
name|getPackageName
argument_list|(
name|cls
argument_list|)
argument_list|,
name|cls
operator|==
literal|null
condition|?
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
else|:
name|cls
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|cls
operator|!=
literal|null
condition|)
block|{
name|boolean
name|hasOf
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
range|:
name|tmp
control|)
block|{
if|if
condition|(
name|c
operator|.
name|getPackage
argument_list|()
operator|==
name|cls
operator|.
name|getPackage
argument_list|()
operator|&&
literal|"ObjectFactory"
operator|.
name|equals
argument_list|(
name|c
operator|.
name|getSimpleName
argument_list|()
argument_list|)
condition|)
block|{
name|hasOf
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|hasOf
condition|)
block|{
name|tmp
operator|.
name|add
argument_list|(
name|cls
argument_list|)
expr_stmt|;
block|}
block|}
name|JAXBContextCache
operator|.
name|scanPackages
argument_list|(
name|tmp
argument_list|)
expr_stmt|;
name|CachedContextAndSchemas
name|ccs
init|=
name|JAXBContextCache
operator|.
name|getCachedContextAndSchemas
argument_list|(
name|tmp
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|jaxbClasses
operator|=
name|ccs
operator|.
name|getClasses
argument_list|()
expr_stmt|;
name|jaxbContext
operator|=
name|ccs
operator|.
name|getContext
argument_list|()
expr_stmt|;
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
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|jaxbContext
return|;
block|}
block|}
end_class

end_unit

