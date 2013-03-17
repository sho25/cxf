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
name|Date
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
name|ws
operator|.
name|addressing
operator|.
name|AddressingProperties
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
name|DefaultSymmetricBinding
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
name|STSClient
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
name|WSS4JInInterceptor
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
name|All
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
name|ExactlyOne
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
name|SPConstants
operator|.
name|SPVersion
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
name|Header
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
name|ProtectionToken
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
name|SecureConversationToken
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
name|SignedParts
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
name|xml
operator|.
name|security
operator|.
name|utils
operator|.
name|Base64
import|;
end_import

begin_class
class|class
name|SecureConversationInInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|SoapMessage
argument_list|>
block|{
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|SecureConversationInInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|SecureConversationInInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_PROTOCOL
argument_list|)
expr_stmt|;
block|}
specifier|private
name|AbstractBinding
name|getBinding
parameter_list|(
name|AssertionInfoMap
name|aim
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
name|SPConstants
operator|.
name|SYMMETRIC_BINDING
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
return|return
operator|(
name|AbstractBinding
operator|)
name|ais
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getAssertion
argument_list|()
return|;
block|}
name|ais
operator|=
name|NegotiationUtils
operator|.
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
return|return
operator|(
name|AbstractBinding
operator|)
name|ais
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getAssertion
argument_list|()
return|;
block|}
name|ais
operator|=
name|NegotiationUtils
operator|.
name|getAllAssertionsByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|TRANSPORT_BINDING
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
return|return
operator|(
name|AbstractBinding
operator|)
name|ais
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getAssertion
argument_list|()
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
name|AssertionInfoMap
name|aim
init|=
name|message
operator|.
name|get
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// extract Assertion information
if|if
condition|(
name|aim
operator|!=
literal|null
condition|)
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
name|SPConstants
operator|.
name|SECURE_CONVERSATION_TOKEN
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
comment|//client side should be checked on the way out
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
name|Object
name|s
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|STS_TOKEN_DO_CANCEL
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
operator|&&
operator|(
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|s
argument_list|)
operator|||
literal|"true"
operator|.
name|equalsIgnoreCase
argument_list|(
name|s
operator|.
name|toString
argument_list|()
argument_list|)
operator|)
condition|)
block|{
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|add
argument_list|(
name|SecureConversationCancelInterceptor
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
name|String
name|s
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|SoapBindingConstants
operator|.
name|SOAP_ACTION
argument_list|)
decl_stmt|;
name|String
name|addNs
init|=
literal|null
decl_stmt|;
name|AddressingProperties
name|inProps
init|=
operator|(
name|AddressingProperties
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|JAXWSAConstants
operator|.
name|SERVER_ADDRESSING_PROPERTIES_INBOUND
argument_list|)
decl_stmt|;
if|if
condition|(
name|inProps
operator|!=
literal|null
condition|)
block|{
name|addNs
operator|=
name|inProps
operator|.
name|getNamespaceURI
argument_list|()
expr_stmt|;
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
comment|//MS/WCF doesn't put a soap action out for this, must check the headers
name|s
operator|=
name|inProps
operator|.
name|getAction
argument_list|()
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|s
operator|!=
literal|null
operator|&&
name|s
operator|.
name|contains
argument_list|(
literal|"/RST/SCT"
argument_list|)
operator|&&
operator|(
name|s
operator|.
name|startsWith
argument_list|(
name|STSUtils
operator|.
name|WST_NS_05_02
argument_list|)
operator|||
name|s
operator|.
name|startsWith
argument_list|(
name|STSUtils
operator|.
name|WST_NS_05_12
argument_list|)
operator|)
condition|)
block|{
name|SecureConversationToken
name|tok
init|=
operator|(
name|SecureConversationToken
operator|)
name|ais
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getAssertion
argument_list|()
decl_stmt|;
name|Policy
name|pol
init|=
name|tok
operator|.
name|getBootstrapPolicy
argument_list|()
decl_stmt|;
if|if
condition|(
name|s
operator|.
name|endsWith
argument_list|(
literal|"Cancel"
argument_list|)
operator|||
name|s
operator|.
name|endsWith
argument_list|(
literal|"/Renew"
argument_list|)
condition|)
block|{
comment|//Cancel and Renew just sign with the token
name|Policy
name|p
init|=
operator|new
name|Policy
argument_list|()
decl_stmt|;
name|ExactlyOne
name|ea
init|=
operator|new
name|ExactlyOne
argument_list|()
decl_stmt|;
name|p
operator|.
name|addPolicyComponent
argument_list|(
name|ea
argument_list|)
expr_stmt|;
name|All
name|all
init|=
operator|new
name|All
argument_list|()
decl_stmt|;
name|Assertion
name|ass
init|=
name|NegotiationUtils
operator|.
name|getAddressingPolicy
argument_list|(
name|aim
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|all
operator|.
name|addPolicyComponent
argument_list|(
name|ass
argument_list|)
expr_stmt|;
name|ea
operator|.
name|addPolicyComponent
argument_list|(
name|all
argument_list|)
expr_stmt|;
specifier|final
name|SecureConversationToken
name|secureConversationToken
init|=
operator|new
name|SecureConversationToken
argument_list|(
name|SPConstants
operator|.
name|SPVersion
operator|.
name|SP12
argument_list|,
name|SPConstants
operator|.
name|IncludeTokenType
operator|.
name|INCLUDE_TOKEN_ALWAYS_TO_RECIPIENT
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|secureConversationToken
operator|.
name|setOptional
argument_list|(
literal|true
argument_list|)
expr_stmt|;
class|class
name|InternalProtectionToken
extends|extends
name|ProtectionToken
block|{
specifier|public
name|InternalProtectionToken
parameter_list|(
name|SPVersion
name|version
parameter_list|,
name|Policy
name|nestedPolicy
parameter_list|)
block|{
name|super
argument_list|(
name|version
argument_list|,
name|nestedPolicy
argument_list|)
expr_stmt|;
name|super
operator|.
name|setToken
argument_list|(
name|secureConversationToken
argument_list|)
expr_stmt|;
block|}
block|}
name|DefaultSymmetricBinding
name|binding
init|=
operator|new
name|DefaultSymmetricBinding
argument_list|(
name|SPConstants
operator|.
name|SPVersion
operator|.
name|SP12
argument_list|,
operator|new
name|Policy
argument_list|()
argument_list|)
decl_stmt|;
name|binding
operator|.
name|setProtectionToken
argument_list|(
operator|new
name|InternalProtectionToken
argument_list|(
name|SPConstants
operator|.
name|SPVersion
operator|.
name|SP12
argument_list|,
operator|new
name|Policy
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|binding
operator|.
name|setIncludeTimestamp
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|binding
operator|.
name|setOnlySignEntireHeadersAndBody
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|binding
operator|.
name|setProtectTokens
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|AbstractBinding
name|origBinding
init|=
name|getBinding
argument_list|(
name|aim
argument_list|)
decl_stmt|;
name|binding
operator|.
name|setAlgorithmSuite
argument_list|(
name|origBinding
operator|.
name|getAlgorithmSuite
argument_list|()
argument_list|)
expr_stmt|;
name|all
operator|.
name|addPolicyComponent
argument_list|(
name|binding
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Header
argument_list|>
name|headers
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|addNs
operator|!=
literal|null
condition|)
block|{
name|headers
operator|=
operator|new
name|ArrayList
argument_list|<
name|Header
argument_list|>
argument_list|()
expr_stmt|;
name|headers
operator|.
name|add
argument_list|(
operator|new
name|Header
argument_list|(
literal|"To"
argument_list|,
name|addNs
argument_list|)
argument_list|)
expr_stmt|;
name|headers
operator|.
name|add
argument_list|(
operator|new
name|Header
argument_list|(
literal|"From"
argument_list|,
name|addNs
argument_list|)
argument_list|)
expr_stmt|;
name|headers
operator|.
name|add
argument_list|(
operator|new
name|Header
argument_list|(
literal|"FaultTo"
argument_list|,
name|addNs
argument_list|)
argument_list|)
expr_stmt|;
name|headers
operator|.
name|add
argument_list|(
operator|new
name|Header
argument_list|(
literal|"ReplyTo"
argument_list|,
name|addNs
argument_list|)
argument_list|)
expr_stmt|;
name|headers
operator|.
name|add
argument_list|(
operator|new
name|Header
argument_list|(
literal|"Action"
argument_list|,
name|addNs
argument_list|)
argument_list|)
expr_stmt|;
name|headers
operator|.
name|add
argument_list|(
operator|new
name|Header
argument_list|(
literal|"MessageID"
argument_list|,
name|addNs
argument_list|)
argument_list|)
expr_stmt|;
name|headers
operator|.
name|add
argument_list|(
operator|new
name|Header
argument_list|(
literal|"RelatesTo"
argument_list|,
name|addNs
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|SignedParts
name|parts
init|=
operator|new
name|SignedParts
argument_list|(
name|SPConstants
operator|.
name|SPVersion
operator|.
name|SP12
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|all
operator|.
name|addPolicyComponent
argument_list|(
name|parts
argument_list|)
expr_stmt|;
name|pol
operator|=
name|p
expr_stmt|;
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|add
argument_list|(
name|SecureConversationTokenFinderInterceptor
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Policy
name|p
init|=
operator|new
name|Policy
argument_list|()
decl_stmt|;
name|ExactlyOne
name|ea
init|=
operator|new
name|ExactlyOne
argument_list|()
decl_stmt|;
name|p
operator|.
name|addPolicyComponent
argument_list|(
name|ea
argument_list|)
expr_stmt|;
name|All
name|all
init|=
operator|new
name|All
argument_list|()
decl_stmt|;
name|Assertion
name|ass
init|=
name|NegotiationUtils
operator|.
name|getAddressingPolicy
argument_list|(
name|aim
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|all
operator|.
name|addPolicyComponent
argument_list|(
name|ass
argument_list|)
expr_stmt|;
name|ea
operator|.
name|addPolicyComponent
argument_list|(
name|all
argument_list|)
expr_stmt|;
name|pol
operator|=
name|p
operator|.
name|merge
argument_list|(
name|pol
argument_list|)
expr_stmt|;
block|}
comment|//setup SCT endpoint and forward to it.
name|unmapSecurityProps
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|String
name|ns
init|=
name|STSUtils
operator|.
name|WST_NS_05_12
decl_stmt|;
if|if
condition|(
name|s
operator|.
name|startsWith
argument_list|(
name|STSUtils
operator|.
name|WST_NS_05_02
argument_list|)
condition|)
block|{
name|ns
operator|=
name|STSUtils
operator|.
name|WST_NS_05_02
expr_stmt|;
block|}
name|NegotiationUtils
operator|.
name|recalcEffectivePolicy
argument_list|(
name|message
argument_list|,
name|ns
argument_list|,
name|pol
argument_list|,
operator|new
name|SecureConversationSTSInvoker
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|add
argument_list|(
name|SecureConversationTokenFinderInterceptor
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
name|NegotiationUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|BOOTSTRAP_POLICY
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|unmapSecurityProps
parameter_list|(
name|Message
name|message
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
for|for
control|(
name|String
name|s
range|:
name|SecurityConstants
operator|.
name|ALL_PROPERTIES
control|)
block|{
name|Object
name|v
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|s
operator|+
literal|".sct"
argument_list|)
decl_stmt|;
if|if
condition|(
name|v
operator|!=
literal|null
condition|)
block|{
name|ex
operator|.
name|put
argument_list|(
name|s
argument_list|,
name|v
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
class|class
name|SecureConversationSTSInvoker
extends|extends
name|STSInvoker
block|{
name|void
name|doIssue
parameter_list|(
name|Element
name|requestEl
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Element
name|binaryExchange
parameter_list|,
name|W3CDOMStreamWriter
name|writer
parameter_list|,
name|String
name|prefix
parameter_list|,
name|String
name|namespace
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|STSUtils
operator|.
name|WST_NS_05_12
operator|.
name|equals
argument_list|(
name|namespace
argument_list|)
condition|)
block|{
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
literal|"RequestSecurityTokenResponseCollection"
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
literal|"RequestSecurityTokenResponse"
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
name|byte
name|clientEntropy
index|[]
init|=
literal|null
decl_stmt|;
name|int
name|keySize
init|=
literal|256
decl_stmt|;
name|long
name|ttl
init|=
literal|300000L
decl_stmt|;
name|String
name|tokenType
init|=
literal|null
decl_stmt|;
name|Element
name|el
init|=
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|requestEl
argument_list|)
decl_stmt|;
while|while
condition|(
name|el
operator|!=
literal|null
condition|)
block|{
name|String
name|localName
init|=
name|el
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
if|if
condition|(
name|namespace
operator|.
name|equals
argument_list|(
name|el
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
literal|"Entropy"
operator|.
name|equals
argument_list|(
name|localName
argument_list|)
condition|)
block|{
name|Element
name|bs
init|=
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|el
argument_list|)
decl_stmt|;
if|if
condition|(
name|bs
operator|!=
literal|null
condition|)
block|{
name|clientEntropy
operator|=
name|Base64
operator|.
name|decode
argument_list|(
name|bs
operator|.
name|getTextContent
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
literal|"KeySize"
operator|.
name|equals
argument_list|(
name|localName
argument_list|)
condition|)
block|{
name|keySize
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|el
operator|.
name|getTextContent
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"TokenType"
operator|.
name|equals
argument_list|(
name|localName
argument_list|)
condition|)
block|{
name|tokenType
operator|=
name|el
operator|.
name|getTextContent
argument_list|()
expr_stmt|;
block|}
block|}
name|el
operator|=
name|DOMUtils
operator|.
name|getNextElement
argument_list|(
name|el
argument_list|)
expr_stmt|;
block|}
comment|// Check received KeySize
if|if
condition|(
name|keySize
argument_list|<
literal|128
operator|||
name|keySize
argument_list|>
literal|512
condition|)
block|{
name|keySize
operator|=
literal|256
expr_stmt|;
block|}
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
literal|"RequestedSecurityToken"
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
name|SecurityContextToken
name|sct
init|=
operator|new
name|SecurityContextToken
argument_list|(
name|NegotiationUtils
operator|.
name|getWSCVersion
argument_list|(
name|tokenType
argument_list|)
argument_list|,
name|writer
operator|.
name|getDocument
argument_list|()
argument_list|)
decl_stmt|;
name|Date
name|created
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|Date
name|expires
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|expires
operator|.
name|setTime
argument_list|(
name|created
operator|.
name|getTime
argument_list|()
operator|+
name|ttl
argument_list|)
expr_stmt|;
name|SecurityToken
name|token
init|=
operator|new
name|SecurityToken
argument_list|(
name|sct
operator|.
name|getIdentifier
argument_list|()
argument_list|,
name|created
argument_list|,
name|expires
argument_list|)
decl_stmt|;
name|token
operator|.
name|setToken
argument_list|(
name|sct
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
name|token
operator|.
name|setTokenType
argument_list|(
name|sct
operator|.
name|getTokenType
argument_list|()
argument_list|)
expr_stmt|;
name|writer
operator|.
name|getCurrentNode
argument_list|()
operator|.
name|appendChild
argument_list|(
name|sct
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
literal|"RequestedAttachedReference"
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
name|token
operator|.
name|setAttachedReference
argument_list|(
name|writeSecurityTokenReference
argument_list|(
name|writer
argument_list|,
literal|"#"
operator|+
name|sct
operator|.
name|getID
argument_list|()
argument_list|,
name|tokenType
argument_list|)
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
literal|"RequestedUnattachedReference"
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
name|token
operator|.
name|setUnattachedReference
argument_list|(
name|writeSecurityTokenReference
argument_list|(
name|writer
argument_list|,
name|sct
operator|.
name|getIdentifier
argument_list|()
argument_list|,
name|tokenType
argument_list|)
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
name|writeLifetime
argument_list|(
name|writer
argument_list|,
name|created
argument_list|,
name|expires
argument_list|,
name|prefix
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
name|byte
index|[]
name|secret
init|=
name|writeProofToken
argument_list|(
name|prefix
argument_list|,
name|namespace
argument_list|,
name|writer
argument_list|,
name|clientEntropy
argument_list|,
name|keySize
argument_list|)
decl_stmt|;
name|token
operator|.
name|setSecret
argument_list|(
name|secret
argument_list|)
expr_stmt|;
operator|(
operator|(
name|TokenStore
operator|)
name|exchange
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getProperty
argument_list|(
name|TokenStore
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|)
operator|.
name|add
argument_list|(
name|token
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
if|if
condition|(
name|STSUtils
operator|.
name|WST_NS_05_12
operator|.
name|equals
argument_list|(
name|namespace
argument_list|)
condition|)
block|{
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|static
specifier|final
class|class
name|SecureConversationTokenFinderInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|SoapMessage
argument_list|>
block|{
specifier|static
specifier|final
name|SecureConversationTokenFinderInterceptor
name|INSTANCE
init|=
operator|new
name|SecureConversationTokenFinderInterceptor
argument_list|()
decl_stmt|;
specifier|private
name|SecureConversationTokenFinderInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_PROTOCOL
argument_list|)
expr_stmt|;
name|addAfter
argument_list|(
name|WSS4JInInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
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
name|boolean
name|foundSCT
init|=
name|NegotiationUtils
operator|.
name|parseSCTResult
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|AssertionInfoMap
name|aim
init|=
name|message
operator|.
name|get
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// extract Assertion information
if|if
condition|(
name|aim
operator|!=
literal|null
condition|)
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
name|SPConstants
operator|.
name|SECURE_CONVERSATION_TOKEN
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
for|for
control|(
name|AssertionInfo
name|inf
range|:
name|ais
control|)
block|{
if|if
condition|(
name|foundSCT
condition|)
block|{
name|inf
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|inf
operator|.
name|setNotAsserted
argument_list|(
literal|"No SecureConversation token found in message."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
specifier|static
class|class
name|SecureConversationCancelInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|SoapMessage
argument_list|>
block|{
specifier|static
specifier|final
name|SecureConversationCancelInterceptor
name|INSTANCE
init|=
operator|new
name|SecureConversationCancelInterceptor
argument_list|()
decl_stmt|;
specifier|public
name|SecureConversationCancelInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|POST_LOGICAL
argument_list|)
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
comment|// TODO Auto-generated method stub
name|AssertionInfoMap
name|aim
init|=
name|message
operator|.
name|get
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// extract Assertion information
if|if
condition|(
name|aim
operator|==
literal|null
condition|)
block|{
return|return;
block|}
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
name|SPConstants
operator|.
name|SECURE_CONVERSATION_TOKEN
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|SecureConversationToken
name|tok
init|=
operator|(
name|SecureConversationToken
operator|)
name|ais
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getAssertion
argument_list|()
decl_stmt|;
name|doCancel
argument_list|(
name|message
argument_list|,
name|aim
argument_list|,
name|tok
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doCancel
parameter_list|(
name|SoapMessage
name|message
parameter_list|,
name|AssertionInfoMap
name|aim
parameter_list|,
name|SecureConversationToken
name|itok
parameter_list|)
block|{
name|Message
name|m2
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getOutMessage
argument_list|()
decl_stmt|;
name|SecurityToken
name|tok
init|=
operator|(
name|SecurityToken
operator|)
name|m2
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN
argument_list|)
decl_stmt|;
if|if
condition|(
name|tok
operator|==
literal|null
condition|)
block|{
name|String
name|tokId
init|=
operator|(
name|String
operator|)
name|m2
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN_ID
argument_list|)
decl_stmt|;
if|if
condition|(
name|tokId
operator|!=
literal|null
condition|)
block|{
name|tok
operator|=
name|NegotiationUtils
operator|.
name|getTokenStore
argument_list|(
name|m2
argument_list|)
operator|.
name|getToken
argument_list|(
name|tokId
argument_list|)
expr_stmt|;
block|}
block|}
name|STSClient
name|client
init|=
name|STSUtils
operator|.
name|getClient
argument_list|(
name|m2
argument_list|,
literal|"sct"
argument_list|)
decl_stmt|;
name|AddressingProperties
name|maps
init|=
operator|(
name|AddressingProperties
operator|)
name|message
operator|.
name|get
argument_list|(
literal|"javax.xml.ws.addressing.context.inbound"
argument_list|)
decl_stmt|;
if|if
condition|(
name|maps
operator|==
literal|null
condition|)
block|{
name|maps
operator|=
operator|(
name|AddressingProperties
operator|)
name|m2
operator|.
name|get
argument_list|(
literal|"javax.xml.ws.addressing.context"
argument_list|)
expr_stmt|;
block|}
synchronized|synchronized
init|(
name|client
init|)
block|{
try|try
block|{
name|SecureConversationTokenInterceptorProvider
operator|.
name|setupClient
argument_list|(
name|client
argument_list|,
name|message
argument_list|,
name|aim
argument_list|,
name|itok
argument_list|,
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|maps
operator|!=
literal|null
condition|)
block|{
name|client
operator|.
name|setAddressingNamespace
argument_list|(
name|maps
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|client
operator|.
name|cancelSecurityToken
argument_list|(
name|tok
argument_list|)
expr_stmt|;
name|NegotiationUtils
operator|.
name|getTokenStore
argument_list|(
name|m2
argument_list|)
operator|.
name|remove
argument_list|(
name|tok
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|m2
operator|.
name|setContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|client
operator|.
name|setTrust
argument_list|(
operator|(
name|Trust10
operator|)
literal|null
argument_list|)
expr_stmt|;
name|client
operator|.
name|setTrust
argument_list|(
operator|(
name|Trust13
operator|)
literal|null
argument_list|)
expr_stmt|;
name|client
operator|.
name|setTemplate
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|client
operator|.
name|setLocation
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|client
operator|.
name|setAddressingNamespace
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

