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
name|claims
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Principal
import|;
end_import

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
name|List
import|;
end_import

begin_comment
comment|/**  * This class holds various ClaimsHandler implementations.  */
end_comment

begin_class
specifier|public
class|class
name|ClaimsManager
block|{
specifier|private
name|List
argument_list|<
name|ClaimsHandler
argument_list|>
name|claimHandlers
decl_stmt|;
specifier|private
name|List
argument_list|<
name|URI
argument_list|>
name|supportedClaimTypes
init|=
operator|new
name|ArrayList
argument_list|<
name|URI
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|List
argument_list|<
name|URI
argument_list|>
name|getSupportedClaimTypes
parameter_list|()
block|{
return|return
name|supportedClaimTypes
return|;
block|}
specifier|public
name|List
argument_list|<
name|ClaimsHandler
argument_list|>
name|getClaimHandlers
parameter_list|()
block|{
return|return
name|claimHandlers
return|;
block|}
specifier|public
name|void
name|setClaimHandlers
parameter_list|(
name|List
argument_list|<
name|ClaimsHandler
argument_list|>
name|claimHandlers
parameter_list|)
block|{
name|this
operator|.
name|claimHandlers
operator|=
name|claimHandlers
expr_stmt|;
if|if
condition|(
name|claimHandlers
operator|==
literal|null
condition|)
block|{
name|supportedClaimTypes
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
else|else
block|{
for|for
control|(
name|ClaimsHandler
name|handler
range|:
name|claimHandlers
control|)
block|{
name|supportedClaimTypes
operator|.
name|addAll
argument_list|(
name|handler
operator|.
name|getSupportedClaimTypes
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|ClaimCollection
name|retrieveClaimValues
parameter_list|(
name|Principal
name|principal
parameter_list|,
name|RequestClaimCollection
name|claims
parameter_list|)
block|{
if|if
condition|(
name|claimHandlers
operator|!=
literal|null
operator|&&
name|claimHandlers
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|ClaimCollection
name|returnCollection
init|=
operator|new
name|ClaimCollection
argument_list|()
decl_stmt|;
for|for
control|(
name|ClaimsHandler
name|handler
range|:
name|claimHandlers
control|)
block|{
name|ClaimCollection
name|claimCollection
init|=
name|handler
operator|.
name|retrieveClaimValues
argument_list|(
name|principal
argument_list|,
name|claims
argument_list|)
decl_stmt|;
if|if
condition|(
name|claimCollection
operator|!=
literal|null
operator|&&
name|claimCollection
operator|.
name|size
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|returnCollection
operator|.
name|addAll
argument_list|(
name|claimCollection
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|returnCollection
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

