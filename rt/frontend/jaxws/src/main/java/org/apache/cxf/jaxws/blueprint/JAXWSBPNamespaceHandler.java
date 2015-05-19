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
name|NamespaceHandler
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
name|injection
operator|.
name|NoJSR250Annotations
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
name|endpoint
operator|.
name|Server
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
name|frontend
operator|.
name|blueprint
operator|.
name|ClientProxyFactoryBeanDefinitionParser
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
name|frontend
operator|.
name|blueprint
operator|.
name|ServerFactoryBeanDefinitionParser
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
name|JaxWsProxyFactoryBean
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
name|JaxWsServerFactoryBean
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
name|support
operator|.
name|JaxWsServiceFactoryBean
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
name|container
operator|.
name|BlueprintContainer
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
literal|"http://cxf.apache.org/blueprint/jaxws"
argument_list|)
specifier|public
class|class
name|JAXWSBPNamespaceHandler
implements|implements
name|NamespaceHandler
block|{
specifier|private
name|BlueprintContainer
name|blueprintContainer
decl_stmt|;
specifier|public
name|JAXWSBPNamespaceHandler
parameter_list|()
block|{     }
specifier|public
name|URL
name|getSchemaLocation
parameter_list|(
name|String
name|namespace
parameter_list|)
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
literal|"schemas/blueprint/jaxws.xsd"
argument_list|)
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
name|String
name|s
init|=
name|element
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"endpoint"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
return|return
operator|new
name|EndpointDefinitionParser
argument_list|()
operator|.
name|parse
argument_list|(
name|element
argument_list|,
name|context
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
literal|"server"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
return|return
operator|new
name|ServerFactoryBeanDefinitionParser
argument_list|(
name|BPJaxWsServerFactoryBean
operator|.
name|class
argument_list|)
operator|.
name|parse
argument_list|(
name|element
argument_list|,
name|context
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
literal|"client"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
return|return
operator|new
name|ClientProxyFactoryBeanDefinitionParser
argument_list|(
name|JaxWsProxyFactoryBean
operator|.
name|class
argument_list|)
operator|.
name|parse
argument_list|(
name|element
argument_list|,
name|context
argument_list|)
return|;
block|}
return|return
literal|null
return|;
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
name|ComponentMetadata
name|decorate
parameter_list|(
name|Node
name|node
parameter_list|,
name|ComponentMetadata
name|component
parameter_list|,
name|ParserContext
name|context
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|BlueprintContainer
name|getBlueprintContainer
parameter_list|()
block|{
return|return
name|blueprintContainer
return|;
block|}
specifier|public
name|void
name|setBlueprintContainer
parameter_list|(
name|BlueprintContainer
name|blueprintContainer
parameter_list|)
block|{
name|this
operator|.
name|blueprintContainer
operator|=
name|blueprintContainer
expr_stmt|;
block|}
annotation|@
name|NoJSR250Annotations
specifier|public
specifier|static
class|class
name|BPJaxWsServerFactoryBean
extends|extends
name|JaxWsServerFactoryBean
block|{
specifier|private
name|Server
name|server
decl_stmt|;
specifier|public
name|BPJaxWsServerFactoryBean
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|public
name|BPJaxWsServerFactoryBean
parameter_list|(
name|JaxWsServiceFactoryBean
name|fact
parameter_list|)
block|{
name|super
argument_list|(
name|fact
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Server
name|getServer
parameter_list|()
block|{
return|return
name|server
return|;
block|}
specifier|public
name|void
name|init
parameter_list|()
block|{
name|create
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Server
name|create
parameter_list|()
block|{
if|if
condition|(
name|server
operator|==
literal|null
condition|)
block|{
name|server
operator|=
name|super
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
return|return
name|server
return|;
block|}
specifier|public
name|void
name|destroy
parameter_list|()
block|{
if|if
condition|(
name|server
operator|!=
literal|null
condition|)
block|{
name|server
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|server
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

