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
name|transport
operator|.
name|http
operator|.
name|auth
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
name|net
operator|.
name|URL
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
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|configuration
operator|.
name|security
operator|.
name|AuthorizationPolicy
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
name|message
operator|.
name|Message
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|DigestAuthSupplier
implements|implements
name|HttpAuthSupplier
block|{
specifier|private
specifier|static
specifier|final
name|char
index|[]
name|HEXADECIMAL
init|=
block|{
literal|'0'
block|,
literal|'1'
block|,
literal|'2'
block|,
literal|'3'
block|,
literal|'4'
block|,
literal|'5'
block|,
literal|'6'
block|,
literal|'7'
block|,
literal|'8'
block|,
literal|'9'
block|,
literal|'a'
block|,
literal|'b'
block|,
literal|'c'
block|,
literal|'d'
block|,
literal|'e'
block|,
literal|'f'
block|}
decl_stmt|;
specifier|final
name|MessageDigest
name|md5Helper
decl_stmt|;
name|Map
argument_list|<
name|URL
argument_list|,
name|DigestInfo
argument_list|>
name|authInfo
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|URL
argument_list|,
name|DigestInfo
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|DigestAuthSupplier
parameter_list|()
block|{
name|MessageDigest
name|md
init|=
literal|null
decl_stmt|;
try|try
block|{
name|md
operator|=
name|MessageDigest
operator|.
name|getInstance
argument_list|(
literal|"MD5"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
comment|//ignore - set to null
block|}
name|md5Helper
operator|=
name|md
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      * With digest, the nonce could expire and thus a rechallenge will be issued.      * Thus, we need requests cached to be able to handle that      */
specifier|public
name|boolean
name|requiresRequestCaching
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|String
name|getAuthorization
parameter_list|(
name|AuthorizationPolicy
name|authPolicy
parameter_list|,
name|URL
name|currentURL
parameter_list|,
name|Message
name|message
parameter_list|,
name|String
name|fullHeader
parameter_list|)
block|{
if|if
condition|(
name|fullHeader
operator|==
literal|null
condition|)
block|{
name|DigestInfo
name|di
init|=
name|authInfo
operator|.
name|get
argument_list|(
name|currentURL
argument_list|)
decl_stmt|;
if|if
condition|(
name|di
operator|!=
literal|null
condition|)
block|{
comment|/* Preemptive authentication is only possible if we have a cached                  * challenge                  */
return|return
name|di
operator|.
name|generateAuth
argument_list|(
name|currentURL
operator|.
name|getFile
argument_list|()
argument_list|,
name|authPolicy
operator|.
name|getUserName
argument_list|()
argument_list|,
name|authPolicy
operator|.
name|getPassword
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
name|HttpAuthHeader
name|authHeader
init|=
operator|new
name|HttpAuthHeader
argument_list|(
name|fullHeader
argument_list|)
decl_stmt|;
if|if
condition|(
name|authHeader
operator|.
name|authTypeIsDigest
argument_list|()
operator|&&
name|authPolicy
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|authHeader
operator|.
name|getParams
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"auth"
operator|.
name|equals
argument_list|(
name|map
operator|.
name|get
argument_list|(
literal|"qop"
argument_list|)
argument_list|)
operator|||
operator|!
name|map
operator|.
name|containsKey
argument_list|(
literal|"qop"
argument_list|)
condition|)
block|{
name|DigestInfo
name|di
init|=
operator|new
name|DigestInfo
argument_list|()
decl_stmt|;
name|di
operator|.
name|qop
operator|=
name|map
operator|.
name|get
argument_list|(
literal|"qop"
argument_list|)
expr_stmt|;
name|di
operator|.
name|realm
operator|=
name|map
operator|.
name|get
argument_list|(
literal|"realm"
argument_list|)
expr_stmt|;
name|di
operator|.
name|nonce
operator|=
name|map
operator|.
name|get
argument_list|(
literal|"nonce"
argument_list|)
expr_stmt|;
name|di
operator|.
name|opaque
operator|=
name|map
operator|.
name|get
argument_list|(
literal|"opaque"
argument_list|)
expr_stmt|;
if|if
condition|(
name|map
operator|.
name|containsKey
argument_list|(
literal|"algorithm"
argument_list|)
condition|)
block|{
name|di
operator|.
name|algorithm
operator|=
name|map
operator|.
name|get
argument_list|(
literal|"algorithm"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|map
operator|.
name|containsKey
argument_list|(
literal|"charset"
argument_list|)
condition|)
block|{
name|di
operator|.
name|charset
operator|=
name|map
operator|.
name|get
argument_list|(
literal|"charset"
argument_list|)
expr_stmt|;
block|}
name|di
operator|.
name|method
operator|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|)
expr_stmt|;
if|if
condition|(
name|di
operator|.
name|method
operator|==
literal|null
condition|)
block|{
name|di
operator|.
name|method
operator|=
literal|"POST"
expr_stmt|;
block|}
name|authInfo
operator|.
name|put
argument_list|(
name|currentURL
argument_list|,
name|di
argument_list|)
expr_stmt|;
return|return
name|di
operator|.
name|generateAuth
argument_list|(
name|currentURL
operator|.
name|getFile
argument_list|()
argument_list|,
name|authPolicy
operator|.
name|getUserName
argument_list|()
argument_list|,
name|authPolicy
operator|.
name|getPassword
argument_list|()
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|createCnonce
parameter_list|()
throws|throws
name|UnsupportedEncodingException
block|{
name|String
name|cnonce
init|=
name|Long
operator|.
name|toString
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
name|cnonce
operator|.
name|getBytes
argument_list|(
literal|"US-ASCII"
argument_list|)
decl_stmt|;
synchronized|synchronized
init|(
name|md5Helper
init|)
block|{
name|bytes
operator|=
name|md5Helper
operator|.
name|digest
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
block|}
return|return
name|encode
argument_list|(
name|bytes
argument_list|)
return|;
block|}
class|class
name|DigestInfo
block|{
name|String
name|qop
decl_stmt|;
name|String
name|realm
decl_stmt|;
name|String
name|nonce
decl_stmt|;
name|String
name|opaque
decl_stmt|;
name|int
name|nc
decl_stmt|;
name|String
name|algorithm
init|=
literal|"MD5"
decl_stmt|;
name|String
name|charset
init|=
literal|"ISO-8859-1"
decl_stmt|;
name|String
name|method
init|=
literal|"POST"
decl_stmt|;
specifier|synchronized
name|String
name|generateAuth
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
block|{
try|try
block|{
name|nc
operator|++
expr_stmt|;
name|String
name|ncstring
init|=
name|String
operator|.
name|format
argument_list|(
literal|"%08d"
argument_list|,
name|nc
argument_list|)
decl_stmt|;
name|String
name|cnonce
init|=
name|createCnonce
argument_list|()
decl_stmt|;
name|String
name|digAlg
init|=
name|algorithm
decl_stmt|;
if|if
condition|(
name|digAlg
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"MD5-sess"
argument_list|)
condition|)
block|{
name|digAlg
operator|=
literal|"MD5"
expr_stmt|;
block|}
name|MessageDigest
name|digester
init|=
name|MessageDigest
operator|.
name|getInstance
argument_list|(
name|digAlg
argument_list|)
decl_stmt|;
name|String
name|a1
init|=
name|username
operator|+
literal|":"
operator|+
name|realm
operator|+
literal|":"
operator|+
name|password
decl_stmt|;
if|if
condition|(
literal|"MD5-sess"
operator|.
name|equalsIgnoreCase
argument_list|(
name|algorithm
argument_list|)
condition|)
block|{
name|algorithm
operator|=
literal|"MD5"
expr_stmt|;
name|String
name|tmp2
init|=
name|encode
argument_list|(
name|digester
operator|.
name|digest
argument_list|(
name|a1
operator|.
name|getBytes
argument_list|(
name|charset
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|a1
operator|=
name|tmp2
operator|+
literal|':'
operator|+
name|nonce
operator|+
literal|':'
operator|+
name|cnonce
expr_stmt|;
block|}
name|String
name|hasha1
init|=
name|encode
argument_list|(
name|digester
operator|.
name|digest
argument_list|(
name|a1
operator|.
name|getBytes
argument_list|(
name|charset
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|a2
init|=
name|method
operator|+
literal|":"
operator|+
name|uri
decl_stmt|;
name|String
name|hasha2
init|=
name|encode
argument_list|(
name|digester
operator|.
name|digest
argument_list|(
name|a2
operator|.
name|getBytes
argument_list|(
literal|"US-ASCII"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|serverDigestValue
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|qop
operator|==
literal|null
condition|)
block|{
name|serverDigestValue
operator|=
name|hasha1
operator|+
literal|":"
operator|+
name|nonce
operator|+
literal|":"
operator|+
name|hasha2
expr_stmt|;
block|}
else|else
block|{
name|serverDigestValue
operator|=
name|hasha1
operator|+
literal|":"
operator|+
name|nonce
operator|+
literal|":"
operator|+
name|ncstring
operator|+
literal|":"
operator|+
name|cnonce
operator|+
literal|":"
operator|+
name|qop
operator|+
literal|":"
operator|+
name|hasha2
expr_stmt|;
block|}
name|String
name|response
init|=
name|encode
argument_list|(
name|digester
operator|.
name|digest
argument_list|(
name|serverDigestValue
operator|.
name|getBytes
argument_list|(
literal|"US-ASCII"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|outParams
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|qop
operator|!=
literal|null
condition|)
block|{
name|outParams
operator|.
name|put
argument_list|(
literal|"qop"
argument_list|,
literal|"auth"
argument_list|)
expr_stmt|;
block|}
name|outParams
operator|.
name|put
argument_list|(
literal|"realm"
argument_list|,
name|realm
argument_list|)
expr_stmt|;
name|outParams
operator|.
name|put
argument_list|(
literal|"opaque"
argument_list|,
name|opaque
argument_list|)
expr_stmt|;
name|outParams
operator|.
name|put
argument_list|(
literal|"nonce"
argument_list|,
name|nonce
argument_list|)
expr_stmt|;
name|outParams
operator|.
name|put
argument_list|(
literal|"uri"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|outParams
operator|.
name|put
argument_list|(
literal|"username"
argument_list|,
name|username
argument_list|)
expr_stmt|;
name|outParams
operator|.
name|put
argument_list|(
literal|"nc"
argument_list|,
name|ncstring
argument_list|)
expr_stmt|;
name|outParams
operator|.
name|put
argument_list|(
literal|"cnonce"
argument_list|,
name|cnonce
argument_list|)
expr_stmt|;
name|outParams
operator|.
name|put
argument_list|(
literal|"response"
argument_list|,
name|response
argument_list|)
expr_stmt|;
return|return
operator|new
name|HttpAuthHeader
argument_list|(
name|HttpAuthHeader
operator|.
name|AUTH_TYPE_DIGEST
argument_list|,
name|outParams
argument_list|)
operator|.
name|getFullHeader
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**      * Encodes the 128 bit (16 bytes) MD5 digest into a 32 characters long       *<CODE>String</CODE> according to RFC 2617.      *       * @param binaryData array containing the digest      * @return encoded MD5, or<CODE>null</CODE> if encoding failed      */
specifier|private
specifier|static
name|String
name|encode
parameter_list|(
name|byte
index|[]
name|binaryData
parameter_list|)
block|{
name|int
name|n
init|=
name|binaryData
operator|.
name|length
decl_stmt|;
name|char
index|[]
name|buffer
init|=
operator|new
name|char
index|[
name|n
operator|*
literal|2
index|]
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
name|n
condition|;
name|i
operator|++
control|)
block|{
name|int
name|low
init|=
name|binaryData
index|[
name|i
index|]
operator|&
literal|0x0f
decl_stmt|;
name|int
name|high
init|=
operator|(
name|binaryData
index|[
name|i
index|]
operator|&
literal|0xf0
operator|)
operator|>>
literal|4
decl_stmt|;
name|buffer
index|[
name|i
operator|*
literal|2
index|]
operator|=
name|HEXADECIMAL
index|[
name|high
index|]
expr_stmt|;
name|buffer
index|[
operator|(
name|i
operator|*
literal|2
operator|)
operator|+
literal|1
index|]
operator|=
name|HEXADECIMAL
index|[
name|low
index|]
expr_stmt|;
block|}
return|return
operator|new
name|String
argument_list|(
name|buffer
argument_list|)
return|;
block|}
block|}
end_class

end_unit

