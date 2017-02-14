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
name|Arrays
import|;
end_import

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
name|List
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
name|interceptor
operator|.
name|Fault
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
name|cxf
operator|.
name|ws
operator|.
name|security
operator|.
name|wss4j
operator|.
name|CryptoCoverageChecker
operator|.
name|XPathExpression
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
name|security
operator|.
name|wss4j
operator|.
name|CryptoCoverageUtil
operator|.
name|CoverageScope
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
name|security
operator|.
name|wss4j
operator|.
name|CryptoCoverageUtil
operator|.
name|CoverageType
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

begin_comment
comment|/**  * Test the DefaultCryptoCoverageChecker, which extends the CryptoCoverageChecker to provide  * an easier way to check to see if the SOAP (1.1 + 1.2) Body was signed and/or encrypted, if  * the Timestamp was signed, and if the WS-Addressing ReplyTo and FaultTo headers were signed,  * and if a UsernameToken was encrypted.  */
end_comment

begin_class
specifier|public
class|class
name|DefaultCryptoCoverageCheckerTest
extends|extends
name|AbstractSecurityTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testSignedWithIncompleteCoverage
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|runInterceptorAndValidate
argument_list|(
literal|"signed_x509_issuer_serial_missing_signed_header.xml"
argument_list|,
name|this
operator|.
name|getPrefixes
argument_list|()
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|XPathExpression
argument_list|(
literal|"//ser:Header"
argument_list|,
name|CoverageType
operator|.
name|SIGNED
argument_list|,
name|CoverageScope
operator|.
name|ELEMENT
argument_list|)
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// This is mostly testing that things work with no prefixes.
name|this
operator|.
name|runInterceptorAndValidate
argument_list|(
literal|"signed_x509_issuer_serial_missing_signed_header.xml"
argument_list|,
literal|null
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|XPathExpression
argument_list|(
literal|"//*"
argument_list|,
name|CoverageType
operator|.
name|SIGNED
argument_list|,
name|CoverageScope
operator|.
name|ELEMENT
argument_list|)
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// This fails as the SOAP Body is not signed
name|this
operator|.
name|runInterceptorAndValidate
argument_list|(
literal|"signed_x509_issuer_serial_missing_signed_header.xml"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSignedWithCompleteCoverage
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|runInterceptorAndValidate
argument_list|(
literal|"signed_x509_issuer_serial.xml"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|runInterceptorAndValidate
argument_list|(
literal|"signed_x509_issuer_serial.xml"
argument_list|,
name|this
operator|.
name|getPrefixes
argument_list|()
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|XPathExpression
argument_list|(
literal|"//ser:Header"
argument_list|,
name|CoverageType
operator|.
name|SIGNED
argument_list|,
name|CoverageScope
operator|.
name|ELEMENT
argument_list|)
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getPrefixes
parameter_list|()
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|prefixes
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|prefixes
operator|.
name|put
argument_list|(
literal|"ser"
argument_list|,
literal|"http://www.sdj.pl"
argument_list|)
expr_stmt|;
return|return
name|prefixes
return|;
block|}
specifier|private
name|void
name|runInterceptorAndValidate
parameter_list|(
name|String
name|document
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|prefixes
parameter_list|,
name|List
argument_list|<
name|XPathExpression
argument_list|>
name|xpaths
parameter_list|,
name|boolean
name|pass
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|Document
name|doc
init|=
name|this
operator|.
name|readDocument
argument_list|(
name|document
argument_list|)
decl_stmt|;
specifier|final
name|SoapMessage
name|msg
init|=
name|this
operator|.
name|getSoapMessageForDom
argument_list|(
name|doc
argument_list|)
decl_stmt|;
specifier|final
name|CryptoCoverageChecker
name|checker
init|=
operator|new
name|DefaultCryptoCoverageChecker
argument_list|()
decl_stmt|;
name|checker
operator|.
name|addPrefixes
argument_list|(
name|prefixes
argument_list|)
expr_stmt|;
name|checker
operator|.
name|addXPaths
argument_list|(
name|xpaths
argument_list|)
expr_stmt|;
specifier|final
name|PhaseInterceptor
argument_list|<
name|SoapMessage
argument_list|>
name|wss4jInInterceptor
init|=
name|this
operator|.
name|getWss4jInInterceptor
argument_list|()
decl_stmt|;
name|wss4jInInterceptor
operator|.
name|handleMessage
argument_list|(
name|msg
argument_list|)
expr_stmt|;
try|try
block|{
name|checker
operator|.
name|handleMessage
argument_list|(
name|msg
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|pass
condition|)
block|{
name|fail
argument_list|(
literal|"Passed interceptor erroneously."
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Fault
name|e
parameter_list|)
block|{
if|if
condition|(
name|pass
condition|)
block|{
name|fail
argument_list|(
literal|"Failed interceptor erroneously."
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"element found matching XPath"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|PhaseInterceptor
argument_list|<
name|SoapMessage
argument_list|>
name|getWss4jInInterceptor
parameter_list|()
block|{
specifier|final
name|WSS4JInInterceptor
name|inHandler
init|=
operator|new
name|WSS4JInInterceptor
argument_list|(
literal|true
argument_list|)
decl_stmt|;
specifier|final
name|String
name|action
init|=
name|WSHandlerConstants
operator|.
name|SIGNATURE
operator|+
literal|" "
operator|+
name|WSHandlerConstants
operator|.
name|ENCRYPT
decl_stmt|;
name|inHandler
operator|.
name|setProperty
argument_list|(
name|WSHandlerConstants
operator|.
name|ACTION
argument_list|,
name|action
argument_list|)
expr_stmt|;
name|inHandler
operator|.
name|setProperty
argument_list|(
name|WSHandlerConstants
operator|.
name|SIG_VER_PROP_FILE
argument_list|,
literal|"insecurity.properties"
argument_list|)
expr_stmt|;
name|inHandler
operator|.
name|setProperty
argument_list|(
name|WSHandlerConstants
operator|.
name|DEC_PROP_FILE
argument_list|,
literal|"insecurity.properties"
argument_list|)
expr_stmt|;
name|inHandler
operator|.
name|setProperty
argument_list|(
name|WSHandlerConstants
operator|.
name|PW_CALLBACK_CLASS
argument_list|,
name|TestPwdCallback
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|inHandler
operator|.
name|setProperty
argument_list|(
name|WSHandlerConstants
operator|.
name|IS_BSP_COMPLIANT
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
return|return
name|inHandler
return|;
block|}
block|}
end_class

end_unit

