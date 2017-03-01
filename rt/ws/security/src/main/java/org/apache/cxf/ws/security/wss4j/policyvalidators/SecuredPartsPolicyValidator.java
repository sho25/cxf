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
name|policyvalidators
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
name|transport
operator|.
name|TLSSessionInfo
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
name|wss4j
operator|.
name|CryptoCoverageUtil
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
name|CryptoCoverageUtil
operator|.
name|CoverageScope
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
name|CryptoCoverageUtil
operator|.
name|CoverageType
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
name|WSDataRef
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
name|AbstractSecuredParts
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
name|Attachments
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

begin_comment
comment|/**  * Validate either a SignedParts or EncryptedParts policy  */
end_comment

begin_class
specifier|public
class|class
name|SecuredPartsPolicyValidator
implements|implements
name|SecurityPolicyValidator
block|{
specifier|private
name|CoverageType
name|coverageType
init|=
name|CoverageType
operator|.
name|ENCRYPTED
decl_stmt|;
comment|/**      * Return true if this SecurityPolicyValidator implementation is capable of validating a      * policy defined by the AssertionInfo parameter      */
specifier|public
name|boolean
name|canValidatePolicy
parameter_list|(
name|AssertionInfo
name|assertionInfo
parameter_list|)
block|{
if|if
condition|(
name|coverageType
operator|==
name|CoverageType
operator|.
name|SIGNED
condition|)
block|{
return|return
name|assertionInfo
operator|.
name|getAssertion
argument_list|()
operator|!=
literal|null
operator|&&
operator|(
name|SP12Constants
operator|.
name|SIGNED_PARTS
operator|.
name|equals
argument_list|(
name|assertionInfo
operator|.
name|getAssertion
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|||
name|SP11Constants
operator|.
name|SIGNED_PARTS
operator|.
name|equals
argument_list|(
name|assertionInfo
operator|.
name|getAssertion
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|)
return|;
block|}
else|else
block|{
return|return
name|assertionInfo
operator|.
name|getAssertion
argument_list|()
operator|!=
literal|null
operator|&&
operator|(
name|SP12Constants
operator|.
name|ENCRYPTED_PARTS
operator|.
name|equals
argument_list|(
name|assertionInfo
operator|.
name|getAssertion
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|||
name|SP11Constants
operator|.
name|ENCRYPTED_PARTS
operator|.
name|equals
argument_list|(
name|assertionInfo
operator|.
name|getAssertion
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|)
return|;
block|}
block|}
comment|/**      * Validate policies.      */
specifier|public
name|void
name|validatePolicies
parameter_list|(
name|PolicyValidatorParameters
name|parameters
parameter_list|,
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
parameter_list|)
block|{
comment|//
comment|// SIGNED_PARTS and ENCRYPTED_PARTS only apply to non-Transport bindings
comment|//
if|if
condition|(
name|isTransportBinding
argument_list|(
name|parameters
operator|.
name|getAssertionInfoMap
argument_list|()
argument_list|,
name|parameters
operator|.
name|getMessage
argument_list|()
argument_list|)
condition|)
block|{
return|return;
block|}
name|Message
name|msg
init|=
name|parameters
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|Element
name|soapBody
init|=
name|parameters
operator|.
name|getSoapBody
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|WSDataRef
argument_list|>
name|dataRefs
init|=
name|parameters
operator|.
name|getEncrypted
argument_list|()
decl_stmt|;
if|if
condition|(
name|coverageType
operator|==
name|CoverageType
operator|.
name|SIGNED
condition|)
block|{
name|dataRefs
operator|=
name|parameters
operator|.
name|getSigned
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
if|if
condition|(
name|ai
operator|.
name|isAsserted
argument_list|()
condition|)
block|{
comment|// Secured Parts could already have been asserted by one of the other validators, if
comment|// they are a child of a SupportingToken
continue|continue;
block|}
name|AbstractSecuredParts
name|p
init|=
operator|(
name|AbstractSecuredParts
operator|)
name|ai
operator|.
name|getAssertion
argument_list|()
decl_stmt|;
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|p
operator|.
name|isBody
argument_list|()
condition|)
block|{
try|try
block|{
if|if
condition|(
name|coverageType
operator|==
name|CoverageType
operator|.
name|SIGNED
condition|)
block|{
name|CryptoCoverageUtil
operator|.
name|checkBodyCoverage
argument_list|(
name|soapBody
argument_list|,
name|dataRefs
argument_list|,
name|CoverageType
operator|.
name|SIGNED
argument_list|,
name|CoverageScope
operator|.
name|ELEMENT
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|CryptoCoverageUtil
operator|.
name|checkBodyCoverage
argument_list|(
name|soapBody
argument_list|,
name|dataRefs
argument_list|,
name|CoverageType
operator|.
name|ENCRYPTED
argument_list|,
name|CoverageScope
operator|.
name|CONTENT
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|WSSecurityException
name|e
parameter_list|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"Soap Body is not "
operator|+
name|coverageType
argument_list|)
expr_stmt|;
continue|continue;
block|}
block|}
for|for
control|(
name|Header
name|h
range|:
name|p
operator|.
name|getHeaders
argument_list|()
control|)
block|{
try|try
block|{
name|CryptoCoverageUtil
operator|.
name|checkHeaderCoverage
argument_list|(
name|parameters
operator|.
name|getSoapHeader
argument_list|()
argument_list|,
name|dataRefs
argument_list|,
name|h
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|h
operator|.
name|getName
argument_list|()
argument_list|,
name|coverageType
argument_list|,
name|CoverageScope
operator|.
name|ELEMENT
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSSecurityException
name|e
parameter_list|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
name|h
operator|.
name|getNamespace
argument_list|()
operator|+
literal|":"
operator|+
name|h
operator|.
name|getName
argument_list|()
operator|+
literal|" not + "
operator|+
name|coverageType
argument_list|)
expr_stmt|;
block|}
block|}
name|Attachments
name|attachments
init|=
name|p
operator|.
name|getAttachments
argument_list|()
decl_stmt|;
if|if
condition|(
name|attachments
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|CoverageScope
name|scope
init|=
name|CoverageScope
operator|.
name|ELEMENT
decl_stmt|;
if|if
condition|(
name|attachments
operator|.
name|isContentSignatureTransform
argument_list|()
condition|)
block|{
name|scope
operator|=
name|CoverageScope
operator|.
name|CONTENT
expr_stmt|;
block|}
name|CryptoCoverageUtil
operator|.
name|checkAttachmentsCoverage
argument_list|(
name|msg
operator|.
name|getAttachments
argument_list|()
argument_list|,
name|dataRefs
argument_list|,
name|coverageType
argument_list|,
name|scope
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSSecurityException
name|e
parameter_list|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"An attachment was not signed/encrypted"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|boolean
name|isTransportBinding
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
name|AssertionInfo
name|symAis
init|=
name|PolicyUtils
operator|.
name|getFirstAssertionByLocalname
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
name|symAis
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|AssertionInfo
name|asymAis
init|=
name|PolicyUtils
operator|.
name|getFirstAssertionByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|ASYMMETRIC_BINDING
argument_list|)
decl_stmt|;
if|if
condition|(
name|asymAis
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|AssertionInfo
name|transAis
init|=
name|PolicyUtils
operator|.
name|getFirstAssertionByLocalname
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
name|transAis
operator|!=
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
comment|// No bindings, check if we are using TLS
name|TLSSessionInfo
name|tlsInfo
init|=
name|message
operator|.
name|get
argument_list|(
name|TLSSessionInfo
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|tlsInfo
operator|!=
literal|null
condition|)
block|{
comment|// We don't need to check these policies for TLS
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SP12Constants
operator|.
name|ENCRYPTED_PARTS
argument_list|)
expr_stmt|;
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SP11Constants
operator|.
name|ENCRYPTED_PARTS
argument_list|)
expr_stmt|;
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SP12Constants
operator|.
name|SIGNED_PARTS
argument_list|)
expr_stmt|;
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SP11Constants
operator|.
name|SIGNED_PARTS
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|CoverageType
name|getCoverageType
parameter_list|()
block|{
return|return
name|coverageType
return|;
block|}
specifier|public
name|void
name|setCoverageType
parameter_list|(
name|CoverageType
name|coverageType
parameter_list|)
block|{
name|this
operator|.
name|coverageType
operator|=
name|coverageType
expr_stmt|;
block|}
block|}
end_class

end_unit

