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
name|net
operator|.
name|URL
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
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
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
name|apache
operator|.
name|aries
operator|.
name|blueprint
operator|.
name|Namespaces
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
name|helpers
operator|.
name|BaseNamespaceHandler
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
name|Metadata
import|;
end_import

begin_class
annotation|@
name|Namespaces
argument_list|(
literal|"http://cxf.apache.org/transports/http-jetty/configuration"
argument_list|)
specifier|public
class|class
name|HTTPJettyTransportNamespaceHandler
extends|extends
name|BaseNamespaceHandler
block|{
specifier|public
specifier|static
specifier|final
name|String
name|JETTY_TRANSPORT
init|=
literal|"http://cxf.apache.org/transports/http-jetty/configuration"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|JETTY_ENGINE
init|=
literal|"engine"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|JETTY_ENGINE_FACTORY
init|=
literal|"engine-factory"
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
name|HTTPJettyTransportNamespaceHandler
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|URL
name|getSchemaLocation
parameter_list|(
name|String
name|s
parameter_list|)
block|{
if|if
condition|(
name|JETTY_TRANSPORT
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
return|return
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"schemas/configuration/http-jetty.xsd"
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|super
operator|.
name|findCoreSchemaLocation
argument_list|(
name|s
argument_list|)
return|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|public
name|Set
argument_list|<
name|Class
argument_list|>
name|getManagedClasses
parameter_list|()
block|{
return|return
literal|null
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
name|parserContext
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Parsing element {{"
operator|+
name|element
operator|.
name|getNamespaceURI
argument_list|()
operator|+
literal|"}}{"
operator|+
name|element
operator|.
name|getLocalName
argument_list|()
operator|+
literal|"}"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|JETTY_ENGINE
operator|.
name|equals
argument_list|(
name|element
operator|.
name|getLocalName
argument_list|()
argument_list|)
condition|)
block|{
comment|//This doesn't hit normal configs.
return|return
operator|new
name|JettyServerEngineParser
argument_list|()
operator|.
name|parse
argument_list|(
name|element
argument_list|,
name|parserContext
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|JETTY_ENGINE_FACTORY
operator|.
name|equals
argument_list|(
name|element
operator|.
name|getLocalName
argument_list|()
argument_list|)
condition|)
block|{
return|return
operator|new
name|JettyServerEngineFactoryParser
argument_list|()
operator|.
name|parse
argument_list|(
name|element
argument_list|,
name|parserContext
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|ComponentMetadata
name|decorate
parameter_list|(
name|Node
name|node
parameter_list|,
name|ComponentMetadata
name|componentMetadata
parameter_list|,
name|ParserContext
name|parserContext
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Decorating node "
operator|+
name|node
operator|+
literal|" "
operator|+
name|componentMetadata
argument_list|)
expr_stmt|;
return|return
name|componentMetadata
return|;
block|}
block|}
end_class

end_unit

