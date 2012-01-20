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
argument_list|)
decl_stmt|;
comment|// TODO re-enable this line once we pick up xmlsec 1.5.0
comment|// XMLSignature signature = new XMLSignature(signatureElement, "", true);
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
if|if
condition|(
name|valid
operator|&&
name|persistSignature
condition|)
block|{
name|message
operator|.
name|setContent
argument_list|(
name|XMLSignature
operator|.
name|class
argument_list|,
name|signature
argument_list|)
expr_stmt|;
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
literal|"Multiple Signature Reference are not currently supported"
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
if|if
condition|(
name|enveloped
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
else|else
block|{
return|return
name|root
return|;
block|}
block|}
comment|/**      * Returns the single element that contains an Id with value      *<code>uri</code> and<code>namespace</code>. The Id can be either a wsu:Id or an Id      * with no namespace. This is a replacement for a XPath Id lookup with the given namespace.       * It's somewhat faster than XPath, and we do not deal with prefixes, just with the real      * namespace URI      *       * If checkMultipleElements is true and there are multiple elements, we log a       * warning and return null as this can be used to get around the signature checking.      *       * @param startNode Where to start the search      * @param value Value of the Id attribute      * @param checkMultipleElements If true then go through the entire tree and return       *        null if there are multiple elements with the same Id      * @return The found element if there was exactly one match, or      *<code>null</code> otherwise      */
specifier|private
specifier|static
name|Element
name|findElementById
parameter_list|(
name|Node
name|startNode
parameter_list|,
name|String
name|value
parameter_list|,
name|boolean
name|checkMultipleElements
parameter_list|)
block|{
comment|//
comment|// Replace the formerly recursive implementation with a depth-first-loop lookup
comment|//
name|Node
name|startParent
init|=
name|startNode
operator|.
name|getParentNode
argument_list|()
decl_stmt|;
name|Node
name|processedNode
init|=
literal|null
decl_stmt|;
name|Element
name|foundElement
init|=
literal|null
decl_stmt|;
name|String
name|id
init|=
name|value
decl_stmt|;
while|while
condition|(
name|startNode
operator|!=
literal|null
condition|)
block|{
comment|// start node processing at this point
if|if
condition|(
name|startNode
operator|.
name|getNodeType
argument_list|()
operator|==
name|Node
operator|.
name|ELEMENT_NODE
condition|)
block|{
name|Element
name|se
init|=
operator|(
name|Element
operator|)
name|startNode
decl_stmt|;
comment|// Try the wsu:Id first
name|String
name|attributeNS
init|=
name|se
operator|.
name|getAttributeNS
argument_list|(
name|WSConstants
operator|.
name|WSU_NS
argument_list|,
literal|"Id"
argument_list|)
decl_stmt|;
if|if
condition|(
literal|""
operator|.
name|equals
argument_list|(
name|attributeNS
argument_list|)
operator|||
operator|!
name|id
operator|.
name|equals
argument_list|(
name|attributeNS
argument_list|)
condition|)
block|{
name|attributeNS
operator|=
name|se
operator|.
name|getAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"Id"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|""
operator|.
name|equals
argument_list|(
name|attributeNS
argument_list|)
operator|||
operator|!
name|id
operator|.
name|equals
argument_list|(
name|attributeNS
argument_list|)
condition|)
block|{
name|attributeNS
operator|=
name|se
operator|.
name|getAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"ID"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|attributeNS
argument_list|)
operator|&&
name|id
operator|.
name|equals
argument_list|(
name|attributeNS
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|checkMultipleElements
condition|)
block|{
return|return
name|se
return|;
block|}
elseif|else
if|if
condition|(
name|foundElement
operator|==
literal|null
condition|)
block|{
name|foundElement
operator|=
name|se
expr_stmt|;
comment|// Continue searching to find duplicates
block|}
else|else
block|{
comment|// Multiple elements with the same 'Id' attribute value
return|return
literal|null
return|;
block|}
block|}
block|}
name|processedNode
operator|=
name|startNode
expr_stmt|;
name|startNode
operator|=
name|startNode
operator|.
name|getFirstChild
argument_list|()
expr_stmt|;
comment|// no child, this node is done.
if|if
condition|(
name|startNode
operator|==
literal|null
condition|)
block|{
comment|// close node processing, get sibling
name|startNode
operator|=
name|processedNode
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
comment|// no more siblings, get parent, all children
comment|// of parent are processed.
while|while
condition|(
name|startNode
operator|==
literal|null
condition|)
block|{
name|processedNode
operator|=
name|processedNode
operator|.
name|getParentNode
argument_list|()
expr_stmt|;
if|if
condition|(
name|processedNode
operator|==
name|startParent
condition|)
block|{
return|return
name|foundElement
return|;
block|}
comment|// close parent node processing (processed node now)
name|startNode
operator|=
name|processedNode
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|foundElement
return|;
block|}
block|}
end_class

end_unit

