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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Principal
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PublicKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|Certificate
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|X509Certificate
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
name|Arrays
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
name|ws
operator|.
name|security
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
name|ws
operator|.
name|security
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
name|ws
operator|.
name|security
operator|.
name|WSDerivedKeyTokenPrincipal
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
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
name|ws
operator|.
name|security
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
name|ws
operator|.
name|security
operator|.
name|saml
operator|.
name|SAMLKeyInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|saml
operator|.
name|ext
operator|.
name|AssertionWrapper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|saml
operator|.
name|ext
operator|.
name|OpenSAMLUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|util
operator|.
name|WSSecurityUtil
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

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|xml
operator|.
name|XMLObject
import|;
end_import

begin_comment
comment|/**  * internal SAMLUtils to avoid direct reference to opensaml from WSS4J interceptors.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|SAMLUtils
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
name|SAMLUtils
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|SAMLUtils
parameter_list|()
block|{     }
specifier|public
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|parseRolesInAssertion
parameter_list|(
name|Object
name|assertion
parameter_list|,
name|String
name|roleAttributeName
parameter_list|)
block|{
if|if
condition|(
operator|(
operator|(
name|AssertionWrapper
operator|)
name|assertion
operator|)
operator|.
name|getSamlVersion
argument_list|()
operator|.
name|equals
argument_list|(
name|SAMLVersion
operator|.
name|VERSION_20
argument_list|)
condition|)
block|{
return|return
name|parseRolesInAssertion
argument_list|(
operator|(
operator|(
name|AssertionWrapper
operator|)
name|assertion
operator|)
operator|.
name|getSaml2
argument_list|()
argument_list|,
name|roleAttributeName
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|parseRolesInAssertion
argument_list|(
operator|(
operator|(
name|AssertionWrapper
operator|)
name|assertion
operator|)
operator|.
name|getSaml1
argument_list|()
argument_list|,
name|roleAttributeName
argument_list|)
return|;
block|}
block|}
specifier|public
specifier|static
name|String
name|getIssuer
parameter_list|(
name|Object
name|assertion
parameter_list|)
block|{
return|return
operator|(
operator|(
name|AssertionWrapper
operator|)
name|assertion
operator|)
operator|.
name|getIssuerString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|Element
name|getAssertionElement
parameter_list|(
name|Object
name|assertion
parameter_list|)
block|{
return|return
operator|(
operator|(
name|AssertionWrapper
operator|)
name|assertion
operator|)
operator|.
name|getElement
argument_list|()
return|;
block|}
comment|//
comment|// these methods are moved from previous WSS4JInInterceptor
comment|//
specifier|private
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|parseRolesInAssertion
parameter_list|(
name|org
operator|.
name|opensaml
operator|.
name|saml1
operator|.
name|core
operator|.
name|Assertion
name|assertion
parameter_list|,
name|String
name|roleAttributeName
parameter_list|)
block|{
name|List
argument_list|<
name|org
operator|.
name|opensaml
operator|.
name|saml1
operator|.
name|core
operator|.
name|AttributeStatement
argument_list|>
name|attributeStatements
init|=
name|assertion
operator|.
name|getAttributeStatements
argument_list|()
decl_stmt|;
if|if
condition|(
name|attributeStatements
operator|==
literal|null
operator|||
name|attributeStatements
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|roles
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|org
operator|.
name|opensaml
operator|.
name|saml1
operator|.
name|core
operator|.
name|AttributeStatement
name|statement
range|:
name|attributeStatements
control|)
block|{
name|List
argument_list|<
name|org
operator|.
name|opensaml
operator|.
name|saml1
operator|.
name|core
operator|.
name|Attribute
argument_list|>
name|attributes
init|=
name|statement
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
for|for
control|(
name|org
operator|.
name|opensaml
operator|.
name|saml1
operator|.
name|core
operator|.
name|Attribute
name|attribute
range|:
name|attributes
control|)
block|{
if|if
condition|(
name|attribute
operator|.
name|getAttributeName
argument_list|()
operator|.
name|equals
argument_list|(
name|roleAttributeName
argument_list|)
condition|)
block|{
for|for
control|(
name|XMLObject
name|attributeValue
range|:
name|attribute
operator|.
name|getAttributeValues
argument_list|()
control|)
block|{
name|Element
name|attributeValueElement
init|=
name|attributeValue
operator|.
name|getDOM
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|attributeValueElement
operator|.
name|getTextContent
argument_list|()
decl_stmt|;
name|roles
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|attribute
operator|.
name|getAttributeValues
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
comment|//                        Don't search for other attributes with the same name if
comment|//<saml:Attribute xmlns:saml="urn:oasis:names:tc:SAML:1.0:assertion"
comment|//                             AttributeNamespace="http://schemas.xmlsoap.org/claims" AttributeName="roles">
comment|//<saml:AttributeValue>Value1</saml:AttributeValue>
comment|//<saml:AttributeValue>Value2</saml:AttributeValue>
comment|//</saml:Attribute>
break|break;
block|}
block|}
block|}
block|}
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|roles
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|parseRolesInAssertion
parameter_list|(
name|org
operator|.
name|opensaml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Assertion
name|assertion
parameter_list|,
name|String
name|roleAttributeName
parameter_list|)
block|{
name|List
argument_list|<
name|org
operator|.
name|opensaml
operator|.
name|saml2
operator|.
name|core
operator|.
name|AttributeStatement
argument_list|>
name|attributeStatements
init|=
name|assertion
operator|.
name|getAttributeStatements
argument_list|()
decl_stmt|;
if|if
condition|(
name|attributeStatements
operator|==
literal|null
operator|||
name|attributeStatements
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|roles
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|org
operator|.
name|opensaml
operator|.
name|saml2
operator|.
name|core
operator|.
name|AttributeStatement
name|statement
range|:
name|attributeStatements
control|)
block|{
name|List
argument_list|<
name|org
operator|.
name|opensaml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Attribute
argument_list|>
name|attributes
init|=
name|statement
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
for|for
control|(
name|org
operator|.
name|opensaml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Attribute
name|attribute
range|:
name|attributes
control|)
block|{
if|if
condition|(
name|attribute
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|roleAttributeName
argument_list|)
condition|)
block|{
for|for
control|(
name|XMLObject
name|attributeValue
range|:
name|attribute
operator|.
name|getAttributeValues
argument_list|()
control|)
block|{
name|Element
name|attributeValueElement
init|=
name|attributeValue
operator|.
name|getDOM
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|attributeValueElement
operator|.
name|getTextContent
argument_list|()
decl_stmt|;
name|roles
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|attribute
operator|.
name|getAttributeValues
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
comment|//                        Don't search for other attributes with the same name if
comment|//<saml:Attribute xmlns:saml="urn:oasis:names:tc:SAML:1.0:assertion"
comment|//                             AttributeNamespace="http://schemas.xmlsoap.org/claims" AttributeName="roles">
comment|//<saml:AttributeValue>Value1</saml:AttributeValue>
comment|//<saml:AttributeValue>Value2</saml:AttributeValue>
comment|//</saml:Attribute>
break|break;
block|}
block|}
block|}
block|}
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|roles
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|void
name|validateSAMLResults
parameter_list|(
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|results
parameter_list|,
name|Message
name|message
parameter_list|,
name|Element
name|body
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|samlResults
init|=
operator|new
name|ArrayList
argument_list|<
name|WSSecurityEngineResult
argument_list|>
argument_list|()
decl_stmt|;
name|WSSecurityUtil
operator|.
name|fetchAllActionResults
argument_list|(
name|results
argument_list|,
name|WSConstants
operator|.
name|ST_SIGNED
argument_list|,
name|samlResults
argument_list|)
expr_stmt|;
name|WSSecurityUtil
operator|.
name|fetchAllActionResults
argument_list|(
name|results
argument_list|,
name|WSConstants
operator|.
name|ST_UNSIGNED
argument_list|,
name|samlResults
argument_list|)
expr_stmt|;
if|if
condition|(
name|samlResults
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|signedResults
init|=
operator|new
name|ArrayList
argument_list|<
name|WSSecurityEngineResult
argument_list|>
argument_list|()
decl_stmt|;
name|WSSecurityUtil
operator|.
name|fetchAllActionResults
argument_list|(
name|results
argument_list|,
name|WSConstants
operator|.
name|SIGN
argument_list|,
name|signedResults
argument_list|)
expr_stmt|;
name|WSSecurityUtil
operator|.
name|fetchAllActionResults
argument_list|(
name|results
argument_list|,
name|WSConstants
operator|.
name|UT_SIGN
argument_list|,
name|signedResults
argument_list|)
expr_stmt|;
for|for
control|(
name|WSSecurityEngineResult
name|samlResult
range|:
name|samlResults
control|)
block|{
name|AssertionWrapper
name|assertionWrapper
init|=
operator|(
name|AssertionWrapper
operator|)
name|samlResult
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_SAML_ASSERTION
argument_list|)
decl_stmt|;
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
name|Certificate
index|[]
name|tlsCerts
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|tlsInfo
operator|!=
literal|null
condition|)
block|{
name|tlsCerts
operator|=
name|tlsInfo
operator|.
name|getPeerCertificates
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|SAMLUtils
operator|.
name|checkHolderOfKey
argument_list|(
name|assertionWrapper
argument_list|,
name|signedResults
argument_list|,
name|tlsCerts
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Assertion fails holder-of-key requirements"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|INVALID_SECURITY
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|SAMLUtils
operator|.
name|checkSenderVouches
argument_list|(
name|assertionWrapper
argument_list|,
name|tlsCerts
argument_list|,
name|body
argument_list|,
name|signedResults
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Assertion fails sender-vouches requirements"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|INVALID_SECURITY
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**      * Check the holder-of-key requirements against the received assertion. The subject      * credential of the SAML Assertion must have been used to sign some portion of      * the message, thus showing proof-of-possession of the private/secret key. Alternatively,      * the subject credential of the SAML Assertion must match a client certificate credential      * when 2-way TLS is used.      * @param assertionWrapper the SAML Assertion wrapper object      * @param signedResults a list of all of the signed results      */
specifier|public
specifier|static
name|boolean
name|checkHolderOfKey
parameter_list|(
name|AssertionWrapper
name|assertionWrapper
parameter_list|,
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|signedResults
parameter_list|,
name|Certificate
index|[]
name|tlsCerts
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|confirmationMethods
init|=
name|assertionWrapper
operator|.
name|getConfirmationMethods
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|confirmationMethod
range|:
name|confirmationMethods
control|)
block|{
if|if
condition|(
name|OpenSAMLUtil
operator|.
name|isMethodHolderOfKey
argument_list|(
name|confirmationMethod
argument_list|)
condition|)
block|{
if|if
condition|(
name|tlsCerts
operator|==
literal|null
operator|&&
operator|(
name|signedResults
operator|==
literal|null
operator|||
name|signedResults
operator|.
name|isEmpty
argument_list|()
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|SAMLKeyInfo
name|subjectKeyInfo
init|=
name|assertionWrapper
operator|.
name|getSubjectKeyInfo
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|compareCredentials
argument_list|(
name|subjectKeyInfo
argument_list|,
name|signedResults
argument_list|,
name|tlsCerts
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/**      * Compare the credentials of the assertion to the credentials used in 2-way TLS or those      * used to verify signatures.      * Return true on a match      * @param subjectKeyInfo the SAMLKeyInfo object      * @param signedResults a list of all of the signed results      * @return true if the credentials of the assertion were used to verify a signature      */
specifier|public
specifier|static
name|boolean
name|compareCredentials
parameter_list|(
name|SAMLKeyInfo
name|subjectKeyInfo
parameter_list|,
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|signedResults
parameter_list|,
name|Certificate
index|[]
name|tlsCerts
parameter_list|)
block|{
name|X509Certificate
index|[]
name|subjectCerts
init|=
name|subjectKeyInfo
operator|.
name|getCerts
argument_list|()
decl_stmt|;
name|PublicKey
name|subjectPublicKey
init|=
name|subjectKeyInfo
operator|.
name|getPublicKey
argument_list|()
decl_stmt|;
name|byte
index|[]
name|subjectSecretKey
init|=
name|subjectKeyInfo
operator|.
name|getSecret
argument_list|()
decl_stmt|;
comment|//
comment|// Try to match the TLS certs first
comment|//
if|if
condition|(
name|tlsCerts
operator|!=
literal|null
operator|&&
name|tlsCerts
operator|.
name|length
operator|>
literal|0
operator|&&
name|subjectCerts
operator|!=
literal|null
operator|&&
name|subjectCerts
operator|.
name|length
operator|>
literal|0
operator|&&
name|tlsCerts
index|[
literal|0
index|]
operator|.
name|equals
argument_list|(
name|subjectCerts
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|tlsCerts
operator|!=
literal|null
operator|&&
name|tlsCerts
operator|.
name|length
operator|>
literal|0
operator|&&
name|subjectPublicKey
operator|!=
literal|null
operator|&&
name|tlsCerts
index|[
literal|0
index|]
operator|.
name|getPublicKey
argument_list|()
operator|.
name|equals
argument_list|(
name|subjectPublicKey
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
comment|//
comment|// Now try the message-level signatures
comment|//
for|for
control|(
name|WSSecurityEngineResult
name|signedResult
range|:
name|signedResults
control|)
block|{
name|X509Certificate
index|[]
name|certs
init|=
operator|(
name|X509Certificate
index|[]
operator|)
name|signedResult
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_X509_CERTIFICATES
argument_list|)
decl_stmt|;
name|PublicKey
name|publicKey
init|=
operator|(
name|PublicKey
operator|)
name|signedResult
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_PUBLIC_KEY
argument_list|)
decl_stmt|;
name|byte
index|[]
name|secretKey
init|=
operator|(
name|byte
index|[]
operator|)
name|signedResult
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
name|certs
operator|!=
literal|null
operator|&&
name|certs
operator|.
name|length
operator|>
literal|0
operator|&&
name|subjectCerts
operator|!=
literal|null
operator|&&
name|subjectCerts
operator|.
name|length
operator|>
literal|0
operator|&&
name|certs
index|[
literal|0
index|]
operator|.
name|equals
argument_list|(
name|subjectCerts
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|publicKey
operator|!=
literal|null
operator|&&
name|publicKey
operator|.
name|equals
argument_list|(
name|subjectPublicKey
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|checkSecretKey
argument_list|(
name|secretKey
argument_list|,
name|subjectSecretKey
argument_list|,
name|signedResult
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
name|boolean
name|checkSecretKey
parameter_list|(
name|byte
index|[]
name|secretKey
parameter_list|,
name|byte
index|[]
name|subjectSecretKey
parameter_list|,
name|WSSecurityEngineResult
name|signedResult
parameter_list|)
block|{
if|if
condition|(
name|secretKey
operator|!=
literal|null
operator|&&
name|subjectSecretKey
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|Arrays
operator|.
name|equals
argument_list|(
name|secretKey
argument_list|,
name|subjectSecretKey
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
else|else
block|{
name|Principal
name|principal
init|=
operator|(
name|Principal
operator|)
name|signedResult
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_PRINCIPAL
argument_list|)
decl_stmt|;
if|if
condition|(
name|principal
operator|instanceof
name|WSDerivedKeyTokenPrincipal
condition|)
block|{
name|secretKey
operator|=
operator|(
operator|(
name|WSDerivedKeyTokenPrincipal
operator|)
name|principal
operator|)
operator|.
name|getSecret
argument_list|()
expr_stmt|;
if|if
condition|(
name|Arrays
operator|.
name|equals
argument_list|(
name|secretKey
argument_list|,
name|subjectSecretKey
argument_list|)
condition|)
block|{
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
comment|/**      * Check the sender-vouches requirements against the received assertion. The SAML      * Assertion and the SOAP Body must be signed by the same signature.      */
specifier|public
specifier|static
name|boolean
name|checkSenderVouches
parameter_list|(
name|AssertionWrapper
name|assertionWrapper
parameter_list|,
name|Certificate
index|[]
name|tlsCerts
parameter_list|,
name|Element
name|body
parameter_list|,
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|signed
parameter_list|)
block|{
comment|//
comment|// If we have a 2-way TLS connection, then we don't have to check that the
comment|// assertion + SOAP body are signed
comment|//
if|if
condition|(
name|tlsCerts
operator|!=
literal|null
operator|&&
name|tlsCerts
operator|.
name|length
operator|>
literal|0
condition|)
block|{
return|return
literal|true
return|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|confirmationMethods
init|=
name|assertionWrapper
operator|.
name|getConfirmationMethods
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|confirmationMethod
range|:
name|confirmationMethods
control|)
block|{
if|if
condition|(
name|OpenSAMLUtil
operator|.
name|isMethodSenderVouches
argument_list|(
name|confirmationMethod
argument_list|)
condition|)
block|{
if|if
condition|(
name|signed
operator|==
literal|null
operator|||
name|signed
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|checkAssertionAndBodyAreSigned
argument_list|(
name|assertionWrapper
argument_list|,
name|body
argument_list|,
name|signed
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/**      * Return true if there is a signature which references the Assertion and the SOAP Body.      * @param assertionWrapper the AssertionWrapper object      * @param body The SOAP body      * @param signed The List of signed results      * @return true if there is a signature which references the Assertion and the SOAP Body.      */
specifier|private
specifier|static
name|boolean
name|checkAssertionAndBodyAreSigned
parameter_list|(
name|AssertionWrapper
name|assertionWrapper
parameter_list|,
name|Element
name|body
parameter_list|,
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|signed
parameter_list|)
block|{
for|for
control|(
name|WSSecurityEngineResult
name|signedResult
range|:
name|signed
control|)
block|{
name|List
argument_list|<
name|WSDataRef
argument_list|>
name|sl
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
name|signedResult
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_DATA_REF_URIS
argument_list|)
argument_list|)
decl_stmt|;
name|boolean
name|assertionIsSigned
init|=
literal|false
decl_stmt|;
name|boolean
name|bodyIsSigned
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|sl
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|WSDataRef
name|dataRef
range|:
name|sl
control|)
block|{
name|Element
name|se
init|=
name|dataRef
operator|.
name|getProtectedElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|se
operator|==
name|assertionWrapper
operator|.
name|getElement
argument_list|()
condition|)
block|{
name|assertionIsSigned
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|se
operator|==
name|body
condition|)
block|{
name|bodyIsSigned
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|assertionIsSigned
operator|&&
name|bodyIsSigned
condition|)
block|{
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
block|}
end_class

end_unit

