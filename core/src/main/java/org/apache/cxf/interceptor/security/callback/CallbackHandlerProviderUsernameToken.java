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
operator|.
name|callback
package|;
end_package

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
name|common
operator|.
name|security
operator|.
name|UsernameToken
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
name|NameDigestPasswordCallbackHandler
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
name|NamePasswordCallbackHandler
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

begin_class
specifier|public
class|class
name|CallbackHandlerProviderUsernameToken
implements|implements
name|CallbackHandlerProvider
block|{
annotation|@
name|Override
specifier|public
name|CallbackHandler
name|create
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
operator|!
operator|(
name|token
operator|instanceof
name|UsernameToken
operator|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|UsernameToken
name|ut
init|=
operator|(
name|UsernameToken
operator|)
name|token
decl_stmt|;
if|if
condition|(
name|ut
operator|.
name|getPasswordType
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"PasswordDigest"
argument_list|)
condition|)
block|{
return|return
operator|new
name|NameDigestPasswordCallbackHandler
argument_list|(
name|ut
operator|.
name|getName
argument_list|()
argument_list|,
name|ut
operator|.
name|getPassword
argument_list|()
argument_list|,
name|ut
operator|.
name|getNonce
argument_list|()
argument_list|,
name|ut
operator|.
name|getCreatedTime
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|NamePasswordCallbackHandler
argument_list|(
name|ut
operator|.
name|getName
argument_list|()
argument_list|,
name|ut
operator|.
name|getPassword
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

