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
name|systest
operator|.
name|https
operator|.
name|conduit
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
name|OutputStream
import|;
end_import

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
name|common
operator|.
name|util
operator|.
name|Base64Utility
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
name|endpoint
operator|.
name|Endpoint
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
name|interceptor
operator|.
name|Fault
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
name|Exchange
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
name|MessageImpl
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
name|phase
operator|.
name|AbstractPhaseInterceptor
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
name|phase
operator|.
name|Phase
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
name|transport
operator|.
name|Conduit
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
name|transport
operator|.
name|http
operator|.
name|Headers
import|;
end_import

begin_comment
comment|/*  * This interceptor will issue 401s  *    No Authorization Header  --> 401 Realm=Cronus  *    Username Mary            --> 401 Realm=Andromeda  *    Username Edward          --> 401 Realm=Zorantius  *    Username George          --> 401 Realm=Cronus  *    If the password is not "password" a 401 is issued without   *    realm.  */
end_comment

begin_class
specifier|public
class|class
name|PushBack401
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
name|PushBack401
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|RECEIVE
argument_list|)
expr_stmt|;
block|}
comment|/**      * This function extracts the user:pass token from       * the Authorization:Basic header. It returns a two element      * String array, the first being the userid, the second      * being the password. It returns null, if it cannot parse.      */
specifier|private
name|String
index|[]
name|extractUserPass
parameter_list|(
name|String
name|token
parameter_list|)
block|{
try|try
block|{
name|byte
index|[]
name|userpass
init|=
name|Base64Utility
operator|.
name|decode
argument_list|(
name|token
argument_list|)
decl_stmt|;
name|String
name|up
init|=
name|IOUtils
operator|.
name|newStringFromBytes
argument_list|(
name|userpass
argument_list|)
decl_stmt|;
name|String
name|user
init|=
name|up
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|up
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|pass
init|=
name|up
operator|.
name|substring
argument_list|(
name|up
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
operator|+
literal|1
argument_list|)
decl_stmt|;
return|return
operator|new
name|String
index|[]
block|{
name|user
block|,
name|pass
block|}
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * This function returns the realm which depends on       * the user name, as follows:      *<pre>      *    Username Mary            --> Andromeda      *    Username Edward          --> Zorantius      *    Username George          --> Cronus      *</pre>      * However, if the password is not "password" this function       * throws an exception, regardless.      */
specifier|private
name|String
name|checkUserPass
parameter_list|(
name|String
name|user
parameter_list|,
name|String
name|pass
parameter_list|)
throws|throws
name|Exception
block|{
comment|//System.out.println("Got user: " + user + " pass: " + pass);
if|if
condition|(
operator|!
literal|"password"
operator|.
name|equals
argument_list|(
name|pass
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"bad password"
argument_list|)
throw|;
block|}
if|if
condition|(
literal|"Mary"
operator|.
name|equals
argument_list|(
name|user
argument_list|)
condition|)
block|{
return|return
literal|"Andromeda"
return|;
block|}
if|if
condition|(
literal|"Edward"
operator|.
name|equals
argument_list|(
name|user
argument_list|)
condition|)
block|{
return|return
literal|"Zorantius"
return|;
block|}
if|if
condition|(
literal|"George"
operator|.
name|equals
argument_list|(
name|user
argument_list|)
condition|)
block|{
return|return
literal|"Cronus"
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|auth
init|=
name|headers
operator|.
name|get
argument_list|(
literal|"Authorization"
argument_list|)
decl_stmt|;
if|if
condition|(
name|auth
operator|==
literal|null
condition|)
block|{
comment|// No Auth Header, respond with 401 Realm=Cronus
name|replyUnauthorized
argument_list|(
name|message
argument_list|,
literal|"Cronus"
argument_list|)
expr_stmt|;
return|return;
block|}
else|else
block|{
for|for
control|(
name|String
name|a
range|:
name|auth
control|)
block|{
if|if
condition|(
name|a
operator|.
name|startsWith
argument_list|(
literal|"Basic "
argument_list|)
condition|)
block|{
name|String
index|[]
name|userpass
init|=
name|extractUserPass
argument_list|(
name|a
operator|.
name|substring
argument_list|(
literal|"Basic "
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|userpass
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|String
name|realm
init|=
name|checkUserPass
argument_list|(
name|userpass
index|[
literal|0
index|]
argument_list|,
name|userpass
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|realm
operator|!=
literal|null
condition|)
block|{
name|replyUnauthorized
argument_list|(
name|message
argument_list|,
name|realm
argument_list|)
expr_stmt|;
return|return;
block|}
else|else
block|{
comment|// Password is good and no realm
comment|// We just return for successful fall thru.
return|return;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// Bad Password
name|replyUnauthorized
argument_list|(
name|message
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
block|}
comment|// No Authorization: Basic
name|replyUnauthorized
argument_list|(
name|message
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
comment|/**      * This function issues a 401 response back down the conduit.      * If the realm is not null, a WWW-Authenticate: Basic realm=      * header is sent. The interceptor chain is aborted stopping      * the Message from going to the servant.      */
specifier|private
name|void
name|replyUnauthorized
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|realm
parameter_list|)
block|{
name|Message
name|outMessage
init|=
name|getOutMessage
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|outMessage
operator|.
name|put
argument_list|(
name|Message
operator|.
name|RESPONSE_CODE
argument_list|,
name|HttpURLConnection
operator|.
name|HTTP_UNAUTHORIZED
argument_list|)
expr_stmt|;
if|if
condition|(
name|realm
operator|!=
literal|null
condition|)
block|{
name|setHeader
argument_list|(
name|outMessage
argument_list|,
literal|"WWW-Authenticate"
argument_list|,
literal|"Basic realm="
operator|+
name|realm
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|abort
argument_list|()
expr_stmt|;
try|try
block|{
name|getConduit
argument_list|(
name|message
argument_list|)
operator|.
name|prepare
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
name|close
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|//System.out.println("Prepare of message not working." + e);
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Retrieves/creates the corresponding Outbound Message.      */
specifier|private
name|Message
name|getOutMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|Message
name|outMessage
init|=
name|exchange
operator|.
name|getOutMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|outMessage
operator|==
literal|null
condition|)
block|{
name|Endpoint
name|endpoint
init|=
name|exchange
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|outMessage
operator|=
operator|new
name|MessageImpl
argument_list|()
expr_stmt|;
name|outMessage
operator|.
name|putAll
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|outMessage
operator|.
name|remove
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
expr_stmt|;
name|outMessage
operator|.
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|outMessage
operator|=
name|endpoint
operator|.
name|getBinding
argument_list|()
operator|.
name|createMessage
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setOutMessage
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
block|}
return|return
name|outMessage
return|;
block|}
comment|/**      * This function sets the header in the PROTOCO_HEADERS of      * the message.      */
specifier|private
name|void
name|setHeader
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|responseHeaders
init|=
name|Headers
operator|.
name|getSetProtocolHeaders
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|responseHeaders
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
name|value
block|}
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This method retrieves/creates the conduit for the response      * message.      */
specifier|private
name|Conduit
name|getConduit
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|IOException
block|{
name|Exchange
name|exchange
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|Conduit
name|conduit
init|=
name|exchange
operator|.
name|getDestination
argument_list|()
operator|.
name|getBackChannel
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setConduit
argument_list|(
name|conduit
argument_list|)
expr_stmt|;
return|return
name|conduit
return|;
block|}
comment|/**      * This method closes the output stream associated with the      * message.      */
specifier|private
name|void
name|close
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|IOException
block|{
name|OutputStream
name|os
init|=
name|message
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|os
operator|.
name|flush
argument_list|()
expr_stmt|;
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

