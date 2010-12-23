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
name|OutputStream
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|CXFBusImpl
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
name|util
operator|.
name|Base64Utility
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
name|AuthorizationPolicy
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
name|helpers
operator|.
name|CastUtils
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
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|EndpointReferenceType
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
name|wsdl
operator|.
name|EndpointReferenceUtils
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

begin_class
specifier|public
class|class
name|HTTPConduitTest
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
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|contentTypes
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
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
comment|/**      * This test class is a Basic Auth Supplier with a      * preemptive UserPass.      */
class|class
name|BasicAuthSupplier
extends|extends
name|HttpBasicAuthSupplier
block|{
specifier|public
name|UserPass
name|getPreemptiveUserPass
parameter_list|(
name|String
name|conduitName
parameter_list|,
name|URL
name|url
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
return|return
name|createUserPass
argument_list|(
literal|"Gandalf"
argument_list|,
literal|"staff"
argument_list|)
return|;
block|}
specifier|public
name|UserPass
name|getUserPassForRealm
parameter_list|(
name|String
name|conduitName
parameter_list|,
name|URL
name|url
parameter_list|,
name|Message
name|m
parameter_list|,
name|String
name|r
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * This test verfies that the "getTarget() call returns the correct      * EndpointReferenceType for the given endpoint address.      */
annotation|@
name|Test
specifier|public
name|void
name|testGetTarget
parameter_list|()
throws|throws
name|Exception
block|{
name|Bus
name|bus
init|=
operator|new
name|CXFBusImpl
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
name|HTTPConduit
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
name|EndpointReferenceType
name|target
init|=
name|EndpointReferenceUtils
operator|.
name|getEndpointReference
argument_list|(
literal|"http://nowhere.com/bar/foo"
argument_list|)
decl_stmt|;
comment|// Test call
name|EndpointReferenceType
name|ref
init|=
name|conduit
operator|.
name|getTarget
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"unexpected null target"
argument_list|,
name|ref
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected target"
argument_list|,
name|EndpointReferenceUtils
operator|.
name|getAddress
argument_list|(
name|ref
argument_list|)
argument_list|,
name|EndpointReferenceUtils
operator|.
name|getAddress
argument_list|(
name|target
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected address"
argument_list|,
name|conduit
operator|.
name|getAddress
argument_list|()
argument_list|,
literal|"http://nowhere.com/bar/foo"
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"unexpected upfront URL"
argument_list|,
name|conduit
operator|.
name|getURL
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected on-demand URL"
argument_list|,
name|conduit
operator|.
name|getURL
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|,
literal|"/bar/foo"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Verfies one of the tenents of our interface -- the Conduit sets up      * an OutputStream on the message after a "prepare".      */
annotation|@
name|Test
specifier|public
name|void
name|testConduitOutputStream
parameter_list|()
throws|throws
name|Exception
block|{
name|Bus
name|bus
init|=
operator|new
name|CXFBusImpl
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
name|HTTPConduit
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
comment|// Test call
name|conduit
operator|.
name|prepare
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Conduit should always set output stream."
argument_list|,
name|message
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAuthPolicyFromEndpointInfo
parameter_list|()
throws|throws
name|Exception
block|{
name|Bus
name|bus
init|=
operator|new
name|CXFBusImpl
argument_list|()
decl_stmt|;
name|EndpointInfo
name|ei
init|=
operator|new
name|EndpointInfo
argument_list|()
decl_stmt|;
name|AuthorizationPolicy
name|ap
init|=
operator|new
name|AuthorizationPolicy
argument_list|()
decl_stmt|;
name|ap
operator|.
name|setPassword
argument_list|(
literal|"password"
argument_list|)
expr_stmt|;
name|ap
operator|.
name|setUserName
argument_list|(
literal|"testUser"
argument_list|)
expr_stmt|;
name|ei
operator|.
name|addExtensor
argument_list|(
name|ap
argument_list|)
expr_stmt|;
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
name|HTTPConduit
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
comment|// Test call
name|conduit
operator|.
name|prepare
argument_list|(
name|message
argument_list|)
expr_stmt|;
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
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Authorization Header should exist"
argument_list|,
name|headers
operator|.
name|get
argument_list|(
literal|"Authorization"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected Authorization Token"
argument_list|,
literal|"Basic "
operator|+
name|Base64Utility
operator|.
name|encode
argument_list|(
literal|"testUser:password"
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|,
name|headers
operator|.
name|get
argument_list|(
literal|"Authorization"
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This test verifies the precedence of Authorization Information.      * Setting authorization information on the Message takes precedence      * over a Basic Auth Supplier with preemptive UserPass, and that      * followed by setting it directly on the Conduit.      */
annotation|@
name|Test
specifier|public
name|void
name|testAuthPolicyPrecedence
parameter_list|()
throws|throws
name|Exception
block|{
name|Bus
name|bus
init|=
operator|new
name|CXFBusImpl
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
name|HTTPConduit
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
name|conduit
operator|.
name|getAuthorization
argument_list|()
operator|.
name|setUserName
argument_list|(
literal|"Satan"
argument_list|)
expr_stmt|;
name|conduit
operator|.
name|getAuthorization
argument_list|()
operator|.
name|setPassword
argument_list|(
literal|"hell"
argument_list|)
expr_stmt|;
name|Message
name|message
init|=
name|getNewMessage
argument_list|()
decl_stmt|;
comment|// Test call
name|conduit
operator|.
name|prepare
argument_list|(
name|message
argument_list|)
expr_stmt|;
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
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Authorization Header should exist"
argument_list|,
name|headers
operator|.
name|get
argument_list|(
literal|"Authorization"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected Authorization Token"
argument_list|,
literal|"Basic "
operator|+
name|Base64Utility
operator|.
name|encode
argument_list|(
literal|"Satan:hell"
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|,
name|headers
operator|.
name|get
argument_list|(
literal|"Authorization"
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
comment|// Setting a Basic Auth User Pass should override
name|conduit
operator|.
name|setAuthSupplier
argument_list|(
operator|new
name|BasicAuthSupplier
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|=
name|getNewMessage
argument_list|()
expr_stmt|;
comment|// Test Call
name|conduit
operator|.
name|prepare
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|headers
operator|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected Authorization Token"
argument_list|,
literal|"Basic "
operator|+
name|Base64Utility
operator|.
name|encode
argument_list|(
literal|"Gandalf:staff"
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|,
name|headers
operator|.
name|get
argument_list|(
literal|"Authorization"
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|conduit
operator|.
name|setAuthSupplier
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// Setting authorization policy on the message should override
comment|// conduit setting
name|AuthorizationPolicy
name|authPolicy
init|=
operator|new
name|AuthorizationPolicy
argument_list|()
decl_stmt|;
name|authPolicy
operator|.
name|setUserName
argument_list|(
literal|"Hello"
argument_list|)
expr_stmt|;
name|authPolicy
operator|.
name|setPassword
argument_list|(
literal|"world"
argument_list|)
expr_stmt|;
name|authPolicy
operator|.
name|setAuthorizationType
argument_list|(
literal|"Basic"
argument_list|)
expr_stmt|;
name|message
operator|=
name|getNewMessage
argument_list|()
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|AuthorizationPolicy
operator|.
name|class
argument_list|,
name|authPolicy
argument_list|)
expr_stmt|;
name|conduit
operator|.
name|prepare
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|headers
operator|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected Authorization Token"
argument_list|,
literal|"Basic "
operator|+
name|Base64Utility
operator|.
name|encode
argument_list|(
literal|"Hello:world"
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|,
name|headers
operator|.
name|get
argument_list|(
literal|"Authorization"
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

