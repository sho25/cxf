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
name|oauth2
operator|.
name|grants
operator|.
name|code
package|;
end_package

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
name|Base64UrlUtility
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

begin_class
specifier|public
class|class
name|DigestCodeVerifier
implements|implements
name|CodeVerifierTransformer
block|{
specifier|public
name|String
name|transformCodeVerifier
parameter_list|(
name|String
name|codeVerifier
parameter_list|)
block|{
name|byte
index|[]
name|digest
init|=
name|MessageDigestUtils
operator|.
name|createDigest
argument_list|(
name|codeVerifier
argument_list|,
name|MessageDigestUtils
operator|.
name|ALGO_SHA_256
argument_list|)
decl_stmt|;
return|return
name|Base64UrlUtility
operator|.
name|encode
argument_list|(
name|digest
argument_list|)
return|;
block|}
block|}
end_class

end_unit

