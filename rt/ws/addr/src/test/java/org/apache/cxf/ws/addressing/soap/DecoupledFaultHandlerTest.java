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
name|ws
operator|.
name|addressing
operator|.
name|soap
package|;
end_package

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
name|headers
operator|.
name|Header
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
name|transport
operator|.
name|Destination
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
name|AddressingProperties
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
name|AttributedURIType
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
name|ContextUtils
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
name|easymock
operator|.
name|EasyMock
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
name|DecoupledFaultHandlerTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testOnewayFault
parameter_list|()
block|{
name|DecoupledFaultHandler
name|handler
init|=
operator|new
name|DecoupledFaultHandler
argument_list|()
block|{
specifier|protected
name|Destination
name|createDecoupledDestination
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|EndpointReferenceType
name|epr
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"http://bar"
argument_list|,
name|epr
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Destination
operator|.
name|class
argument_list|)
return|;
block|}
block|}
decl_stmt|;
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
name|QName
name|qname
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/mustunderstand"
argument_list|,
literal|"TestMU"
argument_list|)
decl_stmt|;
name|message
operator|.
name|getHeaders
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|Header
argument_list|(
name|qname
argument_list|,
operator|new
name|Object
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|AddressingProperties
name|maps
init|=
operator|new
name|AddressingProperties
argument_list|()
decl_stmt|;
name|EndpointReferenceType
name|faultTo
init|=
operator|new
name|EndpointReferenceType
argument_list|()
decl_stmt|;
name|faultTo
operator|.
name|setAddress
argument_list|(
operator|new
name|AttributedURIType
argument_list|()
argument_list|)
expr_stmt|;
name|faultTo
operator|.
name|getAddress
argument_list|()
operator|.
name|setValue
argument_list|(
literal|"http://bar"
argument_list|)
expr_stmt|;
name|maps
operator|.
name|setFaultTo
argument_list|(
name|faultTo
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|ContextUtils
operator|.
name|getMAPProperty
argument_list|(
literal|false
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
argument_list|,
name|maps
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|message
operator|.
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setInMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setOneWay
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|handler
operator|.
name|handleFault
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|message
operator|.
name|getHeaders
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|exchange
operator|.
name|isOneWay
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|message
argument_list|,
name|exchange
operator|.
name|getOutMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|exchange
operator|.
name|getDestination
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

