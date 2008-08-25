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

begin_comment
comment|/**  * Model bean to capture Trust10 assertion info  */
end_comment

begin_class
specifier|public
class|class
name|Trust13
extends|extends
name|AbstractSecurityAssertion
block|{
specifier|private
name|boolean
name|mustSupportClientChallenge
decl_stmt|;
specifier|private
name|boolean
name|mustSupportServerChallenge
decl_stmt|;
specifier|private
name|boolean
name|requireClientEntropy
decl_stmt|;
specifier|private
name|boolean
name|requireServerEntropy
decl_stmt|;
specifier|private
name|boolean
name|mustSupportIssuedTokens
decl_stmt|;
specifier|private
name|boolean
name|requireRequestSecurityTokenCollection
decl_stmt|;
specifier|private
name|boolean
name|requireAppliesTo
decl_stmt|;
specifier|public
name|Trust13
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
comment|/**      * @return Returns the mustSupportClientChallenge.      */
specifier|public
name|boolean
name|isMustSupportClientChallenge
parameter_list|()
block|{
return|return
name|mustSupportClientChallenge
return|;
block|}
comment|/**      * @param mustSupportClientChallenge The mustSupportClientChallenge to set.      */
specifier|public
name|void
name|setMustSupportClientChallenge
parameter_list|(
name|boolean
name|mustSupportClientChallenge
parameter_list|)
block|{
name|this
operator|.
name|mustSupportClientChallenge
operator|=
name|mustSupportClientChallenge
expr_stmt|;
block|}
comment|/**      * @return Returns the mustSupportIssuedTokens.      */
specifier|public
name|boolean
name|isMustSupportIssuedTokens
parameter_list|()
block|{
return|return
name|mustSupportIssuedTokens
return|;
block|}
comment|/**      * @param mustSupportIssuedTokens The mustSupportIssuedTokens to set.      */
specifier|public
name|void
name|setMustSupportIssuedTokens
parameter_list|(
name|boolean
name|mustSupportIssuedTokens
parameter_list|)
block|{
name|this
operator|.
name|mustSupportIssuedTokens
operator|=
name|mustSupportIssuedTokens
expr_stmt|;
block|}
comment|/**      * @return Returns the mustSupportServerChallenge.      */
specifier|public
name|boolean
name|isMustSupportServerChallenge
parameter_list|()
block|{
return|return
name|mustSupportServerChallenge
return|;
block|}
comment|/**      * @param mustSupportServerChallenge The mustSupportServerChallenge to set.      */
specifier|public
name|void
name|setMustSupportServerChallenge
parameter_list|(
name|boolean
name|mustSupportServerChallenge
parameter_list|)
block|{
name|this
operator|.
name|mustSupportServerChallenge
operator|=
name|mustSupportServerChallenge
expr_stmt|;
block|}
comment|/**      * @return Returns the requireClientEntropy.      */
specifier|public
name|boolean
name|isRequireClientEntropy
parameter_list|()
block|{
return|return
name|requireClientEntropy
return|;
block|}
comment|/**      * @param requireClientEntropy The requireClientEntropy to set.      */
specifier|public
name|void
name|setRequireClientEntropy
parameter_list|(
name|boolean
name|requireClientEntropy
parameter_list|)
block|{
name|this
operator|.
name|requireClientEntropy
operator|=
name|requireClientEntropy
expr_stmt|;
block|}
comment|/**      * @return Returns the requireServerEntropy.      */
specifier|public
name|boolean
name|isRequireServerEntropy
parameter_list|()
block|{
return|return
name|requireServerEntropy
return|;
block|}
comment|/**      * @param requireServerEntropy The requireServerEntropy to set.      */
specifier|public
name|void
name|setRequireServerEntropy
parameter_list|(
name|boolean
name|requireServerEntropy
parameter_list|)
block|{
name|this
operator|.
name|requireServerEntropy
operator|=
name|requireServerEntropy
expr_stmt|;
block|}
comment|/**      * @return Returns the requireRequestSecurityTokenCollection.      */
specifier|public
name|boolean
name|isRequireRequestSecurityTokenCollection
parameter_list|()
block|{
return|return
name|requireRequestSecurityTokenCollection
return|;
block|}
comment|/**      * @param requireRequestSecurityTokenCollection The requireRequestSecurityTokenCollection to set.      */
specifier|public
name|void
name|setRequireRequestSecurityTokenCollection
parameter_list|(
name|boolean
name|requireRequestSecurityTokenCollection
parameter_list|)
block|{
name|this
operator|.
name|requireRequestSecurityTokenCollection
operator|=
name|requireRequestSecurityTokenCollection
expr_stmt|;
block|}
comment|/**      * @return Returns the requireAppliesTo.      */
specifier|public
name|boolean
name|isRequireAppliesTo
parameter_list|()
block|{
return|return
name|requireAppliesTo
return|;
block|}
comment|/**      * @param requireAppliesTo The requireAppliesTo to set.      */
specifier|public
name|void
name|setRequireAppliesTo
parameter_list|(
name|boolean
name|requireAppliesTo
parameter_list|)
block|{
name|this
operator|.
name|requireAppliesTo
operator|=
name|requireAppliesTo
expr_stmt|;
block|}
specifier|public
name|QName
name|getRealName
parameter_list|()
block|{
return|return
name|SP12Constants
operator|.
name|TRUST_13
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
name|TRUST_13
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
comment|//<sp:Trust13>
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
comment|// xmlns:sp=".."
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
comment|//<wsp:Policy>
name|writer
operator|.
name|writeStartElement
argument_list|(
name|SPConstants
operator|.
name|POLICY
operator|.
name|getPrefix
argument_list|()
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
name|isMustSupportClientChallenge
argument_list|()
condition|)
block|{
comment|//<sp:MustSupportClientChallenge />
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|SPConstants
operator|.
name|MUST_SUPPORT_CLIENT_CHALLENGE
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
name|isMustSupportServerChallenge
argument_list|()
condition|)
block|{
comment|//<sp:MustSupportServerChallenge />
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|SPConstants
operator|.
name|MUST_SUPPORT_SERVER_CHALLENGE
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
name|isRequireClientEntropy
argument_list|()
condition|)
block|{
comment|//<sp:RequireClientEntropy />
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|SPConstants
operator|.
name|REQUIRE_CLIENT_ENTROPY
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
name|isRequireServerEntropy
argument_list|()
condition|)
block|{
comment|//<sp:RequireServerEntropy />
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|SPConstants
operator|.
name|REQUIRE_SERVER_ENTROPY
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
name|isMustSupportIssuedTokens
argument_list|()
condition|)
block|{
comment|//<sp:MustSupportIssuedTokens />
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|SPConstants
operator|.
name|MUST_SUPPORT_ISSUED_TOKENS
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
name|isRequireRequestSecurityTokenCollection
argument_list|()
condition|)
block|{
comment|//<sp:RequireRequestSecurityTokenCollection />
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|SPConstants
operator|.
name|REQUIRE_REQUEST_SECURITY_TOKEN_COLLECTION
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
name|isRequireAppliesTo
argument_list|()
condition|)
block|{
comment|//<sp:RequireAppliesTo />
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|SPConstants
operator|.
name|REQUIRE_APPLIES_TO
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
comment|//</sp:Trust13>
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
specifier|public
name|short
name|getType
parameter_list|()
block|{
return|return
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Constants
operator|.
name|TYPE_ASSERTION
return|;
block|}
block|}
end_class

end_unit

