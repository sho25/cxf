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
name|rs
operator|.
name|security
operator|.
name|oauth
operator|.
name|provider
package|;
end_package

begin_import
import|import
name|net
operator|.
name|oauth
operator|.
name|OAuthException
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
name|rt
operator|.
name|security
operator|.
name|crypto
operator|.
name|MessageDigestUtils
import|;
end_import

begin_comment
comment|/**  * The utility MD5 sequence generator which can be used for generating  * request or access token keys and secrets as well as request token  * verifiers  */
end_comment

begin_class
specifier|public
class|class
name|MD5SequenceGenerator
block|{
specifier|public
name|String
name|generate
parameter_list|(
name|byte
index|[]
name|input
parameter_list|)
throws|throws
name|OAuthException
block|{
if|if
condition|(
name|input
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|OAuthException
argument_list|(
literal|"You have to pass input to Token Generator"
argument_list|)
throw|;
block|}
return|return
name|MessageDigestUtils
operator|.
name|generate
argument_list|(
name|input
argument_list|,
name|MessageDigestUtils
operator|.
name|ALGO_MD5
argument_list|)
return|;
block|}
block|}
end_class

end_unit

