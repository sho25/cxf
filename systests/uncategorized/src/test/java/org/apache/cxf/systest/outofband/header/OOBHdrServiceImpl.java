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
name|outofband
operator|.
name|header
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
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|javax
operator|.
name|annotation
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
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
name|WebServiceContext
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
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|jaxb
operator|.
name|JAXBDataBinding
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
name|outofband
operator|.
name|header
operator|.
name|ObjectFactory
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
name|outofband
operator|.
name|header
operator|.
name|OutofBandHeader
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
literal|"wsdl/doc_lit_bare.wsdl"
argument_list|)
specifier|public
class|class
name|OOBHdrServiceImpl
implements|implements
name|PutLastTradedPricePortType
block|{
annotation|@
name|Resource
specifier|private
name|WebServiceContext
name|context
decl_stmt|;
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
if|if
condition|(
name|checkContext
argument_list|()
condition|)
block|{
comment|//System.out.println("Received out-of-band header as expected..");
name|sendReturnOOBHeader
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|sendReturnOOBHeader
parameter_list|()
block|{
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|MessageContext
name|ctx
init|=
name|context
operator|.
name|getMessageContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|ctx
operator|!=
literal|null
condition|)
block|{
try|try
block|{
comment|//                  Create out-of-band header object.
name|OutofBandHeader
name|ob
init|=
operator|new
name|OutofBandHeader
argument_list|()
decl_stmt|;
name|ob
operator|.
name|setName
argument_list|(
literal|"testOobReturnHeaderName"
argument_list|)
expr_stmt|;
name|ob
operator|.
name|setValue
argument_list|(
literal|"testOobReturnHeaderValue"
argument_list|)
expr_stmt|;
name|ob
operator|.
name|setHdrAttribute
argument_list|(
literal|"testReturnHdrAttribute"
argument_list|)
expr_stmt|;
comment|// Add Out-of-band header object to HeaderHolder.
name|JAXBElement
argument_list|<
name|OutofBandHeader
argument_list|>
name|job
init|=
operator|new
name|JAXBElement
argument_list|<>
argument_list|(
operator|new
name|QName
argument_list|(
name|OOBHeaderTest
operator|.
name|TEST_HDR_NS
argument_list|,
name|OOBHeaderTest
operator|.
name|TEST_HDR_RESPONSE_ELEM
argument_list|)
argument_list|,
name|OutofBandHeader
operator|.
name|class
argument_list|,
literal|null
argument_list|,
name|ob
argument_list|)
decl_stmt|;
name|Header
name|hdr
init|=
operator|new
name|Header
argument_list|(
operator|new
name|QName
argument_list|(
name|OOBHeaderTest
operator|.
name|TEST_HDR_NS
argument_list|,
name|OOBHeaderTest
operator|.
name|TEST_HDR_RESPONSE_ELEM
argument_list|)
argument_list|,
name|job
argument_list|,
operator|new
name|JAXBDataBinding
argument_list|(
name|ob
operator|.
name|getClass
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Header
argument_list|>
name|hdrList
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|ctx
operator|.
name|get
argument_list|(
name|Header
operator|.
name|HEADER_LIST
argument_list|)
argument_list|)
decl_stmt|;
name|hdrList
operator|.
name|add
argument_list|(
name|hdr
argument_list|)
expr_stmt|;
comment|//Add headerHolder to requestContext.
comment|//                    ctx.put(Header.HEADER_LIST, hdrList);
comment|//System.out.println("Completed adding list to context");
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
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
specifier|private
name|boolean
name|checkContext
parameter_list|()
block|{
name|boolean
name|success
init|=
literal|false
decl_stmt|;
name|MessageContext
name|ctx
init|=
name|context
operator|==
literal|null
condition|?
literal|null
else|:
name|context
operator|.
name|getMessageContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|ctx
operator|.
name|containsKey
argument_list|(
name|Header
operator|.
name|HEADER_LIST
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|?
argument_list|>
name|oobHdr
init|=
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|ctx
operator|.
name|get
argument_list|(
name|Header
operator|.
name|HEADER_LIST
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|iter
init|=
name|oobHdr
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|hdr
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|hdr
operator|instanceof
name|Header
operator|&&
operator|(
operator|(
name|Header
operator|)
name|hdr
operator|)
operator|.
name|getObject
argument_list|()
operator|instanceof
name|Node
condition|)
block|{
name|Header
name|hdr1
init|=
operator|(
name|Header
operator|)
name|hdr
decl_stmt|;
comment|//System.out.println("Node conains : " + hdr1.getObject().toString());
try|try
block|{
name|JAXBElement
argument_list|<
name|?
argument_list|>
name|job
init|=
operator|(
name|JAXBElement
argument_list|<
name|?
argument_list|>
operator|)
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|ObjectFactory
operator|.
name|class
argument_list|)
operator|.
name|createUnmarshaller
argument_list|()
operator|.
name|unmarshal
argument_list|(
operator|(
name|Node
operator|)
name|hdr1
operator|.
name|getObject
argument_list|()
argument_list|)
decl_stmt|;
name|OutofBandHeader
name|ob
init|=
operator|(
name|OutofBandHeader
operator|)
name|job
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"testOobHeader"
operator|.
name|equals
argument_list|(
name|ob
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
literal|"testOobHeaderValue"
operator|.
name|equals
argument_list|(
name|ob
operator|.
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
literal|"testHdrAttribute"
operator|.
name|equals
argument_list|(
name|ob
operator|.
name|getHdrAttribute
argument_list|()
argument_list|)
condition|)
block|{
name|success
operator|=
literal|true
expr_stmt|;
name|iter
operator|.
name|remove
argument_list|()
expr_stmt|;
comment|//mark it processed
block|}
elseif|else
if|if
condition|(
literal|"dontProcess"
operator|.
name|equals
argument_list|(
name|ob
operator|.
name|getHdrAttribute
argument_list|()
argument_list|)
condition|)
block|{
comment|//we won't remove it so we won't let the runtime know
comment|//it's processed.   It SHOULD throw an exception
comment|//saying the mustunderstand wasn't processed
name|success
operator|=
literal|true
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"test failed"
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|JAXBException
name|ex
parameter_list|)
block|{
comment|//
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"MessageContext is null or doesnot contain OOBHeaders"
argument_list|)
throw|;
block|}
return|return
name|success
return|;
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

