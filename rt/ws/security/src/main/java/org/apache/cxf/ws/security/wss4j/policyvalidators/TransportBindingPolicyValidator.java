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
name|security
operator|.
name|transport
operator|.
name|TLSSessionInfo
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
name|TransportBinding
import|;
end_import

begin_comment
comment|/**  * Validate a TransportBinding policy.  */
end_comment

begin_class
specifier|public
class|class
name|TransportBindingPolicyValidator
extends|extends
name|AbstractBindingPolicyValidator
block|{
comment|/**      * Return true if this SecurityPolicyValidator implementation is capable of validating a      * policy defined by the AssertionInfo parameter      */
specifier|public
name|boolean
name|canValidatePolicy
parameter_list|(
name|AssertionInfo
name|assertionInfo
parameter_list|)
block|{
return|return
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
name|TRANSPORT_BINDING
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
name|TRANSPORT_BINDING
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
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|TransportBinding
name|binding
init|=
operator|(
name|TransportBinding
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
comment|// Check that TLS is in use if we are not the requestor
name|boolean
name|initiator
init|=
name|MessageUtils
operator|.
name|isRequestor
argument_list|(
name|parameters
operator|.
name|getMessage
argument_list|()
argument_list|)
decl_stmt|;
name|TLSSessionInfo
name|tlsInfo
init|=
name|parameters
operator|.
name|getMessage
argument_list|()
operator|.
name|get
argument_list|(
name|TLSSessionInfo
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|initiator
operator|&&
name|tlsInfo
operator|==
literal|null
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"TLS is not enabled"
argument_list|)
expr_stmt|;
continue|continue;
block|}
comment|// HttpsToken is validated by the HttpsTokenInterceptorProvider
if|if
condition|(
name|binding
operator|.
name|getTransportToken
argument_list|()
operator|!=
literal|null
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
name|binding
operator|.
name|getTransportToken
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Check the IncludeTimestamp
if|if
condition|(
operator|!
name|validateTimestamp
argument_list|(
name|binding
operator|.
name|isIncludeTimestamp
argument_list|()
argument_list|,
literal|true
argument_list|,
name|parameters
operator|.
name|getResults
argument_list|()
argument_list|,
name|parameters
operator|.
name|getSignedResults
argument_list|()
argument_list|,
name|parameters
operator|.
name|getMessage
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|error
init|=
literal|"Received Timestamp does not match the requirements"
decl_stmt|;
name|ai
operator|.
name|setNotAsserted
argument_list|(
name|error
argument_list|)
expr_stmt|;
continue|continue;
block|}
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
name|binding
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|SPConstants
operator|.
name|INCLUDE_TIMESTAMP
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// We don't need to check these policies for the Transport binding
if|if
condition|(
operator|!
name|ais
operator|.
name|isEmpty
argument_list|()
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
name|SP12Constants
operator|.
name|ENCRYPTED_PARTS
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
name|SP11Constants
operator|.
name|ENCRYPTED_PARTS
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
name|SP12Constants
operator|.
name|SIGNED_PARTS
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
name|SP11Constants
operator|.
name|SIGNED_PARTS
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

