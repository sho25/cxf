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
name|jca
operator|.
name|core
operator|.
name|resourceadapter
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|UriHandlerInitTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|PROP_NAME
init|=
literal|"java.protocol.handler.pkgs"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PKG_ADD
init|=
literal|"do.do"
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testAppendToProp
parameter_list|()
block|{
specifier|final
name|Properties
name|properties
init|=
name|System
operator|.
name|getProperties
argument_list|()
decl_stmt|;
specifier|final
name|String
name|origVal
init|=
name|properties
operator|.
name|getProperty
argument_list|(
name|PROP_NAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|origVal
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|assertTrue
argument_list|(
literal|"pkg has been already been appended"
argument_list|,
name|origVal
operator|.
name|indexOf
argument_list|(
name|PKG_ADD
argument_list|)
operator|==
operator|-
literal|1
argument_list|)
expr_stmt|;
operator|new
name|UriHandlerInit
argument_list|(
name|PKG_ADD
argument_list|)
expr_stmt|;
name|String
name|newValue
init|=
name|properties
operator|.
name|getProperty
argument_list|(
name|PROP_NAME
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"pkg has been appended"
argument_list|,
name|newValue
operator|.
name|indexOf
argument_list|(
name|PKG_ADD
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
specifier|final
name|int
name|len
init|=
name|newValue
operator|.
name|length
argument_list|()
decl_stmt|;
operator|new
name|UriHandlerInit
argument_list|(
name|PKG_ADD
argument_list|)
expr_stmt|;
name|newValue
operator|=
name|properties
operator|.
name|getProperty
argument_list|(
name|PROP_NAME
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"prop has not been appended twice, size is unchanged, newVal="
operator|+
name|newValue
operator|.
name|length
argument_list|()
argument_list|,
name|newValue
operator|.
name|length
argument_list|()
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|properties
operator|.
name|put
argument_list|(
name|PROP_NAME
argument_list|,
name|origVal
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

