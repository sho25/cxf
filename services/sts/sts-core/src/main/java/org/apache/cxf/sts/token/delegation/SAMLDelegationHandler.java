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
name|delegation
package|;
end_package

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
name|ReceivedToken
operator|.
name|STATE
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
name|WSS4JConstants
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
name|common
operator|.
name|saml
operator|.
name|SamlAssertionWrapper
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
name|builder
operator|.
name|SAML1Constants
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
name|builder
operator|.
name|SAML2Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml1
operator|.
name|core
operator|.
name|AudienceRestrictionCondition
import|;
end_import

begin_comment
comment|/**  * The SAML TokenDelegationHandler implementation. It disallows ActAs or OnBehalfOf for  * all cases apart from the case of a Bearer SAML Token. In addition, the AppliesTo  * address (if supplied) must match an AudienceRestriction address (if in token), if the  * "checkAudienceRestriction" property is set to "true".  */
end_comment

begin_class
specifier|public
class|class
name|SAMLDelegationHandler
implements|implements
name|TokenDelegationHandler
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
name|SAMLDelegationHandler
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|boolean
name|checkAudienceRestriction
decl_stmt|;
specifier|public
name|boolean
name|canHandleToken
parameter_list|(
name|ReceivedToken
name|delegateTarget
parameter_list|)
block|{
name|Object
name|token
init|=
name|delegateTarget
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
name|WSS4JConstants
operator|.
name|SAML_NS
operator|.
name|equals
argument_list|(
name|namespace
argument_list|)
operator|||
name|WSS4JConstants
operator|.
name|SAML2_NS
operator|.
name|equals
argument_list|(
name|namespace
argument_list|)
operator|)
operator|&&
literal|"Assertion"
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
specifier|public
name|TokenDelegationResponse
name|isDelegationAllowed
parameter_list|(
name|TokenDelegationParameters
name|tokenParameters
parameter_list|)
block|{
name|TokenDelegationResponse
name|response
init|=
operator|new
name|TokenDelegationResponse
argument_list|()
decl_stmt|;
name|ReceivedToken
name|delegateTarget
init|=
name|tokenParameters
operator|.
name|getToken
argument_list|()
decl_stmt|;
name|response
operator|.
name|setToken
argument_list|(
name|delegateTarget
argument_list|)
expr_stmt|;
if|if
condition|(
name|delegateTarget
operator|.
name|getState
argument_list|()
operator|!=
name|STATE
operator|.
name|VALID
operator|||
operator|!
name|delegateTarget
operator|.
name|isDOMElement
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Delegation token is not valid"
argument_list|)
expr_stmt|;
return|return
name|response
return|;
block|}
if|if
condition|(
name|isDelegationAllowed
argument_list|(
name|delegateTarget
argument_list|,
name|tokenParameters
operator|.
name|getAppliesToAddress
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Delegation is allowed for principal "
operator|+
name|tokenParameters
operator|.
name|getPrincipal
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|response
operator|.
name|setDelegationAllowed
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Delegation is not allowed for principal "
operator|+
name|tokenParameters
operator|.
name|getPrincipal
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|response
return|;
block|}
comment|/**      * Is Delegation allowed for a particular token      */
specifier|protected
name|boolean
name|isDelegationAllowed
parameter_list|(
name|ReceivedToken
name|receivedToken
parameter_list|,
name|String
name|appliesToAddress
parameter_list|)
block|{
name|Element
name|validateTargetElement
init|=
operator|(
name|Element
operator|)
name|receivedToken
operator|.
name|getToken
argument_list|()
decl_stmt|;
try|try
block|{
name|SamlAssertionWrapper
name|assertion
init|=
operator|new
name|SamlAssertionWrapper
argument_list|(
name|validateTargetElement
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|confirmationMethod
range|:
name|assertion
operator|.
name|getConfirmationMethods
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
operator|(
name|SAML1Constants
operator|.
name|CONF_BEARER
operator|.
name|equals
argument_list|(
name|confirmationMethod
argument_list|)
operator|||
name|SAML2Constants
operator|.
name|CONF_BEARER
operator|.
name|equals
argument_list|(
name|confirmationMethod
argument_list|)
operator|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"An unsupported Confirmation Method was used: "
operator|+
name|confirmationMethod
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
if|if
condition|(
name|checkAudienceRestriction
operator|&&
name|appliesToAddress
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|addresses
init|=
name|getAudienceRestrictions
argument_list|(
name|assertion
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|addresses
operator|.
name|isEmpty
argument_list|()
operator|||
name|addresses
operator|.
name|contains
argument_list|(
name|appliesToAddress
argument_list|)
operator|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"The AppliesTo address "
operator|+
name|appliesToAddress
operator|+
literal|" is not contained"
operator|+
literal|" in the Audience Restriction addresses in the assertion"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
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
literal|"Error in ascertaining whether delegation is allowed"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|getAudienceRestrictions
parameter_list|(
name|SamlAssertionWrapper
name|assertion
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|addresses
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|assertion
operator|.
name|getSaml1
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|AudienceRestrictionCondition
name|restriction
range|:
name|assertion
operator|.
name|getSaml1
argument_list|()
operator|.
name|getConditions
argument_list|()
operator|.
name|getAudienceRestrictionConditions
argument_list|()
control|)
block|{
for|for
control|(
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml1
operator|.
name|core
operator|.
name|Audience
name|audience
range|:
name|restriction
operator|.
name|getAudiences
argument_list|()
control|)
block|{
name|addresses
operator|.
name|add
argument_list|(
name|audience
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|assertion
operator|.
name|getSaml2
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml2
operator|.
name|core
operator|.
name|AudienceRestriction
name|restriction
range|:
name|assertion
operator|.
name|getSaml2
argument_list|()
operator|.
name|getConditions
argument_list|()
operator|.
name|getAudienceRestrictions
argument_list|()
control|)
block|{
for|for
control|(
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Audience
name|audience
range|:
name|restriction
operator|.
name|getAudiences
argument_list|()
control|)
block|{
name|addresses
operator|.
name|add
argument_list|(
name|audience
operator|.
name|getAudienceURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|addresses
return|;
block|}
specifier|public
name|boolean
name|isCheckAudienceRestriction
parameter_list|()
block|{
return|return
name|checkAudienceRestriction
return|;
block|}
comment|/**      * Set whether to perform a check that the received AppliesTo address is contained in the      * token as one of the AudienceRestriction URIs. The default is false.      * @param checkAudienceRestriction whether to perform an audience restriction check or not      */
specifier|public
name|void
name|setCheckAudienceRestriction
parameter_list|(
name|boolean
name|checkAudienceRestriction
parameter_list|)
block|{
name|this
operator|.
name|checkAudienceRestriction
operator|=
name|checkAudienceRestriction
expr_stmt|;
block|}
block|}
end_class

end_unit

