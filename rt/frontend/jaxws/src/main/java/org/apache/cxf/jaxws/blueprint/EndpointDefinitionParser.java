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
name|jaxws
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
name|StringTokenizer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
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
name|common
operator|.
name|classloader
operator|.
name|ClassLoaderUtils
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
name|jaxws
operator|.
name|EndpointImpl
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
name|jaxws
operator|.
name|spi
operator|.
name|ProviderImpl
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

begin_class
class|class
name|EndpointDefinitionParser
extends|extends
name|AbstractBPBeanDefinitionParser
block|{
specifier|private
specifier|static
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|EP_CLASS
decl_stmt|;
static|static
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
name|EndpointImpl
operator|.
name|class
decl_stmt|;
try|try
block|{
if|if
condition|(
name|ProviderImpl
operator|.
name|isJaxWs22
argument_list|()
condition|)
block|{
name|cls
operator|=
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
literal|"org.apache.cxf.jaxws22.JAXWS22EndpointImpl"
argument_list|,
name|EndpointDefinitionParser
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|cls
operator|=
name|EndpointImpl
operator|.
name|class
expr_stmt|;
block|}
name|EP_CLASS
operator|=
name|cls
expr_stmt|;
block|}
specifier|public
specifier|static
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
literal|"id"
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
literal|"name"
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
literal|","
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
specifier|public
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
comment|//Endpoint definition
name|MutableBeanMetadata
name|cxfBean
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
comment|//Add a blueprintContainer ref
comment|//cxfBean.addProperty("blueprintContainer", NSUtils.createRef(context, "blueprintContainer"));
comment|//cxfBean.addProperty("bundleContext", NSUtils.createRef(context, "blueprintBundleContext"));
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|getIdOrName
argument_list|(
name|element
argument_list|)
argument_list|)
condition|)
block|{
name|cxfBean
operator|.
name|setId
argument_list|(
name|getIdOrName
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cxfBean
operator|.
name|setId
argument_list|(
literal|"cxf.endpoint."
operator|+
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|cxfBean
operator|.
name|setRuntimeClass
argument_list|(
name|EP_CLASS
argument_list|)
expr_stmt|;
name|boolean
name|isAbstract
init|=
literal|false
decl_stmt|;
name|boolean
name|publish
init|=
literal|true
decl_stmt|;
name|NamedNodeMap
name|atts
init|=
name|element
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
name|String
name|bus
init|=
literal|null
decl_stmt|;
name|Metadata
name|impl
init|=
literal|null
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
operator|||
literal|"abstract"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|cxfBean
operator|.
name|setScope
argument_list|(
name|MutableBeanMetadata
operator|.
name|SCOPE_PROTOTYPE
argument_list|)
expr_stmt|;
name|isAbstract
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"publish"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|publish
operator|=
name|Boolean
operator|.
name|parseBoolean
argument_list|(
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
name|bus
operator|=
name|val
expr_stmt|;
block|}
elseif|else
if|if
condition|(
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
literal|"endpointName"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"serviceName"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|QName
name|q
init|=
name|parseQName
argument_list|(
name|element
argument_list|,
name|val
argument_list|)
decl_stmt|;
name|cxfBean
operator|.
name|addProperty
argument_list|(
name|name
argument_list|,
name|createValue
argument_list|(
name|context
argument_list|,
name|q
argument_list|)
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
name|cxfBean
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
literal|"implementor"
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
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
condition|)
block|{
name|impl
operator|=
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
expr_stmt|;
block|}
else|else
block|{
name|impl
operator|=
name|createObjectOfClass
argument_list|(
name|context
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
operator|!
literal|"name"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|cxfBean
operator|.
name|addProperty
argument_list|(
name|name
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
name|Element
name|elem
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
name|elem
operator|!=
literal|null
condition|)
block|{
name|String
name|name
init|=
name|elem
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"properties"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|Metadata
name|map
init|=
name|parseMapData
argument_list|(
name|context
argument_list|,
name|cxfBean
argument_list|,
name|elem
argument_list|)
decl_stmt|;
name|cxfBean
operator|.
name|addProperty
argument_list|(
name|name
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"binding"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|setFirstChildAsProperty
argument_list|(
name|elem
argument_list|,
name|context
argument_list|,
name|cxfBean
argument_list|,
literal|"bindingConfig"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"inInterceptors"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"inFaultInterceptors"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"outInterceptors"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"outFaultInterceptors"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"features"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"schemaLocations"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"handlers"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|Metadata
name|list
init|=
name|parseListData
argument_list|(
name|context
argument_list|,
name|cxfBean
argument_list|,
name|elem
argument_list|)
decl_stmt|;
name|cxfBean
operator|.
name|addProperty
argument_list|(
name|name
argument_list|,
name|list
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"implementor"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|impl
operator|=
name|context
operator|.
name|parseElement
argument_list|(
name|Metadata
operator|.
name|class
argument_list|,
name|cxfBean
argument_list|,
name|elem
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setFirstChildAsProperty
argument_list|(
name|elem
argument_list|,
name|context
argument_list|,
name|cxfBean
argument_list|,
name|name
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
name|cxfBean
operator|.
name|addArgument
argument_list|(
name|this
operator|.
name|getBusRef
argument_list|(
name|context
argument_list|,
name|bus
argument_list|)
argument_list|,
name|Bus
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|cxfBean
operator|.
name|addArgument
argument_list|(
name|impl
argument_list|,
name|Object
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isAbstract
condition|)
block|{
if|if
condition|(
name|publish
condition|)
block|{
name|cxfBean
operator|.
name|setInitMethod
argument_list|(
literal|"publish"
argument_list|)
expr_stmt|;
block|}
name|cxfBean
operator|.
name|setDestroyMethod
argument_list|(
literal|"stop"
argument_list|)
expr_stmt|;
block|}
comment|// We don't want to delay the registration of our Server
name|cxfBean
operator|.
name|setActivation
argument_list|(
name|MutableBeanMetadata
operator|.
name|ACTIVATION_EAGER
argument_list|)
expr_stmt|;
return|return
name|cxfBean
return|;
block|}
block|}
end_class

end_unit

