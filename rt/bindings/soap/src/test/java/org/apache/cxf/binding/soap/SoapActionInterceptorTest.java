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
name|SoapPreProtocolOutInterceptor
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
name|model
operator|.
name|SoapOperationInfo
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
name|InterfaceInfo
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
name|Assert
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
name|SoapActionInterceptorTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testSoapAction
parameter_list|()
throws|throws
name|Exception
block|{
name|SoapPreProtocolOutInterceptor
name|i
init|=
operator|new
name|SoapPreProtocolOutInterceptor
argument_list|()
decl_stmt|;
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|message
operator|.
name|setExchange
argument_list|(
operator|new
name|ExchangeImpl
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|setOutMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|SoapBinding
name|sb
init|=
operator|new
name|SoapBinding
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|message
operator|=
name|sb
operator|.
name|createMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|message
operator|instanceof
name|SoapMessage
argument_list|)
expr_stmt|;
name|SoapMessage
name|soapMessage
init|=
operator|(
name|SoapMessage
operator|)
name|message
decl_stmt|;
name|soapMessage
operator|.
name|put
argument_list|(
name|Message
operator|.
name|REQUESTOR_ROLE
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Soap11
operator|.
name|getInstance
argument_list|()
argument_list|,
name|soapMessage
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
operator|(
operator|new
name|SoapPreProtocolOutInterceptor
argument_list|()
operator|)
operator|.
name|handleMessage
argument_list|(
name|soapMessage
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
name|reqHeaders
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
operator|)
name|soapMessage
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
name|reqHeaders
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"\"\""
argument_list|,
name|reqHeaders
operator|.
name|get
argument_list|(
name|SoapBindingConstants
operator|.
name|SOAP_ACTION
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|sb
operator|.
name|setSoapVersion
argument_list|(
name|Soap12
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|soapMessage
operator|.
name|clear
argument_list|()
expr_stmt|;
name|soapMessage
operator|=
operator|(
name|SoapMessage
operator|)
name|sb
operator|.
name|createMessage
argument_list|(
name|soapMessage
argument_list|)
expr_stmt|;
name|soapMessage
operator|.
name|put
argument_list|(
name|Message
operator|.
name|REQUESTOR_ROLE
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|i
operator|.
name|handleMessage
argument_list|(
name|soapMessage
argument_list|)
expr_stmt|;
name|String
name|ct
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"application/soap+xml"
argument_list|,
name|ct
argument_list|)
expr_stmt|;
name|BindingOperationInfo
name|bop
init|=
name|createBindingOperation
argument_list|()
decl_stmt|;
name|message
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
name|SoapOperationInfo
name|soapInfo
init|=
operator|new
name|SoapOperationInfo
argument_list|()
decl_stmt|;
name|soapInfo
operator|.
name|setAction
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|bop
operator|.
name|addExtensor
argument_list|(
name|soapInfo
argument_list|)
expr_stmt|;
name|i
operator|.
name|handleMessage
argument_list|(
name|soapMessage
argument_list|)
expr_stmt|;
name|ct
operator|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"application/soap+xml; action=\"foo\""
argument_list|,
name|ct
argument_list|)
expr_stmt|;
block|}
specifier|private
name|BindingOperationInfo
name|createBindingOperation
parameter_list|()
block|{
name|ServiceInfo
name|s
init|=
operator|new
name|ServiceInfo
argument_list|()
decl_stmt|;
name|InterfaceInfo
name|ii
init|=
name|s
operator|.
name|createInterface
argument_list|(
operator|new
name|QName
argument_list|(
literal|"FooInterface"
argument_list|)
argument_list|)
decl_stmt|;
name|s
operator|.
name|setInterface
argument_list|(
name|ii
argument_list|)
expr_stmt|;
name|ii
operator|.
name|addOperation
argument_list|(
operator|new
name|QName
argument_list|(
literal|"fooOp"
argument_list|)
argument_list|)
expr_stmt|;
name|BindingInfo
name|b
init|=
operator|new
name|BindingInfo
argument_list|(
name|s
argument_list|,
literal|"foo"
argument_list|)
decl_stmt|;
return|return
name|b
operator|.
name|buildOperation
argument_list|(
operator|new
name|QName
argument_list|(
literal|"fooOp"
argument_list|)
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
block|}
end_class

end_unit

