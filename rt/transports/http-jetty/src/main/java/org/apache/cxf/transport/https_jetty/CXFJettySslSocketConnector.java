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
name|https_jetty
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|SSLContext
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
name|logging
operator|.
name|LogUtils
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
name|ReflectionInvokationHandler
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
name|ClientAuthentication
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|https
operator|.
name|SSLUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|ssl
operator|.
name|SslSelectChannelConnector
import|;
end_import

begin_comment
comment|/**  * This class extends the Jetty SslSelectChannelConnector, which allows  * us to configure it more in tune with the JSSE, using KeyManagers  * and TrustManagers.   */
end_comment

begin_class
specifier|public
class|class
name|CXFJettySslSocketConnector
extends|extends
name|SslSelectChannelConnector
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|CXFJettySslSocketConnector
operator|.
name|class
argument_list|)
decl_stmt|;
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
name|SecureRandom
name|secureRandom
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|cipherSuites
decl_stmt|;
specifier|protected
name|FiltersType
name|cipherSuitesFilter
decl_stmt|;
comment|/**      * Set the cipherSuites      */
specifier|protected
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
name|cipherSuites
operator|=
name|cs
expr_stmt|;
block|}
comment|/**      * Set the CipherSuites Filter      */
specifier|protected
name|void
name|setCipherSuitesFilter
parameter_list|(
name|FiltersType
name|filter
parameter_list|)
block|{
name|cipherSuitesFilter
operator|=
name|filter
expr_stmt|;
block|}
comment|/**      * Set the KeyManagers.      */
specifier|protected
name|void
name|setKeyManagers
parameter_list|(
name|KeyManager
index|[]
name|kmgrs
parameter_list|)
block|{
name|keyManagers
operator|=
name|kmgrs
expr_stmt|;
block|}
comment|/**      * Set the TrustManagers.      */
specifier|protected
name|void
name|setTrustManagers
parameter_list|(
name|TrustManager
index|[]
name|tmgrs
parameter_list|)
block|{
name|trustManagers
operator|=
name|tmgrs
expr_stmt|;
block|}
comment|/**      * Set the SecureRandom Parameters      */
specifier|protected
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
comment|/**      * Set the ClientAuthentication (from the JAXB type) that      * configures an HTTP Destination.      */
specifier|protected
name|void
name|setClientAuthentication
parameter_list|(
name|ClientAuthentication
name|clientAuth
parameter_list|)
block|{
name|getCxfSslContextFactory
argument_list|()
operator|.
name|setWantClientAuth
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|clientAuth
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|clientAuth
operator|.
name|isSetWant
argument_list|()
condition|)
block|{
name|getCxfSslContextFactory
argument_list|()
operator|.
name|setWantClientAuth
argument_list|(
name|clientAuth
operator|.
name|isWant
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|clientAuth
operator|.
name|isSetRequired
argument_list|()
condition|)
block|{
name|getCxfSslContextFactory
argument_list|()
operator|.
name|setNeedClientAuth
argument_list|(
name|clientAuth
operator|.
name|isRequired
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// setup the create SSLContext on the SSLContextFactory
name|getCxfSslContextFactory
argument_list|()
operator|.
name|setSslContext
argument_list|(
name|createSSLContext
argument_list|()
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|SSLContext
name|createSSLContext
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|proto
init|=
name|getCxfSslContextFactory
argument_list|()
operator|.
name|getProtocol
argument_list|()
operator|==
literal|null
condition|?
literal|"TLS"
else|:
name|getCxfSslContextFactory
argument_list|()
operator|.
name|getProtocol
argument_list|()
decl_stmt|;
name|SSLContext
name|context
init|=
name|getCxfSslContextFactory
argument_list|()
operator|.
name|getProvider
argument_list|()
operator|==
literal|null
condition|?
name|SSLContext
operator|.
name|getInstance
argument_list|(
name|proto
argument_list|)
else|:
name|SSLContext
operator|.
name|getInstance
argument_list|(
name|proto
argument_list|,
name|getCxfSslContextFactory
argument_list|()
operator|.
name|getProvider
argument_list|()
argument_list|)
decl_stmt|;
name|context
operator|.
name|init
argument_list|(
name|keyManagers
argument_list|,
name|trustManagers
argument_list|,
name|secureRandom
argument_list|)
expr_stmt|;
name|String
index|[]
name|cs
init|=
name|SSLUtils
operator|.
name|getCiphersuites
argument_list|(
name|cipherSuites
argument_list|,
name|SSLUtils
operator|.
name|getServerSupportedCipherSuites
argument_list|(
name|context
argument_list|)
argument_list|,
name|cipherSuitesFilter
argument_list|,
name|LOG
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|getCxfSslContextFactory
argument_list|()
operator|.
name|setExcludeCipherSuites
argument_list|(
name|cs
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
specifier|public
name|CxfSslContextFactory
name|getCxfSslContextFactory
parameter_list|()
block|{
try|try
block|{
name|Object
name|o
init|=
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getSslContextFactory"
argument_list|)
operator|.
name|invoke
argument_list|(
name|this
argument_list|)
decl_stmt|;
return|return
name|ReflectionInvokationHandler
operator|.
name|createProxyWrapper
argument_list|(
name|o
argument_list|,
name|CxfSslContextFactory
operator|.
name|class
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//ignore, the NPE is fine
block|}
return|return
literal|null
return|;
block|}
interface|interface
name|CxfSslContextFactory
block|{
name|void
name|setExcludeCipherSuites
parameter_list|(
name|String
modifier|...
name|cs
parameter_list|)
function_decl|;
name|String
name|getProtocol
parameter_list|()
function_decl|;
name|String
name|getProvider
parameter_list|()
function_decl|;
name|void
name|setSslContext
parameter_list|(
name|SSLContext
name|createSSLContext
parameter_list|)
function_decl|;
name|void
name|setNeedClientAuth
parameter_list|(
name|boolean
name|required
parameter_list|)
function_decl|;
name|void
name|setWantClientAuth
parameter_list|(
name|boolean
name|want
parameter_list|)
function_decl|;
name|void
name|setProtocol
parameter_list|(
name|String
name|secureSocketProtocol
parameter_list|)
function_decl|;
name|void
name|setProvider
parameter_list|(
name|String
name|jsseProvider
parameter_list|)
function_decl|;
block|}
block|}
end_class

end_unit

