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
name|ws
operator|.
name|policy
operator|.
name|AssertionInfoMap
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
name|PolicyException
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
name|policy
operator|.
name|interceptors
operator|.
name|SecurityVerificationOutInterceptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Policy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|IMocksControl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|SecurityVerificationOutTest
extends|extends
name|AbstractPolicySecurityTest
block|{
specifier|private
name|IMocksControl
name|control
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|control
operator|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|PolicyException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testEncryptedPartsNoBinding
parameter_list|()
throws|throws
name|Exception
block|{
name|SoapMessage
name|message
init|=
name|coachMessage
argument_list|(
literal|"encrypted_parts_missing_binding.xml"
argument_list|)
decl_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|SecurityVerificationOutInterceptor
operator|.
name|INSTANCE
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|PolicyException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testSignedPartsNoBinding
parameter_list|()
throws|throws
name|Exception
block|{
name|SoapMessage
name|message
init|=
name|coachMessage
argument_list|(
literal|"signed_parts_missing_binding.xml"
argument_list|)
decl_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|SecurityVerificationOutInterceptor
operator|.
name|INSTANCE
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEncryptedPartsOK
parameter_list|()
throws|throws
name|Exception
block|{
name|SoapMessage
name|message
init|=
name|coachMessage
argument_list|(
literal|"encrypted_parts_policy_body.xml"
argument_list|)
decl_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|SecurityVerificationOutInterceptor
operator|.
name|INSTANCE
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSignedPartsOK
parameter_list|()
throws|throws
name|Exception
block|{
name|SoapMessage
name|message
init|=
name|coachMessage
argument_list|(
literal|"signed_parts_policy_body.xml"
argument_list|)
decl_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|SecurityVerificationOutInterceptor
operator|.
name|INSTANCE
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
specifier|private
name|SoapMessage
name|coachMessage
parameter_list|(
name|String
name|policyName
parameter_list|)
throws|throws
name|IOException
throws|,
name|ParserConfigurationException
throws|,
name|SAXException
block|{
name|Policy
name|policy
init|=
name|policyBuilder
operator|.
name|getPolicy
argument_list|(
name|this
operator|.
name|getResourceAsStream
argument_list|(
name|policyName
argument_list|)
argument_list|)
decl_stmt|;
name|AssertionInfoMap
name|aim
init|=
operator|new
name|AssertionInfoMap
argument_list|(
name|policy
argument_list|)
decl_stmt|;
name|SoapMessage
name|message
init|=
name|control
operator|.
name|createMock
argument_list|(
name|SoapMessage
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|REQUESTOR_ROLE
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|message
operator|.
name|get
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|aim
argument_list|)
expr_stmt|;
return|return
name|message
return|;
block|}
block|}
end_class

end_unit

