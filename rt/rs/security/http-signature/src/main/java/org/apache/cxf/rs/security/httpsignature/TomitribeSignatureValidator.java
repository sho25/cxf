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
name|security
operator|.
name|Key
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
name|logging
operator|.
name|Logger
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
name|logging
operator|.
name|LogUtils
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
name|exception
operator|.
name|DifferentAlgorithmsException
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
name|exception
operator|.
name|InvalidDataToVerifySignatureException
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
name|exception
operator|.
name|InvalidSignatureException
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
name|exception
operator|.
name|InvalidSignatureHeaderException
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
name|provider
operator|.
name|AlgorithmProvider
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
name|provider
operator|.
name|PublicKeyProvider
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
name|provider
operator|.
name|SecurityProvider
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
name|Verifier
import|;
end_import

begin_class
specifier|public
class|class
name|TomitribeSignatureValidator
implements|implements
name|SignatureValidator
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|TomitribeSignatureValidator
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|validate
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
name|AlgorithmProvider
name|algorithmProvider
parameter_list|,
name|PublicKeyProvider
name|publicKeyProvider
parameter_list|,
name|SecurityProvider
name|securityProvider
parameter_list|,
name|String
name|method
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
name|Signature
name|signature
init|=
name|extractSignatureFromHeader
argument_list|(
name|messageHeaders
operator|.
name|get
argument_list|(
literal|"Signature"
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|providedAlgorithm
init|=
name|algorithmProvider
operator|.
name|getAlgorithmName
argument_list|(
name|signature
operator|.
name|getKeyId
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|signatureAlgorithm
init|=
name|signature
operator|.
name|getAlgorithm
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|providedAlgorithm
operator|.
name|equals
argument_list|(
name|signatureAlgorithm
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|DifferentAlgorithmsException
argument_list|(
literal|"signature algorithm from header and provided are different"
argument_list|)
throw|;
block|}
name|Key
name|key
init|=
name|publicKeyProvider
operator|.
name|getKey
argument_list|(
name|signature
operator|.
name|getKeyId
argument_list|()
argument_list|)
decl_stmt|;
name|java
operator|.
name|security
operator|.
name|Provider
name|provider
init|=
name|securityProvider
operator|.
name|getProvider
argument_list|(
name|signature
operator|.
name|getKeyId
argument_list|()
argument_list|)
decl_stmt|;
name|runVerifier
argument_list|(
name|messageHeaders
argument_list|,
name|key
argument_list|,
name|signature
argument_list|,
name|provider
argument_list|,
name|method
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|Signature
name|extractSignatureFromHeader
parameter_list|(
name|String
name|signatureString
parameter_list|)
block|{
try|try
block|{
return|return
name|Signature
operator|.
name|fromString
argument_list|(
name|signatureString
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|InvalidSignatureHeaderException
argument_list|(
literal|"failed to parse signature from header"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|runVerifier
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
name|Key
name|key
parameter_list|,
name|Signature
name|signature
parameter_list|,
name|java
operator|.
name|security
operator|.
name|Provider
name|provider
parameter_list|,
name|String
name|method
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
name|Verifier
name|verifier
init|=
operator|new
name|Verifier
argument_list|(
name|key
argument_list|,
name|signature
argument_list|,
name|provider
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Starting signature validation"
argument_list|)
expr_stmt|;
name|boolean
name|success
decl_stmt|;
try|try
block|{
name|success
operator|=
name|verifier
operator|.
name|verify
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
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|InvalidDataToVerifySignatureException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|success
condition|)
block|{
throw|throw
operator|new
name|InvalidSignatureException
argument_list|(
literal|"signature is not valid"
argument_list|)
throw|;
block|}
name|LOG
operator|.
name|fine
argument_list|(
literal|"Finished signature validation"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

