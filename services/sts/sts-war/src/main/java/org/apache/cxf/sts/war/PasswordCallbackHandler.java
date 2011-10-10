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
name|sts
operator|.
name|war
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

begin_class
specifier|public
class|class
name|PasswordCallbackHandler
implements|implements
name|CallbackHandler
block|{
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
if|if
condition|(
name|callbacks
index|[
name|i
index|]
operator|instanceof
name|WSPasswordCallback
condition|)
block|{
comment|// CXF
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
literal|"alice"
operator|.
name|equals
argument_list|(
name|pc
operator|.
name|getIdentifier
argument_list|()
argument_list|)
condition|)
block|{
name|pc
operator|.
name|setPassword
argument_list|(
literal|"clarinet"
argument_list|)
expr_stmt|;
break|break;
block|}
elseif|else
if|if
condition|(
literal|"bob"
operator|.
name|equals
argument_list|(
name|pc
operator|.
name|getIdentifier
argument_list|()
argument_list|)
condition|)
block|{
name|pc
operator|.
name|setPassword
argument_list|(
literal|"trombone"
argument_list|)
expr_stmt|;
break|break;
block|}
elseif|else
if|if
condition|(
literal|"mystskey"
operator|.
name|equals
argument_list|(
name|pc
operator|.
name|getIdentifier
argument_list|()
argument_list|)
condition|)
block|{
name|pc
operator|.
name|setPassword
argument_list|(
literal|"stskpass"
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

