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
operator|.
name|jsonp
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
name|jaxrs
operator|.
name|utils
operator|.
name|JAXRSUtils
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

begin_comment
comment|/**  * Sets a callback key in the message exchange for HTTP requests containing the '_jsonp' parameter in the  * querystring.  */
end_comment

begin_class
specifier|public
class|class
name|JsonpInInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|public
specifier|static
specifier|final
name|String
name|JSONP_TYPE
init|=
literal|"application/x-javascript"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CALLBACK_PARAM
init|=
literal|"_jsonp"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CALLBACK_KEY
init|=
literal|"JSONP.CALLBACK"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_CALLBACK_VALUE
init|=
literal|"callback"
decl_stmt|;
specifier|private
name|String
name|callbackParam
init|=
name|CALLBACK_PARAM
decl_stmt|;
specifier|private
name|String
name|defaultCallback
init|=
name|DEFAULT_CALLBACK_VALUE
decl_stmt|;
specifier|private
name|String
name|acceptType
decl_stmt|;
specifier|private
name|String
name|mediaType
init|=
name|JSONP_TYPE
decl_stmt|;
specifier|public
name|JsonpInInterceptor
parameter_list|()
block|{
name|this
argument_list|(
name|Phase
operator|.
name|UNMARSHAL
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JsonpInInterceptor
parameter_list|(
name|String
name|phase
parameter_list|)
block|{
name|super
argument_list|(
name|phase
argument_list|)
expr_stmt|;
block|}
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
name|String
name|callbackValue
init|=
name|getCallbackValue
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|callbackValue
argument_list|)
condition|)
block|{
if|if
condition|(
name|getAcceptType
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// may be needed to enforce the selection of
comment|// JSON-awarenprovider
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|ACCEPT_CONTENT_TYPE
argument_list|,
name|getAcceptType
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|CALLBACK_KEY
argument_list|,
name|callbackValue
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|String
name|getCallbackValue
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|String
name|theQuery
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|QUERY_STRING
argument_list|)
decl_stmt|;
name|String
name|callback
init|=
name|JAXRSUtils
operator|.
name|getStructuredParams
argument_list|(
name|theQuery
argument_list|,
literal|"&"
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
operator|.
name|getFirst
argument_list|(
name|CALLBACK_KEY
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|callback
argument_list|)
condition|)
block|{
name|String
name|httpAcceptType
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|ACCEPT_CONTENT_TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|httpAcceptType
operator|!=
literal|null
operator|&&
name|mediaType
operator|.
name|equals
argument_list|(
name|httpAcceptType
argument_list|)
condition|)
block|{
return|return
name|defaultCallback
return|;
block|}
block|}
return|return
name|callback
return|;
block|}
specifier|public
name|void
name|setCallbackParam
parameter_list|(
name|String
name|callbackParam
parameter_list|)
block|{
name|this
operator|.
name|callbackParam
operator|=
name|callbackParam
expr_stmt|;
block|}
specifier|public
name|String
name|getCallbackParam
parameter_list|()
block|{
return|return
name|callbackParam
return|;
block|}
specifier|public
name|void
name|setAcceptType
parameter_list|(
name|String
name|acceptType
parameter_list|)
block|{
name|this
operator|.
name|acceptType
operator|=
name|acceptType
expr_stmt|;
block|}
specifier|public
name|String
name|getAcceptType
parameter_list|()
block|{
return|return
name|acceptType
return|;
block|}
specifier|public
name|void
name|setMediaType
parameter_list|(
name|String
name|mediaType
parameter_list|)
block|{
name|this
operator|.
name|mediaType
operator|=
name|mediaType
expr_stmt|;
block|}
specifier|public
name|String
name|getMediaType
parameter_list|()
block|{
return|return
name|mediaType
return|;
block|}
specifier|public
name|void
name|setDefaultCallback
parameter_list|(
name|String
name|defaultCallback
parameter_list|)
block|{
name|this
operator|.
name|defaultCallback
operator|=
name|defaultCallback
expr_stmt|;
block|}
specifier|public
name|String
name|getDefaultCallback
parameter_list|()
block|{
return|return
name|defaultCallback
return|;
block|}
block|}
end_class

end_unit

