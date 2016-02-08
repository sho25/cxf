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
name|rest
package|;
end_package

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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|DELETE
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|DefaultValue
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|GET
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|POST
import|;
end_import

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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|PathParam
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Produces
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|QueryParam
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
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
name|sts
operator|.
name|provider
operator|.
name|model
operator|.
name|RequestSecurityTokenType
import|;
end_import

begin_interface
annotation|@
name|Path
argument_list|(
literal|"/token"
argument_list|)
specifier|public
interface|interface
name|RESTSecurityTokenService
block|{
enum|enum
name|Action
block|{
name|issue
argument_list|(
literal|"issue"
argument_list|)
block|,
name|validate
argument_list|(
literal|"validate"
argument_list|)
block|,
name|renew
argument_list|(
literal|"renew"
argument_list|)
block|,
name|cancel
argument_list|(
literal|"cancel"
argument_list|)
block|;
specifier|private
name|String
name|value
decl_stmt|;
specifier|private
name|Action
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
comment|/**      * @return Issues required token type with default token settings.      */
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"{tokenType}"
argument_list|)
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_XML
block|,
name|MediaType
operator|.
name|APPLICATION_JSON
block|}
argument_list|)
name|Response
name|getToken
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"tokenType"
argument_list|)
name|String
name|tokenType
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"keyType"
argument_list|)
name|String
name|keyType
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"claim"
argument_list|)
name|List
argument_list|<
name|String
argument_list|>
name|requestedClaims
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"appliesTo"
argument_list|)
name|String
name|appliesTo
parameter_list|)
function_decl|;
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"ws-trust/{tokenType}"
argument_list|)
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_XML
block|,
name|MediaType
operator|.
name|APPLICATION_JSON
block|}
argument_list|)
name|Response
name|getTokenViaWSTrust
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"tokenType"
argument_list|)
name|String
name|tokenType
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"keyType"
argument_list|)
name|String
name|keyType
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"claim"
argument_list|)
name|List
argument_list|<
name|String
argument_list|>
name|requestedClaims
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"appliesTo"
argument_list|)
name|String
name|appliesTo
parameter_list|)
function_decl|;
annotation|@
name|POST
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_XML
block|,
name|MediaType
operator|.
name|APPLICATION_JSON
block|}
argument_list|)
name|Response
name|getToken
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"action"
argument_list|)
annotation|@
name|DefaultValue
argument_list|(
literal|"issue"
argument_list|)
name|Action
name|action
parameter_list|,
name|RequestSecurityTokenType
name|request
parameter_list|)
function_decl|;
comment|/**      * Same as {@link #getToken(Action, RequestSecurityTokenType)} with 'cancel' action.      *       * @param request      * @return      */
annotation|@
name|DELETE
annotation|@
name|Path
argument_list|(
literal|"/"
argument_list|)
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_XML
block|,
name|MediaType
operator|.
name|APPLICATION_JSON
block|}
argument_list|)
name|Response
name|removeToken
parameter_list|(
name|RequestSecurityTokenType
name|request
parameter_list|)
function_decl|;
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"KeyExchangeToken"
argument_list|)
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_XML
block|,
name|MediaType
operator|.
name|APPLICATION_JSON
block|}
argument_list|)
name|Response
name|getKeyExchangeToken
parameter_list|(
name|RequestSecurityTokenType
name|request
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

