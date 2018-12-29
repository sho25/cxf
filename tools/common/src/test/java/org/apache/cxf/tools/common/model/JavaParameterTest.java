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
name|tools
operator|.
name|common
operator|.
name|model
package|;
end_package

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

begin_class
specifier|public
class|class
name|JavaParameterTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testGetHolderDefaultTypeValue
parameter_list|()
throws|throws
name|Exception
block|{
name|JavaParameter
name|holderParameter
init|=
operator|new
name|JavaParameter
argument_list|(
literal|"i"
argument_list|,
literal|"java.lang.String"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|holderParameter
operator|.
name|setHolder
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|holderParameter
operator|.
name|setHolderName
argument_list|(
literal|"javax.xml.ws.Holder"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"\"\""
argument_list|,
name|holderParameter
operator|.
name|getDefaultTypeValue
argument_list|()
argument_list|)
expr_stmt|;
name|holderParameter
operator|=
operator|new
name|JavaParameter
argument_list|(
literal|"org.apache.cxf.tools.common.model.JavaParameter"
argument_list|,
literal|"org.apache.cxf.tools.common.model.JavaParameter"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|holderParameter
operator|.
name|setHolder
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|holderParameter
operator|.
name|setHolderName
argument_list|(
literal|"javax.xml.ws.Holder"
argument_list|)
expr_stmt|;
name|String
name|defaultTypeValue
init|=
name|holderParameter
operator|.
name|getDefaultTypeValue
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"new org.apache.cxf.tools.common.model.JavaParameter()"
argument_list|,
name|defaultTypeValue
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

