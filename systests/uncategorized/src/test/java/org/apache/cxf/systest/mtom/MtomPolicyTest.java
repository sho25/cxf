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
name|mtom
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

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
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|java
operator|.
name|util
operator|.
name|List
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
name|Element
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
name|w3c
operator|.
name|dom
operator|.
name|NodeList
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
name|BusException
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
name|AttachmentDeserializer
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
name|DOMUtils
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
name|IOUtils
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
name|jaxws
operator|.
name|JaxWsServerFactoryBean
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
name|EndpointInfo
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
name|test
operator|.
name|AbstractCXFTest
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
name|test
operator|.
name|TestUtilities
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
name|testutil
operator|.
name|common
operator|.
name|TestUtil
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
name|Conduit
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
name|ConduitInitiator
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
name|ConduitInitiatorManager
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
name|policy
operator|.
name|PolicyEngine
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
name|policy
operator|.
name|WSPolicyFeature
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
name|policy
operator|.
name|selector
operator|.
name|FirstAlternativeSelector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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
name|MtomPolicyTest
extends|extends
name|AbstractCXFTest
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
name|MtomPolicyTest
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|address
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/EchoService"
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|setKeepAliveProperty
parameter_list|()
block|{
name|TestUtilities
operator|.
name|setKeepAliveSystemProperty
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|cleanKeepAliveProperty
parameter_list|()
block|{
name|TestUtilities
operator|.
name|recoverKeepAliveSystemProperty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRequiredMtom
parameter_list|()
throws|throws
name|Exception
block|{
name|setupServer
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|sendMtomMessage
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|Node
name|res
init|=
name|invoke
argument_list|(
name|address
argument_list|,
literal|"http://schemas.xmlsoap.org/soap/http"
argument_list|,
literal|"nonmtom.xml"
argument_list|)
decl_stmt|;
name|NodeList
name|list
init|=
name|assertValid
argument_list|(
literal|"//faultstring"
argument_list|,
name|res
argument_list|)
decl_stmt|;
name|String
name|text
init|=
name|list
operator|.
name|item
argument_list|(
literal|0
argument_list|)
operator|.
name|getTextContent
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|text
operator|.
name|contains
argument_list|(
literal|"These policy alternatives can not be satisfied: "
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|text
operator|.
name|contains
argument_list|(
literal|"{http://schemas.xmlsoap.org/ws/2004/09/policy/optimizedmimeserialization}"
operator|+
literal|"OptimizedMimeSerialization"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOptionalMtom
parameter_list|()
throws|throws
name|Exception
block|{
name|setupServer
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sendMtomMessage
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|Node
name|res
init|=
name|invoke
argument_list|(
name|address
argument_list|,
literal|"http://schemas.xmlsoap.org/soap/http"
argument_list|,
literal|"nonmtom.xml"
argument_list|)
decl_stmt|;
name|assertNoFault
argument_list|(
name|res
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setupServer
parameter_list|(
name|boolean
name|mtomRequired
parameter_list|)
throws|throws
name|Exception
block|{
name|getBus
argument_list|()
operator|.
name|getExtension
argument_list|(
name|PolicyEngine
operator|.
name|class
argument_list|)
operator|.
name|setAlternativeSelector
argument_list|(
operator|new
name|FirstAlternativeSelector
argument_list|()
argument_list|)
expr_stmt|;
name|JaxWsServerFactoryBean
name|sf
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|sf
operator|.
name|setServiceBean
argument_list|(
operator|new
name|EchoService
argument_list|()
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setBus
argument_list|(
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|WSPolicyFeature
name|policyFeature
init|=
operator|new
name|WSPolicyFeature
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Element
argument_list|>
name|policyElements
init|=
operator|new
name|ArrayList
argument_list|<
name|Element
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|mtomRequired
condition|)
block|{
name|policyElements
operator|.
name|add
argument_list|(
name|DOMUtils
operator|.
name|readXml
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"mtom-policy.xml"
argument_list|)
argument_list|)
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|policyElements
operator|.
name|add
argument_list|(
name|DOMUtils
operator|.
name|readXml
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"mtom-policy-optional.xml"
argument_list|)
argument_list|)
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|policyFeature
operator|.
name|setPolicyElements
argument_list|(
name|policyElements
argument_list|)
expr_stmt|;
name|sf
operator|.
name|getFeatures
argument_list|()
operator|.
name|add
argument_list|(
name|policyFeature
argument_list|)
expr_stmt|;
name|sf
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|sendMtomMessage
parameter_list|(
name|String
name|a
parameter_list|)
throws|throws
name|Exception
block|{
name|EndpointInfo
name|ei
init|=
operator|new
name|EndpointInfo
argument_list|(
literal|null
argument_list|,
literal|"http://schemas.xmlsoap.org/wsdl/http"
argument_list|)
decl_stmt|;
name|ei
operator|.
name|setAddress
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|ConduitInitiatorManager
name|conduitMgr
init|=
name|getBus
argument_list|()
operator|.
name|getExtension
argument_list|(
name|ConduitInitiatorManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|ConduitInitiator
name|conduitInit
init|=
name|conduitMgr
operator|.
name|getConduitInitiator
argument_list|(
literal|"http://schemas.xmlsoap.org/soap/http"
argument_list|)
decl_stmt|;
name|Conduit
name|conduit
init|=
name|conduitInit
operator|.
name|getConduit
argument_list|(
name|ei
argument_list|)
decl_stmt|;
name|TestMessageObserver
name|obs
init|=
operator|new
name|TestMessageObserver
argument_list|()
decl_stmt|;
name|conduit
operator|.
name|setMessageObserver
argument_list|(
name|obs
argument_list|)
expr_stmt|;
name|Message
name|m
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|String
name|ct
init|=
literal|"multipart/related; type=\"application/xop+xml\"; "
operator|+
literal|"start=\"<soap.xml@xfire.codehaus.org>\"; "
operator|+
literal|"start-info=\"text/xml; charset=utf-8\"; "
operator|+
literal|"boundary=\"----=_Part_4_701508.1145579811786\""
decl_stmt|;
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
name|ct
argument_list|)
expr_stmt|;
name|conduit
operator|.
name|prepare
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|OutputStream
name|os
init|=
name|m
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|InputStream
name|is
init|=
name|getResourceAsStream
argument_list|(
literal|"request"
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Could not find resource "
operator|+
literal|"request"
argument_list|)
throw|;
block|}
name|IOUtils
operator|.
name|copy
argument_list|(
name|is
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|os
operator|.
name|flush
argument_list|()
expr_stmt|;
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
name|byte
index|[]
name|res
init|=
name|obs
operator|.
name|getResponseStream
argument_list|()
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
name|MessageImpl
name|resMsg
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|resMsg
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|res
argument_list|)
argument_list|)
expr_stmt|;
name|resMsg
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
name|obs
operator|.
name|getResponseContentType
argument_list|()
argument_list|)
expr_stmt|;
name|resMsg
operator|.
name|setExchange
argument_list|(
operator|new
name|ExchangeImpl
argument_list|()
argument_list|)
expr_stmt|;
name|AttachmentDeserializer
name|deserializer
init|=
operator|new
name|AttachmentDeserializer
argument_list|(
name|resMsg
argument_list|)
decl_stmt|;
name|deserializer
operator|.
name|initializeAttachments
argument_list|()
expr_stmt|;
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|attachments
init|=
name|resMsg
operator|.
name|getAttachments
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|attachments
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|attachments
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Attachment
name|inAtt
init|=
name|attachments
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|inAtt
operator|.
name|getDataHandler
argument_list|()
operator|.
name|getInputStream
argument_list|()
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|37448
argument_list|,
name|out
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Bus
name|createBus
parameter_list|()
throws|throws
name|BusException
block|{
return|return
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
return|;
block|}
block|}
end_class

end_unit

