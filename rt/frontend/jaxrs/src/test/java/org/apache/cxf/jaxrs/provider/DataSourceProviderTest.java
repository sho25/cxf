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
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|DataHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|DataSource
import|;
end_import

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
name|MediaType
import|;
end_import

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
name|MultivaluedMap
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
name|helpers
operator|.
name|IOUtils
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
name|ext
operator|.
name|multipart
operator|.
name|InputStreamDataSource
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
name|impl
operator|.
name|MetadataMap
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
name|DataSourceProviderTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testReadDataHandler
parameter_list|()
throws|throws
name|Exception
block|{
name|DataSourceProvider
argument_list|<
name|DataHandler
argument_list|>
name|p
init|=
operator|new
name|DataSourceProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|DataHandler
name|ds
init|=
name|p
operator|.
name|readFrom
argument_list|(
name|DataHandler
operator|.
name|class
argument_list|,
literal|null
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|MediaType
operator|.
name|valueOf
argument_list|(
literal|"image/png"
argument_list|)
argument_list|,
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"image"
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"image"
argument_list|,
name|IOUtils
operator|.
name|readStringFromStream
argument_list|(
name|ds
operator|.
name|getDataSource
argument_list|()
operator|.
name|getInputStream
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWriteDataHandler
parameter_list|()
throws|throws
name|Exception
block|{
name|DataSourceProvider
argument_list|<
name|DataHandler
argument_list|>
name|p
init|=
operator|new
name|DataSourceProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|DataHandler
name|ds
init|=
operator|new
name|DataHandler
argument_list|(
operator|new
name|InputStreamDataSource
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"image"
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|,
literal|"image/png"
argument_list|)
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|os
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|p
operator|.
name|writeTo
argument_list|(
name|ds
argument_list|,
name|DataHandler
operator|.
name|class
argument_list|,
name|DataHandler
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|MediaType
operator|.
name|valueOf
argument_list|(
literal|"image/png"
argument_list|)
argument_list|,
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"image"
argument_list|,
name|os
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
name|testReadDataSource
parameter_list|()
throws|throws
name|Exception
block|{
name|DataSourceProvider
argument_list|<
name|DataSource
argument_list|>
name|p
init|=
operator|new
name|DataSourceProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|DataSource
name|ds
init|=
name|p
operator|.
name|readFrom
argument_list|(
name|DataSource
operator|.
name|class
argument_list|,
literal|null
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|MediaType
operator|.
name|valueOf
argument_list|(
literal|"image/png"
argument_list|)
argument_list|,
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"image"
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"image"
argument_list|,
name|IOUtils
operator|.
name|readStringFromStream
argument_list|(
name|ds
operator|.
name|getInputStream
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWriteDataSource
parameter_list|()
throws|throws
name|Exception
block|{
name|DataSourceProvider
argument_list|<
name|DataSource
argument_list|>
name|p
init|=
operator|new
name|DataSourceProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|DataSource
name|ds
init|=
operator|new
name|InputStreamDataSource
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"image"
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|,
literal|"image/png"
argument_list|)
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|outHeaders
init|=
operator|new
name|MetadataMap
argument_list|<>
argument_list|()
decl_stmt|;
try|try
init|(
name|ByteArrayOutputStream
name|os
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
init|)
block|{
name|p
operator|.
name|writeTo
argument_list|(
name|ds
argument_list|,
name|DataSource
operator|.
name|class
argument_list|,
name|DataSource
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|MediaType
operator|.
name|valueOf
argument_list|(
literal|"image/png"
argument_list|)
argument_list|,
name|outHeaders
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"image"
argument_list|,
name|os
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|outHeaders
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
name|testWriteDataSourceWithDiffCT
parameter_list|()
throws|throws
name|Exception
block|{
name|DataSourceProvider
argument_list|<
name|DataSource
argument_list|>
name|p
init|=
operator|new
name|DataSourceProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|p
operator|.
name|setUseDataSourceContentType
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|DataSource
name|ds
init|=
operator|new
name|InputStreamDataSource
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"image"
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|,
literal|"image/png"
argument_list|)
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|outHeaders
init|=
operator|new
name|MetadataMap
argument_list|<>
argument_list|()
decl_stmt|;
try|try
init|(
name|ByteArrayOutputStream
name|os
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
init|)
block|{
name|p
operator|.
name|writeTo
argument_list|(
name|ds
argument_list|,
name|DataSource
operator|.
name|class
argument_list|,
name|DataSource
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|MediaType
operator|.
name|valueOf
argument_list|(
literal|"image/jpeg"
argument_list|)
argument_list|,
name|outHeaders
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"image"
argument_list|,
name|os
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|"image/png"
argument_list|,
name|outHeaders
operator|.
name|getFirst
argument_list|(
literal|"Content-Type"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

