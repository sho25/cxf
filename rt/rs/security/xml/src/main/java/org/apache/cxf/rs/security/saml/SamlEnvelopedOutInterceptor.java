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
package|;
end_package

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
name|XMLUtils
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
name|io
operator|.
name|CachedOutputStream
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
name|xml
operator|.
name|AbstractXmlSecOutInterceptor
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
name|xml
operator|.
name|XmlEncOutInterceptor
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
name|xml
operator|.
name|XmlSigOutInterceptor
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
name|saml
operator|.
name|SamlAssertionWrapper
import|;
end_import

begin_class
specifier|public
class|class
name|SamlEnvelopedOutInterceptor
extends|extends
name|AbstractXmlSecOutInterceptor
block|{
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_ENV_PREFIX
init|=
literal|"env"
decl_stmt|;
specifier|private
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
name|QName
name|envelopeQName
init|=
name|DEFAULT_ENV_QNAME
decl_stmt|;
specifier|private
name|boolean
name|signLater
decl_stmt|;
specifier|public
name|SamlEnvelopedOutInterceptor
parameter_list|()
block|{
comment|// SAML assertions may contain enveloped XML signatures so
comment|// makes sense to avoid having them signed in the detached mode
name|super
operator|.
name|addAfter
argument_list|(
name|XmlSigOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|super
operator|.
name|addBefore
argument_list|(
name|XmlEncOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|SamlEnvelopedOutInterceptor
parameter_list|(
name|boolean
name|signLater
parameter_list|)
block|{
if|if
condition|(
name|signLater
condition|)
block|{
name|super
operator|.
name|addBefore
argument_list|(
name|XmlSigOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|addAfter
argument_list|(
name|XmlSigOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|signLater
operator|=
name|signLater
expr_stmt|;
name|super
operator|.
name|addBefore
argument_list|(
name|XmlEncOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
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
name|createEnvelopedSamlToken
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
name|createEnvelopedSamlToken
parameter_list|(
name|Message
name|message
parameter_list|,
name|Document
name|payloadDoc
parameter_list|)
throws|throws
name|Exception
block|{
name|Element
name|docEl
init|=
name|payloadDoc
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
name|SamlAssertionWrapper
name|assertion
init|=
name|SAMLUtils
operator|.
name|createAssertion
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|QName
name|rootName
init|=
name|DOMUtils
operator|.
name|getElementQName
argument_list|(
name|payloadDoc
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|rootName
operator|.
name|equals
argument_list|(
name|envelopeQName
argument_list|)
condition|)
block|{
name|docEl
operator|.
name|appendChild
argument_list|(
name|assertion
operator|.
name|toDOM
argument_list|(
name|payloadDoc
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|payloadDoc
return|;
block|}
name|Document
name|newDoc
init|=
name|DOMUtils
operator|.
name|createDocument
argument_list|()
decl_stmt|;
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
name|newDoc
operator|.
name|appendChild
argument_list|(
name|root
argument_list|)
expr_stmt|;
name|Element
name|assertionEl
init|=
name|assertion
operator|.
name|toDOM
argument_list|(
name|newDoc
argument_list|)
decl_stmt|;
name|root
operator|.
name|appendChild
argument_list|(
name|assertionEl
argument_list|)
expr_stmt|;
name|payloadDoc
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
name|root
operator|.
name|appendChild
argument_list|(
name|docEl
argument_list|)
expr_stmt|;
if|if
condition|(
name|signLater
condition|)
block|{
comment|// It appears adopting and removing nodes
comment|// leaves some stale refs/state with adopted nodes and thus the digest ends up
comment|// being wrong on the server side if XML sig is applied later in the enveloped mode
comment|// TODO: this is not critical now - but figure out if we can avoid copying
comment|// DOMs
name|CachedOutputStream
name|bos
init|=
operator|new
name|CachedOutputStream
argument_list|()
decl_stmt|;
name|XMLUtils
operator|.
name|writeTo
argument_list|(
name|newDoc
argument_list|,
name|bos
argument_list|)
expr_stmt|;
return|return
name|DOMUtils
operator|.
name|readXml
argument_list|(
name|bos
operator|.
name|getInputStream
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|newDoc
return|;
block|}
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
name|XMLUtils
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

