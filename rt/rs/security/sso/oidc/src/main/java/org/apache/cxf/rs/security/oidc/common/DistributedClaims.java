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
name|rs
operator|.
name|security
operator|.
name|oidc
operator|.
name|common
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

begin_class
specifier|public
class|class
name|DistributedClaims
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|claimNames
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|DistributedClaimSource
argument_list|>
name|claimSources
decl_stmt|;
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getClaimNames
parameter_list|()
block|{
return|return
name|claimNames
return|;
block|}
specifier|public
name|void
name|setClaimNames
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|claimNames
parameter_list|)
block|{
name|this
operator|.
name|claimNames
operator|=
name|claimNames
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|DistributedClaimSource
argument_list|>
name|getClaimSources
parameter_list|()
block|{
return|return
name|claimSources
return|;
block|}
specifier|public
name|void
name|setClaimSources
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|DistributedClaimSource
argument_list|>
name|claimSources
parameter_list|)
block|{
name|this
operator|.
name|claimSources
operator|=
name|claimSources
expr_stmt|;
block|}
block|}
end_class

end_unit

