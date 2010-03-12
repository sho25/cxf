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
name|interceptor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ResourceBundle
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
name|XMLStreamReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|validation
operator|.
name|Schema
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
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|BundleUtils
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
name|databinding
operator|.
name|DataReader
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
name|message
operator|.
name|Message
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
name|MessageUtils
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
name|phase
operator|.
name|AbstractPhaseInterceptor
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
name|cxf
operator|.
name|service
operator|.
name|model
operator|.
name|BindingMessageInfo
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
name|model
operator|.
name|BindingOperationInfo
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
name|model
operator|.
name|EndpointInfo
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
name|model
operator|.
name|MessageInfo
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
name|model
operator|.
name|MessagePartInfo
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
name|model
operator|.
name|OperationInfo
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
name|model
operator|.
name|ServiceInfo
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
name|model
operator|.
name|ServiceModelUtil
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
name|DepthXMLStreamReader
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
name|wsdl
operator|.
name|EndpointReferenceUtils
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractInDatabindingInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|public
specifier|static
specifier|final
name|String
name|NO_VALIDATE_PARTS
init|=
name|AbstractInDatabindingInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".novalidate-parts"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|XSD_ANY
init|=
operator|new
name|QName
argument_list|(
literal|"http://www.w3.org/2001/XMLSchema"
argument_list|,
literal|"anyType"
argument_list|,
literal|"xsd"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ResourceBundle
name|BUNDLE
init|=
name|BundleUtils
operator|.
name|getBundle
argument_list|(
name|AbstractInDatabindingInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|AbstractInDatabindingInterceptor
parameter_list|(
name|String
name|phase
parameter_list|)
block|{
name|super
argument_list|(
name|phase
argument_list|)
expr_stmt|;
block|}
specifier|public
name|AbstractInDatabindingInterceptor
parameter_list|(
name|String
name|i
parameter_list|,
name|String
name|phase
parameter_list|)
block|{
name|super
argument_list|(
name|i
argument_list|,
name|phase
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|boolean
name|isRequestor
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|REQUESTOR_ROLE
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|boolean
name|supportsDataReader
parameter_list|(
name|Message
name|message
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|input
parameter_list|)
block|{
name|Service
name|service
init|=
name|ServiceModelUtil
operator|.
name|getService
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|cls
index|[]
init|=
name|service
operator|.
name|getDataBinding
argument_list|()
operator|.
name|getSupportedReaderFormats
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
range|:
name|cls
control|)
block|{
if|if
condition|(
name|c
operator|.
name|equals
argument_list|(
name|input
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|DataReader
argument_list|<
name|T
argument_list|>
name|getDataReader
parameter_list|(
name|Message
name|message
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|input
parameter_list|)
block|{
name|Service
name|service
init|=
name|ServiceModelUtil
operator|.
name|getService
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
decl_stmt|;
name|DataReader
argument_list|<
name|T
argument_list|>
name|dataReader
init|=
name|service
operator|.
name|getDataBinding
argument_list|()
operator|.
name|createReader
argument_list|(
name|input
argument_list|)
decl_stmt|;
if|if
condition|(
name|dataReader
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
argument_list|(
literal|"NO_DATAREADER"
argument_list|,
name|BUNDLE
argument_list|,
name|service
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
name|dataReader
operator|.
name|setAttachments
argument_list|(
name|message
operator|.
name|getAttachments
argument_list|()
argument_list|)
expr_stmt|;
name|dataReader
operator|.
name|setProperty
argument_list|(
name|DataReader
operator|.
name|ENDPOINT
argument_list|,
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|dataReader
operator|.
name|setProperty
argument_list|(
name|Message
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|setSchemaInMessage
argument_list|(
name|service
argument_list|,
name|message
argument_list|,
name|dataReader
argument_list|)
expr_stmt|;
return|return
name|dataReader
return|;
block|}
specifier|protected
name|DataReader
argument_list|<
name|XMLStreamReader
argument_list|>
name|getDataReader
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|getDataReader
argument_list|(
name|message
argument_list|,
name|XMLStreamReader
operator|.
name|class
argument_list|)
return|;
block|}
specifier|protected
name|DataReader
argument_list|<
name|Node
argument_list|>
name|getNodeDataReader
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|getDataReader
argument_list|(
name|message
argument_list|,
name|Node
operator|.
name|class
argument_list|)
return|;
block|}
specifier|private
name|void
name|setSchemaInMessage
parameter_list|(
name|Service
name|service
parameter_list|,
name|Message
name|message
parameter_list|,
name|DataReader
argument_list|<
name|?
argument_list|>
name|reader
parameter_list|)
block|{
if|if
condition|(
name|MessageUtils
operator|.
name|getContextualBoolean
argument_list|(
name|message
argument_list|,
name|Message
operator|.
name|SCHEMA_VALIDATION_ENABLED
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
condition|)
block|{
comment|//all serviceInfos have the same schemas
name|Schema
name|schema
init|=
name|EndpointReferenceUtils
operator|.
name|getSchema
argument_list|(
name|service
operator|.
name|getServiceInfos
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getBus
argument_list|()
argument_list|)
decl_stmt|;
name|reader
operator|.
name|setSchema
argument_list|(
name|schema
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|DepthXMLStreamReader
name|getXMLStreamReader
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|XMLStreamReader
name|xr
init|=
name|message
operator|.
name|getContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|xr
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|xr
operator|instanceof
name|DepthXMLStreamReader
condition|)
block|{
return|return
operator|(
name|DepthXMLStreamReader
operator|)
name|xr
return|;
block|}
name|DepthXMLStreamReader
name|dr
init|=
operator|new
name|DepthXMLStreamReader
argument_list|(
name|xr
argument_list|)
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|dr
argument_list|)
expr_stmt|;
return|return
name|dr
return|;
block|}
comment|/**      * Find the next possible message part in the message. If an operation in      * the list of operations is no longer a viable match, it will be removed      * from the Collection.      *       * @param exchange      * @param operations      * @param name      * @param client      * @param index      * @return      */
specifier|protected
name|MessagePartInfo
name|findMessagePart
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Collection
argument_list|<
name|OperationInfo
argument_list|>
name|operations
parameter_list|,
name|QName
name|name
parameter_list|,
name|boolean
name|client
parameter_list|,
name|int
name|index
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
name|Endpoint
name|ep
init|=
name|exchange
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|MessagePartInfo
name|lastChoice
init|=
literal|null
decl_stmt|;
name|BindingMessageInfo
name|msgInfo
init|=
literal|null
decl_stmt|;
name|BindingOperationInfo
name|boi
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|OperationInfo
argument_list|>
name|itr
init|=
name|operations
operator|.
name|iterator
argument_list|()
init|;
name|itr
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|OperationInfo
name|op
init|=
name|itr
operator|.
name|next
argument_list|()
decl_stmt|;
name|boi
operator|=
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|getOperation
argument_list|(
name|op
argument_list|)
expr_stmt|;
if|if
condition|(
name|boi
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|client
condition|)
block|{
name|msgInfo
operator|=
name|boi
operator|.
name|getOutput
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|msgInfo
operator|=
name|boi
operator|.
name|getInput
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|msgInfo
operator|==
literal|null
condition|)
block|{
name|itr
operator|.
name|remove
argument_list|()
expr_stmt|;
continue|continue;
block|}
name|Collection
name|bodyParts
init|=
name|msgInfo
operator|.
name|getMessageParts
argument_list|()
decl_stmt|;
if|if
condition|(
name|bodyParts
operator|.
name|size
argument_list|()
operator|==
literal|0
operator|||
name|bodyParts
operator|.
name|size
argument_list|()
operator|<=
name|index
condition|)
block|{
name|itr
operator|.
name|remove
argument_list|()
expr_stmt|;
continue|continue;
block|}
name|MessagePartInfo
name|p
init|=
operator|(
name|MessagePartInfo
operator|)
name|msgInfo
operator|.
name|getMessageParts
argument_list|()
operator|.
name|get
argument_list|(
name|index
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|getNamespaceURI
argument_list|()
operator|==
literal|null
operator|||
name|name
operator|.
name|getNamespaceURI
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
comment|// message part has same namespace with the message
name|name
operator|=
operator|new
name|QName
argument_list|(
name|p
operator|.
name|getMessageInfo
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|name
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|p
operator|.
name|getConcreteName
argument_list|()
argument_list|)
condition|)
block|{
name|exchange
operator|.
name|put
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|,
name|boi
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|put
argument_list|(
name|OperationInfo
operator|.
name|class
argument_list|,
name|boi
operator|.
name|getOperationInfo
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setOneWay
argument_list|(
name|op
operator|.
name|isOneWay
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|p
return|;
block|}
if|if
condition|(
name|XSD_ANY
operator|.
name|equals
argument_list|(
name|p
operator|.
name|getTypeQName
argument_list|()
argument_list|)
condition|)
block|{
name|lastChoice
operator|=
name|p
expr_stmt|;
block|}
else|else
block|{
name|itr
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|lastChoice
operator|!=
literal|null
condition|)
block|{
name|setMessage
argument_list|(
name|message
argument_list|,
name|boi
argument_list|,
name|client
argument_list|,
name|boi
operator|.
name|getBinding
argument_list|()
operator|.
name|getService
argument_list|()
argument_list|,
name|msgInfo
operator|.
name|getMessageInfo
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|lastChoice
return|;
block|}
specifier|protected
name|MessageInfo
name|setMessage
parameter_list|(
name|Message
name|message
parameter_list|,
name|BindingOperationInfo
name|operation
parameter_list|,
name|boolean
name|requestor
parameter_list|,
name|ServiceInfo
name|si
parameter_list|,
name|MessageInfo
name|msgInfo
parameter_list|)
block|{
name|message
operator|.
name|put
argument_list|(
name|MessageInfo
operator|.
name|class
argument_list|,
name|msgInfo
argument_list|)
expr_stmt|;
name|Exchange
name|ex
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|,
name|operation
argument_list|)
expr_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|OperationInfo
operator|.
name|class
argument_list|,
name|operation
operator|.
name|getOperationInfo
argument_list|()
argument_list|)
expr_stmt|;
name|ex
operator|.
name|setOneWay
argument_list|(
name|operation
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|isOneWay
argument_list|()
argument_list|)
expr_stmt|;
comment|//Set standard MessageContext properties required by JAX_WS, but not specific to JAX_WS.
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|WSDL_OPERATION
argument_list|,
name|operation
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|QName
name|serviceQName
init|=
name|si
operator|.
name|getName
argument_list|()
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|WSDL_SERVICE
argument_list|,
name|serviceQName
argument_list|)
expr_stmt|;
name|QName
name|interfaceQName
init|=
name|si
operator|.
name|getInterface
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|WSDL_INTERFACE
argument_list|,
name|interfaceQName
argument_list|)
expr_stmt|;
name|EndpointInfo
name|endpointInfo
init|=
name|ex
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
name|QName
name|portQName
init|=
name|endpointInfo
operator|.
name|getName
argument_list|()
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|WSDL_PORT
argument_list|,
name|portQName
argument_list|)
expr_stmt|;
name|URI
name|wsdlDescription
init|=
name|endpointInfo
operator|.
name|getProperty
argument_list|(
literal|"URI"
argument_list|,
name|URI
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|wsdlDescription
operator|==
literal|null
condition|)
block|{
name|String
name|address
init|=
name|endpointInfo
operator|.
name|getAddress
argument_list|()
decl_stmt|;
try|try
block|{
name|wsdlDescription
operator|=
operator|new
name|URI
argument_list|(
name|address
operator|+
literal|"?wsdl"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
comment|//do nothing
block|}
name|endpointInfo
operator|.
name|setProperty
argument_list|(
literal|"URI"
argument_list|,
name|wsdlDescription
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|WSDL_DESCRIPTION
argument_list|,
name|wsdlDescription
argument_list|)
expr_stmt|;
return|return
name|msgInfo
return|;
block|}
comment|/**      * Returns a BindingOperationInfo if the operation is indentified as       * a wrapped method,  return null if it is not a wrapped method       * (i.e., it is a bare method)      *       * @param exchange      * @param name      * @param client      * @return      */
specifier|protected
name|BindingOperationInfo
name|getBindingOperationInfo
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|QName
name|name
parameter_list|,
name|boolean
name|client
parameter_list|)
block|{
name|String
name|local
init|=
name|name
operator|.
name|getLocalPart
argument_list|()
decl_stmt|;
if|if
condition|(
name|client
operator|&&
name|local
operator|.
name|endsWith
argument_list|(
literal|"Response"
argument_list|)
condition|)
block|{
name|local
operator|=
name|local
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|local
operator|.
name|length
argument_list|()
operator|-
literal|8
argument_list|)
expr_stmt|;
block|}
comment|// TODO: Allow overridden methods.
name|BindingOperationInfo
name|bop
init|=
name|ServiceModelUtil
operator|.
name|getOperation
argument_list|(
name|exchange
argument_list|,
name|local
argument_list|)
decl_stmt|;
if|if
condition|(
name|bop
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|put
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|,
name|bop
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|put
argument_list|(
name|OperationInfo
operator|.
name|class
argument_list|,
name|bop
operator|.
name|getOperationInfo
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|bop
return|;
block|}
specifier|protected
name|MessageInfo
name|getMessageInfo
parameter_list|(
name|Message
name|message
parameter_list|,
name|BindingOperationInfo
name|operation
parameter_list|)
block|{
return|return
name|getMessageInfo
argument_list|(
name|message
argument_list|,
name|operation
argument_list|,
name|isRequestor
argument_list|(
name|message
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|MessageInfo
name|getMessageInfo
parameter_list|(
name|Message
name|message
parameter_list|,
name|BindingOperationInfo
name|operation
parameter_list|,
name|boolean
name|requestor
parameter_list|)
block|{
name|MessageInfo
name|msgInfo
decl_stmt|;
name|OperationInfo
name|intfOp
init|=
name|operation
operator|.
name|getOperationInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|requestor
condition|)
block|{
name|msgInfo
operator|=
name|intfOp
operator|.
name|getOutput
argument_list|()
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|MessageInfo
operator|.
name|class
argument_list|,
name|intfOp
operator|.
name|getOutput
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|msgInfo
operator|=
name|intfOp
operator|.
name|getInput
argument_list|()
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|MessageInfo
operator|.
name|class
argument_list|,
name|intfOp
operator|.
name|getInput
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|msgInfo
return|;
block|}
block|}
end_class

end_unit

