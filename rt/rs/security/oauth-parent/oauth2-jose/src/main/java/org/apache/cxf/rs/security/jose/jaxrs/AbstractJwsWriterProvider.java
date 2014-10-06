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
name|jaxrs
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
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
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|interfaces
operator|.
name|RSAPrivateKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|jaxrs
operator|.
name|utils
operator|.
name|ResourceUtils
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
name|MessageUtils
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
name|JwkUtils
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
name|jws
operator|.
name|JwsCompactProducer
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
name|jws
operator|.
name|JwsSignatureProvider
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
name|jws
operator|.
name|JwsUtils
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
name|jws
operator|.
name|PrivateKeyJwsSignatureProvider
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
name|jwt
operator|.
name|JwtHeaders
import|;
end_import

begin_class
specifier|public
class|class
name|AbstractJwsWriterProvider
block|{
specifier|private
specifier|static
specifier|final
name|String
name|RSSEC_SIGNATURE_OUT_PROPS
init|=
literal|"rs.security.signature.out.properties"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RSSEC_SIGNATURE_PROPS
init|=
literal|"rs.security.signature.properties"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|JSON_WEB_SIGNATURE_ALGO_PROP
init|=
literal|"rs.security.jws.content.signature.algorithm"
decl_stmt|;
specifier|private
name|JwsSignatureProvider
name|sigProvider
decl_stmt|;
specifier|public
name|void
name|setSignatureProvider
parameter_list|(
name|JwsSignatureProvider
name|signatureProvider
parameter_list|)
block|{
name|this
operator|.
name|sigProvider
operator|=
name|signatureProvider
expr_stmt|;
block|}
specifier|protected
name|JwsSignatureProvider
name|getInitializedSigProvider
parameter_list|(
name|JwtHeaders
name|headers
parameter_list|)
block|{
if|if
condition|(
name|sigProvider
operator|!=
literal|null
condition|)
block|{
return|return
name|sigProvider
return|;
block|}
name|Message
name|m
init|=
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
name|String
name|propLoc
init|=
operator|(
name|String
operator|)
name|MessageUtils
operator|.
name|getContextualProperty
argument_list|(
name|m
argument_list|,
name|RSSEC_SIGNATURE_OUT_PROPS
argument_list|,
name|RSSEC_SIGNATURE_PROPS
argument_list|)
decl_stmt|;
if|if
condition|(
name|propLoc
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|()
throw|;
block|}
try|try
block|{
name|Properties
name|props
init|=
name|ResourceUtils
operator|.
name|loadProperties
argument_list|(
name|propLoc
argument_list|,
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getBus
argument_list|()
argument_list|)
decl_stmt|;
name|JwsSignatureProvider
name|theSigProvider
init|=
literal|null
decl_stmt|;
name|String
name|rsaSignatureAlgo
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|JwkUtils
operator|.
name|JWK_KEY_STORE_TYPE
operator|.
name|equals
argument_list|(
name|props
operator|.
name|get
argument_list|(
name|KeyManagementUtils
operator|.
name|RSSEC_KEY_STORE_TYPE
argument_list|)
argument_list|)
condition|)
block|{
name|JsonWebKey
name|jwk
init|=
name|JwkUtils
operator|.
name|loadJsonWebKey
argument_list|(
name|m
argument_list|,
name|props
argument_list|,
name|JsonWebKey
operator|.
name|KEY_OPER_SIGN
argument_list|)
decl_stmt|;
name|rsaSignatureAlgo
operator|=
name|getSignatureAlgo
argument_list|(
name|props
argument_list|,
name|jwk
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
name|theSigProvider
operator|=
name|JwsUtils
operator|.
name|getSignatureProvider
argument_list|(
name|jwk
argument_list|,
name|rsaSignatureAlgo
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|rsaSignatureAlgo
operator|=
name|getSignatureAlgo
argument_list|(
name|props
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|RSAPrivateKey
name|pk
init|=
operator|(
name|RSAPrivateKey
operator|)
name|KeyManagementUtils
operator|.
name|loadPrivateKey
argument_list|(
name|m
argument_list|,
name|props
argument_list|,
name|KeyManagementUtils
operator|.
name|RSSEC_SIG_KEY_PSWD_PROVIDER
argument_list|)
decl_stmt|;
name|theSigProvider
operator|=
operator|new
name|PrivateKeyJwsSignatureProvider
argument_list|(
name|pk
argument_list|,
name|rsaSignatureAlgo
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|theSigProvider
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|()
throw|;
block|}
name|headers
operator|.
name|setAlgorithm
argument_list|(
name|rsaSignatureAlgo
argument_list|)
expr_stmt|;
return|return
name|theSigProvider
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
specifier|protected
name|void
name|writeJws
parameter_list|(
name|JwsCompactProducer
name|p
parameter_list|,
name|JwsSignatureProvider
name|theSigProvider
parameter_list|,
name|OutputStream
name|os
parameter_list|)
throws|throws
name|IOException
block|{
name|p
operator|.
name|signWith
argument_list|(
name|theSigProvider
argument_list|)
expr_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|p
operator|.
name|getSignedEncodedJws
argument_list|()
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|getSignatureAlgo
parameter_list|(
name|Properties
name|props
parameter_list|,
name|String
name|algo
parameter_list|)
block|{
return|return
name|algo
operator|==
literal|null
condition|?
name|props
operator|.
name|getProperty
argument_list|(
name|JSON_WEB_SIGNATURE_ALGO_PROP
argument_list|)
else|:
name|algo
return|;
block|}
block|}
end_class

end_unit

