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
name|common
operator|.
name|i18n
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ResourceBundle
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

begin_class
specifier|public
class|class
name|BundleUtilsTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testGetBundleName
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"unexpected resource bundle name"
argument_list|,
literal|"org.apache.cxf.common.i18n.Messages"
argument_list|,
name|BundleUtils
operator|.
name|getBundleName
argument_list|(
name|getClass
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected resource bundle name"
argument_list|,
literal|"org.apache.cxf.common.i18n.Messages"
argument_list|,
name|BundleUtils
operator|.
name|getBundleName
argument_list|(
name|getClass
argument_list|()
argument_list|,
literal|"Messages"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBundle
parameter_list|()
throws|throws
name|Exception
block|{
name|ResourceBundle
name|bundle
init|=
name|BundleUtils
operator|.
name|getBundle
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"expected resource bundle"
argument_list|,
name|bundle
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected resource"
argument_list|,
literal|"localized message"
argument_list|,
name|bundle
operator|.
name|getString
argument_list|(
literal|"I18N_MSG"
argument_list|)
argument_list|)
expr_stmt|;
name|ResourceBundle
name|nonDefaultBundle
init|=
name|BundleUtils
operator|.
name|getBundle
argument_list|(
name|getClass
argument_list|()
argument_list|,
literal|"Messages"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"expected resource bundle"
argument_list|,
name|nonDefaultBundle
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected resource"
argument_list|,
literal|"localized message"
argument_list|,
name|nonDefaultBundle
operator|.
name|getString
argument_list|(
literal|"I18N_MSG"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

