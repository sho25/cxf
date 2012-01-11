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
specifier|abstract
class|class
name|AbstractAuthorizingInInterceptor
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
name|AbstractAuthorizingInInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ALL_ROLES
init|=
literal|"*"
decl_stmt|;
specifier|public
name|AbstractAuthorizingInInterceptor
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
name|sc
operator|==
literal|null
condition|)
block|{
return|return;
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
name|SecurityContext
name|sc
parameter_list|,
name|Method
name|method
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|expectedRoles
init|=
name|getExpectedRoles
argument_list|(
name|method
argument_list|)
decl_stmt|;
if|if
condition|(
name|expectedRoles
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|denyRoles
init|=
name|getDenyRoles
argument_list|(
name|method
argument_list|)
decl_stmt|;
return|return
name|denyRoles
operator|.
name|isEmpty
argument_list|()
condition|?
literal|true
else|:
name|isUserInRole
argument_list|(
name|sc
argument_list|,
name|denyRoles
argument_list|,
literal|true
argument_list|)
return|;
block|}
if|if
condition|(
name|isUserInRole
argument_list|(
name|sc
argument_list|,
name|expectedRoles
argument_list|,
literal|false
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|sc
operator|.
name|getUserPrincipal
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
name|sc
operator|.
name|getUserPrincipal
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" is not authorized"
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
specifier|protected
name|boolean
name|isUserInRole
parameter_list|(
name|SecurityContext
name|sc
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|roles
parameter_list|,
name|boolean
name|deny
parameter_list|)
block|{
if|if
condition|(
name|roles
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|&&
name|ALL_ROLES
operator|.
name|equals
argument_list|(
name|roles
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
condition|)
block|{
return|return
operator|!
name|deny
return|;
block|}
for|for
control|(
name|String
name|role
range|:
name|roles
control|)
block|{
if|if
condition|(
name|sc
operator|.
name|isUserInRole
argument_list|(
name|role
argument_list|)
condition|)
block|{
return|return
operator|!
name|deny
return|;
block|}
block|}
return|return
name|deny
return|;
block|}
comment|/**      * Returns a list of expected roles for a given method.       * @param method Method      * @return list, empty if no roles are available      */
specifier|protected
specifier|abstract
name|List
argument_list|<
name|String
argument_list|>
name|getExpectedRoles
parameter_list|(
name|Method
name|method
parameter_list|)
function_decl|;
comment|/**      * Returns a list of roles to be denied for a given method.       * @param method Method      * @return list, empty if no roles are available      */
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|getDenyRoles
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
block|}
end_class

end_unit

