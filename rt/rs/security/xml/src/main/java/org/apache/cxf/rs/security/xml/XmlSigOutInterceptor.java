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
name|security
operator|.
name|PrivateKey
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
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
name|common
operator|.
name|util
operator|.
name|StringUtils
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
name|WSPasswordCallback
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

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|xmlsec
operator|.
name|signature
operator|.
name|support
operator|.
name|SignatureConstants
import|;
end_import

begin_comment
comment|//TODO: Make sure that enveloped signatures can be applied to individual
end_comment

begin_comment
comment|//      child nodes of an envelope root element, a new property such as
end_comment

begin_comment
comment|//      targetElementQName will be needed
end_comment

begin_class
specifier|public
class|class
name|XmlSigOutInterceptor
extends|extends
name|AbstractXmlSecOutInterceptor
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ENVELOPED_SIG
init|=
literal|"enveloped"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ENVELOPING_SIG
init|=
literal|"enveloping"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DETACHED_SIG
init|=
literal|"detached"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_ENV_PREFIX
init|=
literal|"env"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|DEFAULT_ENV_QNAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://org.apache.cxf/rs/env"
argument_list|,
literal|"Envelope"
argument_list|,
name|DEFAULT_ENV_PREFIX
argument_list|)
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
name|XmlSigOutInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|SUPPORTED_STYLES
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|ENVELOPED_SIG
argument_list|,
name|ENVELOPING_SIG
argument_list|,
name|DETACHED_SIG
argument_list|)
argument_list|)
decl_stmt|;
specifier|private
name|QName
name|envelopeQName
init|=
name|DEFAULT_ENV_QNAME
decl_stmt|;
specifier|private
name|String
name|sigStyle
init|=
name|ENVELOPED_SIG
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
init|=
operator|new
name|SignatureProperties
argument_list|()
decl_stmt|;
specifier|public
name|XmlSigOutInterceptor
parameter_list|()
block|{     }
specifier|public
name|void
name|setSignatureProperties
parameter_list|(
name|SignatureProperties
name|props
parameter_list|)
block|{
name|this
operator|.
name|sigProps
operator|=
name|props
expr_stmt|;
block|}
specifier|public
name|void
name|setStyle
parameter_list|(
name|String
name|style
parameter_list|)
block|{
if|if
condition|(
operator|!
name|SUPPORTED_STYLES
operator|.
name|contains
argument_list|(
name|style
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported XML Signature style"
argument_list|)
throw|;
block|}
name|sigStyle
operator|=
name|style
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
specifier|public
name|void
name|setSignatureAlgorithm
parameter_list|(
name|String
name|algo
parameter_list|)
block|{
name|sigProps
operator|.
name|setSignatureAlgo
argument_list|(
name|algo
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setDigestAlgorithm
parameter_list|(
name|String
name|algo
parameter_list|)
block|{
name|sigProps
operator|.
name|setSignatureDigestAlgo
argument_list|(
name|algo
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Document
name|processDocument
parameter_list|(
name|Message
name|message
parameter_list|,
name|Document
name|doc
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|createSignature
argument_list|(
name|message
argument_list|,
name|doc
argument_list|)
return|;
block|}
comment|// enveloping& detached sigs will be supported too
specifier|private
name|Document
name|createSignature
parameter_list|(
name|Message
name|message
parameter_list|,
name|Document
name|doc
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|userNameKey
init|=
name|SecurityConstants
operator|.
name|SIGNATURE_USERNAME
decl_stmt|;
name|CryptoLoader
name|loader
init|=
operator|new
name|CryptoLoader
argument_list|()
decl_stmt|;
name|Crypto
name|crypto
init|=
name|loader
operator|.
name|getCrypto
argument_list|(
name|message
argument_list|,
name|SecurityConstants
operator|.
name|SIGNATURE_CRYPTO
argument_list|,
name|SecurityConstants
operator|.
name|SIGNATURE_PROPERTIES
argument_list|)
decl_stmt|;
name|String
name|user
init|=
name|SecurityUtils
operator|.
name|getUserName
argument_list|(
name|message
argument_list|,
name|crypto
argument_list|,
name|userNameKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|user
argument_list|)
operator|||
name|SecurityUtils
operator|.
name|USE_REQUEST_SIGNATURE_CERT
operator|.
name|equals
argument_list|(
name|user
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"User name is not available"
argument_list|)
throw|;
block|}
name|String
name|password
init|=
name|SecurityUtils
operator|.
name|getPassword
argument_list|(
name|message
argument_list|,
name|user
argument_list|,
name|WSPasswordCallback
operator|.
name|SIGNATURE
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|X509Certificate
index|[]
name|issuerCerts
init|=
name|SecurityUtils
operator|.
name|getCertificates
argument_list|(
name|crypto
argument_list|,
name|user
argument_list|)
decl_stmt|;
name|String
name|sigAlgo
init|=
name|sigProps
operator|.
name|getSignatureAlgo
argument_list|()
operator|==
literal|null
condition|?
name|SignatureConstants
operator|.
name|ALGO_ID_SIGNATURE_RSA_SHA1
else|:
name|sigProps
operator|.
name|getSignatureAlgo
argument_list|()
decl_stmt|;
name|String
name|pubKeyAlgo
init|=
name|issuerCerts
index|[
literal|0
index|]
operator|.
name|getPublicKey
argument_list|()
operator|.
name|getAlgorithm
argument_list|()
decl_stmt|;
if|if
condition|(
name|pubKeyAlgo
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DSA"
argument_list|)
condition|)
block|{
name|sigAlgo
operator|=
name|XMLSignature
operator|.
name|ALGO_ID_SIGNATURE_DSA
expr_stmt|;
block|}
name|PrivateKey
name|privateKey
init|=
literal|null
decl_stmt|;
try|try
block|{
name|privateKey
operator|=
name|crypto
operator|.
name|getPrivateKey
argument_list|(
name|user
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|String
name|errorMessage
init|=
literal|"Private key can not be loaded, user:"
operator|+
name|user
decl_stmt|;
name|LOG
operator|.
name|severe
argument_list|(
name|errorMessage
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
name|ex
argument_list|)
throw|;
block|}
name|String
name|id
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|String
name|referenceId
init|=
literal|"#"
operator|+
name|id
decl_stmt|;
name|String
name|digestAlgo
init|=
name|sigProps
operator|.
name|getSignatureDigestAlgo
argument_list|()
operator|==
literal|null
condition|?
name|Constants
operator|.
name|ALGO_ID_DIGEST_SHA1
else|:
name|sigProps
operator|.
name|getSignatureDigestAlgo
argument_list|()
decl_stmt|;
name|XMLSignature
name|sig
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|ENVELOPING_SIG
operator|.
name|equals
argument_list|(
name|sigStyle
argument_list|)
condition|)
block|{
name|sig
operator|=
name|prepareEnvelopingSignature
argument_list|(
name|doc
argument_list|,
name|id
argument_list|,
name|referenceId
argument_list|,
name|sigAlgo
argument_list|,
name|digestAlgo
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|DETACHED_SIG
operator|.
name|equals
argument_list|(
name|sigStyle
argument_list|)
condition|)
block|{
name|sig
operator|=
name|prepareDetachedSignature
argument_list|(
name|doc
argument_list|,
name|id
argument_list|,
name|referenceId
argument_list|,
name|sigAlgo
argument_list|,
name|digestAlgo
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sig
operator|=
name|prepareEnvelopedSignature
argument_list|(
name|doc
argument_list|,
name|id
argument_list|,
name|referenceId
argument_list|,
name|sigAlgo
argument_list|,
name|digestAlgo
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|keyInfoMustBeAvailable
condition|)
block|{
name|sig
operator|.
name|addKeyInfo
argument_list|(
name|issuerCerts
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|sig
operator|.
name|addKeyInfo
argument_list|(
name|issuerCerts
index|[
literal|0
index|]
operator|.
name|getPublicKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|sig
operator|.
name|sign
argument_list|(
name|privateKey
argument_list|)
expr_stmt|;
return|return
name|sig
operator|.
name|getElement
argument_list|()
operator|.
name|getOwnerDocument
argument_list|()
return|;
block|}
specifier|private
name|XMLSignature
name|prepareEnvelopingSignature
parameter_list|(
name|Document
name|doc
parameter_list|,
name|String
name|id
parameter_list|,
name|String
name|referenceId
parameter_list|,
name|String
name|sigAlgo
parameter_list|,
name|String
name|digestAlgo
parameter_list|)
throws|throws
name|Exception
block|{
name|Element
name|docEl
init|=
name|doc
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
name|Document
name|newDoc
init|=
name|DOMUtils
operator|.
name|createDocument
argument_list|()
decl_stmt|;
name|doc
operator|.
name|removeChild
argument_list|(
name|docEl
argument_list|)
expr_stmt|;
name|newDoc
operator|.
name|adoptNode
argument_list|(
name|docEl
argument_list|)
expr_stmt|;
name|Element
name|object
init|=
name|newDoc
operator|.
name|createElementNS
argument_list|(
name|Constants
operator|.
name|SignatureSpecNS
argument_list|,
literal|"ds:Object"
argument_list|)
decl_stmt|;
name|object
operator|.
name|appendChild
argument_list|(
name|docEl
argument_list|)
expr_stmt|;
name|docEl
operator|.
name|setAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"Id"
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|docEl
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
name|XMLSignature
name|sig
init|=
operator|new
name|XMLSignature
argument_list|(
name|newDoc
argument_list|,
literal|""
argument_list|,
name|sigAlgo
argument_list|)
decl_stmt|;
name|newDoc
operator|.
name|appendChild
argument_list|(
name|sig
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
name|sig
operator|.
name|getElement
argument_list|()
operator|.
name|appendChild
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|Transforms
name|transforms
init|=
operator|new
name|Transforms
argument_list|(
name|newDoc
argument_list|)
decl_stmt|;
name|transforms
operator|.
name|addTransform
argument_list|(
name|Transforms
operator|.
name|TRANSFORM_C14N_EXCL_OMIT_COMMENTS
argument_list|)
expr_stmt|;
name|sig
operator|.
name|addDocument
argument_list|(
name|referenceId
argument_list|,
name|transforms
argument_list|,
name|digestAlgo
argument_list|)
expr_stmt|;
return|return
name|sig
return|;
block|}
specifier|private
name|XMLSignature
name|prepareDetachedSignature
parameter_list|(
name|Document
name|doc
parameter_list|,
name|String
name|id
parameter_list|,
name|String
name|referenceId
parameter_list|,
name|String
name|sigAlgo
parameter_list|,
name|String
name|digestAlgo
parameter_list|)
throws|throws
name|Exception
block|{
name|Element
name|docEl
init|=
name|doc
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
name|Document
name|newDoc
init|=
name|DOMUtils
operator|.
name|createDocument
argument_list|()
decl_stmt|;
name|doc
operator|.
name|removeChild
argument_list|(
name|docEl
argument_list|)
expr_stmt|;
name|newDoc
operator|.
name|adoptNode
argument_list|(
name|docEl
argument_list|)
expr_stmt|;
name|docEl
operator|.
name|setAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"Id"
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|docEl
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
name|Element
name|root
init|=
name|newDoc
operator|.
name|createElementNS
argument_list|(
name|envelopeQName
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|envelopeQName
operator|.
name|getPrefix
argument_list|()
operator|+
literal|":"
operator|+
name|envelopeQName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
decl_stmt|;
name|root
operator|.
name|appendChild
argument_list|(
name|docEl
argument_list|)
expr_stmt|;
name|newDoc
operator|.
name|appendChild
argument_list|(
name|root
argument_list|)
expr_stmt|;
name|XMLSignature
name|sig
init|=
operator|new
name|XMLSignature
argument_list|(
name|newDoc
argument_list|,
literal|""
argument_list|,
name|sigAlgo
argument_list|)
decl_stmt|;
name|root
operator|.
name|appendChild
argument_list|(
name|sig
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
name|Transforms
name|transforms
init|=
operator|new
name|Transforms
argument_list|(
name|newDoc
argument_list|)
decl_stmt|;
name|transforms
operator|.
name|addTransform
argument_list|(
name|Transforms
operator|.
name|TRANSFORM_C14N_EXCL_OMIT_COMMENTS
argument_list|)
expr_stmt|;
name|sig
operator|.
name|addDocument
argument_list|(
name|referenceId
argument_list|,
name|transforms
argument_list|,
name|digestAlgo
argument_list|)
expr_stmt|;
return|return
name|sig
return|;
block|}
specifier|private
name|XMLSignature
name|prepareEnvelopedSignature
parameter_list|(
name|Document
name|doc
parameter_list|,
name|String
name|id
parameter_list|,
name|String
name|referenceURI
parameter_list|,
name|String
name|sigAlgo
parameter_list|,
name|String
name|digestAlgo
parameter_list|)
throws|throws
name|Exception
block|{
name|doc
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|setAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"Id"
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|doc
operator|.
name|getDocumentElement
argument_list|()
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
name|XMLSignature
name|sig
init|=
operator|new
name|XMLSignature
argument_list|(
name|doc
argument_list|,
literal|""
argument_list|,
name|sigAlgo
argument_list|)
decl_stmt|;
name|doc
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|appendChild
argument_list|(
name|sig
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
name|Transforms
name|transforms
init|=
operator|new
name|Transforms
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|transforms
operator|.
name|addTransform
argument_list|(
name|Transforms
operator|.
name|TRANSFORM_ENVELOPED_SIGNATURE
argument_list|)
expr_stmt|;
name|transforms
operator|.
name|addTransform
argument_list|(
name|Transforms
operator|.
name|TRANSFORM_C14N_EXCL_OMIT_COMMENTS
argument_list|)
expr_stmt|;
name|sig
operator|.
name|addDocument
argument_list|(
name|referenceURI
argument_list|,
name|transforms
argument_list|,
name|digestAlgo
argument_list|)
expr_stmt|;
return|return
name|sig
return|;
block|}
specifier|public
name|void
name|setEnvelopeName
parameter_list|(
name|String
name|expandedName
parameter_list|)
block|{
name|setEnvelopeQName
argument_list|(
name|DOMUtils
operator|.
name|convertStringToQName
argument_list|(
name|expandedName
argument_list|,
name|DEFAULT_ENV_PREFIX
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setEnvelopeQName
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|.
name|getPrefix
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|name
operator|=
operator|new
name|QName
argument_list|(
name|name
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|name
operator|.
name|getLocalPart
argument_list|()
argument_list|,
name|DEFAULT_ENV_PREFIX
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|envelopeQName
operator|=
name|name
expr_stmt|;
block|}
block|}
end_class

end_unit

