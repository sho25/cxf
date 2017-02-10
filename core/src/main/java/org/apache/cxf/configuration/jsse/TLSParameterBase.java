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
name|security
operator|.
name|SecureRandom
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|configuration
operator|.
name|security
operator|.
name|CertificateConstraintsType
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
name|FiltersType
import|;
end_import

begin_comment
comment|/**  * This class is the base class for SSL/TLS parameters that are common  * to both client and server sides.  */
end_comment

begin_class
specifier|public
class|class
name|TLSParameterBase
block|{
specifier|protected
name|KeyManager
index|[]
name|keyManagers
decl_stmt|;
specifier|protected
name|TrustManager
index|[]
name|trustManagers
decl_stmt|;
specifier|protected
name|String
name|provider
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|ciphersuites
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|protected
name|FiltersType
name|cipherSuiteFilters
decl_stmt|;
specifier|protected
name|CertificateConstraintsType
name|certConstraints
decl_stmt|;
specifier|protected
name|SecureRandom
name|secureRandom
decl_stmt|;
specifier|protected
name|String
name|protocol
decl_stmt|;
specifier|protected
name|String
name|certAlias
decl_stmt|;
comment|/**      * Set the JSSE provider. If not set,      * it uses system default.      */
specifier|public
specifier|final
name|void
name|setJsseProvider
parameter_list|(
name|String
name|prov
parameter_list|)
block|{
name|provider
operator|=
name|prov
expr_stmt|;
block|}
comment|/**      * Return the JSSE provider.        */
specifier|public
name|String
name|getJsseProvider
parameter_list|()
block|{
return|return
name|provider
return|;
block|}
comment|/**      * Sets the KeyManagers for this endpoint.      * This parameter may be set to null for system default behavior.      */
specifier|public
specifier|final
name|void
name|setKeyManagers
parameter_list|(
name|KeyManager
index|[]
name|keyMgrs
parameter_list|)
block|{
name|keyManagers
operator|=
name|keyMgrs
expr_stmt|;
block|}
comment|/**      * Returns the key managers for the endpoint.      */
specifier|public
name|KeyManager
index|[]
name|getKeyManagers
parameter_list|()
block|{
return|return
name|keyManagers
return|;
block|}
comment|/**      * Sets the TrustManagers associated with this endpoint.      * This parameter may be set to null for system default behavior.      */
specifier|public
specifier|final
name|void
name|setTrustManagers
parameter_list|(
name|TrustManager
index|[]
name|trustMgrs
parameter_list|)
block|{
name|trustManagers
operator|=
name|trustMgrs
expr_stmt|;
block|}
comment|/**      * Returns the TrustManagers associated with the endpoint.      */
specifier|public
name|TrustManager
index|[]
name|getTrustManagers
parameter_list|()
block|{
return|return
name|trustManagers
return|;
block|}
comment|/**      * This parameter sets the cipher suites list to use. If left unset      * it uses system defaults.      */
specifier|public
specifier|final
name|void
name|setCipherSuites
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|cs
parameter_list|)
block|{
name|ciphersuites
operator|=
name|cs
expr_stmt|;
block|}
comment|/**      * Returns the CipherSuites associated with this endpoint.      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getCipherSuites
parameter_list|()
block|{
if|if
condition|(
name|ciphersuites
operator|==
literal|null
condition|)
block|{
name|ciphersuites
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
return|return
name|ciphersuites
return|;
block|}
comment|/**      * This parameter sets the filter to include and/or exclude the       * cipher suites to use from the set list or system defaults.      */
specifier|public
specifier|final
name|void
name|setCipherSuitesFilter
parameter_list|(
name|FiltersType
name|filters
parameter_list|)
block|{
name|cipherSuiteFilters
operator|=
name|filters
expr_stmt|;
block|}
comment|/**      * Returns the cipher suites filter       */
specifier|public
name|FiltersType
name|getCipherSuitesFilter
parameter_list|()
block|{
return|return
name|cipherSuiteFilters
return|;
block|}
comment|/**      * This sets the secure random provider and algorithm. If left unset or set      * to null, it uses the system default.      */
specifier|public
specifier|final
name|void
name|setSecureRandom
parameter_list|(
name|SecureRandom
name|random
parameter_list|)
block|{
name|secureRandom
operator|=
name|random
expr_stmt|;
block|}
comment|/**      * Get the certificate constraints type      */
specifier|public
name|CertificateConstraintsType
name|getCertConstraints
parameter_list|()
block|{
return|return
name|certConstraints
return|;
block|}
comment|/**      * Set the certificate constraints type      */
specifier|public
specifier|final
name|void
name|setCertConstraints
parameter_list|(
name|CertificateConstraintsType
name|constraints
parameter_list|)
block|{
name|certConstraints
operator|=
name|constraints
expr_stmt|;
block|}
comment|/**      * Returns the secure random algorithm.      */
specifier|public
name|SecureRandom
name|getSecureRandom
parameter_list|()
block|{
return|return
name|secureRandom
return|;
block|}
comment|/**      * This sets the protocol to use. The system default is usually      * "TLS".      */
specifier|public
specifier|final
name|void
name|setSecureSocketProtocol
parameter_list|(
name|String
name|proto
parameter_list|)
block|{
name|protocol
operator|=
name|proto
expr_stmt|;
block|}
comment|/**      * Returns the secure socket protocol in use.      */
specifier|public
name|String
name|getSecureSocketProtocol
parameter_list|()
block|{
return|return
name|protocol
return|;
block|}
comment|/**      * This parameter configures the cert alias used on server side      * this is useful when keystore has multiple certs      */
specifier|public
specifier|final
name|void
name|setCertAlias
parameter_list|(
name|String
name|ctAlias
parameter_list|)
block|{
name|certAlias
operator|=
name|ctAlias
expr_stmt|;
block|}
comment|/**      * This parameter retrieves the cert alias specified on server side      */
specifier|public
name|String
name|getCertAlias
parameter_list|()
block|{
return|return
name|certAlias
return|;
block|}
block|}
end_class

end_unit

