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
name|jwt
operator|.
name|grant
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
name|core
operator|.
name|MultivaluedMap
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

begin_class
specifier|public
class|class
name|JwtBearerClientCredentialsGrant
extends|extends
name|AbstractJwtBearerGrant
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|4801583498206813025L
decl_stmt|;
specifier|public
name|JwtBearerClientCredentialsGrant
parameter_list|(
name|String
name|assertion
parameter_list|,
name|boolean
name|encoded
parameter_list|,
name|String
name|scope
parameter_list|)
block|{
name|super
argument_list|(
name|OAuthConstants
operator|.
name|CLIENT_CREDENTIALS_GRANT
argument_list|,
name|assertion
argument_list|,
name|encoded
argument_list|,
name|scope
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|toMap
parameter_list|()
block|{
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|initMap
argument_list|()
decl_stmt|;
name|map
operator|.
name|putSingle
argument_list|(
name|Constants
operator|.
name|CLIENT_AUTH_ASSERTION_TYPE
argument_list|,
name|Constants
operator|.
name|CLIENT_AUTH_JWT_BEARER
argument_list|)
expr_stmt|;
name|map
operator|.
name|putSingle
argument_list|(
name|Constants
operator|.
name|CLIENT_AUTH_ASSERTION_PARAM
argument_list|,
name|encodeAssertion
argument_list|()
argument_list|)
expr_stmt|;
name|addScope
argument_list|(
name|map
argument_list|)
expr_stmt|;
return|return
name|map
return|;
block|}
block|}
end_class

end_unit

