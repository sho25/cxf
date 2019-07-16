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
name|local
package|;
end_package

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
name|IOException
import|;
end_import

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
name|io
operator|.
name|OutputStream
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
name|BusFactory
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
name|IOUtils
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
name|Exchange
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
name|ExchangeImpl
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
name|transport
operator|.
name|Conduit
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
name|MessageObserver
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_class
specifier|public
class|class
name|LocalTransportFactoryTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testLocalTransportWithSeparateThread
parameter_list|()
throws|throws
name|Exception
block|{
name|testInvocation
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLocalTransportWithDirectDispatch
parameter_list|()
throws|throws
name|Exception
block|{
name|testInvocation
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|testInvocation
parameter_list|(
name|boolean
name|isDirectDispatch
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Need to create a DefaultBus
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
decl_stmt|;
name|LocalTransportFactory
name|factory
init|=
operator|new
name|LocalTransportFactory
argument_list|()
decl_stmt|;
name|EndpointInfo
name|ei
init|=
operator|new
name|EndpointInfo
argument_list|(
literal|null
argument_list|,
literal|"http://schemas.xmlsoap.org/soap/http"
argument_list|)
decl_stmt|;
name|ei
operator|.
name|setAddress
argument_list|(
literal|"http://localhost/test"
argument_list|)
expr_stmt|;
name|LocalDestination
name|d
init|=
operator|(
name|LocalDestination
operator|)
name|factory
operator|.
name|getDestination
argument_list|(
name|ei
argument_list|,
name|bus
argument_list|)
decl_stmt|;
name|d
operator|.
name|setMessageObserver
argument_list|(
operator|new
name|EchoObserver
argument_list|()
argument_list|)
expr_stmt|;
comment|// Set up a listener for the response
name|Conduit
name|conduit
init|=
name|factory
operator|.
name|getConduit
argument_list|(
name|ei
argument_list|,
name|bus
argument_list|)
decl_stmt|;
name|TestMessageObserver
name|obs
init|=
operator|new
name|TestMessageObserver
argument_list|()
decl_stmt|;
name|conduit
operator|.
name|setMessageObserver
argument_list|(
name|obs
argument_list|)
expr_stmt|;
name|MessageImpl
name|m
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
if|if
condition|(
name|isDirectDispatch
condition|)
block|{
name|m
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
block|}
name|m
operator|.
name|setDestination
argument_list|(
name|d
argument_list|)
expr_stmt|;
name|Exchange
name|ex
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|Bus
operator|.
name|class
argument_list|,
name|bus
argument_list|)
expr_stmt|;
name|m
operator|.
name|setExchange
argument_list|(
name|ex
argument_list|)
expr_stmt|;
name|conduit
operator|.
name|prepare
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|OutputStream
name|out
init|=
name|m
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
literal|1000
condition|;
name|x
operator|++
control|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|"hello"
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|write
argument_list|(
name|builder
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
name|conduit
operator|.
name|close
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|builder
operator|.
name|toString
argument_list|()
argument_list|,
name|obs
operator|.
name|getResponseStream
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|static
class|class
name|EchoObserver
implements|implements
name|MessageObserver
block|{
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
try|try
block|{
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|setInMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|Conduit
name|backChannel
init|=
name|message
operator|.
name|getDestination
argument_list|()
operator|.
name|getBackChannel
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|InputStream
name|in
init|=
name|message
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|backChannel
operator|.
name|prepare
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|OutputStream
name|out
init|=
name|message
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|in
argument_list|,
name|out
argument_list|,
literal|1024
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
name|backChannel
operator|.
name|close
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
class|class
name|TestMessageObserver
implements|implements
name|MessageObserver
block|{
name|ByteArrayOutputStream
name|response
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|boolean
name|written
decl_stmt|;
name|Message
name|inMessage
decl_stmt|;
specifier|public
specifier|synchronized
name|ByteArrayOutputStream
name|getResponseStream
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|written
condition|)
block|{
name|wait
argument_list|()
expr_stmt|;
block|}
return|return
name|response
return|;
block|}
specifier|public
specifier|synchronized
name|void
name|onMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
try|try
block|{
name|message
operator|.
name|remove
argument_list|(
name|LocalConduit
operator|.
name|DIRECT_DISPATCH
argument_list|)
expr_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|message
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
argument_list|,
name|response
argument_list|,
literal|1024
argument_list|)
expr_stmt|;
name|message
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
operator|.
name|close
argument_list|()
expr_stmt|;
name|response
operator|.
name|close
argument_list|()
expr_stmt|;
name|inMessage
operator|=
name|message
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|written
operator|=
literal|true
expr_stmt|;
name|notifyAll
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

