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
name|jwe
package|;
end_package

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
name|Base64UrlUtility
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
name|JoseConstants
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
name|JoseType
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
name|jwa
operator|.
name|ContentAlgorithm
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
name|jwa
operator|.
name|KeyAlgorithm
import|;
end_import

begin_class
specifier|public
class|class
name|JweHeaders
extends|extends
name|JoseHeaders
block|{
specifier|private
name|JweHeaders
name|protectedHeaders
decl_stmt|;
specifier|public
name|JweHeaders
parameter_list|()
block|{     }
specifier|public
name|JweHeaders
parameter_list|(
name|JoseType
name|type
parameter_list|)
block|{
name|super
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JweHeaders
parameter_list|(
name|JoseHeaders
name|headers
parameter_list|)
block|{
name|super
argument_list|(
name|headers
operator|.
name|asMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JweHeaders
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|values
parameter_list|)
block|{
name|super
argument_list|(
name|values
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JweHeaders
parameter_list|(
name|KeyAlgorithm
name|keyEncAlgo
parameter_list|,
name|ContentAlgorithm
name|ctEncAlgo
parameter_list|)
block|{
name|this
argument_list|(
name|keyEncAlgo
argument_list|,
name|ctEncAlgo
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JweHeaders
parameter_list|(
name|ContentAlgorithm
name|ctEncAlgo
parameter_list|)
block|{
name|this
argument_list|(
literal|null
argument_list|,
name|ctEncAlgo
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JweHeaders
parameter_list|(
name|ContentAlgorithm
name|ctEncAlgo
parameter_list|,
name|boolean
name|deflate
parameter_list|)
block|{
name|this
argument_list|(
literal|null
argument_list|,
name|ctEncAlgo
argument_list|,
name|deflate
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JweHeaders
parameter_list|(
name|KeyAlgorithm
name|keyEncAlgo
parameter_list|,
name|ContentAlgorithm
name|ctEncAlgo
parameter_list|,
name|boolean
name|deflate
parameter_list|)
block|{
name|init
argument_list|(
name|keyEncAlgo
argument_list|,
name|ctEncAlgo
argument_list|,
name|deflate
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|init
parameter_list|(
name|KeyAlgorithm
name|keyEncAlgo
parameter_list|,
name|ContentAlgorithm
name|ctEncAlgo
parameter_list|,
name|boolean
name|deflate
parameter_list|)
block|{
if|if
condition|(
name|keyEncAlgo
operator|!=
literal|null
condition|)
block|{
name|setKeyEncryptionAlgorithm
argument_list|(
name|keyEncAlgo
argument_list|)
expr_stmt|;
block|}
name|setContentEncryptionAlgorithm
argument_list|(
name|ctEncAlgo
argument_list|)
expr_stmt|;
if|if
condition|(
name|deflate
condition|)
block|{
name|setZipAlgorithm
argument_list|(
name|JoseConstants
operator|.
name|DEFLATE_ZIP_ALGORITHM
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setKeyEncryptionAlgorithm
parameter_list|(
name|KeyAlgorithm
name|algo
parameter_list|)
block|{
name|super
operator|.
name|setAlgorithm
argument_list|(
name|algo
operator|.
name|getJwaName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|KeyAlgorithm
name|getKeyEncryptionAlgorithm
parameter_list|()
block|{
name|String
name|algo
init|=
name|super
operator|.
name|getAlgorithm
argument_list|()
decl_stmt|;
return|return
name|algo
operator|==
literal|null
condition|?
literal|null
else|:
name|KeyAlgorithm
operator|.
name|getAlgorithm
argument_list|(
name|algo
argument_list|)
return|;
block|}
specifier|public
name|void
name|setContentEncryptionAlgorithm
parameter_list|(
name|ContentAlgorithm
name|algo
parameter_list|)
block|{
name|setHeader
argument_list|(
name|JoseConstants
operator|.
name|JWE_HEADER_CONTENT_ENC_ALGORITHM
argument_list|,
name|algo
operator|.
name|getJwaName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ContentAlgorithm
name|getContentEncryptionAlgorithm
parameter_list|()
block|{
name|Object
name|prop
init|=
name|getHeader
argument_list|(
name|JoseConstants
operator|.
name|JWE_HEADER_CONTENT_ENC_ALGORITHM
argument_list|)
decl_stmt|;
return|return
name|prop
operator|==
literal|null
condition|?
literal|null
else|:
name|ContentAlgorithm
operator|.
name|getAlgorithm
argument_list|(
name|prop
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|void
name|setZipAlgorithm
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|setHeader
argument_list|(
name|JoseConstants
operator|.
name|JWE_HEADER_ZIP_ALGORITHM
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getZipAlgorithm
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|getHeader
argument_list|(
name|JoseConstants
operator|.
name|JWE_HEADER_ZIP_ALGORITHM
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|JoseHeaders
name|setHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
return|return
operator|(
name|JoseHeaders
operator|)
name|super
operator|.
name|setHeader
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
return|;
block|}
specifier|public
name|byte
index|[]
name|toCipherAdditionalAuthData
parameter_list|()
block|{
return|return
name|toCipherAdditionalAuthData
argument_list|(
operator|new
name|JoseHeadersReaderWriter
argument_list|()
operator|.
name|headersToJson
argument_list|(
name|this
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|byte
index|[]
name|toCipherAdditionalAuthData
parameter_list|(
name|String
name|headersJson
parameter_list|)
block|{
name|byte
index|[]
name|headerBytes
init|=
name|StringUtils
operator|.
name|toBytesUTF8
argument_list|(
name|headersJson
argument_list|)
decl_stmt|;
name|String
name|base64UrlHeadersInJson
init|=
name|Base64UrlUtility
operator|.
name|encode
argument_list|(
name|headerBytes
argument_list|)
decl_stmt|;
return|return
name|StringUtils
operator|.
name|toBytesASCII
argument_list|(
name|base64UrlHeadersInJson
argument_list|)
return|;
block|}
specifier|public
name|JweHeaders
name|getProtectedHeaders
parameter_list|()
block|{
return|return
name|protectedHeaders
return|;
block|}
specifier|public
name|void
name|setProtectedHeaders
parameter_list|(
name|JweHeaders
name|protectedHeaders
parameter_list|)
block|{
name|this
operator|.
name|protectedHeaders
operator|=
name|protectedHeaders
expr_stmt|;
block|}
block|}
end_class

end_unit

