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
name|source
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebService
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
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
name|cxf
operator|.
name|source
operator|.
name|doc_lit_bare
operator|.
name|PutLastTradedPricePortType
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://cxf.apache.org/source/doc_lit_bare"
argument_list|,
name|portName
operator|=
literal|"SoapPort"
argument_list|,
name|serviceName
operator|=
literal|"SOAPService"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.cxf.source.doc_lit_bare.PutLastTradedPricePortType"
argument_list|)
specifier|public
class|class
name|PutLastTradePriceImpl
implements|implements
name|PutLastTradedPricePortType
block|{
specifier|public
name|DOMSource
name|nillableParameter
parameter_list|(
name|DOMSource
name|theRequest
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|sayHi
parameter_list|(
name|Holder
argument_list|<
name|DOMSource
argument_list|>
name|body
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
block|}
specifier|public
name|void
name|putLastTradedPrice
parameter_list|(
name|DOMSource
name|body
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
block|}
specifier|public
name|DOMSource
name|bareNoParam
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

