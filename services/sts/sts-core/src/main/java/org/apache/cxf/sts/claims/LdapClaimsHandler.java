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
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|StringTokenizer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|NamingEnumeration
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|NamingException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|directory
operator|.
name|Attribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|directory
operator|.
name|Attributes
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|directory
operator|.
name|SearchControls
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|kerberos
operator|.
name|KerberosPrincipal
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|x500
operator|.
name|X500Principal
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
name|logging
operator|.
name|LogUtils
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
name|springframework
operator|.
name|ldap
operator|.
name|core
operator|.
name|AttributesMapper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ldap
operator|.
name|core
operator|.
name|LdapTemplate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ldap
operator|.
name|filter
operator|.
name|AndFilter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ldap
operator|.
name|filter
operator|.
name|EqualsFilter
import|;
end_import

begin_class
specifier|public
class|class
name|LdapClaimsHandler
implements|implements
name|ClaimsHandler
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|LdapClaimsHandler
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|LdapTemplate
name|ldap
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|claimMapping
decl_stmt|;
specifier|private
name|String
name|userBaseDn
decl_stmt|;
specifier|private
name|String
name|delimiter
init|=
literal|";"
decl_stmt|;
specifier|private
name|boolean
name|x500FilterEnabled
init|=
literal|true
decl_stmt|;
specifier|private
name|String
name|objectClass
init|=
literal|"person"
decl_stmt|;
specifier|private
name|String
name|userNameAttribute
init|=
literal|"cn"
decl_stmt|;
specifier|public
name|String
name|getObjectClass
parameter_list|()
block|{
return|return
name|objectClass
return|;
block|}
specifier|public
name|void
name|setObjectClass
parameter_list|(
name|String
name|objectClass
parameter_list|)
block|{
name|this
operator|.
name|objectClass
operator|=
name|objectClass
expr_stmt|;
block|}
specifier|public
name|String
name|getUserNameAttribute
parameter_list|()
block|{
return|return
name|userNameAttribute
return|;
block|}
specifier|public
name|void
name|setUserNameAttribute
parameter_list|(
name|String
name|userNameAttribute
parameter_list|)
block|{
name|this
operator|.
name|userNameAttribute
operator|=
name|userNameAttribute
expr_stmt|;
block|}
specifier|public
name|void
name|setLdapTemplate
parameter_list|(
name|LdapTemplate
name|ldapTemplate
parameter_list|)
block|{
name|this
operator|.
name|ldap
operator|=
name|ldapTemplate
expr_stmt|;
block|}
specifier|public
name|LdapTemplate
name|getLdapTemplate
parameter_list|()
block|{
return|return
name|ldap
return|;
block|}
specifier|public
name|void
name|setClaimsLdapAttributeMapping
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|ldapClaimMapping
parameter_list|)
block|{
name|this
operator|.
name|claimMapping
operator|=
name|ldapClaimMapping
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getClaimsLdapAttributeMapping
parameter_list|()
block|{
return|return
name|claimMapping
return|;
block|}
specifier|public
name|void
name|setUserBaseDN
parameter_list|(
name|String
name|userBaseDN
parameter_list|)
block|{
name|this
operator|.
name|userBaseDn
operator|=
name|userBaseDN
expr_stmt|;
block|}
specifier|public
name|String
name|getUserBaseDN
parameter_list|()
block|{
return|return
name|userBaseDn
return|;
block|}
specifier|public
name|void
name|setDelimiter
parameter_list|(
name|String
name|delimiter
parameter_list|)
block|{
name|this
operator|.
name|delimiter
operator|=
name|delimiter
expr_stmt|;
block|}
specifier|public
name|String
name|getDelimiter
parameter_list|()
block|{
return|return
name|delimiter
return|;
block|}
specifier|public
name|boolean
name|isX500FilterEnabled
parameter_list|()
block|{
return|return
name|x500FilterEnabled
return|;
block|}
specifier|public
name|void
name|setX500FilterEnabled
parameter_list|(
name|boolean
name|x500FilterEnabled
parameter_list|)
block|{
name|this
operator|.
name|x500FilterEnabled
operator|=
name|x500FilterEnabled
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|URI
argument_list|>
name|getSupportedClaimTypes
parameter_list|()
block|{
name|List
argument_list|<
name|URI
argument_list|>
name|uriList
init|=
operator|new
name|ArrayList
argument_list|<
name|URI
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|uri
range|:
name|getClaimsLdapAttributeMapping
argument_list|()
operator|.
name|keySet
argument_list|()
control|)
block|{
try|try
block|{
name|uriList
operator|.
name|add
argument_list|(
operator|new
name|URI
argument_list|(
name|uri
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|uriList
return|;
block|}
specifier|public
name|ClaimCollection
name|retrieveClaimValues
parameter_list|(
name|RequestClaimCollection
name|claims
parameter_list|,
name|ClaimsParameters
name|parameters
parameter_list|)
block|{
name|Principal
name|principal
init|=
name|parameters
operator|.
name|getPrincipal
argument_list|()
decl_stmt|;
name|String
name|user
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|principal
operator|instanceof
name|KerberosPrincipal
condition|)
block|{
name|KerberosPrincipal
name|kp
init|=
operator|(
name|KerberosPrincipal
operator|)
name|principal
decl_stmt|;
name|StringTokenizer
name|st
init|=
operator|new
name|StringTokenizer
argument_list|(
name|kp
operator|.
name|getName
argument_list|()
argument_list|,
literal|"@"
argument_list|)
decl_stmt|;
name|user
operator|=
name|st
operator|.
name|nextToken
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|principal
operator|instanceof
name|X500Principal
condition|)
block|{
name|X500Principal
name|x500p
init|=
operator|(
name|X500Principal
operator|)
name|principal
decl_stmt|;
name|LOG
operator|.
name|warning
argument_list|(
literal|"Unsupported principal type X500: "
operator|+
name|x500p
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|new
name|ClaimCollection
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|principal
operator|!=
literal|null
condition|)
block|{
name|user
operator|=
name|principal
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|//[TODO] if onbehalfof -> principal == null
name|LOG
operator|.
name|info
argument_list|(
literal|"Principal is null"
argument_list|)
expr_stmt|;
return|return
operator|new
name|ClaimCollection
argument_list|()
return|;
block|}
if|if
condition|(
name|user
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"User must not be null"
argument_list|)
expr_stmt|;
return|return
operator|new
name|ClaimCollection
argument_list|()
return|;
block|}
else|else
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINEST
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|finest
argument_list|(
literal|"Retrieve claims for user "
operator|+
name|user
argument_list|)
expr_stmt|;
block|}
block|}
name|AndFilter
name|filter
init|=
operator|new
name|AndFilter
argument_list|()
decl_stmt|;
name|filter
operator|.
name|and
argument_list|(
operator|new
name|EqualsFilter
argument_list|(
literal|"objectclass"
argument_list|,
name|this
operator|.
name|getObjectClass
argument_list|()
argument_list|)
argument_list|)
operator|.
name|and
argument_list|(
operator|new
name|EqualsFilter
argument_list|(
name|this
operator|.
name|getUserNameAttribute
argument_list|()
argument_list|,
name|user
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|searchAttributeList
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|RequestClaim
name|claim
range|:
name|claims
control|)
block|{
if|if
condition|(
name|getClaimsLdapAttributeMapping
argument_list|()
operator|.
name|keySet
argument_list|()
operator|.
name|contains
argument_list|(
name|claim
operator|.
name|getClaimType
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
condition|)
block|{
name|searchAttributeList
operator|.
name|add
argument_list|(
name|getClaimsLdapAttributeMapping
argument_list|()
operator|.
name|get
argument_list|(
name|claim
operator|.
name|getClaimType
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINER
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|finer
argument_list|(
literal|"Unsupported claim: "
operator|+
name|claim
operator|.
name|getClaimType
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|String
index|[]
name|searchAttributes
init|=
literal|null
decl_stmt|;
name|searchAttributes
operator|=
name|searchAttributeList
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|searchAttributeList
operator|.
name|size
argument_list|()
index|]
argument_list|)
expr_stmt|;
name|AttributesMapper
name|mapper
init|=
operator|new
name|AttributesMapper
argument_list|()
block|{
specifier|public
name|Object
name|mapFromAttributes
parameter_list|(
name|Attributes
name|attrs
parameter_list|)
throws|throws
name|NamingException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Attribute
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Attribute
argument_list|>
argument_list|()
decl_stmt|;
name|NamingEnumeration
argument_list|<
name|?
extends|extends
name|Attribute
argument_list|>
name|attrEnum
init|=
name|attrs
operator|.
name|getAll
argument_list|()
decl_stmt|;
while|while
condition|(
name|attrEnum
operator|.
name|hasMore
argument_list|()
condition|)
block|{
name|Attribute
name|att
init|=
name|attrEnum
operator|.
name|next
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|att
operator|.
name|getID
argument_list|()
argument_list|,
name|att
argument_list|)
expr_stmt|;
block|}
return|return
name|map
return|;
block|}
block|}
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|result
init|=
name|ldap
operator|.
name|search
argument_list|(
operator|(
name|this
operator|.
name|userBaseDn
operator|==
literal|null
operator|)
condition|?
literal|""
else|:
name|this
operator|.
name|userBaseDn
argument_list|,
name|filter
operator|.
name|toString
argument_list|()
argument_list|,
name|SearchControls
operator|.
name|SUBTREE_SCOPE
argument_list|,
name|searchAttributes
argument_list|,
name|mapper
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Attribute
argument_list|>
name|ldapAttributes
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|result
operator|!=
literal|null
operator|&&
name|result
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|ldapAttributes
operator|=
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
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|ClaimCollection
name|claimsColl
init|=
operator|new
name|ClaimCollection
argument_list|()
decl_stmt|;
for|for
control|(
name|RequestClaim
name|claim
range|:
name|claims
control|)
block|{
name|URI
name|claimType
init|=
name|claim
operator|.
name|getClaimType
argument_list|()
decl_stmt|;
name|String
name|ldapAttribute
init|=
name|getClaimsLdapAttributeMapping
argument_list|()
operator|.
name|get
argument_list|(
name|claimType
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|Attribute
name|attr
init|=
name|ldapAttributes
operator|.
name|get
argument_list|(
name|ldapAttribute
argument_list|)
decl_stmt|;
if|if
condition|(
name|attr
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINEST
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|finest
argument_list|(
literal|"Claim '"
operator|+
name|claim
operator|.
name|getClaimType
argument_list|()
operator|+
literal|"' is null"
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|Claim
name|c
init|=
operator|new
name|Claim
argument_list|()
decl_stmt|;
name|c
operator|.
name|setClaimType
argument_list|(
name|claimType
argument_list|)
expr_stmt|;
name|c
operator|.
name|setPrincipal
argument_list|(
name|principal
argument_list|)
expr_stmt|;
name|StringBuilder
name|claimValue
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
try|try
block|{
name|NamingEnumeration
argument_list|<
name|?
argument_list|>
name|list
init|=
operator|(
name|NamingEnumeration
argument_list|<
name|?
argument_list|>
operator|)
name|attr
operator|.
name|getAll
argument_list|()
decl_stmt|;
while|while
condition|(
name|list
operator|.
name|hasMore
argument_list|()
condition|)
block|{
name|Object
name|obj
init|=
name|list
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|obj
operator|instanceof
name|String
operator|)
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"LDAP attribute '"
operator|+
name|ldapAttribute
operator|+
literal|"' has got an unsupported value type"
argument_list|)
expr_stmt|;
break|break;
block|}
name|String
name|itemValue
init|=
operator|(
name|String
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|isX500FilterEnabled
argument_list|()
condition|)
block|{
try|try
block|{
name|X500Principal
name|x500p
init|=
operator|new
name|X500Principal
argument_list|(
name|itemValue
argument_list|)
decl_stmt|;
name|itemValue
operator|=
name|x500p
operator|.
name|getName
argument_list|()
expr_stmt|;
name|int
name|index
init|=
name|itemValue
operator|.
name|indexOf
argument_list|(
literal|'='
argument_list|)
decl_stmt|;
name|itemValue
operator|=
name|itemValue
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|,
name|itemValue
operator|.
name|indexOf
argument_list|(
literal|','
argument_list|,
name|index
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//Ignore, not X500 compliant thus use the whole string as the value
block|}
block|}
name|claimValue
operator|.
name|append
argument_list|(
name|itemValue
argument_list|)
expr_stmt|;
if|if
condition|(
name|list
operator|.
name|hasMore
argument_list|()
condition|)
block|{
name|claimValue
operator|.
name|append
argument_list|(
name|this
operator|.
name|getDelimiter
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|NamingException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Failed to read value of LDAP attribute '"
operator|+
name|ldapAttribute
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
name|c
operator|.
name|setValue
argument_list|(
name|claimValue
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|// c.setIssuer(issuer);
comment|// c.setOriginalIssuer(originalIssuer);
comment|// c.setNamespace(namespace);
name|claimsColl
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|claimsColl
return|;
block|}
block|}
end_class

end_unit

