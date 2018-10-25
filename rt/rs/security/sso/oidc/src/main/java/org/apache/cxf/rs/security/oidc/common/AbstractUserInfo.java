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
name|Collections
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
name|rs
operator|.
name|security
operator|.
name|jose
operator|.
name|jwt
operator|.
name|JwtClaims
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
name|oidc
operator|.
name|utils
operator|.
name|OidcUtils
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractUserInfo
extends|extends
name|JwtClaims
block|{
specifier|public
specifier|static
specifier|final
name|String
name|NAME_CLAIM
init|=
literal|"name"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|GIVEN_NAME_CLAIM
init|=
literal|"given_name"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FAMILY_NAME_CLAIM
init|=
literal|"family_name"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MIDDLE_NAME_CLAIM
init|=
literal|"middle_name"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NICKNAME_CLAIM
init|=
literal|"nickname"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PREFERRED_USERNAME_CLAIM
init|=
literal|"preferred_username"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PROFILE_CLAIM
init|=
literal|"profile"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PICTURE_CLAIM
init|=
literal|"picture"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WEBSITE_CLAIM
init|=
literal|"website"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|EMAIL_CLAIM
init|=
literal|"email"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|EMAIL_VERIFIED_CLAIM
init|=
literal|"email_verified"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|GENDER_CLAIM
init|=
literal|"gender"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ZONEINFO_CLAIM
init|=
literal|"zoneinfo"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|LOCALE_CLAIM
init|=
literal|"locale"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|BIRTHDATE_CLAIM
init|=
literal|"birthdate"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PHONE_CLAIM
init|=
literal|"phone_number"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PHONE_VERIFIED_CLAIM
init|=
literal|"phone_number_verified"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ADDRESS_CLAIM
init|=
literal|"address"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|UPDATED_AT_CLAIM
init|=
literal|"updated_at"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|4554501320190745304L
decl_stmt|;
specifier|public
name|AbstractUserInfo
parameter_list|()
block|{     }
specifier|public
name|AbstractUserInfo
parameter_list|(
name|JwtClaims
name|claims
parameter_list|)
block|{
name|this
argument_list|(
name|claims
operator|.
name|asMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|AbstractUserInfo
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|claims
parameter_list|)
block|{
name|super
argument_list|(
name|claims
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|setProperty
argument_list|(
name|NAME_CLAIM
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|getProperty
argument_list|(
name|NAME_CLAIM
argument_list|)
return|;
block|}
specifier|public
name|void
name|setGivenName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|setProperty
argument_list|(
name|GIVEN_NAME_CLAIM
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getGivenName
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|getProperty
argument_list|(
name|GIVEN_NAME_CLAIM
argument_list|)
return|;
block|}
specifier|public
name|void
name|setFamilyName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|setProperty
argument_list|(
name|FAMILY_NAME_CLAIM
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getFamilyName
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|getProperty
argument_list|(
name|FAMILY_NAME_CLAIM
argument_list|)
return|;
block|}
specifier|public
name|void
name|setMiddleName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|setProperty
argument_list|(
name|MIDDLE_NAME_CLAIM
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getMiddleName
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|getProperty
argument_list|(
name|MIDDLE_NAME_CLAIM
argument_list|)
return|;
block|}
specifier|public
name|void
name|setNickName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|setProperty
argument_list|(
name|NICKNAME_CLAIM
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getNickName
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|getProperty
argument_list|(
name|NICKNAME_CLAIM
argument_list|)
return|;
block|}
specifier|public
name|void
name|setPreferredUserName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|setProperty
argument_list|(
name|PREFERRED_USERNAME_CLAIM
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getPreferredUserName
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|getProperty
argument_list|(
name|PREFERRED_USERNAME_CLAIM
argument_list|)
return|;
block|}
specifier|public
name|void
name|setProfile
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|setProperty
argument_list|(
name|PROFILE_CLAIM
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getProfile
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|getProperty
argument_list|(
name|PROFILE_CLAIM
argument_list|)
return|;
block|}
specifier|public
name|void
name|setPicture
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|setProperty
argument_list|(
name|PICTURE_CLAIM
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getPicture
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|getProperty
argument_list|(
name|PICTURE_CLAIM
argument_list|)
return|;
block|}
specifier|public
name|void
name|setWebsite
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|setProperty
argument_list|(
name|WEBSITE_CLAIM
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getWebsite
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|getProperty
argument_list|(
name|WEBSITE_CLAIM
argument_list|)
return|;
block|}
specifier|public
name|void
name|setGender
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|setProperty
argument_list|(
name|GENDER_CLAIM
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getGender
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|getProperty
argument_list|(
name|GENDER_CLAIM
argument_list|)
return|;
block|}
specifier|public
name|void
name|setZoneInfo
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|setProperty
argument_list|(
name|ZONEINFO_CLAIM
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getZoneInfo
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|getProperty
argument_list|(
name|ZONEINFO_CLAIM
argument_list|)
return|;
block|}
specifier|public
name|void
name|setLocale
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|setProperty
argument_list|(
name|LOCALE_CLAIM
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getLocale
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|getProperty
argument_list|(
name|LOCALE_CLAIM
argument_list|)
return|;
block|}
specifier|public
name|void
name|setEmail
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|setProperty
argument_list|(
name|EMAIL_CLAIM
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getEmail
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|getProperty
argument_list|(
name|EMAIL_CLAIM
argument_list|)
return|;
block|}
specifier|public
name|void
name|setEmailVerified
parameter_list|(
name|Boolean
name|verified
parameter_list|)
block|{
name|setProperty
argument_list|(
name|EMAIL_VERIFIED_CLAIM
argument_list|,
name|verified
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Boolean
name|getEmailVerified
parameter_list|()
block|{
return|return
name|getBooleanProperty
argument_list|(
name|EMAIL_VERIFIED_CLAIM
argument_list|)
return|;
block|}
specifier|public
name|void
name|setBirthDate
parameter_list|(
name|String
name|date
parameter_list|)
block|{
name|setProperty
argument_list|(
name|BIRTHDATE_CLAIM
argument_list|,
name|date
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getBirthDate
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|getProperty
argument_list|(
name|BIRTHDATE_CLAIM
argument_list|)
return|;
block|}
specifier|public
name|String
name|getPhoneNumber
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|getProperty
argument_list|(
name|PHONE_CLAIM
argument_list|)
return|;
block|}
specifier|public
name|void
name|setPhoneNumber
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|setProperty
argument_list|(
name|PHONE_CLAIM
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setPhoneVerified
parameter_list|(
name|Boolean
name|verified
parameter_list|)
block|{
name|setProperty
argument_list|(
name|PHONE_VERIFIED_CLAIM
argument_list|,
name|verified
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Boolean
name|getPhoneVerified
parameter_list|()
block|{
return|return
name|getBooleanProperty
argument_list|(
name|PHONE_VERIFIED_CLAIM
argument_list|)
return|;
block|}
specifier|public
name|UserAddress
name|getUserAddress
parameter_list|()
block|{
name|Object
name|value
init|=
name|getProperty
argument_list|(
name|ADDRESS_CLAIM
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|UserAddress
condition|)
block|{
return|return
operator|(
name|UserAddress
operator|)
name|value
return|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Map
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|value
argument_list|)
decl_stmt|;
return|return
operator|new
name|UserAddress
argument_list|(
name|map
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|void
name|setUserAddress
parameter_list|(
name|UserAddress
name|address
parameter_list|)
block|{
name|setProperty
argument_list|(
name|ADDRESS_CLAIM
argument_list|,
name|address
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setUpdatedAt
parameter_list|(
name|Long
name|time
parameter_list|)
block|{
name|setProperty
argument_list|(
name|UPDATED_AT_CLAIM
argument_list|,
name|time
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Long
name|getUpdatedAt
parameter_list|()
block|{
return|return
name|getLongProperty
argument_list|(
name|UPDATED_AT_CLAIM
argument_list|)
return|;
block|}
specifier|public
name|void
name|setAggregatedClaims
parameter_list|(
name|AggregatedClaims
name|claims
parameter_list|)
block|{
name|setAddClaimNames
argument_list|(
name|claims
operator|.
name|getClaimNames
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|sources
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|claims
operator|.
name|getClaimNames
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|source
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|String
name|jwt
init|=
name|claims
operator|.
name|getClaimSources
argument_list|()
operator|.
name|get
argument_list|(
name|source
argument_list|)
decl_stmt|;
name|sources
operator|.
name|put
argument_list|(
name|source
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
name|OidcUtils
operator|.
name|JWT_CLAIM_SOURCE_PROPERTY
argument_list|,
name|jwt
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|setAddClaimSources
argument_list|(
name|sources
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|setAddClaimSources
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|newSources
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|sources
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|getProperty
argument_list|(
name|OidcUtils
operator|.
name|CLAIM_SOURCES_PROPERTY
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|sources
operator|==
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|OidcUtils
operator|.
name|CLAIM_SOURCES_PROPERTY
argument_list|,
name|sources
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sources
operator|.
name|putAll
argument_list|(
name|newSources
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|setAddClaimNames
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
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|names
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|getProperty
argument_list|(
name|OidcUtils
operator|.
name|CLAIM_NAMES_PROPERTY
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|names
operator|==
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|OidcUtils
operator|.
name|CLAIM_NAMES_PROPERTY
argument_list|,
name|claimNames
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|names
operator|.
name|putAll
argument_list|(
name|claimNames
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|AggregatedClaims
name|getAggregatedClaims
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|names
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|getProperty
argument_list|(
name|OidcUtils
operator|.
name|CLAIM_NAMES_PROPERTY
argument_list|)
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|sources
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|getProperty
argument_list|(
name|OidcUtils
operator|.
name|CLAIM_SOURCES_PROPERTY
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|names
operator|==
literal|null
operator|||
name|sources
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|AggregatedClaims
name|claims
init|=
operator|new
name|AggregatedClaims
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namesMap
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|sourcesMap
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|names
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|source
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|sourceValue
init|=
name|sources
operator|.
name|get
argument_list|(
name|source
argument_list|)
decl_stmt|;
if|if
condition|(
name|sourceValue
operator|!=
literal|null
operator|&&
name|sourceValue
operator|.
name|containsKey
argument_list|(
name|OidcUtils
operator|.
name|JWT_CLAIM_SOURCE_PROPERTY
argument_list|)
condition|)
block|{
name|namesMap
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|source
argument_list|)
expr_stmt|;
name|String
name|jwt
init|=
name|sourceValue
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|sourcesMap
operator|.
name|put
argument_list|(
name|source
argument_list|,
name|jwt
argument_list|)
expr_stmt|;
block|}
block|}
name|claims
operator|.
name|setClaimNames
argument_list|(
name|namesMap
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setClaimSources
argument_list|(
name|sourcesMap
argument_list|)
expr_stmt|;
return|return
name|claims
return|;
block|}
specifier|public
name|void
name|setDistributedClaims
parameter_list|(
name|DistributedClaims
name|claims
parameter_list|)
block|{
name|setAddClaimNames
argument_list|(
name|claims
operator|.
name|getClaimNames
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|sources
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|claims
operator|.
name|getClaimNames
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|source
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|DistributedClaimSource
name|distSource
init|=
name|claims
operator|.
name|getClaimSources
argument_list|()
operator|.
name|get
argument_list|(
name|source
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|mapSource
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|mapSource
operator|.
name|put
argument_list|(
name|OidcUtils
operator|.
name|ENDPOINT_CLAIM_SOURCE_PROPERTY
argument_list|,
name|distSource
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|distSource
operator|.
name|getAccessToken
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|mapSource
operator|.
name|put
argument_list|(
name|OidcUtils
operator|.
name|TOKEN_CLAIM_SOURCE_PROPERTY
argument_list|,
name|distSource
operator|.
name|getAccessToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|sources
operator|.
name|put
argument_list|(
name|source
argument_list|,
name|mapSource
argument_list|)
expr_stmt|;
block|}
name|setAddClaimSources
argument_list|(
name|sources
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DistributedClaims
name|getDistributedClaims
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|names
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|getProperty
argument_list|(
name|OidcUtils
operator|.
name|CLAIM_NAMES_PROPERTY
argument_list|)
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|sources
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|getProperty
argument_list|(
name|OidcUtils
operator|.
name|CLAIM_SOURCES_PROPERTY
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|names
operator|==
literal|null
operator|||
name|sources
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|DistributedClaims
name|claims
init|=
operator|new
name|DistributedClaims
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namesMap
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|DistributedClaimSource
argument_list|>
name|sourcesMap
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|names
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|source
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|sourceValue
init|=
name|sources
operator|.
name|get
argument_list|(
name|source
argument_list|)
decl_stmt|;
if|if
condition|(
name|sourceValue
operator|!=
literal|null
operator|&&
operator|!
name|sourceValue
operator|.
name|containsKey
argument_list|(
name|OidcUtils
operator|.
name|JWT_CLAIM_SOURCE_PROPERTY
argument_list|)
condition|)
block|{
name|namesMap
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|source
argument_list|)
expr_stmt|;
name|DistributedClaimSource
name|distSource
init|=
operator|new
name|DistributedClaimSource
argument_list|()
decl_stmt|;
name|distSource
operator|.
name|setEndpoint
argument_list|(
name|sourceValue
operator|.
name|get
argument_list|(
name|OidcUtils
operator|.
name|ENDPOINT_CLAIM_SOURCE_PROPERTY
argument_list|)
argument_list|)
expr_stmt|;
name|distSource
operator|.
name|setAccessToken
argument_list|(
name|sourceValue
operator|.
name|get
argument_list|(
name|OidcUtils
operator|.
name|TOKEN_CLAIM_SOURCE_PROPERTY
argument_list|)
argument_list|)
expr_stmt|;
name|sourcesMap
operator|.
name|put
argument_list|(
name|source
argument_list|,
name|distSource
argument_list|)
expr_stmt|;
block|}
block|}
name|claims
operator|.
name|setClaimNames
argument_list|(
name|namesMap
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setClaimSources
argument_list|(
name|sourcesMap
argument_list|)
expr_stmt|;
return|return
name|claims
return|;
block|}
block|}
end_class

end_unit

