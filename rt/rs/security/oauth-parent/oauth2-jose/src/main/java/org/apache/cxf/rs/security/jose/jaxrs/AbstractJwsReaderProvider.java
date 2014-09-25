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
name|security
operator|.
name|interfaces
operator|.
name|RSAPublicKey
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
name|Bus
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
name|JwsSignatureVerifier
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
name|PublicKeyJwsSignatureVerifier
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
name|AbstractJwsReaderProvider
block|{
specifier|private
specifier|static
specifier|final
name|String
name|RSSEC_SIGNATURE_IN_PROPS
init|=
literal|"rs.security.signature.in.properties"
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
name|JwsSignatureVerifier
name|sigVerifier
decl_stmt|;
specifier|private
name|String
name|defaultMediaType
decl_stmt|;
specifier|public
name|void
name|setSignatureVerifier
parameter_list|(
name|JwsSignatureVerifier
name|signatureVerifier
parameter_list|)
block|{
name|this
operator|.
name|sigVerifier
operator|=
name|signatureVerifier
expr_stmt|;
block|}
specifier|protected
name|JwsSignatureVerifier
name|getInitializedSigVerifier
parameter_list|()
block|{
if|if
condition|(
name|sigVerifier
operator|!=
literal|null
condition|)
block|{
return|return
name|sigVerifier
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
name|RSSEC_SIGNATURE_IN_PROPS
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
name|Bus
name|bus
init|=
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getBus
argument_list|()
decl_stmt|;
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
name|bus
argument_list|)
decl_stmt|;
name|JwsSignatureVerifier
name|theVerifier
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
name|CryptoUtils
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
name|KEY_OPER_VERIFY
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
name|theVerifier
operator|=
name|JwsUtils
operator|.
name|getSignatureVerifier
argument_list|(
name|jwk
argument_list|,
name|rsaSignatureAlgo
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|theVerifier
operator|=
operator|new
name|PublicKeyJwsSignatureVerifier
argument_list|(
operator|(
name|RSAPublicKey
operator|)
name|CryptoUtils
operator|.
name|loadPublicKey
argument_list|(
name|m
argument_list|,
name|props
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|theVerifier
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

