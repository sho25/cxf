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
name|binding
operator|.
name|soap
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|DataHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|util
operator|.
name|ByteArrayDataSource
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
name|XMLInputFactory
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
name|XMLStreamReader
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
name|attachment
operator|.
name|AttachmentImpl
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
name|attachment
operator|.
name|AttachmentUtil
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
name|binding
operator|.
name|soap
operator|.
name|interceptor
operator|.
name|AbstractSoapInterceptor
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
name|binding
operator|.
name|soap
operator|.
name|interceptor
operator|.
name|MustUnderstandInterceptor
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
name|binding
operator|.
name|soap
operator|.
name|interceptor
operator|.
name|ReadHeadersInterceptor
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
name|binding
operator|.
name|soap
operator|.
name|interceptor
operator|.
name|StartBodyInterceptor
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
name|Attachment
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
name|BindingInfo
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
name|BindingOperationInfo
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
name|ServiceInfo
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
name|assertTrue
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
name|MustUnderstandInterceptorTest
extends|extends
name|TestBase
block|{
specifier|private
specifier|static
specifier|final
name|QName
name|RESERVATION
init|=
operator|new
name|QName
argument_list|(
literal|"http://travelcompany.example.org/reservation"
argument_list|,
literal|"reservation"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|PASSENGER
init|=
operator|new
name|QName
argument_list|(
literal|"http://mycompany.example.com/employees"
argument_list|,
literal|"passenger"
argument_list|)
decl_stmt|;
specifier|private
name|MustUnderstandInterceptor
name|mui
decl_stmt|;
specifier|private
name|DummySoapInterceptor
name|dsi
decl_stmt|;
specifier|private
name|ReadHeadersInterceptor
name|rhi
decl_stmt|;
specifier|private
name|StartBodyInterceptor
name|sbi
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
decl_stmt|;
name|rhi
operator|=
operator|new
name|ReadHeadersInterceptor
argument_list|(
name|bus
argument_list|,
literal|"phase1"
argument_list|)
expr_stmt|;
name|chain
operator|.
name|add
argument_list|(
name|rhi
argument_list|)
expr_stmt|;
name|sbi
operator|=
operator|new
name|StartBodyInterceptor
argument_list|(
literal|"phase1.5"
argument_list|)
expr_stmt|;
name|chain
operator|.
name|add
argument_list|(
name|sbi
argument_list|)
expr_stmt|;
name|mui
operator|=
operator|new
name|MustUnderstandInterceptor
argument_list|(
literal|"phase2"
argument_list|)
expr_stmt|;
name|chain
operator|.
name|add
argument_list|(
name|mui
argument_list|)
expr_stmt|;
name|dsi
operator|=
operator|new
name|DummySoapInterceptor
argument_list|(
literal|"phase3"
argument_list|)
expr_stmt|;
name|chain
operator|.
name|add
argument_list|(
name|dsi
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHandleMessageSucc
parameter_list|()
throws|throws
name|Exception
block|{
name|prepareSoapMessage
argument_list|(
literal|"test-soap-header.xml"
argument_list|)
expr_stmt|;
name|dsi
operator|.
name|getUnderstoodHeaders
argument_list|()
operator|.
name|add
argument_list|(
name|RESERVATION
argument_list|)
expr_stmt|;
name|dsi
operator|.
name|getUnderstoodHeaders
argument_list|()
operator|.
name|add
argument_list|(
name|PASSENGER
argument_list|)
expr_stmt|;
name|soapMessage
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|doIntercept
argument_list|(
name|soapMessage
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"DummaySoapInterceptor getRoles has been called!"
argument_list|,
literal|true
argument_list|,
name|dsi
operator|.
name|isCalledGetRoles
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"DummaySoapInterceptor getUnderstood has been called!"
argument_list|,
literal|true
argument_list|,
name|dsi
operator|.
name|isCalledGetUnderstood
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHandleMessageFail
parameter_list|()
throws|throws
name|Exception
block|{
name|prepareSoapMessage
argument_list|(
literal|"test-soap-header.xml"
argument_list|)
expr_stmt|;
name|dsi
operator|.
name|getUnderstoodHeaders
argument_list|()
operator|.
name|add
argument_list|(
name|RESERVATION
argument_list|)
expr_stmt|;
name|soapMessage
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|doIntercept
argument_list|(
name|soapMessage
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"DummaySoapInterceptor getRoles has been called!"
argument_list|,
literal|true
argument_list|,
name|dsi
operator|.
name|isCalledGetRoles
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"DummaySoapInterceptor getUnderstood has been called!"
argument_list|,
literal|true
argument_list|,
name|dsi
operator|.
name|isCalledGetUnderstood
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|QName
argument_list|>
name|ie
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Set
argument_list|<
name|?
argument_list|>
operator|)
name|soapMessage
operator|.
name|get
argument_list|(
name|MustUnderstandInterceptor
operator|.
name|UNKNOWNS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|ie
operator|==
literal|null
condition|)
block|{
name|fail
argument_list|(
literal|"InBound unknowns missing! Exception should be Can't understands QNames: "
operator|+
name|PASSENGER
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertTrue
argument_list|(
name|ie
operator|.
name|contains
argument_list|(
name|PASSENGER
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHandleMessageWithSoapHeader11Param
parameter_list|()
throws|throws
name|Exception
block|{
name|prepareSoapMessage
argument_list|(
literal|"test-soap-header.xml"
argument_list|)
expr_stmt|;
name|dsi
operator|.
name|getUnderstoodHeaders
argument_list|()
operator|.
name|add
argument_list|(
name|RESERVATION
argument_list|)
expr_stmt|;
name|ServiceInfo
name|serviceInfo
init|=
name|getMockedServiceModel
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"test-soap-header.wsdl"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|BindingInfo
name|binding
init|=
name|serviceInfo
operator|.
name|getBinding
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://org.apache.cxf/headers"
argument_list|,
literal|"headerTesterSOAPBinding"
argument_list|)
argument_list|)
decl_stmt|;
name|BindingOperationInfo
name|bop
init|=
name|binding
operator|.
name|getOperation
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://org.apache.cxf/headers"
argument_list|,
literal|"inHeader"
argument_list|)
argument_list|)
decl_stmt|;
name|soapMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|,
name|bop
argument_list|)
expr_stmt|;
name|soapMessage
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|doIntercept
argument_list|(
name|soapMessage
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"DummaySoapInterceptor getRoles has been called!"
argument_list|,
literal|true
argument_list|,
name|dsi
operator|.
name|isCalledGetRoles
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"DummaySoapInterceptor getUnderstood has been called!"
argument_list|,
literal|true
argument_list|,
name|dsi
operator|.
name|isCalledGetUnderstood
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHandleMessageWithSoapHeader12Param
parameter_list|()
throws|throws
name|Exception
block|{
name|prepareSoapMessage
argument_list|(
literal|"test-soap-12-header.xml"
argument_list|)
expr_stmt|;
name|dsi
operator|.
name|getUnderstoodHeaders
argument_list|()
operator|.
name|add
argument_list|(
name|RESERVATION
argument_list|)
expr_stmt|;
name|ServiceInfo
name|serviceInfo
init|=
name|getMockedServiceModel
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"test-soap-12-header.wsdl"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|BindingInfo
name|binding
init|=
name|serviceInfo
operator|.
name|getBinding
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://org.apache.cxf/headers"
argument_list|,
literal|"headerTesterSOAPBinding"
argument_list|)
argument_list|)
decl_stmt|;
name|BindingOperationInfo
name|bop
init|=
name|binding
operator|.
name|getOperation
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://org.apache.cxf/headers"
argument_list|,
literal|"inHeader"
argument_list|)
argument_list|)
decl_stmt|;
name|soapMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|,
name|bop
argument_list|)
expr_stmt|;
name|soapMessage
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|doIntercept
argument_list|(
name|soapMessage
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"DummaySoapInterceptor getRoles has been called!"
argument_list|,
literal|true
argument_list|,
name|dsi
operator|.
name|isCalledGetRoles
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"DummaySoapInterceptor getUnderstood has been called!"
argument_list|,
literal|true
argument_list|,
name|dsi
operator|.
name|isCalledGetUnderstood
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|prepareSoapMessage
parameter_list|(
name|String
name|payloadFileName
parameter_list|)
throws|throws
name|Exception
block|{
name|soapMessage
operator|=
name|TestUtil
operator|.
name|createEmptySoapMessage
argument_list|(
name|Soap12
operator|.
name|getInstance
argument_list|()
argument_list|,
name|chain
argument_list|)
expr_stmt|;
name|ByteArrayDataSource
name|bads
init|=
operator|new
name|ByteArrayDataSource
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|payloadFileName
argument_list|)
argument_list|,
literal|"Application/xop+xml"
argument_list|)
decl_stmt|;
name|String
name|cid
init|=
name|AttachmentUtil
operator|.
name|createContentID
argument_list|(
literal|"http://cxf.apache.org"
argument_list|)
decl_stmt|;
name|soapMessage
operator|.
name|setContent
argument_list|(
name|Attachment
operator|.
name|class
argument_list|,
operator|new
name|AttachmentImpl
argument_list|(
name|cid
argument_list|,
operator|new
name|DataHandler
argument_list|(
name|bads
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|soapMessage
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|XMLInputFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createXMLStreamReader
argument_list|(
name|bads
operator|.
name|getInputStream
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
class|class
name|DummySoapInterceptor
extends|extends
name|AbstractSoapInterceptor
block|{
specifier|private
name|boolean
name|calledGetRoles
decl_stmt|;
specifier|private
name|boolean
name|calledGetUnderstood
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|URI
argument_list|>
name|roles
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|QName
argument_list|>
name|understood
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|DummySoapInterceptor
parameter_list|(
name|String
name|phase
parameter_list|)
block|{
name|super
argument_list|(
name|phase
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|SoapMessage
name|messageParam
parameter_list|)
block|{         }
specifier|public
name|Set
argument_list|<
name|URI
argument_list|>
name|getRoles
parameter_list|()
block|{
name|calledGetRoles
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|roles
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
try|try
block|{
name|roles
operator|.
name|add
argument_list|(
operator|new
name|URI
argument_list|(
literal|"http://www.w3.org/2003/05/soap-envelope/role/next"
argument_list|)
argument_list|)
expr_stmt|;
name|roles
operator|.
name|add
argument_list|(
operator|new
name|URI
argument_list|(
literal|"http://www.w3.org/2003/05/soap-envelope/role/none"
argument_list|)
argument_list|)
expr_stmt|;
name|roles
operator|.
name|add
argument_list|(
operator|new
name|URI
argument_list|(
literal|"http://www.w3.org/2003/05/soap-envelope/role/ultimateReceiver"
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
return|return
name|roles
return|;
block|}
specifier|public
name|Set
argument_list|<
name|QName
argument_list|>
name|getUnderstoodHeaders
parameter_list|()
block|{
name|calledGetUnderstood
operator|=
literal|true
expr_stmt|;
return|return
name|understood
return|;
block|}
specifier|public
name|boolean
name|isCalledGetRoles
parameter_list|()
block|{
return|return
name|calledGetRoles
return|;
block|}
specifier|public
name|boolean
name|isCalledGetUnderstood
parameter_list|()
block|{
return|return
name|calledGetUnderstood
return|;
block|}
block|}
block|}
end_class

end_unit

