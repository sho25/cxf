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
name|SecurityConstants
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
name|tokenstore
operator|.
name|TokenStoreUtils
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
name|common
operator|.
name|util
operator|.
name|KeyUtils
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
name|SP11Constants
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
name|SP12Constants
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xml
operator|.
name|security
operator|.
name|utils
operator|.
name|Base64
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
name|AbstractSecurityPolicyValidator
block|{
comment|/**      * Return true if this SecurityPolicyValidator implementation is capable of validating a       * policy defined by the AssertionInfo parameter      */
specifier|public
name|boolean
name|canValidatePolicy
parameter_list|(
name|AssertionInfo
name|assertionInfo
parameter_list|)
block|{
if|if
condition|(
name|assertionInfo
operator|.
name|getAssertion
argument_list|()
operator|!=
literal|null
operator|&&
operator|(
name|SP12Constants
operator|.
name|KERBEROS_TOKEN
operator|.
name|equals
argument_list|(
name|assertionInfo
operator|.
name|getAssertion
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|||
name|SP11Constants
operator|.
name|KERBEROS_TOKEN
operator|.
name|equals
argument_list|(
name|assertionInfo
operator|.
name|getAssertion
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
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
comment|/**      * Validate policies.      */
specifier|public
name|void
name|validatePolicies
parameter_list|(
name|PolicyValidatorParameters
name|parameters
parameter_list|,
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
parameter_list|)
block|{
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|kerberosResults
init|=
name|findKerberosResults
argument_list|(
name|parameters
operator|.
name|getResults
argument_list|()
operator|.
name|getResults
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|WSSecurityEngineResult
name|kerberosResult
range|:
name|kerberosResults
control|)
block|{
name|KerberosSecurity
name|kerberosToken
init|=
operator|(
name|KerberosSecurity
operator|)
name|kerberosResult
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_BINARY_SECURITY_TOKEN
argument_list|)
decl_stmt|;
name|boolean
name|asserted
init|=
literal|true
decl_stmt|;
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
name|assertToken
argument_list|(
name|kerberosTokenPolicy
argument_list|,
name|parameters
operator|.
name|getAssertionInfoMap
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isTokenRequired
argument_list|(
name|kerberosTokenPolicy
argument_list|,
name|parameters
operator|.
name|getMessage
argument_list|()
argument_list|)
condition|)
block|{
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|parameters
operator|.
name|getAssertionInfoMap
argument_list|()
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
name|parameters
operator|.
name|getAssertionInfoMap
argument_list|()
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
name|parameters
operator|.
name|getAssertionInfoMap
argument_list|()
argument_list|,
name|kerberosTokenPolicy
argument_list|,
name|kerberosToken
argument_list|)
condition|)
block|{
name|asserted
operator|=
literal|false
expr_stmt|;
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
if|if
condition|(
name|asserted
condition|)
block|{
name|SecurityToken
name|token
init|=
name|createSecurityToken
argument_list|(
name|kerberosToken
argument_list|)
decl_stmt|;
name|token
operator|.
name|setSecret
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|kerberosResult
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_SECRET
argument_list|)
argument_list|)
expr_stmt|;
name|TokenStoreUtils
operator|.
name|getTokenStore
argument_list|(
name|parameters
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|add
argument_list|(
name|token
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|getMessage
argument_list|()
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN_ID
argument_list|,
name|token
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
specifier|private
name|void
name|assertToken
parameter_list|(
name|KerberosToken
name|token
parameter_list|,
name|AssertionInfoMap
name|aim
parameter_list|)
block|{
name|String
name|namespace
init|=
name|token
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
if|if
condition|(
name|token
operator|.
name|isRequireKeyIdentifierReference
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
name|namespace
argument_list|,
name|SPConstants
operator|.
name|REQUIRE_KEY_IDENTIFIER_REFERENCE
argument_list|)
argument_list|)
expr_stmt|;
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
specifier|private
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|findKerberosResults
parameter_list|(
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|wsSecEngineResults
parameter_list|)
block|{
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|results
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|WSSecurityEngineResult
name|wser
range|:
name|wsSecEngineResults
control|)
block|{
name|Integer
name|actInt
init|=
operator|(
name|Integer
operator|)
name|wser
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
name|actInt
operator|.
name|intValue
argument_list|()
operator|==
name|WSConstants
operator|.
name|BST
condition|)
block|{
name|BinarySecurity
name|binarySecurity
init|=
operator|(
name|BinarySecurity
operator|)
name|wser
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
name|binarySecurity
operator|instanceof
name|KerberosSecurity
condition|)
block|{
name|results
operator|.
name|add
argument_list|(
name|wser
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|results
return|;
block|}
specifier|private
name|SecurityToken
name|createSecurityToken
parameter_list|(
name|KerberosSecurity
name|binarySecurityToken
parameter_list|)
block|{
name|SecurityToken
name|token
init|=
operator|new
name|SecurityToken
argument_list|(
name|binarySecurityToken
operator|.
name|getID
argument_list|()
argument_list|)
decl_stmt|;
name|token
operator|.
name|setToken
argument_list|(
name|binarySecurityToken
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
name|token
operator|.
name|setTokenType
argument_list|(
name|binarySecurityToken
operator|.
name|getValueType
argument_list|()
argument_list|)
expr_stmt|;
name|byte
index|[]
name|tokenBytes
init|=
name|binarySecurityToken
operator|.
name|getToken
argument_list|()
decl_stmt|;
try|try
block|{
name|token
operator|.
name|setSHA1
argument_list|(
name|Base64
operator|.
name|encode
argument_list|(
name|KeyUtils
operator|.
name|generateDigest
argument_list|(
name|tokenBytes
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSSecurityException
name|e
parameter_list|)
block|{
comment|// Just consume this for now as it isn't critical...
block|}
return|return
name|token
return|;
block|}
block|}
end_class

end_unit

