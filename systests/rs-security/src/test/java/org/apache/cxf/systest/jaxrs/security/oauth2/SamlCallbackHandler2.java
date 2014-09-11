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
name|systest
operator|.
name|jaxrs
operator|.
name|security
operator|.
name|oauth2
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

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
name|Collections
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
name|Callback
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
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|UnsupportedCallbackException
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
name|PhaseInterceptorChain
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
name|rt
operator|.
name|security
operator|.
name|claims
operator|.
name|SAMLClaim
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
name|crypto
operator|.
name|Crypto
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
name|crypto
operator|.
name|CryptoFactory
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
name|SAMLCallback
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
name|bean
operator|.
name|ActionBean
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
name|bean
operator|.
name|AttributeBean
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
name|bean
operator|.
name|AttributeStatementBean
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
name|bean
operator|.
name|AudienceRestrictionBean
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
name|bean
operator|.
name|AuthDecisionStatementBean
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
name|bean
operator|.
name|AuthDecisionStatementBean
operator|.
name|Decision
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
name|bean
operator|.
name|AuthenticationStatementBean
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
name|bean
operator|.
name|ConditionsBean
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
name|bean
operator|.
name|SubjectBean
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
name|joda
operator|.
name|time
operator|.
name|DateTime
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|common
operator|.
name|SAMLVersion
import|;
end_import

begin_comment
comment|/**  * A CallbackHandler instance that is used by the STS to mock up a SAML Attribute Assertion.  */
end_comment

begin_class
specifier|public
class|class
name|SamlCallbackHandler2
implements|implements
name|CallbackHandler
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|BookServerOAuth2
operator|.
name|PORT
decl_stmt|;
specifier|private
name|String
name|confirmationMethod
init|=
name|SAML2Constants
operator|.
name|CONF_BEARER
decl_stmt|;
specifier|public
name|SamlCallbackHandler2
parameter_list|()
block|{     }
specifier|public
name|void
name|setConfirmationMethod
parameter_list|(
name|String
name|confirmationMethod
parameter_list|)
block|{
name|this
operator|.
name|confirmationMethod
operator|=
name|confirmationMethod
expr_stmt|;
block|}
specifier|public
name|void
name|handle
parameter_list|(
name|Callback
index|[]
name|callbacks
parameter_list|)
throws|throws
name|IOException
throws|,
name|UnsupportedCallbackException
block|{
name|Message
name|m
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|callbacks
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|callbacks
index|[
name|i
index|]
operator|instanceof
name|SAMLCallback
condition|)
block|{
name|SAMLCallback
name|callback
init|=
operator|(
name|SAMLCallback
operator|)
name|callbacks
index|[
name|i
index|]
decl_stmt|;
name|callback
operator|.
name|setSamlVersion
argument_list|(
name|SAMLVersion
operator|.
name|VERSION_20
argument_list|)
expr_stmt|;
name|callback
operator|.
name|setIssuer
argument_list|(
literal|"alice"
argument_list|)
expr_stmt|;
name|String
name|subjectName
init|=
name|m
operator|!=
literal|null
condition|?
operator|(
name|String
operator|)
name|m
operator|.
name|getContextualProperty
argument_list|(
literal|"saml.subject.name"
argument_list|)
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|subjectName
operator|==
literal|null
condition|)
block|{
name|subjectName
operator|=
literal|"alice"
expr_stmt|;
block|}
name|String
name|subjectQualifier
init|=
literal|"www.mock-sts.com"
decl_stmt|;
name|SubjectBean
name|subjectBean
init|=
operator|new
name|SubjectBean
argument_list|(
name|subjectName
argument_list|,
name|subjectQualifier
argument_list|,
name|confirmationMethod
argument_list|)
decl_stmt|;
name|callback
operator|.
name|setSubject
argument_list|(
name|subjectBean
argument_list|)
expr_stmt|;
name|ConditionsBean
name|conditions
init|=
operator|new
name|ConditionsBean
argument_list|()
decl_stmt|;
name|AudienceRestrictionBean
name|audienceRestriction
init|=
operator|new
name|AudienceRestrictionBean
argument_list|()
decl_stmt|;
name|String
name|audienceURI
init|=
literal|"https://localhost:"
operator|+
name|PORT
operator|+
literal|"/oauth2-auth/token"
decl_stmt|;
name|audienceRestriction
operator|.
name|setAudienceURIs
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|audienceURI
argument_list|)
argument_list|)
expr_stmt|;
name|conditions
operator|.
name|setAudienceRestrictions
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|audienceRestriction
argument_list|)
argument_list|)
expr_stmt|;
name|callback
operator|.
name|setConditions
argument_list|(
name|conditions
argument_list|)
expr_stmt|;
name|AuthDecisionStatementBean
name|authDecBean
init|=
operator|new
name|AuthDecisionStatementBean
argument_list|()
decl_stmt|;
name|authDecBean
operator|.
name|setDecision
argument_list|(
name|Decision
operator|.
name|INDETERMINATE
argument_list|)
expr_stmt|;
name|authDecBean
operator|.
name|setResource
argument_list|(
literal|"https://sp.example.com/SAML2"
argument_list|)
expr_stmt|;
name|ActionBean
name|actionBean
init|=
operator|new
name|ActionBean
argument_list|()
decl_stmt|;
name|actionBean
operator|.
name|setContents
argument_list|(
literal|"Read"
argument_list|)
expr_stmt|;
name|authDecBean
operator|.
name|setActions
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|actionBean
argument_list|)
argument_list|)
expr_stmt|;
name|callback
operator|.
name|setAuthDecisionStatementData
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|authDecBean
argument_list|)
argument_list|)
expr_stmt|;
name|AuthenticationStatementBean
name|authBean
init|=
operator|new
name|AuthenticationStatementBean
argument_list|()
decl_stmt|;
name|authBean
operator|.
name|setSubject
argument_list|(
name|subjectBean
argument_list|)
expr_stmt|;
name|authBean
operator|.
name|setAuthenticationInstant
argument_list|(
operator|new
name|DateTime
argument_list|()
argument_list|)
expr_stmt|;
name|authBean
operator|.
name|setSessionIndex
argument_list|(
literal|"123456"
argument_list|)
expr_stmt|;
comment|// AuthnContextClassRef is not set
name|authBean
operator|.
name|setAuthenticationMethod
argument_list|(
literal|"urn:oasis:names:tc:SAML:2.0:ac:classes:PasswordProtectedTransport"
argument_list|)
expr_stmt|;
name|callback
operator|.
name|setAuthenticationStatementData
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|authBean
argument_list|)
argument_list|)
expr_stmt|;
name|AttributeStatementBean
name|attrBean
init|=
operator|new
name|AttributeStatementBean
argument_list|()
decl_stmt|;
name|attrBean
operator|.
name|setSubject
argument_list|(
name|subjectBean
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|roles
init|=
name|m
operator|!=
literal|null
condition|?
name|CastUtils
operator|.
expr|<
name|String
operator|>
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|m
operator|.
name|getContextualProperty
argument_list|(
literal|"saml.roles"
argument_list|)
argument_list|)
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|roles
operator|==
literal|null
condition|)
block|{
name|roles
operator|=
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"user"
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|AttributeBean
argument_list|>
name|claims
init|=
operator|new
name|ArrayList
argument_list|<
name|AttributeBean
argument_list|>
argument_list|()
decl_stmt|;
name|AttributeBean
name|roleClaim
init|=
operator|new
name|AttributeBean
argument_list|()
decl_stmt|;
name|roleClaim
operator|.
name|setSimpleName
argument_list|(
literal|"subject-role"
argument_list|)
expr_stmt|;
name|roleClaim
operator|.
name|setQualifiedName
argument_list|(
name|SAMLClaim
operator|.
name|SAML_ROLE_ATTRIBUTENAME_DEFAULT
argument_list|)
expr_stmt|;
name|roleClaim
operator|.
name|setNameFormat
argument_list|(
name|SAML2Constants
operator|.
name|ATTRNAME_FORMAT_UNSPECIFIED
argument_list|)
expr_stmt|;
name|roleClaim
operator|.
name|setAttributeValues
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|(
name|roles
argument_list|)
argument_list|)
expr_stmt|;
name|claims
operator|.
name|add
argument_list|(
name|roleClaim
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|authMethods
init|=
name|m
operator|!=
literal|null
condition|?
name|CastUtils
operator|.
expr|<
name|String
operator|>
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|m
operator|.
name|getContextualProperty
argument_list|(
literal|"saml.auth"
argument_list|)
argument_list|)
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|authMethods
operator|==
literal|null
condition|)
block|{
name|authMethods
operator|=
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"password"
argument_list|)
expr_stmt|;
block|}
name|AttributeBean
name|authClaim
init|=
operator|new
name|AttributeBean
argument_list|()
decl_stmt|;
name|authClaim
operator|.
name|setSimpleName
argument_list|(
literal|"http://claims/authentication"
argument_list|)
expr_stmt|;
name|authClaim
operator|.
name|setQualifiedName
argument_list|(
literal|"http://claims/authentication"
argument_list|)
expr_stmt|;
name|authClaim
operator|.
name|setNameFormat
argument_list|(
literal|"http://claims/authentication-format"
argument_list|)
expr_stmt|;
name|authClaim
operator|.
name|setAttributeValues
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|(
name|authMethods
argument_list|)
argument_list|)
expr_stmt|;
name|claims
operator|.
name|add
argument_list|(
name|authClaim
argument_list|)
expr_stmt|;
name|attrBean
operator|.
name|setSamlAttributes
argument_list|(
name|claims
argument_list|)
expr_stmt|;
name|callback
operator|.
name|setAttributeStatementData
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|attrBean
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|Crypto
name|crypto
init|=
name|CryptoFactory
operator|.
name|getInstance
argument_list|(
literal|"org/apache/cxf/systest/jaxrs/security/alice.properties"
argument_list|)
decl_stmt|;
name|callback
operator|.
name|setIssuerCrypto
argument_list|(
name|crypto
argument_list|)
expr_stmt|;
name|callback
operator|.
name|setIssuerKeyName
argument_list|(
literal|"alice"
argument_list|)
expr_stmt|;
name|callback
operator|.
name|setIssuerKeyPassword
argument_list|(
literal|"password"
argument_list|)
expr_stmt|;
name|callback
operator|.
name|setSignAssertion
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSSecurityException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

