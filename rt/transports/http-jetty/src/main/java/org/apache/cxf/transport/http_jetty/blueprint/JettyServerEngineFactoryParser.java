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
name|http_jetty
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
name|reflect
operator|.
name|MapEntryImpl
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
name|reflect
operator|.
name|MapMetadataImpl
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
name|MapEntry
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
name|ValueMetadata
import|;
end_import

begin_class
specifier|public
class|class
name|JettyServerEngineFactoryParser
extends|extends
name|AbstractBPBeanDefinitionParser
block|{
specifier|public
specifier|static
specifier|final
name|String
name|JETTY_TRANSPORT
init|=
literal|"http://cxf.apache.org/transports/http-jetty/configuration"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JETTY_THREADING
init|=
literal|"http://cxf.apache.org/configuration/parameterized-types"
decl_stmt|;
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
name|ef
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
name|ef
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
name|ef
operator|.
name|setId
argument_list|(
literal|"jetty.engine.factory-holder-"
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
name|ef
operator|.
name|setRuntimeClass
argument_list|(
name|JettyHTTPServerEngineFactoryHolder
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// setup the ConnectorMap and HandlersMap property for the JettyHTTPServerEngineFactoryHolder
try|try
block|{
comment|// Print the DOM node
name|String
name|xmlString
init|=
name|StaxUtils
operator|.
name|toString
argument_list|(
name|element
argument_list|)
decl_stmt|;
name|ef
operator|.
name|addProperty
argument_list|(
literal|"parsedElement"
argument_list|,
name|createValue
argument_list|(
name|context
argument_list|,
name|xmlString
argument_list|)
argument_list|)
expr_stmt|;
name|ef
operator|.
name|setInitMethod
argument_list|(
literal|"init"
argument_list|)
expr_stmt|;
name|ef
operator|.
name|setActivation
argument_list|(
name|ComponentMetadata
operator|.
name|ACTIVATION_EAGER
argument_list|)
expr_stmt|;
name|ef
operator|.
name|setDestroyMethod
argument_list|(
literal|"destroy"
argument_list|)
expr_stmt|;
comment|// setup the EngineConnector
name|List
argument_list|<
name|Element
argument_list|>
name|engines
init|=
name|DOMUtils
operator|.
name|getChildrenWithName
argument_list|(
name|element
argument_list|,
name|HTTPJettyTransportNamespaceHandler
operator|.
name|JETTY_TRANSPORT
argument_list|,
literal|"engine"
argument_list|)
decl_stmt|;
name|ef
operator|.
name|addProperty
argument_list|(
literal|"connectorMap"
argument_list|,
name|parseEngineConnector
argument_list|(
name|engines
argument_list|,
name|ef
argument_list|,
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|ef
operator|.
name|addProperty
argument_list|(
literal|"handlersMap"
argument_list|,
name|parseEngineHandlers
argument_list|(
name|engines
argument_list|,
name|ef
argument_list|,
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|ef
operator|.
name|addProperty
argument_list|(
literal|"sessionSupportMap"
argument_list|,
name|parseEngineBooleanProperty
argument_list|(
name|engines
argument_list|,
name|ef
argument_list|,
name|context
argument_list|,
literal|"sessionSupport"
argument_list|)
argument_list|)
expr_stmt|;
name|ef
operator|.
name|addProperty
argument_list|(
literal|"reuseAddressMap"
argument_list|,
name|parseEngineBooleanProperty
argument_list|(
name|engines
argument_list|,
name|ef
argument_list|,
name|context
argument_list|,
literal|"reuseAddress"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|ef
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Could not process configuration."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|Metadata
name|parseEngineConnector
parameter_list|(
name|List
argument_list|<
name|Element
argument_list|>
name|engines
parameter_list|,
name|ComponentMetadata
name|enclosingComponent
parameter_list|,
name|ParserContext
name|context
parameter_list|)
block|{
name|List
argument_list|<
name|MapEntry
argument_list|>
name|entries
init|=
operator|new
name|ArrayList
argument_list|<
name|MapEntry
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Element
name|engine
range|:
name|engines
control|)
block|{
name|String
name|port
init|=
name|engine
operator|.
name|getAttribute
argument_list|(
literal|"port"
argument_list|)
decl_stmt|;
name|ValueMetadata
name|keyValue
init|=
name|createValue
argument_list|(
name|context
argument_list|,
name|port
argument_list|)
decl_stmt|;
name|Element
name|connector
init|=
name|DOMUtils
operator|.
name|getFirstChildWithName
argument_list|(
name|engine
argument_list|,
name|HTTPJettyTransportNamespaceHandler
operator|.
name|JETTY_TRANSPORT
argument_list|,
literal|"connector"
argument_list|)
decl_stmt|;
if|if
condition|(
name|connector
operator|!=
literal|null
condition|)
block|{
name|Element
name|first
init|=
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|connector
argument_list|)
decl_stmt|;
name|Metadata
name|valValue
init|=
name|context
operator|.
name|parseElement
argument_list|(
name|Metadata
operator|.
name|class
argument_list|,
name|enclosingComponent
argument_list|,
name|first
argument_list|)
decl_stmt|;
name|entries
operator|.
name|add
argument_list|(
operator|new
name|MapEntryImpl
argument_list|(
name|keyValue
argument_list|,
name|valValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|MapMetadataImpl
argument_list|(
literal|"java.lang.String"
argument_list|,
literal|"org.eclipse.jetty.server.Connector"
argument_list|,
name|entries
argument_list|)
return|;
block|}
specifier|protected
name|Metadata
name|parseEngineHandlers
parameter_list|(
name|List
argument_list|<
name|Element
argument_list|>
name|engines
parameter_list|,
name|ComponentMetadata
name|enclosingComponent
parameter_list|,
name|ParserContext
name|context
parameter_list|)
block|{
name|List
argument_list|<
name|MapEntry
argument_list|>
name|entries
init|=
operator|new
name|ArrayList
argument_list|<
name|MapEntry
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Element
name|engine
range|:
name|engines
control|)
block|{
name|String
name|port
init|=
name|engine
operator|.
name|getAttribute
argument_list|(
literal|"port"
argument_list|)
decl_stmt|;
name|ValueMetadata
name|keyValue
init|=
name|createValue
argument_list|(
name|context
argument_list|,
name|port
argument_list|)
decl_stmt|;
name|Element
name|handlers
init|=
name|DOMUtils
operator|.
name|getFirstChildWithName
argument_list|(
name|engine
argument_list|,
name|HTTPJettyTransportNamespaceHandler
operator|.
name|JETTY_TRANSPORT
argument_list|,
literal|"handlers"
argument_list|)
decl_stmt|;
if|if
condition|(
name|handlers
operator|!=
literal|null
condition|)
block|{
name|Metadata
name|valValue
init|=
name|parseListData
argument_list|(
name|context
argument_list|,
name|enclosingComponent
argument_list|,
name|handlers
argument_list|)
decl_stmt|;
name|entries
operator|.
name|add
argument_list|(
operator|new
name|MapEntryImpl
argument_list|(
name|keyValue
argument_list|,
name|valValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|MapMetadataImpl
argument_list|(
literal|"java.lang.String"
argument_list|,
literal|"java.util.List"
argument_list|,
name|entries
argument_list|)
return|;
block|}
specifier|protected
name|Metadata
name|parseEngineBooleanProperty
parameter_list|(
name|List
argument_list|<
name|Element
argument_list|>
name|engines
parameter_list|,
name|ComponentMetadata
name|enclosingComponent
parameter_list|,
name|ParserContext
name|context
parameter_list|,
name|String
name|propertyName
parameter_list|)
block|{
name|List
argument_list|<
name|MapEntry
argument_list|>
name|entries
init|=
operator|new
name|ArrayList
argument_list|<
name|MapEntry
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Element
name|engine
range|:
name|engines
control|)
block|{
name|String
name|port
init|=
name|engine
operator|.
name|getAttribute
argument_list|(
literal|"port"
argument_list|)
decl_stmt|;
name|ValueMetadata
name|keyValue
init|=
name|createValue
argument_list|(
name|context
argument_list|,
name|port
argument_list|)
decl_stmt|;
name|Element
name|sessionSupport
init|=
name|DOMUtils
operator|.
name|getFirstChildWithName
argument_list|(
name|engine
argument_list|,
name|HTTPJettyTransportNamespaceHandler
operator|.
name|JETTY_TRANSPORT
argument_list|,
name|propertyName
argument_list|)
decl_stmt|;
if|if
condition|(
name|sessionSupport
operator|!=
literal|null
condition|)
block|{
name|String
name|text
init|=
name|DOMUtils
operator|.
name|getContent
argument_list|(
name|sessionSupport
argument_list|)
decl_stmt|;
name|ValueMetadata
name|valValue
init|=
name|createValue
argument_list|(
name|context
argument_list|,
name|text
argument_list|)
decl_stmt|;
name|entries
operator|.
name|add
argument_list|(
operator|new
name|MapEntryImpl
argument_list|(
name|keyValue
argument_list|,
name|valValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|MapMetadataImpl
argument_list|(
literal|"java.lang.String"
argument_list|,
literal|"java.lang.Boolean"
argument_list|,
name|entries
argument_list|)
return|;
block|}
block|}
end_class

end_unit

