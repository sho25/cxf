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
name|saml
operator|.
name|authorization
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|Arrays
import|;
end_import

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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|Set
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
name|common
operator|.
name|util
operator|.
name|ClassHelper
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
name|interceptor
operator|.
name|Fault
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
name|interceptor
operator|.
name|security
operator|.
name|AccessDeniedException
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
name|message
operator|.
name|Message
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
name|phase
operator|.
name|AbstractPhaseInterceptor
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
name|phase
operator|.
name|Phase
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
name|security
operator|.
name|SecurityContext
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
name|security
operator|.
name|claims
operator|.
name|authorization
operator|.
name|Claim
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
name|security
operator|.
name|claims
operator|.
name|authorization
operator|.
name|ClaimMode
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
name|security
operator|.
name|claims
operator|.
name|authorization
operator|.
name|Claims
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
name|service
operator|.
name|Service
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
name|service
operator|.
name|invoker
operator|.
name|MethodDispatcher
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
name|service
operator|.
name|model
operator|.
name|BindingOperationInfo
import|;
end_import

begin_class
specifier|public
class|class
name|ClaimsAuthorizingInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
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
name|ClaimsAuthorizingInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|SKIP_METHODS
decl_stmt|;
static|static
block|{
name|SKIP_METHODS
operator|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|SKIP_METHODS
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"wait"
block|,
literal|"notify"
block|,
literal|"notifyAll"
block|,
literal|"equals"
block|,
literal|"toString"
block|,
literal|"hashCode"
block|}
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|ClaimBean
argument_list|>
argument_list|>
name|claims
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|ClaimBean
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|nameAliases
init|=
name|Collections
operator|.
name|emptyMap
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|formatAliases
init|=
name|Collections
operator|.
name|emptyMap
argument_list|()
decl_stmt|;
specifier|public
name|ClaimsAuthorizingInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_INVOKE
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|SecurityContext
name|sc
init|=
name|message
operator|.
name|get
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|sc
operator|instanceof
name|SAMLSecurityContext
operator|)
condition|)
block|{
throw|throw
operator|new
name|AccessDeniedException
argument_list|(
literal|"Security Context is unavailable or unrecognized"
argument_list|)
throw|;
block|}
name|Method
name|method
init|=
name|getTargetMethod
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|authorize
argument_list|(
operator|(
name|SAMLSecurityContext
operator|)
name|sc
argument_list|,
name|method
argument_list|)
condition|)
block|{
return|return;
block|}
throw|throw
operator|new
name|AccessDeniedException
argument_list|(
literal|"Unauthorized"
argument_list|)
throw|;
block|}
specifier|public
name|void
name|setClaims
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|ClaimBean
argument_list|>
argument_list|>
name|claimsMap
parameter_list|)
block|{
name|claims
operator|.
name|putAll
argument_list|(
name|claimsMap
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Method
name|getTargetMethod
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|BindingOperationInfo
name|bop
init|=
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|bop
operator|!=
literal|null
condition|)
block|{
name|MethodDispatcher
name|md
init|=
operator|(
name|MethodDispatcher
operator|)
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Service
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|(
name|MethodDispatcher
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|md
operator|.
name|getMethod
argument_list|(
name|bop
argument_list|)
return|;
block|}
name|Method
name|method
init|=
operator|(
name|Method
operator|)
name|m
operator|.
name|get
argument_list|(
literal|"org.apache.cxf.resource.method"
argument_list|)
decl_stmt|;
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
return|return
name|method
return|;
block|}
throw|throw
operator|new
name|AccessDeniedException
argument_list|(
literal|"Method is not available : Unauthorized"
argument_list|)
throw|;
block|}
specifier|protected
name|boolean
name|authorize
parameter_list|(
name|SAMLSecurityContext
name|sc
parameter_list|,
name|Method
name|method
parameter_list|)
block|{
name|List
argument_list|<
name|ClaimBean
argument_list|>
name|list
init|=
name|claims
operator|.
name|get
argument_list|(
name|method
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
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
name|saml
operator|.
name|assertion
operator|.
name|Claims
name|actualClaims
init|=
name|sc
operator|.
name|getClaims
argument_list|()
decl_stmt|;
for|for
control|(
name|ClaimBean
name|claimBean
range|:
name|list
control|)
block|{
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
name|saml
operator|.
name|assertion
operator|.
name|Claim
name|claim
init|=
name|claimBean
operator|.
name|getClaim
argument_list|()
decl_stmt|;
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
name|saml
operator|.
name|assertion
operator|.
name|Claim
name|matchingClaim
init|=
name|actualClaims
operator|.
name|findClaimByFormatAndName
argument_list|(
name|claim
operator|.
name|getNameFormat
argument_list|()
argument_list|,
name|claim
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|matchingClaim
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|claimBean
operator|.
name|getClaimMode
argument_list|()
operator|==
name|ClaimMode
operator|.
name|STRICT
condition|)
block|{
return|return
literal|false
return|;
block|}
else|else
block|{
continue|continue;
block|}
block|}
name|List
argument_list|<
name|String
argument_list|>
name|claimValues
init|=
name|claim
operator|.
name|getValues
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|matchingClaimValues
init|=
name|matchingClaim
operator|.
name|getValues
argument_list|()
decl_stmt|;
if|if
condition|(
name|claimBean
operator|.
name|isMatchAll
argument_list|()
operator|&&
operator|!
name|matchingClaimValues
operator|.
name|containsAll
argument_list|(
name|claimValues
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
else|else
block|{
name|boolean
name|matched
init|=
literal|false
decl_stmt|;
for|for
control|(
name|String
name|value
range|:
name|matchingClaimValues
control|)
block|{
if|if
condition|(
name|claimValues
operator|.
name|contains
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|matched
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|matched
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|void
name|setSecuredObject
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
name|ClassHelper
operator|.
name|getRealClass
argument_list|(
name|object
argument_list|)
decl_stmt|;
name|findClaims
argument_list|(
name|cls
argument_list|)
expr_stmt|;
if|if
condition|(
name|claims
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"The claims list is empty, the service object is not protected"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|findClaims
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
if|if
condition|(
name|cls
operator|==
literal|null
operator|||
name|cls
operator|==
name|Object
operator|.
name|class
condition|)
block|{
return|return;
block|}
name|List
argument_list|<
name|ClaimBean
argument_list|>
name|clsClaims
init|=
name|getClaims
argument_list|(
name|cls
operator|.
name|getAnnotation
argument_list|(
name|Claims
operator|.
name|class
argument_list|)
argument_list|,
name|cls
operator|.
name|getAnnotation
argument_list|(
name|Claim
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|Method
name|m
range|:
name|cls
operator|.
name|getMethods
argument_list|()
control|)
block|{
if|if
condition|(
name|SKIP_METHODS
operator|.
name|contains
argument_list|(
name|m
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|List
argument_list|<
name|ClaimBean
argument_list|>
name|methodClaims
init|=
name|getClaims
argument_list|(
name|m
operator|.
name|getAnnotation
argument_list|(
name|Claims
operator|.
name|class
argument_list|)
argument_list|,
name|m
operator|.
name|getAnnotation
argument_list|(
name|Claim
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ClaimBean
argument_list|>
name|allClaims
init|=
operator|new
name|ArrayList
argument_list|<
name|ClaimBean
argument_list|>
argument_list|(
name|methodClaims
argument_list|)
decl_stmt|;
for|for
control|(
name|ClaimBean
name|bean
range|:
name|clsClaims
control|)
block|{
if|if
condition|(
name|isClaimOverridden
argument_list|(
name|bean
argument_list|,
name|methodClaims
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|allClaims
operator|.
name|add
argument_list|(
name|bean
argument_list|)
expr_stmt|;
block|}
name|claims
operator|.
name|put
argument_list|(
name|m
operator|.
name|getName
argument_list|()
argument_list|,
name|allClaims
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|claims
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|findClaims
argument_list|(
name|cls
operator|.
name|getSuperclass
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|claims
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|interfaceCls
range|:
name|cls
operator|.
name|getInterfaces
argument_list|()
control|)
block|{
name|findClaims
argument_list|(
name|interfaceCls
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|boolean
name|isClaimOverridden
parameter_list|(
name|ClaimBean
name|bean
parameter_list|,
name|List
argument_list|<
name|ClaimBean
argument_list|>
name|mClaims
parameter_list|)
block|{
for|for
control|(
name|ClaimBean
name|methodBean
range|:
name|mClaims
control|)
block|{
if|if
condition|(
name|bean
operator|.
name|getClaim
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|methodBean
operator|.
name|getClaim
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|bean
operator|.
name|getClaim
argument_list|()
operator|.
name|getNameFormat
argument_list|()
operator|.
name|equals
argument_list|(
name|methodBean
operator|.
name|getClaim
argument_list|()
operator|.
name|getNameFormat
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|List
argument_list|<
name|ClaimBean
argument_list|>
name|getClaims
parameter_list|(
name|Claims
name|claimsAnn
parameter_list|,
name|Claim
name|claimAnn
parameter_list|)
block|{
name|List
argument_list|<
name|ClaimBean
argument_list|>
name|claimsList
init|=
operator|new
name|ArrayList
argument_list|<
name|ClaimBean
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Claim
argument_list|>
name|annClaims
init|=
operator|new
name|ArrayList
argument_list|<
name|Claim
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|claimsAnn
operator|!=
literal|null
condition|)
block|{
name|annClaims
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|claimsAnn
operator|.
name|value
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|claimAnn
operator|!=
literal|null
condition|)
block|{
name|annClaims
operator|.
name|add
argument_list|(
name|claimAnn
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Claim
name|ann
range|:
name|annClaims
control|)
block|{
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
name|saml
operator|.
name|assertion
operator|.
name|Claim
name|claim
init|=
operator|new
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
name|saml
operator|.
name|assertion
operator|.
name|Claim
argument_list|()
decl_stmt|;
name|String
name|claimName
init|=
name|ann
operator|.
name|name
argument_list|()
decl_stmt|;
if|if
condition|(
name|nameAliases
operator|.
name|containsKey
argument_list|(
name|claimName
argument_list|)
condition|)
block|{
name|claimName
operator|=
name|nameAliases
operator|.
name|get
argument_list|(
name|claimName
argument_list|)
expr_stmt|;
block|}
name|String
name|claimFormat
init|=
name|ann
operator|.
name|format
argument_list|()
decl_stmt|;
if|if
condition|(
name|formatAliases
operator|.
name|containsKey
argument_list|(
name|claimFormat
argument_list|)
condition|)
block|{
name|claimFormat
operator|=
name|formatAliases
operator|.
name|get
argument_list|(
name|claimFormat
argument_list|)
expr_stmt|;
block|}
name|claim
operator|.
name|setName
argument_list|(
name|claimName
argument_list|)
expr_stmt|;
name|claim
operator|.
name|setNameFormat
argument_list|(
name|claimFormat
argument_list|)
expr_stmt|;
name|claim
operator|.
name|setValues
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|ann
operator|.
name|value
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|claimsList
operator|.
name|add
argument_list|(
operator|new
name|ClaimBean
argument_list|(
name|claim
argument_list|,
name|ann
operator|.
name|mode
argument_list|()
argument_list|,
name|ann
operator|.
name|matchAll
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|claimsList
return|;
block|}
specifier|public
name|void
name|setNameAliases
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|nameAliases
parameter_list|)
block|{
name|this
operator|.
name|nameAliases
operator|=
name|nameAliases
expr_stmt|;
block|}
specifier|public
name|void
name|setFormatAliases
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|formatAliases
parameter_list|)
block|{
name|this
operator|.
name|formatAliases
operator|=
name|formatAliases
expr_stmt|;
block|}
block|}
end_class

end_unit

