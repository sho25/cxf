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
name|rs
operator|.
name|security
operator|.
name|saml
operator|.
name|sso
package|;
end_package

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Duration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Instant
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
name|common
operator|.
name|saml
operator|.
name|builder
operator|.
name|SAML2Constants
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
name|util
operator|.
name|DOM2Writer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml2
operator|.
name|core
operator|.
name|AudienceRestriction
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml2
operator|.
name|core
operator|.
name|AuthnStatement
import|;
end_import

begin_comment
comment|/**  * Validate a SAML 2.0 Protocol Response according to the Web SSO profile. The Response  * should be validated by the SAMLProtocolResponseValidator first.  */
end_comment

begin_class
specifier|public
class|class
name|SAMLSSOResponseValidator
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
name|SAMLSSOResponseValidator
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|String
name|issuerIDP
decl_stmt|;
specifier|private
name|String
name|assertionConsumerURL
decl_stmt|;
specifier|private
name|String
name|clientAddress
decl_stmt|;
specifier|private
name|String
name|requestId
decl_stmt|;
specifier|private
name|String
name|spIdentifier
decl_stmt|;
specifier|private
name|boolean
name|enforceResponseSigned
decl_stmt|;
specifier|private
name|boolean
name|enforceAssertionsSigned
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|enforceKnownIssuer
init|=
literal|true
decl_stmt|;
specifier|private
name|TokenReplayCache
argument_list|<
name|String
argument_list|>
name|replayCache
decl_stmt|;
comment|/**      * Enforce that Assertions contained in the Response must be signed (if the Response itself is not      * signed). The default is true.      */
specifier|public
name|void
name|setEnforceAssertionsSigned
parameter_list|(
name|boolean
name|enforceAssertionsSigned
parameter_list|)
block|{
name|this
operator|.
name|enforceAssertionsSigned
operator|=
name|enforceAssertionsSigned
expr_stmt|;
block|}
comment|/**      * Enforce that the Issuer of the received Response/Assertion is known. The default is true.      */
specifier|public
name|void
name|setEnforceKnownIssuer
parameter_list|(
name|boolean
name|enforceKnownIssuer
parameter_list|)
block|{
name|this
operator|.
name|enforceKnownIssuer
operator|=
name|enforceKnownIssuer
expr_stmt|;
block|}
comment|/**      * Validate a SAML 2 Protocol Response      * @param samlResponse      * @param postBinding      * @return a SSOValidatorResponse object      * @throws WSSecurityException      */
specifier|public
name|SSOValidatorResponse
name|validateSamlResponse
parameter_list|(
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Response
name|samlResponse
parameter_list|,
name|boolean
name|postBinding
parameter_list|)
throws|throws
name|WSSecurityException
block|{
comment|// Check the Issuer
name|validateIssuer
argument_list|(
name|samlResponse
operator|.
name|getIssuer
argument_list|()
argument_list|)
expr_stmt|;
comment|// The Response must contain at least one Assertion.
if|if
condition|(
name|samlResponse
operator|.
name|getAssertions
argument_list|()
operator|==
literal|null
operator|||
name|samlResponse
operator|.
name|getAssertions
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"The Response must contain at least one Assertion"
argument_list|)
expr_stmt|;
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
literal|"invalidSAMLsecurity"
argument_list|)
throw|;
block|}
comment|// The Response must contain a Destination that matches the assertionConsumerURL if it is
comment|// signed
name|String
name|destination
init|=
name|samlResponse
operator|.
name|getDestination
argument_list|()
decl_stmt|;
if|if
condition|(
name|samlResponse
operator|.
name|isSigned
argument_list|()
operator|&&
operator|(
name|destination
operator|==
literal|null
operator|||
operator|!
name|destination
operator|.
name|equals
argument_list|(
name|assertionConsumerURL
argument_list|)
operator|)
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"The Response must contain a destination that matches the assertion consumer URL"
argument_list|)
expr_stmt|;
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
literal|"invalidSAMLsecurity"
argument_list|)
throw|;
block|}
if|if
condition|(
name|enforceResponseSigned
operator|&&
operator|!
name|samlResponse
operator|.
name|isSigned
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"The Response must be signed!"
argument_list|)
expr_stmt|;
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
literal|"invalidSAMLsecurity"
argument_list|)
throw|;
block|}
comment|// Validate Assertions
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Assertion
name|validAssertion
init|=
literal|null
decl_stmt|;
name|Instant
name|sessionNotOnOrAfter
init|=
literal|null
decl_stmt|;
for|for
control|(
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Assertion
name|assertion
range|:
name|samlResponse
operator|.
name|getAssertions
argument_list|()
control|)
block|{
comment|// Check the Issuer
if|if
condition|(
name|assertion
operator|.
name|getIssuer
argument_list|()
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Assertion Issuer must not be null"
argument_list|)
expr_stmt|;
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
literal|"invalidSAMLsecurity"
argument_list|)
throw|;
block|}
name|validateIssuer
argument_list|(
name|assertion
operator|.
name|getIssuer
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|samlResponse
operator|.
name|isSigned
argument_list|()
operator|&&
name|enforceAssertionsSigned
operator|&&
name|assertion
operator|.
name|getSignature
argument_list|()
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"The enclosed assertions in the SAML Response must be signed"
argument_list|)
expr_stmt|;
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
literal|"invalidSAMLsecurity"
argument_list|)
throw|;
block|}
comment|// Check for AuthnStatements and validate the Subject accordingly
if|if
condition|(
name|assertion
operator|.
name|getAuthnStatements
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|assertion
operator|.
name|getAuthnStatements
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Subject
name|subject
init|=
name|assertion
operator|.
name|getSubject
argument_list|()
decl_stmt|;
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml2
operator|.
name|core
operator|.
name|SubjectConfirmation
name|subjectConf
init|=
name|validateAuthenticationSubject
argument_list|(
name|subject
argument_list|,
name|assertion
operator|.
name|getID
argument_list|()
argument_list|,
name|postBinding
argument_list|)
decl_stmt|;
if|if
condition|(
name|subjectConf
operator|!=
literal|null
condition|)
block|{
name|validateAudienceRestrictionCondition
argument_list|(
name|assertion
operator|.
name|getConditions
argument_list|()
argument_list|)
expr_stmt|;
name|validAssertion
operator|=
name|assertion
expr_stmt|;
comment|// Store Session NotOnOrAfter
for|for
control|(
name|AuthnStatement
name|authnStatment
range|:
name|assertion
operator|.
name|getAuthnStatements
argument_list|()
control|)
block|{
if|if
condition|(
name|authnStatment
operator|.
name|getSessionNotOnOrAfter
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sessionNotOnOrAfter
operator|=
name|Instant
operator|.
name|ofEpochMilli
argument_list|(
name|authnStatment
operator|.
name|getSessionNotOnOrAfter
argument_list|()
operator|.
name|toDate
argument_list|()
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Fall back to the SubjectConfirmationData NotOnOrAfter if we have no session NotOnOrAfter
if|if
condition|(
name|sessionNotOnOrAfter
operator|==
literal|null
condition|)
block|{
name|sessionNotOnOrAfter
operator|=
name|Instant
operator|.
name|ofEpochMilli
argument_list|(
name|subjectConf
operator|.
name|getSubjectConfirmationData
argument_list|()
operator|.
name|getNotOnOrAfter
argument_list|()
operator|.
name|toDate
argument_list|()
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
name|validAssertion
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"The Response did not contain any Authentication Statement that matched "
operator|+
literal|"the Subject Confirmation criteria"
argument_list|)
expr_stmt|;
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
literal|"invalidSAMLsecurity"
argument_list|)
throw|;
block|}
name|SSOValidatorResponse
name|validatorResponse
init|=
operator|new
name|SSOValidatorResponse
argument_list|()
decl_stmt|;
name|validatorResponse
operator|.
name|setResponseId
argument_list|(
name|samlResponse
operator|.
name|getID
argument_list|()
argument_list|)
expr_stmt|;
name|validatorResponse
operator|.
name|setSessionNotOnOrAfter
argument_list|(
name|sessionNotOnOrAfter
argument_list|)
expr_stmt|;
if|if
condition|(
name|samlResponse
operator|.
name|getIssueInstant
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|validatorResponse
operator|.
name|setCreated
argument_list|(
name|Instant
operator|.
name|ofEpochMilli
argument_list|(
name|samlResponse
operator|.
name|getIssueInstant
argument_list|()
operator|.
name|toDate
argument_list|()
operator|.
name|getTime
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Element
name|assertionElement
init|=
name|validAssertion
operator|.
name|getDOM
argument_list|()
decl_stmt|;
name|Element
name|clonedAssertionElement
init|=
operator|(
name|Element
operator|)
name|assertionElement
operator|.
name|cloneNode
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|validatorResponse
operator|.
name|setAssertionElement
argument_list|(
name|clonedAssertionElement
argument_list|)
expr_stmt|;
name|validatorResponse
operator|.
name|setAssertion
argument_list|(
name|DOM2Writer
operator|.
name|nodeToString
argument_list|(
name|clonedAssertionElement
argument_list|)
argument_list|)
expr_stmt|;
name|validatorResponse
operator|.
name|setOpensamlAssertion
argument_list|(
name|validAssertion
argument_list|)
expr_stmt|;
return|return
name|validatorResponse
return|;
block|}
comment|/**      * Validate the Issuer (if it exists)      */
specifier|private
name|void
name|validateIssuer
parameter_list|(
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Issuer
name|issuer
parameter_list|)
throws|throws
name|WSSecurityException
block|{
if|if
condition|(
name|issuer
operator|==
literal|null
condition|)
block|{
return|return;
block|}
comment|// Issuer value must match (be contained in) Issuer IDP
if|if
condition|(
name|enforceKnownIssuer
operator|&&
operator|(
name|issuer
operator|.
name|getValue
argument_list|()
operator|==
literal|null
operator|||
operator|!
name|issuerIDP
operator|.
name|startsWith
argument_list|(
name|issuer
operator|.
name|getValue
argument_list|()
argument_list|)
operator|)
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Issuer value: "
operator|+
name|issuer
operator|.
name|getValue
argument_list|()
operator|+
literal|" does not match issuer IDP: "
operator|+
name|issuerIDP
argument_list|)
expr_stmt|;
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
literal|"invalidSAMLsecurity"
argument_list|)
throw|;
block|}
comment|// Format must be nameid-format-entity
if|if
condition|(
name|issuer
operator|.
name|getFormat
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|SAML2Constants
operator|.
name|NAMEID_FORMAT_ENTITY
operator|.
name|equals
argument_list|(
name|issuer
operator|.
name|getFormat
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Issuer format is not null and does not equal: "
operator|+
name|SAML2Constants
operator|.
name|NAMEID_FORMAT_ENTITY
argument_list|)
expr_stmt|;
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
literal|"invalidSAMLsecurity"
argument_list|)
throw|;
block|}
block|}
comment|/**      * Validate the Subject (of an Authentication Statement).      */
specifier|private
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml2
operator|.
name|core
operator|.
name|SubjectConfirmation
name|validateAuthenticationSubject
parameter_list|(
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Subject
name|subject
parameter_list|,
name|String
name|id
parameter_list|,
name|boolean
name|postBinding
parameter_list|)
throws|throws
name|WSSecurityException
block|{
if|if
condition|(
name|subject
operator|.
name|getSubjectConfirmations
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml2
operator|.
name|core
operator|.
name|SubjectConfirmation
name|validSubjectConf
init|=
literal|null
decl_stmt|;
comment|// We need to find a Bearer Subject Confirmation method
for|for
control|(
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml2
operator|.
name|core
operator|.
name|SubjectConfirmation
name|subjectConf
range|:
name|subject
operator|.
name|getSubjectConfirmations
argument_list|()
control|)
block|{
if|if
condition|(
name|SAML2Constants
operator|.
name|CONF_BEARER
operator|.
name|equals
argument_list|(
name|subjectConf
operator|.
name|getMethod
argument_list|()
argument_list|)
condition|)
block|{
name|validateSubjectConfirmation
argument_list|(
name|subjectConf
operator|.
name|getSubjectConfirmationData
argument_list|()
argument_list|,
name|id
argument_list|,
name|postBinding
argument_list|)
expr_stmt|;
name|validSubjectConf
operator|=
name|subjectConf
expr_stmt|;
block|}
block|}
return|return
name|validSubjectConf
return|;
block|}
comment|/**      * Validate a (Bearer) Subject Confirmation      */
specifier|private
name|void
name|validateSubjectConfirmation
parameter_list|(
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml2
operator|.
name|core
operator|.
name|SubjectConfirmationData
name|subjectConfData
parameter_list|,
name|String
name|id
parameter_list|,
name|boolean
name|postBinding
parameter_list|)
throws|throws
name|WSSecurityException
block|{
if|if
condition|(
name|subjectConfData
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Subject Confirmation Data of a Bearer Subject Confirmation is null"
argument_list|)
expr_stmt|;
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
literal|"invalidSAMLsecurity"
argument_list|)
throw|;
block|}
comment|// Recipient must match assertion consumer URL
name|String
name|recipient
init|=
name|subjectConfData
operator|.
name|getRecipient
argument_list|()
decl_stmt|;
if|if
condition|(
name|recipient
operator|==
literal|null
operator|||
operator|!
name|recipient
operator|.
name|equals
argument_list|(
name|assertionConsumerURL
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Recipient "
operator|+
name|recipient
operator|+
literal|" does not match assertion consumer URL "
operator|+
name|assertionConsumerURL
argument_list|)
expr_stmt|;
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
literal|"invalidSAMLsecurity"
argument_list|)
throw|;
block|}
comment|// We must have a NotOnOrAfter timestamp
if|if
condition|(
name|subjectConfData
operator|.
name|getNotOnOrAfter
argument_list|()
operator|==
literal|null
operator|||
name|subjectConfData
operator|.
name|getNotOnOrAfter
argument_list|()
operator|.
name|isBeforeNow
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Subject Conf Data does not contain NotOnOrAfter or it has expired"
argument_list|)
expr_stmt|;
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
literal|"invalidSAMLsecurity"
argument_list|)
throw|;
block|}
comment|// Need to keep bearer assertion IDs based on NotOnOrAfter to detect replay attacks
if|if
condition|(
name|postBinding
operator|&&
name|replayCache
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|replayCache
operator|.
name|getId
argument_list|(
name|id
argument_list|)
operator|==
literal|null
condition|)
block|{
name|Instant
name|expires
init|=
name|Instant
operator|.
name|ofEpochMilli
argument_list|(
name|subjectConfData
operator|.
name|getNotOnOrAfter
argument_list|()
operator|.
name|toDate
argument_list|()
operator|.
name|getTime
argument_list|()
argument_list|)
decl_stmt|;
name|Instant
name|currentTime
init|=
name|Instant
operator|.
name|now
argument_list|()
decl_stmt|;
name|long
name|ttl
init|=
name|Duration
operator|.
name|between
argument_list|(
name|currentTime
argument_list|,
name|expires
argument_list|)
operator|.
name|getSeconds
argument_list|()
decl_stmt|;
name|replayCache
operator|.
name|putId
argument_list|(
name|id
argument_list|,
name|ttl
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Replay attack with token id: "
operator|+
name|id
argument_list|)
expr_stmt|;
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
literal|"invalidSAMLsecurity"
argument_list|)
throw|;
block|}
block|}
comment|// Check address
if|if
condition|(
name|subjectConfData
operator|.
name|getAddress
argument_list|()
operator|!=
literal|null
operator|&&
name|clientAddress
operator|!=
literal|null
operator|&&
operator|!
name|subjectConfData
operator|.
name|getAddress
argument_list|()
operator|.
name|equals
argument_list|(
name|clientAddress
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Subject Conf Data address "
operator|+
name|subjectConfData
operator|.
name|getAddress
argument_list|()
operator|+
literal|" does not match"
operator|+
literal|" client address "
operator|+
name|clientAddress
argument_list|)
expr_stmt|;
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
literal|"invalidSAMLsecurity"
argument_list|)
throw|;
block|}
comment|// It must not contain a NotBefore timestamp
if|if
condition|(
name|subjectConfData
operator|.
name|getNotBefore
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"The Subject Conf Data must not contain a NotBefore timestamp"
argument_list|)
expr_stmt|;
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
literal|"invalidSAMLsecurity"
argument_list|)
throw|;
block|}
comment|// InResponseTo must match the AuthnRequest request Id
if|if
condition|(
name|requestId
operator|!=
literal|null
operator|&&
operator|!
name|requestId
operator|.
name|equals
argument_list|(
name|subjectConfData
operator|.
name|getInResponseTo
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"The InResponseTo String does match the original request id "
operator|+
name|requestId
argument_list|)
expr_stmt|;
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
literal|"invalidSAMLsecurity"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|requestId
operator|==
literal|null
operator|&&
name|subjectConfData
operator|.
name|getInResponseTo
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"No InResponseTo String is allowed for the unsolicted case"
argument_list|)
expr_stmt|;
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
literal|"invalidSAMLsecurity"
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|validateAudienceRestrictionCondition
parameter_list|(
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Conditions
name|conditions
parameter_list|)
throws|throws
name|WSSecurityException
block|{
if|if
condition|(
name|conditions
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Conditions are null"
argument_list|)
expr_stmt|;
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
literal|"invalidSAMLsecurity"
argument_list|)
throw|;
block|}
name|List
argument_list|<
name|AudienceRestriction
argument_list|>
name|audienceRestrs
init|=
name|conditions
operator|.
name|getAudienceRestrictions
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|matchSaml2AudienceRestriction
argument_list|(
name|spIdentifier
argument_list|,
name|audienceRestrs
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Assertion does not contain unique subject provider identifier "
operator|+
name|spIdentifier
operator|+
literal|" in the audience restriction conditions"
argument_list|)
expr_stmt|;
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
literal|"invalidSAMLsecurity"
argument_list|)
throw|;
block|}
block|}
specifier|private
name|boolean
name|matchSaml2AudienceRestriction
parameter_list|(
name|String
name|appliesTo
parameter_list|,
name|List
argument_list|<
name|AudienceRestriction
argument_list|>
name|audienceRestrictions
parameter_list|)
block|{
name|boolean
name|oneMatchFound
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|audienceRestrictions
operator|!=
literal|null
operator|&&
operator|!
name|audienceRestrictions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|AudienceRestriction
name|audienceRestriction
range|:
name|audienceRestrictions
control|)
block|{
if|if
condition|(
name|audienceRestriction
operator|.
name|getAudiences
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|boolean
name|matchFound
init|=
literal|false
decl_stmt|;
for|for
control|(
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Audience
name|audience
range|:
name|audienceRestriction
operator|.
name|getAudiences
argument_list|()
control|)
block|{
if|if
condition|(
name|appliesTo
operator|.
name|equals
argument_list|(
name|audience
operator|.
name|getAudienceURI
argument_list|()
argument_list|)
condition|)
block|{
name|matchFound
operator|=
literal|true
expr_stmt|;
name|oneMatchFound
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|matchFound
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
block|}
return|return
name|oneMatchFound
return|;
block|}
specifier|public
name|String
name|getIssuerIDP
parameter_list|()
block|{
return|return
name|issuerIDP
return|;
block|}
specifier|public
name|void
name|setIssuerIDP
parameter_list|(
name|String
name|issuerIDP
parameter_list|)
block|{
name|this
operator|.
name|issuerIDP
operator|=
name|issuerIDP
expr_stmt|;
block|}
specifier|public
name|String
name|getAssertionConsumerURL
parameter_list|()
block|{
return|return
name|assertionConsumerURL
return|;
block|}
specifier|public
name|void
name|setAssertionConsumerURL
parameter_list|(
name|String
name|assertionConsumerURL
parameter_list|)
block|{
name|this
operator|.
name|assertionConsumerURL
operator|=
name|assertionConsumerURL
expr_stmt|;
block|}
specifier|public
name|String
name|getClientAddress
parameter_list|()
block|{
return|return
name|clientAddress
return|;
block|}
specifier|public
name|void
name|setClientAddress
parameter_list|(
name|String
name|clientAddress
parameter_list|)
block|{
name|this
operator|.
name|clientAddress
operator|=
name|clientAddress
expr_stmt|;
block|}
specifier|public
name|String
name|getRequestId
parameter_list|()
block|{
return|return
name|requestId
return|;
block|}
specifier|public
name|void
name|setRequestId
parameter_list|(
name|String
name|requestId
parameter_list|)
block|{
name|this
operator|.
name|requestId
operator|=
name|requestId
expr_stmt|;
block|}
specifier|public
name|String
name|getSpIdentifier
parameter_list|()
block|{
return|return
name|spIdentifier
return|;
block|}
specifier|public
name|void
name|setSpIdentifier
parameter_list|(
name|String
name|spIdentifier
parameter_list|)
block|{
name|this
operator|.
name|spIdentifier
operator|=
name|spIdentifier
expr_stmt|;
block|}
specifier|public
name|void
name|setReplayCache
parameter_list|(
name|TokenReplayCache
argument_list|<
name|String
argument_list|>
name|replayCache
parameter_list|)
block|{
name|this
operator|.
name|replayCache
operator|=
name|replayCache
expr_stmt|;
block|}
specifier|public
name|boolean
name|isEnforceResponseSigned
parameter_list|()
block|{
return|return
name|enforceResponseSigned
return|;
block|}
comment|/**      * Enforce whether a SAML Response must be signed.      */
specifier|public
name|void
name|setEnforceResponseSigned
parameter_list|(
name|boolean
name|enforceResponseSigned
parameter_list|)
block|{
name|this
operator|.
name|enforceResponseSigned
operator|=
name|enforceResponseSigned
expr_stmt|;
block|}
block|}
end_class

end_unit

