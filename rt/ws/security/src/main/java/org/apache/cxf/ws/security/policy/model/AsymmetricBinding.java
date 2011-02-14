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
name|ws
operator|.
name|policy
operator|.
name|PolicyBuilder
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
name|builder
operator|.
name|primitive
operator|.
name|PrimitiveAssertion
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
name|neethi
operator|.
name|All
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|ExactlyOne
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Policy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|PolicyComponent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|PolicyContainingAssertion
import|;
end_import

begin_class
specifier|public
class|class
name|AsymmetricBinding
extends|extends
name|SymmetricAsymmetricBindingBase
implements|implements
name|PolicyContainingAssertion
block|{
specifier|private
name|InitiatorToken
name|initiatorToken
decl_stmt|;
specifier|private
name|RecipientToken
name|recipientToken
decl_stmt|;
specifier|public
name|AsymmetricBinding
parameter_list|(
name|SPConstants
name|version
parameter_list|,
name|PolicyBuilder
name|b
parameter_list|)
block|{
name|super
argument_list|(
name|version
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return Returns the initiatorToken.      */
specifier|public
name|InitiatorToken
name|getInitiatorToken
parameter_list|()
block|{
return|return
name|initiatorToken
return|;
block|}
comment|/**      * @param initiatorToken The initiatorToken to set.      */
specifier|public
name|void
name|setInitiatorToken
parameter_list|(
name|InitiatorToken
name|initiatorToken
parameter_list|)
block|{
name|this
operator|.
name|initiatorToken
operator|=
name|initiatorToken
expr_stmt|;
block|}
comment|/**      * @return Returns the recipientToken.      */
specifier|public
name|RecipientToken
name|getRecipientToken
parameter_list|()
block|{
return|return
name|recipientToken
return|;
block|}
comment|/**      * @param recipientToken The recipientToken to set.      */
specifier|public
name|void
name|setRecipientToken
parameter_list|(
name|RecipientToken
name|recipientToken
parameter_list|)
block|{
name|this
operator|.
name|recipientToken
operator|=
name|recipientToken
expr_stmt|;
block|}
specifier|public
name|QName
name|getRealName
parameter_list|()
block|{
return|return
name|constants
operator|.
name|getAsymmetricBinding
argument_list|()
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
name|getAsymmetricBinding
argument_list|()
return|;
block|}
specifier|public
name|PolicyComponent
name|normalize
parameter_list|()
block|{
return|return
name|this
return|;
block|}
specifier|public
name|Policy
name|getPolicy
parameter_list|()
block|{
name|Policy
name|p
init|=
operator|new
name|Policy
argument_list|()
decl_stmt|;
name|ExactlyOne
name|ea
init|=
operator|new
name|ExactlyOne
argument_list|()
decl_stmt|;
name|p
operator|.
name|addPolicyComponent
argument_list|(
name|ea
argument_list|)
expr_stmt|;
name|All
name|all
init|=
operator|new
name|All
argument_list|()
decl_stmt|;
comment|/*         asymmetricBinding.setAlgorithmSuite(algorithmSuite);         asymmetricBinding.setProtectionOrder(getProtectionOrder());         asymmetricBinding.setSignatureProtection(isSignatureProtection());         asymmetricBinding.setSignedEndorsingSupportingTokens(getSignedEndorsingSupportingTokens());         asymmetricBinding.setTokenProtection(isTokenProtection());         */
if|if
condition|(
name|getInitiatorToken
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|all
operator|.
name|addPolicyComponent
argument_list|(
name|getInitiatorToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getRecipientToken
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|all
operator|.
name|addPolicyComponent
argument_list|(
name|getRecipientToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/*         if (isEntireHeadersAndBodySignatures()) {             all.addPolicyComponent(new PrimitiveAssertion(SP12Constants.ONLY_SIGN_ENTIRE_HEADERS_AND_BODY));         }         */
if|if
condition|(
name|isIncludeTimestamp
argument_list|()
condition|)
block|{
name|all
operator|.
name|addPolicyComponent
argument_list|(
operator|new
name|PrimitiveAssertion
argument_list|(
name|SP12Constants
operator|.
name|INCLUDE_TIMESTAMP
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getLayout
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|all
operator|.
name|addPolicyComponent
argument_list|(
name|getLayout
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ea
operator|.
name|addPolicyComponent
argument_list|(
name|all
argument_list|)
expr_stmt|;
name|PolicyComponent
name|pc
init|=
name|p
operator|.
name|normalize
argument_list|(
name|builder
operator|.
name|getPolicyRegistry
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|pc
operator|instanceof
name|Policy
condition|)
block|{
return|return
operator|(
name|Policy
operator|)
name|pc
return|;
block|}
else|else
block|{
name|p
operator|=
operator|new
name|Policy
argument_list|()
expr_stmt|;
name|p
operator|.
name|addPolicyComponent
argument_list|(
name|pc
argument_list|)
expr_stmt|;
return|return
name|p
return|;
block|}
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
name|localname
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
comment|//<sp:AsymmetricBinding>
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|localname
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
name|String
name|pPrefix
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
name|pPrefix
operator|==
literal|null
condition|)
block|{
name|pPrefix
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
name|pPrefix
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
comment|//<wsp:Policy>
name|writer
operator|.
name|writeStartElement
argument_list|(
name|pPrefix
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
if|if
condition|(
name|initiatorToken
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"InitiatorToken is not set"
argument_list|)
throw|;
block|}
comment|//<sp:InitiatorToken>
name|initiatorToken
operator|.
name|serialize
argument_list|(
name|writer
argument_list|)
expr_stmt|;
comment|//</sp:InitiatorToken>
if|if
condition|(
name|recipientToken
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"RecipientToken is not set"
argument_list|)
throw|;
block|}
comment|//<sp:RecipientToken>
name|recipientToken
operator|.
name|serialize
argument_list|(
name|writer
argument_list|)
expr_stmt|;
comment|//</sp:RecipientToken>
name|AlgorithmSuite
name|algorithmSuite
init|=
name|getAlgorithmSuite
argument_list|()
decl_stmt|;
if|if
condition|(
name|algorithmSuite
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"AlgorithmSuite is not set"
argument_list|)
throw|;
block|}
comment|//<sp:AlgorithmSuite>
name|algorithmSuite
operator|.
name|serialize
argument_list|(
name|writer
argument_list|)
expr_stmt|;
comment|//</sp:AlgorithmSuite>
name|Layout
name|layout
init|=
name|getLayout
argument_list|()
decl_stmt|;
if|if
condition|(
name|layout
operator|!=
literal|null
condition|)
block|{
comment|//<sp:Layout>
name|layout
operator|.
name|serialize
argument_list|(
name|writer
argument_list|)
expr_stmt|;
comment|//</sp:Layout>
block|}
if|if
condition|(
name|isIncludeTimestamp
argument_list|()
condition|)
block|{
comment|//<sp:IncludeTimestamp>
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|SPConstants
operator|.
name|INCLUDE_TIMESTAMP
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
comment|//</sp:IncludeTimestamp>
block|}
if|if
condition|(
name|SPConstants
operator|.
name|ProtectionOrder
operator|.
name|EncryptBeforeSigning
operator|.
name|equals
argument_list|(
name|getProtectionOrder
argument_list|()
argument_list|)
condition|)
block|{
comment|//<sp:EncryptBeforeSign />
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|SPConstants
operator|.
name|ProtectionOrder
operator|.
name|EncryptBeforeSigning
operator|.
name|toString
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
block|}
if|if
condition|(
name|isSignatureProtection
argument_list|()
condition|)
block|{
comment|//<sp:EncryptSignature />
comment|// FIXME move the String constants to a QName
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|SPConstants
operator|.
name|ENCRYPT_SIGNATURE
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
name|isTokenProtection
argument_list|()
condition|)
block|{
comment|//<sp:ProtectTokens />
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|SPConstants
operator|.
name|PROTECT_TOKENS
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
name|isEntireHeadersAndBodySignatures
argument_list|()
condition|)
block|{
comment|//<sp:OnlySignEntireHeaderAndBody />
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|SPConstants
operator|.
name|ONLY_SIGN_ENTIRE_HEADERS_AND_BODY
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
comment|//</sp:AsymmetircBinding>
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

