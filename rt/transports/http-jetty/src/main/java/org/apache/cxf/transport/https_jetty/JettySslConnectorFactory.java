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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|configuration
operator|.
name|jsse
operator|.
name|TLSServerParameters
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
name|http_jetty
operator|.
name|JettyConnectorFactory
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
name|AbstractConnector
import|;
end_import

begin_comment
comment|/**  * This class wraps the JettyConnectorFactory and will create   * TLS enabled acceptors.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|JettySslConnectorFactory
implements|implements
name|JettyConnectorFactory
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
name|TLSServerParameters
name|tlsServerParameters
decl_stmt|;
specifier|public
name|JettySslConnectorFactory
parameter_list|(
name|TLSServerParameters
name|params
parameter_list|)
block|{
name|tlsServerParameters
operator|=
name|params
expr_stmt|;
block|}
comment|/**      * Create a Listener.      *       * @param port the listen port      */
specifier|public
name|AbstractConnector
name|createConnector
parameter_list|(
name|int
name|port
parameter_list|)
block|{
return|return
name|createConnector
argument_list|(
literal|null
argument_list|,
name|port
argument_list|)
return|;
block|}
comment|/**      * Create a Listener.      *       * @param host the host to bind to.  IP address or hostname is allowed. null to bind to all hosts.      * @param port the listen port      */
specifier|public
name|AbstractConnector
name|createConnector
parameter_list|(
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|)
block|{
assert|assert
name|tlsServerParameters
operator|!=
literal|null
assert|;
name|CXFJettySslSocketConnector
name|secureConnector
init|=
operator|new
name|CXFJettySslSocketConnector
argument_list|()
decl_stmt|;
if|if
condition|(
name|host
operator|!=
literal|null
condition|)
block|{
name|secureConnector
operator|.
name|setHost
argument_list|(
name|host
argument_list|)
expr_stmt|;
block|}
name|secureConnector
operator|.
name|setPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|decorateCXFJettySslSocketConnector
argument_list|(
name|secureConnector
argument_list|)
expr_stmt|;
return|return
name|secureConnector
return|;
block|}
comment|/**      * This method sets the security properties for the CXF extension      * of the JettySslConnector.      */
specifier|private
name|void
name|decorateCXFJettySslSocketConnector
parameter_list|(
name|CXFJettySslSocketConnector
name|con
parameter_list|)
block|{
name|con
operator|.
name|setKeyManagers
argument_list|(
name|tlsServerParameters
operator|.
name|getKeyManagers
argument_list|()
argument_list|)
expr_stmt|;
name|con
operator|.
name|setTrustManagers
argument_list|(
name|tlsServerParameters
operator|.
name|getTrustManagers
argument_list|()
argument_list|)
expr_stmt|;
name|con
operator|.
name|setSecureRandom
argument_list|(
name|tlsServerParameters
operator|.
name|getSecureRandom
argument_list|()
argument_list|)
expr_stmt|;
name|con
operator|.
name|setClientAuthentication
argument_list|(
name|tlsServerParameters
operator|.
name|getClientAuthentication
argument_list|()
argument_list|)
expr_stmt|;
name|con
operator|.
name|getSslContextFactory
argument_list|()
operator|.
name|setProtocol
argument_list|(
name|tlsServerParameters
operator|.
name|getSecureSocketProtocol
argument_list|()
argument_list|)
expr_stmt|;
name|con
operator|.
name|getSslContextFactory
argument_list|()
operator|.
name|setProvider
argument_list|(
name|tlsServerParameters
operator|.
name|getJsseProvider
argument_list|()
argument_list|)
expr_stmt|;
name|con
operator|.
name|setCipherSuites
argument_list|(
name|tlsServerParameters
operator|.
name|getCipherSuites
argument_list|()
argument_list|)
expr_stmt|;
name|con
operator|.
name|setCipherSuitesFilter
argument_list|(
name|tlsServerParameters
operator|.
name|getCipherSuitesFilter
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

