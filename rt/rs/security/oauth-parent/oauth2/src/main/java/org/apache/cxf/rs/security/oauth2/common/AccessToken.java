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
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
import|;
end_import

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
name|javax
operator|.
name|persistence
operator|.
name|ElementCollection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|FetchType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Id
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|MapKeyColumn
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|MappedSuperclass
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Transient
import|;
end_import

begin_comment
comment|/**  * Base Access Token representation  */
end_comment

begin_class
annotation|@
name|MappedSuperclass
specifier|public
specifier|abstract
class|class
name|AccessToken
implements|implements
name|Serializable
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|5750544301887053480L
decl_stmt|;
specifier|private
name|String
name|tokenKey
decl_stmt|;
specifier|private
name|String
name|tokenType
decl_stmt|;
specifier|private
name|String
name|refreshToken
decl_stmt|;
specifier|private
name|long
name|expiresIn
init|=
operator|-
literal|1
decl_stmt|;
specifier|private
name|long
name|issuedAt
init|=
operator|-
literal|1
decl_stmt|;
specifier|private
name|long
name|notBefore
init|=
operator|-
literal|1
decl_stmt|;
specifier|private
name|String
name|issuer
decl_stmt|;
specifier|private
name|String
name|encodedToken
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parameters
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|protected
name|AccessToken
parameter_list|()
block|{      }
specifier|protected
name|AccessToken
parameter_list|(
name|String
name|tokenType
parameter_list|,
name|String
name|tokenKey
parameter_list|)
block|{
name|this
operator|.
name|tokenType
operator|=
name|tokenType
expr_stmt|;
name|this
operator|.
name|tokenKey
operator|=
name|tokenKey
expr_stmt|;
block|}
specifier|protected
name|AccessToken
parameter_list|(
name|String
name|tokenType
parameter_list|,
name|String
name|tokenKey
parameter_list|,
name|long
name|expiresIn
parameter_list|,
name|long
name|issuedAt
parameter_list|)
block|{
name|this
argument_list|(
name|tokenType
argument_list|,
name|tokenKey
argument_list|)
expr_stmt|;
name|this
operator|.
name|expiresIn
operator|=
name|expiresIn
expr_stmt|;
name|this
operator|.
name|issuedAt
operator|=
name|issuedAt
expr_stmt|;
block|}
specifier|protected
name|AccessToken
parameter_list|(
name|String
name|tokenType
parameter_list|,
name|String
name|tokenKey
parameter_list|,
name|long
name|expiresIn
parameter_list|,
name|long
name|issuedAt
parameter_list|,
name|String
name|refreshToken
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parameters
parameter_list|)
block|{
name|this
argument_list|(
name|tokenType
argument_list|,
name|tokenKey
argument_list|,
name|expiresIn
argument_list|,
name|issuedAt
argument_list|)
expr_stmt|;
name|this
operator|.
name|refreshToken
operator|=
name|refreshToken
expr_stmt|;
name|this
operator|.
name|parameters
operator|=
name|parameters
expr_stmt|;
block|}
comment|/**      * Returns the token type such as bearer, mac, etc      * @return the type      */
specifier|public
name|String
name|getTokenType
parameter_list|()
block|{
return|return
name|tokenType
return|;
block|}
specifier|public
name|void
name|setTokenType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|tokenType
operator|=
name|type
expr_stmt|;
block|}
comment|/**      * Returns the token key      * @return the key      */
annotation|@
name|Id
specifier|public
name|String
name|getTokenKey
parameter_list|()
block|{
return|return
name|tokenKey
return|;
block|}
specifier|public
name|void
name|setTokenKey
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|this
operator|.
name|tokenKey
operator|=
name|key
expr_stmt|;
block|}
comment|/**      * Sets the refresh token key the client can use to obtain a new      * access token      * @param refreshToken the refresh token      */
specifier|public
name|void
name|setRefreshToken
parameter_list|(
name|String
name|refreshToken
parameter_list|)
block|{
name|this
operator|.
name|refreshToken
operator|=
name|refreshToken
expr_stmt|;
block|}
comment|/**      * Gets the refresh token key the client can use to obtain a new      * access token      * @return the refresh token      */
specifier|public
name|String
name|getRefreshToken
parameter_list|()
block|{
return|return
name|refreshToken
return|;
block|}
comment|/**      * Gets token parameters      * @return      */
annotation|@
name|ElementCollection
argument_list|(
name|fetch
operator|=
name|FetchType
operator|.
name|EAGER
argument_list|)
annotation|@
name|MapKeyColumn
argument_list|(
name|name
operator|=
literal|"propName"
argument_list|)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getParameters
parameter_list|()
block|{
return|return
name|parameters
return|;
block|}
comment|/**      * The token lifetime      * @return the lifetime, -1 means no 'expires_in' parameter was returned      */
specifier|public
name|long
name|getExpiresIn
parameter_list|()
block|{
return|return
name|expiresIn
return|;
block|}
specifier|public
name|void
name|setExpiresIn
parameter_list|(
name|long
name|expiresIn
parameter_list|)
block|{
name|this
operator|.
name|expiresIn
operator|=
name|expiresIn
expr_stmt|;
block|}
specifier|public
name|long
name|getIssuedAt
parameter_list|()
block|{
return|return
name|issuedAt
return|;
block|}
specifier|public
name|void
name|setIssuedAt
parameter_list|(
name|long
name|issuedAt
parameter_list|)
block|{
name|this
operator|.
name|issuedAt
operator|=
name|issuedAt
expr_stmt|;
block|}
comment|/**      * Sets additional token parameters      * @param parameters the token parameters      */
specifier|public
name|void
name|setParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parameters
parameter_list|)
block|{
name|this
operator|.
name|parameters
operator|=
name|parameters
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
annotation|@
name|Transient
specifier|public
name|String
name|getEncodedToken
parameter_list|()
block|{
return|return
name|encodedToken
return|;
block|}
specifier|public
name|void
name|setEncodedToken
parameter_list|(
name|String
name|encodedToken
parameter_list|)
block|{
name|this
operator|.
name|encodedToken
operator|=
name|encodedToken
expr_stmt|;
block|}
comment|/**      * @return the Not Before" timestamp, -1 means no 'nbf' parameter was returned      */
specifier|public
name|long
name|getNotBefore
parameter_list|()
block|{
return|return
name|notBefore
return|;
block|}
specifier|public
name|void
name|setNotBefore
parameter_list|(
name|long
name|notBefore
parameter_list|)
block|{
name|this
operator|.
name|notBefore
operator|=
name|notBefore
expr_stmt|;
block|}
block|}
end_class

end_unit

