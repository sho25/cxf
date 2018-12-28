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
name|assertFalse
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
name|MessagePartInfoTest
extends|extends
name|Assert
block|{
specifier|private
name|MessagePartInfo
name|messagePartInfo
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
name|MessageInfo
name|msg
init|=
operator|new
name|MessageInfo
argument_list|(
literal|null
argument_list|,
name|MessageInfo
operator|.
name|Type
operator|.
name|INPUT
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http/types"
argument_list|,
literal|"testMessage"
argument_list|)
argument_list|)
decl_stmt|;
name|messagePartInfo
operator|=
operator|new
name|MessagePartInfo
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"testMessagePart"
argument_list|)
argument_list|,
name|msg
argument_list|)
expr_stmt|;
name|messagePartInfo
operator|.
name|setElement
argument_list|(
literal|true
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
name|messagePartInfo
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|,
literal|"testMessagePart"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|messagePartInfo
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
name|messagePartInfo
operator|.
name|setName
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http1"
argument_list|,
literal|"testMessagePart1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|messagePartInfo
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|,
literal|"testMessagePart1"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|messagePartInfo
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
name|testElement
parameter_list|()
block|{
name|messagePartInfo
operator|.
name|setElementQName
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http/types"
argument_list|,
literal|"testElement"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|messagePartInfo
operator|.
name|isElement
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|messagePartInfo
operator|.
name|getElementQName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|,
literal|"testElement"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|messagePartInfo
operator|.
name|getElementQName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
literal|"http://apache.org/hello_world_soap_http/types"
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|messagePartInfo
operator|.
name|getTypeQName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testType
parameter_list|()
block|{
name|messagePartInfo
operator|.
name|setTypeQName
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http/types"
argument_list|,
literal|"testType"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|messagePartInfo
operator|.
name|getElementQName
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|messagePartInfo
operator|.
name|isElement
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|messagePartInfo
operator|.
name|getTypeQName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|,
literal|"testType"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|messagePartInfo
operator|.
name|getTypeQName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
literal|"http://apache.org/hello_world_soap_http/types"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

