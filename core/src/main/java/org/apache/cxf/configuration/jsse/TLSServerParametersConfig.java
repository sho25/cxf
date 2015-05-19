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
name|security
operator|.
name|GeneralSecurityException
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
name|configuration
operator|.
name|security
operator|.
name|TLSServerParametersType
import|;
end_import

begin_comment
comment|/**  * This class is used by Spring Config to convert the TLSServerParameters  * JAXB generated type into programmatic TLS Server Parameters for the  * configuration of the http-destination.  */
end_comment

begin_class
annotation|@
name|NoJSR250Annotations
specifier|public
class|class
name|TLSServerParametersConfig
extends|extends
name|TLSServerParameters
block|{
specifier|public
name|TLSServerParametersConfig
parameter_list|(
name|TLSServerParametersType
name|params
parameter_list|)
throws|throws
name|GeneralSecurityException
throws|,
name|IOException
block|{
name|TLSServerParametersTypeInternal
name|iparams
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|params
operator|instanceof
name|TLSServerParametersTypeInternal
condition|)
block|{
name|iparams
operator|=
operator|(
name|TLSServerParametersTypeInternal
operator|)
name|params
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
name|this
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
name|this
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
name|this
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
name|isSetExcludeProtocols
argument_list|()
condition|)
block|{
name|this
operator|.
name|setExcludeProtocols
argument_list|(
name|params
operator|.
name|getExcludeProtocols
argument_list|()
operator|.
name|getExcludeProtocol
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|params
operator|.
name|isSetIncludeProtocols
argument_list|()
condition|)
block|{
name|this
operator|.
name|setIncludeProtocols
argument_list|(
name|params
operator|.
name|getIncludeProtocols
argument_list|()
operator|.
name|getIncludeProtocol
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
name|this
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
condition|)
block|{
name|this
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
name|isSetClientAuthentication
argument_list|()
condition|)
block|{
name|this
operator|.
name|setClientAuthentication
argument_list|(
name|params
operator|.
name|getClientAuthentication
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|params
operator|.
name|isSetKeyManagers
argument_list|()
condition|)
block|{
name|this
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
if|if
condition|(
name|params
operator|.
name|isSetTrustManagers
argument_list|()
condition|)
block|{
name|this
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
name|this
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
name|isSetCertAlias
argument_list|()
condition|)
block|{
name|this
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
condition|)
block|{
name|this
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
condition|)
block|{
name|this
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
block|}
specifier|public
specifier|static
class|class
name|TLSServerParametersTypeInternal
extends|extends
name|TLSServerParametersType
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

