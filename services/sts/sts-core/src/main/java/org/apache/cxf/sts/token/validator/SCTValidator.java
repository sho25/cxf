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
name|validator
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|message
operator|.
name|token
operator|.
name|SecurityContextToken
import|;
end_import

begin_comment
comment|/**  * This class validates a SecurityContextToken.  */
end_comment

begin_class
specifier|public
class|class
name|SCTValidator
implements|implements
name|TokenValidator
block|{
comment|/**      * This tag refers to the secret key (byte[]) associated with a SecurityContextToken that has been      * validated. It is inserted into the additional properties map of the response, so that it can be      * retrieved and inserted into a generated token by a TokenProvider instance.      */
specifier|public
specifier|static
specifier|final
name|String
name|SCT_VALIDATOR_SECRET
init|=
literal|"sct-validator-secret"
decl_stmt|;
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
name|SCTValidator
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Return true if this TokenValidator implementation is capable of validating the      * ReceivedToken argument. The realm is ignored in this token Validator.      */
specifier|public
name|boolean
name|canHandleToken
parameter_list|(
name|ReceivedToken
name|validateTarget
parameter_list|)
block|{
return|return
name|canHandleToken
argument_list|(
name|validateTarget
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Return true if this TokenValidator implementation is capable of validating the      * ReceivedToken argument. The realm is ignored in this token Validator.      */
specifier|public
name|boolean
name|canHandleToken
parameter_list|(
name|ReceivedToken
name|validateTarget
parameter_list|,
name|String
name|realm
parameter_list|)
block|{
name|Object
name|token
init|=
name|validateTarget
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
comment|/**      * Validate a Token using the given TokenValidatorParameters.      */
specifier|public
name|TokenValidatorResponse
name|validateToken
parameter_list|(
name|TokenValidatorParameters
name|tokenParameters
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Validating SecurityContextToken"
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
literal|"A cache must be configured to use the SCTValidator"
argument_list|)
expr_stmt|;
name|TokenValidatorResponse
name|response
init|=
operator|new
name|TokenValidatorResponse
argument_list|()
decl_stmt|;
name|response
operator|.
name|setValid
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return
name|response
return|;
block|}
name|TokenRequirements
name|tokenRequirements
init|=
name|tokenParameters
operator|.
name|getTokenRequirements
argument_list|()
decl_stmt|;
name|ReceivedToken
name|validateTarget
init|=
name|tokenRequirements
operator|.
name|getValidateTarget
argument_list|()
decl_stmt|;
name|TokenValidatorResponse
name|response
init|=
operator|new
name|TokenValidatorResponse
argument_list|()
decl_stmt|;
name|response
operator|.
name|setValid
argument_list|(
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|validateTarget
operator|!=
literal|null
operator|&&
name|validateTarget
operator|.
name|isDOMElement
argument_list|()
condition|)
block|{
try|try
block|{
name|Element
name|validateTargetElement
init|=
operator|(
name|Element
operator|)
name|validateTarget
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
name|validateTargetElement
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
name|byte
index|[]
name|secret
init|=
operator|(
name|byte
index|[]
operator|)
name|token
operator|.
name|getSecret
argument_list|()
decl_stmt|;
name|response
operator|.
name|setValid
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|SCT_VALIDATOR_SECRET
argument_list|,
name|secret
argument_list|)
expr_stmt|;
name|response
operator|.
name|setAdditionalProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|response
operator|.
name|setPrincipal
argument_list|(
name|token
operator|.
name|getPrincipal
argument_list|()
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
block|}
end_class

end_unit

