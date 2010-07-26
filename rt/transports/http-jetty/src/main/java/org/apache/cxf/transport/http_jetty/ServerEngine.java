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
name|http_jetty
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|Handler
import|;
end_import

begin_interface
specifier|public
interface|interface
name|ServerEngine
block|{
comment|/**      * Register a servant.      *       * @param url the URL associated with the servant      * @param handler notified on incoming HTTP requests      */
name|void
name|addServant
parameter_list|(
name|URL
name|url
parameter_list|,
name|JettyHTTPHandler
name|handler
parameter_list|)
function_decl|;
comment|/**      * Remove a previously registered servant.      *       * @param url the URL the servant was registered against.      */
name|void
name|removeServant
parameter_list|(
name|URL
name|url
parameter_list|)
function_decl|;
comment|/**      * Get a previously  registered servant.      *       * @param url the associated URL      * @return the HttpHandler if registered      */
name|Handler
name|getServant
parameter_list|(
name|URL
name|url
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

