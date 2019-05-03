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
name|binding
operator|.
name|soap
operator|.
name|interceptor
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
name|logging
operator|.
name|Logger
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
name|AttachmentDeserializer
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
name|Soap11
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
name|Soap12
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
name|binding
operator|.
name|soap
operator|.
name|SoapMessage
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
name|jms
operator|.
name|interceptor
operator|.
name|SoapJMSInInterceptor
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
name|model
operator|.
name|SoapOperationInfo
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
name|interceptor
operator|.
name|Fault
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
name|Phase
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
name|ws
operator|.
name|addressing
operator|.
name|JAXWSAConstants
import|;
end_import

begin_class
specifier|public
class|class
name|SoapActionInInterceptor
extends|extends
name|AbstractSoapInterceptor
block|{
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
name|SoapActionInInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ALLOW_NON_MATCHING_TO_DEFAULT
init|=
literal|"allowNonMatchingToDefaultSoapAction"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CALCULATED_WSA_ACTION
init|=
name|SoapActionInInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".ACTION"
decl_stmt|;
specifier|public
name|SoapActionInInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|READ
argument_list|)
expr_stmt|;
name|addAfter
argument_list|(
name|ReadHeadersInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|addAfter
argument_list|(
name|EndpointSelectionInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|String
name|getSoapAction
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|m
operator|instanceof
name|SoapMessage
operator|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|SoapMessage
name|message
init|=
operator|(
name|SoapMessage
operator|)
name|m
decl_stmt|;
if|if
condition|(
name|message
operator|.
name|getVersion
argument_list|()
operator|instanceof
name|Soap11
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
init|=
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
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|headers
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|sa
init|=
name|headers
operator|.
name|get
argument_list|(
name|SoapBindingConstants
operator|.
name|SOAP_ACTION
argument_list|)
decl_stmt|;
if|if
condition|(
name|sa
operator|!=
literal|null
operator|&&
operator|!
name|sa
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
name|action
init|=
name|sa
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|action
operator|.
name|startsWith
argument_list|(
literal|"\""
argument_list|)
operator|||
name|action
operator|.
name|startsWith
argument_list|(
literal|"\'"
argument_list|)
condition|)
block|{
name|action
operator|=
name|action
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|action
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|action
return|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|message
operator|.
name|getVersion
argument_list|()
operator|instanceof
name|Soap12
condition|)
block|{
name|String
name|ct
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|ct
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|int
name|start
init|=
name|ct
operator|.
name|indexOf
argument_list|(
literal|"action="
argument_list|)
decl_stmt|;
if|if
condition|(
name|start
operator|==
operator|-
literal|1
operator|&&
name|ct
operator|.
name|indexOf
argument_list|(
literal|"multipart/related"
argument_list|)
operator|==
literal|0
operator|&&
name|ct
operator|.
name|indexOf
argument_list|(
literal|"start-info"
argument_list|)
operator|==
operator|-
literal|1
condition|)
block|{
comment|// the action property may not be found at the package's content-type for non-mtom multipart message
comment|// but skip searching if the start-info property is set
name|List
argument_list|<
name|String
argument_list|>
name|cts
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
call|(
name|List
argument_list|<
name|?
argument_list|>
call|)
argument_list|(
operator|(
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
name|AttachmentDeserializer
operator|.
name|ATTACHMENT_PART_HEADERS
argument_list|)
operator|)
operator|.
name|get
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|cts
operator|!=
literal|null
operator|&&
operator|!
name|cts
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|ct
operator|=
name|cts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|start
operator|=
name|ct
operator|.
name|indexOf
argument_list|(
literal|"action="
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|start
operator|!=
operator|-
literal|1
condition|)
block|{
name|int
name|end
decl_stmt|;
name|char
name|c
init|=
name|ct
operator|.
name|charAt
argument_list|(
name|start
operator|+
literal|7
argument_list|)
decl_stmt|;
comment|// handle the extraction robustly
if|if
condition|(
name|c
operator|==
literal|'\"'
condition|)
block|{
name|start
operator|+=
literal|8
expr_stmt|;
name|end
operator|=
name|ct
operator|.
name|indexOf
argument_list|(
literal|'\"'
argument_list|,
name|start
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|c
operator|==
literal|'\\'
operator|&&
name|ct
operator|.
name|charAt
argument_list|(
name|start
operator|+
literal|8
argument_list|)
operator|==
literal|'\"'
condition|)
block|{
name|start
operator|+=
literal|9
expr_stmt|;
name|end
operator|=
name|ct
operator|.
name|indexOf
argument_list|(
literal|'\\'
argument_list|,
name|start
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|start
operator|+=
literal|7
expr_stmt|;
name|end
operator|=
name|ct
operator|.
name|indexOf
argument_list|(
literal|';'
argument_list|,
name|start
argument_list|)
expr_stmt|;
if|if
condition|(
name|end
operator|==
operator|-
literal|1
condition|)
block|{
name|end
operator|=
name|ct
operator|.
name|length
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|ct
operator|.
name|substring
argument_list|(
name|start
argument_list|,
name|end
argument_list|)
return|;
block|}
block|}
comment|// Return the Soap Action for the JMS Case
if|if
condition|(
name|message
operator|.
name|containsKey
argument_list|(
name|SoapJMSInInterceptor
operator|.
name|JMS_SOAP_ACTION_VALUE
argument_list|)
condition|)
block|{
return|return
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|SoapJMSInInterceptor
operator|.
name|JMS_SOAP_ACTION_VALUE
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
throws|throws
name|Fault
block|{
if|if
condition|(
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
return|return;
block|}
name|String
name|action
init|=
name|getSoapAction
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|action
argument_list|)
condition|)
block|{
name|getAndSetOperation
argument_list|(
name|message
argument_list|,
name|action
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|SoapBindingConstants
operator|.
name|SOAP_ACTION
argument_list|,
name|action
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|getAndSetOperation
parameter_list|(
name|SoapMessage
name|message
parameter_list|,
name|String
name|action
parameter_list|)
block|{
name|getAndSetOperation
argument_list|(
name|message
argument_list|,
name|action
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|getAndSetOperation
parameter_list|(
name|SoapMessage
name|message
parameter_list|,
name|String
name|action
parameter_list|,
name|boolean
name|strict
parameter_list|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|action
argument_list|)
condition|)
block|{
return|return;
block|}
name|Exchange
name|ex
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|Endpoint
name|ep
init|=
name|ex
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
return|return;
block|}
name|BindingOperationInfo
name|bindingOp
init|=
literal|null
decl_stmt|;
name|Collection
argument_list|<
name|BindingOperationInfo
argument_list|>
name|bops
init|=
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|getOperations
argument_list|()
decl_stmt|;
if|if
condition|(
name|bops
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|BindingOperationInfo
name|boi
range|:
name|bops
control|)
block|{
if|if
condition|(
name|isActionMatch
argument_list|(
name|message
argument_list|,
name|boi
argument_list|,
name|action
argument_list|)
condition|)
block|{
if|if
condition|(
name|bindingOp
operator|!=
literal|null
condition|)
block|{
comment|//more than one op with the same action, will need to parse normally
return|return;
block|}
name|bindingOp
operator|=
name|boi
expr_stmt|;
block|}
if|if
condition|(
name|matchWSAAction
argument_list|(
name|boi
argument_list|,
name|action
argument_list|)
condition|)
block|{
if|if
condition|(
name|bindingOp
operator|!=
literal|null
operator|&&
name|bindingOp
operator|!=
name|boi
condition|)
block|{
comment|//more than one op with the same action, will need to parse normally
return|return;
block|}
name|bindingOp
operator|=
name|boi
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|bindingOp
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|strict
condition|)
block|{
comment|//we didn't match the an operation, we'll try again later to make
comment|//sure the incoming message did end up matching an operation.
comment|//This could occur in some cases like WS-RM and WS-SecConv that will
comment|//intercept the message with a new endpoint/operation
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|SoapActionInAttemptTwoInterceptor
argument_list|(
name|action
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
name|ex
operator|.
name|put
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|,
name|bindingOp
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|boolean
name|matchWSAAction
parameter_list|(
name|BindingOperationInfo
name|boi
parameter_list|,
name|String
name|action
parameter_list|)
block|{
name|Object
name|o
init|=
name|getWSAAction
argument_list|(
name|boi
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|!=
literal|null
condition|)
block|{
name|String
name|oa
init|=
name|o
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|action
operator|.
name|equals
argument_list|(
name|oa
argument_list|)
operator|||
name|action
operator|.
name|equals
argument_list|(
name|oa
operator|+
literal|"Request"
argument_list|)
operator|||
name|oa
operator|.
name|equals
argument_list|(
name|action
operator|+
literal|"Request"
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
specifier|private
specifier|static
name|String
name|getWSAAction
parameter_list|(
name|BindingOperationInfo
name|boi
parameter_list|)
block|{
name|Object
name|o
init|=
name|boi
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|getInput
argument_list|()
operator|.
name|getProperty
argument_list|(
name|CALCULATED_WSA_ACTION
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|==
literal|null
condition|)
block|{
name|o
operator|=
name|boi
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|getInput
argument_list|()
operator|.
name|getExtensionAttribute
argument_list|(
name|JAXWSAConstants
operator|.
name|WSAM_ACTION_QNAME
argument_list|)
expr_stmt|;
if|if
condition|(
name|o
operator|==
literal|null
condition|)
block|{
name|o
operator|=
name|boi
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|getInput
argument_list|()
operator|.
name|getExtensionAttribute
argument_list|(
name|JAXWSAConstants
operator|.
name|WSAW_ACTION_QNAME
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|o
operator|==
literal|null
condition|)
block|{
name|String
name|start
init|=
name|getActionBaseUri
argument_list|(
name|boi
operator|.
name|getOperationInfo
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|boi
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|getInputName
argument_list|()
condition|)
block|{
name|o
operator|=
name|addPath
argument_list|(
name|start
argument_list|,
name|boi
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|o
operator|=
name|addPath
argument_list|(
name|start
argument_list|,
name|boi
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|getInputName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|o
operator|!=
literal|null
condition|)
block|{
name|boi
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|getInput
argument_list|()
operator|.
name|setProperty
argument_list|(
name|CALCULATED_WSA_ACTION
argument_list|,
name|o
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|o
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|String
name|getActionBaseUri
parameter_list|(
specifier|final
name|OperationInfo
name|operation
parameter_list|)
block|{
name|String
name|interfaceName
init|=
name|operation
operator|.
name|getInterface
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
decl_stmt|;
return|return
name|addPath
argument_list|(
name|operation
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|interfaceName
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|String
name|getDelimiter
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
if|if
condition|(
name|uri
operator|.
name|startsWith
argument_list|(
literal|"urn"
argument_list|)
condition|)
block|{
return|return
literal|":"
return|;
block|}
return|return
literal|"/"
return|;
block|}
specifier|private
specifier|static
name|String
name|addPath
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|path
parameter_list|)
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|String
name|delimiter
init|=
name|getDelimiter
argument_list|(
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|uri
operator|.
name|endsWith
argument_list|(
name|delimiter
argument_list|)
operator|&&
operator|!
name|path
operator|.
name|startsWith
argument_list|(
name|delimiter
argument_list|)
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|delimiter
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|path
argument_list|)
expr_stmt|;
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
class|class
name|SoapActionInAttemptTwoInterceptor
extends|extends
name|AbstractSoapInterceptor
block|{
specifier|final
name|String
name|action
decl_stmt|;
specifier|public
name|SoapActionInAttemptTwoInterceptor
parameter_list|(
name|String
name|action
parameter_list|)
block|{
name|super
argument_list|(
name|action
argument_list|,
name|Phase
operator|.
name|PRE_LOGICAL
argument_list|)
expr_stmt|;
name|this
operator|.
name|action
operator|=
name|action
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|BindingOperationInfo
name|boi
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getBindingOperationInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|boi
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|action
argument_list|)
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|isActionMatch
argument_list|(
name|message
argument_list|,
name|boi
argument_list|,
name|action
argument_list|)
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|matchWSAAction
argument_list|(
name|boi
argument_list|,
name|action
argument_list|)
condition|)
block|{
return|return;
block|}
name|boolean
name|synthetic
init|=
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|boi
operator|.
name|getProperty
argument_list|(
literal|"operation.is.synthetic"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|synthetic
condition|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
literal|"SOAP_ACTION_MISMATCH"
argument_list|,
name|LOG
argument_list|,
literal|null
argument_list|,
name|action
argument_list|)
throw|;
block|}
block|}
block|}
specifier|private
specifier|static
name|boolean
name|isActionMatch
parameter_list|(
name|SoapMessage
name|message
parameter_list|,
name|BindingOperationInfo
name|boi
parameter_list|,
name|String
name|action
parameter_list|)
block|{
name|SoapOperationInfo
name|soi
init|=
name|boi
operator|.
name|getExtensor
argument_list|(
name|SoapOperationInfo
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|soi
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|boolean
name|allowNoMatchingToDefault
init|=
name|MessageUtils
operator|.
name|getContextualBoolean
argument_list|(
name|message
argument_list|,
name|ALLOW_NON_MATCHING_TO_DEFAULT
argument_list|,
literal|false
argument_list|)
decl_stmt|;
return|return
name|action
operator|.
name|equals
argument_list|(
name|soi
operator|.
name|getAction
argument_list|()
argument_list|)
operator|||
operator|(
name|allowNoMatchingToDefault
operator|&&
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|soi
operator|.
name|getAction
argument_list|()
argument_list|)
operator|||
operator|(
name|message
operator|.
name|getVersion
argument_list|()
operator|instanceof
name|Soap12
operator|)
operator|&&
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|soi
operator|.
name|getAction
argument_list|()
argument_list|)
operator|)
return|;
block|}
block|}
end_class

end_unit

