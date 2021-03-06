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
operator|.
name|interceptor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|SoapMessage
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
name|MessageInfo
operator|.
name|Type
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
name|OperationInfo
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
name|easymock
operator|.
name|EasyMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|IMocksControl
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
name|assertTrue
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|SoapPreProtocolOutInterceptorTest
block|{
specifier|private
name|IMocksControl
name|control
decl_stmt|;
specifier|private
name|SoapPreProtocolOutInterceptor
name|interceptor
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|control
operator|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
expr_stmt|;
name|interceptor
operator|=
operator|new
name|SoapPreProtocolOutInterceptor
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRequestorOutboundSoapAction
parameter_list|()
throws|throws
name|Exception
block|{
name|SoapMessage
name|message
init|=
name|setUpMessage
argument_list|()
decl_stmt|;
name|interceptor
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
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
name|reqHeaders
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|soapaction
init|=
name|reqHeaders
operator|.
name|get
argument_list|(
literal|"soapaction"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|null
operator|!=
name|soapaction
operator|&&
name|soapaction
operator|.
name|size
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"\"http://foo/bar/SEI/opReq\""
argument_list|,
name|soapaction
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|SoapMessage
name|setUpMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|SoapMessage
name|message
init|=
operator|new
name|SoapMessage
argument_list|(
operator|new
name|MessageImpl
argument_list|()
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|BindingOperationInfo
name|bop
init|=
name|setUpBindingOperationInfo
argument_list|(
literal|"http://foo/bar"
argument_list|,
literal|"opReq"
argument_list|,
literal|"opResp"
argument_list|,
name|SEI
operator|.
name|class
operator|.
name|getMethod
argument_list|(
literal|"op"
argument_list|,
operator|new
name|Class
index|[
literal|0
index|]
argument_list|)
argument_list|)
decl_stmt|;
name|SoapOperationInfo
name|sop
init|=
operator|new
name|SoapOperationInfo
argument_list|()
decl_stmt|;
name|sop
operator|.
name|setAction
argument_list|(
literal|"http://foo/bar/SEI/opReq"
argument_list|)
expr_stmt|;
name|bop
operator|.
name|addExtensor
argument_list|(
name|sop
argument_list|)
expr_stmt|;
name|exchange
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
name|message
operator|.
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|message
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
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
return|return
name|message
return|;
block|}
specifier|private
name|BindingOperationInfo
name|setUpBindingOperationInfo
parameter_list|(
name|String
name|nsuri
parameter_list|,
name|String
name|opreq
parameter_list|,
name|String
name|opresp
parameter_list|,
name|Method
name|method
parameter_list|)
block|{
name|ServiceInfo
name|si
init|=
operator|new
name|ServiceInfo
argument_list|()
decl_stmt|;
name|InterfaceInfo
name|iinf
init|=
operator|new
name|InterfaceInfo
argument_list|(
name|si
argument_list|,
operator|new
name|QName
argument_list|(
name|nsuri
argument_list|,
name|method
operator|.
name|getDeclaringClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|OperationInfo
name|opInfo
init|=
name|iinf
operator|.
name|addOperation
argument_list|(
operator|new
name|QName
argument_list|(
name|nsuri
argument_list|,
name|method
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|opInfo
operator|.
name|setProperty
argument_list|(
name|Method
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|method
argument_list|)
expr_stmt|;
name|opInfo
operator|.
name|setInput
argument_list|(
name|opreq
argument_list|,
name|opInfo
operator|.
name|createMessage
argument_list|(
operator|new
name|QName
argument_list|(
name|nsuri
argument_list|,
name|opreq
argument_list|)
argument_list|,
name|Type
operator|.
name|INPUT
argument_list|)
argument_list|)
expr_stmt|;
name|opInfo
operator|.
name|setOutput
argument_list|(
name|opresp
argument_list|,
name|opInfo
operator|.
name|createMessage
argument_list|(
operator|new
name|QName
argument_list|(
name|nsuri
argument_list|,
name|opresp
argument_list|)
argument_list|,
name|Type
operator|.
name|INPUT
argument_list|)
argument_list|)
expr_stmt|;
return|return
operator|new
name|BindingOperationInfo
argument_list|(
literal|null
argument_list|,
name|opInfo
argument_list|)
return|;
block|}
specifier|private
interface|interface
name|SEI
block|{
name|String
name|op
parameter_list|()
function_decl|;
block|}
block|}
end_class

end_unit

