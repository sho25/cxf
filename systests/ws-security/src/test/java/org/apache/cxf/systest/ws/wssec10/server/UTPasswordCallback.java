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
name|ws
operator|.
name|wssec10
operator|.
name|server
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|Map
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
name|UnsupportedCallbackException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|WSPasswordCallback
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|UTPasswordCallback
implements|implements
name|CallbackHandler
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|passwords
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|UTPasswordCallback
parameter_list|()
block|{
name|passwords
operator|.
name|put
argument_list|(
literal|"Alice"
argument_list|,
literal|"ecilA"
argument_list|)
expr_stmt|;
name|passwords
operator|.
name|put
argument_list|(
literal|"Frank"
argument_list|,
literal|"invalid-password"
argument_list|)
expr_stmt|;
comment|//for MS clients
name|passwords
operator|.
name|put
argument_list|(
literal|"abcd"
argument_list|,
literal|"dcba"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Here, we attempt to get the password from the private       * alias/passwords map.      */
specifier|public
name|void
name|handle
parameter_list|(
name|Callback
index|[]
name|callbacks
parameter_list|)
throws|throws
name|IOException
throws|,
name|UnsupportedCallbackException
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|callbacks
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|WSPasswordCallback
name|pc
init|=
operator|(
name|WSPasswordCallback
operator|)
name|callbacks
index|[
name|i
index|]
decl_stmt|;
name|String
name|pass
init|=
name|passwords
operator|.
name|get
argument_list|(
name|pc
operator|.
name|getIdentifier
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|pass
operator|!=
literal|null
condition|)
block|{
name|pc
operator|.
name|setPassword
argument_list|(
name|pass
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
comment|/**      * Add an alias/password pair to the callback mechanism.      */
specifier|public
name|void
name|setAliasPassword
parameter_list|(
name|String
name|alias
parameter_list|,
name|String
name|password
parameter_list|)
block|{
name|passwords
operator|.
name|put
argument_list|(
name|alias
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

