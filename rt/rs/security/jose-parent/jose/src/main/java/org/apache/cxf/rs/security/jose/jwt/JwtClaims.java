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
name|jose
operator|.
name|jwt
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|util
operator|.
name|StringUtils
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
name|helpers
operator|.
name|CastUtils
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
name|jaxrs
operator|.
name|json
operator|.
name|basic
operator|.
name|JsonMapObject
import|;
end_import

begin_class
specifier|public
class|class
name|JwtClaims
extends|extends
name|JsonMapObject
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|6274136637301800283L
decl_stmt|;
specifier|public
name|JwtClaims
parameter_list|()
block|{     }
specifier|public
name|JwtClaims
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|values
parameter_list|)
block|{
name|super
argument_list|(
name|values
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setIssuer
parameter_list|(
name|String
name|issuer
parameter_list|)
block|{
name|setClaim
argument_list|(
name|JwtConstants
operator|.
name|CLAIM_ISSUER
argument_list|,
name|issuer
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getIssuer
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|getClaim
argument_list|(
name|JwtConstants
operator|.
name|CLAIM_ISSUER
argument_list|)
return|;
block|}
specifier|public
name|void
name|setSubject
parameter_list|(
name|String
name|subject
parameter_list|)
block|{
name|setClaim
argument_list|(
name|JwtConstants
operator|.
name|CLAIM_SUBJECT
argument_list|,
name|subject
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getSubject
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|getClaim
argument_list|(
name|JwtConstants
operator|.
name|CLAIM_SUBJECT
argument_list|)
return|;
block|}
comment|/**      * Set a single audience value which will be serialized as a String      * @param audience the audience      */
specifier|public
name|void
name|setAudience
parameter_list|(
name|String
name|audience
parameter_list|)
block|{
name|setClaim
argument_list|(
name|JwtConstants
operator|.
name|CLAIM_AUDIENCE
argument_list|,
name|audience
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get a single audience value. If the audience claim value is an array then the      * first value will be returned.      * @return the audience      */
specifier|public
name|String
name|getAudience
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|audiences
init|=
name|getAudiences
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|audiences
argument_list|)
condition|)
block|{
return|return
name|audiences
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Set an array of audiences      * @param audiences the audiences array      */
specifier|public
name|void
name|setAudiences
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|audiences
parameter_list|)
block|{
name|setClaim
argument_list|(
name|JwtConstants
operator|.
name|CLAIM_AUDIENCE
argument_list|,
name|audiences
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get an array of audiences      * @return the audiences array      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getAudiences
parameter_list|()
block|{
name|Object
name|audiences
init|=
name|getClaim
argument_list|(
name|JwtConstants
operator|.
name|CLAIM_AUDIENCE
argument_list|)
decl_stmt|;
if|if
condition|(
name|audiences
operator|instanceof
name|List
argument_list|<
name|?
argument_list|>
condition|)
block|{
return|return
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|audiences
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|audiences
operator|instanceof
name|String
condition|)
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
operator|(
name|String
operator|)
name|audiences
argument_list|)
return|;
block|}
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
specifier|public
name|void
name|setExpiryTime
parameter_list|(
name|Long
name|expiresIn
parameter_list|)
block|{
name|setClaim
argument_list|(
name|JwtConstants
operator|.
name|CLAIM_EXPIRY
argument_list|,
name|expiresIn
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Long
name|getExpiryTime
parameter_list|()
block|{
return|return
name|getLongProperty
argument_list|(
name|JwtConstants
operator|.
name|CLAIM_EXPIRY
argument_list|)
return|;
block|}
specifier|public
name|void
name|setNotBefore
parameter_list|(
name|Long
name|notBefore
parameter_list|)
block|{
name|setClaim
argument_list|(
name|JwtConstants
operator|.
name|CLAIM_NOT_BEFORE
argument_list|,
name|notBefore
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Long
name|getNotBefore
parameter_list|()
block|{
return|return
name|getLongProperty
argument_list|(
name|JwtConstants
operator|.
name|CLAIM_NOT_BEFORE
argument_list|)
return|;
block|}
specifier|public
name|void
name|setIssuedAt
parameter_list|(
name|Long
name|issuedAt
parameter_list|)
block|{
name|setClaim
argument_list|(
name|JwtConstants
operator|.
name|CLAIM_ISSUED_AT
argument_list|,
name|issuedAt
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Long
name|getIssuedAt
parameter_list|()
block|{
return|return
name|getLongProperty
argument_list|(
name|JwtConstants
operator|.
name|CLAIM_ISSUED_AT
argument_list|)
return|;
block|}
specifier|public
name|void
name|setTokenId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|setClaim
argument_list|(
name|JwtConstants
operator|.
name|CLAIM_JWT_ID
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getTokenId
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|getClaim
argument_list|(
name|JwtConstants
operator|.
name|CLAIM_JWT_ID
argument_list|)
return|;
block|}
specifier|public
name|JwtClaims
name|setClaim
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|setProperty
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Object
name|getClaim
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|getProperty
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
end_class

end_unit

