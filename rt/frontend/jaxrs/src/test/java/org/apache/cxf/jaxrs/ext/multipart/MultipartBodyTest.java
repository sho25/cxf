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
name|ext
operator|.
name|multipart
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|mail
operator|.
name|util
operator|.
name|ByteArrayDataSource
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
name|assertNull
import|;
end_import

begin_class
specifier|public
class|class
name|MultipartBodyTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testGetAttachments
parameter_list|()
block|{
name|List
argument_list|<
name|Attachment
argument_list|>
name|atts
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|atts
operator|.
name|add
argument_list|(
name|createAttachment
argument_list|(
literal|"p1"
argument_list|)
argument_list|)
expr_stmt|;
name|atts
operator|.
name|add
argument_list|(
name|createAttachment
argument_list|(
literal|"p2"
argument_list|)
argument_list|)
expr_stmt|;
name|MultipartBody
name|b
init|=
operator|new
name|MultipartBody
argument_list|(
name|atts
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|atts
argument_list|,
name|b
operator|.
name|getAllAttachments
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|atts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|b
operator|.
name|getRootAttachment
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|atts
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
name|b
operator|.
name|getChildAttachments
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetAttachmentsById
parameter_list|()
block|{
name|List
argument_list|<
name|Attachment
argument_list|>
name|atts
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|atts
operator|.
name|add
argument_list|(
name|createAttachment
argument_list|(
literal|"p1"
argument_list|)
argument_list|)
expr_stmt|;
name|atts
operator|.
name|add
argument_list|(
name|createAttachment
argument_list|(
literal|"p2"
argument_list|)
argument_list|)
expr_stmt|;
name|MultipartBody
name|b
init|=
operator|new
name|MultipartBody
argument_list|(
name|atts
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|atts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|b
operator|.
name|getAttachment
argument_list|(
literal|"p1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|atts
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
name|b
operator|.
name|getAttachment
argument_list|(
literal|"p2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|b
operator|.
name|getAttachment
argument_list|(
literal|"p3"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Attachment
name|createAttachment
parameter_list|(
name|String
name|id
parameter_list|)
block|{
return|return
operator|new
name|Attachment
argument_list|(
name|id
argument_list|,
operator|new
name|DataHandler
argument_list|(
operator|new
name|ByteArrayDataSource
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|1
block|}
argument_list|,
literal|"application/octet-stream"
argument_list|)
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
argument_list|)
return|;
block|}
block|}
end_class

end_unit

