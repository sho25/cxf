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
name|basicDOCBare
package|;
end_package

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigDecimal
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
name|org
operator|.
name|apache
operator|.
name|hello_world_doc_lit_bare
operator|.
name|PutLastTradedPricePortType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_doc_lit_bare
operator|.
name|types
operator|.
name|TradePriceData
import|;
end_import

begin_class
annotation|@
name|javax
operator|.
name|jws
operator|.
name|WebService
argument_list|(
name|serviceName
operator|=
literal|"SOAPService"
argument_list|,
name|portName
operator|=
literal|"SoapPort"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.hello_world_doc_lit_bare.PutLastTradedPricePortType"
argument_list|,
name|targetNamespace
operator|=
literal|"http://apache.org/hello_world_doc_lit_bare"
argument_list|,
name|wsdlLocation
operator|=
literal|"testutils/doc_lit_bare.wsdl"
argument_list|)
specifier|public
class|class
name|PutLastTradedPriceImpl
implements|implements
name|PutLastTradedPricePortType
block|{
specifier|public
name|void
name|sayHi
parameter_list|(
name|Holder
argument_list|<
name|TradePriceData
argument_list|>
name|inout
parameter_list|)
block|{
name|inout
operator|.
name|value
operator|.
name|setTickerPrice
argument_list|(
literal|4.5f
argument_list|)
expr_stmt|;
name|inout
operator|.
name|value
operator|.
name|setTickerSymbol
argument_list|(
literal|"APACHE"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|putLastTradedPrice
parameter_list|(
name|TradePriceData
name|body
parameter_list|)
block|{
comment|//System.out.println("-----TradePriceData TickerPrice : ----- " + body.getTickerPrice());
comment|//System.out.println("-----TradePriceData TickerSymbol : ----- " + body.getTickerSymbol());
block|}
specifier|public
name|String
name|bareNoParam
parameter_list|()
block|{
return|return
literal|"testResponse"
return|;
block|}
specifier|public
name|String
name|nillableParameter
parameter_list|(
name|BigDecimal
name|theRequest
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

