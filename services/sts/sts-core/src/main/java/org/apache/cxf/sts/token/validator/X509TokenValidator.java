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
name|sts
operator|.
name|token
operator|.
name|validator
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
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
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
name|w3c
operator|.
name|dom
operator|.
name|Text
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
name|sts
operator|.
name|STSPropertiesMBean
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
name|sts
operator|.
name|request
operator|.
name|ReceivedToken
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
name|sts
operator|.
name|request
operator|.
name|ReceivedToken
operator|.
name|STATE
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
name|sts
operator|.
name|token
operator|.
name|realm
operator|.
name|CertConstraintsParser
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
name|sts
operator|.
name|provider
operator|.
name|model
operator|.
name|secext
operator|.
name|BinarySecurityTokenType
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
name|WSSConfig
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
name|RequestData
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
name|validate
operator|.
name|Credential
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
name|validate
operator|.
name|SignatureTrustValidator
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
name|validate
operator|.
name|Validator
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
name|exceptions
operator|.
name|XMLSecurityException
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
name|keys
operator|.
name|content
operator|.
name|X509Data
import|;
end_import

begin_comment
comment|/**  * This class validates an X.509 V.3 certificate (received as a BinarySecurityToken or an X509Data  * DOM Element). The cert must be known (or trusted) by the STS crypto object.  */
end_comment

begin_class
specifier|public
class|class
name|X509TokenValidator
implements|implements
name|TokenValidator
block|{
specifier|public
specifier|static
specifier|final
name|String
name|X509_V3_TYPE
init|=
name|WSConstants
operator|.
name|X509TOKEN_NS
operator|+
literal|"#X509v3"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|BASE64_ENCODING
init|=
name|WSConstants
operator|.
name|SOAPMESSAGE_NS
operator|+
literal|"#Base64Binary"
decl_stmt|;
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
name|X509TokenValidator
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Validator
name|validator
init|=
operator|new
name|SignatureTrustValidator
argument_list|()
decl_stmt|;
specifier|private
name|CertConstraintsParser
name|certConstraints
init|=
operator|new
name|CertConstraintsParser
argument_list|()
decl_stmt|;
comment|/**      * Set a list of Strings corresponding to regular expression constraints on the subject DN      * of a certificate      */
specifier|public
name|void
name|setSubjectConstraints
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|subjectConstraints
parameter_list|)
block|{
name|certConstraints
operator|.
name|setSubjectConstraints
argument_list|(
name|subjectConstraints
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set the WSS4J Validator instance to use to validate the token.      * @param validator the WSS4J Validator instance to use to validate the token      */
specifier|public
name|void
name|setValidator
parameter_list|(
name|Validator
name|validator
parameter_list|)
block|{
name|this
operator|.
name|validator
operator|=
name|validator
expr_stmt|;
block|}
comment|/**      * Return true if this TokenValidator implementation is capable of validating the      * ReceivedToken argument.      */
specifier|public
name|boolean
name|canHandleToken
parameter_list|(
name|ReceivedToken
name|validateTarget
parameter_list|)
block|{
return|return
name|canHandleToken
argument_list|(
name|validateTarget
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Return true if this TokenValidator implementation is capable of validating the      * ReceivedToken argument. The realm is ignored in this token Validator.      */
specifier|public
name|boolean
name|canHandleToken
parameter_list|(
name|ReceivedToken
name|validateTarget
parameter_list|,
name|String
name|realm
parameter_list|)
block|{
name|Object
name|token
init|=
name|validateTarget
operator|.
name|getToken
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|token
operator|instanceof
name|BinarySecurityTokenType
operator|)
operator|&&
name|X509_V3_TYPE
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|BinarySecurityTokenType
operator|)
name|token
operator|)
operator|.
name|getValueType
argument_list|()
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
name|token
operator|instanceof
name|Element
operator|&&
name|WSConstants
operator|.
name|SIG_NS
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|Element
operator|)
name|token
operator|)
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|&&
name|WSConstants
operator|.
name|X509_DATA_LN
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|Element
operator|)
name|token
operator|)
operator|.
name|getLocalName
argument_list|()
argument_list|)
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
comment|/**      * Validate a Token using the given TokenValidatorParameters.      */
specifier|public
name|TokenValidatorResponse
name|validateToken
parameter_list|(
name|TokenValidatorParameters
name|tokenParameters
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Validating X.509 Token"
argument_list|)
expr_stmt|;
name|STSPropertiesMBean
name|stsProperties
init|=
name|tokenParameters
operator|.
name|getStsProperties
argument_list|()
decl_stmt|;
name|Crypto
name|sigCrypto
init|=
name|stsProperties
operator|.
name|getSignatureCrypto
argument_list|()
decl_stmt|;
name|CallbackHandler
name|callbackHandler
init|=
name|stsProperties
operator|.
name|getCallbackHandler
argument_list|()
decl_stmt|;
name|RequestData
name|requestData
init|=
operator|new
name|RequestData
argument_list|()
decl_stmt|;
name|requestData
operator|.
name|setSigVerCrypto
argument_list|(
name|sigCrypto
argument_list|)
expr_stmt|;
name|requestData
operator|.
name|setWssConfig
argument_list|(
name|WSSConfig
operator|.
name|getNewInstance
argument_list|()
argument_list|)
expr_stmt|;
name|requestData
operator|.
name|setCallbackHandler
argument_list|(
name|callbackHandler
argument_list|)
expr_stmt|;
name|requestData
operator|.
name|setMsgContext
argument_list|(
name|tokenParameters
operator|.
name|getWebServiceContext
argument_list|()
operator|.
name|getMessageContext
argument_list|()
argument_list|)
expr_stmt|;
name|requestData
operator|.
name|setSubjectCertConstraints
argument_list|(
name|certConstraints
operator|.
name|getCompiledSubjectContraints
argument_list|()
argument_list|)
expr_stmt|;
name|TokenValidatorResponse
name|response
init|=
operator|new
name|TokenValidatorResponse
argument_list|()
decl_stmt|;
name|ReceivedToken
name|validateTarget
init|=
name|tokenParameters
operator|.
name|getToken
argument_list|()
decl_stmt|;
name|validateTarget
operator|.
name|setState
argument_list|(
name|STATE
operator|.
name|INVALID
argument_list|)
expr_stmt|;
name|response
operator|.
name|setToken
argument_list|(
name|validateTarget
argument_list|)
expr_stmt|;
name|BinarySecurity
name|binarySecurity
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|validateTarget
operator|.
name|isBinarySecurityToken
argument_list|()
condition|)
block|{
name|BinarySecurityTokenType
name|binarySecurityType
init|=
operator|(
name|BinarySecurityTokenType
operator|)
name|validateTarget
operator|.
name|getToken
argument_list|()
decl_stmt|;
comment|// Test the encoding type
name|String
name|encodingType
init|=
name|binarySecurityType
operator|.
name|getEncodingType
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|BASE64_ENCODING
operator|.
name|equals
argument_list|(
name|encodingType
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Bad encoding type attribute specified: "
operator|+
name|encodingType
argument_list|)
expr_stmt|;
return|return
name|response
return|;
block|}
comment|//
comment|// Turn the received JAXB object into a DOM element
comment|//
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|createDocument
argument_list|()
decl_stmt|;
name|binarySecurity
operator|=
operator|new
name|X509Security
argument_list|(
name|doc
argument_list|)
expr_stmt|;
name|binarySecurity
operator|.
name|setEncodingType
argument_list|(
name|encodingType
argument_list|)
expr_stmt|;
name|binarySecurity
operator|.
name|setValueType
argument_list|(
name|binarySecurityType
operator|.
name|getValueType
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|data
init|=
name|binarySecurityType
operator|.
name|getValue
argument_list|()
decl_stmt|;
operator|(
operator|(
name|Text
operator|)
name|binarySecurity
operator|.
name|getElement
argument_list|()
operator|.
name|getFirstChild
argument_list|()
operator|)
operator|.
name|setData
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|validateTarget
operator|.
name|isDOMElement
argument_list|()
condition|)
block|{
try|try
block|{
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|createDocument
argument_list|()
decl_stmt|;
name|binarySecurity
operator|=
operator|new
name|X509Security
argument_list|(
name|doc
argument_list|)
expr_stmt|;
name|binarySecurity
operator|.
name|setEncodingType
argument_list|(
name|BASE64_ENCODING
argument_list|)
expr_stmt|;
name|X509Data
name|x509Data
init|=
operator|new
name|X509Data
argument_list|(
operator|(
name|Element
operator|)
name|validateTarget
operator|.
name|getToken
argument_list|()
argument_list|,
literal|""
argument_list|)
decl_stmt|;
if|if
condition|(
name|x509Data
operator|.
name|containsCertificate
argument_list|()
condition|)
block|{
name|X509Certificate
name|cert
init|=
name|x509Data
operator|.
name|itemCertificate
argument_list|(
literal|0
argument_list|)
operator|.
name|getX509Certificate
argument_list|()
decl_stmt|;
operator|(
operator|(
name|X509Security
operator|)
name|binarySecurity
operator|)
operator|.
name|setX509Certificate
argument_list|(
name|cert
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|WSSecurityException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|""
argument_list|,
name|ex
argument_list|)
expr_stmt|;
return|return
name|response
return|;
block|}
catch|catch
parameter_list|(
name|XMLSecurityException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|""
argument_list|,
name|ex
argument_list|)
expr_stmt|;
return|return
name|response
return|;
block|}
block|}
else|else
block|{
return|return
name|response
return|;
block|}
comment|//
comment|// Validate the token
comment|//
try|try
block|{
name|Credential
name|credential
init|=
operator|new
name|Credential
argument_list|()
decl_stmt|;
name|credential
operator|.
name|setBinarySecurityToken
argument_list|(
name|binarySecurity
argument_list|)
expr_stmt|;
if|if
condition|(
name|sigCrypto
operator|!=
literal|null
condition|)
block|{
name|X509Certificate
name|cert
init|=
operator|(
operator|(
name|X509Security
operator|)
name|binarySecurity
operator|)
operator|.
name|getX509Certificate
argument_list|(
name|sigCrypto
argument_list|)
decl_stmt|;
name|credential
operator|.
name|setCertificates
argument_list|(
operator|new
name|X509Certificate
index|[]
block|{
name|cert
block|}
argument_list|)
expr_stmt|;
block|}
name|Credential
name|returnedCredential
init|=
name|validator
operator|.
name|validate
argument_list|(
name|credential
argument_list|,
name|requestData
argument_list|)
decl_stmt|;
name|Principal
name|principal
init|=
name|returnedCredential
operator|.
name|getPrincipal
argument_list|()
decl_stmt|;
if|if
condition|(
name|principal
operator|==
literal|null
condition|)
block|{
name|principal
operator|=
name|returnedCredential
operator|.
name|getCertificates
argument_list|()
index|[
literal|0
index|]
operator|.
name|getSubjectX500Principal
argument_list|()
expr_stmt|;
block|}
name|response
operator|.
name|setPrincipal
argument_list|(
name|principal
argument_list|)
expr_stmt|;
name|validateTarget
operator|.
name|setState
argument_list|(
name|STATE
operator|.
name|VALID
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"X.509 Token successfully validated"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSSecurityException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|""
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
return|return
name|response
return|;
block|}
block|}
end_class

end_unit

