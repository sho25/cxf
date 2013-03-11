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
name|wss4j
operator|.
name|policyvalidators
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

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
name|message
operator|.
name|MessageUtils
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
name|AssertionInfo
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
name|AssertionInfoMap
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
name|policy
operator|.
name|SPConstants
operator|.
name|IncludeTokenType
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
name|policy
operator|.
name|model
operator|.
name|AbstractToken
import|;
end_import

begin_comment
comment|/**  * Some abstract functionality for validating a Security Token.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractTokenPolicyValidator
block|{
comment|/**      * Check to see if a token is required or not.      * @param token the token      * @param message The message      * @return true if the token is required      */
specifier|protected
name|boolean
name|isTokenRequired
parameter_list|(
name|AbstractToken
name|token
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
name|IncludeTokenType
name|inclusion
init|=
name|token
operator|.
name|getIncludeTokenType
argument_list|()
decl_stmt|;
if|if
condition|(
name|inclusion
operator|==
name|IncludeTokenType
operator|.
name|INCLUDE_TOKEN_NEVER
condition|)
block|{
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|inclusion
operator|==
name|IncludeTokenType
operator|.
name|INCLUDE_TOKEN_ALWAYS
condition|)
block|{
return|return
literal|true
return|;
block|}
else|else
block|{
name|boolean
name|initiator
init|=
name|MessageUtils
operator|.
name|isRequestor
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|initiator
operator|&&
operator|(
name|inclusion
operator|==
name|IncludeTokenType
operator|.
name|INCLUDE_TOKEN_ALWAYS_TO_INITIATOR
operator|)
condition|)
block|{
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|initiator
operator|&&
operator|(
name|inclusion
operator|==
name|IncludeTokenType
operator|.
name|INCLUDE_TOKEN_ONCE
operator|||
name|inclusion
operator|==
name|IncludeTokenType
operator|.
name|INCLUDE_TOKEN_ALWAYS_TO_RECIPIENT
operator|)
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
block|}
specifier|protected
name|boolean
name|assertPolicy
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|QName
name|q
parameter_list|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
name|aim
operator|.
name|get
argument_list|(
name|q
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|!=
literal|null
operator|&&
operator|!
name|ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

