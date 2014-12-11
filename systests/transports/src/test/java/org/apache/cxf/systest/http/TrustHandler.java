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
name|systest
operator|.
name|http
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
name|transport
operator|.
name|http
operator|.
name|MessageTrustDecider
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
name|URLConnectionInfo
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
name|UntrustedURLConnectionIOException
import|;
end_import

begin_class
specifier|public
class|class
name|TrustHandler
extends|extends
name|MessageTrustDecider
block|{
specifier|public
name|TrustHandler
parameter_list|()
block|{
comment|// Set the logical name.
name|super
argument_list|(
literal|"The System Test Trust Decider"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|establishTrust
parameter_list|(
name|String
name|conduitName
parameter_list|,
name|URLConnectionInfo
name|connectionInfo
parameter_list|,
name|Message
name|message
parameter_list|)
throws|throws
name|UntrustedURLConnectionIOException
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Trust decision for conduit: "
operator|+
name|conduitName
operator|+
literal|" and "
operator|+
name|connectionInfo
operator|.
name|getURI
argument_list|()
argument_list|)
expr_stmt|;
comment|/*if (connectionInfo instanceof HttpURLConnectionInfo) {             HttpURLConnectionInfo c = (HttpURLConnectionInfo) connectionInfo;             System.out.println("Http method: "                     + c.getHttpRequestMethod() + " on " + c.getURI());         }         if (connectionInfo instanceof HttpsURLConnectionInfo) {             HttpsURLConnectionInfo c = (HttpsURLConnectionInfo) connectionInfo;             System.out.println("TLS Connection to: " + c.getURI());             System.out.println("Enabled Cipher: " + c.getEnabledCipherSuite());             System.out.println("Local Principal: " + c.getLocalPrincipal());             System.out.println("Peer Principal: " + c.getPeerPrincipal());         }         */
comment|//throw new UntrustedURLConnectionIOException("No Way Jose");
block|}
block|}
end_class

end_unit

