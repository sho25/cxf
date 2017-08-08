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
name|xml
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|regex
operator|.
name|Pattern
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|PatternSyntaxException
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
name|XMLStreamReader
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
name|rs
operator|.
name|security
operator|.
name|common
operator|.
name|CryptoLoader
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
name|rs
operator|.
name|security
operator|.
name|common
operator|.
name|RSSecurityUtils
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
name|rs
operator|.
name|security
operator|.
name|common
operator|.
name|TrustValidator
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
name|rt
operator|.
name|security
operator|.
name|utils
operator|.
name|SecurityUtils
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
name|SecurityContext
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
name|W3CDOMStreamReader
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
name|KeyInfo
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
name|signature
operator|.
name|Reference
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
name|signature
operator|.
name|SignedInfo
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
name|signature
operator|.
name|XMLSignature
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
name|transforms
operator|.
name|Transform
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
name|transforms
operator|.
name|Transforms
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
name|Constants
import|;
end_import

begin_class
specifier|public
class|class
name|AbstractXmlSigInHandler
extends|extends
name|AbstractXmlSecInHandler
block|{
specifier|private
name|boolean
name|removeSignature
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|persistSignature
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|keyInfoMustBeAvailable
init|=
literal|true
decl_stmt|;
specifier|private
name|SignatureProperties
name|sigProps
decl_stmt|;
comment|/**      * a collection of compiled regular expression patterns for the subject DN      */
specifier|private
specifier|final
name|Collection
argument_list|<
name|Pattern
argument_list|>
name|subjectDNPatterns
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|void
name|setRemoveSignature
parameter_list|(
name|boolean
name|remove
parameter_list|)
block|{
name|this
operator|.
name|removeSignature
operator|=
name|remove
expr_stmt|;
block|}
specifier|public
name|void
name|setPersistSignature
parameter_list|(
name|boolean
name|persist
parameter_list|)
block|{
name|this
operator|.
name|persistSignature
operator|=
name|persist
expr_stmt|;
block|}
specifier|protected
name|void
name|checkSignature
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Document
name|doc
init|=
name|getDocument
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|doc
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Element
name|root
init|=
name|doc
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
name|Element
name|signatureElement
init|=
name|getSignatureElement
argument_list|(
name|root
argument_list|)
decl_stmt|;
if|if
condition|(
name|signatureElement
operator|==
literal|null
condition|)
block|{
name|throwFault
argument_list|(
literal|"XML Signature is not available"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
name|String
name|cryptoKey
init|=
literal|null
decl_stmt|;
name|String
name|propKey
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|RSSecurityUtils
operator|.
name|isSignedAndEncryptedTwoWay
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|cryptoKey
operator|=
name|SecurityConstants
operator|.
name|ENCRYPT_CRYPTO
expr_stmt|;
name|propKey
operator|=
name|SecurityConstants
operator|.
name|ENCRYPT_PROPERTIES
expr_stmt|;
block|}
else|else
block|{
name|cryptoKey
operator|=
name|SecurityConstants
operator|.
name|SIGNATURE_CRYPTO
expr_stmt|;
name|propKey
operator|=
name|SecurityConstants
operator|.
name|SIGNATURE_PROPERTIES
expr_stmt|;
block|}
name|Crypto
name|crypto
init|=
literal|null
decl_stmt|;
try|try
block|{
name|CryptoLoader
name|loader
init|=
operator|new
name|CryptoLoader
argument_list|()
decl_stmt|;
name|crypto
operator|=
name|loader
operator|.
name|getCrypto
argument_list|(
name|message
argument_list|,
name|cryptoKey
argument_list|,
name|propKey
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|throwFault
argument_list|(
literal|"Crypto can not be loaded"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
name|boolean
name|valid
init|=
literal|false
decl_stmt|;
name|Reference
name|ref
init|=
literal|null
decl_stmt|;
try|try
block|{
name|XMLSignature
name|signature
init|=
operator|new
name|XMLSignature
argument_list|(
name|signatureElement
argument_list|,
literal|""
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|sigProps
operator|!=
literal|null
condition|)
block|{
name|SignedInfo
name|sInfo
init|=
name|signature
operator|.
name|getSignedInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|sigProps
operator|.
name|getSignatureAlgo
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|sigProps
operator|.
name|getSignatureAlgo
argument_list|()
operator|.
name|equals
argument_list|(
name|sInfo
operator|.
name|getSignatureMethodURI
argument_list|()
argument_list|)
condition|)
block|{
name|throwFault
argument_list|(
literal|"Signature Algorithm is not supported"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sigProps
operator|.
name|getSignatureC14nMethod
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|sigProps
operator|.
name|getSignatureC14nMethod
argument_list|()
operator|.
name|equals
argument_list|(
name|sInfo
operator|.
name|getCanonicalizationMethodURI
argument_list|()
argument_list|)
condition|)
block|{
name|throwFault
argument_list|(
literal|"Signature C14n Algorithm is not supported"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
name|ref
operator|=
name|getReference
argument_list|(
name|signature
argument_list|)
expr_stmt|;
name|Element
name|signedElement
init|=
name|validateReference
argument_list|(
name|root
argument_list|,
name|ref
argument_list|)
decl_stmt|;
if|if
condition|(
name|signedElement
operator|.
name|hasAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"ID"
argument_list|)
condition|)
block|{
name|signedElement
operator|.
name|setIdAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"ID"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|signedElement
operator|.
name|hasAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"Id"
argument_list|)
condition|)
block|{
name|signedElement
operator|.
name|setIdAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"Id"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|X509Certificate
name|cert
init|=
literal|null
decl_stmt|;
name|PublicKey
name|publicKey
init|=
literal|null
decl_stmt|;
comment|// See also WSS4J SAMLUtil.getCredentialFromKeyInfo
name|KeyInfo
name|keyInfo
init|=
name|signature
operator|.
name|getKeyInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|keyInfo
operator|!=
literal|null
condition|)
block|{
name|cert
operator|=
name|keyInfo
operator|.
name|getX509Certificate
argument_list|()
expr_stmt|;
if|if
condition|(
name|cert
operator|!=
literal|null
condition|)
block|{
name|valid
operator|=
name|signature
operator|.
name|checkSignatureValue
argument_list|(
name|cert
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|publicKey
operator|=
name|keyInfo
operator|.
name|getPublicKey
argument_list|()
expr_stmt|;
if|if
condition|(
name|publicKey
operator|!=
literal|null
condition|)
block|{
name|valid
operator|=
name|signature
operator|.
name|checkSignatureValue
argument_list|(
name|publicKey
argument_list|)
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
operator|!
name|keyInfoMustBeAvailable
condition|)
block|{
name|String
name|user
init|=
name|getUserName
argument_list|(
name|crypto
argument_list|,
name|message
argument_list|)
decl_stmt|;
name|cert
operator|=
name|RSSecurityUtils
operator|.
name|getCertificates
argument_list|(
name|crypto
argument_list|,
name|user
argument_list|)
index|[
literal|0
index|]
expr_stmt|;
name|publicKey
operator|=
name|cert
operator|.
name|getPublicKey
argument_list|()
expr_stmt|;
name|valid
operator|=
name|signature
operator|.
name|checkSignatureValue
argument_list|(
name|cert
argument_list|)
expr_stmt|;
block|}
comment|// validate trust
operator|new
name|TrustValidator
argument_list|()
operator|.
name|validateTrust
argument_list|(
name|crypto
argument_list|,
name|cert
argument_list|,
name|publicKey
argument_list|,
name|getSubjectContraints
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|valid
operator|&&
name|persistSignature
condition|)
block|{
if|if
condition|(
name|signature
operator|.
name|getKeyInfo
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|SIGNING_CERT
argument_list|,
name|signature
operator|.
name|getKeyInfo
argument_list|()
operator|.
name|getX509Certificate
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|signature
operator|.
name|getKeyInfo
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|SIGNING_PUBLIC_KEY
argument_list|,
name|signature
operator|.
name|getKeyInfo
argument_list|()
operator|.
name|getPublicKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|setContent
argument_list|(
name|Element
operator|.
name|class
argument_list|,
name|signedElement
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|throwFault
argument_list|(
literal|"Signature validation failed"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|valid
condition|)
block|{
name|throwFault
argument_list|(
literal|"Signature validation failed"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|removeSignature
condition|)
block|{
if|if
condition|(
operator|!
name|isEnveloping
argument_list|(
name|root
argument_list|)
condition|)
block|{
name|Element
name|signedEl
init|=
name|getSignedElement
argument_list|(
name|root
argument_list|,
name|ref
argument_list|)
decl_stmt|;
name|signedEl
operator|.
name|removeAttribute
argument_list|(
literal|"ID"
argument_list|)
expr_stmt|;
name|root
operator|.
name|removeChild
argument_list|(
name|signatureElement
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Element
name|actualBody
init|=
name|getActualBody
argument_list|(
name|root
argument_list|)
decl_stmt|;
name|Document
name|newDoc
init|=
name|DOMUtils
operator|.
name|createDocument
argument_list|()
decl_stmt|;
name|newDoc
operator|.
name|adoptNode
argument_list|(
name|actualBody
argument_list|)
expr_stmt|;
name|root
operator|=
name|actualBody
expr_stmt|;
block|}
block|}
name|message
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
operator|new
name|W3CDOMStreamReader
argument_list|(
name|root
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|String
name|getUserName
parameter_list|(
name|Crypto
name|crypto
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
name|SecurityContext
name|sc
init|=
name|message
operator|.
name|get
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|sc
operator|!=
literal|null
operator|&&
name|sc
operator|.
name|getUserPrincipal
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|sc
operator|.
name|getUserPrincipal
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
return|return
name|RSSecurityUtils
operator|.
name|getUserName
argument_list|(
name|crypto
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|private
name|Element
name|getActualBody
parameter_list|(
name|Element
name|envelopingSigElement
parameter_list|)
block|{
name|Element
name|objectNode
init|=
name|getNode
argument_list|(
name|envelopingSigElement
argument_list|,
name|Constants
operator|.
name|SignatureSpecNS
argument_list|,
literal|"Object"
argument_list|,
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|objectNode
operator|==
literal|null
condition|)
block|{
name|throwFault
argument_list|(
literal|"Object envelope is not available"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
name|Element
name|node
init|=
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|objectNode
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|==
literal|null
condition|)
block|{
name|throwFault
argument_list|(
literal|"No signed data is found"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
return|return
name|node
return|;
block|}
specifier|private
name|Element
name|getSignatureElement
parameter_list|(
name|Element
name|sigParentElement
parameter_list|)
block|{
if|if
condition|(
name|isEnveloping
argument_list|(
name|sigParentElement
argument_list|)
condition|)
block|{
return|return
name|sigParentElement
return|;
block|}
return|return
name|DOMUtils
operator|.
name|getFirstChildWithName
argument_list|(
name|sigParentElement
argument_list|,
name|Constants
operator|.
name|SignatureSpecNS
argument_list|,
literal|"Signature"
argument_list|)
return|;
block|}
specifier|protected
name|boolean
name|isEnveloping
parameter_list|(
name|Element
name|root
parameter_list|)
block|{
return|return
name|Constants
operator|.
name|SignatureSpecNS
operator|.
name|equals
argument_list|(
name|root
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|&&
literal|"Signature"
operator|.
name|equals
argument_list|(
name|root
operator|.
name|getLocalName
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|Reference
name|getReference
parameter_list|(
name|XMLSignature
name|sig
parameter_list|)
block|{
name|int
name|count
init|=
name|sig
operator|.
name|getSignedInfo
argument_list|()
operator|.
name|getLength
argument_list|()
decl_stmt|;
if|if
condition|(
name|count
operator|!=
literal|1
condition|)
block|{
name|throwFault
argument_list|(
literal|"Multiple Signature References are not currently supported"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
try|try
block|{
return|return
name|sig
operator|.
name|getSignedInfo
argument_list|()
operator|.
name|item
argument_list|(
literal|0
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|XMLSecurityException
name|ex
parameter_list|)
block|{
name|throwFault
argument_list|(
literal|"Signature Reference is not available"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|Element
name|validateReference
parameter_list|(
name|Element
name|root
parameter_list|,
name|Reference
name|ref
parameter_list|)
block|{
name|boolean
name|enveloped
init|=
literal|false
decl_stmt|;
name|String
name|refId
init|=
name|ref
operator|.
name|getURI
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|refId
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
operator|||
name|refId
operator|.
name|length
argument_list|()
operator|<=
literal|1
condition|)
block|{
name|throwFault
argument_list|(
literal|"Only local Signature References are supported"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
name|Element
name|signedEl
init|=
name|getSignedElement
argument_list|(
name|root
argument_list|,
name|ref
argument_list|)
decl_stmt|;
if|if
condition|(
name|signedEl
operator|!=
literal|null
condition|)
block|{
name|enveloped
operator|=
name|signedEl
operator|==
name|root
expr_stmt|;
block|}
else|else
block|{
name|throwFault
argument_list|(
literal|"Signature Reference ID is invalid"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
name|Transforms
name|transforms
init|=
literal|null
decl_stmt|;
try|try
block|{
name|transforms
operator|=
name|ref
operator|.
name|getTransforms
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLSecurityException
name|ex
parameter_list|)
block|{
name|throwFault
argument_list|(
literal|"Signature transforms can not be obtained"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
name|boolean
name|c14TransformConfirmed
init|=
literal|false
decl_stmt|;
name|String
name|c14TransformExpected
init|=
name|sigProps
operator|!=
literal|null
condition|?
name|sigProps
operator|.
name|getSignatureC14nTransform
argument_list|()
else|:
literal|null
decl_stmt|;
name|boolean
name|envelopedConfirmed
init|=
literal|false
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
name|transforms
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
try|try
block|{
name|Transform
name|tr
init|=
name|transforms
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|Transforms
operator|.
name|TRANSFORM_ENVELOPED_SIGNATURE
operator|.
name|equals
argument_list|(
name|tr
operator|.
name|getURI
argument_list|()
argument_list|)
condition|)
block|{
name|envelopedConfirmed
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|c14TransformExpected
operator|!=
literal|null
operator|&&
name|c14TransformExpected
operator|.
name|equals
argument_list|(
name|tr
operator|.
name|getURI
argument_list|()
argument_list|)
condition|)
block|{
name|c14TransformConfirmed
operator|=
literal|true
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|throwFault
argument_list|(
literal|"Problem accessing Transform instance"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|enveloped
operator|&&
operator|!
name|envelopedConfirmed
condition|)
block|{
name|throwFault
argument_list|(
literal|"Only enveloped signatures are currently supported"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|c14TransformExpected
operator|!=
literal|null
operator|&&
operator|!
name|c14TransformConfirmed
condition|)
block|{
name|throwFault
argument_list|(
literal|"Transform Canonicalization is not supported"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sigProps
operator|!=
literal|null
operator|&&
name|sigProps
operator|.
name|getSignatureDigestAlgo
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Element
name|dm
init|=
name|DOMUtils
operator|.
name|getFirstChildWithName
argument_list|(
name|ref
operator|.
name|getElement
argument_list|()
argument_list|,
name|Constants
operator|.
name|SignatureSpecNS
argument_list|,
literal|"DigestMethod"
argument_list|)
decl_stmt|;
if|if
condition|(
name|dm
operator|!=
literal|null
operator|&&
operator|!
name|dm
operator|.
name|getAttribute
argument_list|(
literal|"Algorithm"
argument_list|)
operator|.
name|equals
argument_list|(
name|sigProps
operator|.
name|getSignatureDigestAlgo
argument_list|()
argument_list|)
condition|)
block|{
name|throwFault
argument_list|(
literal|"Signature Digest Algorithm is not supported"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|signedEl
return|;
block|}
specifier|private
name|Element
name|getSignedElement
parameter_list|(
name|Element
name|root
parameter_list|,
name|Reference
name|ref
parameter_list|)
block|{
name|String
name|rootId
init|=
name|root
operator|.
name|getAttribute
argument_list|(
literal|"ID"
argument_list|)
decl_stmt|;
name|String
name|expectedID
init|=
name|ref
operator|.
name|getURI
argument_list|()
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|expectedID
operator|.
name|equals
argument_list|(
name|rootId
argument_list|)
condition|)
block|{
return|return
name|XMLUtils
operator|.
name|findElementById
argument_list|(
name|root
argument_list|,
name|expectedID
argument_list|,
literal|true
argument_list|)
return|;
block|}
return|return
name|root
return|;
block|}
specifier|public
name|void
name|setSignatureProperties
parameter_list|(
name|SignatureProperties
name|properties
parameter_list|)
block|{
name|this
operator|.
name|sigProps
operator|=
name|properties
expr_stmt|;
block|}
specifier|public
name|void
name|setKeyInfoMustBeAvailable
parameter_list|(
name|boolean
name|use
parameter_list|)
block|{
name|this
operator|.
name|keyInfoMustBeAvailable
operator|=
name|use
expr_stmt|;
block|}
comment|/**      * Set a list of Strings corresponding to regular expression constraints on the subject DN      * of a certificate      */
specifier|public
name|void
name|setSubjectConstraints
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|constraints
parameter_list|)
block|{
if|if
condition|(
name|constraints
operator|!=
literal|null
condition|)
block|{
name|subjectDNPatterns
operator|.
name|clear
argument_list|()
expr_stmt|;
for|for
control|(
name|String
name|constraint
range|:
name|constraints
control|)
block|{
try|try
block|{
name|subjectDNPatterns
operator|.
name|add
argument_list|(
name|Pattern
operator|.
name|compile
argument_list|(
name|constraint
operator|.
name|trim
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PatternSyntaxException
name|ex
parameter_list|)
block|{
throw|throw
name|ex
throw|;
block|}
block|}
block|}
block|}
specifier|private
name|Collection
argument_list|<
name|Pattern
argument_list|>
name|getSubjectContraints
parameter_list|(
name|Message
name|msg
parameter_list|)
throws|throws
name|PatternSyntaxException
block|{
name|String
name|certConstraints
init|=
operator|(
name|String
operator|)
name|SecurityUtils
operator|.
name|getSecurityPropertyValue
argument_list|(
name|SecurityConstants
operator|.
name|SUBJECT_CERT_CONSTRAINTS
argument_list|,
name|msg
argument_list|)
decl_stmt|;
comment|// Check the message property first. If this is not null then use it. Otherwise pick up
comment|// the constraints set as a property
if|if
condition|(
name|certConstraints
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|certConstraintsList
init|=
name|certConstraints
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
if|if
condition|(
name|certConstraintsList
operator|!=
literal|null
condition|)
block|{
name|subjectDNPatterns
operator|.
name|clear
argument_list|()
expr_stmt|;
for|for
control|(
name|String
name|certConstraint
range|:
name|certConstraintsList
control|)
block|{
name|subjectDNPatterns
operator|.
name|add
argument_list|(
name|Pattern
operator|.
name|compile
argument_list|(
name|certConstraint
operator|.
name|trim
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|subjectDNPatterns
return|;
block|}
block|}
end_class

end_unit

