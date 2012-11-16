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
name|common
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
comment|/**  * Captures the information associated with the access token request.  */
end_comment

begin_class
specifier|public
class|class
name|AccessTokenRegistration
block|{
specifier|private
name|Client
name|client
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|requestedScope
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|approvedScope
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|String
name|grantType
decl_stmt|;
specifier|private
name|UserSubject
name|subject
decl_stmt|;
comment|/**      * Sets the {@link Client} instance      * @param client the client      */
specifier|public
name|void
name|setClient
parameter_list|(
name|Client
name|client
parameter_list|)
block|{
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
block|}
comment|/**      * Returns the {@link Client} instance      * @return the client.      */
specifier|public
name|Client
name|getClient
parameter_list|()
block|{
return|return
name|client
return|;
block|}
comment|/**      * Sets the requested scope      * @param requestedScope the scope      */
specifier|public
name|void
name|setRequestedScope
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|requestedScope
parameter_list|)
block|{
name|this
operator|.
name|requestedScope
operator|=
name|requestedScope
expr_stmt|;
block|}
comment|/**      * Gets the requested scope      * @return the scope      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getRequestedScope
parameter_list|()
block|{
return|return
name|requestedScope
return|;
block|}
comment|/**      * Sets the scope explicitly approved by the end user      * @param approvedScope the approved scope      */
specifier|public
name|void
name|setApprovedScope
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|approvedScope
parameter_list|)
block|{
name|this
operator|.
name|approvedScope
operator|=
name|approvedScope
expr_stmt|;
block|}
comment|/**      * Gets the scope explicitly approved by the end user      * @return the scope      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getApprovedScope
parameter_list|()
block|{
return|return
name|approvedScope
return|;
block|}
comment|/**      * Sets the {@link UserSubject) instance capturing       * the information about the end user       * @param subject the end user subject      */
specifier|public
name|void
name|setSubject
parameter_list|(
name|UserSubject
name|subject
parameter_list|)
block|{
name|this
operator|.
name|subject
operator|=
name|subject
expr_stmt|;
block|}
comment|/**      * Gets the {@link UserSubject) instance capturing       * the information about the end user      * @return the subject      */
specifier|public
name|UserSubject
name|getSubject
parameter_list|()
block|{
return|return
name|subject
return|;
block|}
comment|/**      * Sets the type of grant which is exchanged for this token      * @param grantType the grant type      */
specifier|public
name|void
name|setGrantType
parameter_list|(
name|String
name|grantType
parameter_list|)
block|{
name|this
operator|.
name|grantType
operator|=
name|grantType
expr_stmt|;
block|}
comment|/**      * Gets the type of grant which is exchanged for this token      * @return the grant type      */
specifier|public
name|String
name|getGrantType
parameter_list|()
block|{
return|return
name|grantType
return|;
block|}
block|}
end_class

end_unit

