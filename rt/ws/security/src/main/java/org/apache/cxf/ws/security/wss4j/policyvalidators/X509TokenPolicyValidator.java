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
name|ArrayList
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|List
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
name|cxf
operator|.
name|ws
operator|.
name|security
operator|.
name|policy
operator|.
name|model
operator|.
name|X509Token
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
name|WSConstants
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
name|WSSecurityEngineResult
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
name|BinarySecurity
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
name|util
operator|.
name|WSSecurityUtil
import|;
end_import

begin_comment
comment|/**  * Validate an X509 Token policy.  */
end_comment

begin_class
specifier|public
class|class
name|X509TokenPolicyValidator
extends|extends
name|AbstractTokenPolicyValidator
implements|implements
name|TokenPolicyValidator
block|{
specifier|private
specifier|static
specifier|final
name|String
name|X509_V3_VALUETYPE
init|=
name|WSConstants
operator|.
name|X509TOKEN_NS
operator|+
literal|"#X509v3"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PKI_VALUETYPE
init|=
name|WSConstants
operator|.
name|X509TOKEN_NS
operator|+
literal|"#X509PKIPathv1"
decl_stmt|;
specifier|public
name|boolean
name|validatePolicy
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|Message
name|message
parameter_list|,
name|Element
name|soapBody
parameter_list|,
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|results
parameter_list|,
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|signedResults
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
name|SP12Constants
operator|.
name|X509_TOKEN
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|==
literal|null
operator|||
name|ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|bstResults
init|=
operator|new
name|ArrayList
argument_list|<
name|WSSecurityEngineResult
argument_list|>
argument_list|()
decl_stmt|;
name|WSSecurityUtil
operator|.
name|fetchAllActionResults
argument_list|(
name|results
argument_list|,
name|WSConstants
operator|.
name|BST
argument_list|,
name|bstResults
argument_list|)
expr_stmt|;
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|X509Token
name|x509TokenPolicy
init|=
operator|(
name|X509Token
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
name|x509TokenPolicy
argument_list|,
name|message
argument_list|)
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|bstResults
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"The received token does not match the token inclusion requirement"
argument_list|)
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
operator|!
name|checkTokenType
argument_list|(
name|x509TokenPolicy
operator|.
name|getTokenVersionAndType
argument_list|()
argument_list|,
name|bstResults
argument_list|)
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"An incorrect X.509 Token Type is detected"
argument_list|)
expr_stmt|;
continue|continue;
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/**      * Check that at least one received token matches the token type.      */
specifier|private
name|boolean
name|checkTokenType
parameter_list|(
name|String
name|requiredVersionAndType
parameter_list|,
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|bstResults
parameter_list|)
block|{
if|if
condition|(
name|bstResults
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|String
name|requiredType
init|=
name|X509_V3_VALUETYPE
decl_stmt|;
if|if
condition|(
name|SPConstants
operator|.
name|WSS_X509_PKI_PATH_V1_TOKEN10
operator|.
name|equals
argument_list|(
name|requiredType
argument_list|)
operator|||
name|SPConstants
operator|.
name|WSS_X509_PKI_PATH_V1_TOKEN11
operator|.
name|equals
argument_list|(
name|requiredType
argument_list|)
condition|)
block|{
name|requiredType
operator|=
name|PKI_VALUETYPE
expr_stmt|;
block|}
for|for
control|(
name|WSSecurityEngineResult
name|result
range|:
name|bstResults
control|)
block|{
name|BinarySecurity
name|binarySecurityToken
init|=
operator|(
name|BinarySecurity
operator|)
name|result
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_BINARY_SECURITY_TOKEN
argument_list|)
decl_stmt|;
if|if
condition|(
name|binarySecurityToken
operator|!=
literal|null
condition|)
block|{
name|String
name|type
init|=
name|binarySecurityToken
operator|.
name|getValueType
argument_list|()
decl_stmt|;
if|if
condition|(
name|requiredType
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

