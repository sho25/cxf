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
name|model
package|;
end_package

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
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamWriter
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
name|i18n
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
name|security
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
name|cxf
operator|.
name|ws
operator|.
name|security
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
name|cxf
operator|.
name|ws
operator|.
name|security
operator|.
name|policy
operator|.
name|WSSPolicyException
import|;
end_import

begin_class
specifier|public
class|class
name|AlgorithmSuite
extends|extends
name|AbstractSecurityAssertion
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
name|AlgorithmSuite
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|String
name|algoSuiteString
decl_stmt|;
specifier|protected
name|String
name|symmetricSignature
init|=
name|SPConstants
operator|.
name|HMAC_SHA1
decl_stmt|;
specifier|protected
name|String
name|asymmetricSignature
init|=
name|SPConstants
operator|.
name|RSA_SHA1
decl_stmt|;
specifier|protected
name|String
name|computedKey
init|=
name|SPConstants
operator|.
name|P_SHA1
decl_stmt|;
specifier|protected
name|int
name|maximumSymmetricKeyLength
init|=
literal|256
decl_stmt|;
specifier|protected
name|int
name|minimumAsymmetricKeyLength
init|=
literal|1024
decl_stmt|;
specifier|protected
name|int
name|maximumAsymmetricKeyLength
init|=
literal|4096
decl_stmt|;
specifier|protected
name|String
name|digest
decl_stmt|;
specifier|protected
name|String
name|encryption
decl_stmt|;
specifier|protected
name|String
name|symmetricKeyWrap
decl_stmt|;
specifier|protected
name|String
name|asymmetricKeyWrap
decl_stmt|;
specifier|protected
name|String
name|encryptionKeyDerivation
decl_stmt|;
specifier|protected
name|int
name|encryptionDerivedKeyLength
decl_stmt|;
specifier|protected
name|String
name|signatureKeyDerivation
decl_stmt|;
specifier|protected
name|int
name|signatureDerivedKeyLength
decl_stmt|;
specifier|protected
name|int
name|minimumSymmetricKeyLength
decl_stmt|;
specifier|protected
name|String
name|c14n
init|=
name|SPConstants
operator|.
name|EX_C14N
decl_stmt|;
specifier|protected
name|String
name|soapNormalization
decl_stmt|;
specifier|protected
name|String
name|strTransform
decl_stmt|;
specifier|protected
name|String
name|xPath
decl_stmt|;
specifier|public
name|AlgorithmSuite
parameter_list|(
name|SPConstants
name|version
parameter_list|)
block|{
name|super
argument_list|(
name|version
argument_list|)
expr_stmt|;
block|}
specifier|public
name|AlgorithmSuite
parameter_list|()
block|{
name|super
argument_list|(
name|SP12Constants
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return Returns the asymmetricKeyWrap.      */
specifier|public
name|String
name|getAsymmetricKeyWrap
parameter_list|()
block|{
return|return
name|asymmetricKeyWrap
return|;
block|}
comment|/**      * @return Returns the asymmetricSignature.      */
specifier|public
name|String
name|getAsymmetricSignature
parameter_list|()
block|{
return|return
name|asymmetricSignature
return|;
block|}
comment|/**      * @return Returns the computedKey.      */
specifier|public
name|String
name|getComputedKey
parameter_list|()
block|{
return|return
name|computedKey
return|;
block|}
comment|/**      * @return Returns the digest.      */
specifier|public
name|String
name|getDigest
parameter_list|()
block|{
return|return
name|digest
return|;
block|}
comment|/**      * @return Returns the encryption.      */
specifier|public
name|String
name|getEncryption
parameter_list|()
block|{
return|return
name|encryption
return|;
block|}
comment|/**      * @return Returns the encryptionKeyDerivation.      */
specifier|public
name|String
name|getEncryptionKeyDerivation
parameter_list|()
block|{
return|return
name|encryptionKeyDerivation
return|;
block|}
comment|/**      * @return Returns the maximumAsymmetricKeyLength.      */
specifier|public
name|int
name|getMaximumAsymmetricKeyLength
parameter_list|()
block|{
return|return
name|maximumAsymmetricKeyLength
return|;
block|}
comment|/**      * @return Returns the maximumSymmetricKeyLength.      */
specifier|public
name|int
name|getMaximumSymmetricKeyLength
parameter_list|()
block|{
return|return
name|maximumSymmetricKeyLength
return|;
block|}
comment|/**      * @return Returns the minimumAsymmetricKeyLength.      */
specifier|public
name|int
name|getMinimumAsymmetricKeyLength
parameter_list|()
block|{
return|return
name|minimumAsymmetricKeyLength
return|;
block|}
comment|/**      * @return Returns the minimumSymmetricKeyLength.      */
specifier|public
name|int
name|getMinimumSymmetricKeyLength
parameter_list|()
block|{
return|return
name|minimumSymmetricKeyLength
return|;
block|}
comment|/**      * @return Returns the signatureKeyDerivation.      */
specifier|public
name|String
name|getSignatureKeyDerivation
parameter_list|()
block|{
return|return
name|signatureKeyDerivation
return|;
block|}
comment|/**      * @return Returns the symmetricKeyWrap.      */
specifier|public
name|String
name|getSymmetricKeyWrap
parameter_list|()
block|{
return|return
name|symmetricKeyWrap
return|;
block|}
comment|/**      * @return Returns the symmetricSignature.      */
specifier|public
name|String
name|getSymmetricSignature
parameter_list|()
block|{
return|return
name|symmetricSignature
return|;
block|}
comment|/**      * @return Returns the c14n.      */
specifier|public
name|String
name|getInclusiveC14n
parameter_list|()
block|{
return|return
name|c14n
return|;
block|}
comment|/**      * @param c14n The c14n to set.      */
specifier|public
name|void
name|setC14n
parameter_list|(
name|String
name|c14n
parameter_list|)
block|{
name|this
operator|.
name|c14n
operator|=
name|c14n
expr_stmt|;
block|}
comment|/**      * @return Returns the soapNormalization.      */
specifier|public
name|String
name|getSoapNormalization
parameter_list|()
block|{
return|return
name|soapNormalization
return|;
block|}
comment|/**      * @param soapNormalization The soapNormalization to set.      */
specifier|public
name|void
name|setSoapNormalization
parameter_list|(
name|String
name|soapNormalization
parameter_list|)
block|{
name|this
operator|.
name|soapNormalization
operator|=
name|soapNormalization
expr_stmt|;
block|}
comment|/**      * @return Returns the strTransform.      */
specifier|public
name|String
name|getStrTransform
parameter_list|()
block|{
return|return
name|strTransform
return|;
block|}
comment|/**      * @param strTransform The strTransform to set.      */
specifier|public
name|void
name|setStrTransform
parameter_list|(
name|String
name|strTransform
parameter_list|)
block|{
name|this
operator|.
name|strTransform
operator|=
name|strTransform
expr_stmt|;
block|}
comment|/**      * @return Returns the xPath.      */
specifier|public
name|String
name|getXPath
parameter_list|()
block|{
return|return
name|xPath
return|;
block|}
comment|/**      * @param path The xPath to set.      */
specifier|public
name|void
name|setXPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|xPath
operator|=
name|path
expr_stmt|;
block|}
specifier|private
name|void
name|setAlgoSuiteString
parameter_list|(
name|String
name|algoSuiteString
parameter_list|)
block|{
name|this
operator|.
name|algoSuiteString
operator|=
name|algoSuiteString
expr_stmt|;
block|}
specifier|private
name|String
name|getAlgoSuiteString
parameter_list|()
block|{
return|return
name|algoSuiteString
return|;
block|}
specifier|public
name|QName
name|getName
parameter_list|()
block|{
return|return
name|SP12Constants
operator|.
name|INSTANCE
operator|.
name|getAlgorithmSuite
argument_list|()
return|;
block|}
specifier|public
name|QName
name|getRealName
parameter_list|()
block|{
return|return
name|constants
operator|.
name|getAlgorithmSuite
argument_list|()
return|;
block|}
specifier|public
name|void
name|serialize
parameter_list|(
name|XMLStreamWriter
name|writer
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|String
name|localName
init|=
name|getRealName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
decl_stmt|;
name|String
name|namespaceURI
init|=
name|getRealName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
name|String
name|prefix
init|=
name|writer
operator|.
name|getPrefix
argument_list|(
name|namespaceURI
argument_list|)
decl_stmt|;
if|if
condition|(
name|prefix
operator|==
literal|null
condition|)
block|{
name|prefix
operator|=
name|getRealName
argument_list|()
operator|.
name|getPrefix
argument_list|()
expr_stmt|;
name|writer
operator|.
name|setPrefix
argument_list|(
name|prefix
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|localName
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeNamespace
argument_list|(
name|prefix
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
comment|//<wsp:Policy>
name|String
name|wspPrefix
init|=
name|writer
operator|.
name|getPrefix
argument_list|(
name|SPConstants
operator|.
name|POLICY
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|wspPrefix
operator|==
literal|null
condition|)
block|{
name|wspPrefix
operator|=
name|SPConstants
operator|.
name|POLICY
operator|.
name|getPrefix
argument_list|()
expr_stmt|;
name|writer
operator|.
name|setPrefix
argument_list|(
name|wspPrefix
argument_list|,
name|SPConstants
operator|.
name|POLICY
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|writeStartElement
argument_list|(
name|wspPrefix
argument_list|,
name|SPConstants
operator|.
name|POLICY
operator|.
name|getLocalPart
argument_list|()
argument_list|,
name|SPConstants
operator|.
name|POLICY
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
comment|//
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|getAlgoSuiteString
argument_list|()
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
if|if
condition|(
name|SPConstants
operator|.
name|C14N
operator|.
name|equals
argument_list|(
name|getInclusiveC14n
argument_list|()
argument_list|)
condition|)
block|{
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|SPConstants
operator|.
name|INCLUSIVE_C14N
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|SPConstants
operator|.
name|SNT
operator|.
name|equals
argument_list|(
name|getSoapNormalization
argument_list|()
argument_list|)
condition|)
block|{
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|SPConstants
operator|.
name|SOAP_NORMALIZATION_10
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|SPConstants
operator|.
name|STRT10
operator|.
name|equals
argument_list|(
name|getStrTransform
argument_list|()
argument_list|)
condition|)
block|{
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|SPConstants
operator|.
name|STR_TRANSFORM_10
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|SPConstants
operator|.
name|XPATH
operator|.
name|equals
argument_list|(
name|getXPath
argument_list|()
argument_list|)
condition|)
block|{
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|SPConstants
operator|.
name|XPATH10
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|SPConstants
operator|.
name|XPATH20
operator|.
name|equals
argument_list|(
name|getXPath
argument_list|()
argument_list|)
condition|)
block|{
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|SPConstants
operator|.
name|XPATH_FILTER20
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
comment|//</wsp:Policy>
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
comment|//</sp:AlgorithmSuite>
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
specifier|public
name|int
name|getEncryptionDerivedKeyLength
parameter_list|()
block|{
return|return
name|encryptionDerivedKeyLength
return|;
block|}
specifier|public
name|int
name|getSignatureDerivedKeyLength
parameter_list|()
block|{
return|return
name|signatureDerivedKeyLength
return|;
block|}
specifier|public
name|void
name|setAsymmetricKeyWrap
parameter_list|(
name|String
name|asymmetricKeyWrap
parameter_list|)
block|{
name|this
operator|.
name|asymmetricKeyWrap
operator|=
name|asymmetricKeyWrap
expr_stmt|;
block|}
comment|/**      * Set the algorithm suite      *       * @param algoSuite      * @throws WSSPolicyException      * @see SPConstants#ALGO_SUITE_BASIC128      * @see SPConstants#ALGO_SUITE_BASIC128_RSA15      * @see SPConstants#ALGO_SUITE_BASIC128_SHA256      * @see SPConstants#ALGO_SUITE_BASIC128_SHA256_RSA15      * @see SPConstants#ALGO_SUITE_BASIC192      * @see SPConstants#ALGO_SUITE_BASIC192_RSA15      * @see SPConstants#ALGO_SUITE_BASIC192_SHA256      * @see SPConstants#ALGO_SUITE_BASIC192_SHA256_RSA15      * @see SPConstants#ALGO_SUITE_BASIC256      * @see SPConstants#ALGO_SUITE_BASIC256_RSA15      * @see SPConstants#ALGO_SUITE_BASIC256_SHA256      * @see SPConstants#ALGO_SUITE_BASIC256_SHA256_RSA15      * @see SPConstants#ALGO_SUITE_TRIPLE_DES      * @see SPConstants#ALGO_SUITE_TRIPLE_DES_RSA15      * @see SPConstants#ALGO_SUITE_TRIPLE_DES_SHA256      * @see SPConstants#ALGO_SUITE_TRIPLE_DES_SHA256_RSA15      */
comment|//CHECKSTYLE:OFF
specifier|public
name|void
name|setAlgorithmSuite
parameter_list|(
name|String
name|algoSuite
parameter_list|)
throws|throws
name|WSSPolicyException
block|{
name|setAlgoSuiteString
argument_list|(
name|algoSuite
argument_list|)
expr_stmt|;
name|this
operator|.
name|algoSuiteString
operator|=
name|algoSuite
expr_stmt|;
if|if
condition|(
name|algoSuiteString
operator|!=
literal|null
operator|&&
name|algoSuiteString
operator|.
name|endsWith
argument_list|(
literal|"Rsa15"
argument_list|)
condition|)
block|{
name|int
name|index
init|=
name|algoSuiteString
operator|.
name|indexOf
argument_list|(
literal|"Rsa15"
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|warning
argument_list|(
literal|"An Rsa15 AlgorithmSuite - "
operator|+
name|algoSuiteString
operator|+
literal|" - has been configured, "
operator|+
literal|"which is not recommended. Please consider using "
operator|+
name|algoSuiteString
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
operator|+
literal|" instead."
argument_list|)
expr_stmt|;
block|}
comment|// TODO: Optimize this :-)
if|if
condition|(
name|SPConstants
operator|.
name|ALGO_SUITE_BASIC256
operator|.
name|equals
argument_list|(
name|algoSuite
argument_list|)
condition|)
block|{
name|this
operator|.
name|digest
operator|=
name|SPConstants
operator|.
name|SHA1
expr_stmt|;
name|this
operator|.
name|encryption
operator|=
name|SPConstants
operator|.
name|AES256
expr_stmt|;
name|this
operator|.
name|symmetricKeyWrap
operator|=
name|SPConstants
operator|.
name|KW_AES256
expr_stmt|;
name|this
operator|.
name|asymmetricKeyWrap
operator|=
name|SPConstants
operator|.
name|KW_RSA_OAEP
expr_stmt|;
name|this
operator|.
name|encryptionKeyDerivation
operator|=
name|SPConstants
operator|.
name|P_SHA1_L256
expr_stmt|;
name|this
operator|.
name|signatureKeyDerivation
operator|=
name|SPConstants
operator|.
name|P_SHA1_L192
expr_stmt|;
name|this
operator|.
name|encryptionDerivedKeyLength
operator|=
literal|256
expr_stmt|;
name|this
operator|.
name|signatureDerivedKeyLength
operator|=
literal|192
expr_stmt|;
name|this
operator|.
name|minimumSymmetricKeyLength
operator|=
literal|256
expr_stmt|;
name|this
operator|.
name|encryptionDerivedKeyLength
operator|=
literal|256
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SPConstants
operator|.
name|ALGO_SUITE_BASIC192
operator|.
name|equals
argument_list|(
name|algoSuite
argument_list|)
condition|)
block|{
name|this
operator|.
name|digest
operator|=
name|SPConstants
operator|.
name|SHA1
expr_stmt|;
name|this
operator|.
name|encryption
operator|=
name|SPConstants
operator|.
name|AES192
expr_stmt|;
name|this
operator|.
name|symmetricKeyWrap
operator|=
name|SPConstants
operator|.
name|KW_AES192
expr_stmt|;
name|this
operator|.
name|asymmetricKeyWrap
operator|=
name|SPConstants
operator|.
name|KW_RSA_OAEP
expr_stmt|;
name|this
operator|.
name|encryptionKeyDerivation
operator|=
name|SPConstants
operator|.
name|P_SHA1_L192
expr_stmt|;
name|this
operator|.
name|signatureKeyDerivation
operator|=
name|SPConstants
operator|.
name|P_SHA1_L192
expr_stmt|;
name|this
operator|.
name|encryptionDerivedKeyLength
operator|=
literal|192
expr_stmt|;
name|this
operator|.
name|signatureDerivedKeyLength
operator|=
literal|192
expr_stmt|;
name|this
operator|.
name|minimumSymmetricKeyLength
operator|=
literal|192
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SPConstants
operator|.
name|ALGO_SUITE_BASIC128
operator|.
name|equals
argument_list|(
name|algoSuite
argument_list|)
condition|)
block|{
name|this
operator|.
name|digest
operator|=
name|SPConstants
operator|.
name|SHA1
expr_stmt|;
name|this
operator|.
name|encryption
operator|=
name|SPConstants
operator|.
name|AES128
expr_stmt|;
name|this
operator|.
name|symmetricKeyWrap
operator|=
name|SPConstants
operator|.
name|KW_AES128
expr_stmt|;
name|this
operator|.
name|asymmetricKeyWrap
operator|=
name|SPConstants
operator|.
name|KW_RSA_OAEP
expr_stmt|;
name|this
operator|.
name|encryptionKeyDerivation
operator|=
name|SPConstants
operator|.
name|P_SHA1_L128
expr_stmt|;
name|this
operator|.
name|signatureKeyDerivation
operator|=
name|SPConstants
operator|.
name|P_SHA1_L128
expr_stmt|;
name|this
operator|.
name|encryptionDerivedKeyLength
operator|=
literal|128
expr_stmt|;
name|this
operator|.
name|signatureDerivedKeyLength
operator|=
literal|128
expr_stmt|;
name|this
operator|.
name|minimumSymmetricKeyLength
operator|=
literal|128
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SPConstants
operator|.
name|ALGO_SUITE_TRIPLE_DES
operator|.
name|equals
argument_list|(
name|algoSuite
argument_list|)
condition|)
block|{
name|this
operator|.
name|digest
operator|=
name|SPConstants
operator|.
name|SHA1
expr_stmt|;
name|this
operator|.
name|encryption
operator|=
name|SPConstants
operator|.
name|TRIPLE_DES
expr_stmt|;
name|this
operator|.
name|symmetricKeyWrap
operator|=
name|SPConstants
operator|.
name|KW_TRIPLE_DES
expr_stmt|;
name|this
operator|.
name|asymmetricKeyWrap
operator|=
name|SPConstants
operator|.
name|KW_RSA_OAEP
expr_stmt|;
name|this
operator|.
name|encryptionKeyDerivation
operator|=
name|SPConstants
operator|.
name|P_SHA1_L192
expr_stmt|;
name|this
operator|.
name|signatureKeyDerivation
operator|=
name|SPConstants
operator|.
name|P_SHA1_L192
expr_stmt|;
name|this
operator|.
name|encryptionDerivedKeyLength
operator|=
literal|192
expr_stmt|;
name|this
operator|.
name|signatureDerivedKeyLength
operator|=
literal|192
expr_stmt|;
name|this
operator|.
name|minimumSymmetricKeyLength
operator|=
literal|192
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SPConstants
operator|.
name|ALGO_SUITE_BASIC256_RSA15
operator|.
name|equals
argument_list|(
name|algoSuite
argument_list|)
condition|)
block|{
name|this
operator|.
name|digest
operator|=
name|SPConstants
operator|.
name|SHA1
expr_stmt|;
name|this
operator|.
name|encryption
operator|=
name|SPConstants
operator|.
name|AES256
expr_stmt|;
name|this
operator|.
name|symmetricKeyWrap
operator|=
name|SPConstants
operator|.
name|KW_AES256
expr_stmt|;
name|this
operator|.
name|asymmetricKeyWrap
operator|=
name|SPConstants
operator|.
name|KW_RSA15
expr_stmt|;
name|this
operator|.
name|encryptionKeyDerivation
operator|=
name|SPConstants
operator|.
name|P_SHA1_L256
expr_stmt|;
name|this
operator|.
name|signatureKeyDerivation
operator|=
name|SPConstants
operator|.
name|P_SHA1_L192
expr_stmt|;
name|this
operator|.
name|encryptionDerivedKeyLength
operator|=
literal|256
expr_stmt|;
name|this
operator|.
name|signatureDerivedKeyLength
operator|=
literal|192
expr_stmt|;
name|this
operator|.
name|minimumSymmetricKeyLength
operator|=
literal|256
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SPConstants
operator|.
name|ALGO_SUITE_BASIC192_RSA15
operator|.
name|equals
argument_list|(
name|algoSuite
argument_list|)
condition|)
block|{
name|this
operator|.
name|digest
operator|=
name|SPConstants
operator|.
name|SHA1
expr_stmt|;
name|this
operator|.
name|encryption
operator|=
name|SPConstants
operator|.
name|AES192
expr_stmt|;
name|this
operator|.
name|symmetricKeyWrap
operator|=
name|SPConstants
operator|.
name|KW_AES192
expr_stmt|;
name|this
operator|.
name|asymmetricKeyWrap
operator|=
name|SPConstants
operator|.
name|KW_RSA15
expr_stmt|;
name|this
operator|.
name|encryptionKeyDerivation
operator|=
name|SPConstants
operator|.
name|P_SHA1_L192
expr_stmt|;
name|this
operator|.
name|signatureKeyDerivation
operator|=
name|SPConstants
operator|.
name|P_SHA1_L192
expr_stmt|;
name|this
operator|.
name|encryptionDerivedKeyLength
operator|=
literal|192
expr_stmt|;
name|this
operator|.
name|signatureDerivedKeyLength
operator|=
literal|192
expr_stmt|;
name|this
operator|.
name|minimumSymmetricKeyLength
operator|=
literal|192
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SPConstants
operator|.
name|ALGO_SUITE_BASIC128_RSA15
operator|.
name|equals
argument_list|(
name|algoSuite
argument_list|)
condition|)
block|{
name|this
operator|.
name|digest
operator|=
name|SPConstants
operator|.
name|SHA1
expr_stmt|;
name|this
operator|.
name|encryption
operator|=
name|SPConstants
operator|.
name|AES128
expr_stmt|;
name|this
operator|.
name|symmetricKeyWrap
operator|=
name|SPConstants
operator|.
name|KW_AES128
expr_stmt|;
name|this
operator|.
name|asymmetricKeyWrap
operator|=
name|SPConstants
operator|.
name|KW_RSA15
expr_stmt|;
name|this
operator|.
name|encryptionKeyDerivation
operator|=
name|SPConstants
operator|.
name|P_SHA1_L128
expr_stmt|;
name|this
operator|.
name|signatureKeyDerivation
operator|=
name|SPConstants
operator|.
name|P_SHA1_L128
expr_stmt|;
name|this
operator|.
name|encryptionDerivedKeyLength
operator|=
literal|128
expr_stmt|;
name|this
operator|.
name|signatureDerivedKeyLength
operator|=
literal|128
expr_stmt|;
name|this
operator|.
name|minimumSymmetricKeyLength
operator|=
literal|128
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SPConstants
operator|.
name|ALGO_SUITE_TRIPLE_DES_RSA15
operator|.
name|equals
argument_list|(
name|algoSuite
argument_list|)
condition|)
block|{
name|this
operator|.
name|digest
operator|=
name|SPConstants
operator|.
name|SHA1
expr_stmt|;
name|this
operator|.
name|encryption
operator|=
name|SPConstants
operator|.
name|TRIPLE_DES
expr_stmt|;
name|this
operator|.
name|symmetricKeyWrap
operator|=
name|SPConstants
operator|.
name|KW_TRIPLE_DES
expr_stmt|;
name|this
operator|.
name|asymmetricKeyWrap
operator|=
name|SPConstants
operator|.
name|KW_RSA15
expr_stmt|;
name|this
operator|.
name|encryptionKeyDerivation
operator|=
name|SPConstants
operator|.
name|P_SHA1_L192
expr_stmt|;
name|this
operator|.
name|signatureKeyDerivation
operator|=
name|SPConstants
operator|.
name|P_SHA1_L192
expr_stmt|;
name|this
operator|.
name|encryptionDerivedKeyLength
operator|=
literal|192
expr_stmt|;
name|this
operator|.
name|signatureDerivedKeyLength
operator|=
literal|192
expr_stmt|;
name|this
operator|.
name|minimumSymmetricKeyLength
operator|=
literal|192
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SPConstants
operator|.
name|ALGO_SUITE_BASIC256_SHA256
operator|.
name|equals
argument_list|(
name|algoSuite
argument_list|)
condition|)
block|{
name|this
operator|.
name|digest
operator|=
name|SPConstants
operator|.
name|SHA256
expr_stmt|;
name|this
operator|.
name|encryption
operator|=
name|SPConstants
operator|.
name|AES256
expr_stmt|;
name|this
operator|.
name|symmetricKeyWrap
operator|=
name|SPConstants
operator|.
name|KW_AES256
expr_stmt|;
name|this
operator|.
name|asymmetricKeyWrap
operator|=
name|SPConstants
operator|.
name|KW_RSA_OAEP
expr_stmt|;
name|this
operator|.
name|encryptionKeyDerivation
operator|=
name|SPConstants
operator|.
name|P_SHA1_L256
expr_stmt|;
name|this
operator|.
name|signatureKeyDerivation
operator|=
name|SPConstants
operator|.
name|P_SHA1_L192
expr_stmt|;
name|this
operator|.
name|encryptionDerivedKeyLength
operator|=
literal|256
expr_stmt|;
name|this
operator|.
name|signatureDerivedKeyLength
operator|=
literal|256
expr_stmt|;
name|this
operator|.
name|minimumSymmetricKeyLength
operator|=
literal|256
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SPConstants
operator|.
name|ALGO_SUITE_BASIC192_SHA256
operator|.
name|equals
argument_list|(
name|algoSuite
argument_list|)
condition|)
block|{
name|this
operator|.
name|digest
operator|=
name|SPConstants
operator|.
name|SHA256
expr_stmt|;
name|this
operator|.
name|encryption
operator|=
name|SPConstants
operator|.
name|AES192
expr_stmt|;
name|this
operator|.
name|symmetricKeyWrap
operator|=
name|SPConstants
operator|.
name|KW_AES192
expr_stmt|;
name|this
operator|.
name|asymmetricKeyWrap
operator|=
name|SPConstants
operator|.
name|KW_RSA_OAEP
expr_stmt|;
name|this
operator|.
name|encryptionKeyDerivation
operator|=
name|SPConstants
operator|.
name|P_SHA1_L192
expr_stmt|;
name|this
operator|.
name|signatureKeyDerivation
operator|=
name|SPConstants
operator|.
name|P_SHA1_L192
expr_stmt|;
name|this
operator|.
name|encryptionDerivedKeyLength
operator|=
literal|192
expr_stmt|;
name|this
operator|.
name|signatureDerivedKeyLength
operator|=
literal|192
expr_stmt|;
name|this
operator|.
name|minimumSymmetricKeyLength
operator|=
literal|192
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SPConstants
operator|.
name|ALGO_SUITE_BASIC128_SHA256
operator|.
name|equals
argument_list|(
name|algoSuite
argument_list|)
condition|)
block|{
name|this
operator|.
name|digest
operator|=
name|SPConstants
operator|.
name|SHA256
expr_stmt|;
name|this
operator|.
name|encryption
operator|=
name|SPConstants
operator|.
name|AES128
expr_stmt|;
name|this
operator|.
name|symmetricKeyWrap
operator|=
name|SPConstants
operator|.
name|KW_AES128
expr_stmt|;
name|this
operator|.
name|asymmetricKeyWrap
operator|=
name|SPConstants
operator|.
name|KW_RSA_OAEP
expr_stmt|;
name|this
operator|.
name|encryptionKeyDerivation
operator|=
name|SPConstants
operator|.
name|P_SHA1_L128
expr_stmt|;
name|this
operator|.
name|signatureKeyDerivation
operator|=
name|SPConstants
operator|.
name|P_SHA1_L128
expr_stmt|;
name|this
operator|.
name|encryptionDerivedKeyLength
operator|=
literal|128
expr_stmt|;
name|this
operator|.
name|signatureDerivedKeyLength
operator|=
literal|128
expr_stmt|;
name|this
operator|.
name|minimumSymmetricKeyLength
operator|=
literal|128
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SPConstants
operator|.
name|ALGO_SUITE_TRIPLE_DES_SHA256
operator|.
name|equals
argument_list|(
name|algoSuite
argument_list|)
condition|)
block|{
name|this
operator|.
name|digest
operator|=
name|SPConstants
operator|.
name|SHA256
expr_stmt|;
name|this
operator|.
name|encryption
operator|=
name|SPConstants
operator|.
name|TRIPLE_DES
expr_stmt|;
name|this
operator|.
name|symmetricKeyWrap
operator|=
name|SPConstants
operator|.
name|KW_TRIPLE_DES
expr_stmt|;
name|this
operator|.
name|asymmetricKeyWrap
operator|=
name|SPConstants
operator|.
name|KW_RSA_OAEP
expr_stmt|;
name|this
operator|.
name|encryptionKeyDerivation
operator|=
name|SPConstants
operator|.
name|P_SHA1_L192
expr_stmt|;
name|this
operator|.
name|signatureKeyDerivation
operator|=
name|SPConstants
operator|.
name|P_SHA1_L192
expr_stmt|;
name|this
operator|.
name|encryptionDerivedKeyLength
operator|=
literal|192
expr_stmt|;
name|this
operator|.
name|signatureDerivedKeyLength
operator|=
literal|192
expr_stmt|;
name|this
operator|.
name|minimumSymmetricKeyLength
operator|=
literal|192
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SPConstants
operator|.
name|ALGO_SUITE_BASIC256_SHA256_RSA15
operator|.
name|equals
argument_list|(
name|algoSuite
argument_list|)
condition|)
block|{
name|this
operator|.
name|digest
operator|=
name|SPConstants
operator|.
name|SHA256
expr_stmt|;
name|this
operator|.
name|encryption
operator|=
name|SPConstants
operator|.
name|AES256
expr_stmt|;
name|this
operator|.
name|symmetricKeyWrap
operator|=
name|SPConstants
operator|.
name|KW_AES256
expr_stmt|;
name|this
operator|.
name|asymmetricKeyWrap
operator|=
name|SPConstants
operator|.
name|KW_RSA15
expr_stmt|;
name|this
operator|.
name|encryptionKeyDerivation
operator|=
name|SPConstants
operator|.
name|P_SHA1_L256
expr_stmt|;
name|this
operator|.
name|signatureKeyDerivation
operator|=
name|SPConstants
operator|.
name|P_SHA1_L192
expr_stmt|;
name|this
operator|.
name|encryptionDerivedKeyLength
operator|=
literal|256
expr_stmt|;
name|this
operator|.
name|signatureDerivedKeyLength
operator|=
literal|192
expr_stmt|;
name|this
operator|.
name|minimumSymmetricKeyLength
operator|=
literal|256
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SPConstants
operator|.
name|ALGO_SUITE_BASIC192_SHA256_RSA15
operator|.
name|equals
argument_list|(
name|algoSuite
argument_list|)
condition|)
block|{
name|this
operator|.
name|digest
operator|=
name|SPConstants
operator|.
name|SHA256
expr_stmt|;
name|this
operator|.
name|encryption
operator|=
name|SPConstants
operator|.
name|AES192
expr_stmt|;
name|this
operator|.
name|symmetricKeyWrap
operator|=
name|SPConstants
operator|.
name|KW_AES192
expr_stmt|;
name|this
operator|.
name|asymmetricKeyWrap
operator|=
name|SPConstants
operator|.
name|KW_RSA15
expr_stmt|;
name|this
operator|.
name|encryptionKeyDerivation
operator|=
name|SPConstants
operator|.
name|P_SHA1_L192
expr_stmt|;
name|this
operator|.
name|signatureKeyDerivation
operator|=
name|SPConstants
operator|.
name|P_SHA1_L192
expr_stmt|;
name|this
operator|.
name|encryptionDerivedKeyLength
operator|=
literal|192
expr_stmt|;
name|this
operator|.
name|signatureDerivedKeyLength
operator|=
literal|192
expr_stmt|;
name|this
operator|.
name|minimumSymmetricKeyLength
operator|=
literal|192
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SPConstants
operator|.
name|ALGO_SUITE_BASIC128_SHA256_RSA15
operator|.
name|equals
argument_list|(
name|algoSuite
argument_list|)
condition|)
block|{
name|this
operator|.
name|digest
operator|=
name|SPConstants
operator|.
name|SHA256
expr_stmt|;
name|this
operator|.
name|encryption
operator|=
name|SPConstants
operator|.
name|AES128
expr_stmt|;
name|this
operator|.
name|symmetricKeyWrap
operator|=
name|SPConstants
operator|.
name|KW_AES128
expr_stmt|;
name|this
operator|.
name|asymmetricKeyWrap
operator|=
name|SPConstants
operator|.
name|KW_RSA15
expr_stmt|;
name|this
operator|.
name|encryptionKeyDerivation
operator|=
name|SPConstants
operator|.
name|P_SHA1_L128
expr_stmt|;
name|this
operator|.
name|signatureKeyDerivation
operator|=
name|SPConstants
operator|.
name|P_SHA1_L128
expr_stmt|;
name|this
operator|.
name|encryptionDerivedKeyLength
operator|=
literal|128
expr_stmt|;
name|this
operator|.
name|signatureDerivedKeyLength
operator|=
literal|128
expr_stmt|;
name|this
operator|.
name|minimumSymmetricKeyLength
operator|=
literal|128
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SPConstants
operator|.
name|ALGO_SUITE_TRIPLE_DES_SHA256_RSA15
operator|.
name|equals
argument_list|(
name|algoSuite
argument_list|)
condition|)
block|{
name|this
operator|.
name|digest
operator|=
name|SPConstants
operator|.
name|SHA256
expr_stmt|;
name|this
operator|.
name|encryption
operator|=
name|SPConstants
operator|.
name|TRIPLE_DES
expr_stmt|;
name|this
operator|.
name|symmetricKeyWrap
operator|=
name|SPConstants
operator|.
name|KW_TRIPLE_DES
expr_stmt|;
name|this
operator|.
name|asymmetricKeyWrap
operator|=
name|SPConstants
operator|.
name|KW_RSA15
expr_stmt|;
name|this
operator|.
name|encryptionKeyDerivation
operator|=
name|SPConstants
operator|.
name|P_SHA1_L192
expr_stmt|;
name|this
operator|.
name|signatureKeyDerivation
operator|=
name|SPConstants
operator|.
name|P_SHA1_L192
expr_stmt|;
name|this
operator|.
name|encryptionDerivedKeyLength
operator|=
literal|192
expr_stmt|;
name|this
operator|.
name|signatureDerivedKeyLength
operator|=
literal|192
expr_stmt|;
name|this
operator|.
name|minimumSymmetricKeyLength
operator|=
literal|192
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|WSSPolicyException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"INVALID_ALGORITHM"
argument_list|,
name|LOG
argument_list|,
name|algoSuite
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

