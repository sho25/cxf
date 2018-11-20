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
name|trust
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
name|dom
operator|.
name|handler
operator|.
name|RequestData
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
name|validate
operator|.
name|Credential
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
name|validate
operator|.
name|SamlAssertionValidator
import|;
end_import

begin_comment
comment|/**  * This class validates a SAML Assertion by invoking the SamlAssertionValidator in WSS4J. It  * overrides the signature verification, so that if the signature is not trusted, it just sets  * a boolean. The STSTokenValidator can parse this tag and dispatch the Assertion to the STS  * for validation.  */
end_comment

begin_class
specifier|public
class|class
name|STSSamlAssertionValidator
extends|extends
name|SamlAssertionValidator
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
name|STSSamlAssertionValidator
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|boolean
name|trustVerificationSucceeded
decl_stmt|;
comment|/**      * Try to verify trust on the assertion. If it fails, then set a boolean and return.      * @param assertion The signed Assertion      * @param data The RequestData context      * @return A Credential instance      * @throws WSSecurityException      */
annotation|@
name|Override
specifier|protected
name|Credential
name|verifySignedAssertion
parameter_list|(
name|SamlAssertionWrapper
name|assertion
parameter_list|,
name|RequestData
name|data
parameter_list|)
throws|throws
name|WSSecurityException
block|{
try|try
block|{
name|Credential
name|credential
init|=
name|super
operator|.
name|verifySignedAssertion
argument_list|(
name|assertion
argument_list|,
name|data
argument_list|)
decl_stmt|;
name|trustVerificationSucceeded
operator|=
literal|true
expr_stmt|;
return|return
name|credential
return|;
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
literal|"Local trust verification of SAML assertion failed: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex
argument_list|)
expr_stmt|;
name|trustVerificationSucceeded
operator|=
literal|false
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Return if trust verification on the signature of the assertion succeeded.      * @return if trust verification on the signature of the assertion succeeded      */
specifier|public
name|boolean
name|isTrustVerificationSucceeded
parameter_list|()
block|{
return|return
name|trustVerificationSucceeded
return|;
block|}
block|}
end_class

end_unit

