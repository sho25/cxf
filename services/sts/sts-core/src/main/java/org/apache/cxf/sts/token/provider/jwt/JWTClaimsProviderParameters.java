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
name|provider
operator|.
name|jwt
package|;
end_package

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
name|token
operator|.
name|provider
operator|.
name|TokenProviderParameters
import|;
end_import

begin_comment
comment|/**  * The parameters that are passed through to a JWTClaimsProvider implementation to create a   * JWTClaims Object.  */
end_comment

begin_class
specifier|public
class|class
name|JWTClaimsProviderParameters
block|{
specifier|private
name|TokenProviderParameters
name|providerParameters
decl_stmt|;
specifier|public
name|TokenProviderParameters
name|getProviderParameters
parameter_list|()
block|{
return|return
name|providerParameters
return|;
block|}
specifier|public
name|void
name|setProviderParameters
parameter_list|(
name|TokenProviderParameters
name|providerParameters
parameter_list|)
block|{
name|this
operator|.
name|providerParameters
operator|=
name|providerParameters
expr_stmt|;
block|}
block|}
end_class

end_unit

