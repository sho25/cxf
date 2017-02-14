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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPath
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathExpressionException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathFactory
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
name|helpers
operator|.
name|MapNamespaceContext
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
name|WSDataRef
import|;
end_import

begin_comment
comment|/**  * Utility to enable the checking of WS-Security signature / WS-Security  * encryption coverage based on the results of the WSS4J signature/encryption  * processor.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|CryptoCoverageUtil
block|{
comment|/**      * Hidden in utility class.      */
specifier|private
name|CryptoCoverageUtil
parameter_list|()
block|{     }
comment|/**      * Inspects the signed and encrypted content in the message and accurately      * resolves encrypted and then signed elements in {@code signedRefs}.      * Entries in {@code signedRefs} that correspond to an encrypted element      * are resolved to the decrypted element and added to {@code signedRefs}.      * The original reference to the encrypted content remains unaltered in the      * list to allow for matching against a requirement that xenc:EncryptedData      * and xenc:EncryptedKey elements be signed.      *      * @param signedRefs references to the signed content in the message      * @param encryptedRefs references to the encrypted content in the message      */
specifier|public
specifier|static
name|void
name|reconcileEncryptedSignedRefs
parameter_list|(
specifier|final
name|Collection
argument_list|<
name|WSDataRef
argument_list|>
name|signedRefs
parameter_list|,
specifier|final
name|Collection
argument_list|<
name|WSDataRef
argument_list|>
name|encryptedRefs
parameter_list|)
block|{
specifier|final
name|List
argument_list|<
name|WSDataRef
argument_list|>
name|encryptedSignedRefs
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|WSDataRef
name|signedRef
range|:
name|signedRefs
control|)
block|{
name|Element
name|protectedElement
init|=
name|signedRef
operator|.
name|getProtectedElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|protectedElement
operator|!=
literal|null
operator|&&
operator|(
literal|"EncryptedData"
operator|.
name|equals
argument_list|(
name|protectedElement
operator|.
name|getLocalName
argument_list|()
argument_list|)
operator|&&
name|WSConstants
operator|.
name|ENC_NS
operator|.
name|equals
argument_list|(
name|protectedElement
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|||
name|WSConstants
operator|.
name|ENCRYPTED_HEADER
operator|.
name|equals
argument_list|(
name|protectedElement
operator|.
name|getLocalName
argument_list|()
argument_list|)
operator|&&
name|WSConstants
operator|.
name|WSSE11_NS
operator|.
name|equals
argument_list|(
name|protectedElement
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|||
name|WSConstants
operator|.
name|ENCRYPED_ASSERTION_LN
operator|.
name|equals
argument_list|(
name|protectedElement
operator|.
name|getLocalName
argument_list|()
argument_list|)
operator|&&
name|WSConstants
operator|.
name|SAML2_NS
operator|.
name|equals
argument_list|(
name|protectedElement
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|)
condition|)
block|{
for|for
control|(
name|WSDataRef
name|encryptedRef
range|:
name|encryptedRefs
control|)
block|{
if|if
condition|(
name|protectedElement
operator|==
name|encryptedRef
operator|.
name|getEncryptedElement
argument_list|()
condition|)
block|{
specifier|final
name|WSDataRef
name|encryptedSignedRef
init|=
operator|new
name|WSDataRef
argument_list|()
decl_stmt|;
name|encryptedSignedRef
operator|.
name|setWsuId
argument_list|(
name|signedRef
operator|.
name|getWsuId
argument_list|()
argument_list|)
expr_stmt|;
name|encryptedSignedRef
operator|.
name|setContent
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|encryptedSignedRef
operator|.
name|setName
argument_list|(
name|encryptedRef
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|encryptedSignedRef
operator|.
name|setProtectedElement
argument_list|(
name|encryptedRef
operator|.
name|getProtectedElement
argument_list|()
argument_list|)
expr_stmt|;
name|encryptedSignedRef
operator|.
name|setXpath
argument_list|(
name|encryptedRef
operator|.
name|getXpath
argument_list|()
argument_list|)
expr_stmt|;
name|encryptedSignedRefs
operator|.
name|add
argument_list|(
name|encryptedSignedRef
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
name|signedRefs
operator|.
name|addAll
argument_list|(
name|encryptedSignedRefs
argument_list|)
expr_stmt|;
block|}
comment|/**      * Checks that the references provided refer to the      * signed/encrypted SOAP body element.      *      * @param soapBody      *            the SOAP body element      * @param refs      *            the refs to the data extracted from the signature/encryption      * @param type      *            the type of cryptographic coverage to check for      * @param scope      *            the scope of the cryptographic coverage to check for, defaults      *            to element      *      * @throws WSSecurityException      *             if there is an error evaluating the coverage or the body is not      *             covered by the signature/encryption.      */
specifier|public
specifier|static
name|void
name|checkBodyCoverage
parameter_list|(
name|Element
name|soapBody
parameter_list|,
specifier|final
name|Collection
argument_list|<
name|WSDataRef
argument_list|>
name|refs
parameter_list|,
name|CoverageType
name|type
parameter_list|,
name|CoverageScope
name|scope
parameter_list|)
throws|throws
name|WSSecurityException
block|{
if|if
condition|(
operator|!
name|CryptoCoverageUtil
operator|.
name|matchElement
argument_list|(
name|refs
argument_list|,
name|type
argument_list|,
name|scope
argument_list|,
name|soapBody
argument_list|)
condition|)
block|{
name|Exception
name|ex
init|=
operator|new
name|Exception
argument_list|(
literal|"The "
operator|+
name|getCoverageTypeString
argument_list|(
name|type
argument_list|)
operator|+
literal|" does not cover the required elements (soap:Body)."
argument_list|)
decl_stmt|;
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
block|}
specifier|public
specifier|static
name|void
name|checkAttachmentsCoverage
parameter_list|(
name|Collection
argument_list|<
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Attachment
argument_list|>
name|attachments
parameter_list|,
specifier|final
name|Collection
argument_list|<
name|WSDataRef
argument_list|>
name|refs
parameter_list|,
name|CoverageType
name|type
parameter_list|,
name|CoverageScope
name|scope
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|String
name|requiredTransform
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|type
operator|==
name|CoverageType
operator|.
name|SIGNED
operator|&&
name|scope
operator|==
name|CoverageScope
operator|.
name|CONTENT
condition|)
block|{
name|requiredTransform
operator|=
name|WSConstants
operator|.
name|SWA_ATTACHMENT_CONTENT_SIG_TRANS
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|CoverageType
operator|.
name|SIGNED
condition|)
block|{
name|requiredTransform
operator|=
name|WSConstants
operator|.
name|SWA_ATTACHMENT_COMPLETE_SIG_TRANS
expr_stmt|;
block|}
if|if
condition|(
name|attachments
operator|!=
literal|null
condition|)
block|{
comment|// For each matching attachment, check for a ref that covers it.
for|for
control|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Attachment
name|attachment
range|:
name|attachments
control|)
block|{
name|boolean
name|matched
init|=
literal|false
decl_stmt|;
for|for
control|(
name|WSDataRef
name|r
range|:
name|refs
control|)
block|{
name|String
name|id
init|=
name|r
operator|.
name|getWsuId
argument_list|()
decl_stmt|;
if|if
condition|(
name|id
operator|!=
literal|null
operator|&&
name|id
operator|.
name|startsWith
argument_list|(
literal|"cid:"
argument_list|)
condition|)
block|{
name|id
operator|=
name|id
operator|.
name|substring
argument_list|(
literal|4
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|r
operator|.
name|isAttachment
argument_list|()
operator|&&
name|attachment
operator|.
name|getId
argument_list|()
operator|!=
literal|null
operator|&&
name|attachment
operator|.
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|id
argument_list|)
operator|&&
operator|(
name|CoverageType
operator|.
name|ENCRYPTED
operator|==
name|type
operator|||
name|r
operator|.
name|getTransformAlgorithms
argument_list|()
operator|!=
literal|null
operator|&&
name|r
operator|.
name|getTransformAlgorithms
argument_list|()
operator|.
name|contains
argument_list|(
name|requiredTransform
argument_list|)
operator|)
condition|)
block|{
name|matched
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
comment|// We looked through all of the refs, but the element was not signed/encrypted
if|if
condition|(
operator|!
name|matched
condition|)
block|{
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
operator|new
name|Exception
argument_list|(
literal|"The "
operator|+
name|getCoverageTypeString
argument_list|(
name|type
argument_list|)
operator|+
literal|" does not cover the required elements"
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
block|}
comment|/**      * Checks that the references provided refer to the required      * signed/encrypted SOAP header element(s) matching the provided name and      * namespace.  If {@code name} is null, all headers from {@code namespace}      * are inspected for coverage.      *      * @param soapHeader      *            the SOAP header element      * @param refs      *            the refs to the data extracted from the signature/encryption      * @param namespaces      *            the namespace of the header(s) to check for coverage      * @param name      *            the local part of the header name to check for coverage, may be null      * @param type      *            the type of cryptographic coverage to check for      * @param scope      *            the scope of the cryptographic coverage to check for, defaults      *            to element      *      * @throws WSSecurityException      *             if there is an error evaluating the coverage or a header is not      *             covered by the signature/encryption.      */
specifier|public
specifier|static
name|void
name|checkHeaderCoverage
parameter_list|(
name|Element
name|soapHeader
parameter_list|,
specifier|final
name|Collection
argument_list|<
name|WSDataRef
argument_list|>
name|refs
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|name
parameter_list|,
name|CoverageType
name|type
parameter_list|,
name|CoverageScope
name|scope
parameter_list|)
throws|throws
name|WSSecurityException
block|{
specifier|final
name|List
argument_list|<
name|Element
argument_list|>
name|elements
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|elements
operator|=
name|DOMUtils
operator|.
name|getChildrenWithNamespace
argument_list|(
name|soapHeader
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|elements
operator|=
name|DOMUtils
operator|.
name|getChildrenWithName
argument_list|(
name|soapHeader
argument_list|,
name|namespace
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Element
name|el
range|:
name|elements
control|)
block|{
if|if
condition|(
operator|!
name|CryptoCoverageUtil
operator|.
name|matchElement
argument_list|(
name|refs
argument_list|,
name|type
argument_list|,
name|scope
argument_list|,
name|el
argument_list|)
condition|)
block|{
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
operator|new
name|Exception
argument_list|(
literal|"The "
operator|+
name|getCoverageTypeString
argument_list|(
name|type
argument_list|)
operator|+
literal|" does not cover the required elements ({"
operator|+
name|namespace
operator|+
literal|"}"
operator|+
name|name
operator|+
literal|")."
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**      * Checks that the references provided refer to the required      * signed/encrypted elements as defined by the XPath expression in {@code      * xPath}.      *      * @param soapEnvelope      *            the SOAP Envelope element      * @param refs      *            the refs to the data extracted from the signature/encryption      * @param namespaces      *            the prefix to namespace mapping, may be {@code null}      * @param xPath      *            the XPath expression      * @param type      *            the type of cryptographic coverage to check for      * @param scope      *            the scope of the cryptographic coverage to check for, defaults      *            to element      *      * @throws WSSecurityException      *             if there is an error evaluating an XPath or an element is not      *             covered by the signature/encryption.      */
specifier|public
specifier|static
name|void
name|checkCoverage
parameter_list|(
name|Element
name|soapEnvelope
parameter_list|,
specifier|final
name|Collection
argument_list|<
name|WSDataRef
argument_list|>
name|refs
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
parameter_list|,
name|String
name|xPath
parameter_list|,
name|CoverageType
name|type
parameter_list|,
name|CoverageScope
name|scope
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|CryptoCoverageUtil
operator|.
name|checkCoverage
argument_list|(
name|soapEnvelope
argument_list|,
name|refs
argument_list|,
name|namespaces
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|xPath
argument_list|)
argument_list|,
name|type
argument_list|,
name|scope
argument_list|)
expr_stmt|;
block|}
comment|/**      * Checks that the references provided refer to the required      * signed/encrypted elements as defined by the XPath expressions in {@code      * xPaths}.      *      * @param soapEnvelope      *            the SOAP Envelope element      * @param refs      *            the refs to the data extracted from the signature/encryption      * @param namespaces      *            the prefix to namespace mapping, may be {@code null}      * @param xPaths      *            the collection of XPath expressions      * @param type      *            the type of cryptographic coverage to check for      * @param scope      *            the scope of the cryptographic coverage to check for, defaults      *            to element      *      * @throws WSSecurityException      *             if there is an error evaluating an XPath or an element is not      *             covered by the signature/encryption.      */
specifier|public
specifier|static
name|void
name|checkCoverage
parameter_list|(
name|Element
name|soapEnvelope
parameter_list|,
specifier|final
name|Collection
argument_list|<
name|WSDataRef
argument_list|>
name|refs
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|xPaths
parameter_list|,
name|CoverageType
name|type
parameter_list|,
name|CoverageScope
name|scope
parameter_list|)
throws|throws
name|WSSecurityException
block|{
comment|// XPathFactory and XPath are not thread-safe so we must recreate them
comment|// each request.
specifier|final
name|XPathFactory
name|factory
init|=
name|XPathFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
specifier|final
name|XPath
name|xpath
init|=
name|factory
operator|.
name|newXPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|namespaces
operator|!=
literal|null
condition|)
block|{
name|xpath
operator|.
name|setNamespaceContext
argument_list|(
operator|new
name|MapNamespaceContext
argument_list|(
name|namespaces
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|checkCoverage
argument_list|(
name|soapEnvelope
argument_list|,
name|refs
argument_list|,
name|xpath
argument_list|,
name|xPaths
argument_list|,
name|type
argument_list|,
name|scope
argument_list|)
expr_stmt|;
block|}
comment|/**      * Checks that the references provided refer to the required      * signed/encrypted elements as defined by the XPath expressions in {@code      * xPaths}.      */
specifier|public
specifier|static
name|void
name|checkCoverage
parameter_list|(
name|Element
name|soapEnvelope
parameter_list|,
specifier|final
name|Collection
argument_list|<
name|WSDataRef
argument_list|>
name|refs
parameter_list|,
specifier|final
name|XPath
name|xpath
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|xPaths
parameter_list|,
name|CoverageType
name|type
parameter_list|,
name|CoverageScope
name|scope
parameter_list|)
throws|throws
name|WSSecurityException
block|{
comment|// For each XPath
for|for
control|(
name|String
name|xpathString
range|:
name|xPaths
control|)
block|{
comment|// Get the matching nodes
name|NodeList
name|list
decl_stmt|;
try|try
block|{
name|list
operator|=
operator|(
name|NodeList
operator|)
name|xpath
operator|.
name|evaluate
argument_list|(
name|xpathString
argument_list|,
name|soapEnvelope
argument_list|,
name|XPathConstants
operator|.
name|NODESET
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XPathExpressionException
name|e
parameter_list|)
block|{
comment|// The xpath's are not valid in the config.
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|ErrorCode
operator|.
name|FAILURE
argument_list|)
throw|;
block|}
comment|// If we found nodes then we need to do the check.
if|if
condition|(
name|list
operator|.
name|getLength
argument_list|()
operator|!=
literal|0
condition|)
block|{
comment|// For each matching element, check for a ref that
comment|// covers it.
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|list
operator|.
name|getLength
argument_list|()
condition|;
name|x
operator|++
control|)
block|{
specifier|final
name|Element
name|el
init|=
operator|(
name|Element
operator|)
name|list
operator|.
name|item
argument_list|(
name|x
argument_list|)
decl_stmt|;
name|boolean
name|instanceMatched
init|=
name|CryptoCoverageUtil
operator|.
name|matchElement
argument_list|(
name|refs
argument_list|,
name|type
argument_list|,
name|scope
argument_list|,
name|el
argument_list|)
decl_stmt|;
comment|// We looked through all of the refs, but the element was
comment|// not signed.
if|if
condition|(
operator|!
name|instanceMatched
condition|)
block|{
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
operator|new
name|Exception
argument_list|(
literal|"The "
operator|+
name|getCoverageTypeString
argument_list|(
name|type
argument_list|)
operator|+
literal|" does not cover the required elements ("
operator|+
name|xpathString
operator|+
literal|")."
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
block|}
block|}
specifier|private
specifier|static
name|boolean
name|matchElement
parameter_list|(
name|Collection
argument_list|<
name|WSDataRef
argument_list|>
name|refs
parameter_list|,
name|CoverageType
name|type
parameter_list|,
name|CoverageScope
name|scope
parameter_list|,
name|Element
name|el
parameter_list|)
block|{
specifier|final
name|boolean
name|content
decl_stmt|;
switch|switch
condition|(
name|scope
condition|)
block|{
case|case
name|CONTENT
case|:
name|content
operator|=
literal|true
expr_stmt|;
break|break;
case|case
name|ELEMENT
case|:
default|default:
name|content
operator|=
literal|false
expr_stmt|;
block|}
for|for
control|(
name|WSDataRef
name|r
range|:
name|refs
control|)
block|{
comment|// If the element is the same object instance
comment|// as that in the ref, we found it and can
comment|// stop looking at this element.
if|if
condition|(
name|r
operator|.
name|getProtectedElement
argument_list|()
operator|==
name|el
operator|&&
name|r
operator|.
name|isContent
argument_list|()
operator|==
name|content
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
name|String
name|getCoverageTypeString
parameter_list|(
name|CoverageType
name|type
parameter_list|)
block|{
name|String
name|typeString
decl_stmt|;
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|SIGNED
case|:
name|typeString
operator|=
literal|"signature"
expr_stmt|;
break|break;
case|case
name|ENCRYPTED
case|:
name|typeString
operator|=
literal|"encryption"
expr_stmt|;
break|break;
default|default:
name|typeString
operator|=
literal|"crpytography"
expr_stmt|;
block|}
return|return
name|typeString
return|;
block|}
comment|/**      * Differentiates which type of cryptographic coverage to check for.      */
specifier|public
enum|enum
name|CoverageType
block|{
comment|/**          * Checks for encryption of the matching elements.          */
name|ENCRYPTED
block|,
comment|/**          * Checks for a signature over the matching elements.          */
name|SIGNED
block|}
comment|/**      * Differentiates which part of an element to check for cryptographic coverage.      */
specifier|public
enum|enum
name|CoverageScope
block|{
comment|/**          * Checks for encryption of the matching elements.          */
name|CONTENT
block|,
comment|/**          * Checks for a signature over the matching elements.          */
name|ELEMENT
block|}
block|}
end_class

end_unit

