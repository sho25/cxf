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
name|io
operator|.
name|InputStream
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
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLClassLoader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|AccessController
import|;
end_import

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
name|helpers
operator|.
name|IOUtils
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
name|CXFAuthenticator
name|instance
decl_stmt|;
specifier|public
name|CXFAuthenticator
parameter_list|()
block|{     }
specifier|public
specifier|static
specifier|synchronized
name|void
name|addAuthenticator
parameter_list|()
block|{
if|if
condition|(
name|instance
operator|==
literal|null
condition|)
block|{
name|instance
operator|=
operator|new
name|CXFAuthenticator
argument_list|()
expr_stmt|;
name|Authenticator
name|wrapped
init|=
literal|null
decl_stmt|;
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
try|try
block|{
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
if|if
condition|(
name|wrapped
operator|!=
literal|null
operator|&&
name|wrapped
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|ReferencingAuthenticator
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|Method
name|m
init|=
name|wrapped
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"check"
argument_list|)
decl_stmt|;
name|m
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|m
operator|.
name|invoke
argument_list|(
name|wrapped
argument_list|)
expr_stmt|;
block|}
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
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//ignore
block|}
block|}
block|}
try|try
block|{
name|ClassLoader
name|loader
init|=
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|PrivilegedAction
argument_list|<
name|ClassLoader
argument_list|>
argument_list|()
block|{
specifier|public
name|ClassLoader
name|run
parameter_list|()
block|{
return|return
operator|new
name|URLClassLoader
argument_list|(
operator|new
name|URL
index|[
literal|0
index|]
argument_list|,
name|ClassLoader
operator|.
name|getSystemClassLoader
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Method
name|m
init|=
name|ClassLoader
operator|.
name|class
operator|.
name|getDeclaredMethod
argument_list|(
literal|"defineClass"
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|byte
index|[]
operator|.
expr|class
argument_list|,
name|Integer
operator|.
name|TYPE
argument_list|,
name|Integer
operator|.
name|TYPE
argument_list|)
decl_stmt|;
name|InputStream
name|ins
init|=
name|ReferencingAuthenticator
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"ReferencingAuthenticator.class"
argument_list|)
decl_stmt|;
name|byte
name|b
index|[]
init|=
name|IOUtils
operator|.
name|readBytesFromStream
argument_list|(
name|ins
argument_list|)
decl_stmt|;
name|ReflectionUtil
operator|.
name|setAccessible
argument_list|(
name|m
argument_list|)
operator|.
name|invoke
argument_list|(
name|loader
argument_list|,
name|ReferencingAuthenticator
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|b
argument_list|,
literal|0
argument_list|,
name|b
operator|.
name|length
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
name|loader
operator|.
name|loadClass
argument_list|(
name|ReferencingAuthenticator
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|Authenticator
name|auth
init|=
operator|(
name|Authenticator
operator|)
name|cls
operator|.
name|getConstructor
argument_list|(
name|Authenticator
operator|.
name|class
argument_list|,
name|Authenticator
operator|.
name|class
argument_list|)
operator|.
name|newInstance
argument_list|(
name|instance
argument_list|,
name|wrapped
argument_list|)
decl_stmt|;
if|if
condition|(
name|System
operator|.
name|getSecurityManager
argument_list|()
operator|==
literal|null
condition|)
block|{
name|Authenticator
operator|.
name|setDefault
argument_list|(
name|auth
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|PrivilegedAction
argument_list|<
name|Boolean
argument_list|>
argument_list|()
block|{
specifier|public
name|Boolean
name|run
parameter_list|()
block|{
name|Authenticator
operator|.
name|setDefault
argument_list|(
name|auth
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
try|try
block|{
comment|//clear the acc field that can hold onto the webapp classloader
name|Field
name|f
init|=
name|loader
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredField
argument_list|(
literal|"acc"
argument_list|)
decl_stmt|;
name|ReflectionUtil
operator|.
name|setAccessible
argument_list|(
name|f
argument_list|)
operator|.
name|set
argument_list|(
name|loader
argument_list|,
literal|null
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
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//ignore
block|}
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
if|if
condition|(
literal|"basic"
operator|.
name|equals
argument_list|(
name|getRequestingScheme
argument_list|()
argument_list|)
operator|||
literal|"digest"
operator|.
name|equals
argument_list|(
name|getRequestingScheme
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
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

