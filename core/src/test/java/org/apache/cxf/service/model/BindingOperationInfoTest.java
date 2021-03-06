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
name|service
operator|.
name|model
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|BindingOperationInfoTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|TEST_NS
init|=
literal|"urn:test:ns"
decl_stmt|;
specifier|private
name|BindingOperationInfo
name|bindingOperationInfo
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|OperationInfo
name|operationInfo
init|=
operator|new
name|OperationInfo
argument_list|(
literal|null
argument_list|,
operator|new
name|QName
argument_list|(
name|TEST_NS
argument_list|,
literal|"operationTest"
argument_list|)
argument_list|)
decl_stmt|;
name|MessageInfo
name|inputMessage
init|=
name|operationInfo
operator|.
name|createMessage
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"testInputMessage"
argument_list|)
argument_list|,
name|MessageInfo
operator|.
name|Type
operator|.
name|INPUT
argument_list|)
decl_stmt|;
name|operationInfo
operator|.
name|setInput
argument_list|(
literal|"input"
argument_list|,
name|inputMessage
argument_list|)
expr_stmt|;
name|MessageInfo
name|outputMessage
init|=
name|operationInfo
operator|.
name|createMessage
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"testOutputMessage"
argument_list|)
argument_list|,
name|MessageInfo
operator|.
name|Type
operator|.
name|OUTPUT
argument_list|)
decl_stmt|;
name|operationInfo
operator|.
name|setOutput
argument_list|(
literal|"output"
argument_list|,
name|outputMessage
argument_list|)
expr_stmt|;
name|operationInfo
operator|.
name|addFault
argument_list|(
operator|new
name|QName
argument_list|(
name|TEST_NS
argument_list|,
literal|"fault"
argument_list|)
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"faultMessage"
argument_list|)
argument_list|)
expr_stmt|;
name|bindingOperationInfo
operator|=
operator|new
name|BindingOperationInfo
argument_list|(
literal|null
argument_list|,
name|operationInfo
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testName
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|bindingOperationInfo
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|QName
argument_list|(
name|TEST_NS
argument_list|,
literal|"operationTest"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBinding
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNull
argument_list|(
name|bindingOperationInfo
operator|.
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOperation
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|bindingOperationInfo
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|QName
argument_list|(
name|TEST_NS
argument_list|,
literal|"operationTest"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|bindingOperationInfo
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|hasInput
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|bindingOperationInfo
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|hasOutput
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|bindingOperationInfo
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|getInputName
argument_list|()
argument_list|,
literal|"input"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|bindingOperationInfo
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|getOutputName
argument_list|()
argument_list|,
literal|"output"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|bindingOperationInfo
operator|.
name|getFaults
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getFaultInfo
argument_list|()
operator|.
name|getFaultName
argument_list|()
argument_list|,
operator|new
name|QName
argument_list|(
name|TEST_NS
argument_list|,
literal|"fault"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|bindingOperationInfo
operator|.
name|getFaults
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInputMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|BindingMessageInfo
name|inputMessage
init|=
name|bindingOperationInfo
operator|.
name|getInput
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|inputMessage
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|inputMessage
operator|.
name|getMessageInfo
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|,
literal|"testInputMessage"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|inputMessage
operator|.
name|getMessageInfo
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
literal|"http://apache.org/hello_world_soap_http"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOutputMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|BindingMessageInfo
name|outputMessage
init|=
name|bindingOperationInfo
operator|.
name|getOutput
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|outputMessage
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|outputMessage
operator|.
name|getMessageInfo
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|,
literal|"testOutputMessage"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|outputMessage
operator|.
name|getMessageInfo
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
literal|"http://apache.org/hello_world_soap_http"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFaultMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|BindingFaultInfo
name|faultMessage
init|=
name|bindingOperationInfo
operator|.
name|getFaults
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|faultMessage
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|faultMessage
operator|.
name|getFaultInfo
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|,
literal|"faultMessage"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|faultMessage
operator|.
name|getFaultInfo
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
literal|"http://apache.org/hello_world_soap_http"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

