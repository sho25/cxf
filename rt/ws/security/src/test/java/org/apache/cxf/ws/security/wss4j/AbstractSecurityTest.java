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
block|}
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
block|}
end_class

end_unit

