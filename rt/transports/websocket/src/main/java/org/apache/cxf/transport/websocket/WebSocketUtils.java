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
name|transport
operator|.
name|websocket
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
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|WebSocketUtils
block|{
specifier|static
specifier|final
name|String
name|URI_KEY
init|=
literal|"$uri"
decl_stmt|;
specifier|static
specifier|final
name|String
name|METHOD_KEY
init|=
literal|"$method"
decl_stmt|;
specifier|static
specifier|final
name|String
name|SC_KEY
init|=
literal|"$sc"
decl_stmt|;
specifier|static
specifier|final
name|String
name|SM_KEY
init|=
literal|"$sm"
decl_stmt|;
specifier|static
specifier|final
name|String
name|FLUSHED_KEY
init|=
literal|"$flushed"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|CRLF
init|=
literal|"\r\n"
operator|.
name|getBytes
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|COLSP
init|=
literal|": "
operator|.
name|getBytes
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_SC
init|=
literal|"200"
decl_stmt|;
specifier|private
name|WebSocketUtils
parameter_list|()
block|{     }
comment|/**      * Read header properties from the specified input stream.      *        * Only a restricted syntax is allowed as the syntax is in our control.      * Not allowed are:      * - multiline or line-wrapped headers are not not      * - charset other than utf-8. (although i would have preferred iso-8859-1 ;-)      *       * @param in the input stream      * @return a map of name value pairs.      * @throws IOException      */
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|readHeaders
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
init|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|String
operator|.
name|CASE_INSENSITIVE_ORDER
argument_list|)
decl_stmt|;
comment|// read the request line
name|String
name|line
init|=
name|readLine
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|int
name|del
init|=
name|line
operator|.
name|indexOf
argument_list|(
literal|' '
argument_list|)
decl_stmt|;
if|if
condition|(
name|del
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"invalid request: "
operator|+
name|line
argument_list|)
throw|;
block|}
name|headers
operator|.
name|put
argument_list|(
name|METHOD_KEY
argument_list|,
name|line
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|del
argument_list|)
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|URI_KEY
argument_list|,
name|line
operator|.
name|substring
argument_list|(
name|del
operator|+
literal|1
argument_list|)
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
comment|// read headers
while|while
condition|(
operator|(
name|line
operator|=
name|readLine
argument_list|(
name|in
argument_list|)
operator|)
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|line
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|del
operator|=
name|line
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
expr_stmt|;
if|if
condition|(
name|del
operator|<
literal|0
condition|)
block|{
name|headers
operator|.
name|put
argument_list|(
name|line
operator|.
name|trim
argument_list|()
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|headers
operator|.
name|put
argument_list|(
name|line
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|del
argument_list|)
operator|.
name|trim
argument_list|()
argument_list|,
name|line
operator|.
name|substring
argument_list|(
name|del
operator|+
literal|1
argument_list|)
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|headers
return|;
block|}
comment|/**      * Read a line terminated by '\n' optionally preceded by '\r' from the       * specified input stream.      * @param in the input stream      * @return      * @throws IOException      */
comment|// this is copied from AttachmentDeserializer with a minor change to restrict the line termination rule.
specifier|public
specifier|static
name|String
name|readLine
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|(
literal|128
argument_list|)
decl_stmt|;
name|int
name|c
decl_stmt|;
while|while
condition|(
operator|(
name|c
operator|=
name|in
operator|.
name|read
argument_list|()
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
comment|// a linefeed is a terminator, always.
if|if
condition|(
name|c
operator|==
literal|'\n'
condition|)
block|{
break|break;
block|}
elseif|else
if|if
condition|(
name|c
operator|==
literal|'\r'
condition|)
block|{
comment|//just ignore the CR.  The next character SHOULD be an NL.  If not, we're
comment|//just going to discard this
continue|continue;
block|}
else|else
block|{
comment|// just add to the buffer
name|buffer
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|c
argument_list|)
expr_stmt|;
block|}
block|}
comment|// no characters found...this was either an eof or a null line.
if|if
condition|(
name|buffer
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Build response bytes with the status and type information specified in the headers.      *      * @param headers      * @param data      * @param offset      * @param length      * @return      */
specifier|public
specifier|static
name|byte
index|[]
name|buildResponse
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|,
name|byte
index|[]
name|data
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
block|{
name|ByteArrayBuilder
name|sb
init|=
operator|new
name|ByteArrayBuilder
argument_list|()
decl_stmt|;
name|String
name|v
init|=
name|headers
operator|.
name|get
argument_list|(
name|SC_KEY
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|v
operator|==
literal|null
condition|?
name|DEFAULT_SC
else|:
name|v
argument_list|)
operator|.
name|append
argument_list|(
name|CRLF
argument_list|)
expr_stmt|;
name|appendHeaders
argument_list|(
name|headers
argument_list|,
name|sb
argument_list|)
expr_stmt|;
name|byte
index|[]
name|longdata
init|=
name|sb
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
if|if
condition|(
name|data
operator|!=
literal|null
operator|&&
name|length
operator|>
literal|0
condition|)
block|{
name|longdata
operator|=
name|buildResponse
argument_list|(
name|longdata
argument_list|,
name|data
argument_list|,
name|offset
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
return|return
name|longdata
return|;
block|}
comment|/**      * Build response bytes with some generated headers.      *      * @param headers      * @param data      * @param offset      * @param length      * @return      */
specifier|public
specifier|static
name|byte
index|[]
name|buildResponse
parameter_list|(
name|byte
index|[]
name|headers
parameter_list|,
name|byte
index|[]
name|data
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
block|{
specifier|final
name|int
name|hlen
init|=
name|headers
operator|!=
literal|null
condition|?
name|headers
operator|.
name|length
else|:
literal|0
decl_stmt|;
name|byte
index|[]
name|longdata
init|=
operator|new
name|byte
index|[
name|length
operator|+
literal|2
operator|+
name|hlen
index|]
decl_stmt|;
if|if
condition|(
name|hlen
operator|>
literal|0
condition|)
block|{
name|System
operator|.
name|arraycopy
argument_list|(
name|headers
argument_list|,
literal|0
argument_list|,
name|longdata
argument_list|,
literal|0
argument_list|,
name|hlen
argument_list|)
expr_stmt|;
block|}
name|longdata
index|[
name|hlen
index|]
operator|=
literal|0x0d
expr_stmt|;
name|longdata
index|[
name|hlen
operator|+
literal|1
index|]
operator|=
literal|0x0a
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|data
argument_list|,
name|offset
argument_list|,
name|longdata
argument_list|,
name|hlen
operator|+
literal|2
argument_list|,
name|length
argument_list|)
expr_stmt|;
return|return
name|longdata
return|;
block|}
comment|/**      * Build response bytes without status and type information.      *      * @param headers      * @param data      * @param offset      * @param length      * @return      */
specifier|public
specifier|static
name|byte
index|[]
name|buildResponse
parameter_list|(
name|byte
index|[]
name|data
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
block|{
return|return
name|buildResponse
argument_list|(
operator|(
name|byte
index|[]
operator|)
literal|null
argument_list|,
name|data
argument_list|,
name|offset
argument_list|,
name|length
argument_list|)
return|;
block|}
specifier|static
name|byte
index|[]
name|buildHeaderLine
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|byte
index|[]
name|hl
init|=
operator|new
name|byte
index|[
name|name
operator|.
name|length
argument_list|()
operator|+
name|COLSP
operator|.
name|length
operator|+
name|value
operator|.
name|length
argument_list|()
operator|+
name|CRLF
operator|.
name|length
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|name
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|0
argument_list|,
name|hl
argument_list|,
literal|0
argument_list|,
name|name
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|COLSP
argument_list|,
literal|0
argument_list|,
name|hl
argument_list|,
name|name
operator|.
name|length
argument_list|()
argument_list|,
name|COLSP
operator|.
name|length
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|value
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|0
argument_list|,
name|hl
argument_list|,
name|name
operator|.
name|length
argument_list|()
operator|+
name|COLSP
operator|.
name|length
argument_list|,
name|value
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|CRLF
argument_list|,
literal|0
argument_list|,
name|hl
argument_list|,
name|name
operator|.
name|length
argument_list|()
operator|+
name|COLSP
operator|.
name|length
operator|+
name|value
operator|.
name|length
argument_list|()
argument_list|,
name|CRLF
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|hl
return|;
block|}
comment|/**      * Build request bytes with the specified method, url, headers, and content entity.      *       * @param method      * @param url      * @param headers      * @param data      * @param offset      * @param length      * @return      */
specifier|public
specifier|static
name|byte
index|[]
name|buildRequest
parameter_list|(
name|String
name|method
parameter_list|,
name|String
name|url
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|,
name|byte
index|[]
name|data
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
block|{
name|ByteArrayBuilder
name|sb
init|=
operator|new
name|ByteArrayBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|method
argument_list|)
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
operator|.
name|append
argument_list|(
name|url
argument_list|)
operator|.
name|append
argument_list|(
name|CRLF
argument_list|)
expr_stmt|;
name|appendHeaders
argument_list|(
name|headers
argument_list|,
name|sb
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|CRLF
argument_list|)
expr_stmt|;
name|byte
index|[]
name|longdata
init|=
name|sb
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
if|if
condition|(
name|data
operator|!=
literal|null
operator|&&
name|length
operator|>
literal|0
condition|)
block|{
specifier|final
name|byte
index|[]
name|hb
init|=
name|longdata
decl_stmt|;
name|longdata
operator|=
operator|new
name|byte
index|[
name|hb
operator|.
name|length
operator|+
name|length
index|]
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|hb
argument_list|,
literal|0
argument_list|,
name|longdata
argument_list|,
literal|0
argument_list|,
name|hb
operator|.
name|length
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|data
argument_list|,
name|offset
argument_list|,
name|longdata
argument_list|,
name|hb
operator|.
name|length
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
return|return
name|longdata
return|;
block|}
specifier|private
specifier|static
name|void
name|appendHeaders
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|,
name|ByteArrayBuilder
name|sb
parameter_list|)
block|{
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|header
range|:
name|headers
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|header
operator|.
name|getKey
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"$"
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|header
operator|.
name|getKey
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
name|COLSP
argument_list|)
operator|.
name|append
argument_list|(
name|header
operator|.
name|getValue
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
name|CRLF
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
specifier|static
class|class
name|ByteArrayBuilder
block|{
specifier|private
name|ByteArrayOutputStream
name|baos
decl_stmt|;
specifier|public
name|ByteArrayBuilder
parameter_list|()
block|{
name|baos
operator|=
operator|new
name|ByteArrayOutputStream
argument_list|()
expr_stmt|;
block|}
specifier|public
name|ByteArrayBuilder
name|append
parameter_list|(
name|byte
index|[]
name|b
parameter_list|)
block|{
try|try
block|{
name|baos
operator|.
name|write
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore;
block|}
return|return
name|this
return|;
block|}
specifier|public
name|ByteArrayBuilder
name|append
parameter_list|(
name|String
name|s
parameter_list|)
block|{
try|try
block|{
name|baos
operator|.
name|write
argument_list|(
name|s
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
return|return
name|this
return|;
block|}
specifier|public
name|ByteArrayBuilder
name|append
parameter_list|(
name|char
name|c
parameter_list|)
block|{
name|baos
operator|.
name|write
argument_list|(
name|c
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|byte
index|[]
name|toByteArray
parameter_list|()
block|{
return|return
name|baos
operator|.
name|toByteArray
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

