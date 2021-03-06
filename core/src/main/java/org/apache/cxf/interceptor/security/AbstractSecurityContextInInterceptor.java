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
name|security
operator|.
name|SecurityToken
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
specifier|abstract
class|class
name|AbstractSecurityContextInInterceptor
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
name|AbstractSecurityContextInInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|AbstractSecurityContextInInterceptor
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
block|{
name|SecurityToken
name|token
init|=
name|message
operator|.
name|get
argument_list|(
name|SecurityToken
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|token
operator|==
literal|null
condition|)
block|{
name|reportSecurityException
argument_list|(
literal|"Security Token is not available on the current message"
argument_list|)
expr_stmt|;
block|}
name|SecurityContext
name|context
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
name|context
operator|==
literal|null
operator|||
name|context
operator|.
name|getUserPrincipal
argument_list|()
operator|==
literal|null
condition|)
block|{
name|reportSecurityException
argument_list|(
literal|"User Principal is not available on the current message"
argument_list|)
expr_stmt|;
block|}
name|Subject
name|subject
init|=
literal|null
decl_stmt|;
try|try
block|{
name|subject
operator|=
name|createSubject
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|reportSecurityException
argument_list|(
literal|"Failed Authentication : Subject has not been created, "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|subject
operator|==
literal|null
operator|||
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|reportSecurityException
argument_list|(
literal|"Failed Authentication : Invalid Subject"
argument_list|)
expr_stmt|;
block|}
name|Principal
name|principal
init|=
name|getPrincipal
argument_list|(
name|context
operator|.
name|getUserPrincipal
argument_list|()
argument_list|,
name|subject
argument_list|)
decl_stmt|;
name|SecurityContext
name|sc
init|=
name|createSecurityContext
argument_list|(
name|principal
argument_list|,
name|subject
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
name|sc
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Principal
name|getPrincipal
parameter_list|(
name|Principal
name|originalPrincipal
parameter_list|,
name|Subject
name|subject
parameter_list|)
block|{
name|Principal
index|[]
name|ps
init|=
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|Principal
index|[
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|size
argument_list|()
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|ps
operator|!=
literal|null
operator|&&
name|ps
operator|.
name|length
operator|>
literal|0
operator|&&
operator|!
name|DefaultSecurityContext
operator|.
name|isGroupPrincipal
argument_list|(
name|ps
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
return|return
name|ps
index|[
literal|0
index|]
return|;
block|}
return|return
name|originalPrincipal
return|;
block|}
specifier|protected
name|SecurityContext
name|createSecurityContext
parameter_list|(
name|Principal
name|p
parameter_list|,
name|Subject
name|subject
parameter_list|)
block|{
return|return
operator|new
name|DefaultSecurityContext
argument_list|(
name|p
argument_list|,
name|subject
argument_list|)
return|;
block|}
specifier|protected
specifier|abstract
name|Subject
name|createSubject
parameter_list|(
name|SecurityToken
name|token
parameter_list|)
function_decl|;
specifier|protected
name|void
name|reportSecurityException
parameter_list|(
name|String
name|errorMessage
parameter_list|)
block|{
name|LOG
operator|.
name|severe
argument_list|(
name|errorMessage
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|SecurityException
argument_list|(
name|errorMessage
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

