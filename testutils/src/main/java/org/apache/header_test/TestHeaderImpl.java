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
name|header_test
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
name|header_test
operator|.
name|types
operator|.
name|TestHeader1
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|header_test
operator|.
name|types
operator|.
name|TestHeader1Response
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|header_test
operator|.
name|types
operator|.
name|TestHeader2
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|header_test
operator|.
name|types
operator|.
name|TestHeader2Response
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|header_test
operator|.
name|types
operator|.
name|TestHeader3
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|header_test
operator|.
name|types
operator|.
name|TestHeader3Response
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|header_test
operator|.
name|types
operator|.
name|TestHeader5
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|header_test
operator|.
name|types
operator|.
name|TestHeader5ResponseBody
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|header_test
operator|.
name|types
operator|.
name|TestHeader6
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|header_test
operator|.
name|types
operator|.
name|TestHeader6Response
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tests
operator|.
name|type_test
operator|.
name|all
operator|.
name|SimpleAll
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tests
operator|.
name|type_test
operator|.
name|choice
operator|.
name|SimpleChoice
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tests
operator|.
name|type_test
operator|.
name|sequence
operator|.
name|SimpleStruct
import|;
end_import

begin_class
annotation|@
name|WebService
argument_list|(
name|serviceName
operator|=
literal|"SOAPHeaderService"
argument_list|,
name|portName
operator|=
literal|"SoapHeaderPort"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.header_test.TestHeader"
argument_list|,
name|targetNamespace
operator|=
literal|"http://apache.org/header_test"
argument_list|,
name|wsdlLocation
operator|=
literal|"testutils/soapheader.wsdl"
argument_list|)
specifier|public
class|class
name|TestHeaderImpl
implements|implements
name|TestHeader
block|{
specifier|public
name|TestHeader1Response
name|testHeader1
parameter_list|(
name|TestHeader1
name|in
parameter_list|,
name|TestHeader1
name|inHeader
parameter_list|)
block|{
if|if
condition|(
name|in
operator|==
literal|null
operator|||
name|inHeader
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"TestHeader1 part not found."
argument_list|)
throw|;
block|}
name|TestHeader1Response
name|returnVal
init|=
operator|new
name|TestHeader1Response
argument_list|()
decl_stmt|;
name|returnVal
operator|.
name|setResponseType
argument_list|(
name|inHeader
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|returnVal
return|;
block|}
comment|/**      *      * @param out      * @param outHeader      * @param in      */
specifier|public
name|void
name|testHeader2
parameter_list|(
name|TestHeader2
name|in
parameter_list|,
name|Holder
argument_list|<
name|TestHeader2Response
argument_list|>
name|out
parameter_list|,
name|Holder
argument_list|<
name|TestHeader2Response
argument_list|>
name|outHeader
parameter_list|)
block|{
name|TestHeader2Response
name|outVal
init|=
operator|new
name|TestHeader2Response
argument_list|()
decl_stmt|;
name|outVal
operator|.
name|setResponseType
argument_list|(
name|in
operator|.
name|getRequestType
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|value
operator|=
name|outVal
expr_stmt|;
name|TestHeader2Response
name|outHeaderVal
init|=
operator|new
name|TestHeader2Response
argument_list|()
decl_stmt|;
name|outHeaderVal
operator|.
name|setResponseType
argument_list|(
name|in
operator|.
name|getRequestType
argument_list|()
argument_list|)
expr_stmt|;
name|outHeader
operator|.
name|value
operator|=
name|outHeaderVal
expr_stmt|;
block|}
specifier|public
name|TestHeader3Response
name|testHeader3
parameter_list|(
name|TestHeader3
name|in
parameter_list|,
name|Holder
argument_list|<
name|TestHeader3
argument_list|>
name|inoutHeader
parameter_list|)
block|{
if|if
condition|(
name|inoutHeader
operator|.
name|value
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"TestHeader3 part not found."
argument_list|)
throw|;
block|}
name|TestHeader3Response
name|returnVal
init|=
operator|new
name|TestHeader3Response
argument_list|()
decl_stmt|;
name|returnVal
operator|.
name|setResponseType
argument_list|(
name|inoutHeader
operator|.
name|value
operator|.
name|getRequestType
argument_list|()
argument_list|)
expr_stmt|;
name|inoutHeader
operator|.
name|value
operator|.
name|setRequestType
argument_list|(
name|in
operator|.
name|getRequestType
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|returnVal
return|;
block|}
comment|/**      *      * @param requestType      */
specifier|public
name|void
name|testHeader4
parameter_list|(
name|String
name|requestType
parameter_list|)
block|{      }
specifier|public
name|void
name|testHeader5
parameter_list|(
name|Holder
argument_list|<
name|TestHeader5ResponseBody
argument_list|>
name|out
parameter_list|,
name|Holder
argument_list|<
name|TestHeader5
argument_list|>
name|outHeader
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|header_test
operator|.
name|types
operator|.
name|TestHeader5
name|in
parameter_list|)
block|{
name|TestHeader5ResponseBody
name|outVal
init|=
operator|new
name|TestHeader5ResponseBody
argument_list|()
decl_stmt|;
name|outVal
operator|.
name|setResponseType
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|out
operator|.
name|value
operator|=
name|outVal
expr_stmt|;
name|TestHeader5
name|outHeaderVal
init|=
operator|new
name|TestHeader5
argument_list|()
decl_stmt|;
name|outHeaderVal
operator|.
name|setRequestType
argument_list|(
name|in
operator|.
name|getRequestType
argument_list|()
argument_list|)
expr_stmt|;
name|outHeader
operator|.
name|value
operator|=
name|outHeaderVal
expr_stmt|;
block|}
specifier|public
name|TestHeader6Response
name|testHeaderPartBeforeBodyPart
parameter_list|(
name|Holder
argument_list|<
name|TestHeader3
argument_list|>
name|inoutHeader
parameter_list|,
name|TestHeader6
name|in
parameter_list|)
block|{
if|if
condition|(
name|inoutHeader
operator|.
name|value
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"TestHeader3 part not found."
argument_list|)
throw|;
block|}
name|TestHeader6Response
name|returnVal
init|=
operator|new
name|TestHeader6Response
argument_list|()
decl_stmt|;
name|returnVal
operator|.
name|setResponseType
argument_list|(
name|inoutHeader
operator|.
name|value
operator|.
name|getRequestType
argument_list|()
argument_list|)
expr_stmt|;
name|inoutHeader
operator|.
name|value
operator|.
name|setRequestType
argument_list|(
name|in
operator|.
name|getRequestType
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|returnVal
return|;
block|}
specifier|public
name|SimpleStruct
name|sendReceiveAnyType
parameter_list|(
name|Holder
argument_list|<
name|SimpleAll
argument_list|>
name|x
parameter_list|,
name|SimpleChoice
name|y
parameter_list|)
block|{
name|SimpleAll
name|sa
init|=
operator|new
name|SimpleAll
argument_list|()
decl_stmt|;
name|sa
operator|.
name|setVarString
argument_list|(
name|y
operator|.
name|getVarString
argument_list|()
argument_list|)
expr_stmt|;
name|SimpleStruct
name|ss
init|=
operator|new
name|SimpleStruct
argument_list|()
decl_stmt|;
name|ss
operator|.
name|setVarAttrString
argument_list|(
name|x
operator|.
name|value
operator|.
name|getVarAttrString
argument_list|()
operator|+
literal|"Ret"
argument_list|)
expr_stmt|;
name|ss
operator|.
name|setVarInt
argument_list|(
name|x
operator|.
name|value
operator|.
name|getVarInt
argument_list|()
operator|+
literal|100
argument_list|)
expr_stmt|;
name|x
operator|.
name|value
operator|=
name|sa
expr_stmt|;
return|return
name|ss
return|;
block|}
specifier|public
name|String
name|testHeader7
parameter_list|()
block|{
return|return
literal|"Hello"
return|;
block|}
block|}
end_class

end_unit

