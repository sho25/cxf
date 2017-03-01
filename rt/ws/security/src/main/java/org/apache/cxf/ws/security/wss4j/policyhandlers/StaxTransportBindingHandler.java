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
name|wss4j
operator|.
name|policyhandlers
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
name|soap
operator|.
name|SOAPException
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
name|policy
operator|.
name|PolicyUtils
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
name|TokenStoreUtils
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
name|TokenStoreCallbackHandler
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
name|AbstractToken
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
name|AbstractToken
operator|.
name|DerivedKeys
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
operator|.
name|AlgorithmSuiteType
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
name|IssuedToken
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
name|KerberosToken
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
name|KeyValueToken
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
name|SamlToken
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
name|model
operator|.
name|SignedElements
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
name|SpnegoContextToken
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
name|SupportingTokens
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
name|TransportBinding
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
name|TransportToken
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
name|UsernameToken
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
name|X509Token
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
name|XPath
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
name|ext
operator|.
name|WSSConstants
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
name|ext
operator|.
name|WSSSecurityProperties
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
name|ext
operator|.
name|OutboundSecurityContext
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
name|ext
operator|.
name|SecurePart
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
name|ext
operator|.
name|SecurePart
operator|.
name|Modifier
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|StaxTransportBindingHandler
extends|extends
name|AbstractStaxBindingHandler
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
name|StaxTransportBindingHandler
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|TransportBinding
name|tbinding
decl_stmt|;
specifier|public
name|StaxTransportBindingHandler
parameter_list|(
name|WSSSecurityProperties
name|properties
parameter_list|,
name|SoapMessage
name|msg
parameter_list|,
name|TransportBinding
name|tbinding
parameter_list|,
name|OutboundSecurityContext
name|outboundSecurityContext
parameter_list|)
block|{
name|super
argument_list|(
name|properties
argument_list|,
name|msg
argument_list|,
name|tbinding
argument_list|,
name|outboundSecurityContext
argument_list|)
expr_stmt|;
name|this
operator|.
name|tbinding
operator|=
name|tbinding
expr_stmt|;
block|}
specifier|public
name|void
name|handleBinding
parameter_list|()
block|{
name|AssertionInfoMap
name|aim
init|=
name|getMessage
argument_list|()
operator|.
name|get
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
decl_stmt|;
name|configureTimestamp
argument_list|(
name|aim
argument_list|)
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|isRequestor
argument_list|()
condition|)
block|{
if|if
condition|(
name|tbinding
operator|!=
literal|null
condition|)
block|{
name|assertPolicy
argument_list|(
name|tbinding
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|asymSignatureAlgorithm
init|=
operator|(
name|String
operator|)
name|getMessage
argument_list|()
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|ASYMMETRIC_SIGNATURE_ALGORITHM
argument_list|)
decl_stmt|;
if|if
condition|(
name|asymSignatureAlgorithm
operator|!=
literal|null
operator|&&
name|tbinding
operator|.
name|getAlgorithmSuite
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|tbinding
operator|.
name|getAlgorithmSuite
argument_list|()
operator|.
name|setAsymmetricSignature
argument_list|(
name|asymSignatureAlgorithm
argument_list|)
expr_stmt|;
block|}
name|String
name|symSignatureAlgorithm
init|=
operator|(
name|String
operator|)
name|getMessage
argument_list|()
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|SYMMETRIC_SIGNATURE_ALGORITHM
argument_list|)
decl_stmt|;
if|if
condition|(
name|symSignatureAlgorithm
operator|!=
literal|null
operator|&&
name|tbinding
operator|.
name|getAlgorithmSuite
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|tbinding
operator|.
name|getAlgorithmSuite
argument_list|()
operator|.
name|setSymmetricSignature
argument_list|(
name|symSignatureAlgorithm
argument_list|)
expr_stmt|;
block|}
name|TransportToken
name|token
init|=
name|tbinding
operator|.
name|getTransportToken
argument_list|()
decl_stmt|;
if|if
condition|(
name|token
operator|.
name|getToken
argument_list|()
operator|instanceof
name|IssuedToken
condition|)
block|{
name|SecurityToken
name|secToken
init|=
name|getSecurityToken
argument_list|()
decl_stmt|;
if|if
condition|(
name|secToken
operator|==
literal|null
condition|)
block|{
name|unassertPolicy
argument_list|(
name|token
operator|.
name|getToken
argument_list|()
argument_list|,
literal|"No transport token id"
argument_list|)
expr_stmt|;
return|return;
block|}
name|addIssuedToken
argument_list|(
operator|(
name|IssuedToken
operator|)
name|token
operator|.
name|getToken
argument_list|()
argument_list|,
name|secToken
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|assertToken
argument_list|(
name|token
operator|.
name|getToken
argument_list|()
argument_list|)
expr_stmt|;
name|assertTokenWrapper
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|handleNonEndorsingSupportingTokens
argument_list|(
name|aim
argument_list|)
expr_stmt|;
name|handleEndorsingSupportingTokens
argument_list|(
name|aim
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
try|try
block|{
name|handleNonEndorsingSupportingTokens
argument_list|(
name|aim
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|tbinding
operator|!=
literal|null
condition|)
block|{
name|assertPolicy
argument_list|(
name|tbinding
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|tbinding
operator|.
name|getTransportToken
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|assertTokenWrapper
argument_list|(
name|tbinding
operator|.
name|getTransportToken
argument_list|()
argument_list|)
expr_stmt|;
name|assertToken
argument_list|(
name|tbinding
operator|.
name|getTransportToken
argument_list|()
operator|.
name|getToken
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|handleEndorsingSupportingTokens
argument_list|(
name|aim
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
name|addSignatureConfirmation
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
name|configureLayout
argument_list|(
name|aim
argument_list|)
expr_stmt|;
if|if
condition|(
name|tbinding
operator|!=
literal|null
condition|)
block|{
name|assertAlgorithmSuite
argument_list|(
name|tbinding
operator|.
name|getAlgorithmSuite
argument_list|()
argument_list|)
expr_stmt|;
name|assertWSSProperties
argument_list|(
name|tbinding
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrustProperties
argument_list|(
name|tbinding
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertPolicy
argument_list|(
name|SP12Constants
operator|.
name|SIGNED_PARTS
argument_list|)
expr_stmt|;
name|assertPolicy
argument_list|(
name|SP11Constants
operator|.
name|SIGNED_PARTS
argument_list|)
expr_stmt|;
name|assertPolicy
argument_list|(
name|SP12Constants
operator|.
name|ENCRYPTED_PARTS
argument_list|)
expr_stmt|;
name|assertPolicy
argument_list|(
name|SP11Constants
operator|.
name|ENCRYPTED_PARTS
argument_list|)
expr_stmt|;
name|putCustomTokenAfterSignature
argument_list|()
expr_stmt|;
block|}
comment|/**      * Handle the non-endorsing supporting tokens      */
specifier|private
name|void
name|handleNonEndorsingSupportingTokens
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|)
throws|throws
name|Exception
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
decl_stmt|;
name|ais
operator|=
name|PolicyUtils
operator|.
name|getAllAssertionsByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|SIGNED_SUPPORTING_TOKENS
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
name|SupportingTokens
name|sgndSuppTokens
init|=
operator|(
name|SupportingTokens
operator|)
name|ai
operator|.
name|getAssertion
argument_list|()
decl_stmt|;
if|if
condition|(
name|sgndSuppTokens
operator|!=
literal|null
condition|)
block|{
name|addSignedSupportingTokens
argument_list|(
name|sgndSuppTokens
argument_list|)
expr_stmt|;
block|}
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
name|ais
operator|=
name|PolicyUtils
operator|.
name|getAllAssertionsByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|SIGNED_ENCRYPTED_SUPPORTING_TOKENS
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
name|SupportingTokens
name|sgndSuppTokens
init|=
operator|(
name|SupportingTokens
operator|)
name|ai
operator|.
name|getAssertion
argument_list|()
decl_stmt|;
if|if
condition|(
name|sgndSuppTokens
operator|!=
literal|null
condition|)
block|{
name|addSignedSupportingTokens
argument_list|(
name|sgndSuppTokens
argument_list|)
expr_stmt|;
block|}
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
name|ais
operator|=
name|PolicyUtils
operator|.
name|getAllAssertionsByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|ENCRYPTED_SUPPORTING_TOKENS
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
name|SupportingTokens
name|encrSuppTokens
init|=
operator|(
name|SupportingTokens
operator|)
name|ai
operator|.
name|getAssertion
argument_list|()
decl_stmt|;
if|if
condition|(
name|encrSuppTokens
operator|!=
literal|null
condition|)
block|{
name|addSignedSupportingTokens
argument_list|(
name|encrSuppTokens
argument_list|)
expr_stmt|;
block|}
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
name|ais
operator|=
name|PolicyUtils
operator|.
name|getAllAssertionsByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|SUPPORTING_TOKENS
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
name|SupportingTokens
name|suppTokens
init|=
operator|(
name|SupportingTokens
operator|)
name|ai
operator|.
name|getAssertion
argument_list|()
decl_stmt|;
if|if
condition|(
name|suppTokens
operator|!=
literal|null
operator|&&
name|suppTokens
operator|.
name|getTokens
argument_list|()
operator|!=
literal|null
operator|&&
name|suppTokens
operator|.
name|getTokens
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|handleSupportingTokens
argument_list|(
name|suppTokens
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|addSignedSupportingTokens
parameter_list|(
name|SupportingTokens
name|sgndSuppTokens
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|AbstractToken
name|token
range|:
name|sgndSuppTokens
operator|.
name|getTokens
argument_list|()
control|)
block|{
name|assertToken
argument_list|(
name|token
argument_list|)
expr_stmt|;
if|if
condition|(
name|token
operator|!=
literal|null
operator|&&
operator|!
name|isTokenRequired
argument_list|(
name|token
operator|.
name|getIncludeTokenType
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|token
operator|instanceof
name|UsernameToken
condition|)
block|{
name|addUsernameToken
argument_list|(
operator|(
name|UsernameToken
operator|)
name|token
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|token
operator|instanceof
name|IssuedToken
condition|)
block|{
name|addIssuedToken
argument_list|(
operator|(
name|IssuedToken
operator|)
name|token
argument_list|,
name|getSecurityToken
argument_list|()
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|token
operator|instanceof
name|KerberosToken
condition|)
block|{
name|addKerberosToken
argument_list|(
operator|(
name|KerberosToken
operator|)
name|token
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|token
operator|instanceof
name|SamlToken
condition|)
block|{
name|addSamlToken
argument_list|(
operator|(
name|SamlToken
operator|)
name|token
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|token
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
name|token
operator|.
name|getName
argument_list|()
operator|+
literal|" is not supported in the streaming code"
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"A null token was supplied to the streaming code"
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**      * Handle the endorsing supporting tokens      */
specifier|private
name|void
name|handleEndorsingSupportingTokens
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|)
throws|throws
name|Exception
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
decl_stmt|;
name|ais
operator|=
name|PolicyUtils
operator|.
name|getAllAssertionsByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|SIGNED_ENDORSING_SUPPORTING_TOKENS
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
name|SupportingTokens
name|sgndSuppTokens
init|=
literal|null
decl_stmt|;
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|sgndSuppTokens
operator|=
operator|(
name|SupportingTokens
operator|)
name|ai
operator|.
name|getAssertion
argument_list|()
expr_stmt|;
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sgndSuppTokens
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|AbstractToken
name|token
range|:
name|sgndSuppTokens
operator|.
name|getTokens
argument_list|()
control|)
block|{
name|handleEndorsingToken
argument_list|(
name|token
argument_list|,
name|sgndSuppTokens
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|ais
operator|=
name|PolicyUtils
operator|.
name|getAllAssertionsByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|ENDORSING_SUPPORTING_TOKENS
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
name|SupportingTokens
name|endSuppTokens
init|=
literal|null
decl_stmt|;
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|endSuppTokens
operator|=
operator|(
name|SupportingTokens
operator|)
name|ai
operator|.
name|getAssertion
argument_list|()
expr_stmt|;
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endSuppTokens
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|AbstractToken
name|token
range|:
name|endSuppTokens
operator|.
name|getTokens
argument_list|()
control|)
block|{
name|handleEndorsingToken
argument_list|(
name|token
argument_list|,
name|endSuppTokens
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|ais
operator|=
name|PolicyUtils
operator|.
name|getAllAssertionsByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|ENDORSING_ENCRYPTED_SUPPORTING_TOKENS
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
name|SupportingTokens
name|endSuppTokens
init|=
literal|null
decl_stmt|;
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|endSuppTokens
operator|=
operator|(
name|SupportingTokens
operator|)
name|ai
operator|.
name|getAssertion
argument_list|()
expr_stmt|;
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endSuppTokens
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|AbstractToken
name|token
range|:
name|endSuppTokens
operator|.
name|getTokens
argument_list|()
control|)
block|{
name|handleEndorsingToken
argument_list|(
name|token
argument_list|,
name|endSuppTokens
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|ais
operator|=
name|PolicyUtils
operator|.
name|getAllAssertionsByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|SIGNED_ENDORSING_ENCRYPTED_SUPPORTING_TOKENS
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
name|SupportingTokens
name|endSuppTokens
init|=
literal|null
decl_stmt|;
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|endSuppTokens
operator|=
operator|(
name|SupportingTokens
operator|)
name|ai
operator|.
name|getAssertion
argument_list|()
expr_stmt|;
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endSuppTokens
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|AbstractToken
name|token
range|:
name|endSuppTokens
operator|.
name|getTokens
argument_list|()
control|)
block|{
name|handleEndorsingToken
argument_list|(
name|token
argument_list|,
name|endSuppTokens
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|handleEndorsingToken
parameter_list|(
name|AbstractToken
name|token
parameter_list|,
name|SupportingTokens
name|wrapper
parameter_list|)
throws|throws
name|Exception
block|{
name|assertToken
argument_list|(
name|token
argument_list|)
expr_stmt|;
if|if
condition|(
name|token
operator|!=
literal|null
operator|&&
operator|!
name|isTokenRequired
argument_list|(
name|token
operator|.
name|getIncludeTokenType
argument_list|()
argument_list|)
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|token
operator|instanceof
name|IssuedToken
condition|)
block|{
name|SecurityToken
name|securityToken
init|=
name|getSecurityToken
argument_list|()
decl_stmt|;
name|addIssuedToken
argument_list|(
name|token
argument_list|,
name|securityToken
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|signPartsAndElements
argument_list|(
name|wrapper
operator|.
name|getSignedParts
argument_list|()
argument_list|,
name|wrapper
operator|.
name|getSignedElements
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|token
operator|instanceof
name|SecureConversationToken
operator|||
name|token
operator|instanceof
name|SecurityContextToken
operator|||
name|token
operator|instanceof
name|SpnegoContextToken
condition|)
block|{
name|SecurityToken
name|securityToken
init|=
name|getSecurityToken
argument_list|()
decl_stmt|;
name|addIssuedToken
argument_list|(
name|token
argument_list|,
name|securityToken
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|WSSSecurityProperties
name|properties
init|=
name|getProperties
argument_list|()
decl_stmt|;
if|if
condition|(
name|securityToken
operator|!=
literal|null
condition|)
block|{
name|storeSecurityToken
argument_list|(
name|token
argument_list|,
name|securityToken
argument_list|)
expr_stmt|;
comment|// Set up CallbackHandler which wraps the configured Handler
name|TokenStoreCallbackHandler
name|callbackHandler
init|=
operator|new
name|TokenStoreCallbackHandler
argument_list|(
name|properties
operator|.
name|getCallbackHandler
argument_list|()
argument_list|,
name|TokenStoreUtils
operator|.
name|getTokenStore
argument_list|(
name|message
argument_list|)
argument_list|)
decl_stmt|;
name|properties
operator|.
name|setCallbackHandler
argument_list|(
name|callbackHandler
argument_list|)
expr_stmt|;
block|}
name|doSignature
argument_list|(
name|token
argument_list|,
name|wrapper
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setIncludeSignatureToken
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setSignatureAlgorithm
argument_list|(
name|tbinding
operator|.
name|getAlgorithmSuite
argument_list|()
operator|.
name|getSymmetricSignature
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setSignatureCanonicalizationAlgorithm
argument_list|(
name|tbinding
operator|.
name|getAlgorithmSuite
argument_list|()
operator|.
name|getC14n
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|AlgorithmSuiteType
name|algType
init|=
name|tbinding
operator|.
name|getAlgorithmSuite
argument_list|()
operator|.
name|getAlgorithmSuiteType
argument_list|()
decl_stmt|;
name|properties
operator|.
name|setSignatureDigestAlgorithm
argument_list|(
name|algType
operator|.
name|getDigest
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|token
operator|instanceof
name|X509Token
operator|||
name|token
operator|instanceof
name|KeyValueToken
condition|)
block|{
name|doSignature
argument_list|(
name|token
argument_list|,
name|wrapper
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|token
operator|instanceof
name|SamlToken
condition|)
block|{
name|addSamlToken
argument_list|(
operator|(
name|SamlToken
operator|)
name|token
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|signPartsAndElements
argument_list|(
name|wrapper
operator|.
name|getSignedParts
argument_list|()
argument_list|,
name|wrapper
operator|.
name|getSignedElements
argument_list|()
argument_list|)
expr_stmt|;
name|WSSSecurityProperties
name|properties
init|=
name|getProperties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|setSignatureAlgorithm
argument_list|(
name|tbinding
operator|.
name|getAlgorithmSuite
argument_list|()
operator|.
name|getAsymmetricSignature
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setSignatureCanonicalizationAlgorithm
argument_list|(
name|tbinding
operator|.
name|getAlgorithmSuite
argument_list|()
operator|.
name|getC14n
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|AlgorithmSuiteType
name|algType
init|=
name|tbinding
operator|.
name|getAlgorithmSuite
argument_list|()
operator|.
name|getAlgorithmSuiteType
argument_list|()
decl_stmt|;
name|properties
operator|.
name|setSignatureDigestAlgorithm
argument_list|(
name|algType
operator|.
name|getDigest
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|token
operator|instanceof
name|UsernameToken
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Endorsing UsernameTokens are not supported in the streaming code"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|token
operator|instanceof
name|KerberosToken
condition|)
block|{
name|WSSSecurityProperties
name|properties
init|=
name|getProperties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|addAction
argument_list|(
name|WSSConstants
operator|.
name|SIGNATURE
argument_list|)
expr_stmt|;
name|configureSignature
argument_list|(
name|token
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|addKerberosToken
argument_list|(
operator|(
name|KerberosToken
operator|)
name|token
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|signPartsAndElements
argument_list|(
name|wrapper
operator|.
name|getSignedParts
argument_list|()
argument_list|,
name|wrapper
operator|.
name|getSignedElements
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setSignatureAlgorithm
argument_list|(
name|tbinding
operator|.
name|getAlgorithmSuite
argument_list|()
operator|.
name|getSymmetricSignature
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setSignatureCanonicalizationAlgorithm
argument_list|(
name|tbinding
operator|.
name|getAlgorithmSuite
argument_list|()
operator|.
name|getC14n
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|AlgorithmSuiteType
name|algType
init|=
name|tbinding
operator|.
name|getAlgorithmSuite
argument_list|()
operator|.
name|getAlgorithmSuiteType
argument_list|()
decl_stmt|;
name|properties
operator|.
name|setSignatureDigestAlgorithm
argument_list|(
name|algType
operator|.
name|getDigest
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|doSignature
parameter_list|(
name|AbstractToken
name|token
parameter_list|,
name|SupportingTokens
name|wrapper
parameter_list|)
throws|throws
name|Exception
block|{
name|signPartsAndElements
argument_list|(
name|wrapper
operator|.
name|getSignedParts
argument_list|()
argument_list|,
name|wrapper
operator|.
name|getSignedElements
argument_list|()
argument_list|)
expr_stmt|;
comment|// Action
name|WSSSecurityProperties
name|properties
init|=
name|getProperties
argument_list|()
decl_stmt|;
name|WSSConstants
operator|.
name|Action
name|actionToPerform
init|=
name|WSSConstants
operator|.
name|SIGNATURE
decl_stmt|;
if|if
condition|(
name|token
operator|.
name|getDerivedKeys
argument_list|()
operator|==
name|DerivedKeys
operator|.
name|RequireDerivedKeys
condition|)
block|{
name|actionToPerform
operator|=
name|WSSConstants
operator|.
name|SIGNATURE_WITH_DERIVED_KEY
expr_stmt|;
block|}
name|properties
operator|.
name|addAction
argument_list|(
name|actionToPerform
argument_list|)
expr_stmt|;
name|configureSignature
argument_list|(
name|token
argument_list|,
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|token
operator|.
name|getDerivedKeys
argument_list|()
operator|==
name|DerivedKeys
operator|.
name|RequireDerivedKeys
condition|)
block|{
name|properties
operator|.
name|setSignatureAlgorithm
argument_list|(
name|tbinding
operator|.
name|getAlgorithmSuite
argument_list|()
operator|.
name|getSymmetricSignature
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Identifies the portions of the message to be signed/encrypted.      */
specifier|private
name|void
name|signPartsAndElements
parameter_list|(
name|SignedParts
name|signedParts
parameter_list|,
name|SignedElements
name|signedElements
parameter_list|)
throws|throws
name|SOAPException
block|{
name|WSSSecurityProperties
name|properties
init|=
name|getProperties
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|SecurePart
argument_list|>
name|signatureParts
init|=
name|properties
operator|.
name|getSignatureSecureParts
argument_list|()
decl_stmt|;
comment|// Add timestamp
if|if
condition|(
name|timestampAdded
condition|)
block|{
name|SecurePart
name|part
init|=
operator|new
name|SecurePart
argument_list|(
operator|new
name|QName
argument_list|(
name|WSSConstants
operator|.
name|NS_WSU10
argument_list|,
literal|"Timestamp"
argument_list|)
argument_list|,
name|Modifier
operator|.
name|Element
argument_list|)
decl_stmt|;
name|signatureParts
operator|.
name|add
argument_list|(
name|part
argument_list|)
expr_stmt|;
block|}
comment|// Add SignedParts
if|if
condition|(
name|signedParts
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|signedParts
operator|.
name|isBody
argument_list|()
condition|)
block|{
name|SecurePart
name|part
init|=
operator|new
name|SecurePart
argument_list|(
operator|new
name|QName
argument_list|(
name|WSSConstants
operator|.
name|NS_SOAP11
argument_list|,
literal|"Body"
argument_list|)
argument_list|,
name|Modifier
operator|.
name|Element
argument_list|)
decl_stmt|;
name|signatureParts
operator|.
name|add
argument_list|(
name|part
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Header
name|head
range|:
name|signedParts
operator|.
name|getHeaders
argument_list|()
control|)
block|{
name|SecurePart
name|part
init|=
operator|new
name|SecurePart
argument_list|(
operator|new
name|QName
argument_list|(
name|head
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|head
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|,
name|Modifier
operator|.
name|Element
argument_list|)
decl_stmt|;
name|part
operator|.
name|setRequired
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|signatureParts
operator|.
name|add
argument_list|(
name|part
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Handle SignedElements
if|if
condition|(
name|signedElements
operator|!=
literal|null
operator|&&
name|signedElements
operator|.
name|getXPaths
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|XPath
name|xPath
range|:
name|signedElements
operator|.
name|getXPaths
argument_list|()
control|)
block|{
name|List
argument_list|<
name|QName
argument_list|>
name|qnames
init|=
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|policy
operator|.
name|stax
operator|.
name|PolicyUtils
operator|.
name|getElementPath
argument_list|(
name|xPath
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|qnames
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|SecurePart
name|part
init|=
operator|new
name|SecurePart
argument_list|(
name|qnames
operator|.
name|get
argument_list|(
name|qnames
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|,
name|Modifier
operator|.
name|Element
argument_list|)
decl_stmt|;
name|signatureParts
operator|.
name|add
argument_list|(
name|part
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

