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
name|provider
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
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
name|Map
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
name|jaxrs
operator|.
name|resources
operator|.
name|Book
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
name|jaxrs
operator|.
name|resources
operator|.
name|BookStore
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

begin_class
specifier|public
class|class
name|XPathProviderTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testIsReadableClassName
parameter_list|()
block|{
name|XPathProvider
argument_list|<
name|?
argument_list|>
name|provider
init|=
operator|new
name|XPathProvider
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|provider
operator|.
name|setExpression
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|provider
operator|.
name|isReadable
argument_list|(
name|Book
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|provider
operator|.
name|isReadable
argument_list|(
name|BookStore
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|provider
operator|.
name|setClassName
argument_list|(
name|Book
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|provider
operator|.
name|isReadable
argument_list|(
name|BookStore
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|provider
operator|.
name|isReadable
argument_list|(
name|Book
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|provider
operator|.
name|setClassName
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|provider
operator|.
name|isReadable
argument_list|(
name|Book
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|provider
operator|.
name|isReadable
argument_list|(
name|BookStore
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsReadableClassNames
parameter_list|()
block|{
name|XPathProvider
argument_list|<
name|?
argument_list|>
name|provider
init|=
operator|new
name|XPathProvider
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|provider
operator|.
name|isReadable
argument_list|(
name|Book
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|provider
operator|.
name|isReadable
argument_list|(
name|BookStore
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
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
name|map
operator|.
name|put
argument_list|(
name|Book
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"/"
argument_list|)
expr_stmt|;
name|provider
operator|.
name|setExpressions
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|provider
operator|.
name|isReadable
argument_list|(
name|BookStore
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|provider
operator|.
name|isReadable
argument_list|(
name|Book
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadFrom
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|value
init|=
literal|"<Book><name>The Book</name><id>2</id></Book>"
decl_stmt|;
name|XPathProvider
argument_list|<
name|Book
argument_list|>
name|provider
init|=
operator|new
name|XPathProvider
argument_list|<
name|Book
argument_list|>
argument_list|()
decl_stmt|;
name|provider
operator|.
name|setExpression
argument_list|(
literal|"/Book"
argument_list|)
expr_stmt|;
name|provider
operator|.
name|setClassName
argument_list|(
name|Book
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|provider
operator|.
name|setForceDOM
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Book
name|book
init|=
operator|(
name|Book
operator|)
name|provider
operator|.
name|readFrom
argument_list|(
name|Book
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|value
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|book
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2L
argument_list|,
name|book
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The Book"
argument_list|,
name|book
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

