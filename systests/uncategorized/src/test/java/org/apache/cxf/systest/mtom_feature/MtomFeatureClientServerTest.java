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
name|mtom_feature
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Image
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
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
name|javax
operator|.
name|imageio
operator|.
name|ImageIO
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
name|ws
operator|.
name|BindingProvider
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
name|Endpoint
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
name|Holder
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
name|soap
operator|.
name|MTOMFeature
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
name|ext
operator|.
name|logging
operator|.
name|LoggingOutInterceptor
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
name|ext
operator|.
name|logging
operator|.
name|event
operator|.
name|PrintWriterEventSender
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
name|testutil
operator|.
name|common
operator|.
name|AbstractBusClientServerTestBase
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
name|local
operator|.
name|LocalConduit
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
name|BeforeClass
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
name|MtomFeatureClientServerTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|Server
operator|.
name|PORT
decl_stmt|;
specifier|private
specifier|final
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/cxf/systest/mtom_feature"
argument_list|,
literal|"HelloService"
argument_list|)
decl_stmt|;
specifier|private
name|Hello
name|port
init|=
name|getPort
argument_list|()
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|startServers
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|Server
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|createBus
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDetail
parameter_list|()
throws|throws
name|Exception
block|{
name|Holder
argument_list|<
name|byte
index|[]
argument_list|>
name|photo
init|=
operator|new
name|Holder
argument_list|<
name|byte
index|[]
argument_list|>
argument_list|(
literal|"CXF"
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|Holder
argument_list|<
name|Image
argument_list|>
name|image
init|=
operator|new
name|Holder
argument_list|<
name|Image
argument_list|>
argument_list|(
name|getImage
argument_list|(
literal|"/java.jpg"
argument_list|)
argument_list|)
decl_stmt|;
name|port
operator|.
name|detail
argument_list|(
name|photo
argument_list|,
name|image
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CXF"
argument_list|,
operator|new
name|String
argument_list|(
name|photo
operator|.
name|value
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|image
operator|.
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEcho
parameter_list|()
throws|throws
name|Exception
block|{
name|byte
index|[]
name|bytes
init|=
name|ImageHelper
operator|.
name|getImageBytes
argument_list|(
name|getImage
argument_list|(
literal|"/java.jpg"
argument_list|)
argument_list|,
literal|"image/jpeg"
argument_list|)
decl_stmt|;
name|Holder
argument_list|<
name|byte
index|[]
argument_list|>
name|image
init|=
operator|new
name|Holder
argument_list|<
name|byte
index|[]
argument_list|>
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|port
operator|.
name|echoData
argument_list|(
name|image
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|image
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWithLocalTransport
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|implementor
init|=
operator|new
name|HelloImpl
argument_list|()
decl_stmt|;
name|String
name|address
init|=
literal|"local://Hello"
decl_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
name|implementor
argument_list|)
expr_stmt|;
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/cxf/systest/mtom_feature"
argument_list|,
literal|"HelloPort"
argument_list|)
decl_stmt|;
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
literal|"http://schemas.xmlsoap.org/soap/"
argument_list|,
literal|"local://Hello"
argument_list|)
expr_stmt|;
name|port
operator|=
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|Hello
operator|.
name|class
argument_list|,
operator|new
name|MTOMFeature
argument_list|()
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|port
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|,
name|address
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|port
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|LocalConduit
operator|.
name|DIRECT_DISPATCH
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|Holder
argument_list|<
name|byte
index|[]
argument_list|>
name|photo
init|=
operator|new
name|Holder
argument_list|<
name|byte
index|[]
argument_list|>
argument_list|(
literal|"CXF"
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|Holder
argument_list|<
name|Image
argument_list|>
name|image
init|=
operator|new
name|Holder
argument_list|<
name|Image
argument_list|>
argument_list|(
name|getImage
argument_list|(
literal|"/java.jpg"
argument_list|)
argument_list|)
decl_stmt|;
name|port
operator|.
name|detail
argument_list|(
name|photo
argument_list|,
name|image
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CXF"
argument_list|,
operator|new
name|String
argument_list|(
name|photo
operator|.
name|value
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|image
operator|.
name|value
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|port
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|LocalConduit
operator|.
name|DIRECT_DISPATCH
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|photo
operator|=
operator|new
name|Holder
argument_list|<
name|byte
index|[]
argument_list|>
argument_list|(
literal|"CXF"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|image
operator|=
operator|new
name|Holder
argument_list|<
name|Image
argument_list|>
argument_list|(
name|getImage
argument_list|(
literal|"/java.jpg"
argument_list|)
argument_list|)
expr_stmt|;
name|port
operator|.
name|detail
argument_list|(
name|photo
argument_list|,
name|image
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CXF"
argument_list|,
operator|new
name|String
argument_list|(
name|photo
operator|.
name|value
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|image
operator|.
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEchoWithLowThreshold
parameter_list|()
throws|throws
name|Exception
block|{
name|ByteArrayOutputStream
name|bout
init|=
name|this
operator|.
name|setupOutLogging
argument_list|()
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
name|ImageHelper
operator|.
name|getImageBytes
argument_list|(
name|getImage
argument_list|(
literal|"/java.jpg"
argument_list|)
argument_list|,
literal|"image/jpeg"
argument_list|)
decl_stmt|;
name|Holder
argument_list|<
name|byte
index|[]
argument_list|>
name|image
init|=
operator|new
name|Holder
argument_list|<
name|byte
index|[]
argument_list|>
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|Hello
name|hello
init|=
name|this
operator|.
name|getPort
argument_list|(
literal|500
argument_list|)
decl_stmt|;
name|hello
operator|.
name|echoData
argument_list|(
name|image
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"MTOM should be enabled"
argument_list|,
name|bout
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"<xop:Include"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEchoWithHighThreshold
parameter_list|()
throws|throws
name|Exception
block|{
name|ByteArrayOutputStream
name|bout
init|=
name|this
operator|.
name|setupOutLogging
argument_list|()
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
name|ImageHelper
operator|.
name|getImageBytes
argument_list|(
name|getImage
argument_list|(
literal|"/java.jpg"
argument_list|)
argument_list|,
literal|"image/jpeg"
argument_list|)
decl_stmt|;
name|Holder
argument_list|<
name|byte
index|[]
argument_list|>
name|image
init|=
operator|new
name|Holder
argument_list|<
name|byte
index|[]
argument_list|>
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|Hello
name|hello
init|=
name|this
operator|.
name|getPort
argument_list|(
literal|2000
argument_list|)
decl_stmt|;
name|hello
operator|.
name|echoData
argument_list|(
name|image
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"MTOM should not be enabled"
argument_list|,
name|bout
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"<xop:Include"
argument_list|)
operator|==
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ByteArrayOutputStream
name|setupOutLogging
parameter_list|()
block|{
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|PrintWriter
name|writer
init|=
operator|new
name|PrintWriter
argument_list|(
name|bos
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|LoggingOutInterceptor
name|out
init|=
operator|new
name|LoggingOutInterceptor
argument_list|(
operator|new
name|PrintWriterEventSender
argument_list|(
name|writer
argument_list|)
argument_list|)
decl_stmt|;
name|this
operator|.
name|bus
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|out
argument_list|)
expr_stmt|;
return|return
name|bos
return|;
block|}
specifier|private
name|Image
name|getImage
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|ImageIO
operator|.
name|read
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|Hello
name|getPort
parameter_list|()
block|{
return|return
name|getPort
argument_list|(
literal|0
argument_list|)
return|;
block|}
specifier|private
name|Hello
name|getPort
parameter_list|(
name|int
name|threshold
parameter_list|)
block|{
name|URL
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl_systest/mtom.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"WSDL is null"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|HelloService
name|service
init|=
operator|new
name|HelloService
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Service is null "
argument_list|,
name|service
argument_list|)
expr_stmt|;
comment|//return service.getHelloPort();
name|MTOMFeature
name|mtomFeature
init|=
operator|new
name|MTOMFeature
argument_list|()
decl_stmt|;
if|if
condition|(
name|threshold
operator|>
literal|0
condition|)
block|{
name|mtomFeature
operator|=
operator|new
name|MTOMFeature
argument_list|(
literal|true
argument_list|,
name|threshold
argument_list|)
expr_stmt|;
block|}
name|Hello
name|hello
init|=
name|service
operator|.
name|getHelloPort
argument_list|(
name|mtomFeature
argument_list|)
decl_stmt|;
try|try
block|{
name|updateAddressPort
argument_list|(
name|hello
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//ignore
block|}
return|return
name|hello
return|;
block|}
block|}
end_class

end_unit

