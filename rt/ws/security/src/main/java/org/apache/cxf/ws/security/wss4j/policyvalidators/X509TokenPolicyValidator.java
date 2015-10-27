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
name|wss4j
operator|.
name|common
operator|.
name|bsp
operator|.
name|BSPEnforcer
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
name|token
operator|.
name|BinarySecurity
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
name|token
operator|.
name|X509Security
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
name|XMLUtils
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
name|engine
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
name|str
operator|.
name|STRParser
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
name|X509Token
operator|.
name|TokenType
import|;
end_import

begin_comment
comment|/**  * Validate an X509 Token policy.  */
end_comment

begin_class
specifier|public
class|class
name|X509TokenPolicyValidator
extends|extends
name|AbstractSecurityPolicyValidator
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
name|X509TokenPolicyValidator
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|X509_V3_VALUETYPE
init|=
name|WSConstants
operator|.
name|X509TOKEN_NS
operator|+
literal|"#X509v3"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PKI_VALUETYPE
init|=
name|WSConstants
operator|.
name|X509TOKEN_NS
operator|+
literal|"#X509PKIPathv1"
decl_stmt|;
comment|/**      * Return true if this SecurityPolicyValidator implementation is capable of validating a       * policy defined by the AssertionInfo parameter      */
specifier|public
name|boolean
name|canValidatePolicy
parameter_list|(
name|AssertionInfo
name|assertionInfo
parameter_list|)
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
name|X509_TOKEN
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
name|X509_TOKEN
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
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|bstResults
init|=
name|parameters
operator|.
name|getResults
argument_list|()
operator|.
name|getActionResults
argument_list|()
operator|.
name|get
argument_list|(
name|WSConstants
operator|.
name|BST
argument_list|)
decl_stmt|;
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|X509Token
name|x509TokenPolicy
init|=
operator|(
name|X509Token
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
name|assertToken
argument_list|(
name|x509TokenPolicy
argument_list|,
name|parameters
operator|.
name|getAssertionInfoMap
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isTokenRequired
argument_list|(
name|x509TokenPolicy
argument_list|,
name|parameters
operator|.
name|getMessage
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
operator|(
name|bstResults
operator|==
literal|null
operator|||
name|bstResults
operator|.
name|isEmpty
argument_list|()
operator|)
operator|&&
name|parameters
operator|.
name|getSignedResults
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"The received token does not match the token inclusion requirement"
argument_list|)
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
operator|!
name|checkTokenType
argument_list|(
name|x509TokenPolicy
operator|.
name|getTokenType
argument_list|()
argument_list|,
name|bstResults
argument_list|,
name|parameters
operator|.
name|getSignedResults
argument_list|()
argument_list|)
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"An incorrect X.509 Token Type is detected"
argument_list|)
expr_stmt|;
continue|continue;
block|}
block|}
block|}
specifier|private
name|void
name|assertToken
parameter_list|(
name|X509Token
name|token
parameter_list|,
name|AssertionInfoMap
name|aim
parameter_list|)
block|{
name|String
name|namespace
init|=
name|token
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
comment|// Assert references
if|if
condition|(
name|token
operator|.
name|isRequireIssuerSerialReference
argument_list|()
condition|)
block|{
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
name|SPConstants
operator|.
name|REQUIRE_ISSUER_SERIAL_REFERENCE
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|token
operator|.
name|isRequireThumbprintReference
argument_list|()
condition|)
block|{
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
name|SPConstants
operator|.
name|REQUIRE_THUMBPRINT_REFERENCE
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|token
operator|.
name|isRequireEmbeddedTokenReference
argument_list|()
condition|)
block|{
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
name|SPConstants
operator|.
name|REQUIRE_EMBEDDED_TOKEN_REFERENCE
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|token
operator|.
name|isRequireKeyIdentifierReference
argument_list|()
condition|)
block|{
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
name|SPConstants
operator|.
name|REQUIRE_KEY_IDENTIFIER_REFERENCE
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Assert TokenType
name|TokenType
name|tokenType
init|=
name|token
operator|.
name|getTokenType
argument_list|()
decl_stmt|;
if|if
condition|(
name|tokenType
operator|!=
literal|null
condition|)
block|{
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
name|tokenType
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Check that at least one received token matches the token type.      */
specifier|private
name|boolean
name|checkTokenType
parameter_list|(
name|TokenType
name|tokenType
parameter_list|,
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|bstResults
parameter_list|,
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|signedResults
parameter_list|)
block|{
if|if
condition|(
operator|(
name|bstResults
operator|==
literal|null
operator|||
name|bstResults
operator|.
name|isEmpty
argument_list|()
operator|)
operator|&&
name|signedResults
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|String
name|requiredType
init|=
name|X509_V3_VALUETYPE
decl_stmt|;
name|boolean
name|v3certRequired
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|tokenType
operator|==
name|TokenType
operator|.
name|WssX509PkiPathV1Token10
operator|||
name|tokenType
operator|==
name|TokenType
operator|.
name|WssX509PkiPathV1Token11
condition|)
block|{
name|requiredType
operator|=
name|PKI_VALUETYPE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|tokenType
operator|==
name|TokenType
operator|.
name|WssX509V3Token10
operator|||
name|tokenType
operator|==
name|TokenType
operator|.
name|WssX509V3Token11
condition|)
block|{
name|v3certRequired
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|bstResults
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|WSSecurityEngineResult
name|result
range|:
name|bstResults
control|)
block|{
name|BinarySecurity
name|binarySecurityToken
init|=
operator|(
name|BinarySecurity
operator|)
name|result
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_BINARY_SECURITY_TOKEN
argument_list|)
decl_stmt|;
if|if
condition|(
name|binarySecurityToken
operator|!=
literal|null
operator|&&
name|requiredType
operator|.
name|equals
argument_list|(
name|binarySecurityToken
operator|.
name|getValueType
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|v3certRequired
operator|&&
name|binarySecurityToken
operator|instanceof
name|X509Security
condition|)
block|{
try|try
block|{
name|X509Certificate
name|cert
init|=
operator|(
operator|(
name|X509Security
operator|)
name|binarySecurityToken
operator|)
operator|.
name|getX509Certificate
argument_list|(
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|cert
operator|!=
literal|null
operator|&&
name|cert
operator|.
name|getVersion
argument_list|()
operator|==
literal|3
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
catch|catch
parameter_list|(
name|WSSecurityException
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
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
block|}
comment|// Maybe the X.509 token was included as a KeyIdentifier
if|if
condition|(
name|X509_V3_VALUETYPE
operator|.
name|equals
argument_list|(
name|requiredType
argument_list|)
condition|)
block|{
for|for
control|(
name|WSSecurityEngineResult
name|result
range|:
name|signedResults
control|)
block|{
name|STRParser
operator|.
name|REFERENCE_TYPE
name|referenceType
init|=
operator|(
name|STRParser
operator|.
name|REFERENCE_TYPE
operator|)
name|result
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_X509_REFERENCE_TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|STRParser
operator|.
name|REFERENCE_TYPE
operator|.
name|KEY_IDENTIFIER
operator|==
name|referenceType
condition|)
block|{
name|Element
name|signatureElement
init|=
operator|(
name|Element
operator|)
name|result
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_TOKEN_ELEMENT
argument_list|)
decl_stmt|;
name|Element
name|keyIdentifier
init|=
name|getKeyIdentifier
argument_list|(
name|signatureElement
argument_list|)
decl_stmt|;
if|if
condition|(
name|keyIdentifier
operator|!=
literal|null
operator|&&
name|X509_V3_VALUETYPE
operator|.
name|equals
argument_list|(
name|keyIdentifier
operator|.
name|getAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"ValueType"
argument_list|)
argument_list|)
condition|)
block|{
try|try
block|{
name|X509Security
name|token
init|=
operator|new
name|X509Security
argument_list|(
name|keyIdentifier
argument_list|,
operator|new
name|BSPEnforcer
argument_list|(
literal|true
argument_list|)
argument_list|)
decl_stmt|;
name|X509Certificate
name|cert
init|=
name|token
operator|.
name|getX509Certificate
argument_list|(
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|cert
operator|!=
literal|null
operator|&&
name|cert
operator|.
name|getVersion
argument_list|()
operator|==
literal|3
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
catch|catch
parameter_list|(
name|WSSecurityException
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
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|Element
name|getKeyIdentifier
parameter_list|(
name|Element
name|signatureElement
parameter_list|)
block|{
if|if
condition|(
name|signatureElement
operator|!=
literal|null
condition|)
block|{
name|Element
name|keyInfoElement
init|=
name|XMLUtils
operator|.
name|getDirectChildElement
argument_list|(
name|signatureElement
argument_list|,
literal|"KeyInfo"
argument_list|,
name|WSConstants
operator|.
name|SIG_NS
argument_list|)
decl_stmt|;
if|if
condition|(
name|keyInfoElement
operator|!=
literal|null
condition|)
block|{
name|Element
name|strElement
init|=
name|XMLUtils
operator|.
name|getDirectChildElement
argument_list|(
name|keyInfoElement
argument_list|,
literal|"SecurityTokenReference"
argument_list|,
name|WSConstants
operator|.
name|WSSE_NS
argument_list|)
decl_stmt|;
if|if
condition|(
name|strElement
operator|!=
literal|null
condition|)
block|{
name|Element
name|kiElement
init|=
name|XMLUtils
operator|.
name|getDirectChildElement
argument_list|(
name|strElement
argument_list|,
literal|"KeyIdentifier"
argument_list|,
name|WSConstants
operator|.
name|WSSE_NS
argument_list|)
decl_stmt|;
return|return
name|kiElement
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

