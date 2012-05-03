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
name|io
operator|.
name|Serializable
import|;
end_import

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

begin_comment
comment|/**  * This represents a Claim that has been processed by a ClaimsHandler instance.  */
end_comment

begin_class
specifier|public
class|class
name|Claim
implements|implements
name|Serializable
block|{
comment|/**      *       */
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1856135937386473416L
decl_stmt|;
specifier|private
name|URI
name|claimType
decl_stmt|;
specifier|private
name|String
name|issuer
decl_stmt|;
specifier|private
name|String
name|originalIssuer
decl_stmt|;
specifier|private
specifier|transient
name|Principal
name|principal
decl_stmt|;
specifier|private
name|String
name|value
decl_stmt|;
specifier|private
name|URI
name|namespace
init|=
name|ClaimTypes
operator|.
name|URI_BASE
decl_stmt|;
specifier|public
name|URI
name|getNamespace
parameter_list|()
block|{
return|return
name|namespace
return|;
block|}
specifier|public
name|void
name|setNamespace
parameter_list|(
name|URI
name|namespace
parameter_list|)
block|{
name|this
operator|.
name|namespace
operator|=
name|namespace
expr_stmt|;
block|}
specifier|public
name|String
name|getIssuer
parameter_list|()
block|{
return|return
name|issuer
return|;
block|}
specifier|public
name|void
name|setIssuer
parameter_list|(
name|String
name|issuer
parameter_list|)
block|{
name|this
operator|.
name|issuer
operator|=
name|issuer
expr_stmt|;
block|}
specifier|public
name|String
name|getOriginalIssuer
parameter_list|()
block|{
return|return
name|originalIssuer
return|;
block|}
specifier|public
name|void
name|setOriginalIssuer
parameter_list|(
name|String
name|originalIssuer
parameter_list|)
block|{
name|this
operator|.
name|originalIssuer
operator|=
name|originalIssuer
expr_stmt|;
block|}
specifier|public
name|URI
name|getClaimType
parameter_list|()
block|{
return|return
name|claimType
return|;
block|}
specifier|public
name|void
name|setClaimType
parameter_list|(
name|URI
name|claimType
parameter_list|)
block|{
name|this
operator|.
name|claimType
operator|=
name|claimType
expr_stmt|;
block|}
specifier|public
name|Principal
name|getPrincipal
parameter_list|()
block|{
return|return
name|principal
return|;
block|}
specifier|public
name|void
name|setPrincipal
parameter_list|(
name|Principal
name|principal
parameter_list|)
block|{
name|this
operator|.
name|principal
operator|=
name|principal
expr_stmt|;
block|}
specifier|public
name|void
name|setValue
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|String
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
block|}
end_class

end_unit

