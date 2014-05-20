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

begin_class
specifier|public
specifier|final
class|class
name|Constants
block|{
specifier|public
specifier|static
specifier|final
name|String
name|JWT_BEARER_GRANT
init|=
literal|"urn:ietf:params:oauth:grant-type:jwt-bearer"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CLIENT_GRANT_ASSERTION_PARAM
init|=
literal|"assertion"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CLIENT_AUTH_ASSERTION_PARAM
init|=
literal|"client_assertion"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CLIENT_AUTH_ASSERTION_TYPE
init|=
literal|"client_assertion_type"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CLIENT_AUTH_JWT_BEARER
init|=
literal|"urn:ietf:params:oauth:client-assertion-type:jwt-bearer"
decl_stmt|;
specifier|private
name|Constants
parameter_list|()
block|{              }
block|}
end_class

end_unit

