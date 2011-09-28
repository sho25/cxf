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
name|sts
operator|.
name|token
operator|.
name|realm
package|;
end_package

begin_comment
comment|/**  * This class defines some properties that are associated with a realm for the SAMLTokenProvider and  * SAMLTokenValidator.  */
end_comment

begin_class
specifier|public
class|class
name|SAMLRealm
block|{
specifier|private
name|String
name|issuer
decl_stmt|;
specifier|private
name|String
name|signatureAlias
decl_stmt|;
comment|/**      * Get the issuer of this SAML realm      * @return the issuer of this SAML realm      */
specifier|public
name|String
name|getIssuer
parameter_list|()
block|{
return|return
name|issuer
return|;
block|}
comment|/**      * Set the issuer of this SAML realm      * @param issuer the issuer of this SAML realm      */
specifier|public
name|void
name|setIssuer
parameter_list|(
name|String
name|issuer
parameter_list|)
block|{
name|this
operator|.
name|issuer
operator|=
name|issuer
expr_stmt|;
block|}
comment|/**      * Get the signature alias to use for this SAML realm      * @return the signature alias to use for this SAML realm      */
specifier|public
name|String
name|getSignatureAlias
parameter_list|()
block|{
return|return
name|signatureAlias
return|;
block|}
comment|/**      * Set the signature alias to use for this SAML realm      * @param signatureAlias the signature alias to use for this SAML realm      */
specifier|public
name|void
name|setSignatureAlias
parameter_list|(
name|String
name|signatureAlias
parameter_list|)
block|{
name|this
operator|.
name|signatureAlias
operator|=
name|signatureAlias
expr_stmt|;
block|}
block|}
end_class

end_unit

