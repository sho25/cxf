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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|interceptor
operator|.
name|InterceptorChain
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
name|mime
operator|.
name|types
operator|.
name|XopType
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|TestUtil
block|{
specifier|private
name|TestUtil
parameter_list|()
block|{     }
specifier|public
specifier|static
name|XopType
name|createXopObject
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
throws|throws
name|IOException
throws|,
name|URISyntaxException
block|{
name|XopType
name|xopObj
init|=
operator|new
name|XopType
argument_list|()
decl_stmt|;
name|xopObj
operator|.
name|setName
argument_list|(
literal|"hello world"
argument_list|)
expr_stmt|;
name|URL
name|url1
init|=
name|clazz
operator|.
name|getResource
argument_list|(
literal|"my.wav"
argument_list|)
decl_stmt|;
name|xopObj
operator|.
name|setAttachinfo
argument_list|(
operator|new
name|DataHandler
argument_list|(
name|url1
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|xopObj
return|;
block|}
specifier|public
specifier|static
name|SoapMessage
name|createSoapMessage
parameter_list|(
name|SoapVersion
name|soapVersion
parameter_list|,
name|InterceptorChain
name|chain
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
throws|throws
name|IOException
block|{
name|SoapMessage
name|soapMessage
init|=
name|createEmptySoapMessage
argument_list|(
name|soapVersion
argument_list|,
name|chain
argument_list|)
decl_stmt|;
comment|// setup the message result with attachment.class
name|ByteArrayDataSource
name|bads
init|=
operator|new
name|ByteArrayDataSource
argument_list|(
name|clazz
operator|.
name|getResourceAsStream
argument_list|(
literal|"primarySoapPart.xml"
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
comment|// setup the message attachments
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|attachments
init|=
operator|new
name|ArrayList
argument_list|<
name|Attachment
argument_list|>
argument_list|()
decl_stmt|;
name|soapMessage
operator|.
name|setAttachments
argument_list|(
name|attachments
argument_list|)
expr_stmt|;
comment|//        String cidAtt1 = "cid:http://cxf.apache.org/me.bmp";
comment|//        bads = new ByteArrayDataSource(clazz.getResourceAsStream("me.bmp"), "image/bmp");
comment|//        AttachmentImpl att1 = new AttachmentImpl(cidAtt1, new DataHandler(bads));
comment|//        att1.setXOP(true);
comment|//        attachments.add(att1);
name|String
name|cidAtt2
init|=
literal|"cid:http://cxf.apache.org/my.wav"
decl_stmt|;
name|bads
operator|=
operator|new
name|ByteArrayDataSource
argument_list|(
name|clazz
operator|.
name|getResourceAsStream
argument_list|(
literal|"my.wav"
argument_list|)
argument_list|,
literal|"Application/octet-stream"
argument_list|)
expr_stmt|;
name|AttachmentImpl
name|att2
init|=
operator|new
name|AttachmentImpl
argument_list|(
name|cidAtt2
argument_list|,
operator|new
name|DataHandler
argument_list|(
name|bads
argument_list|)
argument_list|)
decl_stmt|;
name|att2
operator|.
name|setXOP
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|attachments
operator|.
name|add
argument_list|(
name|att2
argument_list|)
expr_stmt|;
return|return
name|soapMessage
return|;
block|}
specifier|public
specifier|static
name|SoapMessage
name|createEmptySoapMessage
parameter_list|(
name|SoapVersion
name|soapVersion
parameter_list|,
name|InterceptorChain
name|chain
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|MessageImpl
name|messageImpl
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|messageImpl
operator|.
name|setInterceptorChain
argument_list|(
name|chain
argument_list|)
expr_stmt|;
name|messageImpl
operator|.
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|SoapMessage
name|soapMessage
init|=
operator|new
name|SoapMessage
argument_list|(
name|messageImpl
argument_list|)
decl_stmt|;
name|soapMessage
operator|.
name|setVersion
argument_list|(
name|soapVersion
argument_list|)
expr_stmt|;
return|return
name|soapMessage
return|;
block|}
block|}
end_class

end_unit

