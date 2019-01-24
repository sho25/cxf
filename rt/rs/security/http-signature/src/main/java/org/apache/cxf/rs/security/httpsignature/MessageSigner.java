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
name|httpsignature
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
name|security
operator|.
name|PrivateKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|java
operator|.
name|util
operator|.
name|Objects
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
name|httpsignature
operator|.
name|utils
operator|.
name|DefaultSignatureConstants
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
name|httpsignature
operator|.
name|utils
operator|.
name|SignatureHeaderUtils
import|;
end_import

begin_class
specifier|public
class|class
name|MessageSigner
block|{
specifier|private
specifier|final
name|String
name|digestAlgorithmName
decl_stmt|;
specifier|private
name|SignatureCreator
name|signatureCreator
decl_stmt|;
specifier|public
name|MessageSigner
parameter_list|(
name|String
name|signatureAlgorithmName
parameter_list|,
name|String
name|digestAlgorithmName
parameter_list|,
name|PrivateKey
name|privateKey
parameter_list|,
name|String
name|keyId
parameter_list|)
block|{
name|this
operator|.
name|digestAlgorithmName
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|digestAlgorithmName
argument_list|)
expr_stmt|;
name|this
operator|.
name|signatureCreator
operator|=
operator|new
name|TomitribeSignatureCreator
argument_list|(
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|signatureAlgorithmName
argument_list|)
argument_list|,
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|privateKey
argument_list|)
argument_list|,
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|keyId
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MessageSigner
parameter_list|(
name|PrivateKey
name|privateKey
parameter_list|,
name|String
name|keyId
parameter_list|)
block|{
name|this
argument_list|(
name|DefaultSignatureConstants
operator|.
name|SIGNING_ALGORITHM
argument_list|,
name|DefaultSignatureConstants
operator|.
name|DIGEST_ALGORITHM
argument_list|,
name|privateKey
argument_list|,
name|keyId
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|sign
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|messageHeaders
parameter_list|,
name|String
name|uri
parameter_list|,
name|String
name|method
parameter_list|)
throws|throws
name|IOException
block|{
name|SignatureHeaderUtils
operator|.
name|inspectMessageHeaders
argument_list|(
name|messageHeaders
argument_list|)
expr_stmt|;
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|method
argument_list|)
expr_stmt|;
name|messageHeaders
operator|.
name|put
argument_list|(
literal|"Signature"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|signatureCreator
operator|.
name|createSignature
argument_list|(
name|messageHeaders
argument_list|,
name|uri
argument_list|,
name|method
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|sign
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|messageHeaders
parameter_list|,
name|String
name|uri
parameter_list|,
name|String
name|method
parameter_list|,
name|String
name|messageBody
parameter_list|)
throws|throws
name|IOException
block|{
name|SignatureHeaderUtils
operator|.
name|inspectMessageHeaders
argument_list|(
name|messageHeaders
argument_list|)
expr_stmt|;
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|method
argument_list|)
expr_stmt|;
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|messageBody
argument_list|)
expr_stmt|;
name|messageHeaders
operator|.
name|put
argument_list|(
literal|"Digest"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|SignatureHeaderUtils
operator|.
name|createDigestHeader
argument_list|(
name|messageBody
argument_list|,
name|digestAlgorithmName
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|messageHeaders
operator|.
name|put
argument_list|(
literal|"Signature"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|signatureCreator
operator|.
name|createSignature
argument_list|(
name|messageHeaders
argument_list|,
name|uri
argument_list|,
name|method
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
