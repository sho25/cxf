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
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
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
name|sts
operator|.
name|request
operator|.
name|TokenRequirements
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
name|sts
operator|.
name|provider
operator|.
name|STSException
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
name|tokenstore
operator|.
name|SecurityToken
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
name|trust
operator|.
name|STSUtils
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
name|conversation
operator|.
name|ConversationConstants
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
name|conversation
operator|.
name|ConversationException
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
name|message
operator|.
name|token
operator|.
name|SecurityContextToken
import|;
end_import

begin_comment
comment|/**  * A TokenProvider implementation that provides a SecurityContextToken.  */
end_comment

begin_class
specifier|public
class|class
name|SCTProvider
implements|implements
name|TokenProvider
block|{
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
name|SCTProvider
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|boolean
name|returnEntropy
init|=
literal|true
decl_stmt|;
specifier|private
name|long
name|lifetime
init|=
literal|300L
decl_stmt|;
comment|/**      * Return the lifetime of the generated SCT      * @return the lifetime of the generated SCT      */
specifier|public
name|long
name|getLifetime
parameter_list|()
block|{
return|return
name|lifetime
return|;
block|}
comment|/**      * Set the lifetime of the generated SCT      * @param lifetime the lifetime of the generated SCT      */
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
comment|/**      * Return true if this TokenProvider implementation is capable of providing a token      * that corresponds to the given TokenType.      */
specifier|public
name|boolean
name|canHandleToken
parameter_list|(
name|String
name|tokenType
parameter_list|)
block|{
return|return
name|canHandleToken
argument_list|(
name|tokenType
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Return true if this TokenProvider implementation is capable of providing a token      * that corresponds to the given TokenType in a given realm. The realm is ignored in this       * token provider.      */
specifier|public
name|boolean
name|canHandleToken
parameter_list|(
name|String
name|tokenType
parameter_list|,
name|String
name|realm
parameter_list|)
block|{
if|if
condition|(
name|STSUtils
operator|.
name|TOKEN_TYPE_SCT_05_02
operator|.
name|equals
argument_list|(
name|tokenType
argument_list|)
operator|||
name|STSUtils
operator|.
name|TOKEN_TYPE_SCT_05_12
operator|.
name|equals
argument_list|(
name|tokenType
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Set whether Entropy is returned to the client or not      * @param returnEntropy whether Entropy is returned to the client or not      */
specifier|public
name|void
name|setReturnEntropy
parameter_list|(
name|boolean
name|returnEntropy
parameter_list|)
block|{
name|this
operator|.
name|returnEntropy
operator|=
name|returnEntropy
expr_stmt|;
block|}
comment|/**      * Get whether Entropy is returned to the client or not      * @return whether Entropy is returned to the client or not      */
specifier|public
name|boolean
name|isReturnEntropy
parameter_list|()
block|{
return|return
name|returnEntropy
return|;
block|}
comment|/**      * Create a token given a TokenProviderParameters      */
specifier|public
name|TokenProviderResponse
name|createToken
parameter_list|(
name|TokenProviderParameters
name|tokenParameters
parameter_list|)
block|{
name|TokenRequirements
name|tokenRequirements
init|=
name|tokenParameters
operator|.
name|getTokenRequirements
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Handling token of type: "
operator|+
name|tokenRequirements
operator|.
name|getTokenType
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|tokenParameters
operator|.
name|getTokenStore
argument_list|()
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"A cache must be configured to use the SCTProvider"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
literal|"Can't serialize SCT"
argument_list|,
name|STSException
operator|.
name|REQUEST_FAILED
argument_list|)
throw|;
block|}
name|SymmetricKeyHandler
name|keyHandler
init|=
operator|new
name|SymmetricKeyHandler
argument_list|(
name|tokenParameters
argument_list|)
decl_stmt|;
name|keyHandler
operator|.
name|createSymmetricKey
argument_list|()
expr_stmt|;
try|try
block|{
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|createDocument
argument_list|()
decl_stmt|;
name|SecurityContextToken
name|sct
init|=
operator|new
name|SecurityContextToken
argument_list|(
name|getWSCVersion
argument_list|(
name|tokenRequirements
operator|.
name|getTokenType
argument_list|()
argument_list|)
argument_list|,
name|doc
argument_list|)
decl_stmt|;
name|TokenProviderResponse
name|response
init|=
operator|new
name|TokenProviderResponse
argument_list|()
decl_stmt|;
name|response
operator|.
name|setToken
argument_list|(
name|sct
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
name|response
operator|.
name|setTokenId
argument_list|(
name|sct
operator|.
name|getIdentifier
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|returnEntropy
condition|)
block|{
name|response
operator|.
name|setEntropy
argument_list|(
name|keyHandler
operator|.
name|getEntropyBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|long
name|keySize
init|=
name|keyHandler
operator|.
name|getKeySize
argument_list|()
decl_stmt|;
name|response
operator|.
name|setKeySize
argument_list|(
name|keySize
argument_list|)
expr_stmt|;
name|response
operator|.
name|setComputedKey
argument_list|(
name|keyHandler
operator|.
name|isComputedKey
argument_list|()
argument_list|)
expr_stmt|;
comment|// putting the secret key into the cache
name|SecurityToken
name|token
init|=
operator|new
name|SecurityToken
argument_list|(
name|sct
operator|.
name|getIdentifier
argument_list|()
argument_list|)
decl_stmt|;
name|token
operator|.
name|setSecret
argument_list|(
name|keyHandler
operator|.
name|getSecret
argument_list|()
argument_list|)
expr_stmt|;
name|token
operator|.
name|setPrincipal
argument_list|(
name|tokenParameters
operator|.
name|getPrincipal
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|lifetime
operator|>
literal|0
condition|)
block|{
name|Integer
name|lifetimeInteger
init|=
operator|new
name|Integer
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
name|lifetime
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
decl_stmt|;
name|tokenParameters
operator|.
name|getTokenStore
argument_list|()
operator|.
name|add
argument_list|(
name|token
argument_list|,
name|lifetimeInteger
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|tokenParameters
operator|.
name|getTokenStore
argument_list|()
operator|.
name|add
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
comment|// Create the references
name|TokenReference
name|attachedReference
init|=
operator|new
name|TokenReference
argument_list|()
decl_stmt|;
name|attachedReference
operator|.
name|setIdentifier
argument_list|(
name|sct
operator|.
name|getID
argument_list|()
argument_list|)
expr_stmt|;
name|attachedReference
operator|.
name|setUseDirectReference
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|attachedReference
operator|.
name|setWsseValueType
argument_list|(
name|tokenRequirements
operator|.
name|getTokenType
argument_list|()
argument_list|)
expr_stmt|;
name|response
operator|.
name|setAttachedReference
argument_list|(
name|attachedReference
argument_list|)
expr_stmt|;
name|TokenReference
name|unAttachedReference
init|=
operator|new
name|TokenReference
argument_list|()
decl_stmt|;
name|unAttachedReference
operator|.
name|setIdentifier
argument_list|(
name|sct
operator|.
name|getIdentifier
argument_list|()
argument_list|)
expr_stmt|;
name|unAttachedReference
operator|.
name|setUseDirectReference
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|unAttachedReference
operator|.
name|setWsseValueType
argument_list|(
name|tokenRequirements
operator|.
name|getTokenType
argument_list|()
argument_list|)
expr_stmt|;
name|response
operator|.
name|setUnattachedReference
argument_list|(
name|unAttachedReference
argument_list|)
expr_stmt|;
name|response
operator|.
name|setLifetime
argument_list|(
name|lifetime
argument_list|)
expr_stmt|;
return|return
name|response
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|""
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
literal|"Can't serialize SCT"
argument_list|,
name|e
argument_list|,
name|STSException
operator|.
name|REQUEST_FAILED
argument_list|)
throw|;
block|}
block|}
comment|/**      * Get the Secure Conversation version from the TokenType parameter      */
specifier|private
specifier|static
name|int
name|getWSCVersion
parameter_list|(
name|String
name|tokenType
parameter_list|)
throws|throws
name|ConversationException
block|{
if|if
condition|(
name|tokenType
operator|==
literal|null
condition|)
block|{
return|return
name|ConversationConstants
operator|.
name|DEFAULT_VERSION
return|;
block|}
if|if
condition|(
name|tokenType
operator|.
name|startsWith
argument_list|(
name|ConversationConstants
operator|.
name|WSC_NS_05_02
argument_list|)
condition|)
block|{
return|return
name|ConversationConstants
operator|.
name|getWSTVersion
argument_list|(
name|ConversationConstants
operator|.
name|WSC_NS_05_02
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|tokenType
operator|.
name|startsWith
argument_list|(
name|ConversationConstants
operator|.
name|WSC_NS_05_12
argument_list|)
condition|)
block|{
return|return
name|ConversationConstants
operator|.
name|getWSTVersion
argument_list|(
name|ConversationConstants
operator|.
name|WSC_NS_05_12
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|ConversationException
argument_list|(
literal|"unsupportedSecConvVersion"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

