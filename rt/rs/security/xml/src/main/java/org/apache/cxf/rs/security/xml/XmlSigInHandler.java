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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
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
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|NodeList
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
name|jaxrs
operator|.
name|ext
operator|.
name|RequestHandler
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
name|jaxrs
operator|.
name|model
operator|.
name|ClassResourceInfo
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
name|ws
operator|.
name|security
operator|.
name|components
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
name|XmlSigInHandler
extends|extends
name|AbstractXmlSecInHandler
implements|implements
name|RequestHandler
block|{
specifier|private
name|boolean
name|removeSignature
init|=
literal|true
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
name|Response
name|handleRequest
parameter_list|(
name|Message
name|message
parameter_list|,
name|ClassResourceInfo
name|resourceClass
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
return|return
literal|null
return|;
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
name|sigElement
init|=
name|getSignatureElement
argument_list|(
name|root
argument_list|)
decl_stmt|;
if|if
condition|(
name|sigElement
operator|==
literal|null
condition|)
block|{
name|throwFault
argument_list|(
literal|"Enveloped Signature is not available"
argument_list|,
literal|null
argument_list|)
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
name|SecurityConstants
operator|.
name|SIGNATURE_CRYPTO
argument_list|,
name|SecurityConstants
operator|.
name|SIGNATURE_PROPERTIES
argument_list|)
expr_stmt|;
if|if
condition|(
name|crypto
operator|==
literal|null
condition|)
block|{
name|crypto
operator|=
name|loader
operator|.
name|getCrypto
argument_list|(
name|message
argument_list|,
name|SecurityConstants
operator|.
name|ENCRYPT_CRYPTO
argument_list|,
name|SecurityConstants
operator|.
name|ENCRYPT_PROPERTIES
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
try|try
block|{
name|XMLSignature
name|signature
init|=
operator|new
name|XMLSignature
argument_list|(
name|sigElement
argument_list|,
literal|""
argument_list|)
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
name|X509Certificate
name|cert
init|=
name|keyInfo
operator|.
name|getX509Certificate
argument_list|()
decl_stmt|;
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
name|PublicKey
name|pk
init|=
name|keyInfo
operator|.
name|getPublicKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|pk
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
name|pk
argument_list|)
expr_stmt|;
block|}
block|}
comment|// is this call redundant given that signature.checkSignatureValue uses References ?
name|validateReference
argument_list|(
name|root
argument_list|,
name|signature
argument_list|)
expr_stmt|;
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
name|keyInfo
operator|.
name|getPublicKey
argument_list|()
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
name|root
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
name|sigElement
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
comment|//TODO: If we have a SAML assertion header as well with holder-of-key or
comment|// sender-vouches claims then we will need to store signature or parts of it
comment|// to validate that saml assertion and this payload have been signed by the
comment|// same key
return|return
literal|null
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
name|NodeList
name|list
init|=
name|objectNode
operator|.
name|getChildNodes
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
name|list
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|node
init|=
name|list
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|instanceof
name|Element
condition|)
block|{
name|objectNode
operator|.
name|removeChild
argument_list|(
name|node
argument_list|)
expr_stmt|;
return|return
operator|(
name|Element
operator|)
name|node
return|;
block|}
block|}
name|throwFault
argument_list|(
literal|"No signed data is found"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
specifier|private
name|Element
name|getSignatureElement
parameter_list|(
name|Element
name|root
parameter_list|)
block|{
if|if
condition|(
name|isEnveloping
argument_list|(
name|root
argument_list|)
condition|)
block|{
return|return
name|root
return|;
block|}
return|return
name|getNode
argument_list|(
name|root
argument_list|,
name|Constants
operator|.
name|SignatureSpecNS
argument_list|,
literal|"Signature"
argument_list|,
literal|0
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
name|void
name|validateReference
parameter_list|(
name|Element
name|root
parameter_list|,
name|XMLSignature
name|sig
parameter_list|)
block|{
name|Reference
name|ref
init|=
literal|null
decl_stmt|;
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
literal|"Multiple Signature Reference are not currently supported"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|ref
operator|=
name|sig
operator|.
name|getSignedInfo
argument_list|()
operator|.
name|item
argument_list|(
literal|0
argument_list|)
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
literal|"Signature Reference is not available"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|isEnveloping
argument_list|(
name|root
argument_list|)
condition|)
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
name|refId
init|=
name|ref
operator|.
name|getURI
argument_list|()
decl_stmt|;
if|if
condition|(
name|refId
operator|.
name|length
argument_list|()
operator|==
literal|0
operator|&&
name|rootId
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
comment|// or fragment must be expected ?
return|return;
block|}
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
operator|||
operator|!
name|refId
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
operator|.
name|equals
argument_list|(
name|rootId
argument_list|)
condition|)
block|{
name|throwFault
argument_list|(
literal|"Signature Reference ID is invalid"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
operator|!
name|isEnveloping
argument_list|(
name|root
argument_list|)
condition|)
block|{
name|boolean
name|isEnveloped
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
name|isEnveloped
operator|=
literal|true
expr_stmt|;
break|break;
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
operator|!
name|isEnveloped
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
block|}
block|}
block|}
end_class

end_unit

