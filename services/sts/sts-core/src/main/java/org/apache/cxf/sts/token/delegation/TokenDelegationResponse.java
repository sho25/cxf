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
name|Map
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

begin_comment
comment|/**  * This class encapsulates the response from a TokenDelegationHandler instance.  */
end_comment

begin_class
specifier|public
class|class
name|TokenDelegationResponse
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|additionalProperties
decl_stmt|;
specifier|private
name|ReceivedToken
name|token
decl_stmt|;
specifier|private
name|boolean
name|delegationAllowed
decl_stmt|;
specifier|public
name|ReceivedToken
name|getToken
parameter_list|()
block|{
return|return
name|token
return|;
block|}
specifier|public
name|void
name|setToken
parameter_list|(
name|ReceivedToken
name|token
parameter_list|)
block|{
name|this
operator|.
name|token
operator|=
name|token
expr_stmt|;
block|}
specifier|public
name|void
name|setAdditionalProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|additionalProperties
parameter_list|)
block|{
name|this
operator|.
name|additionalProperties
operator|=
name|additionalProperties
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getAdditionalProperties
parameter_list|()
block|{
return|return
name|additionalProperties
return|;
block|}
specifier|public
name|boolean
name|isDelegationAllowed
parameter_list|()
block|{
return|return
name|delegationAllowed
return|;
block|}
specifier|public
name|void
name|setDelegationAllowed
parameter_list|(
name|boolean
name|delegationAllowed
parameter_list|)
block|{
name|this
operator|.
name|delegationAllowed
operator|=
name|delegationAllowed
expr_stmt|;
block|}
block|}
end_class

end_unit

