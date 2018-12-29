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
name|Assert
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
name|fail
import|;
end_import

begin_class
specifier|public
class|class
name|InterfaceInfoTest
block|{
specifier|private
name|InterfaceInfo
name|interfaceInfo
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
name|interfaceInfo
operator|=
operator|new
name|InterfaceInfo
argument_list|(
operator|new
name|ServiceInfo
argument_list|()
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"interfaceTest"
argument_list|)
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
name|interfaceInfo
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|,
literal|"interfaceTest"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|interfaceInfo
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
name|QName
name|qname
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http1"
argument_list|,
literal|"interfaceTest1"
argument_list|)
decl_stmt|;
name|interfaceInfo
operator|.
name|setName
argument_list|(
name|qname
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|interfaceInfo
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|,
literal|"interfaceTest1"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|interfaceInfo
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
literal|"http://apache.org/hello_world_soap_http1"
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
name|QName
name|name
init|=
operator|new
name|QName
argument_list|(
literal|"urn:test:ns"
argument_list|,
literal|"sayHi"
argument_list|)
decl_stmt|;
name|interfaceInfo
operator|.
name|addOperation
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"sayHi"
argument_list|,
name|interfaceInfo
operator|.
name|getOperation
argument_list|(
name|name
argument_list|)
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|interfaceInfo
operator|.
name|addOperation
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:test:ns"
argument_list|,
literal|"greetMe"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|interfaceInfo
operator|.
name|getOperations
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|boolean
name|duplicatedOperationName
init|=
literal|false
decl_stmt|;
try|try
block|{
name|interfaceInfo
operator|.
name|addOperation
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
literal|"An operation with name [{urn:test:ns}sayHi] already exists in this service"
argument_list|)
expr_stmt|;
name|duplicatedOperationName
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|duplicatedOperationName
condition|)
block|{
name|fail
argument_list|(
literal|"should get IllegalArgumentException"
argument_list|)
expr_stmt|;
block|}
name|boolean
name|isNull
init|=
literal|false
decl_stmt|;
try|try
block|{
name|QName
name|qname
init|=
literal|null
decl_stmt|;
name|interfaceInfo
operator|.
name|addOperation
argument_list|(
name|qname
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|e
parameter_list|)
block|{
name|isNull
operator|=
literal|true
expr_stmt|;
name|assertEquals
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
literal|"Operation Name cannot be null."
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|isNull
condition|)
block|{
name|fail
argument_list|(
literal|"should get NullPointerException"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

