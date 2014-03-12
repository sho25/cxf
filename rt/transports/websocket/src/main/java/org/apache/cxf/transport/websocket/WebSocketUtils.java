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
name|String
name|CRLF
init|=
literal|"\r\n"
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
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
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
name|v
operator|=
name|headers
operator|.
name|get
argument_list|(
literal|"Content-Type"
argument_list|)
expr_stmt|;
if|if
condition|(
name|v
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"Content-Type: "
argument_list|)
operator|.
name|append
argument_list|(
name|v
argument_list|)
operator|.
name|append
argument_list|(
name|CRLF
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|CRLF
argument_list|)
expr_stmt|;
name|byte
index|[]
name|hb
init|=
name|sb
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|()
decl_stmt|;
name|byte
index|[]
name|longdata
init|=
operator|new
name|byte
index|[
name|hb
operator|.
name|length
operator|+
name|length
index|]
decl_stmt|;
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
index|]
decl_stmt|;
name|longdata
index|[
literal|0
index|]
operator|=
literal|0x0d
expr_stmt|;
name|longdata
index|[
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
literal|2
argument_list|,
name|length
argument_list|)
expr_stmt|;
return|return
name|longdata
return|;
block|}
block|}
end_class

end_unit

