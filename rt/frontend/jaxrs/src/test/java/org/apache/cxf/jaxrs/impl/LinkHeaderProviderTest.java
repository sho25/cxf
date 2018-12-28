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
name|jaxrs
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Link
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
name|LinkHeaderProviderTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testFromSimpleString
parameter_list|()
block|{
name|Link
name|l
init|=
name|Link
operator|.
name|valueOf
argument_list|(
literal|"<http://bar>"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://bar"
argument_list|,
name|l
operator|.
name|getUri
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFromSimpleString2
parameter_list|()
block|{
name|Link
name|l
init|=
name|Link
operator|.
name|valueOf
argument_list|(
literal|"</>"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"/"
argument_list|,
name|l
operator|.
name|getUri
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFromComplexString
parameter_list|()
block|{
name|Link
name|l
init|=
name|Link
operator|.
name|valueOf
argument_list|(
literal|"<http://bar>;rel=next;title=\"Next Link\";type=text/xml;method=get"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://bar"
argument_list|,
name|l
operator|.
name|getUri
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|rel
init|=
name|l
operator|.
name|getRel
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"next"
argument_list|,
name|rel
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Next Link"
argument_list|,
name|l
operator|.
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"text/xml"
argument_list|,
name|l
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"get"
argument_list|,
name|l
operator|.
name|getParams
argument_list|()
operator|.
name|get
argument_list|(
literal|"method"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testToString
parameter_list|()
block|{
name|String
name|headerValue
init|=
literal|"<http://bar>;rel=next;title=\"Next Link\";type=text/xml;method=get"
decl_stmt|;
name|String
name|expected
init|=
literal|"<http://bar>;rel=\"next\";title=\"Next Link\";type=\"text/xml\";method=\"get\""
decl_stmt|;
name|Link
name|l
init|=
name|Link
operator|.
name|valueOf
argument_list|(
name|headerValue
argument_list|)
decl_stmt|;
name|String
name|result
init|=
name|l
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

