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
name|context
package|;
end_package

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
name|HashMap
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
name|activation
operator|.
name|DataHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|BindingProvider
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|handler
operator|.
name|MessageContext
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
name|attachment
operator|.
name|AttachmentUtil
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
name|binding
operator|.
name|soap
operator|.
name|SoapBindingConstants
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
name|security
operator|.
name|AuthorizationPolicy
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
name|Attachment
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

begin_class
specifier|public
class|class
name|WrappedMessageContext
implements|implements
name|MessageContext
block|{
specifier|public
specifier|static
specifier|final
name|String
name|SCOPES
init|=
name|WrappedMessageContext
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".SCOPES"
decl_stmt|;
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|cxf2jaxwsMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|jaxws2cxfMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
static|static
block|{
name|cxf2jaxwsMap
operator|.
name|put
argument_list|(
name|Message
operator|.
name|ENDPOINT_ADDRESS
argument_list|,
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|)
expr_stmt|;
name|cxf2jaxwsMap
operator|.
name|put
argument_list|(
name|Message
operator|.
name|MAINTAIN_SESSION
argument_list|,
name|BindingProvider
operator|.
name|SESSION_MAINTAIN_PROPERTY
argument_list|)
expr_stmt|;
name|cxf2jaxwsMap
operator|.
name|put
argument_list|(
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|,
name|MessageContext
operator|.
name|HTTP_REQUEST_METHOD
argument_list|)
expr_stmt|;
name|cxf2jaxwsMap
operator|.
name|put
argument_list|(
name|Message
operator|.
name|RESPONSE_CODE
argument_list|,
name|MessageContext
operator|.
name|HTTP_RESPONSE_CODE
argument_list|)
expr_stmt|;
name|cxf2jaxwsMap
operator|.
name|put
argument_list|(
name|Message
operator|.
name|PATH_INFO
argument_list|,
name|MessageContext
operator|.
name|PATH_INFO
argument_list|)
expr_stmt|;
name|cxf2jaxwsMap
operator|.
name|put
argument_list|(
name|Message
operator|.
name|QUERY_STRING
argument_list|,
name|MessageContext
operator|.
name|QUERY_STRING
argument_list|)
expr_stmt|;
name|cxf2jaxwsMap
operator|.
name|put
argument_list|(
literal|"HTTP.REQUEST"
argument_list|,
name|MessageContext
operator|.
name|SERVLET_REQUEST
argument_list|)
expr_stmt|;
name|cxf2jaxwsMap
operator|.
name|put
argument_list|(
literal|"HTTP.RESPONSE"
argument_list|,
name|MessageContext
operator|.
name|SERVLET_RESPONSE
argument_list|)
expr_stmt|;
name|cxf2jaxwsMap
operator|.
name|put
argument_list|(
literal|"HTTP.CONTEXT"
argument_list|,
name|MessageContext
operator|.
name|SERVLET_CONTEXT
argument_list|)
expr_stmt|;
name|jaxws2cxfMap
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|,
name|Message
operator|.
name|ENDPOINT_ADDRESS
argument_list|)
expr_stmt|;
name|jaxws2cxfMap
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|SESSION_MAINTAIN_PROPERTY
argument_list|,
name|Message
operator|.
name|MAINTAIN_SESSION
argument_list|)
expr_stmt|;
name|jaxws2cxfMap
operator|.
name|put
argument_list|(
name|MessageContext
operator|.
name|HTTP_REQUEST_METHOD
argument_list|,
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|)
expr_stmt|;
name|jaxws2cxfMap
operator|.
name|put
argument_list|(
name|MessageContext
operator|.
name|HTTP_RESPONSE_CODE
argument_list|,
name|Message
operator|.
name|RESPONSE_CODE
argument_list|)
expr_stmt|;
name|jaxws2cxfMap
operator|.
name|put
argument_list|(
name|MessageContext
operator|.
name|PATH_INFO
argument_list|,
name|Message
operator|.
name|PATH_INFO
argument_list|)
expr_stmt|;
name|jaxws2cxfMap
operator|.
name|put
argument_list|(
name|MessageContext
operator|.
name|QUERY_STRING
argument_list|,
name|Message
operator|.
name|QUERY_STRING
argument_list|)
expr_stmt|;
name|jaxws2cxfMap
operator|.
name|put
argument_list|(
name|MessageContext
operator|.
name|SERVLET_REQUEST
argument_list|,
literal|"HTTP.REQUEST"
argument_list|)
expr_stmt|;
name|jaxws2cxfMap
operator|.
name|put
argument_list|(
name|MessageContext
operator|.
name|SERVLET_RESPONSE
argument_list|,
literal|"HTTP.RESPONSE"
argument_list|)
expr_stmt|;
name|jaxws2cxfMap
operator|.
name|put
argument_list|(
name|MessageContext
operator|.
name|SERVLET_CONTEXT
argument_list|,
literal|"HTTP.CONTEXT"
argument_list|)
expr_stmt|;
name|jaxws2cxfMap
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|SOAPACTION_URI_PROPERTY
argument_list|,
name|SoapBindingConstants
operator|.
name|SOAP_ACTION
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|message
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|reqMessage
decl_stmt|;
specifier|private
specifier|final
name|Exchange
name|exchange
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Scope
argument_list|>
name|scopes
decl_stmt|;
specifier|private
name|Scope
name|defaultScope
decl_stmt|;
specifier|public
name|WrappedMessageContext
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|this
argument_list|(
name|m
argument_list|,
name|Scope
operator|.
name|HANDLER
argument_list|)
expr_stmt|;
block|}
specifier|public
name|WrappedMessageContext
parameter_list|(
name|Message
name|m
parameter_list|,
name|Scope
name|defScope
parameter_list|)
block|{
name|this
argument_list|(
name|m
argument_list|,
name|m
operator|.
name|getExchange
argument_list|()
argument_list|,
name|defScope
argument_list|)
expr_stmt|;
block|}
specifier|public
name|WrappedMessageContext
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|m
parameter_list|,
name|Exchange
name|ex
parameter_list|,
name|Scope
name|defScope
parameter_list|)
block|{
name|message
operator|=
name|m
expr_stmt|;
name|exchange
operator|=
name|ex
expr_stmt|;
name|defaultScope
operator|=
name|defScope
expr_stmt|;
name|scopes
operator|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|message
operator|.
name|get
argument_list|(
name|SCOPES
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|isResponse
argument_list|()
operator|&&
name|exchange
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|isRequestor
argument_list|()
condition|)
block|{
name|reqMessage
operator|=
name|exchange
operator|.
name|getOutMessage
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|reqMessage
operator|=
name|exchange
operator|.
name|getInMessage
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|reqMessage
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|scopes
operator|==
literal|null
operator|&&
name|reqMessage
operator|!=
literal|null
condition|)
block|{
name|scopes
operator|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|reqMessage
operator|.
name|get
argument_list|(
name|SCOPES
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|scopes
operator|!=
literal|null
condition|)
block|{
name|m
operator|.
name|put
argument_list|(
name|SCOPES
argument_list|,
name|scopes
argument_list|)
expr_stmt|;
name|copyScoped
argument_list|(
name|reqMessage
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|scopes
operator|==
literal|null
condition|)
block|{
name|scopes
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Scope
argument_list|>
argument_list|()
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|SCOPES
argument_list|,
name|scopes
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|copyScoped
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|msg
parameter_list|)
block|{
for|for
control|(
name|String
name|s
range|:
name|scopes
operator|.
name|keySet
argument_list|()
control|)
block|{
name|message
operator|.
name|put
argument_list|(
name|s
argument_list|,
name|msg
operator|.
name|get
argument_list|(
name|s
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|mapKey
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|String
name|k2
init|=
name|jaxws2cxfMap
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|k2
operator|!=
literal|null
condition|)
block|{
return|return
name|k2
return|;
block|}
return|return
name|key
return|;
block|}
specifier|private
name|String
name|mapKeyReverse
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|String
name|k2
init|=
name|cxf2jaxwsMap
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|k2
operator|!=
literal|null
condition|)
block|{
return|return
name|k2
return|;
block|}
if|if
condition|(
name|Message
operator|.
name|PROTOCOL_HEADERS
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
name|isResponse
argument_list|()
condition|?
name|MessageContext
operator|.
name|HTTP_RESPONSE_HEADERS
else|:
name|MessageContext
operator|.
name|HTTP_REQUEST_HEADERS
return|;
block|}
return|return
name|key
return|;
block|}
specifier|protected
specifier|final
name|boolean
name|isResponse
parameter_list|()
block|{
return|return
name|isOutbound
argument_list|()
operator|^
name|isRequestor
argument_list|()
return|;
block|}
specifier|protected
specifier|final
name|boolean
name|isRequestor
parameter_list|()
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
name|containsKey
argument_list|(
name|Message
operator|.
name|REQUESTOR_ROLE
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
specifier|final
name|boolean
name|isOutbound
parameter_list|()
block|{
return|return
name|message
operator|!=
literal|null
operator|&&
name|exchange
operator|!=
literal|null
operator|&&
operator|(
name|message
operator|==
name|exchange
operator|.
name|getOutMessage
argument_list|()
operator|||
name|message
operator|==
name|exchange
operator|.
name|getOutFaultMessage
argument_list|()
operator|)
return|;
block|}
specifier|public
specifier|final
name|Message
name|getWrappedMessage
parameter_list|()
block|{
return|return
name|message
operator|instanceof
name|Message
condition|?
operator|(
name|Message
operator|)
name|message
else|:
literal|null
return|;
block|}
specifier|public
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getWrappedMap
parameter_list|()
block|{
return|return
name|message
return|;
block|}
specifier|public
name|void
name|clear
parameter_list|()
block|{
comment|//just clear the JAXWS things....
for|for
control|(
name|String
name|key
range|:
name|jaxws2cxfMap
operator|.
name|keySet
argument_list|()
control|)
block|{
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|final
name|boolean
name|containsKey
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
return|return
name|message
operator|.
name|containsKey
argument_list|(
name|mapKey
argument_list|(
operator|(
name|String
operator|)
name|key
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|final
name|boolean
name|containsValue
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
return|return
name|message
operator|.
name|containsValue
argument_list|(
name|value
argument_list|)
return|;
block|}
specifier|public
name|Object
name|get
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|String
name|mappedkey
init|=
name|mapKey
argument_list|(
operator|(
name|String
operator|)
name|key
argument_list|)
decl_stmt|;
name|Object
name|ret
init|=
name|message
operator|.
name|get
argument_list|(
name|mappedkey
argument_list|)
decl_stmt|;
if|if
condition|(
name|ret
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|Message
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|mappedkey
argument_list|)
condition|)
block|{
return|return
name|message
return|;
block|}
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
name|ret
operator|=
name|exchange
operator|.
name|get
argument_list|(
name|mappedkey
argument_list|)
expr_stmt|;
if|if
condition|(
name|ret
operator|!=
literal|null
condition|)
block|{
return|return
name|ret
return|;
block|}
block|}
if|if
condition|(
name|MessageContext
operator|.
name|INBOUND_MESSAGE_ATTACHMENTS
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
if|if
condition|(
name|isRequestor
argument_list|()
operator|&&
name|isOutbound
argument_list|()
condition|)
block|{
name|ret
operator|=
literal|null
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isOutbound
argument_list|()
condition|)
block|{
name|ret
operator|=
name|createAttachments
argument_list|(
name|reqMessage
argument_list|,
name|MessageContext
operator|.
name|INBOUND_MESSAGE_ATTACHMENTS
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ret
operator|=
name|createAttachments
argument_list|(
name|message
argument_list|,
name|MessageContext
operator|.
name|INBOUND_MESSAGE_ATTACHMENTS
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|MessageContext
operator|.
name|OUTBOUND_MESSAGE_ATTACHMENTS
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
if|if
condition|(
name|isRequestor
argument_list|()
operator|&&
operator|!
name|isOutbound
argument_list|()
condition|)
block|{
name|ret
operator|=
name|createAttachments
argument_list|(
name|reqMessage
argument_list|,
name|MessageContext
operator|.
name|OUTBOUND_MESSAGE_ATTACHMENTS
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ret
operator|=
name|createAttachments
argument_list|(
name|isRequestor
argument_list|()
condition|?
name|getWrappedMessage
argument_list|()
else|:
name|createResponseMessage
argument_list|()
argument_list|,
name|MessageContext
operator|.
name|OUTBOUND_MESSAGE_ATTACHMENTS
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|MessageContext
operator|.
name|MESSAGE_OUTBOUND_PROPERTY
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|ret
operator|=
name|isOutbound
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|MessageContext
operator|.
name|HTTP_REQUEST_HEADERS
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|isResponse
argument_list|()
condition|)
block|{
name|ret
operator|=
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|reqMessage
operator|!=
literal|null
operator|&&
operator|!
name|isRequestor
argument_list|()
condition|)
block|{
name|ret
operator|=
name|reqMessage
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|MessageContext
operator|.
name|HTTP_RESPONSE_HEADERS
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|Map
name|mp
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|isResponse
argument_list|()
condition|)
block|{
name|mp
operator|=
operator|(
name|Map
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
comment|//may have to create the out message and add the headers
name|Message
name|tmp
init|=
name|createResponseMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|tmp
operator|!=
literal|null
condition|)
block|{
name|ret
operator|=
operator|(
name|Map
operator|)
name|tmp
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
expr_stmt|;
block|}
block|}
name|ret
operator|=
name|mp
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|BindingProvider
operator|.
name|USERNAME_PROPERTY
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|AuthorizationPolicy
name|authPolicy
init|=
operator|(
name|AuthorizationPolicy
operator|)
name|message
operator|.
name|get
argument_list|(
name|AuthorizationPolicy
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|authPolicy
operator|!=
literal|null
condition|)
block|{
name|ret
operator|=
name|authPolicy
operator|.
name|getUserName
argument_list|()
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|BindingProvider
operator|.
name|PASSWORD_PROPERTY
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|AuthorizationPolicy
name|authPolicy
init|=
operator|(
name|AuthorizationPolicy
operator|)
name|message
operator|.
name|get
argument_list|(
name|AuthorizationPolicy
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|authPolicy
operator|!=
literal|null
condition|)
block|{
name|ret
operator|=
name|authPolicy
operator|.
name|getPassword
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|ret
operator|==
literal|null
operator|&&
name|reqMessage
operator|!=
literal|null
condition|)
block|{
name|ret
operator|=
name|reqMessage
operator|.
name|get
argument_list|(
name|mappedkey
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|ret
return|;
block|}
specifier|private
name|Message
name|createResponseMessage
parameter_list|()
block|{
if|if
condition|(
name|exchange
operator|==
literal|null
operator|||
name|exchange
operator|.
name|isOneWay
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|isResponse
argument_list|()
condition|)
block|{
return|return
name|getWrappedMessage
argument_list|()
return|;
block|}
name|Message
name|m
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|isRequestor
argument_list|()
condition|)
block|{
name|m
operator|=
name|exchange
operator|.
name|getInFaultMessage
argument_list|()
expr_stmt|;
if|if
condition|(
name|m
operator|==
literal|null
condition|)
block|{
name|m
operator|=
name|exchange
operator|.
name|getInMessage
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|m
operator|==
literal|null
condition|)
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
name|m
operator|=
name|ep
operator|.
name|getBinding
argument_list|()
operator|.
name|createMessage
argument_list|()
expr_stmt|;
name|exchange
operator|.
name|setInMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|m
operator|=
name|exchange
operator|.
name|getOutMessage
argument_list|()
expr_stmt|;
if|if
condition|(
name|m
operator|==
literal|null
condition|)
block|{
name|m
operator|=
name|exchange
operator|.
name|getOutFaultMessage
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|m
operator|==
literal|null
condition|)
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
name|m
operator|=
name|ep
operator|.
name|getBinding
argument_list|()
operator|.
name|createMessage
argument_list|()
expr_stmt|;
name|exchange
operator|.
name|setOutMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|m
return|;
block|}
specifier|private
name|Object
name|createAttachments
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|mc
parameter_list|,
name|String
name|propertyName
parameter_list|)
block|{
if|if
condition|(
name|mc
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|attachments
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|mc
operator|.
name|get
argument_list|(
name|Message
operator|.
name|ATTACHMENTS
argument_list|)
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|DataHandler
argument_list|>
name|dataHandlers
init|=
name|AttachmentUtil
operator|.
name|getDHMap
argument_list|(
name|attachments
argument_list|)
decl_stmt|;
name|mc
operator|.
name|put
argument_list|(
name|propertyName
argument_list|,
name|dataHandlers
argument_list|)
expr_stmt|;
name|scopes
operator|.
name|put
argument_list|(
name|propertyName
argument_list|,
name|Scope
operator|.
name|APPLICATION
argument_list|)
expr_stmt|;
return|return
name|dataHandlers
return|;
block|}
specifier|public
specifier|final
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|message
operator|.
name|isEmpty
argument_list|()
return|;
block|}
comment|// map to jaxws
specifier|public
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|keySet
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|set
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|s
range|:
name|message
operator|.
name|keySet
argument_list|()
control|)
block|{
name|set
operator|.
name|add
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|set
operator|.
name|add
argument_list|(
name|mapKeyReverse
argument_list|(
name|s
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|set
argument_list|)
return|;
block|}
specifier|public
specifier|final
name|Set
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|entrySet
parameter_list|()
block|{
name|Set
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|set
init|=
operator|new
name|HashSet
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|s
range|:
name|message
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|set
operator|.
name|add
argument_list|(
name|s
argument_list|)
expr_stmt|;
specifier|final
name|String
name|s2
init|=
name|mapKeyReverse
argument_list|(
name|s
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|Object
name|o
init|=
name|s
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|s2
operator|.
name|equals
argument_list|(
name|s
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
init|=
operator|new
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
block|{
specifier|public
name|String
name|getKey
parameter_list|()
block|{
return|return
name|s2
return|;
block|}
specifier|public
name|Object
name|getValue
parameter_list|()
block|{
return|return
name|o
return|;
block|}
specifier|public
name|Object
name|setValue
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
decl_stmt|;
name|set
operator|.
name|add
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|set
argument_list|)
return|;
block|}
specifier|public
specifier|final
name|Object
name|put
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
return|return
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|,
name|defaultScope
argument_list|)
return|;
block|}
specifier|public
specifier|final
name|Object
name|put
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|,
name|Scope
name|scope
parameter_list|)
block|{
name|String
name|mappedKey
init|=
name|mapKey
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|MessageContext
operator|.
name|MESSAGE_OUTBOUND_PROPERTY
operator|.
name|equals
argument_list|(
name|mappedKey
argument_list|)
condition|)
block|{
name|scopes
operator|.
name|put
argument_list|(
name|mappedKey
argument_list|,
name|scope
argument_list|)
expr_stmt|;
block|}
name|Object
name|ret
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|(
name|MessageContext
operator|.
name|HTTP_RESPONSE_HEADERS
operator|.
name|equals
argument_list|(
name|key
argument_list|)
operator|||
name|MessageContext
operator|.
name|HTTP_RESPONSE_CODE
operator|.
name|equals
argument_list|(
name|key
argument_list|)
operator|||
name|MessageContext
operator|.
name|OUTBOUND_MESSAGE_ATTACHMENTS
operator|.
name|equals
argument_list|(
name|key
argument_list|)
operator|||
name|MessageContext
operator|.
name|HTTP_RESPONSE_CODE
operator|.
name|equals
argument_list|(
name|key
argument_list|)
operator|)
operator|&&
operator|!
name|isResponse
argument_list|()
operator|&&
operator|!
name|isRequestor
argument_list|()
condition|)
block|{
name|Message
name|tmp
init|=
name|createResponseMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|tmp
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|MessageContext
operator|.
name|HTTP_RESPONSE_HEADERS
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
name|tmp
operator|.
name|put
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|,
name|value
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|tmp
operator|.
name|put
argument_list|(
name|mappedKey
argument_list|,
name|value
argument_list|)
return|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|BindingProvider
operator|.
name|USERNAME_PROPERTY
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|AuthorizationPolicy
name|authPolicy
init|=
operator|(
name|AuthorizationPolicy
operator|)
name|message
operator|.
name|get
argument_list|(
name|AuthorizationPolicy
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|authPolicy
operator|==
literal|null
condition|)
block|{
name|authPolicy
operator|=
operator|new
name|AuthorizationPolicy
argument_list|()
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|AuthorizationPolicy
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|authPolicy
argument_list|)
expr_stmt|;
block|}
name|ret
operator|=
name|authPolicy
operator|.
name|getUserName
argument_list|()
expr_stmt|;
name|authPolicy
operator|.
name|setUserName
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|BindingProvider
operator|.
name|PASSWORD_PROPERTY
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|AuthorizationPolicy
name|authPolicy
init|=
operator|(
name|AuthorizationPolicy
operator|)
name|message
operator|.
name|get
argument_list|(
name|AuthorizationPolicy
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|authPolicy
operator|==
literal|null
condition|)
block|{
name|authPolicy
operator|=
operator|new
name|AuthorizationPolicy
argument_list|()
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|AuthorizationPolicy
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|authPolicy
argument_list|)
expr_stmt|;
block|}
name|ret
operator|=
name|authPolicy
operator|.
name|getPassword
argument_list|()
expr_stmt|;
name|authPolicy
operator|.
name|setPassword
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|MessageContext
operator|.
name|HTTP_REQUEST_HEADERS
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|ret
operator|=
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SoapBindingConstants
operator|.
name|SOAP_ACTION
operator|.
name|equals
argument_list|(
name|mappedKey
argument_list|)
operator|&&
operator|!
name|isRequestor
argument_list|()
operator|&&
name|exchange
operator|!=
literal|null
condition|)
block|{
name|Message
name|tmp
init|=
name|createResponseMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|tmp
operator|!=
literal|null
condition|)
block|{
name|tmp
operator|.
name|put
argument_list|(
name|mappedKey
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|ret
operator|=
name|message
operator|.
name|put
argument_list|(
name|mappedKey
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
specifier|public
specifier|final
name|void
name|putAll
parameter_list|(
name|Map
argument_list|<
name|?
extends|extends
name|String
argument_list|,
name|?
extends|extends
name|Object
argument_list|>
name|t
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|?
extends|extends
name|String
argument_list|,
name|?
extends|extends
name|Object
argument_list|>
name|s
range|:
name|t
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|put
argument_list|(
name|s
operator|.
name|getKey
argument_list|()
argument_list|,
name|s
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|final
name|Object
name|remove
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|key
operator|=
name|mapKey
argument_list|(
operator|(
name|String
operator|)
name|key
argument_list|)
expr_stmt|;
name|scopes
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
name|BindingProvider
operator|.
name|PASSWORD_PROPERTY
operator|.
name|equals
argument_list|(
name|key
argument_list|)
operator|||
name|BindingProvider
operator|.
name|USERNAME_PROPERTY
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|message
operator|.
name|remove
argument_list|(
name|AuthorizationPolicy
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|message
operator|.
name|remove
argument_list|(
name|key
argument_list|)
return|;
block|}
specifier|public
specifier|final
name|int
name|size
parameter_list|()
block|{
return|return
name|message
operator|.
name|size
argument_list|()
return|;
block|}
specifier|public
specifier|final
name|Collection
argument_list|<
name|Object
argument_list|>
name|values
parameter_list|()
block|{
return|return
name|message
operator|.
name|values
argument_list|()
return|;
block|}
specifier|public
specifier|final
name|void
name|setScope
parameter_list|(
name|String
name|key
parameter_list|,
name|Scope
name|arg1
parameter_list|)
block|{
if|if
condition|(
operator|!
name|this
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"non-existant property-"
operator|+
name|key
operator|+
literal|"is specified"
argument_list|)
throw|;
block|}
name|scopes
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|arg1
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|final
name|Scope
name|getScope
parameter_list|(
name|String
name|key
parameter_list|)
block|{
if|if
condition|(
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
if|if
condition|(
name|scopes
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
name|scopes
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|defaultScope
return|;
block|}
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"non-existant property-"
operator|+
name|key
operator|+
literal|"is specified"
argument_list|)
throw|;
block|}
specifier|public
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Scope
argument_list|>
name|getScopes
parameter_list|()
block|{
return|return
name|scopes
return|;
block|}
block|}
end_class

end_unit

