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
name|jose
operator|.
name|jws
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FilterInputStream
import|;
end_import

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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|ByteBuffer
import|;
end_import

begin_class
specifier|public
class|class
name|JwsInputStream
extends|extends
name|FilterInputStream
block|{
specifier|private
name|JwsVerificationSignature
name|signature
decl_stmt|;
specifier|private
name|byte
index|[]
name|signatureBytes
decl_stmt|;
specifier|private
name|boolean
name|verifyOnLastRead
decl_stmt|;
specifier|public
name|JwsInputStream
parameter_list|(
name|InputStream
name|out
parameter_list|,
name|JwsVerificationSignature
name|signature
parameter_list|,
name|byte
index|[]
name|signatureBytes
parameter_list|,
name|boolean
name|verifyOnLastRead
parameter_list|)
block|{
name|super
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|this
operator|.
name|signature
operator|=
name|signature
expr_stmt|;
name|this
operator|.
name|signatureBytes
operator|=
name|signatureBytes
expr_stmt|;
name|this
operator|.
name|verifyOnLastRead
operator|=
name|verifyOnLastRead
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|read
parameter_list|()
throws|throws
name|IOException
block|{
name|int
name|value
init|=
name|super
operator|.
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|!=
operator|-
literal|1
condition|)
block|{
name|byte
index|[]
name|bytes
init|=
name|ByteBuffer
operator|.
name|allocate
argument_list|(
name|Integer
operator|.
name|SIZE
operator|/
literal|8
argument_list|)
operator|.
name|putInt
argument_list|(
name|value
argument_list|)
operator|.
name|array
argument_list|()
decl_stmt|;
name|signature
operator|.
name|update
argument_list|(
name|bytes
argument_list|,
literal|0
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|verify
argument_list|()
expr_stmt|;
block|}
return|return
name|value
return|;
block|}
specifier|public
name|int
name|read
parameter_list|(
name|byte
name|b
index|[]
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|num
init|=
name|in
operator|.
name|read
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
decl_stmt|;
if|if
condition|(
name|num
operator|!=
operator|-
literal|1
condition|)
block|{
name|signature
operator|.
name|update
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|num
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|verify
argument_list|()
expr_stmt|;
block|}
return|return
name|num
return|;
block|}
specifier|private
name|void
name|verify
parameter_list|()
block|{
if|if
condition|(
name|verifyOnLastRead
operator|&&
operator|!
name|signature
operator|.
name|verify
argument_list|(
name|signatureBytes
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|JwsException
argument_list|(
name|JwsException
operator|.
name|Error
operator|.
name|INVALID_SIGNATURE
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

