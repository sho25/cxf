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
name|jaxrs
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
name|Collections
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
name|Set
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
name|helpers
operator|.
name|BaseNamespaceHandler
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
name|apache
operator|.
name|cxf
operator|.
name|staxutils
operator|.
name|W3CDOMStreamWriter
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
name|transform
operator|.
name|OutTransformWriter
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
literal|"http://cxf.apache.org/blueprint/jaxrs"
argument_list|)
specifier|public
class|class
name|JAXRSBPNamespaceHandler
extends|extends
name|BaseNamespaceHandler
block|{
specifier|private
name|BlueprintContainer
name|blueprintContainer
decl_stmt|;
specifier|public
name|JAXRSBPNamespaceHandler
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
if|if
condition|(
literal|"http://cxf.apache.org/blueprint/jaxrs"
operator|.
name|equals
argument_list|(
name|namespace
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
literal|"schemas/blueprint/jaxrs.xsd"
argument_list|)
return|;
block|}
return|return
name|super
operator|.
name|findCoreSchemaLocation
argument_list|(
name|namespace
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
name|JAXRSServerFactoryBeanDefinitionParser
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
literal|"client"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
return|return
name|context
operator|.
name|parseElement
argument_list|(
name|Metadata
operator|.
name|class
argument_list|,
literal|null
argument_list|,
name|transformElement
argument_list|(
name|element
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
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
specifier|private
name|Element
name|transformElement
parameter_list|(
name|Element
name|element
parameter_list|)
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|transformMap
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"{"
operator|+
name|element
operator|.
name|getNamespaceURI
argument_list|()
operator|+
literal|"}*"
argument_list|,
literal|"{http://cxf.apache.org/blueprint/jaxrs-client}*"
argument_list|)
decl_stmt|;
name|W3CDOMStreamWriter
name|domWriter
init|=
operator|new
name|W3CDOMStreamWriter
argument_list|()
decl_stmt|;
name|OutTransformWriter
name|transformWriter
init|=
operator|new
name|OutTransformWriter
argument_list|(
name|domWriter
argument_list|,
name|transformMap
argument_list|)
decl_stmt|;
try|try
block|{
name|StaxUtils
operator|.
name|copy
argument_list|(
name|element
argument_list|,
name|transformWriter
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
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
return|return
name|domWriter
operator|.
name|getDocument
argument_list|()
operator|.
name|getDocumentElement
argument_list|()
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
block|}
end_class

end_unit

