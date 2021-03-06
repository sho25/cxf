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
name|saml
operator|.
name|sso
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Key
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
name|CertificateEncodingException
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
name|Base64
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
name|xml
operator|.
name|crypto
operator|.
name|dsig
operator|.
name|CanonicalizationMethod
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|dsig
operator|.
name|DigestMethod
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|dsig
operator|.
name|Reference
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|dsig
operator|.
name|SignatureMethod
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|dsig
operator|.
name|SignedInfo
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|dsig
operator|.
name|Transform
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|dsig
operator|.
name|XMLSignature
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|dsig
operator|.
name|XMLSignatureFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|dsig
operator|.
name|dom
operator|.
name|DOMSignContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|dsig
operator|.
name|keyinfo
operator|.
name|KeyInfo
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|dsig
operator|.
name|keyinfo
operator|.
name|KeyInfoFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|dsig
operator|.
name|keyinfo
operator|.
name|X509Data
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|dsig
operator|.
name|spec
operator|.
name|C14NMethodParameterSpec
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|dsig
operator|.
name|spec
operator|.
name|TransformParameterSpec
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
name|apache
operator|.
name|cxf
operator|.
name|staxutils
operator|.
name|W3CDOMStreamWriter
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
name|DOM2Writer
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
name|stax
operator|.
name|impl
operator|.
name|util
operator|.
name|IDGenerator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
specifier|public
class|class
name|MetadataWriter
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MetadataWriter
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|XMLSignatureFactory
name|XML_SIGNATURE_FACTORY
init|=
name|XMLSignatureFactory
operator|.
name|getInstance
argument_list|(
literal|"DOM"
argument_list|)
decl_stmt|;
comment|//CHECKSTYLE:OFF
specifier|public
name|Document
name|getMetaData
parameter_list|(
name|String
name|serviceURL
parameter_list|,
name|String
name|assertionConsumerServiceURL
parameter_list|,
name|String
name|logoutURL
parameter_list|,
name|Key
name|signingKey
parameter_list|,
name|X509Certificate
name|signingCert
parameter_list|,
name|boolean
name|wantRequestsSigned
parameter_list|)
throws|throws
name|Exception
block|{
name|W3CDOMStreamWriter
name|writer
init|=
operator|new
name|W3CDOMStreamWriter
argument_list|()
decl_stmt|;
name|writer
operator|.
name|writeStartDocument
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|,
literal|"1.0"
argument_list|)
expr_stmt|;
name|String
name|referenceID
init|=
name|IDGenerator
operator|.
name|generateID
argument_list|(
literal|"_"
argument_list|)
decl_stmt|;
name|writer
operator|.
name|writeStartElement
argument_list|(
literal|"md"
argument_list|,
literal|"EntityDescriptor"
argument_list|,
name|SSOConstants
operator|.
name|SAML2_METADATA_NS
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeAttribute
argument_list|(
literal|"ID"
argument_list|,
name|referenceID
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeAttribute
argument_list|(
literal|"entityID"
argument_list|,
name|serviceURL
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeNamespace
argument_list|(
literal|"md"
argument_list|,
name|SSOConstants
operator|.
name|SAML2_METADATA_NS
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeNamespace
argument_list|(
literal|"wsa"
argument_list|,
name|SSOConstants
operator|.
name|WS_ADDRESSING_NS
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeNamespace
argument_list|(
literal|"xsi"
argument_list|,
name|SSOConstants
operator|.
name|SCHEMA_INSTANCE_NS
argument_list|)
expr_stmt|;
name|writeSAMLMetadata
argument_list|(
name|writer
argument_list|,
name|assertionConsumerServiceURL
argument_list|,
name|logoutURL
argument_list|,
name|signingCert
argument_list|,
name|wantRequestsSigned
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
comment|// EntityDescriptor
name|writer
operator|.
name|writeEndDocument
argument_list|()
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|String
name|out
init|=
name|DOM2Writer
operator|.
name|nodeToString
argument_list|(
name|writer
operator|.
name|getDocument
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"***************** unsigned ****************"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"***************** unsigned ****************"
argument_list|)
expr_stmt|;
block|}
name|Document
name|doc
init|=
name|writer
operator|.
name|getDocument
argument_list|()
decl_stmt|;
if|if
condition|(
name|signingKey
operator|!=
literal|null
condition|)
block|{
return|return
name|signMetaInfo
argument_list|(
name|signingCert
argument_list|,
name|signingKey
argument_list|,
name|doc
argument_list|,
name|referenceID
argument_list|)
return|;
block|}
return|return
name|doc
return|;
block|}
specifier|private
name|void
name|writeSAMLMetadata
parameter_list|(
name|XMLStreamWriter
name|writer
parameter_list|,
name|String
name|assertionConsumerServiceURL
parameter_list|,
name|String
name|logoutURL
parameter_list|,
name|X509Certificate
name|signingCert
parameter_list|,
name|boolean
name|wantRequestsSigned
parameter_list|)
throws|throws
name|XMLStreamException
throws|,
name|MalformedURLException
throws|,
name|CertificateEncodingException
block|{
name|writer
operator|.
name|writeStartElement
argument_list|(
literal|"md"
argument_list|,
literal|"SPSSODescriptor"
argument_list|,
name|SSOConstants
operator|.
name|SAML2_METADATA_NS
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeAttribute
argument_list|(
literal|"AuthnRequestsSigned"
argument_list|,
name|Boolean
operator|.
name|toString
argument_list|(
name|wantRequestsSigned
argument_list|)
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeAttribute
argument_list|(
literal|"WantAssertionsSigned"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeAttribute
argument_list|(
literal|"protocolSupportEnumeration"
argument_list|,
literal|"urn:oasis:names:tc:SAML:2.0:protocol"
argument_list|)
expr_stmt|;
if|if
condition|(
name|logoutURL
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|writeStartElement
argument_list|(
literal|"md"
argument_list|,
literal|"SingleLogoutService"
argument_list|,
name|SSOConstants
operator|.
name|SAML2_METADATA_NS
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeAttribute
argument_list|(
literal|"Location"
argument_list|,
name|logoutURL
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeAttribute
argument_list|(
literal|"Binding"
argument_list|,
literal|"urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
comment|// SingleLogoutService
block|}
name|writer
operator|.
name|writeStartElement
argument_list|(
literal|"md"
argument_list|,
literal|"AssertionConsumerService"
argument_list|,
name|SSOConstants
operator|.
name|SAML2_METADATA_NS
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeAttribute
argument_list|(
literal|"Location"
argument_list|,
name|assertionConsumerServiceURL
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeAttribute
argument_list|(
literal|"index"
argument_list|,
literal|"0"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeAttribute
argument_list|(
literal|"isDefault"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeAttribute
argument_list|(
literal|"Binding"
argument_list|,
literal|"urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
comment|// AssertionConsumerService
name|writer
operator|.
name|writeStartElement
argument_list|(
literal|"md"
argument_list|,
literal|"AssertionConsumerService"
argument_list|,
name|SSOConstants
operator|.
name|SAML2_METADATA_NS
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeAttribute
argument_list|(
literal|"Location"
argument_list|,
name|assertionConsumerServiceURL
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeAttribute
argument_list|(
literal|"index"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeAttribute
argument_list|(
literal|"Binding"
argument_list|,
literal|"urn:oasis:names:tc:SAML:2.0:bindings:HTTP-REDIRECT"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
comment|// AssertionConsumerService
comment|/*         if (protocol.getClaimTypesRequested() != null&& !protocol.getClaimTypesRequested().isEmpty()) {             writer.writeStartElement("md", "AttributeConsumingService", SSOConstants.SAML2_METADATA_NS);             writer.writeAttribute("index", "0");              writer.writeStartElement("md", "ServiceName", SSOConstants.SAML2_METADATA_NS);             writer.writeAttribute("xml:lang", "en");             writer.writeCharacters(config.getName());             writer.writeEndElement(); // ServiceName              for (Claim claim : protocol.getClaimTypesRequested()) {                 writer.writeStartElement("md", "RequestedAttribute", SSOConstants.SAML2_METADATA_NS);                 writer.writeAttribute("isRequired", Boolean.toString(claim.isOptional()));                 writer.writeAttribute("Name", claim.getType());                 writer.writeAttribute("NameFormat",                                       "urn:oasis:names:tc:SAML:2.0:attrname-format:unspecified");                 writer.writeEndElement(); // RequestedAttribute             }              writer.writeEndElement(); // AttributeConsumingService         }         */
if|if
condition|(
name|signingCert
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|writeStartElement
argument_list|(
literal|"md"
argument_list|,
literal|"KeyDescriptor"
argument_list|,
name|SSOConstants
operator|.
name|SAML2_METADATA_NS
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeAttribute
argument_list|(
literal|"use"
argument_list|,
literal|"signing"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeStartElement
argument_list|(
literal|"ds"
argument_list|,
literal|"KeyInfo"
argument_list|,
literal|"http://www.w3.org/2000/09/xmldsig#"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeNamespace
argument_list|(
literal|"ds"
argument_list|,
literal|"http://www.w3.org/2000/09/xmldsig#"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeStartElement
argument_list|(
literal|"ds"
argument_list|,
literal|"X509Data"
argument_list|,
literal|"http://www.w3.org/2000/09/xmldsig#"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeStartElement
argument_list|(
literal|"ds"
argument_list|,
literal|"X509Certificate"
argument_list|,
literal|"http://www.w3.org/2000/09/xmldsig#"
argument_list|)
expr_stmt|;
comment|// Write the Base-64 encoded certificate
name|byte
index|[]
name|data
init|=
name|signingCert
operator|.
name|getEncoded
argument_list|()
decl_stmt|;
name|String
name|encodedCertificate
init|=
name|Base64
operator|.
name|getMimeEncoder
argument_list|()
operator|.
name|encodeToString
argument_list|(
name|data
argument_list|)
decl_stmt|;
name|writer
operator|.
name|writeCharacters
argument_list|(
name|encodedCertificate
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
comment|// X509Certificate
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
comment|// X509Data
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
comment|// KeyInfo
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
comment|// KeyDescriptor
block|}
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
comment|// SPSSODescriptor
block|}
specifier|private
specifier|static
name|Document
name|signMetaInfo
parameter_list|(
name|X509Certificate
name|signingCert
parameter_list|,
name|Key
name|signingKey
parameter_list|,
name|Document
name|doc
parameter_list|,
name|String
name|referenceID
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|signatureMethod
init|=
literal|null
decl_stmt|;
if|if
condition|(
literal|"SHA1withDSA"
operator|.
name|equals
argument_list|(
name|signingCert
operator|.
name|getSigAlgName
argument_list|()
argument_list|)
condition|)
block|{
name|signatureMethod
operator|=
name|SignatureMethod
operator|.
name|DSA_SHA1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"SHA1withRSA"
operator|.
name|equals
argument_list|(
name|signingCert
operator|.
name|getSigAlgName
argument_list|()
argument_list|)
condition|)
block|{
name|signatureMethod
operator|=
name|SignatureMethod
operator|.
name|RSA_SHA1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"SHA256withRSA"
operator|.
name|equals
argument_list|(
name|signingCert
operator|.
name|getSigAlgName
argument_list|()
argument_list|)
condition|)
block|{
name|signatureMethod
operator|=
name|SignatureMethod
operator|.
name|RSA_SHA1
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Unsupported signature method: "
operator|+
name|signingCert
operator|.
name|getSigAlgName
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unsupported signature method: "
operator|+
name|signingCert
operator|.
name|getSigAlgName
argument_list|()
argument_list|)
throw|;
block|}
name|List
argument_list|<
name|Transform
argument_list|>
name|transformList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|transformList
operator|.
name|add
argument_list|(
name|XML_SIGNATURE_FACTORY
operator|.
name|newTransform
argument_list|(
name|Transform
operator|.
name|ENVELOPED
argument_list|,
operator|(
name|TransformParameterSpec
operator|)
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|transformList
operator|.
name|add
argument_list|(
name|XML_SIGNATURE_FACTORY
operator|.
name|newCanonicalizationMethod
argument_list|(
name|CanonicalizationMethod
operator|.
name|EXCLUSIVE
argument_list|,
operator|(
name|C14NMethodParameterSpec
operator|)
literal|null
argument_list|)
argument_list|)
expr_stmt|;
comment|// Create a Reference to the enveloped document (in this case,
comment|// you are signing the whole document, so a URI of "" signifies
comment|// that, and also specify the SHA1 digest algorithm and
comment|// the ENVELOPED Transform.
name|Reference
name|ref
init|=
name|XML_SIGNATURE_FACTORY
operator|.
name|newReference
argument_list|(
literal|"#"
operator|+
name|referenceID
argument_list|,
name|XML_SIGNATURE_FACTORY
operator|.
name|newDigestMethod
argument_list|(
name|DigestMethod
operator|.
name|SHA1
argument_list|,
literal|null
argument_list|)
argument_list|,
name|transformList
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
comment|// Create the SignedInfo.
name|SignedInfo
name|si
init|=
name|XML_SIGNATURE_FACTORY
operator|.
name|newSignedInfo
argument_list|(
name|XML_SIGNATURE_FACTORY
operator|.
name|newCanonicalizationMethod
argument_list|(
name|CanonicalizationMethod
operator|.
name|EXCLUSIVE
argument_list|,
operator|(
name|C14NMethodParameterSpec
operator|)
literal|null
argument_list|)
argument_list|,
name|XML_SIGNATURE_FACTORY
operator|.
name|newSignatureMethod
argument_list|(
name|signatureMethod
argument_list|,
literal|null
argument_list|)
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|ref
argument_list|)
argument_list|)
decl_stmt|;
comment|// Create the KeyInfo containing the X509Data.
name|KeyInfoFactory
name|kif
init|=
name|XML_SIGNATURE_FACTORY
operator|.
name|getKeyInfoFactory
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|x509Content
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|x509Content
operator|.
name|add
argument_list|(
name|signingCert
operator|.
name|getSubjectX500Principal
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|x509Content
operator|.
name|add
argument_list|(
name|signingCert
argument_list|)
expr_stmt|;
name|X509Data
name|xd
init|=
name|kif
operator|.
name|newX509Data
argument_list|(
name|x509Content
argument_list|)
decl_stmt|;
name|KeyInfo
name|ki
init|=
name|kif
operator|.
name|newKeyInfo
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|xd
argument_list|)
argument_list|)
decl_stmt|;
comment|// Create a DOMSignContext and specify the RSA PrivateKey and
comment|// location of the resulting XMLSignature's parent element.
comment|//DOMSignContext dsc = new DOMSignContext(keyEntry.getPrivateKey(), doc.getDocumentElement());
name|DOMSignContext
name|dsc
init|=
operator|new
name|DOMSignContext
argument_list|(
name|signingKey
argument_list|,
name|doc
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
decl_stmt|;
name|dsc
operator|.
name|setIdAttributeNS
argument_list|(
name|doc
operator|.
name|getDocumentElement
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|"ID"
argument_list|)
expr_stmt|;
name|dsc
operator|.
name|setNextSibling
argument_list|(
name|doc
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|getFirstChild
argument_list|()
argument_list|)
expr_stmt|;
comment|// Create the XMLSignature, but don't sign it yet.
name|XMLSignature
name|signature
init|=
name|XML_SIGNATURE_FACTORY
operator|.
name|newXMLSignature
argument_list|(
name|si
argument_list|,
name|ki
argument_list|)
decl_stmt|;
comment|// Marshal, generate, and sign the enveloped signature.
name|signature
operator|.
name|sign
argument_list|(
name|dsc
argument_list|)
expr_stmt|;
comment|// Output the resulting document.
return|return
name|doc
return|;
block|}
block|}
end_class

end_unit

