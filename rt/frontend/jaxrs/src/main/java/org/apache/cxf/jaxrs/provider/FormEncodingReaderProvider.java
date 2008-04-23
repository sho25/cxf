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
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|ws
operator|.
name|rs
operator|.
name|ConsumeMime
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|MessageBodyReader
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
name|ext
operator|.
name|Provider
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
name|common
operator|.
name|util
operator|.
name|StringUtils
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
name|MetadataMap
import|;
end_import

begin_class
annotation|@
name|ConsumeMime
argument_list|(
literal|"application/x-www-form-urlencoded"
argument_list|)
annotation|@
name|Provider
specifier|public
specifier|final
class|class
name|FormEncodingReaderProvider
implements|implements
name|MessageBodyReader
argument_list|<
name|Object
argument_list|>
block|{
specifier|public
name|boolean
name|isReadable
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|type
operator|.
name|isAssignableFrom
argument_list|(
name|MultivaluedMap
operator|.
name|class
argument_list|)
return|;
block|}
specifier|public
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|readFrom
parameter_list|(
name|Class
argument_list|<
name|Object
argument_list|>
name|type
parameter_list|,
name|MediaType
name|m
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|,
name|InputStream
name|is
parameter_list|)
block|{
try|try
block|{
name|String
name|charset
init|=
literal|"UTF-8"
decl_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|copy
argument_list|(
name|is
argument_list|,
name|bos
argument_list|,
literal|1024
argument_list|)
expr_stmt|;
name|String
name|postBody
init|=
operator|new
name|String
argument_list|(
name|bos
operator|.
name|toByteArray
argument_list|()
argument_list|,
name|charset
argument_list|)
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
init|=
name|getParams
argument_list|(
name|postBody
argument_list|)
decl_stmt|;
return|return
name|params
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|void
name|copy
parameter_list|(
specifier|final
name|InputStream
name|input
parameter_list|,
specifier|final
name|OutputStream
name|output
parameter_list|,
specifier|final
name|int
name|bufferSize
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
name|bufferSize
index|]
decl_stmt|;
name|int
name|n
init|=
literal|0
decl_stmt|;
name|n
operator|=
name|input
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
while|while
condition|(
operator|-
literal|1
operator|!=
name|n
condition|)
block|{
name|output
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|n
argument_list|)
expr_stmt|;
name|n
operator|=
name|input
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Retrieve map of parameters from the passed in message      *      * @param message      * @return a Map of parameters.      */
specifier|protected
specifier|static
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getParams
parameter_list|(
name|String
name|body
parameter_list|)
block|{
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|body
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|parts
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|body
operator|.
name|split
argument_list|(
literal|"&"
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|part
range|:
name|parts
control|)
block|{
name|String
index|[]
name|keyValue
init|=
name|part
operator|.
name|split
argument_list|(
literal|"="
argument_list|)
decl_stmt|;
comment|// Change to add blank string if key but not value is specified
if|if
condition|(
name|keyValue
operator|.
name|length
operator|==
literal|2
condition|)
block|{
name|params
operator|.
name|add
argument_list|(
name|keyValue
index|[
literal|0
index|]
argument_list|,
name|keyValue
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|params
operator|.
name|add
argument_list|(
name|keyValue
index|[
literal|0
index|]
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|params
return|;
block|}
block|}
end_class

end_unit

