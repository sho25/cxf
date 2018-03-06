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
name|HashMap
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
name|endpoint
operator|.
name|Endpoint
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
name|CastUtils
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
name|message
operator|.
name|Exchange
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
name|service
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchemaAnnotated
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchemaComplexType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchemaElement
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchemaSequence
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|ServiceModelUtil
block|{
specifier|private
name|ServiceModelUtil
parameter_list|()
block|{     }
specifier|public
specifier|static
name|Service
name|getService
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getService
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|String
name|getTargetNamespace
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|//all ServiceInfo's will have the same target namespace
return|return
name|getService
argument_list|(
name|exchange
argument_list|)
operator|.
name|getServiceInfos
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getTargetNamespace
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|BindingOperationInfo
name|getOperation
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|opName
parameter_list|)
block|{
name|Endpoint
name|ep
init|=
name|exchange
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
if|if
condition|(
name|ep
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|BindingInfo
name|service
init|=
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getBinding
argument_list|()
decl_stmt|;
for|for
control|(
name|BindingOperationInfo
name|b
range|:
name|service
operator|.
name|getOperations
argument_list|()
control|)
block|{
if|if
condition|(
name|b
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|.
name|equals
argument_list|(
name|opName
argument_list|)
condition|)
block|{
return|return
name|b
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|BindingOperationInfo
name|getOperation
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|QName
name|opName
parameter_list|)
block|{
name|Endpoint
name|ep
init|=
name|exchange
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
if|if
condition|(
name|ep
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|BindingInfo
name|service
init|=
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getBinding
argument_list|()
decl_stmt|;
return|return
name|service
operator|.
name|getOperation
argument_list|(
name|opName
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|BindingOperationInfo
name|getOperationForWrapperElement
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|QName
name|opName
parameter_list|,
name|boolean
name|output
parameter_list|)
block|{
name|Endpoint
name|ep
init|=
name|exchange
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
if|if
condition|(
name|ep
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|BindingInfo
name|service
init|=
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getBinding
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|QName
argument_list|,
name|BindingOperationInfo
argument_list|>
name|wrapperMap
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|service
operator|.
name|getProperty
argument_list|(
name|output
condition|?
literal|"ServiceModel.WRAPPER.MAP_OUT"
else|:
literal|"ServiceModel.WRAPPER.MAP"
argument_list|,
name|Map
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|wrapperMap
operator|==
literal|null
condition|)
block|{
name|wrapperMap
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
for|for
control|(
name|BindingOperationInfo
name|b
range|:
name|service
operator|.
name|getOperations
argument_list|()
control|)
block|{
if|if
condition|(
name|b
operator|.
name|isUnwrappedCapable
argument_list|()
condition|)
block|{
name|MessagePartInfo
name|part
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|output
operator|&&
name|b
operator|.
name|getOutput
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|b
operator|.
name|getOutput
argument_list|()
operator|.
name|getMessageParts
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|part
operator|=
name|b
operator|.
name|getOutput
argument_list|()
operator|.
name|getMessageParts
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|output
operator|&&
operator|!
name|b
operator|.
name|getInput
argument_list|()
operator|.
name|getMessageParts
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|part
operator|=
name|b
operator|.
name|getInput
argument_list|()
operator|.
name|getMessageParts
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|part
operator|!=
literal|null
condition|)
block|{
name|wrapperMap
operator|.
name|put
argument_list|(
name|part
operator|.
name|getConcreteName
argument_list|()
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|//check for single bare elements
name|BindingMessageInfo
name|info
init|=
name|output
condition|?
name|b
operator|.
name|getOutput
argument_list|()
else|:
name|b
operator|.
name|getInput
argument_list|()
decl_stmt|;
if|if
condition|(
name|info
operator|!=
literal|null
operator|&&
name|info
operator|.
name|getMessageParts
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|wrapperMap
operator|.
name|put
argument_list|(
name|info
operator|.
name|getMessageParts
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getConcreteName
argument_list|()
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|service
operator|.
name|setProperty
argument_list|(
name|output
condition|?
literal|"ServiceModel.WRAPPER.MAP_OUT"
else|:
literal|"ServiceModel.WRAPPER.MAP"
argument_list|,
name|wrapperMap
argument_list|)
expr_stmt|;
block|}
return|return
name|wrapperMap
operator|.
name|get
argument_list|(
name|opName
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|SchemaInfo
name|getSchema
parameter_list|(
name|ServiceInfo
name|serviceInfo
parameter_list|,
name|MessagePartInfo
name|messagePartInfo
parameter_list|)
block|{
name|SchemaInfo
name|schemaInfo
init|=
literal|null
decl_stmt|;
name|String
name|tns
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|messagePartInfo
operator|.
name|isElement
argument_list|()
condition|)
block|{
name|tns
operator|=
name|messagePartInfo
operator|.
name|getElementQName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|tns
operator|=
name|messagePartInfo
operator|.
name|getTypeQName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|SchemaInfo
name|schema
range|:
name|serviceInfo
operator|.
name|getSchemas
argument_list|()
control|)
block|{
if|if
condition|(
name|tns
operator|.
name|equals
argument_list|(
name|schema
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
name|schemaInfo
operator|=
name|schema
expr_stmt|;
block|}
block|}
return|return
name|schemaInfo
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|getOperationInputPartNames
parameter_list|(
name|OperationInfo
name|operation
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|MessagePartInfo
argument_list|>
name|parts
init|=
name|operation
operator|.
name|getInput
argument_list|()
operator|.
name|getMessageParts
argument_list|()
decl_stmt|;
if|if
condition|(
name|parts
operator|==
literal|null
operator|||
name|parts
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|names
return|;
block|}
for|for
control|(
name|MessagePartInfo
name|part
range|:
name|parts
control|)
block|{
name|XmlSchemaAnnotated
name|schema
init|=
name|part
operator|.
name|getXmlSchema
argument_list|()
decl_stmt|;
if|if
condition|(
name|schema
operator|instanceof
name|XmlSchemaElement
operator|&&
operator|(
operator|(
name|XmlSchemaElement
operator|)
name|schema
operator|)
operator|.
name|getSchemaType
argument_list|()
operator|instanceof
name|XmlSchemaComplexType
condition|)
block|{
name|XmlSchemaElement
name|element
init|=
operator|(
name|XmlSchemaElement
operator|)
name|schema
decl_stmt|;
name|XmlSchemaComplexType
name|cplxType
init|=
operator|(
name|XmlSchemaComplexType
operator|)
name|element
operator|.
name|getSchemaType
argument_list|()
decl_stmt|;
name|XmlSchemaSequence
name|seq
init|=
operator|(
name|XmlSchemaSequence
operator|)
name|cplxType
operator|.
name|getParticle
argument_list|()
decl_stmt|;
if|if
condition|(
name|seq
operator|==
literal|null
operator|||
name|seq
operator|.
name|getItems
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
name|names
return|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|seq
operator|.
name|getItems
argument_list|()
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|XmlSchemaElement
name|elChild
init|=
operator|(
name|XmlSchemaElement
operator|)
name|seq
operator|.
name|getItems
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|names
operator|.
name|add
argument_list|(
name|elChild
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|names
operator|.
name|add
argument_list|(
name|part
operator|.
name|getConcreteName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|names
return|;
block|}
specifier|public
specifier|static
name|EndpointInfo
name|findBestEndpointInfo
parameter_list|(
name|QName
name|qn
parameter_list|,
name|List
argument_list|<
name|ServiceInfo
argument_list|>
name|serviceInfos
parameter_list|)
block|{
for|for
control|(
name|ServiceInfo
name|serviceInfo
range|:
name|serviceInfos
control|)
block|{
name|Collection
argument_list|<
name|EndpointInfo
argument_list|>
name|eps
init|=
name|serviceInfo
operator|.
name|getEndpoints
argument_list|()
decl_stmt|;
for|for
control|(
name|EndpointInfo
name|ep
range|:
name|eps
control|)
block|{
if|if
condition|(
name|ep
operator|.
name|getInterface
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|qn
argument_list|)
condition|)
block|{
return|return
name|ep
return|;
block|}
block|}
block|}
name|EndpointInfo
name|best
init|=
literal|null
decl_stmt|;
for|for
control|(
name|ServiceInfo
name|serviceInfo
range|:
name|serviceInfos
control|)
block|{
name|Collection
argument_list|<
name|EndpointInfo
argument_list|>
name|eps
init|=
name|serviceInfo
operator|.
name|getEndpoints
argument_list|()
decl_stmt|;
for|for
control|(
name|EndpointInfo
name|ep
range|:
name|eps
control|)
block|{
if|if
condition|(
name|best
operator|==
literal|null
condition|)
block|{
name|best
operator|=
name|ep
expr_stmt|;
block|}
if|if
condition|(
name|ep
operator|.
name|getTransportId
argument_list|()
operator|.
name|equals
argument_list|(
literal|"http://schemas.xmlsoap.org/wsdl/soap/"
argument_list|)
condition|)
block|{
return|return
name|ep
return|;
block|}
block|}
block|}
return|return
name|best
return|;
block|}
specifier|public
specifier|static
name|QName
name|getServiceQName
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|)
block|{
name|InterfaceInfo
name|ii
init|=
name|ei
operator|.
name|getInterface
argument_list|()
decl_stmt|;
if|if
condition|(
name|ii
operator|!=
literal|null
condition|)
block|{
return|return
name|ii
operator|.
name|getName
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ei
operator|.
name|getService
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|ei
operator|.
name|getService
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|ei
operator|.
name|getName
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

