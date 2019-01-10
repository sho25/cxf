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
name|client
package|;
end_package

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
name|SystemPropertyAction
import|;
end_import

begin_interface
specifier|public
interface|interface
name|ClientProperties
block|{
name|String
name|HTTP_CONNECTION_TIMEOUT_PROP
init|=
literal|"http.connection.timeout"
decl_stmt|;
name|String
name|HTTP_RECEIVE_TIMEOUT_PROP
init|=
literal|"http.receive.timeout"
decl_stmt|;
name|String
name|HTTP_PROXY_SERVER_PROP
init|=
literal|"http.proxy.server.uri"
decl_stmt|;
name|String
name|HTTP_PROXY_SERVER_PORT_PROP
init|=
literal|"http.proxy.server.port"
decl_stmt|;
name|String
name|HTTP_AUTOREDIRECT_PROP
init|=
literal|"http.autoredirect"
decl_stmt|;
name|String
name|HTTP_MAINTAIN_SESSION_PROP
init|=
literal|"http.maintain.session"
decl_stmt|;
name|String
name|HTTP_RESPONSE_AUTOCLOSE_PROP
init|=
literal|"http.response.stream.auto.close"
decl_stmt|;
name|String
name|THREAD_SAFE_CLIENT_PROP
init|=
literal|"thread.safe.client"
decl_stmt|;
name|String
name|THREAD_SAFE_CLIENT_STATE_CLEANUP_PROP
init|=
literal|"thread.safe.client.state.cleanup.period"
decl_stmt|;
name|Boolean
name|DEFAULT_THREAD_SAFETY_CLIENT_STATUS
init|=
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|SystemPropertyAction
operator|.
name|getPropertyOrNull
argument_list|(
name|THREAD_SAFE_CLIENT_PROP
argument_list|)
argument_list|)
decl_stmt|;
empty_stmt|;
name|Integer
name|THREAD_SAFE_CLIENT_STATE_CLEANUP_PERIOD
init|=
name|getIntValue
argument_list|(
name|SystemPropertyAction
operator|.
name|getPropertyOrNull
argument_list|(
name|THREAD_SAFE_CLIENT_STATE_CLEANUP_PROP
argument_list|)
argument_list|)
decl_stmt|;
specifier|static
name|Integer
name|getIntValue
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|o
operator|instanceof
name|Integer
condition|?
operator|(
name|Integer
operator|)
name|o
else|:
name|o
operator|instanceof
name|String
condition|?
name|Integer
operator|.
name|valueOf
argument_list|(
name|o
operator|.
name|toString
argument_list|()
argument_list|)
else|:
literal|null
return|;
block|}
block|}
end_interface

end_unit
