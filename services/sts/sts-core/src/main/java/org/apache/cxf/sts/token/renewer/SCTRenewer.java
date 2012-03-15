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
name|renewer
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
name|Date
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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|handler
operator|.
name|MessageContext
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
name|CastUtils
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
name|ReceivedToken
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
name|sts
operator|.
name|token
operator|.
name|provider
operator|.
name|TokenReference
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
name|WSSecurityEngineResult
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
name|WSSecurityException
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
name|handler
operator|.
name|WSHandlerConstants
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
name|handler
operator|.
name|WSHandlerResult
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
comment|/**  * This class renews a SecurityContextToken.  */
end_comment

begin_class
specifier|public
class|class
name|SCTRenewer
implements|implements
name|TokenRenewer
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
name|SCTRenewer
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// boolean to enable/disable the check of proof of possession
specifier|private
name|boolean
name|verifyProofOfPossession
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
comment|/**      * Return true if this TokenRenewer implementation is capable of renewing the      * ReceivedToken argument.      */
specifier|public
name|boolean
name|canHandleToken
parameter_list|(
name|ReceivedToken
name|targetToken
parameter_list|)
block|{
name|Object
name|token
init|=
name|targetToken
operator|.
name|getToken
argument_list|()
decl_stmt|;
if|if
condition|(
name|token
operator|instanceof
name|Element
condition|)
block|{
name|Element
name|tokenElement
init|=
operator|(
name|Element
operator|)
name|token
decl_stmt|;
name|String
name|namespace
init|=
name|tokenElement
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
name|String
name|localname
init|=
name|tokenElement
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|STSUtils
operator|.
name|SCT_NS_05_02
operator|.
name|equals
argument_list|(
name|namespace
argument_list|)
operator|||
name|STSUtils
operator|.
name|SCT_NS_05_12
operator|.
name|equals
argument_list|(
name|namespace
argument_list|)
operator|)
operator|&&
literal|"SecurityContextToken"
operator|.
name|equals
argument_list|(
name|localname
argument_list|)
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
comment|/**      * Renew a Token using the given TokenRenewerParameters.      */
specifier|public
name|TokenRenewerResponse
name|renewToken
parameter_list|(
name|TokenRenewerParameters
name|tokenParameters
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Trying to renew a SecurityContextToken"
argument_list|)
expr_stmt|;
name|TokenRequirements
name|tokenRequirements
init|=
name|tokenParameters
operator|.
name|getTokenRequirements
argument_list|()
decl_stmt|;
name|ReceivedToken
name|renewTarget
init|=
name|tokenRequirements
operator|.
name|getRenewTarget
argument_list|()
decl_stmt|;
name|TokenRenewerResponse
name|response
init|=
operator|new
name|TokenRenewerResponse
argument_list|()
decl_stmt|;
name|response
operator|.
name|setTokenRenewed
argument_list|(
literal|false
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
literal|"A cache must be configured to use the SCTRenewer"
argument_list|)
expr_stmt|;
return|return
name|response
return|;
block|}
if|if
condition|(
name|renewTarget
operator|!=
literal|null
operator|&&
name|renewTarget
operator|.
name|isDOMElement
argument_list|()
condition|)
block|{
try|try
block|{
name|Element
name|renewTargetElement
init|=
operator|(
name|Element
operator|)
name|renewTarget
operator|.
name|getToken
argument_list|()
decl_stmt|;
name|SecurityContextToken
name|sct
init|=
operator|new
name|SecurityContextToken
argument_list|(
name|renewTargetElement
argument_list|)
decl_stmt|;
name|String
name|identifier
init|=
name|sct
operator|.
name|getIdentifier
argument_list|()
decl_stmt|;
name|SecurityToken
name|token
init|=
name|tokenParameters
operator|.
name|getTokenStore
argument_list|()
operator|.
name|getToken
argument_list|(
name|identifier
argument_list|)
decl_stmt|;
if|if
condition|(
name|token
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Identifier: "
operator|+
name|identifier
operator|+
literal|" is not found in the cache"
argument_list|)
expr_stmt|;
return|return
name|response
return|;
block|}
if|if
condition|(
name|verifyProofOfPossession
operator|&&
operator|!
name|matchKey
argument_list|(
name|tokenParameters
argument_list|,
name|token
operator|.
name|getSecret
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|STSException
argument_list|(
literal|"Failed to verify the proof of possession of the key associated with the "
operator|+
literal|"security context. No matching key found in the request."
argument_list|,
name|STSException
operator|.
name|INVALID_REQUEST
argument_list|)
throw|;
block|}
comment|// Remove old token from the cache
name|tokenParameters
operator|.
name|getTokenStore
argument_list|()
operator|.
name|remove
argument_list|(
name|token
argument_list|)
expr_stmt|;
comment|// Create a new token corresponding to the old token
name|Date
name|expires
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|long
name|currentTime
init|=
name|expires
operator|.
name|getTime
argument_list|()
decl_stmt|;
name|expires
operator|.
name|setTime
argument_list|(
name|currentTime
operator|+
operator|(
name|lifetime
operator|*
literal|1000L
operator|)
argument_list|)
expr_stmt|;
name|SecurityToken
name|newToken
init|=
operator|new
name|SecurityToken
argument_list|(
name|identifier
argument_list|,
literal|null
argument_list|,
name|expires
argument_list|)
decl_stmt|;
name|newToken
operator|.
name|setPrincipal
argument_list|(
name|token
operator|.
name|getPrincipal
argument_list|()
argument_list|)
expr_stmt|;
name|newToken
operator|.
name|setSecret
argument_list|(
name|token
operator|.
name|getSecret
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|token
operator|.
name|getProperties
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|newToken
operator|.
name|setProperties
argument_list|(
name|token
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|tokenParameters
operator|.
name|getTokenStore
argument_list|()
operator|.
name|add
argument_list|(
name|newToken
argument_list|)
expr_stmt|;
name|response
operator|.
name|setTokenRenewed
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|response
operator|.
name|setRenewedToken
argument_list|(
name|sct
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|tokenRequirements
operator|.
name|getTokenType
argument_list|()
operator|!=
literal|null
condition|)
block|{
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
block|}
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
if|if
condition|(
name|tokenRequirements
operator|.
name|getTokenType
argument_list|()
operator|!=
literal|null
condition|)
block|{
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
block|}
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
block|}
catch|catch
parameter_list|(
name|WSSecurityException
name|ex
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
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|response
return|;
block|}
specifier|private
name|boolean
name|matchKey
parameter_list|(
name|TokenRenewerParameters
name|tokenParameters
parameter_list|,
name|byte
index|[]
name|secretKey
parameter_list|)
block|{
name|boolean
name|result
init|=
literal|false
decl_stmt|;
name|MessageContext
name|messageContext
init|=
name|tokenParameters
operator|.
name|getWebServiceContext
argument_list|()
operator|.
name|getMessageContext
argument_list|()
decl_stmt|;
specifier|final
name|List
argument_list|<
name|WSHandlerResult
argument_list|>
name|handlerResults
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|messageContext
operator|.
name|get
argument_list|(
name|WSHandlerConstants
operator|.
name|RECV_RESULTS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|handlerResults
operator|!=
literal|null
operator|&&
name|handlerResults
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|WSHandlerResult
name|handlerResult
init|=
name|handlerResults
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|engineResults
init|=
name|handlerResult
operator|.
name|getResults
argument_list|()
decl_stmt|;
for|for
control|(
name|WSSecurityEngineResult
name|engineResult
range|:
name|engineResults
control|)
block|{
name|Integer
name|action
init|=
operator|(
name|Integer
operator|)
name|engineResult
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_ACTION
argument_list|)
decl_stmt|;
if|if
condition|(
name|action
operator|.
name|equals
argument_list|(
name|WSConstants
operator|.
name|SIGN
argument_list|)
condition|)
block|{
name|byte
index|[]
name|receivedKey
init|=
operator|(
name|byte
index|[]
operator|)
name|engineResult
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_SECRET
argument_list|)
decl_stmt|;
if|if
condition|(
name|Arrays
operator|.
name|equals
argument_list|(
name|secretKey
argument_list|,
name|receivedKey
argument_list|)
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
literal|"Verification of the proof of possession of the key associated with "
operator|+
literal|"the security context successful."
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
block|}
block|}
return|return
name|result
return|;
block|}
specifier|public
name|void
name|setVerifyProofOfPossession
parameter_list|(
name|boolean
name|verifyProofOfPossession
parameter_list|)
block|{
name|this
operator|.
name|verifyProofOfPossession
operator|=
name|verifyProofOfPossession
expr_stmt|;
block|}
block|}
end_class

end_unit

