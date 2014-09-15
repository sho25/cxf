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
name|ws
operator|.
name|security
operator|.
name|policy
operator|.
name|interceptors
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
name|HashSet
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
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|CallbackHandler
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
name|interceptor
operator|.
name|Interceptor
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
name|security
operator|.
name|SecurityContext
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
name|invoker
operator|.
name|Invoker
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
name|transport
operator|.
name|Destination
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
name|MAPAggregator
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
name|policy
operator|.
name|MetadataConstants
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
name|policy
operator|.
name|AssertionInfo
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
name|policy
operator|.
name|AssertionInfoMap
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
name|policy
operator|.
name|EndpointPolicy
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
name|policy
operator|.
name|PolicyEngine
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
name|policy
operator|.
name|builder
operator|.
name|primitive
operator|.
name|PrimitiveAssertion
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
name|security
operator|.
name|SecurityConstants
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
name|security
operator|.
name|tokenstore
operator|.
name|SecurityToken
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
name|security
operator|.
name|tokenstore
operator|.
name|TokenStore
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
name|security
operator|.
name|trust
operator|.
name|STSUtils
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
name|security
operator|.
name|wss4j
operator|.
name|WSS4JUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Assertion
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Policy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|derivedKey
operator|.
name|ConversationConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|ext
operator|.
name|WSSecurityException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|WSConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|WSSecurityEngineResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|handler
operator|.
name|WSHandlerConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|handler
operator|.
name|WSHandlerResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|message
operator|.
name|token
operator|.
name|SecurityContextToken
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|policy
operator|.
name|SP11Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|policy
operator|.
name|SP12Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|policy
operator|.
name|SPConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|policy
operator|.
name|model
operator|.
name|AbstractBinding
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|policy
operator|.
name|model
operator|.
name|AlgorithmSuite
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|policy
operator|.
name|model
operator|.
name|Trust10
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|policy
operator|.
name|model
operator|.
name|Trust13
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|stax
operator|.
name|securityEvent
operator|.
name|WSSecurityEventConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xml
operator|.
name|security
operator|.
name|stax
operator|.
name|securityEvent
operator|.
name|SecurityEvent
import|;
end_import

begin_comment
comment|/**  * This is a collection of utility methods for use in negotiation exchanges such as WS-SecureConversation   * and WS-Trust for SPNEGO.  */
end_comment

begin_class
specifier|final
class|class
name|NegotiationUtils
block|{
specifier|private
name|NegotiationUtils
parameter_list|()
block|{
comment|// complete
block|}
specifier|static
name|Trust10
name|getTrust10
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|)
block|{
name|AssertionInfo
name|ai
init|=
name|getFirstAssertionByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|TRUST_10
argument_list|)
decl_stmt|;
if|if
condition|(
name|ai
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|(
name|Trust10
operator|)
name|ai
operator|.
name|getAssertion
argument_list|()
return|;
block|}
specifier|static
name|Trust13
name|getTrust13
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|)
block|{
name|AssertionInfo
name|ai
init|=
name|getFirstAssertionByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|TRUST_13
argument_list|)
decl_stmt|;
if|if
condition|(
name|ai
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|(
name|Trust13
operator|)
name|ai
operator|.
name|getAssertion
argument_list|()
return|;
block|}
specifier|static
name|TokenStore
name|getTokenStore
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|WSS4JUtils
operator|.
name|getTokenStore
argument_list|(
name|message
argument_list|)
return|;
block|}
specifier|static
name|Assertion
name|getAddressingPolicy
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|boolean
name|optional
parameter_list|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|lst
init|=
name|aim
operator|.
name|get
argument_list|(
name|MetadataConstants
operator|.
name|USING_ADDRESSING_2004_QNAME
argument_list|)
decl_stmt|;
name|Assertion
name|assertion
init|=
literal|null
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|lst
operator|&&
operator|!
name|lst
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|assertion
operator|=
name|lst
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getAssertion
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|assertion
operator|==
literal|null
condition|)
block|{
name|lst
operator|=
name|aim
operator|.
name|get
argument_list|(
name|MetadataConstants
operator|.
name|USING_ADDRESSING_2005_QNAME
argument_list|)
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|lst
operator|&&
operator|!
name|lst
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|assertion
operator|=
name|lst
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getAssertion
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|assertion
operator|==
literal|null
condition|)
block|{
name|lst
operator|=
name|aim
operator|.
name|get
argument_list|(
name|MetadataConstants
operator|.
name|USING_ADDRESSING_2006_QNAME
argument_list|)
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|lst
operator|&&
operator|!
name|lst
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|assertion
operator|=
name|lst
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getAssertion
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|assertion
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|PrimitiveAssertion
argument_list|(
name|MetadataConstants
operator|.
name|USING_ADDRESSING_2006_QNAME
argument_list|,
name|optional
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|optional
condition|)
block|{
return|return
operator|new
name|PrimitiveAssertion
argument_list|(
name|assertion
operator|.
name|getName
argument_list|()
argument_list|,
name|optional
argument_list|)
return|;
block|}
return|return
name|assertion
return|;
block|}
specifier|static
name|AlgorithmSuite
name|getAlgorithmSuite
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|)
block|{
name|AbstractBinding
name|transport
init|=
literal|null
decl_stmt|;
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
name|getAllAssertionsByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|TRANSPORT_BINDING
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|transport
operator|=
operator|(
name|AbstractBinding
operator|)
name|ai
operator|.
name|getAssertion
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|ais
operator|=
name|getAllAssertionsByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|ASYMMETRIC_BINDING
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|transport
operator|=
operator|(
name|AbstractBinding
operator|)
name|ai
operator|.
name|getAssertion
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|ais
operator|=
name|getAllAssertionsByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|SYMMETRIC_BINDING
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|transport
operator|=
operator|(
name|AbstractBinding
operator|)
name|ai
operator|.
name|getAssertion
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
name|transport
operator|!=
literal|null
condition|)
block|{
return|return
name|transport
operator|.
name|getAlgorithmSuite
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|static
name|int
name|getWSCVersion
parameter_list|(
name|String
name|tokenTypeValue
parameter_list|)
throws|throws
name|WSSecurityException
block|{
if|if
condition|(
name|tokenTypeValue
operator|==
literal|null
condition|)
block|{
return|return
name|ConversationConstants
operator|.
name|DEFAULT_VERSION
return|;
block|}
if|if
condition|(
name|tokenTypeValue
operator|.
name|startsWith
argument_list|(
name|ConversationConstants
operator|.
name|WSC_NS_05_02
argument_list|)
condition|)
block|{
return|return
name|ConversationConstants
operator|.
name|getWSTVersion
argument_list|(
name|ConversationConstants
operator|.
name|WSC_NS_05_02
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|tokenTypeValue
operator|.
name|startsWith
argument_list|(
name|ConversationConstants
operator|.
name|WSC_NS_05_12
argument_list|)
condition|)
block|{
return|return
name|ConversationConstants
operator|.
name|getWSTVersion
argument_list|(
name|ConversationConstants
operator|.
name|WSC_NS_05_12
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|ErrorCode
operator|.
name|FAILURE
argument_list|,
literal|"unsupportedSecConvVersion"
argument_list|)
throw|;
block|}
block|}
specifier|static
name|void
name|recalcEffectivePolicy
parameter_list|(
name|SoapMessage
name|message
parameter_list|,
name|String
name|namespace
parameter_list|,
name|Policy
name|policy
parameter_list|,
name|Invoker
name|invoker
parameter_list|,
name|boolean
name|secConv
parameter_list|)
block|{
name|Exchange
name|ex
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
name|ex
operator|.
name|getBus
argument_list|()
decl_stmt|;
name|PolicyEngine
name|pe
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|PolicyEngine
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|pe
condition|)
block|{
return|return;
block|}
name|Destination
name|destination
init|=
name|ex
operator|.
name|getDestination
argument_list|()
decl_stmt|;
try|try
block|{
name|Endpoint
name|endpoint
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|TokenStore
name|store
init|=
name|getTokenStore
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|secConv
condition|)
block|{
name|endpoint
operator|=
name|STSUtils
operator|.
name|createSCEndpoint
argument_list|(
name|bus
argument_list|,
name|namespace
argument_list|,
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getTransportId
argument_list|()
argument_list|,
name|destination
operator|.
name|getAddress
argument_list|()
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|,
name|message
operator|.
name|getVersion
argument_list|()
operator|.
name|getBindingId
argument_list|()
argument_list|,
name|policy
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|endpoint
operator|=
name|STSUtils
operator|.
name|createSTSEndpoint
argument_list|(
name|bus
argument_list|,
name|namespace
argument_list|,
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getTransportId
argument_list|()
argument_list|,
name|destination
operator|.
name|getAddress
argument_list|()
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|,
name|message
operator|.
name|getVersion
argument_list|()
operator|.
name|getBindingId
argument_list|()
argument_list|,
name|policy
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|setProperty
argument_list|(
name|TokenStore
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|store
argument_list|)
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|TokenStore
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|store
argument_list|)
expr_stmt|;
name|EndpointPolicy
name|ep
init|=
name|pe
operator|.
name|getServerEndpointPolicy
argument_list|(
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
argument_list|,
name|destination
argument_list|,
name|message
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|interceptors
init|=
name|ep
operator|.
name|getInterceptors
argument_list|(
name|message
argument_list|)
decl_stmt|;
for|for
control|(
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
name|i
range|:
name|interceptors
control|)
block|{
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|add
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
name|Collection
argument_list|<
name|Assertion
argument_list|>
name|assertions
init|=
name|ep
operator|.
name|getVocabulary
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|assertions
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|,
operator|new
name|AssertionInfoMap
argument_list|(
name|assertions
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|endpoint
operator|.
name|getService
argument_list|()
operator|.
name|setInvoker
argument_list|(
name|invoker
argument_list|)
expr_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|Service
operator|.
name|class
argument_list|,
name|endpoint
operator|.
name|getService
argument_list|()
argument_list|)
expr_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|Binding
operator|.
name|class
argument_list|,
name|endpoint
operator|.
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
name|ex
operator|.
name|remove
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|MAPAggregator
operator|.
name|ACTION_VERIFIED
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|exc
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|exc
argument_list|)
throw|;
block|}
block|}
comment|/**      * Return true on successfully parsing a SecurityContextToken result      */
specifier|static
name|boolean
name|parseSCTResult
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{
name|List
argument_list|<
name|WSHandlerResult
argument_list|>
name|results
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|message
operator|.
name|get
argument_list|(
name|WSHandlerConstants
operator|.
name|RECV_RESULTS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|results
operator|==
literal|null
condition|)
block|{
comment|// Try Streaming results
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|final
name|List
argument_list|<
name|SecurityEvent
argument_list|>
name|incomingEventList
init|=
operator|(
name|List
argument_list|<
name|SecurityEvent
argument_list|>
operator|)
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|SecurityEvent
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".in"
argument_list|)
decl_stmt|;
if|if
condition|(
name|incomingEventList
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|SecurityEvent
name|incomingEvent
range|:
name|incomingEventList
control|)
block|{
if|if
condition|(
name|WSSecurityEventConstants
operator|.
name|SecurityContextToken
operator|==
name|incomingEvent
operator|.
name|getSecurityEventType
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
for|for
control|(
name|WSHandlerResult
name|rResult
range|:
name|results
control|)
block|{
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|wsSecEngineResults
init|=
name|rResult
operator|.
name|getResults
argument_list|()
decl_stmt|;
for|for
control|(
name|WSSecurityEngineResult
name|wser
range|:
name|wsSecEngineResults
control|)
block|{
name|Integer
name|actInt
init|=
operator|(
name|Integer
operator|)
name|wser
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_ACTION
argument_list|)
decl_stmt|;
if|if
condition|(
name|actInt
operator|.
name|intValue
argument_list|()
operator|==
name|WSConstants
operator|.
name|SCT
condition|)
block|{
name|SecurityContextToken
name|tok
init|=
operator|(
name|SecurityContextToken
operator|)
name|wser
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_SECURITY_CONTEXT_TOKEN
argument_list|)
decl_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN_ID
argument_list|,
name|tok
operator|.
name|getIdentifier
argument_list|()
argument_list|)
expr_stmt|;
name|SecurityToken
name|token
init|=
name|getTokenStore
argument_list|(
name|message
argument_list|)
operator|.
name|getToken
argument_list|(
name|tok
operator|.
name|getIdentifier
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|token
operator|==
literal|null
operator|||
name|token
operator|.
name|isExpired
argument_list|()
condition|)
block|{
name|byte
index|[]
name|secret
init|=
operator|(
name|byte
index|[]
operator|)
name|wser
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_SECRET
argument_list|)
decl_stmt|;
if|if
condition|(
name|secret
operator|!=
literal|null
condition|)
block|{
name|token
operator|=
operator|new
name|SecurityToken
argument_list|(
name|tok
operator|.
name|getIdentifier
argument_list|()
argument_list|)
expr_stmt|;
name|token
operator|.
name|setToken
argument_list|(
name|tok
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
name|token
operator|.
name|setSecret
argument_list|(
name|secret
argument_list|)
expr_stmt|;
name|token
operator|.
name|setTokenType
argument_list|(
name|tok
operator|.
name|getTokenType
argument_list|()
argument_list|)
expr_stmt|;
name|getTokenStore
argument_list|(
name|message
argument_list|)
operator|.
name|add
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|token
operator|!=
literal|null
condition|)
block|{
specifier|final
name|SecurityContext
name|sc
init|=
name|token
operator|.
name|getSecurityContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|sc
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|,
name|sc
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|static
name|CallbackHandler
name|getCallbackHandler
parameter_list|(
name|Object
name|o
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
name|CallbackHandler
name|handler
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|CallbackHandler
condition|)
block|{
name|handler
operator|=
operator|(
name|CallbackHandler
operator|)
name|o
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|String
condition|)
block|{
try|try
block|{
name|handler
operator|=
operator|(
name|CallbackHandler
operator|)
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
operator|(
name|String
operator|)
name|o
argument_list|,
name|clazz
argument_list|)
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|handler
operator|=
literal|null
expr_stmt|;
block|}
block|}
return|return
name|handler
return|;
block|}
specifier|static
name|boolean
name|assertPolicy
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|QName
name|name
parameter_list|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
name|aim
operator|.
name|getAssertionInfo
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|!=
literal|null
operator|&&
operator|!
name|ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|static
name|boolean
name|assertPolicy
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|String
name|localname
parameter_list|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
name|NegotiationUtils
operator|.
name|getAllAssertionsByLocalname
argument_list|(
name|aim
argument_list|,
name|localname
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|static
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|getAllAssertionsByLocalname
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|String
name|localname
parameter_list|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|sp11Ais
init|=
name|aim
operator|.
name|get
argument_list|(
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|localname
argument_list|)
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|sp12Ais
init|=
name|aim
operator|.
name|get
argument_list|(
operator|new
name|QName
argument_list|(
name|SP12Constants
operator|.
name|SP_NS
argument_list|,
name|localname
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|sp11Ais
operator|!=
literal|null
operator|&&
operator|!
name|sp11Ais
operator|.
name|isEmpty
argument_list|()
operator|)
operator|||
operator|(
name|sp12Ais
operator|!=
literal|null
operator|&&
operator|!
name|sp12Ais
operator|.
name|isEmpty
argument_list|()
operator|)
condition|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
operator|new
name|HashSet
argument_list|<
name|AssertionInfo
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|sp11Ais
operator|!=
literal|null
condition|)
block|{
name|ais
operator|.
name|addAll
argument_list|(
name|sp11Ais
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sp12Ais
operator|!=
literal|null
condition|)
block|{
name|ais
operator|.
name|addAll
argument_list|(
name|sp12Ais
argument_list|)
expr_stmt|;
block|}
return|return
name|ais
return|;
block|}
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
specifier|static
name|AssertionInfo
name|getFirstAssertionByLocalname
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|String
name|localname
parameter_list|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|sp11Ais
init|=
name|aim
operator|.
name|get
argument_list|(
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|localname
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|sp11Ais
operator|!=
literal|null
operator|&&
operator|!
name|sp11Ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|sp11Ais
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|sp12Ais
init|=
name|aim
operator|.
name|get
argument_list|(
operator|new
name|QName
argument_list|(
name|SP12Constants
operator|.
name|SP_NS
argument_list|,
name|localname
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|sp12Ais
operator|!=
literal|null
operator|&&
operator|!
name|sp12Ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|sp12Ais
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|static
name|boolean
name|isThereAnAssertionByLocalname
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|String
name|localname
parameter_list|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|sp11Ais
init|=
name|aim
operator|.
name|get
argument_list|(
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|localname
argument_list|)
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|sp12Ais
init|=
name|aim
operator|.
name|get
argument_list|(
operator|new
name|QName
argument_list|(
name|SP12Constants
operator|.
name|SP_NS
argument_list|,
name|localname
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|sp11Ais
operator|!=
literal|null
operator|&&
operator|!
name|sp11Ais
operator|.
name|isEmpty
argument_list|()
operator|)
operator|||
operator|(
name|sp12Ais
operator|!=
literal|null
operator|&&
operator|!
name|sp12Ais
operator|.
name|isEmpty
argument_list|()
operator|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

