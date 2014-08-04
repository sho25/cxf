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
name|PrivilegedAction
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
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|Callback
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
name|callback
operator|.
name|CallbackHandler
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
name|callback
operator|.
name|NameCallback
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
name|login
operator|.
name|Configuration
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
name|login
operator|.
name|LoginContext
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
name|login
operator|.
name|LoginException
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
name|interceptor
operator|.
name|InterceptorChain
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
name|callback
operator|.
name|CallbackHandlerProvider
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
name|callback
operator|.
name|CallbackHandlerProviderAuthPol
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
name|callback
operator|.
name|CallbackHandlerProviderUsernameToken
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

begin_class
specifier|public
class|class
name|JAASLoginInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ROLE_CLASSIFIER_PREFIX
init|=
literal|"prefix"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ROLE_CLASSIFIER_CLASS_NAME
init|=
literal|"classname"
decl_stmt|;
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
name|JAASLoginInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|String
name|contextName
init|=
literal|""
decl_stmt|;
specifier|private
name|Configuration
name|loginConfig
decl_stmt|;
specifier|private
name|String
name|roleClassifier
decl_stmt|;
specifier|private
name|String
name|roleClassifierType
init|=
name|ROLE_CLASSIFIER_PREFIX
decl_stmt|;
specifier|private
name|boolean
name|reportFault
decl_stmt|;
specifier|private
name|boolean
name|useDoAs
init|=
literal|true
decl_stmt|;
specifier|private
name|List
argument_list|<
name|CallbackHandlerProvider
argument_list|>
name|callbackHandlerProviders
decl_stmt|;
specifier|private
name|boolean
name|allowAnonymous
init|=
literal|true
decl_stmt|;
specifier|public
name|JAASLoginInterceptor
parameter_list|()
block|{
name|this
argument_list|(
name|Phase
operator|.
name|UNMARSHAL
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JAASLoginInterceptor
parameter_list|(
name|String
name|phase
parameter_list|)
block|{
name|super
argument_list|(
name|phase
argument_list|)
expr_stmt|;
name|this
operator|.
name|callbackHandlerProviders
operator|=
operator|new
name|ArrayList
argument_list|<
name|CallbackHandlerProvider
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|callbackHandlerProviders
operator|.
name|add
argument_list|(
operator|new
name|CallbackHandlerProviderAuthPol
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|callbackHandlerProviders
operator|.
name|add
argument_list|(
operator|new
name|CallbackHandlerProviderUsernameToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setContextName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|contextName
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|String
name|getContextName
parameter_list|()
block|{
return|return
name|contextName
return|;
block|}
annotation|@
name|Deprecated
specifier|public
name|void
name|setRolePrefix
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|setRoleClassifier
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setRoleClassifier
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|roleClassifier
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|String
name|getRoleClassifier
parameter_list|()
block|{
return|return
name|roleClassifier
return|;
block|}
specifier|public
name|void
name|setRoleClassifierType
parameter_list|(
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
operator|!
name|ROLE_CLASSIFIER_PREFIX
operator|.
name|equals
argument_list|(
name|value
argument_list|)
operator|&&
operator|!
name|ROLE_CLASSIFIER_CLASS_NAME
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported role classifier"
argument_list|)
throw|;
block|}
name|roleClassifierType
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|String
name|getRoleClassifierType
parameter_list|()
block|{
return|return
name|roleClassifierType
return|;
block|}
specifier|public
name|void
name|setReportFault
parameter_list|(
name|boolean
name|reportFault
parameter_list|)
block|{
name|this
operator|.
name|reportFault
operator|=
name|reportFault
expr_stmt|;
block|}
specifier|public
name|void
name|setUseDoAs
parameter_list|(
name|boolean
name|useDoAs
parameter_list|)
block|{
name|this
operator|.
name|useDoAs
operator|=
name|useDoAs
expr_stmt|;
block|}
specifier|private
name|CallbackHandler
name|getFirstCallbackHandler
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
for|for
control|(
name|CallbackHandlerProvider
name|cbp
range|:
name|callbackHandlerProviders
control|)
block|{
name|CallbackHandler
name|cbh
init|=
name|cbp
operator|.
name|create
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|cbh
operator|!=
literal|null
condition|)
block|{
return|return
name|cbh
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
specifier|final
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|CallbackHandler
name|handler
init|=
name|getFirstCallbackHandler
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|handler
operator|==
literal|null
operator|&&
operator|!
name|allowAnonymous
condition|)
block|{
throw|throw
operator|new
name|AuthenticationException
argument_list|(
literal|"Authentication required but no authentication information was supplied"
argument_list|)
throw|;
block|}
try|try
block|{
name|LoginContext
name|ctx
init|=
operator|new
name|LoginContext
argument_list|(
name|getContextName
argument_list|()
argument_list|,
literal|null
argument_list|,
name|handler
argument_list|,
name|loginConfig
argument_list|)
decl_stmt|;
name|ctx
operator|.
name|login
argument_list|()
expr_stmt|;
name|Subject
name|subject
init|=
name|ctx
operator|.
name|getSubject
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|getUsername
argument_list|(
name|handler
argument_list|)
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|,
name|createSecurityContext
argument_list|(
name|name
argument_list|,
name|subject
argument_list|)
argument_list|)
expr_stmt|;
comment|// Run the further chain in the context of this subject.
comment|// This allows other code to retrieve the subject using pure JAAS
if|if
condition|(
name|useDoAs
condition|)
block|{
name|Subject
operator|.
name|doAs
argument_list|(
name|subject
argument_list|,
operator|new
name|PrivilegedAction
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Void
name|run
parameter_list|()
block|{
name|InterceptorChain
name|chain
init|=
name|message
operator|.
name|getInterceptorChain
argument_list|()
decl_stmt|;
if|if
condition|(
name|chain
operator|!=
literal|null
condition|)
block|{
name|chain
operator|.
name|doIntercept
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|LoginException
name|ex
parameter_list|)
block|{
name|String
name|errorMessage
init|=
literal|"Authentication failed: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
name|errorMessage
argument_list|)
expr_stmt|;
if|if
condition|(
name|reportFault
condition|)
block|{
name|AuthenticationException
name|aex
init|=
operator|new
name|AuthenticationException
argument_list|(
name|errorMessage
argument_list|)
decl_stmt|;
name|aex
operator|.
name|initCause
argument_list|(
name|ex
argument_list|)
expr_stmt|;
throw|throw
name|aex
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|AuthenticationException
argument_list|(
literal|"Authentication failed (details can be found in server log)"
argument_list|)
throw|;
block|}
block|}
block|}
specifier|private
name|String
name|getUsername
parameter_list|(
name|CallbackHandler
name|handler
parameter_list|)
block|{
if|if
condition|(
name|handler
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
name|NameCallback
name|usernameCallBack
init|=
operator|new
name|NameCallback
argument_list|(
literal|"user"
argument_list|)
decl_stmt|;
name|handler
operator|.
name|handle
argument_list|(
operator|new
name|Callback
index|[]
block|{
name|usernameCallBack
block|}
argument_list|)
expr_stmt|;
return|return
name|usernameCallBack
operator|.
name|getName
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|protected
name|CallbackHandler
name|getCallbackHandler
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|password
parameter_list|)
block|{
return|return
operator|new
name|NamePasswordCallbackHandler
argument_list|(
name|name
argument_list|,
name|password
argument_list|)
return|;
block|}
specifier|protected
name|SecurityContext
name|createSecurityContext
parameter_list|(
name|String
name|name
parameter_list|,
name|Subject
name|subject
parameter_list|)
block|{
if|if
condition|(
name|getRoleClassifier
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|RolePrefixSecurityContextImpl
argument_list|(
name|subject
argument_list|,
name|getRoleClassifier
argument_list|()
argument_list|,
name|getRoleClassifierType
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|DefaultSecurityContext
argument_list|(
name|name
argument_list|,
name|subject
argument_list|)
return|;
block|}
block|}
specifier|public
name|Configuration
name|getLoginConfig
parameter_list|()
block|{
return|return
name|loginConfig
return|;
block|}
specifier|public
name|void
name|setLoginConfig
parameter_list|(
name|Configuration
name|loginConfig
parameter_list|)
block|{
name|this
operator|.
name|loginConfig
operator|=
name|loginConfig
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|CallbackHandlerProvider
argument_list|>
name|getCallbackHandlerProviders
parameter_list|()
block|{
return|return
name|callbackHandlerProviders
return|;
block|}
specifier|public
name|void
name|setCallbackHandlerProviders
parameter_list|(
name|List
argument_list|<
name|CallbackHandlerProvider
argument_list|>
name|callbackHandlerProviders
parameter_list|)
block|{
name|this
operator|.
name|callbackHandlerProviders
operator|.
name|clear
argument_list|()
expr_stmt|;
name|this
operator|.
name|callbackHandlerProviders
operator|.
name|addAll
argument_list|(
name|callbackHandlerProviders
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addCallbackHandlerProviders
parameter_list|(
name|List
argument_list|<
name|CallbackHandlerProvider
argument_list|>
name|callbackHandlerProviders2
parameter_list|)
block|{
name|this
operator|.
name|callbackHandlerProviders
operator|.
name|addAll
argument_list|(
name|callbackHandlerProviders2
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setAllowAnonymous
parameter_list|(
name|boolean
name|allowAnonymous
parameter_list|)
block|{
name|this
operator|.
name|allowAnonymous
operator|=
name|allowAnonymous
expr_stmt|;
block|}
block|}
end_class

end_unit

