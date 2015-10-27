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
name|dom
operator|.
name|WSDataRef
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
name|engine
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
name|handler
operator|.
name|WSHandlerResult
import|;
end_import

begin_comment
comment|/**  * Holds various parameters to the policy validators  */
end_comment

begin_class
specifier|public
class|class
name|PolicyValidatorParameters
block|{
specifier|private
name|AssertionInfoMap
name|assertionInfoMap
decl_stmt|;
specifier|private
name|Message
name|message
decl_stmt|;
specifier|private
name|Element
name|soapBody
decl_stmt|;
specifier|private
name|Element
name|soapHeader
decl_stmt|;
specifier|private
name|WSHandlerResult
name|results
decl_stmt|;
specifier|private
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|signedResults
decl_stmt|;
specifier|private
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|encryptedResults
decl_stmt|;
specifier|private
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|usernameTokenResults
decl_stmt|;
specifier|private
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|samlResults
decl_stmt|;
specifier|private
name|Element
name|timestampElement
decl_stmt|;
specifier|private
name|boolean
name|utWithCallbacks
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|WSDataRef
argument_list|>
name|signed
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|WSDataRef
argument_list|>
name|encrypted
decl_stmt|;
specifier|public
name|Message
name|getMessage
parameter_list|()
block|{
return|return
name|message
return|;
block|}
specifier|public
name|void
name|setMessage
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
name|Element
name|getSoapBody
parameter_list|()
block|{
return|return
name|soapBody
return|;
block|}
specifier|public
name|void
name|setSoapBody
parameter_list|(
name|Element
name|soapBody
parameter_list|)
block|{
name|this
operator|.
name|soapBody
operator|=
name|soapBody
expr_stmt|;
block|}
specifier|public
name|WSHandlerResult
name|getResults
parameter_list|()
block|{
return|return
name|results
return|;
block|}
specifier|public
name|void
name|setResults
parameter_list|(
name|WSHandlerResult
name|results
parameter_list|)
block|{
name|this
operator|.
name|results
operator|=
name|results
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|getSignedResults
parameter_list|()
block|{
return|return
name|signedResults
return|;
block|}
specifier|public
name|void
name|setSignedResults
parameter_list|(
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|signedResults
parameter_list|)
block|{
name|this
operator|.
name|signedResults
operator|=
name|signedResults
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|getEncryptedResults
parameter_list|()
block|{
return|return
name|encryptedResults
return|;
block|}
specifier|public
name|void
name|setEncryptedResults
parameter_list|(
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|encryptedResults
parameter_list|)
block|{
name|this
operator|.
name|encryptedResults
operator|=
name|encryptedResults
expr_stmt|;
block|}
specifier|public
name|AssertionInfoMap
name|getAssertionInfoMap
parameter_list|()
block|{
return|return
name|assertionInfoMap
return|;
block|}
specifier|public
name|void
name|setAssertionInfoMap
parameter_list|(
name|AssertionInfoMap
name|assertionInfoMap
parameter_list|)
block|{
name|this
operator|.
name|assertionInfoMap
operator|=
name|assertionInfoMap
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|getUsernameTokenResults
parameter_list|()
block|{
return|return
name|usernameTokenResults
return|;
block|}
specifier|public
name|void
name|setUsernameTokenResults
parameter_list|(
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|usernameTokenResults
parameter_list|)
block|{
name|this
operator|.
name|usernameTokenResults
operator|=
name|usernameTokenResults
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|getSamlResults
parameter_list|()
block|{
return|return
name|samlResults
return|;
block|}
specifier|public
name|void
name|setSamlResults
parameter_list|(
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|samlResults
parameter_list|)
block|{
name|this
operator|.
name|samlResults
operator|=
name|samlResults
expr_stmt|;
block|}
specifier|public
name|Element
name|getTimestampElement
parameter_list|()
block|{
return|return
name|timestampElement
return|;
block|}
specifier|public
name|void
name|setTimestampElement
parameter_list|(
name|Element
name|timestampElement
parameter_list|)
block|{
name|this
operator|.
name|timestampElement
operator|=
name|timestampElement
expr_stmt|;
block|}
specifier|public
name|boolean
name|isUtWithCallbacks
parameter_list|()
block|{
return|return
name|utWithCallbacks
return|;
block|}
specifier|public
name|void
name|setUtWithCallbacks
parameter_list|(
name|boolean
name|utWithCallbacks
parameter_list|)
block|{
name|this
operator|.
name|utWithCallbacks
operator|=
name|utWithCallbacks
expr_stmt|;
block|}
specifier|public
name|Element
name|getSoapHeader
parameter_list|()
block|{
return|return
name|soapHeader
return|;
block|}
specifier|public
name|void
name|setSoapHeader
parameter_list|(
name|Element
name|soapHeader
parameter_list|)
block|{
name|this
operator|.
name|soapHeader
operator|=
name|soapHeader
expr_stmt|;
block|}
specifier|public
name|Collection
argument_list|<
name|WSDataRef
argument_list|>
name|getSigned
parameter_list|()
block|{
return|return
name|signed
return|;
block|}
specifier|public
name|void
name|setSigned
parameter_list|(
name|Collection
argument_list|<
name|WSDataRef
argument_list|>
name|signed
parameter_list|)
block|{
name|this
operator|.
name|signed
operator|=
name|signed
expr_stmt|;
block|}
specifier|public
name|Collection
argument_list|<
name|WSDataRef
argument_list|>
name|getEncrypted
parameter_list|()
block|{
return|return
name|encrypted
return|;
block|}
specifier|public
name|void
name|setEncrypted
parameter_list|(
name|Collection
argument_list|<
name|WSDataRef
argument_list|>
name|encrypted
parameter_list|)
block|{
name|this
operator|.
name|encrypted
operator|=
name|encrypted
expr_stmt|;
block|}
block|}
end_class

end_unit

