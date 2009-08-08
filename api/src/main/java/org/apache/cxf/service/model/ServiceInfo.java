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
name|service
operator|.
name|model
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
name|Collection
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
name|List
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
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CopyOnWriteArrayList
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
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|xmlschema
operator|.
name|SchemaCollection
import|;
end_import

begin_class
specifier|public
class|class
name|ServiceInfo
extends|extends
name|AbstractDescriptionElement
implements|implements
name|NamedItem
block|{
name|QName
name|name
decl_stmt|;
name|String
name|targetNamespace
decl_stmt|;
name|InterfaceInfo
name|intf
decl_stmt|;
name|List
argument_list|<
name|BindingInfo
argument_list|>
name|bindings
init|=
operator|new
name|CopyOnWriteArrayList
argument_list|<
name|BindingInfo
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|EndpointInfo
argument_list|>
name|endpoints
init|=
operator|new
name|CopyOnWriteArrayList
argument_list|<
name|EndpointInfo
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|QName
argument_list|,
name|MessageInfo
argument_list|>
name|messages
decl_stmt|;
name|List
argument_list|<
name|SchemaInfo
argument_list|>
name|schemas
init|=
operator|new
name|ArrayList
argument_list|<
name|SchemaInfo
argument_list|>
argument_list|(
literal|4
argument_list|)
decl_stmt|;
specifier|private
name|SchemaCollection
name|xmlSchemaCollection
decl_stmt|;
specifier|private
name|String
name|topLevelDoc
decl_stmt|;
specifier|public
name|ServiceInfo
parameter_list|()
block|{
name|xmlSchemaCollection
operator|=
operator|new
name|SchemaCollection
argument_list|()
expr_stmt|;
block|}
specifier|public
name|String
name|getTopLevelDoc
parameter_list|()
block|{
return|return
name|topLevelDoc
return|;
block|}
specifier|public
name|void
name|setTopLevelDoc
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|topLevelDoc
operator|=
name|s
expr_stmt|;
block|}
specifier|public
name|String
name|getTargetNamespace
parameter_list|()
block|{
return|return
name|targetNamespace
return|;
block|}
specifier|public
name|void
name|setTargetNamespace
parameter_list|(
name|String
name|ns
parameter_list|)
block|{
name|targetNamespace
operator|=
name|ns
expr_stmt|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|QName
name|n
parameter_list|)
block|{
name|name
operator|=
name|n
expr_stmt|;
block|}
specifier|public
name|QName
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|InterfaceInfo
name|createInterface
parameter_list|(
name|QName
name|qn
parameter_list|)
block|{
name|intf
operator|=
operator|new
name|InterfaceInfo
argument_list|(
name|this
argument_list|,
name|qn
argument_list|)
expr_stmt|;
return|return
name|intf
return|;
block|}
specifier|public
name|void
name|setInterface
parameter_list|(
name|InterfaceInfo
name|inf
parameter_list|)
block|{
name|intf
operator|=
name|inf
expr_stmt|;
block|}
specifier|public
name|InterfaceInfo
name|getInterface
parameter_list|()
block|{
return|return
name|intf
return|;
block|}
specifier|public
name|BindingInfo
name|getBinding
parameter_list|(
name|QName
name|qn
parameter_list|)
block|{
for|for
control|(
name|BindingInfo
name|bi
range|:
name|bindings
control|)
block|{
if|if
condition|(
name|qn
operator|.
name|equals
argument_list|(
name|bi
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|bi
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|addBinding
parameter_list|(
name|BindingInfo
name|binding
parameter_list|)
block|{
name|BindingInfo
name|bi
init|=
name|getBinding
argument_list|(
name|binding
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|bi
operator|!=
literal|null
condition|)
block|{
name|bindings
operator|.
name|remove
argument_list|(
name|bi
argument_list|)
expr_stmt|;
block|}
name|bindings
operator|.
name|add
argument_list|(
name|binding
argument_list|)
expr_stmt|;
block|}
specifier|public
name|EndpointInfo
name|getEndpoint
parameter_list|(
name|QName
name|qn
parameter_list|)
block|{
for|for
control|(
name|EndpointInfo
name|ei
range|:
name|endpoints
control|)
block|{
if|if
condition|(
name|qn
operator|.
name|equals
argument_list|(
name|ei
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|ei
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|addEndpoint
parameter_list|(
name|EndpointInfo
name|ep
parameter_list|)
block|{
name|EndpointInfo
name|ei
init|=
name|getEndpoint
argument_list|(
name|ep
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ei
operator|!=
literal|null
condition|)
block|{
name|endpoints
operator|.
name|remove
argument_list|(
name|ei
argument_list|)
expr_stmt|;
block|}
name|endpoints
operator|.
name|add
argument_list|(
name|ep
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Collection
argument_list|<
name|EndpointInfo
argument_list|>
name|getEndpoints
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableCollection
argument_list|(
name|endpoints
argument_list|)
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|BindingInfo
argument_list|>
name|getBindings
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableCollection
argument_list|(
name|bindings
argument_list|)
return|;
block|}
specifier|public
name|Map
argument_list|<
name|QName
argument_list|,
name|MessageInfo
argument_list|>
name|getMessages
parameter_list|()
block|{
if|if
condition|(
name|messages
operator|==
literal|null
condition|)
block|{
name|initMessagesMap
argument_list|()
expr_stmt|;
block|}
return|return
name|messages
return|;
block|}
specifier|public
name|MessageInfo
name|getMessage
parameter_list|(
name|QName
name|qname
parameter_list|)
block|{
return|return
name|getMessages
argument_list|()
operator|.
name|get
argument_list|(
name|qname
argument_list|)
return|;
block|}
specifier|private
name|void
name|initMessagesMap
parameter_list|()
block|{
name|messages
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|QName
argument_list|,
name|MessageInfo
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|OperationInfo
name|operation
range|:
name|getInterface
argument_list|()
operator|.
name|getOperations
argument_list|()
control|)
block|{
if|if
condition|(
name|operation
operator|.
name|getInput
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|messages
operator|.
name|put
argument_list|(
name|operation
operator|.
name|getInput
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|operation
operator|.
name|getInput
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|operation
operator|.
name|getOutput
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|messages
operator|.
name|put
argument_list|(
name|operation
operator|.
name|getOutput
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|operation
operator|.
name|getOutput
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|setMessages
parameter_list|(
name|Map
argument_list|<
name|QName
argument_list|,
name|MessageInfo
argument_list|>
name|msgs
parameter_list|)
block|{
name|messages
operator|=
name|msgs
expr_stmt|;
block|}
specifier|public
name|void
name|refresh
parameter_list|()
block|{
name|initMessagesMap
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|addSchema
parameter_list|(
name|SchemaInfo
name|schemaInfo
parameter_list|)
block|{
name|schemas
operator|.
name|add
argument_list|(
name|schemaInfo
argument_list|)
expr_stmt|;
block|}
specifier|public
name|SchemaInfo
name|addNewSchema
parameter_list|(
name|String
name|namespaceURI
parameter_list|)
block|{
name|SchemaInfo
name|schemaInfo
init|=
operator|new
name|SchemaInfo
argument_list|(
name|namespaceURI
argument_list|)
decl_stmt|;
name|schemaInfo
operator|.
name|setSchema
argument_list|(
name|getXmlSchemaCollection
argument_list|()
operator|.
name|newXmlSchemaInCollection
argument_list|(
name|namespaceURI
argument_list|)
argument_list|)
expr_stmt|;
name|schemas
operator|.
name|add
argument_list|(
name|schemaInfo
argument_list|)
expr_stmt|;
return|return
name|schemaInfo
return|;
block|}
specifier|public
name|SchemaInfo
name|getSchema
parameter_list|(
name|String
name|namespaceURI
parameter_list|)
block|{
for|for
control|(
name|SchemaInfo
name|s
range|:
name|schemas
control|)
block|{
if|if
condition|(
name|namespaceURI
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|namespaceURI
operator|.
name|equals
argument_list|(
name|s
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|s
return|;
block|}
block|}
elseif|else
if|if
condition|(
name|s
operator|.
name|getNamespaceURI
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
name|s
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|List
argument_list|<
name|SchemaInfo
argument_list|>
name|getSchemas
parameter_list|()
block|{
return|return
name|schemas
return|;
block|}
specifier|public
name|SchemaCollection
name|getXmlSchemaCollection
parameter_list|()
block|{
return|return
name|xmlSchemaCollection
return|;
block|}
specifier|public
name|void
name|setServiceSchemaInfo
parameter_list|(
name|ServiceSchemaInfo
name|serviceSchemaInfo
parameter_list|)
block|{
name|xmlSchemaCollection
operator|=
name|serviceSchemaInfo
operator|.
name|getSchemaCollection
argument_list|()
expr_stmt|;
name|schemas
operator|=
name|serviceSchemaInfo
operator|.
name|getSchemaInfoList
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|setSchemas
parameter_list|(
name|SchemaCollection
name|cachedXmlSchemaCollection
parameter_list|,
name|List
argument_list|<
name|SchemaInfo
argument_list|>
name|cachedSchemas
parameter_list|)
block|{
name|xmlSchemaCollection
operator|=
name|cachedXmlSchemaCollection
expr_stmt|;
name|schemas
operator|=
name|cachedSchemas
expr_stmt|;
block|}
block|}
end_class

end_unit

