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
name|Iterator
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
name|sts
operator|.
name|token
operator|.
name|provider
operator|.
name|AttributeStatementProvider
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
name|sts
operator|.
name|token
operator|.
name|provider
operator|.
name|TokenProviderParameters
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|WSS4JConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|saml
operator|.
name|bean
operator|.
name|AttributeBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|saml
operator|.
name|bean
operator|.
name|AttributeStatementBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|saml
operator|.
name|builder
operator|.
name|SAML2Constants
import|;
end_import

begin_comment
comment|/**  * This class differs from the ClaimsAttributeStatementProvider in that it combines claims that have the same name.  */
end_comment

begin_class
specifier|public
class|class
name|CombinedClaimsAttributeStatementProvider
implements|implements
name|AttributeStatementProvider
block|{
specifier|private
name|String
name|nameFormat
init|=
name|SAML2Constants
operator|.
name|ATTRNAME_FORMAT_UNSPECIFIED
decl_stmt|;
specifier|public
name|AttributeStatementBean
name|getStatement
parameter_list|(
name|TokenProviderParameters
name|providerParameters
parameter_list|)
block|{
comment|// Handle Claims
name|ProcessedClaimCollection
name|retrievedClaims
init|=
name|ClaimsUtils
operator|.
name|processClaims
argument_list|(
name|providerParameters
argument_list|)
decl_stmt|;
if|if
condition|(
name|retrievedClaims
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Iterator
argument_list|<
name|ProcessedClaim
argument_list|>
name|claimIterator
init|=
name|retrievedClaims
operator|.
name|iterator
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|claimIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Map
argument_list|<
name|AttributeKey
argument_list|,
name|AttributeBean
argument_list|>
name|attributeMap
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|String
name|tokenType
init|=
name|providerParameters
operator|.
name|getTokenRequirements
argument_list|()
operator|.
name|getTokenType
argument_list|()
decl_stmt|;
name|boolean
name|saml2
init|=
name|WSS4JConstants
operator|.
name|WSS_SAML2_TOKEN_TYPE
operator|.
name|equals
argument_list|(
name|tokenType
argument_list|)
operator|||
name|WSS4JConstants
operator|.
name|SAML2_NS
operator|.
name|equals
argument_list|(
name|tokenType
argument_list|)
decl_stmt|;
while|while
condition|(
name|claimIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ProcessedClaim
name|claim
init|=
name|claimIterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|AttributeKey
name|attributeKey
init|=
name|createAttributeKey
argument_list|(
name|claim
argument_list|,
name|saml2
argument_list|)
decl_stmt|;
name|attributeMap
operator|.
name|merge
argument_list|(
name|attributeKey
argument_list|,
name|createAttributeBean
argument_list|(
name|attributeKey
argument_list|,
name|claim
operator|.
name|getValues
argument_list|()
argument_list|)
argument_list|,
parameter_list|(
name|v1
parameter_list|,
name|v2
parameter_list|)
lambda|->
block|{
name|v1
operator|.
name|getAttributeValues
argument_list|()
operator|.
name|addAll
argument_list|(
name|claim
operator|.
name|getValues
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|v1
return|;
block|}
argument_list|)
expr_stmt|;
block|}
name|AttributeStatementBean
name|attrBean
init|=
operator|new
name|AttributeStatementBean
argument_list|()
decl_stmt|;
name|attrBean
operator|.
name|setSamlAttributes
argument_list|(
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|attributeMap
operator|.
name|values
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|attrBean
return|;
block|}
specifier|private
name|AttributeBean
name|createAttributeBean
parameter_list|(
name|AttributeKey
name|attributeKey
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|claimValues
parameter_list|)
block|{
name|AttributeBean
name|attributeBean
init|=
operator|new
name|AttributeBean
argument_list|(
name|attributeKey
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|attributeKey
operator|.
name|getQualifiedName
argument_list|()
argument_list|,
name|claimValues
argument_list|)
decl_stmt|;
name|attributeBean
operator|.
name|setNameFormat
argument_list|(
name|attributeKey
operator|.
name|getNameFormat
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|attributeBean
return|;
block|}
specifier|private
name|AttributeKey
name|createAttributeKey
parameter_list|(
name|ProcessedClaim
name|claim
parameter_list|,
name|boolean
name|saml2
parameter_list|)
block|{
name|URI
name|claimType
init|=
name|claim
operator|.
name|getClaimType
argument_list|()
decl_stmt|;
if|if
condition|(
name|saml2
condition|)
block|{
return|return
operator|new
name|AttributeKey
argument_list|(
name|claimType
operator|.
name|toString
argument_list|()
argument_list|,
name|nameFormat
argument_list|,
literal|null
argument_list|)
return|;
block|}
else|else
block|{
name|String
name|uri
init|=
name|claimType
operator|.
name|toString
argument_list|()
decl_stmt|;
name|int
name|lastSlash
init|=
name|uri
operator|.
name|lastIndexOf
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
if|if
condition|(
name|lastSlash
operator|==
operator|(
name|uri
operator|.
name|length
argument_list|()
operator|-
literal|1
operator|)
condition|)
block|{
name|uri
operator|=
name|uri
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|lastSlash
argument_list|)
expr_stmt|;
name|lastSlash
operator|=
name|uri
operator|.
name|lastIndexOf
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
block|}
name|String
name|namespace
init|=
name|uri
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|lastSlash
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|uri
operator|.
name|substring
argument_list|(
name|lastSlash
operator|+
literal|1
argument_list|,
name|uri
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|AttributeKey
argument_list|(
name|namespace
argument_list|,
literal|null
argument_list|,
name|name
argument_list|)
return|;
block|}
block|}
specifier|public
name|String
name|getNameFormat
parameter_list|()
block|{
return|return
name|nameFormat
return|;
block|}
specifier|public
name|void
name|setNameFormat
parameter_list|(
name|String
name|nameFormat
parameter_list|)
block|{
name|this
operator|.
name|nameFormat
operator|=
name|nameFormat
expr_stmt|;
block|}
specifier|private
specifier|static
class|class
name|AttributeKey
block|{
specifier|private
specifier|final
name|String
name|qualifiedName
decl_stmt|;
specifier|private
specifier|final
name|String
name|simpleName
decl_stmt|;
specifier|private
specifier|final
name|String
name|nameFormat
decl_stmt|;
comment|// SAML 2.0 constructor
name|AttributeKey
parameter_list|(
name|String
name|qualifiedName
parameter_list|,
name|String
name|nameFormat
parameter_list|,
name|String
name|simpleName
parameter_list|)
block|{
name|this
operator|.
name|qualifiedName
operator|=
name|qualifiedName
expr_stmt|;
name|this
operator|.
name|nameFormat
operator|=
name|nameFormat
expr_stmt|;
name|this
operator|.
name|simpleName
operator|=
name|simpleName
expr_stmt|;
block|}
specifier|public
name|String
name|getQualifiedName
parameter_list|()
block|{
return|return
name|qualifiedName
return|;
block|}
specifier|public
name|String
name|getSimpleName
parameter_list|()
block|{
return|return
name|simpleName
return|;
block|}
specifier|public
name|String
name|getNameFormat
parameter_list|()
block|{
return|return
name|nameFormat
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|AttributeKey
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|AttributeKey
name|that
init|=
operator|(
name|AttributeKey
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|qualifiedName
operator|==
literal|null
operator|&&
name|that
operator|.
name|qualifiedName
operator|!=
literal|null
operator|||
name|qualifiedName
operator|!=
literal|null
operator|&&
operator|!
name|qualifiedName
operator|.
name|equals
argument_list|(
name|that
operator|.
name|qualifiedName
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|simpleName
operator|==
literal|null
operator|&&
name|that
operator|.
name|simpleName
operator|!=
literal|null
operator|||
name|simpleName
operator|!=
literal|null
operator|&&
operator|!
name|simpleName
operator|.
name|equals
argument_list|(
name|that
operator|.
name|simpleName
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
operator|!
operator|(
name|nameFormat
operator|==
literal|null
operator|&&
name|that
operator|.
name|nameFormat
operator|!=
literal|null
operator|||
name|nameFormat
operator|!=
literal|null
operator|&&
operator|!
name|nameFormat
operator|.
name|equals
argument_list|(
name|that
operator|.
name|nameFormat
argument_list|)
operator|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|result
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|qualifiedName
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|qualifiedName
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|simpleName
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|simpleName
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|nameFormat
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|nameFormat
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
block|}
end_class

end_unit

