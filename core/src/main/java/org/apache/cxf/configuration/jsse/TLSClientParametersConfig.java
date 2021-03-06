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
name|configuration
operator|.
name|jsse
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
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|GeneralSecurityException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|KeyManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|TrustManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
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
name|injection
operator|.
name|NoJSR250Annotations
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
name|jaxb
operator|.
name|JAXBContextCache
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
name|jaxb
operator|.
name|JAXBContextCache
operator|.
name|CachedContextAndSchemas
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
name|jaxb
operator|.
name|JAXBUtils
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
name|PackageUtils
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
name|TLSClientParametersType
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
name|staxutils
operator|.
name|StaxUtils
import|;
end_import

begin_comment
comment|/**  * This class provides the TLSClientParameters that programmatically  * configure a HTTPConduit. It is initialized with the JAXB  * type TLSClientParametersType that was used in the Spring configuration  * of the http-conduit bean.  */
end_comment

begin_class
annotation|@
name|NoJSR250Annotations
specifier|public
specifier|final
class|class
name|TLSClientParametersConfig
block|{
specifier|private
specifier|static
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
decl_stmt|;
specifier|private
specifier|static
name|JAXBContext
name|context
decl_stmt|;
specifier|private
name|TLSClientParametersConfig
parameter_list|()
block|{
comment|//not constructed
block|}
specifier|private
specifier|static
specifier|synchronized
name|JAXBContext
name|getContext
parameter_list|()
throws|throws
name|JAXBException
block|{
if|if
condition|(
name|context
operator|==
literal|null
operator|||
name|classes
operator|==
literal|null
condition|)
block|{
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|c2
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|JAXBContextCache
operator|.
name|addPackage
argument_list|(
name|c2
argument_list|,
name|PackageUtils
operator|.
name|getPackageName
argument_list|(
name|TLSClientParametersType
operator|.
name|class
argument_list|)
argument_list|,
name|TLSClientParametersConfig
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
name|CachedContextAndSchemas
name|ccs
init|=
name|JAXBContextCache
operator|.
name|getCachedContextAndSchemas
argument_list|(
name|c2
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|classes
operator|=
name|ccs
operator|.
name|getClasses
argument_list|()
expr_stmt|;
name|context
operator|=
name|ccs
operator|.
name|getContext
argument_list|()
expr_stmt|;
block|}
return|return
name|context
return|;
block|}
specifier|public
specifier|static
name|TLSClientParameters
name|createTLSClientParametersFromType
parameter_list|(
name|TLSClientParametersType
name|params
parameter_list|)
throws|throws
name|GeneralSecurityException
throws|,
name|IOException
block|{
name|TLSClientParameters
name|ret
init|=
operator|new
name|TLSClientParameters
argument_list|()
decl_stmt|;
name|boolean
name|usingDefaults
init|=
name|params
operator|.
name|isUseHttpsURLConnectionDefaultSslSocketFactory
argument_list|()
decl_stmt|;
name|TLSClientParametersTypeInternal
name|iparams
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|params
operator|instanceof
name|TLSClientParametersTypeInternal
condition|)
block|{
name|iparams
operator|=
operator|(
name|TLSClientParametersTypeInternal
operator|)
name|params
expr_stmt|;
block|}
if|if
condition|(
name|params
operator|.
name|isDisableCNCheck
argument_list|()
condition|)
block|{
name|ret
operator|.
name|setDisableCNCheck
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|params
operator|.
name|isUseHttpsURLConnectionDefaultHostnameVerifier
argument_list|()
condition|)
block|{
name|ret
operator|.
name|setUseHttpsURLConnectionDefaultHostnameVerifier
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|params
operator|.
name|isUseHttpsURLConnectionDefaultSslSocketFactory
argument_list|()
condition|)
block|{
name|ret
operator|.
name|setUseHttpsURLConnectionDefaultSslSocketFactory
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|params
operator|.
name|isSetSecureSocketProtocol
argument_list|()
condition|)
block|{
name|ret
operator|.
name|setSecureSocketProtocol
argument_list|(
name|params
operator|.
name|getSecureSocketProtocol
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|params
operator|.
name|isSetCipherSuitesFilter
argument_list|()
condition|)
block|{
name|ret
operator|.
name|setCipherSuitesFilter
argument_list|(
name|params
operator|.
name|getCipherSuitesFilter
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|params
operator|.
name|isSetCipherSuites
argument_list|()
condition|)
block|{
name|ret
operator|.
name|setCipherSuites
argument_list|(
name|params
operator|.
name|getCipherSuites
argument_list|()
operator|.
name|getCipherSuite
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|params
operator|.
name|isSetJsseProvider
argument_list|()
condition|)
block|{
name|ret
operator|.
name|setJsseProvider
argument_list|(
name|params
operator|.
name|getJsseProvider
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|params
operator|.
name|isSetSecureRandomParameters
argument_list|()
operator|&&
operator|!
name|usingDefaults
condition|)
block|{
name|ret
operator|.
name|setSecureRandom
argument_list|(
name|TLSParameterJaxBUtils
operator|.
name|getSecureRandom
argument_list|(
name|params
operator|.
name|getSecureRandomParameters
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|params
operator|.
name|isSetKeyManagers
argument_list|()
operator|&&
operator|!
name|usingDefaults
condition|)
block|{
if|if
condition|(
operator|!
name|params
operator|.
name|isSetCertAlias
argument_list|()
condition|)
block|{
name|ret
operator|.
name|setKeyManagers
argument_list|(
name|TLSParameterJaxBUtils
operator|.
name|getKeyManagers
argument_list|(
name|params
operator|.
name|getKeyManagers
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ret
operator|.
name|setKeyManagers
argument_list|(
name|TLSParameterJaxBUtils
operator|.
name|getKeyManagers
argument_list|(
name|params
operator|.
name|getKeyManagers
argument_list|()
argument_list|,
name|params
operator|.
name|getCertAlias
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|params
operator|.
name|isSetTrustManagers
argument_list|()
operator|&&
operator|!
name|usingDefaults
condition|)
block|{
name|ret
operator|.
name|setTrustManagers
argument_list|(
name|TLSParameterJaxBUtils
operator|.
name|getTrustManagers
argument_list|(
name|params
operator|.
name|getTrustManagers
argument_list|()
argument_list|,
name|params
operator|.
name|isEnableRevocation
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|params
operator|.
name|isSetCertConstraints
argument_list|()
condition|)
block|{
name|ret
operator|.
name|setCertConstraints
argument_list|(
name|params
operator|.
name|getCertConstraints
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|params
operator|.
name|isSetSslCacheTimeout
argument_list|()
condition|)
block|{
name|ret
operator|.
name|setSslCacheTimeout
argument_list|(
name|params
operator|.
name|getSslCacheTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|params
operator|.
name|isSetCertAlias
argument_list|()
condition|)
block|{
name|ret
operator|.
name|setCertAlias
argument_list|(
name|params
operator|.
name|getCertAlias
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|iparams
operator|!=
literal|null
operator|&&
name|iparams
operator|.
name|isSetKeyManagersRef
argument_list|()
operator|&&
operator|!
name|usingDefaults
condition|)
block|{
name|ret
operator|.
name|setKeyManagers
argument_list|(
name|iparams
operator|.
name|getKeyManagersRef
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|iparams
operator|!=
literal|null
operator|&&
name|iparams
operator|.
name|isSetTrustManagersRef
argument_list|()
operator|&&
operator|!
name|usingDefaults
condition|)
block|{
name|ret
operator|.
name|setTrustManagers
argument_list|(
name|iparams
operator|.
name|getTrustManagersRef
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
specifier|public
specifier|static
name|Object
name|createTLSClientParameters
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|StringReader
name|reader
init|=
operator|new
name|StringReader
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|data
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|reader
argument_list|)
decl_stmt|;
try|try
block|{
name|JAXBElement
argument_list|<
name|TLSClientParametersType
argument_list|>
name|type
init|=
name|JAXBUtils
operator|.
name|unmarshall
argument_list|(
name|getContext
argument_list|()
argument_list|,
name|data
argument_list|,
name|TLSClientParametersType
operator|.
name|class
argument_list|)
decl_stmt|;
name|TLSClientParametersType
name|cpt
init|=
name|type
operator|.
name|getValue
argument_list|()
decl_stmt|;
return|return
name|createTLSClientParametersFromType
argument_list|(
name|cpt
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
try|try
block|{
name|StaxUtils
operator|.
name|close
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
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
specifier|public
specifier|static
class|class
name|TLSClientParametersTypeInternal
extends|extends
name|TLSClientParametersType
block|{
specifier|private
name|KeyManager
index|[]
name|keyManagersRef
decl_stmt|;
specifier|private
name|TrustManager
index|[]
name|trustManagersRef
decl_stmt|;
specifier|public
name|KeyManager
index|[]
name|getKeyManagersRef
parameter_list|()
block|{
return|return
name|keyManagersRef
return|;
block|}
specifier|public
name|void
name|setKeyManagersRef
parameter_list|(
name|KeyManager
index|[]
name|keyManagersRef
parameter_list|)
block|{
name|this
operator|.
name|keyManagersRef
operator|=
name|keyManagersRef
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSetKeyManagersRef
parameter_list|()
block|{
return|return
name|this
operator|.
name|keyManagersRef
operator|!=
literal|null
return|;
block|}
specifier|public
name|TrustManager
index|[]
name|getTrustManagersRef
parameter_list|()
block|{
return|return
name|trustManagersRef
return|;
block|}
specifier|public
name|void
name|setTrustManagersRef
parameter_list|(
name|TrustManager
index|[]
name|trustManagersRef
parameter_list|)
block|{
name|this
operator|.
name|trustManagersRef
operator|=
name|trustManagersRef
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSetTrustManagersRef
parameter_list|()
block|{
return|return
name|this
operator|.
name|trustManagersRef
operator|!=
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

