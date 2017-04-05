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
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|soap
operator|.
name|SoapFault
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
name|phase
operator|.
name|PhaseInterceptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|SecurityActionToken
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|ext
operator|.
name|WSSecurityException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|WSConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|action
operator|.
name|UsernameTokenAction
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|handler
operator|.
name|RequestData
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|handler
operator|.
name|WSHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|handler
operator|.
name|WSHandlerConstants
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
name|WSS4JOutInterceptorTest
extends|extends
name|AbstractSecurityTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testUsernameTokenText
parameter_list|()
throws|throws
name|Exception
block|{
name|Document
name|doc
init|=
name|readDocument
argument_list|(
literal|"wsse-request-clean.xml"
argument_list|)
decl_stmt|;
name|SoapMessage
name|msg
init|=
name|getSoapMessageForDom
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|WSS4JOutInterceptor
name|ohandler
init|=
operator|new
name|WSS4JOutInterceptor
argument_list|()
decl_stmt|;
name|PhaseInterceptor
argument_list|<
name|SoapMessage
argument_list|>
name|handler
init|=
name|ohandler
operator|.
name|createEndingInterceptor
argument_list|()
decl_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|ACTION
argument_list|,
name|WSHandlerConstants
operator|.
name|USERNAME_TOKEN
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|SIG_PROP_FILE
argument_list|,
literal|"outsecurity.properties"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|USER
argument_list|,
literal|"username"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
literal|"password"
argument_list|,
literal|"myAliasPassword"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|PASSWORD_TYPE
argument_list|,
name|WSConstants
operator|.
name|PW_TEXT
argument_list|)
expr_stmt|;
name|handler
operator|.
name|handleMessage
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|doc
operator|=
name|msg
operator|.
name|getContent
argument_list|(
name|SOAPMessage
operator|.
name|class
argument_list|)
operator|.
name|getSOAPPart
argument_list|()
expr_stmt|;
name|assertValid
argument_list|(
literal|"//wsse:Security"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//wsse:Security/wsse:UsernameToken"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//wsse:Security/wsse:UsernameToken/wsse:Username[text()='username']"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
comment|// Test to see that the plaintext password is used in the header
name|assertValid
argument_list|(
literal|"//wsse:Security/wsse:UsernameToken/wsse:Password[text()='myAliasPassword']"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUsernameTokenDigest
parameter_list|()
throws|throws
name|Exception
block|{
name|Document
name|doc
init|=
name|readDocument
argument_list|(
literal|"wsse-request-clean.xml"
argument_list|)
decl_stmt|;
name|SoapMessage
name|msg
init|=
name|getSoapMessageForDom
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|WSS4JOutInterceptor
name|ohandler
init|=
operator|new
name|WSS4JOutInterceptor
argument_list|()
decl_stmt|;
name|PhaseInterceptor
argument_list|<
name|SoapMessage
argument_list|>
name|handler
init|=
name|ohandler
operator|.
name|createEndingInterceptor
argument_list|()
decl_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|ACTION
argument_list|,
name|WSHandlerConstants
operator|.
name|USERNAME_TOKEN
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|SIG_PROP_FILE
argument_list|,
literal|"outsecurity.properties"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|USER
argument_list|,
literal|"username"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
literal|"password"
argument_list|,
literal|"myAliasPassword"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|PASSWORD_TYPE
argument_list|,
name|WSConstants
operator|.
name|PW_DIGEST
argument_list|)
expr_stmt|;
name|handler
operator|.
name|handleMessage
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|doc
operator|=
name|msg
operator|.
name|getContent
argument_list|(
name|SOAPMessage
operator|.
name|class
argument_list|)
operator|.
name|getSOAPPart
argument_list|()
expr_stmt|;
name|assertValid
argument_list|(
literal|"//wsse:Security"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//wsse:Security/wsse:UsernameToken"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//wsse:Security/wsse:UsernameToken/wsse:Username[text()='username']"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
comment|// Test to see that the password digest is used in the header
name|assertInvalid
argument_list|(
literal|"//wsse:Security/wsse:UsernameToken/wsse:Password[text()='myAliasPassword']"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEncrypt
parameter_list|()
throws|throws
name|Exception
block|{
name|Document
name|doc
init|=
name|readDocument
argument_list|(
literal|"wsse-request-clean.xml"
argument_list|)
decl_stmt|;
name|SoapMessage
name|msg
init|=
name|getSoapMessageForDom
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|WSS4JOutInterceptor
name|ohandler
init|=
operator|new
name|WSS4JOutInterceptor
argument_list|()
decl_stmt|;
name|PhaseInterceptor
argument_list|<
name|SoapMessage
argument_list|>
name|handler
init|=
name|ohandler
operator|.
name|createEndingInterceptor
argument_list|()
decl_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|ACTION
argument_list|,
name|WSHandlerConstants
operator|.
name|ENCRYPT
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|SIG_PROP_FILE
argument_list|,
literal|"outsecurity.properties"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|ENC_PROP_FILE
argument_list|,
literal|"outsecurity.properties"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|USER
argument_list|,
literal|"myalias"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
literal|"password"
argument_list|,
literal|"myAliasPassword"
argument_list|)
expr_stmt|;
name|handler
operator|.
name|handleMessage
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|doc
operator|=
name|msg
operator|.
name|getContent
argument_list|(
name|SOAPMessage
operator|.
name|class
argument_list|)
operator|.
name|getSOAPPart
argument_list|()
expr_stmt|;
name|assertValid
argument_list|(
literal|"//wsse:Security"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//s:Body/xenc:EncryptedData"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSignature
parameter_list|()
throws|throws
name|Exception
block|{
name|Document
name|doc
init|=
name|readDocument
argument_list|(
literal|"wsse-request-clean.xml"
argument_list|)
decl_stmt|;
name|SoapMessage
name|msg
init|=
name|getSoapMessageForDom
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|WSS4JOutInterceptor
name|ohandler
init|=
operator|new
name|WSS4JOutInterceptor
argument_list|()
decl_stmt|;
name|PhaseInterceptor
argument_list|<
name|SoapMessage
argument_list|>
name|handler
init|=
name|ohandler
operator|.
name|createEndingInterceptor
argument_list|()
decl_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|ACTION
argument_list|,
name|WSHandlerConstants
operator|.
name|SIGNATURE
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|SIG_PROP_FILE
argument_list|,
literal|"outsecurity.properties"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|USER
argument_list|,
literal|"myAlias"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
literal|"password"
argument_list|,
literal|"myAliasPassword"
argument_list|)
expr_stmt|;
name|handler
operator|.
name|handleMessage
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|doc
operator|=
name|msg
operator|.
name|getContent
argument_list|(
name|SOAPMessage
operator|.
name|class
argument_list|)
operator|.
name|getSOAPPart
argument_list|()
expr_stmt|;
name|assertValid
argument_list|(
literal|"//wsse:Security"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//wsse:Security/ds:Signature"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTimestamp
parameter_list|()
throws|throws
name|Exception
block|{
name|Document
name|doc
init|=
name|readDocument
argument_list|(
literal|"wsse-request-clean.xml"
argument_list|)
decl_stmt|;
name|SoapMessage
name|msg
init|=
name|getSoapMessageForDom
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|WSS4JOutInterceptor
name|ohandler
init|=
operator|new
name|WSS4JOutInterceptor
argument_list|()
decl_stmt|;
name|PhaseInterceptor
argument_list|<
name|SoapMessage
argument_list|>
name|handler
init|=
name|ohandler
operator|.
name|createEndingInterceptor
argument_list|()
decl_stmt|;
name|ohandler
operator|.
name|setProperty
argument_list|(
name|WSHandlerConstants
operator|.
name|ACTION
argument_list|,
name|WSHandlerConstants
operator|.
name|TIMESTAMP
argument_list|)
expr_stmt|;
name|ohandler
operator|.
name|setProperty
argument_list|(
name|WSHandlerConstants
operator|.
name|SIG_PROP_FILE
argument_list|,
literal|"outsecurity.properties"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|USER
argument_list|,
literal|"myalias"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
literal|"password"
argument_list|,
literal|"myAliasPassword"
argument_list|)
expr_stmt|;
name|handler
operator|.
name|handleMessage
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|doc
operator|=
name|msg
operator|.
name|getContent
argument_list|(
name|SOAPMessage
operator|.
name|class
argument_list|)
operator|.
name|getSOAPPart
argument_list|()
expr_stmt|;
name|assertValid
argument_list|(
literal|"//wsse:Security"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//wsse:Security/wsu:Timestamp"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOverrideCustomAction
parameter_list|()
throws|throws
name|Exception
block|{
name|Document
name|doc
init|=
name|readDocument
argument_list|(
literal|"wsse-request-clean.xml"
argument_list|)
decl_stmt|;
name|SoapMessage
name|msg
init|=
name|getSoapMessageForDom
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|WSS4JOutInterceptor
name|ohandler
init|=
operator|new
name|WSS4JOutInterceptor
argument_list|()
decl_stmt|;
name|PhaseInterceptor
argument_list|<
name|SoapMessage
argument_list|>
name|handler
init|=
name|ohandler
operator|.
name|createEndingInterceptor
argument_list|()
decl_stmt|;
name|CountingUsernameTokenAction
name|action
init|=
operator|new
name|CountingUsernameTokenAction
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|customActions
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|customActions
operator|.
name|put
argument_list|(
name|WSConstants
operator|.
name|UT
argument_list|,
name|action
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|ACTION
argument_list|,
name|WSHandlerConstants
operator|.
name|USERNAME_TOKEN
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|SIG_PROP_FILE
argument_list|,
literal|"outsecurity.properties"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|USER
argument_list|,
literal|"username"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
literal|"password"
argument_list|,
literal|"myAliasPassword"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|PASSWORD_TYPE
argument_list|,
name|WSConstants
operator|.
name|PW_TEXT
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|WSS4JOutInterceptor
operator|.
name|WSS4J_ACTION_MAP
argument_list|,
name|customActions
argument_list|)
expr_stmt|;
name|handler
operator|.
name|handleMessage
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|doc
operator|=
name|msg
operator|.
name|getContent
argument_list|(
name|SOAPMessage
operator|.
name|class
argument_list|)
operator|.
name|getSOAPPart
argument_list|()
expr_stmt|;
name|assertValid
argument_list|(
literal|"//wsse:Security"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//wsse:Security/wsse:UsernameToken"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//wsse:Security/wsse:UsernameToken/wsse:Username[text()='username']"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
comment|// Test to see that the plaintext password is used in the header
name|assertValid
argument_list|(
literal|"//wsse:Security/wsse:UsernameToken/wsse:Password[text()='myAliasPassword']"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|action
operator|.
name|getExecutions
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|customActions
operator|.
name|put
argument_list|(
name|WSConstants
operator|.
name|UT
argument_list|,
operator|new
name|Object
argument_list|()
argument_list|)
expr_stmt|;
name|handler
operator|.
name|handleMessage
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SoapFault
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"An invalid action configuration was defined."
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|customActions
operator|.
name|put
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|,
name|CountingUsernameTokenAction
operator|.
name|class
argument_list|)
expr_stmt|;
name|handler
operator|.
name|handleMessage
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SoapFault
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"An invalid action configuration was defined."
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddCustomAction
parameter_list|()
throws|throws
name|Exception
block|{
name|Document
name|doc
init|=
name|readDocument
argument_list|(
literal|"wsse-request-clean.xml"
argument_list|)
decl_stmt|;
name|SoapMessage
name|msg
init|=
name|getSoapMessageForDom
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|WSS4JOutInterceptor
name|ohandler
init|=
operator|new
name|WSS4JOutInterceptor
argument_list|()
decl_stmt|;
name|PhaseInterceptor
argument_list|<
name|SoapMessage
argument_list|>
name|handler
init|=
name|ohandler
operator|.
name|createEndingInterceptor
argument_list|()
decl_stmt|;
name|CountingUsernameTokenAction
name|action
init|=
operator|new
name|CountingUsernameTokenAction
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|customActions
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|customActions
operator|.
name|put
argument_list|(
literal|12345
argument_list|,
name|action
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|ACTION
argument_list|,
literal|"12345"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|SIG_PROP_FILE
argument_list|,
literal|"outsecurity.properties"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|USER
argument_list|,
literal|"username"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
literal|"password"
argument_list|,
literal|"myAliasPassword"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|PASSWORD_TYPE
argument_list|,
name|WSConstants
operator|.
name|PW_TEXT
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|WSS4JOutInterceptor
operator|.
name|WSS4J_ACTION_MAP
argument_list|,
name|customActions
argument_list|)
expr_stmt|;
name|handler
operator|.
name|handleMessage
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|doc
operator|=
name|msg
operator|.
name|getContent
argument_list|(
name|SOAPMessage
operator|.
name|class
argument_list|)
operator|.
name|getSOAPPart
argument_list|()
expr_stmt|;
name|assertValid
argument_list|(
literal|"//wsse:Security"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//wsse:Security/wsse:UsernameToken"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//wsse:Security/wsse:UsernameToken/wsse:Username[text()='username']"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
comment|// Test to see that the plaintext password is used in the header
name|assertValid
argument_list|(
literal|"//wsse:Security/wsse:UsernameToken/wsse:Password[text()='myAliasPassword']"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|action
operator|.
name|getExecutions
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
class|class
name|CountingUsernameTokenAction
extends|extends
name|UsernameTokenAction
block|{
specifier|private
name|int
name|executions
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|(
name|WSHandler
name|handler
parameter_list|,
name|SecurityActionToken
name|actionToken
parameter_list|,
name|RequestData
name|reqData
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|this
operator|.
name|executions
operator|++
expr_stmt|;
name|reqData
operator|.
name|setPwType
argument_list|(
name|WSConstants
operator|.
name|PW_TEXT
argument_list|)
expr_stmt|;
name|super
operator|.
name|execute
argument_list|(
name|handler
argument_list|,
name|actionToken
argument_list|,
name|reqData
argument_list|)
expr_stmt|;
block|}
specifier|public
name|int
name|getExecutions
parameter_list|()
block|{
return|return
name|this
operator|.
name|executions
return|;
block|}
block|}
block|}
end_class

end_unit

