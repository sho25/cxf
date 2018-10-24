begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|restful
operator|.
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Dispatch
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|handler
operator|.
name|MessageContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|http
operator|.
name|HTTPBinding
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
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
name|staxutils
operator|.
name|StaxUtils
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|Client
block|{
specifier|private
name|Client
parameter_list|()
block|{     }
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_xml_http/wrapped"
argument_list|,
literal|"cutomerservice"
argument_list|)
decl_stmt|;
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_xml_http/wrapped"
argument_list|,
literal|"RestProviderPort"
argument_list|)
decl_stmt|;
name|String
name|endpointAddress
init|=
literal|"http://localhost:9000/customerservice/customer"
decl_stmt|;
comment|// Sent HTTP GET request to query all customer info
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|endpointAddress
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Invoking server through HTTP GET to query all customer info"
argument_list|)
expr_stmt|;
name|InputStream
name|in
init|=
name|url
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|StreamSource
name|source
init|=
operator|new
name|StreamSource
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|printSource
argument_list|(
name|source
argument_list|)
expr_stmt|;
comment|// Sent HTTP GET request to query customer info
name|url
operator|=
operator|new
name|URL
argument_list|(
name|endpointAddress
operator|+
literal|"?id=1234"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Invoking server through HTTP GET to query customer info"
argument_list|)
expr_stmt|;
name|in
operator|=
name|url
operator|.
name|openStream
argument_list|()
expr_stmt|;
name|source
operator|=
operator|new
name|StreamSource
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|printSource
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|Service
name|service
init|=
name|Service
operator|.
name|create
argument_list|(
name|serviceName
argument_list|)
decl_stmt|;
name|service
operator|.
name|addPort
argument_list|(
name|portName
argument_list|,
name|HTTPBinding
operator|.
name|HTTP_BINDING
argument_list|,
name|endpointAddress
argument_list|)
expr_stmt|;
name|Dispatch
argument_list|<
name|DOMSource
argument_list|>
name|dispatcher
init|=
name|service
operator|.
name|createDispatch
argument_list|(
name|portName
argument_list|,
name|DOMSource
operator|.
name|class
argument_list|,
name|Service
operator|.
name|Mode
operator|.
name|PAYLOAD
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|requestContext
init|=
name|dispatcher
operator|.
name|getRequestContext
argument_list|()
decl_stmt|;
name|Client
name|client
init|=
operator|new
name|Client
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
name|client
operator|.
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/CustomerJohnReq.xml"
argument_list|)
decl_stmt|;
name|Document
name|doc
init|=
name|StaxUtils
operator|.
name|read
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|DOMSource
name|reqMsg
init|=
operator|new
name|DOMSource
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|requestContext
operator|.
name|put
argument_list|(
name|MessageContext
operator|.
name|HTTP_REQUEST_METHOD
argument_list|,
literal|"POST"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Invoking through HTTP POST to update customer using JAX-WS Dispatch"
argument_list|)
expr_stmt|;
name|DOMSource
name|result
init|=
name|dispatcher
operator|.
name|invoke
argument_list|(
name|reqMsg
argument_list|)
decl_stmt|;
name|printSource
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Client Invoking succeeded!"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|printSource
parameter_list|(
name|Source
name|source
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"**** Response ******"
argument_list|)
expr_stmt|;
try|try
block|{
name|StaxUtils
operator|.
name|copy
argument_list|(
name|source
argument_list|,
name|System
operator|.
name|out
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

