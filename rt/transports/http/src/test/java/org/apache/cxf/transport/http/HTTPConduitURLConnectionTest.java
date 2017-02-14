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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
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
name|Bus
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
name|bus
operator|.
name|extension
operator|.
name|ExtensionManagerBus
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
name|jsse
operator|.
name|TLSClientParameters
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
name|message
operator|.
name|MessageImpl
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
name|service
operator|.
name|model
operator|.
name|EndpointInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * This class "tests" the HTTPConduit that uses java.net.HttpURLConnection  * and java.net.HttpsURLConnection for its implementation. Should the  * implementation of HTTPConduit change (i.e. no longer use the URLConnections)  * this test will break.  */
end_comment

begin_class
specifier|public
class|class
name|HTTPConduitURLConnectionTest
extends|extends
name|Assert
block|{
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{     }
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{     }
comment|/**      * Generates a new message.      */
specifier|private
name|Message
name|getNewMessage
parameter_list|()
block|{
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
init|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|(
name|String
operator|.
name|CASE_INSENSITIVE_ORDER
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|contentTypes
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|contentTypes
operator|.
name|add
argument_list|(
literal|"text/xml"
argument_list|)
expr_stmt|;
name|contentTypes
operator|.
name|add
argument_list|(
literal|"charset=utf8"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"content-type"
argument_list|,
name|contentTypes
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|,
name|headers
argument_list|)
expr_stmt|;
return|return
name|message
return|;
block|}
comment|/**      * This test verifies that the "prepare" call places an HttpURLConnection on      * the Message and that its URL matches the endpoint.      */
annotation|@
name|Test
specifier|public
name|void
name|testConnectionURL
parameter_list|()
throws|throws
name|Exception
block|{
name|Bus
name|bus
init|=
operator|new
name|ExtensionManagerBus
argument_list|()
decl_stmt|;
name|EndpointInfo
name|ei
init|=
operator|new
name|EndpointInfo
argument_list|()
decl_stmt|;
name|ei
operator|.
name|setAddress
argument_list|(
literal|"http://nowhere.com/bar/foo"
argument_list|)
expr_stmt|;
name|HTTPConduit
name|conduit
init|=
operator|new
name|URLConnectionHTTPConduit
argument_list|(
name|bus
argument_list|,
name|ei
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|conduit
operator|.
name|finalizeConfig
argument_list|()
expr_stmt|;
name|Message
name|message
init|=
name|getNewMessage
argument_list|()
decl_stmt|;
name|conduit
operator|.
name|prepare
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|HttpURLConnection
name|con
init|=
operator|(
name|HttpURLConnection
operator|)
name|message
operator|.
name|get
argument_list|(
literal|"http.connection"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected URL address"
argument_list|,
name|con
operator|.
name|getURL
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|ei
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * This test verifies that URL used is overridden by having the      * ENDPOINT_ADDRESS set on the Message.      */
annotation|@
name|Test
specifier|public
name|void
name|testConnectionURLOverride
parameter_list|()
throws|throws
name|Exception
block|{
name|Bus
name|bus
init|=
operator|new
name|ExtensionManagerBus
argument_list|()
decl_stmt|;
name|EndpointInfo
name|ei
init|=
operator|new
name|EndpointInfo
argument_list|()
decl_stmt|;
name|ei
operator|.
name|setAddress
argument_list|(
literal|"http://nowhere.null/bar/foo"
argument_list|)
expr_stmt|;
name|HTTPConduit
name|conduit
init|=
operator|new
name|URLConnectionHTTPConduit
argument_list|(
name|bus
argument_list|,
name|ei
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|conduit
operator|.
name|finalizeConfig
argument_list|()
expr_stmt|;
name|Message
name|message
init|=
name|getNewMessage
argument_list|()
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|ENDPOINT_ADDRESS
argument_list|,
literal|"http://somewhere.different/"
argument_list|)
expr_stmt|;
comment|// Test call
name|conduit
operator|.
name|prepare
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|HttpURLConnection
name|con
init|=
operator|(
name|HttpURLConnection
operator|)
name|message
operator|.
name|get
argument_list|(
literal|"http.connection"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected URL address"
argument_list|,
name|con
operator|.
name|getURL
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
literal|"http://somewhere.different/"
argument_list|)
expr_stmt|;
block|}
comment|/**      * This verifys that the underlying connection is an HttpsURLConnection.      */
annotation|@
name|Test
specifier|public
name|void
name|testTLSServerParameters
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|connection
init|=
name|doTestTLSServerParameters
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Connection should not be null"
argument_list|,
name|connection
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"TLS Client Parameters should generate an HttpsURLConnection instead of "
operator|+
name|connection
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|HttpsURLConnection
operator|.
name|class
operator|.
name|isInstance
argument_list|(
name|connection
argument_list|)
argument_list|)
expr_stmt|;
name|HttpURLConnection
name|con
init|=
operator|(
name|HttpURLConnection
operator|)
name|connection
decl_stmt|;
name|con
operator|.
name|disconnect
argument_list|()
expr_stmt|;
block|}
specifier|private
name|Object
name|doTestTLSServerParameters
parameter_list|()
throws|throws
name|Exception
block|{
name|Bus
name|bus
init|=
operator|new
name|ExtensionManagerBus
argument_list|()
decl_stmt|;
name|EndpointInfo
name|ei
init|=
operator|new
name|EndpointInfo
argument_list|()
decl_stmt|;
name|ei
operator|.
name|setAddress
argument_list|(
literal|"https://secure.nowhere.null/"
operator|+
literal|"bar/foo"
argument_list|)
expr_stmt|;
name|HTTPConduit
name|conduit
init|=
operator|new
name|URLConnectionHTTPConduit
argument_list|(
name|bus
argument_list|,
name|ei
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|conduit
operator|.
name|finalizeConfig
argument_list|()
expr_stmt|;
name|Message
name|message
init|=
name|getNewMessage
argument_list|()
decl_stmt|;
comment|// We need an SSL policy, or we can't use "https".
name|conduit
operator|.
name|setTlsClientParameters
argument_list|(
operator|new
name|TLSClientParameters
argument_list|()
argument_list|)
expr_stmt|;
comment|// Test call
name|conduit
operator|.
name|prepare
argument_list|(
name|message
argument_list|)
expr_stmt|;
return|return
name|message
operator|.
name|get
argument_list|(
literal|"http.connection"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

