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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|ServerSocket
import|;
end_import

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
name|Arrays
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
name|Level
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
name|SSLServerSocket
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
name|SSLServerSocketFactory
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
name|mortbay
operator|.
name|jetty
operator|.
name|security
operator|.
name|SslSocketConnector
import|;
end_import

begin_comment
comment|/**  * This class extends the Jetty SslSocketConnector, which allows  * us to configure it more in tune with the JSSE, using KeyManagers  * and TrustManagers. Also, Jetty version 6.1.3 has a bug where  * the Trust store needs a password.  */
end_comment

begin_class
specifier|public
class|class
name|CXFJettySslSocketConnector
extends|extends
name|SslSocketConnector
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
comment|/**      * We create our own socket factory.      */
annotation|@
name|Override
specifier|protected
name|SSLServerSocketFactory
name|createFactory
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|proto
init|=
name|getProtocol
argument_list|()
operator|==
literal|null
condition|?
literal|"TLS"
else|:
name|getProtocol
argument_list|()
decl_stmt|;
name|SSLContext
name|context
init|=
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
name|SSLServerSocketFactory
name|con
init|=
name|context
operator|.
name|getServerSocketFactory
argument_list|()
decl_stmt|;
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
name|setExcludeCipherSuites
argument_list|(
name|cs
argument_list|)
expr_stmt|;
return|return
name|con
return|;
block|}
specifier|protected
name|ServerSocket
name|newServerSocket
parameter_list|(
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|,
name|int
name|backlog
parameter_list|)
throws|throws
name|IOException
block|{
name|ServerSocket
name|sock
init|=
name|super
operator|.
name|newServerSocket
argument_list|(
name|host
argument_list|,
name|port
argument_list|,
name|backlog
argument_list|)
decl_stmt|;
if|if
condition|(
name|sock
operator|instanceof
name|SSLServerSocket
operator|&&
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|INFO
argument_list|)
condition|)
block|{
name|SSLServerSocket
name|sslSock
init|=
operator|(
name|SSLServerSocket
operator|)
name|sock
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"CIPHERSUITES_SET"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|sslSock
operator|.
name|getEnabledCipherSuites
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|sock
return|;
block|}
block|}
end_class

end_unit

