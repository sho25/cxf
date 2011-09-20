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
name|http
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|HttpURLConnection
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
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|message
operator|.
name|MessageUtils
import|;
end_import

begin_class
specifier|public
class|class
name|Cookies
block|{
comment|/**      * Variables for holding session state if sessions are supposed to be maintained      */
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Cookie
argument_list|>
name|sessionCookies
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|Cookie
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|maintainSession
decl_stmt|;
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Cookie
argument_list|>
name|getSessionCookies
parameter_list|()
block|{
return|return
name|sessionCookies
return|;
block|}
specifier|public
name|void
name|readFromConnection
parameter_list|(
name|HttpURLConnection
name|connection
parameter_list|)
block|{
if|if
condition|(
name|maintainSession
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|h
range|:
name|connection
operator|.
name|getHeaderFields
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
literal|"Set-Cookie"
operator|.
name|equalsIgnoreCase
argument_list|(
name|h
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|handleSetCookie
argument_list|(
name|h
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|public
name|void
name|writeToMessageHeaders
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
comment|//Do we need to maintain a session?
name|maintainSession
operator|=
name|MessageUtils
operator|.
name|getContextualBoolean
argument_list|(
name|message
argument_list|,
name|Message
operator|.
name|MAINTAIN_SESSION
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|//If we have any cookies and we are maintaining sessions, then use them
if|if
condition|(
name|maintainSession
operator|&&
name|sessionCookies
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
operator|new
name|Headers
argument_list|(
name|message
argument_list|)
operator|.
name|writeSessionCookies
argument_list|(
name|sessionCookies
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Given a list of current cookies and a new Set-Cookie: request, construct      * a new set of current cookies and return it.      * @param current Set of previously set cookies      * @param header Text of a Set-Cookie: header      * @return New set of cookies      */
specifier|private
name|void
name|handleSetCookie
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|headers
parameter_list|)
block|{
if|if
condition|(
name|headers
operator|==
literal|null
operator|||
name|headers
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
for|for
control|(
name|String
name|header
range|:
name|headers
control|)
block|{
name|String
index|[]
name|cookies
init|=
name|header
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|cookie
range|:
name|cookies
control|)
block|{
name|String
index|[]
name|parts
init|=
name|cookie
operator|.
name|split
argument_list|(
literal|";"
argument_list|)
decl_stmt|;
name|String
index|[]
name|kv
init|=
name|parts
index|[
literal|0
index|]
operator|.
name|split
argument_list|(
literal|"="
argument_list|,
literal|2
argument_list|)
decl_stmt|;
if|if
condition|(
name|kv
operator|.
name|length
operator|!=
literal|2
condition|)
block|{
continue|continue;
block|}
name|String
name|name
init|=
name|kv
index|[
literal|0
index|]
operator|.
name|trim
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|kv
index|[
literal|1
index|]
operator|.
name|trim
argument_list|()
decl_stmt|;
name|Cookie
name|newCookie
init|=
operator|new
name|Cookie
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|parts
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|kv
operator|=
name|parts
index|[
name|i
index|]
operator|.
name|split
argument_list|(
literal|"="
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|name
operator|=
name|kv
index|[
literal|0
index|]
operator|.
name|trim
argument_list|()
expr_stmt|;
name|value
operator|=
operator|(
name|kv
operator|.
name|length
operator|>
literal|1
operator|)
condition|?
name|kv
index|[
literal|1
index|]
operator|.
name|trim
argument_list|()
else|:
literal|null
expr_stmt|;
if|if
condition|(
name|name
operator|.
name|equalsIgnoreCase
argument_list|(
name|Cookie
operator|.
name|DISCARD_ATTRIBUTE
argument_list|)
condition|)
block|{
name|newCookie
operator|.
name|setMaxAge
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|name
operator|.
name|equalsIgnoreCase
argument_list|(
name|Cookie
operator|.
name|MAX_AGE_ATTRIBUTE
argument_list|)
operator|&&
name|value
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|newCookie
operator|.
name|setMaxAge
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
comment|// do nothing here
block|}
block|}
elseif|else
if|if
condition|(
name|name
operator|.
name|equalsIgnoreCase
argument_list|(
name|Cookie
operator|.
name|PATH_ATTRIBUTE
argument_list|)
operator|&&
name|value
operator|!=
literal|null
condition|)
block|{
name|newCookie
operator|.
name|setPath
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|newCookie
operator|.
name|getMaxAge
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|sessionCookies
operator|.
name|put
argument_list|(
name|newCookie
operator|.
name|getName
argument_list|()
argument_list|,
name|newCookie
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sessionCookies
operator|.
name|remove
argument_list|(
name|newCookie
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

