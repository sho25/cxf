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
name|frontend
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|w3c
operator|.
name|dom
operator|.
name|Document
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
name|binding
operator|.
name|soap
operator|.
name|interceptor
operator|.
name|EndpointSelectionInterceptor
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
name|common
operator|.
name|util
operator|.
name|UrlUtils
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
name|interceptor
operator|.
name|Interceptor
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
name|MessageSenderInterceptor
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
name|OutgoingChainInterceptor
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
name|StaxOutInterceptor
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
name|common
operator|.
name|gzip
operator|.
name|GZIPOutInterceptor
import|;
end_import

begin_class
specifier|public
class|class
name|WSDLGetInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|public
specifier|static
specifier|final
name|WSDLGetInterceptor
name|INSTANCE
init|=
operator|new
name|WSDLGetInterceptor
argument_list|()
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DOCUMENT_HOLDER
init|=
name|WSDLGetInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".documentHolder"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TRANSFORM_SKIP
init|=
literal|"transform.skip"
decl_stmt|;
specifier|private
name|Interceptor
argument_list|<
name|Message
argument_list|>
name|wsdlGetOutInterceptor
init|=
name|WSDLGetOutInterceptor
operator|.
name|INSTANCE
decl_stmt|;
specifier|public
name|WSDLGetInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|READ
argument_list|)
expr_stmt|;
name|getAfter
argument_list|()
operator|.
name|add
argument_list|(
name|EndpointSelectionInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|WSDLGetInterceptor
parameter_list|(
name|Interceptor
argument_list|<
name|Message
argument_list|>
name|outInterceptor
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
comment|// Let people override the wsdlGetOutInterceptor
name|wsdlGetOutInterceptor
operator|=
name|outInterceptor
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
name|method
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
name|HTTP_REQUEST_METHOD
argument_list|)
decl_stmt|;
name|String
name|query
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
if|if
condition|(
operator|!
literal|"GET"
operator|.
name|equals
argument_list|(
name|method
argument_list|)
operator|||
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|query
argument_list|)
condition|)
block|{
return|return;
block|}
name|String
name|baseUri
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
name|REQUEST_URL
argument_list|)
decl_stmt|;
name|String
name|ctx
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
name|PATH_INFO
argument_list|)
decl_stmt|;
name|WSDLGetUtils
name|utils
init|=
operator|(
name|WSDLGetUtils
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|WSDLGetUtils
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|utils
operator|==
literal|null
condition|)
block|{
name|utils
operator|=
operator|new
name|WSDLGetUtils
argument_list|()
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|WSDLGetUtils
operator|.
name|class
argument_list|,
name|utils
argument_list|)
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|UrlUtils
operator|.
name|parseQueryString
argument_list|(
name|query
argument_list|)
decl_stmt|;
if|if
condition|(
name|isRecognizedQuery
argument_list|(
name|map
argument_list|)
condition|)
block|{
name|Document
name|doc
init|=
name|getDocument
argument_list|(
name|utils
argument_list|,
name|message
argument_list|,
name|baseUri
argument_list|,
name|map
argument_list|,
name|ctx
argument_list|)
decl_stmt|;
name|Endpoint
name|e
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|Message
name|mout
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|mout
operator|.
name|setExchange
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
name|mout
operator|=
name|e
operator|.
name|getBinding
argument_list|()
operator|.
name|createMessage
argument_list|(
name|mout
argument_list|)
expr_stmt|;
name|mout
operator|.
name|setInterceptorChain
argument_list|(
name|OutgoingChainInterceptor
operator|.
name|getOutInterceptorChain
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|setOutMessage
argument_list|(
name|mout
argument_list|)
expr_stmt|;
name|mout
operator|.
name|put
argument_list|(
name|DOCUMENT_HOLDER
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|mout
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"text/xml"
argument_list|)
expr_stmt|;
comment|// just remove the interceptor which should not be used
name|cleanUpOutInterceptors
argument_list|(
name|mout
argument_list|)
expr_stmt|;
comment|// notice this is being added after the purge above, don't swap the order!
name|mout
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|add
argument_list|(
name|wsdlGetOutInterceptor
argument_list|)
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|TRANSFORM_SKIP
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
comment|// skip the service executor and goto the end of the chain.
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|doInterceptStartingAt
argument_list|(
name|message
argument_list|,
name|OutgoingChainInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|cleanUpOutInterceptors
parameter_list|(
name|Message
name|outMessage
parameter_list|)
block|{
comment|// TODO - how can I improve this to provide a specific interceptor chain that just has the
comment|// stax, gzip and message sender components, while also ensuring that GZIP is only provided
comment|// if its already configured for the endpoint.
name|Iterator
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|iterator
init|=
name|outMessage
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
name|inInterceptor
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|inInterceptor
operator|.
name|getClass
argument_list|()
operator|.
name|equals
argument_list|(
name|StaxOutInterceptor
operator|.
name|class
argument_list|)
operator|&&
operator|!
name|inInterceptor
operator|.
name|getClass
argument_list|()
operator|.
name|equals
argument_list|(
name|GZIPOutInterceptor
operator|.
name|class
argument_list|)
operator|&&
operator|!
name|inInterceptor
operator|.
name|getClass
argument_list|()
operator|.
name|equals
argument_list|(
name|MessageSenderInterceptor
operator|.
name|class
argument_list|)
condition|)
block|{
name|outMessage
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|remove
argument_list|(
name|inInterceptor
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|Document
name|getDocument
parameter_list|(
name|WSDLGetUtils
name|utils
parameter_list|,
name|Message
name|message
parameter_list|,
name|String
name|base
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|,
name|String
name|ctxUri
parameter_list|)
block|{
comment|// cannot have two wsdl's being generated for the same endpoint at the same
comment|// time as the addresses may get mixed up
comment|// For WSDL's the WSDLWriter does not share any state between documents.
comment|// For XSD's, the WSDLGetUtils makes a copy of any XSD schema documents before updating
comment|// any addresses and returning them, so for both WSDL and XSD this is the only part that needs
comment|// to be synchronized.
synchronized|synchronized
init|(
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getEndpoint
argument_list|()
init|)
block|{
return|return
name|utils
operator|.
name|getDocument
argument_list|(
name|message
argument_list|,
name|base
argument_list|,
name|params
argument_list|,
name|ctxUri
argument_list|,
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
argument_list|)
return|;
block|}
block|}
specifier|private
name|boolean
name|isRecognizedQuery
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
parameter_list|)
block|{
return|return
name|map
operator|.
name|containsKey
argument_list|(
literal|"wsdl"
argument_list|)
operator|||
name|map
operator|.
name|containsKey
argument_list|(
literal|"xsd"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

