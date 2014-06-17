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
name|jwt
operator|.
name|jaxrs
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
name|io
operator|.
name|InputStream
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|helpers
operator|.
name|IOUtils
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
name|jaxrs
operator|.
name|utils
operator|.
name|JAXRSUtils
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
name|jwe
operator|.
name|JweCryptoProperties
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
name|jwe
operator|.
name|JweDecryptionOutput
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
name|jwe
operator|.
name|JweDecryptor
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
name|jwe
operator|.
name|JweHeaders
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
name|jwe
operator|.
name|WrappedKeyJweDecryptor
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
name|crypto
operator|.
name|CryptoUtils
import|;
end_import

begin_class
specifier|public
class|class
name|AbstractJweDecryptingFilter
block|{
specifier|private
specifier|static
specifier|final
name|String
name|RSSEC_ENCRYPTION_IN_PROPS
init|=
literal|"rs.security.encryption.in.properties"
decl_stmt|;
specifier|private
name|JweDecryptor
name|decryptor
decl_stmt|;
specifier|private
name|JweCryptoProperties
name|cryptoProperties
decl_stmt|;
specifier|private
name|String
name|defaultMediaType
decl_stmt|;
specifier|protected
name|JweDecryptionOutput
name|decrypt
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
name|JweDecryptor
name|theDecryptor
init|=
name|getInitializedDecryptor
argument_list|()
decl_stmt|;
name|JweDecryptionOutput
name|out
init|=
name|theDecryptor
operator|.
name|decrypt
argument_list|(
operator|new
name|String
argument_list|(
name|IOUtils
operator|.
name|readBytesFromStream
argument_list|(
name|is
argument_list|)
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
decl_stmt|;
name|validateHeaders
argument_list|(
name|out
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|out
return|;
block|}
specifier|protected
name|void
name|validateHeaders
parameter_list|(
name|JweHeaders
name|headers
parameter_list|)
block|{
comment|// complete
block|}
specifier|public
name|void
name|setDecryptor
parameter_list|(
name|JweDecryptor
name|decryptor
parameter_list|)
block|{
name|this
operator|.
name|decryptor
operator|=
name|decryptor
expr_stmt|;
block|}
specifier|protected
name|JweDecryptor
name|getInitializedDecryptor
parameter_list|()
block|{
if|if
condition|(
name|decryptor
operator|!=
literal|null
condition|)
block|{
return|return
name|decryptor
return|;
block|}
try|try
block|{
name|PrivateKey
name|pk
init|=
name|CryptoUtils
operator|.
name|loadPrivateKey
argument_list|(
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
argument_list|,
name|RSSEC_ENCRYPTION_IN_PROPS
argument_list|,
name|CryptoUtils
operator|.
name|RSSEC_DECRYPT_KEY_PSWD_PROVIDER
argument_list|)
decl_stmt|;
return|return
operator|new
name|WrappedKeyJweDecryptor
argument_list|(
name|pk
argument_list|,
name|cryptoProperties
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|SecurityException
name|ex
parameter_list|)
block|{
throw|throw
name|ex
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|setCryptoProperties
parameter_list|(
name|JweCryptoProperties
name|cryptoProperties
parameter_list|)
block|{
name|this
operator|.
name|cryptoProperties
operator|=
name|cryptoProperties
expr_stmt|;
block|}
specifier|public
name|String
name|getDefaultMediaType
parameter_list|()
block|{
return|return
name|defaultMediaType
return|;
block|}
specifier|public
name|void
name|setDefaultMediaType
parameter_list|(
name|String
name|defaultMediaType
parameter_list|)
block|{
name|this
operator|.
name|defaultMediaType
operator|=
name|defaultMediaType
expr_stmt|;
block|}
block|}
end_class

end_unit

