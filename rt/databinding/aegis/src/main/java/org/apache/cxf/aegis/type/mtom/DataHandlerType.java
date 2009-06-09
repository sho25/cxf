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
name|aegis
operator|.
name|type
operator|.
name|mtom
package|;
end_package

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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|aegis
operator|.
name|Context
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
name|helpers
operator|.
name|HttpHeaderHelper
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
name|message
operator|.
name|Attachment
import|;
end_import

begin_class
specifier|public
class|class
name|DataHandlerType
extends|extends
name|AbstractXOPType
block|{
specifier|public
name|DataHandlerType
parameter_list|(
name|boolean
name|useXmimeContentType
parameter_list|,
name|String
name|expectedContentTypes
parameter_list|)
block|{
name|super
argument_list|(
name|useXmimeContentType
argument_list|,
name|expectedContentTypes
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|readAttachment
parameter_list|(
name|Attachment
name|att
parameter_list|,
name|Context
name|context
parameter_list|)
block|{
return|return
name|att
operator|.
name|getDataHandler
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Attachment
name|createAttachment
parameter_list|(
name|Object
name|object
parameter_list|,
name|String
name|id
parameter_list|)
block|{
name|DataHandler
name|handler
init|=
operator|(
name|DataHandler
operator|)
name|object
decl_stmt|;
name|AttachmentImpl
name|att
init|=
operator|new
name|AttachmentImpl
argument_list|(
name|id
argument_list|,
name|handler
argument_list|)
decl_stmt|;
name|att
operator|.
name|setXOP
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|att
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|getContentType
parameter_list|(
name|Object
name|object
parameter_list|,
name|Context
name|context
parameter_list|)
block|{
return|return
operator|(
operator|(
name|DataHandler
operator|)
name|object
operator|)
operator|.
name|getContentType
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|wrapBytes
parameter_list|(
name|byte
index|[]
name|bareBytes
parameter_list|,
name|String
name|contentType
parameter_list|)
block|{
comment|// for the benefit of those who are working with string data, we have the following
comment|// trickery
name|String
name|charset
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|contentType
operator|!=
literal|null
operator|&&
name|contentType
operator|.
name|indexOf
argument_list|(
literal|"text/"
argument_list|)
operator|!=
operator|-
literal|1
operator|&&
name|contentType
operator|.
name|indexOf
argument_list|(
literal|"charset"
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|charset
operator|=
name|contentType
operator|.
name|substring
argument_list|(
name|contentType
operator|.
name|indexOf
argument_list|(
literal|"charset"
argument_list|)
operator|+
literal|8
argument_list|)
expr_stmt|;
if|if
condition|(
name|charset
operator|.
name|indexOf
argument_list|(
literal|";"
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|charset
operator|=
name|charset
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|charset
operator|.
name|indexOf
argument_list|(
literal|";"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|normalizedEncoding
init|=
name|HttpHeaderHelper
operator|.
name|mapCharset
argument_list|(
name|charset
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
try|try
block|{
name|String
name|stringData
init|=
operator|new
name|String
argument_list|(
name|bareBytes
argument_list|,
name|normalizedEncoding
argument_list|)
decl_stmt|;
return|return
operator|new
name|DataHandler
argument_list|(
name|stringData
argument_list|,
name|contentType
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
comment|// this space intentionally left blank.
block|}
return|return
operator|new
name|DataHandler
argument_list|(
name|bareBytes
argument_list|,
name|contentType
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|byte
index|[]
name|getBytes
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|DataHandler
name|handler
init|=
operator|(
name|DataHandler
operator|)
name|object
decl_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|InputStream
name|stream
init|=
name|handler
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|stream
argument_list|,
name|baos
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
name|baos
operator|.
name|toByteArray
argument_list|()
return|;
block|}
block|}
end_class

end_unit

