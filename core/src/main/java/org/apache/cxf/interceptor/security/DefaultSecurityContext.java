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
name|Enumeration
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
name|common
operator|.
name|security
operator|.
name|GroupPrincipal
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
name|ReflectionUtil
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

begin_comment
comment|/**  * SecurityContext which implements isUserInRole using the  * following approach : skip the first Subject principal, and then checks  * Groups the principal is a member of  */
end_comment

begin_class
specifier|public
class|class
name|DefaultSecurityContext
implements|implements
name|LoginSecurityContext
block|{
specifier|private
specifier|static
name|Class
argument_list|<
name|?
argument_list|>
name|javaGroup
decl_stmt|;
specifier|private
specifier|static
name|Class
argument_list|<
name|?
argument_list|>
name|karafGroup
decl_stmt|;
specifier|private
name|Principal
name|p
decl_stmt|;
specifier|private
name|Subject
name|subject
decl_stmt|;
static|static
block|{
try|try
block|{
name|javaGroup
operator|=
name|Class
operator|.
name|forName
argument_list|(
literal|"java.security.acl.Group"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|javaGroup
operator|=
literal|null
expr_stmt|;
block|}
try|try
block|{
name|karafGroup
operator|=
name|Class
operator|.
name|forName
argument_list|(
literal|"org.apache.karaf.jaas.boot.principal.Group"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|karafGroup
operator|=
literal|null
expr_stmt|;
block|}
block|}
specifier|public
name|DefaultSecurityContext
parameter_list|(
name|Subject
name|subject
parameter_list|)
block|{
name|this
operator|.
name|p
operator|=
name|findPrincipal
argument_list|(
literal|null
argument_list|,
name|subject
argument_list|)
expr_stmt|;
name|this
operator|.
name|subject
operator|=
name|subject
expr_stmt|;
block|}
specifier|public
name|DefaultSecurityContext
parameter_list|(
name|String
name|principalName
parameter_list|,
name|Subject
name|subject
parameter_list|)
block|{
name|this
operator|.
name|p
operator|=
name|findPrincipal
argument_list|(
name|principalName
argument_list|,
name|subject
argument_list|)
expr_stmt|;
name|this
operator|.
name|subject
operator|=
name|subject
expr_stmt|;
block|}
specifier|public
name|DefaultSecurityContext
parameter_list|(
name|Principal
name|p
parameter_list|,
name|Subject
name|subject
parameter_list|)
block|{
name|this
operator|.
name|p
operator|=
name|p
expr_stmt|;
name|this
operator|.
name|subject
operator|=
name|subject
expr_stmt|;
if|if
condition|(
name|p
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|p
operator|=
name|findPrincipal
argument_list|(
literal|null
argument_list|,
name|subject
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|Principal
name|findPrincipal
parameter_list|(
name|String
name|principalName
parameter_list|,
name|Subject
name|subject
parameter_list|)
block|{
if|if
condition|(
name|subject
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
for|for
control|(
name|Principal
name|principal
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
name|isGroupPrincipal
argument_list|(
name|principal
argument_list|)
operator|&&
operator|(
name|principalName
operator|==
literal|null
operator|||
name|principal
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|principalName
argument_list|)
operator|)
condition|)
block|{
return|return
name|principal
return|;
block|}
block|}
comment|// No match for the principalName. Just return first non-Group Principal
if|if
condition|(
name|principalName
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Principal
name|principal
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
name|isGroupPrincipal
argument_list|(
name|principal
argument_list|)
condition|)
block|{
return|return
name|principal
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
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
if|if
condition|(
name|subject
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Principal
name|principal
range|:
name|subject
operator|.
name|getPrincipals
argument_list|()
control|)
block|{
if|if
condition|(
name|isGroupPrincipal
argument_list|(
name|principal
argument_list|)
operator|&&
name|checkGroup
argument_list|(
operator|(
name|Principal
operator|)
name|principal
argument_list|,
name|role
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|p
operator|!=
name|principal
operator|&&
name|role
operator|.
name|equals
argument_list|(
name|principal
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|protected
name|boolean
name|checkGroup
parameter_list|(
name|Principal
name|principal
parameter_list|,
name|String
name|role
parameter_list|)
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
name|Enumeration
argument_list|<
name|?
extends|extends
name|Principal
argument_list|>
name|members
init|=
literal|null
decl_stmt|;
try|try
block|{
name|Method
name|m
init|=
name|ReflectionUtil
operator|.
name|getMethod
argument_list|(
name|principal
operator|.
name|getClass
argument_list|()
argument_list|,
literal|"members"
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Enumeration
argument_list|<
name|?
extends|extends
name|Principal
argument_list|>
name|ms
init|=
operator|(
name|Enumeration
argument_list|<
name|?
extends|extends
name|Principal
argument_list|>
operator|)
name|m
operator|.
name|invoke
argument_list|(
name|principal
argument_list|)
decl_stmt|;
name|members
operator|=
name|ms
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
while|while
condition|(
name|members
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
comment|// this might be a plain role but could represent a group consisting of other groups/roles
name|Principal
name|member
init|=
name|members
operator|.
name|nextElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|member
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|role
argument_list|)
operator|||
name|isGroupPrincipal
argument_list|(
name|member
argument_list|)
operator|&&
name|checkGroup
argument_list|(
operator|(
name|GroupPrincipal
operator|)
name|member
argument_list|,
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
specifier|public
name|Subject
name|getSubject
parameter_list|()
block|{
return|return
name|subject
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
name|Set
argument_list|<
name|Principal
argument_list|>
name|roles
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|subject
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Principal
name|principal
range|:
name|subject
operator|.
name|getPrincipals
argument_list|()
control|)
block|{
if|if
condition|(
name|principal
operator|!=
name|p
condition|)
block|{
name|roles
operator|.
name|add
argument_list|(
name|principal
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|roles
return|;
block|}
specifier|private
specifier|static
name|boolean
name|instanceOfGroup
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
try|try
block|{
return|return
operator|(
name|javaGroup
operator|!=
literal|null
operator|&&
name|javaGroup
operator|.
name|isInstance
argument_list|(
name|obj
argument_list|)
operator|)
operator|||
operator|(
name|karafGroup
operator|!=
literal|null
operator|&&
name|karafGroup
operator|.
name|isInstance
argument_list|(
name|obj
argument_list|)
operator|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
specifier|public
specifier|static
name|boolean
name|isGroupPrincipal
parameter_list|(
name|Principal
name|principal
parameter_list|)
block|{
return|return
name|principal
operator|instanceof
name|GroupPrincipal
operator|||
name|instanceOfGroup
argument_list|(
name|principal
argument_list|)
return|;
block|}
block|}
end_class

end_unit

