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
name|https
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
name|Principal
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|Certificate
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
name|HttpsURLConnection
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
name|http
operator|.
name|HttpURLConnectionInfo
import|;
end_import

begin_comment
comment|/**  * This class holds information about the HttpsURLConnection. This  * class should be used when the getURL().getProtocol() is "https".  */
end_comment

begin_class
specifier|public
class|class
name|HttpsURLConnectionInfo
extends|extends
name|HttpURLConnectionInfo
block|{
comment|/**      * This field contains the cipherSuite enabled in the       * HTTPS URLconnection.      */
specifier|protected
specifier|final
name|String
name|enabledCipherSuite
decl_stmt|;
comment|/**      * This field contains the certificates that were used to      * authenticate the connection to the peer.      */
specifier|protected
specifier|final
name|Certificate
index|[]
name|localCertificates
decl_stmt|;
comment|/**      * This field contains the Principal that authenticated to the      * peer.      */
specifier|protected
specifier|final
name|Principal
name|localPrincipal
decl_stmt|;
comment|/**      * This field contains the certificates the server presented      * to authenticate.      */
specifier|protected
specifier|final
name|Certificate
index|[]
name|serverCertificates
decl_stmt|;
comment|/**      * This field contains the Principal that represents the       * authenticated peer.      */
specifier|protected
specifier|final
name|Principal
name|peerPrincipal
decl_stmt|;
comment|/**      * This constructor is used to create the info object      * representing the this HttpsURLConnection.      */
name|HttpsURLConnectionInfo
parameter_list|(
name|HttpsURLConnection
name|connection
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|connection
argument_list|)
expr_stmt|;
name|enabledCipherSuite
operator|=
name|connection
operator|.
name|getCipherSuite
argument_list|()
expr_stmt|;
name|localCertificates
operator|=
name|connection
operator|.
name|getLocalCertificates
argument_list|()
expr_stmt|;
name|localPrincipal
operator|=
name|connection
operator|.
name|getLocalPrincipal
argument_list|()
expr_stmt|;
name|serverCertificates
operator|=
name|connection
operator|.
name|getServerCertificates
argument_list|()
expr_stmt|;
name|peerPrincipal
operator|=
name|connection
operator|.
name|getPeerPrincipal
argument_list|()
expr_stmt|;
block|}
comment|/**      * This method returns the cipher suite employed in this      * HttpsURLConnection.      */
specifier|public
name|String
name|getEnabledCipherSuite
parameter_list|()
block|{
return|return
name|enabledCipherSuite
return|;
block|}
comment|/**      * This method returns the certificates that were used to      * authenticate to the peer.      */
specifier|public
name|Certificate
index|[]
name|getLocalCertificates
parameter_list|()
block|{
return|return
name|localCertificates
return|;
block|}
comment|/**      * This method returns the Princpal that authenticated to      * the peer.      */
specifier|public
name|Principal
name|getLocalPrincipal
parameter_list|()
block|{
return|return
name|localPrincipal
return|;
block|}
comment|/**      * This method returns the certificates presented by the      * peer for authentication.      */
specifier|public
name|Certificate
index|[]
name|getServerCertificates
parameter_list|()
block|{
return|return
name|serverCertificates
return|;
block|}
comment|/**      * This method returns the Principal that represents the      * authenticated peer.      */
specifier|public
name|Principal
name|getPeerPrincipal
parameter_list|()
block|{
return|return
name|peerPrincipal
return|;
block|}
block|}
end_class

end_unit

