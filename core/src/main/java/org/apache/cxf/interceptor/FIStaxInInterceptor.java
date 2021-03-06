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
name|interceptor
package|;
end_package

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
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|xml
operator|.
name|fastinfoset
operator|.
name|stax
operator|.
name|StAXDocumentParser
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
comment|/**  * Creates an XMLStreamReader from the InputStream on the Message.  */
end_comment

begin_class
specifier|public
class|class
name|FIStaxInInterceptor
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
name|FI_GET_SUPPORTED
init|=
literal|"org.apache.cxf.fastinfoset.get.supported"
decl_stmt|;
specifier|public
name|FIStaxInInterceptor
parameter_list|()
block|{
name|this
argument_list|(
name|Phase
operator|.
name|POST_STREAM
argument_list|)
expr_stmt|;
block|}
specifier|public
name|FIStaxInInterceptor
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
name|addBefore
argument_list|(
name|StaxInInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|boolean
name|isRequestor
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|message
operator|.
name|containsKey
argument_list|(
name|Message
operator|.
name|REQUESTOR_ROLE
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|StAXDocumentParser
name|getParser
parameter_list|(
name|InputStream
name|in
parameter_list|)
block|{
name|StAXDocumentParser
name|parser
init|=
operator|new
name|StAXDocumentParser
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|parser
operator|.
name|setStringInterning
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|parser
operator|.
name|setForceStreamClose
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|parser
operator|.
name|setInputStream
argument_list|(
name|in
argument_list|)
expr_stmt|;
return|return
name|parser
return|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|message
operator|.
name|getContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
operator|!=
literal|null
operator|||
operator|!
name|isHttpVerbSupported
argument_list|(
name|message
argument_list|)
condition|)
block|{
return|return;
block|}
name|String
name|ct
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
name|CONTENT_TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|ct
operator|!=
literal|null
operator|&&
name|ct
operator|.
name|indexOf
argument_list|(
literal|"fastinfoset"
argument_list|)
operator|!=
operator|-
literal|1
operator|&&
name|message
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
operator|!=
literal|null
operator|&&
name|message
operator|.
name|getContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
operator|==
literal|null
condition|)
block|{
name|message
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|getParser
argument_list|(
name|message
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|//add the StaxInEndingInterceptor which will close the reader
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|add
argument_list|(
name|StaxInEndingInterceptor
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|ct
operator|=
name|ct
operator|.
name|replace
argument_list|(
literal|"fastinfoset"
argument_list|,
literal|"xml"
argument_list|)
expr_stmt|;
if|if
condition|(
name|ct
operator|.
name|contains
argument_list|(
literal|"application/xml"
argument_list|)
condition|)
block|{
name|ct
operator|=
name|ct
operator|.
name|replace
argument_list|(
literal|"application/xml"
argument_list|,
literal|"text/xml"
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
name|ct
argument_list|)
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|FIStaxOutInterceptor
operator|.
name|FI_ENABLED
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
if|if
condition|(
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
comment|//record the fact that is worked so future requests will
comment|//automatically be FI enabled
name|Endpoint
name|ep
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|ep
operator|.
name|put
argument_list|(
name|FIStaxOutInterceptor
operator|.
name|FI_ENABLED
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|boolean
name|isHttpVerbSupported
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|isGET
argument_list|(
name|message
argument_list|)
condition|)
block|{
return|return
name|isRequestor
argument_list|(
name|message
argument_list|)
operator|&&
name|MessageUtils
operator|.
name|getContextualBoolean
argument_list|(
name|message
argument_list|,
name|FI_GET_SUPPORTED
argument_list|,
literal|false
argument_list|)
return|;
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

