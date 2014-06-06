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
name|rs
operator|.
name|security
operator|.
name|oauth2
operator|.
name|utils
package|;
end_package

begin_comment
comment|/**  * Base64 URL Encoding/Decoding utility.  *    * Character 62 ('+') is '-', Character 63 ('/') is '_';  * Padding characters are dropped after the encoding.     *                    */
end_comment

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
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|i18n
operator|.
name|Message
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
name|logging
operator|.
name|LogUtils
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
name|Base64Exception
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
name|Base64Utility
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|Base64UrlUtility
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|Base64UrlUtility
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Base64UrlUtility
parameter_list|()
block|{
comment|//utility class, never constructed
block|}
specifier|public
specifier|static
name|byte
index|[]
name|decode
parameter_list|(
name|String
name|encoded
parameter_list|)
throws|throws
name|Base64Exception
block|{
name|encoded
operator|=
name|encoded
operator|.
name|replace
argument_list|(
literal|"-"
argument_list|,
literal|"+"
argument_list|)
operator|.
name|replace
argument_list|(
literal|'_'
argument_list|,
literal|'/'
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|encoded
operator|.
name|length
argument_list|()
operator|%
literal|4
condition|)
block|{
case|case
literal|0
case|:
break|break;
case|case
literal|2
case|:
name|encoded
operator|+=
literal|"=="
expr_stmt|;
break|break;
case|case
literal|3
case|:
name|encoded
operator|+=
literal|"="
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|Base64Exception
argument_list|(
operator|new
name|Message
argument_list|(
literal|"BASE64_RUNTIME_EXCEPTION"
argument_list|,
name|LOG
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|Base64Utility
operator|.
name|decode
argument_list|(
name|encoded
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|encode
parameter_list|(
name|String
name|str
parameter_list|)
block|{
try|try
block|{
return|return
name|encode
argument_list|(
name|str
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|static
name|String
name|encode
parameter_list|(
name|byte
index|[]
name|id
parameter_list|)
block|{
return|return
name|encodeChunk
argument_list|(
name|id
argument_list|,
literal|0
argument_list|,
name|id
operator|.
name|length
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|encodeChunk
parameter_list|(
name|byte
index|[]
name|id
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
block|{
name|char
index|[]
name|chunk
init|=
name|Base64Utility
operator|.
name|encodeChunk
argument_list|(
name|id
argument_list|,
name|offset
argument_list|,
name|length
argument_list|)
decl_stmt|;
if|if
condition|(
name|chunk
operator|!=
literal|null
condition|)
block|{
name|String
name|encoded
init|=
operator|new
name|String
argument_list|(
name|chunk
argument_list|)
decl_stmt|;
return|return
name|encoded
operator|.
name|replace
argument_list|(
literal|"+"
argument_list|,
literal|"-"
argument_list|)
operator|.
name|replace
argument_list|(
literal|'/'
argument_list|,
literal|'_'
argument_list|)
operator|.
name|replace
argument_list|(
literal|"="
argument_list|,
literal|""
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

