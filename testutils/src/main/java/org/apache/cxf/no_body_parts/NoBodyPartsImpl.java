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
name|no_body_parts
package|;
end_package

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
name|javax
operator|.
name|jws
operator|.
name|WebService
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
name|Holder
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
name|WebServiceException
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
name|no_body_parts
operator|.
name|types
operator|.
name|Operation1
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
name|no_body_parts
operator|.
name|types
operator|.
name|Operation1Response
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
name|no_body_parts
operator|.
name|wsdl
operator|.
name|NoBodyPartsSEI
import|;
end_import

begin_comment
comment|/**  * Implementation class for NoBodyParts  */
end_comment

begin_class
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"urn:org:apache:cxf:no_body_parts/wsdl"
argument_list|)
specifier|public
class|class
name|NoBodyPartsImpl
implements|implements
name|NoBodyPartsSEI
block|{
specifier|private
name|String
name|md5
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
throws|throws
name|NoSuchAlgorithmException
block|{
name|MessageDigest
name|algorithm
init|=
name|MessageDigest
operator|.
name|getInstance
argument_list|(
literal|"MD5"
argument_list|)
decl_stmt|;
name|algorithm
operator|.
name|reset
argument_list|()
expr_stmt|;
name|algorithm
operator|.
name|update
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
name|byte
name|messageDigest
index|[]
init|=
name|algorithm
operator|.
name|digest
argument_list|()
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
comment|/** {@inheritDoc} */
specifier|public
name|Operation1Response
name|operation1
parameter_list|(
name|Operation1
name|parameters
parameter_list|,
name|Holder
argument_list|<
name|byte
index|[]
argument_list|>
name|mimeAttachment
parameter_list|)
block|{
name|Operation1Response
name|r
init|=
operator|new
name|Operation1Response
argument_list|()
decl_stmt|;
try|try
block|{
name|r
operator|.
name|setStatus
argument_list|(
name|md5
argument_list|(
name|mimeAttachment
operator|.
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|WebServiceException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
name|r
return|;
block|}
block|}
end_class

end_unit

