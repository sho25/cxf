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
name|security
operator|.
name|wss4j
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|Cipher
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|SecretKey
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|spec
operator|.
name|SecretKeySpec
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|ParserConfigurationException
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
name|SOAPException
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
name|transform
operator|.
name|dom
operator|.
name|DOMSource
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
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXException
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
name|Soap11
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
name|ws
operator|.
name|security
operator|.
name|WSConstants
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractSecurityTest
extends|extends
name|AbstractCXFTest
block|{
specifier|public
name|AbstractSecurityTest
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|addNamespace
argument_list|(
literal|"wsse"
argument_list|,
name|WSConstants
operator|.
name|WSSE_NS
argument_list|)
expr_stmt|;
name|addNamespace
argument_list|(
literal|"wsse11"
argument_list|,
name|WSConstants
operator|.
name|WSSE11_NS
argument_list|)
expr_stmt|;
name|addNamespace
argument_list|(
literal|"ds"
argument_list|,
name|WSConstants
operator|.
name|SIG_NS
argument_list|)
expr_stmt|;
name|addNamespace
argument_list|(
literal|"s"
argument_list|,
name|Soap11
operator|.
name|getInstance
argument_list|()
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|addNamespace
argument_list|(
literal|"xenc"
argument_list|,
name|WSConstants
operator|.
name|ENC_NS
argument_list|)
expr_stmt|;
name|addNamespace
argument_list|(
literal|"wsu"
argument_list|,
name|WSConstants
operator|.
name|WSU_NS
argument_list|)
expr_stmt|;
name|addNamespace
argument_list|(
literal|"saml1"
argument_list|,
name|WSConstants
operator|.
name|SAML_NS
argument_list|)
expr_stmt|;
name|addNamespace
argument_list|(
literal|"saml2"
argument_list|,
name|WSConstants
operator|.
name|SAML2_NS
argument_list|)
expr_stmt|;
block|}
comment|/**      * Reads a classpath resource into a Document.      * @param name the name of the classpath resource      */
specifier|protected
name|Document
name|readDocument
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|SAXException
throws|,
name|IOException
throws|,
name|ParserConfigurationException
block|{
name|InputStream
name|inStream
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|DOMUtils
operator|.
name|readXml
argument_list|(
name|inStream
argument_list|)
return|;
block|}
comment|/**      * Reads a classpath resource into a SAAJ structure.      * @param name the name of the classpath resource      */
specifier|protected
name|SOAPMessage
name|readSAAJDocument
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|SAXException
throws|,
name|IOException
throws|,
name|ParserConfigurationException
throws|,
name|SOAPException
block|{
name|InputStream
name|inStream
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|MessageFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createMessage
argument_list|(
literal|null
argument_list|,
name|inStream
argument_list|)
return|;
block|}
comment|/**      * Creates a {@link SoapMessage} from the contents of a document.      * @param doc the document containing the SOAP content.      */
specifier|protected
name|SoapMessage
name|getSoapMessageForDom
parameter_list|(
name|Document
name|doc
parameter_list|)
throws|throws
name|SOAPException
block|{
name|SOAPMessage
name|saajMsg
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
name|saajMsg
operator|.
name|getSOAPPart
argument_list|()
decl_stmt|;
name|part
operator|.
name|setContent
argument_list|(
operator|new
name|DOMSource
argument_list|(
name|doc
argument_list|)
argument_list|)
expr_stmt|;
name|saajMsg
operator|.
name|saveChanges
argument_list|()
expr_stmt|;
name|SoapMessage
name|msg
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
name|ex
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|ex
operator|.
name|setInMessage
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setContent
argument_list|(
name|SOAPMessage
operator|.
name|class
argument_list|,
name|saajMsg
argument_list|)
expr_stmt|;
return|return
name|msg
return|;
block|}
specifier|protected
specifier|static
name|boolean
name|checkUnrestrictedPoliciesInstalled
parameter_list|()
block|{
try|try
block|{
name|byte
index|[]
name|data
init|=
block|{
literal|0x00
block|,
literal|0x01
block|,
literal|0x02
block|,
literal|0x03
block|,
literal|0x04
block|,
literal|0x05
block|,
literal|0x06
block|,
literal|0x07
block|}
decl_stmt|;
name|SecretKey
name|key192
init|=
operator|new
name|SecretKeySpec
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|0x00
block|,
literal|0x01
block|,
literal|0x02
block|,
literal|0x03
block|,
literal|0x04
block|,
literal|0x05
block|,
literal|0x06
block|,
literal|0x07
block|,
literal|0x08
block|,
literal|0x09
block|,
literal|0x0a
block|,
literal|0x0b
block|,
literal|0x0c
block|,
literal|0x0d
block|,
literal|0x0e
block|,
literal|0x0f
block|,
literal|0x10
block|,
literal|0x11
block|,
literal|0x12
block|,
literal|0x13
block|,
literal|0x14
block|,
literal|0x15
block|,
literal|0x16
block|,
literal|0x17
block|}
argument_list|,
literal|"AES"
argument_list|)
decl_stmt|;
name|Cipher
name|c
init|=
name|Cipher
operator|.
name|getInstance
argument_list|(
literal|"AES"
argument_list|)
decl_stmt|;
name|c
operator|.
name|init
argument_list|(
name|Cipher
operator|.
name|ENCRYPT_MODE
argument_list|,
name|key192
argument_list|)
expr_stmt|;
name|c
operator|.
name|doFinal
argument_list|(
name|data
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//ignore
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

