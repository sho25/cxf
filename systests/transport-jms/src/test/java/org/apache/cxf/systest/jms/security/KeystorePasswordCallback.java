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
name|jms
operator|.
name|security
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
name|wss4j
operator|.
name|common
operator|.
name|ext
operator|.
name|WSPasswordCallback
import|;
end_import

begin_comment
comment|/**  * A CallbackHandler implementation for keystores.  */
end_comment

begin_class
specifier|public
class|class
name|KeystorePasswordCallback
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
name|KeystorePasswordCallback
parameter_list|()
block|{
name|passwords
operator|.
name|put
argument_list|(
literal|"Alice"
argument_list|,
literal|"abcd!1234"
argument_list|)
expr_stmt|;
name|passwords
operator|.
name|put
argument_list|(
literal|"alice"
argument_list|,
literal|"password"
argument_list|)
expr_stmt|;
name|passwords
operator|.
name|put
argument_list|(
literal|"Bob"
argument_list|,
literal|"abcd!1234"
argument_list|)
expr_stmt|;
name|passwords
operator|.
name|put
argument_list|(
literal|"bob"
argument_list|,
literal|"password"
argument_list|)
expr_stmt|;
name|passwords
operator|.
name|put
argument_list|(
literal|"abcd"
argument_list|,
literal|"dcba"
argument_list|)
expr_stmt|;
name|passwords
operator|.
name|put
argument_list|(
literal|"6e0e88f36ebb8744d470f62f604d03ea4ebe5094"
argument_list|,
literal|"password"
argument_list|)
expr_stmt|;
name|passwords
operator|.
name|put
argument_list|(
literal|"wss40rev"
argument_list|,
literal|"security"
argument_list|)
expr_stmt|;
name|passwords
operator|.
name|put
argument_list|(
literal|"morpit"
argument_list|,
literal|"password"
argument_list|)
expr_stmt|;
block|}
comment|/**      * It attempts to get the password from the private      * alias/passwords map.      */
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
if|if
condition|(
name|pc
operator|.
name|getUsage
argument_list|()
operator|==
name|WSPasswordCallback
operator|.
name|PASSWORD_ENCRYPTOR_PASSWORD
condition|)
block|{
name|pc
operator|.
name|setPassword
argument_list|(
literal|"this-is-a-secret"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
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
name|pc
operator|.
name|setPassword
argument_list|(
literal|"password"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

