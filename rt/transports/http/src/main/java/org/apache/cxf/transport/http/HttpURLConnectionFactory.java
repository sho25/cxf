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
name|http
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
name|HttpURLConnection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|Proxy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_comment
comment|/**  * The primary purpose for this interface is to generate HttpURLConnections  * and retrieve information about the connections. This interface is also   * meant to be used as a lower denominator for HttpURLConnections and  * HttpsURLConnections.  */
end_comment

begin_interface
specifier|public
interface|interface
name|HttpURLConnectionFactory
block|{
comment|/**      * Create an HttpURLConnection, proxified if neccessary.      *       * @param proxy The proxy. May be null if connection is not to be proxied.      * @param url The target URL      * @return An appropriate URLConnection      */
name|HttpURLConnection
name|createConnection
parameter_list|(
name|Proxy
name|proxy
parameter_list|,
name|URL
name|url
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * This method returns Connection Info objects for the particular      * connection. The connection must be connected.      * @param con The connection that is the subject of the information object.      * @return The HttpURLConnection Info for the given connection.      * @throws IOException      */
name|HttpURLConnectionInfo
name|getConnectionInfo
parameter_list|(
name|HttpURLConnection
name|connnection
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * @return the protocol that this connection supports (http or https)      */
name|String
name|getProtocol
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

