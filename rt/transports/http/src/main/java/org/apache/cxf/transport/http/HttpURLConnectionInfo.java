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
name|net
operator|.
name|HttpURLConnection
import|;
end_import

begin_comment
comment|/**  * This class contains the information about the HTTP Connection that  * will be making an HTTP request. This class should be used  * when the getURL().getProtocol() is "http" or "https".  */
end_comment

begin_class
specifier|public
class|class
name|HttpURLConnectionInfo
extends|extends
name|URLConnectionInfo
block|{
specifier|private
specifier|final
name|String
name|httpRequestMethod
decl_stmt|;
comment|/**      * This constructor takes the HttpURLConnection and extracts      * the httpRequestMethod.      */
specifier|public
name|HttpURLConnectionInfo
parameter_list|(
name|HttpURLConnection
name|con
parameter_list|)
block|{
name|super
argument_list|(
name|con
argument_list|)
expr_stmt|;
name|httpRequestMethod
operator|=
name|con
operator|.
name|getRequestMethod
argument_list|()
expr_stmt|;
block|}
comment|/**      * This method returns the request method on the represented      * HttpURLConnection.      */
specifier|public
name|String
name|getHttpRequestMethod
parameter_list|()
block|{
return|return
name|httpRequestMethod
return|;
block|}
block|}
end_class

end_unit

