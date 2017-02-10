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
name|interceptor
operator|.
name|security
package|;
end_package

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
name|Collections
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
name|Set
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
name|Subject
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
name|LoginSecurityContext
import|;
end_import

begin_class
specifier|public
class|class
name|RolePrefixSecurityContextImpl
implements|implements
name|LoginSecurityContext
block|{
specifier|private
name|Principal
name|p
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|Principal
argument_list|>
name|roles
decl_stmt|;
specifier|private
name|Subject
name|theSubject
decl_stmt|;
specifier|public
name|RolePrefixSecurityContextImpl
parameter_list|(
name|Subject
name|subject
parameter_list|,
name|String
name|rolePrefix
parameter_list|)
block|{
name|this
argument_list|(
name|subject
argument_list|,
name|rolePrefix
argument_list|,
name|JAASLoginInterceptor
operator|.
name|ROLE_CLASSIFIER_PREFIX
argument_list|)
expr_stmt|;
block|}
specifier|public
name|RolePrefixSecurityContextImpl
parameter_list|(
name|Subject
name|subject
parameter_list|,
name|String
name|roleClassifier
parameter_list|,
name|String
name|roleClassifierType
parameter_list|)
block|{
name|this
operator|.
name|p
operator|=
name|findPrincipal
argument_list|(
name|subject
argument_list|,
name|roleClassifier
argument_list|,
name|roleClassifierType
argument_list|)
expr_stmt|;
name|this
operator|.
name|roles
operator|=
name|findRoles
argument_list|(
name|subject
argument_list|,
name|roleClassifier
argument_list|,
name|roleClassifierType
argument_list|)
expr_stmt|;
name|this
operator|.
name|theSubject
operator|=
name|subject
expr_stmt|;
block|}
specifier|public
name|Principal
name|getUserPrincipal
parameter_list|()
block|{
return|return
name|p
return|;
block|}
specifier|public
name|boolean
name|isUserInRole
parameter_list|(
name|String
name|role
parameter_list|)
block|{
comment|// there is no guarantee the Principal instances retrieved
comment|// from the Subject properly implement equalTo
for|for
control|(
name|Principal
name|principal
range|:
name|roles
control|)
block|{
if|if
condition|(
name|principal
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|role
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
specifier|static
name|Principal
name|findPrincipal
parameter_list|(
name|Subject
name|subject
parameter_list|,
name|String
name|roleClassifier
parameter_list|,
name|String
name|roleClassifierType
parameter_list|)
block|{
for|for
control|(
name|Principal
name|p
range|:
name|subject
operator|.
name|getPrincipals
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|isRole
argument_list|(
name|p
argument_list|,
name|roleClassifier
argument_list|,
name|roleClassifierType
argument_list|)
condition|)
block|{
return|return
name|p
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
specifier|static
name|Set
argument_list|<
name|Principal
argument_list|>
name|findRoles
parameter_list|(
name|Subject
name|subject
parameter_list|,
name|String
name|roleClassifier
parameter_list|,
name|String
name|roleClassifierType
parameter_list|)
block|{
name|Set
argument_list|<
name|Principal
argument_list|>
name|set
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Principal
name|p
range|:
name|subject
operator|.
name|getPrincipals
argument_list|()
control|)
block|{
if|if
condition|(
name|isRole
argument_list|(
name|p
argument_list|,
name|roleClassifier
argument_list|,
name|roleClassifierType
argument_list|)
condition|)
block|{
name|set
operator|.
name|add
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|set
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|boolean
name|isRole
parameter_list|(
name|Principal
name|p
parameter_list|,
name|String
name|roleClassifier
parameter_list|,
name|String
name|roleClassifierType
parameter_list|)
block|{
if|if
condition|(
name|JAASLoginInterceptor
operator|.
name|ROLE_CLASSIFIER_PREFIX
operator|.
name|equals
argument_list|(
name|roleClassifierType
argument_list|)
condition|)
block|{
return|return
name|p
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
name|roleClassifier
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|p
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
name|roleClassifier
argument_list|)
return|;
block|}
block|}
specifier|public
name|Subject
name|getSubject
parameter_list|()
block|{
return|return
name|theSubject
return|;
block|}
specifier|public
name|Set
argument_list|<
name|Principal
argument_list|>
name|getUserRoles
parameter_list|()
block|{
return|return
name|roles
return|;
block|}
block|}
end_class

end_unit

