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
name|jaxws
operator|.
name|context
package|;
end_package

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
name|attachment
operator|.
name|AttachmentImpl
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
name|message
operator|.
name|Attachment
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

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|WrappedAttachmentsTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testCreateAndModify
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|DataHandler
argument_list|>
name|content
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|content
operator|.
name|put
argument_list|(
literal|"att-1"
argument_list|,
operator|new
name|DataHandler
argument_list|(
operator|new
name|ByteArrayDataSource
argument_list|(
literal|"Hello world!"
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|"text/plain"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|content
operator|.
name|put
argument_list|(
literal|"att-2"
argument_list|,
operator|new
name|DataHandler
argument_list|(
operator|new
name|ByteArrayDataSource
argument_list|(
literal|"Hola mundo!"
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|"text/plain"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|WrappedAttachments
name|attachments
init|=
operator|new
name|WrappedAttachments
argument_list|(
name|content
argument_list|)
decl_stmt|;
name|Attachment
name|att3
init|=
operator|new
name|AttachmentImpl
argument_list|(
literal|"att-3"
argument_list|,
operator|new
name|DataHandler
argument_list|(
operator|new
name|ByteArrayDataSource
argument_list|(
literal|"Bonjour tout le monde!"
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|"text/plain"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|attachments
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|attachments
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|attachments
operator|.
name|add
argument_list|(
name|att3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|attachments
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|attachments
operator|.
name|add
argument_list|(
name|att3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|attachments
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|attachments
operator|.
name|remove
argument_list|(
name|att3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|attachments
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Attachment
name|attx
init|=
name|attachments
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|attachments
operator|.
name|remove
argument_list|(
name|attx
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|attachments
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Attachment
index|[]
name|atts
init|=
name|attachments
operator|.
name|toArray
argument_list|(
operator|new
name|Attachment
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
comment|//NOPMD - explicitly test this
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|atts
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"att-1"
operator|.
name|equals
argument_list|(
name|attx
operator|.
name|getId
argument_list|()
argument_list|)
condition|?
literal|"att-2"
else|:
literal|"att-1"
argument_list|,
name|atts
index|[
literal|0
index|]
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|atts
operator|=
name|attachments
operator|.
name|toArray
argument_list|(
operator|new
name|Attachment
index|[
name|attachments
operator|.
name|size
argument_list|()
index|]
argument_list|)
expr_stmt|;
comment|//NOPMD - explicitly test this
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|atts
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"att-1"
operator|.
name|equals
argument_list|(
name|attx
operator|.
name|getId
argument_list|()
argument_list|)
condition|?
literal|"att-2"
else|:
literal|"att-1"
argument_list|,
name|atts
index|[
literal|0
index|]
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|o
index|[]
init|=
name|attachments
operator|.
name|toArray
argument_list|()
decl_stmt|;
comment|//NOPMD - explicitly test this
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|o
operator|.
name|length
argument_list|)
expr_stmt|;
name|Attachment
name|a
init|=
operator|(
name|Attachment
operator|)
name|o
index|[
literal|0
index|]
decl_stmt|;
name|assertEquals
argument_list|(
literal|"att-1"
operator|.
name|equals
argument_list|(
name|attx
operator|.
name|getId
argument_list|()
argument_list|)
condition|?
literal|"att-2"
else|:
literal|"att-1"
argument_list|,
name|a
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|attachments
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|attachments
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|content
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

