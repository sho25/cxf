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
name|transport
operator|.
name|http
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
name|Field
import|;
end_import

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
name|lang
operator|.
name|reflect
operator|.
name|Modifier
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|Authenticator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|PasswordAuthentication
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
name|message
operator|.
name|Exchange
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
name|PhaseInterceptorChain
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
name|transport
operator|.
name|Conduit
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|CXFAuthenticator
extends|extends
name|Authenticator
block|{
specifier|static
name|Authenticator
name|wrapped
decl_stmt|;
specifier|static
name|boolean
name|setup
decl_stmt|;
specifier|public
name|CXFAuthenticator
parameter_list|()
block|{
try|try
block|{
for|for
control|(
specifier|final
name|Field
name|f
range|:
name|Authenticator
operator|.
name|class
operator|.
name|getDeclaredFields
argument_list|()
control|)
block|{
if|if
condition|(
name|f
operator|.
name|getType
argument_list|()
operator|.
name|equals
argument_list|(
name|Authenticator
operator|.
name|class
argument_list|)
condition|)
block|{
name|ReflectionUtil
operator|.
name|setAccessible
argument_list|(
name|f
argument_list|)
expr_stmt|;
name|wrapped
operator|=
operator|(
name|Authenticator
operator|)
name|f
operator|.
name|get
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
comment|//ignore
block|}
block|}
specifier|public
specifier|static
specifier|synchronized
name|void
name|addAuthenticator
parameter_list|()
block|{
if|if
condition|(
operator|!
name|setup
condition|)
block|{
try|try
block|{
name|Authenticator
operator|.
name|setDefault
argument_list|(
operator|new
name|CXFAuthenticator
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//ignore
block|}
name|setup
operator|=
literal|true
expr_stmt|;
block|}
block|}
specifier|protected
name|PasswordAuthentication
name|getPasswordAuthentication
parameter_list|()
block|{
name|PasswordAuthentication
name|auth
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|wrapped
operator|!=
literal|null
condition|)
block|{
try|try
block|{
for|for
control|(
specifier|final
name|Field
name|f
range|:
name|Authenticator
operator|.
name|class
operator|.
name|getDeclaredFields
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|Modifier
operator|.
name|isStatic
argument_list|(
name|f
operator|.
name|getModifiers
argument_list|()
argument_list|)
condition|)
block|{
name|ReflectionUtil
operator|.
name|setAccessible
argument_list|(
name|f
argument_list|)
expr_stmt|;
name|f
operator|.
name|set
argument_list|(
name|wrapped
argument_list|,
name|f
operator|.
name|get
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|final
name|Method
name|m
init|=
name|Authenticator
operator|.
name|class
operator|.
name|getDeclaredMethod
argument_list|(
literal|"getPasswordAuthentication"
argument_list|)
decl_stmt|;
name|ReflectionUtil
operator|.
name|setAccessible
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|auth
operator|=
operator|(
name|PasswordAuthentication
operator|)
name|m
operator|.
name|invoke
argument_list|(
name|wrapped
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//ignore
block|}
block|}
if|if
condition|(
name|auth
operator|!=
literal|null
condition|)
block|{
return|return
name|auth
return|;
block|}
name|Message
name|m
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|m
operator|!=
literal|null
condition|)
block|{
name|Exchange
name|exchange
init|=
name|m
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|Conduit
name|conduit
init|=
name|exchange
operator|.
name|getConduit
argument_list|(
name|m
argument_list|)
decl_stmt|;
if|if
condition|(
name|conduit
operator|instanceof
name|HTTPConduit
condition|)
block|{
name|HTTPConduit
name|httpConduit
init|=
operator|(
name|HTTPConduit
operator|)
name|conduit
decl_stmt|;
if|if
condition|(
name|getRequestorType
argument_list|()
operator|==
name|RequestorType
operator|.
name|PROXY
operator|&&
name|httpConduit
operator|.
name|getProxyAuthorization
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|un
init|=
name|httpConduit
operator|.
name|getProxyAuthorization
argument_list|()
operator|.
name|getUserName
argument_list|()
decl_stmt|;
name|String
name|pwd
init|=
name|httpConduit
operator|.
name|getProxyAuthorization
argument_list|()
operator|.
name|getPassword
argument_list|()
decl_stmt|;
if|if
condition|(
name|un
operator|!=
literal|null
operator|&&
name|pwd
operator|!=
literal|null
condition|)
block|{
name|auth
operator|=
operator|new
name|PasswordAuthentication
argument_list|(
name|un
argument_list|,
name|pwd
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|getRequestorType
argument_list|()
operator|==
name|RequestorType
operator|.
name|SERVER
operator|&&
name|httpConduit
operator|.
name|getAuthorization
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|un
init|=
name|httpConduit
operator|.
name|getAuthorization
argument_list|()
operator|.
name|getUserName
argument_list|()
decl_stmt|;
name|String
name|pwd
init|=
name|httpConduit
operator|.
name|getAuthorization
argument_list|()
operator|.
name|getPassword
argument_list|()
decl_stmt|;
if|if
condition|(
name|un
operator|!=
literal|null
operator|&&
name|pwd
operator|!=
literal|null
condition|)
block|{
name|auth
operator|=
operator|new
name|PasswordAuthentication
argument_list|(
name|un
argument_list|,
name|pwd
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|// else PhaseInterceptorChain.getCurrentMessage() is null,
comment|// this HTTP call has therefore not been generated by CXF
return|return
name|auth
return|;
block|}
block|}
end_class

end_unit

