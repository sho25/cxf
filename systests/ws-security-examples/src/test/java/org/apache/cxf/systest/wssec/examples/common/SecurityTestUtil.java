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
name|wssec
operator|.
name|examples
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
block|}
end_class

end_unit

