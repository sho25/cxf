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
name|provider
package|;
end_package

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

begin_comment
comment|/**  * This class encapsulates the response from a TokenProvider instance after creating a token.  */
end_comment

begin_class
specifier|public
class|class
name|TokenProviderResponse
block|{
specifier|private
name|Element
name|token
decl_stmt|;
specifier|private
name|String
name|tokenId
decl_stmt|;
specifier|private
name|long
name|lifetime
decl_stmt|;
specifier|private
name|byte
index|[]
name|entropy
decl_stmt|;
specifier|private
name|long
name|keySize
decl_stmt|;
specifier|private
name|boolean
name|computedKey
decl_stmt|;
specifier|private
name|TokenReference
name|attachedReference
decl_stmt|;
specifier|private
name|TokenReference
name|unAttachedReference
decl_stmt|;
comment|/**      * Return true if the entropy represents a Computed Key.      */
specifier|public
name|boolean
name|isComputedKey
parameter_list|()
block|{
return|return
name|computedKey
return|;
block|}
comment|/**      * Set whether the entropy represents a Computed Key or not      */
specifier|public
name|void
name|setComputedKey
parameter_list|(
name|boolean
name|computedKey
parameter_list|)
block|{
name|this
operator|.
name|computedKey
operator|=
name|computedKey
expr_stmt|;
block|}
comment|/**      * Get the KeySize that the TokenProvider set      */
specifier|public
name|long
name|getKeySize
parameter_list|()
block|{
return|return
name|keySize
return|;
block|}
comment|/**      * Set the KeySize      */
specifier|public
name|void
name|setKeySize
parameter_list|(
name|long
name|keySize
parameter_list|)
block|{
name|this
operator|.
name|keySize
operator|=
name|keySize
expr_stmt|;
block|}
comment|/**      * Set the token      * @param token the token to set      */
specifier|public
name|void
name|setToken
parameter_list|(
name|Element
name|token
parameter_list|)
block|{
name|this
operator|.
name|token
operator|=
name|token
expr_stmt|;
block|}
comment|/**      * Get the token      * @return the token to set      */
specifier|public
name|Element
name|getToken
parameter_list|()
block|{
return|return
name|token
return|;
block|}
comment|/**      * Set the token Id      * @param tokenId the token Id      */
specifier|public
name|void
name|setTokenId
parameter_list|(
name|String
name|tokenId
parameter_list|)
block|{
name|this
operator|.
name|tokenId
operator|=
name|tokenId
expr_stmt|;
block|}
comment|/**      * Get the token Id      * @return the token Id      */
specifier|public
name|String
name|getTokenId
parameter_list|()
block|{
return|return
name|tokenId
return|;
block|}
comment|/**      * Set the lifetime of the Token to be returned in seconds      * @param lifetime the lifetime of the Token to be returned in seconds      */
specifier|public
name|void
name|setLifetime
parameter_list|(
name|long
name|lifetime
parameter_list|)
block|{
name|this
operator|.
name|lifetime
operator|=
name|lifetime
expr_stmt|;
block|}
comment|/**      * Get the lifetime of the Token to be returned in seconds      * @return the lifetime of the Token to be returned in seconds      */
specifier|public
name|long
name|getLifetime
parameter_list|()
block|{
return|return
name|lifetime
return|;
block|}
comment|/**      * Set the entropy associated with the token.      * @param entropy the entropy associated with the token.      */
specifier|public
name|void
name|setEntropy
parameter_list|(
name|byte
index|[]
name|entropy
parameter_list|)
block|{
name|this
operator|.
name|entropy
operator|=
name|entropy
expr_stmt|;
block|}
comment|/**      * Get the entropy associated with the token.      * @return the entropy associated with the token.      */
specifier|public
name|byte
index|[]
name|getEntropy
parameter_list|()
block|{
return|return
name|entropy
return|;
block|}
comment|/**      * Set the attached TokenReference      * @param attachtedReference the attached TokenReference      */
specifier|public
name|void
name|setAttachedReference
parameter_list|(
name|TokenReference
name|attachedReference
parameter_list|)
block|{
name|this
operator|.
name|attachedReference
operator|=
name|attachedReference
expr_stmt|;
block|}
comment|/**      * Get the attached TokenReference      * @return the attached TokenReference      */
specifier|public
name|TokenReference
name|getAttachedReference
parameter_list|()
block|{
return|return
name|attachedReference
return|;
block|}
comment|/**      * Set the unattached TokenReference      * @param unAttachedReference  Set the unattached TokenReference      */
specifier|public
name|void
name|setUnattachedReference
parameter_list|(
name|TokenReference
name|unattachedReference
parameter_list|)
block|{
name|this
operator|.
name|unAttachedReference
operator|=
name|unattachedReference
expr_stmt|;
block|}
comment|/**      * Get the unattached TokenReference      * @return the unattached TokenReference      */
specifier|public
name|TokenReference
name|getUnAttachedReference
parameter_list|()
block|{
return|return
name|unAttachedReference
return|;
block|}
block|}
end_class

end_unit

