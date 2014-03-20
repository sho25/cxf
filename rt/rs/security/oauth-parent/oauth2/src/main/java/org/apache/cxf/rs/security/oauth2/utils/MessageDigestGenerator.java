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
name|utils
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|MessageDigest
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchAlgorithmException
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
name|rs
operator|.
name|security
operator|.
name|oauth2
operator|.
name|provider
operator|.
name|OAuthServiceException
import|;
end_import

begin_comment
comment|/**  * The utility Message Digest generator which can be used for generating  * random values  */
end_comment

begin_class
specifier|public
class|class
name|MessageDigestGenerator
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ALGO_SHA_1
init|=
literal|"SHA-1"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ALGO_SHA_256
init|=
literal|"SHA-256"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ALGO_MD5
init|=
literal|"MD5"
decl_stmt|;
specifier|private
name|String
name|algorithm
init|=
name|ALGO_MD5
decl_stmt|;
specifier|public
name|String
name|generate
parameter_list|(
name|byte
index|[]
name|input
parameter_list|)
throws|throws
name|OAuthServiceException
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
name|OAuthServiceException
argument_list|(
literal|"You have to pass input to Token Generator"
argument_list|)
throw|;
block|}
try|try
block|{
name|byte
index|[]
name|messageDigest
init|=
name|createDigest
argument_list|(
name|input
argument_list|,
name|algorithm
argument_list|)
decl_stmt|;
name|StringBuffer
name|hexString
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|messageDigest
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|hexString
operator|.
name|append
argument_list|(
name|Integer
operator|.
name|toHexString
argument_list|(
literal|0xFF
operator|&
name|messageDigest
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|hexString
operator|.
name|toString
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
literal|"server_error"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|byte
index|[]
name|createDigest
parameter_list|(
name|String
name|input
parameter_list|,
name|String
name|algo
parameter_list|)
block|{
try|try
block|{
return|return
name|createDigest
argument_list|(
name|input
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|,
name|algo
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
literal|"server_error"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
literal|"server_error"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|byte
index|[]
name|createDigest
parameter_list|(
name|byte
index|[]
name|input
parameter_list|,
name|String
name|algo
parameter_list|)
throws|throws
name|NoSuchAlgorithmException
block|{
name|MessageDigest
name|md
init|=
name|MessageDigest
operator|.
name|getInstance
argument_list|(
name|algo
argument_list|)
decl_stmt|;
name|md
operator|.
name|reset
argument_list|()
expr_stmt|;
name|md
operator|.
name|update
argument_list|(
name|input
argument_list|)
expr_stmt|;
return|return
name|md
operator|.
name|digest
argument_list|()
return|;
block|}
specifier|public
name|void
name|setAlgorithm
parameter_list|(
name|String
name|algo
parameter_list|)
block|{
name|this
operator|.
name|algorithm
operator|=
name|algo
expr_stmt|;
block|}
block|}
end_class

end_unit

