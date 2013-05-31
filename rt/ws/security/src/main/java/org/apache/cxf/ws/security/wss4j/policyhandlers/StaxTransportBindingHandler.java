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
name|wss4j
operator|.
name|common
operator|.
name|ConfigurationConstants
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
name|stax
operator|.
name|ext
operator|.
name|WSSConstants
import|;
end_import

begin_comment
comment|/**  *   */
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|,
name|SoapMessage
name|msg
parameter_list|)
block|{
name|super
argument_list|(
name|properties
argument_list|,
name|msg
argument_list|)
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
name|configureLayout
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
name|tbinding
operator|=
operator|(
name|TransportBinding
operator|)
name|getBinding
argument_list|(
name|aim
argument_list|)
expr_stmt|;
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
comment|// TODO
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
name|addSignatureConfirmation
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
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
comment|/*TODO                else if (token instanceof IssuedToken || token instanceof KerberosToken) {                 SecurityToken secTok = getSecurityToken();                                  if (includeToken(token.getIncludeTokenType())) {                     //Add the token                     addEncryptedKeyElement(cloneElement(secTok.getToken()));                 }             } */
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
else|else
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
comment|/* TODO if (token instanceof IssuedToken             || token instanceof SecureConversationToken             || token instanceof SecurityContextToken             || token instanceof KerberosToken             || token instanceof SpnegoContextToken) {             addSig(doIssuedTokenSignature(token, wrapper));         } else */
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
name|doX509TokenSignature
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|config
init|=
name|getProperties
argument_list|()
decl_stmt|;
name|config
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|SIG_ALGO
argument_list|,
name|tbinding
operator|.
name|getAlgorithmSuite
argument_list|()
operator|.
name|getAsymmetricSignature
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
name|config
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|SIG_DIGEST_ALGO
argument_list|,
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
block|}
specifier|private
name|void
name|doX509TokenSignature
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|config
init|=
name|getProperties
argument_list|()
decl_stmt|;
name|String
name|actionToPerform
init|=
name|ConfigurationConstants
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
name|ConfigurationConstants
operator|.
name|SIGNATURE_DERIVED
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|containsKey
argument_list|(
name|ConfigurationConstants
operator|.
name|ACTION
argument_list|)
condition|)
block|{
name|String
name|action
init|=
operator|(
name|String
operator|)
name|config
operator|.
name|get
argument_list|(
name|ConfigurationConstants
operator|.
name|ACTION
argument_list|)
decl_stmt|;
name|config
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|ACTION
argument_list|,
name|action
operator|+
literal|" "
operator|+
name|actionToPerform
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|config
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|ACTION
argument_list|,
name|actionToPerform
argument_list|)
expr_stmt|;
block|}
name|configureSignature
argument_list|(
name|wrapper
argument_list|,
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
name|config
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|SIG_ALGO
argument_list|,
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
name|getProperties
argument_list|()
decl_stmt|;
name|String
name|parts
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|properties
operator|.
name|containsKey
argument_list|(
name|ConfigurationConstants
operator|.
name|SIGNATURE_PARTS
argument_list|)
condition|)
block|{
name|parts
operator|=
operator|(
name|String
operator|)
name|properties
operator|.
name|get
argument_list|(
name|ConfigurationConstants
operator|.
name|SIGNATURE_PARTS
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|parts
operator|.
name|endsWith
argument_list|(
literal|";"
argument_list|)
condition|)
block|{
name|parts
operator|+=
literal|";"
expr_stmt|;
block|}
block|}
comment|// Add timestamp
if|if
condition|(
name|timestampAdded
condition|)
block|{
name|parts
operator|+=
literal|"{Element}{"
operator|+
name|WSSConstants
operator|.
name|NS_WSU10
operator|+
literal|"}Timestamp;"
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
name|parts
operator|+=
literal|"{Element}{"
operator|+
name|WSSConstants
operator|.
name|NS_SOAP11
operator|+
literal|"}Body;"
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
name|parts
operator|+=
literal|"{Element}{"
operator|+
name|head
operator|.
name|getNamespace
argument_list|()
operator|+
literal|"}"
operator|+
name|head
operator|.
name|getName
argument_list|()
operator|+
literal|";"
expr_stmt|;
block|}
block|}
comment|/*          * TODO         if (signedElements != null) {             // Handle SignedElements             try {                 result.addAll(                     this.getElements(                         "Element", signedElements.getXPaths(), found, true                     )                 );             } catch (XPathExpressionException e) {                 LOG.log(Level.FINE, e.getMessage(), e);                 // REVISIT             }         }         */
name|properties
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|SIGNATURE_PARTS
argument_list|,
name|parts
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

