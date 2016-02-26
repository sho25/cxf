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
name|http_undertow
package|;
end_package

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|server
operator|.
name|HttpHandler
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|server
operator|.
name|HttpServerExchange
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|util
operator|.
name|Headers
import|;
end_import

begin_class
specifier|public
class|class
name|NotFoundHandler
implements|implements
name|HttpHandler
block|{
annotation|@
name|Override
specifier|public
name|void
name|handleRequest
parameter_list|(
name|HttpServerExchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|setResponseCode
argument_list|(
literal|404
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getResponseHeaders
argument_list|()
operator|.
name|put
argument_list|(
name|Headers
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"text/plain"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getResponseSender
argument_list|()
operator|.
name|send
argument_list|(
literal|"Problem accessing "
operator|+
name|exchange
operator|.
name|getRequestPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

