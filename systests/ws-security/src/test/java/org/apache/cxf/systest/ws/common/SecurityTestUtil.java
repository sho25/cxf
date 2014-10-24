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
name|common
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|Cipher
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|SecretKey
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|spec
operator|.
name|SecretKeySpec
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|BindingProvider
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
name|ws
operator|.
name|security
operator|.
name|SecurityConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|example
operator|.
name|contract
operator|.
name|doubleit
operator|.
name|DoubleItPortType
import|;
end_import

begin_comment
comment|/**  * A utility class for security tests  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|SecurityTestUtil
block|{
specifier|private
specifier|static
specifier|final
name|boolean
name|UNRESTRICTED_POLICIES_INSTALLED
decl_stmt|;
static|static
block|{
name|boolean
name|ok
init|=
literal|false
decl_stmt|;
try|try
block|{
name|byte
index|[]
name|data
init|=
block|{
literal|0x00
block|,
literal|0x01
block|,
literal|0x02
block|,
literal|0x03
block|,
literal|0x04
block|,
literal|0x05
block|,
literal|0x06
block|,
literal|0x07
block|}
decl_stmt|;
name|SecretKey
name|key192
init|=
operator|new
name|SecretKeySpec
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|0x00
block|,
literal|0x01
block|,
literal|0x02
block|,
literal|0x03
block|,
literal|0x04
block|,
literal|0x05
block|,
literal|0x06
block|,
literal|0x07
block|,
literal|0x08
block|,
literal|0x09
block|,
literal|0x0a
block|,
literal|0x0b
block|,
literal|0x0c
block|,
literal|0x0d
block|,
literal|0x0e
block|,
literal|0x0f
block|,
literal|0x10
block|,
literal|0x11
block|,
literal|0x12
block|,
literal|0x13
block|,
literal|0x14
block|,
literal|0x15
block|,
literal|0x16
block|,
literal|0x17
block|}
argument_list|,
literal|"AES"
argument_list|)
decl_stmt|;
name|Cipher
name|c
init|=
name|Cipher
operator|.
name|getInstance
argument_list|(
literal|"AES"
argument_list|)
decl_stmt|;
name|c
operator|.
name|init
argument_list|(
name|Cipher
operator|.
name|ENCRYPT_MODE
argument_list|,
name|key192
argument_list|)
expr_stmt|;
name|c
operator|.
name|doFinal
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|ok
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//
block|}
name|UNRESTRICTED_POLICIES_INSTALLED
operator|=
name|ok
expr_stmt|;
block|}
specifier|private
name|SecurityTestUtil
parameter_list|()
block|{
comment|// complete
block|}
specifier|public
specifier|static
name|void
name|cleanup
parameter_list|()
block|{
name|String
name|tmpDir
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.io.tmpdir"
argument_list|)
decl_stmt|;
if|if
condition|(
name|tmpDir
operator|!=
literal|null
condition|)
block|{
name|File
index|[]
name|tmpFiles
init|=
operator|new
name|File
argument_list|(
name|tmpDir
argument_list|)
operator|.
name|listFiles
argument_list|()
decl_stmt|;
if|if
condition|(
name|tmpFiles
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|File
name|tmpFile
range|:
name|tmpFiles
control|)
block|{
if|if
condition|(
name|tmpFile
operator|.
name|exists
argument_list|()
operator|&&
operator|(
name|tmpFile
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"ws-security.nonce.cache"
argument_list|)
operator|||
name|tmpFile
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"wss4j-nonce-cache"
argument_list|)
operator|||
name|tmpFile
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"ws-security.timestamp.cache"
argument_list|)
operator|||
name|tmpFile
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"wss4j-timestamp-cache"
argument_list|)
operator|)
condition|)
block|{
name|tmpFile
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
specifier|public
specifier|static
name|boolean
name|checkUnrestrictedPoliciesInstalled
parameter_list|()
block|{
return|return
name|UNRESTRICTED_POLICIES_INSTALLED
return|;
block|}
specifier|public
specifier|static
name|void
name|enableStreaming
parameter_list|(
name|DoubleItPortType
name|port
parameter_list|)
block|{
operator|(
operator|(
name|BindingProvider
operator|)
name|port
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|ENABLE_STREAMING_SECURITY
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|port
operator|)
operator|.
name|getResponseContext
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|ENABLE_STREAMING_SECURITY
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

