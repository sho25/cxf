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
name|oauth2
operator|.
name|services
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Path
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
name|rs
operator|.
name|security
operator|.
name|oauth2
operator|.
name|utils
operator|.
name|OAuthConstants
import|;
end_import

begin_comment
comment|/**  * Redirection-based Implicit Grant Service  *   * This resource handles the End User authorising  * or denying the Client embedded in the Web agent.  *   * We can consider having a single authorization service dealing with either  * authorization code or implicit grant.  */
end_comment

begin_class
annotation|@
name|Path
argument_list|(
literal|"/authorize-implicit"
argument_list|)
specifier|public
class|class
name|ImplicitGrantService
extends|extends
name|AbstractImplicitGrantService
block|{
specifier|public
name|ImplicitGrantService
parameter_list|()
block|{
name|super
argument_list|(
name|OAuthConstants
operator|.
name|TOKEN_RESPONSE_TYPE
argument_list|,
name|OAuthConstants
operator|.
name|IMPLICIT_GRANT
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

