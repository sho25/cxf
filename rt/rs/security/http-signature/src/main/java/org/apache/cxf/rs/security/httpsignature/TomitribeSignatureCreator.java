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
name|stream
operator|.
name|Collectors
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

begin_import
import|import
name|org
operator|.
name|tomitribe
operator|.
name|auth
operator|.
name|signatures
operator|.
name|Signature
import|;
end_import

begin_class
specifier|public
class|class
name|TomitribeSignatureCreator
implements|implements
name|SignatureCreator
block|{
specifier|private
specifier|final
name|String
name|signatureAlgorithmName
decl_stmt|;
specifier|private
specifier|final
name|PrivateKey
name|privateKey
decl_stmt|;
specifier|private
specifier|final
name|String
name|keyId
decl_stmt|;
specifier|public
name|TomitribeSignatureCreator
parameter_list|(
name|String
name|signatureAlgorithmName
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
name|signatureAlgorithmName
operator|=
name|signatureAlgorithmName
expr_stmt|;
name|this
operator|.
name|privateKey
operator|=
name|privateKey
expr_stmt|;
name|this
operator|.
name|keyId
operator|=
name|keyId
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|createSignature
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
if|if
condition|(
name|messageHeaders
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"message headers cannot be null"
argument_list|)
throw|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|headers
init|=
name|messageHeaders
operator|.
name|keySet
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|String
operator|::
name|toLowerCase
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
decl_stmt|;
name|headers
operator|.
name|add
argument_list|(
literal|"(request-target)"
argument_list|)
expr_stmt|;
if|if
condition|(
name|keyId
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"key id cannot be null"
argument_list|)
throw|;
block|}
specifier|final
name|Signature
name|signature
init|=
operator|new
name|Signature
argument_list|(
name|keyId
argument_list|,
name|signatureAlgorithmName
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
specifier|final
name|org
operator|.
name|tomitribe
operator|.
name|auth
operator|.
name|signatures
operator|.
name|Signer
name|signer
init|=
operator|new
name|org
operator|.
name|tomitribe
operator|.
name|auth
operator|.
name|signatures
operator|.
name|Signer
argument_list|(
name|privateKey
argument_list|,
name|signature
argument_list|)
decl_stmt|;
return|return
name|signer
operator|.
name|sign
argument_list|(
name|method
argument_list|,
name|uri
argument_list|,
name|SignatureHeaderUtils
operator|.
name|mapHeaders
argument_list|(
name|messageHeaders
argument_list|)
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit
