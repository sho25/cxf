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
name|mtom_xop
package|;
end_package

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
name|soap
operator|.
name|AttachmentPart
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|MessageFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPBody
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPBodyElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPEnvelope
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPMessage
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPPart
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
name|Provider
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
operator|.
name|Mode
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
name|ServiceMode
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
name|WebServiceProvider
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
name|MTOM
import|;
end_import

begin_class
annotation|@
name|WebServiceProvider
argument_list|(
name|portName
operator|=
literal|"TestMtomProviderPort"
argument_list|,
name|serviceName
operator|=
literal|"TestMtomService"
argument_list|,
name|targetNamespace
operator|=
literal|"http://cxf.apache.org/mime"
argument_list|,
name|wsdlLocation
operator|=
literal|"testutils/mtom_xop.wsdl"
argument_list|)
annotation|@
name|ServiceMode
argument_list|(
name|value
operator|=
name|Mode
operator|.
name|MESSAGE
argument_list|)
annotation|@
name|MTOM
specifier|public
class|class
name|TestMtomProviderImpl
implements|implements
name|Provider
argument_list|<
name|SOAPMessage
argument_list|>
block|{
specifier|public
name|SOAPMessage
name|invoke
parameter_list|(
specifier|final
name|SOAPMessage
name|request
parameter_list|)
block|{
try|try
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"=== Received client request ==="
argument_list|)
expr_stmt|;
comment|// create the SOAPMessage
name|SOAPMessage
name|message
init|=
name|MessageFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createMessage
argument_list|()
decl_stmt|;
name|SOAPPart
name|part
init|=
name|message
operator|.
name|getSOAPPart
argument_list|()
decl_stmt|;
name|SOAPEnvelope
name|envelope
init|=
name|part
operator|.
name|getEnvelope
argument_list|()
decl_stmt|;
name|SOAPBody
name|body
init|=
name|envelope
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|SOAPBodyElement
name|testResponse
init|=
name|body
operator|.
name|addBodyElement
argument_list|(
name|envelope
operator|.
name|createName
argument_list|(
literal|"testXopResponse"
argument_list|,
literal|null
argument_list|,
literal|"http://cxf.apache.org/mime/types"
argument_list|)
argument_list|)
decl_stmt|;
name|SOAPElement
name|name
init|=
name|testResponse
operator|.
name|addChildElement
argument_list|(
literal|"name"
argument_list|,
literal|null
argument_list|,
literal|"http://cxf.apache.org/mime/types"
argument_list|)
decl_stmt|;
name|name
operator|.
name|setTextContent
argument_list|(
literal|"return detail + call detail"
argument_list|)
expr_stmt|;
name|SOAPElement
name|attachinfo
init|=
name|testResponse
operator|.
name|addChildElement
argument_list|(
literal|"attachinfo"
argument_list|,
literal|null
argument_list|,
literal|"http://cxf.apache.org/mime/types"
argument_list|)
decl_stmt|;
name|SOAPElement
name|include
init|=
name|attachinfo
operator|.
name|addChildElement
argument_list|(
literal|"Include"
argument_list|,
literal|"xop"
argument_list|,
literal|"http://www.w3.org/2004/08/xop/include"
argument_list|)
decl_stmt|;
name|int
name|fileSize
init|=
literal|0
decl_stmt|;
try|try
init|(
name|InputStream
name|pre
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/wsdl/mtom_xop.wsdl"
argument_list|)
init|)
block|{
for|for
control|(
name|int
name|i
init|=
name|pre
operator|.
name|read
argument_list|()
init|;
name|i
operator|!=
operator|-
literal|1
condition|;
name|i
operator|=
name|pre
operator|.
name|read
argument_list|()
control|)
block|{
name|fileSize
operator|++
expr_stmt|;
block|}
block|}
name|int
name|count
init|=
literal|50
decl_stmt|;
name|byte
index|[]
name|data
init|=
operator|new
name|byte
index|[
name|fileSize
operator|*
name|count
index|]
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
name|count
condition|;
name|x
operator|++
control|)
block|{
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/wsdl/mtom_xop.wsdl"
argument_list|)
operator|.
name|read
argument_list|(
name|data
argument_list|,
name|fileSize
operator|*
name|x
argument_list|,
name|fileSize
argument_list|)
expr_stmt|;
block|}
name|DataHandler
name|dh
init|=
operator|new
name|DataHandler
argument_list|(
operator|new
name|ByteArrayDataSource
argument_list|(
name|data
argument_list|,
literal|"application/octet-stream"
argument_list|)
argument_list|)
decl_stmt|;
comment|// create the image attachment
name|AttachmentPart
name|attachment
init|=
name|message
operator|.
name|createAttachmentPart
argument_list|(
name|dh
argument_list|)
decl_stmt|;
name|attachment
operator|.
name|setContentId
argument_list|(
literal|"mtom_xop.wsdl"
argument_list|)
expr_stmt|;
name|message
operator|.
name|addAttachmentPart
argument_list|(
name|attachment
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Adding attachment: "
operator|+
name|attachment
operator|.
name|getContentId
argument_list|()
operator|+
literal|":"
operator|+
name|attachment
operator|.
name|getSize
argument_list|()
argument_list|)
expr_stmt|;
comment|// add the reference to the image attachment
name|include
operator|.
name|addAttribute
argument_list|(
name|envelope
operator|.
name|createName
argument_list|(
literal|"href"
argument_list|)
argument_list|,
literal|"cid:"
operator|+
name|attachment
operator|.
name|getContentId
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|message
return|;
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
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

