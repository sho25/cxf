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
name|systest
operator|.
name|http_undertow
package|;
end_package

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
name|transport
operator|.
name|http_undertow
operator|.
name|CXFUndertowHttpHandler
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|security
operator|.
name|api
operator|.
name|AuthenticationMechanism
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|security
operator|.
name|api
operator|.
name|AuthenticationMode
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|security
operator|.
name|handlers
operator|.
name|AuthenticationCallHandler
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|security
operator|.
name|handlers
operator|.
name|AuthenticationConstraintHandler
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|security
operator|.
name|handlers
operator|.
name|AuthenticationMechanismsHandler
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|security
operator|.
name|handlers
operator|.
name|SecurityInitialHandler
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|security
operator|.
name|idm
operator|.
name|IdentityManager
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|security
operator|.
name|impl
operator|.
name|DigestAuthenticationMechanism
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|server
operator|.
name|HttpHandler
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|server
operator|.
name|HttpServerExchange
import|;
end_import

begin_class
specifier|public
class|class
name|UndertowDigestAuthHandler
implements|implements
name|CXFUndertowHttpHandler
block|{
specifier|private
name|HttpHandler
name|next
decl_stmt|;
specifier|private
name|HttpHandler
name|securityHandler
decl_stmt|;
specifier|private
name|IdentityManager
name|identityManager
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|handleRequest
parameter_list|(
name|HttpServerExchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|identityManager
operator|==
literal|null
condition|)
block|{
name|buildIdMgr
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|securityHandler
operator|==
literal|null
condition|)
block|{
name|buildSecurityHandler
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|securityHandler
operator|.
name|handleRequest
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|buildSecurityHandler
parameter_list|()
block|{
name|HttpHandler
name|handler
init|=
name|this
operator|.
name|next
decl_stmt|;
name|handler
operator|=
operator|new
name|AuthenticationCallHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
name|handler
operator|=
operator|new
name|AuthenticationConstraintHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|AuthenticationMechanism
argument_list|>
name|mechanisms
init|=
name|Collections
operator|.
expr|<
name|AuthenticationMechanism
operator|>
name|singletonList
argument_list|(
operator|new
name|DigestAuthenticationMechanism
argument_list|(
literal|"WSRealm"
argument_list|,
literal|"WSDomain"
argument_list|,
literal|"DIGEST"
argument_list|,
name|this
operator|.
name|identityManager
argument_list|)
argument_list|)
decl_stmt|;
name|handler
operator|=
operator|new
name|AuthenticationMechanismsHandler
argument_list|(
name|handler
argument_list|,
name|mechanisms
argument_list|)
expr_stmt|;
name|this
operator|.
name|securityHandler
operator|=
operator|new
name|SecurityInitialHandler
argument_list|(
name|AuthenticationMode
operator|.
name|PRO_ACTIVE
argument_list|,
name|identityManager
argument_list|,
name|handler
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setNext
parameter_list|(
name|HttpHandler
name|nextHandler
parameter_list|)
block|{
name|this
operator|.
name|next
operator|=
name|nextHandler
expr_stmt|;
block|}
specifier|private
name|void
name|buildIdMgr
parameter_list|()
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|char
index|[]
argument_list|>
name|users
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|users
operator|.
name|put
argument_list|(
literal|"ffang"
argument_list|,
literal|"pswd"
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
name|identityManager
operator|=
operator|new
name|MapIdentityManager
argument_list|(
name|users
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

