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
name|util
operator|.
name|Collections
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
name|jose
operator|.
name|JoseHeaders
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
name|jose
operator|.
name|JoseHeadersReaderWriter
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
name|jose
operator|.
name|JoseUtils
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
name|jose
operator|.
name|jwk
operator|.
name|JsonWebKey
import|;
end_import

begin_class
specifier|public
class|class
name|JwsJsonSignatureEntry
block|{
specifier|private
name|String
name|encodedJwsPayload
decl_stmt|;
specifier|private
name|String
name|encodedProtectedHeader
decl_stmt|;
specifier|private
name|String
name|encodedSignature
decl_stmt|;
specifier|private
name|JwsJsonProtectedHeader
name|protectedHeader
decl_stmt|;
specifier|private
name|JwsJsonUnprotectedHeader
name|unprotectedHeader
decl_stmt|;
specifier|private
name|JoseHeaders
name|unionHeaders
decl_stmt|;
specifier|public
name|JwsJsonSignatureEntry
parameter_list|(
name|String
name|encodedJwsPayload
parameter_list|,
name|String
name|encodedProtectedHeader
parameter_list|,
name|String
name|encodedSignature
parameter_list|,
name|JwsJsonUnprotectedHeader
name|unprotectedHeader
parameter_list|)
block|{
if|if
condition|(
name|encodedProtectedHeader
operator|==
literal|null
operator|&&
name|unprotectedHeader
operator|==
literal|null
operator|||
name|encodedSignature
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|(
literal|"Invalid security entry"
argument_list|)
throw|;
block|}
name|this
operator|.
name|encodedJwsPayload
operator|=
name|encodedJwsPayload
expr_stmt|;
name|this
operator|.
name|encodedProtectedHeader
operator|=
name|encodedProtectedHeader
expr_stmt|;
name|this
operator|.
name|encodedSignature
operator|=
name|encodedSignature
expr_stmt|;
name|this
operator|.
name|unprotectedHeader
operator|=
name|unprotectedHeader
expr_stmt|;
name|this
operator|.
name|protectedHeader
operator|=
operator|new
name|JwsJsonProtectedHeader
argument_list|(
operator|new
name|JoseHeadersReaderWriter
argument_list|()
operator|.
name|fromJsonHeaders
argument_list|(
name|JoseUtils
operator|.
name|decodeToString
argument_list|(
name|encodedProtectedHeader
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|prepare
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|prepare
parameter_list|()
block|{
name|unionHeaders
operator|=
operator|new
name|JoseHeaders
argument_list|()
expr_stmt|;
if|if
condition|(
name|protectedHeader
operator|!=
literal|null
condition|)
block|{
name|unionHeaders
operator|.
name|asMap
argument_list|()
operator|.
name|putAll
argument_list|(
name|protectedHeader
operator|.
name|getHeaderEntries
argument_list|()
operator|.
name|asMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|unprotectedHeader
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|Collections
operator|.
name|disjoint
argument_list|(
name|unionHeaders
operator|.
name|asMap
argument_list|()
operator|.
name|keySet
argument_list|()
argument_list|,
name|unprotectedHeader
operator|.
name|getHeaderEntries
argument_list|()
operator|.
name|asMap
argument_list|()
operator|.
name|keySet
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|(
literal|"Protected and unprotected headers have duplicate values"
argument_list|)
throw|;
block|}
name|unionHeaders
operator|.
name|asMap
argument_list|()
operator|.
name|putAll
argument_list|(
name|unprotectedHeader
operator|.
name|getHeaderEntries
argument_list|()
operator|.
name|asMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getEncodedJwsPayload
parameter_list|()
block|{
return|return
name|encodedJwsPayload
return|;
block|}
specifier|public
name|String
name|getDecodedJwsPayload
parameter_list|()
block|{
return|return
name|JoseUtils
operator|.
name|decodeToString
argument_list|(
name|encodedJwsPayload
argument_list|)
return|;
block|}
specifier|public
name|byte
index|[]
name|getDecodedJwsPayloadBytes
parameter_list|()
block|{
return|return
name|StringUtils
operator|.
name|toBytesUTF8
argument_list|(
name|getDecodedJwsPayload
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|String
name|getEncodedProtectedHeader
parameter_list|()
block|{
return|return
name|encodedProtectedHeader
return|;
block|}
specifier|public
name|JwsJsonProtectedHeader
name|getProtectedHeader
parameter_list|()
block|{
return|return
name|protectedHeader
return|;
block|}
specifier|public
name|JwsJsonUnprotectedHeader
name|getUnprotectedHeader
parameter_list|()
block|{
return|return
name|unprotectedHeader
return|;
block|}
specifier|public
name|JoseHeaders
name|getUnionHeader
parameter_list|()
block|{
return|return
name|unionHeaders
return|;
block|}
specifier|public
name|String
name|getEncodedSignature
parameter_list|()
block|{
return|return
name|encodedSignature
return|;
block|}
specifier|public
name|byte
index|[]
name|getDecodedSignature
parameter_list|()
block|{
return|return
name|JoseUtils
operator|.
name|decode
argument_list|(
name|getEncodedSignature
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|String
name|getUnsignedEncodedSequence
parameter_list|()
block|{
return|return
name|getEncodedProtectedHeader
argument_list|()
operator|+
literal|"."
operator|+
name|getEncodedJwsPayload
argument_list|()
return|;
block|}
specifier|public
name|String
name|getKeyId
parameter_list|()
block|{
return|return
name|getUnionHeader
argument_list|()
operator|.
name|getKeyId
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|verifySignatureWith
parameter_list|(
name|JwsSignatureVerifier
name|validator
parameter_list|)
block|{
try|try
block|{
return|return
name|validator
operator|.
name|verify
argument_list|(
name|getUnionHeader
argument_list|()
argument_list|,
name|getUnsignedEncodedSequence
argument_list|()
argument_list|,
name|getDecodedSignature
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|SecurityException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|verifySignatureWith
parameter_list|(
name|JsonWebKey
name|key
parameter_list|)
block|{
return|return
name|verifySignatureWith
argument_list|(
name|JwsUtils
operator|.
name|getSignatureVerifier
argument_list|(
name|key
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|String
name|toJson
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"{"
argument_list|)
expr_stmt|;
if|if
condition|(
name|protectedHeader
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"\"protected\":\""
operator|+
name|protectedHeader
operator|.
name|getEncodedHeaderEntries
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|unprotectedHeader
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|protectedHeader
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"\"header\":\""
operator|+
name|unprotectedHeader
operator|.
name|toJson
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\"signature\":\""
operator|+
name|encodedSignature
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"}"
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

