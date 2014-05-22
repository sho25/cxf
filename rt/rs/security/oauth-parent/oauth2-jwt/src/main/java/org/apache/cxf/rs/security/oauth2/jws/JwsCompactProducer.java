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
name|jws
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
name|StringUtils
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
name|jwt
operator|.
name|JwtClaims
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
name|jwt
operator|.
name|JwtConstants
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
name|jwt
operator|.
name|JwtHeaders
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
name|jwt
operator|.
name|JwtToken
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
name|jwt
operator|.
name|JwtTokenReaderWriter
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
name|jwt
operator|.
name|JwtTokenWriter
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
name|utils
operator|.
name|Base64UrlUtility
import|;
end_import

begin_class
specifier|public
class|class
name|JwsCompactProducer
block|{
specifier|private
name|JwtTokenWriter
name|writer
init|=
operator|new
name|JwtTokenReaderWriter
argument_list|()
decl_stmt|;
specifier|private
name|JwtToken
name|token
decl_stmt|;
specifier|private
name|String
name|signature
decl_stmt|;
specifier|private
name|String
name|plainRep
decl_stmt|;
specifier|public
name|JwsCompactProducer
parameter_list|(
name|JwtToken
name|token
parameter_list|)
block|{
name|this
argument_list|(
name|token
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JwsCompactProducer
parameter_list|(
name|JwtToken
name|token
parameter_list|,
name|JwtTokenWriter
name|w
parameter_list|)
block|{
name|this
operator|.
name|token
operator|=
name|token
expr_stmt|;
if|if
condition|(
name|w
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|writer
operator|=
name|w
expr_stmt|;
block|}
block|}
specifier|public
name|JwsCompactProducer
parameter_list|(
name|JwtHeaders
name|headers
parameter_list|,
name|JwtClaims
name|claims
parameter_list|)
block|{
name|this
argument_list|(
name|headers
argument_list|,
name|claims
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JwsCompactProducer
parameter_list|(
name|JwtHeaders
name|headers
parameter_list|,
name|JwtClaims
name|claims
parameter_list|,
name|JwtTokenWriter
name|w
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|JwtToken
argument_list|(
name|headers
argument_list|,
name|claims
argument_list|)
argument_list|,
name|w
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getUnsignedEncodedToken
parameter_list|()
block|{
if|if
condition|(
name|plainRep
operator|==
literal|null
condition|)
block|{
name|plainRep
operator|=
name|Base64UrlUtility
operator|.
name|encode
argument_list|(
name|writer
operator|.
name|headersToJson
argument_list|(
name|token
operator|.
name|getHeaders
argument_list|()
argument_list|)
argument_list|)
operator|+
literal|"."
operator|+
name|Base64UrlUtility
operator|.
name|encode
argument_list|(
name|writer
operator|.
name|claimsToJson
argument_list|(
name|token
operator|.
name|getClaims
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|plainRep
return|;
block|}
specifier|public
name|String
name|getSignedEncodedToken
parameter_list|()
block|{
name|boolean
name|noSignature
init|=
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|signature
argument_list|)
decl_stmt|;
if|if
condition|(
name|noSignature
operator|&&
operator|!
name|isPlainText
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Signature is not available"
argument_list|)
throw|;
block|}
return|return
name|getUnsignedEncodedToken
argument_list|()
operator|+
literal|"."
operator|+
operator|(
name|noSignature
condition|?
literal|""
else|:
name|signature
operator|)
return|;
block|}
specifier|public
name|void
name|signWith
parameter_list|(
name|JwsSignatureVerifier
name|signer
parameter_list|)
block|{
name|setSignatureOctets
argument_list|(
name|signer
operator|.
name|sign
argument_list|(
name|token
operator|.
name|getHeaders
argument_list|()
argument_list|,
name|getUnsignedEncodedToken
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setSignatureText
parameter_list|(
name|String
name|sig
parameter_list|)
block|{
name|setEncodedSignature
argument_list|(
name|Base64UrlUtility
operator|.
name|encode
argument_list|(
name|sig
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setSignatureOctets
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
name|setEncodedSignature
argument_list|(
name|Base64UrlUtility
operator|.
name|encode
argument_list|(
name|bytes
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|setEncodedSignature
parameter_list|(
name|String
name|sig
parameter_list|)
block|{
name|this
operator|.
name|signature
operator|=
name|sig
expr_stmt|;
block|}
specifier|private
name|boolean
name|isPlainText
parameter_list|()
block|{
return|return
name|JwtConstants
operator|.
name|PLAIN_TEXT_ALGO
operator|.
name|equals
argument_list|(
name|token
operator|.
name|getHeaders
argument_list|()
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

