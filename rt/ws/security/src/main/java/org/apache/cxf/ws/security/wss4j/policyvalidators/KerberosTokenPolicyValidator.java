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
name|cxf
operator|.
name|ws
operator|.
name|security
operator|.
name|policy
operator|.
name|PolicyUtils
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
name|dom
operator|.
name|message
operator|.
name|token
operator|.
name|KerberosSecurity
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
name|KerberosToken
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
name|KerberosToken
operator|.
name|ApReqTokenType
import|;
end_import

begin_comment
comment|/**  * Validate a WSSecurityEngineResult corresponding to the processing of a Kerberos Token  * against the appropriate policy.  */
end_comment

begin_class
specifier|public
class|class
name|KerberosTokenPolicyValidator
extends|extends
name|AbstractTokenPolicyValidator
block|{
specifier|private
name|Message
name|message
decl_stmt|;
specifier|public
name|KerberosTokenPolicyValidator
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
block|}
specifier|public
name|boolean
name|validatePolicy
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|KerberosSecurity
name|kerberosToken
parameter_list|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|krbAis
init|=
name|PolicyUtils
operator|.
name|getAllAssertionsByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|KERBEROS_TOKEN
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|krbAis
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|parsePolicies
argument_list|(
name|aim
argument_list|,
name|krbAis
argument_list|,
name|kerberosToken
argument_list|)
expr_stmt|;
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|REQUIRE_KEY_IDENTIFIER_REFERENCE
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|void
name|parsePolicies
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
parameter_list|,
name|KerberosSecurity
name|kerberosToken
parameter_list|)
block|{
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|KerberosToken
name|kerberosTokenPolicy
init|=
operator|(
name|KerberosToken
operator|)
name|ai
operator|.
name|getAssertion
argument_list|()
decl_stmt|;
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isTokenRequired
argument_list|(
name|kerberosTokenPolicy
argument_list|,
name|message
argument_list|)
condition|)
block|{
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
operator|new
name|QName
argument_list|(
name|kerberosTokenPolicy
operator|.
name|getVersion
argument_list|()
operator|.
name|getNamespace
argument_list|()
argument_list|,
literal|"WssKerberosV5ApReqToken11"
argument_list|)
argument_list|)
expr_stmt|;
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
operator|new
name|QName
argument_list|(
name|kerberosTokenPolicy
operator|.
name|getVersion
argument_list|()
operator|.
name|getNamespace
argument_list|()
argument_list|,
literal|"WssGssKerberosV5ApReqToken11"
argument_list|)
argument_list|)
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
operator|!
name|checkToken
argument_list|(
name|aim
argument_list|,
name|kerberosTokenPolicy
argument_list|,
name|kerberosToken
argument_list|)
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"An incorrect Kerberos Token Type is detected"
argument_list|)
expr_stmt|;
continue|continue;
block|}
block|}
block|}
specifier|private
name|boolean
name|checkToken
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|KerberosToken
name|kerberosTokenPolicy
parameter_list|,
name|KerberosSecurity
name|kerberosToken
parameter_list|)
block|{
name|ApReqTokenType
name|apReqTokenType
init|=
name|kerberosTokenPolicy
operator|.
name|getApReqTokenType
argument_list|()
decl_stmt|;
if|if
condition|(
name|apReqTokenType
operator|==
name|ApReqTokenType
operator|.
name|WssKerberosV5ApReqToken11
operator|&&
name|kerberosToken
operator|.
name|isV5ApReq
argument_list|()
condition|)
block|{
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
operator|new
name|QName
argument_list|(
name|kerberosTokenPolicy
operator|.
name|getVersion
argument_list|()
operator|.
name|getNamespace
argument_list|()
argument_list|,
literal|"WssKerberosV5ApReqToken11"
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|apReqTokenType
operator|==
name|ApReqTokenType
operator|.
name|WssGssKerberosV5ApReqToken11
operator|&&
name|kerberosToken
operator|.
name|isGssV5ApReq
argument_list|()
condition|)
block|{
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
operator|new
name|QName
argument_list|(
name|kerberosTokenPolicy
operator|.
name|getVersion
argument_list|()
operator|.
name|getNamespace
argument_list|()
argument_list|,
literal|"WssGssKerberosV5ApReqToken11"
argument_list|)
argument_list|)
expr_stmt|;
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

